package com.manulife.pension.ps.service.report.noticereports.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * VO for Upload And Share Report source of upload.
 * 
 */
public class UploadAndShareReportSourceOfUploadShareVO implements Serializable {

    private static final long serialVersionUID = -858120092292118188L;

    private String userCategory;

    private BigDecimal percentageUpload;

    /**
     * Constructor.
     * 
     * @param userCategory
     * @param percentageUpload
     */
    public UploadAndShareReportSourceOfUploadShareVO(String userCategory, BigDecimal percentageUpload) {
        super();
        this.userCategory = userCategory;
        this.percentageUpload = percentageUpload;
    }

    /**
     * Returns User Category
     * 
     * @return userCategory
     */
    public String getUserCategory() {
        return userCategory;
    }

    /**
     * Sets User Category.
     * 
     * @param userCategory
     */
    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    /**
     * Returns Percentage of document Uploaded
     * 
     * @return percentageUpload
     */
    public BigDecimal getPercentageUpload() {
        return percentageUpload;
    }

    /**
     * Sets percentage of documents placed.
     * 
     * @param percentageUpload
     */
    public void setPercentageUpload(BigDecimal percentageUpload) {
        this.percentageUpload = percentageUpload;
    }

}
