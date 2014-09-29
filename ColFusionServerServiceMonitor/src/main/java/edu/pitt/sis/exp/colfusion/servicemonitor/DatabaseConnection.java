package edu.pitt.sis.exp.colfusion.servicemonitor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hao Bai
 * 
 * This class is used for establishing connection with
 * database, and update colfusion database with operations 
 * such as update service table (update service attributes). 
 */
public class DatabaseConnection {

	private Connection conn;
	private Statement state;
	private ResultSet rs;

	public DatabaseConnection(){
	}
	
	/*query if service is existed in database
	 * */
	public boolean queryServieExistance(String serv_name){
		boolean queryResult= false;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/colfusion", "root", "");
        	state= conn.createStatement();
        	
			rs=state.executeQuery("SELECT * FROM colfusion_service WHERE service_name='"+serv_name+"'");
			if(rs.next())
				queryResult= true;

			/*close database connection after query*/
    		state.close();
    		conn.close();
    		return queryResult;
		}
		catch(SQLException sqe){
			System.out.println("in queryServieExistance:sql");
			System.out.println(sqe.toString()+" "+sqe.getErrorCode()+" "+sqe.getSQLState());
			return queryResult;
		}	
		catch(Exception ioe){
			System.out.println("in queryServieExistance:other");
			System.out.println(ioe.toString()+" "+ioe.getMessage()+" "+ioe.getCause());
			return queryResult;
		}	
	}
	
	/*query all services in database
	 * */
	public List<Service> queryAllServies(){
		List<Service> serviceList= new ArrayList<Service>();
		Service serv;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/colfusion", "root", "");
        	state= conn.createStatement();
        	
			rs=state.executeQuery("SELECT * FROM colfusion_service");
			while(rs.next()){
				serv= new Service();
				serv.setServiceID(rs.getInt("service_id"));
				serv.setServiceName(rs.getString("service_name"));
				serv.setServiceAddress(rs.getString("service_address"));
				serv.setPortNumber(rs.getInt("port_number"));
				serv.setServiceDir(rs.getString("service_dir"));
				serv.setServiceCommand(rs.getString("service_command"));
				serv.setServiceStatus(rs.getString("service_status"));
				
				serviceList.add(serv);
				serv= null;
			}

			/*close database connection after query*/
    		state.close();
    		conn.close();
    		return serviceList;
		}
		catch(SQLException sqe){
			System.out.println("in queryAllServies:sql");
			System.out.println(sqe.toString()+" "+sqe.getErrorCode()+" "+sqe.getSQLState());
			return null;
		}	
		catch(Exception ioe){
			System.out.println("in queryAllServies:other");
			System.out.println(ioe.toString()+" "+ioe.getMessage()+" "+ioe.getCause());
			return null;
		}	
	}
	
	/*query service's status
	 * */
	public String queryServiceStatus(String serv_name){
		String queryResult= null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/colfusion", "root", "");
        	state= conn.createStatement();
        	
			rs=state.executeQuery("SELECT * FROM colfusion_service WHERE service_name='"+serv_name+"'");
			if(rs.next()){
				queryResult= rs.getString("service_status");
				//System.out.println(rs.getString("service_name")+" "+rs.getString("service_status"));
			}
		
			/*close database connection after query*/
    		state.close();
    		conn.close();
    		return queryResult;
		}
		catch(SQLException sqe){
			System.out.println("in queryServiceStatus:sql");
			System.out.println(sqe.toString()+" "+sqe.getErrorCode()+" "+sqe.getSQLState());
			return queryResult;
		}	
		catch(Exception ioe){
			System.out.println("in queryServiceStatus:other");
			System.out.println(ioe.toString()+" "+ioe.getMessage()+" "+ioe.getCause());
			return queryResult;
		}	
	}
	
	/*update service's status
	 * */
	public boolean updateServiceStatus(Service service){
		boolean updateResult= false;
		String serv_name= service.getServiceName();
		String serv_status= service.getServiceStatus();
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/colfusion", "root", "");
			
        	state= conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
        			ResultSet.CONCUR_UPDATABLE);
    			
        	state.executeUpdate("UPDATE colfusion_service " +
        						"SET service_status='"+serv_status+"' "+
        						"WHERE service_name='"+serv_name+"'");
        	updateResult= true;
        	/*close database connection after query*/
    		state.close();
    		conn.close();
    		return updateResult; 
		}
		catch(SQLException sqe){
			System.out.println("in updateServiceStatus:sql");
			System.out.println(sqe.toString()+" "+sqe.getErrorCode()+" "+sqe.getSQLState());
			return updateResult; 
		}	
		catch(Exception ioe){
			System.out.println("in updateServiceStatus:other");
			System.out.println(ioe.toString()+" "+ioe.getMessage()+" "+ioe.getCause());
			return updateResult;
		}	
	}
}