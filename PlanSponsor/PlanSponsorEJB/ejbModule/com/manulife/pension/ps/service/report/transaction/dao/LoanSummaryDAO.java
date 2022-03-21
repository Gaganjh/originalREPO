package com.manulife.pension.ps.service.report.transaction.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanSummaryItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanSummaryReportData;
import com.manulife.pension.service.environment.EnvironmentServiceHelper;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.util.DateComparator;

/**
 * @author grouzan
 *  
 */
public class LoanSummaryDAO extends ReportServiceBaseDAO {
	private static final Logger logger = Logger.getLogger(LoanSummaryDAO.class);

	private static final String className = LoanSummaryDAO.class.getName();

	private static final Date DUMMY_DATE = new GregorianCalendar(1,
			Calendar.JANUARY, 1).getTime();

	private static final String ENV_LOAN_SUMMARY_DAYS_THRESHOLD = "java:comp/env/loanSummaryDaysThreshold";

	private static final String GET_CURRENT_LOAN_SUMMARY = "call "
			+ CUSTOMER_SCHEMA_NAME + "GET_CURRENT_LOAN_SUMMARY(?,?,?,?,?,?)";

	final static String LAST_NAME = "LAST_NAME";

	final static String FIRST_NAME = "FIRST_NAME";

	final static String SOCIAL_SECURITY_NO = "SOCIAL_SECURITY_NO";

	final static String LOAN_NUMBER = "LOAN_ID";

	final static String OUTSTANDING_BALANCE = "OUTSTANDING_BALANCE";

	final static String LAST_REPAYMENT_AMT = "LAST_REPAYMENT_AMOUNT";

	final static String LAST_REPAYMENT_DATE = "LAST_REPAYMENT_DATE";

	final static String LAST_REPAYMENT_TRANSACTION_NO = "LAST_REPAYMENT_TRANSACTION_NO";

	final static String MATURITY_DATE = "MATURITY_DATE";

	final static String EFFECTIVE_DATE = "EFFECTIVE_DATE";
	final static String CREATION_DATE = "LOAN_CREATE_DATE";

	final static String PROFILE_ID = "PROFILE_ID";

	final static String OUTSTANDING_LOANS = "OUTSTANDING_LOANS";

	final static String NUM_OF_LOANS = "NUM_OF_LOANS";

	final static String NUM_OF_PARTICIPANTS = "NUM_OF_PARTICIPANTS";

	final static int REPAYMENT_OVERDUE = 0;

	final static int PAST_MATURITY = 1;

	final static int APPROACHING_MATURITY = 2;
	
	final static String INTEREST_RATE = "LOAN_INTEREST_RATE_PCT";
	
	final static String ORIGINAL_LOAN_AMT = "LOAN_PRINCIPAL_AMT";
	
	final static String ALERT = "ALERT";
	
	final static String PARTICIPANT_ID = "PARTICIPANT_ID";
	

	//TODO: do we hardcode these here ?
	static String[] alertLabels = new String[] { "Repayment is overdue",
			"Past maturity", "Approaching maturity" };

	public static final int DEFAULT_THRESHOLD = 45;

	private String sortDirection = ReportSort.ASC_DIRECTION;

	/**
	 * The fields to compare when comparing two dates.
	 */
	private static final int[] DATE_COMPARISON_FIELDS = new int[] {
			Calendar.YEAR, Calendar.MONTH, Calendar.DATE };

	public static int lookupDaysThreshold() throws SystemException {
		try {
			Context ctx = new InitialContext();
			Integer days = (Integer) ctx
					.lookup(ENV_LOAN_SUMMARY_DAYS_THRESHOLD);
			if (days != null) {
				return days.intValue();
			}
		} catch (NamingException e) {
			throw new SystemException(e, className, "LoanSummaryDAO", "Name ["
					+ ENV_LOAN_SUMMARY_DAYS_THRESHOLD + "] not found.");
		}

		return DEFAULT_THRESHOLD;
	}

	/**
	 * @param criteria
	 * @return RepoertData
	 * @throws SystemException
	 */
	public ReportData getReportData(ReportCriteria criteria)
			throws SystemException, ReportServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		Integer contractNumber = (Integer) criteria
				.getFilterValue(LoanSummaryReportData.FILTER_CONTRACT_NO);

		String sortCriteria = null;
		boolean sortAlerts = isSortingByAlerts(criteria);
		if (!sortAlerts) {
			sortCriteria = this.getSortPhrase(criteria, fieldToColumnMap);
		}
		
		LoanSummaryReportData reportData = new LoanSummaryReportData(criteria,
				-1);

		Connection conn = null;
		CallableStatement statement = null;
		try {
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			statement = conn.prepareCall(GET_CURRENT_LOAN_SUMMARY);

			getData(statement, contractNumber, sortCriteria, criteria,
					sortAlerts, criteria.getStartIndex(), criteria
							.getPageSizeAsBigDecimal(), reportData);
		} catch (SQLException e) {
			handleSqlException(
					e,
					this.getClass().getName(),
					"getReportData",
					"Problem when getting report data; "
							+ "Criteria:" + criteria);
		} finally {
			close(statement, conn);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getReportData");
		}
		return reportData;
	}

	/**
	 * @param conn
	 * @param contractNumber
	 * @param sortCriteria
	 * @param criteria
	 * @param sortAlerts
	 * @param startIndex
	 * @param pageSize
	 * @param reportData
	 * @throws SQLException
	 */
	public void getData(CallableStatement stmt, Integer contractNumber,
			String sortCriteria, ReportCriteria criteria, boolean sortAlerts,
			int startIndex, BigDecimal pageSize,
			LoanSummaryReportData reportData) throws SQLException,
			SystemException {

		stmt.setInt(1, contractNumber.intValue()); //contractNumber
		// "51690"
		if (sortCriteria == null || sortCriteria.length() == 0) {
			stmt.setNull(2, Types.VARCHAR);
		} else {
			stmt.setString(2, sortCriteria);
		}

		// If we sort by alert text, we have to get the whole set of data
		if (sortAlerts) {
			// Page size
			stmt.setBigDecimal(3, null);
			// Start index
			stmt.setInt(4, 1);
		} else {
			// Page size
			stmt.setBigDecimal(3, pageSize);
			// Start index
			stmt.setInt(4, startIndex);
		}

		stmt.registerOutParameter(5, java.sql.Types.DATE);
		stmt.registerOutParameter(6, java.sql.Types.DECIMAL);

		List items = null;
		ResultSet rsSummary = null;
		ResultSet rsDetails = null;

		stmt.execute();

		//asOfDate
		reportData.setAsOfDate(stmt.getDate(5));

		rsSummary = stmt.getResultSet();
		
		if (rsSummary != null) {
		    
    		if (rsSummary.next()) {
    			reportData.setOutstandingBalance(rsSummary
    					.getBigDecimal(OUTSTANDING_LOANS));
    			if (reportData.getOutstandingBalance() == null)
    				reportData.setOutstandingBalance(new BigDecimal("0"));
    			reportData.setNumLoans(rsSummary.getInt(NUM_OF_LOANS));
    			reportData
    					.setNumParticipants(rsSummary.getInt(NUM_OF_PARTICIPANTS));
    
    			reportData.setTotalCount(reportData.getNumLoans());
    		}
    		
		}
		
		stmt.getMoreResults();
		rsDetails = stmt.getResultSet();

		items = new ArrayList();
		
		if (rsDetails != null) {
		    
    		while (rsDetails.next()) {
    			LoanSummaryItem item = new LoanSummaryItem(rsDetails.getString(
    					FIRST_NAME).trim(), rsDetails.getString(LAST_NAME).trim(),
    					rsDetails.getString(SOCIAL_SECURITY_NO), rsDetails
    							.getInt(LOAN_NUMBER), rsDetails
    							.getBigDecimal(OUTSTANDING_BALANCE), rsDetails
    							.getBigDecimal(LAST_REPAYMENT_AMT), rsDetails
    							.getDate(LAST_REPAYMENT_DATE), rsDetails
    							.getBigDecimal(LAST_REPAYMENT_TRANSACTION_NO),
    					rsDetails.getDate(MATURITY_DATE), rsDetails
    							.getDate(EFFECTIVE_DATE),
    							rsDetails.getDate(CREATION_DATE),
    							rsDetails
    							.getString(PROFILE_ID), rsDetails
                                .getBigDecimal(INTEREST_RATE), rsDetails.getBigDecimal(ORIGINAL_LOAN_AMT),
                                rsDetails.getBigDecimal(PARTICIPANT_ID));
    
    			if (item.getLastRepaymentAmt() == null) {
    				item.setLastRepaymentAmt(new BigDecimal(0.00));
    			}
    			items.add(item);
    		}
    		
		}
		
		checkAlerts(items);

		//see if we have to sort by alerts
		if (sortAlerts) {
			sortDirection = getSortAlertsDirection(criteria);
			Collections.sort(items, new LoanSummaryAlertComparator(
					sortDirection));
			
			/*
			 * Handles paging properly.
			 */
			if (criteria.getPageSize() != ReportCriteria.NOLIMIT_PAGE_SIZE
					&& items.size() > criteria.getPageSize()) {
				// getStartIndex() returns an index that is 1-based.
				startIndex = criteria.getStartIndex() - 1;
				int endIndex = startIndex + criteria.getPageSize();
				if (endIndex > items.size()) {
					endIndex = items.size();
				}
				// We need to contruct an ArrayList because Java's sublist
				// is not serializable.
				List subList = items.subList(startIndex, endIndex);
				items = new ArrayList();
				items.addAll(subList);
			}
		}

		reportData.setDetails(items);
	}

	private static final Map fieldToColumnMap = new HashMap();

	//this map contains definitions for ASC direction first and then for DESC
	//default constructor for ReportSort assumes ASC_DIRECTION by default
	static {
		fieldToColumnMap.put(LoanSummaryItem.SORT_NAME,
				new ReportSort[][] {
						new ReportSort[] { new ReportSort(LAST_NAME),
								new ReportSort(FIRST_NAME),
								new ReportSort(SOCIAL_SECURITY_NO),
								new ReportSort(LOAN_NUMBER) },
						new ReportSort[] {
								new ReportSort(LAST_NAME,
										ReportSort.DESC_DIRECTION),
								new ReportSort(FIRST_NAME,
										ReportSort.DESC_DIRECTION),
								new ReportSort(SOCIAL_SECURITY_NO,
										ReportSort.DESC_DIRECTION),
								new ReportSort(LOAN_NUMBER,
										ReportSort.DESC_DIRECTION) } });

		fieldToColumnMap.put(LoanSummaryItem.SORT_LOAN_NUMBER,
				new ReportSort[][] {
						new ReportSort[] {
								new ReportSort(LOAN_NUMBER),
								new ReportSort(LAST_NAME,
										ReportSort.DESC_DIRECTION),
								new ReportSort(FIRST_NAME,
										ReportSort.DESC_DIRECTION),
								new ReportSort(SOCIAL_SECURITY_NO,
										ReportSort.DESC_DIRECTION) },
						new ReportSort[] {
								new ReportSort(LOAN_NUMBER,
										ReportSort.DESC_DIRECTION),
								new ReportSort(LAST_NAME),
								new ReportSort(FIRST_NAME),
								new ReportSort(SOCIAL_SECURITY_NO) } });

		fieldToColumnMap.put(LoanSummaryItem.SORT_OUTSTANDING_BALANCE,
				new ReportSort[][] {
						new ReportSort[] {
								new ReportSort(OUTSTANDING_BALANCE),
								new ReportSort(LAST_NAME,
										ReportSort.DESC_DIRECTION),
								new ReportSort(FIRST_NAME,
										ReportSort.DESC_DIRECTION) },
						new ReportSort[] {
								new ReportSort(OUTSTANDING_BALANCE,
										ReportSort.DESC_DIRECTION),
								new ReportSort(LAST_NAME),
								new ReportSort(FIRST_NAME) } });

		fieldToColumnMap.put(LoanSummaryItem.SORT_LAST_REPAYMENT_DATE,
				new ReportSort[][] {
						new ReportSort[] {
								new ReportSort(LAST_REPAYMENT_DATE),
								new ReportSort(LAST_NAME,
										ReportSort.DESC_DIRECTION),
								new ReportSort(FIRST_NAME,
										ReportSort.DESC_DIRECTION),
								new ReportSort(SOCIAL_SECURITY_NO,
										ReportSort.DESC_DIRECTION),
								new ReportSort(LOAN_NUMBER,
										ReportSort.DESC_DIRECTION) },
						new ReportSort[] {
								new ReportSort(LAST_REPAYMENT_DATE,
										ReportSort.DESC_DIRECTION),
								new ReportSort(LAST_NAME),
								new ReportSort(FIRST_NAME),
								new ReportSort(SOCIAL_SECURITY_NO),
								new ReportSort(LOAN_NUMBER) } });

		fieldToColumnMap.put(LoanSummaryItem.SORT_LAST_REPAYMENT_AMT,
				new ReportSort[][] {
						new ReportSort[] {
								new ReportSort(LAST_REPAYMENT_AMT),
								new ReportSort(LAST_NAME,
										ReportSort.DESC_DIRECTION),
								new ReportSort(FIRST_NAME,
										ReportSort.DESC_DIRECTION) },
						new ReportSort[] {
								new ReportSort(LAST_REPAYMENT_AMT,
										ReportSort.DESC_DIRECTION),
								new ReportSort(LAST_NAME),
								new ReportSort(FIRST_NAME) } });
		
		fieldToColumnMap.put(LoanSummaryItem.SORT_ISSUE_DATE,
                new ReportSort[][] {
                        new ReportSort[] {
                                new ReportSort(CREATION_DATE),
                                new ReportSort(LAST_NAME,
                                        ReportSort.DESC_DIRECTION),
                                new ReportSort(FIRST_NAME,
                                        ReportSort.DESC_DIRECTION) ,
                                new ReportSort(SOCIAL_SECURITY_NO,
        						    	ReportSort.DESC_DIRECTION), 
        						new ReportSort(LOAN_NUMBER,
        								ReportSort.DESC_DIRECTION)
                        								},
                        new ReportSort[] {
                                new ReportSort(CREATION_DATE,
                                        ReportSort.DESC_DIRECTION),
                                new ReportSort(LAST_NAME),
                                new ReportSort(FIRST_NAME),
                                new ReportSort(SOCIAL_SECURITY_NO),
                                new ReportSort(LOAN_NUMBER)} });
		
		fieldToColumnMap.put(LoanSummaryItem.SORT_INTEREST_RATE,
                new ReportSort[][] {
                        new ReportSort[] {
                                new ReportSort(INTEREST_RATE),
                                new ReportSort(LAST_NAME,
                                        ReportSort.DESC_DIRECTION),
                                new ReportSort(FIRST_NAME,
                                        ReportSort.DESC_DIRECTION),
                                new ReportSort(SOCIAL_SECURITY_NO,
        						    	ReportSort.DESC_DIRECTION), 
        						new ReportSort(LOAN_NUMBER,
        								ReportSort.DESC_DIRECTION)},
                        new ReportSort[] {
                                new ReportSort(INTEREST_RATE,
                                        ReportSort.DESC_DIRECTION),
                                new ReportSort(LAST_NAME),
                                new ReportSort(FIRST_NAME),
                                new ReportSort(SOCIAL_SECURITY_NO),
                                new ReportSort(LOAN_NUMBER)} });
		
		fieldToColumnMap.put(LoanSummaryItem.SORT_MATURITY_DATE,
                new ReportSort[][] {
                        new ReportSort[] {
                                new ReportSort(MATURITY_DATE),
                                new ReportSort(LAST_NAME,
                                        ReportSort.DESC_DIRECTION),
                                new ReportSort(FIRST_NAME,
                                        ReportSort.DESC_DIRECTION),
                                new ReportSort(SOCIAL_SECURITY_NO,
        						    	ReportSort.DESC_DIRECTION), 
        						new ReportSort(LOAN_NUMBER,
        								ReportSort.DESC_DIRECTION)},
                        new ReportSort[] {
                                new ReportSort(MATURITY_DATE,
                                        ReportSort.DESC_DIRECTION),
                                new ReportSort(LAST_NAME),
                                new ReportSort(FIRST_NAME),
                                new ReportSort(SOCIAL_SECURITY_NO),
                                new ReportSort(LOAN_NUMBER)} });
		
		fieldToColumnMap.put(LoanSummaryItem.SORT_ORIGINAL_LOAN_AMT,
                new ReportSort[][] {
                        new ReportSort[] {
                                new ReportSort(ORIGINAL_LOAN_AMT),
                                new ReportSort(LAST_NAME,
                                        ReportSort.DESC_DIRECTION),
                                new ReportSort(FIRST_NAME,
                                        ReportSort.DESC_DIRECTION),
                                new ReportSort(SOCIAL_SECURITY_NO,
        						    	ReportSort.DESC_DIRECTION), 
        						new ReportSort(LOAN_NUMBER,
        								ReportSort.DESC_DIRECTION)},
                        new ReportSort[] {
                                new ReportSort(ORIGINAL_LOAN_AMT,
                                        ReportSort.DESC_DIRECTION),
                                new ReportSort(LAST_NAME),
                                new ReportSort(FIRST_NAME),
                                new ReportSort(SOCIAL_SECURITY_NO),
                                new ReportSort(LOAN_NUMBER)} });
		
		fieldToColumnMap.put(LoanSummaryItem.SORT_ALERT,
                new ReportSort[][] {
                        new ReportSort[] {
                                new ReportSort(ALERT),
                                new ReportSort(LAST_NAME,
                                        ReportSort.DESC_DIRECTION),
                                new ReportSort(FIRST_NAME,
                                        ReportSort.DESC_DIRECTION) },
                        new ReportSort[] {
                                new ReportSort(ALERT,
                                        ReportSort.DESC_DIRECTION),
                                new ReportSort(LAST_NAME),
                                new ReportSort(FIRST_NAME) } });
		
	}

	/**
	 * @param sort
	 * @return
	 */
	private static int toIndex(ReportSort sort) {
		if (sort.isAscending())
			return 0;
		else
			return 1;
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
			ReportSort[][] sortDefs = (ReportSort[][]) fieldToColumnMap
					.get(sort.getSortField());

			ReportSort[] fields;
			if (sortDefs != null && sortDefs.length > 1) {
				fields = sortDefs[toIndex(sort)];
			} else {
				fields = new ReportSort[] { sort };
			}

			for (int i = 0; i < fields.length; i++) {
				result.append(fields[i].getSortField()).append(" ");
				result.append(fields[i].getSortDirection());
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
	 * @param criteria
	 * @return
	 */
	protected static boolean isSortingByAlerts(ReportCriteria criteria) {
		Iterator sorts = criteria.getSorts().iterator();
		while (sorts.hasNext()) {
			ReportSort sort = (ReportSort) sorts.next();
			if (sort.getSortField().equals(LoanSummaryItem.SORT_ALERT))
				return true;
		}

		return false;
	}

	/**
	 * @param criteria
	 * @return
	 */
	protected static String getSortAlertsDirection(ReportCriteria criteria) {
		Iterator sorts = criteria.getSorts().iterator();
		while (sorts.hasNext()) {
			ReportSort sort = (ReportSort) sorts.next();
			if (sort.getSortField().equals(LoanSummaryItem.SORT_ALERT))
				return sort.getSortDirection();
		}

		return ReportSort.ASC_DIRECTION;
	}

	/**
	 * @param items
	 */
	protected static void checkAlerts(List items) throws SystemException {

		int overdueDaysThreshold = lookupDaysThreshold();

		//do alerts
		if (items != null) {

			//set today to asofdate
			Calendar today = Calendar.getInstance();
			today.setTime(EnvironmentServiceHelper.getInstance().getAsOfDate());

			Calendar threeMonthsAfterToday = Calendar.getInstance();
			threeMonthsAfterToday.setTime(today.getTime());
			threeMonthsAfterToday.add(Calendar.MONTH, 3);

			if (logger.isDebugEnabled()) {
				logger.debug("Today is set to [" + today.getTime() + "]");
			}
			
			for (Iterator it = items.iterator(); it.hasNext();) {
				LoanSummaryItem item = (LoanSummaryItem) it.next();

				if (logger.isDebugEnabled()) {
					logger.debug("Checking [" + item.getName() + "] ["
							+ item.getProfileId() + "]");
				}
				
				//overdue alert
				Calendar cal = Calendar.getInstance();
				if (item.getLastRepaymentDate() != null
						&& item.getLastRepaymentDate().compareTo(DUMMY_DATE) != 0) {
					cal.setTime(item.getLastRepaymentDate());
					if (logger.isDebugEnabled()) {
						logger.debug("Using last repayment date ["
								+ cal.getTime() + "]");
					}
				} else {
					//no repayments made
					cal.setTime(item.getCreationDate());
					if (logger.isDebugEnabled()) {
						logger.debug("Using creation date ["
								+ cal.getTime() + "]");
					}
				}

				ArrayList alerts = new ArrayList();
				cal.add(Calendar.DAY_OF_MONTH, overdueDaysThreshold);

				if (logger.isDebugEnabled()) {
					logger.debug("After [" + overdueDaysThreshold
							+ "] days are added, it's now [" + cal.getTime()
							+ "]");
				}

				if (DateComparator.compare(cal.getTime(), today.getTime(),
						DATE_COMPARISON_FIELDS) <= 0) {
					//set alert
					if (logger.isDebugEnabled()) {
						logger.debug("Alert added for repayment overdue");
					}
					alerts.add(alertLabels[REPAYMENT_OVERDUE]);
				}

				//now we have 2 alerts related to maturity
				if (item.getMaturityDate() != null) {
					cal.clear();
					cal.setTime(item.getMaturityDate());

					if (logger.isDebugEnabled()) {
						logger.debug("Using maturity date [" + cal.getTime()
								+ "]");
					}
					
					if (cal.before(today)) {
						//it is past maturity, check for non-zero balance
						if (item.getOutstandingBalance().doubleValue() > 0) {
							//set alert
							if (logger.isDebugEnabled()) {
								logger.debug("Alert added for past maturity");
							}
							alerts.add(alertLabels[PAST_MATURITY]);
						}
					} else {
						//check for approaching
						if (threeMonthsAfterToday.after(cal)) {
							//set alert
							if (logger.isDebugEnabled()) {
								logger.debug("Alert added for "
										+ "approaching maturity");
							}
							alerts.add(alertLabels[APPROACHING_MATURITY]);
						}
					}
				}

				item.setAlerts((String[]) alerts.toArray(new String[0]));
			}
		}

	}
	
	private static class LoanSummaryAlertComparator implements Comparator {

		private boolean sortAsc = false;

		public LoanSummaryAlertComparator(String sortDirection) {
			sortAsc = sortDirection.equals(ReportSort.ASC_DIRECTION);
		}
		
		public int compare(Object obj1, Object obj2) {
			LoanSummaryItem item1 = (LoanSummaryItem) obj1;
			LoanSummaryItem item2 = (LoanSummaryItem) obj2;

			if (sortAsc) {
				return doCompare((LoanSummaryItem) obj1, (LoanSummaryItem) obj2);
			} else {
				return doCompare((LoanSummaryItem) obj2, (LoanSummaryItem) obj1);
			}
		}

		int doCompare(LoanSummaryItem item1, LoanSummaryItem item2) {
			String[] alerts1 = item1.getAlerts();
			String[] alerts2 = item2.getAlerts();
			
			int result;
			for (int i = 0; i < alerts1.length && i < alerts2.length; i++) {
				result = alerts1[i].compareTo(alerts2[i]);
				if (result != 0)
					return result;
			}

			//we inverse here, so that item with multiple alerts would
			// show at the top
			//if ascending
			if (alerts1.length == alerts2.length) {
				result = item1.getLastName().compareTo(item2.getLastName());
				if (result != 0)
					return result;

				return item1.getFirstName().compareTo(item2.getFirstName());
			} else if (alerts1.length > alerts2.length) {
				return 1;
			} else {
				return -1;
			}
		}
	}
}
