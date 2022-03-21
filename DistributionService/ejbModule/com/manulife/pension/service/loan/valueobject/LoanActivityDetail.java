package com.manulife.pension.service.loan.valueobject;

import java.sql.Timestamp;

import com.manulife.pension.service.distribution.valueobject.ActivityDetail;

public class LoanActivityDetail implements ActivityDetail {

    private Integer itemNumber;

    private Timestamp lastUpdated;

    private Integer lastUpdatedById;

    private String typeCode;

    private String value = null;

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getLastUpdatedById() {
        return lastUpdatedById;
    }

    public void setLastUpdatedById(Integer lastUpdatedById) {
        this.lastUpdatedById = lastUpdatedById;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
