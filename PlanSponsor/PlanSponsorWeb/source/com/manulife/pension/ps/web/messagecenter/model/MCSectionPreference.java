package com.manulife.pension.ps.web.messagecenter.model;

import java.io.Serializable;

import com.manulife.pension.ps.web.messagecenter.util.MCEnvironment;

/**
 * Section related preference
 * 
 * @author guweigu
 * 
 */
public class MCSectionPreference implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean expand;
	private boolean showAll;
	private String primarySortAttribute;
	private boolean ascending;

	private int defaultMessageCount;

	// default values
	public MCSectionPreference() {
		this(true, false, MCPreference.PriorityAttrName, false, MCEnvironment
				.getDefaultMessageCount());
	}

	public MCSectionPreference(boolean expand, boolean showAll,
			String primarySortAttr, boolean ascending, int defaultMessageCount) {
		this.expand = expand;
		this.showAll = showAll;
		this.primarySortAttribute = primarySortAttr;
		this.ascending = ascending;
		this.defaultMessageCount = defaultMessageCount;
	}

	// sort order etc...
	public boolean isExpand() {
		return expand;
	}

	public boolean isShowAll() {
		return showAll;
	}

	public String getPrimarySortAttribute() {
		return primarySortAttribute;
	}

	public boolean isAscending() {
		return ascending;
	}

	public int getDefaultMessageCount() {
		return defaultMessageCount;
	}
}