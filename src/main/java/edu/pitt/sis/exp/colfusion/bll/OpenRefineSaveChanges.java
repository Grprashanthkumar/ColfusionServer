package edu.pitt.sis.exp.colfusion.bll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	public GeneralResponseGen<String> saveChanges(final String projectId, final String colfusionUserId) throws SQLException {
		
		GeneralResponseGen<String> result = new GeneralResponseGenImpl<>();
		
		result.setSuccessful(false);
		
		int sid = getSid(projectId);
		String tableName = getTableName(projectId);
		
		String msg = "No change needs to be saved!";
		if(!isTimeOutForCurrentUser(sid, tableName, Integer.valueOf(colfusionUserId))) {
			if(tempTbExist(sid, tableName)) {
				rmTb(sid, tableName);
				createTb(sid, tableName);
				rmTb(sid, "temp_" + tableName);
				msg = "Changes have been saved!";
			}
		} else {
			msg = "Time is out, cannot save!";
		}
		
		result.setMessage("OK");
//		result.setPayload(msg + colfusionUserId);
		result.setPayload(msg);
		result.setSuccessful(true);
			
		
		return result;
	}
	
	public boolean isTimeOutForCurrentUser(final int sid, final String tableName, final int colfusionUserId) throws SQLException {
		String driver = "com.mysql.jdbc.Driver";
	    String dbURL = "jdbc:mysql://127.0.0.1:3306/colfusion"; // Connect
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
	    
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String currentTime = format.format(new Date());// new Date()为获取当前系统时间
	    
	    String query = "SELECT startChangeTime FROM colfusion_table_change_log WHERE endChangeTime is NULL and sid = " + sid + " AND tableName = '" + tableName + "' AND operatedUser = " + colfusionUserId;
    	Statement ps = conn.createStatement();
    	ResultSet rs = ps.executeQuery(query);
    	
    	String startTime = "";
    	
        while (rs.next()) {
            startTime = rs.getString("startChangeTime");
            return isTimeOutHelper(currentTime, startTime);
        }
        return true;
	}
	
	public boolean isTimeOutHelper(final String currentTime, String startTime) {
        if (!currentTime.split(" ")[0].equals(startTime.split(" ")[0])) {
            return true;
        }
        startTime = startTime.substring(0, currentTime.length() - 1);
        String[] arr1 = currentTime.split(" ")[1].split(":");
        String[] arr2 = startTime.split(" ")[1].split(":");

        int[] intArr1 = new int[arr1.length];
        int[] intArr2 = new int[arr2.length];

        for (int i = 0; i < arr1.length; i++) {
            intArr1[i] = Integer.valueOf(arr1[i]);
        }
        for (int j = 0; j < arr2.length; j++) {
            intArr2[j] = Integer.valueOf(arr2[j]);
        }

        if (intArr1[0] - intArr2[0] > 1) {
            return true;
        } else if (intArr1[0] - intArr2[0] == 1) {
            if (intArr1[1] + 60 - intArr2[1] > 30) {
                return true;
            } else {
                return false;
            }
        } else if (intArr1[0] - intArr2[0] == 0) {
            if (intArr1[1] - intArr2[1] > 30) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
	
	public void releaseTableLock(final int sid, final String tableName) throws SQLException {
		String driver = "com.mysql.jdbc.Driver";
	    String dbURL = "jdbc:mysql://127.0.0.1:3306/colfusion"; // Connect
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
	    
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String endEditTime = format.format(new Date());// new Date()为获取当前系统时间
	    
	    String query = "UPDATE colfusion_table_change_log SET endChangeTime = '" + endEditTime + "' WHERE endChangeTime IS NULL AND sid = " + sid + " AND tableName = '" + tableName + "'";
    	Statement ps = conn.createStatement();
    	ps.executeUpdate(query);
	}
	
	public boolean tempTbExist(final int sid, final String tableName) throws SQLException {
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
    	if(result.next()) {
			return true;
		} else {
			return false;
		}
	}

	
	public void createTb(final int sid, final String tableName) throws SQLException {
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
	
	public void rmTb(final int sid, final String tableName) throws SQLException {
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
	
	public int getSid(final String projectId) throws SQLException {
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
	
	public String getTableName(final String projectId) throws SQLException {
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
