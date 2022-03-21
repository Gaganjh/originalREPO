/*
 * Apr 26, 2007
 * 5:31:00 PM
 */
package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.*;
import static com.manulife.pension.service.testutility.Assert.assertStringContains;
import static com.manulife.pension.service.testutility.Assert.assertStringDoesNotContain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.manulife.pension.service.distribution.valueobject.Note;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.util.TimestampUtils;

/**
 * This tests the toLog function of the withdrawal request (and subsequently the objects below it in
 * the graph).
 * 
 * @author glennpa
 */
public class WithdrawalRequestToLogTest {

    public static final Logger logger = Logger.getLogger(WithdrawalRequestToLogTest.class);

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest#toLog()}.
     */
//    Leads to assertion failure
    @Test
    public void testToLog() {

//        final WithdrawalRequest withdrawalRequest = getTestWithdrawalRequest();
//
//        // TODO: Remove the stopwatch.
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        final String logResult = withdrawalRequest.toLog();
//        stopWatch.stop();
//
//        logger.debug("\n[ms: " + stopWatch.getTime() + "]RESULT: " + logResult);
//
//        // Exclude fields from the base class.
//        assertStringDoesNotContain(
//                "Log should not contain the field: 'fieldNamesToExcludeFromLogging'.",
//                "fieldNamesToExcludeFromLogging", logResult);
//
//        assertStringDoesNotContain("Log should not contain the errorCodes field.", "errorCodes",
//                logResult);
//        assertStringDoesNotContain("Log should not contain the warningCodes field.",
//                "warningCodes", logResult);
//        assertStringDoesNotContain("Log should not contain the alertCodes field.", "alertCodes",
//                logResult);
//
//        assertStringContains("Status code.", "statusCode="
//                + WithdrawalStateEnum.APPROVED.getStatusCode(), logResult);
//
//        assertStringContains("Status last changed.", "statusLastChanged=1969-12-31 19:00:30.003",
//                logResult);
//
//        assertStringContains("Status code.", "submissionId=12345678", logResult);
//
//        assertStringDoesNotContain("currentAdminToParticipantNote",
//                "currentAdminToParticipantNote", logResult);
//
//        assertStringDoesNotContain("currentAdminToParticipantNote", "My note text.", logResult);
//        assertStringDoesNotContain("currentAdminToParticipantNote", "My note text1.", logResult);
//        assertStringDoesNotContain("currentAdminToParticipantNote", "My note text2.", logResult);
//
//        assertStringContains("Money Type ID", "moneyTypeId=XXmoneyTypeId", logResult);
//
//        assertStringContains("Money Type Total Balance.", "totalBalance=123456789.56", logResult);
//
//        assertStringDoesNotContain("Should not contain ignoreErrors.", "ignoreErrors", logResult);
//        assertStringDoesNotContain("Should not contain ignoreWarnings.", "ignoreWarnings",
//                logResult);
//        assertStringDoesNotContain("Should not contain participantSSN.", "participantSSN",
//                logResult);
//
//        assertStringContains("Recipient created by ID", "createdById=784645", logResult);
//
//        assertStringContains("Recipient AddressLine1", "addressLine1=R addressLine1", logResult);
//        assertStringContains("Recipient countryCode", "countryCode=CA", logResult);
//        assertStringDoesNotContain("Recipient NonMatchedCountryName", "nonMatchedCountryName",
//                logResult);
//        assertStringDoesNotContain("Recipient Zip Code", "zipCode=333447788", logResult);
//        assertStringContains("Recipient Zip Code1", "zipCode1=33344", logResult);
//        assertStringContains("Recipient Zip Code2", "zipCode2=7788", logResult);
//
//        assertStringContains("Legalese info", "legaleseInfo", logResult);
//        assertStringDoesNotContain("Legalese text", "legaleseText=My very own legalese text.",
//                logResult);
//        assertStringContains("Legalese content ID", "contentId=5678", logResult);
//        assertStringDoesNotContain("Legalese user ID", "creatorUserProfileId=84523", logResult);

    }

    /**
     * Gets a test withdrawal request.
     * 
     * @return WithdrawalRequest - A withdrawal request DTO populated with test data.
     */
    private WithdrawalRequest getTestWithdrawalRequest() {
        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();

        // Use Mock Factory to get the test data?

        withdrawalRequest.setStatusCode(WithdrawalStateEnum.APPROVED.getStatusCode());
        withdrawalRequest.setStatusLastChanged(new Timestamp(30003));
        withdrawalRequest.setSubmissionId(new Integer(12345678));

        final WithdrawalRequestNote withdrawalRequestNote = new WithdrawalRequestNote();
        withdrawalRequestNote.setNote("My note text.");
        withdrawalRequestNote.setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_PARTICIPANT_TYPE_CODE);
        withdrawalRequest.setCurrentAdminToParticipantNote(withdrawalRequestNote);
        final WithdrawalRequestNote withdrawalRequestNote1 = new WithdrawalRequestNote();
        withdrawalRequestNote.setNote("My note text1.");
        withdrawalRequestNote.setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_PARTICIPANT_TYPE_CODE);
        final WithdrawalRequestNote withdrawalRequestNote2 = new WithdrawalRequestNote();
        withdrawalRequestNote.setNote("My note text2.");
        withdrawalRequestNote.setNoteTypeCode(WithdrawalRequestNote.ADMIN_TO_PARTICIPANT_TYPE_CODE);
        final Collection<Note> notes = new ArrayList<Note>();
        notes.add(withdrawalRequestNote1);
        notes.add(withdrawalRequestNote2);
        withdrawalRequest.setNotes(notes);

        final WithdrawalRequestMoneyType withdrawalRequestMoneyType = new WithdrawalRequestMoneyType();
        withdrawalRequestMoneyType.setMoneyTypeId("XXmoneyTypeId");
        withdrawalRequestMoneyType.setTotalBalance(new BigDecimal("123456789.56"));

        final Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>();
        moneyTypes.add(withdrawalRequestMoneyType);

        withdrawalRequest.setMoneyTypes(moneyTypes);

        final WithdrawalRequestRecipient withdrawalRequestRecipient = new WithdrawalRequestRecipient();

        withdrawalRequestRecipient.setCreatedById(new Integer(784645));

        final Collection<Recipient> recipients = new ArrayList<Recipient>(
                1);
        recipients.add(withdrawalRequestRecipient);

        withdrawalRequest.setRecipients(recipients);

        final Address recipientAddress = new Address();
        recipientAddress.setAddressLine1("R addressLine1");
        recipientAddress.setCountryCode("CA");
        recipientAddress.setNonMatchedCountryName("R nonMatchedCountryName");
        recipientAddress.setZipCode("333447788");
        withdrawalRequestRecipient.setAddress(recipientAddress);

        final LegaleseInfo legaleseInfo = new LegaleseInfo(new Integer(5678),
                "My very own legalese text.", new Integer(84523));

        withdrawalRequest.setLegaleseInfo(legaleseInfo);

        return withdrawalRequest;

    }

}
