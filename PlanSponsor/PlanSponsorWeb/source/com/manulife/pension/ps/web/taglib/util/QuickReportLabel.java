package com.manulife.pension.ps.web.taglib.util;

import java.io.Serializable;

/**
 * @author Charles Chan
 */
public class QuickReportLabel implements Serializable {

	private String text;
	
	/**
	 * Constructor.
	 * 
	 */
	public QuickReportLabel() {
		super();
	}

	/**
	 * @return Returns the text.
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text The text to set.
	 */
	public void setText(String text) {
		this.text = text;
	}
}
