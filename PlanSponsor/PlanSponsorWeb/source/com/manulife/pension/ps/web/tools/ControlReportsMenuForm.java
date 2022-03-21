package com.manulife.pension.ps.web.tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.platform.web.controller.BaseForm;
import com.manulife.pension.service.fee.valueobject.ClassType;

/**
 * @author Eswar
 *
 */
public class ControlReportsMenuForm extends BaseForm{

	private static final long serialVersionUID = 1L;
	
	private List<String> giflVersions = new ArrayList<String>();
	private List<ClassType> classTypes = new ArrayList<ClassType>();
	private String selectedGIFLVersion;
	private String selectedClassType;
	private Map<String, String> giflVersionNames = new HashMap<String, String>();
	
	private List<Date> inforceDisclosureReviewDates = new ArrayList<Date>();
	private String selectedAsOfDate;


	/**
	 * @return selectedGIFLVersion
	 */
	public String getSelectedGIFLVersion() {
		return selectedGIFLVersion;
	}

	/**
	 * @param selectedGIFLVersion
	 */
	public void setSelectedGIFLVersion(String selectedGIFLVersion) {
		this.selectedGIFLVersion = selectedGIFLVersion;
	}

	/**
	 * @return giflVersions
	 */
	public List<String> getGiflVersions() {
		return giflVersions;
	}

	/**
	 * @param giflVersions
	 */
	public void setGiflVersions(List<String> giflVersions) {
		this.giflVersions = giflVersions;
	}
	
	/**
	 * @return selectedClassType
	 */
	public String getSelectedClassType() {
		return selectedClassType;
	}
	
	/**
	 * @param selectedClassType
	 */
	public void setSelectedClassType(String selectedClassType) {
		this.selectedClassType = selectedClassType;
	}

	/**
	 * @return classTypes
	 */
	public List<ClassType> getClassTypes() {
		return classTypes;
	}

	/**
	 * @param classTypes
	 */
	public void setClassTypes(List<ClassType> classTypes) {
		this.classTypes = classTypes;
	}

	/**
	 * @return giflVersionNames
	 */
	public Map<String, String> getGiflVersionNames() {
		return giflVersionNames;
	}

	/**
	 * @param giflVersionNames
	 */
	public void setGiflVersionNames(Map<String, String> giflVersionNames) {
		this.giflVersionNames = giflVersionNames;
	}

	/**
	 * @return the inforceDisclosureReviewDates
	 */
	public List<Date> getInforceDisclosureReviewDates() {
		return inforceDisclosureReviewDates;
	}

	/**
	 * @param inforceDisclosureReviewDates the inforceDisclosureReviewDates to set
	 */
	public void setInforceDisclosureReviewDates(
			List<Date> inforceDisclosureReviewDates) {
		this.inforceDisclosureReviewDates = inforceDisclosureReviewDates;
	}

	/**
	 * @return the selectedAsOfDate
	 */
	public String getSelectedAsOfDate() {
		return selectedAsOfDate;
	}

	/**
	 * @param selectedAsOfDate the selectedAsOfDate to set
	 */
	public void setSelectedAsOfDate(String selectedAsOfDate) {
		this.selectedAsOfDate = selectedAsOfDate;
	}
}
