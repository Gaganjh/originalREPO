package com.manulife.pension.bd.web.bob.investment;

import static com.manulife.pension.platform.web.CommonErrorCodes.REPORT_SERVICE_UNAVAILABLE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BdBaseController;
import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.contract.ContractDocumentsHelper;
import com.manulife.pension.platform.web.controller.BaseAutoController;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.service.contract.valueobject.CoFiduciaryVO;
import com.manulife.pension.service.contract.valueobject.CofidFundRecommendVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;


@Controller
@RequestMapping( value = "/bob/investment")
@SessionAttributes({"cofiduciaryReviewScreenPageForm"})

public class CofiduciaryReviewReportController extends BaseAutoController {
	@ModelAttribute("cofiduciaryReviewScreenPageForm") 
	public CofiduciaryReviewScreenPageForm populateForm()
	{
		return new CofiduciaryReviewScreenPageForm();
	}
	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("input","/investment/cofiduciaryReviewScreen.jsp");
		forwards.put("default","/investment/cofiduciaryReviewScreen.jsp");
		forwards.put("save","/investment/cofiduciaryReviewScreen.jsp");
		forwards.put("homePage","redirect:/do/home/");
	}

	private static final String FRW_HOME_PAGE = "homePage";

	@RequestMapping(value ="/coFiduciaryFundRecommendationReview/",  method =  {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("cofiduciaryReviewScreenPageForm") CofiduciaryReviewScreenPageForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");
			}
		}
		CofiduciaryReviewScreenPageForm cofiduciaryReviewScreenPageForm = (CofiduciaryReviewScreenPageForm) actionForm;

		Contract contract = getBobContext(request).getCurrentContract();

		if (getBobContext(request).getCurrentContract() == null) {
			return forwards.get(FRW_HOME_PAGE);
		}

		BobContext bob = getBobContext(request);
		BDUserProfile userProfile = bob.getUserProfile();

		String profileId = null;
		if (userProfile != null) {
			profileId = String.valueOf(userProfile.getBDPrincipal()
					.getProfileId());
		}else{
			forwards.get(FRW_HOME_PAGE);
		}

		List<CofidFundRecommendVO> cofidFundRecommendDetails = ContractServiceDelegate
				.getInstance().getCoFiduciaryFundDetails(
						contract.getContractNumber());
		if (cofidFundRecommendDetails != null) {

			for (CofidFundRecommendVO value : cofidFundRecommendDetails) {
				cofiduciaryReviewScreenPageForm
						.setCofidFundRecommendDetails(cofidFundRecommendDetails);
				cofiduciaryReviewScreenPageForm.setCreatedTS(value
						.getCreatedTS());
				cofiduciaryReviewScreenPageForm.setScheduledDate(value
						.getScheduledDate());
				value.setOptOutIndicatorTemp(value.getOptOutIndicator());
			}
		}

		boolean coFidRecommendation = getContractServiceDelegate()
				.checkFundReplacementRecommendationRecievedStatus(
						contract.getContractNumber());

		if (!coFidRecommendation) {
			return forwards.get(FRW_HOME_PAGE);
		}

		boolean checkBrokerInvestmentProfile = ContractServiceDelegate
				.getInstance().checkBrokerInvestmentProfile(
						contract.getContractNumber(),profileId);

		CoFiduciaryVO coFiduciaryVO = getContractServiceDelegate()
				.getCoFiduciaryVO(contract.getContractNumber());

		boolean isCofiduciary321 = false;
		if (null != coFiduciaryVO) {
			isCofiduciary321 = coFiduciaryVO.isCoFiduciary321Selected();

		}

		if (checkBrokerInvestmentProfile && isCofiduciary321) {
			cofiduciaryReviewScreenPageForm.setEditable(true);
		}

		Date cofidMontEndDate = cofiduciaryReviewScreenPageForm.getCofidMontEndDate();

		if (cofidMontEndDate == null) {

			cofidMontEndDate = getContractServiceDelegate()
					.getCoFidLastQuarterEndDate();

			cofiduciaryReviewScreenPageForm.setCofidMontEndDate(cofidMontEndDate);
		}
		
		if (contract.isParticipantNoticeAvailable() == null) {

			contract.setParticipantNoticeAvailable(ContractDocumentsHelper.getInstance()
					.isParticipantNoticeDocumentAvailable(
							contract.getContractNumber(),
							cofidMontEndDate, request));
		}
		
		return forwards.get("default");

	}


	@RequestMapping(value ="/coFiduciaryFundRecommendationReview/" ,params={"action=save"}   , method =  {RequestMethod.POST}) 
	public String doSave (@Valid @ModelAttribute("cofiduciaryReviewScreenPageForm") CofiduciaryReviewScreenPageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");// if input forward not
												// //available, provided default
			}
		}
		Collection errormsg = doValidate(form, request);
		if(errormsg.size()>0){
			request.removeAttribute(BdBaseController.ERROR_KEY);
			request.setAttribute(BdBaseController.ERROR_KEY, errormsg);
			return forwards.get("input");
		}
		
		BobContext bob = getBobContext(request);
		BDUserProfile userProfile = bob.getUserProfile();

		String profileID = null;
		if (userProfile != null) {
			profileID = String.valueOf(userProfile.getBDPrincipal()
					.getProfileId());
		}else{
			forwards.get(FRW_HOME_PAGE);
		}

		if (bob.getCurrentContract() == null) {
			return forwards.get(FRW_HOME_PAGE);
		}
		
		int contractId = bob.getCurrentContract().getContractNumber();
		
		if (form.getCofidFundRecommendDetails() != null) {

			for (CofidFundRecommendVO cofidFundRecommendVo : form
					.getCofidFundRecommendDetails()) {
					
				getContractServiceDelegate().saveSelectedFundDetails(contractId,profileID,cofidFundRecommendVo);
				cofidFundRecommendVo.setOptOutIndicatorTemp(cofidFundRecommendVo.getOptOutIndicator());
			}
		}

		List<CofidFundRecommendVO> cofidFundRecommendDetails = getContractServiceDelegate().getCoFiduciaryFundDetails(contractId);
		form.setDirty("false");
		form
				.setCofidFundRecommendDetails(cofidFundRecommendDetails);

		return forwards.get("save");

	}

	@RequestMapping(value ="/coFiduciaryFundRecommendationReview/", params={"action=downloadNoticePdf"} , method =  {RequestMethod.GET}) 
	public String doDownloadNoticePdf (@Valid @ModelAttribute("cofiduciaryReviewScreenPageForm") CofiduciaryReviewScreenPageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {

		
		List errors = new ArrayList();

		try {
			// get the user profile object and set the current contract to null
			Contract contract = getBobContext(request).getCurrentContract();

			if (contract == null) {
				return forwards.get(FRW_HOME_PAGE);
			}

			// Method to populate the quarterly PDF document for selected
			// contract.
			ContractDocumentsHelper.getInstance().getNoticePdfContractDocument(
					request, response, contract);

		} catch (ServiceUnavailableException e) {
			errors.add(new GenericException(REPORT_SERVICE_UNAVAILABLE));
		}

		if (!errors.isEmpty()) {
			setErrorsInRequest(request, errors);
			return forwards.get(BDConstants.SECONDARY_WINDOW_ERROR_FORWARD);
		} else {
			return null;
		}
	}
	@SuppressWarnings("rawtypes")
	protected Collection doValidate( ActionForm form,
			HttpServletRequest request) {

		Collection penErrors = new ArrayList();
		CofiduciaryReviewScreenPageForm cofiduciaryReviewScreenPageForm = (CofiduciaryReviewScreenPageForm) form;
		
		if (cofiduciaryReviewScreenPageForm !=null && cofiduciaryReviewScreenPageForm.getCofidFundRecommendDetails() != null) {

			for (CofidFundRecommendVO cofidFundRecommendVo : cofiduciaryReviewScreenPageForm
					.getCofidFundRecommendDetails()) {
					
				String value = cofidFundRecommendVo.getOptOutIndicator();
				if (!(ContractDocumentsHelper.OPT_OUT_IND_YES.equalsIgnoreCase(value.trim()) ||  ContractDocumentsHelper.OPT_OUT_IND_NO.equalsIgnoreCase(value.trim()))){
					//someother value is coming in request that needs to be cleaned up
						cofidFundRecommendVo.setOptOutIndicator(cofidFundRecommendVo.getOptOutIndicatorTemp());
						GenericException pse = new GenericException(CommonErrorCodes.ERROR_PSVALIDATION_WITH_GUI_FIELD_NAME, new Object[] { "Actions Selected"});
						if(null!=penErrors)
						penErrors.add(pse);

					}

			}

		}

			return penErrors;
	}
	public ContractServiceDelegate getContractServiceDelegate(){
		return ContractServiceDelegate.getInstance();
	}
	
	public BobContext getBobContext(HttpServletRequest request) {
		return BDSessionHelper.getBobContext(request);
	}

}
