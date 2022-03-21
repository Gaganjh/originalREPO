package com.manulife.pension.service.withdrawal.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;

import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * Provides helper support for initializing the withdrawal request - subgraph objects (recipient and
 * payee).
 * 
 * @author Andrew Dick
 */
public final class WithdrawalRequestHelper {

    private static final Logger logger = Logger.getLogger(WithdrawalRequestHelper.class);

    private static final Integer PAYEE_NO_1 = 1;

    private static final Integer PAYEE_NO_2 = 2;
    
    private static final Integer PAYEE_NO_3 = 3;

    private static final Integer PAYEE_NO_4 = 4;


    /**
     * Default Constructor.
     */
    public WithdrawalRequestHelper() {
    }

    /**
     * Populates the request object with a default recipient object information and its sub-objects
     * based on the Payment To property.
     * 
     * @param request The withdrawal request to create the collection of default recipients for.
     */
	public static void populateDefaultRecipient(final WithdrawalRequest request) {

        final Collection<Recipient> recipients = new ArrayList<Recipient>();
        final WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();

        recipient.setAddress(request.getParticipantAddress());

        if (request.getParticipantAddress().isBlank()) {
            recipient.getAddress().setCountryCode(Address.USA_COUNTRY_CODE);
        }

        // Set state of residence
        recipient.setStateOfResidenceCode(request.getParticipantStateOfResidence());

        // Set participant name
        recipient.setFirstName(request.getFirstName());
        recipient.setLastName(request.getLastName());

        // Add recipient to our list
        recipients.add(recipient);

        final Collection<Payee> payees = new ArrayList<Payee>();
        final Payee payee = new WithdrawalRequestPayee();
        payee.setPayeeNo(PAYEE_NO_1);
        payees.add(payee);

        // Check if two payees are required
        if (StringUtils.equals(request.getPaymentTo(),
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE)
                || StringUtils.equals(request.getPaymentTo(),
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE) || StringUtils.equals(request.getPaymentTo(),
                                WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION) ) {

            final WithdrawalRequestPayee payee2 = new WithdrawalRequestPayee();
            payee2.setPayeeNo(PAYEE_NO_2);
            payees.add(payee2);
        }

        recipient.setPayees(payees);

        request.setRecipients(recipients);
    }

    /**
     * Updates the recipient objects and their sub-objects based on the Payment To property.
     * 
     * @param request The withdrawal request to update the recipients for.
     */
    public static void updateRecipients(final WithdrawalRequest request) {

        // Verify payment to field has been set
        if (StringUtils.isBlank(request.getPaymentTo())) {
            throw new NestableRuntimeException(new StringBuffer(
                    "Withdrawal request must have a Payment To set [").append(request).append("]")
                    .toString());
        }
       
        // Check if payment to has changed
        if (request.hasPaymentToChanged()) {

            updatedPayeesForChangedPaymentTo(request);

        } else {

            // Payment to hasn't changed - use existing payees and set default address
            updatePayeeDefaultAddressForUnchangedPaymentTo(request);
        }

        // Set the first recipient value for state of residence.
        if (StringUtils.isNotBlank(request.getParticipantStateOfResidence())) {
            request.getRecipients().iterator().next().setStateOfResidenceCode(
                    request.getParticipantStateOfResidence());
        } // fi
    }

    /**
     * Updates the payees for requests with changed payment to fields.
     * 
     * @param request The withdrawal request to update the payees.
     */
    private static void updatedPayeesForChangedPaymentTo(final WithdrawalRequest request) {

        // Determine which payment to we are dealing with
        if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE, request
                .getPaymentTo())) {

            // Handle direct to participant
            updatedPayeesForDirectToParticipant(request);

        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, request
                .getPaymentTo())) {

            // Handle rollover to IRA
            updatedPayeesForRolloverToIra(request);

        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE, request
                .getPaymentTo())) {

            // Handle rollover to plan
            updatedPayeesForRolloverToPlan(request);

        } else if (StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE, request
                        .getPaymentTo())) {

            // Handle after tax contribution remainder to IRA
            updatedPayeesForAfterTaxContributionRemainderToIra(request);

        } else if (StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE, request
                        .getPaymentTo())) {

            // Handle after tax contribution remainder to plan
            updatedPayeesForAfterTaxContributionRemainderToPlan(request);

        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, request
                .getPaymentTo())) {

            // Handle plan trustee
            updatedPayeesForPlanTrustee(request);

        }else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION, request
                .getPaymentTo())) {

            // Handle multiple destination
            //updatedPayeesForMultipleDestination(request);
        	//updatedPayeesForDirectToParticipant(request);
        	updatesPayeeForDefaultMutlipayee(request);

        } 
        else {

            throw new NestableRuntimeException(new StringBuffer(
                    "Withdrawal request must have a valid Payment To [").append(
                    request.getPaymentTo()).append("] - [").append(request).append("]").toString());
        }

    }

    /**
     * Updates the payees for requests with direct to participant payment to.
     * 
     * @param request The withdrawal request to update the payee.
     */
    private static void updatedPayeesForDirectToParticipant(final WithdrawalRequest request) {

        final WithdrawalRequestPayee newPayee = new WithdrawalRequestPayee();
        final Recipient recipient = request.getRecipients().iterator().next();
        final Collection<Payee> newPayees = new ArrayList<Payee>();
        
        // Security Enhancements - defaults to participant name
        newPayee.setOrganizationName(request.getParticipantName());

        // Determine which payee to draw information from - gracefully handle no original payment to
        // by using existing single payee
        if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, request
                .getOriginalPaymentTo())
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE, request
                        .getOriginalPaymentTo())
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, request
                        .getOriginalPaymentTo())
                ||StringUtils.equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION, request
                        .getOriginalPaymentTo())
                || StringUtils.isBlank(request.getOriginalPaymentTo())) {

            // Use participant if payment method is check otherwise use blank
            final Payee oldPayee = recipient.getPayees().iterator().next();
            
            // Security Enhancements - default to participant address
            newPayee.setAddress(((Address)request.getParticipantAddress()).cloneAddress());
            newPayee.setDefaultAddress(((Address)request.getParticipantAddress()).cloneAddress());

            newPayee.setPaymentMethodCode(oldPayee.getPaymentMethodCode());
            newPayee.setPayeeNo(PAYEE_NO_1);

        } else if (StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE, request
                        .getOriginalPaymentTo())
                || StringUtils.equals(
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                        request.getOriginalPaymentTo())) {

            // Need to get default from second payee
            final Iterator<Payee> iterator = recipient.getPayees().iterator();
            iterator.next();
           
            if (iterator.hasNext()) {                
                final Payee oldPayee = iterator.next();
                newPayee.setAddress(oldPayee.getAddress());
                newPayee.setDefaultAddress(((Address) request.getParticipantAddress()).cloneAddress());
                newPayee.setPaymentMethodCode(oldPayee.getPaymentMethodCode());
                newPayee.setPayeeNo(PAYEE_NO_1);
                // Security Enhancements - removed code to set OrganizationName
                // For direct to participant, payee name defaults to participant name
            }

        } else {

            throw new NestableRuntimeException(new StringBuffer(
                    "Withdrawal request must have a valid Original Payment To [").append(
                    request.getOriginalPaymentTo()).append("] - [").append(request).append("]")
                    .toString());
        }

        // Place new payee into graph
        newPayees.add(newPayee);
        recipient.setPayees(newPayees);
    }

    /**
     * Updates the payees for requests with rollover to IRA payment to.
     * 
     * @param request The withdrawal request to update the payee.
     */
    private static void updatedPayeesForRolloverToIra(final WithdrawalRequest request) {

        final WithdrawalRequestPayee newPayee = new WithdrawalRequestPayee();
        final Recipient recipient = request.getRecipients().iterator().next();
        final Collection<Payee> newPayees = new ArrayList<Payee>();

        // Determine which payee to draw information from - gracefully handle no original payment to
        // by using existing single payee
        if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE, request
                .getOriginalPaymentTo())
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE, request
                        .getOriginalPaymentTo())
                || StringUtils.equals(
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                        request.getOriginalPaymentTo())
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, request
                        .getOriginalPaymentTo())
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION, request
                        .getOriginalPaymentTo())
                || StringUtils.isBlank(request.getOriginalPaymentTo())) {

            // Blank address and default
            newPayee.setAddress(new Address());
            newPayee.setDefaultAddress(new Address());
            newPayee.setPayeeNo(PAYEE_NO_1);

        } else if (StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE, request
                        .getOriginalPaymentTo())) {

            // Need to get default from first payee
            final Payee oldPayee = recipient.getPayees().iterator().next();
            newPayee.setAddress(oldPayee.getAddress());
            newPayee.setDefaultAddress(new Address());
            newPayee.setPaymentMethodCode(oldPayee.getPaymentMethodCode());
            newPayee.setPayeeNo(PAYEE_NO_1);
            if (StringUtils.equals(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, oldPayee
                    .getPaymentMethodCode())
                    || StringUtils.equals(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, oldPayee
                            .getPaymentMethodCode())
                    || StringUtils.equals(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE, oldPayee
                            .getPaymentMethodCode())) {
                newPayee.setOrganizationName(oldPayee.getOrganizationName());
            }

        } else {

            throw new NestableRuntimeException(new StringBuffer(
                    "Withdrawal request must have a valid Original Payment To [").append(
                    request.getOriginalPaymentTo()).append("] - [").append(request).append("]")
                    .toString());
        }

        // Place new payee into graph
        newPayees.add(newPayee);
        recipient.setPayees(newPayees);
    }

    /**
     * Updates the payees for requests with rollover to plan payment to.
     * 
     * @param request The withdrawal request to update the payee.
     */
    private static void updatedPayeesForRolloverToPlan(final WithdrawalRequest request) {

        final Payee newPayee = new WithdrawalRequestPayee();
        final Recipient recipient = request.getRecipients().iterator().next();
        final Collection<Payee> newPayees = new ArrayList<Payee>();

        // Determine which payee to draw information from - gracefully handle no original payment to
        // by using existing single payee
        if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE, request
                .getOriginalPaymentTo())
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, request
                        .getOriginalPaymentTo())
                || StringUtils.equals(
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE,
                        request.getOriginalPaymentTo())
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, request
                        .getOriginalPaymentTo())
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION, request
                        .getOriginalPaymentTo())
                || StringUtils.isBlank(request.getOriginalPaymentTo())) {

            // Blank address and default
            newPayee.setAddress(new Address());
            newPayee.setDefaultAddress(new Address());
            newPayee.setPayeeNo(PAYEE_NO_1);

        } else if (StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE, request
                        .getOriginalPaymentTo())) {

            // Need to get default from first payee
            final Payee oldPayee = recipient.getPayees().iterator().next();
            newPayee.setAddress(oldPayee.getAddress());
            newPayee.setDefaultAddress(new Address());
            newPayee.setPaymentMethodCode(oldPayee.getPaymentMethodCode());
            newPayee.setPayeeNo(PAYEE_NO_1);
            if (StringUtils.equals(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, oldPayee
                    .getPaymentMethodCode())
                    || StringUtils.equals(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, oldPayee
                            .getPaymentMethodCode())
                    || StringUtils.equals(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE, oldPayee
                            .getPaymentMethodCode())) {
                newPayee.setOrganizationName(oldPayee.getOrganizationName());
            }

        } else {

            throw new NestableRuntimeException(new StringBuffer(
                    "Withdrawal request must have a valid Original Payment To [").append(
                    request.getOriginalPaymentTo()).append("] - [").append(request).append("]")
                    .toString());
        }

        // Place new payee into graph
        newPayees.add(newPayee);
        recipient.setPayees(newPayees);
    }

    /**
     * Updates the payees for requests with after tax contribution remainder to IRA payment to.
     * 
     * @param request The withdrawal request to update the payee.
     */
    private static void updatedPayeesForAfterTaxContributionRemainderToIra(
            final WithdrawalRequest request) {

        final Collection<Payee> newPayees = new ArrayList<Payee>();
        final WithdrawalRequestPayee newPayee1 = new WithdrawalRequestPayee();
        final WithdrawalRequestPayee newPayee2 = new WithdrawalRequestPayee();
        final Recipient recipient = request.getRecipients().iterator().next();
        final Iterator<Payee> iterator = recipient.getPayees().iterator();
        final Payee oldPayee1 = iterator.next();
        Payee oldPayee2 = null;
        if (iterator.hasNext()) {
            oldPayee2 = iterator.next();
        }
        
        // Security Enhancements - defaults to participant name
        newPayee2.setOrganizationName(request.getParticipantName());
        
        // Determine which payee to draw information from - gracefully handle no original payment to
        // by using existing single payee
        if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE, request
                .getOriginalPaymentTo())

                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, request
                        .getOriginalPaymentTo())
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION, request
                        .getOriginalPaymentTo())
                || StringUtils.isBlank(request.getOriginalPaymentTo())) {

            // Blank payee 1 address and default
            newPayee1.setAddress(new Address());
            newPayee1.setDefaultAddress(new Address());
            newPayee1.setPayeeNo(PAYEE_NO_1);

            // Security Enhancements - payee2 address defaults to participant address
            newPayee2.setAddress(((Address)request.getParticipantAddress()).cloneAddress());
            
            newPayee2.setDefaultAddress(((Address)request.getParticipantAddress()).cloneAddress());
            newPayee2.setPayeeNo(PAYEE_NO_2);

        } else if (StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE, request
                        .getOriginalPaymentTo())) {

            // Blank payee 1 address and default
            newPayee1.setAddress(new Address());
            newPayee1.setDefaultAddress(new Address());
            newPayee1.setPayeeNo(PAYEE_NO_1);

            // Payee 2 address uses participant if payment method is check
            newPayee2.setAddress(StringUtils.equals(
                    WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, oldPayee2
                            .getPaymentMethodCode()) ? ((Address)request.getParticipantAddress())
                    .cloneAddress() : new Address());
            newPayee2.setDefaultAddress(((Address)request.getParticipantAddress()).cloneAddress());
            newPayee2.setPayeeNo(PAYEE_NO_2);

        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE,
                request.getOriginalPaymentTo())) {

            // Blank payee 1 address and default
            newPayee1.setAddress(new Address());
            newPayee1.setDefaultAddress(new Address());
            newPayee1.setPayeeNo(PAYEE_NO_1);

            // Payee 2 address uses payee address
            newPayee2.setAddress(((Address)oldPayee1.getAddress()).cloneAddress());
            newPayee2.setDefaultAddress(((Address)request.getParticipantAddress()).cloneAddress());
            newPayee2.setPaymentMethodCode(oldPayee1.getPaymentMethodCode());
            newPayee2.setPayeeNo(PAYEE_NO_2);
            
            // Security Enhancements - removed logic to set the organization for payee2

        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, request
                .getOriginalPaymentTo())) {

            // Blank payee 1 address and default
            newPayee1.setAddress(((Address)oldPayee1.getAddress()).cloneAddress());
            newPayee1.setDefaultAddress(new Address());
            newPayee1.setPaymentMethodCode(oldPayee1.getPaymentMethodCode());
            newPayee1.setPayeeNo(PAYEE_NO_1);
            if (StringUtils.equals(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, oldPayee1
                    .getPaymentMethodCode())
                    || StringUtils.equals(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, oldPayee1
                            .getPaymentMethodCode())
                    || StringUtils.equals(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE,
                            oldPayee1.getPaymentMethodCode())) {
                newPayee1.setOrganizationName(oldPayee1.getOrganizationName());
            }

            // Payee 2 address uses participant if payment method is check
            newPayee2.setAddress(StringUtils.equals(
                    WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, oldPayee1
                            .getPaymentMethodCode()) ?((Address) request.getParticipantAddress())
                    .cloneAddress() : new Address());
            newPayee2.setDefaultAddress(((Address)request.getParticipantAddress()).cloneAddress());
            newPayee2.setPayeeNo(PAYEE_NO_2);

        } else {

            throw new NestableRuntimeException(new StringBuffer(
                    "Withdrawal request must have a valid Original Payment To [").append(
                    request.getOriginalPaymentTo()).append("] - [").append(request).append("]")
                    .toString());
        }

        // Place new payee into graph
        newPayees.add(newPayee1);
        newPayees.add(newPayee2);
        recipient.setPayees(newPayees);
    }

    /**
     * Updates the payees for requests with after tax contribution remainder to plan payment to.
     * 
     * @param request The withdrawal request to update the payee.
     */
    private static void updatedPayeesForAfterTaxContributionRemainderToPlan(
            final WithdrawalRequest request) {

        final Collection<Payee> newPayees = new ArrayList<Payee>();
        final WithdrawalRequestPayee newPayee1 = new WithdrawalRequestPayee();
        final WithdrawalRequestPayee newPayee2 = new WithdrawalRequestPayee();
        final Recipient recipient = request.getRecipients().iterator().next();
        final Iterator<Payee> iterator = recipient.getPayees().iterator();
        final Payee oldPayee1 = iterator.next();
        Payee oldPayee2 = null;
        if (iterator.hasNext()) {
            oldPayee2 = iterator.next();
        }

        // Security Enhancements - defaults to participant name
        newPayee2.setOrganizationName(request.getParticipantName());

        // Determine which payee to draw information from - gracefully handle no original payment to
        // by using existing single payee
        if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, request
                .getOriginalPaymentTo())

                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, request
                        .getOriginalPaymentTo())
                || StringUtils.isBlank(request.getOriginalPaymentTo())) {

            // Blank payee 1 address and default
            newPayee1.setAddress(new Address());
            newPayee1.setDefaultAddress(new Address());
            newPayee1.setPayeeNo(PAYEE_NO_1);

            // Security Enhancements - payee address defaults to participant address
            newPayee2.setAddress(((Address)request.getParticipantAddress()).cloneAddress());
            
            newPayee2.setDefaultAddress(((Address)request.getParticipantAddress()).cloneAddress());
            newPayee2.setPayeeNo(PAYEE_NO_2);

        } else if (StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE, request
                        .getOriginalPaymentTo())) {

            if (oldPayee2 == null) {
                throw new NestableRuntimeException(new StringBuffer(
                        "Withdrawal request with payment to [").append(request.getPaymentTo())
                        .append("] must have a second payee - [").append(request).append("]")
                        .toString());
            }

            // Blank payee 1 address and default
            newPayee1.setAddress(new Address());
            newPayee1.setDefaultAddress(new Address());
            newPayee1.setPayeeNo(PAYEE_NO_1);

            // Payee 2 address uses participant if payment method is check
            newPayee2.setAddress(StringUtils.equals(
                    WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, oldPayee2
                            .getPaymentMethodCode()) ? ((Address)request.getParticipantAddress())
                    .cloneAddress() : new Address());
            newPayee2.setDefaultAddress(((Address)request.getParticipantAddress()).cloneAddress());
            newPayee2.setPayeeNo(PAYEE_NO_2);

        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE,
                request.getOriginalPaymentTo())) {

            // Blank payee 1 address and default
            newPayee1.setAddress(new Address());
            newPayee1.setDefaultAddress(new Address());
            newPayee1.setPayeeNo(PAYEE_NO_1);

            // Payee 2 address uses payee address
            newPayee2.setAddress(((Address)oldPayee1.getAddress()).cloneAddress());
            newPayee2.setDefaultAddress(((Address)request.getParticipantAddress()).cloneAddress());
            newPayee2.setPaymentMethodCode(oldPayee1.getPaymentMethodCode());
            newPayee2.setPayeeNo(PAYEE_NO_2);
            
            // Security Enhancements - removed logic to set organization name

        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE, request
                .getOriginalPaymentTo())) {

            // Blank payee 1 address and default
            newPayee1.setAddress(((Address)oldPayee1.getAddress()).cloneAddress());
            newPayee1.setDefaultAddress(new Address());
            newPayee1.setPaymentMethodCode(oldPayee1.getPaymentMethodCode());
            newPayee1.setPayeeNo(PAYEE_NO_1);
            if (StringUtils.equals(WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, oldPayee1
                    .getPaymentMethodCode())
                    || StringUtils.equals(WithdrawalRequestPayee.ACH_PAYMENT_METHOD_CODE, oldPayee1
                            .getPaymentMethodCode())
                    || StringUtils.equals(WithdrawalRequestPayee.WIRE_PAYMENT_METHOD_CODE,
                            oldPayee1.getPaymentMethodCode())) {
                newPayee1.setOrganizationName(oldPayee1.getOrganizationName());
            }

            // Payee 2 address uses participant if payment method is check
            newPayee2.setAddress(StringUtils.equals(
                    WithdrawalRequestPayee.CHECK_PAYMENT_METHOD_CODE, recipient.getPayees()
                            .iterator().next().getPaymentMethodCode()) ? ((Address)request
                    .getParticipantAddress()).cloneAddress() : new Address());
            newPayee2.setDefaultAddress(((Address)request.getParticipantAddress()).cloneAddress());
            newPayee2.setPayeeNo(PAYEE_NO_2);

        } else {

            throw new NestableRuntimeException(new StringBuffer(
                    "Withdrawal request must have a valid Original Payment To [").append(
                    request.getOriginalPaymentTo()).append("] - [").append(request).append("]")
                    .toString());
        }

        // Place new payee into graph
        newPayees.add(newPayee1);
        newPayees.add(newPayee2);
        recipient.setPayees(newPayees);
    }

    /**
     * Updates the payees for requests with plan trustee payment to.
     * 
     * @param request The withdrawal request to update the payee.
     */
    private static void updatedPayeesForPlanTrustee(final WithdrawalRequest request) {

        final Payee newPayee = new WithdrawalRequestPayee();
        final Recipient recipient = request.getRecipients().iterator().next();
        final Collection<Payee> newPayees = new ArrayList<Payee>();
        
        // Security Enhancements - defaults to trustee name
        newPayee.setOrganizationName(request.getParticipantInfo().getTrusteeName());            

        // Determine which payee to draw information from - gracefully handle no original payment to
        // by using existing single payee
        if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE, request
                .getOriginalPaymentTo())
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, request
                        .getOriginalPaymentTo())
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE, request
                        .getOriginalPaymentTo())
                || StringUtils.equals(
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE,
                        request.getOriginalPaymentTo())
                || StringUtils.equals(
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                        request.getOriginalPaymentTo())
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION, request
                        .getOriginalPaymentTo())
                || StringUtils.isBlank(request.getOriginalPaymentTo())) {

            // Blank address and default
            newPayee.setAddress((Address) request.getTrusteeAddress().cloneAddress());
            newPayee.setDefaultAddress((Address) request.getTrusteeAddress().cloneAddress());
            newPayee.setPayeeNo(PAYEE_NO_1);

        } else {

            throw new NestableRuntimeException(new StringBuffer(
                    "Withdrawal request must have a valid Original Payment To [").append(
                    request.getOriginalPaymentTo()).append("] - [").append(request).append("]")
                    .toString());
        }

        // Place new payee into graph
        newPayees.add(newPayee);
        recipient.setPayees(newPayees);
    }

    
    /**
     * Updates the payees for requests with MultipleDestination payment to.
     * 
     * @param request The withdrawal request to update the payee.
     */
    public static void updatedPayeesForMultipleDestination(final WithdrawalRequest request) {
		

        final Collection<Payee> newPayees = new ArrayList<Payee>();
        final WithdrawalRequestPayee newPayee1 = new WithdrawalRequestPayee();
        final WithdrawalRequestPayee newPayee2 = new WithdrawalRequestPayee();
        final WithdrawalRequestPayee newPayee3 = new WithdrawalRequestPayee();
        final WithdrawalRequestPayee newPayee4 = new WithdrawalRequestPayee();
        final Recipient recipient = request.getRecipients().iterator().next();
        final Iterator<Payee> iterator = recipient.getPayees().iterator();
        final Payee oldPayee1 = iterator.next();
        Payee oldPayee2 = null;
        boolean payeeFlag1 = false;
        boolean payeeFlag2 = false;
        boolean payeeFlag3 = false;
        if (iterator.hasNext()) {
            oldPayee2 = iterator.next();
           
        }
       
        // Security Enhancements - defaults to participant name
       
       
            // Blank payee 1 address and default
        if((request.getTbCategory() != null && request.getTbCategory().equals("TIRA")) || (!request.isNonTaxableFlag() && request.getNratCategory() != null && request.getNratCategory().equals("TIRA")) ){
        	
            newPayee1.setOrganizationName(oldPayee1.getOrganizationName());
            newPayee1.setPayeeNo(PAYEE_NO_1);
            newPayee1.setReasonCode(Payee.REASON_CODE_ROLLOVER);
            newPayee1.setTypeCode(Payee.TYPE_CODE_FINANCIAL_INSTITUTION);
            newPayee1.setFirstName(request.getFirstName());
        	newPayee1.setLastName(request.getLastName());
        	newPayee1.setParticipant(request.getTraditionalIRAPayee());
        	payeeFlag1 = true ;
            if(request.getTraditionalIRAFlag()!= null){
            newPayee1.setTaxes(request.getTraditionalIRAFlag());
            }
            newPayees.add(newPayee1);
        	}
        
        	if ((!request.isTotalRothBalFlag() && request.getRbCategory() != null && request.getRbCategory().equals("RIRA")) || (request.getTbCategory() != null && request.getTbCategory().equals("RIRA")) || (!request.isNonTaxableFlag() && request.getNratCategory() != null && request.getNratCategory().equals("RIRA"))  ){
        	newPayee2.setOrganizationName(oldPayee1.getOrganizationName());
        	newPayee2.setFirstName(request.getFirstName());
        	newPayee2.setLastName(request.getLastName());
        	newPayee2.setTypeCode(Payee.TYPE_CODE_FINANCIAL_INSTITUTION);
           
            if(payeeFlag1){
            newPayee2.setPayeeNo(PAYEE_NO_2);
        	}else{
        	newPayee2.setPayeeNo(PAYEE_NO_1);
        	}
            payeeFlag2 =true;
            newPayee2.setParticipant(request.getRothIRAPayee());
            newPayee2.setReasonCode(Payee.REASON_CODE_ROLLOVER);
            if(request.getRothIRAFlag() != null){
            newPayee2.setTaxes(request.getRothIRAFlag());
            }
            newPayees.add(newPayee2);
        	}
    

            // Security Enhancements - payee address defaults to participant address
        	if((!request.isTotalRothBalFlag() && request.getRbCategory() != null && request.getRbCategory().equals("EQP")) || (request.getTbCategory() != null && request.getTbCategory().equals("EQP")) || (!request.isNonTaxableFlag() && request.getNratCategory() != null && request.getNratCategory().equals("EQP"))  ){
            newPayee3.setAddress(((Address)request.getParticipantAddress()).cloneAddress());
            newPayee3.setFirstName(request.getFirstName());
        	newPayee3.setLastName(request.getLastName());
            newPayee3.setOrganizationName(request.getParticipantName());
            newPayee3.setReasonCode(Payee.REASON_CODE_ROLLOVER);
            newPayee3.setTypeCode(Payee.TYPE_CODE_FINANCIAL_INSTITUTION);
            if(payeeFlag1 && payeeFlag2){
                newPayee3.setPayeeNo(PAYEE_NO_3);
            }else if ((payeeFlag1 && !payeeFlag2)|| (!payeeFlag1 && payeeFlag2)) {
                newPayee3.setPayeeNo(PAYEE_NO_2);
            }else {
                newPayee3.setPayeeNo(PAYEE_NO_1);
            }
            payeeFlag3 = true;
            newPayee3.setParticipant(request.getEmpQulifiedPlanPayee());
            if(request.getEmpQulifiedPlanFlag()!=null){
            newPayee3.setTaxes(request.getEmpQulifiedPlanFlag());
            }
            newPayees.add(newPayee3);
        	}
     
        	if((request.getPayDirectlyTome() != null) && ( request.getPayDirectlyTome().equals("PAAT") || request.getPayDirectlyTome().equals("PAR") || request.getPayDirectlyTome().equals("PA"))){
            	newPayee4.setFirstName(request.getFirstName());
            	newPayee4.setLastName(request.getLastName());
            	
            newPayee4.setAddress(((Address)request.getParticipantAddress()).cloneAddress());
            newPayee4.setDefaultAddress(((Address)request.getParticipantAddress()).cloneAddress());
            newPayee4.setOrganizationName(request.getParticipantName());
            newPayee4.setTypeCode(Payee.TYPE_CODE_PARTICIPANT);
            if(payeeFlag1 && payeeFlag2 && payeeFlag3){
            newPayee4.setPayeeNo(PAYEE_NO_4);
            } else if((payeeFlag2 && payeeFlag3) || (payeeFlag1 && payeeFlag2)|| (payeeFlag1 && payeeFlag3) ){
                newPayee4.setPayeeNo(PAYEE_NO_3);
                }else {
            	newPayee4.setPayeeNo(PAYEE_NO_2);
            }
            newPayee4.setParticipant(request.getParticipantDetails());
            newPayee4.setReasonCode(Payee.REASON_CODE_PAYMENT);
            if(request.getParticipantTaxesFlag() != null){
            newPayee4.setTaxes(request.getParticipantTaxesFlag());
            }
            if(request.getPayDirectlyTomeAmount()!= null){
            	newPayee4.getPaymentInstruction().setPaymentAmount(request.getPayDirectlyTomeAmount());
            }
            newPayees.add(newPayee4);
            }
            
        recipient.setPayees(newPayees);
    
    }
    /**
     * Updates the payee default addresses for requests with unchanged payment to fields.
     * 
     * @param request The withdrawal request to update the payee default addresses.
     */
    private static void updatePayeeDefaultAddressForUnchangedPaymentTo(
            final WithdrawalRequest request) {

        // Determine which payment to is being used
        if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE, request
                .getPaymentTo())) {
            
            // Security Enhancements - set Participant Address as default
            if (request.getRecipients().iterator().next().getPayees().iterator().next().getDefaultAddress().isBlank()) {
                request.getRecipients().iterator().next().getPayees().iterator().next()
                    .setDefaultAddress(((Address)(Address) request.getParticipantAddress()).cloneAddress());
            }

        } else if (StringUtils.equals(
                WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_IRA_CODE, request
                        .getPaymentTo())
                || StringUtils.equals(
                        WithdrawalRequest.PAYMENT_TO_AFTER_TAX_CONTRIBUTION_REMAINDER_TO_PLAN_CODE,
                        request.getPaymentTo())) {

            // Set Participant Address for second payee as default
            final Iterator<Payee> iterator = request.getRecipients().iterator()
                    .next().getPayees().iterator();
            // skip the first payee
            iterator.next();            
                 
            // Security Enhancements - set default only if there is no address 
            if (iterator.next().getDefaultAddress().isBlank()) {
            	final Iterator<Payee> iter = request.getRecipients().iterator().next().getPayees().iterator();
            	iter.next();
            	iter.next()
                	.setDefaultAddress(
                			((Address) request.getParticipantAddress()).cloneAddress());
            }

        } else if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, request
                .getPaymentTo())) {

            // Security Enhancements - set default only if there is no address 
            if (request.getRecipients().iterator().next().getPayees().iterator().next().getDefaultAddress().isBlank()) {
                request.getRecipients().iterator().next().getPayees().iterator().next()
                    .setDefaultAddress((Address) request.getTrusteeAddress().cloneAddress());
            }

        } else {
            // Rollover to IRA or Rollover to Plan - do nothing
        }
    }
    private static void updatesPayeeForDefaultMutlipayee(WithdrawalRequest request){


        final WithdrawalRequestPayee newPayee = new WithdrawalRequestPayee();
        final Recipient recipient = request.getRecipients().iterator().next();
        final Collection<Payee> newPayees = new ArrayList<Payee>();
        
        // Security Enhancements - defaults to participant name
        newPayee.setOrganizationName(request.getParticipantName());

        // Determine which payee to draw information from - gracefully handle no original payment to
        // by using existing single payee
        if (StringUtils.equals(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE, request
                .getOriginalPaymentTo()) 
        		|| StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE, request
                .getOriginalPaymentTo())
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE, request
                        .getOriginalPaymentTo())
                || StringUtils.equals(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE, request
                        .getOriginalPaymentTo())
                || StringUtils.isBlank(request.getOriginalPaymentTo())) {

            // Use participant if payment method is check otherwise use blank
            final Payee oldPayee = recipient.getPayees().iterator().next();
            
            // Security Enhancements - default to participant address
            newPayee.setAddress(((Address)request.getParticipantAddress()).cloneAddress());
            newPayee.setDefaultAddress(((Address)request.getParticipantAddress()).cloneAddress());

            newPayee.setPaymentMethodCode(oldPayee.getPaymentMethodCode());
            newPayee.setPayeeNo(PAYEE_NO_1);

        }  else {

            throw new NestableRuntimeException(new StringBuffer(
                    "Withdrawal request must have a valid Original Payment To [").append(
                    request.getOriginalPaymentTo()).append("] - [").append(request).append("]")
                    .toString());
        }

        // Place new payee into graph
        newPayees.add(newPayee);
        recipient.setPayees(newPayees);
    
    }
}
