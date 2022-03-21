package com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ObjectUtils;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class PayrollSelfServiceDeferralRecord extends PayrollSelfServiceChangeRecord {

	private static final long serialVersionUID = 1L;

	static final Map<String, String> DEFERRAL_INITIATION_METHOD_DESCRIPTION_MAP = Stream
			.of(new String[][] { { "CI", "Scheduled" }, { "AC", "Standalone" }}).collect(Collectors.collectingAndThen(
					Collectors.toMap(data -> data[0], data -> data[1]), Collections::<String, String>unmodifiableMap));

	private static final Map<String, String> DEFERRAL_STATUS_DESCRIPTION_MAP = Stream
			.of(new String[][] { { "PA", "Pending" }, { "AP", "Approved" }, { "CN", "Cancelled" }}).collect(Collectors.collectingAndThen(
					Collectors.toMap(data -> data[0], data -> data[1]), Collections::<String, String>unmodifiableMap));

	@Override
	public String getDescription() {
		return "Deferral Rate Change";
	}

	@Override
	public String getInitiationMethod() {
		return ObjectUtils.firstNonNull(
				PayrollSelfServiceDeferralRecord.DEFERRAL_INITIATION_METHOD_DESCRIPTION_MAP.get(this.getCreatedSourceCode()),
				this.getCreatedSourceCode()
				);
	}
	
	@Override
	public String getStatusDescription() {
		return ObjectUtils.firstNonNull(
				PayrollSelfServiceDeferralRecord.DEFERRAL_STATUS_DESCRIPTION_MAP.get(this.getStatusCode()),
				this.getStatusCode()
				);
	}
	
	@Override
	public boolean isWarningIconApplicable() {
		return Objects.isNull(this.getEffectiveDate());
	}

	@Override
	public boolean isInitiatedTimeReported() {
		return true;
	}
	
	@Override
	public boolean isProcessingDetailReported() {
		return true;
	}

}
