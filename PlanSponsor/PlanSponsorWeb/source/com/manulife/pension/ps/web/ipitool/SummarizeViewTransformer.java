package com.manulife.pension.ps.web.ipitool;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolSummaryField;
import com.manulife.pension.ps.web.ipitool.MapTransformer.Builder;
import com.manulife.pension.service.fee.valueobject.AdministrativeExpense;

enum SummarizeViewTransformer {
    
    INSTANCE;
    
    private static class CurrentValueWrapper
    implements Builder<BigDecimal, IpiToolSummaryField> {
        private final SectionViewListBuilder innerBuilder;
        private CurrentValueWrapper(SectionViewListBuilder innerBuilder) { this.innerBuilder = innerBuilder; }
        @Override
        public void build(BigDecimal input, IpiToolSummaryField spec) {
            innerBuilder.putCurrentValue(spec, input);
        }
    }
    
    private static class ChangedValueWrapper
    implements Builder<BigDecimal, IpiToolSummaryField> {
        private final SectionViewListBuilder innerBuilder;
        private ChangedValueWrapper(SectionViewListBuilder innerBuilder) { this.innerBuilder = innerBuilder; }
        @Override
        public void build(BigDecimal input, IpiToolSummaryField spec) {
            innerBuilder.putInputValue(spec, input.toPlainString());
        }
    }
    
    private static final MapTransformer<IpiToolSummaryField, AdministrativeExpense, BigDecimal> INNER_TRANSFORMER =
            new MapTransformer<IpiToolSummaryField, AdministrativeExpense, BigDecimal>(
                    Arrays.asList(IpiToolSummaryField.values()),
                    new MapTransformer.FieldAttribute<IpiToolSummaryField, AdministrativeExpense>() {
                        @Override
                        public AdministrativeExpense getAttribute(IpiToolSummaryField spec) {
                            return spec.getSpec().getCalculationKey();
                        }

                    });
    
                   
    List<SectionView> transform(Map<AdministrativeExpense, BigDecimal> currentSummary, Map<AdministrativeExpense, BigDecimal> changedSummary, Map<IpiToolField<?>, String> currentFieldMap, Map<IpiToolField<?>, String> inputFieldMap, SectionViewListBuilder builder) {
        
        INNER_TRANSFORMER.transform(currentSummary, new CurrentValueWrapper(builder));
        INNER_TRANSFORMER.transform(changedSummary, new ChangedValueWrapper(builder));
       
        return builder.build();
        
    }
    
}
