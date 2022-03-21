package com.manulife.pension.ps.web.investment;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.fundcheck.model.FundCheckDocFileRequest;
import com.manulife.pension.fundcheck.model.FundCheckDocFileResponse;
import com.manulife.pension.fundcheck.model.FundCheckDocListRequest;
import com.manulife.pension.fundcheck.model.FundCheckDocListResponse;
import com.manulife.pension.fundcheck.model.FundCheckDocumentInfo;
import com.manulife.pension.lp.bos.ereports.common.ContractDocumentNotFoundException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.InvestmentPlatformUpdateVO;
import com.manulife.pension.service.fund.valueobject.InvestmentPlatformUpdateVO.UserIdType;
import com.manulife.pension.util.content.GenericException;

/**
 * This is the action class for the FundCheck page. This contains the logic to
 * display the fund check pdfs.
 * 
 * @author Ilamparithi
 * 
 */
@Controller
@RequestMapping( value = "/fundCheck")
@SessionAttributes({"fundCheckForm"})
public class FundCheckController extends PsAutoController {
	
	@ModelAttribute("fundCheckForm") 
	public  FundCheckForm populateForm() 
	{
		return new  FundCheckForm();}
	

	
	public static Map<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/investment/fundCheck.jsp"); 
		forwards.put("default","/investment/fundCheck.jsp");
		}
	

	boolean isDebugEnabled = logger.isDebugEnabled();
	boolean isErrorEnabled = logger.isEnabledFor(Level.ERROR);
	EReportsServiceDelegate delegate = EReportsServiceDelegate.getInstance();
	public static final String FUNDCHECK_LABEL = "FundCheck<sup>®</sup>";
	private static final String EMPTY_LAYOUT_ID = "/registration/authentication.jsp";

	/**
	 * Constructor
	 */
	public FundCheckController() {
		super(FundCheckController.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAutoAction#doDefault
	 * (org.apache.struts.action.ActionMapping,
	 * com.manulife.pension.platform.web.controller.AutoForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	
	@RequestMapping( method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("fundCheckForm") FundCheckForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {

		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
        		request.setAttribute(Constants.LAYOUT_BEAN, LayoutBeanRepository
    					.getInstance().getPageBean(EMPTY_LAYOUT_ID));
    			request.setAttribute(Constants.TECHNICAL_DIFFICULTIES, true);
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             request.setAttribute(Constants.LAYOUT_BEAN, LayoutBeanRepository
 					.getInstance().getPageBean(EMPTY_LAYOUT_ID));
 			request.setAttribute(Constants.TECHNICAL_DIFFICULTIES, true);
             return forwards.get("input");
        	}
        } 
		
		if (isDebugEnabled) {
			logger.debug("entry -> doDefault");
		}
		try {
			setFundCheckInfo( actionForm, request);
		} catch (ServiceUnavailableException sue) {
			if(isErrorEnabled){
				logger.error("EReports service not available.", sue);
			}
			List<GenericException> errors = new ArrayList<GenericException>();
			errors.add(new GenericException(ErrorCodes.REPORT_SERVICE_UNAVAILABLE));
			setErrorsInRequest(request, errors);
			request.setAttribute(Constants.TECHNICAL_DIFFICULTIES, true);
			return forwards.get("input");
		} catch (Exception e) {
			if(isErrorEnabled)
				logger.error("Error in retrieving FundCheck documents info.", e);
			
			throw new SystemException(e,
					"Error in retrieving FundCheck documents info.");
		}
		if (isDebugEnabled) { 
			logger.debug("exit -> doDefault");
		}
		return forwards.get("input");
	}

	/**
	 * This method contains the logic to retrieve and open the FundCheck PDF.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return an ActionForward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	
	@RequestMapping(params={"action=openPDF"} , method =  {RequestMethod.GET}) 
	public String doOpenPDF (@Valid @ModelAttribute("fundCheckForm") FundCheckForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException, SystemException {
	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             request.setAttribute(Constants.LAYOUT_BEAN, LayoutBeanRepository
 					.getInstance().getPageBean(EMPTY_LAYOUT_ID));
 			request.setAttribute(Constants.TECHNICAL_DIFFICULTIES, true);
             return forwards.get("input");
        	}
        } 
		
		if (isDebugEnabled) {
			logger.debug("entry -> doOpenPDF");
		}
		List<GenericException> errors = new ArrayList<GenericException>();
		try {
			
			if(StringUtils.isNotBlank(form.getSelectedSeason()) && StringUtils.isNotBlank(form.getSelectedYear())){
				FundCheckDocFileResponse fileResponse = getFundCheckPDF(
						form, request);
				UserProfile userProfile=SessionHelper.getUserProfile(request);
				logInvestmentPlatformUpdateDetails(userProfile, fileResponse.getFileName());
				if(fileResponse != null){
					response.setHeader("Cache-Control", "no-cache, no-store");
			        response.setHeader("Pragma", "no-cache");
					response.setContentType(Constants.MIME_TYPE_PDF);
					response.setContentLength(fileResponse.getLength());
					OutputStream out = response.getOutputStream();
					out.write(fileResponse.getReportFragment());
					out.close();
				}else{
					errors.add(new GenericException(ErrorCodes.REPORT_FILE_NOT_FOUND));
				}
			}else{
				errors.add(new GenericException(ErrorCodes.REPORT_FILE_NOT_FOUND));
			}
		} catch (ServiceUnavailableException sue) {
			if(isErrorEnabled)				
				logger.error("EReports service not available.", sue);
			errors.add(new GenericException(ErrorCodes.REPORT_FILE_NOT_FOUND));
		} catch (Exception e) {
			if(isErrorEnabled)
				logger.error("Error in Retrieving PDF.", e);
			errors.add(new GenericException(ErrorCodes.REPORT_FILE_NOT_FOUND));
		}

		String forward = null;
		if (errors.size() > 0) {
			setErrorsInRequest(request, errors);
			request.setAttribute(Constants.LAYOUT_BEAN, LayoutBeanRepository
					.getInstance().getPageBean(EMPTY_LAYOUT_ID));
			forward = forwards.get(Constants.SECONDARY_WINDOW_ERROR_FORWARD);
		} 
		
		if (isDebugEnabled) {
			logger.debug("exit -> doOpenPDF");
		}
		return forward;
	}

	/**
	 * This method sets the FundCheck PDF info to the form. This will be
	 * executed when fundcheck page is loaded for the first time.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @throws ServiceUnavailableException
	 * @throws SystemException
	 */
	private void setFundCheckInfo( AutoForm form,
			HttpServletRequest request) throws ServiceUnavailableException,
			SystemException {
		if (isDebugEnabled) {
			logger.debug("entry -> setFundCheckInfo");
		}
		FundCheckForm fundCheckForm = (FundCheckForm) form;
		UserProfile userProfile = getUserProfile(request);
		int contractNumber = 0;
		if(userProfile != null) {
			contractNumber = userProfile.getCurrentContract().getContractNumber();			
			FundCheckDocListRequest fundCheckRequest = new FundCheckDocListRequest();
			fundCheckRequest.setContractNumber(String.valueOf(contractNumber));
			FundCheckDocListResponse fundCheckResponse = delegate
					.getContractFundCheckDocList(fundCheckRequest);
			FundCheckDocumentInfo[] fundCheckArray = fundCheckResponse
					.getFundCheckDocList();
			Map<String, String> seasonsMap = EnvironmentServiceDelegate.getInstance(
						Environment.getInstance().getAppId()).getFundLaunchSeasons(Constants.FUND_LAUNCH_SEASON);
			if (fundCheckArray.length == 0) {
				fundCheckForm.setFundCheckPDFAvailable(false);
			} else {
				for (int i = 0; i < fundCheckArray.length; i++) {
					FundCheckDocumentInfo info = fundCheckArray[i];
					FundCheckDocumentEntity entity = new FundCheckDocumentEntity();
					entity.setSeason(StringUtils.trim(info.getSeason()));
					entity.setYear(StringUtils.trim(info.getYear()));
					entity.setParticipantNoticeInd(StringUtils.trim(info.getParticipantNoticeInd()));
					try {
						entity.setTitle(info.getFundCheckTitle(seasonsMap));
					} catch (Exception e) {
						if(isErrorEnabled)
							logger.error("The Season value in FundCheckDocumentInfo is invalid.");
					}
					entity.setParticipantNoticeTitle(info.getParticipantNoticeTitle(seasonsMap));
					if (info.isCurrent()) {
						fundCheckForm.setCurrentFundCheck(entity);
					}else {
						fundCheckForm.setPreviousFundCheck(entity);
					}
				}
				fundCheckForm.setPdfCount(fundCheckArray.length);
			}			
		}else{
			if(isErrorEnabled)
				logger.error("Error in retrieving userProfile info.");
			throw new SystemException("Error in retrieving userProfile info.");
		}

		if (isDebugEnabled) {
			logger.debug("exit -> setFundCheckInfo");
		}
	}

	/**
	 * This method retrieves the fundcheck pdf from eReports.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @return a FundCheckDocFileResponse
	 * @throws ServiceUnavailableException
	 * @throws SystemException
	 * @throws ContractDocumentNotFoundException
	 */
	private FundCheckDocFileResponse getFundCheckPDF( 	AutoForm form, HttpServletRequest request)
			throws ServiceUnavailableException, SystemException{
		if (isDebugEnabled) {
			logger.debug("entry -> getFundCheckPDF");
		}
		FundCheckForm fundCheckForm = (FundCheckForm) form;
		UserProfile userProfile = getUserProfile(request);
		int contractNumber = 0;
		if(userProfile != null) {
			Contract contract = userProfile.getCurrentContract();
			if(contract != null) {
				contractNumber = contract.getContractNumber();
			}
			String contractNo = String.valueOf(contractNumber);
			FundCheckDocumentInfo info = new FundCheckDocumentInfo();
			info.setContractNumber(contractNo);
			info.setSeason(fundCheckForm.getSelectedSeason());
			info.setYear(fundCheckForm.getSelectedYear());
			if(null!=fundCheckForm.getSelectedNotice() && fundCheckForm.getSelectedNotice().equalsIgnoreCase("PPT")) {
				info.setLanguage(fundCheckForm.getSelectedLanguage());
				info.setParticipantNoticeInd(fundCheckForm.getSelectedParticipantNotice());
			}else {
				info.setLanguage(null);
				info.setParticipantNoticeInd("N");
			}
			FundCheckDocFileRequest fundCheckFileRequest = new FundCheckDocFileRequest();
			fundCheckFileRequest.setContractNumber(contractNo);
			fundCheckFileRequest
					.setReportKeys(new FundCheckDocumentInfo[] { info });
			FundCheckDocFileResponse fundCheckDocFileResponse = null;
			if(validateFileName(contractNo, fundCheckForm.getSelectedSeason(), fundCheckForm.getSelectedYear())){
				fundCheckDocFileResponse = delegate.getContractFundCheckDocFile(fundCheckFileRequest);
			}
			if (isDebugEnabled) {
				logger.debug("exit -> getFundCheckPDF");
			}
			return fundCheckDocFileResponse;
			
		}else{
			if(isErrorEnabled)
				logger.error("Error in retrieving userProfile info.");
			throw new SystemException("Error in retrieving userProfile info.");
		}
	}
	
	public boolean validateFileName(String contractNumber, String selectedSeason, String selectedYear) throws ServiceUnavailableException, SystemException{
		
		boolean validFile = false;
		FundCheckDocListRequest fundCheckRequest = new FundCheckDocListRequest();
		fundCheckRequest.setContractNumber(contractNumber);
		FundCheckDocListResponse fundCheckResponse = delegate
				.getContractFundCheckDocList(fundCheckRequest);
		FundCheckDocumentInfo[] fundCheckArray = fundCheckResponse
				.getFundCheckDocList();
		if (fundCheckArray.length > 0) {
			for (int i = 0; i < fundCheckArray.length; i++) {
				FundCheckDocumentInfo info = fundCheckArray[i];
				if(info.getSeason().trim().equalsIgnoreCase(selectedSeason) && info.getYear().trim().equals(selectedYear)){
					validFile = true;
					break;
				}
			}
		}
		return validFile;
	}
	
	/**
	 * 
	 * @param userProfile
	 * @param fileName
	 * @throws SystemException
	 */
	public void logInvestmentPlatformUpdateDetails(UserProfile userProfile, String fileName) throws SystemException {

		InvestmentPlatformUpdateVO ipuDetails = new InvestmentPlatformUpdateVO();

		ipuDetails.setContractId(userProfile.getCurrentContract().getContractNumber());
		if(userProfile.getPrincipal().getRole().toString().equals(Constants.APPLICATION_ID_FOR_TPA)) {
			ipuDetails.setApplicationId(Constants.APPLICATION_ID_FOR_TPA);
		}else {
			ipuDetails.setApplicationId(Constants.APPLICATION_ID);
		}
		ipuDetails.setEmail(userProfile.getEmail());
		if (fileName.endsWith("SP.pdf")) {
			ipuDetails.setFunctionCode(Constants.PATCIPENT_NOTICE_SPANISH);
		} else if (fileName.endsWith("EN.pdf")) {
			ipuDetails.setFunctionCode(Constants.PATCIPENT_NOTICE_ENGLISH);
		}else {
			ipuDetails.setFunctionCode(Constants.PS_NOTICE);
		}
		ipuDetails.setProposalNo(0);
		ipuDetails.setReferenceText(fileName);
		if (userProfile.isInternalUser()) {
			ipuDetails.setUserIdType(UserIdType.getIdType(UserIdType.INTERNALUSER));
			ipuDetails.setUserFirstName(userProfile.getPrincipal().getFirstName());
			ipuDetails.setUserLastName(userProfile.getPrincipal().getLastName());
			ipuDetails.setUserId(userProfile.getPrincipal().getUserName());
		} else {
			ipuDetails.setUserIdType(UserIdType.getIdType(UserIdType.EXTERNALUSER));
			ipuDetails.setUserFirstName(userProfile.getPrincipal().getFirstName());
			ipuDetails.setUserLastName(userProfile.getPrincipal().getLastName());
			ipuDetails.setUserId(String.valueOf(userProfile.getPrincipal().getProfileId()));
		}
		ipuDetails.setUserRoleCode(userProfile.getPrincipal().getRole().toString());
		ipuDetails.setRequestedTime(new Date().getTime());
		ipuDetails.setOosOrderNumber(0);
		ipuDetails.setLogIdType("CN");
		ipuDetails.setProducerCode(0);
		FundServiceDelegate.getInstance().logFunctionRequestInfo(ipuDetails);
	}
	
	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations as part of the CL#137697.
	 */
	/*@SuppressWarnings("rawtypes")
	public Collection doValidate( Form form,
			HttpServletRequest request) {
		Collection penErrors = PsValidation.doValidatePenTestAutoAction(form,
				mapping, request, CommonConstants.INPUT);
		if (penErrors != null && penErrors.size() > 0) {
			request.getSession().removeAttribute(PsBaseAction.ERROR_KEY);
			request.removeAttribute(PsBaseAction.ERROR_KEY);
			request.setAttribute(Constants.LAYOUT_BEAN, LayoutBeanRepository
					.getInstance().getPageBean(EMPTY_LAYOUT_ID));
			request.setAttribute(Constants.TECHNICAL_DIFFICULTIES, true);
			return penErrors;
		}
		return super.doValidate( form, request);
	}*/
		
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}
