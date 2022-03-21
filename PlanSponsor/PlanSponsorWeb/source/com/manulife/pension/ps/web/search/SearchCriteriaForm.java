package com.manulife.pension.ps.web.search;

import java.net.URLEncoder;

import com.manulife.pension.ezk.web.ActionForm;

/**
 * A sub-form, contained by the SearchForm. This form is specific to the criteria
 * of the search and contains information to perform the query.
 *
 * @author   Jean-Francis Baril
 * @see SearchForm
 **/
public class SearchCriteriaForm implements ActionForm{

		/**
		 * The search query as entered by the user in the web page's textbox
		 */
		private String searchCriteria;

		public String getSearchCriteria(){
				return searchCriteria;
		}

		public String getSearchCriteriaFormatted(){
				return URLEncoder.encode(searchCriteria);
		}

		public void setSearchCriteria(String searchCriteria){
				this.searchCriteria = searchCriteria;
		}

}