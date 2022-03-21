package com.manulife.pension.bd.web.userprofile;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.delegate.BrokerServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.broker.valueobject.NFACodeConstants;

/**
 * The utility class for mapping the bdfirm to approving firm's NFA code
 * @author guweigu
 *
 */
public class ApprovingFirmUtil {
	private static final Logger log = Logger.getLogger(ApprovingFirmUtil.class);
	
	private static ApprovingFirmUtil instance = new ApprovingFirmUtil();
	
	private Map<Long, String> bdFirmMapToApprovingFirm;
	
	public static ApprovingFirmUtil getInstance() {
		return instance;
	}
	
	private ApprovingFirmUtil() {
		try {
			Map<String, Set<BrokerDealerFirm>> map = BrokerServiceDelegate
					.getInstance(BDConstants.BD_APPLICATION_ID)
					.getApprovingFirmMap();
			bdFirmMapToApprovingFirm = new HashMap<Long, String>();
			for (String nfaCode : map.keySet()) {
				Set<BrokerDealerFirm> firmSet = map.get(nfaCode);
				for (BrokerDealerFirm f : firmSet) {
					bdFirmMapToApprovingFirm.put(f.getId(), nfaCode);
				}
			}
		} catch (SystemException e) {
			log.error("Fail to get approving firm map");
			throw new RuntimeException("Cannot get approving firms", e);
		}
	}
	
	/**
	 * Map the bdfirm's party id to corresponding approving firms' NFA code
	 * if no approving firm is mapped, return as Independent. 
	 * @param bdFirmPartyId
	 * @return
	 */
	public String getApprovingFirmNFACode(Long bdFirmPartyId) {
		String nfaCode = bdFirmMapToApprovingFirm.get(bdFirmPartyId);
		return nfaCode == null ? NFACodeConstants.Independent : nfaCode;
	}
}
