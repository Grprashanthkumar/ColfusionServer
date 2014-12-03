package edu.pitt.sis.exp.colfusion.bll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.dao.StatonverdictsDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.StatonverdictsDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.RelationKey;
import edu.pitt.sis.exp.colfusion.dal.managers.DNameInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.DNameInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.GeneralManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.RelationshipsManager;
import edu.pitt.sis.exp.colfusion.dal.managers.RelationshipsManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationships;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.DnameViewModel;
import edu.pitt.sis.exp.colfusion.relationships.relationshipGraph.RelationshipGraph;
import edu.pitt.sis.exp.colfusion.relationships.relationshipGraph.RelationshipGraphEdge;
import edu.pitt.sis.exp.colfusion.relationships.relationshipGraph.RelationshipGraphEdge.NodeInfo;
import edu.pitt.sis.exp.colfusion.relationships.relationshipGraph.RelationshipGraphNode;

public class RelationshipGraphBL {

	final Logger logger = LogManager.getLogger(RelationshipGraphBL.class.getName());
	
	/**
	 * Construct Relationship Graph where nodes represent tables and edges represent relationships
	 * @return
	 * @throws Exception
	 */
	public RelationshipGraph getRelationshipGraph() throws Exception {
		RelationshipGraph result = new RelationshipGraph();
		
		result.setNodes(getNodes());
		result.setEdges(getEdges());
		
		return result;
	}
	
	/**
	 * Edges represent relationships
	 * 
	 * @return
	 * @throws Exception
	 */
	private List<RelationshipGraphEdge> getEdges() throws Exception {
		RelationshipsManager relationshipMng = new RelationshipsManagerImpl();
		try {
			List<RelationshipGraphEdge> edges = new ArrayList<RelationshipGraphEdge>();
			
			List<ColfusionRelationships> relationships = relationshipMng.findAll(); //TODO FIXME: only need to find "valid" relationship (e.g. not deleted ones)
			
			//TODO FIXME this is wrong, need to fix, should use dao here
			StatonverdictsDAO dao = new StatonverdictsDAOImpl();
			Map<Integer, BigDecimal> confidenceMap = dao.getAvgConfidence();
			
			for (ColfusionRelationships relationship : relationships) {
				
				relationship = GeneralManagerImpl.initializeField(relationship, "colfusionSourceinfoBySid1");
				
				NodeInfo sidFrom = new NodeInfo(relationship.getColfusionSourceinfoBySid1().getSid(), 
						relationship.getColfusionSourceinfoBySid1().getTitle(), relationship.getTableName1());
				
				relationship = GeneralManagerImpl.initializeField(relationship, "colfusionSourceinfoBySid2");
				
				NodeInfo sidTo = new NodeInfo(relationship.getColfusionSourceinfoBySid2().getSid(), 
						relationship.getColfusionSourceinfoBySid2().getTitle(), relationship.getTableName2());
				
				RelationshipGraphEdge edge = new RelationshipGraphEdge(relationship.getRelId(), relationship.getName(), 
						confidenceMap.get(relationship.getRelId()).doubleValue(), sidFrom, sidTo);
				
				edges.add(edge);
			}
			
			return edges;
		} catch (Exception e) {
			String message = "Could not get all relationships from DB.";
			
			logger.error(message);
			throw e;
		}
	}

	/**
	 * Nodes represent stories.
	 * 
	 * @throws Exception
	 */
	private List<RelationshipGraphNode> getNodes() throws Exception {
		SourceInfoManager sourceInfoMng =  new SourceInfoManagerImpl();
		DNameInfoManager dNameInfoMng = new DNameInfoManagerImpl();
		
		//Just use queued as the status, we can add a paramaeter in the method if you want;
		String status = "queued";
		try {
			//List<ColfusionSourceinfo> allStories = sourceInfoMng.findAll(); //TODO FIXME: only need to get nodes that are not drafts (published to the public)
			List<ColfusionSourceinfo> allStories = sourceInfoMng.getSourceInfoByStatus(status);
			List<RelationshipGraphNode> nodes = new ArrayList<RelationshipGraphNode>(allStories.size());
			
			for (ColfusionSourceinfo story : allStories) {
				
				List<RelationKey> tableNames = sourceInfoMng.getTableNames(story.getSid());
				
				for (RelationKey table : tableNames) {
					List<DnameViewModel> allColumns = dNameInfoMng.getDnameListViewModel(story.getSid(), table.getTableName());
					
					RelationshipGraphNode node = new RelationshipGraphNode(story.getSid(), table.getTableName(), story.getTitle(), allColumns);
					nodes.add(node);
				}	
			}
			
			return nodes;
		} catch (Exception e) {
			String message = "Could not get all stories from DB.";
			
			logger.error(message);
			throw e;
		}
	}
}
