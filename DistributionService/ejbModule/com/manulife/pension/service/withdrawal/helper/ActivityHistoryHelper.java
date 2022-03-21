package com.manulife.pension.service.withdrawal.helper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.AnyPredicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.distribution.dao.ActivityDetailDao;
import com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao;
import com.manulife.pension.service.distribution.dao.ActivitySummaryDao;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.ActivityDetail;
import com.manulife.pension.service.distribution.valueobject.ActivityDynamicDetail;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.employee.EmploymentStatus;
import com.manulife.pension.service.employee.valueobject.Employee;
import com.manulife.pension.service.security.SystemUser;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.vesting.MoneyTypeVestingPercentage;
import com.manulife.pension.service.vesting.VestingException;
import com.manulife.pension.service.withdrawal.common.WithdrawalRequestMoneyTypeByIdPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalVestingEngine;
import com.manulife.pension.service.withdrawal.domain.Withdrawal;
import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.exception.WithdrawalActivityHistoryException;
import com.manulife.pension.service.withdrawal.valueobject.Activity;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivityDetail;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivitySummary;
import com.manulife.pension.service.withdrawal.valueobject.SystemOfRecordValues;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;
import com.manulife.util.converter.DateConverter;
import com.manulife.util.converter.StringConverter;

/**
 * Helper class for activity history.
 * 
 * @author Dennis Snowdon
 */
public final class ActivityHistoryHelper {

    /**
     * SYSTEM_OF_RECORD_VALUE_NOT_APPLICABLE.
     */
    private static final String SYSTEM_OF_RECORD_VALUE_NOT_APPLICABLE = "n/a";

    private static final Logger logger = Logger.getLogger(ActivityHistoryHelper.class);

    /**
     * add private constructor so people can't create an instance of this class.
     */
    private ActivityHistoryHelper() {
    }

    /**
     * Saves a system of record snapshot.
     * 
     * <pre>
     * Procedure is
     * 1. delete all SOR dynamic detail records that are going to be 
     *    replaced with new values ( This is achieved by iterating
     *    over the existing withdrawal money types and deleting the
     *    SOR value for each of them). Note - If a money type 
     *    becomes n/a for contract but has a SOR value then 
     *    it will not be deleted.
     * 2. delete all non-parent rows from activity detail -- Do not
     *    delete the money type parent row since there may be 
     *    orphaned SOR values for money types that become n/a for the
     *    contract
     * 3. insert/update the money-type parent row.  Need to explicitly
     *    insertUpdate this row since it won't be there on the first
     *    save, but it will be there for future saves
     * 4. insert the activity detail records
     * 5. insert the activity dynamic detail records.
     * </pre>
     * 
     * @param withdrawal the current withdrawal request
     * @throws DistributionServiceException thrown if there is an error
     */
    private static void saveSystemOfRecordSnapShot(final Withdrawal withdrawal)
            throws DistributionServiceException {

        ActivityDetailDao detailDao = new ActivityDetailDao();
        ActivityDynamicDetailDao dynamicDao = new ActivityDynamicDetailDao();
        Collection<WithdrawalActivityDetail> activityDetailInserts = new ArrayList<WithdrawalActivityDetail>();
        Collection<WithdrawalActivityDetail> activityDetailInsertUpdates = new ArrayList<WithdrawalActivityDetail>();
        Collection<WithdrawalActivityDetail> activityDetailDeletes = new ArrayList<WithdrawalActivityDetail>();

        Collection<ActivityDynamicDetail> adds = new ArrayList<ActivityDynamicDetail>();
        WithdrawalRequest wr = withdrawal.getWithdrawalRequest();
        Integer submissionId = wr.getSubmissionId();
        Integer contractId = wr.getContractId();
        Integer userProfileId = (int) wr.getPrincipal().getProfileId();
        String sorActionCode = WithdrawalActivitySummary.ACTION_CODE_SYSTEM_OF_RECORD;
        SystemOfRecordValues sorValues = getSystemOfRecordValues(withdrawal);
        WithdrawalActivityDetail ad = null;

        ad = new WithdrawalActivityDetail();
        ad.setActionCode(sorActionCode);
        ad.setLastUpdated(withdrawal.getLastSavedTimestamp());
        ad.setLastUpdatedById(userProfileId);
        ad.setItemNumber(WithdrawalFieldDef.STATE_OF_RESIDENCE.getId());
        ad.setValue(sorValues.getWithdrawalValues().get(WithdrawalFieldDef.STATE_OF_RESIDENCE));
        activityDetailInserts.add(ad);
        activityDetailDeletes.add(ad);

        ad = new WithdrawalActivityDetail();
        ad.setActionCode(sorActionCode);
        ad.setLastUpdated(withdrawal.getLastSavedTimestamp());
        ad.setLastUpdatedById(userProfileId);
        ad.setItemNumber(WithdrawalFieldDef.EVENT_DATE.getId());
        ad.setValue(sorValues.getWithdrawalValues().get(WithdrawalFieldDef.EVENT_DATE));
        activityDetailInserts.add(ad);
        activityDetailDeletes.add(ad);

        ad = new WithdrawalActivityDetail();
        ad.setActionCode(sorActionCode);
        ad.setLastUpdated(withdrawal.getLastSavedTimestamp());
        ad.setLastUpdatedById(userProfileId);
        ad.setItemNumber(WithdrawalFieldDef.DATE_OF_BIRTH.getId());
        ad.setValue(sorValues.getWithdrawalValues().get(WithdrawalFieldDef.DATE_OF_BIRTH));
        activityDetailInserts.add(ad);
        activityDetailDeletes.add(ad);

        if (sorValues.getMoneyTypeValues().size() > 0) {
            ad = new WithdrawalActivityDetail();
            ad.setActionCode(sorActionCode);
            ad.setLastUpdated(withdrawal.getLastSavedTimestamp());
            ad.setLastUpdatedById(userProfileId);
            ad.setItemNumber(WithdrawalFieldDef.DYN_MONEY_TYPE);
            activityDetailInsertUpdates.add(ad);
        }
        for (String moneyTypeId : sorValues.getMoneyTypeValues().keySet()) {
            ActivityDynamicDetail add = new ActivityDynamicDetail();
            add.setItemNumber(WithdrawalFieldDef.DYN_MONEY_TYPE);
            add.setSecondaryNumber(MoneyTypeFieldDef.MT_VESTING_PERCENT.getId());
            add.setSecondaryName(moneyTypeId);
            add.setLastUpdated(withdrawal.getLastSavedTimestamp());
            add.setLastUpdatedById(userProfileId);
            add.setTypeCode(sorActionCode);
            add.setValue(sorValues.getMoneyTypeValues().get(moneyTypeId).get(
                    MoneyTypeFieldDef.MT_VESTING_PERCENT));
            adds.add(add);
        }

        // delete all of the existing snapshopt records first
        // since we are creating new ones.
        // If a money type existed when the submission
        // was created and then became n/a, then
        // its SOR value will not be deleted since it will not
        // get added to 'adds' variable since it is not in the participants
        // money list anymore. the activity history
        // comparison will have 1. a valid submitted value,
        // 2. a null current value, and 3. a SOR value.
        // if (3) is supposed to be blank since the money type
        // no longer exists, then will need to add code
        // that deletes all of they dynamic detail SOR values
        // for money types that no longer exist. This should be done
        // in this funciton.
        // i.e. adds = getSORYMonetyTypes()
        // foreach (adds)
        // if !moneyType existsf for participant then
        // deleteList.add(adds)
        // dynamicDao.delete(deleteList);

        dynamicDao.delete(submissionId, contractId, userProfileId, adds);
        detailDao.delete(submissionId, contractId, userProfileId, activityDetailDeletes);

        detailDao
                .insertUpdate(submissionId, contractId, userProfileId, activityDetailInsertUpdates, WithdrawalActivityDetail.class);

        detailDao.insert(submissionId, contractId, userProfileId, activityDetailInserts);
        dynamicDao.insert(submissionId, contractId, userProfileId, adds);
    }

    /**
     * Returns a value object containing the system of record values. This should be called when
     * either a. saving system of record snapshot b. using sor for current comparison ( only dont
     * for BeforeEndState requests )
     * 
     * if these values are used for comparison ( usedForComparison == true), then the money type
     * vesting percentages are not obtained from the system. They are instead obtained from current
     * request. Also need to call the the vesting engine, IF the vesting engine has not been called
     * yet.
     * 
     * 
     * @param withdrawal The withdrawal request
     * @return a {@link SystemOfRecordValues} object containing the values
     * @throws DistributionServiceException thrown if there is an error
     */
    public static SystemOfRecordValues getSystemOfRecordValues(final Withdrawal withdrawal)
            throws DistributionServiceException {

        SystemOfRecordValues values = new SystemOfRecordValues();

        WithdrawalRequest wr = withdrawal.getWithdrawalRequest();
        Integer contractId = wr.getContractId();
        Employee empl;
        // get the necessary objects to pull data from.
        try {
            empl = EmployeeServiceDelegate.getInstance("PS").getEmployeeByProfileId(
                    new Long(wr.getEmployeeProfileId()), contractId, null);
        } catch (SystemException e) {
            throw new WithdrawalActivityHistoryException(e, "DraftState", "saveActivityHistory",
                    "failed to call employee service");
        }
        // state of residence
        values.getWithdrawalValues().put(
                WithdrawalFieldDef.STATE_OF_RESIDENCE,
                empl.getEmployeeDetailVO() != null ? empl.getEmployeeDetailVO()
                        .getResidenceStateCode() : "");

        // event date
        String value = "";
        String employmentStatusCode = "";
        if (empl.getEmployeeDetailVO() != null
                && empl.getEmployeeDetailVO().getEmploymentStatusEffDate() != null) {
            Date d = empl.getEmployeeDetailVO().getEmploymentStatusEffDate();
            value = new StringConverter(null).convert(java.lang.String.class, d).toString();
            employmentStatusCode = empl.getEmployeeDetailVO().getEmploymentStatusCode();
        }
        // only set the event date if the employment status code is valid
        if (StringUtils.isNotEmpty(employmentStatusCode)
                && StringUtils.equals(employmentStatusCode, EmploymentStatus.Terminated)
                || StringUtils.equals(employmentStatusCode, EmploymentStatus.Retired)
                || StringUtils.equals(employmentStatusCode,
                        EmploymentStatus.TotalPermanentDisability)) {
            values.getWithdrawalValues().put(WithdrawalFieldDef.EVENT_DATE, value);
        }

        // date of birth
        value = "";
        if (empl.getEmployeeDetailVO() != null && empl.getEmployeeDetailVO().getBirthDate() != null) {
            Date d = empl.getEmployeeDetailVO().getBirthDate();
            value = new StringConverter(null).convert(java.lang.String.class, d).toString();
        }

        values.getWithdrawalValues().put(WithdrawalFieldDef.DATE_OF_BIRTH, value);

        // money types
        //
        boolean isCritical = false;
        boolean isNonCritical = false;
        WithdrawalVestingEngine vestingEngine = new WithdrawalVestingEngine();
        EmployeeVestingInformation employeeVestingInformation = null;
        try {
            employeeVestingInformation = vestingEngine.getEmployeeVestingInformation(withdrawal
                    .getWithdrawalRequest());
        } catch (VestingException e) {
            isCritical = true;
        }
        if (!isCritical) {
            isNonCritical = vestingEngine.isVestingNonCriticalErrorWithWarning(
                    employeeVestingInformation, withdrawal.getWithdrawalRequest());
        }

        MoneyTypeVestingPercentage vestingType;
        for (WithdrawalRequestMoneyType mt : wr.getMoneyTypes()) {
            String myVal = "";
            BigDecimal vestingValue = null;
            if (!(isCritical)) {
                vestingType = (MoneyTypeVestingPercentage) employeeVestingInformation
                        .getMoneyTypeVestingPercentages()
                        .get(StringUtils.trim(mt.getMoneyTypeId()));
                if (vestingType != null) {
                    vestingValue = vestingType.getPercentage();
                } // fi
            } // fi
            Map<MoneyTypeFieldDef, String> mtValues = new HashMap<MoneyTypeFieldDef, String>();
            if (isNonCritical && vestingValue == null) {
                myVal = "unavailable";
            } else if (!isCritical && vestingValue != null) {
                myVal = new DecimalFormat("#0.000%").format(vestingValue.doubleValue()
                        / GlobalConstants.ONE_HUNDRED.intValue());
            }
            mtValues.put(MoneyTypeFieldDef.MT_VESTING_PERCENT, myVal);
            values.getMoneyTypeValues().put(mt.getMoneyTypeId(), mtValues);
        }

        return values;
    }

    /**
     * This method creates a list of activites for the {@link WithdrawalFieldDef} fields.
     * 
     * @param withdrawal The current withdrawal
     * @param details A list of details to use a the source
     * @param withdrawalSORValues A map of 'system of record' values
     * @param state the withdrawal state
     * @return a list of activties for {@link WithdrawalFieldDef} fields
     * @throws DistributionServiceException thrown if there is an error.
     */
    public static Collection<Activity> getWithdrawalActivities(final Withdrawal withdrawal,
            final List<WithdrawalActivityDetail> details,
            final Map<WithdrawalFieldDef, String> withdrawalSORValues,
            final WithdrawalStateEnum state) throws DistributionServiceException {

        Collection<Activity> activities = new ArrayList<Activity>();
        Map<WithdrawalFieldDef, Activity> fieldToActivityMap = new HashMap<WithdrawalFieldDef, Activity>();

        Converter converter = new DateConverter(null);
        ConvertUtils.register(converter, java.util.Date.class);
        converter = new StringConverter(null);
        ConvertUtils.register(converter, java.lang.String.class);
        // withdrawal fields
        // 1. item name
        // 2. item number
        // 3. current value;
        // 4 original value;
        // 5. last updated user profile id
        // 6. last updated date
        // 7. system of record value
        for (ActivityDetail detail : details) {
            if (detail.getItemNumber() >= WithdrawalFieldDef.DYN_MONEY_TYPE) {
                continue;
            }

            WithdrawalFieldDef field = WithdrawalFieldDef.getFieldFromItemNumber(detail
                    .getItemNumber());
            if (fieldToActivityMap.get(field) == null) {
                fieldToActivityMap.put(field, new Activity());
            }
            Activity activity = fieldToActivityMap.get(field);
            // 1.
            activity.setItemName(field.getName());
            if (field == WithdrawalFieldDef.EVENT_DATE) {
                activity.setItemName(field.getEventName(withdrawal.getWithdrawalRequest()));
            }
            // 2.
            activity.setItemNo(detail.getItemNumber());
            // 3.
            activity.setCurrentValue(field.getValue(withdrawal.getWithdrawalRequest()));

            setCommonAttributes(activity, detail.getTypeCode(), detail.getValue(), detail
                    .getLastUpdated(), detail.getLastUpdatedById());

            activity.setSortOrder(ActivitiesSortDef.getInstance().getSortValue(field));
        }
        // 7. populate the system of record values for the withdrawal fields
        for (WithdrawalFieldDef field : withdrawalSORValues.keySet()) {
            fieldToActivityMap.get(field).setSystemOfRecordValue(withdrawalSORValues.get(field));
        }

        for (WithdrawalFieldDef field : fieldToActivityMap.keySet()) {
            Activity activity = fieldToActivityMap.get(field);
            if (addRow(activity, field.getSystemOfRecord(), withdrawal.getWithdrawalRequest()
                    .getIsParticipantCreated())) {

                if (withdrawal.getWithdrawalRequest().getIsParticipantCreated()) {
                    activity.setShowUserIdAndLastUpdated(isShowUserIdChangedFromSOR(activity));
                    if (activity.getLastUpdateUserProfileId() == SystemUser.DATABASE.getProfileId()) {
                        activity.setShowLastUpdatedTimeAsNa(true);
                    }

                } else {
                    activity.setShowUserIdAndLastUpdated(isShowUserIdAndLastUpdated(activity));
                }

                if (!field.getSystemOfRecord()) {
                    activity.setSystemOfRecordValue(SYSTEM_OF_RECORD_VALUE_NOT_APPLICABLE);
                }
                activities.add(activity);
            }
        }
        // 8 for each of the activities, obtain the

        return activities;
    }

    /**
     * This method creates a list of activities for the {@link MoneyTypeFieldDef} fields.
     * 
     * @param withdrawal The current withdrawal
     * @param dynDetails A list of dynamic details to use a the source
     * @param moneyTypeSORValues A map of 'system of record' values
     * @param state the withdrawal state
     * @return a list of activities for {@link WithdrawalFieldDef} fields
     * @throws DistributionServiceException thrown if there is an error.
     */
    public static Collection<Activity> getMoneyTypeActivities(final Withdrawal withdrawal,
            final List<ActivityDynamicDetail> dynDetails,
            final Map<String, Map<MoneyTypeFieldDef, String>> moneyTypeSORValues,
            final WithdrawalStateEnum state) throws DistributionServiceException {

        Collection<Activity> activities = new ArrayList<Activity>();

        // mapping money type id to a map of field/activities
        // that is, for each money type id there is a row for each field.
        Map<String, Map<MoneyTypeFieldDef, Activity>> keyToFieldValueMap = new HashMap<String, Map<MoneyTypeFieldDef, Activity>>();
        Map<String, String> moneyTypeMap = new HashMap<String, String>();
        for (WithdrawalRequestMoneyType mt : withdrawal.getWithdrawalRequest().getMoneyTypes()) {
            moneyTypeMap.put(mt.getMoneyTypeId(),
                    StringUtils.isNotBlank(mt.getMoneyTypeName()) ? mt.getMoneyTypeName() : mt
                            .getMoneyTypeAliasId());
        }

        for (ActivityDynamicDetail detail : dynDetails) {
            Activity activity;
            if (detail.getItemNumber().intValue() != WithdrawalFieldDef.DYN_MONEY_TYPE.intValue()) {
                continue;
            }

            MoneyTypeFieldDef field = MoneyTypeFieldDef.getFieldFromItemNumber(detail
                    .getSecondaryNumber());
            String moneyTypeId = detail.getSecondaryName();
            if (keyToFieldValueMap.get(moneyTypeId) == null) {
                keyToFieldValueMap.put(moneyTypeId, new HashMap<MoneyTypeFieldDef, Activity>());
            }
            if (keyToFieldValueMap.get(moneyTypeId).get(field) == null) {
                keyToFieldValueMap.get(moneyTypeId).put(field, new Activity());
            }
            activity = keyToFieldValueMap.get(moneyTypeId).get(field);

            // 1.
            activity.setItemName(field.getName() + " - "
                    + moneyTypeMap.get(detail.getSecondaryName()));
            // 2
            activity.setItemNo(WithdrawalFieldDef.DYN_MONEY_TYPE);
            activity.setSecondaryNo(detail.getSecondaryNumber());
            activity.setSecondaryName(detail.getSecondaryName());

            // 3.
            for (WithdrawalRequestMoneyType mt : withdrawal.getWithdrawalRequest().getMoneyTypes()) {
                if (mt.getMoneyTypeId().equals(moneyTypeId)) {
                    activity.setCurrentValue(field.getValue(withdrawal.getWithdrawalRequest(), mt));
                    break;
                }
            }
            // 4.
            setCommonAttributes(activity, detail.getTypeCode(), detail.getValue(), detail
                    .getLastUpdated(), detail.getLastUpdatedById());

        }
        /*
         * 7. populate the system of record values for the money type fields ex. my map i generate
         * in this function a. xxMT1xx -- b. vesting percentage -- c. Activity -- d. withdrwal
         * amount -- e. Activity the map that represents SOR values f. xxMT1xx -- g. vesting
         * percentage -- h String
         * 
         * the for loop below copies the value of (h) into (c) ps. the auto-formatting totally
         * screwed up the helpful visual representation above.
         */
        for (String moneyTypeId : moneyTypeSORValues.keySet()) {
            final Map<MoneyTypeFieldDef, String> moneyTypeSORValueMap = moneyTypeSORValues
                    .get(moneyTypeId);
            if (moneyTypeSORValueMap != null) {
                for (MoneyTypeFieldDef field : moneyTypeSORValueMap.keySet()) {
                    final Map<MoneyTypeFieldDef, Activity> keyToFieldValue = keyToFieldValueMap
                            .get(moneyTypeId);
                    // Only set the System of Record value if we had an original value to start
                    // with.
                    if (keyToFieldValue != null && keyToFieldValue.containsKey(field)) {
                        keyToFieldValue.get(field).setSystemOfRecordValue(
                                moneyTypeSORValueMap.get(field));
                    }
                }
            }
        }
        // do the comparison
        // all fields use the rule:
        // if we're not comparing with system of record, then show "n/a"...except vesting percent.
        // for vesting, if we had a critical error we don't do system of record comparison
        // but then we show "blank" for that field instead of n/a
        for (Map<MoneyTypeFieldDef, Activity> map : keyToFieldValueMap.values()) {
            for (MoneyTypeFieldDef field : map.keySet()) {
                Activity activity = map.get(field);
                boolean systemOfRecordComparison = field.getSystemOfRecord();
                // special rule for vesting critical exceptions
                if (withdrawal.getWithdrawalRequest().getVestingCriticalError()
                        && field == MoneyTypeFieldDef.MT_VESTING_PERCENT) {
                    systemOfRecordComparison = false;
                }
                if (addRow(activity, systemOfRecordComparison, withdrawal.getWithdrawalRequest()
                        .getIsParticipantCreated())) {
                    activity.setShowUserIdAndLastUpdated(isShowUserIdAndLastUpdated(activity));
                    if (!field.getSystemOfRecord()) {
                        if (withdrawal.getWithdrawalRequest().getVestingCriticalError()) {
                            activity.setSystemOfRecordValue("");
                        } else {
                            activity.setSystemOfRecordValue(SYSTEM_OF_RECORD_VALUE_NOT_APPLICABLE);
                        }
                    }
                    if (withdrawal.getWithdrawalRequest().getIsParticipantCreated()) {
                        if (withdrawal.getWithdrawalRequest().getVestingCalledInd()) {
                            activities.add(activity);
                        }
                    } else {
                        activities.add(activity);
                    }

                }
            }
        }
        Set<Activity> sortedSet = new TreeSet<Activity>(new MoneyTypeComparator());
        sortedSet.addAll(activities);
        activities.clear();
        int itemNumber = 1;
        for (Activity activity : sortedSet) {
            activity.setSortOrder(ActivitiesSortDef.getInstance().getMoneyTypeSortValue(
                    itemNumber++));
        }
        return sortedSet;
    }

    /**
     * Sets the original value, and the user id, and last updated date.
     * 
     * The original value is used if the action code says so. The last updated user and timestamp
     * are 1. the original values if no saved record exists. 2. the saved values if a saved record
     * exists.
     * 
     * the problem is that i don't know if a saved record exists or not as i'm iterating over the
     * records. Therefore, i will use a boolean variable that says if a saved record was used, so
     * that i know not to overwrite the saved value if the original comes after and i will know to
     * use the original if the boolean is false
     * 
     * 
     * 
     * @param activity the activity to set the values on.
     * @param actionCode the action code
     * @param value to potentially be used as original value
     * @param lastUpdated the last updated date
     * @param lastUpdatedById the last update user id
     */
    private static void setCommonAttributes(final Activity activity, final String actionCode,
            final String value, final Date lastUpdated, final Integer lastUpdatedById) {

        if (StringUtils.equals(actionCode, WithdrawalActivitySummary.ACTION_ORIGINAL_VALUE)) {
            activity.setOriginalValue(value);
            if (!activity.getHasSavedValue()) {
                activity.setLastUpdateUserProfileId(lastUpdatedById);
                activity.setLastUpdated(lastUpdated);
            }
        } else if (StringUtils.equals(actionCode, WithdrawalActivitySummary.ACTION_CODE_SAVED)) {
            activity.setLastUpdateUserProfileId(lastUpdatedById);
            activity.setLastUpdated(lastUpdated);
            activity.setHasSavedValue(true);
        }
    }

    /**
     * This method creates a list of activities for the {@link PayeeFieldDef} fields.
     * 
     * @param withdrawal The current withdrawal
     * @param dynDetails A list of dynamic details to use a the source
     * @param state the withdrawal state
     * @return a list of activties for {@link WithdrawalFieldDef} fields
     * @throws DistributionServiceException thrown if there is an error.
     */
    public static Collection<Activity> getPayeeActivities(final Withdrawal withdrawal,
            final List<ActivityDynamicDetail> dynDetails, final WithdrawalStateEnum state)
            throws DistributionServiceException {

        Collection<Activity> activities = new ArrayList<Activity>();

        // mapping money type id to a map of field/activities
        // that is, for each money type id there is a row for each field.
        Map<String, Map<PayeeFieldDef, Activity>> keyToFieldValueMap = new HashMap<String, Map<PayeeFieldDef, Activity>>();

        int numberOfPayees = 0;
        final Collection<Recipient> recipients = withdrawal.getWithdrawalRequest()
                .getRecipients();

        if (CollectionUtils.isNotEmpty(recipients)) {
            for (Recipient withdrawalRequestRecipient : recipients) {

                final Collection<Payee> payees = withdrawalRequestRecipient
                        .getPayees();
                if (CollectionUtils.isNotEmpty(payees)) {
                    numberOfPayees = payees.size();
                } // fi
            } // end for
        } // fi

        for (ActivityDynamicDetail detail : dynDetails) {
            Activity activity;
            if (detail.getItemNumber().intValue() != WithdrawalFieldDef.DYN_PAYEE_TYPE.intValue()) {
                continue;
            }

            PayeeFieldDef field = PayeeFieldDef.getFieldFromItemNumber(detail.getSecondaryNumber());
            String payeeNo = detail.getSecondaryName();
            if (keyToFieldValueMap.get(payeeNo) == null) {
                keyToFieldValueMap.put(payeeNo, new HashMap<PayeeFieldDef, Activity>());
            }
            if (keyToFieldValueMap.get(payeeNo).get(field) == null) {
                keyToFieldValueMap.get(payeeNo).put(field, new Activity());
            }
            activity = keyToFieldValueMap.get(payeeNo).get(field);

            // 1.
            if (numberOfPayees == 1) {
                activity.setItemName("Payee " + " " + field.getName());
            } else if (numberOfPayees == 2) {
                activity.setItemName("Payee " + detail.getSecondaryName() + " " + field.getName());
            }
            // 2
            activity.setItemNo(WithdrawalFieldDef.DYN_PAYEE_TYPE);
            activity.setSecondaryNo(detail.getSecondaryNumber());
            activity.setSecondaryName(detail.getSecondaryName());

            // 3.
            for (Recipient recipient : withdrawal.getWithdrawalRequest()
                    .getRecipients()) {
                for (Payee payee : recipient.getPayees()) {
                    if (payee.getPayeeNo().equals(new Integer(payeeNo))) {
                        activity.setCurrentValue(field.getValue(withdrawal.getWithdrawalRequest(),
                                (WithdrawalRequestPayee)payee));
                        break;
                    }
                }
            }
            // 4.
            setCommonAttributes(activity, detail.getTypeCode(), detail.getValue(), detail
                    .getLastUpdated(), detail.getLastUpdatedById());

            activity.setSortOrder(ActivitiesSortDef.getInstance().getSortValue(field, payeeNo));
        }
        // do the comparison
        for (Map<PayeeFieldDef, Activity> map : keyToFieldValueMap.values()) {
            for (Activity activity : map.values()) {
                if (addRow(activity, false, withdrawal.getWithdrawalRequest()
                        .getIsParticipantCreated())) {
                    activity.setShowUserIdAndLastUpdated(isShowUserIdAndLastUpdated(activity));
                    activity.setSystemOfRecordValue(SYSTEM_OF_RECORD_VALUE_NOT_APPLICABLE);
                    activities.add(activity);
                }
            }
        }

        return activities;
    }

    /**
     * This method creates a list of activites for the delarations.
     * 
     * @param withdrawal The current withdrawal
     * @param dynDetails A list of dynamic details to use a the source
     * @param state the withdrawal state
     * @return a list of activties for {@link WithdrawalFieldDef} fields
     */
    public static Collection<Activity> getDelarationActivities(final Withdrawal withdrawal,
            final List<ActivityDynamicDetail> dynDetails, final WithdrawalStateEnum state) {

        Collection<Activity> activities = new ArrayList<Activity>();
        // mapping money type id to a map of field/activities
        // that is, for each money type id there is a row for each field.
        Map<String, Activity> valueMap = new HashMap<String, Activity>();
       
        int declNumber = 1;
        for (ActivityDynamicDetail detail : dynDetails) {
            Activity activity;
            if (detail.getItemNumber().intValue() != WithdrawalFieldDef.DYN_DECLARATION_TYPE
                    .intValue()) {
                continue;
            }

            String declCode = detail.getSecondaryName();
            if (valueMap.get(declCode) == null) {
                valueMap.put(declCode, new Activity());
            }
            activity = valueMap.get(declCode);

            // 1.
            activity.setItemName("DECL " + detail.getSecondaryName());
            // 2
            activity.setItemNo(WithdrawalFieldDef.DYN_DECLARATION_TYPE);
            activity.setSecondaryName(detail.getSecondaryName());

            // 3.
            if (activity.getCurrentValue() == null) {
                boolean found = false;
                for (Declaration declaration : withdrawal.getWithdrawalRequest()
                        .getDeclarations()) {
                    if (declaration.getTypeCode().equals(declCode)) {
                        found = true;
                        break;
                    }
                }
                // Need to use blank if declaration is not available (i.e. suppressed)
                boolean isAvailable = withdrawal.getWithdrawalRequest().isDeclarationAvailable(
                        declCode);
				activity.setCurrentValue(found ? "Yes" : ((isAvailable && !isRiskIndicatorDeclaration(
						withdrawal.getWithdrawalRequest(), declCode)) ? "No" : StringUtils.EMPTY));
			}
			// 4.
            setCommonAttributes(activity, detail.getTypeCode(), detail.getValue(), detail
                    .getLastUpdated(), detail.getLastUpdatedById());
            activity.setSortOrder(ActivitiesSortDef.getInstance().getDeclarationSortValue(
                    declNumber++));

        }
        // do the comparison
        for (Activity activity : valueMap.values()) {
            if (addRow(activity, false, withdrawal.getWithdrawalRequest().getIsParticipantCreated())) {
                activity.setShowUserIdAndLastUpdated(isShowUserIdAndLastUpdated(activity));
                activity.setSystemOfRecordValue(SYSTEM_OF_RECORD_VALUE_NOT_APPLICABLE);
                activities.add(activity);
            }
            if (StringUtils.equals(WithdrawalRequestDeclaration.AT_RISK_TRANSACTION_TYPE_CODE,activity.getSecondaryName())) {
            	if (addRow(activity, false, false)) {
                    activity.setShowUserIdAndLastUpdated(isShowUserIdAndLastUpdated(activity));
                    activity.setSystemOfRecordValue(SYSTEM_OF_RECORD_VALUE_NOT_APPLICABLE);
                    activities.add(activity);
            	}
            }
        }

        // now set the sort order
        return activities;
    }

    /**
     * This function does an equals on trimToEmpty versions of the strings.
     * 
     * This is necessary since ResultSet.getString() returns null for a blank CHARACTER field and ""
     * for a blank VARCHAR field.
     * 
     * @param s1 The first string to compare
     * @param s2 The second string to compare
     * @return true if they are equal.
     */
    private static boolean equals(final String s1, final String s2) {
        return StringUtils.equals(StringUtils.trimToEmpty(s1), StringUtils.trimToEmpty(s2));
    }

    /**
     * returns true if the userid and last update are to be shown.
     * 
     * @param activity The activity to examine
     * @return true if we show the values
     */
    private static Boolean isShowUserIdAndLastUpdated(final Activity activity) {
        return !equals(activity.getOriginalValue(), activity.getCurrentValue());
    }

    /**
     * returns true if the the SOR and updated request values are changed.
     * 
     * @param activity
     * @return
     */
    private static Boolean isShowUserIdChangedFromSOR(final Activity activity) {
        Boolean isChanged = Boolean.FALSE;
        if (StringUtils.isNotEmpty(activity.getSystemOfRecordValue())) {
            isChanged = !equals(activity.getSystemOfRecordValue(), activity.getCurrentValue());
        }
        return isChanged;
    }

    /**
     * finds out if this activity should be added to the activity history change list. *
     * 
     * @param activity the activity to compare
     * @param systemOfRecord true if the function should also do a system of record comparison
     * @param originalRequestValue true if the withdrawal request is Participant inititaed
     * @return true if the row is to be added
     */
    private static boolean addRow(final Activity activity, final Boolean systemOfRecord,
            final boolean originalRequestValue) {

        boolean returnVal = false;
        String val1 = activity.getSystemOfRecordValue();
        String val2 = activity.getOriginalValue();
        String val3 = activity.getCurrentValue();
        if (!originalRequestValue) {
            returnVal = !equals(val2, val3);
        }

        if (systemOfRecord && StringUtils.isNotBlank(val1)) {
            returnVal |= !equals(val1, val3);
        }
        return returnVal;
    }

    /**
     * save the user and the date of any changed fields. this means we need to know the following:
     * 1. was a field changed ( which implies ) a. compare the value of this request before it was
     * saved, to b. the value of the request right now.
     * 
     * 1 way is to read the withdrawal request again and compare that against the withdrawal request
     * being saved for each of the fields.
     * 
     * 
     * if the values differe
     * 
     * 
     * @param newWithdrawal The withdrawal used for field values
     * @param oldWithdrawal The old withdrawal used for field values
     * @throws DistributionServiceException Thrown if an error is encountered
     */
    private static void saveChangedFields(final Withdrawal newWithdrawal,
            final Withdrawal oldWithdrawal) throws DistributionServiceException {

        final WithdrawalRequest newObj = newWithdrawal.getWithdrawalRequest();
        boolean parentKeyAdded = false; // used to determine if we need to
        // potentially add a parent key for a
        // detail record.
        final WithdrawalRequest oldObj = oldWithdrawal.getWithdrawalRequest();

        List<WithdrawalActivityDetail> ads = new ArrayList<WithdrawalActivityDetail>();
        List<ActivityDynamicDetail> adds = new ArrayList<ActivityDynamicDetail>();
        for (WithdrawalFieldDef field : WithdrawalFieldDef.values()) {
            String newValue = field.getValue(newObj);
            String oldValue = field.getValue(oldObj);
            if (!StringUtils.equals(oldValue, newValue)) {
                WithdrawalActivityDetail ad = new WithdrawalActivityDetail();
                ad.setActionCode(WithdrawalActivitySummary.ACTION_CODE_SAVED);
                ad.setItemNumber(field.getId());
                ad.setLastUpdatedById((int) newObj.getPrincipal().getProfileId());
                ad.setLastUpdated(newWithdrawal.getLastSavedTimestamp());
                ad.setValue(null); // no need to save the value, it is never
                // read. just the userid and date.
                ads.add(ad);
            }
        }
        for (Payee newPayee : newObj.getRecipients().iterator().next().getPayees()) {
            for (Payee oldPayee : oldObj.getRecipients().iterator().next()
                    .getPayees()) {
                if (newPayee.getPayeeNo().intValue() == oldPayee.getPayeeNo().intValue()) {
                    for (PayeeFieldDef field : PayeeFieldDef.values()) {
                        String newValue = field.getValue(newObj, (WithdrawalRequestPayee)newPayee);
                        String oldValue = field.getValue(oldObj, (WithdrawalRequestPayee)oldPayee);
                        if (!StringUtils.equals(oldValue, newValue)) {
                            if (!parentKeyAdded) {
                                WithdrawalActivityDetail ad = new WithdrawalActivityDetail();
                                ad.setActionCode(WithdrawalActivitySummary.ACTION_CODE_SAVED);
                                ad.setLastUpdated(newWithdrawal.getLastSavedTimestamp());
                                ad.setLastUpdatedById((int) newObj.getPrincipal().getProfileId());
                                ad.setItemNumber(WithdrawalFieldDef.DYN_PAYEE_TYPE);
                                ads.add(ad);
                                parentKeyAdded = true;
                            }
                            ActivityDynamicDetail add = new ActivityDynamicDetail();
                            add.setTypeCode(WithdrawalActivitySummary.ACTION_CODE_SAVED);
                            add.setItemNumber(WithdrawalFieldDef.DYN_PAYEE_TYPE);
                            add.setSecondaryNumber(field.getId());
                            add.setSecondaryName(newPayee.getPayeeNo().toString());
                            add.setLastUpdatedById((int) newObj.getPrincipal().getProfileId());
                            add.setLastUpdated(newWithdrawal.getLastSavedTimestamp());
                            add.setValue(null); // no need to save the value, it
                            // is never read. just the
                            // userid and date.
                            adds.add(add);
                        }
                    }
                }
            }
        }

        final Timestamp lastUpdated = newWithdrawal.getLastSavedTimestamp();

        saveMoneyTypeChangedFields(newObj, oldObj, lastUpdated, ads, adds);

        parentKeyAdded = false; // reset for declarations
        Collection<String> declarationCodes = new ArrayList<String>();
        declarationCodes.add(WithdrawalRequestDeclaration.TAX_NOTICE_TYPE_CODE);
        declarationCodes.add(WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE);
        declarationCodes.add(WithdrawalRequestDeclaration.WAITING_PERIOD_WAIVED_TYPE_CODE);
        declarationCodes.add(WithdrawalRequestDeclaration.AT_RISK_TRANSACTION_TYPE_CODE);
        for (String code : declarationCodes) {
            boolean foundInNew = false;
            boolean foundInOld = false;
            for (Declaration newDecl : newObj.getDeclarations()) {
                if (StringUtils.equals(code, newDecl.getTypeCode())) {
                    foundInNew = true;
                    break;
                }
            }
            for (Declaration oldDecl : oldObj.getDeclarations()) {
                if (StringUtils.equals(code, oldDecl.getTypeCode())) {
                    foundInOld = true;
                    break;
                }
            }
            boolean availableInNew = newObj.isDeclarationAvailable(code);
            boolean availableInOld = oldObj.isDeclarationAvailable(code);
            if ((foundInNew ^ foundInOld) || (availableInNew != availableInOld)) {
                if (!parentKeyAdded) {
                    WithdrawalActivityDetail ad = new WithdrawalActivityDetail();
                    ad.setActionCode(WithdrawalActivitySummary.ACTION_CODE_SAVED);
                    ad.setLastUpdated(newWithdrawal.getLastSavedTimestamp());
                    ad.setLastUpdatedById((int) newObj.getPrincipal().getProfileId());
                    ad.setItemNumber(WithdrawalFieldDef.DYN_DECLARATION_TYPE);
                    ads.add(ad);
                    parentKeyAdded = true;
                }
                ActivityDynamicDetail add = new ActivityDynamicDetail();
                add.setTypeCode(WithdrawalActivitySummary.ACTION_CODE_SAVED);
                add.setItemNumber(WithdrawalFieldDef.DYN_DECLARATION_TYPE);
                add.setSecondaryNumber(1);
                add.setSecondaryName(code);
                add.setLastUpdatedById((int) newObj.getPrincipal().getProfileId());
                add.setLastUpdated(newWithdrawal.getLastSavedTimestamp());
                add.setValue(null); // no need to save the value, it
                // is never read. just the
                // userid and date.
                adds.add(add);
            }
        }
        new ActivityDetailDao().insertUpdate(newObj.getSubmissionId(), newObj
				.getContractId(), (int) newObj.getPrincipal().getProfileId(),
				ads, WithdrawalActivityDetail.class);
        new ActivityDynamicDetailDao().insertUpdate(newObj.getSubmissionId(),
				newObj.getContractId(), (int) newObj.getPrincipal()
						.getProfileId(), adds);
    }

    /**
     * This method saves the changed fields for the money types. It takes into account that money
     * types can be added and removed since the last time the object was saved.
     * 
     * @param newWithdrawalRequest The new withdrawal request.
     * @param oldWithdrawalRequest The old withdrawal request.
     * @param lastUpdated The last updated {@link Timestamp} to use for the records.
     * @param activityDetails The list of {@link WithdrawalActivityDetail} objects.
     * @param activityDynamicDetails The list of {@link ActivityDynamicDetail} objects.
     */
    private static void saveMoneyTypeChangedFields(final WithdrawalRequest newWithdrawalRequest,
            final WithdrawalRequest oldWithdrawalRequest, final Timestamp lastUpdated,
            final List<WithdrawalActivityDetail> activityDetails,
            final List<ActivityDynamicDetail> activityDynamicDetails) {

        final Collection<WithdrawalRequestMoneyType> newMoneyTypes = newWithdrawalRequest
                .getMoneyTypes();
        final Collection<WithdrawalRequestMoneyType> oldMoneyTypes = oldWithdrawalRequest
                .getMoneyTypes();
        final int lastUpdatedBy = (int) newWithdrawalRequest.getPrincipal().getProfileId();

        boolean parentKeyAdded = false; // reset for money types

        final WithdrawalRequestMoneyTypeByIdPredicate[] newPredicates = new WithdrawalRequestMoneyTypeByIdPredicate[newMoneyTypes
                .size()];

        int i = 0;
        for (WithdrawalRequestMoneyType newMoneyType : newMoneyTypes) {

            // Create a predicate that can match other money types by the ID.
            final WithdrawalRequestMoneyTypeByIdPredicate newMoneyTypePredicate = new WithdrawalRequestMoneyTypeByIdPredicate(
                    newMoneyType);
            // Keep the predicate for later use.
            newPredicates[i++] = newMoneyTypePredicate;

            // Get the money type from the request that matches the default data.
            final WithdrawalRequestMoneyType oldMoneyType = (WithdrawalRequestMoneyType) CollectionUtils
                    .find(oldMoneyTypes, newMoneyTypePredicate);

            for (MoneyTypeFieldDef field : MoneyTypeFieldDef.values()) {
                final String newValue = field.getValue(newWithdrawalRequest, newMoneyType);
                final String oldValue;
                // Check if we already have this or if it's new.
                if (oldMoneyType == null) {
                    // There is no old money type, for the old money type collection, it's new.
                    oldValue = null;
                } else {
                    // We have a match, so we update the data.
                    oldValue = field.getValue(oldWithdrawalRequest, oldMoneyType);
                } // fi
                if (!StringUtils.equals(oldValue, newValue)) {
                    if (!parentKeyAdded) {
                        WithdrawalActivityDetail activityDetail = new WithdrawalActivityDetail();
                        activityDetail.setActionCode(WithdrawalActivitySummary.ACTION_CODE_SAVED);
                        activityDetail.setLastUpdated(lastUpdated);
                        activityDetail.setLastUpdatedById(lastUpdatedBy);
                        activityDetail.setItemNumber(WithdrawalFieldDef.DYN_MONEY_TYPE);
                        activityDetails.add(activityDetail);
                        parentKeyAdded = true;
                    } // fi

                    ActivityDynamicDetail activityDynamicDetail = new ActivityDynamicDetail();
                    activityDynamicDetail.setTypeCode(WithdrawalActivitySummary.ACTION_CODE_SAVED);
                    activityDynamicDetail.setItemNumber(WithdrawalFieldDef.DYN_MONEY_TYPE);
                    activityDynamicDetail.setSecondaryNumber(field.getId());
                    activityDynamicDetail.setSecondaryName(newMoneyType.getMoneyTypeId());
                    activityDynamicDetail.setLastUpdatedById(lastUpdatedBy);
                    activityDynamicDetail.setLastUpdated(lastUpdated);
                    // No need to save the value, it is never read, just the userid and date.
                    activityDynamicDetail.setValue(null);

                    activityDynamicDetails.add(activityDynamicDetail);
                } // fi
            } // end for
        } // end for

        // Selects all the money types that no longer have new values. This should never happen, as
        // the money type rows are kept, even if the balance has gone to zero.
        Collection<WithdrawalRequestMoneyType> noLongerExistingMoneyTypes = CollectionUtils
                .selectRejected(oldMoneyTypes, new AnyPredicate(newPredicates));

        if (CollectionUtils.isNotEmpty(noLongerExistingMoneyTypes)) {
            throw new RuntimeException("There were money types found that don't exist in the new "
                    + "list of money types.");
        } // fi
    }

    /**
     * @param withdrawal withdrawal used for values
     * @throws DistributionServiceException thrown in an exception is encountered
     */
    public static void saveOriginalValues(final Withdrawal withdrawal)
            throws DistributionServiceException {

        ActivityDetailDao detailDao = new ActivityDetailDao();
        ActivityDynamicDetailDao dynamicDao = new ActivityDynamicDetailDao();
        Collection<WithdrawalActivityDetail> ads = new ArrayList<WithdrawalActivityDetail>();
        Collection<ActivityDynamicDetail> adds = new ArrayList<ActivityDynamicDetail>();
        WithdrawalRequest wr = withdrawal.getWithdrawalRequest();
        Integer submissionId = wr.getSubmissionId();
        Integer contractId = wr.getContractId();
        Integer userProfileId = (int) wr.getPrincipal().getProfileId();

        Converter converter = new DateConverter(null);
        ConvertUtils.register(converter, java.util.Date.class);
        converter = new StringConverter(null);
        ConvertUtils.register(converter, java.lang.String.class);

        for (WithdrawalFieldDef field : WithdrawalFieldDef.values()) {
            WithdrawalActivityDetail ad = new WithdrawalActivityDetail();
            ad.setActionCode(WithdrawalActivitySummary.ACTION_ORIGINAL_VALUE);
            ad.setLastUpdated(withdrawal.getLastSavedTimestamp());
            ad.setLastUpdatedById(userProfileId);
            ad.setItemNumber(field.getId());
            ad.setValue(field.getValue(wr));
            ads.add(ad);
        }

        if (wr.getMoneyTypes().size() > 0) {
            WithdrawalActivityDetail ad = new WithdrawalActivityDetail();
            ad.setActionCode(WithdrawalActivitySummary.ACTION_ORIGINAL_VALUE);
            ad.setLastUpdated(withdrawal.getLastSavedTimestamp());
            ad.setLastUpdatedById(userProfileId);
            ad.setItemNumber(WithdrawalFieldDef.DYN_MONEY_TYPE);
            ads.add(ad);
            for (WithdrawalRequestMoneyType mt : wr.getMoneyTypes()) {
                for (MoneyTypeFieldDef field : MoneyTypeFieldDef.values()) {
                    ActivityDynamicDetail add = new ActivityDynamicDetail();
                    add.setItemNumber(WithdrawalFieldDef.DYN_MONEY_TYPE);
                    add.setSecondaryNumber(field.getId());
                    add.setSecondaryName(mt.getMoneyTypeId());
                    add.setLastUpdated(withdrawal.getLastSavedTimestamp());
                    add.setLastUpdatedById(userProfileId);
                    add.setTypeCode(WithdrawalActivitySummary.ACTION_ORIGINAL_VALUE);
                    add.setValue(field.getValue(wr, mt));
                    adds.add(add);
                }
            }
        }

        if (wr.getRecipients().iterator().next().getPayees().size() > 0) {
            WithdrawalActivityDetail ad = new WithdrawalActivityDetail();
            ad.setActionCode(WithdrawalActivitySummary.ACTION_ORIGINAL_VALUE);
            ad.setLastUpdated(withdrawal.getLastSavedTimestamp());
            ad.setLastUpdatedById(userProfileId);
            ad.setItemNumber(WithdrawalFieldDef.DYN_PAYEE_TYPE);
            ads.add(ad);
            Integer payeeNumber = 1;
            for (Recipient recipient : wr.getRecipients()) {
                for (Payee payee : recipient.getPayees()) {
                    for (PayeeFieldDef field : PayeeFieldDef.values()) {
                        ActivityDynamicDetail add = new ActivityDynamicDetail();
                        add.setItemNumber(WithdrawalFieldDef.DYN_PAYEE_TYPE);
                        add.setSecondaryNumber(field.getId());
                        add.setSecondaryName(payee.getPayeeNo() != null ? payee.getPayeeNo()
                                .toString() : payeeNumber.toString());
                        add.setLastUpdated(withdrawal.getLastSavedTimestamp());
                        add.setLastUpdatedById(userProfileId);
                        add.setTypeCode(WithdrawalActivitySummary.ACTION_ORIGINAL_VALUE);
                        add.setValue(field.getValue(wr, (WithdrawalRequestPayee)payee));
                        adds.add(add);
                    }
                    payeeNumber++;
                }
            }
        }
        WithdrawalActivityDetail ad = new WithdrawalActivityDetail();
        ad.setActionCode(WithdrawalActivitySummary.ACTION_ORIGINAL_VALUE);
        ad.setLastUpdated(withdrawal.getLastSavedTimestamp());
        ad.setLastUpdatedById(userProfileId);
        ad.setItemNumber(WithdrawalFieldDef.DYN_DECLARATION_TYPE);
        ads.add(ad);
        Collection<String> declarationCodes = new ArrayList<String>();
        declarationCodes.add(WithdrawalRequestDeclaration.TAX_NOTICE_TYPE_CODE);
        declarationCodes.add(WithdrawalRequestDeclaration.IRA_SERVICE_PROVIDER_TYPE_CODE);
        declarationCodes.add(WithdrawalRequestDeclaration.WAITING_PERIOD_WAIVED_TYPE_CODE);
        declarationCodes.add(WithdrawalRequestDeclaration.AT_RISK_TRANSACTION_TYPE_CODE);
 
        for (String declarationCode : declarationCodes) {
            boolean found = false;
            for (Declaration decl : wr.getDeclarations()) {
                if (decl.getTypeCode().equals(declarationCode)) {
                    found = true;
                    break;
                }
            }
            boolean isAvailable = wr.isDeclarationAvailable(declarationCode);
            ActivityDynamicDetail add = new ActivityDynamicDetail();
            add.setItemNumber(WithdrawalFieldDef.DYN_DECLARATION_TYPE);
            add.setSecondaryNumber(1);
            add.setSecondaryName(declarationCode);
            add.setLastUpdated(withdrawal.getLastSavedTimestamp());
            add.setLastUpdatedById(userProfileId);
            add.setTypeCode(WithdrawalActivitySummary.ACTION_ORIGINAL_VALUE);
            if(isRiskIndicatorDeclaration(wr,declarationCode)) {
            	add.setValue(StringUtils.EMPTY);
            }else {
                // Need to use blank if declaration is not available (i.e. suppressed)
                add.setValue(found ? "Yes" : (isAvailable ? "No" : StringUtils.EMPTY));
            }
            adds.add(add);
        }

        detailDao.insert(submissionId, contractId, userProfileId, ads);
        dynamicDao.insert(submissionId, contractId, userProfileId, adds);
        saveSystemOfRecordSnapShot(withdrawal);
    }

    /**
     * @param withdrawal The withdrawal used for values
     * @param actionCode The action being performed
     * @throws DistributionServiceException thrown if an error is encountered
     */
    public static void saveSummary(final Withdrawal withdrawal, final String actionCode)
            throws DistributionServiceException {

        ActivitySummaryDao summaryDao = new ActivitySummaryDao();
        WithdrawalActivitySummary summary = new WithdrawalActivitySummary();
        WithdrawalRequest wr = withdrawal.getWithdrawalRequest();
        Integer submissionId = wr.getSubmissionId();
        Integer contractId = wr.getContractId();
        Integer userProfileId = (int) wr.getPrincipal().getProfileId();

        summary.setActionCode(actionCode);
        summary.setActionTimestamp(withdrawal.getLastSavedTimestamp());
        summary.setCreatedById(userProfileId);

        summaryDao.insert(submissionId, contractId, userProfileId, summary);
    }

    /**
     * Updates activity history with changes from the last time the withdrawal was saved to the
     * database.
     * 
     * @param newWithdrawal The new withdrawal.
     * @param oldWithdrawal The withdrawal object before it was changed (read from the database).
     * @throws DistributionServiceException thrown if an error is encountered
     */
    public static void updateActivityHistory(final Withdrawal newWithdrawal,
            final Withdrawal oldWithdrawal) throws DistributionServiceException {

        ActivityHistoryHelper.saveChangedFields(newWithdrawal, oldWithdrawal);
        ActivityHistoryHelper.saveSystemOfRecordSnapShot(newWithdrawal);
    }

    /**
     * Retrieves a Withdrawal used for activity history comparison.
     * 
     * @param withdrawal The withdrawal that contains the principal and submission id for reading
     * @return The withdrawal
     * @throws DistributionServiceException thrown if an error is encountered
     */
    public static Withdrawal getOldWithdrawal(final Withdrawal withdrawal)
            throws DistributionServiceException {

        final Withdrawal oldWithdrawal = withdrawal.getSavedWithdrawal();

        oldWithdrawal.getWithdrawalRequest().setCmaSiteCode(WithdrawalRequest.CMA_SITE_CODE_PSW);

        return oldWithdrawal;
    }
    
    /**
     * This method is used to set the initial value of the Risk Declaration for the 
     * participant intiatiated withdrawal request to "No" when the request
     * is submitted by the participant.
     * 
     * @param WithdrawalRequest 
     * @param String - declarationCode
     * @return boolean
     */
    private static boolean isRiskIndicatorDeclaration(WithdrawalRequest wr, String declarationCode) {
    	return (StringUtils.equals(WithdrawalRequestDeclaration.AT_RISK_TRANSACTION_TYPE_CODE, declarationCode) &&
            		wr.getRequestRiskIndicator() && StringUtils.equals("1", wr.getCreatedById().toString()));
    }

}
