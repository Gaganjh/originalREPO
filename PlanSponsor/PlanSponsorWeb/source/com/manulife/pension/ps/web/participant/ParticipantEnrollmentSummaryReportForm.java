package com.manulife.pension.ps.web.participant;
 import java.util.Calendar;
import java.util.Date;

import com.manulife.pension.platform.web.util.Ssn;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.util.render.DateRender;
 public class ParticipantEnrollmentSummaryReportForm extends ReportForm {

	public static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";
	public static final String FULL_COLUMNS = "full";
	public static final String HIDE_DIVISION = "hideDivision";
	public static final String HIDE_PAYROLL = "hidePayroll";
	public static final String HIDE_BOTH = "hideBoth";
	public static final String FIELD_TO_DATE = "toDate";
	public static final String FIELD_FROM_DATE = "fromDate";
	public static final String FIELD_SSN = "ssn";
	public static final String FIELD_LAST_NAME = "lastName";
	
	public static final int NUMBER_FULL_COLUMNS_WITH_WEB = 18;
	public static final int NUMBER_HIDE_ONE_COLUMN_WITH_WEB = 16;
	public static final int NUMBER_HIDE_TWO_COLUMN_WITH_WEB = 14;	

    public static final int NUMBER_FULL_COLUMNS_NO_WEB_AUTO = 16;
    public static final int NUMBER_HIDE_ONE_COLUMN_NO_WEB_AUTO = 14;
    public static final int NUMBER_HIDE_TWO_COLUMN_NO_WEB_AUTO = 12;
    
	public static final int NUMBER_FULL_COLUMNS_NO_WEB = 14;
	public static final int NUMBER_HIDE_ONE_COLUMN_NO_WEB = 12;
	public static final int NUMBER_HIDE_TWO_COLUMN_NO_WEB = 10;	
	
	private String toDate=null;
	private String baseToDate=null;
	private String fromDate=null;
	private String baseFromDate=null;
 	private boolean hasDivisionFeature = true;
 	private boolean hasPayrollNumberFeature = true;
 	private boolean hasInternetEnrollments = true;
    private boolean hasAutoEnrollments = true;
 	private String typeOfPageLayout = "full";
	private int numberOfDisplayColumns = 20;
	private String namePhrase = null;
	private Ssn ssn;
	private String ssnOne;
	private String ssnTwo;
	private String ssnThree;
	private boolean isInitialSearch = true;
	private boolean isSearch = false;
	private String calendarStartDate;
	private String calendarEndDate;
	private String printType = null;
	
	/**
	 * Constructor for ParticipantEnrollmentSummaryReportForm
	 */
	public ParticipantEnrollmentSummaryReportForm() {
		super();
	}


	/**
	 * Gets the toDate
	 * @return Returns a String
	 */
	public String getToDate() {
		//determine the default to Date - current date
		Calendar calToDate = Calendar.getInstance();
		Date dtToDate = calToDate.getTime();
		if (toDate == null) {
			return DateRender.formatByPattern(dtToDate, "", FORMAT_DATE_SHORT_MDY);
		} else return toDate;
	}
	/**
	 * Sets the toDate
	 * @param toDate The toDate to set
	 */
	public void setToDate(String toDate) {
		this.toDate = toDate; 
	}


	/**
	 * Gets the baseToDate
	 * @return Returns a String
	 */
	public String getBaseToDate() {
		return baseToDate;
	}
	public void setBaseToDate(String baseToDate){
		this.baseToDate = baseToDate;
	}	


	/**
	 * Gets the fromDate
	 * @return Returns a String
	 */
	public String getFromDate() {
		//determine default date - 1 month less than current
		Calendar calFromDate = Calendar.getInstance();
		calFromDate.add(Calendar.MONTH, -1);
		Date dtFromDate = calFromDate.getTime();
		if (fromDate == null) {
			return DateRender.formatByPattern(dtFromDate, "", FORMAT_DATE_SHORT_MDY);
		} else return fromDate;
				
	}
	/**
	 * Sets the fromDate
	 * @param fromDate The fromDate to set
	 */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate; 
	}


	/**
	 * Gets the baseFromDate
	 * @return Returns a String
	 */
	public String getBaseFromDate() {
		return baseFromDate;
	}
	
	
	public void setBaseFromDate(String baseFromDate){
		this.baseFromDate = baseFromDate;
	}	


	/**
	 * Gets the hasDivisionFeature
	 * @return Returns a boolean
	 */
	public boolean getHasDivisionFeature() {
		return hasDivisionFeature;
	}
	/**
	 * Sets the hasDivisionFeature
	 * @param hasDivisionFeature The hasDivisionFeature to set
	 */
	public void setHasDivisionFeature(boolean hasDivisionFeature) {
		this.hasDivisionFeature = hasDivisionFeature;
	}

	
	/**
	 * Gets the hasPayrollNumberFeature
	 * @return Returns a boolean
	 */
	public boolean getHasPayrollNumberFeature() {
		return hasPayrollNumberFeature;
	}
	/**
	 * Sets the hasPayrollNumberFeature
	 * @param hasPayrollNumberFeature The hasPayrollNumberFeature to set
	 */
	public void setHasPayrollNumberFeature(boolean hasPayrollNumberFeature) {
		this.hasPayrollNumberFeature = hasPayrollNumberFeature;
	}


	/**
	 * Gets the hasInternetEnrollments
	 * @return Returns a boolean
	 */
	public boolean getHasInternetEnrollments() {
		return hasInternetEnrollments;
	}
	/**
	 * Sets the hasInternetEnrollments
	 * @param hasInternetEnrollments The hasInternetEnrollments to set
	 */
	public void setHasInternetEnrollments(boolean hasInternetEnrollments) {
		this.hasInternetEnrollments = hasInternetEnrollments;
	}
	
    /**
     * Gets hasAutoEnrollments
     * @return boolean
     */
	public boolean getHasAutoEnrollments() {
        return hasAutoEnrollments;
    }

	/**
     * Sets hasAutoEnrollments
     * @param hasAutoEnrollments
	 */
    public void setHasAutoEnrollments(boolean hasAutoEnrollments) {
        this.hasAutoEnrollments = hasAutoEnrollments;
    }


	public String getTypeOfPageLayout(){
		
		String result = null;
        
		if (this.getHasInternetEnrollments() == true){
			if ((this.getHasDivisionFeature() == true) && (this.getHasPayrollNumberFeature() == true)) {
				this.setNumberOfDisplayColumns(NUMBER_FULL_COLUMNS_WITH_WEB);
				result = FULL_COLUMNS;
			}	
			if ((this.getHasDivisionFeature() == true) && (this.getHasPayrollNumberFeature() == false)) {
				this.setNumberOfDisplayColumns(NUMBER_HIDE_ONE_COLUMN_WITH_WEB);
				result = HIDE_PAYROLL;	
			}	
			if ((this.getHasDivisionFeature() == false) && (this.getHasPayrollNumberFeature() == true)) {
				this.setNumberOfDisplayColumns(NUMBER_HIDE_ONE_COLUMN_WITH_WEB);
				result = HIDE_DIVISION;	
			}	
			if ((this.getHasDivisionFeature() == false) && (this.getHasPayrollNumberFeature() == false)) {
				this.setNumberOfDisplayColumns(NUMBER_HIDE_TWO_COLUMN_WITH_WEB);
				result = HIDE_BOTH;
			}
		} else if (this.getHasAutoEnrollments() == false){
			
			if ((this.getHasDivisionFeature() == true) && (this.getHasPayrollNumberFeature() == true)) {
				this.setNumberOfDisplayColumns(NUMBER_FULL_COLUMNS_NO_WEB);
				result = FULL_COLUMNS;
			}	
			if ((this.getHasDivisionFeature() == true) && (this.getHasPayrollNumberFeature() == false)) {
				this.setNumberOfDisplayColumns(NUMBER_HIDE_ONE_COLUMN_NO_WEB);
				result = HIDE_PAYROLL;	
			}	
			if ((this.getHasDivisionFeature() == false) && (this.getHasPayrollNumberFeature() == true)) {
				this.setNumberOfDisplayColumns(NUMBER_HIDE_ONE_COLUMN_NO_WEB);
				result = HIDE_DIVISION;	
			}	
			if ((this.getHasDivisionFeature() == false) && (this.getHasPayrollNumberFeature() == false)) {
				this.setNumberOfDisplayColumns(NUMBER_HIDE_TWO_COLUMN_NO_WEB);
				result = HIDE_BOTH;
			}			
			
		} else {
            if ((this.getHasDivisionFeature() == true) && (this.getHasPayrollNumberFeature() == true)) {
                this.setNumberOfDisplayColumns(NUMBER_FULL_COLUMNS_NO_WEB_AUTO);
                result = FULL_COLUMNS;
            }   
            if ((this.getHasDivisionFeature() == true) && (this.getHasPayrollNumberFeature() == false)) {
                this.setNumberOfDisplayColumns(NUMBER_FULL_COLUMNS_NO_WEB_AUTO);
                result = HIDE_PAYROLL;  
            }   
            if ((this.getHasDivisionFeature() == false) && (this.getHasPayrollNumberFeature() == true)) {
                this.setNumberOfDisplayColumns(NUMBER_FULL_COLUMNS_NO_WEB_AUTO);
                result = HIDE_DIVISION; 
            }   
            if ((this.getHasDivisionFeature() == false) && (this.getHasPayrollNumberFeature() == false)) {
                this.setNumberOfDisplayColumns(NUMBER_HIDE_ONE_COLUMN_NO_WEB_AUTO);
                result = HIDE_BOTH;
            } 
        }
		
		if (result != null){
			return result;
		} else {
			return this.typeOfPageLayout;	
		}

	}	


	 /* Gets the numberOfDisplayColumns
	 * @return Returns a int
	 */
	public int getNumberOfDisplayColumns() {

		return numberOfDisplayColumns;
	}
	/**
	 * Sets the numberOfDisplayColumns
	 * @param numberOfDisplayColumns The numberOfDisplayColumns to set
	 */	
	public void setNumberOfDisplayColumns(int numberOfDisplayColumns){
		this.numberOfDisplayColumns = numberOfDisplayColumns;
	}	


	/**
	 * Gets the namePhrase
	 * @return Returns a String
	 */
	public String getNamePhrase() {
		return namePhrase;
	}
	/**
	 * Sets the namePhrase
	 * @param namePhrase The namePhrase to set
	 */
	public void setNamePhrase(String namePhrase) {
		this.namePhrase = namePhrase;
	}


	/**
	 * Gets the ssn
	 * @return Returns a Ssn
	 */
	public Ssn getSsn() {
		Ssn ssnTemp = new Ssn();
		ssnTemp.setDigits(0,ssnOne);
		ssnTemp.setDigits(1,ssnTwo);
		ssnTemp.setDigits(2,ssnThree);
		return ssnTemp;
	}
	
	
	/**
	 * Gets the ssnOne
	 * @return Returns a String
	 */
	public String getSsnOne() {
		return ssnOne;
	}
	/**
	 * Sets the ssnOne
	 * @param ssnOne The ssnOne to set
	 */
	public void setSsnOne(String ssnOne) {
		this.ssnOne = ssnOne;
	}



	/**
	 * Gets the ssnTwo
	 * @return Returns a String
	 */
	public String getSsnTwo() {
		return ssnTwo;
	}
	/**
	 * Sets the ssnTwo
	 * @param ssnTwo The ssnTwo to set
	 */
	public void setSsnTwo(String ssnTwo) {
		this.ssnTwo = ssnTwo;
	}



	/**
	 * Gets the ssnThree
	 * @return Returns a String
	 */
	public String getSsnThree() {
		return ssnThree;
	}
	/**
	 * Sets the ssnThree
	 * @param ssnThree The ssnThree to set
	 */
	public void setSsnThree(String ssnThree) {
		this.ssnThree = ssnThree;
	}


	/**
	 * Gets the isSearch
	 * @return Returns a boolean
	 */
	public boolean getIsSearch() {
		return isSearch;
	}



	/**
	 * Sets the isSearch
	 * @param isSearch The isSearch to set
	 */
	public void setIsSearch(boolean isSearch) {
		this.isSearch = isSearch; 
	}


	/**
	 * Gets the isInitialSearch
	 * @return Returns a boolean
	 */
	public boolean getIsInitialSearch() {
		return isInitialSearch;
	}


	/**
	 * Sets the isInitialSearch
	 * @param isInitialSearch The isInitialSearch to set
	 */
	public void setIsInitialSearch(boolean isInitialSearch) {
		this.isInitialSearch = isInitialSearch; 
	}

	/**
	 * @return Returns the calendarEndDate.
	 */
	public String getCalendarEndDate() {
		return this.toDate;
	}
	/**
	 * @param calendarEndDate The calendarEndDate to set.
	 */
	public void setCalendarEndDate(String calendarEndDate) {
		this.calendarEndDate = calendarEndDate;
	}
	/**
	 * @return Returns the calendarStartDate.
	 */
	public String getCalendarStartDate() {
		return this.fromDate;
	}
	/**
	 * @param calendarStartDate The calendarStartDate to set.
	 */
	public void setCalendarStartDate(String calendarStartDate) {
		this.calendarStartDate = calendarStartDate;
	}

	/**
	 * Gets the printType
	 * @return Returns a String
	 */
	public String getPrintType() {
		return printType;
	}
	/**
	 * Sets the printType
	 * @param printType The printType to set
	 */
	public void setPrintType(String printType) {
		this.printType = printType;
	}	
	
	
	/**
	 * resets the form
	 */
	public void clear() {

		toDate=null;
		baseToDate=null;
		fromDate=null;
		baseFromDate=null;
 		hasDivisionFeature = true;
 		hasPayrollNumberFeature = true;
 		hasInternetEnrollments = true;
 		typeOfPageLayout = "full";
		numberOfDisplayColumns = 20;
		namePhrase = null;
		ssn = null;
		ssnOne = null;
		ssnTwo = null;
		ssnThree = null;
		isInitialSearch = true;
	 	isSearch = false;
	 	printType = null;
	}
	
}

