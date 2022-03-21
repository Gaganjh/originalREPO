package com.manulife.pension.ps.web.participant;

import java.util.ArrayList;					//CL 110234
import java.util.Date;
import java.util.List;						//CL 110234

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.util.render.DateRender;

public class ParticipantSummaryReportForm extends ReportForm {
	
	public static final String FORMAT_DATE_LONG= "MMMM d, yyyy";
	public static final String FORMAT_DATE_SHORT_YMD = "yyyy-MM-dd";
	public static final String ALL = "All";
	public static final String FIELD_SSN = "ssn";
	public static final String FIELD_LAST_NAME = "lastName";
	public static final int BASE_NUMBER_OF_COLUMNS = 22;			//CL 110234
	public static final int BASE_TABLE_WIDTH = 730;

	private String asOfDate=null;
	private String baseAsOfDate=null;
	private String status=null;
	private String employmentStatus=null;							//CL 110234
	private String namePhrase = null;
	private Ssn ssn;
	private String ssnOne;
	private String ssnTwo;
	private String ssnThree;
    private String division;
    private String resetGateway = null;
    private boolean gatewayChecked = false;
    private boolean showDivision = false;
	private boolean hasLoansFeature = false;
	private boolean hasRothFeature = false;
	private boolean hasContractGatewayInd = false;	
    private List statusList = new ArrayList();				//CL 110234
    private String resetManagedAccount = null;	
    private boolean managedAccountChecked = false;
    private boolean hasManagedAccountInd = false;
	/**
	 * Constructor for ParicipantSummaryReportForm
	 */
	public ParticipantSummaryReportForm() {
		super();
	}
	
	/**
	 * Gets showDivision
	 * @return Returns a boolean
	 */
	public boolean getShowDivision() {
		return showDivision;
	}

	/**
	 * Sets showDivision
	 * @param showDivision The showDivision value to set
	 */
	public void setShowDivision(boolean showDivision) {
		this.showDivision = showDivision;
	}

	
	/**
	 * Gets the hasLoansFeature
	 * @return Returns a boolean
	 */
	public boolean getHasLoansFeature() {
		return hasLoansFeature;
	}

	/**
	 * Sets the hasLoansFeature
	 * @param hasLoansFeature The hasLoansFeature to set
	 */
	public void setHasLoansFeature(boolean hasLoansFeature) {
		this.hasLoansFeature = hasLoansFeature;
	}

	/**
	 * Gets the hasRothFeature
	 * @return Returns a boolean
	 */
	public boolean getHasRothFeature() {
		return hasRothFeature;
	}

	/**
	 * Sets the hasRothFeature
	 * @param hasRothFeature The hasRothFeature to set
	 */
	public void setHasRothFeature(boolean hasRothFeature) {
		this.hasRothFeature = hasRothFeature;
	}	
	
	public boolean isAsOfDateCurrent() {
		if(StringUtils.isEmpty(getBaseAsOfDate())||StringUtils.isEmpty(getAsOfDate())) return true;
		return getBaseAsOfDate().equals(getAsOfDate());
	}

	public String getDisplayAsOfDate() {
		
		if (!StringUtils.isEmpty(getAsOfDate())) {
			return DateRender.formatByPattern(new Date(Long.parseLong(getAsOfDate())), "", FORMAT_DATE_SHORT_YMD);
		} else return null;
	}
	/**
	 * Gets the asOfDate
	 * @return Returns a String
	 */
	public String getAsOfDate() {
		return asOfDate;
	}

	/**
	 * Sets the asOfDate
	 * @param asOfDate The asOfDate to set
	 */
	public void setAsOfDate(String asOfDate) {
		this.asOfDate = asOfDate; 
	}
//	/**
//	 * Gets the contractDates
//	 * @return Returns a ArrayList
//   */	
//	public ArrayList getContractDates() {
//		return contractDates;
//	}

//	/**
//	 * Sets the contractDates
//	 * @param contractDates The contractDates to set
//	 */
//	public void setContractDates(ArrayList contractDates) {
//		this.contractDates = contractDates;
//	}
	/**
	 * Gets the status
	 * @return Returns a string
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * Sets the Status
	 * @param contractDates The contractDates to set
	 */
	public void setStatus(String status) {
		if (ALL.equalsIgnoreCase(status) )
		{ this.status = null; }
		else {this.status = status; }
	}

	/**
	 * Gets the baseAsOfDate
	 * @return Returns a String
	 */
	public String getBaseAsOfDate() {
		return baseAsOfDate;
	}

	/**
	 * Sets the baseAsOfDate
	 * @param baseAsOfDate The baseAsOfDate to set
	 */
	public void setBaseAsOfDate(String baseAsOfDate) {
		this.baseAsOfDate = baseAsOfDate;
	}
	/**
	 * Gets the namePhrase
	 * @return Returns a String
	 */
	public String getNamePhrase() {
		return namePhrase;
	}
	/**
	 * Sets the namePhrase
	 * @param namePhrase The namePhrase to set
	 */
	public void setNamePhrase(String namePhrase) {
		this.namePhrase = namePhrase;
	}

	/**
	 * resets the form
	 */
	public void clear() {
		hasLoansFeature = false;
		asOfDate=null;
		baseAsOfDate=null;
		status=null;
		namePhrase = null;
		ssn = null;
		ssnOne = null;
		ssnTwo = null;
		ssnThree = null;
	}

	/**
	 * Gets the ssn
	 * @return Returns a Ssn
	 */
	public Ssn getSsn() {
		Ssn ssnTemp = new Ssn();
		ssnTemp.setDigits(0,ssnOne);
		ssnTemp.setDigits(1,ssnTwo);
		ssnTemp.setDigits(2,ssnThree);
		return ssnTemp;
	}
	/**
	 * Gets the ssnOne
	 * @return Returns a String
	 */
	public String getSsnOne() {
		return ssnOne;
	}
	/**
	 * Sets the ssnOne
	 * @param ssnOne The ssnOne to set
	 */
	public void setSsnOne(String ssnOne) {
		this.ssnOne = ssnOne;
	}


	/**
	 * Gets the ssnTwo
	 * @return Returns a String
	 */
	public String getSsnTwo() {
		return ssnTwo;
	}
	/**
	 * Sets the ssnTwo
	 * @param ssnTwo The ssnTwo to set
	 */
	public void setSsnTwo(String ssnTwo) {
		this.ssnTwo = ssnTwo;
	}


	/**
	 * Gets the ssnThree
	 * @return Returns a String
	 */
	public String getSsnThree() {
		return ssnThree;
	}
	/**
	 * Sets the ssnThree
	 * @param ssnThree The ssnThree to set
	 */
	public void setSsnThree(String ssnThree) {
		this.ssnThree = ssnThree;
	}

	/**
	 * Gets the division
	 * @return Returns a String
	 */
    public String getDivision() {
        return division;
    }
    /**
     * Sets the division
     * @param division The division to set
     */
    public void setDivision(String division) {
        this.division = division;
    }
	
	private int getTableWidthNumber() {
		return BASE_TABLE_WIDTH
			+ (getShowDivision() ? 75 : 0)
			+ (getHasRothFeature() ? 20 : 0)
			+ (getHasLoansFeature() ? 75 : 0)
			+ (getHasContractGatewayInd() ? 20 : 0);
	}
	
	private int getNumberOfColumns() {
		return BASE_NUMBER_OF_COLUMNS
			+ (getShowDivision() ? 2 : 0)
			+ (getHasRothFeature() ? 2 : 0)
			+ (getHasLoansFeature() ? 2 : 0)
			+ (getHasContractGatewayInd() ? 2 : 0)
			+ (getHasManagedAccountInd() ? 2 : 0);
	}
	
	private int getNumberOfMoneyTotalColumns() {
		return getHasLoansFeature() ? 10 : 8;
	}
	
	public String getTableWidth() {
		return String.valueOf(getTableWidthNumber());
	}
	
	public String getColumnSpan() {
		return String.valueOf(getNumberOfColumns());
	}
	
	public String getColumnSpanLess2() {
		return String.valueOf(getNumberOfColumns() - 2);
	}
	
	public String getColumnSpanLess4() {
		return String.valueOf(getNumberOfColumns() - 4);
	}
	
	public String getParticipantTotalsColumnSpan() {
		return String.valueOf(getNumberOfColumns() - getNumberOfMoneyTotalColumns() - 3);
	}
	
	public String getMoneyTotalsColumnSpan() {
		return String.valueOf(getNumberOfMoneyTotalColumns());
	}

	//Setting and getting gateway related variables
	/**
	 * Gets the hasGatewayFeature
	 * @return Returns boolean
	 */
	public boolean getHasContractGatewayInd() {
		return hasContractGatewayInd;
	}
	/**
	 * Sets the hasGatewayFeature
	 * @param hasGatewayFeature The hasGatewayFeature to set
	 */
	public void setHasContractGatewayInd(boolean hasContractGatewayInd) {
		this.hasContractGatewayInd = hasContractGatewayInd;
	}
	/**
	 * Gets the gateway
	 * @return Returns boolean
	 */
	public boolean getGatewayChecked() {
		return gatewayChecked;
	}
	/**
	 * Sets the gateway
	 * @param gateway The gateway to set
	 */
	public void setGatewayChecked(boolean gatewayChecked) {
		this.gatewayChecked = gatewayChecked;
	}
	/**
	 * Gets the resetGateway
	 * @return Returns a String
	 */
	public String getResetGateway() {
		return resetGateway;
	}
	/**
	 * Sets the resetGateway
	 * @param resetGateway The resetGateway to set
	 */
	public void setResetGateway(String resetGateway) {
		this.resetGateway = resetGateway;
	}
//CL 110234 Begin
	/**
	 * Gets the Employment status
	 * @return Returns a String
	 */
	public String getEmploymentStatus() {
		return employmentStatus;
	}

	/**
	 * Sets the Employment status
	 * @param employmentStatus The employmentStatus to set
	 */
	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}
	/**
	 * Gets the Status List
	 * @return Returns a String
	 */
    public List getStatusList() {
        return statusList;
    }

	/**
	 * Sets the Status List
	 * @param statusList
	 */
    public void setStatusList(List statusList) {
        this.statusList = statusList;
    }
	//CL 110234 End
	/**
	 * Reset the required variables
	 */
	public void resetForm()
	{
	     	
		setGatewayChecked(false);
		 
	}
	/**
	 * Gets the ManagedAccount
	 * @return Returns boolean
	 */
	public boolean getManagedAccountChecked() {
		return managedAccountChecked;
	}
	/**
	 * Sets the ManagedAccount
	 * @param ManagedAccount The ManagedAccount to set
	 */
	public void setManagedAccountChecked(boolean managedAccountChecked) {
		this.managedAccountChecked = managedAccountChecked;
	}
	/**
	 * Gets the hasManagedAccountInd
	 * @return Returns boolean
	 */
	public boolean getHasManagedAccountInd() {
		return hasManagedAccountInd;
	}
	/**
	 * Sets the hasManagedAccountInd
	 * @param hasManagedAccountInd The hasManagedAccountInd to set
	 */
	public void setHasManagedAccountInd(boolean hasManagedAccountInd) {
		this.hasManagedAccountInd = hasManagedAccountInd;
	}
	/**
   	 * Gets the resetManagedAccount	 
	 * @return Returns a String
	 */
	public String getResetManagedAccount() {
		return resetManagedAccount;
	}

	/**
	 * Sets the resetManagedAccount 
	 * @param resetManagedAccount The resetManagedAccount to set
	 */
	public void setResetManagedAccount(String resetManagedAccount) {
		this.resetManagedAccount = resetManagedAccount;
	}
	
}

