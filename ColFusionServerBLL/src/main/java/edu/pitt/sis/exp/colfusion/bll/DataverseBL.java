package edu.pitt.sis.exp.colfusion.bll;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizzard.DataSubmissionWizzardBL;
import edu.pitt.sis.exp.colfusion.bll.dataverse.DataverseClient;
import edu.pitt.sis.exp.colfusion.bll.dataverse.DataverseClientImpl;
import edu.pitt.sis.exp.colfusion.bll.dataverse.DataverseContext;
import edu.pitt.sis.exp.colfusion.bll.dataverse.DataverseContextImpl;
import edu.pitt.sis.exp.colfusion.bll.dataverse.DataverseFileInfo;
import edu.pitt.sis.exp.colfusion.bll.responseModels.AcceptedFilesResponse;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.OneUploadedItemViewModel;

/**
 *
 */
public class DataverseBL {

	//dataverse users key. It dosen't work possibly because daverse website deletes users sometimes.
	//So you should sign up and get user key again.
	//The website is https://apitest.dataverse.org/dataverse/root

	//TODO: see issue #11 and #12
	private final String DATAVERSE_TOKEN_KEY = "551ceb21-bd44-456a-abe0-12d7412f401b";

	//TODO: apparently server base address should also be user provided
	// because there are a number of dataverse servers, CHIA uses harvard's server
	private final String DATAVERSE_SERVER_ADDRESS = "dataverse.harvard.edu"; //"https://dataverse.harvard.edu/api";

	/**
	 *
	 * @param sid
	 * @param fileId
	 * @param fileName
	 * @return
	 * @throws AccessDeniedException
	 * @throws IOException
	 */
	public ArrayList<OneUploadedItemViewModel> getDatafile(final String sid, final String fileId, final String fileName) throws FileNotFoundException, AccessDeniedException {

		final DataverseContext dataverseContext = new DataverseContextImpl(this.DATAVERSE_SERVER_ADDRESS, this.DATAVERSE_TOKEN_KEY);
		final DataverseClient dataverseClient = new DataverseClientImpl(dataverseContext);

		final InputStream dataFile = dataverseClient.getDatafile(fileId);

		//TODO: DataSubmissionWizzardBL should be injected
		final DataSubmissionWizzardBL dataSubmissionWizzardBL = new DataSubmissionWizzardBL();
		final Map<String, InputStream> file = new HashMap<>();
		file.put(fileName, dataFile);

		//TODO FIXME after BL file return Business logic model, need to change here
		final AcceptedFilesResponse result = dataSubmissionWizzardBL.storeUploadedFiles(sid, file);

		return result.getPayload();
	}

	/**
	 *
	 * @param fileName
	 * @param dataverseName
	 * @param datasetName
	 * @return
	 */
	public List<DataverseFileInfo> searchForFile(final String fileName, final String dataverseName, final String datasetName) {

		final DataverseContext dataverseContext = new DataverseContextImpl(this.DATAVERSE_SERVER_ADDRESS, this.DATAVERSE_TOKEN_KEY);
		final DataverseClient dataverseClient = new DataverseClientImpl(dataverseContext);

		final List<DataverseFileInfo> result = dataverseClient.searchForFile(fileName, dataverseName, datasetName);

		return result;
	}
}
