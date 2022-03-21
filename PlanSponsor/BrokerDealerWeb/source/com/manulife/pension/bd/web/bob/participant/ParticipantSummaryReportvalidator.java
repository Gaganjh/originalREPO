package com.manulife.pension.bd.web.bob.participant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.CurrencyValidator;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.BdBaseController;
import com.manulife.pension.bd.web.util.ValidatorUtil;
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryReportData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
@Component
public class ParticipantSummaryReportvalidator extends ValidatorUtil implements Validator {

	 private static final String FILTER_TASK = "filter";
	 private static final RegularExpressionRule lastNameRErule = new RegularExpressionRule(
	            BDErrorCodes.LAST_NAME_INVALID,
	            BDRuleConstants.FIRST_NAME_LAST_NAME_RE);
	 private static final String LAST_NAME = "last_name";
	private static Logger logger = Logger.getLogger(ParticipantSummaryReportvalidator.class);
	@Override
	public boolean supports(Class clazz) {
		return true;
	}

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult = (BindingResult)errors;
		if(!bindingResult.hasErrors()){
		ArrayList<GenericException> error = new ArrayList<GenericException>();
		String[] errorCodes = new String[10];
		ParticipantSummaryReportForm theForm = (ParticipantSummaryReportForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> doValidate");
		}
		boolean isInvalidTotal = false;
		
		
		 // This code has been changed and added  to 
	   	 // Validate form and request against penetration attack, prior to other validations as part of the CL#136970 
		

		String namePhrase = theForm.getNamePhrase();
		List<ValidationError> tempArrayList = new ArrayList<ValidationError>();
		if (FILTER_TASK.equals(getTask(request))) {
			if (StringUtils.isNotBlank(namePhrase)) {
				if ((!(lastNameRErule.validate(LAST_NAME,
	                    tempArrayList, namePhrase)))
						|| (namePhrase.length() > GlobalConstants.LAST_NAME_LENGTH_MAXIMUM)) {
					GenericException exception = new GenericException(
							BDErrorCodes.LAST_NAME_INVALID);
					error.add(exception);
				}
			} 
			
			/**
			 * Validate the Asset Range From, Asset Range To filter values.
			 */
			String assetRangeFrom = theForm.getTotalAssetsFrom();
			String assetRangeTo = theForm.getTotalAssetsTo();
			if (!StringUtils.isEmpty(assetRangeFrom)
					|| !StringUtils.isEmpty(assetRangeTo)) {

				if (StringUtils.isBlank(assetRangeFrom)) {
					isInvalidTotal = true;
				}
				if (StringUtils.isBlank(assetRangeTo)) {
					isInvalidTotal = true;
				}

				if (StringUtils.isEmpty(assetRangeFrom)) {
					assetRangeFrom = BDConstants.ZERO_STRING;
				}
				if (StringUtils.isEmpty(assetRangeTo)) {
					assetRangeTo = BDConstants.ZERO_STRING;
				}

				// Checking if there are any consecutive commas. If yes, showing
				// info message to the
				// user.
				if (assetRangeFrom.contains(BDConstants.CONSECUTIVE_COMMAS)
						|| assetRangeTo
								.contains(BDConstants.CONSECUTIVE_COMMAS)) {
					isInvalidTotal = true;
				} else {
					BigDecimalValidator bigDecimalValidator = CurrencyValidator
							.getInstance();
					// BigDecimal value is set to null, if the currency is
					// invalid.
					BigDecimal assetRangeFromValue = bigDecimalValidator
							.validate(assetRangeFrom);
					BigDecimal assetRangeToValue = bigDecimalValidator
							.validate(assetRangeTo);
					BigDecimal maxAssetRangeValue = BDConstants.MAX_ASSET_VALUE;

					if (assetRangeFromValue == null
							|| assetRangeToValue == null
							|| assetRangeToValue.compareTo(assetRangeFromValue) < 0
							|| assetRangeToValue.compareTo(maxAssetRangeValue) > 0
							|| assetRangeFromValue
									.compareTo(maxAssetRangeValue) > 0) {
						isInvalidTotal = true;
					}
				}
				if (isInvalidTotal) {
					GenericException exception = new GenericException(
							BDErrorCodes.INVALID_TOTAL_ASSET_RANGE_FOR_FILTER);
					error.add(exception);
				}
			}
		}
		if (error.size() > 0) {
			ParticipantSummaryReportController participantSummaryReportAction=new ParticipantSummaryReportController();
			participantSummaryReportAction.populateReportForm( theForm, request);
			ParticipantSummaryReportData reportData = new ParticipantSummaryReportData(
					null, 0);
			request.setAttribute(BDConstants.REPORT_BEAN, reportData);
			for (Object e : error) {
				if (e instanceof GenericException) {
					GenericException errorEx=(GenericException) e;
					errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
					bindingResult.addError(new ObjectError(errors
							                 .getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
					
				}

			request.getSession().removeAttribute(BdBaseController.ERROR_KEY);
			request.removeAttribute(BdBaseController.ERROR_KEY);
			request.getSession().setAttribute(BdBaseController.ERROR_KEY, bindingResult);
 			request.setAttribute(BdBaseController.ERROR_KEY, bindingResult);
 			request.getSession().setAttribute(CommonConstants.ERROR_RDRCT, "input");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- doValidate");
		}
		}
	}
	}
}
