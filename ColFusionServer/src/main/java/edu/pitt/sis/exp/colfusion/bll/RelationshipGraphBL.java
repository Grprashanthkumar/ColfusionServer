package edu.pitt.sis.exp.colfusion.bll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.hibernate.mapping.Set;

import com.google.gson.Gson;

import edu.pitt.sis.exp.colfusion.dal.dao.DNameInfoDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.DNameInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dao.RelationshipsDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.RelationshipsDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionDnameinfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationships;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

public class RelationshipGraphBL {

	
	public String BuildJSON(){
		List<Object> finalResult = new ArrayList(); //list include all elements;
		//Following code is to search for data sets which are isolate.
		List<ColfusionSourceinfo> sourceInfoList= this.getSourceInfoWithoutRelationships();// get sourceinfo without relationships.
		for (int i = 0;i<sourceInfoList.size();i++){
			ColfusionSourceinfo info = (ColfusionSourceinfo)sourceInfoList.get(i);
			HashMap map =this.convertSourceInfoToMap(info);
			finalResult.add(map);
		}
		
		
		//Following code is to search for all relationship from relationship table;
		HashMap<String,Object> sidFalseMap = new HashMap();
		sidFalseMap.put("oneSid", false);
		HashMap<String,Object> pathMap = new HashMap();
		ArrayList pathList = new ArrayList();
		pathList.add(pathMap);
		sidFalseMap.put("allPaths",pathList);
		sidFalseMap.put("title","newPath");
		
		pathMap.put("avgConfidence", 1);
		pathMap.put("avgDataMatchingRatio", 1);
		List<Object> relationshipList =new ArrayList();
		pathMap.put("relationships",relationshipList);
		HashSet<Integer> sidSet = new HashSet();
		HashSet<String> sidTitleSet = new HashSet();
		HashSet<Object> columnSet = new HashSet();
		pathMap.put("sidTitles",sidTitleSet);
		pathMap.put("sids", sidSet);
		ArrayList<Object> relIds = new ArrayList<Object>();
		pathMap.put("relIds",relIds);
		pathMap.put("allColumns",columnSet);
		pathMap.put("oneSid", false);
		pathMap.put("tableName","NA");
		pathMap.put("title","All relationships");
		pathMap.put("sid","2197,2187,2192,3,1");
		pathMap.put("foundSearchKeys",new ArrayList());
		List<ColfusionRelationships> relList =this.getAllRelationShip();
		for (ColfusionRelationships rel :relList){
			relIds.add(rel.getRelId().toString());
			HashMap<String,Object> oneRel =new HashMap();
			HashMap<String,Object> sidFrom = new HashMap();
			HashMap<String,Object> sidTo = new HashMap();
			ColfusionSourceinfo souInfo1 =rel.getColfusionSourceinfoBySid1();
			sidFrom = this.convertSourceInfoToMap(columnSet,souInfo1, false);
			sidSet.add(souInfo1.getSid());
			sidTitleSet.add(souInfo1.getTitle());
			ColfusionSourceinfo souInfo2 =rel.getColfusionSourceinfoBySid2();
			sidSet.add(souInfo2.getSid());
			sidTitleSet.add(souInfo2.getTitle());
			sidTo =this.convertSourceInfoToMap(columnSet,souInfo2,false);
			oneRel.put("sidFrom", sidFrom);
			oneRel.put("sidTo", sidTo);
			oneRel.put("relId", rel.getRelId().toString());
			oneRel.put("relName",rel.getName());
			oneRel.put("confidence",new Double(1).toString());
			oneRel.put("dataMatchingRatio", null);
			relationshipList.add(oneRel);
		}
		finalResult.add(sidFalseMap);
		
		
		Gson gson = new Gson();
		String result = gson.toJson(finalResult);
		System.out.println(result);
		return result;
	}
	
	public List<ColfusionRelationships> getAllRelationShip(){
		List<ColfusionRelationships> relationshipList = new ArrayList<ColfusionRelationships>();
		
		RelationshipsDAO relDAO = new RelationshipsDAOImpl();
		HibernateUtil.beginTransaction();
		relationshipList = relDAO.findAll(ColfusionRelationships.class);
		
		return relationshipList;
	}
	
	public HashMap<String,Object> convertSourceInfoToMap(ColfusionSourceinfo info){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("oneSid", true);
		map.put("sid",info.getSid().toString());
		map.put("title", info.getTitle());
		map.put("tableName","Sheet1");
		map.put("allColumns",this.convertDnameInfoToList(getColunmsBySid(info.getSid())));
		map.put("foundSearchKeys",new ArrayList());
		return  map;
	}
	
	public HashMap<String,Object> convertSourceInfoToMap(HashSet<Object> columnSet,ColfusionSourceinfo info,boolean onesid){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("sid",info.getSid().toString());
		map.put("sidTitle", info.getTitle());
		map.put("tableName","Sheet1");
		ArrayList<HashMap> columns= this.convertDnameInfoToList(getColunmsBySid(info.getSid()));
		for(HashMap col :columns){
			columnSet.add(col);
		}
		
		map.put("allColumns",this.convertDnameInfoToList(getColunmsBySid(info.getSid())));
		return  map;
	}
	
	
	public ArrayList<HashMap> convertDnameInfoToList(List<ColfusionDnameinfo> DNameList){
		ArrayList<HashMap> list = new ArrayList<HashMap>();
		for (ColfusionDnameinfo info: DNameList){
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("cid", info.getCid().toString());
			map.put("dname_chosen",info.getDnameChosen());
			map.put("dname_value_type","String");
			map.put("dname_value_unit",null);
			map.put("dname_value_description", info.getDnameValueDescription());
			map.put("dname_original_name",info.getDnameOriginalName());
			list.add(map);
		}
		return list;
	}
	
	public List<ColfusionDnameinfo> getColunmsBySid(int sid){
		DNameInfoDAO dnameInfoDao = new DNameInfoDAOImpl();
		List<ColfusionDnameinfo> dNameInfoList = dnameInfoDao.findBySid(sid);
		return dNameInfoList; 
	}
	
	public List<ColfusionSourceinfo> getAllSourceInfos(){
		
		SourceInfoDAO sourceInfoDao = new SourceInfoDAOImpl();
		HibernateUtil.beginTransaction();
		List<ColfusionSourceinfo> allSourceInfo =sourceInfoDao.findAll(ColfusionSourceinfo.class);
		return allSourceInfo;
		
	}
	
	public List<ColfusionSourceinfo> getSourceInfoWithoutRelationships(){
		
		SourceInfoDAO sourceInfoDao = new SourceInfoDAOImpl();
		
		List<ColfusionSourceinfo> oneSidsTrue = sourceInfoDao.findOneSidsTrue();
		
		return oneSidsTrue;
	}
}
