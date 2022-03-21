package com.manulife.pension.service.loan.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.util.JdbcHelper;

/**
 * Helper methods specific to Loan Document Generation
 * 
 * @author JThangad
 * 
 */
public class LoanDocumentHelper {

	private final static DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat(
			"###.###");

	private final static String COMPANY_USA = "USA";

	private final static String COMPANY_NY = "NY";

	public final static String COMPANY_ID_MAN_USA = GlobalConstants.MANULIFE_CONTRACT_ID_FOR_USA;

	public final static String COMPANY_ID_MAN_NY = GlobalConstants.MANULIFE_CONTRACT_ID_FOR_NY;
	
	public static synchronized String formatPercentageFormatter(BigDecimal value) {
        return PERCENTAGE_FORMAT.format(value);
    }

	/**
	 * private method to process Non - String data types
	 * 
	 * @param obj
	 * @return java.lang.String
	 */
	public static String getStringValue(Object obj) {

		String stringValue = "";

		if (obj != null) {
			if (obj instanceof java.lang.Boolean) {
				boolean bool = ((Boolean) obj).booleanValue();
				if (bool) {
					stringValue = JdbcHelper.INDICATOR_YES;
				} else {
					stringValue = JdbcHelper.INDICATOR_NO;
				}
			} else if (obj instanceof Integer) {
				stringValue = Integer.toString(((Integer) obj));
			} else if (obj instanceof BigDecimal) {
				stringValue = obj.toString();
			} else {
				obj.toString();
			}
		}
		return stringValue;
	}

	/**
	 * Gets payment frequency according to the payment frequency code
	 * 
	 * @param String
	 * @return java.lang.String
	 */
	public static String paymentFrequency(String paymentFrequency) {
		if (paymentFrequency != null) {
			if (paymentFrequency.equals(GlobalConstants.FREQUENCY_TYPE_WEEKLY)) {
				paymentFrequency = GlobalConstants.FREQUENCY_TYPE_WEEKLY_DESC;
			} else if (paymentFrequency
					.equals(GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY)) {
				paymentFrequency = GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY_DESC;
			} else if (paymentFrequency
					.equals(GlobalConstants.FREQUENCY_TYPE_SEMI_MONTHLY)) {
				paymentFrequency = GlobalConstants.FREQUENCY_TYPE_SEMI_MONTHLY_DESC;
			} else if (paymentFrequency
					.equals(GlobalConstants.FREQUENCY_TYPE_MONTHLY)) {
				paymentFrequency = GlobalConstants.FREQUENCY_TYPE_MONTHLY_DESC;
			}
		}
		return paymentFrequency;
	}

	/**
	 * formatCurrency method generates a String formatted for display:
	 * 
	 * @param BigDecimal
	 * @return java.lang.String
	 */

	public static String currencyFormatter(BigDecimal value) {
		String stringValue = "";
		if (value != null) {
			NumberFormat format = NumberFormat.getNumberInstance();
			format.setMaximumFractionDigits(2);
			format.setMinimumFractionDigits(2);
			stringValue = format.format(value);
		}
		return stringValue;
	}

	/**
	 * Formats the value into percentage format according to the bussiness rules
	 * 
	 * @param BigDecimal
	 * @return java.lang.String
	 */
	public static String percentageFormatter(BigDecimal value) {
		String stringValue = "";
		if (value != null) {
			stringValue = formatPercentageFormatter(value);
		}
		return stringValue;
	}

	/**
	 * Formats the Date according to the given format
	 * 
	 * @param Date
	 * @return java.lang.String
	 */
	public static String dateFormatter(Date value, SimpleDateFormat dateFormat) {
		String stringValue = "";
		if (value != null) {
			stringValue = dateFormat.format(value);
		}
		return stringValue;
	}

	/**
	 * Formats the ssn for display
	 * 
	 * @param String
	 * @return java.lang.String
	 */
	public static String getFormattedSSN(String ssn) {

		StringBuffer buf = new StringBuffer();

		if (ssn != null && ssn.length() == 9) {
			buf.append(ssn.substring(0, 3));
			buf.append("-");
			buf.append(ssn.substring(3, 5));
			buf.append("-");
			buf.append(ssn.substring(5));
		}

		return buf.toString();
	}

	/**
	 * Gives the company name according to the code
	 * 
	 * @param String
	 * @return java.lang.String
	 */
	public static String getCompanyName(String manulifeId) {
		String stringValue = "";
		if (manulifeId != null) {
			if (manulifeId.equals(COMPANY_ID_MAN_USA)) {
				stringValue = COMPANY_USA;
			} else if (manulifeId.equals(COMPANY_ID_MAN_NY)) {
				stringValue = COMPANY_NY;
			}
		}
		return stringValue;
	}

	/**
	 * Gives a sorted list
	 * 
	 * @param value
	 * @return
	 */
	public static List<String> getSortedList(List<String> value) {

		Collections.sort(value);

		return value;
	}

	/**
	 * Gives loan type text
	 * 
	 * @param loanType
	 * @return
	 */
	public static String getLoanTypeText(String loanType) {
		String stringValue = "";
		if (LoanConstants.TYPE_GENERAL_PURPOSE.equals(loanType)) {
			stringValue = LoanConstants.TYPE_GENERAL_PURPOSE_DESC;
		} else if (LoanConstants.TYPE_HARDSHIP.equals(loanType)) {
			stringValue = LoanConstants.TYPE_HARDSHIP_DESC;
		} else if (LoanConstants.TYPE_PRIMARY_RESIDENCE.equals(loanType)) {
			stringValue = LoanConstants.TYPE_PRIMARY_RESIDENCE_DESC;
		}
		return stringValue;
	}

	/**
	 * Formats the participant name
	 * 
	 * @param firstName
	 * @param lastName
	 * @param middleInitial
	 * @return
	 */
	public static String getFormattedParticipantName(String firstName,
			String lastName, String middleInitial) {

		StringBuffer buf = new StringBuffer();
		buf.append(lastName.trim());
		buf.append(", ");
		buf.append(firstName);

		if (middleInitial != null && middleInitial.length() > 0) {
			buf.append(", ");
			buf.append(middleInitial);
		}

		return buf.toString();
	}
	
	/**
	 * Formats the participant name according to requirement LPD:74 
	 * 
	 * @param firstName
	 * @param lastName
	 * @param middleInitial
	 * @return
	 */
	public static String getFormattedParticipantNameAO(String firstName,
			String lastName, String middleInitial) {
		StringBuffer buf = new StringBuffer();
		buf.append(firstName.trim());

		if (middleInitial != null && middleInitial.length() > 0) {
			buf.append(" ");
			buf.append(middleInitial);
		}

		buf.append(" ");
		buf.append(lastName.trim());

		return buf.toString();
	}

	/**
	 * Formats the zip code
	 * 
	 * @param zipCode
	 * @return
	 */
	public static String getFormattedZipCode(String zipCode) {

		StringBuffer buf = new StringBuffer();

		if (zipCode != null && zipCode.length() > 5) {
			buf.append(zipCode.substring(0, 5));
			buf.append("-");
			buf.append(zipCode.substring(5));
		}

		return buf.toString();
	}

	/**
	 * Formats the address
	 * 
	 * @param addressLine1
	 * @param addressLine2
	 * @param city
	 * @param stateCode
	 * @param zipCode
	 * @return
	 */
	public static String getFormattedAddress(String addressLine1,
			String addressLine2, String city, String stateCode, String zipCode, String country) {

		StringBuffer buf = new StringBuffer();
		if (addressLine1 != null) {
			buf.append(addressLine1.trim());
		}
		if (addressLine2 != null) {
			buf.append(", ");
			buf.append(addressLine2.trim());
		}
		if (city != null) {
			buf.append(", ");
			buf.append(city.trim());
		}
		if (stateCode != null) {
			buf.append(", ");
			buf.append(stateCode.trim());
		}
		if (zipCode != null) {
			buf.append(", ");
			if (zipCode.length() > 5) {
				StringBuffer zip = new StringBuffer();
				zip.append(zipCode.substring(0, 5));
				zip.append("-");
				zip.append(zipCode.substring(5));
				buf.append(zip);
			}
			if (zipCode.length() <= 5) {
				buf.append(zipCode.trim());
			}
		}
		
		if (country != null
				&& !GlobalConstants.COUNTRY_CODE_USA.equalsIgnoreCase(country)) {
			String countryName = getCountryName(country);

			if (countryName != null) {
				buf.append(", ");
				buf.append(countryName);
			}
		}
		
		return buf.toString();
	}
	
	/**
	 * Gets contry namefrom contry code
	 * 
	 * @param countrycode
	 * @return
	 */
	public static String getCountryName(String country) {

		String countryName = null;
		EnvironmentServiceDelegate environmentServiceDelegate;
		environmentServiceDelegate = EnvironmentServiceDelegate
				.getInstance("PS");
		Map<String, String> countryList = new HashMap<String, String>();
		try {
			countryList = environmentServiceDelegate.getCountries();
		} catch (SystemException e) {
			throw new RuntimeException(
					"SystemException thrown by EnvironmentServiceDelegate.getCountries()",
					e);
		}

		if (countryList != null) {
			countryName = countryList.get(country);
		}
		return countryName;
	}
}
