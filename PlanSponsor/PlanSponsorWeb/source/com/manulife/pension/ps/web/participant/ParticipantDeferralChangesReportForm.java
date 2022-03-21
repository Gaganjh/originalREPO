package com.manulife.pension.ps.web.participant;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.util.render.DateRender;

public class ParticipantDeferralChangesReportForm extends ReportForm {
	
	public static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";
	public static final String FULL_COLUMNS = "full";
	public static final String HIDE_DIVISION = "hideDivision";
	public static final String HIDE_PAYROLL = "hidePayroll";
	public static final String HIDE_BOTH = "hideBoth";
	public static final String FIELD_TO_DATE = "toDate";
	public static final String FIELD_FROM_DATE = "fromDate";
	public static final int NUMBER_FULL_COLUMNS = 16;
	public static final int NUMBER_HIDE_ONE_COLUMN = 14;
	public static final int NUMBER_HIDE_TWO_COLUMN = 12;	
	
	private String toDate=null;
	private String baseToDate=null;
	private String fromDate=null;
	private String baseFromDate=null;
	private boolean isDirty = false;
	private boolean isSave = false;
	private boolean isSearch = false;
	private boolean isRefresh = false;
	private boolean isInitialSearch = false;
 	private String[] processedIndCheckbox = null;
 	private String[] changedProcessedIndCheckbox = null;
 	private boolean hasDivisionFeature = true;
 	private boolean hasPayrollNumberFeature = true;
 	private String typeOfPageLayout = "full";
	private int numberOfDisplayColumns = 16;
	private boolean unprocessedIndOnly = false;
	private boolean baseUnprocessedIndOnly = false;
	private boolean hasDetailRecords = false;
	private boolean unsavedDataReturn = false;
	private boolean printFriendly = false;	
	private String ext = null;
	private String sortField = null;
	private String sortDirection = null;
	private String baseSortField = null;
	private String baseSortDirection = null;
	private int basePageNumber = 1;
	private int pageNumber = 1;
	private String baseTask = null;
	private boolean deferralReportingInd = false;
	private boolean baseDeferralReportingInd = false;
	private boolean errorsOnPage = false;
	
	/**
	 * Constructor for ParicipantSummaryReportForm
	 */
	public ParticipantDeferralChangesReportForm() {
		super();
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
	 * Gets the baseTask
	 * @return Returns a String
	 */
	public String getBaseTask() {

		return baseTask;
	}
	
	
	public void setBaseTask(String baseTask){
		this.baseTask = baseTask;
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
	 * Gets the ext
	 * @return Returns a String
	 */
	public String getExt() {

		return ext;
	}


	public void setExt(String ext){
		this.ext = ext;
	}	

	/**
	 * Gets the sortField
	 * @return Returns a String
	 */
	public String getSortField() {

		return sortField;
	}


	public void setSortField(String sortField){
		//super.setSortField(sortField);
		this.sortField = sortField;
	}		


	/**
	 * Gets the sortDirection
	 * @return Returns a String
	 */
	public String getSortDirection() {

		return sortDirection;
	}


	public void setSortDirection(String sortDirection){
		//super.setSortDirection(sortDirection);
		this.sortDirection = sortDirection;
	}	

	/**
	 * Gets the baseSortField
	 * @return Returns a String
	 */
	public String getBaseSortField() {

		return baseSortField;
	}


	public void setBaseSortField(String baseSortField){
		this.baseSortField = baseSortField;
	}		


	/**
	 * Gets the baseSortDirection
	 * @return Returns a String
	 */
	public String getBaseSortDirection() {

		return baseSortDirection;
	}


	public void setBaseSortDirection(String baseSortDirection){
		this.baseSortDirection = baseSortDirection;
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
	 * Gets the isDirty
	 * @return Returns a boolean
	 */
	public boolean getIsDirty() {
		return isDirty;
	}


	/**
	 * Sets the isDirty
	 * @param isDirty The isDirty to set
	 */
	public void setIsDirty(boolean isDirty) {
		this.isDirty = isDirty; 
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
	 * Gets the unprocessedIndOnly
	 * @return Returns a boolean
	 */
	public boolean getUnprocessedIndOnly() {
		return unprocessedIndOnly;
	}



	/**
	 * Sets the unprocessedIndOnly
	 * @param unprocessedIndOnly The unprocessedIndOnly to set
	 */
	public void setUnprocessedIndOnly(boolean unprocessedIndOnly) {
		this.unprocessedIndOnly = unprocessedIndOnly; 
	}


	/**
	 * Gets the baseUnprocessedIndOnly
	 * @return Returns a boolean
	 */
	public boolean getBaseUnprocessedIndOnly() {
		return baseUnprocessedIndOnly;
	}



	/**
	 * Sets the baseUnprocessedIndOnly
	 * @param baseUnprocessedIndOnly The baseUnprocessedIndOnly to set
	 */
	public void setBaseUnprocessedIndOnly(boolean baseUnprocessedIndOnly) {
		this.baseUnprocessedIndOnly = baseUnprocessedIndOnly; 
	}


	/**
	 * Gets the isSave
	 * @return Returns a boolean
	 */
	public boolean getIsSave() {
		return isSave;
	}


	/**
	 * Sets the isSave
	 * @param isSave The isSave to set
	 */
	public void setIsSave(boolean isSave) {
		this.isSave = isSave; 
	}


	/**
	 * Gets the isRefresh
	 * @return Returns a boolean
	 */
	public boolean getIsRefresh() {
		return isRefresh;
	}


	/**
	 * Sets the isRefresh
	 * @param isRefresh The isRefresh to set
	 */
	public void setIsRefresh(boolean isRefresh) {
		this.isRefresh = isRefresh; 
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
	 * Gets the unsavedDataReturn
	 * @return Returns a boolean
	 */
	public boolean getUnsavedDataReturn() {
		return unsavedDataReturn; 
	}


	/**
	 * Sets the unsavedDataReturn
	 * @param unsavedDataReturn The unsavedDataReturn to set
	 */
	public void setUnsavedDataReturn(boolean unsavedDataReturn) {
		this.unsavedDataReturn = unsavedDataReturn; 
	}


	/**
	 * Gets the hasDetailRecords
	 * @return Returns a boolean
	 */
	public boolean getHasDetailRecords() {
		return hasDetailRecords;
	}




	/**
	 * Sets the hasDetailRecords
	 * @param hasDetailRecords The hasDetailRecords to set
	 */
	public void setHasDetailRecords(boolean hasDetailRecords) {
		this.hasDetailRecords = hasDetailRecords; 
	}

	/**
	 * Gets the processedIndCheckbox
	 * @return Returns a String[]
	 */
	public String[] getProcessedIndCheckbox() {
		
		//if page isRefresh - there is unsaved data on the page - display the changed Processed Indicator check boxes
		//if page is not Refresh - simply return the processed Indicator check boxes

		if (this.getIsRefresh()){
			return this.changedProcessedIndCheckbox;
		} else {
			return processedIndCheckbox;
		}	
			
	}
	
	/**
	 * Sets the processedIndCheckbox
	 * @param processedIndCheckbox The processedIndCheckbox to set
	 */
	public void setProcessedIndCheckbox(String[] processedIndCheckbox) {
		this.processedIndCheckbox = processedIndCheckbox;
	}

	/**
	 * Gets the changedProcessedIndCheckbox
	 * @return Returns a String[]
	 */
	public String[] getChangedProcessedIndCheckbox() {
		return changedProcessedIndCheckbox;
	}
	/**
	 * Sets the changedProcessedIndCheckbox
	 * @param changedProcessedIndCheckbox The changedProcessedIndCheckbox to set
	 */
	public void setChangedProcessedIndCheckbox(String[] changedProcessedIndCheckbox) {
		this.changedProcessedIndCheckbox = changedProcessedIndCheckbox;
	}
	
	
	/**
	 * method to initialize the processedIndCheckbox array for the multilist box control
	 * on the page.  The array is initialized with all uniqueIds that have a processInd of true
	 * The page will then use this array to initialize the checkboxes as checked
	**/
	public void initializeProcessedIndCheckboxArray(ArrayList processedIndicators){
	
		//create temporary String array to store process indicators from action 
		String[] indicators = new String[processedIndicators.size()];
		String uniqueId = null;
		for (int i = 0; i < processedIndicators.size(); i++){
			uniqueId = processedIndicators.get(i).toString();
			indicators[i] = uniqueId;
		}	

		//set the processedIndCheckbox array
		setProcessedIndCheckbox(indicators);

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
	 * Gets the printFriendly
	 * @return Returns a boolean
	 */
	public boolean getPrintFriendly() {
		return printFriendly;
	}

	/**
	 * Sets the printFriendly
	 * @param printFriendly The printFriendly to set
	 */
	public void setPrintFriendly(boolean printFriendly) {
		this.printFriendly = printFriendly;
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
	 * Gets the basePageNumber
	 * @return Returns a int
	 */
	public int getBasePageNumber() {

		return basePageNumber;
	}
	
	/**
	 * Sets the basePageNumber
	 * @param basePageNumber The basePageNumber to set
	 */	
	public void setBasePageNumber(int basePageNumber){
		this.basePageNumber = basePageNumber;
	
	}	


	/**
	 * Gets the pageNumber
	 * @return Returns a int
	 */
	public int getPageNumber() {

		return pageNumber;
	}
	
	/**
	 * Sets the pageNumber
	 * @param pageNumber The pageNumber to set
	 */	
	public void setPageNumber(int pageNumber){
		this.pageNumber = pageNumber;
	
	}	

	/**
	 * Gets the baseDeferralReportingInd
	 * @return Returns a boolean
	 */
	public boolean getBaseDeferralReportingInd() {

		return baseDeferralReportingInd;
	}
	
	/**
	 * Sets the baseDeferralReportingInd
	 * @param baseDeferralReportingInd The baseDeferralReportingInd to set
	 */	
	public void setBaseDeferralReportingInd(boolean baseDeferralReportingInd){
		this.baseDeferralReportingInd = baseDeferralReportingInd;
	
	}	


	/**
	 * Gets the deferralReportingInd
	 * @return Returns a boolean
	 */
	public boolean getDeferralReportingInd() {

		return deferralReportingInd;
	}
	
	/**
	 * Sets the deferralReportingInd
	 * @param deferralReportingInd The deferralReportingInd to set
	 */	
	public void setDeferralReportingInd(boolean deferralReportingInd){
		this.deferralReportingInd = deferralReportingInd;
	
	}	
	
	/**
	 * Gets the errorsOnPage
	 * @return Returns a boolean
	 */
	public boolean getErrorsOnPage() {

		return errorsOnPage;
	}
	
	/**
	 * Sets the errorsOnPage
	 * @param errorsOnPage The errorsOnPage to set
	 */	
	public void setErrorsOnPage(boolean errorsOnPage){
		this.errorsOnPage = errorsOnPage;
	
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



	public String getTypeOfPageLayout(){
		
		String result = null;
		
		
		
		if ((this.getHasDivisionFeature() == true) && (this.getHasPayrollNumberFeature() == true)) {
			this.setNumberOfDisplayColumns(NUMBER_FULL_COLUMNS);
			result = FULL_COLUMNS;

		}	
		if ((this.getHasDivisionFeature() == true) && (this.getHasPayrollNumberFeature() == false)) {
			this.setNumberOfDisplayColumns(NUMBER_HIDE_ONE_COLUMN);
			result = HIDE_PAYROLL;	
		}	
		if ((this.getHasDivisionFeature() == false) && (this.getHasPayrollNumberFeature() == true)) {
			this.setNumberOfDisplayColumns(NUMBER_HIDE_ONE_COLUMN);
			result = HIDE_DIVISION;	
		}	
		if ((this.getHasDivisionFeature() == false) && (this.getHasPayrollNumberFeature() == false)) {
			this.setNumberOfDisplayColumns(NUMBER_HIDE_TWO_COLUMN);
			result = HIDE_BOTH;
		}	
		
		if (result != null){
			return result;
		} else {
			return this.typeOfPageLayout;	
		}

	}	

	
	
	





	/**
	 * resets the form
	 */
	public void clear() {

		toDate=null;
		baseToDate=null;
		fromDate=null;
		baseFromDate=null;
		isDirty = false;
		isSave = false;
		isSearch = false;
		isRefresh = false;
		isInitialSearch = false;
 		processedIndCheckbox = null;
 		changedProcessedIndCheckbox = null;
 		hasDivisionFeature = true;
 		hasPayrollNumberFeature = true;
 		typeOfPageLayout = "full";
		numberOfDisplayColumns = 18;
		unprocessedIndOnly = false;
		baseUnprocessedIndOnly = false;
		hasDetailRecords = false;
		unsavedDataReturn = false;
		printFriendly = false;
		ext = null;
		sortField = null;
		sortDirection = null;
		baseSortField = null;
		baseSortDirection = null;		
		basePageNumber = 1;
		pageNumber = 1;
		baseTask = null;
		deferralReportingInd = false;
		baseDeferralReportingInd = false;
		errorsOnPage = false;
	}
	
	
	

 
	
	public void reset( HttpServletRequest request){
		unprocessedIndOnly = false;
		deferralReportingInd = false;
		//baseDeferralReportingInd = false;
	}	
	
	
	public void clearArrays() {
		processedIndCheckbox = null;
		changedProcessedIndCheckbox = null;
	}
}
