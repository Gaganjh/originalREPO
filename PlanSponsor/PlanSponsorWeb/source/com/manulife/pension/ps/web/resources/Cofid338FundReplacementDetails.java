package com.manulife.pension.ps.web.resources;

import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.service.contract.valueobject.FundReplacementRecommendationVO;

public class Cofid338FundReplacementDetails {

	private String pageTitle;
	private String sectionHeading;
	private String effectiveDateText;
	private String replacementFundText;
	private String authorizationText;
	private String formAndDocket;
	
	private String contractNumber;
	private String contractName;
	private String companyId;
	private String imagePath;
	private List<FundReplacementRecommendationVO> recommendations = new ArrayList<FundReplacementRecommendationVO>();
    private String sectionHeaderContent;
    private String changeYourProfileContent;
    private String defalutInvestmentOption;
    private String paymentofWilshire;
    private String additionalTermsAndConditions;
    private String stableValueFundTransferText1;
    private String stableValueFundTransferText2;
    private String stableValueFundTransferText3;
	private String generalDisclosure;
    private String footerText;
    private String wilshire338ProfileName;
    
    public Cofid338FundReplacementDetails(){
    	
    }
	public String getSectionHeaderContent() {
		return sectionHeaderContent;
	}

	public void setSectionHeaderContent(String sectionHeaderContent) {
		this.sectionHeaderContent = sectionHeaderContent;
	}

	public String getChangeYourProfileContent() {
		return changeYourProfileContent;
	}

	public void setChangeYourProfileContent(String changeYourProfileContent) {
		this.changeYourProfileContent = changeYourProfileContent;
	}

	public String getDefalutInvestmentOption() {
		return defalutInvestmentOption;
	}

	public void setDefalutInvestmentOption(String defalutInvestmentOption) {
		this.defalutInvestmentOption = defalutInvestmentOption;
	}

	public String getPaymentofWilshire() {
		return paymentofWilshire;
	}

	public void setPaymentofWilshire(String paymentofWilshire) {
		this.paymentofWilshire = paymentofWilshire;
	}

	public String getAdditionalTermsAndConditions() {
		return additionalTermsAndConditions;
	}

	public void setAdditionalTermsAndConditions(String additionalTermsAndConditions) {
		this.additionalTermsAndConditions = additionalTermsAndConditions;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public List<FundReplacementRecommendationVO> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(
			List<FundReplacementRecommendationVO> recommendations) {
		this.recommendations = recommendations;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getSectionHeading() {
		return sectionHeading;
	}

	public void setSectionHeading(String sectionHeading) {
		this.sectionHeading = sectionHeading;
	}

	public String getEffectiveDateText() {
		return effectiveDateText;
	}

	public void setEffectiveDateText(String effectiveDateText) {
		this.effectiveDateText = effectiveDateText;
	}

	public String getReplacementFundText() {
		return replacementFundText;
	}

	public void setReplacementFundText(String replacementFundText) {
		this.replacementFundText = replacementFundText;
	}

	public String getAuthorizationText() {
		return authorizationText;
	}

	public void setAuthorizationText(String authorizationText) {
		this.authorizationText = authorizationText;
	}

	public String getFormAndDocket() {
		return formAndDocket;
	}

	public void setFormAndDocket(String formAndDocket) {
		this.formAndDocket = formAndDocket;
	}

	public String getStableValueFundTransferText1() {
		return stableValueFundTransferText1;
	}

	public void setStableValueFundTransferText1(String stableValueFundTransferText1) {
		this.stableValueFundTransferText1 = stableValueFundTransferText1;
	}

	public String getStableValueFundTransferText2() {
		return stableValueFundTransferText2;
	}

	public void setStableValueFundTransferText2(String stableValueFundTransferText2) {
		this.stableValueFundTransferText2 = stableValueFundTransferText2;
	}

	public String getStableValueFundTransferText3() {
		return stableValueFundTransferText3;
	}

	public void setStableValueFundTransferText3(String stableValueFundTransferText3) {
		this.stableValueFundTransferText3 = stableValueFundTransferText3;
	}

	public String getGeneralDisclosure() {
		return generalDisclosure;
	}

	public void setGeneralDisclosure(String generalDisclosure) {
		this.generalDisclosure = generalDisclosure;
	}

	public String getFooterText() {
		return footerText;
	}

	public void setFooterText(String footerText) {
		this.footerText = footerText;
	}
	
	public String getWilshire338ProfileName() {
		return wilshire338ProfileName;
	}

	public void setWilshire338ProfileName(String wilshire338ProfileName) {
		this.wilshire338ProfileName = wilshire338ProfileName;
	}
}
