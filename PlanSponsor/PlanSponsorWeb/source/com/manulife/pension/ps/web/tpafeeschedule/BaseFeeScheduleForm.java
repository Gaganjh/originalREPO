package com.manulife.pension.ps.web.tpafeeschedule;

import java.util.Date;
import java.util.List;

import com.manulife.pension.ps.web.fee.DesignatedInvestmentManagerUi;
import com.manulife.pension.ps.web.fee.FeeUIHolder;
import com.manulife.pension.ps.web.fee.PBAFeeUIHolder;
import com.manulife.pension.ps.web.fee.PBARestrictionUi;
import com.manulife.pension.ps.web.fee.PersonalBrokerageAccountUi;
import com.manulife.pension.ps.web.util.CloneableAutoForm;

public abstract class BaseFeeScheduleForm extends CloneableAutoForm {
	
	public enum PageMode {
		Unknown,
		View,
		Edit,
		Confirm;
	}
	
	public abstract DesignatedInvestmentManagerUi getDesignatedInvestmentManagerUi();
	public abstract PersonalBrokerageAccountUi getPersonalBrokerageAccountUi();
	public abstract List<PBAFeeUIHolder> getStandardPBAFees();
	public abstract List<PBAFeeUIHolder> getCustomPBAFees();
	public abstract List<PBARestrictionUi> getPbaRestrictionList();
	public abstract List<FeeUIHolder> getTpaFeesStandard();
	public abstract List<FeeUIHolder> getTpaFeesCustomized();
	public abstract List<FeeUIHolder> getNonTpaFees();
	public abstract int getTpaId();
	public abstract String getSelectedContract();
	
	public abstract void setLastUpdateDate(Date lastUpdateDate);
	public abstract void setLastUpdatedUserId(String lastUpdatedUserId);
	
}
