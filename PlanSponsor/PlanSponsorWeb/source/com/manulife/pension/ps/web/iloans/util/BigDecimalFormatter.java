package com.manulife.pension.ps.web.iloans.util;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

public class BigDecimalFormatter {

/**
 * DataFormatter constructor comment.
 */
public BigDecimalFormatter() {
	super();
}
	public static String format(String value, String minDecimals, String maxDecimals) {
		if (StringUtils.isBlank(value)) {
			return "";
		} else {
			return format(new BigDecimal(value), Integer.parseInt(minDecimals),
					Integer.parseInt(maxDecimals));
		}
	}
	
	public static String format(BigDecimal value, int minDecimals, int maxDecimals) {
		if(value==null) return null;

		String bigDecimalString = value.toString();
		int scale = value.scale();
		if(scale>0) {
			//definitely have decimal
			int i;
			//find index of first non 0 from right to left
			for(i=bigDecimalString.length()-1; i>=0; i--) {
				char c = bigDecimalString.charAt(i);
				if(c != '0') break;
			}
			//reset the bigDecimal String to have zeros removed
			bigDecimalString = bigDecimalString.substring(0, i+1);
		}

		//create new big decimal to make it easy to work with scales
		BigDecimal newValue = new BigDecimal(bigDecimalString);
		int newScale = newValue.scale();
		if(minDecimals >= 0) {
			//we specified a min scale so check if we need to bump up the scale
			if(newScale < minDecimals) {
				newValue = newValue.setScale(minDecimals);
			}
		}
		if(maxDecimals >= 0) {
			//we specified the max scale so check if we need to round the value
			if(newScale > maxDecimals) {
				newValue = newValue.setScale(maxDecimals, BigDecimal.ROUND_HALF_UP);				
			}
		}
		return newValue.toString();
			
	}
	public static String formatAsCurrency(BigDecimal value, int minDecimals, int maxDecimals) {
		String stringValue = format(value, minDecimals, maxDecimals);
		
		if(stringValue==null) return null;

		StringBuffer buff = new StringBuffer("");
		int decimalPos = stringValue.indexOf(".");
		int startPos;
		if(decimalPos > 0) {
			buff.append(stringValue.substring(decimalPos));
		    startPos = decimalPos - 1;
		} else {
			startPos = stringValue.length() - 1;
		}
		
		int j = 0;
		
		for(int i=startPos; i>=0; i--) {
			if (j == 3) {
				buff.insert(0, ",");
				j = 0;
			}
			buff.insert(0,stringValue.charAt(i)); 
			j++;
		}
		return "$"+buff.toString();
	}
}
