package com.manulife.pension.service.withdrawal.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.vesting.VestingRetrievalDetails;
import com.manulife.pension.service.vesting.util.VestingMessageType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;

/**
 * Tests the WithdrawalVestingMessageHelper.
 * 
 * @author glennpa
 */
public class TestWithdrawalVestingMessageHelper {

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.common.WithdrawalVestingMessageHelper#addVestingErrors(com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest, com.manulife.pension.service.vesting.EmployeeVestingInformation)}.
     */
    @Test
    public void testAddVestingErrorsWithNoErrors() {
        final WithdrawalVestingMessageHelper withdrawalVestingMessageHelper = WithdrawalVestingMessageHelper
                .getInstance();

        final EmployeeVestingInformation employeeVestingInformation = EmployeeVestingInformation.EMPTY;

        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();

        withdrawalVestingMessageHelper.addVestingErrors(withdrawalRequest,
                employeeVestingInformation);

        assertEquals("There should be 0 messages.", 0, withdrawalRequest.getMessages().size());
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.common.WithdrawalVestingMessageHelper#addVestingErrors(com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest, com.manulife.pension.service.vesting.EmployeeVestingInformation)}.
     */
    @Test
    public void testAddVestingErrorsWithCreditingMethodWarning() {
        final WithdrawalVestingMessageHelper withdrawalVestingMessageHelper = WithdrawalVestingMessageHelper
                .getInstance();

        final EmployeeVestingInformation employeeVestingInformation =
            new MockEmployeeVestingInformation(VestingMessageType.CREDITING_METHOD_IS_UNSPECIFIED);

        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();

        withdrawalVestingMessageHelper.addVestingErrors(withdrawalRequest,
                employeeVestingInformation);

        assertEquals("There should be 1 messages.", 1, withdrawalRequest.getMessages().size());

        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.VESTING_CREDITING_METHOD_IS_UNSPECIFIED).append("].").toString(),
                CollectionUtils.exists(withdrawalRequest.getMessages(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.VESTING_CREDITING_METHOD_IS_UNSPECIFIED)));

    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.common.WithdrawalVestingMessageHelper#addVestingErrors(com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest, com.manulife.pension.service.vesting.EmployeeVestingInformation)}.
     */
    @Test
    public void testAddVestingErrorsWithVestingScheduleNotSetUp() {
        final WithdrawalVestingMessageHelper withdrawalVestingMessageHelper = WithdrawalVestingMessageHelper
                .getInstance();

        final EmployeeVestingInformation employeeVestingInformation =
            new MockEmployeeVestingInformation(VestingMessageType.VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP);

        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();

        withdrawalVestingMessageHelper.addVestingErrors(withdrawalRequest,
                employeeVestingInformation);

        assertEquals("There should be 1 messages.", 1, withdrawalRequest.getMessages().size());

        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP).append("].")
                        .toString(), CollectionUtils.exists(withdrawalRequest.getMessages(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP)));

    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.common.WithdrawalVestingMessageHelper#addVestingErrors(com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest, com.manulife.pension.service.vesting.EmployeeVestingInformation)}.
     */
    @Test
    public void testAddVestingErrorsWithPreviousYearsOfServiceAndPlanYtdHoursWorkedNotProvied() {
        final WithdrawalVestingMessageHelper withdrawalVestingMessageHelper = WithdrawalVestingMessageHelper
                .getInstance();

        final EmployeeVestingInformation employeeVestingInformation =
            new MockEmployeeVestingInformation(VestingMessageType.PREVIOUS_YEARS_OF_SERVICE_AND_PLAN_YTD_HOURS_WORKED_NOT_PROVIDED);

        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();

        withdrawalVestingMessageHelper.addVestingErrors(withdrawalRequest,
                employeeVestingInformation);

        assertEquals("There should be 1 messages.", 1, withdrawalRequest.getMessages().size());

        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.VESTING_MISSING_EMPLOYEE_DATA).append("].").toString(),
                CollectionUtils.exists(withdrawalRequest.getMessages(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.VESTING_MISSING_EMPLOYEE_DATA)));

    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.common.WithdrawalVestingMessageHelper#addVestingErrors(com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest, com.manulife.pension.service.vesting.EmployeeVestingInformation)}.
     */
    @Test
    public void testAddVestingErrorsWithHireDateNotProvied() {
        final WithdrawalVestingMessageHelper withdrawalVestingMessageHelper = WithdrawalVestingMessageHelper
                .getInstance();

        final EmployeeVestingInformation employeeVestingInformation =
            new MockEmployeeVestingInformation(VestingMessageType.HIRE_DATE_NOT_PROVIDED);

        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();

        withdrawalVestingMessageHelper.addVestingErrors(withdrawalRequest,
                employeeVestingInformation);

        assertEquals("There should be 1 messages.", 1, withdrawalRequest.getMessages().size());

        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.VESTING_MISSING_EMPLOYEE_DATA).append("].").toString(),
                CollectionUtils.exists(withdrawalRequest.getMessages(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.VESTING_MISSING_EMPLOYEE_DATA)));

    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.common.WithdrawalVestingMessageHelper#addVestingErrors(com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest, com.manulife.pension.service.vesting.EmployeeVestingInformation)}.
     */
    @Test
    public void testAddVestingErrorsWithEmploymentStatusNotProvied() {
        final WithdrawalVestingMessageHelper withdrawalVestingMessageHelper = WithdrawalVestingMessageHelper
                .getInstance();

        final EmployeeVestingInformation employeeVestingInformation =
            new MockEmployeeVestingInformation(VestingMessageType.EMPLOYMENT_STATUS_NOT_PROVIDED);

        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();

        withdrawalVestingMessageHelper.addVestingErrors(withdrawalRequest,
                employeeVestingInformation);

        assertEquals("There should be 1 messages.", 1, withdrawalRequest.getMessages().size());

        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.VESTING_MISSING_EMPLOYEE_DATA).append("].").toString(),
                CollectionUtils.exists(withdrawalRequest.getMessages(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.VESTING_MISSING_EMPLOYEE_DATA)));

    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.common.WithdrawalVestingMessageHelper#addVestingErrors(com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest, com.manulife.pension.service.vesting.EmployeeVestingInformation)}.
     */
    @Test
    public void testAddVestingErrorsWithNonMappedCode() {
        final WithdrawalVestingMessageHelper withdrawalVestingMessageHelper = WithdrawalVestingMessageHelper
                .getInstance();

        final EmployeeVestingInformation employeeVestingInformation =
            new MockEmployeeVestingInformation(VestingMessageType.OTHER);

        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();

        withdrawalVestingMessageHelper.addVestingErrors(withdrawalRequest,
                employeeVestingInformation);

        assertEquals("There should be 1 messages.", 1, withdrawalRequest.getMessages().size());

        final Logger logger = Logger.getLogger(TestWithdrawalVestingMessageHelper.class);

        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.VESTING_GENERAL_MESSAGE_ALERT).append("].").toString(),
                CollectionUtils.exists(withdrawalRequest.getMessages(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.VESTING_GENERAL_MESSAGE_ALERT)));

    }
    
    private static class MockEmployeeVestingInformation
    implements EmployeeVestingInformation {
        
        private static final long serialVersionUID = 1L;
        
        private final TreeSet<VestingMessageType> errors;
        
        MockEmployeeVestingInformation(VestingMessageType error) {
            errors = new TreeSet<VestingMessageType>();
            errors.add(error);
        }
        public Integer getContractId() { return null; }
        public String getVestingServiceFeature() { return null; }
        public Date getVestingEffectiveDate() { return null; }
        public Map getMoneyTypeVestingPercentages() { return Collections.EMPTY_MAP; }
        public Set getErrors() { return errors; }
        public Map getErrorMap() { return null; }
        public VestingRetrievalDetails getRetrievalDetails() { return null; }

    }
    
}
