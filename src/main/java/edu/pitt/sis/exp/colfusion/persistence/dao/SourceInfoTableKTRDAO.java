/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

import java.util.ArrayList;

import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoTableKtr;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoTableKtrId;

/**
 * @author Evgeny
 *
 */
public interface SourceInfoTableKTRDAO extends GenericDAO<ColfusionSourceinfoTableKtr, ColfusionSourceinfoTableKtrId> {

	/**
	 * Gets locations of KTR files associated with given story by sid.
	 * @param sid is of the story.
	 * @return array of records from CourseInfoTableKTR table.
	 * @throws Exception 
	 */
	ArrayList<ColfusionSourceinfoTableKtr> getKTRLocationsBySid(int sid) throws Exception;

}
