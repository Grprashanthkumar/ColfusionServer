package edu.pitt.sis.exp.colfusion.controllers;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import edu.pitt.sis.exp.colfusion.models.GeneralResponseModel;

@Path("Wizard/")
public class WizardController {
	/**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return JSON serialization of the model.
     */ 
	@Path("test")
    @GET
    @Produces("application/json")
    public GeneralResponseModel getProvenance() {
    	
    	GeneralResponseModel grm = new GeneralResponseModel(); 
    	
    	grm.Status = "StatBLBL";
    	grm.Message = "MsgBLBL";
    	
        return grm;
    }
    
	/**
     * Processes the form submitted from wizard step where data file is uploaded
     * 
     * @param sid
     * @return
     */
	@Path("acceptFileFromWizard")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
 //   @Produces("application/json")
    public String acceptFileFromWizard(@FormDataParam("sid") String sid, @FormDataParam("uploadTimestamp") String uploadTimestamp, 
    		@FormDataParam("fileType") String fileType,
    		@FormDataParam("dbType") String dbType, 
    		@FormDataParam("upload_file") InputStream file,
    	    @FormDataParam("upload_file") FormDataContentDisposition fileDisposition ) {
    	
    	GeneralResponseModel grm = new GeneralResponseModel(); 
    	
    	grm.Status = "StatBLBL";
    	grm.Message = "MsgBLBL";
    	
    	
    	
        return "haha";
    }
}






