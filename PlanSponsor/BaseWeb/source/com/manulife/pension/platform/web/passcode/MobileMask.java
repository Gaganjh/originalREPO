package com.manulife.pension.platform.web.passcode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


public class MobileMask {
	
	
	private static final String STEP_UP_MASKING_STRING = "***-***-";
	
	public static String maskPhone(String phone){
		String maskedMobile = null;
		if(StringUtils.isNotBlank(phone) && phone.length()==10){
			
				StringBuilder sb = new StringBuilder();
				sb.append(STEP_UP_MASKING_STRING);
				sb.append(phone.substring(6, 10));
				maskedMobile = sb.toString();
		}
		return maskedMobile;
	
	}
	
	public static String mask(String mobile) {
		String maskedMobile = StringUtils.trimToEmpty(mobile);

		Pattern pattern = Pattern.compile("(\\d{3})(\\d{3})(\\d{4})");
        Matcher m = pattern.matcher(maskedMobile);
        if(m.matches()){
        	maskedMobile = m.replaceAll("($1) $2-$3");
        }
        return maskedMobile;
	
	}	
}
