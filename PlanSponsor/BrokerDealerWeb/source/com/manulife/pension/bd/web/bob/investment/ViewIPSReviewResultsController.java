package com.manulife.pension.bd.web.bob.investment;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.delegate.IPSRServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.delegate.LockServiceDelegate;
import com.manulife.pension.platform.web.investment.AbstractIPSAndReviewDetailsController;
import com.manulife.pension.platform.web.investment.IPSManagerUtility;
import com.manulife.pension.platform.web.investment.valueobject.FundInfo;
import com.manulife.pension.platform.web.investment.valueobject.IPSFundInstructionPresentation;
import com.manulife.pension.platform.web.investment.valueobject.IPSReviewDetailsVO;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.lock.valueobject.Lock;
import com.manulife.pension.service.contract.util.Constants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.ipsr.valueobject.FromFundVO;
import com.manulife.pension.service.ipsr.valueobject.IPSRReviewRequest;
import com.manulife.pension.service.ipsr.valueobject.ToFundVO;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;
import com.thoughtworks.xstream.XStream;

/**
 * This Action class is used to handle the FRW ViewIPSReviewResultspage actions
 * 
 * @author Vellaisamy S
 * 
 */


@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"editIPSReviewResultsForm"})

public class ViewIPSReviewResultsController extends
		AbstractIPSAndReviewDetailsController {
	@ModelAttribute("editIPSReviewResultsForm") 
	public IPSReviewResultsForm populateForm() 
	{
		return new IPSReviewResultsForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/investment/ipsAndReviewDetails.jsp");
		forwards.put("default","/investment/viewIPSReviewResults.jsp");
		forwards.put("homePage","redirect:/do/home/");
		forwards.put("back","redirect:/do/bob/investment/ipsManager/");
		forwards.put("editIPSReviewResults","redirect:/do/bob/investment/editIPSReviewResults/");
	}
	private static final String XSLT_FILE_KEY_NAME = "IPSReviewResult.XSLFile";
	public static final String EDIT_IPS_REVIEW_LOCK_NAME = "editIPSReview";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.controller.BaseAutoAction#
	 * doDefault(org.apache.struts.action.ActionMapping,
	 * com.manulife.pension.platform.web.controller.AutoForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@RequestMapping(value ="/investment/viewIPSReviewResults/", method = {RequestMethod.GET})
	public String doDefault(@Valid @ModelAttribute("editIPSReviewResultsForm") IPSReviewResultsForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		Contract contract = getBobContext(request).getCurrentContract();
		
		String reviewRequestId = request.getParameter("reviewRequestId");
		int contractId = new Integer(contract.getContractNumber());
		
		

		// Mode will viewMode for View page
		String mode = request.getParameter("mode");

		int reviewId = 0;
		
		// Take the request id from request or from the form bean
		if (StringUtils.isNotBlank(reviewRequestId)) {
			reviewId = Integer.parseInt(reviewRequestId);
		} else if (StringUtils.isNotBlank(actionForm
				.getReviewRequestId())) {
			reviewId = Integer.parseInt(actionForm
					.getReviewRequestId());
		}
		
		IPSRServiceDelegate ipsrServiceDelegate = IPSRServiceDelegate
				.getInstance();
		IPSRReviewRequest ipsrReviewRequest = ipsrServiceDelegate
				.getIPSRReviewRequestForRequestId(reviewId);
		
		// Check whether the access is valid. If not redirect to home page.
		boolean isValid = true;
		
		// Check whether the review id is valid. Invalid review id is possible
		// when trying to access the page through book mark
		if (ipsrReviewRequest == null
				|| ipsrReviewRequest.getContractId() != contractId) {
			isValid = false;
		} else if (!IPSManagerUtility.isViewAvailable (ipsrReviewRequest
				.getReviewRequestStatus(), ipsrReviewRequest
				.getReviewRequestSubStatus())) {
			isValid = false;
		}
		
		if (!isValid){
			return forwards.get(FRW_HOME_ACTION);
		}
		
		// Remove the old bean from the session before loading the data
		actionForm.resetData();
		//removeIPSResultsFormFromSession(request);

		actionForm.setReviewRequestId(String.valueOf(reviewId));
		actionForm.setMode(mode);
				
		actionForm.setAnnualReviewDate(DateRender
				.formatByPattern(ipsrReviewRequest.getAnnualReviewDate(), null,
						MEDIUM_MMDD_SLASHED, RenderConstants.MEDIUM_MD));
		
		// Get the IPS Review Requests for Contract
		List<IPSRReviewRequest> ipsrReviewRequests = getIPSReviewRequests(contractId);
		
		if (IPSManagerUtility.isCurrentReview(ipsrReviewRequests, reviewId)) {
			actionForm.setCurrentReview(true);
		}

		actionForm.setAsOfDate(ipsrReviewRequest.getProjectedReviewDate());
		actionForm.setProcessingDateForReportLink(DateRender
				.formatByPattern(ipsrReviewRequest.getProjectedReviewDate(),
						"", MEDIUM_MMMMDDYYYY_COMMA));
		populateIPSReviewFundInstructionToForm(actionForm,
				ipsrReviewRequest, userProfile.getBDPrincipal().getProfileId(),
				ipsrServiceDelegate);
		
		String iatEffectiveDate = DateRender.formatByPattern(ipsrReviewRequest
				.getProcessingDate(), "", RenderConstants.MEDIUM_MDY_SLASHED);

		actionForm.setIpsIatEffectiveDate(iatEffectiveDate);
		
		boolean riaDesignationInd = SecurityServiceDelegate.getInstance().hasRia338DesignationForUser(contractId, BigDecimal.valueOf(userProfile.getBDPrincipal().getProfileId()));
		
		// Enable the edit button in View IPS Review results page for RIA designated user & Internal user mimicking RIA designated user
		if (riaDesignationInd || (riaDesignationInd && userProfile.isInMimic())) {
			if (IPSManagerUtility.isEditAvailable(ipsrReviewRequest
					.getReviewRequestStatus(), ipsrReviewRequest
					.getReviewRequestSubStatus(), contract.getStatus(), actionForm
					.isAllFundInstructionsIgnored(),contractId)) {
				request.setAttribute(EDIT_AVAILABLE, true);
			}
		}
		
		actionForm.setFromViewPage(true);
		
		// If no Fund Instruction is available for the Review then redirect to Home
		if (!IPSManagerUtility
				.isInstructionsAvailableForView(actionForm
						.getIpsReviewFundInstructionList())) {
			return forwards.get(FRW_HOME_ACTION);
		}

		return forwards.get(DEFAULT_ACTION);
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.manulife.pension.platform.web.investment.
	 * AbstractIPSAndReviewDetailsAction
	 * #prepareXMLFromReport(com.manulife.pension
	 * .platform.web.controller.AutoForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	
	protected String prepareXMLFromReport(
			AutoForm actionForm,
			HttpServletRequest request) throws SystemException {
		
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);

		IPSReviewDetailsVO ipsReviewDetails = new IPSReviewDetailsVO();
		
		IPSReviewResultsForm iPSReviewResultsForm = (IPSReviewResultsForm) actionForm;

		// Get the logo file name
		String logoFileName = PdfHelper.class.getResource(
				CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX
						+ CommonConstants.JHRPS_LOGO_FILE).toString();

		LayoutPage layoutPage = getLayoutPage(
				BDPdfConstants.IPS_REVIEW_RESULTS_PATH, request);

		// Get the Page name.
		String pageName = ContentUtility.getContentAttributeText(layoutPage,
				CommonContentConstants.PAGE_NAME, null);

		// Get the footer.
		String footer = ContentUtility.getPageFooter(layoutPage, null);
		footer = ContentUtility.filterCMAContentForPDF(footer);

		// Get the disclaimer.
		String disclaimer = ContentUtility.getPageDisclaimer(layoutPage, null,
				-1);
		disclaimer = ContentUtility.filterCMAContentForPDF(disclaimer);

		// Get the IntroText1 from Content DB.
		String introText1 = ContentUtility.getContentAttributeText(layoutPage,
				CommonContentConstants.INTRO1_TEXT, null);

		// Get the IntroText2 from Content DB.
		String introText2 = ContentUtility.getContentAttributeText(layoutPage,
				CommonContentConstants.INTRO2_TEXT, null);

		// Get the As of date label.
		String asOfDateLabel = ContentUtility.getContentAttributeText(
				layoutPage, CommonContentConstants.SUB_HEADER, null);

		

		// Get the global disclosure text.
		String globalDisclosureText = ContentHelper.getContentText(
				BDContentConstants.BD_GLOBAL_DISCLOSURE,
				ContentTypeManager.instance().MESSAGE, null);
		globalDisclosureText = ContentUtility.filterCMAContentForPDF(globalDisclosureText);

		// Get the effective date details text.
		String effectiveDateText = ContentHelper.getContentText(
				BDContentConstants.IPS_IAT_EFFECTIVE_DATE_DETAILS,
				ContentTypeManager.instance().MESSAGE, null);

		// Get the standard footer text.
		String standardFooter = ContentUtility.getContentAttributeText(
				layoutPage, CommonContentConstants.TEXT, null);
		standardFooter = ContentUtility.filterCMAContentForPDF(standardFooter);
		
		String fundInfoIconPath = PdfHelper.class.getResource(
				CommonConstants.UNMANAGED_IMAGE_FILE_PREFIX
						+ CommonConstants.INFO_ICON_FILE).toString();

		Contract contract = getBobContext(request).getCurrentContract();
		String contractName = contract.getCompanyName();
		int contractId = contract.getContractNumber();
		ipsReviewDetails.setContractName(contractName);
		ipsReviewDetails.setContractNumber(contractId);
		// Set all the information in the VO which used to create XML.
		ipsReviewDetails.setPageName(pageName);
		ipsReviewDetails.setJhLogoPath(logoFileName);
		ipsReviewDetails.setIntro1Text(introText1);
		ipsReviewDetails.setIntro1Text(introText2);
		ipsReviewDetails.setFooter(footer);
		ipsReviewDetails.setDisclaimer(disclaimer);
		ipsReviewDetails.setGlobalDisclosure(globalDisclosureText);
		ipsReviewDetails.setIatEffectiveDateText(effectiveDateText);
		ipsReviewDetails.setStandardFooter(standardFooter);
		ipsReviewDetails.setFundInfoIconPath(fundInfoIconPath);
		ipsReviewDetails.setResultsPageSubHeader(asOfDateLabel);

		if (iPSReviewResultsForm != null) {

			String reviewRequestId = iPSReviewResultsForm
					.getReviewRequestId();
			if (StringUtils.isNotEmpty(reviewRequestId)) {
				int reviewId = Integer.parseInt(reviewRequestId);

				IPSRServiceDelegate ipsrServiceDelegate = IPSRServiceDelegate
						.getInstance();
				IPSRReviewRequest ipsrReviewRequest = ipsrServiceDelegate
						.getIPSRReviewRequestForRequestId(reviewId);

				iPSReviewResultsForm.setAsOfDate(ipsrReviewRequest
						.getProjectedReviewDate());
				populateIPSReviewFundInstructionToForm(iPSReviewResultsForm,
						ipsrReviewRequest, userProfile.getBDPrincipal()
								.getProfileId(), ipsrServiceDelegate);

			}

			String asOfDate = DateRender.formatByPattern(
					iPSReviewResultsForm.getAsOfDate(), "",
					LONG_MMMMDDYYYY_COMMA);
			
			List<IPSFundInstructionPresentation> fundInstructions = iPSReviewResultsForm
					.getIpsReviewFundInstructionList();
			
			Date processDate = null;
			try {
				if (StringUtils.isNotBlank(iPSReviewResultsForm.getIpsIatEffectiveDate())) {
					processDate = IPSManagerUtility
							.getIATEffectiveDateInDateFormat(iPSReviewResultsForm
									.getIpsIatEffectiveDate());
				}
			} catch (ParseException e) {
				throw new SystemException(e, "Exception while parsing date");
			}

			String iatEffectiveDate = processDate != null ? DateRender.formatByPattern(
							processDate, "",
							MEDIUM_MMMMDDYYYY_COMMA) : "Not selected";
							
			ipsReviewDetails.setIatProcessingDate(iatEffectiveDate);
			ipsReviewDetails.setAsOfDate(asOfDate);
			List<FundInfo> fundInfoList = buildFundInfo(fundInstructions);
			ipsReviewDetails.setFundInstructions(fundInfoList);
		}

		XStream stream = new XStream();
		stream.alias("ips_review_results", IPSReviewDetailsVO.class);
		stream.alias("current_fund_info", FromFundVO.class);
		stream.alias("top_fund_info", ToFundVO.class);
		stream.alias("fund_instruction", FundInfo.class);
		String strXml = stream.toXML(ipsReviewDetails);
		strXml = strXml.replaceAll("&lt;strong&gt;", "&lt;b&gt;");
		strXml = strXml.replaceAll("&lt;/strong&gt;", "&lt;/b&gt;");
		strXml = strXml.replaceAll("&lt;sup&gt;","&lt;SUP&gt;");
		strXml = strXml.replaceAll("&lt;/sup&gt;","&lt;/SUP&gt;");
		return strXml;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.investment.
	 * AbstractIPSAndReviewDetailsAction#getXSLTFileName()
	 */
	@Override
	protected String getXSLTFileName() {
		return XSLT_FILE_KEY_NAME;
	}

	/**
	 * This method gets layout page for the given layout id.
	 * 
	 * @param id
	 * @param request
	 * @return layoutPageBean
	 */
	private LayoutPage getLayoutPage(String id, HttpServletRequest request) {
		BDLayoutBean bean = ApplicationHelper.getLayoutStore(
				request.getServletContext()).getLayoutBean(id, request);
		LayoutPage layoutPageBean = (LayoutPage) bean.getLayoutPageBean();
		return layoutPageBean;
	}

	/**
	 * This Method used to populate the fund details which should be printed in
	 * PDF.
	 * 
	 * @param fundInstructions
	 * @return fundInfoList
	 */
	private List<FundInfo> buildFundInfo(List<IPSFundInstructionPresentation> fundInstructions) {

		List<FundInfo> fundInfoList = new ArrayList<FundInfo>();
		FundInfo fundInfo = null;
		if (fundInstructions != null && fundInstructions.size() > 0) {

			for (IPSFundInstructionPresentation fundInstruction : fundInstructions) {

				fundInfo = new FundInfo();
				fundInfo.setAssetClassName(fundInstruction.getAssetClassName());
				List<FromFundVO> currentFundList = fundInstruction.getFromFundVO();
				
				if(currentFundList != null && currentFundList.size() > 0) {
					List<FromFundVO> currentFunds = new ArrayList<FromFundVO>();
					for(FromFundVO currentFund : currentFundList) {
						if (currentFund != null) {
							String currentFundName = currentFund.getFundName() != null ? currentFund
									.getFundName().trim() : StringUtils.EMPTY;
							currentFund.setFundName(currentFundName);
							currentFunds.add(currentFund);
						}
					}
					fundInfo.setCurrentFundInfo(currentFunds);
				}
				List<ToFundVO> toFundList = fundInstruction.getToFundVO();
				if(toFundList != null && toFundList.size() > 0) {
					List<ToFundVO> topFundList = new ArrayList<ToFundVO>();
					List<String> actionInds = new ArrayList<String>();
					for(ToFundVO toFund : toFundList) {
						if (toFund != null) {
							String topFundName = toFund.getFundName() != null ? toFund
									.getFundName().trim() : StringUtils.EMPTY;
							toFund.setFundName(topFundName);
							topFundList.add(toFund);
							
							String action = toFund.getActionIndicator();
							if (Constants.ACTION_APPROVED
									.equalsIgnoreCase(action)) {
								String approved = ContentHelper.getContentText(
										BDContentConstants.IPS_FUND_ACTION_APPROVE,
										ContentTypeManager.instance().MESSAGE, null);
								actionInds.add(approved);
							} else if (Constants.ACTION_IGNORED
									.equalsIgnoreCase(action)
									|| Constants.SYSTEM_IGNORED
											.equalsIgnoreCase(action)) {
								String ignored = ContentHelper
										.getContentText(
												BDContentConstants.IPS_FUND_ACTION_IGNORE,
												ContentTypeManager.instance().MESSAGE,
												null);
								actionInds.add(ignored);
							} else {
								String noAction = ContentHelper.getContentText(
										BDContentConstants.IPS_FUND_ACTION_NOT_SELECTED,
										ContentTypeManager.instance().MESSAGE, null);
								actionInds.add(noAction);
							}

						}
					}
					fundInfo.setTopFundInfo(topFundList);
					fundInfo.setActionIndicators(actionInds);
				}
				fundInfoList.add(fundInfo);
			}
			
		}
		return fundInfoList;
	}
	
	/**
	 * This method is used to generate Annual Review Report
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws SystemException
	 * @throws IOException 
	 */
	
	@RequestMapping(value ="/investment/viewIPSReviewResults/", params= {"action=generateReviewReport"}, method = {RequestMethod.GET})
	public String doGenerateReviewReport(@Valid @ModelAttribute("editIPSReviewResultsForm") IPSReviewResultsForm actionForm,ModelMap model,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	
	
		
		if (logger.isDebugEnabled()) {
			logger.debug("Inside doGenerateReviewReport");
		}
		
		Contract contract = getBobContext(request).getCurrentContract();
		
		Collection<GenericException> errors = new ArrayList<GenericException>();
		
		populateReviewReport(request, contract, response, errors);
		
		if (!errors.isEmpty()) {
			setErrorsInRequest(request, errors);
			String forwrad=doDefault(actionForm, bindingResult, request, response);
			return forwards.get(forwrad)!=null?forwards.get(forwrad):forwards.get("default");
			//return doDefault( actionForm,model, request, response);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Exiting doGenerateReviewReport");
		}
		return null;
	}
	
	/**
	 * Redirects Edit IPS Review Results Action, Locks Edit IPS for Trustee's
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
	@RequestMapping(value ="/investment/viewIPSReviewResults/", params= {"action=edit"}, method = {RequestMethod.POST})
	public String doEdit(@Valid @ModelAttribute("editIPSReviewResultsForm") IPSReviewResultsForm actionForm,ModelMap model,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	
	
	
		String forward = null;
		Collection<GenericException> errors = new ArrayList<GenericException>();
		
		BobContext bob = BDSessionHelper.getBobContext(request) ;
		int contractId = bob.getCurrentContract().getContractNumber();
		String contractStatus = bob.getCurrentContract().getStatus();
		
		BDUserProfile bdUserProfile = bob.getUserProfile();

		boolean riaDesignationInd = SecurityServiceDelegate.getInstance().hasRia338DesignationForUser(contractId, BigDecimal.valueOf(bdUserProfile.getBDPrincipal().getProfileId()));
		
		// Validation when multiple RIA designated user or Trustee is editing same Contract 
		if (riaDesignationInd && !bdUserProfile.isInMimic()) {
			if (LockServiceDelegate.getInstance().lock(EDIT_IPS_REVIEW_LOCK_NAME, EDIT_IPS_REVIEW_LOCK_NAME + contractId,
					bdUserProfile.getAbstractPrincipal().getProfileId())) {
				forward = forwards.get(EDIT_REVIEW_ACTION);
			} else {
					Lock lockInfo = LockServiceDelegate.getInstance()
							.getLockInfo(EDIT_IPS_REVIEW_LOCK_NAME,EDIT_IPS_REVIEW_LOCK_NAME + contractId);
					
					List<String> userInfoBDW = SecurityServiceDelegate.getInstance().getUserInfoForLock(BigDecimal.valueOf(lockInfo.getLockUserProfileId()));
					
					String lockOwnerDisplayName = StringUtils.EMPTY;
					
					if(userInfoBDW != null && !userInfoBDW.isEmpty()){
						lockOwnerDisplayName = userInfoBDW.get(0) + " " + userInfoBDW.get(1); 
					}

					errors.add(new ValidationError(StringUtils.EMPTY,
							BDErrorCodes.IPSM_TRUSTEE_LOCK,
							new String[] { lockOwnerDisplayName }));

					setErrorsInRequest(request, errors);

					return doDefault( actionForm,model,request, response);
			}
		} else {
			IPSReviewResultsForm iPSReviewResultsForm = (IPSReviewResultsForm) actionForm;
			iPSReviewResultsForm.setFromViewPage(true);
			iPSReviewResultsForm.setFromApprovePage(false);
			forward = forwards.get(EDIT_REVIEW_ACTION);
		}
		
		return forward;
	}
	
	/**
	 * Releases the IPSR Edit Lock for Trustee and Redirects to IPS Landing page
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
	@RequestMapping(value ="/investment/viewIPSReviewResults/", params= {"action=back"}, method = {RequestMethod.POST})
	public String doBack(@Valid @ModelAttribute("editIPSReviewResultsForm") IPSReviewResultsForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

		return forwards.get(BACK_ACTION);
	}
	@RequestMapping(value ="/investment/viewIPSReviewResults/",params= {"action=printPDF"}, method = {RequestMethod.GET})
	public String doPrintPDF(@Valid @ModelAttribute("editIPSReviewResultsForm") IPSReviewResultsForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		String forward=super.doPrintPDF( form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	/**
	 * Returns the BOB Context associated with the given request.
	 * 
	 * @param request
	 *            The request object.
	 * @return The BOBContext object associated with the request (or null if
	 *         none is found).
	 */
	private static BobContext getBobContext(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return session == null ? null : (BobContext) session
				.getAttribute(BDConstants.BOBCONTEXT_KEY);
	}
	
	/**
	 * This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}
	
	/**
	 * avoids token generation as this class acts as intermediate for many
	 * transactions.
	 * 
	 * (non-Javadoc)
	 * @see com.manulife.pension.platform.web.controller.BaseAction#isTokenRequired(java.lang.String)
	 */
	/*@Override
	protected boolean isTokenRequired(String action) {
		return true;
	}*/

	/**
	 * Returns true if token has to be validated for the particular action call
	 * to avoid CSRF vulnerability else false. (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.controller.BaseController#isTokenValidatorEnabled(java.lang.String)
	 */
	@Override
	protected boolean isTokenValidatorEnabled(String action) {
		// avoids methods from validation which ever is not required
		return StringUtils.isNotEmpty(action)
				&&  ( StringUtils.equalsIgnoreCase(action, "Edit")
						|| StringUtils.equalsIgnoreCase(action, "Back"))?true:false;
	} 
	
}
