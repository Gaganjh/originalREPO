package com.manulife.pension.ps.service.report.feeSchedule.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.service.report.feeSchedule.util.FeeScheduleChangeItemComparator;
import com.manulife.pension.ps.service.report.feeSchedule.util.FeeScheduleChangeItemDateComparator;
import com.manulife.pension.ps.service.report.feeSchedule.util.FeeScheduleChangeItemStndScheduleIndComparator;
import com.manulife.pension.ps.service.report.feeSchedule.util.FeeScheduleChangeItemTypeComparator;
import com.manulife.pension.ps.service.report.feeSchedule.util.FeeScheduleChangeItemUserNameComparator;
import com.manulife.pension.ps.service.report.feeSchedule.util.FeeScheduleChangeItemValueComparator;
import com.manulife.pension.ps.service.report.feeSchedule.util.FeeScheduleItemDefaultComparator;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO.AmountType;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO.FeeCategoryCode;

/**
 * 
 * @author Siby Thomas
 *
 */
public class FeeScheduleChangeItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DESCENDING_INDICATOR = "DESC";
	private static final int SORT_LAST = 9999;
	private static final String PERCENTAGE_FORMAT = "###0.###";
	private static final String DOLLOR_FORMAT = "#,##0.00";
	
	public static final DecimalFormat DOLLOR_AMOUNT_FORMAT = new DecimalFormat(
			DOLLOR_FORMAT);
	
	public static final DecimalFormat PERCENTAGE_AMOUNT_FORMAT = new DecimalFormat(
			PERCENTAGE_FORMAT);
	
	/**
	 * @param double value
	 * @param string format
	 * @return String value
	 */
	public static synchronized String formatDecimalValue(BigDecimal value, String format) { 
        return new DecimalFormat(format).format(value); 
    }
	public static final String AMOUNT_ZERO = "0.00";
	
	private static Map<String, FeeScheduleChangeItemComparator> comparators = new HashMap<String, FeeScheduleChangeItemComparator>();
	static {
		comparators.put(ContractFeeScheduleChangeHistoryReportData.SORT_DEFAULT,
				new FeeScheduleItemDefaultComparator());
		comparators.put(ContractFeeScheduleChangeHistoryReportData.SORT_CHANGER_NAME,
				new FeeScheduleChangeItemUserNameComparator());
		comparators.put(ContractFeeScheduleChangeHistoryReportData.SORT_VALUE,
				new FeeScheduleChangeItemValueComparator());
		comparators.put(ContractFeeScheduleChangeHistoryReportData.SORT_CHANGE_DATE,
				new FeeScheduleChangeItemDateComparator());
		comparators.put(ContractFeeScheduleChangeHistoryReportData.SORT_TYPE,
				new FeeScheduleChangeItemTypeComparator());
		comparators.put(ContractFeeScheduleChangeHistoryReportData.SORT_SCHEDULE_IND,
				new FeeScheduleChangeItemStndScheduleIndComparator());
	}

	private Timestamp changedDate;
	private String userId;
	private String userName;
	private String changedType;
	private String changedValue; 
	private String changedValueType; 
	private Boolean standardScheduleApplied;
	private String specialNotes;
	private int typeSortOrder;
	private Boolean deletedIndicator = Boolean.FALSE;
	private String feeCategoryCode;
	private boolean customContractEntry = false;
	private String feeSearchCase = StringUtils.EMPTY;
	
	public FeeScheduleChangeItem(Timestamp changedDate,
			String userId,
			String changedType,
			String changedValue,
			String changedValueType,
			Boolean standardScheduleApplied,
			String specialNotes,
			int typeSortOrder, 
			Boolean deletedIndicator,
			String feeCategoryCode,
			String feeSearchCase) {
		this.changedDate = changedDate;
		this.userId = userId;
		this.changedType = changedType;
		this.changedValue = changedValue;
		this.changedValueType = changedValueType;
		this.standardScheduleApplied = standardScheduleApplied;
		this.specialNotes = specialNotes;
		this.typeSortOrder = typeSortOrder;
		this.deletedIndicator = deletedIndicator;
		this.feeCategoryCode = feeCategoryCode;
		this.feeSearchCase = feeSearchCase;
	}
	
	public FeeScheduleChangeItem(Timestamp changedDate,
			String userId,
			String changedType,
			String changedValue,
			String specialNotes) {
		this.changedDate = changedDate;
		this.userId = userId;
		this.changedType = changedType;
		this.changedValue = changedValue;
		this.changedValueType = null;
		this.standardScheduleApplied = null;
		this.specialNotes = specialNotes;
		this.typeSortOrder = SORT_LAST;
	}

	public FeeScheduleChangeItem(Timestamp changedDate,
			String changedType,
			String changedValue,
			String changedValueType,
			Boolean standardScheduleApplied,
			String specialNotes,
			String userName,
			int typeSortOrder, 
			Boolean deletedIndicator,
			String feeCategoryCode) {
		this.changedDate = changedDate;
		this.userName = userName;
		this.changedType = changedType;
		this.changedValue = changedValue;
		this.changedValueType = changedValueType;
		this.standardScheduleApplied = standardScheduleApplied;
		this.specialNotes = specialNotes;
		this.typeSortOrder = typeSortOrder;
		this.deletedIndicator = deletedIndicator;
		this.feeCategoryCode = feeCategoryCode;
	}
	
	
	public FeeScheduleChangeItem(Timestamp changedDate,
			String changedType,
			String changedValue,
			String changedValueType,
			Boolean standardScheduleApplied,
			String userId, 
			Boolean deletedIndicator,
			String feeCategoryCode,
			String feeSearchCase) {
		this.changedDate = changedDate;
		this.userId = userId;
		this.changedType = changedType;
		this.changedValue = changedValue;
		this.changedValueType = changedValueType;
		this.standardScheduleApplied = standardScheduleApplied;		
		this.deletedIndicator = deletedIndicator;
		this.feeCategoryCode = feeCategoryCode;
		this.feeSearchCase = feeSearchCase;
	}
	
	
	public FeeScheduleChangeItem(Timestamp changedDate,
			String changedType,
			String changedValue,
			Boolean standardScheduleApplied,
			String userId, 
			String specialNotes) {
		this.changedDate = changedDate;
		this.userId = userId;
		this.changedType = changedType;
		this.changedValue = changedValue;
		this.standardScheduleApplied = standardScheduleApplied;
		this.specialNotes = specialNotes;
	}

	/**
	 * Returns a compartor for the given sort field and direction
	 * @param sortField the sort field
	 * @param sortDirection the sort direction ASC or DESC
	 * @return the Compartor
	 */
	public static Comparator<FeeScheduleChangeItem> getComparator(String sortField, String sortDirection) {
		FeeScheduleChangeItemComparator comparator = comparators.get(sortField);
		comparator.setAscending(!DESCENDING_INDICATOR.equalsIgnoreCase(sortDirection));
		return comparator;
	}
	
	public Timestamp getChangedDate() {
		return changedDate;
	}
	public void setChangedDate(Timestamp changedDate) {
		this.changedDate = changedDate;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getChangedType() {
		return changedType;
	}
	public void setChangedType(String changedType) {
		this.changedType = changedType;
	}
	
	public String getChangedValue() {
		if (getDeletedIndicator() && !isCustomContractEntry()) {
			return  AMOUNT_ZERO;
		} 
		return changedValue;
	}
	
	public String getChangedValueAbsolute() {
		return changedValue;
	}
	
	public void setChangedValue(String changedValue) {
		this.changedValue = changedValue;
	}
	
	public String getChangedValueType() {
		return changedValueType;
	}
	public void setChangedValueType(String changedValueType) {
		this.changedValueType = changedValueType;
	}
	
	public Boolean getStandardScheduleApplied() {
		return standardScheduleApplied;
	}
	
	public String getStandardScheduleAppliedValue() {
		if(getStandardScheduleApplied() == null) {
			return StringUtils.EMPTY;
		} else {
			return getStandardScheduleApplied() == Boolean.TRUE ? "Y" : "N";
		}
	}
	
	
	public void setStandardScheduleApplied(Boolean standardScheduleApplied) {
		this.standardScheduleApplied = standardScheduleApplied;
	}

	public Integer getTypeSortOrder() {
		return typeSortOrder;
	}
	public void setTypeSortOrder(Integer typeSortOrder) {
		this.typeSortOrder = typeSortOrder;
	}

	public String getSpecialNotes() {
		return specialNotes;
	}
	public void setSpecialNotes(String specialNotes) {
		this.specialNotes = specialNotes;
	}

	public Boolean getDeletedIndicator() {
		return deletedIndicator;
	}

	public void setDeletedIndicator(Boolean deletedIndicator) {
		this.deletedIndicator = deletedIndicator;
	}

	public String getFeeCategoryCode() {
		return feeCategoryCode;
	}

	public void setFeeCategoryCode(String feeCategoryCode) {
		this.feeCategoryCode = StringUtils.trimToEmpty(feeCategoryCode);
	}

	public boolean isTpaFeeItem(){
		if(FeeCategoryCode.TPA_PRE_DEFINED.getCode().equals(getFeeCategoryCode())
				|| FeeCategoryCode.TPA_NON_STANDARD.getCode().equals(getFeeCategoryCode())) {
			return true;
		}
		return false;
	}
	
    public boolean isTpaStandardFeeItem(){
    	if(FeeCategoryCode.TPA_PRE_DEFINED.getCode().equals(getFeeCategoryCode())) {
			return true;
		}
		return false;
	}
	
    public boolean isCustomContractEntry() {
		return customContractEntry;
	}

	public void setCustomContractEntry(boolean customContractEntry) {
		this.customContractEntry = customContractEntry;
	}
    
	public String getFormttedValue() {
		String valueType = getChangedValueType();
		if (StringUtils.isEmpty(valueType) ||  
				StringUtils.isEmpty(getChangedValue())) {
			return getChangedValue();
		}
		String value = StringUtils.EMPTY;

		if (AmountType.DOLLAR.getCode().equals(valueType)) {
			value = String.valueOf(formatDecimalValue(new BigDecimal(
						getChangedValue()), DOLLOR_FORMAT));
			return AmountType.DOLLAR.getCode().concat(value);

		} else {
			value = String.valueOf(formatDecimalValue(new BigDecimal(
						getChangedValue()), PERCENTAGE_FORMAT));
			return value.concat(AmountType.PERCETAGE.getCode());
		}
	}
	
	public String getFeeSearchCase() {
		return feeSearchCase;
	}
	public void setFeeSearchCase(String feeSearchCase) {
		this.feeSearchCase = feeSearchCase;
	}
}