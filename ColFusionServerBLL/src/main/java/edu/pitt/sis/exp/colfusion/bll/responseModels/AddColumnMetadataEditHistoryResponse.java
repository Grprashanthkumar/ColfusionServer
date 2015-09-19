package edu.pitt.sis.exp.colfusion.bll.responseModels;

public class AddColumnMetadataEditHistoryResponse extends GeneralResponseImpl {
 private String payload;
 public void setPayload(final String a){
	 this.payload=a;
 }
 public String getPayload(){
	 return this.payload;
 }
}
