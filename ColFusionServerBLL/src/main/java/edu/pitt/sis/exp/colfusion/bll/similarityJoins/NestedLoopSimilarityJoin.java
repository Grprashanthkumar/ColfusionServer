/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll.similarityJoins;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.similarityMeasures.FilterBase;
import edu.pitt.sis.exp.colfusion.bll.similarityMeasures.SimilarityDistanceMeasure;
import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.Relationship;
import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.RelationshipLink;
import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.transformation.RelationshipTransformation;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.Row;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.Table;

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
		
		if (table1 == null || table2 == null) {
			return null;
		}
		
		Table result = new Table();
		
		for (Row rowTable1 : table1.getRows()) {
			
			StringBuilder valueKeyRow1 = new StringBuilder();
			
			for (RelationshipTransformation relationshipTransformation : transformationsTable1) {
				valueKeyRow1.append(rowTable1.getByTransformation(relationshipTransformation));
			}
			
						
			for (Row rowTable2 : table2.getRows()) {
				
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
					
					result.getRows().add(constructJointTuple(rowTable1, rowTable2));
				}
				else if (this.lowerBoundFilter != null && lowerBoundFilter.calculate(valueKeyRow1.toString(), valueKeyRow2.toString()) < similarityThreshold) {
					
				}
				else if (this.simDisMeasure != null && simDisMeasure.computeSimilarity(valueKeyRow1.toString(), valueKeyRow2.toString()) >= similarityThreshold) {
					result.getRows().add(constructJointTuple(rowTable1, rowTable2));
				}
			}
		}
		
		return result;
	}


	private Row constructJointTuple(final Row rowTable1, final Row rowTable2) {
		Row result = new Row();
		result.getColumnGroups().addAll(rowTable1.getColumnGroups());
		result.getColumnGroups().addAll(rowTable2.getColumnGroups());
		
		return result;
	}


	public Table join(final Table table1, final Table table2,
			final List<Relationship> relationships, final double similarityThreshold) {
		
		//TODO FIXME only take one relationship in account for now
		
		Relationship relationship = relationships.get(0);
		
		List<RelationshipTransformation> transformationsTable1 = new ArrayList<RelationshipTransformation>();
		List<RelationshipTransformation> transformationsTable2 = new ArrayList<RelationshipTransformation>();
		
		setTransformationsForTables(table1, table2, relationship, transformationsTable1, transformationsTable2);
		
		return join(table1, table2, transformationsTable1, transformationsTable2, similarityThreshold);
	}


	private void setTransformationsForTables(final Table table1, final Table table2,
			final Relationship relationship,
			final List<RelationshipTransformation> transformationsTable1,
			final List<RelationshipTransformation> transformationsTable2) {
		
		//TODO FIXME not check for size so fail get(0) if empty
		
		//TODO FIXME one table can have more than one sid
		
		for (RelationshipLink relationshipLink : relationship.getLinks()) {
			
			if (table1.getRows().get(0).getColumnGroups().get(0).getSid() == relationship.getSidFrom()) {
				transformationsTable1.add(relationshipLink.getFrom());
				
				if (table2.getRows().get(0).getColumnGroups().get(0).getSid() != relationship.getSidTo()) {
					throw new RuntimeException("could not find tranformation for table 2");
				}
				
				transformationsTable2.add(relationshipLink.getTo());
			}
			else if (table1.getRows().get(0).getColumnGroups().get(0).getSid() == relationship.getSidTo()) {
				transformationsTable1.add(relationshipLink.getTo());
				
				if (table2.getRows().get(0).getColumnGroups().get(0).getSid() != relationship.getSidFrom()) {
					throw new RuntimeException("could not find tranformation for table 2");
				}
				
				transformationsTable2.add(relationshipLink.getFrom());
			}
		}
		
	}


//	public Table join(final Table allTuplesFrom, final Table allTuplesTo,
//			final List<RelationshipLink> links, final double similarityThreshold) {
//		throw new RuntimeException("Not implemented yet");
//	}

}
