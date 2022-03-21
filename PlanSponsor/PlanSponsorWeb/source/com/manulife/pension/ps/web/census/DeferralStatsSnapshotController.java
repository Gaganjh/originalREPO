package com.manulife.pension.ps.web.census;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.service.report.census.valueobject.DeferralReportData;
import com.manulife.pension.ps.service.report.census.valueobject.DeferralStatisticsSummary;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.DeferralUtils;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.util.barchart.BarChartException;
import com.manulife.util.barchart.DataSeries;
import com.manulife.util.barchart.Grid;
import com.manulife.util.barchart.Legend;
import com.manulife.util.barchart.Point;
import com.manulife.util.barchart.Rollover;
import com.manulife.util.barchart.taglib.BarChartBean;
import com.manulife.util.piechart.PieChartBean;

/**
 * 
 * @author patuadr
 */
@Controller
@RequestMapping(value = "/census")
@SessionAttributes({"deferralReportForm"})

public final class DeferralStatsSnapshotController extends PsController {
	
	@ModelAttribute("deferralReportForm")
	public  DeferralReportForm  populateForm()
	{
		return new  DeferralReportForm ();
		}

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/census/deferralReport.jsp"); 
		forwards.put("default","/census/deferralStatsSnapshot.jsp");
	}
	
	
	
	
    private static final String PIE_CHART_APPLET_ARCHIVE = "/assets/unmanaged/applets/pieChartApplet.jar";

    private static final String BAR_CHART_APPLET_ARCHIVE = "/assets/unmanaged/applets/barChartApplet.jar";

    private static final int DELTA = 250;

    private static final int BARCHART_HEIGHT = 580;

    private static final int BARCHART_WIDTH = 350;

    private static final int BARCHART_BAR_WIDTH = 11;

    private static final int BARCHART_BCKGRD_RED = 255;

    private static final int BARCHART_BCKGRD_BLUE = 255;

    private static final int BARCHART_BCKGRD_GREEN = 255;

    private static final String DECIMAL_FORMAT = "##0;(##0)";

    /**
     * Constructor for DeferralStatsSnapshotAction
     */
    public DeferralStatsSnapshotController() {
        super(DeferralStatsSnapshotController.class);
    }

    /**
     * Validate the input form. The search field must not be empty.
     * 
     * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
     *      org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
     */
    @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;
    @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
    /**
     * @throws SystemException
     * @see PsController#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
	  
	    @RequestMapping(value ="/deferralStatsSnapshot/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
		public String doExecute(@Valid @ModelAttribute("employeeEnrollmentSummaryReportForm") EmployeeEnrollmentSummaryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
	    	if(bindingResult.hasErrors()){
	        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	        	if(errDirect!=null){
	        		DeferralReportData reportData = new DeferralReportData(null, 0);
	    			request.setAttribute(Constants.REPORT_BEAN, reportData);
	             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	        	}
	        }
	    	
        int contractId = getUserProfile(request).getCurrentContract().getContractNumber();
        try {
            ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();
            ContractServiceFeature csf = delegate.getContractServiceFeature(contractId,
                    Constants.MANAGING_DEFERRALS);
            if (csf != null) {
                String planDeferralType = csf.getAttributeValue(Constants.medDeferralType);
                request.setAttribute("deferralType", planDeferralType);

            }
        } catch (ApplicationException e) {
            throw new ServletException(e);
        }

        DeferralStatisticsSummary vo = DeferralUtils.getStatisticsSummary(contractId);

        request.setAttribute(Constants.ACI_PARTICIPATION_PIECHART,
                geACIParticipationPieChartBean(vo));

        try {
            request.setAttribute(Constants.PERCENT_DEFERRALS_BARCHART,
                    getPercentDeferralsBarChartBean(vo));
            request.setAttribute(Constants.DOLLAR_DEFERRALS_BARCHART,
                    getAmountDeferralsBarChartBean(vo));
            request.setAttribute(Constants.UNKNOWN, Integer.toString(vo.getUnknown()));
        } catch (BarChartException e) {
            throw new ServletException(e);
        }

        return forwards.get("default");
    }

    /**
     * Sets up default chart
     * 
     * @return
     */
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
        pieChart.setKeyFontSize(10);
        pieChart.setFontBold(true);
        pieChart.setDrawBorders(true);
        pieChart.setShowValueWithKey(true);
        return pieChart;
    }

    /**
     * Sets up default chart
     * 
     * @return
     */
    private BarChartBean createDefaultBarChartBean() {
        BarChartBean barChartBean = new BarChartBean();

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> createDefaultBarChartBean");
        }

        barChartBean.setHeight(BARCHART_HEIGHT);
        barChartBean.setWidth(BARCHART_WIDTH);
        barChartBean.setBarWidth(BARCHART_BAR_WIDTH);
        barChartBean.setBackgroundColor(new Color(BARCHART_BCKGRD_RED, BARCHART_BCKGRD_GREEN,
                BARCHART_BCKGRD_BLUE));
        barChartBean.setArchive(BAR_CHART_APPLET_ARCHIVE);
        barChartBean.setDecimalFormatString(DECIMAL_FORMAT);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- createDefaultBarChartBean");
        }

        return barChartBean;
    }

    /**
     * Creates the pie chart based on data passed in the value object
     * 
     * @param vo
     * @return
     */
    private PieChartBean geACIParticipationPieChartBean(DeferralStatisticsSummary vo) {
        PieChartBean pieChart = createDefaultPieChartBean();

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getEnrollmentMethodPieChartBean");
        }

        pieChart.addPieWedge("wedge1", vo.getDefaultSettings(),
                Constants.ParticipationRatePieChart.PARTICIPANTS, "wedge1", "wedge1",
                Constants.ParticipationRatePieChart.COLOR_WEDGE_LABEL, 0);
        pieChart.addPieWedge("wedge2", vo.getActivelyManaged(),
                Constants.ParticipationRatePieChart.OPT_OUT, "wedge2", "wedge2",
                Constants.ParticipationRatePieChart.COLOR_WEDGE_LABEL, 0);
        pieChart.addPieWedge("wedge3", vo.getEnrolledButNotSignedUp(),
                Constants.ParticipationRatePieChart.PENDING_ELIGIBILITY, "wedge3", "wedge3",
                Constants.ParticipationRatePieChart.COLOR_WEDGE_LABEL, 0);
        pieChart.addPieWedge("wedge4", vo.getNotEnrolled(),
                Constants.ParticipationRatePieChart.PENDING_ENROLLMENT, "wedge4", "wedge4",
                Constants.ParticipationRatePieChart.COLOR_WEDGE_LABEL, 0);
        // pieChart.setSuppressWedgesFromKey("wedge1;wedge2;wedge3");

        pieChart.setAppletWidth(95);
        pieChart.setAppletHeight(100);

        pieChart.setPieWidth(95);
        pieChart.setPieHeight(95);
        // pieChart.setWedgeLabelOffset(68);
        // pieChart.setPrecision(2);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getEnrollmentMethodPieChartBean");
        }

        return pieChart;

    }

    /**
     * Creates the bar chart based on data passed in the value object
     * 
     * @param vo
     * @return
     * @throws BarChartException
     */
    private BarChartBean getPercentDeferralsBarChartBean(DeferralStatisticsSummary vo)
            throws BarChartException {
        BarChartBean barChartBean = createDefaultBarChartBean();

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getPercentDeferralsBarChartBean");
        }

        Grid grid = new Grid();
        grid.setPosition(new Point(80, 50));
        grid.setSize(new Point(250, 500));
        grid.setColor(new Color(133, 133, 133));
        grid.setXLabelColor(new Color(0, 0, 0));
        grid.setYLabelColor(new Color(0, 0, 0));

        grid.setMinY(0);
        grid.setMaxY(250);

        grid.setAxisYValue(0);
        // grid.setTickDeltaY(DELTA);
        // grid.setGridDeltaY(DELTA);
        grid.setTickDeltaY(Math.round((grid.getMaxY() - grid.getMinY()) / 5));
        grid.setGridDeltaY(Math.round((grid.getMaxY() - grid.getMinY()) / 5));
        grid.setShowYGrid(true);
        grid.setShowYTicks(true);

        grid.setBarLeftOffset(11);
        grid.setBarDeltaOffset(30);
        grid.setBarGridDeltaX(150);
        grid.setShowXGrid(true);
        grid.setShowXTicks(true);

        // Override the format for Y
        grid.setFormatString(DECIMAL_FORMAT);

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
        rollover.setGeneralText(new String[] { "Roll your mouse over", "a bar to see values" });
        rollover.setFormatString(DECIMAL_FORMAT);
        barChartBean.setRollover(rollover);

        setPercentXTitles(barChartBean);

        barChartBean.setAllSeries(getPercentDataSeries(vo));

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getPercentDeferralsBarChartBean");
        }

        return barChartBean;
    }

    private void setPercentXTitles(BarChartBean barChartBean) {
        ArrayList<String> dataSeries = new ArrayList<String>();
        dataSeries.add("2");
        dataSeries.add("4");
        dataSeries.add("6");
        dataSeries.add("8");
        dataSeries.add("10");
        dataSeries.add("12");
        dataSeries.add(">12");

        ArrayList xtitles = new ArrayList();

        xtitles.add(dataSeries);

        barChartBean.setXTitles(xtitles);
    }

    private void setAmountXTitles(BarChartBean barChartBean) {
        ArrayList<String> dataSeries = new ArrayList<String>();
        dataSeries.add("50");
        dataSeries.add("100");
        dataSeries.add("200");
        dataSeries.add("300");
        dataSeries.add("400");
        dataSeries.add("500");
        dataSeries.add("600");
        dataSeries.add("700");
        dataSeries.add("800");
        dataSeries.add(">800");

        ArrayList xtitles = new ArrayList();

        xtitles.add(dataSeries);

        barChartBean.setXTitles(xtitles);
    }

    private ArrayList getPercentDataSeries(DeferralStatisticsSummary vo) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getContributionsSeries");
        }

        DataSeries traditionalDeferrals = new DataSeries("Before Tax", new Color(88, 139, 146));
        DataSeries rothDeferrals = new DataSeries("Roth 401(k)", new Color(208, 165, 63));

        ArrayList allSeries = new ArrayList();
        // Just testing
        // vo.setRothLessThan2Pct(0);
        // vo.setRothLessThan4Pct(0);
        // vo.setRothLessThan6Pct(0);
        // vo.setRothLessThan8Pct(0);
        // vo.setRothLessThan10Pct(0);
        // vo.setRothLessThan12Pct(0);
        // vo.setRothMoreThan12Pct(0);
        // vo.setTraditionalLessThan2Pct(0);
        // vo.setTraditionalLessThan4Pct(1);
        // vo.setTraditionalLessThan6Pct(2);
        // vo.setTraditionalLessThan8Pct(3);
        // vo.setTraditionalLessThan10Pct(4);
        // vo.setTraditionalLessThan12Pct(5);
        // vo.setTraditionalMoreThan12Pct(6);
        // Just testing

        rothDeferrals.addValue(vo.getRothLessThan2Pct());
        rothDeferrals.addValue(vo.getRothLessThan4Pct());
        rothDeferrals.addValue(vo.getRothLessThan6Pct());
        rothDeferrals.addValue(vo.getRothLessThan8Pct());
        rothDeferrals.addValue(vo.getRothLessThan10Pct());
        rothDeferrals.addValue(vo.getRothLessThan12Pct());
        rothDeferrals.addValue(vo.getRothMoreThan12Pct());

        traditionalDeferrals.addValue(vo.getTraditionalLessThan2Pct());
        traditionalDeferrals.addValue(vo.getTraditionalLessThan4Pct());
        traditionalDeferrals.addValue(vo.getTraditionalLessThan6Pct());
        traditionalDeferrals.addValue(vo.getTraditionalLessThan8Pct());
        traditionalDeferrals.addValue(vo.getTraditionalLessThan10Pct());
        traditionalDeferrals.addValue(vo.getTraditionalLessThan12Pct());
        traditionalDeferrals.addValue(vo.getTraditionalMoreThan12Pct());

        allSeries.add(traditionalDeferrals);
        allSeries.add(rothDeferrals);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getContributionsSeries");
        }

        return allSeries;
    }

    private ArrayList getAmountDataSeries(DeferralStatisticsSummary vo) {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getContributionsSeries");
        }

        DataSeries traditionalDeferrals = new DataSeries("Before Tax", new Color(88, 139, 146));
        DataSeries rothDeferrals = new DataSeries("Roth 401(k)", new Color(208, 165, 63));

        ArrayList allSeries = new ArrayList();
        // Just testing
        // vo.setRothLessThan50$(10);
        // vo.setRothLessThan100$(20);
        // vo.setRothLessThan200$(30);
        // vo.setRothLessThan300$(40);
        // vo.setRothLessThan400$(50);
        // vo.setRothLessThan500$(60);
        // vo.setRothLessThan600$(70);
        // vo.setRothLessThan700$(80);
        // vo.setRothLessThan800$(90);
        // vo.setRothMoreThan800$(100);
        // Just testing

        rothDeferrals.addValue(vo.getRothLessThan50$());
        rothDeferrals.addValue(vo.getRothLessThan100$());
        rothDeferrals.addValue(vo.getRothLessThan200$());
        rothDeferrals.addValue(vo.getRothLessThan300$());
        rothDeferrals.addValue(vo.getRothLessThan400$());
        rothDeferrals.addValue(vo.getRothLessThan500$());
        rothDeferrals.addValue(vo.getRothLessThan600$());
        rothDeferrals.addValue(vo.getRothLessThan700$());
        rothDeferrals.addValue(vo.getRothLessThan800$());
        rothDeferrals.addValue(vo.getRothMoreThan800$());

        traditionalDeferrals.addValue(vo.getTraditionalLessThan50$());
        traditionalDeferrals.addValue(vo.getTraditionalLessThan100$());
        traditionalDeferrals.addValue(vo.getTraditionalLessThan200$());
        traditionalDeferrals.addValue(vo.getTraditionalLessThan300$());
        traditionalDeferrals.addValue(vo.getTraditionalLessThan400$());
        traditionalDeferrals.addValue(vo.getTraditionalLessThan500$());
        traditionalDeferrals.addValue(vo.getTraditionalLessThan600$());
        traditionalDeferrals.addValue(vo.getTraditionalLessThan700$());
        traditionalDeferrals.addValue(vo.getTraditionalLessThan800$());
        traditionalDeferrals.addValue(vo.getTraditionalMoreThan800$());

        allSeries.add(traditionalDeferrals);
        allSeries.add(rothDeferrals);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getContributionsSeries");
        }

        return allSeries;
    }

    /**
     * Creates the bar chart based on data passed in the value object
     * 
     * @param vo
     * @return
     * @throws BarChartException
     */
    private BarChartBean getAmountDeferralsBarChartBean(DeferralStatisticsSummary vo)
            throws BarChartException {
        BarChartBean barChartBean = createDefaultBarChartBean();

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getAmountDeferralsBarChartBean");
        }

        Grid grid = new Grid();
        grid.setPosition(new Point(80, 50));
        grid.setSize(new Point(250, 500));
        grid.setColor(new Color(133, 133, 133));
        grid.setXLabelColor(new Color(0, 0, 0));
        grid.setYLabelColor(new Color(0, 0, 0));

        grid.setMinY(0);
        grid.setMaxY(250);

        grid.setAxisYValue(0);
        // grid.setTickDeltaY(DELTA);
        // grid.setGridDeltaY(DELTA);
        grid.setTickDeltaY(Math.round((grid.getMaxY() - grid.getMinY()) / 5));
        grid.setGridDeltaY(Math.round((grid.getMaxY() - grid.getMinY()) / 5));
        grid.setShowYGrid(true);
        grid.setShowYTicks(true);

        // Override the format for Y
        grid.setFormatString(DECIMAL_FORMAT);

        grid.setBarLeftOffset(11);
        grid.setBarDeltaOffset(26);
        grid.setBarGridDeltaX(208);
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
        rollover.setGeneralText(new String[] { "Roll your mouse over", "a bar to see values" });
        rollover.setFormatString(DECIMAL_FORMAT);
        barChartBean.setRollover(rollover);

        setAmountXTitles(barChartBean);

        barChartBean.setAllSeries(getAmountDataSeries(vo));

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getAmountDeferralsBarChartBean");
        }

        return barChartBean;
    }
}
