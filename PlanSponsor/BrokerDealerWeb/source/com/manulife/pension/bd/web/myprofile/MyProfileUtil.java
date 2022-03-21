package com.manulife.pension.bd.web.myprofile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.ApplicationHelper;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.registration.util.AddressVO;
import com.manulife.pension.bd.web.registration.util.PhoneNumber;
import com.manulife.pension.bd.web.userprofile.BDBrokerUserProfile;
import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.delegate.BDUserSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.bd.exception.BDResendActivationEmailException;
import com.manulife.pension.service.security.bd.exception.BDUserPartyNotExistException;
import com.manulife.pension.service.security.bd.valueobject.BDUserAddressValueObject;
import com.manulife.pension.service.security.bd.valueobject.BrokerAssistantUserInfo;
import com.manulife.pension.service.security.exception.UserNotFoundException;

public class MyProfileUtil {
	private static final String MyProfileAttributeName = "security.myprofile";

	public static final String AssistantList = "assistantList";

	private static final String USA_COUNTRY_CODE = "USA";

	public static Map<String, Integer> MyProfileSecurityServiceExceptionMapping = new HashMap<String, Integer>(
			11);
	static {
		MyProfileSecurityServiceExceptionMapping.put(
				UserNotFoundException.class.getName(),
				BDErrorCodes.MY_PROFILE_CONFLICT_ERROR);
		MyProfileSecurityServiceExceptionMapping.put(
				BDUserPartyNotExistException.class.getName(),
				BDErrorCodes.MY_PROFILE_CONFLICT_ERROR);
		MyProfileSecurityServiceExceptionMapping.put(
				BDResendActivationEmailException.class.getName(),
				BDErrorCodes.MY_PROFILE_CONFLICT_ERROR);		
	}
	/**
	 * Get or create a new MyProfile context
	 * 
	 * @param servletContext
	 * @param request
	 *            Use ServletRequest because WAS tag file uses ServletRequest
	 *            instead of HttpServletRequest
	 * @return
	 * @throws SystemException
	 */
	public static MyProfileContext getContext(ServletContext servletContext,
			ServletRequest servletRequest) throws SystemException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		MyProfileContext context = (MyProfileContext) request.getSession()
				.getAttribute(MyProfileAttributeName);
		if (context == null) {
			context = new MyProfileContext(ApplicationHelper
					.getSecurityManager(servletContext), BDSessionHelper
					.getUserProfile(request));
			request.getSession().setAttribute(MyProfileAttributeName, context);
		}
		return context;
	}

	/**
	 * Clear the context for My Profile
	 * @param servletRequest
	 */
	public static void clearContext(ServletRequest servletRequest) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		request.getSession().removeAttribute(MyProfileAttributeName);
	}

	public static List<BrokerAssistantUserInfo> updateAssistantList(
			HttpServletRequest request) throws SystemException {
		BDUserProfile userProfile = BDSessionHelper.getUserProfile(request);
		List<BrokerAssistantUserInfo> assistants = BDUserSecurityServiceDelegate
				.getInstance()
				.getBrokerAssistants(userProfile.getBDPrincipal());
		request.setAttribute(AssistantList, assistants);
		return assistants;
	}

	public static boolean isAllowResendInvitationForAssistant(
			HttpServletRequest request, BrokerAssistantUserInfo assistant) {
		BDBrokerUserProfile brokerUser = (BDBrokerUserProfile) BDSessionHelper
				.getBaseUserProfile(request);
		return !brokerUser.isDisableAssistant(assistant.getUserProfileId());
	}

	/**
	 * This method populates the AddressVO from BDUserAddressValueObject
	 * 
	 * @param address
	 * @return AddressVO
	 */
	public static AddressVO populateAddressVO(BDUserAddressValueObject bdAddress) {
		AddressVO address = new AddressVO();
		if (bdAddress != null) {
			address = new AddressVO();
			address.setAddress1(bdAddress.getAddrLine1());
			address.setAddress2(bdAddress.getAddrLine2());
			address.setCity(bdAddress.getCity());
			address.setCountry(bdAddress.getCountry());
			if (StringUtils.equals(bdAddress.getCountry(), USA_COUNTRY_CODE)) {
				address.setUsState(bdAddress.getState());
				address.setUsZipCode1(StringUtils.substring(bdAddress
						.getZipCode(), 0, 5));
				address.setUsZipCode2(StringUtils.substring(bdAddress
						.getZipCode(), 5, 9));
			} else {
				address.setOtherState(bdAddress.getState());
				address.setOtherZipCode(bdAddress.getZipCode());
			}
		}
		return address;
	}

	/**
	 * This method populates the PhoneNumber object from a phone number string
	 * 
	 * @param telephoneNumber
	 * @return PhoneNumber
	 */
	public static PhoneNumber populatePhoneNumberVO(String telephoneNumber) {
		PhoneNumber phoneNumber = new PhoneNumber();
		if (!StringUtils.isEmpty(telephoneNumber)) {
			phoneNumber.setAreaCode(StringUtils
					.substring(telephoneNumber, 0, 3));
			phoneNumber
					.setNumber1(StringUtils.substring(telephoneNumber, 3, 6));
			phoneNumber.setNumber2(StringUtils
					.substring(telephoneNumber, 6, 10));
		}
		return phoneNumber;
	}
}
