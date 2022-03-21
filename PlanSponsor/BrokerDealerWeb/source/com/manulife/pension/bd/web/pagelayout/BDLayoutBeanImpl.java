package com.manulife.pension.bd.web.pagelayout;

import java.util.ArrayList;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.util.log.LogUtility;

public class BDLayoutBeanImpl implements BDLayoutBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BDLayoutBeanConfig delegate;
	private Location location;
	private boolean isDefinedBenefit;
	private boolean isGiflSelect;

	public BDLayoutBeanImpl(BDLayoutBeanConfig delegate,
			boolean isDefinedBenefit, boolean isGiflSelect, Location location) {
		this.delegate = delegate;
		this.location = location;
		this.isDefinedBenefit = isDefinedBenefit;
		this.isGiflSelect = isGiflSelect;
	}

	public String getBobMenuId() {
		return delegate.getBobMenuId();
	}

	public String getBody() {
		return delegate.getBody();
	}

	public String getBodyId() {
		return delegate.getBodyId();
	}

	/**
	 * Get the contentId for the page based on whether a defined benefit
	 * content is asked for
	 */
	public int getContentId() {
		int contentId = 0;
		// get the defined benefit contentId if needed
		if (isDefinedBenefit) {			
			contentId = delegate.getDefinedBenefitContentId();
		} 
		//get the GIFL V03 content id if the GIFL version selected by the contract is V03.
		if (isGiflSelect) {
			contentId = delegate.getGiflSelectContentId();
		}
		// if the contentId is not set either not a DB or DB contentId is not set
		if (contentId == 0) {
			contentId = delegate.getContentId();
		}
		return contentId;
	}

	public String getContractReportMenuId() {
		return delegate.getContractReportMenuId();
	}

	public String getContractReportMenuIdForDB() {
		return delegate.getContractReportMenuIdForDB();
	}

	public String getDbAccountMenuId() {
		return delegate.getDbAccountMenuId();
	}

	public int getDefinedBenefitContentId() {
		return delegate.getDefinedBenefitContentId();
	}
	
	/**
	 * Returns the content id of GIFL select version
	 * 
	 * @return int
	 */	
	public int getGiflSelectContentId() {
		return delegate.getGiflSelectContentId();
	}

	public String getFooter() {
		return delegate.getFooter();
	}

	public String getHeader() {
		return delegate.getHeader();
	}

	public ArrayList<String> getJavascripts() {
		return delegate.getJavascripts();
	}

	public String getLayout() {
		return delegate.getLayout();
	}

	public LayoutPage getLayoutPageBean() {
		LayoutPage layoutPage = null;
		Content bean = null;
		try {
			if (location == null) {
				bean = ContentCacheManager.getInstance().getContentById(
						getContentId(),
						ContentTypeManager.instance().LAYOUT_PAGE);
			} else {
				bean = ContentCacheManager.getInstance().getContentById(
						getContentId(),
						ContentTypeManager.instance().LAYOUT_PAGE, location);
			}

		} catch (Exception e) {
			SystemException se = new SystemException(e, this.getClass()
					.getName(), "getLayoutPageBean", "Content id:"
					+ getContentId());
			LogUtility.logSystemException("BD", se);
		}

		if (bean != null) {
			if (bean instanceof LayoutPage)
				layoutPage = (LayoutPage) bean;
		}

		// This is modified to return at least empty LayoutPage object.
		// Otherwise we won't be able to set the
		// null object in the jsp. This is helpful during the development. We
		// can take it out when we
		// finished development or have all the content ready. (IC)
		return layoutPage == null ? (LayoutPage) new com.manulife.pension.content.view.MutableLayoutPage()
				: layoutPage;
	}

	public String getMenuId() {
		return delegate.getMenuId();
	}

	public String getParticipantAccountMenuId() {
		return delegate.getParticipantAccountMenuId();
	}

	public String getSelectedTabId() {
		return delegate.getSelectedTabId();
	}

	public ArrayList<StyleSheet> getStylesheets() {
		return delegate.getStylesheets();
	}

	public String getTitle() {
		return delegate.getTitle();
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
