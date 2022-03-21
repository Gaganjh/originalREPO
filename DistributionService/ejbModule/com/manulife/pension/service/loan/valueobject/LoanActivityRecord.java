package com.manulife.pension.service.loan.valueobject;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.service.distribution.valueobject.ActivityDetail;
import com.manulife.pension.service.loan.LoanField;
import com.manulife.pension.service.loan.LoanField.Type;

public class LoanActivityRecord extends BaseSerializableCloneableObject {

    private static final long serialVersionUID = 1L;

    private String systemOfRecordValue = null;
    
    private String savedValue = null;
    
    private String originalValue = null;
    
    private Integer itemNumber = null;
    
    private Integer subItemNumber = null;
    
    private String subItemName = null;
    
    private String fieldName = null;
    
    private String submittedByName = null;
    private String submittedByRole = null;
    private Timestamp submittedByTimestamp = null;

    private String changedByName = null;
    private String changedByRole = null;
    private Timestamp changedByTimestamp = null;

    private Timestamp systemOfRecordTimestamp = null;
    
    public LoanActivityRecord(Integer itemNumber, Integer subItemNumber) {
        super();
        this.itemNumber = itemNumber;
        this.subItemNumber = subItemNumber;
    }

    public void setValue(String value, String valueTypeCode) {
        if (valueTypeCode.equals(ActivityDetail.TYPE_SAVED)) {
            setSavedValue(value);
        } else if (valueTypeCode.equals(ActivityDetail.TYPE_SYSTEM_OF_RECORD)) {
            setSystemOfRecordValue(value);
        } else if (valueTypeCode.equals(ActivityDetail.TYPE_ORIGINAL)) {
            setOriginalValue(value);
        } else {
            throw new IllegalArgumentException("Unsupported valueTypeCode : " + valueTypeCode);
        }
    }

    public String getSystemOfRecordValue() {
        return systemOfRecordValue;
    }

    public void setSystemOfRecordValue(String systemOfRecordValue) {
        this.systemOfRecordValue = systemOfRecordValue;
    }

    public String getFormattedSystemOfRecordValue() {
        return formatForDisplay(this.systemOfRecordValue);
    }

    public String getSavedValue() {
        return savedValue;
    }

    public void setSavedValue(String savedValue) {
        this.savedValue = savedValue;
    }

    public String getFormattedSavedValue() {
        return formatForDisplay(this.savedValue);
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }
    
    public String getFormattedOriginalValue() {
        if (this.submittedByTimestamp != null) {
            // 'O' record exists for this field
            return formatForDisplay(this.originalValue);
        } else {    
            // 'O' record does not exists for this field
            return "No Value"; 
        }
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Integer getSubItemNumber() {
        return subItemNumber;
    }

    public void setSubItemNumber(Integer subItemNumber) {
        this.subItemNumber = subItemNumber;
    }

    public String getSubItemName() {
        return subItemName;
    }

    public void setSubItemName(String subItemName) {
        this.subItemName = subItemName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getSubmittedByName() {
        return submittedByName;
    }

    public void setSubmittedByName(String submittedByName) {
        this.submittedByName = submittedByName;
    }

    public String getSubmittedByRole() {
        return submittedByRole;
    }

    public void setSubmittedByRole(String submittedByRoleCode) {
        this.submittedByRole = submittedByRoleCode;
    }

    public Timestamp getSubmittedByTimestamp() {
        return submittedByTimestamp;
    }

    public void setSubmittedByTimestamp(Timestamp submittedByTimestamp) {
        this.submittedByTimestamp = submittedByTimestamp;
    }

    public String getChangedByName() {
        return changedByName;
    }

    public void setChangedByName(String changedByName) {
        this.changedByName = changedByName;
    }

    public String getChangedByRole() {
        return changedByRole;
    }

    public void setChangedByRole(String changedByRoleCode) {
        this.changedByRole = changedByRoleCode;
    }

    public Timestamp getChangedByTimestamp() {
        return changedByTimestamp;
    }

    public void setChangedByTimestamp(Timestamp changedByTimestamp) {
        this.changedByTimestamp = changedByTimestamp;
    }
    
    public String formatForDisplay(String aString) {
        if (aString == null){
            return null;
        }
        NumberFormat amountFormatter = NumberFormat.getNumberInstance();
        amountFormatter.setMinimumFractionDigits(2);
        String formattedString = null;
        LoanField.Type type = LoanField.getFieldFromItemNumber(this.itemNumber).getType();
        try {
            if (type.equals(LoanField.Type.AMOUNT)) {
                formattedString = "$" + amountFormatter.format(Double.parseDouble(aString));
            } else if (type.equals(LoanField.Type.PERCENT)) {
                formattedString = aString + "%";
            } else {
                formattedString = aString;
            }
        } catch (Exception e) {
            // If any problems converting, just return the input string value as is
            formattedString = aString;
        }
        return formattedString;
    }

    public String getTextAlignment() {
        LoanField.Type type = LoanField.getFieldFromItemNumber(this.itemNumber).getType();
        if (type.equals(LoanField.Type.AMOUNT) || 
                type.equals(LoanField.Type.PERCENT) ||
                type.equals(LoanField.Type.NUMBER)) {
            return "right";
        } else {
            return "left";
        }
    }
    
    private boolean isNumericValuesAreEqual(String number1, String number2) {
        BigDecimal number1BigD = 
            com.manulife.pension.util.NumberUtils.parseBigDecimal(
                    number1, true);
        if (number1BigD == null) {
            // Assume an invalid numeric to be zero.
            number1BigD = GlobalConstants.ZERO_AMOUNT;
        }
        BigDecimal number2BigD = 
            com.manulife.pension.util.NumberUtils.parseBigDecimal(
                    number2, true);
        if (number2BigD == null) {
            // Assume an invalid numeric to be zero.
            number2BigD = GlobalConstants.ZERO_AMOUNT;
        }
        if (number1BigD.compareTo(number2BigD) == 0) {
            return true;                
        } else {
            return false;
        }
    }
    
    public boolean isDisplaySystemOfRecordInfo() {
        Type fieldType = LoanField.getFieldFromItemNumber(getItemNumber())
        .getType();

        // If the field is numeric, do a numeric compare.
        if (Type.AMOUNT.equals(fieldType) || Type.PERCENT.equals(fieldType)) {
            if (isNumericValuesAreEqual(systemOfRecordValue, savedValue)) {
                return false;
            } else {
                return true;
            }
        } else {
            if (StringUtils.isBlank(this.systemOfRecordValue) ||
                    this.systemOfRecordValue.equals(this.savedValue)) {
                return false;
            }
            return true;
        }
    }    

    public boolean isDisplayOriginalInfo() {
        if (this.submittedByTimestamp == null || this.changedByTimestamp == null) {
            // Implies either no 'O' or no 'S' activity history record exists
            return true;
        }
        Type fieldType = LoanField.getFieldFromItemNumber(getItemNumber())
                .getType();
        
        // If the field is numeric, do a numeric compare.
        if (Type.AMOUNT.equals(fieldType) || Type.PERCENT.equals(fieldType)) {
            if (isNumericValuesAreEqual(originalValue, savedValue)) {
                return false;
            } else {
                return true;
            }
        } else {
            if (this.originalValue != null) {
                if (this.originalValue.equals(this.savedValue)) {
                    return false;
                }    
            } else if (this.savedValue == null) {
                return false;
            }
            return true;
        }
    }

    public boolean isDisplaySubmittedByInfo() {
        if (this.submittedByTimestamp == null) {
            // Implies no 'O' activity history record exists
            return false;
        }
        if (this.changedByTimestamp == null) {
            // Implies no 'S' activity history record exists
            return true;
        }
        Type fieldType = LoanField.getFieldFromItemNumber(getItemNumber())
        .getType();

        // If the field is numeric, do a numeric compare.
        if (Type.AMOUNT.equals(fieldType) || Type.PERCENT.equals(fieldType)) {
            if (isNumericValuesAreEqual(originalValue, savedValue)) {
                return false;
            } else {
                return true;
            }
        } else {
            if (this.originalValue != null) {
                if (this.originalValue.equals(this.savedValue)) {
                    return false;
                }    
            } else if (this.savedValue == null) {
                return false;
            }
            return true;
        }
    }
    
    public boolean isDisplayChangedByInfo() {
        if (this.submittedByTimestamp == null || this.changedByTimestamp == null) {
            // Implies either no 'O' or no 'S' activity history record exists
            return true;
        }
        Type fieldType = LoanField.getFieldFromItemNumber(getItemNumber())
        .getType();

        // If the field is numeric, do a numeric compare.
        if (Type.AMOUNT.equals(fieldType) || Type.PERCENT.equals(fieldType)) {
            if (isNumericValuesAreEqual(originalValue, savedValue)) {
                return false;
            } else {
                return true;
            }
        } else {
            if (this.originalValue != null) {
                if (this.originalValue.equals(this.savedValue)) {
                    return false;
                }    
            } else if (this.savedValue == null) {
                return false;
            }
            return true;
        }
    }

    public Timestamp getSystemOfRecordTimestamp() {
        return systemOfRecordTimestamp;
    }

    public void setSystemOfRecordTimestamp(Timestamp systemOfRecordTimestamp) {
        this.systemOfRecordTimestamp = systemOfRecordTimestamp;
    }

    public String getSubmittedByNameAndRole() {
        if (StringUtils.isNotBlank(this.submittedByName)) {
            return this.submittedByName + "- " + this.submittedByRole;
        } else {
            return "Unknown user";
        }
    }

    public String getChangedByNameAndRole() {
        if (StringUtils.isNotBlank(this.changedByName)) {
            return this.changedByName + "- " + this.changedByRole;
        } else {
            return "Unknown user";
        }
    }

}
