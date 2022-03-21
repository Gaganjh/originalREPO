package com.manulife.pension.ps.web.fee;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.tpafeeschedule.BaseFeeScheduleController;
import com.manulife.pension.ps.web.tpafeeschedule.BaseFeeScheduleForm.PageMode;
import com.manulife.pension.ps.web.tpafeeschedule.FeeScheduleDataValidator;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.fee.valueobject.DesignatedInvestmentManager;
import com.manulife.pension.service.fee.valueobject.PersonalBrokerageAccount;
import com.manulife.pension.service.fund.valueobject.Access404a5;
import com.manulife.pension.service.fund.valueobject.Access404a5.Facility;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * Action class to handle edit 404a5 info page requests
 * 
 * @author Siby Thomas
 *
 */
@Controller
@RequestMapping( value = "/edit404a5NoticeInfo/")
@SessionAttributes({"noticeInfo404a5Form"})

public class Edit404a5NoticeInfoController extends BaseFeeScheduleController {

	@ModelAttribute("noticeInfo404a5Form") 
	public NoticeInfo404a5Form populateForm() 
	{
		return new NoticeInfo404a5Form();
		}

	public static HashMap<String,String> forwards =new HashMap<String,String>();
	static{
		forwards.put("input","/fee/404a5NoticeEdit.jsp");
		forwards.put("edit404a5NoticeInfo","/fee/404a5NoticeEdit.jsp");
		forwards.put( "view404a5NoticeInfo","redirect:/do/view404a5NoticeInfo/" );
		forwards.put( "confirm404a5NoticeInfo","redirect:/do/confirm404a5NoticeInfo/");
		}

	/**
	 * @see BaseAutoController#preExecute()
	 */
	@Override
	protected String preExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {

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
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}else{
				return forwards.get("input");
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

		// load plan provisions to the respective features.
		loadPlanProvisionsToTpaStandardFees(contractId, form.getTpaFeesStandard());

		//add an empty row, if the custom fee count is less than 10
		form.setCustomPBAFees(PBAFeeUIHolder.addEmptyCustomFees(form.getCustomPBAFees()));

		// add an empty row, if fee count less than 5
		form.setNonTpaFees(FeeUIHolder.addEmptyCustomFees(form.getNonTpaFees()));
		form.setTpaFeesCustomized(FeeUIHolder.addEmptyCustomFees(form.getTpaFeesCustomized()));

		//add an empty row, if the restriction count is less than 10
		form.setPbaRestrictionList(PBARestrictionUi.addEmptyCustomRestrictions(form.getPbaRestrictionList()));

		return forwards.get (Constants.EDIT_404a5_NOTICE_INFO_PAGE);

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
	
	@RequestMapping(params = { "action=back" },   method =  {RequestMethod.POST}) 
	public String doBack (@Valid @ModelAttribute("noticeInfo404a5Form") NoticeInfo404a5Form form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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

		form.setPageMode(PageMode.Edit);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doBack");
		}

		return forwards.get(Constants.VIEW_404a5_NOTICE_INFO_PAGE);

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
	@RequestMapping(params = { "action=confirm"}, method =  {RequestMethod.POST}) 
	public String doConfirm (@Valid @ModelAttribute("noticeInfo404a5Form") NoticeInfo404a5Form form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException,CloneNotSupportedException {
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
		
		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		
		// remove empty fees added
		form.setNonTpaFees(removeBlankFeeObjects(form.getNonTpaFees())) ;
		form.setTpaFeesCustomized(removeBlankFeeObjects(form.getTpaFeesCustomized())) ;
		form.setCustomPBAFees(PBAFeeUIHolder.removeBlankFeeObjects(form.getCustomPBAFees())) ;
		form.setPbaRestrictionList(PBARestrictionUi.removeEmptyRestrictionObjects(form.getPbaRestrictionList()));
		
		errorMessages = FeeScheduleDataValidator.validate404a5NoticeInfoFees(form);

		// if errors present go back to same  page
		if (!(errorMessages.isEmpty())) {
			form.setNonTpaFees(FeeUIHolder.addEmptyCustomFees(form.getNonTpaFees()));
			form.setTpaFeesCustomized(FeeUIHolder.addEmptyCustomFees(form.getTpaFeesCustomized()));
			form.setCustomPBAFees(PBAFeeUIHolder.addEmptyCustomFees(form.getCustomPBAFees())) ;
			form.setPbaRestrictionList(PBARestrictionUi.addEmptyCustomRestrictions(form.getPbaRestrictionList()));
			setErrorsInSession(request, errorMessages);
			return forwards.get(Constants.EDIT_404a5_NOTICE_INFO_PAGE);
		} 
		
		// get edited values
		DesignatedInvestmentManager editedDesignatedInvestmentManager = form.getDesignatedInvestmentManagerUi().getEditedDesignatedInvestmentManager();
		editedDesignatedInvestmentManager.setContractid(contractId);
		
		PersonalBrokerageAccount editedPersonalBrokerageAccount = form.getPersonalBrokerageAccountUi().getEditedPersonalBrokerageAccount();
		editedPersonalBrokerageAccount.setContractid(contractId);
		
		List<PBAFeeUIHolder> editedPBAStandardFees = PBAFeeUIHolder.removeEmptyValueFeeObjects(form.getStandardPBAFees());
		List<PBAFeeUIHolder> editedPBACustomizedFees = PBAFeeUIHolder.removeEmptyValueFeeObjects(form.getCustomPBAFees());
		
		List<FeeUIHolder> editedNonTpaFees = FeeUIHolder.removeEmptyValueFeeObjects(form.getNonTpaFees());
		List<FeeUIHolder> editedTpaStandardFees = FeeUIHolder.removeEmptyValueFeeObjects(form.getTpaFeesStandard());
		List<FeeUIHolder> editedTpaCustomizedFees = FeeUIHolder.removeEmptyValueFeeObjects(form.getTpaFeesCustomized());
		
		List<PBARestrictionUi> editedPbaRestrictionList = PBARestrictionUi.removeEmptyRestrictionObjects(form.getPbaRestrictionList());
	

		// get actual values
		DesignatedInvestmentManager previousDesignatedInvestmentManager  = form.getDesignatedInvestmentManagerUi().getStoredDesignatedInvestmentManager();
		
		PersonalBrokerageAccount previousPersonalBrokerageAccount = form.getPersonalBrokerageAccountUi().getStoredPersonalBrokerageAccount();
		
		List<PBAFeeUIHolder> previousPBAStandardFees = ((NoticeInfo404a5Form) form
				.getClonedForm()).getStandardPBAFees();
		previousPBAStandardFees = PBAFeeUIHolder.removeEmptyValueFeeObjects(previousPBAStandardFees);
		List<PBAFeeUIHolder> previousPBACustomizedFees = ((NoticeInfo404a5Form) form
				.getClonedForm()).getCustomPBAFees();
		previousPBACustomizedFees = PBAFeeUIHolder.removeEmptyValueFeeObjects(previousPBACustomizedFees);
		
		List<FeeUIHolder> previousNonTpaFees  = ((NoticeInfo404a5Form) form
				.getClonedForm()).getNonTpaFees();
		previousNonTpaFees = FeeUIHolder.removeEmptyValueFeeObjects(previousNonTpaFees);
		List<FeeUIHolder> previousTpaStandardFees = ((NoticeInfo404a5Form) form
				.getClonedForm()).getTpaFeesStandard();
		previousTpaStandardFees = FeeUIHolder.removeEmptyValueFeeObjects(previousTpaStandardFees);
		List<FeeUIHolder> previousTpaCustomizedFees = ((NoticeInfo404a5Form) form
				.getClonedForm()).getTpaFeesCustomized();
		previousTpaCustomizedFees = FeeUIHolder.removeEmptyValueFeeObjects(previousTpaCustomizedFees);
		
		List<PBARestrictionUi> previousPbaRestrictionList = ((NoticeInfo404a5Form) form.getClonedForm()).getPbaRestrictionList();
		previousPbaRestrictionList = PBARestrictionUi.removeEmptyRestrictionObjects(previousPbaRestrictionList);

		// get changed items
		List<FeeUIHolder> changedNonTpaFees  = FeeUIHolder.getChangedFeeItems(editedNonTpaFees, previousNonTpaFees);
		List<FeeUIHolder> changedTpaStandardFees  = FeeUIHolder.getChangedFeeItems(editedTpaStandardFees, previousTpaStandardFees);
		List<FeeUIHolder> changedTpaCustomizedFees  = FeeUIHolder.getChangedFeeItems(editedTpaCustomizedFees, previousTpaCustomizedFees);
		Set<String> changedDesignatedInvestmentManager = getChangedDesignatedInvestmentManager(editedDesignatedInvestmentManager, previousDesignatedInvestmentManager);
		
		Set<String> changedPersonalBrokerageAccount = getChangedPersonalBrokerageAccount(editedPersonalBrokerageAccount, previousPersonalBrokerageAccount);
		List<PBARestrictionUi> changedPBARestrictions = new ArrayList<PBARestrictionUi>();
		if(previousPersonalBrokerageAccount!=null && Constants.YES.equalsIgnoreCase(previousPersonalBrokerageAccount.getPbaRestriction()) &&  editedPersonalBrokerageAccount!=null && Constants.NO.equalsIgnoreCase(editedPersonalBrokerageAccount.getPbaRestriction())){
			for(PBARestrictionUi res :previousPbaRestrictionList){
				res.setDeletedInd(true);
				changedPBARestrictions.add(res);
			}
		}
		else{
			changedPBARestrictions = PBARestrictionUi.getChangedRestrictionItems(editedPbaRestrictionList, previousPbaRestrictionList);
		}
		
		List<PBAFeeUIHolder> changedPBAStandardFees  = PBAFeeUIHolder.getChangedFeeItems(editedPBAStandardFees, previousPBAStandardFees);
		List<PBAFeeUIHolder> changedPBACustomizedFees  = PBAFeeUIHolder.getChangedFeeItems(editedPBACustomizedFees, previousPBACustomizedFees);		
		
		//To show up teh restrictions that are added/modified/deleted in confirm page
		List<PBARestrictionUi> addedPbaRestrictionList = new ArrayList<PBARestrictionUi>();
		List<PBARestrictionUi> modifiedPbaRestrictionList = new ArrayList<PBARestrictionUi>();
		List<PBARestrictionUi> deletedPbaRestrictionList = new ArrayList<PBARestrictionUi>();
		
		if(changedPBARestrictions!=null && !changedPBARestrictions.isEmpty()){
			for(PBARestrictionUi res : changedPBARestrictions){
				if(res.isDeletedInd()){
					deletedPbaRestrictionList.add(res);
				}
				else if(res.getPbaRestriction().getSeqNo()==0){
					addedPbaRestrictionList.add(res);
				}
				else{
					modifiedPbaRestrictionList.add(res);
				}
			}
		}		

		// if no changes detected, stay back in same page with error
		if (changedNonTpaFees.isEmpty() 
				&& changedTpaStandardFees.isEmpty() 
				&& changedTpaCustomizedFees.isEmpty()
				&& changedDesignatedInvestmentManager.isEmpty()
				&& changedPersonalBrokerageAccount.isEmpty()
				&& (changedPBARestrictions!=null && changedPBARestrictions.isEmpty())
				&& changedPBAStandardFees.isEmpty()
				&& changedPBACustomizedFees.isEmpty()) {
			form.setNonTpaFees(FeeUIHolder.addEmptyCustomFees(form.getNonTpaFees()));
			form.setTpaFeesCustomized(FeeUIHolder.addEmptyCustomFees(form.getTpaFeesCustomized()));
			form.setCustomPBAFees(PBAFeeUIHolder.addEmptyCustomFees(form.getCustomPBAFees()));
			form.setPbaRestrictionList(PBARestrictionUi.addEmptyCustomRestrictions(form.getPbaRestrictionList()));
			errorMessages.add(new ValidationError(new String[] { GLOBAL_ERROR }, ErrorCodes.SAVING_WITH_NO_CHANGES));
			setErrorsInSession(request, errorMessages);
			return forwards.get(Constants.EDIT_404a5_NOTICE_INFO_PAGE);
		}

		
		form.getDesignatedInvestmentManagerUi().setChangedItems(changedDesignatedInvestmentManager);
		form.getPersonalBrokerageAccountUi().setChangedItems(changedPersonalBrokerageAccount);
		form.setUpdatedPbaRestrictionList(changedPBARestrictions);
		
		form.setAddedPbaRestrictionList(addedPbaRestrictionList);
		form.setModifiedPbaRestrictionList(modifiedPbaRestrictionList);
		form.setDeletedPbaRestrictionList(deletedPbaRestrictionList);
		
		form.setUpdatedStandardPBAFees(changedPBAStandardFees);
		form.setUpdatedCustomPBAFees(changedPBACustomizedFees);
		form.setUpdatedTpaFeesStandard(changedTpaStandardFees);
		form.setUpdatedTpaFeesCustomized(changedTpaCustomizedFees);
		form.setUpdatedNonTpaFees(changedNonTpaFees);
		form.setPageMode(PageMode.Edit);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doConfirm");
		}

		return forwards.get(Constants.CONFIRM_404a5_NOTICE_INFO_PAGE);
		
	}
	
	/**
	 * 
	 * get changed DIM info
	 * 
	 * 
	 * @param editedDesignatedInvestmentManager
	 * @param previousDesignatedInvestmentManager
	 * 
	 * @return DesignatedInvestmentManager
	 * @throws SystemException 
	 */
	private Set<String> getChangedDesignatedInvestmentManager(
			DesignatedInvestmentManager editedDesignatedInvestmentManager,
			DesignatedInvestmentManager previousDesignatedInvestmentManager) throws SystemException {
		
		try {
		if(previousDesignatedInvestmentManager == null) {
			if(editedDesignatedInvestmentManager.isEmpty()) {
				return new TreeSet<String>();
			} else {
				previousDesignatedInvestmentManager = new DesignatedInvestmentManager();
			}
		}
		return compareDesignatedInvestmentManager(editedDesignatedInvestmentManager,
				                                  previousDesignatedInvestmentManager,
				                                  DesignatedInvestmentManagerUi.getDimFields());
		} catch (IntrospectionException e) {
			throw new SystemException(e, "Error comparing DIM objects");
		} catch (IllegalAccessException e) {
			throw new SystemException(e, "Error comparing DIM objects");
		} catch (InvocationTargetException e) {
			throw new SystemException(e, "Error comparing DIM objects");
		}
	}
	
	
	/**
	 * 
	 * get changed PBA info
	 * 
	 * 
	 * @param editedDesignatedInvestmentManager
	 * @param previousDesignatedInvestmentManager
	 * 
	 * @return DesignatedInvestmentManager
	 * @throws SystemException 
	 */
	private Set<String> getChangedPersonalBrokerageAccount(PersonalBrokerageAccount editedPersonalBrokerageAccount, PersonalBrokerageAccount previousPersonalBrokerageAccount) throws SystemException {
		
		try {
		if(previousPersonalBrokerageAccount == null) {
			if(editedPersonalBrokerageAccount.isEmpty()) {
				return new TreeSet<String>();
			} else {
				previousPersonalBrokerageAccount = new PersonalBrokerageAccount();
			}
		}
		return comparePersonalBrokerageAccount(editedPersonalBrokerageAccount,
													previousPersonalBrokerageAccount,
													PersonalBrokerageAccountUi.getPbaFields());
		} catch (IntrospectionException e) {
			throw new SystemException(e, "Error comparing PBA objects");
		} catch (IllegalAccessException e) {
			throw new SystemException(e, "Error comparing PBA objects");
		} catch (InvocationTargetException e) {
			throw new SystemException(e, "Error comparing PBA objects");
		}
	}
	
	private static Set<String> compareDesignatedInvestmentManager(
			DesignatedInvestmentManager latestRow,
			DesignatedInvestmentManager previousRow,
			Set<String> fields)
			throws IntrospectionException, IllegalAccessException,
			InvocationTargetException {
		Set<String> changeItems = new TreeSet<String>();
		BeanInfo beanInfo = Introspector.getBeanInfo(latestRow.getClass());
		for (PropertyDescriptor prop : beanInfo.getPropertyDescriptors()) {
			if (fields.contains(prop.getName())) {
				Method getter = prop.getReadMethod();
				Object newValue = getter.invoke(latestRow);
				Object oldValue = getter.invoke(previousRow);
				if (newValue == oldValue
						|| (newValue != null && newValue.equals(oldValue))) {
					continue;
				}
				changeItems.add(prop.getName());
			}
		}

		return changeItems;
	}
	
	
	//TODO need to have common method for comparing DIM and PBA.
	private static Set<String> comparePersonalBrokerageAccount(
			PersonalBrokerageAccount latestRow,
			PersonalBrokerageAccount previousRow,
			Set<String> fields)
			throws IntrospectionException, IllegalAccessException,
			InvocationTargetException {
		Set<String> changeItems = new TreeSet<String>();
		BeanInfo beanInfo = Introspector.getBeanInfo(latestRow.getClass());
		for (PropertyDescriptor prop : beanInfo.getPropertyDescriptors()) {
			if (fields.contains(prop.getName())) {
				Method getter = prop.getReadMethod();
				Object newValue = getter.invoke(latestRow);
				Object oldValue = getter.invoke(previousRow);
				if (newValue == oldValue
						|| (newValue != null && newValue.equals(oldValue))) {
					continue;
				}
				changeItems.add(prop.getName());
			}
		}

		return changeItems;
	}
	
}