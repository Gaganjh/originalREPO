package com.manulife.pension.bd.web.bob.investment;

import com.manulife.pension.bd.web.bob.investment.DynamicColumnModification.ColumnModifier;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BOBColumnsApplicableToTab;

class ColumnValueOverrideEnabler
implements ColumnModifier {
    
    final BOBColumnsApplicableToTab columnSpec;
    
    ColumnValueOverrideEnabler(final BOBColumnsApplicableToTab columnSpec) { this.columnSpec = columnSpec; }
    
    @Override
    public void modify(final String column, final boolean execute) {
        
        if (columnSpec.getApplicableColumnsForTab().get(column).getDefaultEnabled()) {
            columnSpec.getApplicableColumnsForTab().get(column).setOverRideEnable(execute);
        }
        
    }
    
}
