package com.manulife.pension.bd.web.bob.blockOfBusiness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BlockOfBusinessUtility;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.LabelInfoBean;
import com.manulife.pension.ps.service.report.bob.valueobject.RiaFeeDetailsVO;
import com.manulife.pension.ps.service.report.bob.valueobject.RiaFeeRangeVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * BlockOfBusinessForm is the Action Form for BlockOfBusiness page.
 * 
 * @author harlomte
 * 
 */
public class BlockOfBusinessForm extends BaseReportForm {

    private static final long serialVersionUID = 1L;
    
    // Summary section User Name and other details
    private String internalUserName;

    private String financialRepUserName;

    private ArrayList<String> associatedFirmNames;
    
    private boolean internalUserAndNotInMimickModeInd;

    private boolean firmRepUserInd;
    

    private String currentTab;

    // Selected criteria will be capture here.
    private String contractName;

    private String contractNumber;

    private String financialRepName;

    private String assetRangeFrom;

    private String assetRangeTo;

    private String contractState;

    private String fundClassSelected;
    
    private String firmNameSelected;

    private String firmIDSelected;

    private String rvpSelected;

    private String salesRegionSelected;

    private String salesDivisionSelected;

    private String csfFeatureSelected;

    private String asOfDateSelected;

    private String usNySelected;
    
    private String productType;
    

	// Quick Filter related Search Criteria.
    private String quickFilterSelected;

    private String quickFilterContractName;
    
    private String quickFilterContractNumber;

    private String quickFilterRvpSelected;

    private String quickFilterFirmNameSelected;

    private String quickFilterFirmIDSelected;

    private String quickFilterUsOrNySelected;
    
    private String quickFilterProductTypeSelected;

    private String quickFilterFundClassSelected;
    
    // This variable will hold info whether the advance filter should be shown or not.
    private Boolean showAdvanceFilter;
    
    // This variable will let us know if quick filter was submitted or advance filter.
    private Boolean fromQuickFilter;

    // This has a list of applicable quick filters.
    private Map<String, LabelInfoBean> bobQuickFiltersMap;

    // This has a list of applicable advance filters.
    private Map<String, LabelInfoBean> bobAdvancedFiltersMap;
    
    // This will store the filtering criteria created, so that later, it can be used to show
    // "filters used" in CSV, PDF.
    private ReportCriteria filteringCriteriaSaved;

    // This will be used to tell whether the Footnotes describing
    // "Historical Content pertains to certain Contract Info" should be displayed or not.
    private Boolean showHistoricalContractInfoFootnote;

    // This will be used to tell whether the FOotnotes describing about Assets Column in DI tab
    // should be displayed or not.
    private Boolean showDIFootnote;
    
	// THis will be used to tell whether the Footnotes describing about the
	// Pending, Outstanding proposal contract counts are as of Latest as of date.
	private Boolean showPNAndPPContractCountAsOfLatestDateFootnote;
	

    // This indicator will be set to true after the page is accessed for the first time. This was
    // created mainly because, the requirement was to log the Page access into MRL whenever the user
    // accesses the page in default view or when accessed thru a bookmark. Finding out
    // if the user is accessing the page for the first time thru a bookmark was difficult. Hence,
    // this indicator was used to tell if the page is being accessed for the first time or not thru
    // a bookmark.
    private Boolean isPageAccessed;
    
    private Collection<String> legends;
    
    private Collection<String> riaLegends;
    
    private Collection<String> fiduciaryServicesTabLegends;
    
	private boolean isLevel1User;
    
    private boolean noContractsForDetailedBrokerReport;
    
    private List<RiaFeeDetailsVO> riaFeeDetailsVO = new ArrayList<RiaFeeDetailsVO>();

	private List<RiaFeeRangeVO> riaFeeRangeVO = new ArrayList<RiaFeeRangeVO>();
    
	public String getInternalUserName() {
        return internalUserName;
    }

    public void setInternalUserName(String internalUserName) {
        this.internalUserName = internalUserName;
    }

    public String getFinancialRepUserName() {
        return financialRepUserName;
    }

    public void setFinancialRepUserName(String financialRepUserName) {
        this.financialRepUserName = financialRepUserName;
    }

    public ArrayList<String> getAssociatedFirmNames() {
        return associatedFirmNames;
    }

    public void setAssociatedFirmNames(ArrayList<String> associatedFirmNames) {
        this.associatedFirmNames = associatedFirmNames;
    }

    public boolean getInternalUserAndNotInMimickModeInd() {
        return internalUserAndNotInMimickModeInd;
    }

    public void setInternalUserAndNotInMimickModeInd(boolean internalUserAndNotInMimickModeInd) {
        this.internalUserAndNotInMimickModeInd = internalUserAndNotInMimickModeInd;
    }

    public boolean getFirmRepUserInd() {
        return firmRepUserInd;
    }

    public void setFirmRepUserInd(boolean firmRepUserInd) {
        this.firmRepUserInd = firmRepUserInd;
    }

    public Map<String, LabelInfoBean> getBobQuickFiltersMap() {
        return bobQuickFiltersMap;
    }

    public void setBobQuickFiltersMap(Map<String, LabelInfoBean> bobQuickFiltersMap) {
        this.bobQuickFiltersMap = bobQuickFiltersMap;
    }

    /**
     * This method returns only those quick filters that are enabled.
     * 
     * @return - a list of quick filters that are enabled.
     */
    public List<LabelValueBean> getEnabledQuickFiltersList() {
        List<LabelValueBean> quickFiltersList = new ArrayList<LabelValueBean>();

        // The quickFilterOrderList has the same values as the keys present in bobQuickFiltersMap,
        // but in the order in which they need to be displayed in JSP page.
        for (String bobFilterKey : BlockOfBusinessUtility.quickFilterOrderList) {
            if (isQuickFilterEnabled(bobFilterKey)) {
                quickFiltersList.add(new LabelValueBean(bobQuickFiltersMap.get(bobFilterKey)
                        .getTitle(), bobQuickFiltersMap.get(bobFilterKey).getId()));
            }
        }
        return quickFiltersList;
    }
    
    /**
     * This method helps in checking if a given quick filter is enabled or not.
     * 
     * @param fieldKey
     * @return
     */
    public Boolean isQuickFilterEnabled(String fieldKey) {
        return (Boolean) (bobQuickFiltersMap.get(fieldKey) == null ? Boolean.FALSE
                : bobQuickFiltersMap.get(fieldKey).getEnabled());
    }

    public Map<String, LabelInfoBean> getBobAdvancedFiltersMap() {
        return bobAdvancedFiltersMap;
    }

    public void setBobAdvancedFiltersMap(Map<String, LabelInfoBean> bobAdvancedFiltersMap) {
        this.bobAdvancedFiltersMap = bobAdvancedFiltersMap;
    }

    /**
     * This method helps in checking if a given advance filter is enabled or not.
     * 
     * @param fieldKey
     * @return
     */
    public Boolean isAdvancedFilterEnabled(String fieldKey) {
        return (Boolean) (bobAdvancedFiltersMap.get(fieldKey) == null ? Boolean.FALSE
                : bobAdvancedFiltersMap.get(fieldKey).getEnabled());
    }

    /**
     * This method returns a Map containing all the applicable advanced filters.
     * 
     * @return
     */
    public Map<String, Boolean> getAdvancedFilterUsedStatusMap() {
        Map<String, Boolean> isAdvancedFilterAlreadyUsed = new HashMap<String, Boolean>();
        ArrayList<String> advancedFilters = BlockOfBusinessUtility.filterList;
        for (String advancedFilter : advancedFilters) {
            if (isAdvancedFilterEnabled(advancedFilter)) {
                isAdvancedFilterAlreadyUsed.put(advancedFilter, Boolean.FALSE);
            }
        }
        return isAdvancedFilterAlreadyUsed;
    }
   

	public Boolean getShowAdvanceFilter() {
        return showAdvanceFilter;
    }

    public void setShowAdvanceFilter(Boolean showAdvanceFilter) {
        this.showAdvanceFilter = showAdvanceFilter;
    }

    public Boolean getFromQuickFilter() {
        return fromQuickFilter;
    }

    public void setFromQuickFilter(Boolean fromQuickFilter) {
        this.fromQuickFilter = fromQuickFilter;
    }

    public String getQuickFilterContractName() {
        return quickFilterContractName;
    }

    public void setQuickFilterContractName(String quickFilterContractName) {
        this.quickFilterContractName = quickFilterContractName;
    }
    
    public String getQuickFilterContractNumber() {
        return quickFilterContractNumber;
    }

    public void setQuickFilterContractNumber(String quickFilterContractNumber) {
        this.quickFilterContractNumber = quickFilterContractNumber;
    }

    public ReportCriteria getFilteringCriteriaSaved() {
        return filteringCriteriaSaved;
    }

    public void setFilteringCriteriaSaved(ReportCriteria filteringCriteriaSaved) {
        this.filteringCriteriaSaved = filteringCriteriaSaved;
    }

    public String getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(String currentTab) {
        this.currentTab = currentTab;
    }

    public String getQuickFilterRvpSelected() {
        return quickFilterRvpSelected;
    }

    public void setQuickFilterRvpSelected(String quickFilterRvpSelected) {
        this.quickFilterRvpSelected = quickFilterRvpSelected;
    }

    public String getFirmNameSelected() {
        return firmNameSelected;
    }

    public void setFirmNameSelected(String firmNameSelected) {
        this.firmNameSelected = firmNameSelected;
    }

    public String getFirmIDSelected() {
        return firmIDSelected;
    }

    public void setFirmIDSelected(String firmIDSelected) {
        this.firmIDSelected = firmIDSelected;
    }

    public String getQuickFilterFirmIDSelected() {
        return quickFilterFirmIDSelected;
    }

    public void setQuickFilterFirmIDSelected(String quickFilterFirmIDSelected) {
        this.quickFilterFirmIDSelected = quickFilterFirmIDSelected;
    }

    public String getQuickFilterFirmNameSelected() {
        return quickFilterFirmNameSelected;
    }

    public void setQuickFilterFirmNameSelected(String quickFilterFirmNameSelected) {
        this.quickFilterFirmNameSelected = quickFilterFirmNameSelected;
    }

    public String getQuickFilterUsOrNySelected() {
        return quickFilterUsOrNySelected;
    }

    public void setQuickFilterUsOrNySelected(String quickFilterUsOrNySelected) {
        this.quickFilterUsOrNySelected = quickFilterUsOrNySelected;
    }

    public String getQuickFilterProductTypeSelected() {
		return quickFilterProductTypeSelected;
	}

	public void setQuickFilterProductTypeSelected(
			String quickFilterProductTypeSelected) {
		this.quickFilterProductTypeSelected = quickFilterProductTypeSelected;
	}
	
    public String getQuickFilterFundClassSelected() {
        return quickFilterFundClassSelected;
    }

    public void setQuickFilterFundClassSelected(String quickFilterFundClassSelected) {
        this.quickFilterFundClassSelected = quickFilterFundClassSelected;
    }

    public String getQuickFilterSelected() {
        return quickFilterSelected;
    }

    public void setQuickFilterSelected(String quickFilterSelected) {
        this.quickFilterSelected = quickFilterSelected;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getFinancialRepName() {
        return financialRepName;
    }

    public void setFinancialRepName(String financialRepName) {
        this.financialRepName = financialRepName;
    }

    public String getAssetRangeFrom() {
        return assetRangeFrom;
    }

    public void setAssetRangeFrom(String assetRangeFrom) {
        this.assetRangeFrom = assetRangeFrom;
    }

    public String getAssetRangeTo() {
        return assetRangeTo;
    }

    public void setAssetRangeTo(String assetRangeTo) {
        this.assetRangeTo = assetRangeTo;
    }

    public String getContractState() {
        return contractState;
    }

    public void setContractState(String contractState) {
        this.contractState = contractState;
    }

    public String getFundClassSelected() {
        return fundClassSelected;
    }

    public void setFundClassSelected(String fundClassSelected) {
        this.fundClassSelected = fundClassSelected;
    }

    public String getRvpSelected() {
        return rvpSelected;
    }

    public void setRvpSelected(String rvpSelected) {
        this.rvpSelected = rvpSelected;
    }

    public String getSalesRegionSelected() {
        return salesRegionSelected;
    }

    public void setSalesRegionSelected(String salesRegionSelected) {
        this.salesRegionSelected = salesRegionSelected;
    }

    public String getSalesDivisionSelected() {
        return salesDivisionSelected;
    }

    public void setSalesDivisionSelected(String salesDivisionSelected) {
        this.salesDivisionSelected = salesDivisionSelected;
    }

    public String getCsfFeatureSelected() {
        return csfFeatureSelected;
    }

    public void setCsfFeatureSelected(String csfFeatureSelected) {
        this.csfFeatureSelected = csfFeatureSelected;
    }

    public String getAsOfDateSelected() {
        return asOfDateSelected;
    }

    public void setAsOfDateSelected(String asOfDateSelected) {
        this.asOfDateSelected = asOfDateSelected;
    }

    public String getUsNySelected() {
        return usNySelected;
    }

    public void setUsNySelected(String usNySelected) {
        this.usNySelected = usNySelected;
    }

    
    public Boolean getShowHistoricalContractInfoFootnote() {
        return showHistoricalContractInfoFootnote;
    }

    public void setShowHistoricalContractInfoFootnote(Boolean showHistoricalContractInfoFootnote) {
        this.showHistoricalContractInfoFootnote = showHistoricalContractInfoFootnote;
    }

    public Boolean getShowDIFootnote() {
        return showDIFootnote;
    }

    public void setShowDIFootnote(Boolean showDIFootnote) {
        this.showDIFootnote = showDIFootnote;
    }

	public Boolean getShowPNAndPPContractCountAsOfLatestDateFootnote() {
		return showPNAndPPContractCountAsOfLatestDateFootnote;
	}

	public void setShowPNAndPPContractCountAsOfLatestDateFootnote(
			Boolean showPNAndPPContractCountAsOfLatestDateFootnote) {
		this.showPNAndPPContractCountAsOfLatestDateFootnote = showPNAndPPContractCountAsOfLatestDateFootnote;
	}

	public Boolean getIsPageAccessed() {
        return isPageAccessed;
    }

    public void setIsPageAccessed(Boolean isPageAccessed) {
        this.isPageAccessed = isPageAccessed;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void resetQuickFilter() {
        this.setQuickFilterContractName(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterContractNumber(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterRvpSelected(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterFirmIDSelected(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterFirmNameSelected(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterFundClassSelected(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterProductTypeSelected(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterUsOrNySelected(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterSelected(BDConstants.SPACE_SYMBOL);

    }
    
    /**
     * This method will reset advanced filter criteria's
     * 
     */
    public void resetAdvancedFilter() {
        this.setContractName(BDConstants.SPACE_SYMBOL);
        this.setContractNumber(BDConstants.SPACE_SYMBOL);
        this.setContractState(BDConstants.SPACE_SYMBOL);
        this.setAssetRangeFrom(BDConstants.SPACE_SYMBOL);
        this.setAssetRangeTo(BDConstants.SPACE_SYMBOL);
        this.setFinancialRepName(BDConstants.SPACE_SYMBOL);
        this.setRvpSelected(BDConstants.SPACE_SYMBOL);
        this.setFundClassSelected(BDConstants.SPACE_SYMBOL);
        this.setUsNySelected(BDConstants.SPACE_SYMBOL);
        this.setSalesRegionSelected(BDConstants.SPACE_SYMBOL);
        this.setSalesDivisionSelected(BDConstants.SPACE_SYMBOL);
        this.setCsfFeatureSelected(BDConstants.SPACE_SYMBOL);
        this.setFirmIDSelected(BDConstants.SPACE_SYMBOL);
        this.setProductType(BDConstants.SPACE_SYMBOL);
    }
    
    public boolean hasNoContractsForDetailedBrokerReport() {
		return noContractsForDetailedBrokerReport;
	}

	public void setNoContractsForDetailedBrokerReport(
			boolean noContractsForDetailedBrokerReport) {
		this.noContractsForDetailedBrokerReport = noContractsForDetailedBrokerReport;
	}
	
	public List<RiaFeeDetailsVO> getRiaFeeDetailsVO() {
		return riaFeeDetailsVO;
	}

	public void setRiaFeeDetailsVO(List<RiaFeeDetailsVO> riaFeeDetailsVO) {
		this.riaFeeDetailsVO = riaFeeDetailsVO;
	}

	public List<RiaFeeRangeVO> getRiaFeeRangeVO() {
		return riaFeeRangeVO;
	}

	public void setRiaFeeRangeVO(List<RiaFeeRangeVO> riaFeeRangeVO) {
		this.riaFeeRangeVO = riaFeeRangeVO;
	}

	public String getProductType() {
			return productType;
	}

	public void setProductType(String productType) {
			this.productType = productType;
	}
	 
	public boolean isCompensationSectionDisplayed() {
		return !BDConstants.DISCONTINUED_TAB.equals(getCurrentTab());
	}
	
	public boolean isRiaSectionDisplayed() {
		return !BDConstants.DISCONTINUED_TAB.equals(getCurrentTab());
	}
	
	public Collection<String> getLegends() {
		return this.legends;
	}
	
	public void setLegends(Collection<String> legends) {
		this.legends = legends;
	}
	
	public Collection<String> getRiaLegends() {
		return riaLegends;
	}

	public void setRiaLegends(Collection<String> riaLegends) {
		this.riaLegends = riaLegends;
	}

	public boolean isLevel1User() {
		return isLevel1User;
	}

	public void setLevel1User(boolean isLevel1User) {
		this.isLevel1User = isLevel1User;
	}
	
	public boolean isFiduciarySectionDisplayed() {
		return !BDConstants.DISCONTINUED_TAB.equals(getCurrentTab());
	}
	
	public Collection<String> getFiduciaryServicesTabLegends() {
		return fiduciaryServicesTabLegends;
	}

	public void setFiduciaryServicesTabLegends(Collection<String> fiduciaryServicesTabLegends) {
		this.fiduciaryServicesTabLegends = fiduciaryServicesTabLegends;
	}
	
}
