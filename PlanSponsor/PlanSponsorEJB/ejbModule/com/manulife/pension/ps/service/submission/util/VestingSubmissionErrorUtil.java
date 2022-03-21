package com.manulife.pension.ps.service.submission.util;


import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.submission.SubmissionError;


public class VestingSubmissionErrorUtil {
    private static VestingSubmissionErrorUtil instance = null;

    

    public static VestingSubmissionErrorUtil getInstance() {
        if (instance == null) {
            synchronized (VestingSubmissionErrorUtil.class) {
                instance = new VestingSubmissionErrorUtil();
            }
        }
        return instance;
    }

    // order of fields in which errors should be displayed
    protected final String[] errorDisplayOrder = {
            SubmissionErrorHelper.SSN_FIELD_KEY,
            SubmissionErrorHelper.EMPLOYEE_NUMBER_FIELD_KEY,
            SubmissionErrorHelper.FIRST_NAME_FIELD_KEY,
            SubmissionErrorHelper.LAST_NAME_FIELD_KEY,
            SubmissionErrorHelper.MIDDLE_INITIAL_FIELD_KEY,
            SubmissionErrorHelper.VESTING_DATE_KEY,
            SubmissionErrorHelper.VESTED_YEARS_OF_SERVICE_EFF_DATE_KEY,
            SubmissionErrorHelper.VESTING_PERCENTAGE_KEY,
            SubmissionErrorHelper.VESTED_YEARS_OF_SERVICE_KEY,
            SubmissionErrorHelper.APPLY_LTPT_CREDITING_KEY};

    
    public String[] getFieldsByErrorDisplayOrder() {
        return errorDisplayOrder;
    }

 
    /**
     * Sort the validation errors based on the defined errorDisplayOrder
     * 
     * @param errors
     * @return
     */
    public List<SubmissionError> sort(List<SubmissionError> errors) {
        if (errors == null || errors.size() == 0) {
            return errors;
        }
        List<SubmissionError> sortedList = new ArrayList<SubmissionError>(errors.size());
        
        for (String fieldKey : getFieldsByErrorDisplayOrder()) {
            
            for (SubmissionError error : errors) {
                if (error.getField().equals(SubmissionErrorHelper.getFieldLabel(fieldKey))) {
                    sortedList.add(error);
                }
            }
        }
        return sortedList;
    }

 
}
