package edu.pitt.sis.exp.colfusion.responseModels;

import java.util.ArrayList;
import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.DatasetVariableViewModel;

public class ColumnMetadataResponse extends GeneralResponseImpl {
  
  private List<DatasetVariableViewModel> payload = new ArrayList<DatasetVariableViewModel>();
  
  public List<DatasetVariableViewModel> getPayload() {
		return this.payload;
	}
  
  public void setPayload(final List<DatasetVariableViewModel> payload) {
		this.payload = payload; 
	}
}
