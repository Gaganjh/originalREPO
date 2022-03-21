package com.manulife.pension.ps.web.withdrawal.util;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.withdrawal.WebConstants;

/**
 * The WithdrawalBookmarkHelper is used to detect if the user has used irregular navigation to
 * attempt to access a page. This includes using browser bookmarks, typing in the URL of other site
 * page, and/or using the browser back/forward buttons.
 * 
 * @author glennpa
 */
public final class WithdrawalBookmarkHelper {

    /**
     * JSP_FILENAME_SUFFIX.
     */
    private static final String JSP_FILENAME_SUFFIX = ".jsp";

    public static final Logger logger = Logger.getLogger(WithdrawalBookmarkHelper.class);

    public static final String ACTION_FORWARD_BOOKMARK_DETECTED = "withdrawalsBookmark";

    // Initialize the collection to an anonymous class, with these values.
    private static final Collection<String> ENTRY_PAGES = new ArrayList<String>() {
        /**
         * Default serialVersionUID.
         */
        private static final long serialVersionUID = 1L;

        {
            add("/withdrawal/beforeProceedingGatewayInit/");
            add("/withdrawal/beforeProceedingGatewayReview/");

            add("/withdrawal/beforeProceedingInitiate/");
            add("/withdrawal/beforeProceedingReview/");

            add("/withdrawal/loanAndWithdrawalRequests/");
            add("/withdrawal/loanAndWithdrawalRequestsInit/");
            add("/withdrawal/loanAndWithdrawalRequestsPage/");

            add("/withdrawal/entry/view/");
            add("/withdrawal/entry/edit/");
        }
    };

    /**
     * Default Constructor.
     */
    private WithdrawalBookmarkHelper() {
        super();
    }

    /**
     * @see PsController#preExecute TODO preExecute Description.
     * 
     * @param mapping The action mapping.
     * @param form The action form.
     * @param request The request.
     * @param response The response.
     * @return ActionForward - The forward if the request is interrupted from normal process, or
     *         null if the request is allowed to proceed.
     */
    public static String preExecute( final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        final String requestedPath = new UrlPathHelper().getPathWithinServletMapping(request);
        String lastValidPageLocation = (String) request.getSession().getAttribute(
                WebConstants.LAST_ACTIVE_PAGE_LOCATION);

        // printFriendly handling.
        if (checkPrintFriendly(request)) {
            // Don't update the last requested page, don't detect bookmarking, just let all print
            // friendly requests pass through.
            return null;
        } // fi

        // Detect if the user has used a bookmark.
        if (detectBookmark(lastValidPageLocation, requestedPath)) {
            // Found a bookmark, re-direct.
            if (logger.isDebugEnabled()) {
                logger.debug("Bookmark detected. lastValidPageLocation: [" + lastValidPageLocation
                        + "], requestedPath: [" + requestedPath + "]");
            } // fi

            // Remove the last active page.
            request.getSession().removeAttribute(WebConstants.LAST_ACTIVE_PAGE_LOCATION);

            // This halts the current 'execute' action and follows this forward.
            //return mapping.findForward(ACTION_FORWARD_BOOKMARK_DETECTED);
            return ACTION_FORWARD_BOOKMARK_DETECTED;
        } // fi

        // Need to determine if we redirect for Step1/2 request.
        // Trap bookmarking between step 1 and 2, and redirect to the originator.
        if (StringUtils.equals("/withdrawal/entryStep1/", requestedPath)) {
            if (StringUtils.equals(lastValidPageLocation, "/withdrawal/entryStep2/")) {
                //return mapping.findForward("preventBookmarkStep1");
            	return "preventBookmarkStep1";
            } // fi
        } // fi
        if (StringUtils.equals("/withdrawal/entryStep2/", requestedPath)) {
            if (StringUtils.equals(lastValidPageLocation, "/withdrawal/entryStep1/")) {
                //return mapping.findForward("preventBookmarkStep2");
            	return "preventBookmarkStep2";
            } // fi
        } // fi

        // Set the currently requested path as the last validated location, as we've now passed the
        // bookmarking detection.
        request.getSession().setAttribute(WebConstants.LAST_ACTIVE_PAGE_LOCATION, requestedPath);

        // This continues with the current 'execution' without interruption.
        return null;
    }

    /**
     * This method checks if the current request is a print friendly request. It looks at the
     * request and the session attributes that may indicate it's print friendly.
     * 
     * @param request The request.
     * @return boolean - True if the request is a print friendly page, false otherwise.
     */
    private static boolean checkPrintFriendly(final HttpServletRequest request) {
        if ((StringUtils.equalsIgnoreCase(request.getParameter("printFriendly"), "true"))
                || (StringUtils.equalsIgnoreCase((String) request.getSession().getAttribute(
                        WebConstants.PRINTFRIENDLY_KEY), "true"))) {
            // Print Friendly has been detected.
            return true;
        } // fi
        return false;
    }

    /**
     * Determines if the request is a valid path, given that the last validated path has a certain
     * value.
     * 
     * @param lastValidPageLocation - The value of the last path validated by this bookmark
     *            detector.
     * @param requestedPath - The path requested by the user.
     * @return boolean - True if a bookmark is detected, false otherwise.
     */
    private static boolean detectBookmark(final String lastValidPageLocation,
            final String requestedPath) {

        // If it's in the entry page list, allow it.
        if (isEntryPage(requestedPath)) {
            return false;
        } // fi

        // Check if there's no previous page in our section.
        if (StringUtils.isBlank(lastValidPageLocation)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Attempt to go directly to a non-entry page "
                        + "from a non-Withdrawal page.");
            } // fi
            return true;
        } // fi

        if (StringUtils.equals(lastValidPageLocation, requestedPath)) {
            // It's the same action. Always OK.
            return false;
        } // fi

        // Allow bookmarking between step 1 and 2.
        if (StringUtils.equals("/withdrawal/entryStep1/", requestedPath)) {
            if (StringUtils.equals(lastValidPageLocation, "/withdrawal/entryStep2/")) {
                return false;
            } // fi
        } // fi
        if (StringUtils.equals("/withdrawal/entryStep2/", requestedPath)) {
            if (StringUtils.equals(lastValidPageLocation, "/withdrawal/entryStep1/")) {
                return false;
            } // fi
        } // fi

        // Allow navigation between these (don't want to mess with redirects, so added rule here).
        if (StringUtils.equals("/withdrawal/loanAndWithdrawalRequests/", requestedPath)) {
            if (StringUtils.equals(lastValidPageLocation,
                    "/withdrawal/loanAndWithdrawalRequestsInit/")) {
                return false;
            } // fi
        } // fi

        // Fixes for things that use direct links. Replace with actions after.
        if ((StringUtils.equals("/withdrawal/beforeProceedingGatewayInit/", requestedPath))
                || (StringUtils.equals("/withdrawal/beforeProceedingGatewayReview/", requestedPath))) {
            if ((StringUtils
                    .equals(lastValidPageLocation, "/withdrawal/loanAndWithdrawalRequests/"))
                    || (StringUtils.equals(lastValidPageLocation,
                            "/withdrawal/loanAndWithdrawalRequestsInit/"))) {
                return false;
            } // fi
        } // fi

        return true;
    }

    /**
     * Determines if the requestedPath is an entry page path or not.
     * 
     * @param requestedPath - The requested path.
     * @return boolean - True if the requested path is an entry path, false otherwise.
     */
    private static boolean isEntryPage(final String requestedPath) {

        if (ENTRY_PAGES.contains(requestedPath)) {
            return true;
        } // fi

        return false;
    }

    /**
     * When a request is processed and it's forward is determined, this method captures the valid
     * navigation path before control is returned to struts.
     * 
     * @param actionForward The valid action forward after the action has processed.
     * @param mapping The action mapping.
     * @param form The form.
     * @param request The request.
     * @param response The response.
     */
    public static void handleForward(final String actionForward,
             final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        // printFriendly handling.
        if (checkPrintFriendly(request)) {
            // We do NOT keep track of anything on the print friendly pages.
            return;
        } // fi
//TODO
        //final String forwardPath = stripQueryStringFromPath(actionForward.getPath());
        final String forwardPath = stripQueryStringFromPath(actionForward);
        // Check if it's forwarding to a JSP
        if (StringUtils.equalsIgnoreCase(StringUtils.right(forwardPath, JSP_FILENAME_SUFFIX
                .length()), JSP_FILENAME_SUFFIX)) {
            // We've found a JSP forward. We don't want to add this one as the last valid action
            // (it's an end point).
            return;
        } // fi

        // Need to strip off the '/do' portion of the path.
        final String newValidPath = StringUtils.removeStart(forwardPath, "/do");

        // Set this navigation path as the new value for last valid path.
        request.getSession().setAttribute(WebConstants.LAST_ACTIVE_PAGE_LOCATION, newValidPath);
    }

    /**
     * This method takes a requested path, which may include a query string, and strips off any
     * query string it finds. So anything including and after the '?' on the path will be returned.
     * 
     * @param path The path to strip the query string from.
     * @return String - The path without a query string.
     */
    private static String stripQueryStringFromPath(final String path) {
        return StringUtils.substringBefore(path, "?");
    }

}
