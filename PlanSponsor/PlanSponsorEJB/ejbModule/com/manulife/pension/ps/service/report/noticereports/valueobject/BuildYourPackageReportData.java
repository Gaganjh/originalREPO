package com.manulife.pension.ps.service.report.noticereports.valueobject;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.ps.service.report.noticereports.reporthandler.PlanSponsorWebsiteReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Provides report data for Notice Manager Build Your Package Report.
 * 
 */
public class BuildYourPackageReportData extends ReportData {

    private static final long serialVersionUID = -515659860316332870L;

    public static final String REPORT_NAME = "Build Your Package as of";

    /** The filter parameter for a String object contract number */
    public static final String FILTER_CONTRACT_NUMBER = "contractNumber";

    /** The filter parameter for the fromDate */
    public static final String FILTER_FROM_DATE = "fromDate";

    /** The filter parameter for the toDate */
    public static final String FILTER_TO_DATE = "toDate";

    /** The filter parameter for the task */
    public static final String FILTER_TASK = "task";

    public static final String TASK_PRINT = "print";

    private Integer contractNo;

    private Date fromDate;

    private Date toDate;

    private Integer numberOfContractsUsingMailService;

    private Integer totalOrders;

    private Integer numberOfContractsDownloadedPackage;

    private Integer numberOfContractsUsingMailAndDownload;

    private Integer numberOfRepeatContracts;

    private BigDecimal averageNumberOfMailingsPerContract;

    private List<String> monthsWithMostMailings;

    private Integer newBusinessContractsCount;

    private BigDecimal newBusinessContractsPercentage;

    private Integer inforceContractsCount;

    private BigDecimal inforceContractsPercentage;

    private BigDecimal userPreferenceMailPercentage;

    private BigDecimal userPreferenceDownloadPercentage;

    private Integer totalCompletedOrders;

    private Integer pageCount;

    private Integer particpantCount;

    private BigDecimal totalOrderCosts;

    private BigDecimal blackWhiteOrdersPercentage;

    private BigDecimal colorOrdersPercentage;

    private BigDecimal averageNumberOfPagesPerOrder;

    private BigDecimal averageNumberOfPartipicantsPerOrder;

    private BigDecimal amountPaidByJohnHancock;

    private BigDecimal amountPaidByJohnHancockTotalCare;

    private BigDecimal amountPaidByJohnHancockTotalCarePercentage;

    private BigDecimal ordersByPlanSponsorsPercentage;

    private BigDecimal ordersByIntermediaryContactPercentage;

    private BigDecimal ordersByTPAPercentage;

    private BigDecimal ordersByTotalCarePercentage;

    private BigDecimal averageCostPerOrder;

    private BigDecimal orderStapledPercentage;

    private BigDecimal bookletOrdersPercentage;

    private Integer numberOfPostageOrders;

    private Integer numberOfBulkOrders;

    private BigDecimal sealedOrdersPercentage;
   
    private FastDateFormat currentDateDisplay = FastDateFormat.getInstance("MMM dd, yyyy");


    /**
     * Constructor
     * 
     * @param criteria
     * @param totalCount
     */
    public BuildYourPackageReportData(ReportCriteria criteria, int totalCount) {
        super(criteria, totalCount);

    }

    public BuildYourPackageReportData() {
        super(new ReportCriteria(PlanSponsorWebsiteReportHandler.REPORT_ID), 0);

    }

    /**
     * @return the contractNo
     */
    public Integer getContractNo() {
        return contractNo;
    }

    /**
     * @param contractNo the contractNo to set
     */
    public void setContractNo(Integer contractNo) {
        this.contractNo = contractNo;
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * Returns number Of Contracts Using Mail Service.
     * 
     * @return numberOfContractsUsingMailService
     */
    public Integer getNumberOfContractsUsingMailService() {
        return numberOfContractsUsingMailService;
    }

    /**
     * Sets number Of Contracts Using Mail Service.
     * 
     * @param numberOfContractsUsingMailService
     */
    public void setNumberOfContractsUsingMailService(Integer numberOfContractsUsingMailService) {
        this.numberOfContractsUsingMailService = numberOfContractsUsingMailService;
    }

    /**
     * Returns number of Total Orders
     * 
     * @return totalOrders
     */
    public Integer getTotalOrders() {
        return totalOrders;
    }

    /**
     * Sets number of Total Orders
     * 
     * @param totalOrders
     */
    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    /**
     * Gets Number Of Contracts Downloaded Package
     * 
     * @return numberOfContractsDownloadedPackage
     */
    public Integer getNumberOfContractsDownloadedPackage() {
        return numberOfContractsDownloadedPackage;
    }

    /**
     * Sets Number Of Contracts Downloaded Package
     * 
     * @param numberOfContractsDownloadedPackage
     */
    public void setNumberOfContractsDownloadedPackage(Integer numberOfContractsDownloadedPackage) {
        this.numberOfContractsDownloadedPackage = numberOfContractsDownloadedPackage;
    }

    /**
     * Gets Number Of Contracts Using Mail And Download
     * 
     * @return numberOfContractsUsingMailAndDownload
     */
    public Integer getNumberOfContractsUsingMailAndDownload() {
        return numberOfContractsUsingMailAndDownload;
    }

    /**
     * Sets Number Of Contracts Using Mail And Download
     * 
     * @param numberOfContractsUsingMailAndDownload
     */
    public void setNumberOfContractsUsingMailAndDownload(
            Integer numberOfContractsUsingMailAndDownload) {
        this.numberOfContractsUsingMailAndDownload = numberOfContractsUsingMailAndDownload;
    }

    /**
     * Gets number Of Repeat Contracts
     * 
     * @return numberOfRepeatContracts
     */
    public Integer getNumberOfRepeatContracts() {
        return numberOfRepeatContracts;
    }

    /**
     * Sets number Of Repeat Contracts
     * 
     * @param numberOfRepeatContracts
     */
    public void setNumberOfRepeatContracts(Integer numberOfRepeatContracts) {
        this.numberOfRepeatContracts = numberOfRepeatContracts;
    }

    /**
     * Gets Average Number Of Mailings Per Contract
     * 
     * @return averageNumberOfMailingsPerContract
     */
    public BigDecimal getAverageNumberOfMailingsPerContract() {
        return averageNumberOfMailingsPerContract;
    }

    /**
     * Sets Average Number Of Mailings Per Contract
     * 
     * @param averageNumberOfMailingsPerContract
     */
    public void setAverageNumberOfMailingsPerContract(BigDecimal averageNumberOfMailingsPerContract) {
        this.averageNumberOfMailingsPerContract = averageNumberOfMailingsPerContract;
    }

    /**
     * Gets Months With Most Mailings
     * 
     * @return monthsWithMostMailings
     */
    public List<String> getMonthsWithMostMailings() {
        return monthsWithMostMailings;
    }

    public void setMonthsWithMostMailings(List<String> monthsWithMostMailings) {
        this.monthsWithMostMailings = monthsWithMostMailings;
    }
    /**
     * 
     * @return date
     */
    public String getTodayDate() {
		return currentDateDisplay.format(Calendar.getInstance().getTime());
	}

    /**
     * Gets New Business Contracts Count
     * 
     * @return newBusinessContractsCount
     */
    public Integer getNewBusinessContractsCount() {
        return newBusinessContractsCount;
    }

    /**
     * Sets New Business Contracts Count
     * 
     * @param newBusinessContractsCount
     */
    public void setNewBusinessContractsCount(Integer newBusinessContractsCount) {
        this.newBusinessContractsCount = newBusinessContractsCount;
    }

    /**
     * Gets New Business Contracts Percentage
     * 
     * @return newBusinessContractsPercentage
     */
    public BigDecimal getNewBusinessContractsPercentage() {
        return newBusinessContractsPercentage;
    }

    /**
     * Sets New Business Contracts Percentage
     * 
     * @param newBusinessContractsPercentage
     */
    public void setNewBusinessContractsPercentage(BigDecimal newBusinessContractsPercentage) {
        this.newBusinessContractsPercentage = newBusinessContractsPercentage;
    }

    /**
     * Gets Inforce Contracts Count.
     * 
     * @return inforceContractsCount
     */
    public Integer getInforceContractsCount() {
        return inforceContractsCount;
    }

    /**
     * Sets Inforce Contracts Count.
     * 
     * @param inforceContractsCount
     */
    public void setInforceContractsCount(Integer inforceContractsCount) {
        this.inforceContractsCount = inforceContractsCount;
    }

    /**
     * Gets Inforce Contracts Percentage
     * 
     * @return inforceContractsPercentage
     */
    public BigDecimal getInforceContractsPercentage() {
        return inforceContractsPercentage;
    }

    /**
     * Sets Inforce Contracts Percentage
     * 
     * @param inforceContractsPercentage
     */
    public void setInforceContractsPercentage(BigDecimal inforceContractsPercentage) {
        this.inforceContractsPercentage = inforceContractsPercentage;
    }

    /**
     * Gets User Preference Mail Percentage.
     * 
     * @return userPreferenceMailPercentage
     */
    public BigDecimal getUserPreferenceMailPercentage() {
        return userPreferenceMailPercentage;
    }

    /**
     * Sets User Preference Mail Percentage.
     * 
     * @param userPreferenceMailPercentage
     */
    public void setUserPreferenceMailPercentage(BigDecimal userPreferenceMailPercentage) {
        this.userPreferenceMailPercentage = userPreferenceMailPercentage;
    }

    /**
     * Gets User Preference Download Percentage
     * 
     * @return userPreferenceDownloadPercentage
     */
    public BigDecimal getUserPreferenceDownloadPercentage() {
        return userPreferenceDownloadPercentage;
    }

    /**
     * Sets User Preference Download Percentage
     * 
     * @param userPreferenceDownloadPercentage
     */
    public void setUserPreferenceDownloadPercentage(BigDecimal userPreferenceDownloadPercentage) {
        this.userPreferenceDownloadPercentage = userPreferenceDownloadPercentage;
    }

    /**
     * Gets Total Completed Orders
     * 
     * @return totalCompletedOrders
     */
    public Integer getTotalCompletedOrders() {
        return totalCompletedOrders;
    }

    /**
     * Sets Total Completed Orders
     * 
     * @param totalCompletedOrders
     */
    public void setTotalCompletedOrders(Integer totalCompletedOrders) {
        this.totalCompletedOrders = totalCompletedOrders;
    }

    /**
     * Gets Page Count.
     * 
     * @return pageCount
     */
    public Integer getPageCount() {
        return pageCount;
    }

    /**
     * Sets Page Count.
     * 
     * @param pageCount
     */
    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * Gets Participants Count
     * 
     * @return particpantCount
     */
    public Integer getParticpantCount() {
        return particpantCount;
    }

    /**
     * Sets Participants Count
     * 
     * @param particpantCount
     */
    public void setParticpantCount(Integer particpantCount) {
        this.particpantCount = particpantCount;
    }

    /**
     * Gets Total Order Costs
     * 
     * @return totalOrderCosts
     */
    public BigDecimal getTotalOrderCosts() {
        return totalOrderCosts;
    }

    /**
     * Sets Total Order Costs
     * 
     * @param totalOrderCosts
     */
    public void setTotalOrderCosts(BigDecimal totalOrderCosts) {
        this.totalOrderCosts = totalOrderCosts;
    }

    /**
     * Gets BlackWhite Orders Percentage
     * 
     * @return blackWhiteOrdersPercentage
     */
    public BigDecimal getBlackWhiteOrdersPercentage() {
        return blackWhiteOrdersPercentage;
    }

    /**
     * Sets BlackWhite Orders Percentage
     * 
     * @param blackWhiteOrdersPercentage
     */
    public void setBlackWhiteOrdersPercentage(BigDecimal blackWhiteOrdersPercentage) {
        this.blackWhiteOrdersPercentage = blackWhiteOrdersPercentage;
    }

    /**
     * Gets Color Orders Percentage
     * 
     * @return colorOrdersPercentage
     */
    public BigDecimal getColorOrdersPercentage() {
        return colorOrdersPercentage;
    }

    /**
     * Sets Color Orders Percentage
     * 
     * @param colorOrdersPercentage
     */
    public void setColorOrdersPercentage(BigDecimal colorOrdersPercentage) {
        this.colorOrdersPercentage = colorOrdersPercentage;
    }

    /**
     * Gets Average Number Of Pages Per Order.
     * 
     * @return averageNumberOfPagesPerOrder
     */
    public BigDecimal getAverageNumberOfPagesPerOrder() {
        return averageNumberOfPagesPerOrder;
    }

    /**
     * Sets Average Number Of Pages Per Order.
     * 
     * @param averageNumberOfPagesPerOrder
     */
    public void setAverageNumberOfPagesPerOrder(BigDecimal averageNumberOfPagesPerOrder) {
        this.averageNumberOfPagesPerOrder = averageNumberOfPagesPerOrder;
    }

    /**
     * Gets Average Number Of Participants Per Order
     * 
     * @return averageNumberOfPartipicantsPerOrder
     */
    public BigDecimal getAverageNumberOfPartipicantsPerOrder() {
        return averageNumberOfPartipicantsPerOrder;
    }

    /**
     * Sets Average Number Of Participants Per Order
     * 
     * @param averageNumberOfPartipicantsPerOrder
     */
    public void setAverageNumberOfPartipicantsPerOrder(
            BigDecimal averageNumberOfPartipicantsPerOrder) {
        this.averageNumberOfPartipicantsPerOrder = averageNumberOfPartipicantsPerOrder;
    }

    /**
     * Gets Amount Paid By John Hancock
     * 
     * @return amountPaidByJohnHancock
     */
    public BigDecimal getAmountPaidByJohnHancock() {
        return amountPaidByJohnHancock;
    }

    /**
     * Sets Amount Paid By John Hancock
     * 
     * @param amountPaidByJohnHancock
     */
    public void setAmountPaidByJohnHancock(BigDecimal amountPaidByJohnHancock) {
        this.amountPaidByJohnHancock = amountPaidByJohnHancock;
    }

    /**
     * Gets Amount Paid By JohnHancock TotalCare
     * 
     * @return amountPaidByJohnHancockTotalCare
     */
    public BigDecimal getAmountPaidByJohnHancockTotalCare() {
        return amountPaidByJohnHancockTotalCare;
    }

    /**
     * Sets Amount Paid By JohnHancock TotalCare
     * 
     * @param amountPaidByJohnHancockTotalCare
     */
    public void setAmountPaidByJohnHancockTotalCare(BigDecimal amountPaidByJohnHancockTotalCare) {
        this.amountPaidByJohnHancockTotalCare = amountPaidByJohnHancockTotalCare;
    }

    /**
     * Gets Amount Paid By JohnHancock TotalCare Percentage
     * 
     * @return amountPaidByJohnHancockTotalCarePercentage
     */
    public BigDecimal getAmountPaidByJohnHancockTotalCarePercentage() {
        return amountPaidByJohnHancockTotalCarePercentage;
    }

    /**
     * Sets Amount Paid By JohnHancock TotalCare Percentage
     * 
     * @param amountPaidByJohnHancockTotalCarePercentage
     */
    public void setAmountPaidByJohnHancockTotalCarePercentage(
            BigDecimal amountPaidByJohnHancockTotalCarePercentage) {
        this.amountPaidByJohnHancockTotalCarePercentage = amountPaidByJohnHancockTotalCarePercentage;
    }

    /**
     * Gets Orders By PlanSponsors Percentage
     * 
     * @return ordersByPlanSponsorsPercentage
     */
    public BigDecimal getOrdersByPlanSponsorsPercentage() {
        return ordersByPlanSponsorsPercentage;
    }

    /**
     * Sets Orders By PlanSponsors Percentage
     * 
     * @param ordersByPlanSponsorsPercentage
     */
    public void setOrdersByPlanSponsorsPercentage(BigDecimal ordersByPlanSponsorsPercentage) {
        this.ordersByPlanSponsorsPercentage = ordersByPlanSponsorsPercentage;
    }

    /**
     * Gets Orders By IntermediaryContact Percentage
     * 
     * @return ordersByIntermediaryContactPercentage
     */
    public BigDecimal getOrdersByIntermediaryContactPercentage() {
        return ordersByIntermediaryContactPercentage;
    }

    /**
     * Sets Orders By IntermediaryContact Percentage
     * 
     * @param ordersByIntermediaryContactPercentage
     */
    public void setOrdersByIntermediaryContactPercentage(
            BigDecimal ordersByIntermediaryContactPercentage) {
        this.ordersByIntermediaryContactPercentage = ordersByIntermediaryContactPercentage;
    }

    /**
     * Gets Orders By TPAPercentage
     * 
     * @return ordersByTPAPercentage
     */
    public BigDecimal getOrdersByTPAPercentage() {
        return ordersByTPAPercentage;
    }

    /**
     * Sets Orders By TPAPercentage
     * 
     * @param ordersByTPAPercentage
     */
    public void setOrdersByTPAPercentage(BigDecimal ordersByTPAPercentage) {
        this.ordersByTPAPercentage = ordersByTPAPercentage;
    }

    /**
     * Gets Orders By TotalCare Percentage
     * 
     * @return ordersByTotalCarePercentage
     */
    public BigDecimal getOrdersByTotalCarePercentage() {
        return ordersByTotalCarePercentage;
    }

    /**
     * Sets Orders By TotalCare Percentage
     * 
     * @param ordersByTotalCarePercentage
     */
    public void setOrdersByTotalCarePercentage(BigDecimal ordersByTotalCarePercentage) {
        this.ordersByTotalCarePercentage = ordersByTotalCarePercentage;
    }

    /**
     * Gets Average Cost Per Order
     * 
     * @return averageCostPerOrder
     */
    public BigDecimal getAverageCostPerOrder() {
        return averageCostPerOrder;
    }

    /**
     * Sets Average Cost Per Order
     * 
     * @param averageCostPerOrder
     */
    public void setAverageCostPerOrder(BigDecimal averageCostPerOrder) {
        this.averageCostPerOrder = averageCostPerOrder;
    }

    /**
     * Gets Order Stapled Percentage
     * 
     * @return orderStapledPercentage
     */
    public BigDecimal getOrderStapledPercentage() {
        return orderStapledPercentage;
    }

    /**
     * Sets Order Stapled Percentage
     * 
     * @param orderStapledPercentage
     */
    public void setOrderStapledPercentage(BigDecimal orderStapledPercentage) {
        this.orderStapledPercentage = orderStapledPercentage;
    }

    /**
     * Gets Booklet Orders Percentage
     * 
     * @return bookletOrdersPercentage
     */
    public BigDecimal getBookletOrdersPercentage() {
        return bookletOrdersPercentage;
    }

    /**
     * Sets Booklet Orders Percentage
     * 
     * @param bookletOrdersPercentage
     */
    public void setBookletOrdersPercentage(BigDecimal bookletOrdersPercentage) {
        this.bookletOrdersPercentage = bookletOrdersPercentage;
    }

    /**
     * Gets number of postage orders.
     * 
     * @return numberOfPostageOrders
     */
    public Integer getNumberOfPostageOrders() {
        return numberOfPostageOrders;
    }

    /**
     * Sets number of postage orders.
     * 
     * @param numberOfPostageOrders
     */
    public void setNumberOfPostageOrders(Integer numberOfPostageOrders) {
        this.numberOfPostageOrders = numberOfPostageOrders;
    }

    /**
     * Gets Number Of Bulk Orders.
     * 
     * @return numberOfBulkOrders
     */
    public Integer getNumberOfBulkOrders() {
        return numberOfBulkOrders;
    }

    /**
     * Sets Number Of Bulk Orders.
     * 
     * @param numberOfBulkOrders
     */
    public void setNumberOfBulkOrders(Integer numberOfBulkOrders) {
        this.numberOfBulkOrders = numberOfBulkOrders;
    }

    /**
     * Gets Sealed Orders Percentage
     * 
     * @return sealedOrdersPercentage.
     */
    public BigDecimal getSealedOrdersPercentage() {
        return sealedOrdersPercentage;
    }

    /**
     * Sets Sealed Orders Percentage
     * 
     * @param sealedOrdersPercentage
     */
    public void setSealedOrdersPercentage(BigDecimal sealedOrdersPercentage) {
        this.sealedOrdersPercentage = sealedOrdersPercentage;
    }

}
