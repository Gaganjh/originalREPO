package com.manulife.pension.ps.service.report.exception;

import java.sql.SQLException;
import com.manulife.pension.service.report.exception.ReportServiceException;

public class UncashedChecksDatabaseAccessException extends
        ReportServiceException {
    
    private static final String UNCASHED_CHECKS_ERROR_CODE = "1047";
    
    public UncashedChecksDatabaseAccessException(SQLException sqle, String daoClassName, String daoMethodName, String contractId) {
        super(sqle, daoClassName, daoMethodName, "contract " + contractId);
    }
    
    @Override
    public String getErrorCode() { return UNCASHED_CHECKS_ERROR_CODE; }
    
}
