package com.manulife.pension.bd.web.usermanagement;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.manulife.pension.bd.web.validation.rules.BDRuleConstants;
import com.manulife.pension.bd.web.validation.rules.EmailAddressRule;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.service.security.bd.report.valueobject.BDExtUserSearchData;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

@Component
public class ExtUserSearchReportValidator implements Validator {
	private final RegularExpressionRule nameRErule = new RegularExpressionRule(BDErrorCodes.USER_SEARCH_INPUT_INVALID,
			BDRuleConstants.FIRST_NAME_LAST_NAME_RE);

	@Override
	public boolean supports(Class clazz) {
		return true;
	}

	@Override
	public void validate(Object target, Errors errors) {

		Collection<ValidationError> error = new ArrayList();
		BindingResult bindingResult = (BindingResult) errors;
		if (!bindingResult.hasErrors()) {
			ExtUserSearchReportForm f = (ExtUserSearchReportForm) target;
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpServletRequest request = attr.getRequest();
			String[] errorCodes = new String[10];
			/*
			 * // do not do further validation if there are errors already
			 * (otherwise // it shows two set of unrelated errors for one
			 * action; i.e. coming // for press reset pwd, manage user button if
			 * (error.size() != 0 || BDWebCommonUtils.hasErrors(request)) {
			 * 
			 * }
			 */

			String firstName = f.getFirstName();
			String lastName = f.getLastName();
			if (StringUtils.isNotEmpty(firstName)) {
				nameRErule.validate("", error, firstName);
				f.setFirstName(stripHtmlBrackets(firstName));
			}
			if (error.size() == 0 && StringUtils.isNotEmpty(lastName)) {
				nameRErule.validate("", error, lastName);
				f.setLastName(stripHtmlBrackets(lastName));
			}
			if (error.size() == 0 && (!StringUtils.isAlphanumeric(f.getContractNum())
					|| !StringUtils.isAlphanumeric(f.getProducerCode()))) {
				error.add(new ValidationError("", BDErrorCodes.USER_SEARCH_INPUT_INVALID));
				f.setContractNum(stripHtmlBrackets(f.getContractNum()));
				f.setProducerCode(stripHtmlBrackets(f.getProducerCode()));
			}

			if (error.size() == 0 && StringUtils.isNotEmpty(f.getEmailAddress())) {
				EmailAddressRule.getInstance().validate(ExtUserSearchReportForm.FIELD_EMAIL, error,
						f.getEmailAddress());
				f.setEmailAddress(stripHtmlBrackets(f.getEmailAddress()));
			}

			if (error.size() != 0) {
				BDExtUserSearchData reportData = new BDExtUserSearchData(null, 0);
				request.setAttribute(BDConstants.REPORT_BEAN, reportData);
				if (!error.isEmpty()) {
					for (Object e : error) {
						if (e instanceof GenericException) {
							GenericException errorEx = (GenericException) e;
							errorCodes = new String[] { Integer.toString(errorEx.getErrorCode()) };
							bindingResult.addError(new ObjectError(errors.getObjectName(), errorCodes,
									errorEx.getParams(), errorEx.getMessage()));

						}

					}
					if (request.getSession().getAttribute(CommonConstants.ERROR_RDRCT) == null) {
						request.getSession().setAttribute(CommonConstants.ERROR_RDRCT, CommonConstants.ERROR_PAGE);
					}

					request.getSession().removeAttribute(BdBaseController.ERROR_KEY);
					request.getSession().setAttribute(BdBaseController.ERROR_KEY, errors);
				}
			}
		}

	}

	private static String stripHtmlBrackets(String value) {
		if (value != null) {
			value = value.replaceAll("<", "").replaceAll(">", "");
		}
		return value;
	}

	
}
