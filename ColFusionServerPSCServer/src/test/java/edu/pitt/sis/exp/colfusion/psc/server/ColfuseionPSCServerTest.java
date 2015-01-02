package edu.pitt.sis.exp.colfusion.psc.server;



import javax.ws.rs.core.Response;

import org.junit.Ignore;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.psc.server.util.ServerType;
import edu.pitt.sis.exp.colfusion.psc.server.util.Utils;

public class ColfuseionPSCServerTest {
	
	@Ignore
	@Test
	public void testSendRequest() {
		Thread serverThread1 = startServer(Utils.getPort(ServerType.JOINER));
		Thread serverThread2 = startServer(Utils.getPort(ServerType.FETCHER));
		
		String resourceURL = String.format("%s/TableJoin/join/%d/%s/%d/%s/%f", Utils.getBaseRestURL(ServerType.FETCHER), 1746, "CompanyYear", 1745, "CompanyProfit", 0.5);
		
		Response response = Utils.doGet(resourceURL);
	
		serverThread1.interrupt();
		serverThread2.interrupt();
	}
	
	@Ignore
	@Test
	public void testIsAlive() {
		Thread serverThread1 = startServer(Utils.getPort(ServerType.JOINER));
		Thread serverThread2 = startServer(Utils.getPort(ServerType.FETCHER));
		
		String resourceURL = String.format("%s/TableJoin/isAlive", Utils.getBaseRestURL(ServerType.FETCHER));
		
		Response response = Utils.doGet(resourceURL);
	
		System.out.println(response.toString());
		
		resourceURL = String.format("%s/TableJoin/isAlive", Utils.getBaseRestURL(ServerType.JOINER));
		
		response = Utils.doGet(resourceURL);
	
		System.out.println(response.toString());
		
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