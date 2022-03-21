package com.manulife.pension.ps.web.fee;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.fee.valueobject.PBARestriction;


/**
 * 
 * Value Object to store PBA Restriction UI Details
 * 
 * @author Dheepa Poonagal
 *
 */

public class PBARestrictionUi implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private PBARestriction pbaRestriction = new PBARestriction();
	private String restrictionDesc = StringUtils.EMPTY;
	private boolean deletedInd = false;	
	
	public PBARestrictionUi(){
		
	}
	
	public PBARestrictionUi(PBARestriction pbaRestriction) {
		this.pbaRestriction = pbaRestriction;
		this.restrictionDesc = pbaRestriction.getRestriction();
		this.deletedInd = pbaRestriction.isDeletedInd();		
	}

	public PBARestriction getPbaRestriction() {
		this.pbaRestriction.setRestriction(getRestrictionDesc());
		this.pbaRestriction.setDeletedInd(isDeletedInd());
		return this.pbaRestriction;
	}

	public void setPbaRestriction(PBARestriction pbaRestriction) {
		this.pbaRestriction = pbaRestriction;
	}

	public String getRestrictionDesc() {
		return restrictionDesc;
	}

	public void setRestrictionDesc(String restrictionDesc) {
		this.restrictionDesc = restrictionDesc;
	}

	public boolean isDeletedInd() {
		return deletedInd;
	}

	public void setDeletedInd(boolean deletedInd) {
		this.deletedInd = deletedInd;
	}
	
	public Integer getSortSequenceNo() {
		return this.pbaRestriction.getSeqNo();
	}	
	
	
	/**
	 * 
	 * add an empty restriction if count less than 10
	 * 
	 * @param restrictions
	 * @return  List<PBARestrictionUi>
	 */
	public static   List<PBARestrictionUi> addEmptyCustomRestrictions(List<PBARestrictionUi> restrictions){
		restrictions = PBARestrictionUi.removeEmptyRestrictionObjects(restrictions);
		int count = 0;
		for(PBARestrictionUi res : restrictions){
			if(!res.isDeletedInd()) {
				count++;
			}
		}
		if(count < 10) {
			restrictions.add(new PBARestrictionUi(new PBARestriction()));
		}
		return restrictions;
	}
	
	/**
	 * remove restriction items which are empty
	 * 
	 * @param restrictions
	 * @return List<PBARestrictionUi> 
	 */
	public static  List<PBARestrictionUi> removeEmptyRestrictionObjects(List<PBARestrictionUi> restrictions) {
		List<PBARestrictionUi> restrictionList = new ArrayList<PBARestrictionUi>();
		for(PBARestrictionUi restrictionUi : restrictions) {
			if(!((StringUtils.isEmpty(restrictionUi.getRestrictionDesc()) || StringUtils.isBlank(restrictionUi.getRestrictionDesc())) && !restrictionUi.isDeletedInd())){
				restrictionList.add(restrictionUi);
			}			
		}
		return removeEmptyRestrictionRowDeleted(restrictionList);
	}	
	
	
	public static List<PBARestrictionUi> removeEmptyRestrictionRowDeleted(List<PBARestrictionUi> restrictions){
		List<PBARestrictionUi> restrictionList = new ArrayList<PBARestrictionUi>();
		for(PBARestrictionUi res: restrictions){
			if(!(res.isDeletedInd() && (res.getRestrictionDesc().isEmpty() || StringUtils.isBlank(res.getRestrictionDesc())) && res.getPbaRestriction().getSeqNo()==0)){
				restrictionList.add(res);
			}
		}
		return restrictionList;
	}
	
	
	/**
	 * 
	 * get changed restriction items
	 * 
	 * @param editedFees
	 * @param actualFees
	 * 
	 * @return List<PBAFeeUIHolder>
	 * 
	 * @throws SystemException 
	 * @throw  CloneNotSupportedException
	 */
	public static List<PBARestrictionUi> getChangedRestrictionItems(List<PBARestrictionUi> editedRestr, List<PBARestrictionUi> actualRestr) throws SystemException, CloneNotSupportedException {
		List<PBARestrictionUi> changedRestrictionItems = new ArrayList<PBARestrictionUi>();
		for (PBARestrictionUi newRestr : editedRestr) {
			if (actualRestr.contains(newRestr)) {
				PBARestrictionUi oldRestr = actualRestr.get(actualRestr.indexOf(newRestr));
				if(newRestr.isDeletedInd()) {
					// if restriction is deleted, copy all properties of old restriction and set deleted indicator true
					PBARestrictionUi fee  = (PBARestrictionUi) oldRestr.clone();
					//fee.setFeeValue(PBAFeeVO.ZERO);
					fee.setDeletedInd(true);
					changedRestrictionItems.add(fee);
				} else if (!oldRestr.valueEquals(newRestr)) {
					if(StringUtils.isEmpty(newRestr.getRestrictionDesc())) {
						// if new fee description is made empty, set the fee as deleted
						PBARestrictionUi fee  = (PBARestrictionUi) oldRestr.clone();						
						fee.setDeletedInd(true);
						changedRestrictionItems.add(fee);
					} else {
						changedRestrictionItems.add(newRestr);
					}
				}
			} else {
				
					changedRestrictionItems.add(newRestr);
				
			}
		}
		
		return changedRestrictionItems;
	}
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		PBARestrictionUi res = new PBARestrictionUi();
		res.setRestrictionDesc(this.restrictionDesc);		
		res.setDeletedInd(this.deletedInd);
		
		PBARestriction resVO = new PBARestriction();
		resVO.setSeqNo(this.pbaRestriction.getSeqNo());
		res.setPbaRestriction(resVO);
		return res;
	}
	
	
	public boolean valueEquals(Object o) {
		PBARestrictionUi vo = (PBARestrictionUi) o;
		if (StringUtils.equals(vo.getRestrictionDesc(), this.getRestrictionDesc())				
				&& vo.isDeletedInd() == this.isDeletedInd()) {
			return true;
		} 
		return false;
    }
	
	@Override
    public boolean equals(Object o) {
		if(o == null){
			return false;
		}
		if(this.getClass() != o.getClass()){
			return false;
		}
		PBARestrictionUi vo = (PBARestrictionUi) o;		
		return (vo.getPbaRestriction().getSeqNo() == this.getPbaRestriction().getSeqNo());
    }
	
	@Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + pbaRestriction.getSeqNo();        
        return result;
    }
	
	
	public boolean isEmpty() {
		if (StringUtils.isEmpty(this.getRestrictionDesc())) {
			return true;
		}
		return false;
	}
	
}
