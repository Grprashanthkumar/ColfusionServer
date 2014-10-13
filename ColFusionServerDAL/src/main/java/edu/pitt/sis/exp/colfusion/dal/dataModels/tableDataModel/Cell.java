package edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

@XmlRootElement
public class Cell implements Gsonazable{
	
	@Expose private Serializable value;
	
	public Cell() {
		
	}
	
	public Cell(final Serializable value) {
		this.value = value;
	}
	
	public Serializable getValue() {
		return value;
	}
	
	public void setValue(final Serializable value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value.toString();
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
