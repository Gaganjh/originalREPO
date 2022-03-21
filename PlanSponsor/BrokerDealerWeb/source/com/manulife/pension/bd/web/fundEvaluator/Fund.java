package com.manulife.pension.bd.web.fundEvaluator;

public class Fund {

    private String fundId;

    private String isSelectedFund;

    private String isDefaultSelected;

    /**
     * @return the fundId
     */
    public String getFundId() {
        return fundId;
    }

    /**
     * @param fundId the fundId to set
     */
    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    /**
     * @return the isSelectedFund
     */
    public String getIsSelectedFund() {
        return isSelectedFund;
    }

    /**
     * @param isSelectedFund the isSelectedFund to set
     */
    public void setIsSelectedFund(String isSelectedFund) {
        this.isSelectedFund = isSelectedFund;
    }

    /**
     * @return the isDefaultSelected
     */
    public String getIsDefaultSelected() {
        return isDefaultSelected;
    }

    /**
     * @param isDefaultSelected the isDefaultSelected to set
     */
    public void setIsDefaultSelected(String isDefaultSelected) {
        this.isDefaultSelected = isDefaultSelected;
    }

}
