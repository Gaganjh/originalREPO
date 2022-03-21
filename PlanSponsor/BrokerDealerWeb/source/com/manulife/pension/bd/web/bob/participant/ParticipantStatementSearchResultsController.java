package com.manulife.pension.bd.web.bob.participant;

import static com.manulife.pension.bd.web.BDConstants.BOB_PAGE_FORWARD;

import java.io.IOException;
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

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.BobContextUtils;
import com.manulife.pension.bd.web.report.BDReportController;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.StatementServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.ControllerForward;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantStatementItem;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantStatementsReportData;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryReportData;
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
 * 
 * @author Radhakrishnan Munusamy
 * @see BDReportController for details
 */
@Controller
@RequestMapping(value ="/bob")
@SessionAttributes({"ParticipantStatementSearchForm"})

public final class ParticipantStatementSearchResultsController extends BDReportController {
	@ModelAttribute("ParticipantStatementSearchForm") 
	public ParticipantStatementSearchForm populateForm() 
	{
		return new ParticipantStatementSearchForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("bobPage","redirect:/do/bob/blockOfBusiness/Active/");
		forwards.put("input","/participant/participantStatementResults.jsp");
		forwards.put( "default","/participant/participantStatementResults.jsp");
		forwards.put( "sort","/participant/participantStatementResults.jsp");
		forwards.put( "filter","/participant/participantStatementResults.jsp ");
		forwards.put( "page","/participant/participantStatementResults.jsp");
		forwards.put( "print","/participant/participantStatementResults.jsp");
		forwards.put("secondaryWindowSystemError","/WEB-INF/global/secondaryWindowSystemError.jsp");
		forwards.put( "fetchStatements","/participant/participantStatementResults.jsp");
		forwards.put("noStatements","/participant/noParticipantStatementResults.jsp");
		}
	
	private static final String LAST_NAME = "last_name";
	
	private static final String FIRST_NAME = "first_name";
	
	private static final String SSN = "ssn";

    private static final RegularExpressionRule lastNameRErule = new RegularExpressionRule(
            BDErrorCodes.LAST_NAME_INVALID,
            BDRuleConstants.FIRST_NAME_LAST_NAME_RE);
    private static final RegularExpressionRule firstNameRErule = new RegularExpressionRule(
            BDErrorCodes.STMT_FIRST_NAME_INVALID,
            BDRuleConstants.FIRST_NAME_LAST_NAME_RE);
    private static final RegularExpressionRule ssnRErule = new RegularExpressionRule(
            BDErrorCodes.STMT_SSN_INVALID,
            BDRuleConstants.SSN_RE);
    
    private static final String FETCH_STATEMENT_TASK = "fetchStatements";
    
    private static final String GENERATED_PDF ="isReportGenerated";
    private static final String PDF_GENERATED = "pdfGenerated";
	private static final String PDF_NOT_GENERATED = "pdfNotGenerated";
	
	public static final String SECONDARY_WINDOW_SYSTEM_ERROR_FORWARD = "secondaryWindowSystemError";
	private static final String UNIQUE_ERROR_ID = "uniqueErrorId";
	private static final String ERROR_CODE = "errorCode";
    
	/**
	 * Constructor for ParticipantSummaryReportAction
	 */
	public ParticipantStatementSearchResultsController() {
		super(ParticipantStatementSearchResultsController.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#getDownloadData(com.manulife.pension.platform.web.report.BaseReportForm,
	 *      com.manulife.pension.service.report.valueobject.ReportData,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request) {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#getDefaultSortDirection()
	 */
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * @see BaseReportController#getDefaultSort()
	 */
	protected String getDefaultSort() {
		return ParticipantStatementsReportData.DEFAULT_SORT;
	}

	/**
	 * @see BaseReportController#getReportId()
	 */
	protected String getReportId() {
		return ParticipantStatementsReportData.REPORT_ID;
	}

	/**
	 * @return String cvs report file name
	 */
	protected String getReportName() {
		return ParticipantStatementsReportData.REPORT_NAME;
	}

	/**
	 * @see BaseReportController#createReportCriteria(String, HttpServletRequest)
	 */
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportCriteria");
		}

		// default sort criteria
		// this is already set in the super

		Contract currentContract = getBobContext(request).getCurrentContract();

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
	
	
	
	
	@RequestMapping(value ="/participant/participantStatementResults/", params={"task=fetchStatements"} , method =  {RequestMethod.GET}) 
	public String doFetchStatements(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		String forwardPreExecute= preExecute(actionForm, request, response);
       if (StringUtils.isNotBlank(forwardPreExecute)) {
     	  return StringUtils.contains(forwardPreExecute, '/') ? forwardPreExecute : forwards.get(forwardPreExecute);
        }
		if(bindingResult.hasErrors()){
			populateReportForm( actionForm, request);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
			request.setAttribute(BDConstants.REPORT_BEAN, reportData);
			request.setAttribute("statementsList", new ArrayList<ParticipantStatementDocumentVO>());
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
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
		.getAttribute(BDConstants.REPORT_BEAN);

        List<ParticipantStatementItem> participantSummaryDetails = (List<ParticipantStatementItem>)report.getDetails();
        
        ParticipantStatementItem participantDetails = getParticipantDetails(profileId, participantSummaryDetails);
        
        Contract currentContract = getBobContext(request).getCurrentContract();
        BDUserProfile userProfile = (BDUserProfile) getBobContext(request).getUserProfile();
        long userId = getBobContext(request).getUserProfile().getBDPrincipal().getProfileId();
        Integer contractNo = currentContract.getContractNumber();
        
        List<ParticipantStatementDocumentVO> documents = new ArrayList<ParticipantStatementDocumentVO>();
        
        
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
				documents = participantStatementListVo.getDocument();
				serviceSessionId = participantStatementListVo.getSessionId();
			}
			session.setAttribute("serviceSessionId", serviceSessionId);
			
			actionForm.setSelectedFirstName(participantDetails.getFirstName());
			actionForm.setSelectedLastName(participantDetails.getLastName());
        }
        request.setAttribute("statementsList", documents);
        
        if(!userProfile.isInternalUser()){
        	logPageVisits(String.valueOf(userId),contractNo.toString());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doFetchStatements");
        }
        if(documents.size() > 0){
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
		//record.setApplicationId(BDConstants.BD_APPLICATION_ID);
		record.setData("FRW");
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
	 * This method handles fetching of the individual statements from the service and open them in PDF format 
	 * as attachment in the defined filename
	 * 
	 * @param mapping
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return forward
	 * @throws SystemException
	 * @throws IOException
	 */
    public byte[] doFetchStatement( BaseReportForm reportForm, HttpServletRequest request) throws
            SystemException, IOException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doFetchStatement");
        }

        String serviceSessionId = "";
        HttpSession session =request.getSession(false);
        serviceSessionId = (String) session.getAttribute("serviceSessionId");
        
        String documentId = request.getParameter("documentId");
        
        String profileId = request.getParameter("profileId");
        
                
        ParticipantStatementSearchForm form = (ParticipantStatementSearchForm) reportForm;

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
		participantStatementReqVo.setDocumentId(participantStatement.getDocumentId());
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
	
    /*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.bd.web.report.BDReportAction#doCommon(org.apache.struts.action.ActionMapping,
	 *      com.manulife.pension.platform.web.report.BaseReportForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	
			protected String doCommon(
					ParticipantStatementSearchForm actionForm, HttpServletRequest request,
					HttpServletResponse response) throws 
					SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon");
		}
		

		Contract currentContract = getBobContext(request).getCurrentContract();
		int contractNumber = currentContract.getContractNumber(); 
		ContractStatementInfoVO contractStatementInfo = ContractServiceDelegate.getInstance().getContractStatementInfo(contractNumber);
		
		Date stmtEndDate = contractStatementInfo.getLastStatementEndDate();
		Date stmtStartDate = null;
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(stmtEndDate); 
		c.add(Calendar.MONTH, -24);
		c.add(Calendar.DATE, -1);
		stmtStartDate = c.getTime();
		
		// To Apply filter for quarter past 24 months
		actionForm.setStmtGenStartDate(stmtStartDate);
		actionForm.setStmtGenEndDate(stmtEndDate);
		
		String forward = super.doCommon(actionForm, request,
				response);

		
		
		getBobContext(request).setPptProfileId(null);
		
		ParticipantStatementsReportData report = (ParticipantStatementsReportData) request
				.getAttribute(BDConstants.REPORT_BEAN);

		actionForm.setParticipantDetails((List<ParticipantStatementItem>)report.getDetails());
		
        if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCommon");
		}
		return forward;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#populateReportForm(org.apache.struts.action.ActionMapping,
	 *      com.manulife.pension.platform.web.report.BaseReportForm,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {

		super.populateReportForm( reportForm, request);

		String task = getTask(request);
		if (FILTER_TASK.equals(task)) {
			reportForm.setSortField(getDefaultSort());
			reportForm.setSortDirection(getDefaultSortDirection());
		}
	}

	/**
	 * Validate the input form. The search field must not be empty.
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.controller.BaseController#doValidate(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.Form,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Autowired
	private ParticipantStatementSearchValidator participantStatementSearchValidator;
	@Autowired
	   private BDValidatorFWInput  bdValidatorFWInput;
	@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWInput);
	    binder.addValidators(participantStatementSearchValidator);
	}

	/**
	 * @see BaseController#execute(ActionMapping, ActionForm, HttpServletRequest,
	 *      HttpServletResponse)
	 */
	 @RequestMapping(value ="/participant/participantStatementResults/",method =  {RequestMethod.POST}) 
		public String execute(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		 String forwardPreExecute;
		 forwardPreExecute= preExecute(actionForm, request, response);
	       if (StringUtils.isNotBlank(forwardPreExecute)) {
	     	  return  forwards.get(forwardPreExecute);
	        }
	        ControllerForward forward = new ControllerForward("refresh",
					"/do" + new UrlPathHelper().getPathWithinServletMapping(request) + "?task=" + getTask(request), true);
			return "redirect:" + forward.getPath();

    }  
	 @RequestMapping(value ="/participant/participantStatementResults/",method =  {RequestMethod.GET}) 
		public String doDefault(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		 String forwardPreExecute= preExecute(actionForm, request, response);
         if (StringUtils.isNotBlank(forwardPreExecute)) {
      	  return StringUtils.contains(forwardPreExecute, '/') ? forwardPreExecute : forwards.get(forwardPreExecute);
         }
		 if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 populateReportForm( actionForm, request);
					ParticipantSummaryReportData reportData = new ParticipantSummaryReportData(
							null, 0);
					request.setAttribute(BDConstants.REPORT_BEAN,reportData);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			 }
		 }
	        String forward=super.doDefault(actionForm, request, response);
	   	 return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 


 }  	
	 @RequestMapping(value ="/participant/participantStatementResults/", params="{task=filter}",method =  {RequestMethod.GET}) 
		public String doFilter(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		 String forwardPreExecute= preExecute(actionForm, request, response);
        if (StringUtils.isNotBlank(forwardPreExecute)) {
      	  return StringUtils.contains(forwardPreExecute, '/') ? forwardPreExecute : forwards.get(forwardPreExecute);
         }
		 if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 populateReportForm( actionForm, request);
					ParticipantSummaryReportData reportData = new ParticipantSummaryReportData(
							null, 0);
					request.setAttribute(BDConstants.REPORT_BEAN,reportData);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			 }
		 }
	        String forward=super.doFilter(actionForm, request, response);
	   	 return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 


}
	 @RequestMapping(value ="/participant/participantStatementResults/", params="{task=sort}",method =  {RequestMethod.GET}) 
		public String doSort(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		 String forwardPreExecute= preExecute(actionForm, request, response);
        if (StringUtils.isNotBlank(forwardPreExecute)) {
      	  return StringUtils.contains(forwardPreExecute, '/') ? forwardPreExecute : forwards.get(forwardPreExecute);
         }
		 if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 populateReportForm( actionForm, request);
					ParticipantSummaryReportData reportData = new ParticipantSummaryReportData(
							null, 0);
					request.setAttribute(BDConstants.REPORT_BEAN,reportData);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			 }
		 }
	        String forward=super.doSort(actionForm, request, response);
	   	 return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 


}  	
	 @RequestMapping(value ="/participant/participantStatementResults/", params="{task=print}",method =  {RequestMethod.GET}) 
		public String doPrint(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		 String forwardPreExecute= preExecute(actionForm, request, response);
        if (StringUtils.isNotBlank(forwardPreExecute)) {
      	  return StringUtils.contains(forwardPreExecute, '/') ? forwardPreExecute : forwards.get(forwardPreExecute);
         }
		 if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 populateReportForm( actionForm, request);
					ParticipantSummaryReportData reportData = new ParticipantSummaryReportData(
							null, 0);
					request.setAttribute(BDConstants.REPORT_BEAN,reportData);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			 }
		 }
	        String forward=super.doPrint(actionForm, request, response);
	   	 return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 


}  	
	 @RequestMapping(value ="/participant/participantStatementResults/", params="{task=page}",method =  {RequestMethod.GET}) 
		public String doPage(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
		 String forwardPreExecute= preExecute(actionForm, request, response);
        if (StringUtils.isNotBlank(forwardPreExecute)) {
      	  return StringUtils.contains(forwardPreExecute, '/') ? forwardPreExecute : forwards.get(forwardPreExecute);
         }
		 if(bindingResult.hasErrors()){
			 String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			 if(errDirect!=null){
				 request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				 populateReportForm( actionForm, request);
					ParticipantSummaryReportData reportData = new ParticipantSummaryReportData(
							null, 0);
					request.setAttribute(BDConstants.REPORT_BEAN,reportData);
				 return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			 }
		 }
	        String forward=super.doPage(actionForm, request, response);
	   	 return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 


}  	



	/**
	 * The preExecute method has been overriden to see if the contractNumber is
	 * coming as part of request parameter. If the contract Number is coming as
	 * part of request parameter, the BobContext will be setup with contract
	 * information of the contract number passed in the request parameter.
	 * 
	 */
	protected String preExecute( ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SystemException {

		super.preExecute( form, request, response);

		BobContextUtils.setUpBobContext(request);

		BobContext bob = BDSessionHelper.getBobContext(request);
		if (bob == null || bob.getCurrentContract() == null) {
			return forwards.get(BOB_PAGE_FORWARD);
		}

		if (bob.getCurrentContract().getCompanyCode().equals(
				GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY)) {
			ApplicationHelper.setRequestContentLocation(request,
					Location.NEW_YORK);
		}

        BobContextUtils.setupProfileId(request);
        
        return null;
    }
    
    /**
     * Sorting is done only based on the given sort field otherwise based on the Default Sort Direction.
     */
    protected void populateSortCriteria(ReportCriteria criteria, BaseReportForm form) {
        if (form.getSortField() != null) {
            criteria.insertSort(form.getSortField(), form.getSortDirection());
        }else{
            criteria.insertSort(getDefaultSort(), getDefaultSortDirection());
        }
    }
	
	/**
	 * This method will check whether Participant statement pdf got generated or not
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 * @throws IOException 
	 */
    @RequestMapping(value ="/participant/participantStatementResults/", params={"task=checkPdfReportGenerated"} , method = {RequestMethod.GET}) 
	public String doCheckPdfReportGenerated(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
    	String forwardPreExecute= preExecute(actionForm, request, response);
       if (StringUtils.isNotBlank(forwardPreExecute)) {
     	  return StringUtils.contains(forwardPreExecute, '/') ? forwardPreExecute : forwards.get(forwardPreExecute);
        }
    	if(bindingResult.hasErrors()){
			populateReportForm( actionForm, request);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
			request.setAttribute(BDConstants.REPORT_BEAN, reportData);
			request.setAttribute("statementsList", new ArrayList<ParticipantStatementDocumentVO>());
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		HttpSession session = request.getSession(false);
		
		byte[] generatedPDF = doFetchStatement(actionForm, request);

		if(generatedPDF == null){
			response.getWriter().write(PDF_NOT_GENERATED);
		}else{
			response.getWriter().write(PDF_GENERATED);
			session.setAttribute(GENERATED_PDF, new Handle(generatedPDF));
		}
		
		return null;
	}
	
	/**
	 * Method build validate the initial request and forward to BOB page, if the request is not valid
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
    @RequestMapping(value ="/participant/participantStatementResults/", params={"task=fetchStatementPdf"} , method =  {RequestMethod.GET}) 
   	public String doFetchStatementPdf(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException {
    	String forwardPreExecute= preExecute(actionForm, request, response);
       if (StringUtils.isNotBlank(forwardPreExecute)) {
     	  return StringUtils.contains(forwardPreExecute, '/') ? forwardPreExecute : forwards.get(forwardPreExecute);
        }
    	if(bindingResult.hasErrors()){
			populateReportForm( actionForm, request);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
			request.setAttribute(BDConstants.REPORT_BEAN, reportData);
			request.setAttribute("statementsList", new ArrayList<ParticipantStatementDocumentVO>());
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
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
			
			Contract currentContract = getBobContext(request).getCurrentContract();
	        int contractNo = currentContract.getContractNumber();
			String filename = contractNo+"_" + form.getSelectedFirstName() + "_" +form.getSelectedLastName();

			ParticipantStatementListVO participantStatementListVO = form.getParticipantStatementListVo();

			ParticipantStatementDocumentVO participantStatement = getParticipantStatement(documentId, participantStatementListVO.getDocument());
			String endDateStr = formatDate(participantStatement.getEndDate());
			BaseReportController.streamDownloadData(request, response,
					"application/pdf", filename + "_" + endDateStr + ".pdf", download);
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
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws SystemException
	 */
	@RequestMapping(value ="/participant/participantStatementResults/", params={"task=openErrorPdf"} , method =  {RequestMethod.GET}) 
   	public String doOpenErrorPdf(@Valid @ModelAttribute("ParticipantStatementSearchForm") ParticipantStatementSearchForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException {
		String forwardPreExecute= preExecute(actionForm, request, response);
       if (StringUtils.isNotBlank(forwardPreExecute)) {
     	  return StringUtils.contains(forwardPreExecute, '/') ? forwardPreExecute : forwards.get(forwardPreExecute);
        }
		if(bindingResult.hasErrors()){
			populateReportForm( actionForm, request);
			ParticipantStatementsReportData reportData = new ParticipantStatementsReportData(
					null, 0);
			request.setAttribute(BDConstants.REPORT_BEAN, reportData);
			request.setAttribute("statementsList", new ArrayList<ParticipantStatementDocumentVO>());
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
		HttpSession session =request.getSession(false);
		request.setAttribute(ERROR_CODE, "1099");
		if(!session.getAttribute(UNIQUE_ERROR_ID).toString().isEmpty()){
			request.setAttribute(UNIQUE_ERROR_ID,session.getAttribute(UNIQUE_ERROR_ID));
		}
		return forwards.get(SECONDARY_WINDOW_SYSTEM_ERROR_FORWARD);
		
	}
}
