package com.manulife.pension.ireports.model;

import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.manulife.pension.ireports.StandardReportsConstants;

/**
 * Shortlist description.
 */
public class ShortlistKey implements StandardReportsConstants {
	private String shortlistType;
	private String incomeFund;
	private String assetAllocationGroup;
	
	public static final String TYPE_5YR = "5YR";
	public static final String TYPE_3YR = "3YR";
	public static final String TYPE_PERFORM = "PERFORM";
	public static final String TYPE_VALUE = "VALUE";

	public ShortlistKey(String shortListType, String assetAllocationGroup, String incomeFund) {
		this.shortlistType = shortListType;
		this.assetAllocationGroup = assetAllocationGroup;
		this.incomeFund = incomeFund;
	}
	public ShortlistKey(String string) {
		StringTokenizer st = new StringTokenizer(string, "_");
		String shortlistType = st.nextToken();
		String incomeFund = st.nextToken();
		String assetAllocationGroup = st.nextToken();
		this.shortlistType = shortlistType;
		this.assetAllocationGroup = assetAllocationGroup;
		this.incomeFund = incomeFund;
	}
	
	/**
	 *  Is valid when either nothing is entered, or all values are entered
	 * @return
	 */
	public boolean isValid() {
		return (StringUtils.isBlank(this.shortlistType) &&
				StringUtils.isBlank(this.assetAllocationGroup) &&
				StringUtils.isBlank(this.incomeFund) ) ||
		      ! (StringUtils.isBlank(this.shortlistType) ||
				      StringUtils.isBlank(this.assetAllocationGroup) ||
				      StringUtils.isBlank(this.incomeFund) );
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof ShortlistKey)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		ShortlistKey rhs = (ShortlistKey) obj;
		return new EqualsBuilder()
			.append(shortlistType, rhs.shortlistType)
			.append(incomeFund, rhs.incomeFund)
			.append(assetAllocationGroup, rhs.assetAllocationGroup)
			.isEquals();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(shortlistType)
			.append(incomeFund)
			.append(assetAllocationGroup)
			.toHashCode();
	}

	public String getShortlistType() {
		return shortlistType;
	}
	
	public String getIncomeFund() {
		return incomeFund;
	}
	
	public String toString() {
		return shortlistType + "_" + incomeFund + "_" + assetAllocationGroup;
	}

	public String getAssetAllocationGroup() {
		return assetAllocationGroup;
	}
}
