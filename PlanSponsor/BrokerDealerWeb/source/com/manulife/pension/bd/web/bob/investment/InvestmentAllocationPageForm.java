package com.manulife.pension.bd.web.bob.investment;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.investment.valueobject.AllocationTotals;
import com.manulife.pension.ps.service.report.investment.valueobject.FundCategory;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * Form bean for investment pages
 */
public class InvestmentAllocationPageForm extends BaseReportForm {

    private static final long serialVersionUID = 1L;

    private String contractNumber;
    private String asOfDateReport = null; 
    private String asOfDateDetails = null;
    private String currentBusinessDate = null;
    private String actionDetail = null;
    private String selectedFundID = null;
    private String selectedFundName = null;
    
    private String comparableAsOfDateReport = null; 
    private String comparableAsOfDateDetails = null; 

    // initialized to asset view
    private String viewOption = CommonConstants.VIEW_BY_ASSET;

    // initialized to asset class
    private String organizingOption = BDConstants.VIEW_BY_ASSET_CLASS;

    private String otherText = null;

    // LS To do - make all the headers of the totals CMA managed?
    public static final String EXCLUDING_TEXT = " excluding ";
    public static final String OTHER_TEXT = "Total all investment options";
    public static final String LIFECYCLE_TEXT = "Target Date Portfolios";
    public static final String LIFESTYLE_TEXT = "Target Risk Portfolios";
    public static final String GIFL_TEXT = " Guaranteed Income Feature ";
    public static final String ASSET_ALLOCATION_TEXT = "Asset Allocation Portfolios";
    public static final String PBA_TEXT = "Personal Brokerage Accounts";
    
    public static final String FORMAT_DATE_LONG = "MMMM d, yyyy";
    private int numberOfInvestmentOptionSelected;
    private boolean jhiIndicatorFlg;

    /**
     * Gets the contractNumber
     * 
     * @return Returns a String
     */
    public String getContractNumber() {
        return StringUtils.trimToEmpty(contractNumber);
    }

    /**
     * Sets the contractNumber
     * 
     * @param contractNumber The contractNumber to set
     */
    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    /**
     * Sets the actionDetail
     * 
     * @param actionDetail The actionDetail to set
     */
    public void setActionDetail(String actionDetail) {
        this.actionDetail = actionDetail;
    }

    /**
     * Gets the actionDetail
     * 
     * @return Returns a String
     */
    public String getActionDetail() {
        return this.actionDetail;
    }

    /**
     * Gets the as of date in long format
     * 
     * @return Returns a String
     */
    // added for separating between dates of report & details
    public String getAsOfDateReport() {
        return this.asOfDateReport;
    }

    /**
     * Gets the as of date in long format
     * 
     * @return Returns a String
     */
    // added for separating between dates of report & details
    public String getAsOfDateDetails() {
        return this.asOfDateDetails;
    }

    /**
     * Sets the asOfDate
     * 
     * @param asOfDate The asOfDate to set
     */
    // added for separating between dates of report & details
    public void setAsOfDateReport(String asOfDate) {
        this.asOfDateReport = asOfDate;

        if (asOfDate != null) {

            Date date = new Date(Long.valueOf(asOfDateReport).longValue());
            this.comparableAsOfDateReport = getRenderedDate(date, RenderConstants.MEDIUM_YMD_DASHED);
        }

        setAsOfDateDetails(asOfDate);
    }

    /**
     * Sets the asOfDate
     * 
     * @param asOfDate The asOfDate to set
     */
    // added for separating between dates of report & details
    public void setAsOfDateDetails(String asOfDate) {
        this.asOfDateDetails = asOfDate;

        if (asOfDate != null) {
            Date date = new Date(Long.valueOf(asOfDateDetails).longValue());
            this.comparableAsOfDateDetails = getRenderedDate(date,
                    RenderConstants.MEDIUM_YMD_DASHED);
        } else {
            this.comparableAsOfDateDetails = null;
        }
    }

    /**
     * gets the comparableAsOfDate
     * 
     * @param comparableAsOfDate The comparableAsOfDate to set
     */
    // added for separating between dates of report & details
    public String getComparableAsOfDateReport() {
        return this.comparableAsOfDateReport;
    }

    /**
     * gets the comparableAsOfDate
     * 
     * @param comparableAsOfDate The comparableAsOfDate to set
     */
    // added for separating between dates of report & details
    public String getComparableAsOfDateDetails() {
        return this.comparableAsOfDateDetails;
    }

    /**
     * Gets the selected fundID
     * 
     * @return Returns a String
     */
    public String getSelectedFundID() {
        return this.selectedFundID;
    }

    /**
     * Sets the selected fund id
     * 
     * @param selectedFundID The selected Fund ID to set
     */
    public void setSelectedFundID(String selectedFundID) {

        this.selectedFundID = selectedFundID;
    }

    /**
     * Gets the selected fundName
     * 
     * @return Returns a String
     */
    public String getSelectedFundName() {
        return this.selectedFundName;
    }

    /**
     * Sets the selected fund Name
     * 
     * @param selectedFundName The selected Fund Name to set
     */
    public void setSelectedFundName(String selectedFundName) {
        this.selectedFundName = selectedFundName;
    }

    /**
     * gets the currentBusinessDate
     * 
     * @return Returns a string
     */
    public String getCurrentBusinessDate() {
        return this.currentBusinessDate;
    }

    /**
     * Sets the currentBusinessDate
     * 
     * @param currentBusinessDate The currentBusinessDate to set
     */
    public void setCurrentBusinessDate(Date currentBusinessDate) {
        this.currentBusinessDate = getRenderedDate(currentBusinessDate,
                RenderConstants.MEDIUM_YMD_DASHED);
    }

    /**
     * set to true if the current date is the same as the as of date on the form
     * 
     * @return boolean if the current date is the as of date.
     */
    // added for separating between dates of report & details
    public boolean isAsOfDateReportCurrent() {
        if (getCurrentBusinessDate() == null) {
            return false;
        }
        return getCurrentBusinessDate().equals(getComparableAsOfDateReport());
    }

    /**
     * set to true if the current date is the same as the as of date on the form
     * 
     * @return boolean if the current date is the as of date.
     */
    // added for separating between dates of report & details
    public boolean isAsOfDateDetailsCurrent() {
        if (getCurrentBusinessDate() == null) {
            return false;
        }
        return getCurrentBusinessDate().equals(getComparableAsOfDateDetails());
    }

    /**
     * gets the formatted as of date report
     * 
     * @return String
     */
    // added for separating between dates of report & details
    public String getFormattedAsOfDateReport() {
        if (getAsOfDateReport() == null) {
            return "";
        } else {
            Date date = new Date(Long.valueOf(getAsOfDateReport()).longValue());
            return getRenderedDate(date, RenderConstants.LONG_MDY);
        }
    }

    /**
     * gets the formatted as of date details
     * 
     * @return String
     */
    // added for separating between dates of report & details
    public String getFormattedAsOfDateDetails() {
        if (getAsOfDateDetails() == null) {
            return "";
        } else {
            Date date = new Date(Long.valueOf(getAsOfDateDetails()).longValue());
            return getRenderedDate(date, RenderConstants.LONG_MDY);
        }
    }

    /**
     * gets the rendered date
     * 
     * @return String
     */
    public String getRenderedDate(Date date, String pattern) {
        return DateRender.formatByPattern(date.toString(), " ", pattern);
    }

    /**
     * gets the total text
     * 
     * @return String
     */
    public String getTotalText() {
        return this.otherText;
    }

    /**
     * this method will generate the non lifestyle text depending on whether PBA and Lifestyle funds
     * exist.
     */
    @SuppressWarnings("unchecked")
    public void setNonLifestyle(List allocationTotals) {

        boolean hasPBA = false;
        boolean hasAssetAllocation = false;
        boolean hasGateway = false;
        Iterator iter = allocationTotals.iterator();

        while (iter.hasNext()) {
            AllocationTotals total = (AllocationTotals) iter.next();

            if (total.getFundCategoryType().equals(FundCategory.LIFESTYLE)
                    || total.getFundCategoryType().equals(FundCategory.LIFECYCLE))
                hasAssetAllocation = true;
            if (total.getFundCategoryType().equals(FundCategory.PBA))
                hasPBA = true;
            if (total.getFundCategoryType().equals(FundCategory.GIFL))
                hasGateway = true;
        }
        StringBuffer buf = new StringBuffer();
        buf.append(OTHER_TEXT);

        if (hasPBA && hasAssetAllocation && hasGateway) {
            buf.append(EXCLUDING_TEXT).append(ASSET_ALLOCATION_TEXT).append(", ").append(GIFL_TEXT)
                    .append(" and ").append(PBA_TEXT);
        } else if (hasPBA && hasAssetAllocation) {
            buf.append(EXCLUDING_TEXT).append(ASSET_ALLOCATION_TEXT).append(" and ").append(
                    PBA_TEXT);
        } else if (hasAssetAllocation) {
            buf.append(EXCLUDING_TEXT).append(ASSET_ALLOCATION_TEXT);
            if (hasGateway) {
                buf.append(" and ").append(GIFL_TEXT);
            }
        } else if (hasPBA) {
            buf.append(EXCLUDING_TEXT).append(PBA_TEXT);
        } else {
            // continue
        }

        this.otherText = buf.toString();

    }

    public String getViewOption() {
        return viewOption;
    }

    public void setViewOption(String viewOption) {
        this.viewOption = viewOption;
    }

    /**
     * gets the numberOfInvestmentOptionSelected
     * 
     * @return Returns a string
     */
    public int getNumberOfInvestmentOptionSelected() {
        return numberOfInvestmentOptionSelected;
    }

    /**
     * sets the numberOfInvestmentOptionSelected
     * 
     * @param numberOfInvestmentOptionSelected
     */
    public void setNumberOfInvestmentOptionSelected(int numberOfInvestmentOptionSelected) {
        this.numberOfInvestmentOptionSelected = numberOfInvestmentOptionSelected;
    }

    /**
     * gets the organizingOption
     * 
     * @return Returns a string
     */
    public String getOrganizingOption() {
        return organizingOption;
    }

    /**
     * sets the organizingOption
     * 
     * @param organizingOption
     */
    public void setOrganizingOption(String organizingOption) {
        this.organizingOption = organizingOption;
    }

	/**
	 * @return the jhiIndicatorFlg
	 */
	public boolean isJhiIndicatorFlg() {
		return jhiIndicatorFlg;
	}

	/**
	 * @param jhiIndicatorFlg the jhiIndicatorFlg to set
	 */
	public void setJhiIndicatorFlg(boolean jhiIndicatorFlg) {
		this.jhiIndicatorFlg = jhiIndicatorFlg;
	}

   }
