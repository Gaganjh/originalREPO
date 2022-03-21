package com.manulife.pension.ps.web.noticereports;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentHistoryReportData;
import com.manulife.pension.ps.service.report.noticereports.reporthandler.OrderStatusReportHandler;
import com.manulife.pension.ps.service.report.noticereports.valueobject.OrderStatusReportData;
import com.manulife.pension.ps.service.report.noticereports.valueobject.OrderStatusReportVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.piechart.PieChartBean;

@Controller
@RequestMapping( value ="/noticereports")
@SessionAttributes({"alertsReportForm"})

public class OrderStatusReportController extends ReportController {

	@ModelAttribute("alertsReportForm") 
	public AlertsReportForm populateForm()
	{
		return new AlertsReportForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static {
		forwards.put("input", "/noticereports/orderStatusReport.jsp");
		forwards.put("default", "/noticereports/orderStatusReport.jsp");
		forwards.put("search", "/noticereports/orderStatusReport.jsp");
		forwards.put("orderStatusReportPage", "/noticereports/orderStatusReport.jsp");
		forwards.put("print", "/noticereports/orderStatusReport.jsp");
	}

	    private static final String DEFAULT_SORT_FIELD = PlanDocumentHistoryReportData.ACTION_DATE_FIELD;

	    private static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;

	    public static final String REPORT_NAME = "reportName";

	    public static final String REPORT_TITLE = "Order Status as of ";

	   //SimpleDateFormat is converted to FastDateFormat to make it thread safe		
	    private FastDateFormat simpleDateFormat = FastDateFormat.getInstance(
			"MM/dd/yyyy", Locale.US);
	    private static final String PIE_CHART_APPLET_ARCHIVE = "/assets/unmanaged/applets/pieChartApplet.jar";

	    private static final String WEDGE1 = "wedge1";

	    private static final String WEDGE2 = "wedge2";

	    private static final String WEDGE3 = "wedge3";

	    private static final String WEDGE4 = "wedge4";

	    private static final String WEDGE5 = "wedge5";
	    
	    private static final String WEDGE6 = "wedge6";

	    public static final String WEDGE_FONT_COLOR = "#FFFFFF";

	    public static final String ORDER_STATUS_INITIATED_COLOR_WEDGE_LABEL = "#4572A7";

	    public static final String ORDER_STATUS_COMPLETED_COLOR_WEDGE_LABEL = "#DB843D";

	    public static final String ORDER_STATUS_NOTCOMPLETED_COLOR_WEDGE_LABEL = "#89A54E";

	    public static final String ORDER_STATUS_INPROGRESS_COLOR_WEDGE_LABEL = "#71588F";

	    public static final String ORDER_STATUS_ERROR_IN_FILE_COLOR_WEDGE_LABEL = "#AA4643";
	    
	    public static final String ORDER_STATUS_CANCELLED_COLOR_WEDGE_LABEL = "#3794AE";

	    public static final String ORDER_STATUS_FREQUENCY_PIECHART = "pieChart";

	    public static final String SINGLE_SPACE_SYMBOL = " ";

	    public static final String VIEWING_PREFERENCE = "1";

	    public static final int NUMBER_0 = 0;

	    public static final String COLOR_BORDER = "#EAEAEA";
	    
	    public static final String BLANK = "";

   
	     
	    /* (non-Javadoc)
	     * @see com.manulife.pension.platform.web.report.BaseReportController#doCommon(com.manulife.pension.platform.web.report.BaseReportForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	     */
	    public String doCommon(BaseReportForm form, HttpServletRequest request,HttpServletResponse response) 
	    throws SystemException {
	    	
		super.doCommon( form, request, response);

		OrderStatusReportData reportData = (OrderStatusReportData) request
				.getAttribute(CommonConstants.REPORT_BEAN);
		
		 PieChartBean orderStatusFrequencyPieChartBean = getOrderStatusFrequencyPieChartBean(reportData);

	     request.setAttribute(ORDER_STATUS_FREQUENCY_PIECHART, orderStatusFrequencyPieChartBean);

		return forwards.get("orderStatusReportPage");
	}

		
	/** 
	 * Method for search functionality.
	 * @param form
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/orderStatusReport/" ,params={"task=search"}   , method =  {RequestMethod.POST}) 
	public String doSearch (@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			OrderStatusReportData report = new OrderStatusReportData();
			report.setReportCriteria(new ReportCriteria(OrderStatusReportHandler.REPORT_ID));
			report.setDetails(new Vector());
			request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		Collection<GenericException> errors = doValidate(form, request);
		if (!errors.isEmpty()) {
			SessionHelper.setErrorsInSession(request, errors);
			OrderStatusReportData report = new OrderStatusReportData();
			report.setReportCriteria(new ReportCriteria(OrderStatusReportHandler.REPORT_ID));
			request.setAttribute(Constants.REPORT_BEAN, report);
			return forwards.get("input");
		}
		String forward = doCommon(form, request, response);
		return forward;
	}

	
	/**
	 * Method for reset functionality.
	 * @param form
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/orderStatusReport/", params={"task=reset"} , method =  {RequestMethod.POST}) 
	public String doReset (@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			OrderStatusReportData report = new OrderStatusReportData();
            report.setReportCriteria(new ReportCriteria(OrderStatusReportHandler.REPORT_ID));
            report.setDetails(new Vector());
            request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
		}
		}
		Collection<GenericException> errors = super.doValidate(
				form, request);
		
		form.setFromDate(form.getFromDefaultDate());
		form.setToDate(form.getDefaultDate());
		form.setContractNumber(BLANK);
		doCommon( form, request, response);
		return forwards.get("default");

	}
	/**
	 * @param form
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/orderStatusReport/", method =  {RequestMethod.GET}) 
	public String doDefault (@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			OrderStatusReportData report = new OrderStatusReportData();
            report.setReportCriteria(new ReportCriteria(OrderStatusReportHandler.REPORT_ID));
            report.setDetails(new Vector());
            request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
		
		
		/**
		 * @param form
		 * @param bindingResult
		 * @param request
		 * @param response
		 * @return
		 * @throws IOException
		 * @throws ServletException
		 * @throws SystemException
		 */
		@RequestMapping(value ="/orderStatusReport/" ,params={"task=filter"}  , method =  {RequestMethod.GET}) 
	    public String doFilter (@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			OrderStatusReportData report = new OrderStatusReportData();
            report.setReportCriteria(new ReportCriteria(OrderStatusReportHandler.REPORT_ID));
            report.setDetails(new Vector());
            request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		       String forward=super.doFilter( form, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
	    
	    	 @RequestMapping(value ="/orderStatusReport/" ,params={"task=page"}  , method =  {RequestMethod.GET}) 
	    	    public String doPage (@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	    	    throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			OrderStatusReportData report = new OrderStatusReportData();
            report.setReportCriteria(new ReportCriteria(OrderStatusReportHandler.REPORT_ID));
            report.setDetails(new Vector());
            request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	    	
	/**
	 * @param form
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value = "/orderStatusReport/", params = { "task=sort" }, method = { RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			OrderStatusReportData report = new OrderStatusReportData();
            report.setReportCriteria(new ReportCriteria(OrderStatusReportHandler.REPORT_ID));
            report.setDetails(new Vector());
            request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/**
	 * @param form
	 * @param bindingResult
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value = "/orderStatusReport/", params = { "task=download" }, method = { RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("alertsReportForm") AlertsReportForm form,BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			OrderStatusReportData report = new OrderStatusReportData();
            report.setReportCriteria(new ReportCriteria(OrderStatusReportHandler.REPORT_ID));
            report.setDetails(new Vector());
            request.setAttribute(Constants.REPORT_BEAN, report);
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	    	
	protected String getFileName(HttpServletRequest request) {
		// defaults to .csv extension
		OrderStatusReportData orderStatusReportData = new OrderStatusReportData(); 
		return getReportName()+" "+orderStatusReportData.getCurrentDate()+ CSV_EXTENSION;
	} 
	/**
	 * Method to download the data to CSV file
	 * 
	 * @param reportForm
	 * @param report
	 * @param request
	 * @throws SystemException
	 */
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		int total=0;
		AlertsReportForm form = (AlertsReportForm) reportForm;
		OrderStatusReportData orderStatusReportData = (OrderStatusReportData)report;
		StringBuffer buffer = new StringBuffer();
		buffer.append("\"").append(REPORT_TITLE).append(" ").append(orderStatusReportData.getCurrentDate()).append("\"").append(LINE_BREAK);
		buffer.append("Contract Number").append(COMMA).append(StringUtils.trimToEmpty(form.getContractNumber()))
		.append(LINE_BREAK);
		buffer.append("From Date").append(COMMA).append(form.getFromDate())
				.append(LINE_BREAK);
		buffer.append("To Date").append(COMMA).append(form.getToDate())
				.append(LINE_BREAK);
		buffer.append(LINE_BREAK);
		buffer.append("Status type").append(COMMA).append("No.of orders")
		.append(LINE_BREAK);
		for(OrderStatusReportVO vo : orderStatusReportData.getOrderStatusReportVOList()){
			buffer.append(vo.getOrderStatusType()).append(COMMA).append(vo.getTotalOrderStatusCount()).append(LINE_BREAK);
			if(vo.getTotalOrderStatusCount()!=0){
				total= total+ vo.getTotalOrderStatusCount();
			}
		}
		buffer.append("Total").append(COMMA).append(total).append(LINE_BREAK);
		form.setTask(null);
		return buffer.toString().getBytes();
	}

	/**
	 * Method to populate the report criteria
	 * 
	 * @param criteria
	 * @param actionForm
	 * @param request
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm actionForm, HttpServletRequest request)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		AlertsReportForm alertForm = (AlertsReportForm) actionForm;

		// Add filters to the report criteria
		if (alertForm != null) {

			String contractNumber = alertForm.getContractNumber();
			if (contractNumber != null && !BLANK.equals(contractNumber.trim())) {

				try {
					criteria.addFilter(
							OrderStatusReportData.FILTER_CONTRACT_NUMBER,
							new Integer(contractNumber.trim()));
				} catch (NumberFormatException e) {
					List errors = new ArrayList();
					errors.add(new GenericException(
							ErrorCodes.TECHNICAL_DIFFICULTIES));
					setErrorsInRequest(request, errors);
					throw new SystemException(
							"Exception occured while parsing contract number.");
				}
			}

			try {

				Date fromDate = simpleDateFormat.parse(alertForm.getFromDate());
				criteria.addFilter(
						OrderStatusReportData.FILTER_FROM_DATE, fromDate);

				Date toDate = simpleDateFormat.parse(alertForm.getToDate());
				criteria.addFilter(OrderStatusReportData.FILTER_TO_DATE,
						toDate);

			} catch (ParseException e) {
				List errors = new ArrayList();
				errors.add(new GenericException(
						ErrorCodes.TECHNICAL_DIFFICULTIES));
				setErrorsInRequest(request, errors);
				throw new SystemException(
						"Exception occured while calculating Dates");
			}
		}

		String task = getTask(request);

		// If it is a download task then set the required filter variable
		if (DOWNLOAD_TASK.equals(task)) {
			criteria.addFilter(PlanDocumentHistoryReportData.FILTER_TASK,
					PlanDocumentHistoryReportData.TASK_DOWNLOAD);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> populateReportCriteria");
		}
	}

	/**
	 * Gets the current task for this request.
	 * 
	 * @param request
	 *            The current request object.
	 * @return The task for this request.
	 */
	protected String getTask(HttpServletRequest request) {
		String task = null;
		AlertsReportForm alertForm = (AlertsReportForm) request
				.getSession().getAttribute("alertsReportForm");

		if (alertForm != null && alertForm.getTask() != null) {
			task = alertForm.getTask();
		} else {
			task = DEFAULT_TASK;
		}
		return task;
	}
	
	/**
	 * Get the Report Id
	 */
	@Override
	protected String getReportId() {
		return OrderStatusReportHandler.REPORT_ID;
	}

	/**
	 * Get the Report Name
	 */
	@Override
	protected String getReportName() {
		return OrderStatusReportData.REPORT_NAME;
	}

	/**
	 * Get the default sort order
	 */
	@Override
	protected String getDefaultSort() {
		return DEFAULT_SORT_FIELD;
	}

	/**
	 * Get the default sort direction
	 */
	@Override
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}
	 
	/**
	 * Method to create the pie chart properties
	 */
    private PieChartBean createDefaultPieChartBean() {
        PieChartBean pieChart = new PieChartBean();
        pieChart.setAppletArchive(PIE_CHART_APPLET_ARCHIVE);
        pieChart.setPresentationModel(PieChartBean.PRESENTATION_MODEL_SERVLET);
        pieChart.setBorderColor(COLOR_BORDER);
        pieChart.setShowWedgeLabels(true);
        pieChart.setUsePercentsAsWedgeLabels(true);
        pieChart.setPieStyle(PieChartBean.PIE_STYLE_FLAT);
        pieChart.setBorderWidth((float) 1.5);
        pieChart.setWedgeLabelOffset(75);
        pieChart.setFontSize(9);
        pieChart.setFontBold(true);
        pieChart.setDrawBorders(true);
        pieChart.setWedgeLabelExtrusion(35);
        pieChart.setWedgeLabelExtrusionColor("#000000");
        return pieChart;
    }
    
    /**
	 * Method to get the pie chart filled with the values obtained from reportData
	 * @param reportData
	 * @return pieChart
	 */
	 private PieChartBean getOrderStatusFrequencyPieChartBean(OrderStatusReportData reportData) {
	        PieChartBean pieChart = createDefaultPieChartBean();

	        if (logger.isDebugEnabled()) {
	            logger.debug("entry -> getAlertFrequencyPieChartBean");
	        }

	        List<OrderStatusReportVO> freqList = reportData.getOrderStatusReportVOList();
	        boolean isPieChart = false;

	        if (freqList.size() > 0) {

	            String[] wedgesArr = { WEDGE1, WEDGE2, WEDGE3, WEDGE4, WEDGE5, WEDGE6 };
	            int wedgeCount = 0;

	            for (OrderStatusReportVO vo : freqList) {
                    if(vo!=null){
	                if (vo.getTotalOrderStatusCount() > 0 ) {
	                	reportData.setSuppressStatusByPercentageGraphTitle(true);
	                    pieChart.addPieWedge( wedgesArr[wedgeCount++],vo.getTotalOrderStatusCount() , getWedgeColor(vo.getOrderStatusType().trim())
	                    		 ,SINGLE_SPACE_SYMBOL, VIEWING_PREFERENCE, WEDGE_FONT_COLOR, NUMBER_0);
	                    isPieChart = true;
	                    suppressLegendOrderStatusItem(reportData,vo.getOrderStatusType().trim());
	                }
                    }
	            }

	            if (isPieChart) {

	                pieChart.setAppletWidth(115);
	                pieChart.setAppletHeight(130);

	                pieChart.setPieWidth(90);
	                pieChart.setPieHeight(90);
	            }
	        }

	        if (logger.isDebugEnabled()) {
	            logger.debug("exit <- getAlertFrequencyPieChartBean");
	        }

	        return (isPieChart ? pieChart : null);

	    }
	 
	     /**
	      * To Get the Value of the Corresponding Status of the Order Information whether of type 
	      * Initiated,Error,InProgress like status which helps in Suppressing the Corresponding legend 
	      * whose value is equal to zero
	      * 
	      * @param reportData
	      * @param frequnecyName
	      */
                     	 
	 private void suppressLegendOrderStatusItem(OrderStatusReportData reportData,String frequnecyName){
		 
		 if (OrderStatusReportData.INITIATED_STATUS.equalsIgnoreCase(frequnecyName)) {
			  reportData.setSuppressInitiatedLegend(true);
	        } else if (OrderStatusReportData.ERROR_INVALID_REQUEST_STATUS.equalsIgnoreCase(frequnecyName)) {
	           reportData.setSuppressErrorInvalidlegend(true);
	        } else if (OrderStatusReportData.IN_PROGRESS_STATUS.equalsIgnoreCase(frequnecyName)) {
	            reportData.setSuppressInProgressLegend(true);
	        } else if (OrderStatusReportData.COMPLETED_STATUS.equalsIgnoreCase(frequnecyName)) {
	            reportData.setSuppressCompleteLegend(true);
	        } else if (OrderStatusReportData.NOT_COMPLETED_STATUS.equalsIgnoreCase(frequnecyName)) {
	           reportData.setSuppressNotCompelteLegend(true);
	        }else if (OrderStatusReportData.CANCELLED_STATUS.equalsIgnoreCase(frequnecyName)) {
	            reportData.setSuppressCancelledLegend(true);
	        }

	        
	    }
	 
	

 	/**
	 * Method to get the Wedge color for the pie chart
	 * @param frequencyName
	 * @return String
	 */
	 private String getWedgeColor(String frequnecyName) {

	        if (OrderStatusReportData.INITIATED_STATUS.equalsIgnoreCase(frequnecyName)) {
	            return ORDER_STATUS_INITIATED_COLOR_WEDGE_LABEL;
	        } else if (OrderStatusReportData.ERROR_INVALID_REQUEST_STATUS.equalsIgnoreCase(frequnecyName)) {
	            return ORDER_STATUS_ERROR_IN_FILE_COLOR_WEDGE_LABEL;
	        } else if (OrderStatusReportData.IN_PROGRESS_STATUS.equalsIgnoreCase(frequnecyName)) {
	            return ORDER_STATUS_INPROGRESS_COLOR_WEDGE_LABEL;
	        } else if (OrderStatusReportData.COMPLETED_STATUS.equalsIgnoreCase(frequnecyName)) {
	            return ORDER_STATUS_COMPLETED_COLOR_WEDGE_LABEL;
	        } else if (OrderStatusReportData.NOT_COMPLETED_STATUS.equalsIgnoreCase(frequnecyName)) {
	            return ORDER_STATUS_NOTCOMPLETED_COLOR_WEDGE_LABEL;
	        }else if (OrderStatusReportData.CANCELLED_STATUS.equalsIgnoreCase(frequnecyName)) {
	            return ORDER_STATUS_CANCELLED_COLOR_WEDGE_LABEL;
	        }

	        return null;
	    }
	 
	  /**
	   * This code has been changed and added to Validate form and request against
	   * penetration attack, prior to other validations.
	   */
	
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;
	@Autowired
	private OrderStatusReportValidator orderStatusReportValidator;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
		binder.addValidators(orderStatusReportValidator);
	}
	}
