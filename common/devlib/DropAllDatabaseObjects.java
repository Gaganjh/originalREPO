
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Insert the type's description here.
 * Creation date: (11/1/2002 11:25:47 AM)
 * @author: Administrator
 */


public class DropAllDatabaseObjects {
	static {
		try {
			// register the driver with DriverManager
			// The newInstance() call is needed for the sample to work with
			// JDK 1.1.1 on OS/2, where the Class.forName() method does not
			// run the static initializer. For other JDKs, the newInstance
			// call can be omitted.
			Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static Connection con = null;
/**
 * DropAllDatabaseObjects constructor comment.
 */
public DropAllDatabaseObjects() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (11/1/2002 1:27:48 PM)
 * @return int
 */
public static int dropAliases() throws SQLException {
    StringBuffer sqlBuffer = null;
    PreparedStatement stmt  = null;
    PreparedStatement stmt2 = null;
    ResultSet rs = null;
    String tabschema = null;
    String tabname = null;
    int rowsAffected = 0;

    sqlBuffer = new StringBuffer();
    sqlBuffer.append("select tabschema, tabname ");
    sqlBuffer.append("from syscat.tables ");
    sqlBuffer.append("where type in ('A') ");
    sqlBuffer.append("and   tabschema not like 'SYS%' ");
    sqlBuffer.append("and   tabschema not like 'DB2EXT%' ");
    sqlBuffer.append("and   tabschema not like 'BRMS_CONFIG%' ");
    sqlBuffer.append("order by tabschema, tabname ");

    stmt = con.prepareStatement(sqlBuffer.toString());
    rs = stmt.executeQuery();

    while (rs.next()) {
	    tabschema = rs.getString(1);
	    tabname = rs.getString(2);
	    sqlBuffer = new StringBuffer();
	    sqlBuffer.append("drop alias ");
	    sqlBuffer.append(tabschema.trim());
	    sqlBuffer.append(".");
	    sqlBuffer.append(tabname.trim());
	    String sql = sqlBuffer.toString();
	    System.out.println(sql);
	    stmt2 = con.prepareStatement(sql);
	    stmt2.executeUpdate();
	    rowsAffected += 1;
    }
    return rowsAffected;
}
/**
 * Insert the method's description here.
 * Creation date: (11/1/2002 1:27:48 PM)
 * @return int
 */
public static int dropStoredProcedures() throws SQLException {
    StringBuffer sqlBuffer = null;
    PreparedStatement stmt  = null;
    PreparedStatement stmt2 = null;
    ResultSet rs = null;
    String tabschema = null;
    String tabname = null;
    int rowsAffected = 0;

    sqlBuffer = new StringBuffer();
    sqlBuffer.append("select procschema, procname ");
    sqlBuffer.append("from syscat.procedures ");
    sqlBuffer.append("where procschema not like 'SYS%' and procschema not like 'SQLJ%' AND procschema <> 'ASN' AND LANGUAGE NOT IN ('SQL','C') ");
    sqlBuffer.append("and   procschema not like 'DB2EXT%' ");
    sqlBuffer.append("and   procschema not like 'BRMS_CONFIG%' ");
    sqlBuffer.append("order by procschema, procname ");

    stmt = con.prepareStatement(sqlBuffer.toString());
    rs = stmt.executeQuery();

    while (rs.next()) {
	    tabschema = rs.getString(1);
	    tabname = rs.getString(2);
	    sqlBuffer = new StringBuffer();
	    sqlBuffer.append("drop procedure ");
	    sqlBuffer.append(tabschema.trim());
	    sqlBuffer.append(".");
	    sqlBuffer.append(tabname.trim());
	    String sql = sqlBuffer.toString();
	    System.out.println(sql);
	    stmt2 = con.prepareStatement(sql);
	    stmt2.executeUpdate();
	    rowsAffected += 1;
    }
    return rowsAffected;
}
/**
 * Insert the method's description here.
 * Creation date: (11/1/2002 1:27:48 PM)
 * @return int
 */
public static int dropSQLStoredProcedures() throws SQLException {
    StringBuffer sqlBuffer = null;
    PreparedStatement stmt  = null;
    PreparedStatement stmt2 = null;
    ResultSet rs = null;
    String tabschema = null;
    String tabname = null;
    int rowsAffected = 0;

    sqlBuffer = new StringBuffer();
    sqlBuffer.append("select procschema, procname ");
    sqlBuffer.append("from syscat.procedures ");
    sqlBuffer.append("where procschema not like 'SYS%' and procschema not like 'SQLJ%' AND LANGUAGE = 'SQL' ");
    sqlBuffer.append("and   procschema not like 'DB2EXT%' ");
    sqlBuffer.append("and   procschema not like 'BRMS_CONFIG%' ");
    sqlBuffer.append("order by procschema, procname ");

    stmt = con.prepareStatement(sqlBuffer.toString());
    rs = stmt.executeQuery();

    while (rs.next()) {
	    tabschema = rs.getString(1);
	    tabname = rs.getString(2);
	    sqlBuffer = new StringBuffer();
	    sqlBuffer.append("drop procedure ");
	    sqlBuffer.append(tabschema.trim());
	    sqlBuffer.append(".");
	    sqlBuffer.append(tabname.trim());
	    String sql = sqlBuffer.toString();
	    System.out.println(sql);
	    stmt2 = con.prepareStatement(sql);
	    stmt2.executeUpdate();
	    rowsAffected += 1;
    }
    return rowsAffected;
}
/**
 * Insert the method's description here.
 * Creation date: (11/1/2002 1:27:48 PM)
 * @return int
 */
public static int dropTables() throws SQLException {
    StringBuffer sqlBuffer = null;
    PreparedStatement stmt  = null;
    PreparedStatement stmt2 = null;
    ResultSet rs = null;
    String tabschema = null;
    String tabname = null;
    int rowsAffected = 0;

    sqlBuffer = new StringBuffer();
    sqlBuffer.append("select tabschema, tabname ");
    sqlBuffer.append("from syscat.tables ");
    sqlBuffer.append("where type in ('T', 'G') ");
    sqlBuffer.append("and   tabschema not like 'SYS%' ");
    sqlBuffer.append("and   tabschema not like 'DB2EXT%' ");
    sqlBuffer.append("and   tabschema not like 'BRMS_CONFIG%' ");
    sqlBuffer.append("order by tabschema, tabname ");

    stmt = con.prepareStatement(sqlBuffer.toString());
    rs = stmt.executeQuery();

    while (rs.next()) {
	    tabschema = rs.getString(1);
	    tabname = rs.getString(2);
	    sqlBuffer = new StringBuffer("drop table ");
	    sqlBuffer.append(tabschema.trim());
	    sqlBuffer.append(".");
	    sqlBuffer.append(tabname.trim());
	    String sql = sqlBuffer.toString();
	    System.out.println(sql);
	    stmt2 = con.prepareStatement(sql);
	    stmt2.executeUpdate();
	    rowsAffected += 1;
    }
    return rowsAffected;
}
/**
 * Insert the method's description here.
 * Creation date: (11/1/2002 1:27:48 PM)
 * @return int
 */
public static int dropTriggers() throws SQLException {
    String sql = null;
    PreparedStatement stmt  = null;
    PreparedStatement stmt2 = null;
    ResultSet rs = null;
    String tabschema = null;
    String tabname = null;
    int rowsAffected = 0;

    sql =
        "select trigschema, trigname "
            + "from syscat.triggers "
            + "order by trigschema, trigname ";

    stmt = con.prepareStatement(sql);
    rs = stmt.executeQuery();

    while (rs.next()) {
	    tabschema = rs.getString(1);
	    tabname = rs.getString(2);
	    sql = "drop trigger "+tabschema.trim()+"."+tabname.trim();
	    System.out.println(sql);
	    stmt2 = con.prepareStatement(sql);
	    stmt2.executeUpdate();
	    rowsAffected += 1;
    }
    return rowsAffected;
}
/**
  * @return int
 */
public static int dropFunctions() throws SQLException {
    StringBuffer sqlBuffer = null;
    String sql = null;
    PreparedStatement stmt  = null;
    PreparedStatement stmt2 = null;
    ResultSet rs = null;
    String tabschema = null;
    String tabname = null;
    int rowsAffected = 0;

    sqlBuffer = new StringBuffer();
    sqlBuffer.append("select FUNCSCHEMA, FUNCNAME ");
    sqlBuffer.append("from syscat.FUNCTIONS ");
    sqlBuffer.append("where FUNCSCHEMA not like 'SYS%' AND LANGUAGE <> 'SQL'");
    sqlBuffer.append("and   FUNCSCHEMA not like 'DB2EXT%' ");
    sqlBuffer.append("and   FUNCSCHEMA not like 'BRMS_CONFIG%' ");
    sqlBuffer.append("order by FUNCSCHEMA, FUNCNAME ");

    stmt = con.prepareStatement(sqlBuffer.toString());
    rs = stmt.executeQuery();

    while (rs.next()) {
	    tabschema = rs.getString(1);
	    tabname = rs.getString(2);
	    sql = "DROP FUNCTION "+tabschema.trim()+"."+tabname.trim();
	    System.out.println(sql);
	    stmt2 = con.prepareStatement(sql);
	    stmt2.executeUpdate();
	    rowsAffected += 1;
    }
    return rowsAffected;
}
/**
 * @return int
*/
public static int dropSQLFunctions() throws SQLException {
   StringBuffer sqlBuffer = null;
   String sql = null;
   PreparedStatement stmt  = null;
   PreparedStatement stmt2 = null;
   ResultSet rs = null;
   String tabschema = null;
   String tabname = null;
   int rowsAffected = 0;

   sqlBuffer = new StringBuffer();
   sqlBuffer.append("select FUNCSCHEMA, FUNCNAME ");
   sqlBuffer.append("from syscat.FUNCTIONS ");
   sqlBuffer.append("where FUNCSCHEMA not like 'SYS%' AND LANGUAGE = 'SQL'");
   sqlBuffer.append("and   FUNCSCHEMA not like 'DB2EXT%' ");
   sqlBuffer.append("and   FUNCSCHEMA not like 'BRMS_CONFIG%' ");
   sqlBuffer.append("order by FUNCSCHEMA, FUNCNAME ");

   stmt = con.prepareStatement(sqlBuffer.toString());
   rs = stmt.executeQuery();

   while (rs.next()) {
	    tabschema = rs.getString(1);
	    tabname = rs.getString(2);
	    sql = "DROP FUNCTION "+tabschema.trim()+"."+tabname.trim();
	    System.out.println(sql);
	    stmt2 = con.prepareStatement(sql);
	    stmt2.executeUpdate();
	    rowsAffected += 1;
   }
   return rowsAffected;
}
/**
 * Insert the method's description here.
 * Creation date: (11/1/2002 1:27:48 PM)
 * @return int
 */
public static int dropViews() throws SQLException {
    StringBuffer sqlBuffer = null;
    PreparedStatement stmt  = null;
    PreparedStatement stmt2 = null;
    ResultSet rs = null;
    String tabschema = null;
    String tabname = null;
    int rowsAffected = 0;


    sqlBuffer = new StringBuffer();
    sqlBuffer.append("select tabschema, tabname ");
    sqlBuffer.append("from syscat.tables ");
    sqlBuffer.append("where type in ('V') ");
    sqlBuffer.append("and   tabschema not like 'SYS%' ");
    sqlBuffer.append("and   tabschema not like 'DB2EXT%' ");
    sqlBuffer.append("and   tabschema not like 'BRMS_CONFIG%' ");
    sqlBuffer.append("order by tabschema, tabname ");

    stmt = con.prepareStatement(sqlBuffer.toString());
    rs = stmt.executeQuery();

    while (rs.next()) {
	    tabschema = rs.getString(1);
	    tabname = rs.getString(2);
	    sqlBuffer = new StringBuffer("drop view ");
	    sqlBuffer.append(tabschema.trim());
	    sqlBuffer.append(".");
	    sqlBuffer.append(tabname.trim());
	    String sql = sqlBuffer.toString();
	    System.out.println(sql);
	    stmt2 = con.prepareStatement(sql);
	    stmt2.executeUpdate();
	    rowsAffected += 1;
    }
    return rowsAffected;
}

/**
 * @return int
 */
public static int dropNicknames() throws SQLException {
    StringBuffer sqlBuffer = null;
    PreparedStatement stmt  = null;
    PreparedStatement stmt2 = null;
    ResultSet rs = null;
    String tabschema = null;
    String tabname = null;
    int rowsAffected = 0;


    sqlBuffer = new StringBuffer();
    sqlBuffer.append("select tabschema, tabname ");
    sqlBuffer.append("from syscat.tables ");
    sqlBuffer.append("where type in ('N') ");
    sqlBuffer.append("and   tabschema not like 'SYS%' ");
    sqlBuffer.append("and   tabschema not like 'DB2EXT%' ");
    sqlBuffer.append("and   tabschema not like 'BRMS_CONFIG%' ");
    sqlBuffer.append("order by tabschema, tabname ");

    stmt = con.prepareStatement(sqlBuffer.toString());
    rs = stmt.executeQuery();

    while (rs.next()) {
	    tabschema = rs.getString(1);
	    tabname = rs.getString(2);
	    sqlBuffer = new StringBuffer("DROP  NICKNAME ");
	    sqlBuffer.append(tabschema.trim());
	    sqlBuffer.append(".");
	    sqlBuffer.append(tabname.trim());
	    String sql = sqlBuffer.toString();
	    System.out.println(sql);
	    stmt2 = con.prepareStatement(sql);
	    stmt2.executeUpdate();
	    rowsAffected += 1;
    }
    return rowsAffected;
}

public static int dropSequences() throws SQLException {
    StringBuffer sqlBuffer = null;
    PreparedStatement stmt  = null;
    PreparedStatement stmt2 = null;
    ResultSet rs = null;
    String seqschema = null;
    String seqname = null;
    int rowsAffected = 0;


    //sqlBuffer = new StringBuffer(); commented by harish
    //sqlBuffer.append("select seqschema, seqname ");
    //sqlBuffer.append("from syscat.sequences ");

    sqlBuffer = new StringBuffer(); //added by Manoj
    sqlBuffer.append("select seqschema, seqname ");
    sqlBuffer.append("from syscat.sequences where seqschema not like 'SYS%' and seqschema not like 'BRMS_CONFIG%' ");

    stmt = con.prepareStatement(sqlBuffer.toString());
    rs = stmt.executeQuery();

    while (rs.next()) {
	    seqschema = rs.getString(1);
	    seqname = rs.getString(2);
	    sqlBuffer = new StringBuffer("DROP  SEQUENCE ");
	    sqlBuffer.append(seqschema.trim());
	    sqlBuffer.append(".");
	    sqlBuffer.append(seqname.trim());
	    String sql = sqlBuffer.toString();
	    System.out.println(sql);
	    stmt2 = con.prepareStatement(sql);
	    stmt2.executeUpdate();
	    rowsAffected += 1;
    }
    return rowsAffected;
}

public void runDropAllDatabaseObjects(String database, String userId,
				String password, String operationType) throws Exception {

    int rowsAffectedAliases = 0;
    int rowsAffectedViews = 0;
    int rowsAffectedTables = 0;
    int rowsAffectedTriggers = 0;
    int rowsAffectedStoredProcedures = 0;
    int rowsAffectedNickNames = 0;
    int rowsAffectedFunctions = 0;
    int rowsAffectedSequences = 0;

    try {

        con = DriverManager.getConnection("jdbc:db2:" + database, userId, password);
        System.out.println(
            "Connection to " + database + " Using " + userId + " was successful.");

        con.setAutoCommit(false);

        if (operationType.toUpperCase().equals("ALIASES"))
            rowsAffectedAliases = dropAliases();
        if (operationType.toUpperCase().equals("VIEWS"))
            rowsAffectedViews = dropViews();
        if (operationType.toUpperCase().equals("TABLES"))
            rowsAffectedTables = dropTables();
        if (operationType.toUpperCase().equals("TRIGGERS"))
            rowsAffectedTriggers = dropTriggers();
        if (operationType.toUpperCase().equals("STORED_PROCEDURES"))
            rowsAffectedStoredProcedures = dropStoredProcedures();
        if (operationType.toUpperCase().equals("SQL_STORED_PROCEDURES"))
            rowsAffectedStoredProcedures = dropSQLStoredProcedures();
        if (operationType.toUpperCase().equals("NICKNAMES"))
            rowsAffectedNickNames = dropNicknames();
        if (operationType.toUpperCase().equals("FUNCTIONS"))
            rowsAffectedFunctions = dropFunctions();
        if (operationType.toUpperCase().equals("SQL_FUNCTIONS"))
            rowsAffectedFunctions = dropSQLFunctions();
        if (operationType.toUpperCase().equals("SEQUENCES"))
            rowsAffectedSequences = dropSequences();
        if (operationType.toUpperCase().equals("ALL")) {
            rowsAffectedAliases = dropAliases();
            rowsAffectedViews = dropViews();
            rowsAffectedTables = dropTables();
            rowsAffectedTriggers = dropTriggers();
            rowsAffectedStoredProcedures = dropStoredProcedures();
            rowsAffectedStoredProcedures = dropSQLStoredProcedures();
            rowsAffectedNickNames = dropNicknames();
            rowsAffectedFunctions = dropFunctions();
            rowsAffectedFunctions = dropSQLFunctions();
            rowsAffectedSequences = dropSequences();
        }
        System.out.println(
            "==============================================================");
        System.out.println("Summary ...");
        System.out.println(
            "==============================================================");
        System.out.println(
            "Number of Aliases Dropped           = " + rowsAffectedAliases);
        System.out.println(
            "Number of Views Dropped             = " + rowsAffectedViews);
        System.out.println(
            "Number of Tables Dropped            = " + rowsAffectedTables);
        System.out.println(
            "Number of Triggers Dropped          = " + rowsAffectedTriggers);
        System.out.println(
            "Number of Stored Procedures Dropped = " + rowsAffectedStoredProcedures);
        System.out.println(
            "Number of Nicknames Dropped         = " + rowsAffectedNickNames);
        System.out.println(
            "Number of Functions Dropped         = " + rowsAffectedFunctions);
        System.out.println(
            "Number of Sequences Dropped         = " + rowsAffectedSequences);

        con.commit();
    } catch (SQLException e) {
        System.out.println("getSQLState = " + e.getSQLState());
        System.out.println("getErrorCode = " + e.getErrorCode());
        System.out.println("getMessage = " + e.getMessage());
        throw e;
    } catch (Exception e) {
        throw e;
    }
    if (con != null)
        con.close();

}


/**
 * Insert the method's description here.
 * Creation date: (11/1/2002 11:26:05 AM)
 * @param args java.lang.String[]
 */
public static void main(String[] args) throws SQLException, Exception {


        String database = args[0];
        String userid = args[1];
        String password = args[2];
        String operationType = args[3];

        DropAllDatabaseObjects dropAllDatabaseObjects = new DropAllDatabaseObjects();
        dropAllDatabaseObjects.runDropAllDatabaseObjects(database, userid, password, operationType);
        System.exit(0);
 }
}
