package com.manulife.pension.ps.web.fee;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.view.ContentFile;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.fee.util.FeeDisclosureUtility;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWRegulatoryDisclosure;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.fund.valueobject.Access404a5;
import com.manulife.pension.service.fund.valueobject.Access404a5.Facility;
import com.manulife.pension.service.fund.valueobject.Access404a5.MissingInformation;
import com.manulife.pension.service.fund.valueobject.Access404a5.Qualification;
import com.manulife.pension.util.log.ServiceLogRecord;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.service.contract.valueobject.ContractDetail;
import com.manulife.pension.util.ArrayUtility;

/**
 * This is action class to regulatory disclosure page
 * 
 * @author Eswar
 * 
 */
@Controller
@RequestMapping(value ="/fee")
@SessionAttributes("disclosureForm")
public class RegulatoryDisclosureController extends PsController {

	@ModelAttribute("disclosureForm") 
	public RegulatoryDisclosureForm populateForm() 
	{
		return new RegulatoryDisclosureForm();
		}

	public static Map<String,String> forwards = new HashMap<>();
	static{
		forwards.put( "regulatoryDisclosure","/fee/regulatoryDisclosure.jsp");
		}
	
    public static final String REGULATORY_DISCLOSURE = "regulatoryDisclosure";

    /**
     * Constructor
     */
    public RegulatoryDisclosureController() {
        super(RegulatoryDisclosureController.class);
    }
    
    /**
     * This is an overridden method. This method handles the book marking
     * scenarios. This will check if contract is not in pilot it will redirect to HOME page
     * @param form
     *            Form
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @throws ServletException
     *             , IOException, SystemException
     * @return ActionForward
     * 
     */
    public String preExecute(RegulatoryDisclosureForm actionForm, HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	   
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> preExecute");
        }
        UserProfile userProfile = getUserProfile(request);
        Contract currentContract = userProfile.getCurrentContract();
        int contractNumber = currentContract.getContractNumber();
    
       // RegulatoryDisclosureForm regulatoryDisclosureForm = (RegulatoryDisclosureForm) form;
     
    	Access404a5 contractAccess = userProfile.getAccess404a5();
    	
    	Qualification piNoticeQual = contractAccess.getAccess(Facility._404A5_PLAN_AND_INVESTMENT_NOTICE);
    	Qualification iccQual = contractAccess.getAccess(Facility.INVESTMENT_COMPARATIVE_CHART);
    	
    	actionForm.setShow404a5Section(! contractAccess.getAccessibleFacilities().isEmpty());
    	actionForm.setShowPiNoticeLink(piNoticeQual != null && ! piNoticeQual.getTemporarilyMissingInformation().contains(MissingInformation.ICC_CONTACT));
    	actionForm.setShowIccLink(iccQual != null && ! iccQual.getTemporarilyMissingInformation().contains(MissingInformation.ICC_CONTACT));
    	actionForm.setShowMissingIccContactMessage(
                piNoticeQual != null && piNoticeQual.getTemporarilyMissingInformation().contains(MissingInformation.ICC_CONTACT)
                || iccQual != null && iccQual.getTemporarilyMissingInformation().contains(MissingInformation.ICC_CONTACT));
    	actionForm.setShowIccCalendarYearMessage(
                piNoticeQual != null && piNoticeQual.getTemporarilyMissingInformation().contains(MissingInformation.PREVIOUS_YEAR_END_FUND_DATA)
                || iccQual != null && iccQual.getTemporarilyMissingInformation().contains(MissingInformation.PREVIOUS_YEAR_END_FUND_DATA));
    	actionForm.setShowIpiAddendumLink(contractAccess.getAccess(Facility.IMPORTANT_PLAN_INFORMATION_ADDENDUM_TEMPLATE) != null);
    	actionForm.setShowParticipantFundChangeNoticeTemplate(contractAccess.getAccess(Facility.PARTICIPANT_FUND_CHANGE_NOTICE_TEMPLATE) != null);
    	actionForm.setShowParticipantStatementFeesTool(contractAccess.getAccess(Facility.PARTICIPANT_STATEMENT_FEES_TOOL) != null);
    	actionForm.setShowIpiHypotheticalToolLink(contractAccess.getAccess(Facility.IMPORTANT_PLAN_INFORMATION_HYPOTHETICAL_TOOL) != null && userProfile.isAllowedIPIHypotheticalTool());
    	actionForm.setShow404a5NoticeInfoTool(contractAccess.getAccess(Facility._404a5_NOTICE_INFO) != null);
        
    	actionForm.setContractStatus(currentContract.getStatus());
        boolean isParticipantStatementAvaiable = false;
        if (userProfile.isParticipantStatementAvaiable()){
        	isParticipantStatementAvaiable = true;
        }
        
        boolean isFeeDisclsoureAvaiable = FeeDisclosureUtility.isFeeDisclsoureAvaiable(currentContract);
        actionForm.setFeeDisclosureAvaialable(isFeeDisclsoureAvaiable);
        
        boolean contractAndProductRestriction = ContractServiceDelegate.getInstance()
				.getContractAndProdcuctRestrictionForIAContract(contractNumber);
		actionForm.setContractandProductRestrictionFlag(contractAndProductRestriction);
        
		/*if (regulatoryDisclosureForm.getShowPiNoticeLink()) {
			regulatoryDisclosureForm
					.setShow404a5AddendumTransactionProcessingProcessFeeLink(is404a5AddendumforJHWithdrawalProcessFeeLinkAvailable(
							currentContract.isBundledGaIndicator(),
							currentContract.getProductId()));
		}*/
        
        if (!actionForm.isFeeDisclosureAvaialable() 
        		&& !actionForm.getShow404a5Section()
        		&& !isParticipantStatementAvaiable) {
            
        	return Constants.Home_URL_REDIRECT;
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> preExecute");
        }
        return super.preExecute( actionForm, request, response);
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.manulife.pension.platform.web.controller.BaseAction#doExecute(org
     * .apache.struts.action.ActionMapping, org.apache.struts.action.Form,
     * javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @RequestMapping(value ="/disclosure/",  method =  {RequestMethod.GET}) 
    public String doExecute(@Valid @ModelAttribute("disclosureForm") RegulatoryDisclosureForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {
    	String forward = preExecute(actionForm, request, response);
		if(StringUtils.isNotBlank(forward)) {
			return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
		}
    	if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("regulatoryDisclosure");
	       }
		}
        if (logger.isDebugEnabled()) {
            logger.debug(" entered RegulatoryDisclosureAction.doExecute");
        }
        
       
        //RegulatoryDisclosureForm regulatoryDisclosureForm = (RegulatoryDisclosureForm) form;
        
        UserProfile userProfile = getUserProfile(request);
        Contract currentContract = userProfile.getCurrentContract();

        request.getSession().removeAttribute("ipiToolForm");  // Added code for FD2013 : IPI Hypothetical Tool Project.
        
        if (actionForm.isDownload404a5AddendumTemplate()) {
        	actionForm.setDownload404a5AddendumTemplate(false);
        	
        	ContentFile contentFileId = get404a5AddendumTemplateContentFilePath();
        	
        	URL url = new URL(request.getRequestURL().substring(0,
        			request.getRequestURL().indexOf("/"))
        			+ "//"
        			+ request.getServerName() 
        			+ contentFileId.getPath());

        	BaseReportController.streamDownloadData(request, response,
        			"application/msword", contentFileId.getName(),
        			IOUtils.toByteArray(url. openStream()));

        	return null;
        }
        
        int contractNumber = currentContract.getContractNumber();
        
        Date apolloDate = FeeDisclosureUtility.getAsOfDateForWebPage();
        
        // Get the Stable Value Fund Id associated with contract, if available
       	String svfFundId = FeeDisclosureUtility.getStableValueFundId(contractNumber, new Date());
        if (svfFundId != null && !svfFundId.trim().equals("") ) {
        	actionForm.setSvfIndicator(true);
        	actionForm.setStableValueFundId(svfFundId);
		}
        
        // Get the RP and R1 family suite associated with contract, if available
        actionForm.setrPandR1Indicator(FeeDisclosureUtility.checkR1andRPFundAvaliableForSelectedContact(contractNumber, apolloDate));
       
        // Get LT family suite associated with contract, if available
        actionForm.setlTIndicator(FeeDisclosureUtility.checkLTFundAvaliableForSelectedContact(contractNumber, apolloDate));
                
		// inforce pdf
        actionForm.setHasInforceFeeDisclosurePdf(
				FeeDisclosureUtility.checkIfInforceFeeDisclosurePdfAvailable(contractNumber));
		
		// 60 day page
        actionForm.setInvestmentRelatedCostsPageAvailable(
				FeeDisclosureUtility.checkIfInvestmentCostPageAvaiable(contractNumber));
		
		// Check whether contract is pinpoint
        actionForm.setPinpointContract(
				FeeDisclosureUtility.isPinpoinContract(contractNumber));
        
        if (logger.isDebugEnabled()) {
            logger.debug(" exit RegulatoryDisclosureAction.doExecute");
        }
    
        
        return forwards.get(REGULATORY_DISCLOSURE);
    }
	

	
    
    /**
     * To mrl log on generate ICC pdf 
     * @param profileId
     * @param contractNumber
     * @param functionName
     * @param loggingPoint
     */
    public static void logGenerateICCPDF(String profileId,
            String contractNumber, String functionName, String loggingPoint) {
        ServiceLogRecord record = new ServiceLogRecord(functionName);
        Logger interactionLog = Logger.getLogger(ServiceLogRecord.class);

        StringBuffer logData = new StringBuffer();
        logData.append("Participant Profile Id:[" + profileId + "]");
        logData.append("Contract Number:[" + contractNumber + "]");
        logData.append("Action Taken:[ICC - "
                + functionName + "]");

        record.setMethodName(loggingPoint);
        record.setData(logData.toString());
        record.setUserIdentity(profileId);
        record.setDate(new Date());

        interactionLog.error(record);
    }
    
    private boolean is404a5AddendumforJHWithdrawalProcessFeeLinkAvailable(
			boolean isBundileIndicator, String productId) {

		if (Constants.PRODUCT_IDS_MULTICLASS.contains(productId)
				&& !isBundileIndicator) {

			return true;
		}
		return false;
	}

	public ContentFile get404a5AddendumTemplateContentFilePath() throws SystemException {

		ContentFile documentContentFileFromCMA = null;
		try {
			BrowseServiceDelegate browseServiceDelegate = BrowseServiceDelegate
					.getInstance();
			documentContentFileFromCMA = browseServiceDelegate
					.findContentFile(
							ContentConstants.TRANSACTION_FEES_404A5_ADDENDUM_TEMPLATE_CONTENTFILEID_PATH);

		} catch (ContentException contentException) {
			throw new SystemException(
					contentException,
					"Error occured while retrieveing CMA Content Key : "
							+ ContentConstants.TRANSACTION_FEES_404A5_ADDENDUM_TEMPLATE_CONTENTFILEID_PATH
							+ " : " + contentException);
		}

		return documentContentFileFromCMA;

	}
	
	
	/**
	 * * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations.
	 */
	 @Autowired
	   private PSValidatorFWRegulatoryDisclosure  psValidatorFWRegulatoryDisclosure;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWRegulatoryDisclosure);
	}
	
}
