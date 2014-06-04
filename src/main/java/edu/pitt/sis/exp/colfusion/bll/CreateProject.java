package edu.pitt.sis.exp.colfusion.bll;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;











import com.google.refine.ProjectMetadata;
import com.google.refine.io.FileProjectManager;
import com.google.refine.model.Cell;
import com.google.refine.model.Column;
import com.google.refine.model.ModelException;
import com.google.refine.model.Project;
import com.google.refine.model.Row;

import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGenImpl;

public class CreateProject {
	public GeneralResponseGen<String> testCreateProject(int sid,
			String tableName) throws SQLException, IOException {
		GeneralResponseGen<String> result = new GeneralResponseGenImpl<>();

		result.setSuccessful(false);

		backupOriTb(sid, tableName);
		
		String projectLink = "";

		isOpenRefineStarted();

		if (isExist(sid, tableName)) {
			projectLink = getProjectId(sid, tableName);
			System.out.println("**************\nSuccess!\n***************");
		} else {
			Project newProject = new Project();
			ProjectMetadata ppm = new ProjectMetadata();
			// int sid = 1711;

			ppm.setName(tableName);
			ppm.setEncoding("UTF-8");
			setProject(sid, newProject);

			File dir = new File("C:\\Users\\xxl\\AppData\\Roaming\\OpenRefine");
			FileProjectManager.initialize(dir);
			FileProjectManager.singleton.registerProject(newProject, ppm);
			FileProjectManager.singleton.ensureProjectSaved(newProject.id);

			System.out.println("***********************************");
			System.out.println(newProject.id);
			System.out.println("***********************************");

			saveRelation(newProject.id, sid, tableName);

			projectLink = newProject.id + "";

		}
		if (!isOpenRefineStarted()) {
			// If OpenRefine is not started, start OpenRefine
			Process myProcess = null;
			myProcess = Runtime.getRuntime().exec(
					"cmd /c start C:\\Users\\xxl\\Desktop\\chat.cmd");
			System.out.println("***********" + myProcess + "*************");

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		String url = "http://127.0.0.1:3333/project?project=" + projectLink;
		URI uri = null;
		try {
			uri = new java.net.URI(url);
		} catch (Exception e) {
			System.out.println("Failed to load webpage!");
		}
		try {
			java.awt.Desktop.getDesktop().browse(uri);
		} catch (Exception e) {
			System.out.println("Failed to open browser");
		}

		result.setMessage("OK");
		result.setPayload("Please edit data in the new opened page.");
		result.setSuccessful(true);

		return result;
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

	public static void setProject(int sid, Project project) throws SQLException {
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

		// Get and set column names
		String colQuery = "select dname_chosen from colfusion_dnameinfo where sid = "
				+ sid;
		Statement ps = conn.createStatement();
		ResultSet rs = ps.executeQuery(colQuery);
		ArrayList<String> columnNames = new ArrayList<String>();
		while (rs.next()) {
			columnNames.add(rs.getString("dname_chosen"));
		}
		setProjectCol(project, columnNames);

		// Get and set rows
		String rowQuery = "select tableName from colfusion_columntableinfo where cid = (select cid from colfusion_dnameinfo limit 1)";
		Statement ps1 = conn.createStatement();

		ResultSet newRs = ps1.executeQuery(rowQuery);
		String tableName = "";
		while (newRs.next()) {
			tableName = newRs.getString("tableName");
		}

		Connection conn1 = null;
		String dbURL1 = "jdbc:mysql://127.0.0.1:3306/colfusion_filetodb_" + sid;
		conn1 = DriverManager.getConnection(dbURL1, userName, userPwd);
		String rowQuery1 = "select * from " + tableName;
		Statement ps2 = conn1.createStatement();
		Statement ps3 = conn1.createStatement();
		ResultSet rs1 = ps2.executeQuery(rowQuery1);

		ResultSet rsCount = ps3
				.executeQuery("select count(*) from information_schema.columns where table_schema='colfusion_filetodb_"
						+ sid + "' and table_name='" + tableName + "'");
		int colCount = 0;
		while (rsCount.next()) {
			colCount = Integer.parseInt(rsCount.getString(1));
		}

		ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();

		while (rs1.next()) {
			int colIndex = 1;
			ArrayList<String> temp = new ArrayList<String>();
			while (colIndex <= colCount) {
				temp.add(rs1.getString(colIndex));
				colIndex++;
			}
			rows.add(temp);
		}
		setProjectRow(project, rows);
	}

	public static void setProjectCol(Project project,
			ArrayList<String> columnNames) {
		for (int i = 0; i < columnNames.size(); i++) {
			Column column = new Column(i, columnNames.get(i));
			try {
				project.columnModel.addColumn(i, column, true);
			} catch (ModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void setProjectRow(Project project,
			ArrayList<ArrayList<String>> rows) {
		for (int j = 0; j < rows.size(); j++) {
			Row row = new Row(rows.get(j).size());
			for (int k = 0; k < rows.get(j).size(); k++) {
				Cell cell = new Cell(rows.get(j).get(k), null);
				row.setCell(k, cell);
			}
			project.rows.add(row);
		}
	}
}
