package edu.pitt.sis.exp.colfusion.responseModels;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.JoinTablesByRelationshipsViewModel;
import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

public class JointTableByRelationshipsResponeModel extends GeneralResponseImpl implements Gsonazable{
	@Expose private JoinTablesByRelationshipsViewModel payload = new JoinTablesByRelationshipsViewModel();

	/**
	 * @return the payload
	 */
	public JoinTablesByRelationshipsViewModel getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(final JoinTablesByRelationshipsViewModel payload) {
		this.payload = payload;
	}

	@Override
	public String toJson() {
		return Gsonizer.toJson(this, false);
	}

	@Override
	public void fromJson() {
		// TODO Auto-generated method stub
		
	}
}
