package edu.pitt.sis.exp.colfusion.war.rest;

import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import edu.pitt.sis.exp.colfusion.dataverse.DataverseBL;
import edu.pitt.sis.exp.colfusion.responseModels.AcceptedFilesResponse;

@Path("HarvardDataverse/")
public class HarvardDataverseRestService {

	final Logger logger = LogManager.getLogger(HarvardDataverseRestService.class.getName());

	/**
	 * Download file from Harvard Dataverse.
	 *
	 * @param name
	 * @param ids
	 * @param sid
	 * @param uploadTimestamp
	 * @return
	 * @throws IOException
	 */
	@Path("Download/{ids}/{name}/{sid}/{uploadTimestamp}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response dataverseDownloadRevise(@PathParam("name") final String name, @PathParam("ids") final String ids,
			@PathParam("sid") final String sid, @PathParam("uploadTimestamp") final String uploadTimestamp) throws IOException{
		final int id = Integer.parseInt(ids);
		final DataverseBL dataverSerivce = new DataverseBL();
		final AcceptedFilesResponse result = dataverSerivce.dataverseById(id, name, sid, uploadTimestamp);
		return Response.status(200).entity(result).build();
	}

	/**
	 * Search files in the Harvard Dataverse with file name and dataverse name.
	 *
	 * @param name
	 * @param dataverseName
	 * @return
	 */
	@Path("Search")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response dataverseFilesRevise(@QueryParam("fileName") final String fileName,
			@QueryParam("dataverseName") final String dataverseName,
			@DefaultValue("") @QueryParam("datasetName") final String datasetName) {
		final DataverseBL dataverSerivce = new DataverseBL();
		final ArrayList< String> filesArray = dataverSerivce.dataverseByNameRevise(fileName, dataverseName, datasetName);

		final String[] s = new String[filesArray.size()];
		for(int i = 0; i < filesArray.size(); i++){
			s[i] = filesArray.get(i);
		}

		final Gson gson = new Gson();
		final String json = gson.toJson(filesArray);

		return Response.status(200).entity(json).build();
	}
}
