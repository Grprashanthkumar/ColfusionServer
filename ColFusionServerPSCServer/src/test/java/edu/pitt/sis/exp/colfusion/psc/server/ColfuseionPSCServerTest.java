package edu.pitt.sis.exp.colfusion.psc.server;



import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.pitt.sis.exp.colfusion.psc.server.util.ServerType;
import edu.pitt.sis.exp.colfusion.psc.server.util.Utils;

public class ColfuseionPSCServerTest {
	@Test
	public void testSendRequest() {
		Thread serverThread1 = startServer(Utils.getPort(ServerType.JOINER));
		Thread serverThread2 = startServer(Utils.getPort(ServerType.FETCHER));
		
		
		Client client = Client.create();
		
		String resourceURL = String.format("%s/TableJoin/join/%d/%s/%d/%s/%f", Utils.getBaseRestURL(ServerType.FETCHER), 1746, "CompanyYear", 1745, "CompanyProfit", 0.5);
		
		WebResource webResource = client.resource(resourceURL);
		   
		ClientResponse response = webResource.
		        get(ClientResponse.class);
	
		serverThread1.interrupt();
		serverThread2.interrupt();
	}

	private Thread startServer(final String portNumber) {
		ColfusionPSCServer server = new ColfusionPSCServer(portNumber);
		Thread th = new Thread(server);
	
		th.setName("Server with port " + portNumber);
        
		th.start();
		
		return th;
	}
}