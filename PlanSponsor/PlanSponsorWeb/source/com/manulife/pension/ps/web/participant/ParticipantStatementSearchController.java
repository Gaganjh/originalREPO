package com.manulife.pension.ps.web.participant;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.StatementServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantStatementItem;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantStatementsReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.FunctionalLogger;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractStatementInfoVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.statement.valueobject.ParticipantStatementDocumentVO;
import com.manulife.pension.service.statement.valueobject.ParticipantStatementListVO;
import com.manulife.pension.service.statement.valueobject.ParticipantStatementsEnum.BusinessUnit;
import com.manulife.pension.service.statement.valueobject.ParticipantStatementsEnum.Division;
import com.manulife.pension.service.statement.valueobject.ParticipantStatementsEnum.DocumentFormat;
import com.manulife.pension.service.statement.valueobject.ParticipantStatementsEnum.DocumentStatus;
import com.manulife.pension.service.statement.valueobject.ParticipantStatementsEnum.DocumentType;
import com.manulife.pension.service.statement.valueobject.ParticipantStatementsEnum.ProductSubType;
import com.manulife.pension.service.statement.valueobject.ParticipantStatementsEnum.ProductType;
import com.manulife.pension.service.statement.valueobject.ParticipantStatementsRequestVO;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.pension.util.log.ServiceLogRecord;

/**
 * This action handles the Participant Search and Fetch Statements List for the available participants from the TIBCO. It
 * also handles in displaying the individual statements in PDF format.
 *
 * @author Radhakrishnan Munusamy
 * @see ReportController for details
 */
@Controller
@RequestMapping(value ="/participant")
@SessionAttributes({"ParticipantStatementSearchForm"})

public final class ParticipantStatementSearchController extends ReportController {
	@ModelAttribute("ParticipantStatementSearchForm")
	public  ParticipantStatementSearchForm populateForm() 
	{
		return new  ParticipantStatementSearchForm();
		}	
	
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/participant/participantStatementSearch.jsp"); 
		forwards.put("default","/participant/participantStatementSearch.jsp");
		forwards.put("sort","/participant/participantStatementSearch.jsp");
		forwards.put("filter","/participant/participantStatementSearch.jsp");
		forwards.put("page","/participant/participantStatementSearch.jsp");
		forwards.put("print"," /participant/participantStatementSearch.jsp");
		forwards.put("secondaryWindowSystemErrorPage","/WEB-INF/global/secondaryWindowSystemError.jsp");
		forwards.put("fetchStatements","/participant/participantStatementResults.jsp");
		forwards.put("noStatements","/participant/noParticipantStatementResults.jsp");
	}


	private static final String FETCH_STATEMENT_TASK = "fetchStatements";
	
	private static final String GENERATED_PDF ="isReportGenerated";
    private static final String PDF_GENERATED = "pdfGenerated";
	private static final String PDF_NOT_GENERATED = "pdfNotGenerated";
	
	private static final String EMPTY_LAYOUT_ID = "/registration/authentication.jsp";
	public static final String SECONDARY_SYSTEM_ERROR_PAGE = "secondaryWindowSystemErrorPage";
	private static final String UNIQUE_ERROR_ID = "uniqueErrorId";
	private static final String ERROR_CODE = "errorCode";
	
	
	/**
	 * Constructor for ParticipantStatementSearchAction
	 */
	public ParticipantStatementSearchController() {
		super(ParticipantStatementSearchController.class);
	}

	/**
	 * @see ReportController#populateDownloadData(PrintWriter, BaseReportForm,
	 *      ReportData)
	 */
	protected byte[] getDownloadData(
			BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) {

		return null;
	}

	/**
	 * @see ReportController#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * @see ReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return ParticipantStatementsReportData.DEFAULT_SORT;
	}

	/**
	 * @see ReportController#getReportId()
	 */
	protected String getReportId() {
		return ParticipantStatementsReportData.REPORT_ID;
	}

	/**
	 * @see ReportController#getReportName()
	 */
	protected String getReportName() {
		return ParticipantStatementsReportData.REPORT_NAME;
	}

	/**
	 * @see ReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		// default sort criteria
		// this is already set in the super

		UserProfile userProfile = getUserProfile(request);
		Contract currentContract = userProfile.getCurrentContract();

		criteria.addFilter("contractNumber", Integer.toString(currentContract
				.getContractNumber()));

		ParticipantStatementSearchForm psform = (ParticipantStatementSearchForm) form;

		if (!StringUtils.isEmpty(psform.getNamePhrase())) {
			criteria.addFilter(ParticipantStatementsReportData.FILTER_LAST_NAME,
					psform.getNamePhrase());
		}
		
		if (!StringUtils.isEmpty(psform.getFirstName())) {
			criteria.addFilter(ParticipantStatementsReportData.FILTER_FIRST_NAME,
					psform.getFirstName());
		}

		if (psform.getStmtGenStartDate() != null) {
			criteria.addFilter(ParticipantStatementsReportData.FILTER_STMT_START_DATE,
					psform.getStmtGenStartDate());
		}
		
		if (psform.getStmtGenEndDate() != null) {
			criteria.addFilter(ParticipantStatementsReportData.FILTER_STMT_END_DATE,
					psform.getStmtGenEndDate());
		}
		
		if (!psform.getSsn().isEmpty()) {
			criteria.addFilter(ParticipantStatementsReportData.FILTER_SSN,
					psform.getSsn().toString());
		}
		
		String task = getTask(request);
		if (task.equals(FETCH_STATEMENT_TASK)) {
            criteria.setPageSize(ReportCriteria.NOLIMIT_PAGE_SIZE);
        }
		
		if (logger.isDebugEnabled()) {
			logger.debug("criteria= " + criteria);
			logger.debug("exit <- populateReportCriteria");
		}
	}
	
	/**
	 * Method to retrieve the List of Statements aplicable for the selected participant.
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return forward
	 * @throws SystemException
	 */
	
	@RequestMapping(value = {"/participantStatementSearch","/participantStatementResults"},params= {"task=fetchStatements"}, method =  {RequestMethod.GET}) 
	public String doFetchStatements(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			populateReportForm( actionForm, request);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
			request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
			request.setAttribute("statementsList", new ArrayList<ParticipantStatementDocumentVO>());
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get("fetchStatements");//if input forward not //available, provided default
	       }
		}
    
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doFetchStatements");
        }
        HttpSession session =request.getSession(false);
        String serviceSessionId = "";
        
        String profileId = request.getParameter("profileId");
        
      
        actionForm.setParticipantId(profileId);
        String forward = super.doCommon( actionForm, request, response);
        
        ParticipantStatementsReportData report = (ParticipantStatementsReportData) request
		.getAttribute(Constants.REPORT_BEAN);

        List<ParticipantStatementItem> participantSummaryDetails = (List<ParticipantStatementItem>)report.getDetails();
        
        ParticipantStatementItem participantDetails = getParticipantDetails(profileId, participantSummaryDetails);
        
        UserProfile userProfile = getUserProfile(request);
        long userId = userProfile.getPrincipal().getProfileId();
        Contract currentContract = userProfile.getCurrentContract();
        Integer contractNo = currentContract.getContractNumber();
        
        List<ParticipantStatementDocumentVO>  document = new ArrayList<ParticipantStatementDocumentVO>();
        if(participantDetails != null){
	        ParticipantStatementsRequestVO participantStatementsRequestVO = getParticipantStatementsRequestVO(participantDetails, contractNo);
	        
	        ParticipantStatementListVO participantStatementListVo = null;
	        
	        try {
				participantStatementListVo = StatementServiceDelegate.getInstance().getParticipantStatementsList(participantStatementsRequestVO);
				
			} catch (Exception e) {
				throw new SystemException(
						"Exception in doFetchStatements"+e);
			}
	        
	        actionForm.setParticipantStatementListVo(participantStatementListVo);
			
			if(participantStatementListVo != null){
				document = participantStatementListVo.getDocument();
				serviceSessionId = participantStatementListVo.getSessionId();
			}
			session.setAttribute("serviceSessionId", serviceSessionId);
			
			actionForm.setSelectedFirstName(participantDetails.getFirstName());
			actionForm.setSelectedLastName(participantDetails.getLastName());
        }
        request.setAttribute("statementsList", document);
        
        if(!userProfile.isInternalUser()){
        	logPageVisits(String.valueOf(userId),contractNo.toString());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doFetchStatements");
        }
        if(document.size() > 0){
        	return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
        }else{
        	return forwards.get("noStatements");
        }
    }
    
    /**
     * Method to Log the Statement Results and No Results Page visits
     * 
     * @param profileID
     * @param contractNumber
     */
    private void logPageVisits(String profileID, String contractNumber)
	{	
		// Log the execution time.
		ServiceLogRecord record = new ServiceLogRecord();
		Date date = new Date();
		
		// Default value is EZk
		//record.setApplicationId(Constants.PS_APPLICATION_ID);
		record.setData("PSW+TPA");
		record.setMethodName(this.getClass().getName()+":"+"doFetchStatements"); // Logging Point
		record.setUserIdentity(profileID);
		record.setServiceName("PAGE_VISITATION");
		record.setCode(1);		
		record.setMilliSeconds(date.getTime());

		// Log the record to MRL
		logger.error(record);
	}
    
    /**
	 * Method to get the ParticipantStatementItem for the provided profile id.
	 * 
	 * @param profileId
	 * @param participantDetails
	 * @return ParticipantStatementItem
	 */
	private ParticipantStatementItem getParticipantDetails(String profileId,
				List<ParticipantStatementItem> participantDetails) {
    	for (ParticipantStatementItem participantDetail : participantDetails) {
    		if (profileId.equals(participantDetail.getProfileId())){
    			return participantDetail;
    		}
    	}
		return null;
	}
    
    /**
     * populate ParticipantStatementsRequestVO for the fetching Statements Link using service call.
     * 
     * @param participantDetails
     * @param contractNo
     * @return
     * @throws SystemException 
     */
	private ParticipantStatementsRequestVO getParticipantStatementsRequestVO(
			ParticipantStatementItem participantDetails, Integer contractNo) throws SystemException {
		
		
		ContractStatementInfoVO contractStatementInfo = ContractServiceDelegate.getInstance().getContractStatementInfo(contractNo.intValue());
		
		Date stmtEndDate = contractStatementInfo.getLastStatementEndDate();
		Date stmtStartDate = null;
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(stmtEndDate); 
		c.add(Calendar.MONTH, -24);
		c.add(Calendar.DATE, -1);
		stmtStartDate = c.getTime();
		
		ParticipantStatementsRequestVO participantStatementReqVo = new ParticipantStatementsRequestVO();
		
		participantStatementReqVo.setProfileId(participantDetails.getProfileId());
		participantStatementReqVo.setDivision(Division.USA);
		participantStatementReqVo.setBusinessUnit(BusinessUnit.RPS);
		participantStatementReqVo.setProductType(ProductType.ANNUITIES);
		participantStatementReqVo.setProductSubType(ProductSubType.VLI);
		participantStatementReqVo.setSsn(participantDetails.getSsn());
		participantStatementReqVo.setContractId(contractNo.toString());
		participantStatementReqVo.setOrganisationIdentifier("1");
		participantStatementReqVo.setDocumentDateType("CREATION");
		participantStatementReqVo.setFromDate(stmtStartDate);
		participantStatementReqVo.setToDate(stmtEndDate);
		participantStatementReqVo.setDocumentFormat(DocumentFormat.PDF);
		participantStatementReqVo.setDocumentType(DocumentType.REGULAR);
		participantStatementReqVo.setDocumentStatus(DocumentStatus.ACTIVE);
		participantStatementReqVo.setRevisionIndex(new BigInteger("1"));
		participantStatementReqVo.setDocumentId("documentId");;
		
		return participantStatementReqVo;
	}

	/**
	 * This method handles fetching of the individual statements from the service 
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return forward
	 * @throws SystemException
	 * @throws IOException
	 */
    public byte[] doFetchStatement( ParticipantStatementSearchForm form, HttpServletRequest request) throws
            SystemException, IOException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doFetchStatement");
        }

        String serviceSessionId = "";
        HttpSession session =request.getSession(false);
        serviceSessionId = (String) session.getAttribute("serviceSessionId");
        
        String documentId = request.getParameter("documentId");
        
        String profileId = request.getParameter("profileId");
        
        FunctionalLogger.INSTANCE.log("Participant Statement", request, profileId + ":" + documentId, getClass(), getMethodName( form, request));
        
        ParticipantStatementListVO participantStatementListVO = form.getParticipantStatementListVo();
        
        ParticipantStatementDocumentVO participantStatement = getParticipantStatement(documentId, participantStatementListVO.getDocument());
        ParticipantStatementsRequestVO participantStatementsRequestVO = getParticipantStatementsRequestVO(profileId, participantStatement, serviceSessionId);

      	byte[] pdfContent = null; 
      	ParticipantStatementDocumentVO participantStatementDocument = null;
      	try {
			participantStatementDocument = StatementServiceDelegate.getInstance().getParticipantStatementDocument(participantStatementsRequestVO);
			
		} catch (Exception e) {
			SystemException systemException  = new SystemException(
					"Exception in doFetchStatements"+e);
			LogUtility.logSystemException("", systemException);
			session.setAttribute(UNIQUE_ERROR_ID, systemException.getUniqueId());
			return null;
		}
		 pdfContent = participantStatementDocument.getDocument();
		
		return pdfContent; 
    }
    
    /**
     * Method to format date to be used in the file name
     * 
     * @param date
     * @return
     */
    private String formatDate(Date date){
		DateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
		String formattedDate = "";
		if(date != null){
			formattedDate = df.format(date);
		}
		return formattedDate;
	}
    
    /**
     * method to populate the ParticipantStatementsRequestVO to make fetch statement service calls
     * 
     * @param profileId
     * @param participantStatement
     * @return
     */
    private ParticipantStatementsRequestVO getParticipantStatementsRequestVO( String profileId,
    		ParticipantStatementDocumentVO participantStatement, String serviceSessionId) {
    	
		ParticipantStatementsRequestVO participantStatementReqVo = new ParticipantStatementsRequestVO();
		
		participantStatementReqVo.setProfileId(profileId);
		participantStatementReqVo.setDivision(Division.USA);
		participantStatementReqVo.setBusinessUnit(BusinessUnit.RPS);
		//participantStatementReqVo.setProductType(ProductType.convertFromDbCode("Annuities"));
		//participantStatementReqVo.setProductSubType(ProductSubType.convertFromDbCode("Variable Life Insurance"));
		//participantStatementReqVo.setSsn(participantDetails.getSsn());
		//participantStatementReqVo.setContractId(contractNo.toString());
		//participantStatementReqVo.setOrganisationIdentifier("1");
		//participantStatementReqVo.setDocumentDateType("CREATION");
		//participantStatementReqVo.setFromDate(fromDate);
		//participantStatementReqVo.setToDate(toDate);
		participantStatementReqVo.setDocumentFormat(DocumentFormat.convertFromDbCode(participantStatement.getDocumentFormat()));
		participantStatementReqVo.setDocumentType(DocumentType.convertFromDbCode(participantStatement.getDocumentType()));
		participantStatementReqVo.setDocumentStatus(DocumentStatus.ACTIVE);
		participantStatementReqVo.setRevisionIndex(new BigInteger("1"));
		participantStatementReqVo.setDocumentId(participantStatement.getDocumentId());;
		participantStatementReqVo.setSessionId(serviceSessionId);
		return participantStatementReqVo;
	}

	/**
     * Private method to get the individual statement record ParticipantStatementDocumentVO from the List for the document id
     * 
     * @param documentId
     * @param documents
     * @return
     */
	private ParticipantStatementDocumentVO getParticipantStatement(String documentId,
			List<ParticipantStatementDocumentVO> documents) {
		for (ParticipantStatementDocumentVO document : documents) {
    		if(documentId.equals(document.getDocumentId())){
    			return document;
    		}
    	}
		return null;
	}

	/**
     * Invokes the default task (the initial page). It uses the common workflow
     * with validateForm set to true.
     * @throws IOException 
     *
     * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
     *      HttpServletResponse, boolean)
     */
	public String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		ParticipantStatementSearchForm form = (ParticipantStatementSearchForm) reportForm;
		UserProfile userProfile = getUserProfile(request);
		int contractNumber = userProfile.getCurrentContract().getContractNumber();
		
		ContractStatementInfoVO contractStatementInfo = ContractServiceDelegate.getInstance().getContractStatementInfo(contractNumber);
		Date stmtEndDate = contractStatementInfo.getLastStatementEndDate();
		Date stmtStartDate = null;
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(stmtEndDate); 
		c.add(Calendar.MONTH, -24);
		c.add(Calendar.DATE, -1);
		stmtStartDate = c.getTime();
		
		// To Apply filter for quarter past 24 months
		form.setStmtGenStartDate(stmtStartDate);
		form.setStmtGenEndDate(stmtEndDate);
		
		String forward = super.doCommon( reportForm, request,
				response);

		ParticipantStatementsReportData report = (ParticipantStatementsReportData) request
				.getAttribute(Constants.REPORT_BEAN);
		form.setParticipantDetails((List<ParticipantStatementItem>)report.getDetails());
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	/**
	 * Method to populate Report Action form
	 */
	protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {

		super.populateReportForm( reportForm, request);

		String task = getTask(request);
		if (task.equals(FILTER_TASK)) {
			reportForm.setSortField(getDefaultSort());
			reportForm.setSortDirection(getDefaultSortDirection());
		}
		ParticipantStatementSearchForm form = (ParticipantStatementSearchForm) reportForm;
		HttpSession session =request.getSession(false);
		populateFormAttributesToSession(form, session);	
		//Accounts Summary Tab navigation from other Tabs - End
	}
	
	/**
	 * Method to populate the Form attributes to Session
	 * 
	 * @param form
	 * @param session
	 */
	private void populateFormAttributesToSession(ParticipantStatementSearchForm form, 
			HttpSession session) {
		session.setAttribute(ParticipantStatementsReportData.FILTER_SSN, form
				.getSsn());
		session.setAttribute(ParticipantStatementsReportData.FILTER_LAST_NAME, form
				.getNamePhrase());
		session.setAttribute(ParticipantStatementsReportData.FILTER_FIRST_NAME, form
				.getFirstName());
	}


	/**
	 * @see PsController#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	@RequestMapping(value = {"/participantStatementSearch","/participantStatementResults"}, method =  {RequestMethod.POST}) 
	public String execute(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		       }
			populateReportForm( actionForm, request);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
			request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
			request.setAttribute("statementsList", new ArrayList<ParticipantStatementDocumentVO>());
	        
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       
		}
		
		
		ControllerForward forward = new ControllerForward("refresh",
				"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
		return "redirect:" + forward.getPath();
	}
	
	@RequestMapping(value = {"/participantStatementSearch","/participantStatementResults"}, method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			populateReportForm( actionForm, request);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
			request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
			request.setAttribute("statementsList", new ArrayList<ParticipantStatementDocumentVO>());
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		String forward = super.doDefault(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = "/participantStatementResults", method =  {RequestMethod.GET}) 
	public String doDefaultResults(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			populateReportForm( actionForm, request);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
			request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
			request.setAttribute("statementsList", new ArrayList<ParticipantStatementDocumentVO>());
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		String forward = super.doDefault(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = {"/participantStatementSearch","/participantStatementResults"},params= {"task=default"}, method =  {RequestMethod.POST}) 
	public String doDefaultTask(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			request.removeAttribute(CommonConstants.ERROR_RDRCT);
			populateReportForm( actionForm, request);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
			request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
			request.setAttribute("statementsList", new ArrayList<ParticipantStatementDocumentVO>());
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		
		String forward = super.doDefault(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	/**
	 * This method will check whether participant statement pdf got generated or not 
	 * and corresponding pdf generated or not generated  will be send back as ajax call response
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 * @throws IOException 
	 */
	@RequestMapping(value = {"/participantStatementSearch","/participantStatementResults"},params= {"task=checkPdfReportGenerated"}, method =  {RequestMethod.GET}) 
	public String doCheckPdfReportGenerated(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		       }
			populateReportForm( actionForm, request);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
			request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
			request.setAttribute("statementsList", new ArrayList<ParticipantStatementDocumentVO>());
	        
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       
		}
		
		
		HttpSession session = request.getSession(false);
		 ParticipantStatementSearchForm reportForm = (ParticipantStatementSearchForm) actionForm;
		byte[] generatedPDF = doFetchStatement(reportForm, request);

		if(generatedPDF == null){
			response.getWriter().write(PDF_NOT_GENERATED);
		}else{
			response.getWriter().write(PDF_GENERATED);
			session.setAttribute(GENERATED_PDF, new Handle(generatedPDF));
		}
		
		return null;
	}
	
	/**
	 * This method handles fetching of the individual statements from the service and open them in PDF format 
	 * as attachment in the defined filename
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
	@RequestMapping(value = {"/participantStatementSearch","/participantStatementResults"},params= {"task=fetchStatementPdf"}, method =  {RequestMethod.GET}) 
	public String doFetchStatementPdf(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		       }
			populateReportForm( actionForm, request);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
			request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
			request.setAttribute("statementsList", new ArrayList<ParticipantStatementDocumentVO>());
	        
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       
		}
		
		
		HttpSession session = request.getSession(false);
		ParticipantStatementSearchForm form = (ParticipantStatementSearchForm) actionForm;
		byte [] download = null;
		if(session.getAttribute(GENERATED_PDF) != null) {
			download = ((Handle)session.getAttribute(GENERATED_PDF)).getByteArray();
			session.setAttribute(GENERATED_PDF, null);
		}else {
			download =  doFetchStatement(form, request);
		}
		
		if (download != null && download.length > 0) {

			String documentId = request.getParameter("documentId");
			
			UserProfile userProfile = getUserProfile(request);
			Contract currentContract = userProfile.getCurrentContract();
			int contractNo = currentContract.getContractNumber();
			String filename = contractNo+"_" + form.getSelectedFirstName() + "_" +form.getSelectedLastName();

			ParticipantStatementListVO participantStatementListVO = form.getParticipantStatementListVo();

			ParticipantStatementDocumentVO participantStatement = getParticipantStatement(documentId, participantStatementListVO.getDocument());
			String endDateStr = formatDate(participantStatement.getEndDate());
			BaseReportController.streamDownloadData(request, response,
					"application/pdf", filename +  "_" + endDateStr + ".pdf", download);
		}	
		 
		return null;
	}
	/**
	 * Class to handle the byte array object in session scope
	 * 
	 *
	 */
	public static class Handle implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		Handle(byte[] array){
			this.byteArray = array;
		}
		
		private byte[] byteArray;

		/**
		 * @return the array
		 */
		public byte[] getByteArray() {
			return byteArray;
		}
	}
	
	/**
	 * Forward to system error page when the pdf is not get generated
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	@RequestMapping(value = {"/participantStatementSearch","/participantStatementResults"},params= {"task=openErrorPdf"}, method =  {RequestMethod.GET}) 
	public String doOpenErrorPdf(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		       }
			populateReportForm( actionForm, request);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
			request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
			request.setAttribute("statementsList", new ArrayList<ParticipantStatementDocumentVO>());
	        
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       
		}
		
		HttpSession session = request.getSession(false);
		request.setAttribute(ERROR_CODE, "1099");
		if(!session.getAttribute(UNIQUE_ERROR_ID).toString().isEmpty()){
			request.setAttribute(UNIQUE_ERROR_ID,session.getAttribute(UNIQUE_ERROR_ID));
		}
		request.setAttribute(Constants.LAYOUT_BEAN, LayoutBeanRepository
				.getInstance().getPageBean(EMPTY_LAYOUT_ID));
		return forwards.get(SECONDARY_SYSTEM_ERROR_PAGE);
		
	}
	@RequestMapping(value = {"/participantStatementSearch","/participantStatementResults"},params= {"task=sort"}, method =  {RequestMethod.GET}) 
	public String doSort(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		       }
			populateReportForm( actionForm, request);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
			request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
			request.setAttribute("statementsList", new ArrayList<ParticipantStatementDocumentVO>());
	        
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       
		}
		
		
		String forward = super.doSort(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = {"/participantStatementSearch","/participantStatementResults"},params= {"task=print"}, method =  {RequestMethod.GET}) 
	public String doPrint(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		       }
			populateReportForm( actionForm, request);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
			request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
			request.setAttribute("statementsList", new ArrayList<ParticipantStatementDocumentVO>());
	        
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       
		}
		
		
		String forward = super.doPrint(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = {"/participantStatementSearch","/participantStatementResults"},params= {"task=filter"}, method =  {RequestMethod.GET}) 
	public String doFilter(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		       }
			populateReportForm( actionForm, request);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
			request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
			request.setAttribute("statementsList", new ArrayList<ParticipantStatementDocumentVO>());
	        
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       
		}
		
		String forward = super.doFilter(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@RequestMapping(value = {"/participantStatementSearch","/participantStatementResults"},params= {"task=page"}, method =  {RequestMethod.GET}) 
	public String doPage(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		       }
			populateReportForm( actionForm, request);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
			request.setAttribute(CommonConstants.REPORT_BEAN, reportData);
			request.setAttribute("statementsList", new ArrayList<ParticipantStatementDocumentVO>());
	        
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       
		}
		
		String forward = super.doPage(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	@Autowired 
	private ParticipantStatementSearchValidator participantStatementSearchValidator;
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;
	
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	    binder.addValidators(participantStatementSearchValidator);
	}
	
	
}
