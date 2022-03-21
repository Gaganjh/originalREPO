package com.manulife.pension.ps.web.fee;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.ContractFeeScheduleChangeHistoryReportData;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.ContractFeeScheduleChangeHistoryReportData.FilterSections;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.FeeScheduleChangeItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.tpafeeschedule.BaseFeeScheduleController;
import com.manulife.pension.ps.web.tpafeeschedule.BaseFeeScheduleForm;
import com.manulife.pension.ps.web.tpafeeschedule.BaseFeeScheduleForm.PageMode;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractValidationDetail;
import com.manulife.pension.service.fee.util.Constants.FeeScheduleType;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO.FeeCategoryCode;
import com.manulife.pension.service.fee.valueobject.DesignatedInvestmentManager;
import com.manulife.pension.service.fee.valueobject.FeeScheduleHistoryDetails;
import com.manulife.pension.service.fee.valueobject.PBAFeeVO;
import com.manulife.pension.service.fee.valueobject.PBAFees;
import com.manulife.pension.service.fee.valueobject.PBARestriction;
import com.manulife.pension.service.fee.valueobject.PersonalBrokerageAccount;
import com.manulife.pension.service.fee.valueobject.TPAFees;
import com.manulife.pension.service.fund.valueobject.Access404a5;
import com.manulife.pension.service.fund.valueobject.Access404a5.Facility;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.ArrayUtility;

/**
 * 
 * Action class to handle view 404a5 info page requests
 * 
 * @author Siby Thomas
 *
 */
@Controller
@RequestMapping( value = "/view404a5NoticeInfo/")
@SessionAttributes({"noticeInfo404a5Form"})

public class View404a5NoticeInfoController extends BaseFeeScheduleController  {
	@ModelAttribute("noticeInfo404a5Form") 
	public NoticeInfo404a5Form populateForm() 
	{
		return new NoticeInfo404a5Form();
		}
	public static HashMap<String,String> forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/fee/404a5NoticeView.jsp");
		forwards.put("view404a5NoticeInfo","/fee/404a5NoticeView.jsp");
		forwards.put( "edit404a5NoticeInfo","redirect:/do/edit404a5NoticeInfo/");
		}
	
	/**
	 * @see BaseAutoController#preExecute()
	 */
	//@RequestMapping(params={"action=edit","task=edit"}   , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String preExecute (@Valid @ModelAttribute("noticeInfo404a5Form") NoticeInfo404a5Form form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	

		UserProfile userProfile = getUserProfile(request);
		Access404a5 contractAccess = userProfile.getAccess404a5();

		if (contractAccess.getAccess(Facility._404a5_NOTICE_INFO) == null) {
			return Constants.HOME_URL;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> preExecute");
		}
		return super.preExecute( form, request, response);
	}
	
	/**
	 * @see BaseAutoController#doDefault()
	 */
	@RequestMapping(  method =  {RequestMethod.GET}) 
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
	return  forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		
		form.clear( request);
		
		UserProfile userProfile = getUserProfile(request);
	    Contract currentContract = userProfile.getCurrentContract();
	    int contractId = currentContract.getContractNumber();
		
		FeeServiceDelegate feeServiceDelegate = FeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
		
		TPAFirmInfo tpaFirm = TPAServiceDelegate.getInstance().getFirmInfoByContractId(contractId);
		form.setTpaId(tpaFirm != null ? tpaFirm.getId() : 0);
		form.setSelectedContract(String.valueOf(contractId));
		
		TPAFees tpaFees = feeServiceDelegate.getTPAFees(contractId, form.getTpaId());
		
		// get TPA fee information from EJB layer
		List<ContractCustomizedFeeVO> tpaFeeList = tpaFees.getTpaFees();
		
		
		// convert to fee UI format
		List<FeeUIHolder> tpaStandardFees = new ArrayList<FeeUIHolder>();
		List<FeeUIHolder> tpaCustomizedFees = new ArrayList<FeeUIHolder>();
		List<FeeUIHolder> nonTpaStandardFees = new ArrayList<FeeUIHolder>();
		
		for(ContractCustomizedFeeVO contractCustomizedFeeVO : tpaFeeList) {
			if(contractCustomizedFeeVO.getFeeCategoryCode().equals(FeeCategoryCode.TPA_PRE_DEFINED.getCode())) {
				tpaStandardFees.add(new FeeUIHolder(contractCustomizedFeeVO));
			} else {
				tpaCustomizedFees.add(new FeeUIHolder(contractCustomizedFeeVO));
			}
		}
		
		List<ContractCustomizedFeeVO> nonTpaFeeList = feeServiceDelegate.getNonTPAIndividualFees(contractId);
		for(ContractCustomizedFeeVO contractCustomizedFeeVO : nonTpaFeeList) {
			nonTpaStandardFees.add(new FeeUIHolder(contractCustomizedFeeVO));
		}
		
		
		// load plan provisions to the respective features.
		loadPlanProvisionsToTpaStandardFees(contractId, tpaStandardFees);
		
		// store TPA standard, customized and non TPA fees.
		form.setTpaFeesStandard(tpaStandardFees);
		form.setTpaFeesCustomized(tpaCustomizedFees);
		form.setNonTpaFees(nonTpaStandardFees);		
		
		// store dim details
		DesignatedInvestmentManager designatedInvestmentManager = feeServiceDelegate.getDesginatedInvestmentManagerDetails(contractId);
		form.setDesignatedInvestmentManagerUi(new DesignatedInvestmentManagerUi(designatedInvestmentManager, designatedInvestmentManager));
		
		List<ContractCustomizedFeeVO> tpaCustomizedScheduleFees = tpaFees.getCustomizedTpaFees();
		form.setTpaCustomizedScheduleFees(tpaCustomizedScheduleFees);
		
		//store pba details
		PersonalBrokerageAccount pba = feeServiceDelegate.getPersonalBrokerageAccountDetails(contractId);
		form.setPersonalBrokerageAccountUi(new PersonalBrokerageAccountUi(pba, pba));
				
		//get the PBA restriction frm EJB layer
		List<PBARestriction> pbaRestrictionList= feeServiceDelegate.getPBARestrictionDetails(contractId);
		List<PBARestrictionUi> pbaRestrictionUiList = new ArrayList<PBARestrictionUi>();
		for(PBARestriction restrictionVO: pbaRestrictionList){
			pbaRestrictionUiList.add(new PBARestrictionUi(restrictionVO));
		}
		form.setPbaRestrictionList(pbaRestrictionUiList);
		
		//get PBA fee information from EJB layer
		PBAFees pbaFees = feeServiceDelegate.getPBAFees(contractId);
		List<PBAFeeVO> pbaFeeList = new ArrayList<PBAFeeVO>();
		pbaFeeList.addAll(pbaFees.getStandardPBAFees());
		pbaFeeList.addAll(pbaFees.getCustomizedPBAFees());		
		
		// convert to PBA fee UI format
		List<PBAFeeUIHolder> pbaStandardFees = new ArrayList<PBAFeeUIHolder>();
		List<PBAFeeUIHolder> pbaCustomFees = new ArrayList<PBAFeeUIHolder>();
		
		for(PBAFeeVO pBAFeeVO : pbaFeeList) {
			if(pBAFeeVO.getPbaFeeTypeCode()!=null && StringUtils.isNotEmpty(pBAFeeVO.getPbaFeeTypeCode())){
				if(Arrays.asList(PBAFeeVO.pbaStandardFeeArray).contains(pBAFeeVO.getPbaFeeTypeCode())) {
					pbaStandardFees.add(new PBAFeeUIHolder(pBAFeeVO));
				} else {
					pbaCustomFees.add(new PBAFeeUIHolder(pBAFeeVO));
				}
			}			
		}
		
		Collections.sort(pbaStandardFees, new Comparator<PBAFeeUIHolder>() {
			@Override
			public int compare(PBAFeeUIHolder object1, PBAFeeUIHolder object2) {
				return object1.getSortSequenceNo().compareTo(object2.getSortSequenceNo());
			}
		});
		
		Collections.sort(pbaCustomFees, new Comparator<PBAFeeUIHolder>() {
			@Override
			public int compare(PBAFeeUIHolder object1, PBAFeeUIHolder object2) {
				return object1.getSortSequenceNo().compareTo(object2.getSortSequenceNo());
			}
		});		
				
		//store PBA fee(Standard and Custom) details
		form.setStandardPBAFees(pbaStandardFees);
		form.setCustomPBAFees(pbaCustomFees);
				
		// store TPA fee type { none, standard, customized }
		form.setTpaFeeType(tpaFees.getFeeType().name());
		
		// store last updated details
		 if(getUserProfile(request).getRole().isTPA()) {
			 setLastUpdateDetails(userProfile, form, FeeScheduleType.Notice404a5InfoForTpaUser);
		 } else {
			 setLastUpdateDetails(userProfile, form, FeeScheduleType.Notice404a5Info);
		 }
		
		
		// check if TPA is restricted or not
		boolean having404a5FeeAccessPermission = SecurityServiceDelegate.getInstance().isFirmHolding404a5FeeAccessPermission(
						Integer.valueOf(contractId));
		form.setTpaRestricted(!having404a5FeeAccessPermission);
		
		// release lock in case coming from edit or confirm page buttons
        if(form.getPageMode().equals(PageMode.Edit) 
        		|| form.getPageMode().equals(PageMode.Confirm)) {
        	 releaseLock(contractId, request,  LockHelper.EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE);
        }
		
        form.setDirty(Boolean.FALSE.toString());
        form.setPageMode(PageMode.Unknown);
        
		return forwards.get(Constants.VIEW_404a5_NOTICE_INFO_PAGE);
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
	@RequestMapping(params={"action=edit"}   , method =  {RequestMethod.POST}) 
	public String doEdit (@Valid @ModelAttribute("noticeInfo404a5Form") NoticeInfo404a5Form form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		
		String forward = preExecute(form, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return  forwards.get("input");//if input forward not //available, provided default
	       }
		}
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		int contractId = currentContract.getContractNumber();

		// try to obtain a lock for edit page, if lock not obtained go to view page
		final boolean lockObtained = obtainLock(contractId, request,LockHelper.EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE);
		if (!lockObtained) {
			handleObtainLockFailure(contractId, request,LockHelper.EDIT_TPA_CUSTOMIZE_CONTRACT_PAGE);
			return forwards.get(Constants.VIEW_404a5_NOTICE_INFO_PAGE);
		}
		
		FeeServiceDelegate feeServiceDelegate = FeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
		//If the standard fees from form is empty, then get all standard PBA fees from Look up table
		ArrayList<PBAFeeUIHolder> pbaAllStandardFees = new ArrayList<PBAFeeUIHolder>();
		if(form.getStandardPBAFees().isEmpty()){			
			List<PBAFeeVO> pbaAllStandardFeesItems = feeServiceDelegate.getPbaStandardFeeListfromLookUp();			
			for(PBAFeeVO pBAFeeVO:pbaAllStandardFeesItems){
				PBAFeeUIHolder fee = new PBAFeeUIHolder(pBAFeeVO);
				pbaAllStandardFees.add(fee);
			}
			form.setStandardPBAFees(pbaAllStandardFees);
		}		
		
		Collections.sort(form.getStandardPBAFees(), new Comparator<PBAFeeUIHolder>() {
			@Override
			public int compare(PBAFeeUIHolder object1, PBAFeeUIHolder object2) {
				return object1.getSortSequenceNo().compareTo(object2.getSortSequenceNo());
			}
		});
		
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
		
		
		@SuppressWarnings("unchecked")
		Map<String, String> states = EnvironmentServiceDelegate.getInstance().getUSAGeographicalStatesOnly();
		form.setStates(new LinkedHashSet<String>(states.keySet()));
		
		form.setPageMode(PageMode.View);
		form.storeClonedForm();
		
		return forwards.get(Constants.EDIT_404a5_NOTICE_INFO_PAGE);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void setLastUpdateDetails(UserProfile loggedInUser,
			BaseFeeScheduleForm form, FeeScheduleType feeScheduleType)
			throws SystemException {
		try {
			ContractFeeScheduleChangeHistoryReportData reportData = (ContractFeeScheduleChangeHistoryReportData) 
					ReportServiceDelegate.getInstance().getReportData(getReportCriteria(loggedInUser, form));
			for(FeeScheduleChangeItem item : (List<FeeScheduleChangeItem>) reportData.getDetails()) {
				form.setLastUpdateDate(item.getChangedDate());
				form.setLastUpdatedUserId(item.getUserName());
				break;
			}
		} catch (ReportServiceException e) {
			throw new SystemException(e, "Exception thown in setLastUpdateDetails()");
		}
	}
	
	protected ReportCriteria getReportCriteria(UserProfile loggedInUser, BaseFeeScheduleForm reportform)
			throws SystemException {
		
		ReportCriteria criteria = new ReportCriteria(ContractFeeScheduleChangeHistoryReportData.REPORT_ID);
		
		NoticeInfo404a5Form form = (NoticeInfo404a5Form) reportform;
		
		criteria.setPageNumber(1);
		
		criteria.setPageSize(1);
		
		criteria.insertSort(ContractFeeScheduleChangeHistoryReportData.SORT_CHANGE_DATE, "DESC");
		
		// set sections to filter with
		List<FilterSections> sections = new ArrayList<FilterSections>();	
		sections.add(FilterSections.DimSection);
		sections.add(FilterSections.FeeSection);
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_SECTION, sections);
		
		// set fee types to filter with
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_GET_NON_TPA_FEE, true);
		
		// set contract id to filter with
		int contractNumber = loggedInUser.getCurrentContract().getContractNumber();
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_CONTRACT_ID, contractNumber);
		
		FeeServiceDelegate feeService = FeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID);
		
		 // if TPA user, get only history for that TPA
	    FeeScheduleHistoryDetails historyDetails = null;
	    if(loggedInUser.getRole().isTPA()) {
	    	historyDetails = feeService.getFeeScheduleChangeHistoryDetails(contractNumber,
		    		form.getTpaId(), FeeScheduleType.Notice404a5Info);
	    } else {
	    	historyDetails = feeService.getFeeScheduleChangeHistoryDetails(contractNumber,
		    		0, FeeScheduleType.Notice404a5Info);
	    }
		
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_TPA_FIRM_HISTORY, historyDetails.getFirmDetails());
		
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_TO_DATE, new Date());

		Calendar fromDate  = Calendar.getInstance();
		fromDate.set(Calendar.YEAR, (fromDate.get(Calendar.YEAR) - 2) );
		criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_FROM_DATE, fromDate.getTime());
		
		HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> planHistory = 
				ContractServiceDelegate.getInstance().getContractValidationDetailHistory(
						contractNumber, ArrayUtility.toUnsortedSet(ContractValidationDetail.values()));

	    criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_PLAN_PROVISION_HISTORY, planHistory);
		
	    Map<String, String> allUsers = new HashMap<String, String>();
	    if (loggedInUser.isExternalUser()) {
			for (Entry<Integer, String> user : historyDetails.getUpdatedUserDetails().entrySet()) {
				if (Integer.parseInt(Constants.SYSTEM_USER_PROFILE_ID) == user.getKey()) {
					allUsers.put(String.valueOf(user.getKey()), Constants.ADMINISTRATION);
				} else {
					UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserProfileByProfileId(new Long(user.getKey()));
					if (userInfo.getRole().isInternalUser()) {
						allUsers.put(String.valueOf(user.getKey()), Constants.JOHN_HANCOCK_REPRESENTATIVE);
					} else {
						 allUsers.put(String.valueOf(user.getKey()), user.getValue());
					}
				}
			}
		} else {
			for (Entry<Integer, String> user : historyDetails.getUpdatedUserDetails().entrySet()) {
				if (Integer.parseInt(Constants.SYSTEM_USER_PROFILE_ID) == user
						.getKey()) {
					allUsers.put(String.valueOf(user.getKey()),
							Constants.ADMINISTRATION);
				} else {
					allUsers.put(String.valueOf(user.getKey()), user.getValue());
				}
			}
	    }
	    
	    criteria.addFilter(ContractFeeScheduleChangeHistoryReportData.FILTER_USER_NAME, allUsers);
	    
		return criteria;
	}
	
}
