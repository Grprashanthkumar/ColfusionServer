package edu.pitt.sis.exp.colfusion.servicemonitor;


import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

//import com.sun.jersey.*;
//import com.sun.jersey.spi.container.servlet.ServletContainer;
//import com.sun.jersey.api.core.ResourceConfig;
//import com.sun.jersey.api.core.PackagesResourceConfig;
//import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;

/**
 * @author Hao Bai
 * 
 * Starts Jetty server on provided port (or default).
 * REST API is available on 0.0.0.0:PORT/rest/*
 */
@Provider
public class ServiceMonitorServer implements Runnable, ContextResolver<ServiceMonitor> {


	private static final int DEFAULT_PORT = 7473;
	
	private static final Logger logger = LogManager.getLogger(ServiceMonitorServer.class.getName());
	
	//private final String[] args;
	private static ServiceMonitor serviceMonitor;
	private int port;
	
	public ServiceMonitorServer(ServiceMonitor argMonitor, int port) {
		serviceMonitor = argMonitor;
		this.port = port;
	}

	public ServiceMonitorServer() {
		serviceMonitor = new ServiceMonitor();
	}
	
	@Override
	public ServiceMonitor getContext(Class<?> type) {
		return serviceMonitor;
	}

	/**
	 * @param args
	 * @return
	 */
	private int getPortNumber() {
		int port = DEFAULT_PORT;
		
		if (this.port > 0) {
			try {
				port = this.port;
			}
			catch (NumberFormatException ex) {
				String message = String.format("Unable to parse port number from '%d'. %s", this.port, ex.getMessage());
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
		
			   
	    
		//ResourceConfig resourceConfig = new PackagesResourceConfig("edu.pitt.sis.exp.colfusion.servicemonitor.rest");
		//resourceConfig.getSingletons().add(new SingletonTypeInjectableProvider<javax.ws.rs.core.Context, ServiceMonitor>(ServiceMonitor.class, new ServiceMonitor()){});
		//ServletHolder servletHolder = new ServletHolder(new ServletContainer(resourceConfig));  
		
		ServletHolder servletHolder = new ServletHolder(ServletContainer.class);  

		servletHolder.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "edu.pitt.sis.exp.colfusion.servicemonitor.rest");
        
		int port = getPortNumber();
		Server server = new Server(port);
        
		//***************
		
		
	    //root.addServlet(new ServletHolder(new ServletContainer(rc)), "/");
	    
		//***************
		
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
