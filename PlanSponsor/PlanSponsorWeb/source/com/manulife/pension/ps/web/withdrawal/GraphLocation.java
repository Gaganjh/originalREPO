/*
 * GraphLocation.java,v 1.1.2.1 2006/11/10 18:59:46 Paul_Glenn Exp
 * GraphLocation.java,v
 * Revision 1.1.2.1  2006/11/10 18:59:46  Paul_Glenn
 * Updates for biz tier validation mapping.
 *
 */
package com.manulife.pension.ps.web.withdrawal;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.manulife.pension.common.GlobalConstants;

/**
 * This class is used to describe a location in an object graph. It contains the relative property
 * location, the index, if one exists, and the parent element, if one exists.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1.2.1 2006/11/10 18:59:46
 */
public class GraphLocation {

    /**
     * INDEX_RIGHT_DELIMITER
     */
    private static final String INDEX_RIGHT_DELIMITER = "]";

    /**
     * INDEX_LEFT_DELIMITER
     */
    private static final String INDEX_LEFT_DELIMITER = "[";

    public static final String SEPARATOR = ".";

    private String relativeProperty;

    private Integer index;

    private GraphLocation parent;

    /**
     * Default Constructor.
     * 
     * @param parent
     * @param relativeProperty
     * @param index
     */
    public GraphLocation(final GraphLocation parent, final String relativeProperty,
            final Integer index) {
        super();
        this.parent = parent;
        this.relativeProperty = relativeProperty;
        this.index = index;
    }

    /**
     * Default Constructor.
     * 
     * @param parent
     * @param relativeProperty
     */
    public GraphLocation(final GraphLocation parent, final String relativeProperty) {
        super();
        this.parent = parent;
        this.relativeProperty = relativeProperty;
        this.index = null;
    }

    /**
     * Default Constructor.
     * 
     * @param relativeProperty
     * @param index
     */
    public GraphLocation(final String relativeProperty, final Integer index) {
        super();
        this.parent = null;
        this.relativeProperty = relativeProperty;
        this.index = index;
    }

    /**
     * Default Constructor.
     * 
     * @param relativeProperty
     */
    public GraphLocation(final String relativeProperty) {
        super();
        this.parent = null;
        this.relativeProperty = relativeProperty;
        this.index = null;
    }

    public String getFullLocation() {

        final StringBuffer location = new StringBuffer();

        if (parent != null) {
            location.append(parent.getFullLocation());
            location.append(SEPARATOR);
        } // fi

        location.append(relativeProperty);

        if (index != null) {
            location.append(INDEX_LEFT_DELIMITER);
            location.append(index);
            location.append(INDEX_RIGHT_DELIMITER);
        } // fi

        return location.toString();
    }

    public String getFullLocation(final String property) {

        final StringBuffer location = new StringBuffer();

        location.append(getFullLocation());

        if (StringUtils.isNotEmpty(property)) {
            location.append(SEPARATOR);
            location.append(property);
        } // fi

        return location.toString();
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @return the parent
     */
    public GraphLocation getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(GraphLocation parent) {
        this.parent = parent;
    }

    /**
     * @return the relativeProperty
     */
    public String getRelativeProperty() {
        return relativeProperty;
    }

    /**
     * @param relativeProperty the relativeProperty to set
     */
    public void setRelativeProperty(String relativeProperty) {
        this.relativeProperty = relativeProperty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                GlobalConstants.DEFAULT_TO_STRING_BUILDER_STYLE);
    }

}
