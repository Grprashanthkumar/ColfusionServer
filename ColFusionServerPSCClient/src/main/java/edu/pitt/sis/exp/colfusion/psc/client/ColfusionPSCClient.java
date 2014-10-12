package edu.pitt.sis.exp.colfusion.psc.client;

import edu.pitt.sis.exp.colfusion.bll.BasicTableBL;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.Table;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.TwoJointTablesViewModel;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableByRelationshipsResponeModel;

public class ColfusionPSCClient {

	public static void main(final String[] args) {
		ColfusionPSCClient client = new ColfusionPSCClient();
		
		TwoJointTablesViewModel twoTables = new TwoJointTablesViewModel();
		
		client.join(twoTables);

	}

	private void join(final TwoJointTablesViewModel twoTables) {
		BasicTableBL tablBL = new BasicTableBL();
		
		JointTableByRelationshipsResponeModel tableResponse1 = tablBL.getTableDataBySidAndName(twoTables.getSid1(), twoTables.getTableName1());
		Table table1 = tableResponse1.getPayload().getJointTable();
		
		JointTableByRelationshipsResponeModel tableResponse2 = tablBL.getTableDataBySidAndName(twoTables.getSid2(), twoTables.getTableName2());
		Table table2 = tableResponse1.getPayload().getJointTable();
	}

}
