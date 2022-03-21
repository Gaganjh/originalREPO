package com.manulife.pension.bd.web.bob.transaction;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantLoanDetails;

/**
 * Action Form class for Participant Transaction History page.
 * 
 * @author harlomte
 * 
 */
public class ParticipantTransactionHistoryForm extends BaseReportForm {
	
    private static final long serialVersionUID = -2123598251552149890L;

    public static final String PARAMETER_KEY_PROFILE_ID = "profileId";
	public static final String PARAMETER_KEY_PARTICIPANT_ID = "participantId";
	public static final String DUMMY_DATE = "01/01/0001";
	public static final String WITHDRAWAL_AMOUNT = "Withdrawal amount";
	public static final String DISTRIBUTION_AMOUNT = "Distribution amount";

	private String fromDate;
	private String toDate;
	private String transactionType;
	private String profileId;
	private String participantId;
	
	private boolean showAge = false;
	private boolean showPba = false;
	private boolean showLoans = false;
	private String selectedLoan = null;
	private ArrayList<Attribute> loanList = null;
	public List<LabelValueBean> typesDropdown = new ArrayList<LabelValueBean>();

    public List<LabelValueBean> noLonsTypesDropdown = new ArrayList<LabelValueBean>();

	/**
	 * 
	 */
	public ParticipantTransactionHistoryForm() {
		super();
	}
	
	/**
	 * Gets the fromDate
	 * @return Returns a String
	 */
	public String getFromDate() {
		return this.fromDate;
	}
	
	/**
	 * Sets the fromDate
	 * @param fromDate The fromDate to set
	 */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}


	/**
	 * Gets the toDate
	 * @return Returns a String
	 */
	public String getToDate() {
		return this.toDate;
	}
	/**
	 * Sets the toDate
	 * @param toDate The toDate to set
	 */
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}


	/**
	 * Gets the transactionType
	 * @return Returns a String
	 */
	public String getTransactionType() {
		return transactionType;
	}
	/**
	 * Sets the transactionType
	 * @param transactionType The transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * Gets the profileId
	 * @return Returns a String
	 */
	public String getProfileId() {
		return profileId;
	}
	/**
	 * Sets the profileId
	 * @param profileId The profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	
	/**
	 * Gets PArticipant Id
	 * @return String
	 */
	public String getParticipantId() {
		return participantId;
	}

	/**
	 * Sets Participant Id
	 * @param string
	 */
	public void setParticipantId(String string) {
		participantId = string;
	}

	public boolean getShowAge() {
		return showAge;
	}

	public void setShowAge(boolean showAge) {
		this.showAge = showAge;
	}

	public boolean getShowPba() {
		return showPba;
	}

	public void setShowPba(boolean showPba) {
		this.showPba = showPba;
	}

	public class Attribute implements Serializable 
 	{
        private static final long serialVersionUID = 6525053392774199860L;

        private String value;
 		private String label;
 
 		public Attribute(String value, String label)
 		{
 			this.value = value;
 			this.label = label;
 	
 		}	
 		
 		public String getValue() {
 			return value;
 		}
 		public void setValue(String value) {
 			this.value = value;
 		}
 		public String getLabel() {
 			return label;
 		}
 		public void setLabel(String label) {
 			this.label = label;
 		}
 	}

	/**
	 * Gets the showLoans
	 * @return Returns a boolean
	 */
	public boolean getShowLoans() {
		return showLoans;
	}
	/**
	 * Sets the showLoans
	 * @param showLoans The showLoans to set
	 */
	public void setShowLoans(boolean showLoans) {
		this.showLoans = showLoans;
	}
	
	/**
	 * Gets the selectedLoan
	 * @return Returns a String
	 */
	public String getSelectedLoan() {
		return selectedLoan;
	}
	/**
	 * Sets the selectedLoan
	 * @param selectedLoan The selectedLoan to set
	 */
	public void setSelectedLoan(String selectedLoan) {
		this.selectedLoan = selectedLoan;
	}

	/**
	 * Gets the loanList
	 * @return Returns a ArrayList
	 */
	public ArrayList<Attribute> getLoanList() {
		return loanList;
	}
	/**
	 * Sets the loanList
	 * @param loanList The loanList to set
	 */
	public void setLoanList(ArrayList<Attribute> loanList) {
		this.loanList = loanList;
	}
	
	/**
     * This method gets the Loan Details and create a new ArrayList.
     * 
     * @param loanDetailsList
     */
 	public void setLoanDetailList(Collection<ParticipantLoanDetails> loanDetailsList) {
 		 
		DecimalFormat formatter = new DecimalFormat(BDConstants.DOLLAR_WITH_4_PLACES_2_DECIMALS);
 
		loanList = new ArrayList<Attribute>();
 
		Attribute attVO = new Attribute("-1","View Loan Details");
		loanList.add(attVO);
 			
	    for (ParticipantLoanDetails loanDetail : loanDetailsList) {
			String loanLabel = "Loan #" 
			                    + loanDetail.getLoanId()
			                    + ": "
			                    + formatter.format(loanDetail.getOutstandingPrincipalAmount());
			loanList.add(new Attribute(loanDetail.getLoanId(),loanLabel));
		}
 	}
 	public List<LabelValueBean> getTypesDropdown() {
        return typesDropdown;
    }

    public void setTypesDropdown(List<LabelValueBean> typesDropdown) {
        this.typesDropdown = typesDropdown;
    }

    public List<LabelValueBean> getNoLonsTypesDropdown() {
        return noLonsTypesDropdown;
    }

    public void setNoLonsTypesDropdown(List<LabelValueBean> noLonsTypesDropdown) {
        this.noLonsTypesDropdown = noLonsTypesDropdown;
    }

}
