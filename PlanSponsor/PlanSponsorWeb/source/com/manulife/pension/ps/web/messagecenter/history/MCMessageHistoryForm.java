package com.manulife.pension.ps.web.messagecenter.history;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.SsnRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.history.MCHistoryUtils.MessageStatus;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.service.account.entity.ContractMoneyType;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;

public class MCMessageHistoryForm extends ReportForm implements
		MCConstants {

	private static final int MaximumNameLength = 30;

	public static final String EmptySelection = "";

	private String contractId;
	private String tab = EmptySelection;
	private String section = EmptySelection;
	private String type = EmptySelection;
	private String lastName;
	private String fromDate;
	private String toDate;
	private String ssn1 = "";
	private String ssn2 = "";
	private String ssn3 = "";
	private String submissionId = "";

	private Date fromDateAsDate;
	private Date toDateAsDate;
	private String messageStatusForHistory; //don't use the message status field in the report form, because they have different drop down list
											//and therefore different set of values, so treat the separately
	private  List<LabelValueBean> tabb;

	public List<LabelValueBean> getTabb() {
		return tabb;
	}

	public void setTabb(List<LabelValueBean> tabb) {
		this.tabb = tabb;
	}

	/**
	 * Whether the advanced search is on
	 */
	private boolean advancedSearch;

	private String messageId = "";

	private static final int WeekAgo = -7;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ServletContext servlet;

	public MCMessageHistoryForm() {
		setFromDate(getDefaultFromDate());
		setToDate(getDefaultToDate());
	}

	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}

	public List<LabelValueBean> getTabs() throws SystemException {
		return null;//TODOMCHistoryUtils.getTypes(getServlet());
	}
	public List<LabelValueBean> getMessageStatusList() throws SystemException {
		List<LabelValueBean> returnList = new ArrayList<LabelValueBean>();
		for( MessageStatus status : MCHistoryUtils.MessageStatus.values() ) {
			returnList.add(new LabelValueBean(status.getText(), status.getValue()));
		}
		return returnList;
	}

	public List<LabelValueBean> getSections() throws SystemException {
		return null;//TODOMCHistoryUtils.getSections(getServlet(), tab);
	}

	public List<LabelValueBean> getTypes() throws SystemException {
		return null;//TODOMCHistoryUtils.getTypes(getServlet(), tab, section);
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public void clear() {
		tab = EmptySelection;
		section = EmptySelection;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = StringUtils.trimToEmpty(lastName);
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = StringUtils.trimToEmpty(fromDate);
		this.fromDateAsDate = MCUtils.convertDate(this.fromDate);
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = StringUtils.trimToEmpty(toDate);
		this.toDateAsDate = MCUtils.convertDate(this.toDate);
	}

	public String getSsn1() {
		return ssn1;
	}

	public void setSsn1(String ssn1) {
		this.ssn1 = StringUtils.trimToEmpty(ssn1);
	}

	public String getSsn2() {
		return ssn2;
	}

	public void setSsn2(String ssn2) {
		this.ssn2 = StringUtils.trimToEmpty(ssn2);
	}

	public String getSsn3() {
		return ssn3;
	}

	public void setSsn3(String ssn3) {
		this.ssn3 = StringUtils.trimToEmpty(ssn3);
	}

	public String getSsn() {
		return "" + StringUtils.trimToEmpty(ssn1)
				+ StringUtils.trimToEmpty(ssn2) + StringUtils.trimToEmpty(ssn3);
	}

	public void validate(Collection<GenericException> errors) {

		if (!StringUtils.isEmpty(fromDate)) {
			if (fromDateAsDate == null) {
				errors.add(new ValidationError("fromDate",
						ErrorFromDateInvalidFormat));
			}
		}

		if (!StringUtils.isEmpty(toDate)) {
			if (toDateAsDate == null) {
				errors.add(new ValidationError("toDate",
						ErrorToDateInvalidFormat));
			}
		}

		if ((StringUtils.isEmpty(fromDate) && !StringUtils.isEmpty(toDate))) {
			errors.add(new ValidationError("fromDate", ErrorFromDateEmpty));
		}

		if ((!StringUtils.isEmpty(fromDate) && StringUtils.isEmpty(toDate))) {
			errors.add(new ValidationError("toDate", ErrorToDateEmpty));
		}

		if (fromDateAsDate != null && toDateAsDate != null) {
			if (fromDateAsDate.after(toDateAsDate)) {
				errors.add(new ValidationError("fromDate",
						ErrorFromToDateInvalid));
			}
		}
		// two years validation
		if (fromDateAsDate != null) {
			Date twoYearAgo = DateUtils.truncate(DateUtils.add(new Date(),
					Calendar.YEAR, -2), Calendar.DATE);
			if (fromDateAsDate.before(twoYearAgo)) {
				errors.add(new ValidationError("fromDate", ErrorFromDateBeforeTwoYears));
			}

		}

		if (fromDateAsDate != null) {
			if (fromDateAsDate.after(new Date())) {
				errors.add(new ValidationError("fromDate", ErrorToDateInFuture));
			}
		}
		if (!StringUtils.isEmpty(lastName)) {
			if (!StringUtils.isAsciiPrintable(lastName)) {
				errors.add(new ValidationError("name", ErrorNameInvalid));
			}
		}

		String ssn = getSsn();
		if (!StringUtils.isEmpty(ssn)) {
			SsnRule.getInstance().validate("ssn", errors, getSsn());
		}

		if (!StringUtils.isEmpty(submissionId)) {
			try {
				Integer.parseInt(submissionId);
			} catch (NumberFormatException e) {
				errors.add(new ValidationError("submissionId",
						ErrorSubmissionIdFormat));
			}
		}

		return;
	}

	public void sortErrors(Collection<GenericException> errors) {
		Collections.sort((List<GenericException>) errors,
				new Comparator<GenericException>() {
					public int compare(GenericException o1, GenericException o2) {
						int index1 = getErrorIndex(o1.getErrorCode());
						int index2 = getErrorIndex(o2.getErrorCode());
						if (index1 == index2) {
							return 0;
						} else if (index1 < index2) {
							return -1;
						} else {
							return 1;
						}
					}
				});
	}

	public String getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(String submissionId) {
		this.submissionId = StringUtils.trimToEmpty(submissionId);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = StringUtils.trimToEmpty(type);
	}

	public Date getFromDateAsDate() {
		return fromDateAsDate;
	}

	public Date getToDateAsDate() {
		return toDateAsDate;
	}

	public String getDefaultFromDate() {
		return MCUtils.formatDate(DateUtils.add(new Date(), Calendar.DATE,
				WeekAgo));
	}

	public String getDefaultToDate() {
		return MCUtils.formatDate(new Date());
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public boolean isAdvancedSearch() {
		return advancedSearch;
	}

	public void setAdvancedSearch(boolean advancedSearch) {
		this.advancedSearch = advancedSearch;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	/**
	 * The order of the field appeared on screen
	 */
	private static int[] errorOrder = new int[] { ErrorContractIdFormat,
			ErrorFromDateInvalidFormat, ErrorFromToDateInvalid,
			ErrorFromDateEmpty, ErrorToDateInvalidFormat, ErrorToDateEmpty,
			ErrorNameInvalid, ErrorCodes.SSN_INVALID,
			ErrorSubmissionIdFormat };

	protected int[] getErrorOrder() {
		return errorOrder;
	}

	protected int getErrorIndex(int code) {
		int[] errors = getErrorOrder();
		for (int i = errors.length - 1; i >= 0; i--) {
			if (errors[i] == code) {
				return i;
			}
		}
		return -1;
	}

	public String getMessageStatusForHistory() {
		return messageStatusForHistory;
	}

	public void setMessageStatusForHistory(String messageStatusForHistory) {
		this.messageStatusForHistory = messageStatusForHistory;
	}
}
