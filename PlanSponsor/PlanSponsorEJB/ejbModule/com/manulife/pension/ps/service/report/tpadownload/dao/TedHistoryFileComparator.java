/*
 * Created on May 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.service.report.tpadownload.dao;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import com.manulife.pension.ps.service.report.tpadownload.valueobject.TedHistoryFilesItem;

/**
 * @author eldrima
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TedHistoryFileComparator implements Comparator, Serializable {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	private String sortDirection;
	private String sortField;
	
	public TedHistoryFileComparator(String sortField, String sortDirection) {
		this.sortField = sortField;
		this.sortDirection = sortDirection;
	}
	
	public int compare(Object item1, Object item2) {
		TedHistoryFilesItem file1 = (TedHistoryFilesItem) item1;
		TedHistoryFilesItem file2 = (TedHistoryFilesItem) item2;
		
		Date dateToCompare1 = null;
		Date dateToCompare2 = null;
		
		if (getSortField().equalsIgnoreCase(TedHistoryFilesItem.SORT_FIELD_LAST_RUN_DATE)) {
			dateToCompare1 = file1.getLastRunDate();
			dateToCompare2 = file2.getLastRunDate();
		} else {
			dateToCompare1 = file1.getQuarterEndDate();
			dateToCompare2 = file2.getQuarterEndDate();			
		}
		int result = compareDate(dateToCompare1, dateToCompare2);

		if (result == 0) {
			// This is the same as the equals method of TedHistoryFilesItem in order
			// for set sorting to work correctly.  See javadoc re: Comparator for details.
			return file2.getQuarterEndDate().compareTo(file1.getQuarterEndDate()); 
		}
		
		return result;
	}
	//final int -1  = -1;
	//final int 0 =  0;
	//final int 1  =  1;
	
	public int compareDate(Date date1, Date date2) {
		if (getSortDirection().equalsIgnoreCase("ASC")) {
			if (date1.after(date2)) {
				return 1;
			} else if (date1.before(date2)) {
				return -1;
			} else {
				return 0;
			}
		} else {
			if (date2.after(date1)) {
				return 1;
			} else if (date2.before(date1)) {
				return -1;
			} else {
				return 0;
			}			
		}		
	}
	
	public String getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}
	public String getSortField() {
		return sortField;
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

}
