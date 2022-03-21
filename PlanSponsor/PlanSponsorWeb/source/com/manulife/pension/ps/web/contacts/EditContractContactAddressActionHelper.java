package com.manulife.pension.ps.web.contacts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.contacts.util.AddressVO;
import com.manulife.pension.ps.web.controller.PsController;
import com.manulife.pension.ps.web.delegate.LockServiceDelegate;
import com.manulife.pension.ps.web.util.LockHelper;
import com.manulife.pension.service.contract.valueobject.Address;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.util.Pair;
import com.manulife.pension.validator.ValidationError;

/**
 * Helper class for Edit contract address action
 * @author Ranjith Kumar
 *
 */
public class EditContractContactAddressActionHelper {
	
	private static final Logger logger = Logger.getLogger(EditContractContactAddressActionHelper.class);

	/**
	 * To copy legal address to all other addresses
	 * @param legalAddressVO
	 * @param form
	 */
	public static void applyLegalAddressToAll(AddressVO legalAddressVO,
			EditContractContactAddressForm form) {
		
		for (AddressVO address : form.getContactAddresses()) {
			if (!address.getAddressTypeCode().equalsIgnoreCase(
					legalAddressVO.getAddressTypeCode())) {
				address.copyAddressFrom(legalAddressVO);
			}
		}
	}
	
	 /**
     * To validate postbox field in the address
     * @param errors
     * @param addressVO
     */
    public static void validateAddressForPostBox(AddressVO addressVO, Collection errors){
    	
    	if(StringUtils.isNotBlank(addressVO.getAddressLine1())) {
    		validateAddressLine(addressVO.getAddressLine1(), errors);
    	}
    	
    	if(StringUtils.isNotBlank(addressVO.getAddressLine2())) {
    		validateAddressLine(addressVO.getAddressLine2(), errors);
    	}
    }
    
    /**
     * Validates address line
     * @param errors
     * @param addressLine
     * @return
     */
    public static void validateAddressLine(String addressLine, Collection errors){
    	String notAllowedPostBoxExp = "[pP]((.[oO])|([oO].)|(.[oO].)|([oO]))[bB][oO][xX]";			//Defect #4969
    	
    	String addressLineAfterRemovingSpaces = StringUtils.replace(addressLine, Constants.SINGLE_SPACE_SYMBOL, Constants.BLANK);
    	
    	 Pattern p = Pattern.compile(notAllowedPostBoxExp);
		 Matcher m = p.matcher(addressLineAfterRemovingSpaces);
		 if(m.find()) {
			 // Add error for PostBox
			 ValidationError error = new ValidationError("", ErrorCodes.POBOX_NOT_ALLOWED_IN_COURIER);
			 if(!isSameErrorAdded(errors, error)){														//CL 111755
              errors.add(error);
			 }																							//CL 111755
		 }
    }
    
    /**
     * This method will used to avoid adding the duplicate error objects in the error collection.
     * @param errors
     * @param error
     * @return
     * CL 111755
     */
    private static boolean isSameErrorAdded(Collection errors,
			ValidationError error) {
    	int eCode = error.getErrorCode();
		for (Object validationError : errors) {
			int errorCode = ((ValidationError) validationError).getErrorCode();
			if(eCode == errorCode){
				return true;
			}
		}
		return false;
	}

	/**
     * Validates US zip code input value
     * @param errors
     * @param addressVO
     * @param overrideZipCode
     * @param flagRangeCheckAsWarning
     */
    public static void validateUsZipCode(Collection errors, AddressVO addressVO,
			 boolean overrideZipCode,
			boolean flagRangeCheckAsWarning) {
		if (!StringUtils.isBlank(addressVO.getZipCode())) {
			Pattern numericCharacterRegEx = Pattern.compile("[0-9]");
			String invalidCharacters = getNotAllowedCharacter(
					numericCharacterRegEx, addressVO.getZipCode());
			boolean zipCodeInvalidFormat = false;
			if (invalidCharacters.length() > 0) {
				Object[] params = {Address.getAddressTypeForCode(addressVO.getAddressTypeCode())};
				ValidationError error = new ValidationError("", ErrorCodes.ZIP_NOT_VALID_FOR_THIS_STATE, params);
				errors.add(error);
				zipCodeInvalidFormat = true;
			} else {
				if (addressVO.getZipCode().length() != 5 && addressVO.getZipCode().length() != 9) {
					Object[] params = {Address.getAddressTypeForCode(addressVO.getAddressTypeCode())};
					ValidationError error = new ValidationError("", ErrorCodes.ZIP_NOT_VALID_FOR_THIS_STATE, params);
					errors.add(error);
					zipCodeInvalidFormat = true;
				}
			}
			if (!overrideZipCode) {
				if (!zipCodeInvalidFormat && !StringUtils.isBlank(addressVO.getStateCode())) {
					EnvironmentServiceDelegate environmentServiceDelegate = EnvironmentServiceDelegate
							.getInstance(GlobalConstants.PSW_APPLICATION_ID);
					Map<String, List<Pair<Long, Long>>> zipRangesMap = null;
					try {
						zipRangesMap = (Map<String, List<Pair<Long, Long>>>) environmentServiceDelegate
								.getZipCodeRanges();
					} catch (SystemException e) {
						throw new RuntimeException(
								"Unable to retrieve zip code ranges", e);
					}
					List<Pair<Long, Long>> zipRangesList = zipRangesMap
							.get(addressVO.getStateCode());
					if (zipRangesList != null) {
						Long zip = Long.valueOf(addressVO.getZipCode().substring(0, 5));
						boolean validRange = false;
						for (Pair<Long, Long> zipRange : zipRangesList) {
							if (zip.longValue() >= zipRange.getFirst()
									.longValue()
									&& zip.longValue() <= zipRange.getSecond()
											.longValue()) {
								validRange = true;
								break;
							}
						}

						if (!validRange) {
							Object[] params = {Address.getAddressTypeForCode(addressVO.getAddressTypeCode())};
							ValidationError error = new ValidationError("",
									ErrorCodes.ZIP_NOT_VALID_FOR_THIS_STATE, params);
			                errors.add(error);
						}
					}
				}
			}
		}
	}
    
    /**
     * Remove the characters which are not allowed in the allowedCharaterPattern.
     * @param allowedCharacterPattern
     * @param string
     * @return
     */
    public static String getNotAllowedCharacter(
			Pattern allowedCharacterPattern, String string) {
		Matcher m = allowedCharacterPattern.matcher(string);
		String invalidChars = m.replaceAll("");
		return invalidChars;
	}
    
    /**
     * Returns a list of states that belong to US.
     * 
     * @return List a List of LabelValueBean objects with state code as Label and Value since we
     *         have to show the state codes in the screen. But the DAO returns the state codes and
     *         state names.
     */
    @SuppressWarnings("unchecked")
    public static List<LabelValueBean> getUSStates() throws SystemException {
        List<LabelValueBean> tempList = new ArrayList<LabelValueBean>();

        Map<String, String> states = EnvironmentServiceDelegate.getInstance(
            		Constants.PS_APPLICATION_ID).getUSAGeographicalStatesOnly();
            for (Iterator<String> it = states.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                // We have to show the state codes in the screen.
                tempList.add(new LabelValueBean(key.toString(), key.toString()));
            }
            Collections.sort(tempList, new Comparator<LabelValueBean>() {
                public int compare(LabelValueBean o1, LabelValueBean o2) {
                    return o1.getLabel().compareTo(o2.getLabel());
                }
            });
            
        return tempList;
    }
    
    // TODO - to be removed
    /**
     * 
     * @param contractAddresses
     * @param actionForm
     */
    public static void populateAddressesInForm(Collection<Address> contractAddresses, EditContractContactAddressForm form) {
    	List<AddressVO> addressList = new ArrayList<AddressVO>();
    	for(Address address : contractAddresses) {
    		// if(!address.isBlank()) {
    		AddressVO addressVO = new AddressVO();
			addressVO.setAddressLine1(address.getLine1());
			addressVO.setAddressLine2(address.getLine2());
			addressVO.setAddressTypeCode(address.getType().getCode());
			addressVO.setCity(address.getCity());
			addressVO.setStateCode(address.getStateCode());
			addressVO.setZipCode(address.getZipCode());
			addressList.add(addressVO);
    		// }
			if(Address.LEGAL_CODE.equals(addressVO.getAddressTypeCode())) {
				form.setLegalAddressLine1(addressVO.getAddressLine1());
			}
    	}
    	
    	form.setContactAddresses(addressList);
    }
    
    /**
     * Returns the changed addresses
     * @param form
     * @return
     */
    public static List<Address> getUserUpdatedAddresses(EditContractContactAddressForm form) {
    	List<Address> addressList = new ArrayList<Address>();
    	
    	for(AddressVO addressVO : form.getContactAddresses()) {
    		if(StringUtils.isNotBlank(addressVO.getAddressLine1())) {
    			 Address address = new Address(Address.getAddressTypeForCode(addressVO.getAddressTypeCode()));
    			 address.setLine1(addressVO.getAddressLine1());
    			 address.setLine2(addressVO.getAddressLine2());
    			 address.setCity(addressVO.getCity());
    			 address.setStateCode(addressVO.getStateCode());
    			 address.setZipCode(addressVO.getZipCode());
    			 
    			 addressList.add(address);
    		}
    	}
    	
    	return addressList;
    }
    
    /**
     * Attempts to obtain a lock for the specified contact address.
     * 
     * @param planData The contact address that is being locked.
     * @param request The user's request.
     * @return boolean - True if the lock was successfully obtained, false otherwise.
     */
    public static boolean obtainLock(final Contract contract, final HttpServletRequest request)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("obtainLock> Obtaining lock for the contract Address [").append(
                    contract.getContactId()).append("].").toString());
        }
        return LockServiceDelegate.getInstance().lock(LockHelper.CONTACT_ADDRESS_LOCK_NAME,
                LockHelper.CONTACT_ADDRESS_LOCK_NAME + contract.getContactId(),
                PsController.getUserProfile(request).getPrincipal().getProfileId());
    }

    /**
     * Attempts to release any lock held for the specified plan.
     * 
     * @param contract The contract contact address that is being locked.
     * @param request The user's request.
     */
    public static void releaseLock(final Contract contract, final HttpServletRequest request)
            throws SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer("releaseLock> Releasing lock for contact address [").append(
                    contract.getContactId()).append("].").toString());
        }
        LockServiceDelegate.getInstance().releaseLock(LockHelper.CONTACT_ADDRESS_LOCK_NAME,
                LockHelper.CONTACT_ADDRESS_LOCK_NAME + contract.getContactId());
    }
    
    /**
	 * Validates the Address
	 * @return
	 */
	public static boolean isValidAddress(AddressVO addressVO) {
		boolean validAddress = false;
		
		if((StringUtils.isNotBlank(addressVO.getAddressLine1()) && StringUtils.isNotBlank(addressVO.getCity()) &&
				 StringUtils.isNotBlank(addressVO.getStateCode()) && StringUtils.isNotBlank(addressVO.getZipCode())) || 	// All fields are blank  
				 (StringUtils.isBlank(addressVO.getAddressLine1()) && StringUtils.isBlank(addressVO.getCity()) &&
   					 StringUtils.isBlank(addressVO.getStateCode()) && StringUtils.isBlank(addressVO.getZipCode())) || // All fields are not blank 
				 (StringUtils.isNotBlank(addressVO.getAddressLine1()) && 
						 StringUtils.isBlank(addressVO.getCity()) && 
						 StringUtils.isBlank(addressVO.getStateCode()) && 
						 StringUtils.isBlank(addressVO.getZipCode()))) { 
			validAddress = true;
		} else {
			validAddress = false;
		}
		
		return validAddress;
	}
	
	/**
     * Checks if contains Ascii Printable characters
     * @return
     */
    public static boolean isAsciiPrintable(AddressVO addressVO) {
        boolean isAsciiPrintable = false;
        
        if(checkAsciiPrintable(addressVO.getAddressLine1()) &&  checkAsciiPrintable(addressVO.getAddressLine2()) &&
            checkAsciiPrintable(addressVO.getCity()) && checkAsciiPrintable(addressVO.getStateCode()) && checkAsciiPrintable(addressVO.getZipCode())) {
                isAsciiPrintable = true;
            }
            else{
                isAsciiPrintable = false;
            }
        return isAsciiPrintable;
    }
    
    /**
     * Checks if contains Ascii Printable characters
     * @return
     */
    public static boolean checkAsciiPrintable(String addressString) {
        boolean isAsciiPrintable = false;
        
        if(StringUtils.isNotBlank(addressString)){//only if not blank - we should check for ascii printable or decimal 63 (?)
            if( StringUtils.isAsciiPrintable(addressString) && !StringUtils.contains(addressString,"?")){
                isAsciiPrintable = true;
                return isAsciiPrintable;
            }
            else{
                isAsciiPrintable = false;
                return isAsciiPrintable;
            }
        } else {
            isAsciiPrintable = true;//for empty, whitespace and null strings - it should return true
            return isAsciiPrintable;
        }
    }
}
