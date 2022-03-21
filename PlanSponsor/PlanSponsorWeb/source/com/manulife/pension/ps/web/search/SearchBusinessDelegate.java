package com.manulife.pension.ps.web.search;

import java.io.Serializable;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.service.searchengine.SearchFacade;
import com.manulife.pension.service.searchengine.SearchFacadeHome;
import com.manulife.pension.service.searchengine.SearchParameter;
import com.manulife.pension.service.searchengine.SearchResults;
import com.manulife.pension.service.searchengine.SubsetSearchResultsByCategory;
import com.manulife.pension.util.log.LogUtility;

/**
 * Acts as the intermediary between the Struts action that requests the search and
 * the EJB that performs it.
 *
 * It has the responsability to obtain a reference to an EJB and to call the search
 * methods of that bean. It also keeps the reference to the bean for future use, but
 * will obtain a new one if it expires.
 *
 * @author   Jean-Francis Baril
 **/
public class SearchBusinessDelegate implements Serializable {

		private static Logger logger = Logger.getLogger(SearchBusinessDelegate.class);
		private transient InitialContext ctx;
		private SearchFacade searchFacade;

		private Object objref;

		/**
		 * Obtains a reference to a SearchFacade EJB, and then calls the search function of that bean,
		 * returning a collection of results obtained from the bean. The information needed for that
		 * search is contained in the object received by the method and passed along to the bean.
		 *
		 * The method is synchronized to avoid problems if it is called twice or more in rapid succession
		 * on the same instance of the class. (Such as if the user rapidly clicks several time on a link
		 * or a new browser window is opened with the same session). This ensures that only one set of result
		 * is returned.
		 *
		 * @param searchParameter
		 *        The SearchParameter object that will be passed to the SearchFacadeBean in
		 *        order to perform the search.
		 *
		 *
		 * @return
		 *        A Collection object that contains SearchResultsByCategory objects, which
		 *        are the results from the search.
		 *
		 *
		 **/

		public synchronized SearchResults searchResultsByCriteria(SearchParameter searchParameter) {
				SearchResults searchResults = null;
				java.util.Collection temp;
				try {
						//Obtain a reference to an EJB if the current one is null
						if(ctx == null){
								ctx = new InitialContext();
						}
						if(objref == null){
							objref = ctx.lookup("com.manulife.pension.service.searchengine.SearchFacadeHome");
						}

						if (searchFacade == null) {
								SearchFacadeHome searchFacadeHome = (SearchFacadeHome) PortableRemoteObject.narrow(objref, SearchFacadeHome.class);
								searchFacade = searchFacadeHome.create();
						}


				if (logger.isDebugEnabled()) {
						logger.debug("Inside delegate, contract number: " + searchParameter.getSearchCriteria().getContractNumber());
						logger.debug("Inside delegate, user is enrolled: " + searchParameter.getSearchCriteria().isUserEnrolled());
				}

						try {
								//Obtain the result of the search from the bean
								searchResults = searchFacade.searchResultsByCriteria(searchParameter);
						}
						catch(java.rmi.NoSuchObjectException ex) {
								SearchFacadeHome searchFacadeHome = (SearchFacadeHome) PortableRemoteObject.narrow(objref, SearchFacadeHome.class);
								searchFacade = searchFacadeHome.create();
								//Obtain the result of the search from the bean
								searchResults = searchFacade.searchResultsByCriteria(searchParameter);
						}

				}
				catch(java.rmi.RemoteException ex) {
						LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "SearchBusinessDelegate", "searchResultsByCriteria", "EJB container threw remote exception"));
				}
				catch(javax.naming.NamingException ex){
						LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "SearchBusinessDelegate", "searchResultsByCriteria", "EJB container threw naming exception"));
				}
				catch(javax.ejb.CreateException ex){
						LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "SearchBusinessDelegate", "searchResultsByCriteria", "EJB container threw create exception"));
				}

				return searchResults;
		}

		/**
		 * Obtains a new reference for a SearchFacade EJB if there is no existing one, and calls the method
		 * of that bean that will return search results for a specific category of items. It will return
		 * a value object containing those results and information on them.
		 *
		 * This method is synchronized to avoid problems if it is called twice or more in close succession,
		 * such as if the user clicks several times on the URL that triggers the action. Synchronization ensures
		 * only one set of results will be returned.
		 *
		 * @param searchParameter
		 *        The SearchParameter object that will be passed to the SearchFacadeBean in
		 *        order to perform the search.
		 *
		 *
		 * @return
		 *        A SubsetSearchResultsByCategory value object that contains the results of the search as well
		 *        information on those results.
		 *
		 *
		 **/


		public synchronized SubsetSearchResultsByCategory searchResultsByCategory(SearchParameter searchParameter) {
				SubsetSearchResultsByCategory searchResults = null;

				try {
						//Obtain a reference to an EJB if the current one is null
						if(ctx == null){
								ctx = new InitialContext();
						}
						if(objref == null){
							objref = ctx.lookup("java:comp/env/ejb/SearchFacadeHome");
						}

						if (searchFacade == null) {
								SearchFacadeHome searchFacadeHome = (SearchFacadeHome) PortableRemoteObject.narrow(objref, SearchFacadeHome.class);
								searchFacade = searchFacadeHome.create();
						}

						//Obtain the results of the search for a specific category from the bean
						searchResults = searchFacade.searchResultsByIntervalForCategory(searchParameter);

				} catch (java.rmi.RemoteException ex) {
						LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "SearchBusinessDelegate", "searchResultsByCategory", "EJB container threw remote exception"));
				} catch(javax.naming.NamingException ex){
						LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "SearchBusinessDelegate", "searchResultsByCategory", "EJB container threw naming exception"));
				} catch(javax.ejb.CreateException ex){
						LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "SearchBusinessDelegate", "searchResultsByCategory", "EJB container threw create exception"));
				}
				return searchResults;
		}

		/**
		 * removes the stateful session bean that this object is referencing
		 */
		public void cleanUp() {
			if ( searchFacade != null ) {
				try {
					searchFacade.remove();
				} catch (javax.ejb.RemoveException ex) {
					LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "SearchBusinessDelegate", "cleanUp", "EJB container threw remove exception"));
				} catch (java.rmi.RemoteException ex) {
					LogUtility.logSystemException(Constants.PS_APPLICATION_ID,new SystemException(ex, "SearchBusinessDelegate", "cleanUp", "EJB container threw remote exception"));
				}
				searchFacade = null;
			}
		}
}
