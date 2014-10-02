package edu.pitt.sis.exp.colfusion.bll;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import edu.pitt.sis.exp.colfusion.dataModels.tableDataModel.Table;
import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.DatabaseHandlerBase;
import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.DatabaseHandlerFactory;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.responseModels.BasicTableResponseModel;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableByRelationshipsResponeModel;
import edu.pitt.sis.exp.colfusion.viewmodels.BasicTableInfoViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.JoinTablesByRelationshipsViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryMetadataViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryTargetDBViewModel;

public class BasicTableBL {

	BasicTableResponseModel result=new BasicTableResponseModel();
	 
	public BasicTableResponseModel getTableInfo(int sid, String tableName ){
		
        String driver = "com.mysql.jdbc.Driver";

        String url = "jdbc:mysql://127.0.0.1:3306/colfusion";
        String user = "dataverse"; 
        String password = "dataverse";

        try { 
        	
        	List<BasicTableInfoViewModel> tableInfoFromDB= new ArrayList<BasicTableInfoViewModel>();
        	
	         Class.forName(driver);
	         Connection conn = DriverManager.getConnection(url, user, password);
	
	         if(!conn.isClosed()) {
//	          System.out.println("Succeeded connecting to the Database!");
	         }
	         Statement statement = conn.createStatement();
	         String sql = "SELECT * FROM colfusion_columnTableInfo natural join colfusion_dnameinfo where sid = "+sid;
	
	         ResultSet rs = statement.executeQuery(sql);
         

	
	         while(rs.next()) {
	        	 BasicTableInfoViewModel tableInfo = new BasicTableInfoViewModel();
	        	 tableInfo.setCid(rs.getString("cid"));
	        	 tableInfo.setDname_chosen(rs.getString("dname_chosen"));
	        	 tableInfo.setDname_original_name(rs.getString("dname_original_name"));
	        	 tableInfo.setDname_value_description(rs.getString("dname_value_description"));
	        	 tableInfo.setDname_value_type(rs.getString("dname_value_type"));
	        	 tableInfo.setDname_value_unit(rs.getString("dname_value_unit"));
	        	 tableInfoFromDB.add(tableInfo);
	        	 
	        	 System.out.println(rs.getString("cid"));
	          	 System.out.println(rs.getString("dname_chosen"));
         }
	         
//	         result=tableInfoFromDB;
	         result.setPayload(tableInfoFromDB);
	         result.isSuccessful=true;
	         result.message="OK";
	         System.out.println(result.getPayload().get(0).getCid());
	         rs.close();
	         conn.close();
        } catch(ClassNotFoundException e) {
	         System.out.println("Sorry,can`t find the Driver!"); 
	         e.printStackTrace();  
        } catch(SQLException e) {
        	 e.printStackTrace();
        } catch(Exception e) {
        	 e.printStackTrace();
        } 


		return result;
		
	}
	
	public JointTableByRelationshipsResponeModel getTableDataBySidAndName(int sid, String tableName,
			int perPage, int pageNumber ){
		SourceInfoManagerImpl sourceInfo = new SourceInfoManagerImpl();
		StoryTargetDBViewModel storyTargetDBViewModel = sourceInfo.getStorySourceInfoDB(sid);
		try {
			DatabaseHandlerBase databaseHandlerBase = DatabaseHandlerFactory.getDatabaseHandler(storyTargetDBViewModel);
			Table table = databaseHandlerBase.getAll(tableName, perPage, pageNumber);
			
			int countTuples = databaseHandlerBase.getCount(tableName);
			int totalPage = (int) Math.ceil((double)countTuples / perPage);
			
			JointTableByRelationshipsResponeModel result = new JointTableByRelationshipsResponeModel(); 
			
			JoinTablesByRelationshipsViewModel payload = new JoinTablesByRelationshipsViewModel();
			payload.setJointTable(table);
			payload.setPerPage(perPage);
			payload.setPageNo(pageNumber);
			payload.setTotalPage(totalPage);
			
			
			result.setPayload(payload);
			
			result.isSuccessful = true;
			
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

}
