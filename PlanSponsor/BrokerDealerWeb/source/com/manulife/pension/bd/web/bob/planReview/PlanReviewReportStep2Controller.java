package com.manulife.pension.bd.web.bob.planReview;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.fop.apps.FOPException;
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
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.planReview.sort.PlanReviewReportStep2PageColumn;
import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportDataValidator;
import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportUtils;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.PlanReviewServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PDFGenerator;
import com.manulife.pension.platform.web.util.ReportsXSLProperties;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.planReview.util.PlanReviewRequestProperties;
import com.manulife.pension.service.planReview.valueobject.ActivityEventVo;
import com.manulife.pension.service.planReview.valueobject.ActivityVo;
import com.manulife.pension.service.planReview.valueobject.PlanReviewCoverImageDetails;
import com.manulife.pension.service.planReview.valueobject.PlanReviewRequestVO;
import com.manulife.pension.service.planReview.valueobject.PublishDocumentPackageVo;
import com.manulife.pension.util.ImageUtility;
import com.manulife.pension.util.PlanReviewConstants;
import com.manulife.pension.util.PlanReviewConstants.ActivityEventCode;
import com.manulife.pension.util.PlanReviewConstants.ActivitySatusCode;
import com.manulife.pension.util.PlanReviewConstants.ActivityTypeCode;
import com.manulife.pension.util.PlanReviewConstants.EventSourceCode;
import com.manulife.pension.util.PlanReviewConstants.RequestTypeCode;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.RenderConstants;

/**
 * @author Manjunath
 * 
 *         Class to implement Customization functionalities(Step2)
 * 
 */
@Controller
@RequestMapping( value = "/bob/contract")
@SessionAttributes({"planReviewReportForm"})

public class PlanReviewReportStep2Controller extends BasePlanReviewReportController {
	@ModelAttribute("planReviewReportForm")
	public PlanReviewReportForm populateForm() 
	{
		return new PlanReviewReportForm();
		}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/contract/planReview/step2IndividualPlanReviewReport.jsp");
		forwards.put("default","/contract/planReview/step2IndividualPlanReviewReport.jsp");
		forwards.put("sort","/contract/planReview/step2IndividualPlanReviewReport.jsp");
		forwards.put("generateReport","redirect:/do/bob/contract/planReview/Results/");
		forwards.put("back","redirect:/do/bob/contract/planReview/");
		forwards.put("coverImagePage","/WEB-INF/planReview/common/uploadCoverPageImage.jsp");
		forwards.put("uploadLogoPage","/WEB-INF/planReview/common/uploadLogoPage.jsp");
		forwards.put("uploadCoverImage","/contract/planReview/step2IndividualPlanReviewReport.jsp");
		forwards.put("selectedCoverImage","/contract/planReview/step2IndividualPlanReviewReport.jsp");
		forwards.put("uploadLogoImage","/contract/planReview/step2IndividualPlanReviewReport.jsp");
		forwards.put("contractReviewReportStep1Page","redirect:/do/bob/contract/planReview/");
		forwards.put("previewCoverPage","/contract/planReview/step2IndividualPlanReviewReport.jsp");
		forwards.put("history","redirect:/do/bob/contract/planReview/History/");
		forwards.put("homePage","redirect:/do/home/");
		} 
	

	protected static final Logger logger = Logger.getLogger(PlanReviewReportStep2Controller.class);
	
	private static final String XSLT_FILE_KEY_NAME = "PlanReviewReportCoverPage.XSLFile";
	private static final String FILE_JPG_EXTENTION = "jpg";
	
	private static final int TWO_MEGA_BYTES_IN_BYTES = 2097152;
	private static final int ONE_MEGA_BYTE_IN_BYTES = 1048576;
	
	private static final String DATE_LONG_MDY = "MMMMMMMMM dd,yyyy";
	private static final String TOP_LOGO_IMAGE_PATH = "/assets/unmanaged/images/JHRPS-logo-blue.jpg";
    
    public static final FastDateFormat slashMdydateFormat = FastDateFormat
			.getInstance(RenderConstants.MEDIUM_MDY_SLASHED);
    public static final FastDateFormat longMdyDateFormat = FastDateFormat
			.getInstance(DATE_LONG_MDY);
    
    private static final String COMMAND_FILE_NAME = "PlanReviewRequest_map_drive.cmd";
	
    // creates the map for \\mlisezkpiws3\db\PlanReview for PAS9 and PAS10
	static {
		try {
    		Runtime.getRuntime().exec("\\Apps\\PlanSponsor\\scripts\\" + COMMAND_FILE_NAME);
		} catch (Exception e) {
			logger.error("Unable to execute " + COMMAND_FILE_NAME + " file.");
		}
	}
    

	/**
	 * Constructor class.
	 */
	public PlanReviewReportStep2Controller() {
		super(PlanReviewReportStep2Controller.class);
	}

	/**
	 * This method presents the customize page .
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/Customize/",  method={RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault() in PlanReviewReportStep2Action");
		}

		 forward = super.doCommon( actionForm, request,
				response);

		for (PlanReviewReportUIHolder planReviewReportUIHolder : actionForm
				.getContractReviewReportVOList()) {
			planReviewReportUIHolder.setContractSelected(true);
		}
		
		populateCoverPageImages(actionForm, request);

		populateDisplayContractReviewReports(actionForm);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doDefault() in PlanReviewReportStep2Action.");
		}
		return forwards.get(forward);
	}


	/**
	 * This method will perform the task of storing the request details and
	 * calls the MDB.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	
	@RequestMapping(value ="/planReview/Customize/" ,params={"task=generateReport"}, method = {RequestMethod.POST}) 
	public String doGenerateReport (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
	
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doGenerateReport() in PlanReviewReportStep2Action");
		}

		

		ArrayList<GenericException> errorMessages = PlanReviewReportDataValidator
				.validatePresentersName(form);
		
		if (!errorMessages.isEmpty()) {
			setErrorsInSession(request, errorMessages);
			return forwards.get("input");
		}

		// get the final PlanReviewReport obj's to generate reports
		List<PlanReviewReportUIHolder> requestedPlanReviewReports = form
				.getDisplayContractReviewReports();
		//CR #9 changtes
		
		Timestamp requestedDate = new Timestamp(System.currentTimeMillis());
		
		Map<Integer, String> contractsWhichAlereadyReachedLimitMap = getContractsWhichAlereadyReachedLimit(form, request, requestedDate);
		List<Integer> contractsWhichAlereadyReachedLimitList = null;
		if(contractsWhichAlereadyReachedLimitMap != null){
			contractsWhichAlereadyReachedLimitList = new ArrayList<Integer>();
			for ( Entry<Integer, String> contractEntry : contractsWhichAlereadyReachedLimitMap.entrySet()) {
				if(PlanReviewConstants.YES.equalsIgnoreCase(contractEntry.getValue())){
					Integer contractId = contractEntry.getKey();
					requestedPlanReviewReports.remove(getPlanReviewReportUIHolder(String.valueOf(contractId), requestedPlanReviewReports));
					contractsWhichAlereadyReachedLimitList.add(contractId);
				}
				
				
			}
			request.getSession(false).setAttribute(BDConstants.CONTRACTS_WHICH_ALEREADY_REACHED_LIMIT, contractsWhichAlereadyReachedLimitList);
		}
		
		PlanReviewRequestVO PlanReviewRequest = PlanReviewReportUtils.populateRequestDetails(request,
				RequestTypeCode.PUBLISH);
		PlanReviewRequest.setCreatedTS(requestedDate);
		
		// If it is contract level, then the contract will be selected default
			if (!requestedPlanReviewReports.isEmpty()) {
				if (isPlanReviewAdminUser(request)) {

					// if the user is PlanReviewAdminUser and
					// periodEndingReportDate status is
					// not active or null then the test will be a "TEST"
					PlanReviewRequest
							.setRequestModeCode(PlanReviewConstants.REQUEST_MODE_CODE_TEST);
				}
			}

		
		populateActivityDetails(requestedPlanReviewReports,
				PlanReviewRequest, request);
		
		// This method is used to store the Report date from Step2 when user
		// clicks on GenerateReport.
		PlanReviewRequestVO planReviewRequestVo = PlanReviewServiceDelegate
				.getInstance(Environment.getInstance().getApplicationId())
				.insertPublishPlanReviewRequestDetails(PlanReviewRequest
						);

		request.getSession().setAttribute(BDConstants.PLAN_REVIEW_REQUEST_ID,
				String.valueOf(planReviewRequestVo.getRequestId()));

		//TODO: To be uncommented after development
		// MDB call to make PUBLISH webservice call
		PlanReviewReportUtils.firePlanReviewRequestEvents(planReviewRequestVo, requestedPlanReviewReports, request);

		// Logic for regular navigation
		setRegularPageNavigation(request);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doGenerateReport() in PlanReviewReportStep2Action.");
		}

		return forwards.get(getTask(request));
	}

	/**
	 * This method will redirect back to the Step1 Page.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/Customize/", params={"task=back"} , method =  {RequestMethod.POST}) 
	public String doBack (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		PlanReviewReportForm planReviewReportForm = (PlanReviewReportForm) form;
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doBack() in PlanReviewReportStep2Action");
		}
		

		form.getDisplayContractReviewReports().clear();
		form.setRequestBackFromStep2(true);
		form.setUploadCoverImage(null);
		form.setUploadImage(null);
		form.setCompanyName(null);

		setRegularPageNavigation(request);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doBack() in PlanReviewReportStep2Action.");
		}

		return forwards.get( getTask(request));
	}

	/**
	 * This method will used to handle the Sort data for Review Pages.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	
	@RequestMapping(value ="/planReview/Customize/", params={"action=sort","task=sort"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSort() in PlanReviewReportStep2Action");
		}

	

		List<PlanReviewReportUIHolder> displayReports = form
				.getDisplayContractReviewReports();
		
		Collections.sort(
				displayReports,
				PlanReviewReportStep2PageColumn
						.getContractReviewReportStep2PageColumn(
								form.getSortField())
						.getComparatorInstance(form.getSortDirection()));

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doSort() in PlanReviewReportStep2Action.");
		}

		return forwards.get( getTask(request));
	}
	
	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
   	 */
	@Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	}

	/**
	 * This method will redirect to the History Page.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/Customize/", params={"task=history"} , method =  {RequestMethod.POST}) 
	public String doHistory (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doHistory() in ContractReviewReportResultsAdocuction");
		}

		super.setRegularPageNavigation(request);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doResults() in ContractReviewReportResultsAction.");
		}
		

		return forwards.get(getTask(request));
	}

	/**
	 * This method populates the list of selected records to be shown on Step-2
	 * JSP page
	 * 
	 * @param actualContractReviewReports
	 * @param displayContractReviewReports
	 * @param planReviewReportForm
	 * @throws SystemException
	 */
	protected void populateDisplayContractReviewReports(
			PlanReviewReportForm planReviewReportForm)
			throws SystemException {

		List<PlanReviewReportUIHolder> actualContractReviewReports = planReviewReportForm
				.getContractReviewReportVOList();
		List<PlanReviewReportUIHolder> displayContractReviewReports = planReviewReportForm
				.getDisplayContractReviewReports();

		displayContractReviewReports.clear();

		for (PlanReviewReportUIHolder planReviewReportUIHolder : actualContractReviewReports) {
			
			if (planReviewReportUIHolder.isContractSelected()) {

				PlanReviewReportUIHolder displayContractReviewReport = planReviewReportUIHolder
						.cloneObject();

				displayContractReviewReport
						.setCoverImageName(planReviewReportForm
								.getDefaultCoverPageImage()
								.getCoverPageCaptionCmaValue());
				displayContractReviewReport
						.setCmaSelectedCoverImageIndicator(true);
				displayContractReviewReport
						.setCmaCoverPageImage(planReviewReportForm
								.getDefaultCoverPageImage());
				displayContractReviewReport.setUserUploadedLogoImage(false);

				displayContractReviewReports.add(displayContractReviewReport);
			}
		}
		planReviewReportForm
				.setDisplayCotractReviewReports(displayContractReviewReports);
	}

	private void populateCoverPageImages(
			PlanReviewReportForm planReviewReportForm,
			HttpServletRequest request) throws SystemException {

		List<PlanReviewCoverImageDetails> coverImageDetails = PlanReviewReportUtils
				.getPlanReviewCoverPageImageList(request);
			
		planReviewReportForm.getStockCoverPageImages().clear();
		for (PlanReviewCoverImageDetails coverImageDetail : coverImageDetails) {
			if (coverImageDetail.isDefaultCoverPageImage()) {
				planReviewReportForm
						.setDefaultCoverPageImage(new CoverPageImage(
								coverImageDetail));
			} else {
				planReviewReportForm.getStockCoverPageImages().add(
						new CoverPageImage(coverImageDetail));
			}
		}

	}

	/**
	 * This method will be used to display pop up for the ChangeImage for
	 * Customize Page.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/Customize/" ,params={"task=coverImagePage"} , method =  {RequestMethod.POST}) 
	public String doCoverImagePage (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCoverImage() in PlanReviewReportStep2Action");
		}

		

		PlanReviewReportUIHolder planReviewReportUIHolder = getPlanReviewReportUIHolder(
				form.getFilterContractNumber(),
				form.getDisplayContractReviewReports());

		if (planReviewReportUIHolder.isUserSelectedCoverImageIndicator()) {
			if (planReviewReportUIHolder.getUserUploadedCoverPage() != null) {
				form
						.setUploadCoverImage(planReviewReportUIHolder
								.getUserUploadedCoverPage());
			} else {
				form.setUploadCoverImage(null);
			}
		} else {
			form.setUploadCoverImage(null);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doCoverImage() in PlanReviewReportStep2Action.");
		}

		return forwards.get(getTask(request));
	}

	/**
	 * This method will be used to upload cover Image for Customize Page.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/Customize/", params={"task=removeLogo"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doRemoveLogo (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doUploadCoverImage() in PlanReviewReportStep2Action");
		}

		
		List<PlanReviewReportUIHolder> selectedRecordList = form
				.getDisplayContractReviewReports();

		PlanReviewReportUIHolder selectedRecordVo = getPlanReviewReportUIHolder(
				form.getFilterContractNumber(), selectedRecordList);

		if (selectedRecordVo != null) {

			selectedRecordVo.setLogoImageName("");
			selectedRecordVo.setCompanyName("");
			form.setCompanyName("");
			form.setUploadImage(null);
			selectedRecordVo.setUserUploadedLogoImage(false);
			selectedRecordVo.setUserUploadedLogoPage(null);
		} 
		
		constructResponse(response,
				form.getIndex());
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doUploadCoverImage() in PlanReviewReportStep2Action.");
		}

		return null;

	}

	/**
	 * This method will be used to validate Uploaded cover Image attributes
	 * 
	 * @param MultipartFile
	 *            -image
	 * @param String
	 *            -imageName
	 * @return String-responseText
	 * @throws IOException
	 */
	private String validateUploadedCoverImage(MultipartFile image) {
		return validateUploadedImage(image, BDConstants.COVERIMAGE);
	}

	/**
	 * This method will be used to upload cover Image for Customize Page.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/Customize/", params={"task=uploadCoverImage"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doUploadCoverImage (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form,BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doUploadCoverImage() in PlanReviewReportStep2Action");
		}


		MultipartFile uploadedCoverImageFile = form.getUploadCoverImage();

		String responseText = StringUtils.EMPTY;
		String errorText = StringUtils.EMPTY;

		if (uploadedCoverImageFile == null
				|| StringUtils.isBlank(uploadedCoverImageFile.getOriginalFilename())) {
			errorText = "Image can't be uploaded";

		} else {
			errorText = validateUploadedCoverImage(uploadedCoverImageFile);
		}

		if (StringUtils.isNotEmpty(errorText)) {
			// Sending the error response back to AJAX call
			responseText = "{\"hasError\":\"true\",\"error\":\"" + errorText
					+ "\"}";

			constructResponse(response, responseText);
			return null;
		}

		String fileName = FilenameUtils.removeExtension(uploadedCoverImageFile
				.getOriginalFilename());

		PlanReviewReportUIHolder planReviewReportUIHolder = getPlanReviewReportUIHolder(
				form.getFilterContractNumber(),
				form.getDisplayContractReviewReports());

		try {
			planReviewReportUIHolder.setCoverImageName(fileName);
			planReviewReportUIHolder
					.setUserUploadedCoverPage(uploadedCoverImageFile);
			planReviewReportUIHolder.setCmaSelectedCoverImageIndicator(false);
			planReviewReportUIHolder.setUserSelectedCoverImageIndicator(true);

			uploadedCoverImageFile
					.getBytes();

			responseText = "{\"name\":\"" + fileName + "\",\"index\":\""
					+ form.getIndex() + "\"}";

			// Sending the response back to AJAX call
			constructResponse(response, responseText);

		} catch (IOException exception) {
			logger.debug("Exception in Uploaded Cover Image: "
					+ uploadedCoverImageFile.getOriginalFilename(), exception);
			errorText = "Image can't be uploaded";
			responseText = "{\"hasError\":\"true\",\"error\":\"" + errorText
					+ "\"}";
			constructResponse(response, responseText);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doUploadCoverImage() in PlanReviewReportStep2Action.");
		}

		return null;

	}

	/**
	 * This method is a generic Image validation method for Cover and Log Image.
	 * 
	 * @param MultipartFile
	 *            -image
	 * @param String
	 *            -imageName
	 * @return String-responseText
	 * @throws IOException
	 */
	private String validateUploadedImage(MultipartFile image,
			String imageUploadedType) {

		List<String> acceptableFormat = new ArrayList<String>();
		acceptableFormat.add("jpeg");
		acceptableFormat.add(FILE_JPG_EXTENTION);
		// Commented as part of requirement changes
		/*
		 * acceptableFormat.add("png"); acceptableFormat.add("gif");
		 */

		String responseText = StringUtils.EMPTY;
		//Commented the below code as few png images was getting uploaded
		//String content = image.getContentType();
	
		String imageName =image.getOriginalFilename();
		String extension =FilenameUtils.getExtension(imageName);
			
		StringBuffer errorMsg = new StringBuffer();
		
		BufferedImage buffered = null;
		try {
			buffered = ImageIO.read(image.getInputStream());			
			ColorModel colorModel= buffered.getColorModel();
			ColorSpace colorSpace= colorModel.getColorSpace();
			
			// Validate color type of uploaded image (Cover image and Logo Image)
			if(!colorSpace.isCS_sRGB()) {
				errorMsg.append(ContentHelper.getContentText(
						BDContentConstants.UPLOADED_IMAGE_COLORTYPE_IS_NOT_RGB,
						ContentTypeManager.instance().MISCELLANEOUS, null));
			}			
		} catch (Exception exception) {
			
			logger.debug("Image can't be uploaded: " + extension, exception);
			
			errorMsg.append(ContentHelper.getContentText(
					BDContentConstants.UPLOADED_IMAGE_COLORTYPE_IS_NOT_RGB,
					ContentTypeManager.instance().MISCELLANEOUS, null));
		} finally {
			if(buffered != null) {
				buffered.flush();
			}
		}
		
		// Validate CoverImage
		/*if (BDConstants.COVERIMAGE.equals(imageUploadedType)) {

			if (!acceptableFormat.contains(extension)) {

				responseText = ContentHelper.getContentText(
						BDContentConstants.UPLOADED_COVER_IMAGE_IS_NOT_JPG_ERROR,
						ContentTypeManager.instance().MISCELLANEOUS, null);

			} else if (image.getFileSize() > TWO_MEGA_BYTES_IN_BYTES) {
				
				if(StringUtils.isNotBlank(errorMsg.toString())) {
					errorMsg.append("<br/>");
				}
				
				errorMsg.append(ContentHelper
						.getContentText(
								BDContentConstants.UPLOADED_COVER_IMAGE_GREATER_THAN_TWO_MB_ERROR,
								ContentTypeManager.instance().MISCELLANEOUS,
								null));

			}
		}

		// Validate LogoImage
		else {

			if (!acceptableFormat.contains(extension)) {
				
				responseText = ContentHelper.getContentText(
						BDContentConstants.UPLOADED_LOGO_IMAGE_IS_NOT_JPG_ERROR,
						ContentTypeManager.instance().MISCELLANEOUS, null);

			} else if (image.getFileSize() > ONE_MEGA_BYTE_IN_BYTES) {
				
				if(StringUtils.isNotBlank(errorMsg.toString())) {
					errorMsg.append("<br/>");
				}
				errorMsg.append(ContentHelper
						.getContentText(
								BDContentConstants.UPLOADED_LOGO_IMAGE_GREATER_THAN_ONE_MB_ERROR,
								ContentTypeManager.instance().MISCELLANEOUS,
								null));

			}
		}*/
		
		
		if(StringUtils.isBlank(responseText)) {
			responseText = errorMsg.toString();
		}

		return responseText;
	}

	/**
	 * This method will be used to update the selected cover image for Customize
	 * Page.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/Customize/", params={"task=selectedCoverImage"} , method={RequestMethod.GET}) 
	public String doSelectedCoverImage (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
	
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSelectedCoverImage() in PlanReviewReportStep2Action");
		}

		
		String orderIndex = request
				.getParameter(BDConstants.COVER_PAGE_IMAGE_ORDER_INDEX);
		String contractNumber = form.getFilterContractNumber();
		String captionName = null;

		if (StringUtils.isBlank(orderIndex)
				|| !StringUtils.isNumeric(orderIndex)) {
			throw new IllegalArgumentException(
					"Invalid COVER_PAGE_IMAGE_ORDER_INDEX : " + orderIndex);
		}

		PlanReviewCoverImageDetails cmaCoverPageImage = new PlanReviewCoverImageDetails();
		cmaCoverPageImage.setOrderIndex(Integer.parseInt(orderIndex));

		List<PlanReviewCoverImageDetails> coverImageDetails = PlanReviewReportUtils
				.getPlanReviewCoverPageImageList(request);

		int location = coverImageDetails.indexOf(cmaCoverPageImage);

		if (location == -1) {
			throw new IllegalArgumentException(
					"Invalid COVER_PAGE_IMAGE_ORDER_INDEX in list: "
							+ orderIndex);
		}

		cmaCoverPageImage = coverImageDetails.get(location);
		CoverPageImage cmacoverPageImage = new CoverPageImage(cmaCoverPageImage);
		captionName = cmacoverPageImage.getCoverPageCaptionCmaValue();

		PlanReviewReportUIHolder planReviewReportUIHolder = getPlanReviewReportUIHolder(
				contractNumber, form.getDisplayContractReviewReports());

		planReviewReportUIHolder.setCoverImageName(captionName);
		
		planReviewReportUIHolder.setCmaSelectedCoverImageIndicator(true);
		planReviewReportUIHolder.setCmaCoverPageImage(cmacoverPageImage);
		planReviewReportUIHolder.setUserSelectedCoverImageIndicator(false);
		planReviewReportUIHolder.setUserUploadedCoverPage(null);

		String responseText = "{\"name\":\"" + captionName + "\",\"index\":\""
				+ form.getIndex() + "\",\"isImageSlected\":\"" + "true" + "\"}";

		// Sending the response back to AJAX call
		constructResponse(response, responseText);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doSelectedCoverImage() in PlanReviewReportStep2Action.");
		}

		return null;
	}

	/**
	 * This method sends response to AJAX calls
	 * 
	 * @param response
	 * @return String -responseText
	 * @throws SystemException
	 */
	private void constructResponse(HttpServletResponse response,
			String responseText) throws SystemException {

		// Sending the response back to AJAX call
		response.setContentType("text/html");
		PrintWriter out;
		try {
			out = response.getWriter();
		} catch (IOException exception) {
			throw new SystemException(exception.getCause(),
					exception.getMessage());
		}
		out.print(responseText);
		out.flush();
	}

	/**
	 * This method will used to pop up the logo image for Customize Page.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	
	@RequestMapping(value ="/planReview/Customize/", params={"task=uploadLogoPage"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doUploadLogoPage (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doUploadLogoPage() in PlanReviewReportStep2Action");
		}
		
		

		PlanReviewReportUIHolder planReviewReportUIHolder = getPlanReviewReportUIHolder(
				form.getFilterContractNumber(),
				form.getDisplayContractReviewReports());
		
		if (planReviewReportUIHolder.isUserUploadedLogoImage()) {
			if (planReviewReportUIHolder.getUserUploadedLogoPage() != null) {
				form
						.setUploadImage(planReviewReportUIHolder
								.getUserUploadedLogoPage());
				form
						.setCompanyName(planReviewReportUIHolder
								.getCompanyName());
			} else {
				form.setUploadImage(null);
				form.setCompanyName(null);
			}
		} else {
			form.setUploadImage(null);
			form.setCompanyName(null);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doUploadLogoPage() in PlanReviewReportStep2Action.");
		}

		return forwards.get(getTask(request));
	}

	

	/**
	 * This method will be used to validate Logo Image attributes
	 * 
	 * @param MultipartFile
	 *            -image
	 * @return String-responseText
	 * @throws IOException
	 */
	private String validateLogoImage(MultipartFile image)  {
		return validateUploadedImage(image, BDConstants.LOGOIMAGE);
	}

	/**
	 * This method uploads the logo image for Customize Page
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	
	@RequestMapping(value ="/planReview/Customize/", params={"task=uploadLogoImage"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doUploadLogoImage (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doUploadCoverImage() in PlanReviewReportStep2Action");
		}

		

 		MultipartFile uploadedLogoImageFile = form.getUploadImage();
		
		PlanReviewReportUIHolder planReviewReportUIHolder = getPlanReviewReportUIHolder(
				form.getFilterContractNumber(),
				form.getDisplayContractReviewReports());
		
		String responseText = StringUtils.EMPTY;
		String errorText = StringUtils.EMPTY;

			if (uploadedLogoImageFile == null
					|| StringUtils.isBlank(uploadedLogoImageFile.getOriginalFilename())) {
				
				if(planReviewReportUIHolder.getUserUploadedLogoPage() == null) {
					errorText = "Image can't be uploaded";
				}else{
					uploadedLogoImageFile=planReviewReportUIHolder.getUserUploadedLogoPage();
				}
	
			} else {
				errorText = validateLogoImage(uploadedLogoImageFile);
			}
		
		String companyError = StringUtils.EMPTY;
		
		if(StringUtils.isBlank(form.getCompanyName())) {
			
			companyError = ContentHelper.getContentText(
					BDContentConstants.UPLOAD_LOGO_COMPANY_NAME_MANDATORY,
					ContentTypeManager.instance().MISCELLANEOUS, null);
		}

		if (StringUtils.isNotEmpty(errorText)
				|| StringUtils.isNotEmpty(companyError)) {
			// Sending the error response back to AJAX call
			responseText = "{\"hasError\":\"true\",\"error\":\"" + errorText
					+ "\",\"companyError\":\"" + companyError + "\"}";

			constructResponse(response, responseText);
			return null;
		}

		String fileName = FilenameUtils.removeExtension(uploadedLogoImageFile
				.getOriginalFilename());


		try {
			planReviewReportUIHolder.setLogoImageName(fileName);
			planReviewReportUIHolder
					.setUserUploadedLogoPage(uploadedLogoImageFile);
			planReviewReportUIHolder.setUserUploadedLogoImage(true);
			planReviewReportUIHolder.setCompanyName(form.getCompanyName());


			responseText = "{\"name\":\"" + fileName + "\",\"index\":\""
					+ form.getIndex() + "\"}";

			// Sending the response back to AJAX call
			constructResponse(response, responseText);

		} catch (Exception exception) {
			logger.debug("Exception in Uploaded Cover Image: "
					+ uploadedLogoImageFile.getOriginalFilename(), exception);
			errorText = "Image can't be uploaded";
			responseText = "{\"hasError\":\"true\",\"error\":\"" + errorText
					+ "\"}";
			constructResponse(response, responseText);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doUploadCoverImage() in PlanReviewReportStep2Action.");
		}

		return null;

	}

	/**
	 * This method is called when the user clicks on Preview-Icone in Step2 page
	 * for open PreviewCoverPage.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/Customize/", params={"task=previewCoverPage"} , method =  {RequestMethod.POST}) 
	public String doPreviewCoverPage (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Inside doPriviewCoverPa5ge");
		}

		
		List<String> fileList = new ArrayList<String>();

		String contractNumber = form.getFilterContractNumber();

		List<PlanReviewReportUIHolder> step2DisplayContractReviewReports = form
				.getDisplayContractReviewReports();

		PlanReviewReportUIHolder uiHolder = getPlanReviewReportUIHolder(
				contractNumber, step2DisplayContractReviewReports);
		
		uiHolder.setPresenterName(form.getPresenterName());

		ByteArrayOutputStream pdfOutStream = prepareXMLandGeneratePreviewPDF(
				uiHolder, fileList);

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline");
		response.setContentLength(pdfOutStream.size());
		
		ServletOutputStream sos = null;
		
		try {
			sos = response.getOutputStream();
			pdfOutStream.writeTo(sos);
		} catch (IOException ioException) {
			throw new SystemException(ioException, "Exception writing pdfData.");
		} finally {
			try {
				if(sos != null){
					sos.flush();
					sos.close();
				}
			} catch (IOException ioException) {
				logger.error(
						"Exception writing pdfData.", ioException);
			}
		}

		deleteCachedPreviewImageFiles(fileList);
		setRegularPageNavigation(request);

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting doPriviewCoverPage");
		}

		return null;
	}

	/**
	 * to delete the cached images uploaded by the user
	 * 
	 * 
	 * @param form
	 */
	private void deleteCachedPreviewImageFiles(List<String> fileList) {

		for (String filePath : fileList) {
			File cacheImagefile = new File(filePath);
			File parentFile  = cacheImagefile.getParentFile();
			if(!cacheImagefile.delete())  {
				logger.error("Could not able delete the file: " + cacheImagefile.getAbsolutePath());
			}
			if(!parentFile.delete()) {
				logger.error("Could not able delete the file: " + parentFile.getAbsolutePath());
			}
		}
	}

	/**
	 * This method will generate the PDF and return a ByteArrayOutputStream
	 * which will be sent back to the user. This method would: - Create the
	 * XML-String from VO. - Create the PDF using the created XML-String and
	 * XSLT file.yes
	 * 
	 * @throws ContentException
	 */
	protected ByteArrayOutputStream prepareXMLandGeneratePreviewPDF(
			PlanReviewReportUIHolder uiHolder, List<String> fileList)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("Inside prepareXMLandGeneratePDF");
		}

		ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();
		try {

			Object xmlTree = preparePlanReviewXMLFromReport(uiHolder, fileList);

			String xsltFileName = getXSLTFileName();
			String configFileName = getFOPConfigFileName();
			
			String xsltfile = ReportsXSLProperties.get(xsltFileName);
			String configfile = ReportsXSLProperties.get(configFileName);
			String includedXSLPath = ReportsXSLProperties
					.get(CommonConstants.INCLUDED_XSL_FILES_PATH);
			if (xmlTree instanceof Document) {
				pdfOutStream = PDFGenerator.getInstance().generatePDFFromDOM(
						(Document) xmlTree, xsltfile, configfile,
						includedXSLPath);
			}

		} catch (Exception exception) {
			String message = null;
			if (exception instanceof ContentException) {
				message = "Error occured while retrieveing CMA Content during PDF creation.";
			} else if (exception instanceof ParserConfigurationException) {
				message = "Error occured while creating Document object during PDF creation.";
			} else if (exception instanceof FOPException
					|| exception instanceof TransformerException
					|| exception instanceof IOException) {
				message = "Error occured during PDF generation.";
			} else {
				message = "Error occured during PDF generation.";
			}

			throw new SystemException(exception, message);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Exiting prepareXMLandGeneratePDF");
		}
		return pdfOutStream;
	}

	/**
	 * This method would return the key present in ReportsXSL.properties file.
	 * This key has the value as path to FOP Configuration file.
	 * 
	 * @return String
	 */
	protected String getFOPConfigFileName() {
		return CommonConstants.FOP_CONFIG_FILE_KEY_NAME;
	}

	/**
	 * This method would return the key present in ReportsXSL.properties file.
	 * This key has the value as path to XSLT file, which will be used during
	 * PDF generation.
	 * 
	 * @return String - XSLT file location.
	 */
	public String getXSLTFileName() {
		return XSLT_FILE_KEY_NAME;
	}

	/**
	 * This is the main method where the XML is generated. The XML will be used
	 * for PDF generation.
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private Document preparePlanReviewXMLFromReport(
			PlanReviewReportUIHolder uiHolder, List<String> fileList)
			throws SystemException, ParserConfigurationException,
			FileNotFoundException, IOException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> preparePlanReviewXMLFromReport().");
		}

		PDFDocument doc = new PDFDocument();

		Element rootElement = doc
				.createRootElement(BDPdfConstants.PLAN_REVIEW_REPORT);
		
		String topLogoImagePath = StringUtils.EMPTY;
		java.net.URL urlTopLogo = PlanReviewReportStep2Controller.class.getResource(TOP_LOGO_IMAGE_PATH);
		if(urlTopLogo != null){
			topLogoImagePath = urlTopLogo.toExternalForm();
		}
			
		if(StringUtils.isNotBlank(topLogoImagePath)) {
			doc.appendTextNode(
					rootElement,
					BDPdfConstants.JH_LOGO_PATH,
					topLogoImagePath);
		}


		doc.appendTextNode(rootElement, BDPdfConstants.ASOF_DATE,
				longMdyDateFormat.format(new Date()));

		Element subElement = doc
				.createElement(BDPdfConstants.PLAN_REVIEW_SELECTED_ELEMENT);

		String monthEndDate = uiHolder.getSelectedReportMonthEndDate();
		try {
			Date periodEnding = slashMdydateFormat.parse(monthEndDate);
			doc.appendTextNode(subElement, BDPdfConstants.TO_DATE,
					longMdyDateFormat.format(periodEnding));
		} catch (ParseException exception) {
			logger.debug("Exception in Parsing Month End Date:" + monthEndDate);
			throw new SystemException(
					"Exception in Parsing Month End Date while previwing cover page pdf:"
							+ monthEndDate);
		}

		doc.appendTextNode(subElement, BDPdfConstants.CONTRACT_NUMBER,
				String.valueOf(uiHolder.getContractNumber()));
		doc.appendTextNode(subElement, BDPdfConstants.CONTRACT_NAME,
				uiHolder.getContractName());

		doc.appendTextNode(subElement, BDPdfConstants.PRESENTER_NAME,
				uiHolder.getPresenterName());
		
		String coverImagePath = StringUtils.EMPTY;
		if (uiHolder.isCmaSelectedCoverImageIndicator()) {

			coverImagePath = uiHolder.getCmaCoverPageImage()
					.getHighResolutionUNCPath()
					+ uiHolder.getCmaCoverPageImage()
							.getHighResolutionImagePath();

		} else if (uiHolder.isUserSelectedCoverImageIndicator()) {
			// convert array of bytes into file

			if (uiHolder.getUserUploadedCoverPage() != null) {

				String fileServerPath = PlanReviewRequestProperties
						.get(PlanReviewRequestProperties.FILE_SERVER_PATH)
						+ PlanReviewRequestProperties
								.get(PlanReviewRequestProperties.PREVIEW_IMAGES_FOLDER);

				coverImagePath = fileServerPath
						+ new Date().getTime() + File.separator
						+ uiHolder.getUserUploadedCoverPage().getOriginalFilename();

				
				resizeAndCachingCoverImageForPreview(uiHolder.getUserUploadedCoverPage().getInputStream(), coverImagePath,
						PlanReviewConstants.MAX_COVER_PAGE_IMAGE_WIDTH, PlanReviewConstants.MAX_COVER_PAGE_IMAGE_HEIGHT);
				
				fileList.add(coverImagePath);
			}
		}

		if (StringUtils.isNotBlank(coverImagePath)) {

			doc.appendTextNode(subElement, BDPdfConstants.COVER_IMAGE,
					coverImagePath);
		}

		String logoImagePath = StringUtils.EMPTY;
		if (uiHolder.isUserUploadedLogoImage()) {

			if (uiHolder.getUserUploadedLogoPage() != null) {
				String fileServerPath = PlanReviewRequestProperties
						.get(PlanReviewRequestProperties.FILE_SERVER_PATH)
						+ PlanReviewRequestProperties
								.get(PlanReviewRequestProperties.PREVIEW_IMAGES_FOLDER);

				logoImagePath = fileServerPath 
						+ new Date().getTime() + File.separator
						+ uiHolder.getUserUploadedLogoPage().getOriginalFilename();


				resizeAndCachingLogoImageForPreview(uiHolder.getUserUploadedLogoPage().getInputStream(),logoImagePath,
						PlanReviewConstants.MAX_LOGO_IMAGE_WIDTH, PlanReviewConstants.MAX_LOGO_IMAGE_HEIGHT);
				
				fileList.add(logoImagePath);

			}
		}
		
		if (StringUtils.isNotBlank(logoImagePath)) {
			doc.appendTextNode(subElement, BDPdfConstants.LOGO_IMAGE, logoImagePath);
		}

		if (uiHolder.isUserUploadedLogoImage()) {
			doc.appendTextNode(subElement, BDPdfConstants.LOGO_IMAGE_UPLOADED,
					"true");
			doc.appendTextNode(subElement, BDPdfConstants.COMPANY_NAME,
					uiHolder.getCompanyName());
			
		} else {
			doc.appendTextNode(subElement, BDPdfConstants.LOGO_IMAGE_UPLOADED,
					"false");

		}

		doc.appendElement(rootElement, subElement);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> preparePlanReviewXMLFromReport().");
		}

		return doc.getDocument();
	}

	

	/**
	 * This method is used to populate data.
	 * 
	 * @param contractReviewReportVOList
	 * @param requestDataRecord
	 * @throws SystemException
	 */
	private void populateActivityDetails(
			List<PlanReviewReportUIHolder> planReviewReportUIHolderList,
			PlanReviewRequestVO planReviewRequestVO, HttpServletRequest request)
			throws SystemException {

		List<ActivityVo> activityList = new ArrayList<ActivityVo>();
		
		for (PlanReviewReportUIHolder uiHolder : planReviewReportUIHolderList) {
			ActivityVo activityVO = new ActivityVo();
			activityVO.setContractId(uiHolder.getContractNumber());
			activityVO.setStatusCode(ActivitySatusCode.PENDING);
			activityVO.setTypeCode(ActivityTypeCode.PUBLISH);
			populateActivityEvent(uiHolder, activityVO);
			activityList.add(populateDocumentPackageVo(uiHolder, activityVO, request));
		}
		planReviewRequestVO.setActivityVoList(activityList);
		
	}

	/**
	 * This method is used to store the Report date from Step2 when user clicks
	 * on GenerateReport.
	 * 
	 * @param contractReviewReportVOList
	 * @param requestDataRecord
	 * @param request
	 * @throws SystemException
	 */
	private ActivityVo populateDocumentPackageVo(
			PlanReviewReportUIHolder planReviewReportUIHolder,
			ActivityVo acitivityVo, HttpServletRequest request)
			throws SystemException {

		PublishDocumentPackageVo documentPackageVo = new PublishDocumentPackageVo();

		// setting Selected Report Period Ending Date
		populatePeriodEndingDateforContractReport(planReviewReportUIHolder,
				documentPackageVo);

		// setting iReport indicator
		documentPackageVo.setSelectedIreportInd(planReviewReportUIHolder
				.isSelectedperformanceAndExpenseRatio());

		List<LabelValueBean> planReviewIndustrySegmentList = PlanReviewReportUtils
				.getPlanReviewIndustrySegmentList(request);

		// setting Industry codes
		
		String industryCodeValue = planReviewReportUIHolder
		.getSelectedIndustrySegment();
		
		// Keep default to '0', in case the industryCodeValue is blank
		Integer industryCode = null;
		if(StringUtils.equals(industryCodeValue, " ") ) {
			industryCode = Integer.valueOf(0);
		}else if(StringUtils.isNotBlank(industryCodeValue)) {
			industryCode = Integer.valueOf(industryCodeValue);
		}
		
		documentPackageVo.setIndustrySegmentCode(industryCode);
		
		
		String industrySegmentName = StringUtils.EMPTY;
		if(industryCode != null) {
			for (LabelValueBean industrySegment : planReviewIndustrySegmentList) {
	
				if (StringUtils.isNotBlank(industrySegment.getLabel())
						&& Integer.valueOf(industrySegment.getLabel()).equals(industryCode) ) {
					industrySegmentName = industrySegment
							.getValue();
					break;
				}
			}
		}
		
		documentPackageVo.setIndustrySegmentName(industrySegmentName);
		// setting Presenter Name
		documentPackageVo.setPresenterName(planReviewReportUIHolder
				.getPresenterName());

		// setting Uploaded/Selected Cover Page Image details
		boolean isUserSelectedCoverImageIndicator = planReviewReportUIHolder
				.isUserSelectedCoverImageIndicator();
		boolean isCmaSelectedCoverImageIndicator = planReviewReportUIHolder
				.isCmaSelectedCoverImageIndicator();

		documentPackageVo
				.setUserSelectedCoverPageImageInd(isUserSelectedCoverImageIndicator);
		documentPackageVo
				.setCmaSelectedCoverPageImageInd(isCmaSelectedCoverImageIndicator);

		// if user selects any default CMA Cover Page Image
		if (isCmaSelectedCoverImageIndicator) {

			documentPackageVo
					.setHighResDefaultCmaCoverPageImageId(planReviewReportUIHolder
							.getCmaCoverPageImage()
							.getHighResolutionImageCmaKey());
			
			documentPackageVo
			.setLowResDefaultCmaCoverPageImageId(planReviewReportUIHolder
					.getCmaCoverPageImage()
					.getLowResolutionImageCmaKey());
		}

		// if user uploads Cover Page Image
		if (isUserSelectedCoverImageIndicator) {

			MultipartFile userUploadedCoverPage = planReviewReportUIHolder
					.getUserUploadedCoverPage();

			documentPackageVo
					.setHighResUploadedCoverPageImageFileName(userUploadedCoverPage
							.getOriginalFilename()); //
			documentPackageVo
					.setLowResUploadedCoverPageImageFileName(userUploadedCoverPage
							.getOriginalFilename());

			resizeCoverPageImage(planReviewReportUIHolder, documentPackageVo);
		}

		// set Uploaded Logo Image details
		if(planReviewReportUIHolder.isUserUploadedLogoImage()){
			
			MultipartFile userUploadedLogoPage = planReviewReportUIHolder.getUserUploadedLogoPage();
			
			documentPackageVo.setHighResUploadedLogoImageFileName(userUploadedLogoPage.getOriginalFilename());
			documentPackageVo.setLowResUploadedLogoImageFileName(userUploadedLogoPage.getOriginalFilename());
			
			resizeLogoImage(planReviewReportUIHolder, documentPackageVo);
			
			documentPackageVo.setUserSelectedLogoImageInd(planReviewReportUIHolder.isUserUploadedLogoImage());
			
			documentPackageVo.setCompanyName(planReviewReportUIHolder.getCompanyName());
		}

		documentPackageVo.setCreatedTimeStamp(new Timestamp(System
				.currentTimeMillis()));

		acitivityVo.setDocumentPackageVo(documentPackageVo);

		return acitivityVo;
	}
	
	private void resizeLogoImage(
			PlanReviewReportUIHolder planReviewReportUIHolder,
			PublishDocumentPackageVo documentPackageVo) throws SystemException {

		MultipartFile userUploadedLogoPage = planReviewReportUIHolder
				.getUserUploadedLogoPage();

		BufferedImage bufferedImage = null;
		ByteArrayOutputStream baos = null;
		BufferedImage resizedImage = null;

		try {

			bufferedImage = ImageIO.read(new ByteArrayInputStream(
					userUploadedLogoPage.getBytes()));

			resizedImage = ImageUtility.resizeImageWithLongestSide(
					bufferedImage, PlanReviewConstants.MAX_LOGO_IMAGE_WIDTH,
					PlanReviewConstants.MAX_LOGO_IMAGE_HEIGHT);

			baos = new ByteArrayOutputStream();
			
			ImageUtility.createJpgImage(resizedImage,
					PlanReviewConstants.DOTS_PER_INCH_300, 0.95f, baos);

			documentPackageVo.setLogoImage(baos.toByteArray());

		} catch (IOException exception) {
			throw new SystemException(exception,
					"Error while reading the file: "
							+ userUploadedLogoPage.getOriginalFilename()
							+ " for the contract: "
							+ planReviewReportUIHolder.getContractNumber());
		} finally {
			if (bufferedImage != null)
				bufferedImage.flush();
			if (resizedImage != null)
				resizedImage.flush();
			if (baos != null) {

				try {
					baos.close();
				} catch (IOException e) {
					logger.error(
							"Exception occurred while closing the ByteArrayOutputStream for the file: "
									+ userUploadedLogoPage.getOriginalFilename()
									+ " for the contract: "
									+ planReviewReportUIHolder
											.getContractNumber(), e);
				}
			}
		}
	}
	
	
	private void resizeCoverPageImage(
			PlanReviewReportUIHolder planReviewReportUIHolder,
			PublishDocumentPackageVo documentPackageVo) throws SystemException {

		MultipartFile userUploadedCoverPage = planReviewReportUIHolder
				.getUserUploadedCoverPage();

		BufferedImage bufferedImage = null;
		ByteArrayOutputStream baos = null;
		BufferedImage resizedImage = null;

		try {

			bufferedImage = ImageIO.read(new ByteArrayInputStream(
					userUploadedCoverPage.getBytes()));

			resizedImage = ImageUtility.resizeImageWithShortestSide(
					bufferedImage,
					PlanReviewConstants.MAX_COVER_PAGE_IMAGE_WIDTH,
					PlanReviewConstants.MAX_COVER_PAGE_IMAGE_HEIGHT);

			baos = new ByteArrayOutputStream();
			ImageUtility.createJpgImage(resizedImage,
					PlanReviewConstants.DOTS_PER_INCH_300, 0.95f, baos);

			documentPackageVo.setCoverImage(baos.toByteArray());

		} catch (IOException exception) {
			throw new SystemException(exception,
					"Error while reading the file: "
							+ userUploadedCoverPage.getOriginalFilename()
							
							+ " for the contract: "
							+ planReviewReportUIHolder.getContractNumber());
		} finally {
			if (bufferedImage != null)
				bufferedImage.flush();
			if (resizedImage != null)
				resizedImage.flush();
			if (baos != null) {

				try {
					baos.close();
				} catch (IOException e) {
					logger.error(
							"Exception occurred while closing the ByteArrayOutputStream for the file: "
									+ userUploadedCoverPage.getOriginalFilename()
									
									+ " for the contract: "
									+ planReviewReportUIHolder
											.getContractNumber(), e);
				}
			}
		}
	}

	/**
	 * This method is used to populate populateActivityEvent data.
	 * 
	 * @param PlanReviewReportUIHolder
	 * @param ActivityVo
	 * @throws SystemException
	 */
	private void populateActivityEvent(
			PlanReviewReportUIHolder planReviewReportUIHolder, ActivityVo dbVo) {
		List<ActivityEventVo> activityEventVoList = new ArrayList<ActivityEventVo>();
		ActivityEventVo activityEventVo = new ActivityEventVo();
		activityEventVo.setActivityEventSourceCode(EventSourceCode.RPS);
		activityEventVo.setActivityEventCode(ActivityEventCode.OPEN_PUBLISH);
		activityEventVo.setStatusMessage(PlanReviewConstants.STATUS_COMPLETED);
		activityEventVo.setCreatedTimeStamp(new Timestamp(System
				.currentTimeMillis()));
		activityEventVoList.add(activityEventVo);
		dbVo.setActivityEventVoList(activityEventVoList);

	}

	private void populatePeriodEndingDateforContractReport(
			PlanReviewReportUIHolder planReviewReportUIHolder,
			PublishDocumentPackageVo dbvo) throws SystemException {

		try {
			//DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			Date formattedDate = slashMdydateFormat.parse(planReviewReportUIHolder
					.getSelectedReportMonthEndDate());
			dbvo.setPeriodEndDate(formattedDate);
		} catch (ParseException exception) {

			new SystemException(exception, "Invalid month end Date: "
					+ planReviewReportUIHolder.getSelectedReportMonthEndDate()
					+ " for Contract Id: "
					+ planReviewReportUIHolder.getContractNumber());
		}

	}


	private void resizeAndCachingCoverImageForPreview(
			InputStream imageInputStream, String coverImagePath,
			float maxWidth, float maxHeight) throws IOException, SystemException {

		// resizing image
		BufferedImage originalImage = ImageIO.read(imageInputStream);
		BufferedImage resizedImage = ImageUtility.resizeImageWithShortestSide(
				originalImage, maxWidth, maxHeight);

		// Caching image
		File file = new File(coverImagePath);

		if (!file.exists()) {
			if(!file.getParentFile().mkdirs()) {
				logger.error("Could not able make parent dirs. " + file.getParentFile().getAbsolutePath());
			}
			
			if(!file.createNewFile()) {
				logger.error("Could not able to create file. " + file.getAbsolutePath());
			}
		}

		FileOutputStream fileOuputStream = null;
		
		try{
			
			fileOuputStream = new FileOutputStream(file);
				ImageIO.write(resizedImage, FILE_JPG_EXTENTION, fileOuputStream);
				
			} catch (IOException e) {
				throw new SystemException(e, 
						"Exception in saving the cover or logo Image to the location: "
								+ coverImagePath);
			} finally {
				
				if(fileOuputStream != null) {
					try {
						fileOuputStream.close();
					} catch (IOException e) {
						logger.error(
								"Exception on closing the fileOuputStream for file: "
										+ coverImagePath, e);
					}
				}
			}
		
		
	}

	private void resizeAndCachingLogoImageForPreview(
			InputStream imageInputStream, String logoImagePath, float maxWidth,
			float maxHeight) throws IOException, SystemException {

		// resizing image
		BufferedImage originalImage = ImageIO.read(imageInputStream);
		BufferedImage resizedImage = ImageUtility.resizeImageWithLongestSide(
				originalImage, maxWidth, maxHeight);

		// Caching image
		File file = new File(logoImagePath);

		if (!file.exists()) {
			if(!file.getParentFile().mkdirs()) {
				logger.error("Could not able make parent dirs. " + file.getParentFile().getAbsolutePath());
			}
			
			if(!file.createNewFile()) {
				logger.error("Could not able to create file. " + file.getAbsolutePath());
			}
		}

		FileOutputStream fileOuputStream = null;

		try {

			fileOuputStream = new FileOutputStream(file);

			ImageIO.write(resizedImage, FILE_JPG_EXTENTION, fileOuputStream);

		} catch (IOException e) {
			throw new SystemException(e,
					"Exception in saving the cover or logo Image to the location: "
							+ logoImagePath);
		} finally {

			if (fileOuputStream != null) {
				try {
					fileOuputStream.close();
				} catch (IOException e) {
					logger.error(
							"Exception on closing the fileOuputStream for file: "
									+ logoImagePath, e);
				}
			}
		}
	}
	private Map<Integer, String> getContractsWhichAlereadyReachedLimit(
			PlanReviewReportForm planReviewReportForm, HttpServletRequest request,
			Timestamp requestedDate) throws SystemException {
				BDUserProfile userProfile = BDSessionHelper.getUserProfile(request); 
				String userProfileId = null;
				if(userProfile.isInMimic()){
					BDUserProfile mimikedUserProfile = PlanReviewReportUtils
							.getMimckingUserProfile(request);
					userProfileId = String.valueOf(mimikedUserProfile.getBDPrincipal().getProfileId());
					
				}else{
					userProfileId = String.valueOf(userProfile.getBDPrincipal().getProfileId());
				}

		List<ActivityVo> actVoList = getActivityVOListForCRequest(planReviewReportForm
				.getDisplayContractReviewReports());
			
		
		Map<Integer, String> contractsWhichAlreadyRequests = null;

		if (actVoList != null && actVoList.size() >0) {
			contractsWhichAlreadyRequests = PlanReviewServiceDelegate
					.getInstance(Environment.getInstance().getApplicationId())
					.validateUserAllowedRequestLimit(actVoList, userProfileId, userProfile.isInMimic(), requestedDate, true);
		}
		
		return contractsWhichAlreadyRequests;
	}

	private List<ActivityVo> getActivityVOListForCRequest(
			List<PlanReviewReportUIHolder> displayContractReviewReports) throws SystemException {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		List<ActivityVo> actVoList = new ArrayList<ActivityVo>();
		for (PlanReviewReportUIHolder uIHolder : displayContractReviewReports) {
			if (uIHolder.isContractSelected()) {
				ActivityVo actVo = new ActivityVo();
				PublishDocumentPackageVo docPkgVo = new PublishDocumentPackageVo();
				actVo.setContractId(uIHolder.getContractNumber());
				try {
					docPkgVo.setPeriodEndDate(formatter.parse(uIHolder
							.getSelectedReportMonthEndDate()));
				} catch (ParseException e) {
					throw new SystemException(e,
							"exception while parsing month end date: "
									+ uIHolder.getSelectedReportMonthEndDate());
				}
				actVo.setDocumentPackageVo(docPkgVo);
				actVoList.add(actVo);
			}
		}
		return actVoList;
	}
	
	
	/**
	 * This method will redirect to the CSRF Error Page.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	@RequestMapping(value ="/planReview/Customize/", params={"action=csrfError","task=csrfError"}  ,method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doCsrfError(@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }

		return forwards.get("csrfErrorPage");
	}
	@RequestMapping(value ="/planReview/Customize/",params={"task=filter"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doFilter (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		 forward=super.doFilter( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/planReview/Customize/" ,params={"task=page"} , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPage (@Valid @ModelAttribute("planReviewReportForm") PlanReviewReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		 forward=super.doPage( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	
	/** avoids token generation as this class acts as intermediate for many
	 * transactions.
	 * 
     * (non-Javadoc) 
     * @see com.manulife.pension.platform.web.controller.BaseAction#isTokenRequired(java.lang.String)
     */
	/*@Override
	protected boolean isTokenRequired(String action) {
		return StringUtils.isNotEmpty(action)
				&& (StringUtils.equalsIgnoreCase(action, "Default"))?true:false;
	}*/
	/**
	 * Returns true if token has to be validated for the particular action call
	 * to avoid CSRF vulnerability else false. (non-Javadoc)
	 * 
	 * @see
	 * com.manulife.pension.ezk.web.BaseController#isTokenValidatorEnabled(java
	 * .lang.String)
	 */
	@Override
	protected boolean isTokenValidatorEnabled(String action) {
		return StringUtils.isNotEmpty(action)
				&& (StringUtils.equalsIgnoreCase(action, "GenerateReport") 
						|| StringUtils.equalsIgnoreCase(action, "UploadCoverImage")
						|| StringUtils.equalsIgnoreCase(action, "UploadLogoImage"))?true:false;
	
	}
	
	
	
}