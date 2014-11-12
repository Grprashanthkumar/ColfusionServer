package edu.pitt.sis.exp.colfusion.servicemonitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * @author Hao Bai
 * 
 * Starts Jetty server on provided port (or default).
 * REST API is available on 0.0.0.0:PORT/rest/*
 */
public class ServiceMonitorServer implements Runnable {


	private static final int DEFAULT_PORT = 7473;
	
	private static final Logger logger = LogManager.getLogger(ServiceMonitorServer.class.getName());
	
	private final String[] args;
	
	public ServiceMonitorServer(final String... args) {
		this.args = args;
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
			catch (NumberFormatException ex) {
				String message = String.format("Unable to parse port number from '%s'. %s", args[0], ex.getMessage());
				logger.info(message);
				message = String.format("Going to use default port number '%d'.", DEFAULT_PORT);
				logger.info(message);
			}
		}
		else {
			String message = String.format("No port number was specified. Going to use default port number '%d'.", DEFAULT_PORT);
			logger.info(message);
		}
		
		String message = String.format("Port number: %d", port);
		logger.info(message);
		return port;
	}

	@Override
	public void run() {
		logger.info("Starting Jetty Server for ServiceMonitor");
		
		ServletHolder servletHolder = new ServletHolder(ServletContainer.class);    

		servletHolder.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "edu.pitt.sis.exp.colfusion.servicemonitor.rest");
        
		int port = getPortNumber();
		Server server = new Server(port);
        
        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        context.addServlet(servletHolder, "/rest/*");
        
        server.setStopAtShutdown(true);
        
        try {
        	 server.start();
        	 
        	 Runtime.getRuntime().addShutdownHook(
                     new Thread(new ShutdownSignalHandler(server))      
             );
        	 
        	 configure();
        }
        catch (Exception ex) {
        	logger.error("Failed to start jetty server", ex);
        	return;
        }
         
        try {
			server.join();
		}
        catch (InterruptedException ex) {
			ex.printStackTrace();
		}
        finally {
        	try {
				//server.stop();
			} 
        	catch (Exception ex) {
				ex.printStackTrace();
			}
        }
        
        //logger.info("Jetty Server is shutdown");		
	}

	private void configure() {
		
	}
}

class ShutdownSignalHandler implements Runnable {
    
    private final Server _server;

    public ShutdownSignalHandler(final Server server) {
        this._server = server;
    }

    @Override
    public void run() {
       
        try {
            _server.stop();
        } 
        catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
