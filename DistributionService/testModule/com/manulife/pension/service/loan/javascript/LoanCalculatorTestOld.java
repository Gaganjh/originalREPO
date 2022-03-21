package com.manulife.pension.service.loan.javascript;

import java.io.FileReader;
import java.io.Reader;
import java.math.BigDecimal;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class LoanCalculatorTestOld {

	private static final String NUMBER_UTILS_JS_FILE = "D:/cvs/common/assets/javascript/NumberUtils.js";
	private static final String DATE_UTILS_JS_FILE = "D:/cvs/common/assets/javascript/DateUtils.js";
	private static final String LOAN_CALCULATOR_JS_FILE = "D:/cvs/common/assets/javascript/loan/LoanCalculator.js";

	private Context context = null;
	private Scriptable scope = null;

	@Before
	public void setUp() throws Exception {
		context = Context.enter();
		scope = context.initStandardObjects();
		Reader dateUtilsJsReader = new FileReader(DATE_UTILS_JS_FILE);
		Reader numberUtilsJsReader = new FileReader(NUMBER_UTILS_JS_FILE);
		Reader loanCalculatorJsReader = new FileReader(LOAN_CALCULATOR_JS_FILE);

		context.evaluateReader(scope, dateUtilsJsReader, DATE_UTILS_JS_FILE, 1,
				null);
		context.evaluateReader(scope, numberUtilsJsReader,
				NUMBER_UTILS_JS_FILE, 1, null);
		context.evaluateReader(scope, loanCalculatorJsReader,
				LOAN_CALCULATOR_JS_FILE, 1, null);
	}

	@After
	public void tearDown() throws Exception {
		Context.exit();
	}

	@Test
	public void testCalculateInterestPerPaymentPeriod() throws Exception {
		Object result = context.evaluateString(scope,
				"calculateInterestPerPaymentPeriod('5.75', '26')", "<test>", 1,
				null);
		BigDecimal interest = new BigDecimal(Context.toString(result)).setScale(
				4, BigDecimal.ROUND_HALF_UP);

		Assert.assertEquals("Interest rate is not the same", "0.0022", interest
				.toString());
	}

	@Test
	public void testCalculateRepaymentAmount() throws Exception {
		Object result = context.evaluateString(scope,
				"calculateRepaymentAmount('5000', '5', '9.5', '26', '14')",
				"<test>", 1, null);
		Assert.assertEquals("Discounted repayment amount is not correct",
				"48.39", Context.toString(result));
	}
}
