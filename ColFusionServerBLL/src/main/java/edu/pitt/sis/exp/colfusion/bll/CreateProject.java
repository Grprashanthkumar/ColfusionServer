package edu.pitt.sis.exp.colfusion.bll;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.pitt.sis.exp.colfusion.bll.responseModels.GeneralResponseGen;
import edu.pitt.sis.exp.colfusion.bll.responseModels.GeneralResponseGenImpl;

public class CreateProject {
	public GeneralResponseGen<String> testCreateProject(int sid,
			String tableName) throws SQLException, IOException {
		GeneralResponseGen<String> result = new GeneralResponseGenImpl<>();

		result.setSuccessful(false);
//		
//		String msg = "Please edit data in the new opened page.";
//		if(tempTbExist(sid, tableName)) {
//			rmTb(sid, "temp_" + tableName);
//			backupOriTb(sid, tableName);
//		} else {
//			backupOriTb(sid, tableName);
//		}
//		
//		/*
//		 * TODO
//		 * Try to create a new project which is the same with the original one
//		 */
////		backupOriProject(getProjectId(sid, tableName), sid, tableName);
//		
//		String projectLink = "";
//
//		isOpenRefineStarted();
//
//		if (isExist(sid, tableName)) {
//			projectLink = getProjectId(sid, tableName);
//			System.out.println("**************\nSuccess!\n***************");
//		} else {
//			
//			/*
//			 * This is an unstable way to save(create) a new project
//			 * sometimes project can be created, but sometimes not,
//			 * don't know why
//			 */
//			Project newProject = new Project();
//			ProjectMetadata ppm = new ProjectMetadata();
//			// int sid = 1711;
//
//			ppm.setName(tableName);
//			ppm.setEncoding("UTF-8");
//			setProject(sid, newProject);
//
//			File dir = new File("C:\\Users\\xxl\\AppData\\Roaming\\OpenRefine");
//			
//			
//			FileProjectManager.singleton = new ProjectManager() {
//				
//				@Override
//				protected void saveWorkspace() {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				protected void saveProject(Project arg0) throws IOException {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				protected void saveMetadata(ProjectMetadata arg0, long arg1)
//						throws Exception {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public boolean loadProjectMetadata(long arg0) {
//					// TODO Auto-generated method stub
//					return false;
//				}
//				
//				@Override
//				protected Project loadProject(long arg0) {
//					// TODO Auto-generated method stub
//					return null;
//				}
//				
//				@Override
//				public void importProject(long arg0, InputStream arg1, boolean arg2)
//						throws IOException {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public HistoryEntryManager getHistoryEntryManager() {
//					// TODO Auto-generated method stub
//					return null;
//				}
//				
//				@Override
//				public void exportProject(long arg0, TarOutputStream arg1)
//						throws IOException {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public void deleteProject(long arg0) {
//					// TODO Auto-generated method stub
//					
//				}
//			};
//			FileProjectManager.initialize(dir);
//			
//			
//			FileProjectManager.singleton.registerProject(newProject, ppm);
//			FileProjectManager.singleton.ensureProjectSaved(newProject.id);
//			/*
//			 * ********************************************
//			 * The following two lines solve the problem:
//			 * 
//			 * "Only the first time after rerun ColFusionServer, 
//			 * click the "Edit" button can create a project correctly"
//			 * 
//			 * I guess the reason is because without the "save()", some stuff will exist in
//			 * cache so that the new created project cannot be stored correctly
//			 */
//			newProject.dispose();
//			FileProjectManager.singleton.save(true);
//			
//			System.out.println("**********************");
//			System.out.println(portListener());
//			System.out.println("**********************");
//			
//			/*
//			 * ********************************************
//			 */
//
//			System.out.println("***********************************");
//			System.out.println(newProject.id);
//			System.out.println("***********************************");
//
//			saveRelation(newProject.id, sid, tableName);
//
//			projectLink = newProject.id + "";
//
//		}
//		/*
//		 * ****************************************************
//		 * Used to prevent project not being created
//		 */
//		File projectFile = new File("C:\\Users\\xxl\\AppData\\Roaming\\OpenRefine\\" + projectLink + ".project");
//		int count = 0;
//		while(!projectFile.exists() && count < 50) {
//			try {
//				Thread.sleep(100);
//				count++;
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
//		if(count >= 50)
//			msg += " Filed1!";
//		File projectDataFile1 = new File("C:\\Users\\xxl\\AppData\\Roaming\\OpenRefine\\" + projectLink + ".project\\data.zip");
//		File projectDataFile2 = new File("C:\\Users\\xxl\\AppData\\Roaming\\OpenRefine\\" + projectLink + ".project\\metadata.json");
//		int count1 = 0;
//		while((!projectDataFile1.exists() || !projectDataFile2.exists()) && count1 < 50) {
//			try {
//				Thread.sleep(100);
//				count1++;
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
//		if(count1 >= 50)
//			msg += " Filed2!";
//		
//		/*
//		 * *************above**********************
//		 */
//		if (!isOpenRefineStarted()) {
//			// If OpenRefine is not started, start OpenRefine
//			Process myProcess = null;
//			myProcess = Runtime.getRuntime().exec(
//					"cmd /c start C:\\Users\\xxl\\Desktop\\chat.cmd");
//			System.out.println("***********" + myProcess + "*************");
//
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
//
//		String url = "http://127.0.0.1:3333/project?project=" + projectLink;
//		URI uri = null;
//		try {
//			uri = new java.net.URI(url);
//		} catch (Exception e) {
//			System.out.println("Failed to load webpage!");
//		}
//		try {
//			java.awt.Desktop.getDesktop().browse(uri);
//		} catch (Exception e) {
//			System.out.println("Failed to open browser");
//		}
//
//		result.setMessage("OK");
//		result.setPayload(msg);
//		result.setSuccessful(true);

		return result;
	}
	
	public boolean portListener() {
		boolean run = false;  
        try {  
            Socket socket = new Socket("localhost" , 3333);  
            run = socket.isConnected();  
            socket.close();
        } catch (ConnectException e) {  
            System.out.println("Mysql has stop");  
        } catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(run)
        	return true;
        return false;
	}
	
	public void backupOriProject(String projectId, int sid, String tableName) throws SQLException {
		
		Long myProjectId = Long.valueOf(projectId);
		
		File dir = new File("C:\\Users\\xxl\\AppData\\Roaming\\OpenRefine");
//		FileProjectManager.initialize(dir);
		
//		Project tempProject = ProjectUtilities.load(dir, myProjectId);
		
//		saveToProjectMap(myProjectId, tempProject.id);
		
//		FileProjectManager.singleton.registerProject(tempProject, tempProject.getMetadata());
//		FileProjectManager.singleton.ensureProjectSaved(tempProject.id);
	}
	
	public void saveToProjectMap(long projectId, long tempProjectId) throws SQLException {
		String driver = "com.mysql.jdbc.Driver";
	    String dbURL = "jdbc:mysql://127.0.0.1:3306/test"; // Connect
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
	    
	    String query = "insert into projectmap values(projectId, tempProjectId)";
    	Statement ps = conn.createStatement();
    	ps.executeUpdate(query);
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

	public void backupOriTb(int sid, String tableName) throws SQLException {
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
	    
	    String query = "CREATE TABLE temp_" + tableName + " SELECT * FROM " + tableName;
    	Statement ps = conn.createStatement();
    	ps.executeUpdate(query);
	}
	
	// To know if OpenRefine is started, see if the port "3333" is occupied
	public boolean isOpenRefineStarted() throws IOException {
		String readLine;
		String outPut = "";
		Process process = Runtime.getRuntime().exec(
				"cmd /c netstat -nao|findstr \"3333\"");
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(process.getInputStream()));
		while((readLine = bufferedReader.readLine()) != null) {
			outPut += readLine;
		}
		/*
		 * Use subString "LISTENING" is because sometimes even if the OpenRefine
		 * is closed, the output is not null but with a status "TIME_WAIT",
		 * but only if the status is "LISTENING", the OpenRefine is started
		 */
		boolean result = outPut.contains("LISTENING");
		return result;
	}

	public String getProjectId(int sid, String tableName) throws SQLException {
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

		String selectQuery = "select projectId from colfusion_openrefine_project_map where sid = "
				+ sid + " and tableName = '" + tableName + "'";
		Statement ps = conn.createStatement();
		ResultSet rs = ps.executeQuery(selectQuery);

		String projectId = "";
		while (rs.next()) {
			projectId = rs.getString(1);
		}
		return projectId;
	}

	// See if such project is existed
	public boolean isExist(int sid, String tableName) throws SQLException {
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

		String selectQuery = "select * from colfusion_openrefine_project_map where sid = "
				+ sid + " and tableName = '" + tableName + "'";
		Statement ps = conn.createStatement();
		ResultSet rs = ps.executeQuery(selectQuery);

		while (rs.next()) {
			return true;
		}
		return false;
	}


	public static void saveRelation(long projectId, int sid, String tableName)
			throws SQLException {
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

		String insertQuery = String
				.format("insert into colfusion_openrefine_project_map values('%d', %d, '%s')",
						projectId, sid, tableName);
		Statement ps = conn.createStatement();
		ps.executeUpdate(insertQuery);
	}

//	public static void setProject(int sid, Project project) throws SQLException {
//		String driver = "com.mysql.jdbc.Driver";
//		String dbURL = "jdbc:mysql://127.0.0.1:3306/colfusion";
//		String userName = "root"; // UserName 用户名
//		String userPwd = ""; // Pwd 密码
//
//		Connection conn = null;
//		try {
//			Class.forName(driver);
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		conn = DriverManager.getConnection(dbURL, userName, userPwd);
//
//		// Get and set column names
//		String colQuery = "select dname_chosen from colfusion_dnameinfo where sid = "
//				+ sid;
//		Statement ps = conn.createStatement();
//		ResultSet rs = ps.executeQuery(colQuery);
//		ArrayList<String> columnNames = new ArrayList<String>();
//		while (rs.next()) {
//			columnNames.add(rs.getString("dname_chosen"));
//		}
//		setProjectCol(project, columnNames);
//
//		// Get and set rows
//		String rowQuery = "select tableName from colfusion_columntableinfo where cid = (select cid from colfusion_dnameinfo where sid = " + sid + " limit 1)";
//		Statement ps1 = conn.createStatement();
//
//		ResultSet newRs = ps1.executeQuery(rowQuery);
//		String tableName = "";
//		while (newRs.next()) {
//			tableName = newRs.getString("tableName");
//		}
//		System.out.println("***************setProject*************************");
//		System.out.println(tableName);
//		System.out.println("***************setProject*************************");
//		Connection conn1 = null;
//		String dbURL1 = "jdbc:mysql://127.0.0.1:3306/colfusion_filetodb_" + sid;
//		conn1 = DriverManager.getConnection(dbURL1, userName, userPwd);
//		String rowQuery1 = "select * from " + tableName;
//		Statement ps2 = conn1.createStatement();
//		Statement ps3 = conn1.createStatement();
//		ResultSet rs1 = ps2.executeQuery(rowQuery1);
//
//		ResultSet rsCount = ps3
//				.executeQuery("select count(*) from information_schema.columns where table_schema='colfusion_filetodb_"
//						+ sid + "' and table_name='" + tableName + "'");
//		int colCount = 0;
//		while (rsCount.next()) {
//			colCount = Integer.parseInt(rsCount.getString(1));
//		}
//
//		ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
//
//		while (rs1.next()) {
//			int colIndex = 1;
//			ArrayList<String> temp = new ArrayList<String>();
//			while (colIndex <= colCount) {
//				temp.add(rs1.getString(colIndex));
//				colIndex++;
//			}
//			rows.add(temp);
//		}
//		setProjectRow(project, rows);
//	}

//	public static void setProjectCol(Project project,
//			ArrayList<String> columnNames) {
//		for (int i = 0; i < columnNames.size(); i++) {
//			Column column = new Column(i, columnNames.get(i));
//			try {
//				project.columnModel.addColumn(i, column, true);
//			} catch (ModelException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
//
//	public static void setProjectRow(Project project,
//			ArrayList<ArrayList<String>> rows) {
//		for (int j = 0; j < rows.size(); j++) {
//			Row row = new Row(rows.get(j).size());
//			for (int k = 0; k < rows.get(j).size(); k++) {
//				Cell cell = new Cell(rows.get(j).get(k), null);
//				row.setCell(k, cell);
//			}
//			project.rows.add(row);
//		}
//	}
}
