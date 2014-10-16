/**
 * 
 */
package edu.pitt.sis.exp.colfusion.psc.server.rest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import edu.pitt.sis.exp.colfusion.bll.BasicTableBL;
import edu.pitt.sis.exp.colfusion.bll.JoinTablesBL;
import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.Relationship;
import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.RelationshipLink;
import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.transformation.RelationshipTransofmationUtil;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.Table;
import edu.pitt.sis.exp.colfusion.dal.managers.RelationshipsManager;
import edu.pitt.sis.exp.colfusion.dal.managers.RelationshipsManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationships;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationshipsColumns;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.TwoJointTablesViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.TwoTableJoinInputViewModel;
import edu.pitt.sis.exp.colfusion.psc.server.util.ServerType;
import edu.pitt.sis.exp.colfusion.psc.server.util.Utils;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableByRelationshipsResponeModel;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;
import edu.pitt.sis.exp.colfusion.utils.IOUtils;

/**
 * @author Evgeny
 *
 */
@Path("TableJoin/")
public class TableJoinServiceImpl implements TableJoinService {

	private static final Logger logger = LogManager.getLogger(TableJoinServiceImpl.class.getName());
	
	@Override
	public Response joinTables(final String twoJointTables) throws FileNotFoundException, IOException {
		
		logger.info("Got request");
		
		TwoTableJoinInputViewModel model = Gsonizer.fromJson(twoJointTables, TwoTableJoinInputViewModel.class);
		
		JoinTablesBL joinBL = new JoinTablesBL();
		
		for (double i = 0; i<= 1; i += 0.1) {
			model.getTwoJointTables().setSimilarityThreshold(i);
			TwoJointTablesViewModel result = joinBL.joinTables(model);
			
			TwoTableJoinInputViewModel modelToSave = new TwoTableJoinInputViewModel();
			modelToSave.setRelationships(model.getRelationships());
			modelToSave.setTwoJointTables(result);
			
			IOUtils.writeToFile(Gsonizer.toJson(modelToSave, true), model.getIdentifyingString());
		}
		
		return Response.status(200).entity("test2").build();
	}

	@Override
	public Response joinTables(final int sid1, final String tableName1, final int sid2,
			final String tableName2, final double similarityThreshold) throws Exception {
		
		logger.info(String.format("Got: %d %s %d %s %f", sid1, tableName1, sid2, tableName2, similarityThreshold));
		
		BasicTableBL tablBL = new BasicTableBL();
		
		JointTableByRelationshipsResponeModel tableResponse1 = tablBL.getTableDataBySidAndName(sid1, tableName1);
		Table table1 = tableResponse1.getPayload().getJointTable();
		
		JointTableByRelationshipsResponeModel tableResponse2 = tablBL.getTableDataBySidAndName(sid2, tableName2);
		Table table2 = tableResponse2.getPayload().getJointTable();
		
		TwoJointTablesViewModel twoJointTables = new TwoJointTablesViewModel(sid1, tableName1, sid2, tableName2, similarityThreshold, null);
		
		RelationshipsManager relMng = new RelationshipsManagerImpl();
		List<ColfusionRelationships> dbRelationships = relMng.findRelationshipsBySid(sid1, sid2);
		
		List<Relationship> relationships = new ArrayList<Relationship>();
		List<RelationshipLink> links = new ArrayList<RelationshipLink>();
		
		for (ColfusionRelationships dbRelationship : dbRelationships) {
			
			for (Object relColumnOBj : dbRelationship.getColfusionRelationshipsColumnses().toArray()) {
				ColfusionRelationshipsColumns colfusionLink = (ColfusionRelationshipsColumns) relColumnOBj;
				links.add(new RelationshipLink(RelationshipTransofmationUtil.makeRelationshipTransformation(dbRelationship.getRelId(), colfusionLink.getId().getClFrom()), 
						RelationshipTransofmationUtil.makeRelationshipTransformation(dbRelationship.getRelId(), colfusionLink.getId().getClTo())));
			}
			
			Relationship relationship = new Relationship(dbRelationship.getRelId(), dbRelationship.getColfusionSourceinfoBySid1().getSid(), dbRelationship.getTableName1(), 
					dbRelationship.getColfusionSourceinfoBySid2().getSid(), dbRelationship.getTableName2(), links);
			
			relationships.add(relationship);
		}
		
		TwoTableJoinInputViewModel twoTables = new TwoTableJoinInputViewModel(table1,  table2, twoJointTables, relationships);
		
		String twoTablesStr = Gsonizer.toJson(twoTables, true);
		
		ClientConfig clientConfit = new DefaultClientConfig();
		clientConfit.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfit);

		String resourceURL = String.format("%s/TableJoin/join", Utils.getBaseRestURL(ServerType.JOINER));
		
		WebResource webResource = client.resource(resourceURL);
		   
		ClientResponse response = webResource.
				type(MediaType.APPLICATION_JSON).post(ClientResponse.class, twoTablesStr);
		
		return Response.status(200).entity("test").build();
	}
}
