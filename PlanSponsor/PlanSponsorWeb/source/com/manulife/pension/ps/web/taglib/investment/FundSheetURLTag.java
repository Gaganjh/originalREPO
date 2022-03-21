package com.manulife.pension.ps.web.taglib.investment;


import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.taglib.BaseTagHelper;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.pension.util.log.LogUtility;

/**
 * This class is the tag that will build the fund sheet URL.
 *
 * @author   Vivek Lingesan
 **/

public class FundSheetURLTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	private boolean printFriendly = false;
	private String productId;
	private String fundId;
	private String fundType;
	private String rateType;
	private String fundSeries;
	private String fundName;
	private boolean showLink = false;
	
	/**
	 * doStartTag is called by the JSP container when the tag is encountered
	 */
	public int doStartTag()throws JspException {

		try {

			JspWriter out = pageContext.getOut();
			
			if (BaseTagHelper.isPrintFriendly(pageContext.getRequest())) {
					setPrintFriendly(true);
			}
			
			if (!isPrintFriendly() && isShowLink()) { 
			out.println("<a href='#fundsheet'; return true' name='"
                    + fundId
                    + "' onClick='FundWindow(\""
                    + FundServiceDelegate.getInstance().getFundSheetURL(productId, fundSeries, fundType, fundId, rateType, 
                    		Environment.getInstance().getSiteLocation())
                            + "\")'>" + fundName + "</a>");
			} else {
				out.println(fundName);
			}

		} catch (IOException ex){

			SystemException se = new SystemException(ex,
					"Problem occurred during FundURLGenerator doStartTag method call. "
					+ ex.toString()); 
			LogUtility.logSystemException(CommonConstants.PS_APPLICATION_ID,se);
			throw new JspException(se.getMessage());
		} catch (SystemException ex){

			SystemException se = new SystemException(ex,
					"Problem occurred while retrieving the fund sheet url getFundSheetURL method call. "
					+ ex.toString()); 
			LogUtility.logSystemException(CommonConstants.PS_APPLICATION_ID,se);
			throw new JspException(se.getMessage());
		}
		return SKIP_BODY;
	}

	/**
	 * @return the printFriendly
	 */
	public boolean isPrintFriendly() {
		return printFriendly;
	}

	/**
	 * @param printFriendly the printFriendly to set
	 */
	public void setPrintFriendly(boolean printFriendly) {
		this.printFriendly = printFriendly;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the fundId
	 */
	public String getFundId() {
		return fundId;
	}

	/**
	 * @param fundId the fundId to set
	 */
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}

	/**
	 * @return the fundType
	 */
	public String getFundType() {
		return fundType;
	}

	/**
	 * @param fundType the fundType to set
	 */
	public void setFundType(String fundType) {
		this.fundType = fundType;
	}

	/**
	 * @return the rateType
	 */
	public String getRateType() {
		return rateType;
	}

	/**
	 * @param rateType the rateType to set
	 */
	public void setRateType(String rateType) {
		this.rateType = rateType;
	}

	/**
	 * @return the fundSeries
	 */
	public String getFundSeries() {
		return fundSeries;
	}

	/**
	 * @param fundSeries the fundSeries to set
	 */
	public void setFundSeries(String fundSeries) {
		this.fundSeries = fundSeries;
	}

	/**
	 * @return the fundName
	 */
	public String getFundName() {
		return fundName;
	}

	/**
	 * @param fundName the fundName to set
	 */
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	/**
	 * @return the showLink
	 */
	public boolean isShowLink() {
		return showLink;
	}

	/**
	 * @param showLink the showLink to set
	 */
	public void setShowLink(boolean showLink) {
		this.showLink = showLink;
	}

}

