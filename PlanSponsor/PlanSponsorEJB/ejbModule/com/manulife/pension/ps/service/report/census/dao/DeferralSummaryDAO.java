/*
 * Created on June 26, 2007
 *
 * This is the DAO for Deferral page
 */
package com.manulife.pension.ps.service.report.census.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.util.PlanDataHelper;
import com.manulife.pension.ps.service.report.census.valueobject.DeferralDetails;
import com.manulife.pension.ps.service.report.census.valueobject.DeferralReportData;
import com.manulife.pension.ps.service.report.census.valueobject.DeferralStatisticsSummary;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

/**
 * DAO used by the Deferral page
 * 
 * @author patuadr
 *
 */
public class DeferralSummaryDAO extends BaseDatabaseDAO {

    private static final String className = DeferralSummaryDAO.class.getName();

    private static final Logger logger = Logger.getLogger(DeferralSummaryDAO.class);
    
    private static final String GET_DEFERRAL_SUMMARY =
        "{call " + CUSTOMER_SCHEMA_NAME + "GET_DEFERRAL_SUMMARY(?,?,?,?,?,?,?,?,?,?)}";
    private static final String GET_STATISTICS_SUMMARY =
        "{call " + CUSTOMER_SCHEMA_NAME + "GET_DEFERRAL_STATS(?)}";
    
    private static final Map<String, String> fieldToColumnMap = new HashMap<String, String>();
    private static final String AND = " and ";
    private static final String ALL = "All"; 
    private static final String BEFORE_TAX_DEFER_PCT = "BEFORE_TAX_DEFER_PCT";
    private static final String BEFORE_TAX_DEFER_AMT = "BEFORE_TAX_DEFER_AMT";
    private static final String DESIG_ROTH_DEF_PCT = "DESIG_ROTH_DEF_PCT";
    private static final String DESIG_ROTH_DEF_AMT = "DESIG_ROTH_DEF_AMT";
    private static final String OPTED_OUT_PARTICIPANT_STATUS_CODE = "NT";
    
    private static final DecimalFormat PERCENTAGE_FORMATTER = new DecimalFormat("##0");
	private static final DecimalFormat DOLLAR_FORMATTER = new DecimalFormat("##,###.##");
	private static FastDateFormat DATE_FORMATTER = FastDateFormat.getInstance("MM/dd/yyyy HH:mm:ss");
        
    /**
     * Make sure nobody instanciates this class.
     */ 
    private DeferralSummaryDAO() {
    }

    static {
        fieldToColumnMap.put(DeferralReportData.LAST_NAME_FIELD, DeferralReportData.LAST_NAME_COLUMN);
        fieldToColumnMap.put(DeferralReportData.EMPLOYER_DIVISION_FIELD, DeferralReportData.EMPLOYER_DIVISION_COLUMN);
        fieldToColumnMap.put(DeferralReportData.EMPLOYEE_ID_FIELD, DeferralReportData.EMPLOYEE_ID_COLUMN);
        fieldToColumnMap.put(DeferralReportData.BEFORE_TAX_DEFERRAL_PCT_FIELD, DeferralReportData.BEFORE_TAX_DEFER_PCT_COLUMN);
        fieldToColumnMap.put(DeferralReportData.ACI_SETTING_IND_FIELD, DeferralReportData.ACI_SETTINGS_IND_COLUMN);
        fieldToColumnMap.put(DeferralReportData.ACI_ANNIVERSARY_DATE_FIELD, DeferralReportData.ACI_ANNIVERSARY_DATE_COLUMN);     
        fieldToColumnMap.put(DeferralReportData.WARNING_FIELD, DeferralReportData.WARNING_COLUMN);
        fieldToColumnMap.put(DeferralReportData.ALERT_FIELD, DeferralReportData.ALERT_COLUMN);
    }

    /**
     * Get report data.
     * 
     * @param criteria ReportCriteria
     * @return ReportData
     * 
     * @throws SystemException
     */ 
    public static ReportData getReportData(final ReportCriteria criteria)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getReportData");
            logger.debug("Report criteria -> " + criteria.toString());
        }

        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet resultSet = null;
        DeferralReportData psReportDataVO = null;
        int contractNumber = 0;
            
        try {   
            // setup the connection and the statement
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_DEFERRAL_SUMMARY);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Calling Stored Procedure: " + GET_DEFERRAL_SUMMARY);
            }
            
            boolean autoEnrollOn = ((Boolean)criteria.getFilterValue(
                    DeferralReportData.FILTER_AUTO_ENROLL_ON)).booleanValue();
            
            // get contract number from criteria
            contractNumber = (new Integer((String) criteria.getFilterValue(
                    DeferralReportData.FILTER_CONTRACT_NUMBER))).intValue();
            stmt.setInt(1, contractNumber);
            
            // get filter from criteria
            String filterPhrase = createFilterPhrase(criteria);
            if (logger.isDebugEnabled()) {
                logger.debug("Filter phrase: " + filterPhrase);
            }
            if (filterPhrase == null) {
                stmt.setNull(2, Types.VARCHAR);
            } else {
                stmt.setString(2, filterPhrase);
            }
           
            // build sorting
            String sortPhrase = createSortPhrase(criteria);
            if (logger.isDebugEnabled()) {
                logger.debug("Sort phrase: " + sortPhrase);
            }
           
            stmt.setString(3, sortPhrase);
            
            stmt.setInt(4, criteria.getPageSize());
            stmt.setInt(5, criteria.getStartIndex());
           
            // It makes sense to use the frequency just for AE enabled contracts
            if(autoEnrollOn) {
                stmt.setString(6, String.valueOf(PlanDataHelper.getPlanEntryFrequencyForEEDEF(contractNumber)));
            } else {            
                stmt.setNull(6, Types.VARCHAR);
            }
            stmt.setString(7, (criteria.isAdHocFreezePeriod()) ? "Y" : "N");
			
            // register the output parameters:  out_sessionId1
            stmt.registerOutParameter(8, Types.INTEGER);
//          register the output parameters:  out_employeeCount
            stmt.registerOutParameter(9, Types.INTEGER);
            // register the output parameters:  out_totalEmployeeCount
            stmt.registerOutParameter(10, Types.INTEGER);
            
            // execute the stored procedure
            stmt.execute();
            resultSet = stmt.getResultSet();
                        
            // set the attributes in the value object
            psReportDataVO = new DeferralReportData(criteria, stmt.getInt(9));
            psReportDataVO.setTotalNumberOfEmployees(stmt.getInt(10));
            
            List<DeferralDetails> deferralDetails = new ArrayList<DeferralDetails>();
            
            if (resultSet != null) {
                while (resultSet.next()) {                    
                    deferralDetails.add(fillInDataFromResultSet(resultSet, autoEnrollOn,contractNumber));            
                }
            }
            
            psReportDataVO.setDetails(deferralDetails);     
            cleanStaging(stmt.getInt(8), conn);
        } catch (SQLException e) {
           throw new SystemException(e, className, "getReportData", 
           "Problem occurred during GET_DEFERRAL_SUMMARY stored proc call. Report criteria:"
           + criteria);
        } catch (ApplicationException e) {
            throw new SystemException(e, className, "getReportData", 
                    "Problem occurred retrieving Plan Entry Frequency. Contract#: " +
                    contractNumber);
        } finally {            
            close(stmt, conn);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getEmployeeSummary");
        }
        
        return psReportDataVO;

    }
    
    /**
     * Clean the staging data 
     * 
     * @param callSessionId
     * @param con
     * @throws SQLException
     */
    private static void cleanStaging(int callSessionId1, Connection con) throws SQLException {
        String sqlDelete = "delete from PSW100.STORED_PROCEDURE_STAGING where CALL_SESSION_ID = ?";
        
        if(callSessionId1 != 0) {            
            PreparedStatement stmtSession = con.prepareStatement(sqlDelete);
            stmtSession.setInt(1,callSessionId1);
            stmtSession.executeUpdate();
        }
    }

    /**
     * Creates a Value Object for each row returned by the stored procedure
     * 
     * @param resultSet
     * @param autoEnrollmentOn
     * @return
     * @throws SQLException
     */ 
    private static DeferralDetails fillInDataFromResultSet(ResultSet resultSet, boolean autoEnrollmentOn, int contractNumber) throws SQLException, SystemException {

        DeferralDetails item = new DeferralDetails();
        item.setFirstName(resultSet.getString(DeferralReportData.FIRST_NAME_COLUMN));
        item.setLastName(resultSet.getString(DeferralReportData.LAST_NAME_COLUMN));
        item.setMiddleInitial(resultSet.getString(DeferralReportData.MIDDLE_INITIAL_COLUMN));
        item.setSsn(resultSet.getString(DeferralReportData.SOCIAL_SECURITY_NO_COLUMN));
        item.setEmployerDesignatedID(resultSet.getString(DeferralReportData.EMPLOYEE_ID_COLUMN));
        item.setDivision(resultSet.getString(DeferralReportData.EMPLOYER_DIVISION_COLUMN));
        item.setEnrollmentStatus(resultSet.getString(DeferralReportData.ENROLLMENT_STATUS_COLUMN));
        item.setProfileId(resultSet.getString(DeferralReportData.PROFILE_ID_COLUMN));
        item.setParticipantInd(resultSet.getInt(DeferralReportData.PARTICIPANT_IND_COLUMN));         
        item.setDateNextADI(resultSet.getDate(DeferralReportData.NEXT_ANNIVERSARY_DATE_COLUMN));
        item.setIncreaseAmt(resultSet.getString(DeferralReportData.DEFER_INC_AMT_COLUMN) != null ? formatNumericValues(DOLLAR_FORMATTER,resultSet.getString(DeferralReportData.DEFER_INC_AMT_COLUMN)):null);
        item.setLimitAmt(resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_AMT_COLUMN)!= null ? formatNumericValues(DOLLAR_FORMATTER,resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_AMT_COLUMN)):null);
        item.setIncreasePct(resultSet.getString(DeferralReportData.DEFER_INC_PCT_COLUMN) != null ? formatNumericValues(PERCENTAGE_FORMATTER,resultSet.getString(DeferralReportData.DEFER_INC_PCT_COLUMN)):null);
        item.setLimitPct(resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_PCT_COLUMN) != null ?formatNumericValues(PERCENTAGE_FORMATTER,resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_PCT_COLUMN)):null);
        item.setAciSettingsInd(resultSet.getString(DeferralReportData.ACI_SETTINGS_IND_COLUMN));
        item.setBeforeTaxDeferralPct(getParsedPercent(resultSet.getString(DeferralReportData.BEFORE_TAX_DEFER_PCT_COLUMN)));
        item.setDesignatedRothDeferralPct(getParsedPercent(resultSet.getString(DeferralReportData.DESIG_ROTH_DEF_PCT_COLUMN)));
        item.setBeforeTaxDeferralAmt(resultSet.getString(DeferralReportData.BEFORE_TAX_DEFER_AMT_COLUMN));
        item.setDesignatedRothDeferralAmt(resultSet.getString(DeferralReportData.DESIG_ROTH_DEF_AMT_COLUMN));
        item.setAlert(getBoolean(resultSet.getString(DeferralReportData.ALERT_COLUMN)));
        item.setAdHoc401kDeferralChangeReqAmt(resultSet.getString(DeferralReportData.AD_HOC_401K_DEFERRAL_CHANGE_REQ_AMT));
        item.setAdHoc401kDeferralChangeReqPct(resultSet.getString(DeferralReportData.AD_HOC_401K_DEFERRAL_CHANGE_REQ_PCT));
        item.setAciReqWaitingApprovalAnniversaryDate(resultSet.getDate(DeferralReportData.ACI_REQ_WAITING_APPROVAL_ANNIVERSARY_DATE));
       	item.setLastDeferralUpdatedTs(resultSet.getTimestamp(DeferralReportData.DEFER_VALUES_LAST_UPDATED_TS_COLUMN) != null ? DATE_FORMATTER.format(resultSet.getTimestamp(DeferralReportData.DEFER_VALUES_LAST_UPDATED_TS_COLUMN)):"");
       	item.setInstructSrcCode(resultSet.getString(DeferralReportData.PCI_CONTRIBUTION_INSTRUCT_SRC_CODE));
       	item.setParticipantStatusCode(resultSet.getString(DeferralReportData.PARTICIPANT_STATUS_CODE));
       	// WARNING_IND contains Y if there is an outstanding ACi request or N otherwise
       	item.setOutstandingEziRequest(resultSet.getString(DeferralReportData.WARNING_IND_COLUMN));
       
       	//applyZeroRules(item, contractNumber);
        processWarnings(item, resultSet);        
        
        // Fill in the change history
        fillInChangeHistory(item, resultSet);

        return item;
    }
    
    private static void applyZeroRules(DeferralDetails item, int contractNumber) throws SystemException
    {
    	String beforeTaxDefAmt = item.getBeforeTaxDeferralAmt();
    	String beforeTaxDefPct = item.getBeforeTaxDeferralPct();
    	String designatedRothDefAmt = item.getDesignatedRothDeferralAmt();
    	String designatedRothDefPct = item.getDesignatedRothDeferralPct();
    	
    	String defType = getPlanDeferralType(contractNumber);
    	String btd[] = applyConfirmedDeferralZeroRules(beforeTaxDefAmt, beforeTaxDefPct,defType);
    	String drd[] = applyConfirmedDeferralZeroRules(designatedRothDefAmt, designatedRothDefPct,defType);
    	
    	item.setBeforeTaxDeferralAmt(btd[0]);
    	item.setBeforeTaxDeferralPct(btd[1]);
    	item.setDesignatedRothDeferralAmt(drd[0]);
    	item.setDesignatedRothDeferralPct(drd[1]);
        	
    }
    
    private static String[] applyConfirmedDeferralZeroRules(String amt, String pct, String defType)
    {
    	 	
    	if(isZero(amt)&& isZero(pct)){
    		if("$".equalsIgnoreCase(defType)){
    			pct="";
    		} else {
    			amt="";
    		}
    	}
    	else if(isZero(amt) && !isBlankOrNull(pct)){
    		amt="";
    	}else if(isZero(pct) && !isBlankOrNull(amt)){
    		pct="";
    	}
    	
    	return new String[]{amt,pct};
    	
    }
    
    private static boolean isZero(String amt)
    {
       	if("0".equals(amt) || "0.0".equals(amt) || "0.00".equals(amt))
    		return true;
    	return false;
    }
    
    private static boolean isBlankOrNull(String amt)
    {
    	if(amt == null || amt.trim().length() == 0)
    		return true;
    	return false;
    }
    
    private static String getPlanDeferralType(int contractNumber) throws SystemException
    {
    	String planDeferralType = null; 
    	try{
	    	ContractServiceDelegate delegate = ContractServiceDelegate.getInstance();  
	    	ContractServiceFeature csf = delegate.getContractServiceFeature(
	    			contractNumber, ServiceFeatureConstants.MANAGING_DEFERRALS);
	    	planDeferralType = null; 
	    	if (csf != null) {
	             planDeferralType = csf.getAttributeValue(
	            		 ServiceFeatureConstants.MD_DEFERRAL_TYPE);
	         }
    	}catch(ApplicationException ae)
    	{
    		// not able to retrieve deferral type so default to null
    		planDeferralType = null; 
    	}
         return planDeferralType;
    }


    /**
     * Utility method that parses the Warning column string and checks if any warnings
     * are present
     *  
     * @param item
     * @param resultSet
     * @throws SQLException
     */
    private static void processWarnings(DeferralDetails item, ResultSet resultSet) throws SQLException {
        String war = resultSet.getString(DeferralReportData.WARNING_COLUMN);
                
        // It is expected a String with at least 4 characters
        if(war != null && !"".equals(war.trim()) && war.length() >= 4) {
            if(war.charAt(0) != ' ') {
                item.getWarnings().add(DeferralReportData.BEFORE_TAX_EXCEEDS_PLAN_WARNING);
            }
            if(war.charAt(1) != ' ') {
                item.getWarnings().add(DeferralReportData.DEFERRAL_TOTAL_EXCEEDS_PLAN_WARNING);
            }
            if(war.charAt(2) != ' ') {
                item.getWarnings().add(DeferralReportData.DESIGNATED_ROTH_EXCEEDS_PLAN_WARNING);
            }
            
            if ("Y".equals(item.getAciSettingsInd())) { // defect 989 
	            if(war.charAt(3) != ' ') {
	                item.getWarnings().add(DeferralReportData.CONTRIB_PLUS_INCREASE_EXCEEDS_PLAN_WARNING);
	            }
            }
        } else {
            item.setWarnings(new ArrayList<String>());
        }        
    }
    
    /**
     * @param item
     * @throws SQLException 
     */
    private static void fillInChangeHistory(DeferralDetails item, ResultSet resultSet) throws SQLException {

    	if (resultSet.getString(DeferralReportData.ACI_SETTING_IND_LAST_NAME_COLUMN) !=null) {
	        item.getAutoIncreaseHistory().getCurrentUser().setLastName(resultSet.getString(DeferralReportData.ACI_SETTING_IND_LAST_NAME_COLUMN));
	        item.getAutoIncreaseHistory().getCurrentUser().setFirstName(resultSet.getString(DeferralReportData.ACI_SETTING_IND_FIRST_NAME_COLUMN));
    	} else {
            item.getAutoIncreaseHistory().getCurrentUser().setLastName(resultSet.getString(DeferralReportData.ACI_SETTING_IND_PPT_LAST_NAME_COLUMN));
            item.getAutoIncreaseHistory().getCurrentUser().setFirstName(resultSet.getString(DeferralReportData.ACI_SETTING_IND_PPT_FIRST_NAME_COLUMN));    		
    	}        
        item.getAutoIncreaseHistory().getCurrentUser().setUserIdType(resultSet.getString(DeferralReportData.ACI_SETTING_IND_CREATED_USER_ID_TYPE_COLUMN));
        item.getAutoIncreaseHistory().getCurrentUser().setUserId(resultSet.getString(DeferralReportData.ACI_SETTING_IND_CREATED_USER_ID_COLUMN));
        item.getAutoIncreaseHistory().setCurrentUpdatedTs(resultSet.getTimestamp(DeferralReportData.ACI_SETTING_IND_CREATED_TS_COLUMN));                    
        item.getAutoIncreaseHistory().setCurrentValue(resultSet.getString(DeferralReportData.ACI_SETTING_IND_PREVIOUS_VALUE_COLUMN));
        item.getAutoIncreaseHistory().setSourceChannelCode(resultSet.getString(DeferralReportData.ACI_SETTING_IND_SOURCE_CHANNEL_CODE_COLUMN));
        
        if (resultSet.getString(DeferralReportData.DEFFER_INC_PCT_LAST_NAME_COLUMN) != null) {
            item.getIncreasePctHistory().getCurrentUser().setLastName(resultSet.getString(DeferralReportData.DEFFER_INC_PCT_LAST_NAME_COLUMN));
            item.getIncreasePctHistory().getCurrentUser().setFirstName(resultSet.getString(DeferralReportData.DEFFER_INC_PCT_FIRST_NAME_COLUMN));        	
        } else {
            item.getIncreasePctHistory().getCurrentUser().setLastName(resultSet.getString(DeferralReportData.DEFFER_INC_PCT_PPT_LAST_NAME_COLUMN));
            item.getIncreasePctHistory().getCurrentUser().setFirstName(resultSet.getString(DeferralReportData.DEFFER_INC_PCT_PPT_FIRST_NAME_COLUMN));        	
        }        
        item.getIncreasePctHistory().getCurrentUser().setUserIdType(resultSet.getString(DeferralReportData.DEFFER_INC_PCT_CREATED_USER_ID_TYPE_COLUMN));
        item.getIncreasePctHistory().getCurrentUser().setUserId(resultSet.getString(DeferralReportData.DEFFER_INC_PCT_CREATED_USER_ID_COLUMN));
        item.getIncreasePctHistory().setCurrentUpdatedTs(resultSet.getTimestamp(DeferralReportData.DEFFER_INC_PCT_CREATED_TS_COLUMN));                    
        item.getIncreasePctHistory().setCurrentValue(formatNumericValues(PERCENTAGE_FORMATTER,resultSet.getString(DeferralReportData.DEFFER_INC_PCT_PREVIOUS_VALUE_COLUMN)));
        item.getIncreasePctHistory().setSourceChannelCode(resultSet.getString(DeferralReportData.DEFFER_INC_PCT_SOURCE_CHANNEL_CODE_COLUMN));

        if (resultSet.getString(DeferralReportData.DEFFER_INC_AMT_LAST_NAME_COLUMN) != null) { 
            item.getIncreaseAmtHistory().getCurrentUser().setLastName(resultSet.getString(DeferralReportData.DEFFER_INC_AMT_LAST_NAME_COLUMN));
            item.getIncreaseAmtHistory().getCurrentUser().setFirstName(resultSet.getString(DeferralReportData.DEFFER_INC_AMT_FIRST_NAME_COLUMN));        	
        } else {
            item.getIncreaseAmtHistory().getCurrentUser().setLastName(resultSet.getString(DeferralReportData.DEFFER_INC_AMT_PPT_LAST_NAME_COLUMN));
            item.getIncreaseAmtHistory().getCurrentUser().setFirstName(resultSet.getString(DeferralReportData.DEFFER_INC_AMT_PPT_FIRST_NAME_COLUMN));        	
        }
        item.getIncreaseAmtHistory().getCurrentUser().setUserIdType(resultSet.getString(DeferralReportData.DEFFER_INC_AMT_CREATED_USER_ID_TYPE_COLUMN));
        item.getIncreaseAmtHistory().getCurrentUser().setUserId(resultSet.getString(DeferralReportData.DEFFER_INC_AMT_CREATED_USER_ID_COLUMN));
        item.getIncreaseAmtHistory().setCurrentUpdatedTs(resultSet.getTimestamp(DeferralReportData.DEFFER_INC_AMT_CREATED_TS_COLUMN));                    
        item.getIncreaseAmtHistory().setCurrentValue(formatNumericValues(DOLLAR_FORMATTER,resultSet.getString(DeferralReportData.DEFFER_INC_AMT_PREVIOUS_VALUE_COLUMN)));
        item.getIncreaseAmtHistory().setSourceChannelCode(resultSet.getString(DeferralReportData.DEFFER_INC_AMT_SOURCE_CHANNEL_CODE_COLUMN));

        if (resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_PCT_LAST_NAME_COLUMN) != null) {
            item.getLimitPctHistory().getCurrentUser().setLastName(resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_PCT_LAST_NAME_COLUMN));
            item.getLimitPctHistory().getCurrentUser().setFirstName(resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_PCT_FIRST_NAME_COLUMN));        	
        } else {
            item.getLimitPctHistory().getCurrentUser().setLastName(resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_PCT_PPT_LAST_NAME_COLUMN));
            item.getLimitPctHistory().getCurrentUser().setFirstName(resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_PCT_PPT_FIRST_NAME_COLUMN));
        }
        item.getLimitPctHistory().getCurrentUser().setUserIdType(resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_PCT_CREATED_USER_ID_TYPE_COLUMN));
        item.getLimitPctHistory().getCurrentUser().setUserId(resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_PCT_CREATED_USER_ID_COLUMN));
        item.getLimitPctHistory().setCurrentUpdatedTs(resultSet.getTimestamp(DeferralReportData.DEFER_MAX_LIMIT_PCT_CREATED_TS_COLUMN));                    
        item.getLimitPctHistory().setCurrentValue(formatNumericValues(PERCENTAGE_FORMATTER,resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_PCT_PREVIOUS_VALUE_COLUMN)));
        item.getLimitPctHistory().setSourceChannelCode(resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_PCT_SOURCE_CHANNEL_CODE_COLUMN));

        if (resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_AMT_LAST_NAME_COLUMN) != null) {
            item.getLimitAmtHistory().getCurrentUser().setLastName(resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_AMT_LAST_NAME_COLUMN));
            item.getLimitAmtHistory().getCurrentUser().setFirstName(resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_AMT_FIRST_NAME_COLUMN));        	
        } else {
            item.getLimitAmtHistory().getCurrentUser().setLastName(resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_AMT_PPT_LAST_NAME_COLUMN));
            item.getLimitAmtHistory().getCurrentUser().setFirstName(resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_AMT_PPT_FIRST_NAME_COLUMN));        	
        }
        item.getLimitAmtHistory().getCurrentUser().setUserIdType(resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_AMT_CREATED_USER_ID_TYPE_COLUMN));
        item.getLimitAmtHistory().getCurrentUser().setUserId(resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_AMT_CREATED_USER_ID_COLUMN));
        item.getLimitAmtHistory().setCurrentUpdatedTs(resultSet.getTimestamp(DeferralReportData.DEFER_MAX_LIMIT_AMT_CREATED_TS_COLUMN));                    
        item.getLimitAmtHistory().setCurrentValue(formatNumericValues(DOLLAR_FORMATTER,resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_AMT_PREVIOUS_VALUE_COLUMN)));
        item.getLimitAmtHistory().setSourceChannelCode(resultSet.getString(DeferralReportData.DEFER_MAX_LIMIT_AMT_SOURCE_CHANNEL_CODE_COLUMN));
    }
    
    /**
     * Utility method that transforms the alert message into a boolean, because the value coming 
     * back from the stored procedure will be blank or 'Y    ' (as per that complex stored proc) 
     * 
     * @param alert
     * @return
     */
    private static boolean getBoolean(String alert) {
    	if (alert == null) return false;
    	return alert.trim().length()>0;
    }
    
    /**
     * Utility method that parses the String to show just the meaningful decimals 
     * 
     * @param percentage
     * @return
     */
    private static String getParsedPercent(String percentage) {
        if(percentage != null && !"".equals(percentage.trim())) {
            java.text.DecimalFormat format = new java.text.DecimalFormat("###.###");
            Number number = null;
    
            try {
                number = format.parse(percentage);
            } catch (java.text.ParseException e) {
                // Do nothing because it should not happen
                // the data comes from database and it should have been validated before
            }
    
            if(number != null) {
                double per = number.doubleValue()/100;
                return Double.toString(per); 
            }    
        }
        
        // Return as was retrieved from database for NULL, BLANK or un-parseable
        return percentage;
    }
        
    /**
     * Building the sort order.
     * 
     * @param criteria ReportCriteria
     * @return String
     */
    private static String createSortPhrase(ReportCriteria criteria) {
        StringBuffer result = new StringBuffer();
        Iterator sorts = criteria.getSorts().iterator();
        String sortDirection = null;  

        for (int i=0; sorts.hasNext(); i++) {
            ReportSort sort = (ReportSort)sorts.next();
            
            sortDirection = sort.getSortDirection();
            
            if(DeferralReportData.INCREASE_FIELD.equalsIgnoreCase(sort.getSortField())) {
                // Sort by % and after that $
                result.append(DeferralReportData.DEFER_INC_PCT_COLUMN).append(' ')
                    .append(sortDirection) 
                    .append(", ").append(DeferralReportData.DEFER_INC_AMT_COLUMN).append(' ')
                    .append(sortDirection)
                    .append(", " +  DeferralReportData.EMPLOYER_DIVISION_COLUMN).append(' ')
                    .append(sortDirection)
                    .append(", " +  DeferralReportData.LAST_NAME_COLUMN).append(' ')
                    .append(sortDirection)
                    .append(", " +  DeferralReportData.FIRST_NAME_COLUMN).append(' ') 
                    .append(sortDirection)
                    .append(", " + DeferralReportData.MIDDLE_INITIAL_COLUMN).append(' ')
                    .append(sortDirection);
            } else if(DeferralReportData.LIMIT_FIELD.equalsIgnoreCase(sort.getSortField())) {
                // Sort by % and after that $
                result.append(DeferralReportData.DEFER_MAX_LIMIT_PCT_COLUMN).append(' ')
                    .append(sortDirection)
                    .append(", ").append(DeferralReportData.DEFER_MAX_LIMIT_AMT_COLUMN).append(' ')
                    .append(sortDirection)
                    .append(", " +  DeferralReportData.EMPLOYER_DIVISION_COLUMN).append(' ')
                    .append(sortDirection)
                    .append(", " +  DeferralReportData.LAST_NAME_COLUMN).append(' ')
                    .append(sortDirection)
                    .append(", " +  DeferralReportData.FIRST_NAME_COLUMN).append(' ') 
                    .append(sortDirection)
                    .append(", " + DeferralReportData.MIDDLE_INITIAL_COLUMN).append(' ')
                    .append(sortDirection); 
            } else {
                result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');           
                result.append(sortDirection);
                
                if (DeferralReportData.LAST_NAME_FIELD.equals(sort.getSortField())) {
                    result.append(", " +  DeferralReportData.FIRST_NAME_COLUMN).append(' ') 
                            .append(sortDirection)
                            .append(", " + DeferralReportData.MIDDLE_INITIAL_COLUMN).append(' ')
                            .append(sortDirection);
                } else if(DeferralReportData.EMPLOYER_DIVISION_FIELD.equalsIgnoreCase(sort.getSortField())) {
                    result.append(", " +  DeferralReportData.LAST_NAME_COLUMN).append(' ')
                        .append(sortDirection)
                        .append(", " +  DeferralReportData.FIRST_NAME_COLUMN).append(' ') 
                        .append(sortDirection)
                        .append(", " + DeferralReportData.MIDDLE_INITIAL_COLUMN).append(' ')
                        .append(sortDirection);
                } else if(DeferralReportData.ACI_SETTING_IND_FIELD.equalsIgnoreCase(sort.getSortField())) {
                    result.append(", " +  DeferralReportData.EMPLOYER_DIVISION_COLUMN).append(' ')
                        .append(sortDirection)
                        .append(", " +  DeferralReportData.LAST_NAME_COLUMN).append(' ')
                        .append(sortDirection)
                        .append(", " +  DeferralReportData.FIRST_NAME_COLUMN).append(' ') 
                        .append(sortDirection)
                        .append(", " + DeferralReportData.MIDDLE_INITIAL_COLUMN).append(' ')
                        .append(sortDirection);
                }else if(DeferralReportData.BEFORE_TAX_DEFERRAL_PCT_FIELD.equalsIgnoreCase(sort.getSortField())) {
                    result.append(", " +  DeferralReportData.BEFORE_TAX_DEFER_AMT_COLUMN).append(' ')
                    .append(sortDirection)
                    .append(", " +  DeferralReportData.DESIG_ROTH_DEF_PCT_COLUMN).append(' ') 
                    .append(sortDirection)
                    .append(", " + DeferralReportData.DESIG_ROTH_DEF_AMT_COLUMN).append(' ')
                    .append(sortDirection);
             }
            }
            result.append(',');
            
            }
          
   
        
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
          
        return result.toString();
    }

    /**
     * Building the where clause.
     * 
     * @param criteria ReportCriteria
     * @return String
     */
    private static String createFilterPhrase(final ReportCriteria criteria) {
         StringBuffer result = new StringBuffer();
         String segment = (String) criteria.getFilterValue(DeferralReportData.FILTER_SEGMENT);
         String employmentStatus = (String) criteria.getFilterValue(DeferralReportData.FILTER_EMPLOYMENT_STATUS);
         String enrollmentStatus = (String) criteria.getFilterValue(DeferralReportData.FILTER_ENROLLMENT_STATUS);
         String lastNameValue = (String) criteria.getFilterValue(DeferralReportData.FILTER_LAST_NAME);
         String ssnValue = (String) criteria.getFilterValue(DeferralReportData.FILTER_SSN);
         String divisionValue = (String)criteria.getFilterValue(DeferralReportData.FILTER_DIVISION);
         
         if (!StringUtils.isEmpty(segment)) {
             result.append("PARTICIPANT_IND = ").append(segment).append(AND);
         } else {
             result.append("PARTICIPANT_IND != -1 ").append(AND);
         }
         
         // if "All" selected, exclude Cancelled employees for external users only
         // if "Active" selected, retrieve employees with blank or active status
         // else retrieve employees that have selected status
         if (StringUtils.isEmpty(employmentStatus) || employmentStatus.equals(ALL)) {
             if (criteria.isExternalUser()) {
                 result.append("VALUE(EMPLOYMENT_STATUS_CODE,'') != 'C'").append(AND);
             }
         } else if (employmentStatus.equals("A")) {
             result.append("VALUE(EMPLOYMENT_STATUS_CODE,'') in ('A','')").append(AND);
         } else {
             result.append("VALUE(EMPLOYMENT_STATUS_CODE,'') = ").append(wrapInSingleQuotes(employmentStatus).
             toUpperCase()).append(AND);
         }
        
         if (!StringUtils.isEmpty(enrollmentStatus) && !enrollmentStatus.equals(ALL)) {
             result.append("UPPER(ENROLLMENT_STATUS) = ").append(wrapInSingleQuotes(enrollmentStatus).
             toUpperCase()).append(AND);
         }
         
         if (!StringUtils.isEmpty(lastNameValue)) {
             result.append("UPPER(LAST_NAME) like ").append(wrapInSingleQuotes(lastNameValue + "%").
             toUpperCase()).append(AND);
         }
        
         if (!StringUtils.isEmpty(ssnValue)) {
             result.append("SOCIAL_SECURITY_NO = ").append(wrapInSingleQuotes(ssnValue)).
             append(AND);
         }
         
         if (!StringUtils.isEmpty(divisionValue)) {
             result.append("UPPER(EMPLOYER_DIVISION) like ").append(wrapInSingleQuotes(divisionValue + "%").
             toUpperCase() ).append(AND);
         }

         
         if (result.length() > 0 && AND.equals(result.substring(result.length() - AND.length()))) {
             result.delete(result.length() - AND.length(), result.length());
         }
        
         return (result.toString().trim().length() > 0 ? result.toString() : null);
    }
    
    
    /**
     * Returns the VO needed to show the statistics
     * 
     * @param contractId
     * @return
     * @throws SystemException
     */
    public static DeferralStatisticsSummary getStatisticsSummary(int contractId) throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getDeferralStatisticsSummary");
        }
    
        Connection conn = null;
        CallableStatement stmt = null;
        DeferralStatisticsSummary statisticsSummary = null;
            
        try {   
            // setup the connection and the statement
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_STATISTICS_SUMMARY);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Calling Stored Procedure: " + GET_STATISTICS_SUMMARY);
            }
            
            stmt.setInt(1, contractId);
            
            // execute the stored procedure
            stmt.execute();
            
            statisticsSummary = new DeferralStatisticsSummary();
            
            // First set 
            ResultSet resultSet = stmt.getResultSet();            
            
            if (resultSet != null) {                
//                    processSummaryValues(resultSet, statisticsSummary);                
            }
                        
            //  Second set - the details for each employee that had changes
            if(stmt.getMoreResults()) {
                resultSet = stmt.getResultSet();
                
                if (resultSet != null) {
                    while (resultSet.next()) {
                        processDeferralValues(resultSet, statisticsSummary);
                        processACIParticipation(resultSet, statisticsSummary);
                    }
                }
            }
        } catch (SQLException e) {
           throw new SystemException(e, className, "getDeferralStatisticsSummary", 
           "Problem occurred during GET_DEFERRAL_STATS stored proc call. Report criteria:"
           + contractId);
        } finally {
            close(stmt, conn);
        }
    
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getDeferralStatisticsSummary");
        }
        
        return statisticsSummary;          
    }
        
    /**
     * Calculates the statistics for deferrals
     * 
     * @param resultSet
     * @param summary
     * @throws SQLException
     */
    private static void processDeferralValues(ResultSet resultSet, DeferralStatisticsSummary summary) throws SQLException {
        BigDecimal before_tax_defer_pct = resultSet.getBigDecimal(BEFORE_TAX_DEFER_PCT);
        BigDecimal before_tax_defer_amt = resultSet.getBigDecimal(BEFORE_TAX_DEFER_AMT);
        BigDecimal desig_roth_def_pct = resultSet.getBigDecimal(DESIG_ROTH_DEF_PCT);
        BigDecimal desig_roth_def_amt = resultSet.getBigDecimal(DESIG_ROTH_DEF_AMT);
            
        if(before_tax_defer_pct == null && 
                before_tax_defer_amt ==null &&
                desig_roth_def_pct == null &&
                desig_roth_def_amt == null) {
            summary.setUnknown(summary.getUnknown() + 1);
        } else {
            if(before_tax_defer_pct != null) {
                summary.processTraditionalPct(before_tax_defer_pct);
            }
            if(desig_roth_def_pct != null) {
                summary.processRothPct(desig_roth_def_pct);
            }
            if(before_tax_defer_amt != null) {
                summary.processTraditionalAmt(before_tax_defer_amt);
            }
            if(desig_roth_def_amt != null) {
                summary.processRothAmt(desig_roth_def_amt);
            }
        }
    }   
    
    /**
     * Utility method that calculates how many employees fall into each participation category 
     * 
     * @param resultSet
     * @param summary
     * @throws SQLException
     */
    private static void processACIParticipation(ResultSet resultSet, DeferralStatisticsSummary summary) throws SQLException {               

    	if(resultSet.getString(DeferralReportData.PARTICIPANT_STATUS_CODE) == null
    			|| resultSet.getString(DeferralReportData.PARTICIPANT_STATUS_CODE).equals(OPTED_OUT_PARTICIPANT_STATUS_CODE)) {
    		// not enrolled or 90 day opt out
    		summary.setNotEnrolled(summary.getNotEnrolled() + 1);
    	}
    	else {
    		if(!"Y".equalsIgnoreCase(resultSet.getString(DeferralReportData.ACI_SETTING_IND_COLUMN))) {
    	       summary.setEnrolledButNotSignedUp(summary.getEnrolledButNotSignedUp() + 1);
            } 
	        else{       	
		        if(resultSet.getBigDecimal(DeferralReportData.DEFER_INC_PCT_COLUMN) == null && 
		           resultSet.getBigDecimal(DeferralReportData.DEFER_INC_AMT_COLUMN) == null &&
		           resultSet.getBigDecimal(DeferralReportData.DEFER_MAX_LIMIT_AMT_COLUMN) == null &&
		           resultSet.getBigDecimal(DeferralReportData.DEFER_MAX_LIMIT_PCT_COLUMN) == null ){
		        	summary.setDefaultSettings(summary.getDefaultSettings() + 1); 
		        }
		        else {
		        	 summary.setActivelyManaged(summary.getActivelyManaged() + 1);
		        }
	        }
    	}
    }
    
    private static String formatNumericValues(DecimalFormat df, String value)
    {
    	String formattedValue = value;
    	if(value != null && value.trim().length() > 0){
	    	try{
	    		Double d = Double.valueOf(value);
	    		formattedValue = df.format(d);
	    	}catch(NumberFormatException ne)
	    	{
	    		// do nothing
	    	}
    	}
    	else
    	{
    		formattedValue="";
    	}
    	return formattedValue;
    }
    
}