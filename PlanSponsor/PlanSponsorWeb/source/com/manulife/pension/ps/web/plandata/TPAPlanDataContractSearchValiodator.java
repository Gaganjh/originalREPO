package com.manulife.pension.ps.web.plandata;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ezk.web.ActionForm;
import com.manulife.pension.platform.web.report.BaseReportController;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressesReportData;
import com.manulife.pension.ps.service.report.plandata.valueobject.TPAPlanDataContractSearchReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
@Component
public class TPAPlanDataContractSearchValiodator   extends ValidatorUtil implements Validator{
	
private static Logger logger = Logger.getLogger(TPAPlanDataContractSearchValiodator.class);
	
	@Override
	public boolean supports(Class<?> clazz) {
		return TPAPlanDataContractSearchForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		List<GenericException> error = new ArrayList<GenericException>();
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		TPAPlanDataContractSearchForm form  =(TPAPlanDataContractSearchForm) target;
		
		
		
		
		boolean isValid = true;
		
		if (isFilterTask(request)) {
			isValid = validateInputFields(request, form);
		}

		if (!isValid) {
			TPAPlanDataContractSearchReportData reportData = new TPAPlanDataContractSearchReportData(
					null, 0);
			request.setAttribute(Constants.REPORT_BEAN, reportData);
		}
		
	}
	private boolean isFilterTask(HttpServletRequest request) {
		return StringUtils.equals(FILTER_TASK, getTask(request));
	}
private boolean validateInputFields(HttpServletRequest request, ActionForm reportForm)  {
		
		TPAPlanDataContractSearchForm tpaPlanDataContractSearchForm = (TPAPlanDataContractSearchForm) reportForm;
		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		boolean isValid = true;
		
		String contractName = StringUtils.trimToNull(tpaPlanDataContractSearchForm.getContractName());
		String contractId = tpaPlanDataContractSearchForm.getContractNumber();
				
		// Validate contract name length >0 & <3
		if (StringUtils.isNotBlank(contractName)) {

			if (!isValidContractName(tpaPlanDataContractSearchForm
					.getContractName())) {
				ValidationError exception = new ValidationError(
						Constants.TPA_CONTRACT_NAME_SORT_FIELD,
						ErrorCodes.CONTRACT_LESS_THAN_THREE_DIGITS);
				errorMessages.add(exception);
			}
		}
		
		// Validate the contract number field
		if (StringUtils.isNotBlank(contractId)) {

			if (!NumberUtils.isNumber(tpaPlanDataContractSearchForm
					.getContractNumber().trim())) {
				ValidationError exception = new ValidationError(
						Constants.TPA_CONTRACT_NUMBER_SORT_FIELD,
						ErrorCodes.NON_NUMERIC_CONTRACT_NUMBER);
				errorMessages.add(exception);
			}
		}

		
		if (!errorMessages.isEmpty()) {
			isValid = false;
			/*TODO move to controllersetErrorsInRequest(request, errorMessages);*/
		}

		return isValid;
	}
private boolean isValidContractName(String contractName) {

	if (StringUtils.trimToNull(contractName) != null
			&& StringUtils.trimToNull(contractName).length() < 3) {
		return false;
	} else {
		return true;
	}
}
/*@Override
protected String getReportId() {
	
	return ParticipantAddressesReportData.REPORT_ID;
}

@Override
protected String getReportName() {
	
return	ParticipantAddressesReportData.REPORT_NAME;
}

@Override
protected String getDefaultSort() {
	
	return ParticipantAddressesReportData.DEFAULT_SORT;
}

@Override
protected String getDefaultSortDirection() {
	
	return ReportSort.ASC_DIRECTION;
}

@Override
protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
		throws SystemException {
	StringBuffer buffer = new StringBuffer();
	return buffer.toString().getBytes();
}

@Override
protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form,
		HttpServletRequest request) throws SystemException {
	
	
}*/
	
}
