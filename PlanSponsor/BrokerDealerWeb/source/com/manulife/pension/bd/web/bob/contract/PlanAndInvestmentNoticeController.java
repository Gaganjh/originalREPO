package com.manulife.pension.bd.web.bob.contract;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.Access404a5;
import com.manulife.pension.service.fund.valueobject.Access404a5.Facility;
import com.manulife.pension.service.fund.valueobject.Access404a5.MissingInformation;
import com.manulife.pension.service.fund.valueobject.Access404a5.Qualification;
import com.manulife.pension.service.fund.valueobject.FeeDisclosureUserDetails;
import com.manulife.pension.service.fund.valueobject.UserAccess;

@Controller
@RequestMapping(value ="/bob")

public class PlanAndInvestmentNoticeController extends ICCReportController {
	@ModelAttribute("autoForm")
	public AutoForm populateForm() 
	{
		return new AutoForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/bob/blockOfBusinessActive.jsp");
		}

    public PlanAndInvestmentNoticeController() { super(PlanAndInvestmentNoticeController.class); }
    
    @Override
    boolean isDocumentAllowed(Access404a5 access) {
        
        Qualification piNoticeQual = access.getAccess(Facility._404A5_PLAN_AND_INVESTMENT_NOTICE);
        return
                piNoticeQual != null
                && ! piNoticeQual.getTemporarilyMissingInformation().contains(MissingInformation.ICC_CONTACT);
        
    }
    
    @Override
    String getPdfSessionCacheAttributeName() { return "generatedPlanAndInvestmentNoticeArray"; }
    
    @Override
    String getPdfFileName(int contractId) { return "PN_" + contractId + ".pdf"; }
    
    @Override
    byte[] getPdfFileStream(
            int contractId,
            String topLogoPath,
            String bottomLogoPath,
            FeeDisclosureUserDetails context, UserAccess user)
    throws Exception {
        return FundServiceDelegate.getInstance().getPlanAndInvestmentNoticeFileStream(
                contractId,
                topLogoPath,
                bottomLogoPath,
                context, user);
    }
    @RequestMapping(value ="/investment/planAndInvestmentNotice/",  method =  {RequestMethod.GET}) 
    public String doDefault (@Valid @ModelAttribute("autoForm") AutoForm form,ModelMap model, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    		throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		request.getSession().setAttribute(CommonConstants.ERROR_RDRCT,forwards.get(BDConstants.BOB_PAGE_FORWARD));
    		return forwards.get(BDConstants.BOB_PAGE_FORWARD);
    	}
    	String forward=super.doDefault( form,model, request, response);
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
    @RequestMapping(value ="/investment/planAndInvestmentNotice/",params={"action=checkPdfReportGenerated"}   , method =  {RequestMethod.GET}) 
    public String doCheckPdfReportGenerated (@Valid @ModelAttribute("autoForm") AutoForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		request.getSession().setAttribute(CommonConstants.ERROR_RDRCT,forwards.get(BDConstants.BOB_PAGE_FORWARD));
    		return forwards.get(BDConstants.BOB_PAGE_FORWARD);
    	}
    	String forward=super.doCheckPdfReportGenerated( form, request, response);
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
    @RequestMapping(value ="/investment/planAndInvestmentNotice/" ,params={"action=openErrorPdf"}, method =  {RequestMethod.GET}) 
    public String doOpenErrorPdf (@Valid @ModelAttribute("autoForm") AutoForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		request.getSession().setAttribute(CommonConstants.ERROR_RDRCT,forwards.get(BDConstants.BOB_PAGE_FORWARD));
    		return forwards.get(BDConstants.BOB_PAGE_FORWARD);
    	}
    	String forward=super.doOpenErrorPdf( form, request, response);
    	return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
    }
    /**
	 * This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	/**
	 * This method calls doValidate for doing validation.
	 * 
	 * @param form
	 *            Form objects reference
	 * @param request
	 *            HttpServletRequest objects reference
	 * 
	 * @return Collection of errors
	 */
    @Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}
    
    
}
