package com.manulife.pension.ps.web.census.util;

import java.util.ArrayList;
import java.util.Collection;

import com.manulife.pension.validator.ValidationError;

/**
 * Utility class used to display errors/warnings in Employee snapshot pages
 * 
 * @author guweigu
 *
 */
public class EmployeeSnapshotValidationErrors extends AbstractValidationErrors {

    private static final long serialVersionUID = 2000894457471047849L;

    public EmployeeSnapshotValidationErrors(Collection<ValidationError> errors) {
        setErrors(new ArrayList<ValidationError>());
        
        for (ValidationError error: errors) {
            // similar ssn should not be added into this list
            // it shows in separate box
            if (error.getErrorCode() != CensusErrorCodes.SimilarSSN) {
                getErrors().add(error);
            }
        }
    }
}
