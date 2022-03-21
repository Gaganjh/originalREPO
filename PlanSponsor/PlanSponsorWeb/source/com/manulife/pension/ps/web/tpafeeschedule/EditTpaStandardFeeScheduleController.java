package com.manulife.pension.ps.web.tpafeeschedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.tpafeeschedule.TpaStandardFeeScheduleForm.PageMode;
import com.manulife.pension.service.fee.valueobject.FeeDataVO;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * Action class to handle TPA Standard Fee Schedule Data for editing
 * 
 * @author Akhil Khanna
 * 
 */
@Controller
@RequestMapping( value = "/editTpaStandardFeeSchedule/")
@SessionAttributes({"tpaStandardFeeScheduleForm"})

public class EditTpaStandardFeeScheduleController extends BaseFeeScheduleController {
	@ModelAttribute("tpaStandardFeeScheduleForm") 
	public TpaStandardFeeScheduleForm populateForm() 
	{
		return new TpaStandardFeeScheduleForm();
		}
	public static HashMap<String,String>forwards=new HashMap<String,String>();
	static{
		forwards.put("input","/tpafeeschedule/editTpaStandardFeeSchedule.jsp");
	forwards.put("editTpaStandardFeeData","/tpafeeschedule/editTpaStandardFeeSchedule.jsp");
	forwards.put("default","/tpafeeschedule/editTpaStandardFeeSchedule.jsp");
	forwards.put("goToPreviewAction","redirect:/do/previewTpaStandardFeeSchedule/");
	forwards.put("goToViewAction","redirect:/do/viewTpaStandardFeeSchedule/");
	forwards.put("goToSelectTpaFirmAction","redirect:/do/feeSchedule/selectTpaFirm/");}

	/**
	 *Method to get the TPA's Standard Fee Schedule details to be used for
	 * editing including the data for all the standard fee types as well as the
	 * custom fee types
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param actionForm
	 *            AutoForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 * @throws SystemException
	 */
	@RequestMapping(  method =  {RequestMethod.GET,RequestMethod.POST}) 
	public String doDefault(@Valid @ModelAttribute("tpaStandardFeeScheduleForm") TpaStandardFeeScheduleForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
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

		
		
		UserProfile userProfile = getUserProfile(request);
		
		// if lock already exists, go to edit mode, else go to Home Page.
		final boolean lockObtained = checkIfUserObtainedLockOnTpaFirm(
				actionForm.getSelectedTpaFirmId(), request);
		if (!lockObtained) {
			return Constants.HOMEPAGE_FINDER_FORWARD;
		}

		UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(
				userProfile.getPrincipal());
		if (userProfile.getRole().isInternalUser()) {
			return forwards.get(SELECT_TPA_FIRM_PAGE);
		} else {
			// If TPA is not having TPA User Manager permissions redirect to
			// Select TPA firm page
			if (!isTpaUserManager(userInfo,
					actionForm.getSelectedTpaFirmId())) {
				return forwards.get(SELECT_TPA_FIRM_PAGE);
			}
		}
		
		FeeServiceDelegate delegate = FeeServiceDelegate.getInstance("PS");
		List<FeeDataVO> feeLabelList = new ArrayList<FeeDataVO>();
		List<FeeDataVO> standardFeeList = new ArrayList<FeeDataVO>();
		List<FeeDataVO> customFeeList = new ArrayList<FeeDataVO>();
		if (PageMode.PREVIEW.equals(actionForm
				.getPageMode())) {
			standardFeeList = actionForm
					.getFeeLabelList();
			customFeeList = actionForm
					.getAdditionalFeeItemsList();
			actionForm.setFeeLabelList(standardFeeList);
			
			actionForm
					.setAdditionalFeeItemsList(customFeeList);
		} else {

			feeLabelList = delegate
					.getTpaStandardFeeScheduleDataForEdit(Integer
							.parseInt(actionForm
									.getSelectedTpaFirmId()));
			for (FeeDataVO vo : feeLabelList) {
				if (vo.getFeeCategoryCode().equals("TP")) {
					standardFeeList.add(vo);
				} else {
					customFeeList.add(vo);
				}
			}
			actionForm.setFeeLabelList(standardFeeList);
			
			actionForm
					.setAdditionalFeeItemsList(customFeeList);

			// Clone the form to track the changes
			actionForm.clear( request);
			actionForm.storeClonedForm();
		}
		
		// Add an empty row if the additional rows are less than five
		if (actionForm.getAdditionalFeeItemsList().size() < 5) {
			actionForm.getAdditionalFeeItemsList().add(getDefaultFeeDataVO());
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doDefault");
		}
		
		return forwards.get(TPA_STANDARD_FEE_SCHEDULE_EDIT_PAGE);

	}

	/**
	 * This method is called when next button is clicked.It forwards the edit
	 * page modified data rows to the Confirmation page to display after
	 * validating all the modified data.
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return forward
	 * @throws SystemException
	 */
	@RequestMapping(params={"action=next"}   , method =  {RequestMethod.POST}) 
	public String doNext (@Valid @ModelAttribute("tpaStandardFeeScheduleForm") TpaStandardFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
	
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }
	
	
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doNext");
		}

		

		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		
		errorMessages = FeeScheduleDataValidator.validateStandardScheduleFees(form);


		if (!errorMessages.isEmpty()) {
			setErrorsInSession(request, errorMessages);

			return forwards.get("input");
		}

		List<FeeDataVO> actualStandardFeeTypeList = new ArrayList<FeeDataVO>();
		actualStandardFeeTypeList = ((TpaStandardFeeScheduleForm) form
				.getClonedForm()).getFeeLabelList();
		
		List<FeeDataVO> previousAdditionalFeeList = new ArrayList<FeeDataVO>();
		previousAdditionalFeeList = ((TpaStandardFeeScheduleForm) form
				.getClonedForm()).getAdditionalFeeItemsList();
		
		List<FeeDataVO> updatedStandardFeeTypeList = form.getFeeLabelList();
		List<FeeDataVO> updatedCustomFeeTypeList = form.getAdditionalFeeItemsList();
		List<FeeDataVO> previewFeeItems = new ArrayList<FeeDataVO>();

		// Find the changed standard fee types and add it to the preview list
		for (FeeDataVO actualVo : actualStandardFeeTypeList) {
			for (FeeDataVO updatedVo : updatedStandardFeeTypeList) {

				if (actualVo.getFeeCode().equals(updatedVo.getFeeCode())) {
					if (!actualVo.equals(updatedVo)) {			
						previewFeeItems.add(updatedVo);
					}
					break;
				}
			}
		}

		// Clean up the modified custom fee list
		updatedCustomFeeTypeList = removeEmptyFeeObjects(updatedCustomFeeTypeList);
		
		// Find out the changes and add them to the list which will passed on to preview page
		List<FeeDataVO> previewAdditioanleFeeItemList = new ArrayList<FeeDataVO>();
		for (FeeDataVO modifiedVO : updatedCustomFeeTypeList) {
			for (FeeDataVO actualVO : previousAdditionalFeeList) {
				if (StringUtils.equals(modifiedVO.getFeeCode(), actualVO
						.getFeeCode())
						&& !modifiedVO.equals(actualVO)) {
					if (modifiedVO.isDeletedIndicator()
							|| (StringUtils.isBlank(modifiedVO
									.getFeeDescription()) && modifiedVO
									.getFeeValueAsDecimal() == 0.0d)) {
						FeeDataVO deletedFeeVo = new FeeDataVO(actualVO
								.getFeeCode(), actualVO.getFeeDescription(),
								actualVO.getAmountType(), actualVO.getAmountValue(),
								StringUtils.EMPTY);
						deletedFeeVo.setDeletedIndicator(true);
						deletedFeeVo.setFeeCategoryCode("TN");
						previewAdditioanleFeeItemList.add(deletedFeeVo);
					} else {
						previewAdditioanleFeeItemList.add(modifiedVO);
					}
					break;
				}
			}

			if (!modifiedVO.isDeletedIndicator()
					&& StringUtils.isNotBlank(modifiedVO.getAmountValue())
					&& StringUtils.isBlank(modifiedVO.getFeeCode())) {
				previewAdditioanleFeeItemList.add(modifiedVO);
			}
		}
		
		for (FeeDataVO feeDataVo : updatedCustomFeeTypeList) {
			feeDataVo.setFeeCategoryCode("TN");
		}
		
		if (CollectionUtils.isNotEmpty(previewAdditioanleFeeItemList)) {
			previewFeeItems.addAll(previewAdditioanleFeeItemList);
		}

		if (previewFeeItems.isEmpty()) {
			ValidationError exception = new ValidationError(
					new String[] { GLOBAL_ERROR }, ErrorCodes.SAVING_WITH_NO_CHANGES);
			errorMessages.add(exception);
			if (!errorMessages.isEmpty()) {	
				setErrorsInSession(request, errorMessages);
			}
			return forwards.get("input");
		}

		form.setPreviewFeeItemsList(previewFeeItems);
		form.setAdditionalFeeItemsList(updatedCustomFeeTypeList);
		form.setEditPageModified(true);
		form.setPageMode(PageMode.EDIT);
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doNext");
		}
		
		//TODO saveToken(request);
		
		return forwards.get(TPA_STANDARD_FEE_SCHEDULE_PREVIEW_ACTION);
	}

	/**
	 * This method is called when cancel button is clicked.It resets the edit
	 * page modified data and redirect to the view page
	 * 
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return forward
	 * @throws SystemException
	 */
	@RequestMapping(params={"action=cancelEdit"}   ,  method =  {RequestMethod.POST}) 
	public String doCancelEdit (@Valid @ModelAttribute("tpaStandardFeeScheduleForm") TpaStandardFeeScheduleForm form, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
        	String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
        	if(errDirect!=null){
             request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
             return forwards.get(errDirect)!=null?forwards.get(errDirect):forwards.get("input");//if input forward not //available, provided default
        	}
        }


		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doCancelEdit");
		}

		
		form.clear( request);
		form.resetData();
		form.setPageMode(PageMode.EDIT);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doCancelEdit");
		}

		return forwards.get(TPA_STANDARD_FEE_SCHEDULE_VIEW_ACTION);
	}

	private List<FeeDataVO> removeEmptyFeeObjects (List<FeeDataVO> customFeeTypeList) {
		List<FeeDataVO> feeList = new ArrayList<FeeDataVO>();
		
		for (FeeDataVO feeData : customFeeTypeList) {
			if(StringUtils.isNotEmpty(feeData.getFeeCode()) ||  !feeData.isValueEmpty()){
				feeList.add(feeData);
			}
		}
		return feeList;
	}
	
	/**
	 * Returns a default FeeDataVO object
	 * @return feeDataVO
	 */
	private FeeDataVO getDefaultFeeDataVO() {
		return new FeeDataVO("", "", "$", "", "");
	}
}