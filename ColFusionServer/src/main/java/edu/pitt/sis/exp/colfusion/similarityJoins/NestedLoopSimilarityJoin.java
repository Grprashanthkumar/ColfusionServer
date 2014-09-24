/**
 * 
 */
package edu.pitt.sis.exp.colfusion.similarityJoins;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dataModels.tableDataModel.Row;
import edu.pitt.sis.exp.colfusion.dataModels.tableDataModel.Table;
import edu.pitt.sis.exp.colfusion.relationships.transformation.RelationshipTransformation;
import edu.pitt.sis.exp.colfusion.similarityMeasures.FilterBase;
import edu.pitt.sis.exp.colfusion.similarityMeasures.SimilarityDistanceMeasure;

/**
 * @author Evgeny
 *
 */
public class NestedLoopSimilarityJoin extends SimilarityJoinBase {

	Logger logger = LogManager.getLogger(NestedLoopSimilarityJoin.class.getName());
	
	public NestedLoopSimilarityJoin(final SimilarityDistanceMeasure simDisMeasure, final FilterBase upperBoundFilter, final FilterBase lowerBoundFilter) {
		super(simDisMeasure, upperBoundFilter, lowerBoundFilter);
	}

	
	@Override
	public Table join(final Table table1, final Table table2, final List<RelationshipTransformation> transformationsTable1, 
			final List<RelationshipTransformation> transformationsTable2,
			final double similarityThreshold) {
		
		Table result = new Table();
		
		for (Row rowTable1 : table1) {
			
			StringBuilder valueKeyRow1 = new StringBuilder();
			
			for (RelationshipTransformation relationshipTransformation : transformationsTable1) {
				valueKeyRow1.append(rowTable1.getByTransformation(relationshipTransformation));
			}
			
						
			for (Row rowTable2 : table2) {
				
				StringBuilder valueKeyRow2 = new StringBuilder();
				
				for (RelationshipTransformation relationshipTransformation : transformationsTable2) {
					valueKeyRow2.append(rowTable2.getByTransformation(relationshipTransformation));
				}
				
				
//				
//				if (valueKeyRow1.size() != valueKeyRow2.size()) {
//					String msg = String.format("Join two tables FAILED because value key rows for two tables have different size (%d != %d)", valueKeyRow1.size(), valueKeyRow2.size());
//					
//					logger.error(msg);
//					//TODO: should not be runtime exception
//					throw new RuntimeException(msg);
//				}
				
				if (this.upperBoundFilter != null && upperBoundFilter.calculate(valueKeyRow1.toString(), valueKeyRow2.toString()) >= similarityThreshold) {
					
					result.add(constructJointTuple(rowTable1, rowTable2));
				}
				else if (this.lowerBoundFilter != null && lowerBoundFilter.calculate(valueKeyRow1.toString(), valueKeyRow2.toString()) < similarityThreshold) {
					
				}
				else if (this.simDisMeasure != null && simDisMeasure.computeSimilarity(valueKeyRow1.toString(), valueKeyRow2.toString()) >= similarityThreshold) {
					result.add(constructJointTuple(rowTable1, rowTable2));
				}
			}
		}
		
		return result;
	}


	private Row constructJointTuple(final Row rowTable1, final Row rowTable2) {
		Row result = new Row();
		result.addAll(rowTable1);
		result.addAll(rowTable2);
		
		return result;
	}


//	public Table join(final Table allTuplesFrom, final Table allTuplesTo,
//			final List<RelationshipLink> links, final double similarityThreshold) {
//		throw new RuntimeException("Not implemented yet");
//	}

}
