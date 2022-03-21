/*
 * Created on August 16, 2004
 *
 */
package com.manulife.pension.ps.web.tools;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;


import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.ps.service.report.transaction.handler.MoneySourceDescription;
import com.manulife.pension.ps.service.submission.util.StatusGroupHelper;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper;
import com.manulife.pension.ps.web.tools.util.SubmissionTypeTranslater;

/**
 * @author Adrian Robitu
 */
public class SubmissionHistoryForm extends ReportForm {

	private static final String ALL_LABEL = "All";
	private static final String NO_VALUE_INDICATOR = "-1";

	private boolean justMine = false;
	private boolean filterSimple = true;
	private boolean filterBySubmission = true;
	private String filterType;
	private String filterStatus;
	private String filterShowDate;
	private String filterStartDate;
	private String filterEndDate;
	private ArrayList datesCollection;
	private ArrayList fromMonthsCollection;
	private ArrayList toMonthsCollection;
	private ArrayList submissionTypes;
	private ArrayList submissionStatuses;
	private boolean filterActive;

	public SubmissionHistoryForm() {
		super();

		// by default there are no active filters
		filterActive = false;

		// Fill the submission types
		submissionTypes = new ArrayList();
		submissionTypes.add(new LabelValueBean(ALL_LABEL, NO_VALUE_INDICATOR));
        submissionTypes.add(new LabelValueBean(SubmissionTypeTranslater.translate(GFTUploadDetail.SUBMISSION_TYPE_CENSUS),
                GFTUploadDetail.SUBMISSION_TYPE_CENSUS));
        submissionTypes.add(new LabelValueBean(SubmissionTypeTranslater.translate(GFTUploadDetail.SUBMISSION_TYPE_VESTING),
                GFTUploadDetail.SUBMISSION_TYPE_VESTING));
		submissionTypes.add(new LabelValueBean(MoneySourceDescription.CONTRIBUTION, MoneySourceDescription.CONTRIBUTION));
		submissionTypes.add(new LabelValueBean(MoneySourceDescription.FORFEITURE_CONTRIBUTION, MoneySourceDescription.FORFEITURE_CONTRIBUTION));
		submissionTypes.add(new LabelValueBean(SubmissionTypeTranslater.translate(GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR),
						GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR));
		submissionTypes.add(new LabelValueBean(MoneySourceDescription.REINSTATEMENT_CONTRIBUTION, MoneySourceDescription.REINSTATEMENT_CONTRIBUTION));
		submissionTypes.add(new LabelValueBean(SubmissionTypeTranslater.translate(GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_TRANSFER),
						GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_TRANSFER));
		submissionTypes.add(new LabelValueBean("Sample",GFTUploadDetail.SUBMISSION_TYPE_MISCELLANEOUS));  // sample (aka misc)
		submissionTypes.add(new LabelValueBean(SubmissionTypeTranslater.translate(GFTUploadDetail.SUBMISSION_TYPE_PAYMENT),
					GFTUploadDetail.SUBMISSION_TYPE_PAYMENT));
		submissionTypes.add(new LabelValueBean(SubmissionTypeTranslater.translate(GFTUploadDetail.SUBMISSION_TYPE_TEST),
					GFTUploadDetail.SUBMISSION_TYPE_TEST));
		
		
		/**
		 * file type: Conversion.
		 */
		submissionTypes.add(new LabelValueBean(SubmissionTypeTranslater.translate(GFTUploadDetail.SUBMISSION_TYPE_CONVERSION),
				GFTUploadDetail.SUBMISSION_TYPE_CONVERSION));
		
		/**
		 * file type: LTPT.
		 */
        submissionTypes.add(new LabelValueBean(SubmissionTypeTranslater.translate(SubmissionHistoryItemActionHelper.SUBMISSION_TYPE_LTPT),
        		SubmissionHistoryItemActionHelper.SUBMISSION_TYPE_LTPT));

		// Fill the submission statuses
		StatusGroupHelper helper = StatusGroupHelper.getInstance();
		submissionStatuses = new ArrayList(helper.getAllStatusGroupDescriptions().size() + 1);
		submissionStatuses.add(new LabelValueBean(ALL_LABEL, NO_VALUE_INDICATOR));
		Iterator iter = helper.getAllStatusGroupDescriptions().iterator();
		while (iter.hasNext()) {
			StatusGroupHelper.StatusGroup sg = (StatusGroupHelper.StatusGroup) iter.next();
			submissionStatuses.add(new LabelValueBean(sg.getDesc(), sg.getCode()));
		}
	}

	/**
	 * @return Returns the filterStatus.
	 */
	public String getFilterStatus() {
		return filterStatus;
	}

	/**
	 * @param filterStatus The filterStatus to set.
	 */
	public void setFilterStatus(String filterStatus) {
		this.filterStatus = filterStatus;
	}

	/**
	 * @return Returns the filterType.
	 */
	public String getFilterType() {
		return filterType;
	}

	/**
	 * @param filterType The filterType to set.
	 */
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}
	/**
	 * @return Returns the justMine.
	 */
	public boolean isJustMine() {
		return justMine;
	}
	/**
	 * @param justMine The justMine to set.
	 */
	public void setJustMine(boolean justMine) {
		this.justMine = justMine;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Form#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void reset( HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		//super.reset(arg0, arg1);
		justMine = false;
		filterBySubmission = true;
	}

	/**
	 * @return Returns the datesCollection.
	 */
	public ArrayList getDatesCollection() {
		return datesCollection;
	}

	/**
	 * @param datesCollection The datesCollection to set.
	 */
	public void setDatesCollection(ArrayList datesCollection) {
		this.datesCollection = datesCollection;
	}

	/**
	 * @return Returns the filterEndDate.
	 */
	public String getFilterEndDate() {
		return filterEndDate;
	}

	/**
	 * @param filterEndDate The filterEndDate to set.
	 */
	public void setFilterEndDate(String filterEndDate) {
		this.filterEndDate = filterEndDate;
	}

	/**
	 * @return Returns the filterStartDate.
	 */
	public String getFilterStartDate() {
		return filterStartDate;
	}

	/**
	 * @param filterStartDate The filterStartDate to set.
	 */
	public void setFilterStartDate(String filterStartDate) {
		this.filterStartDate = filterStartDate;
	}

	/**
	 * @return Returns the fromMonthsCollection.
	 */
	public ArrayList getFromMonthsCollection() {
		return fromMonthsCollection;
	}

	/**
	 * @param monthsCollection The fromMonthsCollection to set.
	 */
	public void setFromMonthsCollection(ArrayList fromMonthsCollection) {
		this.fromMonthsCollection = fromMonthsCollection;
	}

	/**
	 * @return Returns the toMonthsCollection.
	 */
	public ArrayList getToMonthsCollection() {
		return toMonthsCollection;
	}

	/**
	 * @param monthsCollection The toMonthsCollection to set.
	 */
	public void setToMonthsCollection(ArrayList toMonthsCollection) {
		this.toMonthsCollection = toMonthsCollection;
	}

	/**
	 * @return Returns the filterSimple.
	 */
	public boolean isFilterSimple() {
		return filterSimple;
	}

	/**
	 * @param filterSimple The filterSimple to set.
	 */
	public void setFilterSimple(boolean filterSimple) {
		this.filterSimple = filterSimple;
	}

	/**
	 * @return Returns the filterBySubmission.
	 */
	public boolean isFilterBySubmission() {
		return filterBySubmission;
	}

	/**
	 * @param filterBySubmission The filterBySubmission to set.
	 */
	public void setFilterBySubmission(boolean filterBySubmission) {
		this.filterBySubmission = filterBySubmission;
	}

	/**
	 * @return Returns the submissionStatuses.
	 */
	public ArrayList getSubmissionStatuses() {
		return submissionStatuses;
	}

	/**
	 * @param submissionStatuses The submissionStatuses to set.
	 */
	public void setSubmissionStatuses(ArrayList submissionStatuses) {
		this.submissionStatuses = submissionStatuses;
	}

	/**
	 * @return Returns the submissionTypes.
	 */
	public ArrayList getSubmissionTypes() {
		return submissionTypes;
	}

	/**
	 * @param submissionTypes The submissionTypes to set.
	 */
	public void setSubmissionTypes(ArrayList submissionTypes) {
		this.submissionTypes = submissionTypes;
	}

	/**
	 * @return Returns the filterShowDate.
	 */
	public String getFilterShowDate() {
		return filterShowDate;
	}

	/**
	 * @param filterShowDate The filterShowDate to set.
	 */
	public void setFilterShowDate(String filterShowDate) {
		this.filterShowDate = filterShowDate;
	}

	/**
	 * @return Returns the filterActive.
	 */
	public boolean isFilterActive() {
		return filterActive;
	}

	/**
	 * @param filterActive The filterActive to set.
	 */
	public void setFilterActive(boolean filterActive) {
		this.filterActive = filterActive;
	}

	/**
	 * Convenience method for testing if a certain field has been set.
	 *
	 * @param value - object to test
	 * @return true is the value passed in is not null and different from the no-value place holder
	 */
	public static boolean isFieldSet(Object value) {
		return value != null && !value.equals(NO_VALUE_INDICATOR);
	}
}