package com.manulife.pension.ps.web.census.util;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.manulife.pension.ps.web.census.VestingInformation;
import com.manulife.pension.ps.web.census.util.VestingExplanationModel.EvaluationStrategy;
import com.manulife.pension.ps.web.census.util.VestingExplanationModel.EvaluationType;
import com.manulife.pension.ps.web.census.util.VestingExplanationModel.ModelBuilder;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.service.employee.EmployeeConstants;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.vesting.MoneyTypeVestingPercentage;
import com.manulife.pension.service.vesting.VestingConstants;
import com.manulife.pension.service.vesting.VestingExplanation;
import com.manulife.pension.service.vesting.VestingInputDescription;
import com.manulife.pension.service.vesting.VestingRetrievalDetails;
import com.manulife.pension.service.vesting.VestingRetriever;
import com.manulife.pension.service.vesting.util.VestingMessageType;


public class VestingExplanationRetriever {
    
    private static VestingExplanationRetriever retriever;
    public static VestingExplanationRetriever getInstance() {
        if (retriever == null) {
            retriever = new VestingExplanationRetriever();
        }
        return retriever;
    }
    
    private final Set<String> fullyVestedParticipantStatuses;
    private final VestingExplanationModel model;
    
    // singleton
    private VestingExplanationRetriever() {
        
        fullyVestedParticipantStatuses = createFullyVestedParticipantStatusSet();
        model = createVestingExplanationModel();
        
    }
    
    private Set<String> createFullyVestedParticipantStatusSet() {
        
        HashSet<String> statusSet = new HashSet<String>();
        statusSet.add(EmployeeConstants.ParticipantStatus.PARTIAL_DISABILITY);
        statusSet.add(EmployeeConstants.ParticipantStatus.PARTIAL_DEATH);
        statusSet.add(EmployeeConstants.ParticipantStatus.PARTIAL_RETIREMENT);
        statusSet.add(EmployeeConstants.ParticipantStatus.PARTIAL_TERMINATION);
        statusSet.add(EmployeeConstants.ParticipantStatus.PAID_UP_DISABILITY);
        statusSet.add(EmployeeConstants.ParticipantStatus.PAID_UP_DEATH);
        statusSet.add(EmployeeConstants.ParticipantStatus.PAID_UP_TERMINATION);
        
        return Collections.unmodifiableSet(statusSet);
        
    }
    
    private VestingExplanationModel createVestingExplanationModel() {
        
        VestingExplanationModel model = new VestingExplanationModel();
        ModelBuilder builder = model.getBuilder();
        
        builder.setLevel(EvaluationType.SHORT_CIRCUIT); // root
        
        
        //**********************************************************
        // Level 1 - All of contract's money types are fully vested
        //**********************************************************
        
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return false;
                    }
                },
                0,
                1);
        
        
        //************************************
        // Level 2 - Employee is fully vested
        //************************************
        
        builder.setLevel(EvaluationType.SHORT_CIRCUIT, 2);
        
        // scenario #4 - fully vested participant status, calling application = withdrawals
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return ! parms.isServiceCreditUnspecified
                        && parms.withdrawalReasonCode != null
                        && parms.fullyVestedExplanation != null
                        && VestingRetrievalDetails.EXPLANATION_EMPLOYEE_FULLY_VESTED_PARTICIPANT_STATUS.equals(parms.fullyVestedExplanation.getValue());
                    }
                },
                ContentConstants.EXPLANATION_FULLY_VESTED_PARTICIPANT_STATUS_WITHDRAWAL,
                2, 1);
        
        // scenario #5 - fully vested participant status, calling application is not withdrawals
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return ! parms.isServiceCreditUnspecified
                        && parms.withdrawalReasonCode == null
                        && parms.employee.getParticipantContract() != null
                        && fullyVestedParticipantStatuses.contains(
                                parms.employee.getParticipantContract().getParticipantStatusCode());
                    }
                },
                ContentConstants.EXPLANATION_FULLY_VESTED_PARTICIPANT_STATUS,
                2, 2);
        
        // scenario #3 - fully vested withdrawal reason, calling application = withdrawals
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return ! parms.isServiceCreditUnspecified
                        && parms.withdrawalReasonCode != null
                        && parms.fullyVestedExplanation != null
                        && VestingRetrievalDetails.EXPLANATION_EMPLOYEE_FULLY_VESTED_WITHDRAWAL.equals(parms.fullyVestedExplanation.getValue());
                    }
                },
                ContentConstants.EXPLANATION_FULLY_VESTED_WITHDRAWAL,
                2, 3);
        
        // scenario #1 - fully vested = y and no vyos submitted
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return ! parms.isServiceCreditUnspecified
                        && parms.fullyVested != null
                        && parms.fullyVested.getBooleanValue().booleanValue()
                        && parms.fullyVested.getCalculationUsage() == VestingRetriever.PARAMETER_USAGE_CODE_USED
                        && parms.vyos == null;
                    }
                },
                ContentConstants.EXPLANATION_FULLY_VESTED_NO_VYOS_SUBMITTED,
                2, 4);
        
        // scenario #2 - fully vested = y and vyos submitted
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return ! parms.isServiceCreditUnspecified
                        && parms.fullyVested != null
                        && parms.fullyVested.getBooleanValue().booleanValue()
                        && parms.fullyVested.getCalculationUsage() == VestingRetriever.PARAMETER_USAGE_CODE_USED
                        && parms.vyos != null;
                    }
                },
                ContentConstants.EXPLANATION_FULLY_VESTED_VYOS_SUBMITTED,
                2, 5);
        
        // elapsed time calculates 100%
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return parms.isServiceCreditEt
                        && (parms.fullyVested == null
                                || ! parms.fullyVested.getBooleanValue().booleanValue())
                        && parms.isPercentageFullyVested;
                    }
                },
                ContentConstants.EXPLANATION_FULLY_VESTED_ET,
                2, 6);
        
        // hours of service calculates 100%
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return parms.isServiceCreditHos
                        && (parms.fullyVested == null
                                || ! parms.fullyVested.getBooleanValue().booleanValue())
                        && parms.isPercentageFullyVested;
                    }
                },
                ContentConstants.EXPLANATION_FULLY_VESTED_HOS,
                2, 7);
        
        
        //**************************************************************************
        // Level 3 - Contract has vesting errors and/or employee has vesting errors
        //**************************************************************************
        
        builder.setLevel(EvaluationType.SHORT_CIRCUIT, 3);
        
        // scenario #23
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return parms.errors != null
                        && parms.errors.contains(VestingMessageType.CREDITING_METHOD_IS_UNSPECIFIED)
                        && parms.errors.contains(VestingMessageType.VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP);
                    }
                },
                ContentConstants.EXPLANATION_CREDITING_METHOD_UNSPECIFIED_VESTING_SCHEDULE_MISSING,
                3, 1);
        builder.setCondition( // scenario #21
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return parms.errors != null
                        && parms.errors.contains(VestingMessageType.CREDITING_METHOD_IS_UNSPECIFIED);
                    }
                },
                ContentConstants.EXPLANATION_CREDITING_METHOD_UNSPECIFIED,
                3, 2);
        
        // scenario #19 - employment status cancelled
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return (parms.fullyVested == null
                                || ! parms.fullyVested.getBooleanValue().booleanValue())
                                && parms.employmentStatus != null
                                && VestingConstants.EMPLOYMENT_STATUS_CANCELLED.equals(parms.employmentStatus.getStringValue());
                    }
                },
                ContentConstants.EXPLANATION_UNKNOWN_PROBLEM,
                3, 3);
        
        // un-named intermediate level
        builder.setLevel(EvaluationType.COMPLETE, 3, 4);
        
        // scenario #22 - missing vesting schedule for plan
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return ! parms.isServiceCreditUnspecified
                        && parms.errors != null
                        && parms.errors.contains(VestingMessageType.VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP);
                    }
                },
                ContentConstants.EXPLANATION_VESTING_SCHEDULE_MISSING,
                3, 4, 1);
        
        // scenario #11 - elapsed time: no employment status and calling application is not withdrawals
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return parms.isServiceCreditEt
                        && parms.withdrawalReasonCode == null
                        && parms.errors.contains(VestingMessageType.EMPLOYMENT_STATUS_NOT_PROVIDED);
                    }
                },
                ContentConstants.EXPLANATION_NO_EMPLOYMENT_STATUS_NO_WITHDRAWAL,
                3, 4, 2);
        
        // scenario #12 - elapsed time: no hire date
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return parms.isServiceCreditEt
                        && parms.errors != null
                        && parms.errors.contains(VestingMessageType.HIRE_DATE_NOT_PROVIDED);
                    }
                },
                ContentConstants.EXPLANATION_NO_HIRE_DATE,
                3, 4, 3);
        
        // scenario #17 - hours of service: no vyos and no plan ytd hours worked
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return parms.isServiceCreditHos
                        && parms.errors != null
                        && parms.errors.contains(VestingMessageType.PREVIOUS_YEARS_OF_SERVICE_AND_PLAN_YTD_HOURS_WORKED_NOT_PROVIDED);
                    }
                },
                ContentConstants.EXPLANATION_NO_VYOS_NO_PLAN_YTD_HOURS_WORKED,
                3, 4, 4);
        
        
        //********************************************************
        // Level 4 - Service credit not used to calculate vesting
        //********************************************************
        
        builder.setLevel(EvaluationType.SHORT_CIRCUIT, 4);
        builder.setCondition( // scenario #6 - vyos = as of date
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return ! parms.isServiceCreditUnspecified
                        && parms.vyos != null
                        && parms.vyos.getCalculationUsage() == VestingRetriever.PARAMETER_USAGE_CODE_USED
                        && parms.vyos.getEffectiveDate().equals(parms.asOfDate);
                    }
                },
                ContentConstants.EXPLANATION_VYOS_PROVIDED,
                4, 1);
        
        // vyos after as of date
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return parms.isServiceCreditHos
                        && parms.vyos != null
                        && parms.vyos.getCalculationUsage() == VestingRetriever.PARAMETER_USAGE_CODE_USED
                        && parms.vyos.getEffectiveDate().after(parms.asOfDate)
                        && parms.vyos.getEffectiveDate().equals(parms.vestingEffectiveDate);
                    }
                },
                ContentConstants.EXPLANATION_VYOS_PROVIDED_AFTER_AS_OF,
                4, 2);
        
        
        //****************************************************
        // Level 5 - Service credit used to calculate vesting
        //****************************************************
        
        builder.setLevel(EvaluationType.SHORT_CIRCUIT, 5);
        
        // elapsed time
        builder.setLevel(EvaluationType.COMPLETE, 5, 1);
        
        // un-named intermediate level
        builder.setLevel(EvaluationType.SHORT_CIRCUIT, 5, 1, 1);
        
        // scenario #13 - employment status not active and plan considers status fully vested
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        String esValue = null;
                        if (parms.employmentStatus != null) {
                            esValue = parms.employmentStatus.getStringValue();
                        }
                        return parms.isServiceCreditEt
                        && (parms.fullyVested == null
                                || ! parms.fullyVested.getBooleanValue().booleanValue())
                                && parms.employmentStatus != null
                                && ("R".equals(esValue) && (parms.vestingInfo.isFullyVestedOnRetirement() || parms.vestingInfo.isFullyVestedOnEarlyRetirement())
                                        || "D".equals(esValue) && parms.vestingInfo.isFullyVestedOnDeath()
                                        || "P".equals(esValue) && parms.vestingInfo.isFullyVestedOnDisability());
                    }
                },
                ContentConstants.EXPLANATION_EMPLOYMENT_STATUS_CONSIDERED_FULLY_VESTED,
                5, 1, 1, 1);
        
        // scenario #14 - employment status not active and plan does not consider status fully vested     
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        String esValue = null;
                        if (parms.employmentStatus != null) {
                            esValue = parms.employmentStatus.getStringValue();
                        }
                        return parms.isServiceCreditEt
                        && (parms.fullyVested == null
                                || ! parms.fullyVested.getBooleanValue().booleanValue())
                                && parms.employmentStatus != null
                                && ! VestingConstants.EMPLOYMENT_STATUS_ACTIVE.equals(esValue)
                                && ! VestingConstants.EMPLOYMENT_STATUS_CANCELLED.equals(esValue);
                    }
                },
                ContentConstants.EXPLANATION_EMPLOYMENT_STATUS_NOT_CONSIDERED_FULLY_VESTED,
                5, 1, 1, 2);
        
        // un-named intermediate level
        builder.setLevel(EvaluationType.SHORT_CIRCUIT, 5, 1, 2);
        
        // scenario #7 - no vyos override or vyos override and hire date is equal to or less than as of date        
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return parms.isServiceCreditEt
                        && (parms.fullyVested == null
                                || ! parms.fullyVested.getBooleanValue().booleanValue())
                        && parms.scty != null
                        && parms.scty.getBooleanValue().booleanValue()
                        && parms.hireDate != null
                        && ! parms.hireDate.getDateValue().after(parms.asOfDate);
                    }
                },
                ContentConstants.EXPLANATION_VYOS_HIRE_DATE_BEFORE_ASOFDATE,
                5, 1, 2, 1);
        
        // scenario #8 - no vyos or vyos override and hire date anniversary after the as of date
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return parms.isServiceCreditEt
                        && (parms.fullyVested == null
                                || ! parms.fullyVested.getBooleanValue().booleanValue())
                        && parms.scty != null
                        && ! parms.scty.getBooleanValue().booleanValue()
                        && parms.hireDate != null
                        && ! parms.hireDate.getDateValue().after(parms.asOfDate);
                    }
                },
                ContentConstants.EXPLANATION_VYOS_HIRE_DATE_AFTER_ASOFDATE,
                5, 1, 2, 2);
        
        // scenario #9 - vyos override before the as of date and hire date after the as of date
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return parms.isServiceCreditEt
                        && parms.vyos != null
                        && parms.vyos.getCalculationUsage() == VestingRetriever.PARAMETER_USAGE_CODE_USED
                        && parms.hireDate != null
                        && parms.hireDate.getCalculationUsage() == VestingRetriever.PARAMETER_USAGE_CODE_USED
                        && parms.hireDate.getDateValue().after(parms.asOfDate);
                    }
                },
                ContentConstants.EXPLANATION_VYOS_BEFORE_ASOFDATE_HIRE_DATE_AFTER_ASOFDATE,
                5, 1, 2, 3);
        
        // scenario #10 - no vyos override and hire date is greater than as of date selected
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return parms.isServiceCreditEt
                        && (parms.fullyVested == null
                                || ! parms.fullyVested.getBooleanValue().booleanValue())
                        && parms.vyos == null
                        && parms.hireDate != null
                        && parms.hireDate.getDateValue().after(parms.asOfDate);
                    }
                },
                ContentConstants.EXPLANATION_NO_VYOS_HIRE_DATE_AFTER_ASOFDATE,
                5, 1, 2, 4);
        
        // hos
        builder.setLevel(EvaluationType.SHORT_CIRCUIT, 5, 2);
        
        // scenario #20 - calling application = withdrawals, vesting calculated using more recent data than as of date
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return parms.isServiceCreditHos
                        && parms.withdrawalReasonCode != null
                        && parms.errors.contains(VestingMessageType.MORE_RECENT_DATA_USED_FOR_CALCULATION);
                    }
                },
                ContentConstants.EXPLANATION_USING_MORE_RECENT_ASOFDATE,
                5, 2, 1);
        
        // scenario #15 - no vyos override or vyos override is less than as of date and current year service met
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return parms.isServiceCreditHos
                        && (parms.fullyVested == null
                                || ! parms.fullyVested.getBooleanValue().booleanValue())
                        && parms.scty != null
                        && parms.scty.getBooleanValue().booleanValue();
                    }
                },
                ContentConstants.EXPLANATION_CREDITED_ADDITIONAL_YEAR,
                5, 2, 2);
        
        // scenario #16 - no vyos override or vyos override is less than as of date and current year service not met        
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return parms.isServiceCreditHos
                        && (parms.fullyVested == null
                                || ! parms.fullyVested.getBooleanValue().booleanValue())
                        && parms.scty != null
                        && ! parms.scty.getBooleanValue().booleanValue();
                    }
                },
                ContentConstants.EXPLANATION_NOT_CREDITED_ADDITIONAL_YEAR,
                5, 2, 3);
        
        // scenario #19 - unknown situation
        builder.setCondition(
                new EvaluationStrategy() {
                    public boolean evaluate(VestingExplanationParameters parms) {
                        return true;
                    }
                },
                ContentConstants.EXPLANATION_UNKNOWN_PROBLEM,
                6);
        
        builder.releaseObject();
        
        return model;
        
    }
    
    public List<Integer> retrieveExplanation(VestingInformation vestingInfo, Employee employee, EmployeeVestingInformation evi, String withdrawalReasonCode, Date asOfDate) {
        
        VestingExplanationParameters parms = new VestingExplanationParameters(vestingInfo, employee, evi, withdrawalReasonCode, asOfDate);
        return model.getCmaList(parms);
        
    }
    
    static class VestingExplanationParameters {
        
        private final String withdrawalReasonCode;
        private final Date asOfDate;
        private final VestingInformation vestingInfo;
        private final Employee employee;
        private final boolean isServiceCreditUnspecified;
        private final boolean isServiceCreditEt;
        private final boolean isServiceCreditHos;
        private final Date vestingEffectiveDate;
        private final Set<VestingMessageType> errors;
        private final VestingInputDescription fullyVested;
        private final VestingExplanation fullyVestedExplanation;
        private final VestingInputDescription vyos;
        private final VestingInputDescription hireDate;
        private final VestingInputDescription employmentStatus;
        private final VestingInputDescription scty;
        private final VestingInputDescription cyos;
        private final boolean isPercentageFullyVested;
        
        private VestingExplanationParameters(VestingInformation vestingInfo, Employee employee, EmployeeVestingInformation evi, String withdrawalReasonCode, Date asOfDate) {
            
            this.withdrawalReasonCode = withdrawalReasonCode;
            this.asOfDate = asOfDate;
            this.vestingInfo = vestingInfo;
            this.employee = employee;
            isServiceCreditUnspecified = VestingConstants.CreditingMethod.UNSPECIFIED.equals(evi.getRetrievalDetails().getCreditingMethod());
            isServiceCreditEt = VestingConstants.CreditingMethod.ELAPSED_TIME.equals(evi.getRetrievalDetails().getCreditingMethod());
            isServiceCreditHos = VestingConstants.CreditingMethod.HOURS_OF_SERVICE.equals(evi.getRetrievalDetails().getCreditingMethod());
            vestingEffectiveDate = evi.getVestingEffectiveDate();
            errors = (Set<VestingMessageType>) evi.getErrors();
            VestingRetrievalDetails details = evi.getRetrievalDetails();
            fullyVested = details.getEffectiveInput(VestingRetrievalDetails.PARAMETER_RAW_FULLY_VESTED);
            fullyVestedExplanation = details.getExplanation(VestingRetrievalDetails.EXPLANATION_EMPLOYEE_FULLY_VESTED);
            cyos = details.getEffectiveInput(VestingRetrievalDetails.PARAMETER_CALCULATED_COMPLETED_YEARS_OF_SERVICE);
            vyos = getLastSubmittedInput(
                    details,
                    VestingRetrievalDetails.PARAMETER_RAW_VESTED_YEARS_OF_SERVICE,
                    cyos == null ?
                            evi.getVestingEffectiveDate() :
                                cyos.getEffectiveDate()); // accounts for more recent parameter
            hireDate = details.getEffectiveInput(VestingRetrievalDetails.PARAMETER_RAW_HIRE_DATE, VestingConstants.HIGH_DATE);
            employmentStatus = details.getEffectiveInput(VestingRetrievalDetails.PARAMETER_RAW_EMPLOYMENT_STATUS);
            scty = details.getEffectiveInput(VestingRetrievalDetails.PARAMETER_CALCULATED_SERVICE_CREDITED_CURRENT_YEAR);
            isPercentageFullyVested = isPercentageFullyVested(evi);
            
        }
        
    }
    
    private static VestingInputDescription getLastSubmittedInput(VestingRetrievalDetails details, int parameterName, Date inclusiveCeiling) {
        
        VestingInputDescription submitted = null;

        if ((details != null) && (details.getInputs() != null)) {
            
            List<VestingInputDescription> history = (List<VestingInputDescription>) details.getInputs().get(parameterName);
            if (history != null) {
                
                history_loop:
                for (int i = history.size() - 1; i >= 0; i --) {
                    
                    VestingInputDescription candidate = history.get(i);
                    if (! candidate.getEffectiveDate().after(inclusiveCeiling)) {
                        
                        do {
                            candidate = history.get(i);
                            if (isSubmittedInput(candidate)) {
                                submitted = candidate;
                                break history_loop;
                            } else {
                                i -= 1;
                            }
                        } while (i >= 0);
                    
                    }
                }
            }
        }
        
        return submitted;
        
    }
    
    private static boolean isSubmittedInput(VestingInputDescription vid) {
        return vid != null
                && (vid.getCalculationUsage() == VestingRetriever.PARAMETER_USAGE_CODE_UNUSED
                    || vid.getCalculationUsage() == VestingRetriever.PARAMETER_USAGE_CODE_USED);
    }
    
    private static boolean isPercentageFullyVested(EmployeeVestingInformation evi) {
        
        boolean isFullyVested = true;
        
        for (Object entry : evi.getMoneyTypeVestingPercentages().entrySet()) {
            MoneyTypeVestingPercentage mtvp = (MoneyTypeVestingPercentage) ((Map.Entry) entry).getValue();
            if (isMoneyTypeCalculated(mtvp)) {
                BigDecimal castPct = mtvp.getPercentage();
                if (castPct == null
                        || VestingConstants.FULLY_VESTED_PERCENTAGE.compareTo(castPct) != 0) {
                    isFullyVested = false;
                    break;
                }
            }
        }
        
        return isFullyVested;
        
    }
    
    private static boolean isMoneyTypeCalculated(MoneyTypeVestingPercentage mtvp) {
        
        return
        ! EmployeeConstants.MoneyTypeGroup.MONEY_TYPE_UM.equals(mtvp.getMoneyTypeGroup())
        && ! EmployeeConstants.MoneyTypeGroup.MONEY_TYPE_EE.equals(mtvp.getMoneyTypeGroup())
        && ! mtvp.isMoneyTypeFullyVested();
        
    }
    
}
