package com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ObjectUtils;

public class PayrollSelfServiceEnrollmentRecord extends PayrollSelfServiceChangeRecord {

	private static final long serialVersionUID = 1L;

	private static final Map<String, String> ENROLLMENT_METHOD_DESCRIPTION_MAP = Stream
			.of(new String[][] { { "I", "Internet" }, { "A", "Automatic" }, { "P", "Paper" }, })
			.collect(Collectors.collectingAndThen(Collectors.toMap(data -> data[0], data -> data[1]),
					Collections::<String, String>unmodifiableMap));

	@Override
	public String getDescription() {
		return "Enrollment";
	}

	@Override
	public String getInitiatedBy() {
		if(PayrollSelfServiceChangeRecord.ONLINE_ENROLLMENT_METHOD_CODE.equalsIgnoreCase(this.getCreatedSourceCode())) {
			return this.getParticipantName();
		}
		return JOHN_HANCOCK;
	}

	@Override
	public String getInitiationMethod() {
		return ObjectUtils.firstNonNull(
				ENROLLMENT_METHOD_DESCRIPTION_MAP.get(this.getCreatedSourceCode()),
				this.getCreatedSourceCode()
				);
	}

	@Override
	public boolean isWarningIconApplicable() {
		return Objects.isNull(this.getEffectiveDate());
	}
}
