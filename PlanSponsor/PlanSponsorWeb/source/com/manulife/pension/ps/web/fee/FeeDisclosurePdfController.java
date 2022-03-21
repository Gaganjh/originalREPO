package com.manulife.pension.ps.web.fee;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.manulife.pension.documents.model.DocumentFileOutput;
import com.manulife.pension.documents.model.FeeDisclosureDocumentInfo;
import com.manulife.pension.event.FeeDisclosureViewedReportEvent;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.delegate.EReportsServiceDelegate;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.DynaForm;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.order.delegate.OrderServiceDelegate;
import com.manulife.pension.util.content.GenericException;

/**
 * This class is used to read the generated fee disclosure pdf
 * and to show it to the user 
 * 
 * @author Eswar
 * 
 */
@Controller
public class FeeDisclosurePdfController extends PsController {
	
	private static final String EMPTY_LAYOUT_ID = "/registration/authentication.jsp";
	private static final DateFormat dateFormatter =  new SimpleDateFormat("yyyy-MM-dd");
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("secondaryWindowError","/WEB-INF/global/secondaryWindowError.jsp");
	}
	
	 
	
	public FeeDisclosurePdfController() {
		super(FeeDisclosurePdfController.class);
	}
   	public String doExecute(@Valid @ModelAttribute("dynaForm") DynaForm withdrawalForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
   	throws IOException,ServletException, SystemException {
	

		
		if (logger.isDebugEnabled()) {
			logger.debug(" entered FeeDisclosurePdfAction.doDefault");
		}
		
		List<GenericException> errors = new ArrayList<GenericException>();
		int contractNumber = 0;
		
		try {
			
			UserProfile userProfile = getUserProfile(request);
			Contract currentContract = userProfile.getCurrentContract();
			contractNumber = currentContract.getContractNumber();
			
			FeeDisclosureDocumentInfo documentInfo = OrderServiceDelegate
					.getInstance().getInforceFeeDisclosurePdfDetails(contractNumber, 0);
			
			if(	documentInfo == null) {
				throw new SystemException("Fee Inforce PDF not generated for contract # " + contractNumber);
			}
			
			EReportsServiceDelegate delegate = EReportsServiceDelegate.getInstance();
			DocumentFileOutput fileOutput = delegate.getDocument(documentInfo);
			
			response.setContentType(CommonConstants.MIME_TYPE_PDF);
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
            response.setHeader("Pragma", "no-cache");
			response.setContentLength(fileOutput.getLength());
			OutputStream out = response.getOutputStream();
			out.write(fileOutput.getReportFragment());
			out.flush();
			out.close();
			
			// trigger event , the user is about to view the report
			triggerDisclosureViewedEvent(userProfile, contractNumber, documentInfo);
			
		} catch (Exception e) {
			errors.add(new GenericException(ErrorCodes.REPORT_FILE_NOT_FOUND));
			setErrorsInRequest(request, errors);
			request.setAttribute(Constants.LAYOUT_BEAN, LayoutBeanRepository
					.getInstance().getPageBean(EMPTY_LAYOUT_ID));
			return forwards.get(Constants.SECONDARY_WINDOW_ERROR_FORWARD);
		} 
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit FeeDisclosurePdfAction.doDefault");
		}
		
		return null;
	}
	
	/*
	 * @param userProfile
	 * @param contractNumber
	 * @param documentInfo
	 * @throws SystemException
	 */
	private void triggerDisclosureViewedEvent(UserProfile userProfile, int contractNumber, 
			FeeDisclosureDocumentInfo documentInfo)	throws SystemException {

		long userProfileId = userProfile.getPrincipal().getProfileId();
		
		String reportType = documentInfo.getPdfType();
		
		String effectiveDate = documentInfo.getDocumentEffectiveDate();
		Date reportAsOfDate;
		try {
			synchronized(effectiveDate){
				reportAsOfDate = dateFormatter.parse(effectiveDate);
			}
		} catch (ParseException e) {
			throw new SystemException("Report Effective date invalid for contract  # " + contractNumber 
					+ ". Report Effective Date is " + effectiveDate);
		}

		OrderServiceDelegate orderService = OrderServiceDelegate.getInstance();

		// if the user is a recipient for the report
		// and no other user has viewed the report yet, trigger the event
		if (orderService.isUserARecipientForFeeDisclsoureReport(contractNumber, userProfileId, 
				reportAsOfDate, reportType)	&& !orderService.isDisclosureViewedByUser(contractNumber, 
				reportType, reportAsOfDate)) {
			FeeDisclosureViewedReportEvent feeDisclosureViewedReportEvent = new FeeDisclosureViewedReportEvent();
			feeDisclosureViewedReportEvent.setContractId(contractNumber);
			feeDisclosureViewedReportEvent.setProfileId(userProfileId);
			feeDisclosureViewedReportEvent.setAsOfDate(reportAsOfDate.getTime());
			feeDisclosureViewedReportEvent.setBatchType(reportType);
			feeDisclosureViewedReportEvent.setInitiator(userProfileId);
			EventClientUtility.getInstance("PS").prepareAndSendJMSMessage(feeDisclosureViewedReportEvent);
		}
	}
}