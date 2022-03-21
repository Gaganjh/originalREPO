package com.manulife.pension.ps.web.census;

import static com.manulife.pension.service.contract.valueobject.WithdrawalReason.DEATH;
import static com.manulife.pension.service.contract.valueobject.WithdrawalReason.DISABILITY;
import static com.manulife.pension.service.contract.valueobject.WithdrawalReason.PRE_RETIREMENT;
import static com.manulife.pension.service.contract.valueobject.WithdrawalReason.RETIREMENT;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingDetails;
import com.manulife.pension.ps.service.submission.util.VestingMoneyTypeComparator;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.EmployeeServiceFacade;
import com.manulife.pension.ps.web.census.util.ParameterizedActionForward;
import com.manulife.pension.ps.web.census.util.VestingExplanationRetriever;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWCencusVesting;
import com.manulife.pension.service.contract.util.PlanConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.contract.valueobject.VestingSchedule;
import com.manulife.pension.service.employee.valueobject.ApplyLTPTCreditingInfo;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.employee.valueobject.EmployeeFullyVestedIndicatorInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVYOSInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo.VestingType;
import com.manulife.pension.service.employee.valueobject.EmployeeVestingVO;
import com.manulife.pension.service.employee.valueobject.ParticipantContractVO;
import com.manulife.pension.service.employee.valueobject.UserIdType;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;


@Controller
@RequestMapping(value = "/census")
@SessionAttributes({"vestingInformationForm"})

public class CensusVestingInformationController extends PsAutoController {

	@ModelAttribute("vestingInformationForm") 
	public VestingInformationForm populateForm()
	{
		return new VestingInformationForm();
	}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	
	static{
		forwards.put("input","/census/viewVestingInformation.jsp"); 
		forwards.put("viewVestingInformation","/census/viewVestingInformation.jsp");
		forwards.put("editVestingRedirect","redirect:/do/census/editVestingInformation/"); 
		forwards.put("censusVesting","redirect:/do/census/censusVesting/");
		forwards.put("employeeSnapshot","redirect:/do/census/viewEmployeeSnapshot/");
		forwards.put("viewVestingRedirect","redirect:/do/census/viewVestingInformation/");
		forwards.put("editVestingInformation","/census/editVestingInformation.jsp");
		}
	 
    protected static EmployeeServiceFacade serviceFacade = new EmployeeServiceFacade();
    public static final FastDateFormat DATE_FORMATTER = ContractDateHelper.getDateFormatter("MM/dd/yyyy");
    
	public CensusVestingInformationController() {
		super(CensusVestingInformationController.class);
	}

    /**
     * Default action
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
	@RequestMapping(value= {"/viewVestingInformation/","/editVestingInformation/"},method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("vestingInformationForm") VestingInformationForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		String editMode = request.getParameter(Constants.EDIT_MODE);
		if(bindingResult.hasErrors()){
			VestingInformation  vetingInfo = (VestingInformation)request.getSession(false).getAttribute(CensusConstants.VESTING_INFO);
			request.setAttribute(CensusConstants.VESTING_INFO, vetingInfo);	
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              
	              if (Constants.TRUE.equals(editMode)) {
	            	  return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("editVestingInformation");
	              } else {
	            	  return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewVestingInformation");
	              }
	       }
		}
		 String profileIdValue = request.getParameter(Constants.PROFILE_ID_PARAMETER);
        String asOfDateValue = request.getParameter(Constants.AS_OF_DATE_PARAMETER);
        String source = request.getParameter(Constants.SOURCE_PARAMETER);
        
        if (StringUtils.isEmpty(profileIdValue)) {
            throw new SystemException("No profileId parameter");
        }
        
        VestingInformationForm vestingInfoForm = (VestingInformationForm) actionForm;
        UserProfile userProfile = SessionHelper.getUserProfile(request);
        Contract currentContract = userProfile.getCurrentContract();
        long profileId = Long.parseLong(profileIdValue);
        Date asOfDate = null;
        if (!StringUtils.isEmpty(asOfDateValue)) {
            try {
                asOfDate = DATE_FORMATTER.parse(asOfDateValue);
            } catch (ParseException e) {
                logger.error("An invalid date format :" + asOfDateValue);
            }
        }
        
        // if no date is passed, use the current calendar day
        if (asOfDate == null) {
            asOfDate = new Date();
        }
        
        // populate as of date fields
        vestingInfoForm.setSourceAsOfDate(asOfDateValue);
        vestingInfoForm.setAsOfDate(DATE_FORMATTER.format(asOfDate));
                
        // retrieve employee with vesting data
        Employee employee = serviceFacade.getEmployee(profileId,
                userProfile, asOfDate, true);
        
        // populate vestingInformation value object
        VestingInformation vestingInformation = getVestingInformation(employee, asOfDate, vestingInfoForm.getPlanFieldNames());
                
        // check if participant has a partial status
        checkPartialParticipantStatus(employee, vestingInformation);
        
        // populate all param info
        EmployeeVestingInfo allParamInfo = serviceFacade.getEmployeeVestingInfo(new Long(profileId), 
                userProfile.getCurrentContract().getContractNumber(), 
                new Date(), VestingType.ALL, true, false, true);
        EmployeeVestingInfo filteredStatusInfo = vestingInfoForm.filterOutCanceledStatus(userProfile, allParamInfo);
        vestingInformation.getVestingParamInfo().put(VestingType.EMPLOYMENT_STATUS, filteredStatusInfo);
        vestingInformation.getVestingParamInfo().put(VestingType.VYOS, allParamInfo); 
        if (allParamInfo.getHireDate() != null) {
            vestingInformation.setEmployeeHireDateInfo(allParamInfo.getHireDate());
        }
        if(allParamInfo.getApplyLTPTCreditingInfo() !=null) {
        	vestingInformation.setApplyLTPTCreditingInfo(allParamInfo.getApplyLTPTCreditingInfo());
        }
        
        // Retrieve fullyVestedIndicator again, but don't use any date so that we retrieve whatever
        // value is in CSDB (could be future dated as well)
        vestingInformation.getVestingParamInfo().put(
                VestingType.FULLY_VESTED_IND,
                vestingInfoForm.getFullyVestedInfoIgnoringDate(serviceFacade, userProfile,
                        allParamInfo));
        // set the contract whether RA457 plan or not
        vestingInfoForm.setPlan457(Constants.PRODUCT_RA457.equalsIgnoreCase(currentContract.getProductId()));
        vestingInfoForm.populateForm(vestingInformation);
        
        // store the cloned form for change tracking
        vestingInfoForm.storeClonedForm();
       
        request.setAttribute(CensusConstants.VESTING_INFO, vestingInformation);	
        request.getSession().setAttribute(CensusConstants.VESTING_INFO, vestingInformation);
       // saveToken(request);
        
        if (Constants.TRUE.equals(editMode)) {
            return forwards.get("editVestingInformation");
        } else {
            return forwards.get("viewVestingInformation");
        }
		}
	@RequestMapping(value="/viewVestingInformation/",params={"action=default"},method ={RequestMethod.POST}) 
	public String doDefaultAction(@Valid @ModelAttribute("vestingInformationForm") VestingInformationForm actionForm, BindingResult bindingResult, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException,
            SystemException {
		if(bindingResult.hasErrors()){
			VestingInformation  vetingInfo = (VestingInformation)request.getSession(false).getAttribute(CensusConstants.VESTING_INFO);
			request.setAttribute(CensusConstants.VESTING_INFO, vetingInfo);	
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("editVestingInformation");//if input forward not //available, provided default
	       }
		} 
		String forward=doDefault(actionForm, bindingResult, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	/**
     * Cancel the edit and return to the view vesting information page
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
	@RequestMapping(value="/editVestingInformation/",params={"action=cancel"},method ={RequestMethod.POST}) 
	public String doCancel(@Valid @ModelAttribute("vestingInformationForm") VestingInformationForm actionForm, BindingResult bindingResult, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException,
            SystemException {
		if(bindingResult.hasErrors()){
			VestingInformation  vetingInfo = (VestingInformation)request.getSession(false).getAttribute(CensusConstants.VESTING_INFO);
			request.setAttribute(CensusConstants.VESTING_INFO, vetingInfo);	
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("editVestingInformation");//if input forward not //available, provided default
	       }
		}    
        return forwardToViewVestingInformation(actionForm, request);
    }
    
    /**
     * Save the Vesting parameter information
     *
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @param ignoreWarning
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
	@RequestMapping(value="/editVestingInformation/",params={"action=save"},method ={RequestMethod.POST}) 
	public String doSave(@Valid @ModelAttribute("vestingInformationForm") VestingInformationForm actionForm, BindingResult bindingResult, 
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
			VestingInformation  vetingInfo = (VestingInformation)request.getSession(false).getAttribute(CensusConstants.VESTING_INFO);
			request.setAttribute(CensusConstants.VESTING_INFO, vetingInfo);	
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("editVestingInformation");//if input forward not //available, provided default
	       }
		}

        VestingInformationForm f = (VestingInformationForm) actionForm;
        VestingInformationForm clonedForm = (VestingInformationForm) f.getClonedForm();
        
        UserProfile userProfile = getUserProfile(request);
        String userIdType = userProfile.isInternalUser() ? UserIdType.UP_INTERNAL
                : UserIdType.UP_EXTERNAL;
        String sourceChannelCode = Constants.PS_APPLICATION_ID;

        try {
            
            EmployeeVestingInfo vestingInfo = new EmployeeVestingInfo(Long.parseLong(f.getProfileId()), 
                    userProfile.getCurrentContract().getContractNumber());
            
            
            // populate vyos related values
            for (int i = 0; i < 3; i++) {
                boolean isCurrent = (i == 1 ? true : false);
                Date vyosEffectiveDate = null;
                
                try {
                    vyosEffectiveDate = DATE_FORMATTER.parse(f.getVyosDate(i));
                } catch (ParseException e) {
                    logger.error("An invalid date format :" + f.getVyosDate(i));
                }
                
                Integer vyos = StringUtils.isEmpty(f.getVyos(i)) ? null : new Integer(f.getVyos(i));
                
                EmployeeVYOSInfo vyosInfo = new EmployeeVYOSInfo(vyos, 
                        new java.sql.Date(vyosEffectiveDate.getTime()), 
                        Long.toString(userProfile.getPrincipal().getProfileId()),
                        sourceChannelCode, isCurrent, new Date(), userIdType);
                
                // if not empty before and empty after, set deleteFlag to true (record will be deleted)
                if (StringUtils.isEmpty(f.getVyos(i)) && 
                        !StringUtils.isEmpty(clonedForm.getVyos(i))) {
                        // set deleteFlag to true
                        vyosInfo.setDeleteFromDB(true);
                }
                
                vestingInfo.addVYOS(vyosInfo);
            }
            
            // populate fully vested related values
            Date fullyVestedEffectiveDateNew = null;
            Date fullyVestedEffectiveDateCloned = null;
            
            try {
                fullyVestedEffectiveDateNew = DATE_FORMATTER.parse(f.getFullyVestedEffectiveDate());
            } catch (ParseException e) {
                logger.info("An invalid date format :" + f.getFullyVestedEffectiveDate());
            }
            
            try {
                fullyVestedEffectiveDateCloned = DATE_FORMATTER.parse(clonedForm.getFullyVestedEffectiveDate());
            } catch (ParseException e) {
                logger.info("An invalid date format :" + clonedForm.getFullyVestedEffectiveDate());
            }
            
            java.sql.Date fullyVestedDate = fullyVestedEffectiveDateNew == null ? 
                    fullyVestedEffectiveDateCloned == null ? null : new java.sql.Date(fullyVestedEffectiveDateCloned.getTime()) : 
                    new java.sql.Date(fullyVestedEffectiveDateNew.getTime());
            
            EmployeeFullyVestedIndicatorInfo fullyVestedInfo = new EmployeeFullyVestedIndicatorInfo(
                    new Boolean(f.getFullyVestedInd().equals("Y") ? true : false),
                    fullyVestedDate, 
                    Long.toString(userProfile.getPrincipal().getProfileId()),
                    sourceChannelCode, true, new Date(), userIdType);
            
            // only update if fully vested indicator or effective date changed
            if (!StringUtils.equals(f.getFullyVestedEffectiveDate(), clonedForm.getFullyVestedEffectiveDate()) ||
                !StringUtils.equals(f.getFullyVestedInd(), clonedForm.getFullyVestedInd())) {
                fullyVestedInfo.setUpdateDatabase(true); 
            } else {
                fullyVestedInfo.setUpdateDatabase(false); 
            }
            vestingInfo.setFullyVestedIndicator(fullyVestedInfo);
			
            if (!f.getLtptCrediting().equals(clonedForm.getLtptCrediting())) {
				vestingInfo.setApplyLTPTCreditingInfo(new ApplyLTPTCreditingInfo(f.getLtptCrediting(),
						new java.sql.Date(resetTheTime(Calendar.getInstance()).getTimeInMillis()),
						Long.toString(userProfile.getPrincipal().getProfileId()), sourceChannelCode, true, new Date(),
						userIdType));
			} else {
				vestingInfo.setApplyLTPTCreditingInfo(null);

			}
            
            // save
            serviceFacade.updateEmployeeVestingInfo(vestingInfo);
          
        } catch (SystemException e) {
            logger.error("Fail to update employee", e);
            removeFormUponExit( request);
            throw e;
        }
        
        return forwardToViewVestingInformation( actionForm, request);
    }
	
	/**
	* This method is used to reset the time to 00:00:00 a.m.
	* @param cal
	* @return GregorianCalendar
	*/
	private  Calendar resetTheTime(Calendar cal) {

		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);

	 return cal;
	}
    
	
	public String forwardToViewVestingInformation(VestingInformationForm actionForm, HttpServletRequest request) 
	throws IOException,ServletException, SystemException { 
        String profileIdValue = request.getParameter(Constants.PROFILE_ID_PARAMETER);
        String asOfDateValue = request.getParameter(Constants.AS_OF_DATE_PARAMETER);
        String source = request.getParameter(Constants.SOURCE_PARAMETER);
        String f = forwards.get("viewVestingRedirect");
        
        ParameterizedActionForward editForward = new ParameterizedActionForward(f);
        editForward.addParameter(Constants.PROFILE_ID_PARAMETER, profileIdValue);
        editForward.addParameter(Constants.AS_OF_DATE_PARAMETER, asOfDateValue);
        editForward.addParameter(Constants.SOURCE_PARAMETER, source);
        
        return editForward.getPath();
    }

    
	
	
	@RequestMapping(value="/viewVestingInformation/",params={"action=edit"},method ={RequestMethod.POST}) 
	public String doEdit(@Valid @ModelAttribute("vestingInformationForm") VestingInformationForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException { 
		if(bindingResult.hasErrors()){
			VestingInformation  vetingInfo = (VestingInformation)request.getSession(false).getAttribute(CensusConstants.VESTING_INFO);
			request.setAttribute(CensusConstants.VESTING_INFO, vetingInfo);	
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("editVestingInformation");//if input forward not //available, provided default
	       }
		}
	    String profileIdValue = request.getParameter(Constants.PROFILE_ID_PARAMETER);
        String asOfDateValue = request.getParameter(Constants.AS_OF_DATE_PARAMETER);
        String source = request.getParameter(Constants.SOURCE_PARAMETER);
        String f = forwards.get("editVestingRedirect");
        
        ParameterizedActionForward editForward = new ParameterizedActionForward(f);
        editForward.addParameter(Constants.PROFILE_ID_PARAMETER, profileIdValue);
        editForward.addParameter(Constants.AS_OF_DATE_PARAMETER, asOfDateValue);
        editForward.addParameter(Constants.SOURCE_PARAMETER, source);
        editForward.addParameter(Constants.EDIT_MODE, "true");
        
        return editForward.getPath();
    }
    
    /**
     * Check if participant has a partial status
     */
    private void checkPartialParticipantStatus(final Employee employee,
            final VestingInformation vestingInformation) {

        final ParticipantContractVO participantContractVo = employee.getParticipantContract();
        if (participantContractVo != null) {
            final String participantStatusCode = participantContractVo.getParticipantStatusCode();

            if (CensusVestingDetails.isParticipantStatusPartial(participantStatusCode)) {
                // The status code is a partial code.
                vestingInformation.setPartialParticipantStatus(Boolean.TRUE);
            } else {
                vestingInformation.setPartialParticipantStatus(Boolean.FALSE);
            } // fi
        } // fi
    }

    private VestingInformation getVestingInformation(Employee employee, Date asOfDate, List planFieldNames) throws SystemException {
        
        Integer contractId = employee.getEmployeeDetailVO().getContractId();
        
       
        final PlanDataLite planDataLite = ContractServiceDelegate.getInstance().getPlanDataLight(
                new Integer(contractId));
        Date planLastUpdatedDate = ContractServiceDelegate.getInstance().getPlanLastUpdatedDate(contractId, (Collection)planFieldNames);
        
        VestingInformation vi = new VestingInformation();
        
        vi.setAsOfDate(asOfDate);
        vi.setFullyVestedOnDeath(planDataLite.getWithdrawalReasons().contains(DEATH));
        vi.setFullyVestedOnRetirement(planDataLite.getWithdrawalReasons().contains(RETIREMENT));
        vi.setFullyVestedOnEarlyRetirement(planDataLite.getWithdrawalReasons().contains(PRE_RETIREMENT));
        vi.setFullyVestedOnDisability(planDataLite.getWithdrawalReasons().contains(DISABILITY));
        
        vi.setEmployeeFirstName(employee.getEmployeeDetailVO().getFirstName());
        vi.setEmployeeLastName(employee.getEmployeeDetailVO().getLastName());
        vi.setEmployeeMiddleInit(employee.getEmployeeDetailVO().getMiddleInitial());
        vi.setEmployeeSSN(employee.getEmployeeDetailVO().getSocialSecurityNumber());
        
        vi.setPlanHoursOfService(planDataLite.getHoursOfService());
        vi.setPlanLastUpdatedDate(planLastUpdatedDate);
        
        final EmployeeVestingVO employeeVestingVO = employee.getEmployeeVestingVO();
        EmployeeVestingInformation evi = employeeVestingVO.getEmployeeVestingInformation();
        vi.setEmployeeVestingInformation(evi);
        
        // store effective inputs to the vestingEffectiveInput map
        vi.storeEffectiveInputs(evi);
        
        // get the dynamic explanation
        List<Integer> cmaList = VestingExplanationRetriever.getInstance().retrieveExplanation(vi, employee, evi, null, asOfDate);
        vi.setExplanation(cmaList);
         
        // get the list of money types
        final Map<String, Collection> lookupData = ContractServiceDelegate.getInstance().getLookupData(contractId);
        final List<MoneyTypeVO> list = (List<MoneyTypeVO>)lookupData.get(PlanConstants.MONEY_TYPES_BY_CONTRACT);
        Collections.sort(list, new VestingMoneyTypeComparator());
        vi.setMoneyTypes(list);
        
        // get the vesting schedule
        final Collection<VestingSchedule> vestingSchedules = planDataLite.getVestingSchedules();
        vi.setVestingSchedules(vestingSchedules);
        
        return vi;

    }
    
    /**
     * Navigate back to source page
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @RequestMapping(value={"/viewVestingInformation/"},params={"action=back"} , method =  {RequestMethod.POST}) 
	public String doBack(@Valid @ModelAttribute("vestingInformationForm") VestingInformationForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
			VestingInformation  vetingInfo = (VestingInformation)request.getSession(false).getAttribute(CensusConstants.VESTING_INFO);
			request.setAttribute(CensusConstants.VESTING_INFO, vetingInfo);	
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("viewVestingInformation");//if input forward not //available, provided default
	       }
    	}
        return  forwardToSource(request, actionForm);
    }
    
    /**
     * Navigate back to the source page
     * 
     * @param mapping
     * @param request
     * @param form
     * @return
     */
	protected String forwardToSource(HttpServletRequest request,VestingInformationForm actionForm) 
	throws IOException,ServletException, SystemException {
        removeFormUponExit( request);
        if (CensusConstants.CENSUS_VESTING_PAGE.equals(actionForm.getSource())) {
            String configured = forwards.get(CensusConstants.CENSUS_VESTING_PAGE);
            ParameterizedActionForward realForward = new ParameterizedActionForward(configured);
            
            // send the asOfDate as a parameter
            Date asOfDate = null;
            try {
                asOfDate = DATE_FORMATTER.parse(actionForm.getSourceAsOfDate());
            } catch (ParseException e) {
                logger.error("An invalid date format :" + actionForm.getSourceAsOfDate());
            }
            if (asOfDate != null) {
                realForward.addParameter(Constants.AS_OF_DATE_PARAMETER, asOfDate.getTime() + "");
            }
            return realForward.getPath();
        } else if (CensusConstants.EMPLOYEE_SNAPSHOT_PAGE.equals(actionForm.getSource())) {
            String configured = forwards.get(CensusConstants.EMPLOYEE_SNAPSHOT_PAGE);
            ParameterizedActionForward realForward = new ParameterizedActionForward(configured);
            realForward.addParameter(Constants.PROFILE_ID_PARAMETER, actionForm.getProfileId());
            return realForward.getPath();
        } else {
            return forwards.get(actionForm.getSource());
        }
    }
	
    
        /**
     * Exit from the current page
     * 
     * @param mapping
     * @param request
     */
    protected void removeFormUponExit(
            HttpServletRequest request) {
       request.getSession(false).removeAttribute("VestingInformationForm");
    }
    
    
    @Autowired
	   private PSValidatorFWCencusVesting  psValidatorFWCencusVesting;

    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind(request);
	    binder.addValidators(psValidatorFWCencusVesting);
	}
}
