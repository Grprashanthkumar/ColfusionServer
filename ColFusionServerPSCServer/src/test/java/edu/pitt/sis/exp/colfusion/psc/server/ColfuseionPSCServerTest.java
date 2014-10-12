package edu.pitt.sis.exp.colfusion.psc.server;



import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ColfuseionPSCServerTest {
	@Test
	public void testSendRequest() {
		Thread serverThread1 = startServer("7373");
		Thread serverThread2 = startServer("7472");
		
		
		Client client = Client.create();

		String resourceURL = String.format("http://%s:%s/rest/TableJoin/join/%d/%s/%d/%s/%f", "localhost", "7472", 1, "t1", 2, "t2", 0.5);
		
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
