/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.processes;

import java.io.Serializable;

import javax.persistence.Transient;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.process.ProcessBase;

/**
 * @author Evgeny
 *
 */
public class TestProcess extends ProcessBase {

	transient Logger logger = LogManager.getLogger(TestProcess.class.getName());
	
	private int _sid;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8932832409517582844L;

		@Override
	protected Runnable getRunnable() {
		return this;
	}

	/**
	 * @return the _sid
	 */
	public int getSid() {
		return _sid;
	}

	/**
	 * @param _sid the _sid to set
	 */
	public void setSid(int _sid) {
		this._sid = _sid;
	}

	@Override
	public void execute() throws Exception {
		logger.info("running TestProcess");
		
		Thread.sleep(1000);
		
		logger.info("Finished running TestProcess");
	}

}
