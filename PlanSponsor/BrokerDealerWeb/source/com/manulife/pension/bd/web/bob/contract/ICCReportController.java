package com.manulife.pension.bd.web.bob.contract;

import static com.manulife.pension.bd.web.BDConstants.BOB_PAGE_FORWARD;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.Access404a5;
import com.manulife.pension.service.fund.valueobject.FeeDisclosureUserDetails;
import com.manulife.pension.service.fund.valueobject.Access404a5.Facility;
import com.manulife.pension.service.fund.valueobject.FeeDisclosureUserDetails.UserIdType;
import com.manulife.pension.service.fund.valueobject.UserAccess;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.ServiceLogRecord;

/**
 * This is the action class for the ICCPDFGeneration page. This contains the
 * logic to display the ICC pdfs.
 * 
 * @author Saravanan Narayanasamy
 * 
 */
abstract public class ICCReportController extends BaseAutoController {
	
	private static final String TOP_LOGO_IMAGE_PATH = "/assets/unmanaged/images/JH_404_LOGO_98X40.jpg";
	private static final String BOTTOM_LOGO_IMAGE_PATH = "/assets/unmanaged/images/JH_blue.jpg";	
	
	private static String GENERATED_ICC ="isReportGenerated";

	/**
	 * Constructor
	 */
	public ICCReportController(Class<?> clazz) {
		super(clazz);
		logger = Logger.getLogger(ICCReportController.class);
	}

	/**
	 * Method to validate whether the request is valid or not
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
	public String preExecute(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		
		return validateRequest( request);
	}
	/**
	 * Method to validate the whether the given request for ICC document is allowed or not
	 * 
	 * @param mapping
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	private String validateRequest(
			HttpServletRequest request) throws SystemException {

		// For bookMark issue
        int contractId =
                BDSessionHelper
                .getBobContext(request)
                .getCurrentContract()
                .getContractNumber() ;
		
		UserAccess access = UserAccess.FRW;
		
		Access404a5 contractAcc =
		        FundServiceDelegate
		        .getInstance()
		        .get404a5Permissions(
		                EnumSet.of(
		                        Facility._404A5_PLAN_AND_INVESTMENT_NOTICE,
		                        Facility.INVESTMENT_COMPARATIVE_CHART),
		                contractId,
		                access);

		// For bookMark issue: we need to redirect to home page, if it is accessed
		if (! isDocumentAllowed(contractAcc)) {
			//return mapping.findForward(BOB_PAGE_FORWARD);
			return BOB_PAGE_FORWARD;
		}
		return null;
	}
	
   abstract boolean isDocumentAllowed(Access404a5 access);
	
	/**
	 * Method to build the IccContractDetails object based on the request object
	 * 
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	private int getContractId(HttpServletRequest request) throws SystemException{
		
		BobContext bobContext =BDSessionHelper.getBobContext(request) ;
		
		if (bobContext == null) {
			// MRL logging
			logGenerateIccPdf("BOB is null", "Generate ICC PDF",
					"DoExecute");
			throw new SystemException("Not a valid contract");
		}

		Contract currentContract = bobContext.getCurrentContract(); 
		return currentContract.getContractNumber();
		
	}
	/**
	 * Method build validate the initial request and forward to BOB page, if the request is not valid
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
	@Override
	public String doDefault(
			AutoForm actionForm,ModelMap model, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		
		if(validateRequest( request) != null){
			//return mapping.findForward(BOB_PAGE_FORWARD);
			return BOB_PAGE_FORWARD;
		}
		HttpSession session = request.getSession(false);
		
		byte [] download = null;
		if(session.getAttribute(getPdfSessionCacheAttributeName()) != null) {
			download = ((Handle)session.getAttribute(getPdfSessionCacheAttributeName())).getByteArray();
			session.setAttribute(getPdfSessionCacheAttributeName(), null);
		}else {
			download =  generatePDF( request);
		}
		
		if (download != null && download.length > 0) {
			BaseReportController.streamDownloadData(
			        request,
			        response,
					"application/pdf",
					getPdfFileName(getContractId(request)), download);
		}	
		 
		return null;
	}
	
    abstract String getPdfSessionCacheAttributeName();
    abstract String getPdfFileName(int contractId);

	/**
	 * Method to generate the actual PDF generation, if the request is valid. It will return if the we find any exceptions
	 * while retriving the PDF document
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	private byte[] generatePDF(	HttpServletRequest request) throws SystemException  {

		byte[] downloadData = null;

		try{  	
			String topLogoImagePath = null; 
			String bottomLogoImagePath = null;
			java.net.URL urlTopLogo = ICCReportController.class.getResource(TOP_LOGO_IMAGE_PATH);
			java.net.URL urlBottomLogo = ICCReportController.class.getResource(BOTTOM_LOGO_IMAGE_PATH);
			if(urlTopLogo != null){
				topLogoImagePath = urlTopLogo.toExternalForm();
			} 
			if(urlBottomLogo != null){
				bottomLogoImagePath = urlBottomLogo.toExternalForm();
			}  

			BobContext bobContext =BDSessionHelper.getBobContext(request) ;	
			
			FeeDisclosureUserDetails userDetails = getUserDetails(bobContext);
			UserAccess user = UserAccess.FRW;
			
			downloadData = getPdfFileStream(
			        getContractId(request),
			        topLogoImagePath,
			        bottomLogoImagePath,
			        userDetails, user);

		}catch(Exception e){
			logger.error("Exception occured while generating the PDF :: "+e.getMessage(), e);
			return null;
		}
		return downloadData;
	}
	
	 private FeeDisclosureUserDetails getUserDetails(BobContext userProfile) {
			
		 FeeDisclosureUserDetails userDetails = new FeeDisclosureUserDetails();
			
			BDUserProfile user = userProfile.getUserProfile();
			
			userDetails.setUserRoleCode(user.getRole().getRoleType().getUserRoleCode());
			userDetails.setApplicationId("FRW");
			userDetails.setRequestedTime(new Date().getTime());
			if(userProfile.getUserProfile().isInternalUser()){
				userDetails.setUserIdType(UserIdType.getIdType(UserIdType.INTERNALUSER));
				userDetails.setUserId(String.valueOf(user.getBDPrincipal().getUserName()));
				userDetails.setUserFirstName(user.getBDPrincipal().getFirstName());
				userDetails.setUserLastName(user.getBDPrincipal().getLastName());
			} else {
				userDetails.setUserIdType(UserIdType.getIdType(UserIdType.EXTERNALUSER));
				userDetails.setUserId(String.valueOf(user.getBDPrincipal().getProfileId()));
			}
			
			return userDetails;
		} 
	
    abstract byte[] getPdfFileStream(int contractId, String topLogoPath, String bottomLogoPath, FeeDisclosureUserDetails context, UserAccess user) throws Exception;

	/**
	 * This method will check whether ICC  is generated or not.
	 * If report generation is complete then response status code is set as
	 * 400(request fails) This causes the AJAX request to fail and in the failure
	 * event waiting message will be closed.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 * @throws IOException 
	 */
	public String doCheckPdfReportGenerated(
			AutoForm form, HttpServletRequest request,
			HttpServletResponse response) throws SystemException, IOException {
		
		HttpSession session = request.getSession(false);
		
		byte[] genereatedPDF = generatePDF(request);

		if(genereatedPDF == null){
			response.getWriter().write("pdfNotGenerated");
		}else{
			response.getWriter().write("pdfGenerated");
			session.setAttribute(getPdfSessionCacheAttributeName(), new Handle(genereatedPDF));
		}
		
		return null;
	}
	
	/**
	 * This method will check whether ICC  is generated or not.
	 * If report generation is complete then response status code is set as
	 * 400(request fails) This causes the AJAX request to fail and in the failure
	 * event waiting message will be closed.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	public String doOpenErrorPdf(
			AutoForm form, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		List<GenericException> errors = new ArrayList<GenericException>();
		errors.add(new GenericException(CommonErrorCodes.REPORT_FILE_NOT_FOUND));
		setErrorsInRequest(request, errors);
		//return mapping.findForward(BDConstants.SECONDARY_WINDOW_ERROR_FORWARD);
		return BDConstants.SECONDARY_WINDOW_ERROR_FORWARD;
	}
	
	
	
 	/**
 	 * To mrl log on generate ICC pdf 
 	 * @param profileId
 	 * @param contractNumber
 	 * @param functionName
 	 * @param loggingPoint
 	 */
	private static void logGenerateIccPdf(String userProfile, 
			String functionName, String loggingPoint) {
		ServiceLogRecord record = new ServiceLogRecord(functionName);
		Logger interactionLog = Logger.getLogger(ServiceLogRecord.class);

		StringBuffer logData = new StringBuffer();
		logData.append("User Profile is:[" + userProfile + "]");
		logData.append("Action Taken:[ICC - "
				+ functionName + "]");

		record.setMethodName(loggingPoint);
		record.setData(logData.toString());
		record.setDate(new Date());

		interactionLog.error(record);
	} 
	/**
	 * Class to handle the byte array object in session scope
	 * 
	 * @author arugupu
	 *
	 */
	static class Handle implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		Handle(byte[] array){
			this.byteArray = array;
		}
		
		private byte[] byteArray;

		/**
		 * @return the array
		 */
		public byte[] getByteArray() {
			return byteArray;
		}
	}
}
