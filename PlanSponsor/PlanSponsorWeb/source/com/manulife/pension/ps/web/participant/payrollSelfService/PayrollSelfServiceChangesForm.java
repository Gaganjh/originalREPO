package com.manulife.pension.ps.web.participant.payrollSelfService;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangeViewRecordType;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangesReportData;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.report.ReportActionCloneableForm;
import com.manulife.pension.service.report.valueobject.ReportData;

import lombok.Getter;
import lombok.Setter;


public class PayrollSelfServiceChangesForm extends ReportActionCloneableForm implements Serializable {

	private static final long serialVersionUID = -2037379785387574467L;

	private static final Logger logger = Logger.getLogger(PayrollSelfServiceChangesForm.class);

	//Filter Fields
	@Getter @Setter
	private String lastName;

	@Getter @Setter
	private String ssnFirstGroup;

	@Getter @Setter
	private String ssnSecondGroup;

	@Getter @Setter
	private String ssnThirdGroup;

	@Getter @Setter
	private String selfServiceType = "ALL";

	@Getter @Setter
	private String effectiveDateFrom = getLastMonthStartDay();

	@Getter @Setter
	private String effectiveDateTo;

	@Getter @Setter
	private String contractId;

	@Getter @Setter
	private String contractName;

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	@Getter @Setter
	private PayrollSelfServiceChangesReportData report = null;
	
	@Getter @Setter
	private boolean loanAllowedIndicator;

	public Map<String, String> selfServiceTypes(){
		Map<String, String> map = new LinkedHashMap<>();
		map.put(PayrollSelfServiceChangeViewRecordType.ALL.getCode(), "All");
		map.put(PayrollSelfServiceChangeViewRecordType.DEFERRAL.getCode(), "Deferral Rate Changes");
		map.put(PayrollSelfServiceChangeViewRecordType.ENROLLMENT.getCode(), "Enrollments");
		if(this.loanAllowedIndicator) {
			map.put(PayrollSelfServiceChangeViewRecordType.LOAN.getCode(), "Loans");		
		}
		return map;
	}

	public Ssn getSSN() {
		Ssn ssn = new Ssn();
		ssn.setDigits(0, ssnFirstGroup);
		ssn.setDigits(1, ssnSecondGroup);
		ssn.setDigits(2, ssnThirdGroup);
		return ssn;
	}

	public LocalDate getEffectiveDateFromAsLocalDate() {
		LocalDate localDate = null;
		try {
			localDate = LocalDate.parse(effectiveDateFrom, formatter);
		}catch(DateTimeParseException parseExp) {
			if (logger.isDebugEnabled()) {
				logger.debug("Cannot parse the Effective Date From " + effectiveDateFrom);
			}
		}
		return localDate;
	}

	public LocalDate getEffectiveDateToAsLocalDate() {
		LocalDate localDate = null;
		try {
			localDate = LocalDate.parse(effectiveDateTo, formatter);
		}catch(DateTimeParseException parseExp) {
			if (logger.isDebugEnabled()) {
				logger.debug("Cannot parse the Effective Date to " + effectiveDateTo);
			}
		}
		return localDate;
	}

	public static String getLastMonthStartDay() {
		LocalDate thirtyDaysBackDate = LocalDate.now().minusDays(30L);
		return thirtyDaysBackDate.format(formatter);
	}

	public boolean showEditActionButton(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return (boolean) session.getAttribute(Constants.SHOW_EDIT_ACTION_BUTTON);
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ReportData> getReportPageDetails() {
		if(this.report == null 
				|| CollectionUtils.isEmpty(this.report.getDetails())) {
			return Collections.emptyList();
		}

		int pageSize = getPageSize();

		return (List<ReportData>) this.report.getDetails()
				.stream()
				.skip(this.getPageNumber() < 2 ? 0 : pageSize * (this.getPageNumber() - 1) )
				.limit(pageSize)
				.collect(Collectors.toList());
	}

	protected int getPageSize() {	
		int pageSize = 0;
		if(this.report != null && this.report.getReportCriteria() != null) {
			pageSize = this.report.getReportCriteria().getPageSize();
		}		
		return pageSize;
	}

}
