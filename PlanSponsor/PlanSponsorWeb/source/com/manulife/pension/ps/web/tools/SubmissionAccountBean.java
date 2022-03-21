/*
 * Created on Oct 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.manulife.pension.ps.web.tools;

import java.io.Serializable;

/**
 * @author Tony Tomasone
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SubmissionAccountBean implements Serializable {
	
	private String label;
	private double contributionValue = 0d;
	private double billValue = 0d;
	private double creditValue = 0d;

	/**
	 * 
	 */
	public SubmissionAccountBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return Returns the label.
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return Returns the billValue.
	 */
	public double getBillValue() {
		return billValue;
	}
	/**
	 * @param billValue The billValue to set.
	 */
	public void setBillValue(double billValue) {
		this.billValue = billValue;
	}
	/**
	 * @return Returns the contributionValue.
	 */
	public double getContributionValue() {
		return contributionValue;
	}
	/**
	 * @param contributionValue The contributionValue to set.
	 */
	public void setContributionValue(double contributionValue) {
		this.contributionValue = contributionValue;
	}
	/**
	 * @return Returns the creditValue.
	 */
	public double getCreditValue() {
		return creditValue;
	}
	/**
	 * @param creditValue The creditValue to set.
	 */
	public void setCreditValue(double creditValue) {
		this.creditValue = creditValue;
	}
	/**
	 * @return Returns the totalValue.
	 */
	public double getTotalValue() {
		return contributionValue + billValue + creditValue;
	}

}
