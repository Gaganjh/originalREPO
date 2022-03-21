package com.manulife.pension.bd.web.fundEvaluator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.service.fund.valueobject.FundFamilyVO;

/**
 * 
 * @author Ranjith Kumar
 * 
 */

public class FundEvaluatorForm extends AutoForm {
	
	private static final long serialVersionUID = 0L;
    
	public static final String FIELD_CONTRACT_NUMBER = "contractNumber";    
    
    private String previousAction; // to determine irregular navigation
	
	private String page;   
	
	// STEP1 - Select Your client page 
	private ArrayList fundMenuList = new ArrayList(); // Fund menu

	private ArrayList fundClassesList = new ArrayList<LabelValueBean>(); // Fund classes

	private String fundMenuSelected;

	private String fundClassSelected;

	// added by IPS to Step 1
	private ArrayList<LabelValueBean> statesList = new ArrayList<LabelValueBean>(); // US states
	private String stateSelected;
    private String firmFilterSelected;
    private boolean edwardJones = false;
    private boolean merrillFirmFilter = false;
    private boolean showFirmFilter = false;
    private boolean puertoRicoPreviouslySelected;

    private String financialRepInNy = "no";

	private boolean nml = false;
	
	private String fundUsa;
	
	private String defaultSiteLocation;

	private String clientType = "";
	
	private boolean existingClientClosedFund;
	
	private boolean newPlanClosedFund;

	private String contractNumber;
	
	//STEP 2 - Select Criteria Page
	
	private String pieChartcolors = "";

	private String[] critCheck;

	private String[] critCheckSlider;
	/**
	 * A list of <code>CriteriaVO</code> beans that contain data to be saved The data for display
	 * comes from the report and later the data is captured in this collection part of the Form
	 * 
	 */
	public List<CriteriaVO> theItem = new ArrayList<CriteriaVO>();

	private boolean criteriaSelected = false;

	private String remainingPercentage = "";

	private String[] sliderValue;

	// STEP 3 - Narrow your list page    
	private String preSelectFunds;

	private boolean lifeStylePortfolios = false;

	private boolean lifeCyclePortfolios = false;

	private String compulsoryFunds;

	private boolean SVFSelected = false;

	private boolean MMFSelected = false;

	
	//STEP - 4 Investment Option.

	private String assetClassId;

	private String[] selectedFunds;

	private List<Fund> fundList = new ArrayList<Fund>();

	private HashMap<String, ArrayList<String>> selectedAssetClassFunds = new HashMap<String, ArrayList<String>>();

	private int totalSelectedFunds = 0;

	private HashMap<String, AssetClassDetails> assetClassDetails = new HashMap<String, AssetClassDetails>();

	private String rankingOrder; // for sort option on overlay.

	private String viewInvestmentOptionsBy; // for radio button on inv option overlay

	private String viewInvOptionsByOnPreview; // for radion button on preview overlay.
    
    private boolean selectedAnySVF; // SVF fund selected status for warning message on overlay
    
    private boolean selectedAnySVFCompeting; // SVF Competing selected status for warning message on overlay

	//PrintPreview
	private String listInvesmentOptionBy;

	private boolean isChangeInBaseLineFunds;
	

	//STEP 5 - Customize Report
	private String companyName;

	private String yourName;

	private String yourFirm;

	private String yourCoverSheet = FundEvaluatorConstants.COVER_SHEET_STANDARD;

	private String fundListRiskOrderOrAssetClass;
	
	private String averageExpenceRatioMethod = FundEvaluatorConstants.WEIGHTED_AVG;

	private String includedOptItem1;

	private String includedOptItem3;

	private String includedOptItem4;

	private String includedOptItem5;

	private String includedOptItem6;

	private String fundRankingMethodology;

	private boolean msgPresent = false;

	private String contractBaseClass;

	private String contractBaseFundPackageSeries;
	
	private String contractLocationId;
	
	private boolean isSmithBarneyAssociated;

	private boolean dataModified = false;
	
	private boolean includeGIFLSelectFunds;
	
	private String includedOptItem7;
	
	private String[] lifecycleFundSuites;
	
	private String[] lifestyleFundSuites;
	
	private String stableValueFunds;
	
	private String[] moneyMarketFunds;
	
	private boolean hasGuranteedAccountsFunds;
	
	private ArrayList<FundFamilyVO> fundFamilyList = new ArrayList<FundFamilyVO>();  // To hold FundFamily VO
	
	private ArrayList<FundFamilyVO> lifestyleFamilyList = new ArrayList<FundFamilyVO>();  // To hold FundFamily VO
	
	private Hashtable<String, com.manulife.pension.service.fund.valueobject.Fund> svfFundList = new Hashtable<String, com.manulife.pension.service.fund.valueobject.Fund>();   // To hold FundFamily VO
	
	private Hashtable<String, com.manulife.pension.service.fund.valueobject.Fund> mmfFundList = new Hashtable<String, com.manulife.pension.service.fund.valueobject.Fund>();  // To hold FundFamily VO
	
	private List<String> svfFundCodeList = new ArrayList<String>(); 
	
	private List<String> mmfFundCodeList = new ArrayList<String>();  
	
	private Map<String, Boolean> contractSelectedSVFAndMMFFunds = new HashMap<String, Boolean>();
	
	
	private boolean isPBAContrat;
	
	/**
	 * @return the selectedAssetClassFunds
	 */
	public HashMap<String, ArrayList<String>> getSelectedAssetClassFunds() {
		return selectedAssetClassFunds;
	}

	/**
	 * @param selectedAssetClassFunds the selectedAssetClassFunds to set
	 */
	public void setSelectedAssetClassFunds(
			HashMap<String, ArrayList<String>> selectedAssetClassFunds) {
		this.selectedAssetClassFunds = selectedAssetClassFunds;
	}

	/**
	 * @return the selectedFunds
	 */
	public String[] getSelectedFunds() {
		return selectedFunds;
	}

	/**
	 * @param selectedFunds the selectedFunds to set
	 */

	public void setSelectedFunds(String[] selectedFunds) {
		this.selectedFunds = selectedFunds;
	}

	/**
	 * @return the assetClassId
	 */
	public String getAssetClassId() {
		return assetClassId;
	}

	/**
	 * @param assetClassId the assetClassId to set
	 */
	public void setAssetClassId(String assetClassId) {
		this.assetClassId = assetClassId;
	}

	/**
	 * The while construct is used to create objects as necessary in order to return to the Struts
	 * populate method.
	 * 
	 * @param index
	 * @return CriteriaVO
	 */
	public CriteriaVO getTheItem(int index) {
		while (theItem.size() <= index)
			theItem.add(new CriteriaVO());

		return ((CriteriaVO) theItem.get(index));

	}

	/**
	 * Setter for the list used to pre-populate the form
	 * 
	 * @param theItemList
	 */
	public void setTheItemList(List<CriteriaVO> theItemList) {
		if (theItemList == null) {
			theItemList = new ArrayList<CriteriaVO>();
		}

		this.theItem = theItemList;
	}

	
	/**
	 * @return 
	 */
	public List<CriteriaVO> getTheItemList() {
		return this.theItem;
	}

	/**
	 * @return the contractNumber
	 */
	public String getContractNumber() {
		return contractNumber;
	}

	/**
	 * @param contractNumber the contractNumber to set
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	/**
	 * @return the critCheck
	 */
	public String[] getCritCheck() {
		return critCheck;
	}

	/**
	 * @param critCheck the critCheck to set
	 */
	public void setCritCheck(String[] critCheck) {
		this.critCheck = critCheck;
	}

	/**
	 * @return the critCheckSlider
	 */
	public String[] getCritCheckSlider() {
		return critCheckSlider;
	}

	/**
	 * @param critCheckSlider the critCheckSlider to set
	 */
	public void setCritCheckSlider(String[] critCheckSlider) {
		this.critCheckSlider = critCheckSlider;
	}

	/**
	 * @return the sliderValue
	 */
	public String[] getSliderValue() {
		return sliderValue;
	}

	/**
	 * @param sliderValue the sliderValue to set
	 */
	public void setSliderValue(String[] sliderValue) {
		this.sliderValue = sliderValue;
	}

	/**
	 * @return the fundMenuSelected
	 */
	public String getFundMenuSelected() {
		return fundMenuSelected;
	}

	/**
	 * @param fundMenuSelected the fundMenuSelected to set
	 */
	public void setFundMenuSelected(String fundMenuSelected) {
		this.fundMenuSelected = fundMenuSelected;
	}

	/**
	 * @return the fundClassSelected
	 */
	public String getFundClassSelected() {
		return fundClassSelected;
	}

	/**
	 * @param fundClassSelected the fundClassSelected to set
	 */
	public void setFundClassSelected(String fundClassSelected) {
		this.fundClassSelected = fundClassSelected;
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
	 * @return the fundMenuList
	 */
	public Collection<LabelValueBean> getFundMenuList() {
		return fundMenuList; // TODO - to be moved from here.
	}

	/**
	 * @param fundMenuList the fundMenuList to set
	 */
	public void setFundMenuList(ArrayList fundMenuList) {
		this.fundMenuList = fundMenuList;

	}

	/**
	 * @return the fundClassesList
	 */
	public Collection<LabelValueBean> getFundClassesList() {
		return fundClassesList;
	}

	/**
	 * @param fundClassesList the fundClassesList to set
	 */
	public void setFundClassesList(ArrayList fundClassesList) {
		this.fundClassesList = fundClassesList;
	}

	/**
	 * @return the preSelectFunds
	 */
	public String getPreSelectFunds() {
		return preSelectFunds;
	}

	/**
	 * @param preSelectFunds the preSelectFunds to set
	 */
	public void setPreSelectFunds(String preSelectFunds) {
		this.preSelectFunds = preSelectFunds;
	}

	/**
	 * @return the page
	 */
	public String getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(String page) {
		this.page = page;
	}

	/**
	 * The while construct is used to create objects as necessary in order to return to the Struts
	 * populate method.
	 * 
	 * @param index
	 * @return
	 */
	public Fund getFundList(int index) {
		while (fundList.size() <= index)
			fundList.add(new Fund());

		return ((Fund) fundList.get(index));

	}

	/**
	 * Setter for the list used to pre-populate the form
	 * 
	 * @param fundList
	 */
	public void setFundList(List<Fund> fundList) {
		if (fundList == null) {
			fundList = new ArrayList<Fund>();
		}

		this.fundList = fundList;
	}

	/**
	 * Utility method
	 */
	public List<Fund> getFundList() {
		return this.fundList;
	}

	/**
	 * @return the totalSelectedFunds
	 */
	public int getTotalSelectedFunds() {
		return totalSelectedFunds;
	}

	/**
	 * @param totalSelectedFunds the totalSelectedFunds to set
	 */
	public void setTotalSelectedFunds(int totalSelectedFunds) {
		this.totalSelectedFunds = totalSelectedFunds;
	}

	/**
	 * @return the assetClassDetails
	 */
	public HashMap<String, AssetClassDetails> getAssetClassDetails() {
		return assetClassDetails;
	}

	/**
	 * @param assetClassDetails the assetClassDetails to set
	 */
	public void setAssetClassDetails(
			HashMap<String, AssetClassDetails> assetClassDetails) {
		this.assetClassDetails = assetClassDetails;
	}

	/**
	 * @return the rankingOrder
	 */
	public String getRankingOrder() {
		return rankingOrder;
	}

	/**
	 * @param rankingOrder the rankingOrder to set
	 */
	public void setRankingOrder(String rankingOrder) {
		this.rankingOrder = rankingOrder;
	}

	/**
	 * @return the listInvesmentOptionBy
	 */
	public String getListInvesmentOptionBy() {
		return listInvesmentOptionBy;
	}

	/**
	 * @param listInvesmentOptionBy the listInvesmentOptionBy to set
	 */
	public void setListInvesmentOptionBy(String listInvesmentOptionBy) {
		this.listInvesmentOptionBy = listInvesmentOptionBy;
	}

	/**
	 * @return the viewInvestmentOptionsBy
	 */
	public String getViewInvestmentOptionsBy() {
		return viewInvestmentOptionsBy;
	}

	/**
	 * @param viewInvestmentOptionsBy the viewInvestmentOptionsBy to set
	 */
	public void setViewInvestmentOptionsBy(String viewInvestmentOptionsBy) {
		this.viewInvestmentOptionsBy = viewInvestmentOptionsBy;
	}

	/**
	 * @return the viewInvOptionsByOnPreview
	 */
	public String getViewInvOptionsByOnPreview() {
		return viewInvOptionsByOnPreview;
	}

	/**
	 * @param viewInvOptionsByOnPreview the viewInvOptionsByOnPreview to set
	 */
	public void setViewInvOptionsByOnPreview(String viewInvOptionsByOnPreview) {
		this.viewInvOptionsByOnPreview = viewInvOptionsByOnPreview;
	}

	/**
     * @return the selectedAnySVF
     */
    public boolean isSelectedAnySVF() {
        return selectedAnySVF;
    }

    /**
     * @param selectedAnySVF the selectedAnySVF to set
     */
    public void setSelectedAnySVF(boolean selectedAnySVF) {
        this.selectedAnySVF = selectedAnySVF;
    }

    /**
     * @return the selectedAnySVFCompeting
     */
    public boolean isSelectedAnySVFCompeting() {
        return selectedAnySVFCompeting;
    }

    /**
     * @param selectedAnySVFCompeting the selectedAnySVFCompeting to set
     */
    public void setSelectedAnySVFCompeting(boolean selectedAnySVFCompeting) {
        this.selectedAnySVFCompeting = selectedAnySVFCompeting;
    }

    /**
	 * @return
	 */
	public String getFundUsa() {
		return fundUsa;
	}

	/**
	 * @param fundUsa
	 */
	public void setFundUsa(String fundUsa) {
		this.fundUsa = fundUsa;
	}

	/**
	 * @return
	 */
	public String getCompulsoryFunds() {
		return compulsoryFunds;
	}

	/**
	 * @param compulsoryFunds
	 */
	public void setCompulsoryFunds(String compulsoryFunds) {
		this.compulsoryFunds = compulsoryFunds;
	}

	/**
	 * @return
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return
	 */
	public String getYourName() {
		return yourName;
	}

	/**
	 * @param yourName
	 */
	public void setYourName(String yourName) {
		this.yourName = yourName;
	}

	/**
	 * @return
	 */
	public String getYourFirm() {
		return yourFirm;
	}

	/**
	 * @param yourFirm
	 */
	public void setYourFirm(String yourFirm) {
		this.yourFirm = yourFirm;
	}

	/**
	 * @return
	 */
	public String getYourCoverSheet() {
		return yourCoverSheet;
	}

	/**
	 * @param yourCoverSheet
	 */
	public void setYourCoverSheet(String yourCoverSheet) {
		this.yourCoverSheet = yourCoverSheet;
	}

	/**
	 * @return
	 */
	public String getFundListRiskOrderOrAssetClass() {
		return fundListRiskOrderOrAssetClass;
	}

	/**
	 * @param fundListRiskOrderOrAssetClass
	 */
	public void setFundListRiskOrderOrAssetClass(
			String fundListRiskOrderOrAssetClass) {
		this.fundListRiskOrderOrAssetClass = fundListRiskOrderOrAssetClass;
	}
	

	public String getAverageExpenceRatioMethod() {
		return averageExpenceRatioMethod;
	}

	public void setAverageExpenceRatioMethod(String averageExpenceRatioMethod) {
		this.averageExpenceRatioMethod = averageExpenceRatioMethod;
	}

	/**
	 * @return
	 */
	public String getIncludedOptItem6() {
		return includedOptItem6;
	}

	/**
	 * @param includedOptItem6
	 */
	public void setIncludedOptItem6(String includedOptItem6) {
		this.includedOptItem6 = includedOptItem6;
	}

	/**
	 * @return
	 */
	public String getIncludedOptItem1() {
		return includedOptItem1;
	}

	/**
	 * @param includedOptItem1
	 */
	public void setIncludedOptItem1(String includedOptItem1) {
		this.includedOptItem1 = includedOptItem1;
	}

	/**
	 * @return
	 */
	public String getIncludedOptItem3() {
		return includedOptItem3;
	}

	/**
	 * @param includedOptItem3
	 */
	public void setIncludedOptItem3(String includedOptItem3) {
		this.includedOptItem3 = includedOptItem3;
	}

	/**
	 * @return
	 */
	public String getIncludedOptItem4() {
		return includedOptItem4;
	}

	/**
	 * @param includedOptItem4
	 */
	public void setIncludedOptItem4(String includedOptItem4) {
		this.includedOptItem4 = includedOptItem4;
	}

	/**
	 * @return
	 */
	public String getIncludedOptItem5() {
		return includedOptItem5;
	}

	/**
	 * @param includedOptItem5
	 */
	public void setIncludedOptItem5(String includedOptItem5) {
		this.includedOptItem5 = includedOptItem5;
	}

	/**
	 * @return
	 */
	public String getFundRankingMethodology() {
		return fundRankingMethodology;
	}

	/**
	 * @param fundRankingMethodology
	 */
	public void setFundRankingMethodology(String fundRankingMethodology) {
		this.fundRankingMethodology = fundRankingMethodology;
	}

	
	/**
	 * @return
	 */
	public boolean isMsgPresent() {
		return msgPresent;
	}

	/**
	 * @param msgPresent
	 */
	public void setMsgPresent(boolean msgPresent) {
		this.msgPresent = msgPresent;
	}

	/**
	 * @return
	 */
	public boolean isLifeStylePortfolios() {
		return lifeStylePortfolios;
	}

	/**
	 * @param lifeStylePortfolios
	 */
	public void setLifeStylePortfolios(boolean lifeStylePortfolios) {
		this.lifeStylePortfolios = lifeStylePortfolios;
	}

	/**
	 * @return
	 */
	public boolean isLifeCyclePortfolios() {
		return lifeCyclePortfolios;
	}

	/**
	 * @param lifeCyclePortfolios
	 */
	public void setLifeCyclePortfolios(boolean lifeCyclePortfolios) {
		this.lifeCyclePortfolios = lifeCyclePortfolios;
	}

	/**
	 * @return
	 */
	public boolean isSVFSelected() {
		return SVFSelected;
	}

	/**
	 * @param selected
	 */
	public void setSVFSelected(boolean selected) {
		SVFSelected = selected;
	}

	/**
	 * @return
	 */
	public boolean isMMFSelected() {
		return MMFSelected;
	}

	/**
	 * @param selected
	 */
	public void setMMFSelected(boolean selected) {
		MMFSelected = selected;
	}


	/**
	 * @return
	 */
	public String getClientType() {
		return clientType;
	}

	/**
	 * @param clientType
	 */
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	/**
	 * @return
	 */
	public boolean isCriteriaSelected() {
		return criteriaSelected;
	}

	/**
	 * @param criteriaSelected
	 */
	public void setCriteriaSelected(boolean criteriaSelected) {
		this.criteriaSelected = criteriaSelected;
	}

	/**
	 * @return
	 */
	public String getRemainingPercentage() {
		return remainingPercentage;
	}

	/**
	 * @param remainingPercentage
	 */
	public void setRemainingPercentage(String remainingPercentage) {
		this.remainingPercentage = remainingPercentage;
	}

	/**
	 * @return the isChangeInBaseLineFunds
	 */
	public boolean isChangeInBaseLineFunds() {
		return isChangeInBaseLineFunds;
	}

	/**
	 * @param isChangeInBaseLineFunds the isChangeInBaseLineFunds to set
	 */
	public void setChangeInBaseLineFunds(boolean isChangeInBaseLineFunds) {
		this.isChangeInBaseLineFunds = isChangeInBaseLineFunds;
	}

	/**
	 * @return
	 */
	public String getPieChartcolors() {
		return pieChartcolors;
	}

	/**
	 * @param pieChartcolors
	 */
	public void setPieChartcolors(String pieChartcolors) {
		this.pieChartcolors = pieChartcolors;
	}

	/**
	 * @return
	 */
	public String getDefaultSiteLocation() {
		return defaultSiteLocation;
	}

	/**
	 * @param defaultSiteLocation
	 */
	public void setDefaultSiteLocation(String defaultSiteLocation) {
		this.defaultSiteLocation = defaultSiteLocation;
	}


	/**
	 * @return
	 */
	public boolean isExistingClientClosedFund() {
		return existingClientClosedFund;
	}

	/**
	 * @param existingClientClosedFund
	 */
	public void setExistingClientClosedFund(boolean existingClientClosedFund) {
		this.existingClientClosedFund = existingClientClosedFund;
	}

	/**
	 * @return
	 */
	public boolean isNewPlanClosedFund() {
		return newPlanClosedFund;
	}

	/**
	 * @param newPlanClosedFund
	 */
	public void setNewPlanClosedFund(boolean newPlanClosedFund) {
		this.newPlanClosedFund = newPlanClosedFund;
	}

	/**
	 * @return the contractBaseClass
	 */
	public String getContractBaseClass() {
		return contractBaseClass;
	}

	/**
	 * @param contractBaseClass the contractBaseClass to set
	 */
	public void setContractBaseClass(String contractBaseClass) {
		this.contractBaseClass = contractBaseClass;
	}

	/**
	 * @return the contractBaseFundPackageSeries
	 */
	public String getContractBaseFundPackageSeries() {
		return contractBaseFundPackageSeries;
	}

	/**
	 * @param contractBaseFundPackageSeries the contractBaseFundPackageSeries to set
	 */
	public void setContractBaseFundPackageSeries(
			String contractBaseFundPackageSeries) {
		this.contractBaseFundPackageSeries = contractBaseFundPackageSeries;
	}

	/**
	 * @return
	 */
	public boolean isNml() {
		return nml;
	}

	/**
	 * @param nml
	 */
	public void setNml(boolean nml) {
		this.nml = nml;
	}


    /**
	 * @see org.apache.struts.action.Form#reset(org.apache.struts.action.ActionMapping,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	public void reset( HttpServletRequest arg1) {
		super.reset( arg1);

    		//step 1 check box vlaues
    		existingClientClosedFund = false;
    		newPlanClosedFund = false;
    		nml = false;
    		includeGIFLSelectFunds = false;
    		edwardJones = false;
    		merrillFirmFilter = false;
    		    
    		//step 2 check box values
    		lifeStylePortfolios = false;
    		lifeCyclePortfolios = false;
    		selectedFunds = null;
    		lifecycleFundSuites = null;
    		lifestyleFundSuites = null;
    		stableValueFunds = null;
    		moneyMarketFunds = null;
    		//step 5 check box values
    		includedOptItem1 = null;
    		includedOptItem3 = null;
    		includedOptItem5 = null;
    		includedOptItem6 = null;
    		includedOptItem7 = null;
	}

    /**
     * @return the contractLocationId
     */
    public String getContractLocationId() {
        return contractLocationId;
    }

    /**
     * @param contractLocationId the contractLocationId to set
     */
    public void setContractLocationId(String contractLocationId) {
        this.contractLocationId = contractLocationId;
    }

    /**
     * @return the previousAction
     */
    public String getPreviousAction() {
        return previousAction;
    }

    /**
     * @param previousAction the previousAction to set
     */
    public void setPreviousAction(String previousAction) {
        this.previousAction = previousAction;
    }

    /**
     * @return the isSmithBarneyAssociated
     */
    public boolean isSmithBarneyAssociated() {
        return isSmithBarneyAssociated;
    }

    /**
     * @param isSmithBarneyAssociated the isSmithBarneyAssociated to set
     */
    public void setSmithBarneyAssociated(boolean isSmithBarneyAssociated) {
        this.isSmithBarneyAssociated = isSmithBarneyAssociated;
    }

    /**
     * 
     * @return dataModified
     */
	public boolean isDataModified() {
		return dataModified;
	}

	/**
	 * 
	 * @param dataModified
	 */
	public void setDataModified(boolean dataModified) {
		this.dataModified = dataModified;
	}

	/**
	 * 
	 * @return includeGIFLSelectFunds
	 */
	public boolean isIncludeGIFLSelectFunds() {
		return includeGIFLSelectFunds;
	}

	/**
	 * 
	 * @param includeGIFLSelectFunds
	 */
	public void setIncludeGIFLSelectFunds(boolean includeGIFLSelectFunds) {
		this.includeGIFLSelectFunds = includeGIFLSelectFunds;
	}

	/**
	 * 
	 * @return includedOptItem7
	 */
	public String getIncludedOptItem7() {
		return includedOptItem7;
	}

	/**
	 * 
	 * @param includedOptItem7
	 */
	public void setIncludedOptItem7(String includedOptItem7) {
		this.includedOptItem7 = includedOptItem7;
	}

	/**
	 * 
	 * @return lifecycleFundSuites
	 */
	public String[] getLifecycleFundSuites() {
		return lifecycleFundSuites;
	}

	/**
	 * 
	 * @param lifecycleFundSuites
	 */
	public void setLifecycleFundSuites(String[] lifecycleFundSuites) {
		this.lifecycleFundSuites = lifecycleFundSuites;
	}
	
	
	/**
	 * 
	 * @return lifestyleFundSuites
	 */
	public String[] getLifestyleFundSuites() {
		return lifestyleFundSuites;
	}

	/**
	 * 
	 * @param lifestyleFundSuites
	 */
	public void setLifestyleFundSuites(String[] lifestyleFundSuites) {
		this.lifestyleFundSuites = lifestyleFundSuites;
	}
	
	
	
	/**
	 * 
	 * @return fundFamilyList
	 */
	public ArrayList<FundFamilyVO> getFundFamilyList() {
		return fundFamilyList;
	}

	/**
	 * 
	 * @param fundFamilyList
	 */
	public void setFundFamilyList(ArrayList<FundFamilyVO> fundFamilyList) {
		this.fundFamilyList = fundFamilyList;
	}

	/**
	 * 
	 * @return lifestyleFamilyList
	 */
	public ArrayList<FundFamilyVO> getLifestyleFamilyList() {
		return lifestyleFamilyList;
	}

	/**
	 * 
	 * @param lifestyleFamilyList
	 */
	public void setLifestyleFamilyList(ArrayList<FundFamilyVO> lifestyleFamilyList) {
		this.lifestyleFamilyList = lifestyleFamilyList;
	}
	
	
	
	
	
	
	
    public ArrayList getStatesList() {
        return statesList;
    }

    public void setStatesList(ArrayList statesList) {
        this.statesList = statesList;
    }

    public String getStateSelected() {
        return stateSelected;
    }

    public void setStateSelected(String stateSelected) {
        this.stateSelected = stateSelected;
    }

    public String getFirmFilterSelected() {
        return firmFilterSelected;
    }

    public void setFirmFilterSelected(String firmFilterSelected) {
        this.firmFilterSelected = firmFilterSelected;
    }

    public boolean isEdwardJones() {
        return edwardJones;
    }

    public void setEdwardJones(boolean edwardJones) {
        this.edwardJones = edwardJones;
    }
    
    public boolean isMerrillFirmFilter() {
        return merrillFirmFilter;
    }

    public void setMerrillFirmFilter(boolean merrillFirmFilter) {
        this.merrillFirmFilter = merrillFirmFilter;
    }

    public boolean isShowFirmFilter() {
        return showFirmFilter;
    }

    public void setShowFirmFilter(boolean showFirmFilter) {
        this.showFirmFilter = showFirmFilter;
    }

    public boolean isPuertoRicoPreviouslySelected() {
        return puertoRicoPreviouslySelected;
    }

    public void setPuertoRicoPreviouslySelected(boolean puertoRicoPreviouslySelected) {
        this.puertoRicoPreviouslySelected = puertoRicoPreviouslySelected;
    }

	public boolean isHasGuranteedAccountsFunds() {
		return hasGuranteedAccountsFunds;
	}

	public void setHasGuranteedAccountsFunds(boolean hasGuranteedAccountsFunds) {
		this.hasGuranteedAccountsFunds = hasGuranteedAccountsFunds;
	}

	public String getStableValueFunds() {
		return stableValueFunds;
	}

	public void setStableValueFunds(String stableValueFunds) {
		this.stableValueFunds = stableValueFunds;
	}

	public String[] getMoneyMarketFunds() {
		return moneyMarketFunds;
	}

	public void setMoneyMarketFunds(String[] moneyMarketFunds) {
		this.moneyMarketFunds = moneyMarketFunds;
	}

	public Hashtable<String, com.manulife.pension.service.fund.valueobject.Fund> getSvfFundList() {
		return svfFundList;
	}

	public void setSvfFundList(
			Hashtable<String, com.manulife.pension.service.fund.valueobject.Fund> svfFundList) {
		this.svfFundList = svfFundList;
	}

	public Hashtable<String, com.manulife.pension.service.fund.valueobject.Fund> getMmfFundList() {
		return mmfFundList;
	}

	public void setMmfFundList(
			Hashtable<String, com.manulife.pension.service.fund.valueobject.Fund> mmfFundList) {
		this.mmfFundList = mmfFundList;
	}

	public List<String> getSvfFundCodeList() {
		return svfFundCodeList;
	}

	public void setSvfFundCodeList(List<String> defaultselectedstablevaluefunds) {
		this.svfFundCodeList = defaultselectedstablevaluefunds;
	}

	public List<String> getMmfFundCodeList() {
		return mmfFundCodeList;
	}

	public void setMmfFundCodeList(List<String> mmfFundCodeList) {
		this.mmfFundCodeList = mmfFundCodeList;
	}

	public Map<String, Boolean> getContractSelectedSVFAndMMFFunds() {
		return contractSelectedSVFAndMMFFunds;
	}

	public void setContractSelectedSVFAndMMFFunds(Map<String, Boolean> contractSelectedSVFAndMMFFunds) {
		this.contractSelectedSVFAndMMFFunds = contractSelectedSVFAndMMFFunds;
	}

	public boolean isPBAContrat() {
		return isPBAContrat;
	}

	public void setPBAContrat(boolean isPBAContrat) {
		this.isPBAContrat = isPBAContrat;
	}

	
}