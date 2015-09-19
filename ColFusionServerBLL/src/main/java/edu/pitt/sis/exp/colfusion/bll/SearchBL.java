package edu.pitt.sis.exp.colfusion.bll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.responseModels.LocationListResponse;
import edu.pitt.sis.exp.colfusion.bll.responseModels.RowsResponseModel;
import edu.pitt.sis.exp.colfusion.bll.responseModels.StoryTableResponse;
import edu.pitt.sis.exp.colfusion.dal.dao.LocationIndexDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.RelationKey;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.Table;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerBase;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerFactory;
import edu.pitt.sis.exp.colfusion.dal.managers.DNameInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionIndexlocation;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.DnameViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.LocationViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.SourceInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryTableViewModel;

public class SearchBL {

	final Logger logger = LogManager.getLogger(StoryBL.class.getName());
	
	public LocationListResponse getFacetedList(final String title){
		LocationListResponse result =new LocationListResponse();
		HashMap<String, ArrayList<SourceInfoViewModel>> responseHashMap = new HashMap<String, ArrayList<SourceInfoViewModel>>();
		try{
			SourceInfoManagerImpl sourceInfoManagerImpl = new SourceInfoManagerImpl();
			List<ColfusionSourceinfo> contents = sourceInfoManagerImpl.findBySidOrTitle(title);
			LocationIndexDAOImpl locationIndexDAOImpl = new LocationIndexDAOImpl();
			ArrayList<ColfusionIndexlocation> results =new ArrayList<ColfusionIndexlocation>();
			for (int k=0;k<contents.size();k++) {
				results.addAll(locationIndexDAOImpl.findLocationBySid(contents.get(k).getSid()));
			}
			
			for(int i=0;i<results.size();i++){
				ColfusionSourceinfo colfusionSourceinfo = sourceInfoManagerImpl.findByID(results.get(i).getSid());
				
				if(responseHashMap.get(results.get(i).getLocationSearchKey()) == null){
					SourceInfoViewModel sourceInfoViewModel = new SourceInfoViewModel();
					sourceInfoViewModel.setSid(results.get(i).getSid());
					sourceInfoViewModel.setTitle(colfusionSourceinfo.getTitle());
					ArrayList<SourceInfoViewModel> sourceInfoViewModelList = new ArrayList<SourceInfoViewModel>();
					sourceInfoViewModelList.add(sourceInfoViewModel);
					responseHashMap.put(results.get(i).getLocationSearchKey(), sourceInfoViewModelList);
				}else{
					SourceInfoViewModel sourceInfoViewModel = new SourceInfoViewModel();
					sourceInfoViewModel.setSid(results.get(i).getSid());
					sourceInfoViewModel.setTitle(colfusionSourceinfo.getTitle());
					ArrayList<SourceInfoViewModel> sourceInfoViewModelList = responseHashMap.get(results.get(i).getLocationSearchKey());
					sourceInfoViewModelList.add(sourceInfoViewModel);
					responseHashMap.put(results.get(i).getLocationSearchKey(), sourceInfoViewModelList);
				}	
				
			}
			ArrayList<LocationViewModel> payload = new ArrayList<LocationViewModel>();
			Iterator it = responseHashMap.entrySet().iterator();
		    while (it.hasNext()) {
		    	Map.Entry pair = (Map.Entry)it.next();
		        System.out.println(pair.getKey() + " = " + pair.getValue());
		        LocationViewModel locationViewModel = new LocationViewModel();
		        locationViewModel.setLocationIndex((String)pair.getKey());
		        locationViewModel.setSourceinfoList((ArrayList<SourceInfoViewModel>)pair.getValue());
		        payload.add(locationViewModel);
		        it.remove();
		    }
			
		    result.setPayload(payload);
		    result.isSuccessful = true;
			
		}
		catch(Exception e) {
			result.isSuccessful=false;
			result.message = "Get faceted data sets failed";
		}
		return result;
	}
	
	//TODO: Need to optimize.
	public RowsResponseModel getLocationRows(final int sid){
		DatabaseHandlerBase databaseHandlerBase;
		RowsResponseModel rowsResponseModel = null;
		try {
			databaseHandlerBase = DatabaseHandlerFactory.getTargetDatabaseHandler(sid);
			SourceInfoManagerImpl sourceInfoManagerImpl = new SourceInfoManagerImpl();
			List<RelationKey> tableNames = sourceInfoManagerImpl.getTableNames(sid);
			//Find location column name
			List<String> columnDbNames = new ArrayList<String>();
			DNameInfoManagerImpl dNameInfoManagerImpl = new DNameInfoManagerImpl();
			List<DnameViewModel> dnameViewModdel = dNameInfoManagerImpl.getDnameListViewModel(sid);
			for (int i = 0; i < dnameViewModdel.size(); i++) {
				if (dnameViewModdel.get(i).getDname_value_type().equals("Location")) {
					columnDbNames.add(dnameViewModdel.get(i).getDname_chosen());
				}
			}
			ArrayList<String> payload = new ArrayList<String>();
			if (columnDbNames.size()>0) {
				for(int i = 0; i < tableNames.size(); i++) {
					Table table = databaseHandlerBase.getAll(tableNames.get(i), columnDbNames);
					for (int j = 0; j < table.getRows().size(); j++) {
						String colName = table.getRows().get(j).getColumnGroups().get(0).getColumns().get(0).getCell().toString();
						if(!payload.contains(colName)){
							payload.add(colName);
						}
					}
					
				}
			}
			rowsResponseModel = new RowsResponseModel();
			rowsResponseModel.setPayload(payload);
			rowsResponseModel.isSuccessful = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String message = "Exception:" + e;
			
			logger.error(message);
		}
		
		return rowsResponseModel;
	}
	
	
	public StoryTableResponse getStoryRowsBySid(final int sid) {
		DatabaseHandlerBase databaseHandlerBase;
		StoryTableResponse storyTableResponse = new StoryTableResponse();
		ArrayList<StoryTableViewModel> storyTableViewModelList = new ArrayList<StoryTableViewModel>();
		try {
			databaseHandlerBase = DatabaseHandlerFactory.getTargetDatabaseHandler(sid);
			SourceInfoManagerImpl sourceInfoManagerImpl = new SourceInfoManagerImpl();
			List<RelationKey> tableNames = sourceInfoManagerImpl.getTableNames(sid);
			for(int i = 0; i < tableNames.size(); i++) {
				Table table = databaseHandlerBase.getAll(tableNames.get(i));
				StoryTableViewModel storyTableViewModel = new StoryTableViewModel();
				ArrayList<String> colNames = new ArrayList<>();
				ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
				for (int k = 0; k < table.getRows().get(0).getColumnGroups().get(0).getColumns().size(); k++) {
					colNames.add(table.getRows().get(0).getColumnGroups().get(0).getColumns().get(k).getOriginalName().toString());
				}
				for (int j = 0; j < table.getRows().size(); j++) {
					ArrayList<String> row = new ArrayList<String>();
					for (int k = 0; k < table.getRows().get(j).getColumnGroups().get(0).getColumns().size(); k++) {
						row.add(table.getRows().get(j).getColumnGroups().get(0).getColumns().get(k).getCell().toString());
					}
					rows.add(row);	
				}
				storyTableViewModel.setColNames(colNames);
				storyTableViewModel.setRows(rows);
				storyTableViewModelList.add(storyTableViewModel);
			}
			storyTableResponse.setPayload(storyTableViewModelList);
			storyTableResponse.isSuccessful = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String message = "Exception:" + e;
			logger.error(message);
		}
		return storyTableResponse;
		
	}
	
	
	public LocationListResponse getLocationList(){
		HashMap<String, ArrayList<SourceInfoViewModel>> responseHashMap = new HashMap<String, ArrayList<SourceInfoViewModel>>();
		ArrayList<ColfusionIndexlocation> results = null;
		LocationIndexDAOImpl locationIndexDAOImpl = new LocationIndexDAOImpl();
		results = (ArrayList<ColfusionIndexlocation>) locationIndexDAOImpl.findAllLocation();
		SourceInfoManagerImpl sourceInfoManagerImpl = new SourceInfoManagerImpl();
		for (int i = 0; i < results.size(); i++) {
			ColfusionSourceinfo colfusionSourceinfo = sourceInfoManagerImpl.findByID(results.get(i).getSid());
			if(responseHashMap.get(results.get(i).getLocationSearchKey()) == null){
				SourceInfoViewModel sourceInfoViewModel = new SourceInfoViewModel();
				sourceInfoViewModel.setSid(results.get(i).getSid());
				sourceInfoViewModel.setTitle(colfusionSourceinfo.getTitle());
				ArrayList<SourceInfoViewModel> sourceInfoViewModelList = new ArrayList<SourceInfoViewModel>();
				sourceInfoViewModelList.add(sourceInfoViewModel);
				responseHashMap.put(results.get(i).getLocationSearchKey(), sourceInfoViewModelList);
			}else{
				SourceInfoViewModel sourceInfoViewModel = new SourceInfoViewModel();
				sourceInfoViewModel.setSid(results.get(i).getSid());
				sourceInfoViewModel.setTitle(colfusionSourceinfo.getTitle());
				ArrayList<SourceInfoViewModel> sourceInfoViewModelList = responseHashMap.get(results.get(i).getLocationSearchKey());
				sourceInfoViewModelList.add(sourceInfoViewModel);
				responseHashMap.put(results.get(i).getLocationSearchKey(), sourceInfoViewModelList);
			}	
		}
		
		ArrayList<LocationViewModel> payload = new ArrayList<LocationViewModel>();
		Iterator it = responseHashMap.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        LocationViewModel locationViewModel = new LocationViewModel();
	        locationViewModel.setLocationIndex((String)pair.getKey());
	        locationViewModel.setSourceinfoList((ArrayList<SourceInfoViewModel>)pair.getValue());
	        payload.add(locationViewModel);
	        it.remove();
	    }
		
	    LocationListResponse result =new LocationListResponse();
	    result.setPayload(payload);
	    result.isSuccessful = true;
		
		return result;
		
	}
}
