package edu.pitt.sis.exp.colfusion.dal.managers;

import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.managers.PSCSourceInfoTableManagerImpl.SourceInfoAndTable;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionPscSourceinfoTable;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionPscSourceinfoTableId;

public interface PSCSourceInfoTableManager extends GeneralManager<ColfusionPscSourceinfoTable, ColfusionPscSourceinfoTableId> {

	/**
	 * Finds pairs of sourceInfo id (sid) and table name that are not replicated yet.
	 * @return
	 */
	List<SourceInfoAndTable> findAllNotReplicated();

}
