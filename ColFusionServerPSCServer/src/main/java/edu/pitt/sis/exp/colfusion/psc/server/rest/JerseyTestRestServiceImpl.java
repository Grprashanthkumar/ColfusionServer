/**
 * 
 */
package edu.pitt.sis.exp.colfusion.psc.server.rest;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author Evgeny
 *
 */
@Path("JerseyTest/")
public class JerseyTestRestServiceImpl implements JerseyTestRestService {

	@Override
	public Response getTestString() {
		String result = "Yes, it works!";
		return Response.status(Status.OK).entity(result).build();
	}

}
