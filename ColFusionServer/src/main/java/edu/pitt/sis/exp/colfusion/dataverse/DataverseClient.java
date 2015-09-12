package edu.pitt.sis.exp.colfusion.dataverse;

import java.io.InputStream;
import java.util.List;

/**
 * Dataverse REST API client.
 */
public interface DataverseClient {
	/**
	 *
	 * @return
	 */
	public InputStream getDatafile();

	/**
	 *
	 * @param fileName
	 * @param dataverseName
	 * @param datasetName
	 * @return
	 */
	public List<DataverseFileInfo> searchForFile(String fileName, String dataverseName, String datasetName);
}
