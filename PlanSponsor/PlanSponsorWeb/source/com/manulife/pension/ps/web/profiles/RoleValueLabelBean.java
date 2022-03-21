package com.manulife.pension.ps.web.profiles;

import java.io.Serializable;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * @author Steven Wang
 */
public class RoleValueLabelBean implements Cloneable, Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String value;
    private String label;

	/**
	 * Constructor.
	 */
	public RoleValueLabelBean() {
		super();
	}

	public RoleValueLabelBean(String value, String label) {
		super();
        this.value = value;
        this.label = label;
	}

	public Object clone() {
		try {
            RoleValueLabelBean myClone = (RoleValueLabelBean) super.clone();
			return myClone;
		} catch (CloneNotSupportedException e) {
			// this should not happen because we have implemented Cloneable
			throw new NestableRuntimeException(e);
		}
	}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public String toString(){
        return this.label;
    }
    
    public boolean equals(String value) {
        return this.value == null ? value == null : this.value.equals(value);
    }
}