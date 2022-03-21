package com.manulife.pension.ps.web.messagecenter.personalization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.BundledGaCAR;
import com.manulife.pension.service.security.role.TeamLead;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * Action for the Message priority page
 * 
 * @author guweigu
 *
 */

public class CarMessagePreferencesController extends PsAutoController implements
		MCConstants {

	/**
	 * Default action is to retrieve the message priority perferences from CSDB
	 * 
	 */
	 
	public String doDefault(MCMessagePreferenceForm form,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		form.reset( request);
		long userProfileId = -1;
		int contractId = -1;
		try {
			userProfileId = Long.valueOf(request.getParameter(MCConstants.ParamUserProfileId));
			contractId = Integer.valueOf(request.getParameter(MCConstants.ParamContractId));
		} catch (NumberFormatException nfe) {
			// somebody has been messsing the the URL
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		UserInfo userInfo = null;
		try {
			userInfo = SecurityServiceDelegate.getInstance().searchByProfileId(getUserProfile(request).getPrincipal(),
					userProfileId);
		} catch (SecurityServiceException e) {
			throw new SystemException(e, "can't find user, " + userProfileId);
		}
		if ( userInfo == null ) {
			//somebody has been messsing the the URL
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		UserRole role = userInfo.getRole();
		if (!(role.isTPA() || role instanceof BundledGaCAR)) {
			if ( userInfo.getContractPermission(contractId) == null ) {
				//somebody has been messsing the the URL
				return Constants.HOMEPAGE_FINDER_FORWARD;
			}
			role = userInfo.getContractPermission(contractId).getRole();
		}
		
		if ( getUserProfile(request).getRole() instanceof TeamLead ) {
			form.setUserCanEdit(true);
		}
		
		Set<Integer> accessibleTemplates = MessageServiceFacadeFactory.getInstance(request.getServletContext())
				.getAccessibleContractMessageTemplates(getUserProfile(request).getPrincipal(), userInfo, contractId);
		form.setTemplateRepository(MCUtils.getContractMessageTemplateRepository(request.getServletContext(), request, MCUtils
				.getMessageCenterTree(request.getServletContext()).getId(), accessibleTemplates));
		form.setMessageCenterTop(MCUtils.getMessageCenterTree(request.getServletContext()));
		form.update(contractId, MessageServiceFacadeFactory.getInstance(request.getServletContext()).getContractMessagePreference(
				role, userInfo.getProfileId(), contractId, accessibleTemplates));
		List<LabelValueBean> contractList = new ArrayList<LabelValueBean>();
		form.setFirstName(userInfo.getFirstName());
		form.setLastName(userInfo.getLastName());
		contractList.add(new LabelValueBean(Integer.toString(contractId), Integer.toString(contractId)));
		form.setContracts(contractList);
		Contract contract = null;
		try {
			contract = ContractServiceDelegate.getInstance().getContractDetails(contractId, 
					EnvironmentServiceDelegate.getInstance().retrieveContractDiDuration(role, 0,null));
		} catch (ContractNotExistException e) {
			throw new SystemException(e, "can't find contract, " + contractId);
		}
		form.setContractName(contract.getCompanyName());
		form.setContractId(contractId);
		form.setUserProfileId(userProfileId);
		form.setTpa(userInfo.getRole().isTPA());
		
		form.storeClonedForm();

		request.setAttribute(MCConstants.AttrUserIdTpa, userInfo.getRole().isTPA());
		request.setAttribute(MCConstants.ParamContractId,form.getContractId());
		request.setAttribute(MCConstants.ParamUserProfileId,form.getUserProfileId());

		// Add a struts token to ensure we don't get double submissions.
		////TODO saveToken(request);

		return "preference";
	}
	
	
	
	
	   
}
