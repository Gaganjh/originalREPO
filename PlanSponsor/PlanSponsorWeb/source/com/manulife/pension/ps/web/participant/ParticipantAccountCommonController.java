package com.manulife.pension.ps.web.participant;

import static com.manulife.pension.platform.web.CommonConstants.DOLLAR_SIGN;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.delegate.SynchronizationServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.participant.valueobject.InvestmentOptionVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountAssetsByRiskVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantDeferralVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantLoanDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWParticipantInput;
import com.manulife.pension.ps.web.withdrawal.WithdrawalRequestUi;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.fund.valueobject.FundVO;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.piechart.PieChartBean;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This class is the Base class of new Participant Account actions
 * 
 * @author Ilker Celikyilmaz
 */
public abstract class ParticipantAccountCommonController extends PsController {

    final static String LINE_BREAK = ReportController.LINE_BREAK;

    final static String COMMA = ReportController.COMMA;

    final static String QUOTE = ReportController.QUOTE;

    // protected static final String DOWNLOAD_COLUMN_HEADING_GENERAL = "As of date";

    static final String[] RISK_GROUPS = new String[] { FundVO.RISK_LIFECYCLE,
    		FundVO.RISK_AGGRESIVE, FundVO.RISK_GROWTH, FundVO.RISK_GROWTH_INCOME,
            FundVO.RISK_INCOME, FundVO.RISK_CONSERVATIVE, FundVO.RISK_PBA };

    static final String PIE_CHART_APPLET_ARCHIVE = "/assets/unmanaged/applets/pieChartApplet.jar";

    static final String PARTICIPANT_ACCOUNT_MONEY_TYPE_SUMMARY_URL = "/participant/participantAccountMoneyTypeSummary/";

    static final String PARTICIPANT_ACCOUNT_MONEY_TYPE_DETAILS_URL = "/participant/participantAccountMoneyTypeDetails/";

    static final String PARTICIPANT_ACCOUNT_URL = "/participant/participantAccount/";

    static final String PARTICIPANT_ACCOUNT_NET_DEFERRAL_URL = "/participant/participantAccountNetDeferral/";

    static final String PARTICIPANT_ACCOUNT_NET_CONTRIB_EARNINGS_URL = "/participant/participantAccountNetContribEarnings/";

    // Defined Benefit
    static final String DB_ACCOUNT_URL = "/db/definedBenefitAccount/";

    static final String DB_ACCOUNT_MONEY_TYPE_SUMMARY_URL = "/db/definedBenefitAccountMoneyTypeSummary/";

    static final String DB_ACCOUNT_MONEY_TYPE_DETAILS_URL = "/db/definedBenefitAccountMoneyTypeDetails/";

    static final String DB_RATE_OF_RETURN_CALCULATER_URL = "/db/rateofReturnCalculator/";
    
    static DecimalFormat percentFormatter = new DecimalFormat("#0");

    static final String PERCENT = "%";
    
    //Created a synchronized method to handle decimal formatter. 
    public static synchronized String formatPercentageFormatter(Long value) { 
        return percentFormatter.format(value); 
    }

    public ParticipantAccountCommonController(Class clazz) {
        super(clazz);
    }

    /**
     * @see PsController#doExecute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public String doExecute( ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doExecute");
        }

        List errors = new ArrayList();

        ParticipantAccountForm accountForm = (ParticipantAccountForm) form;
        ParticipantAccountVO participantAccountVO = null;
        PieChartBean pieChartBean = null;

        UserProfile userProfile = getUserProfile(request);
        String mappedPath = new UrlPathHelper().getPathWithinServletMapping(request);
        if (mappedPath.startsWith(PARTICIPANT_ACCOUNT_URL)) {
            
            final String task = request.getParameter("task");
            
            if ("download".equals(task)) {
                
                FunctionalLogger.INSTANCE.log("Download Accounts (Participant)", userProfile, getClass(), task);
                
            } else {
                
                FunctionalLogger.INSTANCE.log("Participant Accounts page", userProfile, getClass(), task);
                
            }
            
        }
        
        int contractNumber = userProfile.getCurrentContract().getContractNumber();
        String productId = userProfile.getCurrentContract().getProductId();
        final ContractInfo contractInfo = WithdrawalServiceDelegate.getInstance().getContractInfo(
                userProfile.getCurrentContract().getContractNumber(), userProfile.getPrincipal());

        // Find the forward
        String forwardName = "participantAccount";
        if (mappedPath.startsWith(PARTICIPANT_ACCOUNT_URL))
            forwardName = "participantAccount";
        else if (mappedPath.startsWith(PARTICIPANT_ACCOUNT_MONEY_TYPE_SUMMARY_URL))
            forwardName = "participantAccountMoneyTypeSummary";
        else if (mappedPath.startsWith(PARTICIPANT_ACCOUNT_MONEY_TYPE_DETAILS_URL))
            forwardName = "participantAccountMoneyTypeDetails";
        else if (mappedPath.startsWith(PARTICIPANT_ACCOUNT_NET_DEFERRAL_URL))
            forwardName = "participantAccountNetDeferral";
        else if (mappedPath.startsWith(PARTICIPANT_ACCOUNT_NET_CONTRIB_EARNINGS_URL))
            forwardName = "participantAccountNetContribEarnings";
        else if (mappedPath.startsWith(DB_ACCOUNT_URL))
            forwardName = "definedBenefitAccount";
        else if (mappedPath.startsWith(DB_ACCOUNT_MONEY_TYPE_SUMMARY_URL))
            forwardName = "definedBenefitAccountMoneyTypeSummary";
        else if (mappedPath.startsWith(DB_ACCOUNT_MONEY_TYPE_DETAILS_URL))
            forwardName = "definedBenefitAccountMoneyTypeDetails";
        else if (mappedPath.startsWith(DB_RATE_OF_RETURN_CALCULATER_URL))
            forwardName = "rateofReturnCalculator";


        // if its a download task its ok to get the data from the session form
        // if its not a download task, i.e. its a regular page display task
        // then we expect to see the parameters fresh from the request
        final String task = request.getParameter("task");
        if (!(StringUtils.equalsIgnoreCase(task, "download") || StringUtils.equalsIgnoreCase(task,
                "print"))) {

            // Not a download action- get parameters from request if they exist
            // Parameters might not be in request if we are returning from a calling page with
            // errors, in which case we use the existing session based parameters
            final String selectedAsOfDate = request.getParameter("selectedAsOfDate");
            final String profileId = request.getParameter("profileId");
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
            SessionHelper.setErrorsInSession(request, errors);
            request.setAttribute("errors", "true");
            //return mapping.findForward(forwardName);
            return forwardName;
        }

        Date date = userProfile.getCurrentContract().getContractDates().getAsOfDate();
        if (accountForm.getBaseAsOfDate() == null) {
            accountForm.setBaseAsOfDate(String.valueOf(date.getTime()));
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

        Principal principal = getUserProfile(request).getPrincipal();

        // If this request is for NET EE Deferral contributions then retrieve the NetEEDeferrralContributions amoumt
        boolean getNetEEDeferrals = false;
        if (mappedPath.startsWith(PARTICIPANT_ACCOUNT_NET_DEFERRAL_URL)) {
            try {
                getNetEEDeferrals = !SynchronizationServiceDelegate.getInstance(
                        Constants.PS_APPLICATION_ID).isApolloBatchRunning();
            } catch (Exception e) { // default is false, already safe
            }
        }
        participantAccountVO = ParticipantServiceDelegate.getInstance().getParticipantAccount(
                principal, contractNumber, productId, profileId, selectedAsOfDate,
                getNetEEDeferrals, false);
        ParticipantDeferralVO deferralVO = participantAccountVO.getParticipantAccountDetailsVO().getParticipantDeferralVO();
        if (deferralVO != null) { // null signifies either getNetEEDeferrals argument false or not able to retrieve deferral data
            accountForm.calculateDeferralFields(
                    deferralVO,
                    getUserProfile(request).getCurrentContract());
        }

        // prepare the pie chart bean
        pieChartBean = getAssetAllocationByRiskPieChartBean(participantAccountVO.getAssetsByRisk());

        // loan details drop down
        Collection loans = participantAccountVO.getLoanDetailsCollection();
        accountForm.setLoanDetailList(loans);

        if (loans.size() == 1) {
            Iterator loansIt = loans.iterator();
            if (loansIt.hasNext()) {
                ParticipantLoanDetails loanDetails = (ParticipantLoanDetails) loansIt.next();
                accountForm.setSelectedLoan(loanDetails.getLoanId());
            }
        }
        
        // requirement SIP
        accountForm.setShowManagedAccount(participantAccountVO.getParticipantAccountDetailsVO().getManagedAccountStatusValue() != null);
        
        // requirement PPR.110
        if (participantAccountVO.getParticipantAccountDetailsVO().getBirthDate() == null) {
            accountForm.setShowAge(false);
        } else {
            accountForm.setShowAge(true);
        }
   
        // requirement PPR.91 and PPR.96 and PPR.97
        if (participantAccountVO.getParticipantAccountDetailsVO().getLoanAssets() == 0) {
            accountForm.setShowLoans(false);
        } else
            accountForm.setShowLoans(true);

        // determine if we should show PBA
        accountForm.setShowPba(false);
        if (userProfile.getCurrentContract().isPBA()
                || participantAccountVO.getParticipantAccountDetailsVO()
                        .getPersonalBrokerageAccount() > 0) {
            accountForm.setShowPba(true);
        }

        // determine if we have NetContribEarnings (after tax money)
        accountForm.setShowNetContribEarnings(false);
        if (participantAccountVO.getNetContribEarningsDetailsCollection().size() > 0) {
            accountForm.setShowNetContribEarnings(true);
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
        

        boolean isParticipantForContract = ContractServiceDelegate.getInstance()
                .existsParticipantForContract(contractNumber, Integer.parseInt(profileId));
        WithdrawalRequestUi.populatePermissions(contractInfo, userProfile.getPrincipal());

        // Determine if we should show create withdrawal request link
        // Set user permissions
        if (!isParticipantForContract) {
            accountForm.setShowCreateWithdrawalRequestLink(false);

        } else {

            accountForm.setShowCreateWithdrawalRequestLink(contractInfo
                    .getShowCreateWithdrawalRequestLink());
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer(
                        "ParticipantAccountCommonAction.doExecute> Loaded contract information [")
                        .append(contractInfo).append(")]").toString());
            }
        }

        // Determine if we should show create loan request link
        final Contract currentContract = userProfile.getCurrentContract();
        LoanSettings loanSettings = LoanServiceDelegate.getInstance().getLoanSettings(
                currentContract.getContractNumber());

        if (!isParticipantForContract) {
            accountForm.setShowLoanCreateLink(false);
        } else {
            accountForm.setShowLoanCreateLink(contractInfo
                    .getShowCreateLoanRequestLink(loanSettings));
            if (logger.isDebugEnabled()) {
                logger.debug(new StringBuffer(
                        "ParticipantAccountCommonAction.doExecute> Loaded contract information [")
                        .append(contractInfo).append(")]").toString());
            }
        }

        // determine if we should show Lifecycle
        accountForm.setShowLifecycle(false);
        accountForm.setShowLifecycle(userProfile.getCurrentContract().getHasLifecycle()
                || participantAccountVO.getAssetsByRisk().getTotalAssetsByRisk(
                        FundVO.RISK_LIFECYCLE) > 0);

        accountForm.setHasInvestments(participantAccountVO.getHasInvestments());
        
        accountForm.setShowGiflFootnote(false);
        //determine whether GIFL footnote should be displayed
        if(participantAccountVO!=null && 
        		participantAccountVO.getParticipantFundsByRisk()!=null){
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
        
        request.setAttribute("account", participantAccountVO);
        request.setAttribute("details", participantAccountVO.getParticipantAccountDetailsVO());
        request.setAttribute("assets", participantAccountVO.getAssetsByRisk());
        request.setAttribute("groups", participantAccountVO.getParticipantFundsByRisk());
        request.setAttribute("pieChartBean", pieChartBean);

        if (task != null && task.equalsIgnoreCase("download")) {
            populateDownloadData(participantAccountVO, accountForm, request, response);
            return null;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doExecute");
        }
       // SessionHelper.setErrorsInSession(request, errors);
        //return mapping.findForward(forwardName);
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

        if (partRiskVO == null)
            return null;
        // setup the layout of the chart
        // width="184" height="235"
        PieChartBean pieChart = new PieChartBean();
        pieChart.setAppletArchive(PIE_CHART_APPLET_ARCHIVE);
        // pieChart.setPresentationModel(PieChartBean.PRESENTATION_MODEL_APPLET);
        pieChart.setPresentationModel(PieChartBean.PRESENTATION_MODEL_SERVLET);
        pieChart.setBorderColor(Constants.AssetAllocationPieChart.COLOR_BORDER);
        pieChart.setShowWedgeLabels(true);
        pieChart.setUsePercentsAsWedgeLabels(true);
        pieChart.setPieStyle(PieChartBean.PIE_STYLE_FLAT);
        pieChart.setBorderWidth((float) 1.5);
        pieChart.setWedgeLabelOffset(75);
        pieChart.setFontSize(10);
        pieChart.setFontBold(true);
        pieChart.setDrawBorders(true);
        pieChart.setAppletWidth(110);
        pieChart.setAppletHeight(110);
        pieChart.setPieWidth(90);
        pieChart.setPieHeight(90);

        // now feed it the actual data
        for (int i = 0; i < RISK_GROUPS.length; i++) {
            pieChart.addPieWedge("wedge" + (i + 1), (float) partRiskVO
                    .getTotalAssetsByRisk(RISK_GROUPS[i]),
                    Constants.AssetAllocationPieChart.COLOR_WEDGES[i], " ", String.valueOf(i + 1),
                    Constants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, 0);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("entry <- getAssetAllocationByRiskPieChartBean");
        }
        return pieChart;
    }

    protected void populateDownloadData(ParticipantAccountVO participantAccountVO,
            ParticipantAccountForm form, HttpServletRequest request, HttpServletResponse response)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateDownloadData");
        }

        UserProfile userProfile = getUserProfile(request);

        boolean showLoans = form.getShowLoans();
        boolean showAge = form.getShowAge();
        boolean isAsOfDateCurrent = form.isAsOfDateCurrent();
        boolean showPBA = userProfile.getCurrentContract().isPBA();
        // GIFL 1C start
        boolean hasContractGatewayInd = userProfile.getCurrentContract().getHasContractGatewayInd();
        // GIFL 1C ends
        StringBuffer buff = new StringBuffer(255);

        Contract currentContract = getUserProfile(request).getCurrentContract();
        buff.append("Contract").append(COMMA).append(currentContract.getContractNumber()).append(
                COMMA).append(currentContract.getCompanyName()).append(LINE_BREAK);

        SimpleDateFormat dateFormatter = new SimpleDateFormat(RenderConstants.MEDIUM_MDY_SLASHED);

        Date asOfDate = new Date(Long.parseLong(form.getSelectedAsOfDate()));

        // Section 1 as of date for the report
        buff.append("As of,").append(dateFormatter.format(asOfDate)).append(LINE_BREAK).append(
                LINE_BREAK);

        // Section 2 participant details for the report
        buff.append("Last name,First name,SSN,Birth Date,");
        if (showAge)
            buff.append("Age,");
        if (isAsOfDateCurrent) {
            buff.append("Default date of birth,Employment status,Employment status effective date,Contribution status,");	//CL 110234
        }
        buff.append("Address line 1,Address line 2,City,State,Zip Code,");					//CL 110234
        buff.append(LINE_BREAK);

        ParticipantAccountDetailsVO detailsVO = participantAccountVO
                .getParticipantAccountDetailsVO();

        buff.append(QUOTE).append(detailsVO.getLastName()).append(QUOTE).append(COMMA);
        buff.append(QUOTE).append(detailsVO.getFirstName()).append(QUOTE).append(COMMA);
        // SSE024, mask ssn if no download report full ssn permission
        boolean maskSSN = true;
        try {
            maskSSN = ReportDownloadHelper.isMaskedSsn(userProfile, currentContract
                    .getContractNumber());

        } catch (SystemException se) {
            logger.error(se);
            // log exception and output blank ssn
        }

        buff.append(SSNRender.format(detailsVO.getSsn(), null, maskSSN)).append(COMMA);
        buff.append(
                detailsVO.getBirthDate() == null ? "Not entered" : dateFormatter.format(detailsVO
                        .getBirthDate())).append(COMMA);

        if (showAge)
            buff.append(detailsVO.getAge()).append(COMMA);
        if (isAsOfDateCurrent) {
            buff.append(detailsVO.getBirthDate() == null ? "Yes" : "No").append(COMMA);
        }
        //CL 110234 Begin
        if (isAsOfDateCurrent){
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
            buff.append(detailsVO.getEmployeeStatus()).append(COMMA);
        }
        //CL 110234 End
        buff.append(QUOTE).append(detailsVO.getAddressLine1()).append(QUOTE).append(COMMA);
        buff.append(QUOTE).append(detailsVO.getAddressLine2()).append(QUOTE).append(COMMA);
        buff.append(QUOTE).append(detailsVO.getCityName()).append(QUOTE).append(COMMA);
        buff.append(detailsVO.getStateCode()).append(COMMA);
        buff.append(detailsVO.getZipCode()).append(COMMA).append(LINE_BREAK).append(LINE_BREAK)
                .append(LINE_BREAK);

        buff.append("Total assets,");
        buff.append(detailsVO.getTotalAssets()).append(LINE_BREAK);
        buff.append("Allocated assets,");
        buff.append(detailsVO.getAllocatedAssets());

        if (showLoans) {
            buff.append(LINE_BREAK);
            buff.append("Loan assets,");
            buff.append(detailsVO.getLoanAssets());
        }

        if (showPBA) {
            buff.append(LINE_BREAK);
            buff.append("Personal brokerage account,");
            buff.append(detailsVO.getPersonalBrokerageAccount());
        }
        // GIFL 1C Starts
        if (isAsOfDateCurrent) {
            // Contract has selected GIFL
            if (hasContractGatewayInd) {
                buff.append(LINE_BREAK);
                buff.append("Guaranteed Income feature,");
                buff.append(participantAccountVO.getParticipantAccountDetailsVO()
                        .getParticipantGIFLIndicatorAsSelect());
            }
            // Contract has not selected GIFL
            else {
                if (participantAccountVO.getParticipantAccountDetailsVO()
                        .getParticipantGIFLIndicator() != null) {
                    buff.append(LINE_BREAK);
                    buff.append("Guaranteed Income feature,");
                    buff.append(participantAccountVO.getParticipantAccountDetailsVO()
                            .getParticipantGIFLIndicatorAsSelect());
                }
            }
        }
        
		if (isAsOfDateCurrent && (detailsVO.getManagedAccountStatusValue() != null)) {
			buff.append(LINE_BREAK);
			buff.append("Managed Accounts,");
			buff.append(detailsVO.getManagedAccountStatusValue());

		}
        // GIFL 1C Ends
        String investInstTypeDes="";
        if (isAsOfDateCurrent) {
            buff.append(LINE_BREAK);
            buff.append("Investment instruction type,");
            if(detailsVO.getInvestmentInstructionType() != null)
            {
            	if("TR".equalsIgnoreCase(detailsVO.getInvestmentInstructionType()))
            	{
            		investInstTypeDes = "TR – Instructions were provided by Trustee - Mapped";
            	}
            	else if("PA".equalsIgnoreCase(detailsVO.getInvestmentInstructionType()))
            	{
            		investInstTypeDes = "PA – Participant Provided";
            	}
            	else if("PR".equalsIgnoreCase(detailsVO.getInvestmentInstructionType()))
            	{
            		investInstTypeDes = "PR – Instructions prorated - participant instructions incomplete / incorrect";
            	}
            	else if("DF".equalsIgnoreCase(detailsVO.getInvestmentInstructionType()))
            	{
            		investInstTypeDes = "DF – Default investment option was used";
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
            buff.append("Automatic rebalance indicator,");
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
            if (userProfile.getCurrentContract().getHasLifecycle()
                    || assetsByRiskVO.getTotalAssetsByRisk(FundVO.RISK_LIFECYCLE) > 0)
                buff.append("Target Date,").append(DOLLAR_SIGN).append(
                        assetsByRiskVO.getTotalAssetsByRisk(FundVO.RISK_LIFECYCLE)).append(COMMA)
                        .append(
                        		formatPercentageFormatter(Math.round(assetsByRiskVO
                                        .getPercentageTotalByRisk(FundVO.RISK_LIFECYCLE) * 100)))
                        .append(PERCENT).append(LINE_BREAK);
            buff.append("Aggressive,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(FundVO.RISK_AGGRESIVE)).append(COMMA)
                    .append(
                    		formatPercentageFormatter(Math.round(assetsByRiskVO
                                    .getPercentageTotalByRisk(FundVO.RISK_AGGRESIVE) * 100)))
                    .append(PERCENT).append(LINE_BREAK);
            buff.append("Growth,").append(DOLLAR_SIGN).append(assetsByRiskVO.getTotalAssetsByRisk(FundVO.RISK_GROWTH))
                    .append(COMMA).append(
                    		formatPercentageFormatter(Math.round(assetsByRiskVO
                                    .getPercentageTotalByRisk(FundVO.RISK_GROWTH) * 100))).append(
                            PERCENT).append(LINE_BREAK);
            buff.append("Growth & income,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(FundVO.RISK_GROWTH_INCOME)).append(COMMA)
                    .append(
                    		formatPercentageFormatter(Math.round(assetsByRiskVO
                                    .getPercentageTotalByRisk(FundVO.RISK_GROWTH_INCOME) * 100)))
                    .append(PERCENT).append(LINE_BREAK);
            buff.append("Income,").append(DOLLAR_SIGN).append(assetsByRiskVO.getTotalAssetsByRisk(FundVO.RISK_INCOME))
                    .append(COMMA).append(
                    		formatPercentageFormatter(Math.round(assetsByRiskVO
                                    .getPercentageTotalByRisk(FundVO.RISK_INCOME) * 100))).append(
                            PERCENT).append(LINE_BREAK);
            buff.append("Conservative,").append(DOLLAR_SIGN).append(
                    assetsByRiskVO.getTotalAssetsByRisk(FundVO.RISK_CONSERVATIVE)).append(COMMA)
                    .append(
                    		formatPercentageFormatter(Math.round(assetsByRiskVO
                                    .getPercentageTotalByRisk(FundVO.RISK_CONSERVATIVE) * 100)))
                    .append(PERCENT).append(LINE_BREAK);
            if (showPBA) {
                buff.append("Personal brokerage account,").append(DOLLAR_SIGN).append(
                        assetsByRiskVO.getTotalAssetsByRisk(FundVO.RISK_PBA)).append(COMMA).append(
                        	formatPercentageFormatter(Math.round(assetsByRiskVO
                                .getPercentageTotalByRisk(FundVO.RISK_PBA) * 100))).append(PERCENT)
                        .append(LINE_BREAK);
            }
            buff.append(LINE_BREAK);
        }

        buff.append(populateDetailedDownloadData(participantAccountVO, form));

        byte[] downloadData = buff.toString().getBytes();

        ReportController.streamDownloadData(request, response, ReportController.CSV_TEXT,
                "participantAccount" + ReportController.CSV_EXTENSION, downloadData);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- populateDownloadData");
        }
    }

    protected BaseReportForm resetForm( BaseReportForm reportForm,
            HttpServletRequest request) throws SystemException {
        return reportForm;
    }

    protected abstract String populateDetailedDownloadData(
            ParticipantAccountVO participantAccountVO, ParticipantAccountForm form) throws SystemException;


	/*
	 *  * (non-Javadoc) 
	 * This code has been changed and added to Validate form and
	 * request against penetration attack, prior to other validations as part of the CL#137697.
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.BaseAction#doValidate(org.apache.struts.action.ActionMapping,
	 * org.apache.struts.action.Form,javax.servlet.http.HttpServletRequest)
	 */
	/*@SuppressWarnings({ "rawtypes" })
	public String validate( Form form,
			HttpServletRequest request) {
		Collection penErrors = PsValidation.doValidatePenTestAutoAction(form,mapping, request, CommonConstants.INPUT);
		if (penErrors != null && penErrors.size() > 0) {
			ActionForward forward = new ActionForward(CommonConstants.ERROR_RDRCT, "/do" + mapping.getPath(),true);
			return forward;
		}
		return super.validate( form, request);
	}*/
    @Autowired
    private PSValidatorFWParticipantInput psValidatorFWParticipantInput;  

    @InitBinder
    protected void initBinder(HttpServletRequest request,
    			ServletRequestDataBinder  binder) {
    	 binder.bind( request);
    	binder.addValidators(psValidatorFWParticipantInput);
    }

}
