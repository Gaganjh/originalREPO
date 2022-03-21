package com.manulife.pension.ps.web.tpabob.util;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.CommonEnvironment;import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.util.ReportDownloadHelper;
import com.manulife.pension.service.contract.valueobject.TPAMissedLoanRepaymentBobDetails;
import com.manulife.pension.service.contract.valueobject.TPAMissedLoanRepaymentContractDetails;
import com.manulife.pension.service.contract.valueobject.TPAMissedLoanRepaymentContractDetails.TPAMissedLoanRepaymentBobParticipantElement;
import com.manulife.util.render.RenderConstants;
import com.manulife.util.render.SSNRender;

/**
 * This class is a Helper class for TPA - Missed Loan Re-payment Report Block Of
 * Business.
 * 
 * @author pottasw
 * 
 */
public class TPAMissedLoanRepaymentReportBlockOfBusinessUtility {

	private static final String LINE_BREAK = System
			.getProperty("line.separator");
	public static final String CSV_REPORT_NAME = "\"Summary_of_Missed_Loan_Repayments_";
	public static final String CSV_FILE_EXTENSION = ".csv";
	public static final String TITLE = "Summary of Missed Loan Repayments";
	private static final String COMMA = ",";

	private static FastDateFormat dataFormatter_MEDIUM_MDY_SLASHED = FastDateFormat
			.getInstance(RenderConstants.MEDIUM_MDY_SLASHED);
	private static FastDateFormat dateFormatter = FastDateFormat
			.getInstance("MM_dd_yyyy");

	/**
	 * 
	 * Method to get TPA Missed Loan Repayment CSV File Data
	 * 
	 * @param userProfile
	 * @return
	 * @throws SystemException
	 */
	public static byte[] getTPAMissedLoanRepaymentCSVFileData(long userProfileId, Date asOfDate)
			throws SystemException {

		StringBuffer buff = new StringBuffer();
		// Title
		buff.append(TITLE).append(LINE_BREAK);
		buff.append(LINE_BREAK);

		buff.append("Report As of: ")
				.append(dataFormatter_MEDIUM_MDY_SLASHED.format(asOfDate))
				.append(LINE_BREAK);
		buff.append(LINE_BREAK);

		String companyId = CommonEnvironment.getInstance().getSiteLocation();
		String locationId = StringUtils.equals(companyId,
				Constants.SITEMODE_USA) ? Constants.COUNTRY_USA_CODE : Constants.COUNTRY_NY_CODE;
		
		Map<Integer, TPAMissedLoanRepaymentBobDetails> tpaMissedLoanRepaymentBobDetailsMap = ContractServiceDelegate
				.getInstance().getTpaMissedLoanRepaymentBoBDetails(
						userProfileId, locationId);

		if (!tpaMissedLoanRepaymentBobDetailsMap.isEmpty()) {
			for (Integer tpaFirmId : tpaMissedLoanRepaymentBobDetailsMap
					.keySet()) {

				TPAMissedLoanRepaymentBobDetails tpaMissedLoanRepaymentBobDetails = tpaMissedLoanRepaymentBobDetailsMap
						.get(tpaFirmId);

				buff.append("Third Party Administrator: ").append(tpaFirmId)
						.append(COMMA);
				buff.append(tpaMissedLoanRepaymentBobDetails.getTpaFirmName())
						.append(LINE_BREAK);
				buff.append(LINE_BREAK);

				Map<Integer, TPAMissedLoanRepaymentContractDetails> contractDetails = tpaMissedLoanRepaymentBobDetails
						.getContractDetails();

				buff.append("Number of contracts").append(LINE_BREAK);
				buff.append("with missed loan repayments").append(COMMA);
				buff.append(contractDetails.size()).append(LINE_BREAK);
				buff.append(LINE_BREAK);

				Set<String> participantSsnSet = new HashSet<String>();
				
				boolean isMaskedParticipantSsn = ReportDownloadHelper.isMaskedSsnByTPAUserAndFirmId(
						userProfileId, tpaFirmId);

				StringBuffer participantDetailsBuffer = new StringBuffer();
				for (TPAMissedLoanRepaymentContractDetails contractdetail : contractDetails
						.values()) {

					participantDetailsBuffer.append("Contract number: ");
					participantDetailsBuffer.append(
							contractdetail.getContractId()).append(COMMA);
					participantDetailsBuffer.append("\"Contract name: ")
							.append(contractdetail.getContractName() + "\"")
							.append(LINE_BREAK);
					participantDetailsBuffer.append(LINE_BREAK);

					participantDetailsBuffer
							.append(buildCsvParticipantDetailHeader());

					for (Map<TPAMissedLoanRepaymentBobParticipantElement, String> particpantDetails : contractdetail
							.getParticipantDetails()) {

						participantDetailsBuffer
								.append(buildCsvParticipantDetailSection(
										particpantDetails, isMaskedParticipantSsn));

						participantSsnSet
								.add(particpantDetails
										.get(TPAMissedLoanRepaymentBobParticipantElement.PARTICIPANT_SSN));
					}

					participantDetailsBuffer.append(LINE_BREAK);
				}

				buff.append("Number of participants").append(LINE_BREAK);
				buff.append("with missed loan repayments").append(COMMA);
				buff.append(participantSsnSet.size()).append(LINE_BREAK);
				buff.append(LINE_BREAK);

				buff.append(participantDetailsBuffer);
			}
		}

		return buff.toString().getBytes();
	}

	private static String buildCsvParticipantDetailHeader()
			throws SystemException {

		StringBuffer buff = new StringBuffer();

		// Build Particpant header
		for (TPAMissedLoanRepaymentBobParticipantElement element : TPAMissedLoanRepaymentBobParticipantElement
				.values()) {
			buff.append(element.getColumnName());
			buff.append(COMMA);
		}
		buff.append(LINE_BREAK);

		return buff.toString();
	}

	private static String buildCsvParticipantDetailSection(
			Map<TPAMissedLoanRepaymentBobParticipantElement, String> particpantDetails, boolean isMaskedParticipantSsn) throws SystemException {

		StringBuffer buff = new StringBuffer();

		for (TPAMissedLoanRepaymentBobParticipantElement field : TPAMissedLoanRepaymentBobParticipantElement
				.values()) {
			if (TPAMissedLoanRepaymentBobParticipantElement.PARTICIPANT_SSN
					.equals(field)) {
				if ( isMaskedParticipantSsn ) {
					buff.append(SSNRender.format(particpantDetails.get(field),
							null, true));
				} else {
					buff.append(SSNRender.format(particpantDetails.get(field),
							null, false));
				}
			} else {
				buff.append(particpantDetails.get(field) == null ? ""
						: particpantDetails.get(field));
			}
			buff.append(COMMA);
		}
		buff.append(LINE_BREAK);
		return buff.toString();
	}

	/**
	 * gets file name
	 * 
	 * @return
	 */
	public static String getCsvFileName(Date asOfDate) {
		return CSV_REPORT_NAME + dateFormatter.format(asOfDate)
				+ CSV_FILE_EXTENSION;
	}

}
