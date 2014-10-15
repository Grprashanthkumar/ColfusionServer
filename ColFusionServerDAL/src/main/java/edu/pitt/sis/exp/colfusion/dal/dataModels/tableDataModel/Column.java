package edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

@XmlRootElement
public class Column implements Gsonazable {
	
	//TODO:get rid of originalName
	@Expose private String originalName;
	
	@Expose private ColumnMetadata metadata;
	
	@Expose private Cell cell;
	
	public Column() {
		
	}
	
	public Column(final String originalName, final Cell cell) {
		this.originalName = originalName;
		this.cell = cell;
	}

	/**
	 * @return the originalName
	 */
	public String getOriginalName() {
		return originalName;
	}
	
	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (o == null || !(o instanceof Column)) {
			return false;
		}
		
		Column cp = Column.class.cast(o);
		return originalName.equals(cp.getOriginalName());
	}
	
	@Override
	public int hashCode()
	{
		return originalName.hashCode();
	}

	/**
	 * @return the cell
	 */
	public Cell getCell() {
		return cell;
	}

	@Override
	public String toJson() {
		return Gsonizer.toJson(this, false);
	}

	@Override
	public void fromJson() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return String.format("Original Name: %s, Cell Value: %s", originalName, getCell().toString()) ;
	}

	/**
	 * @return the metadata
	 */
	private ColumnMetadata getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata the metadata to set
	 */
	private void setMetadata(final ColumnMetadata metadata) {
		this.metadata = metadata;
	}
}
