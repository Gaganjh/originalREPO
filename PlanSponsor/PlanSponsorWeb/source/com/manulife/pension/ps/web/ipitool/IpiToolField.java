package com.manulife.pension.ps.web.ipitool;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;

import com.manulife.pension.service.fee.util.Constants.ContractAdminFeeType;
import com.manulife.pension.service.fee.util.Constants.PaymentTypes;
import com.manulife.pension.service.fee.util.Constants.StatementType;
import com.manulife.pension.service.fee.valueobject.AdministrativeExpense;
import com.manulife.pension.service.fee.valueobject.AdministrativeExpenseParameter;

interface Parser {

}

interface IpiToolField<T> {

	FieldSpec getFieldSpec();
	Class<T> getValueClass();
	String format(Object value);
	
	interface IpiToolFieldValidator {
		ValidationMessage getMessage();
		boolean validate(String value);
	}

	static class IpiToolFieldPatternValidator implements IpiToolFieldValidator {
		
	    private final ValidationMessage message;
		private final Pattern pattern;

		private IpiToolFieldPatternValidator(ValidationMessage message, Pattern pattern) {
			this.message = message;
			this.pattern = pattern;
		}

		@Override
		public ValidationMessage getMessage() { return message; }
		@Override
		public boolean validate(String value) { return pattern.matcher(value).matches(); }
		
	}

	public static final int NUMERICAL_MAX_LENGTH = 12;
	public static final Pattern NUMBER_PATTERN = Pattern.compile("([-]?[1-9]*\\d+)|([-]?[1-9]*\\d+\\.\\d+)");
	
	enum FieldType {
	    
        CODE(StringUtils.EMPTY) {
            String format(Object value) { throw new UnsupportedOperationException(); }
        },
        PERCENTAGE(
                "%",
                new IpiToolFieldValidator() {
                    public ValidationMessage getMessage() { return ValidationMessage.FIELD_FORMAT; }
                    public boolean validate(String value) { return GenericValidator.maxLength(value, NUMERICAL_MAX_LENGTH); }
                }, 
                new IpiToolFieldPatternValidator(ValidationMessage.FIELD_FORMAT, NUMBER_PATTERN)) {
                    @Override
                    String format(Object value) {
                        return ((BigDecimal) value).toPlainString();
                    }
                },
        CURRENCY(
                "$",
                new IpiToolFieldValidator() {
        			public ValidationMessage getMessage() { return ValidationMessage.FIELD_FORMAT; }
                    public boolean validate(String value) { return GenericValidator.maxLength(value, NUMERICAL_MAX_LENGTH); }
                },
                new IpiToolFieldPatternValidator(ValidationMessage.FIELD_FORMAT, NUMBER_PATTERN)) {
                    @Override
                    String format(Object value) {
                        return ((BigDecimal) value).toPlainString();
                    }
                };

        private final String unit;
		private final List<IpiToolFieldValidator> validatorList;

		private FieldType(String unit, IpiToolFieldValidator... validators) {
		    this.unit = unit;
			validatorList = Collections.unmodifiableList(Arrays
					.asList(validators));
		}
		
		String getUnit() { return unit; }
		List<IpiToolFieldValidator> getValidatorList() {
			return validatorList;
		}
		
		abstract String format(Object value);
		
	}
	
	enum CustomFormat {
	    ALLOW_NEGATIVE;
	}
	
	interface FieldSpec {
		String getFormName();
		FieldType getType();
		int getMaxIntDigits();
		int getScale();
		Set<CustomFormat> getCustomFormatsForCurrentValue();
		Set<CustomFormat> getCustomFormatsForInputValue();
		int getOrder();
		String getLabelText();
	    String getUnit(); // $ or %
	    Set<String> getValues();
	}
	
    interface IpiToolInputField<T> {
        InputFieldSpec<T> getInputFieldSpec();
    }
	interface InputFieldSpec<T> {
	    AdministrativeExpenseParameter<T> getCalculationParameterKey();
	}

	class CodeInputFieldSpec<T>
	implements FieldSpec, InputFieldSpec<T> {
	    
		private static class Builder<T> {
		    
			private int order;
			private String labelText;
			private String formName;
			private FieldType type;
			private Set<String> values;
			private ContractAdminFeeType retrievalKey;
			private AdministrativeExpenseParameter<T> calculationParameterKey;

			Builder<T> order(int order) { this.order = order; return this; }
			Builder<T> labelText(String labelText) { this.labelText = labelText; return this; }
			Builder<T> formName(String formName) { this.formName = formName; return this; }
			Builder<T> type(FieldType type) { this.type = type; return this; }
			Builder<T> values(String... values) { this.values = new LinkedHashSet<String>(Arrays.asList(values)); return this; }
			Builder<T> retrievalKey(ContractAdminFeeType retrievalKey) { this.retrievalKey = retrievalKey; return this; }
			Builder<T> calculationParameterKey(AdministrativeExpenseParameter<T> key) { this.calculationParameterKey = key; return this; }
			CodeInputFieldSpec<T> build() {
				return new CodeInputFieldSpec<T>(order, labelText, formName,
						type, values, retrievalKey,
						calculationParameterKey);
			}
			
		}
		
		private static final Set<CustomFormat> CUSTOM_FORMATS = Collections.unmodifiableSet(EnumSet.noneOf(CustomFormat.class));
		private final int order;
		private final String labelText ;
		private final String formName;
		private final FieldType type;
		private final Set<String> values;
		private final ContractAdminFeeType retrievalKey;
		private final AdministrativeExpenseParameter<T> calculationParameterKey;

		private CodeInputFieldSpec(int order, String labelText, String formName,
				FieldType type, Set<String> values,
				ContractAdminFeeType retrievalKey,
				AdministrativeExpenseParameter<T> calculationParameterKey) {
			this.order = order;
			this.labelText = labelText;
			this.formName = formName;
			this.type = type;
			this.values = Collections.unmodifiableSet(values);
			this.retrievalKey = retrievalKey;
			this.calculationParameterKey = calculationParameterKey;
		}

		public int getOrder() { return order; }
		public String getLabelText() { return labelText; }
		public String getFormName() { return formName; }
		public FieldType getType() { return type; }
		public int getMaxIntDigits() { return 0; }
		public int getScale() { return 0; }
		public Set<CustomFormat> getCustomFormats() { return CUSTOM_FORMATS; }
		public Set<String> getValues() { return values; }
		public ContractAdminFeeType getRetrievalKey() { return retrievalKey; }
		public AdministrativeExpenseParameter<T> getCalculationParameterKey() { return calculationParameterKey; }
		public String getUnit() { return type.getUnit(); }

		@Override
		public Set<CustomFormat> getCustomFormatsForCurrentValue() {
			return CUSTOM_FORMATS;
		}

		@Override
		public Set<CustomFormat> getCustomFormatsForInputValue() {
			return CUSTOM_FORMATS;
		}

	}

	class NumericInputFieldSpec
	implements FieldSpec, InputFieldSpec<BigDecimal> {

		private static class Builder {

			private int order;
			private String labelText;
			private String formName;
			private FieldType type;
			private int maxIntDigits;
			private int scale;
			private EnumSet<CustomFormat> currentCustomFormats;
			private EnumSet<CustomFormat> inputCustomFormats;
			private ContractAdminFeeType retrievalKey;
			private AdministrativeExpenseParameter<BigDecimal> calculationParameterKey;

			Builder order(int order) { this.order = order; return this; }
			Builder labelText(String labelText) { this.labelText = labelText; return this; }
			Builder formName(String formName) { this.formName = formName; return this; }
			Builder type(FieldType type) { this.type = type; return this; }
			Builder maxIntDigits(int digits) { maxIntDigits = digits; return this; }
			Builder scale(int scale) { this.scale = scale; return this; }
			Builder currentCustomFormats(CustomFormat first, CustomFormat... rest) { currentCustomFormats = EnumSet.of(first, rest); return this; }
			Builder inputCustomFormats(CustomFormat first, CustomFormat... rest) { inputCustomFormats = EnumSet.of(first, rest); return this; }
			Builder retrievalKey(ContractAdminFeeType retrievalKey) { this.retrievalKey = retrievalKey; return this; }
			Builder calculationParameterKey(AdministrativeExpenseParameter<BigDecimal> key) { this.calculationParameterKey = key; return this; }
			NumericInputFieldSpec build() {
				return new NumericInputFieldSpec(order, labelText, formName,
						type, maxIntDigits, scale, currentCustomFormats, inputCustomFormats, retrievalKey,
						calculationParameterKey);
			}

		}

        private static final Set<CustomFormat> NO_CUSTOM_FORMATS = Collections.unmodifiableSet(EnumSet.noneOf(CustomFormat.class));
		private final int order;
		private final String labelText;
		private final String formName;
		private final FieldType type;
		private final int intDigits;
		private final int scale;
		private final Set<CustomFormat> currentCustomFormats;
		private final Set<CustomFormat> inputCustomFormats;
		private final ContractAdminFeeType retrievalKey;
		private final AdministrativeExpenseParameter<BigDecimal> calculationParameterKey;

		private NumericInputFieldSpec(int order, String labelText,
				String formName, FieldType type, int intDigits, int scale, EnumSet<CustomFormat> currentCustomFormats,
				EnumSet<CustomFormat> inputCustomFormats, ContractAdminFeeType retrievalKey,
				AdministrativeExpenseParameter<BigDecimal> calculationParameterKey) {
			this.order = order;
			this.labelText = labelText;
			this.formName = formName;
			this.type = type;
			this.intDigits = intDigits;
			this.scale = scale;
			this.currentCustomFormats = currentCustomFormats == null ? NO_CUSTOM_FORMATS : Collections.unmodifiableSet(currentCustomFormats);
			this.inputCustomFormats = inputCustomFormats == null ? NO_CUSTOM_FORMATS : Collections.unmodifiableSet(inputCustomFormats);
			this.retrievalKey = retrievalKey;
			this.calculationParameterKey = calculationParameterKey;
		}

		public int getOrder() { return order; }
		public String getLabelText() { return labelText; }
		public String getFormName() { return formName; }
		public FieldType getType() { return type; }
		public int getMaxIntDigits() { return intDigits; }
		public int getScale() { return scale; }
		public Set<CustomFormat> currentCustomFormats() { return currentCustomFormats; }
		public Set<CustomFormat> inputCustomFormats() { return inputCustomFormats; }
		public ContractAdminFeeType getRetrievalKey() { return retrievalKey; }
		public AdministrativeExpenseParameter<BigDecimal> getCalculationParameterKey() { return calculationParameterKey; }
		public String getUnit() { return type.getUnit(); }
		public Set<String> getValues() { return null; }

		@Override
		public Set<CustomFormat> getCustomFormatsForCurrentValue() {
			return currentCustomFormats;
		}

		@Override
		public Set<CustomFormat> getCustomFormatsForInputValue() {
			return inputCustomFormats;
		}


	}

	class SummaryFieldSpec implements FieldSpec {

		private static class Builder {

			private int order;
			private String labelText;
			private String formName;
			private FieldType type;
			private int maxIntDigits;
			private int scale;
			private EnumSet<CustomFormat> customFormats;
			private ContractAdminFeeType retrievalKey;
			private AdministrativeExpense calculationKey;

			Builder order(int order) { this.order = order; return this; }
			Builder labelText(String labelText) { this.labelText = labelText; return this; }
			Builder formName(String formName) { this.formName = formName; return this; }
			Builder type(FieldType type) { this.type = type; return this; }
			Builder maxIntDigits(int digits) { maxIntDigits = digits; return this; }
			Builder scale(int scale) { this.scale = scale; return this; }
			Builder customFormats(CustomFormat first, CustomFormat... rest) { this.customFormats = EnumSet.of(first, rest); return this; }
			Builder retrievalKey(ContractAdminFeeType retrievalKey) { this.retrievalKey = retrievalKey; return this; }
			Builder calculationKey(AdministrativeExpense key) { this.calculationKey = key; return this; }
			SummaryFieldSpec build() {
				return new SummaryFieldSpec(order, labelText, formName,
						type, maxIntDigits, scale, customFormats, retrievalKey, calculationKey);
			}

		}

        private static final Set<CustomFormat> NO_CUSTOM_FORMATS = Collections.unmodifiableSet(EnumSet.noneOf(CustomFormat.class));
		private final int order;
		private final String labelText;
		private final String formName;
		private final FieldType type;
		private final int maxIntDigits;
		private final int scale;
		private final Set<CustomFormat> customFormats;
		private final ContractAdminFeeType retrievalKey;
		private final AdministrativeExpense calculationKey;

		private SummaryFieldSpec(int order, String labelText, String formName,
				FieldType type, int maxIntDigits, int scale, EnumSet<CustomFormat> customFormats,
				ContractAdminFeeType retrievalKey,
				AdministrativeExpense calculationKey) {
			this.order = order;
			this.labelText = labelText;
			this.formName = formName;
			this.type = type;
			this.maxIntDigits = maxIntDigits;
			this.scale = scale;
			this.customFormats = customFormats == null ? NO_CUSTOM_FORMATS : Collections.unmodifiableSet(customFormats);
			this.retrievalKey = retrievalKey;
			this.calculationKey = calculationKey;
		}

		public int getOrder() { return order; }
		public String getLabelText() { return labelText; }
		public String getFormName() { return formName; }
		public FieldType getType() { return type; }
		public int getMaxIntDigits() { return maxIntDigits; }
		public int getScale() { return scale; }
		public Set<CustomFormat> getCustomFormats() { return customFormats; }
		public ContractAdminFeeType getRetrievalKey() { return retrievalKey; }
		public AdministrativeExpense getCalculationKey() { return calculationKey; }
		public String getUnit() { return type.getUnit(); }
		public Set<String> getValues() { return null; }

		@Override
		public Set<CustomFormat> getCustomFormatsForCurrentValue() {
			return customFormats;
			
		}

		@Override
		public Set<CustomFormat> getCustomFormatsForInputValue() {
			return customFormats;
			
		}


	}

	enum IpiToolRecoveryMethodField
	implements IpiToolField<PaymentTypes>, IpiToolInputField<PaymentTypes> {

		ASSET_CHARGE_RECOVERY_METHOD                   (new CodeInputFieldSpec.Builder<PaymentTypes>()  .order(0)  .labelText("Asset Charges")                                                               .formName("assetChargeRm")                      .type(FieldType.CODE) .values("DD", "BL")                 .retrievalKey(ContractAdminFeeType.ASSET_CHARGES_RECOVERY_METHOD)                  .calculationParameterKey(AdministrativeExpenseParameter.RecoveryMethod.ASSET_CHARGES_RECOVERY_METHOD)                  .build()),
		OTHER_CHARGE_RECOVERY_METHOD                   (new CodeInputFieldSpec.Builder<PaymentTypes>()  .order(1)  .labelText("Other Charges")                                                               .formName("otherChargeRm")                      .type(FieldType.CODE) .values("DD", "BL")                 .retrievalKey(ContractAdminFeeType.OTHER_CHARGES_RECOVERY_METHOD)                  .calculationParameterKey(AdministrativeExpenseParameter.RecoveryMethod.OTHER_CHARGES_RECOVERY_METHOD)                  .build()),
		DET_PART_STMT_RECOVERY_METHOD                  (new CodeInputFieldSpec.Builder<PaymentTypes>()  .order(2)  .labelText("Detailed Participant Statement")                                              .formName("detailedParticipantStmtRm")          .type(FieldType.CODE) .values("DD", "BL")                 .retrievalKey(ContractAdminFeeType.DETAILED_PARTICIPANT_STATEMENT_RECOVERY_METHOD) .calculationParameterKey(AdministrativeExpenseParameter.RecoveryMethod.DETAILED_PARTICIPANT_STATEMENT_RECOVERY_METHOD) .build());

		private final CodeInputFieldSpec<PaymentTypes> spec;

		private IpiToolRecoveryMethodField(CodeInputFieldSpec<PaymentTypes> spec) { this.spec = spec; }
		CodeInputFieldSpec<PaymentTypes> getSpec() { return spec; }
        @Override
		public FieldSpec getFieldSpec() { return spec; }
        @Override
		public InputFieldSpec<PaymentTypes> getInputFieldSpec() { return spec; }
        @Override
        public Class<PaymentTypes> getValueClass() { return PaymentTypes.class; }
		@Override
		public String format(Object value) { return ((PaymentTypes) value).getCode(); }

	}
	
	enum IpiToolStatementTypeField
	implements IpiToolField<StatementType>, IpiToolInputField<StatementType> {
	    
        CONTRACT_STATEMENT_RECOVERY_METHOD             (new CodeInputFieldSpec.Builder<StatementType>() .order(3)  .labelText("Contract Stmt Type")                                                          .formName("contractStmtRm")                     .type(FieldType.CODE)  .values("D", "S")                  .retrievalKey(ContractAdminFeeType.CONTRACT_STATEMENT_TYPE)                        .calculationParameterKey(AdministrativeExpenseParameter.StatementType.CONTRACT_STATEMENT_TYPE)                         .build());

        private final CodeInputFieldSpec<StatementType> spec;

        private IpiToolStatementTypeField(CodeInputFieldSpec<StatementType> spec) { this.spec = spec; }
        CodeInputFieldSpec<StatementType> getSpec() { return spec; }
        @Override
        public FieldSpec getFieldSpec() { return spec; }
        @Override
        public InputFieldSpec<StatementType> getInputFieldSpec() { return spec; }
        @Override
        public Class<StatementType> getValueClass() { return StatementType.class; }
		@Override
		public String format(Object value) { return ((StatementType) value).getCode(); }

	}
	
	enum IpiToolJhChargeField
	implements IpiToolField<BigDecimal>, IpiToolInputField<BigDecimal> {

		BLENDED_ASSET_CHARGE                           (new NumericInputFieldSpec.Builder()             .order(0)  .labelText("Blended Asset Charge (BAC)")                                                  .formName("blendedAssetCharge")                 .type(FieldType.PERCENTAGE)  .maxIntDigits(3)  .scale(3)  .retrievalKey(ContractAdminFeeType.BLENDED_ASSET_CHARGE)                           .calculationParameterKey(AdministrativeExpenseParameter.Charge.BLENDED_ASSET_CHARGE)                                   .build()),
		MONTHLY_PARTICIPANT_FEE                        (new NumericInputFieldSpec.Builder()             .order(1)  .labelText("Monthly Participant Fee ($)")                                                 .formName("monthlyParticipantFee")              .type(FieldType.CURRENCY)    .maxIntDigits(3)  .scale(2)  .retrievalKey(ContractAdminFeeType.MONTHLY_PARTICIPANT_FEE)                        .calculationParameterKey(AdministrativeExpenseParameter.Charge.MONTHLY_PARTICIPANT_FEE)                                .build()),
		ANNUALIZED_PARTICIPANT_FEE                     (new NumericInputFieldSpec.Builder()             .order(2)  .labelText("Annualized Participant Fee")                                                  .formName("annualizedParticipantFee")           .type(FieldType.PERCENTAGE)  .maxIntDigits(3)  .scale(4)  .retrievalKey(ContractAdminFeeType.ANNUALIZED_PARTICIPANT_FEE)                     .calculationParameterKey(AdministrativeExpenseParameter.Charge.ANNUALIZED_PARTICIPANT_FEE)                             .build()),
		MARKET_VALUE_EQUALIZER                         (new NumericInputFieldSpec.Builder()             .order(3)  .labelText("Market Value Equalizer")                                                      .formName("marketValueEqualzer")                .type(FieldType.PERCENTAGE)  .maxIntDigits(3)  .scale(6)  .retrievalKey(ContractAdminFeeType.MARKET_VALUE_EQUALIZER)                         .calculationParameterKey(AdministrativeExpenseParameter.Charge.MARKET_VALUE_EQUALIZER)                                 .build()),
		CONDITIONAL_RECORDKEEPING_FEE                  (new NumericInputFieldSpec.Builder()             .order(4)  .labelText("Conditional Recordkeeping Fee ($)")                                           .formName("conditionalRecordkeepingFee")        .type(FieldType.CURRENCY)    .maxIntDigits(5)  .scale(2)  .retrievalKey(ContractAdminFeeType.CONDITIONAL_RECORDKEEPING_FEE)                  .calculationParameterKey(AdministrativeExpenseParameter.Charge.CONDITIONAL_RECORDKEEPING_FEE)                          .build()),
		FLAT_ASSET_CHARGE_ADJUSTMENT                   (new NumericInputFieldSpec.Builder()             .order(5)  .labelText("Flat Asset Charge Adjustment")                                                .formName("flatAssetChargeAdjustment")          .type(FieldType.PERCENTAGE)  .maxIntDigits(3)  .scale(3)  .retrievalKey(ContractAdminFeeType.FLAT_ASSET_CHARGE_ADJUSTMENT)                   .calculationParameterKey(AdministrativeExpenseParameter.Charge.FLAT_ASSET_CHARGE_ADJUSTMENT)                           .currentCustomFormats(CustomFormat.ALLOW_NEGATIVE)		.inputCustomFormats(CustomFormat.ALLOW_NEGATIVE)  .build()),
		LEGACY_ADJUSTMENTS                             (new NumericInputFieldSpec.Builder()             .order(6)  .labelText("Legacy Adjustments (pre ARA 92)")                                             .formName("legacyAdjustments")                  .type(FieldType.PERCENTAGE)  .maxIntDigits(3)  .scale(6)  .retrievalKey(ContractAdminFeeType.LEGACY_ADJUSTMENTS)                             .calculationParameterKey(AdministrativeExpenseParameter.Charge.LEGACY_ADJUSTMENTS)                                     .currentCustomFormats(CustomFormat.ALLOW_NEGATIVE)  	.build()),
		CONTRACT_LEVEL_DETAILED_STATEMENT_FEE          (new NumericInputFieldSpec.Builder()             .order(7)  .labelText("Conditional Level Detailed Statement Fee ($)")                                .formName("contractLevelDetailedStatementFee")  .type(FieldType.CURRENCY)    .maxIntDigits(2)  .scale(2)  .retrievalKey(ContractAdminFeeType.CONTRACT_LEVEL_DETAILED_STATEMENT_FEE)          .calculationParameterKey(AdministrativeExpenseParameter.Charge.CONTRACT_LEVEL_DETAILED_STATEMENT_FEE)                  .build()),
		DISCONTINUANCE_FEE                             (new NumericInputFieldSpec.Builder()             .order(8)  .labelText("Discontinuance (Backend) Charge")                                             .formName("discontinuanceFee")                  .type(FieldType.PERCENTAGE)  .maxIntDigits(3)  .scale(3)  .retrievalKey(ContractAdminFeeType.DISCONTINUANCE_CHARGE_SCALE)                    .calculationParameterKey(AdministrativeExpenseParameter.Charge.DISCONTINUANCE_FEE)                                     .build()),
		DIO_DISCOUNT                                   (new NumericInputFieldSpec.Builder()             .order(9)  .labelText("DIO Discount")                                                                .formName("dioDiscount")                        .type(FieldType.PERCENTAGE)  .maxIntDigits(5)  .scale(6)  .retrievalKey(ContractAdminFeeType.DIO_DISCOUNT)                         .calculationParameterKey(AdministrativeExpenseParameter.Charge.CREDITS_TO_PARTICIPANT)                                 .build());
		
		private final NumericInputFieldSpec spec;

		private IpiToolJhChargeField(NumericInputFieldSpec spec) { this.spec = spec; }
		NumericInputFieldSpec getSpec() { return spec; }
        @Override
		public FieldSpec getFieldSpec() { return spec; }
        @Override
        public InputFieldSpec<BigDecimal> getInputFieldSpec() { return spec; }
        @Override
        public Class<BigDecimal> getValueClass() { return BigDecimal.class; }
        @Override
        public String format(Object value) { return spec.type.format(value); }

	}

	enum IpiToolSalesAndServiceChargeField
	implements IpiToolField<BigDecimal>, IpiToolInputField<BigDecimal> {

		DEPOSIT_BASED_SERC                             (new NumericInputFieldSpec.Builder()             .order(0)  .labelText("Deposit-Based SERC")                                                          .formName("depositBasedService")                .type(FieldType.PERCENTAGE)  .maxIntDigits(3)  .scale(7)  .retrievalKey(ContractAdminFeeType.DEPOSIT_BASED_SERC)                             .calculationParameterKey(AdministrativeExpenseParameter.Charge.DEPOSIT_BASED_SERC)                                     .build()),
		DEPOSIT_BASED_2ND_YEAR_AND_RECURRING           (new NumericInputFieldSpec.Builder()             .order(1)  .labelText("Deposit-Based 2nd Year + Recurring")                                          .formName("depositBased2ndYearRecurring")       .type(FieldType.PERCENTAGE)  .maxIntDigits(3)  .scale(3)  .retrievalKey(ContractAdminFeeType.DEPOSIT_BASED_2ND_YEAR_AND_RECURRING)           .calculationParameterKey(AdministrativeExpenseParameter.Charge.DEPOSIT_BASED_2ND_YEAR_AND_RECURRING)                   .build()),
		ASSET_BASED_SERC                               (new NumericInputFieldSpec.Builder()             .order(2)  .labelText("Asset-Based SERC")                                                            .formName("abSerc")                             .type(FieldType.PERCENTAGE)  .maxIntDigits(3)  .scale(3)  .retrievalKey(ContractAdminFeeType.ASSET_BASED_SERC)                               .calculationParameterKey(AdministrativeExpenseParameter.Charge.ASSET_BASED_SERC)                                       .build()),
		ASSET_BASED_2ND_YEAR_AND_RECURRING             (new NumericInputFieldSpec.Builder()             .order(3)  .labelText("Asset-Based 2nd Year +")                                                      .formName("assetBased2ndYearRecurring")         .type(FieldType.PERCENTAGE)  .maxIntDigits(3)  .scale(3)  .retrievalKey(ContractAdminFeeType.ASSET_BASED_2ND_YEAR_AND_RECURRING)             .calculationParameterKey(AdministrativeExpenseParameter.Charge.ASSET_BASED_2ND_YEAR_AND_RECURRING)                     .build()),
		ASSET_BASED_ALL_YEARS                          (new NumericInputFieldSpec.Builder()             .order(4)  .labelText("Asset-Based SERC All Years (only applies to AIP, 10k and ARA 85 contracts)")  .formName("assetBasedAllYear")                  .type(FieldType.PERCENTAGE)  .maxIntDigits(3)  .scale(6)  .retrievalKey(ContractAdminFeeType.ASSET_BASED_ALL_YEARS)                          .calculationParameterKey(AdministrativeExpenseParameter.Charge.ASSET_BASED_ALL_YEARS)                                  .build()),
		MONTHLY_TPA_PARTICIPANT_FEE                    (new NumericInputFieldSpec.Builder()             .order(5)  .labelText("Monthly TPA Participant Fee ($)")                                             .formName("monthlyTPAParticipantFee")           .type(FieldType.CURRENCY)    .maxIntDigits(3)  .scale(2)  .retrievalKey(ContractAdminFeeType.MONTHLY_TPA_PARTICIPANT_FEE)                    .calculationParameterKey(AdministrativeExpenseParameter.Charge.MONTHLY_TPA_PARTICIPANT_FEE)                            .build()),
		ANNUALIZED_TPA_ASSET_BASED_FEE                 (new NumericInputFieldSpec.Builder()             .order(6)  .labelText("Annualized TPA Asset-Based Fee")                                              .formName("annualizedTPAAssetBasedFee")         .type(FieldType.PERCENTAGE)  .maxIntDigits(3)  .scale(6)  .retrievalKey(ContractAdminFeeType.ANNUALIZED_TPA_ASSET_BASED_FEE)                 .calculationParameterKey(AdministrativeExpenseParameter.Charge.ANNUALIZED_TPA_ASSET_BASED_FEE)                         .build()),
		ANNUALIZED_TPA_FEE                             (new NumericInputFieldSpec.Builder()             .order(7)  .labelText("Annualized TPA Fee")                                                          .formName("annualizedTPAFee")                   .type(FieldType.PERCENTAGE)  .maxIntDigits(3)  .scale(3)  .retrievalKey(ContractAdminFeeType.ANNUALIZED_TPA_FEE)                             .calculationParameterKey(AdministrativeExpenseParameter.Charge.ANNUALIZED_TPA_FEE)                                     .build()),
		ANNUALIZED_RIA_FEE                             (new NumericInputFieldSpec.Builder()             .order(8)  .labelText("Annualized RIA Fee")                                                          .formName("annualizedRIAFee")                   .type(FieldType.PERCENTAGE)  .maxIntDigits(3)  .scale(3)  .retrievalKey(ContractAdminFeeType.ANNUALIZED_RIA_FEE)                             .calculationParameterKey(AdministrativeExpenseParameter.Charge.ANNUALIZED_RIA_FEE)                                     .build());

		private final NumericInputFieldSpec spec;

		private IpiToolSalesAndServiceChargeField(NumericInputFieldSpec spec) { this.spec = spec; }
		NumericInputFieldSpec getSpec() { return spec; }
        @Override
		public FieldSpec getFieldSpec() { return spec; }
		@Override
		public InputFieldSpec<BigDecimal> getInputFieldSpec() { return spec; }
        @Override
        public Class<BigDecimal> getValueClass() { return BigDecimal.class; }
        @Override
		public String format(Object value) { return spec.type.format(value); }

	}
	
	enum IpiToolTrusteeChargesField
	implements IpiToolField<BigDecimal>, IpiToolInputField<BigDecimal> {
		ANNUALIZED_TRUSTEE_FEE                         (new NumericInputFieldSpec.Builder()             .order(0)  .labelText("Annualized Trustee Fee")                                                      .formName("annualizedTrusteeFee")                .type(FieldType.PERCENTAGE)  .maxIntDigits(3)  .scale(3)  .retrievalKey(ContractAdminFeeType.ANNUALIZED_TRUSTEE_FEE)                         .calculationParameterKey(AdministrativeExpenseParameter.Charge.ANNUALIZED_TRUSTEE_FEE)                                 .build());
		
		private final NumericInputFieldSpec spec;

		private IpiToolTrusteeChargesField(NumericInputFieldSpec spec) { this.spec = spec; }
		NumericInputFieldSpec getSpec() { return spec; }
        @Override
		public FieldSpec getFieldSpec() { return spec; }
        @Override
        public InputFieldSpec<BigDecimal> getInputFieldSpec() { return spec; }
        @Override
        public Class<BigDecimal> getValueClass() { return BigDecimal.class; }
        @Override
        public String format(Object value) { return spec.type.format(value); }
        
	}
	
	enum IpiToolSummaryField
	implements IpiToolField<BigDecimal> {
	    
		ASSET_BASED_JH_RECORDKEEPING_CHARGE            (new SummaryFieldSpec.Builder()                  .order(0)  .labelText("John Hancock Recordkeeping Charge (%)")                                       .formName("jhRecordKeepingCharge")               .type(FieldType.PERCENTAGE)  .maxIntDigits(2)  .scale(2)  .retrievalKey(ContractAdminFeeType.JH_ASSET_BASED_FEE)                             .calculationKey(AdministrativeExpense.JH_ASSET_BASED_RECORDKEEPING_CHARGE)                                             .build()),
		PARTICIPANT_BASED_JH_RECORDKEEPING_CHARGE      (new SummaryFieldSpec.Builder()                  .order(0)  .labelText("John Hancock Recordkeeping Charge ($)")                                       .formName("jhParticipantRecordKeepingCharge")    .type(FieldType.CURRENCY)    .maxIntDigits(2)  .scale(2)  .retrievalKey(ContractAdminFeeType.JH_PPT_BASED_FEE)                               .calculationKey(AdministrativeExpense.JH_PARTICIPANT_BASED_RECORDKEEPING_CHARGE)                                       .build()),
		DETAILED_JH_STATEMENT_CHARGE                   (new SummaryFieldSpec.Builder()                  .order(0)  .labelText("John Hancock Detailed Statement Charge")                                      .formName("jhDetailedStatementCharge")           .type(FieldType.CURRENCY)    .maxIntDigits(2)  .scale(2)  .retrievalKey(ContractAdminFeeType.JH_DTL_STMT_FEE)                                .calculationKey(AdministrativeExpense.JH_DETAILED_STATEMENT_CHARGE)                                                    .build()),
		JH_ASSET_BASED_SALES_AND_SERVICE_CHARGE        (new SummaryFieldSpec.Builder()                  .order(0)  .labelText("Sales & Service Charge (%)")                                                  .formName("assetBasedSalesServiceCharge")        .type(FieldType.PERCENTAGE)  .maxIntDigits(2)  .scale(2)  .retrievalKey(ContractAdminFeeType.TPA_PPT_BASED_FEE)                              .calculationKey(AdministrativeExpense.ASSET_BASED_SALES_AND_SERVICE_CHARGE)                                            .build()),
		JH_PARTICIPANT_BASED_SALES_AND_SERVICE_CHARGE  (new SummaryFieldSpec.Builder()                  .order(0)  .labelText("Sales & Service Charge ($)")                                                  .formName("participantBasedSalesServiceCharge")  .type(FieldType.CURRENCY)    .maxIntDigits(2)  .scale(2)  .retrievalKey(ContractAdminFeeType.TPA_PPT_BASED_FEE)                              .calculationKey(AdministrativeExpense.PARTICIPANT_BASED_SALES_AND_SERVICE_CHARGE)                                      .build()),
		JH_ASSET_BASED_TRUSTEE_CHARGE                  (new SummaryFieldSpec.Builder()                  .order(0)  .labelText("Trustee Charge (%)")                                                          .formName("assetBasedTrusteeCharge")             .type(FieldType.PERCENTAGE)  .maxIntDigits(2)  .scale(2)  .retrievalKey(ContractAdminFeeType.TRUSTEE_ASSET_BASED_FEE)                        .calculationKey(AdministrativeExpense.ASSET_BASED_TRUSTEE_CHARGE)                                                      .build());
		
		private final SummaryFieldSpec spec;
		
		private IpiToolSummaryField(SummaryFieldSpec spec) { this.spec = spec; }
		SummaryFieldSpec getSpec() { return spec; }
		public FieldSpec getFieldSpec() { return spec; }
		public Class<BigDecimal> getValueClass() { return BigDecimal.class; }
        @Override
        public String format(Object value) { return spec.type.format(value); }
        
	}
	
}
