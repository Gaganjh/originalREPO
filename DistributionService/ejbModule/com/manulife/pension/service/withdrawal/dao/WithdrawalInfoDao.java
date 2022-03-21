package com.manulife.pension.service.withdrawal.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.withdrawal.exception.WithdrawalDaoException;
import com.manulife.pension.service.withdrawal.valueobject.MultiPayeeMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.TaxesFlag;
import com.manulife.pension.service.withdrawal.valueobject.UserName;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalMultiPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestLoan;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;

/**
 * Data Access Object for the general and participant withdrawal information to be used on the
 * Before Proceeding page and Withdrawal Request pages.
 * 
 * @author Mihai Popa
 */
public final class WithdrawalInfoDao extends BaseDatabaseDAO {

    private static final String CLASS_NAME = WithdrawalInfoDao.class.getName();

    private static final String GET_WITHDRAWAL_INFO_SQL = "{call " + CUSTOMER_SCHEMA_NAME
            + "GET_GENERAL_WITHDRAWAL_INFO(?,?)}";

    private static final String GET_PARTICIPANT_WITHDRAWAL_INFO_SQL = "call "
            + CUSTOMER_SCHEMA_NAME + "GET_PARTICIPANT_WITHDRAWAL_INFO(?,?)";
    
   
 // Retrieves the get Participant money type info
    // Accessed by: PSW100.GET_PARTICIPANT_MONEY_TYPE_INFO
    
       
    public static final String GET_PARTICIPANT_MONEY_TYPE_INFO_SQL_REP =" select distinct ptmt.money_type_tax_code as taxType "
    	       +   "  , rtrim(cmt.money_type_id) as moneyTypeId "
    	       +   "  , rtrim(mt.roth_money_type_ind ) as roth_money_type_ind from "
    	       +    PLAN_SPONSOR_SCHEMA_NAME
    	       +  "contract_money_type_history cmth, "
    	       +   PLAN_SPONSOR_SCHEMA_NAME
    	       +  "contract_money_type cmt, "
    	       +    PLAN_SPONSOR_SCHEMA_NAME
    	       +  "money_type mt, "
    	       +   PLAN_SPONSOR_SCHEMA_NAME
    	       +  "plan_type_money_type ptmt "
    	       +   " where cmth.contract_id = cmt.contract_id "
    	       +   "  and mt.money_type_code in (money__type__code)" 
    	       +   "  and cmth.money_type_id = cmt.money_type_id "
    	       +  "  and cmt.money_type_id = mt.money_type_code "
    	       +  "  and sysdate between cmth.start_effective_date and (cmth.end_effective_date - 1 day)" 
    	       +  "  and cmt.contract_id = ? "
    	       +  " and ptmt.money_type_code=cmt.money_type_id " 
    	       +  "  order by "
    	       +  "  moneyTypeId "
    	       +  " for fetch only" ;
    
    public static final String GET_MULTIPLE_DESTINATION_DISPLAY_SQL = " select MPCL.PAYEE_CATEGORIES,MPCL.PAYEE_CATEGORIES_DESC,MPCL.PAYEE_TYPES"
    		+ " from "
    		+  STP_SCHEMA_NAME
    		+ " MULTI_PAYEE_CATEGORIES_LOOKUP MPCL"
    		+ " inner join "
    		+  STP_SCHEMA_NAME
    		+ " MULTI_PAYEE_DISPLAY_RULE_LOOKUP MPDRL"
    		+ " on MPDRL.PAYEE_CATEGORIES_ID = MPCL.PAYEE_CATEGORIES_ID"
    		+ " where MPDRL.TAX_CODE_BOT_IND = ?"
    		+ " and MPDRL.TAX_CODE_EAR_IND = ?"
    		+ " and MPDRL.EAR_ROTH_IND = ?"
    		+ " and MPDRL.EAR_NON_ROTH_IND = ?" ;

    /**
     * SQL to retrieve applicable WD reasons based on the contract status code and participant
     * status code.
     */
    private static final String GET_PARTICIPANT_WITHDRAWAL_CODES_SQL = "select WITHDRAWAL_REASON_CODE, RTRIM(WITHDRAWAL_REASON_DESC_SHORT) from "
            + CUSTOMER_SCHEMA_NAME
            + "WITHDRAWAL_REASON "
            + "where WITHDRAWAL_REASON_CODE in ( "
            + "  select distinct AA.TRANSACTION_REASON_CODE from "
            + "    (select TRANSACTION_REASON_CODE from "
            + CUSTOMER_SCHEMA_NAME
            + "TRANSACTION_STATUS_CHECK where TRANSACTION_TYPE_CODE='WD' and CONT_PART_CODE='C' and STATUS_CODE = ?) as AA, "
            + "    (select TRANSACTION_REASON_CODE from "
            + CUSTOMER_SCHEMA_NAME
            + "TRANSACTION_STATUS_CHECK where TRANSACTION_TYPE_CODE='WD' and CONT_PART_CODE='P' and STATUS_CODE = ?) as AB "
            + "  where AA.TRANSACTION_REASON_CODE = AB.TRANSACTION_REASON_CODE "
            + ") and ONLINE_WITHDRAWAL_IND = 'Y' order by DISPLAY_ORDER FOR READ ONLY";

    // AP 20061206: retrieves the first name and last name for a list of users profile IDs
    private static final String SELECT_USER_NAMES_SQL = "select USER_PROFILE_ID, FIRST_NAME, LAST_NAME from "
            + PLAN_SPONSOR_SCHEMA_NAME + "USER_PROFILE" + " where USER_PROFILE_ID in ";
    
    
    private static final String MULTI_PAYEE_FLAG = "select * from STP100.MULTI_PAYEE_TAX_INDICATOR_LOOKUP";
    
    private static final String GET_CONTRACT_BASED_MULTIPLE_DESTINATION_SQL = "select count(*) from PSW100.CONTRACT_CS where contract_id = ? and CHECK_PAYABLE_TO_CODE in ('CL','TR')";

    private static Logger logger = Logger.getLogger(WithdrawalInfoDao.class);

    /**
     * Retrieves the Withdrawal Info data from the db2 stored procedure.
     * 
     * @return WithdrawalInfo
     * @throws SystemException
     */
    public static WithdrawalInfo getWithdrawalInfo(int participantId, int contractId)
            throws SystemException {
        /*
         * From Stored Procedure Declaration EZK100.GET_GENERAL_WITHDRAWAL_INFO Set #1 -
         * ROTH_MONEY_TYPE_ID CHAR(5) Set #2 - PERSONAL_BROKERAGE_ACCOUNT_IND CHAR(1) Set #3 -
         * TOTAL_BALANCE_AMT_SUM DECIMAL(13,2) Set #4 - REQ_DATE DATE STATUS CHAR(11) INITIATED_BY
         * CHAR(11) LOAN_REQUEST_ID SMALLINT REQ_EXPIRY_DATE DATE LOAN_REQUEST_STATUS_CODE CHAR(2)
         * APV_DATE DATE APV_EXPIRY_DATE DATE Set #5 - PARTICIPANT_STATUS_CODE CHAR(2)
         * PARTICIPANT_HAS_ROTH_MONEY_IND CHAR(1)
         * 
         * DYNAMIC RESULT SETS 5
         */

        Connection conn = null;
        CallableStatement statement = null;
        WithdrawalInfo vo = null;
        try {
            // setup the connection and the statement
            conn = getReadUncommittedConnection(CLASS_NAME, CUSTOMER_DATA_SOURCE_NAME);
            statement = conn.prepareCall(GET_WITHDRAWAL_INFO_SQL);

            if (logger.isDebugEnabled())
                logger.debug("Calling Stored Procedure: " + GET_WITHDRAWAL_INFO_SQL);

            // set the input parameters
            statement.setBigDecimal(1, intToBigDecimal(participantId));
            statement.setBigDecimal(2, intToBigDecimal(contractId));

            vo = new WithdrawalInfo();
            // execute the stored procedure
            statement.execute();

            // get the first resultSet: SQL_SELECT_CONTRACT_ROTH_MONEY_TYPE
            ResultSet resultSet = statement.getResultSet();
            
            if (resultSet != null) {
                
                if (resultSet.next()) {
                    // FIXME -- what is the preferred way ?
                    // vo.setRothMoneyTypeId(resultSet.getString(1));
                    vo.setContractRothMoneyTypeId(resultSet.getString("ROTH_MONEY_TYPE_ID"));
                }
                
            }
            
            // second result set: SQL_SELECT_PBA_IND
            if (statement.getMoreResults()) {
                resultSet = statement.getResultSet();
                
                if (resultSet != null) {
                    
                    while (resultSet.next()) {
                        vo.setPersonalBrokerageAccountInd(resultSet
                                .getBoolean("PERSONAL_BROKERAGE_ACCOUNT_IND"));
                    }
                    
                }
                
            }

            // third result set: SQL_SELECT_CURRENT_BALANCE
            if (statement.getMoreResults()) {
                resultSet = statement.getResultSet();
                
                if (resultSet != null) {
                    
                    while (resultSet.next()) {
                        vo.setTotalBalance(resultSet.getBigDecimal("TOTAL_BALANCE_AMT_SUM"));
                    }
                    
                }
                
            }

            // forth result set: SQL_SELECT_PARTICIPANT_I_LOANS
            if (statement.getMoreResults()) {
                resultSet = statement.getResultSet();
                
                if (resultSet != null) {
                    
                    while (resultSet.next()) {
                        vo.setRequestDate(resultSet.getDate("REQ_DATE"));
                        vo.setContractStatus(resultSet.getString("STATUS"));
                        vo.setInitiatedBy(resultSet.getString("INITIATED_BY"));
                        vo.setLoanRequestId(resultSet.getShort("LOAN_REQUEST_ID"));
                        vo.setRequestExpiryDate(resultSet.getDate("REQ_EXPIRY_DATE"));
                        vo.setContractStatus(resultSet.getString("LOAN_REQUEST_STATUS_CODE"));
                        vo.setApvDate(resultSet.getDate("APV_DATE"));
                        vo.setApvExpiryDate(resultSet.getDate("APV_EXPIRY_DATE"));
                    }
                    
                }
                
            }

            // fifth result set: SQL_GET_PARTICIPANT_STATUS_ROTH
            if (statement.getMoreResults()) {
                resultSet = statement.getResultSet();
                
                if (resultSet != null) {
                    
                    while (resultSet.next()) {
                        vo.setParticipantStatus(resultSet.getString("PARTICIPANT_STATUS_CODE"));
                        String participantRothMoney = resultSet
                                .getString("PARTICIPANT_HAS_ROTH_MONEY_IND");
                        if (participantRothMoney != null && "Y".equals(participantRothMoney)) {
                            vo.setParticipantHasRothMoney(true);
                        } else {
                            vo.setParticipantHasRothMoney(false);
                        }
                    }
                    
                }
                
            }
            // sixth result set: SQL_GET_PBA_LOAN_IND
            if (statement.getMoreResults()) {
                resultSet = statement.getResultSet();
                
                if (resultSet != null) {
                    
                    while (resultSet.next()) {
                        String pbaMoneyInd = resultSet.getString("PARTICIPANT_PBA_BALANCE_IND");
                        String isLoans = resultSet.getString("CONTRACT_LOAN_IND");
    
                        if (pbaMoneyInd != null && "Y".equals(pbaMoneyInd)) {
                            vo.setPbaMoneyTypeInd(true);
                        } else {
                            vo.setPbaMoneyTypeInd(false);
                        }
                        if (isLoans != null && "Y".equals(isLoans)) {
                            vo.setLoansAllowedInd(true);
                        } else {
                            vo.setLoansAllowedInd(false);
                        }
                    }
                    
                }

            }

        } catch (SQLException e) {
            throw new SystemException(e, WithdrawalInfoDao.class.getName(), "getWithdrawalInfo",
                    "Failed during GET_WITHDRAWAL_INFO_SQL stored proc call.");
        } finally {
            close(statement, conn);
        }

        return vo;
    }

    /**
     * Retrieves the Participant Withdrawal Info data from the db2 stored procedure.
     * 
     * @return ParticipantInfo
     * @throws SystemException
     */
    /*
     * From Stored Procedure Declaration EZK100.GET_PARTICIPANT_WITHDRAWAL_INFO Set #1 -
     * Participant's contribution / investment dates LAST_CONTRIB_APPLIC_DATE DATE
     * LAST_INVESTMENT_DT DATE Set #2 - Participant's after tax contributions MONEY_TYPE_ID CHAR(5)
     * Set #3 - Payment information TRUSTEE_NAME CHAR(90) MTA_IND CHAR(1) CONTRACT_NAME CHAR(30)
     * EFFECTIVE_DATE DATE CHECK_PAYABLE_TO_CODE CHAR(2) CHECK_MAILED_TO_CODE CHAR(2)
     * CONTRACT_STATUS_CODE CHAR(2) Set #4 - Participant's loan list LOAN_ID INTEGER(4)
     * OUTSTANDING_LOAN DECIMAL(13, 2) Set #5 - Participant MT balances (excluding loans and PBA)
     * MONEY_TYPE_ID CHAR(5) CONTRACT_MONEY_TYPE_LONG_NAME CHAR(30) MONEY_TYPE_ALIAS_ID CHAR(5)
     * CONTRACT_MONEY_TYPE_CATEGORY_CODE CHAR(2) TOTAL_BALANCE_AMT DECIMAL(13,2) ROLLOVER_IND
     * CHAR(1) VOLUNTARY_CONTRIBUTION_IND CHAR(1) PRE1987_IND CHAR(1) Set #6 - Generic Money Type
     * list MONEY_TYPE_CODE CHAR(5) MONEY_TYPE_ALIAS_ID CHAR(5)
     * 
     */
    public static ParticipantInfo getParticipantInfo(Long participantId, Integer contractId)
            throws SystemException {

        if (logger.isDebugEnabled())
            logger.debug("entry -> getParticipantInfo(" + participantId + ", " + contractId + ")");

        Connection conn = null;
        CallableStatement statement = null;
        ParticipantInfo vo = null;
        try {
            // setup the connection and the statement
            conn = getReadUncommittedConnection(CLASS_NAME, CUSTOMER_DATA_SOURCE_NAME);
            statement = conn.prepareCall(GET_PARTICIPANT_WITHDRAWAL_INFO_SQL);

            if (logger.isDebugEnabled())
                logger.debug("Calling Stored Procedure: " + GET_PARTICIPANT_WITHDRAWAL_INFO_SQL);

            // set the input parameters
            statement.setBigDecimal(1, new BigDecimal(participantId));
            statement.setBigDecimal(2, intToBigDecimal(contractId));

            vo = new ParticipantInfo();

            // execute the stored procedure
            statement.execute();

            ResultSet resultSet = statement.getResultSet();

            // Process the first result set: SQL_SELECT_PARTICIPANT_DATES
            if (resultSet != null) {
                
                if (resultSet.next()) {
                    vo.setParticipantStatusCode(resultSet.getString("PARTICIPANT_STATUS_CODE"));
                    vo.setLastContributionDate(new java.util.Date(resultSet.getDate(
                            "LAST_CONTRIB_APPLIC_DATE").getTime()));
    
                    // Updated this, as we should only be using the last contribution applicable date.
                    vo.setLastInvestmentDate(new java.util.Date(resultSet.getDate(
                            "LAST_CONTRIB_APPLIC_DATE").getTime()));
                }
                
            }

            // Process the second result set: SQL_SELECT_AFTER_TAX_MONEY
            if (statement.getMoreResults()) {
                resultSet = statement.getResultSet();
                // If the query returns any after tax money types set the flag to true
                vo.setHasAfterTaxContributions(resultSet != null && resultSet.next());
            }

            // Process the third result set: SQL_SELECT_CONTRACT_INFO
            if (statement.getMoreResults()) {
                resultSet = statement.getResultSet();
                
                if (resultSet != null) {
                    
                    if (resultSet.next()) {
                        vo.setTrusteeName(resultSet.getString("TRUSTEE_NAME"));
                        vo.setIsMTAContract("Y".equalsIgnoreCase(resultSet.getString("MTA_IND")));
                        vo.setContractName(resultSet.getString("CONTRACT_NAME"));
                        vo.setContractEffectiveDate(resultSet.getDate("EFFECTIVE_DATE"));
                        vo.setChequePayableToCode(resultSet.getString("CHECK_PAYABLE_TO_CODE"));
                        vo.setContractStatusCode(resultSet.getString("CONTRACT_STATUS_CODE"));
                        if (resultSet.getBigDecimal("THIRD_PARTY_ADMIN_ID") != null) {
                            vo.setThirdPartyAdminId(new Boolean(Boolean.TRUE));
                        } else {
                            vo.setThirdPartyAdminId(new Boolean(Boolean.FALSE));
                        }
                        vo.setManulifeCompanyId(resultSet.getString("MANULIFE_COMPANY_ID"));
                    }
                    
                }
                
            }

            // FIXME: Need to retrieve PENSION_PLAN.PLAN_NAME

            // Process the fourth result set: SQL_SELECT_LOAN_DETAIL
            if (statement.getMoreResults()) {
                resultSet = statement.getResultSet();

                Collection<WithdrawalRequestLoan> loans = new ArrayList<WithdrawalRequestLoan>();
                
                if (resultSet != null) {
                    
                    while (resultSet.next()) {
                        WithdrawalRequestLoan loan = new WithdrawalRequestLoan();
                        loan.setLoanNo(resultSet.getInt("LOAN_ID"));
                        loan.setOutstandingLoanAmount(resultSet.getBigDecimal("OUTSTANDING_LOAN"));
                        loans.add(loan);
                    }
                    
                }
                
                vo.setOutstandingLoans(loans);
            }

            // Process the fifth resultset: SQL_GET_PARTICIPANT_MONEY_TYPES_BALANCE_LIST
            if (statement.getMoreResults()) {
                resultSet = statement.getResultSet();

                Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>();
                
                if (resultSet != null) {
                    
                    while (resultSet.next()) {
                        WithdrawalRequestMoneyType mt = new WithdrawalRequestMoneyType();
                        mt.setMoneyTypeId(resultSet.getString("MONEY_TYPE_ID"));
                        mt.setMoneyTypeName(resultSet.getString("CONTRACT_MONEY_TYPE_LONG_NAME"));
                        mt.setMoneyTypeAliasId(resultSet.getString("MONEY_TYPE_ALIAS_ID"));
                        mt.setMoneyTypeCategoryCode(resultSet.getString("MONEY_TYPE_CATEGORY_CODE"));
                        mt.setTotalBalance(resultSet.getBigDecimal("TOTAL_BALANCE_AMT"));
                        mt.setIsRolloverMoneyType("Y".equalsIgnoreCase(resultSet
                                .getString("ROLLOVER_IND")));
                        mt.setIsVoluntaryContributionMoneyType("Y".equalsIgnoreCase(resultSet
                                .getString("VOLUNTARY_CONTRIBUTION_IND")));
                        mt.setIsPre1987MoneyType("Y".equalsIgnoreCase(resultSet
                                .getString("PRE1987_IND")));
    
                        moneyTypes.add(mt);
                    }
                    
                }
                
                vo.setMoneyTypes(moneyTypes);
            }

            // Process the sixth resultset: SQL_GET_MONEY_TYPE_ALIASES
            if (statement.getMoreResults()) {
                Map<String, String> moneyTypeAliases = new HashMap<String, String>();
                resultSet = statement.getResultSet();
                
                if (resultSet != null) {
                    
                    while (resultSet.next()) {
                        moneyTypeAliases.put(resultSet.getString("MONEY_TYPE_ID"), resultSet
                                .getString("CONTRACT_MONEY_TYPE_LONG_NAME"));
                    }
                    
                }
                
                vo.setMoneyTypeAliases(moneyTypeAliases);
            }

        } catch (SQLException e) {
            throw new SystemException(e, WithdrawalInfoDao.class.getName(), "getParticipantInfo("
                    + participantId + ", " + contractId + ")",
                    "Failed during GET_PARTICIPANT_WITHDRAWAL_INFO_SQL stored proc call.");
        } finally {
            close(statement, conn);
        }

        // Load the participant PBA indicator

        // Note, here we cast from Long to long, then from long to int. Syntax looks funny.
        final WithdrawalInfo withdrawalInfo = getWithdrawalInfo((int) (long) participantId,
                contractId);

        vo.setParticipantHasPbaMoney(BooleanUtils.toBooleanObject(withdrawalInfo
                .isPbaMoneyTypeInd()));

        // Load the participant Roth indicator
        vo.setParticipantHasRothMoney(BooleanUtils.toBooleanObject(withdrawalInfo
                .isParticipantHasRothMoney()));

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getParticipantInfo(" + participantId + ", " + contractId + ")");
        }

        return vo;
    }

    /**
     * Returns an ordered collection of WD reasons. The order in which the codes are returned in the
     * specified display order.
     * 
     * @param contractStatus Contract status code
     * @param participantStatus Participant status code
     * @return Ordered list of withdrawal reasons matching the contract and participant contract
     *         status which are enabled for OnlineWithdrawals.
     * 
     * @see http://mlisusgpsdev1/usgpdict/ASP_DataElementValuesForUserView.asp?ParmUserView=PARTICIPANT%5FCONTRACT
     */
    // FIXME: May need to reuse the prepared statement to improve speed;
    public static Collection<DeCodeVO> getParticipantWithdrawalReasons(final String contractStatus,
            final String participantStatus) throws SystemException {

        final Collection<DeCodeVO> reasons = new ArrayList<DeCodeVO>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            // setup the connection and the statemnt
            conn = getReadUncommittedConnection(CLASS_NAME, CUSTOMER_DATA_SOURCE_NAME);
            pstmt = conn.prepareStatement(GET_PARTICIPANT_WITHDRAWAL_CODES_SQL);
            pstmt.setString(1, contractStatus);
            pstmt.setString(2, participantStatus);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                reasons.add(new DeCodeVO(rs.getString(1), rs.getString(2)));
            }
            rs.close();

        } catch (SQLException e) {
            throw new SystemException(e, WithdrawalInfoDao.class.getName(),
                    "getParticipantWithdrawalReasons(contract status=" + contractStatus
                            + ", participant status=" + participantStatus + ")",
                    "Failed during GET_PARTICIPANT_WITHDRAWAL_CODES_SQL call.");
        } finally {
            close(pstmt, conn);
        }

        return reasons;
    }

    /**
     * Returns the user names (last name, first name) for each unique user_profile_id in the given
     * list.
     * 
     * @param Collection of unique user profile IDs
     * @return Map of UserName VOs keyed by user profile ID
     */
    public static Map<Integer, UserName> getUserNames(Collection<Integer> userProfileIds)
            throws DistributionServiceException {

        Map<Integer, UserName> userNames = new HashMap<Integer, UserName>();

        if (userProfileIds == null || userProfileIds.isEmpty())
            return userNames;

        Connection conn = null;
        PreparedStatement statement = null;
        String idSet = "";
        try {
            // setup the connection and the statement
            try {
                conn = getReadUncommittedConnection(CLASS_NAME, CUSTOMER_DATA_SOURCE_NAME);
            } catch (SystemException systemException) {
                throw new WithdrawalDaoException(systemException, CLASS_NAME, "getUserNames",
                        "Error getting connection.");
            } // end try/catch

            idSet = "(";
            for (Iterator i = userProfileIds.iterator(); i.hasNext();) {
                idSet += i.next().toString();
                if (i.hasNext())
                    idSet += ", ";
            }
            idSet += ")";

            statement = conn.prepareCall(SELECT_USER_NAMES_SQL + idSet);
            statement.execute();

            ResultSet rs = statement.getResultSet();
            
            if (rs != null) {
                
                while (rs.next()) {
                    for (Integer id : userProfileIds) {
                        if (rs.getInt("USER_PROFILE_ID") == id) {
                            userNames.put(id, new UserName(id, rs.getString("LAST_NAME"), rs
                                    .getString("FIRST_NAME")));
                        }
                    }
                }
                
            }

        } catch (SQLException sqlException) {
            throw new WithdrawalDaoException(sqlException, CLASS_NAME, "getNoteCreatorNames",
                    "Failed during execution of SELECT_CREATOR_NAMES_SQL (" + idSet + ").");
        } finally {
            close(statement, conn);
        }

        return userNames;
    }

	public static List<MultiPayeeMoneyType> getParticipantMoneytypetaxCode(Integer contractId, String moneyTypes) throws SystemException {
		
			Connection conn = null;
	        PreparedStatement statement = null;
	        ResultSet resultSet = null;
	        List<MultiPayeeMoneyType> multiPayeeList = new ArrayList<>();
	        MultiPayeeMoneyType multiPayeeMoneyType = null;
	       
	        try {
	            // setup the connection and the statement
	            conn = getReadUncommittedConnection(CLASS_NAME, CUSTOMER_DATA_SOURCE_NAME);
	            String sql = GET_PARTICIPANT_MONEY_TYPE_INFO_SQL_REP.replace("money__type__code",moneyTypes);
	            statement = conn.prepareCall(sql);

	            if (logger.isDebugEnabled())
	                logger.debug("Calling Stored Procedure: " + GET_PARTICIPANT_MONEY_TYPE_INFO_SQL_REP);

	            // set the input parameters	
	            statement.setBigDecimal(1, intToBigDecimal(contractId));
	           
	           
	            // execute the stored procedure	         
	            statement.execute();

	            resultSet = statement.getResultSet();
	            if (resultSet != null) {
	                    
	                    while (resultSet.next()) {
	                      multiPayeeMoneyType = new MultiPayeeMoneyType();
	                      multiPayeeMoneyType.setTaxCode(resultSet.getString("TAXTYPE"));
	                      multiPayeeMoneyType.setMoneyTypeId(resultSet.getString("MONEYTYPEID"));
	                      multiPayeeMoneyType.setRothInd(resultSet.getString("ROTH_MONEY_TYPE_IND"));
	                      multiPayeeList.add(multiPayeeMoneyType);
	                    }
	                    
	                }
	        }catch (SQLException e) {
	            throw new SystemException(e, WithdrawalInfoDao.class.getName(), "getParticipantMoneytypetaxCode(" + contractId + ")",
	                    "Failed during GET_PARTICIPANT_MONEY_TYPE_INFO_SQL stored proc call.");
	        } finally {
	            close(statement, conn);
	        }
	        	return multiPayeeList;
	        }
	public static List <WithdrawalMultiPayee> getMultipayeeDiplayFlag(String botInd,String earInd,String rothInd,String nonRothInd) throws SystemException{
		
		List <WithdrawalMultiPayee> multiPayee = new ArrayList ();
		
		 Connection conn = null;
	     PreparedStatement statement = null;
	     ResultSet resultSet = null;
	     WithdrawalMultiPayee mpe = null;
	       // String taxType = null;
	       // String rothMoneyType = null;
	        try {
	            // setup the connection and the statement
	            conn = getDefaultConnection(CLASS_NAME, BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME);
	            statement = conn.prepareStatement(GET_MULTIPLE_DESTINATION_DISPLAY_SQL);
	            statement.setString(1, botInd);
	            statement.setString(2, earInd);
	            statement.setString(3, rothInd);
	            statement.setString(4, nonRothInd);
	            statement.execute();

	            resultSet = statement.getResultSet();
	            if (resultSet != null) {
	                    
	                    while (resultSet.next()) {
	                    	mpe = new WithdrawalMultiPayee();
	                    	mpe.setPayeeCategories(resultSet.getString("PAYEE_CATEGORIES"));
	                    	mpe.setPayeeCategoriesDesc(resultSet.getString("PAYEE_CATEGORIES_DESC"));
	                    	mpe.setPayeeType("PAYEE_TYPES");
	                    	multiPayee.add(mpe);
	                    	
	                    }
	            }
	            if (logger.isDebugEnabled())
	                logger.debug("Calling Stored Procedure: " + GET_MULTIPLE_DESTINATION_DISPLAY_SQL);
	        }catch (SQLException e) {
	            throw new SystemException(e, WithdrawalInfoDao.class.getName(), "getMultipayeeDiplayFlag",
	                    "Failed during GET_MULTIPLE_DESTINATION_DISPLAY_SQL stored proc call.");
	        } finally {
	            close(statement, conn);
	        }		
		return multiPayee;
		
	}	
	/**
	 * getPayeeTaxFlag
	 */
	public static List<TaxesFlag>getPayeeTaxFlag() throws SQLException, SystemException{
		 
		 List <TaxesFlag>taxesFlag = new ArrayList();
		 TaxesFlag taxFlag = null;
		 
		 Connection conn = null; 
		 PreparedStatement statement = null;
	     ResultSet resultSet = null;
	     try {
			
			 conn = getDefaultConnection(CLASS_NAME, BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME);
	            statement = conn.prepareStatement(MULTI_PAYEE_FLAG);
			  statement.executeQuery();
			  resultSet = statement.getResultSet();
			  if(resultSet !=null) {
				  while (resultSet.next()) {
					  taxFlag = new TaxesFlag();
					   taxFlag.setPayee(resultSet.getString("PAYEE"));
					   taxFlag.setNonTaxable(resultSet.getString("NON_TAXABLE"));
					   taxFlag.setTaxable(resultSet.getString("TAXABLE"));
					   taxFlag.setRothTaxable(resultSet.getString("ROTH_TAXABLE"));
					   taxFlag.setRothNonTax(resultSet.getString("ROTH_NON_TAX"));
					   taxFlag.setRothIRA(resultSet.getString("ROTH_IRA"));
					   taxFlag.setPayeeCategory(resultSet.getString("PAYEE_CATEGORY"));
					   taxesFlag.add(taxFlag);
				  }
			  }if (logger.isDebugEnabled())
	                logger.debug("Calling Stored Procedure: " + MULTI_PAYEE_FLAG);
	        }catch (SQLException e) {
	            throw new SystemException(e, WithdrawalInfoDao.class.getName(), "getMultipayeeDiplayFlag",
	                    "Failed during GET_MULTIPLE_DESTINATION_DISPLAY_SQL stored proc call.");
	        } finally {
	            close(statement, conn);
	        }		
	     return taxesFlag;
	 }

	public static boolean getvalidateMultiDestination(Integer contractId) throws SystemException {
		// TODO Auto-generated method stub
		 Connection conn = null; 
		 PreparedStatement statement = null;
	     ResultSet resultSet = null;	 	
	     try {
	            // setup the connection and the statement
	            conn = getDefaultConnection(CLASS_NAME, BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME);
	            statement = conn.prepareStatement(GET_CONTRACT_BASED_MULTIPLE_DESTINATION_SQL);
	            statement.setBigDecimal(1, intToBigDecimal(contractId));
	            statement.executeQuery();
				  resultSet = statement.getResultSet();
				  if (resultSet != null) {				  
					  while (resultSet.next()) {					  
						  if(resultSet.getInt(1) > 0) {
							  return true;  
						  }
					  }
				  }					
				   } catch (Exception e) {
					
					e.printStackTrace();
				}			      
	       finally {
	            close(statement, conn);
	        }		
	
		  return false;
	}
}
	

