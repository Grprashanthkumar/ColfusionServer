/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationshipsColumnsDataMathingRatios;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationshipsColumnsDataMathingRatiosId;

/**
 * @author Evgeny
 *
 */
public interface RelationshipsColumnsDataMathingRatiosManager extends
		GeneralManager<ColfusionRelationshipsColumnsDataMathingRatios, ColfusionRelationshipsColumnsDataMathingRatiosId> {

	
	ColfusionRelationshipsColumnsDataMathingRatios findColfusionRelationshipsColumnsDataMathingRatios(ColfusionRelationshipsColumnsDataMathingRatiosId colfusionRelationshipsColumnsDataMathingRatiosId);
}
