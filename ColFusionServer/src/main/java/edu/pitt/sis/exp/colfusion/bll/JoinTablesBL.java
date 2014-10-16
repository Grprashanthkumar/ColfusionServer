package edu.pitt.sis.exp.colfusion.bll;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.RelationshipLink;
import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.transformation.RelationshipTransformation;
import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.transformation.RelationshipTransofmationUtil;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.Table;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerBase;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerFactory;
import edu.pitt.sis.exp.colfusion.dal.managers.RelationshipsManager;
import edu.pitt.sis.exp.colfusion.dal.managers.RelationshipsManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationships;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationshipsColumns;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoDb;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.JoinTablesByRelationshipsViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.TwoJointTablesViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.TwoTableJoinInputViewModel;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableByRelationshipsResponeModel;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableResponeModel;
import edu.pitt.sis.exp.colfusion.similarityJoins.NestedLoopSimilarityJoin;
import edu.pitt.sis.exp.colfusion.similarityMeasures.LevenshteinDistance;
import edu.pitt.sis.exp.colfusion.similarityMeasures.NormalizedDistance;

public class JoinTablesBL {

	final Logger logger = LogManager.getLogger(JoinTablesBL.class.getName());
	
	public JointTableResponeModel joinTables(final TwoJointTablesViewModel joinTablesInfo) {
		
		RelationshipsManager relMng = new RelationshipsManagerImpl();
		List<ColfusionRelationships> rels;
		try {
			rels = relMng.findByTables(joinTablesInfo.getSid1(), joinTablesInfo.getTableName1(), 
					joinTablesInfo.getSid2(), joinTablesInfo.getTableName2());
		
		
			//TODO:HACK, FIX ME!!!!
			ColfusionRelationships rel = rels.get(0);
			ColfusionRelationshipsColumns links = (ColfusionRelationshipsColumns) rel.getColfusionRelationshipsColumnses().iterator().next();
			
			Table jointTable = joinTables(rel.getRelId(), links.getId().getClFrom(), links.getId().getClTo(), joinTablesInfo.getSimilarityThreshold());
			
			TwoJointTablesViewModel resultPayload = new TwoJointTablesViewModel(joinTablesInfo.getSid1(), joinTablesInfo.getTableName1(), 
					joinTablesInfo.getSid2(), joinTablesInfo.getTableName2(), joinTablesInfo.getSimilarityThreshold(), jointTable);
			
			JointTableResponeModel result = new JointTableResponeModel();
			result.message = "OK";
			result.setPayload(resultPayload);
			result.isSuccessful = true;
				
			return result;
		} catch (Exception e) {
			JointTableResponeModel result = new JointTableResponeModel();
			result.message = "Error: " + e.getMessage();
			result.setPayload(null);
			result.isSuccessful = false;
				
			return result;
		}
	}

	public Table joinTables(final Integer relId, final String clFrom, final String clTo, final double similarityThreshold) {
		try {
			SourceInfoManager sourceMng = new SourceInfoManagerImpl();
			
			ColfusionSourceinfoDb srouceInfoDBFrom = sourceMng.getColfusionSourceinfoDbFrom(relId);
			ColfusionSourceinfoDb srouceInfoDBTo = sourceMng.getColfusionSourceinfoDbTo(relId);
			
			RelationshipTransformation transformationFrom = RelationshipTransofmationUtil.makeRelationshipTransformation(relId, clFrom);
			List<RelationshipTransformation> transformationFromList = new ArrayList<RelationshipTransformation>();
			transformationFromList.add(transformationFrom);
			
			DatabaseHandlerBase dbHandlerFrom = DatabaseHandlerFactory.getDatabaseHandler(srouceInfoDBFrom);
			
			RelationshipTransformation transformationTo = RelationshipTransofmationUtil.makeRelationshipTransformation(relId, clTo);
			List<RelationshipTransformation> transformationToList = new ArrayList<RelationshipTransformation>();
			transformationToList.add(transformationTo);
			
			DatabaseHandlerBase dbHandlerTo = DatabaseHandlerFactory.getDatabaseHandler(srouceInfoDBTo);
			
			Table allTuplesFrom = dbHandlerFrom.getAll(transformationFrom.getTableName());//, transformationFrom.getColumnDbNames());
			
			Table allTuplesTo = dbHandlerTo.getAll(transformationTo.getTableName());// , transformationTo.getColumnDbNames());
			
			NestedLoopSimilarityJoin simJoin = new NestedLoopSimilarityJoin(new NormalizedDistance(new LevenshteinDistance()), null, null);
			
			Table joinResult = simJoin.join(allTuplesFrom, allTuplesTo, 
					transformationFromList, transformationToList, similarityThreshold);
			
			return joinResult;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

//	public Table joinTables(final Relationship relationship, final double similarityThreshold) {
//		try {
//			DatabaseHandlerBase dbHandlerFrom = DatabaseHandlerFactory.getDatabaseHandler(relationship.getDbFrom());
//			
//			DatabaseHandlerBase dbHandlerTo = DatabaseHandlerFactory.getDatabaseHandler(relationship.getDbTo());
//			
//			Table allTuplesFrom = dbHandlerFrom.getAll(relationship.getTableNameFrom(), null);
//			
//			Table allTuplesTo = dbHandlerTo.getAll(relationship.getTableNameTo());
//			
//			NestedLoopSimilarityJoin simJoin = new NestedLoopSimilarityJoin(new NormalizedDistance(new LevenshteinDistance()), null, null);
//			
//			Table joinResult = simJoin.join(allTuplesFrom, allTuplesTo, 
//					relationship.getLinks(), similarityThreshold);
//			
//			return joinResult;
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
	
	public JointTableByRelationshipsResponeModel joinTablesByRelationships(final JoinTablesByRelationshipsViewModel joinTablesByRelationshipInfo) {
		
		try {
			
			List<TableAsHalfLink> halvesToJoin = getTablesAsHalfLinksInOrderToJoin(joinTablesByRelationshipInfo);
			
			if (halvesToJoin.size() < 2) {
				throw new RuntimeException("There should be at least two tables to perform similarity join");
			}
			
			NestedLoopSimilarityJoin simJoin = new NestedLoopSimilarityJoin(new NormalizedDistance(new LevenshteinDistance()), null, null);
			
			Table resultTable = null;
			
			try (DatabaseHandlerBase dbHandler1 = DatabaseHandlerFactory.getDatabaseHandler(halvesToJoin.get(0).getDbInfo())) {
				resultTable = dbHandler1.getAll(halvesToJoin.get(0).getTableName());
			}
			
			for (int i = 1; i < halvesToJoin.size(); i++) {
				
				Table allTuples2 = null;
				
				try (DatabaseHandlerBase dbHandler2 = DatabaseHandlerFactory.getDatabaseHandler(halvesToJoin.get(i).getDbInfo())) {
					allTuples2 = dbHandler2.getAll(halvesToJoin.get(i).getTableName());
				}
				
				resultTable = simJoin.join(resultTable, allTuples2, 
						halvesToJoin.get(i).getTransformationsReferredTable(), halvesToJoin.get(i).getTransformationsCurrentTable(), joinTablesByRelationshipInfo.getSimilarityThreshold());
			}
			
			JointTableByRelationshipsResponeModel result = new JointTableByRelationshipsResponeModel(); 
			
			JoinTablesByRelationshipsViewModel payload = new JoinTablesByRelationshipsViewModel();
			payload.setJointTable(resultTable);
			
			result.setPayload(payload);
			result.isSuccessful = true;
			
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param joinTablesByRelationshipInfo
	 * @return
	 * @throws Exception
	 */
	private List<TableAsHalfLink> getTablesAsHalfLinksInOrderToJoin(
			final JoinTablesByRelationshipsViewModel joinTablesByRelationshipInfo)
			throws Exception {
		List<TableAsHalfLink> result = new LinkedList<JoinTablesBL.TableAsHalfLink>();
		
		RelationshipsManager relMng = new RelationshipsManagerImpl();
		
		//TODO:need to make sure that the order of relationships is correct and it actually should be optimized to start with smaller datasets first.
		for (int i = 0; i < joinTablesByRelationshipInfo.getRelationshipIds().size(); i++) {
			ColfusionRelationships colfusionRelationship = relMng.findByID(joinTablesByRelationshipInfo.getRelationshipIds().get(i));
			
			List<RelationshipLink> links = new ArrayList<>();
			
			List<RelationshipTransformation> transformations1 =  new ArrayList<RelationshipTransformation>();
			List<RelationshipTransformation> transformations2 = new ArrayList<RelationshipTransformation>();
			
			for (Object relColumnOBj : colfusionRelationship.getColfusionRelationshipsColumnses().toArray()) {
				ColfusionRelationshipsColumns colfusionLink = (ColfusionRelationshipsColumns) relColumnOBj;
				links.add(new RelationshipLink(RelationshipTransofmationUtil.makeRelationshipTransformation(colfusionRelationship.getRelId(), colfusionLink.getId().getClFrom()), 
						RelationshipTransofmationUtil.makeRelationshipTransformation(colfusionRelationship.getRelId(), colfusionLink.getId().getClTo())));
				
				transformations1.add(RelationshipTransofmationUtil.makeRelationshipTransformation(colfusionRelationship.getRelId(), colfusionLink.getId().getClFrom()));
				transformations2.add(RelationshipTransofmationUtil.makeRelationshipTransformation(colfusionRelationship.getRelId(), colfusionLink.getId().getClTo()));
			}
			
			TableAsHalfLink half1 = new TableAsHalfLink(colfusionRelationship.getColfusionSourceinfoBySid1().getColfusionSourceinfoDb(), 
					colfusionRelationship.getTableName1(), 
					transformations1, transformations2);
			
			TableAsHalfLink half2 = new TableAsHalfLink(colfusionRelationship.getColfusionSourceinfoBySid2().getColfusionSourceinfoDb(), 
					colfusionRelationship.getTableName2(), 
					transformations2, transformations1);
			
			if (result.contains(half1)) {
				if (result.contains(half2)) {
					
				}
				else {
					result.add(half2);
				}
			}
			else {
				if (result.contains(half2)) {
					result.add(half1);
				}
				else {
					result.add(half1);
					
					result.add(half2);
				}
			}
		}
		
		return result;
	}
	
	private static class TableAsHalfLink {
		
		private final ColfusionSourceinfoDb dbInfo;
		private final String tableName;
		private final List<RelationshipTransformation> transformationsCurrentTable;
		private final List<RelationshipTransformation> transformationsReferredTable;
		
		public TableAsHalfLink(final ColfusionSourceinfoDb dbInfo, final String tableName, 
				final List<RelationshipTransformation> transformationsCurrentTable,
				final List<RelationshipTransformation> transformationsReferredTable) {
			this.dbInfo = dbInfo;
			this.tableName = tableName;
			this.transformationsCurrentTable = transformationsCurrentTable;
			this.transformationsReferredTable = transformationsReferredTable;
		}

		/**
		 * @return the dbInfo
		 */
		public ColfusionSourceinfoDb getDbInfo() {
			return dbInfo;
		}

		/**
		 * @return the tableName
		 */
		public String getTableName() {
			return tableName;
		}
		
		/**
		 * @return the transformation
		 */
		public List<RelationshipTransformation>  getTransformationsCurrentTable() {
			return transformationsCurrentTable;
		}
		
		/**
		 * @return the transformation
		 */
		public List<RelationshipTransformation>  getTransformationsReferredTable() {
			return transformationsReferredTable;
		}
		
		@Override
		public boolean equals(final Object other) {
			
			if (other instanceof TableAsHalfLink) {
				TableAsHalfLink typedOther = (TableAsHalfLink) other;
				
				return this.tableName.equals(typedOther.getTableName()) && 
						this.getDbInfo().getSourceDatabase().equals(typedOther.getDbInfo().getSourceDatabase());
			}
			
			return false;
		}
	}

	public TwoJointTablesViewModel joinTables(final TwoTableJoinInputViewModel model) {
		// TODO Auto-generated method stub
		return null;
	}
}
