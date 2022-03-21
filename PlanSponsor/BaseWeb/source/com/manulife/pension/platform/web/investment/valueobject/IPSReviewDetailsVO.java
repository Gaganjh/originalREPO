package com.manulife.pension.platform.web.investment.valueobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.platform.web.investment.valueobject.CriteriaAndWeightingPresentation;

/**
 * VO class which is used to hold values to create PDF for IPS Manager pages
 * 
 * @author Turaga Divya
 * 
 */
public class IPSReviewDetailsVO implements Serializable {

	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	private String jhLogoPath;
	private String intro1Text;
	private String intro2Text;
	private String asOfDate;
	private String bodyHeader1;
	private String ipsManagerDetailsSectionTitle;
	private String ipsCriteriaAndWeightingsSectionTitle;
	private String ipsAssistServiceText;
	private String ipsScheduleAnnualReviewDateText;
	private String ipsServiceReviewDateText;
	private String ipsAnnualReviewDate;
	private String pageName;
	List<CriteriaAndWeightingPresentation> ipsCriteriaAndWeightingList;
	private String pieChartURL;
	private String ipsReviewReportSectionTitle;
	private List<IPSReviewReportDetailsVO> ipsReviewReportDetailsList = new ArrayList<IPSReviewReportDetailsVO>();
	private boolean ipsServiceAvailable;
	private String footnotes;
	private List<FundInfo> fundInstructions;
	private String globalDisclosure;
	private int totalweighting;
	private String lastModifiedDate;
	private String disclaimer;
	private String footer;
	private String iatEffectiveDateText;
	private String standardFooter;
	private boolean ipsReviewReportDetailsNotAvailable = false;
	private String ipsNoCurrentOrPreviousReport;
	private String ipsServiceDeactivated;
	private String iatProcessingDate;
	private String contractName;
	private int contractNumber;
	private String fundInfoIconPath;
	private String resultsPageSubHeader;
	
	/**
	 * @return jhLogoPath
	 */
	public String getJhLogoPath() {
		return jhLogoPath;
	}

	/**
	 * @param jhLogoPath
	 *            the jhLogoPath to set
	 */
	public void setJhLogoPath(String jhLogoPath) {
		this.jhLogoPath = jhLogoPath;
	}

	/**
	 * @return intro1Text
	 */
	public String getIntro1Text() {
		return intro1Text;
	}

	/**
	 * @param intro1Text
	 *            the intro1Text to set
	 */
	public void setIntro1Text(String intro1Text) {
		this.intro1Text = intro1Text;
	}

	/**
	 * @return intro2Text
	 */
	public String getIntro2Text() {
		return intro2Text;
	}

	/**
	 * @param intro2Text
	 *            the intro2Text to set
	 */
	public void setIntro2Text(String intro2Text) {
		this.intro2Text = intro2Text;
	}

	/**
	 * @return asOfDate
	 */
	public String getAsOfDate() {
		return asOfDate;
	}

	/**
	 * @param asOfDate
	 *            the asOfDate to set
	 */
	public void setAsOfDate(String asOfDate) {
		this.asOfDate = asOfDate;
	}

	/**
	 * @return ipsManagerDetailsSectionTitle
	 */
	public String getIpsManagerDetailsSectionTitle() {
		return ipsManagerDetailsSectionTitle;
	}

	/**
	 * @param ipsManagerDetailsSectionTitle
	 *            the ipsManagerDetailsSectionTitle to set
	 */
	public void setIpsManagerDetailsSectionTitle(
			String ipsManagerDetailsSectionTitle) {
		this.ipsManagerDetailsSectionTitle = ipsManagerDetailsSectionTitle;
	}

	/**
	 * @return ipsCriteriaAndWeightingsSectionTitle
	 */
	public String getIpsCriteriaAndWeightingsSectionTitle() {
		return ipsCriteriaAndWeightingsSectionTitle;
	}

	/**
	 * @param ipsCriteriaAndWeightingsSectionTitle
	 *            the ipsCriteriaAndWeightingsSectionTitle to set
	 */
	public void setIpsCriteriaAndWeightingsSectionTitle(
			String ipsCriteriaAndWeightingsSectionTitle) {
		this.ipsCriteriaAndWeightingsSectionTitle = ipsCriteriaAndWeightingsSectionTitle;
	}

	/**
	 * @return ipsAssistServiceText
	 */
	public String getIpsAssistServiceText() {
		return ipsAssistServiceText;
	}

	/**
	 * @param ipsAssistServiceText
	 *            the ipsAssistServiceText to set
	 */
	public void setIpsAssistServiceText(String ipsAssistServiceText) {
		this.ipsAssistServiceText = ipsAssistServiceText;
	}

	/**
	 * @return ipsScheduleAnnualReviewDateText
	 */
	public String getIpsScheduleAnnualReviewDateText() {
		return ipsScheduleAnnualReviewDateText;
	}

	/**
	 * @param ipsScheduleAnnualReviewDateText
	 *            the ipsScheduleAnnualReviewDateText to set
	 */
	public void setIpsScheduleAnnualReviewDateText(
			String ipsScheduleAnnualReviewDateText) {
		this.ipsScheduleAnnualReviewDateText = ipsScheduleAnnualReviewDateText;
	}

	/**
	 * @return ipsServiceReviewDateText
	 */
	public String getIpsServiceReviewDateText() {
		return ipsServiceReviewDateText;
	}

	/**
	 * @param ipsServiceReviewDateText
	 *            the ipsServiceReviewDateText to set
	 */
	public void setIpsServiceReviewDateText(String ipsServiceReviewDateText) {
		this.ipsServiceReviewDateText = ipsServiceReviewDateText;
	}

	/**
	 * @return ipsAnnualReviewDate
	 */
	public String getIpsAnnualReviewDate() {
		return ipsAnnualReviewDate;
	}

	/**
	 * @param ipsAnnualReviewDate
	 *            the ipsAnnualReviewDate to set
	 */
	public void setIpsAnnualReviewDate(String ipsAnnualReviewDate) {
		this.ipsAnnualReviewDate = ipsAnnualReviewDate;
	}

	/**
	 * @return ipsCriteriaAndWeightingList
	 */
	public List<CriteriaAndWeightingPresentation> getIpsCriteriaAndWeightingList() {
		return ipsCriteriaAndWeightingList;
	}

	/**
	 * @param ipsCriteriaAndWeightingList
	 *            the ipsCriteriaAndWeightingList to set
	 */
	public void setIpsCriteriaAndWeightingList(
			List<CriteriaAndWeightingPresentation> ipsCriteriaAndWeightingList) {
		this.ipsCriteriaAndWeightingList = ipsCriteriaAndWeightingList;
	}

	/**
	 * @return pageName
	 */
	public String getPageName() {
		return pageName;
	}

	/**
	 * @param pageName
	 *            the pageName to set
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	/**
	 * @return bodyHeader1
	 */
	public String getBodyHeader1() {
		return bodyHeader1;
	}

	/**
	 * @param bodyHeader1
	 *            the bodyHeader1 to set
	 */
	public void setBodyHeader1(String bodyHeader1) {
		this.bodyHeader1 = bodyHeader1;
	}

	/**
	 * @return pieChartURL
	 */
	public String getPieChartURL() {
		return pieChartURL;
	}

	/**
	 * @param pieChartURL
	 *            the pieChartURL to set
	 */
	public void setPieChartURL(String pieChartURL) {
		this.pieChartURL = pieChartURL;
	}

	/**
	 * @return ipsReviewReportSectionTitle
	 */
	public String getIpsReviewReportSectionTitle() {
		return ipsReviewReportSectionTitle;
	}

	/**
	 * @param ipsReviewReportSectionTitle
	 *            the ipsReviewReportSectionTitle to set
	 */
	public void setIpsReviewReportSectionTitle(
			String ipsReviewReportSectionTitle) {
		this.ipsReviewReportSectionTitle = ipsReviewReportSectionTitle;
	}

	/**
	 * @return ipsReviewReportDetailsList
	 */
	public List<IPSReviewReportDetailsVO> getIpsReviewReportDetailsList() {
		return ipsReviewReportDetailsList;
	}

	/**
	 * @param ipsReviewReportDetailsList
	 *            the ipsReviewReportDetailsList to set
	 */
	public void setIpsReviewReportDetailsList(
			List<IPSReviewReportDetailsVO> ipsReviewReportDetailsList) {
		this.ipsReviewReportDetailsList = ipsReviewReportDetailsList;
	}

	/**
	 * @return IPSServiceAvailable
	 */
	public boolean isIPSServiceAvailable() {
		return ipsServiceAvailable;
	}

	/**
	 * @param iPSServiceAvailable
	 *            the IPSServiceAvailable to set
	 */
	public void setIPSServiceAvailable(boolean iPSServiceAvailable) {
		ipsServiceAvailable = iPSServiceAvailable;
	}

	/**
	 * @return fundInstructions
	 */
	public List<FundInfo> getFundInstructions() {
		return fundInstructions;
	}
	
	
	/**
	 * @param fundInstructions
	 * 				the fundInstructions to set
	 */
	public void setFundInstructions(List<FundInfo> fundInstructions) {
		this.fundInstructions = fundInstructions;
	}
	
	/**
	 * @return globalDisclosure
	 */
	public String getGlobalDisclosure() {
		return globalDisclosure;
	}

	/**
	 * @param globalDisclosure
	 *            the globalDisclosure to set
	 */
	public void setGlobalDisclosure(String globalDisclosure) {
		this.globalDisclosure = globalDisclosure;
	}

	/**
	 * @return totalweighting
	 */
	public int getTotalweighting() {
		return totalweighting;
	}

	/**
	 * @param totalweighting
	 *            the totalweighting to set
	 */
	public void setTotalweighting(int totalweighting) {
		this.totalweighting = totalweighting;
	}
	
	/**
	 * @return lastModifiedDate
	 */
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @param lastModifiedDate
	 *            the lastModifiedDate to set
	 */
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * @return footnotes
	 */
	public String getFootnotes() {
		return footnotes;
	}

	/**
	 * @param footnotes
	 *            the footnotes to set.
	 */
	public void setFootnotes(String footnotes) {
		this.footnotes = footnotes;
	}

	/**
	 * @return disclaimer
	 */
	public String getDisclaimer() {
		return disclaimer;
	}

	/**
	 * @param disclaimer
	 *            the disclaimer to set.
	 */
	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	/**
	 * @return footer
	 */
	public String getFooter() {
		return footer;
	}

	/**
	 * @param footer
	 *            the footer to set.
	 */
	public void setFooter(String footer) {
		this.footer = footer;
	}

	/**
	 * @return iatEffectiveDateText
	 */
	public String getIatEffectiveDateText() {
		return iatEffectiveDateText;
	}

	/**
	 * @param iatEffectiveDateText
	 *            the iatEffectiveDateText to set.
	 */
	public void setIatEffectiveDateText(String iatEffectiveDateText) {
		this.iatEffectiveDateText = iatEffectiveDateText;
	}

	/**
	 * @return standardFooter
	 */
	public String getStandardFooter() {
		return standardFooter;
	}

	/**
	 * @param standardFooter
	 *            the standardFooter to set.
	 */
	public void setStandardFooter(String standardFooter) {
		this.standardFooter = standardFooter;
	}

	/**
	 * @return ipsReviewReportDetailsNotAvailable
	 */
	public boolean isIpsReviewReportDetailsNotAvailable() {
		return ipsReviewReportDetailsNotAvailable;
	}

	/**
	 * @param ipsReviewReportDetailsNotAvailable
	 *            the ipsReviewReportDetailsNotAvailable to set
	 */
	public void setIpsReviewReportDetailsNotAvailable(
			boolean ipsReviewReportDetailsNotAvailable) {
		this.ipsReviewReportDetailsNotAvailable = ipsReviewReportDetailsNotAvailable;
	}

	/**
	 * @return ipsNoCurrentOrPreviousReport
	 */
	public String getIpsNoCurrentOrPreviousReport() {
		return ipsNoCurrentOrPreviousReport;
	}

	/**
	 * @param ipsNoCurrentOrPreviousReport
	 *            the ipsNoCurrentOrPreviousReport to set
	 */
	public void setIpsNoCurrentOrPreviousReport(
			String ipsNoCurrentOrPreviousReport) {
		this.ipsNoCurrentOrPreviousReport = ipsNoCurrentOrPreviousReport;
	}
	
	/**
	 * @return ipsServiceDeactivated
	 */
	public String getIpsServiceDeactivated() {
		return ipsServiceDeactivated;
	}

	/**
	 * 
	 * @param ipsServiceDeactivated
	 *            the ipsServiceDeactivated to set
	 */
	public void setIpsServiceDeactivated(String ipsServiceDeactivated) {
		this.ipsServiceDeactivated = ipsServiceDeactivated;
	}
	
	
	/**
	 * @return iatProcessingDate
	 */
	public String getIatProcessingDate() {
		return iatProcessingDate;
	}

	/**
	 * @param iatProcessingDate
	 *  		the iatProcessingDate to set
	 */
	public void setIatProcessingDate(String iatProcessingDate) {
		this.iatProcessingDate = iatProcessingDate;
	}

	/**
	 * @return contractName
	 */
	public String getContractName() {
		return contractName;
	}

	/**
	 * @param contractName
	 * 			the contractName to set.
	 */
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	/**
	 * @return contractNumber
	 */
	public int getContractNumber() {
		return contractNumber;
	}

	/**
	 * @param contractNumber
	 * 			the contractNumber to set.
	 */
	public void setContractNumber(int contractNumber) {
		this.contractNumber = contractNumber;
	}

	/**
	 * @return the fundInfoIconPath
	 */
	public String getFundInfoIconPath() {
		return fundInfoIconPath;
	}

	/**
	 * @param fundInfoIconPath the fundInfoIconPath to set
	 */
	public void setFundInfoIconPath(String fundInfoIconPath) {
		this.fundInfoIconPath = fundInfoIconPath;
	}

	/**
	 * @return the resultsPageSubHeader
	 */
	public String getResultsPageSubHeader() {
		return resultsPageSubHeader;
	}

	/**
	 * @param resultsPageSubHeader the resultsPageSubHeader to set
	 */
	public void setResultsPageSubHeader(String resultsPageSubHeader) {
		this.resultsPageSubHeader = resultsPageSubHeader;
	}
}
