package com.manulife.pension.ps.web.contract;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.ps.web.withdrawal.GraphLocation;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.contract.valueobject.ScheduleAmount;
import com.manulife.pension.service.contract.valueobject.VestingSchedule;
import com.manulife.pension.validator.ValidationError;

public class ScheduleAmountUi extends BasePlanDataUiObject {

    private static final String VO_BEAN_NAME = "scheduleAmount";

    private static final String[] UI_FIELDS = { "amount" };

    private ScheduleAmount scheduleAmount;

    private transient VestingScheduleUi parent;

    private String amount;

    /**
     * Default Constructor.
     * 
     * @param planData The plan data object to load the data from.
     */
    public ScheduleAmountUi(final ScheduleAmount scheduleAmount, final VestingScheduleUi parent) {
        super(UI_FIELDS, VO_BEAN_NAME);
        setScheduleAmount(scheduleAmount);
        setParent(parent);

        convertFromBean();
    }

    /**
     * Default Constructor.
     */
    public ScheduleAmountUi() {
        super(UI_FIELDS, VO_BEAN_NAME);
        // Load the blank bean.
        this.scheduleAmount = new ScheduleAmount();
    }

    /**
     * Converts the matching fields from the ScheduleAmount bean, to this object.
     */
    public final void convertFromBean() {

        try {
            BeanUtils.copyProperties(this, scheduleAmount);
        } catch (IllegalAccessException illegalAccessException) {
            throw new NestableRuntimeException(illegalAccessException);
        } catch (InvocationTargetException invocationTargetException) {
            throw new NestableRuntimeException(invocationTargetException);
        }

        // Convert schedule amount
        if (scheduleAmount.getAmount() != null) {
            setAmount(PlanData.formatPercentageFormatter(scheduleAmount.getAmount()));
        }
    }

    /**
     * Converts the matching fields from this object, to the ScheduleAmount bean.
     */
    public final void convertToBean() {

        try {
            BeanUtils.copyProperties(scheduleAmount, this);
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        }
    }

    /**
     * @return the scheduleAmount
     */
    public ScheduleAmount getScheduleAmount() {
        return scheduleAmount;
    }

    /**
     * @param scheduleAmount the scheduleAmount to set
     */
    public void setScheduleAmount(final ScheduleAmount scheduleAmount) {
        this.scheduleAmount = scheduleAmount;
    }

    /**
     * @return the parent
     */
    public VestingScheduleUi getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(final VestingScheduleUi parent) {
        this.parent = parent;
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(final String amount) {
        this.amount = amount;
    }

    /**
     * Queries if the initial rendering of the schedule amount field should be editable or not.
     * 
     * @return boolean - True if the initial rendering of the field should be editable, false
     *         otherwise.
     */
    public boolean getIsFieldEditable() {

        if (StringUtils.equals(VestingSchedule.VESTING_SCHEDULE_CUSTOMIZED, getParent()
                .getVestingSchedule().getVestingScheduleType())
                && !getIsFieldLast()) {
            return true;
        }

        return false;
    }

    /**
     * Queries if this schedule amount is the last in the vesting schedule amount list.
     * 
     * @return boolean - True if the schedule amount is last, false otherwise.
     */
    public boolean getIsFieldLast() {
        final ScheduleAmountUi lastField = (ScheduleAmountUi) CollectionUtils.get(getParent()
                .getSchedules(), getParent().getSchedules().size() - 1);
        return (lastField == this);
    }

    /**
     * {@inheritDoc}
     */
    public Collection<ValidationError> getValidationMessages(final GraphLocation graphLocation) {
        final Collection<ValidationError> messages = new ArrayList<ValidationError>();

        messages.addAll(getValidationMessages(graphLocation, getScheduleAmount()));

        return messages;
    }
}
