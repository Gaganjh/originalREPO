package com.manulife.pension.ps.web.taglib.resources;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.log.LogUtility;
import com.manulife.util.web.taglib.PropertyHelper;

public class QuarterlyInvestmentGuideURLTag extends TagSupport{

	private String property;
	private String defaultClass;
	private String location;
	boolean nml;
	public int doStartTag()throws JspException {
	 try{
		 	if (getProperty() !=null && (!"".equals(getProperty())))
		 	{
		 	  Object bean =PropertyHelper.getBean(pageContext, getProperty(), 0);	
		 	  Contract currentContract = (Contract) bean;
		 	  defaultClass =currentContract.getDefaultClass();
		 	  nml = currentContract.isNml();
		 	}
		 String qigUrl =QuarterlyInvestmentGuideURLHelper.getQuarterlyInvestmentGuideURL(defaultClass, location, nml);
		 String url ="<a href=\"#\" onClick=\"PDFWindow('" + qigUrl + "')\" onMouseOver= \"self.status='Go to the PDF'; return true\">";
		 pageContext.getOut().print(url);
	
      } catch (IOException ex){
    	SystemException se = new SystemException(ex,"com.manulife.pension.ps.web.taglib.investment.FundClassDescriptionTag", "doStartTag", "Exception when displaying Quarterly Investment Guide: " + ex.toString() );
	    LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
	    throw new JspException(se.getMessage());
      }catch(Exception e){
    	SystemException se = new SystemException(e,"com.manulife.pension.ps.web.taglib.investment.FundClassDescriptionTag", "doStartTag", "Exception when displaying Quarterly Investment Guide: " + e.toString() );
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



	public void setProperty(String property) {
		this.property = property;
	}



	public String getProperty() {
		return property;
	}

}
