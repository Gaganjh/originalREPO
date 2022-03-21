package com.manulife.pension.ps.web.census.util;

import com.manulife.pension.service.employee.util.EmployeeValidationError;
import com.manulife.pension.service.employee.util.EmployeeData.Property;

/**
 * Defines the interface to allow AbstractEmployeeErrorUtil
 * to determine if the EmployeeValidationError should be included
 * 
 * @author guweigu
 *
 */
public interface ValidationErrorFilter {
    /**
     * Returns true if it determins the error should be included.
     *         false if the error should be excluded
     * @param error The validation error.
     * @return
     */
    boolean shouldInclude(Property field, EmployeeValidationError error);
}
