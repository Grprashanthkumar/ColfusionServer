package edu.pitt.sis.exp.colfusion.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GeneralResponseModel {
	public boolean IsSuccessful;
    public String Message;
	public List<Object> PayLoad;
	
	public GeneralResponseModel() {
		PayLoad = new ArrayList<Object>();
	}
}






