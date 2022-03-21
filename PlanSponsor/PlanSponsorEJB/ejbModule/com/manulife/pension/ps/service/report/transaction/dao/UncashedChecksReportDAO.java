package com.manulife.pension.ps.service.report.transaction.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.heartbeat.monitor.TransactionMonitor;
import com.manulife.pension.ps.service.report.exception.UncashedChecksDatabaseAccessException;
import com.manulife.pension.ps.service.report.transaction.valueobject.UncashedChecksReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.UncashedChecksReportItem;
import com.manulife.pension.service.dao.DaoConstants;
import com.manulife.pension.service.report.dao.DirectSqlReportDAOHelper;
import com.manulife.pension.service.report.dao.ReportItemTransformer;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

public class UncashedChecksReportDAO extends ReportServiceBaseDAO {

	private static final Logger logger = Logger
			.getLogger(UncashedChecksReportDAO.class);

	private static final boolean DEBUG_SQL = true;

	private static final String className = UncashedChecksReportDAO.class
			.getName();

	final static String PARTICIPANT_NAME = "PARTICIPANT_NAME";

	final static String LAST_NAME = "LAST_NAME";

	final static String FIRST_NAME = "FIRST_NAME";

	final static String SOCIAL_SECURITY_NUMBER = "SOCIAL_SECURITY_NUMBER";

	final static String CHECK_ISSUE_DATE = "CHECK_ISSUE_DATE";

	final static String PAYEE_TYPE = "PAYEE_TYPE";

	final static String PAYEE_NAME = "PAYEE_NAME";

	final static String CHECK_AMOUNT = "CHECK_AMOUNT";

	final static String TRANSACTION_DATE = "TRANSACTION_DATE";

	final static String TRANSACTION_TYPE = "TRANSACTION_TYPE";

	final static String TRANSACTION_NUMBER = "TRANSACTION_NUMBER";

	final static String CHECK_STATUS = "CHECK_STATUS";

	final static String PARTICIPANT_ID = "PARTICIPANT_ID";

	final static String PROFILE_ID = "PROFILE_ID";

	final static String CYCLE_DATE = "CYCLE_DATE";

	final static String TRANSACTION_REASON_CODE = "TRANSACTION_REASON_CODE";
	
	final static String CONTRACT_NUMBER = "contractNumber";
	
	private static final String GET_UNCASHED_CHECK_INFORMATION = "SELECT A.PRTID AS PARTICIPANT_ID, " +
			" CHQISDT AS CHECK_ISSUE_DATE," +
			" PRTLSTNM AS LAST_NAME," +
			" PRTFSTNM AS FIRST_NAME," +
			" PYEETYP AS PAYEE_TYPE," +
			" PYEENAME AS PAYEE_NAME," +
			" CHQAMT AS CHECK_AMOUNT," +
			" TRANNODT AS TRANSACTION_DATE," +
			" TRTYP AS TRANSACTION_TYPE," +
			" TRANNO AS TRANSACTION_NUMBER," +
			" A.PRTSSN AS SOCIAL_SECURITY_NUMBER," +
			" CHQSTUS AS CHECK_STATUS," +
			" TRRSNCD AS TRANSACTION_REASON_CODE" +
			" FROM "+ DaoConstants.SchemaName.APOLLO+ ".VLP1417 A" +
			" LEFT OUTER JOIN "+ DaoConstants.SchemaName.APOLLO+ ".VLP1074 B on B.PRTID = A.PRTID" +
			" WHERE CNNO = ? and CHQSTUS in ('SD','OS')";

	private static final String GET_PROFILE_ID = "SELECT PROFILE_ID FROM "
			+ DaoConstants.SchemaName.PLAN_SPONSOR+ "PARTICIPANT_CONTRACT WHERE PARTICIPANT_ID=?";

	private static final String UNCASHED_CHECKS_VALUE = "SELECT COUNT(*) AS UC_CHECK_COUNT, SUM(CHQAMT) AS UC_CHECKS_VALUE" +
			" FROM "+ DaoConstants.SchemaName.APOLLO+ ".VLP1417 A " +
			" WHERE A.CNNO = ? AND A.CHQSTUS IN ('SD','OS')	";

	private static final String STALEDATED_CHECKS_VALUE = "SELECT COUNT(*) AS SD_CHECK_COUNT,SUM(CHQAMT) AS SD_CHECKS_VALUE" +
			" FROM "+ DaoConstants.SchemaName.APOLLO+ ".VLP1417 A " +
			" WHERE A.CNNO = ? AND A.CHQSTUS = 'SD' GROUP BY CHQSTUS";

	private static final String OUTSTANDING_CHECKS_VALUE = "SELECT COUNT(*) AS OS_CHECK_COUNT,SUM(CHQAMT) AS OS_CHECKS_VALUE" +
			" FROM "+ DaoConstants.SchemaName.APOLLO+ ".VLP1417 A " +
			" WHERE A.CNNO = ? AND A.CHQSTUS = 'OS' GROUP BY CHQSTUS";

	private static final String SQL_CYCLE_DATE = "SELECT CYCLEDTE AS CYCLE_DATE FROM "+ DaoConstants.SchemaName.APOLLO+ ".VLP0103 WHERE BUSUNIT = 'UNCHEQUE' ";
	
	private static final String ORDER_BY_LAST_NAME = "CASE WHEN PRTFSTNM IS NULL AND PRTLSTNM IS NULL THEN '_' ELSE PRTLSTNM END";
	
	private static final String ORDER_BY_FIRST_NAME = "CASE WHEN PRTFSTNM IS NULL AND PRTLSTNM IS NULL THEN '_' ELSE PRTFSTNM END";
	
	private static final Map fieldToColumnMap = new HashMap();
	private static UncashedChecksItemTransformer transformer = new UncashedChecksItemTransformer();
	
	private final static Map PayeeTypeMap = new HashMap();
	
	static {					
		PayeeTypeMap.put("BE","Beneficiary");
		PayeeTypeMap.put("CL","Client");
		PayeeTypeMap.put("DB","USFP");
		PayeeTypeMap.put("FI","Financial Institution");
		PayeeTypeMap.put("OT","Other");
		PayeeTypeMap.put("PA","Participant");
		PayeeTypeMap.put("TR","Trustee");
	}
	
	private final static Map TransactionReasonCodeMap = new HashMap();
	
	static {
		TransactionReasonCodeMap.put("DE","Death claim");
		TransactionReasonCodeMap.put("DI","Disability claim");
		TransactionReasonCodeMap.put("EA","Excess annual additions");
		TransactionReasonCodeMap.put("EC","Excess contributions");
		TransactionReasonCodeMap.put("ED","Excess deferrals");
		TransactionReasonCodeMap.put("HA","Hardship");
		TransactionReasonCodeMap.put("IL","In-service withdrawal for loan");
		TransactionReasonCodeMap.put("IO","Other withdrawal");
		TransactionReasonCodeMap.put("IR","Employee rollover withdrawal");
		TransactionReasonCodeMap.put("MD","Minimum distribution");
		TransactionReasonCodeMap.put("MT","Mandatory distribution");
		TransactionReasonCodeMap.put("NE","90 day withdrawal election");
		TransactionReasonCodeMap.put("PD","Pre-retirement withdrawal");
		TransactionReasonCodeMap.put("RA","Retirement, annuity purchase");
		TransactionReasonCodeMap.put("RE","Retirement");
		TransactionReasonCodeMap.put("RO","Retirement, rollover");
		TransactionReasonCodeMap.put("SR","Withdrawal of deposits rec\'d after termination");
		TransactionReasonCodeMap.put("TE","Termination of employment");
		TransactionReasonCodeMap.put("TO","Termination, rollover");
		TransactionReasonCodeMap.put("TP","Termination of participation");
		TransactionReasonCodeMap.put("UE","Unvested earnings withdrawal");
		TransactionReasonCodeMap.put("UM","Unvested money withdrawal");
		TransactionReasonCodeMap.put("VC","Voluntary contributions withdrawal");
		TransactionReasonCodeMap.put("OP","Payment to trustees");
		TransactionReasonCodeMap.put("MC","Payment to trustees of matured guaranteed funds");
		TransactionReasonCodeMap.put("DT","Payment to trustees");
	}

	static {
		/*
		 * Sets up the field to column map.
		 */
		fieldToColumnMap.put(UncashedChecksReportItem.SORT_CHECK_ISSUE_DATE,
				new String[] {CHECK_ISSUE_DATE,
					ORDER_BY_LAST_NAME,
					ORDER_BY_FIRST_NAME});

		fieldToColumnMap.put(UncashedChecksReportItem.SORT_PARTICIPANT_NAME,
				new String[]{ORDER_BY_LAST_NAME,
					ORDER_BY_FIRST_NAME,
					CHECK_ISSUE_DATE});

		fieldToColumnMap.put(UncashedChecksReportItem.SORT_PAYEE_NAME,
				new String[] {PAYEE_NAME,
					CHECK_ISSUE_DATE,
					ORDER_BY_LAST_NAME,
					ORDER_BY_FIRST_NAME });

		fieldToColumnMap.put(UncashedChecksReportItem.SORT_CHECK_AMOUNT,
				new String[] {CHECK_AMOUNT,
					CHECK_ISSUE_DATE,
					ORDER_BY_LAST_NAME,
					ORDER_BY_FIRST_NAME});

		fieldToColumnMap.put(UncashedChecksReportItem.SORT_CHECK_STATUS,
				new String[] {CHECK_STATUS,
					CHECK_ISSUE_DATE,
					ORDER_BY_LAST_NAME,
					ORDER_BY_FIRST_NAME });
	}

	/**
	 * This method is to collect the information for the database for the reports. 
	 * @param criteria
	 * @return
	 * @throws SystemException
	 * @throws ReportServiceException
	 */

	public ReportData getReportData(ReportCriteria criteria)
			throws SystemException, ReportServiceException {
	    
	    class DateUnavailabilityException extends Exception {
	        // basic exception
	    }
	    
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		Connection conn = null;
		PreparedStatement statement = null;
		UncashedChecksReportData reportData = new UncashedChecksReportData(
				criteria, 0);

		try {
			TransactionMonitor.getTransactionMonitor().startTransaction(
					Thread.currentThread().getName(),
					"UncashedChecksReportDAO.getReportData()");

			conn = getReadUncommittedConnection(className,
					APOLLO_DATA_SOURCE_NAME);

			PreparedStatement stmt1 = conn.prepareStatement(SQL_CYCLE_DATE);
			ResultSet rs1 = stmt1.executeQuery();
			if(rs1.next()) {
				if(!isValidAsOfDate(rs1.getDate(CYCLE_DATE))) {
					throw new DateUnavailabilityException();
				}
				reportData.setAsOfDate(rs1.getDate(CYCLE_DATE));
			}
			
			populateUncashedCheckDetails(conn, criteria, reportData);

			stmt1 = conn
					.prepareStatement(UNCASHED_CHECKS_VALUE);
			stmt1.setInt(1, ((Integer) criteria
					.getFilterValue(CONTRACT_NUMBER)).intValue());
			rs1 = stmt1.executeQuery();
			while (rs1.next()) {
				
				reportData.setTotalCount(rs1.getInt("UC_CHECK_COUNT"));
				reportData.setUncashedChecksValue(rs1
						.getBigDecimal("UC_CHECKS_VALUE"));
				
			}

			stmt1 = conn.prepareStatement(STALEDATED_CHECKS_VALUE);
			stmt1.setInt(1, ((Integer) criteria
					.getFilterValue(CONTRACT_NUMBER)).intValue());
			rs1 = stmt1.executeQuery();
			while (rs1.next()) {
				
				reportData.setNumStaleDatedChecks(rs1.getInt("SD_CHECK_COUNT"));
				reportData.setStaleDatedChecksValue(rs1
						.getBigDecimal("SD_CHECKS_VALUE"));
			}

			stmt1 = conn.prepareStatement(OUTSTANDING_CHECKS_VALUE);
			stmt1.setInt(1, ((Integer) criteria
					.getFilterValue(CONTRACT_NUMBER)).intValue());

			rs1 = stmt1.executeQuery();
			while (rs1.next()) {
				
				reportData.setNumOutstandingChecks(rs1
						.getInt("OS_CHECK_COUNT"));
				reportData.setOutstandingChecksValue(rs1
						.getBigDecimal("OS_CHECKS_VALUE"));
			}

			stmt1 = conn.prepareStatement(SQL_CYCLE_DATE);
			rs1 = stmt1.executeQuery();
			if(rs1.next()) {
				if(!isValidAsOfDate(rs1.getDate(CYCLE_DATE)) || 
						!rs1.getDate(CYCLE_DATE).equals(reportData.getAsOfDate())) {
					throw new DateUnavailabilityException();
				}
			}
			
			if (reportData.getOutstandingChecksValue() == null) {
				reportData.setOutstandingChecksValue(new BigDecimal(0));
			}
			if (reportData.getStaleDatedChecksValue() == null) {
				reportData.setStaleDatedChecksValue(new BigDecimal(0));
			}
			if (reportData.getUncashedChecksValue() == null) {
				reportData.setUncashedChecksValue(new BigDecimal(0));
			}
			
		} catch (DateUnavailabilityException due) {
		    
		    reportData = new UncashedChecksReportData(criteria, 0);
		    reportData.setAsOfDate(null);
		    
		} catch (SQLException sqle) {
			
//		    throw new UncashedChecksDatabaseAccessException(
//		            sqle,
//		            className,
//		            "getReportData",
//		            criteria.getFilterValue(CONTRACT_NUMBER).toString());
			
			// PPM: 351417 , Feb 2017, modified: dastgme
			 reportData = new UncashedChecksReportData(criteria, 0);
			 reportData.setAsOfDate(null);
			 
			  logger
	             .error("Problem occurred during Apollo Connection SQLException:"
	                     + criteria + ":" + sqle);
			
		} finally {
			TransactionMonitor.getTransactionMonitor().finishTransaction(
					Thread.currentThread().getName());
			close(statement, conn);
		}
		
		return reportData;
		
	}
	
	/**
	 * Return a standard sort phrase using the given field to column map.
	 * 
	 * @param fieldToColumnMap
	 *            The map where key is the sort field and the value is array of
	 *            ReportSort objects defining sort sequence.
	 * @return A String in the form "sortField1 sortDirection1, sortField2
	 *         sortDirection2,..."
	 */
	public String getSortPhrase(ReportCriteria criteria, Map fieldToColumnMap) {
			StringBuffer result = new StringBuffer();
			Iterator it = criteria.getSorts().iterator();
			while (it.hasNext()) {
				ReportSort sort = (ReportSort) it.next();
				Object fieldsRaw;
				if (fieldToColumnMap != null) {
					fieldsRaw = fieldToColumnMap.get(sort.getSortField());
				} else {
					fieldsRaw = sort.getSortField();
				}
				Object[] fields =
					(fieldsRaw.getClass().isArray()
						? (Object[]) fieldsRaw
						: new Object[] { fieldsRaw });
				for (int i = 0; i < fields.length; i++) {

				result.append(fields[i].toString()).append(" ");
				if (sort.getSortField().equals(UncashedChecksReportItem.SORT_CHECK_AMOUNT)
						&& (fields[i].toString().equals(CHECK_ISSUE_DATE)
								|| fields[i].toString().equals(ORDER_BY_LAST_NAME)
								|| fields[i].toString().equals(ORDER_BY_FIRST_NAME))) {
					if (sort.getSortDirection().equals(ReportSort.ASC_DIRECTION)) {
						result.append(ReportSort.DESC_DIRECTION);
					} else {
						result.append(ReportSort.ASC_DIRECTION);
					}
				} else if (sort.getSortField().equals(UncashedChecksReportItem.SORT_CHECK_STATUS)
						&& fields[i].toString().equals(CHECK_STATUS)) {
					if (sort.getSortDirection().equals(ReportSort.ASC_DIRECTION)) {
						result.append(ReportSort.DESC_DIRECTION);
					} else {
						result.append(ReportSort.ASC_DIRECTION);
					}
				} else {
					result.append(sort.getSortDirection());
				}
				if (i < fields.length - 1)
					result.append(", ");
			}

				if (it.hasNext()) {
					result.append(", ");
				}
			}
			return result.toString();
		}


	/**
	 * This method is to populate the Uncashed check details to fetch the
	 * records from the table and put into UncashedChecksReportData.
	 * 
	 * @param connection
	 * @param criteria
	 * @param reportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 * @throws SQLException
	 */
	private void populateUncashedCheckDetails(Connection apolloConnection,
			ReportCriteria criteria, UncashedChecksReportData reportData)
			throws SystemException, ReportServiceException, SQLException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateUncashedCheckDetails");
			if (DEBUG_SQL)
				logger.debug(GET_UNCASHED_CHECK_INFORMATION);
		}

		String query = GET_UNCASHED_CHECK_INFORMATION;
		PreparedStatement stmt = null;
		String sortCriteria = getSortPhrase(criteria, fieldToColumnMap);
		/*
		 * Appends the ORDER BY clause when there is any sorting requirement.
		 */
		if (criteria.getSorts().size() > 0) {
			query = new StringBuffer(query).append(" ORDER BY ").append(
					sortCriteria).toString();
		}
		try {
			stmt = apolloConnection.prepareStatement(query);
			stmt.setInt(1,
					((Integer) criteria.getFilterValue(CONTRACT_NUMBER))
							.intValue());
			ResultSet rs = stmt.executeQuery();
			List items = DirectSqlReportDAOHelper.getReportItems(criteria, rs,
					transformer);
					
			reportData.setDetails(items);

		} finally {
			close(stmt, null);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateUncashedCheckDetails");
		}
	}

	/**
	 * This inner class transforms a result set into a UncashedChecksReportItem
	 * object.
	 */
	private static class UncashedChecksItemTransformer extends
			ReportItemTransformer {

		public Object transform(ResultSet rs) throws SQLException {
			Connection conn = null;
			PreparedStatement stmt = null;
			
			UncashedChecksReportItem item = new UncashedChecksReportItem();
			try {

				item.setCheckIssueDate(rs.getDate(CHECK_ISSUE_DATE));
				item
						.setParticipantFirstName((rs.getString(FIRST_NAME) != null) ? rs
								.getString(FIRST_NAME).trim()
								: null);
				item
						.setParticipantLastName((rs.getString(LAST_NAME) != null) ? rs
								.getString(LAST_NAME).trim()
								: null);

				String payeeType = rs.getString(PAYEE_TYPE);
				String payeeName = rs.getString(PAYEE_NAME);

				item.setCheckAmount(rs.getBigDecimal(CHECK_AMOUNT));

				java.sql.Date transactionDate = rs.getDate(TRANSACTION_DATE);
				if (transactionDate != null) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy/MM/dd");
						java.sql.Date date = new java.sql.Date(sdf.parse(
								"9999/12/31").getTime());
						if (transactionDate.equals(date)) {
							transactionDate = null;
						}
					} catch (ParseException exception) {
						if (logger.isDebugEnabled()) {
							logger.debug(exception);
						}
					}
				}
				item.setTransactionDate(transactionDate);

				String transactionType = rs.getString(TRANSACTION_TYPE);
				String transactionNumber = rs.getString(TRANSACTION_NUMBER);
				if (transactionNumber != null && transactionNumber.trim().equals("0")) {
					transactionNumber = "";
				}
				item.setTransactionNumber(transactionNumber);

				item.setSsn((rs.getString(SOCIAL_SECURITY_NUMBER) != null) ? rs
						.getString(SOCIAL_SECURITY_NUMBER).trim() : null);
				String checkStatus = rs.getString(CHECK_STATUS);

				String participantID = rs.getString("PARTICIPANT_ID");
				conn = getReadUncommittedConnection(className,
						CUSTOMER_DATA_SOURCE_NAME);
				stmt = conn.prepareStatement(GET_PROFILE_ID);
				stmt.setString(1, participantID);
				ResultSet resultSet = stmt.executeQuery();
				if (resultSet.next()) {
					item.setProfileId(resultSet.getString(PROFILE_ID));
				}

				if (payeeType != null) {
					item.setPayeeType((String)PayeeTypeMap.get(payeeType.trim()));
				}
				if (payeeName != null) {
					if (payeeName.trim().length() > 40) {
						payeeName = payeeName.trim().substring(0, 40);
					}
					item.setPayeeName(payeeName);
				}
				String participantLastName = item.getParticipantLastName();
				String participantFirstName = item.getParticipantFirstName();
				if ((participantLastName != null && !""
						.equals(participantLastName))
						&& (participantFirstName != null && !""
								.equals(participantFirstName))) {
					item.setParticipantName(item.getParticipantLastName()
							+ ((participantFirstName != "") ? ", "
									+ participantFirstName : ""));
				} else if (participantLastName != null
						&& !"".equals(participantLastName)) {
					item.setParticipantName(item.getParticipantLastName());
				} else if (participantFirstName != null
						&& !"".equals(participantFirstName)) {
					item.setParticipantName(participantFirstName);
				} else {
					item.setParticipantName(null);
				}

				String transactionReasonCode = rs
						.getString(TRANSACTION_REASON_CODE);
				if (transactionType != null) {
					if ("LI".equals(transactionType)) {
						item.setTransactionType("Loan issue");
					} else if ("DI".equals(transactionType)) {
						item.setTransactionType("Discontinuance");
					} else if ("WD".equals(transactionType)) {
							item.setTransactionType((String)TransactionReasonCodeMap.get(transactionReasonCode));
							if(item.getTransactionType() == null || item.getTransactionType().equals("")) {
								item.setTransactionType("Withdrawal");
							}
					} else if ("CR".equals(transactionType)) {
						item.setTransactionType((String)TransactionReasonCodeMap.get(transactionReasonCode));
					}
				}

				if ("SD".equals(checkStatus)) {
					item.setCheckStatus("Stale dated");
				} else if ("OS".equals(checkStatus)) {
					item.setCheckStatus("Outstanding");
				}
			} catch (SystemException e) {
				throw new SQLException();
			} finally {
				close(stmt, conn);
			}

			return item;
		}
	}

	/**
	 * This method is to validate the asOfDate. This method will return false if
	 * the asOfDate from the table is null or 12/31/9999.
	 * 
	 * @param asOfDate
	 * @return
	 */
	private static boolean isValidAsOfDate(Date asOfDate) {
		try {
			if (asOfDate == null) {
				return false;
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				java.sql.Date date = new java.sql.Date(sdf.parse("9999/12/31")
						.getTime());
				if (date.equals(asOfDate)) {
					return false;
				}
			}
		} catch (ParseException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e);
			}
			return false;
		}
		return true;
	}
}
