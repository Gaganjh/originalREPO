package com.manulife.pension.ps.web.withdrawal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalBookmarkHelper;

/**
 * Provides a common base class for withdrawals report actions.
 *
 * @author glennpa
 */
public abstract class BaseWithdrawalReportController extends ReportController {

    private static final Logger log = Logger.getLogger(BaseWithdrawalReportController.class);

    private static final String MAPPING_PARAMETER_LOAN = "LOAN";

    private static final String REQ_STOP_WATCH = "stopWatch";

    /**
     * This method checks whether the request comes from withdrawal link or loan
     * link.
     *
     * @param mapping
     *
     * @return boolean
     */
    protected boolean isLoanPage(String mapping) {
        boolean result = false;
        if (ObjectUtils.equals(mapping, MAPPING_PARAMETER_LOAN)) {
            result = true;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String preExecute( final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException, SystemException {

		StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			request.setAttribute(REQ_STOP_WATCH, stopWatch);
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
        // Preserve the last active page location, as the call to the
        // superclass removes it.
        final String lastActivePageLocation = (String) request.getSession()
                .getAttribute(WebConstants.LAST_ACTIVE_PAGE_LOCATION);

        final String normalPreExecuteForward = super.preExecute( form, request,
                response);
        //TODO-Adding additional variable mapping to avoid error
        String mapping=new UrlPathHelper().getPathWithinServletMapping(request);
        if (!mapping.contains("loan/")) {

            request.getSession().setAttribute(
                    WebConstants.LAST_ACTIVE_PAGE_LOCATION,
                    lastActivePageLocation);

            final String bookmarkDetectedForward = WithdrawalBookmarkHelper
                    .preExecute( form, request, response);

            if (bookmarkDetectedForward != null) {
                return bookmarkDetectedForward;
            }
        }

        return normalPreExecuteForward;
    }

    @Override
    protected void postExecute( ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SystemException {
        super.postExecute( form, request, response);
		try {
			StopWatch stopWatch = (StopWatch) request
					.getAttribute(REQ_STOP_WATCH);
			stopWatch.stop();
			if (log.isInfoEnabled()) {
				long userProfileId = getUserProfile(request).getPrincipal()
						.getProfileId();
				log.info("postExecute, " + this.getClass().getName()
						+ ", Overall timing: " + stopWatch.toString()
						+ ", userProfileId: " + userProfileId);
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String doExecute( final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException, SystemException {

        // Intercept the forward that's the result, and handle it with the WithdrawalBookmarkHelper.
        final String actionForward = super.doExecute( actionForm, request, response);

        WithdrawalBookmarkHelper.handleForward(actionForward,  actionForm, request,
                response);

        return actionForward;
    }

}
