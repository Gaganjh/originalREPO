package com.manulife.pension.ps.web.participant.payrollSelfService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangeRecord;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;

public class PayrollSelfServiceChangesWebUtility {
	
	private static final Logger logger = Logger.getLogger(PayrollSelfServiceChangesWebUtility.class);
	private static final int MAXIMUM_DECIMAL_DIGIT_FOR_AMOUNT_DISPLAY	= 2;
	private static final int MAXIMUM_DECIMAL_DIGIT_FOR_PERCENT_DISPLAY 	= 3;
	private static final String REPORTED_VALUE_DATE_FORMAT = "MMddyyyy";
	
	private PayrollSelfServiceChangesWebUtility() {}
	
	protected static boolean requireSSNMask(UserProfile userProfile) {
		boolean maskSsnFlag = true;
		try {
			maskSsnFlag = ReportDownloadHelper.isMaskedSsn(userProfile, userProfile.getCurrentContract().getContractNumber());

		} catch (SystemException se) {
			logger.error(se);
		}
		return maskSsnFlag;
	}

	
	protected static boolean allowedToAccessPSS(UserProfile userProfile) {	
		return userProfile.isSelectedAccess() ? false : true ;		
	}
	
	public static String getFormattedNumber(Object value, String valueType) {
		if(value == null) {
			return null;
		}
		DecimalFormat df = new DecimalFormat();
		df.setGroupingUsed(false);

		if(PayrollSelfServiceChangeRecord.VALUE_TYPE_PERCENTAGE_CODE.equals(valueType)) {
			df.setMaximumFractionDigits(MAXIMUM_DECIMAL_DIGIT_FOR_PERCENT_DISPLAY);
		} else {
			df.setMaximumFractionDigits(MAXIMUM_DECIMAL_DIGIT_FOR_AMOUNT_DISPLAY);
			df.setMinimumFractionDigits(MAXIMUM_DECIMAL_DIGIT_FOR_AMOUNT_DISPLAY);
		}
		return df.format(value);
	}
	
	public static String getFormattedValue(Object rawValue, String valueTypeCode) {
		if(rawValue == null) {
			return null;
		}
		
		if(java.util.Date.class.isAssignableFrom(rawValue.getClass())) {
			return new SimpleDateFormat(REPORTED_VALUE_DATE_FORMAT).format(rawValue);
		}
		
		return getFormattedNumber(rawValue, valueTypeCode);		
	}
}
