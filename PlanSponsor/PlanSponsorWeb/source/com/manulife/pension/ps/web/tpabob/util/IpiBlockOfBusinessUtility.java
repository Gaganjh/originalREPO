package com.manulife.pension.ps.web.tpabob.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.content.ContentConstants;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.fee.util.Constants.ContractIndividualExpense;
import com.manulife.pension.service.fee.valueobject.ContractCustomizedFeeVO;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.IpiBoBHelper.IPIBOBCSVHEADER;

/**
 * This class is a Helper class for TPA - IPI Block of Business page.
 * 
 * @author Ramakrishna
 * 
 */
public class IpiBlockOfBusinessUtility implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String LINE_BREAK = System.getProperty("line.separator");
	public static final String CSV_REPORT_NAME = "\"IPI_block_business_rpt_";
	public static final String CSV_FILE_EXTENSION = ".csv";
	public static final String TITLE = "404a-5 Important Plan Information Details";
	private static final String COMMA = ",";
	private static final String AESTRIK = "*";

	public static byte[] getIpiBoBCSVFileData(Map<Integer,String> tpaFirms, String userProfileId) throws SystemException {

		StringBuffer buff = new StringBuffer(255);
		//Title
		buff.append(TITLE).append(LINE_BREAK);
		buff.append("As of: ").append(new SimpleDateFormat("MMM/dd/yyyy").format(new Date())).append(LINE_BREAK);
		
		// Fee Usage Text
		buff.append(ContentHelper.getContentText(ContentConstants.TPA_BOB_REPORT_FEE_USAGE_TEXT,
				ContentTypeManager.instance().MISCELLANEOUS, null)).append(LINE_BREAK);

		buff.append(buildCsvHeaders());
		buff.append(LINE_BREAK);
		
		String companyCode = Constants.COMPANY_NAME_NY.equalsIgnoreCase(Environment.getInstance().getSiteLocation()) 
 		? Constants.COMPANY_ID_NY : Constants.COMPANY_ID_US;
		
		// Get eligible contracts based on profileId.
		List<Integer> contractList = ContractServiceDelegate.getInstance().getEligibleContractsforIPIBoB(userProfileId,companyCode);
		
		if(contractList != null && !contractList.isEmpty()){
			// Build IPI BoB data for eligible contracts
		Map<Integer, Map<IPIBOBCSVHEADER, String>> ipiBoBDataMap = FundServiceDelegate.getInstance().getIpiBobCsvData(contractList);

		for (Integer contractId : contractList) {

			Map<IPIBOBCSVHEADER, String> contractData = ipiBoBDataMap.get(contractId);
			
			contractData.put(IPIBOBCSVHEADER.TPAFIRMNAME, StringUtils.isNotBlank(tpaFirms.get(Integer.valueOf(contractData.get(IPIBOBCSVHEADER.TPAID)))) ? 
									"\""+tpaFirms.get(Integer.valueOf(contractData.get(IPIBOBCSVHEADER.TPAID)))+"\"" : StringUtils.EMPTY);

			for (IPIBOBCSVHEADER field : IPIBOBCSVHEADER.values()) {
				buff.append(
						contractData.get(field) == null ? "" : escapeField(contractData
								.get(field))).append(",");
			}
			 buff.append(LINE_BREAK);
		}
		buff.append(escapeField(ContentHelper.getContentText(
                ContentConstants.TPA_FEE_BOB_DISCLAIMER,
                ContentTypeManager.instance().MISCELLANEOUS, null))).append(LINE_BREAK);
		} else {
			buff.append("There are no plans applicable for this report.");
		}
		
		return buff.toString().getBytes();
    }

	/**
	 * build 3 level headers
	 * 
	 * @param headers
	 * @return String
	 * @throws SystemException
	 */
	private static String buildCsvHeaders() throws SystemException {
		
		Map<IPIBOBCSVHEADER, String> headers = getCsvHeaders();
		
		StringBuffer buff = new StringBuffer(255);
		// Build top level header 
		for (IPIBOBCSVHEADER header : headers.keySet()) {
		    buff.append(header.getTopLevelHeader()!= null ? escapeField(header.getTopLevelHeader().getValue()) : StringUtils.EMPTY);
			buff.append(COMMA);
		}
		buff.append(LINE_BREAK);
		
		// Build Second level header 
		for (IPIBOBCSVHEADER header : headers.keySet()) {
			buff.append(header.getLevel2Header()!= null ? escapeField(header.getLevel2Header().getValue()) : StringUtils.EMPTY);
			buff.append(COMMA);
		}
		buff.append(LINE_BREAK);
		
		
		// Build Main header
		for (Entry<IPIBOBCSVHEADER, String> header : headers.entrySet()) {
			buff.append(escapeField(header.getValue()));
			buff.append(COMMA);
		}
		
		return buff.toString();
	}
	
	/**
	 * gets file name
	 * 
	 * @return
	 */
	public static String getCsvFileName(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy");
		return CSV_REPORT_NAME+dateFormat.format(new Date())+CSV_FILE_EXTENSION;
	}
	
	/**
	 * 
	 * gets updated csv header map
	 * 
	 * @return Map<IPIBOBCSVHEADER , Set<String>>
	 * @throws SystemException
	 */
	private static Map<IPIBOBCSVHEADER , String> getCsvHeaders() throws SystemException {
		
		Map<IPIBOBCSVHEADER , String> headers = new LinkedHashMap<IPIBOBCSVHEADER , String>();
		
        Map<ContractIndividualExpense, ContractCustomizedFeeVO> feeList = FeeServiceDelegate.
        		getInstance(Constants.PS_APPLICATION_ID).getStandardTpaFeeNames();
		
		// Build Main header
		for (IPIBOBCSVHEADER header : IPIBOBCSVHEADER.values()) {
			
			switch(header)  {
			case LOAN_SETUP_FEE:
				headers.put(header, feeList.get(
						ContractIndividualExpense.LOAN_SET_UP)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case LOAN_MAINTENANCE:
				headers.put(header, feeList.get(
						ContractIndividualExpense.LOAN_MAINTENANCE)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case TERMINATION_OF_EMPLOYMENT:
				headers.put(header, feeList.get(
						ContractIndividualExpense.TERMINATION_OF_EMPLOYMENT)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case RETIREMENT:
				headers.put(header, feeList.get(
						ContractIndividualExpense.RETIREMENT)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case HARDSHIPFEE:
				headers.put(header, feeList.get(
						ContractIndividualExpense.HARDSHIP)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case PRE_RETIREMENT:
				headers.put(header, feeList.get(
						ContractIndividualExpense.PRE_RETIREMENT)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case IN_SERVICE_AGE_59_AND_HALF:
				headers.put(header, feeList.get(
						ContractIndividualExpense.IN_SERVICE_AGE_59_AND_HALF)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case MANDATORY_DISTRIBUTION:
				headers.put(header, feeList.get(
						ContractIndividualExpense.MANDATORY_DISTRIBUTION)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case QUALIFIED_DOMESTIC_RELATIONS_ORDER:
				headers.put(header, feeList.get(
						ContractIndividualExpense.QUALIFIED_DOMESTIC_RELATIONS_ORDER)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case DEATH:
				headers.put(header, feeList.get(
						ContractIndividualExpense.DEATH)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case INDIVIDUAL_JH_DEATH_WITHDRAWAL_FEE:
				headers.put(header, "JH Death Withdrawal");
				break;
				
			case INDIVIDUAL_JH_DISABILITY_WITHDRAWAL_FEE:
				headers.put(header, "JH Disability Withdrawal");
				break;
				
			case INDIVIDUAL_JH_HARDSHIP_WITHDRAWAL_FEE:
				headers.put(header,"JH Hardship Withdrawal");
				break;
				
			case INDIVIDUAL_JH_EMPLOYEE_ROLLOVER_WITHDRAWAL_FEE:
				headers.put(header, "JH Employee Rollover Withdrawal");
				break;
				
			case INDIVIDUAL_JH_MINREQUIRED_DIST_WITHDRAWAL_FEE:
				headers.put(header, "JH Min-Required Dist Withdrawal");
				break;
				
			case INDIVIDUAL_JH_MANDATORY_WITHDRAWAL_FEE:
				headers.put(header, "JH Mandatory Withdrawal");
				break;
				
			case INDIVIDUAL_JH_PRERETIREMENT_WITHDRAWAL_FEE:
				headers.put(header, "JH Pre-Retirement Withdrawal");
				break;
				
			case INDIVIDUAL_JH_QDRO_WITHDRAWAL_FEE:
				headers.put(header, "JH QDRO Withdrawal");
				break;
				
			case INDIVIDUAL_JH_RETIREMENT_WITHDRAWAL_FEE:
				headers.put(header,"JH Retirement Withdrawal");
				break;
				
			case INDIVIDUAL_JH_TERMINATION_WITHDRAWAL_FEE:
				headers.put(header, "JH Termination Withdrawal");
				break;
				
			case INDIVIDUAL_JH_VOLUNTARY_WITHDRAWAL_FEE:
				headers.put(header, "JH Voluntary Withdrawal");
				break;
				
						
			case DISABILITY:
				headers.put(header, feeList.get(
						ContractIndividualExpense.DISABILITY)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case REQUIRED_MINIMUM_DISTRIBUTION:
				headers.put(header, feeList.get(
						ContractIndividualExpense.REQUIRED_MINIMUM_DISTRIBUTION)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case VOLUNTARY_CONTRIBUTIONS:
				headers.put(header, feeList.get(
						ContractIndividualExpense.VOLUNTARY_CONTRIBUTIONS)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case AUTOMATIC_CONTRIBUTIONS:
				headers.put(header, feeList.get(
						ContractIndividualExpense.AUTOMATIC_CONTRIBUTIONS)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case EXCESS_CONTRIBUTIONS:
				headers.put(header, feeList.get(
						ContractIndividualExpense.EXCESS_CONTRIBUTIONS)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case EXCESS_DEFERRALS:
				headers.put(header, feeList.get(
						ContractIndividualExpense.EXCESS_DEFERRALS)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case EXCESS_ANNUAL_ADDITIONS:
				headers.put(header, feeList.get(
						ContractIndividualExpense.EXCESS_ANNUAL_ADDITIONS)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case EMPLOYEE_ROLLOVER:
				headers.put(header, feeList.get(
						ContractIndividualExpense.EMPLOYEE_ROLLOVER)
						.getFeeDescription().concat(AESTRIK));
				break;
				
			case IN_PLAN_ROTH_ROLLOVER:
				headers.put(header, feeList.get(
						ContractIndividualExpense.IN_PLAN_ROTH_ROLLOVER)
						.getFeeDescription().concat(AESTRIK));
				break;

			default :
				headers.put(header, header.getColumnName());
				break;
			}
		}
		return headers;
	}
	
	/**
	 * 
	 * @param field
	 * @return
	 */
	private static String escapeField(String field) {
		if(StringUtils.isEmpty(field)) {
			return StringUtils.EMPTY;
		}
		if (field.indexOf(",") != -1 &&
				field.indexOf("\"") != 0 ) {
			StringBuffer newField = new StringBuffer();
			newField = newField.append("\"").append(field).append("\"");
			return newField.toString();
		} else {
			return field;
		}
	}
}
