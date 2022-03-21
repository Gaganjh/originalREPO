package com.manulife.pension.bd.web.content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.util.CompositeComparator;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWContentRule;
import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.view.MutableContent;
import com.manulife.pension.delegate.BDSystemSecurityServiceDelegate;
import com.manulife.pension.delegate.BrokerServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.security.bd.valueobject.ContentFirmRestrictionRule;
import com.manulife.pension.service.security.bd.valueobject.ContentFirmRestrictionRuleImpl;

/**
 * This is the Action class for BD Firm Restrictions Management page.
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping( value = "/firmRestrictions")

public class ContentRuleManagementController extends BaseAutoController {
	@ModelAttribute("contentRuleManagementForm") 
	public ContentRuleManagementForm populateForm()
	{
		return new ContentRuleManagementForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("contentRuleManagement","/content/contentRuleManagement.jsp");
		
		}
	


    public static String CONTENT_RULE_MANAGEMENT_FORMWARD = "contentRuleManagement";

    public static String SHOW_ASCENDING_CLASS = "sort_ascending";

    public static String SHOW_DESCENDING_CLASS = "sort_descending";

    public static String ASCENDING_ORDER = "asc";

    public static String DESCENDING_ORDER = "desc";

    public static String RULE_TYPE_PROPERTY = "ruleType";

    public static String CONTENT_ID_PROPERTY = "contentId";

    /**
     * Constructor
     */
    public ContentRuleManagementController() {
        super(ContentRuleManagementController.class);
    }

    @RequestMapping(value ="/management",  method =  {RequestMethod.GET}) 
    public String doDefault(@Valid @ModelAttribute("contentRuleManagementForm") ContentRuleManagementForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			forwards.get("contentRuleManagement");//if input forward not //available, provided default
    		}
    	}
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDefault");
        }
        try {
            ContentRuleManagementForm contentRuleManagementForm = (ContentRuleManagementForm) actionForm;
            List<Content> contents = new ArrayList<Content>();
            contents = Arrays.asList((BrowseServiceDelegate.getInstance())
                    .findContentsByRestrictionCode(BDConstants.BD_FIRM_RESTRICTION_CODE));

            Set<Integer> contentIds = new TreeSet<Integer>();
            Iterator<Content> iterator = contents.iterator();

            while (iterator.hasNext()) {
                Integer contentId = ((Content) iterator.next()).getKey();
                contentIds.add(contentId);
            }

            // Getting all restricted contents from CMA application
            Map<Integer, ContentFirmRestrictionRule> rules = BDSystemSecurityServiceDelegate
                    .getInstance().getContentRestrictionRules(contentIds);

            iterator = contents.iterator();
            List<ContentRuleDisplayBean> contentRules = new ArrayList<ContentRuleDisplayBean>();

            // Populating the Contnet Rule Display bean with information retrieved from CMA
            while (iterator.hasNext()) {
                MutableContent content = (MutableContent) (iterator.next());
                ContentRuleDisplayBean displayBean = new ContentRuleDisplayBean();
                displayBean.setContentId(content.getKey());
                displayBean.setName(content.getDisplayName());
                displayBean.setCategory(content.getCategory());
                if (rules != null) {
                    if (rules.containsKey(content.getKey())) {
                        ContentFirmRestrictionRuleImpl rule = (ContentFirmRestrictionRuleImpl) rules
                                .get(content.getKey());
                        displayBean.setRuleType(BDContentRuleHelper.getRuleTypeString(rule
                                .getRuleType()));
                        List<? extends BrokerDealerFirm> firms = BrokerServiceDelegate.getInstance(
                                BDConstants.BD_APPLICATION_ID).getBDFirmNamesByIds(
                                rule.getBrokerDealerFirmIds());
                        displayBean.setBrokerDealerFirms(firms);
                    } else {
                        displayBean.setRuleType(BDConstants.NO_RULE);
                    }
                }
                contentRules.add(displayBean);
            }

            // It is possible that there exists a rule where there is no matching restricted content
            // in the CMA. The reason can be either the restriction is removed for the content or
            // the content itself removed from CMA. In both cases we still have to show the Content
            // Rule in the BDW so that internal users can delete it. If the restriction is removed
            // then we can make a call to CMA to get the corresponding content info to populate the
            // display bean. If the content is removed from CMA then we can show an hypen for
            // the content info fields.
            
            // Getting all content rules
            Set<ContentFirmRestrictionRule> allRules = BDSystemSecurityServiceDelegate
                    .getInstance().getContentRestrictionRules();
            for (ContentFirmRestrictionRule rule : allRules) {
                ContentFirmRestrictionRuleImpl ruleImpl = (ContentFirmRestrictionRuleImpl) rule;
                // Filtering the contents without restriction
                if (!BDContentFirmRestrictionHelper.isRestrictedContentExists(rule.getContentId(),
                        contents)) {
                    MutableContent content = (MutableContent) BrowseServiceDelegate.getInstance()
                            .findContentByKey(rule.getContentId());
                    ContentRuleDisplayBean displayBean = new ContentRuleDisplayBean();
                    displayBean.setContentId(ruleImpl.getContentId());
                    if (content != null) {
                        displayBean.setName(content.getDisplayName());
                        displayBean.setCategory(content.getCategory());
                    } else { // content is removed from CMA
                        displayBean.setName(BDConstants.HYPHON_SYMBOL);
                        displayBean.setCategory(BDConstants.HYPHON_SYMBOL);
                    }
                    displayBean.setRuleType(BDContentRuleHelper.getRuleTypeString(ruleImpl
                            .getRuleType()));
                    contentRules.add(displayBean);
                }
            }

            // Default sort order. First by rule and then by content id.
            BeanComparator ruleComparator = new BeanComparator(RULE_TYPE_PROPERTY);
            BeanComparator contentIdComparator = new BeanComparator(CONTENT_ID_PROPERTY);
            CompositeComparator comparator = new CompositeComparator(ruleComparator,
                    contentIdComparator);
            Collections.sort(contentRules, comparator);

            contentRuleManagementForm.setContentIdSortOrder(ASCENDING_ORDER);
            contentRuleManagementForm.setRuleSortOrder(ASCENDING_ORDER);
            contentRuleManagementForm.setContentRules(contentRules);
        } catch (ContentException ce) {
            logger.error("Error in retrieving Contents with Firm Restrictions ", ce);
            throw new SystemException("Error in retrieving Contents with Firm Restrictions "
                    + ce.toString());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doDefault");
        }
        return forwards.get(CONTENT_RULE_MANAGEMENT_FORMWARD);
    }

    /**
     * This method sorts the content rules list based on the given criteria. When a column is
     * clicked for the first time it will be sorted on ascending order. Subsequent clicks in the
     * same column will result in reverse order. If an other column is clicked then the previous
     * column will be reset. If the previous column is clicked again it will be sorted in ascending
     * order (even if it is already in ascending order.)
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return ActionForward
     * @throws SystemException
     */
    @RequestMapping(value ="/management" ,params={"action=sort"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doSort (@Valid @ModelAttribute("contentRuleManagementForm") ContentRuleManagementForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
    		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
    		if(errDirect!=null){
    			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
    			forwards.get("contentRuleManagement");//if input forward not //available, provided default
    		}
    	}
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doSort");
        }
        try {
           
            List<Content> contents = new ArrayList<Content>();
            contents = Arrays.asList((BrowseServiceDelegate.getInstance())
                    .findContentsByRestrictionCode(BDConstants.BD_FIRM_RESTRICTION_CODE));

            Set<Integer> contentIds = new TreeSet<Integer>();
            Iterator<Content> iterator = contents.iterator();

            while (iterator.hasNext()) {
                Integer contentId = ((Content) iterator.next()).getKey();
                contentIds.add(contentId);
            }

            Map<Integer, ContentFirmRestrictionRule> rules = BDSystemSecurityServiceDelegate
                    .getInstance().getContentRestrictionRules(contentIds);

            iterator = contents.iterator();
            List<ContentRuleDisplayBean> contentRules = new ArrayList<ContentRuleDisplayBean>();
            List<ContentRuleDisplayBean> contentsWithNoRule = new ArrayList<ContentRuleDisplayBean>();
            List<ContentRuleDisplayBean> contentsWithRule = new ArrayList<ContentRuleDisplayBean>();

            while (iterator.hasNext()) {
                MutableContent content = (MutableContent) (iterator.next());
                ContentRuleDisplayBean displayBean = new ContentRuleDisplayBean();
                displayBean.setContentId(content.getKey());
                displayBean.setName(content.getDisplayName());
                displayBean.setCategory(content.getCategory());
                if (rules != null) {
                    if (rules.containsKey(content.getKey())) {
                        ContentFirmRestrictionRuleImpl rule = (ContentFirmRestrictionRuleImpl) rules
                                .get(content.getKey());
                        displayBean.setRuleType(BDContentRuleHelper.getRuleTypeString(rule
                                .getRuleType()));
                        List<? extends BrokerDealerFirm> firms = BrokerServiceDelegate.getInstance(
                                BDConstants.BD_APPLICATION_ID).getBDFirmNamesByIds(
                                rule.getBrokerDealerFirmIds());
                        displayBean.setBrokerDealerFirms(firms);
                    } else {
                        displayBean.setRuleType(BDConstants.NO_RULE);
                    }
                }
                if (BDConstants.NO_RULE.equals(displayBean.getRuleType())) {
                    contentsWithNoRule.add(displayBean);
                } else {
                    contentsWithRule.add(displayBean);
                }
            }
            
            // It is possible that there exists a rule where there is no matching restricted content
            // in the CMA. The reason can be either the restriction is removed for the content or
            // the content itself removed from CMA. In both cases we still have to show the Content
            // Rule in the BDW so that internal users can delete it. If the restriction is removed
            // then we can make a call to CMA to get the corresponding content info to populate the
            // display bean. If the content is removed from CMA then we can show an hypen for
            // the content info fields.
            
            // Getting all content rules
            Set<ContentFirmRestrictionRule> allRules = BDSystemSecurityServiceDelegate
                    .getInstance().getContentRestrictionRules();
            for (ContentFirmRestrictionRule rule : allRules) {
                ContentFirmRestrictionRuleImpl ruleImpl = (ContentFirmRestrictionRuleImpl) rule;
                // Filtering the contents without restriction
                if (!BDContentFirmRestrictionHelper.isRestrictedContentExists(rule.getContentId(),
                        contents)) {
                    MutableContent content = (MutableContent) BrowseServiceDelegate.getInstance()
                            .findContentByKey(rule.getContentId());
                    ContentRuleDisplayBean displayBean = new ContentRuleDisplayBean();
                    displayBean.setContentId(ruleImpl.getContentId());
                    if (content != null) {
                        displayBean.setName(content.getDisplayName());
                        displayBean.setCategory(content.getCategory());
                    } else { // content is removed from CMA
                        displayBean.setName(BDConstants.HYPHON_SYMBOL);
                        displayBean.setCategory(BDConstants.HYPHON_SYMBOL);
                    }
                    displayBean.setRuleType(BDContentRuleHelper.getRuleTypeString(ruleImpl
                            .getRuleType()));
                    contentsWithRule.add(displayBean);
                }
            }            
            BeanComparator comparator = new BeanComparator(actionForm
                    .getSortProperty());
            ReverseComparator reverseComparator = new ReverseComparator(new BeanComparator(
            		actionForm.getSortProperty()));
            if (CONTENT_ID_PROPERTY.equals(actionForm.getSortProperty())) {
                // Resetting the next sort order of rule column
            	actionForm.setRuleSortOrder(ASCENDING_ORDER);
                if (ASCENDING_ORDER.equals(actionForm.getContentIdSortOrder())) {
                    // to show up arrow
                	actionForm.setSortClass(SHOW_ASCENDING_CLASS);
                    // next sort order for content id column
                	actionForm.setContentIdSortOrder(DESCENDING_ORDER);
                    Collections.sort(contentsWithRule, comparator);
                    Collections.sort(contentsWithNoRule, comparator);
                } else if (DESCENDING_ORDER.equals(actionForm
                        .getContentIdSortOrder())) {
                    // to show down arrow
                	actionForm.setSortClass(SHOW_DESCENDING_CLASS);
                    // next sort order for content id column
                	actionForm.setContentIdSortOrder(ASCENDING_ORDER);
                    Collections.sort(contentsWithRule, reverseComparator);
                    Collections.sort(contentsWithNoRule, reverseComparator);
                }
            }

            if (RULE_TYPE_PROPERTY.equals(actionForm.getSortProperty())) {
                // Resetting the next sort order of content id column
            	actionForm.setContentIdSortOrder(ASCENDING_ORDER);
                if (ASCENDING_ORDER.equals(actionForm.getRuleSortOrder())) {
                    // to show up arrow
                	actionForm.setSortClass(SHOW_ASCENDING_CLASS);
                    // next sort order for rule column
                    actionForm.setRuleSortOrder(DESCENDING_ORDER);
                    Collections.sort(contentsWithRule, comparator);
                } else if (DESCENDING_ORDER.equals(actionForm.getRuleSortOrder())) {
                    // to show down arrow
                	actionForm.setSortClass(SHOW_DESCENDING_CLASS);
                    // next sort order for rule column
                	actionForm.setRuleSortOrder(ASCENDING_ORDER);
                    Collections.sort(contentsWithRule, reverseComparator);
                }
            }

            // Contents with no rule should always come first.
            contentRules.addAll(contentsWithNoRule);
            contentRules.addAll(contentsWithRule);
            actionForm.setContentRules(contentRules);
        } catch (ContentException ce) {
            logger.error("Error in retrieving Contents with Firm Restrictions in doSort ", ce);
            throw new SystemException(
                    "Error in retrieving Contents with Firm Restrictions in doSort "
                            + ce.toString());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> doSort");
        }
        return forwards.get(CONTENT_RULE_MANAGEMENT_FORMWARD);
	}

	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */   
	@Autowired
	private BDValidatorFWContentRule bdValidatorFWContentRule;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWContentRule);
	}
	
}
