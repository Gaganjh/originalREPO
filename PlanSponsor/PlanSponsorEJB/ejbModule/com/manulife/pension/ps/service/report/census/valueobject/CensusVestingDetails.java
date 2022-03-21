package com.manulife.pension.ps.service.report.census.valueobject;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.vesting.MoneyTypeVestingPercentage;
import com.manulife.pension.service.vesting.VestingRetrievalDetails;

public class CensusVestingDetails extends CensusSummaryDetails  {

	private static final long serialVersionUID = 1L;

    private static final String[] PARTICIPANT_PARTIAL_STATUS_CODE_ARRAY = { "BP", "TP", "RP", "DP" };

    private Date vestingEffDate;
    private Map<String, MoneyTypeVestingPercentage> percentages;
    private VestingRetrievalDetails calculationFact;
    private String participantStatusCode;
    private Date asOfDate;
    private String applyLTPTCrediting;

    /**
     * Default constructor
     *
     */
    public CensusVestingDetails() {
        super();
    }

    public CensusVestingDetails(String ssn, String employeeNumber,
			String firstName, String lastName, String middleInitial,
			String namePrefix, String addressLine1, String addressLine2,
			String city, String stateCode, String zipCode, String country,
			String stateOfResidence, String email, String division,
			Date birthDate, Date hireDate, String status, Date statusDate, 
            String eligibleInd, Date eligibilityDate, 
            String optOutInd, String planYTDHoursWorked, BigDecimal planYTDCompensation,
			Date planYTDHoursWorkedEffDate, String previousYearsOfService,
            Date previousYearsOfServiceEffDate, BigDecimal annualBaseSalary,
			BigDecimal beforeTaxDeferralPercentage, BigDecimal desigRothDeferralPercentage,
			BigDecimal beforeTaxDeferralAmount, BigDecimal desigRothDeferralAmount, String profileId,
			int contractId, boolean participantInd, String participantStatusCode, String fullyVestedInd, 
            Date fullyVestedIndEffDate, Date asOfDate, String maskSensitiveInfo,String providedEligibilityDateInd) {
		super(ssn, employeeNumber, firstName, lastName, middleInitial,
				namePrefix, addressLine1, addressLine2, city, stateCode,
				zipCode, country, stateOfResidence, email, division, birthDate,
				hireDate, status, statusDate, eligibleInd, eligibilityDate,
				optOutInd, planYTDHoursWorked, planYTDCompensation,
				planYTDHoursWorkedEffDate, previousYearsOfService, previousYearsOfServiceEffDate,
				annualBaseSalary, beforeTaxDeferralPercentage, desigRothDeferralPercentage,
                beforeTaxDeferralAmount, desigRothDeferralAmount, profileId, contractId,
                participantInd, fullyVestedInd, fullyVestedIndEffDate, maskSensitiveInfo,providedEligibilityDateInd);
		this.asOfDate = asOfDate;
		this.participantStatusCode = participantStatusCode;
	}



    public Date getVestingEffDate() {
        return vestingEffDate;
    }

    public void setVestingEffDate(Date vestingEffDate) {
        this.vestingEffDate = vestingEffDate;
    }

    public Map<String, MoneyTypeVestingPercentage> getPercentages() {
        return percentages;
    }

    public void setPercentages(Map<String, MoneyTypeVestingPercentage> percentages) {
        this.percentages = percentages;
    }

    public VestingRetrievalDetails getCalculationFact() {
        return calculationFact;
    }

    public void setCalculationFact(VestingRetrievalDetails calculationFact) {
        this.calculationFact = calculationFact;
    }

    /**
     * @return the participantStatusCode
     */
    public String getParticipantStatusCode() {
        return participantStatusCode;
    }

    /**
     * @param participantStatusCode the participantStatusCode to set
     */
    public void setParticipantStatusCode(final String participantStatusCode) {
        this.participantStatusCode = participantStatusCode;
    }

    /**
     * Checks if the participant status is partial or not.
     * 
     * @return boolean - True if the participant status is partial, false otherwise.
     */
    public boolean isParticipantStatusPartial() {
        return isParticipantStatusPartial(this.participantStatusCode);
    }

    /**
     * Checks to see if the participant status code is a partial participant status.
     * 
     * @param participantStatusCode - The participant status code to check.
     * @return boolean - True if the participant status code is a partial code, false otherwise.
     */
    public static boolean isParticipantStatusPartial(final String participantStatusCode) {
        return getParticipantPartialStatuses().contains(participantStatusCode);
    }

    /**
     * Gets all valid status codes that are partial participant statuses.
     * 
     * @return List<String> - A list of participant status codes that are partial.
     */
    private static List<String> getParticipantPartialStatuses() {
        return Arrays.asList(PARTICIPANT_PARTIAL_STATUS_CODE_ARRAY);
    }

    public Date getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(Date asOfDate) {
        this.asOfDate = asOfDate;
    }
    
    private MoneyTypeVestingPercentage latestVestingPercentage;
    
    /**
     * TODO getLatestVestingPercentage Description.
     * 
     * @return The latest {@link MoneyTypeVestingPercentage} object by effective date.
     */
    private MoneyTypeVestingPercentage getLatestVestingPercentage() {
        if (latestVestingPercentage == null) {
            MoneyTypeVestingPercentage latest = null;
            for (MoneyTypeVestingPercentage moneyTypeVestingPercentage : percentages.values()) {
                if ((!moneyTypeVestingPercentage.isMoneyTypeFullyVested())
                        && (moneyTypeVestingPercentage.getLastUpdated() != null)) {
                    if ((latest == null)
                            || (moneyTypeVestingPercentage.getLastUpdated().after(latest
                                    .getLastUpdated()))) {
                        latest = moneyTypeVestingPercentage;
                    } // fi
                } // fi
            } // end for
            latestVestingPercentage = latest;
        } // fi
        return latestVestingPercentage;
    }
    
    private String tipChangedByDisplay;
    
    /**
     * @return String - The tipChangedByDisplay.
     */
    public String getTipChangedByDisplay() {
        return tipChangedByDisplay;
    }

    /**
     * @param tipChangedByDisplay - The tipChangedByDisplay to set.
     */
    public void setTipChangedByDisplay(final String tipChangedByDisplay) {
        this.tipChangedByDisplay = tipChangedByDisplay;
    }
        
    /**
	 * @return the applyLTPTCrediting
	 */
	public String getApplyLTPTCrediting() {
		return applyLTPTCrediting;
	}

	/**
	 * @param applyLTPTCrediting the applyLTPTCrediting to set
	 */
	public void setApplyLTPTCrediting(String applyLTPTCrediting) {
		this.applyLTPTCrediting = applyLTPTCrediting;
	}

	public Date getMaxEffectiveDate() {
        MoneyTypeVestingPercentage latest = null;
        for (MoneyTypeVestingPercentage moneyTypeVestingPercentage : percentages.values()) {
            if ((!moneyTypeVestingPercentage.isMoneyTypeFullyVested()) && 
                    (moneyTypeVestingPercentage.getEffectiveDate() != null)) {
                if ((latest == null)
                        || (moneyTypeVestingPercentage.getEffectiveDate().after(
                                latest.getEffectiveDate()))) {
                    latest = moneyTypeVestingPercentage;
                } // fi
            } // fi
        } // end for
        if (latest == null) {
            return null;
        } else {
            return latest.getEffectiveDate();
        }
    }
    public Date getTipChangedOn() {
        final MoneyTypeVestingPercentage latest = getLatestVestingPercentage();
        if (latest == null) {
            return null;
        } else {
            return latest.getLastUpdated();
        } // fi
    }
    public String getTipChangedBy() {
        final MoneyTypeVestingPercentage latest = getLatestVestingPercentage();
        if (latest == null) {
            return null;
        } else {
        return latest.getLastUserId();
        } // fi
    }
    public String getTipSourceOfTheChange() {
        final MoneyTypeVestingPercentage latest = getLatestVestingPercentage();
        if (latest == null) {
            return null;
        } else {
        return latest.getSourceChannelCode();
        } // fi
    }

    
    
    public void fetchTipChangedByDisplay(final boolean loggedInUserIsInternal) {
        final MoneyTypeVestingPercentage latest = getLatestVestingPercentage();
        if (latest == null) {
            setTipChangedByDisplay(null);
            return;
        } // fi
        
        final String lastUserId = latest.getLastUserId();

        final String JHR = "John Hancock representative";

        // Lookup the user who last changed the info.
        UserInfo lastChangedByUserInfo = null;
        if (StringUtils.isNotBlank(lastUserId)) {
            Long longLastUserId;
            if (NumberUtils.isDigits(StringUtils.trim(lastUserId))) {
                try {
                  longLastUserId = Long.valueOf(lastUserId.trim());
                } catch (NumberFormatException numberFormatException) {
                    // ignore the NumberFormatException
                    longLastUserId = null;
                } // end try/catch
            } else {
                longLastUserId = null;
            } // fi
            
            if (longLastUserId != null) {
                // Only attempt a lookup if the string parses to a long (i.e. profile ID).
                try {
                    lastChangedByUserInfo = SecurityServiceDelegate.getInstance().searchByProfileId(null,
                    longLastUserId);
                } catch (SecurityServiceException securityServiceException) {
                    throw new RuntimeException(securityServiceException);
                } catch (SystemException e) {
                    throw new RuntimeException(e);
                } // end try/catch
            } // fi
            
            final StringBuffer stringBuffer = new StringBuffer();
            if (lastChangedByUserInfo != null && lastChangedByUserInfo.getRole().isExternalUser()) {
                // changedBy is external user
                stringBuffer.append(lastChangedByUserInfo.getLastName()).append(", ").append(lastChangedByUserInfo.getFirstName());
            } else {
                // changed by is internal user, or not a profile ID, so treating as internal.
                if (loggedInUserIsInternal && StringUtils.isNotBlank(lastUserId)) {
                    stringBuffer.append(lastUserId).append(", ");
                } // fi
                stringBuffer.append(JHR);
            }
            setTipChangedByDisplay(stringBuffer.toString());
            return;

        }    

        setTipChangedByDisplay(null);
        return;
    }

}
