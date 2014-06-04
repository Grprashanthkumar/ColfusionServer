/**
 * 
 */
package edu.pitt.sis.exp.colfusion.controllers;

import java.sql.SQLException;

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

import edu.pitt.sis.exp.colfusion.bll.CancelPreview;
import edu.pitt.sis.exp.colfusion.bll.CreateProject;
import edu.pitt.sis.exp.colfusion.bll.OpenRefineBL;
import edu.pitt.sis.exp.colfusion.bll.SavePreview;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen;
//import edu.pitt.sis.exp.colfusion.tests.openrefine.TestCreatePro;
//import edu.pitt.sis.exp.colfusion.tests.openrefine.TestCreatePro2;

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
    @Path("getProjectId/{sid}/{tableName}")
    public Response getProjectId(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	

	@Path("getProjectId/{sid}/{tableName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectId(@PathParam("sid") int sid, @PathParam("tableName") String tableName) {
    	
		
		OpenRefineBL openRefineBL = new OpenRefineBL();
		
//		JOptionPane.showMessageDialog(null, "This is a test msg", "Test Msg", JOptionPane.WARNING_MESSAGE); // Alex
		// do some test in "testCreateProject"
//		openRefineBL.testCreateProject();
		
//		TestCreatePro.testCreatePro();
		
		GeneralResponseGen<String> result = openRefineBL.getProjectID(sid, tableName);
		
    	
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@OPTIONS
    @Path("cancelPreview/{sid}/{tableName}")
    public Response cancelPreview(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	

	@Path("cancelPreview/{sid}/{tableName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelPreview(@PathParam("sid") int sid, @PathParam("tableName") String tableName) throws SQLException {
    	
		
		CancelPreview cancelPreview = new CancelPreview();
		
		GeneralResponseGen<String> result = cancelPreview.cancelPreview(sid, tableName);
		
    	
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@OPTIONS
    @Path("savePreview/{sid}/{tableName}")
    public Response savePreview(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	

	@Path("savePreview/{sid}/{tableName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response savePreview(@PathParam("sid") int sid, @PathParam("tableName") String tableName) throws SQLException {
    	
		
		SavePreview savePreview = new SavePreview();
		
		GeneralResponseGen<String> result = savePreview.savePreview(sid, tableName);
		
    	
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
}
