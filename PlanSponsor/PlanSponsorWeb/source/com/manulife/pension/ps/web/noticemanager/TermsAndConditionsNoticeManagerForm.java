package com.manulife.pension.ps.web.noticemanager;

import com.manulife.pension.ps.web.util.CloneableAutoForm;

@SuppressWarnings("serial")
public class TermsAndConditionsNoticeManagerForm  extends CloneableAutoForm {
	
	private String action = "default";
	private String fromPage = "";

	/**
	 * @return
	 */
	public String getFromPage() {
		return fromPage;
	}

	/**
	 * @param fromPage
	 */
	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.platform.web.controller.AutoForm#getAction()
	 */
	public String getAction() {
		return action;
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.platform.web.controller.AutoForm#setAction(java.lang.String)
	 */
	public void setAction(String action) {
		this.action = action;
	}

	
}
