package com.manulife.pension.service.loan;

import java.math.BigDecimal;

import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.util.iloans.PropertyManager;
import com.manulife.pension.util.config.ConfigurationFactory;

public class LoanDefaults {

	public static final String IMAGE_KEY = PropertyManager.ILOANS_PROPERTY_PREFIX
			+ ".loanPackage.gp1422.form.headerusa.image";

	/**
	 * The maximum difference between requested amount and available balance.
	 * 
	 * @return
	 */
	public static final BigDecimal getRequestedAmountOverAvailableAmountThreshold() {
		return ConfigurationFactory.getConfiguration().getBigDecimal(
				"loan.requestedAmountOverAvailableAmountThreshold",
				BigDecimal.ONE);
	}

	public static final int getAboutToExpireReminderDays() {
		return ConfigurationFactory.getConfiguration().getInt(
				"loan.aboutToExpireReminderDays", 3);
	}
	
	public static final int getPendingApolloLIThreshold() {
		return ConfigurationFactory.getConfiguration().getInt(
				"loan.pendingApolloLIThreshold", 30);
	}

	public static final int getCompletedApolloLIThreshold() {
		return ConfigurationFactory.getConfiguration().getInt(
				"loan.completedApolloLIThreshold", 4);
	}

	public static final int getExpirationDateOffset() {
		return ConfigurationFactory.getConfiguration().getInt(
				"loan.expirationDateOffset", 30);
	}

	public static final int getExpirationDateMinimumFutureDatedDays() {
		return ConfigurationFactory.getConfiguration().getInt(
				"loan.expirationDateMinimumFutureDatedDays", 0);
	}

	public static final int getExpirationDateMaximumFutureDatedDays() {
		return ConfigurationFactory.getConfiguration().getInt(
				"loan.expirationDateMaximumFutureDatedDays", 60);
	}

	public static final int getPayrollDateMinimumFutureDatedDays() {
		return ConfigurationFactory.getConfiguration().getInt(
				"loan.payrollDateMinimumFutureDatedDays", 21);
	}

	public static final int getEstimatedLoanStartDateOffset() {
        return ConfigurationFactory.getConfiguration().getInt(
                "loan.estimatedLoanStartDateOffset", 2);
    }

    public static final int getEstimatedLoanStartDateMaximumFutureDatedDays() {
        return ConfigurationFactory.getConfiguration().getInt(
                "loan.estimatedLoanStartDateMaximumFutureDatedDays", 60);
    }

    public static final int getLoanPackageEstimatedLoanStartDateOffset() {
        return ConfigurationFactory.getConfiguration().getInt(
                "loan.loanPackageEstimatedLoanStartDateOffset", 21);
    }

    public static final int getLoanPackageInvalidAfterThisManyDays() {
        return ConfigurationFactory.getConfiguration().getInt(
                "loan.loanPackageInavlidAfterThisManyDays", 30);
    }

	/**
	 * Returns the scale we use for loan calculation.
	 */
	public static int getLoanCalculationScale() {
		return ConfigurationFactory.getConfiguration().getInt(
				"loan.loanCalculationScale", 20);
	}

	/**
	 * Returns the loan interest rate adjustment. This percentage will be added
	 * to the prime rate to become the final loan interest rate.
	 */
	public static BigDecimal getLoanInterestRateAdjustment() {
		return ConfigurationFactory.getConfiguration().getBigDecimal(
				"loan.loanInterestRateAdjustment", new BigDecimal(0.03));
	}

	/**
	 * Returns the maximum amortization years for the given loan type.
	 * 
	 * @param type
	 *            Loan type in {@LoanConstants}
	 */
	public static int getMaximumAmortizationYears(String type) {
		if (LoanConstants.TYPE_PRIMARY_RESIDENCE.equals(type)) {
			return PlanData.MAXIMUM_PRIMARY_RESIDENCE_AMORTIZATION_PERIOD;
		} else if (LoanConstants.TYPE_GENERAL_PURPOSE.equals(type)) {
			return PlanData.MAXIMUM_GENERAL_PURPOSE_AMORTIZATION_PERIOD;
		} else {
			return PlanData.MAXIMUM_HARDSHIP_AMORTIZATION_PERIOD;
		}
	}

	/**
	 * Returns the default number of loans allowed.
	 */
	public static int getNumberOfLoansAllowed() {
		return ConfigurationFactory.getConfiguration().getInt(
				"loan.numberOfLoansAllowed", 3);
	}

	public static BigDecimal getMinimumLoanAmount() {
		return PlanData.MINIMUM_LOAN_AMOUNT_LOWER_BOUND;
	}

	public static BigDecimal getMaximumLoanAmount() {
		return PlanData.MAXIMUM_LOAN_AMOUNT_UPPER_BOUND;
	}

	public static BigDecimal getMaximumLoanPercentage() {
		return PlanData.MAXIMUM_LOAN_PERCENTAGE;
	}

	public static long getCalendarDaysForLoanProgressBox() {
		return ConfigurationFactory.getConfiguration().getInt(
				"loan.calendarDaysForLoanProgressBox", 30);
	}

	public static String getImagePath() {
		return PropertyManager.getString(IMAGE_KEY);
	}

	/**
	 * GIFL Deselected Date
	 * 
	 * @return
	 */
	public static String getGiflDeselectedDate() {
		return ConfigurationFactory.getConfiguration().getString(
				"loan.giflDeselectedDate", "9999-12-31");
	}

}
