/**
 * 
 */
package com.manulife.pension.ps.web.sendservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.NoticeServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.contract.csf.CsfConstants;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.sendservice.NoticeActiveMailReportController.Handle;
import com.manulife.pension.service.common.cache.NoticeLookup;
import com.manulife.pension.service.common.enumeration.NoticeEnums;
import com.manulife.pension.service.common.exception.ErrorMessages;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.VestingSchedule;
import com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO;
import com.manulife.pension.service.notices.valueobject.NoticePlanDataVO;
import com.manulife.pension.service.request.valueobject.NoticeRequest;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;

/**
 * @author krishta
 *
 */
@Controller
@RequestMapping(value ="/sendservice")

public class NoticeDataViewController extends PsAutoController {
	
	@ModelAttribute("noticePlanDataForm") 
	public NoticePlanDataForm populateForm()
	{
		return new NoticePlanDataForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
	forwards.put("input","/sendservice/noticeplandataview.jsp");
	forwards.put("default","/sendservice/noticeplandataview.jsp");
	forwards.put("active","/do/sendservice/activeMail/");
	forwards.put("completed","/do/sendservice/completedMail/"); 
	forwards.put("secureHomePage","redirect:/do/home/homePage/");
	forwards.put("planDataView","/do/sendservice/planData/");
	}

	private static final String HOMEPAGE="secureHomePage";
	private static final String PDF_GENERATED = "pdfGenerated";
	private static final String GENERATED_PDF ="isReportGenerated";
	private static final String PLAN_INVESTMENT_NOTICE_404A_5 = "404a-5 Plan & Investment Notice Only";
	private static final String PLAN_INVESTMENT_NOTICE_STATUS  = "PR";
	private static final int TRACKING_NUMBER = 1;
	private static final String NOTICE_MATCH_TYPE = "NOTICE_MATCH_TYPE";
    private static final String NOTICE_MATCH_TYPE_VALUE_1="1";
    private static final String NOTICE_MATCH_TYPE_VALUE_2="2";
	
	/**
	 * SimpleDateFormat is converted to FastDateFormat to make it thread safe
	 */
	private static final String FORMAT_DATE_EXTRA_LONG_MDY = "MM_dd_yyyy";
	private static final FastDateFormat formattedChangedDate = FastDateFormat.getInstance(FORMAT_DATE_EXTRA_LONG_MDY); 
	
	/* (non-Javadoc)
	 * @see com.manulife.pension.platform.web.controller.BaseAutoAction#doDefault(org.apache.struts.action.ActionMapping, com.manulife.pension.platform.web.controller.AutoForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@RequestMapping(value ="/planData/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("noticePlanDataForm") NoticePlanDataForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {


    	logger.info("doDefault> Entry - doDefault.");    
    	try{
    	        
    	    
    	    //CR024 Start
    	    List<LabelValueBean> setMatchAppliesToContribList= setMatchAppliesToContribList();
    	    form.setMatchAppliesToContribList(setMatchAppliesToContribList);
    	    //CR024 End
    	    
            NoticePlanDataController noticePlanDataController = new NoticePlanDataController();            
            
            UserProfile userProfile = getUserProfile(request);
			Integer contractId = userProfile.getCurrentContract().getContractNumber();
			
			logger.info("doDefault read notice plan common data from database and display in page");
            NoticePlanCommonVO noticePlanCommonVO  = ContractServiceDelegate.getInstance().readNoticePlanCommonData(contractId);
			
            if(noticePlanCommonVO!=null){
            	Map csfMap=null;
                if(csfMap == null){
    				ContractServiceDelegate service = ContractServiceDelegate.getInstance();
    				
    				try {
    					csfMap = service.getContractServiceFeatures(contractId);
    				} catch (ApplicationException ae) {
    					throw new SystemException(ae.toString() + "EditPlanDataAction" 
    							+ "doDefault" 
    							+ ae.getDisplayMessage());
    				}
    			}
                
               //Notice Generation service - To load the CSF page with selected values
        		ContractServiceFeature noticeGenerationService = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_CD);
        		if(noticeGenerationService !=null){
        		    if(StringUtils.isNotBlank(noticeGenerationService.getValue()) && "Y".equalsIgnoreCase(noticeGenerationService.getValue())){
        		        
        		        String noticeOption = noticeGenerationService.getAttributeValue(ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_OPTCD);    		        
        		        if(StringUtils.isNotBlank(noticeOption)){
        		        	if(CsfConstants.NOTICE_OPT_404A5.equals(noticeOption.trim())){
        		        		noticePlanCommonVO.setNoticeType("404a-5 Plan & Investment Notice Only");
        		        		form.setEligibleNotice(false);
        	                }
        	                else if(CsfConstants.NOTICE_OPT_QDIA.equals(noticeOption.trim())){
        	                	noticePlanCommonVO.setNoticeType("Qualified Default Investment Alternative Only");
        	                	form.setEligibleNotice(true);
        	                }
        	                else if(CsfConstants.NOTICE_OPT_AUTO.equals(noticeOption.trim())){
        	                	noticePlanCommonVO.setNoticeType("Automatic Arrangement");
        	                	form.setEligibleNotice(true);
                            }
                            else if(CsfConstants.NOTICE_OPT_AUTO_QDIA.equals(noticeOption.trim())){
                            	noticePlanCommonVO.setNoticeType("Automatic Arrangement with Qualified Default Investment Alternative");
                            	form.setEligibleNotice(true);
                            }
                            else if(CsfConstants.NOTICE_OPT_SH.equals(noticeOption.trim())){
                            	noticePlanCommonVO.setNoticeType("Safe Harbor");
                            	form.setEligibleNotice(true);
                            }
                            else if(CsfConstants.NOTICE_OPT_SH_QDIA.equals(noticeOption.trim())){
                            	noticePlanCommonVO.setNoticeType("Safe Harbor with Qualified Default Investment Alternative");
                            	form.setEligibleNotice(true);
                            }
        		        }
        		        else{
        		        	noticePlanCommonVO.setNoticeType(null);
        		        	form.setEligibleNotice(false);
        		        }
        		    } else{
        		    	form.setEligibleNotice(false);
        		    }
        		}        		
            }
            
            /* add this to a method : this is done to set the collection size of setMoneyTypeExcludeObject in the form to be same
             * as the size of the Vesting schedule collection object*/
            if(noticePlanCommonVO!=null && null!=noticePlanCommonVO.getVestingSchedules()){
            	Collection<MoneyTypeExcludeObject> moneyTypeExcludeObjectList =  new ArrayList<MoneyTypeExcludeObject>(noticePlanCommonVO.getVestingSchedules().size());
            	for(VestingSchedule vestingSchedule : noticePlanCommonVO.getVestingSchedules()) {
            		moneyTypeExcludeObjectList.add(new MoneyTypeExcludeObject());
            	}
        	    form.setMoneyTypeExcludeObject(moneyTypeExcludeObjectList);
            }
            
            logger.info("doDefault read notice plan data from database and display in page");
            NoticePlanDataVO noticePlanDataVO = ContractServiceDelegate.getInstance().readNoticePlanData(contractId, null);
            form = noticePlanDataController.setValuesToForm(noticePlanDataVO, noticePlanCommonVO, form, null);
           
            form.setNoticePlanDataVO(noticePlanDataVO);  
            
            //validate for PIF data
            List<GenericException> pifDataerrors = new ArrayList<GenericException>();
            pifDataerrors= noticePlanDataController.validatePIFData(noticePlanCommonVO,form);            
            noticePlanDataController.validateContactAddress(noticePlanCommonVO, pifDataerrors);            		
            noticePlanDataController.validateSummaryTab(noticePlanCommonVO, pifDataerrors);
            
        	if(!pifDataerrors.isEmpty()){
        		setErrorsInSession(request, pifDataerrors);
        	} 
        	
        	
        	
        	request.getSession().setAttribute("noticePlanCommonVO", noticePlanCommonVO);
        	request.getSession().setAttribute("noticePlanDataVO", noticePlanDataVO);
            request.getSession().setAttribute(Constants.SEND_SERVICE_PLAN_DATA_FORM, form);
            logger.info("doDefault> Exiting - doDefault");   
            return forwards.get("default");
    	}
		catch(Exception e){
		    logger.error("doDefault method Exception"+e.getMessage());
		    throw new SystemException(e, "Error in doDefault method in NoticeDataViewAction class");
		} 
    }
	@RequestMapping(value ="/planData/",params={"task=printPDF"}, method =  {RequestMethod.GET}) 
	public String doPrintPDF (@Valid @ModelAttribute("noticePlanDataForm") NoticePlanDataForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doPrintPDF( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	

	/**
	 * DFS: CR023 Ad Hoc Notice Generation Function
	 * This method is used to generate an eligible notice document for the participants of notice type in
	 *  Contract Service Feature (CSF) page does not equal “404a-5 Plan & Investment Notice only
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws SystemException
	 */
	@RequestMapping(value ="/planData/",params={"action=previewNoticeDocument"},method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPreviewNoticeDocument (@Valid @ModelAttribute("noticePlanDataForm") NoticePlanDataForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		List<GenericException> errors = new ArrayList<GenericException>();
		NoticeServiceDelegate noticeServiceDelegate = 	NoticeServiceDelegate.getInstance();

		UserProfile userProfile = getUserProfile(request);
		if(!userProfile.isSendServiceAccessible()) {
			return forwards.get(HOMEPAGE);
		} 

		NoticeRequest noticeRequest = new NoticeRequest();
		noticeRequest.setContractId(userProfile.getCurrentContract().getContractNumber());
		noticeRequest.setOrderNo(TRACKING_NUMBER);
		noticeRequest.setNoticeStatus(PLAN_INVESTMENT_NOTICE_STATUS);
		noticeRequest.setNoticeEffectiveDate(new Date());
		noticeRequest.setNoticeTypeCode(NoticeEnums.NoticeType.ELIGIBLE.getType());
		noticeRequest.setAdHocRequest(true);

		try{
			boolean isPreviewNotice =  true;
			String fileFormat = "pdf";
			StringBuilder documentfileName = prepareSaveFileName(
					noticeRequest, fileFormat);
			noticeRequest.setNoticeFileName(documentfileName.toString());
			noticeRequest = noticeServiceDelegate.generateNoticeForPreviewPage(noticeRequest, true,isPreviewNotice);
			byte[] pdfData = noticeRequest.getNoticeDocumentVo().getPdfContent();
			response.getWriter().write(PDF_GENERATED);
			request.getSession(false).setAttribute(GENERATED_PDF, new Handle(pdfData,prepareDownloadFileName(noticeRequest,fileFormat)));
		}	
		catch(SystemException e){
			if(getDataError().contains(Integer.valueOf(e.getMessage())))
			{
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				logger.error(sw.toString());
				logger.error("DocumentGeneration failed for this Contract:"+noticeRequest.getContractId() + "and Notice Type:"
						+ noticeRequest.getNoticeTypeCode() +"Message:" + e.getMessage());
				errors.add(new ValidationError("Data Issue"               //DataIssue//
						, ErrorCodes.ERROR_DATA_ISSUE,
						Type.error));
			}
			if(getTechnicalError().contains(Integer.valueOf(e.getMessage())))
			{
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				logger.error(sw.toString());
				logger.error("DocumentGeneration failed for this Contract:"+noticeRequest.getContractId() + "and Notice Type:"
						+ noticeRequest.getNoticeTypeCode() +"Message:" + e.getMessage());
				errors.add(new ValidationError("Technical Issue"  //TechnicalIssue//
						, ErrorCodes.ERROR_TECHNICAL_ISSUE,
						Type.error));
			}
		}

		if(errors.size()>0)
		{
			setErrorsInSession(request, errors);
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
	@RequestMapping(value ="/planData/",params={"action=fetchPdf"},method ={RequestMethod.GET}) 
	public String doFetchPdf (@Valid @ModelAttribute("noticePlanDataForm") NoticePlanDataForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {


		HttpSession session = request.getSession(false);
		byte [] download;
		String downloadFileName;
		if(session.getAttribute(GENERATED_PDF) != null) {
			download = ((Handle)session.getAttribute(GENERATED_PDF)).getByteArray();
			downloadFileName = ((Handle)session.getAttribute(GENERATED_PDF)).getDocumentFileName();
			session.setAttribute(GENERATED_PDF, null);
		}else {
			doPreviewNoticeDocument( form, bindingResult, request, response);
			download = ((Handle)session.getAttribute(GENERATED_PDF)).getByteArray();
			downloadFileName = ((Handle)session.getAttribute(GENERATED_PDF)).getDocumentFileName();
			session.setAttribute(GENERATED_PDF, null);
		}
		if (download != null && download.length > 0) {
			BaseReportController.streamDownloadData(request, response,
					"application/pdf", downloadFileName, download);
		}	


		return null;
	}

	/**
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
	@RequestMapping(value ="/planData/",params={"action=errorReport"}, method ={RequestMethod.GET}) 
	public String doErrorReport (@Valid @ModelAttribute("noticePlanDataForm") NoticePlanDataForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		form.setEligibleNotice(true);
		return forwards.get("default");
	}

	/**
	 * @param noticeRequest
	 * @param fileFormat
	 * @return
	 */
	private StringBuilder prepareSaveFileName(NoticeRequest noticeRequest,
			String fileFormat) {
		StringBuilder documentfileName = new StringBuilder();
		String currentDate ;
		FastDateFormat formatter = FastDateFormat
				.getInstance("yyyy_MM_dd_HHmmss");
		currentDate = formatter.format(new Date());
		currentDate = currentDate.replaceAll("-", "_");
		documentfileName.append(noticeRequest.getContractId());
		documentfileName.append("_");
		documentfileName.append("ELIGIBLE");
		documentfileName.append("_");
		documentfileName.append(currentDate);
		documentfileName.append(".");
		documentfileName.append(fileFormat);
		return documentfileName;
	}
	
	/**
	 * @param noticeRequest
	 * @param fileFormat
	 * @return
	 */
	private String prepareDownloadFileName(NoticeRequest noticeRequest,
			String fileFormat) {
		StringBuilder documentfileName = new StringBuilder();
		documentfileName.append(noticeRequest.getContractId());
		documentfileName.append("_");
		documentfileName.append("ELIGIBLE");
		documentfileName.append("_");
		documentfileName.append(formattedChangedDate.format(new Date()));
		documentfileName.append(".");
		documentfileName.append(fileFormat);
		return documentfileName.toString();
	}

	/**
	 * If there is no enough data to generate the notice document, then this method will return a data error
	 * @return
	 */
	public List<Integer> getDataError()
	{
		Map<Integer, List<Integer>> dataErrorHashMap = new HashMap<Integer, List<Integer>>();
		List<Integer> dataError = new ArrayList<Integer>(Arrays.asList(
				ErrorMessages.Error.MISSING_FIELD.getCode(),
				ErrorMessages.Error.UN_UPDATE_KEYWORD.getCode(),
				ErrorMessages.Error.UN_FETCH_DATA.getCode(),//tech or data
				ErrorMessages.Error.UN_PARSE_DATE.getCode(),
				ErrorMessages.Error.UN_FETCH_RETURN_ADDRESS.getCode(),//tech or data
				ErrorMessages.Error.UN_FETCH_PARTICIPANT_DETAILS.getCode(),//tech or data
				ErrorMessages.Error.UN_GENERATE_404_DOC.getCode(),//tech or data
				ErrorMessages.Error.UN_CONV_PRINT_REQ_TO_XML.getCode(),
				ErrorMessages.Error.UN_CONV_MAILLIST_TO_XML.getCode(),
				ErrorMessages.Error.INCORRECT_TIME.getCode(),
				ErrorMessages.Error.NOTICEFIEL_OR_ADDRESSFILE_OR_PRINTORDERFILE_NOTNULL.getCode(),//need to confirm
				ErrorMessages.Error.UN_CREATE_ADDRESSDETAILS.getCode(),//need to confirm
				ErrorMessages.Error.INITSTATUS_NO_ORDERS.getCode(),//need to confirm
				ErrorMessages.Error.EREPORTS_NOTICEPDF_OR_ADDRESSXML_OR_CONTRACTXML_BLANK.getCode(),//need to confirm
				ErrorMessages.Error.EREPORTS_ADDRESSXML_BLANK.getCode(),//need to confirm
				ErrorMessages.Error.EREPORTS_CONTRACTXML_BLANK.getCode(),//need to confirm
				ErrorMessages.Error.ZIP_FAILED.getCode(),//need to confirm
				ErrorMessages.Error.UN_INITIALIZE_PARSER.getCode(),//data
				ErrorMessages.Error.UN_READ_INPUT_STREAM.getCode(),//data
				ErrorMessages.Error.UN_PARSE_XML_STREAM.getCode(),//data
				ErrorMessages.Error.DOCUMENT_NOT_AVAIL.getCode(),//data
				ErrorMessages.Error.EREPORT_CONTROLFILE_SAVE_FAILED.getCode()));//need to confirm
		dataErrorHashMap.put(3140,dataError);//data issue
		dataError=dataErrorHashMap.get(3140);
		return dataError;
	}

	/**
	 *  If there are some technical problems like report or RMI registry services down while notice document generation, then this method will return a technical error
	 * @return
	 */
	public List<Integer> getTechnicalError()
	{
		Map<Integer, List<Integer>> technicalErrorHashMap = new HashMap<Integer, List<Integer>>();
		List<Integer> technicalError = new ArrayList<Integer>(Arrays.asList(
				ErrorMessages.Error.TEMPLATE_NOT_AVAIL.getCode(),
				ErrorMessages.Error.EREPORTS_REMOTE_ERROR.getCode()
				,ErrorMessages.Error.EREPORTS_CALL_FAILED.getCode(),
				ErrorMessages.Error.EREPORT_DOC_SAVE_FAILED.getCode(),
				ErrorMessages.Error.CONTROLFILE_GEN_FAILED.getCode(),
				ErrorMessages.Error.FILENAME_CREATION_FAIL.getCode(),//need to confirm
				ErrorMessages.Error.UN_UPDATE_ADV_NOTIFICATION_STATUS.getCode(),//data or tech
				ErrorMessages.Error.UN_UPDATE_NOTICE_STATUS.getCode(),//data or tech
				ErrorMessages.Error.UN_INSERT_NOTICE_REQ_DETAILS.getCode(),//data or tech
				ErrorMessages.Error.UN_UPDATE_NOTICE_FILE_DETAILS.getCode(),//data or tech
				ErrorMessages.Error.UN_UPDATE_NOTICE_ERROR.getCode(),//data or tech
				ErrorMessages.Error.INVALID_ACTION.getCode(),
				ErrorMessages.Error.GENERAL_DOCUMENT_EXCEPTION.getCode(),
				ErrorMessages.Error.WRONG_FILEFORMAT_NOTICEFILE.getCode(),//need to confirm
				ErrorMessages.Error.WRONG_FILEFORMAT_ADDRESSFILE.getCode(),//need to confirm
				ErrorMessages.Error.WRONG_FILEFORMAT_PRINTORDERFILE.getCode(),//need to confirm
				ErrorMessages.Error.UN_GET_CONVERSION_TRIGGER_NOTICE_STATUS.getCode(),
				ErrorMessages.Error.PBM_GETTING_CONTRACT_SERVICE_FEATURE.getCode(),//tech or data
				ErrorMessages.Error.ADDRESS_FILE_NULL.getCode(),
				ErrorMessages.Error.PRINT_ORDER_FILE_NULL.getCode(),
				ErrorMessages.Error.EREPORTS_FAILED.getCode(),
				ErrorMessages.Error.NOTICE_FILE_NULL.getCode(),
				ErrorMessages.Error.NOTICE_FILE_NOT_IN_ARCHIVE.getCode(),//need to confirm
				ErrorMessages.Error.CONTROL_FILE_NOT_IN_ARCHIVE.getCode(),//need to confirm
				ErrorMessages.Error.PDF_NOT_AVAIL.getCode(),
				ErrorMessages.Error.FILE_ERROR_MERRILL_STATUS.getCode()));
		technicalErrorHashMap.put(3141,technicalError);
		technicalError=technicalErrorHashMap.get(3141);
		return technicalError;
	}
	
	//CR024 Start
    public List<LabelValueBean> setMatchAppliesToContribList() throws SystemException {
    	List<LabelValueBean> setMatchAppliesToContribList = new ArrayList<LabelValueBean>();
     	try{
    	NoticeServiceDelegate.getInstance().noticeLookUpInitilization();
    	setMatchAppliesToContribList.add(new LabelValueBean(NoticeLookup.getInstance().get(NOTICE_MATCH_TYPE,NOTICE_MATCH_TYPE_VALUE_1),NOTICE_MATCH_TYPE_VALUE_1));
    	setMatchAppliesToContribList.add(new LabelValueBean(NoticeLookup.getInstance().get(NOTICE_MATCH_TYPE,NOTICE_MATCH_TYPE_VALUE_2),NOTICE_MATCH_TYPE_VALUE_2));
    	}catch(SystemException e){
    		logger.error(e.getMessage());
    	}
		return setMatchAppliesToContribList;
	}
    //CR024 end
}
