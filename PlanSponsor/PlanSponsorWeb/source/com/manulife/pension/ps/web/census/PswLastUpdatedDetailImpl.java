package com.manulife.pension.ps.web.census;

import java.sql.Timestamp;

import com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo.VestingType;
import com.manulife.pension.service.vesting.LastUpdatedDetail;

public class PswLastUpdatedDetailImpl implements LastUpdatedDetail {
    
    private static final long serialVersionUID = 1L;
    
    private final Timestamp timestamp;
    private final String userId;
    private final String userType;
    private final String channel;
    private final String source;
        
    
    public PswLastUpdatedDetailImpl(
            Timestamp timestamp,
            String userId,
            String userType,
            String channel, 
            String source) {
        this.timestamp = timestamp;
        this.userId = userId;
        this.userType = userType;
        this.channel = channel;
        this.source = source;
    }
    
    public Timestamp getTimestamp() { return timestamp; }
    public String getUserId() { return userId; }
    public String getUserType() { return userType; }
    public String getChannel() { return channel; }
    public String getSource() { return source; }
    
}
