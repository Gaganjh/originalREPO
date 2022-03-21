package com.manulife.pension.platform.web.fap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.fop.apps.FOPException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ireports.model.FundOffering;
import com.manulife.pension.platform.utility.ireports.FundReportConstants;
import com.manulife.pension.platform.utility.ireports.utilities.FundReportUtil;
import com.manulife.pension.platform.utility.ireports.valueobject.FundReportParamsHolderVO;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.fap.constants.FapConstants;
import com.manulife.pension.platform.web.fap.tabs.FundScoreCardMetricsSelection;
import com.manulife.pension.platform.web.fap.tabs.util.ColumnsInfoBean;
import com.manulife.pension.platform.web.fap.tabs.util.FapTabUtility;
import com.manulife.pension.platform.web.fap.util.FapFilterUtility;
import com.manulife.pension.platform.web.fap.util.FapReportsUtility;
import com.manulife.pension.platform.utility.ireports.FundReportConstants;
import com.manulife.pension.platform.web.ireports.generator.FundReportGenerator;
import com.manulife.pension.platform.web.util.FundClassUtility;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PDFGenerator;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.platform.web.util.ReportsXSLProperties;
import com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.contract.valueobject.ContractDetailsOtherVO;
import com.manulife.pension.service.fund.fandp.valueobject.FandpFilterCriteria;
import com.manulife.pension.service.fund.fandp.valueobject.FundBaseInformation;
import com.manulife.pension.service.fund.fandp.valueobject.FundsAndPerformance;
import com.manulife.pension.service.fund.util.Constants;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * Base Action class for Funds & Performance
 * Performs the operations like.
 * 		1. loads the actionForm with the default values 
 * 		2. apply filter criteria and fetch the funds
 * 		3. performs sorting for the columns
 * 		4. generates PDFs and CSVs
 * 
 * @author ayyalsa
 * 
 */
public class FapController extends BaseAutoController {
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/WEB-INF/fap/fapFilterResults.jsp");
		forwards.put("continue","/WEB-INF/fap/fapFilterResults.jsp");
		forwards.put("customQuery","/WEB-INF/fap/fapCustomQueryFilter.jsp");
		forwards.put("assetClassDefinitions","/WEB-INF/fap/fapAssetClassDefinitions.jsp");
		forwards.put("sessionExpired","/WEB-INF/fap/fapFilterResults.jsp");
		

		forwards.put("enableTabs", "/fap/fapByFundInformation.jsp");
		forwards.put("displayTabs", "forward:/do/fap/tabs/?action=displayTabs");
		forwards.put("fundInformation", "/fap/fapByFundInformation.jsp");
		forwards.put("pricesAndYTD", "/fap/fapByPricesAndYTD.jsp");
		forwards.put("performanceAndFees", "/fap/fapByPerformanceAndFees.jsp");
		forwards.put("PerformanceAndFeesMonthly", "/fap/fapByPerformanceAndFees.jsp");
		forwards.put("PerformanceAndFeesQuarterly", "/fap/fapByPerformanceAndFees.jsp");
		forwards.put("standardDeviation", "/fap/fapByStandardDeviation");
		forwards.put("fundCharacteristics1", "/fap/fapByFundCharI.jsp");
		forwards.put("fundCharacteristics2", "/fap/fapByFundCharII.jsp");
        forwards.put("morningstar", "/fap/fapByMorningstar.jsp");
		forwards.put("fundScorecard", "/fap/fapByFundScorecard.jsp");
		forwards.put("changeToUS", "redirect:/do/fap/US");
		forwards.put("changeToNY", "redirect:/do/fap/NY");
		forwards.put("additionalParameters", "/WEB-INF/fap/fapAdditonalParametersBox.jsp");
	}
	

    /**
     * Constructor
     */
    public FapController() {
        super(FapController.class);
    }

    public String doDefault(AutoForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException, SystemException {

        FapForm fapForm = (FapForm) actionForm;

        // get the report's as of date
        Date reportAsOfDate = FundServiceDelegate.getInstance().getReportAsOfDate();
        if (reportAsOfDate != null) {
            String formattedValue = DateRender.formatByPattern(reportAsOfDate, null,
                    RenderConstants.MEDIUM_MDY_SLASHED);
            fapForm.setAsOfDate(formattedValue);
        } else {
            fapForm.setAsOfDate(StringUtils.EMPTY);
        }

        populateBaseFilterOptions(fapForm, request);
        
        Object advanceFilterEnabled = request.getSession().getAttribute("advanceFilterEnabled");
        if (advanceFilterEnabled != null) {
        	fapForm.setAdvanceFilterEnabled((Boolean)advanceFilterEnabled);
        	removeAttributesFromSession(request, "advanceFilterEnabled");
        }
        return "input";
    }

    /**
     * 
     * Applies the selected filters and fetches the fund details
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return forward
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
    public String doFilter(AutoForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException, SystemException {

        FapForm fapForm = (FapForm) actionForm;

        String tabSelected = fapForm.getTabSelected();

        if (StringUtils.isBlank(tabSelected)) {
            tabSelected = FapConstants.FUND_INFORMATION_TAB_ID;
        } else {
            tabSelected = getTabName(fapForm);
        }

        // removes the value object in the session
        removeAttributesFromSession(request, FapConstants.VO_FUNDS_AND_PERFORMANCE);
        LinkedHashMap<String, List<? extends FundBaseInformation>> tabValueObject = getTabValueObject(
                request, fapForm, tabSelected, true);

        // do default sorting
        sortFunds(null, tabValueObject);

        // Set the default tab object to the form-bean (Fund Information Tab)
        request.setAttribute("currentTabObject", tabValueObject);

        // the below block is for the Tab's PDFs.
        // In PDF we need to show the Funds selected in the selected list box
        setAttributesForPDF(request, fapForm);
        
        FundScoreCardMetricsSelection fundScoreCardMetricsSelection = (FundScoreCardMetricsSelection) request.getSession()
                .getAttribute(FapConstants.VO_FUND_SCORECARD_SELECTION);

		HashMap<String, List> currentTabColumns = new HashMap<String, List>();
    	if (FapConstants.FUNDSCORECARD_TAB_ID.equals(tabSelected)) {
    		currentTabColumns = (HashMap<String, List>) FapReportsUtility
    				.getHeaderValueObject(tabSelected, FapTabUtility.class,
    						fundScoreCardMetricsSelection);
    	} else {
    		currentTabColumns = (HashMap<String, List>) FapReportsUtility
    				.getHeaderValueObject(tabSelected, FapTabUtility.class,
    						FapConstants.WEB_FORMAT);
    	}
		
		
        // create the columns info for the tab
        fapForm.setColumnsInfo(currentTabColumns);

        if (FapConstants.BASE_FILTER_CONTRACT_FUNDS_KEY.equals(fapForm.getBaseFilterSelect())) {
            fapForm.setContractSearchText(fapForm.getContractSelect());
        }

        /*
         * Set the parent tab Id, when the toggle options (Monthly/Quarterly)
         * are selected
         */
        tabSelected = getSelectedTabName(fapForm);
        fapForm.setTabSelected(tabSelected);
        
        FundsAndPerformance fundsAndPerformance = (FundsAndPerformance) request.getSession()
                .getAttribute(FapConstants.VO_FUNDS_AND_PERFORMANCE);
        boolean isFundListMatch = FundReportUtil.isFundListMatch(request, fundsAndPerformance);
		if (isFundListMatch) {
			fapForm.setContractFundListModified(false);
			fapForm.setContractFundListModified(false);
		} else {
			if (FapConstants.CONTRACT_FUNDS.equals(fapForm.getBaseFilterSelect())) {
				fapForm.setContractFundListModified(true);
			} else {
				fapForm.setAllFundListModified(true);
			}
		}
        
        //return mapping.findForward(tabSelected);
        return tabSelected;
    }

    /**
     * Sets the required attributes for the PDF in session
     * 
     * @param request
     * @param fapForm
     * @throws SystemException 
     */
    private void setAttributesForPDF(HttpServletRequest request, FapForm fapForm)
            throws SystemException {

        // Get the Fund Offering object
        FundReportParamsHolderVO fundReportParams = FundReportGenerator.populateFundReportParams(
                fapForm, request);
        FundOffering fundOffering = FundReportUtil.createFundOffering(fundReportParams, request);
        
        if(fundOffering.getFundMenu() == FundReportConstants.RETAIL_MENU_NUM) {
            setAttributesInSession(request, FapConstants.FUND_MENU, "Retail Funds");
        } else if(fundOffering.getFundMenu() == FundReportConstants.SUB_ADVISED_MENU_NUM){
            setAttributesInSession(request, FapConstants.FUND_MENU, "Sub-Advised Funds");
        } else {
            setAttributesInSession(request, FapConstants.FUND_MENU, "All Funds");
        }

        setAttributesInSession(request, FapConstants.VIEW, fapForm.getBaseFilterSelect());

        if (FapConstants.CONTRACT_FUNDS.equals(fapForm.getBaseFilterSelect())) {

            setAttributesInSession(request, FapConstants.CONTRACT_NUMBER, fapForm
                    .getContractSearchText());
            setAttributesInSession(request, FapConstants.CONTRACT_NAME, fapForm
                    .getSelectedContractName());
            removeAttributesFromSession(request, FapConstants.CLASS);
        } else {
            setAttributesInSession(request, FapConstants.CLASS, fapForm.getClassSelect());
            removeAttributesFromSession(request, FapConstants.CONTRACT_NUMBER);
            removeAttributesFromSession(request, FapConstants.CONTRACT_NAME);
        }
        setAttributesInSession(request, FapConstants.GROUP_BY, fapForm.getGroupBySelect());

    }

    /**
     * Applies the selected filters and fetches the fund details
     * 
     * @param request
     * @param actionForm
     * @param tabName
     * @param notFromSession
     * @return forward
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
    protected LinkedHashMap<String, List<? extends FundBaseInformation>> getTabValueObject(
            HttpServletRequest request, FapForm actionForm, String tabName,
            boolean notFromSession) throws SystemException {

        FundsAndPerformance fundsAndPerformance = null;

        if (!notFromSession) {
            fundsAndPerformance = (FundsAndPerformance) request.getSession().getAttribute(
                    FapConstants.VO_FUNDS_AND_PERFORMANCE);
        }

        if (fundsAndPerformance == null) {
            // Retrieve the data from the cache (FundService module)
            FundServiceDelegate fundServiceDelegate = FundServiceDelegate.getInstance();
            FandpFilterCriteria filterCriteria = FapFilterUtility.createFandpFilterCriteria(
                    actionForm, null, request);

            if (!actionForm.isShowNML()) {
                filterCriteria.setIncludeNMLFunds(includeNMLFunds(request));
            }
            
            filterCriteria.setIncludeOnlyMerrillCoveredFunds(includeOnlyMerrillCoveredFunds(request, filterCriteria.getContractNumber()));
            
            if (actionForm.getSelectedFundsValues() != null
                    && actionForm.getSelectedFundsValues().trim().length() > 0) {
                filterCriteria.setFundIds(convertStringToSet(actionForm.getSelectedFundsValues()));
                
            // if the "Apply to the table below" button is clicked, create
            // filter criteria object and place it in session
            FapFilterUtility.setLastFilterCriteriaInSession(request, actionForm);
            
            } else if (filterCriteria.getContractNumber() != 0) {

            	/*
            	 * If the view by is contract Funds and the Selected Funds 
            	 * Slosh box is empty, then we need to show only the contract 
            	 * selected funds. 
            	 * To do this set the loadContractSelectedFundsOnly to true 
            	 * in filterCriteria object
            	 */
            	filterCriteria.setLoadContractSelectedFundsOnly(true);
            	
            	/*
            	 *  if the "view by" is by contract, then also create the filter
            	 *  criteria object and place in session
            	 */ 
                FapFilterUtility.setLastFilterCriteriaInSession(request, actionForm);
                
            } else {
                /*
                 *  if the "Apply" or "Advance Filters" button is clicked,
                 *  remove the old filter criteria object from the session
                 */ 
            	removeAttributesFromSession(request, FapConstants.LAST_EXECUTED_FILTER_CRITERIA);
            }

            fundsAndPerformance = fundServiceDelegate.getFundsAndPerformance(filterCriteria);

            // Set the value object in the session
            setAttributesInSession(request, FapConstants.VO_FUNDS_AND_PERFORMANCE,
                    fundsAndPerformance);

            synchronized (FapController.class) {
                FapTabUtility.asOfDates = fundsAndPerformance.getAsOfDates();
            }

        }

        Date fundCheckAsOfDate = fundsAndPerformance.getAsOfDates().get(FapConstants.CONTEXT_FCK);

        if (fundCheckAsOfDate != null) {
            String formattedValue = DateRender.formatByPattern(fundCheckAsOfDate, null,
                    RenderConstants.MEDIUM_MDY_SLASHED);
            request.setAttribute(FapConstants.FUND_CHECK_ASOFDATE, formattedValue);
        }

        // set the footnotes symbol array in session
        setAttributesInSession(request, "symbolsArray", getFootNotesSymbols(fundsAndPerformance));

        // return only the specified tab's value object
        return (LinkedHashMap<String, List<? extends FundBaseInformation>>) FapReportsUtility
                .getValueForAttribute(tabName, fundsAndPerformance, false);
    }

    /**
     * Does the sorting for the table columns
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return forward
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    @SuppressWarnings("unchecked")
    public String doSort(AutoForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException, SystemException {

        FapForm fapForm = (FapForm) actionForm;

        // get the current tab
        String currentTabName = getTabName(fapForm);

        // get the current tab value object
        LinkedHashMap<String, List<? extends FundBaseInformation>> currentTabValueObject = (LinkedHashMap<String, List<? extends FundBaseInformation>>) getTabValueObject(
                request, fapForm, currentTabName, false);

        // get the column details for the current tab
        List<ColumnsInfoBean> currentTabLevel2Columns = FapTabUtility
                .getColumnsInfo(currentTabName).get(FapConstants.COLUMN_HEADINGS_LEVEL_2);
        
        FapTabUtility.removeColumnsInfo(currentTabName);

        // get the column number clicked by the user
        int columnNumber = Integer.parseInt(request.getParameter("sortColumn"));
        
        FundScoreCardMetricsSelection fundScoreCardMetricsSelection = (FundScoreCardMetricsSelection) request.getSession()
                .getAttribute(FapConstants.VO_FUND_SCORECARD_SELECTION);

        // get the column Object based on the column number
        ColumnsInfoBean selectedColumn = currentTabLevel2Columns.get(columnNumber);
        int sortOrder = selectedColumn.getSortOrder();
        HashMap<String, List> currentTabColumns = new HashMap<String, List>();
    	if (FapConstants.FUNDSCORECARD_TAB_ID.equals(fapForm.getTabSelected())) {
    		currentTabColumns = (HashMap<String, List>) FapReportsUtility
    				.getHeaderValueObject(currentTabName, FapTabUtility.class,
    						fundScoreCardMetricsSelection);
    	} else {
    		currentTabColumns = (HashMap<String, List>) FapReportsUtility
    				.getHeaderValueObject(currentTabName, FapTabUtility.class,
    						FapConstants.WEB_FORMAT);
    	}
        
		
		
		if (FapConstants.MORNINGSTAR_TAB_ID.equals(currentTabName)) {
			currentTabLevel2Columns = FapTabUtility.getColumnsInfo(
					currentTabName).get(FapConstants.COLUMN_HEADINGS_LEVEL_2);			
			selectedColumn = currentTabLevel2Columns.get(columnNumber);
			selectedColumn.setSortOrder(sortOrder);
		}

        currentTabColumns.get(FapConstants.COLUMN_HEADINGS_LEVEL_2).set(columnNumber, selectedColumn);

        // create the columns info for the default tab (Fund Information Tab)
        fapForm.setColumnsInfo(currentTabColumns);

        sortFunds(selectedColumn, currentTabValueObject);

        // Set the default tab object to the form-bean (Fund Information Tab)
        request.setAttribute(FapConstants.VO_CURRENT_TAB, currentTabValueObject);

        //return mapping.findForward(currentTabName);
        return currentTabName;
    }

    /**
     * Does the sorting on the Tab's value object
     * 
     * @param selectedColumn
     * @param currentTabObject
     */
    @SuppressWarnings("unchecked")
    protected void sortFunds(ColumnsInfoBean selectedColumn,
            LinkedHashMap<String, List<? extends FundBaseInformation>> currentTabObject) {

        BeanComparator comparator = null;

        if (selectedColumn != null) {
            String sortColumn = selectedColumn.getKey();
            int sortOrder = selectedColumn.getSortOrder();

            // Create the comparator for sorting based on the sorting order
            // if descending then create a ReverseComparator
            if (sortOrder == 0) {
                selectedColumn.setSortOrder(1);
                selectedColumn.setSortClass("sort_ascending");
                comparator = new BeanComparator(sortColumn, 
                		new NullComparator(false));
            } else {
                selectedColumn.setSortOrder(0);
                selectedColumn.setSortClass("sort_descending");
                comparator = new BeanComparator(sortColumn, 
                		new NullComparator(new ReverseComparator(), true));
            }
        } else {
            comparator = new BeanComparator("sortNumber");
        }

        Set<String> keySet = currentTabObject.keySet();
        Iterator<String> iterator = keySet.iterator();

        while (iterator.hasNext()) {
            Collections.sort(currentTabObject.get(iterator.next()), comparator);
        }
    }

    /**
     * Returns the down-load CSV content MIME type. Derived class can override this method to return
     * a different MIME type.
     * 
     * @return The CSV content MIME type.
     */
    protected String getContentType() {
        // defaults to "text/csv"
        return FapConstants.CSV_TEXT;
    }

    /**
     * Action method invoked when the user changes the filter criteria It re-populates the drop down
     * with new options
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * 
     * @return ActionForward
     * 
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    public String doReportsAndDownload(AutoForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException, SystemException {

        FapForm fapForm = (FapForm) actionForm;
        fapForm.setFilterResultsId("reports");
        try {
            populateReportDropDownList(fapForm, request);
        } catch (SystemException se) {
            fapForm.setReportList(null);
            fapForm.setCofidDistChannel("");
        }
       // return mapping.findForward(FapConstants.FORWARD_CONTINUE);
        return FapConstants.FORWARD_CONTINUE;
    }

    /**
     * Action method invoked when the user changes the drop down option
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * 
     * @return ActionForward
     * 
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    public String doChangeDropDownList(AutoForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException, SystemException {

        FapForm fapForm = (FapForm) actionForm;
        String selectedReport = fapForm.getSelectedReport();

		if (FapConstants.DOWNLOAD_CSV.equals(selectedReport)
				|| FapConstants.PRINT_CURRENT_VIEW_PDF.equals(selectedReport)) {
            return null;
        }
		
		boolean isMerrill = includeOnlyMerrillCoveredFunds(request, 0);
        if (FapConstants.DOWNLOAD_CSV_ALL.equals(selectedReport)) {
            if(!isMerrill){
	            Collection<GenericException> warning = new ArrayList<GenericException>();
	            warning.add(new GenericExceptionWithContentType(
	                    CommonContentConstants.MESSAGE_NO_FUND_SCORECARD_INFO_IN_DOWNLOADED_CSV,
	                    ContentTypeManager.instance().MISCELLANEOUS));
	            setWarningsInRequest(request, warning);
	            fapForm.setMessagesExist(true);
	            fapForm.setFilterResultsId(null);
            }
            //return mapping.findForward(FapConstants.FORWARD_CONTINUE);
            return FapConstants.FORWARD_CONTINUE;
        }

        if (FundReportUtil.isIreport(selectedReport)) {
            fapForm.resetAdditionalParams();

            // Set the Client Name enabled Indicator.
            FundsAndPerformance fundsAndPerformance = (FundsAndPerformance) request.getSession()
                    .getAttribute(FapConstants.VO_FUNDS_AND_PERFORMANCE);

            FandpFilterCriteria fandPFilterCriteria = fundsAndPerformance.getFilterCriteriaUsed();

            fapForm.setClientNameEnabled(FapConstants.BASE_FILTER_ALL_FUNDS_KEY
                    .equals(fandPFilterCriteria.getViewBy()));
            
            populateAdvisorNameFields(fapForm, request);
        }

        //return mapping.findForward("additionalParameters");
        
        return "additionalParameters";
    }

    /**
     * This method will store the Fap Action Form in to session. This will be later used by the
     * Ireports to get the details from the Form.
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
    public String doStoreFapFormInSession(AutoForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException, SystemException {
        String forward = null;
        FapForm fapForm = (FapForm) actionForm;

        request.getSession(false).setAttribute(FapConstants.FAP_ACTION_FORM_IN_SESSION,
                fapForm);
        
        Collection<GenericException> errors = new ArrayList<GenericException>();
        
        if(fapForm.isStandardReport() || fapForm.isOtherReport()) {
        	if(fapForm.isAdvisorNameDisplayed() && StringUtils.isEmpty(fapForm.getAdvisorName())) {
        		errors.add(new GenericException(CommonErrorCodes.ADVISOR_NAME_IS_EMPTY));
        	}
        }
        
        if(!errors.isEmpty()) {
            fapForm.setMessagesExist(true);
            setErrorsInRequest(request, errors);
            //return mapping.findForward(FapConstants.FORWARD_CONTINUE);
            return FapConstants.FORWARD_CONTINUE;
        }
        
        //return mapping.findForward(forward);
        return forward;
    }

    /**
     * Action method invoked when the user selects downloadCsv in the dropdown and clicks Go
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * 
     * @return ActionForward
     * 
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     * @throws ContentException 
     * @throws NumberFormatException 
     */
    @SuppressWarnings("unchecked")
	public String doDownloadCsv(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException, NumberFormatException, ContentException {

        String selectedTab = getSelectedTabName((FapForm) actionForm);

        FundsAndPerformance fundsAndPerformance = (FundsAndPerformance) request.getSession()
                .getAttribute(FapConstants.VO_FUNDS_AND_PERFORMANCE);
        FapForm fapActionForm = (FapForm) actionForm;
        int contractNumber = 0;
		String contractSearchText = fapActionForm.getContractSearchText();
		
		// check if the search text is a contract number
		if (StringUtils.isNotBlank(contractSearchText) && 
				StringUtils.isNumeric(contractSearchText)) {
			contractNumber = Integer.parseInt(
					fapActionForm.getContractSearchText());
		}
		boolean isMerrillLynchAdvisor = includeOnlyMerrillCoveredFunds(request, contractNumber);

        boolean isFundListMatch = FundReportUtil.isFundListMatch(request, fundsAndPerformance);

        if (isFundListMatch) {
            request.setAttribute(FapConstants.MODIFIED_LINEUP, "false");
        } else {
            request.setAttribute(FapConstants.MODIFIED_LINEUP, "true");
        }
        
        boolean contractFundsFlag = false;
        if(contractNumber>0) {
            contractFundsFlag = true;
        }
        FundScoreCardMetricsSelection fundScoreCardMetricsSelection = (FundScoreCardMetricsSelection) request.getSession()
                .getAttribute(FapConstants.VO_FUND_SCORECARD_SELECTION);

		HashMap<String, List> columns = new HashMap<String, List>();
    	if (FapConstants.FUNDSCORECARD_TAB_ID.equals(selectedTab)) {
    		columns = (HashMap<String, List>) FapReportsUtility
    				.getHeaderValueObject(selectedTab, FapTabUtility.class,
    						fundScoreCardMetricsSelection);
    	} else {
    		columns = (HashMap<String, List>) FapReportsUtility
    				.getHeaderValueObject(selectedTab, FapTabUtility.class,
    						FapConstants.CSV_FORMAT);
    	}

        if (FapConstants.PRICES_YTD_TAB_ID.equals(selectedTab)) {
            List<ColumnsInfoBean> levelTwoTabs = columns.get(FapConstants.COLUMN_HEADINGS_LEVEL_2);
            levelTwoTabs.addAll(FapTabUtility.getLevel2columnsMap().get(
                    FapConstants.PRICES_YTD_LEVEL2COLUMNS_CSV));
        }

        HashMap<String, List<? extends FundBaseInformation>> data = (HashMap<String, List<? extends FundBaseInformation>>) FapReportsUtility
                .getValueForAttribute(((FapForm) actionForm).getTabSelected(),
                        fundsAndPerformance, false);

        Location site = null;
        if (CommonConstants.SITEMODE_USA.equals(((FapForm) actionForm).getSiteLocation())) {
            site = Location.USA;
        } else {
            site = Location.NEW_YORK;
        }

		byte[] downloadedData = FapReportsUtility.getDownloadedData(
				selectedTab, columns, data, request, getLayoutPage(
						PdfConstants.GENERIC_FAP_TAB_PATH, request),
				((FapForm) actionForm).getAsOfDate(),
				getAllTabHeaders((FapForm) actionForm), site, fundScoreCardMetricsSelection
				, getGlobalDisclosureCMAKey(selectedTab), isMerrillLynchAdvisor, contractFundsFlag);

        streamDownloadData(request, response, getContentType(), getFileName(actionForm, request),
                downloadedData);

        return null;

    }

    /**
     * Needs to be overriden by the sub class to return back the list of tab headers
     * 
     * @return Map<String, String>
     */
    protected Map<String, String> getAllTabHeaders(
    		FapForm fapForm) {
        return new HashMap<String, String>();
    }

    /**
     * Action method invoked when the user selects downloadCsvAll in the dropdown and clicks Go
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * 
     * @return ActionForward
     * 
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     * @throws ContentException 
     * @throws NumberFormatException 
     */
	public String doDownloadCsvAll(
			AutoForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SystemException, NumberFormatException, ContentException {

        FundsAndPerformance fundsAndPerformance = (FundsAndPerformance) request.getSession()
                .getAttribute(FapConstants.VO_FUNDS_AND_PERFORMANCE);
        FapForm fapActionForm = (FapForm) actionForm;
        int contractNumber = 0;
		String contractSearchText = fapActionForm.getContractSearchText();
		
		// check if the search text is a contract number
		if (StringUtils.isNotBlank(contractSearchText) && 
				StringUtils.isNumeric(contractSearchText)) {
			contractNumber = Integer.parseInt(
					fapActionForm.getContractSearchText());
		}
		
		boolean isMerrillLynchAdvisor = includeOnlyMerrillCoveredFunds(request, contractNumber);
        boolean isFundListMatch = FundReportUtil.isFundListMatch(request, fundsAndPerformance);

        if (isFundListMatch) {
            request.setAttribute(FapConstants.MODIFIED_LINEUP, "false");
        } else {
            request.setAttribute(FapConstants.MODIFIED_LINEUP, "true");
        }
        boolean contractFundsFlag = false;
        if(contractNumber>0) {
            contractFundsFlag = true;
        }

        HashMap<List<ColumnsInfoBean>, String> finalListOfHeader = new LinkedHashMap<List<ColumnsInfoBean>, String>();
        Map<String, List<ColumnsInfoBean>> level2columns = FapTabUtility.getLevel2columnsMap();

        finalListOfHeader.put(level2columns.get(FapConstants.COMMON_LEVEL2COLUMNS1),
                FapConstants.FUND_INFORMATION_TAB_ID);
        finalListOfHeader.put(level2columns.get(FapConstants.FUND_INFORMATION_LEVEL2COLUMNS),
                FapConstants.FUND_INFORMATION_TAB_ID);
        finalListOfHeader.put(level2columns.get(FapConstants.PRICES_YTD_LEVEL2COLUMNS),
                FapConstants.PRICES_YTD_TAB_ID);
        finalListOfHeader.put(level2columns
                .get(FapConstants.PERFORMANCE_AND_FEES_MONTHLY_LEVEL2COLUMNS),
                FapConstants.PERFORMANCE_FEES_TAB_ID);
        finalListOfHeader.put(level2columns
                .get(FapConstants.PERFORMANCE_AND_FEES_QUATERLY_LEVEL2COLUMNS),
                FapConstants.PERFORMANCE_FEES_TAB_ID);
        finalListOfHeader.put(level2columns
                .get(FapConstants.PERFORMANCE_AND_FEES_COMMON_LEVEL2COLUMNS),
                FapConstants.PERFORMANCE_FEES_TAB_ID);
        finalListOfHeader.put(level2columns.get(FapConstants.STANDARD_DEVIATION_LEVEL2COLUMNS),
                FapConstants.STANDARD_DEVIATION_TAB_ID);
        finalListOfHeader.put(level2columns.get(FapConstants.FUND_CHAR1_LEVEL2COLUMNS),
                FapConstants.FUND_CHAR_I_TAB_ID);
        finalListOfHeader.put(level2columns.get(FapConstants.FUND_CHAR2_LEVEL2COLUMNS),
                FapConstants.FUND_CHAR_II_TAB_ID);
        finalListOfHeader.put(level2columns.get(FapConstants.MORNINGSTAR_LEVEL2COLUMNS),
                FapConstants.MORNINGSTAR_TAB_ID);
        
        List<List<ColumnsInfoBean>> level1Tab = FapTabUtility.createLevel1HeadersForCsvAll();

        Location site = null;
        if (CommonConstants.SITEMODE_USA.equals(((FapForm) actionForm).getSiteLocation())) {
            site = Location.USA;
        } else {
            site = Location.NEW_YORK;
        }

        HashMap<String, List<? extends FundBaseInformation>> currentTabData = (HashMap<String, List<? extends FundBaseInformation>>) FapReportsUtility
                .getValueForAttribute(((FapForm) actionForm).getTabSelected(),
                        fundsAndPerformance, false);
        
        LinkedHashMap<String, List<? extends FundBaseInformation>> sortedData = (LinkedHashMap<String, List<? extends FundBaseInformation>>) FapReportsUtility
                .getValueForAttribute(FapConstants.FUND_INFORMATION_TAB_ID,
                        fundsAndPerformance, false);
        if(FapConstants.FUNDSCORECARD_TAB_ID.equals(((FapForm)actionForm).getTabSelected())) {
        	sortFunds(null, sortedData);
        }

        byte[] downloadedData = FapReportsUtility.getDownloadedDataForCsvAll(
				level1Tab, finalListOfHeader, currentTabData, request, getLayoutPage(
						PdfConstants.GENERIC_FAP_TAB_PATH, request),
				((FapForm) actionForm).getAsOfDate(),
				getAllTabHeaders((FapForm) actionForm), site, sortedData, isMerrillLynchAdvisor, contractFundsFlag);

        streamDownloadData(request, response, getContentType(), getFileName(actionForm, request),
                downloadedData);

        return null;

    }

    /**
     * Stream the download data to the Response. This method is singled out and made public static
     * so that non-ReportActions can use it. This method closes the Response's OutputStream when it
     * returns.
     * 
     * @param request
     * @param response
     * @param contentType
     * @param fileName
     * @param downloadData
     * 
     * @throws SystemException
     */
    public static void streamDownloadData(HttpServletRequest request, HttpServletResponse response,
            String contentType, String fileName, byte[] downloadData) throws SystemException {

    	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
        response.setContentType(contentType);

        String userAgent = request.getHeader("User-Agent");

        if (userAgent == null || userAgent.indexOf("MSIE 5") < 0) {
            response.setHeader(FapConstants.CONTENT_DISPOSITION_TEXT, FapConstants.ATTACHMENT_TEXT
                    + fileName);
        }
        response.setContentLength(downloadData.length);
        try {
            response.getOutputStream().write(downloadData);
        } catch (IOException ioException) {
            throw new SystemException(ioException, "Exception writing downloadData.");
        } finally {
            try {
                response.getOutputStream().close();
            } catch (IOException ioException) {
                throw new SystemException(ioException, "Exception closing output stream.");
            }
        }
    }

    /**
     * Generates i:report
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return forward
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    public String doGenerateIReport(AutoForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException, SystemException {

        FapForm fapForm = (FapForm) request.getSession(false).getAttribute(
                FapConstants.FAP_ACTION_FORM_IN_SESSION);

        if (fapForm != null) {
        	
        	//changes done as part of ACR REWRITE (historical iReport)
        	fapForm.setSelectedFormWebPage(true);
        	
            // Get the parameters from the Form.
            FundReportParamsHolderVO fundReportParams = FundReportGenerator.populateFundReportParams(
                    fapForm, request);

            // Trigger ireports using the parameters.
            ByteArrayOutputStream pdfOutStream = FundReportGenerator.triggerIreport(
                    fundReportParams, request);

            // Clearing out the Fap Action Form from the session.
            request.getSession(false).removeAttribute(FapConstants.FAP_ACTION_FORM_IN_SESSION);

            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    		response.setHeader("Pragma", "no-cache");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline");
            response.setContentLength(pdfOutStream.size());

            try {
                ServletOutputStream sos = response.getOutputStream();
                pdfOutStream.writeTo(sos);
                sos.flush();
            } catch (IOException ioException) {
                throw new SystemException(ioException, "Exception writing ireport pdfData.");
            } finally {
                try {
                    response.getOutputStream().close();
                } catch (IOException ioException) {
                    throw new SystemException(ioException, "Exception writing ireport pdfData.");
                }
            }
        }
        return null;
    }

    /**
     * Creates the filter values and populates it the actionForm
     * 
     * @param autoForm
     * @throws SystemException
     */
    protected void populateBaseFilterOptions(FapForm fapForm, HttpServletRequest request)
            throws SystemException {

        fapForm.setBaseFilterList(FapFilterUtility
                .createBaseFilterViewOptionList(includeContractFundsOption(request)));

        fapForm.setFundClassList(FapFilterUtility.getFundClassList());
        fapForm.setGroupByList(FapFilterUtility.createBaseFilterGroupByList());
        
        if(StringUtils.isEmpty(fapForm.getTabSelected()))
        {
        	fapForm.setTabSelected(FapConstants.FUND_INFORMATION_TAB_ID);
        }
        String contractNumber = (String) request.getSession().getAttribute(
                FapConstants.ATTR_CONTRACT_NUMBER);
        String groupby = (String) request.getSession().getAttribute(
                FapConstants.ATTR_GROUP_BY);
        String tabSelected = (String) request.getSession().getAttribute(
                FapConstants.ATTR_TAB_SELECTED);
        if (contractNumber != null) {
            fapForm.setBaseFilterSelect(FapConstants.BASE_FILTER_CONTRACT_FUNDS_KEY);
            fapForm.setContractSearchText(contractNumber);
            removeAttributesFromSession(request, FapConstants.ATTR_CONTRACT_NUMBER);
            fapForm.setGroupBySelect(groupby);
            removeAttributesFromSession(request, FapConstants.ATTR_GROUP_BY);
            if(StringUtils.isNotEmpty(tabSelected))
            {
	            fapForm.setTabSelected(tabSelected);
	            removeAttributesFromSession(request, FapConstants.ATTR_TAB_SELECTED);
            }
        }
        
        populateReportDropDownList(fapForm, request);
    }

    /**
     * Gets the CSV file name
     * 
     * @param actionForm
     * @return String
     */
    protected String getFileName(AutoForm actionForm, HttpServletRequest request) {
        FapForm fapForm = (FapForm) actionForm;

        StringBuffer name = new StringBuffer();
        String fileName = FapConstants.CSV_FILE_NAME;
        if (request.getSession().getAttribute("FileName") != null) {
            fileName = (String) request.getSession().getAttribute("FileName");
        }
        name.append(fileName).append("_");
        if (fapForm.getContractSearchText() != null) {
            name.append(fapForm.getContractSearchText()).append("_");
        }
        name.append(fapForm.getAsOfDate().replaceAll("/", "")).append(
                FapConstants.CSV_EXTENSION);
        return name.toString();
    }

    /**
     * The method populates the form bean with the reports & download dropdown list
     * 
     * @param fapForm
     * 
     * @throws SystemException
     */
    protected void populateReportDropDownList(FapForm fapForm,
            HttpServletRequest request) throws SystemException {
        Map<String, String> reports = new LinkedHashMap<String, String>();
        if (FapConstants.CONTRACT_FUNDS.equals(fapForm.getBaseFilterSelect())) {
            reports = FapFilterUtility.getReportsAndDownloadList();
            // remove market index report from the drop
            // down list
            reports.remove(FapConstants.OTHER_REPORTS_TITLE);
            reports.remove(FapConstants.MARKET_INDEX_REPORT_TITLE);
            String contract = fapForm.getContractSearchText();
            if (!StringUtils.trimToEmpty(contract).equals("") 
            		&& contract.length() >= FapConstants.CONTRACT_NUMBER_MIN_LENGTH 
    				&& contract.length() <= FapConstants.CONTRACT_NUMBER_MAX_LENGTH 
    				&& StringUtils.isNumeric(contract)) {
                ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();
                Contract vo = null;
                try {
                    vo = delegate.getContractDetails(Integer.parseInt(contract), 6);
                    setAttributesInSession(request, FapConstants.CONTRACT_DEAILS, vo);

                    if(Integer.parseInt(contract) != 0){
        	    		ContractDetailsOtherVO contractDetailsOtherVO = delegate.getContractDetailsOther(Integer.parseInt(contract));
        				if(contractDetailsOtherVO != null && contractDetailsOtherVO.isMerrillLynch()){
        					fapForm.setCofidDistChannel("ML");
        				}
        	    	}
                    
                } catch (ContractNotExistException e) {
                    throw new SystemException(e, "Contract number is incorect");
                }
            }
        } else {
            reports = FapFilterUtility.getReportsAndDownloadList();
        }
        fapForm.setReportList(reports);
        fapForm.setSelectedReport(FapConstants.DEFAULT_REPORTS_VALUE);
    }

    /**
     * Converts the String which has a delimiter as "|" into Set
     * 
     * The Funds that are in the selectedFunds Slosh box will be stored in 
     * a String separated by "|". This method converts the String to a Set.
     * 
     * @param valuesAsString
     * @return
     */
    private Set<String> convertStringToSet(String valuesAsString) {

        // Convert the Fund IDs String to a Set
        Set<String> valuesAsSet = new HashSet<String>();
        if (valuesAsString.length() > 0) {
            StringTokenizer stringTokenizer = new StringTokenizer(valuesAsString, "|");

            while (stringTokenizer.hasMoreElements()) {
                valuesAsSet.add(stringTokenizer.nextToken());
            }
        }

        return valuesAsSet;
    }

    /**
     * Returns the currently selected tab name.
     * 
     * If the Tab name is "PerformanceAndFees", then we post-fix
     * "Monthly" to the tab name and return it back.
     * 
     * Other tab names are returned unchanged
     * 
     * @param fapForm
     * @return
     */
    protected String getTabName(FapForm fapForm) {
        String tabName = fapForm.getTabSelected();

        if (StringUtils.equalsIgnoreCase(fapForm.getTabSelected(),
                FapConstants.PERFORMANCE_FEES_TAB_ID)) {
            tabName = StringUtils.capitalize(tabName) + "Monthly";
        }

        return tabName;
    }

    /**
     * Returns a footNotes symbol array
     * 
     * @param fundsAndPerformance
     * @return
     */
    private String[] getFootNotesSymbols(FundsAndPerformance fundsAndPerformance) {

        String allSymbols = new String();

        for (String symbol : fundsAndPerformance.getFundFootNotes()) {
            if (StringUtils.isNotBlank(allSymbols)) {
                allSymbols += ",";
            }
            allSymbols += symbol;
        }

        String[] allSymbolsArray = StringUtils.split(allSymbols, ',');
        return allSymbolsArray;
    }

    /**
     * This method will be called when the user selects the print current view in the drop down and
     * clicks go. This method fetches the data from the database and places the information into an
     * XML file. Using Apache-FOP, the XML file, XSLT file is converted into a PDF file. The PDF
     * file is sent back to the user.
     * 
     * @param reportForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     * @throws FOPException
     * @throws TransformerException
     * @throws ContentException
     */
    @SuppressWarnings("unchecked")
    public String doPrintPdf(AutoForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException, SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("Inside doPrintPDF");
        }

        FapForm fapForm = (FapForm) actionForm;
        
        // get the selected tab and set it to the form
        String selectedTab = getSelectedTabName(fapForm);
        fapForm.setTabSelected(selectedTab);
        
        FundScoreCardMetricsSelection fundScoreCardMetricsSelection = (FundScoreCardMetricsSelection) request.getSession()
                .getAttribute(FapConstants.VO_FUND_SCORECARD_SELECTION);
        
		HashMap<String, List> columns = new HashMap<String, List>();
    	if (FapConstants.FUNDSCORECARD_TAB_ID.equals(selectedTab)) {
    		columns = (HashMap<String, List>) FapReportsUtility
    				.getHeaderValueObject(selectedTab, FapTabUtility.class,
    						fundScoreCardMetricsSelection);
    	} else {
    		columns = (HashMap<String, List>) FapReportsUtility
    				.getHeaderValueObject(selectedTab, FapTabUtility.class,
    						FapConstants.PDF_FORMAT);
    	}

        FundsAndPerformance fundsAndPerformance = (FundsAndPerformance) request.getSession()
                .getAttribute(FapConstants.VO_FUNDS_AND_PERFORMANCE);

        boolean isFundListMatch = FundReportUtil.isFundListMatch(request, fundsAndPerformance);

        if (isFundListMatch) {
            request.setAttribute(FapConstants.MODIFIED_LINEUP, "false");
        } else {
            request.setAttribute(FapConstants.MODIFIED_LINEUP, "true");
        }

        Date fundCheckAsOfDate = fundsAndPerformance.getAsOfDates().get("FCK");

        if (fundCheckAsOfDate != null) {
            String formattedValue = DateRender.formatByPattern(fundCheckAsOfDate, null,
                    RenderConstants.MEDIUM_MDY_SLASHED);
            fapForm.setFundcheckAsOfDate(formattedValue);
        } else {
            fapForm.setFundcheckAsOfDate("");
        }

        HashMap<String, List<? extends FundBaseInformation>> reportData = (HashMap<String, List<? extends FundBaseInformation>>) FapReportsUtility
                .getValueForAttribute(fapForm.getTabSelected(),
                        fundsAndPerformance, false);

        ByteArrayOutputStream pdfOutStream = prepareXMLandGeneratePDF(actionForm, columns,
                reportData, request);
        
        response.setHeader("Cache-Control", "must-revalidate,no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline");
        response.setContentLength(pdfOutStream.size());

        try {
            ServletOutputStream sos = response.getOutputStream();
            pdfOutStream.writeTo(sos);
            sos.flush();
        } catch (IOException ioException) {
            throw new SystemException(ioException, "Exception writing pdfData.");
        } finally {
            try {
                response.getOutputStream().close();
            } catch (IOException ioException) {
                throw new SystemException(ioException, "Exception writing pdfData.");
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Exiting doPrintPDF");
        }
        return null;
    }

    /**
     * This method will generate the PDF and return a ByteArrayOutputStream which will be sent back
     * to the user. This method would: - Create the XML-String from VO. - Create the PDF using the
     * created XML-String and XSLT file.
     * 
     * @throws ContentException
     */
    @SuppressWarnings("unchecked")
    protected ByteArrayOutputStream prepareXMLandGeneratePDF(AutoForm form,
            HashMap<String, List> columns,
            HashMap<String, List<? extends FundBaseInformation>> reportData,
            HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside prepareXMLandGeneratePDF");
        }

        ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();
        try {

            Object xmlTree = prepareXMLFromReport(form, columns, reportData, request);
            String xsltFileName = getXSLTFileName();
            String configFileName = getFOPConfigFileName();
            if (xmlTree == null || xsltFileName == null) {
                return pdfOutStream;
            }
            String xsltfile = ReportsXSLProperties.get(xsltFileName);
            String configfile = ReportsXSLProperties.get(configFileName);
            String includedXSLPath = ReportsXSLProperties
                    .get(CommonConstants.INCLUDED_XSL_FILES_PATH);
            if (xmlTree instanceof Document) {
                pdfOutStream = PDFGenerator.getInstance().generatePDFFromDOM((Document) xmlTree,
                        xsltfile, configfile, includedXSLPath);
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
     * This method needs to be overridden by any Report that needs PDF Generation functionality.
     * This method would generate the XML file.
     * 
     * 
     * @param reportForm
     * @param report
     * @param request
     * @return Object
     * @throws ParserConfigurationException
     * @throws SystemException
     * @throws ContentException 
     * @throws NumberFormatException 
     */
    @SuppressWarnings("unchecked")
	public Document prepareXMLFromReport(AutoForm form,
			HashMap<String, List> columns,
			HashMap<String, List<? extends FundBaseInformation>> reportData,
			HttpServletRequest request) throws ParserConfigurationException,
			SystemException, NumberFormatException, ContentException {

        PDFDocument doc = new PDFDocument();
        Element rootElement = doc.createRootElement(PdfConstants.FUNDS_AND_PERFORMANCE);

        String selectedTab = ((FapForm) form).getTabSelected();
        
        String modifiedLineUp = (String) request.getAttribute(FapConstants.MODIFIED_LINEUP);
        String currentView = (String) request.getSession().getAttribute(FapConstants.VIEW);
        String currentContract = (String) request.getSession().getAttribute(
                FapConstants.CONTRACT_NUMBER);
        String currentContractName = (String) request.getSession().getAttribute(
                FapConstants.CONTRACT_NAME);
        String currentClass = (String) request.getSession().getAttribute(FapConstants.CLASS);
        String currentGroupByOption = (String) request.getSession().getAttribute(
                FapConstants.GROUP_BY);
        String fundMenu = (String) request.getSession().getAttribute(
                FapConstants.FUND_MENU);
        
        FapForm fapActionForm = new FapForm();
        int contractNumber = 0;
		String contractSearchText = fapActionForm.getContractSearchText();
		
		// check if the search text is a contract number
		if (StringUtils.isNotBlank(contractSearchText) && 
				StringUtils.isNumeric(contractSearchText)) {
			contractNumber = Integer.parseInt(
					fapActionForm.getContractSearchText());
		}
		boolean isMerrillLynchAdvisor = includeOnlyMerrillCoveredFunds(request, contractNumber);
        boolean isMerrillLynchContract = false;
        if(currentContract != null){
        	isMerrillLynchContract = includeOnlyMerrillCoveredFunds(request, Integer.parseInt(currentContract));
        }
        

        if(!"All Funds".equals(fundMenu)) {
            currentView = fundMenu;
        } else {
            if (FapConstants.CONTRACT_FUNDS.equals(currentView)) {
                currentView = "Contract Funds";
            } else {
                currentView = "All Funds";
            } 
        }
        
        if ("true".equals(modifiedLineUp)) {
        	 if ("Contract Funds".equals(currentView)) {
            	 currentView = currentView + " - Modified Lineup^^";
            } else {
            	 currentView = currentView + " - Modified Lineup^";
            } 
        }

        if (currentClass != null) {
            currentClass = FundClassUtility.getInstance().getFundClassName(currentClass);
        }

        if ("filterRiskCategoryFunds".equals(currentGroupByOption)) {
            currentGroupByOption = "Risk/Return Category";
        } else {
            currentGroupByOption = "Asset Class";
        }

        doc.appendTextNode(rootElement, PdfConstants.TAB_NAME, selectedTab);
        
        FundScoreCardMetricsSelection fundScoreCardMetricsSelection = (FundScoreCardMetricsSelection) request.getSession()
                .getAttribute(FapConstants.VO_FUND_SCORECARD_SELECTION);
        
        if(fundScoreCardMetricsSelection != null) {
        	 doc.appendTextNode(rootElement, PdfConstants.SHOW_MORNIGSTAR_SCORECARD, String.valueOf(fundScoreCardMetricsSelection.isShowMorningstarScorecardMetrics()));
             doc.appendTextNode(rootElement, PdfConstants.SHOW_FI360_SCORECARD, String.valueOf(fundScoreCardMetricsSelection.isShowFi360ScorecardMetrics()));
             doc.appendTextNode(rootElement, PdfConstants.SHOW_RPAG_SCORECARD, String.valueOf(fundScoreCardMetricsSelection.isShowRpagScorecardMetrics()));
        }
        
        FapReportsUtility.setLogoAndPageName(getLayoutPage(PdfConstants.GENERIC_FAP_TAB_PATH,
                request), doc, rootElement, FapConstants.JHRPS_LOGO_FILE);

        doc.appendTextNode(rootElement, PdfConstants.ASOF_DATE, ((FapForm) form)
                .getAsOfDate());
        
        if( FapConstants.FUNDSCORECARD_TAB_ID.equals(((FapForm) form).getTabSelected())) {
        	Date scorecardDate = FapTabUtility.asOfDates.get(Constants.FUND_SCORECARD_EFFECTIVE_DATE_KEY);
        	String formattedValue = DateRender.formatByPattern(scorecardDate, null,
                    RenderConstants.MEDIUM_MDY_SLASHED);
        	doc.appendTextNode(rootElement, PdfConstants.JHSCORECARD_AS_OF_DATE, formattedValue);
        }

        // setting into1 and intro2 elements
        PdfHelper.setIntro1Intro2XMLElements(getLayoutPage(PdfConstants.GENERIC_FAP_TAB_PATH,
                request), doc, rootElement);

        // set headers specific to tab
        Map<String, String> tabHeaders = getAllTabHeaders((FapForm) form);
        String tabHeader = tabHeaders.get(selectedTab);
        PdfHelper.convertIntoDOM(PdfConstants.TAB_HEADER, rootElement, doc, tabHeader);

        /*
        if ("Contract Funds".equalsIgnoreCase(currentView)) {
            tabHeader = tabHeaders.get(FapConstants.FAP_CONTRACT_VIEW_ID);
        } else {
            tabHeader = tabHeaders.get(FapConstants.FAP_GENERIC_VIEW_ID);
        }             
        PdfHelper.convertIntoDOM(PdfConstants.TAB_HEADER, rootElement, doc, tabHeader);
        */
        
        Element filters = doc.createElement("filters");
        doc.appendElement(rootElement, filters);

        doc.appendTextNode(filters, "currentView", currentView);
        if (currentContract != null) {
            doc.appendTextNode(filters, "currentContract", currentContract);
            doc.appendTextNode(rootElement, "currentContract", currentContract);
            doc.appendTextNode(rootElement, "currentContractName", currentContractName);
        }
        if (currentClass != null) {
            doc.appendTextNode(filters, "currentClass", currentClass);
        }
        if (currentGroupByOption != null) {
            doc.appendTextNode(filters, "currentGroupByOption", currentGroupByOption);
        }

        /*
         * Level 1 Header section
         */
        List<List<ColumnsInfoBean>> levelOneTabs = columns
                .get(FapConstants.COLUMN_HEADINGS_LEVEL_1);
        FapReportsUtility.setLevel1HeaderElements(doc, rootElement, levelOneTabs, selectedTab);

        /*
         * Level 2 Header section
         */
        List<ColumnsInfoBean> levelTwoTabs = columns.get(FapConstants.COLUMN_HEADINGS_LEVEL_2);
        FapReportsUtility.setLevel2HeaderElements(doc, rootElement, levelTwoTabs, selectedTab, isMerrillLynchAdvisor, isMerrillLynchContract, currentView);

        

        /*
         * Table values section
         */
        FapReportsUtility.setFundDeatilsElements(doc, rootElement, levelTwoTabs, reportData,
        	      selectedTab, ((FapForm) form).getFundcheckAsOfDate(), isMerrillLynchAdvisor, isMerrillLynchContract);

        Location site = null;
        if (CommonConstants.SITEMODE_USA.equals(((FapForm) form).getSiteLocation())) {
            site = Location.USA;
        } else {
            site = Location.NEW_YORK;
        }
        
        /*
         * set fund footnotes section
         */
		FapReportsUtility.setFootNotesElement(doc, rootElement, request,
				getLayoutPage(PdfConstants.GENERIC_FAP_TAB_PATH, request),
				site, reportData, selectedTab, getGlobalDisclosureCMAKey(selectedTab));

        return doc.getDocument();
    }
    
    protected int getGlobalDisclosureCMAKey(String selectedTab) {
    	if( FapConstants.FUNDSCORECARD_TAB_ID.equals(selectedTab)) {
    		return CommonContentConstants.FUND_SCORECARD_GLOBAL_DISCLOSURE;
    	} else {
    		return CommonContentConstants.BD_GLOBAL_DISCLOSURE;
    	}
    }
    
    /**
     * This method is used to get XSL file name for PDF generation
     * 
     * 
     * @return String XSLT file name
     */
    protected String getXSLTFileName() {
        return FapConstants.XSL_FILE_NAME;
    }

    /**
     * This method would return the key present in ReportsXSL.properties file. This key has the
     * value as path to FOP Configuration file.
     * 
     * @return String
     */
    protected String getFOPConfigFileName() {
        return CommonConstants.FOP_CONFIG_FILE_KEY_NAME;
    }

    /**
     * Places the given Object in Session with key as "key"
     * 
     * @param request
     * @param key
     * @param value
     */
    protected void setAttributesInSession(HttpServletRequest request, String key, Object value) {
        request.getSession(false).setAttribute(key, value);
    }

    /**
     * Removes the Object from the Session with key as "key"
     * 
     * @param request
     * @param key
     */
    protected void removeAttributesFromSession(HttpServletRequest request, String key) {
        request.getSession(false).removeAttribute(key);
    }

    /**
     *  In TPA, the tabs are highlighted by matching the tabId with 
     *  FapForm.tabSelected attribute. When the user clicks the 
     *  Monthly or Quarterly toggle option in Performance & Fees tab, the 
     *  tab name will be "performanceAndFeesMonthly" or "performanceAndFeesQuarterly".
     *  But the tabId will be "performanceAndFees". So, tabs will not 
     *  be highlighted. In-order to avoid this, the tabSelected is set 
     *  as "performanceAndFees", when the actual tabSelected value has the toggle
     *  option id.
     *
     * @param fapForm
     * @return tabSelected
     */
    protected String getSelectedTabName(FapForm fapForm) {
    	
    	 String selectedTab = fapForm.getTabSelected();
    	
    	 if ("PerformanceAndFeesQuarterly".equals(selectedTab)
         		|| "PerformanceAndFeesMonthly".equals(selectedTab)) {
             selectedTab = FapConstants.PERFORMANCE_FEES_TAB_ID;
         }
    	 
    	 return selectedTab;
    }
    
    /**
     * This method gets layout page for the given layout id.
     * 
     * Needs to be over ridden by corresponding base classes to return back the LayoutPage based on
     * the application (PS or BDW)
     * 
     * @param path
     * @return LayoutPage
     */
    protected LayoutPage getLayoutPage(String id, HttpServletRequest request) {
        return null;
    }

    /**
     * Returns true, if the NML funds needs to be included
     * 
     * Needs to be overridden
     * 
     * @param request
     * @return boolean
     */
    protected boolean includeNMLFunds(HttpServletRequest request) throws SystemException {
        return false;
    }
    
    /**
     * Returns true, if the ML funds needs to be included
     * 
     * Needs to be overridden
     * 
     * @param request
     * @return boolean
     */
    protected boolean includeOnlyMerrillCoveredFunds(HttpServletRequest request, int contractNumber) throws SystemException {
        return false;
    }

    /**
     * Returns true, if the "Contract Funds" option needs to be included in the base filter
     * drop-down list
     * 
     * Needs to be overridden
     * 
     * @param request
     * @return boolean
     */
    protected boolean includeContractFundsOption(HttpServletRequest request) throws SystemException {
        return true;
    }

    /**
     * Sets the warning collection in the request
     * 
     * Needs to be overridden
     * 
     * @param request
     */
    @SuppressWarnings("unchecked")
    protected void setWarningsInRequest(HttpServletRequest request, Collection warnings) {
    }
    
    /**
     * Sets the information message collection in the request
     * 
     * Needs to be overridden
     * 
     * @param request
     */
    @SuppressWarnings("unchecked")
    protected void setInformationMessagesInRequest(HttpServletRequest request, Collection warnings) {
    }
    
    /**
     * Sets the advisor name to default
     * 
     * Needs to be overridden
     * 
     * @param request
     */
    protected void populateAdvisorNameFields(FapForm fapForm, HttpServletRequest request) {
    	fapForm.setAdvisorNameDisplayed(true);
    	fapForm.setAdvisorName(null);
    }
}
