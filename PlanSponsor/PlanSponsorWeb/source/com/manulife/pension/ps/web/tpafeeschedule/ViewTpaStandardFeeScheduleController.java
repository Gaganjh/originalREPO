package com.manulife.pension.ps.web.tpafeeschedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.tpafeeschedule.TpaStandardFeeScheduleForm.PageMode;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.service.fee.util.Constants.FeeScheduleType;
import com.manulife.pension.service.fee.valueobject.FeeDataVO;
import com.manulife.pension.service.fee.valueobject.FeeScheduleUpdateDetails;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * Action class to handle TPA Standard Fee Schedule Data view
 * 
 * @author Akhil Khanna
 * 
 */
@Controller
@RequestMapping( value = "/viewTpaStandardFeeSchedule/")
@SessionAttributes({"tpaStandardFeeScheduleForm"})

public class ViewTpaStandardFeeScheduleController extends BaseFeeScheduleController {
	
	@ModelAttribute("tpaStandardFeeScheduleForm") 
	public TpaStandardFeeScheduleForm populateForm()
	{
		return new TpaStandardFeeScheduleForm();
		}
	public static HashMap<String,String> forwards =new HashMap<String,String>();
	static{
		forwards.put("input","/tpafeeschedule/viewTpaStandardFeeSchedule.jsp");
		forwards.put("viewTpaStandardFeeData","/tpafeeschedule/viewTpaStandardFeeSchedule.jsp");
		forwards.put("default","/tpafeeschedule/viewTpaStandardFeeSchedule.jsp");
		forwards.put("goToSelectTpaFirmAction","redirect:/do/feeSchedule/selectTpaFirm/");
		forwards.put("changeHistory","redirect:/do/changeHistoryTpaStandardFeeSchedule/");
		forwards.put("edit","redirect:/do/editTpaStandardFeeSchedule/");
		forwards.put("customizeContract","redirect:/do/tpafee/contractSearch/");
		forwards.put("defaultForInternalUser","/tpafeeschedule/viewStandardFeeScheduleForInternalUser.jsp");}

	
	public String preExecute(TpaStandardFeeScheduleForm form, HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		String forward = super.preExecute(form, request,
				response);
		if (forward != null) {
			return forward;
		}

		// if a tpaId existing in request it means its navigated from
		// MessageCenter.
		if (request.getParameter(Constants.TPA_FIRM_ID_REQUEST_PARAM) != null) {
			String selectedTpaFirmId = request.getParameter(Constants.TPA_FIRM_ID_REQUEST_PARAM);

			Map<Integer, String> accessibleTpaFirms = getAllTpaFirmsForTpaLoginUser(request);

			// validate the tpaId whether it is belongs to the login User, if
			// the user bookmark the url and changes the tpaId.
			if (accessibleTpaFirms.containsKey(Integer
					.parseInt(selectedTpaFirmId))) {
				request.getSession().setAttribute(
						Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID,
						selectedTpaFirmId);
				// not to show back button.
				request.getSession().setAttribute(
						Constants.REDIRECTING_TO_VIEW_FEE_SCHEDULE_PAGE,
						Boolean.TRUE.toString());
			} else {
				forward = Constants.HOMEPAGE_FINDER_FORWARD;
			}	
		}

		return forward;
	}

	/**
	 *Method to get the TPA's Standard Fee Schedule details
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param actionForm
	 *            AutoForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 */

	@RequestMapping(  method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doDefault(@Valid @ModelAttribute("tpaStandardFeeScheduleForm") TpaStandardFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, "/")?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
	

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}
		
		UserProfile userProfile = getUserProfile(request);
		
		
		if(request.getSession().getAttribute(Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID) != null) {
			String selectedTpaFirmId = (String) request.getSession().getAttribute(Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID);
			form.setSelectedTpaFirmId(selectedTpaFirmId);
			form.resetData();
			request.getSession().removeAttribute(Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID);
		}
		
		/*If the user is redirected to the view page without going through the Select TPA firm page
		 then the back button should not be shown*/
		if (request.getSession().getAttribute(
				Constants.REDIRECTING_TO_VIEW_FEE_SCHEDULE_PAGE) != null) {
			form.setShowBackButton(Boolean
					.valueOf((String) request.getSession().getAttribute(
							Constants.REDIRECTING_TO_VIEW_FEE_SCHEDULE_PAGE)));
			request.getSession().removeAttribute(
					Constants.REDIRECTING_TO_VIEW_FEE_SCHEDULE_PAGE);
		}
		
		if (StringUtils.isBlank(form
						.getSelectedTpaFirmId())) {
			return forwards.get(SELECT_TPA_FIRM_PAGE);
		}

		// Populate the form bean
		populateStandardFeeDetailsToForm(form,
				request);

		// release lock in case coming from edit or confirm page buttons
		if (form.getPageMode() != null
				&& (form.getPageMode().equals(
						PageMode.EDIT) || form
						.getPageMode().equals(PageMode.PREVIEW))) {
			releaseLock(Integer.parseInt(form
					.getSelectedTpaFirmId()), request,
					LockHelper.EDIT_TPA_STANDARD_SCHEDULE_PAGE);
			request.getSession(false).removeAttribute(PsBaseUtil.ERROR_KEY);
		}
		
		form.setPageMode(PageMode.VIEW);
		form.setEditPageModified(false);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}
		
		//If the user is an Internal User then redirect to view page for internal user
		if (userProfile.getRole().isInternalUser()){
			return forwards.get(TPA_STANDARD_FEE_SCHEDULE_VIEW_PAGE_FOR_INTERNAL_USER);
		}

		return forwards.get(TPA_STANDARD_FEE_SCHEDULE_VIEW_PAGE);
	}


	/**
	 * Method to get the TPA Standard FeeSchedule Data for the view page
	 * 
	 * @param tpaStandardFeeScheduleForm
     * @param request
	 * @return feeDataItems
	 * @throws SystemException
	 */
	private List<FeeDataVO> getTpaStandardFeeScheduleData(
			TpaStandardFeeScheduleForm tpaStandardFeeScheduleForm,
			HttpServletRequest request) throws SystemException {
		List<FeeDataVO> feeDataItems = new ArrayList<FeeDataVO>();
		String tpaFirmId = tpaStandardFeeScheduleForm
				.getSelectedTpaFirmId();

		FeeServiceDelegate feeDelegate = FeeServiceDelegate
				.getInstance(CommonConstants.PS_APPLICATION_ID);
		
		if (tpaFirmId != null) {
			List<FeeDataVO> items =  feeDelegate.getTpaStandardFeeScheduleData(Integer
					.parseInt(tpaFirmId));
			// add values greater than 0
			for(FeeDataVO vo : items) {
				if(vo.getFeeValueAsDecimal() > 0.0d) {
					feeDataItems.add(vo);
				}
			}
		}
		
		return feeDataItems;
	}

 	/**
	 * Method to populate the Standard Fee details to form
	 * @param tpaStandardFeeScheduleForm
     * @param request
	 * @throws SystemException
	 */ 	
	 	
 	@SuppressWarnings("unchecked")
	public void populateStandardFeeDetailsToForm(
			TpaStandardFeeScheduleForm tpaStandardFeeScheduleForm,
			HttpServletRequest request) throws SystemException {

		UserProfile userProfile = getUserProfile(request);

		Map<Integer, String> tpaFirmMap = new TreeMap<Integer, String>();
		if (userProfile.getRole().isInternalUser()) {
			UserInfo userInfo = (UserInfo) request.getSession(false)
					.getAttribute(Constants.TPA_USER_INFO);
			Collection<TPAFirmInfo> tpaFirmInfo = userInfo
					.getTpaFirmsAsCollection();
			for (TPAFirmInfo firmInfo : tpaFirmInfo) {
				tpaFirmMap.put(firmInfo.getId(), firmInfo.getName());
			}
			tpaStandardFeeScheduleForm.setShowChangeHistoryLink(true);
		} 
		else { 
		
		/* If the the user is not an internal user userInfo will be taken from
		 the security service delegate*/
			
			String companyCode = Constants.COMPANY_NAME_NY
					.equalsIgnoreCase(Environment.getInstance()
							.getSiteLocation()) ? Constants.COMPANY_ID_NY
					: Constants.COMPANY_ID_US;
		
			UserInfo userInfo = SecurityServiceDelegate.getInstance()
					.getUserInfo(userProfile.getPrincipal());
			tpaFirmMap = TPAServiceDelegate.getInstance()
					.retrieveTpaFirmsByTPAUserProfileId(
							userProfile.getPrincipal().getProfileId(),
							companyCode);
			tpaStandardFeeScheduleForm
					.setOtherThanInternalUserPresent(true);
			tpaStandardFeeScheduleForm
					.setTpaUserManager(isTpaUserManager(userInfo,
							tpaStandardFeeScheduleForm.getSelectedTpaFirmId()));
			tpaStandardFeeScheduleForm
					.setShowChangeHistoryLink(isTpaUserManager(
							userInfo, tpaStandardFeeScheduleForm.getSelectedTpaFirmId()));
		}

		/*populate the Standard Fee Schedule List obtained form the fee service delegate and set it to form*/
		List<FeeDataVO> actualFeeItemsList = getTpaStandardFeeScheduleData(
				tpaStandardFeeScheduleForm, request);
		tpaStandardFeeScheduleForm
				.setActualFeeItemsList(actualFeeItemsList);

		String lastUpdatedTs = StringUtils.EMPTY;
		String lastUpdatedUserName = StringUtils.EMPTY;

		/*Logic to get the last updated date and its corresponding user name from the actualFeeItemsList*/
		FeeScheduleUpdateDetails standardFeeScheduleDetails = FeeServiceDelegate
				.getInstance(CommonConstants.PS_APPLICATION_ID)
				.getFeeScheuleUpdateDetails(
						null,
						tpaStandardFeeScheduleForm.getSelectedTpaFirmId(),
						FeeScheduleType.StandardFeeSchedule);
		
		if (standardFeeScheduleDetails != null) {
			if (standardFeeScheduleDetails.getLastUpdatedTs() != null) {
				lastUpdatedTs = DateRender
						.formatByPattern(new Date(standardFeeScheduleDetails
								.getLastUpdatedTs().getTime()), "",
								RenderConstants.EXTRA_LONG_MDY);
			}
			lastUpdatedUserName = standardFeeScheduleDetails
					.getLastUpdatedUserName();
		}

		tpaStandardFeeScheduleForm.setLastUpdatedDate(lastUpdatedTs);
		tpaStandardFeeScheduleForm.setLastUpdatedUserName(lastUpdatedUserName);
	}
 	
 	/**
	 * Method to go to the Edit TPA's Standard Fee Schedule details
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param actionForm
	 *            AutoForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 */
 	@RequestMapping(params={"action=edit"}   , method =  {RequestMethod.POST}) 
 	public String doEdit (@Valid @ModelAttribute("tpaStandardFeeScheduleForm") TpaStandardFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
 	throws IOException,ServletException, SystemException {
 		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, "/")?forward:forwards.get(forward);
		}
 		if(bindingResult.hasErrors()){
	        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	        	if(errDirect!=null){
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	        	}
	        }
		Integer tpaFirmId = Integer.parseInt(form.getSelectedTpaFirmId());
		
		// try to obtain a lock for edit page, if lock not obtained go to view page
		final boolean lockObtained = obtainLock(tpaFirmId,
				request, LockHelper.EDIT_TPA_STANDARD_SCHEDULE_PAGE);
		if (!lockObtained) {
			handleObtainLockFailure(tpaFirmId, request,
					LockHelper.EDIT_TPA_STANDARD_SCHEDULE_PAGE);
			return forwards.get(TPA_STANDARD_FEE_SCHEDULE_VIEW_PAGE);
		} 
		
		return forwards.get(EDIT_STANDARD_TPA_PAGE);
	}
	
	/**
	 *Method to get the TPA's Standard Fee Schedule Change History details
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param actionForm
	 *            AutoForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 */
 	

 		@RequestMapping(params={"action=changeHistory"}   , method =  {RequestMethod.POST}) 
 	 	public String doChangeHistory (@Valid @ModelAttribute("tpaStandardFeeScheduleForm") TpaStandardFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
 	 	throws IOException,ServletException, SystemException {
 			String forward = preExecute(form, request, response);
 			if(StringUtils.isNotBlank(forward)) {
 				return StringUtils.contains(forward, "/")?forward:forwards.get(forward);
 			}
 			if(bindingResult.hasErrors()){
 	        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	        	if(errDirect!=null){
 	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
 	        	}
 	        }
 			request.getSession().setAttribute(
				Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID,
				form.getSelectedTpaFirmId());
		return forwards.get(CHANGE_HISTORY_STANDARD_TPA_PAGE);

	}
	
	/**
	 *Method to get the TPA's Standard Fee Schedule Change History details
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param actionForm
	 *            AutoForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 */
 		@RequestMapping(params={"action=customizeContract"} ,  method =  {RequestMethod.POST}) 
 	 	public String doCustomizeContract (@Valid @ModelAttribute("tpaStandardFeeScheduleForm") TpaStandardFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
 	 	throws IOException,ServletException, SystemException {
 			String forward = preExecute(form, request, response);
 			if(StringUtils.isNotBlank(forward)) {
 				return StringUtils.contains(forward, "/")?forward:forwards.get(forward);
 			}
 			if(bindingResult.hasErrors()){
 	        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
 	        	if(errDirect!=null){
 	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
 	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
 	        	}
 	        }
		
		request.getSession().setAttribute(Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID, form.getSelectedTpaFirmId());
		return forwards.get(CUSTOMIZE_CONTRACT_PAGE);
		
	}
	
	/**
	 *Method to go back from this page
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param actionForm
	 *            AutoForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 */
 		@RequestMapping(params={"action=back"} , method =  {RequestMethod.POST}) 
 	 	public String doBack (@Valid @ModelAttribute("tpaStandardFeeScheduleForm") TpaStandardFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
 	 	throws IOException,ServletException, SystemException {
 			String forward = preExecute(form, request, response);
 			if(StringUtils.isNotBlank(forward)) {
 				return StringUtils.contains(forward, "/")?forward:forwards.get(forward);
 			}
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
		
		return forwards.get(SELECT_TPA_FIRM_PAGE);
	}
}
