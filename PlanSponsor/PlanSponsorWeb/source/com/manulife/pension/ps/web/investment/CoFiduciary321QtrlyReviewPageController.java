package com.manulife.pension.ps.web.investment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.ereports.ContractDocumentInfo;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.platform.web.investment.CoFiduciary321QtrlyReviewPageForm;
import com.manulife.pension.platform.web.investment.valueobject.CoFidContractDocuments;
import com.manulife.pension.platform.web.util.DataUtility;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.contract.ContractDocumentsHelper;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWInput;
import com.manulife.pension.service.contract.util.CoFidPlanReviewHelper;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;


/**
 * Action class for CoFiduciary321 quarterly review page details.
 * 
 * @author Sreenivasa Koppula
 *
 */

@Controller
@RequestMapping( value = "/investment")
@SessionAttributes({"coFiduciary321QtrlyReviewPageForm"})


public class CoFiduciary321QtrlyReviewPageController extends BaseAutoController {
	
	
	@ModelAttribute("coFiduciary321QtrlyReviewPageForm") 
	public  CoFiduciary321QtrlyReviewPageForm populateForm() 
	{
		return new  CoFiduciary321QtrlyReviewPageForm();
		}

	public static Map<String,String> forwards = new HashMap<>();
	static{
		forwards.put("input","/investment/cofid321QtrlyReview.jsp");
		forwards.put("default","/investment/cofid321QtrlyReview.jsp");
	}

	
	private static final String DEFAULT_ACTION = "default";	
	private static final String DOCUMENT_TYPE_COFID = "CFD";
	private static final String DOCUMENT_SUB_TYPE_321 = "321";
	private static final String DOCUMENT_SUB_TYPE_338 = "338";

	@RequestMapping(value ="/cofidQtrlyReview/",method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("coFiduciary321QtrlyReviewPageForm") CoFiduciary321QtrlyReviewPageForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		String forward = null;
		
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				forward="/do"+new UrlPathHelper().getPathWithinServletMapping(request);
				return "redirect:"+forward;
			}
		}		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		Contract contract = userProfile.getCurrentContract();
		
		if (userProfile.getCurrentContract() == null) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		
		// if service provider has past 24 months documents
		if (!userProfile.getCurrentContract().isServiceProviderHasPast24MonthsDocuments()) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		// contract selected CoFid in the last 24 months?
		boolean isCoFiduciary = ContractServiceDelegate.getInstance()
				.checkCoFidContractIndicator(
						userProfile.getCurrentContract().getContractNumber());
		/*if (!isCoFiduciary) { 
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}*/
		request.setAttribute("isCoFiduciary",
				isCoFiduciary);
		
		boolean isFundRecommendationAvailable = ContractServiceDelegate.getInstance()
				.checkFundReplacementRecommendationRecievedStatus(userProfile.getCurrentContract().getContractNumber());
				
		request.setAttribute("isFundRecommendationAvailable",
				isFundRecommendationAvailable);

	//CoFiduciary321QtrlyReviewPageForm coFiduciary321QtrlyReviewPageForm = (CoFiduciary321QtrlyReviewPageForm) actionForm;
		actionForm.resetData();
		
		
		List<CoFidContractDocuments> contractDocumentList = new ArrayList<CoFidContractDocuments>();
		 
		actionForm.setCurrentDate(new Date());
		
		// get the list of contract and amendment documents available
		// for this contract number.
		ContractDocumentInfo[] contractDocInfo = null;
		
		try {
			contractDocInfo = com.manulife.pension.platform.web.contract.ContractDocumentsHelper
					.getContractDocuments(contract);
			
		} catch (SystemException e) {
			List errors = new ArrayList();
			errors.add(new GenericException(
					ErrorCodes.REPORT_FILE_NOT_FOUND));
			setErrorsInRequest(request, errors);
			return forwards.get("input");
			
		}catch (ServiceUnavailableException e) {
			
			List errors = new ArrayList();
			errors.add(new GenericException(
					ErrorCodes.REPORT_SERVICE_UNAVAILABLE));
			setErrorsInRequest(request, errors);
			return forwards.get("input");
		}
		/*
		String dateStr = "01/31/2016";
		Date startDate = null;
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
		try {
			startDate = (Date)formatter.parse(dateStr);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List reports = new ArrayList();
		ContractDocumentInfo contractDocumentInfo1 = new ContractDocumentInfo();
		contractDocumentInfo1.setContractNumber("124665");
		contractDocumentInfo1.setDocumentCreatedDate(startDate);		
		contractDocumentInfo1.setDocumentType("CFD");
		contractDocumentInfo1.setDocumentSubType("321");
		contractDocumentInfo1.setPartyIdentifierCode("WILSHIRE");
		
		ContractDocumentInfo contractDocumentInfo2 = new ContractDocumentInfo();
		contractDocumentInfo2.setContractNumber("111833");
		contractDocumentInfo2.setDocumentCreatedDate(startDate);		
		contractDocumentInfo2.setDocumentType("CFD");
		contractDocumentInfo2.setDocumentSubType("338");
		contractDocumentInfo2.setPartyIdentifierCode("WILSHIRE");
		
		ContractDocumentInfo contractDocumentInfo3 = new ContractDocumentInfo();
		contractDocumentInfo3.setContractNumber("111998");
		contractDocumentInfo3.setDocumentCreatedDate(startDate);		
		contractDocumentInfo3.setDocumentType("CFD");
		contractDocumentInfo3.setDocumentSubType("321");
		contractDocumentInfo3.setPartyIdentifierCode("MSTAR");
		
		reports.add(contractDocumentInfo1);
		reports.add(contractDocumentInfo2);
		reports.add(contractDocumentInfo3);
		contractDocInfo=(ContractDocumentInfo[]) reports.toArray(new ContractDocumentInfo[0]);*/
		
			
		// populate the values into ContractDocuments (value
		// object)which will be used in Wilshire Adviser Reports page.
		if(contractDocInfo != null){
			
			for (int i = 0; i < contractDocInfo.length; i++) {
				
				if(contractDocInfo[i].getDocumentType() != null && contractDocInfo[i].getDocumentSubType() !=null
						&& (StringUtils.equalsIgnoreCase(contractDocInfo[i].getDocumentType().trim(), 
								DOCUMENT_TYPE_COFID)) && ((StringUtils.equalsIgnoreCase(contractDocInfo[i].getDocumentSubType().trim(), 
										DOCUMENT_SUB_TYPE_321) || (StringUtils.equalsIgnoreCase(contractDocInfo[i].getDocumentSubType().trim(), 
												DOCUMENT_SUB_TYPE_338))))){
					
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
								"Failed to build provider name ["
										+ contractDocInfo[i].getContractName() + "]");
					}

					coFidContractDocuments.setProviderDisplayName(providerName);
					coFidContractDocuments.setContractDocName(pdfFileName);
					coFidContractDocuments.
								setDocumentCreatedDate(contractDocInfo[i].getDocumentCreatedDate());
					
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
			actionForm.setCoFidContractDocList(contractDocumentList);
		}
		return forwards.get(DEFAULT_ACTION);
	}
		  @Autowired
	   private PSValidatorFWInput  psValidatorFWInput;

		  @InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(psValidatorFWInput);
	}
	
}
