package edu.pitt.sis.exp.colfusion.bll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OpenRefineDbConn {
	
    

    
    public static String getTableData() throws SQLException {
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
    	String query = "select sid from colfusion_dnameinfo where cid = 2360";
    	Statement ps = conn.createStatement();
    	ResultSet rs = ps.executeQuery(query);
    	String result = "";
    	while(rs.next()) {
    		result += rs.getString("sid");
    	}
    	return result;
    }
}
