package com.manulife.pension.ps.web.withdrawal;

import java.util.Collection;

import com.manulife.pension.validator.ValidationError;

/**
 * Defines an API for withdrawal UI object to translate business level errors to web errors.
 * 
 * @author Andrew Dick
 */
public interface WithdrawalMessageToValidationErrorTranslator {

    public String PROPERTY_SEPARATOR = ".";

    /**
     * Translates the contained error, warning and alert codes to validation errors.
     * 
     * @param graphLocation The location in the graph.
     * @return Collection<ValidationError> The collection of validation errors.
     */
    public Collection<ValidationError> getValidationMessages(final GraphLocation graphLocation);

}
