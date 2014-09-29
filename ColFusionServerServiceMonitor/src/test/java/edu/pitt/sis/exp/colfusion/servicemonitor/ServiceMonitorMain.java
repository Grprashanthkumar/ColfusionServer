package edu.pitt.sis.exp.colfusion.servicemonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Hao Bai
 * 
 * This is the main class used for test.
 */
public class ServiceMonitorMain extends TimerTask{

	private static ServiceMonitorController sermoncon;
	private static int roundCount;
	private static Timer timer;
	
	public void run(){
		List<Service> servlist= new ArrayList<Service>();
		try{	
			servlist= sermoncon.getServicesStatus();
			if(servlist!= null){
				for(int i=0; i<servlist.size() ; i++)
					System.out.println(servlist.get(i).getServiceName()+": "+servlist.get(i).getServiceStatus());
				roundCount++;
				/*roundCount is used for controlling stop process*/
				if(roundCount==20){
					sermoncon.stopServiceMonitor();
					System.out.println("Services Monitoring process stopped!");
					timer.cancel();
				}
			}
		}
		catch(Exception excep){
			System.out.println(excep.toString()+" "+ excep.getMessage()+" "+ excep.getCause());
		}
	}
	
	public static void main(String[] args) {	
		sermoncon= new ServiceMonitorController();
		roundCount= 0;
		
		sermoncon.startServiceMonitor();
		System.out.println("Services Monitoring process started!");
		
		timer= new Timer();
		try{
			timer.schedule(new ServiceMonitorMain(), 0, 5000);
		}
		catch(Exception excep){
			System.out.println(excep.toString()+" "+ excep.getMessage()+" "+ excep.getCause());
		}
	}
}
