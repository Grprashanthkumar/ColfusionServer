package edu.pitt.sis.exp.colfusion.bll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGenImpl;

public class OpenRefineSaveChanges {
final Logger logger = LogManager.getLogger(OpenRefineSaveChanges.class.getName());
	
	

	/**
	 * 
	 * @param sid
	 * @param tableName
	 * @return
	 * @throws SQLException 
	 */
	public GeneralResponseGen<String> saveChanges(String projectId) throws SQLException {
		
		GeneralResponseGen<String> result = new GeneralResponseGenImpl<>();
		
		result.setSuccessful(false);
		
		int sid = getSid(projectId);
		String tableName = getTableName(projectId);
		
		String msg = "No change needs to be saved!";
		
		if(tempTbExist(sid, tableName)) {
			rmTb(sid, tableName);
			createTb(sid, tableName);
			rmTb(sid, "temp_" + tableName);
			msg = "Changes have been saved!";
		}
		
		
		result.setMessage("OK");
		result.setPayload(msg);
		result.setSuccessful(true);
			
		
		return result;
	}
	
	public boolean tempTbExist(int sid, String tableName) throws SQLException {
		String driver = "com.mysql.jdbc.Driver";
	    String dbURL = "jdbc:mysql://127.0.0.1:3306/colfusion_filetodb_" + sid; // Connect
	                                                                                // to
	                                                                                // Server
	                                                                                // &
	                                                                                // DB连接服务器和数据库test
	    String userName = "root"; // UserName 用户名
	    String userPwd = ""; // Pwd 密码
	    
	    try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Connection conn = DriverManager.getConnection(dbURL, userName, userPwd);
	    
	    String query = "SHOW TABLES LIKE 'temp_" + tableName + "'";
    	Statement ps = conn.createStatement();
    	ResultSet result = ps.executeQuery(query);
    	if(result.next())
    		return true;
    	else {
			return false;
		}
	}

	
	public void createTb(int sid, String tableName) throws SQLException {
		String driver = "com.mysql.jdbc.Driver";
	    String dbURL = "jdbc:mysql://127.0.0.1:3306/colfusion_filetodb_" + sid; // Connect
	                                                                                // to
	                                                                                // Server
	                                                                                // &
	                                                                                // DB连接服务器和数据库test
	    String userName = "root"; // UserName 用户名
	    String userPwd = ""; // Pwd 密码
	    
	    try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Connection conn = DriverManager.getConnection(dbURL, userName, userPwd);
	    
	    String query = "CREATE TABLE " + tableName + " SELECT * FROM temp_" + tableName;
    	Statement ps = conn.createStatement();
    	ps.executeUpdate(query);
	}
	
	public void rmTb(int sid, String tableName) throws SQLException {
		String driver = "com.mysql.jdbc.Driver";
	    String dbURL = "jdbc:mysql://127.0.0.1:3306/colfusion_filetodb_" + sid; // Connect
	                                                                                // to
	                                                                                // Server
	                                                                                // &
	                                                                                // DB连接服务器和数据库test
	    String userName = "root"; // UserName 用户名
	    String userPwd = ""; // Pwd 密码
	    
	    try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Connection conn = DriverManager.getConnection(dbURL, userName, userPwd);
	    
	    String query = "DROP TABLE " + tableName;
    	Statement ps = conn.createStatement();
    	ps.execute(query);
	}
	
	public int getSid(String projectId) throws SQLException {
		String driver = "com.mysql.jdbc.Driver";
		String dbURL = "jdbc:mysql://127.0.0.1:3306/colfusion";
		String userName = "root"; // UserName 用户名
		String userPwd = ""; // Pwd 密码

		Connection conn = null;
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn = DriverManager.getConnection(dbURL, userName, userPwd);

		String selectQuery = "select sid from colfusion_openrefine_project_map where projectId = '" + projectId + "'";
		Statement ps = conn.createStatement();
		ResultSet rs = ps.executeQuery(selectQuery);

		String sid = "";
		while (rs.next()) {
			sid = rs.getString(1);
		}
		return Integer.valueOf(sid);
	}
	
	public String getTableName(String projectId) throws SQLException {
		String driver = "com.mysql.jdbc.Driver";
		String dbURL = "jdbc:mysql://127.0.0.1:3306/colfusion";
		String userName = "root"; // UserName 用户名
		String userPwd = ""; // Pwd 密码

		Connection conn = null;
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn = DriverManager.getConnection(dbURL, userName, userPwd);

		String selectQuery = "select tableName from colfusion_openrefine_project_map where projectId = '" + projectId + "'";
		Statement ps = conn.createStatement();
		ResultSet rs = ps.executeQuery(selectQuery);

		String tableName = "";
		while (rs.next()) {
			tableName = rs.getString(1);
		}
		return tableName;
	}
}
