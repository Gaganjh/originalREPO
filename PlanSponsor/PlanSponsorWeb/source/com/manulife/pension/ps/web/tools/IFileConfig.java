package com.manulife.pension.ps.web.tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.util.StaticHelperClass;

public class IFileConfig implements Serializable {

	private ArrayList paymentAccounts;
	private ArrayList accounts = new ArrayList();
	private ArrayList years = new ArrayList();
	private boolean isCashAccountPresent = false;
	private boolean isCurrentContractHasUploadHistory = false;
	private boolean isDirectDebitAccountPresent = false;
	
	/**
	 * @return
	 */
	public ArrayList getAccounts() {
		return accounts;
	}

	/**
	 * @return
	 */
	public List getPaymentAccounts() {
		return paymentAccounts;
	}

	/**
	 * @return
	 */
	public ArrayList getYears() {
		return years;
	}

	/**
	 * @param list
	 */
	public void setAccounts(ArrayList list) {
		accounts = list;
	}

	/**
	 * @param vector
	 */
	public void setPaymentAccounts(ArrayList vector) {
		paymentAccounts = vector;
	}

	/**
	 * @param list
	 */
	public void setYears(ArrayList list) {
		years = list;
	}

	/**
	 * @return
	 */
	public boolean isCashAccountPresent() {
		return isCashAccountPresent;
	}

	/**
	 * @param b
	 */
	public void setCashAccountPresent(boolean b) {
		isCashAccountPresent = b;
	}

	/**
	 * @return
	 */
	public boolean isCurrentContractHasUploadHistory() {
		return isCurrentContractHasUploadHistory;
	}

	/**
	 * @param b
	 */
	public void setCurrentContractHasUploadHistory(boolean b) {
		isCurrentContractHasUploadHistory = b;
	}

	public boolean isDirectDebitAccountPresent() {
		return isDirectDebitAccountPresent;
	}

	public void setDirectDebitAccountPresent(boolean isDebitAccountPresent) {
		this.isDirectDebitAccountPresent = isDebitAccountPresent;
	}
	
	public String toString() {
		return StaticHelperClass.toString(this);
	}
}
