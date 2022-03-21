/**
 * Created on August 23, 2006
 */ 
package com.manulife.pension.ps.service.withdrawal.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SelectBeanListQueryHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.withdrawal.valueobject.SearchParticipantWithdrawalReportData;
import com.manulife.pension.ps.service.withdrawal.valueobject.SearchParticipantWithdrawalRequestItem;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * This is the DAO for SearchParticipantWithdrawal report
 * 
 * @author Harsh Kuthiala
 */
public class SearchParticipantWithdrawalDAO extends BaseDatabaseDAO {


	private static final String className = SearchParticipantWithdrawalDAO.class.getName();

	private static final Logger logger = Logger.getLogger(SearchParticipantWithdrawalDAO.class);

        private static final String SQL_SELECT_PARTICIPANT_CS_DB = 
            "select ccs.contract_id contractNumber, "
            + "ccs.contract_name contractName, "        	
            + "ed.profile_id profileId, "      
            + "ed.last_name lastName, "
            + "ed.first_name firstName, "
            + "ed.middle_initial middleInitial, "
            + "ed.social_security_no ssn, "
            + "ed.birth_date birthDate, "
            + "ed.hire_date hireDate "
            + "from "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "contract_cs ccs, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "employee_contract ed, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "participant_contract pc "
            + "where ccs.contract_id = ed.contract_id "
            + "and ccs.contract_id  = pc.contract_id "
            + "and ed.profile_id = pc.profile_id "
            + "and ccs.contract_id  = ? "
            + "and ccs.manulife_company_id = ? "
            + "and pc.participant_status_code != 'CN' ";
            
        
        private static final String SQL_SELECT_PARTICIPANT_CS_DB_TPA = 
        	"select ccs.contract_id contractNumber, "
            + "ccs.contract_name contractName, "
            + "ed.profile_id profileId, " 
            + "ed.last_name lastName, "
            + "ed.first_name firstName, "
            + "ed.middle_initial middleInitial, "            
            + "ed.social_security_no ssn, " 
            + "ed.birth_date birthDate, "
            + "ed.hire_date hireDate "
            + "from  "
            + PLAN_SPONSOR_SCHEMA_NAME + "contract_cs ccs, " 
            + PLAN_SPONSOR_SCHEMA_NAME + "employee_contract ed, "
            + PLAN_SPONSOR_SCHEMA_NAME + "participant_contract pc, "
            + PLAN_SPONSOR_SCHEMA_NAME + "external_user_tpa_firm eutpaf, "
            + PLAN_SPONSOR_SCHEMA_NAME + "user_profile up "
            + "where ccs.contract_id = ed.contract_id "
            + "and ccs.contract_id  = pc.contract_id "
            + "and ed.profile_id = pc.profile_id "
            + "and ccs.contract_id  = ? "
            + "and ccs.manulife_company_id = ? "
            
			+ "and ccs.third_party_admin_id = eutpaf.tpa_firm_id "
			+ "and eutpaf.user_profile_id = up.user_profile_id  "
			+ "and up.user_profile_id = ? "
			+ "and up.psw_directory_role_code = ? "
			
			+ "and ( "
            + "      (ccs.tpa_staff_plan_ind = 'Y'  and "   
                    + "0 < (select count(*) " 
                    + " from " 
                    + "   psw100.contract_cs a "
                    + ",   psw100.external_user_tpa_firm b "
                    + ",   psw100.user_profile c "
                    + ",   psw100.permission_holder d " 
                    + ",   psw100.permission_grant e "
                    + " where " 
                    + "     a.contract_id = ? "
                    + " and a.third_party_admin_id = b.tpa_firm_id "
                    + " and b.user_profile_id = c.user_profile_id " 
                    + " and c.user_profile_id = ? "
                    + " and d.user_tpa_firm_tpa_firm_id =  b.tpa_firm_id "
                    + " and d.user_tpa_firm_user_profile_id =  b.user_profile_id " 
                    + " and d.perm_relationship_type_code = ? "
                    + " and d.permission_holder_id = e.permission_holder_id " 
                    + " and e.security_task_permission_code = ?)) " 
              + "or "  
              + "  (ccs.tpa_staff_plan_ind = 'N') "
              + ") "			
			+ "and (ccs.contract_status_code in('AC','CF')) "
			+ "and pc.participant_status_code != 'CN' ";
        
        
    //private static final String AND_CONTRACT_NUMBER_CLAUSE = " and ccs.contract_id  = ? ";          
   	private static final String AND_LAST_NAME_CLAUSE = " and ed.last_name like ? ";//CL 115918 - removed upper
   	private static final String AND_SSN_CLAUSE = " and ed.social_security_no = ? ";
   	
   	private static final String ORDER_BY_CLAUSE = " order by ed.last_name asc, ed.first_name asc, ed.social_security_no desc ";
   	private static final String FOR_FETCH_CLAUSE = " for fetch only ";

	
	public static SearchParticipantWithdrawalReportData getParticipant(ReportCriteria criteria) 
		throws SystemException {

		logger.debug("entry -> getParticipants");
		logger.debug("Report criteria -> " + criteria.toString());
		
        SearchParticipantWithdrawalReportData reportDataVO = null;
		List searchParams = new ArrayList();
		String query = generateSqlQuery(criteria, searchParams);
		SelectBeanListQueryHandler searchHandler = 
			new SelectBeanListQueryHandler(CUSTOMER_DATA_SOURCE_NAME, query,
			SearchParticipantWithdrawalRequestItem.class);
        try {
            logger.debug("Executing  Query: " + query);
            List withdrawalsList = (List) searchHandler.select(searchParams.toArray());
            reportDataVO = 
                new SearchParticipantWithdrawalReportData(criteria, withdrawalsList.size());
            
            reportDataVO.setDetails(withdrawalsList);
        } catch (DAOException e) {
            throw handleDAOException(e, className, "getParticipants",
                    "Problem occurred with sub query: " + query + " with criteria: " + criteria);
        }
		logger.debug("exit <- getParticipants");
		return reportDataVO;
	}
    
    private static String generateSqlQuery (ReportCriteria criteria, List paramList) 
    		throws SystemException {
        
    	StringBuffer query;
		Boolean isTpa = (Boolean)criteria
				.getFilterValue(SearchParticipantWithdrawalReportData.FILTER_IS_TPA);
        Integer contractId = (Integer)criteria
    			.getFilterValue(SearchParticipantWithdrawalReportData.FILTER_CONTRACT_ID);

		//contractId 
    	paramList.add(contractId);

		//US or NY
		String manulifeCompanyId;
		String siteLocation = (String)criteria.getFilterValue(SearchParticipantWithdrawalReportData.FILTER_SITE_LOCATION);
		if(siteLocation.toUpperCase().equals("US")) {
			manulifeCompanyId="019";
		} else if (siteLocation.toUpperCase().equals("NY") ){
			manulifeCompanyId="094";
		} else {
			throw new SystemException("InvalidSiteLocation");
		}
		paramList.add(manulifeCompanyId);
		
		if(contractId != null && isTpa != null && isTpa.booleanValue()) {
			query = new StringBuffer(SQL_SELECT_PARTICIPANT_CS_DB_TPA);
			Integer userProfileId  = (Integer) criteria
					.getFilterValue(SearchParticipantWithdrawalReportData.FILTER_USER_PROFILE_ID);
			paramList.add(userProfileId); //1576495
			paramList.add("TPA");         //'TPA'
			paramList.add(contractId);
			paramList.add(userProfileId);
			paramList.add("USTF");        //'USTF'
			paramList.add("TSPA");        //'TSPA'
			
    	} else {
    		query = new StringBuffer(SQL_SELECT_PARTICIPANT_CS_DB);
    	}

        String lastName = (String) criteria
				.getFilterValue(SearchParticipantWithdrawalReportData.FILTER_LAST_NAME);
		if (lastName != null) {
			query.append(AND_LAST_NAME_CLAUSE);
			paramList.add(lastName.toUpperCase() + "%");			
		}        
        String ssn = (String) criteria
				.getFilterValue(SearchParticipantWithdrawalReportData.FILTER_SSN);
		if (ssn != null) {
			query.append(AND_SSN_CLAUSE);
			paramList.add(ssn);	
		}        
		
		query.append(ORDER_BY_CLAUSE);
		query.append(FOR_FETCH_CLAUSE);
        return query.toString();
    }
    
}