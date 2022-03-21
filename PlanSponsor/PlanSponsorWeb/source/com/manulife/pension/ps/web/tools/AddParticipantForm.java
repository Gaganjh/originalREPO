package com.manulife.pension.ps.web.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.ps.service.report.submission.valueobject.AddParticipantReportData;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.util.CloneableForm;

/**
 * @author Jim Adamthwaite
 *
 * form for the add participant page of the submission function
 */

public class AddParticipantForm extends ReportForm implements CloneableForm {
	private static final String EDIT_MODE_IND = "e";
	private static final String EDIT_PAGE_URL = "\"/do/tools/editContribution/?subNo=";
	private static String FALSE_VALUE = "false";

	private CloneableForm clonedForm;
	private String subNo;
	private String mode;
	private boolean isAllowedView = false;
	private AddParticipantReportData theReport;
	private boolean isViewMode = false;
	private boolean isHasChanged = false;
	private String createIndicator = "N";
	private List<Boolean> addBoxes = new ArrayList<Boolean>();
	private String createParticipantName = "";
	private String createParticipantId = "";
	
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
	public AddParticipantReportData getTheReport() {
		return theReport;
	}
	/**
	 * @param theReport The theReport to set.
	 */
	public void setTheReport(AddParticipantReportData theReport) {
		this.theReport = theReport;
	}

	public boolean isEditMode() {
		if ( EDIT_MODE_IND.equals(mode) ) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * @return Returns the mode.
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @param mode The mode to set.
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public String getEditPageURL() {
		return EDIT_PAGE_URL + getSubNo() + "\"";
	}
	public String getCreateIndicator() {
		return this.createIndicator;
	}
	public void setCreateIndicator(String createIndicator) {
		this.createIndicator = createIndicator;
	}
	/**
	 * @return Returns the deleteBoxes.
	 */
	public List<Boolean> getAddBoxes() {
		return addBoxes;
	}
	/**
	 * @param deleteBoxes The deleteBoxes to set.
	 */
	public void setAddBoxes(List<Boolean> addBoxes) {
		this.addBoxes = addBoxes;
	}

	public boolean getAddBox(int index) {
		if (addBoxes == null) {
			return false;
		}

		boolean v =  addBoxes.get(new Integer(index));
		if (v == false)
			return false;
		else
			return v;
	}

	public void setAddBox(int index, Boolean value) {
		if (this.addBoxes == null) {
			addBoxes = new ArrayList<Boolean>();
		}
		addBoxes.add(index,value);

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
	public void clear( HttpServletRequest request) {
		this.clonedForm = null;
		this.subNo = "";
		this.mode = "";
		this.isAllowedView = false;
		this.isViewMode = false;
		this.isHasChanged = false;
		this.createIndicator = "N";
		this.addBoxes = new ArrayList<Boolean>();
		this.createParticipantId = "";
		this.createParticipantName = "";
		this.isHasChanged = false;

	}

	public CloneableForm getClonedForm() {
		return clonedForm;
	}

	public Object clone() {
		AddParticipantForm myClone = new AddParticipantForm();
		
		myClone.setCreateParticipantId(getCreateParticipantId());
		myClone.setCreateParticipantName(getCreateParticipantName());
		
		// add checkboxes
		if (getAddBoxes() != null) {
			Iterator its = getAddBoxes().iterator();
			List<Boolean> newAddBoxes = new ArrayList<Boolean>();
			while (its.hasNext()) {
				boolean it = (boolean) its.next();
				newAddBoxes.add(it);
			}
			myClone.setAddBoxes(newAddBoxes);
		}
		
		return myClone;
	}

	public void storeClonedForm() {
		clonedForm = (CloneableForm) clone();
	}
	public String getCreateParticipantId() {
		return this.createParticipantId;
	}
	public void setCreateParticipantId(String createParticipantId) {
		this.createParticipantId = createParticipantId;
	}
	public String getCreateParticipantName() {
		return this.createParticipantName;
	}
	public void setCreateParticipantName(String createParticipantName) {
		this.createParticipantName = createParticipantName;
	}
}

