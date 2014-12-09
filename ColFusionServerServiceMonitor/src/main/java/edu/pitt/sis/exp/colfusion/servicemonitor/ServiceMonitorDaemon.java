package edu.pitt.sis.exp.colfusion.servicemonitor;


public class ServiceMonitorDaemon {

	private static ServiceMonitorDaemon serviceMonitorDaemon;
	private static ServiceMonitor serviceMonitor;
	private static ServiceMonitorServer server;
	  
	/** The usage string. */
	private static final String USAGE_STRING = "The valid input should be: ServiceMonitorDeamon.java start/stop";

	/**
	 * main function
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if(args.length <= 0) {
			//	System.out.println(USAGE_STRING);
			//	System.exit(1);
		}
			
		//if(args[1].equals("start")) {
			serviceMonitorDaemon = new ServiceMonitorDaemon();
			serviceMonitorDaemon.start();
			
		//}
		//else if(args[1].equals("stop")) {
		//	serviceMonitor.stopServiceMonitor();
		//	System.out.println("Services Monitoring process stopped!");
		//}
	    
	}  

	public ServiceMonitorDaemon() {
		serviceMonitor = new ServiceMonitor();
		server = new ServiceMonitorServer();
	}

	
	/**
	 * get entity of Service Monitor
	 * 
	 * @return entity of ServiceMonitor
	 */
	public static ServiceMonitor getServiceMonitor() {
		return serviceMonitor;
	}

	/**
	 * set entity of Service Monitor
	 * 
	 * @param entity of serviceMonitor
	 */
	public static void setServiceMonitor(ServiceMonitor serviceMonitor) {
		ServiceMonitorDaemon.serviceMonitor = serviceMonitor;
	}

	/**
	 * get entity of ServiceMonitorServer
	 * 
	 * @return entity of ServiceMonitorServer
	 */
	public static ServiceMonitorServer getServer() {
		return server;
	}

	/**
	 * set entity of ServiceMonitorServer
	 * 
	 * @param entity of ServiceMonitorServer
	 */
	public static void setServer(ServiceMonitorServer server) {
		ServiceMonitorDaemon.server = server;
	}

	/**
	 * Start the Daemon. Namely, start the monitor and the server.
	 * 
	 * @throws Exception if there is an issue starting either server.
	 */
	public void start() throws Exception {
		serviceMonitor.startServiceMonitor();
		server.run();
	}

	/**
	 * Stop the Daemon. Namely, stop the monitor and the server.
	 * 
	 * @throws Exception if there is an issue stopping either server.
	 */
	public void stop() throws Exception {
		serviceMonitor.stopServiceMonitor();
	    //server.stop();
	}
	
	/**
	 * return entity of ServiceMonitorDaemon
	 * 
	 * @return entity of ServiceMonitorDaemon
	 */
	public static ServiceMonitorDaemon getInstance() {
		return serviceMonitorDaemon;
	}
}
