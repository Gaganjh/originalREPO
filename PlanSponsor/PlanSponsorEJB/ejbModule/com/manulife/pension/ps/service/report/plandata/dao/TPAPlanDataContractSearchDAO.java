package com.manulife.pension.ps.service.report.plandata.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.plandata.valueobject.TPAPlanDataContract;
import com.manulife.pension.ps.service.report.plandata.valueobject.TPAPlanDataContractSearchReportData;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class TPAPlanDataContractSearchDAO extends BaseDatabaseDAO {

	private static final String className = TPAPlanDataContractSearchDAO.class.getName();
	private static final Logger logger = Logger.getLogger(TPAPlanDataContractSearchDAO.class);
	
	
	


private static String GET_CONTRACT_LIST = 
			
			"SELECT CS.contract_name,CS.contract_id, CS.third_party_admin_id AS TPA_FIRM_ID "+
			"FROM   psw100.contract_all_vw C ,psw100.contract_cs CS "+
			"WHERE  C.contract_id = CS.contract_id and C.contract_id IN (SELECT UC.contract_id " +
			                         "FROM   psw100.contract_cs UC, " +
			                                "psw100.third_party_administrator T, " +
			                                "psw100.user_profile U, " +
			                                "psw100.client Cl, " +
			                                "psw100.PLAN p, " +
			                                "psw100.external_user_tpa_firm E " +
			                         "WHERE  U.user_profile_id = ? "+
			                                "AND U.user_profile_id = E.user_profile_id " +
			                                "AND U.end_ts > CURRENT TIMESTAMP " +
			                                "AND E.tpa_firm_id = T.third_party_admin_id " +
			                                "AND UC.third_party_admin_id = " +
			                                    "T.third_party_admin_id " +
			                                "AND UC.distribution_channel <> 'MTA' " +
			                                "AND UC.product_id NOT IN ( " +
			                                    "'DB06', 'DBNY06', 'RA457' ) " +
			                                "AND UC.contract_status_code IN ( " +
			                                    "'PS', 'DC', 'PC', 'CA', " +
			                                    "'AC', 'CF', 'IA' ) " +
			                                "AND cl.group_field_office_no NOT IN " +
			                                    "( '25270', '25280' ) " +
			                                "AND plan_type_code <> '457' " +
			                                "AND organization_type_code <> 'GT' " +
			                                "AND industry_type_code <> 'GO' " +
			                                "AND UC.third_party_admin_id = ? " +
			                                "AND UC.manulife_company_id = ?) "+
			       "AND ( ( product_type_cd = 'U' " +
			               "AND C.manulife_company_id = ? )  "+
			              "OR product_type_cd = 'A' " +
			                 "AND ( C.manulife_company_id = ?  "+
			                       "AND ( ( CS.tpa_staff_plan_ind = 'N' " +
			                                "OR CS.tpa_staff_plan_ind IS NULL ) " +
			                              "OR ( CS.tpa_staff_plan_ind = 'Y' " +
			                                   "AND (SELECT COUNT(*) " +
			                                        "FROM   psw100.permission_grant PG, " +
			                                               "psw100.permission_holder PH, " +
			                                               "psw100.contract_cs CC " +
			                                        "WHERE  PG.permission_holder_id = " +
			                                               "PH.permission_holder_id " +
			                                               "AND " +
			                                       "PH.user_tpa_firm_user_profile_id = ? " +
			                                               "AND CC.third_party_admin_id = " +
			                                                   "PH.user_tpa_firm_tpa_firm_id " +
			                                               "AND CC.contract_id = " +
			                                                   "C.contract_id " +
			                                               "AND " +
			                                       "PG.security_task_permission_code = " +
			                                       "'TSPA') " +
			                                       "> 0 " +
			                                 ") ) ) ) {0}";
	
	private static String GET_CONTRACT_LIST_FOR_INTERNAL_USER = 
			
			"SELECT CS.contract_name,CS.contract_id, CS.third_party_admin_id AS TPA_FIRM_ID "+
			"FROM   psw100.contract_all_vw C ,psw100.contract_cs CS "+
			"WHERE  C.contract_id = CS.contract_id and C.contract_id IN (SELECT UC.contract_id " +
			                         "FROM   psw100.contract_cs UC, " +
			                                "psw100.third_party_administrator T, " +
			                                "psw100.client Cl, " +
			                                "psw100.PLAN p, " +
			                                "psw100.external_user_tpa_firm E " +
			                         "WHERE  "+
			                                "E.tpa_firm_id = T.third_party_admin_id " +
			                                "AND UC.third_party_admin_id = " +
			                                    "T.third_party_admin_id " +
			                                "AND UC.distribution_channel <> 'MTA' " +
			                                "AND UC.product_id NOT IN ( " +
			                                    "'DB06', 'DBNY06', 'RA457' ) " +
			                                "AND UC.contract_status_code IN ( " +
			                                    "'PS', 'DC', 'PC', 'CA', " +
			                                    "'AC', 'CF', 'IA' ) " +
			                                "AND cl.group_field_office_no NOT IN " +
			                                    "( '25270', '25280' ) " +
			                                "AND plan_type_code <> '457' " +
			                                "AND organization_type_code <> 'GT' " +
			                                "AND industry_type_code <> 'GO' " +
			                                "AND UC.third_party_admin_id = ? " +
			                                "AND UC.manulife_company_id = ?) "+
			       "AND ( ( product_type_cd = 'U' " +
			               "AND C.manulife_company_id = ? )  "+
			              "OR product_type_cd = 'A' " +
			                 "AND ( C.manulife_company_id = ?  "+
			                       "AND ( ( CS.tpa_staff_plan_ind = 'N' " +
			                                "OR CS.tpa_staff_plan_ind IS NULL ) " +
			                              "OR ( CS.tpa_staff_plan_ind = 'Y' " +
			                                   "AND (SELECT COUNT(*) " +
			                                        "FROM   psw100.permission_grant PG, " +
			                                               "psw100.permission_holder PH, " +
			                                               "psw100.contract_cs CC " +
			                                        "WHERE  PG.permission_holder_id = " +
			                                               "PH.permission_holder_id " +
			                                               "AND CC.third_party_admin_id = " +
			                                                   "PH.user_tpa_firm_tpa_firm_id " +
			                                               "AND CC.contract_id = " +
			                                                   "C.contract_id " +
			                                               "AND " +
			                                       "PG.security_task_permission_code = " +
			                                       "'TSPA') " +
			                                       "> 0 " +
			                                 ") ) ) ) {0}";
	
	private static String FILTER_CONTRACT_NAME = " and UPPER(cs.CONTRACT_NAME) = ?";
	
	private static String FILTER_CONTRACT_NAME_LIKE = " and UPPER(cs.CONTRACT_NAME) like UPPER(''{0}%'')";
	
	private static String FILTER_CONTRACT_NUMBER = "  and cs.contract_id = ?";
			
 	private static String VALIDATE_CONTRACT_AVAILABLE_IN_OTHER_FIRM = "SELECT CS.contract_name, "
			+ "       CS.contract_id, "
			+ "       TPA.third_party_admin_id AS TPA_FIRM_ID "
			+ "FROM   " + PLAN_SPONSOR_SCHEMA_NAME+ "contract_cs CS "
			+ "       LEFT JOIN " + PLAN_SPONSOR_SCHEMA_NAME+ "third_party_administrator TPA "
			+ "              ON CS.third_party_admin_id = TPA.third_party_admin_id "
			+ "WHERE cs.MANULIFE_COMPANY_ID = ? AND  TPA.third_party_admin_id IN ( {0} )";
	
 	private static String VALIDATE_CONTRACT = "SELECT 1 "
 			+ "FROM   " + PLAN_SPONSOR_SCHEMA_NAME+ "contract_cs CS "
 			+ "       LEFT OUTER JOIN " + PLAN_SPONSOR_SCHEMA_NAME+ "client cl "
 			+ "                    ON cs.client_id = cl.client_id "
 			+ "       INNER JOIN " + PLAN_SPONSOR_SCHEMA_NAME+ "PLAN p "
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
	private TPAPlanDataContractSearchDAO() {
	}

	public static ReportData getReportData(ReportCriteria criteria)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
			logger.debug("Report criteria -> " + criteria.toString());
		}
		
		BigDecimal tpaFirmId = convertStringToBigDecimal(
				(String) criteria
						.getFilterValue(TPAPlanDataContractSearchReportData.FILTER_TPA_FIRM_ID),
						TPAPlanDataContractSearchReportData.FILTER_TPA_FIRM_ID,
				className);
		BigDecimal profileId = convertStringToBigDecimal(
				(String) criteria
						.getFilterValue(TPAPlanDataContractSearchReportData.USER_PROFILE_ID),
						TPAPlanDataContractSearchReportData.USER_PROFILE_ID,
				className);
		String companyCode = (String) criteria
				.getFilterValue(TPAPlanDataContractSearchReportData.FILTER_COMPANY_CODE);
			
		Connection connection = null;
		PreparedStatement statement1 = null;
		PreparedStatement statement2 = null;
		PreparedStatement statement3 = null;
		ResultSet resultSet = null;
		TPAPlanDataContractSearchReportData vo = null;
		TPAPlanDataContract item;
		
		try {
			
			String contractNumber =   StringUtils.trimToEmpty((String) criteria
					.getFilterValue(TPAPlanDataContractSearchReportData.FILTER_CONTRACT_NUMBER));
			String contractName = StringUtils.trimToEmpty((String) criteria
					.getFilterValue(TPAPlanDataContractSearchReportData.FILTER_CONTRACT_NAME));
			
			String filterQuery = "";
			
			if (StringUtils.isNotBlank(contractNumber)) {
				filterQuery = filterQuery + FILTER_CONTRACT_NUMBER;
			}
			
			if (StringUtils.isNotBlank(contractName)) {
				filterQuery = filterQuery + MessageFormat.format(FILTER_CONTRACT_NAME_LIKE, sqlizeTheString(contractName));
			}
			
			String query;
			
			if(criteria.isExternalUser()) {
				query = GET_CONTRACT_LIST.replace("{0}", filterQuery) ;
			}
			else {
				query = GET_CONTRACT_LIST_FOR_INTERNAL_USER.replace("{0}", filterQuery) ;
			}
			
			
			// setup the connection and the statement
			connection = getReadUncommittedConnection(className,
					CUSTOMER_DATA_SOURCE_NAME);
			statement1 = connection.prepareStatement(query);

			// set the input parameters
			if(criteria.isExternalUser()) {
				
				statement1.setBigDecimal(1, profileId);
				statement1.setBigDecimal(2, tpaFirmId);
				statement1.setString(3, companyCode);
				statement1.setString(4, companyCode);
				statement1.setString(5, companyCode);
				statement1.setBigDecimal(6, profileId);
				
				if (StringUtils.isNotBlank(contractNumber)) {
					statement1.setInt(7, NumberUtils.toInt(contractNumber));
				}
			}
			else {
				
				statement1.setBigDecimal(1, tpaFirmId);
				statement1.setString(2, companyCode);
				statement1.setString(3, companyCode);
				statement1.setString(4, companyCode);
				
				if (StringUtils.isNotBlank(contractNumber)) {
					statement1.setInt(5, NumberUtils.toInt(contractNumber));
				}
			}
			
			
			statement1.execute();

			resultSet = statement1.getResultSet();

			vo = new TPAPlanDataContractSearchReportData(criteria, 0);

			List<TPAPlanDataContract> contracts = new ArrayList<TPAPlanDataContract>();
			
			if (resultSet != null) {
				while (resultSet.next()) {
					item = new TPAPlanDataContract();
					item.setTpaFirmId(resultSet.getInt("TPA_FIRM_ID"));
					item.setContractName(resultSet.getString("CONTRACT_NAME"));
					item.setContractId (resultSet.getInt("CONTRACT_ID"));
					//item.setFeeSchedule(resultSet.getString("fee_schedule"));
					contracts.add(item);
					
					Map csfMap=null;
			        if(csfMap == null){
						ContractServiceDelegate service = ContractServiceDelegate.getInstance();
						
						try {
							csfMap = service.getContractServiceFeatures(item.getContractId());
						} catch (ApplicationException ae) {
							throw new SystemException(ae.toString() + "TPAPlanDataContractSearchDAO " 
									+ "getReportData " 
									+ ae.getDisplayMessage());
						}
					}
			        
			        ContractServiceFeature noticeGenerationService = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.NOTICE_SERVICE_GENERATION_CD);
			        if(noticeGenerationService !=null){
			        	if("Y".equalsIgnoreCase(noticeGenerationService.getValue())) {
			        		item.setServiceSelected("Yes");
			        	}
			        	else {
			        		String effectiveDate = noticeGenerationService.getAttributeValue("NEFD");
			        		if(effectiveDate==null || effectiveDate.trim().length()==0) {
			        			item.setServiceSelected("No");
			        		}
			        		else {
			        			item.setServiceSelected("No (De-selected)");
			        		}
			        	}
			        	
			        }

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
				
				statement2 = connection.prepareStatement(isValidContractQuery);
				
				statement2.setBigDecimal(1, tpaFirmId);
				statement2.setString(2, companyCode);
				
				if (StringUtils.isNotBlank(contractNumber) && StringUtils.isBlank(contractName)) {
					statement2.setInt(3, NumberUtils.toInt(contractNumber));
				}
				
				if (StringUtils.isNotBlank(contractName) && StringUtils.isBlank(contractNumber)) {
					statement2.setString(3, StringUtils.upperCase(contractName));
				}
				
				if (StringUtils.isNotBlank(contractName) && StringUtils.isNotBlank(contractNumber)) {
					statement2.setInt(3, NumberUtils.toInt(contractNumber));
					statement2.setString(4, StringUtils.upperCase(contractName));
				}
				
				statement2.execute();
				resultSet = statement2.getResultSet();
				if(resultSet.next()) {
					vo.setInvalidContractSearch(true);
				}
				
				if (!vo.isInvalidContractSearch()) {
					String masterTpaFirmIds = (String) criteria
							.getFilterValue(TPAPlanDataContractSearchReportData.FILTER_MASTER_TPA_FIRM_IDS);
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

					statement3 = connection.prepareStatement(contractListQuery);
					statement3.setString(1, companyCode);
					
					if (StringUtils.isNotBlank(contractNumber)
							&& StringUtils.isBlank(contractName)) {
						statement3.setInt(2, NumberUtils.toInt(contractNumber));
					}

					if (StringUtils.isNotBlank(contractName)
							&& StringUtils.isBlank(contractNumber)) {
						statement3.setString(2, StringUtils.upperCase(contractName));
					}

					if (StringUtils.isNotBlank(contractName)
							&& StringUtils.isNotBlank(contractNumber)) {
						statement3.setInt(2, NumberUtils.toInt(contractNumber));
						statement3.setString(3, StringUtils.upperCase(contractName));
					}

					statement3.execute();
					resultSet = statement3.getResultSet();
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
			close(statement1, connection);
			close(statement2, null);
			close(statement3, null);
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
