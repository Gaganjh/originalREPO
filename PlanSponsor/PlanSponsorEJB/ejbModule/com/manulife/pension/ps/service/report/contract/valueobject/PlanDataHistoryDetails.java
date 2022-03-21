package com.manulife.pension.ps.service.report.contract.valueobject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.contract.util.PlanConstants;
import com.manulife.pension.service.contract.util.PlanUtils;
import com.manulife.pension.service.contract.valueobject.PlanDataHistoryVO;

public class PlanDataHistoryDetails implements Serializable {
    private static final long serialVersionUID = -7097200523257424820L;
    
    private Integer planId = new Integer(0);
    private Timestamp createdTs = null;
    private String userId = "";
    private String userIdType = "";
    private String channelCode = "";
    private Integer sequenceNo = new Integer(0);
    private String fieldName = "";
    //private String oldValue = "";
    private String newValue = "";
    
    /**
     * @return the channelCode
     */
    public String getChannelCode() {
        return channelCode;
    }

    /**
     * @param channelCode the channelCode to set
     */
    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * @return the newValue
     */
    public String getNewValue() {
        return newValue;
    }

    /**
     * @param newValue the newValue to set
     */
    public void setNewValue(String newValue) {
        try {
            if (!StringUtils.isBlank(newValue)) {
                if (Arrays.asList(PlanDataHistoryVO.intFields).contains(this.getFieldName())) {
                    newValue = PlanConstants.formatIntegerFormatter(PlanUtils.s2int(newValue));
                } else if (Arrays.asList(PlanDataHistoryVO.ageFields).contains(this.getFieldName())) {
                    newValue = PlanConstants.formatAgeFormatter(PlanUtils.s2decimal(newValue));
                } else if (Arrays.asList(PlanDataHistoryVO.amtFields).contains(this.getFieldName())) {
                    newValue = "$" + PlanConstants.formatAmountFormatter(PlanUtils.s2decimal(newValue));
                } else if (Arrays.asList(PlanDataHistoryVO.pctFields).contains(this.getFieldName())) {
                    newValue = PlanConstants.formatBigDecimalPercentageFormatter(PlanUtils.s2decimal(newValue)) + "%";
                } else {}
                
                if (PlanDataHistoryVO.FIELD_LOANS_MAXIMUM_NUMBER_OUTSTANDING_ALLOWED.equalsIgnoreCase(this.getFieldName())) {
                    if (StringUtils.trim(newValue).equals(PlanConstants.EMPTY_CODE)) {
                        newValue = "";
                    } else if (StringUtils.trim(newValue).equals(PlanConstants.UNLIMITED_CODE)) {
                        newValue = PlanConstants.UNLIMITED_TEXT;
                    } else {}
                }
            }
        } catch (Exception e) {
            //should not happen
        }
        
        this.newValue = newValue;
    }

    /**
     * @return the oldValue
     */
//    public String getOldValue() {
//        return oldValue;
//    }

    /**
     * @param oldValue the oldValue to set
     */
//    public void setOldValue(String oldValue) {
//        this.oldValue = oldValue;
//    }

    /**
     * @return the sequenceNo
     */
    public Integer getSequenceNo() {
        return sequenceNo;
    }

    /**
     * @param sequenceNo the sequenceNo to set
     */
    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    /**
     * @return the createdTs
     */
    public Timestamp getCreatedTs() {
        return createdTs;
    }

    /**
     * @param createdTs the createdTs to set
     */
    public void setCreatedTs(Timestamp createdTs) {
        this.createdTs = createdTs;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the userIdType
     */
    public String getUserIdType() {
        return userIdType;
    }

    /**
     * @param userIdType the userIdType to set
     */
    public void setUserIdType(String userIdType) {
        this.userIdType = userIdType;
    }

    /**
     * @return the planId
     */
    public Integer getPlanId() {
        return planId;
    }

    /**
     * @param planId the planId to set
     */
    public void setPlanId(Integer planId) {
        this.planId = planId;
    }
}
