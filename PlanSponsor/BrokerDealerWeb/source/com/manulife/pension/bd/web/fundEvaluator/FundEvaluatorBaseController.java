package com.manulife.pension.bd.web.fundEvaluator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.fundEvaluator.common.CoreToolGlobalData;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;

/**
 * Base action class for the FundEvaluator action class.
 *  
 * @author Ranjith Kumar
 *
 */

public abstract class FundEvaluatorBaseController extends BaseAutoController {
   
    private HashMap<String, FundEvaluatorPageNavigationInfo> pageNavigationInformation = new HashMap<String, FundEvaluatorPageNavigationInfo>();
    
    // Static block for initializing the FundEvaluator page navigation information
    {
        pageNavigationInformation.put(FundEvaluatorConstants.FORWARD_SELECT_YOUR_CLIENT,
                new FundEvaluatorPageNavigationInfo("step1",
                        FundEvaluatorConstants.FORWARD_SELECT_YOUR_CLIENT,
                        FundEvaluatorConstants.FORWARD_SELECT_CRITERIA, null));
        pageNavigationInformation.put(FundEvaluatorConstants.FORWARD_SELECT_CRITERIA,
                new FundEvaluatorPageNavigationInfo("step2",
                        FundEvaluatorConstants.FORWARD_SELECT_CRITERIA,
                        FundEvaluatorConstants.FORWARD_NARROW_YOUR_LIST,
                        FundEvaluatorConstants.FORWARD_SELECT_YOUR_CLIENT));
        pageNavigationInformation.put(FundEvaluatorConstants.FORWARD_NARROW_YOUR_LIST,
                new FundEvaluatorPageNavigationInfo("step3",
                        FundEvaluatorConstants.FORWARD_NARROW_YOUR_LIST,
                        FundEvaluatorConstants.FORWARD_INVESTMENT_OPTIONS_SELECTION,
                        FundEvaluatorConstants.FORWARD_SELECT_CRITERIA));
        pageNavigationInformation.put(FundEvaluatorConstants.FORWARD_INVESTMENT_OPTIONS_SELECTION,
                new FundEvaluatorPageNavigationInfo("step4",
                        FundEvaluatorConstants.FORWARD_INVESTMENT_OPTIONS_SELECTION,
                        FundEvaluatorConstants.FORWARD_CUSTOMIZE_REPORT,
                        FundEvaluatorConstants.FORWARD_NARROW_YOUR_LIST));
        pageNavigationInformation.put(FundEvaluatorConstants.FORWARD_CUSTOMIZE_REPORT,
                new FundEvaluatorPageNavigationInfo("step5",
                        FundEvaluatorConstants.FORWARD_CUSTOMIZE_REPORT, 
                        FundEvaluatorConstants.NEXT_ACTION_INAPPLICABLE,
                        FundEvaluatorConstants.FORWARD_INVESTMENT_OPTIONS_SELECTION));
    }
    
    
    /**
     * Constructor.
     * 
     * @param clazz The class used to configure the logger.
     */
    public FundEvaluatorBaseController(final Class clazz) {
        super(clazz);
    }
    
    @Override    
    protected String preExecute( ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException, SystemException {

        super.preExecute( form, request, response);
        
        FundEvaluatorForm fundEvaluatorForm = (FundEvaluatorForm) form;
        String actionName = fundEvaluatorForm.getAction();
        String previousAction = fundEvaluatorForm.getPreviousAction();
        
      //SVFA: SVF & MMF competing funds are fetched from FundService instead of properties file
        if(CoreToolGlobalData.defaultSelectedStableValueFunds == null 
        		|| (CoreToolGlobalData.defaultSelectedStableValueFunds!= null && CoreToolGlobalData.defaultSelectedStableValueFunds.isEmpty())){
        	 List<String> defaultSelectedStableValueFunds = new ArrayList<String>();
        	 Map<String, List<String>> svfFunds;
        	 try {
    			svfFunds = FundServiceDelegate.getInstance().getStableValueFunds();
    			for(String companyId :svfFunds.keySet()){
    				defaultSelectedStableValueFunds.addAll(svfFunds.get(companyId));
    			}
    			CoreToolGlobalData.defaultSelectedStableValueFunds.addAll(defaultSelectedStableValueFunds);
    		} catch (SystemException e) {
    			throw new SystemException(e,"Exception occured while retriving the default SVF funds at :preExecute");
    		}
        	 
        }

        if(CoreToolGlobalData.defaultSelectedMoneyMarketFunds == null 
        		|| (CoreToolGlobalData.defaultSelectedMoneyMarketFunds!= null && CoreToolGlobalData.defaultSelectedMoneyMarketFunds.isEmpty())){
       	 List<String> defaultSelectedMoneyMarketFunds = new ArrayList<String>();
       	 Map<String, List<String>> mmfFunds;
       	 try {
   			mmfFunds = FundServiceDelegate.getInstance().getMoneyMarketFunds();
   			for(String companyId :mmfFunds.keySet()){
   				defaultSelectedMoneyMarketFunds.addAll(mmfFunds.get(companyId));
   			}
   			CoreToolGlobalData.defaultSelectedMoneyMarketFunds.addAll(defaultSelectedMoneyMarketFunds);
   		} catch (SystemException e) {
   			throw new SystemException(e,"Exception occured while retriving the default SVF funds at :preExecute");
   		}
       }
        if(StringUtils.isNotBlank(actionName)) { // if actionName is blank, base classes will take care
            FundEvaluatorPageNavigationInfo pageInfo = pageNavigationInformation.get(actionName);
            if(pageInfo != null) { // page navigations next/previous buttons will be handled
                if(StringUtils.equals(pageInfo.getPageAction(), actionName) &&
                        (StringUtils.equals(pageInfo.getNextAction(), previousAction) ||
                        StringUtils.equals(pageInfo.getPreviousAction(), previousAction))){
                    // Valid flow. Do Nothing
                } else {
                    // forward to home page if navigation is irregular
                    //return mapping.findForward("default");
                	return "default";
                }  
            }
        } 
        
        FundEvaluatorActionHelper.setContentLocation(fundEvaluatorForm, request);
        return null;
    }
    
    /**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 *//*
	@SuppressWarnings("rawtypes")
	public Collection doValidate(ActionMapping mapping, Form form, HttpServletRequest request) {
		Collection penErrors = FrwValidation.doValidatePenTestAutoAction(form, mapping, request, CommonConstants.DEFAULT);
		if (penErrors != null && penErrors.size() > 0) {
			FundEvaluatorForm fundEvaluatorForm = (FundEvaluatorForm) form;
			String actionName = fundEvaluatorForm.getAction();
			 FundEvaluatorPageNavigationInfo pageInfo = pageNavigationInformation.get(actionName);
			 request.getSession().setAttribute(CommonConstants.ERROR_RDRCT,  mapping.findForward(pageInfo == null ?FundEvaluatorConstants.FORWARD_CUSTOMIZE_REPORT:pageInfo.getPreviousAction())); 
			return penErrors;
		}
		return super.doValidate(mapping, form, request);
	}*/

}