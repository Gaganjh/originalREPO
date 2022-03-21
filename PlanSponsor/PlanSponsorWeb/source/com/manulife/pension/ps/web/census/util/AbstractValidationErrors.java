package com.manulife.pension.ps.web.census.util;

import java.io.Serializable;
import java.util.List;

import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;

/**
 * Base class for utility classes used to display errors/warnings
 * 
 * @author patuadr
 *
 */
public abstract class AbstractValidationErrors implements Serializable {
    private List<ValidationError> errors;
   
    /**
     * Returns just the number of errors
     * 
     * @return
     */
    public int getNumOfErrors() {
        int i = 0;
        for (ValidationError error: errors) {
            if (error.getType().compareTo(Type.error) == 0) {
                i++;
            }
        }
        return i;
    }
    
    /**
     * Returns just the number of warnings
     * 
     * @return
     */
    public int getNumOfWarnings() {
        int i = 0;
        for (ValidationError error: errors) {
            if (error.getType().compareTo(Type.warning) == 0) {
                i++;
            }
        }
        return i;
    }
    
    public int getNumOfAlerts() {
        int i = 0;
        for (ValidationError error: errors) {
            if (error.getType().compareTo(Type.alert) == 0) {
                i++;
            }
        }
        return i;
    }
    
    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }    
    
}
