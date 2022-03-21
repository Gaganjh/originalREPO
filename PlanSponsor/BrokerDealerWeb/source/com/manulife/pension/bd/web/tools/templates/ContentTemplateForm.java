package com.manulife.pension.bd.web.tools.templates;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * store the values for template three body page
 *
 * @author ambroar
 *
 */
public class ContentTemplateForm extends AutoForm {

    /**
     *
     */
    private static final long serialVersionUID = 0L;

    private String currentPageName;

    /**
     * Constructor for ContentTemplateReportForm
     */
    public ContentTemplateForm() {
        super();
    }

    /**
     * @return the String currentPageName
     */
    public String getCurrentPageName() {
        return currentPageName;
    }

    /**
     * @param String currentPageName
     *            the currentPageName to set
     */
    public void setCurrentPageName(String currentPageName) {
        this.currentPageName = currentPageName;
    }

}
