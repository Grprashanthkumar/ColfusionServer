package edu.pitt.sis.exp.colfusion.bll;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.refine.ProjectManager;
import com.google.refine.ProjectMetadata;
import com.google.refine.commands.workspace.GetAllProjectMetadataCommand;
import com.google.refine.model.Cell;
import com.google.refine.model.Column;
import com.google.refine.model.ModelException;
import com.google.refine.model.Project;
import com.google.refine.model.Row;

import edu.pitt.sis.exp.colfusion.bll.OpenRefineBL;
import edu.pitt.sis.exp.colfusion.persistence.managers.OpenRefineProjectMapManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.OpenRefineProjectMapManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionOpenrefineProjectMap;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionOpenrefineProjectMapId;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGenImpl;

public class TestCreatePro {
final Logger logger = LogManager.getLogger(OpenRefineBL.class.getName());
	
	

	/**
	 * 
	 * @param sid
	 * @param tableName
	 * @return
	 * @throws Exception 
	 * @throws ClassNotFoundException 
	 */
	public GeneralResponseGen<String> createPro(int sid, String tableName) {
		
		GeneralResponseGen<String> result = new GeneralResponseGenImpl<>();
		
		result.setSuccessful(false);
		
		
//		GetAllProjectMetadataCommand.sid = sid;
//		GetAllProjectMetadataCommand.tableName = tableName;
		
//		String url = "http://127.0.0.1:3333";
//        URI uri = null;
//        try {
//                uri = new java.net.URI(url);  
//        } catch (Exception e) {
//                logger.info("Failed to load webpage!");
//        }
//        try {
//                java.awt.Desktop.getDesktop().browse(uri);      
//        } catch (Exception e) {
//                logger.info("Failed to open browser");
//        }
		JOptionPane.showMessageDialog(null, "Before", "Test Msg", JOptionPane.WARNING_MESSAGE);
		Project newProject = new Project();
		JOptionPane.showMessageDialog(null, "After", "Test Msg", JOptionPane.WARNING_MESSAGE);
        ProjectMetadata ppm = new ProjectMetadata();
//        int sid = 1711;
        
        ppm.setName("This_is_a_test115");
        ppm.setEncoding("UTF-8");
        
			try {
				setProject(sid, newProject);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//        setMyProjectCol(newProject);
//        setMyProjectRow(newProject);
        ProjectManager.singleton.registerProject(newProject, ppm);
        ProjectManager.singleton.ensureProjectSaved(newProject.id);
        
        
			try {
				saveRelation(newProject.id, sid, "Sheet1");
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		
		result.setMessage("OK");
		result.setPayload("Hello world!");
		result.setSuccessful(true);
		
		return result;
	}
	
	public void saveRelation(long projectId, int sid, String tableName) throws SQLException, ClassNotFoundException {
        String driver = "com.mysql.jdbc.Driver";
        String dbURL = "jdbc:mysql://127.0.0.1:3306/colfusion"; 
        String userName = "root"; // UserName 用户名
        String userPwd = ""; // Pwd 密码
        
        Connection conn = null;
        
                        Class.forName(driver);
         
        conn = DriverManager.getConnection(dbURL, userName, userPwd);
        
        String insertQuery = String.format("insert into colfusion_openrefine_project_map values('%d', %d, '%s')", projectId, sid, tableName);
        Statement ps = conn.createStatement();
        ps.executeUpdate(insertQuery);
    }
    public void setProject(int sid, Project project) throws SQLException, ModelException, ClassNotFoundException {
        String driver = "com.mysql.jdbc.Driver";
        String dbURL = "jdbc:mysql://127.0.0.1:3306/colfusion"; 
        String userName = "root"; // UserName 用户名
        String userPwd = ""; // Pwd 密码
        
        Connection conn = null;
        
                        Class.forName(driver);
         
        conn = DriverManager.getConnection(dbURL, userName, userPwd);
        
        // Get and set column names
        String colQuery = "select dname_chosen from colfusion_dnameinfo where sid = " + sid;
        Statement ps = conn.createStatement();
        ResultSet rs = ps.executeQuery(colQuery);
        ArrayList<String> columnNames = new ArrayList<String>();
        while(rs.next()) {
                columnNames.add(rs.getString("dname_chosen"));
        }
        setProjectCol(project, columnNames);
        
        // Get and set rows
        String rowQuery = "select tableName from colfusion_columntableinfo where cid = (select cid from colfusion_dnameinfo limit 1)";
        Statement ps1 = conn.createStatement();
        
        ResultSet newRs = ps1.executeQuery(rowQuery);
        String tableName = "";
        while(newRs.next()) {
            tableName = newRs.getString("tableName");
        }
        
        
        
        Connection conn1 = null;
        String dbURL1 = "jdbc:mysql://127.0.0.1:3306/colfusion_filetodb_" + sid;
        conn1 = DriverManager.getConnection(dbURL1, userName, userPwd);
        String rowQuery1 = "select * from " + tableName;
        Statement ps2 = conn1.createStatement();
        Statement ps3 = conn1.createStatement();
        ResultSet rs1 = ps2.executeQuery(rowQuery1);
        
        ResultSet rsCount = ps3.executeQuery("select count(*) from information_schema.columns where table_schema='colfusion_filetodb_" + sid + "' and table_name='" + tableName + "'");
        int colCount = 0;
        while(rsCount.next()) {
                colCount = Integer.parseInt(rsCount.getString(1));
        }
        
        ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
   
        while(rs1.next()) {
            int colIndex = 1;
            ArrayList<String> temp = new ArrayList<String>();
            while(colIndex <= colCount) {
                temp.add(rs1.getString(colIndex));
                colIndex++;
            }
            rows.add(temp);
        }
        setProjectRow(project, rows);
    }
    
    public void setProjectCol(Project project, ArrayList<String> columnNames) throws ModelException {
        for(int i = 0; i < columnNames.size(); i++) {
            Column column = new Column(i, columnNames.get(i));
            
                    project.columnModel.addColumn(i, column, true);
            
        }
    }
    
    public void setProjectRow(Project project, ArrayList<ArrayList<String>> rows) {
        for(int j = 0; j < rows.size(); j++) {
            Row row = new Row(rows.get(j).size());
            for(int k = 0; k < rows.get(j).size(); k++) {
                    Cell cell = new Cell(rows.get(j).get(k), null);
                    row.setCell(k, cell);
            }
            project.rows.add(row);
    }
    }
	
}
