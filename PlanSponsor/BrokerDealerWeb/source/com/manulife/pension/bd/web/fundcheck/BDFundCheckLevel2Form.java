package com.manulife.pension.bd.web.fundcheck;

import java.util.List;
import java.util.Map;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.fundcheck.model.FundCheckDocumentInfo;
import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * This class is used for level2 version of fundcheck page.
 * @author Ilam
 *
 */

public class BDFundCheckLevel2Form extends AutoForm {

	private static final long serialVersionUID = 1L;

	private boolean fundCheckPDFAvailable = false;
	
	private List<List<FundCheckDocumentInfo>> fundCheckInfo;
	
	private String selectedSeason;
	
	private String selectedYear;

	private String selectedPDFCode;
	
	private boolean showProducerCode;
	
	private Map<String, String> searchTypeMap;
	
	private String searchType;
	
	private String searchInput;
	
	private List<FundCheckDocumentInfo> searchResultFundCheckInfo;
	
	private boolean showSearchResults;
	
	private boolean noSearchResults = false;
	
	private boolean searchNameList;
	
	private Map<String,String> nameMap;
	
	private String selectedId;
	
	private String selectedName;	

	private String springSeason = BDConstants.SEASON4;
	
	private String fallSeason = BDConstants.SEASON9;
	
	private String selectedPDF = BDConstants.FUNDCHECK_INPUT_PRODUCER_CODE_KEY;
	
	private boolean fundCheckPDFAvailableForSearch = true;
	
	private boolean showCompanyId = false;
	
	private String selectedCompanyId;
	
	private boolean addMoreFilters = false;
	
	private int nameListSize;
	
	private String selectedLanguage;
	
	private String participantNoticeInd;
	
	private String searchActionType;


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
	 * Returns a list that contains a list of FundCheckDocumentInfo objects
	 * 
	 * @return the fundCheckInfo
	 */
	public List<List<FundCheckDocumentInfo>> getFundCheckInfo() {
		return fundCheckInfo;
	}

	/**
	 * Sets the FundCheckDocumentInfo list
	 * 
	 * @param fundCheckInfo the fundCheckInfo to set
	 */
	public void setFundCheckInfo(List<List<FundCheckDocumentInfo>> fundCheckInfo) {
		this.fundCheckInfo = fundCheckInfo;
	}

	/**
	 * Returns the season of the PDF to be opened
	 * 
	 * @return the selectedSeason
	 */
	public String getSelectedSeason() {
		return selectedSeason;
	}

	/**
	 * Sets the season of the PDF to be opened
	 * 
	 * @param selectedSeason the selectedSeason to set
	 */
	public void setSelectedSeason(String selectedSeason) {
		this.selectedSeason = selectedSeason;
	}

	/**
	 * Returns the year of the PDF to be opened
	 * 
	 * @return the selectedYear
	 */
	public String getSelectedYear() {
		return selectedYear;
	}

	/**
	 * Sets the year of the PDF to be opened
	 * 
	 * @param selectedYear the selectedYear to set
	 */
	public void setSelectedYear(String selectedYear) {
		this.selectedYear = selectedYear;
	}

	/**
	 * Returns a flag to identify whether to show the producer code or not
	 * 
	 * @return the showProducerCode
	 */
	public boolean isShowProducerCode() {
		return showProducerCode;
	}

	/**
	 * Sets the flag to identify whether to show the producer code or not
	 * 
	 * @param showProducerCode the showProducerCode to set
	 */
	public void setShowProducerCode(boolean showProducerCode) {
		this.showProducerCode = showProducerCode;
	}

	/**
	 * Returns a Map for the search type drop down
	 * 
	 * @return the searchTypeMap
	 */
	public Map<String, String> getSearchTypeMap() {
		return searchTypeMap;
	}

	/**
	 * Sets a Map of values for the search type drop down
	 * 
	 * @param searchTypeMap the searchTypeMap to set
	 */
	public void setSearchTypeMap(Map<String, String> searchTypeMap) {
		this.searchTypeMap = searchTypeMap;
	}

	/**
	 * Returns the search type
	 * 
	 * @return the searchType
	 */
	public String getSearchType() {
		return searchType;
	}

	/**
	 * Sets the search type
	 * 
	 * @param searchType the searchType to set
	 */
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	/**
	 * Returns the search input
	 * 
	 * @return the searchInput
	 */
	public String getSearchInput() {
		return searchInput;
	}

	/**
	 * Sets the search input
	 * 
	 * @param searchInput the searchInput to set
	 */
	public void setSearchInput(String searchInput) {
		this.searchInput = searchInput;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isShowSearchResults() {
		return showSearchResults;
	}
	/**
	 * 
	 * @param showSearchResults
	 */
	public void setShowSearchResults(boolean showSearchResults) {
		this.showSearchResults = showSearchResults;
	}
	/**
	 * 
	 * @return
	 */
	public boolean isNoSearchResults() {
		return noSearchResults;
	}
	/**
	 * 
	 * @param noSearchResults
	 */
	public void setNoSearchResults(boolean noSearchResults) {
		this.noSearchResults = noSearchResults;
	}

	public List<FundCheckDocumentInfo> getSearchResultFundCheckInfo() {
		return searchResultFundCheckInfo;
	}

	public void setSearchResultFundCheckInfo(
			List<FundCheckDocumentInfo> searchResultFundCheckInfo) {
		this.searchResultFundCheckInfo = searchResultFundCheckInfo;
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

	public String getSelectedPDF() {
		return selectedPDF;
	}

	public void setSelectedPDF(String selectedPDF) {
		this.selectedPDF = selectedPDF;
	}

	public boolean isFundCheckPDFAvailableForSearch() {
		return fundCheckPDFAvailableForSearch;
	}

	public void setFundCheckPDFAvailableForSearch(
			boolean fundCheckPDFAvailableForSearch) {
		this.fundCheckPDFAvailableForSearch = fundCheckPDFAvailableForSearch;
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

	/**
	 * @return the selectedPDFCode
	 */
	public String getSelectedPDFCode() {
		return selectedPDFCode;
	}

	/**
	 * @param selectedPDFCode the selectedPDFCode to set
	 */
	public void setSelectedPDFCode(String selectedPDFCode) {
		this.selectedPDFCode = selectedPDFCode;
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
