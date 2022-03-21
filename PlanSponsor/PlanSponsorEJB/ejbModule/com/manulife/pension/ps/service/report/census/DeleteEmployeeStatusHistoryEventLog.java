package com.manulife.pension.ps.service.report.census;


import com.manulife.pension.util.log.EventLog;
import com.manulife.pension.util.log.LogEventException;

/**
 * Event Log implementation for Census (Employee) data update
 * event.
 * 
 * @author guweigu
 *
 */
public class DeleteEmployeeStatusHistoryEventLog extends EventLog {
    private static String EventName = "DeleteEmployeeStatusHistoryEventLog";
    
    private String applicationId;
    private String CONTRACT_NUMBER ="CONTRACT_NUMBER";
    private String PROFILE_ID ="PROFILE_ID";
    private String TOTAL_RECORDS ="TOTAL_RECORDS";
    private String EFFECTIVE_DATE ="EFFECTIVE_DATE";
    private String STATUS ="STATUS";
    private String LAST_UPDATED_ID ="LAST_UPDATED_ID";
    private String LAST_UPDATED_TYPE ="LAST_UPDATED_TYPE";
    private String LAST_UPDATED_TS ="LAST_UPDATED_TS";
    private String SOURCE_CHANNEL = "SOURCE_CHANNEL"; 
    
    public DeleteEmployeeStatusHistoryEventLog() {
        super(DeleteEmployeeStatusHistoryEventLog.class);     
    }

    public DeleteEmployeeStatusHistoryEventLog(String applicationId) {
        super(DeleteEmployeeStatusHistoryEventLog.class);
    }

    protected String getApplicationId() throws LogEventException {
        return applicationId;
    }

    protected String getEventName() {
        return EventName;
    }

    protected String prepareLogData() throws LogEventException {
        StringBuffer data = new StringBuffer();
        
        data.append("PRINCIPAL_USERNAME:").append(getPrincipalUserName());
        data.append(",PRINCIPAL_ROLE:").append(getPrincipalRole());
        data.append(",UPDATED_TS:").append(getUpdatedTS());
        
        data.append(",").append(CONTRACT_NUMBER).append(":").append(getParameter(CONTRACT_NUMBER));
        data.append(",").append(PROFILE_ID).append(":").append(getParameter(PROFILE_ID));
        data.append(",").append(TOTAL_RECORDS).append(":").append(getParameter(TOTAL_RECORDS));
        data.append(",").append(EFFECTIVE_DATE).append(":").append(getParameter(EFFECTIVE_DATE));
        data.append(",").append(STATUS).append(":").append(getParameter(STATUS));
        data.append(",").append(LAST_UPDATED_TS).append(":").append(getParameter(LAST_UPDATED_TS));
        data.append(",").append(SOURCE_CHANNEL).append(":").append(getParameter(SOURCE_CHANNEL));
        data.append(",").append(LAST_UPDATED_ID).append(":").append(getParameter(LAST_UPDATED_ID));
        data.append(",").append(LAST_UPDATED_TYPE).append(":").append(getParameter(LAST_UPDATED_TYPE));
       
        return data.toString();     
    }

    /**
     * This method overrides the one in EventLog to get rid of the username validation.
     * 
     * @throws LogEventException 
     *      If any required information is missing.
     */
    protected void validateAll() throws LogEventException
    {
        if ( getClassName().length() == 0)
            throw new LogEventException("className should not be empty");
        else if ( getMethodName().length() == 0)
            throw new LogEventException("methodName should not be empty");

        validate();
    }
    
    protected void validate() throws LogEventException {
        if ( !logInfoExists(CONTRACT_NUMBER) )
            throw new LogEventException("Contract Number of the participant has not been set.");
        else if ( !logInfoExists(PROFILE_ID))
            throw new LogEventException("ProfileId has not been set.");
    }

}
