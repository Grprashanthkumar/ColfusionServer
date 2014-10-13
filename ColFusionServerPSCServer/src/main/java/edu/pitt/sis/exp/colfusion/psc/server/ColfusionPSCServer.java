package edu.pitt.sis.exp.colfusion.psc.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Starts Jetty server on provided port (or default).
 * REST API is available on 0.0.0.0:PORT/rest/*
 * 
 * @author Evgeny
 *
 */
public class ColfusionPSCServer implements Runnable{
	
	// nice blog: http://jlunaquiroga.blogspot.com/2014/01/restful-web-services-with-jetty-and.html
	
	private static final int DEFAULT_PORT = 7473;
	
	private static final Logger logger = LogManager.getLogger(ColfusionPSCServer.class.getName());
	
	private final String[] args;
	
	public ColfusionPSCServer(final String... args) {
		this.args = args;
	}

	public static void main(final String[] args) throws Exception
    {
		ColfusionPSCServer server = new ColfusionPSCServer(args);
		server.run();
    }

	/**
	 * @param args
	 * @return
	 */
	private int getPortNumber() {
		int port = DEFAULT_PORT;
		
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			}
			catch (NumberFormatException e) {
				String message = String.format("Unable to parse port number from '%s'. %s", args[0], e.getMessage());
				logger.info(message);
				message = String.format("Going to use default port number '%d'.", DEFAULT_PORT);
				logger.info(message);
			}
		}
		else {
			String message = String.format("No port number was not specified. Going to use default port number '%d'.", DEFAULT_PORT);
			logger.info(message);
		}
		
		String message = String.format("Port number: %d", port);
		logger.info(message);
		return port;
	}

	@Override
	public void run() {
		logger.info("Starting Jetty Server");
		
		ServletHolder sh = new ServletHolder(ServletContainer.class);    

		sh.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "edu.pitt.sis.exp.colfusion.psc.server.rest");
        
		int port = getPortNumber();
		Server server = new Server(port);
        
        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        context.addServlet(sh, "/rest/*");
        
        try {
        	 server.start();
             server.join();
        }
        catch (Exception e) {
        	logger.error("Failed to start jetty server", e);
        }
         
        logger.info("Jetty Server is shutdown");		
	}
}
