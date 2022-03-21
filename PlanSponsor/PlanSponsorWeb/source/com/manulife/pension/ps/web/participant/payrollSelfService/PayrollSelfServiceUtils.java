package com.manulife.pension.ps.web.participant.payrollSelfService;

import java.sql.Date;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangeRecord;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangesReportData;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PayrollSelfServiceUtils {

	private PayrollSelfServiceUtils() {}	

	public static void sortReportDetails(PayrollSelfServiceChangesReportData report, String sortField, String sortDirection) {
		if(report == null
				|| CollectionUtils.isEmpty(report.getDetails())) {
			return;
		}

		boolean isDescendingSort = ReportSort.DESC_DIRECTION.equalsIgnoreCase(sortDirection);

		switch(sortField) {
		case PayrollSelfServiceChangesReportData.INITIATED_FIELD:     
			sortByInitiated(report, isDescendingSort);	
			break;
		case PayrollSelfServiceChangesReportData.LAST_NAME_FIELD:     
			sortByLastName(report, isDescendingSort);	
			break;
		case PayrollSelfServiceChangesReportData.DESCRIPTION_FIELD:     
			sortByDescription(report, isDescendingSort);	
			break;
		default:
			sortByEffectiveDate(report, isDescendingSort);		 
			break;
		}
	}

	private static void sortByDescription(PayrollSelfServiceChangesReportData report, boolean isDescendingSort) {
		if(isDescendingSort) {
			report.setDetails((Collection) report.getDetails().stream()
					.sorted(Comparator.comparing(PayrollSelfServiceChangeRecord::getDescription, Comparator.nullsLast(Comparator.reverseOrder()))
							.thenComparing(PayrollSelfServiceChangeRecord::getEffectiveDate, Comparator.nullsLast(Comparator.reverseOrder()))
							).collect(Collectors.toList()));
		} else {
			report.setDetails((Collection) report.getDetails().stream()
					.sorted(Comparator.comparing(PayrollSelfServiceChangeRecord::getDescription, Comparator.nullsFirst(Comparator.naturalOrder()))
							.thenComparing(PayrollSelfServiceChangeRecord::getEffectiveDate, Comparator.nullsFirst(Comparator.naturalOrder()))
							).collect(Collectors.toList()));
		}		
	}

	private static void sortByLastName(PayrollSelfServiceChangesReportData report, boolean isDescendingSort) {
		if(isDescendingSort) {
			report.setDetails((Collection) report.getDetails().stream()
					.sorted(Comparator.comparing(PayrollSelfServiceChangeRecord::getLastName, Comparator.nullsLast(Comparator.reverseOrder()))
							.thenComparing(PayrollSelfServiceChangeRecord::getFirstName, Comparator.nullsLast(Comparator.reverseOrder()))
							.thenComparing(PayrollSelfServiceChangeRecord::getMiddleInitial, Comparator.nullsLast(Comparator.reverseOrder()))	
							.thenComparing(PayrollSelfServiceChangeRecord::getEffectiveDate, Comparator.nullsLast(Comparator.reverseOrder()))
							).collect(Collectors.toList()));
		} else {
			report.setDetails((Collection) report.getDetails().stream()
					.sorted(Comparator.comparing(PayrollSelfServiceChangeRecord::getLastName, Comparator.nullsFirst(Comparator.naturalOrder()))
							.thenComparing(PayrollSelfServiceChangeRecord::getFirstName, Comparator.nullsFirst(Comparator.naturalOrder()))
							.thenComparing(PayrollSelfServiceChangeRecord::getMiddleInitial, Comparator.nullsFirst(Comparator.naturalOrder()))	
							.thenComparing(PayrollSelfServiceChangeRecord::getEffectiveDate, Comparator.nullsFirst(Comparator.naturalOrder()))
							).collect(Collectors.toList()));
		}	       

	}

	private static void sortByInitiated(PayrollSelfServiceChangesReportData report, boolean isDescendingSort) {
		if(isDescendingSort) {
			report.setDetails((Collection) report.getDetails().stream()
					.sorted(Comparator.comparing(PayrollSelfServiceChangeRecord::getCreatedDate, Comparator.nullsLast(Comparator.reverseOrder()))
							.thenComparing(PayrollSelfServiceChangeRecord::getEffectiveDate, Comparator.nullsLast(Comparator.reverseOrder())
									)).collect(Collectors.toList()));
		} else {
			report.setDetails((Collection) report.getDetails().stream()
					.sorted(Comparator.comparing(PayrollSelfServiceChangeRecord::getCreatedDate, Comparator.nullsFirst(Comparator.naturalOrder()))
							.thenComparing(PayrollSelfServiceChangeRecord::getEffectiveDate, Comparator.nullsFirst(Comparator.naturalOrder())
									)).collect(Collectors.toList()));
		}

	}

	private static void sortByEffectiveDate(PayrollSelfServiceChangesReportData report, boolean isDescendingSort) {
		if(isDescendingSort) {
			report.setDetails((Collection) report.getDetails().stream()
					.sorted(Comparator.comparing(PayrollSelfServiceChangeRecord::getEffectiveDate, Comparator.nullsLast(Comparator.reverseOrder()))
							.thenComparing(PayrollSelfServiceChangeRecord::getLastName, Comparator.nullsLast(Comparator.reverseOrder()))
							.thenComparing(PayrollSelfServiceChangeRecord::getFirstName, Comparator.nullsLast(Comparator.reverseOrder()))	
							.thenComparing(PayrollSelfServiceChangeRecord::getMiddleInitial, Comparator.nullsLast(Comparator.reverseOrder()))
							).collect(Collectors.toList()));
		} else {
			report.setDetails((Collection) report.getDetails().stream()
					.sorted(Comparator.comparing(PayrollSelfServiceChangeRecord::getEffectiveDate, Comparator.nullsFirst(Comparator.naturalOrder()))
							.thenComparing(PayrollSelfServiceChangeRecord::getLastName, Comparator.nullsFirst(Comparator.naturalOrder()))
							.thenComparing(PayrollSelfServiceChangeRecord::getFirstName, Comparator.nullsFirst(Comparator.naturalOrder()))	
							.thenComparing(PayrollSelfServiceChangeRecord::getMiddleInitial, Comparator.nullsFirst(Comparator.naturalOrder()))
							).collect(Collectors.toList()));
		}
	}
	
	/**
	 * Calculate the Enrollment Effective Date.
	 * 
	 *  If both Eligible Plan Entry Date and Plan Entry Date are null, return null.
	 *  If eligiblePlanEntryDate is not null, return the latest between eligiblePlanEntryDate and enrollmentProcessedDate
	 *  Otherwise if planEntryDate is not null, return the latest between planEntryDate and enrollmentProcessedDate
	 * 
	 * @param planEntryDate the Plan Entry Date
	 * @param eligiblePlanEntryDate the Eligible Plan Entry Date
	 * @param enrollmentProcessedDate the Enrollment Processed Date
	 * 
	 * @return Enrollment Effective Date as <code>java.sql.Date</code>
	 */
	public static Date calculateEnrollmentEffective(Date planEntryDate, Date eligiblePlanEntryDate, Date enrollmentProcessedDate) {
		
		if (null == eligiblePlanEntryDate &&  null == planEntryDate) return null;
		
		Date entryDateToCompare = ObjectUtils.firstNonNull(eligiblePlanEntryDate, planEntryDate);
		if (enrollmentProcessedDate == null) return entryDateToCompare;
		
		return (entryDateToCompare.compareTo(enrollmentProcessedDate) > 0) ? entryDateToCompare : enrollmentProcessedDate;
		
	}

	/**
	 * Method to sort activity history report
	 * 
	 * @param report
	 */

	public static void sortActivityHistoryReportDetails(ReportData report) {
		if (report == null || CollectionUtils.isEmpty(report.getDetails())) {
			return;
		}
		report.setDetails((Collection) report.getDetails().stream().sorted(Comparator
				.comparing(PayrollSelfServiceChangeRecord::getEffectiveDate,
						Comparator.nullsFirst(Comparator.naturalOrder()))
				.thenComparing(PayrollSelfServiceChangeRecord::getLastName, Comparator.nullsFirst(Comparator.naturalOrder()))
				.thenComparing(PayrollSelfServiceChangeRecord::getFirstName,
						Comparator.nullsFirst(Comparator.naturalOrder()))
				.thenComparing(PayrollSelfServiceChangeRecord::getMiddleInitial,
						Comparator.nullsFirst(Comparator.naturalOrder()))
				.thenComparing(PayrollSelfServiceChangeRecord::getDescription,
						Comparator.nullsFirst(Comparator.naturalOrder())))
				.collect(Collectors.toList()));
	}

}
