package edu.pitt.sis.exp.colfusion.dataverse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.OneUploadedItemViewModel;
import edu.pitt.sis.exp.colfusion.responseModels.AcceptedFilesResponse;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.IOUtils;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;

public class DataverseBL {

	//dataverse users key. It dosen't work possibly because daverse website deletes users sometimes.
	//So you should sign up and get user key again.
	//The website is https://apitest.dataverse.org/dataverse/root

	//TODO: see issue #11 and #12
	static String dataverseKey = "551ceb21-bd44-456a-abe0-12d7412f401b";

	public AcceptedFilesResponse dataverseById(final int id, final String name, final String sid, final String uploadTimestamp) throws IOException {

		final String idString = String.valueOf(id);

		final Client c = ClientBuilder.newClient();
		final WebTarget target = c.target(UriBuilder.fromUri("https://apitest.dataverse.org/api/access/datafile/" + idString + "?format=original").build());
		final InputStream responseMsg = target.request().get(InputStream.class);

		final String uploadFilesLocation = IOUtils.getAbsolutePathInColfution(ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_UPLOAD_FILES_FOLDER));
		final String uploadFileAbsolutePath = uploadFilesLocation + File.separator + sid;
		final AcceptedFilesResponse result = new AcceptedFilesResponse();
		// save it

		final IOUtilsStoredFileInfoModel fileInfo = IOUtils.writeInputStreamToFile(responseMsg, uploadFileAbsolutePath, name, true);
		final ArrayList<IOUtilsStoredFileInfoModel> fileInfoArrayList = new ArrayList<IOUtilsStoredFileInfoModel>();
		fileInfoArrayList.add(fileInfo);

		final OneUploadedItemViewModel oneItem = new OneUploadedItemViewModel();
		oneItem.getFiles().add(fileInfo);

		result.getPayload().add(oneItem);
		result.isSuccessful = true;
		result.message = "Files are uploaded successfully";

		return result;
	}

	public ArrayList<String> dataverseByNameRevise(final String file_name, final String dataverseName, final String datasetName) {

		//Sent rest api by file_name = trees

		//binary inputstream -> save to upload

		final Client c = ClientBuilder.newClient();

		final String url = "https://apitest.dataverse.org/api/search?q=" + file_name + "&subtree=" + dataverseName + "&key=" + dataverseKey;

		final WebTarget target = c.target(UriBuilder.fromUri(url).build());

		final String responseMsg = target.request().get(String.class);
		System.out.println(responseMsg);
		JSONObject json;

		// This ArrayList will store files information
		final ArrayList<String> filesArray=new ArrayList<String>();
		try {
			json = new JSONObject(responseMsg);
			final JSONObject data = json.getJSONObject("data");
			final JSONArray item = data.getJSONArray("items");
			for(int i=0; i<item.length();i++){
				final org.json.JSONObject file = item.getJSONObject(i);

				//compare dataset name to narrow result
				if(!(datasetName.equals(""))&&file.getString("dataset_citation").contains(datasetName)){
					filesArray.add(file.toString());
					System.out.println(file.toString());
				}
				else if(datasetName.equals("")){
					filesArray.add(file.toString());
					System.out.println(file.toString());
				}
			}

		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return filesArray;
	}
}
