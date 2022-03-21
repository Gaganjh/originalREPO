package com.manulife.pension.ps.web.fee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO.AmountType;

/**
 * 
 * Fee UI value object
 * 
 * @author Siby Thomas
 *
 */
public class FeeUIHolder implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	private static final String PERCENTAGE_FORMAT = "###0.###";
	private static final String PERCENTAGE_FORMAT_FOR_EDIT_PAGE = "###0.000";
	private static final String DOLLOR_FORMAT = "#,##0.00";
	public static final DecimalFormat DOLLOR_AMOUNT_FORMAT = new DecimalFormat(
			DOLLOR_FORMAT);
	
	public static final DecimalFormat PERCENTAGE_AMOUNT_FORMAT = new DecimalFormat(
			PERCENTAGE_FORMAT);
	
	public static final DecimalFormat PERCENTAGE_AMOUNT_FORMAT_FOR_EDIT_PAGE = new DecimalFormat(
			PERCENTAGE_FORMAT_FOR_EDIT_PAGE);
	
	/**
	 * @param Double value
	 * @param String format
	 * @return String value
	 */
	public static synchronized String formatDecimalValue(BigDecimal value, String format) { 
        return new DecimalFormat(format).format(value); 
    }
	public static final String ZERO_VALUE = "0.00";
	
	private ContractCustomizedFeeVO contractCustomizedFeeVO = new ContractCustomizedFeeVO();
	
	private String feeValue = StringUtils.EMPTY;
	private String feeValueType = AmountType.DOLLAR.getCode();
	private String feeDescription = StringUtils.EMPTY;
	private String notes = StringUtils.EMPTY;
	private boolean deleted = false;
	private boolean disabled = false;

	public FeeUIHolder() {
		
	}
	
	public FeeUIHolder(ContractCustomizedFeeVO contractCustomizedFeeVO) {
		this.contractCustomizedFeeVO = contractCustomizedFeeVO;
		this.feeValueType = contractCustomizedFeeVO.getFeeValueType();
		if(contractCustomizedFeeVO.getFeeValueAmount() == null) {
			this.feeValue = StringUtils.EMPTY;
		} else {
			this.feeValue = getDecimalFormatted(String.valueOf(contractCustomizedFeeVO.getFeeValueAmount().doubleValue()), 
					contractCustomizedFeeVO.getFeeValueType());
		}
		this.notes = contractCustomizedFeeVO.getNotes();
		this.feeDescription = contractCustomizedFeeVO.getFeeDescription();
		this.deleted = contractCustomizedFeeVO.isDelete();
	}
	
	private void setContractCustomizedFeeVO(ContractCustomizedFeeVO contractCustomizedFeeVO){
		this.contractCustomizedFeeVO = contractCustomizedFeeVO;
	}
	
	public String getNonFormattedAmountValue() {
		String value = ZERO_VALUE;
		if (StringUtils.isNotBlank(this.feeValue)) {
			value = StringUtils.replace(this.feeValue, ",", "");
		}
		return value;
	}
	
	public ContractCustomizedFeeVO getContractCustomizedFeeVO() {
		double value = getFeeValueAsDecimal();
		String valueType = getFeeValueType();
		if(StringUtils.equals(AmountType.DOLLAR.getCode(), valueType)) {
			this.contractCustomizedFeeVO.setFeeAmount(new BigDecimal(String.valueOf(value)));
			this.contractCustomizedFeeVO.setFeePercentage(null);
		} else if(StringUtils.equals(AmountType.PERCETAGE.getCode(), valueType)) { 
			this.contractCustomizedFeeVO.setFeePercentage(new BigDecimal(String.valueOf(value)));
			this.contractCustomizedFeeVO.setFeeAmount(null);
		} 
		this.contractCustomizedFeeVO.setFeeDescription(getFeeDescription());
		this.contractCustomizedFeeVO.setNotes(getNotes());
		this.contractCustomizedFeeVO.setDelete(getDeleted());
		return this.contractCustomizedFeeVO;
		
	}
	
	public String getFeeCode() {
		return this.contractCustomizedFeeVO.getFeeCode();
	}
	public void setFeeCode(String feeCode) {
		this.contractCustomizedFeeVO.setFeeCode(StringUtils.trimToEmpty(feeCode));
	}
	
	public String getFeeDescription() {
		return StringUtils.trimToEmpty(this.feeDescription);
	}
	public void setFeeDescription(String feeDescription) {
		this.feeDescription = (StringUtils.trimToEmpty(feeDescription));
	}
	
	public String getNotes() {
		return StringUtils.trimToEmpty(this.notes);
	}
	public void setNotes(String notes) {
		this.notes = (StringUtils.trimToEmpty(notes));
	}
	
	public boolean getDeleted() {
		return this.deleted;
	}
	public void setDeleted(boolean isDelete) {
		this.deleted = isDelete;
	}
	
	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public Integer getSortOrder() {
		return this.contractCustomizedFeeVO.getOrder();
	}
	
	private String getDecimalFormatted(String amountValue){
		String value = StringUtils.EMPTY;
		if (StringUtils.isBlank(amountValue)) {
			amountValue = ContractCustomizedFeeVO.ZERO_AMOUNT_STRING;
		}
		if(this.getFeeValueType().equals(AmountType.DOLLAR.getCode())) {
			value = String.valueOf(formatDecimalValue(new BigDecimal(amountValue),DOLLOR_FORMAT));
		} else {
			value = String.valueOf(formatDecimalValue(new BigDecimal(amountValue),PERCENTAGE_FORMAT));
		}
		return value;
	}
	
	private String getDecimalFormatted(String amountValue, String amountType){
		String value = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(amountValue)) {
			if(amountType.equals(AmountType.DOLLAR.getCode())) {
				value = String.valueOf(formatDecimalValue(new BigDecimal(amountValue),DOLLOR_FORMAT));
			} else {
				value = String.valueOf(formatDecimalValue(new BigDecimal(amountValue),PERCENTAGE_FORMAT_FOR_EDIT_PAGE));
			}
		} else {
			value = ContractCustomizedFeeVO.ZERO_AMOUNT_STRING;
		}
		return value;
	}
	
	public String getFeeValue() {
		return feeValue;
	}
	
	public double getFeeValueAsDecimal() {
		double value = 0.0d;
			try {
			 value = Double.valueOf(getNonFormattedAmountValue());
			} catch (NumberFormatException e) {
				// do nothing
			}
		return value;
	}
	
	public void setFeeValue(String feeValue) {
		this.feeValue = (StringUtils.trimToEmpty(feeValue));
	}
	
	public String getFeeValueType() {
		return feeValueType;
	}

	public void setFeeValueType(String feeValueType) {
		this.feeValueType = feeValueType;
	}
	
	public String getAmountValueFormatted() {
		
		String amountValue = ContractCustomizedFeeVO.ZERO;
		if(!this.deleted){
			amountValue = this.getNonFormattedAmountValue();
		}
		String formattedValue = getDecimalFormatted(amountValue);
		if(this.getFeeValueType().equals(AmountType.DOLLAR.getCode())) {
			return AmountType.DOLLAR.getCode().concat(formattedValue);
		} else if(this.getFeeValueType().equals(AmountType.PERCETAGE.getCode())){
			return formattedValue.concat(AmountType.PERCETAGE.getCode());
		} else {
			return StringUtils.EMPTY;
		}
	}

	@Override
    public boolean equals(Object o) {
		FeeUIHolder vo = (FeeUIHolder) o;
		if(StringUtils.isEmpty(this.getFeeCode()) ||  StringUtils.isEmpty(vo.getFeeCode())) {
			return false;
		}
	   return vo.getFeeCode().equals(this.getFeeCode());
    }
	
    public boolean valueEquals(Object o) {
		FeeUIHolder vo = (FeeUIHolder) o;
		if (StringUtils.equals(vo.getFeeDescription(), this.getFeeDescription())
				&& StringUtils.equals(vo.getFeeValueType(), this.getFeeValueType())
				&& vo.getFeeValueAsDecimal() == this.getFeeValueAsDecimal()
				&& StringUtils.equals(vo.getNotes(), this.getNotes())
				&& vo.getDeleted() == this.getDeleted()) {
			return true;
		} 
		return false;
    }
	
	public boolean isEmpty() {
		if (StringUtils.isEmpty(this.getFeeDescription())
				&& StringUtils.isEmpty(this.getFeeValue())
				&& StringUtils.isEmpty(this.getNotes())) {
			return true;
		}
		return false;
	}
	
	public boolean isValueEmpty() {
		if (StringUtils.isEmpty(this.getFeeDescription())
				&& getFeeValueAsDecimal() == 0.0d
				&& StringUtils.isEmpty(this.getNotes())) {
			return true;
		}
		return false;
	}
	
	public boolean getShowFee() {
		if (getFeeValueAsDecimal() == 0.0d) {
			return false;
		}
		return true;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		FeeUIHolder fee = new FeeUIHolder();
		fee.setFeeDescription(this.feeDescription);
		fee.setFeeValue(this.feeValue);
		fee.setFeeValueType(this.feeValueType);
		fee.setNotes(this.notes);
		fee.setDeleted(this.deleted);
		fee.setDisabled(this.disabled);
		ContractCustomizedFeeVO feeVO = new ContractCustomizedFeeVO();
		feeVO.setFeeCode(this.contractCustomizedFeeVO.getFeeCode());
		feeVO.setContractId(this.contractCustomizedFeeVO.getContractId());
		feeVO.setTpaId(this.contractCustomizedFeeVO.getTpaId());
		fee.setContractCustomizedFeeVO(feeVO);
		return fee;
	}
	
	/**
	 * 
	 * get changed fee items
	 * 
	 * @param editedFees
	 * @param actualFees
	 * 
	 * @return List<FeeUIHolder>
	 * 
	 * @throws SystemException 
	 * @throw  CloneNotSupportedException
	 */
	public static List<FeeUIHolder> getChangedFeeItems(List<FeeUIHolder> editedFees, List<FeeUIHolder> actualFees) throws SystemException, CloneNotSupportedException {
		List<FeeUIHolder> changedFeeItems = new ArrayList<FeeUIHolder>();
		for (FeeUIHolder newFee : editedFees) {
			if (actualFees.contains(newFee)) {
				FeeUIHolder oldFee = actualFees.get(actualFees.indexOf(newFee));
				if(newFee.getDeleted()) {
					// if fee is deleted, copy all properties of old fee and set deleted indicator true
					FeeUIHolder fee  = (FeeUIHolder) oldFee.clone();
					fee.setFeeValue(ContractCustomizedFeeVO.ZERO);
					fee.setNotes(StringUtils.EMPTY);
					fee.setDeleted(true);
					changedFeeItems.add(fee);
				} else if (!oldFee.valueEquals(newFee)) {
					if(StringUtils.isEmpty(newFee.getFeeDescription())) {
						// if new fee description is made empty, set the fee as deleted
						FeeUIHolder fee  = (FeeUIHolder) oldFee.clone();
						fee.setFeeValue(ContractCustomizedFeeVO.ZERO);
						fee.setNotes(StringUtils.EMPTY);
						fee.setDeleted(true);
						changedFeeItems.add(fee);
					} else {
						changedFeeItems.add(newFee);
					}
				}
			} else {
				//  brand new fees added
				if(newFee.getFeeValueAsDecimal() > 0.0d) {
					changedFeeItems.add(newFee);
				}
			}
		}
		
		return changedFeeItems;
	}
	
	/**
	 * remove fee items which are empty
	 * 
	 * @param fees
	 * @return List<FeeUIHolder> 
	 */
	public static  List<FeeUIHolder> removeEmptyFeeObjects(List<FeeUIHolder> fees) {
		List<FeeUIHolder> feeList = new ArrayList<FeeUIHolder>();
		for(FeeUIHolder feeUi : fees) {
			if(StringUtils.isNotEmpty(feeUi.getFeeCode()) 
					||  !feeUi.isEmpty()){
				feeList.add(feeUi);
			}
		}
		return feeList;
	}
	
	/**
	 * remove fee items which are 0 value and empty
	 * 
	 * @param fees
	 * @return List<FeeUIHolder> 
	 */
	public static  List<FeeUIHolder> removeEmptyValueFeeObjects(List<FeeUIHolder> fees) {
		List<FeeUIHolder> feeList = new ArrayList<FeeUIHolder>();
		for(FeeUIHolder feeUi : fees) {
			if(StringUtils.isNotEmpty(feeUi.getFeeCode()) 
					||  !feeUi.isValueEmpty()){
				feeList.add(feeUi);
			}
		}
		return feeList;
	}    
	
	/**
	 * remove fee items which are 0 value
	 * 
	 * @param fees
	 * @return List<FeeUIHolder> 
	 */
	public static  List<FeeUIHolder> removeZeroValueFeeObjects(List<FeeUIHolder> fees) {
		List<FeeUIHolder> feeList = new ArrayList<FeeUIHolder>();
		for(FeeUIHolder feeUi : fees) {
			if(feeUi.getFeeValueAsDecimal() > 0.0d){
				feeList.add(feeUi);
			}
		}
		return feeList;
	}
	
	/**
	 * 
	 * add an empty custom fee if count less than 5
	 * 
	 * @param fees
	 * @return  List<FeeUIHolder>
	 */
	public static   List<FeeUIHolder> addEmptyCustomFees(List<FeeUIHolder> fees){
		fees = FeeUIHolder.removeEmptyFeeObjects(fees);
		int count = 0;
		for(FeeUIHolder fee : fees){
			if(!fee.getDeleted()) {
				count++;
			}
		}
		if(count < 5) {
			fees.add(new FeeUIHolder(new ContractCustomizedFeeVO()));
		}
		return fees;
	}
	
	/**
	 * assign fee codes
	 * 
	 * @param feeCodes
	 * @param changedItems
	 * @param actualList
	 */
	public static  void assignFeeCodes(TreeSet<String> feeCodes ,
			List<FeeUIHolder> changedItems, List<FeeUIHolder> actualList) {
		
		if(!feeCodes.isEmpty()) {
			// remove used fee codes
			for(FeeUIHolder fee : actualList) {
				if(StringUtils.isNotEmpty(fee.getFeeCode()) &&
						feeCodes.contains(fee.getFeeCode())) {
					feeCodes.remove(fee.getFeeCode());
				}
			}
			
			// add used fee codes back, as it is deleted
			for(FeeUIHolder fee : changedItems) {
				if(fee.getDeleted() 
						&& StringUtils.isNotEmpty(fee.getFeeCode())) {
					feeCodes.add(fee.getFeeCode());
				}
			}
			
			// assign fee codes
			for (FeeUIHolder fee : changedItems) {
				if (StringUtils.isEmpty(fee.getFeeCode())) {
					String code = feeCodes.first();
					fee.setFeeCode(code);
					feeCodes.remove(code);
				}
			}
		}
	}
}
