package edu.pitt.sis.exp.colfusion.servicemonitor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Hao Bai
 * 
 * This class is used for establishing connection with
 * database, and update colfusion database with operations 
 * such as update service table (update service attributes). 
 */
public class DatabaseConnection {

	private Connection connection;
	private Statement state;
	private ResultSet resultSet;
	
	private Logger logger = LogManager.getLogger(DatabaseConnection.class.getName());

	public DatabaseConnection(){
	}
	
	/*query if service is existed in database
	 * */
	public boolean queryServieExistance(String serviceName){
		boolean queryResult = false;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/colfusion", "root", "");
        	state = connection.createStatement();
        	
        	resultSet = state.executeQuery("SELECT * FROM colfusion_service WHERE service_name='"+serviceName+"'");
			if(resultSet.next())
				queryResult = true;

			/*close database connection after query*/
    		state.close();
    		connection.close();
    		return queryResult;
		}
		catch(SQLException sqlException){
			logger.error("In queryServieExistance:sql\n"
						+ sqlException.toString() + " " + sqlException.getErrorCode() + " " + sqlException.getSQLState());
			return queryResult;
		}	
		catch(Exception exception){
			logger.error("In queryServieExistance:other\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
			return queryResult;
		}	
	}
	
	/*query all services in database
	 * */
	public List<Service> queryAllServies(){
		List<Service> serviceList = new ArrayList<Service>();
		Service service;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/colfusion", "root", "");
        	state = connection.createStatement();
        	
        	resultSet = state.executeQuery("SELECT * FROM colfusion_service");
			while(resultSet.next()){
				service = new Service();
				service.setServiceID(resultSet.getInt("service_id"));
				service.setServiceName(resultSet.getString("service_name"));
				service.setServiceAddress(resultSet.getString("service_address"));
				service.setPortNumber(resultSet.getInt("port_number"));
				service.setServiceDir(resultSet.getString("service_dir"));
				service.setServiceCommand(resultSet.getString("service_command"));
				service.setServiceStatus(resultSet.getString("service_status"));
				service.setServicePreviousStatus(resultSet.getString("service_status"));
				
				serviceList.add(service);
				service = null;
			}

			/*close database connection after query*/
    		state.close();
    		connection.close();
    		return serviceList;
		}
		catch(SQLException sqlException){
			logger.error("In queryAllServies:sql\n"
					+ sqlException.toString() + " " + sqlException.getErrorCode() + " " + sqlException.getSQLState());
			return null;
		}	
		catch(Exception exception){
			logger.error("In queryAllServies:other\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
			return null;
		}	
	}
	
	/*query service's status
	 * */
	public String queryServiceStatus(String serviceName){
		String queryResult = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/colfusion", "root", "");
        	state = connection.createStatement();
        	
        	resultSet = state.executeQuery("SELECT * FROM colfusion_service WHERE service_name='"+serviceName+"'");
			if(resultSet.next()){
				queryResult = resultSet.getString("service_status");
				//System.out.println(rs.getString("service_name")+" "+rs.getString("service_status"));
			}
		
			/*close database connection after query*/
    		state.close();
    		connection.close();
    		return queryResult;
		}
		catch(SQLException sqlException){
			logger.error("In queryServiceStatus:sql\n"
					+ sqlException.toString() + " " + sqlException.getErrorCode() + " " + sqlException.getSQLState());
			return queryResult;
		}	
		catch(Exception exception){
			logger.error("In queryServiceStatus:other\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
			return queryResult;
		}	
	}
	
	/*update service's status
	 * */
	public boolean updateServiceStatus(Service service){
		boolean updateResult = false;
		String serviceName = service.getServiceName();
		String serviceStatus = service.getServiceStatus();
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/colfusion", "root", "");
			
        	state = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
        			ResultSet.CONCUR_UPDATABLE);
    			
        	state.executeUpdate("UPDATE colfusion_service " +
        						"SET service_status='"+serviceStatus+"' "+
        						"WHERE service_name='"+serviceName+"'");
        	updateResult = true;
        	/*close database connection after query*/
    		state.close();
    		connection.close();
    		return updateResult; 
		}
		catch(SQLException sqlException){
			logger.error("In updateServiceStatus:sql\n"
					+ sqlException.toString() + " " + sqlException.getErrorCode() + " " + sqlException.getSQLState());
			return updateResult; 
		}	
		catch(Exception exception){
			logger.error("In updateServiceStatus:other\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
			return updateResult;
		}	
	}

	/*query users' email address
	 * */
	public List<String> queryUserEmails(String userLevel){
		List<String> queryResult = new ArrayList<String>();
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/colfusion", "root", "");
        	state = connection.createStatement();
        	
        	resultSet = state.executeQuery("SELECT * FROM colfusion_users "+
        	                               "WHERE user_level='"+userLevel+"'");
			while(resultSet.next()){
				queryResult.add(resultSet.getString("user_email"));
				//System.out.println(resultSet.getString("user_email"));
			}
		
			/*close database connection after query*/
    		state.close();
    		connection.close();
    		return queryResult;
		}
		catch(SQLException sqlException){
			logger.error("In queryUserEmails:sql\n"
					+ sqlException.toString() + " " + sqlException.getErrorCode() + " " + sqlException.getSQLState());
			return queryResult;
		}	
		catch(Exception exception){
			logger.error("In queryUserEmails:other\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
			return queryResult;
		}	
	}
}