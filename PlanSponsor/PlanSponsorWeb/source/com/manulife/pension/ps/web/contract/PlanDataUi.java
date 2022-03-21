package com.manulife.pension.ps.web.contract;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.plan.MoneyTypeEligibilityCriterionUi;
import com.manulife.pension.ps.web.plan.MoneyTypeExcludedEmployeeUi;
import com.manulife.pension.ps.web.withdrawal.GraphLocation;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Address;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.contract.valueobject.VestingSchedule;
import com.manulife.pension.service.plan.valueobject.MoneyTypeEligibilityCriterion;
import com.manulife.pension.service.plan.valueobject.MoneyTypeExcludedEmployee;
import com.manulife.pension.service.plan.valueobject.PlanAutoContributionIncrease;
import com.manulife.pension.service.plan.valueobject.PlanContributionRule;
import com.manulife.pension.service.plan.valueobject.PlanDeferralLimits;
import com.manulife.pension.service.plan.valueobject.PlanEmployeeDeferralElection;
import com.manulife.pension.service.plan.valueobject.PlanMoneyTypeEmployerContribution;
import com.manulife.pension.validator.ValidationError;

public class PlanDataUi extends BasePlanDataUiObject {

	private static final long serialVersionUID = 1L;

	public static final String UNLIMITED_CODE = "U";

    public static final String YES = "Yes";

    public static final String NO = "No";

    public static final String UNSPECIFIED = "Unspecified";

    public static final String PERCENTAGE = "Percentage";

    public static final String AMOUNT = "Dollar";

    public static final String EITHER = "Either";

    public static final String HOURS_OF_SERVICE = "Hours of service";

    public static final String ELAPSED_TIME = "Elapsed time";

    public static final String FACTS_AND_CIRCUMSTANCES = "Facts &amp; Circumstances";

    public static final String SAFE_HARBOR = "Safe Harbor";

    public static final String OTHER = "Other";

    public static final String WEEKLY = "Weekly";

    public static final String BI_WEEKLY = "Bi-weekly";

    public static final String MONTHLY = "Monthly";

    public static final String SECTION_GENERAL = "general";

    public static final String SECTION_ELIGIBILITY_AND_PARTICIPATION = "eligibility_and_participation";

    public static final String SECTION_OTHER_PLAN_INFORMATION = "other_plan_information";

    public static final String SECTION_CONTRIBUTIONS = "contributions";

    public static final String SECTION_WITHDRAWALS = "withdrawals";

    public static final String SECTION_LOANS = "loans";

    public static final String SECTION_VESTING = "vesting_and_forfeitures";

    public static final String SECTION_FORFEITURES = "forfeitures";

    private static final int TAX_IDENTIFICATION_NUMBER_PART_1_LENGTH = 2;

    private static final String VO_BEAN_NAME = "planData";

    private static final String[] UI_FIELDS = { "planEffectiveDate", "firstPlanEntryDateString",
            "deferralPercentageForAutomaticEnrollment", "automaticEnrollmentEffectiveDate",
            "selectedUnvestedMoneyOptions", "selectedWithdrawalReasons", "retirementAge",
            "earlyRetirementAge", "minimumHardshipAmount", "maximumHardshipAmount",
            "selectedAllowableMoneyTypesForWithdrawals", "selectedAllowableMoneyTypesForLoans",
            "minimumLoanAmount", "maximumLoanAmount", "maximumLoanPercentage",
            "loanInterestRateAbovePrime", "maximumNumberofOutstandingLoansSelect",
            "maximumAmortizationPeriodGeneralPurpose", "maximumAmortizationPeriodHardship",
            "maximumAmortizationPeriodPrimaryResidence", "partialWithdrawalMinimumWithdrawalAmount",
            "postedRuleSets", "aciEffectiveDate", "aciAppliesToEffectiveDate", "aciApplyDate" };

    private PlanData planData;

    private String employerTaxIdentificationNumberPart1;

    private String employerTaxIdentificationNumberPart2;

    private String planEffectiveDate;

    private String firstPlanEntryDateString;

    private String[] selectedUnvestedMoneyOptions = ArrayUtils.EMPTY_STRING_ARRAY;

    private String[] selectedWithdrawalReasons = ArrayUtils.EMPTY_STRING_ARRAY;
    
    private String[] selectedDeferralElectionMonths = ArrayUtils.EMPTY_STRING_ARRAY;

	private String retirementAge;

	private String retirementAgeDisplay;

	private String retirementServiceMinimumYears;

    private String earlyRetirementAge;

    private String earlyRetirementAgeDisplay;

    private String earlyRetirementServiceMinimumYears;

    private String minimumHardshipAmount;

    private String maximumHardshipAmount;

    private String[] selectedAllowableMoneyTypesForWithdrawals = ArrayUtils.EMPTY_STRING_ARRAY;

    private String[] selectedAllowableMoneyTypesForLoans = ArrayUtils.EMPTY_STRING_ARRAY;

    private String minimumLoanAmount;

    private String maximumLoanAmount;

    private String maximumLoanPercentage;

    private String loanInterestRateAbovePrime;

    private String automaticEnrollmentEffectiveDate;

    private String deferralPercentageForAutomaticEnrollment;
    
    private String deferralMaxPercent;
    
    private boolean hasBothEEDEFAndEEROTMoneyTypes;
    
    private String deferralMinPercent;
    
    private String maximumAmortizationPeriodGeneralPurpose;

    private String maximumAmortizationPeriodHardship;

    private String maximumAmortizationPeriodPrimaryResidence;

    private Collection<VestingScheduleUi> vestingSchedules;

    private Collection<AddressUi> collapsedAddresses;

    private Collection<MoneyTypeEligibilityCriterionUi> moneyTypeEligibilityCriteria;
    
    private Collection<MoneyTypeExcludedEmployeeUi> moneyTypeExcludedEmployees;
    
    private String aciEffectiveDate;
    
    private String aciApplyDate;
    
    private String aciAppliesToEffectiveDate;
    
    private String partialWithdrawalMinimumWithdrawalAmount;
    
    private String[] employerRulesets = new String[100];

    private String[] viewableRules;
    
	//splitters
	private static final String s1 = "~1~";
	private static final String s2 = "~2~";
	private static final String s3 = "~3~";
    
	/**
     * Default Constructor.
     * 
     * @param planData The plan data object to load the data from.
     */
    public PlanDataUi(final PlanData planData) {
        super(UI_FIELDS, VO_BEAN_NAME);
        setPlanData(planData);

        convertFromBean();
    }

    /**
     * Default Constructor.
     */
    public PlanDataUi() {
        super(UI_FIELDS, VO_BEAN_NAME);
        // Load the blank bean.
        this.planData = new PlanData();
        setSelectedUnvestedMoneyOptions(new String[0]);
        setSelectedWithdrawalReasons(new String[0]);
        setSelectedAllowableMoneyTypesForWithdrawals(new String[0]);
        setSelectedAllowableMoneyTypesForLoans(new String[0]);
        setVestingSchedules(new ArrayList<VestingScheduleUi>());
        setCollapsedAddresses(new ArrayList<AddressUi>());
    }

    /**
     * Converts the matching fields from the PlanData bean, to this object.
     */
    public final void convertFromBean() {

        try {
            BeanUtils.copyProperties(this, planData);
        } catch (IllegalAccessException illegalAccessException) {
            throw new NestableRuntimeException(illegalAccessException);
        } catch (InvocationTargetException invocationTargetException) {
            throw new NestableRuntimeException(invocationTargetException);
        }
        if (planData.getRetirementAge() != null) {
			setRetirementAgeDisplay(planData.getRetirementAge().doubleValue() % 1 == 0 ? Integer.toString(planData
					.getRetirementAge().intValue()) : getRetirementAge());
		}
		if (planData.getEarlyRetirementAge() != null) {
			setEarlyRetirementAgeDisplay(planData.getEarlyRetirementAge().doubleValue() % 1 == 0 ? Integer
					.toString(planData.getEarlyRetirementAge().intValue()) : getEarlyRetirementAge());
		}
        
        
        // Convert employer tax identification number
        final String taxId = planData.getEmployerTaxIdentificationNumber();
        if (StringUtils.isNotBlank(taxId)) {
            if (taxId.length() <= TAX_IDENTIFICATION_NUMBER_PART_1_LENGTH) {
                setEmployerTaxIdentificationNumberPart1(taxId);
            } else {
                setEmployerTaxIdentificationNumberPart1(StringUtils.substring(taxId, 0,
                        TAX_IDENTIFICATION_NUMBER_PART_1_LENGTH));
                setEmployerTaxIdentificationNumberPart2(StringUtils.substring(taxId,
                        TAX_IDENTIFICATION_NUMBER_PART_1_LENGTH));
            }
        }

        // Convert maximum loan percentage
        if (planData.getMaximumLoanPercentage() != null) {
            setMaximumLoanPercentage(PlanData.formatBigDecimalPercentageFormatter(planData
                    .getMaximumLoanPercentage()));
        }

        // Convert loan interest rate above prime
        if (planData.getLoanInterestRateAbovePrime() != null) {
            setLoanInterestRateAbovePrime(PlanData.formatBigDecimalPercentageFormatter(planData
                    .getLoanInterestRateAbovePrime()));
        }
        
        if (planData.getDeferralPercentageForAutomaticEnrollment() != null) {
			setDeferralPercentageForAutomaticEnrollment(PlanData.formatBigDecimalPercentageFormatter
					(planData
							.getDeferralPercentageForAutomaticEnrollment()));
		}

        if (planData.getWithdrawalDistributionMethod().getMinWithdrawalAmount() != null) {
			setPartialWithdrawalMinimumWithdrawalAmount(PlanData.formatIntegerFormatter
					(planData.getWithdrawalDistributionMethod()
							.getMinWithdrawalAmount()));
		}
        
        // Convert year end and default to Plan Year End if first plan entry date doesn't exist
        if (planData.getFirstPlanEntryDate() == null) {
            setFirstPlanEntryDateString(getPlanYearEndPlusOneString(planData));
        } else {
        	setFirstPlanEntryDateString(planData.getFirstPlanEntryDate().getData());
        }

        // Convert withdrawal reasons
        setSelectedWithdrawalReasons(planData.getWithdrawalReasons().toArray(
                new String[CollectionUtils.size(planData.getWithdrawalReasons())]));

        // Convert unvested money options
        setSelectedUnvestedMoneyOptions(planData.getUnvestedMoneyOptions().toArray(
                new String[CollectionUtils.size(planData.getUnvestedMoneyOptions())]));

        // Convert allowable money types for withdrawals
        setSelectedAllowableMoneyTypesForWithdrawals(planData
                .getAllowableMoneyTypesForWithdrawals().toArray(
                        new String[CollectionUtils.size(planData
                                .getAllowableMoneyTypesForWithdrawals())]));

        // Convert allowable money types for loans
        setSelectedAllowableMoneyTypesForLoans(planData.getAllowableMoneyTypesForLoans().toArray(
                new String[CollectionUtils.size(planData.getAllowableMoneyTypesForLoans())]));

        // Load the Vesting Schedules
        if (CollectionUtils.isEmpty(planData.getVestingSchedules())) {
            setVestingSchedules(new ArrayList<VestingScheduleUi>(0));
        } else {
            setVestingSchedules(new ArrayList<VestingScheduleUi>(planData.getVestingSchedules()
                    .size()));

            for (VestingSchedule vestingSchedule : planData.getVestingSchedules()) {

                getVestingSchedules().add(new VestingScheduleUi(vestingSchedule, this));
            }
        }

        // Load money type eligibility criteria
		if (CollectionUtils.isEmpty(planData.getMoneyTypeEligibilityCriteria())) {
			setMoneyTypeEligibilityCriteria(new ArrayList<MoneyTypeEligibilityCriterionUi>(
					0));
		} else {
			setMoneyTypeEligibilityCriteria(new ArrayList<MoneyTypeEligibilityCriterionUi>(
					planData.getMoneyTypeEligibilityCriteria().size()));
			for (MoneyTypeEligibilityCriterion moneyTypeEligibilityCriterion : planData
					.getMoneyTypeEligibilityCriteria()) {
				getMoneyTypeEligibilityCriteria().add(
						new MoneyTypeEligibilityCriterionUi(
								moneyTypeEligibilityCriterion, this));
			}
		}

		boolean foundEEDEF = false;
		boolean foundEEROT = false;
		for (MoneyTypeEligibilityCriterion moneyType : planData.getMoneyTypeEligibilityCriteria()) {
			if(ServiceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL.equals(moneyType.getMoneyTypeId())) {
				foundEEDEF = true;
			}else if(ServiceFeatureConstants.EMPLOYEE_ROTH_CONTRIBUTION.equals(moneyType.getMoneyTypeId())) {
				foundEEROT = true;
			}
			if(foundEEDEF && foundEEROT) {
				setHasBothEEDEFAndEEROTMoneyTypes(true);
				break;
			}
		}
		
		
        // Load money type excluded employee
		if (CollectionUtils.isEmpty(planData.getMoneyTypeExcludedEmployees())) {
			setMoneyTypeExcludedEmployees(new ArrayList<MoneyTypeExcludedEmployeeUi>(
					0));
		} else {
			setMoneyTypeExcludedEmployees(new ArrayList<MoneyTypeExcludedEmployeeUi>(
					planData.getMoneyTypeExcludedEmployees().size()));
			for (MoneyTypeExcludedEmployee moneyTypeExcludedEmployee : planData
					.getMoneyTypeExcludedEmployees()) {
				getMoneyTypeExcludedEmployees().add(
						new MoneyTypeExcludedEmployeeUi(
								moneyTypeExcludedEmployee, this));
			}
		}

        // Load the Addresses
        setCollapsedAddresses(getCollapsedAddresses(planData.getAddresses()));

        if (planData.getPlanDeferral() != null) {
			PlanDeferralLimits planDeferralLiimits = planData.getPlanDeferral();
			if (planDeferralLiimits != null && planDeferralLiimits.getDeferralMaxPercent() != null) {
				setDeferralMaxPercent(PlanData.formatBigDecimalPercentageFormatter
						(planDeferralLiimits.getDeferralMaxPercent()));
			}
			if (planDeferralLiimits != null && planDeferralLiimits.getDeferralMinPercent() != null) {
				setDeferralMinPercent(PlanData.
						formatBigDecimalPercentageFormatter(planDeferralLiimits.getDeferralMinPercent()));
			}
		}
        
        //load the plan employee deferral election months
		setSelectedDeferralElectionMonths(planData.getPlanEmployeeDeferralElection()
				.getEmployeeDeferralElectionSelectedMonths().toArray(new String[0]));

		if (planData.getPlanAutoContributionIncrease().getEffectiveDate() != null) {
			setAciEffectiveDate(new SimpleDateFormat("MM/dd/yyyy").format(planData.getPlanAutoContributionIncrease()
					.getEffectiveDate()));
		}
		if (planData.getPlanAutoContributionIncrease().getNewParticipantApplyDate()!= null) {
			setAciAppliesToEffectiveDate(new SimpleDateFormat("MM/dd/yyyy").format(planData.getPlanAutoContributionIncrease()
					.getNewParticipantApplyDate()));
		}
		if (planData.getPlanAutoContributionIncrease().getAnnualApplyDate() != null) {
		    setAciApplyDate(planData.getPlanAutoContributionIncrease()
					.getAnnualApplyDate().toString());
		}
		
		
		//convert the rules sets to encoded strings
		List<Integer> rules = new ArrayList<Integer>();
		Map<Integer, Map<String, String>> moneyTypesToRule = new HashMap<Integer, Map<String,String>>();
		String[] ruleSets = new String[100];
		
		//collect all the rules, money types and rules to money type arrays.
		for ( int i = 0 ;  i <  planData.getPlanMoneyTypeEmployerContributions().size() ; i++  ) {
			PlanMoneyTypeEmployerContribution item = planData.getPlanMoneyTypeEmployerContributions().get(i);
			rules.add(item.getRuleNumber());
			if ( moneyTypesToRule.get(item.getRuleNumber()) == null ) {
				moneyTypesToRule.put(item.getRuleNumber(), new HashMap<String,String>());
			}
			moneyTypesToRule.get(item.getRuleNumber()).put(StringUtils.trim(item.getMoneyTypeId()), Boolean.toString(item.getSelectedInd()));
		}
		//each primary field is separated by ~1~ 
		//each secondary field is separated by ~2~
		/*
		this table summarizes how the data maps;
		0, Rule Number, 
		1, list of money types
		2, getEmployerContributionMaxMatchAmount
		3, getEmployerContributionMaxMatchPercent
		4, getEmployerMatchContributionFirstPercent
		5, getEmployerMatchContributionNextPercent
		6, getEmployerMatchFirstEmployeeDeferralAmount
		7, getEmployerMatchFirstEmployeeDeferralPercent
		8, getEmployerMatchNextEmployeeDeferralAmount
		9, getEmployerMatchNextEmployeeDeferralPercent
		10, getNonElectiveContributionPercent
		11, ruleCode
		*/       		
		for (PlanContributionRule rule : planData.getPlanContributionRules() ) {
			StringBuffer encodedString = new StringBuffer();
			encodedString.append(rule.getRuleNumber()).append(s1);
			if (moneyTypesToRule.get(rule.getRuleNumber()) == null) {
				encodedString.append(s1);
			} else {
				Iterator<String> it = moneyTypesToRule.get(rule.getRuleNumber()).keySet().iterator();
				while (it.hasNext()) {
					String mt = it.next();
					encodedString.append(mt).append(s3);
					encodedString.append(moneyTypesToRule.get(rule.getRuleNumber()).get(mt));
					if (it.hasNext())
						encodedString.append(s2);
					else
						encodedString.append(s1);
				}
			}
			if ( rule.getEmployerContributionMaxMatchAmount() != null ) {
				encodedString.append(PlanContributionRule.TYPE_DOLLAR).append(s1);
				encodedString.append( rule.getEmployerContributionMaxMatchAmount()).append(s1);
			} else if ( rule.getEmployerContributionMaxMatchPercent() != null ) {
				encodedString.append(PlanContributionRule.TYPE_PERCENT).append(s1);
				encodedString.append(rule.getEmployerContributionMaxMatchPercent()).append(s1);
			} else {
				encodedString.append(s1);
				encodedString.append(s1);
			}
			encodedString.append(rule.getEmployerMatchContributionFirstPercent()!= null ? rule.getEmployerMatchContributionFirstPercent()  : "").append(s1);
			encodedString.append(rule.getEmployerMatchContributionNextPercent()!= null ? rule.getEmployerMatchContributionNextPercent()  : "").append(s1);

			if ( rule.getEmployerMatchFirstEmployeeDeferralAmount() != null ) {
				encodedString.append(PlanContributionRule.TYPE_DOLLAR).append(s1);
				encodedString.append( rule.getEmployerMatchFirstEmployeeDeferralAmount()).append(s1);
			} else if ( rule.getEmployerMatchFirstEmployeeDeferralPercent() != null ) {
				encodedString.append(PlanContributionRule.TYPE_PERCENT).append(s1);
				encodedString.append(rule.getEmployerMatchFirstEmployeeDeferralPercent()).append(s1);
			} else {
				encodedString.append(s1);
				encodedString.append(s1);
			}

			if ( rule.getEmployerMatchNextEmployeeDeferralAmount() != null ) {
				encodedString.append(PlanContributionRule.TYPE_DOLLAR).append(s1);
				encodedString.append( rule.getEmployerMatchNextEmployeeDeferralAmount()).append(s1);
			} else if ( rule.getEmployerMatchNextEmployeeDeferralPercent() != null ) {
				encodedString.append(PlanContributionRule.TYPE_PERCENT).append(s1);
				encodedString.append(rule.getEmployerMatchNextEmployeeDeferralPercent()).append(s1);
			} else {
				encodedString.append(s1);
				encodedString.append(s1);
			}
			DecimalFormat format = new DecimalFormat("######.##");
			encodedString.append(rule.getNonElectiveContributionPercent()!= null ? format.format(rule.getNonElectiveContributionPercent().doubleValue())  : "").append(s1);
			encodedString.append(rule.getRuleCode());
			
			ruleSets[rule.getRuleNumber()-1] = encodedString.toString();
			
		}
		setEmployerRulesets(ruleSets);

		ContractServiceDelegate delegate = ContractServiceDelegate
				.getInstance();
		
		try {
			setViewableRules(delegate.getPlanContributionRulesDescriptions(
					planData.getPlanContributionRules(), planData
							.getManulifeCompanyId()));
		} catch (SystemException e) {
			throw new RuntimeException(e);
		}
		
		
		planData.getPlanEmployeeDeferralElection().setEmployeeDeferralElectionCodeDisplayDates(
				PlanEmployeeDeferralElection.getCalculatedEmployeeDeferralElectionDaysDisplay(
						planData.getPlanYearEnd(), planData.getPlanEmployeeDeferralElection()
								.getEmployeeDeferralElectionCode(), planData.getPlanEmployeeDeferralElection()
								.getEmployeeDeferralElectionSelectedDay(), planData.getPlanEmployeeDeferralElection()
								.getEmployeeDeferralElectionSelectedMonths(), new SimpleDateFormat("MMM d")));

    }

    /**
     * Converts the matching fields from this object, to the PlanData bean.
     */
    public final void convertToBean() {

        try {
            BeanUtils.copyProperties(planData, this);
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        }

        // Convert employer tax identification number
        planData.setEmployerTaxIdentificationNumber(new StringBuffer(StringUtils
                .trimToEmpty(getEmployerTaxIdentificationNumberPart1())).append(
                StringUtils.trimToEmpty(getEmployerTaxIdentificationNumberPart2())).toString());

        // Convert year end
        if (StringUtils.isNotBlank(getFirstPlanEntryDateString())) {
            planData.setFirstPlanEntryDate(new DayOfYear(getFirstPlanEntryDateString()));
        } else {
            planData.setFirstPlanEntryDate(null);
        }

        // Convert withdrawal reasons
        if (ArrayUtils.isEmpty(getSelectedWithdrawalReasons())) {
            planData.setWithdrawalReasons(new ArrayList<String>(0));
        } else {
            planData.setWithdrawalReasons(Arrays.asList(getSelectedWithdrawalReasons()));
        }

        // Convert unvested options
        final List<String> options = new ArrayList<String>();
        if (!ArrayUtils.isEmpty(getSelectedUnvestedMoneyOptions())) {
            options.addAll(Arrays.asList(getSelectedUnvestedMoneyOptions()));
        }
        planData.setUnvestedMoneyOptions(options);

        // Convert allowable money types for withdrawals
        if (ArrayUtils.isEmpty(getSelectedAllowableMoneyTypesForWithdrawals())) {
            planData.setAllowableMoneyTypesForWithdrawals(new ArrayList<String>(0));
        } else {
            planData.setAllowableMoneyTypesForWithdrawals(Arrays
                    .asList(getSelectedAllowableMoneyTypesForWithdrawals()));
        }

		/*
		 * Loans
		 */
		if (!PlanData.YES_CODE.equals(planData.getLoansAllowed())) {
			planData.setMaximumAmortizationPeriodGeneralPurpose(null);
			planData.setMaximumAmortizationPeriodHardship(null);
			planData.setMaximumAmortizationPeriodPrimaryResidence(null);
			planData.setMinimumLoanAmount(null);
			planData.setMaximumLoanAmount(null);
			planData.setMaximumLoanPercentage(null);
			planData.setLoanInterestRateAbovePrime(null);
			planData.setMaximumNumberofOutstandingLoans(null);
			planData.setAllowableMoneyTypesForLoans(new ArrayList<String>(0));

			setMaximumAmortizationPeriodGeneralPurpose(null);
			setMaximumAmortizationPeriodHardship(null);
			setMaximumAmortizationPeriodPrimaryResidence(null);
			setLoanInterestRateAbovePrime(null);
			setMaximumLoanAmount(null);
			setMinimumLoanAmount(null);
			setMaximumLoanPercentage(null);
		
		} else {
			// Convert allowable money types for loans
			if (ArrayUtils.isEmpty(getSelectedAllowableMoneyTypesForLoans())) {
				planData
						.setAllowableMoneyTypesForLoans(new ArrayList<String>(0));
			} else {
				planData.setAllowableMoneyTypesForLoans(Arrays
						.asList(getSelectedAllowableMoneyTypesForLoans()));
			}
		}

        // Load the Vesting Schedules
        if (CollectionUtils.isEmpty(getVestingSchedules())) {
            planData.setVestingSchedules(new ArrayList<VestingSchedule>(0));
        } else {
            final Collection<VestingSchedule> convertedVestingSchedules = new ArrayList<VestingSchedule>(
                    getVestingSchedules().size());
            boolean multipleVestingSchedulesForOneSingleMoneyType = PlanData.YES_CODE.equals(getPlanData().getMultipleVestingSchedulesForOneSingleMoneyType());
            
            for (VestingScheduleUi vestingScheduleUi : getVestingSchedules()) {

                vestingScheduleUi.convertToBean();
				if (multipleVestingSchedulesForOneSingleMoneyType) {
					vestingScheduleUi.getVestingSchedule().setVestingScheduleType("");
				}
	            convertedVestingSchedules.add(vestingScheduleUi.getVestingSchedule());
            }
            planData.setVestingSchedules(convertedVestingSchedules);
        }

		boolean hasHoursOfServiceCreditingMethod = false;

		// Load the Money type eligibility criteria
		if (CollectionUtils.isEmpty(getMoneyTypeEligibilityCriteria())) {
			planData
					.setMoneyTypeEligibilityCriteria(new ArrayList<MoneyTypeEligibilityCriterion>(
							0));
		} else {
			
			final List<MoneyTypeEligibilityCriterion> convertedList = new ArrayList<MoneyTypeEligibilityCriterion>(
					getMoneyTypeEligibilityCriteria().size());
	    	boolean multipleEligibilityRulesForOneSingleMoneyType = PlanData.YES_CODE
					.equals(getPlanData()
							.getMultipleEligibilityRulesForOneSingleMoneyType());

	    	for (MoneyTypeEligibilityCriterionUi moneyTypeEligibilityCriterionUi : getMoneyTypeEligibilityCriteria()) {
				moneyTypeEligibilityCriterionUi.convertToBean();

				MoneyTypeEligibilityCriterion moneyTypeEligibilityCriterion = moneyTypeEligibilityCriterionUi
					.getMoneyTypeEligibilityCriterion();
				
				moneyTypeEligibilityCriterion.removeMessages();
				
				if (multipleEligibilityRulesForOneSingleMoneyType) {
					/*
					 * If more than one eligibility rules for a single money type.
					 * Reset all values.
					 */
	            	moneyTypeEligibilityCriterion.setHoursOfService(null);
	            	moneyTypeEligibilityCriterion.setImmediateEligibilityIndicator(Boolean.FALSE);
	            	moneyTypeEligibilityCriterion.setServiceCreditingMethod(PlanData.UNSPECIFIED_CODE);
	            	moneyTypeEligibilityCriterion.setMinimumAge(null);
	            	moneyTypeEligibilityCriterion.setPeriodOfService(null);
	            	moneyTypeEligibilityCriterion.setPeriodOfServiceUnit(null);
	            	moneyTypeEligibilityCriterionUi.setServiceCreditingMethod(PlanData.UNSPECIFIED_CODE);
	            	moneyTypeEligibilityCriterion.setPartTimeEligibilityIndicator(Boolean.FALSE);
	            	
	            	if (!PlanData.YES_CODE.equals(planData
							.getAutomaticEnrollmentAllowed())) {
						moneyTypeEligibilityCriterion
								.setPlanEntryFrequencyIndicator(PlanData.UNSPECIFIED_CODE);
						moneyTypeEligibilityCriterionUi
								.setPlanEntryFrequencyIndicator(PlanData.UNSPECIFIED_CODE);
					} else {
						if (!"EEDEF".equals(moneyTypeEligibilityCriterion
								.getMoneyTypeId())) {
							moneyTypeEligibilityCriterion
									.setPlanEntryFrequencyIndicator(PlanData.UNSPECIFIED_CODE);
							moneyTypeEligibilityCriterionUi
									.setPlanEntryFrequencyIndicator(PlanData.UNSPECIFIED_CODE);
						}
					}
	            } else if (moneyTypeEligibilityCriterion.getImmediateEligibilityIndicator() != null &&
	            		moneyTypeEligibilityCriterion.getImmediateEligibilityIndicator()) {
	            	moneyTypeEligibilityCriterion.setHoursOfService(null);
	            	moneyTypeEligibilityCriterion.setServiceCreditingMethod(PlanData.UNSPECIFIED_CODE);
	            	moneyTypeEligibilityCriterion.setMinimumAge(null);
	            	moneyTypeEligibilityCriterion.setPeriodOfService(null);
	            	moneyTypeEligibilityCriterion.setPeriodOfServiceUnit(null);
	            	
	            	moneyTypeEligibilityCriterionUi.setServiceCreditingMethod(PlanData.UNSPECIFIED_CODE);
	            }

				if (PlanData.VESTING_SERVICE_CREDIT_METHOD_HOURS_OF_SERVICE
						.equals(moneyTypeEligibilityCriterion
								.getServiceCreditingMethod())) {
					hasHoursOfServiceCreditingMethod = true;
				}

				convertedList.add(moneyTypeEligibilityCriterion);
			}
			planData.setMoneyTypeEligibilityCriteria(convertedList);
		}
		
		if (! hasHoursOfServiceCreditingMethod) {
			/*
			 * PLA.772 - Eligibility computation period should be disabled if there is no hours of service crediting method.
			 */
			planData.setEligibilityComputationPeriodAfterTheInitialPeriod(PlanData.UNSPECIFIED_CODE);
		}

        // Load the Money type excluded employee list.
		if (CollectionUtils.isEmpty(getMoneyTypeExcludedEmployees())) {
			planData
					.setMoneyTypeExcludedEmployees(new ArrayList<MoneyTypeExcludedEmployee>(
							0));
		} else {
			final List<MoneyTypeExcludedEmployee> convertedList = new ArrayList<MoneyTypeExcludedEmployee>(
					getMoneyTypeExcludedEmployees().size());
			for (MoneyTypeExcludedEmployeeUi moneyTypeExcludedEmployeeUi : getMoneyTypeExcludedEmployees()) {
				moneyTypeExcludedEmployeeUi.convertToBean();
				convertedList.add(moneyTypeExcludedEmployeeUi
						.getMoneyTypeExcludedEmployee());
			}
			planData.setMoneyTypeExcludedEmployees(convertedList);
		}
		
		if (!PlanData.YES_CODE.equals(planData.getAutomaticEnrollmentAllowed())) {
			planData.setIsNinetyDayOrShorterWithdrawalElectionOffered(PlanData.UNSPECIFIED_CODE);
			planData.setDeferralPercentageForAutomaticEnrollment(null);
			planData.setAutomaticEnrollmentEffectiveDate(null);
			// clear out values that were cleared from JavaScripts
			setDeferralPercentageForAutomaticEnrollment("");
			setAutomaticEnrollmentEffectiveDate("");
		}
		
		/*
		 * Clear selected day and months when OTHER is not selected
		 */
		if (!PlanEmployeeDeferralElection.OTHER.equals(planData
				.getPlanEmployeeDeferralElection()
				.getEmployeeDeferralElectionCode())) {
			planData.getPlanEmployeeDeferralElection()
					.setEmployeeDeferralElectionSelectedMonths(
							new ArrayList<String>());
			planData.getPlanEmployeeDeferralElection()
					.setEmployeeDeferralElectionSelectedDay("");
		} else {
			// select deferral months
			planData.getPlanEmployeeDeferralElection()
					.setEmployeeDeferralElectionSelectedMonths(
							Arrays.asList(getSelectedDeferralElectionMonths()));
		}
		
		PlanDeferralLimits planDeferralLimits = planData.getPlanDeferral();
		if (planDeferralLimits == null) {
			planDeferralLimits = new PlanDeferralLimits();
			planData.setPlanDeferral(planDeferralLimits);
		}

		if (!StringUtils.isBlank(deferralMaxPercent)) {
			planDeferralLimits.setDeferralMaxPercent(new BigDecimal(
					deferralMaxPercent).setScale(PlanDeferralLimits.PERCENTAGE_SCALE));
		} else {
			planDeferralLimits.setDeferralMaxPercent(null);
		}

		if (!StringUtils.isBlank(deferralMinPercent)) {
			planDeferralLimits.setDeferralMinPercent(new BigDecimal(
					deferralMinPercent).setScale(PlanDeferralLimits.PERCENTAGE_SCALE));
		} else {
			planDeferralLimits.setDeferralMinPercent(null);
		}

		// clean deferral max amount
		if (planDeferralLimits.getDeferralIrsApplies() != null
				&& planDeferralLimits.getDeferralIrsApplies()) {
			planData.getPlanDeferral().setDeferralMaxAmount(null);
		}
		
		// clear ACI fields
		if (!PlanData.YES_CODE.equals(planData.getAciAllowed())) {
			planData.getPlanAutoContributionIncrease().setEffectiveDate(null);
			planData.getPlanAutoContributionIncrease().setAppliesTo(null);
			planData.getPlanAutoContributionIncrease()
					.setDefaultAutoIncreaseMaxPercent(null);
			planData.getPlanAutoContributionIncrease()
					.setDefaultIncreasePercent(null);
			planData.getPlanAutoContributionIncrease()
					.setEffectiveDate(null);
			planData.getPlanAutoContributionIncrease()
					.setNewParticipantApplyDate(null);
			planData.getPlanAutoContributionIncrease().setAnnualApplyDate(null);
			// clear out values that were cleared from JavaScripts
			setAciEffectiveDate("");
			setAciAppliesToEffectiveDate("");
			setAciApplyDate("");
		} else {
			try {
				SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yyyy");
				if (StringUtils.isNotEmpty(getAciEffectiveDate())) {
					planData.getPlanAutoContributionIncrease()
							.setEffectiveDate(
									sf.parse(getAciEffectiveDate()));
				} else {
					planData.getPlanAutoContributionIncrease()
							.setEffectiveDate(null);
				}
				if (StringUtils.isNotEmpty(getAciAppliesToEffectiveDate())) {
					planData.getPlanAutoContributionIncrease()
							.setNewParticipantApplyDate(
									sf.parse(getAciAppliesToEffectiveDate()));
				} else {
					planData.getPlanAutoContributionIncrease()
							.setNewParticipantApplyDate(null);
				}
				if (StringUtils.isNotEmpty(getAciApplyDate())) {
					planData.getPlanAutoContributionIncrease()
							.setAnnualApplyDate(
									new DayOfYear(getAciApplyDate()));
				} else {
					planData.getPlanAutoContributionIncrease()
							.setAnnualApplyDate(null);
				}
			} catch (ParseException e) {
				// todo - do something here.
			}
		}
		
		/*
		 * Clears the date field if not for New Participants.
		 */
		if (!PlanAutoContributionIncrease.APPLY_TO_PPT_NEW_PARTICIPANT_CODE
				.equals(planData.getPlanAutoContributionIncrease()
						.getAppliesTo())) {
			planData.getPlanAutoContributionIncrease().setNewParticipantApplyDate(null);
		}
		
		/*
		this table summarizes how the data maps;
		0, Rule Number, 
		1, list of money types
		2, getEmployerContributionMaxMatchType
		3, getEmployerContributionMaxMatchValue
		4, getEmployerMatchContributionFirstPercent
		5, getEmployerMatchContributionNextPercent
		6, getEmployerMatchFirstEmployeeDeferralType
		7, getEmployerMatchFirstEmployeeDeferralValue
		8, getEmployerMatchNextEmployeeDeferralType
		9, getEmployerMatchNextEmployeeDeferralValue
		10, getNonElectiveContributionPercent
		11, ruleCode      
		*/       		
		//convert rulesets
		List<PlanMoneyTypeEmployerContribution> contributionMoneyTypes = new ArrayList<PlanMoneyTypeEmployerContribution>();
		List<PlanMoneyTypeEmployerContribution> filteredContributionMoneyTypes = new ArrayList<PlanMoneyTypeEmployerContribution>();
		List<PlanContributionRule> rules = new ArrayList<PlanContributionRule>();
		planData.setPlanMoneyTypeEmployerContributions(filteredContributionMoneyTypes);
		planData.setPlanContributionRules(rules);
		Integer postedRuleSets = planData.getNumberOfRuleSets();
		for ( String ruleset : employerRulesets ) {
			if ( rules.size() >= postedRuleSets) continue;
			if ( ruleset == null ) continue;
			List<String> vals = Arrays.asList(ruleset.split(s1,12));
			List<String> moneyTypes = Arrays.asList(vals.get(1).split(s2));
			for ( String moneyType : moneyTypes ) {
				if ( StringUtils.isEmpty(moneyType) ) continue;
				String[] moneyTypeMap = moneyType.split(s3);
				PlanMoneyTypeEmployerContribution contrib = new PlanMoneyTypeEmployerContribution();
				contrib.setRuleNumber(Integer.parseInt(vals.get(0)));
				contrib.setMoneyTypeId(moneyTypeMap[0]);
				contrib.setSelectedInd(StringUtils.equals(moneyTypeMap[1], Boolean.TRUE.toString()));
				contributionMoneyTypes.add(contrib);
			}

			PlanContributionRule rule = new PlanContributionRule();
			rule.setRuleNumber(Integer.parseInt(vals.get(0)));
			if ( StringUtils.equals(vals.get(2), PlanContributionRule.TYPE_PERCENT) ) {
				rule.setEmployerContributionMaxMatchPercent(StringUtils.isNotEmpty(vals.get(3)) ? new BigDecimal(vals.get(3)) : null);
			}
			if ( StringUtils.equals(vals.get(2), PlanContributionRule.TYPE_DOLLAR) ) {
				rule.setEmployerContributionMaxMatchAmount(StringUtils.isNotEmpty(vals.get(3)) ? new BigDecimal(vals.get(3)) : null);
			}
			rule.setEmployerMatchContributionFirstPercent(StringUtils.isNotEmpty(vals.get(4)) ? new BigDecimal(vals.get(4)) : null);
			rule.setEmployerMatchContributionNextPercent(StringUtils.isNotEmpty(vals.get(5)) ? new BigDecimal(vals.get(5)) : null);

			if ( StringUtils.equals(vals.get(6), PlanContributionRule.TYPE_PERCENT) ) {
				rule.setEmployerMatchFirstEmployeeDeferralPercent(StringUtils.isNotEmpty(vals.get(7)) ? new BigDecimal(vals.get(7)) : null);
			}
			if ( StringUtils.equals(vals.get(6), PlanContributionRule.TYPE_DOLLAR) ) {
				rule.setEmployerMatchFirstEmployeeDeferralAmount(StringUtils.isNotEmpty(vals.get(7)) ? new BigDecimal(vals.get(7)) : null);
			}
			if ( StringUtils.equals(vals.get(8), PlanContributionRule.TYPE_PERCENT) ) {
				rule.setEmployerMatchNextEmployeeDeferralPercent(StringUtils.isNotEmpty(vals.get(9)) ? new BigDecimal(vals.get(9)) : null);
			}
			if ( StringUtils.equals(vals.get(8), PlanContributionRule.TYPE_DOLLAR) ) {
				rule.setEmployerMatchNextEmployeeDeferralAmount(StringUtils.isNotEmpty(vals.get(9)) ? new BigDecimal(vals.get(9)) : null);
			}
			rule.setNonElectiveContributionPercent(StringUtils.isNotEmpty(vals.get(10)) ? new BigDecimal(vals.get(10)) : null);
			rule.setRuleCode(vals.get(11));
			
			//add the ui fields needed for validation
			rule.setMaxMatchType(vals.get(2));
			rule.setMaxMatchValue(vals.get(3));
			rule.setFirstEmployeeDeferralType(vals.get(6));
			rule.setFirstEmployeeDeferralValue(vals.get(7));
			rule.setNextEmployeeDeferralType(vals.get(8));
			rule.setNextEmployeeDeferralValue(vals.get(9));
			
			//lets see if we should save this rule or not!
			if (rule.getEmployerContributionMaxMatchAmount() != null
					|| rule.getEmployerContributionMaxMatchPercent() != null
					|| rule.getEmployerMatchContributionFirstPercent() != null
					|| rule.getEmployerMatchContributionNextPercent() != null
					|| rule.getEmployerMatchFirstEmployeeDeferralAmount() != null
					|| rule.getEmployerMatchFirstEmployeeDeferralPercent() != null
					|| rule.getEmployerMatchNextEmployeeDeferralAmount() != null
					|| rule.getEmployerMatchNextEmployeeDeferralPercent() != null
					|| rule.getNonElectiveContributionPercent() != null || StringUtils.isNotEmpty(rule.getRuleCode())
					|| rule.getSelectedMoneyTypes(contributionMoneyTypes).size() > 0) {
				rules.add(rule);
			}
		}
		//filter the Money Types.  Only use money types that have a rule. 
		for ( PlanMoneyTypeEmployerContribution moneyType : contributionMoneyTypes ) {
			for ( PlanContributionRule rule : rules ) {
				if ( rule.getRuleNumber().equals(moneyType.getRuleNumber()) ) {
					filteredContributionMoneyTypes.add(moneyType);
					break;
				}
			}
		}
		
		
		
		
		if (planData.getWithdrawalDistributionMethod()
				.getPartialWithdrawalIndicator() != null
				&& !planData.getWithdrawalDistributionMethod()
						.getPartialWithdrawalIndicator()) {
			planData.getWithdrawalDistributionMethod().setMinWithdrawalAmount(
					null);
			setPartialWithdrawalMinimumWithdrawalAmount("");
		} else {
			Integer minWithdrawalAmount = null;
			if (!StringUtils
					.isBlank(getPartialWithdrawalMinimumWithdrawalAmount())) {
				minWithdrawalAmount = new Integer(
						getPartialWithdrawalMinimumWithdrawalAmount().replace(
								",", "").replace(".00", ""));
			}
			planData.getWithdrawalDistributionMethod().setMinWithdrawalAmount(
					minWithdrawalAmount);
		}
		
		if (!PlanData.YES_CODE.equals(planData.getQdiaRestrictionImposed())) {
			planData.setQdiaRestrictionDetails("");
		}
    }

    /**
     * @return the planData
     */
    public PlanData getPlanData() {
        return planData;
    }

    /**
     * @param planData the planData to set
     */
    public void setPlanData(final PlanData planData) {
        this.planData = planData;
    }

    public String getAutomaticEnrollmentEffectiveDate() {
		return automaticEnrollmentEffectiveDate;
	}

	public void setAutomaticEnrollmentEffectiveDate(
			String automaticEnrollmentEffectiveDate) {
		this.automaticEnrollmentEffectiveDate = automaticEnrollmentEffectiveDate;
	}

	/**
     * @return the planEffectiveDate
     */
    public String getPlanEffectiveDate() {
        return planEffectiveDate;
    }

    /**
     * @param planEffectiveDate the planEffectiveDate to set
     */
    public void setPlanEffectiveDate(final String planEffectiveDate) {
        this.planEffectiveDate = planEffectiveDate;
    }

    public String getIntendsToMeetIrcQualifiedAutomaticContributionArrangementDisplay() {
    	
    	if (PlanData.UNSPECIFIED.equals(planData.getIntendsToMeetIrcQualifiedAutomaticContributionArrangementDisplay())) {
    		return "";
    	}
    	
    	return planData.getIntendsToMeetIrcQualifiedAutomaticContributionArrangementDisplay();
    }
        	    
    public String getLoansAllowedDisplay() {
    	if ( StringUtils.equals(PlanData.UNSPECIFIED_CODE, planData.getLoansAllowed())) {
    		return "";
    	} else{
    		return planData.getLoansAllowedDisplay();
    	}
    }
    
    public String getIsNinetyDayOrShorterWithdrawalElectionOfferedDisplay() {
    	
    	if (PlanData.UNSPECIFIED.equals(planData.getIsNinetyDayOrShorterWithdrawalElectionOfferedDisplay())) {
    		return "";
    	}
    	return planData.getIsNinetyDayOrShorterWithdrawalElectionOfferedDisplay();
    }

    public String getAutomaticEnrollmentEffectiveDateDisplay() {
		if (PlanData.YES_CODE.equals(planData.getAutomaticEnrollmentAllowed())) {

			if (planData.getAutomaticEnrollmentEffectiveDate() != null) {
				Date today = DateUtils.truncate(new Date(), Calendar.DATE);
				if (planData.getAutomaticEnrollmentEffectiveDate().compareTo(
						today) > 0) {
					FastDateFormat sdf = FastDateFormat
							.getInstance("MMMMM d, yyyy");
					return ", effective "
							+ sdf.format(planData
									.getAutomaticEnrollmentEffectiveDate());
				}
			}
		}
		return "";
	}
    
    /**
     * Get the display value of automatic enrollment
     */
    public String getAutomaticEnrollmentAllowedDisplay() {
    	
    	if (PlanData.UNSPECIFIED.equals(planData.getAutomaticEnrollmentAllowedDisplay())) {
    		return "";
    	}
    	return planData.getAutomaticEnrollmentAllowedDisplay();
    }

    public String getIndividualSecuritiesInvestmentAllowedDisplay() {
    	
    	if (PlanData.UNSPECIFIED.equals(planData.getIndividualSecuritiesInvestmentAllowedDisplay())) {
    		return "";
    	}
		return planData.getIndividualSecuritiesInvestmentAllowedDisplay();
	}

	/**
	 * Get the display value of imposed QDIA restriction indicator
	 */
	public String getQdiaRestrictionImposedDisplay() {
		
		if (PlanData.UNSPECIFIED.equals(planData.getQdiaRestrictionImposedDisplay())) {
    		return "";
    	}
		return planData.getQdiaRestrictionImposedDisplay();
	}
    
   	/**
     * Get the display value of the rollovers delayed until eligibility requirements are met.
     */
    public String getRolloversDelayedUntilEligibilityReqtMetDisplay() {
    	
    	if (PlanData.UNSPECIFIED.equals(planData.getRolloversDelayedUntilEligibilityReqtMetDisplay())) {
    		return "";
    	}
        return planData.getRolloversDelayedUntilEligibilityReqtMetDisplay();
    }
    
    /**
     * Get the display value of the allow permitted disparity.
     */
    public String getAllowPermittedDisparityDisplay() {
        return getYesNoUnspecifiedDisplay(planData.getAllowPermittedDisparity());
    }

    /**
     * Get the display value of the is safe harbor plan.
     */
    public String getIsSafeHarborPlanDisplay() {
    	
    	if (PlanData.UNSPECIFIED.equals(planData.getIsSafeHarborPlanDisplay())) {
    		return "";
    	}
    	
        return planData.getIsSafeHarborPlanDisplay();
    }

    /**
     * Get the display value of the mutliple eligibility rules for any single money type.
     */
    public String getMultipleEligibilityRulesForOneSingleMoneyTypeDisplay() {
    	
    	if (PlanData.UNSPECIFIED.equals(planData.getMultipleEligibilityRulesForOneSingleMoneyTypeDisplay())) {
    		return "";
    	}
        return planData.getMultipleEligibilityRulesForOneSingleMoneyTypeDisplay();
    }
    
    /**
     * Get the display value of the requires spousal consent for distributions.
     */
    public String getRequiresSpousalConsentForDistributionsDisplay() {
    	if (PlanData.UNSPECIFIED.equals(planData.getRequiresSpousalConsentForDistributionsDisplay())) {
    		return "";
    	}
        return planData.getRequiresSpousalConsentForDistributionsDisplay();
    }
    
    /**
     * Get the display value of the eedefEarningsAllowedInd.
     */
    public String getEedefEarningsAllowedIndDisplay() {
    	if (PlanData.UNSPECIFIED_CODE.equals(planData.getEedefEarningsAllowedInd())) {
    		return "";
    	}
        return planData.getEedefEarningsAllowedInd();
    }
    
    /**
     * Get the display value of the plan entry date basis.
     */
    public String getPlanEntryDateBasisDisplay() {
    	if (PlanData.UNSPECIFIED.equals(planData.getPlanEntryDateBasisDisplay())) {
    		return "";
    	}
    	return planData.getPlanEntryDateBasisDisplay();
    }

    /**
     * @return the selectedUnvestedMoneyOptions
     */
    public String[] getSelectedUnvestedMoneyOptions() {
        return selectedUnvestedMoneyOptions;
    }

    /**
     * @param selectedUnvestedMoneyOptions the selectedUnvestedMoneyOptions to set
     */
    public void setSelectedUnvestedMoneyOptions(final String[] selectedUnvestedMoneyOptions) {
        this.selectedUnvestedMoneyOptions = selectedUnvestedMoneyOptions;
    }

    /**
     * @return the selectedWithdrawalReasons
     */
    public String[] getSelectedWithdrawalReasons() {
        return selectedWithdrawalReasons;
    }

    /**
     * @param selectedWithdrawalReasons the selectedWithdrawalReasons to set
     */
    public void setSelectedWithdrawalReasons(final String[] selectedWithdrawalReasons) {
        this.selectedWithdrawalReasons = selectedWithdrawalReasons;
    }

    /**
     * Get the radio button value of the hardship withdrawal provisions.
     */
    public String getHardshipWithdrawalProvisionsRadio() {
        final String hardshipWithdrawalProvision = planData.getHardshipWithdrawalProvisions();
        if (StringUtils.isEmpty(hardshipWithdrawalProvision)) {
            return PlanData.UNSPECIFIED_CODE;
        } else {
            return hardshipWithdrawalProvision;
        }
    }

    /**
     * Set the hardship withdrawal provisions using the radio button value.
     */
    public void setHardshipWithdrawalProvisionsRadio(final String code) {
        if (StringUtils.equals(PlanData.UNSPECIFIED_CODE, code)) {
            planData.setHardshipWithdrawalProvisions(StringUtils.EMPTY);
        } else {
            planData.setHardshipWithdrawalProvisions(code);
        }
    }

    /**
     * Get the display value of the hardship withdrawal provisions.
     */
    public String getHardshipWithdrawalProvisionsDisplay() {

        // We don't pass through to the plan data version because we are escaping the ampersand here
        final String hardshipProvision = planData.getHardshipWithdrawalProvisions();
        if (StringUtils.equals(PlanData.HARDSHIP_WITHDRAWAL_PROVISION_FACTS_AND_CIRCUMSTANCES,
                hardshipProvision)) {
            return FACTS_AND_CIRCUMSTANCES;
        } else if (StringUtils.equals(PlanData.HARDSHIP_WITHDRAWAL_PROVISION_SAFE_HARBOR,
                hardshipProvision)) {
            return SAFE_HARBOR;
        } else {
            return "";
        }
    }

    /**
     * @return the selectedAllowableMoneyTypesForLoans
     */
    public String[] getSelectedAllowableMoneyTypesForLoans() {
        return selectedAllowableMoneyTypesForLoans;
    }

    /**
     * @param selectedAllowableMoneyTypesForLoans the selectedAllowableMoneyTypesForLoans to set
     */
    public void setSelectedAllowableMoneyTypesForLoans(
            final String[] selectedAllowableMoneyTypesForLoans) {
        this.selectedAllowableMoneyTypesForLoans = selectedAllowableMoneyTypesForLoans;
    }

    /**
     * @return the minimumLoanAmount
     */
    public String getMinimumLoanAmount() {
        return minimumLoanAmount;
    }

    /**
     * @param minimumLoanAmount the minimumLoanAmount to set
     */
    public void setMinimumLoanAmount(final String minimumLoanAmount) {
        this.minimumLoanAmount = minimumLoanAmount;
    }

    /**
     * @return the maximumLoanAmount
     */
    public String getMaximumLoanAmount() {
        return maximumLoanAmount;
    }

    /**
     * @param maximumLoanAmount the maximumLoanAmount to set
     */
    public void setMaximumLoanAmount(final String maximumLoanAmount) {
        this.maximumLoanAmount = maximumLoanAmount;
    }

    /**
     * @return the maximumLoanPercentage
     */
    public String getMaximumLoanPercentage() {
        return maximumLoanPercentage;
    }

    /**
     * @param maximumLoanPercentage the maximumLoanPercentage to set
     */
    public void setMaximumLoanPercentage(final String maximumLoanPercentage) {
        this.maximumLoanPercentage = maximumLoanPercentage;
    }

    /**
     * @return the maximumNumberofOutstandingLoans
     */
    public String getMaximumNumberofOutstandingLoansSelect() {
        // Convert to the unlimited code rather than using null or empty string
        if (planData.getMaximumNumberofOutstandingLoans() == null) {
            return UNLIMITED_CODE;
        } else {
            return String.valueOf(planData.getMaximumNumberofOutstandingLoans());
        }
    }

    /**
     * @param maximumNumberofOutstandingLoans the maximumNumberofOutstandingLoans to set
     */
    public void setMaximumNumberofOutstandingLoansSelect(final String code) {

        if (StringUtils.equals(UNLIMITED_CODE, code)) {
            planData.setMaximumNumberofOutstandingLoans(null);
        } else {
            planData.setMaximumNumberofOutstandingLoans(Integer.parseInt(code));
        }
    }

    /**
     * @return the loanInterestRateAbovePrime
     */
    public String getLoanInterestRateAbovePrime() {
        return loanInterestRateAbovePrime;
    }

    /**
     * @param loanInterestRateAbovePrime the loanInterestRateAbovePrime to set
     */
    public void setLoanInterestRateAbovePrime(final String loanInterestRateAbovePrime) {
        this.loanInterestRateAbovePrime = loanInterestRateAbovePrime;
    }

    public String getDeferralPercentageForAutomaticEnrollment() {
		return deferralPercentageForAutomaticEnrollment;
	}

	public void setDeferralPercentageForAutomaticEnrollment(
			String deferralPercentageForAutomaticEnrollment) {
		this.deferralPercentageForAutomaticEnrollment = deferralPercentageForAutomaticEnrollment;
	}

	/**
     * @return the firstPlanEntryDateString
     */
    public String getFirstPlanEntryDateString() {
        return firstPlanEntryDateString;
    }

    /**
     * @param firstPlanEntryDateString the firstPlanEntryDateString to set
     */
    public void setFirstPlanEntryDateString(final String firstPlanEntryDateString) {
        this.firstPlanEntryDateString = firstPlanEntryDateString;
    }

    /**
     * @return the retirementAge
     */
    public String getRetirementAge() {
        return retirementAge;
    }

    /**
     * @param retirementAge the retirementAge to set
     */
    public void setRetirementAge(final String retirementAge) {
        this.retirementAge = retirementAge;
    }

    
    public String getRetirementAgeDisplay() {
		return retirementAgeDisplay;
	}

	public void setRetirementAgeDisplay(String retirementAgeDisplay) {
		this.retirementAgeDisplay = retirementAgeDisplay;
	}

	/**
     * @return the retirementServiceMinimumYears
     */
    public String getRetirementServiceMinimumYears() {
        return retirementServiceMinimumYears;
    }

    /**
     * @param retirementServiceMinimumYears the retirementServiceMinimumYears to set
     */
    public void setRetirementServiceMinimumYears(final String retirementServiceMinimumYears) {
        this.retirementServiceMinimumYears = retirementServiceMinimumYears;
    }

    /**
     * @return the earlyRetirementAge
     */
    public String getEarlyRetirementAge() {
        return earlyRetirementAge;
    }
    /**
     * @return the earlyRetirementAge
     */
    public String getEarlyRetirementAgeDisplay() {
        return earlyRetirementAgeDisplay;
    }

    public void setEarlyRetirementAgeDisplay(String earlyRetirementAgeDisplay) {
		this.earlyRetirementAgeDisplay = earlyRetirementAgeDisplay;
	}

	/**
     * @param earlyRetirementAge the earlyRetirementAge to set
     */
    public void setEarlyRetirementAge(final String earlyRetirementAge) {
        this.earlyRetirementAge = earlyRetirementAge;
    }

    /**
     * @return the earlyRetirementServiceMinimumYears
     */
    public String getEarlyRetirementServiceMinimumYears() {
        return earlyRetirementServiceMinimumYears;
    }

    /**
     * @param earlyRetirementServiceMinimumYears the earlyRetirementServiceMinimumYears to set
     */
    public void setEarlyRetirementServiceMinimumYears(
            final String earlyRetirementServiceMinimumYears) {
        this.earlyRetirementServiceMinimumYears = earlyRetirementServiceMinimumYears;
    }

    /**
     * @return the minimumHardshipAmount
     */
    public String getMinimumHardshipAmount() {
        return minimumHardshipAmount;
    }

    /**
     * @param minimumHardshipAmount the minimumHardshipAmount to set
     */
    public void setMinimumHardshipAmount(final String minimumHardshipAmount) {
        this.minimumHardshipAmount = minimumHardshipAmount;
    }

    /**
     * @return the maximumHardshipAmount
     */
    public String getMaximumHardshipAmount() {
        return maximumHardshipAmount;
    }

    /**
     * @param maximumHardshipAmount the maximumHardshipAmount to set
     */
    public void setMaximumHardshipAmount(final String maximumHardshipAmount) {
        this.maximumHardshipAmount = maximumHardshipAmount;
    }

    /**
     * @return the selectedAllowableMoneyTypesForWithdrawals
     */
    public String[] getSelectedAllowableMoneyTypesForWithdrawals() {
        return selectedAllowableMoneyTypesForWithdrawals;
    }

    /**
     * @param selectedAllowableMoneyTypesForWithdrawals the
     *            selectedAllowableMoneyTypesForWithdrawals to set
     */
    public void setSelectedAllowableMoneyTypesForWithdrawals(
            final String[] selectedAllowableMoneyTypesForWithdrawals) {
        this.selectedAllowableMoneyTypesForWithdrawals = selectedAllowableMoneyTypesForWithdrawals;
    }

    /**
     * @return the vestingSchedules
     */
    public Collection<VestingScheduleUi> getVestingSchedules() {
        return vestingSchedules;
    }

    /**
     * @param vestingSchedules the vestingSchedules to set
     */
    public void setVestingSchedules(Collection<VestingScheduleUi> vestingSchedules) {
        this.vestingSchedules = vestingSchedules;
    }

    /**
     * @return the collapsedAddresses
     */
    public Collection<AddressUi> getCollapsedAddresses() {
        return collapsedAddresses;
    }

    /**
     * @param collapsedAddresses the addresses to set
     */
    public void setCollapsedAddresses(final Collection<AddressUi> collapsedAddresses) {
        this.collapsedAddresses = collapsedAddresses;
    }

    /**
     * Retrieves the collapsed addresses.
     * 
     * @param addresses The collection of addresses to collapse.
     * @return Collection<AddressUi> The collection of collapsed addresses.
     */
    protected Collection<AddressUi> getCollapsedAddresses(final Collection<Address> addresses) {

        final Collection<AddressUi> collapsedAddresses = new ArrayList<AddressUi>();
        final String COURIER_TYPE_STRING = Address.Type.COURIER.toString();
        for (Address address : addresses) {

            boolean exists = false;
            for (AddressUi ui : collapsedAddresses) {

                // If address exists in the collection
                if (ui.collapseEquals(address)) {

                    // Modify the type and header
                    ui.setType(Address.Type.COMBINED);
                    ui.setHeader(new StringBuffer(ui.getHeader()).append(AddressUi.SEPARATOR)
                            .append(AddressUi.SPACE).append(
                                    AddressUi.getHeaderForType(address.getType())).toString());

                    // Java enums don't compare properly if they have been serialized, so we have to
                    // use the string form
                    if (StringUtils.equals(COURIER_TYPE_STRING, address.getType().toString())) {
                        // Add the attention if the new type is a courier
                        ui.setAttention(address.getAttention());
                    }

                    // Set flag and break
                    exists = true;
                    break;
                }
            }
            // Check if we found the address, if not add it
            if (!exists) {
                final AddressUi ui = new AddressUi(address);
                ui.setHeader(AddressUi.getHeaderForType(address.getType()));
                collapsedAddresses.add(ui);
            }
        }

        // Fix up the pluralization
        for (AddressUi ui : collapsedAddresses) {
            final int separatorCount = StringUtils.countMatches(ui.getHeader(), AddressUi.AND);

            // Check for no address on record and combined type
            if (StringUtils.equals(ui.getLine1(), PlanData.NO_ADDRESS_ON_RECORD)
                    && (ui.getType() == Address.Type.COMBINED)) {
                ui.setLine1(PlanData.NO_ADDRESSES_ON_RECORD);
            }
        }
        return collapsedAddresses;
    }

    /**
     * Checks the list of collapsed address ui beans for the specified address value object.
     * 
     * @param addresses The collection of ui beans to search.
     * @param address The address data to search for.
     * @return boolean - True if the equivalent address data already exists in the collection.
     */
    private boolean doesAddressExist(final Collection<AddressUi> addresses, final Address address) {
        for (AddressUi ui : addresses) {
            if (ui.collapseEquals(address)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts a Yes/No/Unspecified code to the display value.
     * 
     * @param code The code to convert to display value.
     * @return String - The display value of the specified code.
     */
    private String getYesNoUnspecifiedDisplay(final String code) {
        if (StringUtils.equals(PlanData.YES_CODE, code)) {
            return YES;
        } else if (StringUtils.equals(PlanData.NO_CODE, code)) {
            return NO;
        } else {
            return UNSPECIFIED;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Collection<ValidationError> getValidationMessages(final GraphLocation graphLocation) {

        final Collection<ValidationError> messages = new ArrayList<ValidationError>();

        messages.addAll(getValidationMessages(graphLocation, getPlanData()));

        int i = 0;
        for (VestingScheduleUi vestingScheduleUi : getVestingSchedules()) {
            messages.addAll(vestingScheduleUi.getValidationMessages(new GraphLocation(
                    graphLocation, "vestingSchedules", i)));
            i++;
        }
        
        i = 0;
        for (MoneyTypeEligibilityCriterionUi moneyTypeEligibilityCriterionUi: getMoneyTypeEligibilityCriteria()) {
            messages.addAll(moneyTypeEligibilityCriterionUi.getValidationMessages(new GraphLocation(
                    graphLocation, "moneyTypeEligibilityCriteria", i)));
            i++;
        }
        
        i = 0;
        for (MoneyTypeExcludedEmployeeUi moneyTypeExcludedEmployeeUi: getMoneyTypeExcludedEmployees()) {
            messages.addAll(moneyTypeExcludedEmployeeUi.getValidationMessages(new GraphLocation(
                    graphLocation, "moneyTypeExcludedEmployees", i)));
            i++;
        }

        return messages;
    }

    /**
     * @return the employerTaxIdentificationNumberPart1
     */
    public String getEmployerTaxIdentificationNumberPart1() {
        return employerTaxIdentificationNumberPart1;
    }

    /**
     * @param employerTaxIdentificationNumberPart1 the employerTaxIdentificationNumberPart1 to set
     */
    public void setEmployerTaxIdentificationNumberPart1(
            final String employerTaxIdentificationNumberPart1) {
        this.employerTaxIdentificationNumberPart1 = employerTaxIdentificationNumberPart1;
    }

    /**
     * @return the employerTaxIdentificationNumberPart2
     */
    public String getEmployerTaxIdentificationNumberPart2() {
        return employerTaxIdentificationNumberPart2;
    }

    /**
     * @param employerTaxIdentificationNumberPart2 the employerTaxIdentificationNumberPart2 to set
     */
    public void setEmployerTaxIdentificationNumberPart2(
            final String employerTaxIdentificationNumberPart2) {
        this.employerTaxIdentificationNumberPart2 = employerTaxIdentificationNumberPart2;
    }

    /**
     * @return the maximumAmortizationPeriodGeneralPurpose
     */
    public String getMaximumAmortizationPeriodGeneralPurpose() {
        return maximumAmortizationPeriodGeneralPurpose;
    }

    /**
     * @param maximumAmortizationPeriodGeneralPurpose the maximumAmortizationPeriodGeneralPurpose to
     *            set
     */
    public void setMaximumAmortizationPeriodGeneralPurpose(
            final String maximumAmortizationPeriodGeneralPurpose) {
        this.maximumAmortizationPeriodGeneralPurpose = maximumAmortizationPeriodGeneralPurpose;
    }

    /**
     * @return the maximumAmortizationPeriodHardship
     */
    public String getMaximumAmortizationPeriodHardship() {
        return maximumAmortizationPeriodHardship;
    }

    /**
     * @param maximumAmortizationPeriodHardship the maximumAmortizationPeriodHardship to set
     */
    public void setMaximumAmortizationPeriodHardship(final String maximumAmortizationPeriodHardship) {
        this.maximumAmortizationPeriodHardship = maximumAmortizationPeriodHardship;
    }

    /**
     * @return the maximumAmortizationPeriodPrimaryResidence
     */
    public String getMaximumAmortizationPeriodPrimaryResidence() {
        return maximumAmortizationPeriodPrimaryResidence;
    }

    /**
     * @param maximumAmortizationPeriodPrimaryResidence the
     *            maximumAmortizationPeriodPrimaryResidence to set
     */
    public void setMaximumAmortizationPeriodPrimaryResidence(
            final String maximumAmortizationPeriodPrimaryResidence) {
        this.maximumAmortizationPeriodPrimaryResidence = maximumAmortizationPeriodPrimaryResidence;
    }

    /**
     * Queries if the address line 2 row should be suppressed (if all address line 2 elements are
     * blank).
     * 
     * @return boolean - True if address line 2 row should be suppressed.
     */
    public boolean getSuppressLine2() {
        for (Address address : planData.getAddresses()) {
            if (StringUtils.isNotEmpty(address.getLine2())) {
                return false;
            }
        }

        // All address line 2 data was blank - suppress the row
        return true;
    }

	public Collection<MoneyTypeEligibilityCriterionUi> getMoneyTypeEligibilityCriteria() {
		return moneyTypeEligibilityCriteria;
	}

	public void setMoneyTypeEligibilityCriteria(
			Collection<MoneyTypeEligibilityCriterionUi> moneyTypeEligibilityCriteria) {
		this.moneyTypeEligibilityCriteria = moneyTypeEligibilityCriteria;
	}

	public Collection<MoneyTypeExcludedEmployeeUi> getMoneyTypeExcludedEmployees() {
		return moneyTypeExcludedEmployees;
	}

	public void setMoneyTypeExcludedEmployees(
			Collection<MoneyTypeExcludedEmployeeUi> moneyTypeExcludedEmployees) {
		this.moneyTypeExcludedEmployees = moneyTypeExcludedEmployees;
	}

    /**
     * Get the display value of the multiple eligibility rules for any single money type.
     */
    public String getEligibilityComputationPeriodAfterTheInitialPeriodDisplay() {
    	
    	if (PlanData.UNSPECIFIED.equals(planData.getEligibilityComputationPeriodAfterTheInitialPeriodDisplay())) {
    		return "";
    	}
        return planData.getEligibilityComputationPeriodAfterTheInitialPeriodDisplay();
    }
    
    /**
     * Get the default first plan entry date
     * default is planYearEnd + 1 day
     * 
     * @param planData
     * @return a string of mm/dd 
     */
    private String getPlanYearEndPlusOneString(PlanData planData) {
        String returnString = "";
        DayOfYear planYearEnd = planData.getPlanYearEnd();
        if(planYearEnd != null) {            
            final Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setTime(planYearEnd.getAsDatePlusOneNonLeapYear());
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
            returnString = formatter.format(calendar.getTime());
        }
        return returnString;
    }
    
    public String getVestingComputationPeriodDisplay() {
    	
    	if (PlanData.UNSPECIFIED.equals(planData.getVestingComputationPeriodDisplay())) {
    		return "";
    	}
        return planData.getVestingComputationPeriodDisplay();
    }
    
    public String getMultipleVestingSchedulesForOneSingleMoneyTypeDisplay() {
    	
    	if (PlanData.UNSPECIFIED.equals(planData.getMultipleVestingSchedulesForOneSingleMoneyTypeDisplay())) {
    		return "";
    	}
        return planData.getMultipleVestingSchedulesForOneSingleMoneyTypeDisplay();
    }


	public String[] getSelectedDeferralElectionMonths() {
		return selectedDeferralElectionMonths;
	}

	public void setSelectedDeferralElectionMonths(String[] selectedDeferralElectionMonths) {
		this.selectedDeferralElectionMonths = selectedDeferralElectionMonths;
	}

	public String getAciEffectiveDate() {
		return aciEffectiveDate;
	}

	public void setAciEffectiveDate(String aciEffectiveDate) {
		this.aciEffectiveDate = aciEffectiveDate;
	}


	public String getAciAppliesToEffectiveDate() {
		return aciAppliesToEffectiveDate;
	}

	public void setAciAppliesToEffectiveDate(String aciAppliesToEffectiveDate) {
		this.aciAppliesToEffectiveDate = aciAppliesToEffectiveDate;
	}

	public String getAciApplyDate() {
        return aciApplyDate;
    }

    public void setAciApplyDate(String aciAppliesToEffectiveDate) {
        this.aciApplyDate = aciAppliesToEffectiveDate;
    }

    public String[] getEmployerRulesets() {
		return employerRulesets;
	}

	public void setEmployerRulesets(String[] employerRulesets) {
		this.employerRulesets = employerRulesets;
	}

	public String getPartialWithdrawalMinimumWithdrawalAmount() {
		return partialWithdrawalMinimumWithdrawalAmount;
	}

	public void setPartialWithdrawalMinimumWithdrawalAmount(
			String partialWithdrawalMinimumWithdrawalAmount) {
		this.partialWithdrawalMinimumWithdrawalAmount = partialWithdrawalMinimumWithdrawalAmount;
	}	


    public String[] getViewableRules() {
		return viewableRules;
	}

	public void setViewableRules(String[] viewableRules) {
		this.viewableRules = viewableRules;
	}

	public String getDeferralMaxPercent() {
		return deferralMaxPercent;
	}

	public void setDeferralMaxPercent(String deferralMaxPercent) {
		this.deferralMaxPercent = deferralMaxPercent;
	}

	public void setHasBothEEDEFAndEEROTMoneyTypes(boolean hasBothEEDEFAndEEROTMoneyTypes) {
		this.hasBothEEDEFAndEEROTMoneyTypes = hasBothEEDEFAndEEROTMoneyTypes;
	}

	public boolean getHasBothEEDEFAndEEROTMoneyTypes() {
		return hasBothEEDEFAndEEROTMoneyTypes;
	}
	
	public String getDeferralMinPercent() {
		return deferralMinPercent;
	}

	public void setDeferralMinPercent(String deferralMinPercent) {
		this.deferralMinPercent = deferralMinPercent;
	}
	
	public void updateEligibilityRequirementsForEEROT() {
		MoneyTypeEligibilityCriterionUi moneyTypeEligibilityCriterionUiForEEDEF = null;
		MoneyTypeEligibilityCriterionUi moneyTypeEligibilityCriterionUiForEEROT = null;
		int index = 0;
		int eerotIndex = 0;
		//Object[] moneyTypeEligibilityCriterias = this.getMoneyTypeEligibilityCriteria().toArray();
		MoneyTypeEligibilityCriterionUi[] moneyTypeEligibilityCriterias = (MoneyTypeEligibilityCriterionUi[]) this.getMoneyTypeEligibilityCriteria().toArray(new MoneyTypeEligibilityCriterionUi[0]);

		//Collection<MoneyTypeEligibilityCriterionUi> moneyTypeEligibilityCriterias = this.getMoneyTypeEligibilityCriteria();
		for (MoneyTypeEligibilityCriterionUi moneyTypeEligibilityCriterionUi : moneyTypeEligibilityCriterias) {
			if (moneyTypeEligibilityCriterionUi
					.getMoneyTypeEligibilityCriterion().getMoneyTypeId().equals(ServiceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL)) {
				moneyTypeEligibilityCriterionUiForEEDEF = moneyTypeEligibilityCriterionUi;
			} else if (moneyTypeEligibilityCriterionUi
					.getMoneyTypeEligibilityCriterion().getMoneyTypeId().equals(ServiceFeatureConstants.EMPLOYEE_ROTH_CONTRIBUTION)) {
				moneyTypeEligibilityCriterionUiForEEROT = moneyTypeEligibilityCriterionUi;
				eerotIndex = index;
			}
			index ++;
		}
		if(moneyTypeEligibilityCriterionUiForEEDEF != null && moneyTypeEligibilityCriterionUiForEEROT != null) {
			copyData(moneyTypeEligibilityCriterionUiForEEDEF,
					moneyTypeEligibilityCriterionUiForEEROT,
					MoneyTypeEligibilityCriterionUi.UI_FIELDS,
					MoneyTypeEligibilityCriterionUi.UI_FIELDS_DATATYPES);
			moneyTypeEligibilityCriterionUiForEEROT.convertToBean();
			moneyTypeEligibilityCriterias[eerotIndex] = moneyTypeEligibilityCriterionUiForEEROT;
			Collection<MoneyTypeEligibilityCriterionUi> updatedMoneyTypeEligibilityCriterias = Arrays.asList(moneyTypeEligibilityCriterias);
            this.setMoneyTypeEligibilityCriteria(updatedMoneyTypeEligibilityCriterias);
		}
	}
	
    public void updatePartTimeEligibilityForEEDEFAndEEROT() {
        MoneyTypeEligibilityCriterionUi[] moneyTypeEligibilityCriterias = (MoneyTypeEligibilityCriterionUi[]) this
                .getMoneyTypeEligibilityCriteria().toArray(new MoneyTypeEligibilityCriterionUi[0]);
        for (MoneyTypeEligibilityCriterionUi moneyTypeEligibilityCriterionUi : moneyTypeEligibilityCriterias) {
            if ((ServiceFeatureConstants.EMPLOYEE_ELECTIVE_DEFERAL
                    .equals(moneyTypeEligibilityCriterionUi.getMoneyTypeEligibilityCriterion().getMoneyTypeId())
                    || ServiceFeatureConstants.EMPLOYEE_ROTH_CONTRIBUTION
                            .equals(moneyTypeEligibilityCriterionUi.getMoneyTypeEligibilityCriterion()
							        .getMoneyTypeId()))
                    && (!moneyTypeEligibilityCriterionUi.getImmediateEligibilityIndicator()
                            && MoneyTypeEligibilityCriterion.SERVICE_CREDIT_METHOD_HOURS_OF_SERVICE_CODE.equals(
                                    moneyTypeEligibilityCriterionUi.getServiceCreditingMethod()))) {
                moneyTypeEligibilityCriterionUi.setPartTimeEligibilityIndicator(Boolean.TRUE);
            }
        }
    }
	
	/**
	 * copy date from source to target object
	 * 
	 * @param sourceObject
	 * @param targetObject
	 * @param attributeNames
	 * @throws SystemException
	 */
	private void copyData(MoneyTypeEligibilityCriterionUi sourceObject,
			MoneyTypeEligibilityCriterionUi targetObject,
			String[] attributeNames, Class<?>[] arguements)  {
		final String getterPrefix = "get";
		final String setterPrefix = "set";
		Class<?> thisClass = sourceObject.getClass();
		Method method = null;
		String methodName = null;
		Object resultValue = null; 
		try {
			int i =0 ;
			for (String attribute : attributeNames) {
				Class<?> arguement = arguements[i];
				methodName = getterPrefix + attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
				method = getMethod(thisClass, methodName, null);
				resultValue = method.invoke(sourceObject, new Object[] {});
				methodName = setterPrefix + attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
				method = getMethod(thisClass, methodName, arguement);
				method.invoke(targetObject, new Object[] {resultValue});
				i++;
			}
		} catch (Exception causingException) {
			throw new NestableRuntimeException(causingException);
		}
	}
	
	/**
	 * return the method
	 * @param thisClass
	 * @param methodName
	 * @return Method
	 * @throws SystemException
	 */
	private Method getMethod(Class<?> thisClass, String methodName, Class<?> arguement) {
		Method method = null;
		try {
			if(arguement != null) {
			  method = thisClass.getDeclaredMethod(methodName, new Class[] {arguement});
			} else {
			  method = thisClass.getDeclaredMethod(methodName, new Class[] {});	
			}
		} catch (NoSuchMethodException causingException) {
			throw new NestableRuntimeException(causingException);
		}
		return method;
	}
}
