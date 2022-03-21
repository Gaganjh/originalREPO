/**
 * 
 */
package com.manulife.pension.ireports.report.util;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;

import com.manulife.pension.ireports.util.propertymanager.PropertyManager;

public class ReportStrings implements MessageSourceAware {
	//public static MessageResources REPORT_MESSAGES = MessageResources.getMessageResources("pdf_reports");
	private MessageSource messageSource;
	private String siteCode;
	private String prefix;
	
	public ReportStrings(String prefix, String siteCode) {
		super();
		this.siteCode = siteCode;
		this.prefix = prefix;
	}
	
	public String getString(String key) {
		String plainKey = prefix + key;
		String siteCodeKey = prefix + key + "." + siteCode;
		String siteCodeKeyValue = messageSource.getMessage(siteCodeKey, null, null);
		String plainKeyValue = messageSource.getMessage(plainKey, null, null);
		if (!siteCodeKeyValue.isEmpty()) {
			return PropertyManager.getString(siteCodeKey);
		} else if (!plainKeyValue.isEmpty()) {
			return PropertyManager.getString(plainKey);
		} else {
			throw new IllegalArgumentException("Invalid/missing key (" + key +") for pdf messages with prefix=" + prefix + " and siteCode=" + siteCode );
		}
            }

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource=messageSource;
	}
        }
