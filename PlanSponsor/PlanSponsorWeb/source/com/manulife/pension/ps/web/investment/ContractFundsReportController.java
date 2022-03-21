package com.manulife.pension.ps.web.investment;


import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Footnote;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.cache.FootnoteCacheImpl;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.investment.sort.FundsByBigDecimalColumnSortorderTool;
import com.manulife.pension.ps.web.investment.sort.FundsByStringColumnSortorderTool;
import com.manulife.pension.ps.web.investment.util.SortTool;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.account.valueobject.CustomerServicePrincipal;
import com.manulife.pension.service.account.valueobject.InvestmentReport;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.SvgifFund;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * Action class for the Contract Funds Report.  It gets the data from the view funds database,
 * sorts the columns as required, and obtains the footnotes required that will be displayed on the
 * jsp page
 *
 *
 * @author   Chris Shin
 * @version  CS1.0  (March 1, 2004)
 **/

@Controller
@RequestMapping( value = "/investment")
@SessionAttributes("contractFundsForm")

public class ContractFundsReportController extends PsController {

	
	@ModelAttribute("contractFundsForm") 
	public  ContractFundsForm populateForm() 
	{
		return new  ContractFundsForm();
		}
	
	public static Map<String,String> forwards = new HashMap<>();
	static {
		forwards.put("input", "/investment/contractFundsReport.jsp");
		forwards.put("contractFunds", "/investment/contractFundsReport.jsp");
		}

	
	
	private static final String LINE_BREAK = ReportController.LINE_BREAK;
	private static final String COMMA = ReportController.COMMA;
	private static final String DOWNLOAD = "download";
	private static final String TASK = "task";
	private static final String REPORT_ID = "ContractFundsReport";
	private static final String FORMAT_YEAR_YYYY = "yyyy";
	private static final String FORMAT_MONTH_MM = "MM";
	private static final String MONTHLY_KEY = "monthly";

	public ContractFundsReportController()
	{
		super(ContractFundsReportController.class);
	}

	@RequestMapping(value ="/contractFundsReport/", method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("contractFundsForm") ContractFundsForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,SystemException, ContentException, ParseException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get("input");
        	}
        }  
	
	
	
	String forward = null;

		HttpSession session = request.getSession(false);
		Contract currentContract = getUserProfile(request).getCurrentContract();

		if ( currentContract.isContractAllocated() ){
		forward = forwards.get("contractFunds");
		}
		/* TODO
		 * 
		 * else {
			forward = mapping.findForward("contractFundsUnallocated");
		}*/

		//ContractFundsForm contractFundsForm = (ContractFundsForm) form;

		//default selectedView to return back selected funds
		if (actionForm.getSelectedView() == null) {
			if (session.getAttribute(Constants.VIEW_INVESTMENT) != null) {
				actionForm.setSelectedView((String)session.getAttribute(Constants.VIEW_INVESTMENT));
			} else {
				actionForm.setSelectedView(ContractFundsForm.VIEW_SELECTED);
				session.setAttribute(Constants.VIEW_INVESTMENT, ContractFundsForm.VIEW_SELECTED);
			}
		}

		if(session.getAttribute(MONTHLY_KEY) != null){
			if(request.getParameter(MONTHLY_KEY) != null){
				session.setAttribute(MONTHLY_KEY, request.getParameter(MONTHLY_KEY));
			} 
		}else {
			session.setAttribute(MONTHLY_KEY, "false");
		}
		
		actionForm.setAsOfDate(currentContract.getContractDates().getAsOfDate());

		InvestmentReportPresentationValue report = createFundList(request, actionForm.getSelectedView());

		//set the form property so that it will display correctly
		if (report != null) {
			actionForm.setSelectedViewBy(String.valueOf(report.getGroupingScheme()));
			report.setFeeWaiverFunds(FundServiceDelegate.getInstance().getFundFeeWaiverIndicator());
			report.setRestrictedFunds(FundServiceDelegate.getInstance().getRestrictedFunds());
		}

		//check to see whether a download to a csv is required
		String task = request.getParameter(TASK);
		if (task != null && DOWNLOAD.equals(task)) {
			String reportFileName = REPORT_ID + ReportController.CSV_EXTENSION;

			byte[] downloadData = getDownloadData(report, request, response,
				getFormattedDate(actionForm.getAsOfDate()),
				actionForm.getSelectedView());
			if (downloadData != null) {
				ReportController.streamDownloadData(request, response, ReportController.CSV_TEXT,
						reportFileName, downloadData);
			}
			// do not iterate with Struts
			return null;
		}

		return forward;
    }

    private String getFormattedDate(Date dateValue) {

    	return DateRender.formatByPattern(dateValue.toString(), " ",RenderConstants.MEDIUM_MDY_SLASHED);
    }
	/**
     *
     * Retrieves the presentation value object for funds information from the ezk service
     *
	 * @param request
	 * 		the HttpServletRequest.
	 *
	 * @param viewOnForm
	 * 		String containing VIEW_AVAILABLE or VIEW_SELECTED
     *
     * @return
     * 		InvestmentReportPresentationValue object
     *
     * @exception SystemException
     * 		cannot get the InvestmentReportPresentationValue object due to a problem from the service call
     *
     */
	public InvestmentReportPresentationValue getFunds(HttpServletRequest request, String viewOnForm) throws SystemException{

		InvestmentReportPresentationValue report = null;
		HttpSession session = request.getSession(false);

		Object obj = session.getAttribute(Constants.WEB_INVESTMENT_REPORT);

		if (obj != null) {
			report = (InvestmentReportPresentationValue) obj;
		}

		boolean isShowSelectedFundsFromForm = false;

		if (ContractFundsForm.VIEW_SELECTED.equals(viewOnForm)) {
			isShowSelectedFundsFromForm = true;
		}

		boolean prevSortByTypeSet = false;
		String prevAssetRiskOrderBySet = null;

		UserProfile profile = getUserProfile(request);
		// gets	the value object from service
		if (obj == null||
			(obj!= null && !viewOnForm.equals(session.getAttribute(Constants.VIEW_INVESTMENT))) ||
			(obj!= null && profile.getCurrentContract().getContractNumber() != Integer.parseInt(report.getContractNumber())) ) {

			if (obj!= null && !viewOnForm.equals(session.getAttribute(Constants.VIEW_INVESTMENT)) ||
			   (obj!= null && profile.getCurrentContract().getContractNumber() != Integer.parseInt(report.getContractNumber())) ) {
				//remove these objects from the session if the view investment options or contract has changed
				session.removeAttribute(Constants.INVESTMENT_BY_ASSET);
				session.removeAttribute(Constants.INVESTMENT_BY_RISK_CATEGORY);
				session.setAttribute(Constants.VIEW_INVESTMENT, viewOnForm);
				prevSortByTypeSet=true;
				prevAssetRiskOrderBySet = Integer.toString(report.getAssetRiskOrderBy());
			}

			//presentation value object
			int contractNumber = 0;
			String clientId = null;

			contractNumber = profile.getCurrentContract().getContractNumber();
			clientId  = profile.getClientId();
			report = this.getFundsFromService(getCustomerServicePrincipal(clientId), clientId, Integer.toString(contractNumber),InvestmentReport.GROUPING_SCHEME_BY_RISK_CATEGORY, session,isShowSelectedFundsFromForm);

			if (report == null)
				return null;

			session.setAttribute(Constants.WEB_INVESTMENT_REPORT, report);
		}

		//is sorting required?
		if (request.getParameter("sortByType") != null || prevSortByTypeSet) {
			report = this.sortFunds(request, (InvestmentReportPresentationValue) session.getAttribute(Constants.WEB_INVESTMENT_REPORT),isShowSelectedFundsFromForm, prevAssetRiskOrderBySet);
			session.setAttribute(Constants.WEB_INVESTMENT_REPORT, report);
		}

    	return report;
	}

	/**
     *
     * Calls to the ezk service and places the resulting value object into the session
     *
     * @param principal
     * 		the CustomerServicePrincipal for the Plan Sponsor
     *
     * @param clientID
     * 		the clientID
     *
     * @param contractNumber
     * 		the contractNumber
     *
     * @param orderBy
     * 		InvestmentReport.GROUPING_SCHEME_BY_ASSET_CLASS or InvestmentReport.GROUPING_SCHEME_BY_RISK_CATEGORY
     *
	 * @param session
	 * 		the HttpSession.
	 *
	 * @param viewAvailable
	 * 		boolean containing the directive for the service to bring back all available investment options
	 *
	 * @param viewOnForm
	 * 		String containing VIEW_AVAILABLE or VIEW_SELECTED
     *
     * @return
     * 		InvestmentReportPresentationValue object
     *
     * @exception SystemException
     * 		cannot get the InvestmentReportPresentationValue object due to a problem from the service call
     *
     */
	private InvestmentReportPresentationValue getFundsFromService(CustomerServicePrincipal principal, String clientID,
				String contractNumber, int orderBy, HttpSession session, boolean viewSelected) throws SystemException{

		InvestmentReport valobj = null;
		//gets from session
		if ((orderBy == InvestmentReport.GROUPING_SCHEME_BY_ASSET_CLASS) && (session.getAttribute(Constants.INVESTMENT_BY_ASSET) != null)){
			valobj = (InvestmentReport)session.getAttribute(Constants.INVESTMENT_BY_ASSET);
		}

		if ((orderBy == InvestmentReport.GROUPING_SCHEME_BY_RISK_CATEGORY) && (session.getAttribute(Constants.INVESTMENT_BY_RISK_CATEGORY) != null)){
			valobj =  (InvestmentReport)session.getAttribute(Constants.INVESTMENT_BY_RISK_CATEGORY);
		}

		if (valobj == null) {
			//gets from service
			try {
				valobj = (getAccountService() == null)? null : getAccountService().getFunds(principal, null, contractNumber,orderBy, viewSelected);

				if (valobj == null)
					return null;

				if (orderBy == InvestmentReport.GROUPING_SCHEME_BY_ASSET_CLASS)
					session.setAttribute(Constants.INVESTMENT_BY_ASSET, valobj);
				else
					session.setAttribute(Constants.INVESTMENT_BY_RISK_CATEGORY, valobj);
			} catch (Exception e) {
				SystemException se = new SystemException(e, this.getClass().getName(),
					"getFundsFromService", "Exception occurred calling service.  Contract= " + contractNumber);
				throw se;
		   	}
		}

		InvestmentReportPresentationValue report = new InvestmentReportPresentationValue(valobj);

		return report;
	}

	/**
     *
     * Sort the contents of the InvestmentReportPresentationValue object.
     *
	 * @param request
	 * 		the HttpServletRequest.
	 *
	 * @param report
	 * 		the InvestmentReportPresentationValue
	 *
	 * @param isShowAvailableFundsFromForm
	 * 		boolean containing the directive for the service to bring back all available investment options
	 *
	 * @param prevAssetRiskOrderBy
	 * 		String containing 0 (view by risk category) or 1 (view by asset class)
     *
     * @return
     * 		InvestmentReportPresentationValue object
     *
     * @exception SystemException
     * 		cannot get the InvestmentReportPresentationValue object due to a problem from the service call
     *
     */
	private InvestmentReportPresentationValue sortFunds(HttpServletRequest request,
			InvestmentReportPresentationValue report,boolean isShowSelectedFundsFromForm,
			String prevAssetRiskOrderBy) throws SystemException{

		int columnNumber = 0;
		int assetRiskOrderBy = 1;
		int sortByType;

		HttpSession session = request.getSession(false);
		UserProfile profile = getUserProfile(request);
		int contractNumber = profile.getCurrentContract().getContractNumber();
		String clientId  = profile.getClientId();

		if (prevAssetRiskOrderBy == null) {
			sortByType = Integer.parseInt(request.getParameter("sortByType").toString());

			if (sortByType == 0){
				assetRiskOrderBy = Integer.parseInt(request.getParameter("assetRiskOrderBy").toString());
			}

			if (sortByType == 1){
				columnNumber = Integer.parseInt(request.getParameter("columnNumber").toString());
			}

		} else {

			sortByType=0;
			assetRiskOrderBy = Integer.parseInt(prevAssetRiskOrderBy);
		}

		if (sortByType == 0){
			if ((assetRiskOrderBy == 0) && (report.getGroupingScheme() != InvestmentReport.GROUPING_SCHEME_BY_RISK_CATEGORY)){
				report = this.getFundsFromService(getCustomerServicePrincipal(clientId), clientId, Integer.toString(contractNumber),InvestmentReport.GROUPING_SCHEME_BY_RISK_CATEGORY,session,isShowSelectedFundsFromForm);
			}
			if ((assetRiskOrderBy == 1) && (report.getGroupingScheme() != InvestmentReport.GROUPING_SCHEME_BY_ASSET_CLASS)) {
				report = this.getFundsFromService(getCustomerServicePrincipal(clientId), clientId, Integer.toString(contractNumber),InvestmentReport.GROUPING_SCHEME_BY_ASSET_CLASS, session, isShowSelectedFundsFromForm);
			}
			report.setAssetRiskOrderBy(assetRiskOrderBy);
		}

		if (Integer.parseInt(report.getContractNumber()) != contractNumber){
			//request for a different contract number
			report = this.getFundsFromService(getCustomerServicePrincipal(clientId), clientId, Integer.toString(contractNumber),InvestmentReport.GROUPING_SCHEME_BY_ASSET_CLASS, session, isShowSelectedFundsFromForm);
		}

		if (sortByType == 1){
			boolean sortType = InvestmentReportPresentationValue.SORT_DESCENDING;

			SortTool sortTool = getSortTool(columnNumber);

			// have we sorted on this column before?
			if (report.getColumnNumber() == columnNumber) {
				if (report.getSortType() == InvestmentReportPresentationValue.SORT_DESCENDING) {
					sortType = InvestmentReportPresentationValue.SORT_ASCENDING;
				} else {
					sortType = InvestmentReportPresentationValue.SORT_DESCENDING;
				}
			} else {
				if (sortTool instanceof FundsByStringColumnSortorderTool)
					sortType = InvestmentReportPresentationValue.SORT_ASCENDING;
				if (sortTool instanceof FundsByBigDecimalColumnSortorderTool)
					sortType = InvestmentReportPresentationValue.SORT_DESCENDING;
			}

			report.sortDataBy(sortTool, sortType);
			report.setColumnNumber(columnNumber);
			report.setSortType(sortType);
		}

		return report;

	}

	private SortTool getSortTool(int column){
	    
	    SortTool sort = new FundsByBigDecimalColumnSortorderTool(column);
	    
		switch (column) {
		    
			case 0:
			case 2:
			case 18:
			    sort = new FundsByStringColumnSortorderTool(column);
			    break;
			    
			case 19:
			case 20:
			case 30:
	            throw new IllegalArgumentException("Field type " + column + " cannot be sorted.");
			    
            case 1:
            case 21:
            case 22:
            case 23:
            case 29:
                throw new IllegalArgumentException("String column " + column + "not available for sorting.");
                
            case 14:
            case 15:
            case 16:
                throw new IllegalArgumentException("BigDecimal column " + column + "not available for sorting.");
                
		}
			        
		return sort;
		
	}

	/**
     *
     * Sort the contents of the InvestmentReportPresentationValue object.
     *
	 * @param request
	 * 		the HttpServletRequest.
	 *
	 * @param view
	 * 		String representing the view investment option selected
     *
     * @return
     * 		InvestmentReportPresentationValue object
     *
     * @exception Exception
     * 		cannot get the InvestmentReportPresentationValue object due to a problem from the service call
     *
     */
	private InvestmentReportPresentationValue createFundList(HttpServletRequest request, String view) throws SystemException {

		HttpSession session = request.getSession(false);
		InvestmentReportPresentationValue report = this.getFunds(request,view);

		String[] footnotes = report.getFootNotes();

		session.setAttribute(Constants.WEB_INVESTMENT_REPORT, report);
		session.setAttribute(Constants.WEB_SYMBOLS_FOOTNOTES, footnotes);

		return report;
	}

	private AccountServiceDelegate getAccountService() {

		return AccountServiceDelegate.getInstance();
	}

	/**
     *
     * Generates the csv file when the "download to csv" link is clicked from the web page.
     *
     * @param report
     * 		the InvestmentReportPresentationValue
     *
	 * @param request
	 * 		the HttpServletRequest.
	 *
	 * @param response
	 * 		the HttpServletResponse.
	 *
	 * @param asOfDate
	 * 		String representing the current date
	 *
 	 * @param selectedView
	 * 		String representing whether the page is displaying ContractFundsForm.VIEW_AVAILABLE
	 * 		or ContractFundsForm.VIEW_SELECTED
     *
     * @return
     * 		void
     *
     * @exception IOException
     * 		could not write to the response
	 * @throws SystemException 
	 * @throws ContentException 
	 * @throws ParseException 
     *
     */
	protected byte[] getDownloadData(InvestmentReportPresentationValue report, HttpServletRequest request,
		HttpServletResponse response, String asOfDate, String selectedView) throws IOException, SystemException, ContentException, ParseException{

		StringBuffer buffer = new StringBuffer();
		Location location = Location.USA;
		Contract currentContract = getUserProfile(request).getCurrentContract();
		String performanceDisclosureText = null;
		String riskDisclaimerText = null;
		String classDisclosure = null;
		String pageDisclaimerText = null;
		String svgifFundId=null;
		String fundCreditRate=null;
		String svgifDisclosureText =null;
		Content svgifDisclosureContent = null;
		boolean isSVGIFFund = false;
		boolean isUBSCoveredFund = false;
		boolean isUBSContract = false;
		Date date = new SimpleDateFormat("yyyy/MM/dd").parse(asOfDate);
		List<SvgifFund> svgifFundList = FundServiceDelegate.getInstance().getSVGIFDefaultFunds();
		List<String> ubsFundList = FundServiceDelegate.getInstance().getServiceProviderCoveredFunds(date, currentContract.getCompanyCode(), Constants.UBS_COFID); //isNewYorkProposal? "094" : "019", "UBS" );
		isUBSContract = currentContract.getDistributionChannel().equals(Constants.UBS_COFID);
		// Retrieve the disclosure Content for SVGIF Funds
		if(svgifFundList != null){
		svgifDisclosureContent = ContentCacheManager
				.getInstance().getContentById(ContentConstants.SVGIF_FUND_DISCLOSURE,ContentTypeManager.instance().FEE_DISCLOSURE, Location.US);
		if(svgifDisclosureContent !=null){
		svgifDisclosureText = ContentUtility.getContentAttribute(svgifDisclosureContent, "text");
		}
		}
		if (GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY.equalsIgnoreCase(currentContract.getCompanyCode())){
			location = Location.NEW_YORK;
		}
		
		try {
			// Retrieve the performance disclosure content
			Content performanceDisclosureContent = ContentCacheManager
					.getInstance().getContentById(
							ContentConstants.PERFORMANCE_DISCLOSURE_TEXT,
							ContentTypeManager.instance().DISCLAIMER, location);
			performanceDisclosureText = ContentUtility.getContentAttribute(
							performanceDisclosureContent, "text");

			// Retrieve the risk disclaimer content
			Content riskDisclaimerContent = ContentCacheManager.getInstance()
					.getContentById(
							ContentConstants.RISK_DISCLOSURES_BY_SITE,
							ContentTypeManager.instance().DISCLAIMER, location);
			riskDisclaimerText = ContentUtility.getContentAttribute(
					riskDisclaimerContent, "text");
		} catch (ContentException exp) {
			throw new SystemException(exp, "Error while retrieving CMA content");
		}	
		
        buffer.append("Contract").append(COMMA).append(
				currentContract.getContractNumber()).append(COMMA).append(
				currentContract.getCompanyName()).append(LINE_BREAK);

        buffer.append("As of,").append(asOfDate).append(LINE_BREAK);
		buffer.append(LINE_BREAK);

		if (performanceDisclosureText != null) {
			buffer.append(ReportController.QUOTE).append(
					ContentUtility
							.filterCMAContentForCSV(performanceDisclosureText))
					.append(ReportController.QUOTE);
			buffer.append(LINE_BREAK);
			buffer.append(LINE_BREAK);
		}
		
		//todo: display shown information

		if (selectedView.equals(ContractFundsForm.VIEW_AVAILABLE)) {
			buffer.append("Shown: All available investment options");
		} else {
			buffer.append("Shown: Selected investment options");
		}
		buffer.append(LINE_BREAK);

		if (report.getGroupingScheme() == 0) {
			buffer.append("View by Risk Category");
		} else {
			buffer.append("View by Asset Class");
		}
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append(COMMA).append(COMMA);
		buffer.append(addQuote("Unit Values as of: " + DateRender.formatByPattern(report.getUnitValueEffectiveDate()," ",RenderConstants.MEDIUM_MDY_SLASHED , RenderConstants.EXTRA_LONG_MDY)));
		buffer.append(COMMA).append(COMMA).append(COMMA);
		buffer.append(addQuote("Returns* (%) as of: " + DateRender.formatByPattern(report.getRateOfReturnEffectiveDate()," ",RenderConstants.MEDIUM_MDY_SLASHED , RenderConstants.EXTRA_LONG_MDY)));
		buffer.append(COMMA).append(COMMA).append(COMMA);
		buffer.append(addQuote("Returns* (%) as of: " + DateRender.formatByPattern(report.getRateOfReturnEffectiveDate()," ",RenderConstants.MEDIUM_MDY_SLASHED , RenderConstants.EXTRA_LONG_MDY) + " Monthly"));
		buffer.append(COMMA).append(COMMA).append(COMMA).append(COMMA).append(COMMA);
		buffer.append(addQuote("Returns* (%) as of: " + DateRender.formatByPattern(calculateQuarterEnd(new Date(report.getRateOfReturnEffectiveDate()))," ",RenderConstants.MEDIUM_MDY_SLASHED , RenderConstants.EXTRA_LONG_MDY) + " Quarterly"));
		buffer.append(COMMA).append(COMMA).append(COMMA).append(COMMA).append(COMMA);
		buffer.append(addQuote("Expense Ratios (ER) as of: " + DateRender.formatByPattern(report.getFerEffectiveDate()," ",RenderConstants.MEDIUM_MDY_SLASHED , RenderConstants.EXTRA_LONG_MDY)));
		buffer.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append("\"\",\"\",\"\",Daily Change");
		buffer.append(LINE_BREAK);
		buffer.append("Investment Option,Manager or Sub-Adviser,Unit Value, ($), (%),1mth ,3mth ,YTD ,1Yr ,3Yr ,5Yr ,10Yr ,Since Inception, 1Yr ,3Yr ,5Yr ,10Yr ,Since Inception, Expense Ratio** (%),Morningstar Category");
		buffer.append(LINE_BREAK);

		ViewFundGroupPresentation[] groupfunds = report.getFundGroups();

		for (int i = 0; i < groupfunds.length; i++){
			String groupName = (groupfunds[i]).getGroupName();

			buffer.append(groupName);
			buffer.append(LINE_BREAK);

			if ((groupfunds[i]).getViewFunds() != null) {

				ViewFundPresentation[] funds = (groupfunds[i]).getViewFunds();

				/**
				 *  For each fund, get related data
				 */

				for (int j = 0; j < funds.length; j++){
					//Supress the non-covered funds (funds that do not have a W on PCIQ screen)
					isUBSCoveredFund = ubsFundList.contains(funds[j].getFundId());
					if(isUBSContract){
						if( !isUBSCoveredFund && !funds[j].getSelectedFlag()){
						continue; 
					}
				}
					String fundName = funds[j].getFundName();
	 				String fundId = funds[j].getFundId();
	 				isSVGIFFund = svgifFundList.stream().filter(o -> o.getFundId().equals(fundId)).findFirst().isPresent();
	 				String fundType = funds[j].getFundType();
					String fundManagerName = funds[j].getFundManagerName();
					String rateType = funds[j].getRateType();		
					BigDecimal unitValue = funds[j].getUnitValue();					
					//$
					BigDecimal dailyVariance = funds[j].getDailyVariance();
					//% 
					BigDecimal dailyReturn = funds[j].getDailyReturn();
					BigDecimal oneMonthReturn = funds[j].getOneMonthReturn();
					BigDecimal threeMonthReturn = funds[j].getThreeMonthReturn();
					BigDecimal yearToDateReturn = funds[j].getYearToDateReturn();
					BigDecimal oneYearReturn = funds[j].getOneYearReturn();
					BigDecimal threeYearReturn = funds[j].getThreeYearReturn();
					BigDecimal fiveYearReturn = funds[j].getFiveYearReturn();
					BigDecimal tenYearReturn = funds[j].getTenYearReturn();
					String morningstarCategory = funds[j].getMorningstarCategory();
					morningstarCategory = StringUtils.isNotEmpty(morningstarCategory) ? WordUtils.capitalize(morningstarCategory.toLowerCase()) : morningstarCategory;
					BigDecimal annualInvestmentCharge = funds[j].getAnnualInvestmentCharge();
					BigDecimal sinceInception = funds[j].getSinceInceptionReturn();
					BigDecimal oneYearQuarterlyReturn = funds[j].getOneYearQuarterlyReturn();
					BigDecimal threeYearQuarterlyReturn = funds[j].getThreeYearQuarterlyReturn();
					BigDecimal fiveYearQuarterlyReturn = funds[j].getFiveYearQuarterlyReturn();
					BigDecimal tenYearQuarterlyReturn = funds[j].getTenYearQuarterlyReturn();
					BigDecimal sinceInceptionQuarterly = funds[j].getSinceInceptionQuarterlyReturn();
					// Setting credit Rating for SVGIF Funds Disclosure
					for (SvgifFund svgFunds:svgifFundList) {
						svgifFundId = svgFunds.getFundId();
						fundCreditRate = svgFunds.getFundCreditingRate();
						if(svgifFundId.equals(fundId) && (svgifDisclosureText != null && svgifDisclosureText.contains("{0}"))) {
							svgifDisclosureText = svgifDisclosureText.replace("{0}", fundCreditRate+"%");
						}
					}
					
					/**
					 * Get array of footnote symbols for fund (only uniques values)
					 */
					buffer.append(addQuote(funds[j].getFundName())).append(COMMA);

					/**
					 * print out each fund's data
					 */

					buffer.append(fundManagerName!=null&&fundManagerName.trim().length()>0?addQuote(fundManagerName):"-").append(COMMA);
					// supressing values for SVGIF Funds Values
					if(isSVGIFFund){
						buffer.append(unitValue == null?unitValue.toString():"-").append(COMMA);
						buffer.append(dailyVariance == null?dailyVariance.toString():"-").append(COMMA);
						buffer.append(dailyReturn == null?dailyReturn.toString():"-").append(COMMA);
						buffer.append(annualInvestmentCharge == null ? annualInvestmentCharge.toString() : "-").append(COMMA);
					}
					buffer.append(unitValue!=null?unitValue.toString():"-").append(COMMA);
					buffer.append(dailyVariance!=null?dailyVariance.toString():"-").append(COMMA);
					buffer.append(dailyReturn!=null?dailyReturn.toString():"-").append(COMMA);
					buffer.append(oneMonthReturn!=null?oneMonthReturn.toString():"-").append(COMMA);
					buffer.append(threeMonthReturn!=null?threeMonthReturn.toString():"-").append(COMMA);
					buffer.append(yearToDateReturn!=null?yearToDateReturn.toString():"-").append(COMMA);					
					buffer.append(oneYearReturn!=null?oneYearReturn.toString():"-").append(COMMA);
					buffer.append(threeYearReturn!=null?threeYearReturn.toString():"-").append(COMMA);
					buffer.append(fiveYearReturn!=null?fiveYearReturn.toString():"-").append(COMMA);
					buffer.append(tenYearReturn!=null?tenYearReturn.toString():"-").append(COMMA);
					
					//since Inception
					buffer.append(sinceInception!=null?sinceInception.toString():"-").append(COMMA);
					buffer.append(oneYearQuarterlyReturn!=null?oneYearQuarterlyReturn.toString():"-").append(COMMA);
					buffer.append(threeYearQuarterlyReturn!=null?threeYearQuarterlyReturn.toString():"-").append(COMMA);
					buffer.append(fiveYearQuarterlyReturn!=null?fiveYearQuarterlyReturn.toString():"-").append(COMMA);
					buffer.append(tenYearQuarterlyReturn!=null?tenYearQuarterlyReturn.toString():"-").append(COMMA);
					//since Inception Quarterly
					buffer.append(sinceInceptionQuarterly!=null?sinceInceptionQuarterly.toString():"-").append(COMMA);
								
					//Expense Ratio**
					buffer.append(annualInvestmentCharge != null ? annualInvestmentCharge.toString() : "-").append(COMMA);
					buffer.append(morningstarCategory!=null?morningstarCategory:"-").append(COMMA);
					buffer.append(LINE_BREAK);
					if (funds[j].getFundDisclosureText() != null
							&& (Constants.MONEY_MARKET_FUND_US.equals(fundId) || Constants.MONEY_MARKET_FUND_NY.equals(fundId))){
						buffer.append(ReportController.QUOTE).append(funds[j].getFundDisclosureText()).append(ReportController.QUOTE);
						buffer.append(LINE_BREAK);
					}
					if (funds[j].getFundDisclosureText() != null
							&& (Constants.INVESTCO_PREMIER_US_GOVT_MONEY_FUND_US.equals(fundId) || Constants.INVESTCO_PREMIER_US_GOVT_MONEY_FUND_US.equals(fundId))){
						buffer.append(ReportController.QUOTE).append(funds[j].getFundDisclosureText()).append(ReportController.QUOTE);
						buffer.append(LINE_BREAK);
					}
					//Disclosure for SVGIF Funds
					if (isSVGIFFund) {
						buffer.append(ReportController.QUOTE).append(svgifDisclosureText).append(ReportController.QUOTE);
						buffer.append(LINE_BREAK);						
					}

				}
				buffer.append(LINE_BREAK);
			}

		}
		
		try {
			Content layoutBean = null;
			layoutBean = ContentCacheManager.getInstance().getContentById(
							ContentConstants.LAYOUT_PAGE_CONTRACT_FUNDS,
							ContentTypeManager.instance().LAYOUT_PAGE);
			buffer.append(setCsvFooter(layoutBean, request, location));
			
			//Retrieve the ClassDisclosure text
			Content classDisclosureContent = ContentCacheManager.getInstance().getContentById(
					ContentConstants.DYNAMIC_FOOTNOTE_MC,ContentTypeManager.instance().PAGE_FOOTNOTE,location);
			classDisclosure = ContentUtility.getContentAttribute(classDisclosureContent, "text");
			if (classDisclosure != null) {
				buffer.append(ReportController.QUOTE).append(
						ContentUtility
								.filterCMAContentForCSV(classDisclosure))
						.append(ReportController.QUOTE);
				buffer.append(LINE_BREAK);
				buffer.append(LINE_BREAK);
			}
			// Add the risk disclaimer to CSV
			if(riskDisclaimerText!=null){
				addContentsAsCSV(buffer, riskDisclaimerText);
			}
			
			//Retrieve the page disclaimer text
			Content pageDisclaimer = null;
			if(currentContract.isDefinedBenefitContract()){
				pageDisclaimer = ContentCacheManager.getInstance().getContentById(
								 ContentConstants.LAYOUT_PAGE_CONTRACT_FUNDS_DB,
								 ContentTypeManager.instance().LAYOUT_PAGE);	
			}else{
				pageDisclaimer = ContentCacheManager.getInstance().getContentById(
						 ContentConstants.LAYOUT_PAGE_CONTRACT_FUNDS,
						 ContentTypeManager.instance().LAYOUT_PAGE);	
			}
			pageDisclaimerText = ContentUtility.getPageDisclaimer(pageDisclaimer, new String[] {}, -1);
			buffer.append(LINE_BREAK);
			buffer.append(LINE_BREAK);
			if(pageDisclaimerText!=null){
				addContentsAsCSV(buffer, pageDisclaimerText);
			}
        } catch (ContentException e) {
            throw new SystemException(e, "Exception getting the fund footnotes");
        }
        
		return buffer.toString().getBytes();

	}
	
	/**
	 * Gets the CSV footer
	 * 
	 * @param layoutPageBean
	 * @param request
	 * @param location
	 * @param data
	 * @param selectedTab
	 * 
	 * @return String
	 * 
	 * @throws SystemException
	 * @throws ContentException
	 * @throws NumberFormatException
	 */
	public static String setCsvFooter(Content footNoteContent,
			HttpServletRequest request, Location location) throws SystemException, NumberFormatException,
			ContentException {
        StringBuffer buffer = new StringBuffer();
        Contract currentContract = getUserProfile(request).getCurrentContract();
        String inforcedText = null;
        buffer.append(LINE_BREAK);
 
        // FootNotes - start
        String footer = ContentUtility.getPageFooter(footNoteContent, new String[] {});
        String footnotes = ContentUtility.getPageFootnotes(footNoteContent, new String[] {}, -1);
 
        if (footer != null) {
            addContentsAsCSV(buffer, footer);
        }
 
        if (footnotes != null) {
            addContentsAsCSV(buffer, footnotes);
        }
        //Retrieve the Inforced text
        if(currentContract.getFundPackageSeriesCode()!=null){
	        if(!currentContract.getFundPackageSeriesCode().equalsIgnoreCase(Constants.FUND_PACKAGE_MULTICLASS)){
	        	 try {
	        	        buffer.append(LINE_BREAK);
	        	        Content inforcedContent = ContentCacheManager.getInstance().getContentById(
	    				ContentConstants.DYNAMIC_FOOTNOTE_INFORCED,ContentTypeManager.instance().PAGE_FOOTNOTE,location);
			    		inforcedText = ContentUtility.getContentAttribute(inforcedContent, "text");
			    		if (inforcedText != null) {
			    			buffer.append(ReportController.QUOTE).append(
			    					ContentUtility
			    							.filterCMAContentForCSV(inforcedText))
			    					.append(ReportController.QUOTE);
			    			buffer.append(LINE_BREAK);
			    			buffer.append(LINE_BREAK);
			    		}
	            }catch (ContentException e) {
	                throw new SystemException(e, "Exception getting the Inforced text");
	            }
	        }
        }
        String[] footnoteSymbolsArray = null;
 
        if (request.getAttribute("symbolsArray") != null){
            footnoteSymbolsArray = (String[]) request.getAttribute("symbolsArray");
        }else if (request.getSession().getAttribute("symbolsArray") != null){
            footnoteSymbolsArray = (String[]) request.getSession().getAttribute("symbolsArray");
        }
 
		final String companyId = Location.NEW_YORK.equals(location) ? CommonConstants.COMPANY_ID_NY
				: CommonConstants.COMPANY_ID_US;
 
        if (footnoteSymbolsArray != null) {
            Footnote[] sortedSymbolsArray = new Footnote[] {};
            try {
				sortedSymbolsArray = FootnoteCacheImpl.getInstance()
						.sortFootnotes(footnoteSymbolsArray, companyId);
            } catch (Exception e) {
                throw new SystemException(e, "Exception getting the fund footnotes");
            }
 
            /**
             * loop through the footnoteSymbolsArray, print the symbols in order - *'s, #'s, ^'s,
             * +'s, and numbers 1 to 18 Text for footnotes currently hard-coded, waiting for
             * getContent method to be developed
             */
            for (int i = 0; i < sortedSymbolsArray.length; i++) {
                if (sortedSymbolsArray[i] != null) {
                    String returnText = "";
                    if (sortedSymbolsArray[i].getText() != null) {
                        returnText = ContentUtility.jsEsc(sortedSymbolsArray[i].getText());
                        //returnText = ContentUtility.filterCMAContentForCSV(returnText);
                    }
                    String returnSymbol = sortedSymbolsArray[i].getSymbol();
                    String footnone = returnSymbol + " " + returnText;
                    footnone = (footnone.indexOf("\"")> -1)? footnone.replaceAll("\"", "\'"):footnone;
                    //split the <BR> tags to separate sections
                    String[] footNoteArray = footnone.split("<br/>|<BR/>|<br>|<BR>|</p>");
                    if(footNoteArray.length >1){
	                    for (String value : footNoteArray) {
	                        value = ContentUtility.filterCMAContentForCSV(value);
	                        buffer.append(escapeField(value)).append(LINE_BREAK);
	                    }
                    }else{
                    	buffer.append(escapeField(footnone));
                    }
                    buffer.append(LINE_BREAK);
                }
            }
			buffer.append(LINE_BREAK);
		}
		return buffer.toString();
	}

	private String addQuote(String text) {

		StringBuffer buff = new StringBuffer();
		buff.append("\"");
		buff.append(text);
		buff.append("\"");
		return buff.toString();

	}

	private CustomerServicePrincipal getCustomerServicePrincipal(String clientId) {

		CustomerServicePrincipal principal = new CustomerServicePrincipal();
		principal.setName(clientId);
		principal.setRoles(new String[] {CustomerServicePrincipal.ROLE_SUPER_USER });

		return principal;
	}

	 /**
     * calculate the previous closest quarter end date
     * <P>
     * <b>input</b> - we always get a date that is the last day of any given
     * month<br>
     * <b>output</b> - closest qe equal or prior to month provided.
     */
    public static Date calculateQuarterEnd(Date inputDate) {
        SimpleDateFormat sdfMonth = new SimpleDateFormat(FORMAT_MONTH_MM);
        SimpleDateFormat sdfYear = new SimpleDateFormat(FORMAT_YEAR_YYYY);
        Calendar cal = Calendar.getInstance();
        int i = 0;
        // current month (subtract 1 as month starts at zero in calendar)
        int currentMth = Integer.parseInt(sdfMonth.format(inputDate)) - 1;
        // get the current year
        int currentYear = Integer.parseInt(sdfYear.format(inputDate));
        // quarter months (11 is first and last to make life easy in Jan/Feb)
        int[] qMe = new int[] { 11, 2, 5, 8, 11 };

        for (i = 1; i < qMe.length; i++) {
            if (currentMth < qMe[i]) {
                if (qMe[i] == 2) {
					currentYear = currentYear - 1;
				}
                break;
            }
        }
        cal.clear();
        cal.set(Calendar.YEAR, currentYear);
        cal.set(Calendar.MONTH, qMe[i - 1]);
        cal.set(Calendar.DAY_OF_MONTH, cal
                .getActualMaximum(Calendar.DAY_OF_MONTH)); // get the last day
                                                            // of the month set

        return new Date(cal.getTime().getTime());
    }
    
    /**
     * Strips out the HTML tags from the buffer
     * 
     * @param buffer
     * @param content
     */
    private static void addContentsAsCSV(StringBuffer buffer, String content) {


    	if(content.indexOf("&quot;")> -1){
    		content = content.replaceAll("&quot;", "\'");
    	}
        // replace double quotes with single quotes    	
        if (content.contains("\"")) {
            content = content.replaceAll("\"", "\'");
        }
        if(content.indexOf("&amp;")> -1){
        	content = content.replaceAll("&amp;", "&");
        }

        // split the <BR> tags to separate sections
        String[] contentArray = content.split("<br/>|<BR/>|<br>|<BR>|</p>");

        for (String value : contentArray) {
            value = ContentUtility.filterCMAContentForCSV(value);
            buffer.append(escapeField(value)).append(LINE_BREAK);
        }
    }
    
    /**
     * don't want excel to think the , is the next field
     * 
     * @param field
     * @return String
     */
    public static String escapeField(String field) {
        if (field.indexOf(COMMA) != -1) {
            StringBuffer newField = new StringBuffer();
            newField = newField.append(CommonConstants.DOUBLE_QUOTES).append(field).append(
                    CommonConstants.DOUBLE_QUOTES);
            return newField.toString();
        } else {
            return field;
        }
    }
    
    /** This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations.
	 */
   /* @SuppressWarnings("rawtypes")
	public Collection doValidate( Form form,
			HttpServletRequest request) {
		Collection penErrors = PsValidation.doValidatePenTestAutoAction(form,
				mapping, request, "INPUT");
		if (penErrors != null && penErrors.size() > 0) {
			request.removeAttribute(PsBaseAction.ERROR_KEY);
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
