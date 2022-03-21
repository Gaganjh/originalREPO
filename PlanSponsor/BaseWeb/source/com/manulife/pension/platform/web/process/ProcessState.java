package com.manulife.pension.platform.web.process;

import java.io.Serializable;

/**
 * A process state within on process flow
 * 
 * @author guweigu
 *
 */
public interface ProcessState extends Serializable {
	/**
	 * The id of the state
	 * @return
	 */
	String getId();
	
	/**
	 * Next state based on the result
	 * @param result
	 * @return
	 */
	ProcessState getNext(String result);
	
	
	/**
	 * Returns whether the process has been ended at this state
	 * @return
	 */
	boolean hasProcessEnded();
	
	/**
	 * The corresponding URL for this state
	 * @return
	 */
	String getUrl();
	
	/**
	 * Check whether the state is same as this one
	 * @param state
	 * @return
	 */
	boolean isSameState(ProcessState state);
}
