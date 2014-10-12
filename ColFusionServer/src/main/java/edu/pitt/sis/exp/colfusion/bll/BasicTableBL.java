package edu.pitt.sis.exp.colfusion.bll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.Table;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerBase;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerFactory;
import edu.pitt.sis.exp.colfusion.dal.managers.ExecutionInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionExecuteinfo;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.BasicTableInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.JoinTablesByRelationshipsViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryTargetDBViewModel;
import edu.pitt.sis.exp.colfusion.responseModels.BasicTableResponseModel;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableByRelationshipsResponeModel;
import edu.pitt.sis.exp.colfusion.responseModels.StoryStatusResponseModel;

public class BasicTableBL {

	BasicTableResponseModel result=new BasicTableResponseModel();
	 
	public BasicTableResponseModel getTableInfo(final int sid, final String tableName ){
		
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
	
	public JointTableByRelationshipsResponeModel getTableDataBySidAndName(final int sid, final String tableName,
			final int perPage, final int pageNumber ){
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
	
	//TODO: need to be refactored
	public JointTableByRelationshipsResponeModel getTableDataBySidAndName(final int sid, final String tableName){
		SourceInfoManagerImpl sourceInfo = new SourceInfoManagerImpl();
		StoryTargetDBViewModel storyTargetDBViewModel = sourceInfo.getStorySourceInfoDB(sid);
		try {
			DatabaseHandlerBase databaseHandlerBase = DatabaseHandlerFactory.getDatabaseHandler(storyTargetDBViewModel);
			Table table = databaseHandlerBase.getAll(tableName);
			
			JointTableByRelationshipsResponeModel result = new JointTableByRelationshipsResponeModel(); 
			
			JoinTablesByRelationshipsViewModel payload = new JoinTablesByRelationshipsViewModel();
			payload.setJointTable(table);
			result.setPayload(payload);
			
			result.isSuccessful = true;
			
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public StoryStatusResponseModel getStoryStatus(final int sid){
		StoryStatusResponseModel result = new StoryStatusResponseModel();
		List<ColfusionExecuteinfo> contents = new ArrayList<ColfusionExecuteinfo>();
		try {
			SourceInfoManagerImpl sourceInfoManagerImpl = new SourceInfoManagerImpl();
			
			//I cannot get tableNames list from database by Hibernate!!!!!!
			List<String> tableNames = sourceInfoManagerImpl.getTableNames(sid);
			
			ExecutionInfoManagerImpl executionInfoManagerImpl = new ExecutionInfoManagerImpl();
			for(int i=0;i<tableNames.size();i++){
				ColfusionExecuteinfo content = executionInfoManagerImpl.getExecutionInfo(sid,tableNames.get(i));
				contents.add(content);
			}
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(Exception e) {
			result.isSuccessful=false;
			result.message = "Get StoryStatus failed";
		}
		
		
		return result;
	}
	
	

}
