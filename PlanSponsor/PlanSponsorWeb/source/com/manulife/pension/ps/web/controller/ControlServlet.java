package com.manulife.pension.ps.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.home.SearchCompanyNameController;
import com.manulife.pension.ps.web.home.SelectContractDetailUtil;
import com.manulife.pension.ps.web.login.ContractCookie;
import com.manulife.pension.ps.web.login.UserCookie;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.util.CommonMrlLoggingUtil;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.report.valueobject.SelectContractReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.utility.ConversionUtility;
import com.manulife.pension.util.IPAddressUtils;
import com.manulife.pension.util.TrueIPCookie;
import com.manulife.pension.util.log.ServiceLogRecord;

/**
 * @author Ilker Celikyilmaz
 * 
 *         This is the ControllerServlet that intercepts will all reuqests the
 *         the application (jsp and action not images) and validate if ther user
 *         is allowed for the requested URL. If the request does not have a
 *         session or session does not have a User object controller checks if
 *         the URL is public url. If user has valid User object in the session
 *         then the controller checks the URL against user's credential. If the
 *         requested URL is not allowed than the user redirected to the login
 *         page. If the requested url is allowed but does not exist the
 *         error.jsp displayed.
 */
public class ControlServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 7320813912362272065L;
    private static final Logger logger = Logger.getLogger(ControlServlet.class);
    private final static String HOMEPAGE_FINDER_ACTION = "/do/home/homePageFinder/";
    private final static String PHONE_COLLECTION_PAGE = "/do/phoneCollection/";
    // login page
    private final static String FORWARDED_LOGIN_PAGE = "/global/layout/publichomelayout.jsp";
    private final static String LOGIN_LAYOUT_ID = "/login/loginPage.jsp";
    // recommended settings page
    private final static String REQUESTED_RECOMMENDEDSETTINGS_PAGE = "/public/recommendedSettings";
    private final static String FORWARDED_RECOMMENDEDSETTINGS_PAGE_SECURE = "/global/layout/defaultlayout.jsp";
    private final static String FORWARDED_RECOMMENDEDSETTINGSNONAV_PAGE_SECURE = "/global/layout/standardlayout.jsp";
    private final static String FORWARDED_RECOMMENDEDSETTINGS_PAGE_PUBLIC = "/global/layout/publichomelayout.jsp";
    private final static String RECOMMENDEDSETTINGS_LAYOUT_ID_SECURE = "/public/recommendedSettingsSecure";
    private final static String RECOMMENDEDSETTINGS_LAYOUT_ID_PUBLIC = "/public/recommendedSettingsPublic";
    private final static String RECOMMENDEDSETTINGS_LAYOUT_ID_SECURE_NONAV = "/public/recommendedSettingsSecureNoNav";

    // contact us page
    private final static String REQUESTED_CONTACTUS_PAGE = "/public/contactUs.jsp";
    private final static String CONTACTUS_LAYOUT_ID_SECURE = "/public/contactUsPageSecure";
    private final static String CONTACTUS_LAYOUT_ID_PUBLIC = "/public/contactUsPagePublic";
    private final static String FORWARDED_SECURE_CONTACTUS_PAGE = "/global/layout/standardlayout.jsp";
    private final static String FORWARDED_PUBLIC_CONTACTUS_PAGE = "/global/layout/publichomelayout.jsp";

    // Unallocated Login page
    /* TODO
     * private static final String REQUESTED_UNALLOCATED_PAGE = "/unallocated";
    private static final String FORWARDED_UNALLOCATED_LAYOUT_PAGE = "/global/layout/publichomelayout.jsp";
    private static final String UNALLOCATED_PAGEBEAN_ID = "/unallocated/unallocatedLogin.jsp";*/

    private SecurityManager securityManager = null;

    private static final boolean DUMP_PARAMETERS = true;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        securityManager = SecurityManager.getInstance();

        // These are going to be application scope to be accessed from all JSP's
        getServletContext().setAttribute(Constants.ENVIRONMENT_KEY,
                Environment.getInstance());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     * 
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final StopWatch stopWatch = new StopWatch();
        try {
            if (logger.isInfoEnabled()) {
                logger.info("+ doPost [" + request.getRequestURI()
                        + "] - starting timer.");
                stopWatch.start();
            }
        } catch (IllegalStateException e) {
            final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
                    + Thread.currentThread().toString();
            logger.error(message);
        }
        processRequest(request, response);
        try {
            if (logger.isInfoEnabled()) {
                stopWatch.stop();
                logger.info(new StringBuffer("- doPost ["
                        + request.getRequestURI() + "] - time duration [")
                        .append(stopWatch.toString()).append("]").toString());
            }
        }  catch (IllegalStateException e) {
            final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread " + Thread.currentThread().toString(); 
            logger.error(message);
        }

    
    }
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     * 
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final StopWatch stopWatch = new StopWatch();
        try {
            if (logger.isInfoEnabled()) {
                logger.info("+ doGet [" + request.getRequestURI()
                        + "] - starting timer.");
                stopWatch.start();
            }
        } catch (IllegalStateException e) {
            final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
                    + Thread.currentThread().toString();
            logger.error(message);
        }

        processRequest(request, response);
        try {
            if (logger.isInfoEnabled()) {
                stopWatch.stop();
                logger.info(new StringBuffer("- doGet ["
                        + request.getRequestURI() + "] - time duration [")
                        .append(stopWatch.toString()).append("]").toString());
            }
        } catch (IllegalStateException e) {
            final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
                    + Thread.currentThread().toString();
            logger.error(message);
        }
    }

    /**
     * This method process the request and forward the request to the original
     * request if the URL is public and the user is anonymous or if the users's
     * credentials authenticated for the requestred url. Otherwise it is
     * redirected to the login page.
     * 
     * @param request
     *            HttpServletRequest object
     * @param response
     *            HttpServletResponse object
     */
    public void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        doLoginAction(request, response);
        if (logger.isDebugEnabled()) {
            logger.debug("Control Servlet!!!");
            logger.debug("requested URI:" + request.getRequestURI());
            if (DUMP_PARAMETERS) {
                logger.debug("Dumping parameters:");
                Enumeration parameters = request.getParameterNames();
                while (parameters.hasMoreElements()) {
                    String parameter = (String) parameters.nextElement();
                    String[] values = request.getParameterValues(parameter);
                    if (values == null) {
                        logger.debug("Parameter [" + parameter
                                + "] does not exist.");

                    } else {
                        StringBuffer valuesBuffer = new StringBuffer();
                        for (int i = 0; i < values.length; i++) {
                            valuesBuffer.append(values[i]);
                            if (i != values.length - 1) {
                                valuesBuffer.append(", ");
                            }
                        }
                        logger.debug("Parameter [" + parameter + "] is ["
                                + valuesBuffer + "]");
                    }
                }
            }
        }

        String requestedURL = request.getRequestURI().substring(
                request.getContextPath().length());
        HttpSession session = request.getSession(false);

        if (!securityManager.isInactive(requestedURL)) {
            if (REQUESTED_RECOMMENDEDSETTINGS_PAGE.equals(requestedURL)) {
                if ((session != null)
                        && (SessionHelper.getUserProfile(request) != null)) {
                    if (SessionHelper.getUserProfile(request)
                            .getCurrentContract() == null) {
                        request
                                .setAttribute(
                                        Constants.LAYOUT_BEAN,
                                        LayoutBeanRepository
                                                .getInstance()
                                                .getPageBean(
                                                        RECOMMENDEDSETTINGS_LAYOUT_ID_SECURE_NONAV));
                        dispatch(
                                FORWARDED_RECOMMENDEDSETTINGSNONAV_PAGE_SECURE,
                                request, response);
                    } else {
                        request.setAttribute(Constants.LAYOUT_BEAN,
                                LayoutBeanRepository.getInstance().getPageBean(
                                        RECOMMENDEDSETTINGS_LAYOUT_ID_SECURE));
                        dispatch(FORWARDED_RECOMMENDEDSETTINGS_PAGE_SECURE,
                                request, response);
                    }
                } else {
                    request.setAttribute(Constants.LAYOUT_BEAN,
                            LayoutBeanRepository.getInstance().getPageBean(
                                    RECOMMENDEDSETTINGS_LAYOUT_ID_PUBLIC));
                    dispatch(FORWARDED_RECOMMENDEDSETTINGS_PAGE_PUBLIC,
                            request, response);
                }
            } else if (REQUESTED_CONTACTUS_PAGE.equals(requestedURL)) {
                if ((session != null)
                        && (SessionHelper.getUserProfile(request) != null)) {
                    request.setAttribute(Constants.LAYOUT_BEAN,
                            LayoutBeanRepository.getInstance().getPageBean(
                                    CONTACTUS_LAYOUT_ID_SECURE));
                    dispatch(FORWARDED_SECURE_CONTACTUS_PAGE, request, response);
                } else {
                    request.setAttribute(Constants.LAYOUT_BEAN,
                            LayoutBeanRepository.getInstance().getPageBean(
                                    CONTACTUS_LAYOUT_ID_PUBLIC));
                    dispatch(FORWARDED_PUBLIC_CONTACTUS_PAGE, request, response);
                }

            } /*
            TODO
            else if (REQUESTED_UNALLOCATED_PAGE.equals(requestedURL)) {
                if ((session != null)
                        && (SessionHelper.getUserProfile(request) != null)) {
                    forwardFindHomePageAction(requestedURL, request, response);
                } else {
                    request.setAttribute(Constants.LAYOUT_BEAN,
                            LayoutBeanRepository.getInstance().getPageBean(
                                    UNALLOCATED_PAGEBEAN_ID));
                    dispatch(FORWARDED_UNALLOCATED_LAYOUT_PAGE, request,
                            response);
                }

            } */else {

                if (session == null
                        && securityManager.isPublicUrl(requestedURL)) {
                    dispatch(requestedURL, request, response);
                } else if (session != null) {
                    Object obj = SessionHelper.getUserProfile(request);

                    if (obj == null) {
                        if (securityManager.isPublicUrl(requestedURL)) {
                            dispatch(requestedURL, request, response);
						} else if (securityManager.isSecondStepUrl(requestedURL)
								&& session.getAttribute("USERID") != null
								&& session.getAttribute(Constants.IS_TRANSITION) == null) {
							dispatch(requestedURL, request, response);
						} else if (securityManager.isSecondStepTransitionUrl(requestedURL)
								&& session.getAttribute(Constants.IS_TRANSITION) != null) {
							dispatch(requestedURL, request, response);
						} else {
                            session.invalidate();
                            forwardLoginPage(request, response);
                        }
                    } else {
                    	//phone collection session handling
						if (session.getAttribute(Constants.PHONE_COLLECTION) != null) {
							//dispatch to requested phone collection page url
							if (securityManager.isPhoneCollectionUrl(requestedURL)) {
								dispatch(requestedURL, request, response);
							//check edit my profile page  
							} else if (securityManager.isPhoneCollectionEditmyprofileUrl(requestedURL)) {
								//allow authorized user to edit my profile page 
								if (securityManager.isUserAuthorized((UserProfile) obj, requestedURL)) {
									//user should not get challenged when the preferences are not set
									session.setAttribute(Constants.CHALLENGE_PASSCODE_IND, false);
									dispatch(requestedURL, request, response);
								//unauthorized user of edit my profile page is allowed to original navigation. 	
								} else {
									session.removeAttribute(Constants.PHONE_COLLECTION);
								}
							//when url is not phone collection AND not edit my profile AND not sign out 	
							} else {
								// allow user to navigate public urls and sign out
								if (securityManager.isPublicUrl(requestedURL)
										|| securityManager.isSignOutUrl(requestedURL)) {
									dispatch(requestedURL, request, response);
								} else {
									// dispatch request to phone collection controller
									dispatch(PHONE_COLLECTION_PAGE, request, response);
								}
							}
							return;
						}
						//phone collection page request handling when its not phone collection session
						else if (securityManager.isPhoneCollectionUrl(requestedURL)) {
							forwardFindHomePageAction(requestedURL, request, response);
							return;
						}
                    	
                        boolean isSwitchNotNecessaryOrSuccess = switchContractIfNecessary(
                                request, response);

                        if (isSwitchNotNecessaryOrSuccess
                                && securityManager.isUserAuthorized(
                                        (UserProfile) obj, requestedURL)) {
                            
                            dispatch(requestedURL, request, response);
                        } else {
                            /**
                             * If switch contract is not successful or if the
                             * user is not authorized to view the contract, the
                             * result is the same.
                             */
                        	// If User is not authorized
                       	 if (!securityManager.isUserAuthorized((UserProfile) obj, requestedURL)) {
                       		try {
								CommonMrlLoggingUtil.logUnAuthAcess(request,"User is not authorized",this.getClass().getName()+":"+"processRequest");
							} catch (SystemException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                       	 }
                       	 
                            forwardFindHomePageAction(requestedURL, request,
                                    response);
                        }
                    }
                } else {
                    forwardLoginPage(request, response);
                }
            }
        } else {
            if (session != null) {
                forwardFindHomePageAction(requestedURL, request, response);
            } else {
                forwardLoginPage(request, response);
            }
        }
    }

    public static boolean switchContractIfNecessary(HttpServletRequest request,
            HttpServletResponse response) {

        UserProfile userProfile = SessionHelper.getUserProfile(request);
        if (userProfile != null
                && userProfile.getPrincipal().getRole() instanceof InternalUser) {
            /**
             * Limit contract switching to internal user for now. If we want to
             * open it up, we need to check if the user has access to the
             * requested contract number.
             */
            String contractNumberString = request
                    .getParameter("switchContractId");

            if (StringUtils.isNotBlank(contractNumberString)) {
                /**
                 * Additional checks (e.g. unallocated contracts) are done at
                 * the securityinfo.xml level on the redirect.
                 */
                Integer contractNumber = Integer.valueOf(contractNumberString);

                if (userProfile.getCurrentContract() != null
                        && userProfile.getCurrentContract().getContractNumber() == contractNumber
                                .intValue()) {
                    /**
                     * If we are already in the right contract, don't switch.
                     */
                } else {
                    SessionHelper.setMCSelectContract(request, new Boolean(true));
                    ReportServiceDelegate service = ReportServiceDelegate
                            .getInstance();
                    ReportCriteria criteria = new ReportCriteria(
                            SelectContractReportData.REPORT_ID);
                    criteria.addFilter(
                            SelectContractReportData.FILTER_CLIENT_ID, String
                                    .valueOf(userProfile.getPrincipal()
                                            .getProfileId()));
                    criteria.addFilter(
                            SelectContractReportData.FILTER_SITE_LOCATION,
                            Environment.getInstance().getDBSiteLocation());

                    criteria.addFilter(
                            SelectContractReportData.FILTER_DI_DURATION,
                            SearchCompanyNameController.DI_DURATION_24_MONTH);
                    criteria.addFilter(
                            SelectContractReportData.FILTER_USER_ROLE,
                            ConversionUtility
                                    .convertToStoredProcRole(userProfile
                                            .getRole()));
                    criteria.addFilter(
                            SelectContractReportData.FILTER_SEARCH_STRING,
                            "C.CONTRACT_ID=" + contractNumber);
                    criteria.insertSort(
                            SelectContractReportData.SORT_CONTRACT_NAME, "asc");
                    criteria.setPageNumber(1);
                    criteria.setPageSize(1);
                    try {
                        ReportData reportData = service.getReportData(criteria);
                        if (reportData != null
                                && reportData.getDetails() != null
                                && reportData.getDetails().size() == 1) {
                            SelectContractDetailUtil.selectContract(
                                    userProfile, contractNumber);
                            // set the contract cookie to the current
                            // contract
                            response.addCookie(new ContractCookie(
                                    contractNumber));
                            response.addCookie(new UserCookie(userProfile
                                    .getPrincipal().getUserName(), userProfile
                                    .getPrincipal().getRole()));
                            
                            String ipAddress = IPAddressUtils.getRemoteIpAddress(request);
                        	String SITE_PROTOCOL = Environment.getInstance().getSiteProtocol();
                        	response.addCookie(new TrueIPCookie(ipAddress, SITE_PROTOCOL, Constants.TRUE_COOKIES_SITE_NAME));
                            
                        } else {
                            /**
                             * Cannot switch contract due to other conditions.
                             */
                            return false;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return true;
    }

    /**
     * This method forward the request to the requested URL. It uses
     * RequestDispathcer to forward the request. If the url does not exist it
     * will redirect to error.jsp
     * 
     * Updated May 27, 2005 by Mark Eldridge: Added try catch to avoid errors
     * where response is already committed via a download of TED files / some
     * other commit.
     * 
     * @param requestedURL
     *            This is the url that is going to be dispatched
     * @param request
     *            HttpServletRequest object
     * @param response
     *            HttpServletResponse object
     */
    private void dispatch(String requestedURL, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        if (logger.isDebugEnabled())
            logger.debug("Access Allowed to URL :" + requestedURL);

        // here we add the /WEB-INF context infront of the request
        // to be able to forward it to the requested resource
        RequestDispatcher rd = getServletContext().getRequestDispatcher(
                "/WEB-INF" + requestedURL);
        try {
            rd.forward(request, response);
        } catch (Exception e) {
            logger.error("Dispatch Fails for '/WEB-INF" + requestedURL + "':",
                    e);
        }
    }

    /**
     * This method forward the request find home page action.
     * 
     * @param requestedURL
     *            This is the url that is going to be dispatched
     * @param request
     *            HttpServletRequest object
     * @param response
     *            HttpServletResponse object
     */
    private void forwardFindHomePageAction(String requestedURL,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        if (logger.isDebugEnabled())
            logger.debug("Access DENIED for URL! :" + requestedURL);

        RequestDispatcher rd = getServletContext().getRequestDispatcher(
                "/WEB-INF" + HOMEPAGE_FINDER_ACTION);
        rd.forward(request, response);

    }

    /**
     * This method forward the request to Login Page
     * 
     * @param request
     *            HttpServletRequest object
     * @param response
     *            HttpServletResponse object
     */
    private void forwardLoginPage(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        if (logger.isDebugEnabled())
            logger.debug("Redirecting to Login Page");
        if (isDirectURL(request)) {
            StringBuffer url = request.getRequestURL();
            String queryStr = request.getQueryString();
            if (queryStr != null) {
                url.append("?" + queryStr);
            }
            request.setAttribute(Constants.DIRECT_URL_ATTR, url.toString());
        }       
        request.setAttribute(Constants.LAYOUT_BEAN, LayoutBeanRepository
                .getInstance().getPageBean(LOGIN_LAYOUT_ID));

        // We do not force cookie sanitation on a regular arrival at login page.
        // Otherwise an empty string JSESSIONID cookie is created unnecessarily.
        // So calling this with a "false" for the forcing cookie removal parameter.
        SessionHelper.invalidateSession(request, response, false);

        RequestDispatcher rd = getServletContext().getRequestDispatcher(
                "/WEB-INF" + FORWARDED_LOGIN_PAGE);
        rd.forward(request, response);
    }

    /**
     * Returns whether the request is an URL that will redirect to it once login
     */
    protected boolean isDirectURL(HttpServletRequest request) {
        if (StringUtils.equalsIgnoreCase("GET", request.getMethod())) {
            return true;
        }
        return false;
    }
    
    public void doLoginAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String ipAddress = IPAddressUtils.getRemoteIpAddress(request);
    	String SITE_PROTOCOL = Environment.getInstance().getSiteProtocol();
    	response.addCookie(new TrueIPCookie(ipAddress, SITE_PROTOCOL, Constants.TRUE_COOKIES_SITE_NAME));
    }
    
}
