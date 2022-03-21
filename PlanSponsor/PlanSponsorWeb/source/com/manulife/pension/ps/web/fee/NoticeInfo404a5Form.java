package com.manulife.pension.ps.web.fee;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.ps.web.tpafeeschedule.BaseFeeScheduleForm;
import com.manulife.pension.ps.web.util.CloneableForm;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO;
import com.manulife.pension.service.fee.valueobject.PBAFeeVO;
import com.manulife.pension.service.fee.valueobject.PBARestriction;

public class NoticeInfo404a5Form extends BaseFeeScheduleForm {
	
	private CloneableForm clonedForm;
	
	private static final long serialVersionUID = 1L;
	
	
	private DesignatedInvestmentManagerUi designatedInvestmentManagerUi = null;
	private PersonalBrokerageAccountUi personalBrokerageAccountUi = null;
	private List<PBAFeeUIHolder> standardPBAFees = new ArrayList<PBAFeeUIHolder>();
	private List<PBAFeeUIHolder> customPBAFees = new ArrayList<PBAFeeUIHolder>();
	private List<PBAFeeUIHolder> updatedStandardPBAFees = new ArrayList<PBAFeeUIHolder>();
	private List<PBAFeeUIHolder> updatedCustomPBAFees = new ArrayList<PBAFeeUIHolder>();
	
	private List<PBARestrictionUi> pbaRestrictionList = new ArrayList<PBARestrictionUi>();
	private List<PBARestrictionUi> updatedPbaRestrictionList = new ArrayList<PBARestrictionUi>();
	//To show up in Confirm page
	private List<PBARestrictionUi> addedPbaRestrictionList = new ArrayList<PBARestrictionUi>();
	private List<PBARestrictionUi> modifiedPbaRestrictionList = new ArrayList<PBARestrictionUi>();
	private List<PBARestrictionUi> deletedPbaRestrictionList = new ArrayList<PBARestrictionUi>();
	
	
	private List<FeeUIHolder> nonTpaFees = new ArrayList<FeeUIHolder>();
	private List<FeeUIHolder> tpaFeesStandard = new ArrayList<FeeUIHolder>();
	private List<FeeUIHolder> tpaFeesCustomized = new ArrayList<FeeUIHolder>();
	
	private List<FeeUIHolder> updatedNonTpaFees = new ArrayList<FeeUIHolder>();
	private List<FeeUIHolder> updatedTpaFeesStandard = new ArrayList<FeeUIHolder>();
	private List<FeeUIHolder> updatedTpaFeesCustomized = new ArrayList<FeeUIHolder>();
	
	private int tpaId;
	private String tpaFeeType;
	private boolean tpaRestricted;
	private String selectedContract;

	private String lastUpdatedUserId;
	private Date lastUpdateDate;
	
	private PageMode pageMode = PageMode.Unknown;
	private Set<String> states = new LinkedHashSet<String>();
	private String dirty = Boolean.FALSE.toString();
	
	private List<ContractCustomizedFeeVO> tpaCustomizedScheduleFees = new ArrayList<ContractCustomizedFeeVO>();
	
	public List<ContractCustomizedFeeVO> getTpaCustomizedScheduleFees() {
		return tpaCustomizedScheduleFees;
	}
	public void setTpaCustomizedScheduleFees(
			List<ContractCustomizedFeeVO> tpaCustomizedScheduleFees) {
		this.tpaCustomizedScheduleFees = tpaCustomizedScheduleFees;
	}
	
	public List<FeeUIHolder> getUpdatedNonTpaFees() {
		return updatedNonTpaFees;
	}
	public void setUpdatedNonTpaFees(List<FeeUIHolder> updatedNonTpaFees) {
		this.updatedNonTpaFees = updatedNonTpaFees;
	}
	
	public List<FeeUIHolder> getUpdatedTpaFeesStandard() {
		return updatedTpaFeesStandard;
	}
	public void setUpdatedTpaFeesStandard(List<FeeUIHolder> updatedTpaFeesStandard) {
		this.updatedTpaFeesStandard = updatedTpaFeesStandard;
	}
	
	public List<FeeUIHolder> getUpdatedTpaFeesCustomized() {
		return updatedTpaFeesCustomized;
	}
	public void setUpdatedTpaFeesCustomized(
			List<FeeUIHolder> updatedTpaFeesCustomized) {
		this.updatedTpaFeesCustomized = updatedTpaFeesCustomized;
	}
	
	public List<FeeUIHolder> getTpaFeesStandard() {
		return tpaFeesStandard;
	}
	public void setTpaFeesStandard(List<FeeUIHolder> tpaFeesStandard) {
		this.tpaFeesStandard = tpaFeesStandard;
	}
	
	public List<FeeUIHolder> getTpaFeesCustomized() {
		return this.tpaFeesCustomized;
	}
	
	public void setTpaFeesCustomized(List<FeeUIHolder> tpaFeesCustomized) {
		this.tpaFeesCustomized = tpaFeesCustomized;
	}
	
	public void setNonTpaFees(List<FeeUIHolder> nonTpaFees) {
		this.nonTpaFees = nonTpaFees;
	}
	public List<FeeUIHolder> getNonTpaFees() {
		return this.nonTpaFees;
	}
	
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	public boolean isTpaRestricted() {
		return tpaRestricted;
	}
	public void setTpaRestricted(boolean tpaRestricted) {
		this.tpaRestricted = tpaRestricted;
	}
	
	public PageMode getPageMode() {
		return pageMode;
	}
	public void setPageMode(PageMode pageMode) {
		this.pageMode = pageMode;
	}
	
	public String getTpaFeeType() {
		return tpaFeeType;
	}
	public void setTpaFeeType(String tpaFeeType) {
		this.tpaFeeType = tpaFeeType;
	}
	
	public FeeUIHolder getTpaStandardFee(int index) {
		for (int i = getTpaFeesStandard().size(); i <= index; i++) {
			getTpaFeesStandard().add(new FeeUIHolder(new ContractCustomizedFeeVO()));
		}
		return ((FeeUIHolder) getTpaFeesStandard().get(index));
	}

	public FeeUIHolder getTpaCustomizedFee(int index) {
		for (int i = getTpaFeesCustomized().size(); i <= index; i++) {
			getTpaFeesCustomized().add(new FeeUIHolder(new ContractCustomizedFeeVO()));
		}
		return ((FeeUIHolder) getTpaFeesCustomized().get(index));
	}

	public FeeUIHolder getNonTpaFee(int index) {
		for (int i = getNonTpaFees().size(); i <= index; i++) {
			getNonTpaFees().add(new FeeUIHolder(new ContractCustomizedFeeVO()));
		}
		return ((FeeUIHolder) getNonTpaFees().get(index));
	}
	
	public PBAFeeUIHolder getPbaStandardFee(int index) {
		for (int i = getStandardPBAFees().size(); i <= index; i++) {
			getStandardPBAFees().add(new PBAFeeUIHolder(new PBAFeeVO()));
		}
		return ((PBAFeeUIHolder) getStandardPBAFees().get(index));
	}
	
	public PBAFeeUIHolder getPbaCustomizedFee(int index) {
		for (int i = getCustomPBAFees().size(); i <= index; i++) {
			getCustomPBAFees().add(new PBAFeeUIHolder(new PBAFeeVO()));
		}
		return ((PBAFeeUIHolder) getCustomPBAFees().get(index));
	}
	
	public PBARestrictionUi getPbaRestriction(int index){
		for(int i=getPbaRestrictionList().size(); i<=index; i++){
			getPbaRestrictionList().add(new PBARestrictionUi(new PBARestriction()));
		}
		return ((PBARestrictionUi) getPbaRestrictionList().get(index));
	}

	public DesignatedInvestmentManagerUi getDesignatedInvestmentManagerUi() {
		return designatedInvestmentManagerUi;
	}
	public void setDesignatedInvestmentManagerUi(
			DesignatedInvestmentManagerUi designatedInvestmentManagerUi) {
		this.designatedInvestmentManagerUi = designatedInvestmentManagerUi;
	}
	
	public PersonalBrokerageAccountUi getPersonalBrokerageAccountUi() {
		return personalBrokerageAccountUi;
	}
	public void setPersonalBrokerageAccountUi(
			PersonalBrokerageAccountUi personalBrokerageAccountUi) {
		this.personalBrokerageAccountUi = personalBrokerageAccountUi;
	}
	
	public List<PBAFeeUIHolder> getStandardPBAFees() {
		return standardPBAFees;
	}
	public void setStandardPBAFees(List<PBAFeeUIHolder> standardPBAFees) {
		this.standardPBAFees = standardPBAFees;
	}
	public List<PBAFeeUIHolder> getCustomPBAFees() {
		return customPBAFees;
	}
	public void setCustomPBAFees(List<PBAFeeUIHolder> customPBAFees) {
		this.customPBAFees = customPBAFees;
	}
	/**
	 * @return the pbaRestrictionList
	 */
	public List<PBARestrictionUi> getPbaRestrictionList() {
		return pbaRestrictionList;
	}
	/**
	 * @param pbaRestrictionList the pbaRestrictionList to set
	 */
	public void setPbaRestrictionList(List<PBARestrictionUi> pbaRestrictionList) {
		this.pbaRestrictionList = pbaRestrictionList;
	}
	public List<PBARestrictionUi> getUpdatedPbaRestrictionList() {
		return updatedPbaRestrictionList;
	}
	public void setUpdatedPbaRestrictionList(
			List<PBARestrictionUi> updatedPbaRestrictionList) {
		this.updatedPbaRestrictionList = updatedPbaRestrictionList;
	}
	public List<PBARestrictionUi> getAddedPbaRestrictionList() {
		return addedPbaRestrictionList;
	}
	public void setAddedPbaRestrictionList(
			List<PBARestrictionUi> addedPbaRestrictionList) {
		this.addedPbaRestrictionList = addedPbaRestrictionList;
	}
	public List<PBARestrictionUi> getModifiedPbaRestrictionList() {
		return modifiedPbaRestrictionList;
	}
	public void setModifiedPbaRestrictionList(
			List<PBARestrictionUi> modifiedPbaRestrictionList) {
		this.modifiedPbaRestrictionList = modifiedPbaRestrictionList;
	}
	public List<PBARestrictionUi> getDeletedPbaRestrictionList() {
		return deletedPbaRestrictionList;
	}
	public void setDeletedPbaRestrictionList(
			List<PBARestrictionUi> deletedPbaRestrictionList) {
		this.deletedPbaRestrictionList = deletedPbaRestrictionList;
	}
	public List<PBAFeeUIHolder> getUpdatedStandardPBAFees() {
		return updatedStandardPBAFees;
	}
	public void setUpdatedStandardPBAFees(
			List<PBAFeeUIHolder> updatedStandardPBAFees) {
		this.updatedStandardPBAFees = updatedStandardPBAFees;
	}
	public List<PBAFeeUIHolder> getUpdatedCustomPBAFees() {
		return updatedCustomPBAFees;
	}
	public void setUpdatedCustomPBAFees(List<PBAFeeUIHolder> updatedCustomPBAFees) {
		this.updatedCustomPBAFees = updatedCustomPBAFees;
	}
	public int getTpaId() {
		return tpaId;
	}
	public void setTpaId(int tpaId) {
		this.tpaId = tpaId;
	}
	
	public Set<String> getStates() {
		return states;
	}
	public void setStates(Set<String> states) {
		this.states = states;
	}
	
    public String getDirty() {
        return dirty;
    }

    public void setDirty(final String dirty) {
        this.dirty = Boolean.parseBoolean(dirty) ? Boolean.TRUE.toString() : Boolean.FALSE
                .toString();
    }
	
	/**
	 * Get clonedForm
	 * 
	 * @return  clonedForm
	 */
	public CloneableForm getClonedForm() {
		return clonedForm;
	}

	/**
	 * Clear the cloneForm
	 */
	public void clear( HttpServletRequest request) {
		this.designatedInvestmentManagerUi = null;
		this.personalBrokerageAccountUi = null;
		this.pbaRestrictionList = null;
		this.updatedPbaRestrictionList = new ArrayList<PBARestrictionUi>();
		this.standardPBAFees = null;
		this.customPBAFees = null;
		this.updatedStandardPBAFees = new ArrayList<PBAFeeUIHolder>();
		this.updatedCustomPBAFees = new ArrayList<PBAFeeUIHolder>();
		this.updatedNonTpaFees = new ArrayList<FeeUIHolder>();
		this.updatedTpaFeesCustomized = new ArrayList<FeeUIHolder>();
		this.updatedTpaFeesStandard = new ArrayList<FeeUIHolder>();
		this.tpaFeesCustomized =null;
		this.tpaFeesStandard = null;
		this.nonTpaFees = null;
		this.tpaFeeType = null;
		this.dirty = Boolean.FALSE.toString();
		clonedForm = null;
	}
	
	/**
	 * Store the clone form to cloneForm
	 */
	public void storeClonedForm() {
		clonedForm = (CloneableForm) cloneForm();
	}
	

	/**
	 * method to clone the form and set the two lists for standard fee data and
	 * custom fee data obtained from the database.This will be required for
	 * matching the modified lists and filtering the modified items to the
	 * preview list.
	 * 
	 * @return CloneableForm
	 */
	private CloneableForm cloneForm() {

		NoticeInfo404a5Form cloneForm = new NoticeInfo404a5Form();
		
		List<FeeUIHolder> nonTpaFees = new ArrayList<FeeUIHolder>();
		List<FeeUIHolder> tpaFeesCustomized = new ArrayList<FeeUIHolder>();
		List<FeeUIHolder> tpaFeesStandard = new ArrayList<FeeUIHolder>();
		List<PBAFeeUIHolder> pbaFeesStandard = new ArrayList<PBAFeeUIHolder>();
		List<PBAFeeUIHolder> pbaFeesCustomized = new ArrayList<PBAFeeUIHolder>();
		List<PBARestrictionUi> pbaRestrictions = new ArrayList<PBARestrictionUi>();
		
		for (FeeUIHolder fee : getNonTpaFees()) {
			nonTpaFees.add(new FeeUIHolder(fee.getContractCustomizedFeeVO()));
		}
		for (FeeUIHolder fee : getTpaFeesCustomized()) {
			tpaFeesCustomized.add(new FeeUIHolder(fee.getContractCustomizedFeeVO()));
		}
		for (FeeUIHolder fee : getTpaFeesStandard()) {
			tpaFeesStandard.add(new FeeUIHolder(fee.getContractCustomizedFeeVO()));
		}
		for (PBAFeeUIHolder fee : getStandardPBAFees()) {
			pbaFeesStandard.add(new PBAFeeUIHolder(fee.getPbaFeeVO()));
		}
		for (PBAFeeUIHolder fee : getCustomPBAFees()){
			pbaFeesCustomized.add(new PBAFeeUIHolder(fee.getPbaFeeVO()));
		}
		for(PBARestrictionUi restriction: getPbaRestrictionList()){
			pbaRestrictions.add(new PBARestrictionUi(restriction.getPbaRestriction()));
		}
		
		cloneForm.setNonTpaFees(nonTpaFees);
		cloneForm.setTpaFeesCustomized(tpaFeesCustomized);
		cloneForm.setTpaFeesStandard(tpaFeesStandard);
		cloneForm.setStandardPBAFees(pbaFeesStandard);
		cloneForm.setCustomPBAFees(pbaFeesCustomized);
		cloneForm.setPbaRestrictionList(pbaRestrictions);

		return cloneForm;

	}	
	
	public boolean getHasTpaFees() {
		for(FeeUIHolder fee : getTpaFeesStandard()) {
			if(fee.getFeeValueAsDecimal() > 0.0d){
				return true;
			}
		}
		for(FeeUIHolder fee : getTpaFeesCustomized()) {
			if(fee.getFeeValueAsDecimal() > 0.0d){
				return true;
			}
		}
		return false;
	}
	@Override
	public String getSelectedContract() {
		return selectedContract;
	}
	
	public void setSelectedContract(String selectedContract) {
		this.selectedContract = selectedContract;
	}
	
}
