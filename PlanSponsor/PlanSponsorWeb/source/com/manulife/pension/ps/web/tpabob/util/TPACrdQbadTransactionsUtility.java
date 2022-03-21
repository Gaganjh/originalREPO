package com.manulife.pension.ps.web.tpabob.util;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.service.contract.valueobject.CrdQbadTransactions;
import com.manulife.util.render.NumberRender;
import com.manulife.util.render.SSNRender;

public class TPACrdQbadTransactionsUtility {

	private static final String LINE_BREAK = System.getProperty("line.separator");
	public static final String CSV_REPORT_NAME = "\"Other_Withdrawal_";
	public static final String CSV_FILE_EXTENSION = ".csv";
	public static final String TITLE = "Other Withdrawal and Associated Repayments Report";
	private static final String COMMA = ",";

	private static FastDateFormat dateFormatter = FastDateFormat.getInstance("MM_dd_yyyy");
	private static FastDateFormat monthDateFormatter = FastDateFormat.getInstance("MMM/dd/yyyy");
	private static final String ZERO_AMOUNT_STRING = "0.00";

	/**
	 * gets file name
	 * 
	 * @return
	 */
	public static String getCsvFileName(Date asOfDate,Integer contractNumber) {
		if(contractNumber==null) {
			return CSV_REPORT_NAME +"BoB_Report_"+dateFormatter.format(asOfDate) + CSV_FILE_EXTENSION;
		}
		else {
			return CSV_REPORT_NAME +"Report_"+contractNumber+"_"+dateFormatter.format(asOfDate) + CSV_FILE_EXTENSION;
		}
	}
	
	/**
	 * 
	 * Method to get TPA CRD QBD CSV File Data
	 * 
	 * @param asOfDate
	 * @param contractNumbers
	 * @return
	 * @throws SystemException
	 */
	public static byte[] getCrdQbdTransactionReportCSVFileData(Date asOfDate, List<Integer> contractNumbers)
			throws SystemException, RemoteException {

		StringBuffer buff = new StringBuffer();
		// Title
		buff.append(TITLE).append(LINE_BREAK);
		buff.append(LINE_BREAK);

		buff.append("Report Generated On: ").append(monthDateFormatter.format(asOfDate))
				.append(LINE_BREAK);
		buff.append(LINE_BREAK);

		buff.append("CRD - ").append(COMMA).append("Coronavirus-Related Distribution").append(LINE_BREAK);
		buff.append("QBA - ").append(COMMA).append("Qualified Birth/Adoption Distribution").append(LINE_BREAK);
		buff.append(LINE_BREAK);
		Map<Integer, List<CrdQbadTransactions>> crdQbaTransactionsData = ContractServiceDelegate.getInstance()
				.getCrdQbdTransactions(asOfDate, contractNumbers);
		for (Integer contractNumber : contractNumbers) {
			buff.append("Contract Number: ").append(contractNumber).append(LINE_BREAK);
			buff.append(LINE_BREAK);
			prepareHeader(buff);
			if (crdQbaTransactionsData.get(contractNumber) == null) {
				buff.append("No data available");
				buff.append(LINE_BREAK);
			} else {
				for (CrdQbadTransactions transactions : crdQbaTransactionsData.get(contractNumber)) {
					buff.append(transactions.getReason()).append(COMMA).append(transactions.getType()).append(COMMA)
							.append(transactions.getFirstName()).append(COMMA).append(transactions.getLastName())
							.append(COMMA)
							.append(SSNRender.format(transactions.getMaskedSSN(), StringUtils.EMPTY, true))
							.append(COMMA)
							.append(StringEscapeUtils.escapeCsv((NumberRender.formatByPattern(transactions.getAmount(),
									ZERO_AMOUNT_STRING, Constants.AMOUNT_FORMAT_TWO_DECIMALS_WITH_DOLLAR))))
							.append(COMMA).append(transactions.getTransactionNumber()).append(COMMA);
					if (transactions.getTransactionDate() != null) {
						buff.append(monthDateFormatter.format(transactions.getTransactionDate())).append(COMMA);
					} else {
						buff.append("").append(COMMA);
					}
					buff.append(transactions.getMoneyType()).append(COMMA);
					if (transactions.getChildFirstName() != null && "CRD".equals(transactions.getChildFirstName())) {

						buff.append("").append(COMMA);
					} else {
						buff.append(transactions.getChildFirstName() != null ? transactions.getChildFirstName() : "")
								.append(COMMA);
					}
					if (transactions.getChildBirthOrAdoptionDate() != null) {
						buff.append(monthDateFormatter.format(transactions.getChildBirthOrAdoptionDate())).append(COMMA);
					} else {
						buff.append("").append(COMMA);
					}

					buff.append(LINE_BREAK);
				}
			}
			buff.append(LINE_BREAK);
		}
		buff.append(LINE_BREAK);
		return buff.toString().getBytes();
	}

	private static void prepareHeader(StringBuffer buff) {
		buff.append("Reason").append(COMMA).append("Type").append(COMMA).append("First Name").append(COMMA)
				.append("Last Name").append(COMMA).append("Masked SSN").append(COMMA).append("Amount").append(COMMA)
				.append("Txn #").append(COMMA).append("Txn Date").append(COMMA)
				.append("Money Type").append(COMMA).append("Child First Name").append(COMMA)
				.append("Child Date of Birth/Adoption Date").append(LINE_BREAK);
	}
}
