
package com.manulife.pension.ps.web.noticemanager;

import com.manulife.pension.platform.web.controller.AutoForm;


/**
 * Form to update the notice manager status update from merrill
 * @author krishta
 *
 */
public class NoticeManagerOrderStatusUpdateForm extends AutoForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 private String status;
	 private String trackingNumber;
	 private String orderNumber;
	 private String colorInd;
	 private String pageCount;
	 private String participantCount;
	 private String VIP;
	 private String mailDate;
	 private String totalCost;
	 private String  orderStapledInd;
	 private String  largeEnvelopeInd;
	 private String  bulkOrderInd;
	 private String  orderSealedInd;
	 private String  md5Hash;
	 
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the trackingNumber
	 */
	public String getTrackingNumber() {
		return trackingNumber;
	}
	/**
	 * @param trackingNumber the trackingNumber to set
	 */
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}
	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	/**
	 * @return the colorInd
	 */
	public String getColorInd() {
		return colorInd;
	}
	/**
	 * @param colorInd the colorInd to set
	 */
	public void setColorInd(String colorInd) {
		this.colorInd = colorInd;
	}
	/**
	 * @return the pageCount
	 */
	public String getPageCount() {
		return pageCount;
	}
	/**
	 * @param pageCount the pageCount to set
	 */
	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}
	/**
	 * @return the participantCount
	 */
	public String getParticipantCount() {
		return participantCount;
	}
	/**
	 * @param participantCount the participantCount to set
	 */
	public void setParticipantCount(String participantCount) {
		this.participantCount = participantCount;
	}
	
	/**
	 * @return the mailDate
	 */
	public String getMailDate() {
		return mailDate;
	}
	/**
	 * @param mailDate the mailDate to set
	 */
	public void setMailDate(String mailDate) {
		this.mailDate = mailDate;
	}
	/**
	 * @return the totalCost
	 */
	public String getTotalCost() {
		return totalCost;
	}
	/**
	 * @param totalCost the totalCost to set
	 */
	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
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
	 * @return the vIP
	 */
	public String getVIP() {
		return VIP;
	}
	/**
	 * @param vIP the vIP to set
	 */
	public void setVIP(String vip) {
		this.VIP = vip;
	}
	/**
	 * @return the md5Hash
	 */
	public String getMd5Hash() {
		return md5Hash;
	}
	/**
	 * @param md5Hash the md5Hash to set
	 */
	public void setMd5Hash(String md5Hash) {
		this.md5Hash = md5Hash;
	}
	 
	 
}
