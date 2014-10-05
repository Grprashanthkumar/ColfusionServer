/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.managers;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationshipsColumnsDataMathingRatios;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationshipsColumnsDataMathingRatiosId;

/**
 * @author Evgeny
 *
 */
public interface RelationshipsColumnsDataMathingRatiosManager extends
		GeneralManager<ColfusionRelationshipsColumnsDataMathingRatios, ColfusionRelationshipsColumnsDataMathingRatiosId> {

	
	ColfusionRelationshipsColumnsDataMathingRatios findColfusionRelationshipsColumnsDataMathingRatios(ColfusionRelationshipsColumnsDataMathingRatiosId colfusionRelationshipsColumnsDataMathingRatiosId);
}
