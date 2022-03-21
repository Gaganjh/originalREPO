package com.manulife.pension.platform.web.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * A simple implementation of the Process state
 * @author guweigu
 *
 */
public class SimpleProcessState implements ProcessState {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3604558727748668782L;
	private Map<String, ProcessState> map = new HashMap<String, ProcessState>(
			11);
	private String id;
	private String url;
	private boolean processedEnded = false;
	
	public SimpleProcessState(String id, String url) {
		this(id, url, false);
	}

	public SimpleProcessState(String id, String url, boolean endProcess) {
		this.id = id;
		this.url = url;
		processedEnded = endProcess;
	}

	public void addNext(String result, ProcessState next) {
		map.put(result, next);
	}

	public String getId() {
		return id;
	}

	public ProcessState getNext(String result) {
		return map.get(result);
	}

	public String getUrl() {
		return url;
	}

	public boolean isSameState(ProcessState state) {
		if (state == null) {
			return false;
		}
		return StringUtils.equals(id, state.getId());
	}

	public boolean hasProcessEnded() {
		return processedEnded;
	}
}
