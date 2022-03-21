package com.manulife.pension.bd.web.firmsearch;

import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.delegate.BrokerServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.broker.exception.AmbiguousFirmNameException;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;

/**
 * Utility class to help the firm name search
 * 
 * @author guweigu
 * 
 */
public class FirmSearchUtils {
	private static final Logger logger = Logger.getLogger(FirmSearchUtils.class);
	
	public static String getFirmPartyId(String firmName) throws SystemException {
		try {
			BrokerDealerFirm firm = BrokerServiceDelegate.getInstance(
					BDConstants.BD_APPLICATION_ID).getBDFirmByName(firmName);
			if (firm != null) {
				return Long.toString(firm.getId());
			}
		} catch (AmbiguousFirmNameException e) {
			logger.error("Ambigious firm name: " + firmName, e);
		}
		return "";
	}
}
