package com.manulife.pension.ps.web.census.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryDetails;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingDetails;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.taglib.census.EmployeeSnapshotValidationErrorMessageTag;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.service.vesting.VestingConstants;
import com.manulife.pension.service.vesting.util.VestingMessageType;
import com.manulife.pension.util.content.MessageProvider;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;

/**
 * Utility class used to display errors/warnings in Census pages
 *
 * @author patuadr
 *
 */
public class CensusValidationErrors extends AbstractValidationErrors {

    private static final Logger log = Logger.getLogger(CensusValidationErrors.class);

    private static final long serialVersionUID = -2963366092536764491L;
    private static final String BIRTH_DATE_WARNING = "Blank Birth Date!";
    private static final String ELIGIB_IND_WARNING = "Blank Eligibility Indicator!";
    private static final String ELIGIB_DATE_WARNING = "Blank Eligibility Date!";
    private static final String UnspecifiedMethod = "UnspecifedCreditingMethod";
    private static final String MissingSchedule = "MissingVestingSchedule";

    public CensusValidationErrors(List<ValidationError> errors) {
        setErrors(errors);
    }


    /**
     * Utility method that performs a warning assesment for the Eligibility tab
     *
     * @param element
     */
    public static void processEligibilityWarnings(EmployeeSummaryDetails element) {
        if (StringUtils.isEmpty(element.getEligibleToEnroll()) &&
           isActive(element.getEmploymentStatus()) &&
           SecurityConstants.NON_PARTICIPANT_STATUS.equalsIgnoreCase(element.getEnrollmentStatus())) {
            element.getWarnings().setEligibilityIndWarning();
        }

        if (element.isEligible() &&
            StringUtils.isEmpty(element.getEligibilityDate()) &&
            SecurityConstants.NON_PARTICIPANT_STATUS.equalsIgnoreCase(element.getEnrollmentStatus())) {
             element.getWarnings().setEligibilityDateWarning();
        }
    }

    /**
     * Utility method that performs a warning assessment for the Vesting tab
     *
     * @param element
     */
    public static void processVestingWarnings(CensusVestingDetails element, Map errors) throws SystemException {
        Set vestingErrors = element.getCensusErrors();

        if (element.isParticipantStatusPartial()) {
            element.getWarnings().setWarnings(true);
        }

        if (vestingErrors != null && vestingErrors.size() > 0) {

            // check if PYOS and YTD hours worked is missing
            if (vestingErrors.contains(VestingMessageType.PREVIOUS_YEARS_OF_SERVICE_AND_PLAN_YTD_HOURS_WORKED_NOT_PROVIDED)) {
                element.getWarnings().setWarnings(true);
            }

            // check if Hire Date is missing
            if (vestingErrors.contains(VestingMessageType.HIRE_DATE_NOT_PROVIDED)) {
                element.getWarnings().setWarnings(true);
            }

            // check if Employment Status is missing
            if (vestingErrors.contains(VestingMessageType.EMPLOYMENT_STATUS_NOT_PROVIDED)) {
                element.getWarnings().setWarnings(true);
            }

            // unspecified crediting method (contract level warning)
            if (vestingErrors.contains(VestingMessageType.CREDITING_METHOD_IS_UNSPECIFIED)) {
                if (!errors.containsKey(UnspecifiedMethod)) {
                    errors.put(UnspecifiedMethod,new ValidationError(UnspecifiedMethod,
                        CensusErrorCodes.UnspecifiedCalculationMethod, Type.warning));
                }
            }

            // no vesting schedule (contract level warning)
            if (vestingErrors.contains(VestingMessageType.VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP)) {
                if (!errors.containsKey(MissingSchedule)) {
                    errors.put(MissingSchedule,new ValidationError(MissingSchedule,
                        CensusErrorCodes.MissingVestingSchedule, Type.warning));
                }
            }

        }

    }

    /**
     * Utility method checks if employment status is Active or Blank
     *
     * @return
     */
    private static boolean isActive(String status) {
        if (StringUtils.isEmpty(status) || status.equals("A") || status.equals(SecurityConstants.ACTIVE_STATUS)) {
            return true;
        }

        return false;
    }

}
