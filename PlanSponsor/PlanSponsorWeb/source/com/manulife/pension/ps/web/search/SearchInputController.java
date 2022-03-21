package com.manulife.pension.ps.web.search;



import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.delegate.ServiceLocator;
import com.manulife.pension.delegate.ServiceLocatorException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.SecurityManager;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.taglib.search.UrlGenerator;
import com.manulife.pension.ps.web.taglib.search.UrlGeneratorFactory;
import com.manulife.pension.ps.web.taglib.search.UrlGeneratorNotFoundException;
import com.manulife.pension.ps.web.taglib.search.UrlGeneratorTagConfig;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorSearchInput;
import com.manulife.pension.reference.Reference;
import com.manulife.pension.service.searchengine.SearchCriteria;
import com.manulife.pension.service.searchengine.SearchParameter;
import com.manulife.pension.service.searchengine.SearchResult;
import com.manulife.pension.service.searchengine.SearchResults;
import com.manulife.pension.service.searchengine.SearchResultsByCategory;
import com.manulife.pension.service.searchengine.SubsetSearchResultsByCategory;
import com.manulife.pension.util.log.LogUtility;


/**
 * Controller that handles search requests from JSP pages. The action can handle
 * the intial search by keyword and return results for all category. It also handles
 * subsequent request for the search results belonging to a single category of items.
 *
 * @author   Jean-Francis Baril
 * @see SearchEngineConstants
 **/
@Controller
@RequestMapping(value="/search")
public class SearchInputController {

		private static Logger logger = Logger.getLogger(SearchInputController.class);
		private static final int NUMBER_RESULTS_DISPLAY_PER_CATEGORY = 10;
		private static final int NUMBER_RESULTS_DISPLAY_INIT = 10;
		private static final int NUMBER_MAX_QUERY_NUMBER = 500; 
		
		
		 @Autowired
	      private PSValidatorSearchInput psValidatorSearchInput;  
		 
		@ModelAttribute("searchCriteriaForm")
		public SearchCriteriaForm populateForm(){
			return new SearchCriteriaForm();
		} 
		
		public static HashMap<String,String> forwards = new HashMap<String,String>();
		static{

	  		forwards.put("searchInput","/search/searchInputForm.jsp" );
			forwards.put("searchOutput","/search/searchOutputForm.jsp" );
			forwards.put("searchByCategory","/search/searchResultsByCategory.jsp" );
		}

		/**
		 * Handles the request received from the JSP pages. It will send the initial search
		 * request to the business delegate to obtain results from all categories, or it will
		 * send a request to obtain a range of results from a specific category.
		 *
		 * The value of a request scope parameter with the name of "nextPage" will determine wether
		 * the action will obtain the initial search results, or the subsequent results for a given
		 * category. The value set by the SearchEngineConstants.INIT_RESULT_PAGE attribute will trigger
		 * the first possibility, while the value of SearchEngineConstants.CATEGORY_RESULT_PAGE will
		 * trigger the second.
		 *
		 * @param actionMapping
		 *        The action mapping obatined from the web.xml file of the web application
		 *
		 * @param actionForm
		 *        The form bean associated with this action, which contains the data for the search
		 *
		 * @param request
		 *        Standard parameter for a Struts action
		 *
		 * @param response
		 *         Standard parameter for a Struts action
		 *
		 * @return
		 *        ActionForward that will forward the user to the correct result page
		 *
		 * @exception IOException
		 *
		 * @exception ServletException
		 *        If a problem occurs in the execution of the servlet's operation
		 **/
		@RequestMapping(value="/searchInputAction",method={RequestMethod.GET,RequestMethod.POST})
		public String doExecute(@Valid @ModelAttribute("searchCriteriaForm") SearchCriteriaForm searchForm,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, javax.servlet.ServletException {

			//System.out.println("SearchControllerAction.doExecute()::TODO::retrofit exception handling of entire search web layer so that exceptions are passed to the error for display on broken page, at present the way the module works all exceptions are caught & logged , however the application is not allowed to break. Any defects thus may go unnoticed till very late in the testing process");

				String forward = null;
				if(bindingResult.hasErrors()){
		        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		        	if(errDirect!=null){
		             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("searchInput");//if input forward not //available, provided default
		        	}
		        } 
				
				SearchBusinessDelegate searchBusinessDelegate = this.getSearchBusinessDelegate(request);
				SearchForm searchInputForm = new SearchForm();
				//Recover parameters received from the page in the request
				String paramSearchCriteria = request.getParameter("searchCriteriaForm.searchCriteria");
				String category = request.getParameter("category");
				String categoryPrintName = request.getParameter("categoryPrintName");
				String nextPage = request.getParameter("nextPage");
				String requestedHeaderType = request.getParameter("requestedHeaderType");
				String requestedFooterType = request.getParameter("requestedFooterType");
				
				String giflVersion = getUserProfile(request).getCurrentContract().getGiflVersion();
				
				if (logger.isDebugEnabled()) {
					logger.log(Priority.DEBUG, "Action, nextPage value : " + nextPage);
				}

				int numberResultsDisplayInit = NUMBER_RESULTS_DISPLAY_INIT;
				// Can be null when action is called from third page and onward
				if(request.getParameter("numberResultsDisplayInit") != null){
						numberResultsDisplayInit = Integer.parseInt(request.getParameter("numberResultsDisplayInit"));
				}

				int numberResultsDisplayCategory = NUMBER_RESULTS_DISPLAY_PER_CATEGORY;
				// Can be null when action is called from third page and onward
				if(request.getParameter("numberResultsDisplayCategory") != null){
						numberResultsDisplayCategory = Integer.parseInt(request.getParameter("numberResultsDisplayCategory"));
				}
				int newIndex = 0;
				// Can be null if action is called from the first page
				if(request.getParameter("newIndex") != null) {
						newIndex = Integer.parseInt(request.getParameter("newIndex"));
				}

				boolean userFromNY = false;
                try {
                    userFromNY= !Constants.SITEMODE_USA.equals(ServiceLocator.getEnvironmentVariable(Constants.SITE_MODE));
                } catch (ServiceLocatorException ignore) {
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("userFromNY:"+userFromNY);
                }

				int contractNumber = 0;
				boolean userEnrolled = false;
                boolean definedBenefit = false;
                
				// Getting user profile information
				UserProfile profile = getUserProfile(request);
				if (profile != null) {
                     contractNumber = profile.getCurrentContract().getContractNumber();
                          userEnrolled = true;
                     definedBenefit = profile.getCurrentContract().isDefinedBenefitContract();
				}


				if (logger.isDebugEnabled()) {
					logger.debug("Inside action, contract number: " + contractNumber);
					logger.debug("Inside action, user is enrolle: " + userEnrolled);
				}

				// No request, return to initial page
				if (nextPage == null) {
						forward = forwards.get(SearchEngineConstants.INITIAL_PAGE_FORWARD);
				// User is requesting a new search
				} else if(nextPage.equals(SearchEngineConstants.INIT_RESULT_PAGE)){

						forward = forwards.get(SearchEngineConstants.INIT_RESULT_PAGE_FORWARD);

						SearchCriteria searchCriteria = new SearchCriteria(
                                paramSearchCriteria, Integer.toString(contractNumber),
                                userEnrolled, userFromNY, definedBenefit, giflVersion);

						//SearchParameter will carry the data needed to perform the search
						//SearchParameter searchParameter =  new SearchParameter(searchCriteria, numberResultsDisplayInit, numberResultsDisplayCategory);
						SearchParameter searchParameter =  new SearchParameter(searchCriteria, NUMBER_MAX_QUERY_NUMBER, numberResultsDisplayCategory);
						
						//check if MTA an pass list of excluded content
						ArrayList list = null;
						if(getUserProfile(request).getCurrentContract().isMta())
						{
							list = new ArrayList();
							//This is ugly, but we store the content types to exclude on the database search,
							//and the category definitions to exclude from the searchKeywordRules.xml
							
							//For the content database search
							list.add("LayoutPage");
							list.add("GuideArticle");
							
							//For the category definitions
							list.add("feduciary page");
							list.add("admin guide");
							list.add("Consolidation Services");
							if (logger.isDebugEnabled()) {
								logger.debug("contract is MTA excluding the following contracts ::");
								logger.debug(list.toString());
							}
						} else if (definedBenefit) {
                            list = new ArrayList();
                            // exclude fidiciary for defined benefit
                            list.add("feduciary page");
                            list.add("Consolidation Services");
                            if (logger.isDebugEnabled()) {
                                logger.debug("contract is Defined Benefit excluding the following contracts ::");
                                logger.debug(list.toString());
                            }                           
						}
						else
						{
							if (logger.isDebugEnabled()) {
								logger.debug("contract is not MTA and not Defined Benefit");
							}
						}
						searchParameter.setExcludedContentList(list);
						if (logger.isDebugEnabled()) {
								logger.log(Priority.DEBUG, "Inside action, before search");
						}

						//Performing search
						SearchResults searchResults = searchBusinessDelegate.searchResultsByCriteria(searchParameter);

						//**************
						Iterator results = searchResults.getSearchResults().iterator();
						// if there are any redords that are not going to be diplayed
						// we have to cont them and remov ethem from the list
						int cntToBeRemoved = 0;
						while ( results.hasNext() ) 
						{
							SearchResultsByCategory resultsByCategory = (SearchResultsByCategory)results.next();
							Collection resultsForCategory = resultsByCategory.getResultsForCategory();
							Iterator categoryResults = resultsForCategory.iterator();
							int cntToBeRemovedForCategory = 0;
							while ( categoryResults.hasNext() )
							{
								try {
									Object resultBean = categoryResults.next(); 
									Reference reference= (Reference)PropertyUtils.getProperty(resultBean, "reference");
									
									
						            UrlGenerator urlGenerator= UrlGeneratorFactory.getInstance().getGenerator(reference);

						            String url= urlGenerator.generateUrl(reference, 
						            		UrlGeneratorTagConfig.getInstance().getProperties( request.getSession(false), 
						            				request), (SearchResult)resultBean);

						            if (  !url.startsWith("http") &&  !SecurityManager.getInstance().isUserAuthorized(getUserProfile(request), url) ) {
										resultsByCategory.removeResultForCategory((SearchResult)resultBean);
									}

						        } catch (NoSuchMethodException unableToFindTheSpecifiedProperty) {
						        	SystemException se = new SystemException(unableToFindTheSpecifiedProperty, this.getClass().getName(), "doExecute", "Unable to find the specified property");
						        	LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
						        } catch (InvocationTargetException unableToInvokePropertyGetter) {
						        	SystemException se = new SystemException(unableToInvokePropertyGetter, this.getClass().getName(), "doExecute", "Unable to invoke property getter");
						        	LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);        	
						        } catch (IllegalAccessException notAllowedToInvokePropertyGetter) {
						        	SystemException se = new SystemException(notAllowedToInvokePropertyGetter, this.getClass().getName(), "doExecute", "Not allowed to invoke property getter");
						        	LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);        	
						        } catch (UrlGeneratorNotFoundException cannotFindAUrlGeneratorForReference) {
						        	SystemException se = new SystemException(cannotFindAUrlGeneratorForReference, this.getClass().getName(), "doExecute", "Cannot find a URLGenrator for Reference");
						        	LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);        	
						        }
							}
							
							
							resultsByCategory.setNbrResults();
							// This will make sure the empty category title will not show up
							if ( resultsByCategory.getNbrResults() == 0 )
								resultsByCategory.setCategoryPrintName("");
						}
						searchResults.setNbrResults();
						//*************
						//**************

						results = searchResults.getSearchResults().iterator();
						// if there are any redords that are not going to be diplayed
						// we have to cont them and remov ethem from the list
						while ( results.hasNext() ) 
						{
							SearchResultsByCategory resultsByCategory = (SearchResultsByCategory)results.next();
							Collection resultsForCategory = resultsByCategory.getResultsForCategory();
							Iterator categoryResults = resultsForCategory.iterator();
							int cnt = 0;
							
							if ( resultsByCategory.getNbrResults() > numberResultsDisplayInit) {
								resultsByCategory.setHasMore(true);
								while ( categoryResults.hasNext() )
								{
									Object resultBean = categoryResults.next();
									cnt++;
									if ( cnt > numberResultsDisplayInit )
										resultsByCategory.removeResultForCategory((SearchResult)resultBean);
								}
							}
						}						
						//*********
						
						//Results are returned to JSP in forms
						SearchResultsForm searchResultForm = new SearchResultsForm();
						SearchCriteriaForm searchCriteriaForm = new SearchCriteriaForm();

						searchCriteriaForm.setSearchCriteria(paramSearchCriteria);
						searchResultForm.setSearchResults(searchResults.getSearchResults());
						searchResultForm.setNbrResults(searchResults.getNbrResults());

						if (logger.isDebugEnabled()) {
								logger.log(Priority.DEBUG, "Inside action, collection returned size : " + searchResults.getSearchResults().size());
								logger.log(Priority.DEBUG, "Inside action, collection returned number : " + searchResults.getNbrResults());

								java.util.Iterator iter = searchResultForm.getSearchResults().iterator();
								while (iter.hasNext()) {
										SearchResultsByCategory item = (SearchResultsByCategory)iter.next();
										logger.log(Priority.DEBUG, "Inside action, category print name : " + item.getCategoryPrintName());
								}

						}


						(searchInputForm).setSearchResultsForm(searchResultForm);
						(searchInputForm).setSearchCriteriaForm(searchCriteriaForm);
						(searchInputForm).setRequestedHeaderType(requestedHeaderType);
						(searchInputForm).setRequestedFooterType(requestedFooterType);
						(searchInputForm).setNumberResultsDisplayCategory(numberResultsDisplayCategory);
						(searchInputForm).setNumberResultsDisplayInit(numberResultsDisplayInit);

				//User is requesting results for a specific category
				} else if(nextPage.equals(SearchEngineConstants.CATEGORY_RESULT_PAGE)){

						forward = forwards.get(SearchEngineConstants.CATEGORY_RESULT_PAGE_FORWARD);
						String currentSearchcriteria = request.getParameter("currentSearchCriteria");
						SearchCriteria searchCriteria = new SearchCriteria(currentSearchcriteria,Integer.toString(contractNumber),userEnrolled,userFromNY, definedBenefit, giflVersion);

						//SearchParameter will carry the data needed to perform the search
						//SearchParameter searchParameter = new SearchParameter(searchCriteria, category, numberResultsDisplayInit, numberResultsDisplayCategory, newIndex);
						SearchParameter searchParameter = new SearchParameter(searchCriteria, category, 0 , NUMBER_MAX_QUERY_NUMBER, 0);						

						//Getting results for a given range in one category
						SubsetSearchResultsByCategory subsetSearchResultsByCategory = searchBusinessDelegate.searchResultsByCategory(searchParameter);

						//**** Remove search results that user doesn't have permission
						Collection resultsForCategory = subsetSearchResultsByCategory.getResultsForCategory();
						Iterator categoryResults = resultsForCategory.iterator();
						int cntToBeRemovedForCategory = 0;
						while ( categoryResults.hasNext() )
						{
							try {
								Object resultBean = categoryResults.next(); 
								Reference reference= (Reference)PropertyUtils.getProperty(resultBean, "reference");
								
								
					            UrlGenerator urlGenerator= UrlGeneratorFactory.getInstance().getGenerator(reference);

					            String url= urlGenerator.generateUrl(reference, 
					            		UrlGeneratorTagConfig.getInstance().getProperties( request.getSession(false), 
					            				request), (SearchResult)resultBean);

								if (  !url.startsWith("http") &&  !SecurityManager.getInstance().isUserAuthorized(getUserProfile(request), url) ) {
									subsetSearchResultsByCategory.removeResultForCategory((SearchResult)resultBean);
								}

					        } catch (NoSuchMethodException unableToFindTheSpecifiedProperty) {
					        	SystemException se = new SystemException(unableToFindTheSpecifiedProperty, this.getClass().getName(), "doExecute", "Unable to find the specified property");
					        	LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
					        } catch (InvocationTargetException unableToInvokePropertyGetter) {
					        	SystemException se = new SystemException(unableToInvokePropertyGetter, this.getClass().getName(), "doExecute", "Unable to invoke property getter");
					        	LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);        	
					        } catch (IllegalAccessException notAllowedToInvokePropertyGetter) {
					        	SystemException se = new SystemException(notAllowedToInvokePropertyGetter, this.getClass().getName(), "doExecute", "Not allowed to invoke property getter");
					        	LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);        	
					        } catch (UrlGeneratorNotFoundException cannotFindAUrlGeneratorForReference) {
					        	SystemException se = new SystemException(cannotFindAUrlGeneratorForReference, this.getClass().getName(), "doExecute", "Cannot find a URLGenrator for Reference");
					        	LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);        	
					        }
						}
						
						subsetSearchResultsByCategory.setNbrResults();

						categoryResults = subsetSearchResultsByCategory.getResultsForCategory().iterator();
						// if there are any redords that are not going to be diplayed
						// we have to cont them and remov ethem from the list
						int cnt = 0;
						int floor = (newIndex * numberResultsDisplayCategory )+ numberResultsDisplayInit+1;
						int max = (((newIndex + 1) * numberResultsDisplayCategory ) + numberResultsDisplayInit);

						if ( subsetSearchResultsByCategory.getNbrResults() > max ) 
							subsetSearchResultsByCategory.setHasNext(true);
							
						while ( categoryResults.hasNext() )
						{
						  Object resultBean = categoryResults.next();
						  cnt++;
						  if ( cnt < floor || cnt > max )
						  	subsetSearchResultsByCategory.removeResultForCategory((SearchResult)resultBean);
						  
					    }
					
						//*********
						
						
						Collection searchResults = subsetSearchResultsByCategory.getResultsForCategory();

						//Results are returned to JSP in forms
						SearchResultsForm searchResultForm = new SearchResultsForm();
						SearchCriteriaForm searchCriteriaForm = new SearchCriteriaForm();

						searchResultForm.setSearchResults(searchResults);
						searchResultForm.setCategory(category);
						searchResultForm.setCategoryPrintName(categoryPrintName);
						if (logger.isDebugEnabled()) {
							logger.log(Priority.DEBUG, " inside Controller Action categoryPrintName : " + categoryPrintName);
						}
						searchResultForm.setCurrentIndex(newIndex);
						searchResultForm.setNbrResults(subsetSearchResultsByCategory.getNbrResults());
						searchResultForm.setHasPrevious(subsetSearchResultsByCategory.isHasPrevious());
						searchResultForm.setHasNext(subsetSearchResultsByCategory.isHasNext());

						searchCriteriaForm.setSearchCriteria(currentSearchcriteria);

						(searchInputForm).setSearchResultsForm(searchResultForm);
						(searchInputForm).setSearchCriteriaForm(searchCriteriaForm);
						(searchInputForm).setRequestedHeaderType(requestedHeaderType);
						(searchInputForm).setRequestedFooterType(requestedFooterType);
						(searchInputForm).setNumberResultsDisplayCategory(numberResultsDisplayCategory);
						(searchInputForm).setNumberResultsDisplayInit(numberResultsDisplayInit);
						
				}

				request.setAttribute("searchForm", searchInputForm);
				if (logger.isDebugEnabled()) {
					logger.debug("forward = " + forward);
				}
				return forward;
		}
		
		public static UserProfile getUserProfile(final HttpServletRequest request) {
			return SessionHelper.getUserProfile(request);
		}

		/**
		 * Recovers the SearchBusinessDelegate from the user's session, or creates a new one
		 * if there is none.
		 *
		 * @param request
		 *        The HTTP request sent to the server
		 *
		 * @return
		 *        The SearchBusinessDelegate that will be used for requesting search results
		 **/

		public SearchBusinessDelegate getSearchBusinessDelegate(HttpServletRequest request){
				// Obtain the existing SearchBusinessDelegate object from the session
				SearchBusinessDelegate searchBusinessDelegate = (SearchBusinessDelegate)request.getSession(true).getAttribute(SearchEngineConstants.SEARCH_BUSINESS_DELEGATE);

				//Create a new instance if none already exists
				if(searchBusinessDelegate == null){
						searchBusinessDelegate = new SearchBusinessDelegate();
						request.getSession(true).setAttribute(SearchEngineConstants.SEARCH_BUSINESS_DELEGATE, searchBusinessDelegate);
				}
				return searchBusinessDelegate;

		} 
		
		/**This code has been changed and added  to 
		 * Validate form and request against penetration attack, prior to other validations as part of the CL#137697.
		 * 
		 */
		
	       /**
	       * This code has been changed and added to Validate form and request against
	       * penetration attack, prior to other validations.
	       */
			@InitBinder
	       public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	              binder.bind( request);
	              binder.addValidators(psValidatorSearchInput);
	       }

}