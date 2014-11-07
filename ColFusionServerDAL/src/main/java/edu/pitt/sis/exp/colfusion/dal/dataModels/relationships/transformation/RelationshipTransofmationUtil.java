package edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.ColumnMetadata;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.RelationKey;
import edu.pitt.sis.exp.colfusion.dal.managers.DNameInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.DNameInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionDnameinfo;

public class RelationshipTransofmationUtil {

	static Logger logger = LogManager.getLogger(RelationshipTransofmationUtil.class.getName());
	
	private static final Pattern pattern = Pattern.compile("(\\()(\\d+)(\\))");
	
	public static Matcher match(final String transformationInCids) {
		return pattern.matcher(transformationInCids);
	}
	
	/**
	 * Extracts column ids from column transformation strings (column ids are supposed to be wrapped in cid(...));
	 * @param transformation the transformation string.
	 * @return list of ids extracted from the transformation string.
	 */
	public static List<Integer> extractCids(final String transformationInCids) {
				
		Matcher matcher = RelationshipTransofmationUtil.match(transformationInCids);
		
		List<Integer> result = new ArrayList<>();
		
		try {
			while (matcher.find()) {
				result.add(Integer.parseInt(matcher.group(2)));
			}
			
			return result;
		}
		catch(Exception e) {
			logger.error(String.format("Could not extract cids from this string: %s", transformationInCids));
			throw e;
		}	
	}

	public static RelationshipTransformation makeRelationshipTransformation(
			final int relId, final String transformationInCids) throws Exception {
		
		List<Integer> cids = RelationshipTransofmationUtil.extractCids(transformationInCids);
		
		DNameInfoManager columnMng = new DNameInfoManagerImpl();
		
		List<ColumnMetadata> columnsMetadata = new ArrayList<ColumnMetadata>();
		
		Integer sid = null;
		RelationKey relationKey = null;
		
		for (Integer cid : cids) {
			
			ColfusionDnameinfo column = columnMng.findByID(cid);
			
			if (sid == null) {
				sid = column.getColfusionSourceinfo().getSid();
			}
			else if (sid != column.getColfusionSourceinfo().getSid()) {
				String message = String.format("makeRelationshipTransformation FIALED: encounter not "
						+ "unique sid (%d and %d) for relid %d and transformation '%s'", 
						sid, column.getColfusionSourceinfo().getSid(), relId, transformationInCids);
				
				logger.error(message);
				throw new RuntimeException(message);
			}
			
			if (relationKey == null) {
				relationKey = new RelationKey(column.getColfusionColumnTableInfo().getTableName(), column.getColfusionColumnTableInfo().getDbTableName());
			}
			else if (!relationKey.getDbTableName().equals(column.getColfusionColumnTableInfo().getDbTableName())) {
				String message = String.format("makeRelationshipTransformation FIALED: encounter not "
						+ "unique table name (%s and %s) for relid %d and transformation '%s'", 
						relationKey.getDbTableName(), column.getColfusionColumnTableInfo().getDbTableName(), relId, transformationInCids);
				
				logger.error(message);
				throw new RuntimeException(message);
			}
			
			ColumnMetadata columnMetadata = new ColumnMetadata(cid, column.getDnameOriginalName(), 
					column.getDnameChosen(), column.getDnameValueDescription(), 
					column.getDnameValueUnit(), column.getDnameValueType(), 
					column.getDnameValueFormat(), column.getMissingValue());
			
			columnsMetadata.add(columnMetadata);
		}
		
		return new RelationshipTransformation(transformationInCids, sid, relationKey, columnsMetadata);
	}
}
