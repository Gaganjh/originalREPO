package com.manulife.pension.ps.service.report.participant.payrollselfservice.dao;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Data Access for PayrollSelfService Deferrals for Activity history report
 *
 */
public class PayrollSelfServiceActivityHistoryReportDeferralDAO extends PayrollSelfServiceDeferralDAO {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(PayrollSelfServiceActivityHistoryReportDeferralDAO.class);

	
	/**
	 * Default Constructor
	 */
	public PayrollSelfServiceActivityHistoryReportDeferralDAO() {
		// empty constructor - sonarqube rules
	}

	@Override
	protected String createFilterPhrase(PayrollSelfServiceSearchCriteria searchCriteria) {
		StringBuilder filterBuilder = new StringBuilder();
		if (StringUtils.isNotEmpty(searchCriteria.getLastNameValue())) {
			filterBuilder.append(" AND EC.last_name LIKE ")
				.append(wrapInSingleQuotes(searchCriteria.getLastNameValue().toUpperCase().concat("%")));
		} else if (StringUtils.isNotEmpty(searchCriteria.getSsnValue())) {
			filterBuilder.append(" AND EC.SOCIAL_SECURITY_NO = ")
				.append(wrapInSingleQuotes(searchCriteria.getSsnValue()));
		}
		
		if (StringUtils.isNotEmpty(searchCriteria.getEffectiveDateFrom())) {
			filterBuilder.append(" AND PCI.EFFECTIVE_DATE >= date(")
				.append(wrapInSingleQuotes(searchCriteria.getEffectiveDateFrom())).append(")");
		}
		
		if (StringUtils.isNotEmpty(searchCriteria.getEffectiveDateTo())) {
			filterBuilder.append(" AND PCI.EFFECTIVE_DATE <= date(")
				.append(wrapInSingleQuotes(searchCriteria.getEffectiveDateTo())).append(")");
		}
		
		return filterBuilder.toString();
	}

}


