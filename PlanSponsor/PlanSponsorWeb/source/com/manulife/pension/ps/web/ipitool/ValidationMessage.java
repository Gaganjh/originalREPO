package com.manulife.pension.ps.web.ipitool;

enum ValidationMessage {
    
    ASSET_AND_OTHER_RM(11, 1612),
    FIELD_FORMAT(12, 1613),
    BAC_CALCULATION(13, 1614),
    DI_CALCULATION(14, 1615),
    NEGATIVE_SUMMARY(15, 1616),
    NONZERO_ANNUALIZED_TPA_ABF(16, 1621);
    
    private final int order;
    private final int key;
    private ValidationMessage(int order, int key) { this.order = order; this.key = key; }
    int getOrder() { return order; }
    int getKey() { return key; }
    
}
