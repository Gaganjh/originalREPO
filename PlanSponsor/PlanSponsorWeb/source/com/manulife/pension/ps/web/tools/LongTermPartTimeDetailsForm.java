package com.manulife.pension.ps.web.tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringEscapeUtils;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Message;
import com.manulife.pension.ps.service.report.submission.valueobject.LongTermPartTimeDetailsReportData;
import com.manulife.pension.ps.service.submission.util.SubmissionErrorHelper;
import com.manulife.pension.ps.service.submission.valueobject.ReportDataErrors;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.util.CloneableForm;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.submission.SubmissionError;
import com.manulife.pension.util.content.manager.ContentCacheManager;

public class LongTermPartTimeDetailsForm extends ReportForm implements CloneableForm {
	
	private static final long serialVersionUID = 4075507295709337482L;
	private static final String NEW_DATE = "new Date(";
	private static final String RIGHT_PAR = ")";
	private static final String COMMA = ", ";
	private static final String DECIMAL_ZERO = "0.00";
	private static final String ID_99 = "99";
	private static final String NEW_ARRAY = "new Array(";
	private static final String COMMA_QUOTE = ", \"";
	private static final String QUOTE = "\"";
	private static final String END_BR = "]']\"";
	private static final String _ROW_ = "].row[";
	private static final String ELEMENTS__LTPTCOLUMNS = "elements['longTermPartTimeReportColumns[";
	private static final String UPLOADFORMOBJ = "longTermPartTimeDetailsForm.";
	private static String ERROR_TYPE_ERROR = "error";
	private static String ERROR_TYPE_WARN = "warn";
	private static String ZERO_VALUE = DECIMAL_ZERO;
	private static String SAVE_TASK = "save";
	private static String SUBMIT_TASK = "submit";

	private boolean isEditFunctionAvailable = false;
	
	private CloneableForm clonedForm;
	private String subNo;
	
	private boolean isAllowedView = false;
	private boolean isNoPermission = true;
    private boolean maskSSN = false;
	private LongTermPartTimeDetailsReportData theReport = new LongTermPartTimeDetailsReportData(new ReportCriteria(""),0);
	
	private Map errorMap;
	private boolean isViewMode = false;
	private boolean isShowConfirmDialog = false;
    private boolean isIgnoreDataCheckWarnings = false;
	private boolean isHasChanged = false;
	private String calendarStartDate;
	private String calendarEndDate;
    private String futurePlanYearEnd;
	
	private List<RowVal> longTermPartTimeReportColumns = new ArrayList<RowVal>();
	private Map employerIds = new HashMap();
	private Map recordNumbers = new HashMap();
	private List longTermPartTimeAssessmentYears = new ArrayList();
	private List<Boolean> deleteBoxes = new ArrayList<Boolean>();
	private String warningMessage = "";
	private int myOwnPageNumber = 1;
	private boolean resubmit = false;
	private String forwardFromSave = "";
	
    public class RowVal implements Serializable {
		
		private List row;
		private List rowId;
		
		public List getRow() {
			if (row == null) {
				row=new ArrayList();
			}
			if(row.size() < theReport.getDetails().size()){
				for(int i = row.size();i < theReport.getDetails().size();i++){
					row.add(ZERO_VALUE);
				}
				
			}
			return row;
		}
		
		public List getRowId() {
			if (rowId == null) {
				rowId=new ArrayList();
			}
			if(rowId.size() < theReport.getDetails().size()){
				for(int i = rowId.size();i < theReport.getDetails().size();i++){
					rowId.add(ZERO_VALUE);
				}
				
			}
			return rowId;
		}

		public void setRow(List row) {
			this.row = row;
		}

		public void setRowId(List rowId) {
			this.rowId = rowId;
		}
		
		public String getRow(int index) {
			if (row == null) {
				return ZERO_VALUE;
			}
			if(row.size() < theReport.getDetails().size()){
				for(int i = row.size();i < theReport.getDetails().size();i++){
					row.add(ZERO_VALUE);
				}
				
			}
			String v = (String) row.get(new Integer(index));
			if (v == null)
				return ZERO_VALUE;
			else
				return v;
		}

		public void setRow(int index, Integer value) {
			if (row == null) {
				row = new ArrayList();
			}
			row.add(new Integer(index), value);

		}
		
		public String getRowId(int index) {
			if (rowId == null) {
				return ID_99;
			}

			String v = (String) rowId.get(new Integer(index));
			if (v == null)
				return ID_99;
			else
				return v;
		}

		public void setRowId(int index, Integer value) {
			if (rowId == null) {
				rowId = new ArrayList();
			}
			rowId.add(new Integer(index), value);

		}
		/**
		 * @return Returns the row.
		 */
		public List getRowMap() {
			return row;
		}
		/**
		 * @param row The row to set.
		 */
		public void setRowMap(List row) {
			this.row = row;
		}
		/**
		 * @return Returns the rowId.
		 */
		public List getRowIdMap() {
			return rowId;
		}
		/**
		 * @param rowId The rowId to set.
		 */
		public void setRowIdMap(List rowId) {
			this.rowId = rowId;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#clone()
		 */
		protected Object clone() {
			RowVal newRowVal = new RowVal();
			
			// rowIds
			if (getRowIdMap() != null) {
				Iterator its = getRowIdMap().iterator();
				List rowIdMap = new ArrayList();
				while (its.hasNext()) {
					String it =  (String)its.next();
					rowIdMap.add(it);
				}
				newRowVal.setRowIdMap(rowIdMap);
			}

			// rows
			if (getRowMap() != null) {
				Iterator its = getRowMap().iterator();
				List rowMap = new ArrayList();
				while (its.hasNext()) {
					String it = (String) its.next();
					rowMap.add(it);
				}
				newRowVal.setRowMap(rowMap);
			}

			return newRowVal;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.util.CloneableForm#clear(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void clear( HttpServletRequest request) {
        super.clear();
		this.clonedForm = null;
		this.isAllowedView = false;
		this.errorMap = null;
		this.isViewMode = false;
		this.isShowConfirmDialog = false;
        this.isIgnoreDataCheckWarnings = false;
		this.calendarStartDate = null;
		this.calendarEndDate = null;
		this.longTermPartTimeReportColumns = new ArrayList();
		this.employerIds = new HashMap();
		this.recordNumbers = new HashMap();
		this.deleteBoxes = new ArrayList();
		this.warningMessage = "";
		this.isHasChanged = false;
		this.resubmit = false;
		this.forwardFromSave = "";

	}

	public CloneableForm getClonedForm() {
		return clonedForm;
	}

	public Object clone() {
		LongTermPartTimeDetailsForm myClone = new LongTermPartTimeDetailsForm();
		
		// clone the LongTermPartTimeReport columns
		if (longTermPartTimeReportColumns != null) {
			Iterator its = getLongTermPartTimeReportColumnsMap().iterator();
			List newLongTermPartTimeReportColumns = new ArrayList();
			while (its.hasNext()) {
				Object object = (Object) its.next();
				newLongTermPartTimeReportColumns.add(((RowVal) object).clone());
			}
			myClone.setLongTermPartTimeReportColumnsMap(newLongTermPartTimeReportColumns);
		}
		
		
		// delete checkboxes
		if (getDeleteBoxesMap() != null) {
			Iterator its = getDeleteBoxesMap().iterator();
			List<Boolean> newDeleteBoxes = new ArrayList<Boolean>();
			while (its.hasNext()) {
				Boolean it = (Boolean) its.next();
				newDeleteBoxes.add(it);
			}
			myClone.setDeleteBoxesMap(newDeleteBoxes);
		}
        
        // LongTermPartTime Assessment Years
        if (getLongTermPartTimeAssessmentYearsMap() != null) {
            Iterator its = getLongTermPartTimeAssessmentYearsMap().iterator();
            List newLongTermPartTimeAssessmentYears = new ArrayList();
            while (its.hasNext()) {
            	String assessmentYear = (String) its.next();
                newLongTermPartTimeAssessmentYears.add(assessmentYear);
            }
            myClone.setLongTermPartTimeAssessmentYearsMap(newLongTermPartTimeAssessmentYears);
        }
        
		return myClone;
	}

	public void storeClonedForm() {
		clonedForm = (CloneableForm) clone();
	}
	/**
	 * @return Returns the isAllowedView.
	 */
	public boolean isAllowedView() {
		return isAllowedView;
	}
	/**
	 * @param isAllowedView The isAllowedView to set.
	 */
	public void setAllowedView(boolean isAllowedView) {
		this.isAllowedView = isAllowedView;
	}
	/**
	 * @return Returns the subNo.
	 */
	public String getSubNo() {
		return subNo;
	}
	/**
	 * @param subNo The subNo to set.
	 */
	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}
	
	public void clearErrors() {
		errorMap = null;
	}
	
	public void addErrorCollection(Integer sourceRecordNo, Collection errors) {
		if (errorMap == null) {
			errorMap = new HashMap();
			errorMap.put(sourceRecordNo,errors);
		}
	}
	
	/**
	 * @return Returns the isViewMode.
	 */
	public boolean isViewMode() {
		return isViewMode;
	}
	/**
	 * @param isViewMode The isViewMode to set.
	 */
	public void setViewMode(boolean isViewMode) {
		this.isViewMode = isViewMode;
	}

	/**
	 * @return Returns the theReport.
	 */
	public LongTermPartTimeDetailsReportData getTheReport() {
		return theReport;
	}
	/**
	 * @param theReport The theReport to set.
	 */
	public void setTheReport(LongTermPartTimeDetailsReportData theReport) {
		this.theReport = theReport;
	}

	/**
	 * @return Returns the calendarEndDate.
	 */
	public String getCalendarEndDate() {
		return calendarEndDate;
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
		return calendarStartDate;
	}
	/**
	 * @param calendarStartDate The calendarStartDate to set.
	 */
	public void setCalendarStartDate(String calendarStartDate) {
		this.calendarStartDate = calendarStartDate;
	}
	
	/**
	 * @return Returns the errorMap.
	 */
	public Map getErrorMap() {
		return errorMap;
	}
	/**
	 * @param errorMap The errorMap to set.
	 */
	public void setErrorMap(Map errorMap) {
		this.errorMap = errorMap;
	}
	/**
	 * @return Returns the error type.
	 */
	public boolean getErrorType(String field, int row, String severity) {
		if (
				theReport == null ||
				theReport.getLongTermPartTimeData() == null ||
				theReport.getLongTermPartTimeData().getReportDataErrors() == null
		)	return false;
		
		Iterator errorsIt = theReport.getLongTermPartTimeData().getReportDataErrors().getErrors().iterator();
		
		while (errorsIt.hasNext()) {
			SubmissionError error = (SubmissionError) errorsIt.next();
			if (error.getField().equals(field) && error.getRowNumber() == row) {
				if (SubmissionErrorHelper.isError(error) && ERROR_TYPE_ERROR.equals(severity))
					return true;
				else if (SubmissionErrorHelper.isWarning(error) && ERROR_TYPE_WARN.equals(severity))
					return true;
			}
		}
		return false;
	}

	/**
	 * @return Returns the longTermPartTimeReportColumns.
	 */
	public List getLongTermPartTimeReportColumnsMap() {
		return longTermPartTimeReportColumns;
	}
	/**
	 * @param longTermPartTimeReportColumns The longTermPartTimeReportColumns to set.
	 */
	public void setLongTermPartTimeReportColumnsMap(List longTermPartTimeReportColumns) {
		this.longTermPartTimeReportColumns = longTermPartTimeReportColumns;
	}

	public RowVal getLongTermPartTimeReportColumns(int index) {
		if(longTermPartTimeReportColumns == null )
		{
			longTermPartTimeReportColumns=new ArrayList<RowVal>();
			
		}
		RowVal v = (RowVal) longTermPartTimeReportColumns.get(new Integer(index));
		if (v == null) {
			longTermPartTimeReportColumns.add(new Integer(index), new RowVal());
			v = (RowVal) longTermPartTimeReportColumns.get(new Integer(index));
		}
		return v;
	}
    
    public void setLongTermPartTimeReportColumns(int index, RowVal val) {
    	longTermPartTimeReportColumns.add(new Integer(index), val);
    }
       
	/**
	 * @return Returns the deleteBoxes.
	 */
	public List<Boolean> getDeleteBoxesMap() {
		return deleteBoxes;
	}
	/**
	 * @param deleteBoxes The deleteBoxes to set.
	 */
	public void setDeleteBoxesMap(List<Boolean> deleteBoxes) {
		this.deleteBoxes = deleteBoxes;
	}

	public Boolean getDeleteBoxes(int index) {
		if (deleteBoxes == null) {
			return false;
		}

		Boolean v = (Boolean) deleteBoxes.get(new Integer(index));
		if (v == null)
			return false;
		else
			return v;
	}

	public void setDeleteBoxes(int index, Boolean value) {
		if (this.deleteBoxes == null) {
			deleteBoxes = new ArrayList<Boolean>();
		}
		deleteBoxes.add(new Integer(index), value);

	}

	public String getCurrentDateJavascriptObject() {
		Calendar calendar = AbstractSubmitController.adjustDate4pm(null);
		StringBuffer buffer = new StringBuffer(NEW_DATE);
		buffer.append(String.valueOf(calendar.get(Calendar.YEAR))).append(COMMA);
		buffer.append(String.valueOf(calendar.get(Calendar.MONTH))).append(COMMA);
		buffer.append(String.valueOf(calendar.get(Calendar.DATE))).append(RIGHT_PAR);
		return buffer.toString();
	}
	
	public Collection  getLongTermPartTimeReportColumnsObjectNamesForJavascript() {

		Collection list	= new ArrayList();
		

		if (longTermPartTimeReportColumns != null) {
			int colSize = longTermPartTimeReportColumns.size();
			
			for (int col = 0; col < colSize; col++) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(NEW_ARRAY);
				
				RowVal columnVal = (RowVal) longTermPartTimeReportColumns.get(new Integer(col));
				
				int rowSize = columnVal.getRowMap().size();
				for (int row = 0; row < rowSize; row ++) {
					String rowVal = (String) columnVal.getRowMap().get(new Integer(row));
					// start creating each of the column arrays
					buffer.append(row == 0 ? QUOTE : COMMA_QUOTE)
					.append(UPLOADFORMOBJ)
					.append(ELEMENTS__LTPTCOLUMNS)
					.append(col)
					.append(_ROW_)
					.append(row)
					.append(END_BR);
				}
				buffer.append(RIGHT_PAR);
				list.add(buffer.toString());
			}
		}
		return list;
	}
	
	public Collection  getParticipantFieldObjectNamesForJavascript() {

		Collection list	= new ArrayList();
		
		int numberOfRows = 0;
		
		if (theReport != null && theReport.getLongTermPartTimeData() != null) {
			numberOfRows = theReport.getDetails().size();

			for (int row = 0; row < numberOfRows; row++) {

				StringBuffer buffer = new StringBuffer();
				buffer.append(NEW_ARRAY);
				
				if (longTermPartTimeReportColumns != null) {
					int colSize = longTermPartTimeReportColumns.size();
					
					for (int col = 0; col < colSize; col++) {
						// start creating each of the row arrays
						buffer.append(col == 0 ? QUOTE : COMMA_QUOTE)
						.append(UPLOADFORMOBJ)
						.append(ELEMENTS__LTPTCOLUMNS)
						.append(col)
						.append(_ROW_)
						.append(row)
						.append(END_BR);
					}
				}

				buffer.append(RIGHT_PAR);
				list.add(buffer.toString());
			}
		}
		return list;
	}
	/**
	 * @return Returns the employerIds.
	 */
	public Map getEmployerIdsMap() {
		return employerIds;
	}
	/**
	 * @param employerIds The employerIds to set.
	 */
	public void setEmployerIdsMap(Map employerIds) {
		this.employerIds = employerIds;
	}

	public String getEmployerIds(int index) {
		if (employerIds == null) {
			return "";
		}

		String v = (String) employerIds.get(new Integer(index));
		if (v == null)
			return "";
		else
			return v;
	}

	public void setEmployerIds(int index, String value) {
		if (this.employerIds == null) {
			employerIds = new HashMap();
		}
		employerIds.put(new Integer(index), value);

	}
	/**
	 * @return Returns the recordNumbers.
	 */
	public Map getRecordNumbersMap() {
		return recordNumbers;
	}
	/**
	 * @param employerIds The recordNumbers to set.
	 */
	public void setRecordNumbersMap(Map recordNumbers) {
		this.recordNumbers = recordNumbers;
	}

	public Integer getRecordNumbers(int index) {
		if (recordNumbers == null) {
			return new Integer(0);
		}

		Integer v = (Integer) recordNumbers.get(new Integer(index));
		if (v == null)
			return new Integer(0);
		else
			return v;
	}

	public void setRecordNumbers(int index, Integer value) {
		if (this.recordNumbers == null) {
			recordNumbers = new HashMap();
		}
		recordNumbers.put(new Integer(index), value);

	}
	
	public boolean isSaveAction(String task) {
		return SAVE_TASK.equals(task);
	}
	
	public boolean isSubmitAction(String task) {
		return SUBMIT_TASK.equals(task);
	}
	
	/**
	 * @return Returns the isShowConfirmDialog.
	 */
	public boolean isShowConfirmDialog() {
		return isShowConfirmDialog;
	}
	/**
	 * @param isShowConfirmDialog The isShowConfirmDialog to set.
	 */
	public void setShowConfirmDialog(boolean isShowConfirmDialog) {
		this.isShowConfirmDialog = isShowConfirmDialog;
	}
	/**
	 * @return Returns the isIgnoreDataCheckWarnings.
	 */
	public boolean isIgnoreDataCheckWarnings() {
		return isIgnoreDataCheckWarnings;
	}
	/**
	 * @param isIgnoreDataCheckWarnings The isIgnoreDataCheckWarnings to set.
	 */
	public void setIgnoreDataCheckWarnings(boolean isIgnoreDataCheckWarnings) {
		this.isIgnoreDataCheckWarnings = isIgnoreDataCheckWarnings;
	}
	
	public String getDialogWarningMessages() {
			
		if ( 
				theReport == null || 
				theReport.getLongTermPartTimeData() == null || 
				theReport.getLongTermPartTimeData().getReportDataErrors() == null ||
				theReport.getLongTermPartTimeData().getReportDataErrors().getErrors() == null ) {
			return "";
		}

		ReportDataErrors errors = theReport.getLongTermPartTimeData().getReportDataErrors();
		
		if ( errors.getErrors().size() == 0 ) {
			// no data check errors/warnings, return
			return "";
		}
		
		StringBuffer buff = new StringBuffer("");
		
		// loop through the collection of errors
		Iterator iter = errors.getErrors().iterator();
		while (iter.hasNext()) {
			buff.append("\\n");
			SubmissionError error = (SubmissionError)iter.next();
						
			// if it's a participant-level error, display the row number
			buff.append(" ");
			if (error.isParticipantLevel()) {
				buff.append("Row # ").append(error.getRowNumber());
			} else {
				// else display the field name
				buff.append(error.getFieldDisplayLabel());
			}
			buff.append(" ");
						
			// display the content
			try {
				Message msg = (Message)ContentCacheManager.getInstance().getContentById(error.getContentId(), ContentTypeManager.instance().MESSAGE);
				buff.append(StringEscapeUtils.escapeJavaScript(msg.getText()));
			} catch (ContentException ce) {
				buff.append("unknown error/warning");
			}
		}
		return buff.toString();
	}

	/**
	 * @return Returns the isNoPermission.
	 */
	public boolean isNoPermission() {
		return isNoPermission;
	}
	/**
	 * @param isNoPermission The isNoPermission to set.
	 */
	public void setNoPermission(boolean isNoPermission) {
		this.isNoPermission = isNoPermission;
	}
	/**
	 * @return Returns the warningMessage.
	 */
	public String getWarningMessage() {
		return warningMessage;
	}
	/**
	 * @param warningMessage The warningMessage to set.
	 */
	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}
	
	/**
	 * @return Returns the isHasChanged.
	 */
	public boolean isHasChanged() {
		return isHasChanged;
	}
	/**
	 * @param isHasChanged The isHasChanged to set.
	 */
	public void setHasChanged(boolean isHasChanged) {
		this.isHasChanged = isHasChanged;
	}
	public int getMyOwnPageNumber() {
		return myOwnPageNumber;
	}
	public void setMyOwnPageNumber(int myOwnPageNumber) {
		this.myOwnPageNumber = myOwnPageNumber;
	}
	public boolean isResubmit() {
		return this.resubmit;
	}
	public void setResubmit(boolean resubmit) {
		this.resubmit = resubmit;
	}
	public String getForwardFromSave() {
		return this.forwardFromSave;
	}
	public void setForwardFromSave(String forwardFromSave) {
		this.forwardFromSave = forwardFromSave;
	}
   
	public boolean isEditFunctionAvailable() {
		return this.isEditFunctionAvailable;
	}

	public void setEditFunctionAvailable(boolean isEditFunctionAvailable) {
		this.isEditFunctionAvailable = isEditFunctionAvailable;
	}

    public boolean isMaskSSN() {
        return maskSSN;
    }

    public void setMaskSSN(boolean maskSSN) {
        this.maskSSN = maskSSN;
    }

    public String getFuturePlanYearEnd() {
        return futurePlanYearEnd;
    }

    public void setFuturePlanYearEnd(String futurePlanYearEnd) {
        this.futurePlanYearEnd = futurePlanYearEnd;
    }

	/**
	 * @return Returns the longTermPartTimeAssessmentYears.
	 */
	public List getLongTermPartTimeAssessmentYearsMap() {
		return longTermPartTimeAssessmentYears;
	}
	
	/**
	 * @param longTermPartTimeAssessmentYears to set.
	 */
	public void setLongTermPartTimeAssessmentYearsMap(List longTermPartTimeAssessmentYears) {
		this.longTermPartTimeAssessmentYears = longTermPartTimeAssessmentYears;
	}
	
	public Integer getLongTermPartTimeAssessmentYears(int index) {

		if (longTermPartTimeAssessmentYears == null) {
			return new Integer(0);
		}

		Integer value = (Integer) longTermPartTimeAssessmentYears.get(new Integer(index));
		if (value == null)
			return new Integer(0);
		else
			return value;
	}

	public void setLongTermPartTimeAssessmentYears(int index, Integer value) {
		if (this.longTermPartTimeAssessmentYears == null) {
			longTermPartTimeAssessmentYears = new ArrayList();
		}
		longTermPartTimeAssessmentYears.add(new Integer(index), value);
	}
	
}
