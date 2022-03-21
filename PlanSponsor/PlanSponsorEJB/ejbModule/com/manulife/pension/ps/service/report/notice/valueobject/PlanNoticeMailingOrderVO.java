package com.manulife.pension.ps.service.report.notice.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PlanNoticeMailingOrderVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer orderNumber;
	private Integer contractId;
	private BigDecimal profileId;
	private LookupDescription planNoticeMailingOrderStatus;
	private Date orderStatusDate;
	private String colorPrintInd;
	private Integer totalPageCount;
	private Integer noOfParticipant;
	private BigDecimal totalMailingCost;
	private String mailingName;
	private String mailingAddress;
	private String vipInd;
	private String orderStapledInd;
	private String largeEnvelopeInd;
	private String bulkOrderInd;
	private String orderSealedInd;
	private String orderStatusCode;
	private Integer merrilOrderNumber;
	private String adressFileOption;
	
	public String getMailingAddress() {
		return mailingAddress;
	}
	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}
	/**
	 * @return the orderNumber
	 */
	public Integer getOrderNumber() {
		return orderNumber;
	}
	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
	/**
	 * @return the contractId
	 */
	public Integer getContractId() {
		return contractId;
	}
	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	/**
	 * @return the profileId
	 */
	public BigDecimal getProfileId() {
		return profileId;
	}
	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(BigDecimal profileId) {
		this.profileId = profileId;
	}
	/**
	 * @return the planNoticeMailingOrderStatus
	 */
	public LookupDescription getPlanNoticeMailingOrderStatus() {
		return planNoticeMailingOrderStatus;
	}
	/**
	 * @param planNoticeMailingOrderStatus the planNoticeMailingOrderStatus to set
	 */
	public void setPlanNoticeMailingOrderStatus(
			LookupDescription planNoticeMailingOrderStatus) {
		this.planNoticeMailingOrderStatus = planNoticeMailingOrderStatus;
	}
	/**
	 * @return the orderStatusDate
	 */
	public Date getOrderStatusDate() {
		return orderStatusDate;
	}
	/**
	 * @param orderStatusDate the orderStatusDate to set
	 */
	public void setOrderStatusDate(Date orderStatusDate) {
		this.orderStatusDate = orderStatusDate;
	}
	/**
	 * @return the colorPrintInd
	 */
	public String getColorPrintInd() {
		return colorPrintInd;
	}
	/**
	 * @param colorPrintInd the colorPrintInd to set
	 */
	public void setColorPrintInd(String colorPrintInd) {
		this.colorPrintInd = colorPrintInd;
	}
	/**
	 * @return the totalPageCount
	 */
	public Integer getTotalPageCount() {
		return totalPageCount;
	}
	/**
	 * @param totalPageCount the totalPageCount to set
	 */
	public void setTotalPageCount(Integer totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	/**
	 * @return the noOfParticipant
	 */
	public Integer getNoOfParticipant() {
		return noOfParticipant;
	}
	/**
	 * @param noOfParticipant the noOfParticipant to set
	 */
	public void setNoOfParticipant(Integer noOfParticipant) {
		this.noOfParticipant = noOfParticipant;
	}
	/**
	 * @return the totalMailingCost
	 */
	public BigDecimal getTotalMailingCost() {
		return totalMailingCost;
	}
	/**
	 * @param totalMailingCost the totalMailingCost to set
	 */
	public void setTotalMailingCost(BigDecimal totalMailingCost) {
		this.totalMailingCost = totalMailingCost;
	}
	/**
	 * @return the mailingName
	 */
	public String getMailingName() {
		return mailingName;
	}
	/**
	 * @param mailingName the mailingName to set
	 */
	public void setMailingName(String mailingName) {
		this.mailingName = mailingName;
	}
	/**
	 * @return the vipInd
	 */
	public String getVipInd() {
		return vipInd;
	}
	/**
	 * @param vipInd the vipInd to set
	 */
	public void setVipInd(String vipInd) {
		this.vipInd = vipInd;
	}
	/**
	 * @return the orderStapledInd
	 */
	public String getOrderStapledInd() {
		return orderStapledInd;
	}
	/**
	 * @param orderStapledInd the orderStapledInd to set
	 */
	public void setOrderStapledInd(String orderStapledInd) {
		this.orderStapledInd = orderStapledInd;
	}
	/**
	 * @return the largeEnvelopeInd
	 */
	public String getLargeEnvelopeInd() {
		return largeEnvelopeInd;
	}
	/**
	 * @param largeEnvelopeInd the largeEnvelopeInd to set
	 */
	public void setLargeEnvelopeInd(String largeEnvelopeInd) {
		this.largeEnvelopeInd = largeEnvelopeInd;
	}
	/**
	 * @return the bulkOrderInd
	 */
	public String getBulkOrderInd() {
		return bulkOrderInd;
	}
	/**
	 * @param bulkOrderInd the bulkOrderInd to set
	 */
	public void setBulkOrderInd(String bulkOrderInd) {
		this.bulkOrderInd = bulkOrderInd;
	}
	/**
	 * @return the orderSealedInd
	 */
	public String getOrderSealedInd() {
		return orderSealedInd;
	}
	/**
	 * @param orderSealedInd the orderSealedInd to set
	 */
	public void setOrderSealedInd(String orderSealedInd) {
		this.orderSealedInd = orderSealedInd;
	}
	/**
	 * @return the orderStatusCode
	 */
	public String getOrderStatusCode() {
		return orderStatusCode;
	}
	/**
	 * @param orderStatusCode the orderStatusCode to set
	 */
	public void setOrderStatusCode(String orderStatusCode) {
		this.orderStatusCode = orderStatusCode;
	}
	/**
	 * @return the merrilOrderNumber
	 */
	public Integer getMerrilOrderNumber() {
		return merrilOrderNumber;
	}
	/**
	 * @param merrilOrderNumber the merrilOrderNumber to set
	 */
	public void setMerrilOrderNumber(Integer merrilOrderNumber) {
		this.merrilOrderNumber = merrilOrderNumber;
	}
	/**
	 * @return the adressFileOption
	 */
	public String getAdressFileOption() {
		return adressFileOption;
	}
	/**
	 * @param AdressFileOption
	 *            the AdressFileOption to set
	 */
	public void setAdressFileOption(String adressFileOption) {
		this.adressFileOption = adressFileOption;
	}
	
	
}
