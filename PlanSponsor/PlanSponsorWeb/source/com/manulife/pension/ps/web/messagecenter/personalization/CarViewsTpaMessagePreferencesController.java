package com.manulife.pension.ps.web.messagecenter.personalization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.message.valueobject.MessagePreferenceError.ErrorCode;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.TeamLead;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;
/**
 * Action for the Message priority page
 * 
 * @author guweigu
 *
 */
@Controller

@SessionAttributes({"messagePrefForm"})

public class CarViewsTpaMessagePreferencesController extends PsAutoController implements
		MCConstants {
	@ModelAttribute("messagePrefForm")
	public MCMessagePreferenceForm populateForm() 
	{
		return new MCMessagePreferenceForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/messagecenter/car_views_tpa_message_preferences.jsp");
		forwards.put("preference","/messagecenter/car_views_tpa_message_preferences.jsp"); 
		forwards.put("messagecenter","redirect:/do/messagecenter/summary");
		}

	private static final LabelValueBean EmptyLabel = new LabelValueBean("", "");

	private static EnumMap<ErrorCode, Integer> ErrorMap = new EnumMap<ErrorCode, Integer>(
			ErrorCode.class);
	static {
		ErrorMap.put(ErrorCode.CANNOT_TURN_OFF, ErrorLastUserTurnOff);
	}

	@RequestMapping(value ="/mcCarView/viewTpaMessagePreferences", method= {RequestMethod.POST,RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("messagePrefForm") MCMessagePreferenceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		MCMessagePreferenceForm form = (MCMessagePreferenceForm) actionForm;
		form.reset( request);
		long userProfileId = -1;
		try {
			userProfileId = Long.valueOf(request.getParameter(MCConstants.ParamUserProfileId));
		} catch (NumberFormatException nfe) {
			// somebody has been messsing the the URL
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		UserInfo userInfo = null;
		try {
			userInfo = SecurityServiceDelegate.getInstance().searchByProfileId(getUserProfile(request).getPrincipal(),
					userProfileId);
			UserRole role = userInfo.getRole();
			//of course, this userInfo doesn't have their permissions set up correctly. 
			for (TPAFirmInfo tpaFirm : userInfo.getTpaFirmsAsCollection()) {
				Collection<PermissionType> permissions = tpaFirm.getContractPermission().getRole().getPermissions();
				for (PermissionType perm : permissions) {
					if (StringUtils.equals(PermissionType.getPermissionCode(perm), PermissionType
							.getPermissionCode(PermissionType.MANAGE_TPA_USERS)))
						role.addPermission(perm);
				}
			}		
			
			if ( getUserProfile(request).getRole() instanceof TeamLead ) {
				form.setUserCanEdit(true);
			}
			
		} catch (SecurityServiceException e) {
			throw new SystemException(e, "can't find user, " + userProfileId);
		}
		if ( userInfo == null ) {
			//somebody has been messsing the the URL
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		Set<Integer> accessibleTemplates = MessageServiceFacadeFactory.getInstance(request.getServletContext())
				.getAccessibleTpaFirmMessageTemplates(getUserProfile(request).getPrincipal(), userInfo);
		form.setTemplateRepository(MCUtils.getTpaFirmMessageTemplateRepository(request.getServletContext(), request, MCUtils
				.getMessageCenterTree(request.getServletContext()).getId(), accessibleTemplates));
		form.setMessageCenterTop(MCUtils.getMessageCenterTree(request.getServletContext()));
		form.update(0, MessageServiceFacadeFactory.getInstance(request.getServletContext()).getTpaFirmMessagePreference(
				userInfo.getRole(), userInfo.getProfileId(), accessibleTemplates));
		List<LabelValueBean> contractList = new ArrayList<LabelValueBean>();
		form.setFirstName(userInfo.getFirstName());
		form.setLastName(userInfo.getLastName());
		contractList.add(new LabelValueBean(Integer.toString(0), Integer.toString(0)));
		form.setContracts(contractList);
		int contractId = -1;
		try {
			contractId = Integer.valueOf(request.getParameter(MCConstants.ParamContractId));
		} catch (NumberFormatException nfe) {
			// somebody has been messsing the the URL
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		form.setContractId(contractId);
		form.setUserProfileId(userProfileId);
		form.setTpa(userInfo.getRole().isTPA());
		form.storeClonedForm();

		request.setAttribute(MCConstants.AttrUserIdTpa, userInfo.getRole().isTPA());
		request.setAttribute(MCConstants.ParamContractId,form.getContractId());
		request.setAttribute(MCConstants.ParamUserProfileId,form.getUserProfileId());

		// Add a struts token to ensure we don't get double submissions.
		////TODO saveToken(request);

		return forwards.get("preference");
	}
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations.
   	 */
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
     @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}
