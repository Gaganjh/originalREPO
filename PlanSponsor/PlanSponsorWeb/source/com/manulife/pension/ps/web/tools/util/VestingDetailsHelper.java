package com.manulife.pension.ps.web.tools.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.manulife.pension.ps.service.report.participant.valueobject.VestingParticipant;
import com.manulife.pension.ps.service.submission.valueobject.VestingDetailItem;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.util.render.DateRender;

/**
 * @author Diana Macean
 */
public class VestingDetailsHelper implements Serializable {

	private static final String SSN_LABEL = "SSN";
	private static final String EMPLOYEE_NUMBER_LABEL = "Employee ID";
	
	public static final String STATUS_COMPLETE = "16";
    public static final String STATUS_PARTIALLY_COMPLETE_WARNINGS = "19";
    public static final String STATUS_PARTIALLY_COMPLETE_IGNORES = "21";
	public static final String STATUS_CANCELLED = "99";
    public static final String STATUS_SYNTACTIC_CHECK_FAILED = "01";
    
    private static final String PROVIDED_DATE_FIELD = "vestingDate";
    private static final String CALCULATED_DATE_FIELD = "vyosDate";
    
    private static final String EMPTY_STRING = "";
    private static final String COLON_STRING = ":";
	
	private boolean editMode = false;
	private boolean errorFlag = false;
	private boolean locked = false;
	private boolean permissable = false;
    private boolean allowedToDownload = false;
    
    // DateFormats for parsing string dates (sent from STP or PSW)
    public static final DateFormat NO_SLASH_DATE_FORMAT = new SimpleDateFormat("MMddyyyy");      
    public static final DateFormat SLASH_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    private static final Object DATE_SYNC_OBJECT = new Object();  // Just used to synchronize on.

    static {
        // set lenient on dateFormats
        NO_SLASH_DATE_FORMAT.setLenient(false);
        SLASH_DATE_FORMAT.setLenient(false);
    }
	
	public VestingDetailsHelper() {
		super();
	}

	/**
	 * @return Returns the editMode.
	 */
	public boolean isEditMode() {
		return editMode;
	}
	/**
	 * @param editMode The editMode to set.
	 */
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}
    
	/**
	 * @return Returns the errorFlag.
	 */
	public boolean isErrorFlag() {
		return errorFlag;
	}
	/**
	 * @param errorFlag The errorFlag to set.
	 */
	public void setErrorFlag(boolean errorFlag) {
		this.errorFlag = errorFlag;
	}
	/**
	 * @return Returns the locked.
	 */
	public boolean isLocked() {
		return locked;
	}
	/**
	 * @param locked The locked to set.
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	/**
	 * @return Returns the permissable.
	 */
	public boolean isPermissable() {
		return permissable;
	}
	/**
	 * @param permissable The permissable to set.
	 */
	public void setPermissable(boolean permissable) {
		this.permissable = permissable;
	}

	public boolean isVestingEditable() {
		// display the edit button if the case is not locked and the user has permissions
		if ( locked == false && permissable == true) {
			return true;
		} else {
			return false;
		}
	}
	
	
	private String maskSSN(String ssn) {
		StringBuffer maskedSSN = new StringBuffer("xxxxx");
        
		// if the ssn is null or empty, return an empty string
        if ( ssn == null || ssn.length() == 0) {
            return "";
        }
        
        // if the ssn is less than 9 characters long, just return "xxxxx"
		if ( ssn.length() < 9 ) {
			return maskedSSN.toString();
		}
        // else append last 4 characters
        if ( ssn.length() == 9 ) {
            maskedSSN.append(ssn.substring(5,9));
        } else if ( ssn.length() > 9 ) {
            maskedSSN.append("x").append(ssn.substring(ssn.length() - 4));
        }
		return maskedSSN.toString();
	}
    
    public String getSSN(VestingParticipant participant, boolean forceMask) {
        StringBuffer html = new StringBuffer();
        
        if ( forceMask ) {
            html.append(formatSSN(maskSSN(participant.getSsn())));
        } else {
            html.append(formatSSN(participant.getSsn()));
        }
        
        return html.toString();
    }
	
	private String formatSSN(String ssn) {

		// if the ssn is null or empty, return an empty string
		if ( ssn == null || ssn.length() == 0) {
			return "";
		}
		
		// if the ssn is not 9 characters long, return the ssn as it is
		if ( ssn.length() != 9 ) {
			return ssn;
		}
		
		// format the ssn
		StringBuffer formattedSSN = new StringBuffer();
		formattedSSN.append(ssn.substring(0,3));
		formattedSSN.append("-");
		formattedSSN.append(ssn.substring(3,5));
		formattedSSN.append("-");
		formattedSSN.append(ssn.substring(5,9));
		return formattedSSN.toString();
	}

    public boolean getAllowedToDownload() {
        return allowedToDownload;
    }

    public void setAllowedToDownload(boolean allowedToDownload) {
        this.allowedToDownload = allowedToDownload;
    }

    public static String formatVestingDateStp(String vestingDate) {
    	String date = StringUtils.trimToNull(vestingDate);
    	if (date != null) {
    		try {
    			Date parsedDate = DateUtils.parseDate(date, new String[] {
						"M/d/yyyy", "MM/dd/yyyy", "M/dd/yyyy", "MM/d/yyyy" });
				return DateFormatUtils.format(parsedDate, "MM/dd/yyyy");
    		} catch(ParseException e) {
    			// do nothing. return date as usual.
    		}
    	}
    	return date; 
    }
    
    public static String formatVestingDateForWeb(VestingParticipant item, String vestingCSF) {
        DateFormat dateFormat = null;
        Date convertedDate = null;
        String outDate = "";
        String inDate = "";
        
        if (item != null) {
            
            if (ServiceFeatureConstants.PROVIDED.equals(vestingCSF)) {
                inDate = item.getPercDate();
            } else {
                inDate = item.getVyosDate();
            }
            
            // Format only if the given date is not empty and its length is <= 10
            if (StringUtils.isNotEmpty(inDate)) {
                // We have to go thru this logic to not parse date as 0MM/0DD/0YYYY (these all are
                // parsed correctly by DateUtils or SimpleDateFormat)
                inDate = inDate.trim();

                if (inDate.length() == 8) { // MMddyyyy format
                    // Check whether its all numeric or not
                    if (StringUtils.isNumeric(inDate)) {
                        dateFormat = NO_SLASH_DATE_FORMAT;
                    }
                } else if (inDate.length() == 10) { // MM/dd/yyyy format
                    // ensure neither of day, month, year is greater than their max digits
                    // dd = 2 digits, mm = 2 digits, yyyy = 4 digits
                    String[] splittedDates = inDate.split("/");
                    if (splittedDates.length == 3) {
                        boolean isFormatValid = true;
                        for (int i = 0; isFormatValid && i < splittedDates.length; i++) {
                            if (!StringUtils.isNumeric(splittedDates[i])
                                    || (i == 0 && splittedDates[i].length() != 2)
                                    || (i == 1 && splittedDates[i].length() != 2)
                                    || (i == 2 && splittedDates[i].length() != 4)) {
                                isFormatValid = false;
                            }
                        }
                        
                        if (isFormatValid) {
                            dateFormat = SLASH_DATE_FORMAT;
                        }
                    }
                } 
                
                if (dateFormat != null) {
                    // Format the date
                    try {
                        synchronized (DATE_SYNC_OBJECT) {
                            convertedDate = dateFormat.parse(inDate);
                        }
                    } catch(ParseException ignored) {
                    }
                }
                
                outDate = DateRender.formatByPattern(convertedDate, inDate, "MM/dd/yyyy");
            }
        }    
            
        return outDate;
            
    }

	public static String checkApplyLTPTCreditingForWeb(VestingParticipant item) {

		String applyLTPTCrediting = "";

		if (item != null) {
			if (item.getApplyLTPTCrediting() != null) {
				applyLTPTCrediting = item.getApplyLTPTCrediting();
			}
		}
		return applyLTPTCrediting;
	}

    public static int getParamFieldCount(VestingDetailItem vesting) {
        if (ServiceFeatureConstants.PROVIDED.equals(vesting.getVestingCSF())) {
            return vesting.getPercentageMoneyTypes().size();
        } else if (ServiceFeatureConstants.CALCULATED.equals(vesting.getVestingCSF())) {
            return 1;
        }
        
        return 0;
    }
    
    public static int getParamFieldsDisplayed(VestingDetailItem vesting, int moneyTypesDisplayed, int paramsDisplayed) {
        if (ServiceFeatureConstants.PROVIDED.equals(vesting.getVestingCSF())) {
            return moneyTypesDisplayed;
        } else if (ServiceFeatureConstants.CALCULATED.equals(vesting.getVestingCSF())) {
            return paramsDisplayed;
        }
        
        return 0;
    }
    
    public static int getParamFieldSize(VestingDetailItem vesting, int moneyTypeSize, int paramSize) {
        if (ServiceFeatureConstants.PROVIDED.equals(vesting.getVestingCSF())) {
            return moneyTypeSize;
        } else if (ServiceFeatureConstants.CALCULATED.equals(vesting.getVestingCSF())) {
            return paramSize;
        }
        
        return 0;
    }

    /*
     * Checks if money type valid for vesting.
     * 
     * Returns false if money type is one of the following:
     * - an employee money type (money type that belongs to the Money Group ‘EE’)
     * - a fully vested Employer money type (money type that belongs to the Money 
     *   Group ‘ER’ and where the Fully Vested Indicator is ‘Y’)
     * Else returns true
     */
    private static boolean isMTValidForVesting(MoneyTypeVO moneyTypeVO) {
        if ( moneyTypeVO.getMoneyGroup().equals("EE") ||
             (moneyTypeVO.getMoneyGroup().equals("ER") && 
              moneyTypeVO.getFullyVested().equals("Y"))    ) {
             
          return false;  
        }
        return true;
    }

	
}
