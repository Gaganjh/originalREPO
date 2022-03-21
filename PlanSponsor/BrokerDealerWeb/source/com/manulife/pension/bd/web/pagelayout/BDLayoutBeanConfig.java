package com.manulife.pension.bd.web.pagelayout;

import java.util.ArrayList;

import com.manulife.pension.content.valueobject.LayoutPage;

public class BDLayoutBeanConfig implements BDLayoutBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String menuId;
	private String bodyId;

	// This will give the MenuItem to be highlighted in
	// "Contract Navigation Tab" for DC contract.
	private String contractReportMenuId;
	// This will give the MenuItem to be highlighted in
	// "Contract Navigation Tab" for DB contract.
	private String contractReportMenuIdForDB;

	// This will give the "Tab" to be highlighted in Participant Account page.
	private String participantAccountMenuId;
	// This will give the "Tab" to be highlighted in Defined Benefit Account
	// page.
	private String dbAccountMenuId;

	// This will give the "Tab" to be highlighted in Block Of Business page.
	private String bobMenuId;

	// This will give the "Tab" to be highlighted.
	private String selectedTabId;

	private String layout;
	private String header;
	private String body;
	private String footer;
	private String title;
	private int contentId;
	private int definedBenefitContentId;
	private ArrayList<StyleSheet> stylesheets;
	private ArrayList<String> javascripts;
	private int giflSelectContentId;

	public int getContentId() {
		return contentId;
	}

	public void setContentId(int contentId) {
		this.contentId = contentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getBobMenuId() {
		return bobMenuId;
	}

	public void setBobMenuId(String bobMenuId) {
		this.bobMenuId = bobMenuId;
	}

	public String getSelectedTabId() {
		return selectedTabId;
	}

	public void setSelectedTabId(String selectedTabId) {
		this.selectedTabId = selectedTabId;
	}

	public String getBodyId() {
		return bodyId;
	}

	public void setBodyId(String bodyId) {
		this.bodyId = bodyId;
	}

	public String getContractReportMenuId() {
		return contractReportMenuId;
	}

	public void setContractReportMenuId(String contractReportMenuId) {
		this.contractReportMenuId = contractReportMenuId;
	}

	public String getContractReportMenuIdForDB() {
		return contractReportMenuIdForDB;
	}

	public void setContractReportMenuIdForDB(String contractReportMenuIdForDB) {
		this.contractReportMenuIdForDB = contractReportMenuIdForDB;
	}

	public String getParticipantAccountMenuId() {
		return participantAccountMenuId;
	}

	public void setParticipantAccountMenuId(String participantAccountMenuId) {
		this.participantAccountMenuId = participantAccountMenuId;
	}

	public ArrayList<StyleSheet> getStylesheets() {
		return stylesheets;
	}

	public void setStylesheets(ArrayList<StyleSheet> stylesheets) {
		this.stylesheets = stylesheets;
	}

	public ArrayList<String> getJavascripts() {
		return javascripts;
	}

	public void setJavascripts(ArrayList<String> javascripts) {
		this.javascripts = javascripts;
	}

	public int getDefinedBenefitContentId() {
		return definedBenefitContentId;
	}

	public void setDefinedBenefitContentId(int definedBenefitContentId) {
		this.definedBenefitContentId = definedBenefitContentId;
	}

	public String getDbAccountMenuId() {
		return dbAccountMenuId;
	}

	public void setDbAccountMenuId(String dbAccountMenuId) {
		this.dbAccountMenuId = dbAccountMenuId;
	}

	/**
	 * This methos returns the LayoutPage associated with the page
	 * 
	 * @return LayoutPage
	 */
	public LayoutPage getLayoutPageBean() {
		return null;
	}

	/**
	 * Returns the content id specific to GIFL V03
	 * 
	 * @return int
	 */
	public int getGiflSelectContentId() {
		return giflSelectContentId;
	}

	/**
	 * Sets the content id specific to GIFL V03
	 * 
	 * @param giflSelectContentId
	 */
	public void setGiflSelectContentId(int giflSelectContentId) {
		this.giflSelectContentId = giflSelectContentId;
	}
}
