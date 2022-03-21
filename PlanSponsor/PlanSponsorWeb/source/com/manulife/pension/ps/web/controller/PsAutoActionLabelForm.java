package com.manulife.pension.ps.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;


import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * PsAutoActionLabelForm extends the PsAutoForm to add support for label
 * replacement of the actions. The form translates the 'actionLabel' field into
 * an appropriate 'action' field (used by the rest of the PsAutoAction
 * framework).
 * 
 */
public class PsAutoActionLabelForm extends AutoForm {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The ACTION_LABEL contains the property name for the label.
     */
    public static final String ACTION_LABEL = "actionLabel";

    private static final Logger logger = Logger
            .getLogger(PsAutoActionLabelForm.class);

    protected String actionLabel = null;

    /**
     * @see com.manulife.pension.platform.web.controller.AutoForm#getAction()
     */
    public String getAction() {

        if (StringUtils.isNotBlank(actionLabel)) {
            return getActionFromLabel();
        }

        return super.getAction();
    }

    /**
     * This method translates the label into an action.
     * 
     * @return String The action to perform.
     */
    private String getActionFromLabel() {

        // Unescapes special HTML codes (like "&amp;").
        String result = StringEscapeUtils.unescapeHtml(actionLabel);

        // Replace any special characters.
        result = StringUtils.replace(result, "&", "And");

        // Capitalizes on whitespace.
        result = WordUtils.capitalize(result);

        // Removes whitespace.
        result = StringUtils.deleteWhitespace(result);

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer().append("actionLabel: ").append(
                    actionLabel).append(", original action: ").append(action)
                    .append(", action: ").append(result));
        } // fi

        return result;
    }

    /**
     * @see com.manulife.pension.platform.web.controller.AutoForm#reset(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest)
     */
    public void reset( HttpServletRequest arg1) {
       // //super.reset(arg0, arg1);

        actionLabel = null;
    }

    /**
     * @return Returns the actionLabel.
     */
    public String getActionLabel() {
        return actionLabel;
    }

    /**
     * @param actionLabel
     *            The actionLabel to set.
     */
    public void setActionLabel(String actionLabel) {
        this.actionLabel = actionLabel;
    }

}
