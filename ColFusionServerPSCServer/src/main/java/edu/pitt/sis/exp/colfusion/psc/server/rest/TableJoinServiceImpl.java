/**
 * 
 */
package edu.pitt.sis.exp.colfusion.psc.server.rest;

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
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.Table;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.TwoJointTablesViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.TwoTableJoinInputViewModel;
import edu.pitt.sis.exp.colfusion.psc.server.util.ServerType;
import edu.pitt.sis.exp.colfusion.psc.server.util.Utils;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableByRelationshipsResponeModel;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

/**
 * @author Evgeny
 *
 */
@Path("TableJoin/")
public class TableJoinServiceImpl implements TableJoinService {

	private static final Logger logger = LogManager.getLogger(TableJoinServiceImpl.class.getName());
	
	@Override
	public Response joinTables(final String twoJointTables) {
		
		logger.info("Got request");
		
		TwoTableJoinInputViewModel model = Gsonizer.fromJson(twoJointTables, TwoTableJoinInputViewModel.class);
		
		return Response.status(200).entity("test2").build();
	}

	@Override
	public Response joinTables(final int sid1, final String tableName1, final int sid2,
			final String tableName2, final double similarityThreshold) {
		
		logger.info(String.format("Got: %d %s %d %s %f", sid1, tableName1, sid2, tableName2, similarityThreshold));
		
		BasicTableBL tablBL = new BasicTableBL();
		
		JointTableByRelationshipsResponeModel tableResponse1 = tablBL.getTableDataBySidAndName(sid1, tableName1);
		Table table1 = tableResponse1.getPayload().getJointTable();
		
		JointTableByRelationshipsResponeModel tableResponse2 = tablBL.getTableDataBySidAndName(sid2, tableName2);
		Table table2 = tableResponse2.getPayload().getJointTable();
		
		TwoJointTablesViewModel twoJointTables = new TwoJointTablesViewModel(sid1, tableName1, sid2, tableName2, similarityThreshold, null);
		
		TwoTableJoinInputViewModel twoTables = new TwoTableJoinInputViewModel(table1,  table2, twoJointTables);
		
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
