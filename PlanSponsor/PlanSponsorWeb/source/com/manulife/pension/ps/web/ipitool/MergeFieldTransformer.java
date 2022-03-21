package com.manulife.pension.ps.web.ipitool;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolJhChargeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolRecoveryMethodField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolSalesAndServiceChargeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolStatementTypeField;
import com.manulife.pension.ps.web.ipitool.IpiToolField.IpiToolTrusteeChargesField;

enum MergeFieldTransformer {
    
    INSTANCE;
   //return Map that uses input field value if it exists;  otherwise current field value
    Map<IpiToolField<?>, String> transform(Map<IpiToolField<?>, String> currentFieldMap, Map<IpiToolField<?>, String> inputFieldMap) {
    
         Map<IpiToolField<?>, String> map = new HashMap<IpiToolField<?>, String>();
         for(IpiToolRecoveryMethodField ipi: IpiToolRecoveryMethodField.values()){
        	 
        	 if(StringUtils.isNotBlank(inputFieldMap.get(ipi))){
	        	 map.put(ipi, inputFieldMap.get(ipi));
	         } else if(currentFieldMap.get(ipi) != null){
	        	 map.put(ipi, currentFieldMap.get(ipi));
	         }
         }
         for(IpiToolStatementTypeField ipi: IpiToolStatementTypeField.values()){
        	 
        	 if(StringUtils.isNotBlank(inputFieldMap.get(ipi))){
	        	 map.put(ipi, inputFieldMap.get(ipi));
	         } else if(currentFieldMap.get(ipi) != null){
	        	 map.put(ipi, currentFieldMap.get(ipi));
	         }
         }
         
         for(IpiToolJhChargeField ipcf: IpiToolJhChargeField.values()){
        	 if(StringUtils.isNotBlank(inputFieldMap.get(ipcf))){
	        	 map.put(ipcf, inputFieldMap.get(ipcf));
	         } else if(currentFieldMap.get(ipcf) != null){
	        	 map.put(ipcf, currentFieldMap.get(ipcf));
	         }
         }
        
         for(IpiToolSalesAndServiceChargeField ipsc: IpiToolSalesAndServiceChargeField.values()){
        	 if(StringUtils.isNotBlank(inputFieldMap.get(ipsc))){
	        	 map.put(ipsc, inputFieldMap.get(ipsc));
	         } else if(currentFieldMap.get(ipsc) != null){
	        	 map.put(ipsc, currentFieldMap.get(ipsc));
	         }
         }
         
         for(IpiToolTrusteeChargesField iptc: IpiToolTrusteeChargesField.values()){
        	 if(StringUtils.isNotBlank(inputFieldMap.get(iptc))){
	        	 map.put(iptc, inputFieldMap.get(iptc));
	         } else if(currentFieldMap.get(iptc) != null){
	        	 map.put(iptc, currentFieldMap.get(iptc));
	         }
         }
   
         return  map;
   }
    
}
