package com.manulife.pension.ps.web.ipitool;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.manulife.pension.ps.web.ipitool.IpiToolField.CodeInputFieldSpec;
import com.manulife.pension.ps.web.ipitool.IpiToolField.CustomFormat;
import com.manulife.pension.ps.web.ipitool.IpiToolField.FieldSpec;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolFieldValidator;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolJhChargeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.NumericInputFieldSpec;
import com.manulife.pension.util.ArrayUtility;

class FieldLevelFormValidator {
    
    private final List<FieldValidation> validationList;
    
    interface FieldValidation {
        
        void validate(String value, IpiToolField.FieldSpec fieldSpec, FieldLevelErrorDefinitionBuilder builder);
        Set<FieldValidation> getDependencies();
        
    }

    public static final FieldValidation FIELD_TYPE_VALIDATION =
            new FieldValidation() {
                
                @Override
                public void validate(String value, FieldSpec fieldSpec, FieldLevelErrorDefinitionBuilder builder) {
                    
                    for ( IpiToolFieldValidator validator : fieldSpec.getType().getValidatorList()) {
                        
                        if (! validator.validate(value)) {
                            
                            builder.add(validator.getMessage());
                            
                        }
                        
                    }
                    
                }

                @Override
                public Set<FieldValidation> getDependencies() { return Collections.emptySet(); }
                
            };
    
    public static final FieldValidation ALLOW_CURRENT_NEGATIVE_VALIDATION =
            new FieldValidation() {
        
                @Override
                public void validate(String value, FieldSpec fieldSpec, FieldLevelErrorDefinitionBuilder builder) {
                    
                    try {
                        
                        if (fieldSpec instanceof NumericInputFieldSpec
                                && new BigDecimal(value).signum() < 0
                                && ! ((NumericInputFieldSpec) fieldSpec).getCustomFormatsForCurrentValue().contains(CustomFormat.ALLOW_NEGATIVE)) {
                            
                            builder.add(ValidationMessage.FIELD_FORMAT);
                            
                        }
                        
                    } catch (NumberFormatException nfe) {
                        // do nothing
                    }
                    
                }
                
                @Override
                public Set<FieldValidation> getDependencies() { return ArrayUtility.toUnsortedSet(FIELD_TYPE_VALIDATION); }
                
            };
            
      public static final FieldValidation ALLOW_INPUT_NEGATIVE_VALIDATION =
                new FieldValidation() {
            
                    @Override
                    public void validate(String value, FieldSpec fieldSpec, FieldLevelErrorDefinitionBuilder builder) {
                        
                        try {
                            
                            if (fieldSpec instanceof NumericInputFieldSpec
                                    && new BigDecimal(value).signum() < 0
                                    && ! ((NumericInputFieldSpec) fieldSpec).getCustomFormatsForInputValue().contains(CustomFormat.ALLOW_NEGATIVE)) {
                                
                                builder.add(ValidationMessage.FIELD_FORMAT);
                                
                            }
                            
                        } catch (NumberFormatException nfe) {
                            // do nothing
                        }
                        
                    }
                    
                    @Override
                    public Set<FieldValidation> getDependencies() { return ArrayUtility.toUnsortedSet(FIELD_TYPE_VALIDATION); }
                    
                };
    
    public static final FieldValidation SCALE_VALIDATION =
            new FieldValidation() {
                
                @Override
                public void validate(String value, FieldSpec fieldSpec, FieldLevelErrorDefinitionBuilder builder) {
                    
        			if (fieldSpec instanceof NumericInputFieldSpec
        					&& value.indexOf("-") == 0
        					&& fieldSpec.getFormName().equals(
        							IpiToolJhChargeField.FLAT_ASSET_CHARGE_ADJUSTMENT
        									.getFieldSpec().getFormName())
        					&& value.contains(".")
                            && (value.length()-1) - (value.indexOf(".")) > fieldSpec.getScale()) {
                            	
                            	 builder.add(ValidationMessage.FIELD_FORMAT);
                            	 
					} else if (fieldSpec instanceof NumericInputFieldSpec
							&& value.indexOf("-") < 0
							&& value.contains(".")
							&& (value.length() - 1) - value
								.indexOf(".") > fieldSpec.getScale()) {
                        
                        builder.add(ValidationMessage.FIELD_FORMAT);
                        
                    }
                    
                }

                @Override
                public Set<FieldValidation> getDependencies() { return ArrayUtility.toUnsortedSet(FIELD_TYPE_VALIDATION); }
                
            };
    
    public static final FieldValidation MAX_INT_DIGITS_VALIDATION =
            new FieldValidation() {
                
                @Override
                public void validate(String value, FieldSpec fieldSpec, FieldLevelErrorDefinitionBuilder builder) {
                    
			if (fieldSpec instanceof NumericInputFieldSpec
					&& value.indexOf("-") == 0
					&& fieldSpec.getFormName().equals(
							IpiToolJhChargeField.FLAT_ASSET_CHARGE_ADJUSTMENT
									.getFieldSpec().getFormName())
					&& (value.contains(".") ? (value.indexOf(".")-1) : (value
							.length()-1)) > fieldSpec.getMaxIntDigits()) {
                    	
                    	 builder.add(ValidationMessage.FIELD_FORMAT);
                    	 
                    }else if (fieldSpec instanceof NumericInputFieldSpec
                    		&& value.indexOf("-") < 0
                            && (value.contains(".")
                                    ? value.indexOf(".")
                                    : value.length())
                                > fieldSpec.getMaxIntDigits()) {
                        
                        builder.add(ValidationMessage.FIELD_FORMAT);
                        
                    }
                    
                }
                
                @Override
                public Set<FieldValidation> getDependencies() { return ArrayUtility.toUnsortedSet(FIELD_TYPE_VALIDATION); }
                
            };
    
    public static final FieldValidation CODE_VALIDATION =
            new FieldValidation() {
        
                @Override
                public void validate(String value, FieldSpec fieldSpec, FieldLevelErrorDefinitionBuilder builder) {
                        
                    if (fieldSpec instanceof CodeInputFieldSpec<?>
                            && ! ((CodeInputFieldSpec<?>) fieldSpec).getValues().contains(value)) {
                        
                        builder.add(ValidationMessage.FIELD_FORMAT);
                        
                    }
                    
                }

                @Override
                public Set<FieldValidation> getDependencies() { return Collections.emptySet(); }
                
            };
    
    static FieldLevelFormValidator getInstance(FieldValidation... validations) {
        return new FieldLevelFormValidator(Arrays.asList(validations));
    }
    
    interface FieldLevelErrorDefinitionBuilder {
        void add(ValidationMessage... messages);
    }
    
    private FieldLevelFormValidator(List<FieldValidation> validationList) {
        this.validationList = Collections.unmodifiableList(validationList);
    }
    
    void validate(final Iterator<Map.Entry<IpiToolField<?>, String>> fieldIterator, final ErrorDefinitionBuilder builder) {
        
        final HashSet<FieldValidation> skipFailSet = new HashSet<FieldValidation>();
        
        class LocalBuilder implements FieldLevelErrorDefinitionBuilder {
            
            private IpiToolField<?> field;
            private FieldValidation validation;
            
            private void setCurrentField(IpiToolField<?> field) { this.field = field; }
            private void setCurrentValidation(FieldValidation validation) { this.validation = validation; }
            
            @Override
            public void add(ValidationMessage... messages) { builder.add(field, messages); skipFailSet.add(validation); }
            
        }
        
        final LocalBuilder localBuilder = new LocalBuilder();
        
        while (fieldIterator.hasNext()) {
            
            final Map.Entry<IpiToolField<?>, String> entry = fieldIterator.next();
            localBuilder.setCurrentField(entry.getKey());
            
            for (final FieldValidation validation : validationList) {
                
                if (Collections.disjoint(skipFailSet, validation.getDependencies())) {
                    
                    localBuilder.setCurrentValidation(validation);
                    validation.validate(entry.getValue(), entry.getKey().getFieldSpec(), localBuilder);
                    
                } else {
                    
                    skipFailSet.add(validation);
                    
                }
            }
            
            skipFailSet.clear();
            
        }
        
    }
    
}