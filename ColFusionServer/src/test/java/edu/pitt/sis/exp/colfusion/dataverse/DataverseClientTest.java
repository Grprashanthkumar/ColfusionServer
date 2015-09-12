package edu.pitt.sis.exp.colfusion.dataverse;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class DataverseClientTest {

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
		final DataverseClientImpl dataverseClient = new DataverseClientImpl();

		final List<DataverseFileInfo> files = dataverseClient.searchForFile("Mortality.monthly_Madras.India_1916.1921",
				"worldhistorical", "Monthly mortality data of Madras, India from 1916 to 1921");

		assertEquals("Wrong number of files", 1, files.size());

		//TODO: make sure if order is always the same
		final DataverseFileInfo file1 = files.get(0);

		assertEquals("Wrong parsed file name", "Mortality.monthly_Madras.India_1916.1921.xlsx", file1.getFileName());
		assertEquals("Wrong parsed file id", "2681803", file1.getFileId());
	}
}
