package com.manulife.pension.platform.web.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonErrorCodes;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.validator.ValidationError;

public class PasswordMeterUtility {
	protected static Logger logger = Logger.getLogger(PasswordMeterUtility.class);
	
	 public static String validatePasswordMeter( HttpServletRequest request, HttpServletResponse response,String userName,boolean isFRWExternalUser) throws SecurityServiceException, SystemException, IOException
	 {
		 final String jsonObj = IOUtils.toString(request.getInputStream());
			String jsonText = "[" + jsonObj + "]";
			JsonParser parser = new JsonParser();
			JsonArray jsonArr = (JsonArray) parser.parse(jsonText);
			String newPassword  = null;
			if (StringUtils.isNotEmpty(jsonObj)) {
				for (int i = 0; i < jsonArr.size(); i++) {

					JsonObject obj = (JsonObject) jsonArr.get(i);

				newPassword = (obj.get("newPassword")).getAsString();
				}
			}
			String password  = newPassword;
			SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
			String errors = service.passwordStrengthValidation(password,userName,isFRWExternalUser);
			String responseText = errors;
			response.setContentLength(responseText.length());
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(responseText);
			out.flush();
			
		 return null;
	 }
	 
	public static Collection<ValidationError> validateSeqConsCharForExUsers(String userName, String password,
			Collection<ValidationError> errors) {
		try {
			Pattern passRegex = Pattern.compile(".*([0-9A-Za-z])\\1{3,}.*");
			SecurityServiceDelegate service = SecurityServiceDelegate.getInstance();
			if (passRegex.matcher(password.toLowerCase()).matches() || validateConsecutiveSeq(password)
					|| checkUsernamePasswordSequence(userName, password) || service.isContextSpecificWords(password)) {
				errors.add(new ValidationError("", CommonErrorCodes.FRW_EXTERNAL_USER_PASSWORD_FAILS_STANDARDS));
			}
		} catch (SystemException | RemoteException exception) {
			if (logger.isDebugEnabled()) {
				logger.debug("exception occured while calling "
                		+ "validateSeqConsCharForExUsers service call" +exception.getMessage());
			}
		}
		return errors;
	}
	
	/*
	 * This method is used to validate the four or more consecutive sequence character is present in the password
	 * Example:
	 * 	2345 - false
	 *  abc  - true
	 *  cdef - false
	 */
	private static Boolean validateConsecutiveSeq(String pwd) {
		char[] passwordArray = pwd.toLowerCase().toCharArray();
	    int asciiCode;
	    boolean isConSeq = false;
	    int previousAsciiCode = 0;
	    int numSeqcount = 0;
	    final int contSeqNum = 3;

	    for (int i = 0; i < passwordArray.length; i++) {
	        asciiCode = passwordArray[i];
	        if ((previousAsciiCode + 1) == asciiCode) {
	            numSeqcount++;
	            if (numSeqcount >= contSeqNum) {
	                isConSeq = true;
	                break;
	            }
	        } else {
	            numSeqcount = 0;
	        }
	        previousAsciiCode = asciiCode;
	    }
	    return isConSeq;
	}

	private static Boolean checkUsernamePasswordSequence(String uname, String pwd) {
		Boolean contains = false;

		for (String seq : uname.split("/[\\@\\-\\.\\_]/g")) {
			if (pwd.contains(seq) || pwd.contains(seq.toUpperCase()) || pwd.contains(seq.toLowerCase())) {
				contains = true;
				break;
			}
		}

		if (pwd.contains(uname)) {
			contains = true;
		}
		return contains;
	}
}
