package com.manulife.pension.ps.service.report.feeSchedule.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.TPAFeeScheduleContract;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.TPAFeeScheduleContractSearchReportData;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class TPAFeeScheduleContractSearchDAO extends BaseDatabaseDAO {

	private static final String className = TPAFeeScheduleContractSearchDAO.class.getName();
	private static final Logger logger = Logger.getLogger(TPAFeeScheduleContractSearchDAO.class);

	private static String GET_CONTRACT_LIST = 
			
			" WITH temp  "
			+ "  AS (SELECT contract_id, " 
		    + "             third_party_admin_id, " 
		    + "             Max(created_ts) AS created_ts " 
		    + "      FROM   psw100.contract_custom_tpa_fee_hist CCTFH " 
		    + "      WHERE  third_party_admin_id = ?  "
		    + "             AND fee_category_code <> 'NT'  "
		    + "             AND created_ts IN (SELECT Max(created_ts) " 
		    + "                                FROM   psw100.contract_custom_tpa_fee_hist " 
		    + "                                WHERE  fee_code = CCTFH.fee_code  "
		    + "                                       AND third_party_admin_id =  "
		    + "                                           CCTFH.third_party_admin_id " 
		    + "                                       AND contract_id = CCTFH.contract_id " 
		    + "                                       AND fee_category_code =  "
		    + "                                           CCTFH.fee_category_code) " 
		    + "      GROUP  BY contract_id,  "
		    + "                third_party_admin_id), " 
		    + "  FEE_SCHEDULE_TYPE  "
		    + "  AS (SELECT DISTINCT contract_id, " 
		    + "                      'customOrReset' AS fee_schedule_type " 
		    + "      FROM   psw100.contract_custom_tpa_fee_hist CCTFH " 
		    + "      WHERE ( (deleted_ind='Y' and FEE_CATEGORY_CODE='TP') or deleted_ind = 'N' )   "
		    + "             AND third_party_admin_id = ? " 
		    + "             AND fee_category_code <> 'NT'  "
		    + "             AND created_ts IN (SELECT Max(created_ts) " 
		    + "                                FROM   psw100.contract_custom_tpa_fee_hist " 
		    + "                                WHERE  fee_code = CCTFH.fee_code  "
		    + "                                       AND third_party_admin_id =  "
		    + "                                           CCTFH.third_party_admin_id " 
		    + "                                       AND contract_id = CCTFH.contract_id  "
		    + "                                      AND fee_category_code =  "
		    + "                                           CCTFH.fee_category_code)), " 
			+ " base "
			+ "     AS (SELECT CS.contract_name, "
			+ "                CS.contract_id, "
			+ "                cs.third_party_admin_id AS TPA_FIRM_ID, "
			+ "                CASE "
			+ "                 WHEN cust.fee_schedule_type = 'customOrReset' and cust.deleted_ind='N'"
		    + "                                THEN 'Customized'"
		    + "                 WHEN cust.deleted_ind='Y'"
		    + "                               THEN 'Standard'  "
			+ "                 WHEN std.created_ts IS NOT NULL "
			+ "                       AND cust.fee_schedule_type IS NULL THEN 'Standard' "
			+ "                END                     AS fee_schedule "
			+ "         FROM   psw100. contract_cs CS "
			+ "                LEFT JOIN (SELECT third_party_admin_id, "
			+ "                                  created_ts, "
			+ "                                  created_user_id "
			+ "                           FROM   psw100.tpa_standard_fee_hist "
			+ "                           WHERE  third_party_admin_id = ? "
			+ "                           ORDER  BY created_ts desc "
			+ "                           FETCH first ROW only) std "
			+ "                       ON std.third_party_admin_id = CS.third_party_admin_id "
			+ " LEFT JOIN (SELECT DISTINCT t.contract_id, " 
			+ "         cctfh.created_ts,  "
            + "         cctfh.created_user_id, " 
            + "         cctfh.deleted_ind, "
            + "         t1.fee_schedule_type,  "
            + "         cctfh.third_party_admin_id " 
            + " FROM   temp t  "
            + " INNER JOIN psw100.contract_custom_tpa_fee_hist " 
            + "           cctfh  "
            + "        ON cctfh.contract_id = t.contract_id " 
            + "           AND cctfh.third_party_admin_id =  "
            + "               t.third_party_admin_id  "
            + "           AND cctfh.created_ts = t.created_ts " 
            + " LEFT OUTER JOIN FEE_SCHEDULE_TYPE t1  "
            + "             ON t.contract_id =  "
            + "  t1.contract_id) cust "
			+ "                       ON cust.contract_id = CS.contract_id "
			+ "                          AND cust.third_party_admin_id = "
			+ "                              CS.third_party_admin_id "
			+ "                LEFT OUTER JOIN psw100.client cl "
			+ "                             ON cs.client_id = cl.client_id "
			+ "                INNER JOIN psw100.PLAN p "
			+ "                        ON cs.plan_id = p.plan_id "
			+ "         WHERE  cs.distribution_channel <> 'MTA' "
			+ "                AND cs.product_id NOT IN ( 'DB06', 'DBNY06', 'RA457' ) "
			+ "                AND cs.contract_status_code IN ( 'PS', 'DC', 'PC', 'CA', "
			+ "                                                 'AC', 'CF' ) "
			+ "                AND cl.group_field_office_no NOT IN ( '25270', '25280' ) "
			+ "                AND plan_type_code <> '457' "
			+ "                AND organization_type_code <> 'GT' "
			+ "                AND industry_type_code <> 'GO' "
			+ "                AND CS.third_party_admin_id = ? AND cs.MANULIFE_COMPANY_ID = ? {0}) "
			+ "     SELECT * from BASE FOR READ ONLY ";
	
	private static String FILTER_CONTRACT_NAME = " and UPPER(cs.CONTRACT_NAME) = ?";
	
	private static String FILTER_CONTRACT_NAME_LIKE = " and UPPER(cs.CONTRACT_NAME) like UPPER(''{0}%'')";
	
	private static String FILTER_CONTRACT_NUMBER = "  and cs.contract_id = ?";
			
 	private static String VALIDATE_CONTRACT_AVAILABLE_IN_OTHER_FIRM = "SELECT CS.contract_name, "
			+ "       CS.contract_id, "
			+ "       TPA.third_party_admin_id AS TPA_FIRM_ID "
			+ "FROM   psw100. contract_cs CS "
			+ "       LEFT JOIN psw100. third_party_administrator TPA "
			+ "              ON CS.third_party_admin_id = TPA.third_party_admin_id "
			+ "WHERE cs.MANULIFE_COMPANY_ID = ? AND  TPA.third_party_admin_id IN ( {0} )";
	
 	private static String VALIDATE_CONTRACT = "SELECT 1 "
 			+ "FROM   psw100.contract_cs CS "
 			+ "       LEFT OUTER JOIN psw100.client cl "
 			+ "                    ON cs.client_id = cl.client_id "
 			+ "       INNER JOIN psw100.PLAN p "
 			+ "               ON cs.plan_id = p.plan_id "
 			+ "WHERE  CS.third_party_admin_id = ? AND cs.MANULIFE_COMPANY_ID = ? "
 			+ "       AND (cs.distribution_channel = 'MTA' "
 			+ "       OR cs.product_id IN ( 'DB06', 'DBNY06', 'RA457' ) "
 			+ "       OR cs.contract_status_code NOT IN ( 'PS', 'DC', 'PC', 'CA',  'AC', 'CF' ) "
 			+ "       OR cl.group_field_office_no IN ( '25270', '25280' ) "
 			+ "       OR plan_type_code = '457' "
 			+ "       OR organization_type_code = 'GT' "
 			+ "       OR industry_type_code = 'GO' )";
 	
	// Make sure nobody instantiates this class
	private TPAFeeScheduleContractSearchDAO() {
	}

	public static ReportData getReportData(ReportCriteria criteria)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
			logger.debug("Report criteria -> " + criteria.toString());
		}
		
		BigDecimal tpaFirmId = convertStringToBigDecimal(
				(String) criteria
						.getFilterValue(TPAFeeScheduleContractSearchReportData.FILTER_TPA_FIRM_ID),
						TPAFeeScheduleContractSearchReportData.FILTER_TPA_FIRM_ID,
				className);
		String companyCode = (String) criteria
				.getFilterValue(TPAFeeScheduleContractSearchReportData.FILTER_COMPANY_CODE);
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		TPAFeeScheduleContractSearchReportData vo = null;
		TPAFeeScheduleContract item;
		
		try {
			
			String contractNumber =   StringUtils.trimToEmpty((String) criteria
					.getFilterValue(TPAFeeScheduleContractSearchReportData.FILTER_CONTRACT_NUMBER));
			String contractName = StringUtils.trimToEmpty((String) criteria
					.getFilterValue(TPAFeeScheduleContractSearchReportData.FILTER_CONTRACT_NAME));
			
			String filterQuery = "";
			
			if (StringUtils.isNotBlank(contractNumber)) {
				filterQuery = filterQuery + FILTER_CONTRACT_NUMBER;
			}
			
			if (StringUtils.isNotBlank(contractName)) {
				filterQuery = filterQuery + MessageFormat.format(FILTER_CONTRACT_NAME_LIKE, sqlizeTheString(contractName));
			}
			
			String query = GET_CONTRACT_LIST.replace("{0}", filterQuery) ;
			
			// setup the connection and the statement
			connection = getReadUncommittedConnection(className,
					CUSTOMER_DATA_SOURCE_NAME);
			statement = connection.prepareStatement(query);

			// set the input parameters
			statement.setBigDecimal(1, tpaFirmId);
			statement.setBigDecimal(2, tpaFirmId);
			statement.setBigDecimal(3, tpaFirmId);
			statement.setBigDecimal(4, tpaFirmId);
			statement.setString(5, companyCode);
			
			if (StringUtils.isNotBlank(contractNumber)) {
				statement.setInt(6, NumberUtils.toInt(contractNumber));
			}
			
			statement.execute();

			resultSet = statement.getResultSet();

			vo = new TPAFeeScheduleContractSearchReportData(criteria, 0);

			List<TPAFeeScheduleContract> contracts = new ArrayList<TPAFeeScheduleContract>();
			
			if (resultSet != null) {
				while (resultSet.next()) {
					item = new TPAFeeScheduleContract();
					item.setTpaFirmId(resultSet.getInt("TPA_FIRM_ID"));
					item.setContractName(resultSet.getString("CONTRACT_NAME"));
					item.setContractId (resultSet.getInt("CONTRACT_ID"));
					item.setFeeSchedule(resultSet.getString("fee_schedule"));
					contracts.add(item);
				}
				vo.setDetails(contracts);
			}
			
			if (contracts.isEmpty()) {
				
				String isValidContractQuery = VALIDATE_CONTRACT;
				
				if (StringUtils.isNotBlank(contractNumber)) {
					isValidContractQuery = isValidContractQuery + FILTER_CONTRACT_NUMBER;
				}
				
				if (StringUtils.isNotBlank(contractName)) {
					isValidContractQuery = isValidContractQuery + FILTER_CONTRACT_NAME;
				}
				
				statement = connection.prepareStatement(isValidContractQuery);
				
				statement.setBigDecimal(1, tpaFirmId);
				statement.setString(2, companyCode);
				
				if (StringUtils.isNotBlank(contractNumber) && StringUtils.isBlank(contractName)) {
					statement.setInt(3, NumberUtils.toInt(contractNumber));
				}
				
				if (StringUtils.isNotBlank(contractName) && StringUtils.isBlank(contractNumber)) {
					statement.setString(3, StringUtils.upperCase(contractName));
				}
				
				if (StringUtils.isNotBlank(contractName) && StringUtils.isNotBlank(contractNumber)) {
					statement.setInt(3, NumberUtils.toInt(contractNumber));
					statement.setString(4, StringUtils.upperCase(contractName));
				}
				
				statement.execute();
				resultSet = statement.getResultSet();
				if(resultSet.next()) {
					vo.setInvalidContractSearch(true);
				}
				
				if (!vo.isInvalidContractSearch()) {
					String masterTpaFirmIds = (String) criteria
							.getFilterValue(TPAFeeScheduleContractSearchReportData.FILTER_MASTER_TPA_FIRM_IDS);
					String contractListQuery = MessageFormat.format(VALIDATE_CONTRACT_AVAILABLE_IN_OTHER_FIRM,
							masterTpaFirmIds);

					if (StringUtils.isNotBlank(contractNumber)) {
						contractListQuery = contractListQuery
								+ FILTER_CONTRACT_NUMBER;
					}

					if (StringUtils.isNotBlank(contractName)) {
						contractListQuery = contractListQuery
								+ FILTER_CONTRACT_NAME;// MessageFormat.format(FILTER_CONTRACT_NAME,
														// contractName);
					}

					statement = connection.prepareStatement(contractListQuery);
					statement.setString(1, companyCode);
					
					if (StringUtils.isNotBlank(contractNumber)
							&& StringUtils.isBlank(contractName)) {
						statement.setInt(2, NumberUtils.toInt(contractNumber));
					}

					if (StringUtils.isNotBlank(contractName)
							&& StringUtils.isBlank(contractNumber)) {
						statement.setString(2, StringUtils.upperCase(contractName));
					}

					if (StringUtils.isNotBlank(contractName)
							&& StringUtils.isNotBlank(contractNumber)) {
						statement.setInt(2, NumberUtils.toInt(contractNumber));
						statement.setString(3, StringUtils.upperCase(contractName));
					}

					statement.execute();
					resultSet = statement.getResultSet();
					if (resultSet.next()) {
						vo.setContractExistsOnTheOtherFirm(true);
						vo.setOtherFirmIdWhereContractExist(resultSet.getInt("TPA_FIRM_ID"));
					}
				}
			}
			
		} catch (SQLException e) {
			throw new SystemException(e, 
					"Problem occurred during getReportData method call. "
							+ "ReportCriteria parameters are: "
							+ criteria.toString());
		} finally {
			close(statement, connection);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- getReportData");

		return vo;
	}
	
	/**
     * This method is to used to add another ' after ' in order
     * to make the SQL statement valid
     *
     * @param str
     * @return String
     */
    private static String sqlizeTheString(String str) {
    	StringBuffer temp = new StringBuffer();
    	int initialIndex = 0;
    	int index = 0;
    	while ((index = str.indexOf("'", initialIndex)) != -1) {
    		temp.append(str.substring(initialIndex,index+1)).append("'");
    		initialIndex = index+1;
    	}

    	if (temp.length() == 0) temp.append(str);

    	return temp.toString();
    }
}
