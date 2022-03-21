package com.manulife.pension.ps.web.contract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.collections.CollectionUtils;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.ps.web.plan.util.PlanValidationMappingHelper;
import com.manulife.pension.ps.web.withdrawal.GraphLocation;
import com.manulife.pension.service.contract.common.PlanMessage;
import com.manulife.pension.service.contract.valueobject.BasePlanData;
import com.manulife.pension.validator.ValidationError;

public class BasePlanDataUiObject extends BaseSerializableCloneableObject {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private HashSet<String> uiFields = new HashSet<String>(0);

    private String valueObjectBeanName;

    /**
     * Default Constructor.
     * 
     * @param uiFields An array of field names that are mapped as Strings in the UI object.
     * @param voBeanName The field name of the value object that this UI object wraps.
     */
    protected BasePlanDataUiObject(final String[] uiFields, final String voBeanName) {
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
     * This gets all the messages that are contained within this object graph.
     * 
     * @param graphLocation The relative location to get the message for.
     * @param planDataObject The plan data object (impl {@link BasePlanData}) to collect messages
     *            from.
     * @return Collection - The UI validation error found within this object graph.
     */
    public Collection<ValidationError> getValidationMessages(final GraphLocation graphLocation,
            final BasePlanData planDataObject) {

        final Collection<ValidationError> messages = new ArrayList<ValidationError>();

        for (PlanMessage planMessage : planDataObject.getMessages()) {
            final String[] propertyNames = new String[planMessage.getPropertyNames().size()];
            int i = 0;
            for (String propertyName : planMessage.getPropertyNames()) {
                propertyNames[i] = graphLocation
                        .getFullLocation(getUiNameFromPropertyName(propertyName));
                i++;
            }
            final ValidationError validationError = new ValidationError(propertyNames,
                    PlanValidationMappingHelper.getErrorCodeFromMessage(planMessage), planMessage
                            .getParameters().toArray(), PlanValidationMappingHelper
                            .getValidationTypeFromMessage(planMessage), PlanValidationMappingHelper
                            .getGroupFromMessage(planMessage));

            messages.add(validationError);
        }

        return messages;
    }
}
