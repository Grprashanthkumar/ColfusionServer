/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.managers;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCanvases;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUsers;

/**
 * @author xiaotingli
 *
 */
public interface CanvasesManager extends GeneralManager<ColfusionCanvases, Integer> {

	public ColfusionCanvases createNewCanvas(ColfusionUsers canvasOwner, String name);
}
