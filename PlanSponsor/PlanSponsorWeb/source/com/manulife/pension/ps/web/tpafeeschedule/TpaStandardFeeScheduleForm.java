package com.manulife.pension.ps.web.tpafeeschedule;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.ps.web.util.CloneableForm;
import com.manulife.pension.ps.web.util.CloneableAutoForm;
import com.manulife.pension.service.fee.valueobject.FeeDataVO;

/**
 * Form Bean for TPA Standard Fee Schedule View,Edit and Confirmation fields
 * @author Akhil Khanna
 * 
 */
public class TpaStandardFeeScheduleForm extends CloneableAutoForm {

	private List<FeeDataVO> actualFeeItemsList;
	private List<FeeDataVO> feeLabelList  = new ArrayList<FeeDataVO>();
	private List<FeeDataVO> displayFeeItemsList;
	private List<FeeDataVO> additionalFeeItemsList = new ArrayList<FeeDataVO>();
	private List<FeeDataVO> previewFeeItemsList;
	private CloneableForm clonedForm;
	private PageMode pageMode;
	private String lastUpdatedDate;
	private int lastUpdatedUserId;
	private String lastUpdatedUserName;
	private List<String> amountTypeList;
	private String selectedTpaFirmId;
	private boolean tpaUserManager;
	private boolean editPageModified = false;
	private boolean otherThanInternalUserPresent=false;
	private boolean showBackButton=false;
	private boolean showChangeHistoryLink = false;
		
	/**
	 * @return the pageMode
	 */
	public PageMode getPageMode() {
		return pageMode;
	}

	/**
	 * @param pageMode the pageMode to set
	 */
	public void setPageMode(PageMode pageMode) {
		this.pageMode = pageMode;
	}

	/**
	 * Creating an enum for making the page modes
	 * 
	 * @param lastUpdatedDate
	 */
	enum PageMode {
		VIEW, EDIT, PREVIEW,CHANGE_HISTORY;
	};
	
	/**
	 * Get the LastUpdatedDate
	 * 
	 * @return String lastUpdatedDate
	 */

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	/**
	 * Set the lastUpdatedDate
	 * 
	 * @param lastUpdatedDate
	 */

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	
	
	/**
	 * @return the selectedTpaFirmId
	 */
	public String getSelectedTpaFirmId() {
		return selectedTpaFirmId;
	}

	/**
	 * @param tpaId the selectedTpaFirmId to set
	 */
	public void setSelectedTpaFirmId(String selectedTpaFirmId) {
		this.selectedTpaFirmId = selectedTpaFirmId;
	}

	/**
	 * Get the actualFeeItemsList
	 * 
	 * @return List<FeeDataVO> actualFeeItemsList
	 */
	public List<FeeDataVO> getActualFeeItemsList() {
		return actualFeeItemsList;
	}

	/**
	 * Set the actualFeeItemsList
	 * 
	 * @param List<FeeDataVO> actualFeeItemsList
	 */
	public void setActualFeeItemsList(List<FeeDataVO> actualFeeItemsList) {
		this.actualFeeItemsList = actualFeeItemsList;
	}

	/**
	 * Get the PreviewFeeItemsList
	 * 
	 * @return List<FeeDataVO> previewFeeItemsList
	 */
	public List<FeeDataVO> getPreviewFeeItemsList() {
		return previewFeeItemsList;
	}

	/**
	 * Set the previewFeeItemsList
	 * 
	 * @param List<FeeDataVO> previewFeeItemsList
	 */
	public void setPreviewFeeItemsList(List<FeeDataVO> previewFeeItemsList) {
		this.previewFeeItemsList = previewFeeItemsList;
	}
	
	/**
	 * Get the AmountTypeList
	 * 
	 * @return List<FeeDataVO> amountTypeList
	 */
	public List<String> getAmountTypeList() {
		return amountTypeList;
	}

	/**
	 * Set the amountTypeList
	 * 
	 * @param List<String> amountTypeList
	 */
	public void setAmountTypeList(List<String> amountTypeList) {
		this.amountTypeList = amountTypeList;
	}

	/**
	 * Get the additionalFeeItemsList
	 * 
	 * @return List<FeeDataVO> additionalFeeItemsList
	 */
	public List<FeeDataVO> getAdditionalFeeItemsList() {
		return additionalFeeItemsList;
	}

	/**
	 * Set the additionalFeeItemsList
	 * 
	 * @param List<FeeDataVO> additionalFeeItemsList
	 */
	public void setAdditionalFeeItemsList(List<FeeDataVO> additionalFeeItemsList) {
		this.additionalFeeItemsList = additionalFeeItemsList;
	}

	/**
	 * Get the displayFeeItemsList
	 * 
	 * @return List<FeeDataVO> displayFeeItemsList
	 */
	public List<FeeDataVO> getDisplayFeeItemsList() {
		return displayFeeItemsList;
	}

	/**
	 * Set the defaultFeeItemsList
	 * 
	 * @param defaultFeeItemsList
	 */
	public void setDisplayFeeItemsList(List<FeeDataVO> defaultFeeItemsList) {
		this.displayFeeItemsList = defaultFeeItemsList;
	}

	/**
	 * Get the feeLabelList
	 * 
	 * @return List<FeeDataVO> feeLabelList
	 */
	public FeeDataVO getFeeLabelItems(int index) {
		if (this.feeLabelList == null) {
			this.setFeeLabelList(new ArrayList<FeeDataVO>());
		}

		int listSize = this.feeLabelList.size();

		if (index >= listSize) {

			for (int i = 0; i <= (index - listSize); i++) {
				this.feeLabelList.add(new FeeDataVO());
			}
		}

		return (FeeDataVO) feeLabelList.get(index);
	}

	/**
	 * Get the additionalFeeItemsList
	 * 
	 * @return List<FeeDataVO> additionalFeeItemsList
	 */
	public FeeDataVO getAdditionalFeeItems(int index) {
		if (this.additionalFeeItemsList == null) {
			this.setAdditionalFeeItemsList(new ArrayList<FeeDataVO>());
		}

		int listSize = this.additionalFeeItemsList.size();

		if (index >= listSize) {

			for (int i = 0; i <= (index - listSize); i++) {
				this.additionalFeeItemsList.add(new FeeDataVO());
			}
		}

		return (FeeDataVO) additionalFeeItemsList.get(index);
	}



	/**
	 * Get isTpaUserManager
	 * 
	 * @return boolean tpaUserManager
	 */
	public boolean isTpaUserManager() {
		return tpaUserManager;
	}

	/**
	 * Set the tpaUserManager
	 * 
	 * @param tpaUserManager
	 */
	public void setTpaUserManager(boolean tpaUserManager) {
		this.tpaUserManager = tpaUserManager;
	}

	/**
	 * Get lastUpdatedUserId
	 * 
	 * @return int lastUpdatedUserId
	 */
	public int getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}

	/**
	 * Set the lastUpdatedUserId
	 * 
	 * @param lastUpdatedUserId
	 */
	public void setLastUpdatedUserId(int lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}

	/**
	 * Get lastUpdatedUserName
	 * 
	 * @return String lastUpdatedUserName
	 */
	public String getLastUpdatedUserName() {
		return lastUpdatedUserName;
	}

	/**
	 * Set the lastUpdatedUserName
	 * 
	 * @param lastUpdatedUserName
	 */
	public void setLastUpdatedUserName(String lastUpdatedUserName) {
		this.lastUpdatedUserName = lastUpdatedUserName;
	}

	/**
	 * Get feeLabelList
	 * 
	 * @return  List<FeeDataVO> feeLabelList
	 */
	public List<FeeDataVO> getFeeLabelList() {
		return feeLabelList;
	}

	/**
	 * Set the feeLabelList
	 * 
	 * @param feeLabelList
	 */
	public void setFeeLabelList(List<FeeDataVO> feeLabelList) {
		this.feeLabelList = feeLabelList;
	}



	/**
	 * @return the editPageModified
	 */
	public boolean isEditPageModified() {
		return editPageModified;
	}

	/**
	 * @param editPageModified the editPageModified to set
	 */
	public void setEditPageModified(boolean editPageModified) {
		this.editPageModified = editPageModified;
	}
	
	/**
	 * @return the forwardingDirectlyToViewPage
	 */
	public boolean isShowBackButton() {
		return showBackButton;
	}

	/**
	 * @param forwardingDirectlyToViewPage the forwardingDirectlyToViewPage to set
	 */
	public void setShowBackButton(boolean showBackButton) {
		this.showBackButton = showBackButton;
	}

	/**
	 * @return the otherThanInternalUserPresent
	 */
	public boolean isOtherThanInternalUserPresent() {
		return otherThanInternalUserPresent;
	}

	/**
	 * @param otherThanInternalUserPresent the otherThanInternalUserPresent to set
	 */
	public void setOtherThanInternalUserPresent(boolean otherThanInternalUserPresent) {
		this.otherThanInternalUserPresent = otherThanInternalUserPresent;
	}
	
	public boolean isShowChangeHistoryLink() {
		return showChangeHistoryLink;
	}

	public void setShowChangeHistoryLink(boolean showChangeHistoryLink) {
		this.showChangeHistoryLink = showChangeHistoryLink;
	}

	/**
	 * Method to reset all the data set by the form when the user clicks on the cancel button 
	 * on the edit page to go to the view page
	 */
	public void resetData() {
		this.actualFeeItemsList = new ArrayList<FeeDataVO>();
		this.feeLabelList = new ArrayList<FeeDataVO>();
		this.additionalFeeItemsList = new ArrayList<FeeDataVO>();
		this.displayFeeItemsList = new ArrayList<FeeDataVO>();
		this.previewFeeItemsList = new ArrayList<FeeDataVO>();
		this.amountTypeList = new ArrayList<String>();
		this.lastUpdatedDate = null;
		this.lastUpdatedUserId = 0;
		this.lastUpdatedUserName = null;
		this.tpaUserManager = false;
		this.editPageModified=false;
		clonedForm = null;
	}

	/**
	 * Get clonedForm
	 * 
	 * @return  clonedForm
	 */
	public CloneableForm getClonedForm() {
		return clonedForm;
	}

	/**
	 * Clear the cloneForm
	 */
	public void clear( HttpServletRequest request) {
		clonedForm = null;
	}
	
	/**
	 * Store the clone form to cloneForm
	 */
	public void storeClonedForm() {
		clonedForm = (CloneableForm) cloneForm();
	}

	/**
	 * method to clone the form and set the two lists for standard fee data and
	 * custom fee data obtained from the database.This will be required for
	 * matching the modified lists and filtering the modified items to the
	 * preview list.
	 * 
	 * @return CloneableForm
	 */
	private CloneableForm cloneForm() {

		TpaStandardFeeScheduleForm cloneForm = new TpaStandardFeeScheduleForm();

		List<FeeDataVO> standardFeeItemList = new ArrayList<FeeDataVO>();
		List<FeeDataVO> customFeeItemList = new ArrayList<FeeDataVO>();

		for (FeeDataVO vo : getFeeLabelList()) {
			standardFeeItemList.add(new FeeDataVO(vo.getFeeCode(), vo
					.getFeeDescription(), vo.getAmountType(), vo
					.getAmountValue(), vo.getNotes()));
		}
		for (FeeDataVO vo : getAdditionalFeeItemsList()) {
			customFeeItemList.add(new FeeDataVO(vo.getFeeCode(), vo
					.getFeeDescription(), vo.getAmountType(), vo
					.getAmountValue(), vo.getNotes()));
		}

		cloneForm.setFeeLabelList(standardFeeItemList);
		cloneForm.setAdditionalFeeItemsList(customFeeItemList);

		return cloneForm;

	}	

}
