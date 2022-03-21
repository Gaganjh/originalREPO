package com.manulife.pension.ps.service.report.transaction.handler;

import org.apache.commons.lang.ObjectUtils;

/**
 * This class represents the key to lookup a TransactionHandler using the
 * TransactionHandlerFactory.
 * 
 * @author Charles Chan
 */
public class TransactionHandlerKey {

	/**
	 * The cached hash value.
	 */
	private int hashValue;

	/** 
	 * Whether we need to rehash the key or not. If true, rehash, otherwise,
	 * use the cached hash value.
	 */
	private boolean rehash;

	/** 
	 * The transaction type.
	 */
	private String transactionType;

	/**
	 * The transaction reason code.
	 */
	private String reasonCode;

    /**
     * The transaction reason code.(added for Roth - Excess Withdrawals)
     */
    private String reasonCodeExcessWD;

	/**
	 * The report that displays the transaction.
	 */
	private String reportId;

	/**
	 * Constructor.
	 */
	public TransactionHandlerKey() {
		super();
	}

	/**
	 * Constructor.
	 * @param type The transaction type.
	 */
	public TransactionHandlerKey(String type) {
		this(type, null, null);
	}

	/**
	 * Constructor.
	 * @param type The transaction type
	 * @param reasonCode The transaction reason code
	 */
	public TransactionHandlerKey(String type, String reasonCode) {
		this(type, reasonCode, null);
	}

	/**
	 * Constructor.
	 * @param type The transaction type
	 * @param reasonCode The transaction reason code
	 * @param reportId The report that displays the transaction.
	 */
	public TransactionHandlerKey(String type, String reasonCode, String reportId) {
		super();
		this.transactionType = type;
		this.reasonCode = reasonCode;
		this.reportId = reportId;
		this.rehash = true;
	}
	
	/**
     * Constructor.
     * @param type The transaction type
     * @param reasonCode The transaction reason code
     * @param reportId The report that displays the transaction.
     * @param reasonCodeExcessWD The transaction reason code specific -identifies Excess WD
     */
    public TransactionHandlerKey(String type, String reasonCode, String reportId, String reasonCodeExcessWD) {
        super();
        this.transactionType = type;
        this.reasonCode = reasonCode;
        this.reportId = reportId;
        this.reasonCodeExcessWD = reasonCodeExcessWD;
        this.rehash = true;
    }
    
	/**
	 * @return Returns the reasonCode.
	 */
	public String getReasonCode() {
		return reasonCode;
	}

    /**
     * @return Returns the reasonCodeExcessWD.
     */
    public String getReasonCodeExcessWD() {
        return reasonCodeExcessWD;
    }

	/**
	 * @param reasonCode
	 *            The reasonCode to set.
	 */
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
		rehash = true;
	}

    /**
     * @param reasonCode
     *            The reasonCodeExcessWD to set.
     */
    public void setReasonCodeExcessWD(String reasonCodeExcessWD) {
        this.reasonCodeExcessWD = reasonCodeExcessWD;
        rehash = true;
    }

	/**
	 * @return Returns the reportId.
	 */
	public String getReportId() {
		return reportId;
	}

	/**
	 * @param reportId
	 *            The reportId to set.
	 */
	public void setReportId(String reportId) {
		this.reportId = reportId;
		rehash = true;
	}

	/**
	 * @return Returns the transactionType.
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType
	 *            The transactionType to set.
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
		rehash = true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (arg0 == this) {
			return true;
		}
		if (arg0 instanceof TransactionHandlerKey) {
			TransactionHandlerKey key = (TransactionHandlerKey) arg0;
			return ObjectUtils.equals(transactionType, key.transactionType)
					&& ObjectUtils.equals(reasonCode, key.reasonCode)
					&& ObjectUtils.equals(reportId, key.reportId);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {

		if (rehash) {
			StringBuffer sb = new StringBuffer();

			if (transactionType != null) {
				sb.append(transactionType);
			}
			if (reasonCode != null) {
				sb.append(reasonCode);
			}
			if (reasonCodeExcessWD != null) {
                sb.append(reasonCodeExcessWD);
            }
			if (reportId != null) {
				sb.append(reportId);
			}
			hashValue = sb.toString().hashCode();
			rehash = false;
		}
		return hashValue;
	}
}
