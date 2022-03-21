package com.manulife.pension.ps.web.profiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * A view object contain contract access page elements for edit my profile page
 * 
 * @author Steven Wang
 */
public class MyProfileContractAccess implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    public final static String YES = "Yes";

    public final static String NO = "No";

    public static final String FIELD_CONTRACT_NUMBER = "contractNumber";

    public static final String FIELD_CONTRACT_NAME = "contractName";

    public static final String FIELD_EMAIL_PREFERENCE = "emailPreference";

    private Integer contractNumber;
    
    private List<Integer> tpaFirmIds = new ArrayList<Integer>();
    private boolean mailRecepient;
    private boolean primaryContact;
    private boolean trusteeMailRecepient;
    private boolean oldMailRecepient;
    private boolean oldPrimaryContact;
    private boolean oldTrusteeMailRecepient;
    private boolean trusteeMailRecepientAllowed;

    private boolean specialAttributeAllowed;
    private boolean disableAttributes;

	private String primaryContactFirstName;
	private String primaryContactLastName;
	private String clientMailFirstName;
	private String clientMailLastName;	
	private String trusteeMailLastName;
	private String trusteeMailFirstName;

    /**
     * contract number
     */
    private String contractName;

    // Email preferences
    private String receiveILoanEmail = NO;

    private String oldReceiveILoanEmail = NO;

    private boolean showReceiveILoanEmail;

    private String emailNewsletter = NO;

    private String oldEmailNewsletter = NO;
    
    private boolean showEmailNewsletter;
    
    public String getEmailNewsletter() {
        return emailNewsletter;
    }

    public void setEmailNewsletter(String emailNewsletter) {
        this.emailNewsletter = emailNewsletter;
    }

    public String getOldEmailNewsletter() {
        return oldEmailNewsletter;
    }

    public void setOldEmailNewsletter(String oldEmailNewsletter) {
        this.oldEmailNewsletter = oldEmailNewsletter;
    }

    /**
     * Gets the Email news letter Changed
     * 
     * @return Returns a boolean
     */
    public boolean isEmailNewsLetterChanged() {
        return emailNewsletter != null && !emailNewsletter.equals(oldEmailNewsletter);
    }

    /**
     * Gets the Special Attribute Changed
     * @return Returns a boolean
     */
    public boolean isSpecialAttributeChanged() {
        return (isMailRecepientChanged() || isTrusteeMailRecepientChanged() || isPrimaryContactChanged());
    }
    /**
     * Gets the Primary Contact Changed
     * @return Returns a boolean
     */    
    public boolean isPrimaryContactChanged() {
        return (primaryContact != oldPrimaryContact);
    }
    /**
     * Gets the Mail Recepient Changed
     * @return Returns a boolean
     */
    public boolean isMailRecepientChanged() {
        return (mailRecepient != oldMailRecepient);
    }
    /**
     * Gets the Trustee Mail Recepient Changed
     * @return Returns a boolean
     */
    public boolean isTrusteeMailRecepientChanged() {
        return (trusteeMailRecepient != oldTrusteeMailRecepient);
    }

    /**
     * Clone object for change tracker function and highlight if changed function
     */
    public Object clone() {
        try {
            MyProfileContractAccess clonedObject = (MyProfileContractAccess) super.clone();
            /*
             * Add all direct debit accounts into the clone object.
             */
            return clonedObject;
        } catch (CloneNotSupportedException e) {
            // this should not happen because this object implements Cloneable
            throw new NestableRuntimeException(e);
        }
    }

    /**
     * Get object attributes in a flat map for checking entity is changed
     */
    public Map getFormAsMap(String fieldPrefix) {
        Map formMap = new HashMap();
        String fieldId = fieldPrefix + FIELD_CONTRACT_NUMBER;
        formMap.put(fieldId, getContractNumber());
        fieldId = fieldPrefix + FIELD_CONTRACT_NAME;
        formMap.put(fieldId, getContractName());

        return formMap;
    }

    /**
     * Getcontract name;
     * 
     * @return
     */
    public String getContractName() {
        return contractName;
    }

    /**
     * Set contract name
     * 
     * @param contractName
     */
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Integer getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(Integer contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getOldReceiveILoanEmail() {
        return oldReceiveILoanEmail;
    }

    public void setOldReceiveILoanEmail(String oldReceiveILoanEmail) {
        this.oldReceiveILoanEmail = oldReceiveILoanEmail;
    }

    public String getReceiveILoanEmail() {
        return receiveILoanEmail;
    }

    public void setReceiveILoanEmail(String receiveILoanEmail) {
        this.receiveILoanEmail = receiveILoanEmail;
    }

    /**
     * Gets the reveive iload email changed
     * 
     * @return Returns a boolean
     */
    public boolean isReceiveILoanEmailChanged() {
        return receiveILoanEmail != null && !receiveILoanEmail.equals(oldReceiveILoanEmail);
    }



    public boolean isChanged() {
    	return (isReceiveILoanEmailChanged() || isEmailNewsLetterChanged() || isSpecialAttributeChanged());
    }

    /**
     * @return the showEmailNewsletter
     */
    public boolean isShowEmailNewsletter() {
        return showEmailNewsletter;
    }

    /**
     * @param showEmailNewsletter the showEmailNewsletter to set
     */
    public void setShowEmailNewsletter(boolean showEmailNewsletter) {
        this.showEmailNewsletter = showEmailNewsletter;
    }

    /**
     * @return the showReceiveILoanEmail
     */
    public boolean isShowReceiveILoanEmail() {
        return showReceiveILoanEmail;
    }

    /**
     * @param showReceiveILoanEmail the showReceiveILoanEmail to set
     */
    public void setShowReceiveILoanEmail(boolean showReceiveILoanEmail) {
        this.showReceiveILoanEmail = showReceiveILoanEmail;
    }

    public boolean isShowEmailPreferences() {
    	return isShowEmailNewsletter() || isShowReceiveILoanEmail() || isSpecialAttributeAllowed();
    }

    /**
     * @return the tpaFirmIds
     */
    public List<Integer> getTpaFirmIds() {
        return tpaFirmIds;
    }

    /**
     * @param tpaFirmIds the tpaFirmIds to set
     */
    public void setTpaFirmIds(List<Integer> tpaFirmIds) {
        this.tpaFirmIds = tpaFirmIds;
    }

	/**
	 * @return the mailRecepient
	 */
	public boolean isMailRecepient() {
		return mailRecepient;
	}

	/**
	 * @param mailRecepient the mailRecepient to set
	 */
	public void setMailRecepient(boolean mailRecepient) {
		this.mailRecepient = mailRecepient;
	}

	/**
	 * @return the trusteeMailRecepient
	 */
	public boolean isTrusteeMailRecepient() {
		return trusteeMailRecepient;
	}

	/**
	 * @param trusteeMailRecepient the trusteeMailRecepient to set
	 */
	public void setTrusteeMailRecepient(boolean trusteeMailRecepient) {
		this.trusteeMailRecepient = trusteeMailRecepient;
	}

	/**
	 * @return the primaryContact
	 */
	public boolean isPrimaryContact() {
		return primaryContact;
	}

	/**
	 * @param primaryContact the primaryContact to set
	 */
	public void setPrimaryContact(boolean primaryContact) {
		this.primaryContact = primaryContact;
	}

	/**
	 * @return the oldMailRecepient
	 */
	public boolean isOldMailRecepient() {
		return oldMailRecepient;
	}

	/**
	 * @param oldMailRecepient the oldMailRecepient to set
	 */
	public void setOldMailRecepient(boolean oldMailRecepient) {
		this.oldMailRecepient = oldMailRecepient;
	}

	/**
	 * @return the oldPrimaryContact
	 */
	public boolean isOldPrimaryContact() {
		return oldPrimaryContact;
	}

	/**
	 * @param oldPrimaryContact the oldPrimaryContact to set
	 */
	public void setOldPrimaryContact(boolean oldPrimaryContact) {
		this.oldPrimaryContact = oldPrimaryContact;
	}

	/**
	 * @return the oldTrusteeMailRecepient
	 */
	public boolean isOldTrusteeMailRecepient() {
		return oldTrusteeMailRecepient;
	}

	/**
	 * @param oldTrusteeMailRecepient the oldTrusteeMailRecepient to set
	 */
	public void setOldTrusteeMailRecepient(boolean oldTrusteeMailRecepient) {
		this.oldTrusteeMailRecepient = oldTrusteeMailRecepient;
	}

	/**
	 * @return the specialAttributeAllowed
	 */
	public boolean isSpecialAttributeAllowed() {
		return specialAttributeAllowed;
	}

	/**
	 * @param specialAttributeAllowed the specialAttributeAllowed to set
	 */
	public void setSpecialAttributeAllowed(boolean specialAttributeAllowed) {
		this.specialAttributeAllowed = specialAttributeAllowed;
	}

	/**
	 * @return the trusteeMailRecepientAllowed
	 */
	public boolean isTrusteeMailRecepientAllowed() {
		return trusteeMailRecepientAllowed;
	}

	/**
	 * @param trusteeMailRecepientAllowed the trusteeMailRecepientAllowed to set
	 */
	public void setTrusteeMailRecepientAllowed(boolean trusteeMailRecepientAllowed) {
		this.trusteeMailRecepientAllowed = trusteeMailRecepientAllowed;
	}

	/**
	 * @return the disableAttributes
	 */
	public boolean isDisableAttributes() {
		return disableAttributes;
	}

	/**
	 * @param disableAttributes the disableAttributes to set
	 */
	public void setDisableAttributes(boolean disableAttributes) {
		this.disableAttributes = disableAttributes;
	}

	/**
	 * @return the primaryContactFirstName
	 */
	public String getPrimaryContactFirstName() {
		return primaryContactFirstName;
	}

	/**
	 * @param primaryContactFirstName the primaryContactFirstName to set
	 */
	public void setPrimaryContactFirstName(String primaryContactFirstName) {
		this.primaryContactFirstName = primaryContactFirstName;
	}

	/**
	 * @return the primaryContactLastName
	 */
	public String getPrimaryContactLastName() {
		return primaryContactLastName;
	}

	/**
	 * @param primaryContactLastName the primaryContactLastName to set
	 */
	public void setPrimaryContactLastName(String primaryContactLastName) {
		this.primaryContactLastName = primaryContactLastName;
	}

	/**
	 * @return the clientMailFirstName
	 */
	public String getClientMailFirstName() {
		return clientMailFirstName;
	}

	/**
	 * @param clientMailFirstName the clientMailFirstName to set
	 */
	public void setClientMailFirstName(String clientMailFirstName) {
		this.clientMailFirstName = clientMailFirstName;
	}

	/**
	 * @return the clientMailLastName
	 */
	public String getClientMailLastName() {
		return clientMailLastName;
	}

	/**
	 * @param clientMailLastName the clientMailLastName to set
	 */
	public void setClientMailLastName(String clientMailLastName) {
		this.clientMailLastName = clientMailLastName;
	}

	/**
	 * @return the trusteeMailLastName
	 */
	public String getTrusteeMailLastName() {
		return trusteeMailLastName;
	}

	/**
	 * @param trusteeMailLastName the trusteeMailLastName to set
	 */
	public void setTrusteeMailLastName(String trusteeMailLastName) {
		this.trusteeMailLastName = trusteeMailLastName;
	}

	/**
	 * @return the trusteeMailFirstName
	 */
	public String getTrusteeMailFirstName() {
		return trusteeMailFirstName;
	}

	/**
	 * @param trusteeMailFirstName the trusteeMailFirstName to set
	 */
	public void setTrusteeMailFirstName(String trusteeMailFirstName) {
		this.trusteeMailFirstName = trusteeMailFirstName;
	}

}
