/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.Table;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class TwoTableJoinInputViewModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Expose private Table table1;
	@Expose private Table table2;
	
	@Expose private TwoJointTablesViewModel twoJointTables;
	
	public TwoTableJoinInputViewModel() {
		
	}
	
	public TwoTableJoinInputViewModel(final Table table1, final Table table2, final TwoJointTablesViewModel twoJointTables) {
		this.table1 = table1;
		this.table2 = table2;
		setTwoJointTables(twoJointTables);
	}
	
	/**
	 * @return the jointTable
	 */
	public Table getTable1() {
		return table1;
	}

	/**
	 * @param jointTable the jointTable to set
	 */
	public void setTable1(final Table table1) {
		this.table1 = table1;
	}
	
	/**
	 * @return the jointTable
	 */
	public Table getTable2() {
		return table2;
	}

	/**
	 * @param jointTable the jointTable to set
	 */
	public void setTable2(final Table table2) {
		this.table2 = table2;
	}

	/**
	 * @return the twoJointTables
	 */
	private TwoJointTablesViewModel getTwoJointTables() {
		return twoJointTables;
	}

	/**
	 * @param twoJointTables the twoJointTables to set
	 */
	private void setTwoJointTables(final TwoJointTablesViewModel twoJointTables) {
		this.twoJointTables = twoJointTables;
	}
}
