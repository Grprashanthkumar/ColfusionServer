package edu.pitt.sis.exp.colfusion.responseModels;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.RelationshipsViewModel;
import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;


public class RelationshipsResponseModel extends GeneralResponseImpl implements Gsonazable{
	@Expose private List<RelationshipsViewModel> data = new ArrayList<RelationshipsViewModel>();
	@Expose private ControlModel Control = new ControlModel();
	

	/**
	 * @return the payload
	 */
	public List<RelationshipsViewModel> getPayload() {
		return data;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(final List<RelationshipsViewModel> payload) {
		this.data = payload;
	}

	public ControlModel getControl() {
		return Control;
	}

	public void setControl(final ControlModel control) {
		Control = control;
	}

	@Override
	public String toJson() {
		return Gsonizer.toJson(this, true);
	}

	@Override
	public void fromJson() {
		// TODO Auto-generated method stub
		
	}
	
	public class ControlModel{
		@Expose private int perPage;
		@Expose private int totalPage;
		@Expose private int pageNO;
		@Expose String cols;
		public int getPerPage() {
			return perPage;
		}
		public void setPerPage(final int perPage) {
			this.perPage = perPage;
		}
		public int getTotalPage() {
			return totalPage;
		}
		public void setTotalPage(final int totalPage) {
			this.totalPage = totalPage;
		}
		public int getPageNO() {
			return pageNO;
		}
		public void setPageNO(final int pageNO) {
			this.pageNO = pageNO;
		}
		public String getCols() {
			return cols;
		}
		public void setCols(final String cols) {
			this.cols = cols;
		}

	}
	
}
