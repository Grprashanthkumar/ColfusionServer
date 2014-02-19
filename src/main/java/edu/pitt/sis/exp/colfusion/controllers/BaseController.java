/**
 * 
 */
package edu.pitt.sis.exp.colfusion.controllers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 * @author Evgeny
 *
 */
public abstract class BaseController {
	
	protected Response makeCORS(ResponseBuilder req) {
	   ResponseBuilder rb = req.header("Access-Control-Allow-Origin", "*")
	      .header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
	      .header("Access-Control-Allow-Headers", "Content-Type, Accept"); 
	  
	   return rb.build();
	}
}
