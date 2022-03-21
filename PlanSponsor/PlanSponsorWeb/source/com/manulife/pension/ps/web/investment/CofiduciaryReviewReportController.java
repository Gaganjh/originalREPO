package com.manulife.pension.ps.web.investment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.platform.web.contract.ContractDocumentsHelper;
import com.manulife.pension.ps.service.delegate.ServiceUnavailableException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.controller.PsAutoController;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.validation.pentest.PSValidatorFWDefault;
import com.manulife.pension.service.contract.valueobject.CoFiduciaryVO;
import com.manulife.pension.service.contract.valueobject.CofidFundRecommendVO;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.content.GenericException;


@Controller
@RequestMapping( value = "/investment")
@SessionAttributes({"cofiduciaryReviewScreenPageForm"})

public class CofiduciaryReviewReportController extends PsAutoController {

	@ModelAttribute("cofiduciaryReviewScreenPageForm") 
	public CofiduciaryReviewScreenPageForm populateForm()
	{
		return new CofiduciaryReviewScreenPageForm();
		}

	public static Map<String,String> forwards = new HashMap<>();
	static{
		forwards.put("input","/investment/cofiduciaryReviewScreen.jsp");
		forwards.put("default","/investment/cofiduciaryReviewScreen.jsp");
		forwards.put("save","/investment/cofiduciaryReviewScreen.jsp" );
		forwards.put("secondaryWindowError","/WEB-INF/global/secondaryWindowError.jsp");
		}

	private static final String EMPTY_LAYOUT_ID = "/registration/authentication.jsp";
	
	@RequestMapping(value ="/coFiduciaryFundRecommendationReview/",  method = {RequestMethod.GET}) 
	public String doDefault(@Valid @ModelAttribute("cofiduciaryReviewScreenPageForm") CofiduciaryReviewScreenPageForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if (bindingResult.hasErrors()) {
			String errDirect = (String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
			if (errDirect != null) {
				request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
				return forwards.get("input");
			}
		}
		
		UserProfile userProfile = getUserProfileOverride(request);
		Contract currentContract = userProfile.getCurrentContract();

		if (currentContract == null) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}

		List<CofidFundRecommendVO> cofidFundRecommendDetails = getContractServiceDelegate().getCoFiduciaryFundDetails(
						userProfile.getCurrentContract().getContractNumber());
		if (cofidFundRecommendDetails != null) {
			for (CofidFundRecommendVO value : cofidFundRecommendDetails) {
				actionForm
						.setCofidFundRecommendDetails(cofidFundRecommendDetails);
				actionForm.setCreatedTS(value
						.getCreatedTS());
				actionForm.setScheduledDate(value
						.getScheduledDate());
				value.setOptOutIndicatorTemp(value.getOptOutIndicator());
			}
		}

		boolean isAutoexecuteOnAndFundRecommendationAvailable = getContractServiceDelegate().checkFundRecommendationFileAndAutoExecuteStatus(
						userProfile.getCurrentContract().getContractNumber());

		if (!isAutoexecuteOnAndFundRecommendationAvailable) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		CoFiduciaryVO coFiduciaryVO = getContractServiceDelegate()
				.getCoFiduciaryVO(
						userProfile.getCurrentContract().getContractNumber());

		boolean isCofiduciary321 = false;
		if (null != coFiduciaryVO) {
			isCofiduciary321 = coFiduciaryVO.isCoFiduciary321Selected();

		}

		if (isCofiduciary321 && getUserProfile(request).isTrustee()) {

			actionForm.setEditable(true);

		}
		

		Date cofidMontEndDate = actionForm.getCofidMontEndDate();

		if (cofidMontEndDate == null) {

			cofidMontEndDate = getContractServiceDelegate()
					.getCoFidLastQuarterEndDate();

			actionForm.setCofidMontEndDate(cofidMontEndDate);
		}
		
		if (currentContract.isParticipantNoticeAvailable() == null) {

			currentContract.setParticipantNoticeAvailable(ContractDocumentsHelper.getInstance()
					.isParticipantNoticeDocumentAvailable(
							currentContract.getContractNumber(),
							cofidMontEndDate, request));
		}

		return forwards.get("default");
	}

	@RequestMapping(value ="/coFiduciaryFundRecommendationReview/" ,params={"action=save"} , method =  {RequestMethod.POST}) 
	public String doSave (@Valid @ModelAttribute("cofiduciaryReviewScreenPageForm") CofiduciaryReviewScreenPageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
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
			request.removeAttribute(PsBaseUtil.ERROR_KEY);
			request.setAttribute(PsBaseUtil.ERROR_KEY, errormsg);
			return forwards.get("input");
		}
		UserProfile userProfile = getUserProfileOverride(request);
		String profileID ;
		if (userProfile != null) {
			profileID = String.valueOf(userProfile.getPrincipal()
					.getProfileId());
		}else{
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}
		Contract currentContract = userProfile.getCurrentContract();

		if (currentContract == null) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}

		if (form.getCofidFundRecommendDetails() != null) {
			for (CofidFundRecommendVO cofidFundRecommendVo : form
					.getCofidFundRecommendDetails()) {
				getContractServiceDelegate()
							.saveSelectedFundDetails(
									userProfile.getCurrentContract()
											.getContractNumber(),
									profileID,
									cofidFundRecommendVo);
				cofidFundRecommendVo.setOptOutIndicatorTemp(cofidFundRecommendVo.getOptOutIndicator());
			}

		}

		List<CofidFundRecommendVO> cofidFundRecommendDetails = getContractServiceDelegate().getCoFiduciaryFundDetails(
						userProfile.getCurrentContract().getContractNumber());
		
		form.setDirty("false");
		form
				.setCofidFundRecommendDetails(cofidFundRecommendDetails);
		return forwards.get("save");

	}

	@RequestMapping(value ="/coFiduciaryFundRecommendationReview/", params={"action=downloadNoticePdf"} , method =  {RequestMethod.GET}) 
	public String doDownloadNoticePdf (@Valid @ModelAttribute("cofiduciaryReviewScreenPageForm") CofiduciaryReviewScreenPageForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws SystemException {
		if(bindingResult.hasErrors()){
	        String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	       if(errDirect!=null){
	              request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	              return forwards.get("input");
	       }
		}

		List errors = new ArrayList();
		

		try {
			// get the user profile object and set the current contract to null
			UserProfile userProfile = getUserProfile(request);
			Contract currentContract = userProfile.getCurrentContract();

			if (currentContract == null) {
				return Constants.HOMEPAGE_FINDER_FORWARD;
			}

			// Method to populate the quarterly PDF document for selected
			// contract.
			ContractDocumentsHelper.getInstance().getNoticePdfContractDocument(
					request, response, currentContract);

		} catch (ServiceUnavailableException e) {
			errors.add(new GenericException(
					ErrorCodes.REPORT_SERVICE_UNAVAILABLE));
		}

		if (!errors.isEmpty()) {
			setErrorsInRequest(request, errors);
			request.setAttribute(Constants.LAYOUT_BEAN, LayoutBeanRepository
					.getInstance().getPageBean(EMPTY_LAYOUT_ID));

			return forwards.get(Constants.SECONDARY_WINDOW_ERROR_FORWARD);
		}

		return null;
	}
	
	public ContractServiceDelegate getContractServiceDelegate(){
		return ContractServiceDelegate.getInstance();
	}
	
	public UserProfile getUserProfileOverride(HttpServletRequest request) {
		return SessionHelper.getUserProfile(request);
	}
	
	protected Collection doValidate(ActionForm  form, HttpServletRequest request) {

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

		if (!penErrors.isEmpty()) {
			request.removeAttribute(CommonConstants.ERROR_KEY);
			return penErrors;
		}
		return penErrors;
	}

	@Autowired
	private PSValidatorFWDefault psValidatorFWDefault;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.bind(request);
		binder.addValidators(psValidatorFWDefault);
	}

}
