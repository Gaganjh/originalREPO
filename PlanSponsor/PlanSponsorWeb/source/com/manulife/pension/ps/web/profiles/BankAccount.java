package com.manulife.pension.ps.web.profiles;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.lp.model.gft.DirectDebitAccount;

/**
 * This class represents a bank account. It is used to capture FORM data
 * submitted by the user.
 * 
 * @author Charles Chan
 */
public class BankAccount implements Cloneable, Serializable {

	private String label;

	private String primaryKey;

	private boolean noAccess;

	private String bankAccountNumber;

	private String bankName;

	private String bankAccountDescription;

	/**
	 * Constructor.
	 */
	public BankAccount() {
		super();
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public static BankAccount getBankAccount(DirectDebitAccount account) {
		BankAccount baccount = new BankAccount();
		String primaryKey = account.getInstructionNumber() + ":"
				+ account.getEffectiveDate().getTime();
		baccount.setPrimaryKey(primaryKey);
		baccount.setBankName(account.getBankName());
		baccount.setBankAccountDescription(account.getBankAccountDescription());
		baccount.setBankAccountNumber(account.getBankAccountNumber());
		return baccount;
	}

	public DirectDebitAccount getDirectDebitAccount() {
		DirectDebitAccount account = new DirectDebitAccount();
		String primaryKey = getPrimaryKey();
		setDirectDebitAccountPrimaryKey(account, primaryKey);
		account.setBankName(getBankName());
		account.setBankAccountDescription(getBankAccountDescription());
		account.setBankAccountNumber(getBankAccountNumber());
		return account;
	}

	/**
	 * Sets the direct debit account's primary key to the bank account's primary
	 * key.
	 * 
	 * @param account
	 * @param baccount
	 */
	private static void setDirectDebitAccountPrimaryKey(
			DirectDebitAccount account, String primaryKey) {
		if (primaryKey != null) {
			int instructionNumberEndIndex = primaryKey.indexOf(':');
			account.setInstructionNumber(primaryKey.substring(0,
					instructionNumberEndIndex));
			String effectiveDateStr = primaryKey
					.substring(instructionNumberEndIndex + 1);
			account.setEffectiveDate(new Date(Long.valueOf(effectiveDateStr)
					.longValue()));
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return Returns the disabled.
	 */
	public boolean isNoAccess() {
		return noAccess;
	}

	/**
	 * @param disabled
	 *            The disabled to set.
	 */
	public void setNoAccess(boolean disabled) {
		this.noAccess = disabled;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// this should not happen because this object implements Cloneable
			throw new NestableRuntimeException(e);
		}
	}

	/**
	 * Equality check. Only primary key is checked.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof BankAccount) {
			BankAccount account = (BankAccount) obj;
			return ObjectUtils.equals(primaryKey, account.primaryKey);
		}
		return false;
	}

	/**
	 * @return Returns the bankAccountDescription.
	 */
	public String getBankAccountDescription() {
		return bankAccountDescription;
	}

	/**
	 * @param bankAccountDescription
	 *            The bankAccountDescription to set.
	 */
	public void setBankAccountDescription(String bankAccountDescription) {
		this.bankAccountDescription = bankAccountDescription;
	}

	/**
	 * @return Returns the bankAccountNumber.
	 */
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	/**
	 * @param bankAccountNumber
	 *            The bankAccountNumber to set.
	 */
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	/**
	 * @return Returns the bankName.
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName
	 *            The bankName to set.
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
}