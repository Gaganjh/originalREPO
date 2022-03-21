package com.manulife.pension.ps.web.participant.payrollSelfService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;

import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;

public class PayrollSelfServiceChangesInterceptor implements HandlerInterceptor{
	
	private static final Logger logger = Logger.getLogger(PayrollSelfServiceChangesInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if (logger.isDebugEnabled()) {
			logger.debug("preHandle request before load the PayrollSelfService Page to check the access");
		}
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		if(PayrollSelfServiceChangesWebUtility.allowedToAccessPSS(userProfile)) {
			// If user is allowed to edit census data then set it true for showEditActionButton Otherwise user can still access the page and edit button will not be shown up
			request.getSession(false).setAttribute(Constants.SHOW_EDIT_ACTION_BUTTON,userProfile.isAllowedUpdateCensusData());			
			return true;
		}
		return false;
	}

}
