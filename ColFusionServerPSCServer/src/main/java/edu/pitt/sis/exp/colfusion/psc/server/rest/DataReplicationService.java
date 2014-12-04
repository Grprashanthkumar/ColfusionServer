package edu.pitt.sis.exp.colfusion.psc.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.pitt.sis.exp.colfusion.psc.server.business.DataReplicationBL;

@Path("DataReplication/")
public class DataReplicationService {
	
	@Path("triggerDataReplication")
	@GET
    @Produces(MediaType.TEXT_PLAIN)
	public Response triggerDataReplication() {
		DataReplicationBL dataReplicationBL = new DataReplicationBL();
		
		try {
			int count = dataReplicationBL.doDataReplication();
			return Response.ok(count).build();
		} catch (Exception e) {
			return Response.serverError().build();
		}
	}
}
