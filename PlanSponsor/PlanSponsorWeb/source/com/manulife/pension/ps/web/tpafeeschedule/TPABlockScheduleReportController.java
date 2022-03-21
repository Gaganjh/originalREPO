package com.manulife.pension.ps.web.tpafeeschedule;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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

import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.ps.service.report.feeSchedule.TpaFeeScheduleDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.fee.valueobject.BlockScheduleReportVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.util.Pair;

/**
 * 
 * Action class to down load block schedule report
 * 
 * @author Sithomas
 *
 */
@Controller
@RequestMapping( value = "/tpa")

public class TPABlockScheduleReportController extends BaseReportController {
	@ModelAttribute("blockScheduleReportForm") 
	public ReportForm populateForm()
{
		return new ReportForm();
		}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/tpafeeschedule/viewTpaStandardFeeSchedule.jsp");
		}

	private String CSV_REPORT_NAME = "_block_sched_rpt_";
	public static final String UNDERSCORE = "_";
	public static final DecimalFormat decimalFormatter = new DecimalFormat("00");
	
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
	public static final FastDateFormat dateFormatter = FastDateFormat.getInstance("MMM/dd/yyyy");
	
	public static final String REPORT_TITLE = "404a-5 Important Information Block Fee Schedule Report";
	public static final String TPA_TITLE = "TPA: ";
	public static final String AS_OF_TITLE = "As of: ";
	
	public static final String CONTRACT_NAME = "Contract Name";
	public static final String CONTRACT_NUMBER = "Contract Number";
	public static final String LAST_UPDATED_DATE = "Last Update Date";
	public static final String LAST_UPDATED_USER = "Last Update User";
	public static final String FEE_SCHEDULE = "Fee Schedule";
	
	//synchronizes this method to avoid race condition.
	public static synchronized String formatDecimalFormatter(int value) { 
        return decimalFormatter.format(value); 
    }
	
	
	 /**
     * Invokes the default task (the initial page). It uses the common workflow
     * with validateForm set to true.
     *
     * @see #doCommon(ActionMapping, BaseReportForm, HttpServletRequest,
     *      HttpServletResponse, boolean)
     */
	@RequestMapping(value ="/blockScheduleReport/",  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("blockScheduleReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

    	if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        } 
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> doDefault");
        }

        String forward = doGetBlockScheduleReport( form, bindingResult, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- doDefault");
        }

        return forward;
    }
    

	/**
	 * This method forwards the request to the CSV Download for block schedule
	 * report.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value ="/blockScheduleReport/" ,params={"action=getBlockScheduleReport","task=getBlockScheduleReport"}   , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doGetBlockScheduleReport (@Valid @ModelAttribute("blockScheduleReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        } 
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doGetBlockScheduleReport");
		}

		byte[] downloadData = null;

		String tpaFirmIdString = request.getParameter("selectedTpaId");
		
		if(StringUtils.isEmpty(tpaFirmIdString) 
				|| !StringUtils.isNumeric(tpaFirmIdString)) {
			throw new SystemException("Incorrect TPA ID - " + tpaFirmIdString);
		}
		
		int  tpaFirmId = Integer.valueOf(tpaFirmIdString);
		
		TPAFirmInfo firm = TPAServiceDelegate.getInstance().getFirmInfo(tpaFirmId);
		String tpaFirmName = firm.getName();
		
		String companyCode = Constants.COMPANY_NAME_NY.equalsIgnoreCase(Environment.getInstance().getSiteLocation()) 
 		? Constants.COMPANY_ID_NY : Constants.COMPANY_ID_US;
		
		Calendar reportRequestedDate = Calendar.getInstance();

		List<BlockScheduleReportVO> reportData = FeeServiceDelegate
				.getInstance(Constants.PS_APPLICATION_ID)
				.getBlockScheduleReportData(tpaFirmId,companyCode);
		setLastUpdatedTimeStamp(reportData, tpaFirmId);

		downloadData = getDownloadData(tpaFirmId, tpaFirmName, reportRequestedDate,
				reportData);

		streamDownloadData(request, response, CSV_TEXT,
				getFileName(tpaFirmId, reportRequestedDate), downloadData);

		/**
		 * No need to forward to any other JSP or action. Returns null will make
		 * Struts to return controls back to server immediately.
		 */
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doGetBlockScheduleReport");
		}
		return null;

	}

	/**
	 * 
	 * get csv as a byte array
	 * 
	 * @param tpaFirmId
	 * @param reportRequestedDate
	 * @param reportData
	 * 
	 * @return byte[]
	 * 
	 * @throws SystemException
	 */
	protected byte[] getDownloadData(int tpaFirmId, String tpaFirmName,
			Calendar reportRequestedDate, List<BlockScheduleReportVO> reportData)
			throws SystemException {
		
		 StringBuffer buff = new StringBuffer(255);
		 String tpaDescription=StringUtils.EMPTY;
		 
		 buff.append(REPORT_TITLE).append(LINE_BREAK);
		 buff.append(escapeField(tpaDescription.concat(TPA_TITLE).concat(String.valueOf(tpaFirmId)).concat(Constants.HYPHON_SYMBOL).concat(tpaFirmName))).append(LINE_BREAK);		 
		 buff.append(AS_OF_TITLE).append(dateFormatter.format(reportRequestedDate.getTime())).append(LINE_BREAK);
		 
		 buff.append(LINE_BREAK);
		 
		 
		 // add header
		 buff.append(CONTRACT_NAME).append(COMMA);
		 buff.append(CONTRACT_NUMBER).append(COMMA);
		 buff.append(LAST_UPDATED_DATE).append(COMMA);
		 buff.append(LAST_UPDATED_USER).append(COMMA);
		 buff.append(FEE_SCHEDULE).append(COMMA);
		 buff.append(LINE_BREAK);
		 
		 // add report
		 for(BlockScheduleReportVO blockScheduleReportVO : reportData) {
			 buff.append(escapeField(blockScheduleReportVO.getContractName())).append(COMMA);
			 buff.append(blockScheduleReportVO.getContractId()).append(COMMA);
			 if(blockScheduleReportVO.getLastUpdateDate() != null) {
				 buff.append( dateFormatter.format(blockScheduleReportVO.getLastUpdateDate()));
			 }
			 buff.append(COMMA);
			 if(blockScheduleReportVO.getLastUpdatedUserName() != null) {
				 buff.append(blockScheduleReportVO.getLastUpdatedUserName());
			 }
			 buff.append(COMMA);
			 buff.append(blockScheduleReportVO.getScheduleType()).append(COMMA);
			 buff.append(LINE_BREAK);
		 }
		
		return  buff.toString().getBytes();
	}

	/**
	 * Returns file name
	 * 
	 */
	private String getFileName(int tpaFirmId, Calendar reportRequestedDate) {
		StringBuilder fileName = new StringBuilder();
		fileName
				.append(tpaFirmId)
				.append(CSV_REPORT_NAME)
				.append(formatDecimalFormatter(reportRequestedDate.get(Calendar.MONTH) + 1).trim())
				.append(UNDERSCORE)
				.append(formatDecimalFormatter(reportRequestedDate.get(Calendar.DATE)).trim())
				.append(UNDERSCORE)
				.append(formatDecimalFormatter(reportRequestedDate.get(Calendar.YEAR)).trim())
				.append(CSV_EXTENSION);
		return fileName.toString();
	}

	@Override
	protected String getReportId() {
		return null;
	}

	@Override
	protected String getReportName() {
		return null;
	}

	@Override
	protected String getDefaultSort() {
		return null;
	}

	@Override
	protected String getDefaultSortDirection() {
		return null;
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

	}
	
	/**
	 * Adding escape field if any comma character found in a String
	 * 
	 * @param field
	 * @return
	 */
	private String escapeField(String field) {
		if (field.indexOf(COMMA) != -1) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append(QUOTE).append(field).append(QUOTE);
			return newField.toString();

		} else {
			return field;
		}
	}
	
	private void setLastUpdatedTimeStamp(List<BlockScheduleReportVO> reportData, int tpaFirmId) throws SystemException{
		for(BlockScheduleReportVO vo : reportData){
			Pair<String, Timestamp> lastUpdatedUserDetails = TpaFeeScheduleDetails.getLastUpdateTpaCustomScheduleDetails(vo.getContractId(), tpaFirmId);
			vo.setLastUpdateDate(lastUpdatedUserDetails.getSecond());
			vo.setLastUpdatedUserName(lastUpdatedUserDetails.getFirst());
		}
	}
	
	@RequestMapping(value ="/blockScheduleReport/" ,params={"action=filter","task=filter"}   ,method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doFilter (@Valid @ModelAttribute("blockScheduleReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doFilter( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	
	@RequestMapping(value ="/blockScheduleReport/" ,params={"action=page","task=page"}   , method =  {RequestMethod.POST,RequestMethod.GET}) 
		public String doPage (@Valid @ModelAttribute("blockScheduleReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
		throws IOException,ServletException, SystemException {
			if(bindingResult.hasErrors()){
		        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
		       if(errDirect!=null){
		              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
		return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
		       }
			}
		       String forward=super.doPage( form, request, response);
				return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	 }
		
	
	@RequestMapping(value ="/blockScheduleReport/" ,params={"action=sort","task=sort"}   ,method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSort (@Valid @ModelAttribute("blockScheduleReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doSort( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
 }
	@RequestMapping(value ="/blockScheduleReport/"  ,params={"action=download","task=download"}   , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDownload (@Valid @ModelAttribute("blockScheduleReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException{
	if(bindingResult.hasErrors()){
        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
       if(errDirect!=null){
              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
       }
	}
       String forward=super.doDownload( form, request, response);
		return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
}
	
	@RequestMapping(value ="/blockScheduleReport/",params={"action=downloadAll","task=downloadAll"}   ,  method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doDownloadAll (@Valid @ModelAttribute("blockScheduleReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doDownload( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/blockScheduleReport/", params={"action=print","task=print"}   , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrint (@Valid @ModelAttribute("blockScheduleReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doPrint( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	@RequestMapping(value ="/blockScheduleReport/",params={"action=save","task=save"}   , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doSave (@Valid @ModelAttribute("blockScheduleReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
	       }
		}
	       String forward=super.doSave( form, request, response);
			return StringUtils.contains(forward,'/')?forward:forwards.get(forward); 
	}
	
	@RequestMapping(value ="/blockScheduleReport/",params={"action=printPDF","task=printPDF"}   , method =  {RequestMethod.POST,RequestMethod.GET}) 
	public String doPrintPDF (@Valid @ModelAttribute("blockScheduleReportForm") ReportForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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
	 * This code has been changed and added to Validate form and
	 */

	  @Autowired
 private PSValidatorFWInput  psValidatorFWInput;
@InitBinder
public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
  binder.bind( request);
  binder.addValidators(psValidatorFWInput);
}
}
