package com.manulife.pension.bd.web.fap;

import com.manulife.pension.platform.web.investment.ChartDataBean;
import com.manulife.pension.platform.web.investment.PerformanceChartInputForm;

/**
 * BDPerformanceChartInputForm form bean extends the PerformanceChartInputForm where you 
 * have all the fund selection and fund percentage properties. This form bean would be having 
 * the additional attributes that are required by the BrokerDealerWeb
 * 
 * @author SAyyalusamy
 */
public class BDPerformanceChartInputForm extends PerformanceChartInputForm {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private String fundType = "";
	private String userPreference = "";
	private String fundClass = "B";
	private boolean NMLSelection = false;
	private String checkBoxValue= "";
	private ChartDataBean chartDataBean = null;
	private boolean fromInput;
	protected boolean    hideDivSec = true;
	protected boolean userNMLAssociated = false;
	protected boolean jhiIndicator = false;
	
	private String fund1= "";
	private String fund2= "";
	private String fund3= "";
	private String fund4= "";
	private String fund5= "";
	private String fund6= "";
	/**
	 * Default Constructor
	 */
	public BDPerformanceChartInputForm() {
		
	}
	
	/**
	 * Returns the Fund Type
	 *  
	 * @return fundType
	 * 			String
	 */
	public String getFundType() {
		return fundType;
	}

	/**
	 * Sets the Fund Type
	 * 
	 * @param fundType
	 * 			String
	 */
	public void setFundType(String fundType) {
		this.fundType = fundType;
	}

	/**
	 * Returns the user Preference (US or NY)
	 * 
	 * @return userPreference
	 * 			String
	 */
	public String getUserPreference() {
		return userPreference;
	}

	/**
	 * Sets the user preference (US or NY)
	 * 
	 * @param userPreference
	 * 			String
	 */
	public void setUserPreference(String userPreference) {
		this.userPreference = userPreference;
	}

	/**
	 * Returns the fund class 
	 * 
	 * @return fundClass 	
	 * 			String
	 */
	public String getFundClass() {
		return fundClass;
	}
	
	/**
	 * Sets the user selected fundClass
	 * 
	 * @param fundClass 
	 * 			String
	 */
	public void setFundClass(String fundClass) {
		this.fundClass = fundClass;
	}

	/**
	 * Returns the NML selection value.
	 * 'Y' if selected else 'N'
	 * 
	 * @return NMLSelection
	 * 			String
	 */
	public boolean getNMLSelection() {
		return NMLSelection;
	}

	/**
	 * Sets the NML Selection value ('Y' or 'N')
	 * 
	 * @param selection the NMLSelection to set
	 */
	public void setNMLSelection(boolean selection) {
		NMLSelection = selection;
	}
	/**
	 * Returns the ChartDataBean  value.
	 * 
	 * @return chartDataBean
	 * 			ChartDataBean
	 */
	public ChartDataBean getChartDataBean() {
		return chartDataBean;
	}
	/**
	 * Sets the ChartDataBean
	 * 
	 * @param chartDataBean 
	 * 			ChartDataBean
	 */
	public void setChartDataBean(ChartDataBean chartDataBean) {
		this.chartDataBean = chartDataBean;
	}
	/**
	 * Returns the CheckBoxValue  value.
	 * 
	 * @return CheckBoxValue
	 * 			CheckBoxValue
	 */
	public String getCheckBoxValue() {
		return checkBoxValue;
	}
	/**
	 * Sets the checkBoxValue
	 * 
	 * @param checkBoxValue 
	 * 			checkBoxValue
	 */
	public void setCheckBoxValue(String checkBoxValue) {
		this.checkBoxValue = checkBoxValue;
	}
	/**
	 * Returns the fromInput  value.
	 * 
	 * @return fromInput
	 * 			fromInput
	 */
	public boolean isFromInput() {
		return fromInput;
	}
	/**
	 * Sets the fromInput
	 * 
	 * @param fromInput 
	 * 			fromInput
	 */
	public void setFromInput(boolean fromInput) {
		this.fromInput = fromInput;
	}

	public boolean isHideDivSec() {
		return hideDivSec;
	}

	public void setHideDivSec(boolean hideDivSec) {
		this.hideDivSec = hideDivSec;
	}

	public boolean isUserNMLAssociated() {
		return userNMLAssociated;
	}

	public void setUserNMLAssociated(boolean userNMLAssociated) {
		this.userNMLAssociated = userNMLAssociated;
	}

	public String getFund1() {
		return fund1;
	}

	public void setFund1(String fund1) {
		this.fund1 = fund1;
	}

	public String getFund2() {
		return fund2;
	}

	public void setFund2(String fund2) {
		this.fund2 = fund2;
	}

	public String getFund3() {
		return fund3;
	}

	public void setFund3(String fund3) {
		this.fund3 = fund3;
	}

	public String getFund4() {
		return fund4;
	}

	public void setFund4(String fund4) {
		this.fund4 = fund4;
	}

	public String getFund5() {
		return fund5;
	}

	public void setFund5(String fund5) {
		this.fund5 = fund5;
	}

	public String getFund6() {
		return fund6;
	}

	public void setFund6(String fund6) {
		this.fund6 = fund6;
	}

	public void resetFundIds(){
		this.fund1 = "";
		this.fund2 = "";
		this.fund3 = "";
		this.fund4 = "";
		this.fund5 = "";
		this.fund6 = "";
	}

	public boolean isJhiIndicator() {
		return jhiIndicator;
	}

	public void setJhiIndicator(boolean jhiIndicator) {
		this.jhiIndicator = jhiIndicator;
	}

}
