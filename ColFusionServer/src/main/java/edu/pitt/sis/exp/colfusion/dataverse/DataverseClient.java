package edu.pitt.sis.exp.colfusion.dataverse;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Dataverse REST API client.
 */
public interface DataverseClient {
	/**
	 * Get the data file form dataverse by given file id.
	 *
	 * @return {@link InputStream} that represent the data file
	 * @throws FileNotFoundException if dataverse return any response not equal to 200
	 */
	public InputStream getDatafile(String fileId) throws FileNotFoundException;

	/**
	 * Search for a data file with given parameters (filename, dataverse name and dataset name)
	 *
	 * @param fileName
	 * @param dataverseName
	 * @param datasetName
	 * @return {@link List} of {@link DataverseFileInfo} that match search criteria. In case dataverse return an error or a no file found,
	 * an empty list is returned.
	 */
	public List<DataverseFileInfo> searchForFile(String fileName, String dataverseName, String datasetName);
}
