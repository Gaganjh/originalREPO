package com.manulife.pension.ps.web.plandata;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * Action class to handle the Select TPA Firm page for TPA Users associated with
 * multiple TPA firm ID's
 * 
 * @author Dheepa Poongol
 * 
 */
@Controller
@RequestMapping(value="/plandata")

public class TpaPlanDataController extends BaseAutoController {
	@ModelAttribute("selectTpaFirmForPlanDataForm") 
	public SelectTpaFirmForPlanDataForm populateForm() 
	{
		return new SelectTpaFirmForPlanDataForm();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("default","/plandata/selectTpaFirmForPlanData.jsp");
		forwards.put("goToCustomizeContractSearchPage","redirect:/do/plandata/contractSearch/");
		}

	
	private static final String TPA_STANDARD_FEE_SCHEDULE_VIEW_PAGE = "view";
	private static final String TPA_CUSTOMIZE_CONTRACT_SEARCH_PAGE = "goToCustomizeContractSearchPage";

	public TpaPlanDataController() {
		super(TpaPlanDataController.class);
	}

	/**
	 * Method to go to the Select TPA firm page or view page based upon certain
	 * permissions to the User
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 * 
	 */
	@RequestMapping(value="/viewTpaPlanDataContractsView/", method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("selectTpaFirmForPlanDataForm") SelectTpaFirmForPlanDataForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		Map<Integer, String> tpaFirmMap=new TreeMap<Integer, String>();
        String companyCode = Constants.COMPANY_NAME_NY.equalsIgnoreCase(Environment.getInstance().getSiteLocation()) 
        		? Constants.COMPANY_ID_NY : Constants.COMPANY_ID_US;
		
		//For Internal Users only
		if (userProfile.getRole().isInternalUser()) {
			UserInfo userInfo = (UserInfo) request.getSession(false)
					.getAttribute(Constants.TPA_USER_INFO);
			if(userInfo!=null)
			{
			tpaFirmMap = TPAServiceDelegate.getInstance()
					.retrieveTpaFirmsByTPAUserProfileId(userInfo.getProfileId(), companyCode);
			}else{
				return Constants.HOMEPAGE_FINDER_FORWARD;
			}
				
		} 
		else {
				// For other users
			tpaFirmMap = TPAServiceDelegate.getInstance()
			.retrieveTpaFirmsByTPAUserProfileId(
							userProfile.getPrincipal().getProfileId(), companyCode);
		}
		
		// no access if no TPA firms
		if(tpaFirmMap.isEmpty()) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		Integer tpaFirmId = null;
		
		if (tpaFirmMap.size() == 1) {

			for (Map.Entry<Integer, String> tpaFirm : tpaFirmMap.entrySet()) {
				tpaFirmId = tpaFirm.getKey();
			}
		}

		// checks for User with single TPA firm ID
		if (tpaFirmId != null) {
			
		    request.getSession().setAttribute(
	                Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID,
	                String.valueOf(tpaFirmId));

	        return forwards.get( TPA_CUSTOMIZE_CONTRACT_SEARCH_PAGE);
				
		}		
		request.setAttribute("tpaFirmMap", tpaFirmMap);		
		
		return forwards.get("default");
	}
	
	
	/**
     * Method to go to the tpa standard page
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws SystemException
     * 
     */
	@RequestMapping(value="/viewTpaPlanDataContractsView/",params= {"action=standardize"}, method =  {RequestMethod.POST})
	public String doStandardize(@Valid @ModelAttribute("selectTpaFirmForPlanDataForm") SelectTpaFirmForPlanDataForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
        
		UserProfile userProfile = SessionHelper.getUserProfile(request);
        Integer tpaFirmId = Integer.valueOf(form.getSelectedTpaFirmId());
        
        if(tpaFirmId!=null){            
            request.getSession().setAttribute(
                    Constants.SELECTED_STANDARDIZE_TPA_FIRM_ID,
                    String.valueOf(tpaFirmId));

            return forwards.get( TPA_CUSTOMIZE_CONTRACT_SEARCH_PAGE);
        }
        
        return Constants.HOMEPAGE_FINDER_FORWARD; 
     }
}
	

