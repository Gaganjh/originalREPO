package com.manulife.pension.bd.web.fundcheck;

import java.util.List;
import java.util.Map;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.fundcheck.model.FundCheckDocumentInfo;
import com.manulife.pension.platform.web.controller.AutoForm;

public class BDFundCheckInternalForm extends AutoForm {
	
	private static final long serialVersionUID = 1L;

	private Map<String, String> searchTypeMap;
	
	private Map<String, String> searchTypeMapL2;

	private boolean fundCheckPDFAvailableForSearch = true;	

	private String searchType;
	
	private boolean searchInternalIndicator;
	
	private String searchTypeL2;	
	
	private String searchInput;
	
	private String searchInputL2;	

	private boolean showSearchResults;
	
	private List<List<FundCheckDocumentInfo>> searchResultFundCheckInfo;
	
	private List<FundCheckDocumentInfo> searchResultFundCheckInfoL2;
		
	private boolean fundCheckPDFAvailable = true;
	
	private List<List<FundCheckDocumentInfo>> fundCheckInfo;
	
	private String selectedSeason;
	
	private String selectedYear;
	
	private String selectedPDF;
	
	private boolean showProducerCode;
	
	private boolean searchNameList;
	
	private Map<String,String> nameMap;
	
	private Map<String,String> nameMapL2;	

	private String selectedId;
	
	private String selectedName;
	
	private String springSeason = BDConstants.SEASON4;
	
	private String fallSeason = BDConstants.SEASON9;
	
	private boolean noSearchResults = false;
	
	private boolean showCompanyId = false;
	
	private String selectedCompanyId;
	
	private boolean addMoreFilters = false;
	
	private int nameListSize;

	private int nameListSizeL2;
	
	private String selectedLanguage;
	
	private String participantNoticeInd;
	
	private String searchActionType;

	/**
	 * @return the searchTypeMap
	 */
	public Map<String, String> getSearchTypeMap() {
		return searchTypeMap;
	}

	/**
	 * @param searchTypeMap the searchTypeMap to set
	 */
	public void setSearchTypeMap(Map<String, String> searchTypeMap) {
		this.searchTypeMap = searchTypeMap;
	}
	
	public Map<String, String> getSearchTypeMapL2() {
		return searchTypeMapL2;
	}

	public void setSearchTypeMapL2(Map<String, String> searchTypeMapL2) {
		this.searchTypeMapL2 = searchTypeMapL2;
	}
	
	public String getSearchTypeL2() {
		return searchTypeL2;
	}

	public void setSearchTypeL2(String searchTypeL2) {
		this.searchTypeL2 = searchTypeL2;
	}
	

	public Map<String, String> getNameMapL2() {
		return nameMapL2;
	}

	public void setNameMapL2(Map<String, String> nameMapL2) {
		this.nameMapL2 = nameMapL2;
	}

	public int getNameListSizeL2() {
		return nameListSizeL2;
	}

	public void setNameListSizeL2(int nameListSizeL2) {
		this.nameListSizeL2 = nameListSizeL2;
	}
	

	public String getSearchInputL2() {
		return searchInputL2;
	}

	public void setSearchInputL2(String searchInputL2) {
		this.searchInputL2 = searchInputL2;
	}

	public boolean isFundCheckPDFAvailableForSearch() {
		return fundCheckPDFAvailableForSearch;
	}

	public void setFundCheckPDFAvailableForSearch(
			boolean fundCheckPDFAvailableForSearch) {
		this.fundCheckPDFAvailableForSearch = fundCheckPDFAvailableForSearch;
	}
	
	public List<FundCheckDocumentInfo> getSearchResultFundCheckInfoL2() {
		return searchResultFundCheckInfoL2;
	}

	public void setSearchResultFundCheckInfoL2(
			List<FundCheckDocumentInfo> searchResultFundCheckInfoL2) {
		this.searchResultFundCheckInfoL2 = searchResultFundCheckInfoL2;
	}

	/**
	 * @return the searchType
	 */
	public String getSearchType() {
		return searchType;
	}

	/**
	 * @param searchType the searchType to set
	 */
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	/**
	 * @return the searchInput
	 */
	public String getSearchInput() {
		return searchInput;
	}

	/**
	 * @param searchInput the searchInput to set
	 */
	public void setSearchInput(String searchInput) {
		this.searchInput = searchInput;
	}

	/**
	 * @return the showSearchResults
	 */
	public boolean isShowSearchResults() {
		return showSearchResults;
	}

	/**
	 * @param showSearchResults the showSearchResults to set
	 */
	public void setShowSearchResults(boolean showSearchResults) {
		this.showSearchResults = showSearchResults;
	}

	/**
	 * @return the searchResultFundCheckInfo
	 */
	public List<List<FundCheckDocumentInfo>> getSearchResultFundCheckInfo() {
		return searchResultFundCheckInfo;
	}

	/**
	 * @param searchResultFundCheckInfo the searchResultFundCheckInfo to set
	 */
	public void setSearchResultFundCheckInfo(
			List<List<FundCheckDocumentInfo>> searchResultFundCheckInfo) {
		this.searchResultFundCheckInfo = searchResultFundCheckInfo;
	}

	/**
	 * @return the fundCheckPDFAvailable
	 */
	public boolean isFundCheckPDFAvailable() {
		return fundCheckPDFAvailable;
	}

	/**
	 * @param fundCheckPDFAvailable the fundCheckPDFAvailable to set
	 */
	public void setFundCheckPDFAvailable(boolean fundCheckPDFAvailable) {
		this.fundCheckPDFAvailable = fundCheckPDFAvailable;
	}

	/**
	 * @return the fundCheckInfo
	 */
	public List<List<FundCheckDocumentInfo>> getFundCheckInfo() {
		return fundCheckInfo;
	}

	/**
	 * @param fundCheckInfo the fundCheckInfo to set
	 */
	public void setFundCheckInfo(List<List<FundCheckDocumentInfo>> fundCheckInfo) {
		this.fundCheckInfo = fundCheckInfo;
	}

	/**
	 * @return the selectedSeason
	 */
	public String getSelectedSeason() {
		return selectedSeason;
	}

	/**
	 * @param selectedSeason the selectedSeason to set
	 */
	public void setSelectedSeason(String selectedSeason) {
		this.selectedSeason = selectedSeason;
	}

	/**
	 * @return the selectedYear
	 */
	public String getSelectedYear() {
		return selectedYear;
	}

	/**
	 * @param selectedYear the selectedYear to set
	 */
	public void setSelectedYear(String selectedYear) {
		this.selectedYear = selectedYear;
	}

	/**
	 * @return the selectedPDF
	 */
	public String getSelectedPDF() {
		return selectedPDF;
	}

	/**
	 * @param selectedPDF the selectedPDF to set
	 */
	public void setSelectedPDF(String selectedPDF) {
		this.selectedPDF = selectedPDF;
	}

	/**
	 * @return the showProducerCode
	 */
	public boolean isShowProducerCode() {
		return showProducerCode;
	}

	/**
	 * @param showProducerCode the showProducerCode to set
	 */
	public void setShowProducerCode(boolean showProducerCode) {
		this.showProducerCode = showProducerCode;
	}

	/**
	 * @return the searchNameList
	 */
	public boolean isSearchNameList() {
		return searchNameList;
	}

	/**
	 * @param searchNameList the searchNameList to set
	 */
	public void setSearchNameList(boolean searchNameList) {
		this.searchNameList = searchNameList;
	}

	/**
	 * @return the nameMap
	 */
	public Map<String, String> getNameMap() {
		return nameMap;
	}

	/**
	 * @param nameMap the nameMap to set
	 */
	public void setNameMap(Map<String, String> nameMap) {
		this.nameMap = nameMap;
	}

	/**
	 * @return the selectedId
	 */
	public String getSelectedId() {
		return selectedId;
	}

	/**
	 * @param selectedId the selectedId to set
	 */
	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}

	/**
	 * @return the selectedName
	 */
	public String getSelectedName() {
		return selectedName;
	}

	/**
	 * @param selectedName the selectedName to set
	 */
	public void setSelectedName(String selectedName) {
		this.selectedName = selectedName;
	}

	/**
	 * @return the springSeason
	 */
	public String getSpringSeason() {
		return springSeason;
	}

	/**
	 * @param springSeason the springSeason to set
	 */
	public void setSpringSeason(String springSeason) {
		this.springSeason = springSeason;
	}

	/**
	 * @return the fallSeason
	 */
	public String getFallSeason() {
		return fallSeason;
	}

	/**
	 * @param fallSeason the fallSeason to set
	 */
	public void setFallSeason(String fallSeason) {
		this.fallSeason = fallSeason;
	}

	public boolean isNoSearchResults() {
		return noSearchResults;
	}

	public void setNoSearchResults(boolean noSearchResults) {
		this.noSearchResults = noSearchResults;
	}

	public boolean isShowCompanyId() {
		return showCompanyId;
	}

	public void setShowCompanyId(boolean showCompanyId) {
		this.showCompanyId = showCompanyId;
	}

	public String getSelectedCompanyId() {
		return selectedCompanyId;
	}

	public void setSelectedCompanyId(String selectedCompanyId) {
		this.selectedCompanyId = selectedCompanyId;
	}

	/**
	 * @return the addMoreFilters
	 */
	public boolean isAddMoreFilters() {
		return addMoreFilters;
	}

	/**
	 * @param addMoreFilters the addMoreFilters to set
	 */
	public void setAddMoreFilters(boolean addMoreFilters) {
		this.addMoreFilters = addMoreFilters;
	}
	
	/**
	 * @return the nameListSize
	 */
	public int getNameListSize() {
		return nameListSize;
	}

	/**
	 * @param nameListSize the nameListSize to set
	 */
	public void setNameListSize(int nameListSize) {
		this.nameListSize = nameListSize;
	}

	public boolean isSearchInternalIndicator() {
		return searchInternalIndicator;
	}

	public void setSearchInternalIndicator(boolean searchInternalIndicator) {
		this.searchInternalIndicator = searchInternalIndicator;
	}

	public String getSelectedLanguage() {
		return selectedLanguage;
	}

	public void setSelectedLanguage(String selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}

	public String getParticipantNoticeInd() {
		return participantNoticeInd;
	}

	public void setParticipantNoticeInd(String participantNoticeInd) {
		this.participantNoticeInd = participantNoticeInd;
	}
	
	public String getSearchActionType() {
		return searchActionType;
	}
	
	public void setSearchActionType(String searchActionType) {
		this.searchActionType = searchActionType;
	}

}
