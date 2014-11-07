package edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel;

/**
 * This class is used for a super bad workaround that need to be fixed as soon as possible. The problem is in the design of 
 * metadata database. In particularly how columnTableInto table is designed.
 * @author Evgeny
 *
 */
public class RelationKey {
	
	private final String tableName;
	private final String dbTableName;
	
	
	public RelationKey(final String tableNameP, final String dbTableNameP) {
		tableName = tableNameP;
		dbTableName = dbTableNameP;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @return the dbTableName
	 */
	public String getDbTableName() {
		return dbTableName;
	}
	
	@Override
	public boolean equals(final Object other) {
		if (other instanceof RelationKey) {
			RelationKey otherRelationKey = (RelationKey) other;
			
			return this.getTableName().equals(otherRelationKey.getTableName()) && this.getDbTableName().equals(otherRelationKey.getDbTableName());
		}
		
		return false;
	}
}
