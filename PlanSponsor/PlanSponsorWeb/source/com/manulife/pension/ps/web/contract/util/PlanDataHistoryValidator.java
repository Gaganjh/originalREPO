package com.manulife.pension.ps.web.contract.util;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.contract.PlanDataHistoryForm;
import com.manulife.pension.service.contract.util.PlanUtils;
import com.manulife.pension.validator.ValidationError;

public class PlanDataHistoryValidator {
    public static final int ERR_INVALID_DATE_FORMAT = 1333;
    public static final int ERR_INVALID_FROM_TO = 1334;
    
    public static final String FROM_DATE = "fromDate";
    public static final String TO_DATE = "toDate";
    
    public static final String FROM_TEXT = "From";
    public static final String TO_TEXT = "To";

    public void validate(PlanDataHistoryForm form, List<ValidationError> errors) throws Exception {
        String fromDateStr = form.getFromDate().trim();
        if (!StringUtils.isBlank(fromDateStr)) {
            if (!PlanUtils.isValidDate(fromDateStr)) {
                errors.add(new ValidationError(FROM_DATE, ERR_INVALID_DATE_FORMAT, new Object[]{FROM_TEXT}));
            }
        }

        String toDateStr = form.getToDate().trim();
        if (!StringUtils.isBlank(toDateStr)) {
            if (!PlanUtils.isValidDate(toDateStr)) {
                errors.add(new ValidationError(TO_DATE, ERR_INVALID_DATE_FORMAT, new Object[]{TO_TEXT}));
            }
        }
        
        if (errors.isEmpty()) {
            Date fromDate = PlanUtils.s2d(fromDateStr);
            Date toDate = PlanUtils.s2d(toDateStr);
            if (toDate.before(fromDate)) {
                errors.add(new ValidationError(FROM_DATE, ERR_INVALID_FROM_TO));
            }
        }
    }
}
