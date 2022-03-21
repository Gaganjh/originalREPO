package com.manulife.pension.ps.web.tpafeeschedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

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
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.fee.FeeUIHolder;
import com.manulife.pension.ps.web.tpafeeschedule.BaseFeeScheduleForm.PageMode;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * 
 * Action Class For Edit TPA Customize Contract Fee Page
 * 
 * @author Siby Thomas
 *
 */
@Controller
@RequestMapping( value = "/editTpaCustomizedContractFee/")
@SessionAttributes({"tpaCustomizeContractForm"})
public class TPAContractFeeScheduleEditController extends BaseFeeScheduleController {
	
	@ModelAttribute("tpaCustomizeContractForm") 
	public TPAContractFeeScheduleForm populateForm() 
	{
		return new TPAContractFeeScheduleForm();
	}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/tpa/editTpaContractFeeSchedule.jsp");
		forwards.put("editTpaCustomizedContractFee","/tpa/editTpaContractFeeSchedule.jsp");
		forwards.put("viewTpaCustomizedContractFee","redirect:/do/viewTpaCustomizedContractFee/");
		forwards.put("confirmTpaCustomizedContractFee","redirect:/do/confirmTpaCustomizedContractFee/"); 
		forwards.put("confirmTpaCustomizedContractFee","redirect:/do/confirmTpaCustomizedContractFee/");
		forwards.put("resetToStandardSchedule","redirect:/do/resetToStandardFeeSchedule/");
	}

	
	/**
	 * 
	 * handle default request from page
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * 
	 * @return ActionForward
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("tpaCustomizeContractForm") TPAContractFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		
		// if lock already exists, go to edit mode, else go to Home page.
		final boolean lockObtained = checkIfUserObtainedLockOnContract(form.getSelectedContract(), request);
		if (!lockObtained) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		// If User bookmarks to edit page and is not having 404a5FeeAccessPermission then redirect to view page.
		if(!form.isFee404a5AccessPermission()){
			return forwards.get(Constants.VIEW_TPA_CUSTOMIZE_CONTRACT_PAGE);
		}
		
		// // If User coming back from confirm page, hold the changed values.
		if (PageMode.Confirm.equals(form
				.getPageMode())) {
			form.setTpaFeesCustomized(FeeUIHolder.addEmptyCustomFees(form.getTpaFeesCustomized()));
			return forwards.get(Constants.EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE);
		}
		
		FeeServiceDelegate feeServiceDelegate = FeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
		
		// get all standard fees
		List<ContractCustomizedFeeVO> tpaAllStandardFeeItems = feeServiceDelegate
				.getStandardTpaFeeList();
		ArrayList<FeeUIHolder> tpaAllStandardFees = new ArrayList<FeeUIHolder>();
		for (ContractCustomizedFeeVO contractCustomizedFeeVO : tpaAllStandardFeeItems) {
			FeeUIHolder fee = new FeeUIHolder(contractCustomizedFeeVO);
			fee.setFeeValue(FeeUIHolder.ZERO_VALUE);
			tpaAllStandardFees.add(fee);
		}

		// replace standard fees with TPA customized ones
		List<FeeUIHolder> tpaSelectedStandardFees = form.getTpaFeesStandard();
		for (FeeUIHolder fee : tpaSelectedStandardFees) {
			if (tpaAllStandardFees.contains(fee)) {
				tpaAllStandardFees.remove(fee);
				tpaAllStandardFees.add(fee);
			}
		}
		
		for (FeeUIHolder fee : tpaAllStandardFees) {
			if (fee.getFeeValueAsDecimal() == 0.0d) {
				fee.setNotes(StringUtils.EMPTY);
			}
		}

		Collections.sort(tpaAllStandardFees, new Comparator<FeeUIHolder>() {
			@Override
			public int compare(FeeUIHolder object1, FeeUIHolder object2) {
				return object1.getSortOrder().compareTo(object2.getSortOrder());
			}
		});
				
		form.setTpaFeesStandard(tpaAllStandardFees);
		form.storeClonedForm();
			
		//load plan provisions to the respective features.
		loadPlanProvisionsToTpaStandardFees(Integer.parseInt(form.getSelectedContract()), form.getTpaFeesStandard());
		
		form.setResetToStandardSchedule((!form.getTpaStandardScheduleFees()
				.isEmpty() && !form.getTpaCustomizedScheduleFees().isEmpty()));
		
        // add an empty row, if fee count less than 5
		form.setTpaFeesCustomized(FeeUIHolder.addEmptyCustomFees(form.getTpaFeesCustomized()));
		
		return forwards.get(Constants.EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE);

	}

	/**
	 * go back to view page
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * 
	 * @return ActionForward
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(params={"action=back"}   , method =  {RequestMethod.POST}) 
	public String doBack (@Valid @ModelAttribute("tpaCustomizeContractForm") TPAContractFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		form.reset( request);
		form.setPageMode(PageMode.Edit);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doBack");
		}

		return forwards.get(Constants.VIEW_TPA_CUSTOMIZE_CONTRACT_PAGE);
		
	}
	
	/**
	 * go back to view page
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * 
	 * @return ActionForward
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping( params={"action=resetToStandardSchedule"} , method =  {RequestMethod.POST}) 
	public String doResetToStandardSchedule (@Valid @ModelAttribute("tpaCustomizeContractForm") TPAContractFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry <- doResetToStandardSchedule");
		}	
		form.setPageMode(PageMode.Edit);
		request.getSession().setAttribute(
				Constants.RESET_CONTRACT_CUSTOM_FEE_SCHEDULE,
				Boolean.TRUE.toString());
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doResetToStandardSchedule");
		}

		return forwards.get(Constants.FORWARD_RESET_TO_STANDARD_SCHEDULE_PAGE);
		
	}

	/**
	 * go to confirm page if no error, else stay in same page
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * 
	 * @return ActionForward
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 * @throws CloneNotSupportedException 
	 */
	
	@RequestMapping( params={"action=confirm"}  , method =  {RequestMethod.POST}) 
	public String doConfirm (@Valid @ModelAttribute("tpaCustomizeContractForm") TPAContractFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException, CloneNotSupportedException {

		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		
		// remove empty fees added
		form.setTpaFeesCustomized(removeBlankFeeObjects(form.getTpaFeesCustomized())) ;
		
		errorMessages = FeeScheduleDataValidator.validateCustomContractScheduleFees(form);

		// if errors present go back to same  page
		if (!(errorMessages.isEmpty())) {
			form.setTpaFeesCustomized(FeeUIHolder.addEmptyCustomFees(form.getTpaFeesCustomized()));
			setErrorsInSession(request, errorMessages);
			return forwards.get(Constants.EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE);
		} 
		
		// get edited values
		List<FeeUIHolder> editedTpaStandardFees = FeeUIHolder.removeEmptyValueFeeObjects(form.getTpaFeesStandard());
		List<FeeUIHolder> editedTpaCustomizedFees = FeeUIHolder.removeEmptyValueFeeObjects(form.getTpaFeesCustomized());
	

		// get actual values
		List<FeeUIHolder> previousTpaStandardFees = ((TPAContractFeeScheduleForm) form
				.getClonedForm()).getTpaFeesStandard();
		previousTpaStandardFees = FeeUIHolder.removeEmptyValueFeeObjects(previousTpaStandardFees);
		
		List<FeeUIHolder> previousTpaCustomizedFees = ((TPAContractFeeScheduleForm) form
				.getClonedForm()).getTpaFeesCustomized();
		previousTpaCustomizedFees = FeeUIHolder.removeEmptyValueFeeObjects(previousTpaCustomizedFees);

		List<FeeUIHolder> changedTpaStandardFees  = FeeUIHolder.getChangedFeeItems(editedTpaStandardFees, previousTpaStandardFees);
		List<FeeUIHolder> changedTpaCustomizedFees  = FeeUIHolder.getChangedFeeItems(editedTpaCustomizedFees, previousTpaCustomizedFees);


        boolean isChangesMade = false;
       if (!changedTpaStandardFees.isEmpty()  || !changedTpaCustomizedFees.isEmpty()) {
        	isChangesMade = true;
        }
		
		// if no changes detected, stay back in same page with error
		if (!isChangesMade) {
			form.setTpaFeesCustomized(FeeUIHolder.addEmptyCustomFees(form.getTpaFeesCustomized()));
			errorMessages.add(new ValidationError(new String[] { GLOBAL_ERROR }, ErrorCodes.SAVING_WITH_NO_CHANGES));
			setErrorsInSession(request, errorMessages);
			return forwards.get(Constants.EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE);
		}

		form.setUpdatedTpaFeesStandard(changedTpaStandardFees);
		form.setUpdatedTpaFeesCustomized(changedTpaCustomizedFees);
		form.setPageMode(PageMode.Edit);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doConfirm");
		}

		return forwards.get(Constants.CONFIRM_TPA_CUSTOMIZE_CONTRACT_PAGE);
		
	}
}