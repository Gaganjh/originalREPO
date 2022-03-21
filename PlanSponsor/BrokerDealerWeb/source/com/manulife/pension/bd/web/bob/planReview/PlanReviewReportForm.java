package com.manulife.pension.bd.web.bob.planReview;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.manulife.pension.service.environment.valueobject.LabelValueBean;

/**
 * PlanReviewReportForm is the Action Form for PlanReviewReport Step1 and
 * Step2 Pages .
 * 
 * @author Vanikishore
 * 
 */
/**
 * @author akarave
 * 
 */
public class PlanReviewReportForm extends BasePlanReviewReportForm {

	private static final long serialVersionUID = 1L;

	private boolean requestBackFromStep2;

	private boolean rvpUser;

	private boolean definedBenefit;

	private boolean disabledContract;

	private boolean hasDefaultAddress;

	private String action;

	private List<PlanReviewReportUIHolder> contractReviewReportVOList;

	private List<PlanReviewReportUIHolder> displayContractReviewReports = new ArrayList<PlanReviewReportUIHolder>();
	
	private List<PlanReviewReportUIHolder> actualPlanReviewReports = new ArrayList<PlanReviewReportUIHolder>();

	private boolean allContractSelected;

	private boolean allRatioReportSelected;

	private boolean allContractPrintSelected;

	private boolean downloadContracts;

	private boolean downloadSumContracts;

	private boolean downloadAllContracts;

	private boolean downloadAllSumContracts;

	private int filterPrintConfirmId;

	private String filterContractNumber;

	private String filterContractName;

	private String pathValue;

	private String index;

	private boolean defaultAddressCheck;

	private int requestsProcessed = 0;

	private List<LabelValueBean> industrySegementOptions;

	private MultipartFile uploadImage;

	private MultipartFile uploadCoverImage;

	private Date recentReportMonthEndDate;

	private String uploadImageName;

	private String companyName;

	private String industryOptionSelected;
	private List<LabelValueBean> numberOfCopies;

	private boolean warningExist;

	private String bobResults;

	private CoverPageImage defaultCoverPageImage;

	private List<CoverPageImage> stockCoverPageImages = new ArrayList<CoverPageImage>();

	private String presenterName;
	
	private boolean errorExists = false;

	public String getPresenterName() {
		return presenterName;
	}

	public void setPresenterName(String presenterName) {
		this.presenterName = presenterName;
	}

	public boolean isDefinedBenefit() {
		return definedBenefit;
	}

	public void setDefinedBenefit(boolean definedBenefit) {
		this.definedBenefit = definedBenefit;
	}

	public boolean isDisabledContract() {
		return disabledContract;
	}

	public void setDisabledContract(boolean disabledContract) {
		this.disabledContract = disabledContract;
	}

	public CoverPageImage getDefaultCoverPageImage() {
		return defaultCoverPageImage;
	}

	public void setDefaultCoverPageImage(CoverPageImage defaultCoverPageImage) {
		this.defaultCoverPageImage = defaultCoverPageImage;
	}

	public List<CoverPageImage> getStockCoverPageImages() {
		return stockCoverPageImages;
	}

	public void setStockCoverPageImages(
			List<CoverPageImage> stockCoverPageImages) {
		this.stockCoverPageImages = stockCoverPageImages;
	}

	public boolean isRvpUser() {
		return rvpUser;
	}

	public void setRvpUser(boolean rvpUser) {
		this.rvpUser = rvpUser;
	}

	public boolean isHasDefaultAddress() {
		return hasDefaultAddress;
	}

	public void setHasDefaultAddress(boolean hasDefaultAddress) {
		this.hasDefaultAddress = hasDefaultAddress;
	}

	public boolean isWarningExist() {
		return warningExist;
	}

	public void setWarningExist(boolean warningExist) {
		this.warningExist = warningExist;
	}

	public String getIndustryOptionSelected() {
		return industryOptionSelected;
	}

	public void setIndustryOptionSelected(String industryOptionSelected) {
		this.industryOptionSelected = industryOptionSelected;
	}

	public List<LabelValueBean> getIndustrySegementOptions() {
		return industrySegementOptions;
	}

	public void setIndustrySegementOptions(
			List<LabelValueBean> industrySegementOptions) {
		this.industrySegementOptions = industrySegementOptions;
	}

	public int getRequestsProcessed() {
		return requestsProcessed;
	}

	public void setRequestsProcessed(int requestsProcessed) {
		this.requestsProcessed = requestsProcessed;
	}

	public boolean isDefaultAddressCheck() {
		return defaultAddressCheck;
	}

	public void setDefaultAddressCheck(boolean defaultAddressCheck) {
		this.defaultAddressCheck = defaultAddressCheck;
	}

	public int getFilterPrintConfirmId() {
		return filterPrintConfirmId;
	}

	public void setFilterPrintConfirmId(int filterPrintConfirmId) {
		this.filterPrintConfirmId = filterPrintConfirmId;
	}

	public String getFilterContractNumber() {
		return filterContractNumber;
	}

	public void setFilterContractNumber(String filterContractNumber) {
		this.filterContractNumber = filterContractNumber;
	}

	public String getFilterContractName() {
		return filterContractName;
	}

	public void setFilterContractName(String filterContractName) {
		this.filterContractName = filterContractName;
	}

	public boolean isAllContractPrintSelected() {
		return allContractPrintSelected;
	}

	public void setAllContractPrintSelected(boolean allContractPrintSelected) {
		this.allContractPrintSelected = allContractPrintSelected;
	}


	public boolean isDownloadContracts() {
		return downloadContracts;
	}

	public void setDownloadContracts(boolean downloadContracts) {
		this.downloadContracts = downloadContracts;
	}

	public boolean isDownloadSumContracts() {
		return downloadSumContracts;
	}

	public void setDownloadSumContracts(boolean downloadSumContracts) {
		this.downloadSumContracts = downloadSumContracts;
	}

	public boolean isDownloadAllSumContracts() {
		return downloadAllSumContracts;
	}

	public void setDownloadAllSumContracts(boolean downloadAllSumContracts) {
		this.downloadAllSumContracts = downloadAllSumContracts;
	}

	public boolean isDownloadAllContracts() {
		return downloadAllContracts;
	}

	public void setDownloadAllContracts(boolean downloadAllContracts) {
		this.downloadAllContracts = downloadAllContracts;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getRecentReportMonthEndDate() {
		return recentReportMonthEndDate;
	}

	public void setRecentReportMonthEndDate(Date recentReportMonthEndDate) {
		this.recentReportMonthEndDate = recentReportMonthEndDate;
	}

	public List<PlanReviewReportUIHolder> getContractReviewReportVOList() {
		return contractReviewReportVOList;
	}

	public int getContractReviewReportVOListSize() {

		if (this.contractReviewReportVOList == null) {
			this.setContractReviewReportVOList(new ArrayList<PlanReviewReportUIHolder>());
		}

		return contractReviewReportVOList.size();
	}

	public int getDisplayContractReviewReportsSize() {

		if (this.displayContractReviewReports == null) {
			this.setDisplayCotractReviewReports(new ArrayList<PlanReviewReportUIHolder>());
		}

		return displayContractReviewReports.size();

	}

	public void setContractReviewReportVOList(
			List<PlanReviewReportUIHolder> contractReviewReportVOList) {
		this.contractReviewReportVOList = contractReviewReportVOList;
	}

	public List<PlanReviewReportUIHolder> getDisplayContractReviewReports() {
		return displayContractReviewReports;
	}

	public void setDisplayCotractReviewReports(
			List<PlanReviewReportUIHolder> displayContractReviewReports) {
		this.displayContractReviewReports = displayContractReviewReports;
	}

	public PlanReviewReportUIHolder getDisplayContractReviewReportVO(int index) {
		if (this.displayContractReviewReports == null) {
			this.setDisplayCotractReviewReports(new ArrayList<PlanReviewReportUIHolder>());
		}

		int listSize = this.displayContractReviewReports.size();

		if (index >= listSize) {

			throw new IllegalArgumentException("Invalid accessing the index: "
					+ index
					+ ", from displayCotractReviewReports list of size: "
					+ listSize);
		}

		return displayContractReviewReports.get(index);
	}

	public int getSelectedRecordListSize() {

		return getDisplayContractReviewReports().size();
	}

	public boolean isRequestBackFromStep2() {
		return requestBackFromStep2;
	}

	public void setRequestBackFromStep2(boolean requestBackToStep1) {
		this.requestBackFromStep2 = requestBackToStep1;
	}

	public PlanReviewReportUIHolder getSelectedRecordVO(int index) {
		if (this.displayContractReviewReports == null) {
			this.setDisplayCotractReviewReports(new ArrayList<PlanReviewReportUIHolder>());
		}

		int listSize = this.displayContractReviewReports.size();

		if (index >= listSize) {

			for (int i = 0; i <= (index - listSize); i++) {
				this.displayContractReviewReports
						.add(new PlanReviewReportUIHolder());
			}
		}

		return (PlanReviewReportUIHolder) displayContractReviewReports
				.get(index);
	}

	public boolean isAllContractSelected() {
		return allContractSelected;
	}

	public void setAllContractSelected(boolean allContractSelected) {
		this.allContractSelected = allContractSelected;
	}

	public boolean isAllRatioReportSelected() {
		return allRatioReportSelected;
	}

	public void setAllRatioReportSelected(boolean allRatioReportSelected) {
		this.allRatioReportSelected = allRatioReportSelected;
	}

	public String getPathValue() {
		return pathValue;
	}

	public void setPathValue(String pathValue) {
		this.pathValue = pathValue;
	}

	public String getUploadImageName() {
		return uploadImageName;
	}

	public void setUploadImageName(String uploadImageName) {
		this.uploadImageName = uploadImageName;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public MultipartFile getUploadImage() {
		return uploadImage;
	}

	public void setUploadImage(MultipartFile uploadImage) {
		this.uploadImage = uploadImage;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBobResults() {
		return bobResults;
	}

	public void setBobResults(String bobResults) {
		this.bobResults = bobResults;
	}

	public List<LabelValueBean> getNumberOfCopies() {
		return numberOfCopies;
	}

	public void setNumberOfCopies(List<LabelValueBean> numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}

	public MultipartFile getUploadCoverImage() {
		return uploadCoverImage;
	}

	public void setUploadCoverImage(MultipartFile uploadCoverImage) {
		this.uploadCoverImage = uploadCoverImage;
	}

	public boolean isErrorExists() {
		return errorExists;
	}

	public void setErrorExists(boolean errorExists) {
		this.errorExists = errorExists;
	}

	public List<PlanReviewReportUIHolder> getActualPlanReviewReports() {
		return actualPlanReviewReports;
	}

	public void setActualPlanReviewReports(
			List<PlanReviewReportUIHolder> actualPlanReviewReports) {
		this.actualPlanReviewReports = actualPlanReviewReports;
	}

}
