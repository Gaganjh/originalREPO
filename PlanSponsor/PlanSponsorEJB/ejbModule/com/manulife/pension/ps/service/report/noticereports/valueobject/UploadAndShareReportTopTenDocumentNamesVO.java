package com.manulife.pension.ps.service.report.noticereports.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * VO for Upload And Share Report top ten documents name.
 * 
 */
public class UploadAndShareReportTopTenDocumentNamesVO implements Serializable {

    private static final long serialVersionUID = -323725582282773733L;

    private String documentName;

    private BigDecimal documentPercentage;

    /**
     * Constructor.
     * 
     * @param documentName
     * @param documentPercentage
     */
    public UploadAndShareReportTopTenDocumentNamesVO(String documentName,
            BigDecimal documentPercentage) {
        super();
        this.documentName = documentName;
        this.documentPercentage = documentPercentage;
    }

    /**
     * Returns Document Name.
     * 
     * @return documentName
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * Sets Document Name.
     * 
     * @param documentName
     */
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    /**
     * Returns Document Percentage.
     * 
     * @return documentPercentage
     */
    public BigDecimal getDocumentPercentage() {
        return documentPercentage;
    }

    /**
     * Sets Document Percentage.
     * 
     * @param documentPercentage
     */
    public void setDocumentPercentage(BigDecimal documentPercentage) {
        this.documentPercentage = documentPercentage;
    }

}
