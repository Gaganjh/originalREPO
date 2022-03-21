package com.manulife.pension.ps.web.preferences;

import com.manulife.pension.ps.web.controller.PsForm;

/**
 * @author Charles Chan
 */
public class SetReportPageSizeForm extends PsForm {

    private Integer newPageSize;

    /**
     * Constructor.
     */
    public SetReportPageSizeForm() {
        super();
    }

    /**
     * @return Returns the newPageSize.
     */
    public Integer getNewPageSize() {
        return newPageSize;
    }

    /**
     * @param newPageSize
     *            The newPageSize to set.
     */
    public void setNewPageSize(Integer newPageSize) {
        this.newPageSize = newPageSize;
    }
}