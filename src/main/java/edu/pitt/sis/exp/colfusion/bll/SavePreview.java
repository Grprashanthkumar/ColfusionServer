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

public class SavePreview {
final Logger logger = LogManager.getLogger(OpenRefineBL.class.getName());
	
	

	/**
	 * 
	 * @param sid
	 * @param tableName
	 * @return
	 * @throws SQLException 
	 */
	public GeneralResponseGen<String> savePreview(int sid, String tableName) throws SQLException {
		
		GeneralResponseGen<String> result = new GeneralResponseGenImpl<>();
		
		result.setSuccessful(false);
		
		String msg = "";
		
		if(tempTbExist(sid, tableName)) {
			rmTb(sid, "temp_" + tableName);
			msg = "Change has been saved!";
		} else {
			msg = "No change needs to be saved!";
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
}
