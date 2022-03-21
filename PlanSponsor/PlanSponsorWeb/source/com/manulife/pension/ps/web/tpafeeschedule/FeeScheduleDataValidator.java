package com.manulife.pension.ps.web.tpafeeschedule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.validation.rules.generic.RegularExpressionRule;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.fee.DesignatedInvestmentManagerUi;
import com.manulife.pension.ps.web.fee.DesignatedInvestmentManagerUi.DesignatedInvestmentManagerUIFields;
import com.manulife.pension.ps.web.fee.FeeUIHolder;
import com.manulife.pension.ps.web.fee.PBAFeeUIHolder;
import com.manulife.pension.ps.web.fee.PBARestrictionUi;
import com.manulife.pension.ps.web.fee.PersonalBrokerageAccountUi;
import com.manulife.pension.ps.web.fee.PersonalBrokerageAccountUi.PersonalBrokerageAccountUIFields;
import com.manulife.pension.service.common.constants.Constants;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO.AmountType;
import com.manulife.pension.service.fee.valueobject.FeeDataVO;
import com.manulife.pension.util.Pair;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationRuleSet;

public class FeeScheduleDataValidator {
	
	
	private static final String DIM = "Designated Investment Manager";
	
	private static final String NON_TPA_FEE = "nonTpaFee";
	private static final String STANDARD_TPA_FEE = "tpaStandardFee";
	private static final String CUSTOMIZED_TPA_FEE = "tpaCustomizedFee";
	private static final String STANDARD_PBA_FEE= "pbaStandardFee";
	private static final String CUSTOMIZED_PBA_FEE = "pbaCustomizedFee";
	private static final String PBA_RESTRICTION ="pbaRestriction";
	
	private enum FeeCategory {
		Standard,
		Custom;
	};
	
	public enum Component{
		StandardFeeSchedule,
		CustomFeeSchedule,
		Notice404info;
	}
	
	private enum FeeScheduleErrorCode {
		FEE_VALUE_MUST_BE_NUMERIC(
				ErrorCodes.TPA_STANDARD_SCHEDULE_FEE_VALUE_MUST_BE_NUMERIC, 
				ErrorCodes.TPA_CUSTOM_SCHEDULE_FEE_VALUE_MUST_BE_NUMERIC,
				ErrorCodes.NOTICE_INFO_FEE_VALUE_MUST_BE_NUMERIC), 
		CUSTOM_FEE_VALUE_INCOMPLETE(
				ErrorCodes.TPA_STANDARD_SCHEDULE_CUSTOM_FEE_VALUE_INCOMPLETE,
				ErrorCodes.TPA_CUSTOM_SCHEDULE_CUSTOM_FEE_VALUE_INCOMPLETE,
				ErrorCodes.NOTICE_INFO_CUSTOM_FEE_VALUE_INCOMPLETE), 
		CUSTOM_FEE_TYPE_INCOMPLETE(
				ErrorCodes.TPA_STANDARD_SCHEDULE_CUSTOM_FEE_TYPE_INCOMPLETE,
				ErrorCodes.TPA_CUSTOM_SCHEDULE_CUSTOM_FEE_TYPE_INCOMPLETE,
				ErrorCodes.NOTICE_INFO_CUSTOM_FEE_TYPE_INCOMPLETE), 
		CUSTOM_FEE_TYPE_AND_VALUE_INCOMPLETE(
				ErrorCodes.TPA_STANDARD_SCHEDULE_FEE_TYPE_AND_VALUE_INCOMPLETE,
				ErrorCodes.TPA_CUSTOM_SCHEDULE_FEE_TYPE_AND_VALUE_INCOMPLETE,
				ErrorCodes.NOTICE_INFO_CUSTOM_FEE_TYPE_AND_VALUE_INCOMPLETE), 
		NEGATIVE_FEE_VALUE(
				ErrorCodes.TPA_STANDARD_SCHEDULE_NEGATIVE_FEE_VALUE,
				ErrorCodes.TPA_CUSTOM_SCHEDULE_NEGATIVE_FEE_VALUE,
				ErrorCodes.NOTICE_INFO_NEGATIVE_FEE_VALUE), 
		INVALID_FEE_AMOUNT_FORMAT(
				ErrorCodes.TPA_STANDARD_SCHEDULE_INVALID_FEE_AMOUNT_FORMAT,
				ErrorCodes.TPA_CUSTOM_SCHEDULE_INVALID_FEE_AMOUNT_FORMAT,
				ErrorCodes.NOTICE_INFO_INVALID_FEE_AMOUNT_FORMAT), 
		INVALID_FEE_PERCENTAGE_FORMAT(
				ErrorCodes.TPA_STANDARD_SCHEDULE_INVALID_FEE_PERCENTAGE_FORMAT,
				ErrorCodes.TPA_CUSTOM_SCHEDULE_INVALID_FEE_PERCENTAGE_FORMAT,
				ErrorCodes.NOTICE_INFO_INVALID_FEE_PERCENTAGE_FORMAT), 
		INVALID_FEE_VALUE_WITH_SPECIAL_NOTE(
				ErrorCodes.TPA_STANDARD_SCHEDULE_INVALID_FEE_VALUE_WITH_SPECIAL_NOTE,
				ErrorCodes.TPA_CUSTOM_SCHEDULE_INVALID_FEE_VALUE_WITH_SPECIAL_NOTE,
				ErrorCodes.NOTICE_INFO_INVALID_FEE_VALUE_WITH_SPECIAL_NOTE);

		int StandardErrorCode;
		int noticeInfoErrorCode;
		int CustomErrorCode;

		FeeScheduleErrorCode(int StandardErrorCode, int CustomErrorCode,
				int noticeInfoErrorCode) {
			this.StandardErrorCode = StandardErrorCode;
			this.CustomErrorCode = CustomErrorCode;
			this.noticeInfoErrorCode = noticeInfoErrorCode;
		}

		public int getErrorCode(Component comp) throws SystemException {
			switch (comp) {
			case Notice404info:
				return noticeInfoErrorCode;
			case StandardFeeSchedule:
				return StandardErrorCode;
			case CustomFeeSchedule:
				return CustomErrorCode;
			default:
				throw new SystemException("Invalid FeeSchedule Comp:" + comp);
			}
		}
	}
	
	private interface FeeScheduleData{
		boolean getDeleted();
		boolean isEmpty();
		String getFeeDescription();
		String getFeeValue();
		String getNotes();
		String getFeeValueType();
		boolean getFeeDisabled();
	}
	
	private interface PbaFeeScheduleData{
		boolean getDeleted();
		boolean isEmpty();
		String getFeeDescription();
		String getFeeValue();
		String getFeeValueType();		
	}
	
	

	public static ArrayList<GenericException> validateStandardScheduleFees(
			TpaStandardFeeScheduleForm form) throws SystemException {

		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();

		// validate TPA standard fee
		validateFees(errorMessages, STANDARD_TPA_FEE,
				getFeeSceduleDataListFromStandardSchedule(form
						.getFeeLabelList()), FeeCategory.Standard,
				Component.StandardFeeSchedule);

		// validate TPA customized fee
		validateFees(errorMessages, CUSTOMIZED_TPA_FEE,
				getFeeSceduleDataListFromStandardSchedule(form
						.getAdditionalFeeItemsList()), FeeCategory.Custom,
				Component.StandardFeeSchedule);

		return errorMessages;

	}
	
	public static ArrayList<GenericException> validateCustomContractScheduleFees(
			BaseFeeScheduleForm form) throws SystemException {
		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		
		validateCommon(errorMessages, form, Component.CustomFeeSchedule);
		
		return errorMessages;
		
	}
	
	public static ArrayList<GenericException> validate404a5NoticeInfoFees(
			BaseFeeScheduleForm form) throws SystemException {
		ArrayList<GenericException> errorMessages = new ArrayList<GenericException>();
		
		// validate DIM section
		validateDesignatedInvestmentManagerInfo(form
				.getDesignatedInvestmentManagerUi(), errorMessages);	
		
		//Check if all the PBA details are empty. IF empty, ignoring the individual field validation
		if(checkIfPBADetailsAreAvailable(form.getPersonalBrokerageAccountUi(), form.getStandardPBAFees(), form.getCustomPBAFees())){
			//validate PBA section
			validatePersonalBrokerageAccount(form.getPersonalBrokerageAccountUi(), form.getPbaRestrictionList(), errorMessages);
		}

		// validate non TPA customized fee
		validateFees(errorMessages, NON_TPA_FEE, getFeeSceduleDataList(form
				.getNonTpaFees()), FeeCategory.Custom, Component.Notice404info);
		
		validateCommon(errorMessages, form, Component.Notice404info);
		
		return errorMessages;
		
	}
	
	public static boolean checkIfPBADetailsAreAvailable(PersonalBrokerageAccountUi pbaDetails, List<PBAFeeUIHolder> standardFees, List<PBAFeeUIHolder> customFees){
		boolean completedStandardFee = false;
		boolean isPBADetailsAvailable = true;
		if(standardFees!=null && standardFees.size()>0){
			nextStdFee : for(PBAFeeUIHolder fee: standardFees){
				if(StringUtils.isEmpty(fee.getFeeValue())){
					continue nextStdFee;
				}
				else if(isNumeric(fee.getFeeValue()) && Double.parseDouble(fee.getFeeValue())==0.0d){
					continue nextStdFee;
				}
				else {
					completedStandardFee = true;
				}
			}
		}
		
		if(pbaDetails.getValueEmpty() && !completedStandardFee && (customFees!=null && customFees.size()==0)){
			isPBADetailsAvailable = false;
		}		
		return isPBADetailsAvailable;
	}
		
	public static ArrayList<GenericException> validateCommon(
			ArrayList<GenericException> errorMessages, BaseFeeScheduleForm form, Component comp) throws SystemException {
		
		// validate TPA standard fee
		validateFees(errorMessages, STANDARD_TPA_FEE,
				getFeeSceduleDataList(form.getTpaFeesStandard()),
				FeeCategory.Standard, comp);

		// validate TPA customized fee
		validateFees(errorMessages, CUSTOMIZED_TPA_FEE,
				getFeeSceduleDataList(form.getTpaFeesCustomized()),
				FeeCategory.Custom, comp);
		
		if(!(form instanceof TPAContractFeeScheduleForm)){
			//Check if all the PBA details are empty. IF empty, ignoring the individual field validation
			if(checkIfPBADetailsAreAvailable(form.getPersonalBrokerageAccountUi(), form.getStandardPBAFees(), form.getCustomPBAFees())){
				checkForAnyCompletedPBAFee(form.getStandardPBAFees(), form.getCustomPBAFees(), errorMessages);

				// validate PBA standard fee
				validatePBAFees(errorMessages, STANDARD_PBA_FEE,
						getPBAFeeSceduleDataList(form.getStandardPBAFees()),
						FeeCategory.Standard, comp);		

				//validate PBA customized fee
				validatePBAFees(errorMessages, CUSTOMIZED_PBA_FEE,
						getPBAFeeSceduleDataList(form.getCustomPBAFees()),
						FeeCategory.Custom, comp);
			}
		}

		return errorMessages;
	}
	
	private static void checkForAnyCompletedPBAFee(List<PBAFeeUIHolder> standardFees, List<PBAFeeUIHolder> customFees, ArrayList<GenericException> errorMessages){
		boolean completedStandardFee = false;
		boolean completedCustomFee = false;
		if(standardFees!=null && standardFees.size()>0){
			nextStdFee : for(PBAFeeUIHolder fee: standardFees){
				if(StringUtils.isEmpty(fee.getFeeValue())){
					continue nextStdFee;
				}
				else if(isNumeric(fee.getFeeValue()) && Double.parseDouble(fee.getFeeValue())==0.0d){
					continue nextStdFee;
				}
				else {
					completedStandardFee = true;
				}
			}
		}
		
		if(customFees!=null && customFees.size()>0){
			nextCustomFee : for(PBAFeeUIHolder fee: customFees){
				if(fee.getDeleted() || StringUtils.isBlank(fee.getFeeDescription()) || (StringUtils.isEmpty(fee.getFeeValue()) || (isNumeric(fee.getFeeValue()) && Double.parseDouble(fee.getFeeValue())==0.0d))){
					continue nextCustomFee;
				}
				else{
					completedCustomFee = true;
				}
			}
		}
		
		if(!(completedStandardFee || completedCustomFee)){			
			errorMessages.add(new ValidationError("pbaFee", ErrorCodes.MANDATORY_FIELDS_INVALID));
		}
	}
	
	private static void validatePersonalBrokerageAccount(PersonalBrokerageAccountUi pbaDetails, List<PBARestrictionUi> restrictions, ArrayList<GenericException> errorMessages){		
			//PBA Provider name
			if(StringUtils.isBlank(pbaDetails.getPbaProviderName())) {
				errorMessages.add(new ValidationError(
						    PersonalBrokerageAccountUIFields.pbaProviderName.name(),
							ErrorCodes.MANDATORY_FIELDS_INVALID));
			} else if (!StringUtils.isAsciiPrintable(pbaDetails.getPbaProviderName())){
				errorMessages.add(new ValidationError(
						PersonalBrokerageAccountUIFields.pbaProviderName.name(),
						ErrorCodes.INVALID_CHARACTERS_DETECTED));
			}
			
			//PBA Phone number
			if(StringUtils.isBlank(pbaDetails.getPbaPhonePrefix())
					|| StringUtils.isBlank(pbaDetails.getPbaPhoneAreaCode())
					|| StringUtils.isBlank(pbaDetails.getPbaPhoneNumber())) {
				errorMessages.add(new ValidationError(
						PersonalBrokerageAccountUIFields.pbaPhone.name(),
							ErrorCodes.MANDATORY_FIELDS_INVALID));
			}
			
			if (StringUtils.isNotEmpty(pbaDetails.getPbaPhone())) {
				if (!StringUtils.isNumeric(pbaDetails.getPbaPhonePrefix())
						|| !StringUtils.isNumeric(pbaDetails.getPbaPhoneAreaCode())
						|| !StringUtils.isNumeric(pbaDetails.getPbaPhoneNumber())) {
					errorMessages.add(new ValidationError(
							PersonalBrokerageAccountUIFields.pbaPhone.name(),
							ErrorCodes.PHONE_NUMBER_INVALID));
				}
				
				if (StringUtils.isEmpty(pbaDetails.getPbaPhonePrefix())
						|| StringUtils.isEmpty(pbaDetails.getPbaPhoneAreaCode())
						|| StringUtils.isEmpty(pbaDetails.getPbaPhoneNumber())) {
					if (StringUtils.isNotEmpty(pbaDetails.getPbaPhoneExt())) {
						errorMessages.add(new ValidationError(PersonalBrokerageAccountUIFields.pbaPhone.name(),
								ErrorCodes.PHONE_NUMBER_INCOMPLETE_1));
					} else {
						errorMessages.add(new ValidationError(PersonalBrokerageAccountUIFields.pbaPhone.name(),
								ErrorCodes.PHONE_NUMBER_INCOMPLETE_2));
					}
					
				}
			} else {
				if (StringUtils.isNotEmpty(pbaDetails.getPbaPhoneExt())) {
					errorMessages.add(new ValidationError(
							PersonalBrokerageAccountUIFields.pbaPhone.name(),
							ErrorCodes.PHONE_NUMBER_INCOMPLETE_1));
				}
			}
			
			if (!StringUtils.isNumeric(pbaDetails.getPbaPhoneExt())) {
				errorMessages.add(new ValidationError(
						PersonalBrokerageAccountUIFields.pbaPhone.name(),
						ErrorCodes.PHONE_EXTENSION_INVALID));
			}
			
			//PBA minimum deposit
			if(StringUtils.isBlank(pbaDetails.getPbaMinDeposit())){
				errorMessages.add(new ValidationError(PersonalBrokerageAccountUIFields.pbaMinDeposit.name(),
							ErrorCodes.MANDATORY_FIELDS_INVALID));
			}
			else if(!isNumeric(pbaDetails.getPbaMinDeposit())){
				errorMessages.add(new ValidationError(PersonalBrokerageAccountUIFields.pbaMinDeposit.name(),
						ErrorCodes.INVALID_PBA_MIN_DEPOSIT));	
				//System.out.println("Non numeric");
			}			
			else if(BigDecimal.ZERO.compareTo(new BigDecimal(Double.parseDouble(pbaDetails.getPbaMinDeposit()))) == 0){
				errorMessages.add(new ValidationError(PersonalBrokerageAccountUIFields.pbaMinDeposit.name(),
						ErrorCodes.MANDATORY_FIELDS_INVALID));
				//System.out.println("zero value");
			}
			else if(BigDecimal.ZERO.compareTo(new BigDecimal(Double.parseDouble(pbaDetails.getPbaMinDeposit()))) > 0){
				errorMessages.add(new ValidationError(PersonalBrokerageAccountUIFields.pbaMinDeposit.name(),
						ErrorCodes.INVALID_PBA_MIN_DEPOSIT));
				//System.out.println("-ve value");
			}
			else if(!isValidMinDepositValue(pbaDetails.getPbaMinDeposit())){
				errorMessages.add(new ValidationError(PersonalBrokerageAccountUIFields.pbaMinDeposit.name(),
						ErrorCodes.INVALID_PBA_MIN_DEPOSIT));	
				//System.out.println("invalid format");
			}
			
			//PBA Email address
			if (StringUtils.isNotEmpty(pbaDetails.getPbaEmailAddress())) {
				ValidationRuleSet rule = new ValidationRuleSet();
				rule.addRule(new RegularExpressionRule(ErrorCodes.EMAIL_ADDRESS_INVALID,
						"^[a-zA-Z0-9\\-\\._']+@([a-zA-Z0-9\\-]+\\.)+[a-zA-Z0-9]+$"));
				rule. validate(
						PersonalBrokerageAccountUIFields.pbaEmailAddress.name(), errorMessages,
						pbaDetails.getPbaEmailAddress());
				if (pbaDetails.getPbaEmailAddress().contains("@jhancock")
						|| pbaDetails.getPbaEmailAddress().contains("@manulife")) {
					errorMessages.add(new ValidationError(PersonalBrokerageAccountUIFields.pbaEmailAddress.name(),
									ErrorCodes.EMAIL_ADDRESS_DOMAIN_INVALID));
				}
				if (!StringUtils.isAsciiPrintable(pbaDetails.getPbaEmailAddress())){
					errorMessages.add(new ValidationError(
							PersonalBrokerageAccountUIFields.pbaEmailAddress.name(),
							ErrorCodes.INVALID_CHARACTERS_DETECTED));
				}
			}
		
		if(Constants.Y.equalsIgnoreCase(pbaDetails.getPbaRestriction())){
			if(restrictions ==null || restrictions.size()==0 || !checkForAnyCompletedRestriction(restrictions)){
				errorMessages.add(new ValidationError(PersonalBrokerageAccountUIFields.pbaRestriction.name(),
						ErrorCodes.ERROR_PBA_RESTRICTION_REQUIRED));
			}
			else if(restrictions !=null && restrictions.size()>0){				
				validatePbaRestrictions(restrictions, PBA_RESTRICTION, errorMessages);				
			}
		}
	}
	
	private static boolean checkForAnyCompletedRestriction(List<PBARestrictionUi> restrictions){		
		for(PBARestrictionUi res: restrictions){
			if(StringUtils.isNotEmpty(res.getRestrictionDesc()) && !res.isDeletedInd()){
				return true;
			}
		}
		return false;
	}
	
	private static void validatePbaRestrictions(List<PBARestrictionUi> restrictions, String resName, ArrayList<GenericException> errorMessages){
		int count = -1;
		nextRes : for(PBARestrictionUi res: restrictions){
			count++;
			if(res.isEmpty() || res.isDeletedInd()) {
				continue nextRes;
			}
			if(StringUtils.isNotEmpty(res.getRestrictionDesc())){
				if (!StringUtils.isAsciiPrintable(res.getRestrictionDesc())){
					errorMessages.add(new ValidationError(resName + count,
							ErrorCodes.INVALID_CHARACTERS_DETECTED));
					continue nextRes;
				}
			}			
		}
	}
	
	private static void  validateDesignatedInvestmentManagerInfo(DesignatedInvestmentManagerUi designatedInvestmentManager,
			ArrayList<GenericException> errorMessages) {
		
		if(!designatedInvestmentManager.getValueEmpty()) {
			
			if(StringUtils.isBlank(designatedInvestmentManager.getFirstName())) {
				errorMessages.add(new ValidationError(
						    DesignatedInvestmentManagerUIFields.firstName.name(),
							ErrorCodes.MANDATORY_FIELDS_INVALID));
			} else if (!StringUtils.isAsciiPrintable(designatedInvestmentManager.getFirstName())){
				errorMessages.add(new ValidationError(
					    DesignatedInvestmentManagerUIFields.firstName.name(),
						ErrorCodes.INVALID_CHARACTERS_DETECTED));
			}
			
			if(StringUtils.isBlank(designatedInvestmentManager.getLastName())) {
				errorMessages.add(new ValidationError(
						    DesignatedInvestmentManagerUIFields.lastName.name(),
							ErrorCodes.MANDATORY_FIELDS_INVALID));
			} else if (!StringUtils.isAsciiPrintable(designatedInvestmentManager.getLastName())){
				errorMessages.add(new ValidationError(
					    DesignatedInvestmentManagerUIFields.lastName.name(),
						ErrorCodes.INVALID_CHARACTERS_DETECTED));
			}
			
			if (!StringUtils.isAsciiPrintable(designatedInvestmentManager.getCompany())){
				errorMessages.add(new ValidationError(
					    DesignatedInvestmentManagerUIFields.company.name(),
						ErrorCodes.INVALID_CHARACTERS_DETECTED));
			}
			
			if(StringUtils.isBlank(designatedInvestmentManager.getAddressLine1())) {
				errorMessages.add(new ValidationError(
						    DesignatedInvestmentManagerUIFields.addressLine1.name(),
							ErrorCodes.MANDATORY_FIELDS_INVALID));
			}else if (!StringUtils.isAsciiPrintable(designatedInvestmentManager.getAddressLine1())){
				errorMessages.add(new ValidationError(
					    DesignatedInvestmentManagerUIFields.addressLine1.name(),
						ErrorCodes.INVALID_CHARACTERS_DETECTED));
			}
			
			
			if (!StringUtils.isAsciiPrintable(designatedInvestmentManager.getAddressLine2())){
				errorMessages.add(new ValidationError(
					    DesignatedInvestmentManagerUIFields.addressLine2.name(),
						ErrorCodes.INVALID_CHARACTERS_DETECTED));
			}
			
			if(StringUtils.isBlank(designatedInvestmentManager.getCity())) {
				errorMessages.add(new ValidationError(
						    DesignatedInvestmentManagerUIFields.city.name(),
							ErrorCodes.MANDATORY_FIELDS_INVALID));
			}else if (!StringUtils.isAsciiPrintable(designatedInvestmentManager.getCity())){
				errorMessages.add(new ValidationError(
					    DesignatedInvestmentManagerUIFields.city.name(),
						ErrorCodes.INVALID_CHARACTERS_DETECTED));
			}
			
			if(StringUtils.isBlank(designatedInvestmentManager.getState())) {
				errorMessages.add(new ValidationError(
						    DesignatedInvestmentManagerUIFields.state.name(),
							ErrorCodes.MANDATORY_FIELDS_INVALID));
			}
			
			if(StringUtils.isBlank(designatedInvestmentManager.getZipCode())) {
				errorMessages.add(new ValidationError(
						    DesignatedInvestmentManagerUIFields.zipCode.name(),
							ErrorCodes.MANDATORY_FIELDS_INVALID));
			}
			
			if(StringUtils.isBlank(designatedInvestmentManager.getPhonePrefix())
					|| StringUtils.isBlank(designatedInvestmentManager.getPhoneAreaCode())
					|| StringUtils.isBlank(designatedInvestmentManager.getPhoneNumber())) {
				errorMessages.add(new ValidationError(
						    DesignatedInvestmentManagerUIFields.phone.name(),
							ErrorCodes.MANDATORY_FIELDS_INVALID));
			}
			
			if (StringUtils.isNotEmpty(designatedInvestmentManager.getPhone())) {
				if (!StringUtils.isNumeric(designatedInvestmentManager.getPhonePrefix())
						|| !StringUtils.isNumeric(designatedInvestmentManager.getPhoneAreaCode())
						|| !StringUtils.isNumeric(designatedInvestmentManager.getPhoneNumber())) {
					errorMessages.add(new ValidationError(
							DesignatedInvestmentManagerUIFields.phone.name(),
							ErrorCodes.PHONE_NUMBER_INVALID));
				}
				
				if (StringUtils.isEmpty(designatedInvestmentManager.getPhonePrefix())
						|| StringUtils.isEmpty(designatedInvestmentManager.getPhoneAreaCode())
						|| StringUtils.isEmpty(designatedInvestmentManager.getPhoneNumber())) {
					if (StringUtils.isNotEmpty(designatedInvestmentManager.getPhoneExt())) {
						errorMessages.add(new ValidationError(DesignatedInvestmentManagerUIFields.phone.name(),
								ErrorCodes.PHONE_NUMBER_INCOMPLETE_1));
					} else {
						errorMessages.add(new ValidationError(DesignatedInvestmentManagerUIFields.phone.name(),
								ErrorCodes.PHONE_NUMBER_INCOMPLETE_2));
					}
					
				}
			} else {
				if (StringUtils.isNotEmpty(designatedInvestmentManager.getPhoneExt())) {
					errorMessages.add(new ValidationError(
							DesignatedInvestmentManagerUIFields.phone.name(),
							ErrorCodes.PHONE_NUMBER_INCOMPLETE_1));
				}
			}
			
			if (!StringUtils.isNumeric(designatedInvestmentManager.getPhoneExt())) {
				errorMessages.add(new ValidationError(
						DesignatedInvestmentManagerUIFields.phone.name(),
						ErrorCodes.PHONE_EXTENSION_INVALID));
			}
			
			
			if (!StringUtils.isNumeric(designatedInvestmentManager.getZipCode1())
					|| !StringUtils.isNumeric(designatedInvestmentManager.getZipCode2())) {
				errorMessages.add(new ValidationError(
						DesignatedInvestmentManagerUIFields.zipCode.name(),
						ErrorCodes.ZIP_CODE_NOT_NUMERIC,
						new String[]{DIM}));
			} else { 
				if (StringUtils.isNotEmpty(designatedInvestmentManager.getZipCode())
						&& StringUtils.isNotEmpty(designatedInvestmentManager.getState())) {
					validateZipCode(errorMessages, 
							designatedInvestmentManager.getZipCodeValue(), 
							designatedInvestmentManager.getState());
				}
			}
			
			// email address validation
			if (StringUtils.isNotEmpty(designatedInvestmentManager
					.getEmailAddress())) {
				ValidationRuleSet rule = new ValidationRuleSet();
				rule.addRule(new RegularExpressionRule(ErrorCodes.EMAIL_ADDRESS_INVALID,
						"^[a-zA-Z0-9\\-\\._']+@([a-zA-Z0-9\\-]+\\.)+[a-zA-Z0-9]+$"));
				rule. validate(
						DesignatedInvestmentManagerUIFields.emailAddress.name(), errorMessages,
						designatedInvestmentManager.getEmailAddress());
				if (designatedInvestmentManager.getEmailAddress().contains("@jhancock")
						|| designatedInvestmentManager.getEmailAddress().contains("@manulife")) {
					errorMessages.add(new ValidationError(DesignatedInvestmentManagerUIFields.emailAddress.name(),
									ErrorCodes.EMAIL_ADDRESS_DOMAIN_INVALID));
				}
			}
			
			if (!StringUtils.isAsciiPrintable(designatedInvestmentManager.getSpecialNotes())){
				errorMessages.add(new ValidationError(
					    DesignatedInvestmentManagerUIFields.specialNotes.name(),
						ErrorCodes.INVALID_CHARACTERS_DETECTED));
			}
		}
	}
	
	/**
	 * 
	 * @param errorMessages
	 * @param feeType
	 * @param fees
	 * @param feeCategory
	 */
	private static void validateFees(ArrayList<GenericException> errorMessages ,
			String feeType , List<FeeScheduleData> fees, FeeCategory feeCategory, Component comp) throws SystemException {
		
		int count = -1;
		
		nextFee : for (FeeScheduleData fee : fees) {
			
			count ++;
			double amount = 0.0d;
			
			if(fee.isEmpty() || fee.getDeleted() || fee.getFeeDisabled()) {
				continue nextFee;
			}	
			
			if (StringUtils.isNotEmpty(fee.getFeeValue())) {
				try {
					amount = Double.parseDouble(fee.getFeeValue());
				} catch (NumberFormatException e) {
					errorMessages.add(new ValidationError(feeType + count,
							FeeScheduleErrorCode.FEE_VALUE_MUST_BE_NUMERIC.getErrorCode(comp)));
					continue nextFee;
				}
			}
			
			// custom fee type additional validation
			if(feeCategory.equals(FeeCategory.Custom)) {
				
				if (StringUtils.isNotEmpty(fee.getFeeDescription())) {
					if (StringUtils.isEmpty(fee.getFeeValue()) || amount == 0.0) {
						errorMessages.add(new ValidationError(feeType + count,
								FeeScheduleErrorCode.CUSTOM_FEE_VALUE_INCOMPLETE.getErrorCode(comp)));
						continue nextFee;
					}
				}
				
				if (amount != 0.0d) {
					if (StringUtils.isEmpty(fee.getFeeDescription())) {
						errorMessages.add(new ValidationError(feeType + count,
								FeeScheduleErrorCode.CUSTOM_FEE_TYPE_INCOMPLETE.getErrorCode(comp)));
						continue nextFee;
					}
				}
				
				if (StringUtils.isNotEmpty(fee.getNotes())) {
					if (StringUtils.isEmpty(fee.getFeeDescription()) && amount == 0.0d) {
						errorMessages.add(new ValidationError(feeType + count,
								FeeScheduleErrorCode.CUSTOM_FEE_TYPE_AND_VALUE_INCOMPLETE.getErrorCode(comp)));
						continue nextFee;
					}
				}
				
			} else {
				if (StringUtils.isNotEmpty(fee.getNotes())) {
					if (amount <= 0.0d) {
						errorMessages.add(new ValidationError(feeType + count,
								FeeScheduleErrorCode.INVALID_FEE_VALUE_WITH_SPECIAL_NOTE.getErrorCode(comp)));
						continue nextFee;
					}
				}
			}
			
			if ( amount < 0.0) {
				errorMessages.add(new ValidationError(feeType + count,
						FeeScheduleErrorCode.NEGATIVE_FEE_VALUE.getErrorCode(comp)));
				continue nextFee;
			}
			
			
			if (StringUtils.equals(AmountType.DOLLAR.getCode(), fee.getFeeValueType()) ) {
				if (!isValidFeeValueForDollar(String.valueOf(amount))) {
					errorMessages.add(new ValidationError(feeType + count,
							FeeScheduleErrorCode.INVALID_FEE_AMOUNT_FORMAT.getErrorCode(comp)));
					continue nextFee;
				}
			}
			
			if (StringUtils.equals(AmountType.PERCETAGE.getCode(), fee.getFeeValueType()) ) {
				if (!isValidFeeValueForPercentage(String.valueOf(amount))) {
					errorMessages.add(new ValidationError(feeType + count,
							FeeScheduleErrorCode.INVALID_FEE_PERCENTAGE_FORMAT.getErrorCode(comp)));
					continue nextFee;
				}
			}
			
			
			if (!StringUtils.isAsciiPrintable(fee.getNotes()) ||
					!StringUtils.isAsciiPrintable(fee.getFeeDescription())) {
				errorMessages.add(new ValidationError(feeType + count,
						ErrorCodes.INVALID_CHARACTERS_DETECTED));
				continue nextFee;
			}
			
		}		
	}
	
	/**
	 * 
	 * @param errorMessages
	 * @param feeType
	 * @param fees
	 * @param feeCategory
	 */
	private static void validatePBAFees(ArrayList<GenericException> errorMessages ,
			String feeType , List<PbaFeeScheduleData> fees, FeeCategory feeCategory, Component comp) throws SystemException {
		
		int count = -1;
		
		nextFee : for (PbaFeeScheduleData fee : fees) {
			
			count ++;
			double amount = 0.0d;
			
			if(fee.isEmpty() || fee.getDeleted()) {
				continue nextFee;
			}	
			
			if (StringUtils.isNotEmpty(fee.getFeeValue())) {
				try {
					amount = Double.parseDouble(fee.getFeeValue());
				} catch (NumberFormatException e) {
					errorMessages.add(new ValidationError(feeType + count,
							FeeScheduleErrorCode.FEE_VALUE_MUST_BE_NUMERIC.getErrorCode(comp)));
					continue nextFee;
				}
			}
			
			// custom fee type additional validation
			if(feeCategory.equals(FeeCategory.Custom)) {
				
				if (StringUtils.isNotEmpty(fee.getFeeDescription())) {
					if (StringUtils.isEmpty(fee.getFeeValue()) || amount == 0.0) {
						errorMessages.add(new ValidationError(feeType + count,
								FeeScheduleErrorCode.CUSTOM_FEE_VALUE_INCOMPLETE.getErrorCode(comp)));
						continue nextFee;
					}
				}
				
				if (amount != 0.0d) {
					if (StringUtils.isEmpty(fee.getFeeDescription())) {
						errorMessages.add(new ValidationError(feeType + count,
								FeeScheduleErrorCode.CUSTOM_FEE_TYPE_INCOMPLETE.getErrorCode(comp)));
						continue nextFee;
					}
				}
				
				if (!StringUtils.isAsciiPrintable(fee.getFeeDescription())) {
					errorMessages.add(new ValidationError(feeType + count,
							ErrorCodes.INVALID_CHARACTERS_DETECTED));
					continue nextFee;
				}
								
			} 
			
			if ( amount < 0.0) {
				errorMessages.add(new ValidationError(feeType + count,
						FeeScheduleErrorCode.NEGATIVE_FEE_VALUE.getErrorCode(comp)));
				continue nextFee;
			}
			
			
			if (StringUtils.equals(PBAFeeUIHolder.DOLLAR, fee.getFeeValueType()) ) {
				if (!isValidFeeValueForDollar(String.valueOf(amount))) {
					errorMessages.add(new ValidationError(feeType + count,
							FeeScheduleErrorCode.INVALID_FEE_AMOUNT_FORMAT.getErrorCode(comp)));
					continue nextFee;
				}
			}
			
			if (StringUtils.equals(PBAFeeUIHolder.PERCENTAGE, fee.getFeeValueType()) ) {
				if (!isValidFeeValueForPercentage(String.valueOf(amount))) {
					errorMessages.add(new ValidationError(feeType + count,
							FeeScheduleErrorCode.INVALID_FEE_PERCENTAGE_FORMAT.getErrorCode(comp)));
					continue nextFee;
				}
			}
			
			
			
			
		}		
	}
	
	private static List<FeeScheduleData> getFeeSceduleDataList(List<FeeUIHolder> fees){	
		List<FeeScheduleData> feeDataList = new ArrayList<FeeScheduleData> ();
		for(final FeeUIHolder feeUi : fees){
			feeDataList.add(new FeeScheduleData(){
				public boolean getDeleted(){
					return feeUi.getDeleted();
				}
				public boolean isEmpty(){
					return feeUi.isEmpty();
				}
				public String getFeeDescription(){
					return feeUi.getFeeDescription();
				}
				public String getFeeValue(){
					return feeUi.getNonFormattedAmountValue();
				}
				public String getNotes(){
					return feeUi.getNotes();
				}
				public String getFeeValueType(){
					return feeUi.getFeeValueType();
				}
				public boolean getFeeDisabled(){
					return feeUi.isDisabled();
				}
			});
		}	
		return feeDataList;		
	}
	
	
	private static List<PbaFeeScheduleData> getPBAFeeSceduleDataList(List<PBAFeeUIHolder> fees){	
		List<PbaFeeScheduleData> feeDataList = new ArrayList<PbaFeeScheduleData> ();
		for(final PBAFeeUIHolder feeUi : fees){
			feeDataList.add(new PbaFeeScheduleData(){
				public boolean getDeleted(){
					return feeUi.getDeleted();
				}
				public boolean isEmpty(){
					return feeUi.isEmpty();
				}
				public String getFeeDescription(){
					return feeUi.getFeeDescription();
				}
				public String getFeeValue(){
					return feeUi.getNonFormattedAmountValue();
				}
				public String getFeeValueType(){
					return feeUi.getFeeValueType();
				}
			});
		}	
		return feeDataList;		
	}
	
	
	private static List<FeeScheduleData> getFeeSceduleDataListFromStandardSchedule(List<FeeDataVO> fees){	
		List<FeeScheduleData> feeDataList = new ArrayList<FeeScheduleData> ();
		for(final FeeDataVO fee : fees){
			feeDataList.add(new FeeScheduleData(){
				public boolean getDeleted(){
					return fee.isDeletedIndicator();
				}
				public boolean isEmpty(){
					return (StringUtils.isEmpty(fee.getFeeDescription())
							&& StringUtils.isEmpty(fee.getAmountValue())
							&& StringUtils.isEmpty(fee.getNotes()));
				}
				public String getFeeDescription(){
					return fee.getFeeDescription();
				}
				public String getFeeValue(){
					return fee.getNonFormattedAmountValue();
				}
				public String getNotes(){
					return fee.getNotes();
				}
				public String getFeeValueType(){
					return fee.getAmountType();
				}
				public boolean getFeeDisabled() {
					return false;
				}
			});
		}	
		return feeDataList;		
	}
	/**
	 * Check whether the feeValue is of the form of 4 digits to the left of
	 * decimal and 2 digits to the right of decimal , if not add the error to
	 * the passed list
	 * 
	 * @param amountValue
	 * 
	 */
	public static boolean isValidFeeValueForDollar(String amountValue) {

		Pattern pattern = Pattern.compile("^\\d{1,4}(\\.\\d{1,2})?$");
		Matcher m = pattern.matcher(amountValue);
		if (!m.matches()) {
			return false;
		} else {
			if (BigDecimal.valueOf(9999.99d).compareTo(
					new BigDecimal(amountValue)) >= 0) {
				return true;
			} else {
				return false;
			}
		}

	}

	/**
	 * Check whether the feeValue is of the form of 3 digits to the left of
	 * decimal and 2 digits to the right of decimal , if not add the error to
	 * the passed list
	 * 
	 * @param amountValue
	 * 
	 */
	public static boolean isValidFeeValueForPercentage(String amountValue) {

		Pattern pattern = Pattern.compile("^\\d{1,3}(\\.\\d{1,3})?$");
		Matcher m = pattern.matcher(amountValue);
		if (!m.matches()) {
			return false;
		} else {
			if (Float.parseFloat(amountValue) <= 100.00) {
				return true;
			} else {
				return false;
			}
		}

	}
	
	
	/**
	 * Check whether the pba min deposit is of the form of 9 digits to the left of
	 * decimal and 2 digits to the right of decimal , if not add the error to
	 * the passed list
	 * 
	 * @param amountValue
	 * 
	 */
	public static boolean isValidMinDepositValue(String amountValue) {

		Pattern pattern = Pattern.compile("^\\d{1,9}(\\.\\d{1,2})?$");
		Matcher m = pattern.matcher(amountValue);
		if (!m.matches()) {
			return false;
		} else {
			if (BigDecimal.valueOf(999999999.99d).compareTo(
					new BigDecimal(amountValue)) > 0) {
				return true;
			} else {
				return false;
			}
		}

	}
	
	public static boolean isNumeric(String value){
		try {
			double amount = Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
     * Validates US zip code input value
     * @param errors
     * @param zipCode
     * @param stateCode
     */
    @SuppressWarnings("unchecked")
	public static void validateZipCode(ArrayList<GenericException> errorMessages, String zipCode, String stateCode) {
		if (!StringUtils.isBlank(zipCode)) {
			Pattern numericCharacterRegEx = Pattern.compile("[0-9]");
			String invalidCharacters = getNotAllowedCharacter(
					numericCharacterRegEx, zipCode);
			boolean zipCodeInvalidFormat = false;
			if (invalidCharacters.length() > 0) {
				errorMessages.add(new ValidationError(
						DesignatedInvestmentManagerUIFields.zipCode.name(),
						ErrorCodes.ZIP_NOT_VALID_FOR_THIS_STATE,
						new String[]{DIM}));
				zipCodeInvalidFormat = true;
			} else {
				if (zipCode.length() != 5 && zipCode.length() != 9) {
					errorMessages.add(new ValidationError(
							DesignatedInvestmentManagerUIFields.zipCode.name(),
							ErrorCodes.ZIP_NOT_VALID_FOR_THIS_STATE,
							new String[]{DIM}));
					zipCodeInvalidFormat = true;
				}
			}
			
			if (!zipCodeInvalidFormat && !StringUtils.isBlank(stateCode)) {
				EnvironmentServiceDelegate environmentServiceDelegate = EnvironmentServiceDelegate
						.getInstance(GlobalConstants.PSW_APPLICATION_ID);
				Map<String, List<Pair<Long, Long>>> zipRangesMap = null;
				try {
					zipRangesMap = (Map<String, List<Pair<Long, Long>>>) environmentServiceDelegate
							.getZipCodeRanges();
				} catch (SystemException e) {
					throw new RuntimeException(
							"Unable to retrieve zip code ranges", e);
				}
				List<Pair<Long, Long>> zipRangesList = zipRangesMap
						.get(stateCode);
				if (zipRangesList != null) {
					Long zip = Long.valueOf(zipCode.substring(0, 5));
					boolean validRange = false;
					for (Pair<Long, Long> zipRange : zipRangesList) {
						if (zip.longValue() >= zipRange.getFirst().longValue()
								&& zip.longValue() <= zipRange.getSecond()
										.longValue()) {
							validRange = true;
							break;
						}
					}

					if (!validRange) {
						errorMessages.add(new ValidationError(
								DesignatedInvestmentManagerUIFields.zipCode
										.name(),
								ErrorCodes.ZIP_NOT_VALID_FOR_THIS_STATE,
								new String[] {DIM}));
					}
				}
			}
		}
	}
    
    /**
     * Remove the characters which are not allowed in the allowedCharaterPattern.
     * @param allowedCharacterPattern
     * @param string
     * @return
     */
    public static String getNotAllowedCharacter(
			Pattern allowedCharacterPattern, String string) {
		Matcher m = allowedCharacterPattern.matcher(string);
		String invalidChars = m.replaceAll("");
		return invalidChars;
	}
}
