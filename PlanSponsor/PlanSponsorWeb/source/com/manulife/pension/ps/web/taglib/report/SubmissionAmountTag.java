package com.manulife.pension.ps.web.taglib.report;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.taglib.report.AbstractReportTag;
import com.manulife.pension.ps.service.report.submission.valueobject.SubmissionErrorCollection;
import com.manulife.pension.ps.service.submission.util.SubmissionErrorHelper;
import com.manulife.pension.submission.SubmissionError;

public class SubmissionAmountTag extends AbstractReportTag {

	private static final long serialVersionUID = 1L;

	private SubmissionErrorCollection errors;
	private String row = "0";
	private String codes;
	private String value;
	private String field;
	private String amount;

	public int doStartTag() throws JspException {
		try {
			if (StringUtils.isEmpty(amount)) {
				pageContext.getOut().print("&nbsp;");
			} else {
				// we need to check if there are multiple fields to check at
				// the
				// same time
				ArrayList<String> codesToCheck = new ArrayList<String>();
				StringTokenizer tokenizer = new StringTokenizer(codes, ",");
				while (tokenizer.hasMoreTokens()) {
					codesToCheck.add(tokenizer.nextToken());
				}

				boolean isError = SubmissionErrorHelper.evaluateMatchedField(
						field, value, Integer.parseInt(row), errors,
						codesToCheck, new Predicate() {
							public boolean evaluate(Object object) {
								SubmissionError error = (SubmissionError) object;
								return SubmissionErrorHelper.isError(error);
							}
						});

				//if (!isError) {
					try {
						BigDecimal number = new BigDecimal(amount);
						DecimalFormat df = new DecimalFormat("#.##############");
						pageContext.getOut().print(df.format(number) + " %");
						return SKIP_BODY;
					} catch (NumberFormatException e) {
						// do nothing, print out the amount as-is.
					}
				//}
				
				/*
				 * Just print the amount as-is
				 */
				pageContext.getOut().print(amount);
			}
		} catch (MalformedURLException e) {
			throw new JspException(e);
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

	/**
	 * @return Returns the errors.
	 */
	public SubmissionErrorCollection getErrors() {
		return errors;
	}

	/**
	 * @param errors
	 *            The errors to set.
	 */
	public void setErrors(SubmissionErrorCollection errors) {
		this.errors = errors;
	}

	/**
	 * @return Returns the row.
	 */
	public String getRow() {
		return row;
	}

	/**
	 * @param row
	 *            The row to set.
	 */
	public void setRow(String row) {
		this.row = row;
	}

	/**
	 * @return Returns the codes.
	 */
	public String getCodes() {
		return codes;
	}

	/**
	 * @param codes
	 *            The codes to set.
	 */
	public void setCodes(String codes) {
		this.codes = codes;
	}

	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}
