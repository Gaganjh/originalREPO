package com.manulife.pension.ps.web.plan;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.ps.web.contract.BasePlanDataUiObject;
import com.manulife.pension.ps.web.contract.PlanDataUi;
import com.manulife.pension.ps.web.withdrawal.GraphLocation;
import com.manulife.pension.service.plan.valueobject.MoneyTypeExcludedEmployee;
import com.manulife.pension.validator.ValidationError;

public class MoneyTypeExcludedEmployeeUi extends BasePlanDataUiObject {

	private static final long serialVersionUID = 1L;

	private static final String VO_BEAN_NAME = "moneyTypeExcludedEmployee";

    private static final String[] UI_FIELDS = {
			"union", "nonResidentAliens",
			"highlyCompensated", "leased", "partTimeOrTemporary" };

    private MoneyTypeExcludedEmployee moneyTypeExcludedEmployee;
    
    private transient PlanDataUi parent;

    private Boolean union;
	private Boolean nonResidentAliens;
	private Boolean highlyCompensated;
	private Boolean leased;
	private Boolean other;
	
	
    /**
     * Default Constructor.
     * 
     * @param planData The plan data object to load the data from.
     */
    public MoneyTypeExcludedEmployeeUi(final MoneyTypeExcludedEmployee moneyTypeExcludedEmployee, final PlanDataUi parent) {
        super(UI_FIELDS, VO_BEAN_NAME);
        setMoneyTypeExcludedEmployee(moneyTypeExcludedEmployee);
        setParent(parent);

        convertFromBean();
    }

    /**
     * Default Constructor.
     */
    public MoneyTypeExcludedEmployeeUi() {
        super(UI_FIELDS, VO_BEAN_NAME);
        this.moneyTypeExcludedEmployee = new MoneyTypeExcludedEmployee();
    }


    /**
     * Converts the matching fields from the ScheduleAmount bean, to this object.
     */
    public final void convertFromBean() {

        try {
            BeanUtils.copyProperties(this, moneyTypeExcludedEmployee);
        } catch (IllegalAccessException illegalAccessException) {
            throw new NestableRuntimeException(illegalAccessException);
        } catch (InvocationTargetException invocationTargetException) {
            throw new NestableRuntimeException(invocationTargetException);
        }
    }

    /**
     * Converts the matching fields from this object, to the ScheduleAmount bean.
     */
    public final void convertToBean() {

        try {
            BeanUtils.copyProperties(moneyTypeExcludedEmployee, this);
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        }
    }

	public MoneyTypeExcludedEmployee getMoneyTypeExcludedEmployee() {
		return moneyTypeExcludedEmployee;
	}
	public void setMoneyTypeExcludedEmployee(
			MoneyTypeExcludedEmployee moneyTypeExcludedEmployee) {
		this.moneyTypeExcludedEmployee = moneyTypeExcludedEmployee;
	}
	public PlanDataUi getParent() {
		return parent;
	}
	public void setParent(PlanDataUi parent) {
		this.parent = parent;
	}
	public Boolean getUnion() {
		return union;
	}
	public void setUnion(Boolean union) {
		this.union = union;
	}
	public Boolean getNonResidentAliens() {
		return nonResidentAliens;
	}
	public void setNonResidentAliens(Boolean nonResidentAliens) {
		this.nonResidentAliens = nonResidentAliens;
	}
	public Boolean getHighlyCompensated() {
		return highlyCompensated;
	}
	public void setHighlyCompensated(Boolean highlyCompensated) {
		this.highlyCompensated = highlyCompensated;
	}
	public Boolean getLeased() {
		return leased;
	}
	public void setLeased(Boolean leased) {
		this.leased = leased;
	}
	public Boolean getOther() {
		return other;
	}
	public void setOther(Boolean partTimeOrTemporary) {
		this.other = partTimeOrTemporary;
	}

	/**
     * {@inheritDoc}
     */
    public Collection<ValidationError> getValidationMessages(final GraphLocation graphLocation) {
        final Collection<ValidationError> messages = new ArrayList<ValidationError>();
        messages.addAll(getValidationMessages(graphLocation, getMoneyTypeExcludedEmployee()));
        return messages;
    }

}
