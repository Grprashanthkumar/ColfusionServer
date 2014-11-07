package edu.pitt.sis.exp.colfusion.dal.managers;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionColumnTableInfo;

public interface ColumnTableInfoManager extends GeneralManager<ColfusionColumnTableInfo, Integer> {
	
	//TODO FIXME: this is workaround until we fix our database design
	public ColfusionColumnTableInfo findBySidAndOriginalTableName(final int sid,
			final String tableName);
}
