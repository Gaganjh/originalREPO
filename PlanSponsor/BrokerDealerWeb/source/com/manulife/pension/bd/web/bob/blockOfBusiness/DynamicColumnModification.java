package com.manulife.pension.bd.web.bob.blockOfBusiness;

import java.util.Collection;

import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportData;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessSummaryVO;

enum DynamicColumnModification {
    
    INSTANCE;
    
    static interface ColumnModifier {
        void modify(String column, boolean execute);
    }
    
    void override(final ColumnModifier modifier, final Collection<BlockOfBusinessReportData> reportDataList) {
        
        boolean isPreSignatureContractInBoB = false;
        boolean isContractWithRiaInBoB = false;
        
        for (final BlockOfBusinessReportData reportData : reportDataList) {
            
            if (reportData == null) {
                continue;
            }
            
            final BlockOfBusinessSummaryVO summary = reportData.getBobSummaryVO();
            if (summary != null) {
                
                if (summary.getHasPresigContract()) {
                    isPreSignatureContractInBoB = true;
                }
                
                if (summary.getHasContractWithRiaAssocaited()) {
                    isContractWithRiaInBoB = true;
                }
                
            }
            
        }
        
        modifier.modify(BlockOfBusinessReportData.COMMISSION_ASSET_ALL_YRS, ! isPreSignatureContractInBoB);
        modifier.modify(BlockOfBusinessReportData.COL_COMMISSIONS_RIA_FEES_ID, ! isContractWithRiaInBoB);
        
   }
   
}
