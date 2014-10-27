package edu.pitt.sis.exp.colfusion.servicemonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.managers.ServiceManager;
import edu.pitt.sis.exp.colfusion.dal.managers.ServiceManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.UserManager;
import edu.pitt.sis.exp.colfusion.dal.managers.UserManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionServices;

/**
 * @author Hao Bai
 * 
 * This is the main class used for test.
 */
public class ServiceMonitorMain extends TimerTask{

	private static ServiceMonitorController serviceMonitorController;
	private static int roundCount;
	private static Timer timer;
	private static Logger logger = LogManager.getLogger(ServiceMonitorMain.class.getName());
	
	public void run(){
		List<ColfusionServices> serviceList = new ArrayList<ColfusionServices>();
		try{	
			serviceList = serviceMonitorController.getServicesStatus();
			if(serviceList != null){
				for(int i = 0; i < serviceList.size() ; i++)
					System.out.println(serviceList.get(i).getServiceName()+": "+serviceList.get(i).getServiceStatus());
				roundCount++;
				/*roundCount is used for controlling stop process*/
				if(roundCount == 100){
					serviceMonitorController.stopServiceMonitor();
					System.out.println("Services Monitoring process stopped!");
					timer.cancel();
				}
			}
		}
		/*Logger is not used here, since this is a Test class.*/
		catch(Exception exception){
			logger.error("In ServiceMonitorMain.run()\n"
					+exception.toString()+" "+exception.getMessage()+" "+exception.getCause());
		}
	}
	
	public static void main(String[] args) throws Exception {	
		
/*		ServiceManager servMan = new ServiceManagerImpl();
		
		servMan.queryServieExistance("Apache");
		
		for(ColfusionServices serv : servMan.findAll()){
			System.out.println(serv.getServiceName());
			if(serv.getServiceName().equals("Neo4j")){
				serv.setServiceStatus("stopped");
				servMan.updateServiceStatus(serv);
			}
		}
		
		System.out.println(servMan.queryServiceStatus("MySQL"));
		
		UserManager userMan = new UserManagerImpl();
		for(String email : userMan.queryUserEmails("admin")){
			System.out.println(email);
		}   */

			
		serviceMonitorController = new ServiceMonitorController();
		roundCount = 0;
		
		serviceMonitorController.startServiceMonitor();
		System.out.println("Services Monitoring process started!");
		
		timer = new Timer();
		try{
			timer.schedule(new ServiceMonitorMain(), 0, 5000);
		}
		//Logger is not used here, since this is a Test class.
		catch(Exception exception){
			logger.error("In ServiceMonitorMain.main()\n"
					+exception.toString()+" "+exception.getMessage()+" "+exception.getCause());
		} 
	}
}
