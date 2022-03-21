package com.manulife.pension.ps.service.report.noticereports.valueobject;

import java.io.Serializable;

public class OrderStatusReportVO implements Serializable {

    private static final long serialVersionUID = 411825576264897618L;

    private String orderStatusType;
    
    private int totalOrderStatusCount;

    /**
	 * @return the totalOrderStatusCount
	 */
	public int getTotalOrderStatusCount() {
		return totalOrderStatusCount;
	}

	/**
	 * @param totalOrderStatusCount the totalOrderStatusCount to set
	 */
	public void setTotalOrderStatusCount(int totalOrderStatusCount) {
		this.totalOrderStatusCount = totalOrderStatusCount;
	}

	/**
	 * @return the orderStatusType
	 */
	public String getOrderStatusType() {
		return orderStatusType;
	}

	/**
	 * @param orderStatusType the orderStatusType to set
	 */
	public void setOrderStatusType(String orderStatusType) {
		this.orderStatusType = orderStatusType;
	}

}
