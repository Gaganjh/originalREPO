package com.manulife.pension.bd.web.home;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BlockOfBusinessUtility;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.content.BDContentFirmRestrictionHelper;
import com.manulife.pension.bd.web.controller.AuthorizationSubject;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.controller.BDAuthorizationSubject;
import com.manulife.pension.bd.web.controller.SecurityManager;
import com.manulife.pension.bd.web.messagecenter.BDMessageCenterFacade;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWError;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.security.BDUserRoleType;

/**
 * This is the action class for Secure Home Page
 * 
 * @author Ilamparithi
 *
 */
@Controller
@RequestMapping(value ="/secured")

public class SecuredHomePageController extends BDController {
	@ModelAttribute("securedHomePageForm") 
	public SecuredHomePageForm populateForm()
	{
		return new SecuredHomePageForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("page","/home/home.jsp");
		forwards.put("error","/home/public_home.jsp");
		}

    private static EnumMap<BDUserRoleType, Integer> LAYER_ID_MAP = new EnumMap<BDUserRoleType, Integer>(
            BDUserRoleType.class);
    static {
        LAYER_ID_MAP.put(BDUserRoleType.BasicFinancialRep,
                BDContentConstants.LAYER_ID_FOR_BASIC_BROKER);
        LAYER_ID_MAP.put(BDUserRoleType.FirmRep, BDContentConstants.LAYER_ID_FOR_FIRM_REP);
        LAYER_ID_MAP.put(BDUserRoleType.FinancialRep, BDContentConstants.LAYER_ID_FOR_BROKER);
        LAYER_ID_MAP.put(BDUserRoleType.FinancialRepAssistant,
                BDContentConstants.LAYER_ID_FOR_BROKER);
        LAYER_ID_MAP.put(BDUserRoleType.RIAUser,
                BDContentConstants.LAYER_ID_FOR_RIAUSER);
    }

    /**
     * Constructor
     */
    public SecuredHomePageController() {
        super(SecuredHomePageController.class);
    }

    /**
     * Override execute method for this action 
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @RequestMapping(value ="/",  method =  {RequestMethod.GET}) 
    public String doExecute(@Valid @ModelAttribute("securedHomePageForm") SecuredHomePageForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			forwards.get("error");//if input forward not //available, provided default
    		}
    	}
        BDUserProfile profile = BDSessionHelper.getUserProfile(request);
        
        BDUserRoleType roleType = profile.getBDPrincipal().getBDUserRole().getRoleType();
        String roleId = roleType.getRoleId();
        // For external user
        if (!profile.isInternalUser()) {
        	actionForm.setLayerId(LAYER_ID_MAP.get(roleType));
        	actionForm.setShowMyBOB(HomePageHelper.isSectionAllowed(roleId,
                    HomePageHelper.MY_BOB_SECTION));
        	actionForm.setShowMessageCenter(HomePageHelper.isSectionAllowed(roleId,
                    HomePageHelper.MESSAGE_CENTER_SECTION));
        	actionForm.setShowBOBSetup(HomePageHelper.isSectionAllowed(roleId,
                    HomePageHelper.BOB_SETUP_SECTION));
            // For internal user
        } else {
        	actionForm
                    .setLayerId(BDContentConstants.LAYER_ID_FOR_INTERNAL_USER_NOT_IN_MIMIC);
        	actionForm.setShowMyBOB(HomePageHelper.isSectionAllowed(roleId,
                    HomePageHelper.MY_BOB_SECTION));
        	actionForm.setShowMessageCenter(HomePageHelper.isSectionAllowed(roleId,
                    HomePageHelper.MESSAGE_CENTER_SECTION));
        	actionForm.setShowBOBSetup(HomePageHelper.isSectionAllowed(roleId,
                    HomePageHelper.BOB_SETUP_SECTION));
        }
        actionForm.setShowIeval(showIevalLink(profile, request.getServletContext()));
        try {
            if (actionForm.isAllWhatsNewContents()) {
            	actionForm.setWhatsNewContents(BDContentFirmRestrictionHelper
                        .getAllAllowedContents(profile, BDContentConstants.BD_WHATS_NEW_GROUP));
            } else {
            	actionForm.setWhatsNewContents(BDContentFirmRestrictionHelper
                        .getCurrentAllowedContents(profile, BDContentConstants.BD_WHATS_NEW_GROUP));
            }
            if (BDContentFirmRestrictionHelper.getAllAllowedContents(profile,
                    BDContentConstants.BD_WHATS_NEW_GROUP).size() > BDContentFirmRestrictionHelper
                    .getCurrentAllowedContents(profile, BDContentConstants.BD_WHATS_NEW_GROUP).size()) {
            	actionForm.setShowWhatsNewLink(true);
            }
            actionForm.setMarketingCommentaryContents(BDContentFirmRestrictionHelper
                    .getCurrentAllowedContents(profile, BDContentConstants.BD_MARKETING_COMMENTARY_GROUP));
            actionForm.setShowMarketingCommentary(actionForm
                    .getMarketingCommentaryContents().size() > 0);
        } catch (Exception e) {
        	logger.error("Unexpected exception for what's new section", e);
        }
        try {
			if (HomePageHelper.isSectionAllowed(roleId,
					HomePageHelper.MESSAGE_CENTER_SECTION)) {
				actionForm
						.setMessageCenterSummary(BDMessageCenterFacade
								.getInstance().getMesageCenterSummary(profile));
				actionForm.setEnableMCPreferencesLink((profile
						.isInMimic()) ? false : true);
			}
		} catch (Exception e) {
			logger.error("Unexpected exception for message center section", e);
		}
		try {
			if (HomePageHelper.isSectionAllowed(roleId,
					HomePageHelper.MY_BOB_SECTION)) {
				BDUserProfile mimickingUserProfile = BlockOfBusinessUtility
						.getMimckingUserProfile(request);
				ReportCriteria criteria = BlockOfBusinessUtility
						.populateReportCriteria(profile, mimickingUserProfile,
								null, "AC", BlockOfBusinessUtility.getDBSessionIDForDefaultAsOfDate(request));
				actionForm.setMyBOBSummary(BlockOfBusinessUtility
								.callBOBStoredProc(criteria, request).getBobSummaryVO());
			}
		} catch (Exception e) {
			logger.error("Unexpected exception for BOB section", e);
		}
        return forwards.get("page");
    }

    /**
     * Returns a flag to indicate whether to show the I:Evaluator link (of Quick Links section) to the
     * given user
     * 
     * @param profile
     * @return boolean
     */
    private boolean showIevalLink(BDUserProfile profile,ServletContext servlet) {
        boolean showIevalLink = false;
        try {
			SecurityManager securityManager = ApplicationHelper
							.getSecurityManager(servlet);
			AuthorizationSubject s = new BDAuthorizationSubject();
			s.setUserProfile(profile);
			showIevalLink = securityManager.isUserAuthorized(s, URLConstants.FundEvaluator);
		} catch (Exception e) {
			logger.error("Unexecpted exception", e);
		}
        return showIevalLink;
	}

	/**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 */
    @Autowired
	   private BDValidatorFWError  bdValidatorFWError;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWError);
}
}
