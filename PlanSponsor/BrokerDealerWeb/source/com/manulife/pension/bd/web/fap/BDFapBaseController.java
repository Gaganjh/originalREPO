package com.manulife.pension.bd.web.fap;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.BobContextUtils;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.userprofile.BDAssistantUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfileHelper;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.fap.FapForm;
import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.platform.web.fap.customquery.CustomQueryController;
import com.manulife.pension.platform.web.fap.util.FapReportsUtility;
import com.manulife.pension.service.broker.valueobject.NFACodeConstants;
import com.manulife.pension.service.contract.valueobject.ContractDetailsOtherVO;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.role.BDBasicFinancialRep;
import com.manulife.pension.service.security.role.BDFirmRep;
import com.manulife.pension.service.security.role.BDUserRole;
/**
 * Action Class is specific to the 
 * Broker Dealer Module - Funds & Performance pages.
 * 
 * The contract search is implemented in this action class. 
 *  
 * @author ayyalsa
 *
 */
public class BDFapBaseController extends CustomQueryController {

    @Override
    public String preExecute(ActionForm form,
            HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException, SystemException {

        String forward = null;
        FapForm fapForm = (FapForm) form;

        // get the userProfile from the session
        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);

        // If the userProfile is NULL, then the session should be expired
		if (userProfile == null) {
			// set the results ID
			fapForm.setFilterResultsId(FapConstants.SESSION_EXPIRED);
			
			return forwards.get(FapConstants.FORWARD_SESSION_EXPIRED);

		// if the user is in Contract F&P, create the bobContext and 
		// set the content Location
		} else if (fapForm.isContractMode()) {

            BobContextUtils.setUpBobContext(request);
            
            // if the bobContext is null, then forward to the BOB Overview page
            BobContext bob = BDSessionHelper.getBobContext(request) ;
            if (bob == null || bob.getCurrentContract() == null) {
               
            	return forwards.get(BDConstants.BOB_PAGE_FORWARD);
            }
            
            BobContextUtils.setupProfileId(request);
            // set the content location
            setContentLocation(request, 
            		bob.getCurrentContract().getCompanyCode());
            
        // the user should be in Generic F&P, set the content 
        // location based on the company Id
        } else {
        	// set the content location
        	setContentLocation(request, fapForm.getCompanyId());
        }
		
        return forward;
    }
	
    /**
     * Sets the content location to load the CMA content. By default 
     * the US content will be retrieved. If need to load the NY content, then 
     * set the content location explicitly. 
     * 
     * @param request
     * @param companyId
     */
    protected final void setContentLocation(HttpServletRequest request, 
    		String companyId) {

    	// no need to validate for US company id. By default always the 
    	// content location is US.
    	if (StringUtils.equals(companyId, FapConstants.COMPANY_ID_NY)) {
			ApplicationHelper.setRequestContentLocation(
					request, Location.NEW_YORK);
		}
		
    }
    
    /**
     * Returns the current contract number, which is stored in the BobContext
     * 
     * @param request
     * @return contractNumber
     */
	 protected String getCurrentContractNumber(HttpServletRequest request) {
	    	BobContext bobContext = BDSessionHelper.getBobContext(request);
	    	String contractNumberAsString = null;
	    	
    	int contractNumber = 0;
    	if (bobContext != null && bobContext.getCurrentContract() != null) {
    		contractNumber = 
    			bobContext.getCurrentContract().getContractNumber();
    	}
    	
    	if (contractNumber != 0){
    		contractNumberAsString = String.valueOf(contractNumber);
    	}
    	
    	return contractNumberAsString;
    }
	
    /**
     * The method populates the form bean with the reports & download 
     * dropdown list
     * 
     * @param fapForm
     * 
     * @throws SystemException
     */
    protected void populateReportDropDownList(FapForm fapForm,
            HttpServletRequest request) throws SystemException {
        
        super.populateReportDropDownList(fapForm, request);
        BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
        // if user is level 1 broker then hide
        // standard reports and other reports dropdown
        if (userProfile.getRole() instanceof BDBasicFinancialRep) {
            Map<String, String> reports = fapForm.getReportList();
            reports.remove(FapConstants.STANDARD_REPORTS_TITLE);
            reports.remove(FapConstants.EXPENSE_RATIO_REPORT_TITLE);
            reports.remove(FapConstants.FUND_CHARACTERISITICS_REPORT_TITLE);
            reports.remove(FapConstants.INVESTEMNT_RETURNS_AND_EXPENSE_RATIO_REPORT_TITLE);
            reports.remove(FapConstants.INVESTEMNT_RETURNS_AND_STANDARD_DEVIATION_REPORT_TITLE);
            reports.remove(FapConstants.MORNINGSTAR_RATINGS_AND_TICKR_SYMBOLS_REPORT_TITLE);
            reports.remove(FapConstants.OTHER_REPORTS_TITLE);
            reports.remove(FapConstants.MARKET_INDEX_REPORT_TITLE);
        }
    }
    
    /**
     * This method gets layout page for the given layout id.
     * 
     * @param path
     * @return LayoutPage
     */
    protected LayoutPage getLayoutPage(
    		String id, HttpServletRequest request) {
    	ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    	HttpServletRequest req = attr.getRequest();
        //BDLayoutBean bean = ApplicationHelper.getLayoutStore(getServlet().getServletContext()).getLayoutBean(id, request);
    	BDLayoutBean bean = ApplicationHelper.getLayoutStore(req.getServletContext()).getLayoutBean(id, request);
        LayoutPage layoutPageBean = (LayoutPage) bean.getLayoutPageBean();
        return layoutPageBean;
    }
    
    /**
     * returns the BDUser's profile Id
     */
    protected long getProfileId(HttpServletRequest request) {
		
		BDPrincipal principal = 
			BDSessionHelper.getUserProfile(request).getBDPrincipal();
		
		return principal.getProfileId();
	}
    
    /**
     * Validates whether the user has permission to view the saved data
     */
    protected boolean userAllowedToViewSavedData(HttpServletRequest request) {
    	
    	boolean isUserAllowed = true;
    	
    	BDUserProfile userProfile = getMimickingUserProfile(request);
    	
    	if (userProfile != null) {
    		BDUserRole userRole = userProfile.getBDPrincipal().getBDUserRole();
    		if (userRole.getRoleType().compareTo(
    				BDUserRoleType.FinancialRepAssistant) == 0) {
    			isUserAllowed = false;
    		}
    	}
    	
    	return isUserAllowed;
    	
    }
    
	/**
	 * validates whether the user is in mimic
	 * 
	 * @param request
	 * @return true, if the user is in mimic
	 * @throws SystemException
	 */
	protected boolean isUserInMimic(HttpServletRequest request) 
	throws SystemException {
		
		boolean inMimic = false;
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		
		if (userProfile.isInMimic()) {
			inMimic = true;
		}
		return inMimic;
	}
	
	/**
	 * validates whether the NML funds are to be included 
	 * 
	 * @param request
	 * @return true, if the user is 
	 * 					1. NML BD Firm Rep, 
	 * 					2. Broker associated with NML 
	 * 					3. Assistant of 2
	 * 					4. Internal User mimicking 1, 2 or 3
	 * @throws SystemException
	 */
	protected boolean includeNMLFunds(HttpServletRequest request) 
	throws SystemException {
		
		boolean includeNMLFunds = false;
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		
		if (BDUserProfileHelper.associatedWithApprovingFirm(
				userProfile, NFACodeConstants.NML) &&
				(BDUserProfileHelper.isFirmRep(userProfile) ||
						BDUserProfileHelper.isFinancialRep(userProfile) ||
						BDUserProfileHelper.isFinancialRepAssistant(userProfile))) {
			
			includeNMLFunds = true;
		}

		return includeNMLFunds;
	}
	
	/**
	 * validates whether the ML funds are to be included 
	 * 
	 * @param request
	 * @return true, if the user is 
	 * 					Merrill Lynch user, 
	 * 					
	 * @throws SystemException
	 */
	@Override
	protected boolean includeOnlyMerrillCoveredFunds(HttpServletRequest request, int contractNumber) throws SystemException {
		Boolean isMerrillAdvisor = false;
		boolean isMerrillContract = false;
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
		isMerrillAdvisor = userProfile.isMerrillAdvisor();
    	if(contractNumber != 0){
    		ContractDetailsOtherVO contractDetailsOtherVO = contractServiceDelegate.getContractDetailsOther(contractNumber);
			if(contractDetailsOtherVO != null && contractDetailsOtherVO.isMerrillLynch()){
				isMerrillContract = true;
			}
    	}
    	userProfile.setMerrillContract(isMerrillContract);
        return isMerrillAdvisor || isMerrillContract;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void setWarningsInRequest(HttpServletRequest request, 
			Collection warnings) {
		request.setAttribute(FapConstants.BD_WARNINGS_KEY, warnings);
	}
	 
	@Override
	@SuppressWarnings("unchecked")
	protected void setInformationMessagesInRequest(HttpServletRequest request, 
			Collection warnings) {
		request.setAttribute(FapConstants.BDW_INFORMATION_MESSAGES, warnings);		
    }
	
	@Override
    protected Map<String, String> getAllTabHeaders(
    		FapForm fapForm) {
		
		// Set the content location based on the company ID
		if (StringUtils.equals(FapConstants.COMPANY_ID_NY, 
				fapForm.getCompanyId())) {
			return FapReportsUtility.getAllTabHeaders(Location.NEW_YORK);
		}

		 return FapReportsUtility.getAllTabHeaders(null);
    }
    
    @Override
	protected void postExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {
		
    	FapForm actionForm = (FapForm) form;
    	
    	String path = "/fap/fapUS.jsp";
    	
    	if (actionForm.isContractMode()){
    		path = "/fap/contractFandp.jsp";
    	}
    	ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    	HttpServletRequest req = attr.getRequest();
		//BDLayoutBean bean = ApplicationHelper.getLayoutStore(getServlet().getServletContext()).getLayoutBean(path, request);
    	BDLayoutBean bean = ApplicationHelper.getLayoutStore(req.getServletContext()).getLayoutBean(path, request);
		// if bean is null it means the request is not going
		// to be forwarded to jsp(one of the layout pages).
		if (bean != null) {
			request.setAttribute("layoutPageBean", bean.getLayoutPageBean());
		}
	}
    
    /**
     * This method would return the UserProfile object of the mimicking user.
     * 
     * @return - BDUserProfile object of the mimicking user.
     */
    @SuppressWarnings("unchecked")
    private BDUserProfile getMimickingUserProfile(HttpServletRequest request) {
        Map<String, Object> mimickingUserSession = 
        	(Map<String, Object>) request.getSession(false).getAttribute(
        			BDConstants.ATTR_MIMICKING_SESSION);
        
        if (mimickingUserSession == null) {
            return null;
        }
        
        BDUserProfile mimickingInternalUserProfile = 
        	(BDUserProfile) mimickingUserSession.get(
        			BDConstants.USERPROFILE_KEY);

        return mimickingInternalUserProfile;
    }
    
    /**
     * Returns True if the current company name and the selected company 
     * name matches
     * 
     * @param fapForm
     * @return boolean
     */
    protected boolean doesCompanyNameMatch(FapForm fapForm) {
    	
    	if ((StringUtils.equals(fapForm.getCompanyId(), 
    					FapConstants.COMPANY_ID_US) && 
				StringUtils.equals(fapForm.getSelectedCompanyName(), 
						FapConstants.COMPANY_NAME_US)) 
			||
				(StringUtils.equals(fapForm.getCompanyId(), 
						FapConstants.COMPANY_ID_NY) && 
				StringUtils.equals(fapForm.getSelectedCompanyName(), 
						FapConstants.COMPANY_NAME_NY))) {
    		return true;
    	}
    	return false;
    }
    
    /**
	 * validate whether the contract has GIFL select feature.
	 * 
	 * @param fapForm - used to check whether request is coming from generic F&P or contract f&p
	 * @param request
	 * @return boolean - return true if contract has GIFL select else return false
	 * @throws SystemException
	 */
    protected boolean includeGIFL(FapForm fapForm,
			HttpServletRequest request) throws SystemException{
		boolean isGIFLDisplayRequired = false;
		String contractNumber = null;
		
		// if request is coming from contract F&P. get the GIFL version from bob object.
		if(fapForm.isContractMode()){
			BobContext bob = BDSessionHelper.getBobContext(request);
			// If contract has GIFL select
			if (bob != null && bob.getCurrentContract() != null 
					&& bob.getCurrentContract().getGiflVersion() != null
					&& StringUtils.equals(bob.getCurrentContract().getGiflVersion(), FapConstants.GIFL_SELECT_VERSION)){
				isGIFLDisplayRequired = true;
			}			
		}else{ // request is coming from generic F&P page
			contractNumber = fapForm.getContractSearchText();
			if(StringUtils.isNotBlank(contractNumber)){
				String giflVersion = ContractServiceDelegate.getInstance().getContractGiflVersionNo(Integer.valueOf(contractNumber));
				// If contract has GIFL select
				if(StringUtils.isNotBlank(giflVersion) && StringUtils.equals(giflVersion, FapConstants.GIFL_SELECT_VERSION)){
					isGIFLDisplayRequired = true;					
				}
			}
			else{ // This means that request is coming for all funds ( not contract funds)
				isGIFLDisplayRequired = true;
			}
		}
		return isGIFLDisplayRequired;
	}
    
	@Override
	protected void populateAdvisorNameFields(FapForm fapForm, HttpServletRequest request) {
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		
		if(userProfile.isInternalUser() || (!userProfile.isInternalUser() && userProfile.isInMimic())) {
			fapForm.setAdvisorNameDisplayed(true);
		} else {
			if(userProfile.getRole() instanceof BDFirmRep) {
				if(userProfile.getBDPrincipal().getProducerLicense()) {
					fapForm.setAdvisorNameDisplayed(false);
					fapForm.setAdvisorName(userProfile.getBDPrincipal().getFirstName()+ " " + userProfile.getBDPrincipal().getLastName());
				} else {
					fapForm.setAdvisorNameDisplayed(true);
				}
			} else if (userProfile instanceof BDAssistantUserProfile) {
				BDAssistantUserProfile financialRepAssistant =  (BDAssistantUserProfile) userProfile;
				fapForm.setAdvisorNameDisplayed(false);
				fapForm.setAdvisorName(financialRepAssistant.getParentPrincipal().getFirstName()
						+ " " + financialRepAssistant.getParentPrincipal().getLastName());
			} else {
				fapForm.setAdvisorNameDisplayed(false);
				fapForm.setAdvisorName(userProfile.getBDPrincipal().getFirstName()+ " " + userProfile.getBDPrincipal().getLastName());
			}
		}
	}
}
