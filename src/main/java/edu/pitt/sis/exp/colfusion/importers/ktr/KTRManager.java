/**
 * 
 */
package edu.pitt.sis.exp.colfusion.importers.ktr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.util.CellReference;
import org.eclipse.persistence.exceptions.XMLConversionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.PropertyKeys;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoTableKTRManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoTableKTRManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoTableKtr;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoTableKtrId;
import edu.pitt.sis.exp.colfusion.utils.IOUtils;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;
import edu.pitt.sis.exp.colfusion.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.WorksheetViewModel;

/**
 * The class to manage one KTR file or files if uploaded file has more than one sheet: create, read, write, modify.
 * 
 * @author Evgeny
 *
 */
public class KTRManager {
	final Logger logger = LogManager.getLogger(KTRManager.class.getName());
	
	private int sid;
	
	public KTRManager(int sid) {
		setSid(sid);
	}
	
	/**
	 * Create a ktr file from the template file, and populate the ktr file with information from the {@link FileContentInfoViewModel} information.
	 * The function should be called for each file separately. As the result a new ktr file will be created for each sheet in the file.
	 * 
	 * If the otherFiles field is not empty, then those files should have exactly the same structure as the main file. The ktr template will then 
	 * list other files with exactly the same properties for sheets and as the result the data from all file will be appended.
	 * @param file
	 * @throws Exception 
	 */
	public void createTemplate(FileContentInfoViewModel file) throws Exception {
		
		logger.info(String.format("Starting to create KTR file(s) for %s sid", sid));
		
		//TODO: this should be done in some other place, because the same line is used DataSubmissionWizardBLL when saveVariablesMetadata.
		String tableNamePrefix = file.getWorksheets().size() > 1 ? file.getFileName() + " - " : "";
		
		ArrayList<String> filesAbsoluteNames = new ArrayList<>();
		filesAbsoluteNames.add(file.getFileAbsoluteName());
		if (file.getOtherFilesAbsoluteNames() != null) {
			for (String fileName : file.getOtherFilesAbsoluteNames()) {
				filesAbsoluteNames.add(fileName);
			}
		}
		
		for(WorksheetViewModel worksheet : file.getWorksheets()) {
			
			String tableName = tableNamePrefix + worksheet.getSheetName();
			
			IOUtilsStoredFileInfoModel copiedKTRFileInfo = createKTRFileFromTemplate(sid, file.getExtension(), tableName);  
			
			//TODO uncomment saveKTRFileLocationToDB(sid, tableName, copiedKTRFileInfo.getAbsoluteFileName());
			
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
	private void fillKTRFile(String ktrAbsoluteName, String dataFileExtension, ArrayList<String> filesAbsoluteNames, WorksheetViewModel worksheet, 
			String tableName) 
			throws Exception {
		Document ktrDocument = IOUtils.getInstance().readXMLDocument(ktrAbsoluteName);
		
		ktrDocumentChangeConnection(ktrDocument);
		
		if (!dataFileExtension.equals("csv")) {
			ktrDocumentAddSheets(ktrDocument, worksheet);
		}
		
		ktrDocumentAddFiles(ktrDocument, filesAbsoluteNames);
		ktrDocumentAddVariablesIntoInputInputSourceStep(ktrDocument, dataFileExtension, worksheet.getVariables());
		ktrDocumentAddTableNateIntoTargetSchemaStep(ktrDocument, tableName);
		ktrDocumentAddVariablesIntoTargetSchemaStep(ktrDocument, worksheet.getVariables());
		
		ktrDocumentChangeTransformationName(ktrDocument, String.valueOf(getSid()));
		
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(ktrDocument);
		StreamResult result = new StreamResult(new File(ktrAbsoluteName));
		transformer.transform(source, result);
	}

	/**
	 * Set the transformation name of the given KTR document to a given name.
	 * @param ktrDocument the document in which to set transformation name.
	 * @param transformationName the name to set.
	 * @throws Exception
	 */
	private void ktrDocumentChangeTransformationName(Document ktrDocument, String transformationName) throws Exception {
		logger.info("starting change transformation name in the KTR document");
 		
 		//XPath to get fields node to populate with selected variables
 		String expression = "/transformation/info/name";
 		
 		XPath xPath =  XPathFactory.newInstance().newXPath();
 		NodeList nameTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
 		
 		if (nameTag.getLength() == 0) {
 			logger.error(String.format("nameTag failed, no /transformation/info/name tag "));
 			
 			throw new Exception("nameTag failed, no /transformation/info/name tag");
 		}
 		
 		Node nameNode = (Node) nameTag.item(0);
 		
 		removeAllChildNodes(nameNode);
 		
 		nameNode.appendChild(ktrDocument.createTextNode(transformationName));
 		
 		logger.info("finished change transformation name in the KTR document");
         
		
	}

	/**
	 * Removes all child nodes from the given node of the XML {@link Document}
	 * @param node parent node from which the children should be deleted.
	 */
	public void removeAllChildNodes(Node node) 
    {
		NodeList children = node.getChildNodes();
		
		for (int i = children.getLength() - 1; i >= 0; i--) {
			node.removeChild(children.item(i));
		}
    }
	
	/**
	 * Adds table tag into KTR file into Target Schema step with name of the table in the database where the data should be imported.
	 * 
	 * @param ktrDocument KTR document as XML in memory.
	 * @param tableName the name of the table.
	 * @throws Exception
	 */
	private void ktrDocumentAddTableNateIntoTargetSchemaStep(Document ktrDocument, String tableName) throws Exception {
		logger.info("starting add table name into KTR document into Target Schema step");
		
		//XPath to get fields node to populate with selected variables
		String expression = "/transformation/step[name = 'Target Schema']";
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList stepTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (stepTag.getLength() == 0) {
			logger.error(String.format("fieldsTag failed, no /transformation/step[name = 'Target Schema'] tag "));
			
			throw new Exception("fieldsTag failed, no /transformation/step[name = 'Target Schema'] tag");
		}
		
		Node stepNode = (Node) stepTag.item(0);
		
		Element table = ktrDocument.createElement("table");
		table.appendChild(ktrDocument.createTextNode(tableName));

		stepNode.appendChild(table);
		
		logger.info("finished add table name into KTR document into TargetSchema step");
	}

	/**
	 * 
	 * Populates fields tag in the Target Schema step with variables information.
	 * @param ktrDocument KTR document as XML in memory.
	 * @param variables the info about variables
	 * @throws Exception
	 */
	private void ktrDocumentAddVariablesIntoTargetSchemaStep(Document ktrDocument, ArrayList<DatasetVariableViewModel> variables) throws Exception {
		logger.info("starting add variables into KTR document into Target Schema step");
		
		//XPath to get fields node to populate with selected variables
		String expression = "/transformation/step[name = 'Target Schema']/fields";
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList fieldsTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (fieldsTag.getLength() == 0) {
			logger.error(String.format("fieldsTag failed, no /transformation/step[name = 'Target Schema']/fields tag "));
			
			throw new Exception("fieldsTag failed, no /transformation/step[name = 'Target Schema']/fields tag");
		}
		
		Node fieldsNode = (Node) fieldsTag.item(0);
		
		for(DatasetVariableViewModel variable : variables) {
			
			if (!variable.isChecked()) { //Add only those variables that user selected.
				continue;
			}
			
			Element field = ktrDocument.createElement("field");
			
			Element columnName = ktrDocument.createElement("column_name");
			columnName.appendChild(ktrDocument.createTextNode(variable.getOriginalName()));

			Element streamName = ktrDocument.createElement("stream_name");
			streamName.appendChild(ktrDocument.createTextNode(variable.getOriginalName()));

			field.appendChild(columnName);
			field.appendChild(streamName);
				
			fieldsNode.appendChild(field);
		}
		
		logger.info("finished add variables into KTR document into TargetSchema step");
	}

	/**
	 * Populates fields tag in the InputSource tag with variables information.
	 * @param ktrDocument KTR document as XML in memory.
	 * @param dataFileExtension extension of the data file.
	 * @param variables the info about variables
	 * @throws Exception
	 */
	private void ktrDocumentAddVariablesIntoInputInputSourceStep(Document ktrDocument, String dataFileExtension, ArrayList<DatasetVariableViewModel> variables) throws Exception {
		
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
		
		Node fieldsNode = (Node) fieldsTag.item(0);
		
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
	 * 
	 * @param ktrDocument
	 * @param filesAbsoluteNames
	 * @throws Exception
	 */
	private void ktrDocumentAddFiles(Document ktrDocument, ArrayList<String> filesAbsoluteNames) throws Exception {
		//XPath to get connection node for the database to load data to
		String expression = "/transformation/step[name = 'Excel Input File']/file";
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList fileTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (fileTag.getLength() == 0) {
			logger.error(String.format("fileTag failed, no /trasformation/step/file tag for the connection to the database"));
			
			throw new Exception("fileTag failed, no /trasformation/step/file tag for the connection to the database");
		}
		
		Node fileNode = (Node) fileTag.item(0);
		
		for(String fileName : filesAbsoluteNames) {
			Element name = ktrDocument.createElement("name");
			name.appendChild(ktrDocument.createTextNode(fileName));
			
			fileNode.appendChild(name);
		}
	}

	/**
	 * Adds info from to extract from the excel file.
	 * @param ktrDocument the KTR as XML document parsed in memory.
	 * @param worksheet the sheet info.
	 * @throws Exception
	 */
	private void ktrDocumentAddSheets(Document ktrDocument, WorksheetViewModel worksheet) throws Exception {
		//XPath to get connection node for the database to load data to
		String expression = "/transformation/step[name = 'Excel Input File']/sheets";
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList excelStepSheetsTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (excelStepSheetsTag.getLength() == 0) {
			logger.error(String.format("ktrDocumentAddSheets failed, no excel/Step/Sheets tag for the connection to the database"));
			
			throw new Exception("ktrDocumentAddSheets failed, no excel/Step/Sheets tag for the connection to the database");
		}
		
		Node excelStepSheetsNode = (Node) excelStepSheetsTag.item(0);
		
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
	 * @param ktrDocument KTR as XML document parsed in memory.
	 * @throws Exception
	 */
	private void ktrDocumentChangeConnection(Document ktrDocument) throws Exception {
		
		//XPath to get connection node for the database to load data to
		String expression = "/transformation/connection[name = 'colfusion.exp.sis.pitt.edu']";
		 
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList connectionTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (connectionTag.getLength() == 0) {
			logger.error(String.format("ktrDocumentChangeConnection failed, no connection tag for the connection to the database"));
			
			throw new Exception("ktrDocumentChangeConnection failed, no connection tag for the connection to the database");
		}
	
		Node connectionNode = (Node) connectionTag.item(0);
		
		setConnectionNode(connectionNode, ConfigManager.getInstance().getPropertyByName(PropertyKeys.targetFileToDBDatabase_DatabaseNamePrefix) + sid,
				ConfigManager.getInstance().getPropertyByName(PropertyKeys.targetFileToDBDatabase_UserName),
				ConfigManager.getInstance().getPropertyByName(PropertyKeys.targetFileToDBDatabase_Password),
				ConfigManager.getInstance().getPropertyByName(PropertyKeys.targetFileToDBDatabase_Server),
				ConfigManager.getInstance().getPropertyByName(PropertyKeys.targetFileToDBDatabase_Port),
				ConfigManager.getInstance().getPropertyByName(PropertyKeys.targetFileToDBDatabase_Type));
		
			
		//XPath to get connection for logging database
		expression = "/transformation/connection[name = 'connection_for_logging']";
		
		xPath =  XPathFactory.newInstance().newXPath();
		connectionTag = (NodeList) xPath.compile(expression).evaluate(ktrDocument, XPathConstants.NODESET);
		
		if (connectionTag.getLength() == 0) {
			logger.error(String.format("ktrDocumentChangeConnection failed, no connection tag for the connection to the database"));
			
			throw new Exception("ktrDocumentChangeConnection failed, no connection tag for the connection to the database");
		}
	
		connectionNode = (Node) connectionTag.item(0);
		
		setConnectionNode(connectionNode, ConfigManager.getInstance().getPropertyByName(PropertyKeys.logginDatabase_DatabaseNamePrefix),
				ConfigManager.getInstance().getPropertyByName(PropertyKeys.logginDatabase_UserName),
				ConfigManager.getInstance().getPropertyByName(PropertyKeys.logginDatabase_Password),
				ConfigManager.getInstance().getPropertyByName(PropertyKeys.logginDatabase_Server),
				ConfigManager.getInstance().getPropertyByName(PropertyKeys.logginDatabase_Port),
				ConfigManager.getInstance().getPropertyByName(PropertyKeys.logginDatabase_Type));
		
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
	private void setConnectionNode(Node connectionNode, String databaseName, String userName, String password, String server, String port, String type) {
		//TODO there must be a better way to do that
		NodeList childNodes = connectionNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++)
		{
		    Node child = childNodes.item(i);
		    if (!(child instanceof Element))
		        continue;

		    if (child.getNodeName().equals("database"))
		        child.getFirstChild().setNodeValue(databaseName);
		    else if (child.getNodeName().equals("username"))
		        child.getFirstChild().setNodeValue(userName);
		    else if (child.getNodeName().equals("password"))
		        child.getFirstChild().setNodeValue(password);
		    else if (child.getNodeName().equals("server"))
		        child.getFirstChild().setNodeValue(server);
		    else if (child.getNodeName().equals("port"))
		        child.getFirstChild().setNodeValue(port);
		    else if (child.getNodeName().equals("type"))
		        child.getFirstChild().setNodeValue(type);
		}
	}

	/**
	 * Saved location of the KTR file which is used for loading data from a worksheet into db table.
	 * @param sid id of the story.
	 * @param tableName name of the sheet/table.
	 * @param ktrAbsoluteFileName absolute location of the KTR file.
	 */
	private void saveKTRFileLocationToDB(int sid, String tableName, String ktrAbsoluteFileName) {
		
		SourceInfoTableKTRManager sourceInfoTableKTRManager = new SourceInfoTableKTRManagerImpl();
		SourceInfoManager sourceInoMgr = new SourceInfoManagerImpl();
		ColfusionSourceinfo sourceInfo = sourceInoMgr.findByID(sid);
		
		ColfusionSourceinfoTableKtrId sourceInfoTableKTRId = new ColfusionSourceinfoTableKtrId(sid, tableName);
		
		ColfusionSourceinfoTableKtr sourceInfoTableKTR = new ColfusionSourceinfoTableKtr(sourceInfoTableKTRId, sourceInfo, 
				ktrAbsoluteFileName);
		
		sourceInfoTableKTRManager.save(sourceInfoTableKTR);
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
	private IOUtilsStoredFileInfoModel createKTRFileFromTemplate(int sid, String dataFileExtension, String tableName) throws IOException {
		
		String ktrBaseDirLocation = ConfigManager.getInstance().getPropertyByName(PropertyKeys.ktrFielsBaseLocation);
		String ktrDirectoryLocation = ktrBaseDirLocation + File.separator +	sid;
		
		//TODO for now simple check for csv, but what if we going to have more extensions, need to rewrite this.
		String ktrTemplateName = dataFileExtension.equals("csv") 
				? ConfigManager.getInstance().getPropertyByName(PropertyKeys.csvToDatabaseKTRTemplate) 
				: ConfigManager.getInstance().getPropertyByName(PropertyKeys.excelToDatabaseKTRTemplate);
				
		String ktrTemplateNameLocation = Thread.currentThread().getContextClassLoader().getResource(ktrTemplateName).getFile();
		
		IOUtilsStoredFileInfoModel result = IOUtils.getInstance().copyFileContent(ktrTemplateNameLocation, ktrDirectoryLocation, tableName + ".ktr");
		
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
	public void setSid(int sid) {
		this.sid = sid;
	}
}
