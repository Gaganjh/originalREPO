
package com.manulife.pension.ps.web.noticemanager.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;



import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.PlanNoticeDocumentServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.CommonEnvironment;import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentReportData;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentChangeHistoryVO;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.noticemanager.UploadSharedNoticeManagerForm;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Address;
import com.manulife.pension.service.contract.valueobject.ClientContact;
import com.manulife.pension.service.contract.valueobject.ContactOptionType;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.Access404a5;
import com.manulife.pension.service.fund.valueobject.Access404a5.Facility;
import com.manulife.pension.service.fund.valueobject.Access404a5.MissingInformation;
import com.manulife.pension.service.fund.valueobject.Access404a5.Qualification;
import com.manulife.pension.service.fund.valueobject.UserAccess;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.UserRole;

/**
 * 
 * Notice Manager Utility to prepare the Jh document available for the given contract
 * 
 * @author krishta
 * 
 */
public class NoticeManagerUtility {

    public static Properties properties = new Properties();

    // Below static block to initialize property file
    static {
    	InputStream propertyFileStream = null;
        try {
            propertyFileStream = Class.forName(NoticeManagerUtility.class.getName())
                    .getClassLoader().getResourceAsStream(Constants.PROPERTIES_FILE_NAME);
            properties.load(propertyFileStream);
        } catch (Throwable e) {
            SystemException se = new SystemException(e,
                    "NoticeManagerUtility - static: Static block failed for noticeManager.properties");
            throw ExceptionHandlerUtility.wrap(se);
        }finally{
        	try {
        		if(propertyFileStream != null)
        			propertyFileStream.close();
			} catch (IOException e) {
				SystemException se = new SystemException(e,
	                    "NoticeManagerUtility - static: Static block failed for noticeManager.properties");
	            throw ExceptionHandlerUtility.wrap(se);
			}
        }
    }
    final static Logger logger = Logger.getLogger(NoticeManagerUtility.class);

    /**
     * Get the notice Manager edit Permission
     * 
     * @param reportData
     * @param userProfile
     * @throws SystemException
     */
    public static boolean getNoticeManagerUserAccessPermission(UserProfile userProfile) throws SystemException {
        boolean isTPAUser = userProfile.isTPA();
        boolean isTPAUserManager = userProfile.isTpaUserManager();
        boolean isExternalUser = userProfile.getRole().isExternalUser();
        boolean isInternalUser = userProfile.isInternalUser();
        boolean isNoticeManagerTermsofUseConsent = false;
        String termsOfUseCode = PlanNoticeDocumentServiceDelegate.getInstance().
        								getTermsAndAcceptanceInd(new BigDecimal(userProfile.getPrincipal().getProfileId()));
        if (Constants.YES.equalsIgnoreCase(termsOfUseCode)) {
            isNoticeManagerTermsofUseConsent = true;
        }
        Contract contract = userProfile.getCurrentContract();
        boolean noticeManagerAccessPermission = true;
        if ((isExternalUser && !userProfile.isNoticeManagerAccessAllowed()) || (isInternalUser)
                || Contract.STATUS_CONTRACT_DISCONTINUED.equals(contract.getStatus())) {
            noticeManagerAccessPermission = false;
        } else if (!isNoticeManagerTermsofUseConsent && (isExternalUser || isTPAUser || isTPAUserManager)) {
            noticeManagerAccessPermission = false;
        }
        return noticeManagerAccessPermission;
    }

    /**
     * Check whether the user has permission to add custom document
     * 
     * @param reportData
     * @param userProfile
     * @throws SystemException
     */
    public static boolean getNoticeManagerUploadedAccessPermission(
            PlanDocumentReportData reportData, UserProfile userProfile,
            BaseReportForm reportForm) throws SystemException {
        boolean isExternalUser = userProfile.getRole().isExternalUser();
        boolean isInternalUser = userProfile.isInternalUser();
        boolean isTermAcceptedUser = true;
        String termsOfUseCode = reportData.getTermsOfUseCode().trim();
        UploadSharedNoticeManagerForm uploadshareform = (UploadSharedNoticeManagerForm) reportForm;
        uploadshareform.setUserTermsAndAcceptanceInd(termsOfUseCode);
        if (Constants.X.equalsIgnoreCase(termsOfUseCode) || !userProfile.isNoticeManagerAccessAllowed()) {
            isTermAcceptedUser = false;
        }
        Contract contract = userProfile.getCurrentContract();
        int uploadedDocumentCount = PlanNoticeDocumentServiceDelegate.getInstance()
                .getUserUploadedDocumentPostedCount(contract.getContractNumber());
        if (uploadedDocumentCount >= (Integer.parseInt(properties
                .getProperty("UPLOAD_DOCUMENT_POSTED_COUNT")))) {
            uploadshareform.setUploadDocumentPostedCount(true);
        } else {
            uploadshareform.setUploadDocumentPostedCount(false);
        }
        if (uploadedDocumentCount == 1) {
            uploadshareform.setDisableTheDownSortArrow(true);
        }
        boolean noticeManagerUploadedPermission = true;
        if ((isExternalUser && !isTermAcceptedUser) || (isInternalUser)
                || Contract.STATUS_CONTRACT_DISCONTINUED.equals(contract.getStatus())) {
            noticeManagerUploadedPermission = false;
        }
        return noticeManagerUploadedPermission;
    }

    /**
     * Verify the 404a5 document access permission and set it the form for further validation
     * 
     * @param userProfile
     * @param reportData
     * @param reportForm
     * @throws SystemException 
     */

    public static void get404a5JHDocumentAccessPermission(UserProfile userProfile, BaseReportForm reportForm) throws SystemException {
        UploadSharedNoticeManagerForm uploadshareform = (UploadSharedNoticeManagerForm) reportForm;
    	UserAccess userAccess = null;
		if(userProfile.isTPA()){
			userAccess = UserAccess.TPA;
		}else if (userProfile.isInternalUser()){
			userAccess = UserAccess.INTERNAL_USER;
		}else if (userProfile.getRole().isPlanSponsor()){
			userAccess = UserAccess.PSW;
			}else {
				userAccess = null;
	        }
		FundServiceDelegate fundServiceDelegate = FundServiceDelegate.getInstance();
    Access404a5 contractAccess = fundServiceDelegate.get404a5Permissions(EnumSet.of(
            Facility._404A5_PLAN_AND_INVESTMENT_NOTICE,
            Facility.IMPORTANT_PLAN_INFORMATION_ADDENDUM_TEMPLATE,
            Facility.INVESTMENT_COMPARATIVE_CHART,
            Facility.PARTICIPANT_FUND_CHANGE_NOTICE_TEMPLATE,
            Facility.PARTICIPANT_STATEMENT_FEES_TOOL,
            Facility.IMPORTANT_PLAN_INFORMATION_HYPOTHETICAL_TOOL,
            Facility._404a5_NOTICE_INFO),userProfile.getCurrentContract().getContractNumber(),userAccess);
       
    Qualification piNoticeQual = contractAccess
                .getAccess(Facility._404A5_PLAN_AND_INVESTMENT_NOTICE);
        Qualification iccQual = contractAccess.getAccess(Facility.INVESTMENT_COMPARATIVE_CHART);

        boolean show404section = !contractAccess.getAccessibleFacilities().isEmpty();
        boolean showPlanInvestmentNotice = piNoticeQual != null
                && !piNoticeQual.getTemporarilyMissingInformation().contains(
                        MissingInformation.ICC_CONTACT);
        boolean showICC = iccQual != null
                && !iccQual.getTemporarilyMissingInformation().contains(
                        MissingInformation.ICC_CONTACT);
        boolean showMissingIccContactMessage = piNoticeQual != null
                && piNoticeQual.getTemporarilyMissingInformation().contains(
                        MissingInformation.ICC_CONTACT)
                || iccQual != null
                && iccQual.getTemporarilyMissingInformation().contains(
                        MissingInformation.ICC_CONTACT);
        boolean showIccCalendarYearMessage = piNoticeQual != null
                && piNoticeQual.getTemporarilyMissingInformation().contains(
                        MissingInformation.PREVIOUS_YEAR_END_FUND_DATA)
                || iccQual != null
                && iccQual.getTemporarilyMissingInformation().contains(
                        MissingInformation.PREVIOUS_YEAR_END_FUND_DATA);

        uploadshareform.setShow404section(show404section);
        uploadshareform.setShowPlanInvestmentNotice(showPlanInvestmentNotice);
        uploadshareform.setShowICC(showICC);
        uploadshareform.setShowMissingIccContactMessage(showMissingIccContactMessage);
        uploadshareform.setShowIccCalendarYearMessage(showIccCalendarYearMessage);

    }

    /**
     * Add the Jh notice document to the report data based on the given condition NMBYP 23,25,28
     * given in Build your package DFS
     * 
     * @param userProfile
     * @param report
     * @param reportForm
     * @throws SystemException
     */
    public static List<PlanNoticeDocumentVO>  addJhNoticeDocumentVO(UserProfile userProfile,
    			BaseReportForm reportForm, boolean isIccIncluded)
            throws SystemException {
        Contract currentContract = userProfile.getCurrentContract();
        int contractNumber = currentContract.getContractNumber();
        List<PlanNoticeDocumentVO> jhPlanNoticeDocuments = new ArrayList<PlanNoticeDocumentVO>();
        UploadSharedNoticeManagerForm uploadshareform = (UploadSharedNoticeManagerForm) reportForm;
        boolean iccDocumentAvailable = false;
        PlanNoticeDocumentVO planNoticeDocumentVO = null;
        if (uploadshareform.isShowPlanHighlight()) {

            planNoticeDocumentVO = getJhNoticeDocument(contractNumber, Constants.PLAN_HIGHLIGHT_NOTICE,
                    iccDocumentAvailable);
            jhPlanNoticeDocuments.add(planNoticeDocumentVO);

        }
        if (isIccIncluded && uploadshareform.isShow404section() && uploadshareform.isShowICC()) {

            iccDocumentAvailable = true;
            planNoticeDocumentVO = getJhNoticeDocument(contractNumber, Constants.PLAN_ICC_DOCUMENT,
                    iccDocumentAvailable);
            jhPlanNoticeDocuments.add(planNoticeDocumentVO);

        }
        if (uploadshareform.isShow404section() && uploadshareform.isShowPlanInvestmentNotice()) {

            planNoticeDocumentVO = getJhNoticeDocument(contractNumber, Constants.PLAN_INVESTMENT_DOCUMENT,
                    iccDocumentAvailable);
            jhPlanNoticeDocuments.add(planNoticeDocumentVO);

        }
		return jhPlanNoticeDocuments;


    }

    /**
     * Add the Jh notice document to the report data based on the given condition NMBYP 23,25,28
     * given in Build your package DFS
     * 
     * @param userProfile
     * @param report
     * @param reportForm
     * @throws SystemException
     */
    public static boolean   validateJHDocumentsWhileMerging(String selectedDocuments, UserProfile userProfile)
            throws SystemException {
    	String documentSelectedstring[] = selectedDocuments.split(",");
        
        UserAccess userAccess = null;
		if(userProfile.isTPA()){
			userAccess = UserAccess.TPA;
		}else if (userProfile.isInternalUser()){
			userAccess = UserAccess.INTERNAL_USER;
		}else if (userProfile.getRole().isPlanSponsor()){
			userAccess = UserAccess.PSW;
		}else {
			userAccess = null;
        }
    	FundServiceDelegate fundServiceDelegate = FundServiceDelegate.getInstance();
        Access404a5 contractAccess = fundServiceDelegate.get404a5Permissions(EnumSet.of(
                Facility._404A5_PLAN_AND_INVESTMENT_NOTICE,
                Facility.IMPORTANT_PLAN_INFORMATION_ADDENDUM_TEMPLATE,
                Facility.INVESTMENT_COMPARATIVE_CHART,
                Facility.PARTICIPANT_FUND_CHANGE_NOTICE_TEMPLATE,
                Facility.PARTICIPANT_STATEMENT_FEES_TOOL,
                Facility.IMPORTANT_PLAN_INFORMATION_HYPOTHETICAL_TOOL,
                Facility._404a5_NOTICE_INFO),userProfile.getCurrentContract().getContractNumber(),userAccess);
        boolean show404section = !contractAccess.getAccessibleFacilities().isEmpty();
    	Qualification iccQual = contractAccess.getAccess(Facility.INVESTMENT_COMPARATIVE_CHART);
    	Qualification piNoticeQual = contractAccess.getAccess(Facility._404A5_PLAN_AND_INVESTMENT_NOTICE);
        for(int documentIdIndex = 0; documentIdIndex< documentSelectedstring.length; documentIdIndex++){
        	if(Constants.PLAN_HIGHLIGHT.equals(documentSelectedstring[documentIdIndex])){
		    	//plan highlights
		        try {
					boolean isPlanHighlightAvailable = isCSFfeatureAvailable(userProfile.getCurrentContract().getContractNumber());
					if(!isPlanHighlightAvailable){
						return false;
					}
				} catch (ApplicationException e) {
					throw new SystemException(e,
		                    "Unable to retrive service feature for contract id " + userProfile.getCurrentContract().getContractNumber());
				}
	    	
        	}
        	if(Constants.PLAN_ICC_DOCUMENT.equals(documentSelectedstring[documentIdIndex])){
        		 boolean showICC = iccQual != null
        		 	&& !iccQual.getTemporarilyMissingInformation().contains(
 		                MissingInformation.ICC_CONTACT);
        		 if(!(show404section&&showICC)){
					return false;
				}
        	}
        	if(Constants.PLAN_INVESTMENT.equals(documentSelectedstring[documentIdIndex])){
		        //404
        		  boolean showPlanInvestmentNotice = piNoticeQual != null
                  && !piNoticeQual.getTemporarilyMissingInformation().contains(
                          MissingInformation.ICC_CONTACT);
		        if(!(show404section&&showPlanInvestmentNotice)){
					return false;
				}
        	}
        }
		return true;
    }

    /**
     * Prepare the input for the Jh document and add into the Jh list
     * 
     * @param contractNumber
     * @param jhDocumentName
     * @param iccDocumentAvailable
     */
    public static PlanNoticeDocumentVO getJhNoticeDocument(int contractNumber,
            String jhDocumentName, boolean iccDocumentAvailable) {
        PlanNoticeDocumentVO planNoticeDocumentVO = new PlanNoticeDocumentVO();
        planNoticeDocumentVO.setDocumentName(jhDocumentName);
        planNoticeDocumentVO.setContractId(contractNumber);
        planNoticeDocumentVO.setJhDocument(true);
        planNoticeDocumentVO.setPostToPptInd("Yes");
        planNoticeDocumentVO.setIccDocument(iccDocumentAvailable);
        return planNoticeDocumentVO;
    }

    /**
     * Check the plan highlight document available condition
     * 
     * @param userProfile
     * @param report
     * @param reportForm
     * @throws SystemException
     */
    public static boolean getPlanHighlightDocument(UserProfile userProfile, BaseReportForm reportForm) throws SystemException {
        Integer contractId = userProfile.getCurrentContract().getContractNumber();
        UploadSharedNoticeManagerForm uploadshareform = (UploadSharedNoticeManagerForm) reportForm;

        boolean isPlanHighlightAvailable = false;
        try {
            isPlanHighlightAvailable = isCSFfeatureAvailable(contractId);
        } catch (ApplicationException exception) {
            throw new SystemException(exception,
                    "Unable to retrive service feature for contract id " + contractId);
        }
        uploadshareform.setShowPlanHighlight(isPlanHighlightAvailable);
        return isPlanHighlightAvailable;
    }

    /**
     * Method to verify whether Contract Service Feature is available for a contract or not.
     * 
     * @param contractId
     * @return isCSFfeatureAvailable boolean
     * @throws SystemException
     * @throws ApplicationException
     */
    private static boolean isCSFfeatureAvailable(Integer contractId) throws SystemException,
            ApplicationException {
        boolean isCSFfeatureAvailable = false;
        ContractServiceFeature summaryPlanHighlightAvailable = ContractServiceDelegate
                .getInstance().getContractServiceFeature(contractId,
                        ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_AVAILABLE);
        if (summaryPlanHighlightAvailable != null) {
            boolean isSummaryPlanHighlightAvailable = ContractServiceFeature.internalToBoolean(
                    summaryPlanHighlightAvailable.getValue()).booleanValue();
            boolean isSummaryPlanHighlightReviewed = ContractServiceFeature
                    .internalToBoolean(
                            summaryPlanHighlightAvailable
                                    .getAttributeValue(ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_REVIEWDED))
                    .booleanValue();
            isCSFfeatureAvailable = isSummaryPlanHighlightAvailable
                    && isSummaryPlanHighlightReviewed;
        }
        return isCSFfeatureAvailable;
    }

    /**
     * Returns a byte array that contains the SPH PDF document.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws SystemException
     */
    public static String doViewSphPdf(
            BaseReportForm reportForm, HttpServletRequest request,
            HttpServletResponse response, UserProfile userProfile) throws SystemException {
        final Logger logger = Logger.getLogger(BaseReportController.class);
        byte[] downloadData = null;
        final Integer contractId = userProfile.getCurrentContract().getContractNumber();
        Location location = Location.valueOfForAbbreviation(CommonEnvironment.getInstance()
                .getSiteLocation());
        String headerFooterImagePath = null;
        java.net.URL url = NoticeManagerUtility.class
                .getResource(CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX
                        + CommonConstants.HEADER_FOOTER_IMAGE);
        if (url != null) {
            headerFooterImagePath = url.toExternalForm();
        }
        if (logger.isDebugEnabled()) {
            logger.info("Header Footer Image path for the PH PDF is " + headerFooterImagePath);
        }
        downloadData = ContractServiceDelegate.getInstance().generateSphPdf(contractId, location,
                headerFooterImagePath);
        if (downloadData != null && downloadData.length > 0) {
            ReportController.streamDownloadData(request, response, "application/pdf", "sph_"
                    + contractId + ".pdf", downloadData);
        }
        return null;
    }

    /**
     * 
     * Method used to validate Product Restriction (CCNM 3 - 2.1)
     * 
     * @param userProfile
     * @return boolean
     * @throws SystemException
     */
    public static boolean validateProductRestriction(Contract contract) throws SystemException {
        String productId = contract.getProductId();
        if (contract.isDefinedBenefitContract()
                || Constants.PRODUCT_RA457.equalsIgnoreCase(productId)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Method used to verify the contract restrictions (CCNM 4).
     * 
     * @param contract
     * @return boolean
     * @throws SystemException
     * @throws ContractDoesNotExistException
     */
    public static boolean validateContractRestriction(Contract contract) throws SystemException,
            ContractDoesNotExistException {
        boolean isRestrictedContract = ContractServiceDelegate.getInstance().isGovernmentPlan(
                contract.getContractNumber());
        boolean isMta = contract.isMta();
        if (isRestrictedContract || isMta)
            return true;
        else
            return false;
    }

    /**
     * Method used to validate whether the contract is DISCONTINUED status (DI) (CCNM 5)
     * 
     * @param userProfile
     * @return boolean
     * @throws SystemException
     */
    public static boolean validateDIStatus(Contract contract, UserRole role) throws SystemException {
        String tempdiDuration = Constants.DI_DURATION_6_MONTH;
        boolean contractStatusDI = false;
        if (role instanceof InternalUser || role instanceof ThirdPartyAdministrator) {
            tempdiDuration = Constants.DI_DURATION_24_MONTH;
        } else {
            tempdiDuration = Constants.DI_DURATION_6_MONTH;
        }
        int diDuration = Integer.parseInt(tempdiDuration);
        Contract contractDetails = null;
        try {
            contractDetails = ContractServiceDelegate.getInstance().getContractDetails(
                    contract.getContractNumber(), diDuration);
        } catch (ContractNotExistException contractNotExistException) {
        	throw new SystemException (contractNotExistException, "Failed to retrieve contract "+ contract.getContractNumber() + "details");
        }
        if(contractDetails.isDiscontinued()){
        	contractStatusDI = true;	
        }
        return contractStatusDI;

    }

    /**
     * Method used to validate the contract status (CCNM 8 - 2.6).
     * 
     * @param userProfile
     * @return boolean
     * @throws SystemException
     */
    public static boolean validateBuisnessContractStatus(Contract contract) throws SystemException {
        String contractStatus = contract.getStatus();

        if (Contract.STATUS_PROPOSAL_SIGNED.equalsIgnoreCase(contractStatus)
                || Contract.STATUS_DETAILS_COMPLETED.equalsIgnoreCase(contractStatus)
                || Contract.STATUS_PENDING_CONTRACT_APPROVAL.equalsIgnoreCase(contractStatus)
                || Contract.STATUS_CONTRACT_APPROVED.equalsIgnoreCase(contractStatus)
                || Contract.STATUS_ACTIVE_CONTRACT.equalsIgnoreCase(contractStatus)
                || Contract.STATUS_CONTRACT_FROZEN.equalsIgnoreCase(contractStatus)) {
            return true;
        } else {
            return false;
        }
        // TODO : Pending requriement:applying the If the PeopleSoft Requirement Checklist item
        // “Ready to order Enrolment Kits” is not complete

    }

    /**
     * This method will validate the client contact and mailing address information. (CCNM 7 - 2.5)
     * 
     * @param userProfile
     * @return boolean
     * @throws SystemException
     */
    public static boolean validateClientContact(Contract contract) throws SystemException {

        int contractNumber = contract.getContractNumber();

        Set<ContactOptionType> contactOptions = new HashSet<ContactOptionType>();
        contactOptions.add(ContactOptionType.ICC_DESIGNATE);
        contactOptions.add(ContactOptionType.PRIMARY_CONTACT);
        contactOptions.add(ContactOptionType.TRUSTEE_MAIL_RECIPIENT);

        Map<String, Address> clientAddress = ContractServiceDelegate.getInstance()
                .getClientAddress(contractNumber);

        Map<ContactOptionType, ClientContact> contacts = ContractServiceDelegate.getInstance()
                .getContractContacts(contractNumber, contactOptions);
        Address address = clientAddress.get(Address.MAILING_CODE);

        if (address == null) {
            address = clientAddress.get(Address.LEGAL_CODE);

            if (address == null) {
                address = clientAddress.get(Address.TRUSTEE_CODE);
            }
        }

        if ((isContactValid(contacts.get(ContactOptionType.ICC_DESIGNATE))
                || isContactValid(contacts.get(ContactOptionType.PRIMARY_CONTACT)) || isContactValid(contacts
                    .get(ContactOptionType.TRUSTEE_MAIL_RECIPIENT))) && (address != null)) {

            return true;
        }

        return false;

    }

    /**
     * Method to check whether contact is valid or not.
     * 
     * @param contact
     * @return boolean
     */
    private static boolean isContactValid(ClientContact contact) {
        return (contact != null && (StringUtils.isNotBlank(contact.getFirstName()))
                && (StringUtils.isNotBlank(contact.getLastName())) && (contact.getPhone() != null && !contact
                .getPhone().isEmpty()));

    }

    /**
     * To get the PlanAndInvestment Notice ReplaceMessage
     * @param reportForm
     * @param userProfile
     * @return
     * @throws SystemException
     * @throws ContractDoesNotExistException
     */
    public static boolean getPlanAndInvestmentNoticeReplaceMessage(BaseReportForm reportForm,
            UserProfile userProfile) throws SystemException, ContractDoesNotExistException {
        boolean planAndInvestmentReplacedMessage = false;
        boolean iccLinkNote = false;
        UserAccess userAccess = null;
		if(userProfile.isTPA()){
			userAccess = UserAccess.TPA;
		}else if (userProfile.isInternalUser()){
			userAccess = UserAccess.INTERNAL_USER;
		}else if (userProfile.getRole().isPlanSponsor()){
			userAccess = UserAccess.PSW;
			}else {
				userAccess = null;
	        }
		FundServiceDelegate fundServiceDelegate = FundServiceDelegate.getInstance();
    Access404a5 contractAccess = fundServiceDelegate.get404a5Permissions(EnumSet.of(
            Facility._404A5_PLAN_AND_INVESTMENT_NOTICE,
            Facility.IMPORTANT_PLAN_INFORMATION_ADDENDUM_TEMPLATE,
            Facility.INVESTMENT_COMPARATIVE_CHART,
            Facility.PARTICIPANT_FUND_CHANGE_NOTICE_TEMPLATE,
            Facility.PARTICIPANT_STATEMENT_FEES_TOOL,
            Facility.IMPORTANT_PLAN_INFORMATION_HYPOTHETICAL_TOOL,
            Facility._404a5_NOTICE_INFO),userProfile.getCurrentContract().getContractNumber(),userAccess);
        Qualification piNoticeQual = contractAccess
                .getAccess(Facility._404A5_PLAN_AND_INVESTMENT_NOTICE);
        Qualification iccQual = contractAccess.getAccess(Facility.INVESTMENT_COMPARATIVE_CHART);
        Contract currentContract = userProfile.getCurrentContract();
        if (piNoticeQual != null
                && piNoticeQual.getTemporarilyMissingInformation().contains(
                        MissingInformation.ICC_CONTACT)
                || iccQual != null
                && iccQual.getTemporarilyMissingInformation().contains(
                        MissingInformation.ICC_CONTACT)) {
            iccLinkNote = true;
        }
        if ((validateProductRestriction(currentContract)
                || validateContractRestriction(currentContract)
                || validateBuisnessContractStatus(currentContract)) && iccLinkNote) {
            planAndInvestmentReplacedMessage = true;
        }
        return planAndInvestmentReplacedMessage;
    }

    /**
     * Returns a byte array that contains the SPH PDF document.
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws SystemException
     */
    public static byte[] doGetSphPdf( BaseReportForm reportForm,
            HttpServletRequest request, HttpServletResponse response, UserProfile userProfile)
            throws SystemException {
        final Logger logger = Logger.getLogger(BaseReportController.class);
        byte[] downloadData = null;
        final Integer contractId = userProfile.getCurrentContract().getContractNumber();
        Location location = Location.valueOfForAbbreviation(CommonEnvironment.getInstance()
                .getSiteLocation());
        String headerFooterImagePath = null;
        java.net.URL url = NoticeManagerUtility.class
                .getResource(CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX
                        + CommonConstants.HEADER_FOOTER_IMAGE);
        if (url != null) {
            headerFooterImagePath = url.toExternalForm();
        }
        if (logger.isDebugEnabled()) {
            logger.info("Header Footer Image path for the PH PDF is " + headerFooterImagePath);
        }
        downloadData = ContractServiceDelegate.getInstance().generateSphPdf(contractId, location,
                headerFooterImagePath);
        if (downloadData != null && downloadData.length > 0) {
            return downloadData;
        }
        return null;
    }
    
    /**
     * Get the notice Manager permission to display tabs
     * 
     * @param reportData
     * @param userProfile
     * @throws SystemException
     */
    public static boolean getNoticeManagerUserAccessPermissionTab(UserProfile userProfile) throws SystemException {
        boolean isTPAUser = userProfile.isTPA();
        boolean isTPAUserManager = userProfile.isTpaUserManager();
        boolean isExternalUser = userProfile.getRole().isExternalUser();
        boolean isNoticeManagerTermsofUseConsent = false;
        String termsOfUseCode = PlanNoticeDocumentServiceDelegate.getInstance().
        								getTermsAndAcceptanceInd(new BigDecimal(userProfile.getPrincipal().getProfileId()));
        if (Constants.YES.equalsIgnoreCase(termsOfUseCode) || "".equalsIgnoreCase(termsOfUseCode)) {
            isNoticeManagerTermsofUseConsent = true;
        }
        Contract contract = userProfile.getCurrentContract();
        boolean noticeManagerAccessPermission = true;
        if ((isExternalUser && !userProfile.isNoticeManagerAccessAllowed())
        		||(!isNoticeManagerTermsofUseConsent && (isExternalUser || isTPAUser ||isTPAUserManager))
                || Contract.STATUS_CONTRACT_DISCONTINUED.equals(contract.getStatus())) {
            noticeManagerAccessPermission = false;
        } 
        return noticeManagerAccessPermission;
    }
    
    /**
     * Get the notice Manager permission to display tabs
     * 
     * @param reportData
     * @param userProfile
     * @throws SystemException
     */
    public static boolean getNoticeManagerTabSelection(UserProfile userProfile,HttpServletRequest request,BaseReportForm reportForm) throws SystemException {
    	boolean orderStatusTab =  true;
    	boolean buildYourPackageTab =  true;
    	boolean buildYourPackageNPTab =  true;
    	boolean uploadAndShareTab =  true;
    	  UploadSharedNoticeManagerForm uploadshareform = (UploadSharedNoticeManagerForm) reportForm;
    	try {
			if(validateContractRestriction(userProfile.getCurrentContract()) 
						|| validateProductRestriction(userProfile.getCurrentContract())
						|| validateDIStatus(userProfile.getCurrentContract(), userProfile.getRole())){
				uploadAndShareTab = false;
				orderStatusTab = false;
				buildYourPackageTab = false;
				buildYourPackageNPTab = false;
			}
			String tempdiDuration = Constants.DI_DURATION_6_MONTH;
			UserRole role = userProfile.getRole();
	        if (role instanceof InternalUser || role instanceof ThirdPartyAdministrator) {
	            tempdiDuration = Constants.DI_DURATION_24_MONTH;
	        } else {
	            tempdiDuration = Constants.DI_DURATION_6_MONTH;
	        }
	        int diDuration = Integer.parseInt(tempdiDuration);
	        Contract contractDetails = null;
	        try {
	            contractDetails = ContractServiceDelegate.getInstance().getContractDetails(
	            		userProfile.getCurrentContract().getContractNumber(), diDuration);
	        } catch (ContractNotExistException contractNotExistException) {
	        	throw new SystemException (contractNotExistException, "Failed to retrieve contract details");
	        }
			if(Contract.STATUS_CONTRACT_DISCONTINUED.equals(contractDetails.getStatus())){
				buildYourPackageTab = false;
			}
		} catch (ContractDoesNotExistException e) {
			throw new SystemException (e, "Failed to retrieve contract details");
		}
		
		Integer customNoticeCount = PlanNoticeDocumentServiceDelegate.getInstance().
											checkCustomNoticeDocumentCount(userProfile.getCurrentContract().getContractNumber());
			get404a5JHDocumentAccessPermission(userProfile,reportForm);
			NoticeManagerUtility.getPlanHighlightDocument(userProfile, reportForm);
			List<PlanNoticeDocumentVO> jhDocumentCount = addJhNoticeDocumentVO(userProfile, reportForm, true);
			if(customNoticeCount==0 && (jhDocumentCount == null || (jhDocumentCount != null && jhDocumentCount.size() == 0)) ){
				buildYourPackageTab = false;
			}
			boolean accessPermission = getNoticeManagerUserAccessPermissionTab(userProfile);
			if(!accessPermission ){
				buildYourPackageNPTab = false;
			}
		uploadshareform.setUploadAndShareTab(uploadAndShareTab);
		uploadshareform.setBuildYourPackageTab(buildYourPackageTab);
		uploadshareform.setOrderStatusTab(orderStatusTab);
		uploadshareform.setBuildYourPackageNPTab(buildYourPackageNPTab);
		
        return uploadAndShareTab;
    }

	/**
	 * This method will check the twelve month rule for the Document
	 * 
	 * @param postToPptInd
	 * @param planNoticeDocumentChangeHistoryVO
	 * @return
	 */
	public static boolean checkTwelveMonthRuleForDocument(String postToPptInd,
			PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO) throws SystemException {
		if (null != postToPptInd && !postToPptInd.isEmpty() && postToPptInd.equalsIgnoreCase(Constants.YES)
				&& null != planNoticeDocumentChangeHistoryVO
				&& null != planNoticeDocumentChangeHistoryVO.getChangedDatePlusOneYear() && StringUtils
						.isNotBlank(String.valueOf(planNoticeDocumentChangeHistoryVO.getChangedDatePlusOneYear()))) {
			int compareDates = planNoticeDocumentChangeHistoryVO.getChangedDatePlusOneYear().compareTo(new Date());
			if (compareDates < 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method will return the available date for the Document
	 * 
	 * @param changedDatePlusOneYear
	 * @return
	 */
	public static String getDocAvailableUntilDate(Timestamp changedDatePlusOneYear) {
		String docAvailableUntilDate = null;
		String pattern = "MMM dd, yyyy";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		if (null != changedDatePlusOneYear && StringUtils.isNotBlank(String.valueOf(changedDatePlusOneYear))) {
			docAvailableUntilDate = formatter.format(changedDatePlusOneYear.toLocalDateTime());
			return docAvailableUntilDate;
		}
		return docAvailableUntilDate;
	}
}
