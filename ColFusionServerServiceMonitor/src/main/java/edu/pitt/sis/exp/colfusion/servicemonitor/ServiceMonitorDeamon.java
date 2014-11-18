package edu.pitt.sis.exp.colfusion.servicemonitor;


public class ServiceMonitorDeamon {

	private static ServiceMonitor serviceMonitor;
	private ServiceMonitorServer server;
	  
	/** The usage string. */
	private static final String USAGE_STRING = "The valid input should be: ServiceMonitorDeamon.java start/stop";

	public static void main(String[] args) throws Exception {
		if(args.length <= 0) {
			//	System.out.println(USAGE_STRING);
			//	System.exit(1);
		}
			
		//if(args[1].equals("start")) {
			final ServiceMonitorDeamon serviceMonitorDeamon = new ServiceMonitorDeamon();
			serviceMonitorDeamon.start();
			
		//}
		//else if(args[1].equals("stop")) {
		//	serviceMonitor.stopServiceMonitor();
		//	System.out.println("Services Monitoring process stopped!");
		//}
	    
	}  

	public ServiceMonitorDeamon() {
		serviceMonitor = new ServiceMonitor();
		server = new ServiceMonitorServer();
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
	
	public static ServiceMonitor getServiceMonitorInstance() {
		return serviceMonitor;
	}
}
