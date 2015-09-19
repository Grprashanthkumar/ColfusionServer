/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll.similarityJoins;

import java.util.List;

import edu.pitt.sis.exp.colfusion.bll.similarityMeasures.FilterBase;
import edu.pitt.sis.exp.colfusion.bll.similarityMeasures.SimilarityDistanceMeasure;
import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.transformation.RelationshipTransformation;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.Table;

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
			List<RelationshipTransformation> transformationTable1, List<RelationshipTransformation> transformationTable2, double similarityThreshold);
}
