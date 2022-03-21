package com.manulife.pension.ireports.dao;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

/**
 * Ssort alphas first in alpha order, alphanumerics next in numeric order then numerics next in numeric order
 */
public class FootnoteSymbolComparator implements Comparator {
	private static String STATIC_FOOTNOTE_SYMBOL_PREFIX = "*";
	
	public int compare (Object p1, Object p2){
		String symbol1 = (String)p1;
		String symbol2 = (String)p2;
		// Introduced as part of ACR_REWRITE
		if(symbol1.startsWith("i") && symbol2.startsWith("i")){
			Integer marketIndexSymbol1 = Integer.valueOf(symbol1.substring(1));
			Integer marketIndexSymbol2 = Integer.valueOf(symbol2.substring(1));
			return marketIndexSymbol1.compareTo(marketIndexSymbol2);
		}
		
		else if (StringUtils.isNumeric(symbol1)) {
			if (StringUtils.isNumeric(symbol2)) { //both numeric
				int symbol1Int = Integer.parseInt(symbol1);
				int symbol2Int = Integer.parseInt(symbol2);
				return symbol1Int - symbol2Int;
			} else {
				return 1;
			}
		} else {
			if (StringUtils.isNumeric(symbol2)) {
				return -1;
			} else { //both non-numeric
				String symbol1StrVal = StringUtils.replaceChars(symbol1, STATIC_FOOTNOTE_SYMBOL_PREFIX, "");
				String symbol2StrVal = StringUtils.replaceChars(symbol2, STATIC_FOOTNOTE_SYMBOL_PREFIX, "");
				if ( !StringUtils.isEmpty(symbol1StrVal) &&
					 !StringUtils.isEmpty(symbol2StrVal) &&
					StringUtils.isNumeric(symbol1StrVal) &&
					StringUtils.isNumeric(symbol2StrVal)) { // both alphanumeric
					return 
						Integer.parseInt(symbol1StrVal) - Integer.parseInt(symbol2StrVal);
				} else if (StringUtils.isNumeric(symbol1StrVal)) {
					return 1;
				} else if (StringUtils.isNumeric(symbol2StrVal)) {
					return -1;
				}
				// both alpha
				return symbol1.compareTo(symbol2);
			}
		}
	}}