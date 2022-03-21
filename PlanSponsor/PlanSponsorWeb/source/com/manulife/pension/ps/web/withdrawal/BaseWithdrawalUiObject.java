/*
 * BaseWithdrawalUiObject.java,v 1.1.2.1 2006/11/10 18:59:46 Paul_Glenn Exp
 * BaseWithdrawalUiObject.java,v
 * Revision 1.1.2.1  2006/11/10 18:59:46  Paul_Glenn
 * Updates for biz tier validation mapping.
 *
 */
package com.manulife.pension.ps.web.withdrawal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.collections.CollectionUtils;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalValidationMappingHelper;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessage;
import com.manulife.pension.service.withdrawal.valueobject.BaseWithdrawal;
import com.manulife.pension.validator.ValidationError;

/**
 * BaseWithdrawalUiObject is the base class for all the withdrawal UI objects. These UI objects are
 * wrappers around the basic withdrawal value objects. They primarily provide {@link String} fields
 * for types that are enterable through the UI. This is primarily due to the way Struts does type
 * conversion on it's Forms. There are other UI specific methods and such also in these objects. UI
 * validation errors can be generated from the withdrawal objects (the non-UI versions) by calling
 * getValidationMessages().
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1.2.1 2006/11/10 18:59:46
 */
public abstract class BaseWithdrawalUiObject extends BaseSerializableCloneableObject implements
        WithdrawalMessageToValidationErrorTranslator {

    private HashSet<String> uiFields = new HashSet<String>(0);

    private String valueObjectBeanName;

    /**
     * Default Constructor.
     * 
     * @param uiFields An array of field names that are mapped as Strings in the UI object.
     * @param voBeanName The field name of the value object that this UI object wraps.
     */
    protected BaseWithdrawalUiObject(final String[] uiFields, final String voBeanName) {
        super();

        this.valueObjectBeanName = voBeanName;

        // Set the uiFields HashSet.
        this.uiFields = new HashSet<String>(uiFields.length);
        CollectionUtils.addAll(this.uiFields, uiFields);
    }

    /**
     * Gets the UI name for the property. If the property is managed by the UI object, it's value is
     * just the property name. If the property is not managed by the UI object, this value is
     * basically the name of the value object appended to the property (i.e.
     * withdrawalObjectX.fieldY).
     * 
     * @param propertyName The name of the property to get the UI name for.
     * @return String - The UI value for this property name.
     */
    public String getUiNameFromPropertyName(final String propertyName) {
        // This maps biz property names to UI property names
        // i.e. in the UI object, or it's nested VO.
        if (uiFields.contains(propertyName)) {
            // It's a UI field.
            return propertyName;
        } else {
            return new StringBuffer(valueObjectBeanName).append(GraphLocation.SEPARATOR).append(
                    propertyName).toString();
        }
    }

    /**
     * This gets all the messages that are contained within this object graph. The
     * 
     * @param graphLocation The relative location to get the message for.
     * @param withdrawalObject The withdrawal object (impl {@link BaseWithdrawal}) to collect
     *            messages from.
     * @return Collection - The UI validation error found within this object graph.
     */
    public Collection<ValidationError> getValidationMessages(final GraphLocation graphLocation,
            final BaseWithdrawal withdrawalObject) {

        final Collection<ValidationError> messages = new ArrayList<ValidationError>();

        for (WithdrawalMessage withdrawalMessage : withdrawalObject.getMessages()) {
            final String[] propertyNames = new String[withdrawalMessage.getPropertyNames().size()];
            int i = 0;
            for (String propertyName : withdrawalMessage.getPropertyNames()) {
                propertyNames[i] = graphLocation
                        .getFullLocation(getUiNameFromPropertyName(propertyName));
                i++;
            } // end for
            final ValidationError validationError = new ValidationError(propertyNames,
                    WithdrawalValidationMappingHelper.getErrorCodeFromMessage(withdrawalMessage),
                    withdrawalMessage.getParameters().toArray(), WithdrawalValidationMappingHelper
                            .getValidationTypeFromMessage(withdrawalMessage));

            messages.add(validationError);
        } // end for

        return messages;
    }

}
