package edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

@XmlRootElement
public class Cell implements Gsonazable{
	
	private static final Logger logger = LogManager.getLogger(Cell.class.getName());
	
	private Serializable value;
	
//	static {
//		Gsonizer.registerTypeAdapter(Cell.class, new CellSerializer());
//		Gsonizer.registerTypeAdapter(Cell.class, new CellDeserializer());
//		
//		logger.info("Registered CellSerializer and CellDeserializer()");
//	}
	
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
		return value == null ? null : value.toString();
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