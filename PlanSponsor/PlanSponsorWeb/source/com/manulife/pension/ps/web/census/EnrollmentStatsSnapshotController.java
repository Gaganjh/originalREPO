package com.manulife.pension.ps.web.census;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.service.report.census.valueobject.StatisticsSummary;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.CensusUtils;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.util.piechart.PieChartBean;

/**
 * 
 * @author patuadr
 */
@Controller
@RequestMapping( value = "/census/enrollmentStatsSnapshot/")
@SessionAttributes({" employeeEnrollmentSummaryReportForm"})

public final class EnrollmentStatsSnapshotController extends PsController {
	@ModelAttribute(" employeeEnrollmentSummaryReportForm")
	public  EmployeeEnrollmentSummaryReportForm populateForm()
	{
		return new  EmployeeEnrollmentSummaryReportForm();
		}
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/census/employeeEnrollmentSummaryReport.jsp"); 
		forwards.put("default","/census/enrollmentStatsSnapshot.jsp");
	}

	
    private static final String PIE_CHART_APPLET_ARCHIVE = "/assets/unmanaged/applets/pieChartApplet.jar";

    /**
     * Constructor for EmployeeEnrollmentSummaryReportAction
     */
    public EnrollmentStatsSnapshotController() {
        super(EnrollmentStatsSnapshotController.class);
    }

  


    /**
     * @throws SystemException
     * @see PsController#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    @RequestMapping( method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("employeeEnrollmentSummaryReportForm") EmployeeEnrollmentSummaryReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        } 
   
        int contractId = getUserProfile(request).getCurrentContract().getContractNumber();

        StatisticsSummary vo = CensusUtils.getStatisticsSummary(contractId);

        request.setAttribute(Constants.ENROLLMENT_METHOD_PIECHART,
                getEnrollmentMethodPieChartBean(vo));

        request.setAttribute(Constants.PARTICIPATION_RATE_PIECHART,
                getParticipationRateChartBean(vo));

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
        // pieChart.setWedgeLabelOffset(35);
        pieChart.setFontSize(10);
        pieChart.setKeyFontSize(10);
        pieChart.setFontBold(true);
        pieChart.setDrawBorders(true);
        pieChart.setShowValueWithKey(true);
        return pieChart;
    }

    /**
     * Creates the pie chart based on data passed in the value object
     * 
     * @param vo
     * @return
     */
    private PieChartBean getEnrollmentMethodPieChartBean(StatisticsSummary vo) {
        PieChartBean pieChart = createDefaultPieChartBean();

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getEnrollmentMethodPieChartBean");
        }

        pieChart.addPieWedge("wedge1", vo.getAutoEnrolled(),
                Constants.EnrollmentMethodPieChart.AUTO, "wedge1", "wedge1",
                Constants.EnrollmentMethodPieChart.COLOR_WEDGE_LABEL, 0);
        pieChart.addPieWedge("wedge2", vo.getDefaultEnrolled(),
                Constants.EnrollmentMethodPieChart.DEFAULT, "wedge2", "wedge2",
                Constants.EnrollmentMethodPieChart.COLOR_WEDGE_LABEL, 0);
        pieChart.addPieWedge("wedge3", vo.getPaperEnrolled(),
                Constants.EnrollmentMethodPieChart.PAPER, "wedge3", "wedge3",
                Constants.EnrollmentMethodPieChart.COLOR_WEDGE_LABEL, 0);
        pieChart.addPieWedge("wedge4", vo.getInternetEnrolled(),
                Constants.EnrollmentMethodPieChart.INTERNET, "wedge4", "wedge4",
                Constants.EnrollmentMethodPieChart.COLOR_WEDGE_LABEL, 0);
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
     * Creates the pie chart based on data passed in the value object
     * 
     * @param vo
     * @return
     */
    private PieChartBean getParticipationRateChartBean(StatisticsSummary vo) {
        PieChartBean pieChart = createDefaultPieChartBean();

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getParticipationRateChartBean");
        }

        pieChart.addPieWedge("wedge1", vo.getParticipants(),
                Constants.ParticipationRatePieChart.PARTICIPANTS, "wedge1", "wedge1",
                Constants.ParticipationRatePieChart.COLOR_WEDGE_LABEL, 0);
        pieChart.addPieWedge("wedge2", vo.getOptOut(), Constants.ParticipationRatePieChart.OPT_OUT,
                "wedge2", "wedge2", Constants.ParticipationRatePieChart.COLOR_WEDGE_LABEL, 0);
        pieChart.addPieWedge("wedge3", vo.getPendingEligibility(),
                Constants.ParticipationRatePieChart.PENDING_ELIGIBILITY, "wedge3", "wedge3",
                Constants.ParticipationRatePieChart.COLOR_WEDGE_LABEL, 0);
        pieChart.addPieWedge("wedge4", vo.getPendingEnrollment(),
                Constants.ParticipationRatePieChart.PENDING_ENROLLMENT, "wedge4", "wedge4",
                Constants.ParticipationRatePieChart.COLOR_WEDGE_LABEL, 0);
        pieChart.addPieWedge("wedge5", vo.getNotEligible(),
                Constants.ParticipationRatePieChart.NOT_ELIGIBLE, "wedge5", "wedge5",
                Constants.ParticipationRatePieChart.COLOR_WEDGE_LABEL, 0);
        // pieChart.setSuppressWedgesFromKey("wedge1;wedge2;wedge3");

        pieChart.setAppletWidth(95);
        pieChart.setAppletHeight(100);

        pieChart.setPieWidth(95);
        pieChart.setPieHeight(95);
        // pieChart.setWedgeLabelOffset(68);
        // pieChart.setPrecision(2);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getParticipationRateChartBean");
        }

        return pieChart;

    }
    /**
     * Validate the input form. The search field must not be empty.
     * 
     * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
     *      org.apache.struts.action.Form, javax.servlet.http.HttpServletRequest)
     */

	@Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
}
