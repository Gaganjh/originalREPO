package com.manulife.pension.ps.web.plan;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.ps.web.contract.BasePlanDataUiObject;
import com.manulife.pension.ps.web.contract.PlanDataUi;
import com.manulife.pension.ps.web.withdrawal.GraphLocation;
import com.manulife.pension.service.plan.valueobject.MoneyTypeEligibilityCriterion;
import com.manulife.pension.validator.ValidationError;

public class MoneyTypeEligibilityCriterionUi extends BasePlanDataUiObject {

	private static final long serialVersionUID = 1L;

	private static final String VO_BEAN_NAME = "moneyTypeEligibilityCriterion";

	public static final String[] UI_FIELDS = { "immediateEligibilityIndicator", "serviceCreditingMethod", "minimumAge",
			"hoursOfService", "periodOfService", "periodOfServiceUnit", "planEntryFrequencyIndicator",
			"partTimeEligibilityIndicator" };
    
    public static final Class<?>[] UI_FIELDS_DATATYPES = { Boolean.class, String.class,
    	String.class, String.class, String.class,
    	String.class, String.class, Boolean.class };

    private MoneyTypeEligibilityCriterion moneyTypeEligibilityCriterion;

    private transient PlanDataUi parent;

	private Boolean immediateEligibilityIndicator;
	private String serviceCreditingMethod;
	private String minimumAge;
	private String hoursOfService;
	private String periodOfService;
	private String periodOfServiceUnit;
	private String planEntryFrequencyIndicator;
	private Boolean partTimeEligibilityIndicator;

	/**
     * Default Constructor.
     * 
     * @param planData The plan data object to load the data from.
     */
    public MoneyTypeEligibilityCriterionUi(final MoneyTypeEligibilityCriterion moneyTypeEligibilityCriterion, final PlanDataUi parent) {
        super(UI_FIELDS, VO_BEAN_NAME);
        setMoneyTypeEligibilityCriterion(moneyTypeEligibilityCriterion);
        setParent(parent);

        convertFromBean();
    }

    /**
     * Default Constructor.
     */
    public MoneyTypeEligibilityCriterionUi() {
        super(UI_FIELDS, VO_BEAN_NAME);
        this.moneyTypeEligibilityCriterion = new MoneyTypeEligibilityCriterion();
    }

    /**
     * Converts the matching fields from the ScheduleAmount bean, to this object.
     */
    public final void convertFromBean() {

        try {
            BeanUtils.copyProperties(this, moneyTypeEligibilityCriterion);
        } catch (IllegalAccessException illegalAccessException) {
            throw new NestableRuntimeException(illegalAccessException);
        } catch (InvocationTargetException invocationTargetException) {
            throw new NestableRuntimeException(invocationTargetException);
        }
        // drop the point zero
        if (getMinimumAge()!= null) {
            int index = getMinimumAge().lastIndexOf(".");
            if (index > 0) {
                if (StringUtils.substring(getMinimumAge(), index+1).equals("0")) 
                    setMinimumAge(StringUtils.substring(getMinimumAge(), 0, index));
            }
        } 
    }

    /**
     * Converts the matching fields from this object, to the ScheduleAmount bean.
     */
    public final void convertToBean() {

        try {
            BeanUtils.copyProperties(moneyTypeEligibilityCriterion, this);
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        }
    }

    public MoneyTypeEligibilityCriterion getMoneyTypeEligibilityCriterion() {
		return moneyTypeEligibilityCriterion;
	}

	public void setMoneyTypeEligibilityCriterion(
			MoneyTypeEligibilityCriterion moneyTypeEligibilityCriterion) {
		this.moneyTypeEligibilityCriterion = moneyTypeEligibilityCriterion;
	}

	public PlanDataUi getParent() {
		return parent;
	}

	public void setParent(PlanDataUi parent) {
		this.parent = parent;
	}

	public Boolean getImmediateEligibilityIndicator() {
		return immediateEligibilityIndicator;
	}

	public void setImmediateEligibilityIndicator(
			Boolean immediateEligibilityIndicator) {
		this.immediateEligibilityIndicator = immediateEligibilityIndicator;
	}

	public String getServiceCreditingMethod() {
		return serviceCreditingMethod;
	}

	public void setServiceCreditingMethod(
			String serviceCreditingMethod) {
		this.serviceCreditingMethod = serviceCreditingMethod;
	}

	public String getHoursOfService() {
        return hoursOfService;
    }

    public void setHoursOfService(String hoursOfService) {
        this.hoursOfService = hoursOfService;
    }

    public String getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(String minimumAge) {
        this.minimumAge = minimumAge;
    }

    public String getPeriodOfService() {
        return periodOfService;
    }

    public void setPeriodOfService(String periodOfService) {
        this.periodOfService = periodOfService;
    }

    public String getPeriodOfServiceUnit() {
		return periodOfServiceUnit;
	}

	public void setPeriodOfServiceUnit(String periodOfServiceUnit) {
		this.periodOfServiceUnit = periodOfServiceUnit;
	}

	public String getPlanEntryFrequencyIndicator() {
		return planEntryFrequencyIndicator;
	}

	public void setPlanEntryFrequencyIndicator(String planEntryFrequencyIndicator) {
		this.planEntryFrequencyIndicator = planEntryFrequencyIndicator;
	}

	/**
     * {@inheritDoc}
     */
    public Collection<ValidationError> getValidationMessages(final GraphLocation graphLocation) {
        final Collection<ValidationError> messages = new ArrayList<ValidationError>();
        messages.addAll(getValidationMessages(graphLocation, getMoneyTypeEligibilityCriterion()));
        return messages;
    }
    
    public String getPeriodOfServiceAndUnitDisplay() {
        return moneyTypeEligibilityCriterion.getPeriodOfServiceAndUnitDisplay();
    }

    public String getServiceCreditingMethodDisplay() {
        return moneyTypeEligibilityCriterion.getServiceCreditingMethodDisplay();
    }
    
    public String getPlanEntryFrequencyIndicatorDisplay() {
        return moneyTypeEligibilityCriterion.getPlanEntryFrequencyIndicatorDisplay();
    }
    
    public Boolean getPartTimeEligibilityIndicator() {
		return partTimeEligibilityIndicator;
	}

	public void setPartTimeEligibilityIndicator(Boolean partTimeEligibilityIndicator) {
		this.partTimeEligibilityIndicator = partTimeEligibilityIndicator;
	}
}
