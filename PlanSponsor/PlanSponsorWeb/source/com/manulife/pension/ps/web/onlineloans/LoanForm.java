package com.manulife.pension.ps.web.onlineloans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;


import com.manulife.pension.content.view.ContentText;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.LoanDocumentServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.util.CloneableAutoActionLabelForm;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.distribution.valueobject.AtRiskDetailsInputVO;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.DistributionAddress;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;
import com.manulife.pension.service.distribution.valueobject.Note;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.distribution.valueobject.RequestType;
import com.manulife.pension.service.loan.LoanError;
import com.manulife.pension.service.loan.LoanErrorCode;
import com.manulife.pension.service.loan.LoanField;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.LoanWarning;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.loan.util.LoanContentConstants;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanAddress;
import com.manulife.pension.service.loan.valueobject.LoanDeclaration;
import com.manulife.pension.service.loan.valueobject.LoanFee;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanNote;
import com.manulife.pension.service.loan.valueobject.LoanParameter;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPayee;
import com.manulife.pension.service.loan.valueobject.LoanPaymentInstruction;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanRecipient;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.util.CalendarUtils;
import com.manulife.pension.util.JdbcHelper;
import com.manulife.util.converter.ConverterHelper;
import com.manulife.util.converter.DateConverter;

/**
 * LoanForm is the action form for the loan entry
 */
public class LoanForm extends CloneableAutoActionLabelForm {

	private static final Pattern webMultiLineAllowedCharacterRegEx = Pattern
			.compile("[\n\r\\x20-\\x7e]");

	private static final Pattern numericCharacterRegEx = Pattern
			.compile("[0-9.,]");

	private static final Pattern numericCharacterNoCommaRegEx = Pattern
			.compile("[0-9.]");

	private static final Pattern dateFormatRegEx = Pattern
			.compile("^[0-9]+[/][0-9]+[/][0-9]+$");

	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Object> lookupData = new HashMap<String, Object>();

	private Integer contractId;

	private Integer submissionId;

	private Integer participantProfileId;

	private String legallyMarried;

	private Integer participantId;

	private String status;

	private String firstName;

	private String lastName;

	private String middleInitial;

	private String employmentStatusCode;

	private String ssn;

	private String contractName;

	private String loanType;

	private String loanReason;

	private String requestDate;

	private String expirationDate;

	private String startDate;

	private String payrollDate;

	private String tpaLoanFee;

	private String defaultProvision;

	private String maturityDate;

	private Integer maximumAmortizationPeriodYears;

	private String maximumLoanAvailable;
	
	private boolean applyIrs10KDollarRule;

	private String loanAmount;

	private String loanAmountOriginalValue;

	private String paymentAmount;

	private String paymentFrequency;

	private Integer amortizationPeriodYears;

	private Integer amortizationPeriodMonths;

	private String interestRate;

	private Map<String, String> moneyTypeVestingPercentages = new HashMap<String, String>();

	private Map<String, Boolean> moneyTypeExcludedInds = new HashMap<String, Boolean>();

	private String maxBalanceLast12Months;

	private String outstandingLoansCount;

	private String currentOutstandingBalance;

	private String currentParticipantNote;

	private String currentAdministratorNote;

	private String addressLine1;

	private String addressLine2;

	private String city;

	private AddressStateCode stateCode = new AddressStateCode();

	private String country;

	private ZipCode zipCode = new ZipCode();

	private String abaRoutingNumber;

	private String bankName;

	private String accountType;

	private String accountNumber;

	private String paymentMethod;

	private Timestamp lastUpdatedTs;

	private boolean showConfirmation = false;

	private String createdByRoleCode;

	private Map<String, Integer> maximumAmortizationYearsMap;

	private NotificationAndDeclaration truthInLendingNotice = new NotificationAndDeclaration();
	private boolean truthInLendingNoticeAcceptedOriginalValue = false;

	private NotificationAndDeclaration promissoryNote = new NotificationAndDeclaration();
	private boolean promissoryNoteAcceptedOriginalValue = false;

	private NotificationAndDeclaration amortizationSchedule = new NotificationAndDeclaration();

	private NotificationAndDeclaration participantDeclaration = new NotificationAndDeclaration();
	private boolean participantDeclarationAcceptedOriginalValue = false;

	private NotificationAndDeclaration atRiskTransaction = new NotificationAndDeclaration();
	private boolean atRiskTransactionAcceptedOriginalValue = false;
	private boolean atRiskTransactionInd = false;

	private boolean displayIRSlabel = false;
	
	private NotificationAndDeclaration loanPackageInstructionsForParticipant = new NotificationAndDeclaration();

	private NotificationAndDeclaration loanPackageInstructionsForPlanAdministrator = new NotificationAndDeclaration();

	private NotificationAndDeclaration loanFormInstructions = new NotificationAndDeclaration();

	private NotificationAndDeclaration loanForm = new NotificationAndDeclaration();

	private Integer loanApprovalGenericContentId = null;

	private Integer loanApprovalAdditionalContentKey = null;
	private Integer loanApprovalAdditionalContentId = null;
	private String loanApprovalAdditionalContentTypeCode = null;
	
	private ArrayList<String> detailText;

	private boolean ignoreWarning;

	public class ZipCode implements Serializable {
		private static final long serialVersionUID = 1L;

		private String zipCode1;

		private String zipCode2;

		private String zipCodeNonUSA;

		public void reset() {
			zipCodeNonUSA = null;
			zipCode1 = null;
			zipCode2 = null;
		}

		public String getZipCode1() {
			return zipCode1;
		}

		public void setZipCode1(String zipCode1) {
			this.zipCode1 = StringUtils.trim(zipCode1);
		}

		public String getZipCode2() {
			return zipCode2;
		}

		public void setZipCode2(String zipCode2) {
			this.zipCode2 = StringUtils.trim(zipCode2);
		}

		public String getZipCodeNonUSA() {
			return zipCodeNonUSA;
		}

		public void setZipCodeNonUSA(String zipCodeNonUSA) {
			this.zipCodeNonUSA = StringUtils.trim(zipCodeNonUSA);
		}

		protected String getValue() {
			if (isCountryUSA()) {
				return (zipCode1 == null ? "" : zipCode1)
						+ (zipCode2 == null ? "" : zipCode2);
			} else {
				return zipCodeNonUSA;
			}
		}

		protected void setValue(String zipCode) {
			String zipCodeTemp = StringUtils.stripToNull(zipCode);
			zipCode1 = null;
			zipCode2 = null;
			zipCodeNonUSA = null;
			if (isCountryUSA()) {
				if (zipCodeTemp == null) {
					return;
				}
				int len = zipCodeTemp.length();
				if (len > 0) {
					zipCode1 = zipCodeTemp.substring(0, len >= 5 ? 5 : len);
				}
				if (len > 5) {
					zipCode2 = zipCodeTemp.substring(5);
				}
			} else {
				zipCodeNonUSA = zipCodeTemp;
			}
		}
	}

	public class AddressStateCode implements Serializable {
		private static final long serialVersionUID = 1L;

		private String stateUSA;

		private String stateNonUSA;

		public void reset() {
			stateUSA = null;
			stateNonUSA = null;
		}

		public String getStateNonUSA() {
			return stateNonUSA;
		}

		public void setStateNonUSA(String stateNonUSA) {
			this.stateNonUSA = StringUtils.trim(stateNonUSA);
		}

		public String getStateUSA() {
			return stateUSA;
		}

		public void setStateUSA(String stateUSA) {
			this.stateUSA = StringUtils.trim(stateUSA);
		}

		protected String getValue() {
			if (isCountryUSA()) {
				return stateUSA;
			} else {
				return stateNonUSA;
			}
		}

		protected void setValue(String state) {
			if (isCountryUSA()) {
				stateUSA = state;
				stateNonUSA = null;
			} else {
				stateNonUSA = state;
				stateUSA = null;
			}
		}
	}

	public class NotificationAndDeclaration implements Serializable {
		private static final long serialVersionUID = 1L;

		private Integer htmlContentId;

		private Integer pdfContentId;

		private String html;

		private boolean accepted = false;

		public Integer getHtmlContentId() {
			return htmlContentId;
		}

		public void setHtmlContentId(Integer htmlContentId) {
			this.htmlContentId = htmlContentId;
		}

		public Integer getPdfContentId() {
			return pdfContentId;
		}

		public void setPdfContentId(Integer pdfContentId) {
			this.pdfContentId = pdfContentId;
		}

		public boolean isAccepted() {
			return accepted;
		}

		public void setAccepted(boolean accepted) {
			this.accepted = accepted;
		}

		public String getHtml() {
			return html;
		}

		public void setHtml(String html) {
			this.html = html;
		}
	}

	public boolean isIgnoreWarning() {
		return ignoreWarning;
	}

	public void setIgnoreWarning(boolean ignoreWarning) {
		this.ignoreWarning = ignoreWarning;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lasName) {
		this.lastName = lasName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	public String getEmploymentStatusCode() {
		return employmentStatusCode;
	}

	public void setEmploymentStatusCode(String employmentStatusCode) {
		this.employmentStatusCode = employmentStatusCode;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Integer participantId) {
		this.participantId = participantId;
	}

	public String getLegallyMarried() {
		return legallyMarried;
	}

	public void setLegallyMarried(String legallyMarried) {
		this.legallyMarried = legallyMarried;
	}

	public Integer getParticipantProfileId() {
		return participantProfileId;
	}

	public void setParticipantProfileId(Integer participantProfileId) {
		this.participantProfileId = participantProfileId;
	}

	private boolean isDeny() {
		return "deny".equals(getActionLabel()) || "Deny".equals(getAction());
	}

	private boolean isDelete() {
		return "delete".equals(getActionLabel());
	}

	/**
	 * @return the detailText
	 */
	public ArrayList<String> getDetailText() {
		return detailText;
	}

	/**
	 * @param detailText the detailText to set
	 */
	public void setDetailText(ArrayList<String> detailText) {
		this.detailText = detailText;
	}

	/**
	 * Checks that the input date value is in the format int/int/int and
	 * is a valid MM/dd/yyyy value.
	 * 
	 * @param aDate
	 * @return 0 = is a valid date, 1 = is not of the format int/int/int,
	 *         2 = is not a valid MM/dd/yyyy value.
	 */
	private int dateValidation(String aDate) {
		return CalendarUtils.isValidDateFormat(aDate);
	}

	private BigDecimal createBigDecimal(String str)
			throws NumberFormatException {
		if (str == null || StringUtils.isBlank(str)) {
			return null;
		}
		String tempStr = str;
		tempStr = tempStr.replace(",", "");
		tempStr = tempStr.replace("$", "");
		return NumberUtils.createBigDecimal(tempStr);
	}

	public Loan toLoan(Loan loan, Principal principal,
			boolean isAgreedToApprove, boolean isLoanPackageSelected) throws SystemException {

		DateConverter dateConverter = new DateConverter(null);
		String loginRoleCode = LoanConstants.USER_ROLE_PLAN_SPONSOR_CODE;
		if (principal.getRole().isTPA()) {
			loginRoleCode = LoanConstants.USER_ROLE_TPA_CODE;
		} else if(principal.getRole().isInternalUser()) {
			// Modified for Bundled GA Project 2012
			// The login role code becomes "JH" for Loans and Withdrawals
			// when the user is a bundled GA rep.
		    // Actually any internal user that is able to initiate a loan
		    // should have this code.
			loginRoleCode = LoanConstants.USER_ROLE_JH_BUNDLED_GA_CODE;
		}

		if (loan == null) {
			if (submissionId != null) {
				loan = LoanServiceDelegate.getInstance().read(
						(int) principal.getProfileId(), contractId,
						submissionId);
			} else {
				loan = LoanServiceDelegate.getInstance().initiateLoan(
						getParticipantProfileId(), contractId,
						(int) principal.getProfileId());
			}
		}
		
		if (loan.getSubmissionId() != null) {
			/*
			 * Revert the last updated ts to the one saved in the FORM. This is
			 * important because we want to detect concurrent update.
			 */
			loan.setLastUpdated(lastUpdatedTs);
		} else {
			loan.setCreatedByRoleCode(loginRoleCode);
			loan.setParticipantId(getParticipantId());
			loan.setParticipantProfileId(getParticipantProfileId());
		}

		loan.setIgnoreWarning(ignoreWarning);
		loan.setLoginUserProfileId((int) principal.getProfileId());
		
		/*
		 * Login user role code is required when an event is created.
		 */
		loan.setLoginRoleCode(loginRoleCode);

		if (isDelete()) {
			/*
			 * Deleting a loan doesn't require any validation.
			 */
			return loan;
		}

		List<LoanMessage> errors = loan.getErrors();

		if (StringUtils.isNotBlank(getTpaLoanFee())) {
			Matcher m = numericCharacterRegEx.matcher(getTpaLoanFee());
			String invalidChars = m.replaceAll("");
			if (invalidChars.length() > 0) {
				LoanError loanError = new LoanError(
						LoanErrorCode.TPA_LOAN_ISSUE_FEE_NON_NUMERIC,
						LoanField.TPA_LOAN_ISSUE_FEE);
				errors.add(loanError);
			} else {
				Fee fee = new LoanFee();
				fee.setTypeCode(Fee.DOLLAR_TYPE_CODE);
				try {
					fee.setValue(createBigDecimal(tpaLoanFee));
					if (fee.getValue().compareTo(
							LoanConstants.TPA_LOAN_ISSUE_FEE_MAXIMUM_VALUE) > 0) {
						LoanWarning loanWarning = new LoanWarning(
								LoanErrorCode.TPA_LOAN_ISSUE_FEE_EXCEEDS_MAXIMUM,
								LoanField.TPA_LOAN_ISSUE_FEE);
						errors.add(loanWarning);
					}
					if (loan.getFee() != null) {
						if (fee.getValue().compareTo(loan.getFee().getValue()) != 0) {
							loan.setFeeChanged(true);
						}
					}
					loan.setFee(fee);
				} catch (NumberFormatException e) {
					LoanError loanError = new LoanError(
							LoanErrorCode.TPA_LOAN_ISSUE_FEE_INVALID_FORMAT,
							LoanField.TPA_LOAN_ISSUE_FEE);
					errors.add(loanError);
				}
			}
		} else {
			if (loan.getFee() != null) {
				loan.setFeeChanged(true);
			}
			loan.setFee(null);
		}

		if (StringUtils.isNotBlank(currentParticipantNote)) {
			if (currentParticipantNote.length() > Note.MAXIMUM_LENGTH) {
				LoanError loanError = new LoanError(
						LoanErrorCode.NOTE_TO_PARTICIPANT_TOO_LONG,
						LoanField.CURRENT_PARTICIPANT_NOTE, Integer
								.toString(currentParticipantNote.length()));
				errors.add(loanError);
			}
			Matcher m = webMultiLineAllowedCharacterRegEx
					.matcher(currentParticipantNote);
			String invalidChars = m.replaceAll("");
			if (invalidChars.length() > 0) {
				LoanError loanError = new LoanError(
						LoanErrorCode.NOTE_TO_PARTICIPANT_INVALID_CHARACTERS,
						LoanField.CURRENT_PARTICIPANT_NOTE, invalidChars);
				errors.add(loanError);
			}
			LoanNote loanNote = new LoanNote();
			loanNote.setNoteTypeCode(Note.ADMIN_TO_PARTICIPANT_TYPE_CODE);
			loanNote.setNote(currentParticipantNote);
			loan.setCurrentParticipantNote(loanNote);
		} else {
			if (loan.getCurrentParticipantNote() != null
					&& StringUtils.isNotBlank(loan.getCurrentParticipantNote()
							.getNote()))
				loan.getCurrentParticipantNote()
						.setNote(currentParticipantNote);
		}

		if (StringUtils.isNotBlank(currentAdministratorNote)) {
			if (currentAdministratorNote.length() > Note.MAXIMUM_LENGTH) {
				LoanError loanError = new LoanError(
						LoanErrorCode.NOTE_TO_ADMINISTRATOR_TOO_LONG,
						LoanField.CURRENT_ADMINISTRATOR_NOTE, Integer
								.toString(currentAdministratorNote.length()));
				errors.add(loanError);
			}
			Matcher m = webMultiLineAllowedCharacterRegEx
					.matcher(currentAdministratorNote);
			String invalidChars = m.replaceAll("");
			if (invalidChars.length() > 0) {
				LoanError loanError = new LoanError(
						LoanErrorCode.NOTE_TO_ADMINISTRATOR_INVALID_CHARACTERS,
						LoanField.CURRENT_ADMINISTRATOR_NOTE, invalidChars);
				errors.add(loanError);
			}
			LoanNote loanNote = new LoanNote();
			loanNote.setNoteTypeCode(Note.ADMIN_TO_ADMIN_TYPE_CODE);
			loanNote.setNote(currentAdministratorNote);
			loan.setCurrentAdministratorNote(loanNote);
		} else {
			if (loan.getCurrentAdministratorNote() != null
					&& StringUtils.isNotBlank(loan
							.getCurrentAdministratorNote().getNote()))
				loan.getCurrentAdministratorNote().setNote(
						currentParticipantNote);
		}

		if (isDeny()) {
			/*
			 * Validations for Deny are done above (notes and fees)
			 * 
	         * Clear the Declarations & Managed Content Lists so they're not
	         * attempted to be inserted into the SDB.
	         */
	        loan.getDeclarations().clear();
	        loan.getManagedContents().clear();
	        
	        if(loan.getAcceptedParameter() != null){
	        	loan.getAcceptedParameter().setReadyToSave(true);
	        } else if (loan.getReviewedParameter() != null){
	        	loan.getReviewedParameter().setReadyToSave(true);
	        }
	        
			return loan;
		}

		if (legallyMarried != null) {
			loan.setLegallyMarriedInd("Y".equalsIgnoreCase(legallyMarried));
		}

		loan.setApplyIrs10KDollarRuleInd(isApplyIrs10KDollarRule());
		loan.setRequestDate((Date) dateConverter.convert(java.util.Date.class,
				requestDate));
		if (loan.getRequestDate() != null) {
			// Now set the time to be start of day (i.e. zero hundred hours).
			loan.setRequestDate(DateUtils.truncate(loan.getRequestDate(),
					Calendar.DAY_OF_MONTH));
		}

		loan.setLoanType(getLoanType());

		if (StringUtils.isNotBlank(loanReason)) {
			if (loanReason.length() > LoanConstants.LOAN_REASON_MAXIMUM_LENGTH) {
				LoanError loanError = new LoanError(
						LoanErrorCode.LOAN_REASON_TOO_LONG,
						LoanField.LOAN_REASON, Integer.toString(loanReason
								.length()));
				errors.add(loanError);
			}
			Matcher m = webMultiLineAllowedCharacterRegEx.matcher(loanReason);
			String invalidChars = m.replaceAll("");
			if (invalidChars.length() > 0) {
				LoanError loanError = new LoanError(
						LoanErrorCode.LOAN_REASON_INVALID_CHARACTER,
						LoanField.LOAN_REASON, invalidChars);
				errors.add(loanError);
			}
		}
		loan.setLoanReason(getLoanReason());

		if (!StringUtils.isBlank(expirationDate)) {
			/*
			 * Checks if the expirationDate is in the correct format and if it
			 * represents a valid date.
			 */
			LoanError loanError;
			switch (dateValidation(expirationDate)) {
			case 1:
				loanError = new LoanError(
						LoanErrorCode.EXPIRATION_DATE_INVALID_FORMAT,
						LoanField.EXPIRATION_DATE);
				errors.add(loanError);
				break;
			case 2:
				loanError = new LoanError(
						LoanErrorCode.EXPIRATION_DATE_INVALID_VALUE,
						LoanField.EXPIRATION_DATE);
				errors.add(loanError);
				break;
			default:
				loan.setExpirationDate((Date) dateConverter.convert(
						java.util.Date.class, expirationDate));
				// Now set the time to be start of day (i.e. zero hundred
				// hours).
				loan.setExpirationDate(DateUtils.truncate(loan
						.getExpirationDate(), Calendar.DAY_OF_MONTH));
			}
		} else {
			LoanError loanError;
			loanError = new LoanError(LoanErrorCode.MISSING_EXPIRATION_DATE,
					LoanField.EXPIRATION_DATE);
			errors.add(loanError);
		}

		if (!StringUtils.isEmpty(payrollDate)) {
			/*
			 * Checks if the payrollDate is in the correct format and if it
			 * represents a valid date.
			 */
			LoanError loanError;
			switch (dateValidation(payrollDate)) {
			case 1:
				loanError = new LoanError(
						LoanErrorCode.PAYROLL_DATE_INVALID_FORMAT,
						LoanField.PAYROLL_DATE);
				errors.add(loanError);
				break;
			case 2:
				loanError = new LoanError(
						LoanErrorCode.PAYROLL_DATE_INVALID_VALUE,
						LoanField.PAYROLL_DATE);
				errors.add(loanError);
				break;
			default:
				loan.setFirstPayrollDate((Date) dateConverter.convert(
						java.util.Date.class, payrollDate));
				// Now set the time to be start of day (i.e. zero hundred
				// hours).
				loan.setFirstPayrollDate(DateUtils.truncate(loan
						.getFirstPayrollDate(), Calendar.DAY_OF_MONTH));
			}
		} else {
			loan.setFirstPayrollDate((Date) dateConverter.convert(
					java.util.Date.class, getPayrollDate()));
		}

		if (StringUtils.isNotBlank(getDefaultProvision())) {
			if (getDefaultProvision().length() > LoanConstants.DEFAULT_PROVISION_MAXIMUM_LENGTH) {
				LoanError loanError = new LoanError(
						LoanErrorCode.DEFAULT_PROVISION_TOO_LONG,
						LoanField.DEFAULT_PROVISION, Integer
								.toString(getDefaultProvision().length()));
				errors.add(loanError);
			}
			Matcher m = webMultiLineAllowedCharacterRegEx
					.matcher(getDefaultProvision());
			String invalidChars = m.replaceAll("");
			if (invalidChars.length() > 0) {
				LoanError loanError = new LoanError(
						LoanErrorCode.DEFAULT_PROVISION_INVALID_CHARACTER,
						LoanField.DEFAULT_PROVISION, invalidChars);
				errors.add(loanError);
			}
		}
		loan.setDefaultProvision(getDefaultProvision());

		loan.setMaturityDate((Date) dateConverter.convert(java.util.Date.class,
				getMaturityDate()));
		if (loan.getMaturityDate() != null) {
			// Now set the time to be start of day (i.e. zero hundred hours).
			loan.setMaturityDate(DateUtils.truncate(loan.getMaturityDate(),
					Calendar.DAY_OF_MONTH));
		}

		loan.setMaximumAmortizationYears(maximumAmortizationPeriodYears);

		/*
		 * Notice that we are only setting the loan parameter object when the
		 * request is DRAFT or PENDING REVIEW. In all other cases, nothing can
		 * be changed by the user on screen.
		 */
		LoanParameter loanParameter = null;
		if (LoanStateEnum.DRAFT.getStatusCode().equals(status)) {
			loanParameter = loan.getOriginalParameter();
			if (loanParameter == null) {
				loanParameter = new LoanParameter();
				loanParameter.setStatusCode(status);
				loan.setOriginalParameter(loanParameter);
			}
		} else if (LoanStateEnum.PENDING_REVIEW.getStatusCode().equals(status)) {
			loanParameter = loan.getReviewedParameter();
			if (loanParameter == null) {
				loanParameter = new LoanParameter();
				loanParameter.setStatusCode(status);
				loan.setReviewedParameter(loanParameter);
			}
		}

		if (loanParameter != null) {
			loanParameter.setReadyToSave(true);
			if (StringUtils.isNotBlank(loanAmount)) {
				Matcher m = numericCharacterRegEx.matcher(loanAmount);
				String invalidChars = m.replaceAll("");
				if (invalidChars.length() > 0) {
					LoanError loanError = new LoanError(
							LoanErrorCode.LOAN_AMOUNT_BLANK_OR_NON_NUMERIC,
							LoanField.LOAN_AMOUNT);
					errors.add(loanError);
				} else {
					try {
						loanParameter
								.setLoanAmount(createBigDecimal(loanAmount));
					} catch (NumberFormatException e) {
						LoanError loanError = new LoanError(
								LoanErrorCode.LOAN_AMOUNT_INVALID_FORMAT,
								LoanField.LOAN_AMOUNT);
						errors.add(loanError);
					}
				}
			} else {
				// loanAmount == null
				loanParameter.setLoanAmount(createBigDecimal(loanAmount));
			}

			try {
				loanParameter.setPaymentAmount(createBigDecimal(paymentAmount));
			} catch (NumberFormatException e) {
				throw new SystemException(e, "Problem converting"
						+ " to BigDecimal, paymentAmount = " + paymentAmount);
			}

			if (StringUtils.isNotBlank(interestRate)) {
				Matcher m = numericCharacterNoCommaRegEx.matcher(interestRate);
				String invalidChars = m.replaceAll("");
				if (invalidChars.length() > 0) {
					LoanError loanError = new LoanError(
							LoanErrorCode.INTEREST_RATE_BLANK_OR_NON_NUMERIC,
							LoanField.INTEREST_RATE);
					errors.add(loanError);
				} else {
					BigDecimal interestRateBigDecimal = null;
					try {
						interestRateBigDecimal = createBigDecimal(interestRate);
					} catch (NumberFormatException e) {
						LoanError loanError = new LoanError(
								LoanErrorCode.INTEREST_RATE_INVALID_FORMAT,
								LoanField.INTEREST_RATE);
						errors.add(loanError);
					}
					if (interestRateBigDecimal != null) {
					    if (interestRateBigDecimal
									.compareTo(LoanConstants.INTEREST_RATE_MAXIMUM) >= 0) {
					        LoanError loanError = new LoanError(
					                LoanErrorCode.INTEREST_RATE_OUT_OF_RANGE,
					                LoanField.INTEREST_RATE);
					        errors.add(loanError);
					    } else if (interestRateBigDecimal
                            .compareTo(LoanConstants.INTEREST_RATE_MINIMUM) <= 0) {
                                LoanError loanError = new LoanError(
                                        LoanErrorCode.INTEREST_RATE_OUT_OF_RANGE,
                                        LoanField.INTEREST_RATE);
                                errors.add(loanError);
                        }
					}    
					loanParameter.setInterestRate(interestRateBigDecimal);
				}
			} else {
				// interestRate == null
				loanParameter.setInterestRate(createBigDecimal(interestRate));
			}

			loanParameter.setPaymentFrequency(StringUtils
					.trimToNull(paymentFrequency));

			try {
				loanParameter
						.setMaximumAvailable(createBigDecimal(maximumLoanAvailable));
			} catch (NumberFormatException e) {
				// Leave current value as is.
			}

			loanParameter.setAmortizationMonths(getAmortizationMonths());
		}

		List<LoanMoneyType> moneyTypes = loan.getMoneyTypesWithAccountBalance();

		for (LoanMoneyType moneyType : moneyTypes) {
			String moneyTypeId = moneyType.getMoneyTypeId();
			String vestingPercentage = StringUtils
					.trimToNull(moneyTypeVestingPercentages.get(moneyTypeId));
			if (!StringUtils.isBlank(vestingPercentage)) {
				String fieldName = LoanField.MONEY_TYPE_VESTING_PERCENTAGE_PREFIX
						.getFieldName()
						+ "_" + moneyTypeId;

				Matcher m = numericCharacterNoCommaRegEx
						.matcher(vestingPercentage);
				String invalidChars = m.replaceAll("");
				if (invalidChars.length() > 0) {
					LoanError loanError = new LoanError(
							LoanErrorCode.VESTING_PERCENTAGE_NON_NUMERIC,
							fieldName);
					errors.add(loanError);
				} else {
					BigDecimal vestingPercentageBigDecimal = null;
					try {
						vestingPercentageBigDecimal = createBigDecimal(vestingPercentage);
					} catch (NumberFormatException e) {
						LoanError vestingPercentageError = new LoanError(
								LoanErrorCode.VESTING_PERCENTAGE_INVALID_FORMAT,
								fieldName);
						errors.add(vestingPercentageError);
					}

					if (vestingPercentageBigDecimal != null
							&& (vestingPercentageBigDecimal
									.compareTo(LoanConstants.VESTING_PERCENTAGE_MAXIMUM) > 0)) {
						LoanError vestingPercentageError = new LoanError(
								LoanErrorCode.VESTING_PERCENTAGE_TOO_HIGH,
								fieldName);
						errors.add(vestingPercentageError);
					}
					moneyType.setVestingPercentage(vestingPercentageBigDecimal);
				}
			} else {
				moneyType.setVestingPercentage(null);
			}

			moneyType.setExcludeIndicator(getMoneyTypeExcludedInd(moneyTypeId));
		}

		if (StringUtils.isNotBlank(maxBalanceLast12Months)) {
			Matcher m = numericCharacterRegEx.matcher(maxBalanceLast12Months);
			String invalidChars = m.replaceAll("");
			if (invalidChars.length() > 0) {
				LoanError loanError = new LoanError(
						LoanErrorCode.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS_NON_NUMERIC,
						LoanField.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS);
				errors.add(loanError);
			} else {
				BigDecimal maxBalanceLast12MonthsBigDecimal = null;
				try {
					maxBalanceLast12MonthsBigDecimal = createBigDecimal(maxBalanceLast12Months);
				} catch (NumberFormatException e) {
					LoanError vestingPercentageError = new LoanError(
							LoanErrorCode.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS_INVALID_FORMAT,
							LoanField.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS);
					errors.add(vestingPercentageError);
				}
				loan
						.setMaxBalanceLast12Months(maxBalanceLast12MonthsBigDecimal);
			}
		} else {
			loan.setCurrentOutstandingBalance(null);
		}

		if (StringUtils.isNotBlank(outstandingLoansCount)) {
			try {
				loan.setOutstandingLoansCount(NumberUtils
						.createInteger(outstandingLoansCount));
			} catch (NumberFormatException e) {
				LoanError loanError = new LoanError(
						LoanErrorCode.OUTSTANDING_LOANS_COUNT_NON_NUMERIC,
						LoanField.OUTSTANDING_LOANS_COUNT);
				errors.add(loanError);
			}
		} else {
			loan.setOutstandingLoansCount(null);
		}

		if (StringUtils.isNotBlank(currentOutstandingBalance)) {
			Matcher m = numericCharacterRegEx
					.matcher(currentOutstandingBalance);
			String invalidChars = m.replaceAll("");
			if (invalidChars.length() > 0) {
				LoanError loanError = new LoanError(
						LoanErrorCode.CURRENT_OUTSTANDING_LOAN_BALANCE_NON_NUMERIC,
						LoanField.CURRENT_OUTSTANDING_LOAN_BALANCE);
				errors.add(loanError);
			} else {
				BigDecimal currentOutstandingBalanceBigDecimal = null;
				try {
					currentOutstandingBalanceBigDecimal = createBigDecimal(currentOutstandingBalance);
				} catch (NumberFormatException e) {
					LoanError vestingPercentageError = new LoanError(
							LoanErrorCode.CURRENT_OUTSTANDING_LOAN_BALANCE_INVALID_FORMAT,
							LoanField.CURRENT_OUTSTANDING_LOAN_BALANCE);
					errors.add(vestingPercentageError);
				}
				loan
						.setCurrentOutstandingBalance(currentOutstandingBalanceBigDecimal);
			}
		} else {
			loan.setCurrentOutstandingBalance(null);
		}

		if (!isPaymentInstructionSuppressed()) {

			DistributionAddress address = new LoanAddress();
			address.setAddressLine1(StringUtils.trimToNull(addressLine1));
			address.setAddressLine2(StringUtils.trimToNull(addressLine2));
			address.setCity(StringUtils.trimToNull(city));
			address.setStateCode(StringUtils.trimToNull(stateCode.getValue()));
			address.setCountryCode(StringUtils.trimToNull(country));
			address.setZipCode(StringUtils.trimToNull(zipCode.getValue()));

			Payee payee = new LoanPayee();
			payee.setAddress(address);
			payee.setPaymentMethodCode(paymentMethod);
			payee.setFirstName(firstName);
			payee.setLastName(lastName);
			payee.setTypeCode(Payee.TYPE_CODE_PARTICIPANT);
			payee.setReasonCode(Payee.REASON_CODE_PAYMENT);

			if (Payee.ACH_PAYMENT_METHOD_CODE.equals(paymentMethod)
					|| Payee.WIRE_PAYMENT_METHOD_CODE.equals(paymentMethod)) {

				PaymentInstruction paymentInstruction = new LoanPaymentInstruction();
				if (StringUtils.isNotBlank(abaRoutingNumber)) {
					boolean abaRoutingNumberValid = true;
					if (abaRoutingNumber.length() != LoanConstants.ABA_ROUTING_NUMBER_LENGTH) {
						abaRoutingNumberValid = false;
					} else {
						for (int i = 0; i < LoanConstants.ABA_ROUTING_NUMBER_LENGTH; i++) {
							if (abaRoutingNumber.charAt(i) < '0'
									|| abaRoutingNumber.charAt(i) > '9') {
								abaRoutingNumberValid = false;
							}
						}
					}
					if (abaRoutingNumberValid) {
						try {
							paymentInstruction.setBankTransitNumber(Integer
									.parseInt(abaRoutingNumber));
						} catch (NumberFormatException e) {
							abaRoutingNumberValid = false;
						}
					}
					if (!abaRoutingNumberValid) {
						LoanError loanError = new LoanError(
								LoanErrorCode.ABA_ROUTING_NUMBER_NON_NUMERIC,
								LoanField.ABA_ROUTING_NUMBER);
						errors.add(loanError);
					}
				} else if (StringUtils.isWhitespace(abaRoutingNumber)
						&& abaRoutingNumber.length() > 0) {
					LoanError loanError = new LoanError(
							LoanErrorCode.ABA_ROUTING_NUMBER_NON_NUMERIC,
							LoanField.ABA_ROUTING_NUMBER);
					errors.add(loanError);
				}

				paymentInstruction
						.setBankName(StringUtils.trimToNull(bankName));

				if (!Payee.WIRE_PAYMENT_METHOD_CODE.equals(paymentMethod)) {
					paymentInstruction.setBankAccountTypeCode(accountType);
				}

				paymentInstruction.setBankAccountNumber(StringUtils
						.trimToNull(accountNumber));
				payee.setPaymentInstruction(paymentInstruction);
			}

			LoanRecipient recipient = new LoanRecipient();
			recipient.setAddress((DistributionAddress) ((LoanAddress) address)
					.clone());
			recipient.setFirstName(firstName);
			recipient.setLastName(lastName);
			recipient.getPayees().add(payee);

			loan.setRecipient(recipient);
		}

		/*
		 * Declarations & Managed Content
		 */
		loan.getDeclarations().clear();
		loan.getManagedContents().clear();

		if (loan.isParticipantInitiated()) {
			if (JdbcHelper.INDICATOR_YES.equals(loan.getAtRiskInd())) {
				if (atRiskTransaction.isAccepted() ) {
					LoanDeclaration declaration = new LoanDeclaration();
					declaration.setTypeCode(LoanDeclaration.AT_RISK_TRANSACTION_TYPE_CODE);
					loan.getDeclarations().add(declaration);
				}
				if (isAgreedToApprove) {
					if(loan.getAtRiskDetailsVO() != null) {
						
		            	AtRiskDetailsInputVO atRiskDetils = new AtRiskDetailsInputVO();
		            	atRiskDetils.setSubmissionId(loan.getSubmissionId());
		            	atRiskDetils.setLoanOrWithdrawalReq(RequestType.LOAN);
		            	atRiskDetils.setProfileId(loan.getParticipantProfileId());
		            	atRiskDetils.setContractId(loan.getContractId());
		            	atRiskDetils.setParticipantInitiated(loan.isParticipantInitiated());
		            	List<Object>  atRiskTextList = LoanAndWithdrawalDisplayHelper.atRiskDisplayText(atRiskDetils,loan.getAtRiskDetailsVO());
		            	
		            	if(atRiskTextList != null && atRiskTextList.size() > 0){
		            	Map<Integer, Integer> content = ((Map<Integer, Integer>)atRiskTextList.get(2));
		            	
		            	for (Map.Entry<Integer, Integer> entry : content.entrySet()) {
							addManagedContent(loan, ManagedContent.AT_RISK_TEXT,
									entry.getKey(),
									entry.getValue());
							}
		            	
		            	}
						setAtRiskTransactionInd(Boolean.TRUE);
					}
				}
			}
		} else {
			if (loan.isDeclartionSectionDisplayed()) {
				if (truthInLendingNoticeAcceptedOriginalValue) {
					truthInLendingNotice.setAccepted(truthInLendingNoticeAcceptedOriginalValue);
				}

				if (promissoryNoteAcceptedOriginalValue) {
					promissoryNote.setAccepted(promissoryNoteAcceptedOriginalValue);
				}
			}
			if (truthInLendingNotice.isAccepted()) {
				LoanDeclaration declaration = new LoanDeclaration();
				declaration
						.setTypeCode(LoanDeclaration.TRUTH_IN_LENDING_NOTICE);
				loan.getDeclarations().add(declaration);
			}
			if (promissoryNote.isAccepted()) {
				LoanDeclaration declaration = new LoanDeclaration();
				declaration
						.setTypeCode(LoanDeclaration.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE);
				loan.getDeclarations().add(declaration);
			}	
			if (isAgreedToApprove) {
				addManagedContent(loan,
						ManagedContent.TRUTH_IN_LENDING_NOTICE_HTML,
						LoanContentConstants.TRUTH_IN_LENDING_NOTICE_HTML,
						truthInLendingNotice.getHtmlContentId());

				addManagedContent(loan,
						ManagedContent.TRUTH_IN_LENDING_NOTICE_PDF,
						LoanContentConstants.TRUTH_IN_LENDING_NOTICE_PDF,
						truthInLendingNotice.getPdfContentId());

				addManagedContent(
						loan,
						ManagedContent.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE_HTML,
						LoanContentConstants.PROMISSORYNOTE_AND_IRREVOCABLEPLEDGE_HTML,
						promissoryNote.getHtmlContentId());

				addManagedContent(
						loan,
						ManagedContent.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE_PDF,
						LoanContentConstants.PROMISSORYNOTE_AND_IRREVOCABLEPLEDGE_PDF,
						promissoryNote.getPdfContentId());

				addManagedContent(loan,
						ManagedContent.AMORTIZATION_SCHEDULE_HTML,
						LoanContentConstants.AMORTIZATION_SCHEDULE_HTML,
						amortizationSchedule.getHtmlContentId());

				addManagedContent(loan,
						ManagedContent.AMORTIZATION_SCHEDULE_PDF,
						LoanContentConstants.AMORTIZATION_SCHEDULE_PDF,
						amortizationSchedule.getPdfContentId());
			}
		}

		if (isAgreedToApprove) {
			addManagedContent(
					loan,
					ManagedContent.LOAN_APPROVAL_GENERIC,
					com.manulife.pension.ps.web.onlineloans.LoanContentConstants.APPROVAL_DIALOG_TEXT,
					loanApprovalGenericContentId);
			addManagedContent(loan, loanApprovalAdditionalContentTypeCode,
					loanApprovalAdditionalContentKey,
					loanApprovalAdditionalContentId);
		}

		if (isLoanPackageSelected) {
			addManagedContent(loan, ManagedContent.TRUTH_IN_LENDING_NOTICE_PDF,
					LoanContentConstants.TRUTH_IN_LENDING_NOTICE_PDF,
					truthInLendingNotice.getPdfContentId());
			addManagedContent(
					loan,
					ManagedContent.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE_PDF,
					LoanContentConstants.PROMISSORYNOTE_AND_IRREVOCABLEPLEDGE_PDF,
					promissoryNote.getPdfContentId());
			addManagedContent(
					loan,
					ManagedContent.LOAN_PACKAGE_INSTRUCTIONS_FOR_PARTICIPANT,
					LoanContentConstants.LOAN_PACKAGE_INSTRUCTIONS_FOR_PARTICIPANT,
					loanPackageInstructionsForParticipant.getPdfContentId());
			addManagedContent(loan,
					ManagedContent.LOAN_PACKAGE_INSTRUCTIONS_FOR_ADMIN,
					LoanContentConstants.LOAN_PACKAGE_INSTRUCTIONS_FOR_ADMIN,
					loanPackageInstructionsForPlanAdministrator
							.getPdfContentId());
			addManagedContent(loan, ManagedContent.AMORTIZATION_SCHEDULE_PDF,
					LoanContentConstants.AMORTIZATION_SCHEDULE_PDF,
					amortizationSchedule.getPdfContentId());
			addManagedContent(loan, ManagedContent.LOAN_FORM_PDF,
					LoanContentConstants.LOAN_FORM_PDF, loanForm.getPdfContentId());
		}

		return loan;
	}

	private void addManagedContent(Loan loan, String contentTypeCode,
			Integer contentKey, Integer contentId) {
		ManagedContent content = new ManagedContent();
		content.setCmaSiteCode(ManagedContent.CMA_SITE_CODE_PSW);
		content.setContentId(contentId);
		content.setContentKey(contentKey);
		content.setContentTypeCode(contentTypeCode);
		loan.getManagedContents().add(content);
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}

	public void parseLoan(Loan loan, LoanPlanData loanPlanData)
			throws SystemException {

		/*
		 * Save the last updated timestamp from the retrieved loan.
		 */
		lastUpdatedTs = loan.getLastUpdated();

		EnvironmentServiceDelegate envDelegate = EnvironmentServiceDelegate
				.getInstance(Constants.PS_APPLICATION_ID);

		DateFormat dateFormatter = ConverterHelper.getDefaultDateFormat();
		NumberFormat amountFormatter = NumberFormat.getNumberInstance();
		amountFormatter.setMinimumFractionDigits(2);
		Calendar today = Calendar.getInstance();

		this.contractId = loan.getContractId();
		this.submissionId = loan.getSubmissionId();
		this.participantProfileId = loan.getParticipantProfileId();
		if (loan.getLegallyMarriedInd() != null) {
			this.legallyMarried = loan.getLegallyMarriedInd() ? "Y" : "N";
		}
		this.setApplyIrs10KDollarRule(loan.getApplyIrs10KDollarRuleInd());
		this.participantId = loan.getParticipantId();
		this.participantProfileId = loan.getParticipantProfileId();
		this.createdByRoleCode = loan.getCreatedByRoleCode();

		this.status = loan.getStatus();
		this.loanType = loan.getLoanType();
		this.loanReason = loan.getLoanReason();
		this.expirationDate = dateFormatter.format(loan.getExpirationDate());
		if (LoanStateEnum.DRAFT.getStatusCode().equals(loan.getStatus())) {
			this.requestDate = dateFormatter.format(today.getTime());
		} else {
			this.requestDate = dateFormatter.format(loan.getRequestDate());
		}

		this.startDate = dateFormatter.format(loan.getEffectiveDate());

		if (loan.getFirstPayrollDate() != null) {
			this.payrollDate = dateFormatter.format(loan.getFirstPayrollDate());
		}

		// If contract does not have a TPA firm, set the TPA loan issue fee to
		// the empty string so it can be saved as null later on.
		if (loanPlanData.getThirdPartyAdminId() != null) {
			this.tpaLoanFee = loan.getFee() != null ? amountFormatter
					.format(loan.getFee().getValue().doubleValue()) : "";
					//CL 122340 fix - comma in TPA loan issue fee causes problem
					if(tpaLoanFee.indexOf(",") !=-1){
						this.tpaLoanFee = tpaLoanFee.replaceAll(",","");
					}
					
		} else {
			this.tpaLoanFee = "";
		}
		this.defaultProvision = loan.getDefaultProvision();
		if (loan.getMaturityDate() != null) {
			this.maturityDate = dateFormatter.format(loan.getMaturityDate());
		}
		this.maximumAmortizationPeriodYears = loan
				.getMaximumAmortizationYears();

		LoanParameter loanParameter = null;
		if (LoanStateEnum.DRAFT.getStatusCode().equals(status)) {
			loanParameter = loan.getOriginalParameter();
		} else if (LoanStateEnum.PENDING_REVIEW.getStatusCode().equals(status)) {
			loanParameter = loan.getReviewedParameter();
		}

		if (loanParameter != null) {
			setAmortizationMonths(loanParameter.getAmortizationMonths());
			loanAmount = loanParameter.getLoanAmount() != null ? loanParameter
					.getLoanAmount().toString() : "";
			paymentAmount = loanParameter.getPaymentAmount() != null ? loanParameter
					.getPaymentAmount().toString()
					: "";
			paymentFrequency = loanParameter.getPaymentFrequency();

			if (paymentFrequency == null
					&& !PlanData.PAYROLL_FREQUENCY_UNSPECIFIED
							.equals(loanPlanData.getPayrollFrequency())) {
				paymentFrequency = loanPlanData.getPayrollFrequency();
			}

			if (loanParameter.getInterestRate() == null) {
				BigDecimal primeRate = envDelegate.getWSJPrimeRate(new Date());
				BigDecimal loanInterestRateAdjustment = loanPlanData
						.getLoanInterestRateOverPrime();
				interestRate = primeRate.add(loanInterestRateAdjustment)
						.multiply(new BigDecimal(100)).setScale(
								LoanConstants.LOAN_INTEREST_RATE_SCALE,
								LoanConstants.LOAN_INTEREST_RATE_ROUND_RULE)
						.toString();
			} else {
				interestRate = loanParameter.getInterestRate().toString();
			}
		}

		if (loan.getOriginalParameter() != null) {
		    loanAmountOriginalValue = loan.getOriginalParameter().getLoanAmount()
	            != null ? loan.getOriginalParameter().getLoanAmount().toString()
	                    : "";
		}
		
		for (LoanMoneyType moneyType : loan.getMoneyTypesWithAccountBalance()) {
			String moneyTypeId = moneyType.getMoneyTypeId();
			String formMoneyTypeVestingPercentage = moneyTypeVestingPercentages
					.get(moneyTypeId);
			if (formMoneyTypeVestingPercentage == null) {
				if (moneyType.getVestingPercentage() != null) {
					setMoneyTypeVestingPercentage(moneyTypeId, moneyType
							.getVestingPercentage().setScale(
									LoanConstants.VESTING_PERCENTAGE_SCALE)
							.toString());
				} else {
					setMoneyTypeVestingPercentage(moneyTypeId, "0.000");
				}
			}
			Boolean formMoneyTypeExcludedInd = moneyTypeExcludedInds
					.get(moneyTypeId);
			if (formMoneyTypeExcludedInd == null) {
				setMoneyTypeExcludedInd(moneyTypeId, new Boolean(moneyType
						.getExcludeIndicator()));
			}
		}

		this.maxBalanceLast12Months = loan.getMaxBalanceLast12Months() != null ? amountFormatter
				.format(loan.getMaxBalanceLast12Months().doubleValue())
				: "";
		if (loan.getOutstandingLoansCount() != null) {
			outstandingLoansCount = loan.getOutstandingLoansCount().toString();
		}
		this.currentOutstandingBalance = loan.getCurrentOutstandingBalance() != null ? amountFormatter
				.format(loan.getCurrentOutstandingBalance().doubleValue())
				: "";
		if (loan.getCurrentAdministratorNote() != null) {
			currentAdministratorNote = loan.getCurrentAdministratorNote()
					.getNote();
		}
		if (loan.getCurrentParticipantNote() != null) {
			currentParticipantNote = loan.getCurrentParticipantNote().getNote();
		}

		if (!isPaymentInstructionSuppressed()) {
			if (loan.getRecipient() != null) {
				Recipient recipient = loan.getRecipient();
				Collection<Payee> payees = recipient.getPayees();
				if (payees != null && payees.size() > 0) {
					Payee payee = payees.iterator().next();
					DistributionAddress address = payee.getAddress();
					addressLine1 = address.getAddressLine1();
					addressLine2 = address.getAddressLine2();
					city = address.getCity();
					country = address.getCountryCode();
					zipCode = new ZipCode();
					zipCode.setValue(address.getZipCode());
					stateCode = new AddressStateCode();
					stateCode.setValue(address.getStateCode());

					PaymentInstruction paymentInstruction = payee
							.getPaymentInstruction();
					if (paymentInstruction.getBankTransitNumber() != null) {
						if (paymentInstruction.getBankTransitNumber() == 0) {
							abaRoutingNumber = null;
						} else {
							abaRoutingNumber = String
									.valueOf(paymentInstruction
											.getBankTransitNumber());
							if (StringUtils.isNotBlank(abaRoutingNumber)
									&& abaRoutingNumber.length() < LoanConstants.ABA_ROUTING_NUMBER_LENGTH) {
								// Pad with leading zeroes.
								abaRoutingNumber = StringUtils
										.leftPad(
												abaRoutingNumber,
												LoanConstants.ABA_ROUTING_NUMBER_LENGTH,
												'0');
							}
						}
					}
					bankName = paymentInstruction.getBankName();
					accountType = paymentInstruction.getBankAccountTypeCode();
					accountNumber = paymentInstruction.getBankAccountNumber();
					paymentMethod = payee.getPaymentMethodCode();
				}
			}
		}
		maximumAmortizationYearsMap = loanPlanData
				.getMaximumAmortizationYearsMap();

		LoanDocumentServiceDelegate loanDocumentServiceDelegate = LoanDocumentServiceDelegate
				.getInstance();
		for (LoanDeclaration declaration : loan.getDeclarations()) {
			if (Declaration.TRUTH_IN_LENDING_NOTICE.equals(declaration
					.getTypeCode())) {
				truthInLendingNotice.setAccepted(true);
			} else if (Declaration.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE
					.equals(declaration.getTypeCode())) {
				promissoryNote.setAccepted(true);
			} else if (Declaration.LOAN_PARTICIPANT_AUTHORIZATION
					.equals(declaration.getTypeCode())) {
				participantDeclaration.setAccepted(true);
				ManagedContent managedContent = loan
						.getManagedContent(ManagedContent.LOAN_PARTICIPANT_AUTHORIZATION);
				ContentText contentText = loanDocumentServiceDelegate
						.getContentTextById(managedContent.getContentId());
				participantDeclaration.setHtml(contentText.getText());
				participantDeclaration.setHtmlContentId(managedContent
						.getContentId());
			} else if (Declaration.AT_RISK_TRANSACTION_TYPE_CODE
					.equals(declaration.getTypeCode())) {
				atRiskTransaction.setAccepted(true);
			}
		}

		/*
		 * The following managed content corresponds to Declaration section
		 * checkboxes that are editable.
		 */
		ManagedContent managedContent = loan
				.getManagedContent(ManagedContent.TRUTH_IN_LENDING_NOTICE_HTML);
		if (managedContent != null) {
			truthInLendingNotice
					.setHtmlContentId(managedContent.getContentId());
		} else {
			// save the latest version from CMA.
			populateNotice(truthInLendingNotice, loanDocumentServiceDelegate,
					LoanContentConstants.TRUTH_IN_LENDING_NOTICE_HTML,
					LoanContentConstants.TRUTH_IN_LENDING_NOTICE_PDF);
		}

		managedContent = loan
				.getManagedContent(ManagedContent.PROMISSORY_NOTE_AND_IRREVOCABLE_PLEDGE_HTML);
		if (managedContent != null) {
			promissoryNote.setHtmlContentId(managedContent.getContentId());
		} else {
			// save the latest version from CMA.
			populateNotice(
					promissoryNote,
					loanDocumentServiceDelegate,
					LoanContentConstants.PROMISSORYNOTE_AND_IRREVOCABLEPLEDGE_HTML,
					LoanContentConstants.PROMISSORYNOTE_AND_IRREVOCABLEPLEDGE_PDF);
		}

		managedContent = loan
				.getManagedContent(ManagedContent.AMORTIZATION_SCHEDULE_HTML);
		if (managedContent != null) {
			amortizationSchedule
					.setHtmlContentId(managedContent.getContentId());
		} else {
			// save the latest version from CMA.
			populateNotice(amortizationSchedule, loanDocumentServiceDelegate,
					LoanContentConstants.AMORTIZATION_SCHEDULE_HTML,
					LoanContentConstants.AMORTIZATION_SCHEDULE_PDF);
		}

		/*
		 * Other managed content.
		 */
		managedContent = loan
				.getManagedContent(ManagedContent.LOAN_PACKAGE_INSTRUCTIONS_FOR_PARTICIPANT);
		if (managedContent != null) {
			loanPackageInstructionsForParticipant
					.setPdfContentId(managedContent.getContentId());
		} else {
			// save the latest version from CMA.
			populateNotice(
					loanPackageInstructionsForParticipant,
					loanDocumentServiceDelegate,
					null,
					LoanContentConstants.LOAN_PACKAGE_INSTRUCTIONS_FOR_PARTICIPANT);
		}

		managedContent = loan
				.getManagedContent(ManagedContent.LOAN_PACKAGE_INSTRUCTIONS_FOR_ADMIN);
		if (managedContent != null) {
			loanPackageInstructionsForPlanAdministrator
					.setPdfContentId(managedContent.getContentId());
		} else {
			// save the latest version from CMA.
			populateNotice(loanPackageInstructionsForPlanAdministrator,
					loanDocumentServiceDelegate, null,
					LoanContentConstants.LOAN_PACKAGE_INSTRUCTIONS_FOR_ADMIN);
		}

		managedContent = loan.getManagedContent(ManagedContent.LOAN_FORM_PDF);
		if (managedContent != null) {
			loanForm.setPdfContentId(managedContent.getContentId());
		} else {
			// save the latest version from CMA.
			populateNotice(loanForm, loanDocumentServiceDelegate, null,
					LoanContentConstants.LOAN_FORM_PDF);
		}

		if (JdbcHelper.INDICATOR_YES.equals(loan.getAtRiskInd())
				&& loan.getAtRiskDetailsVO() != null){
			setAtRiskTransactionInd(Boolean.TRUE);

			//start here? pending approval view option ---before
			AtRiskDetailsInputVO atRiskDetils = new AtRiskDetailsInputVO();
        	atRiskDetils.setSubmissionId(loan.getSubmissionId());
        	atRiskDetils.setLoanOrWithdrawalReq(RequestType.LOAN);
        	atRiskDetils.setProfileId(loan.getParticipantProfileId());
        	atRiskDetils.setContractId(loan.getContractId());
        	atRiskDetils.setParticipantInitiated(loan.isParticipantInitiated());
        	List<Object>  atRiskTextList = LoanAndWithdrawalDisplayHelper.atRiskDisplayText(atRiskDetils,loan.getAtRiskDetailsVO());
        	
        	if(atRiskTextList != null && atRiskTextList.size() > 0){
        		atRiskTransaction.setHtml(((ArrayList<String>)atRiskTextList.get(0)).get(0)); // approval text
        		setDetailText((ArrayList<String>)atRiskTextList.get(1));
        	}
		
		}

		if (LoanStateEnum.PENDING_APPROVAL.getStatusCode().equals(getStatus())
				|| LoanStateEnum.PENDING_REVIEW.getStatusCode().equals(
						getStatus())) {
			// save latest content versions to be displayed on the Approval
			// confirmation page.
			ContentText contentHtml = loanDocumentServiceDelegate
					.getContentTextByKey(com.manulife.pension.ps.web.onlineloans.LoanContentConstants.APPROVAL_DIALOG_TEXT);
            if (contentHtml != null) {
                setLoanApprovalGenericContentId(contentHtml.getId());
            } else {
                throw new SystemException("Unexpected null returned for "
                        + "contentHtml for contentKey = "
                        + com.manulife.pension.ps.web.onlineloans.LoanContentConstants.APPROVAL_DIALOG_TEXT);
            }
			setAdditionalApprovalInfo(loan, loanPlanData,
					loanDocumentServiceDelegate);
		}
	}

	private void populateNotice(NotificationAndDeclaration notice,
			LoanDocumentServiceDelegate loanDocumentServiceDelegate,
			Integer htmlContentKey, Integer pdfContentKey)
			throws SystemException {
		if (htmlContentKey != null) {
			ContentText contentHtml = loanDocumentServiceDelegate
					.getContentTextByKey(htmlContentKey);
            if (contentHtml != null) {
                notice.setHtmlContentId(contentHtml.getId());
            } else {
                throw new SystemException("Unexpected null returned for "
                        + "contentHtml for contentKey = "
                        + htmlContentKey);
            }
		}
		if (pdfContentKey != null) {
			ContentText contentPdf = loanDocumentServiceDelegate
					.getContentTextByKey(pdfContentKey);
            if (contentPdf != null) {
                notice.setPdfContentId(contentPdf.getId());
            } else {
                throw new SystemException("Unexpected null returned for "
                        + "pdfContentKey for contentKey = "
                        + pdfContentKey);
            }
		}
	}

	private void setAdditionalApprovalInfo(Loan loan,
			LoanPlanData loanPlanData,
			LoanDocumentServiceDelegate loanDocumentServiceDelegate)
			throws SystemException {
		String spousalConstentReqdInd = loanPlanData.getSpousalConsentReqdInd();
		if (loan.isParticipantInitiated()) {
			if (spousalConstentReqdInd == null) {
				setLoanApprovalAdditionalContentKey(com.manulife.pension.ps.web.onlineloans.LoanContentConstants.APPROVAL_DIALOG_PRT_INITIATED_SPOUSAL_CONSENT_REQD_IS_NULL);
				setLoanApprovalAdditionalContentTypeCode(ManagedContent.LOAN_APPROVAL_PARTICIPANT_INITIATED_PLAN_SPOUSAL_CONSENT_NULL);
			} else if (Constants.YES.equals(spousalConstentReqdInd)) {
				setLoanApprovalAdditionalContentKey(com.manulife.pension.ps.web.onlineloans.LoanContentConstants.APPROVAL_DIALOG_PRT_INITIATED_SPOUSAL_CONSENT_REQD_IS_YES);
				setLoanApprovalAdditionalContentTypeCode(ManagedContent.LOAN_APPROVAL_PARTICIPANT_INITIATED_PLAN_SPOUSAL_CONSENT_YES);
			} else if (Constants.NO.equals(spousalConstentReqdInd)) {
				setLoanApprovalAdditionalContentKey(com.manulife.pension.ps.web.onlineloans.LoanContentConstants.APPROVAL_DIALOG_PRT_INITIATED_SPOUSAL_CONSENT_REQD_IS_NO);
				setLoanApprovalAdditionalContentTypeCode(ManagedContent.LOAN_APPROVAL_PARTICIPANT_INITIATED_PLAN_SPOUSAL_CONSENT_NO);
			}
		} else {
			if (spousalConstentReqdInd == null) {
				setLoanApprovalAdditionalContentKey(com.manulife.pension.ps.web.onlineloans.LoanContentConstants.APPROVAL_DIALOG_EXT_INITIATED_SPOUSAL_CONSENT_REQD_IS_NULL);
				setLoanApprovalAdditionalContentTypeCode(ManagedContent.LOAN_APPROVAL_EXT_USER_INITIATED_PLAN_SPOUSAL_CONSENT_NULL);
			} else if (Constants.YES.equals(spousalConstentReqdInd)) {
				setLoanApprovalAdditionalContentKey(com.manulife.pension.ps.web.onlineloans.LoanContentConstants.APPROVAL_DIALOG_EXT_INITIATED_SPOUSAL_CONSENT_REQD_IS_YES);
				setLoanApprovalAdditionalContentTypeCode(ManagedContent.LOAN_APPROVAL_EXT_USER_INITIATED_PLAN_SPOUSAL_CONSENT_YES);
			} else if (Constants.NO.equals(spousalConstentReqdInd)) {
				setLoanApprovalAdditionalContentKey(com.manulife.pension.ps.web.onlineloans.LoanContentConstants.APPROVAL_DIALOG_EXT_INITIATED_SPOUSAL_CONSENT_REQD_IS_NO);
				setLoanApprovalAdditionalContentTypeCode(ManagedContent.LOAN_APPROVAL_EXT_USER_INITIATED_PLAN_SPOUSAL_CONSENT_NO);
			}
		}
		if (loanApprovalAdditionalContentKey != null) {
			ContentText contentHtml = loanDocumentServiceDelegate
					.getContentTextByKey(loanApprovalAdditionalContentKey);
            if (contentHtml != null) {
                setLoanApprovalAdditionalContentId(contentHtml.getId());
            } else {
                throw new SystemException("Unexpected null returned for "
                        + "contentHtml for contentKey = "
                        + loanApprovalAdditionalContentKey);
            }
		}

	}

	public void parseParticipantData(LoanParticipantData participantData) {
		this.firstName = participantData.getFirstName();
		this.lastName = participantData.getLastName();
		this.middleInitial = participantData.getMiddleInitial();
		if (this.legallyMarried == null) {
			/*
			 * We should only reset the legally married indicator if it was
			 * empty.
			 */
			this.legallyMarried = participantData.getLegallMarriedInd();
		}
		this.ssn = participantData.getSsn();
		this.employmentStatusCode = participantData.getEmploymentStatusCode();
	}

	@Override
	public void reset( HttpServletRequest request) {
		super.reset( request);
		ignoreWarning = false;

		/*
		 * The below correspond to check box values and need to be set to false
		 * (unchecked) since only the checked values will be passed back from
		 * the page
		 */
		setApplyIrs10KDollarRule(false);
		moneyTypeExcludedInds.clear();
		truthInLendingNotice.setAccepted(false);
		promissoryNote.setAccepted(false);
		participantDeclaration.setAccepted(false);
		atRiskTransaction.setAccepted(false);
	}

	public void parseContract(LoanPlanData loanPlanData) {
		this.contractName = loanPlanData.getContractName();
	}

	public void clear() {
		contractId = null;
		submissionId = null;
		participantProfileId = null;
		legallyMarried = null;
		participantId = null;
		status = null;
		firstName = null;
		lastName = null;
		employmentStatusCode = null;
		contractName = null;
		ssn = null;
		loanType = null;
		loanReason = null;
		requestDate = null;
		expirationDate = null;
		startDate = null;
		payrollDate = null;
		tpaLoanFee = null;
		defaultProvision = null;
		maturityDate = null;
		maximumAmortizationPeriodYears = null;
		maximumLoanAvailable = null;
		applyIrs10KDollarRule = false;
		loanAmount = null;
        loanAmountOriginalValue = null;
		paymentAmount = null;
		paymentFrequency = null;
		amortizationPeriodYears = null;
		amortizationPeriodMonths = null;
		interestRate = null;
		moneyTypeVestingPercentages.clear();
		moneyTypeExcludedInds.clear();
		maxBalanceLast12Months = null;
		outstandingLoansCount = null;
		currentOutstandingBalance = null;
		abaRoutingNumber = null;
		bankName = null;
		accountType = null;
		accountNumber = null;
		currentParticipantNote = null;
		currentAdministratorNote = null;
		showConfirmation = false;
		truthInLendingNotice = new NotificationAndDeclaration();
		promissoryNote = new NotificationAndDeclaration();
		amortizationSchedule = new NotificationAndDeclaration();
		participantDeclaration = new NotificationAndDeclaration();
		atRiskTransaction = new NotificationAndDeclaration();
		atRiskTransactionInd = false;
		loanApprovalGenericContentId = null;
		loanApprovalAdditionalContentKey = null;
		loanApprovalAdditionalContentId = null;
		loanApprovalAdditionalContentTypeCode = null;
		ignoreWarning = false;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getLoanReason() {
		return loanReason;
	}

	public void setLoanReason(String loanReason) {
		this.loanReason = loanReason;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expiryDate) {
		this.expirationDate = expiryDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getPayrollDate() {
		return payrollDate;
	}

	public void setPayrollDate(String payrollDate) {
		this.payrollDate = payrollDate;
	}

	public String getTpaLoanFee() {
		return tpaLoanFee;
	}

	public void setTpaLoanFee(String tpaLoanFee) {
		this.tpaLoanFee = tpaLoanFee;
	}

	public String getDefaultProvision() {
		return defaultProvision;
	}

	public void setDefaultProvision(String defaultProvision) {
		this.defaultProvision = defaultProvision;
	}

	public String getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}

	public Integer getMaximumAmortizationPeriodYears() {
		return maximumAmortizationPeriodYears;
	}

	public void setMaximumAmortizationPeriodYears(Integer amortizationPeriod) {
		this.maximumAmortizationPeriodYears = amortizationPeriod;
	}

	public String getMaximumLoanAvailable() {
		return maximumLoanAvailable;
	}

	public void setMaximumLoanAvailable(String maximumLoanAvailable) {
		this.maximumLoanAvailable = maximumLoanAvailable;
	}

	public String getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getPaymentFrequency() {
		return paymentFrequency;
	}

	public void setPaymentFrequency(String paymentFrequency) {
		this.paymentFrequency = paymentFrequency;
	}

	public Integer getAmortizationMonths() {
		if (amortizationPeriodYears != null && amortizationPeriodMonths != null) {
			return amortizationPeriodYears * 12 + amortizationPeriodMonths;
		} else {
			return null;
		}
	}

	public void setAmortizationMonths(Integer amortizationMonths) {
		if (amortizationMonths != null) {
			amortizationPeriodYears = amortizationMonths / 12;
			amortizationPeriodMonths = amortizationMonths % 12;
		} else {
			amortizationPeriodYears = null;
			amortizationPeriodMonths = null;
		}
	}

	public Integer getAmortizationPeriodYears() {
		return amortizationPeriodYears;
	}

	public void setAmortizationPeriodYears(Integer amortizationPeriodYears) {
		this.amortizationPeriodYears = amortizationPeriodYears;
		Integer planMaximumAmortizationPeriodYears = getMaximumAmortizationYearsMap()
				.get(loanType);
		if (amortizationPeriodYears != null && planMaximumAmortizationPeriodYears!= null
				&& amortizationPeriodYears
						.compareTo(planMaximumAmortizationPeriodYears) == 0) {
			amortizationPeriodMonths = 0;
		}
	}

	public Integer getAmortizationPeriodMonths() {
		return amortizationPeriodMonths;
	}

	public void setAmortizationPeriodMonths(Integer amortizationPeriodMonths) {
		this.amortizationPeriodMonths = amortizationPeriodMonths;
	}

	public String getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getMoneyTypeVestingPercentage(String moneyTypeId) {
		return moneyTypeVestingPercentages.get(moneyTypeId);
	}

	public void setMoneyTypeVestingPercentage(String moneyTypeId,
			String percentage) {
		this.moneyTypeVestingPercentages.put(moneyTypeId, percentage);
	}

	public Map<String, String> getMoneyTypeVestingPercentages() {
		return moneyTypeVestingPercentages;
	}

	public void setMoneyTypeVestingPercentages(
			Map<String, String> moneyTypeVestingPercentages) {
		this.moneyTypeVestingPercentages = moneyTypeVestingPercentages;
	}

	public Map<String, Boolean> getMoneyTypeExcludedInds() {
		return moneyTypeExcludedInds;
	}

	public void setMoneyTypeExcludedInds(
			Map<String, Boolean> moneyTypeExcludedInds) {
		this.moneyTypeExcludedInds = moneyTypeExcludedInds;
	}

	public boolean getMoneyTypeExcludedInd(String moneyTypeId) {
		Boolean result = moneyTypeExcludedInds.get(moneyTypeId);
		return result == null ? false : result;
	}

	public void setMoneyTypeExcludedInd(String moneyTypeId, boolean excludedInd) {
		this.moneyTypeExcludedInds.put(moneyTypeId, excludedInd);
	}

	public String getMaxBalanceLast12Months() {
		return maxBalanceLast12Months;
	}

	public void setMaxBalanceLast12Months(String maxBalanceLast12Months) {
		this.maxBalanceLast12Months = maxBalanceLast12Months;
	}

	public String getOutstandingLoansCount() {
		return outstandingLoansCount;
	}

	public void setOutstandingLoansCount(String outstandingLoansCount) {
		this.outstandingLoansCount = outstandingLoansCount;
	}

	public String getCurrentOutstandingBalance() {
		return currentOutstandingBalance;
	}

	public void setCurrentOutstandingBalance(String currentOutstandingBalance) {
		this.currentOutstandingBalance = currentOutstandingBalance;
	}

	public String getCurrentParticipantNote() {
		return currentParticipantNote;
	}

	public void setCurrentParticipantNote(String currentParticipantNote) {
		this.currentParticipantNote = currentParticipantNote;
	}

	public String getCurrentAdministratorNote() {
		return currentAdministratorNote;
	}

	public void setCurrentAdministratorNote(String currentAdministratorNote) {
		this.currentAdministratorNote = currentAdministratorNote;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public AddressStateCode getStateCode() {
		return stateCode;
	}

	public void setStateCode(AddressStateCode stateCode) {
		this.stateCode = stateCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public ZipCode getZipCode() {
		return zipCode;
	}

	public void setZipCode(ZipCode zipCode) {
		this.zipCode = zipCode;
	}

	public String getAbaRoutingNumber() {
		return abaRoutingNumber;
	}

	public void setAbaRoutingNumber(String abaRoutingNumber) {
		this.abaRoutingNumber = abaRoutingNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Map<String, Object> getLookupData() {
		return lookupData;
	}

	public void setLookupData(Map<String, Object> lookupData) {
		this.lookupData = lookupData;
	}

	public boolean isCountryUSA() {
		// USA will be shown if country is empty
		if (StringUtils.isBlank(country)) {
			return true;
		}

		if (country.equals("USA")) {
			return true;
		}

		try {
			EnvironmentServiceDelegate environmentServiceDelegate = EnvironmentServiceDelegate
					.getInstance(Constants.PS_APPLICATION_ID);
			Map<String, String> countries = environmentServiceDelegate
					.getCountries();
			// if it is an invalid country, it becomes USA in UI
			if (!countries.containsKey(country)) {
				return true;
			}
		} catch (SystemException e) {
			/* cannot retrieve country list.. assume it's an invalid country */
			return true;
		}
		return false;
	}

	public Timestamp getLastUpdatedTs() {
		return lastUpdatedTs;
	}

	public void setLastUpdatedTs(Timestamp lastUpdatedTs) {
		this.lastUpdatedTs = lastUpdatedTs;
	}

	public boolean isShowConfirmation() {
		return showConfirmation;
	}

	public void setShowConfirmation(boolean showConfirmation) {
		this.showConfirmation = showConfirmation;
	}

	private boolean isPaymentInstructionSuppressed() {
		return LoanStateEnum.PENDING_REVIEW.getStatusCode().equals(status)
				&& LoanConstants.USER_ROLE_PARTICIPANT_CODE
						.equals(createdByRoleCode);
	}

	public Map<String, Integer> getMaximumAmortizationYearsMap() {
		return maximumAmortizationYearsMap;
	}

	public void setMaximumAmortizationYearsMap(
			Map<String, Integer> maximumAmortizationYearsMap) {
		this.maximumAmortizationYearsMap = maximumAmortizationYearsMap;
	}

	public NotificationAndDeclaration getTruthInLendingNotice() {
		return truthInLendingNotice;
	}

	public void setTruthInLendingNotice(
			NotificationAndDeclaration truthInLendingNotice) {
		this.truthInLendingNotice = truthInLendingNotice;
	}

	public NotificationAndDeclaration getPromissoryNote() {
		return promissoryNote;
	}

	public void setPromissoryNote(NotificationAndDeclaration promissoryNote) {
		this.promissoryNote = promissoryNote;
	}

	public NotificationAndDeclaration getAmortizationSchedule() {
		return amortizationSchedule;
	}

	public void setAmortizationSchedule(
			NotificationAndDeclaration amortizationSchedule) {
		this.amortizationSchedule = amortizationSchedule;
	}

	public NotificationAndDeclaration getParticipantDeclaration() {
		return participantDeclaration;
	}

	public void setParticipantDeclaration(
			NotificationAndDeclaration participantDeclaration) {
		this.participantDeclaration = participantDeclaration;
	}

	public NotificationAndDeclaration getAtRiskTransaction() {
		return atRiskTransaction;
	}

	public void setAtRiskTransaction(
			NotificationAndDeclaration atRiskTransaction) {
		this.atRiskTransaction = atRiskTransaction;
	}

	public Integer getLoanApprovalAdditionalContentKey() {
		return loanApprovalAdditionalContentKey;
	}

	public void setLoanApprovalAdditionalContentKey(
			Integer loanApprovalAdditionalContentKey) {
		this.loanApprovalAdditionalContentKey = loanApprovalAdditionalContentKey;
	}

	public Integer getLoanApprovalGenericContentId() {
		return loanApprovalGenericContentId;
	}

	public void setLoanApprovalGenericContentId(
			Integer loanApprovalGenericContentId) {
		this.loanApprovalGenericContentId = loanApprovalGenericContentId;
	}

	public Integer getLoanApprovalAdditionalContentId() {
		return loanApprovalAdditionalContentId;
	}

	public void setLoanApprovalAdditionalContentId(
			Integer loanApprovalAdditionalContentId) {
		this.loanApprovalAdditionalContentId = loanApprovalAdditionalContentId;
	}

	public String getLoanApprovalAdditionalContentTypeCode() {
		return loanApprovalAdditionalContentTypeCode;
	}

	public void setLoanApprovalAdditionalContentTypeCode(
			String loanApprovalAdditionalContentTypeCode) {
		this.loanApprovalAdditionalContentTypeCode = loanApprovalAdditionalContentTypeCode;
	}

	public NotificationAndDeclaration getLoanPackageInstructionsForParticipant() {
		return loanPackageInstructionsForParticipant;
	}

	public void setLoanPackageInstructionsForParticipant(
			NotificationAndDeclaration loanPackageInstructionsForParticipant) {
		this.loanPackageInstructionsForParticipant = loanPackageInstructionsForParticipant;
	}

	public NotificationAndDeclaration getLoanPackageInstructionsForPlanAdministrator() {
		return loanPackageInstructionsForPlanAdministrator;
	}

	public void setLoanPackageInstructionsForPlanAdministrator(
			NotificationAndDeclaration loanPackageInstructionsForPlanAdministrator) {
		this.loanPackageInstructionsForPlanAdministrator = loanPackageInstructionsForPlanAdministrator;
	}

	public NotificationAndDeclaration getLoanFormInstructions() {
		return loanFormInstructions;
	}

	public void setLoanFormInstructions(
			NotificationAndDeclaration loanFormInstructions) {
		this.loanFormInstructions = loanFormInstructions;
	}

	public NotificationAndDeclaration getLoanForm() {
		return loanForm;
	}

	public void setLoanForm(NotificationAndDeclaration loanForm) {
		this.loanForm = loanForm;
	}

	public boolean getTruthInLendingNoticeAcceptedOriginalValue() {
		return truthInLendingNoticeAcceptedOriginalValue;
	}

	public void setTruthInLendingNoticeAcceptedOriginalValue(
			boolean truthInLendingNoticeAcceptedOriginalValue) {
		this.truthInLendingNoticeAcceptedOriginalValue = truthInLendingNoticeAcceptedOriginalValue;
	}

	public boolean getPromissoryNoteAcceptedOriginalValue() {
		return promissoryNoteAcceptedOriginalValue;
	}

	public void setPromissoryNoteAcceptedOriginalValue(
			boolean promissoryNoteAcceptedOriginalValue) {
		this.promissoryNoteAcceptedOriginalValue = promissoryNoteAcceptedOriginalValue;
	}

	public boolean getParticipantDeclarationAcceptedOriginalValue() {
		return participantDeclarationAcceptedOriginalValue;
	}

	public void setParticipantDeclarationAcceptedOriginalValue(
			boolean participantDeclarationAcceptedOriginalValue) {
		this.participantDeclarationAcceptedOriginalValue = participantDeclarationAcceptedOriginalValue;
	}

	public boolean getAtRiskTransactionAcceptedOriginalValue() {
		return atRiskTransactionAcceptedOriginalValue;
	}

	public void setAtRiskTransactionAcceptedOriginalValue(
			boolean atRiskTransactionAcceptedOriginalValue) {
		this.atRiskTransactionAcceptedOriginalValue = atRiskTransactionAcceptedOriginalValue;
	}

    public String getLoanAmountOriginalValue() {
        return loanAmountOriginalValue;
    }

    public void setLoanAmountOriginalValue(String loanAmountOriginalValue) {
        this.loanAmountOriginalValue = loanAmountOriginalValue;
    }

	public boolean isApplyIrs10KDollarRule() {
		return applyIrs10KDollarRule;
	}

	public void setApplyIrs10KDollarRule(boolean applyIrs10KDollarRule) {
		this.applyIrs10KDollarRule = applyIrs10KDollarRule;
	}

	public boolean isDisplayIRSlabel() {
		return displayIRSlabel;
	}

	public void setDisplayIRSlabel(boolean displayIRSlabel) {
		this.displayIRSlabel = displayIRSlabel;
	}

	/**
	 * @return the pinAddressAtRisk
	 */
	public boolean getAtRiskTransactionInd() {
		return atRiskTransactionInd;
	}

	/**
	 * @param pinAddressAtRisk the pinAddressAtRisk to set
	 */
	public void setAtRiskTransactionInd(boolean atRiskTransactionInd) {
		this.atRiskTransactionInd = atRiskTransactionInd;
	}
	
	
}
