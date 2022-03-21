package com.manulife.pension.ps.service.loadtest;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.ibm.db2.jcc.DB2ClientRerouteServerList;
import com.ibm.db2.jcc.DB2SimpleDataSource;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;

/**
 * This utility is to reflect on the StoredProc signature and call it
 * with the input values
 * 
 * @author guweigu
 *
 */
public class StoredProcInvoker extends BaseDatabaseDAO {

	private static Map<String, StoredProcMetaData> metaCache = new HashMap<String, StoredProcInvoker.StoredProcMetaData>();

	public StoredProcMetaData getStoredProcMetaData(Connection connection,
			String schemaName, String storedProcName) throws SQLException {
		String fullName = schemaName + "." + storedProcName;
		StoredProcMetaData metaData = metaCache.get(fullName);
		if (metaData == null) {
			metaData = new StoredProcMetaData(fullName);
			ResultSet rs = connection.getMetaData().getProcedureColumns(null,
					schemaName, storedProcName, null);
			while (rs.next()) {
				StoredProcParameterMetaData p = new StoredProcParameterMetaData(
						StringUtils.upperCase(rs.getString("COLUMN_NAME")),
						rs.getInt("COLUMN_TYPE"), rs.getInt("DATA_TYPE"),
						rs.getString("TYPE_NAME"));
				metaData.addParameter(p);
				metaCache.put(fullName, metaData);
			}

			rs.close();
		}
		return metaData;
	}

	public Map<String, Object> invoke(Connection connection,
			StoredProcMetaData metaData, Map<String, String> values)
			throws SQLException, ParseException {

		CallableStatement statement = connection.prepareCall(metaData
				.getCallStatementString());
		try {
			for (int i = 1; i <= metaData.getParameterCount(); i++) {
				StoredProcParameterMetaData parameterMeta = metaData
						.getParameter(i - 1);

				if (parameterMeta.getColumnType() != ParameterMetaData.parameterModeIn) {
					statement.registerOutParameter(i,
							parameterMeta.getDataType());
				} else {
					String parameterStringValue = values.get(parameterMeta
							.getName());

					parameterMeta.setParameter(statement, i,
							parameterStringValue);
				}
			}
			statement.execute();
			Map<String, Object> output = new HashMap<String, Object>();
			for (int i = 1; i <= metaData.getParameterCount(); i++) {
				StoredProcParameterMetaData parameterMeta = metaData
						.getParameter(i - 1);
				if (parameterMeta.getColumnType() != ParameterMetaData.parameterModeIn) {
					output.put(parameterMeta.getName(), statement.getObject(i));
				}
			}
			return output;

		} finally {
			close(statement, null);
		}

	}

	static public class StoredProcParameterMetaData {
		private String name;
		private int columnType;
		private int dataType;
		private String typeName;

		public StoredProcParameterMetaData(String name, int columnType,
				int dataType, String typeName) {
			super();
			this.name = name;
			this.columnType = columnType;
			this.dataType = dataType;
			this.typeName = typeName;
		}

		public void setParameter(CallableStatement statement, int index,
				String parameterStringValue) throws SQLException,
				ParseException {
			switch (dataType) {
			case Types.CHAR:
			case Types.VARCHAR:
				statement.setString(index, parameterStringValue);
				break;
			case Types.INTEGER:
				statement.setInt(index, parameterStringValue == null ? 0
						: Integer.parseInt(parameterStringValue));
				break;
			case Types.DECIMAL:
				statement.setBigDecimal(index,
						parameterStringValue == null ? new BigDecimal("0")
								: new BigDecimal(parameterStringValue));
				break;
			case Types.DATE:
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date d = format.parse(parameterStringValue);
				statement.setDate(index, parameterStringValue == null ? null
						: new java.sql.Date(d.getTime()));
				break;
			default:
				throw new UnsupportedOperationException(
						"Data Type not supported: " + dataType);
			}
		}

		public String getName() {
			return name;
		}

		public int getColumnType() {
			return columnType;
		}

		public int getDataType() {
			return dataType;
		}

		public String getTypeName() {
			return typeName;
		}

	};

	static class StoredProcMetaData {
		private String name;
		private List<StoredProcParameterMetaData> parameters;

		public StoredProcMetaData(String name) {
			this.name = name;
			parameters = new ArrayList<StoredProcInvoker.StoredProcParameterMetaData>();
		}

		public void addParameter(StoredProcParameterMetaData parameter) {
			parameters.add(parameter);
		}

		public int getParameterCount() {
			return parameters.size();
		}

		public StoredProcParameterMetaData getParameter(int index) {
			return parameters.get(index);
		}

		public String getCallStatementString() {
			StringBuffer buf = new StringBuffer("call " + name + "(");
			buf.append(StringUtils.repeat("?,", parameters.size() - 1));
			buf.append("?)");
			return buf.toString();
		}

	}

	public Map<String, Object> invoke(String schemaName, String storedProcName,
			Map<String, String> input) throws SystemException {

		Connection connection = null;
		try {
			connection = getDefaultConnection(
					StoredProcInvoker.class.getName(),
					CUSTOMER_DATA_SOURCE_NAME);

			Map<String, Object> output = invoke(
					connection,
					getStoredProcMetaData(connection, schemaName,
							storedProcName), input);
			return output;
		} catch (Exception e) {
			if (e instanceof SystemException) {
				throw (SystemException) e;
			} else {
				throw new SystemException(e, "Fail to invoke the storedProc = "
						+ schemaName + "." + storedProcName);
			}
		}

		finally {
			close(null, connection);
		}
	}

	public static void main(String[] args) throws Exception {
		DB2ClientRerouteServerList dbList = new DB2ClientRerouteServerList();
		DB2SimpleDataSource dataSource = new DB2SimpleDataSource();
		dataSource.setDriverType(2);

		dbList.setPrimaryServerName("localhost");
		dbList.setPrimaryPortNumber(50000);

		Properties p = new Properties();
		p.load(ClassLoader.getSystemResourceAsStream("test.properties"));
		String dbUser = p.getProperty("csdb.database.username");

		String dbPassword = p.getProperty("csdb.database.password");
		dataSource.setUser(dbUser);
		dataSource.setPassword(dbPassword);
		String dbName = p.getProperty("csdb.database.name");
		dataSource.setDatabaseName(dbName);

		dataSource.setClientRerouteServerList(dbList);

		Connection connection = dataSource.getConnection();
		StoredProcInvoker invoker = new StoredProcInvoker();

		long t1 = System.currentTimeMillis();
		StoredProcMetaData metaData = invoker.getStoredProcMetaData(connection,
				"EZK100", "IPS_FUND_EVALUATION");
		long t2 = System.currentTimeMillis();

		System.out.println("Meta : " + (t2 - t1));
		Map<String, String> input = new HashMap<String, String>();
		input.put("IPSOPTION", "BAL");
		input.put("WEIGHTINGDATE", "2011-02-11");
		input.put("INCLUDECLOSEFUND", "Y");
		input.put("INCLUDENMLFUND", "N");
		input.put("INVESTMENTOPTION", "BOTH");
		input.put("RATETYPE", "CL5");
		input.put("COMPANYCODE", "019");
		// input.put("DISTRIBUTIONCHANNEL", null);
		input.put("ISSUESTATE", "CA");
		input.put("ASSETCLASSSELECTION", "FSW");
		input.put("INCLUDELS", "Y");
		input.put("INCLUDERC", "N");
		input.put("INCLUDERL", "N");
		input.put("INCLUDEGIFL", "N");
		input.put("SVFIND", "Y");
		input.put("INCLUDEAVAILABLEFUNDS", "N");

		try {
			long t3 = System.currentTimeMillis();
			Map<String, Object> output = invoker.invoke(connection, metaData,
					input);
			long t4 = System.currentTimeMillis();
			System.out.println("Invoke : " + (t4 - t3));
			System.out.println(output);
		} finally {
			if(connection!= null)
				connection.close();
		}
	}
}
