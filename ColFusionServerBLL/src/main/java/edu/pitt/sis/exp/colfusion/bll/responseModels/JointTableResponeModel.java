package edu.pitt.sis.exp.colfusion.bll.responseModels;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.TwoJointTablesViewModel;
import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

public class JointTableResponeModel extends GeneralResponseImpl implements Gsonazable{
	@Expose private TwoJointTablesViewModel payload = new TwoJointTablesViewModel();

	/**
	 * @return the payload
	 */
	public TwoJointTablesViewModel getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(final TwoJointTablesViewModel payload) {
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
