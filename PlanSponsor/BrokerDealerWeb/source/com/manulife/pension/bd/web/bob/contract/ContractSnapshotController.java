package com.manulife.pension.bd.web.bob.contract;

import static com.manulife.pension.bd.web.BDConstants.BOB_PAGE_FORWARD;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.mail.URLName;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;
import javax.validation.Valid;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.BobContextUtils;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.pagelayout.BDLayoutBean;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentType;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractAssetGrowthAndContributionsWithdrawalsVO.AssetGrowth;
import com.manulife.pension.service.contract.valueobject.ContractAssetGrowthAndContributionsWithdrawalsVO.ContributionsWithdrawals;
import com.manulife.pension.service.contract.valueobject.ContractAssetsByRiskAndParticipantStatusVO;
import com.manulife.pension.service.contract.valueobject.ContractSnapshotVO;
import com.manulife.pension.service.fund.valueobject.FundVO;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.MessageProvider;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.barchart.BarChartException;
import com.manulife.util.barchart.DataSeries;
import com.manulife.util.barchart.Grid;
import com.manulife.util.barchart.Legend;
import com.manulife.util.barchart.Point;
import com.manulife.util.barchart.Rollover;
import com.manulife.util.barchart.taglib.BarChartBean;
import com.manulife.util.piechart.PieChartBean;

/**
 * ContractSnapshot Action class This class is used to forward the users's request to ContractSnapshot page
 * 
 * @author Simona Stoicescu
 * @author AAmbrose [moved from plansponsor]
 */
@Controller
@RequestMapping( value ="/bob")
@SessionAttributes({"contractSnapshotForm"})

public class ContractSnapshotController extends BDController {
	@ModelAttribute("contractSnapshotForm") 
	public ContractSnapshotForm populateForm() 
	{
		return new ContractSnapshotForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/contract/contractSnapshot.jsp");
		forwards.put("contractSnapshot","/contract/contractSnapshot.jsp");
		}

    private static final String CONTRACT_SNAPSHOT_PAGE = "contractSnapshot";

    private static final String PIE_CHART_APPLET_ARCHIVE = "/assets/unmanaged/applets/pieChartApplet.jar";

    private static final String BARCHART_APPLET_ARCHIVE = "/assets/unmanaged/applets/barChartApplet.jar";

    private static final String DECIMAL_FORMAT = "$#,##0;($#,##0)";

    private static final double MAX = 1000;

    private static final int DELTA = 500;

    private static final int BARCHART_HEIGHT = 180;

    private static final int BARCHART_WIDTH = 350;

    private static final int BARCHART_BAR_WIDTH = 11;

    private static final int BARCHART_BCKGRD_RED = 255;

    private static final int BARCHART_BCKGRD_BLUE = 255;

    private static final int BARCHART_BCKGRD_GREEN = 255;

    private static final String[] RISK_GROUPS = new String[] { FundVO.RISK_LIFECYCLE,
            FundVO.RISK_AGGRESIVE, FundVO.RISK_GROWTH, FundVO.RISK_GROWTH_INCOME,
            FundVO.RISK_INCOME, FundVO.RISK_CONSERVATIVE, FundVO.RISK_PBA };

    private static final String WEDGE1 = "wedge1";

    private static final String WEDGE2 = "wedge2";

    private static final String WEDGE3 = "wedge3";

    private static final String WEDGE4 = "wedge4";

    private static final String WEDGE5 = "wedge5";
    
    private static final String WEDGE6 = "wedge6";
    
    private static final String WEDGE7 = "wedge7";

    private static final String WEDGE1_TO_WEDGE7 = "wedge1;wedge2;wedge3;wedge4;wedge5;wedge6;wedge7";

    private static final String WEDGE1_TO_WEDGE3 = "wedge1;wedge2;wedge3";

    private static final String EMPLOYER_BALANCE = "Employer Balance";

    private static final String EMPLOYEE_BALANCE = "Employee Balance";

    private static final String EMPLOYER_CONTRIBUTIONS = "Employer Contributions";

    private static final String EMPLOYEE_CONTRIBUTIONS = "Employee Contributions";

    private static final String CONTRIBUTIONS = "Contributions";

    private static final String DISTRIBUTIONS = "Distributions";

    public ContractSnapshotController() {
        super(ContractSnapshotController.class);
    }

    /**
     * The preExecute method has been overriden to see if the contractNumber is coming as part of request parameter. If
     * the contract Number is coming as part of request parameter, the BobContext will be setup with contract
     * information of the contract number passed in the request parameter.
     * 
     */
    protected String preExecute( ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException, SystemException {
        super.preExecute( form, request, response);

        BobContextUtils.setUpBobContext(request);

        BobContext bob = BDSessionHelper.getBobContext(request);
        if (bob == null || bob.getCurrentContract() == null) {
            return forwards.get(BOB_PAGE_FORWARD);
        }

        if (bob.getCurrentContract().getCompanyCode().equals(
                GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY)) {
            ApplicationHelper.setRequestContentLocation(request, Location.NEW_YORK);
        }

        return null;
    }

   @RequestMapping(value ="/contract/contractSnapshot/", method =  {RequestMethod.POST,RequestMethod.GET}) 
    public String doExecute(@Valid @ModelAttribute("contractSnapshotForm") ContractSnapshotForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
    throws IOException,ServletException, SystemException {

	   String forward = null;
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doExecute");
        }

        // get the user profile object and set the current contract to null
        Contract currentContract = getBobContext(request).getCurrentContract();
        forward= preExecute(actionForm, request, response);
        if ( StringUtils.isNotBlank(forward)) {
     	    return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
        }
        //End of preExecute
        Date asOfDate = null;
       

        // set the contract gifl/roth message
        ArrayList<GenericException> infoMessages = new ArrayList<GenericException>();
        ContentType contentType = ContentTypeManager.instance().MISCELLANEOUS;
    
        if (currentContract.hasRoth()) {
            GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
                    BDContentConstants.MISCELLANEOUS_ROTH_INFO, contentType, false);
            infoMessages.add(exception);
        }
        
        if (currentContract.getHasContractGatewayInd()) {
            GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
                    BDContentConstants.CONTRACT_SNAPSHOT_GIFL_MESSAGE, contentType, false);
            infoMessages.add(exception);
        }

        if (!infoMessages.isEmpty()) {
            setMessagesInRequest(request, infoMessages, BDConstants.INFO_MESSAGES);
        }

        // reset the form if the URL is the default URL with no parameters
        // this way we always show the default view of the report
        if (!request.getParameterNames().hasMoreElements()) {
            resetForm( actionForm, request);
        }

       
       ByteArrayOutputStream bos = loadChartData( request, currentContract, actionForm);
        
        if (BDConstants.PRINT_PDF_TASK.equals(request.getParameter(BDConstants.TASK_KEY))) {
        	 /**
             * notice that we cannot set Cache-Control to no-cache, or IE will give an error.
             * 
             * @see Microsoft Knowledge Base Article - 231296
             *      (http://support.microsoft.com/default.aspx?scid=kb;en-us;231296)
             */
        	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        	response.setHeader("Pragma", "no-cache");
            response.setContentType("application/pdf");
            /**
             * For some reason, the filename attribute for content disposition is never used.
             */
            response.setHeader("Content-Disposition", "inline");

            /**
             * It's important to set the content length or IE may not be able to display properly.
             */

            if (logger.isDebugEnabled()) {
                logger.debug("Size of PDF stream: " + bos.size() + " bytes");
            }
            response.setContentLength(bos.size());

            ServletOutputStream sos = response.getOutputStream();
            bos.writeTo(sos);
            sos.flush();
            sos.close();

            /**
             * No need to forward to any other JSP or action. Returns null will make Struts to return controls back
             * to server immediately.
             */
            return null;
        }else{
        	return forwards.get(CONTRACT_SNAPSHOT_PAGE);
        }
        
    }

	private ByteArrayOutputStream loadChartData( HttpServletRequest request,Contract currentContract,
			ContractSnapshotForm myForm) throws SystemException, IOException {
		 Date asOfDate = null;
		 myForm.setContractHasRothInfo(currentContract.hasRothNoExpiryCheck());
		if (!myForm.getIsRecentDate()) {
	            asOfDate = new Date();
	            asOfDate.setTime(Long.parseLong(myForm.getStringDate()));
		}
		// The first time the action is called, 1) we pre-load the errors in
        // the action form.
        // 2) select the asOfDate value in the list box stringDate.
        if (myForm.getStringDate().length() == 0) {
            // We use javascript for calculating the participation rate.
            // Therefore we pre-load the errors in the form and we show them
            // through javascript when necessary.
            Collection<GenericException> errorCollection = new ArrayList<GenericException>();
            errorCollection.add(new GenericException(BDErrorCodes.EMPTY_VALUE));
            errorCollection.add(new GenericException(BDErrorCodes.TOO_SMALL));
            errorCollection.add(new GenericException(BDErrorCodes.INVALID_ENTRY));

            try {
                String[] errors = MessageProvider.getInstance().getMessages(errorCollection);
                if (errors == null || errors.length != 3)
                    throw (new ContentException("Contract snapshot: Missing error messages"));
                myForm.setError1(errors[0]);
                myForm.setError2(errors[1]);
                myForm.setError3(errors[2]);
            } catch (ContentException e) {
                SystemException se = new SystemException(e, this.getClass().getName(), "doExecute",
                        "ContentException occurred while getting possible error messages.");
                LogUtility.logSystemException(BDConstants.BD_APPLICATION_ID, se);
            }

            myForm.setStringDate(String.valueOf(currentContract.getContractDates().getAsOfDate()
                    .getTime()));
        }
        ContractSnapshotVO vo = ContractServiceDelegate.getInstance().getContractSnapshot(
        		currentContract.getContractNumber(), asOfDate);

        // Should we display the loan amount?
        myForm.setDisplayLoan(currentContract.isLoanFeature()
                || (vo.getPlanAssets().getLoanAssets() != null
                && !new BigDecimal("0.00").equals(vo.getPlanAssets().getLoanAssets())));

        // determine if we should show PBA?
        if (currentContract.isPBA()
                || vo.getPlanAssets().getPersonalBrokerageAccountAmount().doubleValue() > 0) {
            myForm.setDisplayPba(true);
        }
        // If the contract currently has Lifecycle Funds selected
        // OR the contract has a balance in Lifecycle funds as of the selected
        // date (current or
        // past), then the following will be displayed
        myForm.setDisplayLifecycle(currentContract.getHasLifecycle()
                || vo.getContractAssetsByRisk().getTotalAssetsByRisk("LC") > 0);
        // prepare the Asset growth and Contributions and Withdrawals
        // data for the BarChart
        BarChartBean assetGrowthBarChart = new BarChartBean();
        BarChartBean contributionWithdrawalsBarChart = new BarChartBean();
        try {
            configureBarChartBeanForAssetGrowth(currentContract.isDefinedBenefitContract(),
                    assetGrowthBarChart, vo.getContractAssetGrowth().getLast24MonthAssetGrowth(
                            asOfDate == null ? currentContract.getContractDates().getAsOfDate()
                                    : asOfDate));

            Date contribWithdrawDate = asOfDate == null ? currentContract.getContractDates()
                    .getAsOfDate() : asOfDate;

            configureBarChartBeanForContributions(currentContract.isDefinedBenefitContract(),
                    contributionWithdrawalsBarChart, vo.getContractAssetGrowth()
                            .getLastTwelveMonthContributionsWithdrawals(contribWithdrawDate,
                                    currentContract.getContractDates()));
        } catch (BarChartException e) {
            SystemException se = new SystemException(e, this.getClass().getName(), "doExecute",
                    "Error occurred while generating BarCharts.");
            LogUtility.logSystemException(BDConstants.BD_APPLICATION_ID, se);
        }

        PieChartBean participantStatusPieChartBean = getParticipantStatusPieChartBean(vo);
        PieChartBean assetAllocByRiskPieChartBean = getAssetAllocationByRiskPieChartBean(vo);

		PieChartBean assetAllocBelow30PieChartBean = getAssetAllocationByAgeGroupPieChartBean(vo,ContractAssetsByRiskAndParticipantStatusVO.AGE_GROUP_BELOW_30);
		PieChartBean assetAllocBetween30And39PieChartBean = getAssetAllocationByAgeGroupPieChartBean(vo,ContractAssetsByRiskAndParticipantStatusVO.AGE_GROUP_BETWEEN_30_39);
		PieChartBean assetAllocBetween40And49PieChartBean = getAssetAllocationByAgeGroupPieChartBean(vo,ContractAssetsByRiskAndParticipantStatusVO.AGE_GROUP_BETWEEN_40_49);
		PieChartBean assetAllocBetween50And59PieChartBean = getAssetAllocationByAgeGroupPieChartBean(vo,ContractAssetsByRiskAndParticipantStatusVO.AGE_GROUP_BETWEEN_50_59);
		PieChartBean assetAllocBetween60AndAbovePieChartBean = getAssetAllocationByAgeGroupPieChartBean(vo,ContractAssetsByRiskAndParticipantStatusVO.AGE_GROUP_60_AND_PLUS);

        if (BDConstants.PRINT_PDF_TASK.equals(request.getParameter(BDConstants.TASK_KEY))) {

            if (logger.isDebugEnabled()) {
                logger.debug("Printer Friendly PDF will be generated...");
            }

            BDLayoutBean bean = ApplicationHelper.getLayoutStore(request.getServletContext())
                    .getLayoutBean(forwards.get(CONTRACT_SNAPSHOT_PAGE), request);

            // LayoutBean layoutBean = LayoutBeanRepository.getInstance()
            // .getPageBean(
            // mapping.findForward(CONTRACT_SNAPSHOT_PAGE)
            // .getPath());
            String[] infoMessageErrors = null;
            try {
                infoMessageErrors = ContentHelper
                        .getMessagesUsingContentType((Collection<GenericException>) request
                                .getAttribute(BDConstants.INFO_MESSAGES));
                ContractSnapshotPdf pdf = new ContractSnapshotPdf(myForm.getStringDate(), myForm
                        .getDisplayLoan(), myForm.getDisplayPba(), myForm.getDisplayLifecycle(),
                        myForm.getIsRecentDate(), myForm.isContractHasRothInfo(), currentContract,
                        getRequestUrlName(request), ApplicationHelper.getRequestContentLocation(request),
                        bean, vo, assetGrowthBarChart, contributionWithdrawalsBarChart,
                        participantStatusPieChartBean, assetAllocByRiskPieChartBean,
                        assetAllocBelow30PieChartBean, assetAllocBetween30And39PieChartBean,
                        assetAllocBetween40And49PieChartBean, assetAllocBetween50And59PieChartBean,
                        assetAllocBetween60AndAbovePieChartBean, infoMessageErrors);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                pdf.writeTo(bos);

               return bos;
            } catch (ContentException e) {
                SystemException se = new SystemException(e, this.getClass().getName(), "doExecute",
                        "ContentException occurred while generating the PDF.");
                throw se;
            }
        } else {

            if (logger.isDebugEnabled())
                logger.debug(ContractSnapshotController.class.getName()
                        + ":forwarding to Contract Snapshot Page.");

            request.setAttribute(BDConstants.CONTRACT_SNAPSHOT, vo);

            request.setAttribute(BDConstants.CONTRACT_SNAPSHOT_ASSET_GROWTH, assetGrowthBarChart);
            request.setAttribute(BDConstants.CONTRACT_SNAPSHOT_CONTR_WITHDRAWALS,
                    contributionWithdrawalsBarChart);

            request.setAttribute(BDConstants.CONTRACT_SNAPSHOT_STATUS_PIECHART,
                    participantStatusPieChartBean);

            request.setAttribute(BDConstants.CONTRACT_SNAPSHOT_RISK_PIECHART,
                    assetAllocByRiskPieChartBean);
            request.setAttribute(BDConstants.CONTRACT_SNAPSHOT_AGE_BELOW_30_PIECHART,
                    assetAllocBelow30PieChartBean);
            request.setAttribute(BDConstants.CONTRACT_SNAPSHOT_AGE_30_39_PIECHART,
                    assetAllocBetween30And39PieChartBean);
            request.setAttribute(BDConstants.CONTRACT_SNAPSHOT_AGE_40_49_PIECHART,
                    assetAllocBetween40And49PieChartBean);
            request.setAttribute(BDConstants.CONTRACT_SNAPSHOT_AGE_50_59_PIECHART,
                    assetAllocBetween50And59PieChartBean);
            request.setAttribute(BDConstants.CONTRACT_SNAPSHOT_AGE_60_ABOVE_PIECHART,
                    assetAllocBetween60AndAbovePieChartBean);

            if (logger.isDebugEnabled()) {
                logger.debug("exit <- doExecute");
            }

            return null;
        }
	}

    /**
     * @param HttpServletRequest request
     * @return URLName urlName
     */
    private URLName getRequestUrlName(HttpServletRequest request) {
        StringBuffer urlSb = HttpUtils.getRequestURL(request);
        return new URLName(urlSb.toString());
    }

    private PieChartBean createDefaultPieChartBean() {
        PieChartBean pieChart = new PieChartBean();
        pieChart.setAppletArchive(PIE_CHART_APPLET_ARCHIVE);
        // pieChart.setPresentationModel(PieChartBean.PRESENTATION_MODEL_APPLET);
        pieChart.setPresentationModel(PieChartBean.PRESENTATION_MODEL_SERVLET);
        pieChart.setBorderColor(BDConstants.AssetAllocationPieChart.COLOR_BORDER);
        pieChart.setShowWedgeLabels(true);
        pieChart.setUsePercentsAsWedgeLabels(true);
        pieChart.setPieStyle(PieChartBean.PIE_STYLE_FLAT);
        pieChart.setBorderWidth((float) 1.5);
        pieChart.setWedgeLabelOffset(75);
        pieChart.setFontSize(10);
        pieChart.setFontBold(true);
        pieChart.setDrawBorders(true);
        pieChart.setWedgeLabelExtrusion(35);
        pieChart.setWedgeLabelExtrusionThreshold(6);
        pieChart.setWedgeLabelExtrusionColor("#000000");
        return pieChart;
    }

    private PieChartBean getParticipantStatusPieChartBean(ContractSnapshotVO vo) {
        PieChartBean pieChart = createDefaultPieChartBean();

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getParticipantStatusPieChartBean");
        }

        if (vo.getContractAssetsByRisk().getHasEmployeeMoneyType()) {
            pieChart.addPieWedge(WEDGE1,
                    vo.getContractAssetsByRisk().getActiveParticipantsNumber(),
                    BDConstants.ParticipantStatusPieChart.COLOR_ACTIVE,
                    BDConstants.SINGLE_SPACE_SYMBOL, BDConstants.VIEWING_PREFERENCE,
                    BDConstants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, BDConstants.NUMBER_0);
            pieChart.addPieWedge(WEDGE2, vo.getContractAssetsByRisk()
                    .getInactiveWithBalanceParticipantsNumber(),
                    BDConstants.ParticipantStatusPieChart.COLOR_INACTIVE_WITH_BALANCE,
                    BDConstants.SINGLE_SPACE_SYMBOL, BDConstants.VIEWING_PREFERENCE,
                    BDConstants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, BDConstants.NUMBER_0);
            pieChart.addPieWedge(WEDGE3, vo.getContractAssetsByRisk()
                    .getInactiveWithUMParticipantsNumber(),
                    BDConstants.ParticipantStatusPieChart.COLOR_INACTIVE_UNINVESTED_MONEY,
                    BDConstants.SINGLE_SPACE_SYMBOL, BDConstants.VIEWING_PREFERENCE,
                    BDConstants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, BDConstants.NUMBER_0);
            pieChart.setSuppressWedgesFromKey(WEDGE1_TO_WEDGE3);

        } else {
            pieChart.addPieWedge(WEDGE1, vo.getContractAssetsByRisk()
                    .getActiveContributingParticipantsNumber(),
                    BDConstants.ParticipantStatusPieChart.COLOR_ACTIVE_AND_CONTRIBUTING,
                    BDConstants.SINGLE_SPACE_SYMBOL, BDConstants.VIEWING_PREFERENCE,
                    BDConstants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, BDConstants.NUMBER_0);
            pieChart.addPieWedge(WEDGE2, vo.getContractAssetsByRisk()
                    .getActiveOptedOutParticipantsNumber(),
                    BDConstants.ParticipantStatusPieChart.COLOR_ACTIVE_OPTED_OUT, 
                    BDConstants.SINGLE_SPACE_SYMBOL, BDConstants.VIEWING_PREFERENCE,
                    BDConstants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, BDConstants.NUMBER_0);            
            pieChart.addPieWedge(WEDGE3, vo.getContractAssetsByRisk()
                    .getActiveNoBalanceParticipantsNumber(),
                    BDConstants.ParticipantStatusPieChart.COLOR_ACTIVE_NO_BALANCE,
                    BDConstants.SINGLE_SPACE_SYMBOL, BDConstants.VIEWING_PREFERENCE,
                    BDConstants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, BDConstants.NUMBER_0);
            pieChart.addPieWedge(WEDGE4, vo.getContractAssetsByRisk()
                    .getActiveNotContributingParticipantsNumber(),
                    BDConstants.ParticipantStatusPieChart.COLOR_ACTIVE_NOT_CONTRIBUTING,
                    BDConstants.SINGLE_SPACE_SYMBOL, BDConstants.VIEWING_PREFERENCE,
                    BDConstants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, BDConstants.NUMBER_0);
            pieChart.addPieWedge(WEDGE5, vo.getContractAssetsByRisk()
                    .getInactiveWithBalanceParticipantsNumber(),
                    BDConstants.ParticipantStatusPieChart.COLOR_INACTIVE_WITH_BALANCE,
                    BDConstants.SINGLE_SPACE_SYMBOL, BDConstants.VIEWING_PREFERENCE,
                    BDConstants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, BDConstants.NUMBER_0);
            pieChart.addPieWedge(WEDGE6, vo.getContractAssetsByRisk()
                    .getInactiveWithUMParticipantsNumber(),
                    BDConstants.ParticipantStatusPieChart.COLOR_INACTIVE_UNINVESTED_MONEY,
                    BDConstants.SINGLE_SPACE_SYMBOL, BDConstants.VIEWING_PREFERENCE,
                    BDConstants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, BDConstants.NUMBER_0);
            pieChart.addPieWedge(WEDGE7, vo.getContractAssetsByRisk()
                    .getOptedOutNotVestedParticipantsNumber(),
                    BDConstants.ParticipantStatusPieChart.COLOR_OPTED_OUT_NOT_VESTED, 
                    BDConstants.SINGLE_SPACE_SYMBOL, BDConstants.VIEWING_PREFERENCE,
                    BDConstants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, BDConstants.NUMBER_0);
            pieChart.setSuppressWedgesFromKey(WEDGE1_TO_WEDGE7);            
        }

        pieChart.setAppletWidth(130);
        pieChart.setAppletHeight(135);

        pieChart.setPieWidth(95);
        pieChart.setPieHeight(95);
        // pieChart.setWedgeLabelOffset(68);
        // pieChart.setPrecision(2);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getParticipantStatusPieChartBean");
        }

        return pieChart;

    }

    /**
     * Returns a pie chart bean for the asset allocation by risk category chart.
     * 
     * @param vo The contract snapshot value object.
     * @return A pie chart bean for the asset allocation by risk category chart.
     */
    private PieChartBean getAssetAllocationByRiskPieChartBean(ContractSnapshotVO vo) {
        PieChartBean pieChart = createDefaultPieChartBean();
        pieChart.setAppletWidth(130);
        pieChart.setAppletHeight(135);
        pieChart.setPieWidth(95);
        pieChart.setPieHeight(95);
        ContractAssetsByRiskAndParticipantStatusVO byRiskVo = vo.getContractAssetsByRisk();

        String wedgeLabelColor;
        for (int i = 0; i < RISK_GROUPS.length; i++) {
            wedgeLabelColor = BDConstants.AssetAllocationPieChart.COLOR_WEDGE_LABEL;
            if (BDConstants.AssetAllocationPieChart.COLOR_WEDGES[i] == BDConstants.AssetAllocationPieChart.COLOR_GROWTH_INCOME) {
                wedgeLabelColor = "#000000";
            }

            pieChart.addPieWedge("wedge" + (i + 1), (float) byRiskVo
                    .getTotalAssetsByRisk(RISK_GROUPS[i]),
                    BDConstants.AssetAllocationPieChart.COLOR_WEDGES[i],
                    BDConstants.SINGLE_SPACE_SYMBOL, String.valueOf(i + 1), wedgeLabelColor,
                    BDConstants.NUMBER_0);

        }

        return pieChart;
    }

    /**
     * Retrieves assets allocation by age group to construct the pie chart bean
     * 
     * @param ContractSnapshotVO vo
     * @param String ageGroup
     * @return PieChartBean pieChart
     */
    private PieChartBean getAssetAllocationByAgeGroupPieChartBean(ContractSnapshotVO vo,
            String ageGroup) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getAssetAllocationByAgeGroupPieChartBean");
        }

        ContractAssetsByRiskAndParticipantStatusVO byRiskVo = vo.getContractAssetsByRisk();
        ContractAssetsByRiskAndParticipantStatusVO.AssetsByAgeGroup assetsByAgeGroup = byRiskVo
                .getAgeGroup(ageGroup);
        PieChartBean pieChart = createDefaultPieChartBean();
        pieChart.setAppletWidth(90);
        pieChart.setAppletHeight(95);
        pieChart.setPieWidth(43);
        pieChart.setPieHeight(43);
        pieChart.setShowWedgeLabels(false);
        for (int i = 0; i < RISK_GROUPS.length; i++) {
            String labelColor = BDConstants.AssetAllocationPieChart.COLOR_WEDGE_LABEL;
            if (BDConstants.AssetAllocationPieChart.COLOR_WEDGES[i] == BDConstants.AssetAllocationPieChart.COLOR_GROWTH_INCOME) {
                labelColor = BDConstants.AssetAllocationPieChart.COLOR_WEDGE_LABEL_DARK;
            }

            pieChart.addPieWedge("wedge" + (i + 1), (float) assetsByAgeGroup
                    .getAssetTotal(RISK_GROUPS[i]),
                    BDConstants.AssetAllocationPieChart.COLOR_WEDGES[i], null, null, labelColor,
                    BDConstants.NUMBER_0);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getAssetAllocationByAgeGroupPieChartBean");
        }

        return pieChart;
    }

    /**
     * configure the contribution details for bar chart
     * 
     * @param boolean isDefinedBenefit
     * @param BarChartBean barChartBean
     * @param Collection dataSeries
     * @throws BarChartException
     */
    private void configureBarChartBeanForContributions(boolean isDefinedBenefit,
            BarChartBean barChartBean, Collection dataSeries) throws BarChartException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> configureBarChartBeanForContributions");
        }

        setBarChartCommonProperties(barChartBean);

        Grid grid = new Grid();
        grid.setPosition(new Point(100, 50));
        grid.setSize(new Point(250, 120));
        grid.setColor(new Color(133, 133, 133));
        grid.setXLabelColor(new Color(0, 0, 0));
        grid.setYLabelColor(new Color(0, 0, 0));

        double max = getMaxContributions(dataSeries);
        // leave some room, increase the max
        // value with 15 %.
        max = 1.15 * max;
        // System.out.println("***** max = " + max );
        double min = getMinContributions(dataSeries);
        // System.out.println("***** min = " + min );

        grid.setMinY((Math.floor(min / DELTA)) * DELTA);
        grid.setMaxY((Math.ceil(max / DELTA)) * DELTA);

        grid.setAxisYValue(0);
        // grid.setTickDeltaY(DELTA);
        // grid.setGridDeltaY(DELTA);
        grid.setTickDeltaY(Math.round((grid.getMaxY() - grid.getMinY()) / 4));
        grid.setGridDeltaY(Math.round((grid.getMaxY() - grid.getMinY()) / 4));
        grid.setShowYGrid(true);
        grid.setShowYTicks(true);

        grid.setBarLeftOffset(11);
        grid.setBarDeltaOffset(21);
        grid.setBarGridDeltaX(126);
        grid.setShowXGrid(true);
        grid.setShowXTicks(true);

        barChartBean.setGrid(grid);

        Legend legend = new Legend(20, 325, 150, 50);
        legend.setBackgroundColor(new Color(255, 255, 255));
        legend.setTextColor(new Color(0, 0, 0));
        legend.setShowBorder(true);
        legend.setBorderColor(new Color(0, 0, 0));
        barChartBean.setLegend(legend);

        Rollover rollover = new Rollover(220, 325, 150, 50);
        rollover.setBackgroundColor(new Color(255, 255, 255));
        rollover.setTextColor(new Color(0, 0, 0));
        rollover.setShowBorder(true);
        rollover.setBorderColor(new Color(0, 0, 0));
        if (!isDefinedBenefit) {
            rollover.setGeneralText(new String[] { "Roll your mouse over", "a bar to see asset",
                    "figures" });
        } else {
            rollover.setGeneralText(new String[] { "Roll your mouse over",
                    "a bar to see asset figures" });
        }
        barChartBean.setRollover(rollover);
        setContributionsRowTitles(barChartBean, dataSeries);

        if (dataSeries != null) {
            barChartBean.setAllSeries(getContributionsSeries(dataSeries, isDefinedBenefit));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- configureBarChartBeanForContributions");
        }

    }

    /**
     * configure the asset growth details for bar chart
     * 
     * @param boolean isDefinedBenefit
     * @param BarChartBean barChartBean
     * @param Collection dataSeries
     * @throws BarChartException
     */
    private void configureBarChartBeanForAssetGrowth(boolean isDefinedBenefit,
            BarChartBean barChartBean, Collection dataSeries) throws BarChartException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> configureBarChartBeanForAssetGrowth");
        }

        setBarChartCommonProperties(barChartBean);
        barChartBean.setHeight(barChartBean.getHeight() + 18);

        Grid grid = new Grid();
        grid.setPosition(new Point(100, 50));
        grid.setSize(new Point(250, 120));
        grid.setColor(new Color(133, 133, 133));
        grid.setXLabelColor(new Color(0, 0, 0));
        grid.setYLabelColor(new Color(0, 0, 0));

        double max = getMaxAssetGrowth(dataSeries);
        // leave some room, increase the max
        // value with 15 %.
        max = 1.15 * max;

        grid.setMinY(0);
        grid.setMaxY(Math.ceil(max / DELTA) * DELTA);
        grid.setAxisYValue(0);
        // grid.setTickDeltaY(DELTA);
        // grid.setGridDeltaY(DELTA);
        grid.setTickDeltaY(Math.round(grid.getMaxY() / 4));
        grid.setGridDeltaY(Math.round(grid.getMaxY() / 4));
        grid.setShowYGrid(true);
        grid.setShowYTicks(true);

        grid.setBarLeftOffset(11);
        grid.setBarDeltaOffset(33);
        grid.setBarGridDeltaX(132);
        grid.setShowXGrid(true);
        grid.setShowXTicks(true);

        barChartBean.setGrid(grid);

        Legend legend = new Legend(20, 325, 150, 50);
        legend.setBackgroundColor(new Color(255, 255, 255));
        legend.setTextColor(new Color(0, 0, 0));
        legend.setShowBorder(true);
        legend.setBorderColor(new Color(0, 0, 0));
        barChartBean.setLegend(legend);

        Rollover rollover = new Rollover(220, 325, 150, 50);
        rollover.setBackgroundColor(new Color(255, 255, 255));
        rollover.setTextColor(new Color(0, 0, 0));
        rollover.setShowBorder(true);
        rollover.setBorderColor(new Color(0, 0, 0));

        if (!isDefinedBenefit) {
            rollover.setGeneralText(new String[] { "Roll your mouse over a",
                    "bar to see asset figures" });
        } else {
            rollover
                    .setGeneralText(new String[] { "Roll your mouse over a bar to see asset figures" });
        }
        barChartBean.setRollover(rollover);

        setAssetGrowthRowTitles(barChartBean, dataSeries);

        if (dataSeries != null) {

            barChartBean.setAllSeries(getAssetGrowthSeries(dataSeries, isDefinedBenefit));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- configureBarChartBeanForAssetGrowth");
        }
    }

    /**
     * Bar chart common properties
     * 
     * @param barChartBean
     */
    private void setBarChartCommonProperties(BarChartBean barChartBean) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> setBarChartCommonProperties");
        }

        barChartBean.setHeight(BARCHART_HEIGHT);
        barChartBean.setWidth(BARCHART_WIDTH);
        barChartBean.setBarWidth(BARCHART_BAR_WIDTH);
        barChartBean.setBackgroundColor(new Color(BARCHART_BCKGRD_RED, BARCHART_BCKGRD_GREEN,
                BARCHART_BCKGRD_BLUE));
        barChartBean.setArchive(BARCHART_APPLET_ARCHIVE);
        barChartBean.setDecimalFormatString(DECIMAL_FORMAT);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- setBarChartCommonProperties");
        }
    }

    /**
     * Returns contributions Series values
     * 
     * @param Collection dataSeries
     * @param boolean isDefinedBenefit
     * @return ArrayList allSeries
     */
    private ArrayList getContributionsSeries(Collection dataSeries, boolean isDefinedBenefit) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getContributionsSeries");
        }
        DataSeries employeeContributions = null;
        DataSeries employerContributions = null;

        if (!isDefinedBenefit) {
            employerContributions = new DataSeries(EMPLOYER_CONTRIBUTIONS, new Color(88, 139, 146));
            employeeContributions = new DataSeries(EMPLOYEE_CONTRIBUTIONS, new Color(208, 165, 63));
        } else {
            employerContributions = new DataSeries(CONTRIBUTIONS, new Color(88, 139, 146));
        }
        DataSeries withdrawal = new DataSeries(DISTRIBUTIONS, new Color(157, 108, 142));

        Iterator iterator = dataSeries.iterator();
        ArrayList allSeries = new ArrayList();
        while (iterator.hasNext()) {
            ContributionsWithdrawals contributions = (ContributionsWithdrawals) iterator.next();
            employerContributions.addValue(contributions.getEmployerContribution());
            if (!isDefinedBenefit) {
                employeeContributions.addValue(contributions.getEmployeeContribution());
            }
            withdrawal.addValue(contributions.getWithdrawal());

        }
        allSeries.add(employerContributions);
        if (!isDefinedBenefit) {
            allSeries.add(employeeContributions);
        }
        allSeries.add(withdrawal);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getContributionsSeries");
        }

        return allSeries;
    }

    /**
     * Returns max contributions
     * 
     * @param Collection dataSeries
     * @return double max
     */
    private double getMaxContributions(Collection dataSeries) {
        double max = MAX;
        Iterator iterator = dataSeries.iterator();
        while (iterator.hasNext()) {
            ContributionsWithdrawals contributions = (ContributionsWithdrawals) iterator.next();
            if (max < (contributions.getEmployeeContribution() + contributions
                    .getEmployerContribution())) {
                max = contributions.getEmployeeContribution()
                        + contributions.getEmployerContribution();
            }

        }

        return max;
    }

    /**
     * returns min contributions
     * 
     * @param Collection dataSeries
     * @return double min
     */
    private double getMinContributions(Collection dataSeries) {
        double min = 0;
        Iterator iterator = dataSeries.iterator();
        while (iterator.hasNext()) {
            ContributionsWithdrawals contributions = (ContributionsWithdrawals) iterator.next();
            if (min > contributions.getWithdrawal()) {
                min = contributions.getWithdrawal();
            }

        }

        return min;
    }

    /**
     * set contributions row titles
     * 
     * @param BarChartBean barChartBean
     * @param Collection dataSeries
     */
    public void setContributionsRowTitles(BarChartBean barChartBean, Collection dataSeries) {
        ArrayList xtitles = new ArrayList();
        ArrayList row1Titles = new ArrayList();

        Iterator iterator = dataSeries.iterator();
        while (iterator.hasNext()) {
            ContributionsWithdrawals contributions = (ContributionsWithdrawals) iterator.next();
            row1Titles.add(contributions.getMonth());

        }

        xtitles.add(row1Titles);
        barChartBean.setXTitles(xtitles);

    }

    /**
     * returns asset growth series
     * 
     * @param Collection dataSeries
     * @param boolean isDefinedBenefit
     * @return ArrayList allSeries
     */
    private ArrayList getAssetGrowthSeries(Collection dataSeries, boolean isDefinedBenefit) {
        DataSeries employeeBalance = null;
        DataSeries employerBalance = new DataSeries(EMPLOYER_BALANCE, new Color(88, 139, 146));
        if (!isDefinedBenefit) {
            employeeBalance = new DataSeries(EMPLOYEE_BALANCE, new Color(208, 165, 63));
        }
        Iterator iterator = dataSeries.iterator();
        ArrayList allSeries = new ArrayList();
        while (iterator.hasNext()) {
            AssetGrowth assetGrowth = (AssetGrowth) iterator.next();
            employerBalance.addValue(assetGrowth.getEmployerBalance());
            if (!isDefinedBenefit) {
                employeeBalance.addValue(assetGrowth.getEmployeeBalance());
            }
        }
        allSeries.add(employerBalance);
        if (!isDefinedBenefit) {
            allSeries.add(employeeBalance);
        }
        return allSeries;
    }

    /**
     * set asset growth row titles
     * 
     * @param BarChartBean barChartBean
     * @param Collection dataSeries
     */
    public void setAssetGrowthRowTitles(BarChartBean barChartBean, Collection dataSeries) {
        ArrayList xtitles = new ArrayList();
        ArrayList row1Titles = new ArrayList();
        ArrayList row2Titles = new ArrayList();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/yy");

        Iterator iterator = dataSeries.iterator();
        while (iterator.hasNext()) {
            AssetGrowth assetGrowth = (AssetGrowth) iterator.next();
            row1Titles.add(assetGrowth.getQuarterAsString());

            row2Titles.add(formatter.format(assetGrowth.getMonthEndDate()).toString().trim());
        }

        xtitles.add(row1Titles);
        xtitles.add(row2Titles);
        barChartBean.setXTitles(xtitles);
    }

    /**
     * Returns max asset growth
     * 
     * @param Collection dataSeries
     * @return double max
     */
    public double getMaxAssetGrowth(Collection dataSeries) {
        double max = MAX;
        Iterator iterator = dataSeries.iterator();

        while (iterator.hasNext()) {
            AssetGrowth assetGrowth = (AssetGrowth) iterator.next();
            if (max < (assetGrowth.getEmployeeBalance() + assetGrowth.getEmployerBalance())) {
                max = assetGrowth.getEmployeeBalance() + assetGrowth.getEmployerBalance();
            }
        }

        return max;
    }

    /**
     * reset form values
     * 
     * @param ActionMapping mapping
     * @param ActionForm reportForm
     * @param HttpServletRequest request
     * @return Form reportForm
     * @throws SystemException
     */
    protected ActionForm resetForm( ActionForm form,
            HttpServletRequest request) throws SystemException {
        try {
            ActionForm blankForm = (ActionForm) form.getClass().newInstance();
            PropertyUtils.copyProperties(form, blankForm);
            ((BaseReportForm) form).reset( request);
        } catch (Exception e) {
            throw new SystemException(e, this.getClass().getName(), "resetForm",
                    "exception in resetting the form");
        }

        return form;
    }

    /**
     * This checks if any informational messages already present in the session and adds the current message to the
     * existing messages.
     * 
     * @param request
     * @param messages
     * @param reqAttributeName
     */
    private void setMessagesInRequest(final HttpServletRequest request, final Collection messages,
            final String reqAttributeName) {
        if (messages != null) {
            // check for messages already in session scope
            Collection existingMessages = (Collection) request.getAttribute(reqAttributeName);
            if (existingMessages != null) {
                messages.addAll(existingMessages);
                request.removeAttribute(reqAttributeName);
            }
            request.setAttribute(reqAttributeName, messages);
        }
    }

    /**This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 * 
	 */
	/*@SuppressWarnings("rawtypes")
	public Collection doValidate(ActionMapping mapping, Form form, HttpServletRequest request) {
		Collection penErrors = FrwValidation.doValidatePenTestAutoAction(form, mapping, request, CommonConstants.INPUT);
		if (penErrors != null && penErrors.size() > 0) {
			try {
				loadChartData(mapping, request, getBobContext(request).getCurrentContract(), (ContractSnapshotForm) form);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return penErrors;
		}
		return super.doValidate(mapping, form, request);
	}*/
    
      @Autowired
	  private BDValidatorFWInput  bdValidatorFWInput;
      @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	  binder.bind(request);
	  binder.addValidators(bdValidatorFWInput);
	   
	}
    
}
