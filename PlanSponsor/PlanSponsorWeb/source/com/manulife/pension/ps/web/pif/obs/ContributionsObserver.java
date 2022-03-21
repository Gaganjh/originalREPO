package com.manulife.pension.ps.web.pif.obs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.pif.PIFDataUi;
import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.pif.valueobject.PIFMoneyType;
import com.manulife.pension.service.pif.valueobject.PlanInfoContributionsVO;
import com.manulife.pension.service.pif.valueobject.PlanInfoVO;
import com.manulife.pension.service.plan.valueobject.PlanAutoContributionIncrease;
import com.manulife.pension.service.plan.valueobject.PlanContributionRule;
import com.manulife.pension.service.plan.valueobject.PlanDeferralLimits;
import com.manulife.pension.service.plan.valueobject.PlanEmployeeDeferralElection;
import com.manulife.pension.service.plan.valueobject.PlanMoneyTypeEmployerContribution;

/**
 * Observer class for Contributions plan information data
 * @author 	rajenra
 */
public class ContributionsObserver implements Observer {
	
	//splitters
	private static final String s1 = "~1~";
	private static final String s2 = "~2~";
	private static final String s3 = "~3~";
	
	private PlanInfoContributionsVO contributions = new PlanInfoContributionsVO();
	
	/**
	 * @return the contributions
	 */
	public PlanInfoContributionsVO getContributions() {
		return contributions;
	}

	/**
	 * The constructor
	 * @param contributions
	 */
	public ContributionsObserver(PlanInfoContributionsVO contributions) {
		super();
		this.contributions = contributions;
	}

	/**
	 * method to update bean info from planInfoVO 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateFromBean(PIFDataUi uiVO, PlanInfoVO planInfoVO) {
		
		
        if (planInfoVO.getContributions().getPlanDeferralLimits() != null) {
			PlanDeferralLimits planDeferralLimits = planInfoVO.getContributions().getPlanDeferralLimits();
			if (planDeferralLimits != null && planDeferralLimits.getDeferralMaxPercent() != null) {
				uiVO.setDeferralMaxPercent(PlanData.formatPercentageFormatter(Double.parseDouble(planDeferralLimits.getDeferralMaxPercent().toString())));
			}
			if (planDeferralLimits != null && planDeferralLimits.getDeferralMinPercent() != null) {
				uiVO.setDeferralMinPercent(PlanData.formatPercentageFormatter(Double.parseDouble(planDeferralLimits.getDeferralMinPercent().toString())));
			}
		} 
        
        //Set Auto Increase Max Percent
        if (planInfoVO.getContributions().getPlanAutoContributionIncrease()
        		.getDefaultAutoIncreaseMaxPercent() != null) {
			BigDecimal planDeferralLimits = planInfoVO.getContributions().getPlanAutoContributionIncrease()
    		.getDefaultAutoIncreaseMaxPercent();
			if (planDeferralLimits != null) {
				uiVO.setMaxAutomaticIncrease(PlanData.formatPercentageFormatter(Double.parseDouble(planDeferralLimits.toString())));
			}
		}
        
        //Set Annual Increase Percent
        if (planInfoVO.getContributions().getPlanAutoContributionIncrease()
        		.getDefaultIncreasePercent() != null) {
			BigDecimal planDefaultIncrease = planInfoVO.getContributions().getPlanAutoContributionIncrease()
    		.getDefaultIncreasePercent();
			if (planDefaultIncrease != null) {
				uiVO.setAnnualIncrease(PlanData.formatPercentageFormatter(Double.parseDouble(planDefaultIncrease.toString())));
			}
		}         
        
        //load the plan employee deferral election months
        uiVO.setSelectedDeferralElectionMonths(planInfoVO.getContributions().getPlanEmployeeDeferralElection()
				.getEmployeeDeferralElectionSelectedMonths().toArray(new String[0]));        

		if (planInfoVO.getContributions().getPlanAutoContributionIncrease().getEffectiveDate() != null) {
			uiVO.setAciEffectiveDate(new SimpleDateFormat("MM/dd/yyyy").format(planInfoVO.getContributions().getPlanAutoContributionIncrease()
					.getEffectiveDate()));
		}
		if (planInfoVO.getContributions().getPlanAutoContributionIncrease().getNewParticipantApplyDate()!= null) {
			uiVO.setAciAppliesToEffectiveDate(new SimpleDateFormat("MM/dd/yyyy").format(
					planInfoVO.getContributions().getPlanAutoContributionIncrease().getNewParticipantApplyDate()));
		}
		if (planInfoVO.getContributions().getPlanAutoContributionIncrease().getAnnualApplyDate() != null) {
			uiVO.setAciApplyDate(planInfoVO.getContributions().getPlanAutoContributionIncrease()
					.getAnnualApplyDate().toString());
		}
		
		//set the selected employer money type for contribution formula section.
		List<PIFMoneyType>  permittedEmployerMoneyTypes = planInfoVO.getPifMoneyType().getPermittedEmployerMoneyTypes();
		List<PIFMoneyType> employerMoneyTypes = new ArrayList<PIFMoneyType>();
		for(PIFMoneyType pifMoneyType : permittedEmployerMoneyTypes){
			if(pifMoneyType.getSelectedIndicator()){
				employerMoneyTypes.add(pifMoneyType);
			}
		}
		if(employerMoneyTypes.size()>0){
			uiVO.setEmployerMoneyTypeList(employerMoneyTypes);
		}else{
			uiVO.setEmployerMoneyTypeList(null);
		}

		
		//convert the rules sets to encoded strings
		List<Integer> rules = new ArrayList<Integer>();
		Map<Integer, Map<String, String>> moneyTypesToRule = new HashMap<Integer, Map<String,String>>();
		String[] ruleSets = new String[100];
		
		//collect all the rules, money types and rules to money type arrays.
		for ( int i = 0 ;  i <  planInfoVO.getContributions().getPlanMoneyTypeEmployerContributions().size() ; i++  ) {
			PlanMoneyTypeEmployerContribution item = planInfoVO.getContributions().getPlanMoneyTypeEmployerContributions().get(i);
			rules.add(item.getRuleNumber());
			if ( moneyTypesToRule.get(item.getRuleNumber()) == null ) {
				moneyTypesToRule.put(item.getRuleNumber(), new HashMap<String,String>());
			}	
			moneyTypesToRule.get(item.getRuleNumber()).put(StringUtils.trim(item.getMoneyTypeId()), 
					Boolean.toString(item.getSelectedInd()));

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
		for (PlanContributionRule rule : planInfoVO.getContributions().getPlanContributionRules() ) {
			DecimalFormat format = new DecimalFormat("#.00");
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
				encodedString.append(rule.getEmployerContributionMaxMatchAmount()!= null ? format.format(rule
						.getEmployerContributionMaxMatchAmount().doubleValue())  : "").append(s1);					
			} else if ( rule.getEmployerContributionMaxMatchPercent() != null ) {
				encodedString.append(PlanContributionRule.TYPE_PERCENT).append(s1);
				encodedString.append(rule.getEmployerContributionMaxMatchPercent()!= null ? format.format(rule
						.getEmployerContributionMaxMatchPercent().doubleValue())  : "").append(s1);				
			} else {
				encodedString.append(s1);
				encodedString.append(s1);
			}
			encodedString.append(rule.getEmployerMatchContributionFirstPercent()!= null ? format.format(rule
					.getEmployerMatchContributionFirstPercent().doubleValue())  : "").append(s1);	
			encodedString.append(rule.getEmployerMatchContributionNextPercent()!= null ? format.format(rule
					.getEmployerMatchContributionNextPercent().doubleValue())  : "").append(s1);	

			if ( rule.getEmployerMatchFirstEmployeeDeferralAmount() != null || (rule.getFirstEmployeeDeferralType() != null && 
					StringUtils.equals(rule.getFirstEmployeeDeferralType(), PlanContributionRule.TYPE_DOLLAR)) ) {
				encodedString.append(PlanContributionRule.TYPE_DOLLAR).append(s1);
				encodedString.append(rule.getEmployerMatchFirstEmployeeDeferralAmount()!= null ? format.format(rule
						.getEmployerMatchFirstEmployeeDeferralAmount().doubleValue())  : "").append(s1);				
			} else if ( rule.getEmployerMatchFirstEmployeeDeferralPercent() != null || (rule.getFirstEmployeeDeferralType() != null && 
					StringUtils.equals(rule.getFirstEmployeeDeferralType(), PlanContributionRule.TYPE_PERCENT))) {
				encodedString.append(PlanContributionRule.TYPE_PERCENT).append(s1);
				encodedString.append(rule.getEmployerMatchFirstEmployeeDeferralPercent()!= null ? format.format(rule
						.getEmployerMatchFirstEmployeeDeferralPercent().doubleValue())  : "").append(s1);				
			} else {
				encodedString.append(s1);
				encodedString.append(s1);
			}

			if ( rule.getEmployerMatchNextEmployeeDeferralAmount() != null || (rule.getNextEmployeeDeferralType()) != null && 
					StringUtils.equals(rule.getNextEmployeeDeferralType(), PlanContributionRule.TYPE_DOLLAR)) {
				encodedString.append(PlanContributionRule.TYPE_DOLLAR).append(s1);
				encodedString.append(rule.getEmployerMatchNextEmployeeDeferralAmount()!= null ? format.format(rule
						.getEmployerMatchNextEmployeeDeferralAmount().doubleValue())  : "").append(s1);				
			} else if ( rule.getEmployerMatchNextEmployeeDeferralPercent() != null || (rule.getNextEmployeeDeferralType()) != null && 
					StringUtils.equals(rule.getNextEmployeeDeferralType(), PlanContributionRule.TYPE_PERCENT)) {
				encodedString.append(PlanContributionRule.TYPE_PERCENT).append(s1);
				encodedString.append(rule.getEmployerMatchNextEmployeeDeferralPercent()!= null ? format.format(rule
						.getEmployerMatchNextEmployeeDeferralPercent().doubleValue())  : "").append(s1);					
			} else {
				encodedString.append(s1);
				encodedString.append(s1);
			}

			DecimalFormat nonElectiveFormat = new DecimalFormat("######.##");
			encodedString.append(rule.getNonElectiveContributionPercent()!= null ? nonElectiveFormat.format(rule
					.getNonElectiveContributionPercent().doubleValue())  : "").append(s1);
			encodedString.append(rule.getRuleCode()).append(s1);
			encodedString.append(rule.getPermanentRuleNumber() != null ?  rule.getPermanentRuleNumber() : "");
			
			
			ruleSets[rule.getRuleNumber()-1] = encodedString.toString();
			
		}
		if(employerMoneyTypes.size()>0){
			uiVO.setEmployerRulesets(ruleSets);
		}else{
			uiVO.setEmployerRulesets(null);
		}

		ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();		
		try {
			uiVO.setViewableRules(delegate.getPlanContributionRulesDescriptions(
					planInfoVO.getContributions().getPlanContributionRules(), planInfoVO.getGeneralInformations()
							.getManulifeCompanyId()));
		} catch (SystemException e) {
			throw new RuntimeException(e);
		}	

	}

	/**
	 * method to update form bean to planInfoVO 
	 * @param PIFDataUi
	 * @param PlanInfoVO
	 */
	public void updateToBean(PIFDataUi uiVO, PlanInfoVO planInfoVO) {
		/*
		 * Clear selected day and months when OTHER is not selected
		 */
		if (!PlanEmployeeDeferralElection.OTHER.equals(planInfoVO.getContributions()
				.getPlanEmployeeDeferralElection().getEmployeeDeferralElectionCode())) {
			planInfoVO.getContributions().getPlanEmployeeDeferralElection().setEmployeeDeferralElectionSelectedMonths(
							new ArrayList<String>());
			planInfoVO.getContributions().getPlanEmployeeDeferralElection().setEmployeeDeferralElectionSelectedDay("");
		} else {
			// select deferral months
			planInfoVO.getContributions().getPlanEmployeeDeferralElection().setEmployeeDeferralElectionSelectedMonths(
							Arrays.asList(uiVO.getSelectedDeferralElectionMonths()));
		}		

		PlanDeferralLimits planDeferralLimits = planInfoVO.getContributions().getPlanDeferralLimits();
		if (planDeferralLimits == null) {
			planDeferralLimits = new PlanDeferralLimits();
			planInfoVO.getContributions().setPlanDeferralLimits(planDeferralLimits);
		}

		if (!StringUtils.isBlank(uiVO.getDeferralMaxPercent())) {
			planDeferralLimits.setDeferralMaxPercent(new BigDecimal(
					uiVO.getDeferralMaxPercent()).setScale(PlanDeferralLimits.PERCENTAGE_SCALE, RoundingMode.FLOOR));
		} else {
			planDeferralLimits.setDeferralMaxPercent(null);
		}

		if (!StringUtils.isBlank(uiVO.getDeferralMinPercent())) {
			planDeferralLimits.setDeferralMinPercent(new BigDecimal(
					uiVO.getDeferralMinPercent()).setScale(PlanDeferralLimits.PERCENTAGE_SCALE, RoundingMode.FLOOR));
		} else {
			planDeferralLimits.setDeferralMinPercent(null);
		}

		// clean deferral max amount
		if (planDeferralLimits.getDeferralIrsApplies() != null
				&& planDeferralLimits.getDeferralIrsApplies()) {
			planInfoVO.getContributions().getPlanDeferralLimits().setDeferralMaxAmount(null);
		}
		
		// clear ACI fields
		if (!PlanData.YES_CODE.equals(planInfoVO.getContributions().getAciAllowed())) {
			planInfoVO.getContributions().getPlanAutoContributionIncrease().setEffectiveDate(null);
			planInfoVO.getContributions().getPlanAutoContributionIncrease().setAppliesTo(null);
			planInfoVO.getContributions().getPlanAutoContributionIncrease()
					.setDefaultAutoIncreaseMaxPercent(null);
			planInfoVO.getContributions().getPlanAutoContributionIncrease()
					.setDefaultIncreasePercent(null);
			planInfoVO.getContributions().getPlanAutoContributionIncrease()
					.setEffectiveDate(null);
			planInfoVO.getContributions().getPlanAutoContributionIncrease()
					.setNewParticipantApplyDate(null);
			planInfoVO.getContributions().getPlanAutoContributionIncrease().setAnnualApplyDate(null);
			planInfoVO.getContributions().getPlanAutoContributionIncrease().setDefaultAutoIncreaseMaxPercent(null);	
			planInfoVO.getContributions().getPlanAutoContributionIncrease().setDefaultIncreasePercent(null);			
			// clear out values that were cleared from JavaScripts
			uiVO.setAciEffectiveDate("");
			uiVO.setAciAppliesToEffectiveDate("");
			uiVO.setAciApplyDate("");
			uiVO.setMaxAutomaticIncrease("");	
			uiVO.setAnnualIncrease("");				
		} else {
			try {
				SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yyyy");
				if (StringUtils.isNotEmpty(uiVO.getAciEffectiveDate())) {
					planInfoVO.getContributions().getPlanAutoContributionIncrease()
							.setEffectiveDate(
									sf.parse(uiVO.getAciEffectiveDate()));
				} else {
					planInfoVO.getContributions().getPlanAutoContributionIncrease()
							.setEffectiveDate(null);
				}
				if (StringUtils.isNotEmpty(uiVO.getAciAppliesToEffectiveDate())) {
					planInfoVO.getContributions().getPlanAutoContributionIncrease()
							.setNewParticipantApplyDate(
									sf.parse(uiVO.getAciAppliesToEffectiveDate()));
				} else {
					planInfoVO.getContributions().getPlanAutoContributionIncrease()
							.setNewParticipantApplyDate(null);
				}
				if (StringUtils.isNotEmpty(uiVO.getAciApplyDate())) {
					planInfoVO.getContributions().getPlanAutoContributionIncrease()
							.setAnnualApplyDate(
									new DayOfYear(uiVO.getAciApplyDate()));
				} else {
					planInfoVO.getContributions().getPlanAutoContributionIncrease()
							.setAnnualApplyDate(null);
				}
				//Set the Annual Increase and Maximum Auto Increase Percent
				planInfoVO.getContributions().getPlanAutoContributionIncrease()
				.setDefaultAutoIncreaseMaxPercent(StringUtils.isNotEmpty(uiVO.getMaxAutomaticIncrease()) ? 
					(new BigDecimal(uiVO.getMaxAutomaticIncrease()).setScale(PlanDeferralLimits.PERCENT_SCALE, RoundingMode.FLOOR)) : null);	
				planInfoVO.getContributions().getPlanAutoContributionIncrease()
				.setDefaultIncreasePercent(StringUtils.isNotEmpty(uiVO.getAnnualIncrease()) ? 
					(new BigDecimal(uiVO.getAnnualIncrease()).setScale(PlanDeferralLimits.PERCENT_SCALE, RoundingMode.FLOOR)) : null);				
			} catch (ParseException e) {
				// todo - do something here.
			}
		}		
		
		/*
		 * Clears the date field if not for New Participants.
		 */
		if (!PlanAutoContributionIncrease.APPLY_TO_PPT_NEW_PARTICIPANT_CODE
				.equals(planInfoVO.getContributions().getPlanAutoContributionIncrease()
						.getAppliesTo())) {
			planInfoVO.getContributions().getPlanAutoContributionIncrease().setNewParticipantApplyDate(null);
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
		Integer postedRuleSets = planInfoVO.getContributions().getContributionsFormulaCount();
		if(postedRuleSets!= null){
			DecimalFormat format = new DecimalFormat("#.00");
			List<PlanMoneyTypeEmployerContribution> contributionMoneyTypes = new ArrayList<PlanMoneyTypeEmployerContribution>();
			List<PlanMoneyTypeEmployerContribution> filteredContributionMoneyTypes = new ArrayList<PlanMoneyTypeEmployerContribution>();
			List<PlanContributionRule> rules = new ArrayList<PlanContributionRule>();		
			
			planInfoVO.getContributions().setPlanMoneyTypeEmployerContributions(filteredContributionMoneyTypes);
			planInfoVO.getContributions().setPlanContributionRules(rules);
			if(uiVO.getEmployerRulesets() != null){
				for ( String ruleset : uiVO.getEmployerRulesets() ) {
					if ( rules.size() >= postedRuleSets) continue;
					if ( ruleset == null ) continue;
					List<String> vals = Arrays.asList(ruleset.split(s1,13));
					List<String> moneyTypes = Arrays.asList(vals.get(1).split(s2));
					if(StringUtils.isEmpty(vals.get(1))) continue;
					for ( String moneyType : moneyTypes ) {
						if ( StringUtils.isEmpty(moneyType) ) continue;
						String[] moneyTypeMap = moneyType.split(s3);
						PlanMoneyTypeEmployerContribution contrib = new PlanMoneyTypeEmployerContribution();
						contrib.setRuleNumber(Integer.parseInt(vals.get(0)));
						if(vals.get(12) != null){
							contrib.setPermanentRuleNumber(StringUtils.isNotEmpty(vals.get(12)) ? Integer.parseInt(vals.get(12)) : null);					
						}else{
							contrib.setPermanentRuleNumber(null);
						}
						contrib.setMoneyTypeId(moneyTypeMap[0]);
						if(uiVO.getEmployerMoneyTypeList() != null){
							for(PIFMoneyType pifMoneyType : uiVO.getEmployerMoneyTypeList()){
								if(pifMoneyType.getSelectedIndicator()){
									if(StringUtils.trim(moneyTypeMap[0]).equals(StringUtils.trim(pifMoneyType.getMoneyTypeCode()))){					
										contrib.setShortName(pifMoneyType.getShortName());
									}					
								}				
							}							
						}

						contrib.setSelectedInd(StringUtils.equals(moneyTypeMap[1], Boolean.TRUE.toString()));
						contributionMoneyTypes.add(contrib);
					}
	
					PlanContributionRule rule = new PlanContributionRule();
					rule.setRuleNumber(Integer.parseInt(vals.get(0)));
					if ( StringUtils.equals(vals.get(2), PlanContributionRule.TYPE_PERCENT) ) {
						rule.setEmployerContributionMaxMatchPercent(StringUtils.isNotEmpty(vals.get(3)) ? 
								new BigDecimal(format.format(new BigDecimal(vals.get(3)).doubleValue())) : null);
					}
					if ( StringUtils.equals(vals.get(2), PlanContributionRule.TYPE_DOLLAR) ) {
						rule.setEmployerContributionMaxMatchAmount(StringUtils.isNotEmpty(vals.get(3)) ? 
								new BigDecimal(format.format(new BigDecimal(vals.get(3)).doubleValue())) : null);					
					}
					rule.setEmployerMatchContributionFirstPercent(StringUtils.isNotEmpty(vals.get(4)) ? 
							new BigDecimal(format.format(new BigDecimal(vals.get(4)).doubleValue())) : null);
					rule.setEmployerMatchContributionNextPercent(StringUtils.isNotEmpty(vals.get(5)) ? 
							new BigDecimal(format.format(new BigDecimal(vals.get(5)).doubleValue())) : null);
	
					if ( StringUtils.equals(vals.get(6), PlanContributionRule.TYPE_PERCENT) ) {
						rule.setEmployerMatchFirstEmployeeDeferralPercent(StringUtils.isNotEmpty(vals.get(7)) ? 
								new BigDecimal(format.format(new BigDecimal(vals.get(7)).doubleValue())) : null);					
					}
					if ( StringUtils.equals(vals.get(6), PlanContributionRule.TYPE_DOLLAR) ) {
						rule.setEmployerMatchFirstEmployeeDeferralAmount(StringUtils.isNotEmpty(vals.get(7)) ? 
								new BigDecimal(format.format(new BigDecimal(vals.get(7)).doubleValue())) : null);					
					}
					if ( StringUtils.equals(vals.get(8), PlanContributionRule.TYPE_PERCENT) ) {
						rule.setEmployerMatchNextEmployeeDeferralPercent(StringUtils.isNotEmpty(vals.get(9)) ? 
								new BigDecimal(format.format(new BigDecimal(vals.get(9)).doubleValue())) : null);					
					}
					if ( StringUtils.equals(vals.get(8), PlanContributionRule.TYPE_DOLLAR) ) {
						rule.setEmployerMatchNextEmployeeDeferralAmount(StringUtils.isNotEmpty(vals.get(9)) ? 
								new BigDecimal(format.format(new BigDecimal(vals.get(9)).doubleValue())) : null);					
					}
					DecimalFormat nonElectiveFormat = new DecimalFormat("######.##");
					rule.setNonElectiveContributionPercent(StringUtils.isNotEmpty(vals.get(10)) ? 
							new BigDecimal(nonElectiveFormat.format(new BigDecimal(vals.get(10)).doubleValue())) : null);				
					rule.setRuleCode(vals.get(11));
					if(vals.get(12) != null){
						rule.setPermanentRuleNumber(StringUtils.isNotEmpty(vals.get(12)) ? Integer.parseInt(vals.get(12)) : null);					
					}else{
						rule.setPermanentRuleNumber(null);
					}
					
					//add the ui fields needed for validation
					rule.setMaxMatchType(vals.get(2));
					rule.setFirstEmployeeDeferralType(vals.get(6));
					rule.setNextEmployeeDeferralType(vals.get(8));					
					rule.setMaxMatchValue(vals.get(3));
					rule.setFirstEmployeeDeferralValue(vals.get(7));
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
			}
		
		}

	}

	/**
	 * method to update the list of money types 
	 * @param List<PIFMoneyType>
	 */
    public void update(List<PIFMoneyType> list) {
		// Iterate through PIFMoneyType money types list  and set the corresponding selected indicator in appropriate Contributions VO 
		
	}


}
