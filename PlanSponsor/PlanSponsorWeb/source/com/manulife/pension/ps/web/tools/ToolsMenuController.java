
package com.manulife.pension.ps.web.tools;

import static com.manulife.pension.platform.web.CommonConstants.PS_APPLICATION_ID;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
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

import com.manulife.pension.delegate.EligibilityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.census.util.CensusUtils;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.util.MCEnvironment;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.pagelayout.LayoutBean;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWToolsMenu;
import com.manulife.pension.ps.web.withdrawal.WithdrawalRequestUi;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalWebUtil;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractMoneyTypeEligibilityVO;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.eligibility.util.LongTermPartTimeAssessmentUtil;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.util.BaseEnvironment;
@Controller
@RequestMapping(value ="/tools")

public class ToolsMenuController extends PsController {

	
	@ModelAttribute("dynaForm") 
	public DynaForm populateForm()
	{ 
		return new DynaForm();
		}
	public static Map<String,String> forwards = new HashMap<>();
	private static final String TOOLSMENU = "toolsMenu";
	 public static final String  IS_LIVESITE_VARIABLE = "isLiveSite";
	 public static final String  IS_FCP_LIVE_VARIABLE = "isFcpLive";
	static{
		forwards.put(TOOLSMENU,"/tools/toolsMenu.jsp");
	}

   //SimpleDateFormat is converted to FastDateFormat to make it thread safe
    private static final FastDateFormat DATE_FORMATTER_KEY = FastDateFormat.getInstance("yyyy/MM/dd");

    private static final FastDateFormat  DATE_FORMATTER_VALUE = FastDateFormat.getInstance("MMM dd yyyy");

    public ToolsMenuController() {
        super(ToolsMenuController.class);
    }

    
   @RequestMapping(value = "/toolsMenu/" , method =  {RequestMethod.GET}) 
    public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {
	   UserProfile userProfile = getUserProfile(request);
	   if(bindingResult.hasErrors()){
       	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
       	if(errDirect!=null){
       		try {
				request.setAttribute(Constants.REQUEST_TYPE,WithdrawalWebUtil.getTypeOfRequest(userProfile));
			} catch (SystemException e) {
				e.printStackTrace();
			}
            request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
            return forwards.get(TOOLSMENU);
       	}
       }
	     
       
       FunctionalLogger.INSTANCE.log("Tools page", userProfile, getClass(), "doExecute");
       // To check if the contract has EC is on/off and part time eligibility rules
       isDisplayLongTermPartTimeInfoTemplate(request);
       // get permissions for the "Submission History" page
        if ((userProfile.isSelectedAccess() && userProfile.isAllowedUploadSubmissions())
                || (!userProfile.isSelectedAccess() && userProfile.isSubmissionAccess())) {
            request.setAttribute(Constants.SUBMISSION_HISTORY_ACCESS, true);
        } else {
            request.setAttribute(Constants.SUBMISSION_HISTORY_ACCESS, false);
        }

        //DOL_FCP changes
        BaseEnvironment environment = new BaseEnvironment();

		boolean isLiveNaming = Boolean.parseBoolean(environment
				.getNamingVariable(IS_LIVESITE_VARIABLE,null));
		
		
		boolean isFcpLiveNaming = Boolean.parseBoolean(environment
				.getNamingVariable(IS_FCP_LIVE_VARIABLE,null));
		
		if(!isLiveNaming || isFcpLiveNaming) {
			request.setAttribute(Constants.IS_FCP_CONTENT,true);
		}
                
        // get permissions for vesting template link
        int contractNumber = userProfile.getContractProfile().getContract().getContractNumber();

        TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(
                contractNumber);

        boolean vestingAccess = ((userProfile.getRole().isExternalUser()
                && userProfile.getRole().isTPA() && userProfile.isAllowedSubmitUpdateVesting() && firmInfo
                .getContractPermission().isSubmitUpdateVesting()) || userProfile
                .isAllowedSubmitUpdateVesting())
                && CensusUtils.isVestingEnabled(contractNumber);
        request.setAttribute(Constants.VESTING_ACCESS, vestingAccess);
        request.setAttribute(Constants.VESTING_CALCULATED, CensusUtils.isVestingCalculated(contractNumber));

        // Get permission for withdrawals tool link
        final ContractInfo contractInfo = WithdrawalServiceDelegate.getInstance().getContractInfo(
                userProfile.getCurrentContract().getContractNumber(), userProfile.getPrincipal());
        WithdrawalRequestUi.populatePermissions(contractInfo, userProfile.getPrincipal());
        request.setAttribute(Constants.WITHDRAWAL_TOOLS_LINK_ACCESS, contractInfo
                .getShowWithdrawalToolsLink());
        //To check the request type . ie. loan , withdrawal or loanandwithdrawal 
        request.setAttribute(Constants.REQUEST_TYPE,WithdrawalWebUtil.getTypeOfRequest(userProfile));
        
        
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("doExecute> Loaded withdrawal contract information [")
                .append(contractInfo).append("]").toString());
        } // fi
        
		EligibilityServiceDelegate eligibilityServiceDelegate = EligibilityServiceDelegate
				.getInstance(PS_APPLICATION_ID);
        
        List<Date> moneyTypePEDS = new ArrayList<Date>();
		List<ContractMoneyTypeEligibilityVO> totalMoneyTypePEDS = eligibilityServiceDelegate
				.getmoneyTypePastAndFuturePEDs(contractNumber);

	Calendar cal = GregorianCalendar.getInstance();
	cal.setTime(new Date());
	cal.set(Calendar.HOUR, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);
	cal.set(Calendar.AM_PM, Calendar.AM);
	
	for(int ped=0; ped < totalMoneyTypePEDS.size(); ped++ ){

	   
		ContractMoneyTypeEligibilityVO vo = totalMoneyTypePEDS.get(ped);
	    String moneyType = vo.getMoneyType()== null ? "":vo.getMoneyType().trim();
	    if("EEDEF".equalsIgnoreCase(moneyType)){
		if(vo.getInitialEnrollmentDate()!= null && vo.getNextPlanEntryDate()!= null 
			&& vo.getInitialEnrollmentDate().compareTo(vo.getNextPlanEntryDate())!=0
			&& vo.getInitialEnrollmentDate().compareTo(cal.getTime()) >= 0){
			    
		    moneyTypePEDS.add(vo.getInitialEnrollmentDate());
		    moneyTypePEDS.add(vo.getNextPlanEntryDate());
		    moneyTypePEDS.add(vo.getNextPlanEntryDate2());
		    
		}else{
		    
		    moneyTypePEDS.add(vo.getNextPlanEntryDate());
		    moneyTypePEDS.add(vo.getNextPlanEntryDate2());
		    moneyTypePEDS.add(vo.getNextPlanEntryDate3());
		}
	    }
	    
	}
      
        if (moneyTypePEDS.size() > 0) {
            request.setAttribute(ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF,
                    ServiceFeatureConstants.YES);

            Iterator<Date> it = moneyTypePEDS.iterator();
            TreeMap<String,String> npeDates = new TreeMap<String,String>();
            while (it.hasNext()) {
                Date date = (Date) it.next();
                npeDates.put(DATE_FORMATTER_KEY.format(date), DATE_FORMATTER_VALUE
                        .format(date));
            }
            request.setAttribute(Constants.NEXT_PLAN_ENTRY_DATES, npeDates);
        }
        
        
        // retrieve auto-enrollment feature
        try {

            ContractServiceFeature csf = com.manulife.pension.delegate.ContractServiceDelegate
                    .getInstance().getContractServiceFeature(contractNumber,
                            ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
            if (ContractServiceFeature.internalToBoolean(csf.getValue()).booleanValue()) {
               
                if (moneyTypePEDS.size() > 0) {
                    request.setAttribute(ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE,
                            ServiceFeatureConstants.YES);
                   
                } else{
                    request.setAttribute(ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE,
                            ServiceFeatureConstants.NO);
                }
            } else{
                request.setAttribute(ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE,
                        ServiceFeatureConstants.NO);
            }
        } catch (ApplicationException ae) {
            throw new SystemException(ae.getMessage());
        }

		// retrieve auto-increase feature
		String signUpMethod = com.manulife.pension.delegate.ContractServiceDelegate.getInstance()
		                                       .determineSignUpMethod(contractNumber);
		if (StringUtils.equals(ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO, signUpMethod)) {
			request.setAttribute(ServiceFeatureConstants.ACI_SIGNUP_METHOD_AUTO,
					ServiceFeatureConstants.YES);
			request.getSession().setAttribute(Constants.SIGNUP_METHOD, Constants.ACI_AUTO);
		} else if (StringUtils.equals(ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP, signUpMethod)) {
			request.setAttribute(
					ServiceFeatureConstants.ACI_SIGNUP_METHOD_SIGNUP,
					ServiceFeatureConstants.YES);
			request.getSession().setAttribute(Constants.SIGNUP_METHOD, Constants.ACI_SIGNUP);
		} 
		
		// retrieve Eligibility Calculation feature
        try {
            
            ContractServiceFeature csf = com.manulife.pension.delegate.ContractServiceDelegate
                    .getInstance().getContractServiceFeature(contractNumber,
                            ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF);
            if (ContractServiceFeature.internalToBoolean(csf.getValue()).booleanValue()) {
        	if (moneyTypePEDS.size() > 0) {
        	    request.setAttribute(ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF,
                            ServiceFeatureConstants.YES);
        	}else{
                    request.setAttribute(ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF,
                            ServiceFeatureConstants.NO);
        	}
            } else {
                request.setAttribute(ServiceFeatureConstants.ELIGIBILITY_CALCULATION_CSF,
                        ServiceFeatureConstants.NO);
            }
        } catch (ApplicationException ae) {
            throw new SystemException(ae.getMessage());
        }
       
        ServletContext servlet = request.getSession().getServletContext(); 
        // check if the message center summary box should be displayed
        if (userProfile.isSelectedAccess() && MCEnvironment.isMessageCenterSummaryBoxAvailable(request)) {
	        request.setAttribute("mcModel", MCUtils
					.getHomePageBoxModel(request,servlet, userProfile));
        }
        return forwards.get(TOOLSMENU);
    }

    protected String createLayoutBean(HttpServletRequest request, String forward
            ) {
        // Special code required for partial sub-menu on MTA contract.
        // The Resources menu has a partial sub-menu - Tools and Quarterly Investment Guide only.
        // Full Resources menu has 5 sub-menus and the Tools submenu is in position 3.
        // Therefore, we must reset the submenu for the Tools page
        // from 3 (in a full navbar) to 1.

        LayoutBean bean = null;

        // get the current LayoutBean
        if (forward != null) {
          //  bean = LayoutBeanRepository.getInstance().getPageBean(forward.getPath());
        }

        // get the user profile object to determine is the contract is MTA
        UserProfile userProfile = getUserProfile(request);
        boolean isMta = userProfile.getContractProfile().getContract().isMta();

        // if MTA contract, clone current bean, forward to page, and save cloned bean in request
        if (isMta) {
            LayoutBean cloneBean = (LayoutBean) bean.clone();
            cloneBean.setSubmenu("1");
            if (cloneBean != null) {
                forward = forwards.get(cloneBean.getLayoutURL());
                request.setAttribute(Constants.LAYOUT_BEAN, cloneBean);
            }
        }

        // if bean is null it means the request is not going
        // to be forwarded to jsp(one of the layout pages).

        // DEFAULT FORWARD - if not MTA contract (not the condition we handled above) and if the
        // bean is NOT null
        // forward to page and save the orginal bean in request.
        if ((!isMta) && (bean != null)) {
            forward = forwards.get(bean.getLayoutURL());
            request.setAttribute(Constants.LAYOUT_BEAN, bean);

            // for defined benefit contracts
            if (getUserProfile(request).getCurrentContract() != null
                    && getUserProfile(request).getCurrentContract().isDefinedBenefitContract()) {
                if (bean.getDefinedBenefitContentId() > 0) {
                    LayoutBean cloneBean = (LayoutBean) bean.clone();
                    cloneBean.setContentId(cloneBean.getDefinedBenefitContentId());
                    request.setAttribute(Constants.LAYOUT_BEAN, cloneBean);
                }
            }
            // for gifl version 3 content layout
            if (getUserProfile(request).getCurrentContract() != null
                    && Constants.GIFL_VERSION_03.equals(getUserProfile(request).getCurrentContract().getGiflVersion())) {
	            		if (bean.getGiflSelectContentId() > 0) {
	                    LayoutBean cloneBean = (LayoutBean) bean.clone();
	                    cloneBean.setContentId(cloneBean.getGiflSelectContentId());
	                    request.setAttribute(Constants.LAYOUT_BEAN, cloneBean);
                }
            }
            
        }


        return forward;
    }
    
    /**
	 * Check if the contract has EligibilityCalculationService is on/off and part time eligible rules
	 * 
	 */
	public void isDisplayLongTermPartTimeInfoTemplate(HttpServletRequest request) throws SystemException
	{
		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();
		String longTermPartTimeInfoTemplateAccess = "N";

		if (!(Constants.PRODUCT_RA457.equalsIgnoreCase(currentContract.getProductId()))) {
			List<String> allMoneyTypeIdsWithPartTimeEnabledList = LongTermPartTimeAssessmentUtil.getInstance()
					.getAllMoneyTypeIdsWithPartTimeEnabled(currentContract.getContractNumber());
			if (!(allMoneyTypeIdsWithPartTimeEnabledList.isEmpty())) {
				longTermPartTimeInfoTemplateAccess = "Y";
			}
		}
		request.setAttribute(Constants.LONG_TERM_PART_TIME_INFO_TEMPLATE_ACCESS, longTermPartTimeInfoTemplateAccess);
	}

    /**
	 *  This code has been changed and added to validate form and request against penetration attack, prior to other validations
	 *  
	 * @see com.manulife.pension.platform.web.controller.BaseController#doValidate(org.apache.struts.action.ActionMapping, org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
	*/
	
	 @Autowired
	   private PSValidatorFWToolsMenu  psValidatorFWToolsMenu;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWToolsMenu);
	}
	
}