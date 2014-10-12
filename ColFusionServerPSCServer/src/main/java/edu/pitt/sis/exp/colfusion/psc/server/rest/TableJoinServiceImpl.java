/**
 * 
 */
package edu.pitt.sis.exp.colfusion.psc.server.rest;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.TwoJointTablesViewModel;
import edu.pitt.sis.exp.colfusion.psc.server.ColfusionPSCServer;

/**
 * @author Evgeny
 *
 */
@Path("TableJoin/")
public class TableJoinServiceImpl implements TableJoinService {

	private static final Logger logger = LogManager.getLogger(ColfusionPSCServer.class.getName());
	
	@Override
	public Response joinTables(final TwoJointTablesViewModel twoJointTables) {
		
		logger.info("Got request");
		
		return Response.status(200).entity("test2").build();
	}

	@Override
	public Response joinTables(final int sid1, final String tableName1, final int sid2,
			final String tableName2, final double similarityThreshold) {
		
		logger.info(String.format("Got: %d %s %d %s %f", sid1, tableName1, sid2, tableName2, similarityThreshold));
		
		TwoJointTablesViewModel model = new TwoJointTablesViewModel(sid1, tableName1, sid2, tableName2, similarityThreshold, null, null, null);
		
		Client client = Client.create();

		String resourceURL = String.format("http://%s:%s/rest/TableJoin/join", "localhost", "7373");
		
		WebResource webResource = client.resource(resourceURL);
		   
		ClientResponse response = webResource.
				type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, model);
		
//		Client client = Client.create();
//		
//		String resourceURL = String.format("http://%s:%s/rest/TableJoin/join", ColfusionPSCServer.properties.getProperty("colfusion.tablejoiner.host"),
//				ColfusionPSCServer.properties.getProperty("colfusion.tablejoiner.port"));
//		
//		WebResource webResource = client.resource(resourceURL);
//		
//		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, )
//		
		return Response.status(200).entity("test").build();
	}

}
