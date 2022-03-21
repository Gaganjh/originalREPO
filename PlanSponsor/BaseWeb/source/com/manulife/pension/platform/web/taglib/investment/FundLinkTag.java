package com.manulife.pension.platform.web.taglib.investment;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.fund.delegate.FundServiceDelegate;
import com.manulife.util.web.taglib.PropertyHelper;

public class FundLinkTag extends BodyTagSupport {

	private String fundIdProperty = null;;

	private String fundTypeProperty = null;

	private String productId =null;
	private String fundSeries; // LS. multiclass phase 2
	private String rateType =null;
	private String siteLocation = null;
	
	private String fundClass = null;

	private int scope;

	public void setScope(int scope) {
		this.scope = scope;

	}

	public int doEndTag() throws JspException {
        String fundTypeString = "";
        String fundIdString = "";
        String rateType = "";
        Object result = null;
        
	    try {
            if(getFundTypeProperty() != null) { 
                result = PropertyHelper.getBean(pageContext,
                        getFundTypeProperty().trim(), scope);
                if(result != null) {                    
                    fundTypeString = result.toString();
                }
            }
            if(getFundIdProperty() != null) {
                result = PropertyHelper.getBean(pageContext,
                        getFundIdProperty().trim(), scope);
                if(result != null) {
                    fundIdString = result.toString();
                }
            }
            if(getRateType() != null) {
                result = PropertyHelper.getBean(pageContext,
                        getRateType().trim(), scope);
                if(result != null) {
                    rateType = result.toString();
                }
            }
            
            String fundSheetURL = "";
			if (fundClass == null || StringUtils.isEmpty(fundClass)) {

				fundSheetURL = FundServiceDelegate.getInstance()
						.getFundSheetURL(productId, fundSeries, fundTypeString,
								fundIdString, rateType, siteLocation);

			} else {

				fundSheetURL = FundServiceDelegate.getInstance()
						.getFundSheetURL(fundTypeString, fundIdString,
								fundClass, siteLocation);

			}
            
            JspWriter out = pageContext.getOut();
            out.print(fundSheetURL);
        } catch (InvocationTargetException e) {
            throw new JspException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new JspException(e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new JspException(e.getMessage());
        } catch (Exception e) {
            throw new JspException(e.getMessage());
        }

		return EVAL_PAGE;
	}

	/**
	 * Gets the fundIdProperty
	 * 
	 * @return Returns a String
	 */
	public String getFundIdProperty() {
		return fundIdProperty;
	}

	/**
	 * Sets the fundIdProperty
	 * 
	 * @param fundIdProperty
	 *            The fundIdProperty to set
	 */
	public void setFundIdProperty(String fundIdProperty) {
		this.fundIdProperty = fundIdProperty;
	}

	/**
	 * Gets the FundTypeProperty
	 * 
	 * @return Returns a String
	 */
	public String getFundTypeProperty() {
		return fundTypeProperty;
	}

	/**
	 * Sets the FundTypeProperty
	 * 
	 * @param FundTypeProperty
	 *            The FundTypeProperty to set
	 */
	public void setFundTypeProperty(String fundTypeProperty) {
		this.fundTypeProperty = fundTypeProperty;
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

	public void setRateType(String rateType) {
		this.rateType = rateType;
	}

	public String getRateType() {
		return rateType;
	}

	public void setFundSeries(String fundSeries) {
		this.fundSeries = fundSeries;
	}

	public String getFundSeries() {
		return fundSeries;
	}

    public String getSiteLocation() {
        return siteLocation;
    }

    public void setSiteLocation(String siteLocation) {
        this.siteLocation = siteLocation;
    }
    
    public String getFundClass() {
        return fundClass;
    }

    public void setFundClass(String fundClass) {
        this.fundClass = fundClass;
    }

}