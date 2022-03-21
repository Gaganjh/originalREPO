package com.manulife.pension.ps.web.contract;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.SynchronizationServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.ereports.ReportInfo;
import com.manulife.pension.lp.model.ereports.ReportListRequest;
import com.manulife.pension.lp.model.ereports.ReportListResponse;
import com.manulife.pension.lp.model.ereports.RequestConstants;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContributionHistoryReportItem;
import com.manulife.pension.service.contract.valueobject.LoanRepaymentReportItem;
import com.manulife.pension.service.contract.valueobject.OutstandingLoanReportItem;
import com.manulife.pension.service.plan.valueobject.PlanDataLite;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;

/**
 * ContractStatementsAction Action class This class is used to forward the
 * users's request to ContractStatements page
 * 
 * @author Ludmila Stern modified 17/05/2005 Problem log 43844. Setting
 *         setStaffPlanAccessAllowed flag;
 * @author Balamurugan modified for the project Audit CD CL # 132693 2016 march release
 */

@Controller
@RequestMapping( value = "/contract")
@SessionAttributes({"contractStatementsForm"})

public class ContractStatementsController extends PsAutoController {
	
	@ModelAttribute("contractStatementsForm")
	public  ContractStatementsForm populateForm()
	{
		return new  ContractStatementsForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	private static final String INPUT = "input";
	private static final String CONTRACT_STATEMENTS_PAGE = "contractStatements";
	private static final String CONTRACT_STATEMENTS_MTA_PAGE = "contractStatementsMTA";
	private static final String CLIENT_USER_TYPE = "CLIENT";
	private static Environment env = Environment.getInstance();
	private static final FastDateFormat formatPeriodEndingYear_YYYYMMDD = FastDateFormat
			.getInstance("yyyy/MM/dd");
	private static final FastDateFormat formatDate_MMDDYYYY = FastDateFormat
			.getInstance("MM/dd/yyyy");
	private static final FastDateFormat formatYear = FastDateFormat
			.getInstance("yyyy");
	private static final String ZERO_AMOUNT_STRING = "0.00";
	private static final String INTEREST_FORMAT = "##.##";
	private static final String CSV_GENERATED = "csvGenerated";
	private static final String CSV_NOT_GENERATED = "csvNotGenerated";
	private static final String GENERATED_CSV_REPORT = "generatedCSVReport";
	// CSV Headers
	private static final String CSV_COLUMN_HEADING_CONTRIBUTION_HISTORY = "CNNO,REPORT FROM DTE,REPORT TO DTE,REPORT GENERATED ON,LAST NAME,"
			+ "FIRST NAME,DIVISION,PRT STATUS,TRANNO,PROCESS DTE,EFFECTIVE DTE,"
			+ "PAYROLL DTE,MONEY SOURCE,JH STANDARD MT,CUSTOMIZED MT,AMOUNT";
	private static final String CSV_COLUMN_HEADING_OUTSTANDING_LOAN = "Contract Number,Effective Date,"
			+ "Report Generated,Last Name,First Name,Division,Loan #,"
			+ "Loan Issue Effective Date,Loan Issue Amount,Interest Rate,"
			+ "Maturity Date,Balance As Of,Total Outstanding Loan Balance";
	private static final String CSV_COLUMN_HEADING_LOAN_REPAYMENTS = "Contract Number,Report From Date,"
			+ "Report To Date,Report Generated On,Last Name,First Name,Division,Loan #,"
			+ "Effective Date,Txn Number,Loan Repayment,Loan Principle,Loan Interest";

	static {
		forwards.put(INPUT, "/contract/contractStatements.jsp");
		forwards.put(CONTRACT_STATEMENTS_PAGE, "/contract/contractStatements.jsp");
		forwards.put(CONTRACT_STATEMENTS_MTA_PAGE, "/contract/contractStatementsMTA.jsp");
	}

	public ContractStatementsController() {
		super(ContractStatementsController.class);
	}

	@RequestMapping(value ="/contractStatements/", method =  RequestMethod.GET) 
	public String doDefault(@Valid @ModelAttribute("contractStatementsForm") ContractStatementsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        }
	
		ContractStatements contractStatements = new ContractStatements();
		String sitemode = env.getSiteLocation();
		actionForm.setApolloBatchRun(SynchronizationServiceDelegate.getInstance(
                Constants.PS_APPLICATION_ID).isApolloBatchRunning());
		
		// get the user profile object and set the current contract to null
		UserProfile userProfile = getUserProfile(request);
		Contract contract = userProfile.getCurrentContract();
		ReportInfo[] reports = null;
		List<GenericException> errors = new ArrayList<GenericException>();
		try {
			reports = getReports(userProfile, contract, sitemode);
		} catch (ServiceUnavailableException e) {

			errors.add(new GenericException(
					ErrorCodes.REPORT_SERVICE_UNAVAILABLE_CONTRACT_STATEMENTS));
			logger.error("Error reported from Contract Statements Action for the contract :"+contract.getContactId() +" :"+e.getMessage());
		}
		
		if(errors.size()>0){
			/*
			 * if service unavailable exception occurred then handle this attribute
			 * in jsp so that sections not-related to report-service will get
			 * displayed
			 */
				request.getSession().setAttribute("reportServiceNotAvailable",true);
				setErrorsInSession(request, errors);
		}else{
				request.getSession().removeAttribute("reportServiceNotAvailable");
		}
		// call this method to add reportInfo array with new CSV reports details
		reports = addAuditOnCdReports(reports, contract);
		// if no report available then set the error message and display no
		// section
		if (reports == null) {
			return forwards.get(INPUT);
		}

		contractStatements = buildContractStatementsLists(reports);

		// add contractStatements object to the request
		request.getSession().setAttribute(Constants.CONTRACT_STATEMENTS_KEY,
				contractStatements);
		request.getSession().setAttribute("efOptions", contractStatements.getEfOptions());
		request.getSession().setAttribute("paOptions", contractStatements.getPaOptions());
		request.getSession().setAttribute("saOptions", contractStatements.getSaOptions());
		request.getSession().setAttribute("crOptions", contractStatements.getCrOptions());
		request.getSession().setAttribute("bpOptions", contractStatements.getBpOptions()); // defined benefit
		request.getSession().setAttribute("scOptions", contractStatements.getScOptions()); // Schedule C options
		request.getSession().setAttribute("asOptions", contractStatements.getAsOptions());
		request.getSession().setAttribute("clOptions", contractStatements.getClOptions());
		request.getSession().setAttribute("chOptions", contractStatements.getChOptions());
		request.getSession().setAttribute("olOptions", contractStatements.getOlOptions());
		request.getSession().setAttribute("lrOptions", contractStatements.getLrOptions());

		// log the action
		if (logger.isDebugEnabled()) {
			logger.debug(contractStatements);
			logger.debug("forwarding to Contract Statements Page.");
		}

		// find the right forward
		if (contract.isMta())
			return forwards.get(CONTRACT_STATEMENTS_MTA_PAGE);

		return forwards.get(CONTRACT_STATEMENTS_PAGE);

	}

	/**
	 * 
	 * @param actionform
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/contractStatements/" ,params="action=csvDefault"  , method =  RequestMethod.GET) 
	public String doCsvDefault (@Valid @ModelAttribute("contractStatementsForm") ContractStatementsForm actionform, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        }
	
		String reportType = request.getParameter("reportType");
		String periodEnd = null;
		Date inputPeriodEndDate = null;
		UserProfile userProfile = getUserProfile(request);
		Contract contract = userProfile.getCurrentContract();
		List<GenericException> errors = new ArrayList<GenericException>();
		
		if (reportType != null && !reportType.isEmpty()) {
			periodEnd = request.getParameter("periodEnd");
			inputPeriodEndDate = parseDate(periodEnd,
					formatPeriodEndingYear_YYYYMMDD);
			/*
			 * If input Period End date and actual period end Date varies then
			 * show the contract statements page
			 */
			if (!determinePlanYear(contract).contains(inputPeriodEndDate)) {
				return forwards.get(CONTRACT_STATEMENTS_PAGE);
			}
			String periodEndDateMessage = DateRender.format(inputPeriodEndDate,
					RenderConstants.MEDIUM_MDY_SLASHED);
		
		try {
			return doDownload(request,   actionform,response, contract);
		} catch (SystemException e) {
			errors.add(new GenericException(
					ErrorCodes.REPORT_CSV_FILE_NOT_FOUND, new Object[] {
							e.getMessage(), periodEndDateMessage }));
			if(errors.size() ==1){
				request.getSession(false).setAttribute(CommonConstants.ERROR_KEY, errors);
			}
		}
		}
		return forwards.get(INPUT);

	}
	
	
	/**
	 * 
	 * Method to handle the failure state of the CSV report generation.
	 * 
	 * @param actionform
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/contractStatements/", params="action=csvErrorReport", method =  RequestMethod.GET) 
	public String doCsvErrorReport (@Valid @ModelAttribute("contractStatementsForm") ContractStatementsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        }
	
		HttpSession session = request.getSession(false);
		String reportTypeMessage = null;
		List<GenericException> errors = new ArrayList<GenericException>();
		String periodEndingYear = (String) session.getAttribute("periodEnd");
		String reportType = (String) session.getAttribute("reportType");
		Boolean reportServiceNotAvailable = (Boolean) session.getAttribute("reportServiceNotAvailable");
		if(reportServiceNotAvailable){
			errors.add(new GenericException(
					ErrorCodes.REPORT_SERVICE_UNAVAILABLE_CONTRACT_STATEMENTS));
		}
		String periodEndDateMessage = "";
		Date inputPeriodEndDate = null;
		try{
			 inputPeriodEndDate = parseDate(periodEndingYear,
				formatPeriodEndingYear_YYYYMMDD);
		}catch(SystemException e){
			logger.error("Problem with parsing the period Ending date" +periodEndingYear );
		}
		if(inputPeriodEndDate!=null){
			periodEndDateMessage = DateRender.format(inputPeriodEndDate,
					RenderConstants.MEDIUM_MDY_SLASHED);
		}
		if (StringUtils.equals(reportType, Constants.REPORT_TYPE_CONT_HISTORY)) {
			reportTypeMessage = Constants.CAPTION_CONTRIBUTION_HISTORY;
		} else if (StringUtils.equals(reportType,
				Constants.REPORT_TYPE_OUTSTANDING_LOAN)){
			reportTypeMessage = Constants.CAPTION_OUTSTANDING_LOAN;
		} else{	
			reportTypeMessage = Constants.CAPTION_LOAN_REPAYMENT;
		}
		errors.add(new GenericException(
				ErrorCodes.REPORT_CSV_FILE_NOT_FOUND, new Object[] {
						reportTypeMessage, periodEndDateMessage }));
		setErrorsInSession(request, errors);
		session.removeAttribute("periodEnd");
		session.removeAttribute("reportType");
		return forwards.get(INPUT);
	}
	
	/**
	 *  Build the contract report list for the statement page
	 * @param reportInfos
	 * @return
	 * @throws SystemException
	 */
	public ContractStatements buildContractStatementsLists(
			ReportInfo[] reportInfos) throws SystemException {
		ContractStatements contractStatements = new ContractStatements();
		String fileName = null;
		for (int i = 0; i < reportInfos.length; i++) {

			try {
				if (StringUtils.equals(reportInfos[i].getReportType(),Constants.REPORT_TYPE_CONT_HISTORY)
						|| StringUtils.equals(reportInfos[i].getReportType(),
								Constants.REPORT_TYPE_OUTSTANDING_LOAN)
						|| StringUtils.equals(reportInfos[i].getReportType(),
								Constants.REPORT_TYPE_LOAN_REPAYMENT)) {

					fileName = reportInfos[i].getReportType()
							+ "_"
							+ formatPeriodEndingYear_YYYYMMDD
									.format(reportInfos[i]
											.getReportPeriodEndDate());

				} else {
					fileName = ContractStatementsHelper
							.buildPdfFileName(reportInfos[i]);

				}
			} catch (Exception e) {
				throw new SystemException(e,
						"buildContractStatementsLists Failed to build PDF: Contract Name ["
								+ reportInfos[i].getContractName() + "] in"
								+ this.getClass().getName());
			}
			contractStatements.addStatement(reportInfos[i].getReportType(),
					fileName,
					ContractStatementsHelper.buildLabel(reportInfos[i]));
		}

		return contractStatements;

	}

	// Makes call to Business Delegate to gets the Array of all the
	// ContractStatements for this contract

	public ReportInfo[] getReports(UserProfile userProfile, Contract contract,
			String sitemode) throws SystemException,
			ServiceUnavailableException {

		EReportsServiceDelegate delegate = EReportsServiceDelegate
				.getInstance();
		ReportListResponse reportListResponse = null;
		// prepares request parameters
		ReportListRequest reportListRequest = new ReportListRequest();
		// Allow tpa staff plan access for all the users. Assuming that
		// users who are not allowed to view the statements for this contract
		// have already been restricted from
		// from viewing this page.

		reportListRequest.setStaffPlanAccessAllowed(true);
		reportListRequest.setContractNumber((new Integer(contract
				.getContractNumber())).toString());
		reportListRequest.setClientId(userProfile.getClientId());
		if (sitemode.equals(Constants.SITEMODE_USA)) {
			reportListRequest.setCompanyCode(RequestConstants.COMPANY_CODE_USA);
		} else {
			reportListRequest.setCompanyCode(RequestConstants.COMPANY_CODE_NY);
		}
		reportListRequest.setUserType(CLIENT_USER_TYPE);
		// sort by 1. report type asc, 2. report print date desc
		reportListRequest.setSortFields(new int[] {
				ReportListRequest.SORT_FIELD_REPORT_TYPE,
				ReportListRequest.SORT_FIELD_REPORT_END_DATE,
				ReportListRequest.SORT_FIELD_REPORT_PRINT_DATE });
		reportListRequest.setSortAscendingOrders(new boolean[] { true, false,
				false });

		// logg the call to eReports service
		if (logger.isDebugEnabled())
			logger.debug("calling eReports service with the following param: "
					+ reportListRequest.getClientId() + ";"
					+ reportListRequest.getContractNumber());

		reportListResponse = delegate.getReportList(reportListRequest);
		return reportListResponse.getReportList();
	}

	/**
	 * @method Add Existing reportInfo Array with csv report details
	 * @param reportInfo Array,contract object
	 * @return reportInfo Array.
	 * @throws SystemException
	 */
	private ReportInfo[] addAuditOnCdReports(ReportInfo[] reportInfos,
			Contract contract) throws SystemException {

		List<Date> periodEndingDates = new ArrayList<Date>();
		periodEndingDates = determinePlanYear(contract);
		int contractId = contract.getContractNumber();
		if (periodEndingDates.isEmpty()  ) {
			return reportInfos;
		}
		List<ReportInfo> reports = new ArrayList<ReportInfo>();
		ReportInfo[] csvRepotArray = null;
		String[] sections = null;
		if (!isLoanAllowed(contractId)) {
			sections = new String[1];
			sections[0] = Constants.REPORT_TYPE_CONT_HISTORY;
		} else {
			sections = new String[3];
			sections[0] = Constants.REPORT_TYPE_CONT_HISTORY;
			sections[1] = Constants.REPORT_TYPE_OUTSTANDING_LOAN;
			sections[2] = Constants.REPORT_TYPE_LOAN_REPAYMENT;
		}
		for (int i = 0; i < sections.length; i++) {
			for (Date planYearEnd : periodEndingDates) {
				ReportInfo reportInfo = new ReportInfo();
				reportInfo.setContractNumber(new Integer(contractId).toString());
				reportInfo.setContractName(contract.getContractCarName());
				reportInfo.setReportType(sections[i]);
				reportInfo.setReportPeriodEndDate(planYearEnd);
				reports.add(reportInfo);
			}
		}
		// add it in a new CSV array
		csvRepotArray = (ReportInfo[]) reports.toArray(new ReportInfo[0]);
		// if existing report array is not null then merge existing and new
		// array
		if (reportInfos != null) {
			return concatArray(reportInfos, csvRepotArray);
		} else {
			return csvRepotArray;
		}

	}

	/**
	 * @method merge existing array with new array
	 * @param Existing report info , new report info
	 * @return reportInfo Array.
	 */
	public ReportInfo[] concatArray(ReportInfo[] reportInfo,
			ReportInfo[] csvRepotArray) {

		int reportInfoLength = reportInfo.length;
		int reportInfoNewLength = csvRepotArray.length;
		ReportInfo[] reportInfoAll = new ReportInfo[reportInfoLength
				+ reportInfoNewLength];
		System.arraycopy(reportInfo, 0, reportInfoAll, 0, reportInfoLength);
		// merge existing array to new array
		System.arraycopy(csvRepotArray, 0, reportInfoAll, reportInfoLength,
				reportInfoNewLength);
		return reportInfoAll;
	}

	/**
	 * @method Find out the period ending dates to be populated in drop downs
	 * 
	 * @param contract object
	 *            
	 * @return List of period Ending Dates.
	 * @throws SystemException
	 */

	private List<Date> determinePlanYear(Contract contract)
			throws SystemException {
		List<Date> periodEnd = new ArrayList<Date>();
		Date contractEffectiveDate = parseDate(
				formatDate_MMDDYYYY.format(contract.getEffectiveDate()),
				formatDate_MMDDYYYY);
		String contractStatus = contract.getStatus();
		Date contractStatusEffectiveDate = parseDate(
				formatDate_MMDDYYYY.format(contract
						.getContractStatusEffectiveDate()), formatDate_MMDDYYYY);
		Calendar calendarInsForCurrDate = Calendar.getInstance();
		Date currentDate = calendarInsForCurrDate.getTime();
		int currentYear = calendarInsForCurrDate.get(Calendar.YEAR);
		Calendar calendarInsForcurrentDateMinus24Months = Calendar
				.getInstance();
		calendarInsForcurrentDateMinus24Months.add(Calendar.MONTH, -24);
		String currentDateMinus24Months = formatDate_MMDDYYYY
				.format(calendarInsForcurrentDateMinus24Months.getTime());
		Date currentDateMinus2Years = parseDate(currentDateMinus24Months,
				formatDate_MMDDYYYY);
		int contractId = contract.getContractNumber();
		ContractServiceDelegate delegate = ContractServiceDelegate
				.getInstance();
		Date planYearEndDate = delegate
				.getPlanYearEndForAciContract(contractId);
		String planYearEnd = formatPlanYearEndDate(planYearEndDate);
		Date planYearEndDateCurrent = parseDate(planYearEnd + currentYear,
				formatDate_MMDDYYYY);
		Calendar calendarInsForPlanYearEndDate = Calendar.getInstance();
		calendarInsForPlanYearEndDate.setTime(planYearEndDateCurrent);
		while (currentDateMinus2Years.before(planYearEndDateCurrent)
				&& (contractEffectiveDate.before(planYearEndDateCurrent) 
							||contractEffectiveDate.equals(planYearEndDateCurrent))) {
			if (planYearEndDateCurrent.before(currentDate)) {
				if (StringUtils.equals(contractStatus,
						Contract.STATUS_CONTRACT_DISCONTINUED)) {
					if (!periodEnd.contains(contractStatusEffectiveDate)) {
						periodEnd.add(contractStatusEffectiveDate);
					}
					if (contractStatusEffectiveDate
							.after(planYearEndDateCurrent)) {
						periodEnd.add(planYearEndDateCurrent);
					}
				} else {
					periodEnd.add(planYearEndDateCurrent);

				}
			}
			calendarInsForPlanYearEndDate.add(Calendar.YEAR, -1);
			planYearEndDateCurrent = calendarInsForPlanYearEndDate.getTime();

		}
		return periodEnd;
	}	
		
	/**
	 * @Method convert string type of periodEndDate to Date type and format
	 * 
	 * @param string periodEnd Date,FastDate formatter
	 *            
	 * @return formatted Date of periodEnd Date.
	 * @throws SystemException
	 */
	private Date parseDate(String periodEndDateStr, FastDateFormat formatter)
			throws SystemException {
		Date periodEndingYear;
		try {
			periodEndingYear = formatter.parse(periodEndDateStr);
		} catch (ParseException e) {
			String message = "parseDate() Failed to parse Date:"
					+ this.getClass().getName();
			logger.error(message);
			throw new SystemException(e, message);
		}
		return periodEndingYear;
	}

	/**
	 * Download the CSV file
	 * 
	 * @param request ,response,contract object
	 *            
	 * @return null
	 * @throws SystemException
	 * @throws IOException 
	 */
	private String doDownload (HttpServletRequest request,ContractStatementsForm form, HttpServletResponse response, Contract contract) 
	throws IOException,ServletException, SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doDownload");
		}
		byte[] downloadData = null;
		ContractStatementsForm contractStatementsForm = (ContractStatementsForm)form;
		HttpSession session = request.getSession(false);
		String periodEndingYear = request.getParameter("periodEnd");
		String reportType = request.getParameter("reportType");
		int contractId = contract.getContractNumber();
		
		try{
			downloadData = getDownloadData(periodEndingYear,reportType,contractId,contract);
		}catch(SystemException e){
			logger.error("Failed to generate the csv report for the contract:"+contractId+ "and the report type :"+reportType);
		}
		session.setAttribute("periodEnd", periodEndingYear);
		session.setAttribute("reportType", reportType);
		if(downloadData == null){
			response.getWriter().write(CSV_NOT_GENERATED);
		}else{
			contractStatementsForm.setPeriodEnd(periodEndingYear);
			contractStatementsForm.setReportType(reportType);
			response.getWriter().write(CSV_GENERATED);
			session.setAttribute(GENERATED_CSV_REPORT, new Handle(downloadData,reportType,periodEndingYear));
		}


		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDownload");
		}
		return null;
	}

	/**
	 * @Method Get the data to be downloaded
	 * 
	 * @param periodEndingYear,report type,contract id,contract object
	 *            
	 * @return byte Array
	 * @throws SystemException
	 */
	private byte[] getDownloadData(String periodEndingYear, String reportType,
			int contractId, Contract contract) throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> populateDownloadData");

		StringBuffer buffer = new StringBuffer();
		Date inputPeriodEndDate = parseDate(periodEndingYear,
				formatPeriodEndingYear_YYYYMMDD);
		inputPeriodEndDate = parseDate(
				formatDate_MMDDYYYY.format(inputPeriodEndDate),
				formatDate_MMDDYYYY);
		ContractServiceDelegate delegate = ContractServiceDelegate
				.getInstance();
		Date contractEffectiveDate = contract.getEffectiveDate();
		contractEffectiveDate = parseDate(DateRender.format(
				contractEffectiveDate, RenderConstants.MEDIUM_MDY_SLASHED),
				formatDate_MMDDYYYY);
		Date reportFromDate = determineReportFromDate(contract, delegate,inputPeriodEndDate, contractEffectiveDate,contractId);
		if (StringUtils.equals(reportType, Constants.REPORT_TYPE_CONT_HISTORY)) {
			buildCsvContributionHistoryReport(buffer, delegate, contractId,
					inputPeriodEndDate,reportFromDate);
		}
		else if (StringUtils.equals(reportType,
				Constants.REPORT_TYPE_OUTSTANDING_LOAN)) {
			buildCsvOutstandingLoanReport(buffer, delegate, contractId,
					inputPeriodEndDate);
		} else {
			
			buildCsvLoanRepaymentReport(buffer, delegate, contractId,
					inputPeriodEndDate, contractEffectiveDate, reportFromDate);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- populateDownloadData");
		return buffer.toString().getBytes();
	}

	/**
	 * @Method Get the contentType of CSV
	 *            
	 * @return content type
	 * 
	 */
	private String getContentType() {
		return BaseReportController.CSV_TEXT;
	}

	/**
	 * @method build the data for the CSV file Outstanding Loan report
	 * 
	 * @param buffer,contract service delegate object,contract id,period End Date
	 *           
	 * @throws SystemException
	 */
	private void buildCsvOutstandingLoanReport(StringBuffer buffer,
			ContractServiceDelegate delegate, int contractId,
			Date inputPeriodEndDate) throws SystemException {

		String reportTypeMessage = null;
		Date currentDate = Calendar.getInstance().getTime();
		BigDecimal totalOutstandingAmount = null;
		buffer.append(CSV_COLUMN_HEADING_OUTSTANDING_LOAN);
		buffer.append(Constants.LINE_BREAK);
		List<OutstandingLoanReportItem> listOfRecords = new ArrayList<OutstandingLoanReportItem>();
		try {
			// get the values from contract service
			listOfRecords = delegate.retrieveOutstandingLoanStatements(
					contractId, inputPeriodEndDate);
		} catch (Exception e) {
			reportTypeMessage = Constants.CAPTION_OUTSTANDING_LOAN;
			String message = "Error in retrieving " + reportTypeMessage;
			logger.error(message, e);
			throw new SystemException(e, reportTypeMessage);
		}
		if (listOfRecords.isEmpty()) {
			String CSV_NO_DATA_FOUND = "There are no outstanding loans for contract "+ contractId +" " +
					"and period end date "+ DateRender.format(inputPeriodEndDate,
							RenderConstants.MEDIUM_MDY_SLASHED);
			buffer.append(CSV_NO_DATA_FOUND);
		} else {
			for (OutstandingLoanReportItem olItem : listOfRecords) {
				buffer.append(contractId).append(Constants.COMMA);
				buffer.append(
						DateRender.format(inputPeriodEndDate,
								RenderConstants.MEDIUM_MDY_SLASHED)).append(
						Constants.COMMA);
				buffer.append(
						DateRender.format(currentDate,
								RenderConstants.MEDIUM_MDY_SLASHED)).append(
						Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv(olItem
								.getParticipantLastName())).append(
						Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv(olItem
								.getParticipantFirstName())).append(
						Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv(olItem
								.getParticipantDivision())).append(
						Constants.COMMA);
				buffer.append(olItem.getLoanNumber()).append(Constants.COMMA);
				buffer.append(
						DateRender.format(olItem.getLoanIssueEffectiveDate(),
								RenderConstants.MEDIUM_MDY_SLASHED)).append(
						Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv((NumberRender.formatByPattern(
								olItem.getLoanIssueAmount(),
								ZERO_AMOUNT_STRING,
								Constants.AMOUNT_FORMAT_TWO_DECIMALS_WITH_DOLLAR))))
						.append(Constants.COMMA);
				buffer.append(
						NumberRender.formatByPattern(
								olItem.getLoanInterestRate(),
								ZERO_AMOUNT_STRING, INTEREST_FORMAT)).append(
						Constants.COMMA);

				buffer.append(
						DateRender.format(olItem.getMaturityDate(),
								RenderConstants.MEDIUM_MDY_SLASHED)).append(
						Constants.COMMA);
				buffer.append(
						DateRender.format(olItem.getBalanceAsOf(),
								RenderConstants.MEDIUM_MDY_SLASHED)).append(
						Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv((NumberRender.formatByPattern(
								totalOutstandingAmount = olItem
										.getLoanPrincipleAmount().add(
												olItem.getLoanInterestAmount()),
								ZERO_AMOUNT_STRING,
								Constants.AMOUNT_FORMAT_TWO_DECIMALS_WITH_DOLLAR))))
						.append(Constants.COMMA);
				buffer.append(Constants.LINE_BREAK);
			}
		}
	}

	/**
	 * build the data for the CSV file of Loan re-payment report
	 * 
	 * @param buffer,contract service delegate object,contract id,
	 *            period end date,contract Effective Date
	 * @throws SystemException
	 */
	private void buildCsvLoanRepaymentReport(StringBuffer buffer,
			ContractServiceDelegate delegate, int contractId,
			Date inputPeriodEndDate, Date contractEffectiveDate,Date reportFromDate)
			throws SystemException {
		String reportTypeMessage = null;
		Date currentDate = Calendar.getInstance().getTime();
		buffer.append(CSV_COLUMN_HEADING_LOAN_REPAYMENTS);
		buffer.append(Constants.LINE_BREAK);
		List<LoanRepaymentReportItem> listOfRecords = new ArrayList<LoanRepaymentReportItem>();
		try {
			listOfRecords = delegate.retrieveLoanRepaymentStatements(
					contractId, inputPeriodEndDate, reportFromDate);
		} catch (Exception e) {
			reportTypeMessage = Constants.CAPTION_LOAN_REPAYMENT;
			String message = "Error in retrieving " + reportTypeMessage;
			logger.error(message, e);
			throw new SystemException(e, reportTypeMessage);
		}
		if (listOfRecords.isEmpty()) {
			String CSV_NO_DATA_FOUND = "There are no loan repayments for contract "+ contractId +" " +
					"and period end date "+ DateRender.format(inputPeriodEndDate,
							RenderConstants.MEDIUM_MDY_SLASHED);
			buffer.append(CSV_NO_DATA_FOUND);
		} else {
			for (LoanRepaymentReportItem lrItem : listOfRecords) {
				buffer.append(contractId).append(Constants.COMMA);
				buffer.append(
						DateRender.format(lrItem.getReportFromDate(),
								RenderConstants.MEDIUM_MDY_SLASHED)).append(
						Constants.COMMA);
				buffer.append(
						DateRender.format(inputPeriodEndDate,
								RenderConstants.MEDIUM_MDY_SLASHED)).append(
						Constants.COMMA);
				buffer.append(
						DateRender.format(currentDate,
								RenderConstants.MEDIUM_MDY_SLASHED)).append(
						Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv(lrItem
								.getParticipantLastName())).append(
						Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv(lrItem
								.getParticipantFirstName())).append(
						Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv(lrItem
								.getParticipantDivision())).append(
						Constants.COMMA);
				buffer.append(lrItem.getLoanNumber()).append(Constants.COMMA);
				buffer.append(
						DateRender.format(
								lrItem.getIntereseDateEffectiveDate(),
								RenderConstants.MEDIUM_MDY_SLASHED)).append(
						Constants.COMMA);
				buffer.append(lrItem.getTransactionNumber()).append(
						Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv(NumberRender
								.formatByPattern(
										lrItem.getLoanRepaymentAmount(),
										ZERO_AMOUNT_STRING,
										Constants.AMOUNT_FORMAT_TWO_DECIMALS_WITH_DOLLAR)))
						.append(Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv(NumberRender
								.formatByPattern(lrItem.getLoanPriciple(),
										ZERO_AMOUNT_STRING,
										Constants.AMOUNT_FORMAT_TWO_DECIMALS)))
						.append(Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv(NumberRender
								.formatByPattern(lrItem.getLoanInterest(),
										ZERO_AMOUNT_STRING,
										Constants.AMOUNT_FORMAT_TWO_DECIMALS)))
						.append(Constants.COMMA);

				buffer.append(Constants.LINE_BREAK);
			}
		}
	}

	/**
	 * @method Check whether the contract allows loan or not
	 * 
	 * @param contract number
	 *            
	 * @return boolean true or false
	 * 
	 */
	private boolean isLoanAllowed(int contractId) {
		final PlanDataLite planData = getPlanDataLite(contractId);
		if (StringUtils.equals(ServiceFeatureConstants.YES,
				planData.getLoansAllowedInd())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @Method to calculate start date for loan repayments report from peridEndingDate
	 * @param peridEndingDate 
	 * @return fromDate
	 * @throws SystemException
	 */
	private Date calculateReportFromDate(Date periodEndingDate,Date contractEffectiveDate)
			throws SystemException {
		Date fromDate;
		Calendar calendarInsForPlanYearEndDate = Calendar.getInstance();
		calendarInsForPlanYearEndDate.setTime(periodEndingDate);
		calendarInsForPlanYearEndDate.add(Calendar.YEAR, -1);
		periodEndingDate = calendarInsForPlanYearEndDate.getTime();
		calendarInsForPlanYearEndDate.add(Calendar.DATE,1);
		fromDate = calendarInsForPlanYearEndDate.getTime();
		if (contractEffectiveDate.after(fromDate)){
			fromDate = contractEffectiveDate;
		}
		return fromDate;
	}


	
	/**
	 * @Method to remove the year of planyearEnddate
	 * @param planYearEndDate 
	 * @return string planYearEnd
	 */
	private String formatPlanYearEndDate(Date planYearEndDate){
		String  planYearEnd = DateRender.format(planYearEndDate,RenderConstants.MEDIUM_MDY_SLASHED).substring(0,6);
		return planYearEnd;
	}
	/**
	 * Retrieves the lite version of Plan data.
	 *
	 * @param contractId
	 * @return object of PlanDataLite
	 */
	protected PlanDataLite getPlanDataLite(int contractId) {

		PlanDataLite planDataLite = null;
		try {
			planDataLite = ContractServiceDelegate.getInstance()
					.getPlanDataLight(contractId);
		} catch (SystemException e) {
			throw new NestableRuntimeException(e);
		}
		return planDataLite;

	}
	
	private Date determineReportFromDate(Contract contract,ContractServiceDelegate delegate,Date inputPeriodEndDate,Date contractEffectiveDate ,int contractId) throws SystemException{
		
		Date reportFromDate = null;
		if (StringUtils.equals(contract.getStatus(),
				Contract.STATUS_CONTRACT_DISCONTINUED)) {
			Date contractStatusEffectiveDate = contract
					.getContractStatusEffectiveDate();
			contractStatusEffectiveDate = parseDate(
					formatDate_MMDDYYYY.format(contractStatusEffectiveDate),
					formatDate_MMDDYYYY);
			if (contractStatusEffectiveDate.equals(inputPeriodEndDate)) {
				Date planYearEndDate = delegate
						.getPlanYearEndForAciContract(contractId);
				String planYearEnd = formatPlanYearEndDate(planYearEndDate);
				String terminatedYear = formatYear
						.format(contractStatusEffectiveDate);
				Date planYearEndDateOnTerminatedYear = parseDate(
						planYearEnd + terminatedYear, formatDate_MMDDYYYY);
				reportFromDate = calculateReportFromDate(
						planYearEndDateOnTerminatedYear,
						contractEffectiveDate);
			} else {
				reportFromDate = calculateReportFromDate(inputPeriodEndDate,
						contractEffectiveDate);
			}

		} else {
			reportFromDate = calculateReportFromDate(inputPeriodEndDate,
					contractEffectiveDate);
		}
		return reportFromDate;
	}
	
	/**
	 * build the data for the CSV file of Contribution History report
	 * 
	 * @param buffer,contract service delegate object,contract id,period End Date
	 *            
	 * @throws SystemException
	 */

	private void buildCsvContributionHistoryReport(StringBuffer buffer,
			ContractServiceDelegate delegate, int contractId,
			Date inputPeriodEndDate, Date reportFromDate) throws SystemException {
		StopWatch stop = new StopWatch();
		stop.start();
		String reportTypeMessage = null;
		Date currentDate = Calendar.getInstance().getTime();
		buffer.append(CSV_COLUMN_HEADING_CONTRIBUTION_HISTORY);
		buffer.append(Constants.LINE_BREAK);
		List<ContributionHistoryReportItem> listOfRecords = new ArrayList<ContributionHistoryReportItem>();
		try {
			listOfRecords = delegate.retrieveContributionHisoryStatements(
					contractId, inputPeriodEndDate,reportFromDate);
		} catch (Exception e) {
			reportTypeMessage = Constants.CAPTION_CONTRIBUTION_HISTORY;
			String message = "Error in retrieving " + reportTypeMessage;
			logger.error(message, e);
			throw new SystemException(e, reportTypeMessage);

		}
		
		StopWatch stop1 = new StopWatch();
		stop1.start();
		if (listOfRecords.isEmpty()) {
			String CSV_NO_DATA_FOUND = "There is no contribution history for contract "+ contractId +" " +
					"and period end date "+ DateRender.format(inputPeriodEndDate,
							RenderConstants.MEDIUM_MDY_SLASHED);
			buffer.append(CSV_NO_DATA_FOUND);
		} else {
			for (ContributionHistoryReportItem chItem : listOfRecords) {
				buffer.append(contractId).append(Constants.COMMA);
				buffer.append(
						DateRender.format(chItem.getReportFromDate(),
								RenderConstants.MEDIUM_MDY_SLASHED)).append(
						Constants.COMMA);
				buffer.append(
						DateRender.format(inputPeriodEndDate,
								RenderConstants.MEDIUM_MDY_SLASHED)).append(
						Constants.COMMA);
				buffer.append(
						DateRender.format(currentDate,
								RenderConstants.MEDIUM_MDY_SLASHED)).append(
						Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv(chItem
								.getParticipantLastName())).append(
						Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv(chItem
								.getParticipantFirstName())).append(
						Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv(chItem
								.getParticipantDivision())).append(
						Constants.COMMA);
				buffer.append(chItem.getParticipantStatus()).append(
						Constants.COMMA);
				buffer.append(chItem.getTransactionNumber()).append(
						Constants.COMMA);
				buffer.append(
						DateRender.format(chItem.getTransactionProcessDate(),
								RenderConstants.MEDIUM_MDY_SLASHED)).append(
						Constants.COMMA);
				buffer.append(
						DateRender.format(chItem.getTransactionEffectiveDate(),
								RenderConstants.MEDIUM_MDY_SLASHED)).append(
						Constants.COMMA);
				buffer.append(
						DateRender.format(chItem.getPayrollDate(),
								RenderConstants.MEDIUM_MDY_SLASHED)).append(
						Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv(chItem
								.getTransactionMoneyType())).append(
						Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv(chItem
								.getTransactionApolloMoneyType())).append(
						Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv(chItem
								.getTransClientDefinedMoneyType()))
						.append(Constants.COMMA);
				buffer.append(
						StringEscapeUtils.escapeCsv(NumberRender
								.formatByPattern(chItem.getTransactionAmount(),
										ZERO_AMOUNT_STRING,
										Constants.AMOUNT_FORMAT_TWO_DECIMALS)))
						.append(Constants.COMMA);
				buffer.append(Constants.LINE_BREAK);
			}
		}
	
		stop1.stop();
		 //TODO Sysout will be removed once the processing time get normalized
		logger.error("Execution time of Contribution history CSV creation :"+stop1.toString());
		
		stop.stop();
		logger.error("Total execution time of Contribution history :"+stop.toString() +" for the contract id: "+contractId);
	}
	
	/**
	 * Method to get the CSV report based on the information provided
	 * 
	 * @param mapping
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	
	@RequestMapping(value ="/contractStatements/", params="action=csvDownload"  , method = RequestMethod.GET) 
	public String doCsvDownload (@Valid @ModelAttribute("contractStatementsForm") ContractStatementsForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(INPUT);//if input forward not //available, provided default
        	}
        }
	
	
		
		HttpSession session = request.getSession(false);
		ContractStatementsForm contractStatementsForm = (ContractStatementsForm)form;
		String reportFileName = null;
		String periodEndingYear = (String) session.getAttribute("periodEnd");
		String reportType = (String) session.getAttribute("reportType");
		
		UserProfile userProfile = getUserProfile(request);
		Contract contract = userProfile.getCurrentContract();
		int contractId = contract.getContractNumber();
		byte [] downloadData = null;
		if(session.getAttribute(GENERATED_CSV_REPORT) != null) {
			downloadData = ((Handle)session.getAttribute(GENERATED_CSV_REPORT)).getByteArray();
			periodEndingYear = ((Handle)session.getAttribute(GENERATED_CSV_REPORT)).getPeriodEndingYear();
					reportType = ((Handle)session.getAttribute(GENERATED_CSV_REPORT)).getReportType();
			session.setAttribute(GENERATED_CSV_REPORT, null);
			
		}else{
			downloadData = getDownloadData(periodEndingYear, reportType, contractId, contract);
		}
		// build a file name of CSV report
		reportFileName = ContractStatementsHelper.buildCsvFileName(reportType,
				contractId, periodEndingYear);
					
		if (downloadData != null && downloadData.length > 0) {
			BaseReportController.streamDownloadData(request, response,
					getContentType(), reportFileName, downloadData);
		}	
		 
		return null;
	}
	
	/**
	 * Class to handle the byte array request object
	 * 
	 * @author krishta
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
		Handle(byte[] array,String reportType,String periodEndingYear){
			this.byteArray = array;
			this.reportType = reportType;
			this.periodEndingYear = periodEndingYear;
		}
		private byte[] byteArray;
		
		private String reportType;
		private String periodEndingYear;
		/**
		 * @return the array
		 */
		public byte[] getByteArray() {
			return byteArray;
		}
		public String getReportType() {
			return reportType;
		}
		public String getPeriodEndingYear() {
			return periodEndingYear;
		}
		
	}

	/** This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#137697.
	 */
	 @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}

}