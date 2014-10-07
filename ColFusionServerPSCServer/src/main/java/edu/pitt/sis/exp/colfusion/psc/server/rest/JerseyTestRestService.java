package edu.pitt.sis.exp.colfusion.psc.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface JerseyTestRestService {
	
	@GET
	@Path("getTestString")
	@Produces(MediaType.APPLICATION_JSON)
	Response getTestString();
}
