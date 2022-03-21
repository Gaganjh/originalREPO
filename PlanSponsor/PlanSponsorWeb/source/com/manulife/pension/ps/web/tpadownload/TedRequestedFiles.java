/*
 * Created on May 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.tpadownload;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * @author eldrima
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TedRequestedFiles implements Serializable {

	private ArrayList fileListing = new ArrayList();
	private String directory = "\\CONTRACT";
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe
	private FastDateFormat sdf = FastDateFormat.getInstance("MM-dd-yyyy-kk-mm");
	// TED_MM-DD-YYYY-HH-MM.zip
	
	/**
	 * @return Returns the directory.
	 */
	public String getDirectory() {
		return directory;
	}
	/**
	 * @param directory The directory to set.
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	/**
	 * @return Returns the fileListing.
	 */
	public ArrayList getFileListing() {
		return fileListing;
	}
	/**
	 * @param fileListing The fileListing to set.
	 */
	public void setFileListing(ArrayList fileListing) {
		this.fileListing = fileListing;
	}
	/**
	 * @return Returns the zipFileName.
	 */
	public String getZipFileName() {
		Date now = new Date(System.currentTimeMillis());
		String date = sdf.format(now);
		// TED_MM-DD-YYYY-HH-MM.zip
		String filename = "TED_" + date + ".ZIP"; 
		return filename;
	}
}
