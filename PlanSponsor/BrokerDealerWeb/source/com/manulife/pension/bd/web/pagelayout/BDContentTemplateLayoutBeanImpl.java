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

/**
 * 
 * In current scenario BDLayoutBean were constructed by reading the pages.xml.
 * 
 * In dynamic content page, we can’t predict the content id, menu id, body, body
 * id, these values were constructed through ContentTemplatePageContext and
 * passed to BDContentTemplateLayoutBeanImpl.
 * 
 * This class responsible for constructing the BDLayoutBean without using the
 * pages.xml.
 * 
 * @author aambrose
 * 
 */
public class BDContentTemplateLayoutBeanImpl implements BDLayoutBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Location location;

	private ContentTemplatePageContext contentTemplatePageContext;

	public BDContentTemplateLayoutBeanImpl(Location location,
			ContentTemplatePageContext contentTemplatePageContext) {
		this.location = location;
		this.contentTemplatePageContext = contentTemplatePageContext;
	}

	public String getBobMenuId() {
		return "";
	}

	public String getBody() {
		return contentTemplatePageContext.getBody();
	}

	public String getBodyId() {
		return contentTemplatePageContext.getBodyId();
	}

	/**
	 * Get the contentId
	 */
	public int getContentId() {
		return contentTemplatePageContext.getContentId();
	}

	public String getContractReportMenuId() {
		return "";
	}

	public String getContractReportMenuIdForDB() {
		return "";
	}

	public String getDbAccountMenuId() {
		return "";
	}

	public int getDefinedBenefitContentId() {
		return 0;
	}
	
	public int getGiflSelectContentId() {
		return 0;
	}	

	public String getFooter() {
		return "";
	}

	public String getHeader() {
		return "";
	}

	public ArrayList<String> getJavascripts() {
		return null;
	}

	public String getLayout() {
		return "";
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
		return contentTemplatePageContext.getMenuId();
	}

	public String getParticipantAccountMenuId() {
		return "";
	}

	public String getSelectedTabId() {
		return "";
	}

	public ArrayList<StyleSheet> getStylesheets() {
		return null;
	}

	public String getTitle() {
		return "";
	}

	public int hashCode() {
		return getMenuId().hashCode();
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
