package com.manulife.pension.ps.service.report.tpadownload.dao;

import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.util.BaseEnvironment;



/**
 * @author eldrima
 *
 * Encapsulates characteristics about the TPA eDownload FTP Server.
 */

public class TedFTPServer {

	private static final String TED_SERVER_NAME_PROPERTY = "ted.downloadFtpServer";
	private static final String FTP_USER_ID_PROPERTY = "ted.downloadFtpUsername";
	private static final String FTP_PASSWORD_PROPERTY = "ted.downloadFtpPassword";
	private static final String TED_EDOWNLOADFILES_DIR_PROPERTY = "ted.fileDownloadDirectory";
	private static final String TED_FTP_IND = "ted.ftpInd";
	
	private static String serverName = null;
	private static String userid = null;
	private static String password = null;
	private static String tpaEDownloadFilesDir = null;
	private static String tedFtpInd = null;
	
	static {
		try {
	   		// get SMTP server property
			BaseEnvironment environment = new BaseEnvironment();
					
			serverName = environment.getNamingVariable(TED_SERVER_NAME_PROPERTY, null);
			userid = environment.getNamingVariable(FTP_USER_ID_PROPERTY, null);
			password = environment.getNamingVariable(FTP_PASSWORD_PROPERTY, null);
			tpaEDownloadFilesDir = environment.getNamingVariable(TED_EDOWNLOADFILES_DIR_PROPERTY, null);
			tedFtpInd = environment.getNamingVariable(TED_FTP_IND, null);
						
		} catch (Exception e) {
	   		SystemException se = new SystemException(e, 
	   				"com.manulife.pension.ps.service.report.tpadownload.dao.TedFTPServer",
	   				"static",
	   				"Static block failed for TedFTPServer. Could not load the email related properties");
			throw ExceptionHandlerUtility.wrap(se);
		} 	
	}	
	
	/**
	 * Retrieves the ManuMerge FTP server characteristics from the appropriate properties file.
	 *
	 */
	private TedFTPServer() throws Throwable {		
		super();	
	}
			 
	/**
	 * This method was created in VisualAge.
	 * @return java.lang.String
	 */
	public static String getPassword() {
		return password;
	}
	/**
	 * This method was created in VisualAge.
	 * @return java.lang.String
	 */
	public static String getServerName() {
		return serverName;
	}

	/**
	 * This method was created in VisualAge.
	 * @return java.lang.String
	 */
	public static String getUserid() {
		return userid;
	}
	/**
	 * This method was created in VisualAge.
	 * @param newValue java.lang.String
	 */

	/**
	 * Returns a String that represents the value of this object.
	 * @return a string representation of the receiver
	 */
	public String toString() {
		return super.toString();
	}
	
	public static String getTpaEDownloadFilesDir() {
		return tpaEDownloadFilesDir;
	}
	
	public static void setTpaEDownloadFilesDir(String tpaEDownloadFilesDir) {
		TedFTPServer.tpaEDownloadFilesDir = tpaEDownloadFilesDir;
	}

	public static String getTedFtpInd() {
		return tedFtpInd;
	}

	public static void setTedFtpInd(String tedFtpInd) {
		TedFTPServer.tedFtpInd = tedFtpInd;
	}
}
