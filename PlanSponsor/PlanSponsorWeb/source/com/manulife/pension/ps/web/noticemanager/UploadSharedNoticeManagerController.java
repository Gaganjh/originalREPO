package com.manulife.pension.ps.web.noticemanager;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.content.valueobject.Message;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.documents.model.DocumentFileOutput;
import com.manulife.pension.documents.model.DocumentInfo;
import com.manulife.pension.documents.model.PlanNoticeDocumentInfo;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.platform.web.delegate.PlanNoticeDocumentServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentReportData;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentChangeHistoryVO;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.contract.csf.CsfConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.noticemanager.util.NoticeManagerUtility;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.validator.ValidationError;

/**
 * This is the action class for the Upload Share page. This contains the logic to
 * Display the Custom and JH PlanHighlight Documents and Navigate to Add and Edit pages.
 * 
 * @author Murali Vishwanadula
 * 
 */
@Controller
@RequestMapping(value ="/noticemanager")
@SessionAttributes({"uploadsharedPlandocForm"})

public class UploadSharedNoticeManagerController extends ReportController {
	
	@ModelAttribute("uploadsharedPlandocForm") 
	public UploadSharedNoticeManagerForm populateForm()
	{
		return new UploadSharedNoticeManagerForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/noticemanager/buildYourPackage.jsp");
		forwards.put("default","/noticemanager/uploadAndSharedContractDocument.jsp");
		forwards.put("uploadandsharepage","/noticemanager/uploadAndSharedContractDocument.jsp");
		forwards.put("addnotice","redirect:/do/noticemanager/addcustomplandocument/");
		forwards.put("editnotice","redirect:/do/noticemanager/editCustomPlanDocument/");
		forwards.put("termsandconditions","redirect:/do/noticemanager/termsandconditions/");
		forwards.put("secureHomePage","redirect:/do/home/homePage/");}

	@RequestMapping(value ="/uploadandsharepages/" ,method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("uploadsharedPlandocForm") UploadSharedNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
        	}
        }  
		String forward = super.doDefault(form, request, response);
		postExecute(form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 

	}
	
	/**
	 * Method to get the Custom and JH documents based on the information provided
	 * 
	 * @param mapping
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	 
	public String doCommon(BaseReportForm reportForm, HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {

		boolean planAndInvestmentReplacedMessage = false;
		Timestamp uploadDocTs = null;
		Timestamp uploadDocFiveYearsTs =null;
		UserProfile userProfile = getUserProfile(request);
				UploadSharedNoticeManagerForm actionForm = (UploadSharedNoticeManagerForm) reportForm;
		
		actionForm.setCustomSortInd(false);
		actionForm.setUploadAndSharePageInd(true);
		super.doCommon( reportForm, request, response);
		PlanDocumentReportData reportData = (PlanDocumentReportData) request
				.getAttribute(CommonConstants.REPORT_BEAN);
		actionForm.setReport(reportData);
		if(actionForm.getReport() == null){
			actionForm.setReport(reportData);
		}
		List<PlanNoticeDocumentVO> planVoList = null;
		List<PlanNoticeDocumentVO> planDocVoList = null;
		List<PlanNoticeDocumentVO> planVoJhDocList = new ArrayList<PlanNoticeDocumentVO>();
		List<PlanNoticeDocumentVO> planVoListFiveYears = new ArrayList<PlanNoticeDocumentVO>();
		planVoList = reportData.getCustomPlanNoticeDocuments();
		if(planVoList != null){
		request.setAttribute(Constants.PLAN_VO_LIST_LENGTH, planVoList.size()-1);
		/*for(PlanNoticeDocumentVO planVO:planVoList){
			PlanNoticeDocumentChangeHistoryVO planChangeHistoryVO = planVO.getPlanNoticeDocumentChangeDetail();
			if(planChangeHistoryVO!=null){
				uploadDocTs = planChangeHistoryVO.getChangedDate();
				Calendar calendar = Calendar.getInstance(); 
				SimpleDateFormat formate =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				calendar.setTime(uploadDocTs); 
				calendar.add(Calendar.YEAR, 5);
				uploadDocFiveYearsTs=Timestamp.valueOf(formate.format(calendar.getTime()));
				Timestamp currentTimestamp =  new Timestamp(System.currentTimeMillis());
				if( currentTimestamp.after(uploadDocFiveYearsTs)){
					planVO.setUploadDocFiveYears(true);
				}
				planVoListFiveYears.add(planVO);
			}
		}
		*/
		}
		/*
		if(uploadDocTs!=null){
			reportData.setCustomPlanNoticeDocuments(planVoListFiveYears);
			request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
		}*/
		if((planVoList.size())>1){
			actionForm.setShowActionButon(true);
		}
		else{
			actionForm.setShowActionButon(false);
		}
		
		actionForm.setProfileId(new BigDecimal(userProfile.getPrincipal().getProfileId()));

		try {
			if  (NoticeManagerUtility.validateProductRestriction(userProfile
					.getCurrentContract())
					|| NoticeManagerUtility.validateContractRestriction(userProfile
							.getCurrentContract())
							|| NoticeManagerUtility.validateDIStatus(
									userProfile.getCurrentContract(), userProfile.getRole()))  {
				return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
			}
		} catch (ContractDoesNotExistException ex) {
			throw new SystemException(ex,"Getting contract number" +request.getAttribute(Constants.CONTRACT_NO) +" doesn't existing  "); 
		}
		NoticeManagerUtility.get404a5JHDocumentAccessPermission(userProfile,
				reportForm);
		NoticeManagerUtility.getPlanHighlightDocument(userProfile, 
				reportForm);
		NoticeManagerUtility.getNoticeManagerTabSelection(userProfile, request, reportForm);
		if(!actionForm.getUploadAndShareTab()){
			return Constants.HOMEPAGE_FINDER_FORWARD_REDIRECT;
		}
		reportData.setJhPlanNoticeDocuments(NoticeManagerUtility.addJhNoticeDocumentVO(userProfile, 
				actionForm,false));
		boolean noticeManagerUploadedPermission = NoticeManagerUtility.getNoticeManagerUploadedAccessPermission(reportData, userProfile, reportForm);
		actionForm.setAddDocumentPermission(noticeManagerUploadedPermission);
		// Apply the User permission rule for edit button displayed in upload
		// and share page
		boolean noticeManagerAccessPermission = NoticeManagerUtility
				.getNoticeManagerUserAccessPermission(userProfile);
		try {
			planAndInvestmentReplacedMessage = NoticeManagerUtility.getPlanAndInvestmentNoticeReplaceMessage(reportForm,userProfile);
		} catch (ContractDoesNotExistException e) {
			throw new SystemException(e,"Getting contract number" +request.getAttribute(Constants.CONTRACT_NO) +" doesn't existing  "); 
		}
		actionForm.setShowPlanAndInvestmentReplaceMessage(planAndInvestmentReplacedMessage);
		planDocVoList = reportData.getJhPlanNoticeDocuments();
		if(planDocVoList != null){
		for(PlanNoticeDocumentVO planJhVO:planDocVoList){
			planJhVO.setLastActionedUserName(Constants.JOHN_HANCOCK);
			planVoJhDocList.add(planJhVO);
		}
		}
		reportData.setJhPlanNoticeDocuments(planVoJhDocList);
		request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
		actionForm
		.setNoticeManagerAccessPermissions(noticeManagerAccessPermission);
		actionForm.setUserTermsAndAcceptanceInd(reportData.getTermsOfUseCode().trim());
		actionForm.setInternalUser(userProfile.isInternalUser());
		if(reportData.getCustomPlanNoticeDocuments().size() == 0 && reportData.getJhPlanNoticeDocuments().size() == 0){
			actionForm.setNoDocumentAvailableInd(true);
		}else{
			actionForm.setNoDocumentAvailableInd(false);
		}
		return forwards.get(Constants.UPLOAD_AND_SHARE);
	}

	/** Forward to Add page based on terms and condition indicator value
	 * 
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/uploadandsharepages/" ,params={"task=addNotice"}, method =  {RequestMethod.POST}) 
	public String doAddNotice (@Valid @ModelAttribute("uploadsharedPlandocForm") UploadSharedNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		
		String termsAndAcceptanceInd = form.getUserTermsAndAcceptanceInd();
		HttpSession session = request.getSession(false);
		form.setSortAppliedInd(false);
		session.setAttribute(Constants.ACTION_PERFORMED, Constants.ADD);
		if (!Constants.YES.equals(termsAndAcceptanceInd)) {
			form.setTask(null);
			postExecute(form, request, response);
			return forwards.get(Constants.TERMS_AND_CONDITIONS);
		}else{
			form.setTask(null);
			postExecute(form, request, response);
			return forwards.get(Constants.ADD_NOTICE);
		}

	}

	/**
	 * To view the CustomPlanDocument if satisfies the all the validations
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 * @throws IOException 
	 * @throws ApplicationException 
	 */
	@RequestMapping(value ="/uploadandsharepages/", params={"task=viewNotice"} , method =  {RequestMethod.POST}) 
	public String doViewNotice (@Valid @ModelAttribute("uploadsharedPlandocForm") UploadSharedNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException, ApplicationException  {

		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		byte[] genereatedPDF = null;
		
		UserProfile userProfile = getUserProfile(request);
		PlanNoticeDocumentVO planNoticeDocumentVO = null;
		String planDocument = request.getParameter(Constants.PLAN_DOCUMENT);
		if (Constants.CUSTOM_PLAN_DOCUMENT.equals(planDocument)) {
			Collection<GenericException> errors = super.doValidate( form,request);
			String softDelIndicator = null;
			softDelIndicator = PlanNoticeDocumentServiceDelegate.getInstance()
					.checkPlanNoticeSoftDeleteIndicator(Integer.parseInt(request
							.getParameter(Constants.CONTRACT_NO)),Integer.parseInt(request
									.getParameter(Constants.DOCUMENT_ID)),request.getParameter(Constants.DOCUMENT_FILE_NAME));
				if (softDelIndicator != null
						&& softDelIndicator.equalsIgnoreCase(Constants.YES)) {
					planNoticeDocumentVO = PlanNoticeDocumentServiceDelegate.
							getInstance().getPlanNoticeDeletedUsername(Integer.parseInt(request.getParameter(Constants.CONTRACT_NO)),Integer.parseInt(request.getParameter(Constants.DOCUMENT_ID)));
					String  username= null;
					if(planNoticeDocumentVO!=null){
						username = planNoticeDocumentVO.getPlanNoticeDeletedUserName();
					}
					if (Constants.VIEW.equals(request.getParameter(Constants.SOURCE))) {
						errors.add(new ValidationError(
								CsfConstants.EMPTY_STRING, ErrorCodes.VIEW_BUTTON_ERROR_MESSAGE,
								new Object[] {username} ));
						SessionHelper.setErrorsInSession(request, errors);
					}else if (Constants.LINK.equals(request.getParameter(Constants.SOURCE))) {
						errors.add(new ValidationError(
								CsfConstants.EMPTY_STRING, ErrorCodes.VIEW_DOCUMENT_NAME_ERROR_MESSAGE,
								new Object[] {username} ));
						SessionHelper.setErrorsInSession(request, errors);
					} 
				} 
			genereatedPDF = generatePDF(request, response);
			HttpSession session = request.getSession(false);
			session.setAttribute(Constants.DOCUMENT_FILE_NAME,
					request.getParameter(Constants.DOCUMENT_FILE_NAME));
			if(errors.size() > 0){
			response.getWriter().write(Constants.VALIDATION_ERROR);
			}else if (genereatedPDF == null) {
				response.getWriter().write(Constants.PDF_NOT_GENERATED);
			} else {
				session.setAttribute(getPdfSessionCacheAttributeName(),
						new Handle(genereatedPDF));
				response.getWriter().write(Constants.PDF_GENERATED);
			}
		} else if (Constants.PLAN_HIGHLIGHT_DOCUMENT.equals(planDocument)) {
			Collection<GenericException> errors = doValidate( form, request);
			final Integer contractId = userProfile.getCurrentContract().getContractNumber();
			boolean summaryHighlightsAvailable = false;
			boolean summaryHighlightsReviewed = false;
			ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();
				ContractServiceFeature serviceFeature = delegate
						.getContractServiceFeature(
								contractId,
								ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_AVAILABLE);
				summaryHighlightsAvailable = ContractServiceFeature
						.internalToBoolean(serviceFeature.getValue())
						.booleanValue();
				summaryHighlightsReviewed = ContractServiceFeature
						.internalToBoolean(
								serviceFeature
								.getAttributeValue(ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_REVIEWDED))
								.booleanValue();
			if (!summaryHighlightsAvailable || !summaryHighlightsReviewed) {
				if (Constants.VIEW.equals(request.getParameter(Constants.SOURCE))) {
					errors.add(new ValidationError(CsfConstants.EMPTY_STRING,
							ErrorCodes.JH_VIEW_BUTTON_ERROR_MESSAGE));
				}else if (Constants.LINK.equals(request.getParameter(Constants.SOURCE))) {
					errors.add(new ValidationError(CsfConstants.EMPTY_STRING,
							ErrorCodes.JH_DOCUMENT_NAME_ERROR_MESSAGE));
				}
			}if(errors.size() > 0){
				SessionHelper.setErrorsInSession(request, errors);
				response.getWriter().write(Constants.VALIDATION_ERROR);
				}else{
			final Logger logger = Logger.getLogger(BaseReportController.class);
			byte[] downloadData = null;
			Location location = Location
					.valueOfForAbbreviation(CommonEnvironment.getInstance()
							.getSiteLocation());
			String headerFooterImagePath = null;
			java.net.URL url = NoticeManagerUtility.class
					.getResource(CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX
							+ CommonConstants.HEADER_FOOTER_IMAGE);
			if (url != null) {
				headerFooterImagePath = url.toExternalForm();
			}
			if (logger.isDebugEnabled()) {
				logger.info("Header Footer Image path for the PH PDF is "
						+ headerFooterImagePath);
			}
			downloadData = ContractServiceDelegate
					.getInstance()
					.generateSphPdf(contractId, location, headerFooterImagePath);
			HttpSession session = request.getSession(false);
			if (downloadData == null) {
				response.getWriter().write(Constants.PDF_NOT_GENERATED);
			} else {
				session.setAttribute(getPdfSessionCacheAttributeKey(),
						new Handle(downloadData));
				response.getWriter().write(Constants.PDF_GENERATED);
			}
				}
		}else if (Constants.PI_NOTICE.equals(planDocument)) {
			Collection<GenericException> errors = super.doValidate( form,request);
			NoticeManagerUtility.get404a5JHDocumentAccessPermission(userProfile,
					form);
		        if (!form.isShow404section() || !form.isShowPlanInvestmentNotice()) {
						 if (Constants.VIEW.equals(request.getParameter(Constants.SOURCE))) {
							 errors.add(new ValidationError(CsfConstants.EMPTY_STRING,
									 ErrorCodes.JH_VIEW_BUTTON_ERROR_MESSAGE));
							}else if (Constants.LINK.equals(request.getParameter(Constants.SOURCE))) {
								errors.add(new ValidationError(CsfConstants.EMPTY_STRING,
										ErrorCodes.JH_DOCUMENT_NAME_ERROR_MESSAGE));
							}
		        }
		        if (errors.size() > 0) {
				SessionHelper.setErrorsInSession(request, errors);
				response.getWriter().write(Constants.VALIDATION_ERROR);
			}else{
				response.getWriter().write(Constants.VALIDATION_SUCCESS);
			}
		} else if (Constants.ICC_NOTICE.equals(planDocument)) {
			Collection<GenericException> errors = super.doValidate(form,request);
			NoticeManagerUtility.get404a5JHDocumentAccessPermission(userProfile, form);
			 if (!form.isShow404section() || !form.isShowICC()) {
				 if (Constants.VIEW.equals(request.getParameter(Constants.SOURCE))) {
					 errors.add(new ValidationError(CsfConstants.EMPTY_STRING,
							 ErrorCodes.JH_VIEW_BUTTON_ERROR_MESSAGE));
					}else if (Constants.LINK.equals(request.getParameter(Constants.SOURCE))) {
						errors.add(new ValidationError(CsfConstants.EMPTY_STRING,
								ErrorCodes.JH_DOCUMENT_NAME_ERROR_MESSAGE));
					}
		        }
		        if (errors.size() > 0) {
				SessionHelper.setErrorsInSession(request, errors);
				response.getWriter().write(Constants.VALIDATION_ERROR);
			  }else{
				  response.getWriter().write(Constants.VALIDATION_SUCCESS);
			}
		}
		postExecute(form, request, response);
		return null;
	}

	/**
	 * forward to Edit page
	 * 
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/uploadandsharepages/", params={"task=editNotice"}  , method =  {RequestMethod.POST}) 
	public String doEditNotice (@Valid @ModelAttribute("uploadsharedPlandocForm") UploadSharedNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		
		final UserProfile userProfile = getUserProfile(request);
		PlanNoticeDocumentVO planNoticeDocumentVO = null;
		PlanNoticeDocumentVO planNoticeVO = null;
		doCommon( form, request, response);
		Collection<GenericException> errors = super.doValidate(
				form, request);
		long profileid = userProfile.getPrincipal().getProfileId();
		String softDelIndicator = null;
		Lock documentLocked = null;
		softDelIndicator = PlanNoticeDocumentServiceDelegate.getInstance()
				.checkPlanNoticeSoftDeleteIndicator(Integer.parseInt(request
						.getParameter(Constants.CONTRACT_NO)),Integer.parseInt(request
								.getParameter(Constants.DOCUMENT_ID)),form.getPlanDocumentFileName());
		if (softDelIndicator != null
				&& softDelIndicator.equalsIgnoreCase(Constants.YES)) {
			planNoticeDocumentVO = PlanNoticeDocumentServiceDelegate.
					getInstance().getPlanNoticeDeletedUsername(Integer.parseInt(request.getParameter(Constants.CONTRACT_NO)),Integer.parseInt(request.getParameter(Constants.DOCUMENT_ID)));
			String  username= null;
			if(planNoticeDocumentVO!=null){
				username = planNoticeDocumentVO.getPlanNoticeDeletedUserName();
			}
				errors.add(new ValidationError(
						CsfConstants.EMPTY_STRING, ErrorCodes.EDIT_BUTTON_ERROR_MESSAGE,
						new Object[] {username} ));
				SessionHelper.setErrorsInSession(request, errors);
				return forwards.get(Constants.UPLOAD_AND_SHARE);
			} 
		 documentLocked = LockServiceDelegate.getInstance().getLockInfo(Constants.CUSTOM_DOCUMENT_LOCK,request.getParameter(Constants.DOCUMENT_ID));
		 if (documentLocked!=null){
			 long loggedUserProfileId = userProfile.getPrincipal().getProfileId();
			 long noticeLokedProfileId = documentLocked.getLockUserProfileId();
			Timestamp ts = documentLocked.getLockCreateTs();
			long currentTS = System.currentTimeMillis(); 
			long lockedTS = ts.getTime();
			long minutes = TimeUnit.MILLISECONDS.toMinutes(currentTS - lockedTS);
			if(minutes >= 30 || (loggedUserProfileId == noticeLokedProfileId)){
				    LockServiceDelegate.getInstance().releaseLock(Constants.CUSTOM_DOCUMENT_LOCK,request.getParameter(Constants.DOCUMENT_ID));		
					}else{	
						planNoticeVO = PlanNoticeDocumentServiceDelegate.
								getInstance().getPlanNoticeLockedUserName(documentLocked.getComponentKey(),documentLocked.getLockUserProfileId());
						String UserName = planNoticeVO.getPlanNoticeLockedUserName();			
			errors.add(new ValidationError(
					CsfConstants.EMPTY_STRING, ErrorCodes.EDIT_BUTTON_ACTIVE_LOCK_ERROR_MESSAGE,
					new Object[] {UserName} ));
			SessionHelper.setErrorsInSession(request, errors);
			return forwards.get(Constants.UPLOAD_AND_SHARE);
					}}else {
			LockServiceDelegate.getInstance().lock(
					Constants.CUSTOM_DOCUMENT_LOCK,
					request.getParameter(Constants.DOCUMENT_ID), profileid);
		}
		HttpSession session = request.getSession(false);
		form.setSortAppliedInd(false);
		session.setAttribute(Constants.DOCUMENT_ID, form.getDocumentId());
		form.setTask(null);
		postExecute(form, request, response);
		return forwards.get(Constants.EDIT_NOTICE);
	}
	
	/**
	 * To apply the CustomPlanDocument display order based on user changes
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/uploadandsharepages/", params={"task=applyNoticeOrder"}, method =  {RequestMethod.POST}) 
	public String doApplyNoticeOrder (@Valid @ModelAttribute("uploadsharedPlandocForm") UploadSharedNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		boolean documentDisplyOrderChanges = false;
		String forward = doCommon( form, request, response);
	
		String documentorderstring[] = form.getDocumentsOrder()
				.split(",");
		form.setSortAppliedInd(false);
		PlanDocumentReportData reportData = (PlanDocumentReportData) request
				.getAttribute(CommonConstants.REPORT_BEAN);
		List<PlanNoticeDocumentVO> planVoList = new ArrayList<PlanNoticeDocumentVO>();
		List<Integer> documentOrderList = new ArrayList<Integer>();
		for (int documentIdIndex = 0; documentIdIndex < documentorderstring.length; documentIdIndex++) {
			documentOrderList.add(new Integer(
					documentorderstring[documentIdIndex]));
		}
		documentDisplyOrderChanges = PlanNoticeDocumentServiceDelegate
				.getInstance().updateCustomPlanNoticeDocumentOrder(
						documentOrderList);
		form
		.setDocumentDisplyOrderChanges(documentDisplyOrderChanges);
		if (documentDisplyOrderChanges) {
			for (int index = 0; index < documentOrderList.size(); index++) {
				for (PlanNoticeDocumentVO planVo : reportData
						.getCustomPlanNoticeDocuments())

				{
					if (documentOrderList.get(index).equals(
							planVo.getDocumentId())) {
						planVoList.add(planVo);
					}
				}
			}
			reportData.setCustomPlanNoticeDocuments(planVoList);
			request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
		}
		form.setTask(null);
		postExecute(form, request, response);
		return forward;
	}

	/**
	 *  To reset the default CustomPlanDocument display order
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/uploadandsharepages/", params={"task=resetPlanDocumentPreviousChanges"}, method =  {RequestMethod.POST}) 
	public String doResetPlanDocumentPreviousChanges (@Valid @ModelAttribute("uploadsharedPlandocForm") UploadSharedNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		
		form.setDisableTheResetButton(true);
		form.setSortAppliedInd(false);
		form.setTask(null);
		String forward = doCommon( form, request, response);
		return forward;
	}

	/**
	 * To deleting the CustomPlanDocument based on response value
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/uploadandsharepages/" ,params={"task=deleteNotice"}, method =  {RequestMethod.POST}) 
	public String doDeleteNotice (@Valid @ModelAttribute("uploadsharedPlandocForm") UploadSharedNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		String forward = null;
		
		PlanNoticeDocumentVO planNoticeDocumentVO = new PlanNoticeDocumentVO();
		PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO = new PlanNoticeDocumentChangeHistoryVO();
		planNoticeDocumentChangeHistoryVO.setDocumentId(form
				.getDocumentId());
		planNoticeDocumentChangeHistoryVO.setContractId(form
				.getContractId());
		planNoticeDocumentVO.setDocumentId(form.getDocumentId());
		planNoticeDocumentVO.setContractId(form.getContractId());
		UserProfile userProfile = getUserProfile(request);
		planNoticeDocumentVO.setProfileId(new BigDecimal(userProfile.getPrincipal().getProfileId()));
		boolean userResponse = form.isConfirmIndicator();
		if (userResponse) {
			LockServiceDelegate.getInstance().releaseLock(Constants.CUSTOM_DOCUMENT_LOCK,request.getParameter(Constants.DOCUMENT_ID));
			PlanNoticeDocumentServiceDelegate.getInstance()
			.deleteCustomPlanNoticeDocument(planNoticeDocumentVO);
			form.setTask(null);
			BigDecimal profileId = new BigDecimal(userProfile.getPrincipal().getProfileId());
			Integer contractId =userProfile.getCurrentContract().getContractNumber();
			String userAction =CommonConstants.NOTICE_DELETE;
			//Adds the notice delete information
			PlanNoticeDocumentServiceDelegate.getInstance().userActionLog(contractId,profileId,userAction);
			forward = doCommon( form, request, response);
		} else {
			LockServiceDelegate.getInstance().releaseLock(Constants.CUSTOM_DOCUMENT_LOCK,request.getParameter(Constants.DOCUMENT_ID));
			forward = doCommon( form, request, response);
		}	
		form.setTask(null);
		postExecute(form, request, response);
		return forward;
	}

	@Override
	protected String getReportId() {
		return PlanDocumentReportData.REPORT_ID;
	}

	@Override
	protected String getReportName() {
		return PlanDocumentReportData.REPORT_NAME;
	}

	@Override
	protected String getDefaultSort() {
		return Constants.DEFAULT_SORT_FIELD;
	}

	@Override
	protected String getDefaultSortDirection() {
		return Constants.DEFAULT_SORT_DIRECTION;
	}

	/**
	 * this method doesn't required any implementation,it is only for
	 * overwritten purpose
	 */
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
					throws SystemException {
		return null;
	}

	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {

		if (logger.isDebugEnabled()) { 
			logger.debug("entry -> populateReportCriteria");
		}
		UserProfile userProfile = getUserProfile(request);
		criteria.addFilter(PlanDocumentReportData.FILTER_CONTRACT_NUMBER,
				userProfile.getCurrentContract().getContractNumber());

		criteria.addFilter(PlanDocumentReportData.FILTER_USER_PROFILE_ID,
				String.valueOf(userProfile.getPrincipal().getProfileId()));
		request.getParameter(TASK_KEY);

		// additional sorts for the download details
		// criteria.insertSort(PlanDocumentReportData.SORT_FIELD_SORT_NUMBER,
		// ReportSort.ASC_DIRECTION);
		criteria.addFilter(PlanDocumentReportData.FILTER_TASK,
				PlanDocumentReportData.TASK_PRINT);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}
	}

	/**
	 * To validating the CustomPlanDocuments
	 * @param form
	 * @param request
	 * @return
	 * @throws SystemException 
	 * @throws IOException 
	 */
	
	
	
	@RequestMapping(value ="/uploadandsharepages/" ,params={"task=validateCustomPlanDocument"}, method =  {RequestMethod.POST}) 
	public void doValidateCustomPlanDocument(@Valid @ModelAttribute("uploadsharedPlandocForm") UploadSharedNoticeManagerForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException, IOException  {
		final UserProfile userProfile = getUserProfile(request);
		PlanNoticeDocumentVO planNoticeDocumentVO = null;
		PlanNoticeDocumentVO planNoticeVO = null;
		Collection<GenericException> errors = super.doValidate( reportForm,
				request);
		long profileid = userProfile.getPrincipal().getProfileId();
		String softDelIndicator = null;
		softDelIndicator = PlanNoticeDocumentServiceDelegate.getInstance()
				.checkPlanNoticeSoftDeleteIndicator(Integer.parseInt(request
						.getParameter(Constants.CONTRACT_NO)),Integer.parseInt(request
								.getParameter(Constants.DOCUMENT_ID)),request.getParameter(Constants.DOCUMENT_FILE_NAME));
			if (softDelIndicator != null
					&& softDelIndicator.equalsIgnoreCase(Constants.YES)) {
					planNoticeDocumentVO = PlanNoticeDocumentServiceDelegate.
							getInstance().getPlanNoticeDeletedUsername(Integer.parseInt(request.getParameter(Constants.CONTRACT_NO)),
									Integer.parseInt(request.getParameter(Constants.DOCUMENT_ID)));
					String  username= null;
					if(planNoticeDocumentVO!=null){
						username = planNoticeDocumentVO.getPlanNoticeDeletedUserName();
					}
						errors.add(new ValidationError(
								CsfConstants.EMPTY_STRING, ErrorCodes.DELETE_BUTTON_ERROR_MESSAGE,
								new Object[] {username} ));
						response.getWriter().write(Constants.VALIDATION_ERROR);
						SessionHelper.setErrorsInSession(request, errors);
					} 
				Lock documentLocked = LockServiceDelegate.getInstance().getLockInfo(Constants.CUSTOM_DOCUMENT_LOCK,request.getParameter(Constants.DOCUMENT_ID));
				if (documentLocked!=null) {
					 long loggedUserProfileId = userProfile.getPrincipal().getProfileId();
					 long noticeLokedProfileId = documentLocked.getLockUserProfileId();
					 Timestamp ts = documentLocked.getLockCreateTs();
						long currentTS = System.currentTimeMillis(); 
						long lockedTS = ts.getTime();
						long minutes = TimeUnit.MILLISECONDS.toMinutes(currentTS - lockedTS);
				        if(minutes >= 30 || (loggedUserProfileId == noticeLokedProfileId)){
						 LockServiceDelegate.getInstance().releaseLock(Constants.CUSTOM_DOCUMENT_LOCK,request.getParameter(Constants.DOCUMENT_ID));
					 }else{
					planNoticeVO = PlanNoticeDocumentServiceDelegate.
							getInstance().getPlanNoticeLockedUserName(documentLocked.getComponentKey(),documentLocked.getLockUserProfileId());
					String UserName = planNoticeVO.getPlanNoticeLockedUserName();
					errors.add(new ValidationError(
							CsfConstants.EMPTY_STRING, ErrorCodes.DELETE_BUTTON_ACTIVE_LOCK_ERROR_MESSAGE,
							new Object[] {UserName} ));
					response.getWriter().write(Constants.VALIDATION_ERROR);
					SessionHelper.setErrorsInSession(request, errors);
				}}else{
					LockServiceDelegate.getInstance().lock(Constants.CUSTOM_DOCUMENT_LOCK,request.getParameter(Constants.DOCUMENT_ID),profileid);
				}
				if(errors.size() == 0){
					response.getWriter().write(Constants.VALIDATION_SUCCESS);
				}
	}
	/**
	 * Populate the error in same plan notice page
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/uploadandsharepages/", params={"task=populateError"}, method =  {RequestMethod.GET}) 
	public String doPopulateError (@Valid @ModelAttribute("uploadsharedPlandocForm") UploadSharedNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {


		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		doCommon( form, request, response);
		postExecute(form, request, response);
		return forwards.get(Constants.UPLOAD_AND_SHARE);
	}
	/**
	 * To validating the JHPlanDocument 
	 * @param form
	 * @param request
	 * @return
	 * @throws SystemException
	 * @throws ApplicationException 
	 */
	public Collection validateJhPlanDocumen( ActionForm form,
			HttpServletRequest request) throws SystemException, ApplicationException {

		Collection<Exception> errors = super.doValidate( form,request);
		final UserProfile userProfile = getUserProfile(request);
		final Integer contractId = userProfile.getCurrentContract().getContractNumber();
		boolean summaryHighlightsAvailable = false;
		boolean summaryHighlightsReviewed = false;
		int error = 0;
		ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();
		
			ContractServiceFeature serviceFeature = delegate
					.getContractServiceFeature(
							contractId,
							ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_AVAILABLE);
			summaryHighlightsAvailable = ContractServiceFeature
					.internalToBoolean(serviceFeature.getValue())
					.booleanValue();
			summaryHighlightsReviewed = ContractServiceFeature
					.internalToBoolean(
							serviceFeature
							.getAttributeValue(ServiceFeatureConstants.SUMMARY_PLAN_HIGHLIGHT_REVIEWDED))
							.booleanValue();
		if (!summaryHighlightsAvailable || !summaryHighlightsReviewed) {
			if (Constants.VIEW.equals(request.getParameter(Constants.SOURCE))) {
				error = ErrorCodes.JH_VIEW_BUTTON_ERROR_MESSAGE;
			}else if (Constants.LINK.equals(request.getParameter(Constants.SOURCE))) {
				error = ErrorCodes.JH_DOCUMENT_NAME_ERROR_MESSAGE;
			}
				errors.add(new ValidationError(CsfConstants.EMPTY_STRING,
						error));
			
		}
		return errors;
	}

	/**
	 * Class to handle the byte array request object
	 * 
	 * @author arugupu
	 * 
	 */
	static class Handle implements Serializable {

		/**
		 * This is used to get the byte array
		 */
		private static final long serialVersionUID = 1L;

		Handle(byte[] array) {
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

	String getPdfSessionCacheAttributeName() {
		return "generatedCustomPlanNoticeArray";
	}

	String getPdfSessionCacheAttributeKey() {
		return "generatedPlanHighlighttNoticeArray";
	}

	/**This method is used to get the get the generatePDF array
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	private byte[] generatePDF(HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		DocumentInfo documentInfo = null;
		DocumentFileOutput documentFileOutput = null;
		byte[] downloadData = null;
		documentInfo = new PlanNoticeDocumentInfo(
				request.getParameter(Constants.CONTRACT_NO),
				request.getParameter(Constants.DOCUMENT_FILE_NAME), " ");
		try {
			documentFileOutput = EReportsServiceDelegate.getInstance()
					.getDocument(documentInfo);
		} catch (ServiceUnavailableException e) {
			logger.error(
					"problam ocuured during the generatePDF method",e);

		}
		downloadData = documentFileOutput.getReportFragment();
		return downloadData;
	}

	/**This method is used to Download the pdf document
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	
	@RequestMapping(value ="/uploadandsharepages/",params={"task=pdfDownload"},method =  {RequestMethod.GET}) 
	public String doPdfDownload (@Valid @ModelAttribute("uploadsharedPlandocForm") UploadSharedNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		HttpSession session = request.getSession(false);
		byte[] download = null;
		if (session.getAttribute(getPdfSessionCacheAttributeName()) != null) {
			download = ((Handle) session
					.getAttribute(getPdfSessionCacheAttributeName()))
					.getByteArray();
			session.setAttribute(getPdfSessionCacheAttributeName(), null);
		} else {
			download = generatePDF(request, response);
		}
		String documentFileName = (String) session
				.getAttribute(Constants.DOCUMENT_FILE_NAME);
		if (download != null && download.length > 0) {
			BaseReportController.streamDownloadData(request, response,
					"application/pdf", documentFileName, download);
		}
		if(session.getAttribute(Constants.DOCUMENT_VISITED) == null)
		{
			int documentLog=0;
			++documentLog;
			session.setAttribute(Constants.DOCUMENT_VISITED, new Integer(documentLog));
		}else{
			int documentLog=(Integer) session.getAttribute(Constants.DOCUMENT_VISITED);
			++documentLog;
			session.setAttribute(Constants.DOCUMENT_VISITED, new Integer(documentLog));
		}
		return null;
	}

	/**This method is used to Download the pdf document
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/uploadandsharepages/", params={"task=planHighlightPdfDownload"}, method =  {RequestMethod.GET}) 
	public String doPlanHighlightPdfDownload (@Valid @ModelAttribute("uploadsharedPlandocForm") UploadSharedNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		byte[] download = null;
		UserProfile userProfile = getUserProfile(request);
		final Integer contractId = userProfile.getCurrentContract()
				.getContractNumber();
		HttpSession session = request.getSession(false);
		if (session.getAttribute(getPdfSessionCacheAttributeKey()) != null) {
			download = ((Handle) session
					.getAttribute(getPdfSessionCacheAttributeKey()))
					.getByteArray();
			session.setAttribute(getPdfSessionCacheAttributeKey(), null);
		} else {
			Location location = Location
					.valueOfForAbbreviation(CommonEnvironment.getInstance()
							.getSiteLocation());
			String headerFooterImagePath = null;
			java.net.URL url = NoticeManagerUtility.class
					.getResource(CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX
							+ CommonConstants.HEADER_FOOTER_IMAGE);
			if (url != null) {
				headerFooterImagePath = url.toExternalForm();
			}
			download = ContractServiceDelegate.getInstance().generateSphPdf(
					contractId, location, headerFooterImagePath);
			download = generatePDF(request, response);
		}
		if (download != null && download.length > 0) {
			ReportController.streamDownloadData(request, response,
					"application/pdf", "sph_" + contractId + ".pdf", download);
		}
		if(session.getAttribute(Constants.DOCUMENT_VISITED) == null)
		{
			int documentLog=0;
			++documentLog;
			session.setAttribute(Constants.DOCUMENT_VISITED, new Integer(documentLog));
		}else{
			int documentLog=(Integer) session.getAttribute(Constants.DOCUMENT_VISITED);
			++documentLog;
			session.setAttribute(Constants.DOCUMENT_VISITED, new Integer(documentLog));
		}
		return null;
	}	

	/**
	 * Adds the page log information
	 * @param reportForm
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	  protected void postExecute(  ActionForm form, HttpServletRequest request, 
				HttpServletResponse response) throws ServletException, IOException, SystemException {
		  

		  HttpSession session = request.getSession(false);
		  super.postExecute( form, request, response);
		  UserProfile userProfile = getUserProfile(request);
		  BigDecimal profileId = new BigDecimal(userProfile.getPrincipal().getProfileId());
		  Integer contractId =userProfile.getCurrentContract().getContractNumber();
		  String userAction =CommonConstants.UPLOAD_AND_SHARE_PAGE;
		  if(session.getAttribute(Constants.LOGGED) == null)
		  {
			  PlanNoticeDocumentServiceDelegate.getInstance().userActionLog(contractId, profileId, userAction);
			  session.setAttribute(Constants.LOGGED, "VISITED");
		  }
	  }

	/**
	 * This method will check whetherCustomPlan Document is generated or not. If
	 * report generation is complete then response status code is set as
	 * 400(request fails) This causes the AJAX request to fail and in the
	 * failure event waiting message will be closed.
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	  @RequestMapping(value ="/uploadandsharepages/", params={"action=openErrorPdf"}, method =  {RequestMethod.GET}) 
		public String doOpenErrorPdf (@Valid @ModelAttribute("uploadsharedPlandocForm") UploadSharedNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
	  
	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  

		List<GenericException> errors = new ArrayList<GenericException>();
		errors.add(new GenericException(ErrorCodes.REPORT_FILE_NOT_FOUND));
		setErrorsInRequest(request, errors);
		request.setAttribute(Constants.LAYOUT_BEAN, LayoutBeanRepository
				.getInstance().getPageBean(Constants.EMPTY_LAYOUT_ID));
		return forwards.get(Constants.SECONDARY_WINDOW_ERROR_FORWARD);

	}
	  @RequestMapping(value ="/uploadandsharepages/",params={"task=customSort"},  method =  {RequestMethod.POST}) 
		public String doCustomSort (@Valid @ModelAttribute("uploadsharedPlandocForm") UploadSharedNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }  
		PlanDocumentReportData reportData = null;
		
		form.setSortAppliedInd(true);
		Integer documentId = form.getDocumentId();
		int documentIndex=0;
		String sortTypeArrow = form.getSortTypeArrow();
			reportData = form.getReport();
		reportData = form.getReport();
		List<PlanNoticeDocumentVO> planVoList = new ArrayList<PlanNoticeDocumentVO>();
		planVoList.addAll(reportData.getCustomPlanNoticeDocuments());
		for(PlanNoticeDocumentVO planVo : planVoList){
			if(planVo.getDocumentId().equals(documentId)){
				documentIndex =	planVoList.indexOf(planVo);
			}
		}
		if(Constants.CUSTOM_SORT_ARROW.equalsIgnoreCase(sortTypeArrow)){
			if(documentIndex !=0){
				Collections.swap(planVoList, documentIndex, documentIndex - 1);
			}
		} else{
			if(documentIndex != planVoList.size()-1){
				Collections.swap(planVoList, documentIndex, documentIndex + 1);
			}
		} 
		reportData.setCustomPlanNoticeDocuments(planVoList);
		form.setReport(reportData);
		request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
		form.setCustomSortInd(true);
		
		form.setTask(null);
		return forwards.get(Constants.UPLOAD_AND_SHARE);
	}
	
	/**
	 * To get documentPosted username for displaying in delete CustomPlanDo
	 * cument confirm warning message
	 * @param reportForm
	 * @param request
	 * @param response
	 * @throws SystemException
	 * @throws IOException
	 */

	  @RequestMapping(value ="/uploadandsharepages/",params={"task=getDocumentPostedUserName"},  method =  {RequestMethod.POST}) 
		public void doGetDocumentPostedUserName (@Valid @ModelAttribute("uploadsharedPlandocForm") UploadSharedNoticeManagerForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException { 
	  /*
		String user  = null;
		PlanNoticeDocumentVO planNoticeDocumentVO = null;
		
		planNoticeDocumentVO = PlanNoticeDocumentServiceDelegate.
				getInstance().getDocumentPostedUsername(form.getContractId(),form.getDocumentId());
		PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO = planNoticeDocumentVO.getPlanNoticeDocumentChangeDetail();
		user = planNoticeDocumentChangeHistoryVO.getChangedUserName();
		response.getWriter().write(user);
		*/
		
		// check twelve month rule for the document before allow to delete

		String docAvailableUntilDate = null;
		String username = null;
		StringBuffer warningMessage = new StringBuffer();
		String postToPptInd = request.getParameter(Constants.POST_TO_PPT_IND);
		PlanNoticeDocumentChangeHistoryVO planNoticeDocumentHistoryVO = null;

		planNoticeDocumentHistoryVO = PlanNoticeDocumentServiceDelegate.getInstance()
				.getCustomPlanNoticeDocumentHistory(Integer.parseInt(request.getParameter(Constants.CONTRACT_NO)),
						Integer.parseInt(request.getParameter(Constants.DOCUMENT_ID)));

		if (null != planNoticeDocumentHistoryVO) {

			planNoticeDocumentHistoryVO.setCrossedTwelveMonthRule(
					NoticeManagerUtility.checkTwelveMonthRuleForDocument(postToPptInd, planNoticeDocumentHistoryVO));

			if (!planNoticeDocumentHistoryVO.isCrossedTwelveMonthRule()) {
				username = planNoticeDocumentHistoryVO.getChangedUserName();
				docAvailableUntilDate = NoticeManagerUtility
						.getDocAvailableUntilDate(planNoticeDocumentHistoryVO.getChangedDatePlusOneYear());

				try {
					Message message = (Message) ContentCacheManager.getInstance().getContentById(
							ContentConstants.NMC_DELETE_DOC_WARNING_MESSAGE, ContentTypeManager.instance().MESSAGE);
					String[] params = { username, docAvailableUntilDate };
					warningMessage.append(ContentUtility.getContentAttribute(message, "text", null, params));

				} catch (ContentException e) {
					throw new SystemException(e, "Failed to call content service.");
				}
			}
		}
		response.getWriter().write(warningMessage.toString());
	}

	/**
	 * This code has been changed and added to Validate form and request against
	 * penetration attack, prior to other validations.
	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
	
}
