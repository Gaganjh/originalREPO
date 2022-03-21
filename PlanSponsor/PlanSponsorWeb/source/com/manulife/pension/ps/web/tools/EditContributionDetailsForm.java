package com.manulife.pension.ps.web.tools;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;


import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Message;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData;
import com.manulife.pension.ps.service.submission.util.SubmissionErrorHelper;
import com.manulife.pension.ps.service.submission.valueobject.MoneyTypeHeader;
import com.manulife.pension.ps.service.submission.valueobject.ReportDataErrors;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionParticipant;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionPaymentItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper;
import com.manulife.pension.ps.web.util.CloneableForm;
import com.manulife.pension.service.contract.valueobject.ContractPaymentInfoVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.submission.SubmissionError;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.util.log.LogUtility;

/**
 * 
 * @author Tony Tomasone
 *
 */

public class EditContributionDetailsForm extends ReportForm implements SubmissionPaymentForm, CloneableForm {
	private static final String ELEMENTS__AMOUNTS = "elements['amounts[";
	private static final String ELEMENTS__BILLAMOUNTS = "elements['billAmounts[";
	private static final String ELEMENTS__CREDITAMOUNTS = "elements['creditAmounts[";
	private static final String NULL = "null";
	private static final String NEW_DATE = "new Date(";
	private static final String RIGHT_PAR = ")";
	private static final String RIGHT_BR = "]";
	private static final String LEFT_BR = "[";
	private static final String COMMA = ", ";
	private static final String DECIMAL_ZERO = "0.00";
	private static final String ID_99 = "99";
	private static final String NEW_ARRAY = "new Array(";
	private static final String COMMA_QUOTE = ", \"";
	private static final String QUOTE = "\"";
	private static final String END_BR = "]']\"";
	private static final String _ROW_ = "].row[";
	private static final String ELEMENTS__CONTRIBUTIONCOLUMNS = "elements['contributionColumnsMap[";
	private static final String ELEMENTS__LOANCOLUMNS = "elements['loanColumnsMap[";
	private static final String UPLOADFORMOBJ = "uploadFormObj.";
	private static final String EDIT_MODE_IND = "e";
	private static String ERROR_TYPE_ERROR = "error";
	private static String ERROR_TYPE_WARN = "warn";
	private static String ERROR_TYPE_NONE = "none";
	private static String ZERO_VALUE = DECIMAL_ZERO;
	private static String FALSE_VALUE = "false";
	private static String DEFAULT_MONEY_DD_LABEL = "Select Type";
	private static String DEFAULT_MONEY_DD_ID = "-1";
	private static String SAVE_TASK = "save";
	private static String SUBMIT_TASK = "submit";
	
	private CloneableForm clonedForm;
	private String subNo;
	private String mode;
	private String lastPayroll;
	private boolean isAllowedView = false;
	private boolean isCashAccountPresent = false;
	private boolean isDisplayGenerateStatementSection = false;
	private boolean isDisplayPaymentInstructionSection = false;
	private boolean isDisplayTemporaryCreditSection = false;
	private boolean isDisplayBillPaymentSection = false;
	private boolean isNoPermission = true;
	private ContributionDetailsReportData theReport = new ContributionDetailsReportData(new ReportCriteria(""),0);
	private BigDecimal participantTotal;
	private Map allocationTotalValues;
	private Collection loanTotalValues;
	private Map errorMap;
	private boolean isViewMode = false;
	private boolean isShowLoans = true;
	private boolean isShowConfirmDialog = false;
	private boolean isIgnoreDataCheckWarnings = false;
	private boolean isHasChanged = false;
	private Collection statementDates;
	private ContractPaymentInfoVO paymentInfo;
	private String payrollEffectiveDate;
	private String requestEffectiveDate;
	private String calendarStartDate;
	private String calendarEndDate;
	private String payrollCalendarStartDate;
	private String payrollCalendarEndDate;
	private String marketClose;
	private String moneySourceID;
	private Date defaultEffectiveDate;
	private Date [] allowedPaymentDates;
	private Date [] allowedPayrollDates;
	private Collection accounts;
	private List amounts;
	private List billAmounts;	
	private List creditAmounts;
	private List<RowVal> contributionColumns = new ArrayList<RowVal>();
	private Map employerIds = new HashMap();
	private Map recordNumbers = new HashMap();
	private List<RowVal> loanColumns = new ArrayList<RowVal>();
	private List<Boolean> deleteBoxes = new ArrayList<Boolean>();
	private Collection contractMoneySources;
	private List moneyTypeColumns = new ArrayList();
	private Collection contractMoneyTypes;
	private String warningMessage = "";
	private int myOwnPageNumber = 1;
	private boolean resubmit = false;
	private String forwardFromSave = "";
	
    public class RowVal implements Serializable {
		
		private List row;
		private List rowId;
		
		public List getRow() {
			if (row == null) {
				row=new ArrayList();
			}
			if(row.size() < theReport.getDetails().size()){
				for(int i = row.size();i < theReport.getDetails().size();i++){
					row.add(ZERO_VALUE);
				}
				
			}
			return row;
		}
		
		public List getRowId() {
			if (rowId == null) {
				rowId=new ArrayList();
			}
			if(rowId.size() < theReport.getDetails().size()){
				for(int i = rowId.size();i < theReport.getDetails().size();i++){
					rowId.add(ZERO_VALUE);
				}
				
			}
			return rowId;
		}

		public void setRow(List row) {
			this.row = row;
		}

		

		public void setRowId(List rowId) {
			this.rowId = rowId;
		}
		
		
		public String getRow(int index) {
			if (row == null) {
				return ZERO_VALUE;
			}
			if(row.size() < theReport.getDetails().size()){
				for(int i = row.size();i < theReport.getDetails().size();i++){
					row.add(ZERO_VALUE);
				}
				
			}
			String v = (String) row.get(new Integer(index));
			if (v == null)
				return ZERO_VALUE;
			else
				return v;
		}

		public void setRow(int index, String value) {
			if (row == null) {
				row = new ArrayList();
			}
			row.add(new Integer(index), value);

		}
		
		public String getRowId(int index) {
			if (rowId == null) {
				return ID_99;
			}

			String v = (String) rowId.get(new Integer(index));
			if (v == null)
				return ID_99;
			else
				return v;
		}

		public void setRowId(int index, String value) {
			if (rowId == null) {
				rowId = new ArrayList();
			}
			rowId.add(new Integer(index), value);

		}
		/**
		 * @return Returns the row.
		 */
		public List getRowMap() {
			return row;
		}
		/**
		 * @param row The row to set.
		 */
		public void setRowMap(List row) {
			this.row = row;
		}
		/**
		 * @return Returns the rowId.
		 */
		public List getRowIdMap() {
			return rowId;
		}
		/**
		 * @param rowId The rowId to set.
		 */
		public void setRowIdMap(List rowId) {
			this.rowId = rowId;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#clone()
		 */
		protected Object clone() {
			RowVal newRowVal = new RowVal();
			
			// rowIds
			if (getRowIdMap() != null) {
				Iterator its = getRowIdMap().iterator();
				List rowIdMap = new ArrayList();
				while (its.hasNext()) {
					String it =  (String)its.next();
					rowIdMap.add(it);
				}
				newRowVal.setRowIdMap(rowIdMap);
			}

			// rows
			if (getRowMap() != null) {
				Iterator its = getRowMap().iterator();
				List rowMap = new ArrayList();
				while (its.hasNext()) {
					String it = (String) its.next();
					rowMap.add(it);
				}
				newRowVal.setRowMap(rowMap);
			}

			return newRowVal;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.util.CloneableForm#clear(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void clear( HttpServletRequest request) {
		this.clonedForm = null;
		//subNo = null;
		this.mode = null;
		this.lastPayroll = null;
		this.isAllowedView = false;
		this.isCashAccountPresent = false;
		this.isDisplayGenerateStatementSection = false;
		this.isDisplayPaymentInstructionSection = false;
		this.isDisplayTemporaryCreditSection = false;
		this.isDisplayBillPaymentSection = false;
//		this.isNoPermission = true;
//		this.theReport = new ContributionDetailsReportData(null, 0);
		this.participantTotal = null;
		this.allocationTotalValues = null;
		this.loanTotalValues = null;
		this.errorMap = null;
//		this.isViewMode = false;
		this.isShowLoans = true;
//		this.isShowConfirmDialog = false;
		this.isIgnoreDataCheckWarnings = false;
		this.statementDates = null;
		this.paymentInfo = null;
		this.payrollEffectiveDate = null;
//		this.requestEffectiveDate = null;
		this.calendarStartDate = null;
		this.calendarEndDate = null;
		this.payrollCalendarStartDate = null;
		this.payrollCalendarEndDate = null;
		this.marketClose = null;
		this.moneySourceID = null;
		this.defaultEffectiveDate = null;
		this.allowedPaymentDates = null;
		this.allowedPayrollDates = null;
		this.accounts = null;
		this.amounts = null;
		this.billAmounts = null;
		this.creditAmounts = null;
		this.contributionColumns = new ArrayList();
		this.employerIds = new HashMap();
		this.recordNumbers = new HashMap();
		this.loanColumns = new ArrayList();
		this.deleteBoxes = new ArrayList();
		this.moneyTypeColumns = new ArrayList();
		this.contractMoneyTypes = null;
		this.warningMessage = "";
		this.isHasChanged = false;
		this.resubmit = false;
		this.forwardFromSave = "";

	}

	public CloneableForm getClonedForm() {
		return clonedForm;
	}

	public Object clone() {
		EditContributionDetailsForm myClone = new EditContributionDetailsForm();
		myClone.setPayrollEffectiveDate(this.payrollEffectiveDate);
		myClone.setRequestEffectiveDate(this.requestEffectiveDate);
		myClone.setLastPayroll(this.lastPayroll);
		
		Iterator amounts = null;
		// payment allocation amounts
		if (getAmountsMap() != null) {
			amounts = getAmountsMap().iterator();
			List newAmounts = new ArrayList();
			while (amounts.hasNext()) {
				String amountEntry =  (String)amounts.next();
				newAmounts.add(amountEntry);
			}
			myClone.setAmountsMap(newAmounts);
		}
		
		// payment bill amounts
		if (getBillAmountsMap() != null) {
			amounts = getBillAmountsMap().iterator();
			List newBillAmounts = new ArrayList();
			while (amounts.hasNext()) {
				String amountEntry = (String) amounts.next();
				newBillAmounts.add(amountEntry);
			}
			myClone.setBillAmountsMap(newBillAmounts);
		}
		
		// payment credit amounts
		if (getCreditAmountsMap() != null) {
			amounts = getCreditAmountsMap().iterator();
			List newCreditAmounts = new ArrayList();
			while (amounts.hasNext()) {
				String amountEntry = (String) amounts.next();
				newCreditAmounts.add(amountEntry);
			}
			myClone.setCreditAmountsMap(newCreditAmounts);
		}
		
		// clone the contribution columns
		if (contributionColumns != null) {
			Iterator its = getContributionColumnsMap().iterator();
			List newContributionColumns = new ArrayList();
			while (its.hasNext()) {
				Object object = (Object) its.next();
				newContributionColumns.add(((RowVal) object).clone());
			}
			myClone.setContributionColumnsMap(newContributionColumns);
		}
		
		// clone the loan columns
		if (loanColumns != null) {
			Iterator its = getLoanColumnsMap().iterator();
			List newLoanColumns = new ArrayList ();
			while (its.hasNext()) {
				Object entry =  its.next();
				newLoanColumns.add(((RowVal) entry).clone());
			}
			myClone.setLoanColumnsMap(newLoanColumns);
		}
		
		// delete checkboxes
		if (getDeleteBoxesMap() != null) {
			Iterator its = getDeleteBoxesMap().iterator();
			List<Boolean> newDeleteBoxes = new ArrayList<Boolean>();
			while (its.hasNext()) {
				Boolean it = (Boolean) its.next();
				newDeleteBoxes.add(it);
			}
			myClone.setDeleteBoxesMap(newDeleteBoxes);
		}
		
		// money type header dropdowns
		if (getMoneyTypeColumnsMap() != null) {
			Iterator its = getMoneyTypeColumnsMap().iterator();
			List newMoneyTypeColumns = new ArrayList();
			while (its.hasNext()) {
				String it = (String) its.next();
				newMoneyTypeColumns.add(it);
			}
			myClone.setMoneyTypeColumnsMap(newMoneyTypeColumns);
		}
		
		// money source
		myClone.setMoneySourceID(getMoneySourceID());
		
		return myClone;
	}

	public void storeClonedForm() {
		clonedForm = (CloneableForm) clone();
	}
	/**
	 * @return Returns the isAllowedView.
	 */
	public boolean isAllowedView() {
		return isAllowedView;
	}
	/**
	 * @param isAllowedView The isAllowedView to set.
	 */
	public void setAllowedView(boolean isAllowedView) {
		this.isAllowedView = isAllowedView;
	}
	/**
	 * @return Returns the subNo.
	 */
	public String getSubNo() {
		return subNo;
	}
	/**
	 * @param subNo The subNo to set.
	 */
	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}
	/**
	 * @return Returns the participantTotal.
	 */
	public BigDecimal getParticipantTotal() {
		participantTotal = new BigDecimal(0d);
		if (theReport != null && theReport.getContributionData() != null) {
			Iterator participants = theReport.getDetails().iterator();
			while (participants.hasNext()) {
				SubmissionParticipant participant = (SubmissionParticipant) participants.next();
				participantTotal = participantTotal.add(participant.getParticipantTotal());
			}
		}
		return participantTotal;
	}
	/**
	 * @param participantTotal The participantTotal to set.
	 */
	public void setParticipantTotal(BigDecimal participantTotal) {
		this.participantTotal = participantTotal;
	}
	
	public void clearErrors() {
		errorMap = null;
	}
	
	public void addErrorCollection(Integer sourceRecordNo, Collection errors) {
		if (errorMap == null) {
			errorMap = new HashMap();
			errorMap.put(sourceRecordNo,errors);
		}
	}
	
	/**
	 * @return Returns the allocationMoneyValues.
	 */
	public Map getAllocationTotalValues() {
		allocationTotalValues = new HashMap();
		if (theReport != null && theReport.getContributionData() != null) {
			Iterator moneyTypes = theReport.getContributionData().getAllocationMoneyTypes().iterator();
			while (moneyTypes.hasNext()) {
				MoneyTypeHeader moneyType = (MoneyTypeHeader)moneyTypes.next();
				String key = moneyType.getKey();
				Iterator participants = theReport.getDetails().iterator();
				BigDecimal typeTotal = new BigDecimal(0d);
				while (participants.hasNext()) {
					SubmissionParticipant participant = (SubmissionParticipant) participants.next();
					BigDecimal amount = (BigDecimal)participant.getMoneyTypeAmounts().get(key);
					if (amount != null) typeTotal = typeTotal.add(amount);
				}
				allocationTotalValues.put(key,typeTotal);
			}
		}
		
		return allocationTotalValues;
	}
	/**
	 * @param allocationMoneyValues The allocationMoneyValues to set.
	 */
	public void setAllocationTotalValues(Map allocationTotalValues) {
		this.allocationTotalValues = allocationTotalValues;
	}
	/**
	 * @return Returns the loanTotalValues.
	 */
	public Collection getLoanTotalValues() {
		loanTotalValues = new ArrayList();
		if (
				theReport != null && 
				theReport.getContributionData() != null
		) {
			for (int i = 0; i < theReport.getContributionData().getMaximumNumberOfLoans(); i++) {
				Iterator participants = theReport.getDetails().iterator();
				BigDecimal loanTotal = new BigDecimal(0d);
				while (participants.hasNext()) {
					SubmissionParticipant participant = (SubmissionParticipant) participants.next();
					
					// get the loan values
					if (participant.getLoanAmounts() == null || participant.getLoanAmounts().values() == null) continue;
					BigDecimal [] loanAmounts = (BigDecimal []) participant.getLoanAmounts().values().toArray(new BigDecimal [] {new BigDecimal(0d)});
					BigDecimal amount = null;
					if (i < loanAmounts.length)
						amount = loanAmounts[i];
					else 
						amount = new BigDecimal(0d);
					
					if (amount != null)
						loanTotal = loanTotal.add(amount);
				}
				loanTotalValues.add(loanTotal);
			}
		}
		return loanTotalValues;
	}
	/**
	 * @param loanTotalValues The loanTotalValues to set.
	 */
	public void setLoanTotalValues(Collection loanTotalValues) {
		this.loanTotalValues = loanTotalValues;
	}
	/**
	 * @return Returns the isViewMode.
	 */
	public boolean isViewMode() {
		return isViewMode;
	}
	/**
	 * @param isViewMode The isViewMode to set.
	 */
	public void setViewMode(boolean isViewMode) {
		this.isViewMode = isViewMode;
	}
	/**
	 * @return Returns the isDisplayPaymentInstructionSection.
	 */
	public boolean isDisplayPaymentInstructionSection() {
		return isDisplayPaymentInstructionSection;
	}
	/**
	 * @param isDisplayPaymentInstructionSection The isDisplayPaymentInstructionSection to set.
	 */
	public void setDisplayPaymentInstructionSection(
			boolean isDisplayPaymentInstructionSection) {
		this.isDisplayPaymentInstructionSection = isDisplayPaymentInstructionSection;
	}
	/**
	 * @return Returns the theReport.
	 */
	public ContributionDetailsReportData getTheReport() {
		return theReport;
	}
	/**
	 * @param theReport The theReport to set.
	 */
	public void setTheReport(ContributionDetailsReportData theReport) {
		this.theReport = theReport;
	}
	/**
	 * @return Returns the statementDates.
	 */
	public Collection getStatementDates() {
		return statementDates;
	}
	/**
	 * @param statementDates The statementDates to set.
	 */
	public void setStatementDates(Collection statementDates) {
		this.statementDates = statementDates;
	}

	/**
	 * @return Returns the paymentDetails.
	 */
	public SubmissionUploadDetailBean getPaymentDetails() {
		
		SubmissionUploadDetailBean det = null;
		if (
				theReport != null && 
				theReport.getContributionData() != null &&
				theReport.getContributionData().getSubmissionPaymentItem() != null
		) {
			SubmissionPaymentItem paymentItem = theReport.getContributionData().getSubmissionPaymentItem();
			// show the payment section
			setDisplayPaymentInstructionSection(true);
			
			// use some existing DTOs for presentation of the payment details
			GFTUploadDetail result = new GFTUploadDetail();
			result.setContractName(paymentItem.getContractName());
			result.setContractNumber(String.valueOf(paymentItem.getContractId().toString()));
			result.setReceivedDate(paymentItem.getSubmissionDate());
			result.setRequestedPaymentEffectiveDate(paymentItem.getRequestedPaymentEffectiveDate());
			result.setSubmissionId(paymentItem.getSubmissionId().toString());
		
			
//			if (userProfile.isInternalUser()) {
//				result.setUserName(theReport.getContributionData().getSubmitterName() +	" (" + contributionItem.getSubmitterID() + ")");
//			} else {
//				result.setUserName(theReport.getContributionData().getSubmitterName());
//			}
			result.setUserName(theReport.getContributionData().getSubmitterName());
			result.setUserSSN(theReport.getContributionData().getSubmitterID());
		
			result.setPaymentInstructions(paymentItem.getPaymentInstructions());
			
			det = new SubmissionUploadDetailBean(result);
			det.setStatus(SubmissionHistoryItemActionHelper.getInstance().getDisplayStatus(theReport.getContributionData()));
		}
		return det;
	}
	

	public boolean isEditMode() {
		if ( EDIT_MODE_IND.equals(mode) ) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * @return Returns the mode.
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @param mode The mode to set.
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	/**
	 * @return Returns the paymentInfo.
	 */
	public ContractPaymentInfoVO getPaymentInfo() {
		return paymentInfo;
	}
	/**
	 * @param paymentInfo The paymentInfo to set.
	 */
	public void setPaymentInfo(ContractPaymentInfoVO paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	/**
	 * @return Returns the accounts.
	 */
	public Collection getAccounts() {
		return accounts;
	}
	/**
	 * @param accounts The accounts to set.
	 */
	public void setAccounts(Collection accounts) {
		this.accounts = accounts;
	}
	/**
	 * @return Returns the isCashAccountPresent.
	 */
	public boolean isCashAccountPresent() {
		return isCashAccountPresent;
	}
	/**
	 * @param isCashAccountPresent The isCashAccountPresent to set.
	 */
	public void setCashAccountPresent(boolean isCashAccountPresent) {
		this.isCashAccountPresent = isCashAccountPresent;
	}
	/**
	 * @return Returns the marketClose.
	 */
	public String getMarketClose() {
		return marketClose;
	}
	/**
	 * @param marketClose The marketClose to set.
	 */
	public void setMarketClose(String marketClose) {
		this.marketClose = marketClose;
	}
	/**
	 * @return Returns the calendarEndDate.
	 */
	public String getCalendarEndDate() {
		return calendarEndDate;
	}
	/**
	 * @param calendarEndDate The calendarEndDate to set.
	 */
	public void setCalendarEndDate(String calendarEndDate) {
		this.calendarEndDate = calendarEndDate;
	}
	/**
	 * @return Returns the calendarStartDate.
	 */
	public String getCalendarStartDate() {
		return calendarStartDate;
	}
	/**
	 * @param calendarStartDate The calendarStartDate to set.
	 */
	public void setCalendarStartDate(String calendarStartDate) {
		this.calendarStartDate = calendarStartDate;
	}
	/**
	 * @return Returns the defaultEffectiveDate.
	 */
	public Date getDefaultEffectiveDate() {
		return defaultEffectiveDate;
	}
	/**
	 * @param defaultEffectiveDate The defaultEffectiveDate to set.
	 */
	public void setDefaultEffectiveDate(Date defaultEffectiveDate) {
		this.defaultEffectiveDate = defaultEffectiveDate;
	}
	/**
	 * @return Returns the requestEffectiveDate.
	 */
	public String getRequestEffectiveDate() {
		return requestEffectiveDate;
	}
	/**
	 * @param requestEffectiveDate The requestEffectiveDate to set.
	 */
	public void setRequestEffectiveDate(String requestEffectiveDate) {
		this.requestEffectiveDate = requestEffectiveDate;
	}
	/**
	 * @return Returns the allowedMarketDates.
	 */
	public Date[] getAllowedPaymentDates() {
		return allowedPaymentDates;
	}
	/**
	 * @param allowedMarketDates The allowedMarketDates to set.
	 */
	public void setAllowedPaymentDates(Date[] allowedPaymentDates) {
		this.allowedPaymentDates = allowedPaymentDates;
	}
	/**
	 * @return Returns the isDisplayBillPaymentSection.
	 */
	public boolean isDisplayBillPaymentSection() {
		return isDisplayBillPaymentSection;
	}
	/**
	 * @param isDisplayBillPaymentSection The isDisplayBillPaymentSection to set.
	 */
	public void setDisplayBillPaymentSection(boolean isDisplayBillPaymentSection) {
		this.isDisplayBillPaymentSection = isDisplayBillPaymentSection;
	}
	/**
	 * @return Returns the isDisplayGenerateStatementSection.
	 */
	public boolean isDisplayGenerateStatementSection() {
		return isDisplayGenerateStatementSection;
	}
	/**
	 * @param isDisplayGenerateStatementSection The isDisplayGenerateStatementSection to set.
	 */
	public void setDisplayGenerateStatementSection(
			boolean isDisplayGenerateStatementSection) {
		this.isDisplayGenerateStatementSection = isDisplayGenerateStatementSection;
	}
	/**
	 * @return Returns the isDisplayTemporaryCreditSection.
	 */
	public boolean isDisplayTemporaryCreditSection() {
		return isDisplayTemporaryCreditSection;
	}
	/**
	 * @param isDisplayTemporaryCreditSection The isDisplayTemporaryCreditSection to set.
	 */
	public void setDisplayTemporaryCreditSection(
			boolean isDisplayTemporaryCreditSection) {
		this.isDisplayTemporaryCreditSection = isDisplayTemporaryCreditSection;
	}
 
	public boolean isDisplayMoreSections() {
		return isDisplayBillPaymentSection || isDisplayTemporaryCreditSection;
	}
	public String getAmounts(int index) {
		if (amounts == null) {
			return "0.00";
		}

		String v = (String) amounts.get(new Integer(index));
		if (v == null)
			return "0.00";
		else
			return v;
	}

	public void setAmounts(int index, String value) {
		if (this.amounts == null) {
			amounts = new ArrayList();
		}
		amounts.add(new Integer(index), value);

	}

	public String getBillAmounts(int index) {
		if (billAmounts == null) {
			return "0.00";
		}

		String v = (String) billAmounts.get(new Integer(index));
		if (v == null)
			return "0.00";
		else
			return v;
	}

	public void setBillAmounts(int index, String value) {
		if (this.billAmounts == null) {
			billAmounts = new ArrayList();
		}
		billAmounts.add(new Integer(index), value);

	}

	public String getCreditAmounts(int index) {
		if (creditAmounts == null) {
			return "0.00";
		}

		String v = (String) creditAmounts.get(new Integer(index));
		if (v == null)
			return "0.00";
		else
			return v;
	}

	public void setCreditAmounts(int index, String value) {
		if (this.creditAmounts == null) {
			creditAmounts = new ArrayList();
		}
		creditAmounts.add(new Integer(index), value);

	}
	/**
	 * @return Returns the amounts.
	 */
	public List getAmountsMap() {
		return amounts;
	}
	/**
	 * @param amounts The amounts to set.
	 */
	public void setAmountsMap(List amounts) {
		this.amounts = amounts;
	}
	/**
	 * @return Returns the billAmounts.
	 */
	public List getBillAmountsMap() {
		return billAmounts;
	}
	/**
	 * @param billAmounts The billAmounts to set.
	 */
	public void setBillAmountsMap(List billAmounts) {
		this.billAmounts = billAmounts;
	}
	/**
	 * @return Returns the creditAmounts.
	 */
	public List getCreditAmountsMap() {
		return creditAmounts;
	}
	/**
	 * @param creditAmounts The creditAmounts to set.
	 */
	public void setCreditAmountsMap(List creditAmounts) {
		this.creditAmounts = creditAmounts;
	}
	/**
	 * @return Returns the errorMap.
	 */
	public Map getErrorMap() {
		return errorMap;
	}
	/**
	 * @param errorMap The errorMap to set.
	 */
	public void setErrorMap(Map errorMap) {
		this.errorMap = errorMap;
	}
	/**
	 * @return Returns the error type.
	 */
	public boolean getErrorType(String field, int row, String severity) {
		if (
				theReport == null ||
				theReport.getContributionData() == null ||
				theReport.getContributionData().getReportDataErrors() == null
		)	return false;
		
		Iterator errorsIt = theReport.getContributionData().getReportDataErrors().getErrors().iterator();
		
		while (errorsIt.hasNext()) {
			SubmissionError error = (SubmissionError) errorsIt.next();
			if (error.getField().equals(field) && error.getRowNumber() == row) {
				if (SubmissionErrorHelper.isError(error) && ERROR_TYPE_ERROR.equals(severity))
					return true;
				else if (SubmissionErrorHelper.isWarning(error) && ERROR_TYPE_WARN.equals(severity))
					return true;
			}
		}
		return false;
	}

	/**
	 * @return Returns the lastPayroll.
	 */
	public String getLastPayroll() {
		return lastPayroll;
	}
	/**
	 * @param lastPayroll The lastPayroll to set.
	 */
	public void setLastPayroll(String lastPayroll) {
		this.lastPayroll = lastPayroll;
	}
	/**
	 * @return Returns the contributionColumns.
	 */
	public List getContributionColumnsMap() {
		return contributionColumns;
	}
	/**
	 * @param contributionColumns The contributionColumns to set.
	 */
	public void setContributionColumnsMap(List contributionColumns) {
		this.contributionColumns = contributionColumns;
	}
	/**
	 * @return Returns the loanColumns.
	 */
	public List getLoanColumnsMap() {
		return loanColumns;
	}
	/**
	 * @param loanColumns The loanColumns to set.
	 */
	public void setLoanColumnsMap(List loanColumns) {
		this.loanColumns = loanColumns;
	}
	public List getContributionColumns() {
		if(contributionColumns == null )
		{
			contributionColumns=new ArrayList<RowVal>();
			
		}
		if(contributionColumns.size() < theReport.getContributionData().getAllocationMoneyTypes().size()){
			for(int i = contributionColumns.size();i < theReport.getContributionData().getAllocationMoneyTypes().size();i++){
				contributionColumns.add(new RowVal());
			}
			
		}
		return contributionColumns;
	}

	public RowVal getContributionColumns(int index) {
		if(contributionColumns == null )
		{
			contributionColumns=new ArrayList<RowVal>();
			
		}
		if(contributionColumns.size() < theReport.getContributionData().getAllocationMoneyTypes().size()){
			for(int i = contributionColumns.size();i < theReport.getContributionData().getAllocationMoneyTypes().size();i++){
				contributionColumns.add(new RowVal());
			}
			
		}
		RowVal v = (RowVal) contributionColumns.get(new Integer(index));
		if (v == null) {
			contributionColumns.add(new Integer(index), new RowVal());
			v = (RowVal) contributionColumns.get(new Integer(index));
		}
		return v;
	}
    
    public void setContributionColumns(int index, RowVal val) {
        contributionColumns.add(new Integer(index), val);
    }
   
	public RowVal getLoanColumn(int index) {
		if(loanColumns == null )
		{
			loanColumns=new ArrayList<RowVal>();
			
		}
		if(loanColumns.size() < theReport.getContributionData().getMaximumNumberOfLoans()){
			for(int i = loanColumns.size();i < theReport.getContributionData().getMaximumNumberOfLoans();i++){
				loanColumns.add(new RowVal());
			}
			
		}
		RowVal v = (RowVal) loanColumns.get(new Integer(index));
		if (v == null) {
			loanColumns.add(new Integer(index), new RowVal());
			v = (RowVal) loanColumns.get(new Integer(index));
		}
		return v;
	}
       
	/**
	 * @return Returns the deleteBoxes.
	 */
	public List<Boolean> getDeleteBoxesMap() {
		return deleteBoxes;
	}
	/**
	 * @param deleteBoxes The deleteBoxes to set.
	 */
	public void setDeleteBoxesMap(List<Boolean> deleteBoxes) {
		this.deleteBoxes = deleteBoxes;
	}

	public Boolean getDeleteBoxes(int index) {
		if (deleteBoxes == null) {
			return false;
		}

		Boolean v = (Boolean) deleteBoxes.get(new Integer(index));
		if (v == null)
			return false;
		else
			return v;
	}

	public void setDeleteBoxes(int index, Boolean value) {
		if (this.deleteBoxes == null) {
			deleteBoxes = new ArrayList<Boolean>();
		}
		deleteBoxes.add(new Integer(index), value);

	}


	/**
	 * @return Returns the contractMoneyTypes.
	 */
	public Collection getContractMoneyTypes() {
		return contractMoneyTypes;
	}
	/**
	 * @param contractMoneyTypes The contractMoneyTypes to set.
	 */
	public void setContractMoneyTypes(Collection contractMoneyTypes) {
		this.contractMoneyTypes = contractMoneyTypes;
	}
	/**
	 * @return Returns the allowedPayrollDates.
	 */
	public Date[] getAllowedPayrollDates() {
		return allowedPayrollDates;
	}
	/**
	 * @param allowedPayrollDates The allowedPayrollDates to set.
	 */
	public void setAllowedPayrollDates(Date[] allowedPayrollDates) {
		this.allowedPayrollDates = allowedPayrollDates;
	}

	public String getAllowedPayrollDatesJavaScript() {
		//var validDates = [ new Date(2004,9,27), new Date(2004,9,29), new Date(2004,9,30)];

		if (allowedPayrollDates == null) return "";
		StringBuffer buffer = new StringBuffer(LEFT_BR);
		for (int i = 0; i < allowedPayrollDates.length; i++) {
			Date date = allowedPayrollDates[i];
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			buffer.append(NEW_DATE);
			buffer.append(String.valueOf(calendar.get(Calendar.YEAR))).append(COMMA);
			buffer.append(String.valueOf(calendar.get(Calendar.MONTH))).append(COMMA);
			buffer.append(String.valueOf(calendar.get(Calendar.DATE))).append(RIGHT_PAR);
			if (i != allowedPayrollDates.length -1 ) buffer.append(COMMA);
		}
		buffer.append(RIGHT_BR);
		return buffer.toString(); 
	}

	public String getAllowedPaymentDatesJavaScript() {
		//var validDates = [ new Date(2004,9,27), new Date(2004,9,29), new Date(2004,9,30)];
		
		if (allowedPaymentDates == null) return "";
		StringBuffer buffer = new StringBuffer(LEFT_BR);
		for (int i = 0; i < allowedPaymentDates.length; i++) {
			Date date = allowedPaymentDates[i];
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			buffer.append(NEW_DATE);
			buffer.append(String.valueOf(calendar.get(Calendar.YEAR))).append(COMMA);
			buffer.append(String.valueOf(calendar.get(Calendar.MONTH))).append(COMMA);
			buffer.append(String.valueOf(calendar.get(Calendar.DATE))).append(RIGHT_PAR);
			if (i != allowedPaymentDates.length -1 ) buffer.append(COMMA);
		}
		buffer.append(RIGHT_BR);
		return buffer.toString();
	}
	public String getPaymentContributionInputObjectsNamesForJavascript() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(NEW_ARRAY);

		if (accounts != null) {
			for (int i = 0; i < accounts.size(); i++) {
				buffer.append(i == 0 ? QUOTE : COMMA_QUOTE).append(UPLOADFORMOBJ)
						.append(ELEMENTS__AMOUNTS).append(String.valueOf(i))
						.append(END_BR);
			}
		}
		buffer.append(RIGHT_PAR);

		return buffer.toString();
	}

	public String getPaymentBillInputObjectsNamesForJavascript() {
		if(!isDisplayBillPaymentSection) return NULL;
		StringBuffer buffer = new StringBuffer();
		buffer.append(NEW_ARRAY);

		if (accounts != null) {
			for (int i = 0; i < accounts.size(); i++) {
				buffer.append(i == 0 ? QUOTE : COMMA_QUOTE).append(UPLOADFORMOBJ)
						.append(ELEMENTS__BILLAMOUNTS).append(String.valueOf(i))
						.append(END_BR);
			}
		}
		buffer.append(RIGHT_PAR);

		return buffer.toString();
	}

	public String getPaymentCreditInputObjectsNamesForJavascript() {
		if(!isDisplayTemporaryCreditSection) return NULL;
		StringBuffer buffer = new StringBuffer();
		buffer.append(NEW_ARRAY);

		if (accounts != null) {
			for (int i = 0; i < accounts.size(); i++) {
				buffer.append(i == 0 ? QUOTE : COMMA_QUOTE).append(UPLOADFORMOBJ)
						.append(ELEMENTS__CREDITAMOUNTS).append(String.valueOf(i))
						.append(END_BR);
			}
		}
		buffer.append(RIGHT_PAR);

		return buffer.toString();
	}
	
	public Collection getAccountsRowsObjectsNamesForJavascript() {
		Collection list = new ArrayList();
		
		if (accounts != null) {
			for (int i = 0; i < accounts.size(); i++) {
						StringBuffer buffer = new StringBuffer();
						buffer.append(NEW_ARRAY);
						buffer.append(QUOTE)
						.append(UPLOADFORMOBJ)
						.append(ELEMENTS__AMOUNTS).append(String.valueOf(i))
						.append(END_BR);
						
						if (isDisplayBillPaymentSection) {
							buffer.append(COMMA_QUOTE)
							.append(UPLOADFORMOBJ)
							.append(ELEMENTS__BILLAMOUNTS).append(String.valueOf(i))
							.append(END_BR);
						}
						if (isDisplayTemporaryCreditSection) {
							buffer.append(COMMA_QUOTE)
							.append(UPLOADFORMOBJ)
							.append(ELEMENTS__CREDITAMOUNTS).append(String.valueOf(i))
							.append(END_BR);
						}
						buffer.append(RIGHT_PAR);
						list.add(buffer.toString());
			}
		}

		return list;
	}
	
	public String getDefaultEffectiveDateJavascriptObject() {
		if(defaultEffectiveDate == null) return NULL;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(defaultEffectiveDate);
		StringBuffer buffer = new StringBuffer(NEW_DATE);
		buffer.append(String.valueOf(calendar.get(Calendar.YEAR))).append(COMMA);
		buffer.append(String.valueOf(calendar.get(Calendar.MONTH))).append(COMMA);
		buffer.append(String.valueOf(calendar.get(Calendar.DATE))).append(RIGHT_PAR);
		return buffer.toString();
	}
	
	public String getCurrentDateJavascriptObject() {
		Calendar calendar = AbstractSubmitController.adjustDate4pm(null);
		StringBuffer buffer = new StringBuffer(NEW_DATE);
		buffer.append(String.valueOf(calendar.get(Calendar.YEAR))).append(COMMA);
		buffer.append(String.valueOf(calendar.get(Calendar.MONTH))).append(COMMA);
		buffer.append(String.valueOf(calendar.get(Calendar.DATE))).append(RIGHT_PAR);
		return buffer.toString();
	}
	/**
	 * @return Returns the payrollCalendarEndDate.
	 */
	public String getPayrollCalendarEndDate() {
		return payrollCalendarEndDate;
	}
	/**
	 * @param payrollCalendarEndDate The payrollCalendarEndDate to set.
	 */
	public void setPayrollCalendarEndDate(String payrollCalendarEndDate) {
		this.payrollCalendarEndDate = payrollCalendarEndDate;
	}
	/**
	 * @return Returns the payrollCalendarStartDate.
	 */
	public String getPayrollCalendarStartDate() {
		return payrollCalendarStartDate;
	}
	/**
	 * @param payrollCalendarStartDate The payrollCalendarStartDate to set.
	 */
	public void setPayrollCalendarStartDate(String payrollCalendarStartDate) {
		this.payrollCalendarStartDate = payrollCalendarStartDate;
	}

	public Collection  getContributionColumnsInputObjectsNamesForJavascript() {

		Collection list	= new ArrayList();
		

		if (contributionColumns != null) {
			int colSize = contributionColumns.size();
			
			for (int col = 0; col < colSize; col ++) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(NEW_ARRAY);
				
				RowVal columnVal = (RowVal) contributionColumns.get(new Integer(col));
				
				int rowSize = columnVal.getRowMap().size();
				for (int row = 0; row < rowSize; row ++) {
					String rowVal = (String) columnVal.getRowMap().get(new Integer(row));
					// start creating each of the colun arrays
					buffer.append(row == 0 ? QUOTE : COMMA_QUOTE)
					.append(UPLOADFORMOBJ)
					.append(ELEMENTS__CONTRIBUTIONCOLUMNS)
					.append(col)
					.append(_ROW_)
					.append(row)
					.append(END_BR);
				}
				buffer.append(RIGHT_PAR);
				list.add(buffer.toString());
			}
		}
		return list;
	}

	public Collection  getLoanColumnsInputObjectsNamesForJavascript() {

		Collection list	= new ArrayList();
		

		if (loanColumns != null) {
			int colSize = loanColumns.size();
			
			for (int col = 0; col < colSize; col ++) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(NEW_ARRAY);
				
				RowVal columnVal = (RowVal) loanColumns.get(new Integer(col));
				
				int rowSize = columnVal.getRowMap().size();
				for (int row = 0; row < rowSize; row ++) {
					String rowVal = (String) columnVal.getRowMap().get(new Integer(row));
					// start creating each of the colun arrays
					buffer.append(row == 0 ? QUOTE : COMMA_QUOTE)
					.append(UPLOADFORMOBJ)
					.append(ELEMENTS__LOANCOLUMNS)
					.append(col)
					.append(_ROW_)
					.append(row)
					.append(END_BR);
				}
				buffer.append(RIGHT_PAR);
				list.add(buffer.toString());
			}
		}
		return list;
	}

	public String getMoneyFieldsPageTotalsForJavascript() {
		StringBuffer buffer = new StringBuffer();

		if (contributionColumns != null) {
			int colSize = contributionColumns.size();
			buffer.append(LEFT_BR);
			
			for (int col = 0; col < colSize; col ++) {
				RowVal columnVal = (RowVal) contributionColumns.get(new Integer(col));
				int rowSize = columnVal.getRowMap().size();
				double rowTotalValue = 0d;
				for (int row = 0; row < rowSize; row ++) {
					String rowValueString = (String) columnVal.getRowMap().get(new Integer(row));
					double rowValue = 0d;
					try {
						rowValue = rowValue + NumberFormat.getNumberInstance().parse(rowValueString).doubleValue();
					} catch (ParseException e) {
						//this shouldn't really happen here as we validate before, but...
						SystemException se = new SystemException(e, EditContributionDetailsForm.class.getName(), "getMoneyFieldsPageTotalsForJavascript", e.getMessage());
						LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
					}
					rowTotalValue += rowValue;
				}
				
				// start creating each of the colun arrays
				buffer.append(col == 0 ? "" : COMMA).append(rowTotalValue);
			}
			buffer.append(RIGHT_BR);
		}
		return buffer.toString();
	}

	public String getLoanFieldsPageTotalsForJavascript() {

		Collection list	= new ArrayList();
		StringBuffer buffer = new StringBuffer();

		if (loanColumns != null) {
			int colSize = loanColumns.size();
			
			buffer.append(LEFT_BR);
			
			for (int col = 0; col < colSize; col ++) {
				RowVal columnVal = (RowVal) loanColumns.get(new Integer(col));
				
				int rowSize = columnVal.getRowMap().size();
				double rowTotalValue = 0d;
				for (int row = 0; row < rowSize; row ++) {
					String rowValueString = (String) columnVal.getRowMap().get(new Integer(row));
					double rowValue = 0d;
					try {
						rowValue = rowValue + NumberFormat.getNumberInstance().parse(rowValueString).doubleValue();
					} catch (ParseException e) {
						//this shouldn't really happen here as we validate before, but...
						SystemException se = new SystemException(e, EditContributionDetailsForm.class.getName(), "getLoanFieldsPageTotalsForJavascript", e.getMessage());
						LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
					}
					rowTotalValue += rowValue;
				}
				
				// start creating each of the colun arrays
				buffer.append(col == 0 ? "" : COMMA).append(rowTotalValue);
			}
			buffer.append(RIGHT_BR);
		}
		return buffer.toString();
	}

	public String getMoneyFieldsColumnTotalsForJavascript() {

		Collection list	= new ArrayList();
		StringBuffer buffer = new StringBuffer();

		if (theReport != null && theReport.getContributionData() != null) {
			
			List allocationMoneyTypes = theReport.getContributionData().getAllocationMoneyTypes();
			Map allocationTotalValues = theReport.getContributionData().getAllocationTotalValues();
			
			int colSize = allocationMoneyTypes.size();
			
			buffer.append(LEFT_BR);
			
			Iterator types = allocationMoneyTypes.iterator();
			int col = 0;
			while (types.hasNext()) {
				MoneyTypeHeader moneyType = (MoneyTypeHeader) types.next();
				BigDecimal typeTotal = (BigDecimal) allocationTotalValues.get(moneyType.getKey());

				// start creating each of the colun arrays
				buffer.append(col == 0 ? "" : COMMA);
				if (typeTotal != null) {
					buffer.append(typeTotal.toString());
				} else {
					buffer.append(DECIMAL_ZERO);
				}
				col++;
			}
			buffer.append(RIGHT_BR);
		}
		return buffer.toString();
	}

	public String getLoanFieldsColumnTotalsForJavascript() {

		Collection list	= new ArrayList();
		StringBuffer buffer = new StringBuffer();

		if (theReport != null && theReport.getContributionData() != null) {
			
			Collection loanTotalValues = theReport.getContributionData().getLoanTotalValues();
			
			int colSize = loanTotalValues.size();
			
			buffer.append(LEFT_BR);
			
			Iterator loanTotals = loanTotalValues.iterator();
			int col = 0;
			while (loanTotals.hasNext()) {
				BigDecimal loanTotal = (BigDecimal) loanTotals.next();

				// start creating each of the column arrays
				buffer.append(col == 0 ? "" : COMMA);
				if (loanTotal != null) {
					buffer.append(loanTotal.toString());
				} else {
					buffer.append(DECIMAL_ZERO);
				}
				col++;
			}
			buffer.append(RIGHT_BR);
		}
		return buffer.toString();
	}

	
	public Collection  getParticipantFieldsObjectsNamesForJavascript() {

		Collection list	= new ArrayList();
		
		int numberOfRows = 0;
		
		if (theReport != null && theReport.getContributionData() != null) {
			numberOfRows = theReport.getDetails().size();

			for (int row = 0; row < numberOfRows; row++) {

				StringBuffer buffer = new StringBuffer();
				buffer.append(NEW_ARRAY);
				boolean colStarted = false;
				if (contributionColumns != null) {
					int colSize = contributionColumns.size();
					
					for (int col = 0; col < colSize; col ++) {
						// start creating each of the colun arrays
						colStarted = true;
						buffer.append(col == 0 ? QUOTE : COMMA_QUOTE)
						.append(UPLOADFORMOBJ)
						.append(ELEMENTS__CONTRIBUTIONCOLUMNS)
						.append(col)
						.append(_ROW_)
						.append(row)
						.append(END_BR);
					}
				}

				if (loanColumns != null) {
					int colSize = loanColumns.size();
					
					for (int col = 0; col < colSize; col ++) {
						// start creating each of the colun arrays
						buffer.append(col == 0 && !colStarted ? QUOTE : COMMA_QUOTE)
						.append(UPLOADFORMOBJ)
						.append(ELEMENTS__LOANCOLUMNS)
						.append(col)
						.append(_ROW_)
						.append(row)
						.append(END_BR);
					}
				}

				buffer.append(RIGHT_PAR);
				list.add(buffer.toString());
			}
		}
		return list;
	}

	/**
	 * @return Returns the payrollEffectiveDate.
	 */
	public String getPayrollEffectiveDate() {
		return payrollEffectiveDate;
	}
	/**
	 * @param payrollEffectiveDate The payrollEffectiveDate to set.
	 */
	public void setPayrollEffectiveDate(String payrollEffectiveDate) {
		this.payrollEffectiveDate = payrollEffectiveDate;
	}
	/**
	 * @return Returns the moneyTypeColumns.
	 */
	public List getMoneyTypeColumnsMap() {
		return moneyTypeColumns;
	}
	/**
	 * @param moneyTypeColumns The moneyTypeColumns to set.
	 */
	public void setMoneyTypeColumnsMap(List moneyTypeColumns) {
		this.moneyTypeColumns = moneyTypeColumns;
	}

	public String getMoneyTypeColumns(int index) {
		if (moneyTypeColumns == null) {
			return DEFAULT_MONEY_DD_ID;
		}

		String v = (String) moneyTypeColumns.get(new Integer(index));
		if (v == null)
			return DEFAULT_MONEY_DD_ID;
		else
			return v;
	}

	public void setMoneyTypeColumns(int index, String value) {
		if (this.moneyTypeColumns == null) {
			moneyTypeColumns = new ArrayList();
		}
		moneyTypeColumns.add( value);

	}

	/**
	 * @return Returns the contractMoneySources.
	 */
	public Collection getContractMoneySources() {
		return contractMoneySources;
	}
	/**
	 * @param contractMoneySources The contractMoneySources to set.
	 */
	public void setContractMoneySources(Collection contractMoneySources) {
		this.contractMoneySources = contractMoneySources;
	}
	/**
	 * @return Returns the moneySourceID.
	 */
	public String getMoneySourceID() {
		return moneySourceID;
	}
	/**
	 * @param moneySourceID The moneySourceID to set.
	 */
	public void setMoneySourceID(String moneySourceID) {
		this.moneySourceID = moneySourceID;
	}
	/**
	 * @return Returns the employerIds.
	 */
	public Map getEmployerIdsMap() {
		return employerIds;
	}
	/**
	 * @param employerIds The employerIds to set.
	 */
	public void setEmployerIdsMap(Map employerIds) {
		this.employerIds = employerIds;
	}

	public String getEmployerIds(int index) {
		if (employerIds == null) {
			return "";
		}

		String v = (String) employerIds.get(new Integer(index));
		if (v == null)
			return "";
		else
			return v;
	}

	public void setEmployerIds(int index, String value) {
		if (this.employerIds == null) {
			employerIds = new HashMap();
		}
		employerIds.put(new Integer(index), value);

	}
	/**
	 * @return Returns the recordNumbers.
	 */
	public Map getRecordNumbersMap() {
		return recordNumbers;
	}
	/**
	 * @param employerIds The recordNumbers to set.
	 */
	public void setRecordNumbersMap(Map recordNumbers) {
		this.recordNumbers = recordNumbers;
	}

	public Integer getRecordNumbers(int index) {
		if (recordNumbers == null) {
			return new Integer(0);
		}

		Integer v = (Integer) recordNumbers.get(new Integer(index));
		if (v == null)
			return new Integer(0);
		else
			return v;
	}

	public void setRecordNumbers(int index, Integer value) {
		if (this.recordNumbers == null) {
			recordNumbers = new HashMap();
		}
		recordNumbers.put(new Integer(index), value);

	}
	
	public boolean isSaveAction(String task) {
		return SAVE_TASK.equals(task);
	}
	
	public boolean isSubmitAction(String task) {
		return SUBMIT_TASK.equals(task);
	}
	/**
	 * @return Returns the isShowLoans.
	 */
	public boolean isShowLoans() {
		return isShowLoans;
	}
	/**
	 * @param isShowLoans The isShowLoans to set.
	 */
	public void setShowLoans(boolean isShowLoans) {
		this.isShowLoans = isShowLoans;
	}
	/**
	 * @return Returns the isShowConfirmDialog.
	 */
	public boolean isShowConfirmDialog() {
		return isShowConfirmDialog;
	}
	/**
	 * @param isShowConfirmDialog The isShowConfirmDialog to set.
	 */
	public void setShowConfirmDialog(boolean isShowConfirmDialog) {
		this.isShowConfirmDialog = isShowConfirmDialog;
	}
	/**
	 * @return Returns the isIgnoreDataCheckWarnings.
	 */
	public boolean isIgnoreDataCheckWarnings() {
		return isIgnoreDataCheckWarnings;
	}
	/**
	 * @param isIgnoreDataCheckWarnings The isIgnoreDataCheckWarnings to set.
	 */
	public void setIgnoreDataCheckWarnings(boolean isIgnoreDataCheckWarnings) {
		this.isIgnoreDataCheckWarnings = isIgnoreDataCheckWarnings;
	}
	
	public String getDialogWarningMessages() {
			
		if ( 
				theReport == null || 
				theReport.getContributionData() == null || 
				theReport.getContributionData().getReportDataErrors() == null ||
				theReport.getContributionData().getReportDataErrors().getErrors() == null ) {
			return "";
		}

		ReportDataErrors errors = theReport.getContributionData().getReportDataErrors();
		
		if ( errors.getErrors().size() == 0 ) {
			// no data check errors/warnings, return
			return "";
		}
		
		StringBuffer buff = new StringBuffer("");
		
		// loop through the collection of errors
		Iterator iter = errors.getErrors().iterator();
		while (iter.hasNext()) {
			buff.append("\\n");
			SubmissionError error = (SubmissionError)iter.next();
						
			// if it's a participant-level error, display the row number
			buff.append(" ");
			if (error.isParticipantLevel()) {
				buff.append("Row # ").append(error.getRowNumber());
			} else {
				// else display the field name
				buff.append(error.getFieldDisplayLabel());
			}
			buff.append(" ");
						
			// display the content
			try {
				Message msg = (Message)ContentCacheManager.getInstance().getContentById(error.getContentId(), ContentTypeManager.instance().MESSAGE);
				buff.append(StringEscapeUtils.escapeJavaScript(msg.getText()));
			} catch (ContentException ce) {
				buff.append("unknown error/warning");
			}
		}
		return buff.toString();
	}

	/**
	 * @return Returns the isNoPermission.
	 */
	public boolean isNoPermission() {
		return isNoPermission;
	}
	/**
	 * @param isNoPermission The isNoPermission to set.
	 */
	public void setNoPermission(boolean isNoPermission) {
		this.isNoPermission = isNoPermission;
	}
	/**
	 * @return Returns the warningMessage.
	 */
	public String getWarningMessage() {
		return warningMessage;
	}
	/**
	 * @param warningMessage The warningMessage to set.
	 */
	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.tools.SubmissionPaymentForm#getPaymentTableHeight()
	 */
	public int getPaymentTableHeight() {
		// calculate the payment table height from the number of accounts
		int height = 0;
		boolean hasDebit = false;
		if (accounts != null && accounts.size() != 0) {
			height = 0;
			Iterator accountsIt = accounts.iterator();
			while (accountsIt.hasNext()) {
				PaymentAccountBean currentAccount = (PaymentAccountBean) accountsIt.next();
				if (currentAccount.getType().equals(CASH_ACCOUNT_TYPE))
					height += CASH_HEIGHT;
				else {
					height += DEBIT_HEIGHT;
					hasDebit = true;
				}
			}
		}
		// if the user doesn't have direct debit accounts or permission
		// we need to add at least one row's worth of height
		if (!hasDebit)
			height += DEBIT_HEIGHT;
		if (height < MIN_PAYMENT_TABLE_HEIGHT)
			height = MIN_PAYMENT_TABLE_HEIGHT;
		if (height > MAX_PAYMENT_TABLE_HEIGHT)
			height = MAX_PAYMENT_TABLE_HEIGHT;
		return height;
	}
	/**
	 * @return Returns the isHasChanged.
	 */
	public boolean isHasChanged() {
		return isHasChanged;
	}
	/**
	 * @param isHasChanged The isHasChanged to set.
	 */
	public void setHasChanged(boolean isHasChanged) {
		this.isHasChanged = isHasChanged;
	}
	public int getMyOwnPageNumber() {
		return myOwnPageNumber;
	}
	public void setMyOwnPageNumber(int myOwnPageNumber) {
		this.myOwnPageNumber = myOwnPageNumber;
	}
	public boolean isResubmit() {
		return this.resubmit;
	}
	public void setResubmit(boolean resubmit) {
		this.resubmit = resubmit;
	}
	public String getForwardFromSave() {
		return this.forwardFromSave;
	}
	public void setForwardFromSave(String forwardFromSave) {
		this.forwardFromSave = forwardFromSave;
	}

	public List getAmounts() {

		if(amounts == null){
			amounts=new ArrayList();
		}
		if(amounts.size()<accounts.size()){
			for(int i=amounts.size();i<=accounts.size();i++){
				amounts.add(i,"0.00");
			}
		}
		return amounts;
	
	}

	public void setAmounts(List amounts) {
		this.amounts = amounts;
	}

	public List getBillAmounts() {

		if(billAmounts == null){
			billAmounts=new ArrayList();
		}
		if(billAmounts.size()<accounts.size()){
			for(int i=billAmounts.size();i<=accounts.size();i++){
				billAmounts.add(i,"0.00");
			}
		}
		return billAmounts;
	
	}

	public void setBillAmounts(List billAmounts) {
		this.billAmounts = billAmounts;
	}

	public List getCreditAmounts() {

		if(creditAmounts == null){
			creditAmounts=new ArrayList();
		}
		if(creditAmounts.size()<accounts.size()){
			for(int i=creditAmounts.size();i<=accounts.size();i++){
				creditAmounts.add(i,"0.00");
			}
		}
		return creditAmounts;
	
	}

	public void setCreditAmounts(List creditAmounts) {
		this.creditAmounts = creditAmounts;
	}
}


