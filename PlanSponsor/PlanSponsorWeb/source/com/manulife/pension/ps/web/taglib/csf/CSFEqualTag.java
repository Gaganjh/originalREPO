package com.manulife.pension.ps.web.taglib.csf;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;

/**
 * @author Aron Rogers
 */
public class CSFEqualTag extends TagSupport {

    private static final long serialVersionUID = -1L;

    private static final Logger logger = Logger.getLogger(CSFEqualTag.class);
    
    private static final String ANY_CONTRACT = "ANY";

    private String contractId;
    private int	contractNumber = 0;
    
    private String csfCode;

    private String csfAttrCode;

    private String value;
    
 

    /**
     * Constructor.
     */
    public CSFEqualTag() {
        super();
    }

    /**
     * @return Returns the contractId.
     */
    public String getContractId() {
        return contractId;
    }

    /**
     * @param id Sets the contract Id.
     */
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    /**
     * @return Returns the csf code.
     */
    public String getCsfCode() {
        return csfCode;
    }

    /**
     * @param id Sets the csf code.
     */
    public void setCsfCode(String csfCode) {
        this.csfCode = csfCode;
    }

    /**
     * @return Returns the csf attribute code.
     */
    public String getCsfAttrCode() {
        return csfAttrCode;
    }

    /**
     * @param id Sets the csf attribute code.
     */
    public void setCsfAttrCode(String csfAttrCode) {
        this.csfAttrCode = csfAttrCode;
    }

    /**
     * @param id Gets the csf value.
     */
    public String getValue() {
        return value;
    }

    /**
     * @param id Sets the csf value.
     */
    public void setValue(String value) {
        this.value = value;
    }

    public int doStartTag() throws JspTagException, JspException {
        boolean csfEqual = condition();

        return csfEqual ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }

	/**
	 * @return
	 */
	protected boolean condition() {
        boolean csfEqual = false;
        UserProfile userProfile = (UserProfile) SessionHelper.getUserProfile((HttpServletRequest) pageContext.getRequest());
        
		ContractServiceDelegate csfDelegate = ContractServiceDelegate.getInstance();
        try {
    		// Special case, check ALL contracts that users has access to
			if (getContractId() != null && getContractId().toUpperCase().equals(ANY_CONTRACT)) {
            	BigDecimal profileId = new BigDecimal( userProfile.getPrincipal().getProfileId() );
	            if (csfAttrCode == null) {
	            	csfEqual = csfDelegate.hasContractWithContractServiceFeature(profileId, csfCode, getValue() );
	            } else {
	            	csfEqual = csfDelegate.hasContractWithContractServiceFeature(profileId, csfCode, csfAttrCode, getValue() );
	            }
			} else {
				if (getContractNumber() == 0) {
		            if (userProfile != null) {
		                contractNumber = userProfile.getCurrentContract().getContractNumber();
		            }
		        }

	            ContractServiceFeature csf = csfDelegate.getContractServiceFeature(contractNumber, csfCode);
	            if (csfAttrCode == null) {
	                csfEqual = value.equals(csf.getValue());
	            } else {
	                csfEqual = value.equals(csf.getAttributeValue(csfAttrCode));
	            }
			}
        } catch (Exception e) {
            logger.error("Exception evaluating CSF " + csfCode + ", attribute " + csfAttrCode
                    + " against value " + value + ":\n" + e);
        }
		return csfEqual;
	}

	public int doEndTag() throws JspTagException {
        return EVAL_PAGE;
    }
	
	
    private int getContractNumber() {
    	try {
    		contractNumber = Integer.parseInt(getContractId());
    	} catch (NumberFormatException e) {
    		logger.error(e);
    	}
		return contractNumber;
	}

}
