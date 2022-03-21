package com.manulife.pension.ps.web.tools.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.service.report.participant.valueobject.LongTermPartTimeParticipant;
import com.manulife.pension.ps.service.report.participant.valueobject.VestingParticipant;
import com.manulife.pension.ps.service.submission.valueobject.LongTermPartTimeDetailItem;
import com.manulife.pension.ps.service.submission.valueobject.VestingDetailItem;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.util.render.DateRender;

/**
 * @author bobbave
 */
public class LongTermPartTimeDetailsHelper implements Serializable {

	private static final String SSN_LABEL = "SSN";
	
	public static final String STATUS_COMPLETE = "16";
    public static final String STATUS_PARTIALLY_COMPLETE_IGNORES = "A2";
	public static final String STATUS_CANCELLED = "99";
    public static final String STATUS_SYNTACTIC_CHECK_FAILED = "01";
    
    private static final String EMPTY_STRING = "";
    private static final String COLON_STRING = ":";
	
	private boolean editMode = false;
	private boolean errorFlag = false;
	private boolean locked = false;
	private boolean permissable = false;
    private boolean allowedToDownload = false;
    
	public LongTermPartTimeDetailsHelper() {
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

	public boolean isLongTermPartTimeReportEditable() {
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
    
    public String getSSN(LongTermPartTimeParticipant participant, boolean forceMask) {
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
    
	public static Integer checkLongTermPartTimeAssessmentYearForWeb(LongTermPartTimeParticipant item) {
		// default assessment year is 1
		int assessmentYear = 1;

		if (item != null) {

			if (Integer.parseInt(item.getLongTermPartTimeAssessmentYear()) != 0 && !(Integer.parseInt(item.getLongTermPartTimeAssessmentYear()) <= 1)
					&& !(Integer.parseInt(item.getLongTermPartTimeAssessmentYear()) >= 3)) {
				assessmentYear = Integer.parseInt(item.getLongTermPartTimeAssessmentYear());
			}
		}

		return assessmentYear;

	}
}
