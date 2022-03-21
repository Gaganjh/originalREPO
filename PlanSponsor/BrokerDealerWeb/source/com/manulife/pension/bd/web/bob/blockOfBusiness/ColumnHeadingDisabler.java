package com.manulife.pension.bd.web.bob.blockOfBusiness;

import java.util.Map;

import com.manulife.pension.bd.web.bob.blockOfBusiness.DynamicColumnModification.ColumnModifier;

class ColumnHeadingDisabler
implements ColumnModifier {
    
    final Map<String, Boolean> headingSpec;
    
    ColumnHeadingDisabler(final Map<String, Boolean> headingSpec) { this.headingSpec = headingSpec; }
    
    @Override
    public void modify(String column, boolean execute) {
        
        if (execute
                && headingSpec.containsKey(column)
                && headingSpec.get(column).booleanValue()) {
            
            headingSpec.put(column, Boolean.FALSE);
            
        }
        
    }
    
}
