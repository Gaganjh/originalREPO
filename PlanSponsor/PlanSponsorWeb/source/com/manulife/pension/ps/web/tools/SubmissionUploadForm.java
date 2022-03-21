package com.manulife.pension.ps.web.tools;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.web.multipart.MultipartFile;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData;
import com.manulife.pension.service.contract.valueobject.ContractPaymentInfoVO;

public class SubmissionUploadForm extends AutoForm implements SubmissionPaymentForm {

	private static final String SEND_ACTION = "send";

	public static final String PAYMENT_INSTRUCTION_ACCOUNT_PREFIX = "pmtaccount_";

	private String actionLabel;

	private boolean isDisplayFileUploadSection = false;

	private boolean isCashAccountPresent = false;

	private boolean isDisplayGenerateStatementSection = false;

	private boolean isDisplayPaymentInstructionSection = false;

	private boolean isDisplayTemporaryCreditSection = false;

	private boolean isDisplayBillPaymentSection = false;
	
	private boolean isViewMode = false;
	
	private Collection accounts;
	
	private Collection statementEndDates;
	
	private Collection statementDates = new ArrayList();
	
	private List amounts;

	private List billAmounts;
	
	private List creditAmounts;
	
	private String requestEffectiveDate;
	
	private String calendarStartDate;
	
	private String calendarEndDate;
	
	private ContractPaymentInfoVO paymentInfo;
	
	private String marketClose;
	
	private Date defaultEffectiveDate;
	
	private Date [] allowedMarketDates;
	
    /**
     * E-mail used for notification
     */
    private String email;

  	
	/**
	 * To get contribution total amount.
	 */
	public  BigDecimal contributionTotal;
	
	
	/**
	 * file type: Regular, Misc., etc.
	 */
	private String fileType;

	/**
	 * last payroll: y/n
	 */
	private String lastPayroll;

	/**
	 * The file that the user has uploaded
	 */
	private transient MultipartFile uploadFile;

	private static final Map ACTION_LABEL_MAP = new HashMap();

	/*
	 * Maps the button label to the corresponding action.
	 */
	static {
		ACTION_LABEL_MAP.put("send", "send");
	}

	/**
	 * Retrieve a representation of the file the user has uploaded
	 */
	public MultipartFile getUploadFile() {
		return uploadFile;
	}

	/**
	 * Set a representation of the file the user has uploaded
	 */
	public void setUploadFile(MultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public boolean isSendAction() {
		return SEND_ACTION.equals(getAction());
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String string) {
		fileType = string;
	}

	public String getLastPayroll() {
		return lastPayroll;
	}

	public void setLastPayroll(String string) {
		lastPayroll = string;
	}

	public boolean isDisplayFileUploadSection() {
		return isDisplayFileUploadSection;
	}

	public boolean isDisplayPaymentInstructionSection() {
		return isDisplayPaymentInstructionSection;
	}

	public void setDisplayFileUploadSection(boolean b) {
		isDisplayFileUploadSection = b;
	}

	public void setDisplayPaymentInstructionSection(boolean b) {
		isDisplayPaymentInstructionSection = b;
	}

	public Collection getAccounts() {
		return accounts;
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

	public void setAccounts(Collection collection) {
		accounts = collection;
	}

	public boolean isCashAccountPresent() {
		return isCashAccountPresent;
	}

	public void setCashAccountPresent(boolean b) {
		isCashAccountPresent = b;
	}

	public String getPaymentContributionInputObjectsNamesForJavascript() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("new Array(");

		if (accounts != null) {
			for (int i = 0; i < accounts.size(); i++) {
				buffer.append(i == 0 ? "\"" : ", \"").append("uploadFormObj.")
						.append("elements['amounts[").append(String.valueOf(i))
						.append("]']\"");
			}
		}
		buffer.append(")");

		return buffer.toString();
	}

	public String getPaymentBillInputObjectsNamesForJavascript() {
		if(!isDisplayBillPaymentSection) return "null";
		StringBuffer buffer = new StringBuffer();
		buffer.append("new Array(");

		if (accounts != null) {
			for (int i = 0; i < accounts.size(); i++) {
				buffer.append(i == 0 ? "\"" : ", \"").append("uploadFormObj.")
						.append("elements['billAmounts[").append(String.valueOf(i))
						.append("]']\"");
			}
		}
		buffer.append(")");

		return buffer.toString();
	}

	public String getPaymentCreditInputObjectsNamesForJavascript() {
		if(!isDisplayTemporaryCreditSection) return "null";
		StringBuffer buffer = new StringBuffer();
		buffer.append("new Array(");

		if (accounts != null) {
			for (int i = 0; i < accounts.size(); i++) {
				buffer.append(i == 0 ? "\"" : ", \"").append("uploadFormObj.")
						.append("elements['creditAmounts[").append(String.valueOf(i))
						.append("]']\"");
			}
		}
		buffer.append(")");

		return buffer.toString();
	}

	
	public Collection getAccountsRowsObjectsNamesForJavascript() {
		Collection list = new ArrayList();
		
		if (accounts != null) {
			for (int i = 0; i < accounts.size(); i++) {
						StringBuffer buffer = new StringBuffer();
						buffer.append("new Array(");
						buffer.append("\"")
						.append("uploadFormObj.")
						.append("elements['amounts[").append(String.valueOf(i))
						.append("]']\"");
						
						if (isDisplayBillPaymentSection) {
							buffer.append(", \"")
							.append("uploadFormObj.")
							.append("elements['billAmounts[").append(String.valueOf(i))
							.append("]']\"");
						}
						if (isDisplayTemporaryCreditSection) {
							buffer.append(", \"")
							.append("uploadFormObj.")
							.append("elements['creditAmounts[").append(String.valueOf(i))
							.append("]']\"");
						}
						buffer.append(")");
						list.add(buffer.toString());
			}
		}

		return list;
	}
	
	
	
	
	
	public String getDefaultEffectiveDateJavascriptObject() {
		if(defaultEffectiveDate == null) return "null";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(defaultEffectiveDate);
		StringBuffer buffer = new StringBuffer("new Date(");
		buffer.append(String.valueOf(calendar.get(Calendar.YEAR))).append(",");
		buffer.append(String.valueOf(calendar.get(Calendar.MONTH))).append(",");
		buffer.append(String.valueOf(calendar.get(Calendar.DATE))).append(")");
		return buffer.toString();
	}
	
	public String getCurrentDateJavascriptObject() {
		Calendar calendar = AbstractSubmitController.adjustDate4pm(null);
		StringBuffer buffer = new StringBuffer("new Date(");
		buffer.append(String.valueOf(calendar.get(Calendar.YEAR))).append(",");
		buffer.append(String.valueOf(calendar.get(Calendar.MONTH))).append(",");
		buffer.append(String.valueOf(calendar.get(Calendar.DATE))).append(")");
		return buffer.toString();
	}

	public void reset( HttpServletRequest request) {
		super.reset( request);
		actionLabel = null;
	}

	public void clear( HttpServletRequest request) {
		reset( request);
		isCashAccountPresent = false;
		isDisplayFileUploadSection = false;
		isDisplayPaymentInstructionSection = false;
		fileType = null;
		lastPayroll = null;
		accounts = null;
		amounts = null;
		billAmounts = null;
		creditAmounts = null;
	}

	public String getActionLabel() {
		return actionLabel;
	}

	public void setActionLabel(String actionLabel) {
		this.actionLabel = trimString(actionLabel);
		setAction((String) ACTION_LABEL_MAP.get(actionLabel));
	}

	public String getAction() {
		if (super.getAction().length() == 0 && actionLabel != null
				&& actionLabel.length() > 0) {
			setAction((String) ACTION_LABEL_MAP.get(actionLabel));
		}
		return super.getAction();
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
	 * @return Returns the isDisplayBillPaymentSection.
	 */
	public boolean isDisplayBillPaymentSection() {
		return isDisplayBillPaymentSection;
	}
	/**
	 * @return Returns the isDisplayBillPaymentSection.
	 */
	public boolean isDisplayMoreSections() {
		return isDisplayBillPaymentSection || isDisplayTemporaryCreditSection;
	}
	/**
	 * @param isDisplayBillPaymentSection The isDisplayBillPaymentSection to set.
	 */
	public void setDisplayBillPaymentSection(boolean isDisplayBillPaymentSection) {
		this.isDisplayBillPaymentSection = isDisplayBillPaymentSection;
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
	/**
	 * @return Returns the statementEndDates.
	 */
	public Collection getStatementEndDates() {
		return statementEndDates;
	}
	/**
	 * @param statementEndDates The statementEndDates to set.
	 */
	public void setStatementEndDates(ArrayList statementEndDates) {
		this.statementEndDates = statementEndDates;
	}

	public Date getEndDate(int i) {
		if (statementEndDates !=null ) 
			return (Date) ((ArrayList)statementEndDates).get(i);
		else 
			return null;
	}

	public Date getStartDate(int i) {
		if (statementDates !=null ) 
			return (Date) ((ArrayList)statementDates).get(i);
		else 
			return null;
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
	 * @return Returns the allowedMarketDates.
	 */
	public Date[] getAllowedMarketDates() {
		return allowedMarketDates;
	}
	
	public String getAllowedMarketDatesJavaScript() {
		//var validDates = [ new Date(2004,9,27), new Date(2004,9,29), new Date(2004,9,30)];
		
		if (allowedMarketDates == null) return "";
		StringBuffer buffer = new StringBuffer("[ ");
		for (int i = 0; i < allowedMarketDates.length; i++) {
			Date date = allowedMarketDates[i];
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			buffer.append("new Date(");
			buffer.append(String.valueOf(calendar.get(Calendar.YEAR))).append(",");
			buffer.append(String.valueOf(calendar.get(Calendar.MONTH))).append(",");
			buffer.append(String.valueOf(calendar.get(Calendar.DATE))).append(")");
			if (i != allowedMarketDates.length -1 ) buffer.append(", ");
		}
		buffer.append(" ]");
		return buffer.toString();
	}
	/**
	 * @param allowedMarketDates The allowedMarketDates to set.
	 */
	public void setAllowedMarketDates(Date[] allowedMarketDates) {
		this.allowedMarketDates = allowedMarketDates;
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
	
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.tools.SubmissionPaymentForm#getTheReport()
	 */
	public ContributionDetailsReportData getTheReport() {
		// TODO Auto-generated method stub
		return null;
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
	 * To get contribution total amount.
	 * @return
	 */
	public BigDecimal getContributionTotal() {
		return contributionTotal;
	}

	public void setContributionTotal(BigDecimal contributionTotal) {
		this.contributionTotal = contributionTotal;
	}
	
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

