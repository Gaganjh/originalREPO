package com.manulife.pension.ps.web.contract;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

import sun.misc.Perf.GetPerfAction;

import com.manulife.pension.ps.web.withdrawal.GraphLocation;
import com.manulife.pension.service.contract.valueobject.ScheduleAmount;
import com.manulife.pension.service.contract.valueobject.VestingSchedule;
import com.manulife.pension.validator.ValidationError;

public class VestingScheduleUi extends BasePlanDataUiObject {

    private static final String VO_BEAN_NAME = "vestingSchedule";

    private static final String[] UI_FIELDS = { };

    private VestingSchedule vestingSchedule;

    private transient PlanDataUi parent;

    private Collection<ScheduleAmountUi> schedules = new ArrayList<ScheduleAmountUi>(
            VestingSchedule.YEARS_OF_SERVICE);

    /**
     * Default Constructor.
     * 
     * @param planData The plan data object to load the data from.
     */
    public VestingScheduleUi(final VestingSchedule vestingSchedule, final PlanDataUi parent) {
        super(UI_FIELDS, VO_BEAN_NAME);
        setVestingSchedule(vestingSchedule);
        setParent(parent);

        convertFromBean();
    }

    /**
     * Default Constructor.
     */
    public VestingScheduleUi() {
        super(UI_FIELDS, VO_BEAN_NAME);
        // Load the blank bean.
        this.vestingSchedule = new VestingSchedule();
        setSchedules(new ArrayList<ScheduleAmountUi>());
    }

    /**
     * Converts the matching fields from the VestingSchedule bean, to this object.
     */
    public final void convertFromBean() {

        try {
            BeanUtils.copyProperties(this, vestingSchedule);
        } catch (IllegalAccessException illegalAccessException) {
            throw new NestableRuntimeException(illegalAccessException);
        } catch (InvocationTargetException invocationTargetException) {
            throw new NestableRuntimeException(invocationTargetException);
        }

        // Load the Schedule Amounts
        if (CollectionUtils.isEmpty(vestingSchedule.getSchedules())) {
            setSchedules(new ArrayList<ScheduleAmountUi>(0));
        } else {
            setSchedules(new ArrayList<ScheduleAmountUi>(vestingSchedule.getSchedules().size()));

            for (ScheduleAmount scheduleAmount : vestingSchedule.getSchedules()) {

                getSchedules().add(new ScheduleAmountUi(scheduleAmount, this));
            }
        }
    }

    /**
     * Converts the matching fields from this object, to the VestingSchedule bean.
     */
    public final void convertToBean() {

        try {
            BeanUtils.copyProperties(vestingSchedule, this);
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        }

        // Load the Schedules Amounts
        if (CollectionUtils.isEmpty(getSchedules())) {
            vestingSchedule.setSchedules(new ArrayList<ScheduleAmount>(0));
        } else {
            final Collection<ScheduleAmount> convertedSchedules = new ArrayList<ScheduleAmount>(
                    getSchedules().size());

            for (ScheduleAmountUi scheduleAmountUi : getSchedules()) {

                scheduleAmountUi.convertToBean();

                convertedSchedules.add(scheduleAmountUi.getScheduleAmount());
            }
            vestingSchedule.setSchedules(convertedSchedules);
        }
    }

    /**
     * @return the vestingSchedule
     */
    public VestingSchedule getVestingSchedule() {
        return vestingSchedule;
    }

    /**
     * @param vestingSchedule the vestingSchedule to set
     */
    public void setVestingSchedule(final VestingSchedule vestingSchedule) {
        this.vestingSchedule = vestingSchedule;
    }

    /**
     * @return the schedules
     */
    public Collection<ScheduleAmountUi> getSchedules() {
        return schedules;
    }

    /**
     * @param schedules the schedules to set
     */
    public void setSchedules(final Collection<ScheduleAmountUi> schedules) {
        this.schedules = schedules;
    }

    /**
     * @return the parent
     */
    public PlanDataUi getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(final PlanDataUi parent) {
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<ValidationError> getValidationMessages(final GraphLocation graphLocation) {

        final Collection<ValidationError> messages = new ArrayList<ValidationError>();

        messages.addAll(getValidationMessages(graphLocation, getVestingSchedule()));

        int i = 0;
        for (ScheduleAmountUi scheduleAmountUi : getSchedules()) {
            messages.addAll(scheduleAmountUi.getValidationMessages(new GraphLocation(graphLocation,
                    "schedules", i)));
            i++;
        }

        return messages;
    }
}
