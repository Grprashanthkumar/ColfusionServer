package edu.pitt.sis.exp.colfusion.war.rest.api;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import edu.pitt.sis.exp.colfusion.bll.DataverseBL;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.OneUploadedItemViewModel;
import edu.pitt.sis.exp.colfusion.dataverse.DataverseFileInfo;
import edu.pitt.sis.exp.colfusion.responseModels.AcceptedFilesResponse;
import edu.pitt.sis.exp.colfusion.war.rest.responses.RestResponseBuilder;
import edu.pitt.sis.exp.colfusion.war.rest.viewModels.harwardDataverse.GetDataFileViewModel;
import edu.pitt.sis.exp.colfusion.war.rest.viewModels.harwardDataverse.SearchForFileResultViewModel;

@Api(value = "/HarvardDataverse", description = "Harvard Dataverse reslated services")
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
	@Path("getDataFile")
	@POST
	@ApiOperation(
			value = "Get data file from dataverse server and stores it in colfusion as uploaded data file from local machine",
			notes = "",
			response = AcceptedFilesResponse.class)
	//TODO: come back and fix this annotations
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid ID supplied"),
			@ApiResponse(code = 404, message = "StoryMetadata not found") })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataFile(final GetDataFileViewModel getDataFileViewModel) throws IOException{

		final DataverseBL dataverSerivce = new DataverseBL();
		final List<OneUploadedItemViewModel> result = dataverSerivce.getDatafile(getDataFileViewModel.getSid(),
				getDataFileViewModel.getFileId(), getDataFileViewModel.getFileName());

		return RestResponseBuilder.build(result, true, "", Status.OK);
	}

	/**
	 * Search files in the Harvard Dataverse with file name and dataverse name.
	 *
	 * @param name
	 * @param dataverseName
	 * @return
	 */
	@Path("searchForFile")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response dataverseFilesRevise(@QueryParam("fileName") final String fileName,
			@QueryParam("dataverseName") final String dataverseName,
			@DefaultValue("") @QueryParam("datasetName") final String datasetName) {
		final DataverseBL dataverSerivce = new DataverseBL();
		final List<DataverseFileInfo> filesArray = dataverSerivce.searchForFile(fileName, dataverseName, datasetName);

		final List<SearchForFileResultViewModel> result = filesArray.stream().map(f -> new SearchForFileResultViewModel(f)).collect(Collectors.toList());

		return RestResponseBuilder.build(result, true, "", Status.OK);
	}
}
