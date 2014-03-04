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
			
			fillKTRFile(copiedKTRFileInfo.getAbsoluteFileName(), filesAbsoluteNames, worksheet);
		}		
	}

	/**
	 * Fills all parts of the KTR file with user provided info. After this function KTR file is ready to be executed.
	 * 
	 * @param ktrAbsoluteName the absolute KTR file location.
	 * @param filesAbsoluteNames the names of the files from which to read the data (ALl files must have the same structure).
	 * @param worksheet - what sheet to import.
	 * @throws Exception 
	 */
	private void fillKTRFile(String ktrAbsoluteName, ArrayList<String> filesAbsoluteNames, WorksheetViewModel worksheet) 
			throws Exception {
		Document ktrDocument = IOUtils.getInstance().readXMLDocument(ktrAbsoluteName);
		
		ktrDocumentChangeConnection(ktrDocument);
		
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(ktrDocument);
		StreamResult result = new StreamResult(new File(ktrAbsoluteName));
		transformer.transform(source, result);
	}

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
	
	private void setConnectionNode(Node connectionNode, String databaseNamePrefix, String userName, String password, String server, String port, String type) {
		//TODO there must be a better way to do that
		NodeList childNodes = connectionNode.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++)
		{
		    Node child = childNodes.item(i);
		    if (!(child instanceof Element))
		        continue;

		    if (child.getNodeName().equals("database"))
		        child.getFirstChild().setNodeValue(databaseNamePrefix);
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
