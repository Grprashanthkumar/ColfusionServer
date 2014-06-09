package edu.pitt.sis.exp.colfusion.dataModels.tableDataModel;

public class Column {
	
	//private final int cid;
	private final String originalName;
	//private final String chosenName;
	private final Cell cell;
	
	public Column(final String originalName, final Cell cell) {
	//	this.cid = cid;
		this.originalName = originalName;
	//	this.chosenName = chosenName;
		this.cell = cell;
	}

	/**
	 * @return the cid
	 */
//	public int getCid() {
//		return cid;
//	}

	/**
	 * @return the originalName
	 */
	public String getOriginalName() {
		return originalName;
	}

	/**
	 * @return the chosenName
	 */
//	public String getChosenName() {
//		return chosenName;
//	}
	
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
	public String toString() {
		return String.format("Original Name: %s, Cell Value: %s", originalName, getCell().toString()) ;
	}
}
