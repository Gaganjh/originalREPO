package com.manulife.pension.ps.web.tpafeeschedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.ps.web.fee.DesignatedInvestmentManagerUi;
import com.manulife.pension.ps.web.fee.FeeUIHolder;
import com.manulife.pension.ps.web.fee.PBAFeeUIHolder;
import com.manulife.pension.ps.web.fee.PBARestrictionUi;
import com.manulife.pension.ps.web.fee.PersonalBrokerageAccountUi;
import com.manulife.pension.ps.web.util.CloneableForm;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO;

/**
 * 
 * Action Form For TPA Customize Contract Fee
 * 
 * @author Siby Thomas
 *
 */
public class TPAContractFeeScheduleForm extends BaseFeeScheduleForm {
	
	private CloneableForm clonedForm;
	
	private static final long serialVersionUID = 1L;
	
	private List<ContractCustomizedFeeVO> tpaStandardScheduleFees = new ArrayList<ContractCustomizedFeeVO>();
	private List<ContractCustomizedFeeVO> tpaCustomizedScheduleFees = new ArrayList<ContractCustomizedFeeVO>();
	
	private List<FeeUIHolder> tpaFeesStandard = new ArrayList<FeeUIHolder>();
	private List<FeeUIHolder> tpaFeesCustomized = new ArrayList<FeeUIHolder>();
	
	private List<FeeUIHolder> updatedTpaFeesStandard = new ArrayList<FeeUIHolder>();
	private List<FeeUIHolder> updatedTpaFeesCustomized = new ArrayList<FeeUIHolder>();
	
	private List<FeeUIHolder> standardScheduleFeesToReset = new ArrayList<FeeUIHolder>();
	
	private String selectedContract;
	private String selectedContractName;
	
	private int tpaId;
	private String tpaFeeType;

	private String lastUpdatedUserId;
	private Date lastUpdateDate;
	

	private PageMode pageMode = PageMode.Unknown;
	private String dirty = Boolean.FALSE.toString();
	
	private boolean resetToStandardSchedule = false;
	
	private boolean fee404a5AccessPermission = false;
	
	
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
	
	public String getSelectedContract() {
		return selectedContract;
	}
	public void setSelectedContract(String selectedContract) {
		this.selectedContract = selectedContract;
	}
	
	public String getSelectedContractName() {
		return selectedContractName;
	}
	public void setSelectedContractName(String selectedContractName) {
		this.selectedContractName = selectedContractName;
	}
	
	public List<ContractCustomizedFeeVO> getTpaCustomizedScheduleFees() {
		return tpaCustomizedScheduleFees;
	}
	public void setTpaCustomizedScheduleFees(
			List<ContractCustomizedFeeVO> tpaCustomizedScheduleFees) {
		this.tpaCustomizedScheduleFees = tpaCustomizedScheduleFees;
	}

	
	public List<ContractCustomizedFeeVO> getTpaStandardScheduleFees() {
		return tpaStandardScheduleFees;
	}
	public void setTpaStandardScheduleFees(
			List<ContractCustomizedFeeVO> tpaStandardScheduleFees) {
		this.tpaStandardScheduleFees = tpaStandardScheduleFees;
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
	
	public List<FeeUIHolder> getStandardScheduleFeesToReset() {
		return standardScheduleFeesToReset;
	}

	public void setStandardScheduleFeesToReset(
			List<FeeUIHolder> standardScheduleFeesToReset) {
		this.standardScheduleFeesToReset = standardScheduleFeesToReset;
	}

	public void setTpaFeesCustomized(List<FeeUIHolder> tpaFeesCustomized) {
		this.tpaFeesCustomized = tpaFeesCustomized;
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

	public int getTpaId() {
		return tpaId;
	}
	public void setTpaId(int tpaId) {
		this.tpaId = tpaId;
	}

	public boolean isResetToStandardSchedule() {
		return resetToStandardSchedule;
	}
	public void setResetToStandardSchedule(boolean resetToStandardSchedule) {
		this.resetToStandardSchedule = resetToStandardSchedule;
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
		this.updatedTpaFeesCustomized = new ArrayList<FeeUIHolder>();
		this.updatedTpaFeesStandard = new ArrayList<FeeUIHolder>();
		this.tpaFeesCustomized =null;
		this.tpaFeesStandard = null;
		this.tpaFeeType = null;
		this.lastUpdateDate = null;
		this.lastUpdatedUserId = null;
		this.dirty = Boolean.FALSE.toString();
		this.resetToStandardSchedule = false;
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

		TPAContractFeeScheduleForm cloneForm = new TPAContractFeeScheduleForm();
		
		List<FeeUIHolder> tpaFeesCustomized = new ArrayList<FeeUIHolder>();
		List<FeeUIHolder> tpaFeesStandard = new ArrayList<FeeUIHolder>();
		
		for (FeeUIHolder fee : getTpaFeesCustomized()) {
			tpaFeesCustomized.add(new FeeUIHolder(fee.getContractCustomizedFeeVO()));
		}
		for (FeeUIHolder fee : getTpaFeesStandard()) {
			tpaFeesStandard.add(new FeeUIHolder(fee.getContractCustomizedFeeVO()));
		}
		
		cloneForm.setTpaFeesCustomized(tpaFeesCustomized);
		cloneForm.setTpaFeesStandard(tpaFeesStandard);

		return cloneForm;

	}
	
	public boolean isFee404a5AccessPermission() {
		return fee404a5AccessPermission;
	}
	public void setFee404a5AccessPermission(
			boolean fee404a5AccessPermission) {
		this.fee404a5AccessPermission = fee404a5AccessPermission;
	}
	@Override
	public DesignatedInvestmentManagerUi getDesignatedInvestmentManagerUi(){
		throw new UnsupportedOperationException("Unsupported Access of getDesignatedInvestmentManagerUi() from TPAContractFeeScheduleForm.");
	}
	@Override
	public List<FeeUIHolder> getNonTpaFees(){
		throw new UnsupportedOperationException("Unsupported Access of getNonTpaFees() from TPAContractFeeScheduleForm.");
	}
	
	@Override
	public List<PBARestrictionUi> getPbaRestrictionList(){
		throw new UnsupportedOperationException("Unsupported Access of getPbaRestrictionList() from TPAContractFeeScheduleForm.");
	}
	
	@Override
	public List<PBAFeeUIHolder> getStandardPBAFees(){
		throw new UnsupportedOperationException("Unsupported Access of getStandardPBAFees() from TPAContractFeeScheduleForm.");
	}
	
	@Override
	public List<PBAFeeUIHolder> getCustomPBAFees(){
		throw new UnsupportedOperationException("Unsupported Access of getCustomPBAFees() from TPAContractFeeScheduleForm.");
	}
	
	@Override
	public PersonalBrokerageAccountUi getPersonalBrokerageAccountUi(){
		throw new UnsupportedOperationException("Unsupported Access of getPersonalBrokerageAccountUi() from TPAContractFeeScheduleForm.");
	}
}