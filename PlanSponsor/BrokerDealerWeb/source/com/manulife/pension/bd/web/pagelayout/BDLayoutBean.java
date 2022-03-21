package com.manulife.pension.bd.web.pagelayout;

import java.io.Serializable;
import java.util.ArrayList;

import com.manulife.pension.content.valueobject.LayoutPage;

/**
 * The interface for BDW LayoutBean
 * 
 * @author guweigu
 *
 */
public interface BDLayoutBean extends Serializable {
	public int getContentId();

	public String getTitle();

	public String getLayout();

	public String getHeader();

	public String getBody();

	public String getFooter();

	public String getMenuId();

	public String getBobMenuId();

	public String getSelectedTabId();

	public String getBodyId();

	public String getContractReportMenuId();

	public String getContractReportMenuIdForDB();

	public String getParticipantAccountMenuId();

	public ArrayList<StyleSheet> getStylesheets();

	public ArrayList<String> getJavascripts();

	public int getDefinedBenefitContentId();
	
	/**
	 * Returns the content id of GIFL select version
	 * 
	 * @return int
	 */
	public int getGiflSelectContentId();

	public String getDbAccountMenuId();

	/**
	 * This methos returns the LayoutPage associated with the page
	 * 
	 * @return LayoutPage
	 */
	public LayoutPage getLayoutPageBean();
}
