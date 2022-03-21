package com.manulife.pension.ps.web.ipitool;

import java.io.Serializable;

public interface SectionView
extends Serializable {
    
    String getSectionName();
    Iterable<FieldView> getFields();
    
}
