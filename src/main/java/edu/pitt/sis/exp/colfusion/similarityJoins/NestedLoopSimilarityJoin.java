/**
 * 
 */
package edu.pitt.sis.exp.colfusion.similarityJoins;

import edu.pitt.sis.exp.colfusion.relationships.transformation.RelationshipTransformation;
import edu.pitt.sis.exp.colfusion.similarityMeasures.FilterBase;
import edu.pitt.sis.exp.colfusion.similarityMeasures.SimilarityDistanceMeasure;

/**
 * @author Evgeny
 *
 */
public class NestedLoopSimilarityJoin extends SimilarityJoinBase {

	public NestedLoopSimilarityJoin(final SimilarityDistanceMeasure simDisMeasure, final FilterBase filter) {
		super(simDisMeasure, filter);
	}

	
	@Override
	public ColfusionResultSet join(final ColfusionResultSet table1,	final ColfusionResultSet table2,
			final RelationshipTransformation transformationTable1, 	final RelationshipTransformation transformationTable2,
			final double similarityThreshold) {
		//TODO:IMPLEMENT
		
		return new ColfusionResultSet(null);
	}

}
