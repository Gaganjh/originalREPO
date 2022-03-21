/*
 * Apr 27, 2007
 * 11:39:40 AM
 */
package com.manulife.pension.service.withdrawal.log;

import java.lang.reflect.Field;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.manulife.pension.service.withdrawal.valueobject.BaseWithdrawal;

/**
 * ReflectionToLogBuilder Extends the basic ReflectionToStringBuilder to use the
 * {@link BaseWithdrawal}{@link #toString()} method of the {@link BaseWithdrawal} objects if they
 * exist, otherwise just use the regular {@link Object#toString()} methods.
 * 
 * @author glennpa
 */
public class ReflectionToLogBuilder extends ReflectionToStringBuilder {

    /**
     * Default Constructor.
     * 
     * @param object The object to get a value from.
     */
    public ReflectionToLogBuilder(final Object object) {
        super(object);
    }

    /**
     * Default Constructor.
     * 
     * @param object The object to get a value from.
     * @param style The style to use.
     */
    public ReflectionToLogBuilder(final Object object, final ToStringStyle style) {
        super(object, style);
    }

    /**
     * Default Constructor.
     * 
     * @param object The object to get a value from.
     * @param style The style to use.
     * @param buffer The StringBuffer to use.
     */
    public ReflectionToLogBuilder(final Object object, final ToStringStyle style,
            final StringBuffer buffer) {
        super(object, style, buffer);
    }

    /**
     * Default Constructor.
     * 
     * @param object The object to get a value from.
     * @param style The style to use.
     * @param buffer The StringBuffer to use.
     * @param reflectUpToClass the superclass to reflect up to (inclusive), may be <code>null</code>
     * @param outputTransients whether to include transient fields
     * @param outputStatics whether to include static fields
     */
    public ReflectionToLogBuilder(final Object object, final ToStringStyle style,
            final StringBuffer buffer, final Class reflectUpToClass,
            final boolean outputTransients, final boolean outputStatics) {
        super(object, style, buffer, reflectUpToClass, outputTransients, outputStatics);
    }

    /**
     * This method is overriden from the base method to provide the field value as the
     * {@link BaseWithdrawal#toLog()} value if the object is a {@link BaseWithdrawal} object.
     * 
     * @param field The field to get the value for.
     * @return Object The field value, or the toLog value if the object is a BaseWithdrawal object.
     * @throws IllegalAccessException - Thrown from the superclass.
     * 
     * @see org.apache.commons.lang.builder.ReflectionToStringBuilder#getValue(java.lang.reflect.Field)
     */
    @Override
    protected Object getValue(final Field field) throws IllegalAccessException {

        Object myValue = super.getValue(field);

        // Deal with an object that's a BaseWithdrawal type.
        if (myValue instanceof BaseWithdrawal) {
            BaseWithdrawal baseWithdrawal = (BaseWithdrawal) myValue;
            myValue = baseWithdrawal.toLog();
        } // fi

        // Deal with a Collection that may contain a BaseWithdrawal type.
        if (myValue instanceof Collection) {
            Collection collection = (Collection) myValue;
            myValue = CollectionUtils.collect(collection, new WithdrawalToLogTransformer());
        } // fi

        return myValue;
    }

    /**
     * Builds a String for a toString method excluding the given field names.
     * 
     * @param object The object to "toString".
     * @param excludeFieldNames The field names to exclude
     * @return The toString value.
     */
    public static String toLogExclude(final Object object,
            final Collection<String> excludeFieldNames) {
        return toLogExclude(object, excludeFieldNames.toArray(new String[excludeFieldNames.size()]));
    }

    /**
     * Builds a String for a toString method excluding the given field names.
     * 
     * @param object The object to "toString".
     * @param excludeFieldNames The field names to exclude
     * @return The toString value.
     */
    public static String toLogExclude(final Object object, final String[] excludeFieldNames) {
        return new ReflectionToLogBuilder(object).setExcludeFieldNames(excludeFieldNames)
                .toString();
    }

}
