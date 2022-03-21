package com.manulife.pension.ps.web.census;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;



import com.manulife.pension.ps.service.report.census.valueobject.EmployeeStatusHistoryDetails;
import com.manulife.pension.ps.web.report.ReportActionCloneableForm;

/**
 * Form that works in tandem with <code>EmployeeStatusHistoryReportAction</code>
 * 
 * @author sternlu
 *
 */
public class EmployeeStatusHistoryReportForm extends ReportActionCloneableForm {

	private static final long serialVersionUID = 3807168635095804153L;

	public static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";

	public static final String STATUS = "employmentStatus";

	public static final String EFFECTIVE_DATE = "effectiveDate";

	public static final String LAST_UPDATED_DATE = "lastUpdatedDate";

	public static final String LAST_UPDATED_NAME = "lastUpdatedName";

	//    private static SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	//     private static SimpleDateFormat DELETE_DATE_FORMAT = new SimpleDateFormat("YYYY-MM-DD");
	private String profileId;

	private String task;

	private String savedFlag;

	/**
	 * A list of <code>EmployeeStatusHistoryDetails</code> beans that contain data to be saved
	 * The data for display comes from the report and later the data is captured in this collection
	 * part of the Form
	 * 
	 */
	public List<EmployeeStatusHistoryDetails> theItem = new ArrayList<EmployeeStatusHistoryDetails>();

	/**
	 * Constructor for EmployeeStatusHistoryReportForm
	 */
	public EmployeeStatusHistoryReportForm() {
		super();
	}

	/**
	 * resets the form
	 */
	public void reset() {
		for (EmployeeStatusHistoryDetails element : theItem) {
			//
			element.setMarkedForDeletionDisplay(element.isMarkedForDeletion());
			//theItem = new ArrayList<EmployeeStatusHistoryDetails>();
		}
	}

	/**
	 * The while construct is used to create objects as necessary in order to return 
	 * to the Struts populate method. 
	 * 
	 * @param index
	 * @return
	 */
	public EmployeeStatusHistoryDetails getTheItem(int index) {
		while (theItem.size() <= index)
			theItem.add(new EmployeeStatusHistoryDetails());

		return ((EmployeeStatusHistoryDetails) theItem.get(index));

	}

	/**
	 * Setter for the list used to pre-populate the form
	 * 
	 * @param theItemList
	 */
	public void setTheItemList(List<EmployeeStatusHistoryDetails> theItemList) {
		if (theItemList == null) {
			theItemList = new ArrayList<EmployeeStatusHistoryDetails>();
		}

		this.theItem = theItemList;
	}

	/**
	 * Utility method
	 */
	public List<EmployeeStatusHistoryDetails> getTheItemList() {
		return this.theItem;
	}

	/**
	 * Overrides the one from base
	 */
	public void reset( HttpServletRequest arg1) {

		//super.reset(arg0, arg1);
	}

	public void clearItemList() {
		this.theItem = new ArrayList<EmployeeStatusHistoryDetails>();
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	@Override
	public Object clone() {
		EmployeeStatusHistoryReportForm newForm = new EmployeeStatusHistoryReportForm();

		for (EmployeeStatusHistoryDetails element : theItem) {
			try {
				newForm.getTheItemList().add(
						(EmployeeStatusHistoryDetails) element.clone());
			} catch (CloneNotSupportedException e) {
				// Never reacheable
				e.printStackTrace();
			}
		}
		return newForm;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getTask() {
		return task;
	}

	public void setSavedFlag(String savedFlag) {
		this.savedFlag = savedFlag;
	}

	public String getSavedFlag() {
		return savedFlag;
	}

}
