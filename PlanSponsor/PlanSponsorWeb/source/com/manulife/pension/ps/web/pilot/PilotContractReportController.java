package com.manulife.pension.ps.web.pilot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.ReportServiceDelegate;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.ps.service.report.profiles.reporthandler.BusinessTeamCodeLookup;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.pilot.report.valueobject.PilotContractReportData;
import com.manulife.pension.service.contract.pilot.report.valueobject.PilotContractReportDetails;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

/**
 * Pilot contract report action
 * 
 * @author Steven
 */
@Controller
@RequestMapping(value ="/pilot")
@SessionAttributes({ "pilotContractReportForm" })

public class PilotContractReportController extends ReportController {
	@ModelAttribute("pilotContractReportForm")
	public PilotContractReportForm populateForm() {
		return new PilotContractReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("input", "/pilot/pilotContractReport.jsp");
		forwards.put("default", "/pilot/pilotContractReport.jsp");
		forwards.put("save", "/pilot/pilotContractReport.jsp");
		forwards.put("sort", "/pilot/pilotContractReport.jsp");
		forwards.put("page", "/pilot/pilotContractReport.jsp");
		forwards.put("print", "/pilot/pilotContractReport.jsp");
		forwards.put("filter", "/pilot/pilotContractReport.jsp");
	}

	public static final int DEFAULT_PAGE_SIZE = 35;

	public static final String REPORT_TITLE = "John Hancock USA - Pilot contract";

	protected static final String DOWNLOAD_COLUMN_HEADING = "Teamcode,JH Rep, Contract number, Contract Name, Pilot project, TPA firm ID, TPA firm name, Number of client contacts";

	private static final String IWITHDRAWALS = "i:withdrawals";

	private static final String IWITHDRAWALS_CODE = "WD";

	private static final String PLAN_VESTING = "Plan/Vesting";

	private static final String PLAN_VESTING_CODE = "PV";

	/**
	 * Constructor.
	 */
	public PilotContractReportController() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param className
	 */
	public PilotContractReportController(Class className) {
		super(className);
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportId()
	 */
	protected String getReportId() {
		return PilotContractReportData.REPORT_ID;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportName()
	 */
	protected String getReportName() {
		return PilotContractReportData.REPORT_NAME;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return PilotContractReportData.SORT_FIELD_TEAM_CODE;
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * common task
	 */


	protected String doCommon(BaseReportForm actionForm, 
			HttpServletRequest request, HttpServletResponse response)
					throws SystemException {
		String forward = super.doCommon(actionForm, request, response);
		request.setAttribute("pilotNamesLookup", getPilotNameLookup());

		return forward;
	}

	@RequestMapping(value ="/pilotContractReport/", method = { RequestMethod.POST, RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("pilotContractReportForm") PilotContractReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			setReportData(request);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}else{
				return forwards.get("input");
			}
		}
		String forward = super.doDefault(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/pilotContractReport/", params = { "task=filter" }, method = {
			RequestMethod.POST})
	public String doFilter(@Valid @ModelAttribute("pilotContractReportForm") PilotContractReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			setReportData(request);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}else{
				return forwards.get("input");
			}

		}
		String forward = super.doFilter(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/pilotContractReport/", params = {"task=page" }, method = {
			 RequestMethod.GET })
	public String doPage(@Valid @ModelAttribute("pilotContractReportForm") PilotContractReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			setReportData(request);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doPage(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/pilotContractReport/", params = {  "task=sort" }, method = {
			 RequestMethod.GET })
	public String doSort(@Valid @ModelAttribute("pilotContractReportForm") PilotContractReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			setReportData(request);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}else{
				return forwards.get("input");
			}
		}
		String forward = super.doSort(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value ="/pilotContractReport/", params = {  "task=download" }, method = {
			 RequestMethod.GET })
	public String doDownload(@Valid @ModelAttribute("pilotContractReportForm") PilotContractReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			setReportData(request);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}else{
				return forwards.get("input");
			}
		}
		String forward = super.doDownload(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	@RequestMapping(value = "/pilotContractReport/", params = {  "task=downloadAll" }, method = {
			 RequestMethod.GET })
	public String doDownloadAll(@Valid @ModelAttribute("pilotContractReportForm") PilotContractReportForm form,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException, SystemException {

		if (bindingResult.hasErrors()) {
			setReportData(request);
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}else{
				return forwards.get("input");
			}
		}
		String forward = super.doDownloadAll(form, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}

	/**
	 * populate report action form
	 */
	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}
		super.populateReportForm(reportForm, request);
		PilotContractReportForm form = (PilotContractReportForm) reportForm;
		form.setTeamCodeList(buildTeamCodeList());

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportForm");
		}

	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#populateDownloadData(java.io.PrintWriter,
	 *      com.manulife.pension.ps.web.report.BaseReportForm,
	 *      com.manulife.pension.service.report.valueobject.ReportData,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		PilotContractReportForm form = (PilotContractReportForm) reportForm;

		// find the contract sort code

		StringBuffer buffer = new StringBuffer();

		// As of and total count
		buffer.append(REPORT_TITLE).append(LINE_BREAK);

		// filters used
		if (!StringUtils.isEmpty(form.getContractNumber())) {
			buffer.append("Contract number,").append(form.getContractNumber()).append(LINE_BREAK);
		}

		if (!StringUtils.isEmpty(form.getTeamCode())) {
			buffer.append("Team code").append(form.getTeamCode()).append(LINE_BREAK);
		}

		// heading and records
		buffer.append(LINE_BREAK);
		buffer.append(DOWNLOAD_COLUMN_HEADING);
		buffer.append(LINE_BREAK);

		Iterator iterator = report.getDetails().iterator();
		while (iterator.hasNext()) {
			buffer.append(LINE_BREAK);
			PilotContractReportDetails theItem = (PilotContractReportDetails) iterator.next();

			buffer.append(theItem.getTeamCode() == null ? "" : theItem.getTeamCode()).append(COMMA);
			buffer.append(escapeField(theItem.getTeamName())).append(COMMA);
			buffer.append(theItem.getContractNumber()).append(COMMA);
			buffer.append(escapeField(theItem.getContractName())).append(COMMA);
			String pilotValues = "";
			for (String pilotName : theItem.getPilotEnabledMap().keySet()) {
				pilotValues = pilotValues + pilotName + " = " + theItem.getPilotEnabledMap().get(pilotName).toString()
						+ ",";
			}
			if (pilotValues.lastIndexOf(",") == pilotValues.length() - 1)
				pilotValues = pilotValues.substring(0, pilotValues.length() - 1);
			buffer.append(escapeField(pilotValues)).append(COMMA);
			buffer.append(theItem.getTpaFirmId() == null ? "" : theItem.getTpaFirmId()).append(COMMA);
			buffer.append(escapeField(theItem.getTpaFirmName())).append(COMMA);
			buffer.append(theItem.getClientContactNumber()).append(COMMA);

		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria,
	 *      com.manulife.pension.ps.web.report.BaseReportForm,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
			HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		PilotContractReportForm pForm = (PilotContractReportForm) form;

		if (!StringUtils.isEmpty(pForm.getContractNumber())) {
			criteria.addFilter(PilotContractReportData.FILTER_CONTRACT_NUMBER, pForm.getContractNumber());
		}

		if (!StringUtils.isEmpty(pForm.getTeamCode())) {
			criteria.addFilter(PilotContractReportData.FILTER_TEAM_CODE, pForm.getTeamCode());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateReportCriteria");
		}
	}

	/**
	 * Mocked data for web side testing
	 * 
	 * @param pageNumber
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	private PilotContractReportData getMockedPilotContractReportData(int pageNumber, HttpServletRequest request)
			throws SystemException {
		PilotContractReportData data = new PilotContractReportData();
		// data.setDetails(buildDetails(pageNumber));
		data.setTotalCount(20);
		ReportCriteria criteria = new ReportCriteria(getReportId());
		criteria.setPageSize(DEFAULT_PAGE_SIZE);
		criteria.setPageNumber(pageNumber);
		data.setReportCriteria(criteria);

		return data;
	}

	// get pilot name view bean for dynamic column
	private PilotNameLookup getPilotNameLookup() {

		PilotNameLookup lookup = new PilotNameLookup();
		Collection<String> pilotNames = null;

		try {
			pilotNames = getContractServiceDelegate().getPilotList();
		} catch (SystemException se) {
			throw new RuntimeException(se.getCause());
		}

		for (String pilotName : pilotNames) {
			if (pilotName.equals(IWITHDRAWALS)) {
				lookup.getPilotNames().add(IWITHDRAWALS_CODE);
			}
			if (pilotName.equals(PLAN_VESTING)) {
				lookup.getPilotNames().add(PLAN_VESTING_CODE);
			}

		}
		return lookup;
	}

	// get polot service delegate
	private ContractServiceDelegate getContractServiceDelegate() {
		return ContractServiceDelegate.getInstance();

	}

	/**
	 * @see com.manulife.pension.ps.web.report.ReportController#getReportData(java.lang.String,
	 *      com.manulife.pension.service.report.valueobject.ReportCriteria,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected ReportData getReportData(String reportId, ReportCriteria reportCriteria, HttpServletRequest request)
			throws SystemException, ReportServiceException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		ReportServiceDelegate service = ReportServiceDelegate.getInstance();
		// reportCriteria.setPageSize(10);
		ReportData bean = service.getReportData(reportCriteria);
		// ReportData bean = getMockedSecurityRoleReportReportData(1, request);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getReportData");
		}

		return bean;

	}

	/**
	 * Checks whether we're in the right state.
	 * 
	 *      javax.servlet.http.HttpServletRequest)
	 */
	private void setReportData(HttpServletRequest request) {

			// request.setAttribute(DISPLAY_SHOW_ALL_KEY, "true");
			PilotContractReportData report = new PilotContractReportData();
			request.setAttribute(Constants.REPORT_BEAN, report);
			request.setAttribute("pilotNamesLookup", getPilotNameLookup());
	}

	/**
	 * Validate the input form. The search field must not be empty.
	 * 
	 * @see com.manulife.pension.ps.web.controller.PsController#doValidate(ActionMapping,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Autowired
	private PilotContractReportValidator pilotContractReportValidator;

	@Autowired
    private PSValidatorFWInput  psValidatorFWInput;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(pilotContractReportValidator);
	}

	 private Contract getContract(String contractNumber) throws ContractNotExistException, SystemException {
		Contract contract = null;
		contract = ContractServiceDelegate.getInstance().getContractDetails(Integer.valueOf(contractNumber).intValue(),
				6);
		if (contract != null && contract.getCompanyName() == null) {
			contract = null;
		}
		return contract;
	}

	/**
	 * Determine whether a given contract has a valid status for conversion.
	 * 
	 * @param contract
	 * @return true if the given contract has a valid status for conversion
	 */
	private boolean isValidContractStatus(Contract contract) {
		return (Contract.STATUS_PROPOSAL_SIGNED.equals(contract.getStatus())
				|| Contract.STATUS_DETAILS_COMPLETED.equals(contract.getStatus())
				|| Contract.STATUS_PENDING_CONTRACT_APPROVAL.equals(contract.getStatus())
				|| Contract.STATUS_CONTRACT_APPROVED.equals(contract.getStatus())
				|| Contract.STATUS_ACTIVE_CONTRACT.equals(contract.getStatus())
				|| Contract.STATUS_CONTRACT_FROZEN.equals(contract.getStatus()));
	}

	// dummy data for team code list
	private List<LabelValueBean> buildTeamCodeList() {
		List<LabelValueBean> codes = new ArrayList<LabelValueBean>();
		codes.add(new LabelValueBean("All", "All"));
		for (String code : getBusinessTeamCodes()) {
			codes.add(new LabelValueBean(code, code));
		}
		return codes;
	}

	private Collection<String> getBusinessTeamCodes() {
		Collection<String> codes = null;

		try {
			codes = BusinessTeamCodeLookup.getBusinessTeamCodes();
		} catch (SystemException se) {
			logger.error(se);
		}
		return codes;
	}

	// //escape ","
	private String escapeField(String field) {
		if (field == null) {
			return "";
		} else if (field.indexOf(",") != -1) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		} else {
			return field;
		}
	}
}