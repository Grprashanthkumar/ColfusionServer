package edu.pitt.sis.exp.colfusion.visualization;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Canvas {
	  final Logger logger = LogManager.getLogger(Canvas.class.getName());
	
		public String vid = "";
		public String canvasName = "";
		public String owner = "";
		public int authorization, privilege = 0;
		public Date cdate = new Date();
		
		public Canvas(String vid, String canvasName, String owner, Date cdate, int privilege, int authorization){
			this.vid = vid;
			this.canvasName = canvasName;
			this.owner = owner;
			this.cdate = cdate;
			this.privilege = privilege;
			this.authorization = authorization;
		}
		

		public Canvas createNewCanvas(String vid, String canvasName, String owner, Date cdate, int privilege, int authorization){
			Canvas result = new Canvas(vid, canvasName, owner, cdate, privilege, authorization);
			return result;
			
		}
		
		/*public Canvas openCanvas(){
			Canvas result = new Canvas();
			return result;
		}
		
		public void deleteCanvas(){
			
		}
		
		public Canvas searchCanvas(){
			Canvas result = new Canvas();
			return result;
		}*/
}

