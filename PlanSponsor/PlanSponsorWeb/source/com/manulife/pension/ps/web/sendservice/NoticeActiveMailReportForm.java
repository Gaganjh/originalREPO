/**
 * 
 */
package com.manulife.pension.ps.web.sendservice;

import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.service.request.valueobject.NoticeRequest;


/**
 * @author krishta
 *
 */
public class NoticeActiveMailReportForm extends BaseReportForm {
	
	private List<NoticeRequest> noticeRequestList =  new ArrayList<NoticeRequest>();

	private Integer selectedTrackingNumber;
	private String sourceStatus;
	/**
	 * @return the noticeRequestList
	 */
	public List<NoticeRequest> getNoticeRequestList() {
		return noticeRequestList;
	}

	/**
	 * @param noticeRequestList the noticeRequestList to set
	 */
	public void setNoticeRequestList(List<NoticeRequest> noticeRequestList) {
		this.noticeRequestList = noticeRequestList;
	}
	
	/**
	 * @return the noticeRequestList
	 */
	public NoticeRequest getNoticeRequest(Integer trackingNumber) {
		for(NoticeRequest noticeRequest:noticeRequestList){
			if(trackingNumber.equals(noticeRequest.getOrderNo())){
				return noticeRequest;
			}
		}
		return null;
	}

	/**
	 * @return the selectedTrackingNumber
	 */
	public Integer getSelectedTrackingNumber() {
		return selectedTrackingNumber;
	}

	/**
	 * @param selectedTrackingNumber the selectedTrackingNumber to set
	 */
	public void setSelectedTrackingNumber(Integer selectedTrackingNumber) {
		this.selectedTrackingNumber = selectedTrackingNumber;
	}

	/**
	 * @return the sourceStatus
	 */
	public String getSourceStatus() {
		return sourceStatus;
	}

	/**
	 * @param sourceStatus the sourceStatus to set
	 */
	public void setSourceStatus(String sourceStatus) {
		this.sourceStatus = sourceStatus;
	}
	
	
	

}
