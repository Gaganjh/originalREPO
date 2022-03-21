package com.manulife.pension.ps.web.search;

import java.io.Serializable;

import com.manulife.pension.ps.web.controller.PsForm;





/**
 * The top level form bean attached to the SearchControllerAction Struts action.
 * It contains two sub-forms for data regarding the search criteria and the search
 * results, respectively. It also contains general data about the number of results
 * to be displayed per page.
 *
 * @author   Jean-Francis Baril
 * @see SearchCriteriaForm, SearchResultsForm
 **/

public class SearchForm extends PsForm implements Serializable{

    /**
     * The form that will be populated when the user requests a search
     */
		private SearchCriteriaForm searchCriteriaForm;

		/**
     * The form that will contain the results to be displayed on the results pages
     */
    private SearchResultsForm searchResultsForm;

		/**
     * The number of results to be displayed per category on the first results page
     */
    private int numberResultsDisplayInit;

		/**
     * The number of results to be displayed per page on the second results page
     */
    private int numberResultsDisplayCategory;

    /**
     * The type of header to be displayed on the result page
     */
    private String requestedHeaderType;

    /**
     * The type of footer to be displayed on the result page
     */
    private String requestedFooterType;

		public SearchForm(){
				this.searchCriteriaForm = new SearchCriteriaForm();
				this.searchResultsForm = new SearchResultsForm();
		}

		public SearchCriteriaForm getSearchCriteriaForm(){
						return searchCriteriaForm;
				}

		public void setSearchCriteriaForm(SearchCriteriaForm searchCriteriaForm){
						this.searchCriteriaForm = searchCriteriaForm;
				}

		public SearchResultsForm getSearchResultsForm(){
						return searchResultsForm;
				}

		public void setSearchResultsForm(SearchResultsForm searchResultsForm){
						this.searchResultsForm = searchResultsForm;
				}

		public void setNumberResultsDisplayInit(int numberResultsDisplayInit) {
				this.numberResultsDisplayInit = numberResultsDisplayInit;
		}
		public int getNumberResultsDisplayInit() {
				return numberResultsDisplayInit;
		}

		public void setNumberResultsDisplayCategory(int numberResultsDisplayCategory) {
				this.numberResultsDisplayCategory = numberResultsDisplayCategory;
		}

		public int getNumberResultsDisplayCategory() {
				return numberResultsDisplayCategory;
		}
    public String getRequestedHeaderType() {
      return requestedHeaderType;
    }
    public void setRequestedHeaderType(String requestedHeaderType) {
      this.requestedHeaderType = requestedHeaderType;
    }
    public String getRequestedFooterType() {
      return requestedFooterType;
    }
    public void setRequestedFooterType(String requestedFooterType) {
      this.requestedFooterType = requestedFooterType;
    }
}