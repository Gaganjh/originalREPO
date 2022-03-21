package com.manulife.pension.ps.web.investment;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.service.fund.valueobject.Access404a5;
import com.manulife.pension.service.fund.valueobject.FeeDisclosureUserDetails;
import com.manulife.pension.service.fund.valueobject.UserAccess;
import com.manulife.pension.service.fund.valueobject.FeeDisclosureUserDetails.UserIdType;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.ServiceLogRecord;

/**
 * This is the action class for the ICCPDFGeneration page. This contains the logic to
 * display the ICC pdfs.
 * 
 * @author Rajesh Rajendran
 * 
 */
abstract public class IccReportController extends PsAutoController {
	private static final String PDF_GENERATED = "pdfGenerated";
	private static final String PDF_NOT_GENERATED = "pdfNotGenerated";
	/* Logger */
	private static final String EMPTY_LAYOUT_ID = "/registration/authentication.jsp";
	private static final String TOP_LOGO_IMAGE_PATH = "/assets/unmanaged/images/JH_404_LOGO_98X40.jpg";
	private static final String BOTTOM_LOGO_IMAGE_PATH = "/assets/unmanaged/images/JH_blue.jpg";	
	
	/**
	 * Constructor
	 */
	public IccReportController(Class<?> clazz) {
		super(clazz);
	}
	/**
	 * Method to validate the request
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
	public String doValidate(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		
		return validateRequest( request);
	}

	/**
	 * Method to validate the whether the given request for ICC document is allowed or not
	 * @param mapping
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	private String validateRequest(
			HttpServletRequest request) throws SystemException {
		
		Access404a5 contractAcc = getUserProfile(request).getAccess404a5();

		// For bookMark issue: we need to redirect to home page, if it is accessed
		if (! isDocumentAllowed(contractAcc)) {
			//return mapping.findForward(Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT);
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
		return null;
	}
	
	abstract boolean isDocumentAllowed(Access404a5 access);
	
	/**
	 * Method to build the IccContractDetails object for the given request
	 * 
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	private int getContractId(HttpServletRequest request) throws SystemException{
		Integer contractId = 0;
		UserProfile userProfile = getUserProfile(request);
		if(userProfile == null 
				|| userProfile.getCurrentContract() == null) {
			//MRL logging
			logGenerateIccPdf("User Profile Object is null", 
						"Generate ICC PDF", "DoExecute");
        	throw new SystemException("Not a valid contract");
		}
		contractId = userProfile.getCurrentContract().getContractNumber();
		
		return contractId;
		
	}
	/**
	 * Method to get the ICC pdf based on the information provided
	 * 
	 * @param mapping
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	@Override
	public String doDefault(
			AutoForm actionForm,ModelMap model,HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException {
		
		if(validateRequest( request) != null){
			//return mapping.findForward(Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT);
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
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
			BaseReportController.streamDownloadData(request, response,
					"application/pdf", getPdfFileName(getContractId(request)), download);
		}	
		 
		return null;
	}
	
	abstract String getPdfSessionCacheAttributeName();
	abstract String getPdfFileName(int contractId);
	
	/**
	 * Method to generate the ICC PDF
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
			java.net.URL urlTopLogo = IccReportController.class.getResource(TOP_LOGO_IMAGE_PATH);
			java.net.URL urlBottomLogo = IccReportController.class.getResource(BOTTOM_LOGO_IMAGE_PATH);
			if(urlTopLogo != null){
				topLogoImagePath = urlTopLogo.toExternalForm();
			} 
			if(urlBottomLogo != null){
				bottomLogoImagePath = urlBottomLogo.toExternalForm();
			}  
			UserProfile userProfile = getUserProfile(request);
			
			UserAccess access = null;
			if(userProfile.isTPA()){
				access = UserAccess.TPA;
			}else if (userProfile.isInternalUser()){
				access = UserAccess.INTERNAL_USER;
			}else if (userProfile.getRole().isPlanSponsor()){
				access = UserAccess.PSW;
			}
			
			
			FeeDisclosureUserDetails userDetails = getfeeDisclosureUserDetails(userProfile);
			

			downloadData = getPdfFileStream(getContractId(request), topLogoImagePath, bottomLogoImagePath, userDetails, access);

		}catch(Exception e){
			logger.error("Exception occured while generating the PDF :: "+e.getMessage(), e);
			return null;
		}
		return downloadData;
	}
	
	private FeeDisclosureUserDetails getfeeDisclosureUserDetails(UserProfile userProfile) {
		
		FeeDisclosureUserDetails userDetails = new FeeDisclosureUserDetails();
		
		userDetails.setUserRoleCode(userProfile.getPrincipal().getRole().toString());
		userDetails.setApplicationId("PSW");
		userDetails.setRequestedTime(new Date().getTime());
		if(userProfile.isInternalUser()){
			userDetails.setUserIdType(UserIdType.getIdType(UserIdType.INTERNALUSER));
			userDetails.setUserId(String.valueOf(userProfile.getPrincipal().getUserName()));
			userDetails.setUserFirstName(userProfile.getPrincipal().getFirstName());
			userDetails.setUserLastName(userProfile.getPrincipal().getLastName());
		} else {
			userDetails.setUserIdType(UserIdType.getIdType(UserIdType.EXTERNALUSER));
			userDetails.setUserId(String.valueOf(userProfile.getPrincipal().getProfileId()));
		}
		
		
		
		return userDetails;
	}
	abstract byte[] getPdfFileStream(int contractId, String topLogoPath, String bottomLogoPath, 
			FeeDisclosureUserDetails context, UserAccess user) throws Exception;
	
	
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
			response.getWriter().write(PDF_NOT_GENERATED);
		}else{
			response.getWriter().write(PDF_GENERATED);
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
		errors.add(new GenericException(ErrorCodes.REPORT_FILE_NOT_FOUND));
		setErrorsInRequest(request, errors);
		request.setAttribute(Constants.LAYOUT_BEAN, LayoutBeanRepository
				.getInstance().getPageBean(EMPTY_LAYOUT_ID));
		//return mapping.findForward(Constants.SECONDARY_WINDOW_ERROR_FORWARD);
		return Constants.SECONDARY_WINDOW_ERROR_FORWARD;
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
	 * Class to handle the byte array request object
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
