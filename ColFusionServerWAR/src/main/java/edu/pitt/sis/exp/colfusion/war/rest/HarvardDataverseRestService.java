package edu.pitt.sis.exp.colfusion.war.rest;

import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import edu.pitt.sis.exp.colfusion.dataverse.DataverseBL;
import edu.pitt.sis.exp.colfusion.responseModels.AcceptedFilesResponse;

@Path("HDataverse/")
public class HarvardDataverseRestService {

	final Logger logger = LogManager.getLogger(HarvardDataverseRestService.class.getName());
	
	
	//Download file from Harvard Dataverse
	@Path("DownLoad/{ids}/{name}/{sid}/{uploadTimestamp}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
	public  Response dataverseDownloadRevise(@PathParam("name") final String name,@PathParam("ids") final String ids,@PathParam("sid") final String sid,@PathParam("uploadTimestamp") final String uploadTimestamp) throws IOException{
		AcceptedFilesResponse result;
		int id=Integer.parseInt(ids);
		DataverseBL dataverSerivce = new DataverseBL();
		result = dataverSerivce.dataverseById(id,name,sid,uploadTimestamp);
		return Response.status(200).entity(result).build(); 
	
	}


	//Search files in the Harvard Dataverse with file name and dataverse name 
	
	@Path("DataverseName/{name}/{dataverseName}")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public  Response dataverseFilesRevise(@PathParam("name") final String name, @PathParam("dataverseName") final String dataverseName){
		boolean result = false;
		
		DataverseBL dataverSerivce = new DataverseBL();
		ArrayList< String> filesArray=new ArrayList<String>();
		filesArray = dataverSerivce.dataverseByNameRevise(name, dataverseName,"");
		 String[] s=new String[filesArray.size()];
		 for(int i=0;i<filesArray.size();i++){
			 s[i]=filesArray.get(i);
		 }
		 
		 Gson gson = new Gson();
		 String json = gson.toJson(filesArray);
		 
		 return Response.status(200).entity(json).build();
		}
	
	//Search files in the Harvard Dataverse with file name and dataverse name and dataset name 
	
	@Path("DataverseName/{name}/{dataverseName}/{datasetName}")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public  Response dataverseFilesRevise(@PathParam("name") final String name,@PathParam("datasetName") final String datasetName, @PathParam("dataverseName") final String dataverseName){
		boolean result = false;
		
		DataverseBL dataverSerivce = new DataverseBL();
		ArrayList< String> filesArray=new ArrayList<String>();
		filesArray = dataverSerivce.dataverseByNameRevise(name, dataverseName,datasetName);
		 String[] s=new String[filesArray.size()];
		 for(int i=0;i<filesArray.size();i++){
			 s[i]=filesArray.get(i);
		 }
		 
		 Gson gson = new Gson();
		 String json = gson.toJson(filesArray);
		 
		 return Response.status(200).entity(json).build();
		}

}
