package com.manulife.pension.ps.web.contract;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.mail.URLName;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.pagelayout.LayoutBean;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWNull;
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
 * @author 
 */
@SuppressWarnings("deprecation")
@Controller
@RequestMapping(value = "/contract")
@SessionAttributes({"contractSnapshotForm"})
public class ContractSnapshotController extends PsController{

    private static final String CONTRACT_SNAPSHOT_PAGE = "/contract/contractSnapshot.jsp";

    private static final String PRINT_FRIENDLY_PARAM = "printFriendly";

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
    
    public ContractSnapshotController() {
        super(ContractSnapshotController.class);
    }
  
    @ModelAttribute("contractSnapshotForm")
    public ContractSnapshotForm populateForm() {
		return new ContractSnapshotForm(); // populates form for the first time if its null
	} 
    @RequestMapping( value ="/contractSnapshot", method =  RequestMethod.GET)
    public String doExecute(@Valid @ModelAttribute("contractSnapshotForm") ContractSnapshotForm contractSnapshotForm,
			BindingResult bindingResult,HttpServletRequest request, HttpServletResponse response) throws IOException, SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doExecute");
        }
        if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return "redirect:/do" + new UrlPathHelper().getPathWithinServletMapping(request);
			}
		}
		
        // get the user profile object and set the current contract to null
        Contract currentContract = getUserProfile(request).getCurrentContract();

        Date asOfDate = null;

        // reset the form if the URL is the default URL with no parameters
        // this way we always show the default view of the report
        
        if (!request.getParameterNames().hasMoreElements()) {        	
            resetForm( contractSnapshotForm);
        }

        if (!contractSnapshotForm.getIsRecentDate()) {
            asOfDate = new Date();
            asOfDate.setTime(Long.parseLong(contractSnapshotForm.getStringDate()));
        }

        ContractSnapshotVO vo = ContractServiceDelegate.getInstance().getContractSnapshot(
                currentContract.getContractNumber(), asOfDate);

        // The first time the action is called, 1) we pre-load the errors in
        // the action form.
        // 2) select the asOfDate value in the list box stringDate.
        if (contractSnapshotForm.getStringDate().length() == 0) {
            // We use javascript for calculating the participation rate.
            // Therefore we pre-load the errors in the form and we show them
            // through javascript when necessary.
            Collection errorCollection = new ArrayList();
            errorCollection.add(new GenericException(ErrorCodes.EMPTY_VALUE));
            errorCollection.add(new GenericException(ErrorCodes.TOO_SMALL));
            errorCollection.add(new GenericException(ErrorCodes.INVALID_ENTRY));

            try {
                String[] errors = MessageProvider.getInstance().getMessages(errorCollection);
                if (errors == null || errors.length != 3)
                    throw (new ContentException("Contract snapshot: Missing error messages"));
                contractSnapshotForm.setError1(errors[0]);
                contractSnapshotForm.setError2(errors[1]);
                contractSnapshotForm.setError3(errors[2]);
            } catch (ContentException e) {
                SystemException se = new SystemException(e, this.getClass().getName(), "doExecute",
                        "ContentException occurred while getting possible error messages.");
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);
            }

            contractSnapshotForm.setStringDate(String.valueOf(currentContract.getContractDates().getAsOfDate()
                    .getTime()));
        }

        // Should we display the loan amount?
        contractSnapshotForm.setDisplayLoan(currentContract.isLoanFeature()
                || (vo.getPlanAssets().getLoanAssets() != null
                && !new BigDecimal("0.00").equals(vo.getPlanAssets().getLoanAssets())));

        // Should be display Blended Asset Charge ?
        //myForm.setDisplayBlendedAssets(getUserProfile(request).isTPA()
          //      || getUserProfile(request).isInternalUser());
        
        //Date invalidDate = new GregorianCalendar(9999, Calendar.DECEMBER, 31).getTime();
        //if (invalidDate.equals(vo.getPlanAssets().getAssetChargeAsOfDate())) {
        	//myForm.setDisplayBlendedAssets(false);
        //}
        
		// Suppress the display of Blended Asset Charge
        contractSnapshotForm.setDisplayBlendedAssets(false);

        // determine if we should show PBA?
        if (currentContract.isPBA()
                || vo.getPlanAssets().getPersonalBrokerageAccountAmount().doubleValue() > 0) {
        	contractSnapshotForm.setDisplayPba(true);
        }
        // If the contract currently has Lifecycle Funds selected
        // OR the contract has a balance in Lifecycle funds as of the selected date (current or past), then the
        // following will be displayed
        contractSnapshotForm.setDisplayLifecycle(currentContract.getHasLifecycle()
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
            LogUtility.logSystemException(Constants.PS_APPLICATION_ID, se);
        }

        PieChartBean participantStatusPieChartBean = getParticipantStatusPieChartBean(vo);
        PieChartBean assetAllocByRiskPieChartBean = getAssetAllocationByRiskPieChartBean(vo);

        PieChartBean assetAllocBelow30PieChartBean = getAssetAllocationByAgeGroupPieChartBean(vo,
                ContractAssetsByRiskAndParticipantStatusVO.AGE_GROUP_BELOW_30);
        PieChartBean assetAllocBetween30And39PieChartBean = getAssetAllocationByAgeGroupPieChartBean(
                vo, ContractAssetsByRiskAndParticipantStatusVO.AGE_GROUP_BETWEEN_30_39);
        PieChartBean assetAllocBetween40And49PieChartBean = getAssetAllocationByAgeGroupPieChartBean(
                vo, ContractAssetsByRiskAndParticipantStatusVO.AGE_GROUP_BETWEEN_40_49);
        PieChartBean assetAllocBetween50And59PieChartBean = getAssetAllocationByAgeGroupPieChartBean(
                vo, ContractAssetsByRiskAndParticipantStatusVO.AGE_GROUP_BETWEEN_50_59);
        PieChartBean assetAllocBetween60AndAbovePieChartBean = getAssetAllocationByAgeGroupPieChartBean(
                vo, ContractAssetsByRiskAndParticipantStatusVO.AGE_GROUP_60_AND_PLUS);

        if ("true".equals(request.getParameter(PRINT_FRIENDLY_PARAM))) {

            if (logger.isDebugEnabled()) {
                logger.debug("Printer Friendly PDF will be generated...");
            }

            LayoutBean layoutBean = LayoutBeanRepository.getInstance().getPageBean(CONTRACT_SNAPSHOT_PAGE);

            try {
                ContractSnapshotPdf pdf = new ContractSnapshotPdf(contractSnapshotForm.getStringDate(), contractSnapshotForm
                        .getDisplayLoan(), contractSnapshotForm.getDisplayPba(), contractSnapshotForm.getDisplayLifecycle(),
                        contractSnapshotForm.getIsRecentDate(), contractSnapshotForm.isDisplayBlendedAssets(),
                        getUserProfile(request), getRequestUrlName(request), layoutBean, vo,
                        assetGrowthBarChart, contributionWithdrawalsBarChart,
                        participantStatusPieChartBean, assetAllocByRiskPieChartBean,
                        assetAllocBelow30PieChartBean, assetAllocBetween30And39PieChartBean,
                        assetAllocBetween40And49PieChartBean, assetAllocBetween50And59PieChartBean,
                        assetAllocBetween60AndAbovePieChartBean);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                pdf.writeTo(bos);

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
                 * No need to forward to any other JSP or action. Returns null will make  to return controls back
                 * to server immediately.
                 */
                return null;
            } catch (ContentException e) {
                SystemException se = new SystemException(e, this.getClass().getName(), "doExecute",
                        "ContentException occurred while generating the PDF.");
                throw se;
            }
        } else {

            if (logger.isDebugEnabled())
                logger.debug(ContractProfileController.class.getName()
                        + ":forwarding to Contract Snapshot Page.");

            request.setAttribute(Constants.CONTRACT_SNAPSHOT, vo);

            request.setAttribute(Constants.CONTRACT_SNAPSHOT_ASSET_GROWTH, assetGrowthBarChart);
            request.setAttribute(Constants.CONTRACT_SNAPSHOT_CONTR_WITHDRAWALS,
                    contributionWithdrawalsBarChart);

            request.setAttribute(Constants.CONTRACT_SNAPSHOT_STATUS_PIECHART,
                    participantStatusPieChartBean);

            request.setAttribute(Constants.CONTRACT_SNAPSHOT_RISK_PIECHART,
                    assetAllocByRiskPieChartBean);
            request.setAttribute(Constants.CONTRACT_SNAPSHOT_AGE_BELOW_30_PIECHART,
                    assetAllocBelow30PieChartBean);
            request.setAttribute(Constants.CONTRACT_SNAPSHOT_AGE_30_39_PIECHART,
                    assetAllocBetween30And39PieChartBean);
            request.setAttribute(Constants.CONTRACT_SNAPSHOT_AGE_40_49_PIECHART,
                    assetAllocBetween40And49PieChartBean);
            request.setAttribute(Constants.CONTRACT_SNAPSHOT_AGE_50_59_PIECHART,
                    assetAllocBetween50And59PieChartBean);
            request.setAttribute(Constants.CONTRACT_SNAPSHOT_AGE_60_ABOVE_PIECHART,
                    assetAllocBetween60AndAbovePieChartBean);

            if (logger.isDebugEnabled()) {
                logger.debug("exit <- doExecute");
            }

            return CONTRACT_SNAPSHOT_PAGE;
        }
    }

    private URLName getRequestUrlName(HttpServletRequest request) {
        StringBuffer urlSb = HttpUtils.getRequestURL(request);
        return new URLName(urlSb.toString());
    }

    private PieChartBean createDefaultPieChartBean() {
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
        return pieChart;
    }

    private PieChartBean getParticipantStatusPieChartBean(ContractSnapshotVO vo) {
        PieChartBean pieChart = createDefaultPieChartBean();

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getParticipantStatusPieChartBean");
        }

        if (vo.getContractAssetsByRisk().getHasEmployeeMoneyType()) {
            pieChart.addPieWedge("wedge1", vo.getContractAssetsByRisk()
                    .getActiveParticipantsNumber(),
                    Constants.ParticipantStatusPieChart.COLOR_ACTIVE, " ", "1",
                    Constants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, 0);
            pieChart.addPieWedge("wedge2", vo.getContractAssetsByRisk()
                    .getInactiveWithBalanceParticipantsNumber(),
                    Constants.ParticipantStatusPieChart.COLOR_INACTIVE_WITH_BALANCE, " ", "1",
                    Constants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, 0);
            pieChart.addPieWedge("wedge3", vo.getContractAssetsByRisk()
                    .getInactiveWithUMParticipantsNumber(),
                    Constants.ParticipantStatusPieChart.COLOR_INACTIVE_UNINVESTED_MONEY, " ", "1",
                    Constants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, 0);
            pieChart.setSuppressWedgesFromKey("wedge1;wedge2;wedge3");

        } else {
            pieChart.addPieWedge("wedge1", vo.getContractAssetsByRisk()
                    .getActiveContributingParticipantsNumber(),
                    Constants.ParticipantStatusPieChart.COLOR_ACTIVE_AND_CONTRIBUTING, " ", "1",
                    Constants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, 0);
            pieChart.addPieWedge("wedge2", vo.getContractAssetsByRisk()
                    .getActiveOptedOutParticipantsNumber(),
                    Constants.ParticipantStatusPieChart.COLOR_ACTIVE_OPTED_OUT, " ", "1",
                    Constants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, 0);
            pieChart.addPieWedge("wedge3", vo.getContractAssetsByRisk()
                    .getActiveNoBalanceParticipantsNumber(),
                    Constants.ParticipantStatusPieChart.COLOR_ACTIVE_NO_BALANCE, " ", "1",
                    Constants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, 0);
            pieChart.addPieWedge("wedge4", vo.getContractAssetsByRisk()
                    .getActiveNotContributingParticipantsNumber(),
                    Constants.ParticipantStatusPieChart.COLOR_ACTIVE_NOT_CONTRIBUTING, " ", "1",
                    Constants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, 0);
            pieChart.addPieWedge("wedge5", vo.getContractAssetsByRisk()
                    .getInactiveWithBalanceParticipantsNumber(),
                    Constants.ParticipantStatusPieChart.COLOR_INACTIVE_WITH_BALANCE, " ", "1",
                    Constants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, 0);
            pieChart.addPieWedge("wedge6", vo.getContractAssetsByRisk()
                    .getInactiveWithUMParticipantsNumber(),
                    Constants.ParticipantStatusPieChart.COLOR_INACTIVE_UNINVESTED_MONEY, " ", "1",
                    Constants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, 0);
            pieChart.addPieWedge("wedge7", vo.getContractAssetsByRisk()
                    .getOptedOutNotVestedParticipantsNumber(),
                    Constants.ParticipantStatusPieChart.COLOR_OPTED_OUT_NOT_VESTED, " ", "1",
                    Constants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, 0);
            pieChart.setSuppressWedgesFromKey("wedge1;wedge2;wedge3;wedge4;wedge5;wedge6;wedge7");
        }

        pieChart.setAppletWidth(95);
        pieChart.setAppletHeight(100);

        pieChart.setPieWidth(95);
        pieChart.setPieHeight(95);
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
        pieChart.setAppletWidth(95);
        pieChart.setAppletHeight(100);
        pieChart.setPieWidth(95);
        pieChart.setPieHeight(95);
        ContractAssetsByRiskAndParticipantStatusVO byRiskVo = vo.getContractAssetsByRisk();
        for (int i = 0; i < RISK_GROUPS.length; i++) {
            pieChart.addPieWedge("wedge" + (i + 1), (float) byRiskVo
                    .getTotalAssetsByRisk(RISK_GROUPS[i]),
                    Constants.AssetAllocationPieChart.COLOR_WEDGES[i], " ", String.valueOf(i + 1),
                    Constants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, 0);

        }

        return pieChart;
    }

    private PieChartBean getAssetAllocationByAgeGroupPieChartBean(ContractSnapshotVO vo,
            String ageGroup) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getAssetAllocationByAgeGroupPieChartBean");
        }

        ContractAssetsByRiskAndParticipantStatusVO byRiskVo = vo.getContractAssetsByRisk();
        ContractAssetsByRiskAndParticipantStatusVO.AssetsByAgeGroup assetsByAgeGroup = byRiskVo
                .getAgeGroup(ageGroup);
        PieChartBean pieChart = createDefaultPieChartBean();
        pieChart.setAppletWidth(43);
        pieChart.setAppletHeight(48);
        pieChart.setPieWidth(43);
        pieChart.setPieHeight(43);
        pieChart.setShowWedgeLabels(false);
        for (int i = 0; i < RISK_GROUPS.length; i++) {
            pieChart.addPieWedge("wedge" + (i + 1), (float) assetsByAgeGroup
                    .getAssetTotal(RISK_GROUPS[i]),
                    Constants.AssetAllocationPieChart.COLOR_WEDGES[i], null, null,
                    Constants.AssetAllocationPieChart.COLOR_WEDGE_LABEL, 0);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getAssetAllocationByAgeGroupPieChartBean");
        }

        return pieChart;
    }

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

    private ArrayList getContributionsSeries(Collection dataSeries, boolean isDefinedBenefit) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getContributionsSeries");
        }
        DataSeries employeeContributions = null;
        DataSeries employerContributions = null;

        if (!isDefinedBenefit) {
            employerContributions = new DataSeries("Employer contributions",
                    new Color(88, 139, 146));
            employeeContributions = new DataSeries("Employee contributions",
                    new Color(208, 165, 63));
        } else {
            employerContributions = new DataSeries("Contributions", new Color(88, 139, 146));
        }
        DataSeries withdrawal = new DataSeries("Distributions", new Color(157, 108, 142));

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

    private ArrayList getAssetGrowthSeries(Collection dataSeries, boolean isDefinedBenefit) {
        DataSeries employeeBalance = null;
        DataSeries employerBalance = new DataSeries("Employer Balance", new Color(88, 139, 146));
        if (!isDefinedBenefit) {
            employeeBalance = new DataSeries("Employee Balance", new Color(208, 165, 63));
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

   private void resetForm(ContractSnapshotForm form){
	   form.setDisplayBlendedAssets(false);
	   form.setDisplayLifecycle(false);
	   form.setDisplayLoan(true);
	   form.setDisplayPba(false);
	   form.setError1("");
	   form.setError2("");
	   form.setError3("");
	   form.setIsRecentDate(true);
	   form.setStringDate("");
	   
   }
    
	@Autowired
    private PSValidatorFWNull psValidatorFWNull; 
	
    @InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWNull);
	}
}
