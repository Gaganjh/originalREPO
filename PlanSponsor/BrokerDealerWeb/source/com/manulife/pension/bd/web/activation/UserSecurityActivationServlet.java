package com.manulife.pension.bd.web.activation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.process.BDProcessContextHelper;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.delegate.BDPublicSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.bd.BDSecurityConstants;
import com.manulife.pension.service.security.bd.valueobject.BDSecurityInteractionRequestEx;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.util.IPAddressUtils;

/**
 * The servlet handles the all security activation URL.
 * 
 * It serves:
 *  1. validates the url parameter's
 *  2. Retrieve the security request
 *  3. Based on the request, construct the activation process
 *  4. delegate the activation process to kick off the activation
 *  
 * @author guweigu
 *
 */
public class UserSecurityActivationServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String SYSTEM_ERROR_PAGE = "/error.jsp";
	private static final Logger log = Logger
			.getLogger(UserSecurityActivationServlet.class);

	public UserSecurityActivationServlet() {
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Need to clear the session if there is one.
		// The activation has to start fresh, in case the current
		// browser session is already logged in for example
		BDSessionHelper.invalidateSessionKeepCookie(req);
		
		String idStr = StringUtils
				.trimToNull(req
						.getParameter(BDSecurityConstants.USER_SECURITY_REQUEST_ID_PARAMETER_NAME));
		String token = StringUtils.trimToNull(req
				.getParameter(BDSecurityConstants.TOKEN_PARAMETER_NAME));

		BDSecurityInteractionRequestEx request = null;

		if (idStr != null && token != null) {
			long id = 0L;
			try {
				id = Long.parseLong(idStr);
			} catch (NumberFormatException e) {
				log.error("The request id is not a number : " + idStr);
			}

			if (id != 0) {
				try {
					String ipAddress = IPAddressUtils.getRemoteIpAddress(req);
					request = BDPublicSecurityServiceDelegate.getInstance()
							.validateInteractionRequest(id, token, ipAddress);
				} catch (SecurityServiceException e) {
					// It is ok, just log it, the context will handle the case
					// when no request is returned
					log.debug("The request id and toke is not matched : id = "
							+ id + ", toke = " + token);
				} catch (SystemException e) {
					log.error("Activation fails due to SystemException", e);
					getServletContext().getRequestDispatcher(SYSTEM_ERROR_PAGE)
							.forward(req, resp);
					return;
				}
			}
		}
		// kick off the activation process
		try {
			AbstractActivationProcessContext context = ActivationContextFactory
					.getInstance().getProcessContext(request);
			BDProcessContextHelper.setProcessContext(req, context.getName(),
					context);

			resp.sendRedirect(context.getCurrentState().getUrl());
		} catch (SystemException e) {
			log.error("Activation fails due to SystemException", e);
			getServletContext().getRequestDispatcher(SYSTEM_ERROR_PAGE)
					.forward(req, resp);
			return;
		}
	}
}
