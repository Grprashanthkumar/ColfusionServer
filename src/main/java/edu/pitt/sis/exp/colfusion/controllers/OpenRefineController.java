/**
 * 
 */
package edu.pitt.sis.exp.colfusion.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
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
public class OpenRefineController  extends BaseController {
	final Logger logger = LogManager.getLogger(OpenRefineController.class.getName());
	

	@OPTIONS
    @Path("createNewProject/{sid}/{tableName}")
    public Response createNewProject(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	

	@Path("createNewProject/{sid}/{tableName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response newStoryMetadata(@PathParam("sid") int sid, @PathParam("tableName") String tableName) throws ClassNotFoundException, Exception {

		CreateProject createProject = new CreateProject();
		GeneralResponseGen<String> result = createProject.testCreateProject(sid, tableName);
		
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	
	@OPTIONS
    @Path("saveChanges/{openRefinProjectId}")
    public Response saveChangesOptions(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	

	@Path("saveChanges/{openRefinProjectId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveChanges(@PathParam("openRefinProjectId") String openRefinProjectId) throws ClassNotFoundException, Exception {

		OpenRefineSaveChanges openRefineSaveChanges = new OpenRefineSaveChanges();
		GeneralResponseGen<String> result = openRefineSaveChanges.saveChanges(openRefinProjectId);
		
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
}
