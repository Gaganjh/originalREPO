package com.manulife.pension.bd.web.bob.participant;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.util.render.DateRender;
import java.util.List;										//CL 110234
import java.util.ArrayList;									//CL 110234

/**
 * to store the Participant Summary Report page form values
 * @author ambroar
 *
 */
public class ParticipantSummaryReportForm extends BaseReportForm {
	

	private static final long serialVersionUID = 1L;
	public static final String ALL = "All";
	public static final String FIELD_SSN = "ssn";
	public static final String FIELD_LAST_NAME = "lastName";
	public static final int BASE_NUMBER_OF_COLUMNS = 22;
	public static final int BASE_TABLE_WIDTH = 760;

	private boolean hasLoansFeature = false;
	private boolean hasRothFeature = false; // whether the contract includes Roth money types
	private String asOfDate=null;
	private String baseAsOfDate=null;
	private String status=null;
	private String employmentStatus=null;						//CL 110234
	private String namePhrase = null;
	private String ssnOne;
	private String ssnTwo;
	private String ssnThree;
	private boolean hasContractGatewayInd = false;
	private boolean hasManagedAccountInd = false;
	private boolean gatewayChecked = false;
	private boolean managedAccountChecked = false;
	private String resetGateway = null;
	private String resetManagedAccount = null;
    private String showCustomizeFilter = "";
    private String quickFilterStatus=null;
	private String quickFilterEmploymentStatus=null;					//CL 110234
    private String quickFilterNamePhrase = null;
    private boolean quickFilterGatewayChecked = false;
    private boolean quickFilterManagedAccount = false;
    private String quickFilterDivision = null;
    private String participantFilter = null;
    private String totalAssetsFrom = null;
    private String totalAssetsTo = null;
    private String quickTotalAssetsFrom = null;
    private String quickTotalAssetsTo = null;
    private boolean showDivision = false;
    private String division;
    private List statusList = new ArrayList();					//CL 110234
	
	/**
	 * Constructor for ParicipantSummaryReportForm
	 */
	public ParticipantSummaryReportForm() {
		super();
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
	
	/**
     * Validate the as of date is current date
	 * @return boolean isAsOfDateCurrent
	 */
	public boolean isAsOfDateCurrent() {
		if(StringUtils.isEmpty(getBaseAsOfDate())||StringUtils.isEmpty(getAsOfDate())) return true;
		return getBaseAsOfDate().equals(getAsOfDate());
	}

	/**
     * To format the as of date
	 * @return displayAsOfDate
	 */
	public String getDisplayAsOfDate() {
		
		if (!StringUtils.isEmpty(getAsOfDate())) {
			return DateRender.formatByPattern(new Date(Long.parseLong(getAsOfDate())), "", BDConstants.FORMAT_DATE_SHORT_YMD);
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

	public int getNumberOfColumns() {
		return BASE_NUMBER_OF_COLUMNS
			+ (getHasLoansFeature() ? BDConstants.NUMBER_2 : BDConstants.NUMBER_0)
			+ (getHasRothFeature() ? BDConstants.NUMBER_2 : BDConstants.NUMBER_0);
	}
	
	public int getTableWidth() {
		return BASE_TABLE_WIDTH
			+ (getHasLoansFeature() ? BDConstants.NUMBER_40 : BDConstants.NUMBER_0)
			+ (getHasRothFeature() ? BDConstants.NUMBER_40 : BDConstants.NUMBER_0);
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
	/**
	 * Reset the required variables
	 */
	public void resetForm()
	{
	     	
		setGatewayChecked(false);
		setQuickFilterGatewayChecked(false);
	 
	}
	/**
	 * Reset the required variables for managedAccount
	 */
	public void resetForm2()
	{
		setManagedAccountChecked(false);
		setQuickFilterManagedAccount(false);
		 
	}
    /**
     * @return the showCustomizeFilter
     */
    public String getShowCustomizeFilter() {
        return showCustomizeFilter;
    }
    /**
     * @param showCustomizeFilter the showCustomizeFilter to set
     */
    public void setShowCustomizeFilter(String showCustomizeFilter) {
        this.showCustomizeFilter = showCustomizeFilter;
    }
    /**
     * @return the quickFilterGatewayChecked
     */
    public boolean getQuickFilterGatewayChecked() {
        return quickFilterGatewayChecked;
    }
    /**
     * @param quickFilterGatewayChecked the quickFilterGatewayChecked to set
     */
    public void setQuickFilterGatewayChecked(boolean quickFilterGatewayChecked) {
        this.quickFilterGatewayChecked = quickFilterGatewayChecked;
    }
    /**
     * @return the quickFilterNamePhrase
     */
    public String getQuickFilterNamePhrase() {
        return quickFilterNamePhrase;
    }
    /**
     * @param quickFilterNamePhrase the quickFilterNamePhrase to set
     */
    public void setQuickFilterNamePhrase(String quickFilterNamePhrase) {
        this.quickFilterNamePhrase = quickFilterNamePhrase;
    }
    /**
     * @return the quickFilterStatus
     */
    public String getQuickFilterStatus() {
        return quickFilterStatus;
    }
    /**
     * @param quickFilterStatus the quickFilterStatus to set
     */
    public void setQuickFilterStatus(String quickFilterStatus) {
        this.quickFilterStatus = quickFilterStatus;
    }
    /**
     * @return the participantFilter
     */
    public String getParticipantFilter() {
        return participantFilter;
    }
    /**
     * @param participantFilter the participantFilter to set
     */
    public void setParticipantFilter(String participantFilter) {
        this.participantFilter = participantFilter;
    }
    /**
     * @return the totalAssetsFrom
     */
    public String getTotalAssetsFrom() {
        return totalAssetsFrom;
    }
    /**
     * @param totalAssetsFrom the totalAssetsFrom to set
     */
    public void setTotalAssetsFrom(String totalAssetsFrom) {
        this.totalAssetsFrom = totalAssetsFrom;
    }
    /**
     * @return the totalAssetsTo
     */
    public String getTotalAssetsTo() {
        return totalAssetsTo;
    }
    /**
     * @param totalAssetsTo the totalAssetsTo to set
     */
    public void setTotalAssetsTo(String totalAssetsTo) {
        this.totalAssetsTo = totalAssetsTo;
    }
    /**
     * @return the quickTotalAssetsFrom
     */
    public String getQuickTotalAssetsFrom() {
        return quickTotalAssetsFrom;
    }
    /**
     * @param quickTotalAssetsFrom the quickTotalAssetsFrom to set
     */
    public void setQuickTotalAssetsFrom(String quickTotalAssetsFrom) {
        this.quickTotalAssetsFrom = quickTotalAssetsFrom;
    }
    /**
     * @return the quickTotalAssetsTo
     */
    public String getQuickTotalAssetsTo() {
        return quickTotalAssetsTo;
    }
    /**
     * @param quickTotalAssetsTo the quickTotalAssetsTo to set
     */
    public void setQuickTotalAssetsTo(String quickTotalAssetsTo) {
        this.quickTotalAssetsTo = quickTotalAssetsTo;
    }
	/**
	 * @return the boolean showDivision
	 */
	public boolean isShowDivision() {
		return showDivision;
	}
	/**
	 * @param showDivision the showDivision to set
	 */
	public void setShowDivision(boolean showDivision) {
		this.showDivision = showDivision;
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
    
    /**
     * Gets the quickFilterDivision
     * @return Returns a String
     */
    public String getQuickFilterDivision() {
        return quickFilterDivision;
    }
    
    /**
     * Sets the quickFilterDivision
     * @param quickFilterDivision The quickFilterDivision to set
     */
    public void setQuickFilterDivision(String quickFilterDivision) {
        this.quickFilterDivision = quickFilterDivision;
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
	 * @return Returns a list
	 */
    public List getStatusList() {
        return statusList;
    }

	/**
	 * Sets the Status List
	 * @param statusList The status List to set
	 */
    public void setStatusList(List statusList) {
        this.statusList = statusList;
    }

	/**
	 * Gets the Quick Filter Employment Status
	 * @return Returns a String
	 */
    public String getQuickFilterEmploymentStatus() {
		return quickFilterEmploymentStatus;
	}
    
	/**
	 * Sets the Quick Filter Employment Status
	 * @param QuickFilterEmploymentStatus The QuickFilterEmploymentStatus to set
	 */
	public void setQuickFilterEmploymentStatus(String quickFilterEmploymentStatus) {
		this.quickFilterEmploymentStatus = quickFilterEmploymentStatus;
	}

	// Setting and getting Managed Account related variables
	/**
	 * Gets the hasManagedAccount 
	 * @return Returns boolean
	 */
	public boolean getHasManagedAccountInd() {
		return hasManagedAccountInd;
	}

	/**
	 * Sets the hasManagedAccount 
	 * @param hasManagedAccount The hasManagedAccount to set
	 */
	public void setHasManagedAccountInd(boolean hasManagedAccountInd) {
		this.hasManagedAccountInd = hasManagedAccountInd;
	}
	
	/**
	 * Gets the managedAccount
	 * @return Returns boolean
	 */
	public boolean getManagedAccountChecked() {
		return managedAccountChecked;
	}
	
	/**
	 * Sets the managedAccount
	 * @param managedAccount The managedAccount to set
	 */
	public void setManagedAccountChecked(boolean managedAccountChecked) {
		this.managedAccountChecked = managedAccountChecked;
	}
	
	 /**
     * @return the quickFilterManagedAccount
     */
	public boolean getQuickFilterManagedAccount() {
		return quickFilterManagedAccount;
	}
	/**
     * @param quickFilterManagedAccount the quickFilterManagedAccount to set
     */
	public void setQuickFilterManagedAccount(boolean quickFilterManagedAccount) {
		this.quickFilterManagedAccount = quickFilterManagedAccount;
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
	
//CL 110234 End
}

