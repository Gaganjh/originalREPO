package com.manulife.pension.bd.web.bob.participant;

import static com.manulife.pension.bd.web.BDConstants.BOB_PAGE_FORWARD;
import static com.manulife.pension.platform.web.CommonConstants.DOLLAR_SIGN;
import static com.manulife.pension.platform.web.CommonConstants.PERCENTAGE_SIGN;
import static com.manulife.pension.service.fund.valueobject.FundVO.RISK_AGGRESIVE;
import static com.manulife.pension.service.fund.valueobject.FundVO.RISK_CONSERVATIVE;
import static com.manulife.pension.service.fund.valueobject.FundVO.RISK_GROWTH;
import static com.manulife.pension.service.fund.valueobject.FundVO.RISK_GROWTH_INCOME;
import static com.manulife.pension.service.fund.valueobject.FundVO.RISK_INCOME;
import static com.manulife.pension.service.fund.valueobject.FundVO.RISK_LIFECYCLE;
import static com.manulife.pension.service.fund.valueobject.FundVO.RISK_PBA;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.util.UrlPathHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BDPdfConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.BobContextUtils;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.navigation.UserMenu;
import com.manulife.pension.bd.web.navigation.UserMenuItem;
import com.manulife.pension.bd.web.navigation.UserNavigation;
import com.manulife.pension.bd.web.report.BDPdfController;
import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.SynchronizationServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.platform.web.controller.BaseForm;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.FundClassUtility;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.ps.service.participant.valueobject.InvestmentOptionVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountAssetsByRiskVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantFundSummaryVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantLoanDetails;
import com.manulife.pension.service.account.valueobject.Fund;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.fund.valueobject.FundVO;
import com.manulife.pension.service.security.BDPrincipal;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.pdf.PdfConstants;
import com.manulife.util.piechart.PieChartBean;
import com.manulife.util.piechart.PieChartUtil;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This class is the Base class of new Participant Account actions
 * 
 * @author Saravana
 */
public abstract class ParticipantAccountCommonController extends BDPdfController {

    private Environment env = Environment.getInstance();

    public final static String LINE_BREAK = BDReportController.LINE_BREAK;

    public final static String COMMA = BDReportController.COMMA;

    public final static String QUOTE = BDReportController.QUOTE;

    public final static String CSV_EXTENSION = ".csv";

    private final static String DEFAULT_BIRTH_DATE = "1/1/1980";

    private final static String DEFAULT_PERCENT = "0%";

    private final static String PERCENT_PATTERN = "##0%";

    protected final static String XSL_EXTENSION = ".XSLFile";

    protected final static String STRING_YES = "YES";

    protected static PieChartBean pieChartBean;

    protected static final int HUNDRED = 100;

    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MMddyyyy");

    /**
     * Constructor
     * 
     * @param clazz
     */
    @SuppressWarnings("unchecked")
    public ParticipantAccountCommonController(Class clazz) {
        super(clazz);
    }

    /**
     * The preExecute method has been overriden to see if the contractNumber is coming as part of request parameter. If
     * the contract Number is coming as part of request parameter, the BobContext will be setup with contract
     * information of the contract number passed in the request parameter.
     * 
     */
    protected String preExecute(ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException, SystemException {

        super.preExecute( form, request, response);

        BobContextUtils.setUpBobContext(request);

        BobContext bob = BDSessionHelper.getBobContext(request);
        if (bob == null || bob.getCurrentContract() == null) {
            //return mapping.findForward(BOB_PAGE_FORWARD);
        	return BOB_PAGE_FORWARD;
        }

        if (bob.getCurrentContract().getCompanyCode().equals(
                GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY)) {
            ApplicationHelper.setRequestContentLocation(request, Location.NEW_YORK);
        }

        BobContextUtils.setupProfileId(request);

        return null;
    }

    /**
     * @see BaseController#doExecute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    @SuppressWarnings("unchecked")
    public String doExecute(ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doExecute");
        }

        List<GenericException> errors = new ArrayList<GenericException>();
        ParticipantAccountVO participantAccountVO = null;

        ParticipantAccountForm accountForm = (ParticipantAccountForm) form;
        Contract contract = getBobContext(request).getCurrentContract();
        int contractNumber = contract.getContractNumber();
        String productId = contract.getProductId();
        String mappingForward = new UrlPathHelper().getPathWithinServletMapping(request);
        // Find the forward
        String forwardName = BDConstants.PARTICIPANT_ACCOUNT_FORWARD;
        if (mappingForward.startsWith(BDConstants.PARTICIPANT_ACCOUNT_URL)) {
            forwardName = BDConstants.PARTICIPANT_ACCOUNT_FORWARD;
        } else if (mappingForward.startsWith(
                BDConstants.PARTICIPANT_ACCOUNT_MONEY_TYPE_SUMMARY_URL)) {
            forwardName = BDConstants.PPT_ACCOUNT_MONEYTYPE_SUMMARY_FORWARD;
        } else if (mappingForward.startsWith(
                BDConstants.PARTICIPANT_ACCOUNT_MONEY_TYPE_DETAILS_URL)) {
            forwardName = BDConstants.PPT_ACCOUNT_MONEYTYPE_DETAILS_FORWARD;
        } else if (mappingForward.startsWith(BDConstants.PARTICIPANT_ACCOUNT_NET_DEFERRAL_URL)) {
            forwardName = BDConstants.PPT_ACCOUNT_NET_DEFERRAL_FORWARD;
        } else if (mappingForward.startsWith(
                BDConstants.PARTICIPANT_ACCOUNT_NET_CONTRIB_EARNINGS_URL)) {
            forwardName = BDConstants.PPT_ACCOUNT_NET_CONTRIB_EARNINGS_FORWARD;
        } else if (mappingForward.startsWith(BDConstants.DB_ACCOUNT_URL)) {
            forwardName = BDConstants.PPT_DB_ACCOUNT_FORWARD;
        } else if (mappingForward.startsWith(BDConstants.DB_ACCOUNT_MONEY_TYPE_SUMMARY_URL)) {
            forwardName = BDConstants.PPT_DB_ACCOUNT_MONEYTYPE_SUMMARY_FORWARD;
        } else if (mappingForward.startsWith(BDConstants.DB_ACCOUNT_MONEY_TYPE_DETAILS_URL)) {
            forwardName = BDConstants.PPT_DB_ACCOUNT_MONEYTYPE_DETAILS_FORWARD;
        }

        final String task = request.getParameter("task");

        if (!(StringUtils.equalsIgnoreCase(task, "download") || StringUtils.equalsIgnoreCase(task,
                "print"))) {
            String selectedAsOfDate = request.getParameter("selectedAsOfDate");
            String profileId = request.getParameter("profileId");
            if (StringUtils.isNotBlank(selectedAsOfDate) && StringUtils.isNotBlank(profileId)) {
                accountForm.setSelectedAsOfDate(selectedAsOfDate);
                accountForm.setProfileId(profileId);
            }
        }

        String participantId = (String) ObjectUtils.defaultIfNull(request
                .getParameter("participantId"), "");
        String profileId = accountForm.getProfileId();

        if (participantId.length() > 0) {
            profileId = ParticipantServiceDelegate.getInstance().getProfileIdByParticipantId(
                    participantId, contractNumber);
            accountForm.setProfileId(profileId);
        }

        if (profileId == null || profileId.length() == 0) {
            StringBuffer message = new StringBuffer("Cannot retrieve profile ID: ");
            if (request.getParameter("participantId") == null) {
                message.append(" participantId is null and reportForm.getProfileId() returns ["
                        + accountForm.getProfileId() + "]");
            } else {
                message.append(" participantId is [" + participantId + "]");
            }

            // THIS CODE WILL probably go away when data problems were fixed.
            errors.add(new GenericException(2261));
            BDSessionHelper.setErrorsInSession(request, errors);
            request.setAttribute("errors", "true");
            //return mapping.findForward(forwardName);
            return forwardName;
        }

        Date date = contract.getContractDates().getAsOfDate();
        if (accountForm.getBaseAsOfDate() == null) {
            accountForm.setBaseAsOfDate(String.valueOf(date.getTime()));
        }

        /*
         * If first time visit need to show default date
         */
        if (task == null) {
            accountForm.setSelectedAsOfDate(String.valueOf(date.getTime()));
        }
        
        
        if(task == null && request.getParameter("lastVisited") == null) {
            // default is asset class
            accountForm.setFundsOrganizedBy(BDConstants.VIEW_BY_ASSET_CLASS);
        }

        Date selectedAsOfDate = null;
        if (accountForm.getSelectedAsOfDate() != null
                && accountForm.getSelectedAsOfDate().trim().length() != 0)
            selectedAsOfDate = new Date(Long.parseLong(accountForm.getSelectedAsOfDate()));
        else
            accountForm.setSelectedAsOfDate(String.valueOf(date.getTime()));

        if (logger.isDebugEnabled()) {
            logger.debug("selectedAsOfDate = " + selectedAsOfDate);
        }

        BDPrincipal bdPrincipal = (BDPrincipal) getUserProfile(request).getAbstractPrincipal();

        // If this request is for NET EE Deferral contributions then retrieve the NetEEDeferrralContributions amount
        boolean getNetEEDeferrals = false;
        String mappedPath = new UrlPathHelper().getPathWithinServletMapping(request);
        if (mappedPath.startsWith(BDConstants.PARTICIPANT_ACCOUNT_NET_DEFERRAL_URL)) {
            try {
                getNetEEDeferrals = !SynchronizationServiceDelegate.getInstance("PS")
                        .isApolloBatchRunning();
            } catch (Exception e) { // default is false, already safe
            }
        }

        boolean organizeFundsByAssetClass = false;
        if (BDConstants.VIEW_BY_ASSET_CLASS.equals(accountForm.getFundsOrganizedBy())) {
            organizeFundsByAssetClass = true;
        }
        // Retrieve the participant related account details.
        participantAccountVO = ParticipantServiceDelegate.getInstance().getParticipantAccount(
                bdPrincipal, contractNumber, productId, profileId, selectedAsOfDate,
                getNetEEDeferrals, organizeFundsByAssetClass);

        // Determine the class type for all the funds
        populateFundClass(participantAccountVO.getOrganizedParticipantFunds(), contract);
        populateFundClass(participantAccountVO.getParticipantFundsByRisk(), contract);

        if (getNetEEDeferrals
                && participantAccountVO.getParticipantAccountDetailsVO().getParticipantDeferralVO() != null) {
            accountForm.calculateDeferralFields(participantAccountVO
                    .getParticipantAccountDetailsVO().getParticipantDeferralVO(), contract);
        }

        if (mappedPath.startsWith(BDConstants.PARTICIPANT_ACCOUNT_NET_DEFERRAL_URL)) {
            if (!participantAccountVO.getParticipantAccountDetailsVO()
                    .isNetEEDeferralContributionsAvailable()
                    || participantAccountVO.getParticipantAccountDetailsVO()
                            .getParticipantDeferralVO() == null) {
                errors.add(new GenericException(BDErrorCodes.TECHNICAL_DIFFICULTIES));
            }
        }

        // If the Participant's Date of Birth is not on file, don't show the age
        if (participantAccountVO.getParticipantAccountDetailsVO().getBirthDate() == null) {
            accountForm.setShowAge(false);
        } else {
            accountForm.setShowAge(true);
        }
        
       // If the Participant's managed account is null or empty, don't show the managed Account
        accountForm.setShowManagedAccount(participantAccountVO.getParticipantAccountDetailsVO().getManagedAccountStatusValue() != null);
        // determine if we should show PBA
        accountForm.setShowPba(false);
        if (contract.isPBA()
                || participantAccountVO.getParticipantAccountDetailsVO()
                        .getPersonalBrokerageAccount() > 0) {
            accountForm.setShowPba(true);
        }
        
        accountForm.setShowNonRothHeader(false);
        if(participantAccountVO.isNonRothMoneyTypeInd())
        {
        	accountForm.setShowNonRothHeader(true);
        }
        accountForm.setShowRothHeader(false);
        if(participantAccountVO.isRothMoneyTypeInd())
        {
        	accountForm.setShowRothHeader(true);
        }
        if(participantAccountVO.getRothMoneyTypeCount()!= 0)
        {
        	if(participantAccountVO.getRothMoneyTypeCount()>=2)
        	{
        		accountForm.setShowMultileRothFootnote(true);
        	}
        }

        // determine if we should show Loan
        if (participantAccountVO.getParticipantAccountDetailsVO().getLoanAssets() == 0) {
            accountForm.setShowLoans(false);
        } else {
            accountForm.setShowLoans(true);
        }

        // determine if we should show Lifecycle
        accountForm.setShowLifecycle(false);
        accountForm.setShowLifecycle(contract.getHasLifecycle()
                || participantAccountVO.getAssetsByRisk().getTotalAssetsByRisk(
                        FundVO.RISK_LIFECYCLE) > 0);

        // loan details drop down
        Collection<ParticipantLoanDetails> loans = participantAccountVO.getLoanDetailsCollection();
        accountForm.setLoanDetailList(loans);

        if (loans.size() == 1) {
            Iterator<ParticipantLoanDetails> loansIt = loans.iterator();
            if (loansIt.hasNext()) {
                ParticipantLoanDetails loanDetails = (ParticipantLoanDetails) loansIt.next();
                accountForm.setSelectedLoan(loanDetails.getLoanId());
            }
        }

        accountForm.setHasInvestments(participantAccountVO.getHasInvestments());

        // prepare the pie chart bean
        pieChartBean = getAssetAllocationByRiskPieChartBean(participantAccountVO.getAssetsByRisk());

        // Validation
        boolean displayTabs = true;
        ArrayList<GenericException> warning = new ArrayList<GenericException>();
        if (!accountForm.isAsOfDateCurrent()) {
            warning.add(new GenericException(
                    BDErrorCodes.PARTICIPANT_ACCOUNT_MONEY_TYPE_REPORTS_NOT_AVAILABLE));
            displayTabs = false;
        }

        if (!accountForm.getHasInvestments()) {
            warning.add(new GenericException(BDErrorCodes.PARTICIPANT_ACCOUNT_NO_PARTICIPANTS));
        }

        boolean displayAfterTaxMoneyTab = true;
        if (participantAccountVO.getNetContribEarningsDetailsCollection() == null
                || participantAccountVO.getNetContribEarningsDetailsCollection().isEmpty()) {
            displayAfterTaxMoneyTab = false;
        }

        if (!contract.isDefinedBenefitContract()) {
            /*
             * Participant Account page tab navigation
             */
            generateParticipantAccountMenu(request, displayTabs, displayAfterTaxMoneyTab);
        } else {
            /*
             * defined benefit page tab navigation
             */
            generateDefinedBenefitAccountMenu(request, displayTabs);
        }

        /**
         * Display Gifl footnote if GIFL investment category is available in the list only for Participant Summary & money type details 
         */
        accountForm.setShowGiflFootnote(false); 
        //determine whether GIFL footnote should be displayed
		if (mappedPath.startsWith(BDConstants.PARTICIPANT_ACCOUNT_URL)
				|| mappedPath.startsWith(
		                BDConstants.PARTICIPANT_ACCOUNT_MONEY_TYPE_DETAILS_URL)) {
		        if(participantAccountVO!=null && 
		        		participantAccountVO.getParticipantFundsByRisk()!=null && !organizeFundsByAssetClass){
				        	for(int riskcat = 0; riskcat < participantAccountVO.getParticipantFundsByRisk().length;riskcat++){
				        		InvestmentOptionVO investmentOptionVO = participantAccountVO.getParticipantFundsByRisk()[riskcat];
					        		if(investmentOptionVO!=null && 
					        				FundVO.RISK_GIFL.equals(investmentOptionVO.getCategory().getCategoryCode()) &&
					        					investmentOptionVO.getParticipantFundSummaryArray()!=null && 
					        						investmentOptionVO.getParticipantFundSummaryArray().length > 0){
					        							accountForm.setShowGiflFootnote(true);
					        							break;
					        		}
				        	}
		        }
		}        
        
        // Setting participant gifl indicator in the bob context for the
		// selected participant. This is used in
		// ContractNavigationGenerator.java to display the gifl link
		// in the contract navigation tab.
		if (participantAccountVO != null
				&& participantAccountVO.getParticipantAccountDetailsVO() != null) {
			BobContextUtils.setParticipantGiflInd(participantAccountVO
					.getParticipantAccountDetailsVO()
					.getParticipantGIFLIndicator(), request);
		}

        // Set the value objects in the request
        request.setAttribute("account", participantAccountVO);
        request.setAttribute("details", participantAccountVO.getParticipantAccountDetailsVO());
        request.setAttribute("assets", participantAccountVO.getAssetsByRisk());
        request.setAttribute("organizedFunds", participantAccountVO.getOrganizedParticipantFunds());
        request.setAttribute("pieChartBean", pieChartBean);
        request.setAttribute("bdMessages", warning);
        request.setAttribute("bdErrors", errors);
        //Pentest changes
        ParticipantAccountVO tempParticipantAccount = (ParticipantAccountVO) request.getSession().getAttribute("account");
        if(tempParticipantAccount != null){
        	request.getSession().removeAttribute("account");
        }
        request.getSession().setAttribute("account", participantAccountVO);

        if (task != null && task.equalsIgnoreCase("download")) {
            populateDownloadData(participantAccountVO, accountForm, request, response);
            return null;
        }
        populateCappingCriteria(null, accountForm, participantAccountVO);
        if (task != null && task.equalsIgnoreCase("printPDF")) {
            doPrintPDF(accountForm, null, participantAccountVO, request, response);
            return null;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doExecute");
        }
        // return mapping.findForward(forwardName);
        return forwardName;
    }

    /**
     * Returns a pie chart bean for the asset allocation by risk category chart.
     * 
     * @param vo The contract snapshot value object.
     * @return A pie chart bean for the asset allocation by risk category chart.
     */
    private PieChartBean getAssetAllocationByRiskPieChartBean(
            ParticipantAccountAssetsByRiskVO partRiskVO) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getAssetAllocationByRiskPieChartBean");
        }

        if (partRiskVO == null) {
            return null;
        }

        PieChartBean pieChart = new PieChartBean();
        pieChart.setAppletArchive(BDConstants.PIE_CHART_APPLET_ARCHIVE);
        pieChart.setPresentationModel(PieChartBean.PRESENTATION_MODEL_SERVLET);
        pieChart.setBorderColor(BDConstants.AssetAllocationPieChart.COLOR_BORDER);
        pieChart.setHexBackgroundColor("#E2E4E5");
        pieChart.setShowWedgeLabels(true);
        pieChart.setUsePercentsAsWedgeLabels(true);
        pieChart.setPieStyle(PieChartBean.PIE_STYLE_FLAT);
        pieChart.setBorderWidth((float) 3.5);
        pieChart.setWedgeLabelOffset(75);
        pieChart.setFontSize(15);
        pieChart.setFontBold(true);
        pieChart.setDrawBorders(true);
        pieChart.setAppletWidth(160);
        pieChart.setAppletHeight(170);
        pieChart.setPieWidth(150);

        for (int i = 0; i < BDConstants.RISK_GROUPS.length; i++) {
            String labelColor = BDConstants.AssetAllocationPieChart.COLOR_WEDGE_LABEL;
            if (BDConstants.AssetAllocationPieChart.COLOR_WEDGES[i] == BDConstants.AssetAllocationPieChart.COLOR_GROWTH_INCOME) {
                labelColor = BDConstants.AssetAllocationPieChart.COLOR_WEDGE_LABEL_DARK;
            }

            pieChart.addPieWedge("wedge" + (i + 1), (float) partRiskVO
                    .getTotalAssetsByRisk(BDConstants.RISK_GROUPS[i]),
                    BDConstants.AssetAllocationPieChart.COLOR_WEDGES[i], " ",
                    String.valueOf(i + 1), labelColor, 0);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("entry <- getAssetAllocationByRiskPieChartBean");
        }
        return pieChart;
    }

    /**
     * Generates the CSV data
     * 
     * @param participantAccountVO
     * @param form
     * @param request
     * @param response
     * @throws SystemException
     */
    protected void populateDownloadData(ParticipantAccountVO participantAccountVO,
            ParticipantAccountForm form, HttpServletRequest request, HttpServletResponse response)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateDownloadData");
        }

        BobContext bobContext = getBobContext(request);

        boolean showLoans = form.getShowLoans();
        boolean showAge = form.getShowAge();
        boolean isAsOfDateCurrent = form.isAsOfDateCurrent();
        boolean showPBA = bobContext.getCurrentContract().isPBA();
        boolean hasContractGatewayInd = bobContext.getCurrentContract().getHasContractGatewayInd();
        StringBuffer buff = new StringBuffer(255);

        Contract currentContract = bobContext.getCurrentContract();
        buff.append("Contract").append(COMMA).append(currentContract.getContractNumber()).append(
                COMMA).append(currentContract.getCompanyName()).append(LINE_BREAK);

        SimpleDateFormat dateFormatter = new SimpleDateFormat(RenderConstants.MEDIUM_MDY_SLASHED);

        Date asOfDate = new Date(Long.parseLong(form.getSelectedAsOfDate()));

        // Section 1 as of date for the report
        buff.append("As of,").append(dateFormatter.format(asOfDate)).append(LINE_BREAK).append(
                LINE_BREAK);

        // Section 2 participant details for the report
        buff.append("Last name,First name,SSN,Date of Birth,");
        if (showAge) {
            buff.append("Age,");
        }

        if (isAsOfDateCurrent) {
            buff.append("Default date of birth,");
        }
        if (isAsOfDateCurrent) {
            buff.append("Employment status,Employment status effective date,Contribution status,");		//CL 110234
        }
        buff.append(LINE_BREAK);

        ParticipantAccountDetailsVO detailsVO = participantAccountVO
                .getParticipantAccountDetailsVO();

        buff.append(QUOTE).append(detailsVO.getLastName()).append(QUOTE).append(COMMA);
        buff.append(QUOTE).append(detailsVO.getFirstName()).append(QUOTE).append(COMMA);
        buff.append(SSNRender.format(detailsVO.getSsn(), null)).append(COMMA);
        buff.append(
                detailsVO.getBirthDate() == null ? "1/1/1980" : dateFormatter.format(detailsVO
                        .getBirthDate())).append(COMMA);

        if (showAge) {
            buff.append(detailsVO.getAge()).append(COMMA);
        }

        if (isAsOfDateCurrent) {
            buff.append(detailsVO.getBirthDate() == null ? "Yes" : "No").append(COMMA);
        }

        if (isAsOfDateCurrent) {
        	//CL 110234 Begin
			if(detailsVO.getEmploymentStatus()!= null){
				buff.append(detailsVO.getEmploymentStatus()).append(COMMA);
			}else{
				buff.append(COMMA);
			}
			if(detailsVO.getEffectiveDate()!= null){
				buff.append(detailsVO.getEffectiveDate()).append(COMMA);
			}else{
				buff.append(COMMA);
			}
			//CL 110234 End
            buff.append(detailsVO.getEmployeeStatus()).append(COMMA);
        }
        buff.append(LINE_BREAK);

        buff.append("Total assets,");
        buff.append(DOLLAR_SIGN).append(detailsVO.getTotalAssets()).append(LINE_BREAK);
        buff.append("Allocated assets,");
        buff.append(DOLLAR_SIGN).append(detailsVO.getAllocatedAssets());

        if ("YES".equals(detailsVO.getShowLoanFeature())) {
            buff.append(LINE_BREAK);
            buff.append("Loan assets,");
            buff.append(DOLLAR_SIGN).append(detailsVO.getLoanAssets());
        }

        if (showPBA) {
            buff.append(LINE_BREAK);
            buff.append("Personal brokerage account,");
            buff.append(DOLLAR_SIGN).append(detailsVO.getPersonalBrokerageAccount());
        }
        // GIFL 1C Starts
        if (isAsOfDateCurrent && hasContractGatewayInd) {
            buff.append(LINE_BREAK);
            buff.append("Guaranteed Income feature,");
            buff.append(participantAccountVO.getParticipantAccountDetailsVO()
                    .getParticipantGIFLIndicatorAsSelect());
        }
        // GIFL 1C Ends
		// MA for SIP
		if (isAsOfDateCurrent && (detailsVO.getManagedAccountStatusValue() != null)) {
			buff.append(LINE_BREAK);
			buff.append("Managed Accounts,");
			buff.append(detailsVO.getManagedAccountStatusValue());
		}
        
        String investInstTypeDes="";
        if (isAsOfDateCurrent) {
            buff.append(LINE_BREAK);
            buff.append("Investment Instruction Type,");
            if(detailsVO.getInvestmentInstructionType() != null)
            {
            	if("TR".equalsIgnoreCase(detailsVO.getInvestmentInstructionType()))
            	{
            		investInstTypeDes = "TR - Instructions were provided by Trustee - Mapped";
            	}
            	else if("PA".equalsIgnoreCase(detailsVO.getInvestmentInstructionType()))
            	{
            		investInstTypeDes = "PA - Participant Provided";
            	}
            	else if("PR".equalsIgnoreCase(detailsVO.getInvestmentInstructionType()))
            	{
            		investInstTypeDes = "PR " + "- Instructions prorated - participant instructions incomplete / incorrect";
            	}
            	else if("DF".equalsIgnoreCase(detailsVO.getInvestmentInstructionType()))
            	{
            		investInstTypeDes = "DF - Default investment option was used";
            	} 
            	else if("MA".equalsIgnoreCase(detailsVO.getInvestmentInstructionType()))
            	{
            		investInstTypeDes = "MA - Managed Accounts";
            	}   
            }
            buff.append(investInstTypeDes).append(LINE_BREAK);
            buff.append("Last contribution date,");
            if (detailsVO.getLastContributionDate() != null) {
                buff.append(dateFormatter.format(detailsVO.getLastContributionDate())).append(
                        LINE_BREAK);
            } else {
                buff.append("none").append(LINE_BREAK);
            }
            buff.append("Automated Rebalance?,");
            buff.append(detailsVO.getAutomaticRebalanceIndicator()).append(LINE_BREAK);
            ;
            if (detailsVO.getRothFirstDepositYear() != 9999) {
                buff.append("Year of first Roth contribution,");
                buff.append(detailsVO.getRothFirstDepositYear());
            }
        }
        buff.append(LINE_BREAK);

        // Section 3 allocated assets for the report
        ParticipantAccountAssetsByRiskVO assetsByRiskVO = participantAccountVO.getAssetsByRisk();

        if (assetsByRiskVO != null) {
            buff.append(LINE_BREAK).append(LINE_BREAK);
            if (bobContext.getCurrentContract().getHasLifecycle()
                    || assetsByRiskVO.getTotalAssetsByRisk(RISK_LIFECYCLE) > 0) {

                buff.append("Target Date,").append(DOLLAR_SIGN).append(
                        assetsByRiskVO.getTotalAssetsByRisk(RISK_LIFECYCLE)).append(COMMA).append(
                        Math.round(HUNDRED
                                * assetsByRiskVO.getPercentageTotalByRisk(RISK_LIFECYCLE))).append(
                        PERCENTAGE_SIGN).append(LINE_BREAK);
            }

            buff.append("Aggressive,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(RISK_AGGRESIVE)).append(COMMA).append(
                    Math.round(HUNDRED * assetsByRiskVO.getPercentageTotalByRisk(RISK_AGGRESIVE)))
                    .append(PERCENTAGE_SIGN).append(LINE_BREAK);

            buff.append("Growth,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(RISK_GROWTH)).append(COMMA).append(
                    Math.round(HUNDRED * assetsByRiskVO.getPercentageTotalByRisk(RISK_GROWTH)))
                    .append(PERCENTAGE_SIGN).append(LINE_BREAK);

            buff.append("Growth & income,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(RISK_GROWTH_INCOME)).append(COMMA).append(
                    Math.round(HUNDRED
                            * assetsByRiskVO.getPercentageTotalByRisk(RISK_GROWTH_INCOME))).append(
                    PERCENTAGE_SIGN).append(LINE_BREAK);

            buff.append("Income,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(RISK_INCOME)).append(COMMA).append(
                    Math.round(HUNDRED * assetsByRiskVO.getPercentageTotalByRisk(RISK_INCOME)))
                    .append(PERCENTAGE_SIGN).append(LINE_BREAK);

            buff.append("Conservative,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(RISK_CONSERVATIVE)).append(COMMA).append(
                    Math
                            .round(HUNDRED
                                    * assetsByRiskVO.getPercentageTotalByRisk(RISK_CONSERVATIVE)))
                    .append(PERCENTAGE_SIGN).append(LINE_BREAK);

            if (showPBA) {
                buff.append("Personal brokerage account,").append(DOLLAR_SIGN).append(
                        assetsByRiskVO.getTotalAssetsByRisk(RISK_PBA)).append(COMMA).append(
                        Math.round(HUNDRED * assetsByRiskVO.getPercentageTotalByRisk(RISK_PBA)))
                        .append(PERCENTAGE_SIGN).append(LINE_BREAK);
            }

            buff.append(LINE_BREAK);
        }

        buff.append(populateDetailedDownloadData(participantAccountVO, form, request));

        String fileName = getFileName(form);
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache");
        response.setContentType(BDReportController.CSV_TEXT);
        response.setHeader(BDReportController.CONTENT_DISPOSITION_TEXT, BDReportController.ATTACHMENT_TEXT
                + fileName);

        try {
            response.getOutputStream().println(buff.toString());
        } catch (IOException ioe) {
            SystemException se = new SystemException(ioe, this.getClass().getName(),
                    "populateDownloadData",
                    "populateDownloadData caught Exception writing csv data to ouput stream.");
            throw se;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- populateDownloadData");
        }
    }

    /**
     * @See BDPdfAction#prepareXMLFromReport()
     */
    @Override
    public Document prepareXMLFromReport(BaseForm reportForm, Object reportData,
            ParticipantAccountVO participantAccountVO, HttpServletRequest request)
            throws ParserConfigurationException {

        PDFDocument doc = new PDFDocument();
        ParticipantAccountDetailsVO detailsVO = participantAccountVO
                .getParticipantAccountDetailsVO();
        ParticipantAccountAssetsByRiskVO assetsByRiskVO = participantAccountVO.getAssetsByRisk();
        ParticipantAccountForm accountForm = (ParticipantAccountForm) reportForm;
        Element rootElement = doc.createRootElement(BDPdfConstants.PPT_ACCOUNT);
        Element reportSummaryElement = doc.createElement(BDPdfConstants.REPORT_SUMMARY_DETAIL);
        prepareXMLFromReportCommon(accountForm, detailsVO, assetsByRiskVO, request,
                reportSummaryElement, doc);

        if (detailsVO != null) {
            String ssnString = SSNRender.format(detailsVO.getSsn(), BDConstants.DEFAULT_SSN_VALUE);
            doc.appendTextNode(reportSummaryElement, BDPdfConstants.PPT_SSN, ssnString);

            Date dob = detailsVO.getBirthDate();
            doc.appendTextNode(reportSummaryElement, BDPdfConstants.DATE_OF_BIRTH, DateRender
                    .formatByPattern(dob, DEFAULT_BIRTH_DATE, RenderConstants.MEDIUM_MDY_SLASHED));

            if (accountForm.getShowAge()) {
                doc.appendTextNode(reportSummaryElement, BDPdfConstants.AGE, String
                        .valueOf(detailsVO.getAge()));
            }
            if (accountForm.isAsOfDateCurrent()) {
            	//CL 110234 Begin
    			if(detailsVO.getEmploymentStatus()!= null){
                    doc.appendTextNode(reportSummaryElement, BDPdfConstants.EMPLOYMENT_STATUS, 
                    		detailsVO.getEmploymentStatus()+"    ");
    			}else{
                    doc.appendTextNode(reportSummaryElement, BDPdfConstants.EMPLOYMENT_STATUS, "");
    			}
    			if(detailsVO.getEffectiveDate()!= null){
                    doc.appendTextNode(reportSummaryElement, BDPdfConstants.EMPLOYMENT_STATUS_EFFECTIVE_DATE,
                    		detailsVO.getEffectiveDate());
    			}else{
                    doc.appendTextNode(reportSummaryElement, BDPdfConstants.EMPLOYMENT_STATUS_EFFECTIVE_DATE, "");
    			}
            	//CL 110234 End
            	doc.appendTextNode(reportSummaryElement, BDPdfConstants.STATUS, detailsVO
                        .getEmployeeStatus());
                if (getBobContext(request).getCurrentContract().getHasContractGatewayInd()) {
                    doc.appendTextNode(reportSummaryElement, BDPdfConstants.PPT_GIFL_SELECT,
                            detailsVO.getParticipantGIFLIndicatorAsSelect());
                } else {
                    if (detailsVO.getParticipantGIFLIndicator() != null) {
                        doc.appendTextNode(reportSummaryElement, BDPdfConstants.PPT_GIFL_SELECT,
                                detailsVO.getParticipantGIFLIndicatorAsSelect());
                    }
                }
                // SIP Managed Account
				if (detailsVO.getManagedAccountStatusValue() != null) {
					doc.appendTextNode(reportSummaryElement, BDPdfConstants.MANAGED_ACCOUNT,
							detailsVO.getManagedAccountStatusValue());
				} 

                doc.appendTextNode(reportSummaryElement, BDPdfConstants.INVESTMENT_INSTRUCTION_TYPE,
                        detailsVO.getInvestmentInstructionType());
                Date lastContribDate = detailsVO.getLastContributionDate();
                doc.appendTextNode(reportSummaryElement, BDPdfConstants.LAST_CONTRIB_DATE,
                        DateRender.formatByPattern(lastContribDate, null,
                                RenderConstants.MEDIUM_MDY_SLASHED));

                doc.appendTextNode(reportSummaryElement, BDPdfConstants.AUTO_REBALANCE_IND,
                        detailsVO.getAutomaticRebalanceIndicator());

                if (!(detailsVO.getRothFirstDepositYear() == 9999)) {
                    doc.appendTextNode(reportSummaryElement,
                            BDPdfConstants.ROTH_FIRST_DEPOSIT_YEAR, String.valueOf(detailsVO
                                    .getRothFirstDepositYear()));
                }
            }

            if (accountForm.getShowPba()) {
                String pbaAccount = NumberRender.formatByType(detailsVO
                        .getPersonalBrokerageAccount(), null, RenderConstants.CURRENCY_TYPE);
                doc.appendTextNode(reportSummaryElement, BDPdfConstants.PBA_ACCOUNT, pbaAccount);
            }
            if (STRING_YES.equalsIgnoreCase(detailsVO.getShowLoanFeature())) {
                String loanAssets = NumberRender.formatByType(detailsVO.getLoanAssets(), null,
                        RenderConstants.CURRENCY_TYPE);
                doc.appendTextNode(reportSummaryElement, BDPdfConstants.LOAN_ASSETS, loanAssets);
            }
        }

        String totalAssetsByRisk = getTotalAssetsByRisk(assetsByRiskVO,
                Fund.RISK_CATEGORY_CODE_PERSONAL_BROKER_ACCOUNT);
        String percentageTotalByRisk = getTotalPercentByRisk(assetsByRiskVO,
                Fund.RISK_CATEGORY_CODE_PERSONAL_BROKER_ACCOUNT);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.TOTAL_ASSETS_PB, totalAssetsByRisk);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.TOTAL_PERCENT_PB,
                percentageTotalByRisk);

        doc.appendElement(rootElement, reportSummaryElement);

        Element messageElement;
        int msgNum = 0;
        String message;
        Element infoMessagesElement = doc.createElement(BDPdfConstants.INFO_MESSAGES);
        if (!accountForm.isAsOfDateCurrent()) {
            messageElement = doc.createElement(BDPdfConstants.MESSAGE);
            message = ContentHelper
                    .getContentTextWithParamsSubstitution(
                            BDContentConstants.MISCELLANEOUS_PARTICIPANT_ACCOUNT_MONEY_TYPE_REPORTS_NOT_AVAILABLE,
                            ContentTypeManager.instance().MESSAGE, null, DateRender
                                    .formatByPattern(getBobContext(request).getCurrentContract()
                                            .getContractDates().getAsOfDate(), null,
                                            RenderConstants.MEDIUM_MDY_SLASHED));
            doc
                    .appendTextNode(messageElement, BDPdfConstants.MESSAGE_NUM, String
                            .valueOf(++msgNum));
            PdfHelper.convertIntoDOM(BDPdfConstants.MESSAGE_TEXT, messageElement, doc, message);
            doc.appendElement(infoMessagesElement, messageElement);
        }

        if (!accountForm.getHasInvestments()) {
            messageElement = doc.createElement(BDPdfConstants.MESSAGE);
            message = ContentHelper.getContentText(
                    BDContentConstants.MESSAGE_PARTICIPANT_ACCOUNT_NO_PARTICIPANTS,
                    ContentTypeManager.instance().MESSAGE, null);
            doc
                    .appendTextNode(messageElement, BDPdfConstants.MESSAGE_NUM, String
                            .valueOf(++msgNum));
            PdfHelper.convertIntoDOM(BDPdfConstants.MESSAGE_TEXT, messageElement, doc, message);
            doc.appendElement(infoMessagesElement, messageElement);
        }
        doc.appendElement(rootElement, infoMessagesElement);

        prepareDetailedXMLFromReport(participantAccountVO, accountForm, doc, rootElement, request);
        return doc.getDocument();
    }

    /**
     * This method creates XML elements common for both Participant Account and DB Account reports
     * 
     * @param accountForm
     * @param participantAccountVO
     * @param request
     * @param rootElement
     * @param doc
     * @return Object[]
     * @throws ContentException
     * @throws ParserConfigurationException
     */
    protected void prepareXMLFromReportCommon(ParticipantAccountForm accountForm,
            ParticipantAccountDetailsVO detailsVO, ParticipantAccountAssetsByRiskVO assetsByRiskVO,
            HttpServletRequest request, Element reportSummaryElement, PDFDocument doc)
            throws ParserConfigurationException {

        // Summary Info - start

        String portNumber = System.getProperty("webcontainer.http.port") == null ? "9081" : System.getProperty("webcontainer.http.port");
        String baseURI = "http://localhost:" + portNumber;

        pieChartBean.setHexBackgroundColor("#ECEAE3");
        String fileName = PieChartUtil.createURLStringFOP(pieChartBean);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.PIE_CHART_URL, baseURI
                + fileName.replaceAll(PdfConstants.AMPERSAND, CommonConstants.AMPERSAND_SYMBOL));

        if (detailsVO != null) {
            doc.appendTextNode(reportSummaryElement, BDPdfConstants.FIRST_NAME, detailsVO
                    .getFirstName());
            doc.appendTextNode(reportSummaryElement, BDPdfConstants.LAST_NAME, detailsVO
                    .getLastName());

            String totalAssets = NumberRender.formatByType(detailsVO.getTotalAssets(), null,
                    RenderConstants.CURRENCY_TYPE);
            String allocatedAssets = NumberRender.formatByType(detailsVO.getAllocatedAssets(),
                    null, RenderConstants.CURRENCY_TYPE);

            doc.appendTextNode(reportSummaryElement, BDPdfConstants.TOTAL_ASSETS, totalAssets);
            doc.appendTextNode(reportSummaryElement, BDPdfConstants.ALLOCATED_ASSETS,
                    allocatedAssets);

            if (accountForm.isAsOfDateCurrent()) {
                Date lastContribDate = detailsVO.getLastContributionDate();
                doc.appendTextNode(reportSummaryElement, BDPdfConstants.LAST_CONTRIB_DATE,
                        DateRender.formatByPattern(lastContribDate, BDConstants.NOT_APPLICABLE,
                                RenderConstants.MEDIUM_MDY_SLASHED));
            }
        }

        String totalAssetsByRisk, percentageTotalByRisk;
        if (accountForm.getShowLifecycle()) {
            totalAssetsByRisk = getTotalAssetsByRisk(assetsByRiskVO,
                    Fund.RISK_CATEGORY_CODE_LIFECYCLE);
            percentageTotalByRisk = getTotalPercentByRisk(assetsByRiskVO,
                    Fund.RISK_CATEGORY_CODE_LIFECYCLE);
            doc.appendTextNode(reportSummaryElement, BDPdfConstants.TOTAL_ASSETS_LC,
                    totalAssetsByRisk);
            doc.appendTextNode(reportSummaryElement, BDPdfConstants.TOTAL_PERCENT_LC,
                    percentageTotalByRisk);
            doc.appendTextNode(reportSummaryElement, BDPdfConstants.COLOR_LC,
                    BDConstants.AssetAllocationPieChart.COLOR_LIFECYCLE);
        }

        doc.appendTextNode(reportSummaryElement, BDPdfConstants.COLOR_AG,
                BDConstants.AssetAllocationPieChart.COLOR_AGRESSIVE);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.COLOR_GR,
                BDConstants.AssetAllocationPieChart.COLOR_GROWTH);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.COLOR_GI,
                BDConstants.AssetAllocationPieChart.COLOR_GROWTH_INCOME);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.COLOR_IN,
                BDConstants.AssetAllocationPieChart.COLOR_INCOME);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.COLOR_CN,
                BDConstants.AssetAllocationPieChart.COLOR_CONSERVATIVE);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.COLOR_PB,
                BDConstants.AssetAllocationPieChart.COLOR_PBA);

        totalAssetsByRisk = getTotalAssetsByRisk(assetsByRiskVO, Fund.RISK_CATEGORY_CODE_AGGRESSIVE);
        percentageTotalByRisk = getTotalPercentByRisk(assetsByRiskVO,
                Fund.RISK_CATEGORY_CODE_AGGRESSIVE);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.TOTAL_ASSETS_AG, totalAssetsByRisk);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.TOTAL_PERCENT_AG,
                percentageTotalByRisk);

        totalAssetsByRisk = getTotalAssetsByRisk(assetsByRiskVO, Fund.RISK_CATEGORY_CODE_GROWTH);
        percentageTotalByRisk = getTotalPercentByRisk(assetsByRiskVO,
                Fund.RISK_CATEGORY_CODE_GROWTH);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.TOTAL_ASSETS_GR, totalAssetsByRisk);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.TOTAL_PERCENT_GR,
                percentageTotalByRisk);

        totalAssetsByRisk = getTotalAssetsByRisk(assetsByRiskVO,
                Fund.RISK_CATEGORY_CODE_GROWTH_AND_INCOME);
        percentageTotalByRisk = getTotalPercentByRisk(assetsByRiskVO,
                Fund.RISK_CATEGORY_CODE_GROWTH_AND_INCOME);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.TOTAL_ASSETS_GI, totalAssetsByRisk);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.TOTAL_PERCENT_GI,
                percentageTotalByRisk);

        totalAssetsByRisk = getTotalAssetsByRisk(assetsByRiskVO, Fund.RISK_CATEGORY_CODE_INCOME);
        percentageTotalByRisk = getTotalPercentByRisk(assetsByRiskVO,
                Fund.RISK_CATEGORY_CODE_INCOME);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.TOTAL_ASSETS_IN, totalAssetsByRisk);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.TOTAL_PERCENT_IN,
                percentageTotalByRisk);

        totalAssetsByRisk = getTotalAssetsByRisk(assetsByRiskVO,
                Fund.RISK_CATEGORY_CODE_CONSERVATIVE);
        percentageTotalByRisk = getTotalPercentByRisk(assetsByRiskVO,
                Fund.RISK_CATEGORY_CODE_CONSERVATIVE);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.TOTAL_ASSETS_CN, totalAssetsByRisk);
        doc.appendTextNode(reportSummaryElement, BDPdfConstants.TOTAL_PERCENT_CN,
                percentageTotalByRisk);

        // Summary Info - end

    }

    /**
     * Returns total assets by risk
     * 
     * @param assetsByRiskVO
     * @param riskCode
     * @return totalAssetsByRisk
     */
    private String getTotalAssetsByRisk(ParticipantAccountAssetsByRiskVO assetsByRiskVO,
            String riskCode) {
        String totalAssetsByRisk = null;
        if (assetsByRiskVO != null) {
            totalAssetsByRisk = NumberRender.formatByType(assetsByRiskVO
                    .getTotalAssetsByRisk(riskCode), null, RenderConstants.CURRENCY_TYPE);
        }
        return totalAssetsByRisk;
    }

    /**
     * Returns total percent by risk
     * 
     * @param assetsByRiskVO
     * @param riskCode
     * @return totalPercentByRisk
     */
    private String getTotalPercentByRisk(ParticipantAccountAssetsByRiskVO assetsByRiskVO,
            String riskCode) {
        String totalPercentByRisk = null;
        if (assetsByRiskVO != null) {
            totalPercentByRisk = NumberRender.formatByPattern(assetsByRiskVO
                    .getPercentageTotalByRisk(riskCode), DEFAULT_PERCENT, PERCENT_PATTERN);
        }
        return totalPercentByRisk;
    }

    /**
     * Resets the form
     * 
     * @param mapping
     * @param reportForm
     * @param request
     * 
     * @return BaseReportForm
     * 
     * @throws SystemException
     */
    protected BaseReportForm resetForm(
            BaseReportForm reportForm, HttpServletRequest request) throws SystemException {
        return reportForm;
    }

    /**
     * This method is to generate the page specific csv file
     * 
     * @param participantAccountVO
     * @param form
     * @return String
     */
    protected abstract String populateDetailedDownloadData(
            ParticipantAccountVO participantAccountVO, ParticipantAccountForm form,
            HttpServletRequest request);

    /**
     * * This method is to populate page specific XML data for PDF generation
     * 
     * @param participantAccountVO
     * @param form
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param request
     * @return
     * @throws ContentException
     * @throws ParserConfigurationException
     */
    protected abstract Document prepareDetailedXMLFromReport(
            ParticipantAccountVO participantAccountVO, ParticipantAccountForm form,
            PDFDocument doc, Element rootElement, HttpServletRequest request)
            throws ParserConfigurationException;

    /**
     * Returns the tab name for the .csv file name
     * 
     * @return String - tab name
     */
    protected abstract String getTabName();

    /**
     * Generates the file name for the download report (CSV file)
     * 
     * @param form ParticipantAccountForm object
     * 
     * @return String The file name
     */
    protected String getFileName(ParticipantAccountForm form) {

        String fileName = null;
        String dateString = null;
        Long selectedAsOfDate = new Long(form.getSelectedAsOfDate());

        synchronized (DATE_FORMATTER) {
            dateString = DATE_FORMATTER.format(new Date(selectedAsOfDate));
        }

        fileName = BDConstants.PARTICIPANT_ACCOUNT_CSV_NAME + "-" + getTabName() + "-" + dateString
                + CSV_EXTENSION;

        return fileName;
    }

    /**
     * @See BDPdfAction#populateCappingCriteria()
     */
    @Override
    protected void populateCappingCriteria(Object reportData, BaseForm form,
            ParticipantAccountVO participantAccountVO) {

        ParticipantAccountForm accountForm = (ParticipantAccountForm) form;
        accountForm.setPdfCapped(false);

        if (getNumberOfRowsInReport(participantAccountVO) > getMaxCappedRowsInPDF()) {
            accountForm.setCappedRowsInPDF(getMaxCappedRowsInPDF());
            accountForm.setPdfCapped(true);
        } else {
            accountForm.setCappedRowsInPDF(getNumberOfRowsInReport(participantAccountVO));
        }

    }

    /**
     * @See BDPdfAction#getXSLTFileName()
     */
    @Override
    protected String getXSLTFileName() {
        String fileName = null;
        fileName = BDConstants.PARTICIPANT_ACCOUNT_CSV_NAME + CommonConstants.HYPHON_SYMBOL
                + getTabName() + XSL_EXTENSION;
        return fileName;
    }

    /**
     * Populates the fund class
     * 
     * @param investmentOptionVO
     * @param contract
     * @throws SystemException
     */
    private void populateFundClass(InvestmentOptionVO[] investmentOptionVO, Contract contract)
            throws SystemException {

        ParticipantFundSummaryVO[] participantFundSummaryVOs = null;

        for (int i = 0; i < investmentOptionVO.length; i++) {
            participantFundSummaryVOs = investmentOptionVO[i].getParticipantFundSummaryArray();

            for (int j = 0; j < participantFundSummaryVOs.length; j++) {

                if (BDConstants.FUND_PACKAGE_MULTICLASS.equalsIgnoreCase(contract
                        .getFundPackageSeriesCode())) {
                    if (StringUtils.isNotBlank(participantFundSummaryVOs[j].getRateType())) {
                        participantFundSummaryVOs[j].setFundClass(FundClassUtility
                                .getInstance().getFundClassMediumName(
                                        participantFundSummaryVOs[j].getRateType()));
                    }
                } else if (BDConstants.BD_PRODUCT_ID.equalsIgnoreCase(contract.getProductId())
                        || BDConstants.BD_PRODUCT_NY_ID.equalsIgnoreCase(contract.getProductId())) {
                    participantFundSummaryVOs[j].setFundClass(FundClassUtility
                            .getInstance().getFundClassMediumName(BDConstants.CL2));
                } else {
                    participantFundSummaryVOs[j].setFundClass(FundClassUtility
                            .getInstance().getFundClassMediumName(BDConstants.CL5));
                }
            }
        }
    }

    /**
     * Generates the user navigation for the Participant Account tab
     * 
     * @param request
     * @throws SystemException
     */
    private void generateParticipantAccountMenu(HttpServletRequest request, boolean displayTabs,
            boolean displayAfterTaxMoneyTab) throws SystemException {

        UserNavigation pptAccountUserNavigation = new UserNavigation();
        ;
        UserMenu pptAccountUserMenu = new UserMenu();

        pptAccountUserMenu.addLevelOneUserMenuItem(new UserMenuItem(
                BDConstants.PARTICIPANT_ACCOUNT_TAB_ID, BDConstants.PARTICIPANT_ACCOUNT_TAB_NAME,
                "/do" + BDConstants.PARTICIPANT_ACCOUNT_URL + "?lastVisited='true'"));

        if (displayTabs) {
            pptAccountUserMenu.addLevelOneUserMenuItem(new UserMenuItem(
                    BDConstants.MONEY_TYPE_SUMMARY_TAB_ID, BDConstants.MONEY_TYPE_SUMMARY_TAB_NAME,
                    "/do" + BDConstants.PARTICIPANT_ACCOUNT_MONEY_TYPE_SUMMARY_URL + "?lastVisited='true'"));

            pptAccountUserMenu.addLevelOneUserMenuItem(new UserMenuItem(
                    BDConstants.MONEY_TYPE_DETAILS_TAB_ID, BDConstants.MONEY_TYPE_DETAILS_TAB_NAME,
                    "/do" + BDConstants.PARTICIPANT_ACCOUNT_MONEY_TYPE_DETAILS_URL + "?lastVisited='true'"));

            if (displayAfterTaxMoneyTab) {
                pptAccountUserMenu.addLevelOneUserMenuItem(new UserMenuItem(
                        BDConstants.AFTER_TAX_MONEY_TAB_ID, BDConstants.AFTER_TAX_MONEY_TAB_NAME,
                        "/do" + BDConstants.PARTICIPANT_ACCOUNT_NET_CONTRIB_EARNINGS_URL + "?lastVisited='true'"));
            }

            pptAccountUserMenu.addLevelOneUserMenuItem(new UserMenuItem(
                    BDConstants.EE_DEFERRALS_TAB_ID, BDConstants.EE_DEFERRALS_TAB_NAME, "/do"
                            + BDConstants.PARTICIPANT_ACCOUNT_NET_DEFERRAL_URL + "?lastVisited='true'"));
        }

        pptAccountUserNavigation.setUserMenu(pptAccountUserMenu);

        request.getSession().setAttribute(BDConstants.PPT_ACCOUNT_USER_NAVIGATION,
                pptAccountUserNavigation);
    }

    /**
     * Generates the user navigation for the Participant Account tab
     * 
     * @param request
     * @throws SystemException
     */
    private void generateDefinedBenefitAccountMenu(HttpServletRequest request, boolean displayTabs)
            throws SystemException {

        UserNavigation definedBenefitAccountUserNavigation = new UserNavigation();
        UserMenu definedBenefitAccountUserMenu = new UserMenu();

        definedBenefitAccountUserMenu.addLevelOneUserMenuItem(new UserMenuItem(
                BDConstants.DB_ACCOUNT_TAB_ID, BDConstants.DB_ACCOUNT_TAB_NAME, "/do"
                        + BDConstants.DB_ACCOUNT_URL + "?lastVisited='true'"));
        if (displayTabs) {
            definedBenefitAccountUserMenu.addLevelOneUserMenuItem(new UserMenuItem(
                    BDConstants.MONEY_TYPE_SUMMARY_TAB_ID, BDConstants.MONEY_TYPE_SUMMARY_TAB_NAME,
                    "/do" + BDConstants.DB_ACCOUNT_MONEY_TYPE_SUMMARY_URL + "?lastVisited='true'"));

            definedBenefitAccountUserMenu.addLevelOneUserMenuItem(new UserMenuItem(
                    BDConstants.MONEY_TYPE_DETAILS_TAB_ID, BDConstants.MONEY_TYPE_DETAILS_TAB_NAME,
                    "/do" + BDConstants.DB_ACCOUNT_MONEY_TYPE_DETAILS_URL + "?lastVisited='true'"));

        }

        definedBenefitAccountUserNavigation.setUserMenu(definedBenefitAccountUserMenu);
        request.getSession().setAttribute(BDConstants.DB_ACCOUNT_USER_NAVIGATION,
                definedBenefitAccountUserNavigation);
    }

    /**
     * @param String fundClassNumber
     * @return boolean true/false. if valid Fund Class Number then it will return true otherwise false.
     */
    private boolean isValidFundClassNumber(String fundClassNumber) {
        boolean isValidFundClassNumber = false;
        if (StringUtils.isNotBlank(fundClassNumber) && StringUtils.isNumeric(fundClassNumber)) {
            isValidFundClassNumber = true;
        }
        return isValidFundClassNumber;

    }
    
    /**
     * don't want excel to think the , is the next field
     * 
     * @param field
     * @return String
     */
    protected static String escapeField(String field) {
        if (field.indexOf(COMMA) != -1) {
            StringBuffer newField = new StringBuffer();
            newField = newField.append(CommonConstants.DOUBLE_QUOTES).append(field).append(
                    CommonConstants.DOUBLE_QUOTES);
            return newField.toString();
        } else {
            return field;
        }
    }
    
	/*
	 * (non-Javadoc) This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations as part of
	 * the CL#136970.
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAction#doValidate(org
	 * .apache.struts.action.ActionMapping, org.apache.struts.action.Form,
	 * javax.servlet.http.HttpServletRequest)
	 */
/*@SuppressWarnings("rawtypes")
   public Collection doValidate(ActionForm form, HttpServletRequest request) {
   	Collection penErrors = FrwValidator.doValidatePenTestAction(form,  request, BDConstants.INPUT);
   	if (penErrors != null && penErrors.size() > 0) {
   	request.getSession(false).getAttribute("account");
   	ParticipantAccountVO participantAccountVO = (ParticipantAccountVO) request.getSession().getAttribute("account");
       if(participantAccountVO != null){
    	   request.setAttribute("penErrors", true);
       	request.setAttribute("account", participantAccountVO);
       	request.setAttribute("details", participantAccountVO.getParticipantAccountDetailsVO());
       	request.setAttribute("assets", participantAccountVO.getAssetsByRisk());
           request.setAttribute("organizedFunds", participantAccountVO.getOrganizedParticipantFunds());
       }
       if(pieChartBean != null){
       	request.setAttribute("pieChartBean", pieChartBean);
       }
   		return penErrors;
	}
	return super.doValidate( form, request);
}*/
    @Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
	
	 @InitBinder
	    protected void initBinder(HttpServletRequest request,
	    			ServletRequestDataBinder  binder) {
	    	binder.bind(request);
	    	binder.addValidators(bdValidatorFWInput);
	    }
		
	   
	}
   

