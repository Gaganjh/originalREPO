package com.manulife.pension.ps.service.participant.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;			//CL 110234
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SelectSingleOrNoValueQueryHandler;
import com.intware.dao.jdbc.StoredProcedureHandler;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.participant.valueobject.InvestmentOptionVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountAssetsByRiskVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountDetailsVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantDeferralVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantFundMoneyTypeDetailVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantFundSummaryVO;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantLoanDetails;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantNetContribEarningsVO;
import com.manulife.pension.ps.service.report.investment.valueobject.ContractFundDetails;
import com.manulife.pension.ps.service.report.investment.valueobject.FundCategory;
import com.manulife.pension.service.account.entity.ContractFund;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.environment.EnvironmentServiceHelper;
import com.manulife.pension.service.fund.cache.FundInfoCache;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.service.fund.valueobject.AssetClassVO;
import com.manulife.pension.service.fund.valueobject.FundVO;
import com.manulife.pension.service.fund.valueobject.SvgifFund;

public class ParticipantAccountDAO extends BaseDatabaseDAO  {
	
	private static final String className = ParticipantAccountDAO.class.getName();
	private static final Logger logger = Logger.getLogger(ParticipantAccountDAO.class);
	
	private static final String SELECT_BY_PARTICIPANT_ID =
		"call " + CUSTOMER_SCHEMA_NAME + "SELECT_BY_PARTICIPANT_ID(?)";

	private static final String GET_PARTICIPANT_ACCOUNT_DTL = 
        "call " + CUSTOMER_SCHEMA_NAME + "GET_PARTICIPANT_ACCOUNT_MONEY_TYPE_DTL(?,?,?,?,?,?,?,?,?)"; 		//CL 110234
        
	private static final String GET_PARTICIPANT_DEFERRAL_DTL = 
        "call " + CUSTOMER_SCHEMA_NAME + "GET_PARTICIPANT_DEFERRAL_DTL(?,?)"; 

	private static final String GET_PARTICIPANT_NET_EE_DEFERRAL_CONTRIBUTIONS =	
        "call " + APOLLO_STORED_PROC_SCHEMA_NAME + ".LP_TPA_NET_EEDEF(?,?,?,?,?,?)";
	public static final String ERROR_APOLLO_SP_BATCH = "9999";
	
	
	private static final String SELECT_FUND_RATE =        
        "call VF100.SELECT_FUND_RATE(?,?,?)";  
	
	 private static final String EMPLOYEE_CONTRIBUTION = "EE";
	 private static final String EMPLOYER_CONTRIBUTION = "ER";
	 private static final String INDICATOR_YES_FLAG = "Y";
	 private static final String INDICATOR_NO_FLAG = "N";
	 private static final String INDICATOR_YES_DISPLAY = "Yes";
	 private static final String INDICATOR_NO_DISPLAY = "No";
	 private static final String LSA_FUND = "LSA";
	 private static final String EEDEF_MONEY_TYPE = "EEDEF";	 
	 private static final int ZEROPAD = 0;
	 
	 
	 private static final String SQL_SELECT_PARTICIPANT_ID = 
	 	"select participant_id from "
	 	+ CUSTOMER_SCHEMA_NAME + "participant_contract where "
		+ "contract_id=? and profile_id=?";
	 
		private static final String SELECT_CONTRACT_FUNDS_SQL
		= "call VF100.SELECT_CONTRACT_FUNDS(?)";
	 
	 /** 
	  * Make sure nobody instantiates this class
	  */
	 private ParticipantAccountDAO()
	 {
	 }	
	 
	 
	 @SuppressWarnings("unchecked")
	public static int getParticipantIdByProfileId(long profileId, int contractNumber) throws SystemException {
	 	BigDecimal prtId = null;
		List parameters = new ArrayList(2);
		parameters.add(0,new Integer(contractNumber));
		parameters.add(1,new BigDecimal(String.valueOf(profileId)));
		SelectSingleOrNoValueQueryHandler handler = new SelectSingleOrNoValueQueryHandler(CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_PARTICIPANT_ID, BigDecimal.class);
	 	try {
			logger.debug("Executing Prepared SQL Statment: "+ SQL_SELECT_PARTICIPANT_ID);
			prtId = (BigDecimal)handler.select(parameters.toArray());
	 	} catch (DAOException e) {
	 		throw new SystemException(e,
	        	"Problem occurred during call: " + SQL_SELECT_PARTICIPANT_ID +". Input parameters are contractNumber:" + 
	        	contractNumber + 
	        	", profileId:" + profileId);
	 	}
	 	if ( prtId == null ) {
	 		return -1;
	 	} else {
	 		return prtId.intValue();
	 	}
	 }
	 
	/**
     * Retrieves a profileId, given a contract id and a participant id.
     * Returns a null string if no profile Id is found.
     * @param participantId participant id
     * @param contractNumber contract id
     * @return String with the profileId 
     * @throws SystemException
     */
    public static String getProfileIdByParticipantId(String participantId, int contractNumber) throws SystemException {

		if (logger.isDebugEnabled() )
			logger.debug("entry -> getByParticipantId");
		
		String profileId = null;	
			
        Connection conn = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        
        try 
        {

        	// setup the connection and the statemnt
            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            statement = conn.prepareCall(SELECT_BY_PARTICIPANT_ID);
 
            if (logger.isDebugEnabled() )
				logger.debug("Calling Stored Procedure: " + SELECT_BY_PARTICIPANT_ID);
            
            // set the input parameters
            statement.setBigDecimal(1, convertStringToBigDecimal(participantId, "participantId", className));
            
            // execute the stored procedure
            statement.execute();
                      
			// ***************  Result set #1   ****************************
            resultSet = statement.getResultSet();
            
            if (resultSet != null) {
                
                while (resultSet.next()) 
                {
                	if (contractNumber == resultSet.getInt("CONTRACT_ID"))
                	{
       	            	profileId = resultSet.getString("PROFILE_ID");
       	            	break;
                	}	
    
                }
                
            }
            
        }
        catch (SQLException e) 
        {
           throw new SystemException(e,
           		"Problem occurred during SELECT_BY_PARTICIPANT_ID stored proc call. Input parameters are contractNumber:" + 
           				contractNumber + 
           				", participantId:" + participantId);
        } 
        finally 
		{
        	close(statement, conn);
        }
        
		
       	if (logger.isDebugEnabled() )
			logger.debug("exit <- getByParticipantId");

        return profileId;

    }	 
	 
	
	/**
	* Retrieves the participant account data from the db2 stored procedure.
	* @param contractNumber contract id
	* @param profileId  profile id
	* @param asOfDate	The as of date for the request query
	* @return ParticipantAccountVO 
	* @throws SystemException
	*/
	@SuppressWarnings("unchecked")
	public static ParticipantAccountVO getParticipantAccountDetails(
			int contractNumber, String productId, String profileId, Date asOfDate, 
			boolean organizeFundsByAssetClass) throws SystemException {
		
		if (logger.isDebugEnabled() )
			logger.debug("entry -> getParticipantAccountDetails");
		// get the unit values first	
		HashMap fundValues = getFundsUnitValue(String.valueOf(contractNumber), asOfDate);		
		HashMap contractRateTypes = getContractRateTypes(String.valueOf(contractNumber));
		List<SvgifFund> svgFunds = FundServiceDelegate.getInstance().getSVGIFDefaultFunds();		
		boolean svgFlag = false;
			
		ParticipantAccountVO participantAccountVO = new ParticipantAccountVO();
		ParticipantAccountDetailsVO participantAccountDetailsVO = new ParticipantAccountDetailsVO();
		
        Connection conn = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        
        // Temporary collection
        Map participantFunds = null;
        String fundId = null;
        ParticipantFundSummaryVO participantFundSummaryVO = null;
        
        try {

        	// setup the connection and the statement
            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            statement = conn.prepareCall(GET_PARTICIPANT_ACCOUNT_DTL);
 
            if (logger.isDebugEnabled() )
				logger.debug("Calling Stored Procedure: "+GET_PARTICIPANT_ACCOUNT_DTL);
            
            
            BigDecimal BigDecimalProfileId =  convertStringToBigDecimal(profileId, "profileId", className);
            // set the input parameters
            statement.setBigDecimal(1, intToBigDecimal(contractNumber));
            statement.setBigDecimal(2, BigDecimalProfileId);
            
            if (asOfDate != null)
            	statement.setDate(3, new java.sql.Date(asOfDate.getTime()));
            else
               	statement.setNull(3, Types.DATE);

            statement.setDate(4, new java.sql.Date(System.currentTimeMillis()));		//CL 110234
            
            // register the output parameters
            //CL 110234 Begin
            statement.registerOutParameter(5, Types.DECIMAL);//version
            statement.registerOutParameter(6, Types.VARCHAR); 
            statement.registerOutParameter(7, Types.VARCHAR);
            statement.registerOutParameter(8, Types.DATE);
            statement.registerOutParameter(9, Types.INTEGER);
            //CL 110234 End
            
            // execute the stored procedure
            statement.execute();
                      
            // get the output parameters
            //CL 110234 Begin
            statement.getBigDecimal(5); // version
            participantAccountDetailsVO.setEmployeeStatus(statement.getString(6));  //employeeStatus
            participantAccountDetailsVO.setEmploymentStatus(statement.getString(7));  //employment Status
            Date effDate = statement.getDate(8);
            if(effDate!=null){
            String DATE_FORMAT = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            participantAccountDetailsVO.setEffectiveDate(sdf.format(effDate));  //Effective Date
            }
            participantAccountDetailsVO.setParticipantInd(statement.getInt(9) == 1 ? true : false); // participantInd flag
            //CL 110234 End

            // populate what we know.
            participantAccountDetailsVO.setProfileId(BigDecimalProfileId);
            
			// ***************  Result set #1   ****************************
			//
			// this result set contains employee personal information
			
            if (logger.isDebugEnabled() )
				logger.debug("Getting first result set in " + GET_PARTICIPANT_ACCOUNT_DTL);

            resultSet = statement.getResultSet();
            
            int recordCount = 0;
            
            if (resultSet != null) {
            
                if (resultSet.next()) 
                {
                	participantAccountDetailsVO.setFirstName(resultSet.getString("FIRST_NAME"));
                	if(participantAccountDetailsVO.getFirstName()!=null) participantAccountDetailsVO.setFirstName(participantAccountDetailsVO.getFirstName().trim());
                	participantAccountDetailsVO.setLastName(resultSet.getString("LAST_NAME"));
                	participantAccountDetailsVO.setMiddleInitial(resultSet.getString("MIDDLE_INITIAL"));
                	if(participantAccountDetailsVO.getLastName()!=null) participantAccountDetailsVO.setLastName(participantAccountDetailsVO.getLastName().trim());
    				participantAccountDetailsVO.setSsn(resultSet.getString("SOCIAL_SECURITY_NO"));
    				participantAccountDetailsVO.setBirthDate(resultSet.getDate("BIRTH_DATE") == null 
    															? null 
    															: new java.util.Date(resultSet.getDate("BIRTH_DATE").getTime()));
    				participantAccountDetailsVO.setAge(resultSet.getInt("AGE"));
    				
    				String temp = resultSet.getString("ADDR_LINE1");
    				if (temp != null) participantAccountDetailsVO.setAddressLine1(temp.trim());
    				temp = resultSet.getString("ADDR_LINE2");
    				if (temp != null) participantAccountDetailsVO.setAddressLine2(temp.trim());
    				participantAccountDetailsVO.setCityName(resultSet.getString("CITY_NAME"));
    				participantAccountDetailsVO.setStateCode(resultSet.getString("STATE_CODE"));
    				participantAccountDetailsVO.setZipCode(resultSet.getString("ZIP_CODE"));
    				
    				
    
                    recordCount++;
                }
                
            } 
            
            // *
            //**************  Result set #2   ****************************
			//*
			// this result set contains contract detail information
			//DEFAULT_INVESTMENT	
			//LAST_INVESTMENT_DT    // last contribution date
			//AUTOMATIC_REBALANCE
			//PERSONAL_BROKERAGE_ACCOUNT_IND
			
            if (logger.isDebugEnabled() )
				logger.debug("Getting 2nd result set in " + GET_PARTICIPANT_ACCOUNT_DTL);

			statement.getMoreResults();
           	resultSet = statement.getResultSet(); //CONTRACT DETAIL -- 2nd result set
           	
           	if (resultSet != null) {
           	    
                while (resultSet.next())
                {
                	participantAccountDetailsVO.setDefaultInvestmentIndicator(indToDisplay(resultSet.getString("DEFAULT_INVESTMENT_IND")));
                	participantAccountDetailsVO.setInvestmentInstructionType(resultSet.getString("INVESTMENT_INSTRUCTION_TYP_CD"));
                   	participantAccountDetailsVO.setLastContributionDate(resultSet.getDate("LAST_INVESTMENT_DT"));
                   	participantAccountDetailsVO.setAutomaticRebalanceIndicator(indToDisplay(resultSet.getString("AUTOMATIC_REBALANCE_IND")));
                   	participantAccountDetailsVO.setPersonalBrokerageAccountIndicator(indToDisplay(resultSet.getString("PERSONAL_BROKERAGE_ACCOUNT_IND")));
                   	//Gateway Phase 1 -- start
                   	//saving participant gateway indicator to VO
                   	participantAccountDetailsVO.setParticipantGatewayInd(resultSet.getString("GATEWAY_OPTION_STATUS_CODE"));
                   	//Gateway Phase 1 -- end
                }
    			
           	}
           	
			// ***************  Result set #3   ****************************
			//
			// this result set contains the information
			// SHOW_LOAN_FEATURE
            if (logger.isDebugEnabled() )
				logger.debug("Getting 3rd result set in " + GET_PARTICIPANT_ACCOUNT_DTL);

			statement.getMoreResults();			
           	resultSet = statement.getResultSet(); //CONTRACT LOAN DTL -- 3rd result set
           	
           	if (resultSet != null) {
           	    
                while (resultSet.next())
                {
                	 	participantAccountDetailsVO.setShowLoanFeature(resultSet.getString("SHOW_LOAN_FEATURE"));
                }
                
           	}
           	
			// ***************  Result set #4   ****************************
			//
			// this result set contains the totalAssets information
			// TOTAL_BALANCE_AMT_SUM
            if (logger.isDebugEnabled() )
				logger.debug("Getting 4th result set in " + GET_PARTICIPANT_ACCOUNT_DTL);

			statement.getMoreResults();			
           	resultSet = statement.getResultSet(); //TOTAL ASSETS -- 4th result set
           	
           	if (resultSet != null) {
           	    
                while (resultSet.next())
                {
                	participantAccountDetailsVO.setAllocatedAssets(resultSet.getDouble("TOTAL_BALANCE_AMT_SUM"));
                }
                
           	}
           	
			// ***************  Result set #5   ****************************
			//
			// this result set contains the loan Assets information
			// OUTSTANDING_PRINCIPAL_AMT_SUM
            if (logger.isDebugEnabled() )
				logger.debug("Getting 5th result set in " + GET_PARTICIPANT_ACCOUNT_DTL);

			statement.getMoreResults();			
           	resultSet = statement.getResultSet(); //LOAN ASSETS -- 5th result set
           	
           	if (resultSet != null) {
           	    
                while (resultSet.next())
                {
                	participantAccountDetailsVO.setLoanAssets(resultSet.getDouble("LOAN_ASSET"));
    
                }
    			
           	}
           	
            // This is to fix Requirement PPR.91 with CL44666 
        	if ( participantAccountDetailsVO.getLoanAssets() > 0)
        		participantAccountDetailsVO.setShowLoanFeature("YES");		            

			// ***************  Result set #6   ****************************
			//
			// this result set contains the total_PBA information
			// TOTAL_PBA_AMT_SUM
            if (logger.isDebugEnabled() )
				logger.debug("Getting 6th result set in " + GET_PARTICIPANT_ACCOUNT_DTL);
			
			statement.getMoreResults();			
           	resultSet = statement.getResultSet(); //TOTAL PBA -- 6th result set
           	
           	if (resultSet != null) {
           	    
                while (resultSet.next())
                {
                	participantAccountDetailsVO.setPersonalBrokerageAccount(resultSet.getDouble("TOTAL_PBA_AMT_SUM"));
                }
    			
           	}
           	
			// calcualte the total assets
			participantAccountDetailsVO.setTotalAssets(
				participantAccountDetailsVO.getAllocatedAssets()+
				participantAccountDetailsVO.getLoanAssets()+
				participantAccountDetailsVO.getPersonalBrokerageAccount()
			);
			
			
			participantAccountVO.setParticipantAccountDetailsVO(participantAccountDetailsVO);
			
			// ***************  Result set #7   ****************************
			//
			// this result set contains the loanDetails drop-down information
			// LOAN_ID
			// OUTSTANDING_PRINCIPAL_AMT
            if (logger.isDebugEnabled() )
				logger.debug("Getting 7th result set in " + GET_PARTICIPANT_ACCOUNT_DTL);
			
			statement.getMoreResults();			
           	resultSet = statement.getResultSet(); //LOAN DETAILS -- 7th result set
           
           	if (resultSet != null) {
           	    
                while (resultSet.next())
                {           	
                	ParticipantLoanDetails partLoanDetails = new ParticipantLoanDetails(resultSet.getString("LOAN_ID"),
                	 																	resultSet.getDouble("OUTSTANDING_LOAN"));
                	participantAccountVO.addParticipantLoanDetails(partLoanDetails);
                }
    			
           	}
           	
			// ***************  Result set #8   ****************************
			//
			// this result set contains the fundSummary information
			//INVESTMENT_OPTION_ID
			//MONEY_TYPE
			//COMPOSITE_RATE
			//UNITS_HELD_SUM
			//TOTAL_BALANCE_AMT_SUM
            if (logger.isDebugEnabled() )
				logger.debug("Getting 8th result set in " + GET_PARTICIPANT_ACCOUNT_DTL);
			
			statement.getMoreResults();									
           	resultSet = statement.getResultSet(); //FUND SUMMARY -- 8th result set
            	
            // we'll create a temporary HashMap of objects
            participantFunds = new HashMap();
            
            if (resultSet != null) {
                
                while (resultSet.next())
                {
                	fundId = resultSet.getString("INVESTMENT_OPTION_ID");
                	// protect against bad data coming back from the stored procedure
                	if(fundId==null||fundId.equalsIgnoreCase("")||fundId.equalsIgnoreCase("0")) continue;
                	
                	// First this we'll try to find the participant value object in the Hashmap.
                	// If it's there, get the object and update it;
                	// if not, create one.
                	// This way we make sure we have only one record for each FundId in the HashMap;
                	// When we're done creating the HashMap, we'll do all the additional calculations
                	// and populate the ParticipantFundSummary which will be used by the presentation layer
                	
                	participantFundSummaryVO = (ParticipantFundSummaryVO)participantFunds.get(fundId);
                	
                	if (participantFundSummaryVO == null)
                	{
                	 	participantFundSummaryVO = new ParticipantFundSummaryVO();  	
                	 	participantFundSummaryVO.setFundId(fundId);
                	 	participantFunds.put(fundId, participantFundSummaryVO);
                	} 	
                	            	
                	String moneyType = resultSet.getString("MONEY_TYPE");
     			
       				if ( EMPLOYEE_CONTRIBUTION.equalsIgnoreCase(moneyType.trim()))  {
       					participantFundSummaryVO.setEmployeeCompositeRate(resultSet.getDouble("COMPOSITE_RATE"));
       					participantFundSummaryVO.setEmployeeNumberOfUnitsHeld(resultSet.getDouble("UNITS_HELD_SUM"));
       					participantFundSummaryVO.setEmployeeBalance(resultSet.getDouble("TOTAL_BALANCE_AMT_SUM"));
       				}  
       				else if ( EMPLOYER_CONTRIBUTION.equalsIgnoreCase(moneyType.trim())) {
       					participantFundSummaryVO.setEmployerCompositeRate(resultSet.getDouble("COMPOSITE_RATE"));
       					participantFundSummaryVO.setEmployerNumberOfUnitsHeld(resultSet.getDouble("UNITS_HELD_SUM"));
       					participantFundSummaryVO.setEmployerBalance(resultSet.getDouble("TOTAL_BALANCE_AMT_SUM"));
       				}
                }
    			
            }
            
			// ***************  Result set #9   ****************************
			// this result set contains the ongoingContribution information
			//INVESTMENT_OPTION_ID
			//ALLOCATION_PCT
            if (logger.isDebugEnabled() )
				logger.debug("Getting 9th result set in " + GET_PARTICIPANT_ACCOUNT_DTL);
			
			statement.getMoreResults();
           	resultSet = statement.getResultSet(); //ONGOING CONTRIBUTION -- 9th result set
           	
           	if (resultSet != null) {
           	    
                while (resultSet.next())
                {
                	fundId = resultSet.getString("INVESTMENT_OPTION_ID");
                	// protect against bad data coming back from the stored procedure
                	if(fundId==null||fundId.equalsIgnoreCase("")||fundId.equalsIgnoreCase("0")) continue;
    
       	         	participantFundSummaryVO = (ParticipantFundSummaryVO)participantFunds.get(fundId);
                	
                	if (participantFundSummaryVO == null)
                	{
                	 	participantFundSummaryVO = new ParticipantFundSummaryVO(); 
    	           	 	participantFundSummaryVO.setFundId(fundId);
                	 	participantFunds.put(fundId, participantFundSummaryVO);
                	} 	
                	
                	String moneyType = resultSet.getString("MONEY_TYPE");
                	
       				if ( EMPLOYEE_CONTRIBUTION.equalsIgnoreCase(moneyType))  {
       					participantFundSummaryVO.setEmployeeOngoingContributions(resultSet.getDouble("ALLOCATION_PCT")/100.00);
       				}  
       				else if ( EMPLOYER_CONTRIBUTION.equalsIgnoreCase(moneyType)) {
       					participantFundSummaryVO.setEmployerOngoingContributions(resultSet.getDouble("ALLOCATION_PCT")/100.00);
       				}            	 	
       				
                	participantFundSummaryVO.setFundTotalOngoingContributions(resultSet.getDouble("ALLOCATION_PCT")/100.00);
                }
                
           	}
           	
            //INVESTMENT_OPTION_ID 
			//MONEY_TYPE_ID 
			//CONTRACT_MONEY_TYPE_LONG_NAME  
            //MONEY_TYPE_CATEGORY_CODE
			//COMPOSITE_RATE        
			//UNITS_HELD_QTY      
			//TOTAL_BALANCE_AMT
            if (logger.isDebugEnabled() )
				logger.debug("Getting 10th result set in " + GET_PARTICIPANT_ACCOUNT_DTL);
			
            
			if ( statement.getMoreResults() ) {
	           	resultSet = statement.getResultSet(); //MONEY_TYPE_DETAILS -- 10th result set            
	
	           	double eeDefBalance = 0.0;
	           	
	           	if (resultSet != null) {
	           	    
    	            while (resultSet.next())
    	            {
    	            	fundId = resultSet.getString("INVESTMENT_OPTION_ID");
    	            	String investmentId = resultSet.getString("INVESTMENT_OPTION_ID").trim();
    	            	// protect against bad data coming back from the stored procedure
    	            	if(fundId==null||fundId.equalsIgnoreCase("")||fundId.equalsIgnoreCase("0")) continue;
    	            	svgFlag = svgFunds.stream().anyMatch(o -> o.getFundId().equals(investmentId));
    	   	            ParticipantFundMoneyTypeDetailVO moneyTypeDetailVO = new ParticipantFundMoneyTypeDetailVO();
    	   	            moneyTypeDetailVO.setMoneyTypeName(resultSet.getString("CONTRACT_MONEY_TYPE_LONG_NAME").trim());
    	   	            moneyTypeDetailVO.setMoneyType(resultSet.getString("MONEY_TYPE_CATEGORY_CODE").trim());
    	   	            moneyTypeDetailVO.setCompositeRate(resultSet.getDouble("COMPOSITE_RATE"));
    	   	            
    	   	            // Suppressing number of units values for SVGIF 
    	   	            if(!svgFlag) {
    	   	            	moneyTypeDetailVO.setNumberOfUnitsHeld(resultSet.getDouble("UNITS_HELD_QTY"));
    	   	            }
    	   	            moneyTypeDetailVO.setBalance(resultSet.getDouble("TOTAL_BALANCE_AMT"));    
    	   	            String moteyTypeId = resultSet.getString("MONEY_TYPE_ID").trim();
    	            	
    	            	if ( !fundId.startsWith(LSA_FUND) ) {
    		   	         	participantFundSummaryVO = (ParticipantFundSummaryVO)participantFunds.get(fundId);
    		   	            participantFundSummaryVO.addFundMoneyTypeDetail(moneyTypeDetailVO);
    	
    		   	            if ( moneyTypeDetailVO.getMoneyType().equals(EMPLOYEE_CONTRIBUTION) ) {
    		   	            	participantAccountVO.addEmployeeMoneyTypeAsset(moneyTypeDetailVO.getMoneyTypeName(), moneyTypeDetailVO.getBalance());
    							// Ths following code added in order to PPR.424
    							// We need to display the lesser amount of the CSDB EEDEF value 
    							// or Apollo STP result		   	            	
    		   	            	if ( EEDEF_MONEY_TYPE.equals(moteyTypeId) )
    		   	            		eeDefBalance += moneyTypeDetailVO.getBalance();
    		   	            }
    		   	            else
    		   	            	participantAccountVO.addEmployerMoneyTypeAsset(moneyTypeDetailVO.getMoneyTypeName(), moneyTypeDetailVO.getBalance());
    	            	} 
    	            	else { // If this is a Loan Asset
    	            		
    		   	            if ( moneyTypeDetailVO.getMoneyType().equals(EMPLOYEE_CONTRIBUTION) )
    		   	            	participantAccountVO.addEmployeeMoneyTypeLoan(moneyTypeDetailVO.getMoneyTypeName(), moneyTypeDetailVO.getBalance());
    		   	            else
    		   	            	participantAccountVO.addEmployerMoneyTypeLoan(moneyTypeDetailVO.getMoneyTypeName(), moneyTypeDetailVO.getBalance());
    	            	}
    	            		
    	            }
    	            
	           	}
	           	
	            participantAccountVO.getParticipantAccountDetailsVO().setNetEEDeferralContributions(eeDefBalance);
			}
           
			//start  result 11
           			// ***************  Result set #11   ****************************
			//
			// this result set contains the after tax money type information
			// CUSTOM_LONG_NAME
			// NET_CONTRIBUTION_AMT
			// EARNINGS_AMT
            if (logger.isDebugEnabled() )
				logger.debug("Getting 11th result set in " + GET_PARTICIPANT_ACCOUNT_DTL);

			if ( statement.getMoreResults() ) {
	           	resultSet = statement.getResultSet(); //AFTER TAX MONEY -- 11th result set            
	           	
	           	if (resultSet != null) {
	           		ParticipantNetContribEarningsVO partNetContribEarningsDetails = null;
	           		int count = 0;
	           		while (resultSet.next())
	           		{    
	           			String rothMoneyTypeInd = resultSet.getString("ROTH_MONEY_TYPE_IND").trim();
	           			partNetContribEarningsDetails = new ParticipantNetContribEarningsVO();
	           			if("Y".equals(rothMoneyTypeInd)){
	           				count = count+1;
	           				partNetContribEarningsDetails.setMoneyTypeName(resultSet.getString("CONTRACT_MONEY_TYPE_LONG_NAME").trim());
	           				partNetContribEarningsDetails.setNetContributions(resultSet.getDouble("NET_CONTRIBUTION_AMT"));
	           				partNetContribEarningsDetails.setEarnings(resultSet.getDouble("EARNINGS_AMT"));
	           				participantAccountVO.setRothMoneyTypeInd(true);
	           				participantAccountVO.setRothMoneyTypeCount(count);
	           				partNetContribEarningsDetails.setRothMoneyTypeInd(true);
	           			}
	           			else
	           			{
	           				partNetContribEarningsDetails.setMoneyTypeName(resultSet.getString("CONTRACT_MONEY_TYPE_LONG_NAME").trim());
	           				partNetContribEarningsDetails.setNetContributions(resultSet.getDouble("NET_CONTRIBUTION_AMT"));
	           				partNetContribEarningsDetails.setEarnings(resultSet.getDouble("EARNINGS_AMT"));
	           				participantAccountVO.setNonRothMoneyTypeInd(true);	
	           				partNetContribEarningsDetails.setNonRothMoneyTypeInd(true);
	           			}

	           			participantAccountVO.addNetContribEarningsDetails(partNetContribEarningsDetails);
	           		}

	           	}
	           	
			}
            //start  result 12
   			// ***************  Result set #12   ****************************
			//
			// this result set contains the rothFirstDepositYear 
			// ROTH_FIRST_DEPOSIT_YEAR
			if (logger.isDebugEnabled() )
			    logger.debug("Getting 12th result set in " + GET_PARTICIPANT_ACCOUNT_DTL);

			if ( statement.getMoreResults() ) {
			    resultSet = statement.getResultSet(); //AFTER TAX MONEY -- 11th result set            
        
			    if (resultSet != null) {
    
    			    while (resultSet.next())
    			    {           	
    			        participantAccountVO.getParticipantAccountDetailsVO().setRothFirstDepositYear(resultSet.getInt("ROTH_FIRST_DEPOSIT_YEAR"));
    			    }
    			    
			    }
			    
			}	
           
           
             
        } catch (SQLException e) {
           throw new SystemException(e,
           		"Problem occurred during GET_PARTICIPANT_ACCOUNT_DTL stored proc call. Input parameters are contractNumber:" + 
           				contractNumber + 
           				", profileId:" + profileId +
           				", asOfDate:" + asOfDate);
        } finally {
        	close(statement, conn);
        }
        
        /* we're done collecting all the information; let's massage all this data,
          calculate the totals, populate the summaries and get outta here */
         
		// temporary array to group the ParticipantFundSummaryVO objects by risk category,
		// and within a risk category sorted by fund sortNumber
		//	Gifl Risk category is also considered
        TreeSet[] treeSets = new TreeSet[FundVO.getRiskCategoryCodesWithGifl().length];
        
        //It's for pie chart, since pie chart should remain unchanged due to Gifl P3 
        TreeSet[] riskCategoryWithoutGifl = new TreeSet[FundVO.getRiskCategoryCodes().length];

		List<AssetClassVO> assetClasses = new ArrayList(FundInfoCache
				.getAllAssetClasses());
        
        int size = assetClasses.size() + 1;
        
        // add pba to the asset classes
        assetClasses
                .add(new AssetClassVO(Constants.PBA_CATEGORY_CODE, size,
                Constants.PBA_DESC));
        
        TreeSet[] treeSetsByOrganizeOption = new TreeSet[size + 1];
		
		if (organizeFundsByAssetClass){
			for (int i = 0; i < treeSetsByOrganizeOption.length; i++) {
				treeSetsByOrganizeOption[i] = new TreeSet();	
			}
		}
		
		for (int i = 0; i < treeSets.length; i++) treeSets[i] = new TreeSet();
		for (int i = 0; i < riskCategoryWithoutGifl.length; i++){
		 	riskCategoryWithoutGifl[i] = new TreeSet();
		} 
		
		FundVO fundVO = null;
		
		Iterator iter = participantFunds.values().iterator();

		double totalPBAandAllocatedAssets = participantAccountDetailsVO.getPersonalBrokerageAccount() +
											participantAccountDetailsVO.getAllocatedAssets();
											
		while (iter.hasNext()) {
			participantFundSummaryVO = (ParticipantFundSummaryVO)iter.next();
			fundVO = FundInfoCache.getFundData(participantFundSummaryVO.getFundId());
			
			participantFundSummaryVO.setFundName(fundVO.getName());
			participantFundSummaryVO.setFundType(fundVO.getType());
			participantFundSummaryVO.setSortNumber(fundVO.getSortNumber());
			String rateType = (String)contractRateTypes.get(participantFundSummaryVO.getFundId().trim());
			String fundid = participantFundSummaryVO.getFundId().trim();
			ContractFundDetails contractFundDetails = (ContractFundDetails)fundValues.get(participantFundSummaryVO.getFundId().trim());
			svgFlag = svgFunds.stream().anyMatch(o -> o.getFundId().equals(fundid));

			participantFundSummaryVO.setRateType(rateType);
			participantFundSummaryVO.setSvgifFlg(svgFlag);
			
				if (contractFundDetails != null) {							
				// Suppressing unit values for SVGIF
				if(!svgFlag)
					participantFundSummaryVO.setFundUnitValue(contractFundDetails.getUnitValue().doubleValue());				
				
            	// FIX: the number of units for both the employee and employer amounts cannot be retrieved correctly from the database
				//      for historical values.  Hence, a calculation is required to determine the correct balances			
				Calendar today = Calendar.getInstance();
				today.setTime(EnvironmentServiceHelper.getInstance().getAsOfDate());
				//only calculate if it's historical
				if (asOfDate != null && !asOfDate.equals(today.getTime()) && participantFundSummaryVO.getFundUnitValue() > 0) {
					participantFundSummaryVO.setEmployeeNumberOfUnitsHeld(participantFundSummaryVO.getEmployeeBalance() / participantFundSummaryVO.getFundUnitValue());
					participantFundSummaryVO.setEmployerNumberOfUnitsHeld(participantFundSummaryVO.getEmployerBalance() / participantFundSummaryVO.getFundUnitValue());
				}
			}
																	
			
         	// calculate the totals
				
				if(!svgFlag){
					participantFundSummaryVO.setFundTotalNumberOfUnitsHeld(participantFundSummaryVO.getEmployeeNumberOfUnitsHeld()+participantFundSummaryVO.getEmployerNumberOfUnitsHeld());
				}
				

			participantFundSummaryVO.setFundTotalBalance(participantFundSummaryVO.getEmployeeBalance()+participantFundSummaryVO.getEmployerBalance());         	
         	if ( participantFundSummaryVO.getFundId().trim().equals("3YC") ||
         			participantFundSummaryVO.getFundId().trim().equals("5YC") ||
         			participantFundSummaryVO.getFundId().trim().equals("10YC") )
         	{
         		// commented out and replaced with code below to fix CommonLog: 44360
	         	//participantFundSummaryVO.setFundTotalCompositeRate(participantFundSummaryVO.getEmployeeCompositeRate());
         		participantFundSummaryVO.setFundTotalCompositeRate(participantFundSummaryVO.getEmployeeCompositeRate() >0.0 ?  
         				participantFundSummaryVO.getEmployeeCompositeRate() : participantFundSummaryVO.getEmployerCompositeRate());
	         	participantFundSummaryVO.setEmployerCompositeRate(0.0);
	         	participantFundSummaryVO.setEmployeeCompositeRate(0.0);	         	
         	}
         	else
	         	participantFundSummaryVO.setFundTotalCompositeRate(participantFundSummaryVO.getEmployeeCompositeRate()+participantFundSummaryVO.getEmployerCompositeRate());
         		         	
         	if(participantAccountVO.getParticipantAccountDetailsVO().getTotalAssets()!=0) 
	         	participantFundSummaryVO.setFundTotalPercentageOfTotal(participantFundSummaryVO.getFundTotalBalance()/totalPBAandAllocatedAssets);
	         	
        	//RiskCategoryWithLS grouped by order No so it only has GW value
         	int index = -1;
         	if(FundVO.RISK_GIFL.equals(fundVO.getRiskCategoryCodeWithLS())) {
         		index = FundVO.getRiskCategoryWithGiflOrder(fundVO.getRiskCategoryCodeWithLS());
         	} else {
         		index = FundVO.getRiskCategoryWithGiflOrder(fundVO.getRiskCategoryCodeWithGifl());
         	}
         	

			if (index != -1) treeSets[index].add(participantFundSummaryVO);
			
			// Its for Pie Chart
			index = FundVO.getRiskCategoryOrder(fundVO.getRiskCategoryCode());
			
			if(index != -1){
				riskCategoryWithoutGifl[index].add(participantFundSummaryVO);
			}
		
			
			if (organizeFundsByAssetClass){
			    
				index = getAssetClass(assetClasses, fundVO.getAssetClass())
                        .getSortOrder();
				
				 if ("NPB".equals(fundVO.getId().trim()) || "PBA".equals(fundVO.getId().trim())) {
                    index = size;
                }
				
				if (index != -1) {
					treeSetsByOrganizeOption[index].add(participantFundSummaryVO);
				}
			}
		}	
		
		ParticipantAccountAssetsByRiskVO assetsByRisk = new ParticipantAccountAssetsByRiskVO();
		participantAccountVO.setAssetsByRisk(assetsByRisk);

		BeanComparator comp = new BeanComparator("sortOrder");
		Collections.sort(assetClasses, comp);
		
		// Convert to arrays
		// This is left unchanged for the PSW application and the Pie Chart
		// Gifl Risk category also included to group the funds
		participantAccountVO.setParticipantFundsByRisk(new InvestmentOptionVO[FundVO.getRiskCategoryCodesWithGifl().length]);
		for (int i = 0; i < FundVO.getRiskCategoryCodesWithGifl().length; i++) {
			InvestmentOptionVO vo = new InvestmentOptionVO();
			vo.setCategory(new FundCategory(FundVO.getRiskCategoryCodesWithGifl()[i]));
			vo.setParticipantFundSummaryArray((ParticipantFundSummaryVO[])treeSets[i].toArray(new ParticipantFundSummaryVO[0]));
			participantAccountVO.getParticipantFundsByRisk()[i] = vo;
		}
		
		// Pie Chart risk category should remain unchanged due to GIFL P3. Split the logic to find asset total
		for (int i = 0; i < FundVO.getRiskCategoryCodes().length; i++) {
			ParticipantFundSummaryVO [] participantFundSummary = (ParticipantFundSummaryVO[])riskCategoryWithoutGifl[i].toArray(new ParticipantFundSummaryVO[0]);
			
			// consolidate the assets by risk category without GIFL risk category
			double total = 0;
			for (int j = 0; j < participantFundSummary.length; j++) 
				total += participantFundSummary[j].getFundTotalBalance();
			assetsByRisk.setAssetTotal(FundVO.getRiskCategoryCodes()[i], total);
		}
	
		// If the "Funds Organized By" option is by Asset class
		if (organizeFundsByAssetClass) {
			participantAccountVO.setOrganizedParticipantFunds(new InvestmentOptionVO[assetClasses.size()]);
			int arrayIndex = 0;
			for (AssetClassVO assetClassVO : assetClasses) {
				InvestmentOptionVO vo = new InvestmentOptionVO();
				vo.setCategory(new FundCategory(assetClassVO.getAssetClass(), assetClassVO.getAssetClassDesc(),assetClassVO.getSortOrder()));
				vo.setParticipantFundSummaryArray((ParticipantFundSummaryVO[])treeSetsByOrganizeOption[assetClassVO.getSortOrder()].toArray(new ParticipantFundSummaryVO[0]));
				
				participantAccountVO.getOrganizedParticipantFunds()[arrayIndex] = vo;
				arrayIndex++;
			}
		} else {
			participantAccountVO.setOrganizedParticipantFunds(participantAccountVO.getParticipantFundsByRisk());
		}
		
		if (logger.isDebugEnabled() )
			logger.debug("exit <- getParticipantAccountDetails");

        return participantAccountVO;
    }

	
	@SuppressWarnings("unchecked")
	public static HashMap getFundsUnitValue(String contractNumber, Date asOfDate)
		throws SystemException {

		if (logger.isDebugEnabled() )
			logger.debug("entry -> getFundsUnitValue");

        Connection conn = null;
        CallableStatement statement = null;
        ResultSet rs = null;

		HashMap funds = new HashMap();
		try {
			// setup the connection and the statement
            conn = getReadUncommittedConnection(className, BaseDatabaseDAO.VIEW_FUNDS_DATA_SOURCE_NAME);
            statement = conn.prepareCall(SELECT_FUND_RATE);
	           
            statement.setString(1, contractNumber.trim());

            if (asOfDate != null)
            	statement.setDate(2, new java.sql.Date(asOfDate.getTime()));
            else
               	statement.setNull(2, Types.DATE);            	

            // register output parameters
            statement.registerOutParameter(3, Types.VARCHAR); //version               	

            
            if (logger.isDebugEnabled() )
				logger.debug("Calling Stored Procedure: "+ "SELECT_FUND_RATE");

            statement.execute();

			rs = statement.getResultSet(); 					
			String fundId =null;
			ContractFundDetails fundDetails =null;
			
			if (rs != null) {
			    
    			while (rs.next())
    			{
    				fundId =rs.getString("FUND_INVESTMENTID");
    				fundDetails = new ContractFundDetails(fundId.trim(),rs.getString("RATETYPE"), new Double(rs.getDouble("UNITVALUE")));
    				funds.put(fundId.trim(), fundDetails);
    			}
    			
			}
			
	    } catch (SQLException e) {
           throw new SystemException(e, "Problem occurred during SELECT_FUND_RATE stored proc call.");
        } finally {
        	close(statement, conn);
        }		
		
		if (logger.isDebugEnabled() )
			logger.debug("exit <- getFundsUnitValue");
			
		return funds;	
	}

	/**
	* Retrieves the participant net emploee controbutions (EE type) from the Apollo db2 stored procedure.
	* @param contractNumber contract id
	* @param ssn  ssn
	* @param asOfDate	The as of date for the request query
	* @return netEmployeeContibution
	* @throws SystemException
	*/
	public static double getParticipantNetEEDeferralContributions(int contractNumber, String ssn, Date asOfDate)
        throws SystemException	{

		if (logger.isDebugEnabled() )
			logger.debug("entry -> getParticipantNetEEDeferralContributions");
			
        Connection conn = null;
        CallableStatement statement = null;
        double netEEConributions = -1;
        
        try {

        	// setup the connection and the statement
            conn = getReadUncommittedConnection(className, APOLLO_DATA_SOURCE_NAME);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);            
            statement = conn.prepareCall(GET_PARTICIPANT_NET_EE_DEFERRAL_CONTRIBUTIONS);
            
 
            if (logger.isDebugEnabled() )
				logger.debug("Calling Stored Procedure: "+GET_PARTICIPANT_NET_EE_DEFERRAL_CONTRIBUTIONS);
            
            // pad zero with contract number to support 5 and 6 digits             
            String padContractNumber = Integer.toString(contractNumber);
            while(padContractNumber.length() < GlobalConstants.CONTRACT_NUMBER_MAX_LENGTH){
            	padContractNumber = ZEROPAD+padContractNumber;
            }
            // set the input parameters
            
            // Updated the call signature in stored-procedure in getParticipantNetEEDeferralContributions() 
            // to match the Apollo signature. It seems after the upgrade DB2 universal driver there is no 
            // default conversion and parms must be explicitly            
            statement.setString(1, padContractNumber); // statement.setInt(1, contractNumber);
            statement.setString(2, ssn);
            
            if (asOfDate != null)
            	statement.setString(3, new java.sql.Date(asOfDate.getTime()).toString()); // statement.setDate(3, new java.sql.Date(asOfDate.getTime()));
            else
               	statement.setString(3, " ");
            	//statement.setNull(3, Types.VARCHAR);
            
            // register the output parameters
            statement.registerOutParameter(4, Types.CHAR);// return Code
            statement.registerOutParameter(5, Types.CHAR);// Msg Number  
            statement.registerOutParameter(6, Types.CHAR);// Net EE Deferral conribution
            
            // execute the stored procedure
            statement.execute();
                      
            // get the output parameters            
            if ("OK".equalsIgnoreCase(statement.getString(4)) )
            	netEEConributions = (new Double(statement.getString(6))).doubleValue(); 
            else
            	throw new SQLException(statement.getString(4));
            // The following is the possible error codes from Apollo.	
            //	Contract number not numeric		+0001
			//	Contact does not exist on Apollo		+0003
			//	Participant SSN not numeric		+0002
			//	Participant does not exist under contract		+0004
			//	Request date not a business date or not a processed date in the past on Apollo.		+0005
			//	Request date cannot be found on Apollo calendar table 		+0006
			//	Request date not in the right date format of yyyy-mm-dd		+0007
			//	Unexpected DB2 SQLCODE		Sqlcode
			//	Apollo batch is running		9999
            	
        } catch (SQLException e) {
           throw new SystemException(e,
           		"Problem occurred during GET_PARTICIPANT_NET_EE_DEFERRAL_CONTRIBUTIONS stored proc call. Input parameters are contractNumber:" + 
           				contractNumber + 
           				", ssn:" + ssn +
           				", asOfDate:" + asOfDate);
        } finally {
        	close(statement, conn);
        }

        if (logger.isDebugEnabled() )
			logger.debug("exit <- getParticipantNetEEDeferralContributions");

        return netEEConributions;
    }
	
	private final static String indToDisplay(String indicator) {
		if (indicator==null) return indicator;
		else if(indicator.equalsIgnoreCase(INDICATOR_NO_FLAG)) return INDICATOR_NO_DISPLAY;
		else if(indicator.equalsIgnoreCase(INDICATOR_YES_FLAG)) return INDICATOR_YES_DISPLAY;
		else return indicator;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap getContractRateTypes(String contractNumber) throws SystemException {
		
		HashMap contractRateTypes =new HashMap();
		try {

				StoredProcedureHandler handler = new StoredProcedureHandler(
				VIEW_FUNDS_DATA_SOURCE_NAME,
				SELECT_CONTRACT_FUNDS_SQL,
				new StoredProcedureHandler.OutputDefinition[] {
					new StoredProcedureHandler.BeanOutputDefinition(
						ContractFund.class,
						new String[] {
							"contractNumber",
							"fundid",
							"packageflag",
							"selectedflag",
							"ratetype"},
						StoredProcedureHandler.MANY)
				});

			Object[] results = handler.execute(new Object[] { contractNumber });
          ContractFund [] contractFunds =(ContractFund[]) results[0];
          
            for (int i =0; i<contractFunds.length; i++)
            {
            	contractRateTypes.put( contractFunds [i].getFundId().trim(), contractFunds [i].getRatetype().trim());
            }
			
		} catch (DAOException e ) {
	           throw new SystemException(e, "Problem occurred during SELECT_CONTRACT_FUNDS_SQL stored proc call.");
	        }		
			
			if (logger.isDebugEnabled() )
				logger.debug("exit <- getContractRateTypes");
			return contractRateTypes;		
	}
	
	
	public static ParticipantDeferralVO getParticipantNetEEDeferralData(int contractNumber, String profileId) throws SystemException {
		Connection conn = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        	        
        try {
            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            statement = conn.prepareCall(GET_PARTICIPANT_DEFERRAL_DTL);
            
            statement.setInt(1, contractNumber);
            statement.setString(2, profileId);
            
            statement.execute();
            resultSet = statement.getResultSet();
            
            ParticipantDeferralVO deferralVO = new ParticipantDeferralVO();
            
            if (resultSet != null) {
                
                if (resultSet.next()) { // (main)found record
                   deferralVO.setAciAnniversaryDate(resultSet.getDate("ACI_ANNIVERSARY_DATE"));
                   deferralVO.setBeforeTaxDeferralAmount(readDouble(resultSet, "BEFORE_TAX_DEFER_AMT"));
                   deferralVO.setBeforeTaxDeferralPercent(readDouble(resultSet, "BEFORE_TAX_DEFER_PCT"));
                   deferralVO.setRothAmount(readDouble(resultSet, "DESIG_ROTH_DEF_AMT"));
                   deferralVO.setRothPercent(readDouble(resultSet, "DESIG_ROTH_DEF_PCT"));
                   deferralVO.setOutstandingRequests(resultSet.getInt("OUTSTANDING"));
                   deferralVO.setMaxLimitAmount(readDouble(resultSet, "DEFER_MAX_LIMIT_AMT"));
                   deferralVO.setMaxLimitPercent(readDouble(resultSet, "DEFER_MAX_LIMIT_PCT"));
                   deferralVO.setIncreaseAmount(readDouble(resultSet, "DEFER_INC_AMT"));
                   deferralVO.setIncreasePercent(readDouble(resultSet, "DEFER_INC_PCT"));
                   deferralVO.setParticipantACISetting(resultSet.getString("ACI_SETTING_IND"));
                }
                
            }
            
            if (statement.getMoreResults()) { // most recent, adHoc, before tax.
            	resultSet = statement.getResultSet();
            	
            	if (resultSet != null) {
            	    
                	if (resultSet.next()) {	            
    	            	if ("PA".equalsIgnoreCase(resultSet.getString("PROCESSED_STATUS_CODE"))) { // outstanding
    	            	   deferralVO.setPendingBeforeTaxAmount(readDouble(resultSet, "CONTRIBUTION_AMT"));
    	            	   deferralVO.setPendingBeforeTaxPercent(readDouble(resultSet, "CONTRIBUTION_PCT"));
    	            	}
                	}
                	
            	}
            	
            }
            
            if (statement.getMoreResults()) { // most recent, ACI request.
            	resultSet = statement.getResultSet();
            	
            	if (resultSet != null) {
            	    
                	if (resultSet.next()) {
                		deferralVO.setMostRecentACIProcessedDate(resultSet.getDate("PROCESSED_TS")); // PPC20.2
                		deferralVO.setMostRecentACIProcessedStatusCode(resultSet.getString("PROCESSED_STATUS_CODE")); // PPC20.1
                		deferralVO.setMostRecentACIAnniversaryDate(resultSet.getDate("ACI_ANNIVERSARY_DATE")); // PPC20.1
                	}
                	
            	}
            	
            }
                        
            return deferralVO;
        } catch (SQLException e) {
           throw new SystemException(e,
           		"Problem occurred during GET_PARTICIPANT_DEFERRAL_DTL stored proc call. Input parameters are contractNumber:" + 
           				contractNumber + ", profileId:" + profileId);
        } finally {
        	close(statement, conn);
        }		
	}
	
	private static Double readDouble(ResultSet rs, String field) throws SQLException {
		BigDecimal value = rs.getBigDecimal(field);
		if (value == null) return null;
		else return value.doubleValue();
	}

   private static AssetClassVO getAssetClass(List<AssetClassVO> assetClasses, String assetClass) {
    	
    	for (AssetClassVO assetClassVO : assetClasses) {
    		if (assetClass.equalsIgnoreCase(assetClassVO.getAssetClass())) {
    			return assetClassVO;
    		}
    	}
    	// this will never happen
    	return new AssetClassVO("", 0, "");
    }	
}
