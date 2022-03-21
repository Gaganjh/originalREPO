package com.manulife.pension.bd.web.bob.planReview.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportForm;
import com.manulife.pension.bd.web.bob.planReview.PlanReviewReportUIHolder;
import com.manulife.pension.bd.web.myprofile.MyProfileBrokerPersonalInfoForm;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.environment.EnvironmentServiceHelper;
import com.manulife.pension.service.planReview.valueobject.ShippingVO;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

/**
 * This is a validator class for Plan Review report Pages
 * 
 * @author NagaRaju
 * 
 * 
 */
public class PlanReviewReportDataValidator {
	
	private static final String PRESENTER_NAME = "presenterName";
	
	private static final String RECIPIENT_NAME = "RecipientName";
	private static final String RECIPIENT_PHONE_NUMBER = "phoneNumber";
	private static final String EMAIL_ADDRESS = "emailAddress";
	private static final String COMPANY = "company";
	private static final String ADDRESS_LINE_1 = "addressLine1";
	private static final String CITY = "city";
	private static final String STATE = "state";
	private static final String COUNTRY = "country";
	private static final String ZIP_CODE = "zipCode";
	 
	private final static RegularExpressionRule invalidContactEmailRERule = new RegularExpressionRule(
	            BDErrorCodes.EMAIL_AND_ZIP_INVALID, BDRuleConstants.EMAIL_ADDRESS_RE);
	/**
	 * This method validates for Empty Presenter Name
	 * 
	 * @param contractReviewReportForm
	 * @return ArrayList<GenericException>
	 * @throws SystemException
	 */
	public static ArrayList<GenericException> validatePresentersName(
			PlanReviewReportForm contractReviewReportForm)
			throws SystemException {

		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();

		validatePresenter(errorMessages, PRESENTER_NAME,
				contractReviewReportForm.getDisplayContractReviewReports());

		return errorMessages;

	}
	
	/**
	 * This method validates for Empty Presenter Name
	 * 
	 * @param contractReviewReportForm
	 * @return ArrayList<GenericException>
	 * @throws SystemException
	 */
	public static ArrayList<GenericException> validateShippingpage(
			ShippingVO shippingAddressVO )
			throws SystemException {

		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		
		validateMondetoryShippindDetails(errorMessages, RECIPIENT_NAME, shippingAddressVO.getRecipientName());
		validateMondetoryShippindDetails(errorMessages, RECIPIENT_PHONE_NUMBER, shippingAddressVO.getRecipientPhoneNumber());
		validateMondetoryShippindDetails(errorMessages, ADDRESS_LINE_1, shippingAddressVO.getRecipientAddressLine1());
		validateMondetoryShippindDetails(errorMessages, COUNTRY, shippingAddressVO.getRecipientCountry());
		validateMondetoryShippindDetails(errorMessages, CITY, shippingAddressVO.getRecipientCity());
		validateMondetoryShippindDetails(errorMessages, ZIP_CODE, shippingAddressVO.getRecipientZip());
		validateMondetoryShippindDetails(errorMessages, STATE, shippingAddressVO.getRecipientState());
		validateMondetoryShippindDetails(errorMessages, EMAIL_ADDRESS, shippingAddressVO.getRecipientEmail());
		validateMondetoryShippindDetails(errorMessages, COMPANY, shippingAddressVO.getRecipientCompanyName());

		if (StringUtils.isNotEmpty(shippingAddressVO.getRecipientEmail())) {
			invalidContactEmailRERule.validate(
					MyProfileBrokerPersonalInfoForm.FIELD_CONTACT_EMAIL,
					errorMessages, shippingAddressVO.getRecipientEmail());
		}
		
		if (StringUtils.isNotEmpty(shippingAddressVO.getRecipientZip())) {

			if (StringUtils.equals("USA",
					shippingAddressVO.getRecipientCountry())) {

				boolean isValid = false;
				if (StringUtils.isNotEmpty(shippingAddressVO
						.getRecipientState()) 
						&& StringUtils.isNumeric(shippingAddressVO.getRecipientZip())) {
						
					try {
						isValid = EnvironmentServiceHelper.getInstance()
								.isUsZipCodeValidForState(
										shippingAddressVO.getRecipientState(),
										shippingAddressVO.getRecipientZip());
					} catch (Exception exception) {
						// do nothing.
						isValid = false;
					}

				}

				if (!isValid) {
					errorMessages.add(new ValidationError(ZIP_CODE,
							BDErrorCodes.EMAIL_AND_ZIP_INVALID));
				}
			}
		}
		
		return errorMessages;

	}
	
	/**
	 * This method validates for Empty Presenter Name
	 * 
	 * @param errorMessages
	 * @param presenterName
	 * @param selectedlist
	 * @throws SystemException
	 */
	private static void validateMondetoryShippindDetails(
			ArrayList<GenericException> errorMessages, String fieldName,
			String fieldValue) throws SystemException {

		if (StringUtils.isEmpty(fieldValue)) {
			errorMessages.add(new ValidationError(fieldName,
					BDErrorCodes.MANDATORY_FIELD));
		}
	}

	/**
	 * This method validates for Empty Presenter Name
	 * 
	 * @param errorMessages
	 * @param presenterName
	 * @param selectedlist
	 * @throws SystemException
	 */
	private static void validatePresenter(
			ArrayList<GenericException> errorMessages, String presenterName,
			List<PlanReviewReportUIHolder> selectedlist) throws SystemException {
		int count = -1;

		nextFee: for (PlanReviewReportUIHolder slected : selectedlist) {
			count++;
			if (StringUtils.isEmpty(slected.getPresenterName())) {
				errorMessages.add(new ValidationError(presenterName + count,
						BDErrorCodes.PLAN_REVIEW_REPORT_PRESENTER_NAME_BLANK));
				continue nextFee;
			}

		}
	}

}
