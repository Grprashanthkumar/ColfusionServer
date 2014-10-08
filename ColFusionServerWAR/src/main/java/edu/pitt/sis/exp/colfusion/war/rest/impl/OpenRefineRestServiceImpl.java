/**
 * 
 */
package edu.pitt.sis.exp.colfusion.war.rest.impl;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.CreateProject;
import edu.pitt.sis.exp.colfusion.bll.OpenRefineSaveChanges;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen;
import edu.pitt.sis.exp.colfusion.war.rest.OpenRefineRestService;

/**
 * @author xxl
 *
 */
@Path("OpenRefine/")
public class OpenRefineRestServiceImpl  extends BaseController implements OpenRefineRestService {
	final Logger logger = LogManager.getLogger(OpenRefineRestServiceImpl.class.getName());
	
	@Override
	public Response createNewProject(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response createNewProject(final int sid, final String tableName) throws ClassNotFoundException, Exception {

		CreateProject createProject = new CreateProject();
		GeneralResponseGen<String> result = createProject.testCreateProject(sid, tableName);
		
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@Override
	public Response saveChanges(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response saveChanges(final String openRefinProjectId, final String colfusionUserId) throws ClassNotFoundException, Exception {

		OpenRefineSaveChanges openRefineSaveChanges = new OpenRefineSaveChanges();
		GeneralResponseGen<String> result = openRefineSaveChanges.saveChanges(openRefinProjectId, colfusionUserId);
		
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
}
