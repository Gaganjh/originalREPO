package com.manulife.pension.ps.administration.valueobject;

import java.io.Serializable;
import java.util.Date;

import com.manulife.pension.util.StaticHelperClass;
/*
 * Created on Jul 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Ilker Celikyilmaz
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TpaUser implements Serializable {
	
	private String firmId;
	private String firmName;
	private String ssn;
	private String apolloFirstName;
	private String apolloLastName;
	private String apolloEmail;	
	private String pswFirstName;
	private String pswLastName;
	private boolean apolloStaffPlanPermission;
	private boolean pswStaffPlanPermission;
	private boolean apolloReceiveIloanEmailPermission;
	private boolean pswReceiveIloanEmailPermission;
	private Date conversionDate;
	private String pswStatus;
	private String pswEmail;
	private String matchingILoansPermissionActionOnPSW = "";
	private boolean primaryTpa;
	
	public TpaUser() {
		
	} 
	

	
	/**
	 * @return Returns the apolloFirstName.
	 */
	public String getApolloFirstName() {
		return apolloFirstName;
	}
	/**
	 * @param apolloFirstName The apolloFirstName to set.
	 */
	public void setApolloFirstName(String apolloFirstName) {
		this.apolloFirstName = apolloFirstName;
	}
	/**
	 * @return Returns the apolloLastName.
	 */
	public String getApolloLastName() {
		return apolloLastName;
	}
	/**
	 * @param apolloLastName The apolloLastName to set.
	 */
	public void setApolloLastName(String apolloLastName) {
		this.apolloLastName = apolloLastName;
	}
	/**
	 * @return Returns the apolloStaffPlanPermission.
	 */
	public boolean isApolloStaffPlanPermission() {
		return apolloStaffPlanPermission;
	}
	/**
	 * @param apolloStaffPlanPermission The apolloStaffPlanPermission to set.
	 */
	public void setApolloStaffPlanPermission(boolean apolloStaffPlanPermission) {
		this.apolloStaffPlanPermission = apolloStaffPlanPermission;
	}
	/**
	 * @return Returns the conversionDate.
	 */
	public Date getConversionDate() {
		return conversionDate;
	}
	/**
	 * @param conversionDate The conversionDate to set.
	 */
	public void setConversionDate(Date conversionDate) {
		this.conversionDate = conversionDate;
	}
	/**
	 * @return Returns the firmId.
	 */
	public String getFirmId() {
		return firmId;
	}
	/**
	 * @param firmId The firmId to set.
	 */
	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}
	/**
	 * @return Returns the firmName.
	 */
	public String getFirmName() {
		return firmName;
	}
	/**
	 * @param firmName The firmName to set.
	 */
	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}
	/**
	 * @return Returns the pswFirstName.
	 */
	public String getPswFirstName() {
		return pswFirstName;
	}
	/**
	 * @param pswFirstName The pswFirstName to set.
	 */
	public void setPswFirstName(String pswFirstName) {
		this.pswFirstName = pswFirstName;
	}
	/**
	 * @return Returns the pswLastName.
	 */
	public String getPswLastName() {
		return pswLastName;
	}
	/**
	 * @param pswLastName The pswLastName to set.
	 */
	public void setPswLastName(String pswLastName) {
		this.pswLastName = pswLastName;
	}
	/**
	 * @return Returns the pswStaffPlanPermission.
	 */
	public boolean isPswStaffPlanPermission() {
		return pswStaffPlanPermission;
	}
	/**
	 * @param pswStaffPlanPermission The pswStaffPlanPermission to set.
	 */
	public void setPswStaffPlanPermission(boolean pswStaffPlanPermission) {
		this.pswStaffPlanPermission = pswStaffPlanPermission;
	}
	/**
	 * @return Returns the apolloReceiveIloanEmailPermission.
	 */
	public boolean isApolloReceiveIloanEmailPermission() {
		return apolloReceiveIloanEmailPermission;
	}
	/**
	 * @param apolloReceiveIloanEmailPermission The apolloReceiveIloanEmailPermission to set.
	 */
	public void setApolloReceiveIloanEmailPermission(
			boolean apolloReceiveIloanEmailPermission) {
		this.apolloReceiveIloanEmailPermission = apolloReceiveIloanEmailPermission;
	}
	/**
	 * @return Returns the ssn.
	 */
	public String getSsn() {
		return ssn;
	}
	/**
	 * @param ssn The ssn to set.
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	
	public String toString()
	{
		return StaticHelperClass.toString(this);
	}
	/**
	 * @return Returns the pswStatus.
	 */
	public String getPswStatus() {
		return pswStatus;
	}
	/**
	 * @param pswStatus The pswStatus to set.
	 */
	public void setPswStatus(String pswStatus) {
		this.pswStatus = pswStatus;
	}
	/**
	 * @return Returns the pswEmail.
	 */
	public String getPswEmail() {
		return pswEmail;
	}
	/**
	 * @param pswEmail The pswEmail to set.
	 */
	public void setPswEmail(String pswEmail) {
		this.pswEmail = pswEmail;
	}
	/**
	 * @return Returns the pswReceiveIloanEmailPermission.
	 */
	public boolean isPswReceiveIloanEmailPermission() {
		return pswReceiveIloanEmailPermission;
	}
	/**
	 * @param pswReceiveIloanEmailPermission The pswReceiveIloanEmailPermission to set.
	 */
	public void setPswReceiveIloanEmailPermission(
			boolean pswReceiveIloanEmailPermission) {
		this.pswReceiveIloanEmailPermission = pswReceiveIloanEmailPermission;
	}
	/**
	 * @return Returns the matchingILoansPermissionActionOnPSW.
	 */
	public String getMatchingILoansPermissionActionOnPSW() {
		return matchingILoansPermissionActionOnPSW;
	}
	/**
	 * @param matchingILoansPermissionActionOnPSW The matchingILoansPermissionActionOnPSW to set.
	 */
	public void setMatchingILoansPermissionActionOnPSW(
			String matchingILoansPermissionActionOnPSW) {
		this.matchingILoansPermissionActionOnPSW = matchingILoansPermissionActionOnPSW;
	}
	
	/**
	 * @return Returns the pswEmail.
	 */
	public String getApolloEmail() {
		return apolloEmail;
	}
	/**
	 * @param apolloEmail The apolloEmail to set.
	 */
	public void setApolloEmail(String apolloEmail) {
		this.apolloEmail = apolloEmail;
	}	
	/**
	 * @return Returns the primaryTpa.
	 */
	public boolean isPrimaryTpa() {
		return primaryTpa;
	}
	/**
	 * @param primaryTpa The primaryTpa to set.
	 */
	public void setPrimaryTpa(boolean primaryTpa) {
		this.primaryTpa = primaryTpa;
	}
}
