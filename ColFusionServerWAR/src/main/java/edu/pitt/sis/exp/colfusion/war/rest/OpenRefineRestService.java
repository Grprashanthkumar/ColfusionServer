/**
 * 
 */
package edu.pitt.sis.exp.colfusion.war.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.CreateProject;
import edu.pitt.sis.exp.colfusion.bll.OpenRefineSaveChanges;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen;

/**
 * @author xxl
 *
 */
@Path("OpenRefine/")
public class OpenRefineRestService{
	final Logger logger = LogManager.getLogger(OpenRefineRestService.class.getName());
	
	
	@Path("createNewProject/{sid}/{tableName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)    
	public Response createNewProject(@PathParam("sid") final int sid, @PathParam("tableName") final String tableName) throws ClassNotFoundException, Exception {

		CreateProject createProject = new CreateProject();
		GeneralResponseGen<String> result = createProject.testCreateProject(sid, tableName);
		
    	return Response.status(200).entity(result).build(); //.build();
    }
	

	
	@Path("saveChanges/{openRefinProjectId}/{colfusionUserId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response saveChanges(@PathParam("openRefinProjectId") final String openRefinProjectId,
    		@PathParam("colfusionUserId") final String colfusionUserId) throws ClassNotFoundException, Exception {

		OpenRefineSaveChanges openRefineSaveChanges = new OpenRefineSaveChanges();
		GeneralResponseGen<String> result = openRefineSaveChanges.saveChanges(openRefinProjectId, colfusionUserId);
		
    	return Response.status(200).entity(result).build(); //.build();
    }
}
