/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.Relationship;
import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.RelationshipLink;
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
	
	@Expose private List<Relationship> relationships;
	
	@Expose private TwoJointTablesViewModel twoJointTables;
	
	public TwoTableJoinInputViewModel() {
		
	}
	
	public TwoTableJoinInputViewModel(final Table table1, final Table table2, 
			final TwoJointTablesViewModel twoJointTables, final List<Relationship> relationships) {
		this.table1 = table1;
		this.table2 = table2;
		setTwoJointTables(twoJointTables);
		this.setRelationships(relationships);
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
	public TwoJointTablesViewModel getTwoJointTables() {
		return twoJointTables;
	}

	/**
	 * @param twoJointTables the twoJointTables to set
	 */
	public void setTwoJointTables(final TwoJointTablesViewModel twoJointTables) {
		this.twoJointTables = twoJointTables;
	}

	/**
	 * @return the relationships
	 */
	public List<Relationship> getRelationships() {
		return relationships;
	}

	/**
	 * @param relationships the relationships to set
	 */
	public void setRelationships(final List<Relationship> relationships) {
		this.relationships = relationships;
	}
	
	public String getIdentifyingString() {
		StringBuilder strBuilding = new StringBuilder();
		strBuilding.append(twoJointTables.getSid1()).append("_").append(twoJointTables.getTableName1())
					.append("_").append(twoJointTables.getSid2()).append("_").append(twoJointTables.getTableName2()).append("_");
		
		for (Relationship relationship :relationships) {
			strBuilding.append(relationship.getRelId()).append("_");
			
			for (RelationshipLink relationshipLink : relationship.getLinks()) {
				strBuilding.append(relationshipLink.getFrom()).append("_").append(relationshipLink.getTo()).append("_");
			}
		}
		
		strBuilding.append(twoJointTables.getSimilarityThreshold());
		
		return strBuilding.toString();
	}
}
