package com.manulife.pension.bd.web.fundEvaluator;

import java.io.Serializable;

public class ClientVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String fundMenu;

    private String fundClass;

    private String financialRepInNy = "no";

    private String nml = "no";

    /**
     * @return the fundMenu
     */
    public String getFundMenu() {
        return fundMenu;
    }

    /**
     * @param fundMenu the fundMenu to set
     */
    public void setFundMenu(String fundMenu) {
        this.fundMenu = fundMenu;
    }

    /**
     * @return the fundClass
     */
    public String getFundClass() {
        return fundClass;
    }

    /**
     * @param fundClass the fundClass to set
     */
    public void setFundClass(String fundClass) {
        this.fundClass = fundClass;
    }

    /**
     * @return the financialRepInNy
     */
    public String getFinancialRepInNy() {
        return financialRepInNy;
    }

    /**
     * @param financialRepInNy the financialRepInNy to set
     */
    public void setFinancialRepInNy(String financialRepInNy) {
        this.financialRepInNy = financialRepInNy;
    }

    /**
     * @return the nml
     */
    public String getNml() {
        return nml;
    }

    /**
     * @param nml the nml to set
     */
    public void setNml(String nml) {
        this.nml = nml;
    }

}
