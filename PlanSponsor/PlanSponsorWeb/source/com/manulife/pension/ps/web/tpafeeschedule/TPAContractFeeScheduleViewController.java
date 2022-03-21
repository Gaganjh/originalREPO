package com.manulife.pension.ps.web.tpafeeschedule;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.ps.service.report.feeSchedule.TpaFeeScheduleDetails;
import com.manulife.pension.ps.web.Constants;

import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.fee.FeeUIHolder;
import com.manulife.pension.ps.web.tpafeeschedule.BaseFeeScheduleForm.PageMode;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.service.fee.util.Constants.FeeScheduleType;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO.FeeCategoryCode;
import com.manulife.pension.service.fee.valueobject.TPAFees;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.util.Pair;
/**
 * 
 * Action Class For View TPA Customize Contract Fee Page
 * 
 * @author Siby Thomas
 *
 */
@Controller
@RequestMapping( value = "/viewTpaCustomizedContractFee/")
@SessionAttributes({"tpaCustomizeContractForm"})

public class TPAContractFeeScheduleViewController extends BaseFeeScheduleController  {
	
	@ModelAttribute("tpaCustomizeContractForm") 
	public TPAContractFeeScheduleForm populateForm() 
	{
		return new TPAContractFeeScheduleForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/tpa/viewTpaContractFeeSchedule.jsp"); 
		forwards.put("viewTpaCustomizedContractFee","/tpa/viewTpaContractFeeSchedule.jsp"); 
		forwards.put("editTpaCustomizedContractFee","redirect:/do/editTpaCustomizedContractFee/"); 
		forwards.put("viewTpaCustomizedContractFeeChangeHistory","redirect:/do/viewTpaCustomizedContractFeeChangeHistory/");
		forwards.put("goToCustomizeContractSearchPage","redirect:/do/tpafee/contractSearch/");
		}

	protected String preExecute(TPAContractFeeScheduleForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

	

		String forward = super.preExecute( actionForm, request,
				response);
		if (forward != null) {
			return forward;
		}

		// if a contractID existing in request it means its navigated from
		// MessageCenter.
		if (request.getParameter(Constants.SELECTED_CUSTOMIZE_CONTRACT) != null) {
			String selectedContractNumber = request
					.getParameter(Constants.SELECTED_CUSTOMIZE_CONTRACT);

			TPAFirmInfo tpaFirm = TPAServiceDelegate.getInstance()
					.getFirmInfoByContractId(
							Integer.parseInt(selectedContractNumber));

			Map<Integer, String> accessibleTpaFirms = getAllTpaFirmsForTpaLoginUser(request);

			// validate the contract's tpaId whether it is belongs to the login User, if
			// the user bookmark the url and changes the contractId.
			if (tpaFirm != null && accessibleTpaFirms.containsKey(tpaFirm.getId())) {
				request.getSession().setAttribute(
						Constants.SELECTED_CUSTOMIZE_CONTRACT,
						selectedContractNumber);
			} else {
				forward = Constants.HOMEPAGE_FINDER_FORWARD;
			}
		}

		return forward;
	}
	
	/**
	 * @see BaseAutoController#doDefault()
	 */

	@RequestMapping(  method =  {RequestMethod.GET}) 
	protected String doDefualt(@Valid @ModelAttribute("tpaCustomizeContractForm") TPAContractFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return  StringUtils.contains(forward, "/")?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		form.clear( request);
		
		if(request.getParameter(Constants.SELECTED_CUSTOMIZE_CONTRACT)!= null){
			String selectedContractNumber = request.getParameter(Constants.SELECTED_CUSTOMIZE_CONTRACT);
			String contractName = ContractServiceDelegate.getInstance().getContractName(Integer.valueOf(selectedContractNumber));
			form.setSelectedContract(selectedContractNumber);
			form.setSelectedContractName(contractName);
			request.getSession().removeAttribute(Constants.SELECTED_CUSTOMIZE_CONTRACT);
		}
		
		
		
		
		if(request.getSession().getAttribute(Constants.SELECTED_CUSTOMIZE_CONTRACT) != null) {
			String selectedContractNumber = (String) request.getSession().getAttribute(Constants.SELECTED_CUSTOMIZE_CONTRACT);
			String contractName = ContractServiceDelegate.getInstance().getContractName(Integer.valueOf(selectedContractNumber));
			form.setSelectedContract(selectedContractNumber);
			form.setSelectedContractName(contractName);
			request.getSession().removeAttribute(Constants.SELECTED_CUSTOMIZE_CONTRACT);
		}
		
		if(StringUtils.isEmpty(form.getSelectedContract())) {
			 return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		UserProfile userProfile = getUserProfile(request);
	    int contractId = Integer.valueOf(form.getSelectedContract());
		
		FeeServiceDelegate feeServiceDelegate = FeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
		
		TPAFirmInfo tpaFirm = TPAServiceDelegate.getInstance().getFirmInfoByContractId(contractId);
		int tpaId = tpaFirm != null ? tpaFirm.getId() : 0;
		form.setTpaId(tpaId);
		
		TPAFees tpaFees = feeServiceDelegate.getTPAFees(contractId, tpaId);
		
		List<ContractCustomizedFeeVO> tpaStandardScheduleFees = new ArrayList<ContractCustomizedFeeVO>();
		for(ContractCustomizedFeeVO vo : tpaFees.getStandardTpaFees()) {
			if(vo.getFeeValueAmount() != null 
					&& vo.getFeeValueAmount().doubleValue() > 0.0d) {
				tpaStandardScheduleFees.add(vo);
			}
		}
		form.setTpaStandardScheduleFees(tpaStandardScheduleFees);
		
		List<ContractCustomizedFeeVO> tpaCustomizedScheduleFees = tpaFees.getCustomizedTpaFees();
		form.setTpaCustomizedScheduleFees(tpaCustomizedScheduleFees);
		
		// get TPA fee information from EJB layer
		List<ContractCustomizedFeeVO> tpaFeeList = tpaFees.getTpaFees();
		
		
		// convert to fee UI format
		List<FeeUIHolder> tpaStandardFees = new ArrayList<FeeUIHolder>();
		List<FeeUIHolder> tpaCustomizedFees = new ArrayList<FeeUIHolder>();
		
		for(ContractCustomizedFeeVO contractCustomizedFeeVO : tpaFeeList) {
			if(contractCustomizedFeeVO.getFeeCategoryCode().equals(FeeCategoryCode.TPA_PRE_DEFINED.getCode())) {
				tpaStandardFees.add(new FeeUIHolder(contractCustomizedFeeVO));
			} else {
				tpaCustomizedFees.add(new FeeUIHolder(contractCustomizedFeeVO));
			}
		}
		//load plan provisions to the respective features.
		loadPlanProvisionsToTpaStandardFees(contractId, tpaStandardFees);
		// store TPA standard, customized and non TPA fees.
		form.setTpaFeesStandard(tpaStandardFees);
		form.setTpaFeesCustomized(tpaCustomizedFees);
		
		
		// store TPA fee type { none, standard, customized }
		form.setTpaFeeType(tpaFees.getFeeType().name());
		
		// store last updated details
		setLastUpdateDetails(userProfile, form, FeeScheduleType.CustomizedFeeSchedule);
		
		
		// release lock in case coming from edit or confirm page buttons
        if(form.getPageMode().equals(PageMode.Edit) 
        		|| form.getPageMode().equals(PageMode.Confirm)) {
        	 releaseLock(contractId, request,  LockHelper.EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE);
        }
		
        form.setDirty(Boolean.FALSE.toString());
        form.setPageMode(PageMode.Unknown);
        
		// Checking whether the contract is having 404a5 Fee Access Permission
		// to restrict on Edit functionality
		boolean having404a5FeeAccessPermission = SecurityServiceDelegate
				.getInstance().isFirmHolding404a5FeeAccessPermission(
						Integer.valueOf(form.getSelectedContract()));
		form.setFee404a5AccessPermission(having404a5FeeAccessPermission);
        
		return forwards.get(Constants.VIEW_TPA_CUSTOMIZE_CONTRACT_PAGE);
	}
	
	/**
	 * 
	 * handle edit request from page
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * 
	 * @return
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */

	@RequestMapping(params={"action=edit"},  method =  {RequestMethod.POST,RequestMethod.GET}) 
	protected String doEdit(@Valid @ModelAttribute("tpaCustomizeContractForm") TPAContractFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return  StringUtils.contains(forward, "/")?forward:forwards.get(forward);
		}
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		int contractId = Integer.valueOf(form.getSelectedContract());

		// try to obtain a lock for edit page, if lock not obtained go to view page
		final boolean lockObtained = obtainLock(contractId, request,LockHelper.EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE);
		if (!lockObtained) {
			handleObtainLockFailure(contractId, request, LockHelper.EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE);
			return forwards.get(Constants.VIEW_TPA_CUSTOMIZE_CONTRACT_PAGE);
		}
		
		form.setPageMode(PageMode.View);		
		return forwards.get(Constants.EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE);
	}
	
	/**
	 * 
	 * action method to forward to  customize contract  change history page
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * 
	 * @return ActionForward
	 * 
	 * @throws IOException
	 * @throws SystemException
	 */
	@RequestMapping( params={"action=goToHistory"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doGoToHistory (@Valid @ModelAttribute("tpaCustomizeContractForm") TPAContractFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return  StringUtils.contains(forward, "/")?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		String contractNumber = form.getSelectedContract();
		request.getSession().setAttribute(Constants.SELECTED_CUSTOMIZE_CONTRACT, contractNumber);
		return forwards.get(Constants.VIEW_TPA_CUSTOMIZE_CONTRACT_CHANGE_HISTORY_PAGE);
	}
	
	
	/**
	 * 
	 * action method to forward to  customize contract search page
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * 
	 * @return ActionForward
	 * 
	 * @throws IOException
	 * @throws SystemException
	 */
	
	@RequestMapping( params={"action=back"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doBack (@Valid @ModelAttribute("tpaCustomizeContractForm") TPAContractFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return  StringUtils.contains(forward, "/")?forward:forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");
	       }
		}
		
		request.getSession().setAttribute(Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID, form.getTpaId());
		return forwards.get("goToCustomizeContractSearchPage");
	}
	
	@Override
	protected void setLastUpdateDetails(UserProfile loggedInUser,
			BaseFeeScheduleForm actionForm, FeeScheduleType feeScheduleType)
			throws SystemException {
		TPAContractFeeScheduleForm form = (TPAContractFeeScheduleForm)actionForm;
		Pair<String, Timestamp> lastUpdatedUserDetails = TpaFeeScheduleDetails.getLastUpdateTpaCustomScheduleDetails(
				Integer.valueOf(form.getSelectedContract()), form.getTpaId());
		form.setLastUpdateDate(lastUpdatedUserDetails.getSecond());
		form.setLastUpdatedUserId(lastUpdatedUserDetails.getFirst());
	}
}
