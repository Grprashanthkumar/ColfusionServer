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
	
	static String dataverseKey = "0fc954b0-e7d8-45df-b8ea-4a39066bbdbd";

	public AcceptedFilesResponse dataverseById( final int id,  final String name,final String sid, final String uploadTimestamp) throws IOException {
		
	
		String idString =String.valueOf(id);
		
		//Sent rest api by id
		
		//binary inputstream -> save to upload
		
		Client c = ClientBuilder.newClient();
		WebTarget target = c.target(UriBuilder.fromUri("https://apitest.dataverse.org/api/access/datafile/" + idString + "?format=original").build());
        InputStream responseMsg = target.request().get(InputStream.class);
        
		
		String uploadFilesLocation = IOUtils.getAbsolutePathInColfution(ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_UPLOAD_FILES_FOLDER));
		String uploadFileAbsolutePath = uploadFilesLocation + File.separator + sid; 
		AcceptedFilesResponse result = new AcceptedFilesResponse();
		// save it
		
		IOUtilsStoredFileInfoModel fileInfo = IOUtils.writeInputStreamToFile(responseMsg, uploadFileAbsolutePath, name, true);
		ArrayList<IOUtilsStoredFileInfoModel> fileInfoArrayList = new ArrayList<IOUtilsStoredFileInfoModel>();
		fileInfoArrayList.add(fileInfo);
		
		OneUploadedItemViewModel oneItem = new OneUploadedItemViewModel();
		oneItem.getFiles().add(fileInfo);
		
		result.getPayload().add(oneItem);
		result.isSuccessful = true;
		result.message = "Files are uploaded successfully";
	
        return result;
	}
	

	
	
	
public ArrayList<String> dataverseByNameRevise(String file_name, String dataverseName, String datasetName) {
		
		//Sent rest api by file_name = trees
		
		//binary inputstream -> save to upload
		
		Client c = ClientBuilder.newClient();
		
		String url = "https://apitest.dataverse.org/api/search?q=" + file_name + "&subtree=" + dataverseName + "&key=" + dataverseKey;
		
        WebTarget target = c.target(UriBuilder.fromUri(url).build());

        String responseMsg = target.request().get(String.class);
        System.out.println(responseMsg);
       JSONObject json;
       
       // This ArrayList will store files information 
       ArrayList<String> filesArray=new ArrayList<String>();
	try {
		   json = new JSONObject(responseMsg);
	       JSONObject data = json.getJSONObject("data");
	       JSONArray item = data.getJSONArray("items");
	       for(int i=0; i<item.length();i++){
	    	   org.json.JSONObject file = item.getJSONObject(i);
	    	   
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
	       
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		return filesArray;
		
	}



}
