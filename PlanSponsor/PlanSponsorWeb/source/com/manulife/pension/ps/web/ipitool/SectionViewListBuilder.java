package com.manulife.pension.ps.web.ipitool;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.manulife.pension.ps.web.ipitool.IpiToolField.FieldSpec;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolJhChargeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolRecoveryMethodField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolSalesAndServiceChargeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolStatementTypeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolTrusteeChargesField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolSummaryField;
import com.manulife.pension.service.fee.util.Constants.PaymentTypes;

public class SectionViewListBuilder {
    
    private enum Section {
        
        RECOVERY_METHODS(0, "Recovery Methods", IpiToolRecoveryMethodField.values(), IpiToolStatementTypeField.values()),
        JH_CHARGES(1, "John Hancock Charges", IpiToolJhChargeField.values()),
        SALES_SERVICE_CHARGES(2, "Sales & Service Charges", IpiToolSalesAndServiceChargeField.values()),
        TRUSTEE_CHARGES(3, "Trustee Charges", IpiToolTrusteeChargesField.values()),
        SUMMARY_CHARGES(4, "Summary of New IPI Charges", IpiToolSummaryField.values());
        
        private final int order;
        private final String label;
        private final List<IpiToolField<?>> fields;
        
        private Section(int order, String label, IpiToolField<?>[]... fieldArrays) {
            
            this.order = order;
            this.label = label;
            
            final ArrayList<IpiToolField<?>> innerList = new ArrayList<IpiToolField<?>>();
            
            for (IpiToolField<?>[] fieldArray : fieldArrays) {
                innerList.addAll(Arrays.asList(fieldArray));
            }
            
            Collections.sort(
                    innerList,
                    new Comparator<IpiToolField<?>>() {
                        @Override
                        public int compare(IpiToolField<?> object1, IpiToolField<?> object2) {
                            return object1.getFieldSpec().getOrder() - object2.getFieldSpec().getOrder();
                        }
                    });
            
            innerList.trimToSize();
            fields = Collections.unmodifiableList(innerList);
            
        }
    }
    
    private static final List<Section> SECTION_LIST;
    static {
        
        final List<Section> innerList = Arrays.asList(Section.values());
        Collections.sort(
                innerList,
                new Comparator<Section>() {
                    @Override
                    public int compare(Section object1, Section object2) {
                        return object1.order - object2.order;
                    }
                });
        SECTION_LIST = Collections.unmodifiableList(innerList);
        
    }
    
    private static class ImmutableSectionView
    implements SectionView {
        private static final long serialVersionUID = 1L;
        private final String sectionName;
        private final List<FieldView> fields;
        private ImmutableSectionView(String sectionName, ArrayList<FieldView> fields) {
            this.sectionName = sectionName;
            fields.trimToSize();
            this.fields = Collections.unmodifiableList(fields);
        }
        @Override
        public String getSectionName() { return sectionName; }
        @Override
        public Iterable<FieldView> getFields() { return fields; }
    }
    
    private static class ImmutableFieldView
    implements FieldView {
        private static final long serialVersionUID = 1L;
        private final String labelText;
        private final String unit;
        private final int scale;
        private final String formName;
        private final String currentValue;
        private final String inputValue;
        private final Set<String> recoveryValues;
        private ImmutableFieldView(
                String labelText,
                String unit,
                int scale,
                String formName,
                String currentValue,
                String inputValue,
                Set<String> recoveryValues) {
            this.labelText = labelText;
            this.unit = unit;
            this.scale = scale;
            this.formName = formName;
            this.currentValue = currentValue;
            this.inputValue = inputValue;
            this.recoveryValues = recoveryValues;
        }
        @Override
        public String getLabelText() { return labelText; }
        @Override
        public String getUnit() { return unit; }
        @Override
        public int getScale() { return scale; }
        @Override
        public String getCurrentValue() { return currentValue; }
        @Override
        public String getInputValue() { return inputValue; }
		@Override
		public String getFormName() {	return formName;  }
		@Override
		public Set<String> getRecoveryValues() { return recoveryValues; }
    }
    
    private final HashMap<IpiToolField<?>, String> hypoValueMap = new HashMap<IpiToolField<?>, String>();
    private final HashMap<IpiToolField<?>, Object> currentValueMap = new HashMap<IpiToolField<?>, Object>();
    
    <T> void putCurrentValue(IpiToolField<T> field, T currentValue) {
    	currentValueMap.put(field, currentValue);
    };
    
    <T> void putInputValue(IpiToolField<T> field, String inputValue) {
        
        final String oldValue = hypoValueMap.put(field, inputValue);
        if (oldValue != null) {
            throw new IllegalStateException("Value for key " + field.toString()
                    + " already set: " + inputValue.toString());
        }
        
    }
    
    // make sure sections and fields are in the proper order as defined by
    // section and FieldSpec.getOrder
    List<SectionView> build() {
        
        final ArrayList<SectionView> sectionViewList = new ArrayList<SectionView>();
        
        for (Section section : SECTION_LIST) {
            
            final ArrayList<FieldView> innerFieldViewList = new ArrayList<FieldView>();
            for (final IpiToolField<?> fieldSpec : section.fields) {
            	
                FieldSpec spec = fieldSpec.getFieldSpec() ;
            	final Object currentValue = currentValueMap.get(fieldSpec);
            	
                if (currentValue != null) {
                	
                    final String hypoValue = hypoValueMap.get(fieldSpec);
                    
                    innerFieldViewList.add(
                            new ImmutableFieldView(
                             		spec.getLabelText(), 
                             		spec.getUnit(),
                             		spec.getScale(),
                             		spec.getFormName(), 
                             		fieldSpec.format(currentValue),
                             		hypoValue, spec.getValues()));
                    
                }
            }
            
            innerFieldViewList.trimToSize();
            
            if (! innerFieldViewList.isEmpty()) {
                
                sectionViewList.add(
                        new ImmutableSectionView(
                                section.label,
                                innerFieldViewList));
                
            }
            
        }
        
        return Collections.unmodifiableList(sectionViewList);
        
    }
    
}
