package com.manulife.pension.bd.web.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BdBaseController;
import com.manulife.pension.bd.web.registration.util.AddressUtil;
import com.manulife.pension.delegate.BrokerServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.CommonEnvironment;
import com.manulife.pension.service.broker.exception.InvalidIdException;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.broker.valueobject.impl.BrokerDealerFirmImpl;

/**
 * Common utility classes for BDWeb
 * 
 * @author guweigu
 * 
 */
public class BDWebCommonUtils {
	private static final Logger log = Logger.getLogger(BDWebCommonUtils.class);
	

    private static final String SSN_MASK = "XXX-XX";

    private static final String TAX_ID_MASK = "XX-XXX";
    
    private static final String FULL_SSN_MASK = "XXX-XX-XXXX";

    /**
     * Get the request parameter value and convert it as long
     * 
     * @param request
     * @param paramName
     * @return
     */
    public static Long getRequestParameterAsLong(HttpServletRequest request, String paramName)
            throws NumberFormatException {
        String value = StringUtils.trimToNull(request.getParameter(paramName));
        if (value != null) {
            return Long.parseLong(value);
        } else {
            return null;
        }
    }

    /**
     * Get the request parameter value and convert it as long
     * 
     * @param request
     * @param paramName
     * @return
     */
    public static Integer getRequestParameterAsInt(HttpServletRequest request, String paramName)
            throws NumberFormatException {
        String value = StringUtils.trimToNull(request.getParameter(paramName));
        if (value != null) {
            return Integer.parseInt(value);
        } else {
            return null;
        }
    }
    /**
     * This method returns the hyphenated string for ssn or tax id.
     * 
     * @param id
     * @param type
     * @return String a hypenated string
     */
    public static String getHyphenatedString(String id, String type) {
        StringBuffer sb = new StringBuffer(id);
        if (!StringUtils.isEmpty(id)) {
            if (StringUtils.equals(BDConstants.ID_TYPE_TAX, type)) {
                return sb.insert(2, "-").toString();
            } else if (StringUtils.equals(BDConstants.ID_TYPE_SSN, type)) { // SSN
                sb.insert(3, "-");
                sb.insert(6, "-");
                return sb.toString();
            }
        }
        return null;
    }

    /**
     * This method tests whether the given string is SSN or not. This method assumes either a valid
     * SSN or Tax Id will be sent.
     * 
     * @param ssnTaxId
     * @return
     */
    public static boolean isSsn(String ssnTaxId) {
        if (!StringUtils.isEmpty(ssnTaxId)) {
            if (ssnTaxId.indexOf('-') == 3) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method tests whether the given string is TaxId or not. This method assumes either a
     * valid SSN or Tax Id will be sent.
     * 
     * @param ssnTaxId
     * @return boolean
     */
    public static boolean isTaxId(String ssnTaxId) {
        if (!StringUtils.isEmpty(ssnTaxId)) {
            if (ssnTaxId.indexOf('-') == 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method will mask the given SSN or Tax ID. his method assumes either a valid SSN or Tax
     * Id will be sent.
     * 
     * @param ssnTaxId
     * @return String
     */
    public static String maskSsnTaxId(String ssnTaxId) {
        StringBuffer maskedId = new StringBuffer("");
        if (!StringUtils.isEmpty(ssnTaxId)) {
            if (isSsn(ssnTaxId)) {
                maskedId.append(SSN_MASK);
                maskedId.append(StringUtils.substring(ssnTaxId, 6));
            } else {
                maskedId.append(TAX_ID_MASK);
                maskedId.append(StringUtils.substring(ssnTaxId, 6));
            }
        }
        return maskedId.toString();
    }
    
  /**
   * This method will full mask the given SSN . This method assumes either a valid SSN or Tax
   * Id will be sent.
   */
    public static String fullMaskSsn(String ssnTaxId) {
		StringBuffer maskedId = new StringBuffer("");
		if (!StringUtils.isEmpty(ssnTaxId)) {
			if (isSsn(ssnTaxId)) {
				maskedId.append(FULL_SSN_MASK);
			} else {
				maskedId.append(TAX_ID_MASK);
				maskedId.append(StringUtils.substring(ssnTaxId, 6));
			}
		}
		return maskedId.toString();
	}


    /**
     * Check if the request or session contains the validation error
     */
    @SuppressWarnings("unchecked")
    public static boolean hasErrors(final HttpServletRequest request) {
    	
    	Collection errorCollection = null;
        List  errorList =null;
        
        
    	Object obj =  request.getAttribute(BdBaseController.ERROR_KEY);
    	
    	if( obj instanceof Collection){
    		errorCollection = (Collection) obj;
    	} else if (obj instanceof BeanPropertyBindingResult){
			errorList=((BeanPropertyBindingResult) obj).getAllErrors();
		}
    	
    	if ((errorCollection != null && errorCollection.size() != 0) || (errorList != null && errorList.size() != 0)) {
            return true;
        }
    	
        obj =  request.getSession().getAttribute(BdBaseController.ERROR_KEY);
    	if( obj instanceof Collection){
    		errorCollection = (Collection) obj;
    	} else if (obj instanceof BeanPropertyBindingResult){
			errorList=((BeanPropertyBindingResult) obj).getAllErrors();
		}
    	
    	if ((errorCollection != null && errorCollection.size() != 0) || (errorList != null && errorList.size() != 0)) {
            return true;
        }
        return false;
    }

    /**
     * The method formats the telephone number string. If the phone number is invalid, the phone
     * number is returned without any formatting.
     * 
     * @param phoneNumber
     * @return String
     */
    public static String formatPhoneNumber(String phoneNumber) {
        StringBuffer num = new StringBuffer(phoneNumber);
        if (StringUtils.isNotEmpty(phoneNumber) && phoneNumber.length() == 10) {
            num.insert(3, BDConstants.HYPHON_SYMBOL);
            num.insert(7, BDConstants.HYPHON_SYMBOL);
            return num.toString();
        }
        return phoneNumber;
    }

    /**
     * The method formats the zip code. If the zip code contains 9 digits, a hyphen is inserted at
     * the 5 position. Otherwise the zip code is returned without any formatting.
     * 
     * @param zipCode
     * @param country
     * @return String
     */
    public static String formatZipCode(String zipCode, String country) {
        StringBuffer zip = new StringBuffer(zipCode);
        if (StringUtils.isNotEmpty(zipCode) && StringUtils.isNumeric(zipCode)
                && zipCode.length() == 9 && StringUtils.equals(BDConstants.COUNTRY_USA, country)) {
            zip.insert(5, BDConstants.HYPHON_SYMBOL);
            return zip.toString();
        }
        return zipCode;
    }
    
    /**
     * Returns the country name by the code
     * @param countryCode
     * @return
     */
    public static String getCountryName(String countryCode) {
    	List<LabelValueBean> lists = AddressUtil.getInstance().getCountriesList();
    	for (LabelValueBean country: lists) {
    		if (StringUtils.equals(country.getValue(), countryCode)) {
    			return country.getLabel();
    		}
    	}
    	return "";
    }
    
	/**
	 * Parse the firm id list string in format of id1,id2,id3... and retrieve
	 * the BrokerDealerFirm objects
	 * 
	 * @param firmListStr
	 * @return
	 * @throws SystemException
	 */
	public static  List<BrokerDealerFirm> getFirmList(String firmListStr)
			throws SystemException {
		String[] idStrList = StringUtils.split(firmListStr, ',');
		List<BrokerDealerFirm> firms = new ArrayList<BrokerDealerFirm>(
				idStrList.length);
		for (String idStr : idStrList) {
			try {
				firms.add(BrokerServiceDelegate.getInstance(
						BDConstants.BD_APPLICATION_ID).getBrokerDealerFirmById(
						Long.parseLong(idStr)));
			} catch (InvalidIdException e) {
				log.error("Fail to get the BrokerDealer firm by firm id:"
						+ idStr, e);
				throw new SystemException(e,
						"Fail to get the BrokerDealer firm by firm id:" + idStr);
			}
		}
		return firms;
	}
	
	/**
	 * Parse the RIA firm id list string in format of id1,id2,id3... and retrieve
	 * the BrokerDealerFirm objects
	 * 
	 * @param firmListStr
	 * @return
	 * @throws SystemException
	 */
	public static  List<BrokerDealerFirm> getRIAFirmList(String firmListStr)
			throws SystemException {
		String[] idStrList = StringUtils.split(firmListStr, ',');
		List<BrokerDealerFirm> firms = new ArrayList<BrokerDealerFirm>(
				idStrList.length);
		for (String idStr : idStrList) {
			try {
				firms.add(BrokerServiceDelegate.getInstance(
						BDConstants.BD_APPLICATION_ID).getRIAFirmById(
						Long.parseLong(idStr)));
			} catch (InvalidIdException e) {
				log.error("Fail to get the RIA firm by firm id:"
						+ idStr, e);
				throw new SystemException(e,
						"Fail to get the RIA firm by firm id:" + idStr);
			}
		}
		return firms;
	}
	
	/**
	 * Parse the RIA firm id list string in format of id1,id2,id3... and retrieve
	 * the BrokerDealerFirm objects
	 * 
	 * @param firmListStr
	 * @return
	 * @throws SystemException
	 */
	public static  List<BrokerDealerFirm> getRIAUserFirmList(long userProfileId)
			throws SystemException {
		List<BrokerDealerFirm> riaFirms = new ArrayList<BrokerDealerFirm>();
		try {
			riaFirms = BrokerServiceDelegate
					.getInstance(BDConstants.BD_APPLICATION_ID)
					.getRIAUserFirmList(userProfileId);
		} catch (InvalidIdException e) {
			log.error("Fail to get the RIA firm by userProfileId:"
					+ userProfileId, e);
			throw new SystemException(e,
					"Fail to get the RIA firm by userProfileId:" + userProfileId);
		}
		return riaFirms;
	}
	

	/**
	 * This method id used to get the RIA firms with permission
	 * @param firmListStr
	 * @param firmPermissionsListStr
	 * @return updatedFirms
	 * @throws SystemException
	 */
	public static List<BrokerDealerFirm> getRiaFirmsWithPermission(
			String firmListStr, String firmPermissionsListStr)
			throws SystemException {

		String[] idPermissionStrArr = StringUtils.split(firmPermissionsListStr,',');
		List<String> idPermissionStrList = new ArrayList<String>();
		idPermissionStrList = Arrays.asList(idPermissionStrArr);
		List<BrokerDealerFirm> firms = getRIAFirmList(firmListStr);
		List<BrokerDealerFirm> updatedFirms = new ArrayList<BrokerDealerFirm>();

		for (BrokerDealerFirm riaFirm : firms) {
			BrokerDealerFirmImpl updatedfirm = new BrokerDealerFirmImpl();
			updatedfirm.setId(riaFirm.getId());
			updatedfirm.setBrokerDealerFirmId(riaFirm.getBrokerDealerFirmId());
			updatedfirm.setFirmName(riaFirm.getFirmName());
			updatedfirm.setStatus(riaFirm.getStatus());
			updatedfirm.setType(riaFirm.getType());
			if (!idPermissionStrList.contains(String.valueOf(riaFirm.getId()))) {
				updatedfirm.setFirmPermission(false);
			}
			updatedFirms.add(updatedfirm);
		}

		return updatedFirms;
	}
	
	/**
	 * This method is used to get the RIA email count
	 * @param riaEmailId
	 * @return riaEmailCount
	 * @throws SystemException
	 */
	public static int getRegisteredEmailCount(String riaEmailId) throws SystemException{
		int riaEmailCount = BrokerServiceDelegate.getInstance(BDConstants.BD_APPLICATION_ID).getRegisteredEmailCount(riaEmailId);
		return riaEmailCount;
	}

}
