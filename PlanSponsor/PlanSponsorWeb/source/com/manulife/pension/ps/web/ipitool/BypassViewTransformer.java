package com.manulife.pension.ps.web.ipitool;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolJhChargeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolRecoveryMethodField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolSalesAndServiceChargeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolStatementTypeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolTrusteeChargesField;
import com.manulife.pension.service.fee.util.Constants.PaymentTypes;
import com.manulife.pension.service.fee.util.Constants.StatementType;


enum BypassViewTransformer {
    
    INSTANCE;
    
    // transform only following fields:  IpiToolRecoveryMethodField, IpiToolJhChargeField, IpiToolSalesAndServiceChargeField
    // do not transform IpiToolSummaryField
    // use SectionViewListBuilder to build the view  
    SectionViewListBuilder transform(Map<IpiToolField<?>, String> currentFieldMap, Map<IpiToolField<?>, String> inputFieldMap, SectionViewListBuilder builder) {
    	 
    	for(IpiToolRecoveryMethodField recoveryField : IpiToolRecoveryMethodField.values()){
    		 builder.putCurrentValue(recoveryField,PaymentTypes.getFromValue(currentFieldMap.get(recoveryField)));
        	 if(StringUtils.isNotBlank(inputFieldMap.get(recoveryField))){
        		 builder.putInputValue(recoveryField,inputFieldMap.get(recoveryField));	 
        	 }
     	 }
    	
    	 for(IpiToolStatementTypeField stmtField : IpiToolStatementTypeField.values()){
    		 builder.putCurrentValue(stmtField,StatementType.getFromValue(currentFieldMap.get(stmtField)));
    		 if(StringUtils.isNotBlank(inputFieldMap.get(stmtField))){
    			 builder.putInputValue(stmtField,inputFieldMap.get(stmtField));	 
    		 }
    	 }
   	 
    	 for(IpiToolJhChargeField jhField : IpiToolJhChargeField.values()){
    		 builder.putCurrentValue(jhField,new BigDecimal( currentFieldMap.get(jhField)));
    		 if(StringUtils.isNotBlank(inputFieldMap.get(jhField))){
    			builder.putInputValue(jhField,inputFieldMap.get(jhField));
    		 }
    	 }
    	 for(IpiToolSalesAndServiceChargeField salesField : IpiToolSalesAndServiceChargeField.values()){
    		 builder.putCurrentValue(salesField,new BigDecimal( currentFieldMap.get(salesField)));
    		 if(StringUtils.isNotBlank(inputFieldMap.get(salesField))){
    			 builder.putInputValue(salesField,inputFieldMap.get(salesField));
    		 }
         }
       
    	 for(IpiToolTrusteeChargesField trusteeField : IpiToolTrusteeChargesField.values()){
        	 builder.putCurrentValue(trusteeField, new BigDecimal( currentFieldMap.get(trusteeField)));
        	 if(StringUtils.isNotBlank(inputFieldMap.get(trusteeField))){
        	 builder.putInputValue(trusteeField,inputFieldMap.get(trusteeField));
        	 }
         }
    	 
         return builder;
    }
    
}
