/**
 * 
 */
package edu.pitt.sis.exp.colfusion.similarityJoins;

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

	public NestedLoopSimilarityJoin(final SimilarityDistanceMeasure simDisMeasure, final FilterBase upperBoundFilter, final FilterBase lowerBoundFilter) {
		super(simDisMeasure, upperBoundFilter, lowerBoundFilter);
	}

	
	@Override
	public Table join(final Table table1, final Table table2, final RelationshipTransformation transformationTable1, 	final RelationshipTransformation transformationTable2,
			final double similarityThreshold) {
		
		Table result = new Table();
		
		for (Row rowTable1 : table1) {
			String value1 = rowTable1.getByTransformation(transformationTable1);
			
			for (Row rowTable2 : table2) {
				
				String value2 = rowTable2.getByTransformation(transformationTable2);
				
				if (this.upperBoundFilter != null && upperBoundFilter.calculate(value1, value2) >= similarityThreshold) {
					result.add(constructJointTuple(rowTable1, rowTable2));
				}
				else if (this.lowerBoundFilter != null && lowerBoundFilter.calculate(value1, value2) < similarityThreshold) {
					
				}
				else if (this.simDisMeasure != null && simDisMeasure.computeSimilarity(value1, value2) >= similarityThreshold) {
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

}
