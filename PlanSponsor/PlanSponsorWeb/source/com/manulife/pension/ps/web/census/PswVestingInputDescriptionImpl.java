package com.manulife.pension.ps.web.census;

import java.util.Date;

import com.manulife.pension.service.vesting.LastUpdatedDetail;
import com.manulife.pension.service.vesting.VestingInputDescription;
import com.manulife.pension.service.vesting.VestingRetriever;

public class PswVestingInputDescriptionImpl implements VestingInputDescription {

    private static final long serialVersionUID = 1L;
    private final Integer parameter;
    
    public PswVestingInputDescriptionImpl(Integer parameter) {
        this.parameter = parameter;
    }
    
    public Boolean getBooleanValue() {
        return null;
    }

    public int getCalculationUsage() {
        return VestingRetriever.PARAMETER_USAGE_CODE_UNUSED;
    }

    public Date getDateValue() {
        return null;
    }

    public Date getEffectiveDate() {
        return null;
    }

    public Integer getIntegerValue() {
        return null;
    }

    public LastUpdatedDetail getLastUpdatedDetail() {
        return null;
    }

    public Object getObjectValue() {
        return null;
    }

    public int getParameterName() {
        return parameter;
    }
    
    /**
     * Returns String.  Doesn't matter which type is returned since all value accessors return null.
     */
    public int getParameterType() {
        return VestingInputDescription.PARAMETER_TYPE_STRING;
    }

    public String getStringValue() {
        return null;
    }
}
