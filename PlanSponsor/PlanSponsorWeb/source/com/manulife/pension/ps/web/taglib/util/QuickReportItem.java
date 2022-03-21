package com.manulife.pension.ps.web.taglib.util;

import java.io.Serializable;

/**
 * This class represents a quick report list item.
 * 
 * @author Charles Chan
 */
public class QuickReportItem implements Serializable{

	private String id;
	private String url;
	private String contentId;
	private String title;
	
	public QuickReportItem() {
	}

	/**
	 * @return The quick report ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param string The quick report ID
	 */
	public void setId(String string) {
		id = string;
	}

	/**
	 * @return The quick report URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param string The quick report URL
	 */
	public void setUrl(String string) {
		url = string;
	}

	/**
	 * @return Returns the contentId.
	 */
	public String getContentId() {
		return contentId;
	}

	/**
	 * @param contentId The contentId to set.
	 */
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id=[").append(id).append("] url=[").append(url).append(
				"]");
		return sb.toString();
	}
}
