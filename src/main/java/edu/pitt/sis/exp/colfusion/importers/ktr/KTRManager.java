/**
 * 
 */
package edu.pitt.sis.exp.colfusion.importers.ktr;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	
	/**
	 * Create a ktr file from the template file, and populate the ktr file with information from the {@link FileContentInfoViewModel} information.
	 * The function should be called for each file separately. As the result a new ktr file will be created for each sheet in the file.
	 * 
	 * If the otherFiles field is not empty, then those files should have exactly the same structure as the main file. The ktr template will then 
	 * list other files with exactly the same properties for sheets and as the result the data from all file will be appended.
	 * @param file
	 */
	public void createTemplate(FileContentInfoViewModel file, int sid) throws IOException {
		
		logger.info(String.format("Starting to create KTR file(s) for $s sid", sid));
		
		String ktrBaseDirLocation = ConfigManager.getInstance().getPropertyByName(PropertyKeys.ktrFielsBaseLocation);
		String ktrDirectoryLocation = ktrBaseDirLocation + File.separator +	sid;
		
		//TODO for now simple check for csv, but what if we going to have more extensions, need to rewrite this.
		String ktrTemplateName = file.getExtension().equals("csv") 
				? ConfigManager.getInstance().getPropertyByName(PropertyKeys.csvToDatabaseKTRTemplate) 
				: ConfigManager.getInstance().getPropertyByName(PropertyKeys.excelToDatabaseKTRTemplate);
				
		String ktrTemplateNameLocation = Thread.currentThread().getContextClassLoader().getResource(ktrTemplateName).getFile();
				
		//TODO: this should be done in some other place, because the same line is used DataSubmissionWizardBLL when saveVariablesMetadata.
		String tableNamePrefix = file.getWorksheets().size() > 1 ? file.getFileName() + " - " : "";
		
		
		if (sourceInfo == null) {
			//TODO handle it better and let user know there were some problems.
			logger.error(String.format("source info was not found (is equal to null) for %s sid", sid));
			throw new Error("source info was not found (is equal to null)");
		}
		
		for(WorksheetViewModel worksheet : file.getWorksheets()) {
			
			String tableName = tableNamePrefix + worksheet.getSheetName();
			
			IOUtilsStoredFileInfoModel copiedKTRFileInfo = createKTRFileFromTemplate(ktrTemplateNameLocation, 
					ktrDirectoryLocation, tableName + ".ktr");  
			
			saveKTRFileLocationToDB(sid, tableName, copiedKTRFileInfo.getAbsoluteFileName());			
		}
		
		
	}

	private void saveKTRFileLocationToDB(int sid, String tableName, String ktrAbsoluteFileName) {
		
		SourceInfoTableKTRManager sourceInfoTableKTRManager = new SourceInfoTableKTRManagerImpl();
		SourceInfoManager sourceInoMgr = new SourceInfoManagerImpl();
		ColfusionSourceinfo sourceInfo = sourceInoMgr.findByID(sid);
		
		ColfusionSourceinfoTableKtrId sourceInfoTableKTRId = new ColfusionSourceinfoTableKtrId(sid, tableName);
		
		ColfusionSourceinfoTableKtr sourceInfoTableKTR = new ColfusionSourceinfoTableKtr(sourceInfoTableKTRId, sourceInfo, 
				ktrAbsoluteFileName);
		
		sourceInfoTableKTRManager.save(sourceInfoTableKTR);
	}

	private IOUtilsStoredFileInfoModel createKTRFileFromTemplate( String ktrTemplateNameLocation, String ktrDirectoryLocation, 
			String tableName) throws IOException {
		IOUtilsStoredFileInfoModel result = IOUtils.getInstance().copyFileContent(ktrTemplateNameLocation, ktrDirectoryLocation, tableName);
		
		logger.info(String.format("Copied KTR file for table name %s", tableName));
		
		return result;
	}
}
