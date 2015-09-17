package edu.pitt.sis.exp.colfusion.war.rest.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
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
	 * @param getDataFileViewModel parameters for the file to get form dataverse.
	 * @return
	 * @throws IOException Request to dataverse server return status not equal 200 and so either something is wrong with
	 *					dataverse service, or with user provided parameters.
	 */
	@Path("getDataFile")
	@POST
	@ApiOperation(
			value = "Get data file from dataverse server and stores it in colfusion as uploaded data file from local machine.",
			notes = "",
			response = AcceptedFilesResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Datafile from dataverse was successfully uploaded into colfusion server."),
			@ApiResponse(code = 403, message = "If access to the specified datafile has been forbiden (e.g. user doesn't have rights to read that file."),
			@ApiResponse(code = 404, message = "Request to dataverse server return status not equal 200 and so either something is wrong with "
					+ "dataverse service, or with user provided parameters.")})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataFile(final GetDataFileViewModel getDataFileViewModel) {

		final DataverseBL dataverSerivce = new DataverseBL();

		try {
			final List<OneUploadedItemViewModel> result = dataverSerivce.getDatafile(getDataFileViewModel.getSid(),
					getDataFileViewModel.getFileId(), getDataFileViewModel.getFileName());

			return RestResponseBuilder.build(result, true, "", Status.OK);
		}
		catch(final FileNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		} catch (final AccessDeniedException e) {
			return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
		}
	}

	/**
	 * Search files in the Harvard Dataverse with file name and dataverse name.
	 *
	 * @param fileName
	 * @param dataverseName
	 * @param datasetName
	 * @return
	 */
	@Path("searchForFile")
	@GET
	@ApiOperation(
			value = "Search files in the Harvard Dataverse with given file name, dataverse name, and dataset name.",
			notes = "",
			response = SearchForFileResultViewModel.class,
			responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Search request was successfull (don't mean that anything was found though).")})
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchForFile(@QueryParam("fileName") final String fileName,
			@QueryParam("dataverseName") final String dataverseName,
			@DefaultValue("") @QueryParam("datasetName") final String datasetName) {
		final DataverseBL dataverSerivce = new DataverseBL();
		final List<DataverseFileInfo> filesArray = dataverSerivce.searchForFile(fileName, dataverseName, datasetName);

		final List<SearchForFileResultViewModel> result = filesArray.stream().map(f -> new SearchForFileResultViewModel(f)).collect(Collectors.toList());

		return RestResponseBuilder.build(result, true, "", Status.OK);
	}
}
