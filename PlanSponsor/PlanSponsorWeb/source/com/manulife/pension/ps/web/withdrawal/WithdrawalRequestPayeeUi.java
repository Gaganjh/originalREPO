package com.manulife.pension.ps.web.withdrawal;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.util.CalendarUtils;
import com.manulife.pension.validator.ValidationError;

/**
 * WithdrawalRequestPayeeUi provides String fields for non-String fields in the
 * {@link WithdrawalRequestPayee} object. To access String fields, just access the
 * {@link WithdrawalRequestPayee} object directly, as it's a field of this object.
 * 
 * @author Andrew Dick
 */
public class WithdrawalRequestPayeeUi extends BaseWithdrawalUiObject {

    /**
     * Payment To Display for after tax contribution remainder to plan - payee 1.
     */
    public static final String DISPLAY_PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_1 = "Taxable portion to other plan";

    /**
     * Payment To Display for after tax contribution remainder to plan - payee 1.
     */
    public static final String DISPLAY_PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_1 = "Taxable portion to IRA";

    /**
     * Payment To Display for both after tax contribution codes - payee 2.
     */
    public static final String DISPLAY_PAYMENT_TO_AFTER_TAX_CONTRIBUTION_PAYEE_2 = "Participant directly";

    /**
     * Payment To Display for rollover to IRA.
     */
    public static final String DISPLAY_PAYMENT_TO_ROLLOVER_TO_IRA = "Rollover all to IRA";

    /**
     * Payment To Display for rollover to IRA.
     */
    public static final String DISPLAY_PAYMENT_TO_ROLLOVER_TO_JH_IRA = "Rollover all to John Hancock IRA";

    /**
     * Payment To Display for rollover to plan.
     */
    public static final String DISPLAY_PAYMENT_TO_ROLLOVER_TO_PLAN = "Rollover all to other plan";

    /**
     * Payment To Display for plan trustee.
     */
    public static final String DISPLAY_PAYMENT_TO_PLAN_TRUSTEE = "Plan Trustee";
    /**
     * Payment To Display for Multiple Destination
     */
    public static final String DISPLAY_PAYMENT_TO_MULTIPLE_DESTINATION = "Multiple Destination";
    
    public static final String DISPLAY_TRADITIONAL_IRA = "Traditional IRA";
    
    public static final String DISPLAY_ROTH_IRA = "Roth IRA";
    
    public static final String DISPLAY_EMP_PLAN = "Employer Sponsored Qualified Plan";
    
    /**
     * Payment To Display for participant.
     */
    public static final String DISPLAY_PAYMENT_TO_PARTICIPANT = "Participant directly";

    /**
     * Length of bank transit field.
     */
    public static final int BANK_TRANSIT_FIELD_LENGTH = 9;

    /**
     * Padding for bank transit field.
     */
    public static final String BANK_TRANSIT_FIELD_PADDING = "0";

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private WithdrawalRequestPayee withdrawalRequestPayee;

    private transient WithdrawalRequestRecipientUi parent;

    private static final String VO_BEAN_NAME = "withdrawalRequestPayee";

    private static final String[] UI_FIELDS = { "recipientNo", "occurrenceNo", "shareValue",
            "bankTransitNumber" };

    private static final String BANK_TRANSIT_PROPERTY = "bankTransitNumber";

    private static final String ROLLOVER_FI_SUB_SUB_HEADER = "Rollover financial institution";

    private static final String BANK_SUB_SUB_HEADER = "Payee";

    private static final String PARTICIPANT_SUB_SUB_HEADER = "Participant";

    private static final String PLAN_TRUSTEE_SUB_SUB_HEADER = "Trustee";
    
    private static final String NAME_SUBSECTION_NAME = "Name";
    
    private static final String NAME_SUBSECTION_PROVIDER_NAME = "Provider Name";
    
  

    // These are the non-String fields from the WithdrawalRequestPayee
    // class.
    private String recipientNo;

    private String occurrenceNo;

    private String shareValue;

    // Non-string fields from Payment Instruction
    private String bankTransitNumber;

    private AddressUi addressUi;

    // Dummy fields to allow manipulation of checkboxes on wizard page

    // Separate the organization name into check and EFT
    private String eftOrganizationName;

    private String checkOrganizationName;
    
    
    private String eftRolloverType;

    private String checkRolloverType;
    
    // attributes for PDF
    private boolean isFirstRolloverPayee;
    private String subSectionNameColumn;
    private String paymentToDisplay;
    private boolean showBankAccountNumber;
    private boolean showAccountNumber;
    private boolean showTrusteeForRollover;

    /**
     * Default Constructor.
     * 
     * @param withdrawalRequestPayee The bean to create the data with.
     * @param parent The parent recipient object.
     */
    public WithdrawalRequestPayeeUi(final WithdrawalRequestPayee withdrawalRequestPayee,
            final WithdrawalRequestRecipientUi parent) {
        super(UI_FIELDS, VO_BEAN_NAME);

        setWithdrawalRequestPayee(withdrawalRequestPayee);
        this.parent = parent;

        convertFromBean();
    }

    /**
     * Converts the matching fields from the {@link WithdrawalRequestPayee} bean, to this object.
     */
    public final void convertFromBean() {
        try {
            BeanUtils.copyProperties(this, withdrawalRequestPayee);
            BeanUtils.copyProperty(this, BANK_TRANSIT_PROPERTY, withdrawalRequestPayee
                    .getPaymentInstruction().getBankTransitNumber());
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        } // end try/catch

        // Pad the bank transit number to it's full length.
        bankTransitNumber = StringUtils.leftPad(bankTransitNumber, BANK_TRANSIT_FIELD_LENGTH,
                BANK_TRANSIT_FIELD_PADDING);

        setCheckOrganizationName(getWithdrawalRequestPayee().getOrganizationName());
        
        // Security Enhancements  
        setEftOrganizationName(getWithdrawalRequestPayee().getOrganizationName());
        
        // Determine which organization name is being used
        final String paymentMethod = getWithdrawalRequestPayee().getPaymentMethodCode();
        if (StringUtils.equals(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, paymentMethod)) {
            setCheckOrganizationName(getWithdrawalRequestPayee().getOrganizationName());
            setCheckRolloverType(getWithdrawalRequestPayee().getRolloverType());

        } else if (StringUtils
                .equals(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, paymentMethod)
                || StringUtils.equals(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE,
                        paymentMethod)) {

            setEftOrganizationName(getWithdrawalRequestPayee().getOrganizationName());
            setEftRolloverType(getWithdrawalRequestPayee().getRolloverType());
        }

        this.addressUi = new AddressUi((Address)withdrawalRequestPayee.getAddress());
    }

    /**
     * Converts the matching fields from this object, to the {@link WithdrawalRequestPayee} bean.
     */
    public final void convertToBean() {
        try {
            BeanUtils.copyProperties(withdrawalRequestPayee, this);
            BeanUtils.copyProperty(withdrawalRequestPayee.getPaymentInstruction(),
                    BANK_TRANSIT_PROPERTY, getBankTransitNumber());
            // set the typeCode
            withdrawalRequestPayee.setTypeCode(getPaymentTypeCode());
        } catch (IllegalAccessException e) {
            throw new NestableRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NestableRuntimeException(e);
        }
        getWithdrawalRequestPayee().setOrganizationName(getCheckOrganizationName());
        getWithdrawalRequestPayee().setRolloverType(getCheckRolloverType());
        // Determine which organization name is being used
        final String paymentMethod = getWithdrawalRequestPayee().getPaymentMethodCode();
        if (StringUtils.equals(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, paymentMethod)) {
            getWithdrawalRequestPayee().setOrganizationName(getCheckOrganizationName());
            getWithdrawalRequestPayee().setRolloverType(getCheckRolloverType());

        } else if (StringUtils
                .equals(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, paymentMethod)
                || StringUtils.equals(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE,
                        paymentMethod)) {

            getWithdrawalRequestPayee().setOrganizationName(getEftOrganizationName());
            getWithdrawalRequestPayee().setRolloverType(getEftRolloverType());
            // Security Enhancements - default credit party name to be same as EFT organization name for ACH and WIRE
            getWithdrawalRequestPayee().getPaymentInstruction().setCreditPartyName(getEftOrganizationName());
        }
   
    }

    /**
     * @return the occurrenceNo
     */
    public String getOccurrenceNo() {
        return occurrenceNo;
    }

    /**
     * @param occurrenceNo the occurrenceNo to set
     */
    public void setOccurrenceNo(final String occurrenceNo) {
        this.occurrenceNo = occurrenceNo;
    }

    /**
     * @return the recipientNo
     */
    public String getRecipientNo() {
        return recipientNo;
    }

    /**
     * @param recipientNo the recipientNo to set
     */
    public void setRecipientNo(final String recipientNo) {
        this.recipientNo = recipientNo;
    }

    /**
     * @return the shareValue
     */
    public String getShareValue() {
        return shareValue;
    }

    /**
     * @param shareValue the shareValue to set
     */
    public void setShareValue(final String shareValue) {
        this.shareValue = shareValue;
    }

    /**
     * @return the withdrawalRequestPayee
     */
    public WithdrawalRequestPayee getWithdrawalRequestPayee() {
        return withdrawalRequestPayee;
    }

    /**
     * @param withdrawalRequestPayee the withdrawalRequestPayee to set
     */
    public void setWithdrawalRequestPayee(final WithdrawalRequestPayee withdrawalRequestPayee) {
        this.withdrawalRequestPayee = withdrawalRequestPayee;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<ValidationError> getValidationMessages(final GraphLocation graphLocation) {
        final Collection<ValidationError> messages = new ArrayList<ValidationError>();

        messages.addAll(getValidationMessages(graphLocation, getWithdrawalRequestPayee()));

        // Get the address messages.
        messages.addAll(addressUi.getValidationMessages(new GraphLocation(graphLocation,
                "addressUi")));

        return messages;
    }

    /**
     * @return the parent
     */
    public WithdrawalRequestRecipientUi getParent() {
        return parent;
    }

    /**
     * @return the paymentToDisplay
     */
    public String getPaymentToDisplay() {

        // Get payment to field
        final String paymentTo = getParent().getParent().getWithdrawalRequest().getPaymentTo();
       
        // Check if set
        if (StringUtils.isBlank(paymentTo)) {
            return StringUtils.EMPTY;
        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE,
                paymentTo)) {
            return DISPLAY_PAYMENT_TO_PARTICIPANT;
        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, paymentTo)) {
            return DISPLAY_PAYMENT_TO_ROLLOVER_TO_IRA;
        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_JH_IRA_CODE,
                paymentTo)) {
            return DISPLAY_PAYMENT_TO_ROLLOVER_TO_JH_IRA;
        } else if (StringUtils
                .equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE, paymentTo)) {
            return DISPLAY_PAYMENT_TO_ROLLOVER_TO_PLAN;
        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, paymentTo)) {
            return DISPLAY_PAYMENT_TO_PLAN_TRUSTEE;
        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION, paymentTo)) {
        	String paymentToDisplayValue="";
        	if(this.getWithdrawalRequestPayee().getParticipant()!=null){
        		if(this.getWithdrawalRequestPayee().getParticipant().indexOf(DISPLAY_ROTH_IRA) !=-1 ){
        			paymentToDisplayValue = DISPLAY_ROTH_IRA;
        		}else if(this.getWithdrawalRequestPayee().getParticipant().indexOf(DISPLAY_TRADITIONAL_IRA) !=-1 ){
        			paymentToDisplayValue =  DISPLAY_TRADITIONAL_IRA;
        		}else if (this.getWithdrawalRequestPayee().getParticipant().indexOf(DISPLAY_EMP_PLAN) !=-1 ){
        			paymentToDisplayValue =  DISPLAY_EMP_PLAN;
        		}else{
        			paymentToDisplayValue =  DISPLAY_PAYMENT_TO_PARTICIPANT; 
        		}
        	}
        	return paymentToDisplayValue ;
        }else if (StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE,
                paymentTo)) {

            // Double payees use display versions
            if (isFirstPayee()) {
                return DISPLAY_PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_PAYEE_1;
            } else if (isSecondPayee()) {
                return DISPLAY_PAYMENT_TO_AFTER_TAX_CONTRIBUTION_PAYEE_2;
            } else {
                return StringUtils.EMPTY;
            }

        } else if (StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                paymentTo)) {

            // Double payees use display versions
            if (isFirstPayee()) {
                return DISPLAY_PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_PAYEE_1;
            } else if (isSecondPayee()) {
                return DISPLAY_PAYMENT_TO_AFTER_TAX_CONTRIBUTION_PAYEE_2;
            } else {
                return StringUtils.EMPTY;
            }

        } else {
            return StringUtils.EMPTY;
        }
    }
    
    private void setPaymentToDisplay() {
    	paymentToDisplay = getPaymentToDisplay();
    }

    /**
     * @return the payment type code (to be saved in the payee table) based on the paymentTo field
     */
    public String getPaymentTypeCode() {
        // Get payment to field
        final String paymentTo = getParent().getParent().getWithdrawalRequest().getPaymentTo();
        String typeCode = StringUtils.EMPTY;

        if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE, paymentTo)) {
            typeCode = WithdrawalRequestPayee.TYPE_CODE_PARTICIPANT;
        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, paymentTo)
                || StringUtils
                        .equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE, paymentTo)
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_JH_IRA_CODE,
                        paymentTo)) {
            typeCode = WithdrawalRequestPayee.TYPE_CODE_FINANCIAL_INSTITUTION;
        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, paymentTo)) {
            typeCode = WithdrawalRequestPayee.TYPE_CODE_TRUSTEE;
            // multiple payees
        } else if (StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE,
                paymentTo)
                || StringUtils.equals(
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                        paymentTo) ) {
            if (isFirstPayee()) {
                typeCode = WithdrawalRequestPayee.TYPE_CODE_FINANCIAL_INSTITUTION;
            } else if (isSecondPayee()) {
                typeCode = WithdrawalRequestPayee.TYPE_CODE_PARTICIPANT;
            } else {
                typeCode = StringUtils.EMPTY;
            }
        }
        	else if (StringUtils.equals(paymentTo,
                    WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
            	if(getWithdrawalRequestPayee().getParticipant()!=null && 
            			getWithdrawalRequestPayee().getParticipant().indexOf("Participant") !=-1 ){
            		typeCode = WithdrawalRequestPayee.TYPE_CODE_PARTICIPANT ;
            	}else{
            		typeCode = WithdrawalRequestPayee.TYPE_CODE_FINANCIAL_INSTITUTION;
            	}
            	
            }
        	
        
        return typeCode;
    }

    /**
     * Determines whether the check payee name should be editable or not. Currently the check payee
     * name is editable if:
     * <ul>
     * <li> Payment To is Rollover to IRA.
     * <li> Payment To is Rollover to plan.
     * <li> Payment To is After-tax contributions direct to participant, remainder to IRA (payee 1
     * only).
     * <li> Payment To is After-tax contributions direct to participant, remainder to plan (payee 1
     * only).
     * </ul>
     * 
     * @return the checkPayeeNameEditable
     */
    public boolean isCheckPayeeNameEditable() {

        return getWithdrawalRequestPayee().isCheckPayeeNameEditable(
                getParent().getParent().getWithdrawalRequest());
    }

    /**
     * @return the bankTransitNumber
     */
    public String getBankTransitNumber() {
        return bankTransitNumber;
    }

    /**
     * @param bankTransitNumber the bankTransitNumber to set
     */
    public void setBankTransitNumber(final String bankTransitNumber) {
        this.bankTransitNumber = bankTransitNumber;
    }

    /**
     * Queries if the bank account number row is shown.
     */
    public boolean getShowBankAccountNumber() {
        return getWithdrawalRequestPayee().getShowBankAccountNumber();
    }

    private void setShowBankAccountNumber(){
    	showBankAccountNumber = getShowBankAccountNumber();
    }
    /**
     * Queries if the bank account number for rollover row is shown.
     */
    public boolean getShowAccountNumber() {
        final WithdrawalRequest withdrawalRequest = getParent().getParent().getWithdrawalRequest();

        return getWithdrawalRequestPayee().getShowAccountNumber(withdrawalRequest);
    }
    
    private void setShowAccountNumber() {
    	showAccountNumber = getShowAccountNumber();
    }

    /**
     * Queries if the payee is the first payee in the recipient collection.
     */
    public boolean isFirstPayee() {

        return (this == getParent().getPayees().iterator().next());
    }

    /**
     * Queries if the payee is the second payee in the recipient collection.
     */
    public boolean isSecondPayee() {

        final Collection<WithdrawalRequestPayeeUi> collection = getParent().getPayees();
        if (collection.size() > 1) {
            final Iterator<WithdrawalRequestPayeeUi> iterator = collection.iterator();
            iterator.next();
            return (this == iterator.next());
        }

        return false;
    }
    
 
    /**
     * Determines if the Trustee of _(name of plan)_ for rollover is shown.
     */
    public boolean getShowTrusteeForRollover() {
        final WithdrawalRequest withdrawalRequest = getParent().getParent().getWithdrawalRequest();

        return getWithdrawalRequestPayee().getShowTrusteeForRollover(withdrawalRequest);
    }
    
    private void setShowTrusteeForRollover() {
    	showTrusteeForRollover = getShowTrusteeForRollover();
    }

    /**
     * Queries if the default address should be used for check payees.
     */
    public boolean getUseDefaultAddressForCheckPayee() {

        boolean result = false;
        result |= StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, getParent()
                .getParent().getWithdrawalRequest().getPaymentTo());
        result |= StringUtils.equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE,
                getParent().getParent().getWithdrawalRequest().getPaymentTo());
        result |= (StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION,
                getParent().getParent().getWithdrawalRequest().getPaymentTo()) && isSecondPayee());
        result |= (StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                getParent().getParent().getWithdrawalRequest().getPaymentTo()) && isSecondPayee());
        return result;
    }

    /**
     * Queries if the default address should be used for eft payees.
     */
    public boolean getUseDefaultAddressForEftPayee() {
        return StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, getParent()
                .getParent().getWithdrawalRequest().getPaymentTo());
    }

    /**
     * Sets the default value for the amount type field.
     */
    public void setDefaultIrsDistributionCode() {

        // Do not set default if already a value
        if (StringUtils.isBlank(getWithdrawalRequestPayee().getIrsDistCode())) {

            final WithdrawalRequest request = getParent().getParent().getWithdrawalRequest();

            // Default is blank if no birth date available
            if (request.getBirthDate() != null) {

                // Default is blank if simple withdrawal reason
                if (!request.getWithdrawalReasonSimple()) {

                    if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, request
                            .getPaymentTo())) {

                        // Set to Select
                    	// Check age of participant
                        if (CalendarUtils.isAgeLessThanFiftyNineAndAHalf(request.getBirthDate())) {

                            // Set to early distribution
                            getWithdrawalRequestPayee()
                                    .setIrsDistCode(
                                            WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
                        } else {

                            // Set to normal distribution
                            getWithdrawalRequestPayee()
                                    .setIrsDistCode(
                                            WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
                        }

                    } else if (StringUtils.equals(
                            WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, request
                                    .getPaymentTo())
                            || StringUtils.equals(
                                    WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE, request
                                            .getPaymentTo())) {

                        // Set to Rollover
                        getWithdrawalRequestPayee().setIrsDistCode(
                                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);

                    } else if (StringUtils.equals(
                            WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE, request
                                    .getPaymentTo())) {

                        // Check age of participant
                        if (CalendarUtils.isAgeLessThanFiftyNineAndAHalf(request.getBirthDate())) {

                            // Set to early distribution
                            getWithdrawalRequestPayee()
                                    .setIrsDistCode(
                                            WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
                        } else {

                            // Set to normal distribution
                            getWithdrawalRequestPayee()
                                    .setIrsDistCode(
                                            WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
                        }

                    }// Added for Multiple Destination in Payment to 
                    else if (StringUtils.equals(
                            WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION, request
                            .getPaymentTo())) {

                // Check age of participant
                if (CalendarUtils.isAgeLessThanFiftyNineAndAHalf(request.getBirthDate())) {

                    // Set to early distribution
                    getWithdrawalRequestPayee()
                            .setIrsDistCode(
                                    WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
                } else {

                    // Set to normal distribution
                    getWithdrawalRequestPayee()
                            .setIrsDistCode(
                                    WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
                }
                

            }  else if (StringUtils
                            .equals(
                                    WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE,
                                    request.getPaymentTo())
                            || StringUtils
                                    .equals(
                                            WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                                            request.getPaymentTo())) {

                        // Determine which payee we are
                        if (isFirstPayee()) {

                            // Set to Rollover
                            getWithdrawalRequestPayee().setIrsDistCode(
                                    WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_ROLLOVER);

                        } else if (isSecondPayee()) {

                            // Check age of participant
                            if (CalendarUtils
                                    .isAgeLessThanFiftyNineAndAHalf(request.getBirthDate())) {

                                // Set to early distribution
                                getWithdrawalRequestPayee()
                                        .setIrsDistCode(
                                                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_EARLY_DISTRIBUTION_UNDER_59_5);
                            } else {

                                // Set to normal distribution
                                getWithdrawalRequestPayee()
                                        .setIrsDistCode(
                                                WithdrawalRequestPayee.IRS_DISTRIBUTION_CODE_NORMAL_DISTRIBUTION_OVER_59_5);
                            }

                        } else {

                            throw new RuntimeException(new StringBuffer(
                                    "Unexpected number of payees [").append(
                                    getParent().getPayees().size()).append(
                                    "] for withdrawal request [").append(request).append("].")
                                    .toString());
                        }

                    } else {

                        throw new RuntimeException(new StringBuffer("Unexpected Payment To [")
                                .append(request.getPaymentTo())
                                .append("] for withdrawal request [").append(request).append("].")
                                .toString());
                    }
                } else {
                    // Set to Select
                    getWithdrawalRequestPayee().setIrsDistCode(StringUtils.EMPTY);
                }
            } else {
                // Set to Select
                getWithdrawalRequestPayee().setIrsDistCode(StringUtils.EMPTY);
            }
        }
    }

    /**
     * Queries if this is the first payee for a Rollover payment.
     */
    public boolean getIsFirstRolloverPayee() {
        final String paymentTo = getParent().getParent().getWithdrawalRequest().getPaymentTo();
        
        if(StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION) && getWithdrawalRequestPayee().getParticipant()!=null && 
        		getWithdrawalRequestPayee().getParticipant().indexOf("Participant") !=-1 ){
    		return false;
   		}
        else return isFirstPayee()
                && (StringUtils
                        .equals(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE)
                        || StringUtils.equals(paymentTo,
                                WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE)
                        || StringUtils
                                .equals(
                                        paymentTo,
                                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE)
                        || StringUtils
                                .equals(
                                        paymentTo,
                                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE) || StringUtils
                        .equals(paymentTo, WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_JH_IRA_CODE)  || StringUtils.equals(paymentTo, WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION) );
    }

    private void setIsFirstRolloverPayee() {
    	isFirstRolloverPayee = getIsFirstRolloverPayee();
    }
    
    public String getEftPayeeSubSubHeader() {

        final String paymentTo = getParent().getParent().getWithdrawalRequest().getPaymentTo();
        if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE, paymentTo)
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, paymentTo)) {

            return BANK_SUB_SUB_HEADER;

        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, paymentTo)
                || StringUtils
                        .equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE, paymentTo)
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_JH_IRA_CODE,
                        paymentTo)) {

            return ROLLOVER_FI_SUB_SUB_HEADER;

        } else if (StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE,
                paymentTo)
                || StringUtils.equals(
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                        paymentTo) ) {
        	 if (isFirstPayee()) {

                 return ROLLOVER_FI_SUB_SUB_HEADER;

             } else if (isSecondPayee()) {

                 return ROLLOVER_FI_SUB_SUB_HEADER;

             }
            
             else {

                 return StringUtils.EMPTY;
             }


        }else if (StringUtils.equals(paymentTo,
                WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
        	if(getWithdrawalRequestPayee().getParticipant()!=null && 
        			getWithdrawalRequestPayee().getParticipant().indexOf("Participant") !=-1 ){
        		return BANK_SUB_SUB_HEADER;
        	}else{
        		return ROLLOVER_FI_SUB_SUB_HEADER ;
        	}
        	
        }
        else {

            return StringUtils.EMPTY;
        }
		 
    }

    public String getCheckPayeeSubSubHeader() {

        final String paymentTo = getParent().getParent().getWithdrawalRequest().getPaymentTo();
        if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE, paymentTo)) {

            return PARTICIPANT_SUB_SUB_HEADER;

        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, paymentTo)) {

            return PLAN_TRUSTEE_SUB_SUB_HEADER;

        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, paymentTo)
                || StringUtils
                        .equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE, paymentTo)) {

            return ROLLOVER_FI_SUB_SUB_HEADER;

        }else if (StringUtils.equals(paymentTo,
                WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
        	if(getWithdrawalRequestPayee().getParticipant()!=null && 
        			getWithdrawalRequestPayee().getParticipant().indexOf("Participant") !=-1 ){
        		return PARTICIPANT_SUB_SUB_HEADER;
        	}else{
        		return ROLLOVER_FI_SUB_SUB_HEADER ;
        	}
        	
        }
         else if (StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE,
                paymentTo)
                || StringUtils.equals(
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                        paymentTo)) {

            if (isFirstPayee()) {

                return ROLLOVER_FI_SUB_SUB_HEADER;

            } else if (isSecondPayee()) {

                return ROLLOVER_FI_SUB_SUB_HEADER;

            }
            else {

                return StringUtils.EMPTY;
            }

        } else {

            return StringUtils.EMPTY;
        }
    }

    /**
     * Sets the default rollover plan name.
     */
    public void setDefaultRolloverPlanName() {
        if (StringUtils.isBlank(getWithdrawalRequestPayee().getRolloverPlanName())) {
            getWithdrawalRequestPayee().setRolloverPlanName(
                    WithdrawalRequestPayee.DEFAULT_ROLLOVER_PLAN_NAME);
        }
    }

    /**
     * @return the addressUi
     */
    public AddressUi getAddressUi() {
        return addressUi;
    }

    /**
     * @param addressUi the addressUi to set
     */
    public void setAddressUi(final AddressUi addressUi) {
        this.addressUi = addressUi;
    }


    /**
     * @return the eftOrganizationName
     */
    public String getEftOrganizationName() {
        return eftOrganizationName;
    }

    /**
     * @param eftOrganizationName the eftOrganizationName to set
     */
    public void setEftOrganizationName(String eftOrganizationName) {
        this.eftOrganizationName = eftOrganizationName;
    }

    /**
     * @return the checkOrganizationName
     */
    public String getCheckOrganizationName() {
        return checkOrganizationName;
    }

    /**
     * @param checkOrganizationName the checkOrganizationName to set
     */
    public void setCheckOrganizationName(String checkOrganizationName) {
        this.checkOrganizationName = checkOrganizationName;
    }
    

    public String getSubSectionNameColumn() {
        final String paymentTo = getParent().getParent().getWithdrawalRequest().getPaymentTo();
       
        if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, paymentTo)
                || StringUtils
                        .equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE, paymentTo)
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_JH_IRA_CODE,
                        paymentTo)
                || StringUtils.equals(
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE,
                        paymentTo)
               || StringUtils.equals(
                         WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                         paymentTo)) {

            return NAME_SUBSECTION_PROVIDER_NAME;

        } else if (StringUtils.equals(paymentTo,
                WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
        	if(getWithdrawalRequestPayee().getParticipant()!=null && 
        			getWithdrawalRequestPayee().getParticipant().indexOf("Participant") !=-1 ){
        		return NAME_SUBSECTION_NAME;
        	}else{
        		return NAME_SUBSECTION_PROVIDER_NAME ;
        	}
        	
        }else  {
        	return NAME_SUBSECTION_NAME;
        }
    
    }
    
    private void setSubSectionNameColumn() {
    	subSectionNameColumn = getSubSectionNameColumn();
    }

    /**
     * Set the required values for PDF
     */
    public void setValuesForPDF() {
    	setIsFirstRolloverPayee();
    	setSubSectionNameColumn();
    	setPaymentToDisplay();
    	setShowBankAccountNumber();
    	setShowAccountNumber();
    	setShowTrusteeForRollover();
    }
    /**
     * Determines whether the payee/provider name should be editable or not. 
     * 
     * @return the eftPayeePartyNameEditable
     */    
    public boolean isEftPayeeNameEditable() {

        return getWithdrawalRequestPayee().isEftPayeeNameEditable(
                getParent().getParent().getWithdrawalRequest());
    }
    /**
     * Determines whether the payee/provider address should be editable or not. 
     * 
     * @return the eftPayeePartyNameEditable
     */    
    public boolean isPayeeAddressEditable() {

        return getWithdrawalRequestPayee().isPayeeAddressEditable(
                getParent().getParent().getWithdrawalRequest());
    }

	public String getEftRolloverType() {
		return eftRolloverType;
	}

	public void setEftRolloverType(String eftRolloverType) {
		this.eftRolloverType = eftRolloverType;
	}

	public String getCheckRolloverType() {
		return checkRolloverType;
	}

	public void setCheckRolloverType(String checkRolloverType) {
		this.checkRolloverType = checkRolloverType;
	}
    
   
}
