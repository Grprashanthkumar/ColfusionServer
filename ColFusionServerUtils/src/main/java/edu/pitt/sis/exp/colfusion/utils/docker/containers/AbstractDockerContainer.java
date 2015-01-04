/**
 * 
 */
package edu.pitt.sis.exp.colfusion.utils.docker.containers;

/**
 * @author Evgeny
 *
 */
public abstract class AbstractDockerContainer {
	protected final String containerId;
	
	public AbstractDockerContainer(final String containerId) {
		this.containerId = containerId;
	}
}
