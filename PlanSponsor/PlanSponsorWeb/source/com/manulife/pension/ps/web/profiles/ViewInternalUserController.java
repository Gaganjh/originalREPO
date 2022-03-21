package com.manulife.pension.ps.web.profiles;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.broker.valueobject.RegionalVicePresident;
import com.manulife.pension.service.broker.valueobject.impl.RegionalVicePresidentImpl;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.bd.exception.BDRvpUserExistException;
import com.manulife.pension.service.security.exception.DuplicateEmployeeNumberException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.BDInternalUser;
import com.manulife.pension.service.security.role.BDRvp;
import com.manulife.pension.service.security.role.BDUserRole;
import com.manulife.pension.service.security.role.BDUserRoleFactory;
import com.manulife.pension.service.security.role.RelationshipManager;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.UserRoleFactory;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.valueobject.RelationshipManagerVO;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * @author Charles Chan
 */
@Controller
@RequestMapping( value = "/profiles")
@SessionAttributes({"addEditUserForm","managePasswordForm"})

public class ViewInternalUserController extends AbstractAddEditUserController {

	@ModelAttribute("addEditUserForm") 
	public AddEditUserForm populateForm()
	{
		return new AddEditUserForm();
		}
	@ModelAttribute("managePasswordForm") 
	public ManagePasswordForm populateFormPassword() 
	{
		return new ManagePasswordForm();
}
	public static HashMap<String,String> forwards=new HashMap<String,String>();
	static{
	forwards.put("input","/profiles/viewInternalUser.jsp");
	forwards.put("manageUsers","redirect:/do/profiles/manageInternalUsers/" );
	}

	/**
	 * Constructor.
	 */
	public ViewInternalUserController() {
		super(ViewInternalUserController.class);
		securityExceptionFormFieldMap.put(
				DuplicateEmployeeNumberException.class,
				AddEditUserForm.FIELD_EMPLOYEE_NUMBER);
		securityExceptionFormFieldMap.put(
				BDRvpUserExistException.class,
				AddEditUserForm.FIELD_RVP);
	}

	protected void populateUserInfo(HttpServletRequest request,
			UserInfo userInfo, AddEditUserForm form) {

		userInfo.setWebAccessInd(true);
		userInfo.setEmployeeNumber(form.getEmployeeNumber());

		/*
		 * Set role for the plan sponsor site.
		 */
		userInfo.setRole(UserRoleFactory.getUserRole(form
				.getPlanSponsorSiteRole()));

		/*
		 * Set the Internal User permissions
		 */
		if ("Yes".equals(form.getAccess408DisclosureRegen())) {
			userInfo.addUserPermission(PermissionType.REGEN_408_DISCLOSURE);
		} else {
			userInfo.removeUserPermission(PermissionType.REGEN_408_DISCLOSURE);
		}
		if ("Yes".equals(form.getAccessIPIHypotheticalTool())) {
			userInfo.addUserPermission(PermissionType.IPI_HYPOTHETICAL_TOOL);
		} else {
			userInfo.removeUserPermission(PermissionType.IPI_HYPOTHETICAL_TOOL);
		}

		/*
		 * Set role for the participant site.
		 */
		userInfo.setParticipantRole(form.getParticipantSiteRole());

		/*
		 * Set role for broker dealer
		 */
		String bdRoleCode = form.getBrokerDealerSiteRole();
		BDInternalUser role = null;
		if (!bdRoleCode.equals(AccessLevelHelper.NO_ACCESS)) {
			role = BDUserRoleFactory
					.getInterUserRole(bdRoleCode);
			// if this is RVP, needs to set the rvp entity as well
			if (role != null
					&& role.getRoleType().compareTo(BDUserRoleType.RVP) == 0) {
				long rvpId = Long.parseLong(form.getRvpId());
				RegionalVicePresidentImpl rvp = new RegionalVicePresidentImpl();
				rvp.setId(rvpId);
				((BDRvp) role).setRvpEntity(rvp);
			}
		}

		userInfo.setBdUserRole(role);
		String pswRoleCode = form.getPlanSponsorSiteRole();
		RelationshipManagerVO rm = null;

		if (pswRoleCode != null && !pswRoleCode.equals(AccessLevelHelper.NO_ACCESS)) {
			if (StringUtils.isNotEmpty(form.getRmId()) && !"UA".equals(form.getRmId())) {
				long rmId = Long.parseLong(form.getRmId());
				rm = new RelationshipManagerVO();
				rm.setPartyId(Long.toString(rmId));
			}
     	 }
		userInfo.setPswRole(rm);
	  }

	protected void populateForm(HttpServletRequest request,
			AddEditUserForm form, UserInfo userInfo) {

		form.setPasswordState(userInfo.getPasswordState());
		form.setEmployeeNumber(userInfo.getEmployeeNumber());

		if (userInfo.getParticipantRole() != null) {
			form.setParticipantSiteRole(userInfo.getParticipantRole()
					.toString());
		}
		if (userInfo.getRole() != null) {
			form.setPlanSponsorSiteRole(userInfo.getRole().toString());
			String psSiteRole = form.getPlanSponsorSiteRole();
			if (RelationshipManager.ID.equals(psSiteRole)) {
				RelationshipManagerVO relationshipManagerVO = userInfo.getPswRole();
				String rmId = relationshipManagerVO == null ? "" : relationshipManagerVO.getPartyId();
				form.setRmId(rmId);
				if(StringUtils.isBlank(form.getRmIdSaved())){ 
					form.setRmIdSaved(form.getRmId());
				}
				if (StringUtils.isEmpty(rmId)) {
					form.setRmDisplayName("");

				} else {
					List<LabelValueBean> rmList = form.getRmList();
					if(rmList != null && !rmList.isEmpty()){
						for (LabelValueBean rmLablevalueBean : rmList) {
							if (rmLablevalueBean.getValue().equals(rmId)) {
								form.setRmDisplayName(rmLablevalueBean.getLabel());
								break;
							}
						}
					}
				}
				
			} 
			form.setAccess408DisclosureRegen(userInfo.getRole().hasPermission(PermissionType.REGEN_408_DISCLOSURE)?"Yes":"No");
			form.setAccessIPIHypotheticalTool(userInfo.getRole().hasPermission(PermissionType.IPI_HYPOTHETICAL_TOOL)?"Yes":"No");
		}
		
        BDUserRole bdRole = userInfo.getBdUserRole();
		if (bdRole == null) {
			form.setBrokerDealerSiteRole(AccessLevelHelper.NO_ACCESS);
		} else {
			BDUserRoleType roleType = bdRole.getRoleType();
			form.setBrokerDealerSiteRole(roleType.getUserRoleCode());
			if (BDUserRoleType.RVP.compareTo(roleType) == 0) {
				RegionalVicePresident rvpEntity = ((BDRvp) bdRole).getRvpEntity();
				String rvpId = rvpEntity == null ? "" : Long.toString(rvpEntity.getId());
				form.setRvpId(rvpId);
				if (StringUtils.isEmpty(rvpId)) {
					form.setRvpDisplayName("");
				} else {
					List<LabelValueBean> rvpList = form.getRvpList();
					for (LabelValueBean rvp : rvpList) {
						if (rvp.getValue().equals(rvpId)) {
							form.setRvpDisplayName(rvp.getLabel());
							break;
						}
					}
				}
			}
		}
		form
		.setBdLicenceVerified(ManageInternalUserHelper
				.getLicenseVerifiedDisplay(userInfo
						.getBdLicenseVerified()));
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAction#postExecute(org
	 * .apache.struts.action.ActionMapping, org.apache.struts.action.Form,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	 
	protected void postExecute(AddEditUserForm actionForm,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		// moved from doValidate to this method as doValidate does not throw
		// SystemException.
		// we want the dropdown to be populated even if the validation fails.
		populateAccessLevelDropdown(request);
	}

	/**
	 * @see com.manulife.pension.ps.web.controller.PsAutoController#doDefault(org.apache.struts.action.ActionMapping,
	 *      AutoForm, javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@RequestMapping(value ="/viewInternalUser/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("addEditUserForm") AddEditUserForm actionForm,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		actionForm.setRvpList(ManageInternalUserHelper.getRVPs());
		actionForm.setRmList(ManageInternalUserHelper.getRMs());
		model.addAttribute("mapping","view");
		String forward = super.doDefault(actionForm,model, request,response);

		
		/*
		 * Call super's doDefault. If it returns any forward other than the
		 * input forward, we just return.
		 */
		if (!forward.equals("input")) {
			return forwards.get(forward);
		}

		populateAccessLevelDropdown(request);
		postExecute(actionForm,request, response);
		return forwards.get("input");
	}

	@Override
	protected UserInfo getUserInfo(Principal p, String userName)
			throws SystemException, SecurityServiceException {
		return SecurityServiceDelegate.getInstance()
				.searchInternalUserByUserName(p, userName);
	}

	/**
	 * Calls the AccessLevelHelper to get drop down lists with the dislay names
	 * for the roles the user has access to.
	 * 
	 * @param userRole
	 *            The role the user is logged in
	 */
	protected void populateAccessLevelDropdown(HttpServletRequest request) {

		UserRole userRole = getUserProfile(request).getRole();
		// plan sponsor site drop down list
		List accessDropDownList = AccessLevelHelper.getInstance()
				.getInternalAccessLevels(userRole);
		if (accessDropDownList != null) {
			request.setAttribute(Constants.INTERNAL_ACCESS_LEVEL,
					accessDropDownList);
		}
		// ezk site drop down list
		List ezkAccessDropDownList = AccessLevelHelper.getInstance()
				.getEzkAccessLevels(userRole);
		if (ezkAccessDropDownList != null) {
			request.setAttribute(Constants.EZK_ACCESS_LEVEL,
					ezkAccessDropDownList);
		}

		// broker dealer site drop down list
		List bdAccessDropDownList = AccessLevelHelper.getInstance()
				.getBDAccessLevels(userRole);
		if (bdAccessDropDownList != null) {
			request.setAttribute(Constants.BD_ACCESS_LEVEL,
					bdAccessDropDownList);
		}
	}
	
	
	 /* (non-Javadoc)
     * @see com.manulife.pension.platform.web.controller.BaseAction#getSubmitAction(javax.servlet.http.HttpServletRequest, org.apache.struts.action.Form)
     */
    @Override
    protected String getSubmitAction(HttpServletRequest request, ActionForm form)
    		throws ServletException {
    	String action = super.getSubmitAction(request,form);
    	AddEditUserForm actionForm = (AddEditUserForm)form ;
    	 if(StringUtils.isNotBlank(actionForm.getAction()))
   		  action = actionForm.getAction()!= null ? actionForm.getAction():StringUtils.EMPTY;
   		  
    	return action;
    }
	

	@RequestMapping(value ="/viewInternalUser/", params={"action=cancel"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doCancel (@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
	throws  SystemException, IOException, ServletException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		model.addAttribute("mapping","view");
	      String forward=super.doCancel( form,model,request, response);
	      
	  	postExecute(form,request, response);
		return forwards.get(forward);
 }
	@RequestMapping(value ="/viewInternalUser/", params={"action=save"} ,method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSave (@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) 
	throws  SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		model.addAttribute("mapping","view");
	       String forward=super.doSave( form,model, request, response);
		return forwards.get(forward);
 }
	@RequestMapping(value ="/viewInternalUser/" ,params={"action=confirm"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doConfirm (@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) throws SystemException, IOException, ServletException  {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doConfirm( form, request, response);
	       postExecute(form,request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ="/viewInternalUser/" ,params={"action=refresh"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doRefresh (@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) throws SystemException, IOException, ServletException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doRefresh( form, request, response);
	       postExecute(form,request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	
	@RequestMapping(value ="/viewInternalUser/", params={"action=finish"},method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doFinish (@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form,BindingResult bindingResult,ModelMap model,HttpServletRequest request,HttpServletResponse response) throws SystemException, IOException, ServletException  {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		model.addAttribute("mapping","view");
	       String forward=super.doFinish( form,model, request, response);
	       postExecute(form,request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ="/viewInternalUser/",params={"action=printPDF"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrintPDF (@Valid @ModelAttribute("addEditUserForm") AddEditUserForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doPrintPDF( form, request, response);
	       postExecute(form,request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }

	
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	 @Autowired
	 private AddEditInternalUserValidator addEditInternalUserValidator;
	 
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(addEditInternalUserValidator);
	    
	   
	}
}