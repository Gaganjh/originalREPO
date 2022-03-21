package com.manulife.pension.bd.web.bob.investment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.fop.apps.FOPException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.blockOfBusiness.BlockOfBusinessForm;
import com.manulife.pension.bd.web.bob.investment.util.Constants.Location;
import com.manulife.pension.bd.web.bob.investment.util.FeeCmaDataImpl;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.report.BOBReportController;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Footnote;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.view.MutableFootnote;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PDFGenerator;
import com.manulife.pension.platform.web.util.ReportsXSLProperties;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.report.investment.valueobject.InvestmentCostReportData;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractDetailsOtherVO;
import com.manulife.pension.service.fee.util.estimatedcosts.EstimatedRecordKeepingCostComponentGenerator;
import com.manulife.pension.service.fee.util.estimatedcosts.FeeDataContextInterface.DollarBasedFeeDetail;
import com.manulife.pension.service.fee.util.estimatedcosts.FeeDataContextInterface.EstimatedJhRecordKeepingCostSummary;
import com.manulife.pension.service.fee.util.estimatedcosts.FeeDataContextInterface.FeeCmaData;
import com.manulife.pension.service.fee.util.estimatedcosts.FeeDataContextInterface.FeeDetail;
import com.manulife.pension.service.fee.util.estimatedcosts.FeeDataContextInterface.FootNote;
import com.manulife.pension.service.fee.valueobject.ContractClass;
import com.manulife.pension.service.fee.valueobject.FundFeeVO;
import com.manulife.pension.service.fee.valueobject.InvestmentGroup;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.Access404a5;
import com.manulife.pension.service.fund.valueobject.Access404a5.Facility;
import com.manulife.pension.service.fund.valueobject.Access404a5.MissingInformation;
import com.manulife.pension.service.fund.valueobject.Access404a5.Qualification;
import com.manulife.pension.service.fund.valueobject.UserAccess;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.Footnotes;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.pension.service.contract.valueobject.ContractDetail;
import com.manulife.pension.util.ArrayUtility;


/**
 * action class for investment related costs page
 * 
 * @author  nallaba 
 *
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"investmentSelectionReportForm"})

public class FeeDisclosureInvestmentReportController extends BOBReportController {
	@ModelAttribute("investmentSelectionReportForm") 
	public InvestmentSelectionReportForm populateForm() 
	{
		return new InvestmentSelectionReportForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/contract/regulatoryDisclosures.jsp");
		forwards.put("default","/contract/regulatoryDisclosuresList.jsp");
		forwards.put("print","/contract/regulatoryDisclosures.jsp");
		forwards.put("filter","/contract/regulatoryDisclosures.jsp");
		forwards.put("regulatoryDisclosure","/contract/regulatoryDisclosures.jsp");
		forwards.put("bobPage","redirect:/do/bob/");
		}   

	
	private static final String FEE_INVESTMENT_PAGE_DATE_LIMIT = "FEE_INVESTMENT_PAGE_DATE_LIMIT";
	private static final String XSLT_FILE_KEY_NAME = "regulatoryDisclosures.XSLFile";
	private static final String UTF8 = "utf-8";
	private static final String DEFAULT_VALUE = "0";
	private static final String PATTERN = "0.00";
	private static final int SCALE = 2;
	private static final int ROUNDING_MODE = 4;
	private static final int DIGITS = 1;
	private static final boolean SIGN = true;
	
	public FeeDisclosureInvestmentReportController(){
		super(FeeDisclosureInvestmentReportController.class);
	}
	
	/**
	 * This is an overridden method. This method handles the book marking
	 * scenarios. This will check if contract is not in pilot it will redirect to HOME page
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            Form
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws ServletException
	 *             , IOException, SystemException8
	 * @return ActionForward
	 * 
	 */
	@Override
	protected String preExecute( ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> preExecute");
		}
		return super.preExecute( form, request, response);
	}
	
	/**
	 * This method sets the report criteria and gets the report data required for investment 
	 * related cost page.
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            Form
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws SystemException
	 * @return ActionForward
	 * 
	 */
	 

	public String doCommon(BaseReportForm form, 
				HttpServletRequest request,	HttpServletResponse response) throws SystemException {
		
		InvestmentSelectionReportForm actionForm = (InvestmentSelectionReportForm) form;
		String forward = forwards.get("default");
		
		Date invesmentFeePageDateLowerLimit = getInvestmentPageLowerLimitDate();
		BobContext bob = BDSessionHelper.getBobContext(request) ;
		
		Contract contract = bob.getCurrentContract();
		
		ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
		//checking Merrill contract
		ContractDetailsOtherVO contractDetailsOtherVO =  contractServiceDelegate.getContractDetailsOther(contract.getContractNumber());
		
		if(contractDetailsOtherVO!=null && contractDetailsOtherVO.isMerrillLynch()) {
			actionForm.setMerrillLynchContract(contractDetailsOtherVO.isMerrillLynch());
		}
		

		Map<String, String> mtaMap = contractServiceDelegate.getMtaContractDetails(contract.getContractNumber());
		Boolean isGovernmentPlan = null;
		try {
			isGovernmentPlan = contractServiceDelegate.isGovernmentPlan(contract.getContractNumber());
		} catch (ContractDoesNotExistException e1) {
			// not a contract
		}
		 //404 section
	      if((contract.getStatus().equals(BDConstants.ACTIVE)||(contract.getStatus().equals(BDConstants.FROZEN)))
	    		  && !contract.isDefinedBenefitContract()
	    		  && (mtaMap != null && (!StringUtils.equals(mtaMap.get(BDConstants.DISTRIBUTION_CHANNEL), BDConstants.MTA) 
	    			  && !StringUtils.equals(mtaMap.get(BDConstants.GROUP_FIELD_OFFICE_NO), BDConstants.GFO_CODE_25270)
	    			  && !StringUtils.equals(mtaMap.get(BDConstants.GROUP_FIELD_OFFICE_NO), BDConstants.GFO_CODE_25280)))
	    		  && (isGovernmentPlan != null && !isGovernmentPlan)){
	    	  actionForm.setDisplay404Section(true);
			}else{
				actionForm.setDisplay404Section(false);
			}
	       //408b2 section 
	       if(FeeDisclosureUtility.isFeeDisclsoureAvaiable(contract)){
				  actionForm.setShow408b2Section(true);
			  }else{
				  actionForm.setShow408b2Section(false); 
			  }
		
	      
	    if(actionForm.isDisplay404Section()){
	    	  UserAccess access = UserAccess.FRW;
	    	  BDUserProfile userProfile = getUserProfile(request);
		
		try {
		    
    		Access404a5 contractAcc =
    		        FundServiceDelegate
    		        .getInstance()
    		        .get404a5Permissions(
    		                EnumSet.of(
    		                        Facility._404A5_PLAN_AND_INVESTMENT_NOTICE,
    		                        Facility.INVESTMENT_COMPARATIVE_CHART,
    		                        Facility.IMPORTANT_PLAN_INFORMATION_ADDENDUM_TEMPLATE,
    		                        Facility.PARTICIPANT_FUND_CHANGE_NOTICE_TEMPLATE),
    		                        contract.getContractNumber(),
    		                access);
    		
    		Qualification piNoticeQual = contractAcc.getAccess(Facility._404A5_PLAN_AND_INVESTMENT_NOTICE);
    		Qualification iccQual = contractAcc.getAccess(Facility.INVESTMENT_COMPARATIVE_CHART);
    		
    		actionForm.setDisplay404Section(! contractAcc.getAccessibleFacilities().isEmpty());
    		actionForm.setShowPlanAndInvestmentNotice(
    	            piNoticeQual != null
    	            && ! piNoticeQual.getTemporarilyMissingInformation().contains(MissingInformation.ICC_CONTACT));
    		actionForm.setShowIcc(
                    iccQual != null
                    && ! iccQual.getTemporarilyMissingInformation().contains(MissingInformation.ICC_CONTACT));
    		actionForm.setShowPreviousYearEndIccUnavailableMessage(
                    piNoticeQual != null
                    && piNoticeQual.getTemporarilyMissingInformation().contains(MissingInformation.PREVIOUS_YEAR_END_FUND_DATA)
                    || iccQual != null
                    && iccQual.getTemporarilyMissingInformation().contains(MissingInformation.PREVIOUS_YEAR_END_FUND_DATA));
    		actionForm.setShowMissingIccContactMessage(
                    piNoticeQual != null
                    && piNoticeQual.getTemporarilyMissingInformation().contains(MissingInformation.ICC_CONTACT)
                    || iccQual != null
                    && iccQual.getTemporarilyMissingInformation().contains(MissingInformation.ICC_CONTACT));
    		actionForm.setShowIpiAddendum(contractAcc.getAccess(Facility.IMPORTANT_PLAN_INFORMATION_ADDENDUM_TEMPLATE) != null);
    		actionForm.setShowParticipantFundChangeNotice(contractAcc.getAccess(Facility.PARTICIPANT_FUND_CHANGE_NOTICE_TEMPLATE) != null);
    	    
		} catch (SystemException se) {
		    
	          LogUtility.logSystemException(
	                    Constants.PS_APPLICATION_ID,
	                    new SystemException(
	                            se,
	                            "Profile ID " + userProfile.getBDPrincipal().getProfileId() +
	                            " contract ID " + contract.getContractNumber()));
	          actionForm.setDisplay404Section(false);
	          actionForm.setShowPlanAndInvestmentNotice(false);
	          actionForm.setShowIcc(false);
	          actionForm.setShowPreviousYearEndIccUnavailableMessage(false);
	          actionForm.setShowMissingIccContactMessage(false);
	          actionForm.setShowIpiAddendum(false);
	          actionForm.setShowParticipantFundChangeNotice(false);

			}

		}
	      
		if(actionForm.isShow408b2Section()){
			if(FeeDisclosureUtility.checkIfInvestmentCostPageAvaiable(contract.getContractNumber())){
				actionForm.setInvestmentRelatedCostsPageAvailable(true);
			//default selectedView is set to selected funds
			if(request.getParameter(BDConstants.SELECTED_VIEW)!=null){
				if(StringUtils.equals(BDConstants.AVAILABLE, request.getParameter(BDConstants.SELECTED_VIEW))) {
					actionForm.setSelectedView(BDConstants.AVAILABLE);
				} else {
					actionForm.setSelectedView(BDConstants.SELECTED);
				}
			}else{
				if (actionForm.getSelectedView() == null) {

					actionForm.setSelectedView(BDConstants.SELECTED);
				}
			}
			
		List<Date> reportDates = FeeServiceDelegate.getInstance(Environment.getInstance().getAppId())
				.getDatesForInvestmentCostPage(contract.getContractNumber(), invesmentFeePageDateLowerLimit);
		
		
		request.setAttribute(BDConstants.REPORT_DATES_FEE_DISCLOSURE, reportDates);
		actionForm.setAvailableReportDates(reportDates);
	
		//investmentSelectionReportForm.setSelectedAsOfDate(investmentSelectionReportForm.getParameter(BDConstants.SELECTED_AS_OF_DATE));
		if(reportDates.size() > 0) {
			if(actionForm.getSelectedAsOfDate() == null) {
				// set the 1st date from the list in this case
				actionForm.setSelectedAsOfDate(String.valueOf(reportDates.get(0).getTime()));
			}
			if(StringUtils.equals(actionForm.getSelectedAsOfDate(), 
					String.valueOf(reportDates.get(0).getTime()))){
				request.setAttribute(BDConstants.SHOW_LINK, true);
			}
			actionForm.setDisplaySelectedAsOfDate(
					new Date(Long.valueOf(actionForm.getSelectedAsOfDate()).longValue()));
		}
		// Search parameter are set in session for print report
		HttpSession session = request.getSession(false);
		if(StringUtils.equals(PRINT_TASK, request.getParameter(TASK_KEY))){
			actionForm.setSelectedView(
					(String)session.getAttribute(BDConstants.SELECTED_VIEW));
			actionForm.setSelectedAsOfDate(
					(String)session.getAttribute(BDConstants.SELECTED_AS_OF_DATE));
			session.setAttribute(BDConstants.SELECTED_AS_OF_DATE, actionForm.getSelectedAsOfDate());
		} else {
			session.setAttribute(BDConstants.SELECTED_VIEW, actionForm.getSelectedView());
			session.setAttribute(BDConstants.SELECTED_AS_OF_DATE, actionForm.getSelectedAsOfDate());
		}
		
		
			Date selectedAsOfDate = new Date(Long.valueOf(actionForm.getSelectedAsOfDate()));
	        String svfFundId = contractServiceDelegate.getStableValueFundId(contract.getContractNumber(), selectedAsOfDate);
	        if (svfFundId != null && !("").equals(svfFundId.trim())) {
	        	actionForm.setSvfIndicator(true);
	        	actionForm.setStableValueFundId(svfFundId);
			}
		
	        actionForm.setrPandR1Indicator(FeeDisclosureUtility.checkR1andRPFundAvaliableForSelectedContact(contract.getContractNumber(), selectedAsOfDate));
	        
	        actionForm.setlTIndicator(FeeDisclosureUtility.checkLTFundAvaliableForSelectedContact(contract.getContractNumber(), selectedAsOfDate));
	        
		forward = super.doCommon( actionForm, request, response);
		
		InvestmentCostReportData reportData = (InvestmentCostReportData) request.getAttribute(BDConstants.REPORT_BEAN);
	   
		// if there is footnotes , set it to request scope
		if(reportData.getFootNotes().size() > 0) {
			request.setAttribute(BDConstants.WEB_SYMBOLS_FOOTNOTES, reportData.getFootNotes().toArray(new String[1]));
	    }
		
		if(reportDates.size() > 0) {
			if(StringUtils.equals(actionForm.getSelectedAsOfDate(), String.valueOf(reportDates.get(0).getTime()))){
				actionForm.setShowAvailableOptions(true);
			} else {
				actionForm.setShowAvailableOptions(false);
			}
		} else {
			actionForm.setShowAvailableOptions(false);
		}
		
		FeeCmaDataImpl feeCmaData = new FeeCmaDataImpl(GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY.equalsIgnoreCase(contract.getCompanyCode()) ? Location.NY : Location.US);
		
		// load Total A, Total B Section Fees & FootNotes
		EstimatedJhRecordKeepingCostSummary recordKeepingCostsummary = getSummaryOfRecordKeepingFees(actionForm, reportData, feeCmaData);
		
		// Add N19, N20 & N21 footnotes.
		Map<FootNote, String> reportFootnotes = recordKeepingCostsummary.getReportFootnotes();
		boolean isSVFFund = false;
		List<String> svfFundIdList = new ArrayList<String>();
		boolean addN20FootNote = false;
		boolean addN21FootNote = false;
		Map<InvestmentGroup,List<FundFeeVO>> funds = reportData.getInvestmentData();
		for(Map.Entry<InvestmentGroup,List<FundFeeVO>> entry : funds.entrySet()){
			for (FundFeeVO fundFeeVo : entry.getValue()) {
				addN20FootNote = true;
				if (!reportData.getContractDetails().isDefinedBenefitContract()
						&& StringUtils.equals(BDConstants.FUND_TYPE_GARUNTEED,
								fundFeeVo.getFundType())) {
					addN21FootNote = true;
				}
				if (StringUtils.equals(fundFeeVo.getFundFamilyCatCode(),
						BDConstants.FUND_FAMILY_CATEGORIZATION_SVF)){
					isSVFFund = true;
					svfFundIdList.add(fundFeeVo.getFundId());
				} 
			}
		}
		
		if (isSVFFund) {
			for(String svfFund : svfFundIdList) {
				if (svfFund.equalsIgnoreCase(BDConstants.FUND_CODE_MSV)) {
					reportFootnotes.put(FootNote.FOOTNOTE_N19, feeCmaData.getImpSecN19MSVFootnote());
				} else if (svfFund.equalsIgnoreCase(BDConstants.FUND_CODE_NMY)) {
					reportFootnotes.put(FootNote.FOOTNOTE_N19, feeCmaData.getImpSecN19NMYFootnote());
				}
			}
		}
		
		if (addN20FootNote) {
			reportFootnotes.put(FootNote.FOOTNOTE_N20,feeCmaData.getImpSecN20Footnote());
		}

		if (addN21FootNote) {
			reportFootnotes.put(FootNote.FOOTNOTE_N21,feeCmaData.getImpSecN21Footnote());
		}
		
		request.setAttribute(BDConstants.ESTIMATED_JH_RECORDKEEPING_COST_SUMMARY, recordKeepingCostsummary);
		}
		}
		forward = super.doCommon( actionForm, request, response);
		//  Class zero indicator to display dynamic footer
		if(actionForm.getSelectedAsOfDate() != null){
			Date asOfDate = 	new Date(Long.valueOf(actionForm.getSelectedAsOfDate()));
			if(asOfDate != null){		
				ContractClass contractClass= FeeServiceDelegate.getInstance(Environment.getInstance().getAppId()).getContractRatetype(contract.getContractNumber(),asOfDate);
				if(contractClass!=null && BDConstants.CLASS_ZERO.equals(contractClass.getClassId())) {
					actionForm.setClassZero(true);
				} else {
					actionForm.setClassZero(false);
				}
			}
		}
		if(actionForm.getSelectedAsOfDate() != null){
			Date asOfDate = 	new Date(Long.valueOf(actionForm.getSelectedAsOfDate()));
			if(asOfDate != null){
				boolean svgifInd = false;
				
				if(StringUtils.equals(BDConstants.SELECTED, actionForm.getSelectedView())) {
					svgifInd = FeeServiceDelegate.getInstance(Environment.getInstance().getAppId()).getSvgIndicatorFlg(contract.getContractNumber(), true);
					actionForm.setSvfFlag(svgifInd);
				}else {
					svgifInd = FeeServiceDelegate.getInstance(Environment.getInstance().getAppId()).getSvgIndicatorFlg(contract.getContractNumber(), false);
					actionForm.setSvfFlag(svgifInd);
				}
			}
		}
		return forward;
	}
	
	
	/**
	 * load Total A & B Section Fees
	 * 
	 * @param investmentSelectionReportForm
	 * @param reportData
	 * @throws SystemException
	 */
	private EstimatedJhRecordKeepingCostSummary getSummaryOfRecordKeepingFees(
			InvestmentSelectionReportForm investmentSelectionReportForm,
			InvestmentCostReportData reportData, FeeCmaDataImpl feeCmaData) throws SystemException {
		
		
		return EstimatedRecordKeepingCostComponentGenerator.generateSummaryOfRecordkeepingCostSubComponents(
				FeeServiceDelegate.getInstance(Environment.getInstance().getAppId())
				.isPreAlignmentDate(
						new Date(Long.valueOf(
								investmentSelectionReportForm
										.getSelectedAsOfDate())
								.longValue())),
				reportData.getContractDetails(),
				reportData.getRecordedEstimatedCostOfRKCharges(), (FeeCmaData) feeCmaData, 
				FeeServiceDelegate.getInstance(Environment.getInstance().getAppId())
				.isPreClassZeroPhaseTwo(
						new Date(Long.valueOf(
								investmentSelectionReportForm
										.getSelectedAsOfDate())
								.longValue())));
	
	}
	
	/*
	 * This method used to get the investment page lower date range of effective date 
	 * @return date - lower date range 
	 * @throws SystemException
	 */
	private Date getInvestmentPageLowerLimitDate() throws SystemException {
		Map<String, String> businessParams = EnvironmentServiceDelegate.getInstance(Environment.getInstance().getAppId()).getBusinessParamMap();
		String value = businessParams.get(FEE_INVESTMENT_PAGE_DATE_LIMIT);
		Date lowerLimitDate = null;
		if(StringUtils.isNotEmpty(value)) {
			try {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				lowerLimitDate = formatter.parse(value);
			} catch (ParseException e) {
				logger.error("Exception the business param FEE_INVESTMENT_PAGE_DATE_LIMIT is not " +
						"valid " + e.getMessage());
			    throw new SystemException("Exception the business param FEE_INVESTMENT_PAGE_DATE_LIMIT " +
			    		"is not valid " + e.getMessage());
			}
		}
		return lowerLimitDate;
	}
	
	/**
	 * validates whether the ML funds are to be included 
	 * 
	 * @param request
	 * @return true, if the user is Merrill Lynch user
	 * 					or contract is Merrill Lynch contract, 
	 * 					
	 * @throws SystemException
	 */
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
        return isMerrillAdvisor || isMerrillContract;
	}

	/* 
	 * This method is used to set up report criteria
	 * (non-Javadoc)
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria, com.manulife.pension.platform.web.report.BaseReportForm, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form, HttpServletRequest request)
			throws SystemException {
		InvestmentSelectionReportForm investmentSelectionReportForm = (InvestmentSelectionReportForm) form;
		BobContext bob = BDSessionHelper.getBobContext(request) ;
		criteria.addFilter(InvestmentCostReportData.FILTER_CONTRACT_NO, Integer.valueOf(bob.getCurrentContract().getContractNumber()));
		criteria.addFilter(InvestmentCostReportData.FILTER_AS_OF_DATE, investmentSelectionReportForm.getSelectedAsOfDate());
		boolean selectedViewFlag = true;
		if(StringUtils.equals(BDConstants.AVAILABLE, investmentSelectionReportForm.getSelectedView())) {
			selectedViewFlag = false;
			criteria.addFilter(InvestmentCostReportData.FILTER_MERRILL_COVERED_FUNDS, includeOnlyMerrillCoveredFunds(request, bob.getCurrentContract().getContractNumber()));
		}else{
			criteria.addFilter(InvestmentCostReportData.FILTER_MERRILL_COVERED_FUNDS, false);
		}
		criteria.addFilter(InvestmentCostReportData.SELECTED_VIEW, selectedViewFlag);
		// check if chosen date is the last date in the report drop down
		if(investmentSelectionReportForm.getSelectedAsOfDate() != null &&
				investmentSelectionReportForm.getAvailableReportDates() != null) {
		    criteria.addFilter(InvestmentCostReportData.FILTER_CHECK_FOR_HISTORY, true);
		} else {
			 criteria.addFilter(InvestmentCostReportData.FILTER_CHECK_FOR_HISTORY, false);
		}
	}
	
	/**
	 * 
	 * @param investmentSelectionReportForm
	 * @param investmentCostReportData
	 * @param request
	 * @return
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 *  * @throws SystemException 
	 */
    @SuppressWarnings("unchecked")
	public String prepareXMLFromReport(InvestmentSelectionReportForm investmentSelectionReportForm,
			InvestmentCostReportData investmentCostReportData, HttpServletRequest request) throws ParserConfigurationException, TransformerException, SystemException {
        
        PDFDocument doc = new PDFDocument();
        // Gets layout page for regulatoryDisclosures.jsp
        LayoutPage layoutPageBean = getLayoutPage(BDPdfConstants.REGULATOTY_DISCLOSURE_PATH, request);

        Element rootElement = doc.createRootElement(BDPdfConstants.REGULATORY_DISCLOSURES);
        
		String regDiscText1 = ContentUtility.getContentAttributeText(layoutPageBean, CommonContentConstants.INTRO2_TEXT, null);
				
		doc.appendTextNode(rootElement, "regDiscText1", regDiscText1);
		
        BobContext bob = BDSessionHelper.getBobContext(request) ;
		Contract currentContract = bob.getCurrentContract();
        doc.appendTextNode(rootElement, "currentContract", Integer.toString(currentContract.getContractNumber()));
        doc.appendTextNode(rootElement, "currentContractName", currentContract.getCompanyName());
        
        HttpSession session = request.getSession(false);
        investmentSelectionReportForm.setSelectedAsOfDate((String)session.getAttribute(BDConstants.SELECTED_AS_OF_DATE));
        String formattedValue = DateRender.formatByPattern(	new Date(Long.valueOf(investmentSelectionReportForm.getSelectedAsOfDate()).longValue()), null,RenderConstants.MEDIUM_MDY_SLASHED);
        doc.appendTextNode(rootElement, BDPdfConstants.ASOF_DATE, formattedValue);

        // Sets Logo, Page Name, Contract Details, Intro-1, Intro-2.
        setIntroXMLElements(layoutPageBean, doc, rootElement, request);
        
        EstimatedJhRecordKeepingCostSummary recordKeepingCostsummary = (EstimatedJhRecordKeepingCostSummary) request.getAttribute(BDConstants.ESTIMATED_JH_RECORDKEEPING_COST_SUMMARY);
        String regDiscText2 = recordKeepingCostsummary.getIntroductionText();
        String	regDiscText3 = ContentHelper.getContentText(BDContentConstants.REPORT_DETAILS_SUMMARY_CONTENT,ContentTypeManager.instance().MESSAGE, null); 
        String fwiDisclosureText = ContentHelper.getContentText(BDContentConstants.FEE_WAIVER_DISCLOSURE_TEXT,ContentTypeManager.instance().DISCLAIMER, null);
        String restrictedFundsText = ContentHelper.getContentText(BDContentConstants.REGULATORY_RESTRICTED_FUNDS_TEXT,ContentTypeManager.instance().MESSAGE, null);
        //regDiscText3.replaceAll("</p>", StringUtils.EMPTY);
        doc.appendTextNode(rootElement,"regDiscText2", regDiscText2);		
        doc.appendTextNode(rootElement,"regDiscText3", regDiscText3);
        doc.appendTextNode(rootElement,"fwiDisclosureText", StringEscapeUtils.unescapeHtml(ContentUtility.filterCMAContentForPDF(fwiDisclosureText)));
        
    	
		if (includeOnlyMerrillCoveredFunds(request, currentContract.getContractNumber())) {
			doc.appendTextNode(rootElement, "restrictedFundsText", "\n"
					+ StringEscapeUtils.unescapeHtml(ContentUtility.filterCMAContentForPDF(restrictedFundsText)));
		}
	
        doc.appendTextNode(rootElement,"totalAandBlableText", ContentUtility.filterCMAContentForPDF(recordKeepingCostsummary.getLableText()));
        doc.appendTextNode(rootElement,"totalAandB", recordKeepingCostsummary.getTotalAmount());
        doc.appendTextNode(rootElement,"totalAintroductionText", parseSuperTagToXSLFO(recordKeepingCostsummary.getSectionTotalA().getIntroductionText()));
        doc.appendTextNode(rootElement,"totalBintroductionText", parseSuperTagToXSLFO(recordKeepingCostsummary.getSectionTotalB().getIntroductionText()));
        doc.appendTextNode(rootElement,"totalAlableText", parseSuperTagToXSLFO(recordKeepingCostsummary.getSectionTotalA().getLableText()));
        doc.appendTextNode(rootElement,"totalBlableText", parseSuperTagToXSLFO(recordKeepingCostsummary.getSectionTotalB().getLableText()));
        doc.appendTextNode(rootElement,"totalA", recordKeepingCostsummary.getSectionTotalA().getTotalAmount());
        doc.appendTextNode(rootElement,"totalB", recordKeepingCostsummary.getSectionTotalB().getTotalAmount());
        doc.appendTextNode(rootElement,"fundsSelected", Integer.toString(investmentCostReportData.getNumberOfFundsSelected()));
        doc.appendTextNode(rootElement,"classOfFunds", investmentCostReportData.getContractClassName());
        String view ;
        if(investmentSelectionReportForm.getSelectedView().equalsIgnoreCase("selected")){
        	view = "Selected investment options";
        }else{
        	view = "All available investment options";	
        }
        doc.appendTextNode(rootElement,"shown", view);
        if(!recordKeepingCostsummary.isPreAlignmentIndicator()){
	        doc.appendTextNode(rootElement,"notPreAlignment", "notPreAlignment");
	        Element feeDetailsSectionAElement;
	        List<FeeDetail> FeeDetailSectionA= recordKeepingCostsummary.getSectionTotalA().getFeeDetails();
	        for (FeeDetail feeDetailSecA : FeeDetailSectionA) {
	        	feeDetailsSectionAElement=doc.createElement("sectionElementsA");
	        	doc.appendTextNode(feeDetailsSectionAElement, "description",parseSuperTagToXSLFO(feeDetailSecA.getDescription()));
	        	doc.appendTextNode(feeDetailsSectionAElement, "methodOfPayment", feeDetailSecA.getMethodOfPayment());
	        	doc.appendTextNode(feeDetailsSectionAElement, "amount", feeDetailSecA.getAmount());
	        	doc.appendElement(rootElement, feeDetailsSectionAElement);
	        }
	        
	        Element feeDetailsSectionBElement;
	        List<FeeDetail> FeeDetailSectionB= recordKeepingCostsummary.getSectionTotalB().getFeeDetails();
	        for (FeeDetail feeDetailSecB : FeeDetailSectionB) {
	        	feeDetailsSectionBElement=doc.createElement("sectionElementsB");
	        	doc.appendTextNode(feeDetailsSectionBElement, "description",parseSuperTagToXSLFO(feeDetailSecB.getDescription()));
	        	doc.appendTextNode(feeDetailsSectionBElement, "methodOfPayment", feeDetailSecB.getMethodOfPayment());
	        	doc.appendTextNode(feeDetailsSectionBElement, "amount", feeDetailSecB.getAmount());
	        	doc.appendElement(rootElement, feeDetailsSectionBElement);
	        }
	        
	        Element revenueAddendumSectionAElement;
	        List<FeeDetail> revenueAddendumDetailsA= recordKeepingCostsummary.getRevenueAddendumDetailsSubSection().getFeeDetails();
	        for (FeeDetail feeDetailSecA : revenueAddendumDetailsA) {
	        	revenueAddendumSectionAElement=doc.createElement("revenueAddendumElementsA");
	        	doc.appendTextNode(revenueAddendumSectionAElement, "description",parseSuperTagToXSLFO(feeDetailSecA.getDescription()));
	        	doc.appendTextNode(revenueAddendumSectionAElement, "amount", feeDetailSecA.getAmount());
	        	doc.appendElement(rootElement, revenueAddendumSectionAElement);
	        }
	        
	        Element revenueAddendumSectionBElement;
	         
	        	revenueAddendumSectionBElement=doc.createElement("revenueAddendumElementsB");
	        	doc.appendTextNode(revenueAddendumSectionBElement, "description",parseSuperTagToXSLFO(recordKeepingCostsummary.getRevenueAddendumDetailsSubSection().getLableText()));
	        	doc.appendTextNode(revenueAddendumSectionBElement, "amount",recordKeepingCostsummary.getRevenueAddendumDetailsSubSection().getAmount());
	        	doc.appendElement(rootElement, revenueAddendumSectionBElement);
	        }
        
        
        //Class Zero Phase 2 Changes - Dollar Based Section
        
        doc.appendTextNode(rootElement,"dollarBasedSectionHeading", 
        					ContentUtility.filterCMAContentForPDF(recordKeepingCostsummary.getDollarBasedSection().getIntroductionText()));
        doc.appendTextNode(rootElement,"dollarBasedSectionIntro", recordKeepingCostsummary.getDollarBasedSection().getLableText());
        
        if(!recordKeepingCostsummary.isPreClassZeroPhaseTwoInd()){
	        Element feeDetailsSectionDBElement;
	        List<DollarBasedFeeDetail> dollarBasedFeeDetail = recordKeepingCostsummary.getDollarBasedSection().getFeeDetails();
	        for (DollarBasedFeeDetail feeDetailDB : dollarBasedFeeDetail) {
	        	feeDetailsSectionDBElement=doc.createElement("sectionElementsDB");
	        	doc.appendTextNode(feeDetailsSectionDBElement, "description", parseSuperTagToXSLFO(feeDetailDB.getDescription()));
	        	doc.appendTextNode(feeDetailsSectionDBElement, "methodOfPayment", feeDetailDB.getMethodOfPayment());
	        	doc.appendTextNode(feeDetailsSectionDBElement, "amount", feeDetailDB.getAmount());
	        	doc.appendTextNode(feeDetailsSectionDBElement, "frequency", feeDetailDB.getFeeFrequency());
	        	doc.appendElement(rootElement, feeDetailsSectionDBElement);
	        	
	        	doc.appendTextNode(rootElement,"notPreClassZeroPhaseTwo", "notPreClassZeroPhaseTwo");
	        }
        }
        
        //Class Zero Phase 2 Changes - Dollar Based section Ends
        	BigDecimal maxCredit=new BigDecimal(0.15);
        	Boolean isSvpContract= investmentCostReportData.getHasSvpFund();
            Element fundDetailsElement;
            Element fundDetailElement;
            Map<InvestmentGroup,List<FundFeeVO>> funds= investmentCostReportData.getInvestmentData();
            int varStatus=0;
    		for(Map.Entry<InvestmentGroup,List<FundFeeVO>> entry : funds.entrySet()){
    			fundDetailsElement= doc.createElement(BDPdfConstants.FUND_DETAILS);
    			
    			doc.appendTextNode(fundDetailsElement, BDPdfConstants.FUND_GROUP, entry.getKey().getGroupname());
    			for (FundFeeVO fundFeeVo : entry.getValue()) {
    				if(isSvpContract && (maxCredit.compareTo(fundFeeVo.getTotalRevenueUsedTowardsPlanCosts())<0)){
    					maxCredit=fundFeeVo.getTotalRevenueUsedTowardsPlanCosts();
    				}
    				
					fundDetailElement = doc.createElement(BDPdfConstants.FUND_DETAIL);
					  setFundDetailsXMLElements(doc, fundDetailElement, fundFeeVo, investmentSelectionReportForm, investmentCostReportData, request);
	                doc.appendElement(fundDetailsElement, fundDetailElement);
    			}
    			doc.appendElement(rootElement, fundDetailsElement);  
    			varStatus++;
    		}
    		
    		Element theReport;
    		
    		theReport=doc.createElement("theReport");
			doc.appendTextNode(theReport, "averageUnderlyingFundNetCost",NumberRender.format(investmentCostReportData.getAverageUnderlyingFundNetCost(), DEFAULT_VALUE,
					PATTERN, null, SCALE, ROUNDING_MODE, DIGITS, SIGN));
        	doc.appendTextNode(theReport, "averageRevenueFromUnderlyingFund",NumberRender.format(investmentCostReportData.getAverageRevenueFromUnderlyingFund(), DEFAULT_VALUE,
					PATTERN, null, SCALE, ROUNDING_MODE, DIGITS, SIGN));
        	doc.appendTextNode(theReport, "averageRevenueFromSubAccount",NumberRender.format(investmentCostReportData.getAverageRevenueFromSubAccount(), DEFAULT_VALUE,
					PATTERN, null, SCALE, ROUNDING_MODE, DIGITS, SIGN));
        	doc.appendTextNode(theReport, "averageTotalRevenueUsedTowardsPlanCosts",NumberRender.format(investmentCostReportData.getAverageTotalRevenueUsedTowardsPlanCosts(), DEFAULT_VALUE,
					PATTERN, null, SCALE, ROUNDING_MODE, DIGITS, SIGN));
        	doc.appendTextNode(theReport, "averageExpenseRatio",NumberRender.format(investmentCostReportData.getAverageExpenseRatio(), DEFAULT_VALUE,
					PATTERN, null, SCALE, ROUNDING_MODE, DIGITS, SIGN));
        	
        	doc.appendElement(rootElement, theReport);
        	List<String> footnotes=  recordKeepingCostsummary.getOrderedFootNotes();
    		Element footnotesElement;
    		for(String  footnote : footnotes){
	             footnotesElement = doc.createElement(PdfConstants.TAB_FOOTNOTES);
	             doc.appendTextNode(footnotesElement, PdfConstants.VALUE,footnote);
	             doc.appendElement(rootElement, footnotesElement);
                 }
    		 Map<String, MutableFootnote> footnotesMap = new LinkedMap();
    		 MutableFootnote[] footnotesUSA;
			try {
				footnotesUSA = retrieveFootnotes();
				Arrays.sort( footnotesUSA, Footnotes.getInstance() );
				if (footnotesUSA != null) {
	                 for (MutableFootnote footnoteUSA : footnotesUSA) {
	                     footnotesMap.put(footnoteUSA.getSymbol().trim(), footnoteUSA);
	                 }
	             }
			} catch (ContentException e) {
				throw new TransformerException("Exception occured in footnotesElement " + e.toString());
			}
	    	
			HashMap<String, String> hash = new HashMap<String, String>();
			HashMap<String, Footnote> hashNotes = new LinkedHashMap<String, Footnote>();
			String[] symbols = investmentCostReportData.getFootNotes().toArray(new String[1]);
			for( int i = 0; i < symbols.length; i++ ){
				hash.put( symbols[i], symbols[i] );
			}
			
			Set<String> keySet = footnotesMap.keySet();
			Iterator<String> it = keySet.iterator();
			String symbol = StringUtils.EMPTY;
			while(it.hasNext()) {
				symbol = it.next();
				if ( hash.containsKey(symbol)){
					hashNotes.put(symbol, footnotesMap.get(symbol));
				}
			}
			
			
			Element orderedFootnotesElement;
			for(Map.Entry<String, Footnote> entry : hashNotes.entrySet()){
				String FOOTNOTE = "ofn";
				orderedFootnotesElement = doc.createElement(FOOTNOTE);
	             String footNotesContent = entry.getValue().getText();
	             doc.appendTextNode(orderedFootnotesElement, PdfConstants.KEY,entry.getKey());
	             doc.appendTextNode(orderedFootnotesElement, PdfConstants.VALUE,footNotesContent);
	             doc.appendElement(rootElement, orderedFootnotesElement);
                }
    		  
        // Sets footer, footnotes and disclaimer
		String footer;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		if (investmentSelectionReportForm.isClassZero() && investmentSelectionReportForm.isSvfFlag()) {
			String svgFooter = ContentHelper.getContentText(CommonContentConstants.SVGIF_DISCLOSURE,
								ContentTypeManager.instance().MESSAGE, null);	
			String sigFooter = ContentHelper.getContentText(BDContentConstants.INVESTMENT_SELECTION_CONTENT_CL0,
								ContentTypeManager.instance().MESSAGE, null);			
			if (sigFooter.contains("{0}")) {
				 sigFooter = sigFooter.replace("{0}", df.format(maxCredit)+"%");
				}
			footer = svgFooter+sigFooter;
		}else if(investmentSelectionReportForm.isClassZero()) {			
			footer = ContentHelper.getContentText(
					BDContentConstants.INVESTMENT_SELECTION_CONTENT_CL0,
					ContentTypeManager.instance().MESSAGE, null);
			 if (footer.contains("{0}")) {
				 footer = footer.replace("{0}",df.format(maxCredit)+"%");
				}
		} else {
			footer = ContentHelper.getContentText(
					BDContentConstants.INVESTMENT_SELECTION_CONTENT_NON_CL0,
					ContentTypeManager.instance().MESSAGE, null);
		}
         doc.appendTextNode(rootElement,PdfConstants.FOOTER, footer);	

         
         
         
         String footnotesAtBottom = ContentUtility.getPageFootnotes(layoutPageBean,	new String[] {}, -1);
         final Pattern PATTERN_LINE_BREAK = Pattern.compile(".<br[^>]*>", Pattern.CASE_INSENSITIVE);
         footnotesAtBottom = PATTERN_LINE_BREAK.matcher(footnotesAtBottom).replaceAll(".<p></p>");
         doc.appendTextNode(rootElement,PdfConstants.FOOTNOTES,footnotesAtBottom);
          
         String globalDisclosureText = ContentHelper.getContentText(BDContentConstants.BD_GLOBAL_DISCLOSURE,
                 ContentTypeManager.instance().MISCELLANEOUS, null);
         final Pattern PATTERN_LINE_BREAK_DISC = Pattern.compile("<br><br[^>]*>", Pattern.CASE_INSENSITIVE);
         globalDisclosureText = PATTERN_LINE_BREAK_DISC.matcher(globalDisclosureText).replaceAll("<p></p>");
         doc.appendTextNode(rootElement,PdfConstants.GLOBAL_DISCLOSURE,globalDisclosureText);
         
         StringWriter writer = new StringWriter();
         TransformerFactory tf = TransformerFactory.newInstance();
         Transformer transformer1 = tf.newTransformer();
         transformer1.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
         transformer1.setOutputProperty(OutputKeys.METHOD, "xml");
         transformer1.setOutputProperty(OutputKeys.INDENT, "yes");
         transformer1.setOutputProperty(OutputKeys.ENCODING, UTF8);

         transformer1.transform(new DOMSource((Document)doc.getDocument()), new StreamResult(writer));
         writer.flush();
        
        return writer.toString();
    }
    

	/**
     * This method sets fund details XML elements
     * 
     * @param doc
     * @param fundDetailElement
	 * @param investmentCostReportData 
     * @param fund
     */

    @SuppressWarnings("unchecked")
	private void setFundDetailsXMLElements(PDFDocument doc, Element fundDetailElement, FundFeeVO fundFeeVo,
			InvestmentSelectionReportForm investmentSelectionReportForm, InvestmentCostReportData investmentCostReportData, HttpServletRequest request) throws SystemException{
    	BobContext bob = BDSessionHelper.getBobContext(request) ;
        Element fundTxnElement = doc.createElement(BDPdfConstants.FUND_TXN);
        if (fundFeeVo != null) {
        	doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_ID, fundFeeVo.getFundId());
        	doc.appendTextNode(fundTxnElement, BDPdfConstants.FEE_WAIVER_INDICATOR,
            		String.valueOf(investmentCostReportData.isFeeWaiverFund(fundFeeVo.getFundId())));

			if (includeOnlyMerrillCoveredFunds(request, bob.getCurrentContract().getContractNumber())) {
				doc.appendTextNode(fundTxnElement, BDPdfConstants.MERRILL_RESRICTED_FUND_INDICATOR,
						String.valueOf(investmentCostReportData.isRestrictedFund(fundFeeVo.getFundId())));
			}
        	doc.appendTextNode(fundTxnElement, BDPdfConstants.FUND_NAME, fundFeeVo.getFundName());
        	StringBuffer footNoteMarkers = new StringBuffer();
        	if(fundFeeVo.getFootNoteMarkers().size()>0){
        		for (Iterator iterator = fundFeeVo.getFootNoteMarkers().iterator(); iterator.hasNext();) {
        			footNoteMarkers.append((String)iterator.next());
				}
        	}
        	
        	if (fundFeeVo.getFundType().equals("GA")){
        		footNoteMarkers.append("N21");
        	}
			if ((fundFeeVo.getFundId().equals("MSV"))||(fundFeeVo.getFundId().equals("NMY"))){
				footNoteMarkers.append("N19");		
			        	}

        	doc.appendTextNode(fundTxnElement, "footNoteMarkers", footNoteMarkers.toString());
        	
        	doc.appendTextNode(fundTxnElement, BDPdfConstants.UNDERLYING_FUND_NETCOST, NumberRender.format(fundFeeVo.getUnderlyingFundNetCost(), DEFAULT_VALUE,
					PATTERN, null, SCALE, ROUNDING_MODE, DIGITS, SIGN));
        	doc.appendTextNode(fundTxnElement, BDPdfConstants.REVENUE_FROM_UNDERLYINGFUND, NumberRender.format(fundFeeVo.getRevenueFromUnderlyingFund(), DEFAULT_VALUE,
					PATTERN, null, SCALE, ROUNDING_MODE, DIGITS, SIGN));
        	doc.appendTextNode(fundTxnElement, BDPdfConstants.REVENUE_FROM_SUBACCOUNT, NumberRender.format(fundFeeVo.getRevenueFromSubAccount(), DEFAULT_VALUE,
					PATTERN, null, SCALE, ROUNDING_MODE, DIGITS, SIGN));
        	doc.appendTextNode(fundTxnElement, BDPdfConstants.TOTAL_REVENUE_USED_TOWARDS_PLANCOSTS, NumberRender.format(fundFeeVo.getTotalRevenueUsedTowardsPlanCosts(), DEFAULT_VALUE,
					PATTERN, null, SCALE, ROUNDING_MODE, DIGITS, SIGN));
        	doc.appendTextNode(fundTxnElement, BDPdfConstants.EXPENSE_RATIO, NumberRender.format(fundFeeVo.getExpenseRatio(), DEFAULT_VALUE,
					PATTERN, null, SCALE, ROUNDING_MODE, DIGITS, SIGN));
    		doc.appendTextNode(fundTxnElement, BDPdfConstants.REDEMPTION_FEE, NumberRender.format(fundFeeVo.getRedemptionFee(), DEFAULT_VALUE,
					PATTERN, null, SCALE, ROUNDING_MODE, DIGITS, SIGN));
        	
        	if(investmentSelectionReportForm.getSelectedView().equals(BDConstants.SELECTED)){
        		if(fundFeeVo.isFundfeeChanged() || fundFeeVo.isRedemptionFeeChanged() ){
        			doc.appendTextNode(fundTxnElement, "fontWeight", "bold");
        		}
        		
        	}else{
        		if(fundFeeVo.isSelectedFund()){
            		if(fundFeeVo.isFundfeeChanged() || fundFeeVo.isRedemptionFeeChanged() ){
            			doc.appendTextNode(fundTxnElement, "fontWeight", "bold");
            			doc.appendTextNode(fundTxnElement, "rowColor", "#FFFFDB");
            		}else{
            			doc.appendTextNode(fundTxnElement, "rowColor", "#FFFFDB");
            		}
        		}else{
        			if(!fundFeeVo.isFundfeeChanged() && !fundFeeVo.isRedemptionFeeChanged() ){
            			doc.appendTextNode(fundTxnElement, "fontWeight", "bold");
            		}
        		}
        	} 
        }
        doc.appendElement(fundDetailElement, fundTxnElement);
    }
	
	
	 /**
     * This method will generate the PDF and return a ByteArrayOutputStream which will be sent back to
     * the user.
     * This method would:
     * - Create the XML-String from VO.
     * - Create the PDF using the created XML-String and XSLT file.
     * @throws ContentException
     */
    protected ByteArrayOutputStream prepareXMLandGeneratePDF(BaseReportForm reportForm,
            ReportData report, HttpServletRequest request) throws SystemException {

        ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();
        try {
        	InvestmentSelectionReportForm investmentSelectionReportForm = (InvestmentSelectionReportForm) reportForm;
        	InvestmentCostReportData data = (InvestmentCostReportData) report;
            Object xmlTree = prepareXMLFromReport(investmentSelectionReportForm, data, request);
            String xsltFileName = getXSLTFileName();
            String configFileName = getFOPConfigFileName();
            if (xmlTree == null || xsltFileName == null) {
                return pdfOutStream;
            }
            String xsltfile = ReportsXSLProperties.get(xsltFileName);
            String configfile = ReportsXSLProperties.get(configFileName);
            String includedXSLPath = ReportsXSLProperties.get(CommonConstants.INCLUDED_XSL_FILES_PATH);
            if (xmlTree instanceof String) {
            	pdfOutStream = PDFGenerator.getInstance().generatePDF((String)xmlTree, xsltfile, configfile, includedXSLPath, true);
            }

        } catch (Exception exception) {
            String message = null;
            if (exception instanceof ContentException) {
                message = "Error occured while retrieveing CMA Content during PDF creation.";
            } else if (exception instanceof ParserConfigurationException) {
                message = "Error occured while creating Document object during PDF creation.";
            } else if (exception instanceof FOPException || exception instanceof TransformerException ||
                    exception instanceof IOException) {
                message = "Error occured during PDF generation.";
            } else {
                message = "Error occured during PDF generation.";
            }

            throw new SystemException(exception, message);
        }
        return pdfOutStream;
    }
    
    private MutableFootnote[] retrieveFootnotes() throws ContentException {
        MutableFootnote[] footnotes = (MutableFootnote[]) BrowseServiceDelegate.getInstance()
                .findContent(ContentTypeManager.instance().FOOTNOTE);
        return footnotes == null ? new MutableFootnote[0] : footnotes;
    }
    
    /**
     * Overrides this method to write the report to a PrintWriter.
     *
     * @param reportForm
     *            The report form.
     * @param report
     *            The report data.
     * @param request
     *            The HTTP request.
     * @return byte array
     */
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, 
			HttpServletRequest request)	throws SystemException {
		// no implementation
		return null;
	}
    
    /**
     * Overridden method returns unique report ID for Investment Cost Report Data.
     * @return reportId
     */
	@Override
	protected String getReportId() {
		return InvestmentCostReportData.REPORT_ID;
	}
    /**
     * Overridden method returns unique report name for Investment Cost Report Data.
     * @return reportName
     */
	@Override
	protected String getReportName() {
		return InvestmentCostReportData.REPORT_NAME;
	}
    /**
     * Overridden method used to set the default sort field for the criteria
     * @return sortFieldName
     */
	@Override
	protected String getDefaultSort() {
		return null;
	}
    /**
     * Overridden method used to set the default sort direction for the criteria
     * @return sortDirection
     */
	@Override
	protected String getDefaultSortDirection() {
		return null;
	}
    
	/**
	 * @See BaseReportAction#getXSLTFileName()
	 */
	@Override
	public String getXSLTFileName() {
		return XSLT_FILE_KEY_NAME;
	}
	
	public static String parseSuperTagToXSLFO(String content) {
		
		
		final Pattern PATTERN_SUP_TAG = Pattern.compile("<sup[^>]*>|<SUP[^>]*>");
		final String FO_INLINE_SUPERSCRIPT = "<FRWSUP>";
		
        // replace all <sup> with <fo:inline baseline-shift="super">
        content = PATTERN_SUP_TAG.matcher(content).replaceAll(FO_INLINE_SUPERSCRIPT);
        
        return content;
	}
	
	/**
	 * This code has been changed and added  to 
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
     * Invokes the doDisplayRegulatoryDisclosure task . It uses the common
     * workflow with validateForm set to true.
     *
     * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
     *      HttpServletResponse, boolean)
     */
	@RequestMapping(value ="/contract/regulatoryDisclosures/" ,params={"task=regulatoryDisclosure"}   , method =  {RequestMethod.GET}) 
	public String doRegulatoryDisclosure (@Valid @ModelAttribute("investmentSelectionReportForm") InvestmentSelectionReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			try {
				doCommon( (InvestmentSelectionReportForm)form, request, null);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doRegulatoryDisclosure");
        }

         forward = doCommon( form, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doRegulatoryDisclosure");
        }

        return forwards.get("regulatoryDisclosure");
    }
    
    
    /* (non-Javadoc)
     * @see com.manulife.pension.platform.web.report.BaseReportAction#doDefault(org.apache.struts.action.ActionMapping,
     *  com.manulife.pension.platform.web.report.BaseReportForm, javax.servlet.http.HttpServletRequest,
     *   javax.servlet.http.HttpServletResponse)
     */
	@RequestMapping(value ="/contract/regulatoryDisclosures/", method ={RequestMethod.GET,RequestMethod.POST}) 
	public String doDefault (@Valid @ModelAttribute("investmentSelectionReportForm") InvestmentSelectionReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		
		if(bindingResult.hasErrors()){
			try {
				doCommon( (InvestmentSelectionReportForm)form, request, null);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDefault");
		}        
		//forward =super.doExecute( form, request, response);
	//	String forward = forwards.get("default");

		Date invesmentFeePageDateLowerLimit = getInvestmentPageLowerLimitDate();
		BobContext bob = BDSessionHelper.getBobContext(request) ;

		Contract contract = bob.getCurrentContract();

		ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();

		Map<String, String> mtaMap = contractServiceDelegate.getMtaContractDetails(contract.getContractNumber());
		Boolean isGovernmentPlan = null;
		try {
			isGovernmentPlan = contractServiceDelegate.isGovernmentPlan(contract.getContractNumber());
		} catch (ContractDoesNotExistException e1) {
			// not a contract
		}
		//404 section
		if((contract.getStatus().equals(BDConstants.ACTIVE)||(contract.getStatus().equals(BDConstants.FROZEN)))
				&& !contract.isDefinedBenefitContract()
				&& (mtaMap != null && (!StringUtils.equals(mtaMap.get(BDConstants.DISTRIBUTION_CHANNEL), BDConstants.MTA) 
						&& !StringUtils.equals(mtaMap.get(BDConstants.GROUP_FIELD_OFFICE_NO), BDConstants.GFO_CODE_25270)
						&& !StringUtils.equals(mtaMap.get(BDConstants.GROUP_FIELD_OFFICE_NO), BDConstants.GFO_CODE_25280)))
				&& (isGovernmentPlan != null && !isGovernmentPlan)){
			form.setDisplay404Section(true);
		}else{
			form.setDisplay404Section(false);
		}
		//408b2 section 
		if(FeeDisclosureUtility.isFeeDisclsoureAvaiable(contract)){
			form.setShow408b2Section(true);
		}else{
			form.setShow408b2Section(false); 
		}

		if(form.isDisplay404Section()){
			UserAccess access = UserAccess.FRW;
			BDUserProfile userProfile = getUserProfile(request);

			try {

				Access404a5 contractAcc =
						FundServiceDelegate
						.getInstance()
						.get404a5Permissions(
								EnumSet.of(
										Facility._404A5_PLAN_AND_INVESTMENT_NOTICE,
										Facility.INVESTMENT_COMPARATIVE_CHART,
										Facility.IMPORTANT_PLAN_INFORMATION_ADDENDUM_TEMPLATE,
										Facility.PARTICIPANT_FUND_CHANGE_NOTICE_TEMPLATE),
								contract.getContractNumber(),
								access);

				Qualification piNoticeQual = contractAcc.getAccess(Facility._404A5_PLAN_AND_INVESTMENT_NOTICE);
				Qualification iccQual = contractAcc.getAccess(Facility.INVESTMENT_COMPARATIVE_CHART);

				form.setDisplay404Section(! contractAcc.getAccessibleFacilities().isEmpty());
				form.setShowPlanAndInvestmentNotice(
						piNoticeQual != null
						&& ! piNoticeQual.getTemporarilyMissingInformation().contains(MissingInformation.ICC_CONTACT));
				form.setShowIcc(
						iccQual != null
						&& ! iccQual.getTemporarilyMissingInformation().contains(MissingInformation.ICC_CONTACT));
				form.setShowPreviousYearEndIccUnavailableMessage(
						piNoticeQual != null
						&& piNoticeQual.getTemporarilyMissingInformation().contains(MissingInformation.PREVIOUS_YEAR_END_FUND_DATA)
						|| iccQual != null
						&& iccQual.getTemporarilyMissingInformation().contains(MissingInformation.PREVIOUS_YEAR_END_FUND_DATA));
				form.setShowMissingIccContactMessage(
						piNoticeQual != null
						&& piNoticeQual.getTemporarilyMissingInformation().contains(MissingInformation.ICC_CONTACT)
						|| iccQual != null
						&& iccQual.getTemporarilyMissingInformation().contains(MissingInformation.ICC_CONTACT));
				form.setShowIpiAddendum(contractAcc.getAccess(Facility.IMPORTANT_PLAN_INFORMATION_ADDENDUM_TEMPLATE) != null);
				form.setShowParticipantFundChangeNotice(contractAcc.getAccess(Facility.PARTICIPANT_FUND_CHANGE_NOTICE_TEMPLATE) != null);
				
				boolean contractAndProductRestriction = ContractServiceDelegate.getInstance()
						.getContractAndProdcuctRestrictionForIAContract(contract.getContractNumber());
				form.setContractandProductRestrictionFlag(contractAndProductRestriction);

			} catch (SystemException se) {

				LogUtility.logSystemException(
						Constants.PS_APPLICATION_ID,
						new SystemException(
								se,
								"Profile ID " + userProfile.getBDPrincipal().getProfileId() +
								" contract ID " + contract.getContractNumber()));
				form.setDisplay404Section(false);
				form.setShowPlanAndInvestmentNotice(false);
				form.setShowIcc(false);
				form.setShowPreviousYearEndIccUnavailableMessage(false);
				form.setShowMissingIccContactMessage(false);
				form.setShowIpiAddendum(false);
				form.setShowParticipantFundChangeNotice(false);

			}

		}
		return forwards.get("default");
	}
	@RequestMapping(value ="/contract/regulatoryDisclosures/",params={"task=filter"}, method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doFilter (@Valid @ModelAttribute("investmentSelectionReportForm") InvestmentSelectionReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forward=preExecute(form, request, response);
        if ( StringUtils.isNotBlank(forward)) {
        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
		if(bindingResult.hasErrors()){
			try {
				doCommon( (InvestmentSelectionReportForm)form, request, null);
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		}
		        forward=super.doFilter( form, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
     
	 @RequestMapping(value ="/contract/regulatoryDisclosures/" ,params={"task=page"} , method =  {RequestMethod.POST}) 
	    public String doPage (@Valid @ModelAttribute("investmentSelectionReportForm") InvestmentSelectionReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    		throws IOException,ServletException, SystemException {
		 String forward=preExecute(form, request, response);
	        if ( StringUtils.isNotBlank(forward)) {
	        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	        }
		 if(bindingResult.hasErrors()){
			 try {
				 doCommon( (InvestmentSelectionReportForm)form, request, null);
			 } catch (SystemException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			 }
		 }
		  forward=super.doPage( form, request, response);
		 return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	   
	   @RequestMapping(value ="/contract/regulatoryDisclosures/" ,params={"task=sort"}, method =  {RequestMethod.POST}) 
	   public String doSort (@Valid @ModelAttribute("investmentSelectionReportForm") InvestmentSelectionReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			   throws IOException,ServletException, SystemException {
		   String forward=preExecute(form, request, response);
	        if ( StringUtils.isNotBlank(forward)) {
	        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	        }
		   if(bindingResult.hasErrors()){
			   try {
				   doCommon( (InvestmentSelectionReportForm)form, request, null);
			   } catch (SystemException e) {
				   // TODO Auto-generated catch block
				   e.printStackTrace();
			   }
			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			   if(errDirect!=null){
				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			   }
		   }
		    forward=super.doSort( form, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	   }
	   @RequestMapping(value ="/contract/regulatoryDisclosures/", params={"task=download"},method =  {RequestMethod.POST}) 
	   public String doDownload (@Valid @ModelAttribute("investmentSelectionReportForm") InvestmentSelectionReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			   throws IOException,ServletException, SystemException {
		   String forward=preExecute(form, request, response);
	        if ( StringUtils.isNotBlank(forward)) {
	        	   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	        }
		   if(bindingResult.hasErrors()){
			   try {
				   doCommon( (InvestmentSelectionReportForm)form, request, null);
			   } catch (SystemException e) {
				   // TODO Auto-generated catch block
				   e.printStackTrace();
			   }
			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			   if(errDirect!=null){
				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			   }
		   }
		    forward=super.doDownload( form, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	   }
	   
	   
	   @RequestMapping(value ="/contract/regulatoryDisclosures/", params={"task=printPDF"}, method ={RequestMethod.GET,RequestMethod.POST}) 
	    public String doPrintPDF (@Valid @ModelAttribute("investmentSelectionReportForm") InvestmentSelectionReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
				   throws IOException,ServletException, SystemException {
		   if(bindingResult.hasErrors()){
			   try {
				   doCommon( (InvestmentSelectionReportForm)form, request, null);
			   } catch (SystemException e) {
				   // TODO Auto-generated catch block
				   e.printStackTrace();
			   }
			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			   if(errDirect!=null){
				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			   }
		   }
	        String forward = super.doPrintPDF(form, request, response);
	        if (logger.isDebugEnabled()) {
	            logger.debug("exit <- doPrintPDF");
	        }

	        return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	    }
	   
	   
	   @RequestMapping(value ="/contract/regulatoryDisclosures/", params={"task=downloadAll"}, method =  {RequestMethod.POST}) 
	   public String doDownloadAll (@Valid @ModelAttribute("investmentSelectionReportForm") InvestmentSelectionReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			   throws IOException,ServletException, SystemException {
		   String forward=preExecute(form, request, response);
	        if ( StringUtils.isNotBlank(forward)) {
	     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	        }
		   if(bindingResult.hasErrors()){
			   try {
				   doCommon( (InvestmentSelectionReportForm)form, request, null);
			   } catch (SystemException e) {
				   // TODO Auto-generated catch block
				   e.printStackTrace();
			   }
			   String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			   if(errDirect!=null){
				   request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				   return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			   }
		   }
		    forward=super.doDownloadAll( form, request, response);
		   return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	   }	
	
  }
    
    

