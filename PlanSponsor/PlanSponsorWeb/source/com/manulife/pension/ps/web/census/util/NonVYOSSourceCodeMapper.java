package com.manulife.pension.ps.web.census.util;

/**
 * Mapper class to map source channel codes for non-vesting fields 
 * (EmploymentStatus, HireDate and PlanYTDHoursWorked)
 * to new codes that are used by UI 
 * 
 * @author maceadia
 */
public class NonVYOSSourceCodeMapper extends
        BaseEmployeeVestingParamSourceCodeMapper {

    private static final long serialVersionUID = 1L;

    @Override
    protected void populateMap() {
        getSourceCodeMap().put("IF", "CF");
        getSourceCodeMap().put("FL", "CF");
        getSourceCodeMap().put("RE", "CF");
        getSourceCodeMap().put("AL", "CF");
    }
}
