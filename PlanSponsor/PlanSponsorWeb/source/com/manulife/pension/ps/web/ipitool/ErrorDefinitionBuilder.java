package com.manulife.pension.ps.web.ipitool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

class ErrorDefinitionBuilder {
    
    static final ErrorDefinitionBuilder UNSUPPORTED_BUILDER =
            new ErrorDefinitionBuilder() {
                @Override
                void add(IpiToolField<?> field, ValidationMessage... messages) {
                    throw new UnsupportedOperationException(
                            field.getFieldSpec().getLabelText() +
                            ": " + StringUtils.join(messages, ", "));
                }
            };
    
    private final HashSet<String> errorFields = new HashSet<String>();
    final TreeSet<ValidationMessage> messages =
            new TreeSet<ValidationMessage>(
                    new Comparator<ValidationMessage>() {
                        @Override
                        public int compare(ValidationMessage object1, ValidationMessage object2) { return object1.getOrder() - object2.getOrder(); }
                    });
    
    void add(IpiToolField<?> field, ValidationMessage... messageArray) {
        
        if (messageArray != null && messageArray.length > 0) {
            
            errorFields.add(field.getFieldSpec().getFormName());
            
            for (final ValidationMessage message : messageArray) {
                
                messages.add(message);
                
            }
            
        }
        
    }
    
    boolean isEmpty() { return messages.isEmpty(); }
    boolean isFieldInError(IpiToolField<?> field) { return errorFields.contains(field.getFieldSpec().getFormName()); }
    Set<String> getErrorFields() { return Collections.unmodifiableSet(errorFields); }
    List<Integer> getErrorKeys() {
        
        final ArrayList<Integer> errorKeys = new ArrayList<Integer>();
        for (ValidationMessage message : messages) {
            errorKeys.add(Integer.valueOf(message.getKey()));
        }
        errorKeys.trimToSize();
        
        return Collections.unmodifiableList(errorKeys);
        
    }
}