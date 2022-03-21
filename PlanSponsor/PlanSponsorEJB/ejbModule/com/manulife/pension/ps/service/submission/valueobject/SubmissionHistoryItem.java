/*
 * Created on August 17, 2004
 */
package com.manulife.pension.ps.service.submission.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.service.report.submission.valueobject.SubmissionHistoryReportData;
import com.manulife.pension.ps.service.submission.util.MoneySourceHelper;
import com.manulife.pension.ps.service.submission.util.StatusGroupHelper;
import com.manulife.pension.ps.service.submission.util.SubmissionHistoryItemComparator;
import com.manulife.pension.ps.service.submission.util.SubmissionHistoryItemPayrollDateComparator;
import com.manulife.pension.ps.service.submission.util.SubmissionHistoryItemStatusComparator;
import com.manulife.pension.ps.service.submission.util.SubmissionHistoryItemSubmissionDateComparator;
import com.manulife.pension.ps.service.submission.util.SubmissionHistoryItemSubmissionNumberComparator;
import com.manulife.pension.ps.service.submission.util.SubmissionHistoryItemSubmitterComparator;
import com.manulife.pension.ps.service.submission.util.SubmissionHistoryItemTypeComparator;

/**
 * One item in the submission history.
 *
 * @author Adrian Robitu
 */
public class SubmissionHistoryItem implements Serializable, Lockable {

	private static final String DESCENDING_INDICATOR = "DESC";

	public static final String SORT_SUBMISSION_NUMBER = "submissionNumber";
	public static final String SORT_SUBMISSION_DATE = "submissionDate";
	public static final String SORT_TYPE = "type";
	public static final String SORT_PAYROLL_DATE = "payrollDate";
	public static final String SORT_CONTRIBUTION_TOTAL = "contributionTotal";
	public static final String SORT_SUBMITTER_NAME = "submitterName";
	public static final String SORT_STATUS = "status";

	public static final String USER_TYPE_INTERNAL = "I";
	public static final String USER_TYPE_INTERNAL_DESCRIPTION = "John Hancock Account Representative";
	
	private static final String FILE_LOADER = "FL";
	public static final String SYSTEM_STATUS_CODE = "95";
	public static final String ZERO_CONTRIBUTUION_STATUS = "22";
	
	private static final Set DRAFT_STATES = new HashSet(2);
	static {
		DRAFT_STATES.add("14");
		DRAFT_STATES.add("A7");
	}

	private static Map comparators = new HashMap();
	static {
		comparators.put(SubmissionHistoryReportData.SORT_SUBMITTER_NAME,
				new SubmissionHistoryItemSubmitterComparator());
		comparators.put(SubmissionHistoryReportData.SORT_SUBMISSION_ID,
				new SubmissionHistoryItemSubmissionNumberComparator());
		comparators.put(SubmissionHistoryReportData.SORT_SUBMISSION_DATE,
				new SubmissionHistoryItemSubmissionDateComparator());
		comparators.put(SubmissionHistoryReportData.SORT_TYPE,
				new SubmissionHistoryItemTypeComparator());
		comparators.put(SubmissionHistoryReportData.SORT_USER_STATUS,
				new SubmissionHistoryItemStatusComparator());
		comparators.put(SubmissionHistoryReportData.SORT_PAYROLL_DATE,
				new SubmissionHistoryItemPayrollDateComparator());
	}

	private String submitterID;
	private String submitterType;
	private String submitterName;
	private Integer submissionId; // this is the internal id used in STP tables as part of the PK
	private Integer contractId;
	private String contractName;
	private Date submissionDate;
	private String type;
	private String systemStatus;
	private Date payrollDate;
	private BigDecimal contributionTotal;
	private BigDecimal paymentTotal;
	private String moneySourceID;
	private boolean validMoneySource;
	private MoneySource moneySource;
	private String applicationCode;
	private Lock lock;
	private boolean internalSubmitter;
	private boolean lockedByInternalUser;
    private String submitterEmail;
    private String tpaSystemName;
    private String tpaSubmissionType;
    private String tpaVersionNo;
    private String comboFileInd;
	private Date lastUpdatedDate;


	public SubmissionHistoryItem() {
		super();
	}

	public SubmissionHistoryItem(String submitterID, String submitterType, String submitterName, Integer submissionId,
			Date submissionDate, String type, String systemStatus, Date payrollDate,
			BigDecimal contributionTotal, BigDecimal paymentTotal, String moneySourceID, String applicationCode, Integer contractId, String contractName, 
            String tpaSystemName, String tpaSubmissionType, String tpaVersionNo, Lock lock ) {
		this.submitterID = submitterID;
		this.submitterType = submitterType;
		this.submitterName = submitterName;
		this.submissionId = submissionId;
		this.submissionDate = submissionDate;
		this.type = type;
		this.systemStatus = systemStatus;
		this.payrollDate = payrollDate;
		this.contributionTotal = contributionTotal;
		this.paymentTotal = paymentTotal;
		setMoneySourceID(moneySourceID);
		validMoneySource = true;
		this.applicationCode = applicationCode;
		this.contractId = contractId;
		this.contractName = contractName;
		this.lock = lock;
		this.internalSubmitter = false;
		this.lockedByInternalUser= false;
		this.tpaSystemName = tpaSystemName;
        this.tpaSubmissionType = tpaSubmissionType;
        this.tpaVersionNo = tpaVersionNo;
	}
	
	public SubmissionHistoryItem(String submitterID, String submitterType, String submitterName, String submitterEmail, Integer submissionId,
			Date submissionDate, String type, String systemStatus, Date payrollDate,
			BigDecimal contributionTotal, BigDecimal paymentTotal, String moneySourceID, String applicationCode, Integer contractId, String contractName, 
            String tpaSystemName, String tpaSubmissionType, String tpaVersionNo, Lock lock ) {
		this( submitterID,  submitterType,  submitterName,   submissionId, submissionDate,  type,  
				systemStatus,  payrollDate, contributionTotal,  paymentTotal,  moneySourceID,  applicationCode, 
				contractId,  contractName, tpaSystemName,  tpaSubmissionType,  tpaVersionNo,  lock );
		this.submitterEmail = submitterEmail;
	}

	/**
	 * @return Returns the contractId.
	 */
	public Integer getContractId() {
		return contractId;
	}
	/**
	 * @param contractId The contractId to set.
	 */
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	
	/**
	 * @return Returns the submissionId.
	 */
	public Integer getSubmissionId() {
		return submissionId;
	}
	/**
	 * @param submissionId The submissionId to set.
	 */
	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}
	/**
	 * @return Returns the contractName.
	 */
	public String getContractName() {
		return contractName;
	}
	/**
	 * @param contractName The contractName to set.
	 */
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	/**
	 * @return Returns the contributionTotal.
	 */
	public BigDecimal getContributionTotal() {
		return contributionTotal;
	}
    
	/**
	 * @return Returns the paymentTotal.
	 */
	public BigDecimal getPaymentTotal() {
		return paymentTotal;
	}

	/**
	 * @return Returns the payrollDate.
	 */
	public Date getPayrollDate() {
		return payrollDate;
	}

	/**
	 * @return Returns the submissionDate.
	 */
	public Date getSubmissionDate() {
		return submissionDate;
	}

	/**
	 * @return Returns the submitterID.
	 */
	public String getSubmitterID() {
		return submitterID;
	}

	/**
	 * @return Returns the submitterName.
	 */
	public String getSubmitterName() {
		if((USER_TYPE_INTERNAL.equals(getSubmitterType()))&&
				!(SYSTEM_STATUS_CODE.equals(getSystemStatus().trim()))){
			return USER_TYPE_INTERNAL_DESCRIPTION;
		}else
		return submitterName;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param contributionTotal The contributionTotal to set.
	 */
	public void setContributionTotal(BigDecimal contributionTotal) {
		this.contributionTotal = contributionTotal;
	}
    

	/**
	 * @param paymentTotal The paymentTotal to set.
	 */
	public void setPaymentTotal(BigDecimal paymentTotal) {
		this.paymentTotal = paymentTotal;
	}

	/**
	 * @param payrollDate The payrollDate to set.
	 */
	public void setPayrollDate(Date payrollDate) {
		this.payrollDate = payrollDate;
	}

	/**
	 * @param submissionDate The submissionDate to set.
	 */
	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	/**
	 * @param submitterID The submitterID to set. It trims white speaces.
	 */
	public void setSubmitterID(String submitterID) {
		this.submitterID = submitterID == null ? submitterID : submitterID.trim();
	}

	/**
	 * @param submitterName The submitterName to set.
	 */
	public void setSubmitterName(String submitterName) {
		this.submitterName = submitterName;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return Returns the systemStatuses.
	 */
	public String getSystemStatus() {
		return systemStatus;
	}

	/**
	 * @param systemStatuses The systemStatuses to set.
	 */
	public void setSystemStatus(String systemStatus) {
		this.systemStatus = systemStatus;
	}

	public boolean equals(Object arg0) {
		if (arg0 == null || !(arg0 instanceof SubmissionHistoryItem)) {
			return false;
		}
		SubmissionHistoryItem otherItem = (SubmissionHistoryItem)arg0;
		Integer otherNumber = otherItem.getSubmissionId();
		if (submissionId == null || otherNumber == null) {
			return false;
		}
		return submissionId.equals(otherNumber);
	}

	/**
	 * @return Returns the submitterType.
	 */
	public String getSubmitterType() {
		return submitterType;
	}

	/**
	 * @param submitterType The submitterType to set.
	 */
	public void setSubmitterType(String submitterType) {
		this.submitterType = submitterType;
	}

	/**
	 * Returns a compartor for the given sort field and direction
	 * @param sortField the sort field
	 * @param sortDirection the sort direction ASC or DESC
	 * @return the Compartor
	 */
	public static Comparator getComparator(String sortField, String sortDirection) {
		SubmissionHistoryItemComparator comparator =
			comparators.containsKey(sortField) ? (SubmissionHistoryItemComparator) comparators.get(sortField) :
												 (SubmissionHistoryItemComparator) comparators.get(SubmissionHistoryReportData.SORT_SUBMISSION_DATE);
		comparator.setAscending(!DESCENDING_INDICATOR.equalsIgnoreCase(sortDirection));
		return comparator;
	}


	/**
	 * @return The status grouping
	 */
	public String getStatusGroup() {
		return StatusGroupHelper.getInstance().getGroupByStatus(getSystemStatus());
	}

	/**
	 * @return Returns the isValidMoneySource.
	 */
	public boolean isValidMoneySource() {
		return validMoneySource;
	}

	/**
	 * @param isValidMoneySource The isValidMoneySource to set.
	 */
	public void setValidMoneySource(boolean validMoneySource) {
		this.validMoneySource = validMoneySource;
	}

	/**
	 * @return Returns the moneySourceID.
	 */
	public String getMoneySourceID() {
		return moneySourceID;
	}

	/**
	 * @param moneySourceID The moneySourceID to set.
	 */
	public void setMoneySourceID(String moneySourceID) {
		if (moneySourceID != null) {
			this.moneySourceID = moneySourceID.trim();
			this.moneySource = MoneySourceHelper.getMoneySource(this.moneySourceID);
		}
	}

	public String toString() {
		return getSubmitterID() + ", " + getSubmitterName() + ", " +
				getSubmissionId() + ", " + getSubmissionDate() + ", " + getType() + ", " +
				getSystemStatus() + ", " + getPayrollDate() + ", " +
				getContributionTotal() + ", " + getPaymentTotal();
	}

	/**
	 * @return Returns the applicationCode.
	 */
	public String getApplicationCode() {
		return applicationCode;
	}

	/**
	 * @param applicationCode The applicationCode to set.
	 */
	public void setApplicationCode(String applicationCode) {
		if ( applicationCode != null ) {
			this.applicationCode = applicationCode.trim();
		}
	}

	/**
	 * A convenience method to hide implementation of application codes
	 *
	 * @return
	 */
	public boolean isFileLoaderCase() {
		return FILE_LOADER.equals(applicationCode);
	}

	/**
	 * A convenience method used to determine if the item has a draft status
	 *
	 * @return
	 */
	public boolean isDraft() {
		return DRAFT_STATES.contains(getSystemStatus());
	}


	/**
	 * @return Returns the lockTS.
	 */
	public Date getLockTS() {
		return lock != null ? lock.getLockTS() : null;
	}
	
	/**
	 * @param lockTS The lockTS to set.
	 */
	public void setLockTS(Date lockTS) {
		if ( lock == null ) {
			lock = new Lock();
		}
		this.lock.setLockTS(lockTS);
	}
	
	/**
	 * @return Returns the lockUserId.
	 */
	public String getLockUserId() {
		return lock != null ? lock.getUserId(): null;
	}
	
	/**
	 * @param lockUserId The lockUserId to set.
	 */
	public void setLockUserId(String lockUserId) {
		if ( lock == null ) {
			lock = new Lock();
		}
		this.lock.setUserId(lockUserId);

	}

	/**
	 * @return user name of the individual with the lock
	 */
	public String getLockUserName() {
		return lock != null ? lock.getUserName(): null;
	}
	
	/**
	 * @return a boolean to determine whether or not it is locked
	 */
	public boolean isLocked() {
		return lock.isActive();
	}
	
	/**
	 * @param profileId
	 * @return a boolean indicating whether or not the user can edit the submission based on lock status
	 */
	public boolean isLockAvailable(String userId) {
		return lock.isAvailable(userId);
	}
	
	public Lock getLock() {
		return lock;
	}
	
	public void setLock(Lock lock) {
		this.lock = lock;
	}
	/**
	 * @return Returns the moneySource.
	 */
	public MoneySource getMoneySource() {
		return moneySource;
	}
	/**
	 * @param moneySource The moneySource to set.
	 */
	public void setMoneySource(MoneySource moneySource) {
		this.moneySource = moneySource;
	}
	public boolean isInternalSubmitter() {
		return this.internalSubmitter;
	}
	public void setInternalSubmitter(boolean isInternalSubmitter) {
		this.internalSubmitter = isInternalSubmitter;
	}
	public boolean isLockedByInternalUser() {
		return this.lockedByInternalUser;
	}
	public void setLockedByInternalUser(boolean lockedByInternalUser) {
		this.lockedByInternalUser = lockedByInternalUser;
	}

    public String getSubmitterEmail() {
        return submitterEmail;
    }

    public void setSubmitterEmail(String submitterEmail) {
        this.submitterEmail = submitterEmail;
    }

    public String getTpaSubmissionType() {
        return tpaSubmissionType;
    }

    public void setTpaSubmissionType(String tpaSubmissionType) {
        this.tpaSubmissionType = tpaSubmissionType;
    }

    public String getTpaSystemName() {
        return tpaSystemName;
    }

    public void setTpaSystemName(String tpaSystemName) {
        this.tpaSystemName = tpaSystemName;
    }

    public String getTpaVersionNo() {
        return tpaVersionNo;
    }

    public void setTpaVersionNo(String tpaVersionNo) {
        this.tpaVersionNo = tpaVersionNo;
    }

    /**
     * @return the lastUpdatedDate
     */
    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    /**
     * @param lastUpdatedDate the lastUpdatedDate to set
     */
    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    
    /**
     * @return the comboFileInd
     */
    public String getComboFileInd() {
		return comboFileInd;
	}

    /**
     * @param comboFileInd the comboFileInd to set
     */
	public void setComboFileInd(String comboFileInd) {
		this.comboFileInd = comboFileInd;
	}

	/**
     * @return the ZeroContribution Ind
     */
	public boolean isZeroContribution() {
		return StringUtils.equals(ZERO_CONTRIBUTUION_STATUS, this.getSystemStatus());
	}
}