package com.manulife.pension.ps.web.participant.payrollSelfService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangesActivityHistoryReportData;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangesReportData;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.report.ReportController;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.content.GenericException;

@Controller
@RequestMapping("/participant/payrollSelfServiceActivityHistory")
@SessionAttributes({ "payrollSelfServiceChangesForm" })
public class PayrollSelfServiceChangesActivityHistoryReportController extends ReportController {

	private static final Logger LOGGER = Logger.getLogger(PayrollSelfServiceChangesActivityHistoryReportController.class);
	private static final String CSV_DOWNLOAD_FILENAME_PART = "Activity_History_Report_";
	
	@Autowired
	private PayrollSelfServiceChangesActivityHistoryCSVReport payrollSelfServiceChangesActivityHistoryCSVReport;

	@ModelAttribute("payrollSelfServiceChangesForm")
	public PayrollSelfServiceChangesForm populateForm() {
		return new PayrollSelfServiceChangesForm();
	}


	@GetMapping(params = { "task=download" })
	public void doDownload(
			@Valid @ModelAttribute("payrollSelfServiceChangesForm") PayrollSelfServiceChangesForm payrollSelfServiceChangesForm,
			BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response)
					throws SystemException {
		
		// Validate User and Contract for access		
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		if (userProfile == null || userProfile.isSelectedAccess()
				|| userProfile.getCurrentContract().isDiscontinued()) {
			String profileId = userProfile == null ? null : userProfile.getProfileId();
			int contractId = userProfile == null ? null : userProfile.getCurrentContract().getContactId();
			LOGGER.error("Unauthorized activity history report access by user profile id [" + profileId
					+ "] and contract id [" + contractId + "]");
			return;
		}
	
        ReportCriteria criteria = getReportCriteria(getReportId(), payrollSelfServiceChangesForm, request);
        ReportData reportData = null;
        try {
        	reportData = getReportData(getReportId(), criteria, request);
        	PayrollSelfServiceUtils.sortActivityHistoryReportDetails(reportData);        	
        	
        } catch (ReportServiceException e) {
            LOGGER.error("Received a Report service exception: ", e);
            setErrorsInRequest(request, Arrays.asList(new GenericException(Integer.parseInt(e.getErrorCode()))));
        }
        byte[] downloadData = getDownloadData(payrollSelfServiceChangesForm, reportData, request);
        streamDownloadData(request, response, getContentType(), getFileName(request), downloadData);
	}

	@Override
	protected String getReportId() {
		return PayrollSelfServiceChangesActivityHistoryReportData.REPORT_ID;
	}

	@Override
	protected String getReportName() {
		return PayrollSelfServiceChangesActivityHistoryReportData.REPORT_NAME;
	}

	@Override
	protected String getDefaultSort() {
		return PayrollSelfServiceChangesActivityHistoryReportData.DEFAULT_SORT;
	}

	@Override
	protected String getDefaultSortDirection() {
		return ReportSort.ASC_DIRECTION;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected byte[] getDownloadData(BaseReportForm reportForm, ReportData report, HttpServletRequest request)
			throws SystemException {
		final int contractId = getContractNumber(request);
		PayrollSelfServiceChangesForm payrollSelfServiceChangesForm = (PayrollSelfServiceChangesForm) reportForm;
		payrollSelfServiceChangesForm.setContractId(String.valueOf(contractId));
		payrollSelfServiceChangesForm.setContractName(getContractName(contractId));
		return payrollSelfServiceChangesActivityHistoryCSVReport.generateCSV(report.getDetails(), payrollSelfServiceChangesForm,
				PayrollSelfServiceChangesWebUtility.requireSSNMask(getUserProfile(request))).getBytes();
	}

	protected String getFileName(HttpServletRequest request) {
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return new StringBuilder().append(CSV_DOWNLOAD_FILENAME_PART)
				.append(getUserProfile(request).getCurrentContract().getContractNumber())
				.append("_")
				.append(localDate.format(formatter))
				.append(CSV_EXTENSION)				
				.toString();
	}

	@Override
	protected void populateReportCriteria(ReportCriteria criteria, BaseReportForm form, HttpServletRequest request)
			throws SystemException {
		PayrollSelfServiceChangesForm payrollSelfServiceChangesForm = (PayrollSelfServiceChangesForm) form;
		final int contractNumber = getContractNumber(request);
		criteria.addFilter(PayrollSelfServiceChangesActivityHistoryReportData.FILTER_CONTRACT_ID, Integer.valueOf(contractNumber));
		if (StringUtils.isNotBlank(payrollSelfServiceChangesForm.getLastName())) {
			criteria.addFilter(PayrollSelfServiceChangesActivityHistoryReportData.FILTER_LAST_NAME,
					payrollSelfServiceChangesForm.getLastName());
		} else if (StringUtils.isNotBlank(payrollSelfServiceChangesForm.getSSN().toString())) {
			criteria.addFilter(PayrollSelfServiceChangesActivityHistoryReportData.FILTER_SSN,
					payrollSelfServiceChangesForm.getSSN().toString());
		}
		if (StringUtils.isNotBlank(payrollSelfServiceChangesForm.getSelfServiceType())) {
			criteria.addFilter(PayrollSelfServiceChangesActivityHistoryReportData.FILTER_RECORD_TYPE,
					payrollSelfServiceChangesForm.getSelfServiceType());
		}

		if (Objects.nonNull(payrollSelfServiceChangesForm.getEffectiveDateFrom())) {
			criteria.addFilter(PayrollSelfServiceChangesActivityHistoryReportData.FILTER_FROM_EFFECTIVE_DATE,
					payrollSelfServiceChangesForm.getEffectiveDateFrom());
		}
		if (Objects.nonNull(payrollSelfServiceChangesForm.getEffectiveDateTo())) {
			criteria.addFilter(PayrollSelfServiceChangesActivityHistoryReportData.FILTER_TO_EFFECTIVE_DATE,
					payrollSelfServiceChangesForm.getEffectiveDateTo());
		}
		if(payrollSelfServiceChangesForm.isLoanAllowedIndicator()) {
			criteria.addFilter(PayrollSelfServiceChangesReportData.FILTER_INCLUDE_LOANS,
					Boolean.TRUE);
		}
	}

	private int getContractNumber(HttpServletRequest request) {
		// always need the contractId
		UserProfile userProfile = getUserProfile(request);
		return userProfile.getCurrentContract().getContractNumber();
	}
	@Override
	protected void populateReportForm(BaseReportForm reportForm, HttpServletRequest request) {
		// do nothing - sonarqube rules
	}

	private String getContractName(int contractId) {
		try {
			return ContractServiceDelegate.getInstance().getContractName(contractId);
		} catch (SystemException exp) {
			LOGGER.error("Excepion while getting contract name for " + contractId + " from Contract service" + exp);
		}
		return null;
	}
	

}
