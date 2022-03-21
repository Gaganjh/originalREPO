package com.manulife.pension.ps.web.ipitool;

import java.io.Serializable;
import java.util.Set;

public interface FieldView
extends Serializable {
    
    String getLabelText();
    String getUnit(); // $ or %
    int getScale();
    String getFormName();
    String getCurrentValue();
    String getInputValue();
    Set<String> getRecoveryValues();
    
}
