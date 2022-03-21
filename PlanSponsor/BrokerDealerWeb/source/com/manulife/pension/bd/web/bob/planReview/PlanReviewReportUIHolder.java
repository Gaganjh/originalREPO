package com.manulife.pension.bd.web.bob.planReview;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.manulife.pension.bd.web.bob.planReview.util.PlanReviewReportUtils;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.planReview.valueobject.PeriodEndingReportDateVO;
import com.manulife.pension.service.planReview.valueobject.PrintDocumentPackgeVo;
import com.manulife.pension.service.planReview.valueobject.ShippingVO;
import com.manulife.util.render.DateRender;
import com.manulife.util.render.RenderConstants;

public class PlanReviewReportUIHolder implements Serializable{

	private static String FILE_JPG_EXTENSION = ".jpg";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int contractNumber;	

	private int noOfContracts;	
	private boolean contractSelected;
    private boolean selectedperformanceAndExpenseRatio;
	private List<PeriodEndingReportDateVO> reportMonthEndDates = new ArrayList<PeriodEndingReportDateVO>();
	private List<LabelValueBean> industrySegementOptions = new ArrayList<LabelValueBean>();
	private List<PrintDocumentPackgeVo> printRequestedVoList = new ArrayList<PrintDocumentPackgeVo>();
	private String contractName;
	private String selectedReportMonthEndDate;
	private String selectedIndustrySegment;
    private String presenterName;
    private String coverImageName;
    private String logoImageName;
    private boolean isUploadLogoImage=false;
    private boolean isUserUploadedLogoImage=false;
    
    private int defaultCmaCoverPageImageId;
   // CompletionStatus
    private String status = "In Progress";
    private String statusImage = "/assets/unmanaged/images/ajax-wait-indicator.gif";
    private String statusImageTitle = StringUtils.EMPTY;
    
    private boolean planReviewReportPdfsSelected;
	private boolean exeSummaryPdfsSelected;
    private String numberOfCopies;
    private boolean printContractSelected;
    private String requestedBy = "Assistant1";
	private String requestedFor = "Broker1";
	private String outputType = "Pdf & Print";

	private ShippingVO shippingVO;
	private String contractStatusCode;
	private Date  contractEffectiveDate;
	private Date  contractStatusEffectiveDate;
	private boolean enable ;
	private String productId =null; 
	private Timestamp  createdTimeStamap;
	private String agencySellerId = null;
	
	private MultipartFile userUploadedCoverPage = null;
	private CoverPageImage cmaCoverPageImage;
	private MultipartFile userUploadedLogoPage = null;
	
	private boolean definedBenefitContract;
	
	private String printConfirmNumber;

	public MultipartFile getUserUploadedCoverPage() {
		return userUploadedCoverPage;
	}

	public void setUserUploadedCoverPage(MultipartFile userUploadedCoverPage) {
		this.userUploadedCoverPage = userUploadedCoverPage;
	}

	public CoverPageImage getCmaCoverPageImage() {
		return cmaCoverPageImage;
	}

	public void setCmaCoverPageImage(CoverPageImage cmaCoverPageImage) {
		this.cmaCoverPageImage = cmaCoverPageImage;
	}

	public int getDefaultCmaCoverPageImageId() {
		return defaultCmaCoverPageImageId;
	}

	public void setDefaultCmaCoverPageImageId(int defaultCmaCoverPageImageId) {
		this.defaultCmaCoverPageImageId = defaultCmaCoverPageImageId;
	}

	public boolean isUserUploadedLogoImage() {
		return isUserUploadedLogoImage;
	}

	public void setUserUploadedLogoImage(boolean isUserUploadedLogoImage) {
		this.isUserUploadedLogoImage = isUserUploadedLogoImage;
	}

	public Date getContractStatusEffectiveDate() {
		return contractStatusEffectiveDate;
	}

	public void setContractStatusEffectiveDate(Date contractStatusEffectiveDate) {
		this.contractStatusEffectiveDate = contractStatusEffectiveDate;
	}

	private String  recentReportMonthEndDate;
	private boolean  eligibleMonthEndDate;
	private boolean  eligibleContract;
	private String companyName;
	
	private String  productType;
	private int documentPakgId;
	private int activityId;
	private boolean  planReviewReportDisabled = false;
	private String  planReviewReportDisabledText = StringUtils.EMPTY;
	private boolean dataModified = false;
	private int publishDocumentActivityId;	
	private boolean cmaSelectedCoverImageIndicator=false;
	private boolean userSelectedCoverImageIndicator=false;
	
	
	public int getNoOfContracts() {
		return noOfContracts;
	}

	public void setNoOfContracts(int noOfContracts) {
		this.noOfContracts = noOfContracts;
	}

	
	
public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

public boolean isEligibleMonthEndDate() {
		return eligibleMonthEndDate;
	}

	public void setEligibleMonthEndDate(boolean eligibleMonthEndDate) {
		this.eligibleMonthEndDate = eligibleMonthEndDate;
	}

	public boolean isEligibleContract() {
		return eligibleContract;
	}

	public void setEligibleContract(boolean eligibleContract) {
		this.eligibleContract = eligibleContract;
	}

/**
 * Retrieves the recent active report month end date
 * @return  
 */
	public String getRecentReportMonthEndDate() {
		if (recentReportMonthEndDate == null && reportMonthEndDates != null
				&& !reportMonthEndDates.isEmpty()) {
			recentReportMonthEndDate = PlanReviewReportUtils
					.getRecentPeriodEndingDate(reportMonthEndDates);
		}
		return recentReportMonthEndDate;
	}

	public void setRecentReportMonthEndDate(String recentReportMonthEndDate) {
		this.recentReportMonthEndDate = recentReportMonthEndDate;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getContractStatusCode() {
		return contractStatusCode;
	}

	public void setContractStatusCode(String contractStatusCode) {
		this.contractStatusCode = contractStatusCode;
	}

	public Date getContractEffectiveDate() {
		return contractEffectiveDate;
	}

	public void setContractEffectiveDate(Date contractEffectiveDate) {
		this.contractEffectiveDate = contractEffectiveDate;
	}

	public String getCoverImageName() {

		if (StringUtils.isBlank(coverImageName)) {
			return StringUtils.EMPTY;
		} else {
			if (coverImageName.length() > 15) {
				return coverImageName.substring(0, 15);
			}
		}
		return coverImageName;
	}
	
	public String getCoverImageNameTitle() {

		if (StringUtils.isNotBlank(coverImageName)
				&& isUserSelectedCoverImageIndicator()) {
			return coverImageName + FILE_JPG_EXTENSION;
		}

		return coverImageName;
	}

	public boolean isCmaSelectedCoverImageIndicator() {
		return cmaSelectedCoverImageIndicator;
	}

	public void setCmaSelectedCoverImageIndicator(
			boolean cmaSelectedCoverImageIndicator) {
		this.cmaSelectedCoverImageIndicator = cmaSelectedCoverImageIndicator;
	}

	public boolean isUserSelectedCoverImageIndicator() {
		return userSelectedCoverImageIndicator;
	}

	public void setUserSelectedCoverImageIndicator(
			boolean userSelectedCoverImageIndicator) {
		this.userSelectedCoverImageIndicator = userSelectedCoverImageIndicator;
	}

	public void setCoverImageName(String coverImageName) {
		this.coverImageName = coverImageName;
	}

	public String getLogoImageName() {
		
		if (StringUtils.isBlank(logoImageName)) {
			return StringUtils.EMPTY;
		} else {
			if (logoImageName.length() > 15) {
				return logoImageName.substring(0, 15);
			}
		}

		return logoImageName;
	}
	
	public String getLogoImageNameTitle() {
		
		if(StringUtils.isNotBlank(logoImageName)) {
			return logoImageName + FILE_JPG_EXTENSION;
		}
		
		return logoImageName;
	}

	public void setLogoImageName(String logoImageName) {
		this.logoImageName = logoImageName;
	}


	public ShippingVO getShippingVO() {
		return shippingVO;
	}

	public void setShippingVO(ShippingVO shippingVO) {
		this.shippingVO = shippingVO;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getRequestedFor() {
		return requestedFor;
	}

	public void setRequestedFor(String requestedFor) {
		this.requestedFor = requestedFor;
	}

	public String getOutputType() {
		return outputType;
	}

	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}

	public boolean isPrintContractSelected() {
		return printContractSelected;
	}

	public void setPrintContractSelected(boolean printContractSelected) {
		this.printContractSelected = printContractSelected;
	}

    
	public String getContractName() {
		if(StringUtils.isEmpty(contractName)){
			return  StringUtils.EMPTY;
		}
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public int getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(int contractNumber) {
		this.contractNumber = contractNumber;
	}
	public String getSelectedReportMonthEndDate() {
		return selectedReportMonthEndDate;
	}
	public void setSelectedReportMonthEndDate(String selectedReportMonthEndDate) {
		this.selectedReportMonthEndDate = selectedReportMonthEndDate;
	}
	public String getSelectedIndustrySegment() {
		if(StringUtils.isEmpty(selectedIndustrySegment)){
			return StringUtils.EMPTY;
		}
		return selectedIndustrySegment;
	}
	public void setSelectedIndustrySegment(String selectedIndustrySegment) {
		this.selectedIndustrySegment = selectedIndustrySegment;
	}
	public List<LabelValueBean> getIndustrySegementOptions() {
		return industrySegementOptions;
	}
	public void setIndustrySegementOptions(List<LabelValueBean> industrySegementOptions) {
		this.industrySegementOptions = industrySegementOptions;
	}
	public boolean isContractSelected() {
		return contractSelected;
	}
	public void setContractSelected(boolean contractSelected) {
		this.contractSelected = contractSelected;
	}
	public boolean isSelectedperformanceAndExpenseRatio() {
		return selectedperformanceAndExpenseRatio;
	}
	public void setSelectedperformanceAndExpenseRatio(
			boolean selectedperformanceAndExpenseRatio) {
		this.selectedperformanceAndExpenseRatio = selectedperformanceAndExpenseRatio;
	}
	public List<PeriodEndingReportDateVO> getReportMonthEndDates() {
		return reportMonthEndDates;
	}
	public void setReportMonthEndDates(List<PeriodEndingReportDateVO> reportMonthEndDates) {
		this.reportMonthEndDates = reportMonthEndDates;
	}
	public String getPresenterName() {
		return presenterName;
	}
	public void setPresenterName(String presenterName) {
		this.presenterName = presenterName;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String requestStatus) {
		this.status = requestStatus;
	}
	
	
	
	public PlanReviewReportUIHolder cloneObject() throws SystemException {
		try {
			return (PlanReviewReportUIHolder) BeanUtils.cloneBean(this);
		} catch (IllegalAccessException e) {
			throw new SystemException(e.getMessage());
		} catch (InstantiationException e) {
			throw new SystemException(e.getMessage());
		} catch (InvocationTargetException e) {
			throw new SystemException(e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new SystemException(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @return boolean
	 */

	public boolean equals(Object obj) {

		if (obj == null || !(obj instanceof PlanReviewReportUIHolder)) {
			return false;
		}
		PlanReviewReportUIHolder vo = (PlanReviewReportUIHolder) obj;

		if (Integer.valueOf(contractNumber).compareTo(
				Integer.valueOf(vo.getContractNumber())) == 0) {
			return true;
		}

		return false;

	}

	public void setNumberOfCopies(String numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}

	public String getNumberOfCopies() {
		return numberOfCopies;
	}

	public List<PrintDocumentPackgeVo> getPrintRequestedVoList() {
		return printRequestedVoList;
	}

	public void setPrintRequestedVoList(
			List<PrintDocumentPackgeVo> printRequestedVoList) {
		this.printRequestedVoList = printRequestedVoList;
	}

	public PrintDocumentPackgeVo getPrintRequestedVoList(int index) {
		if (this.printRequestedVoList == null) {
			this.setPrintRequestedVoList(new ArrayList<PrintDocumentPackgeVo>());
		}

		int listSize = this.printRequestedVoList.size();

		if (index >= listSize) {

			throw new IllegalArgumentException("Invalid accessing the index: "
					+ index
					+ ", from displayCotractReviewReports list of size: "
					+ listSize);
		}

		return printRequestedVoList.get(index);
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setDocumentPakgId(int documentPakgId) {
		this.documentPakgId = documentPakgId;
	}

	public int getDocumentPakgId() {
		return documentPakgId;
	}

	public boolean isUploadLogoImage() {
		return isUploadLogoImage;
	}

	public void setUploadLogoImage(boolean isUploadLogoImage) {
		this.isUploadLogoImage = isUploadLogoImage;
	}

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public boolean getPlanReviewReportDisabled() {
		return planReviewReportDisabled;
	}

	public void setPlanReviewReportDisabled(boolean planReviewReportDiesabled) {
		this.planReviewReportDisabled = planReviewReportDiesabled;
	}

	public String getPlanReviewReportDisabledText() {
		return planReviewReportDisabledText;
	}

	public void setPlanReviewReportDisabledText(
			String planReviewReportDiesabledText) {
		this.planReviewReportDisabledText = planReviewReportDiesabledText;
	}

	public void setDataModified(boolean dataModified) {
		this.dataModified = dataModified;
	}

	public boolean isDataModified() {
		return dataModified;
	}

	public int getPublishDocumentActivityId() {
		return publishDocumentActivityId;
	}

	public void setPublishDocumentActivityId(int publishDocumentActivityId) {
		this.publishDocumentActivityId = publishDocumentActivityId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public MultipartFile getUserUploadedLogoPage() {
		return userUploadedLogoPage;
	}

	public void setUserUploadedLogoPage(MultipartFile userUploadedLogoPage) {
		this.userUploadedLogoPage = userUploadedLogoPage;
	}

	public Timestamp getCreatedTimeStamap() {
		return createdTimeStamap;
	}

	public void setCreatedTimeStamap(Timestamp createdTimeStamap) {
		this.createdTimeStamap = createdTimeStamap;
	}

	public boolean isPlanReviewReportPdfsSelected() {
		return planReviewReportPdfsSelected;
	}

	public void setPlanReviewReportPdfsSelected(boolean planReviewReportPdfsSelected) {
		this.planReviewReportPdfsSelected = planReviewReportPdfsSelected;
	}

	public boolean isExeSummaryPdfsSelected() {
		return exeSummaryPdfsSelected;
	}

	public void setExeSummaryPdfsSelected(boolean exeSummaryPdfsSelected) {
		this.exeSummaryPdfsSelected = exeSummaryPdfsSelected;
	}

	public String getAgencySellerId() {
		return agencySellerId;
	}

	public void setAgencySellerId(String agencySellerId) {
		this.agencySellerId = agencySellerId;
	}

	public String getPrintConfirmNumber() {
		return printConfirmNumber;
	}

	public void setPrintConfirmNumber(String printConfirmNumber) {
		this.printConfirmNumber = printConfirmNumber;
	}

	public boolean isDefinedBenefitContract() {
		return definedBenefitContract;
	}

	public void setDefinedBenefitContract(boolean definedBenefitContract) {
		this.definedBenefitContract = definedBenefitContract;
	}

	public String getStatusImage() {
		return statusImage;
	}

	public void setStatusImage(String statusImage) {
		this.statusImage = statusImage;
	}

	public String getStatusImageTitle() {
		return statusImageTitle;
	}

	public void setStatusImageTitle(String statusImageTitle) {
		this.statusImageTitle = statusImageTitle;
	}

	public String getFormattedRequestedDate() {
		
		if(createdTimeStamap == null) {
			return StringUtils.EMPTY;
		}
		
		return DateRender.formatByPattern(
				new Date(createdTimeStamap
						.getTime()), StringUtils.EMPTY,
				RenderConstants.LONG_TIMESTAMP_MDY_SLASHED);
		
	}
	
}
