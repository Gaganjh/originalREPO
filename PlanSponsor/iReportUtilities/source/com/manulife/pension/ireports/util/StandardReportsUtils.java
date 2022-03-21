package com.manulife.pension.ireports.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.ireports.report.ReportFormattingConstants;
import com.manulife.util.tools.InvalidArgumentException;

public class StandardReportsUtils implements StandardReportsConstants {

	private StandardReportsUtils() {
	}

	public static String[] convertDelimitedListToArray(String delimitedListString, String delimiter) {
		StringTokenizer tokenizer = new StringTokenizer(delimitedListString, delimiter);
		String[] array = new String[tokenizer.countTokens()];
		int i=0;
		while(tokenizer.hasMoreTokens()){
			array[i] = tokenizer.nextToken();
			i++;
		}
		return array;
	}

	/**
	 * @param value pass in exact percent value, i.e. 0.2 will return 0.20% (not 20%)
	 * @return value formatted as %
	 */
	public static String formatPercentage(BigDecimal value) {
		if (value == null){
			return "n/a";
		}
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		percentFormat.setMaximumFractionDigits(2);
		percentFormat.setMinimumFractionDigits(2);
		return percentFormat.format(value.doubleValue() / 100);
	}
	
	/**
	 * @param value in range from -1 to 1, i.e. 0.2 will return 20.00%
	 * @return value formatted as %
	 */
	public static String formatPercentageZeroToOneRange(BigDecimal value) {
		if (value == null){
			return "n/a";
		}
		NumberFormat percentFormat = NumberFormat.getPercentInstance();
		percentFormat.setMaximumFractionDigits(2);
		percentFormat.setMinimumFractionDigits(2);
		return percentFormat.format(value.doubleValue());
	}

	public static String formatFundDataDate(final Date effectivedate) {
		return effectivedate == null ?
				"n/a" :
				DateFormatUtils.format(effectivedate, ReportFormattingConstants.DATE_PATTERN_FUND_DATA);
	}

	public static String formatFundDataDecimal(final BigDecimal value) {
		if (value == null){
			return "n/a";
		}
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setMinimumFractionDigits(2);
		return numberFormat.format(value.doubleValue());
	}

	public static String formatFundDataString(final String ratetype) {
		return ratetype == null ? "n/a" : ratetype;
	}
	
	public static Date adjustDateToAPreviousMonthEnd(Date date, int monthsBack) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		for (int i = 0; i < monthsBack; i++) {
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		return cal.getTime();
	}
	
	public static Map trimEmptyKeys(Map map) {
		Map trimmedMap = new LinkedMap();
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			Map.Entry currentEntry = (Map.Entry) iter.next();
			String trimmedMapKey = StringUtils.trim((String) currentEntry.getKey());
			if (!StringUtils.isEmpty(trimmedMapKey)){
				trimmedMap.put(currentEntry.getKey(), currentEntry.getValue());
			}
		}
		return trimmedMap;
	}

	public static String getStackTraceString(Exception e) throws IOException {
		String returnValue = "";
		ByteArrayOutputStream os = null;
		PrintStream ps = null;
		try {
			os = new ByteArrayOutputStream();
			ps = new PrintStream(os);
			e.printStackTrace(ps);
			ps.flush();
			returnValue = os.toString();
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (os != null) {
				os.close();
			}
		}
		return returnValue;
	}
	
	public static boolean isHypothetical(Date inception, Date effective, int years) throws InvalidArgumentException {
		if(inception == null || effective == null) {
			throw new InvalidArgumentException();
		}
		
		if(effective.getTime() > inception.getTime()+ ((long) years*1000*60*60*24*365)) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isNewYork(String companyId) {
		return COMPANY_ID_NY.equals(companyId);
	}

	public static String sitecodeToCompanyId(String sitecode) {
		return (STANDARDREPORTS_COMPANY_NY.equals(sitecode) ? COMPANY_ID_NY : COMPANY_ID_USA);
	}

	public static String companyIdToSitecode(String companyId) {
		return isNewYork(companyId) ? STANDARDREPORTS_COMPANY_NY : STANDARDREPORTS_COMPANY_USA;
	}

    /**
     * This method returns "n/a" if the parameter passed is null, else, it returns the value.
     * 
     * @param val
     * @return
     */
    public static String getValueorNA(String val) {
        if (StringUtils.isBlank(val)) {
            return StandardReportsConstants.NA;
        } else {
            return val;
        }
    }
    
    /**
     * This method returns emplty space if the parameter passed is null, else, it returns the value.
     * 
     * @param val
     * @return
     */
    public static String getValueorBlank(String val) {
        if (StringUtils.isBlank(val)) {
            return StandardReportsConstants.BLANK;
        } else {
            return val;
        }
    }
    
    /**
     * validates whether the given String is a valid decimal value.
     * If TRUE, returns the formatted value
     * If FALSE, returns the given String  
     * 
     * @param valueAsString
     * @return
     */
    public static String formatPercentage(String valueAsString) {
    	
    	BigDecimal valueAsBigDecimal = null;
    	
    	if (valueAsString != null) {
	    	try {
	    		valueAsBigDecimal = new BigDecimal(valueAsString);
	    	} catch(NumberFormatException ne) {
	    		return valueAsString;
	    	}
    	}
    	
    	return formatPercentage(valueAsBigDecimal);
    }
    
}
