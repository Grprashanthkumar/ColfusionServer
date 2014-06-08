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
public abstract class SimilarityJoinBase {
	
	protected final FilterBase filter;
	
	protected final SimilarityDistanceMeasure simDisMeasure;
	
	protected SimilarityJoinBase(final SimilarityDistanceMeasure simDisMeasure, final FilterBase filter) {
		this.simDisMeasure = simDisMeasure;
		this.filter = filter;
	}
	
	
	public abstract ColfusionResultSet join(final ColfusionResultSet table1, final ColfusionResultSet table2, 
			RelationshipTransformation transformationTable1, RelationshipTransformation transformationTable2, double similarityThreshold);
}
