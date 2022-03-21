package com.manulife.pension.ps.web.tpafeeschedule;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

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
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.fee.FeeUIHolder;
import com.manulife.pension.ps.web.tpafeeschedule.BaseFeeScheduleForm.PageMode;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO.FeeCategoryCode;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO.TpaCustomFeeCodes;

/**
 * 
 * Action Class For Confirm TPA Customize Contract Fee Page
 * 
 * @author Siby Thomas 
 *
 */ 
@Controller
@RequestMapping( value = "/confirmTpaCustomizedContractFee/")
@SessionAttributes({"tpaCustomizeContractForm"})

public class TPAContractFeeScheduleConfirmController extends BaseFeeScheduleController  {
	@ModelAttribute("tpaCustomizeContractForm") 
	public TPAContractFeeScheduleForm populateForm()
	{
		return new TPAContractFeeScheduleForm();
		}

	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/tpa/confirmTpaContractFeeSchedule.jsp");
		forwards.put("confirmTpaCustomizedContractFee","/tpa/confirmTpaContractFeeSchedule.jsp");
		forwards.put("editTpaCustomizedContractFee","redirect:/do/editTpaCustomizedContractFee/");
		forwards.put("viewTpaCustomizedContractFee","redirect:/do/viewTpaCustomizedContractFee/");
		}

	@RequestMapping(method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("tpaCustomizeContractForm") TPAContractFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
	
		// if lock already exists, go to edit mode, else go to Home page.
		final boolean lockObtained = checkIfUserObtainedLockOnContract(form.getSelectedContract(), request);
		if (!lockObtained) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		boolean isChangesMade = false;
        if (!form.getUpdatedTpaFeesStandard().isEmpty()  || !form.getUpdatedTpaFeesCustomized().isEmpty()) {
        	isChangesMade = true;
        }
       
		// if no changes to show in this page or not directing from edit page then go to edit page.
		if(!isChangesMade || !PageMode.Edit.equals(form.getPageMode())){
			return forwards.get(Constants.EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE);
		}
		
		if(isChangesMade){
        	form.setPageMode(PageMode.Confirm);
        }

		return forwards.get(Constants.CONFIRM_TPA_CUSTOMIZE_CONTRACT_PAGE);
	}
	
	@RequestMapping( params = {"action=back"},method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doBack (@Valid @ModelAttribute("tpaCustomizeContractForm") TPAContractFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
		
		form.setPageMode(PageMode.Confirm);
		return forwards.get(Constants.EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE);
	}
	
	@RequestMapping(  params = {"action=save"},method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSave (@Valid @ModelAttribute("tpaCustomizeContractForm") TPAContractFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
		
		UserProfile userProfile = getUserProfile(request);
		
		int contractId = Integer.valueOf(form.getSelectedContract());
		
		FeeServiceDelegate feeServiceDelegate = FeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
		
		List<ContractCustomizedFeeVO> changedFeeItems = new ArrayList<ContractCustomizedFeeVO>();
		
		// if customizing for first time, copy over non ) value standard schedule fees.
		if(form.getTpaCustomizedScheduleFees().isEmpty()) {
			
			// add updated/existing standard TPA fees
			List<FeeUIHolder> updatedTpaFees = new ArrayList<FeeUIHolder>();
			for(FeeUIHolder fee : form.getUpdatedTpaFeesStandard()){
				updatedTpaFees.add(fee);
			}
			nextFee : for(FeeUIHolder fee : form.getTpaFeesStandard()) {
				if(updatedTpaFees.contains(fee)){
					continue nextFee;
				} else {
					updatedTpaFees.add(fee);
				}
			}
			form.setUpdatedTpaFeesStandard(updatedTpaFees);
			
			// add customized TPA fees
			for(FeeUIHolder fee : form.getTpaFeesCustomized()) {
				fee.setFeeCode(StringUtils.EMPTY);
			} 
			form.setUpdatedTpaFeesCustomized(FeeUIHolder.removeZeroValueFeeObjects(form.getTpaFeesCustomized()));
		}

		if(!form.getUpdatedTpaFeesStandard().isEmpty()) {
			FeeUIHolder.assignFeeCodes(new TreeSet<String>(), form.getUpdatedTpaFeesStandard(), 
					((TPAContractFeeScheduleForm)form.getClonedForm()).getTpaFeesStandard());
		}
		
		if(!form.getUpdatedTpaFeesCustomized().isEmpty()) {
			FeeUIHolder.assignFeeCodes(getAllTpaCustomFeeCodes(), form.getUpdatedTpaFeesCustomized(), 
					((TPAContractFeeScheduleForm)form.getClonedForm()).getTpaFeesCustomized());
		}
		
		for(FeeUIHolder fee : form.getUpdatedTpaFeesStandard()) {
			ContractCustomizedFeeVO feeVO = fee.getContractCustomizedFeeVO();
			updateFeeInfo(feeVO, form, userProfile, FeeCategoryCode.TPA_PRE_DEFINED);
			if(fee.isDisabled()) {
				if(feeVO.getFeeAmount() != null) {
					feeVO.setFeeAmount(BigDecimal.ZERO);
				} else if (feeVO.getFeePercentage() != null) {
					feeVO.setFeePercentage(BigDecimal.ZERO);
				}
				// Code added for Defect 6664.
				feeVO.setNotes(StringUtils.EMPTY);
			}
			changedFeeItems.add(feeVO);
		}
		for(FeeUIHolder fee : form.getUpdatedTpaFeesCustomized()) {
			ContractCustomizedFeeVO feeVO = fee.getContractCustomizedFeeVO();
			updateFeeInfo(feeVO, form, userProfile, FeeCategoryCode.TPA_NON_STANDARD);
			changedFeeItems.add(feeVO);
		}
		
		feeServiceDelegate.save404a5NoticeInfoData(contractId, null, null, changedFeeItems, null, null);
		
		fireIpiHostingCustomScheduleChangeEvent(
				TPAContractFeeScheduleConfirmController.class, "doSubmit", contractId,
				userProfile.getPrincipal().getProfileId(), false);
		
		form.setPageMode(PageMode.Confirm);
		
		return forwards.get(Constants.VIEW_TPA_CUSTOMIZE_CONTRACT_PAGE);
	}
	
	private void updateFeeInfo(ContractCustomizedFeeVO fee, 
			TPAContractFeeScheduleForm form, UserProfile userProfile,  FeeCategoryCode category) {
		fee.setContractId(Integer.valueOf(form.getSelectedContract()));
		fee.setTpaId(form.getTpaId());
		fee.setFeeCategoryCode(category.getCode());
		fee.setCreatedUserId(String.valueOf(userProfile.getPrincipal().getProfileId()));
	}
	
	private TreeSet<String> getAllTpaCustomFeeCodes() {
		TreeSet<String> feeCodes = new TreeSet<String>();
		for(TpaCustomFeeCodes code : TpaCustomFeeCodes.values()) {
			feeCodes.add(code.getCode());
		}
		return feeCodes;
	}
}