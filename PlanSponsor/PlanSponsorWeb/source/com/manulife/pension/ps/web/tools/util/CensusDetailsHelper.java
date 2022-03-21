package com.manulife.pension.ps.web.tools.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.participant.valueobject.VestingParticipant;
import com.manulife.pension.ps.service.report.submission.valueobject.CensusSubmissionReportData;
import com.manulife.pension.ps.service.submission.util.SubmissionErrorHelper;
import com.manulife.pension.ps.service.submission.valueobject.CensusSubmissionItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.util.CensusUtils;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.employee.valueobject.EmployeeDetailVO;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.RenderConstants;

import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * @author Diana Macean
 */
public class CensusDetailsHelper implements Serializable {

    private static final String SSN_LABEL = "SSN";

    private static final String EMPLOYEE_NUMBER_LABEL = "Employee ID";

    private static final String STATUS_COMPLETE = "16";

    private static final String STATUS_CANCELLED = "99";

    private static final String EMPTY_STRING = "";

    private boolean editMode = false;

    private boolean errorFlag = false;

    private boolean locked = false;

    private boolean permissable = false;

    private boolean allowedToDownload = false;

    private UserInfo userInfo;

    private static final String[] DATE_FORMATS = new String[] { "MMddyyyy", RenderConstants.MEDIUM_MDY_SLASHED };
    
    // DateFormats for parsing string dates (sent from STP or PSW)
    public static final DateFormat NO_SLASH_DATE_FORMAT = new SimpleDateFormat("MMddyyyy");      
    public static final DateFormat SLASH_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    private static final Object DATE_SYNC_OBJECT = new Object();  // Just used to synchronize on.

    static {
        // set lenient on dateFormats
        NO_SLASH_DATE_FORMAT.setLenient(false);
        SLASH_DATE_FORMAT.setLenient(false);
    }
    
    
    
    /**
     * Constructor
     */
    public CensusDetailsHelper() {
        super();
        
    }

    /**
     * Returns primary employee identifier label 
     * depending on the contract sort option
     * 
     * @param report
     * @return
     * @throws SystemException
     */
    public String getEmployeeIdentifierColumnLabel(CensusSubmissionReportData report)
            throws SystemException {

        if (Constants.SSN_SORT_OPTION_CODE.equals(report.getSortCode())) {
            return SSN_LABEL;
        } else {
            return EMPLOYEE_NUMBER_LABEL;
        }
    }

    /**
     * Returns secondary employee identifier label 
     * depending on the contract sort option
     * 
     * @param report
     * @return
     * @throws SystemException
     */
    public String getOtherEmployeeIdentifierColumnLabel(CensusSubmissionReportData report)
            throws SystemException {

        if (Constants.SSN_SORT_OPTION_CODE.equals(report.getSortCode())) {
            return EMPLOYEE_NUMBER_LABEL;
        } else {
            return SSN_LABEL;
        }
    }

    /**
     * Returns primary employee identifier sort field 
     * depending on the contract sort option
     * 
     * @param report
     * @return
     * @throws SystemException
     */
    public String getEmployeeIdentifierSortField(CensusSubmissionReportData report)
            throws SystemException {

        if (Constants.SSN_SORT_OPTION_CODE.equals(report.getSortCode())) {
            return CensusSubmissionReportData.SORT_SSN;
        } else {
            return CensusSubmissionReportData.SORT_EMP_ID;
        }
    }

    /**
     * Returns secondary employee identifier sort field 
     * depending on the contract sort option
     * 
     * @param report
     * @return
     * @throws SystemException
     */
    public String getOtherEmployeeIdentifierSortField(CensusSubmissionReportData report)
            throws SystemException {

        if (Constants.SSN_SORT_OPTION_CODE.equals(report.getSortCode())) {
            return CensusSubmissionReportData.SORT_EMP_ID;
        } else {
            return CensusSubmissionReportData.SORT_SSN;
        }
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
     * Returns error icon if in error, value if value does not exceed maximum 
     * length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getAddressLine1(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getAddress1Status());
        if (errorIndicator != null) {
            addressHTML.append(errorIndicator);
        }

        if (item.getAddressLine1() != null) {
            addressHTML.append(item.getAddresLine1Truncated());
        }

        return addressHTML.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getAddressLine1ToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getAddressLine1() != null && 
            item.getAddressLine1().length() > CensusSubmissionItem.MAX_ADDRESS_LENGTH) {
            addressHTML.append(item.getAddressLine1());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error, value if value does not exceed maximum 
     * length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getFirstName(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getFirstNameStatus());
        if (errorIndicator != null) {
            addressHTML.append(errorIndicator);
        }

        if (item.getFirstName() != null) {
            addressHTML.append(item.getFirstNameTruncated());
        }

        return addressHTML.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getFirstNameToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getFirstName() != null && 
            item.getFirstName().length() > CensusSubmissionItem.MAX_FIRST_NAME_LENGTH) {
            addressHTML.append(item.getFirstName());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error and value
     * 
     * @param item
     * @return
     */
    public String getEligibilityIndicator(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getEligibilityIndicatorStatus());
        if (errorIndicator != null) {
            addressHTML.append(errorIndicator);
        }

        if (item.getEligibilityIndicator() != null) {
            addressHTML.append(item.getEligibilityIndicator());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error and value
     * 
     * @param item
     * @return
     */
    public String getOptOutIndicator(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getOptOutIndicatorStatus());
        if (errorIndicator != null) {
            addressHTML.append(errorIndicator);
        }

        if (item.getOptOutIndicator() != null) {
            addressHTML.append(item.getOptOutIndicator());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error, value if value does not exceed maximum 
     * length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getLastName(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getLastNameStatus());
        if (errorIndicator != null) {
            addressHTML.append(errorIndicator);
        }

        if (item.getLastName() != null) {
            addressHTML.append(item.getLastNameTruncated());
        }

        return addressHTML.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getLastNameToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getLastName() != null && 
            item.getLastName().length() > CensusSubmissionItem.MAX_LAST_NAME_LENGTH) {
            addressHTML.append(item.getLastName());
        }

        return addressHTML.toString();
    }


    /**
     * Returns error icon if in error, value if value does not exceed maximum 
     * length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getEmployeeProvidedEmail(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getEmployeeProvidedEmailStatus());
        if (errorIndicator != null) {
            addressHTML.append(errorIndicator);
        }

        if (item.getEmployeeProvidedEmail() != null) {
            addressHTML.append(item.getEmployeeProvidedEmailTruncated());
        }

        return addressHTML.toString();
    }
    
    /**
     * Returns error icon if in error, value if value does not exceed maximum 
     * length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getDivision(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getDivisionStatus());
        if (errorIndicator != null) {
            addressHTML.append(errorIndicator);
        }

        if (item.getDivision() != null) {
            addressHTML.append(item.getDivisionTruncated());
        }

        return addressHTML.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getDivisionToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getDivision() != null && 
            item.getDivision().length() > CensusSubmissionItem.MAX_DIVISION_LENGTH) {
            addressHTML.append(item.getDivision());
        }

        return addressHTML.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getEmployeeProvidedEmailToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getEmployeeProvidedEmail() != null && 
            item.getEmployeeProvidedEmail().length() > CensusSubmissionItem.MAX_EMAIL_LENGTH) {
            addressHTML.append(item.getEmployeeProvidedEmail());
        }

        return addressHTML.toString();
    }


    /**
     * Returns error icon if in error, value if value does not exceed maximum 
     * length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getAddressLine2(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getAddress2Status());
        if (errorIndicator != null) {
            addressHTML.append(errorIndicator);
        }

        if (item.getAddressLine2() != null) {
            addressHTML.append(item.getAddresLine2Truncated());
        }
        return addressHTML.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getAddressLine2ToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getAddressLine2() != null && 
            item.getAddressLine2().length() > CensusSubmissionItem.MAX_ADDRESS_LENGTH) {
            addressHTML.append(item.getAddressLine2());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error, value if value does not exceed maximum 
     * length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getStateOfResidence(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getStateOfResidenceStatus());
        if (errorIndicator != null) {
            addressHTML.append(errorIndicator);
        }

        if (item.getStateOfResidence() != null) {
            addressHTML.append(item.getStateOfResidenceTruncated());
        }

        return addressHTML.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getStateOfResidenceToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getStateOfResidence() != null && 
            item.getStateOfResidence().length() > CensusSubmissionItem.MAX_STATE_LENGTH) {
            addressHTML.append(item.getStateOfResidence());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error, value if value does not exceed maximum length 
     * or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getNamePrefix(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getNamePrefixStatus());
        if (errorIndicator != null) {
            addressHTML.append(errorIndicator);
        }

        if (item.getNamePrefix() != null) {
            addressHTML.append(item.getNamePrefixTruncated());
        }

        return addressHTML.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getNamePrefixToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getNamePrefix() != null && 
            item.getNamePrefix().length() > CensusSubmissionItem.MAX_PREFIX_LENGTH) {
            addressHTML.append(item.getNamePrefix());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error and value
     * 
     * @param item
     * @return
     */
    public String getMiddleInitial(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getMiddleInitialStatus());
        if (errorIndicator != null) {
            addressHTML.append(errorIndicator);
        }

        if (item.getMiddleInitial() != null) {
            addressHTML.append(item.getMiddleInitial());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error, value if value does not exceed maximum length 
     * or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getCity(CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getCityStatus());
        if (errorIndicator != null) {
            html.append(errorIndicator);
        }

        if (item.getCity() != null) {
            html.append(item.getCityTruncated());
        }

        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getCityToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getCity() != null && 
            item.getCity().length() > CensusSubmissionItem.MAX_CITY_LENGTH) {
            addressHTML.append(item.getCity());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error and value
     * 
     * @param item
     * @return
     */
    public String getEmployeeStatus(CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getEmployeeStatusStatus());
        if (errorIndicator != null) {
            html.append(errorIndicator);
        }

        if (item.getEmployeeStatus() != null) {
            html.append(item.getEmployeeStatus());
        }

        return html.toString();
    }

    /**
     * Returns error icon if in error, formatted value if value does not exceed 
     * maximum length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getBirthDate(CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getBirthDateStatus());
        if (errorIndicator != null) {
            html.append(errorIndicator);
        }

        String inDate = item.getBirthDate();
        if (inDate != null && inDate.length() > CensusSubmissionItem.MAX_DATE_LENGTH) {
            html.append(CensusSubmissionItem.getTruncatedValue(inDate, CensusSubmissionItem.MAX_DATE_LENGTH));
        } else {
            html.append(formatDateForWeb(inDate));
        }

        return html.toString();
    }
    
    /**
     * Convert a date string to a proper formatted date string for the web
     * 
     * @param inDate
     * @return
     */
    public static String formatDateForWeb(String inDate) {
        DateFormat dateFormat = null;
        Date convertedDate = null;
        String outDate = "";
            
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
        
        return outDate;
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getBirthDateToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getBirthDate() != null && 
            item.getBirthDate().length() > CensusSubmissionItem.MAX_DATE_LENGTH) {
            addressHTML.append(item.getBirthDate());
        }

        return addressHTML.toString();
    }


    /**
     * Returns error icon if in error, formatted value if value does not exceed 
     * maximum length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getHireDate(CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getHireDateStatus());
        if (errorIndicator != null) {
            html.append(errorIndicator);
        }

        String inDate = item.getHireDate();
        if (inDate != null && inDate.length() > CensusSubmissionItem.MAX_DATE_LENGTH) {
            html.append(CensusSubmissionItem.getTruncatedValue(inDate, CensusSubmissionItem.MAX_DATE_LENGTH));
        } else {
            html.append(formatDateForWeb(inDate));
        }

        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getHireDateToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getHireDate() != null && 
            item.getHireDate().length() > CensusSubmissionItem.MAX_DATE_LENGTH) {
            addressHTML.append(item.getHireDate());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error, formatted value if value does not exceed 
     * maximum length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getEmployeeStatusDate(CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getEmployeeStatusDateStatus());
        if (errorIndicator != null) {
            html.append(errorIndicator);
        }

        String inDate = item.getEmployeeStatusDate();
        if (inDate != null && inDate.length() > CensusSubmissionItem.MAX_DATE_LENGTH) {
            html.append(CensusSubmissionItem.getTruncatedValue(inDate, CensusSubmissionItem.MAX_DATE_LENGTH));
        } else {
            html.append(formatDateForWeb(inDate));
        }

        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getEmployeeStatusDateToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getEmployeeStatusDate() != null && 
            item.getEmployeeStatusDate().length() > CensusSubmissionItem.MAX_DATE_LENGTH) {
            addressHTML.append(item.getEmployeeStatusDate());
        }

        return addressHTML.toString();
    }


    /**
     * Returns error icon if in error, formatted value if value does not exceed 
     * maximum length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getEligibilityDate(CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getEligibilityDateStatus());
        if (errorIndicator != null) {
            html.append(errorIndicator);
        }

        String inDate = item.getEligibilityDate();
        if (inDate != null && inDate.length() > CensusSubmissionItem.MAX_DATE_LENGTH) {
            html.append(CensusSubmissionItem.getTruncatedValue(inDate, CensusSubmissionItem.MAX_DATE_LENGTH));
        } else {
            html.append(formatDateForWeb(inDate));
        }

        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getEligibilityDateToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getEligibilityDate() != null && 
            item.getEligibilityDate().length() > CensusSubmissionItem.MAX_DATE_LENGTH) {
            addressHTML.append(item.getEligibilityDate());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error, formatted value if value does not exceed 
     * maximum length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getPlanYTDHoursWorkedEffDate(CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getPlanYTDHoursWorkedEffDateStatus());
        if (errorIndicator != null) {
            html.append(errorIndicator);
        }

        String inDate = item.getPlanYTDHoursWorkedEffDate();
        if (inDate != null && inDate.length() > CensusSubmissionItem.MAX_DATE_LENGTH) {
            html.append(CensusSubmissionItem.getTruncatedValue(inDate, CensusSubmissionItem.MAX_DATE_LENGTH));
        } else {
            html.append(formatDateForWeb(inDate));
        }

        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getPlanYTDHoursWorkedEffDateToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getPlanYTDHoursWorkedEffDate() != null && 
            item.getPlanYTDHoursWorkedEffDate().length() > CensusSubmissionItem.MAX_DATE_LENGTH) {
            addressHTML.append(item.getPlanYTDHoursWorkedEffDate());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error, value if value does not exceed 
     * maximum length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getState(CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getStateStatus());
        if (errorIndicator != null) {
            html.append(errorIndicator);
        }

        if (item.getStateCode() != null) {
            html.append(item.getStateCodeTruncated());
        }
        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getStateToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getStateCode() != null && 
            item.getStateCode().length() > CensusSubmissionItem.MAX_STATE_LENGTH) {
            addressHTML.append(item.getStateCode());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error, formatted value if value does not exceed 
     * maximum length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getZip(CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getZipStatus());
        if (errorIndicator != null) {
            html.append(errorIndicator);
        }

        if (item.getZipCode() != null) {
            if (item.getZipCode().length() > CensusSubmissionItem.MAX_ZIP_LENGTH) {
                html.append(CensusSubmissionItem.getTruncatedValue(item.getZipCode(),
                        CensusSubmissionItem.MAX_ZIP_LENGTH));
            } else {
                if ("USA".equals(item.getCountry())) {
                    html.append(formatZip(item.getZipCode().trim()));
                } else {
                    html.append(item.getZipCode().trim());
                }
            }
        }

        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getZipToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getZipCode() != null && 
            item.getZipCode().length() > CensusSubmissionItem.MAX_ZIP_LENGTH) {
            addressHTML.append(item.getZipCode());
        }

        return addressHTML.toString();
    }

    /**
     * Format ZIP
     * 
     * @param zip
     * @return
     */
    public String formatZip(String zip) {
        if (zip != null) {
            if (zip.length() == 9 && zip.indexOf("-") == -1 && StringUtils.isNumeric(zip)) {
                /*
                 * Reformatting is only applied if the Zip code is somewhat valid. That is, it has 9
                 * digits and doesn't have '-' already.
                 */
                StringBuffer html = new StringBuffer();
                html.append(zip.substring(0, 5)).append("-").append(zip.substring(5, 9));
                return html.toString();
            }
        }
        return zip;
    }

    /**
     * Returns error icon if in error, value if value does not exceed 
     * maximum length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getCountry(CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getCountryStatus());
        if (errorIndicator != null) {
            html.append(errorIndicator);
        }

        if (item.getCountry() != null) {
            html.append(item.getCountryTruncated());
        }
        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getCountryToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getCountry() != null && 
            item.getCountry().length() > CensusSubmissionItem.MAX_COUNTRY_LENGTH) {
            addressHTML.append(item.getCountry());
        }

        return addressHTML.toString();
    }

    private String getErrorIndicator(int errorStatus) {
        if (errorStatus == CensusSubmissionItem.NON_EDITABLE_WARNING) {
            return Constants.WARNING_ICON;
        } else if (errorStatus == CensusSubmissionItem.EDITABLE_WARNING) {
            return Constants.WARNING_ICON;
        } else if (errorStatus == CensusSubmissionItem.ERROR) {
            return Constants.ERROR_ICON;
        } else {
            return null;
        }
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

    public boolean isEditAllowed() {
        // display the edit button if there is an error, the case is not locked and the user has
        // permissions
        if (errorFlag == true && locked == false && permissable == true) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCensusEditable(CensusSubmissionItem item) {
        // display the edit button/icon if there is an error or an editable warning,
        // the case is not locked and the user has permissions
        if (SubmissionErrorHelper.getNumberOfErrorsAndEditableWarnings(item.getErrors()) > 0
                && locked == false && permissable == true
                && !STATUS_CANCELLED.equals(item.getProcessStatus())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDateDisplayable(CensusSubmissionItem item) {
        if (STATUS_COMPLETE.equals(item.getProcessStatus())
                || STATUS_CANCELLED.equals(item.getProcessStatus())) {
            return true;
        } else {
            return false;
        }
    }

    public String getId(CensusSubmissionReportData report, CensusSubmissionItem item) {
        return getId(report, item, false);
    }

    /**
     * Returns error icon if in error, formatted primary identifier if value 
     * does not exceed maximum length or truncated primary identifier if value 
     * exceeds maximum length
     * 
     * @param report
     * @param item
     * @param forceMask
     * @return
     */
    public String getId(CensusSubmissionReportData report, CensusSubmissionItem item,
            boolean forceMask) {
        StringBuffer html = new StringBuffer();
        String errorIndicator;
        if (!Constants.SSN_SORT_OPTION_CODE.equals(report.getSortCode())) {

            errorIndicator = getErrorIndicator(item.getEmployeeNumberStatus());
            if (errorIndicator != null) {
                html.append(errorIndicator);
            }

            String empNumber = StringUtils.defaultIfEmpty(item.getEmployeeNumber(), EMPTY_STRING);
            html.append(CensusSubmissionItem.getTruncatedValue(empNumber, CensusSubmissionItem.MAX_EMP_ID_LENGTH));

        } else {

            errorIndicator = getErrorIndicator(item.getSsnStatus());
            if (errorIndicator != null) {
                html.append(errorIndicator);
            }

            String ssn = StringUtils.defaultIfEmpty(item.getSsn(), EMPTY_STRING);
            if (!editMode || forceMask) {
                html.append(formatSSN(maskSSN(item.getSsn()),StringUtils.isNumeric(item.getSsn())));
            } else {
                if (ssn.length() > CensusSubmissionItem.MAX_SSN_LENGTH) {
                    html.append(CensusSubmissionItem.getTruncatedValue(ssn, CensusSubmissionItem.MAX_SSN_LENGTH));
                } else {
                    html.append(formatSSN(item.getSsn(),StringUtils.isNumeric(item.getSsn())));
                }
            }
            
        }
        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param report
     * @param item
     * @return
     */
    public String getIdToolTip(CensusSubmissionReportData report, CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();
        
        if (Constants.SSN_SORT_OPTION_CODE.equals(report.getSortCode())) {
            if (editMode && item.getSsn()!= null && 
                item.getSsn().length() > CensusSubmissionItem.MAX_SSN_LENGTH) {
                html.append(item.getSsn());
            }
        } else {
            if (item.getEmployeeNumber() != null && 
                item.getEmployeeNumber().length() > CensusSubmissionItem.MAX_EMP_ID_LENGTH) {
                html.append(item.getEmployeeNumber());
            }
        }

        return html.toString();
    }

    /**
     * Returns error icon if in error, formatted value if value does not exceed 
     * maximum length or truncated value if value exceeds maximum length
     * 
     * @param report
     * @param item
     * @param user
     * @param forceMask
     * @param isOnline
     * 
     * @return
     */
    public String getPlanYTDCompensation(CensusSubmissionReportData report,
            CensusSubmissionItem item, UserProfile user, boolean forceMask, boolean isOnline) {
        StringBuffer html = new StringBuffer();
        boolean maskSensitiveInfo = CensusUtils.isMaskSensitiveInformation(user, userInfo,
                isMaskSensitiveInfoInd(report, item));

        if (isOnline) {
            String errorIndicator = getErrorIndicator(item.getPlanYTDCompensationStatus());
            if (errorIndicator != null) {
                html.append(errorIndicator);
            }
        }

        String inValue = item.getPlanYTDCompensation();
        if (inValue != null) {
            if (inValue.length() > CensusSubmissionItem.MAX_NUMERIC_LENGTH) {
                html.append(CensusSubmissionItem
                        .getTruncatedValue(inValue, CensusSubmissionItem.MAX_NUMERIC_LENGTH));
            } else {
                if (maskSensitiveInfo || forceMask) {
                    html.append("******");
                } else if (inValue.toUpperCase().contains("E")) {
                    html.append(inValue);
                } else {
                    html.append(NumberRender.formatByType(inValue, inValue,
                            RenderConstants.CURRENCY_TYPE, false));
                }
            }
        }

        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getPlanYTDCompensationToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getPlanYTDCompensation() != null && 
            item.getPlanYTDCompensation().length() > CensusSubmissionItem.MAX_NUMERIC_LENGTH) {
            addressHTML.append(item.getPlanYTDCompensation());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error, formatted value if value does not exceed 
     * maximum length or truncated value if value exceeds maximum length
     * 
     * @param report
     * @param item
     * @param user
     * @param forceMask
     * @param isOnline
     * 
     * @return
     */
    public String getAnnualBaseSalary(CensusSubmissionReportData report, CensusSubmissionItem item,
            UserProfile user, boolean forceMask, boolean isOnline) {
        StringBuffer html = new StringBuffer();
        boolean maskSensitiveInfo = CensusUtils.isMaskSensitiveInformation(user, userInfo,
                isMaskSensitiveInfoInd(report, item));

        if (isOnline) {
            String errorIndicator = getErrorIndicator(item.getAnnualBaseSalaryStatus());
            if (errorIndicator != null) {
                html.append(errorIndicator);
            }
        }

        String inValue = item.getAnnualBaseSalary();
        if (inValue != null) {
            if (inValue.length() > CensusSubmissionItem.MAX_NUMERIC_LENGTH) {
                html.append(CensusSubmissionItem
                        .getTruncatedValue(inValue, CensusSubmissionItem.MAX_NUMERIC_LENGTH));
            } else {
                if (maskSensitiveInfo || forceMask) {
                    html.append("******");
                } else if (inValue.toUpperCase().contains("E")) {
                    html.append(inValue);
                } else {
                    html.append(NumberRender.formatByType(inValue, inValue,
                            RenderConstants.CURRENCY_TYPE, false));
                }
            }
        }

        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getAnnualBaseSalaryToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getAnnualBaseSalary() != null && 
            item.getAnnualBaseSalary().length() > CensusSubmissionItem.MAX_NUMERIC_LENGTH) {
            addressHTML.append(item.getAnnualBaseSalary());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error, formatted value if value does not exceed 
     * maximum length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getPlanYTDHoursWorked(CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getPlanYTDHoursWorkedStatus());
        if (errorIndicator != null) {
            html.append(errorIndicator);
        }

        String inValue = item.getPlanYTDHoursWorked();
        if (inValue != null) {
            if (inValue.length() > CensusSubmissionItem.MAX_NUMERIC_LENGTH) {
                html.append(CensusSubmissionItem
                        .getTruncatedValue(inValue, CensusSubmissionItem.MAX_NUMERIC_LENGTH));
            } else if (inValue.toUpperCase().contains("E")) {
                html.append(inValue);
            } else {
                html.append(NumberRender.formatByType(inValue, inValue,
                        RenderConstants.INTEGER_TYPE, false));
            }
        }

        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getPlanYTDHoursWorkedToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getPlanYTDHoursWorked() != null && 
            item.getPlanYTDHoursWorked().length() > CensusSubmissionItem.MAX_NUMERIC_LENGTH) {
            addressHTML.append(item.getPlanYTDHoursWorked());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error, formatted value if value does not exceed 
     * maximum length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getBeforeTaxDeferralPerc(CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getBeforeTaxDeferralPercStatus());
        if (errorIndicator != null) {
            html.append(errorIndicator);
        }

        String inValue = item.getBeforeTaxDeferralPerc();
        if (inValue != null) {
            if (inValue.length() > CensusSubmissionItem.MAX_PERC_LENGTH) {
                html.append(CensusSubmissionItem.getTruncatedValue(inValue, CensusSubmissionItem.MAX_PERC_LENGTH));
            } else if (inValue.toUpperCase().contains("E")) {
                html.append(inValue);
            } else {
                html.append(NumberRender.formatByPattern(inValue, inValue, "##0.000", 3,
                        BigDecimal.ROUND_DOWN));
            }
        }

        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getBeforeTaxDeferralPercToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getBeforeTaxDeferralPerc() != null && 
            item.getBeforeTaxDeferralPerc().length() > CensusSubmissionItem.MAX_PERC_LENGTH) {
            addressHTML.append(item.getBeforeTaxDeferralPerc());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error, formatted value if value does not exceed 
     * maximum length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getDesigRothDeferralPerc(CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getDesigRothDeferralPercStatus());
        if (errorIndicator != null) {
            html.append(errorIndicator);
        }

        String inValue = item.getDesigRothDeferralPerc();
        if (inValue != null) {
            if (inValue.length() > CensusSubmissionItem.MAX_PERC_LENGTH) {
                html.append(CensusSubmissionItem.getTruncatedValue(inValue, CensusSubmissionItem.MAX_PERC_LENGTH));
            } else if (inValue.toUpperCase().contains("E")) {
                html.append(inValue);
            } else {
                html.append(NumberRender.formatByPattern(inValue, inValue, "##0.000", 3,
                        BigDecimal.ROUND_DOWN));
            }
        }

        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getDesigRothDeferralPercToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getDesigRothDeferralPerc() != null && 
            item.getDesigRothDeferralPerc().length() > CensusSubmissionItem.MAX_PERC_LENGTH) {
            addressHTML.append(item.getDesigRothDeferralPerc());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error, formatted value if value does not exceed 
     * maximum length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getBeforeTaxDeferralAmt(CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getBeforeTaxDeferralAmtStatus());
        if (errorIndicator != null) {
            html.append(errorIndicator);
        }

        String inValue = item.getBeforeTaxDeferralAmt();
        if (inValue != null) {
            if (inValue.length() > CensusSubmissionItem.MAX_DEFFERAL_LENGTH) {
                html.append(CensusSubmissionItem
                        .getTruncatedValue(inValue, CensusSubmissionItem.MAX_DEFFERAL_LENGTH));
            } else if (inValue.toUpperCase().contains("E")) {
                html.append(inValue);
            } else {
                html.append(NumberRender.formatByPattern(inValue, inValue, "###,##0.00"));
            }
        }

        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getBeforeTaxDeferralAmtToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getBeforeTaxDeferralAmt() != null && 
            item.getBeforeTaxDeferralAmt().length() > CensusSubmissionItem.MAX_DEFFERAL_LENGTH) {
            addressHTML.append(item.getBeforeTaxDeferralAmt());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error, formatted value if value does not exceed 
     * maximum length or truncated value if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getDesigRothDeferralAmt(CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();

        String errorIndicator = getErrorIndicator(item.getDesigRothDeferralAmtStatus());
        if (errorIndicator != null) {
            html.append(errorIndicator);
        }

        String inValue = item.getDesigRothDeferralAmt();
        if (inValue != null) {
            if (inValue.length() > CensusSubmissionItem.MAX_DEFFERAL_LENGTH) {
                html.append(CensusSubmissionItem
                        .getTruncatedValue(inValue, CensusSubmissionItem.MAX_DEFFERAL_LENGTH));
            } else if (inValue.toUpperCase().contains("E")) {
                html.append(inValue);
            } else {
                html.append(NumberRender.formatByPattern(inValue, inValue, "###,##0.00"));
            }
        }

        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getDesigRothDeferralAmtToolTip(CensusSubmissionItem item) {
        StringBuffer addressHTML = new StringBuffer();

        if (item.getDesigRothDeferralAmt() != null && 
            item.getDesigRothDeferralAmt().length() > CensusSubmissionItem.MAX_DEFFERAL_LENGTH) {
            addressHTML.append(item.getDesigRothDeferralAmt());
        }

        return addressHTML.toString();
    }

    /**
     * Returns error icon if in error, formatted secondary identifier if value does not exceed
     * maximum length or truncated secondary identifier if value exceeds maximum length
     * 
     * @param item
     * @return
     */
    public String getOtherId(CensusSubmissionReportData report, CensusSubmissionItem item,
            boolean forceMask) {
        StringBuffer html = new StringBuffer();
        String errorIndicator;
        if (Constants.SSN_SORT_OPTION_CODE.equals(report.getSortCode())) {

            errorIndicator = getErrorIndicator(item.getEmployeeNumberStatus());
            if (errorIndicator != null) {
                html.append(errorIndicator);
            }

            String empNumber = StringUtils.defaultIfEmpty(item.getEmployeeNumber(), EMPTY_STRING);
            html.append(CensusSubmissionItem.getTruncatedValue(empNumber, CensusSubmissionItem.MAX_EMP_ID_LENGTH));

        } else {

            errorIndicator = getErrorIndicator(item.getSsnStatus());
            if (errorIndicator != null) {
                html.append(errorIndicator);
            }

            String ssn = StringUtils.defaultIfEmpty(item.getSsn(), EMPTY_STRING);
            if (!editMode || forceMask) {
                html.append(formatSSN(maskSSN(item.getSsn()),StringUtils.isNumeric(item.getSsn())));
            } else {
                if (ssn.length() > CensusSubmissionItem.MAX_SSN_LENGTH) {
                    html.append(CensusSubmissionItem.getTruncatedValue(ssn, CensusSubmissionItem.MAX_SSN_LENGTH));
                } else {
                    html.append(formatSSN(item.getSsn(),StringUtils.isNumeric(item.getSsn())));
                }
            }
        }

        return html.toString();
    }
    
    /**
     * Get formatted Employee Number (used by error correction page)
     * 
     * @param empNumber
     * @param isDisabled
     * @return
     */
    public static String getFormattedEmpNumber(String empNumber, boolean isDisabled) {
        
        StringBuffer html = new StringBuffer();
        String formattedEmpNumber = StringUtils.defaultIfEmpty(empNumber, EMPTY_STRING);
        
        if (formattedEmpNumber.length() > CensusSubmissionItem.MAX_EMP_ID_LENGTH && isDisabled) {
            html.append(CensusSubmissionItem.getTruncatedValue(formattedEmpNumber, 
                        CensusSubmissionItem.MAX_EMP_ID_LENGTH));
        } else {
            html.append(formattedEmpNumber);
        }
        
        return html.toString();
    }
    
    /**
     * Get formatted SSN (used by error correction page)
     * 
     * @param ssn
     * @param forceMask
     * @param isDisabled
     * @return
     */
    public static String getFormattedSsn(String ssn, boolean forceMask, boolean isDisabled) {
        
        StringBuffer html = new StringBuffer();
        String formattedSsn = StringUtils.defaultIfEmpty(ssn, EMPTY_STRING);
        
        if (formattedSsn.length() > CensusSubmissionItem.MAX_SSN_LENGTH && isDisabled) {
            html.append(CensusSubmissionItem.getTruncatedValue(formattedSsn, 
                        CensusSubmissionItem.MAX_SSN_LENGTH));
        } else {
            if (forceMask) {
                html.append(formatSSN(maskSSN(formattedSsn),StringUtils.isNumeric(formattedSsn)));
            } else {
                html.append(formatSSN(formattedSsn,StringUtils.isNumeric(formattedSsn)));
            }
        }
        
        return html.toString();
    }
    
    /**
     * Displays full field value as a tool tip, 
     * only if value exceeds maximum length
     * 
     * @param report
     * @param item
     * @return
     */
    public String getOtherIdToolTip(CensusSubmissionReportData report, CensusSubmissionItem item) {
        StringBuffer html = new StringBuffer();
        
        if (Constants.SSN_SORT_OPTION_CODE.equals(report.getSortCode())) {
            if (item.getEmployeeNumber() != null && 
                item.getEmployeeNumber().length() > CensusSubmissionItem.MAX_EMP_ID_LENGTH) {
                html.append(item.getEmployeeNumber());
            }
        } else {
            if (editMode && item.getSsn()!= null && 
                item.getSsn().length() > CensusSubmissionItem.MAX_SSN_LENGTH) {
                html.append(item.getSsn());
            }
        }

        return html.toString();
    }

    /**
     * Mask SSN
     * 
     * @param ssn
     * @return
     */
    private static String maskSSN(String ssn) {
        StringBuffer maskedSSN = new StringBuffer("xxxxx");

        // if the ssn is null or empty, return an empty string
        if (ssn == null || ssn.length() == 0) {
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

    /**
     * Format SSN in xxx-xx-xxxx format
     * 
     * @param ssn
     * @return
     */
    private static String formatSSN(String ssn, boolean isNumeric) {

        // if the ssn is null or empty, return an empty string
        if (ssn == null || ssn.length() == 0) {
            return "";
        }

        // if the ssn is not 9 characters long, return the ssn as it is
        if (ssn.length() != 9) {
            return ssn;
        }

        // if the ssn is 9 characters long and numeric, format the ssn
        // else if the ssn is 9 characters long and non-numeric, return as is
        StringBuffer formattedSSN = new StringBuffer();
        if (ssn.length() == 9 && isNumeric) {
            formattedSSN.append(ssn.substring(0, 3));
            formattedSSN.append("-");
            formattedSSN.append(ssn.substring(3, 5));
            formattedSSN.append("-");
            formattedSSN.append(ssn.substring(5, 9));
        } else {
            formattedSSN.append(ssn);
        }
        return formattedSSN.toString();
    }

    /**
     * Finds the mask sensitive info flag from CSDB
     * 
     * @param report
     * @param item
     * @return boolean
     */
    private boolean isMaskSensitiveInfoInd(CensusSubmissionReportData report,
            CensusSubmissionItem item) {
        boolean maskSensitiveInfoInd = false;
        EmployeeDetailVO vo = null;

        try {
            // find employee in CSDB by identifier
            EmployeeServiceDelegate delegate = EmployeeServiceDelegate
                    .getInstance(Constants.PS_APPLICATION_ID);
            if (Constants.SSN_SORT_OPTION_CODE.equals(report.getSortCode())) {
                vo = delegate.getEmployeeByIdentifier(item.getSsn(), item.getContractNumber(), true);
            } else {
                vo = delegate.getEmployeeByIdentifier(item.getEmployeeNumber(), item.getContractNumber(), false);
            }
        } catch (SystemException e) {
            LogUtility.logSystemException(Constants.PS_APPLICATION_ID, e);
        }

        if (vo != null) {
            if (Constants.YES.equals(vo.getMaskSensitiveInfoInd()))
                maskSensitiveInfoInd = true;
            else
                maskSensitiveInfoInd = false;
        }

        return maskSensitiveInfoInd;
    }

    public boolean getAllowedToDownload() {
        return allowedToDownload;
    }

    public void setAllowedToDownload(boolean allowedToDownload) {
        this.allowedToDownload = allowedToDownload;
    }

	/**
	 * @param userInfo the userInfo to set
	 */
	public void setUserInfo(final UserInfo userInfo) {
		this.userInfo = userInfo;
	}

}
