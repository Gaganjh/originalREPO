package com.manulife.pension.ps.web.search;



import java.io.Serializable;
import java.util.Collection;

/**
 * A sub-form, contained by the SearchForm. This form is specific to the results
 * of the search and is used to tranfer them to the JSP for display. It also contains
 * information relating to those results and necessary for their proper display.
 *
 * @author   Jean-Francis Baril
 * @see SearchForm
 **/

public class SearchResultsForm implements Serializable {

    public SearchResultsForm() {
        //initialize the collection
        searchResults = new java.util.ArrayList();
    }

		/**
		 * The category of the results provided by the form. It is only used on the second
		 * results page.
		 */
		private String category;

		/**
		 * The category well format for the result pages. It is only used on the second
		 * results page.
		 */
		private String categoryPrintName;

		/**
		 * A collection of SearchResult objects, each of which represents an actual result
		 * from the search.
		 */
		private Collection searchResults;

		/**
		 * The starting index of the results being provided by the form.
		 */
		private int currentIndex;

		/**
		 * The number of results, either total for the first results page, or
		 * for the category specified for the second results page. This is not
		 * the actual number of results in the Collection searchResults.
		 */
		private int nbrResults;

		/**
		 * Used on the second results page, indicating if there is a prior subset
		 * of results to this one.
		 */
		private boolean hasPrevious;

		/**
		 * Used on the second results page, indicating if there is a subsequent subset
		 * of results to this one.
		 */
		private boolean hasNext;

		public Collection getSearchResults(){
						return searchResults;
		}

		public void setSearchResults(Collection searchResults){
						this.searchResults = searchResults;
		}

		public String getCategory() {
				return category;
		}

		public void setCategory(String category) {
				this.category = category;
		}
		public int getCurrentIndex() {
				return currentIndex;
		}
		public void setCurrentIndex(int currentIndex) {
				this.currentIndex = currentIndex;
		}
		public boolean isHasNext() {
				return hasNext;
		}
		public void setHasNext(boolean hasNext) {
				this.hasNext = hasNext;
		}
		public boolean isHasPrevious() {
				return hasPrevious;
		}
		public void setHasPrevious(boolean hasPrevious) {
				this.hasPrevious = hasPrevious;
		}
		public int getNbrResults() {
			return nbrResults;
		}
		public void setNbrResults(int nbrResults) {
			this.nbrResults = nbrResults;
		}
		public String getCategoryPrintName() {
				return categoryPrintName;
		}
		public void setCategoryPrintName(String categoryPrintName) {
				this.categoryPrintName = categoryPrintName;
		}
}