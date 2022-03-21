package com.manulife.pension.platform.web.taglib.report;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.PropertyUtils;

import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.controller.BaseUserProfile;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.pension.util.content.manager.ContentCacheManager;

/**
 * This class represents a tag that allows user to change the report page size.
 * It relies on the JavaScript "RemoteScripting.js" to communicate the user's
 * decision to the server and refresh the current page.
 * 
 * @author Charles Chan
 */
public class SetReportPageSizeTag extends TagSupport {

    private String preCondition;
    private boolean editContribution = false;
    private boolean editVesting = false;
    private boolean editLongTermPartTimeDetails = false;

    /**
     * Constructor.
     */
    public SetReportPageSizeTag() {
        super();
    }

    private String getWarningMessage() throws JspException {
        try {
            Content content = ContentCacheManager.getInstance().getContentById(
                    CommonContentConstants.WARNING_INVALID_REPORT_PAGE_SIZE,
                    ContentTypeManager.instance().MISCELLANEOUS);
            return (String) PropertyUtils.getProperty(content, "text");
        } catch (Exception e) {
            return "(Unmanaged text) You must enter a number "
                    + "betweem 1 and 100";
        }
    }

    public final int doEndTag() throws JspException {

        BaseUserProfile profile = BaseSessionHelper
                .getBaseUserProfile((HttpServletRequest) pageContext.getRequest());

        int defaultPageSize = new Integer(CommonEnvironment.getInstance()
                .getDefaultPageSize()).intValue();

        int actualPageSize = profile.getPreferences().getInt(
                UserPreferenceKeys.REPORT_PAGE_SIZE, defaultPageSize);

        StringBuffer sb = new StringBuffer();
        sb.append("Rows per page&nbsp;");
        sb.append("<input type=\"text\" name=\"newPageSize\" "
                + "size=\"3\" maxlength=\"3\" value=\"" + actualPageSize
                + "\">\n");
        sb.append("&nbsp;<a href=\"\" onclick=\"")
                .append("if (");
        if (preCondition != null && preCondition.length() > 0) {
            sb.append(preCondition);
        } else {
            sb.append("true");
        }
        sb.append(") {");
        if (editContribution || editVesting || editLongTermPartTimeDetails) {
        	sb.append("setReportPageSizePreferenceForEdit('" + getWarningMessage()
        			+ "'); }return false;\">go</a>\n");
        } else {
        	sb.append("setReportPageSizePreference('" + getWarningMessage()
        			+ "'); }return false;\">go</a>\n");
        }
        sb.append("<script language=\"JavaScript\">\n").append(
                "reportPageSize=").append(actualPageSize).append(";\n");
        sb.append("</script>");

        try {
            pageContext.getOut().print(sb.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return EVAL_PAGE;
    }

    /**
     * @return Returns the preCondition.
     */
    public String getPreCondition() {
        return preCondition;
    }

    /**
     * @param preCondition
     *            The preCondition to set.
     */
    public void setPreCondition(String preCondition) {
        this.preCondition = preCondition;
    }

	public boolean isEditContribution() {
		return editContribution;
	}
	public void setEditContribution(boolean editContribution) {
		this.editContribution = editContribution;
	}

    public boolean isEditVesting() {
        return editVesting;
    }

	public void setEditVesting(boolean editVesting) {
        this.editVesting = editVesting;
    }

    public boolean isEditLongTermPartTimeDetails() {
		return editLongTermPartTimeDetails;
	}

	public void setEditLongTermPartTimeDetails(boolean editLongTermPartTimeDetails) {
		this.editLongTermPartTimeDetails = editLongTermPartTimeDetails;
	}

}