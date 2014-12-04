package edu.pitt.sis.exp.colfusion.psc.server.business;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.process.ProcessBase;

public class ReplicationProcess extends ProcessBase {

	@Expose int sourceInfoId;
	@Expose String tableName;
	
	public ReplicationProcess(final int sourceInfoId, final String tableName) {
		this.sourceInfoId = sourceInfoId;
		this.tableName = tableName;
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Runnable getRunnable() {
		return this;
	}

}
