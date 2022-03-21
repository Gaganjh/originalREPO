package com.manulife.pension.ps.service.report.tpadownload.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.tpadownload.valueobject.TedCurrentFilesItem;
import com.manulife.pension.ps.service.report.tpadownload.valueobject.TedCurrentFilesReportData;
import com.manulife.pension.service.contract.report.dao.SelectContractDAO;
import com.manulife.pension.service.contract.report.valueobject.SelectContract;
import com.manulife.pension.service.contract.report.valueobject.SelectContractReportData;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.utility.ConversionUtility;
import com.manulife.pension.util.StaticHelperClass;

public class TedCurrentFilesDAO extends ReportServiceBaseDAO {

	private static final String className = TedCurrentFilesDAO.class.getName();

	private static final Logger logger = Logger
			.getLogger(TedCurrentFilesDAO.class);

	private static final String STOP_QUERY = "stop";

	private static final String TPA_ID = "a.THIRD_PARTY_ADMIN_ID";

	private static final String CONTRACT_NAME = "a.CONTRACT_NAME";

	private static final String CONTRACT_NUMBER = "a.CONTRACT_ID";

	private static final String QUARTER_END_DATE = "a.PERIOD_END_DATE";

	private static final String CORRECTION_INDICATOR = "a.CORRECTION_IND";

	private static final String LAST_RUN_DATE = "a.LAST_RUN_DATE";

	private static final String FILE_SIZE = "a.FILE_SIZE";

	private static final String DOWNLOAD_STATUS = "a.DOWNLOAD_IND";

	private static final String DOWNLOAD_IND_DESC = "b.DOWNLOAD_IND_DESC";

	private static final String DOWNLOAD_IND_SORT_ORDER = "b.SORT_ORDER";

	private static final String LAST_DOWNLOAD_DATE = "a.LAST_DOWNLOAD_DATE";

	private static final String MANUMERGE_SCHEMA_NAME = "MMRG100.";

	private static final String TPAS_SCHEMA_NAME = "TPAS100.";

	private static final String MANUMGERGE_TABLE_NAME = "MANUMERGE_CURRENT_FILE";

	private static final String TPAS_TABLE_NAME = "TPA_REPORT";

	private static final String MANUMERGE_SORT_TABLE_NAME = "DOWNLOAD_IND_LOOKUP";

	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
	private static final FastDateFormat sdf = FastDateFormat.getInstance(
			"MM/dd/yyyy");

	private static final String YEAR_END_IND = "c.YEAREND_IND";

	private static final String TPA_CONTRACT_NO = "c.CONTRACT_NO";

	private static final String TPA_REPORT_END_DATE = "c.REPORT_END_DATE";

	private static final String GET_CURRENT_FILES_LIST_SQL_1 = "select "
			+ TPA_ID + "," + CONTRACT_NUMBER + "," + QUARTER_END_DATE + ","
			+ CONTRACT_NAME + "," + CORRECTION_INDICATOR + "," + LAST_RUN_DATE
			+ "," + LAST_DOWNLOAD_DATE + "," + FILE_SIZE + ","
			+ DOWNLOAD_STATUS + "," + DOWNLOAD_IND_DESC + ","
			+ DOWNLOAD_IND_SORT_ORDER + ", ";

	private static final String GET_CURRENT_FILES_LIST_SQL_2 = " FROM "
			+ MANUMERGE_SCHEMA_NAME + MANUMGERGE_TABLE_NAME + " a INNER JOIN "
			+ MANUMERGE_SCHEMA_NAME + MANUMERGE_SORT_TABLE_NAME
			+ " b ON a.DOWNLOAD_IND = b.DOWNLOAD_IND_LOOKUP_CD ";

	private static final String UPDATE_DOWNLOAD_IND_SQL = "update "
			+ MANUMERGE_SCHEMA_NAME
			+ MANUMGERGE_TABLE_NAME
			+ " SET DOWNLOAD_IND = ?, LAST_DOWNLOAD_DATE = ? WHERE THIRD_PARTY_ADMIN_ID = ? AND CONTRACT_ID = ? AND PERIOD_END_DATE = ?";

	private static final String YEAR_END_QUERY = "SELECT DISTINCT CONTRACT_NO,REPORT_END_DATE,YEAREND_IND FROM "
			+ TPAS_SCHEMA_NAME
			+ TPAS_TABLE_NAME
			+ MANUMERGE_SCHEMA_NAME
			+ MANUMGERGE_TABLE_NAME + " WHERE YEAREND_IND='Y'";

	private static final Map fieldToColumnMap = new HashMap();
	static {
		fieldToColumnMap.put(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NAME,
				CONTRACT_NAME);
		fieldToColumnMap.put(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NUMBER,
				CONTRACT_NUMBER);
		fieldToColumnMap.put(TedCurrentFilesItem.SORT_FIELD_CORRECTED_IND,
				CORRECTION_INDICATOR);
		fieldToColumnMap.put(TedCurrentFilesItem.SORT_FIELD_DOWNLOAD_STATUS,
				DOWNLOAD_IND_SORT_ORDER);
		fieldToColumnMap.put(TedCurrentFilesItem.SORT_FIELD_LAST_RUN_DATE,
				LAST_RUN_DATE);
		fieldToColumnMap.put(TedCurrentFilesItem.SORT_FIELD_LAST_DOWNLOAD_DATE,
				LAST_DOWNLOAD_DATE);
		fieldToColumnMap.put(TedCurrentFilesItem.SORT_FIELD_QUARTER_END_DATE,
				QUARTER_END_DATE);
		fieldToColumnMap.put(TedCurrentFilesItem.SORT_FIELD_YEAR_END_IND,
				YEAR_END_IND);
	}

	// Make sure nobody instantiates this class
	private TedCurrentFilesDAO() {
	}

	public static ReportData getReportData(ReportCriteria criteria)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getTedCurrentFilesList");
			logger.debug("Report criteria -> " + criteria.toString());
		}

		Connection conn = null;
		CallableStatement statement = null;
		ResultSet resultSet = null;
		TedCurrentFilesReportData vo = null;

		// Check that the FTP server is available;

		try {
			TedFTPHelper server = new TedFTPHelper(Boolean.parseBoolean(TedFTPServer.getTedFtpInd()));
			if (!server.isServerConnectionOK()) {
				vo = new TedCurrentFilesReportData(null, 0, 0);
				vo.setErrorCode(TedCurrentFilesReportData.RETURN_CODE_FTP_SERVER_DOWN);
				vo.setDetails(new ArrayList(0));
				return vo;
			}
		} catch (Exception e) {
			vo = new TedCurrentFilesReportData(null, 0, 0);
			vo.setErrorCode(TedCurrentFilesReportData.RETURN_CODE_FTP_SERVER_DOWN);
			vo.setDetails(new ArrayList(0));
			return vo;
		}

		try {
			// setup the connection and the statement
			conn = getReadUncommittedConnection(className,
					CUSTOMER_DATA_SOURCE_NAME);
			String sortPhrase = createSortPhrase(criteria);
			String filterPhrase = createFilterPhrase(criteria);

			if (filterPhrase.equals(STOP_QUERY)) {
				return new TedCurrentFilesReportData(criteria, 0, 0);
			}

			String yearEndToFilter = (String) criteria
					.getFilterValue(TedCurrentFilesReportData.FILTER_YEAR_END);
			if (yearEndToFilter == null || yearEndToFilter.trim().equals("")) {
				yearEndToFilter = YEAR_END_IND;
			} else if (yearEndToFilter != null
					&& yearEndToFilter.trim().equals("Y")) {
				yearEndToFilter = YEAR_END_IND;
			} else if (yearEndToFilter != null
					&& yearEndToFilter.trim().equals("N")) {
				yearEndToFilter = "'' AS YEAREND_IND";
			}

			String sql = GET_CURRENT_FILES_LIST_SQL_1 + yearEndToFilter
					+ GET_CURRENT_FILES_LIST_SQL_2 + filterPhrase
					+ " ORDER BY " + sortPhrase + " FOR READ ONLY ";

			statement = conn.prepareCall(sql, ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);

			// execute the stored procedure
			statement.execute();

			resultSet = statement.getResultSet();

			List currentFileList = new ArrayList();
			int size = 0;
			int newAndDownloadedFileCounter = 0;

			if (resultSet != null) {

				while (resultSet.next()) {
					if (logger.isDebugEnabled()) {

					}
					TedCurrentFilesItem item = new TedCurrentFilesItem();
					item.setContractName(resultSet.getString(CONTRACT_NAME
							.substring(2)));
					item.setContractNumber(resultSet.getString(CONTRACT_NUMBER
							.substring(2)));
					item.setCorrectedIndicator(resultSet
							.getString(CORRECTION_INDICATOR.substring(2)));

					// This must be set before download ind.
					Date lastDownloadDate = resultSet
							.getDate(LAST_DOWNLOAD_DATE.substring(2));
					GregorianCalendar downloadDate = new GregorianCalendar();
					downloadDate.setTime(lastDownloadDate);
					item.setLastDownloadDate(downloadDate);

					// This must be set after last download date
					String downloadInd = resultSet.getString(DOWNLOAD_STATUS
							.substring(2));
					item.setDownloadStatusCode(downloadInd);
					if ((downloadInd
							.equalsIgnoreCase(TedCurrentFilesItem.DOWNLOAD_STATUS_DOWNLOADED))
							|| (downloadInd
									.equalsIgnoreCase(TedCurrentFilesItem.DOWNLOAD_STATUS_NEW))) {
						newAndDownloadedFileCounter += 1;
					}
					Date lastRunDate = resultSet.getDate(LAST_RUN_DATE
							.substring(2));
					GregorianCalendar runDate = new GregorianCalendar();
					runDate.setTime(lastRunDate);
					item.setLastRunDate(runDate);

					GregorianCalendar periodDate = new GregorianCalendar();
					periodDate.setTime(resultSet.getDate(QUARTER_END_DATE
							.substring(2)));
					item.setQuarterEndDate(periodDate);
					item.setFileSize(resultSet.getString(FILE_SIZE.substring(2)));
					item.setTpaId(resultSet.getBigDecimal(TPA_ID.substring(2))
							.toString());
					item.setYearEnd(resultSet.getString(YEAR_END_IND
							.substring(2)));
					currentFileList.add(item);
					size += 1;
				}

			}

			vo = new TedCurrentFilesReportData(criteria, size,
					newAndDownloadedFileCounter);
			vo.setDetails(currentFileList);
		} catch (SQLException e) {
			throw new SystemException(e, className, "getReportData",
					"Problem occurred during TedCurrentFilesDAO:getCurrentFiles SQL call.");
		} finally {
			close(statement, conn);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getTEDCurrentFilesList");
		}

		return vo;
	}

	protected static String createSortPhrase(ReportCriteria criteria) {
		StringBuffer result = new StringBuffer();
		Iterator sorts = criteria.getSorts().iterator();
		String firstSortDirection = null;
		String firstSortField = null;

		if (logger.isDebugEnabled()) {
			logger.debug("enter -> createSortPhrase");
		}
		for (int i = 0; sorts.hasNext(); i++) {
			ReportSort sort = (ReportSort) sorts.next();

			if (i == 0) {
				// Primary sort as clicked on page.
				firstSortDirection = sort.getSortDirection();
				firstSortField = sort.getSortField();
				result.append(fieldToColumnMap.get(firstSortField)).append(' ');
				result.append(firstSortDirection);
			}

			if (firstSortField
					.equalsIgnoreCase(TedCurrentFilesItem.SORT_FIELD_DOWNLOAD_STATUS)) {
				// Requirement TED 42
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_QUARTER_END_DATE));
				result.append(" ");
				result.append(ReportSort.DESC_DIRECTION);
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NAME));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NUMBER));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);

			} else if (firstSortField
					.equalsIgnoreCase(TedCurrentFilesItem.SORT_FIELD_CORRECTED_IND)) {
				// Requirement TED 41
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_DOWNLOAD_STATUS));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_QUARTER_END_DATE));
				result.append(" ");
				result.append(ReportSort.DESC_DIRECTION);
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NAME));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NUMBER));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);
			} else if (firstSortField
					.equalsIgnoreCase(TedCurrentFilesItem.SORT_FIELD_LAST_RUN_DATE)) {
				// Requirement TED 40
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_DOWNLOAD_STATUS));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_QUARTER_END_DATE));
				result.append(" ");
				result.append(ReportSort.DESC_DIRECTION);
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NAME));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NUMBER));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);

			} else if (firstSortField
					.equalsIgnoreCase(TedCurrentFilesItem.SORT_FIELD_QUARTER_END_DATE)) {
				// Requirement TED 39
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_DOWNLOAD_STATUS));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NAME));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NUMBER));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);

			} else if (firstSortField
					.equalsIgnoreCase(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NUMBER)) {
				// Requirement TED 37
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_QUARTER_END_DATE));
				result.append(" ");
				result.append(ReportSort.DESC_DIRECTION);

			} else if (firstSortField
					.equalsIgnoreCase(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NAME)) {
				// Requirement TED 38
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NUMBER));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_QUARTER_END_DATE));
				result.append(" ");
				result.append(ReportSort.DESC_DIRECTION);
			} else if (firstSortField
					.equalsIgnoreCase(TedCurrentFilesItem.SORT_FIELD_LAST_DOWNLOAD_DATE)) {
				// New Req
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_QUARTER_END_DATE));
				result.append(" ");
				result.append(ReportSort.DESC_DIRECTION);
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NAME));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NUMBER));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);
			} else if (firstSortField
					.equalsIgnoreCase(TedCurrentFilesItem.SORT_FIELD_YEAR_END_IND)) {
				// CL 130434
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_DOWNLOAD_STATUS));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_QUARTER_END_DATE));
				result.append(" ");
				result.append(ReportSort.DESC_DIRECTION);
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NAME));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);
				result.append(", ");
				result.append(fieldToColumnMap
						.get(TedCurrentFilesItem.SORT_FIELD_CONTRACT_NUMBER));
				result.append(" ");
				result.append(ReportSort.ASC_DIRECTION);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- createSortPhrase");
		}
		return result.toString();
	}

	private static String createFilterPhrase(ReportCriteria criteria)
			throws SystemException {
		StringBuffer result = new StringBuffer();

		if (logger.isDebugEnabled()) {
			logger.debug("enter -> createFilterPhrase");
		}

		// **** This filter is enacted each time... it guarantees TPA can only
		// see files they have
		// access to *****
		// Get list of contracts that this TPA is actually allowed to view.
		ArrayList contracts = getContracts(
				(String) criteria
						.getFilterValue(TedCurrentFilesReportData.FILTER_PROFILE_ID),
				(String) criteria
						.getFilterValue(TedCurrentFilesReportData.FILTER_SITE_LOCATION));
		if (contracts.size() > 0) {
			result.append(" AND " + CONTRACT_NUMBER + " IN (");
			for (int i = 0; i < contracts.size(); i++) {
				result.append(contracts.get(i));
				if ((contracts.size() > 1) && (i < contracts.size() - 1)) {
					result.append(",");
				}
			}
			result.append(") ");
		}

		// **** The following are column filters *****
		String contractNumberToFilter = (String) criteria
				.getFilterValue(TedCurrentFilesReportData.FILTER_CONTRACT_NUMBER);
		String contractNameToFilter = (String) criteria
				.getFilterValue(TedCurrentFilesReportData.FILTER_CONTRACT_NAME);
		String downloadStatusToFilter = (String) criteria
				.getFilterValue(TedCurrentFilesReportData.FILTER_DOWNLOAD_STATUS);
		String correctedStatusToFilter = (String) criteria
				.getFilterValue(TedCurrentFilesReportData.FILTER_CORRECTED);
		String periodEndDateToFilter = (String) criteria
				.getFilterValue(TedCurrentFilesReportData.FILTER_PERIOD_END_DATE);
		String yearEndToFilter = (String) criteria
				.getFilterValue(TedCurrentFilesReportData.FILTER_YEAR_END);

		// If we have a contract number, we blow away previous IN ( ) clause and
		// use the following.
		// but only if it is a valid contract number. If number not provided,
		// then we do name if
		// provided.

		if (contractNumberToFilter != null
				&& (contractNumberToFilter.trim().length() >= 5 && contractNumberToFilter
						.trim().length() <= 7)) {
			if (contracts.contains(contractNumberToFilter.trim())) {
				result = new StringBuffer();
				result.append(" AND " + CONTRACT_NUMBER + "="
						+ contractNumberToFilter.trim() + " ");
			} else {
				return STOP_QUERY; // We don't want to continue, since its not a
									// valid contract.
			}
		} else if (contractNameToFilter != null
				&& !contractNameToFilter.trim().equals("")) {
			result.append(" AND UPPER(" + CONTRACT_NAME + ") LIKE UPPER('"
					+ contractNameToFilter.trim() + "%') ");
		}

		// Extra independant filters

		if (downloadStatusToFilter != null
				&& !downloadStatusToFilter.trim().equals("")) {

			if (downloadStatusToFilter
					.equals(TedCurrentFilesItem.DOWNLOAD_STATUS_NEW)) {
				String today = sdf.format(GregorianCalendar.getInstance()
						.getTime());
				result.append(" AND (" + DOWNLOAD_STATUS + "='"
						+ downloadStatusToFilter.trim() + "' ");
				result.append(" OR (" + DOWNLOAD_STATUS + "='"
						+ TedCurrentFilesItem.DOWNLOAD_STATUS_DOWNLOADED
						+ "' AND " + LAST_DOWNLOAD_DATE + "='" + today
						+ "' )) ");
			} else {
				result.append(" AND " + DOWNLOAD_STATUS + "='"
						+ downloadStatusToFilter.trim() + "' ");
			}

		}

		if (correctedStatusToFilter != null
				&& !correctedStatusToFilter.trim().equals("")) {
			result.append(" AND " + CORRECTION_INDICATOR + "='"
					+ correctedStatusToFilter.trim() + "' ");
		}

		if (periodEndDateToFilter != null
				&& !periodEndDateToFilter.trim().equals("")) {
			result.append(" AND " + QUARTER_END_DATE + "='"
					+ periodEndDateToFilter.trim() + "' ");
		}

		String subQuery = createYearEndSubquery(contracts,
				contractNumberToFilter, yearEndToFilter);

		result.append(subQuery);

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> createFilterPhrase  PHRASE="
					+ result.toString());
		}
		return result.toString();
	}

	/**
	 * Updates the downloadInd column in the data base for the current ManuMerge
	 * file specified by executing the appropriate SQL statement.
	 * 
	 * @return int
	 * @param key
	 *            com.manulife.pension.lp.model.sft.TPAidKey
	 * @param currentFileName
	 *            com.manulife.pension.lp.bos.filetransfer.CurrentFileName
	 */
	public static void updateDownloadInd(String tpaId, String contractNumber,
			String quarterEndDate) throws SystemException {
		Connection conn = null;
		CallableStatement statement = null;
		ResultSet rs = null;
		int returnCode = 0;

		if (logger.isDebugEnabled()) {
			logger.debug("enter -> updateDownloadInd");
		}

		try {
			// Connect to DB
			conn = getReadUncommittedConnection(className,
					CUSTOMER_DATA_SOURCE_NAME);
			statement = conn.prepareCall(UPDATE_DOWNLOAD_IND_SQL,
					ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			Date currentDate = new Date(System.currentTimeMillis());

			statement.setString(1, "D");
			statement.setDate(2, currentDate);
			statement.setBigDecimal(3, new BigDecimal(tpaId));
			statement.setInt(4, new Integer(contractNumber).intValue());
			Date quarterEnd = Date.valueOf(quarterEndDate);
			statement.setDate(5, quarterEnd);
			// execute query
			returnCode = statement.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			throw new SystemException(e, className, "updateDownloadInd",
					"Could not update indicator for: TPA=" + tpaId
							+ ", Contract=" + contractNumber + ", quarterEnd="
							+ quarterEndDate);
		} finally {
			close(statement, conn);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- updateDownloadInd");
		}
	}

	public static ArrayList getContracts(String profileId, String siteLocation)
			throws SystemException {
		ArrayList contracts = new ArrayList();

		if (logger.isDebugEnabled()) {
			logger.debug("enter -> getContracts");
		}

		ReportCriteria criteria = new ReportCriteria(
				SelectContractReportData.REPORT_ID);
		// set report criteria
		criteria.insertSort("contractName", ReportSort.ASC_DIRECTION);
		criteria.setPageSize(9999);
		criteria.addFilter(SelectContractReportData.FILTER_CLIENT_ID, profileId);
		criteria.addFilter(SelectContractReportData.FILTER_SITE_LOCATION,
				siteLocation);
		criteria.addFilter(SelectContractReportData.FILTER_USER_ROLE,
				ConversionUtility
						.convertToStoredProcRole(new ThirdPartyAdministrator()));
		criteria.addFilter(SelectContractReportData.FILTER_SEARCH_STRING, null);

		try {
			ReportData vo = SelectContractDAO.getReportData(criteria);
			if (vo != null && vo.getDetails() != null) {
				SelectContract[] contractDetails = (SelectContract[]) vo
						.getDetails().toArray(new SelectContract[0]);
				StaticHelperClass.toString(contractDetails);
				for (int i = 0; i < vo.getTotalCount(); i++) {
					if (contractDetails[i] != null) {
						int contractNumber = contractDetails[i]
								.getContractNumber();
						contracts.add(String.valueOf(contractNumber));
					}
				}
			}
		} catch (Exception e) {
			throw new SystemException(e, className, "getContracts",
					"Problem occurred during SelectContractDAO.getReportData() call.");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit -> getContracts");
		}

		return contracts;
	}

	private String getOppositeSort(String direction) {
		if (direction.equalsIgnoreCase(ReportSort.DESC_DIRECTION)) {
			return ReportSort.ASC_DIRECTION;
		} else {
			return ReportSort.DESC_DIRECTION;
		}
	}

	private static String createYearEndSubquery(ArrayList contracts,
			String contractNumber, String yearEndToFilter)
			throws SystemException {
		StringBuffer result = new StringBuffer();
		StringBuffer contractList = new StringBuffer();
		StringBuffer contractID = new StringBuffer();

		if (contractNumber != null && !contractNumber.trim().equals("")) {
			contractList.append(" CONTRACT_NO " + "=" + contractNumber.trim()
					+ " ");
			contractID.append(" CONTRACT_ID = " + contractNumber.trim() + " ");
		} else {
			if (contracts.size() > 0) {
				contractList.append(" CONTRACT_NO IN (");
				contractID.append(" CONTRACT_ID IN (");
				for (int i = 0; i < contracts.size(); i++) {
					contractList.append(contracts.get(i));
					contractID.append(contracts.get(i));
					if ((contracts.size() > 1) && (i < contracts.size() - 1)) {
						contractList.append(",");
						contractID.append(",");
					}
				}
				contractList.append(") ");
				contractID.append(") ");
			}
		}

		if (yearEndToFilter == null || yearEndToFilter.trim().equals("")) {
			result.append(" LEFT OUTER JOIN (SELECT DISTINCT CONTRACT_NO,REPORT_END_DATE,YEAREND_IND FROM "
					+ TPAS_SCHEMA_NAME
					+ TPAS_TABLE_NAME
					+ ","
					+ MANUMERGE_SCHEMA_NAME
					+ MANUMGERGE_TABLE_NAME
					+ " WHERE YEAREND_IND='Y' AND "
					+ contractList
					+ "AND CONTRACT_ID=CONTRACT_NO AND PERIOD_END_DATE=REPORT_END_DATE) C "
					+ "ON A.CONTRACT_ID=C.CONTRACT_NO AND A.PERIOD_END_DATE=C.REPORT_END_DATE ");
		} else if (yearEndToFilter != null
				&& yearEndToFilter.trim().equals("Y")) {
			result.append(" INNER JOIN (SELECT DISTINCT CONTRACT_NO,REPORT_END_DATE,YEAREND_IND FROM "
					+ TPAS_SCHEMA_NAME
					+ TPAS_TABLE_NAME
					+ ","
					+ MANUMERGE_SCHEMA_NAME
					+ MANUMGERGE_TABLE_NAME
					+ " WHERE YEAREND_IND='Y' AND "
					+ contractList
					+ "AND CONTRACT_ID=CONTRACT_NO AND PERIOD_END_DATE=REPORT_END_DATE) C "
					+ "ON A.CONTRACT_ID=C.CONTRACT_NO AND A.PERIOD_END_DATE=C.REPORT_END_DATE ");
		} else if (yearEndToFilter != null
				&& yearEndToFilter.trim().equals("N")) {
			result.append(" INNER JOIN (SELECT CONTRACT_ID,PERIOD_END_DATE FROM "
					+ MANUMERGE_SCHEMA_NAME
					+ MANUMGERGE_TABLE_NAME
					+ " WHERE "
					+ contractID
					+ " EXCEPT SELECT DISTINCT CONTRACT_NO,REPORT_END_DATE FROM "
					+ TPAS_SCHEMA_NAME
					+ TPAS_TABLE_NAME
					+ " WHERE YEAREND_IND='Y' AND "
					+ contractList
					+ ") C ON A.CONTRACT_ID=C.CONTRACT_ID AND A.PERIOD_END_DATE=C.PERIOD_END_DATE ");
		}

		return result.toString();
	}
}