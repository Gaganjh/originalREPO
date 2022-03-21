package com.manulife.pension.platform.web.fap.tabs.util;

/**
 * ColumnsToggleInfoBean, holds the information for the sub-tabs for the 
 * Funds & Performance page.
 * Currently only the PerformanceAndFees tab has this sub-tab option, which would 
 * be Quarterly or Monthly
 *  
 * @author ayyalsa
 *
 */
public class ColumnsToggleInfoBean implements java.io.Serializable {

	/**
	 * a default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String option;
	private String actionURL;
	private boolean optionActive;
	
	/**
	 * Constructor
	 * 
	 * @param id
	 * @param option
	 * @param actionURL
	 * @param optionActive
	 */
	public ColumnsToggleInfoBean(String id, String option, String actionURL, boolean optionActive) {
		this.id = id;
		this.option = option;
		this.actionURL = actionURL;
		this.optionActive = optionActive;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the option
	 */
	public String getOption() {
		return option;
	}
	/**
	 * @param option the option to set
	 */
	public void setOption(String option) {
		this.option = option;
	}
	/**
	 * @return the actionURL
	 */
	public String getActionURL() {
		return actionURL;
	}
	/**
	 * @param actionURL the actionURL to set
	 */
	public void setActionURL(String actionURL) {
		this.actionURL = actionURL;
	}
	/**
	 * @return the optionActive
	 */
	public boolean isOptionActive() {
		return optionActive;
	}
	/**
	 * @param optionActive the optionActive to set
	 */
	public void setOptionActive(boolean optionActive) {
		this.optionActive = optionActive;
	}
	
}
