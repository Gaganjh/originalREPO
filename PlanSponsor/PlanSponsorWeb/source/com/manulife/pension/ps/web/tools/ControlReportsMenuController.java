package com.manulife.pension.ps.web.tools;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.fee.valueobject.ClassType;
import com.manulife.pension.service.order.acknowledgement.FeeBatchProcessProperties;
import com.manulife.pension.service.order.delegate.OrderServiceDelegate;
import com.manulife.pension.service.order.domain.OrderServiceConstants;
import com.manulife.pension.service.order.valueobject.Batch;

/**
 * @author marcest
 *
 * Create on Feb 6, 2007
 */
@Controller
@RequestMapping(value ="/tools")

public class ControlReportsMenuController extends PsController {
	@ModelAttribute("controlReportsForm")
	public ControlReportsMenuForm populateForm() 
	{
		return new ControlReportsMenuForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static {
		forwards.put("input", "/tools/controlReports.jsp");
		forwards.put("controlReports", "/tools/controlReports.jsp");
	}

	private static final String REPORTSMENU = "controlReports";
	private static final String REVIEW_REPORT = "reviewReport";
	private static final String TASK_KEY = "task";
	private static final String FD_REVIEW_REPORT_RECIPIENTS = FeeBatchProcessProperties
		.get(FeeBatchProcessProperties.FD_REVIEW_REPORT_RECIPIENTS);
	private static final String GIFL_REVIEW_REPORT_RECIPIENTS = FeeBatchProcessProperties
	.get(FeeBatchProcessProperties.GIFL_REVIEW_REPORT_RECIPIENTS);
	
	public ControlReportsMenuController() {
		super(ControlReportsMenuController.class);
	}


	@RequestMapping(value ="/controlReports/",  method =  {RequestMethod.GET}) 
	public String doExecute(@Valid @ModelAttribute("controlReportsForm") ControlReportsMenuForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		if(bindingResult.hasErrors()){
			String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if(errDirect!=null){
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
			}
		} 

		// Inforce Fee Disclosure review report request generation. Based on task and permission
		// it creates report request
		String task = request.getParameter(TASK_KEY);
		UserProfile userProfile = getUserProfile(request);
		String userName = userProfile.getAbstractPrincipal().getUserName();
		boolean hasFDReviewReportPermission = hasFDReviewReportPermission(userName);
		
		if(hasFDReviewReportPermission) {
			
			
			request.setAttribute("hasFDReviewReportPermission", hasFDReviewReportPermission);
			
			if(StringUtils.equals(REVIEW_REPORT, task)) {
				String selectedDate = actionForm.getSelectedAsOfDate();
				Calendar requestDate = Calendar.getInstance();
				if(StringUtils.equals(selectedDate, "Current")) {
					requestDate.set(9999, 11, 31);
				} else {
					requestDate.setTimeInMillis(Long.valueOf(selectedDate));
				}
				OrderServiceDelegate.getInstance().createFDReviewReportOrder(createBatch(new java.sql.Date(requestDate.getTimeInMillis())));
			}
			
			Calendar minDate = Calendar.getInstance();
			// need to show dates greater than June 29, 201
			minDate.set(2011, 5, 29);
			List<Date> inforceReviewdates = FeeServiceDelegate.getInstance("PS").selectFeeEffectiveDates(minDate.getTime());
			actionForm.setInforceDisclosureReviewDates(inforceReviewdates);


		} 
		
		boolean hasGIFLReviewReportPermission = StringUtils.containsIgnoreCase(
				GIFL_REVIEW_REPORT_RECIPIENTS, userName);
		
		if (hasGIFLReviewReportPermission) {
			Date asOfDate = new Date();
			Map<String, String> giflVersionNames = new TreeMap<String, String>();

			request.setAttribute("hasGIFLReviewReportPermission",hasGIFLReviewReportPermission);
			String companyId = CommonEnvironment.getInstance().getSiteLocation();
			boolean  isNy = StringUtils.equalsIgnoreCase(companyId, Constants.SITEMODE_USA) ? false : true;
			List<String> giflVersions = FeeServiceDelegate.getInstance(
					Environment.getInstance().getAppId())
					.getGIFLVersionsForEffectiveDate(asOfDate, isNy);

			
			if(giflVersions != null && giflVersions.size() > 1){
				giflVersionNames.put(StringUtils.EMPTY, StringUtils.EMPTY);
				for(String giflVersion : giflVersions){
					String versionName = getGIFLVersionName(giflVersion);
					giflVersionNames.put(giflVersion, versionName);
				}
			} else if (giflVersions != null && !giflVersions.isEmpty()) {
				String giflCode = giflVersions.get(0);
				giflVersionNames.put(giflCode, getGIFLVersionName(giflCode));
			} 
			actionForm.setGiflVersionNames(giflVersionNames);


			List<ClassType> classTyes = FeeServiceDelegate.getInstance(Environment.getInstance().getAppId())
					.getClassTypes(asOfDate);

			actionForm.setClassTypes(classTyes);
		}
		
        // if TPA vesting submission report section is available
        // retrieve all possible TPA Firms for validation
        if (userProfile.isInternalUser() && userProfile.isAllowedSubmitUpdateVesting()) {
            List tpaFirms = TPAServiceDelegate.getInstance().getTpaFirmIds();
            request.setAttribute("tpaFirms", tpaFirms);
        }

		return forwards.get(REPORTSMENU);
	}
	
	
	/*
	 * Create a batch value object
	 * @return Batch
	 */
	private Batch createBatch(Date date) {
		Batch batch = new Batch();
		batch.setBatchTypeCode(OrderServiceConstants.FULL_DISCLOSURE_REVIEW_REPORTING);
		batch.setBatchRunDate(date);
		batch.setBatchStatusCode(OrderServiceConstants.FeeDisclosureBatchStatus.Initiated.getCode());
		batch.setCreatedUserId(OrderServiceConstants.BATCH_CREATED_USERID);
		batch.setCreatedUserIdType(OrderServiceConstants.BATCH_CREATED_USERTYPE);
		return batch;
	}
	/*
	 * Checks the user has permission to create request for Inforce disclosure review report
	 * @return true if the user has permission
	 */
	private boolean hasFDReviewReportPermission(String userName) {
		return StringUtils.containsIgnoreCase(FD_REVIEW_REPORT_RECIPIENTS, userName);
	}
	
	/**
	 * This is used to short name of GIFL Version
	 * 
	 * @param giflCode
	 * 
	 * @return giflShortName
	 * 
	 * @throws SystemException
	 */
	protected String getGIFLVersionName(String giflCode) throws SystemException {

		Map<String, String> versionsMap = EnvironmentServiceDelegate
				.getInstance(Environment.getInstance().getAppId())
				.getGIFLVersions();
		String giflShortName = null;
		if (versionsMap != null && !versionsMap.isEmpty()) {
			giflShortName = versionsMap.get(giflCode);
		}

		// if the returned giflShortName is null, return giflCode
		if (giflShortName == null) {
			giflShortName = giflCode;
		}

		return giflShortName;
	}

	/**This code has been changed and added  to 
   	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
   	 */
	@Autowired
	private PSValidatorFWInput psValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWInput);
	}
}