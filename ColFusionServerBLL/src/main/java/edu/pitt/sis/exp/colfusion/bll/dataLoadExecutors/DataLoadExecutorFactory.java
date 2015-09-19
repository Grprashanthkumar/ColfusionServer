/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll.dataLoadExecutors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.utils.DataSourceTypes;

/**
 * @author Evgeny
 *
 */
public class DataLoadExecutorFactory {
	final static Logger logger = LogManager.getLogger(DataLoadExecutorFactory.class.getName());
	
	public static DataLoadExecutor getDataLoadExecutor(final DataSourceTypes sourceType) throws Exception {
		switch (sourceType) {
		case DATA_FILE:
			return new DataLoadExecutorKTRImpl();
			
		default:
			
			logger.error("getDataLoadExecutor failed: executor not found", sourceType);
			
			throw new IllegalArgumentException("Executor for data source type" + sourceType.getValue() + " not found");
		}
	}
}
