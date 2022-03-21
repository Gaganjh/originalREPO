package com.manulife.pension.platform.web.investment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;



import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.investment.valueobject.CriteriaAndWeightingPresentation;
import com.manulife.pension.platform.web.investment.valueobject.IPSReviewReportDetailsVO;


/**
 * Form Bean for IPS Review Details
 * 
 * @author Karthik
 *
 */
public class IPSAndReviewDetailsForm extends AutoForm  {

	private static final long serialVersionUID = 1L;
	
	private Map<String, Integer> criteriaAndWeighting = null;
	
	private String annualReviewDate = null;
	
	private boolean iPSAssistServiceAvailable = false;
	
	private int totalWeighting = 0;
	
	private List<CriteriaAndWeightingPresentation> criteriaAndWeightingPresentationList = null;
	
	private String mode;
	
	private Date lastModifiedOn;
	
	private String lastUpdatedUserId;
	
	private boolean pdfCapped;
	
	private String lastUpdatedUserIdType;
	
	private String sourceChannelCode;

	private boolean formChanged;
	
	private boolean saveSuccess;
	
	private boolean editLinkAccessible;
	
	private List<IPSReviewReportDetailsVO> ipsReviewReportDetailsList = new ArrayList<IPSReviewReportDetailsVO>();
	
	private boolean interimReportLinkAvailable;
	
	private boolean isNewAnnualReviewDateAvailable;
	
	private boolean dateChanged;
	
	private String newAnnualReviewMonth;
	
	private String newAnnualReviewDate;
	
	private boolean flag;
	
	private String contactName;
	
	private String contactInformation;
	
	private String comments;
	
	private boolean criteriaChanged;
	
	private String newServiceDateConfirmationText;
	
	private boolean initialNotificationSent;
	
	private boolean is338DesignateRia;
	
	
	public Map<String, Integer> getCriteriaAndWeighting() {
		return criteriaAndWeighting;
	}

	public void setCriteriaAndWeighting(Map<String, Integer> criteriaAndWeighting) {
		this.criteriaAndWeighting = criteriaAndWeighting;
	}

	public List<CriteriaAndWeightingPresentation> getCriteriaAndWeightingPresentationList() {
		return criteriaAndWeightingPresentationList;
	}

	public void setCriteriaAndWeightingPresentationList(
			List<CriteriaAndWeightingPresentation> criteriaAndWeightingPresentationList) {
		this.criteriaAndWeightingPresentationList = criteriaAndWeightingPresentationList;
	}

	public String getAnnualReviewDate() {
		return annualReviewDate;
	}

	public void setAnnualReviewDate(String annualReviewDate) {
		this.annualReviewDate = annualReviewDate;
	}

	public int getTotalWeighting() {
		return totalWeighting;
	}

	public void setTotalWeighting(int totalWeighting) {
		this.totalWeighting = totalWeighting;
	}

	public boolean isiPSAssistServiceAvailable() {
		return iPSAssistServiceAvailable;
	}

	public void setiPSAssistServiceAvailable(boolean iPSAssistServiceAvailable) {
		this.iPSAssistServiceAvailable = iPSAssistServiceAvailable;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}

	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}

	public String getLastUpdatedUserIdType() {
		return lastUpdatedUserIdType;
	}

	public void setLastUpdatedUserIdType(String lastUpdatedUserIdType) {
		this.lastUpdatedUserIdType = lastUpdatedUserIdType;
	}

	public String getSourceChannelCode() {
		return sourceChannelCode;
	}

	public void setSourceChannelCode(String sourceChannelCode) {
		this.sourceChannelCode = sourceChannelCode;
	}

	public CriteriaAndWeightingPresentation getCriteriaAndWeightingId(int index) {
		
		if (this.criteriaAndWeightingPresentationList == null) {
			this.setCriteriaAndWeightingPresentationList(createMinimumNumberOfCriteriaRows());
		}
		
		int listSize = this.criteriaAndWeightingPresentationList.size();
		
		if (index >= listSize) {
			
			for (int i=0; i<= (index - listSize); i++) {
				this.criteriaAndWeightingPresentationList.add(new CriteriaAndWeightingPresentation());
			}
		}
		
        return (CriteriaAndWeightingPresentation) criteriaAndWeightingPresentationList.get(index);
    }
	
	public boolean isFormChanged() {
		return formChanged;
	}

	public void setFormChanged(boolean formChanged) {
		this.formChanged = formChanged;
	}

	public boolean isSaveSuccess() {
		return saveSuccess;
	}

	public void setSaveSuccess(boolean saveSuccess) {
		this.saveSuccess = saveSuccess;
	}

	private static List<CriteriaAndWeightingPresentation> 
	createMinimumNumberOfCriteriaRows() {
		
		List<CriteriaAndWeightingPresentation> customQueryRowList = 
			new ArrayList<CriteriaAndWeightingPresentation>();
		
		// Add three empty rows
		customQueryRowList.add(new CriteriaAndWeightingPresentation());
		customQueryRowList.add(new CriteriaAndWeightingPresentation());
		customQueryRowList.add(new CriteriaAndWeightingPresentation());
	
		return customQueryRowList;
	}

	public boolean isEditLinkAccessible() {
		return editLinkAccessible;
	}

	public void setEditLinkAccessible(boolean editLinkAccessible) {
		this.editLinkAccessible = editLinkAccessible;
	}

	/**
	 * @return the ipsReviewReportDetailsList
	 */
	public List<IPSReviewReportDetailsVO> getIpsReviewReportDetailsList() {
		return ipsReviewReportDetailsList;
	}

	/**
	 * @param ipsReviewReportDetailsList the ipsReviewReportDetailsList to set
	 */
	public void setIpsReviewReportDetailsList(
			List<IPSReviewReportDetailsVO> ipsReviewReportDetailsList) {
		this.ipsReviewReportDetailsList = ipsReviewReportDetailsList;
	}

	/**
	 * @return the interimReportLinkAvailable
	 */
	public boolean isInterimReportLinkAvailable() {
		return interimReportLinkAvailable;
	}

	/**
	 * @param interimReportLinkAvailable the interimReportLinkAvailable to set
	 */
	public void setInterimReportLinkAvailable(boolean interimReportLinkAvailable) {
		this.interimReportLinkAvailable = interimReportLinkAvailable;
	}

	/**
	 * @return the initialNotificationSent
	 */
	public boolean isInitialNotificationSent() {
		return initialNotificationSent;
	}

	/**
	 * @param initialNotificationSent the initialNotificationSent to set
	 */
	public void setInitialNotificationSent(boolean initialNotificationSent) {
		this.initialNotificationSent = initialNotificationSent;
	}

	/**
	 * @return the dateChanged
	 */
	public boolean isDateChanged() {
		return dateChanged;
	}

	/**
	 * @param dateChanged the dateChanged to set
	 */
	public void setDateChanged(boolean dateChanged) {
		this.dateChanged = dateChanged;
	}

	/**
	 * @return the newAnnualReviewMonth
	 */
	public String getNewAnnualReviewMonth() {
		return newAnnualReviewMonth;
	}

	/**
	 * @param newAnnualReviewMonth the newAnnualReviewMonth to set
	 */
	public void setNewAnnualReviewMonth(String newAnnualReviewMonth) {
		this.newAnnualReviewMonth = newAnnualReviewMonth;
	}

	/**
	 * @return the newAnnualReviewDate
	 */
	public String getNewAnnualReviewDate() {
		return newAnnualReviewDate;
	}

	/**
	 * @param newAnnualReviewDate the newAnnualReviewDate to set
	 */
	public void setNewAnnualReviewDate(String newAnnualReviewDate) {
		this.newAnnualReviewDate = newAnnualReviewDate;
	}

	public boolean isPdfCapped() {
		return pdfCapped;
	}

	public void setPdfCapped(boolean pdfCapped) {
		this.pdfCapped = pdfCapped;
	}

	/**
	 * @return the flag
	 */
	public boolean isFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	/**
	 * @return the contactName
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * @param contactName the contactName to set
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	/**
	 * @return the contactInformation
	 */
	public String getContactInformation() {
		return contactInformation;
	}

	/**
	 * @param contactInformation the contactInformation to set
	 */
	public void setContactInformation(String contactInformation) {
		this.contactInformation = contactInformation;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the criteriaChanged
	 */
	public boolean isCriteriaChanged() {
		return criteriaChanged;
	}

	/**
	 * @param criteriaChanged the criteriaChanged to set
	 */
	public void setCriteriaChanged(boolean criteriaChanged) {
		this.criteriaChanged = criteriaChanged;
	}

	/**
	 * @return the newServiceDateConfirmationText
	 */
	public String getNewServiceDateConfirmationText() {
		return newServiceDateConfirmationText;
	}

	/**
	 * @param newServiceDateConfirmationText the newServiceDateConfirmationText to set
	 */
	public void setNewServiceDateConfirmationText(
			String newServiceDateConfirmationText) {
		this.newServiceDateConfirmationText = newServiceDateConfirmationText;
	}

	/**
	 * @return the isNewAnnualReviewDateAvailable
	 */
	public boolean isNewAnnualReviewDateAvailable() {
		return isNewAnnualReviewDateAvailable;
	}

	/**
	 * @param isNewAnnualReviewDateAvailable the isNewAnnualReviewDateAvailable to set
	 */
	public void setNewAnnualReviewDateAvailable(
			boolean isNewAnnualReviewDateAvailable) {
		this.isNewAnnualReviewDateAvailable = isNewAnnualReviewDateAvailable;
	}

	public boolean isIs338DesignateRia() {
		return is338DesignateRia;
	}

	public void setIs338DesignateRia(boolean is338DesignateRia) {
		this.is338DesignateRia = is338DesignateRia;
	}
	

}
