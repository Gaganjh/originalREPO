package com.manulife.pension.bd.web.firmsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.content.ContentRuleMaintenanceForm;
import com.manulife.pension.bd.web.validation.pentest.FrwValidator;
import com.manulife.pension.delegate.BrokerServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.util.BaseSessionHelper;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.broker.valueobject.impl.BrokerDealerFirmImpl;
import com.manulife.pension.validator.ValidationError;

@Controller 
@RequestMapping(value ="/firmsearch")
@SessionAttributes({"contentRuleMaintenanceForm"})
public class FirmSearchController extends BaseAutoController {

	@ModelAttribute("contentRuleMaintenanceForm") 
	public ContentRuleMaintenanceForm populateForm()
	{
		return new ContentRuleMaintenanceForm();
		}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("searchResult","/WEB-INF/firmsearch/firmSearchResult.jsp");
	}
	

	public static final String SEARCH_RESULT_FORWARD = "searchResult";

	public static final Logger logger = Logger
			.getLogger(FirmSearchController.class);
	

	@RequestMapping(value ="/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("contentRuleMaintenanceForm") ContentRuleMaintenanceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

	
		// Do not proceed upon penetration violation, to be re-assessed later.
		//
		List errors = new ArrayList<ValidationError>();
        if (FrwValidator.getInstance().validateSanitizeCatalogedFormFields(actionForm, errors, request) == false) {
        	return forwards.get(SEARCH_RESULT_FORWARD);
        }
		
		try {
			
			List<? extends BrokerDealerFirm> matchingFirms = null;
			
			String firmType = actionForm.getFirmType();

			if (!StringUtils.isEmpty(firmType) && StringUtils.equals(firmType, "RIA")) {
				if(StringUtils.isNumeric(actionForm.getQuery())){
					matchingFirms = BrokerServiceDelegate
							.getInstance(BDConstants.BD_APPLICATION_ID)
							.findRIAFirmsById(actionForm.getQuery());
					sortFirmById(matchingFirms, actionForm
							.getQuery());
				}else{
					matchingFirms = BrokerServiceDelegate
							.getInstance(BDConstants.BD_APPLICATION_ID)
							.findRIAFirmsByName(actionForm.getQuery());
					sortFirmByName(matchingFirms, actionForm
							.getQuery());
				}
			} else {
				matchingFirms = BrokerServiceDelegate
				.getInstance(BDConstants.BD_APPLICATION_ID)
				.findBDFirmsByName(actionForm.getQuery());
				sortFirmByName(matchingFirms, actionForm
						.getQuery());
			}
			
			actionForm.setMatchingFirms(matchingFirms);
		} catch (Exception ex) {
			logger.error("Exception in firm search " + ex);
		}
		return forwards.get(SEARCH_RESULT_FORWARD);
	}

	/**
	 * This method is called when we need to get the List of Firms for a given
	 * RVP User.
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
	
	@RequestMapping(value ="/" ,params={"action=filter"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doFilter (@Valid @ModelAttribute("contentRuleMaintenanceForm") ContentRuleMaintenanceForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {


		try {
			
			List<? extends BrokerDealerFirm> matchingFirms = null;

			String rvpId = request.getParameter(BDConstants.RVP_ID);

			if (!StringUtils.isEmpty(rvpId)) {
				matchingFirms = BrokerServiceDelegate.getInstance(
						BDConstants.BD_APPLICATION_ID).findBDFirmsByNameForRVP(
								actionForm.getQuery(), Long.valueOf(rvpId));
                sortFirmByName(matchingFirms, actionForm.getQuery());
			} else {
				matchingFirms = BrokerServiceDelegate.getInstance(
						BDConstants.BD_APPLICATION_ID).findBDFirmsByName(
								actionForm.getQuery());
				sortFirmByName(matchingFirms, actionForm.getQuery());
			}

			actionForm.setMatchingFirms(matchingFirms);
		} catch (Exception ex) {
			logger.error("Exception in firm search " + ex);
		}
		return forwards.get(SEARCH_RESULT_FORWARD);
	}

    /**
     * This method is called when we need to validate the entered firm name
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
	
	@RequestMapping(value ="/", params={"action=verify"} , method =  {RequestMethod.POST}) 
	public String doVerify (@ModelAttribute("contentRuleMaintenanceForm") ContentRuleMaintenanceForm actionForm,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

	
   
    	//
    	// Re-verify for penetration detection.
    	//
    	Collection e = BaseSessionHelper.getErrorsInSession(request);
        if (e != null) {
        	BaseSessionHelper.removeErrorsInSession(request);
        }
		List errors = new ArrayList<ValidationError>();
        if (FrwValidator.getInstance().validateSanitizeCatalogedFormFields(actionForm, errors, request) == false) {
        	setErrorsInSession(request, errors);
        	//
        	// Pass in Json as: [{firmId:-1, firmName:"-1"}] to indicate penetration violation.
        	//
        	List<BrokerDealerFirm> matchingFirms = new ArrayList<BrokerDealerFirm>(1);
        	BrokerDealerFirmImpl firm = new BrokerDealerFirmImpl();
            firm.setId(-1);
            firm.setFirmName("-1");    
        	matchingFirms.add(firm);
        	
        	actionForm.setMatchingFirms(matchingFirms);
        	
        	return forwards.get(SEARCH_RESULT_FORWARD);
        }
        
        try {
        	ContentRuleMaintenanceForm firmSearchForm = (ContentRuleMaintenanceForm) actionForm;
            List<BrokerDealerFirm> matchingFirms = new ArrayList<BrokerDealerFirm>(1);

            String firmName = StringUtils.trimToEmpty(request
					.getParameter(BDConstants.FIRM_NAME_PARAM));
            BrokerDealerFirm firm = null;
       
            String firmType = firmSearchForm.getFirmType();
            if (!StringUtils.isEmpty(firmName)) {
				if (!StringUtils.isEmpty(firmType) && StringUtils.equals(firmType, "RIA")) {
					// TODO : Change the method signature to support only RIA
	                firm = (BrokerDealerFirmImpl) BrokerServiceDelegate.getInstance(
	                        BDConstants.BD_APPLICATION_ID).getRIAFirmByName(firmName);
	            } else {
	            	firm = (BrokerDealerFirmImpl) BrokerServiceDelegate.getInstance(
	                        BDConstants.BD_APPLICATION_ID).getBDFirmByName(firmName);
	            }
			}
            if (firm != null) {
                matchingFirms.add(firm);
            }
            firmSearchForm.setMatchingFirms(matchingFirms);
        } catch (Exception ex) {
            logger.error("Exception in firm search " + ex);
        }
        return forwards.get(SEARCH_RESULT_FORWARD);
    }
    
	// sort the firm by firm name, the name start with query stays before others
	private void sortFirmByName(List<? extends BrokerDealerFirm> matchingFirms,
			final String query) {
		final String upperQuery = StringUtils.upperCase(query);
		Collections.sort(matchingFirms, new Comparator<BrokerDealerFirm>() {

			public int compare(BrokerDealerFirm o1, BrokerDealerFirm o2) {
				String name1 = StringUtils.upperCase(StringUtils.trimToEmpty(o1
						.getFirmName()));
				String name2 = StringUtils.upperCase(StringUtils.trimToEmpty(o2
						.getFirmName()));
				return compareFirmName(name1, name2, upperQuery);
			}
		});
	}
	
	private int compareFirmName(String name1, String name2, String query) {
		boolean starts1 = (StringUtils.indexOf(name1, query) == 0);
		boolean starts2 = (StringUtils.indexOf(name2, query) == 0);
		// if both name start or not start with query, compare them
		if ((starts1 && starts2) || (!starts1 && !starts2)) {
			return name1.compareTo(name2);
		}
		// otherwise, the one starts with query wins
		if (starts1) {
			return -1;
		} else {
			return 1;
		}
	}
	
	// sort the firm by firm id, the name start with query stays before others
	private void sortFirmById(List<? extends BrokerDealerFirm> matchingFirms,
			final String query) {
		Collections.sort(matchingFirms, new Comparator<BrokerDealerFirm>() {

			public int compare(BrokerDealerFirm o1, BrokerDealerFirm o2) {
				long id1 = o1.getId();
				long id2 = o2.getId();
				return compareFirmId(id1, id2);
			}
		});
	}
	
	private int compareFirmId(long name1, long name2) {
			
		Long id1 = Long.valueOf(name1);
		Long id2 = Long.valueOf(name2);
			
		return id1.compareTo(id2);
			
	}
}
