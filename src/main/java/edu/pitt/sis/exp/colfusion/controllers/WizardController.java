package edu.pitt.sis.exp.colfusion.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import edu.pitt.sis.exp.colfusion.models.GeneralResponseModel;

@Path("Wizard/")
public class WizardController {
	/**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return JSON serialization of the model.
     */ 
    @GET
    @Produces("application/json")
    public GeneralResponseModel getProvenance(@PathParam("sid") String sid) {
    	
    	GeneralResponseModel grm = new GeneralResponseModel(); 
    	
    	grm.Status = "StatBLBL";
    	grm.Message = "MsgBLBL";
    	
        return grm;
    }
}






