package com.manulife.pension.platform.web.util;

/**
 * This class contains the data utility methods copied from ezk

 * LS June 2006 - added multiple fundsheet logic - for ARABD and ARABDY productId go to /c2
 * for any other product id - c5 directory under fundsheet root
 * 
 * ME Dec 16, 2008 - Refactored getFundSheetURL to use getFundClassNumber instead of deprecated getFundClassShortName
 */

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.web.taglib.render.ZipCodeFormatTag;

public class DataUtility {

    private static final String[] G_FUND = { "3YC", "5YC", "10YC" };

    private static final String G_FUND_TYPE = "ga";

    private static CommonEnvironment env = CommonEnvironment.getInstance();

    private static final String SUFFIX = "a";

    public static final BigDecimal ZERO_VALUE = new BigDecimal("0.000");

    public static final BigDecimal FULL_VALUE = new BigDecimal("100.0");

    private static Map<String, String> countryList = null;

    private static Map<String, String> stateList = null;
    /**
     * DataUtility constructor comment.
     */
    public DataUtility() {
        super();
    }

    public static boolean isPositiveValue(BigDecimal value) {
        boolean result = false;

        if ((value != null) && (value.compareTo(ZERO_VALUE) > 0)) {
            result = true;
        }
        return result;
    }

    public static boolean isZeroValue(BigDecimal value) {
        boolean result = false;

        if ((value == null) || (value.compareTo(ZERO_VALUE) == 0)) {
            result = true;
        }
        return result;
    }

    public static String getFundSheetCode(String fundID) {

        String fundcode = fundID.trim().toLowerCase().concat(SUFFIX);

        if (Arrays.asList(G_FUND).contains(fundID.trim())) {
            fundcode = G_FUND_TYPE;
        }

        return fundcode;
    }

    /**
     * 
     * @param productId - eg ARA06
     * @param fundType - eg GA - to tell if guaranteed fund or not.
     * @param fundID - The fund id.
     * @param rateType - The rate type (AKA: class) of fund.
     * @param fundSeries - determines if the series is multi-class or not. Older series have a tweak
     *            to determine their class.
     * @param siteLocation - the site location
     * 
     * @return String containing the URL of the fundsheet requested.
     *//*
    public static String getFundSheetURL(String productId, String fundType,
            String fundID,
            String rateType, String fundSeries, String siteLocation) {

        String fundDirectory = fundID.trim().toLowerCase().concat(SUFFIX);
        String fileName = "/index.html";
        try {

            
             * ME DEC2008: I have modified this to add the "C" on, as Marketing
             * URLs still use this. However we no longer use C01 .. C09 for
             * anything on the web...other than fundsheet URLs. I have
             * refactored this code to use the getFundClassNumber method rather
             * than the deprecated getFundClassShortName. It is deprecated
             * because the business no longer wants the "C0#" version displayed
             * anywhere, but would rather just the number # part.
             

            String fundsheetUrl = env.getFundSheetURL().concat("/").concat(
                    siteLocation);

            // Determine the integer part of the fund class.
            String fundClassNumber = null;
            if (CommonConstants.FUND_PACKAGE_MULTICLASS
                    .equalsIgnoreCase(fundSeries)) {
                fundClassNumber = FundClassUtility.getInstance()
                        .getFundClassNumber(rateType);
            } else if (CommonConstants.BD_PRODUCT_ID
                    .equalsIgnoreCase(productId)
                    || CommonConstants.BD_PRODUCT_NY_ID
                            .equalsIgnoreCase(productId)) {
                fundClassNumber = FundClassUtility.getInstance()
                        .getFundClassNumber("CL2");
            } else {
                fundClassNumber = FundClassUtility.getInstance()
                        .getFundClassNumber("CL5");
            }

            
             * Now we will pad the fund class number with a leading zero and
             * take the right-most 2 digits. eg 1) "0".concat("9") = "09", which
             * is then used to make "C09". eg 2) "0".concat("10") = "010", which
             * is then taken care of by the if length=3 section to make "C10".
             
            fundClassNumber = "0".concat(fundClassNumber);
            if (fundClassNumber.length() == 3) {
                // In case we ever go to class C10 or higher, we take the
                // substring(1) of "010" to give "10".
                fundClassNumber = fundClassNumber.substring(1);
            }
            // Create the directory from the padded fundClassNumber by adding a
            // "C" to the front.
            String fundClassDirectory = "/C".concat(fundClassNumber);

            // Concatenate the class directory onto the URL.
            fundsheetUrl = fundsheetUrl.concat(fundClassDirectory).concat("/");

            // Special handling for guaranteed funds
            
            if (Arrays.asList(CommonConstants.GA_FUND_TYPE).contains(
                    fundType.trim())) {
                if (siteLocation.equals(CommonConstants.SITEMODE_USA))
                    fundDirectory = USA_G_FUND_CODE;
                else
                    fundDirectory = NY_G_FUND_CODE;
            }

            if (Arrays.asList(CommonConstants.PBA_FUND_TYPE).contains(
                    fundID.trim())) {
                return "nothingimplemented"; // This is kinda crappy code!
            } else {
                // Replace '&' in fundDirectory with an underscore '_'
                fundsheetUrl = fundsheetUrl.concat(
                        fundDirectory.replace('&', '_')).concat(fileName);
                // System.out.println(fundsheetUrl);
                return fundsheetUrl;
            }
        } catch (Exception e) {
            return CommonConstants.DUMMY_URL.concat(fundDirectory.replace('&',
                    '_'));
        }

    }

    *//**
     * 
     * @param fundType - eg GA - to tell if guaranteed fund or not.
     * @param fundID - The fund id.
     * @param fundClassShortName - The fund class short name
     * @param siteLocation - the site location
     * 
     * @return String containing the URL of the fundsheet requested.
     *//*
    public static String getFundSheetURL(String fundType,
            String fundID,
            String fundClassShortName,
            String siteLocation) {

        String fundDirectory = fundID.trim().toLowerCase().concat(SUFFIX);
        String fileName = "/index.html";
        try {

            String fundsheetUrl = env.getFundSheetURL().concat("/").concat(
                    siteLocation).concat("/");

            // Concatenate the class directory onto the URL.
            fundsheetUrl = fundsheetUrl.concat(fundClassShortName).concat("/");

            // Special handling for guaranteed funds
            if (Arrays.asList(CommonConstants.GA_FUND_TYPE).contains(
                    fundType.trim())) {
                if (siteLocation.equals(CommonConstants.SITEMODE_USA))
                    fundDirectory = USA_G_FUND_CODE;
                else
                    fundDirectory = NY_G_FUND_CODE;
            }

            if (Arrays.asList(CommonConstants.PBA_FUND_TYPE).contains(
                    fundID.trim())) {
                return "nothingimplemented"; // This is kinda crappy code!
            } else {
                // Replace '&' in fundDirectory with an underscore '_'
                fundsheetUrl = fundsheetUrl.concat(
                        fundDirectory.replace('&', '_')).concat(fileName);
                // System.out.println(fundsheetUrl);
                return fundsheetUrl;
            }
        } catch (Exception e) {
            return CommonConstants.DUMMY_URL.concat(fundDirectory.replace('&',
                    '_'));
        }
    }
    */
    

    public static boolean isApolloDateValid(Date date) {
        Calendar caldate = Calendar.getInstance();
        caldate.setTime(date);
        if ((date == null) || (caldate.get(Calendar.YEAR) == 9999))
            return false;
        return true;
    }

    public static String getLongDate(String dateString, String pattern) {

        int year = Integer.parseInt(dateString.substring(0, 4));
        int month = Integer.parseInt(dateString.substring(5, 7));
        int day = Integer.parseInt(dateString.substring(8));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatDate = new SimpleDateFormat(pattern);
        cal.set(year, month - 1, day);
        return formatDate.format(cal.getTime());

    }

    public static String compute8DigitContractNumber(int number) {
        StringBuffer contractNumber = new StringBuffer(Integer.toString(number));
        int checkDigit = 0;
        int weight = 0;
        int digit = 0;
        int sum = 0;
        for (int i = 0; i < contractNumber.length(); i++) {
            if (i % 2 == 0) {
                weight = 2;
            } else {
                weight = 1;
            }
            digit = Integer.parseInt(contractNumber.substring(contractNumber
                    .length()
                    - i - 1, contractNumber.length() - i));
            // multiply by it's weight
            digit *= weight;
            if (digit > 9) {
                String tmp = Integer.toString(digit);
                // reset to 0
                digit = 0;
                for (int j = 0; j < tmp.length(); j++) {
                    digit += Integer.parseInt(tmp.substring(j, j + 1));
                }
            }
            // add the number to the sum.
            sum += digit;
        }
        int remainder = sum % 10;
        checkDigit = remainder == 0 ? 0 : Math.abs(remainder - 10);
        return contractNumber.append("-").append("00").append("-").append(
                Integer.toString(checkDigit)).toString();
    }

    /**
     * Removes all trailimg zeros, eg. 100.1200 will return 100.12 100.0000 will
     * return 100
     * 
     * @param value
     * @return
     */
    public static String formatValueWithoutTrailingZeros(String value) {
        int dotIndex = value.indexOf(".");
        if (value.startsWith("0")) {
            int i = 0;
            int stopPosition = value.length();
            if (dotIndex != -1) {
                stopPosition = dotIndex - 1;
            }
            int zeroCounter = 0;
            while (i < stopPosition) {
                if (value.charAt(i) == '0')
                    zeroCounter++;
                else
                    break;
                i++;
            }
            // always include at least one significant digit
            if (zeroCounter != value.length()
                    && value.charAt(zeroCounter) == '.') {
                zeroCounter--;
                value = value.substring(zeroCounter, stopPosition);
            } else if (zeroCounter == value.length()) {
                value = value.substring(zeroCounter - 1, stopPosition);
            }
        }

        if (dotIndex > -1) {
            String decimalPart = value.substring(dotIndex + 1, value.length());
            String wholePart = value.substring(0, dotIndex);
            int i = decimalPart.length() - 1;
            int trialingZeroCounter = 0;
            while (i >= 0) {
                if (decimalPart.charAt(i) == '0')
                    trialingZeroCounter++;
                else
                    break;
                i--;
            }
            if (trialingZeroCounter < decimalPart.length()) {
                value = wholePart
                        + "."
                        + decimalPart.substring(0, decimalPart.length()
                                - trialingZeroCounter);
            } else {
                value = wholePart;
            }

        }

        return value;
    }

    /**
     * formatCurrency method generates a String formatted for display:
     * 
     * @param String a valid unforatted string (e.g. 1,111.99999)
     * @param boolean
     * @return java.lang.String
     */
    public static String formatCurrency(String str) {
        return (numericStringFormatter(str, 2, true, false));
    }

    /**
     * formatCurrencyWithoutDecimals method generates a String formatted for
     * display: It will not include the decimal parts
     * 
     * @param String a valid unformatted string (e.g. 1,111.99999)
     * @param boolean
     * @return java.lang.String
     */
    public static String formatCurrencyWithoutDecimals(String str) {
        return (numericStringFormatter(str, 0, true, false));
    }

    /**
     * numericStringFormatter method generates a number formatted for display: -
     * it truncates at the provided number of decimal places and adds commas if
     * specified
     * 
     * @param String a valid unforatted number (e.g. 999999.99999, 88888)
     * @return java.lang.String
     */
    protected static String numericStringFormatter(String str,
            int numDecimalPlaces, boolean includeCommas, boolean suppressZeros) {
        String commaSym = ",";

        // Get rid of any beginning dollar signs
        if (str.startsWith("$")) {
            str = str.substring(1);
        }

        // the framework will handle any NumberFormatExceptions
        // Double curr = new Double(str);
        Double curr = new Double(str);

        // if zero suppression has been requested, return an empty string if the
        // number is zero
        if ((curr.doubleValue() == 0.0) && (suppressZeros)) {
            return ("");
        }

        // recreate the string from the Double to eliminate leading zeros
        str = curr.toString();
        // check for computerized scientific notation
        int posDec = str.indexOf(".");
        int posE = str.indexOf("E");

        if (posE > 0) {
            // System.out.println(str);
            int exponentValue = Integer.valueOf(str.substring(posE + 1))
                    .intValue();
            StringBuffer strAdjust = new StringBuffer(str.length());

            String coefficient = str.substring(0, posE);
            // get the integer part
            String intPartOfCoefficient = coefficient.substring(0, posDec);
            // get the decimal part
            String decPartOfCoefficient = coefficient.substring(posDec + 1,
                    posE);

            if (exponentValue < 0) {

                strAdjust.append("0.");
                int numZerosToPrefix = Math.abs(exponentValue
                        + intPartOfCoefficient.length());
                for (int i = 0; i < numZerosToPrefix; i++) {
                    strAdjust.append("0");
                }
                strAdjust.append(intPartOfCoefficient);
                strAdjust.append(decPartOfCoefficient);
            } else {
                // append the integer and decimal part
                strAdjust.append(intPartOfCoefficient);
                strAdjust.append(decPartOfCoefficient);

                // find the number of zeros to be postfix
                int numZerosToPostfix = Math.abs(exponentValue
                        - decPartOfCoefficient.length());
                for (int i = 0; i < numZerosToPostfix; i++) {
                    strAdjust.append("0");
                }
            }
            str = strAdjust.toString();
            // System.out.println(str);

        }
        // if there is no ".", add it prior to formatting
        int pos = str.indexOf(".");
        if (pos == -1 && numDecimalPlaces > 0) {
            str = str.concat(".00");
            pos = str.indexOf(".");
        }

        // do the formatting to place the decimal point and truncate any extra
        // decimals
        if (numDecimalPlaces > 0) {
            String str1 = str.substring(0, pos);
            String str2 = str.substring(pos + 1);

            int len = str2.length();
            if (len > numDecimalPlaces) {
                str2 = str.substring(pos + 1, pos + 1 + numDecimalPlaces);
            } else {
                for (int i = 0; i < numDecimalPlaces - len; i++) {
                    str2 = str2.concat("0");
                }
            }

            str = str1 + "." + str2;
        } else {
            if (pos != -1) {
                str = str.substring(0, pos);
            }
        }

        // create string buffers
        StringBuffer strbr = new StringBuffer(str);
        int len = strbr.length();

        StringBuffer strbr2 = new StringBuffer(len);

        // read the string from end to start, adding commas where needed
        if (includeCommas) {

            int offset;
            if (numDecimalPlaces == 0) {
                offset = 0;
            } else {
                offset = numDecimalPlaces + 1;
            }

            int count = 0;
            for (int i = len - 1; i >= 0; i--) {
                strbr2.append(strbr.charAt(i));
                if (i < len - offset) {
                    count += 1;
                }
                if (count == 3 && i > 0) {
                    strbr2.append(commaSym);
                    count -= 3;
                }
            }

            len = strbr2.length();
            StringBuffer strbr3 = new StringBuffer(len);
            for (int i = len - 1; i >= 0; i--) {
                strbr3.append(strbr2.charAt(i));
            }
            return strbr3.toString();
        } else {
            return str;
        }
    }
    
    /**
     * This method calculates the days between 2 dates.
     * 
     * @param d1
     * @param d2
     * @return
     */
    public static int daysBetween(Date d1, Date d2) {
        int secondsInOneDay = 1000 * 60 * 60 * 24;
        return (int) ((d2.getTime() - d1.getTime()) / secondsInOneDay);
    }

    /**
     * Retrieves the country name corresponding to the country code.
     * If country code is blank, null or invalid, the country name for USA 
     * will be returned.
     * @return country name
     */
    public static String getCountryName(String countryCode) {
        Map<String, String> countryMap = getCountryList();
        String countryName = countryMap.get(countryCode);
        
        if (countryName == null) {
            countryName = countryMap.get(GlobalConstants.COUNTRY_CODE_USA);
        }
        return countryName;
    }

    /**
     * Get the country list from environment service
     * @return
     */
    @SuppressWarnings("unchecked")
	private static Map<String, String> getCountryList() {
		if (countryList == null) {
			try {
				countryList = EnvironmentServiceDelegate.getInstance(
						CommonConstants.PS_APPLICATION_ID).getCountries();
			} catch (SystemException e) {
				throw new RuntimeException(
						"SystemException thrown by EnvironmentServiceDelegate.getCountries()",
						e);
			}
		}
		return countryList;
	}
    
    @SuppressWarnings("unchecked")
	private static Map<String, String> getStateList() {
		if (stateList == null) {
			try {
				stateList = EnvironmentServiceDelegate.getInstance(
						CommonConstants.PS_APPLICATION_ID).getUSAStates();
			} catch (SystemException e) {
				throw new RuntimeException(
						"SystemException thrown by EnvironmentServiceDelegate.getUSAStates()",
						e);
			}
		}
		return stateList;
	}

    /**
     * Formats an address for display as follows:
     *      addressLine1
     *      addressLine2 (if non-blank)
     *      city state_name formatted_zip_code
     *      country_name
     *      
     * @param isHtmlOutput
     * @param addressLine1
     * @param addressLine2
     * @param city
     * @param stateCode
     * @param zipCode
     * @param countryCode
     * @return
     */
    public static String formatAddressForDisplay(boolean isHtmlOutput,
            String addressLine1, String addressLine2, String city, String stateCode,
            String zipCode, String countryCode) {
        StringBuffer outAddress = new StringBuffer();
        String countryName = getCountryName(countryCode);
        String lineSeparator = "\n";
        String htmlLineSeparator = "<br>";
        
        if (isHtmlOutput) {
            lineSeparator = htmlLineSeparator;
        }
        
        outAddress.append(lineSeparator);
        outAddress.append(addressLine1 + lineSeparator);
        if (StringUtils.isNotBlank(addressLine2)) {
            outAddress.append(addressLine2 + lineSeparator);
        }
        outAddress.append(city + " ");
        if (GlobalConstants.COUNTRY_CODE_USA.equals(countryCode)) {
            outAddress.append(getStateList().get(stateCode) + " ");
            ZipCodeFormatTag zipCodeFormatter = new ZipCodeFormatTag();
            outAddress.append(zipCodeFormatter.doFormat(zipCode) + lineSeparator);
        } else {
            outAddress.append(stateCode + " ");
            outAddress.append(zipCode + lineSeparator);
        }
        outAddress.append(countryName);
        
        return outAddress.toString();
    }
    
    /**
	 * logic for retrieving the current quarter and year.
	 * 
	 * @param coFidQuarterEndDate
	 * @return String
	 */
    public static String yearOfCurrentQuarter(Date coFidQuarterEndDate){
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(coFidQuarterEndDate);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);
		String currentQuarter = (month >= Calendar.JANUARY && month <= Calendar.MARCH)? "Q1" :
		       (month >= Calendar.APRIL && month <= Calendar.JUNE)? "Q2" :
		       (month >= Calendar.JULY && month <= Calendar.SEPTEMBER)? "Q3" : "Q4";
		
		currentQuarter = String.valueOf(year)+"_"+currentQuarter;
		
		return currentQuarter;
	}
    
    /**
	 * Method to subtract the 24 months from current date.
	 * 
	 * @return Date
	 */
    public static Date currentDateMinus24Months(){
		
    	SimpleDateFormat dateFormat = new SimpleDateFormat(RenderConstants.MEDIUM_YMD_DASHED);
		Date coFidDate = null;
		Calendar c = Calendar.getInstance(); 
		c.add(Calendar.MONTH, -24);
		String currentDateMinus24Months = dateFormat.format(c.getTime());
		
		try {
			coFidDate =  dateFormat.parse(currentDateMinus24Months);
		} catch (ParseException e) {
			
		}
		return coFidDate;
	}

}
