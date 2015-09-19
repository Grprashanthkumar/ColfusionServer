package edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizzard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.dataLoadExecutors.DataLoadExecutor;
import edu.pitt.sis.exp.colfusion.bll.dataLoadExecutors.DataLoadExecutorFactory;
import edu.pitt.sis.exp.colfusion.bll.importers.Importer;
import edu.pitt.sis.exp.colfusion.bll.importers.ImporterFactory;
import edu.pitt.sis.exp.colfusion.bll.importers.ImporterType;
import edu.pitt.sis.exp.colfusion.bll.importers.ktr.KTRManager;
import edu.pitt.sis.exp.colfusion.bll.process.ProcessManager;
import edu.pitt.sis.exp.colfusion.bll.responseModels.AcceptedFilesResponse;
import edu.pitt.sis.exp.colfusion.bll.responseModels.FileContentInfoReponse;
import edu.pitt.sis.exp.colfusion.bll.responseModels.GeneralResponseImpl;
import edu.pitt.sis.exp.colfusion.bll.responseModels.OneNumberResponse;
import edu.pitt.sis.exp.colfusion.bll.responseModels.PreviewFileResponse;
import edu.pitt.sis.exp.colfusion.dal.managers.DNameInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.DNameInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.utils.DataSourceTypes;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.CreateTemplateViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FilesContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.OneUploadedItemViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.PreviewFileViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetViewModel;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.IOUtils;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;


/**
 * Handles all business logic for the data submission wizard.
 *
 * @author Evgeny
 *
 */
public class DataSubmissionWizzardBL {

	final Logger logger = LogManager.getLogger(DataSubmissionWizzardBL.class.getName());

	/**
	 * Stores the uploaded files into disk. Also it performs some actions depending on the file type. e.g. unzips archives.
	 *
	 * @param sid story id for which the files are uploaded.
	 * @param inputStreams the input streams of the files.
	 * @return the response message which will say if the upload was successful and if not what might be the reason.
	 */
	public AcceptedFilesResponse storeUploadedFiles(final String sid, final Map<String, InputStream> inputStreams) {

		final String uploadFilesLocation = IOUtils.getAbsolutePathInColfution(ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_UPLOAD_FILES_FOLDER));
		final String uploadFileAbsolutePath = uploadFilesLocation + File.separator + sid;

		final AcceptedFilesResponse result = new AcceptedFilesResponse();

		try {

			for (final Map.Entry<String, InputStream> inputStream : inputStreams.entrySet()){

				final IOUtilsStoredFileInfoModel fileInfo = IOUtils.writeInputStreamToFile(inputStream.getValue(), uploadFileAbsolutePath, inputStream.getKey(), false);

				if (fileInfo.isArchive()) {
					final ArrayList<IOUtilsStoredFileInfoModel> filesInfo = IOUtils.unarchive(fileInfo.getAbsoluteFileName());

					final OneUploadedItemViewModel oneItem = new OneUploadedItemViewModel();
					oneItem.getFiles().addAll(filesInfo);

					result.getPayload().add(oneItem);
				}
				else {
					final ArrayList<IOUtilsStoredFileInfoModel> fileInfoArrayList = new ArrayList<IOUtilsStoredFileInfoModel>();
					fileInfoArrayList.add(fileInfo);

					final OneUploadedItemViewModel oneItem = new OneUploadedItemViewModel();
					oneItem.getFiles().add(fileInfo);

					result.getPayload().add(oneItem);
				}
			}

			result.isSuccessful = true;
			result.message = "Files are uploaded successfully";

		} catch (final IOException e) {

			this.logger.error("StoreUploadedFiles failed!", e);

			result.isSuccessful = false;
			result.message = "There seems to be an error, try later.";
		} catch (final ArchiveException e) {

			this.logger.error("StoreUploadedFiles failed!", e);

			result.isSuccessful = false;
			result.message = "There seems to be an error, try later.";
		}

		return result;
	}

	/**
	 * Gets sheets/tables information from submitted files.
	 * @param createTemplateViewModel is the model which describes uploaded files.
	 * @return the response which has file content info in the payload field.
	 */
	public FileContentInfoReponse getFilesContentInfo(final CreateTemplateViewModel createTemplateViewModel) {
		final FileContentInfoReponse result = new FileContentInfoReponse();

		try {

			for (final OneUploadedItemViewModel oneItem : createTemplateViewModel.getFileName()) {
				if (oneItem.getFiles().isEmpty()) {
					//TODO:might need to handle it differently
					continue;
				}

				final FileContentInfoViewModel oneFileContentInfo = new FileContentInfoViewModel();

				final IOUtilsStoredFileInfoModel fileModel = oneItem.getFiles().get(0);

				oneFileContentInfo.setExtension(fileModel.getFileExtension());
				oneFileContentInfo.setFileName(fileModel.getFileName());
				oneFileContentInfo.setFileAbsoluteName(fileModel.getAbsoluteFileName());

				final String[] otherFiles = new String[oneItem.getFiles().size() - 1];
				for (int i = 1; i < oneItem.getFiles().size(); i++) {
					otherFiles[i - 1] = oneItem.getFiles().get(i).getAbsoluteFileName();
				}

				oneFileContentInfo.setOtherFilesAbsoluteNames(otherFiles);

				final ImporterType importerType = ImporterType.getImporterType(fileModel.getFileExtension());

				final Importer importer = ImporterFactory.getImporter(importerType);
				oneFileContentInfo.getWorksheets().addAll(importer.getTables(fileModel));

				result.getPayload().add(oneFileContentInfo);
				result.isSuccessful = true;
				result.message = "OK";
			}

		} catch (final Exception e) {
			this.logger.error("getFilesContentInfo failed!", e);

			result.isSuccessful = false;
			result.message = "There seems to be an error, try later.";
		}

		return result;
	}


	/**
	 * Gets variables from selected sheets/tables.
	 *
	 * @param filesWithSelectedSheets info about files with selected sheets/tables.
	 * @return response which has in payload into about variables from selected sheets/tables.
	 */
	public FileContentInfoReponse getFilesVariablesAndRecomendations(final List<FileContentInfoViewModel> filesWithSelectedSheets) {
		final FileContentInfoReponse result = new FileContentInfoReponse();

		try {

			for (final FileContentInfoViewModel oneFile : filesWithSelectedSheets) {

				final ImporterType importerType = ImporterType.getImporterType(oneFile.getExtension());

				final Importer importer = ImporterFactory.getImporter(importerType);

				final HashMap<String, ArrayList<DatasetVariableViewModel>> variablesForAllSelectedSheetsInFile = importer.readVariables(oneFile);

				for (final WorksheetViewModel worksheet : oneFile.getWorksheets()) {
					if (variablesForAllSelectedSheetsInFile.containsKey(worksheet.getSheetName())) {
						worksheet.setVariables(variablesForAllSelectedSheetsInFile.get(worksheet.getSheetName()));
					}
				}
			}

			result.setPayload((ArrayList<FileContentInfoViewModel>)filesWithSelectedSheets);

			result.isSuccessful = true;
			result.message = "OK";

		} catch (final Exception e) {
			this.logger.error("getFilesContentInfo failed!", e);

			result.isSuccessful = false;
			result.message = "There seems to be an error, try later.";
		}

		return result;
	}

	/**
	 * Accepts information about several files includes several worksheets in any of them. Gets the metadata of each variable and stores in the
	 * database in the dnameinfo table and columnTable info.
	 * @param dataMatchingStepData {@link List} list of files info {@link FileContentInfoViewModel}
	 * @return {@link FileContentInfoReponse} with success or error message no payload.
	 */
	public GeneralResponseImpl saveVariablesMetadata(final FilesContentInfoViewModel filesInfo) {
		final FileContentInfoReponse result = new FileContentInfoReponse();

		try {

			final DNameInfoManager dNameInfoMgr = new DNameInfoManagerImpl();

			for (final FileContentInfoViewModel file : filesInfo.getFiles()) {

				for (final WorksheetViewModel worksheet : file.getWorksheets()) {
					dNameInfoMgr.createOrUpdateSheetMetadata(worksheet, filesInfo.getSid(), filesInfo.getUserId());
				}
			}

			result.isSuccessful = true;
			result.message = "OK";

		} catch (final Exception e) {
			this.logger.error("getFilesContentInfo failed!", e);

			result.isSuccessful = false;
			result.message = "There seems to be an error, try later.";
		}

		return result;
	}

	/**
	 * Creates a KTR file for each file.
	 * @param dataMatchingStepData
	 * @return
	 */
	public GeneralResponseImpl generateKTR(final FilesContentInfoViewModel dataMatchingStepData) {

		final GeneralResponseImpl result = new GeneralResponseImpl();
		result.isSuccessful = true;
		result.message = "OK";

		final KTRManager ktrManager = new KTRManager(dataMatchingStepData.getSid());

		for(final FileContentInfoViewModel file : dataMatchingStepData.getFiles()) {
			try {
				ktrManager.createTemplate(file);
			} catch (final Exception e) {

				final String msg = String.format("create ktr failed for %s file", file.getFileName());

				this.logger.error(msg, e);
				result.message += "\n" + msg;

				result.isSuccessful = false;
			}
		}

		return result;
	}

	/**
	 * Triggers background execution of the ETL job: e.g. either running KTR execution or importing data from database dump file.
	 * @param sid
	 * @return
	 */
	public GeneralResponseImpl triggerDataLoadExecution(final int sid) {
		final GeneralResponseImpl result = new GeneralResponseImpl();

		result.isSuccessful = true;
		result.message = "OK";

		//TODO: make catch more granular
		try {

			final SourceInfoManager storyMng = new SourceInfoManagerImpl();

			final DataSourceTypes sourceType = storyMng.getStorySourceType(sid);

			final DataLoadExecutor executor = DataLoadExecutorFactory.getDataLoadExecutor(sourceType);

			executor.setSid(sid);

			ProcessManager.getInstance().queueProcess(executor);

		} catch (final Exception e) {
			result.isSuccessful = false;
			result.message = "Couldn't trigger KTR execution";

			this.logger.error("triggerDataLoadExecution failed: Couldn't trigger KTR execution for " + sid);
		}


		return result;
	}

	/**
	 * Read a range of rows from all sheets/tables in given file. The rage is specified by the number of rows to read and the page number.
	 *
	 * @param previewFileViewModel has info about the data file and row rages.
	 * @return the {@link PreviewFileResponse} response in which payload is the data read from the file.
	 */
	public PreviewFileResponse getDataPreviewFromFile(final PreviewFileViewModel previewFileViewModel) {

		final PreviewFileResponse result = new PreviewFileResponse();
		result.isSuccessful = false;

		final ImporterType importerType = ImporterType.getImporterType(FilenameUtils.getExtension(previewFileViewModel.getFileAbsoluteName()));
		Importer importer = null;
		try {
			importer = ImporterFactory.getImporter(importerType);
		} catch (final Exception e) {
			this.logger.error(String.format("getDataPreviewFromFiles failed for %s", previewFileViewModel.toString()), e);

			result.message = String.format("Couldn't read data from %s file.", previewFileViewModel.getFileName());

			return result;
		}

		if (importer == null) {
			this.logger.error(String.format("getDataPreviewFromFiles failed for %s. The imported is null.", previewFileViewModel.toString()));

			result.message = String.format("Couldn't read data from %s file.", previewFileViewModel.getFileName());

			return result;
		}

		try {
			previewFileViewModel.setWorksheetsData(importer.readWorksheetData(previewFileViewModel));
		} catch (final FileNotFoundException e) {
			this.logger.error(String.format("getDataPreviewFromFiles failed for %s. File not found.", previewFileViewModel.toString()), e);

			result.message = String.format("Couldn't read data from %s file. Could not find the file to read from.", previewFileViewModel.getFileName());

			return result;
		} catch (final IOException e) {
			this.logger.error(String.format("getDataPreviewFromFiles failed for %s. File not found.", previewFileViewModel.toString()), e);

			result.message = String.format("Couldn't read data from %s file.", previewFileViewModel.getFileName());

			return result;
		}

		result.isSuccessful = true;
		result.setPayload(previewFileViewModel);

		return result;
	}


	public OneNumberResponse estimateDataPreviewFromFile(final PreviewFileViewModel previewFileViewModel) {

		final OneNumberResponse result = new OneNumberResponse();

		result.isSuccessful = true;
		result.message = "OK";

		//TODO: implement
		result.setPayload(1000);

		return result;
	}
}
