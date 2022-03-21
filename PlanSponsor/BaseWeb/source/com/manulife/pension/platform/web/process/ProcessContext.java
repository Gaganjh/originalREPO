package com.manulife.pension.platform.web.process;

import java.io.Serializable;

/**
 * The context information for one process flow
 * @author guweigu
 *
 */
abstract public class ProcessContext implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1;
	private ProcessState currentState;

	/**
	 * Returns the start state of this process
	 * 
	 * @return
	 */
	abstract public ProcessState getStartState();

	
	/**
	 * Get the current state
	 * @return
	 */
	public ProcessState getCurrentState() {
		return currentState;
	}

	/**
	 * Set the current state
	 * @param currentState
	 */
	public void setCurrentState(ProcessState currentState) {
		this.currentState = currentState;
	}
}
