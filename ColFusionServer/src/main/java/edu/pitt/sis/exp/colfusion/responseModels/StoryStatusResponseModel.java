package edu.pitt.sis.exp.colfusion.responseModels;

import java.util.ArrayList;
import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionExecuteinfo;
import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;


/**
 * @author Weichuan Hong
 *
 */
public class StoryStatusResponseModel extends GeneralResponseImpl implements Gsonazable{
	private List<ColfusionExecuteinfo> payload = new ArrayList<ColfusionExecuteinfo>();

	/**
	 * @return the payload
	 */
	public List<ColfusionExecuteinfo> getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(List<ColfusionExecuteinfo> payload) {
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
