package edu.pitt.sis.exp.colfusion.bll;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dataModels.tableDataModel.Table;
import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.DatabaseHandlerBase;
import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.DatabaseHandlerFactory;
import edu.pitt.sis.exp.colfusion.persistence.managers.RelationshipsManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.RelationshipsManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationships;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationshipsColumns;
import edu.pitt.sis.exp.colfusion.relationships.transformation.Relationship;
import edu.pitt.sis.exp.colfusion.relationships.transformation.RelationshipLink;
import edu.pitt.sis.exp.colfusion.relationships.transformation.RelationshipTransformation;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableByRelationshipsResponeModel;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableResponeModel;
import edu.pitt.sis.exp.colfusion.similarityJoins.NestedLoopSimilarityJoin;
import edu.pitt.sis.exp.colfusion.similarityMeasures.LevenshteinDistance;
import edu.pitt.sis.exp.colfusion.similarityMeasures.NormalizedDistance;
import edu.pitt.sis.exp.colfusion.viewmodels.JoinTablesByRelationshipsViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.TwoJointTablesViewModel;

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
			ColfusionRelationshipsColumns links = (ColfusionRelationshipsColumns)rel.getColfusionRelationshipsColumnses().iterator().next();
			
			Table jointTable = joinTables(links.getId().getClFrom(), links.getId().getClTo(), joinTablesInfo.getSimilarityThreshold());
			
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

	public Table joinTables(final String clFrom, final String clTo, final double similarityThreshold) {
		try {
			RelationshipTransformation transformationFrom = new RelationshipTransformation(clFrom);
			
			DatabaseHandlerBase dbHandlerFrom = DatabaseHandlerFactory.getDatabaseHandler(transformationFrom.getTargetDbConnectionInfo());
			
			RelationshipTransformation transformationTo = new RelationshipTransformation(clTo);
			
			DatabaseHandlerBase dbHandlerTo = DatabaseHandlerFactory.getDatabaseHandler(transformationTo.getTargetDbConnectionInfo());
			
			Table allTuplesFrom = dbHandlerFrom.getAll(transformationFrom.getTableName(), transformationFrom.getColumnDbNames());
			
			Table allTuplesTo = dbHandlerTo.getAll(transformationTo.getTableName(), transformationTo.getColumnDbNames());
			
			NestedLoopSimilarityJoin simJoin = new NestedLoopSimilarityJoin(new NormalizedDistance(new LevenshteinDistance()), null, null);
			
			Table joinResult = simJoin.join(allTuplesFrom, allTuplesTo, 
					transformationFrom, transformationTo, similarityThreshold);
			
			return joinResult;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Table joinTables(final Relationship relationship, final double similarityThreshold) {
		try {
			DatabaseHandlerBase dbHandlerFrom = DatabaseHandlerFactory.getDatabaseHandler(relationship.getDbFrom());
			
			DatabaseHandlerBase dbHandlerTo = DatabaseHandlerFactory.getDatabaseHandler(relationship.getDbTo());
			
			Table allTuplesFrom = dbHandlerFrom.getAll(relationship.getTableNameFrom(), null);
			
			Table allTuplesTo = dbHandlerTo.getAll(relationship.getTableNameTo());
			
			NestedLoopSimilarityJoin simJoin = new NestedLoopSimilarityJoin(new NormalizedDistance(new LevenshteinDistance()), null, null);
			
			Table joinResult = simJoin.join(allTuplesFrom, allTuplesTo, 
					relationship.getLinks(), similarityThreshold);
			
			return joinResult;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public JointTableByRelationshipsResponeModel joinTablesByRelationships(final JoinTablesByRelationshipsViewModel joinTablesByRelationshipInfo) {
		try {
			RelationshipsManager relMng = new RelationshipsManagerImpl();
			
			Table joinResult = null;
			
			//TODO:need to make sure that the order of relationships is correct and it actually should be optimized to start with smaller datasets first.
			for (int i = 0; i < joinTablesByRelationshipInfo.getRelationshipIds().size(); i++) {
				ColfusionRelationships colfusionRelationship = relMng.findByID(joinTablesByRelationshipInfo.getRelationshipIds().get(i));
				
				List<RelationshipLink> links = new ArrayList<>();
				
				for (Object relColumnOBj : colfusionRelationship.getColfusionRelationshipsColumnses().toArray()) {
					ColfusionRelationshipsColumns colfusionLink = (ColfusionRelationshipsColumns) relColumnOBj;
					links.add(new RelationshipLink(new RelationshipTransformation(colfusionLink.getId().getClFrom()), 
							new RelationshipTransformation(colfusionLink.getId().getClTo())));
				}
				
				Relationship relationship = new Relationship(colfusionRelationship.getColfusionSourceinfoBySid1().getSid(), colfusionRelationship.getTableName1(), 
						colfusionRelationship.getColfusionSourceinfoBySid2().getSid(), colfusionRelationship.getTableName2(), links, 
						colfusionRelationship.getColfusionSourceinfoBySid1().getColfusionSourceinfoDb(), 
						colfusionRelationship.getColfusionSourceinfoBySid2().getColfusionSourceinfoDb());
				
				if (joinResult == null) {
					joinResult = joinTables(relationship, joinTablesByRelationshipInfo.getSimilarityThreshold());
				}
				else {
					DatabaseHandlerBase dbHandlerTo = DatabaseHandlerFactory.getDatabaseHandler(relationship.getDbTo());
					Table allTuplesTo = dbHandlerTo.getAll(relationship.getTableNameTo());
				}
			}
			
			JointTableByRelationshipsResponeModel result = new JointTableByRelationshipsResponeModel(); 
			
			JoinTablesByRelationshipsViewModel payload = new JoinTablesByRelationshipsViewModel();
			payload.setJointTable(joinResult);
			
			result.setPayload(payload);
			
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
