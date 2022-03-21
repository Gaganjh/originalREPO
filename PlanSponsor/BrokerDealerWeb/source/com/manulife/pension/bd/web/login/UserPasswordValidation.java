package com.manulife.pension.bd.web.login;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import com.manulife.pension.delegate.BDPublicSecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.bd.valueobject.BDLoginValueObject;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.passcode.RequestDetails;

interface UserPasswordValidation {

	BDLoginValueObject execute(String userName, String password, String deviceToken, String devicePrint, HttpServletRequest request, boolean initiatedFromRegistration)
			throws SecurityServiceException, SystemException;

	static UserPasswordValidation INSTANCE =
			new UserPasswordValidation() {

		private static final String STEPUP_PASSCODE_LOOPBACK_OCTET = "127.";

		@Override
		public BDLoginValueObject execute(
				final String userName,
				final String password,
				final String deviceToken,
				final String devicePrint,
				final HttpServletRequest request,
				final boolean initiatedFromRegistration)
						throws SecurityServiceException, SystemException {
			return BDPublicSecurityServiceDelegate.getInstance().login(userName, 
					password, deviceToken, new RequestDetails(request, devicePrint), initiatedFromRegistration);

		}

		/**
		 * Check if the ip address for the request is  local host loop back address.
		 * Only happens if request is coming from the same machine on which server is located.
		 * if so then return the local host address otherwise returns passed ip address.
		 * This is needed for step up authentication, where loop back address will be rejected.
		 * 
		 * @param ipAddress
		 * @return ipAddress of local host address if loop back otherwise passed ip address.
		 * @throws UnknownHostException
		 */
		private String checkLoopbackIpAddress(String ipAddress)
				throws UnknownHostException {
			if (ipAddress.startsWith(STEPUP_PASSCODE_LOOPBACK_OCTET)) {
				InetAddress inetAddress = InetAddress.getLocalHost();
				return inetAddress.getHostAddress();
			} else {
				return ipAddress;
			}
		}

	};

}
