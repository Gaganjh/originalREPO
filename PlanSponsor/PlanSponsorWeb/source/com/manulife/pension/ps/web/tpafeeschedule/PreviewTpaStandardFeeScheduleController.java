package com.manulife.pension.ps.web.tpafeeschedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
import com.manulife.pension.event.IpiHostingStandardScheduleChangeEvent;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.tpafeeschedule.TpaStandardFeeScheduleForm.PageMode;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO.TpaCustomFeeCodes;
import com.manulife.pension.service.fee.valueobject.FeeDataVO;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * Action class to handle TPA Standard Fee Schedule Modified Data to be
 * displayed on the Confirmation page
 * 
 * @author Akhil Khanna
 * 
 */
@Controller
@RequestMapping( value = "/previewTpaStandardFeeSchedule/")
@SessionAttributes({"tpaStandardFeeScheduleForm"})

public class PreviewTpaStandardFeeScheduleController extends BaseFeeScheduleController {
	@ModelAttribute("tpaStandardFeeScheduleForm")
	public TpaStandardFeeScheduleForm populateForm() 
	{
		return new TpaStandardFeeScheduleForm();
		}
	

public static HashMap<String,String>forwards=new HashMap<String,String>();
static{
		forwards.put("input","/tpafeeschedule/previewTpaStandardFeeSchedule.jsp");
		forwards.put("previewTpaStandardFeeData","/tpafeeschedule/previewTpaStandardFeeSchedule.jsp");
		forwards.put("goToViewAction","redirect:/do/viewTpaStandardFeeSchedule/"); 
		forwards.put("goToEditAction","redirect:/do/editTpaStandardFeeSchedule/");
		forwards.put("goToSelectTpaFirmAction","redirect:/do/feeSchedule/selectTpaFirm/");
		}

	/**
	 * Method to get the TPA's Standard Fee Schedule modified details from the
	 * edit page to the Confirmation page
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
	 * @throws SystemException
	 */
@RequestMapping(method =  {RequestMethod.GET}) 
public String doDefault(@Valid @ModelAttribute("tpaStandardFeeScheduleForm") TpaStandardFeeScheduleForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
throws IOException,ServletException, SystemException {
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
		
		// if lock already exists, go to edit mode, else go to Home Page.
		final boolean lockObtained = checkIfUserObtainedLockOnTpaFirm(
				actionForm.getSelectedTpaFirmId(), request);
		if (!lockObtained) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				userProfile.getPrincipal());

		if (userProfile.getRole().isInternalUser()) {
			return forwards.get(SELECT_TPA_FIRM_PAGE);
		} else {
			// If TPA is not having TPA User Manager permissions redirect to
			// Select
			// TPA firm page
			if (!isTpaUserManager(userInfo,
					actionForm.getSelectedTpaFirmId())) {
				return forwards.get(SELECT_TPA_FIRM_PAGE);
			}
		}
		
		List<FeeDataVO> previewFeeItems = new ArrayList<FeeDataVO>();
		previewFeeItems = actionForm
				.getPreviewFeeItemsList();

		if (!PageMode.EDIT.equals(actionForm
				.getPageMode())
				|| previewFeeItems.isEmpty()) {
			return forwards.get(TPA_STANDARD_FEE_SCHEDULE_EDIT_ACTION);
		}

		actionForm.setPageMode(PageMode.PREVIEW);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}

		return forwards.get(TPA_STANDARD_FEE_SCHEDULE_PREVIEW_PAGE);

	}

	/**
	 * Method to save the TPA's Standard Fee Schedule modified details obtained
	 * from the Confirmation page and forward to the view page with the updated
	 * details
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
	 * @throws SystemException
	 */
@RequestMapping(params={"action=submit"}   , method =  {RequestMethod.POST}) 
public String doSubmit (@Valid @ModelAttribute("tpaStandardFeeScheduleForm") TpaStandardFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
throws IOException,ServletException, SystemException {
	if(bindingResult.hasErrors()){
    	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    	if(errDirect!=null){
         request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
         return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
    	}
    }

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSubmit");
		}

		// If the token is not valid then forward to view page
		/*if (!isTokenValid(request)) {
			logger.error("Detect double submission");
			return forwards.get(TPA_STANDARD_FEE_SCHEDULE_VIEW_ACTION);
		}

		resetToken(request);*/
		
		StringBuffer updatedFees = new StringBuffer();

		
		UserProfile userProfile = getUserProfile(request);

		List<FeeDataVO> submittedList = new ArrayList<FeeDataVO>();
		submittedList = form
				.getPreviewFeeItemsList();

		assignFeeCode(submittedList, form
				.getAdditionalFeeItemsList());
		
		for (FeeDataVO submittedVO : submittedList) {
			submittedVO.setLoginProfileId(userProfile.getPrincipal()
					.getProfileId());
			// to get the updated fee codes.
			updatedFees.append("'")
					.append(StringUtils.trimToEmpty(submittedVO.getFeeCode()))
					.append("'").append(", ");
		}

		if (StringUtils.isNotBlank(updatedFees.toString())) {
			updatedFees.delete(updatedFees.length() - ", ".length(),
					updatedFees.length());
		}
		
		FeeServiceDelegate delegate = FeeServiceDelegate.getInstance("PS");
		delegate.saveStandardFeeScheduleData(submittedList, Integer
				.parseInt(form.getSelectedTpaFirmId()));

		// trigger IpiHostingStandardScheduleChangeEvent
		fireIpiHostingStandardScheduleChangeEvent(Integer
				.parseInt(form.getSelectedTpaFirmId()),
				getUserProfile(request), updatedFees);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doSubmit");
		}

		return forwards.get(TPA_STANDARD_FEE_SCHEDULE_VIEW_ACTION);
	}

	private void assignFeeCode(List<FeeDataVO> previewFeeList,
			List<FeeDataVO> actualFeeList) {
		List<String> usedFeeCodes = new ArrayList<String>();
		List<String> unUsedFeeCodes = new ArrayList<String>();

		for (FeeDataVO feeData : actualFeeList) {
			if (StringUtils.equals("TN", feeData.getFeeCategoryCode())
					&& StringUtils.isNotBlank(feeData.getFeeCode())
					&& !feeData.isDeletedIndicator()) {
				usedFeeCodes.add(feeData.getFeeCode());
			}
		}

		for (TpaCustomFeeCodes cust : TpaCustomFeeCodes.values()) {
			if (!usedFeeCodes.contains(cust.getCode())) {
				unUsedFeeCodes.add(cust.getCode());
			}
		}

		for (FeeDataVO feeData : previewFeeList) {
			if (StringUtils.equals("TN", feeData.getFeeCategoryCode())
					&& StringUtils.isBlank(feeData.getFeeCode())) {
				feeData.setFeeCode(unUsedFeeCodes.get(0));
				unUsedFeeCodes.remove(0);
			}
		}
	}

	private void fireIpiHostingStandardScheduleChangeEvent(int tpaFirmId,
			UserProfile userProfile, StringBuffer updatedTpaStdFees) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> fireIpiHostingStandardScheduleChangeEvent");
		}

		IpiHostingStandardScheduleChangeEvent StdScheduleChangeEvent = new IpiHostingStandardScheduleChangeEvent(
				"PreviewTpaStandardFeeScheduleAction", "doSubmit");
		StdScheduleChangeEvent.setTpaFirmId(tpaFirmId);
		StdScheduleChangeEvent.setInitiator(userProfile.getPrincipal()
				.getProfileId());
		StdScheduleChangeEvent.setUpdatedTpaFees(updatedTpaStdFees.toString());
		Set<Integer> accessibleContracts = userProfile
				.getMessageCenterAccessibleContracts();
		StringBuffer contracts = new StringBuffer();
		for (Integer contract : accessibleContracts) {
			contracts.append(String.valueOf(contract)).append(",");
		}
		StdScheduleChangeEvent
				.setAccessibleContracts(StringUtils.substring(contracts
						.toString(), 0, contracts.toString().lastIndexOf(",")));
		EventClientUtility.getInstance(Environment.getInstance().getAppId())
				.prepareAndSendJMSMessage(StdScheduleChangeEvent);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- fireIpiHostingStandardScheduleChangeEvent");
		}
	}

}