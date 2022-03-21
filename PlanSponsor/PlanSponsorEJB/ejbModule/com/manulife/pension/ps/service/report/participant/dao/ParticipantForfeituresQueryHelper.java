package com.manulife.pension.ps.service.report.participant.dao;

import org.apache.log4j.Logger;

import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantForfeituresReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * ParticipantForfeituresQueryHelper class to compose & return the SQL Queries 
 * for Accounts Forfeitures details page
 * 
 * @author Vinothkumar Balasubramaniyam
 */
public class ParticipantForfeituresQueryHelper {
	
	//Attribute to hold the logger message
	private static final Logger logger = Logger.getLogger(ParticipantForfeituresQueryHelper.class);

	//Default Private Constructor to make sure nobody instantiate this class
	private ParticipantForfeituresQueryHelper(){}
	
	/**
	 * Method to generate the SQL for getting current AsOfDate
	 * 
	 * @return SQL query as String
	 */
	public static String getCurrentAsOfDateSQL() {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getCurrentAsOfDateSQL");
		}
		
		StringBuilder strQuery = new StringBuilder();
		strQuery.append("SELECT ");
		strQuery.append("     CYCLE_DATE AS AS_OF_DATE ");
		strQuery.append("FROM ");
		strQuery.append("     PSW100.RF_RUNDATE_CS ");
		strQuery.append("WHERE ");
		strQuery.append("     CYCLE_ID      = 'RUNDATE' ");
		strQuery.append(" AND BUSINESS_UNIT = 'PS' ");
		
		if(logger.isDebugEnabled()) {
			logger.debug("exit <- getCurrentAsOfDateSQL");
		}
		
		return strQuery.toString();		
	}
	
	/**
	 * Method to generate the SQL for Total Participants
	 * 
	 * @return SQL query as String
	 */
	public static String getTotalParticipantsSQL() {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getTotalParticipantsSQL");
		}
		
		StringBuilder strQuery = new StringBuilder();
		strQuery.append("SELECT ");
		strQuery.append("	COUNT(PARTICIPANT_ID) AS NO_OF_PARTICIPANTS ");
		strQuery.append("FROM ");
		strQuery.append("	PSW100.PARTICIPANT_CONTRACT PC ");
		strQuery.append("WHERE ");
		strQuery.append("	PC.CONTRACT_ID = ?");
		strQuery.append("	AND (PC.PARTICIPANT_STATUS_CODE = 'AC' ");
		strQuery.append("	OR (PC.PARTICIPANT_STATUS_CODE <> 'CN' ");
		strQuery.append("	AND (SELECT SUM (PB.TOTAL_BALANCE_AMT) ");
		strQuery.append("FROM ");
		strQuery.append("	PSW100.PARTICIPANT_CURRENT_BAL_LSA PB ");
		strQuery.append("WHERE ");
		strQuery.append("	PB.CONTRACT_ID=PC.CONTRACT_ID ");
		strQuery.append("	AND PB.PARTICIPANT_ID=PC.PARTICIPANT_ID ) > 0 ))");
		
		if(logger.isDebugEnabled()) {
			logger.debug("exit <- getTotalParticipantsSQL");
		}
				
		return strQuery.toString();		
	}
	
	/**
	 * Method to generate the SQL for Employee Assets, Employer Assets, Forfeitures Total.
	 * The summations are classified by Money Types.
	 * 
	 * @return SQL query as String
	 */
	public static String getParticipantsSummarySQL() {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getParticipantsSummarySQL");
		}
		
		StringBuilder strQuery = new StringBuilder();
		strQuery.append("SELECT  ");
		strQuery.append("	SUM(TOTAL_BALANCE_AMT) AS TOTAL_ASSESTS, ");
		strQuery.append("	MT.MONEY_TYPE_GROUP ");
		strQuery.append("FROM ");
		strQuery.append("	PSW100.PARTICIPANT_CURRENT_BAL_LSA PCB, ");
		strQuery.append("	PSW100.PARTICIPANT_CONTRACT PC,");
		strQuery.append("	PSW100.CONTRACT_MONEY_TYPE CMT,");
		strQuery.append("	PSW100.MONEY_TYPE MT ");
		strQuery.append("WHERE ");
		strQuery.append("   PC.PARTICIPANT_STATUS_CODE <> 'CN' ");
		strQuery.append("	AND PC.CONTRACT_ID = ? ");
		strQuery.append("	AND PCB.CONTRACT_ID = PC.CONTRACT_ID  ");
		strQuery.append("	AND PCB.PARTICIPANT_ID = PC.PARTICIPANT_ID  ");
		strQuery.append("	AND PCB.MONEY_TYPE_ID = CMT.MONEY_TYPE_ID  ");
		strQuery.append("	AND PCB.CONTRACT_ID = CMT.CONTRACT_ID ");
		strQuery.append("	AND CMT.MONEY_TYPE_ID = MT.MONEY_TYPE_CODE ");
		strQuery.append("GROUP BY ");
		strQuery.append("	(MT.MONEY_TYPE_GROUP) ");
		strQuery.append("ORDER BY ");
		strQuery.append("	(MT.MONEY_TYPE_GROUP)");
		
		if(logger.isDebugEnabled()) {
			logger.debug("exit <- getParticipantsSummarySQL");
		}
				
		return strQuery.toString();		
	}	
	
	/**
	 * Method to generate the SQL for Participant Details
	 * 
	 * @return SQL query as String
	 */
	public static String getParticipantDetailsSQL(ReportCriteria criteria) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getParticipantDetailsSQL");
		}
		
		//Get the sort & filter criteria values
		String sortFields = (String)criteria.getFilterValue(
				ParticipantForfeituresReportData.FILTER_SORT_CRITERIA);
		String filterFields = (String)criteria.getFilterValue(
				ParticipantForfeituresReportData.FILTER_FILTER_CRITERIA);
		
		int stopIndex = 0;
		int startIndex = 0;
		
		int recordPerPage = criteria.getPageSize();
		int startingRecordNo = criteria.getStartIndex();
		
		// Set the start and stop indexes to return the proper rows.
		// The default condition is the first ten rows are returned.
		if (startingRecordNo > 0) {
			startIndex = startingRecordNo;
		} else {
			startIndex = 1;
		}

		if (recordPerPage > 0) {
			stopIndex = startIndex + recordPerPage - 1;
		} else {
			stopIndex = startIndex + 9;
		}
		
		StringBuilder strQuery = new StringBuilder();
		strQuery.append("WITH PARTICIPANT_SUMMARY (");
		strQuery.append("		PARTICIPANT_ID, ");
		strQuery.append("		CONTRACT_ID, ");
		strQuery.append("		PROFILE_ID, ");
		strQuery.append("		FIRST_NAME, ");
		strQuery.append("		LAST_NAME, ");
		strQuery.append("		SOCIAL_SECURITY_NO, ");
		strQuery.append("		EMPLOYER_DIVISION, ");
		strQuery.append("		STATUS, ");
		strQuery.append("		EMPLOYEE_ASSET, ");
		strQuery.append("		EMPLOYER_ASSET, ");
		strQuery.append("		TOTAL_BALANCE,");
		strQuery.append("		FORFEITURES");
		strQuery.append("	) AS 		");
		strQuery.append("	(SELECT ");
		strQuery.append("		PC.PARTICIPANT_ID, ");
		strQuery.append("		PC.CONTRACT_ID, ");
		strQuery.append("		PC.PROFILE_ID, ");
		strQuery.append("		EC.FIRST_NAME, ");
		strQuery.append("		EC.LAST_NAME, ");
		strQuery.append("		EC.SOCIAL_SECURITY_NO AS SOCIAL_SECURITY_NO, ");
		strQuery.append("		EC.EMPLOYER_DIVISION AS EMPLOYER_DIVISION, ");
		strQuery.append("		ezk100.contrib_status(pc.participant_status_code, ec.auto_enroll_opt_out_ind, mtcc.ee_count, mtcc.er_count, ");
		strQuery.append("				pb1.um_count, pb1.non_um_count, rd.cycle_date, pc.last_investment_dt, SUM_TOTAL_BALANCE_AMT) AS STATUS, ");
		strQuery.append("		VALUE(");
		strQuery.append("			(SELECT ");
		strQuery.append("					SUM(PCB.TOTAL_BALANCE_AMT) ");
		strQuery.append("			FROM ");
		strQuery.append("				PSW100.PARTICIPANT_CURRENT_BAL_LSA PCB, ");
		strQuery.append("				PSW100.CONTRACT_MONEY_TYPE MT ");
		strQuery.append("			WHERE ");
		strQuery.append("				PCB.PARTICIPANT_ID = PC.PARTICIPANT_ID ");
		strQuery.append("				AND PCB.CONTRACT_ID = PC.CONTRACT_ID ");
		strQuery.append("				AND PCB.MONEY_TYPE_ID = MT.MONEY_TYPE_ID  ");
		strQuery.append("				AND PCB.CONTRACT_ID = MT.CONTRACT_ID  ");
		strQuery.append("				AND MT.MONEY_TYPE_CATEGORY_CODE LIKE 'EE%' ");
		strQuery.append("			), 0) AS EMPLOYEE_ASSET, ");
		strQuery.append("		VALUE(");
		strQuery.append("			(SELECT ");
		strQuery.append("				SUM(PCB.TOTAL_BALANCE_AMT) ");
		strQuery.append("			FROM ");
		strQuery.append("				PSW100.PARTICIPANT_CURRENT_BAL_LSA PCB, ");
		strQuery.append("				PSW100.CONTRACT_MONEY_TYPE MT ");
		strQuery.append("			WHERE ");
		strQuery.append("				PCB.PARTICIPANT_ID = PC.PARTICIPANT_ID ");
		strQuery.append("				AND PCB.CONTRACT_ID = PC.CONTRACT_ID ");
		strQuery.append("				AND PCB.MONEY_TYPE_ID = MT.MONEY_TYPE_ID  ");
		strQuery.append("				AND PCB.CONTRACT_ID = MT.CONTRACT_ID  ");
		strQuery.append("				AND MT.MONEY_TYPE_CATEGORY_CODE LIKE 'ER%' ");
		strQuery.append("			), 0) AS EMPLOYER_ASSET, ");
		strQuery.append("		VALUE(");
		strQuery.append("			(SELECT ");
		strQuery.append("				SUM(PCB.TOTAL_BALANCE_AMT) ");
		strQuery.append("			FROM ");
		strQuery.append("				PSW100.PARTICIPANT_CURRENT_BAL_LSA PCB, ");
		strQuery.append("				PSW100.CONTRACT_MONEY_TYPE MT ");
		strQuery.append("			WHERE ");
		strQuery.append("				PCB.PARTICIPANT_ID = PC.PARTICIPANT_ID ");
		strQuery.append("				AND PCB.CONTRACT_ID = PC.CONTRACT_ID ");
		strQuery.append("				AND PCB.MONEY_TYPE_ID = MT.MONEY_TYPE_ID  ");
		strQuery.append("				AND PCB.CONTRACT_ID = MT.CONTRACT_ID  ");
		strQuery.append("				AND MT.MONEY_TYPE_CATEGORY_CODE LIKE 'E%' ");
		strQuery.append("			), 0) AS TOTAL_BALANCE,");
		strQuery.append("			");
		strQuery.append("		VALUE(");
		strQuery.append("			(SELECT ");
		strQuery.append("				SUM(PCB.TOTAL_BALANCE_AMT) ");
		strQuery.append("			FROM ");
		strQuery.append("				PSW100.PARTICIPANT_CURRENT_BAL_LSA PCB, ");
		strQuery.append("				PSW100.CONTRACT_MONEY_TYPE CMT,");
		strQuery.append("				PSW100.MONEY_TYPE MT");
		strQuery.append("			WHERE ");
		strQuery.append("				PCB.PARTICIPANT_ID = PC.PARTICIPANT_ID ");
		strQuery.append("				AND PCB.CONTRACT_ID = PC.CONTRACT_ID ");
		strQuery.append("				AND PCB.MONEY_TYPE_ID = CMT.MONEY_TYPE_ID  ");
		strQuery.append("				AND PCB.CONTRACT_ID = CMT.CONTRACT_ID  ");
		strQuery.append("				AND CMT.MONEY_TYPE_ID = MT.MONEY_TYPE_CODE");
		strQuery.append("				AND CMT.MONEY_TYPE_CATEGORY_CODE LIKE 'ER%' ");
		strQuery.append("				AND MT.MONEY_TYPE_GROUP LIKE 'UM%'");
		strQuery.append("			), 0) AS FORFEITURES ");
		strQuery.append("	FROM ");
		strQuery.append("		PSW100.EMPLOYEE_CONTRACT EC, ");
		strQuery.append("		PSW100.RF_RUNDATE_CS RD, ");
		strQuery.append("		(SELECT ");
		strQuery.append("			A.CONTRACT_ID, ");
		strQuery.append("			A.PARTICIPANT_ID, ");
		strQuery.append("			case ");
		strQuery.append("				when SUM(TOTAL_BALANCE_AMT) is null then 0 ");
		strQuery.append("				else SUM(TOTAL_BALANCE_AMT) ");
		strQuery.append("			end as SUM_TOTAL_BALANCE_AMT ");
		strQuery.append("		FROM ");
		strQuery.append("			EZK100.PARTICIPANT_CONTRACT A ");
		strQuery.append("			LEFT OUTER JOIN PSW100.PARTICIPANT_CURRENT_BAL_LSA PB ON (");
		strQuery.append("					A.CONTRACT_ID = PB.CONTRACT_ID AND A.PARTICIPANT_ID = PB.PARTICIPANT_ID) ");
		strQuery.append("		WHERE ");
		strQuery.append("				A.CONTRACT_ID = ? ");
		strQuery.append("			AND A.CONTRACT_ID = ?");
		strQuery.append("			AND A.CONTRACT_ID = ?");
		strQuery.append("		GROUP BY A.CONTRACT_ID, A.PARTICIPANT_ID ) AS PB, ");
		strQuery.append("		(SELECT ");
		strQuery.append("			SUM( CASE WHEN  SUBSTR(MONEY_TYPE_CATEGORY_CODE,1, 2) = 'EE' THEN 1 ELSE 0 END ) AS EE_COUNT, ");
		strQuery.append("			SUM( CASE WHEN  SUBSTR(MONEY_TYPE_CATEGORY_CODE,1, 2) = 'ER' THEN 1 ELSE 0 END ) AS ER_COUNT  ");
		strQuery.append("		FROM ");
		strQuery.append("			PSW100.CONTRACT_MONEY_TYPE CMT ");
		strQuery.append("		WHERE CMT.CONTRACT_ID = ? ) AS MTCC,");
		strQuery.append("		PSW100.PARTICIPANT_CONTRACT PC ");
		strQuery.append("		left outer join (");
		strQuery.append("			select ");
		strQuery.append("				pbx.profile_id, pbx.contract_id, ");
		strQuery.append("				count(case when money_type_group='UM' then 1 else null end) UM_COUNT, ");
		strQuery.append("				count(case when money_type_group != 'UM' then 1 else null end) NON_UM_COUNT ");
		strQuery.append("			from ");
		strQuery.append("				ezk100.participant_current_balance pbx ");
		strQuery.append("				inner join ezk100.money_type mt1 on pbx.money_Type_id=mt1.money_type_code ");
		strQuery.append("		 	where ");
		strQuery.append("				contract_id=? and total_balance_amt > 0");
		strQuery.append("		 	group by ");
		strQuery.append("				profile_id,contract_id");
		strQuery.append("		) pb1 on pb1.profile_id=pc.profile_id and pb1.contract_id=pc.contract_id ");
		strQuery.append("        LEFT OUTER JOIN (");
		strQuery.append("			select ");
		strQuery.append("				EVP1a.CONTRACT_ID");
		strQuery.append("				,EVP1a.PROFILE_ID");
		strQuery.append("                ,EVP1a.PARAMETER_VALUE as ES_val  ");
		strQuery.append("                ,EVP1a.EFFECTIVE_DATE  as ES_dte ");
		strQuery.append("			from ");
		strQuery.append("				PSW100.EMPLOYEE_VESTING_PARAMETER EVP1a ");
		strQuery.append("            where ");
		strQuery.append("				 EVP1a.PARAMETER_NAME  = 'EMPLOYMENT_STATUS'");
		strQuery.append("                AND EVP1a.EFFECTIVE_DATE = (");
		strQuery.append("									select ");
		strQuery.append("										max(EVP1b.EFFECTIVE_DATE)");
		strQuery.append("                                    from ");
		strQuery.append("										PSW100.EMPLOYEE_VESTING_PARAMETER EVP1b");
		strQuery.append("                                    where ");
		strQuery.append("										 EVP1a.CONTRACT_ID      = EVP1b.CONTRACT_ID");
		strQuery.append("                                        AND EVP1a.PROFILE_ID       = EVP1b.PROFILE_ID ");
		strQuery.append("                                        AND EVP1a.PARAMETER_NAME   = EVP1b.PARAMETER_NAME");
		strQuery.append("                                        AND EVP1b.EFFECTIVE_DATE <= ?");
		strQuery.append("									)  ");
		strQuery.append("				AND EVP1a.CONTRACT_ID = ? ");
		strQuery.append("        ) as EVP_ES on  ");
		strQuery.append("				PC.CONTRACT_ID = EVP_ES.CONTRACT_ID");
		strQuery.append("            AND PC.PROFILE_ID  = EVP_ES.PROFILE_ID   ");
		strQuery.append("	WHERE ");
		strQuery.append("		RD.CYCLE_ID = 'RUNDATE' ");
		strQuery.append("		AND RD.BUSINESS_UNIT = 'PS' ");
		strQuery.append("		AND PC.CONTRACT_ID = ? ");
		strQuery.append("		AND PC.CONTRACT_ID = EC.CONTRACT_ID ");
		strQuery.append("		AND PC.PROFILE_ID  = EC.PROFILE_ID ");
		strQuery.append("		AND PB.CONTRACT_ID = PC.CONTRACT_ID ");
		strQuery.append("		AND PB.PARTICIPANT_ID = PC.PARTICIPANT_ID ");
		strQuery.append("		AND (");
		strQuery.append("				PC.PARTICIPANT_STATUS_CODE = 'AC' ");
		strQuery.append("			OR (");
		strQuery.append("					PC.PARTICIPANT_STATUS_CODE <> 'CN' ");
		strQuery.append("				AND (");
		strQuery.append("						SELECT ");
		strQuery.append("							SUM (PB.TOTAL_BALANCE_AMT) ");
		strQuery.append("						FROM ");
		strQuery.append("							PSW100.PARTICIPANT_CURRENT_BAL_LSA PB ");
		strQuery.append("						WHERE ");
		strQuery.append("								PB.CONTRACT_ID=PC.CONTRACT_ID ");
		strQuery.append("							AND PB.PARTICIPANT_ID=PC.PARTICIPANT_ID ");
		strQuery.append("						) > 0 ");
		strQuery.append("				)");
		strQuery.append("			)");
		strQuery.append("	),");
		
		strQuery.append("PARTICIPANT_STATUS_HIST ( ");
		strQuery.append("				PARTICIPANT_ID, ");
		strQuery.append("				CONTRACT_ID, ");
		strQuery.append("				PARTICIPANT_STATUS_CODE, "); 
		strQuery.append("				TERMINATION_DATE) AS ( ");
		strQuery.append("					SELECT  ");
		strQuery.append("						PARTICIPANT_ID, ");
		strQuery.append("						CONTRACT_ID, ");
		strQuery.append("						PARTICIPANT_STATUS_CODE, ");
		strQuery.append("						MIN(PARTICIPANT_STATUS_EFF_DATE) ");
		strQuery.append("					FROM ");
		strQuery.append("						PSW100.PARTICIPANT_STATUS_HIST ");
		strQuery.append("					WHERE  ");
		strQuery.append("							CONTRACT_ID= ? ");
		strQuery.append("						AND PARTICIPANT_STATUS_CODE IN ('AP', 'AT', 'AU', 'BP', 'BT', 'BU', 'DP', "); 
		strQuery.append("	'DT', 'DU', 'PT', 'RP', 'RT', 'RU', 'TP', 'TT', 'TU', 'NT') ");
		strQuery.append("					GROUP BY  ");
		strQuery.append("						PARTICIPANT_ID, CONTRACT_ID, PARTICIPANT_STATUS_CODE) ");
		
		strQuery.append("	SELECT ");
		strQuery.append("		FIRST_NAME, ");
		strQuery.append("		LAST_NAME, ");
		strQuery.append("		SOCIAL_SECURITY_NO, ");
		strQuery.append("		EMPLOYER_DIVISION, ");		
		strQuery.append("		STATUS, ");
		strQuery.append("		EMPLOYEE_ASSET, ");
		strQuery.append("		EMPLOYER_ASSET, ");
		strQuery.append("		TOTAL_BALANCE,");
		strQuery.append("		FORFEITURES,");
		strQuery.append("		PROFILE_ID, ");
		strQuery.append("		CONTRACT_ID, ");
		strQuery.append("		TERMINATION_DATE ");
		strQuery.append("	FROM ");
		strQuery.append("		(SELECT ");
		strQuery.append("			FIRST_NAME, ");
		strQuery.append("			LAST_NAME, ");
		strQuery.append("			SOCIAL_SECURITY_NO, ");
		strQuery.append("			EMPLOYER_DIVISION, ");
		strQuery.append("			STATUS, ");
		strQuery.append("			EMPLOYEE_ASSET, ");
		strQuery.append("			EMPLOYER_ASSET, ");
		strQuery.append("			TOTAL_BALANCE, ");
		strQuery.append("			FORFEITURES,");
		strQuery.append("			PROFILE_ID, ");
		strQuery.append("			PS.CONTRACT_ID, ");
		strQuery.append("			PSH.TERMINATION_DATE, ");
		
		if (sortFields == null){
			strQuery.append("			ROWNUMBER() OVER ( ORDER BY LAST_NAME ASC, FIRST_NAME ASC ) AS ROW_NUMBER ");
		} else {		
			strQuery.append("			ROWNUMBER() OVER ( ORDER BY " );		
			strQuery.append(sortFields);
			strQuery.append(") AS ROW_NUMBER ");
		}
		
		strQuery.append("		FROM ");
		strQuery.append("			PARTICIPANT_SUMMARY PS  ");
		strQuery.append("			LEFT OUTER JOIN PARTICIPANT_STATUS_HIST PSH ON ( ");
		strQuery.append("					PS.CONTRACT_ID = PSH.CONTRACT_ID AND PS.PARTICIPANT_ID = PSH.PARTICIPANT_ID) "); 
		strQuery.append("		WHERE ");
		strQuery.append("				FORFEITURES > 0  ");		
		if (filterFields != null){
			strQuery.append("		AND  ");
			strQuery.append( filterFields );
		}
		
		strQuery.append("		 ) AS FINAL_SUMMARY ");
		strQuery.append("	WHERE ");
		
		//When Print Report or Download Report has been selected,
		//recordPerPage would be returned as -1
		if ( recordPerPage == -1 || recordPerPage < 0 ) { 
			strQuery.append(" ROW_NUMBER >= 0 ");
		}else {
			strQuery.append(" ROW_NUMBER BETWEEN ")
				.append(startIndex).append(" AND ").append(stopIndex);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("exit <- getParticipantDetailsSQL");
		}
				
		return strQuery.toString();		
	}	
	
	/**
	 * Method to generate the SQL for the number of Participants for the given search criteria
	 * which will be used to provide the inputs to pagination logic
	 * 
	 * @param criteria
	 * @return SQL query as String
	 */
	public static String getParticipantsCountForSearchCriteriaSQL(ReportCriteria criteria) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getParticipantsCountForSearchCriteriaSQL");
		}
		
		String filterFields = (String)criteria.getFilterValue(
				ParticipantForfeituresReportData.FILTER_FILTER_CRITERIA);
		
		StringBuilder strQuery = new StringBuilder();
		strQuery.append("WITH PARTICIPANT_SUMMARY (");
		strQuery.append("		PARTICIPANT_ID, ");
		strQuery.append("		CONTRACT_ID, ");
		strQuery.append("		PROFILE_ID, ");
		strQuery.append("		FIRST_NAME, ");
		strQuery.append("		LAST_NAME, ");
		strQuery.append("		SOCIAL_SECURITY_NO, ");
		strQuery.append("		EMPLOYER_DIVISION, ");
		strQuery.append("		STATUS, ");
		strQuery.append("		EMPLOYEE_ASSET, ");
		strQuery.append("		EMPLOYER_ASSET, ");
		strQuery.append("		TOTAL_BALANCE,");
		strQuery.append("		FORFEITURES");
		strQuery.append("	) AS 		");
		strQuery.append("	(SELECT ");
		strQuery.append("		PC.PARTICIPANT_ID, ");
		strQuery.append("		PC.CONTRACT_ID, ");
		strQuery.append("		PC.PROFILE_ID, ");
		strQuery.append("		EC.FIRST_NAME, ");
		strQuery.append("		EC.LAST_NAME, ");
		strQuery.append("		EC.SOCIAL_SECURITY_NO AS SOCIAL_SECURITY_NO, ");
		strQuery.append("		EC.EMPLOYER_DIVISION AS EMPLOYER_DIVISION, ");
		strQuery.append("		ezk100.contrib_status(pc.participant_status_code, ec.auto_enroll_opt_out_ind, mtcc.ee_count, mtcc.er_count, ");
		strQuery.append("				pb1.um_count, pb1.non_um_count, rd.cycle_date, pc.last_investment_dt, SUM_TOTAL_BALANCE_AMT) AS STATUS, ");
		strQuery.append("		VALUE(");
		strQuery.append("			(SELECT ");
		strQuery.append("					SUM(PCB.TOTAL_BALANCE_AMT) ");
		strQuery.append("			FROM ");
		strQuery.append("				PSW100.PARTICIPANT_CURRENT_BAL_LSA PCB, ");
		strQuery.append("				PSW100.CONTRACT_MONEY_TYPE MT ");
		strQuery.append("			WHERE ");
		strQuery.append("				PCB.PARTICIPANT_ID = PC.PARTICIPANT_ID ");
		strQuery.append("				AND PCB.CONTRACT_ID = PC.CONTRACT_ID ");
		strQuery.append("				AND PCB.MONEY_TYPE_ID = MT.MONEY_TYPE_ID  ");
		strQuery.append("				AND PCB.CONTRACT_ID = MT.CONTRACT_ID  ");
		strQuery.append("				AND MT.MONEY_TYPE_CATEGORY_CODE LIKE 'EE%' ");
		strQuery.append("			), 0) AS EMPLOYEE_ASSET, ");
		strQuery.append("		VALUE(");
		strQuery.append("			(SELECT ");
		strQuery.append("				SUM(PCB.TOTAL_BALANCE_AMT) ");
		strQuery.append("			FROM ");
		strQuery.append("				PSW100.PARTICIPANT_CURRENT_BAL_LSA PCB, ");
		strQuery.append("				PSW100.CONTRACT_MONEY_TYPE MT ");
		strQuery.append("			WHERE ");
		strQuery.append("				PCB.PARTICIPANT_ID = PC.PARTICIPANT_ID ");
		strQuery.append("				AND PCB.CONTRACT_ID = PC.CONTRACT_ID ");
		strQuery.append("				AND PCB.MONEY_TYPE_ID = MT.MONEY_TYPE_ID  ");
		strQuery.append("				AND PCB.CONTRACT_ID = MT.CONTRACT_ID  ");
		strQuery.append("				AND MT.MONEY_TYPE_CATEGORY_CODE LIKE 'ER%' ");
		strQuery.append("			), 0) AS EMPLOYER_ASSET, ");
		strQuery.append("		VALUE(");
		strQuery.append("			(SELECT ");
		strQuery.append("				SUM(PCB.TOTAL_BALANCE_AMT) ");
		strQuery.append("			FROM ");
		strQuery.append("				PSW100.PARTICIPANT_CURRENT_BAL_LSA PCB, ");
		strQuery.append("				PSW100.CONTRACT_MONEY_TYPE MT ");
		strQuery.append("			WHERE ");
		strQuery.append("				PCB.PARTICIPANT_ID = PC.PARTICIPANT_ID ");
		strQuery.append("				AND PCB.CONTRACT_ID = PC.CONTRACT_ID ");
		strQuery.append("				AND PCB.MONEY_TYPE_ID = MT.MONEY_TYPE_ID  ");
		strQuery.append("				AND PCB.CONTRACT_ID = MT.CONTRACT_ID  ");
		strQuery.append("				AND MT.MONEY_TYPE_CATEGORY_CODE LIKE 'E%' ");
		strQuery.append("			), 0) AS TOTAL_BALANCE,");
		strQuery.append("			");
		strQuery.append("		VALUE(");
		strQuery.append("			(SELECT ");
		strQuery.append("				SUM(PCB.TOTAL_BALANCE_AMT) ");
		strQuery.append("			FROM ");
		strQuery.append("				PSW100.PARTICIPANT_CURRENT_BAL_LSA PCB, ");
		strQuery.append("				PSW100.CONTRACT_MONEY_TYPE CMT,");
		strQuery.append("				PSW100.MONEY_TYPE MT");
		strQuery.append("			WHERE ");
		strQuery.append("				PCB.PARTICIPANT_ID = PC.PARTICIPANT_ID ");
		strQuery.append("				AND PCB.CONTRACT_ID = PC.CONTRACT_ID ");
		strQuery.append("				AND PCB.MONEY_TYPE_ID = CMT.MONEY_TYPE_ID  ");
		strQuery.append("				AND PCB.CONTRACT_ID = CMT.CONTRACT_ID  ");
		strQuery.append("				AND CMT.MONEY_TYPE_ID = MT.MONEY_TYPE_CODE");
		strQuery.append("				AND CMT.MONEY_TYPE_CATEGORY_CODE LIKE 'ER%' ");
		strQuery.append("				AND MT.MONEY_TYPE_GROUP LIKE 'UM%'");
		strQuery.append("			), 0) AS FORFEITURES ");
		strQuery.append("	FROM ");
		strQuery.append("		PSW100.EMPLOYEE_CONTRACT EC, ");
		strQuery.append("		PSW100.RF_RUNDATE_CS RD, ");
		strQuery.append("		(SELECT ");
		strQuery.append("			A.CONTRACT_ID, ");
		strQuery.append("			A.PARTICIPANT_ID, ");
		strQuery.append("			case ");
		strQuery.append("				when SUM(TOTAL_BALANCE_AMT) is null then 0 ");
		strQuery.append("				else SUM(TOTAL_BALANCE_AMT) ");
		strQuery.append("			end as SUM_TOTAL_BALANCE_AMT ");
		strQuery.append("		FROM ");
		strQuery.append("			EZK100.PARTICIPANT_CONTRACT A ");
		strQuery.append("			LEFT OUTER JOIN PSW100.PARTICIPANT_CURRENT_BAL_LSA PB ON (");
		strQuery.append("					A.CONTRACT_ID = PB.CONTRACT_ID AND A.PARTICIPANT_ID = PB.PARTICIPANT_ID) ");
		strQuery.append("		WHERE ");
		strQuery.append("				A.CONTRACT_ID = ? ");
		strQuery.append("			AND A.CONTRACT_ID = ?");
		strQuery.append("			AND A.CONTRACT_ID = ?");
		strQuery.append("		GROUP BY A.CONTRACT_ID, A.PARTICIPANT_ID ) AS PB, ");
		strQuery.append("		(SELECT ");
		strQuery.append("			SUM( CASE WHEN  SUBSTR(MONEY_TYPE_CATEGORY_CODE,1, 2) = 'EE' THEN 1 ELSE 0 END ) AS EE_COUNT, ");
		strQuery.append("			SUM( CASE WHEN  SUBSTR(MONEY_TYPE_CATEGORY_CODE,1, 2) = 'ER' THEN 1 ELSE 0 END ) AS ER_COUNT  ");
		strQuery.append("		FROM ");
		strQuery.append("			PSW100.CONTRACT_MONEY_TYPE CMT ");
		strQuery.append("		WHERE CMT.CONTRACT_ID = ? ) AS MTCC,");
		strQuery.append("		PSW100.PARTICIPANT_CONTRACT PC ");
		strQuery.append("		left outer join (");
		strQuery.append("			select ");
		strQuery.append("				pbx.profile_id, pbx.contract_id, ");
		strQuery.append("				count(case when money_type_group='UM' then 1 else null end) UM_COUNT, ");
		strQuery.append("				count(case when money_type_group != 'UM' then 1 else null end) NON_UM_COUNT ");
		strQuery.append("			from ");
		strQuery.append("				ezk100.participant_current_balance pbx ");
		strQuery.append("				inner join ezk100.money_type mt1 on pbx.money_Type_id=mt1.money_type_code ");
		strQuery.append("		 	where ");
		strQuery.append("				contract_id=? and total_balance_amt > 0");
		strQuery.append("		 	group by ");
		strQuery.append("				profile_id,contract_id");
		strQuery.append("		) pb1 on pb1.profile_id=pc.profile_id and pb1.contract_id=pc.contract_id ");
		strQuery.append("        LEFT OUTER JOIN (");
		strQuery.append("			select ");
		strQuery.append("				EVP1a.CONTRACT_ID");
		strQuery.append("				,EVP1a.PROFILE_ID");
		strQuery.append("                ,EVP1a.PARAMETER_VALUE as ES_val  ");
		strQuery.append("                ,EVP1a.EFFECTIVE_DATE  as ES_dte ");
		strQuery.append("			from ");
		strQuery.append("				PSW100.EMPLOYEE_VESTING_PARAMETER EVP1a ");
		strQuery.append("            where ");
		strQuery.append("				 EVP1a.PARAMETER_NAME  = 'EMPLOYMENT_STATUS'");
		strQuery.append("                AND EVP1a.EFFECTIVE_DATE = (");
		strQuery.append("									select ");
		strQuery.append("										max(EVP1b.EFFECTIVE_DATE)");
		strQuery.append("                                    from ");
		strQuery.append("										PSW100.EMPLOYEE_VESTING_PARAMETER EVP1b");
		strQuery.append("                                    where ");
		strQuery.append("										 EVP1a.CONTRACT_ID      = EVP1b.CONTRACT_ID");
		strQuery.append("                                        AND EVP1a.PROFILE_ID       = EVP1b.PROFILE_ID ");
		strQuery.append("                                        AND EVP1a.PARAMETER_NAME   = EVP1b.PARAMETER_NAME");
		strQuery.append("                                        AND EVP1b.EFFECTIVE_DATE <= ?");
		strQuery.append("									)  ");
		strQuery.append("				AND EVP1a.CONTRACT_ID = ? ");
		strQuery.append("        ) as EVP_ES on  ");
		strQuery.append("				PC.CONTRACT_ID = EVP_ES.CONTRACT_ID");
		strQuery.append("            AND PC.PROFILE_ID  = EVP_ES.PROFILE_ID   ");
		strQuery.append("	WHERE ");
		strQuery.append("		RD.CYCLE_ID = 'RUNDATE' ");
		strQuery.append("		AND RD.BUSINESS_UNIT = 'PS' ");
		strQuery.append("		AND PC.CONTRACT_ID = ? ");
		strQuery.append("		AND PC.CONTRACT_ID = EC.CONTRACT_ID ");
		strQuery.append("		AND PC.PROFILE_ID  = EC.PROFILE_ID ");
		strQuery.append("		AND PB.CONTRACT_ID = PC.CONTRACT_ID ");
		strQuery.append("		AND PB.PARTICIPANT_ID = PC.PARTICIPANT_ID ");
		strQuery.append("		AND (");
		strQuery.append("				PC.PARTICIPANT_STATUS_CODE = 'AC' ");
		strQuery.append("			OR (");
		strQuery.append("					PC.PARTICIPANT_STATUS_CODE <> 'CN' ");
		strQuery.append("				AND (");
		strQuery.append("						SELECT ");
		strQuery.append("							SUM (PB.TOTAL_BALANCE_AMT) ");
		strQuery.append("						FROM ");
		strQuery.append("							PSW100.PARTICIPANT_CURRENT_BAL_LSA PB ");
		strQuery.append("						WHERE ");
		strQuery.append("								PB.CONTRACT_ID=PC.CONTRACT_ID ");
		strQuery.append("							AND PB.PARTICIPANT_ID=PC.PARTICIPANT_ID ");
		strQuery.append("						) > 0 ");
		strQuery.append("				)");
		strQuery.append("			)");
		strQuery.append("	)");
		strQuery.append("	SELECT ");
		strQuery.append("		COUNT(*) as NO_OF_RECORDS ");
		strQuery.append("	FROM ");
		strQuery.append("		(SELECT ");
		strQuery.append("			FIRST_NAME, ");
		strQuery.append("			LAST_NAME, ");
		strQuery.append("			SOCIAL_SECURITY_NO, ");
		strQuery.append("			EMPLOYER_DIVISION, ");
		strQuery.append("			STATUS, ");
		strQuery.append("			EMPLOYEE_ASSET, ");
		strQuery.append("			EMPLOYER_ASSET, ");
		strQuery.append("			TOTAL_BALANCE, ");
		strQuery.append("			FORFEITURES,");
		strQuery.append("			PROFILE_ID, ");
		strQuery.append("			CONTRACT_ID, ");
		strQuery.append("			ROWNUMBER() OVER ( ORDER BY LAST_NAME ASC, FIRST_NAME ASC ) AS ROW_NUMBER ");
		strQuery.append("		FROM ");
		strQuery.append("			PARTICIPANT_SUMMARY PS  ");
		strQuery.append("		WHERE ");
		strQuery.append("				FORFEITURES > 0  ");
		if (filterFields != null){
			strQuery.append("		AND ");
			strQuery.append( filterFields );
		}
		strQuery.append("		 ) AS FINAL_SUMMARY ");
		
		if(logger.isDebugEnabled()) {
			logger.debug("exit <- getParticipantsCountForSearchCriteriaSQL");
		}
		
		return strQuery.toString();		
	}		
}
