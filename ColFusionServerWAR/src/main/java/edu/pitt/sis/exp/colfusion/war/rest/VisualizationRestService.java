package edu.pitt.sis.exp.colfusion.war.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.RelationshipBL;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponse;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGenImpl;
import edu.pitt.sis.exp.colfusion.responseModels.RelationshipLinksResponse;


@Path("visualization/")
public class VisualizationRestService {
	
	final Logger logger = LogManager.getLogger(StoryRestService.class.getName());
	

}
