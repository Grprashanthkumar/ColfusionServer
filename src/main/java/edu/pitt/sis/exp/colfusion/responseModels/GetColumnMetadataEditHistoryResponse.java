package edu.pitt.sis.exp.colfusion.responseModels;

import java.util.ArrayList;
import java.util.List;

import edu.pitt.sis.exp.colfusion.viewmodels.StoryMetadataHistoryLogRecordViewModel;


public class GetColumnMetadataEditHistoryResponse extends GeneralResponseImpl {
  
  private List<StoryMetadataHistoryLogRecordViewModel> payload = new ArrayList<StoryMetadataHistoryLogRecordViewModel>();
  
  public List<StoryMetadataHistoryLogRecordViewModel> getPayload() {
		return this.payload;
	}
  
  public void setPayload(final List<StoryMetadataHistoryLogRecordViewModel> payload) {
		this.payload = payload; 
	}
}
