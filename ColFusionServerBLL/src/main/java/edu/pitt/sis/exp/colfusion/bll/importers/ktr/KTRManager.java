/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll.importers.ktr;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.util.CellReference;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoTableKTRManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoTableKTRManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoTableKtr;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoTableKtrId;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryTargetDBViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetViewModel;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.IOUtils;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;
import edu.pitt.sis.exp.colfusion.utils.ResourceUtils;
import edu.pitt.sis.exp.colfusion.utils.StoryUtils;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;

/**
 * The class to manage one KTR file or files if uploaded file has more than one sheet: create, read, write, modify.
 * 
 * @author Evgeny
 *
 */
public class KTRManager {
	final Logger logger = LogManager.getLogger(KTRManager.class.getName());
	
	private int sid;
	
	private Document ktrDocument;

	private String ktrAbsoluteName;
	
	public KTRManager(final int sid) {
		setSid(sid);
	}
	
	/**
	 * Create KTR(s) file from the template file (one ktr file for one worksheet), and populate the ktr file with information from the {@link FileContentInfoViewModel} information.
	 * The function should be called for each file separately. As the result a new ktr file will be created for each sheet in the file.
	 * 
	 * If the otherFiles field is not empty, then those files should have exactly the same structure as the main file. The ktr template will then 
	 * list other files with exactly the same properties for sheets and as the result the data from all file will be appended.
	 * 
	 * The KTR files locations are also saved to database at this point.
	 * @param file
	 * @throws Exception 
	 */
	public void createTemplate(final FileContentInfoViewModel file) throws Exception {
		
		logger.info(String.format("Starting to create KTR file(s) for %s sid", sid));
		
		ArrayList<String> filesAbsoluteNames = new ArrayList<>();
		filesAbsoluteNames.add(file.getFileAbsoluteName());
		if (file.getOtherFilesAbsoluteNames() != null) {
			for (String fileName : file.getOtherFilesAbsoluteNames()) {
				filesAbsoluteNames.add(fileName);
			}
		}
		
		for(WorksheetViewModel worksheet : file.getWorksheets()) {
			
			String tableName = worksheet.getUniqueShortName();
			
			IOUtilsStoredFileInfoModel copiedKTRFileInfo = createKTRFileFromTemplate(sid, file.getExtension(), tableName);  
			
			//TODO: Maybe this functionality should be separated from this code, so to make it less dependent on the database
			saveKTRFileLocationToDB(sid, tableName, copiedKTRFileInfo.getAbsoluteFileName());
			
			fillKTRFile(copiedKTRFileInfo.getAbsoluteFileName(), file.getExtension(), filesAbsoluteNames, worksheet, tableName);
		}		
	}

	/**
	 * Fills all parts of the KTR file with user provided info. After this function KTR file is ready to be executed.
	 * 
	 * @param ktrAbsoluteName the absolute KTR file location.
	 * @param filesAbsoluteNames the names of the files from which to read the data (ALl files must have the same structure).
	 * @param worksheet - what sheet to import.
	 * @param tableName the name of the table into which the data from the worksheet will be imported.
	 * @throws Exception 
	 */
	private void fillKTRFile(final String ktrAbsoluteName, final String dataFileExtension, final ArrayList<String> filesAbsoluteNames, final WorksheetViewModel worksheet, 
			final String tableName) 
			throws Exception {
		
		if (ktrDocument == null) {
			loadKTR(ktrAbsoluteName);
		}
		
		ktrDocumentChangeConnection();
		
		//TODO: don't use string, use enum
		if (!dataFileExtension.equals("csv")) {
			ktrDocumentAddSheets(worksheet);
		}
		
		ktrDocumentAddFiles(dataFileExtension, filesAbsoluteNames);
		ktrDocumentAddVariablesIntoInputInputSourceStep(dataFileExtension, worksheet.getVariables());

		//select values step into ktr document
		ktrDocumentSelectValues(dataFileExtension, worksheet.getVariables());
		
		//add constant value step
		ktrDocumentConstantValues(dataFileExtension, worksheet.getVariables());
				
		ktrDocumentAddTableNameIntoTargetSchemaStep(tableName);
		ktrDocumentAddVariablesIntoTargetSchemaStep(worksheet.getVariables());
		
		ktrDocumentChangeTransformationName(String.format("%d_%s", getSid(), tableName));
		
		saveKTRFile();
	}

	/**
	 * Saves the KTR document from memory to file.
	 * 
	 * @param ktrAbsoluteName the absolute file name where to save KTR document.
	 * @throws TransformerException
	 */
	private void saveKTRFile() throws TransformerException {
		// write the content into xml file
		IOUtils.writeXMLDocument(ktrDocument, ktrAbsoluteName);
	}
	
	/**
	 * Set the transformation name of the given KTR document to a given name.
	 * @param transformationName the name to set.
	 * @throws Exception
	 */
	private void ktrDocumentChangeTransformationName(final String transformationName) throws Exception {
		logger.info("starting change transformation name in the KTR document");
 		
 		//XPath to get fields node to populate with selected variables
 		String expression = "/transformation/info/name";
 		
 		XPath xPath =  XPathFactory.newInstance().newXPath();
 		NodeList nameTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
 		
 		if (nameTag.getLength() == 0) {
 			logger.error(String.format("nameTag failed, no /transformation/info/name tag "));
 			
 			throw new Exception("nameTag failed, no /transformation/info/name tag");
 		}
 		
 		Node nameNode = nameTag.item(0);
 		
 		removeAllChildNodes(nameNode);
 		
 		nameNode.appendChild(ktrDocument.createTextNode(transformationName));
 		
 		logger.info("finished change transformation name in the KTR document");		
	}

	/**
	 * Removes all child nodes from the given node of the XML {@link Document}
	 * @param node parent node from which the children should be deleted.
	 */
	public void removeAllChildNodes(final Node node) 
    {
		NodeList children = node.getChildNodes();
		
		for (int i = children.getLength() - 1; i >= 0; i--) {
			node.removeChild(children.item(i));
		}
    }
	
	/**
	 * Adds table tag into KTR file into Target Schema step with name of the table in the database where the data should be imported.
	 * 
	 * @param tableName the name of the table.
	 * @throws Exception
	 */
	private void ktrDocumentAddTableNameIntoTargetSchemaStep(final String tableName) throws Exception {
		logger.info("starting add table name into KTR document into Target Schema step");
		
		//XPath to get fields node to populate with selected variables
		String expression = "/transformation/step[name = 'Target Schema']";
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList stepTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (stepTag.getLength() == 0) {
			logger.error(String.format("fieldsTag failed, no /transformation/step[name = 'Target Schema'] tag "));
			
			throw new Exception("fieldsTag failed, no /transformation/step[name = 'Target Schema'] tag");
		}
		
		Node stepNode = stepTag.item(0);
		
		Element table = ktrDocument.createElement("table");
		table.appendChild(ktrDocument.createTextNode(tableName));

		stepNode.appendChild(table);
		
		logger.info("finished add table name into KTR document into TargetSchema step");
	}

	/**
	 * 
	 * Populates fields tag in the Target Schema step with variables information.
	 * 
	 * @param variables the info about variables
	 * @throws Exception
	 */
	private void ktrDocumentAddVariablesIntoTargetSchemaStep(final ArrayList<DatasetVariableViewModel> variables) throws Exception {
		logger.info("starting add variables into KTR document into Target Schema step");
		
		//XPath to get fields node to populate with selected variables
		String expression = "/transformation/step[name = 'Target Schema']/fields"; //Target Schema
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList fieldsTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (fieldsTag.getLength() == 0) {
			logger.error(String.format("fieldsTag failed, no /transformation/step[name = 'Target Schema']/fields tag "));
			
			throw new Exception("fieldsTag failed, no /transformation/step[name = 'Target Schema']/fields tag");
		}
		
		Node fieldsNode = fieldsTag.item(0);
		
		for(DatasetVariableViewModel variable : variables) {
			
			if (!variable.isChecked()) { //Add only those variables that user selected.
				continue;
			}
			
			Element field = ktrDocument.createElement("field");
			
			Element columnName = ktrDocument.createElement("column_name");
			columnName.appendChild(ktrDocument.createTextNode(String.format("%s", variable.getOriginalName())));
			
			Element streamName = ktrDocument.createElement("stream_name");
			//stream name for the final step checking if constant column or not
			if(!variable.getIsConstant())
			streamName.appendChild(ktrDocument.createTextNode(variable.getOriginalName()));
			else
			streamName.appendChild(ktrDocument.createTextNode(variable.getOriginalName()+"_constant"));
			
			field.appendChild(columnName);
			field.appendChild(streamName);
				
			fieldsNode.appendChild(field);
		}
		
		logger.info("finished add variables into KTR document into TargetSchema step");
	}
	/**
	 * Populates fields tag in the Constant Value step with variables information.
	 * 
	 * @param dataFileExtension extension of the data file.
	 * @param variables the info about variables
	 * @throws Exception
	 */
	private void ktrDocumentConstantValues(final String dataFileExtension, final ArrayList<DatasetVariableViewModel> variables) throws Exception {
		
		logger.info("starting select variables into KTR document into SelectValues step");
		
		//TODO: the CSV type as a string is not good, should be an enum.
		String stepName = dataFileExtension.equals("csv") ? "Add constants" : "Excel Input File";
		
		//XPath to get fields node to populate with selected variables
		String expression = String.format("/transformation/step[name = '%s']/fields", stepName);
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList fieldsTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (fieldsTag.getLength() == 0) {
			logger.error(String.format("fieldsTag failed, no /trasformation/step/fields tag "));
			
			throw new Exception("fieldsTag failed, no /trasformation/step/fields tag");
		}
		
		Node fieldsNode = fieldsTag.item(0);
		
		for(DatasetVariableViewModel variable : variables) {
			
			if (!variable.isChecked()) {
				continue;
			}
			
			if(variable.getIsConstant())
			{
			
			Element field = ktrDocument.createElement("field");
			
			Element name = ktrDocument.createElement("name");
			name.appendChild(ktrDocument.createTextNode(variable.getOriginalName()+"_constant"));

			Element type = ktrDocument.createElement("type");
			type.appendChild(ktrDocument.createTextNode("String"));
		
			Element length = ktrDocument.createElement("length");
			length.appendChild(ktrDocument.createTextNode("-1"));
			
			Element precision = ktrDocument.createElement("precision");
			precision.appendChild(ktrDocument.createTextNode("-1"));
			
			Element nullif = ktrDocument.createElement("nullif");
			nullif.appendChild(ktrDocument.createTextNode(variable.getConstantValue()));
			
			field.appendChild(name);
			field.appendChild(type);
			field.appendChild(length);
			field.appendChild(precision);
			field.appendChild(nullif);

			
			//TODO get the enum of possible types, don't compare with string.
			if (variable.getVariableValueType() == "INT") {
				Element format = ktrDocument.createElement("format");
				format.appendChild(ktrDocument.createTextNode("0.##############;-0.##############"));
				field.appendChild(format);
            }

			fieldsNode.appendChild(field);
			}
		}
		
		logger.info("finished add variables into KTR document into InputSource step");
	}
	
	
	
	
	/**
	 * Populates fields tag in the Select Value step with variables information.
	 * 
	 * @param dataFileExtension extension of the data file.
	 * @param variables the info about variables
	 * @throws Exception
	 */
	private void ktrDocumentSelectValues(final String dataFileExtension, final ArrayList<DatasetVariableViewModel> variables) throws Exception {
		
		logger.info("starting select variables into KTR document into SelectValues step");
		
		//TODO: the CSV type as a string is not good, should be an enum.
		String stepName = dataFileExtension.equals("csv") ? "Select values" : "Excel Input File";
		
		//XPath to get fields node to populate with selected variables
		String expression = String.format("/transformation/step[name = '%s']/fields", stepName);
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList fieldsTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (fieldsTag.getLength() == 0) {
			logger.error(String.format("fieldsTag failed, no /trasformation/step/fields tag "));
			
			throw new Exception("fieldsTag failed, no /trasformation/step/fields tag");
		}
		
		Node fieldsNode = fieldsTag.item(0);
		
		for(DatasetVariableViewModel variable : variables) {
			
			if (!variable.isChecked()) {
				continue;
			}
			
			Element field = ktrDocument.createElement("field");
			
			Element name = ktrDocument.createElement("name");
			name.appendChild(ktrDocument.createTextNode(variable.getOriginalName()));
		
			Element length = ktrDocument.createElement("length");
			length.appendChild(ktrDocument.createTextNode("-1"));
			
			Element precision = ktrDocument.createElement("precision");
			precision.appendChild(ktrDocument.createTextNode("-1"));

			field.appendChild(name);
			field.appendChild(length);
			field.appendChild(precision);

			//TODO get the enum of possible types, don't compare with string.
				
			fieldsNode.appendChild(field);
		}
		
		logger.info("finished add variables into KTR document into InputSource step");
	}
	
	
	/**
	 * Populates fields tag in the InputSource tag with variables information.
	 * 
	 * @param dataFileExtension extension of the data file.
	 * @param variables the info about variables
	 * @throws Exception
	 */
	private void ktrDocumentAddVariablesIntoInputInputSourceStep(final String dataFileExtension, final ArrayList<DatasetVariableViewModel> variables) throws Exception {
		
		logger.info("starting add variables into KTR document into Input Source step");
		
		//TODO: the CSV type as a string is not good, should be an enum.
		String stepName = dataFileExtension.equals("csv") ? "CSV file input" : "Excel Input File";
		
		//XPath to get fields node to populate with selected variables
		String expression = String.format("/transformation/step[name = '%s']/fields", stepName);
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList fieldsTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (fieldsTag.getLength() == 0) {
			logger.error(String.format("fieldsTag failed, no /trasformation/step/fields tag "));
			
			throw new Exception("fieldsTag failed, no /trasformation/step/fields tag");
		}
		
		Node fieldsNode = fieldsTag.item(0);
		
		for(DatasetVariableViewModel variable : variables) {
			
			if (!variable.isChecked()) {
				continue;
			}
			
			Element field = ktrDocument.createElement("field");
			
			Element name = ktrDocument.createElement("name");
			name.appendChild(ktrDocument.createTextNode(variable.getOriginalName()));

			Element type = ktrDocument.createElement("type");
			type.appendChild(ktrDocument.createTextNode("String"));
			
			Element length = ktrDocument.createElement("length");
			length.appendChild(ktrDocument.createTextNode("-1"));
			
			Element precision = ktrDocument.createElement("precision");
			precision.appendChild(ktrDocument.createTextNode("-1"));
			
			Element trimType = ktrDocument.createElement("trim_type");
			trimType.appendChild(ktrDocument.createTextNode("both"));
			
			Element repeat = ktrDocument.createElement("repeat");
			repeat.appendChild(ktrDocument.createTextNode("N"));
			
			field.appendChild(name);
			field.appendChild(type);
			field.appendChild(length);
			field.appendChild(precision);
			field.appendChild(trimType);
			field.appendChild(repeat);
			
			//TODO get the enum of possible types, don't compare with string.
			if (variable.getVariableValueType() == "INT") {
				Element format = ktrDocument.createElement("format");
				format.appendChild(ktrDocument.createTextNode("0.##############;-0.##############"));
				field.appendChild(format);
            }
           
			Element streamName = ktrDocument.createElement("stream_name");
			streamName.appendChild(ktrDocument.createTextNode(variable.getOriginalName()));
			field.appendChild(streamName);
				
			fieldsNode.appendChild(field);
		}
		
		logger.info("finished add variables into KTR document into InputSource step");
	}

	/**
	 * Add absolute names for files which need to be processed.
	 * 
	 * @param filesAbsoluteNames the extension of the data file without dot.
	 * @throws Exception
	 */
	private void ktrDocumentAddFiles(final String dataFileExtension, final ArrayList<String> filesAbsoluteNames) throws Exception {
		
		//TODO: the CSV type as a string is not good, should be an enum.
		String stepName = dataFileExtension.equals("csv") ? "CSV file input" : "Excel Input File";
		
		//XPath to get fields node to populate with selected variables
		String expression = String.format("/transformation/step[name = '%s']/file", stepName);
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList fileTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (fileTag.getLength() == 0) {
			logger.error(String.format("fileTag failed, no /trasformation/step/file tag "));
			
			throw new Exception("fileTag failed, no /trasformation/step/file tag");
		}
		
		Node fileNode = fileTag.item(0);
		
		for(String fileName : filesAbsoluteNames) {
			Element name = ktrDocument.createElement("name");
			name.appendChild(ktrDocument.createTextNode(IOUtils.getFileURLFromName(fileName)));
			
			fileNode.appendChild(name);
		}
	}

	/**
	 * Adds info from to extract from the excel file.
	 * @param worksheet the sheet info.
	 * @throws Exception
	 */
	private void ktrDocumentAddSheets(final WorksheetViewModel worksheet) throws Exception {
		//XPath to get connection node for the database to load data to
		String expression = "/transformation/step[name = 'Excel Input File']/sheets";
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList excelStepSheetsTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (excelStepSheetsTag.getLength() == 0) {
			logger.error(String.format("ktrDocumentAddSheets failed, no excel/Step/Sheets tag for the connection to the database"));
			
			throw new Exception("ktrDocumentAddSheets failed, no excel/Step/Sheets tag for the connection to the database");
		}
		
		Node excelStepSheetsNode = excelStepSheetsTag.item(0);
		
		Element sheetTag = ktrDocument.createElement("sheet");;
		Element sheetNameTag = ktrDocument.createElement("name");
		sheetNameTag.appendChild(ktrDocument.createTextNode(worksheet.getSheetName()));
		
		Element sheetStartRowTag = ktrDocument.createElement("startrow");
		sheetStartRowTag.appendChild(ktrDocument.createTextNode(String.valueOf(worksheet.getHeaderRow() - 1)));
		
		Element sheetStartColumnTag = ktrDocument.createElement("startcol");
		// Converting Column Letter to Number index.
		String columnIndexAsStr = String.valueOf(CellReference.convertColStringToIndex(worksheet.getStartColumn()));
		sheetStartColumnTag.appendChild(ktrDocument.createTextNode(columnIndexAsStr));
		 
		
		sheetTag.appendChild(sheetNameTag);
		sheetTag.appendChild(sheetStartRowTag);
		sheetTag.appendChild(sheetStartColumnTag);
		
		excelStepSheetsNode.appendChild(sheetTag);
	}

	/**
	 * Changes the dummy connection in the KTR template to the connection info in the settings file where the data and logging should be saved.
	 * @throws Exception
	 */
	private void ktrDocumentChangeConnection() throws Exception {
		
		//XPath to get connection node for the database to load data to
		String expression = "/transformation/connection[name = 'colfusion.exp.sis.pitt.edu']";
		 
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList connectionTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (connectionTag.getLength() == 0) {
			logger.error(String.format("ktrDocumentChangeConnection failed, no connection tag for the connection to the database"));
			
			throw new Exception("ktrDocumentChangeConnection failed, no connection tag for the connection to the database");
		}
	
		Node connectionNode = connectionTag.item(0);
		
		ConfigManager configManager = ConfigManager.getInstance();
		
		setConnectionNode(connectionNode, configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_DATABASE_NAME_PREFIX) + sid,
				configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_USERNAME),
				configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_PASSWORD),
				configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_HOST),
				configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_PORT),
				configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_VENDOR));
		
			
		//XPath to get connection for logging database
		expression = "/transformation/connection[name = 'connection_for_logging']";
		
		xPath =  XPathFactory.newInstance().newXPath();
		connectionTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (connectionTag.getLength() == 0) {
			logger.error(String.format("ktrDocumentChangeConnection failed, no connection tag for the connection to the database"));
			
			throw new Exception("ktrDocumentChangeConnection failed, no connection tag for the connection to the database");
		}
	
		connectionNode = connectionTag.item(0);
		
		setConnectionNode(connectionNode, configManager.getProperty(PropertyKeys.COLFUSION_PENTAHO_LOGGING_DATABASE_DATABASE_NAME),
				configManager.getProperty(PropertyKeys.COLFUSION_PENTAHO_LOGGING_DATABASE_USERNAME),
				configManager.getProperty(PropertyKeys.COLFUSION_PENTAHO_LOGGING_DATABASE_PASSWORD),
				configManager.getProperty(PropertyKeys.COLFUSION_PENTAHO_LOGGING_DATABASE_HOST),
				configManager.getProperty(PropertyKeys.COLFUSION_PENTAHO_LOGGING_DATABASE_PORT),
				configManager.getProperty(PropertyKeys.COLFUSION_PENTAHO_LOGGING_DATABASE_VENDOR));
		
	}
	
	/**
	 * Updates connection tag for the target db with provided info. KTR file is supposed to be loaded at this time already.
	 * The updated file is saved to the disk.
	 * 
	 * @param databaseName the name of the database to set (target for data and logging data).
	 * @param userName user name which will be used to connect to database.
	 * @param password which will be used to connect to the database
	 * @param server URL of the server where database is running.
	 * @param port on which database is listening.
	 * @param type the vendor of the database, e.g. MySQL.
	 * @throws Exception
	 */
	public void updateTargetDBConnectionInfo(final String databaseName, final String userName, final String password, final String server, final String port, 
			final String type) throws Exception{
		
		if (ktrDocument == null) {
			//TODO: create appropriate exception.
			
			logger.error(String.format("updateTargetDBConnectionInfo failed: ktrDocument is not loaded for %d sid", sid));
			throw new Exception(String.format("updateTargetDBConnectionInfo failed: ktrDocument is not loaded for %d sid", sid));
		}
		
		//XPath to get connection node for the database to load data to
		String expression = "/transformation/connection[name = 'colfusion.exp.sis.pitt.edu']";
		 
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList connectionTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (connectionTag.getLength() == 0) {
			logger.error(String.format("updateTargetDBConnectionInfo failed, no connection tag for the connection to the database"));
			
			throw new Exception("updateTargetDBConnectionInfo failed, no connection tag for the connection to the database");
		}
	
		Node connectionNode = connectionTag.item(0);
		
		setConnectionNode(connectionNode, databaseName, userName, password, server, port, type);
		
		saveKTRFile();
	}
	
	//TODO this can actually be generalized by using map
	/**
	 * Actually modifies XML document (loaded in memory) node with provided info.
	 * @param connectionNode the XML node which need to be modified.
	 * @param databaseName the name of the database to set (target for data and logging data).
	 * @param userName user name which will be used to connect to database.
	 * @param password which will be used to connect to the database
	 * @param server URL of the server where database is running.
	 * @param port on which database is listening.
	 * @param type the vendor of the database, e.g. MySQL.
	 */
	private void setConnectionNode(final Node connectionNode, final String databaseName, final String userName, final String password, final String server, final String port, 
			final String type) {
		//TODO there must be a better way to do that
		NodeList childNodes = connectionNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++)
		{
		    Node child = childNodes.item(i);
		    if (!(child instanceof Element)) {
				continue;
			}

		    if (child.getNodeName().equals("database")) {
				child.getFirstChild().setNodeValue(databaseName);
			} else if (child.getNodeName().equals("username")) {
				child.getFirstChild().setNodeValue(userName);
			} else if (child.getNodeName().equals("password")) {
				child.getFirstChild().setNodeValue(password);
			} else if (child.getNodeName().equals("server")) {
				child.getFirstChild().setNodeValue(server);
			} else if (child.getNodeName().equals("port")) {
				child.getFirstChild().setNodeValue(port);
			} else if (child.getNodeName().equals("type")) {
				child.getFirstChild().setNodeValue(type);
			}
		}
	}

	/**
	 * Saved location of the KTR file which is used for loading data from a worksheet into db table.
	 * 
	 * @param sid id of the story.
	 * @param tableName name of the sheet/table.
	 * @param ktrAbsoluteFileName absolute location of the KTR file.
	 * @throws Exception 
	 */
	private void saveKTRFileLocationToDB(final int sid, final String tableName, final String ktrAbsoluteFileName) throws Exception {
		
		SourceInfoTableKTRManager sourceInfoTableKTRManager = new SourceInfoTableKTRManagerImpl();
		SourceInfoManager sourceInoMgr = new SourceInfoManagerImpl();
		ColfusionSourceinfo sourceInfo;
		try {
			sourceInfo = sourceInoMgr.findByID(sid);
		} catch (Exception e) {
			logger.error(String.format("Failed to find sourceinfo by sid = %d in saveKTRFileLocationToDB", sid), e);
			
			throw e;
		}
		
		ColfusionSourceinfoTableKtrId sourceInfoTableKTRId = new ColfusionSourceinfoTableKtrId(sid, tableName);
		
		ColfusionSourceinfoTableKtr sourceInfoTableKTR = new ColfusionSourceinfoTableKtr(sourceInfoTableKTRId, sourceInfo, 
				ktrAbsoluteFileName);
		
		try {
			sourceInfoTableKTRManager.saveOrUpdate(sourceInfoTableKTR);
		} catch (Exception e) {
			logger.error(String.format("Failed to saveOrUpdate sourceinfotablektr in saveKTRFileLocationToDB"), e);
			
			throw e;
		}
	}

	/**
	 * Copy KTR template content into new file which will be used to fill with information collected via wizard to load data from excel or CSV file.
	 * 
	 * @param sid the id of the story for which the KTRfile will be created (id will be used for the folder to hold KTRs created for each sheet).
	 * @param dataFileExtension extension of the submitted file.
	 * @param tableName name of the table for which to create KTR. The table is a excel sheet. The newly created KTR file will have the same name.
	 * @return info about created KTR file.
	 * @throws IOException
	 */
	private IOUtilsStoredFileInfoModel createKTRFileFromTemplate(final int sid, final String dataFileExtension, final String tableName) throws IOException {
		
		String ktrBaseDirLocation = IOUtils.getAbsolutePathInColfution(ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_KTR_FOLDER));
		String ktrDirectoryLocation = ktrBaseDirLocation + File.separator +	sid;
		
		//TODO for now simple check for csv, but what if we going to have more extensions, need to rewrite this.
		String ktrTemplateName = dataFileExtension.equals("csv") 
				? ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_KTR_TEMPLATES_CSV_TO_DATABASE) 
				: ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_KTR_TEMPLATES_EXCEL_TO_DATABASE);
				
		InputStream ktrTemplate = ResourceUtils.getResourceAsStream(this.getClass(), ktrTemplateName);
			
		IOUtilsStoredFileInfoModel result = IOUtils.writeInputStreamToFile(ktrTemplate, ktrDirectoryLocation, tableName + ".ktr", true);
		
		logger.info(String.format("Copied KTR file for table name %s", tableName));
		
		return result;
	}

	/**
	 * @return the sid
	 */
	public int getSid() {
		return sid;
	}

	/**
	 * @param sid the sid to set
	 */
	public void setSid(final int sid) {
		this.sid = sid;
	}

	/**
	 * Loads KTR file specified as absolute path into memory and keeps it in the private variable.
	 * @param ktrLocation the absolute file location.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public void loadKTR(final String ktrLocation) throws ParserConfigurationException, SAXException, IOException {
		ktrDocument = IOUtils.readXMLDocument(ktrLocation);		
		this.ktrAbsoluteName = ktrLocation;
	}

	/**
	 * Reads the target database connection information.
	 * 
	 * @return the info about target database.
	 * @throws Exception 
	 */
	public StoryTargetDBViewModel readTargetDatabaseInfo() throws Exception {
		
		if (ktrDocument == null) {
			//TODO: create appropriate exception.
			
			logger.error(String.format("readTargetDatabaseInfo failed: ktrDocument is not loaded for %d sid", sid));
			throw new Exception(String.format("readTargetDatabaseInfo failed: ktrDocument is not loaded for %d sid", sid));
		}
		
		//XPath to get connection node for the database to load data to
		String expression = "/transformation/connection[name = 'colfusion.exp.sis.pitt.edu']";
		 
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList connectionTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (connectionTag.getLength() == 0) {
			logger.error(String.format("ktrDocumentChangeConnection failed, no connection tag for the connection to the database"));
			
			throw new Exception("ktrDocumentChangeConnection failed, no connection tag for the connection to the database");
		}
	
		Node connectionNode = connectionTag.item(0);
		
		StoryTargetDBViewModel result = new StoryTargetDBViewModel();
		
		if (connectionNode.getNodeType() == Node.ELEMENT_NODE) {
			Element connectionElement = (Element) connectionNode;
			
			result.setSid(sid);
			//TODO: maybe we need to check if the findByTagName return one tag or not
			result.setDatabaseName(connectionElement.getElementsByTagName("database").item(0).getFirstChild().getNodeValue());
			result.setDriver(connectionElement.getElementsByTagName("type").item(0).getFirstChild().getNodeValue());
			result.setPassword(connectionElement.getElementsByTagName("password").item(0).getFirstChild().getNodeValue());
			result.setPort(Integer.valueOf(connectionElement.getElementsByTagName("port").item(0).getFirstChild().getNodeValue()));
			result.setServerAddress(connectionElement.getElementsByTagName("server").item(0).getFirstChild().getNodeValue());
			result.setUserName(connectionElement.getElementsByTagName("username").item(0).getFirstChild().getNodeValue());
			
			result.setIsLocal(1);
			result.setLinkedServerName(StoryUtils.generateLinkedServerName(sid));
		}
		else {
			
			//TODO:handle it better
			logger.error(String.format("ktrDocumentChangeConnection failed, no connection tag for the connection to the database"));
			
			throw new Exception("ktrDocumentChangeConnection failed, no connection tag for the connection to the database");
		}
			
		return result;
	}

	/**
	 * Gets the table name from the target database step (the table where the data should be loaded).
	 * 
	 * The KTR file should be loaded into memory before calling this function (call loadKTR).
	 * @return the table name.
	 * @throws Exception 
	 */
	public String getTableName() throws Exception {
		if (ktrDocument == null) {
			//TODO: create appropriate exception.
			
			logger.error(String.format("getTableName failed: ktrDocument is not loaded for %d sid", sid));
			throw new Exception(String.format("getTableName failed: ktrDocument is not loaded for %d sid", sid));
		}
		
		//XPath to get connection node for the database to load data to
		String expression = "/transformation/step[name = 'Target Schema']";
		 
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList targetSchemaStep = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (targetSchemaStep.getLength() == 0) {
			logger.error(String.format("getTableName failed, no /transformation/step[name = 'Target Schema'] tag"));
			
			throw new Exception("getTableName failed, no /transformation/step[name = 'Target Schema'] tag");
		}
	
		Node targetSchemaStepNode = targetSchemaStep.item(0);
		
		if (targetSchemaStepNode.getNodeType() == Node.ELEMENT_NODE) {
			Element targetSchemaStepElement = (Element) targetSchemaStepNode;
			
			try {
				return targetSchemaStepElement.getElementsByTagName("table").item(0).getFirstChild().getNodeValue();
			} catch (Exception e) {
				//TODO:handle it better
				logger.error(String.format("getTableName failed, could not read table name"), e);
				
				throw new Exception("getTableName failed, could not read table name");
			}
		}
		else {
			
			//TODO:handle it better
			logger.error(String.format("getTableName failed, no /transformation/step[name = 'Target Schema'] tag or found tag is wrong nodetype"));
			
			throw new Exception("getTableName failed, no /transformation/step[name = 'Target Schema'] tag or found tag is wrong nodetype");
		}
	}

	/**
	 * Updates the name tag of the transformation.
	 * The KTR file should be loaded before this call.
	 * 
	 * @param transformationName the name to set.
	 * @throws Exception 
	 */
	public void changeTransformaitonName(final String transformationName) throws Exception {
		
		if (ktrDocument == null) {
			//TODO: create appropriate exception.
			
			logger.error(String.format("changeTransformaitonName failed: for %s name", transformationName));
			throw new Exception(String.format("changeTransformaitonName failed: for %s name", transformationName));
		}
		
		ktrDocumentChangeTransformationName(transformationName);
		
		saveKTRFile();
	}

	public List<String> getTargetTableColumns() throws Exception {
		// TODO we can actually get the columns from the column metadata table in the db.
		
		if (ktrDocument == null) {
			//TODO: create appropriate exception.
			
			logger.error(String.format("getTargetTableColumns failed: ktrDocument is not loaded for %d sid", sid));
			throw new Exception(String.format("getTargetTableColumns failed: ktrDocument is not loaded for %d sid", sid));
		}
		
		//XPath to get connection node for the database to load data to
		String expression = "/transformation/step[name = 'Target Schema']/fields/field/column_name";
		 
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList columnNames = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (columnNames.getLength() == 0) {
			logger.error(String.format("getTargetTableColumns failed, no /transformation/step[name = 'Target Schema']/fields/field/column_name tag"));
			
			throw new Exception("getTargetTableColumns failed, no /transformation/step[name = 'Target Schema']/fields/field/column_name tag");
		}
	
		ArrayList<String> result = new ArrayList<>();
		for (int i = 0; i < columnNames.getLength(); i++) {
			result.add(columnNames.item(i).getFirstChild().getNodeValue());
		}
		
		return result;
		
	}
}
