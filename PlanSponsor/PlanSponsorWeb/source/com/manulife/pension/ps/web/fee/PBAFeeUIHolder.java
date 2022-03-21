package com.manulife.pension.ps.web.fee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO.AmountType;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO;
import com.manulife.pension.service.fee.valueobject.PBAFeeVO;

/**
 * 
 * PBA Fee UI value object
 * 
 * @author Dheepa Poonagal
 *
 */
public class PBAFeeUIHolder implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	private static final String PERCENTAGE_FORMAT = "###0.###";
	private static final String PERCENTAGE_FORMAT_FOR_EDIT_PAGE = "###0.000";
	private static final String DOLLOR_FORMAT = "#,##0.00";
	public static final String DOLLAR ="D";
	public static final String PERCENTAGE ="P";
	
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
	
	private PBAFeeVO pbaFeeVO = new PBAFeeVO();
	
	private String feeTypeCode = StringUtils.EMPTY;
	private String feeValue = StringUtils.EMPTY;
	private String feeValueType = AmountType.DOLLAR.getCode();
	private String feeDescription = StringUtils.EMPTY;
	private boolean deleted = false;	
	private boolean isExisting = false;
	public PBAFeeUIHolder() {
		
	}
	
	public PBAFeeUIHolder(PBAFeeVO pBAFeeVO) {
		this.pbaFeeVO = pBAFeeVO;
		this.feeTypeCode = pBAFeeVO.getPbaFeeTypeCode();
		this.feeValueType = pBAFeeVO.getPbaFeeUnitType();
		if((pBAFeeVO.getPbaFeeAmount()!=null && pBAFeeVO.getPbaFeeAmount().compareTo(BigDecimal.ZERO)==0) || (pBAFeeVO.getPbaFeeAmount() == null)) {
			this.feeValue = StringUtils.EMPTY;
		} else {
			this.feeValue = getDecimalFormatted(String.valueOf(pBAFeeVO.getPbaFeeAmount().doubleValue()), 
					pBAFeeVO.getPbaFeeUnitType());
		}
		this.feeDescription = pBAFeeVO.getPbaFeeDescription();
		this.deleted = pBAFeeVO.isDeletedInd();
		this.isExisting = pBAFeeVO.isExisting();
	}
	
	public void setPbaFeeVO(PBAFeeVO pbaFeeVO) {
		this.pbaFeeVO = pbaFeeVO;
	}
	
	
	public String getNonFormattedAmountValue() {
		String value = ZERO_VALUE;
		if (StringUtils.isNotBlank(this.feeValue)) {
			value = StringUtils.replace(this.feeValue, ",", "");
		}
		return value;
	}
	
	public PBAFeeVO getPbaFeeVO() {		
		double value = getFeeValueAsDecimal();
		this.pbaFeeVO.setPbaFeeAmount(new BigDecimal(String.valueOf(value)));
		this.pbaFeeVO.setPbaFeeDescription(getFeeDescription());
		this.pbaFeeVO.setDeletedInd(getDeleted());
		this.pbaFeeVO.setPbaFeeTypeCode(getFeeTypeCode());
		this.pbaFeeVO.setPbaFeeUnitType(getFeeValueType());
		this.pbaFeeVO.setExisting(isExisting());
		return this.pbaFeeVO;
	}	
	
	public Integer getSortSequenceNo() {
		return this.pbaFeeVO.getSequenceNo();
	}	
	
	public String getFeeTypeCode() {
		return StringUtils.trimToEmpty(this.feeTypeCode);
	}

	public void setFeeTypeCode(String feeTypeCode) {
		this.feeTypeCode = feeTypeCode;
	}

	public String getFeeDescription() {
		return StringUtils.trimToEmpty(this.feeDescription);
	}
	public void setFeeDescription(String pbaFeeDescription) {
		this.feeDescription = (StringUtils.trimToEmpty(pbaFeeDescription));
	}
	
	
	public boolean getDeleted() {
		return this.deleted;
	}
	public void setDeleted(boolean isDelete) {
		this.deleted = isDelete;
	}	
	
	private String getDecimalFormatted(String amountValue){
		String value = StringUtils.EMPTY;
		if (StringUtils.isBlank(amountValue)) {
			amountValue = PBAFeeVO.ZERO_AMOUNT_STRING;
		}
		if(DOLLAR.equals(this.getFeeValueType())) {
			value = String.valueOf(formatDecimalValue(new BigDecimal(amountValue),DOLLOR_FORMAT));
		} else if(PERCENTAGE.equals(this.getFeeValueType())){
			value = String.valueOf(formatDecimalValue(new BigDecimal(amountValue),PERCENTAGE_FORMAT));
		}
		return value;
	}
	
	private String getDecimalFormatted(String amountValue, String amountType){
		String value = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(amountValue)) {
			if(DOLLAR.equals(amountType)) {
				value = String.valueOf(formatDecimalValue(new BigDecimal(amountValue),DOLLOR_FORMAT));
			} else if(PERCENTAGE.equals(amountType)) {
				value = String.valueOf(formatDecimalValue(new BigDecimal(amountValue),PERCENTAGE_FORMAT_FOR_EDIT_PAGE));
			}
		} else {
			value = PBAFeeVO.ZERO_AMOUNT_STRING;
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
	
	public boolean isExisting() {
		return isExisting;
	}

	public void setExisting(boolean isExisting) {
		this.isExisting = isExisting;
	}

	public String getAmountValueFormatted() {
		
		//display the amount when deleted as 0$ or 0%.
		String amountValue = PBAFeeVO.ZERO;
		if(!this.deleted){
			amountValue = this.getNonFormattedAmountValue();
		}
		
		String formattedValue = getDecimalFormatted(amountValue);
		if(this.getFeeValueType().equals(DOLLAR)) {
			return AmountType.DOLLAR.getCode().concat(formattedValue);
		} else if(this.getFeeValueType().equals(PERCENTAGE)){
			return formattedValue.concat(AmountType.PERCETAGE.getCode());
		} else {
			return StringUtils.EMPTY;
		}
	}

	@Override
    public boolean equals(Object o) {
		if(o == null){
			return false;
		}
		if(this.getClass() != o.getClass()){
			return false;
		}
		PBAFeeUIHolder vo = (PBAFeeUIHolder) o;
		if(StringUtils.isEmpty(this.getFeeTypeCode()) ||  StringUtils.isEmpty(vo.getFeeTypeCode())) {
			return false;
		}
	   return vo.getFeeTypeCode().equals(this.getFeeTypeCode());
    }
	
	@Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + feeTypeCode.hashCode();        
        return result;
    }
	
    public boolean valueEquals(Object o) {
		PBAFeeUIHolder vo = (PBAFeeUIHolder) o;
		if (StringUtils.equals(vo.getFeeDescription(), this.getFeeDescription())
				&& StringUtils.equals(vo.getFeeValueType(), this.getFeeValueType())
				&& vo.getFeeValueAsDecimal() == this.getFeeValueAsDecimal()
				&& vo.getDeleted() == this.getDeleted()) {
			return true;
		} 
		return false;
    }
	
    
	public boolean isEmpty() {
		if (StringUtils.isEmpty(this.getFeeDescription())
				&& StringUtils.isEmpty(this.getFeeValue())) {
			return true;
		}
		return false;
	}
	
	public boolean isValueEmpty() {
		if (StringUtils.isEmpty(this.getFeeDescription())
				&& getFeeValueAsDecimal() == 0.0d) {
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
		PBAFeeUIHolder fee = new PBAFeeUIHolder();
		fee.setFeeDescription(this.feeDescription);
		fee.setFeeValue(this.feeValue);
		fee.setFeeValueType(this.feeValueType);
		fee.setFeeTypeCode(this.feeTypeCode);
		fee.setDeleted(this.deleted);
		PBAFeeVO feeVO = new PBAFeeVO();
		feeVO.setPbaFeeTypeCode(this.pbaFeeVO.getPbaFeeTypeCode());
		feeVO.setContractId(this.pbaFeeVO.getContractId());
		feeVO.setSequenceNo(this.pbaFeeVO.getSequenceNo());
		fee.setPbaFeeVO(feeVO);
		return fee;
	}
	
	/**
	 * 
	 * get changed fee items
	 * 
	 * @param editedFees
	 * @param actualFees
	 * 
	 * @return List<PBAFeeUIHolder>
	 * 
	 * @throws SystemException 
	 * @throw  CloneNotSupportedException
	 */
	public static List<PBAFeeUIHolder> getChangedFeeItems(List<PBAFeeUIHolder> editedFees, List<PBAFeeUIHolder> actualFees) throws SystemException, CloneNotSupportedException {
		List<PBAFeeUIHolder> changedFeeItems = new ArrayList<PBAFeeUIHolder>();
		for (PBAFeeUIHolder newFee : editedFees) {
			if (actualFees.contains(newFee)) {
				PBAFeeUIHolder oldFee = actualFees.get(actualFees.indexOf(newFee));
				if(newFee.getDeleted()) {
					// if fee is deleted, copy all properties of old fee and set deleted indicator true
					PBAFeeUIHolder fee  = (PBAFeeUIHolder) oldFee.clone();
					//TODO raised query - #18
					//fee.setFeeValue(PBAFeeVO.ZERO);
					fee.setDeleted(true);
					changedFeeItems.add(fee);
				} else if (!oldFee.valueEquals(newFee)) {
					if(StringUtils.isEmpty(newFee.getFeeDescription())) {
						// if new fee description is made empty, set the fee as deleted
						PBAFeeUIHolder fee  = (PBAFeeUIHolder) oldFee.clone();
						fee.setFeeValue(PBAFeeVO.ZERO);
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
	 * @return List<PBAFeeUIHolder> 
	 */
	public static  List<PBAFeeUIHolder> removeEmptyFeeObjects(List<PBAFeeUIHolder> fees) {
		List<PBAFeeUIHolder> feeList = new ArrayList<PBAFeeUIHolder>();
		for(PBAFeeUIHolder feeUi : fees) {
			if(StringUtils.isNotEmpty(feeUi.getFeeTypeCode()) 
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
	 * @return List<PBAFeeUIHolder> 
	 */
	public static  List<PBAFeeUIHolder> removeEmptyValueFeeObjects(List<PBAFeeUIHolder> fees) {
		List<PBAFeeUIHolder> feeList = new ArrayList<PBAFeeUIHolder>();
		for(PBAFeeUIHolder feeUi : fees) {
			if(StringUtils.isNotEmpty(feeUi.getFeeTypeCode()) 
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
	 * @return List<PBAFeeUIHolder> 
	 */
	public static  List<PBAFeeUIHolder> removeZeroValueFeeObjects(List<PBAFeeUIHolder> fees) {
		List<PBAFeeUIHolder> feeList = new ArrayList<PBAFeeUIHolder>();
		for(PBAFeeUIHolder feeUi : fees) {
			if(feeUi.getFeeValueAsDecimal() > 0.0d){
				feeList.add(feeUi);
			}
		}
		return feeList;
	}
	
	/**
	 * 
	 * add an empty custom fee if count less than 10
	 * 
	 * @param fees
	 * @return  List<PBAFeeUIHolder>
	 */
	public static   List<PBAFeeUIHolder> addEmptyCustomFees(List<PBAFeeUIHolder> fees){
		fees = PBAFeeUIHolder.removeEmptyFeeObjects(fees);
		int count = 0;
		for(PBAFeeUIHolder fee : fees){
			if(!fee.getDeleted()) {
				count++;
			}
		}
		if(count < 10) {
			fees.add(new PBAFeeUIHolder(new PBAFeeVO()));
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
			List<PBAFeeUIHolder> changedItems, List<PBAFeeUIHolder> actualList) {
		
		if(!feeCodes.isEmpty()) {
			// remove used fee codes
			for(PBAFeeUIHolder fee : actualList) {
				if(StringUtils.isNotEmpty(fee.getFeeTypeCode()) &&
						feeCodes.contains(fee.getFeeTypeCode())) {
					feeCodes.remove(fee.getFeeTypeCode());
				}
			}
			
			// add used fee codes back, as it is deleted
			for(PBAFeeUIHolder fee : changedItems) {
				if(fee.getDeleted() 
						&& StringUtils.isNotEmpty(fee.getFeeTypeCode())) {
					feeCodes.add(fee.getFeeTypeCode());
				}
			}
			
			// assign fee codes
			for (PBAFeeUIHolder fee : changedItems) {
				if (StringUtils.isEmpty(fee.getFeeTypeCode())) {
					String code = feeCodes.first();
					fee.setFeeTypeCode(code);
					feeCodes.remove(code);
				}
			}
		}
	}
	
	
	public static List<PBAFeeUIHolder> removeBlankFeeObjects(List<PBAFeeUIHolder> fees) {
		List<PBAFeeUIHolder> feeList = new ArrayList<PBAFeeUIHolder>();
		for(PBAFeeUIHolder feeUi : fees) {
			if(StringUtils.isNotEmpty(feeUi.getFeeTypeCode()) ||  !feeUi.isEmpty()){
				feeList.add(feeUi);
			}
		}
		return feeList;
	}
}
