package com.manulife.pension.ps.web.ipitool;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolJhChargeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolRecoveryMethodField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolSalesAndServiceChargeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolStatementTypeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolTrusteeChargesField;
import com.manulife.pension.ps.web.ipitool.MapTransformer.Builder;
import com.manulife.pension.service.fee.util.Constants.ContractAdminFeeType;
import com.manulife.pension.service.fee.util.Constants.PaymentTypes;
import com.manulife.pension.service.fee.util.Constants.StatementType;

enum CurrentFieldRetrievalViewTransformer {
    
    INSTANCE;
    
   
    private static final MapTransformer<IpiToolRecoveryMethodField, ContractAdminFeeType, Object> RECOVERY_METHOD_TRANSFORMER =
        new MapTransformer<IpiToolRecoveryMethodField, ContractAdminFeeType, Object>(
                Arrays.asList(IpiToolRecoveryMethodField.values()),
                new MapTransformer.FieldAttribute<IpiToolRecoveryMethodField, ContractAdminFeeType>() {

					@Override
					public ContractAdminFeeType getAttribute(
							IpiToolRecoveryMethodField spec) {
						return spec.getSpec().getRetrievalKey();
					}
					
                });
    
    private static final MapTransformer<IpiToolStatementTypeField, ContractAdminFeeType, Object> STATEMENTTYPE_TRANSFORMER =
        new MapTransformer<IpiToolStatementTypeField, ContractAdminFeeType, Object>(
                Arrays.asList(IpiToolStatementTypeField.values()),
                new MapTransformer.FieldAttribute<IpiToolStatementTypeField, ContractAdminFeeType>() {

					@Override
					public ContractAdminFeeType getAttribute(
							IpiToolStatementTypeField spec) {
						return spec.getSpec().getRetrievalKey();
					}
					
                });
    
    
    private static final MapTransformer<IpiToolJhChargeField, ContractAdminFeeType, Object> JH_CHARGE_FIELD_TRANSFORMER =
        new MapTransformer<IpiToolJhChargeField, ContractAdminFeeType, Object>(
                Arrays.asList(IpiToolJhChargeField.values()),
                new MapTransformer.FieldAttribute<IpiToolJhChargeField, ContractAdminFeeType>() {
                  

					@Override
					public ContractAdminFeeType getAttribute(
							IpiToolJhChargeField spec) {
						return spec.getSpec().getRetrievalKey();
					}

                });
    
    
    private static final MapTransformer<IpiToolSalesAndServiceChargeField, ContractAdminFeeType, Object> SALES_AND_SERVICE_CHARGE_TRANSFORMER =
        new MapTransformer<IpiToolSalesAndServiceChargeField, ContractAdminFeeType, Object>(
                Arrays.asList(IpiToolSalesAndServiceChargeField.values()),
                new MapTransformer.FieldAttribute<IpiToolSalesAndServiceChargeField, ContractAdminFeeType>() {
                   

					@Override
					public ContractAdminFeeType getAttribute(
							IpiToolSalesAndServiceChargeField spec) {
						return spec.getSpec().getRetrievalKey();
					}

					
                });
    
    private static final MapTransformer<IpiToolTrusteeChargesField, ContractAdminFeeType, Object> TRUSTEE_CHARGE_TRANSFORMER =
        new MapTransformer<IpiToolTrusteeChargesField, ContractAdminFeeType, Object>(
                Arrays.asList(IpiToolTrusteeChargesField.values()),
                new MapTransformer.FieldAttribute<IpiToolTrusteeChargesField, ContractAdminFeeType>() {
                   

					@Override
					public ContractAdminFeeType getAttribute(
							IpiToolTrusteeChargesField spec) {
						return spec.getSpec().getRetrievalKey();
					}

					
                });
    
    
    // transform following fields:  IpiToolRecoveryMethodField, IpiToolJhChargeField, IpiToolSalesAndServiceChargeField
    // use InputFieldSpec.getRetrievalKey
    // use IpiToolSectionViewListBuilder to build the view
   List<SectionView> transform(Map<ContractAdminFeeType, Object> retrievedCurrentFields) {
       
    	 final SectionViewListBuilder builder = new SectionViewListBuilder();
    	 
    	 RECOVERY_METHOD_TRANSFORMER.transform(
    	         retrievedCurrentFields,
    	         new Builder<Object, IpiToolRecoveryMethodField>() {

                    @Override
                    public void build(Object input,
                            IpiToolRecoveryMethodField spec) {
                        builder.putCurrentValue(spec, (PaymentTypes)input);
                    }
    	             
    	         });
    	 
    	 STATEMENTTYPE_TRANSFORMER.transform(
    	         retrievedCurrentFields,
    	         new Builder<Object, IpiToolStatementTypeField>() {

                    @Override
                    public void build(Object input,
                    		IpiToolStatementTypeField spec) {
                        builder.putCurrentValue(spec, (StatementType)input);
                    }
    	             
    	         });
    	 
    	 
    	 JH_CHARGE_FIELD_TRANSFORMER.transform(
    	         retrievedCurrentFields,
    	         new Builder<Object, IpiToolJhChargeField>() {

                    @Override
                    public void build(Object input,
                    		IpiToolJhChargeField spec) {
                        builder.putCurrentValue(spec,(BigDecimal)input) ;
                    }
    	             
    	         });
         
    	 SALES_AND_SERVICE_CHARGE_TRANSFORMER.transform(
    	         retrievedCurrentFields,
    	         new Builder<Object, IpiToolSalesAndServiceChargeField>() {

                    @Override
                    public void build(Object input,
                    		IpiToolSalesAndServiceChargeField spec) {
                        builder.putCurrentValue(spec,(BigDecimal)input) ;
                    }
    	             
    	         });
    	 TRUSTEE_CHARGE_TRANSFORMER.transform(
    	         retrievedCurrentFields,
    	         new Builder<Object, IpiToolTrusteeChargesField>() {

                    @Override
                    public void build(Object input,
                    		IpiToolTrusteeChargesField spec) {
                        builder.putCurrentValue(spec,(BigDecimal)input) ;
                    }
    	             
    	         });
        
      
         return builder.build();
    }
}

