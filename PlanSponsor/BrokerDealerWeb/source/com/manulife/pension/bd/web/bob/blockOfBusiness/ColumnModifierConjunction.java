package com.manulife.pension.bd.web.bob.blockOfBusiness;

import java.util.Set;

import com.manulife.pension.bd.web.bob.blockOfBusiness.DynamicColumnModification.ColumnModifier;

class ColumnModifierConjunction
implements ColumnModifier {
    
    final Set<ColumnModifier> modifiers;
    
    ColumnModifierConjunction(final Set<ColumnModifier> modifiers) { this.modifiers = modifiers; }
    
    @Override
    public void modify(final String column, final boolean execute) {
        
        for(final ColumnModifier modifier : modifiers) {
            
            modifier.modify(column, execute);
            
        }

    }

}
