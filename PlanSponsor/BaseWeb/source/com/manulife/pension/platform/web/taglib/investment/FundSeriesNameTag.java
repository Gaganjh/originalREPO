/*
 * Created on May 23, 2006
 * LS , multiple fund classes phase1
 * Translates fund class/fund series code into fund class name
 * parameters property - (e.g userprofile.currentContract)
 * location (USA or NY
 * productId and fundSeries ( alternative for property)
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.platform.web.taglib.investment;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.investment.FundSeriesNameHelper;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.web.taglib.PropertyHelper;


/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FundSeriesNameTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private String fundSeries;
	private String location;
	private String productId;
	private String property;
	
	public int doStartTag()throws JspException {
	 try{
	 	if (property !=null && (!"".equals(property)))
	 	{
	 	  Object bean =PropertyHelper.getBean(pageContext, property, 0);	
	 	  Contract currentContract = (Contract) bean; 	  
	 	  setFundSeries(currentContract.getFundPackageSeriesCode());
	 	  productId = currentContract.getProductId();
	 	}
	 	  String fundSeriesName = FundSeriesNameHelper.getFundSeriesName(productId, location, fundSeries.trim());
	 	  pageContext.getOut().print(fundSeriesName);	
      } catch (IOException ex){
    	SystemException se = new SystemException(ex,"com.manulife.pension.ps.web.taglib.investment.FundSeriesDescriptionTag", "doStartTag", "Exception when displaying fund series code: " + ex.toString() );
	    LogUtility.log(se);
	    throw new JspException(se.getMessage());
      }catch(Exception e){
    	SystemException se = new SystemException(e,"com.manulife.pension.ps.web.taglib.investment.FundSeriesDescriptionTag", "doStartTag", "Exception when displaying fund series code: " + e.toString() );
      	throw new JspException(se.getMessage());
      }
    return SKIP_BODY;
   }



	/**
	 * @param location The location to set.
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return Returns the location.
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param productId The productId to set.
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return Returns the productId.
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param property The property to set.
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return Returns the property.
	 */
	public String getProperty() {
		return property;
	}



	/**
	 * @param fundSeries The fundSeries to set.
	 */
	public void setFundSeries(String fundSeries) {
		this.fundSeries = fundSeries;
	}



	/**
	 * @return Returns the fundSeries.
	 */
	public String getFundSeries() {
		return fundSeries;
	}
}
