package com.manulife.pension.platform.web.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.fop.apps.FOPException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.LayoutPage;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.PdfConstants;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.controller.DynamicController;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.platform.web.util.PDFDocument;
import com.manulife.pension.platform.web.util.PDFGenerator;
import com.manulife.pension.platform.web.util.PdfHelper;
import com.manulife.pension.platform.web.util.ReportsXSLProperties;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.MessageProvider;

/**
 * Base Action class for all reporting sub classes.
 */
public abstract class BaseReportController extends DynamicController {
    public static final Logger logger = Logger.getLogger(BaseReportController.class);

    @SuppressWarnings("unchecked")
    public static final Class[] ACTION_METHOD_PARAM_TYPES = {
             BaseReportForm.class,
            HttpServletRequest.class, HttpServletResponse.class };


    public static final String CONTENT_DISPOSITION_TEXT = "Content-Disposition";

    public static final String ATTACHMENT_TEXT = "attachment; filename=";

    public static final String CSV_TEXT = "text/csv";

    public static final String CSV_EXTENSION = ".csv";

    public static final String COMMA = ",";

    public static final String WHITE_SPACE_CHAR = " ";

    public static final String QUOTE = "\"";

    public static final String LINE_BREAK = System
            .getProperty("line.separator");

    protected static final String TASK_KEY = "task";

    protected static final String FILTER_TASK = "filter";

    protected static final String SHOWALL_TASK = "showAll";

    protected static final String SORT_TASK = "sort";

    protected static final String PAGE_TASK = "page";

    protected static final String DOWNLOAD_TASK = "download";

    protected static final String DOWNLOAD_ALL_TASK = "downloadAll";

    protected static final String PRINT_TASK = "print";

    protected static final String DEFAULT_TASK = "default";

    protected static final String PRINT_PDF_TASK = "printPDF";

	protected static final String RESET_TASK = "reset";

    protected static final String ZERO_AMOUNT_STRING = "0.00";
    
    protected static final String DOUBLE_QUOTE = "\"";

    /**
     * @return The unique report ID.
     */
    protected abstract String getReportId();

    /**
     * @return The unique name for the report.
     */
    protected abstract String getReportName();

    /**
     * @return The default sort field for the criteria.
     */
    protected abstract String getDefaultSort();

    /**
     * @return The default sort direction for the criteria.
     */
    protected abstract String getDefaultSortDirection();

    /**
     * Overrides this method to write the report to a PrintWriter.
     *
     * @param reportForm
     *            The report form.
     * @param report
     *            The report data.
     * @param request
     *            The HTTP request.
     * @return byte array
     */
    protected abstract byte[] getDownloadData(BaseReportForm reportForm,
            ReportData report, HttpServletRequest request)
            throws SystemException;

    /**
     * Override this method to write the report to a PrintWriter.
     *
     * @param reportForm - The report Form.
     * @param report - The report data.
     * @param request - The HTTP request
     * @return - byte array.
     * @throws SystemException
     */
    protected byte[] getDownloadAllData(BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) throws SystemException {
        String downloadData = new String();
        return downloadData.getBytes();
    }

    /**
     * Overrides this method to populate a report criteria with the information
     * from the Action Form and the Request. This method is called every time
     * before getReportData.
     *
     * @param criteria
     *            The report criteria to populate
     * @param form
     *            The form that contains the user's submitted content.
     * @param request
     *            The HTTP request.
     */
    protected abstract void populateReportCriteria(ReportCriteria criteria,
            BaseReportForm form, HttpServletRequest request) throws SystemException;

    /**
     * Helper method to add Filter Criteria entered by the User.
     *
     * @param criteria
     * @param criteriaName
     * @param criteriaValue
     */
    protected void addFilterCriteria(ReportCriteria criteria, String criteriaName,
            Object criteriaValue) {
        if (criteriaValue instanceof String && !StringUtils.isEmpty((String) criteriaValue)) {
            criteria.addFilter(criteriaName, criteriaValue);
        } else {
            if (criteriaValue != null) {
                criteria.addFilter(criteriaName, criteriaValue);
            }
        }
    }

    /**
     * Constructor.
     */
    public BaseReportController() {
        super(BaseReportController.class);
    }

    /**
     * Constructor.
     */
    @SuppressWarnings("unchecked")
    public BaseReportController(Class className) {
        super(className);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.manulife.pension.ps.web.controller.DynamicAction#getActionMethodParameters()
     */
    @SuppressWarnings("unchecked")
    final protected Class[] getActionMethodParameters() {
        return ACTION_METHOD_PARAM_TYPES;
    }

    /**
     * This method returns the appropriate action methods. It turns the first
     * letter of the task name to capital letter and prefixes with "do". The
     * following methods are used for the reports:
     * <ul>
     * <li>doDefault()
     * <li>doFilter()
     * <li>doPage()
     * <li>doSort()
     * <li>doDownload()
     * <li>doPrint()
     * </ul>
     */
    final protected String getMethodName(
            ActionForm actionForm, HttpServletRequest request) {

        String task = getTask(request);
        String methodName = "do" + task.substring(0, 1).toUpperCase()
                + task.substring(1);
        return methodName;
    }

    /**
     * Gets the current task for this request.
     *
     * @param request
     *            The current request object.
     * @return The task for this request.
     */
    protected String getTask(HttpServletRequest request) {
        String task = request.getParameter(TASK_KEY);
        if (task == null) {
            task = DEFAULT_TASK;
        }
        return task;
    }

    /**
     * Invokes the default task (the initial page). It uses the common workflow
     * with validateForm set to true.
     *
     * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
     *      HttpServletResponse, boolean)
     */
    public String doDefault(
            BaseReportForm reportForm, HttpServletRequest request,
            HttpServletResponse response) throws
            SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDefault");
        }

        // reset the form in the session if any
        // this will ensure that the user always sees
        // the default view of the report
        reportForm = resetForm(reportForm, request);

        String forward = doCommon(reportForm, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doDefault");
        }

        return forward;
    }

    /**
     * Invokes the filter task (e.g. limiting date range). It uses the common
     * workflow with validateForm set to true.
     *
     * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
     *      HttpServletResponse, boolean)
     */
    public String doFilter(
            BaseReportForm reportForm, HttpServletRequest request,
            HttpServletResponse response) throws
            SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doFilter");
        }

        String forward = doCommon(reportForm, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doFilter");
        }

        return forward;
    }

    /**
     * Invokes the page task (next/previous page). It uses the common workflow
     * with validateForm set to true.
     *
     * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
     *      HttpServletResponse, boolean)
     */
    public String doPage(
            BaseReportForm reportForm, HttpServletRequest request,
            HttpServletResponse response) throws
            SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doPage");
        }

        String forward = doCommon(reportForm, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doPage");
        }

        return forward;
    }

    /**
     * Invokes the sort task. It uses the common workflow with validateForm set
     * to true.
     *
     * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
     *      HttpServletResponse, boolean)
     */
    public String doSort(
            BaseReportForm reportForm, HttpServletRequest request,
            HttpServletResponse response) throws
            SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doSort");
        }

        String forward = doCommon(reportForm, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doSort");
        }

        return forward;
    }

    /**
     * Invokes the download task. The first half of this task uses the common
     * workflow with validateForm set to true. The second half of this task
     * takes the populated report data object and create the CSV file.
     *
     * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
     *      HttpServletResponse, boolean)
     * @see #populateDownloadData(PrintWriter, BaseReportForm, ReportData,
     *      HttpServletRequest)
     * @return null so that Struts will not try to forward to another page.
     */
    @SuppressWarnings("unchecked")
    public String doDownload(
            BaseReportForm reportForm, HttpServletRequest request,
            HttpServletResponse response) throws
            SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDownload");
        }
        byte[] downloadData = null;

        doCommon(reportForm, request, response);

        // check for errors
        // if any just stream an error message, otherwise deliver the report
        Collection errors = (Collection) request.getAttribute(CommonEnvironment.getInstance().getErrorKey());
        if (errors != null && errors.size() > 0) {
            downloadData = getErrorText(errors).getBytes();
            if (logger.isDebugEnabled()) {
                logger.debug("found errors: " + getErrorText(errors));
            }
        } else {

            if (logger.isDebugEnabled()) {
                logger.debug("Did not find any errors -- streaming the report data");
            }

            ReportData report = (ReportData) request
                    .getAttribute(CommonConstants.REPORT_BEAN);
            downloadData = getDownloadData(reportForm, report, request);
        }

        streamDownloadData(request, response, getContentType(),
                getFileName(reportForm,request), downloadData);

        /**
         * No need to forward to any other JSP or action. Returns null will make
         * Struts to return controls back to server immediately.
         */
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doDownload");
        }
        return null;
    }

    /**
     * Invokes the downloadAll task. The downloadAll Task is called when the user wants to get a CSV
     * report of all the report data present in all tabs.
     *
     * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest, HttpServletResponse,
     *      boolean)
     * @see #populateDownloadData(PrintWriter, BaseReportForm, ReportData, HttpServletRequest)
     * @return null so that Struts will not try to forward to another page.
     */
    @SuppressWarnings("unchecked")
    public String doDownloadAll(BaseReportForm reportForm,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDownload");
        }
        byte[] downloadData = null;

        doCommon(reportForm, request, response);

        // check for errors
        // if any just stream an error message, otherwise deliver the report
        Collection errors = (Collection) request.getAttribute(CommonEnvironment.getInstance().getErrorKey());
        if (errors != null && errors.size() > 0) {
            downloadData = getErrorText(errors).getBytes();
            if (logger.isDebugEnabled()) {
                logger.debug("found errors: " + getErrorText(errors));
            }
        } else {

            if (logger.isDebugEnabled()) {
                logger.debug("Did not find any errors -- streaming the report data");
            }

            ReportData report = (ReportData) request.getAttribute(CommonConstants.REPORT_BEAN);
            downloadData = getDownloadAllData(reportForm, report, request);
        }

        streamDownloadData(request, response, getContentType(), getFileNameForCSVAll(reportForm,
                request),
                downloadData);

        /**
         * No need to forward to any other JSP or action. Returns null will make Struts to return
         * controls back to server immediately.
         */
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doDownload");
        }
        return null;
    }

    /**
     * Stream the download data to the Response. This method is singled out and
     * made public static so that non-ReportActions can use it. This method
     * closes the Response's OutputStream when it returns.
     * @throws SystemException
     */
    public static void streamDownloadData(HttpServletRequest request,
            HttpServletResponse response, String contentType, String fileName,
            byte[] downloadData) throws SystemException {

        response.setHeader("Cache-Control", "max-age=0,must-revalidate");
        response.setContentType(contentType);

        // The Content-Disposition header was removed to avoid poping up
        // multiple open dialog boxes in IE 5.5. In that case, the filename
        // is derived (by the browser) to be the last part of the URL path
        // with the string after the last dot as the file extension. e.g.
        // http://www.aaa.org/transactionHistory/?download&ext=.csv
        // will generate a filename "transactionHistory.csv". Please refer
        // to standardreporttoolssection.jsp for the construction of the
        // URL.

        String userAgent = request.getHeader("User-Agent");
        if (logger.isDebugEnabled()) {
            logger.debug("user agent: " + userAgent);
        }

        if (userAgent == null || userAgent.indexOf("MSIE 5") < 0) {
            response.setHeader(CONTENT_DISPOSITION_TEXT, ATTACHMENT_TEXT
                    + fileName);
        }

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache");
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
     * Invokes the print task. It uses the common workflow with validateForm set
     * to false.
     *
     * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
     *      HttpServletResponse, boolean)
     */
    public String doPrint(
            BaseReportForm reportForm, HttpServletRequest request,
            HttpServletResponse response) throws
            SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doPrint");
        }

        String forward = doCommon(reportForm, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doPrint");
        }

        return forward;
    }


    /**
     * The common method used by all other doXXX() methods. This method
     * basically performs the following tasks:
     * <ol>
     * <li>Populates a report action form to its default state.
     * (populateReportForm)</li>
     * <li>If validateForm is true, invoke the validate() method on the action
     * form.</li>
     * <li>Populates a report criteria based on the action form.
     * (getReportCriteria and populateReportCriteria)</li>
     * <li>Obtains report data from the delegate (getReportData)</li>
     * <li>Stores the report data object into the request</li>
     * </ol>
     * <p>
     * All of the reporting framework tasks have a very similar workflow and
     * they all reuse this method. Please refer to the individual Javadoc
     * comments for any variation.
     *
     * @param mapping
     *            The Struts Action Mapping object.
     * @param reportForm
     *            The current report form.
     * @param request
     *            The current request object.
     * @param response
     *            The current response object.
     * @return The ActionForward appropriate for this task. If validateForm is
     *         true and the validation fails, the ActionMapping's input page is
     *         returned.
     * @throws IOException
     * @throws ServletException
     */
    @SuppressWarnings("unchecked")
    protected String doCommon(
            BaseReportForm reportForm, HttpServletRequest request,
            HttpServletResponse response) throws
            SystemException {

        String reportId = getReportId();

        /*
         * If the report form is stored in the session and its report ID is
         * different from this Action's report ID, we clear the form's content.
         */
        /*
         * removed -- see resetForm() call in doDefault
         *
         * if (mapping.getScope().equals("session") &&
         * !reportId.equals(reportForm.getReportId())) { reportForm.clear();
         * reportForm.setReportId(reportId); }
         */

        String task = getTask(request);

        populateReportForm(reportForm, request);

        ReportCriteria criteria = getReportCriteria(reportId, reportForm,
                request);

        try {
            ReportData report = getReportData(reportId, criteria, request);
            request.setAttribute(CommonConstants.REPORT_BEAN, report);
        } catch (ReportServiceException e) {
            logger.error("Received a Report service exception: ", e);
            List errors = new ArrayList();
            errors.add(new GenericException(Integer.parseInt(
                    e.getErrorCode())));
            setErrorsInRequest(request, errors);
            //TODO
            //return mapping.getInputForward();
            return "input";
        }
        //return findForward(mapping, task);
        return task;
    }

    /**
     * Returns the report data using the given criteria.
     *
     * @param reportId
     *            The report ID to use.
     * @param reportCriteria
     *            The report criteria to use. This criteria is populated by the
     *            #getReportCriteria(String, ReportCriteria, BaseReportForm,
     *            HttpServletRequest) method.
     * @param request
     *            The current request object.
     * @return A populated ReportData object.
     * @throws SystemException
     *             if the business delegate throws an exception.
     */
    protected ReportData getReportData(String reportId,
            ReportCriteria reportCriteria, HttpServletRequest request)
            throws SystemException, ReportServiceException {
		StopWatch stopWatch = new StopWatch();
		try {

			stopWatch.start();
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}
        ReportServiceDelegate service = ReportServiceDelegate.getInstance();
        ReportData bean = service.getReportData(reportCriteria);

		try {

			stopWatch.stop();
			if (logger.isInfoEnabled()) {
				logger.info("Report Id [" + reportId + "] criteria ["
						+ reportCriteria + "] timing: " + stopWatch.toString());
			}
		} catch (IllegalStateException e) {
			final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
					+ Thread.currentThread().toString();
			logger.error(message);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getReportData");
		}
        
        return bean;
    }

    /**
     * Returns a report criteria object based on the given form and request. It
     * invokes #populateReportCriteria(ReportCriteria, BaseReportForm,
     * HttpServletRequest) of the derived class to populate action-specific
     * criteria.
     *
     * @param reportId
     *            The report ID.
     * @param form
     *            The form used to populate the criteria.
     * @param request
     *            The current request object.
     * @return A new ReportCriteria object pre-populated with information from
     *         the BaseReportForm.
     */
    protected ReportCriteria getReportCriteria(String reportId,
            BaseReportForm form, HttpServletRequest request) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getReportCriteria");
        }

        ReportCriteria criteria = new ReportCriteria(reportId);
        populateSortCriteria(criteria, form);
        criteria.setPageNumber(form.getPageNumber());

        String task = getTask(request);

        /*
         * Set other attributes of the criteria.
         */
        if (task.equals(PRINT_TASK)) {
            criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);
        } else if (task.equals(DOWNLOAD_TASK)) {
            criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);
        } else if (task.equals(PRINT_PDF_TASK)) {
            criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);
        } else if (task.equals(DOWNLOAD_ALL_TASK)) {
            criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);
        } else {
            criteria.setPageSize(getPageSize(request));
        }

        /*
         * Let derived class populate other attributes of the criteria.
         */
        populateReportCriteria(criteria, form, request);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getReportCriteria");
        }

        return criteria;
    }

    /**
     * Populate sort criteria in the criteria object using the given FORM. The
     * default implementation inserts the FORM's sort field and sort direction.
     * Override this if more sorting is required.
     *
     * @param criteria
     *            The criteria to populate
     * @param form
     *            The Form to populate from.
     */
    protected void populateSortCriteria(ReportCriteria criteria,
            BaseReportForm form) {
        if (form.getSortField() != null) {
            criteria.insertSort(form.getSortField(), form.getSortDirection());
            if (!form.getSortField().equals(getDefaultSort())) {
                criteria.insertSort(getDefaultSort(), getDefaultSortDirection());
            }
        }
    }

    /**
     * Populates an empty report form with default parameters. Any parameter
     * that is not empty (null or empty string) should not be touched. Dervied
     * class should override this method to add in their own initialization
     * code. Default implementation populates the sort field and the sort
     * direction.
     *
     * @param mapping
     *            TODO
     * @param reportForm
     *            The report form to populate.
     * @param request
     *            The current request object.
     */
    protected void populateReportForm(
            BaseReportForm reportForm, HttpServletRequest request) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> populateReportForm");
        }

        String task = getTask(request);

        /*
         * Reset page number properly.
         */
        if (task.equals(DEFAULT_TASK) || task.equals(SORT_TASK)
                || task.equals(FILTER_TASK) || task.equals(PRINT_TASK)
                || task.equals(DOWNLOAD_TASK)
                || task.equals(PRINT_PDF_TASK) || task.equals(DOWNLOAD_ALL_TASK)) {
            reportForm.setPageNumber(1);
        }

        /*
         * Set default sort if we're in default task.
         */
        if (task.equals(DEFAULT_TASK) || reportForm.getSortDirection() == null
                || reportForm.getSortDirection().length() == 0) {
            reportForm.setSortDirection(getDefaultSortDirection());
        }

        /*
         * Set default sort direction if we're in default task.
         */
        if (task.equals(DEFAULT_TASK) || reportForm.getSortField() == null
                || reportForm.getSortField().length() == 0) {
            reportForm.setSortField(getDefaultSort());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- populateReportForm");
        }
    }

    /**
     * Returns the download CSV content MIME type. Derived class can override
     * this method to return a different MIME type.
     *
     * @return The CSV content MIME type.
     */
    protected String getContentType() {
        // defaults to "text/csv"
        return CSV_TEXT;
    }

    /**
     * Given a report ID, returns the downloaded CSV file name.
     * This method is overriden in specific action classes when the CSV filename includes some value from the
     * Form like 'transactionDate' etc.. otherwise by default it will return the report name itself as CSV file name.
     *
     * @param request
     * @return The file name used for the downloaded CSV.
     */

    protected String getFileName(BaseReportForm form, HttpServletRequest request) {
          return getFileName(request);
    }

    /**
     * This method will be called to get the CSV File name, when the user clicks the CSV All Button.
     *
     * @param form
     * @param request
     * @return - String representing the the CSV File Name
     */
    protected String getFileNameForCSVAll(BaseReportForm form, HttpServletRequest request) {
        return getFileName(request);
    }

    /**
     * Given a report ID, returns the downloaded CSV file name.
     *
     * @param request
     *            Allows children to provide request specific info (like contract number).
     * @return The file name used for the downloaded CSV.
     */
    protected String getFileName(HttpServletRequest request) {
        // defaults to .csv extension
        return getReportName() + CSV_EXTENSION;
    }

    /**
     * Returns the number of report items per page. By default, it returns into
     * the Environment's default page size.
     * @param request TODO
     *
     * @see com.manulife.pension.ps.web.util.Environment#getDefaultPageSize()
     * @return The number of report items per page.
     */
    protected int getPageSize(HttpServletRequest request) {
        // overwrite it with default from environment variable
        return new Integer(getApplicationFacade(request).getEnvironment().getDefaultPageSize())
                .intValue();
    }

    protected BaseReportForm resetForm(
            BaseReportForm reportForm, HttpServletRequest request)
            throws SystemException {
        try {
            BaseReportForm blankForm = (BaseReportForm) reportForm
                    .getClass().newInstance();
            PropertyUtils.copyProperties(reportForm, blankForm);
           // reportForm.reset(mapping, request);
        } catch (Exception e) {
            throw new SystemException(e, this.getClass().getName(),
                    "resetForm", "exception in resetting the form");
        }

        return reportForm;
    }

    /**
     * Returns a non-null String from the given object.
     *
     * @param s
     *            The object to retrieve string for.
     * @return The string representation of the object (by invoking the
     *         toString() method on the object) or an empty string if the object
     *         is null.
     */
    public static String getCsvString(Object s) {

        if (s == null) {
            return "";
        } else {
            return "\"" + s.toString() + "\"";
        }
    }

    /**
     * writes a simple list of errors
     */
    @SuppressWarnings("unchecked")
    public static String getErrorText(Collection errorCollection) throws SystemException {
        try {
            StringBuffer errorText = new StringBuffer();
            String[] errors = MessageProvider.getInstance().getMessages( errorCollection );

            for (int i=0; i < errors.length; i++ ) {
                errorText.append( errors[i] );
            }
            String errorDesc =  errorText.toString();
	        //To replace &nbsp; with blank in error message.
	        errorDesc=errorDesc.replaceAll(CommonConstants.BLANK_STR, CommonConstants.BLANK);
            return errorDesc.toString();
        } catch (ContentException e) {
            throw new SystemException (e,
                "ReportAction", "getErrorText", "error getting error text");
        }
    }

    /**
     * Invokes the save task. It uses the common workflow with validateForm set to true.
     *
     * @param mapping
     * @param reportForm
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws SystemException
     */
    public String doSave(
            BaseReportForm reportForm, HttpServletRequest request,
            HttpServletResponse response) throws
            SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doSave");
        }
        logger.debug("exit <- doSave");
        String forward = doCommon(reportForm, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doSave");
        }

        return forward;
    }

    /**
     * Utility method to validate the contract  number
     */
    public boolean doContractNumberValidator(String filterContractNumber ){
        boolean isValidContract = false;
        if ( filterContractNumber != null && filterContractNumber.trim().length() > 0 ) {
            int contractLength = filterContractNumber.trim().length();
            if (contractLength < CommonConstants.CONTRACT_NUMBER_MIN_LENGTH || contractLength > CommonConstants.CONTRACT_NUMBER_MAX_LENGTH) {
                isValidContract =  true;
            } else {
                try {
                    int cn = Integer.parseInt(filterContractNumber);
                    if (cn < CommonConstants.CONTRACT_NUMBER_MIN_VALUE || cn > CommonConstants.CONTRACT_NUMBER_MAX_VALUE) {  // check for negative 4 digit numbers, and leading zeros scenario eg) 00100
                        isValidContract =  true;
                    }
                } catch (Exception e) {
                    isValidContract =  true;
                }
            }
        }
        return isValidContract;
    }

    /**
     * This checks if any informational messages already present in the session and adds the current
     * message to the existing messages.
     *
     * @param request
     * @param messages
     * @param reqAttributeName
     */
    @SuppressWarnings("unchecked")
    public static void setMessagesInRequest(final HttpServletRequest request,
            final Collection messages, final String reqAttributeName) {
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

    /**
     * This method checks to see if the # of rows in the Report would be greater than the PDF cap
     * limit. If yes, the "pdfCapped" is set to true in the form. This boolean value will be later
     * used in JSP to open a pop-up whenever the user clicks on PDF button to open "PDF-version" of
     * the Report.
     */
    protected void populateCappingCriteria(ReportData report, BaseReportForm reportForm,
            HttpServletRequest request) {

        reportForm.setPdfCapped(false);

        if (getNumberOfRowsInReport(report) > getMaxCappedRowsInPDF()) {
            reportForm.setCappedRowsInPDF(getMaxCappedRowsInPDF());
            reportForm.setPdfCapped(true);
        } else {
            reportForm.setCappedRowsInPDF(getNumberOfRowsInReport(report));
        }

    }

    /**
     * This method will return the # of rows that is present in the current Report.
     * The Integer returned will be used to see if we need to cap the # of rows or not.
     * @param report
     * @return - The number of rows in the current Report.
     */
    public Integer getNumberOfRowsInReport(ReportData report) {
        int noOfRows = 0;
        if (report != null) {
            noOfRows = report.getTotalCount();
        }
        return noOfRows;
    }

    /**
     * This method will be called when the user clicks on the PDF icon. This method fetches
     * the data from the database and places the information into an XML file.
     * Using Apache-FOP, the XML file, XSLT file is converted into a PDF file.
     * The PDF file is sent back to the user.
     * @param mapping
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
    public String doPrintPDF(BaseReportForm reportForm,
            HttpServletRequest request, HttpServletResponse response) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("Inside doPrintPDF");
        }
        doCommon(reportForm, request, response);
        ReportData report = (ReportData) request.getAttribute(CommonConstants.REPORT_BEAN);

        ByteArrayOutputStream pdfOutStream = prepareXMLandGeneratePDF(reportForm, report, request);

        response.setHeader("Cache-Control", "must-revalidate");
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
     * This method will generate the PDF and return a ByteArrayOutputStream which will be sent back to
     * the user.
     * This method would:
     * - Create the XML-String from VO.
     * - Create the PDF using the created XML-String and XSLT file.
     * @throws ContentException
     */
    protected ByteArrayOutputStream prepareXMLandGeneratePDF(BaseReportForm reportForm,
            ReportData report, HttpServletRequest request) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("Inside prepareXMLandGeneratePDF");
        }

        ByteArrayOutputStream pdfOutStream = new ByteArrayOutputStream();
        try {

            Object xmlTree = prepareXMLFromReport(reportForm, report, request);
            String xsltFileName = getXSLTFileName();
            String configFileName = getFOPConfigFileName();
            if (xmlTree == null || xsltFileName == null) {
                return pdfOutStream;
            }
            String xsltfile = ReportsXSLProperties.get(xsltFileName);
            String configfile = ReportsXSLProperties.get(configFileName);
            String includedXSLPath = ReportsXSLProperties.get(CommonConstants.INCLUDED_XSL_FILES_PATH);
            if (xmlTree instanceof Document) {
                pdfOutStream = PDFGenerator.getInstance().generatePDFFromDOM((Document)xmlTree, xsltfile, configfile, includedXSLPath);
            }

        } catch (Exception exception) {
            String message = null;
            if (exception instanceof ContentException) {
                message = "Error occured while retrieveing CMA Content during PDF creation.";
            } else if (exception instanceof ParserConfigurationException) {
                message = "Error occured while creating Document object during PDF creation.";
            } else if (exception instanceof FOPException || exception instanceof TransformerException ||
                    exception instanceof IOException) {
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
     * @param reportForm
     * @param report
     * @param request
     * @return Object
     * @throws ParserConfigurationException
     */
    public Object prepareXMLFromReport(BaseReportForm reportForm, ReportData report,
            HttpServletRequest request) throws SystemException, ParserConfigurationException {
        return null;
    }

    /**
     * This method needs to be overridden by any Report that needs PDF Generation functionality.
     * This method would return the key present in ReportsXSL.properties file.
     * This key has the value as path to XSLT file, which will be used during PDF generation.
     *
     * @return String
     */
    public String getXSLTFileName() {
        return null;
    }

    /**
     * This method would return the key present in ReportsXSL.properties file.
     * This key has the value as path to FOP Configuration file.
     *
     * @return String
     */
    protected String getFOPConfigFileName() {
        return CommonConstants.FOP_CONFIG_FILE_KEY_NAME;
    }

    /**
     * This method returns the # of rows where the PDF will be capped.
     * @return Integer
     */
    protected Integer getMaxCappedRowsInPDF() {
        return Integer.valueOf(ReportsXSLProperties.get(CommonConstants.MAX_CAPPED_ROWS_IN_PDF));
    }

    /**
     * This method is used by PDF Generation functionality.
     *
     * This sets footer, footnotes and disclaimer XML elements common for reports
     *
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param params
     */
    protected void setFooterXMLElements(LayoutPage layoutPageBean,
            PDFDocument doc, Element rootElement, Location location, String[]... params) {
        PdfHelper.setFooterXMLElements(layoutPageBean, doc, rootElement, location,
                CommonContentConstants.GLOBAL_DISCLOSURE, params);
    }

    /**
     * This method is used by PDF Generation functionality.
     *
     * This sets Logo, PathName, Introduction-1 & 2 XML elements common for reports.
     *
     * @param layoutPageBean
     * @param doc
     * @param request
     */
    protected void setIntroXMLElements(LayoutPage layoutPageBean,
            PDFDocument doc, Element rootElement, Contract contract) {
        PdfHelper.setIntroXMLElements(layoutPageBean, doc, rootElement, contract);
    }

    /**
     * This method is used by PDF Generation functionality.
     *
     * This sets the Logo, Path Name XML elements.
     *
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setLogoAndPageName(LayoutPage layoutPageBean, PDFDocument doc,
            Element rootElement) {
        PdfHelper.setLogoAndPageName(layoutPageBean, doc, rootElement);
    }

    /**
     * This method is used by PDF Generation functionality.
     *
     * This sets the Introduction-1, Introduction-2 XML elements.
     *
     * @param layoutPageBean
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setIntro1Intro2XMLElements(LayoutPage layoutPageBean, PDFDocument doc,
            Element rootElement) {
        PdfHelper.setIntro1Intro2XMLElements(layoutPageBean, doc, rootElement);
    }

    /**
     * This method is used by PDF Generation functionality.
     *
     * This sets XML elements of Informational messages common for reports.
     *
     * @param doc
     * @param rootElement
     * @param request
     */
    @SuppressWarnings("unchecked")
    protected void setInfoMessagesXMLElements(PDFDocument doc, Element rootElement,
            Collection infoMessageCollection) {
        PdfHelper.setInfoMessagesXMLElements(doc, rootElement, infoMessageCollection);
    }

    /**
     * This method is used by PDF Generation functionality.
     *
     * This sets XML elements of Informational messages common for reports.
     *
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setInfoMessagesXMLElementsForReqAttr(PDFDocument doc, Element rootElement,
            HttpServletRequest request, String reqAttribute) {
        PdfHelper.setInfoMessagesXMLElementsForReqAttr(doc, rootElement, request, reqAttribute);
    }

    /**
     * This method is used by PDF Generation functionality.
     *
     * This sets XML elements of Error messages common for reports.
     *
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setErrorMessagesXMLElementsForReqAttr(PDFDocument doc, Element rootElement,
            HttpServletRequest request, String reqAttribute) {
        PdfHelper.setErrorMessagesXMLElementsForReqAttr(doc, rootElement, request, reqAttribute);
    }

    /**
     * This method is used by PDF Generation functionality.
     *
     * This sets XML element for Roth message.
     *
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setRothMessageElement(PDFDocument doc, Element rootElement, Contract contract) {
        PdfHelper.setRothMessageElement(doc, rootElement, contract);
    }

    /**
     * This method is used by PDF Generation functionality.
     *
     * This sets XML element for Signature Plus Disclosure message.
     *
     * @param doc
     * @param rootElement
     * @param request
     */
    protected void setSigPlusDisclosureMessageElement(PDFDocument doc, Element rootElement, String sigPlusText) {
        PdfHelper.setSigPlusDisclosureMessageElement(doc, rootElement, sigPlusText);
    }

    /**
     * This sets Global Disclosure XML element common for reports
     *
     * @param doc
     * @param rootElement
     */
    public static void setGlobalDisclosureXMLElement(PDFDocument pdfDoc, Element rootElement,
            Location location) {
        PdfHelper.setGlobalDisclosureXMLElement(pdfDoc, rootElement, location,
                CommonContentConstants.GLOBAL_DISCLOSURE);
    }

    /**
     * This method returns PDF capped text
     *
     * @return String
     */
    protected String getPDFCappedText() {
        return PdfConstants.PDF_CAPPED_MESSAGE1 + getMaxCappedRowsInPDF()
                + PdfConstants.PDF_CAPPED_MESSAGE2;
    }

    /**
     * This removes parentheses if present and prefix with minus sign instead
     * Example  input:(123.45678)  returns:-123.34567
     *
     * @param value
     * @return String
     */
    protected String removeParanthesesAndPrefixMinus(String value) {
        return PdfHelper.removeParanthesesAndPrefixMinus(value);
    }

}