package com.manulife.pension.bd.web.bob.investment;


import java.util.Date;
import java.util.List;

import com.manulife.pension.platform.web.report.BaseReportForm;

/**
 * This class is used to hold the data for 
 * Funds changed report.
 * 
 * @author nallaba
 *
 */
public class InvestmentSelectionReportForm extends BaseReportForm {
	
	private static final long serialVersionUID = 1L;
	
	private List<Date> availableReportDates;
	private String selectedAsOfDate;
	private String selectedView;
	private boolean showAvailableOptions;
	private Date displaySelectedAsOfDate;
	private boolean show408b2Section;
	private boolean display404Section;
    private boolean showPlanAndInvestmentNotice;
    private boolean showIcc;
    private boolean showPreviousYearEndIccUnavailableMessage;
    private boolean showMissingIccContactMessage;
    private boolean showIpiAddendum;
    private boolean showParticipantFundChangeNotice;
    
    private boolean svfIndicator = false;
    private boolean investmentRelatedCostsPageAvailable = false;
    private boolean svfFlag = false;
    
    private boolean classZero = false;
    
    private String stableValueFundId = "";
    private boolean merrillLynchContract;
    
   // RP and R1 fund suite discloser
 	private boolean rPandR1Indicator = false;
 	
 	private boolean lTIndicator= false;
 	
 	private boolean contractandProductRestrictionFlag = false;
 	
 	
 	/**
 	 * @return the rPandR1Indicator
 	 */
 	
 	public boolean isrPandR1Indicator() {
 		return rPandR1Indicator;
 	}

 	public void setrPandR1Indicator(boolean rPandR1Indicator) {
 		this.rPandR1Indicator = rPandR1Indicator;
 	}

	/**
	 * @return the svfIndicator
	 */
	public boolean isSvfIndicator() {
		return svfIndicator;
	}

	/**
	 * @param svfIndicator the svfIndicator to set
	 */
	public void setSvfIndicator(boolean svfIndicator) {
		this.svfIndicator = svfIndicator;
	}
	
	/**
	 * @return the availableReportDates
	 */
	public List<Date> getAvailableReportDates() {
		return availableReportDates;
	}
	/**
	 * @param availableReportDates the availableReportDates to set
	 */
	public void setAvailableReportDates(List<Date> availableReportDates) {
		this.availableReportDates = availableReportDates;
	}
	/**
	 * @return the selectedAsOfDate
	 */
	public String getSelectedAsOfDate() {
		return selectedAsOfDate;
	}
	/**
	 * @param selectedAsOfDate the selectedAsOfDate to set
	 */
	public void setSelectedAsOfDate(String selectedAsOfDate) {
		this.selectedAsOfDate = selectedAsOfDate;
	}
	/**
	 * @return the selectedView
	 */
	public String getSelectedView() {
		return selectedView;
	}
	/**
	 * @param selectedView the selectedView to set
	 */
	public void setSelectedView(String selectedView) {
		this.selectedView = selectedView;
	}
	
	/**
	 * @return the showAvailableOptions
	 */
	public boolean isShowAvailableOptions() {
		return showAvailableOptions;
	}
	
	/**
	 * @param showAvailableOptions the showAvailableOptions to set
	 */
	public void setShowAvailableOptions(boolean showAvailableOptions) {
		this.showAvailableOptions = showAvailableOptions;
	}
	/**
	 * @return the displaySelectedAsOfDate
	 */
	public Date getDisplaySelectedAsOfDate() {
		return displaySelectedAsOfDate;
	}
	/**
	 * @param displaySelectedAsOfDate the displaySelectedAsOfDate to set
	 */
	public void setDisplaySelectedAsOfDate(Date displaySelectedAsOfDate) {
		this.displaySelectedAsOfDate = displaySelectedAsOfDate;
	}
	
	public void setShow408b2Section(boolean show408b2Section) {
		this.show408b2Section = show408b2Section;
	}

	public boolean isShow408b2Section() {
		return show408b2Section;
	}

	public void setDisplay404Section(boolean display404Section) {
		this.display404Section = display404Section;
	}

	public boolean isDisplay404Section() {
		return display404Section;
	}
	
	public boolean getShowPlanAndInvestmentNotice() { return showPlanAndInvestmentNotice; }
	public void setShowPlanAndInvestmentNotice(boolean show) { showPlanAndInvestmentNotice = show; }
	
	public boolean getShowIcc() { return showIcc; }
	public void setShowIcc(boolean show) { showIcc = show; }
	
	public boolean getShowPreviousYearEndIccUnavailableMessage() { return showPreviousYearEndIccUnavailableMessage; }
	public void setShowPreviousYearEndIccUnavailableMessage(boolean show) { showPreviousYearEndIccUnavailableMessage = show; }
	
	public boolean getShowMissingIccContactMessage() { return showMissingIccContactMessage; }
	public void setShowMissingIccContactMessage(boolean show) { showMissingIccContactMessage = show; }
	
    public boolean getShowIpiAddendum() { return showIpiAddendum; }
    public void setShowIpiAddendum(boolean show) { showIpiAddendum = show; }
    
	public boolean getShowParticipantFundChangeNotice() { return showParticipantFundChangeNotice; }
	public void setShowParticipantFundChangeNotice(boolean show) { showParticipantFundChangeNotice = show; }
	
	public boolean isInvestmentRelatedCostsPageAvailable() {
		return investmentRelatedCostsPageAvailable;
	}

	public void setInvestmentRelatedCostsPageAvailable(
			boolean investmentRelatedCostsPageAvailable) {
		this.investmentRelatedCostsPageAvailable = investmentRelatedCostsPageAvailable;
	}
	
	public boolean isClassZero() {
		return classZero;
	}

	public void setClassZero(boolean classZero) {
		this.classZero = classZero;
	}

	/**
	 * Returns the Stable Value Fund Id
	 * 
	 * @return stableValueFundId
	 */
	public String getStableValueFundId() {
		return stableValueFundId;
	}

	/**
	 * Sets the Stable Value Fund Id.
	 * 
	 * @param stableValueFundId
	 */
	public void setStableValueFundId(String stableValueFundId) {
		this.stableValueFundId = stableValueFundId;
	}
	/**
	 * @return the merrillLynchContract
	 */
	public boolean isMerrillLynchContract() {
		return merrillLynchContract;
	}

	/**
	 * @param merrillLynchContract the merrillLynchContract to set
	 */
	public void setMerrillLynchContract(boolean merrillLynchContract) {
		this.merrillLynchContract = merrillLynchContract;
	}

	public boolean isSvfFlag() {
		return svfFlag;
	}

	public void setSvfFlag(boolean svfFlag) {
		this.svfFlag = svfFlag;
	}
	
	/**
	 * @return the lTIndicator
	 */
	public boolean islTIndicator() {
		return lTIndicator;
	}

	/**
	 * @param lTIndicator the lTIndicator to set
	 */
	public void setlTIndicator(boolean lTIndicator) {
		this.lTIndicator = lTIndicator;
	}
	
	/**
	 * @return the contractandProductRestrictionFlag
	 */
	public boolean isContractandProductRestrictionFlag() {
		return contractandProductRestrictionFlag;
	}

	/**
	 * @param contractandProductRestrictionFlag the contractandProductRestrictionFlag to set
	 */
	public void setContractandProductRestrictionFlag(boolean contractandProductRestrictionFlag) {
		this.contractandProductRestrictionFlag = contractandProductRestrictionFlag;
	}
	
}
