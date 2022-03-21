package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;

/**
 * TODO TestWithdrawalIsAgeLessThanNormalRetirementAge Description.
 * 
 * @author glennpa
 */
public class TestWithdrawalIsAgeLessThanNormalRetirementAge {

    private static final Logger logger = Logger
            .getLogger(TestWithdrawalIsAgeLessThanNormalRetirementAge.class);

    /**
     * TODO setUpBeforeClass Description.
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * TODO tearDownAfterClass Description.
     * 
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * TODO setUp Description.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * TODO tearDown Description.
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    private WithdrawalRequest getWithdrawalRequestWithNoNormalRetirementAge() {
        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.DRAFT.getStatusCode());
        withdrawalRequest.getContractInfo().setNormalRetirementAge(null);

        return withdrawalRequest;
    }

    private WithdrawalRequest getWithdrawalRequestWithNormalRetirementAgeOf40() {
        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.DRAFT.getStatusCode());
        withdrawalRequest.getContractInfo().setNormalRetirementAge(new BigDecimal(40));

        return withdrawalRequest;
    }

    private WithdrawalRequest getWithdrawalRequestWithNormalRetirementAgeOfFiftyFiveAndAHalf() {
        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setStatusCode(WithdrawalStateEnum.DRAFT.getStatusCode());
        withdrawalRequest.getContractInfo().setNormalRetirementAge(new BigDecimal(55.5));

        return withdrawalRequest;
    }

    // private WithdrawalRequest getWithdrawalRequestWithNoNormalRetirementAgeOf65() {
    // final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
    // withdrawalRequest.getContractInfo().setNormalRetirementAge(65);
    //
    // return withdrawalRequest;
    // }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.domain.Withdrawal#isAgeLessThanNormalRetirementAge(java.util.Date, java.util.Date)}.
     */
    @Test
    public void testIsAgeLessThanNormalRetirementAgeWithAgeBeforeRetirementDateAndNoNormalValue() {

        final WithdrawalRequest withdrawalRequest = getWithdrawalRequestWithNoNormalRetirementAge();
        Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        final Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        final Date dateOfBirth = DateUtils.addYears(today, -25);
        final Date retirementDate = today;

        assertTrue("Age should be before the retirement age.", withdrawal
                .isAgeLessThanNormalRetirementAge(dateOfBirth, retirementDate));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.domain.Withdrawal#isAgeLessThanNormalRetirementAge(java.util.Date, java.util.Date)}.
     */
    @Test
    public void testIsAgeLessThanNormalRetirementAgeWithAgeAfterRetirementDateAndNoNormalValue() {

        final WithdrawalRequest withdrawalRequest = getWithdrawalRequestWithNoNormalRetirementAge();
        Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        final Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        final Date dateOfBirth = DateUtils.addYears(today, -75);
        final Date retirementDate = today;

        assertFalse("Age should be after the retirement age.", withdrawal
                .isAgeLessThanNormalRetirementAge(dateOfBirth, retirementDate));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.domain.Withdrawal#isAgeLessThanNormalRetirementAge(java.util.Date, java.util.Date)}.
     */
    @Test
    public void testIsAgeLessThanNormalRetirementAgeWithAgeOnRetirementDateAndNoNormalValue() {

        final WithdrawalRequest withdrawalRequest = getWithdrawalRequestWithNoNormalRetirementAge();
        Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        final Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date dateOfBirth = DateUtils.addYears(today, -65);
        dateOfBirth = DateUtils.addDays(dateOfBirth, 0);
        final Date retirementDate = today;

        assertFalse("Age should be equal to the retirement age.", withdrawal
                .isAgeLessThanNormalRetirementAge(dateOfBirth, retirementDate));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.domain.Withdrawal#isAgeLessThanNormalRetirementAge(java.util.Date, java.util.Date)}.
     */
    @Test
    public void testIsAgeLessThanNormalRetirementAgeWithAgeOneDayYoungerThanRetirementDateAndNoNormalValue() {

        final WithdrawalRequest withdrawalRequest = getWithdrawalRequestWithNoNormalRetirementAge();
        Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        final Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date dateOfBirth = DateUtils.addYears(today, -65);
        dateOfBirth = DateUtils.addDays(dateOfBirth, 1);
        final Date retirementDate = today;

        assertTrue("Age should be one day younger than the retirement age.", withdrawal
                .isAgeLessThanNormalRetirementAge(dateOfBirth, retirementDate));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.domain.Withdrawal#isAgeLessThanNormalRetirementAge(java.util.Date, java.util.Date)}.
     */
    @Test
    public void testIsAgeLessThanNormalRetirementAgeWithAgeBeforeRetirementDateAndA40YearNormalRetirementAge() {

        final WithdrawalRequest withdrawalRequest = getWithdrawalRequestWithNormalRetirementAgeOf40();
        Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        final Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        final Date dateOfBirth = DateUtils.addYears(today, -25);
        final Date retirementDate = today;

        assertTrue("Age should be before the retirement age.", withdrawal
                .isAgeLessThanNormalRetirementAge(dateOfBirth, retirementDate));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.domain.Withdrawal#isAgeLessThanNormalRetirementAge(java.util.Date, java.util.Date)}.
     */
    @Test
    public void testIsAgeLessThanNormalRetirementAgeWithAgeAfterRetirementDateAndA40YearNormalRetirementAge() {

        final WithdrawalRequest withdrawalRequest = getWithdrawalRequestWithNormalRetirementAgeOf40();
        Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        final Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        final Date dateOfBirth = DateUtils.addYears(today, -45);
        final Date retirementDate = today;

        assertFalse("Age should be after the retirement age.", withdrawal
                .isAgeLessThanNormalRetirementAge(dateOfBirth, retirementDate));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.domain.Withdrawal#isAgeLessThanNormalRetirementAge(java.util.Date, java.util.Date)}.
     */
    @Test
    public void testIsAgeLessThanNormalRetirementAgeWithAgeOnRetirementDateAndA40YearNormalRetirementAge() {

        final WithdrawalRequest withdrawalRequest = getWithdrawalRequestWithNormalRetirementAgeOf40();
        Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        final Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date dateOfBirth = DateUtils.addYears(today, -40);
        dateOfBirth = DateUtils.addDays(dateOfBirth, 0);
        final Date retirementDate = today;

        assertFalse("Age should be equal to the retirement age.", withdrawal
                .isAgeLessThanNormalRetirementAge(dateOfBirth, retirementDate));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.domain.Withdrawal#isAgeLessThanNormalRetirementAge(java.util.Date, java.util.Date)}.
     */
    @Test
    public void testIsAgeLessThanNormalRetirementAgeWithAgeBeforeRetirementDateAndAFiftyFiveAndAHalfYearNormalRetirementAge() {

        final WithdrawalRequest withdrawalRequest = getWithdrawalRequestWithNormalRetirementAgeOfFiftyFiveAndAHalf();
        Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        final Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date dateOfBirth = DateUtils.addYears(today, -55);
        dateOfBirth = DateUtils.addMonths(dateOfBirth, -6);
        dateOfBirth = DateUtils.addDays(dateOfBirth, 1);
        final Date retirementDate = today;

        assertTrue("Age should be before the retirement age.", withdrawal
                .isAgeLessThanNormalRetirementAge(dateOfBirth, retirementDate));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.domain.Withdrawal#isAgeLessThanNormalRetirementAge(java.util.Date, java.util.Date)}.
     */
    @Test
    public void testIsAgeLessThanNormalRetirementAgeWithAgeAfterRetirementDateAndAFiftyFiveAndAHalfYearNormalRetirementAge() {

        final WithdrawalRequest withdrawalRequest = getWithdrawalRequestWithNormalRetirementAgeOfFiftyFiveAndAHalf();
        Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        final Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date dateOfBirth = DateUtils.addYears(today, -55);
        dateOfBirth = DateUtils.addMonths(dateOfBirth, -6);
        final Date retirementDate = today;

        assertFalse("Age should be after the retirement age.", withdrawal
                .isAgeLessThanNormalRetirementAge(dateOfBirth, retirementDate));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.domain.Withdrawal#isAgeLessThanNormalRetirementAge(java.util.Date, java.util.Date)}.
     */
    @Test
    public void testIsAgeLessThanNormalRetirementAgeWithAgeOnRetirementDateAndAFiftyFiveAndAHalfYearNormalRetirementAge() {

        final WithdrawalRequest withdrawalRequest = getWithdrawalRequestWithNormalRetirementAgeOfFiftyFiveAndAHalf();
        Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        final Date today = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date dateOfBirth = DateUtils.addYears(today, -55);
        dateOfBirth = DateUtils.addMonths(dateOfBirth, -6);
        dateOfBirth = DateUtils.addDays(dateOfBirth, 0);
        final Date retirementDate = today;

        assertFalse("Age should be equal to the retirement age.", withdrawal
                .isAgeLessThanNormalRetirementAge(dateOfBirth, retirementDate));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.domain.Withdrawal#isAgeLessThanNormalRetirementAge(java.util.Date, java.util.Date)}.
     */
    @Test
    public void testIsAgeLessThanNormalRetirementAgeWithLeapYearBirthDateAndNoNormalValue() {

        final WithdrawalRequest withdrawalRequest = getWithdrawalRequestWithNoNormalRetirementAge();
        Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        Calendar leapDay = Calendar.getInstance();
        leapDay.set(2000, Calendar.FEBRUARY, 29);

        Date dateOfBirth = leapDay.getTime();
        dateOfBirth = DateUtils.addDays(dateOfBirth, 0);

        Date retirementDate = DateUtils.addYears(leapDay.getTime(), 65);
        retirementDate = DateUtils.addDays(retirementDate, 0);

        assertFalse("Age should be equal to the retirement age.", withdrawal
                .isAgeLessThanNormalRetirementAge(dateOfBirth, retirementDate));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.domain.Withdrawal#isAgeLessThanNormalRetirementAge(java.util.Date, java.util.Date)}.
     */
    @Test
    public void testIsAgeLessThanNormalRetirementAgeWithLeapYearBirthDateAndA40YearNormalRetirementAge() {

        final WithdrawalRequest withdrawalRequest = getWithdrawalRequestWithNormalRetirementAgeOf40();
        Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        Calendar leapDay = Calendar.getInstance();
        leapDay.set(2060, Calendar.FEBRUARY, 29);

        // The year 2100, is a skip of the regular leap year pattern. (every 100y, skip a leap,
        // every 400 skip 100y skip of the leap).

        Date dateOfBirth = leapDay.getTime();
        dateOfBirth = DateUtils.addDays(dateOfBirth, 0);

        Date retirementDate = DateUtils.addYears(leapDay.getTime(), 40);
        retirementDate = DateUtils.addDays(retirementDate, 0);

        assertFalse("Age should be equal to the retirement age.", withdrawal
                .isAgeLessThanNormalRetirementAge(dateOfBirth, retirementDate));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.domain.Withdrawal#isAgeLessThanNormalRetirementAge(java.util.Date, java.util.Date)}.
     */
    @Test
    public void testIsAgeLessThanNormalRetirementAgeWithNonLeapYearBirthDateAndA40YearNormalRetirementAge() {

        final WithdrawalRequest withdrawalRequest = getWithdrawalRequestWithNormalRetirementAgeOf40();
        Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        Calendar leapDay = Calendar.getInstance();
        leapDay.set(2000, Calendar.FEBRUARY, 29);

        Date dateOfBirth = leapDay.getTime();
        dateOfBirth = DateUtils.addDays(dateOfBirth, 3);

        Date retirementDate = DateUtils.addYears(leapDay.getTime(), 40);
        retirementDate = DateUtils.addDays(retirementDate, 2);

        assertTrue("Age should be less than the retirement age.", withdrawal
                .isAgeLessThanNormalRetirementAge(dateOfBirth, retirementDate));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.domain.Withdrawal#isAgeLessThanNormalRetirementAge(java.util.Date, java.util.Date)}.
     */
    @Test
    public void testIsAgeLessThanNormalRetirementAgeWithLeapYearBirthDateMinusADayAndA40YearNormalRetirementAge() {

        final WithdrawalRequest withdrawalRequest = getWithdrawalRequestWithNormalRetirementAgeOf40();
        Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        Calendar leapDay = Calendar.getInstance();
        leapDay.set(2000, Calendar.FEBRUARY, 29);

        Date dateOfBirth = leapDay.getTime();
        dateOfBirth = DateUtils.addDays(dateOfBirth, 0);

        Date retirementDate = DateUtils.addYears(leapDay.getTime(), 40);
        retirementDate = DateUtils.addDays(retirementDate, 1);

        assertFalse("Age should be older than the retirement age.", withdrawal
                .isAgeLessThanNormalRetirementAge(dateOfBirth, retirementDate));
    }

}
