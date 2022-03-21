package com.manulife.pension.service.loan.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SelectBeanListQueryHandler;
import com.intware.dao.jdbc.SelectBeanQueryHandler;
import com.intware.dao.jdbc.SelectMultiFieldMultiRowQueryHandler;
import com.intware.dao.jdbc.SelectMultiFieldQueryHandler;
import com.intware.dao.jdbc.SelectSingleOrNoValueQueryHandler;
import com.intware.dao.jdbc.SelectSingleValueQueryHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.valueobject.PlanData;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.dao.DaoConstants;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.ActivitySummary;
import com.manulife.pension.service.loan.LoanDefaults;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.loan.exception.LoanDaoException;
import com.manulife.pension.service.loan.valueobject.LoanEventData;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.loan.valueobject.LoanTypeVO;
import com.manulife.pension.service.loan.valueobject.OutstandingLoan;

/**
 * This class represents a DAO that provides supportive data to the loan
 * application.
 */
public class LoanSupportDao extends BaseDatabaseDAO {

	private DataSource csdbDataSource;

	private static final Logger logger = Logger.getLogger(LoanSupportDao.class);

	public LoanSupportDao(DataSource csdbDataSource) {
		this.csdbDataSource = csdbDataSource;
	}

	public Integer getParticipantIdByProfileId(Integer profileId)
			throws LoanDaoException {
		SelectSingleOrNoValueQueryHandler handler = new SelectSingleOrNoValueQueryHandler(
				csdbDataSource, LoanSupportDaoSql.PARTICIPANT_ID_BY_PROFILE_ID,
				new int[] { Types.INTEGER }, Integer.class);
		try {
			Integer result = (Integer) handler
					.select(new Object[] { profileId });
			return result;
		} catch (DAOException e) {
			throw new LoanDaoException(
					"Cannot retrieve participant ID for profile ID ["
							+ profileId + "]");
		}
	}

	/**
	 * Retrieve loan related plan data for the given contract.
	 * 
	 * @param contractId
	 * @return a loan related plan data.
	 * @throws LoanDaoException
	 */
	public LoanPlanData getLoanPlanData(Integer contractId)
			throws LoanDaoException {
		LoanPlanData loanPlanData = null;
		Object[][] planLoanFields;
		SelectBeanQueryHandler handler = new SelectBeanQueryHandler(
				csdbDataSource, LoanSupportDaoSql.PLAN_DATA, LoanPlanData.class);
		SelectSingleValueQueryHandler countMoneyTypeAllowedForLoanHandler = new SelectSingleValueQueryHandler(
				csdbDataSource,
				LoanSupportDaoSql.COUNT_MONEY_TYPE_ALLOWED_FOR_LOAN,
				Integer.class);
		SelectMultiFieldMultiRowQueryHandler planLoanHandler = new SelectMultiFieldMultiRowQueryHandler(
				DaoConstants.DataSourceJndiName.CUSTOMER_SERVICE,
				LoanSupportDaoSql.SELECT_PLAN_LOAN_DATA, new Class[] {
						String.class, Integer.class });

		try {
			Integer countMoneyTypeAllowedForLoan = (Integer) countMoneyTypeAllowedForLoanHandler
					.select(new Object[] { contractId });
			loanPlanData = (LoanPlanData) handler
					.select(new Object[] { contractId });
			loanPlanData
					.setNoMoneyTypeAllowedForLoan(countMoneyTypeAllowedForLoan == 0);
		} catch (DAOException e) {
			throw new LoanDaoException("Cannot retrieve plan data ["
					+ contractId + "]", e);
		}

		if (loanPlanData.getThirdPartyAdminId() != null
				&& loanPlanData.getThirdPartyAdminId() == 0) {
			/*
			 * SelectBeanQueryHandler sets all integer/long parameter to 0 if
			 * they are null.
			 */
			loanPlanData.setThirdPartyAdminId(null);
		}

		if (PlanData.UNSPECIFIED_CODE.equals(loanPlanData
				.getSpousalConsentReqdInd())) {
			/*
			 * An unspecified code ('U') is much harder to work with than null.
			 * So, convert the indicator to null if it's not specified.
			 */
			loanPlanData.setSpousalConsentReqdInd(null);
		}

		if (PlanData.PAYROLL_FREQUENCY_UNSPECIFIED.equals(loanPlanData
				.getPayrollFrequency())) {
			loanPlanData.setPayrollFrequency(null);
		}

		if (loanPlanData.getMaxNumberOfOutstandingLoans() == null
				|| loanPlanData.getMaxNumberOfOutstandingLoans().intValue() == 0) {
			/*
			 * 0 means unspecified.
			 */
			loanPlanData.setMaxNumberOfOutstandingLoans(LoanDefaults
					.getNumberOfLoansAllowed());
			loanPlanData.setUsingDefaultMaxNumberOfOutstandingLoans(true);
		}

		try {
			planLoanFields = (Object[][]) planLoanHandler
					.select(new Object[] { new Integer(contractId) });
		} catch (DAOException e) {
			throw new LoanDaoException(
					"Cannot retrieve PLAN_LOAN data for contract ["
							+ contractId + "]", e);
		}

		/*
		 * Set the maximum amortization years values for the various loan types
		 * for the contract, if they exist on CSDB.
		 */
		if (planLoanFields != null && planLoanFields.length > 0
				&& planLoanFields[0] != null) {
			for (int i = 0; i < planLoanFields.length; i++) {
				Integer periodYears = (Integer) planLoanFields[i][1];
				if (periodYears != null && periodYears != 0) {
					loanPlanData.setMaximumAmortizationYears(
							(String) planLoanFields[i][0],
							(Integer) planLoanFields[i][1]);
				}
			}
		}
		
			getLoanDataFees(loanPlanData,contractId);
		
		/*
		 * If a loan type does not contain a maximum amortization years value,
		 * assign the default value.
		 */
		String[] loanTypes = new String[] { LoanConstants.TYPE_GENERAL_PURPOSE,
				LoanConstants.TYPE_HARDSHIP,
				LoanConstants.TYPE_PRIMARY_RESIDENCE, };
		
		for (String loanType : loanTypes) {
			LoanTypeVO loanTypesValues = new LoanTypeVO();
			loanTypesValues.setLoanTypeCode(loanType);
			loanTypesValues.setMaxAmortizationYear(loanPlanData.getMaximumAmortizationYears(loanType));
			loanPlanData.getLoanTypeList().add(loanTypesValues);
	   }

		for (String loanType : loanTypes) {
			if (loanPlanData.getMaximumAmortizationYears(loanType) == null) {
				loanPlanData.setMaximumAmortizationYears(loanType, LoanDefaults
						.getMaximumAmortizationYears(loanType));
				loanPlanData.setUsingDefaultMaximumAmortizationYears(true);
			}
		}

		/*
		 * Set default maximum/minimum data if it's not in the plan
		 */
		if (loanPlanData.getMaximumLoanAmount() == null) {
			loanPlanData.setMaximumLoanAmount(LoanDefaults
					.getMaximumLoanAmount());
			loanPlanData.setUsingDefaultMaximumLoanAmount(true);
		}
		if (loanPlanData.getMaximumLoanPercentage() == null) {
			loanPlanData.setMaximumLoanPercentage(LoanDefaults
					.getMaximumLoanPercentage());
			loanPlanData.setUsingDefaultMaximumLoanPercentage(true);

		}
		if (loanPlanData.getMinimumLoanAmount() == null) {
			loanPlanData.setMinimumLoanAmount(LoanDefaults
					.getMinimumLoanAmount());
			loanPlanData.setUsingDefaultMinimumLoanAmount(true);
		}

		if (loanPlanData.getLoanInterestRateOverPrime() == null) {
			loanPlanData.setLoanInterestRateOverPrime(LoanDefaults
					.getLoanInterestRateAdjustment());
			loanPlanData.setUsingDefaultInterestRateOverPrime(true);
		} else {
			BigDecimal interestRate = loanPlanData
					.getLoanInterestRateOverPrime();
			loanPlanData.setLoanInterestRateOverPrime(interestRate
					.divide(LoanConstants.ONE_HUNDRED_2_DECIMAL_PLACES));
		}
		return loanPlanData;
	}

	
	
	private LoanPlanData getLoanDataFees(LoanPlanData loanPlanData,Integer contractId) throws LoanDaoException {
		
		SelectMultiFieldQueryHandler plandatafeesHandler = new SelectMultiFieldQueryHandler(
				csdbDataSource, LoanSupportDaoSql.PLAN_DATA_FEES,new Class[] {
						BigDecimal.class,BigDecimal.class,BigDecimal.class,BigDecimal.class});
		
		Object[] plandatafees;
		
			try {
				plandatafees = (Object[]) plandatafeesHandler
						.select(new Object[] { contractId });
				
				BigDecimal feeamt = (BigDecimal)plandatafees[2];
				BigDecimal chargefee = (BigDecimal)plandatafees[3];
				
				loanPlanData.setContractLoanMonthlyFlatFee(feeamt);
				loanPlanData.setContractLoanExpenseMarginPct(chargefee);
				
			} catch (DAOException e) {
				throw new LoanDaoException(
						"Cannot retrieve PLAN_LOAN_FEES data for contract ["
								+ contractId + "]", e);
			}
		return loanPlanData;
	}
	
	
	private LoanMoneyType getLoanMoneyType(
			List<LoanMoneyType> participantMoneyTypes, String moneyTypeId) {
		for (Iterator<LoanMoneyType> it = participantMoneyTypes.iterator(); it
				.hasNext();) {
			LoanMoneyType participantMoneyType = it.next();
			if (moneyTypeId.equals(participantMoneyType.getMoneyTypeId())) {
				return participantMoneyType;
			}
		}
		return null;
	}

	/**
	 * Returns participant money types (excluding UM money types)
	 * 
	 * @param contractId
	 * @param profileId
	 * @return
	 * @throws LoanDaoException
	 */
	public List<LoanMoneyType> getParticipantMoneyTypesForLoans(
			Integer contractId, Integer profileId) throws LoanDaoException {
		SelectBeanListQueryHandler handler = new SelectBeanListQueryHandler(
				csdbDataSource,
				LoanSupportDaoSql.PARTICIPANT_MONEY_TYPES_BALANCE_LIST,
				LoanMoneyType.class);
		SelectBeanListQueryHandler handlerLoans = new SelectBeanListQueryHandler(
				csdbDataSource,
				LoanSupportDaoSql.PARTICIPANT_MONEY_TYPES_LOAN_BALANCE_LIST,
				LoanMoneyType.class);
		try {
			List<LoanMoneyType> loanMoneyTypeLoans = (List<LoanMoneyType>) handlerLoans
					.select(new Object[] { profileId, contractId });
			List<LoanMoneyType> loanMoneyTypes = (List<LoanMoneyType>) handler
					.select(new Object[] { profileId, contractId });

			/*
			 * Combine the loan balance into the list of money types.
			 */
			for (LoanMoneyType loanMoneyType : loanMoneyTypes) {
				LoanMoneyType loanMoneyTypeLoan = getLoanMoneyType(
						loanMoneyTypeLoans, loanMoneyType.getMoneyTypeId());
				if (loanMoneyTypeLoan != null) {
					loanMoneyType.setLoanBalance(loanMoneyTypeLoan
							.getLoanBalance());
				} else {
					loanMoneyType.setLoanBalance(BigDecimal.ZERO);
				}
			}

			/*
			 * Add remaining money types that do not have account balance.
			 */
			for (LoanMoneyType loanMoneyTypeLoan : loanMoneyTypeLoans) {
				LoanMoneyType loanMoneyType = getLoanMoneyType(loanMoneyTypes,
						loanMoneyTypeLoan.getMoneyTypeId());
				if (loanMoneyType == null) {
					loanMoneyTypeLoan.setAccountBalance(BigDecimal.ZERO);
					loanMoneyTypes.add(loanMoneyTypeLoan);
				}
			}

			return loanMoneyTypes;

		} catch (DAOException e) {
			throw new LoanDaoException("Cannot retrieve data contract ID ["
					+ contractId + "] profile ID [" + profileId + "]", e);
		}
	}

	/**
	 * Returns # of outstanding loans
	 * 
	 * @param contractId
	 * @param profileId
	 * @return
	 * @throws LoanDaoException
	 */
	public Integer getNumberOfOutstandingLoans(Integer contractId,
			Integer participantId) throws LoanDaoException {

		SelectSingleValueQueryHandler handler = new SelectSingleValueQueryHandler(
				csdbDataSource, LoanSupportDaoSql.OUTSTANDING_LOAN_COUNT,
				Integer.class);

		try {
			Integer count = (Integer) handler.select(new Object[] {
					participantId, contractId });
			return count;

		} catch (DAOException e) {
			throw new LoanDaoException("Cannot retrieve data contract ID ["
					+ contractId + "] participant ID [" + participantId + "]",
					e);
		}
	}

	/**
	 * Checks if the participant has positive PBA balance.
	 * 
	 * @param contractId
	 * @param profileId
	 * @return
	 * @throws LoanDaoException
	 */
	public boolean isPositivePbaBalance(Integer contractId, Integer profileId)
			throws LoanDaoException {

		SelectSingleValueQueryHandler handler = new SelectSingleValueQueryHandler(
				csdbDataSource, LoanSupportDaoSql.PBA_BALANCE, BigDecimal.class);

		try {
			BigDecimal balance = (BigDecimal) handler.select(new Object[] {
					profileId, contractId });
			return balance.compareTo(BigDecimal.ZERO) > 0;

		} catch (DAOException e) {
			throw new LoanDaoException("Cannot retrieve data contract ID ["
					+ contractId + "] profile ID [" + profileId + "]", e);
		}
	}

	/**
	 * gets outstanding old i:loan requests count
	 * 
	 * @param contractId
	 * @return
	 * @throws LoanDaoException
	 */
	public Integer getOutstandingOldIloanRequestsCount(int contractId)
			throws LoanDaoException {

		SelectSingleValueQueryHandler handler = new SelectSingleValueQueryHandler(
				csdbDataSource,
				LoanSupportDaoSql.OUTSTANDING_OLD_ILOAN_REQUEST_COUNT,
				Integer.class);

		try {
			Integer count = (Integer) handler
					.select(new Object[] { contractId });
			return count;

		} catch (DAOException e) {
			throw new LoanDaoException(
					"Cannot retrieve Old ILoan Requests data for the contract ["
							+ contractId + "]", e);
		}
	}

	/**
	 * get LRK01 and Allow Online Loans CSF information for a given list of
	 * contracts
	 * 
	 * @param contractIdList
	 * @return Map of Contract Id & its specific LoanSettings
	 */
	public Map<Integer, ArrayList<LoanSettings>> getPartialLoanSettingsData(
			Integer[] contractIdList) throws DAOException {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		Map<Integer, ArrayList<LoanSettings>> loanSettingsMap = new HashMap<Integer, ArrayList<LoanSettings>>();

		String sql = LoanSupportDaoSql.GET_PARTIAL_LOAN_SETTINGS;

		try {

			conn = csdbDataSource.getConnection();

			stmt = conn.createStatement();

			String contractIdString = "( ";
			for (int i = 0; i < contractIdList.length; i++) {
				contractIdString += contractIdList[i];
				if (i != contractIdList.length - 1) {
					contractIdString += ",";
				}
			}

			contractIdString += ")";
			sql = sql + contractIdString;
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ArrayList<LoanSettings> result = new ArrayList<LoanSettings>();
				LoanSettings loanSettings = new LoanSettings();
				loanSettings.setLrk01(true);

				if (rs.getString("SERVICE_FEATURE_VALUE") != null
						&& rs.getString("SERVICE_FEATURE_VALUE").trim().equals(
								"Y")) {
					loanSettings.setAllowOnlineLoans(true);
				}

				result.add(loanSettings);
				loanSettingsMap.put(rs.getInt("CONTRACT_ID"), result);
			}

			return loanSettingsMap;
		} catch (SQLException e) {
			throw new DAOException(
					"Problem occurred in getting Partial LoanSettings", e);
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DAOException(
						"Problem occurred in getting Partial LoanSettings", e);
			}
		}
	}

	/**
	 * get if the contract has Loan Record Keeping Product Feature
	 * 
	 * @param contractId
	 * @return
	 * @throws LoanDaoException
	 */
	public boolean hasLoanRecordKeepingProductFeature(int contractId)
			throws LoanDaoException {

		SelectSingleValueQueryHandler handler = new SelectSingleValueQueryHandler(
				csdbDataSource, LoanSupportDaoSql.LRK_FEATURE_SQL,
				Integer.class);

		try {
			Integer count = (Integer) handler
					.select(new Object[] { contractId });
			return (count != null && count > 0);

		} catch (DAOException e) {
			throw new LoanDaoException(
					"Cannot retrieve loan record keeping prod feature information for ["
							+ contractId + "]", e);
		}
	}

	/**
	 * helper method to retrieve the participant profile id for a given
	 * submission number
	 * 
	 * @param submissionNumber
	 * @param contractId
	 * @return
	 */
	public Integer getParticipantProfileIdForSubmission(int submissionNumber,
			int contractId) throws LoanDaoException {
		Integer participantProfileId = null;
		SelectSingleValueQueryHandler handler = new SelectSingleValueQueryHandler(
				csdbDataSource,
				LoanSupportDaoSql.GET_PARTICIPANT_PROFILE_ID_FOR_SUBMISSION,
				Integer.class);

		try {
			participantProfileId = (Integer) handler.select(new Object[] {
					submissionNumber, contractId });

		} catch (DAOException e) {
			throw new LoanDaoException(
					"Cannot retrieve participant profile id for Contract Id["
							+ contractId + "], SubmissionNumber ["
							+ submissionNumber + "]", e);
		}
		return participantProfileId;
	}

	
	
	/**
	 * LoanEventHandlers requires the information about initiator, reviewer &
	 * approver of a loan request. This method is created to support the same.
	 * 
     * 
     * If the loan is initiated by a participant, then it will have the following states:
     * Created (Sent for Review by a participant) - status SU 
     * Reviewed (Sent for Acceptance by a plan sponsor or a TPA) - status SA
     * Accepted (Sent for Approval by participant) - status RE 
     * Approved or Denied by a plan sponsor
     * 
     * If the loan is initiated by a plan sponsor or TPA, then it will have the following states:
     * Created (Sent for Review by plan sponsor or TPA) - status SU 
     * Reviewed (Sent for Approval by plan sponsor or TPA) - status RE
     * Approved or Denied by a plan sponsor
     * 
     * CL 119183 changes:
     * A reviewer can not be a participant. 
     * For a participant-initiated loan, reviewer ID will be that of the SA status
     * For a plan sponsor or TPA initiated loan, reviewer ID will be that of the RE status
     *   
	 * @param submissionId
	 * @return
	 * @throws LoanDaoException
	 */
	public LoanEventData getLoanDataForEventMessages(int submissionId)
			throws LoanDaoException {

		LoanEventData data = null;
		Connection conn = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;

		try {
			conn = BaseDatabaseDAO.getDefaultConnection(LoanSupportDao.class
					.getName(), BaseDatabaseDAO.CUSTOMER_DATA_SOURCE_NAME);
			
			stmt1 = conn.prepareStatement(LoanSupportDaoSql.SELECT_LOAN_INITIATOR_FOR_EVENTS);
			stmt1.setInt(1, submissionId);
			rs1 = stmt1.executeQuery();
			
			if (rs1 != null && rs1.next()) {
				data = new LoanEventData();
				data.setSubmissionId(submissionId);
				data.setInitiatorUserProfileId(rs1.getInt("PROFILEID"));
				data.setCreatedByRoleCode(rs1.getString("ROLECODE"));
				
				stmt2 = conn.prepareStatement(LoanSupportDaoSql.SELECT_LOAN_REVIEWER_FOR_EVENTS);
				stmt2.setInt(1, submissionId);
				rs2 = stmt2.executeQuery();
				
				while (rs2.next()) {
				    String status = rs2.getString("STATUS");
				    if (((ActivitySummary.SENT_FOR_APPROVAL).equalsIgnoreCase(status) 
				            && !"PA".equalsIgnoreCase(data.getCreatedByRoleCode()))
				        || ActivitySummary.SENT_FOR_ACCEPTANCE.equalsIgnoreCase(status)) {
				        
				        data.setReviewerUserProfileId(rs2.getInt("PROFILEID"));
				        
				    } else if ((ActivitySummary.APPROVED).equalsIgnoreCase(status)) {
				        
				        data.setApproverUserProfileId(rs2.getInt("PROFILEID"));
				        
				    }
				}
			}

		} catch (SQLException e) {
			throw new LoanDaoException(
					"Problem occurred in retrieving data for SubmissionId "
							+ submissionId, e);
		} catch (SystemException e) {
			throw new LoanDaoException(
					"Problem occurred while getting data for submissionId "
							+ submissionId, e);
		} finally {
			try {
                if(rs1 != null){
                    rs1.close();
                }
                
                if(rs2 != null){
                    rs2.close();
                }
            } catch (SQLException e) {
                throw new LoanDaoException("Can't close resultsets for submissionId " + submissionId, e);
            }
            BaseDatabaseDAO.close(stmt1, conn);
            BaseDatabaseDAO.close(stmt2, conn);
		}

		return data;
	}

	/**
	 * retrieves the name of the contract created for the use of event
	 * processing
	 * 
	 * @param contractId
	 * @return
	 * @throws LoanDaoException
	 */
	public String getContractName(int contractId) throws LoanDaoException {
		String contractName;

		SelectSingleValueQueryHandler handler = new SelectSingleValueQueryHandler(
				csdbDataSource, LoanSupportDaoSql.GET_CONTRACT_NAME_SQL,
				String.class);

		try {
			contractName = (String) handler.select(new Object[] { contractId });

		} catch (DAOException e) {
			throw new LoanDaoException("Cannot retrieve contract name for ["
					+ contractId + "]", e);
		}
		return contractName;
	}

	/**
	 * This method returns the Outstanding loan for given participant, contract,
	 * and loanid
	 * 
	 * @param contractId
	 * @param participantId
	 * @param loanId
	 * @return
	 * @throws DistributionServiceException
	 */
	public OutstandingLoan getOutstandingLoan(Integer contractId,
			Long profileId, Integer loanId) throws DistributionServiceException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		OutstandingLoan returnVo = null;
		try {
			conn = csdbDataSource.getConnection();
			stmt = conn
					.prepareStatement(LoanSupportDaoSql.SQL_SELECT_OUTSTANDING_LOAN);
			stmt.setInt(1, contractId);
			stmt.setLong(2, profileId);
			stmt.setInt(3, loanId);
			rs = stmt.executeQuery();

			if (rs.next()) {
				returnVo = toOutstandingLoan(rs);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new LoanDaoException("Problem occurred in prepared call: "
					+ LoanSupportDaoSql.SQL_SELECT_OUTSTANDING_LOAN
					+ " for contract ID:" + contractId + " participant ID:"
					+ profileId + ", loan id=" + loanId, e);
		} finally {
			BaseDatabaseDAO.close(stmt, conn);
		}
		return returnVo;
	}

	/**
	 * Populates and returns OutstandingLoan from Resultset
	 * 
	 * @param rs
	 *            ResultSet
	 * @return OutstandingLoan
	 * @throws SQLException
	 */
	private OutstandingLoan toOutstandingLoan(ResultSet rs) throws SQLException {
		OutstandingLoan returnVo = new OutstandingLoan(
				rs.getLong("PROFILE_ID"),
				rs.getLong("PARTICIPANT_ID"),
				rs.getInt("CONTRACT_ID"),
				rs.getInt("LOAN_ID"),
				rs.getBigDecimal("LOAN_PRINCIPAL_AMT"),
				new java.util.Date(rs.getDate("EFFECTIVE_DATE").getTime()),
				rs.getBigDecimal("LOAN_INTEREST_RATE_PCT"),
				rs.getBigDecimal("OUTSTANDING_PRINCIPAL_AMT"),
				rs.getBigDecimal("OUTSTANDING_INTEREST_AMT"),
				new java.util.Date(rs.getDate("MATURITY_DATE").getTime()),
				rs.getBigDecimal("LOAN_EXPENSE_MARGIN_RATE"),
				new java.util.Date(rs.getDate("LAST_REPAYMENT_DATE").getTime()),
				rs.getBigDecimal("LAST_REPAYMENT_AMT"), rs
						.getLong("LAST_REPAYMENT_TRANSACTION_NO"),
				new java.util.Date(rs.getDate("LOAN_CREATE_DATE").getTime()));

		return returnVo;
	}

}
