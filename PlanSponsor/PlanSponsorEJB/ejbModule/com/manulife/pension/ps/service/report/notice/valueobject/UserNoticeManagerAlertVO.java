/**
 * 
 */
package com.manulife.pension.ps.service.report.notice.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;

import com.manulife.util.render.RenderConstants;

/**
 * @author krishta
 *
 */
public class UserNoticeManagerAlertVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 private Integer alertId;
	 private Integer contractId;
	 private BigDecimal profileId;
	 private String alertName;
	 private String alertUrgencyName;
	 private Date startDate;
	 private String stringStartDate;
	 private String alertFrequenceCode;
	 private String alertTimingCode;
	 
	 private static final String DATE_FORMAT = RenderConstants.MEDIUM_MDY_SLASHED;
		public static FastDateFormat dateFormat = FastDateFormat.getInstance(DATE_FORMAT);
	/**
	 * @return the alertId
	 */
	public Integer getAlertId() {
		return alertId;
	}
	/**
	 * @param alertId the alertId to set
	 */
	public void setAlertId(Integer alertId) {
		this.alertId = alertId;
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
	 * @return the alertName
	 */
	public String getAlertName() {
		return alertName;
	}
	/**
	 * @param alertName the alertName to set
	 */
	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}
	/**
	 * @return the alertUrgencyName
	 */
	public String getAlertUrgencyName() {
		return alertUrgencyName;
	}
	/**
	 * @param alertUrgencyName the alertUrgencyName to set
	 */
	public void setAlertUrgencyName(String alertUrgencyName) {
		this.alertUrgencyName = alertUrgencyName;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the alertFrequenceCode
	 */
	public String getAlertFrequenceCode() {
		return alertFrequenceCode;
	}
	/**
	 * @param alertFrequenceCode the alertFrequenceCode to set
	 */
	public void setAlertFrequenceCode(String alertFrequenceCode) {
		this.alertFrequenceCode = alertFrequenceCode;
	}
	/**
	 * @return the alertTimingCode
	 */
	public String getAlertTimingCode() {
		return alertTimingCode;
	}
	/**
	 * @param alertTimingCode the alertTimingCode to set
	 */
	public void setAlertTimingCode(String alertTimingCode) {
		this.alertTimingCode = alertTimingCode;
	}
	/**
	 * @return the stringStartDate
	 */
	public String getStringStartDate() {
		
		// if DB value is not null
				if(startDate != null){
					//and stringStartDate is accessing first time then return the DB value
					if (stringStartDate == null){
						return dateFormat.format(startDate);
					}
				}
		return stringStartDate;
	}
	/**
	 * @param stringStartDate the stringStartDate to set
	 */
	public void setStringStartDate(String stringStartDate) {
		this.stringStartDate = stringStartDate;
	}
	

}
