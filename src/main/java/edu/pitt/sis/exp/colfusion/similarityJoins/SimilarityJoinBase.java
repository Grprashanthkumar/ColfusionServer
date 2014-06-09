/**
 * 
 */
package edu.pitt.sis.exp.colfusion.similarityJoins;

import edu.pitt.sis.exp.colfusion.dataModels.tableDataModel.Table;
import edu.pitt.sis.exp.colfusion.relationships.transformation.RelationshipTransformation;
import edu.pitt.sis.exp.colfusion.similarityMeasures.FilterBase;
import edu.pitt.sis.exp.colfusion.similarityMeasures.SimilarityDistanceMeasure;

/**
 * @author Evgeny
 *
 */
public abstract class SimilarityJoinBase {
	
	protected final FilterBase upperBoundFilter;
	protected final FilterBase lowerBoundFilter;
	
	protected final SimilarityDistanceMeasure simDisMeasure;
	
	protected SimilarityJoinBase(final SimilarityDistanceMeasure simDisMeasure, final FilterBase upperBoundFilter, final FilterBase lowerBoundFilter) {
		this.simDisMeasure = simDisMeasure;
		this.upperBoundFilter = upperBoundFilter;
		this.lowerBoundFilter = lowerBoundFilter;
	}
	
	
	public abstract Table join(final Table table1, final Table table2, 
			RelationshipTransformation transformationTable1, RelationshipTransformation transformationTable2, double similarityThreshold);
}
