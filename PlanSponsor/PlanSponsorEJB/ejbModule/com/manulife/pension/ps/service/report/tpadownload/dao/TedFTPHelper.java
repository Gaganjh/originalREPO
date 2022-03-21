/*
 * Created on Apr 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.service.report.tpadownload.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;

import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.report.tpadownload.valueobject.TedHistoryFilesItem;
import com.manulife.util.ftp.FTPClient;
import com.manulife.util.ftp.FTPException;
/**
 * @author eldrima
 *
 * A TPA eDownload specific class that assists with FTP functions against the TEDownload FTP Server
 * using the MRL FTPClient class to perform all the FTP specific funcations.
 */
public class TedFTPHelper {
	
	private FTPClient ftpClient = null;
	private static final String COMMAND_FILE_NAME = "\\apps\\PlanSponsor\\scripts\\TEDFileOpearation.cmd";
	private static final String DRIVE_LETTER="Y";
	private boolean ftpIndicator;
	/**
	 * ManumergeFTPHelperNew constructor comment.
	 */
	
	public TedFTPHelper(boolean ftpIndicator) {
		this.ftpIndicator = ftpIndicator;
	}
	
	/**
	 * Connects to the ManuMerge FTP server specified by the TedFTPServer class.
	 *
	 * @exception com.manulife.util.ftp.FTPException The exception description.
	 */
	public void connect() throws Throwable {
		Process proc = null;
		try {
			if (ftpIndicator) {
				ftpClient = new FTPClient(TedFTPServer.getServerName(), TedFTPServer.getUserid(),
						TedFTPServer.getPassword());
			} else {
				try {
					proc = Runtime.getRuntime()
							.exec(new String[] { COMMAND_FILE_NAME, "MAP", DRIVE_LETTER, TedFTPServer.getUserid(),
									TedFTPServer.getPassword(), TedFTPServer.getServerName(), "", "" });
					String message = formatInputMsg(proc);
					if (message != null && message.trim().equals("")) {
						throw new Exception("Error in connect() to Manumerge FTP Server " + TedFTPServer.getServerName()
								+ ", FTPException error msg: " + formatErrorMessage(proc));
					}

				} finally {
					proc.destroy();
				}

			}
		} catch (FTPException e) {
			e.printStackTrace();
			throw new Exception("Error in connect() to Manumerge FTP Server " + TedFTPServer.getServerName()
					+ ", FTPException error msg: " + e.getMessage());
		}

	}

	private String formatInputMsg(Process proc) {
		BufferedReader stdInput = null;
		String readMsg = "";
		String message = "";
		try {
			stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			while ((readMsg = stdInput.readLine()) != null) {
				message = message + readMsg;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				stdInput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return message;
	}
	private String formatErrorMessage(Process proc) {
		String errorMsg = "";
		String errorMsg1 = "";
		BufferedReader stdError = null;
		try {
			 stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

			while ((errorMsg = stdError.readLine()) != null) {
				errorMsg1 = errorMsg1 + "\n" + errorMsg;
			}
			System.out.println("Error Message"+errorMsg1);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				stdError.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return errorMsg1.trim();

	}

	/**
	 * Disconnects from the ManuMerge FTP server.
	 *
	 * @exception com.manulife.util.ftp.FTPException The exception description.
	 */
	public void disconnect() {

		if (ftpIndicator) {
			ftpClient.logout();
		} else {
			Process proc = null;
			try {
				proc = Runtime.getRuntime().exec(new String[]{COMMAND_FILE_NAME, "DRIVEDEL", DRIVE_LETTER,  "", 
						"", "","", ""});
				String message = formatInputMsg(proc);
				if (message != null && message.trim().equals("")) {
					System.out.println(" Disconnect Exception :"+formatErrorMessage(proc));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				proc.destroy();	
			}
			
			
		}
	}
	/**
	 * Returns a directory listing in the form of a String array for the ManuMerge FTP server 
	 * directory specified.  The output is simply what it returned by the FTP command issued by the
	 * MRL FTPClient.
	 *
	 * @return String[]
	 *		Directory listing
	 *
	 * @param aDirectory java.lang.String
	 *		The fully qualified directory name for which a diretory listing is required
	 *
	 * @exception com.manulife.util.ftp.FTPException The exception description.
	 */
	public final String[] getDirectoryListing(String aDirectory) throws Throwable {

		String[] dirList = null;
		BufferedReader stdInput = null;
		Process proc = null;

		try {
			if (ftpIndicator) {

				if (ftpClient.setCurrentDirectory(aDirectory)) {
					dirList = ftpClient.getDirectoryListing();
				} else {
					// &&& Should log this failure somewhere so we can analyze
					// it. The userid
					// we're using could lose it's access rights to the
					// Manumerge FTP Server and
					// this message would be useful in determining this.

				}
			} else {
				try{
				proc = Runtime.getRuntime()
						.exec(new String[] { COMMAND_FILE_NAME, "DIR", DRIVE_LETTER, TedFTPServer.getUserid(),
								TedFTPServer.getPassword(), TedFTPServer.getServerName(), aDirectory.replace("/", "\\"), "" });
				stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

				// Read the output from the command
				String line = null;
				Vector<String> list = new Vector<String>();
				while ((line = stdInput.readLine()) != null) {
					list.addElement(line);
				}
				String[] result = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					result[i] = (String) list.elementAt(i);
				}
				if(list != null && list.size() == 0){
					System.out.println(" getDirectoryListing  path :"+aDirectory+"::"+  formatErrorMessage(proc));
				}
				return result;
				}finally{
					stdInput.close();
					proc.destroy();
				}

			}
		} catch (FTPException e) {
			e.printStackTrace();
			throw new Exception("Error in ManumergeFTPHelper.getDirectoryListing() on " + aDirectory
					+ ", FTPException error msg: " + e.getMessage());
		} 
		return dirList;
	}
		
	public boolean getFile(String directory, String fileName, OutputStream stream) throws Exception {
		boolean status = false;
		Process proc = null;

		try {
			if(ftpIndicator){
			ftpClient.setCurrentDirectory(directory);			
			status = ftpClient.getBinaryFile(fileName, stream);
			}else {
				try{
				  proc =Runtime.getRuntime().exec(new String[]{COMMAND_FILE_NAME, "GET", DRIVE_LETTER, TedFTPServer.getUserid(),
							TedFTPServer.getPassword(), TedFTPServer.getServerName(),directory.replace("/", "\\")+"\\"+fileName, ""});
				  if(readData(proc.getInputStream(),stream, 2048 * 1024)){
					  return true;					 
				  }else {
					  System.out.println("File retrive failed ,Path ::"+ directory.replace("/", "\\")+"\\"+fileName);
				  }
				 
				}finally{
					proc.destroy();
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}	
	
	
	/**
	 * Checks if a connection can be established with the ManuMerge FTP server.
	 *
	 * @return boolean
	 *		Indicates if successful connection with the FTP server could be established
	 */
	public boolean isServerConnectionOK() {
		Process proc = null;
		try {
			if (ftpIndicator) {
				ftpClient = new FTPClient(TedFTPServer.getServerName(), TedFTPServer.getUserid(),
						TedFTPServer.getPassword());
			} else {

				try {
					proc = Runtime.getRuntime()
							.exec(new String[] { COMMAND_FILE_NAME, "MAP", DRIVE_LETTER, TedFTPServer.getUserid(),
									TedFTPServer.getPassword(), TedFTPServer.getServerName(), "", "" });
					String message = formatInputMsg(proc);
					if (message != null && message.trim().equals("")) {
						formatErrorMessage(proc);
						return false;
					}
					
					/*if (errorMsg != null && !errorMsg.equals("")) {
						return false;
					}*/
				} catch (IOException e) {

					e.printStackTrace();
					return false;
				} finally {
					proc.destroy();
					
				}

			}
		} catch(FTPException e) {		      
			return false;
		}
		return true;
	}
	/**
	 * Parses out the required file details contained in the String passed in as input, which is 
	 * one of the elements returned by the getDirectoryListing method.  These file details are
	 * then passed back in the class HistoryFileDetails.
	 *
	 * @return com.manulife.pension.lp.bos.HistoryFileDetails
	 *		Will be null if the input fileDetails are not associated with a .ZIP file.
	 *
	 * @param fileDetails java.lang.String
	 *		Contains the details for a single file as retrieved by the getDirectoryListing method.
	 *		It is assumed that the format of this string is as follows:
	 *				04-13-99  09:24PM                 6806 5230019991.ZIP
	 *				modDate  modTime			  fileSize filename
	 */
	public TedHistoryFilesItem parseOutFileDetails(String fileDetails) {
		
		TedHistoryFilesItem historyFile = null;
		
		Vector fileDetailTokens = new Vector(10);
		String fileName = "";
		String modDate = "";
		String fileSize = "";
		
		if (fileDetails.length() == 0) {
			// A blank line, not interested in it.
			return historyFile;
		}
		
		StringTokenizer st = new StringTokenizer(fileDetails," ");
		while (st.hasMoreElements()) {
			fileDetailTokens.addElement(st.nextToken());
		} 
		
		// Determine the filename.
		fileName = (String) fileDetailTokens.lastElement();		// Filename is always last element.	
		// fileName Must be of the format: cccccyyyyq.ZIP
		//						where	ccccc = contract number (numeric)
		//								yyyy  = year			(numeric)
		//								q     = quarter			(numeric)
		if ( (fileName.length() < 14) ||
				( !(fileName.substring((fileName.length() - 3), fileName.length()).equalsIgnoreCase("ZIP")))) {	// Filename doesn't end in ZIP.
			// Not a .ZIP file, not interested in it.
			return historyFile;
		}
		String fileNameNoExtension = fileName.substring(0,(fileName.length() - 4));
		// fileName without the extention must be numeric
		try {
			long nameChk = Long.parseLong(fileNameNoExtension);
		} catch (Exception e) {
			return historyFile;
		}
		historyFile = new TedHistoryFilesItem();
		historyFile.setFileName(fileNameNoExtension);
		if(fileNameNoExtension.length() == Constants.FILE_NAME_LENGTH_1) {
			historyFile.setYear(new Integer(fileNameNoExtension.substring(Constants.YEAR_START_INDEX_1, Constants.YEAR_START_INDEX_1+4)));
			historyFile.setQuarter(new Integer(fileNameNoExtension.substring(Constants.QUARTER_START_INDEX_1, Constants.QUARTER_START_INDEX_1+1)));
		}
		if(fileNameNoExtension.length() == Constants.FILE_NAME_LENGTH_2) {
			historyFile.setYear(new Integer(fileNameNoExtension.substring(Constants.YEAR_START_INDEX_2, Constants.YEAR_START_INDEX_2+4)));
			historyFile.setQuarter(new Integer(fileNameNoExtension.substring(Constants.QUARTER_START_INDEX_2, Constants.QUARTER_START_INDEX_2+1)));
		}
		if(fileNameNoExtension.length() == Constants.FILE_NAME_LENGTH_3) {
			historyFile.setYear(new Integer(fileNameNoExtension.substring(Constants.YEAR_START_INDEX_3, Constants.YEAR_START_INDEX_3+4)));
			historyFile.setQuarter(new Integer(fileNameNoExtension.substring(Constants.QUARTER_START_INDEX_3, Constants.QUARTER_START_INDEX_3+1)));
			}
		// Determine the file's modification date and store it as the last run date of the Manumerge file.
		modDate = (String) fileDetailTokens.elementAt(0);
		int DateYear = Integer.parseInt(modDate.substring(6));
		int DateMonth = Integer.parseInt(modDate.substring(0,2));
		int DateDay = Integer.parseInt(modDate.substring(3,5));
		//Handling 4 digit format  year 
		if(String.valueOf(DateYear).length() <= 2 ){
		if (DateYear < 90) {		// Make year Y2K compliant.
			DateYear = DateYear + 2000;
		} else {
			DateYear = DateYear + 1900;
		}
		}
		Date lastRunDate = (new GregorianCalendar(DateYear, (DateMonth - 1), DateDay)).getTime();	//	Note: the MONTH value supplied should be 0-based.
		historyFile.setLastRunDate(lastRunDate);
		
		// Determine the file's size.
		if (!ftpIndicator) {
			fileSize = (String) fileDetailTokens.elementAt(3);
			String regex = "(?<=[\\d])(,)(?=[\\d])";
			historyFile.setFileSize(Integer.parseInt(Pattern.compile(regex).matcher(fileSize).replaceAll("")));
		} else {
			fileSize = (String) fileDetailTokens.elementAt(2);
			historyFile.setFileSize(Integer.parseInt(fileSize));
		}
		return historyFile;
	}

	public boolean readData(InputStream in, OutputStream out, int size) {
		byte[] buffer = new byte[4096];
		int done = 0;
		while (done < size) {
			int read;
			try {
				read = in.read(buffer);

				if (read == -1) {
					break;
				}
				out.write(buffer, 0, read);
				done += read;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return done > 0;
	}

	
}
