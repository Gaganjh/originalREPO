package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Message;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.submission.valueobject.TPAVestingSubmissionReportData;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem;
import com.manulife.pension.ps.service.submission.valueobject.TPAVestingSubmissionDetailsVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWTpa;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.StringUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

/**
 * Used to download a list of the latest vesting submission
 * for each contract of a given TPA Firm or a given TPA user
 * 
 * @author Diana Macean
 * 
 */
@Controller
@RequestMapping(value="/tools")
public class TPAVestingSubmissionReportController extends ReportController {

	@ModelAttribute("tpaVestingSubmissionReportForm")
	public ReportForm populateForm() {
		return new ReportForm();
	}

	public static HashMap<String, String> forwards = new HashMap<String, String>();
	static {
		forwards.put("TPAtoControlreport", "redirect:/do/tools/controlReports/");
	}

	
    public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MMddyyyy");
    
    private static String DEFAULT_SORT = TPAVestingSubmissionReportData.SORT_CONTRACT_ID;
    private static String DEFAULT_SORT_DIRECTION = ReportSort.ASC_DIRECTION;
    
    public static final String FILE_LINE_BREAK_SEPARATOR = "|";
    public static final int FILE_LINE_BREAK_LENGTH = 100;
    public static final String STATUS_REMOVE_TEXT = "Click ‘V’ for details";
    public static final String DUMMY_DATE = "/8888";
    
    private static Environment env = Environment.getInstance();

	/**
	 * Constructor.
	 */
	public TPAVestingSubmissionReportController() {
		super(TPAVestingSubmissionReportController.class);
	}
     
	@RequestMapping(value = "/tpaVestingSubmissionReport/", method = { RequestMethod.GET })
	public String doDefault(@Valid @ModelAttribute("tpaVestingSubmissionReportForm") ReportForm actionForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("TPAtoControlreport");
			}else{
                return forwards.get("TPAtoControlreport"); // this is to show the form validation errors, when you don’t have any pen test errros
          }
		}
		String forward = super.doDefault(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
	@RequestMapping(value ="/tpaVestingSubmissionReport/",params={"task=download"}, method =  {RequestMethod.GET}) 
	public String doDownload(@Valid @ModelAttribute("tpaVestingSubmissionReportForm") ReportForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
				throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession()
					.getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");
			}
		}
		String forward = super.doDownload(actionForm, request, response);
		return StringUtils.contains(forward, '/') ? forward : forwards.get(forward);
	}
	
    /* (non-Javadoc)
     * @see com.manulife.pension.ps.web.report.ReportAction#populateReportCriteria(com.manulife.pension.service.report.valueobject.ReportCriteria, com.manulife.pension.ps.web.report.ReportForm, javax.servlet.http.HttpServletRequest)
     */
    protected void populateReportCriteria(ReportCriteria criteria,
    		BaseReportForm form, HttpServletRequest request)
            throws SystemException {
        
        List<BigDecimal> contractList = new ArrayList();
        boolean tpaPlanStaffAccess = false;
        // get the user profile object 
        UserProfile userProfile = getUserProfile(request);
        
        if (userProfile.getRole() instanceof InternalUser) {
            // if internal user coming from the Control Reports page, 
            // the only TPA Firm ID is available in the request
            String tpaFirmId = request.getParameter(TPAVestingSubmissionReportData.FILTER_TPA_FIRM);
            for (Integer contractId : (List<Integer>)TPAServiceDelegate.getInstance().getContractsByFirm(Integer.parseInt(tpaFirmId))) {
                contractList.add(new BigDecimal(contractId.intValue()));
            }
            tpaPlanStaffAccess = userProfile.getRole().hasPermission(PermissionType.TPA_STAFF_PLAN_ACCESS);
            
        } else if (userProfile.getRole() instanceof ThirdPartyAdministrator) {
            // add all contracts for logged in TPA
            UserInfo userInfo = SecurityServiceDelegate.getInstance()
            .getUserInfo(userProfile.getPrincipal());
            for (TPAFirmInfo tpaFirmInfo : userInfo.getTpaFirmsAsCollection()) {
                List<Integer> tempContractList = (List<Integer>)TPAServiceDelegate.getInstance().getContractsByFirm(tpaFirmInfo.getId());
                for (Integer contractId : tempContractList) {
                    contractList.add(new BigDecimal(contractId.intValue()));
                }
                tpaPlanStaffAccess = tpaFirmInfo.getContractPermission().isTpaStaffPlanAccess();
            } 
        }
                 
        criteria.addFilter(TPAVestingSubmissionReportData.FILTER_CONTRACT_LIST, contractList);
        criteria.addFilter(TPAVestingSubmissionReportData.FILTER_PLAN_STAFF_PERMISSION, tpaPlanStaffAccess);
        if (env.getDBSiteLocation().equals(Constants.DB_SITEMODE_USA)) {
            criteria.addFilter(TPAVestingSubmissionReportData.FILTER_PSW_SITE, GlobalConstants.MANULIFE_CONTRACT_ID_FOR_USA);
        } else {
            criteria.addFilter(TPAVestingSubmissionReportData.FILTER_PSW_SITE, GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY); 
        }
        
    }

    /* (non-Javadoc)
     * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSort()
     */
    protected String getDefaultSort() {
        return DEFAULT_SORT;
    }
    /* (non-Javadoc)
     * @see com.manulife.pension.ps.web.report.ReportAction#getDefaultSortDirection()
     */
    protected String getDefaultSortDirection() {
        return DEFAULT_SORT_DIRECTION;
    }
    
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportId()
	 */
	protected String getReportId() {
		return TPAVestingSubmissionReportData.REPORT_ID;
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getReportName()
	 */
	protected String getReportName() {
		return TPAVestingSubmissionReportData.REPORT_NAME;
	}
	

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getDownloadData(com.manulife.pension.ps.web.report.ReportForm, com.manulife.pension.service.report.valueobject.ReportData, javax.servlet.http.HttpServletRequest)
	 */
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report,
			HttpServletRequest request) {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateDownloadData");
		}

		StringBuffer buffer = new StringBuffer();
		TPAVestingSubmissionReportData reportData = (TPAVestingSubmissionReportData) report;
        String todayDate = DateRender.formatByPattern(Calendar.getInstance().getTime(), "", 
                RenderConstants.LONG_TIMESTAMP_MDY_SLASHED);
        
        // Disclaimer & comments
        Message disclaimerMessage = null;
        Message commentNoInfo = null;
        Message commentDateRange = null;
        Message commentOnlineUpdates = null;
        try {
            disclaimerMessage = (Message) ContentCacheManager.getInstance().getContentById(
                    ContentConstants.DOWNLOAD_REPORT_DISCLAIMER_FOR_TPA_REPORT, ContentTypeManager.instance().MESSAGE);
            commentNoInfo = (Message) ContentCacheManager.getInstance().getContentById(
                    ContentConstants.DOWNLOAD_REPORT_COMMENT_NO_INFO, ContentTypeManager.instance().MESSAGE);
            commentDateRange = (Message) ContentCacheManager.getInstance().getContentById(
                    ContentConstants.DOWNLOAD_REPORT_COMMENT_DATE_RANGE, ContentTypeManager.instance().MESSAGE);
            commentOnlineUpdates = (Message) ContentCacheManager.getInstance().getContentById(
                    ContentConstants.DOWNLOAD_REPORT_COMMENT_ONLINE_UPDATES, ContentTypeManager.instance().MESSAGE);
        } catch (ContentException ce) {
        } catch (NullPointerException ne) {
        }
        
        // Wrap the disclaimer text.
        final String disclaimerText = StringUtils.chomp(getTextFromContent(disclaimerMessage, null));
        final String disclaimerTextWithSeparator = WordUtils.wrap(disclaimerText,
                FILE_LINE_BREAK_LENGTH, FILE_LINE_BREAK_SEPARATOR, false);
        final String[] disclaimerLines = StringUtils.split(disclaimerTextWithSeparator,
                FILE_LINE_BREAK_SEPARATOR);
        for (String disclaimerLine : disclaimerLines) {
            buffer.append(escapeField(disclaimerLine)).append(LINE_BREAK);
        }
        buffer.append(LINE_BREAK);
        
        // Report name and date
        buffer.append("Report name:").append(COMMA); 
        buffer.append("Summary of Last Vesting File Submissions");
        buffer.append(LINE_BREAK);
        
        buffer.append("Report run date:").append(COMMA); 
        buffer.append(todayDate);
        buffer.append(LINE_BREAK);
        
        buffer.append(LINE_BREAK);

		// Fill in the header
		Iterator columnLabels = reportData.getColumnLabels().iterator();
		while (columnLabels.hasNext()) {
			buffer.append(columnLabels.next());
			if (columnLabels.hasNext()) {
				buffer.append(COMMA);
			}
		}

        if (reportData.getDetails() != null) {
    		Iterator items = reportData.getDetails().iterator();
    		while (items.hasNext()) {
                
                TPAVestingSubmissionDetailsVO tpaSubmissionDetail = (TPAVestingSubmissionDetailsVO) items.next();
                       
    			buffer.append(LINE_BREAK);         
                buffer.append(tpaSubmissionDetail.getTpaFirmId()).append(COMMA);
                buffer.append(escapeField(tpaSubmissionDetail.getTpaFirmName())).append(COMMA);
                buffer.append(tpaSubmissionDetail.getContractId()).append(COMMA);
                buffer.append(escapeField(tpaSubmissionDetail.getContractName())).append(COMMA);
                
                // plan year end (MM/DD)
                GregorianCalendar planYearEnd = new GregorianCalendar();
                planYearEnd.setTime(tpaSubmissionDetail.getPlanYearEnd());    
                buffer.append(" ").append(DateRender.formatByPattern(planYearEnd.getTime(), "", 
                        RenderConstants.MEDIUM_MDY_SLASHED).replaceAll(DUMMY_DATE, "")).append(COMMA);
    
                // last file submission date
                buffer.append(DateRender.formatByPattern(tpaSubmissionDetail.getLastSubmissionDate(), "",
                        RenderConstants.LONG_TIMESTAMP_MDY_SLASHED));
                buffer.append(COMMA);
                
                // last submission ID
                buffer.append(
                        ObjectUtils.defaultIfNull(tpaSubmissionDetail.getSubmissionId(),
                                StringUtils.EMPTY)).append(COMMA);
                
                // last file submission status
                buffer.append(getDisplayStatus(tpaSubmissionDetail));
                buffer.append(COMMA);
                
                // vesting service feature, only display if at least one vesting submission received
                if (tpaSubmissionDetail.getLastSubmissionDate() != null) {
                    if (StringUtils.equals(tpaSubmissionDetail.getVestingServiceFeature(), ServiceFeatureConstants.PROVIDED)) {
                        buffer.append("TPA Provided");
                    } else if (StringUtils.equals(tpaSubmissionDetail.getVestingServiceFeature(), ServiceFeatureConstants.CALCULATED)) {
                        buffer.append("JH Calculated");
                    }
                }
                buffer.append(COMMA);
                
                // earliest vesting eff date in submission
                buffer.append(DateRender.formatByPattern(tpaSubmissionDetail.getEarliestDate(), "",
                            RenderConstants.MEDIUM_MDY_SLASHED));
                buffer.append(COMMA);
                
                // last vesting eff date in submission
                buffer.append(DateRender.formatByPattern(tpaSubmissionDetail.getLatestDate(), "",
                            RenderConstants.MEDIUM_MDY_SLASHED));
                buffer.append(COMMA);
                
                // online update after file submission
                if (tpaSubmissionDetail.getOnlineUpdateDate() != null) {
                    buffer.append("Y");
                }
                buffer.append(COMMA);
                
                // comments
                String tempComment = "";
                if (tpaSubmissionDetail.getComments().indexOf("0") != -1) {
                    tempComment = getTextFromContent(commentNoInfo, null);
                } else {
                    if (tpaSubmissionDetail.getComments().indexOf("1") != -1) {
                        tempComment = getTextFromContent(commentDateRange, null);
                    }
                    if (tpaSubmissionDetail.getComments().indexOf("2") != -1) {
                        String contentParams [] = new String[2];
                        contentParams[0] = getChangedBy(tpaSubmissionDetail.getOnlineUserId(),getUserProfile(request));
                        contentParams[1] = DateRender.formatByPattern(tpaSubmissionDetail.getOnlineUpdateDate(), "",
                                RenderConstants.LONG_TIMESTAMP_MDY_SLASHED);
                        tempComment += getTextFromContent(commentOnlineUpdates, contentParams);
                    }
                }
                buffer.append(escapeField(tempComment));
                buffer.append(COMMA);
            }      
        }
        

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateDownloadData");
		}

		return buffer.toString().getBytes();
	}

	private StringBuffer appendBuffer(StringBuffer buffer, Object o) {
		if (o != null) {
			buffer.append(o);
		}
		return buffer;
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.report.ReportAction#getFileName(javax.servlet.http.HttpServletRequest)
	 */
    protected String getFileName(HttpServletRequest request) {
        // defaults to .csv extension
        String dateString = null;
        synchronized (DATE_FORMATTER) {
            dateString = DATE_FORMATTER.format(new Date());
        }
        
        return "Vesting_Submission_Report_for_" + dateString + CSV_EXTENSION;
    }
    
    private String getTextFromContent(Message msg, String[] params) {
        String result = "no content found";
        if (msg == null || StringUtils.isEmpty(msg.getText())) {
            return result;
        } else {
            return StringUtility.substituteParams(msg.getText(), (Object[])params);
        }
    }
    
    private String escapeField(String field) {
        if (field.indexOf(",") != -1) {
            StringBuffer newField = new StringBuffer();
            newField = newField.append("\"").append(field).append("\"");
            return newField.toString();
        } else {
            return field;
        }
    }

    /**
     * Generic method to display the changedBy info 
     * using logged in userProfile and the changedBy userId
     * 
     * @param userId
     * @param userProfile
     * @return
     */
    private String getChangedBy(String userId, UserProfile userProfile) {
        final String JHR = "John Hancock Representative";

        // Lookup the user who last changed the info.
        UserInfo userInfo = null;
        if (StringUtils.isNotEmpty(userId)) {
            try {
                userInfo = SecurityServiceDelegate.getInstance().searchByProfileId(null,
                Long.valueOf(userId.trim()));
            } catch (NumberFormatException numberFormatException) {
                // ignore the NumberFormatException
            } catch (SecurityServiceException securityServiceException) {
                throw new RuntimeException(securityServiceException);
            } catch (SystemException systemException) {
                throw new RuntimeException(systemException);
            }
        }  
        
        StringBuffer changedBy = new StringBuffer();

        if (userInfo != null && userInfo.getRole().isExternalUser()) {
            // changedBy is external user
            changedBy.append(userInfo.getFirstName()).append(" ").append(userInfo.getLastName());
        } else {
            // Either we don't have userInfo for changeby user OR changedBy user is an internal one
            // display its userId (if its not empty). If logged in user is an internal one then return
            // userId of changedBy user along with JHR
            if (userProfile.getRole().isInternalUser() && StringUtils.isNotEmpty(userId)) {
                changedBy.append(userId).append(", ");
            } 
            changedBy.append(JHR);
        }
        
        return changedBy.toString();
    }
    
    /**
     * Display status has to display same as on the Submission History page,
     * however the icon and any interactive message has to be removed
     * 
     * @param tpaItemVO
     * @return
     */
    private String getDisplayStatus(TPAVestingSubmissionDetailsVO tpaItemVO) {
        StringBuffer outStatus = new StringBuffer();
        
        // get display status
        SubmissionHistoryItem item = new SubmissionHistoryItem();
        item.setSystemStatus(tpaItemVO.getLastSubmissionStatus());
        item.setType(GFTUploadDetail.SUBMISSION_TYPE_VESTING);
        String tempStatus = SubmissionHistoryItemActionHelper.getInstance().getDisplayStatus(item);
        
        // remove icon and interactive message
        outStatus.append(tempStatus.substring(tempStatus.lastIndexOf(">") + 1).replaceAll(STATUS_REMOVE_TEXT, ""));
        
        return outStatus.toString();
    }
    
    /**
	 * This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#137697.
	 */
	 @Autowired
	   private PSValidatorFWTpa  psValidatorFWTpa;

	 @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWTpa);
	}
}
