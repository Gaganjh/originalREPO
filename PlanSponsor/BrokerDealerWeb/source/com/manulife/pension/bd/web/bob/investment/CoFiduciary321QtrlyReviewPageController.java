package com.manulife.pension.bd.web.bob.investment;

import static com.manulife.pension.platform.web.CommonErrorCodes.REPORT_FILE_NOT_FOUND;
import static com.manulife.pension.platform.web.CommonErrorCodes.REPORT_SERVICE_UNAVAILABLE;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.bob.contract.ContractDocumentsHelper;
import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWInput;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.ereports.ContractDocumentInfo;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.investment.CoFiduciary321QtrlyReviewPageForm;
import com.manulife.pension.platform.web.investment.valueobject.CoFidContractDocuments;
import com.manulife.pension.platform.web.util.DataUtility;
import com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.service.contract.util.CoFidPlanReviewHelper;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;
import com.manulife.util.render.RenderConstants;

/**
 * Action class for CoFiduciary321 quarterly review process page details.
 * 
 * @author Sreenivasa Koppula
 *
 */
@Controller
@RequestMapping(value ="/bob/investment")
@SessionAttributes({"coFiduciary321QtrlyReviewPageForm"})

public class CoFiduciary321QtrlyReviewPageController extends BaseAutoController {
	@ModelAttribute("coFiduciary321QtrlyReviewPageForm") 
	public CoFiduciary321QtrlyReviewPageForm populateForm() 
	{
		return new CoFiduciary321QtrlyReviewPageForm();
		
	}
	public static HashMap<String,String>forwards = new HashMap<String,String>();
	static {
		forwards.put("input", "/investment/cofid321QtrlyReview.jsp");
		forwards.put("default", "/investment/cofid321QtrlyReview.jsp");
		forwards.put("homePage", "redirect:/do/home/");
	}

	private static final String DEFAULT_ACTION = "default";
	SimpleDateFormat dateFormat = new SimpleDateFormat(RenderConstants.MEDIUM_YMD_DASHED);
	private static final String DOCUMENT_TYPE_COFID = "CFD";
	private static final String DOCUMENT_SUB_TYPE_321 = "321";
	private static final String DOCUMENT_SUB_TYPE_338 = "338";
	private static final String FRW_HOME_PAGE = "homePage";
	
	@RequestMapping(value ="/cofidQtrlyReview/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("coFiduciary321QtrlyReviewPageForm") CoFiduciary321QtrlyReviewPageForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get(errDirect) != null ? forwards.get(errDirect) : forwards.get("input");

			}
		}
		Contract contract = getBobContext(request).getCurrentContract();
		
		if (getBobContext(request).getCurrentContract() == null) {
			return forwards.get(FRW_HOME_PAGE);
		}
		
		if (!contract.isServiceProviderHasPast24MonthsDocuments()) {
			return FRW_HOME_PAGE;
		}
		
		// contract selected CoFid in the last 24 months?
		/*boolean isCoFiduciary = ContractServiceDelegate.getInstance()
				.checkCoFidContractIndicator(contract.getContractNumber());
		
		if (!isCoFiduciary) {
			return forwards.get(FRW_HOME_PAGE);
		}*/

		CoFiduciary321QtrlyReviewPageForm coFiduciary321QtrlyReviewPageForm = 
															(CoFiduciary321QtrlyReviewPageForm)actionForm;
		
		// Reset the form before loading the data
		coFiduciary321QtrlyReviewPageForm.resetData();
		
		coFiduciary321QtrlyReviewPageForm.setCurrentDate(new Date());
		List<CoFidContractDocuments>  contractDocumentList = new ArrayList<CoFidContractDocuments>();
		ContractDocumentInfo[] contractDocInfo = null;
		
		try {
			// get the list of contract documents available for this contract
	        // number.
			contractDocInfo = com.manulife.pension.platform.web.contract.ContractDocumentsHelper
					.getContractDocuments(contract);

		} catch (SystemException e) {
		    List<GenericException> errors = new ArrayList<GenericException>();
			errors.add(new GenericException(
					REPORT_FILE_NOT_FOUND));
			setErrorsInRequest(request, errors);
			return forwards.get("input");
			
		} catch (ServiceUnavailableException e) {
			List<GenericException> errors = new ArrayList<GenericException>();
			errors.add(new GenericException(
					REPORT_SERVICE_UNAVAILABLE));
			setErrorsInRequest(request, errors);
			return forwards.get("input");
		}
		
		/*
		String dateStr = "01/31/2016";
		Date displayDate = null;
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
		try {
			displayDate = (Date)formatter.parse(dateStr);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		coFiduciary321QtrlyReviewPageForm.setCurrentDate(new Date());
		
		List reports = new ArrayList();
		ContractDocumentInfo contractDocumentInfo1 = new ContractDocumentInfo();
		contractDocumentInfo1.setContractNumber("96167");
		contractDocumentInfo1.setDocumentCreatedDate(displayDate);		
		contractDocumentInfo1.setDocumentType("CFD");
		contractDocumentInfo1.setDocumentSubType("321");
		contractDocumentInfo1.setPartyIdentifierCode("WILSHIRE");
		
		ContractDocumentInfo contractDocumentInfo2 = new ContractDocumentInfo();
		contractDocumentInfo2.setContractNumber("111833");
		contractDocumentInfo2.setDocumentCreatedDate(displayDate);		
		contractDocumentInfo2.setDocumentType("CFD");
		contractDocumentInfo2.setDocumentSubType("338");
		contractDocumentInfo2.setPartyIdentifierCode("WILSHIRE");
		
		ContractDocumentInfo contractDocumentInfo3 = new ContractDocumentInfo();
		contractDocumentInfo3.setContractNumber("111998");
		contractDocumentInfo3.setDocumentCreatedDate(displayDate);		
		contractDocumentInfo3.setDocumentType("CFD");
		contractDocumentInfo3.setDocumentSubType("321");
		contractDocumentInfo3.setPartyIdentifierCode("MSTAR");
			
		reports.add(contractDocumentInfo1);
		reports.add(contractDocumentInfo2);
		reports.add(contractDocumentInfo3);
		contractDocInfo=(ContractDocumentInfo[]) reports.toArray(new ContractDocumentInfo[0]);*/
		// populate the values into ContractDocuments (value
		// object)which will be used in Wilshire Adviser Reports page.
		if(contractDocInfo != null && contractDocInfo.length > 0){
			
			for (int i = 0; i < contractDocInfo.length; i++) {
				
				if(contractDocInfo[i].getDocumentType() != null  && contractDocInfo[i].getDocumentSubType() !=null
						&& StringUtils.equalsIgnoreCase(contractDocInfo[i].getDocumentType(), 
								DOCUMENT_TYPE_COFID) && ((StringUtils.equalsIgnoreCase(contractDocInfo[i].getDocumentSubType().trim(), 
										DOCUMENT_SUB_TYPE_321) || (StringUtils.equalsIgnoreCase(contractDocInfo[i].getDocumentSubType().trim(), 
												DOCUMENT_SUB_TYPE_338)))) ){
					
					CoFidContractDocuments coFidContractDocuments = new CoFidContractDocuments();
					String providerName = null;
					String pdfFileName = null;
					try {
						providerName = CoFidPlanReviewHelper
								.getInstance()
								.getCoFidLookUpDescription(
										contractDocInfo[i]
												.getPartyIdentifierCode().trim(),
										contractDocInfo[i].getDocumentSubType().trim());
						pdfFileName = ContractDocumentsHelper
								.getPdfReportFileName(contractDocInfo[i]);
					} catch (Exception e) {
						throw new SystemException(e, this.getClass().getName(),
								"buildContractDocumentsList",
								"Failed to build PDF: Contract Name ["
										+ contractDocInfo[i].getContractName() + "]");
					}
				
					coFidContractDocuments.setProviderDisplayName(providerName);
					coFidContractDocuments.setContractDocName(pdfFileName);
					coFidContractDocuments.
								setDocumentCreatedDate(contractDocInfo[i].getDocumentCreatedDate());
					
					Date startDate = gerQuarterStarDate(contractDocInfo[i].getDocumentCreatedDate());
					
					coFidContractDocuments.setQuarterStartDate(startDate);
					
					coFidContractDocuments
					.setDocumentCurrentQuarter(DataUtility.yearOfCurrentQuarter(contractDocInfo[i]
							.getDocumentCreatedDate()));
					
					// validating if Effective Date (year and month) of the
					// report is > than or equals to the year and month of
					// (current date - 24 moths)
					if (contractDocInfo[i].getDocumentCreatedDate().after(
							DataUtility.currentDateMinus24Months())
							|| contract
									.getContractDates()
									.getAsOfDate()
									.equals(
											DataUtility
													.currentDateMinus24Months())) {
						contractDocumentList.add(coFidContractDocuments);
					}
				}
			}
			coFiduciary321QtrlyReviewPageForm.setCoFidContractDocList(contractDocumentList);
		} 
		
		// If CoFid contracts are not available then display the information message.
		if(contractDocumentList.isEmpty()) {
			
			ArrayList<GenericException> coFidInfoMessages = new ArrayList<GenericException>();
			
			GenericExceptionWithContentType exception = new GenericExceptionWithContentType(
                    BDContentConstants.NO_COFID321_REPORTS_FOUND,
                    ContentTypeManager.instance().MISCELLANEOUS,false);
			coFidInfoMessages.add(exception);
			
			setMessagesInRequest(request, coFidInfoMessages,
                    BDConstants.INFO_MESSAGES);
			
		}
			
		return forwards.get(DEFAULT_ACTION);
	}
	
	private Date gerQuarterStarDate(Date documentCreatedDate) {
		Calendar c = Calendar.getInstance(); 
		c.setTime(documentCreatedDate); 
		c.add(Calendar.MONTH, -2);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	public static BobContext getBobContext(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return session == null ? null : (BobContext) session
				.getAttribute(BDConstants.BOBCONTEXT_KEY);
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
	private static void setMessagesInRequest(final HttpServletRequest request,
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
	 * This code has been changed and added  to 
	 * Validate form and request against penetration attack, prior to other validations as part of the CL#136970.
	 */
	@Autowired
	private BDValidatorFWInput bdValidatorFWInput;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(bdValidatorFWInput);
	}
	
}
