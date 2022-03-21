package com.manulife.pension.bd.web.estatement;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.estatement.sort.RiaStatementsPageColumn;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWNull;
import com.manulife.pension.delegate.StatementServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.ria.valueobject.RiaStatementDocument;
import com.manulife.pension.service.ria.valueobject.RiaStatementsEnum;
import com.manulife.pension.service.ria.valueobject.RiaStatementsRequestData;
import com.manulife.pension.service.ria.valueobject.RiaStatementsResponseData;
import com.manulife.pension.validator.ValidationError;
import com.manulife.util.render.RenderConstants;

/**
 * The action for RIA eStatements
 * 
 * @author raoprer
 * 
 */
@Controller
@RequestMapping(value ={"/estatement","/ViewRIAStatements"})

public class EstatementReportController extends BaseReportController {
	@ModelAttribute("estatementReportForm") 
	public EstatementReportForm populateForm() 
	{
		return new EstatementReportForm();
		}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static{
		forwards.put("internal","/estatement/eStatement.jsp");
		forwards.put("external","/estatement/eStatementExternal.jsp");
		forwards.put("fail","/home/public_home.jsp");
		}

	private FastDateFormat ymdDateFormat = FastDateFormat.getInstance(RenderConstants.MEDIUM_YMD_DASHED);
	
	private FastDateFormat mdyDateFormat = FastDateFormat.getInstance(RenderConstants.MEDIUM_MDY_DASHED);
	
	protected static String DEFAULT_SORT = "statementDate";
	protected static String DEFAULT_SORT_DIRECTION = "desc";
	
	public EstatementReportController() {
		super(EstatementReportController.class);
	} 

	@RequestMapping(value ={"/fetch",""},  method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doDefault(@Valid @ModelAttribute("estatementReportForm") EstatementReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String uponError = null;
			BDUserProfile profile = BDSessionHelper.getUserProfile(request);
			if (profile == null) {
				uponError =  CommonConstants.FAIL;
			} else if (profile.isInternalUser()) {
				uponError = CommonConstants.INTERNAL ;
			} else {
				uponError = CommonConstants.EXTERNAL;
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(uponError);//if input forward not //available, provided default
	       }
		}

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDefault");
        }

        // reset the form in the session if any
        // this will ensure that the user always sees
        // the default view of the report
        // reportForm = resetForm(mapping, reportForm, request);

        String forward = doCommon( actionForm, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doDefault");
        }

        return forward;
    }
	 
	/**
	 * This method will get the required information.
	 * 
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	protected String doCommon(
			BaseReportForm reportForm, HttpServletRequest request,
			HttpServletResponse response) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCommon() in EstatementReportAction");
		}

		EstatementReportForm estatementReportForm = (EstatementReportForm) reportForm;

		populateReportForm( reportForm, request);

		BDUserProfile profile = BDSessionHelper.getUserProfile(request);

		String forward = null;

		if (profile.isInternalUser()) {
			forward = doCommonForInternalUser( reportForm, request,
					response);
		} else {
			forward = doCommonForExternalUser( reportForm, request,
					response);
		}


		List<RiaStatementVO> riaStatementListVO = estatementReportForm
				.getRiaStatementListVO();
		
		ReportData report = new EstatementReportData(riaStatementListVO.size(), getPageSize(request), estatementReportForm.getPageNumber());
        request.setAttribute(CommonConstants.REPORT_BEAN, report);

		//sorting
		Collections.sort(riaStatementListVO, RiaStatementsPageColumn
				.getRiaStatementsPageColumn(reportForm.getSortField())
				.getComparatorInstance(reportForm.getSortDirection()));
		
		//paging
		estatementReportForm.setRiaStatementListVO(paging(riaStatementListVO,estatementReportForm, request));
		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doCommon() in EstatementReportAction.");
		}

		return forward;

	}
	
	private List<RiaStatementVO> paging(
			List<RiaStatementVO> items, EstatementReportForm estatementReportForm,
			HttpServletRequest request) {
		List<RiaStatementVO> pageDetails = new ArrayList<RiaStatementVO>();
		
		if (items != null) {
			
			int pageNumber = estatementReportForm.getPageNumber();
			int pageSize = getPageSize(request);
			
			int startIndex = (pageNumber - 1) * pageSize + 1;
			
			if (pageSize == ReportCriteria.NOLIMIT_PAGE_SIZE) {
				pageDetails.addAll(items);
			} else {
				for (int i = startIndex - 1; i < pageNumber * pageSize
						&& i < items.size(); i++) {
					pageDetails.add(items.get(i));
				}
			}
		}
		
		return pageDetails;
	}
	
	/**
	 * 
	 */
	protected int getPageSize(HttpServletRequest request) {
    	return 12;
    }
	
	/**
	 * This method will used to handle the Sort data for BOB Pages.
	 * 
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ={"/fetch",""},params= {"task=sort"},  method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doSort(@Valid @ModelAttribute("estatementReportForm") EstatementReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String uponError = null;
			BDUserProfile profile = BDSessionHelper.getUserProfile(request);
			if (profile == null) {
				uponError =  CommonConstants.FAIL;
			} else if (profile.isInternalUser()) {
				uponError = CommonConstants.INTERNAL ;
			} else {
				uponError = CommonConstants.EXTERNAL;
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(uponError);//if input forward not //available, provided default
	       }
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSort() in EstatementReportAction");
		}

		String forward = super.doSort( actionForm, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doSort() in EstatementReportAction.");
		}

		return forward;
	}
	@RequestMapping(value ={"/fetch",""},params= {"task=page"},  method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doPage(@Valid @ModelAttribute("estatementReportForm") EstatementReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String uponError = null;
			BDUserProfile profile = BDSessionHelper.getUserProfile(request);
			if (profile == null) {
				uponError =  CommonConstants.FAIL;
			} else if (profile.isInternalUser()) {
				uponError = CommonConstants.INTERNAL ;
			} else {
				uponError = CommonConstants.EXTERNAL;
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(uponError);//if input forward not //available, provided default
	       }
		}

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doSort() in EstatementReportAction");
		}

		String forward = super.doPage( actionForm, request, response);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> doSort() in EstatementReportAction.");
		}

		return forward;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.platform.web.report.BaseReportAction#
	 * populateReportForm(org.apache.struts.action.ActionMapping,
	 * com.manulife.pension.platform.web.report.BaseReportForm,
	 * javax.servlet.http.HttpServletRequest)
	 */
	protected void populateReportForm(
			BaseReportForm reportForm, HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateReportForm");
		}

		String task = getTask(request);

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
	 * do default action called when RUM (Internal User) when trying to access
	 * the RIA statements page
	 */
	 
	public String doCommonForExternalUser(BaseReportForm reportForm, HttpServletRequest request,HttpServletResponse response) 
	throws  SystemException {
		
		String forward = null;

		EstatementReportForm estatementReportForm = (EstatementReportForm) reportForm;

		// Get the role of the user from User Profile object and set the
		// userType in the form.
		BDUserProfile profile = BDSessionHelper.getUserProfile(request);
		String userType = profile.getRole().getRoleType().getUserRoleCode();
		List errors = new ArrayList<ValidationError>();

		
		estatementReportForm.setUserType(userType);
		if (profile.isInternalUser()) {
			
			forward = forwards.get("internal");
		} else {
			
			String filterRiaStmDateStr = estatementReportForm.getRiaStmDateFilter();
			Date filterRiaStmDate = null;
			try {
				if(filterRiaStmDateStr != null && StringUtils.isNotEmpty(filterRiaStmDateStr) && !"ALL".equalsIgnoreCase(filterRiaStmDateStr)){
					filterRiaStmDate = mdyDateFormat.parse(filterRiaStmDateStr);
				}
			} catch (ParseException e) {
				throw new SystemException("parser error");
			}
			String filterRiaFirm = estatementReportForm.getRiaFirmFilter();
			
			String riaServiceSessionId = "";
	        HttpSession session =request.getSession(false);
	        
			List<RiaStatementVO> riaStatementsList = new ArrayList<RiaStatementVO>();
			List<Date> listDate = new ArrayList<Date>();
			List<String> listDateStr = new ArrayList<String>();
			List<String> listFirms = new ArrayList<String>();
			List<BrokerDealerFirm> riaFirmsList = BDWebCommonUtils.getRIAUserFirmList(profile.getBDPrincipal().getProfileId());
			
			try {
			if(!riaFirmsList.isEmpty()){
				List<String> riaFirms = new ArrayList<String>();
				for(BrokerDealerFirm firm : riaFirmsList){
					if(firm.isFirmPermission()){
						riaFirms.add(String.valueOf(firm.getId()));
						if(listFirms != null && !listFirms.contains(firm.getFirmName())){
							listFirms.add(firm.getFirmName());
						}
					}
				}
				// TIBCO RIA call
				RiaStatementsRequestData riaStatementsRequestData= new RiaStatementsRequestData();
				RiaStatementsResponseData riaStatementsResponseData= new RiaStatementsResponseData();
				//populateRiaStatementRequest()
				
				riaStatementsRequestData.setProfileId(String.valueOf(profile.getBDPrincipal().getProfileId()));
				riaStatementsRequestData.setBusinessUnit(RiaStatementsEnum.BusinessUnit.RPS);
				riaStatementsRequestData.setDivision(RiaStatementsEnum.Division.USA);
				riaStatementsRequestData.setProductType(RiaStatementsEnum.ProductType.LIC);
				riaStatementsRequestData.setProductSubType(RiaStatementsEnum.ProductSubType.VLI);
				
				riaStatementsRequestData.setRiaFirms(riaFirms);
				riaStatementsResponseData = StatementServiceDelegate.getInstance().getRiaStatementsList(riaStatementsRequestData);
				
				if(riaStatementsResponseData != null && riaStatementsResponseData.getDocument() != null && !riaStatementsResponseData.getDocument().isEmpty()){
					riaStatementsList = populateRiaStatementList(riaStatementsResponseData);
					riaServiceSessionId = riaStatementsResponseData.getSessionId();
					
					for(RiaStatementVO riaStatementVO : riaStatementsList){
						if(listDate != null && !listDate.contains(riaStatementVO.getGenDate())){
							listDate.add(riaStatementVO.getGenDate());
						}

					}
					
					if(filterRiaStmDate != null){
						List<RiaStatementVO> tempRiaStatementsList = new ArrayList<RiaStatementVO>();
						for(RiaStatementVO riaStatementVO : riaStatementsList){
							if(filterRiaStmDate.compareTo(riaStatementVO.getGenDate())==0){
								tempRiaStatementsList.add(riaStatementVO);
							}
						}
						riaStatementsList = tempRiaStatementsList;
					}
					
					if(filterRiaFirm != null && !"ALL".equalsIgnoreCase(filterRiaFirm) && StringUtils.isNotEmpty(filterRiaFirm)){
						List<RiaStatementVO> tempRiaStatementsList = new ArrayList<RiaStatementVO>();
						for(RiaStatementVO riaStatementVO : riaStatementsList){
							if(StringUtils.equalsIgnoreCase(filterRiaFirm, riaStatementVO.getFirmName())){
								tempRiaStatementsList.add(riaStatementVO);
							}
						}
						riaStatementsList = tempRiaStatementsList;
					}
				}
				session.setAttribute("riaServiceSessionId", riaServiceSessionId);
			}
			}
			catch (Exception ex) {
				logger.error("Exception in firm search " + ex);
				errors.add(new ValidationError(" ", BDErrorCodes.RIA_STATEMENT_NOT_ACCESSABLE));
			}
						
			// Setting the value in the form
			if(riaStatementsList != null){
				estatementReportForm.setRiaStatementListVO(riaStatementsList);
				
				Collections.sort(listDate, new Comparator<Date>() {

					@Override
					public int compare(Date arg0, Date arg1) {
						return arg1.compareTo(arg0);
					}
				});
				estatementReportForm.setListDates(listDate);
				
				for(Date stmDate : listDate){
					listDateStr.add(mdyDateFormat.format(stmDate));
				}
				estatementReportForm.setListDatesStr(listDateStr);
				
				Collections.sort(listFirms, new Comparator<String>() {

					@Override
					public int compare(String arg0, String arg1) {
						return arg0.compareToIgnoreCase(arg1);
					}
				});
				estatementReportForm.setListFirms(listFirms);
			}
			
			forward = forwards.get("external");
		}
		if (!errors.isEmpty()) {
			setErrorsInSession(request, errors);
		}
		return forward;
	}

	/**
	 * do FetchDocuments called when a RUM user /RIA User chooses to fetch
	 * Statements for RIA firm/firms
	 * 
	 * @param reportForm
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	 
	public String doCommonForInternalUser( BaseReportForm reportForm, HttpServletRequest request,HttpServletResponse response) 
	throws  SystemException {
		
		String forward = null;
		
		String riaServiceSessionId = "";
        HttpSession session =request.getSession(false);
        
		EstatementReportForm estatementReportForm = (EstatementReportForm) reportForm;
		BDUserProfile profile = BDSessionHelper.getUserProfile(request);
		List<RiaStatementVO> riaStatementsList = new ArrayList<RiaStatementVO>();
		List errors = new ArrayList<ValidationError>();
		try {
			if(estatementReportForm != null && estatementReportForm.getSelectedFirmId() != null){
				// TIBCO RIA call
				RiaStatementsRequestData riaStatementsRequestData= new RiaStatementsRequestData();
				RiaStatementsResponseData riaStatementsResponseData= new RiaStatementsResponseData();
				//populateRiaStatementRequest()
				
				riaStatementsRequestData.setProfileId(String.valueOf(profile.getBDPrincipal().getProfileId()));
				riaStatementsRequestData.setBusinessUnit(RiaStatementsEnum.BusinessUnit.RPS);
				riaStatementsRequestData.setDivision(RiaStatementsEnum.Division.USA);
				riaStatementsRequestData.setProductType(RiaStatementsEnum.ProductType.LIC);
				riaStatementsRequestData.setProductSubType(RiaStatementsEnum.ProductSubType.VLI);
				List<String> riaFirms = new ArrayList<String>();
				riaFirms.add(estatementReportForm.getSelectedFirmId());
				riaStatementsRequestData.setRiaFirms(riaFirms);
				riaStatementsResponseData = StatementServiceDelegate.getInstance().getRiaStatementsList(riaStatementsRequestData);
				
				if(riaStatementsResponseData != null && riaStatementsResponseData.getDocument() != null && !riaStatementsResponseData.getDocument().isEmpty()){
					riaStatementsList = populateRiaStatementList(riaStatementsResponseData);
					riaServiceSessionId = riaStatementsResponseData.getSessionId();
				}
				session.setAttribute("riaServiceSessionId", riaServiceSessionId);
				
				// Setting the value in the form
				if(riaStatementsList != null){
					estatementReportForm.setRiaStatementListVO(riaStatementsList);
				}
			}
			
		} catch (Exception ex) {
			logger.error("Exception in firm search " + ex);
			errors.add(new ValidationError(" ", BDErrorCodes.RIA_STATEMENT_NOT_ACCESSABLE ));
		}
		
		String userType = estatementReportForm.getUserType();
		
		userType = "RUM";
		
		if (StringUtils.equals(userType, "RUM")){
			forward = forwards.get("internal");
		} else if (StringUtils.equals(userType, "RIA")){
			forward = forwards.get("external");
		}
		if (!errors.isEmpty()) {
			setErrorsInSession(request, errors);
		}
		return forward;
	}
	
	private List<RiaStatementVO> populateRiaStatementList(
			RiaStatementsResponseData riaStatementsResponseData) {
		
		List<RiaStatementVO> riaStatementsList = new ArrayList<RiaStatementVO>();
		
		List<RiaStatementDocument> riaStatements= riaStatementsResponseData.getDocument();
		
		for (RiaStatementDocument riaStatementDocument : riaStatements){
			RiaStatementVO riaStatementVO = new RiaStatementVO();
			riaStatementVO.setCsvDocId(riaStatementDocument.getDocumentId());
			riaStatementVO.setPdfDocId(riaStatementDocument.getDocumentId());
			riaStatementVO.setFirmName(riaStatementDocument.getFirmName());
			riaStatementVO.setFirmId(riaStatementDocument.getFirmId());
			riaStatementVO.setGenDateStr(riaStatementDocument.getStatementMonthDateStr());
			riaStatementVO.setGenDate(StringUtils.isEmpty(riaStatementDocument.getStatementMonthDateStr()) ? new Date(): getStatementDate(riaStatementDocument.getStatementMonthDateStr()));
			riaStatementsList.add(riaStatementVO);
		}
		
		return riaStatementsList;
	}

	private Date getStatementDate(String dateStr) {
		Date statementDate = null;
		try {
			statementDate = ymdDateFormat.parse(dateStr);
		} catch (ParseException e) {
			logger.error("Exception in statementDate Parse " + e);
		}
		return statementDate;
	}

	/**
	 * 
	 * @param selectedFirmId
	 * @return
	 */
	private List<RiaStatementVO> getRiaStatementList(String selectedFirmId) {
		List<RiaStatementVO> riaStatementsList = new ArrayList<RiaStatementVO>();
		
		return riaStatementsList;
	}

	/**
	 * doDownloadDocuments method is called when RUM user /RIA User chooses to download
	  	statements for respective RIA firm/firms
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ={"/fetch",""},params= {"task=downloadDocuments"},  method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doDownloadDocuments(@Valid @ModelAttribute("estatementReportForm") EstatementReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
			String uponError = null;
			BDUserProfile profile = BDSessionHelper.getUserProfile(request);
			if (profile == null) {
				uponError =  CommonConstants.FAIL;
			} else if (profile.isInternalUser()) {
				uponError = CommonConstants.INTERNAL ;
			} else {
				uponError = CommonConstants.EXTERNAL;
			}
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get(uponError);//if input forward not //available, provided default
	       }
		}
		
			EstatementReportForm estatementReportForm = (EstatementReportForm) actionForm;
			System.out.println("entered to print");
			String selectedFileType=estatementReportForm.getFileType();
			System.out.println(selectedFileType);
			String selectedDocId=estatementReportForm.getDocId();
			System.out.println(selectedDocId);
			String statementFirmId=estatementReportForm.getStatementFirmId();
			String statementFirmName=estatementReportForm.getStatementFirmName();
			
			if(StringUtils.isNotEmpty(statementFirmName))
			{
				statementFirmName= statementFirmName.replaceAll("\\s+", "_");		   
			}
			String statementGenDateStr=estatementReportForm.getStatementGenDateStr();
			
			Date statementDate = getStatementDate(statementGenDateStr);
			String fileNameDateStr = mdyDateFormat.format(statementDate);
			
			String fileName = StringUtils.EMPTY;
			if(StringUtils.isNotEmpty(statementFirmId)){
				fileName = statementFirmId + "_" + statementFirmName + "_" + fileNameDateStr;
			}else{
				fileName = statementFirmName + "_" + fileNameDateStr;
			}
			
			System.out.println(fileName);
			fileName = URLEncoder.encode(fileName, "UTF-8");
			String riaServiceSessionId = "";
	        HttpSession session =request.getSession(false);
	        riaServiceSessionId = (String) session.getAttribute("riaServiceSessionId");
			try
			{
			
				BDUserProfile profile = BDSessionHelper.getUserProfile(request);
				
				RiaStatementsRequestData riaStatementsRequestData = new RiaStatementsRequestData();
				riaStatementsRequestData.setProfileId(String.valueOf(profile.getBDPrincipal().getProfileId()));
				riaStatementsRequestData.setDocumentId(selectedDocId);
				riaStatementsRequestData.setSessionId(riaServiceSessionId);
				riaStatementsRequestData.setBusinessUnit(RiaStatementsEnum.BusinessUnit.RPS);
				riaStatementsRequestData.setDivision(RiaStatementsEnum.Division.USA);
				riaStatementsRequestData.setProductType(RiaStatementsEnum.ProductType.LIC);
				riaStatementsRequestData.setProductSubType(RiaStatementsEnum.ProductSubType.VLI);
				if("pdf".equalsIgnoreCase(selectedFileType)){
					riaStatementsRequestData.setDocumentFormat(RiaStatementsEnum.DocumentFormat.PDF);
				}else if("csv".equalsIgnoreCase(selectedFileType)) {
					riaStatementsRequestData.setDocumentFormat(RiaStatementsEnum.DocumentFormat.CSV);
				}
				riaStatementsRequestData.setDocumentType(RiaStatementsEnum.DocumentType.RIASTATEMENT);
				riaStatementsRequestData.setDocumentStatus(RiaStatementsEnum.DocumentStatus.ACTIVE);
				riaStatementsRequestData.setRevisionIndex(new BigInteger("1"));

				System.out.println("+++++++++++++++++++++++++++++ TIBCO START ++++++++++++++++++++++");
				
				RiaStatementDocument riaStatementDocument = StatementServiceDelegate.getInstance().getRiaStatementDocument(riaStatementsRequestData);
				
				System.out.println("+++++++++++++++++++++++++++++ TIBCO END ++++++++++++++++++++++");
								
				byte[] download = null;
				
				if(riaStatementDocument != null && riaStatementDocument.getDocument() != null){
					System.out.println("+++++++++++++++++++++++++++++ DOC BYTES AVAILABLE ++++++++++++++++++++++");
					download = riaStatementDocument.getDocument();
				}
				if (download != null && download.length > 0) {

					if("pdf".equalsIgnoreCase(selectedFileType)){
						streamRiaDownloadData(request, response,
								"application/pdf; name="+fileName+".pdf", fileName+".pdf", download);
					}else if("csv".equalsIgnoreCase(selectedFileType)) {
						streamRiaDownloadData(request, response,
								"text/csv", fileName+".csv", download);
					}
				}	
				 
				return null;
		        
			}
			catch(Exception e)
			{
				System.out.println("not found");
			}
			
			return null;
	}
	
	/**
     * Stream the download data to the Response. This method is singled out and
     * made public static so that non-ReportActions can use it. This method
     * closes the Response's OutputStream when it returns.
     * @throws SystemException
     */
    public static void streamRiaDownloadData(HttpServletRequest request,
            HttpServletResponse response, String contentType, String fileName,
            byte[] downloadData) throws SystemException {

        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0,must-revalidate");
        response.setHeader("Pragma", "no-cache");
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

        if (userAgent == null || userAgent.indexOf("MSIE 5") < 0) {
            response.setHeader("Content-Disposition", "attachment; filename="
                    + fileName);
        }

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

	@Override
	protected String getReportId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getReportName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDefaultSortDirection() {
		return DEFAULT_SORT_DIRECTION;
	}

	@Override
	protected String getDefaultSort() {
		return DEFAULT_SORT;
	}

	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm,
			ReportData report, HttpServletRequest request)
			throws SystemException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void populateReportCriteria(ReportCriteria criteria,
			BaseReportForm form, HttpServletRequest request)
			throws SystemException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Validate form and request against penetration attack, prior to other
	 * validations.
	 */
	
	@Autowired
	private BDValidatorFWNull bdValidatorFWNull;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWNull);
	}
}
