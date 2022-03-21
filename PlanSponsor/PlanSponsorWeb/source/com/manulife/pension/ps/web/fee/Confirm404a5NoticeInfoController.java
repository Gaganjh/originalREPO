package com.manulife.pension.ps.web.fee;

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
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.tpafeeschedule.BaseFeeScheduleForm.PageMode;
import com.manulife.pension.ps.web.tpafeeschedule.BaseFeeScheduleController;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO.FeeCategoryCode;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO.NonTpaCustomFeeCodes;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO.TpaCustomFeeCodes;
import com.manulife.pension.service.fee.valueobject.DesignatedInvestmentManager;
import com.manulife.pension.service.fee.valueobject.PBAFeeVO;
import com.manulife.pension.service.fee.valueobject.PBAFeeVO.PbaCustomFeeCodes;
import com.manulife.pension.service.fee.valueobject.PBARestriction;
import com.manulife.pension.service.fee.valueobject.PersonalBrokerageAccount;
import com.manulife.pension.service.fund.valueobject.Access404a5;
import com.manulife.pension.service.fund.valueobject.Access404a5.Facility;
import com.manulife.pension.service.security.role.permission.PermissionType;


@Controller
@RequestMapping( value = "/confirm404a5NoticeInfo/")
@SessionAttributes({"noticeInfo404a5Form"})

public class Confirm404a5NoticeInfoController extends BaseFeeScheduleController  {
	@ModelAttribute("noticeInfo404a5Form") 
	public NoticeInfo404a5Form populateForm() 
	{
		return new NoticeInfo404a5Form();
		}

	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/fee/404a5NoticePreview.jsp");
		forwards.put("confirm404a5NoticeInfo","/fee/404a5NoticePreview.jsp");
		forwards.put("edit404a5NoticeInfo","redirect:/do/edit404a5NoticeInfo/");
		forwards.put( "view404a5NoticeInfo","redirect:/do/view404a5NoticeInfo/");}
	
	/**
	 * @see BaseAutoController#preExecute()
	 */
	protected String preExecute(@Valid @ModelAttribute("noticeInfo404a5Form") NoticeInfo404a5Form form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		UserProfile userProfile = getUserProfile(request);
		Access404a5 contractAccess = userProfile.getAccess404a5();

		// if internal user or page not accessible go to home page
		if (!userProfile.isInternalUser() 
				|| !userProfile.getRole().hasPermission(PermissionType.FEE_ACCESS_404A5)
				|| contractAccess.getAccess(Facility._404a5_NOTICE_INFO) == null) {
			return Constants.HOME_URL;
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> preExecute");
		}
		return super.preExecute( form, request, response);
	}

	
	@RequestMapping( method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("noticeInfo404a5Form") NoticeInfo404a5Form form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		int contractId = currentContract.getContractNumber();
		
		// if lock already exists, go to edit mode, else go to Home Page.
		final boolean lockObtained = isLockObtained(contractId, request, LockHelper.EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE);
		if (!lockObtained) {
			
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		
		boolean isChangesMade = true;
		if (form.getUpdatedNonTpaFees().isEmpty()
				&& form.getUpdatedTpaFeesStandard().isEmpty()
				&& form.getUpdatedTpaFeesCustomized().isEmpty()
				&& (form.getDesignatedInvestmentManagerUi() == null || form
						.getDesignatedInvestmentManagerUi().getChangedItems()
						.isEmpty())
				&& (form.getPersonalBrokerageAccountUi() == null || form
						.getPersonalBrokerageAccountUi().getChangedItems()
						.isEmpty())	
				&& form.getUpdatedPbaRestrictionList().isEmpty()
				&& form.getUpdatedStandardPBAFees().isEmpty()
				&& form.getUpdatedCustomPBAFees().isEmpty()) {
			isChangesMade = false;
		}
		
		// if no changes to show in this page or not directing from edit page then go to edit page.
		if (!isChangesMade || !PageMode.Edit.equals(form.getPageMode())) {
			//return  Constants.EDIT_404a5_NOTICE_INFO_PAGE;
			return  forwards.get(Constants.EDIT_404a5_NOTICE_INFO_PAGE);
		}
		
		if(isChangesMade){
			form.setPageMode(PageMode.Confirm);
		}

		return forwards.get(Constants.CONFIRM_404a5_NOTICE_INFO_PAGE);
		//return Constants.CONFIRM_404a5_NOTICE_INFO_PAGE;
	}
	
	

	@RequestMapping(params = { "action=back"}, method =  {RequestMethod.POST}) 
	public String doBack(@Valid @ModelAttribute("noticeInfo404a5Form") NoticeInfo404a5Form form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		form.setPageMode(PageMode.Confirm);
		
		return forwards.get(Constants.EDIT_404a5_NOTICE_INFO_PAGE);
		//return Constants.EDIT_404a5_NOTICE_INFO_PAGE;
	}
	@RequestMapping( params = { "action=confirm" },method =  {RequestMethod.POST}) 
	public String doSave(@Valid @ModelAttribute("noticeInfo404a5Form") NoticeInfo404a5Form form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		int contractId = currentContract.getContractNumber();
		
		FeeServiceDelegate feeServiceDelegate = FeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
		
		// if customizing for first time, copy over other standard schedule values which are greater than 0
		if(form.getTpaCustomizedScheduleFees().isEmpty()
				&& (!form.getUpdatedTpaFeesStandard().isEmpty()
						|| !form.getUpdatedTpaFeesCustomized().isEmpty())) {
			
			
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
			
			for(FeeUIHolder fee : form.getTpaFeesCustomized()) {
				fee.setFeeCode(StringUtils.EMPTY);
			} 
			form.setUpdatedTpaFeesCustomized(FeeUIHolder.removeZeroValueFeeObjects(form.getTpaFeesCustomized()));	
			
		}
		
		List<PBAFeeUIHolder> updatedPbaStandFees = new ArrayList<PBAFeeUIHolder>();
		for(PBAFeeUIHolder fee: form.getUpdatedStandardPBAFees()){
			updatedPbaStandFees.add(fee);
		}
		nextFee : for(PBAFeeUIHolder fee : form.getStandardPBAFees()){
			if(updatedPbaStandFees.contains(fee)){
				continue nextFee;
			}
			else if(!fee.isExisting()){
				updatedPbaStandFees.add(fee);					
			}
		}
		form.setUpdatedStandardPBAFees(updatedPbaStandFees);		
		
		if(!form.getUpdatedTpaFeesStandard().isEmpty()) {
			FeeUIHolder.assignFeeCodes(new TreeSet<String>(), form.getUpdatedTpaFeesStandard(), 
					((NoticeInfo404a5Form)form.getClonedForm()).getTpaFeesStandard());
		}
		
		if(!form.getUpdatedTpaFeesCustomized().isEmpty()) {
			FeeUIHolder.assignFeeCodes(getAllTpaCustomFeeCodes(), form.getUpdatedTpaFeesCustomized(), 
					((NoticeInfo404a5Form)form.getClonedForm()).getTpaFeesCustomized());
		}
		
		if(!form.getUpdatedNonTpaFees().isEmpty()) {
			FeeUIHolder.assignFeeCodes(getAllNonTpaFeeCodes(), form.getUpdatedNonTpaFees(),
					((NoticeInfo404a5Form)form.getClonedForm()).getNonTpaFees());
		}
		
		if(!form.getUpdatedCustomPBAFees().isEmpty()){
			PBAFeeUIHolder.assignFeeCodes(getAllPbaCustomFeeCodes(), form.getUpdatedCustomPBAFees(), ((NoticeInfo404a5Form)form.getClonedForm()).getCustomPBAFees());		
		}
		
		List<ContractCustomizedFeeVO> changedFeeItems = new ArrayList<ContractCustomizedFeeVO>();
		
		for(FeeUIHolder fee : form.getUpdatedNonTpaFees()) {
			ContractCustomizedFeeVO feeVO = fee.getContractCustomizedFeeVO();
			updateFeeInfo(feeVO, form, userProfile, contractId, FeeCategoryCode.NON_TPA);
			changedFeeItems.add(feeVO);
		}
		for(FeeUIHolder fee : form.getUpdatedTpaFeesStandard()) {
			ContractCustomizedFeeVO feeVO = fee.getContractCustomizedFeeVO();
			updateFeeInfo(feeVO, form, userProfile, contractId, FeeCategoryCode.TPA_PRE_DEFINED);
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
			updateFeeInfo(feeVO, form, userProfile, contractId, FeeCategoryCode.TPA_NON_STANDARD);
			changedFeeItems.add(feeVO);
		}
		
		List<PBAFeeVO> changedPBAFees = new ArrayList<PBAFeeVO>();
		for(PBAFeeUIHolder fee: form.getUpdatedStandardPBAFees()){
			PBAFeeVO vo = fee.getPbaFeeVO();
			updatePBAFeeInfo(vo, userProfile, contractId);
			changedPBAFees.add(vo);
		}		
		
		for(PBAFeeUIHolder fee: form.getUpdatedCustomPBAFees()){
			PBAFeeVO vo = fee.getPbaFeeVO();
			updatePBAFeeInfo(vo, userProfile, contractId);
			changedPBAFees.add(vo);
		}
		
		DesignatedInvestmentManager designatedInvestmentManager = null;
		if(!form.getDesignatedInvestmentManagerUi().getChangedItems().isEmpty()) {
			designatedInvestmentManager = form.getDesignatedInvestmentManagerUi().getEditedDesignatedInvestmentManager();
			if(designatedInvestmentManager.isEmpty()) {
				designatedInvestmentManager = form.getDesignatedInvestmentManagerUi().getStoredDesignatedInvestmentManager();
				designatedInvestmentManager.setDeleted(true);
			}
			designatedInvestmentManager.setContractid(contractId);
			designatedInvestmentManager.setCreatedUserId(String.valueOf(userProfile.getPrincipal().getProfileId()));
		}
		
		PersonalBrokerageAccount personalBrokerageAccount = null;
		if(!form.getPersonalBrokerageAccountUi().getChangedItems().isEmpty()) {
			personalBrokerageAccount = form.getPersonalBrokerageAccountUi().getEditedPersonalBrokerageAccount();
			if(personalBrokerageAccount.isEmpty()) {
				personalBrokerageAccount = form.getPersonalBrokerageAccountUi().getStoredPersonalBrokerageAccount();
				personalBrokerageAccount.setDeleted(true);
			}
			personalBrokerageAccount.setContractid(contractId);
			personalBrokerageAccount.setCreatedUserId(String.valueOf(userProfile.getPrincipal().getProfileId()));
		}
		
		List<PBARestriction> changedRestrictions = new ArrayList<PBARestriction>();
		for(PBARestrictionUi restrictionUi: form.getUpdatedPbaRestrictionList()){
			PBARestriction res = restrictionUi.getPbaRestriction();
			res.setCreatedUserId(String.valueOf(userProfile.getPrincipal().getProfileId()));
			changedRestrictions.add(res);
		}
		
		feeServiceDelegate.save404a5NoticeInfoData(contractId, designatedInvestmentManager, personalBrokerageAccount, changedFeeItems, changedPBAFees, changedRestrictions);
		
		form.setPageMode(PageMode.Confirm);		
		
		// trigger IpiHostingCustomScheduleChangeEvent
		fireIpiHostingCustomScheduleChangeEvent(
				Confirm404a5NoticeInfoController.class, "doSave", contractId,
				userProfile.getPrincipal().getProfileId(), false);
		
		//return mapping.findForward(Constants.VIEW_404a5_NOTICE_INFO_PAGE);
		return forwards.get(Constants.VIEW_404a5_NOTICE_INFO_PAGE);
		//return Constants.VIEW_404a5_NOTICE_INFO_PAGE;
	}
	
	private void updateFeeInfo(ContractCustomizedFeeVO fee, 
			NoticeInfo404a5Form form, UserProfile userProfile, int contractId,  FeeCategoryCode category) {
		fee.setContractId(contractId);
		// non TPA fees are at contract level
		if(category.equals(FeeCategoryCode.NON_TPA)) {
			fee.setTpaId(0);
		} else {
			fee.setTpaId(form.getTpaId());
		}
		fee.setFeeCategoryCode(category.getCode());
		fee.setCreatedUserId(String.valueOf(userProfile.getPrincipal().getProfileId()));
	}

	
	private TreeSet<String> getAllNonTpaFeeCodes() {
		TreeSet<String> feeCodes = new TreeSet<String>();
		for(NonTpaCustomFeeCodes code : NonTpaCustomFeeCodes.values()) {
			feeCodes.add(code.getCode());
		}
		return feeCodes;
	}
	
	private TreeSet<String> getAllTpaCustomFeeCodes() {
		TreeSet<String> feeCodes = new TreeSet<String>();
		for(TpaCustomFeeCodes code : TpaCustomFeeCodes.values()) {
			feeCodes.add(code.getCode());
		}
		return feeCodes;
	}
	
	private void updatePBAFeeInfo(PBAFeeVO fee, UserProfile userProfile, int contractId) {
		fee.setContractId(contractId);
		fee.setCreatedUserId(String.valueOf(userProfile.getPrincipal().getProfileId()));
	}
	
	private TreeSet<String> getAllPbaCustomFeeCodes() {
		TreeSet<String> feeCodes = new TreeSet<String>();
		for(PbaCustomFeeCodes code : PbaCustomFeeCodes.values()) {
			feeCodes.add(code.getCode());
		}
		return feeCodes;
	}

	
}
