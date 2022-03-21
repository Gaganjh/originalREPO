package com.manulife.pension.bd.web.bob.planReview;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BdBaseController;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BlockOfBusinessUtility;
import com.manulife.pension.bd.web.bob.planReview.sort.PrintPlanReviewRequestColoumn;
import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportDataValidator;
import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportUtils;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.PlanReviewServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.BusinessUnit;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.Division;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.DocumentFormat;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.DocumentStatus;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.DocumentType;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.ProductSubType;
import com.manulife.pension.service.planReview.util.PlanReviewDocumentServiceConstants.ProductType;
import com.manulife.pension.service.planReview.valueobject.ActivityEventVo;
import com.manulife.pension.service.planReview.valueobject.ActivityVo;
import com.manulife.pension.service.planReview.valueobject.PlanReviewReportDocumentPackage;
import com.manulife.pension.service.planReview.valueobject.PlanReviewReportDocumentVo;
import com.manulife.pension.service.planReview.valueobject.PlanReviewRequestVO;
import com.manulife.pension.service.planReview.valueobject.PrintDocumentPackgeVo;
import com.manulife.pension.service.planReview.valueobject.ShippingVO;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.PlanReviewConstants;
import com.manulife.pension.util.PlanReviewConstants.ActivityEventCode;
import com.manulife.pension.util.PlanReviewConstants.ActivityEventStatus;
import com.manulife.pension.util.PlanReviewConstants.ActivitySatusCode;
import com.manulife.pension.util.PlanReviewConstants.ActivityTypeCode;
import com.manulife.pension.util.PlanReviewConstants.Country;
import com.manulife.pension.util.PlanReviewConstants.EventSourceCode;
import com.manulife.pension.util.PlanReviewConstants.PlanReviewDocumentType;
import com.manulife.pension.util.PlanReviewConstants.RequestTypeCode;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * This action class get the requested plan Review reports and shipping details
 * to ship the printed report.
 * 
 * @author Manjunath
 * 
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"planReviewPrintForm"})

public class PrintPlanReviewReportController extends BasePlanReviewReportController {
	@ModelAttribute("planReviewPrintForm")
	public PlanReviewPrintForm populateForm() 
	{
		return new PlanReviewPrintForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/planReview/printBobPlanReviewReports.jsp");
		forwards.put("default","/planReview/printBobPlanReviewReports.jsp");
		forwards.put("sort","/planReview/printBobPlanReviewReports.jsp");
		forwards.put("printShipping","/WEB-INF/planReview/common/printShippingPage.jsp");
		forwards.put("printConfirm","/WEB-INF/planReview/common/printConfirmPage.jsp");
		forwards.put("contractReviewReportStep1Page","redirect:/do/bob/planReview/");
		forwards.put("history","redirect:/do/bob/planReview/History/");
		forwards.put("planReviewRequest","redirect:/do/bob/planReview/");
		forwards.put("homePage","redirect:/do/home/");
		forwards.put("submit","redirect:/planReview/printBobplanReviewReports");
		forwards.put("errorMsg","redirect:/planReview/printBobPlanReviewReports.jsp");
	}
	
	protected static final Logger logger = Logger.getLogger(PrintPlanReviewReportController.class);
	
	private static final FastDateFormat SHORT_MDY_FORMATTER = FastDateFormat
			.getInstance(RenderConstants.MEDIUM_MDY_SLASHED);
	
	protected static String DEFAULT_PRINT_CONFIRMATION_SORT = "contractNumber";
	protected static String DEFAULT_PRINT_CONFIRMATION_SORT_DIRECTION = "asc";

	public PrintPlanReviewReportController() {
		super(PrintPlanReviewReportController.class);

	}


	/**
	 * This method is used to display page with requested plan review reports.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException bob
	 */
	@RequestMapping(value ="/planReview/Print/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("planReviewPrintForm") PlanReviewPrintForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
			logger.debug("entry -> doDefault() in PlanReviewReportPrintAction");
		}

		// To get the request id from Result action
		String planReviewRequestId = (String) request.getSession(false)
				.getAttribute(BDConstants.PLAN_REVIEW_REQUEST_ID);
		
		// To get the activity id from History details action
		String planReviewActivityId = (String) request.getSession(false)
				.getAttribute(BDConstants.PLAN_REVIEW_ACTIVITY_ID);

		if (StringUtils.isBlank(planReviewRequestId)
				|| planReviewRequestId == null || !StringUtils.isNumeric(planReviewRequestId)) {
			
			if (StringUtils.isBlank(planReviewActivityId)
					|| planReviewActivityId == null || !StringUtils.isNumeric(planReviewActivityId)) {
				return forwards.get(BDConstants.FORWARD_PLAN_REVIEW_REPORTS_STEP1_PAGE);
			}
		}

		// removed the session attribute from the session.
		request.getSession(false)
				.removeAttribute(BDConstants.PLAN_REVIEW_REQUEST_ID);
		
		// removed the session attribute from the session.
		request.getSession(false)
				.removeAttribute(BDConstants.PLAN_REVIEW_ACTIVITY_ID);

		

		// set to no of copies drop down value
		actionForm.setNumberOfCopies(PlanReviewReportUtils
				.getNumberOfCopiesDropDownValues());

		// To get selected contracts in results page from DB and populate in
		// print page
		boolean isPublishRequest = false;

		PlanReviewRequestVO planReviewRequestVo = (PlanReviewRequestVO) PlanReviewServiceDelegate
				.getInstance(Environment.getInstance().getApplicationId())
				.retrievePlanReviewRequestDetails(
						planReviewRequestId, planReviewActivityId, isPublishRequest);

		List<PlanReviewReportUIHolder> uiHolderList = getPrintPlanReviewReports(planReviewRequestVo);
		
		
		
		
		actionForm.setDisplayCotractReviewReports(uiHolderList);
		 forward = doCommon( actionForm, request, response);

		Map<String, String> usaStates = EnvironmentServiceDelegate
				.getInstance().getUSAStates();

		Map<String, String> canadaProvinces = PlanReviewServiceDelegate
				.getInstance(Environment.getInstance().getApplicationId())
				.getCanadianProvinces();

		actionForm.setUsaStates(usaStates);
		actionForm.setCanadianProvinces(canadaProvinces);

		Map<String, String> countryMap = actionForm.getCountryMap();
		
		for(Country country: Country.values()){
			countryMap.put(country.getCode(), country.getDescription());
		}
		
		//TODO need to checkthe value of mapping.getparameter
		request.setAttribute(BDConstants.BOB_LEVEL_RESULTS_PAGE,"bob");

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doDefault() in PlanReviewReportPrintAction.");
		}

		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@RequestMapping(value ="/planReview/Print/",params= {"task=planReviewRequest"}, method =  {RequestMethod.POST}) 
	public String doPlanReviewRequest(@Valid @ModelAttribute("planReviewPrintForm") PlanReviewPrintForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {	
		String forward=preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input"); //if input forward not //available, provided default
	       }
		}

		HttpSession session = request.getSession(false);

		session.setAttribute(BDConstants.REQUEST_FROM_HISTORY_OR_PRINT, Boolean.TRUE);

		setRegularPageNavigation(request);

		return forwards.get(getTask(request));
	}

	/**
	 * This method will get the required information.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	protected String doCommon(
			PlanReviewPrintForm	 actionForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon() in BoBContractReviewRequestAction");
		}

		

		populateReportForm( actionForm, request);

		List<PlanReviewReportUIHolder> planReviewPrintVOList = actionForm
				.getDisplayContractReviewReports();

		Collections.sort(planReviewPrintVOList, PrintPlanReviewRequestColoumn
				.getPrintPlanReviewRequestColoumn(actionForm.getSortField())
				.getComparatorInstance(actionForm.getSortDirection()));
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doCommon() in BoBContractReviewRequestAction.");
		}

		return forwards.get(getTask(request));
	}
	
	private List<PlanReviewReportUIHolder> getPrintPlanReviewReports(
			PlanReviewRequestVO planReviewRequestVo) {

		List<PlanReviewReportUIHolder> planReviewPrintList = new ArrayList<PlanReviewReportUIHolder>();
		List<ActivityVo> activityVoList = planReviewRequestVo
				.getActivityVoList();

		for (ActivityVo activityVo : activityVoList) {
			
			PlanReviewReportUIHolder uiHolder = new PlanReviewReportUIHolder();
			
			uiHolder.setDocumentPakgId(activityVo.getActivityId());
			uiHolder.setContractNumber(activityVo.getContractId());
			uiHolder.setContractName(activityVo.getContractName());
			uiHolder.setPublishDocumentActivityId(activityVo.getActivityId());
			uiHolder.setCreatedTimeStamap(activityVo
					.getCreatedTS());
			PrintDocumentPackgeVo docpackageVo = (PrintDocumentPackgeVo) activityVo
					.getDocumentPackageVo();
			uiHolder.setSelectedReportMonthEndDate(DateRender.formatByPattern(
					docpackageVo.getPeriodEndDate(), null,
					RenderConstants.MEDIUM_YMD_DASHED,
					RenderConstants.MEDIUM_MDY_SLASHED));
			uiHolder.setAgencySellerId(planReviewRequestVo.getBrokerSellerId());
			
			// Set default to 2
			uiHolder.setNumberOfCopies("2");
			
			planReviewPrintList.add(uiHolder);
		}

		return planReviewPrintList;
	}

	/**
	 * This method will used to handle the Shipping data for BOB Pages.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/Print/",params= {"task=printShipping"},  method =  {RequestMethod.POST}) 
	public String doPrintShipping(@Valid @ModelAttribute("planReviewPrintForm") PlanReviewPrintForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
			logger.debug("entry -> doPrintShipping() in PlanReviewReportPrintAction");
		}

		
		List<PlanReviewReportUIHolder> uiHolderList = actionForm
				.getDisplayContractReviewReports();

		// removing the existing errors from request
		request.removeAttribute(BdBaseController.ERROR_KEY);

		// refreshed the report selection
		for (PlanReviewReportUIHolder uiHolder : uiHolderList) {
			uiHolder.setPrintContractSelected(false);
			uiHolder.setNumberOfCopies("2");
		}

		String jsonObj = request.getParameter("planreviewPrintJsonObj");
		String jsonText2 = "[" + jsonObj + "]";
		JsonParser parser = new JsonParser();
		JsonArray jsonArr = (JsonArray) parser.parse(jsonText2);

		// Json object should be get for bob lvl and contract lvl and for any
		// no. of contracts
		if (uiHolderList != null && StringUtils.isNotEmpty(jsonObj)) {
			for (int i = 0; i < jsonArr.size(); i++) {

				JsonObject obj = (JsonObject) jsonArr.get(i);

				String contractId = (obj.get("ContractId")).getAsString();
				String nuOfCopies = (obj.get("Copies")).getAsString();

				PlanReviewReportUIHolder planReviewReportUIHolder = getPlanReviewReportUIHolder(
						contractId, uiHolderList);
				planReviewReportUIHolder.setPrintContractSelected(true);
				planReviewReportUIHolder.setNumberOfCopies(nuOfCopies);
			}
		}

		ShippingVO defaultShippingAddress = null;
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		
		if (!userProfile.isInternalUser()) {
			// external user
			defaultShippingAddress = PlanReviewReportUtils
					.populateShippingdetails(getBrokerId(request));

			actionForm
					.setDefaultBrokerShippingAddress(defaultShippingAddress);
		}

		if (defaultShippingAddress != null) {

			defaultShippingAddress.setDefaultAddressSelected(true);
			defaultShippingAddress.setMakeDefaultAddressSelected(false);
			defaultShippingAddress.setDisableDefaultAddressCheckbox(false);
			defaultShippingAddress.setDisbaleMakeDefaultAddressSelected(false);

			actionForm.setShippingVO(defaultShippingAddress);
			actionForm.setDefaultAddress(true);

		} else {

			ShippingVO shippingVO = new ShippingVO();

			// set defaults
			shippingVO.setRecipientCountry(Country.USA.getCode());

			shippingVO.setDefaultAddressSelected(false);
			shippingVO.setMakeDefaultAddressSelected(false);
			shippingVO.setDisableDefaultAddressCheckbox(true);
			
			if (userProfile.isInternalUser()) {
				shippingVO.setDisbaleMakeDefaultAddressSelected(true);
			} else {
				shippingVO.setDisbaleMakeDefaultAddressSelected(false);
			}

			actionForm.setDefaultBrokerShippingAddress(null);
			actionForm.setShippingVO(shippingVO);
			actionForm.setDefaultAddress(false);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doPrintShipping() in PlanReviewReportPrintAction.");
		}
		return forwards.get(getTask(request));
	}

	/**
	 * This method will used to handle the Shipping data for BOB Pages.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/Print/",params= {"task=printConfirm"},  method =  {RequestMethod.POST}) 
	public String doPrintConfirm(@Valid @ModelAttribute("planReviewPrintForm") PlanReviewPrintForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
			logger.debug("entry -> doPrintConfirm() in PlanReviewReportPrintAction");
		}

		

		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();

		ShippingVO newShippingVO = new ShippingVO();

		String jsonObj = request.getParameter("planreviewPrintJsonObj");

		if (StringUtils.isNotEmpty(jsonObj)) {

			JsonParser parser = new JsonParser();
			JsonObject obj = (JsonObject) parser.parse(jsonObj);

			boolean isDefaultAddressSelected = StringUtils.equalsIgnoreCase(obj
					.get("defaultAddressCheckBoxInd").getAsString(), "on");
			
			newShippingVO.setDefaultAddressSelected(isDefaultAddressSelected);

				newShippingVO.setRecipientName((obj.get("recipientName")).getAsString());
				newShippingVO.setRecipientPhoneNumber((obj.get("recipientPhoneNumber"))
						.getAsString());
				newShippingVO.setRecipientEmail((obj.get("recipientEmail"))
						.getAsString());
				newShippingVO.setRecipientCompanyName((obj.get("recipientCompany"))
						.getAsString());
				newShippingVO.setRecipientAddressLine1((obj.get("addressLine1"))
						.getAsString());
				newShippingVO.setRecipientAddressLine2((obj.get("addressLine2"))
						.getAsString());
				newShippingVO.setRecipientCity((obj.get("recipientCity")).getAsString());
				newShippingVO.setRecipientState((obj.get("recipientState"))
						.getAsString());

				newShippingVO.setRecipientZip((obj.get("recipientZipCode")).getAsString());

				newShippingVO.setRecipientCountry((obj.get("recipientCountry"))
						.getAsString());

				newShippingVO.setMakeDefaultAddressSelected(StringUtils
						.equalsIgnoreCase(obj.get("makeDefaultAddressCheckboxInd")
								.getAsString(), "on"));

				if (actionForm.getShippingVO() != null) {
					newShippingVO
							.setDisableDefaultAddressCheckbox(actionForm
									.getShippingVO()
									.isDisableDefaultAddressCheckbox());
				}

				actionForm.setShippingVO(newShippingVO);

				errorMessages = PlanReviewReportDataValidator
						.validateShippingpage(newShippingVO);

				if (!errorMessages.isEmpty()) {
					setErrorsInSession(request, errorMessages);
					return forwards.get( "printShipping");
				}
			} else {
				return forwards.get( "printShipping");
			}

		// Overrides default address details with new address details if
		// make default address checkbox is selected
		boolean isMakeDefaultAddressSelected = false;

		if (newShippingVO.isMakeDefaultAddressSelected()) {
			isMakeDefaultAddressSelected = true;
		}

		ShippingVO shiipingAddress = actionForm.getShippingVO();

		if (isMakeDefaultAddressSelected
				&& actionForm.getDefaultBrokerShippingAddress() != null) {
			shiipingAddress.setBrokerHavingAlreadyDefaultAddress(true);
		} else {
			shiipingAddress.setBrokerHavingAlreadyDefaultAddress(false);
		}

		shiipingAddress.setBrokerId(getBrokerId(request));

		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);

		
		if (!shiipingAddress.isBrokerHavingAlreadyDefaultAddress()) {
			
			if (!userProfile.isInMimic()) {
				shiipingAddress.setUserProfileId(BigDecimal.valueOf(userProfile
						.getBDPrincipal().getProfileId()));
			} else {
				// set mimicked profileID
				BDUserProfile mimickingUserProfile = BlockOfBusinessUtility
						.getMimckingUserProfile(request);

				shiipingAddress.setUserProfileId(BigDecimal
						.valueOf(mimickingUserProfile.getBDPrincipal()
								.getProfileId()));
			}
			
		} else {

			if (!userProfile.isInMimic()) {
				shiipingAddress.setLastUpdatedProfileid(BigDecimal
						.valueOf(userProfile.getBDPrincipal().getProfileId()));
			} else {
				// set mimicked profileID
				BDUserProfile mimickingUserProfile = BlockOfBusinessUtility
						.getMimckingUserProfile(request);

				shiipingAddress.setLastUpdatedProfileid(BigDecimal
						.valueOf(mimickingUserProfile.getBDPrincipal()
								.getProfileId()));
			}
		}

		storeAndPopulateConfirmationdetails(
				actionForm, request, shiipingAddress,
				isMakeDefaultAddressSelected);
		
		Collections.sort(actionForm.getPlanPrintRequestedVOList(), PrintPlanReviewRequestColoumn
				.getPrintPlanReviewRequestColoumn(getPrintConfirmationSort())
				.getComparatorInstance(getPrintConfirmationSortDirection()));
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doPrintConfirm() in PlanReviewReportPrintAction.");
		}

		return forwards.get( getTask(request));

	}
	
	protected String getPrintConfirmationSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	protected String getPrintConfirmationSort() {
		return DEFAULT_SORT;
	}
	
	
		
	/**
	 * This method will used to handle the Shipping data for BOB Pages.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/Print/",params= {"task=clearPrintReports"},  method =  {RequestMethod.POST}) 
	public String doClearPrintReports(@Valid @ModelAttribute("planReviewPrintForm") PlanReviewPrintForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
			logger.debug("entry -> doClearPrintReports() in PlanReviewReportPrintAction");
		}

		
		actionForm.getPlanPrintRequestedVOList().clear();
		
		// refreshed the report selection
		for (PlanReviewReportUIHolder uiHolder : actionForm
				.getDisplayContractReviewReports()) {
			uiHolder.setPrintContractSelected(false);
			uiHolder.setNumberOfCopies("2");
		}
		
		// Sending the response back to AJAX call
		constructResponse(response, "{}");

		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doClearPrintReports() in PlanReviewReportPrintAction");
		}
		
		return null;
		
	}
	/**
	 * This method will used to handle the Shipping data for BOB Pages.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	
	@RequestMapping(value ="/planReview/Print/",params= {"task=validateUserPrintRequestLimit"},  method =  {RequestMethod.POST}) 
	public String doValidateUserPrintRequestLimit(@Valid @ModelAttribute("planReviewPrintForm") PlanReviewPrintForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
			logger.debug("entry -> doPrintShipping() in PlanReviewReportPrintAction");
		}

		
		List<PlanReviewReportUIHolder> uiHolderList = actionForm
				.getDisplayContractReviewReports();

		// removing the existing errors from request
		request.removeAttribute(BdBaseController.ERROR_KEY);

		// refreshed the report selection
		for (PlanReviewReportUIHolder uiHolder : uiHolderList) {
			uiHolder.setPrintContractSelected(false);
			uiHolder.setNumberOfCopies("2");
		}

		String jsonObj = request.getParameter("planreviewPrintJsonObj");
		String jsonText2 = "[" + jsonObj + "]";
		JsonParser parser = new JsonParser();
		JsonArray jsonArr = (JsonArray) parser.parse(jsonText2);

		// Json object should be get for bob lvl and contract lvl and for any
		// no. of contracts
		if (uiHolderList != null && StringUtils.isNotEmpty(jsonObj)) {
			for (int i = 0; i < jsonArr.size(); i++) {

				JsonObject obj = (JsonObject) jsonArr.get(i);

				String contractId = (obj.get("ContractId")).getAsString();

				PlanReviewReportUIHolder planReviewReportUIHolder = getPlanReviewReportUIHolder(
						contractId, uiHolderList);
				planReviewReportUIHolder.setPrintContractSelected(true);
			}
		}

		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		//CR#9 Allowed Request Limit Condition” to document the error validation parameters which will be used to limit excess PDF and Print requests
		String userProfileId = null;
		if(userProfile.isInMimic()){
			BDUserProfile mimikedUserProfile = PlanReviewReportUtils
					.getMimckingUserProfile(request);
			userProfileId = String.valueOf(mimikedUserProfile.getBDPrincipal().getProfileId());
			
		}else{
			userProfileId = String.valueOf(userProfile.getBDPrincipal().getProfileId());
		}
		Map<Integer, String> contractMap = validateUserAllowedPrintPlanReviewRequestLimit(uiHolderList, userProfileId, userProfile.isInMimic(), 
				 new Timestamp(System.currentTimeMillis()), false);
		String contractIdRespone = "";
		StringBuffer contractIds = new StringBuffer();
		if(contractMap !=null){
			for(Entry<Integer, String> entry :contractMap.entrySet()){
				if(PlanReviewConstants.YES.equals(entry.getValue()))
				contractIds.append(entry.getKey() +",&nbsp;");
			}
			
		}
		if(contractIds.lastIndexOf(",") != -1){
			contractIdRespone = contractIds.substring(0, contractIds.lastIndexOf(","));
		}else{
			contractIdRespone = contractIds.toString();
		}
		constructResponse(response, contractIdRespone);
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doPrintShipping() in PlanReviewReportPrintAction");
		}
		return null;
	}
	@RequestMapping(value ="/planReview/Print/",params= {"task=displayPrintErrors"},  method =  {RequestMethod.POST}) 
	public String doDisplayPrintErrors(@Valid @ModelAttribute("planReviewPrintForm") PlanReviewPrintForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
			logger.debug("entry -> doPrintShipping() in PlanReviewReportPrintAction");
		}

		
		List<PlanReviewReportUIHolder> uiHolderList = actionForm
				.getDisplayContractReviewReports();
		String contractString = actionForm.getContractIdList();
		
		if(StringUtils.isBlank(contractString)) {
			return forwards.get("input");
		}
		
		String [] contrctIds = contractString.split(",&nbsp;");
		
		ArrayList<GenericException> errors = new ArrayList<GenericException>();
		
		for (String  contrctId : contrctIds ) {
			PlanReviewReportUIHolder planReviewReportUIHolder=getPlanReviewReportUIHolder(contrctId, uiHolderList);
		if( planReviewReportUIHolder!= null){
				errors.add(new ValidationError("selectedReportMonthEndDate"
						+ uiHolderList.indexOf(planReviewReportUIHolder),
						BDErrorCodes.ALLOWED_PLAN_REVIEW_REQUEST_LIMIT,
						new Object[]{contractString}));
			}
		}
		if(!errors.isEmpty()){
			setErrorsInSession(request, errors);
		}
		
		return forwards.get("input");
	}
	
	
	/**
	 * This method will show error message Print Request page
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/Print/",params= {"task=errorMsg"},  method =  {RequestMethod.POST}) 
	public String doErrorMsg(@Valid @ModelAttribute("planReviewPrintForm") PlanReviewPrintForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
			logger.debug("entry -> doErrorMsg() in PlanReviewPrintAction");
		}
		request.setAttribute(BDConstants.BOB_LEVEL_RESULTS_PAGE,
				BDConstants.PR_BOB_LEVEL_PARAMETER);
		
		List<GenericException> errors = new ArrayList<GenericException>();
		actionForm.setErrorExists(Boolean.FALSE);

		errors.addAll(validateUserContractSelected(
				actionForm));
		if(!errors.isEmpty()){
			actionForm.setErrorExists(Boolean.TRUE);
			setErrorsInSession(request, errors);
			return forwards.get("input");
		}

		return forwards.get( getTask(request));
	}
	
	/**
	 * 
	 * @param errorMessages
	 * @param selectedIndustrySegment
	 * @param selectedlist
	 * @throws SystemException
	 */
	private ArrayList<GenericException> validateUserContractSelected(
			PlanReviewReportForm planReviewReportForm)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> validateSelectedIndustrySegmentValue");
		}

		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();

		
				
					errorMessages.add(new ValidationError(null, BDErrorCodes.SELECT_CONTRACT_FOR_PRINT_ERROR,
							Type.error));
	

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> validateSelectedIndustrySegmentValue");
		}

		return errorMessages;
	}

	// method to populate Confirmation details
	protected void storeAndPopulateConfirmationdetails(
			PlanReviewPrintForm printForm, HttpServletRequest request,
			ShippingVO shippingAddress, boolean isMakeDefaultAddressSelected)
					throws SystemException {

		List<PlanReviewReportUIHolder> uiHolderList = printForm
				.getDisplayContractReviewReports();

		List<PlanReviewReportUIHolder> selectedReportsForPrint = new ArrayList<PlanReviewReportUIHolder>();
		
		
		// Check for all the contracts for which print is requested
		for (PlanReviewReportUIHolder uiHolder : uiHolderList) {
			if (uiHolder.isPrintContractSelected()) {
				uiHolder.setShippingVO(shippingAddress);
				selectedReportsForPrint.add(uiHolder);
			}
		}
		
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		//CR#9 Allowed Request Limit Condition” to document the error validation parameters which will be used to limit excess PDF and Print requests
		String userProfileId = null;
		if(!userProfile.isInternalUser() && userProfile.isInMimic()){
			BDUserProfile mimikedUserProfile = PlanReviewReportUtils
					.getMimckingUserProfile(request);
			userProfileId = String.valueOf(mimikedUserProfile.getBDPrincipal().getProfileId());
			
		}else{
			userProfileId = String.valueOf(userProfile.getBDPrincipal().getProfileId());
		}
		
		Timestamp requestedDate = new Timestamp(System.currentTimeMillis());
		
		Map<Integer, String> contractsWhichAlereadyReachedLimitMap =  validateUserAllowedPrintPlanReviewRequestLimit(selectedReportsForPrint, userProfileId, 
				userProfile.isInMimic(), requestedDate, true);
		
		if(contractsWhichAlereadyReachedLimitMap != null){
			for ( Entry<Integer, String> contractEntry : contractsWhichAlereadyReachedLimitMap.entrySet()) {
				if(PlanReviewConstants.YES.equalsIgnoreCase(contractEntry.getValue())){
					Integer contractId = contractEntry.getKey();
					selectedReportsForPrint.remove(getPlanReviewReportUIHolder(String.valueOf(contractId), selectedReportsForPrint));
				}
			}
		}
		
		PlanReviewRequestVO printPlanReviewRequest =null;
		
		if (!selectedReportsForPrint.isEmpty()) {
			
			PlanReviewRequestVO getPlanReviewDocListRequest = populateRequestDetails(
					selectedReportsForPrint, request,
					RequestTypeCode.GET_DOC_LIST);
			getPlanReviewDocListRequest.setCreatedTS(requestedDate);
			
			// insert getdocList
			getPlanReviewDocListRequest = PlanReviewServiceDelegate.getInstance(
					Environment.getInstance().getApplicationId())
					.insertGetDocumentListRequestDetails(
							getPlanReviewDocListRequest);
			
			printPlanReviewRequest = populateRequestDetails(
					selectedReportsForPrint, request, RequestTypeCode.PRINT_DOC);
			printPlanReviewRequest.setCreatedTS(requestedDate);
			
			// insert print doc details
			printPlanReviewRequest = PlanReviewServiceDelegate.getInstance(
					Environment.getInstance().getApplicationId())
					.insertPrintPlanReviewRequestDetails(
							printPlanReviewRequest, shippingAddress);
			
			populateTibcoGetDocumentListProperties(
					getPlanReviewDocListRequest, selectedReportsForPrint);
			
			for (ActivityVo getDocListActivity : getPlanReviewDocListRequest
					.getActivityVoList()) {

				PlanReviewReportUIHolder uiHolder = getPlanReviewReportUIHolder(
						String.valueOf(getDocListActivity.getContractId()),
						selectedReportsForPrint);

				Date reportMonthEndDate = null;

				try {
					reportMonthEndDate = SHORT_MDY_FORMATTER.parse(uiHolder
							.getSelectedReportMonthEndDate());
				} catch (ParseException e) {
					throw new SystemException(e,
							"Invalid Plan Reivew Report Month-End Date: "
									+ uiHolder.getSelectedReportMonthEndDate());
				}

				// TIBCO GET DOC LIST call
				PlanReviewReportDocumentPackage planReviewReportDocPackage = getPlanReviewDocumentList(
						userProfileId,
						getDocListActivity,
						String.valueOf(uiHolder.getPublishDocumentActivityId()),
						reportMonthEndDate);

				ActivityVo printActivity = null;
				
				for(ActivityVo activity : printPlanReviewRequest.getActivityVoList()) {
					
					if(StringUtils.equals(
									String.valueOf(getDocListActivity.getContractId()),
									String.valueOf(activity.getContractId()))) {
						
						
						printActivity = activity;
					}
				}
				
				if(printActivity == null){
					logger.error("Invalid print request. getPlanReviewDocListRequest =[" +
							getPlanReviewDocListRequest+ "],   printPlanReviewRequest=[" +
							printPlanReviewRequest + "]");
					continue;
				}
				
				if (planReviewReportDocPackage != null) {
					
					boolean isHavingPrintDocument = false;
					String dstDocumentID = StringUtils.EMPTY;
					for (PlanReviewReportDocumentVo document : planReviewReportDocPackage.getPlanReviewReportDocumentVoList()) {

						if (StringUtils.equalsIgnoreCase(
								PlanReviewDocumentType.PLAN_REVIEW_HIGH_RESOLUTION
										.getDocumentTypeCode(), document
										.getDocumentType())) {
							isHavingPrintDocument = true;
							dstDocumentID = document.getDocumentId();
							break;
						}
					}
					
					if(isHavingPrintDocument) {
						
						printActivity.setDstoDocumentPackage(planReviewReportDocPackage);
						
						// record the activity event to OK
						PlanReviewServiceDelegate.getInstance(
								new BaseEnvironment().getApplicationId())
								.recordPlanReveiwActivityEvent(
										printActivity.getActivityId(),
										new ActivityEventVo(EventSourceCode.RPS,
												ActivityEventCode.OPEN_PRINT,
												ActivityEventStatus.OK,
												"OK"));
						
						// Trigger webservice TIBCO PRINT DOC for Contract Review Report PDF
						callPrintPlanReviewDocument(printActivity, userProfileId, dstDocumentID);
						
					} else {
						
						// record the activity event to fail
						PlanReviewServiceDelegate.getInstance(
								new BaseEnvironment().getApplicationId())
								.recordPlanReveiwActivityEvent(
										printActivity.getActivityId(),
										new ActivityEventVo(EventSourceCode.RPS,
												ActivityEventCode.OPEN_PRINT,
												ActivityEventStatus.FAILED,
												"Failed, since no print doc found in GetDocList response."));
					}
				} else {
					
					// record the activity event to fail
					PlanReviewServiceDelegate.getInstance(
							new BaseEnvironment().getApplicationId())
							.recordPlanReveiwActivityEvent(
									printActivity.getActivityId(),
									new ActivityEventVo(EventSourceCode.RPS,
											ActivityEventCode.OPEN_PRINT,
											ActivityEventStatus.FAILED,
											"Failed, since no documents found in GetDocList response."));
					
				}
			}

		}
			
		if(printPlanReviewRequest !=null){
			
			for (ActivityVo activity : printPlanReviewRequest.getActivityVoList()) {
				PlanReviewReportUIHolder uiHolder = getPlanReviewReportUIHolder(
						String.valueOf(activity.getContractId()),
						selectedReportsForPrint);

				PrintDocumentPackgeVo printDocumentPackgeVo = (PrintDocumentPackgeVo) activity
						.getDocumentPackageVo();

				uiHolder.setPrintConfirmNumber(printDocumentPackgeVo
						.getPackageTrackingNo());

			}
		}
		
		printForm.setPlanPrintRequestedVOList(selectedReportsForPrint);

		
	}
	

	/**
	 * This method is used to store the Report date from Step2 when user clicks
	 * on GenerateReport.
	 * 
	 * @param planReviewReportVOList
	 * @param request
	 * @throws SystemException
	 */
	private PlanReviewRequestVO populateRequestDetails(
			List<PlanReviewReportUIHolder> planReviewReportVOList,
			HttpServletRequest request, RequestTypeCode requestTypeCode)
			throws SystemException {

		PlanReviewRequestVO planReviewRequest = PlanReviewReportUtils
				.populateRequestDetails(request, requestTypeCode);

		if (RequestTypeCode.PRINT_DOC.equals(requestTypeCode)) {
			
			populateActivityDetails(planReviewReportVOList,
					planReviewRequest, ActivityTypeCode.PRINT_DOC);
		} else {
			
			populateActivityDetails(planReviewReportVOList,
					planReviewRequest, ActivityTypeCode.GET_DOC_LIST);
		}
		
		return planReviewRequest;
	}

	/**
	 * This method is used to populate data.
	 * 
	 * @param planReviewReportVOList
	 * @param requestDataRecord
	 * @throws SystemException
	 */
	private void populateActivityDetails(
			List<PlanReviewReportUIHolder> planReviewReportVOList,
			PlanReviewRequestVO planReviewRequestVO ,ActivityTypeCode activityTypeCode) throws SystemException {

		List<ActivityVo> activityList = new ArrayList<ActivityVo>();
		for (PlanReviewReportUIHolder uiHolder : planReviewReportVOList) {
			ActivityVo activity = new ActivityVo();
			activity.setContractId(uiHolder.getContractNumber());
			activity.setTypeCode(activityTypeCode);
			
			if (ActivityTypeCode.PRINT_DOC.equals(activityTypeCode)) {
				
				activity.setStatusCode(ActivitySatusCode.PENDING);
				activityList.add(populatePrintPackage(uiHolder, activity));
				
			} else {
				
				activity.setStatusCode(ActivitySatusCode.PENDING);
				populateActivityEvent(activity,
						ActivityEventCode.GET_DOC_LIST_START, ActivityEventStatus.OK, "OK");
				activityList.add(activity);
			}
			
		}
		planReviewRequestVO.setActivityVoList(activityList);
	}


	/**
	 * This method is used to populate data.
	 * 
	 * @param planReviewReportVOList
	 * @throws SystemException
	 */
	public ActivityVo populatePrintPackage(
			PlanReviewReportUIHolder planReviewReportUIHolder,
			ActivityVo acitivityVo) throws SystemException {
		PrintDocumentPackgeVo printDetails = new PrintDocumentPackgeVo();

		printDetails.setBrokerId(planReviewReportUIHolder.getShippingVO()
				.getBrokerId());
		printDetails.setPublishDocumentActivityId(planReviewReportUIHolder
				.getPublishDocumentActivityId());
		printDetails.setNoOfCopy(planReviewReportUIHolder.getNumberOfCopies());
		printDetails.setPackageTrackingNo(String
				.valueOf(planReviewReportUIHolder
						.getPublishDocumentActivityId()));
		printDetails.setPublishDocumentActivityId(planReviewReportUIHolder
				.getPublishDocumentActivityId());
		printDetails.setCreatedTs(new Timestamp(System.currentTimeMillis()));
		populateMonthAndYear(planReviewReportUIHolder, printDetails);
		// newShippingVO.setContractId(vo.getContractNumber());
		printDetails.setShippingVO(planReviewReportUIHolder.getShippingVO());
		acitivityVo.setDocumentPackageVo(printDetails);
		return acitivityVo;
	}

	public void populateMonthAndYear(PlanReviewReportUIHolder vo,
			PrintDocumentPackgeVo dbvo) throws SystemException {
		try {
			
			String monthEndDate = vo.getSelectedReportMonthEndDate();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date parse = sdf.parse(monthEndDate);
			Calendar c = Calendar.getInstance();
			c.setTime(parse);
			dbvo.setReportMonth((c.get(Calendar.MONTH) + 1));
			dbvo.setReportYear(c.get(Calendar.YEAR));
		
		} catch (ParseException e) {
			throw new SystemException(e, "Invalid Date: "
					+ vo.getSelectedReportMonthEndDate());
		}
	}
	
	/**
	 * This method will used to store the Shipping data for BOB Pages.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/Print/",params= {"task=submit"},  method =  {RequestMethod.POST}) 
	public String doSubmit(@Valid @ModelAttribute("planReviewPrintForm") PlanReviewPrintForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
			logger.debug("entry -> doSubmit() in PlanReviewReportPrintAction");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doSubmit() in PlanReviewReportPrintAction.");
		}

		return forwards.get(getTask(request));
	}

	/**
	 * This method will used to handle the Sort data for BOB Pages.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/Print/",params= {"task=sort"},  method =  {RequestMethod.POST}) 
	public String doSort(@Valid @ModelAttribute("planReviewPrintForm") PlanReviewPrintForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
			logger.debug("entry -> doSort() in PlanReviewReportPrintAction");
		}

		 forward = doCommon( actionForm, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doSort() in PlanReviewReportPrintAction.");
		}

		return forward;
	}

	/**
	 * This method will used to get State Dropdown Values.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	
	@RequestMapping(value ="/planReview/Print/",params= {"task=getStateDropdownValues"},  method =  {RequestMethod.GET}) 
	public String doGetStateDropdownValues(@Valid @ModelAttribute("planReviewPrintForm") PlanReviewPrintForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	
		String countrycode = request.getParameter("countryCode");
		String selectedState = request.getParameter("selectedState");

		StringBuffer statesBuffer = new StringBuffer();
		StringBuffer sb = new StringBuffer();

		if (StringUtils.equals(Country.USA.getCode(), countrycode)) {

			Map<String, String> usaStates = actionForm
					.getUsaStates();
			sb.append("{\"countryCode\":[ ");

			for (String usaSateCode : usaStates.keySet()) {
				sb.append("{\"stateCode\":\"" + usaSateCode + "\",\"selected\":\"" + 
						(StringUtils.equals(selectedState, usaSateCode)? "selected" : StringUtils.EMPTY)+ "\"},");
			}

			String states = StringUtils.substring(sb.toString(), 0, sb
					.toString().length() - 1);
			statesBuffer.append(states + "] }");
		} else if (StringUtils.equals(Country.CANADA.getCode(), countrycode)) {

			Map<String, String> canadianProvinces = actionForm
					.getCanadianProvinces();
			sb = new StringBuffer();
			sb.append("{\"countryCode\":[ ");

			for (String canadianProvinceCode : canadianProvinces.keySet()) {
				sb.append("{\"stateCode\":\"" + canadianProvinceCode + "\",\"selected\":\"" + 
						(StringUtils.equals(selectedState, canadianProvinceCode)? "selected" : StringUtils.EMPTY)+"\"},");
			}

			String states = StringUtils.substring(sb.toString(), 0, sb
					.toString().length() - 1);

			statesBuffer.append(states + "] }");

		} else {
			return null;
		}

		// Sending the response back to AJAX call
		constructResponse(response, statesBuffer.toString());

		return null;
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
	@RequestMapping(value ="/planReview/Print/",params= {"task=history"},  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doHistory(@Valid @ModelAttribute("planReviewPrintForm") PlanReviewPrintForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
			logger.debug("entry -> doHistory() in BobPlanReviewReportStep1Action");
		}

		setRegularPageNavigation(request);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doHistory() in BobPlanReviewReportStep1Action.");
		}

		return forwards.get( getTask(request));
	}

	/**
	 * This method will used to get Broker Default Address.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planReview/Print/",params= {"action=getBrokerDefaultAddress","task=getBrokerDefaultAddress"},  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doGetBrokerDefaultAddress(@Valid @ModelAttribute("planReviewPrintForm") PlanReviewPrintForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
		
		ShippingVO shiipingAddress = actionForm
				.getDefaultBrokerShippingAddress();

		String responseText = StringUtils.EMPTY;
		if (shiipingAddress != null) {

			
			String country = Country.USA.getCode();
			
			if(StringUtils.equalsIgnoreCase(Country.CANADA.getCode(), shiipingAddress.getRecipientCountry())) {
				country = Country.CANADA.getDescription();
			}
			
			responseText = "{\"recipientName\":\""
					+ shiipingAddress.getRecipientName()
					+ "\",\"recipientPhoneNumber\":\""
					+ shiipingAddress.getRecipientPhoneNumber()
					+ "\",\"recipientEmail\":\""
					+ shiipingAddress.getRecipientEmail()
					+ "\",\"recipientCompany\":\""
					+ shiipingAddress.getRecipientCompanyName()
					+ "\",\"addressLine1\":\""
					+ shiipingAddress.getRecipientAddressLine1()
					+ "\",\"addressLine2\":\""
					+ shiipingAddress.getRecipientAddressLine2()
					+ "\",\"recipientCity\":\""
					+ shiipingAddress.getRecipientCity()
					+ "\",\"recipientZipCode\":\""
					+ shiipingAddress.getRecipientZip() + "\",\"state\":\""
					+ shiipingAddress.getRecipientState() + "\",\"country\":\""
					+ country + "\"}";
		}

		// Sending the response back to AJAX call
		constructResponse(response, responseText);

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

	private void populateTibcoPrintDocumentProperties(ActivityVo activity, String dstDocumentID) {
			
		activity.setDivision(Division.USA);
		activity.setBusinessUnit(BusinessUnit.RPS);
		activity.setProductType(ProductType.ANNUITIES);
		activity.setProductSubType(ProductSubType.VARIABLELIFEINSURANCE);
		activity.setDocumentId(dstDocumentID);
		activity.setDocumentFormat(DocumentFormat.PDF);
		activity.setDocumentType(DocumentType.REGULAR);
		activity.setDocumentStatus(DocumentStatus.ACTIVE);
	}

	
	/*
	 * CR #9
	 * checking each contract row selected on the print page, if any contract rows meet the Allowed Request Limit Condition for print requests, and  
	 */
	private Map<Integer, String> validateUserAllowedPrintPlanReviewRequestLimit(
			List<PlanReviewReportUIHolder> uiHolderList,
			String userProfileId,boolean isUserProfileMimked, Timestamp requestedDate, boolean isThisRequestSubmitted) throws SystemException {
		

		List<ActivityVo> actVoList = getActivityVOListForCRequest(uiHolderList);
		
		Map<Integer, String> contractsWhichAlreadyRequests = null;

		if (actVoList != null && !actVoList.isEmpty()) {
			contractsWhichAlreadyRequests = PlanReviewServiceDelegate
					.getInstance(Environment.getInstance().getApplicationId())
					.validateUserAllowedPrintPlanReviewRequestLimit(actVoList, userProfileId,isUserProfileMimked, requestedDate, isThisRequestSubmitted);
		}
		
		
		return contractsWhichAlreadyRequests;
	}

	private List<ActivityVo> getActivityVOListForCRequest(
			List<PlanReviewReportUIHolder> displayContractReviewReports) throws SystemException {
		//SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		List<ActivityVo> actVoList = new ArrayList<ActivityVo>();
		for (PlanReviewReportUIHolder uIHolder : displayContractReviewReports) {
			if (uIHolder.isPrintContractSelected()) {
				ActivityVo actVo = new ActivityVo();
				PrintDocumentPackgeVo docPkgVo = new PrintDocumentPackgeVo();
				actVo.setContractId(uIHolder.getContractNumber());
				try {
					docPkgVo.setPeriodEndDate(SHORT_MDY_FORMATTER.parse(uIHolder
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
	
	private void callPrintPlanReviewDocument(ActivityVo printActivity,
			String userProfileId, String dstDocumentID) throws SystemException {

		populateTibcoPrintDocumentProperties(printActivity, dstDocumentID);

		try {
			PlanReviewServiceDelegate.getInstance(
					Environment.getInstance().getApplicationId())
					.callPrintPlanReviewDocument(printActivity, userProfileId);
		} catch (Exception exception) {

			logger.error("Exception occured for PlanReview Print request: ["
					+ printActivity.toString() + "] for publishDocActivityId: "
					+ printActivity, exception);

			// record the activity event to fail
			PlanReviewServiceDelegate.getInstance(
					new BaseEnvironment().getApplicationId())
					.recordPlanReveiwActivityEvent(
							printActivity.getActivityId(),
							new ActivityEventVo(EventSourceCode.RPS,
									ActivityEventCode.SEND_PCF_PRINT,
									ActivityEventStatus.FAILED,
									StringUtils.substring(exception.getMessage(), 0, 254)));
		}
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
	 * This method will redirect to the CSRF Error Page.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	  @RequestMapping(value ="/planReview/Print/",params= {"action=csrfError","task=csrfError"},  method =  {RequestMethod.POST,RequestMethod.GET}) 
		public String doCsrfError(@Valid @ModelAttribute("planReviewPrintForm") PlanReviewPrintForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		  String forward=preExecute(actionForm, request, response);
	        if ( StringUtils.isNotBlank(forward)) {
	        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	        }
		return forwards.get("csrfErrorPage");
	}
}
