package com.manulife.pension.bd.web.content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.navigation.URLConstants;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWContentRuleMaintanence;
import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.view.MutableContent;
import com.manulife.pension.delegate.BDSystemSecurityServiceDelegate;
import com.manulife.pension.delegate.BrokerServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerRedirect;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.broker.valueobject.impl.BrokerDealerFirmImpl;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.bd.valueobject.ContentFirmRestrictionRule;
import com.manulife.pension.service.security.bd.valueobject.ContentFirmRestrictionRuleImpl;
import com.manulife.pension.service.security.bd.valueobject.ContentFirmRestrictionRuleImpl.RuleType;

/**
 * This is the Action class for BD Firm Maintenance page
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping( value = "/firmRestrictions")
@SessionAttributes({"contentRuleMaintenanceForm"})
public class ContentRuleMaintenanceController extends BaseAutoController {
	@ModelAttribute("contentRuleMaintenanceForm") 
	public ContentRuleMaintenanceForm populateForm()
	{
		return new ContentRuleMaintenanceForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("contentRuleMaintenance","/content/contentRuleMaintenance.jsp");
		forwards.put("contentRuleView","/content/contentRule.jsp" );
		forwards.put("contentRuleManagement","redirect:/do/firmRestrictions/management");
		}

    public static String CONTENT_RULE_MAINTENANCE_FORMWARD = "contentRuleMaintenance";

    public static String CONTENT_RULE_MAINTENANCE_FROM_CANCEL_FORMWARD = "contentRuleMaintenanceFromCancel";

    public static String CONTENT_RULE_VIEW_FORMWARD = "contentRuleView";

    public static String CONTENT_RULE_MANAGEMENT_FORWARD = "contentRuleManagement";

    /**
     * Constructor
     */
    public ContentRuleMaintenanceController() {
        super(ContentRuleMaintenanceController.class);
    }

    @RequestMapping(value ="/maintenance",  method =  {RequestMethod.GET}) 
    public String doDefault(@Valid @ModelAttribute("contentRuleMaintenanceForm") ContentRuleMaintenanceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    		return	forwards.get("contentRuleMaintenance");//if input forward not //available, provided default
    		}
    	}
    
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDefault");
        }
        ContentRuleMaintenanceForm contentRuleMaintenanceForm = (ContentRuleMaintenanceForm) actionForm;
        //Irregular navigation (like invalid bookmark)
        if (StringUtils.isEmpty(contentRuleMaintenanceForm.getTask())
                || contentRuleMaintenanceForm.getContentId() == 0) {
            return forwards.get(CONTENT_RULE_MANAGEMENT_FORWARD);
        }
        try {

            Set<Integer> contentIds = new HashSet<Integer>(1);
            contentIds.add(contentRuleMaintenanceForm.getContentId());

            Map<Integer, ContentFirmRestrictionRule> rules = new HashMap<Integer, ContentFirmRestrictionRule>();
			rules = BDSystemSecurityServiceDelegate.getInstance()
					.getContentRestrictionRules(contentIds);

            MutableContent content = (MutableContent) BrowseServiceDelegate.getInstance()
                    .findContentByKey(contentRuleMaintenanceForm.getContentId());

            ContentRuleDisplayBean displayBean = new ContentRuleDisplayBean();
            displayBean.setContentId(contentRuleMaintenanceForm.getContentId());
            if (content != null) {
                displayBean.setName(content.getDisplayName());
                displayBean.setCategory(content.getCategory());
            } else { // content is removed from CMA
                displayBean.setName(BDConstants.HYPHON_SYMBOL);
                displayBean.setCategory(BDConstants.HYPHON_SYMBOL);
            }
            if (rules != null) {
                if (rules.containsKey(contentRuleMaintenanceForm.getContentId())) {
                    ContentFirmRestrictionRuleImpl rule = (ContentFirmRestrictionRuleImpl) rules
                            .get(contentRuleMaintenanceForm.getContentId());
                    displayBean.setRuleType(BDContentRuleHelper.getRuleTypeString(rule
                            .getRuleType()));
                    List<? extends BrokerDealerFirm> firms = new ArrayList<BrokerDealerFirmImpl>();
                    // When redirected from Cancel, firm ids can be null. So we have to
                    // handle that condition here but it is not necessary for view since a
                    // rule cannot exist without firms
                    if (rule.getBrokerDealerFirmIds() != null
                            && rule.getBrokerDealerFirmIds().size() > 0) {
                        firms = BrokerServiceDelegate.getInstance(BDConstants.BD_APPLICATION_ID)
                                .getBDFirmNamesByIds(rule.getBrokerDealerFirmIds());
                    }
                    displayBean.setBrokerDealerFirms(firms);
                } else {
                    displayBean.setRuleType(BDConstants.NO_RULE);
                }
            }
            contentRuleMaintenanceForm.setContentRule(displayBean);

        } catch (Exception se) {
            logger.error("Exception while retrieving the content rule. Content Id: "
                    + contentRuleMaintenanceForm.getContentId(), se);
            throw new SystemException("Exception while retrieving the content rule. Content Id: "
                    + contentRuleMaintenanceForm.getContentId() + se.toString());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doDefault");
        }
        return forwards.get(CONTENT_RULE_MAINTENANCE_FORMWARD);
    }

    /**
     * This method saves the changes made to the content rule.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     * @throws SystemException
     */
    
    @RequestMapping(value ="/maintenance" ,params={"action=save"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doSave (@Valid @ModelAttribute("contentRuleMaintenanceForm") ContentRuleMaintenanceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws  SystemException {

    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    		return	forwards.get("contentRuleMaintenance");//if input forward not //available, provided default
    		}
    	}
    
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doSave");
        }
      //  ContentRuleMaintenanceForm contentRuleMaintenanceForm = (ContentRuleMaintenanceForm) actionForm;
        // If rule type is empty hat means all firms are removed and rule is deleted. So the next
        // task should be add (i.e. new database insert)
        RuleType ruleType = BDContentRuleHelper.getRuleType(actionForm.getRuleType());
        String task = (ruleType == null)?"add":"edit";
        String redirect = "redirect:"+URLConstants.ContentRuleMaintenance + "?contentId="
                + actionForm.getContentId() + "&task=" + task;
        try {
            saveRule(actionForm, request);
        } catch (Exception se) {
            logger.error("Exception while saving the content rule for Save action. Content Id: "
                    + actionForm.getContentId() + se.toString(), se);
            throw new SystemException(
                    "Exception while saving the content rule for Save action. Content Id: "
                            + actionForm.getContentId() + se.toString());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doSave");
        }
        actionForm.setSelectedFirmName(""); 
        return (redirect);
    }

    /**
     * This method saves the changes made to the content rule and then return the control to the
     * Management page
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     * @throws SystemException
     */
    
    @RequestMapping(value ="/maintenance", params={"action=saveExit"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doSaveExit (@Valid @ModelAttribute("contentRuleMaintenanceForm") ContentRuleMaintenanceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws  SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    		return	forwards.get("contentRuleMaintenance");//if input forward not //available, provided default
    		}
    	}
    
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doSaveExit");
        }
        
        try {
            saveRule(actionForm, request);
        } catch (Exception se) {
            logger.error(
                    "Exception while saving the content rule for Save and Exit action. Content Id: "
                            + actionForm.getContentId() + se.toString(), se);
            throw new SystemException(
                    "Exception while saving the content rule for Save and Exit action. Content Id: "
                            + actionForm.getContentId() + se.toString());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doSaveExit");
        }
        return forwards.get(CONTENT_RULE_MANAGEMENT_FORWARD);
    }

    /**
     * This method is triggered when the user clicks the Cancel button. If no changes were made user
     * will be forwarded to content rule management page otherwise user will be again forwarded to
     * content rule maintenance page.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     * @throws SystemException
     */
    
    @RequestMapping(value ="/maintenance", params={"action=cancel"}  , method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doCancel (@Valid @ModelAttribute("contentRuleMaintenanceForm") ContentRuleMaintenanceForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws  SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    		return	forwards.get("contentRuleMaintenance");//if input forward not //available, provided default
    		}
    	}
    	form.setSelectedFirmName(""); 
        String redirect = "redirect:"+URLConstants.ContentRuleManagement;
        return (redirect);
    }

    /**
     * This method retrieves the stored content rule from database for view
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     * @throws SystemException
     */
    
    @RequestMapping(value ="/maintenance", params={"action=view"}, method =  {RequestMethod.GET}) 
    public String doView (@Valid @ModelAttribute("contentRuleMaintenanceForm") ContentRuleMaintenanceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			return forwards.get("contentRuleMaintenance");//if input forward not //available, provided default
    		}
    	}
    
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doView");
        }
       
        try {
            Set<Integer> contentIds = new TreeSet<Integer>();
            contentIds.add(actionForm.getContentId());

            Map<Integer, ContentFirmRestrictionRule> rules = BDSystemSecurityServiceDelegate
                    .getInstance().getContentRestrictionRules(contentIds);

            MutableContent content = (MutableContent) BrowseServiceDelegate.getInstance()
                    .findContentByKey(actionForm.getContentId());

            ContentRuleDisplayBean displayBean = new ContentRuleDisplayBean();
            displayBean.setContentId(actionForm.getContentId());
            if (content != null) {
                displayBean.setName(content.getDisplayName());
                displayBean.setCategory(content.getCategory());
            } else { // content is removed from CMA
                displayBean.setName(BDConstants.HYPHON_SYMBOL);
                displayBean.setCategory(BDConstants.HYPHON_SYMBOL);
            }
            if (rules != null) {
                if (rules.containsKey(actionForm.getContentId())) {
                    ContentFirmRestrictionRuleImpl rule = (ContentFirmRestrictionRuleImpl) rules
                            .get(actionForm.getContentId());
                    displayBean.setRuleType(BDContentRuleHelper.getRuleTypeString(rule
                            .getRuleType()));
                    List<? extends BrokerDealerFirm> firms = BrokerServiceDelegate.getInstance(
                            BDConstants.BD_APPLICATION_ID).getBDFirmNamesByIds(
                            rule.getBrokerDealerFirmIds());
                    displayBean.setBrokerDealerFirms(firms);
                } else {
                    // User is coming from a book mark
                    
                    		ControllerRedirect f= new ControllerRedirect(URLConstants.HomeURL);
                    		return f.getPath();
                }
            }
            actionForm.setContentRule(displayBean);

        } catch (Exception se) {
            logger.error("Exception while retrieving the content rule. Content Id: "
                    + actionForm.getContentId(), se);
            throw new SystemException("Exception while retrieving the content rule. Content Id: "
                    + actionForm.getContentId() + se.toString());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doView");
        }
        return forwards.get(CONTENT_RULE_VIEW_FORMWARD);
    }

    /**
     * This method contains the save logic to store the content rule to database
     * 
     * @param actionForm
     * @param request
     * @throws SystemException
     */
    private void saveRule(AutoForm actionForm, HttpServletRequest request)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> saveRule");
        }
        if (hasContentRuleChanged(actionForm, request)) {
            ContentRuleMaintenanceForm contentRuleMaintenanceForm = (ContentRuleMaintenanceForm) actionForm;
            ContentFirmRestrictionRuleImpl contentRule = new ContentFirmRestrictionRuleImpl();
            contentRule.setContentId(contentRuleMaintenanceForm.getContentId());
            if (!StringUtils.isEmpty(contentRuleMaintenanceForm.getRuleType())) {
                contentRule.setRuleType(BDContentRuleHelper.getRuleType(contentRuleMaintenanceForm
                        .getRuleType()));
            } else {
                contentRule.setRuleType(null);
            }
            BDPrincipal principal = BDSessionHelper.getUserProfile(request).getBDPrincipal();
            contentRule.setBrokerDealerFirmIds(BDContentRuleHelper
                    .getFirmIdsSet(contentRuleMaintenanceForm.getFirmListStr()));
            BDSystemSecurityServiceDelegate.getInstance().saveContentRestrictionRule(principal,
                    contentRule, contentRuleMaintenanceForm.getTask());
            if (logger.isDebugEnabled()) {
                logger.debug("exit -> saveRule");
            }
        }
    }

    /**
     * This method tests whether the rule has been modified or not.
     * 
     * @param actionForm
     * @param request
     * @return boolean a boolean to indicate whether the rule has been modified or not.
     * @throws SystemException
     */
    private boolean hasContentRuleChanged(AutoForm actionForm, HttpServletRequest request)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> hasContentRuleChanged");
        }
        boolean hasContentRuleChanged = false;
        ContentRuleMaintenanceForm contentRuleMaintenanceForm = (ContentRuleMaintenanceForm) actionForm;
        Set<Integer> contentIds = new TreeSet<Integer>();
        contentIds.add(contentRuleMaintenanceForm.getContentId());

        Map<Integer, ContentFirmRestrictionRule> rules = BDSystemSecurityServiceDelegate
                .getInstance().getContentRestrictionRules(contentIds);

        ContentFirmRestrictionRuleImpl rule = (ContentFirmRestrictionRuleImpl) rules
                .get(contentRuleMaintenanceForm.getContentId());
        if (rule != null) {
            Set<Long> existingFirmIds = rule.getBrokerDealerFirmIds();
            String ruleType = BDContentRuleHelper.getRuleTypeString(rule.getRuleType());
            Set<Long> latestFirmIds = BDContentRuleHelper.getFirmIdsSet(contentRuleMaintenanceForm
                    .getFirmListStr());
            if (!ruleType.equals(contentRuleMaintenanceForm.getRuleType())
                    || !existingFirmIds.equals(latestFirmIds)) {
                hasContentRuleChanged = true;
            }
        } else {
            if (!StringUtils.isEmpty(contentRuleMaintenanceForm.getRuleType())
                    || !StringUtils.isEmpty(contentRuleMaintenanceForm.getFirmListStr())) {
                hasContentRuleChanged = true;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> hasContentRuleChanged");
        }
        return hasContentRuleChanged;
	}

	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
    
    @Autowired
	   private BDValidatorFWContentRuleMaintanence  bdValidatorFWContentRuleMaintanence;

    @InitBinder
    public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWContentRuleMaintanence);
	}
	
	
	
}
