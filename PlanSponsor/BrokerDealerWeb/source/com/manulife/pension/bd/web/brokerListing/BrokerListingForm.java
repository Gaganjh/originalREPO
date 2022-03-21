package com.manulife.pension.bd.web.brokerListing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.LabelInfoBean;

/**
 * This is the Action Form for Broker Listing Report.
 * 
 * @author HArlomte
 * 
 */
public class BrokerListingForm extends BaseReportForm {

    private static final long serialVersionUID = 3806381792347147725L;

    private String asOfDateSelected;
    
    // Advance Filter Items Selected
    private String financialRepName;

    private String bdFirmID;

    private String bdFirmName;

    private String cityName;

    private String stateCode;

    private String zipCode;

    private String producerCode;

    private String rvpName;

    private String salesRegion;

    private String salesDivision;

    // Quick Filter Items selected
    private String quickFilterFinancialRepName;

    private String quickFilterBDFirmID;
    
    private String quickFilterBDFirmName;
    
    private String quickFilterCityName;
    
    private String quickFilterStateCode;
    
    private String quickFilterZipCode;

    private String quickFilterProducerCode;

    private String quickFilterRVPName;

    private String quickFilterSalesRegion;

    private String quickFilterSalesDivision;
    
    // This variable will let us know if quick filter was submitted or advance filter.
    private Boolean fromQuickFilter;

    // Quick Filter related Search Criteria.
    private String quickFilterSelected;

    // This has a list of applicable filters.
    private Map<String, LabelInfoBean> filtersMap;

    // This variable will hold info whether the advance filter should be shown or not.
    private Boolean showAdvanceFilter;
    
    // This indicator will be set to true after the page is accessed for the first time. This was
    // created mainly because, the requirement was to log the Page access into MRL whenever the user
    // accesses the page in default view or when accessed thru a bookmark. Finding out
    // if the user is accessing the page for the first time thru a bookmark was difficult. Hence,
    // this indicator was used to tell if the page is being accessed for the first time or not thru
    // a bookmark.
    private Boolean isPageAccessed;
    
    /**
     * This method returns only those quick filters that are enabled.
     * 
     * @return - a list of quick filters that are enabled.
     */
    public List<LabelValueBean> getEnabledQuickFiltersList() {
        List<LabelValueBean> quickFiltersList = new ArrayList<LabelValueBean>();
        
        ArrayList<String> allFilters = new ArrayList<String>(1);
        allFilters.add(BDConstants.FILTER_BLANK_ID);
        allFilters.addAll(BrokerListingUtility.allBrokerListingFilters);
        
        for (String filterKey : allFilters) {
            if (isFilterEnabled(filterKey)) {
                quickFiltersList.add(new LabelValueBean(filtersMap.get(filterKey).getTitle(),
                        filtersMap.get(filterKey).getId()));
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
    public Boolean isFilterEnabled(String fieldKey) {
        return (Boolean) (filtersMap.get(fieldKey) == null ? Boolean.FALSE : filtersMap.get(
                fieldKey).getEnabled());
    }
    
    public String getFinancialRepName() {
        return financialRepName;
    }

    public void setFinancialRepName(String financialRepName) {
        this.financialRepName = financialRepName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getProducerCode() {
        return producerCode;
    }

    public void setProducerCode(String producerCode) {
        this.producerCode = producerCode;
    }

    public String getRvpName() {
        return rvpName;
    }

    public void setRvpName(String rvpName) {
        this.rvpName = rvpName;
    }

    public String getSalesRegion() {
        return salesRegion;
    }

    public void setSalesRegion(String salesRegion) {
        this.salesRegion = salesRegion;
    }

    public String getSalesDivision() {
        return salesDivision;
    }

    public void setSalesDivision(String salesDivision) {
        this.salesDivision = salesDivision;
    }

    public String getQuickFilterFinancialRepName() {
        return quickFilterFinancialRepName;
    }

    public void setQuickFilterFinancialRepName(String quickFilterFinancialRepName) {
        this.quickFilterFinancialRepName = quickFilterFinancialRepName;
    }

    public String getQuickFilterCityName() {
        return quickFilterCityName;
    }

    public void setQuickFilterCityName(String quickFilterCityName) {
        this.quickFilterCityName = quickFilterCityName;
    }

    public String getQuickFilterStateCode() {
        return quickFilterStateCode;
    }

    public void setQuickFilterStateCode(String quickFilterStateCode) {
        this.quickFilterStateCode = quickFilterStateCode;
    }

    public String getQuickFilterZipCode() {
        return quickFilterZipCode;
    }

    public void setQuickFilterZipCode(String quickFilterZipCode) {
        this.quickFilterZipCode = quickFilterZipCode;
    }

    public String getQuickFilterProducerCode() {
        return quickFilterProducerCode;
    }

    public void setQuickFilterProducerCode(String quickFilterProducerCode) {
        this.quickFilterProducerCode = quickFilterProducerCode;
    }

    public String getQuickFilterRVPName() {
        return quickFilterRVPName;
    }

    public void setQuickFilterRVPName(String quickFilterRVPName) {
        this.quickFilterRVPName = quickFilterRVPName;
    }

    public String getQuickFilterSalesRegion() {
        return quickFilterSalesRegion;
    }

    public void setQuickFilterSalesRegion(String quickFilterSalesRegion) {
        this.quickFilterSalesRegion = quickFilterSalesRegion;
    }

    public String getQuickFilterSalesDivision() {
        return quickFilterSalesDivision;
    }

    public void setQuickFilterSalesDivision(String quickFilterSalesDivision) {
        this.quickFilterSalesDivision = quickFilterSalesDivision;
    }

    public Boolean getFromQuickFilter() {
        return fromQuickFilter;
    }

    public void setFromQuickFilter(Boolean fromQuickFilter) {
        this.fromQuickFilter = fromQuickFilter;
    }

    public Map<String, LabelInfoBean> getFiltersMap() {
        return filtersMap;
    }

    public void setFiltersMap(Map<String, LabelInfoBean> filtersMap) {
        this.filtersMap = filtersMap;
    }

    public String getAsOfDateSelected() {
        return asOfDateSelected;
    }

    public void setAsOfDateSelected(String asOfDateSelected) {
        this.asOfDateSelected = asOfDateSelected;
    }

    public Boolean getShowAdvanceFilter() {
        return showAdvanceFilter;
    }

    public void setShowAdvanceFilter(Boolean showAdvanceFilter) {
        this.showAdvanceFilter = showAdvanceFilter;
    }

    public String getQuickFilterSelected() {
        return quickFilterSelected;
    }

    public void setQuickFilterSelected(String quickFilterSelected) {
        this.quickFilterSelected = quickFilterSelected;
    }

    public String getBdFirmID() {
        return bdFirmID;
    }

    public void setBdFirmID(String bdFirmID) {
        this.bdFirmID = bdFirmID;
    }

    public String getBdFirmName() {
        return bdFirmName;
    }

    public void setBdFirmName(String bdFirmName) {
        this.bdFirmName = bdFirmName;
    }

    public String getQuickFilterBDFirmID() {
        return quickFilterBDFirmID;
    }

    public void setQuickFilterBDFirmID(String quickFilterBDFirmID) {
        this.quickFilterBDFirmID = quickFilterBDFirmID;
    }

    public String getQuickFilterBDFirmName() {
        return quickFilterBDFirmName;
    }

    public void setQuickFilterBDFirmName(String quickFilterBDFirmName) {
        this.quickFilterBDFirmName = quickFilterBDFirmName;
    }

    public Boolean getIsPageAccessed() {
        return isPageAccessed;
    }

    public void setIsPageAccessed(Boolean isPageAccessed) {
        this.isPageAccessed = isPageAccessed;
    }

    /**
     * This method is used to reset the quick Filter items.
     */
    public void resetQuickFilter() {
        this.setQuickFilterFinancialRepName(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterBDFirmID(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterBDFirmName(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterCityName(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterStateCode(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterZipCode(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterProducerCode(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterRVPName(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterSalesRegion(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterSalesDivision(BDConstants.SPACE_SYMBOL);
        this.setQuickFilterSelected(BDConstants.SPACE_SYMBOL);
    }

}
