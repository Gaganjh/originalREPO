package com.manulife.pension.platform.web.util;

import java.io.Serializable;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * @author Charles Chan
 */
public class Ssn implements Cloneable, Serializable {

	private String[] digits = new String[3];

	/**
	 * Constructor.
	 */
	public Ssn() {
		super();
	}

	public Ssn(String ssn) {
		super();
		digits[0] = ssn.substring(0, 3);
		digits[1] = ssn.substring(3, 5);
		digits[2] = ssn.substring(5, 9);
	}

	public String[] getDigits() {
		return digits;
	}

	public String getDigits(int index) {
		return digits[index];
	}

	public void setDigits(int index, String numbers) {
		digits[index] = numbers;
	}

	public boolean isEmpty() {
		boolean empty = true;
		for (int i = 0; i < 3 && empty; i++) {
			if (digits[i] != null && digits[i].length() > 0) {
				empty = false;
			}
		}
		return empty;
	}

	public Object clone() {
		try {
			Ssn myClone = (Ssn) super.clone();
			myClone.digits = (String[]) digits.clone();
			return myClone;
		} catch (CloneNotSupportedException e) {
			// this should not happen because we have implemented Cloneable
			throw new NestableRuntimeException(e);
		}
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof Ssn) {
			Ssn rhs = (Ssn) obj;
			return ObjectUtils.equals(digits[0], rhs.digits[0])
					&& ObjectUtils.equals(digits[1], rhs.digits[1])
					&& ObjectUtils.equals(digits[2], rhs.digits[2]);
		}
		return false;
	}

	public String toString() {
		if (digits[0] != null && digits[1] != null && digits[2] != null) {
			StringBuffer sb = new StringBuffer(digits[0]).append(digits[1])
					.append(digits[2]);
			return sb.toString();
		} else {
			return "";
		}
	}

}