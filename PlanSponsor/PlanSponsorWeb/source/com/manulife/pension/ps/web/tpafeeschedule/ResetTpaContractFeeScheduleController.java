package com.manulife.pension.ps.web.tpafeeschedule;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.manulife.pension.ps.web.fee.FeeUIHolder;
import com.manulife.pension.ps.web.tpafeeschedule.BaseFeeScheduleForm.PageMode;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO;

@Controller
@RequestMapping( value ="/resetToStandardFeeSchedule")
@SessionAttributes({"tpaCustomizeContractForm"})

public class ResetTpaContractFeeScheduleController extends BaseFeeScheduleController {
	
	@ModelAttribute("tpaCustomizeContractForm") 
	public TPAContractFeeScheduleForm populateForm() 
	{
		return new TPAContractFeeScheduleForm();
		}
	
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/tpa/resetToTpaStandardFeeSchedule.jsp");
		forwards.put("default","/tpa/resetToTpaStandardFeeSchedule.jsp");
		forwards.put("submit","redirect:/do/viewTpaCustomizedContractFee/");
		forwards.put("back","redirect:/do/editTpaCustomizedContractFee/");
		}

	

	/**
	 * shows the reset to standard page
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
	@RequestMapping(  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("tpaCustomizeContractForm") TPAContractFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        } 
	
		String isResetTostandardSchedule = (String) request.getSession()
				.getAttribute(Constants.RESET_CONTRACT_CUSTOM_FEE_SCHEDULE);

		if (StringUtils.isBlank(form.getSelectedContract())) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}

		if (StringUtils.isBlank(isResetTostandardSchedule)) {
			// if lock already exists, go to edit mode, else go to Home page.
			final boolean lockObtained = checkIfUserObtainedLockOnContract(
					form.getSelectedContract(), request);
			if (!lockObtained) {
				return Constants.HOMEPAGE_FINDER_FORWARD;
			} else {
				return forwards.get("back");
			}

		}
		request.getSession().removeAttribute(
				Constants.RESET_CONTRACT_CUSTOM_FEE_SCHEDULE);

		// retrieve standard schedule fees and apply plan provision validations
		// to show them on the page.
		List<FeeUIHolder> tpaStdScheduleFees = new ArrayList<FeeUIHolder>();

		for (ContractCustomizedFeeVO contractCustomizedFeeVO : form
				.getTpaStandardScheduleFees()) {
			tpaStdScheduleFees.add(new FeeUIHolder(contractCustomizedFeeVO));
		}

		loadPlanProvisionsToTpaStandardFees(
				Integer.parseInt(form.getSelectedContract()),
				tpaStdScheduleFees);

		form.setStandardScheduleFeesToReset(tpaStdScheduleFees);

		return forwards.get("default");
	}
	
	/**
	 * Method to reset the custom fees to standard.
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
	@RequestMapping(params={"action=submit"}   , method =  {RequestMethod.POST}) 
	public String doSubmit (@Valid @ModelAttribute("tpaCustomizeContractForm") TPAContractFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        } 

		
		long profileId = getUserProfile(request).getPrincipal()
		.getProfileId();

		FeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID)
				.deleteAllCustomizedTpaFeesForContract(
						Integer.parseInt(form.getSelectedContract()),
						String.valueOf(profileId), "Resetted To Standard Schedule.");

		form.setPageMode(PageMode.Edit);
		fireIpiHostingCustomScheduleChangeEvent(
				ResetTpaContractFeeScheduleController.class, "doSubmit", Integer
						.parseInt(form.getSelectedContract()), profileId, isCustomEqualsToStandatd(form));
		return forwards.get(form.getAction());
	}
	
	
	private boolean isCustomEqualsToStandatd(
			TPAContractFeeScheduleForm form) {
		boolean isEqual = false;
		List<ContractCustomizedFeeVO> tpaStandardScheduleFees = form
				.getTpaStandardScheduleFees();
		
		List<ContractCustomizedFeeVO> tpaCustomizedScheduleFees = new ArrayList<ContractCustomizedFeeVO>();
		// filtering out non-zero fee values.
		for (ContractCustomizedFeeVO fee : form.getTpaCustomizedScheduleFees()) {
			if (!((fee.getFeeAmount() != null && BigDecimal.ZERO.compareTo(fee
					.getFeeAmount()) == 0) || (fee.getFeePercentage() != null && BigDecimal.ZERO
					.compareTo(fee.getFeePercentage()) == 0))) {
				tpaCustomizedScheduleFees.add(fee);
			}
		}
		
		if (tpaStandardScheduleFees.isEmpty()
				|| tpaCustomizedScheduleFees.isEmpty()) {
			return false;
		}


		if (tpaStandardScheduleFees.size() != tpaCustomizedScheduleFees.size()) {
			return false;
		}

		int numberOfMatches = 0;

		for (ContractCustomizedFeeVO stdFee : tpaStandardScheduleFees) {
			if (tpaCustomizedScheduleFees.contains(stdFee)) {
				++numberOfMatches;
			}
		}

		if (numberOfMatches == tpaCustomizedScheduleFees.size()) {
			isEqual = true;
		}

		return isEqual;
	}
	
	/**
	 * go back to edit page
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
	@RequestMapping( params={"action=back"}   ,method =  {RequestMethod.POST}) 
	public String doBack (@Valid @ModelAttribute("tpaCustomizeContractForm") TPAContractFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        } 
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doBack");
		}

		return forwards.get(form.getAction());
		
	}

}
