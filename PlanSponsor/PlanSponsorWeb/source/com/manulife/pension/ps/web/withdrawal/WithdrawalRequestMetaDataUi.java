package com.manulife.pension.ps.web.withdrawal;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;

/**
 * @author kuthiha
 * 
 */
public class WithdrawalRequestMetaDataUi extends BaseSerializableCloneableObject {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private WithdrawalRequestMetaData metaData;

    private String originator;

    public WithdrawalRequestMetaDataUi(final WithdrawalRequestMetaData data, final String origin) {
        metaData = data;
        originator = origin;
    }

    public WithdrawalRequestMetaData getMetaData() {
        return metaData;
    }

    public String getOriginator() {
        return originator;
    }

}
