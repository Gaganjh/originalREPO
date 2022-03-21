package com.manulife.pension.ps.web.census.util;

/**
 * Mapper class to map source channel codes for vesting fields (VYOS) 
 * to new codes that are used by UI
 * 
 * @author maceadia
 */
public class VYOSSourceCodeMapper extends
        BaseEmployeeVestingParamSourceCodeMapper {

    private static final long serialVersionUID = 1L;

    @Override
    protected void populateMap() {
        getSourceCodeMap().put("IF", "VF");
        getSourceCodeMap().put("FL", "VF");
        getSourceCodeMap().put("RE", "VF");
        getSourceCodeMap().put("AL", "VF");
    }
}
