package edu.pitt.sis.exp.colfusion.dataverse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.importers.excel.ExcelFile;
import edu.pitt.sis.exp.colfusion.importers.excel.ExcelFileHandler;
import edu.pitt.sis.exp.colfusion.importers.excel.ExcelFileHandlerImpl;
import edu.pitt.sis.exp.colfusion.utils.IOUtils;
import edu.pitt.sis.exp.colfusion.utils.UnitTestBase;

/**
 * Tests {@link DataverseClientImpl}
 */
public class DataverseClientTest extends UnitTestBase {

	private final String DATAVERSE_SERVER_ADDRESS = "dataverse.harvard.edu"; //Should use this after able to create file on dataverse: apitest.dataverse.org
	private final String DATAVERSE_TOKEN_KEY = "551ceb21-bd44-456a-abe0-12d7412f401b"; // Get token for apitest server

	private DataverseClient getDataverseClient() {
		final DataverseContext dataverseContext = new DataverseContextImpl(this.DATAVERSE_SERVER_ADDRESS, this.DATAVERSE_TOKEN_KEY);
		return new DataverseClientImpl(dataverseContext);
	}

	@Test
	public void testDataverSearchResultItemJsonItemToDataverseFileInfo() throws JSONException {
		final String item = "{\n" +
				"                \"name\":\"trees.png\",\n" +
				"                \"type\":\"file\",\n" +
				"                \"url\":\"https://apitest.dataverse.org/api/access/datafile/12\",\n" +
				"                \"image_url\":\"https://apitest.dataverse.org/api/access/preview/12\",\n" +
				"                \"file_id\":\"12\",\n" +
				"                \"description\":\"\",\n" +
				"                \"published_at\":\"2015-01-12T16:05:44Z\",\n" +
				"                \"file_type\":\"PNG Image\",\n" +
				"                \"size_in_bytes\":8361,\n" +
				"                \"md5\":\"0386269a5acb2c57b4eade587ff4db64\",\n" +
				"                \"dataset_citation\":\"Spruce, Sabrina, 2015, \\\"Spruce Goose\\\", http://dx.doi.org/10.5072/FK2/Y6RGTQ,  Root Dataverse,  V1\"\n" +
				"            }";

		final JSONObject jsonItem = new JSONObject(item);

		final DataverseFileInfo dataverseFileInfo = DataverseClientImpl.dataverSearchResultItemJsonItemToDataverseFileInfo(jsonItem);

		assertEquals("Wrong parsed file name", "trees.png", dataverseFileInfo.getFileName());
		assertEquals("Wrong parsed file id", "12", dataverseFileInfo.getFileId());
		assertEquals("Wrong parsed published at", "2015-01-12T16:05:44Z", dataverseFileInfo.getPublishedAt());
		assertEquals("Wrong parsed size in bytes", 8361, dataverseFileInfo.getSize());
		assertEquals("Wrong parsed dataset citation", "Spruce, Sabrina, 2015, \"Spruce Goose\", http://dx.doi.org/10.5072/FK2/Y6RGTQ,  Root Dataverse,  V1",
				dataverseFileInfo.getCitation());
	}

	@Test
	public void testSearchForFile() {
		final DataverseClient dataverseClient = getDataverseClient();

		final List<DataverseFileInfo> files = dataverseClient.searchForFile("Mortality.monthly_Madras.India_1916.1921",
				"worldhistorical", "Monthly mortality data of Madras, India from 1916 to 1921");

		assertEquals("Wrong number of files", 1, files.size());

		final DataverseFileInfo file1 = files.get(0);

		assertEquals("Wrong parsed file name", "Mortality.monthly_Madras.India_1916.1921.xlsx", file1.getFileName());
		assertEquals("Wrong parsed file id", "2681803", file1.getFileId());
	}

	//TODO: we should not test on real files in dataverse, instead use dataverse test api
	// the problem is that they say that files on test api web site are deleted periodically
	// so it would be nice if we could first "submit" a file programmatically to the dataverse.
	@Test
	public void testGetDatafileCorrectId() throws IOException {
		final DataverseClient dataverseClient = getDataverseClient();

		final InputStream fileInputStream = dataverseClient.getDatafile("2681803");

		final File dataFile = this.tempFolder.newFile("dataFileFromDataverse.xlsx");

		IOUtils.writeToFile(fileInputStream, dataFile, true);

		final ExcelFileHandler excelFileHandler = new ExcelFileHandlerImpl();
		try (ExcelFile excelFile = excelFileHandler.openFile(dataFile.getAbsolutePath())) {

		}

		assertNotNull("Got input stream that is null", fileInputStream);
	}

	@Test(expected = FileNotFoundException.class)
	public void testGetDatafileWrongId() throws FileNotFoundException {
		final DataverseClient dataverseClient = getDataverseClient();

		final InputStream fileInputStream = dataverseClient.getDatafile("123");

		assertNotNull("Got input stream that is null", fileInputStream);
	}
}
