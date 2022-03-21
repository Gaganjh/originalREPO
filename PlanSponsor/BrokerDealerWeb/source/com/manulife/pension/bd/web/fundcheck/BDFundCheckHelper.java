package com.manulife.pension.bd.web.fundcheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.service.broker.valueobject.ProducerCodeInfo;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.valueobject.BrokerEntityAssoc;

public class BDFundCheckHelper {
	
	private static final String LEVEL1_VERSION = "level1Version";
	private static final String LEVEL2_VERSION = "level2Version";
	private static final String INTERNAL_VERSION = "internalVersion";
	
	private Map<String, String> FUNDCHECK_ROLE_VERSION_MAP;
	
	private Map<String, String> LEVEL2_SEARCH_TYPE_MAP;
	
	private Map<String, String> INTERNAL_SEARCH_TYPE_MAP;
	
	public BDFundCheckHelper() {
		
		FUNDCHECK_ROLE_VERSION_MAP = new HashMap<String, String>();
		
		LEVEL2_SEARCH_TYPE_MAP = new LinkedHashMap<String, String>();
		
		INTERNAL_SEARCH_TYPE_MAP = new LinkedHashMap<String, String>();
		
		FUNDCHECK_ROLE_VERSION_MAP.put(BDUserRoleType.BasicFinancialRep.getRoleId(), LEVEL1_VERSION);
		FUNDCHECK_ROLE_VERSION_MAP.put(BDUserRoleType.FinancialRep.getRoleId(), LEVEL2_VERSION);
		FUNDCHECK_ROLE_VERSION_MAP.put(BDUserRoleType.FirmRep.getRoleId(), LEVEL1_VERSION);
		FUNDCHECK_ROLE_VERSION_MAP.put(BDUserRoleType.FinancialRepAssistant.getRoleId(), LEVEL2_VERSION);
		FUNDCHECK_ROLE_VERSION_MAP.put(BDUserRoleType.RVP.getRoleId(), INTERNAL_VERSION);
		FUNDCHECK_ROLE_VERSION_MAP.put(BDUserRoleType.CAR.getRoleId(), INTERNAL_VERSION);
		FUNDCHECK_ROLE_VERSION_MAP.put(BDUserRoleType.SuperCAR.getRoleId(), INTERNAL_VERSION);
		FUNDCHECK_ROLE_VERSION_MAP.put(BDUserRoleType.InternalBasic.getRoleId(), INTERNAL_VERSION);
		FUNDCHECK_ROLE_VERSION_MAP.put(BDUserRoleType.Administrator.getRoleId(), INTERNAL_VERSION);
		FUNDCHECK_ROLE_VERSION_MAP.put(BDUserRoleType.NationalAccounts.getRoleId(), INTERNAL_VERSION);
		FUNDCHECK_ROLE_VERSION_MAP.put(BDUserRoleType.RIAUserManager.getRoleId(), INTERNAL_VERSION);
		FUNDCHECK_ROLE_VERSION_MAP.put(BDUserRoleType.ContentManager.getRoleId(), INTERNAL_VERSION);
		
		LEVEL2_SEARCH_TYPE_MAP.put(BDConstants.FUNDCHECK_INPUT_SELECT_KEY, BDConstants.FUNDCHECK_INPUT_SELECT_LABEL);
		LEVEL2_SEARCH_TYPE_MAP.put(BDConstants.FUNDCHECK_INPUT_CONTRACT_NAME_KEY, BDConstants.FUNDCHECK_INPUT_CONTRACT_NAME_LABEL);
		LEVEL2_SEARCH_TYPE_MAP.put(BDConstants.FUNDCHECK_INPUT_CONTRACT_NUMBER_KEY, BDConstants.FUNDCHECK_INPUT_CONTRACT_NUMBER_LABEL);
		
		INTERNAL_SEARCH_TYPE_MAP.put(BDConstants.FUNDCHECK_INPUT_SELECT_KEY, BDConstants.FUNDCHECK_INPUT_SELECT_LABEL);
		INTERNAL_SEARCH_TYPE_MAP.put(BDConstants.FUNDCHECK_INPUT_CONTRACT_NAME_KEY, BDConstants.FUNDCHECK_INPUT_CONTRACT_NAME_LABEL);
		INTERNAL_SEARCH_TYPE_MAP.put(BDConstants.FUNDCHECK_INPUT_CONTRACT_NUMBER_KEY, BDConstants.FUNDCHECK_INPUT_CONTRACT_NUMBER_LABEL);
		INTERNAL_SEARCH_TYPE_MAP.put(BDConstants.FUNDCHECK_INPUT_FR_NAME_KEY, BDConstants.FUNDCHECK_INPUT_FR_NAME_LABEL);
		INTERNAL_SEARCH_TYPE_MAP.put(BDConstants.FUNDCHECK_INPUT_PRODUCER_CODE_KEY, BDConstants.FUNDCHECK_INPUT_PRODUCER_CODE_LABEL);		
	}
	
	/**
	 * Returns the correct page version for the given role
	 * 
	 * @param roleId
	 * @return String
	 */
	public String getFundCheckPageVersion(String roleId) {
		return FUNDCHECK_ROLE_VERSION_MAP.get(roleId);
	}

	/**
	 * Returns associated producer codes with the broker entities
	 * 
	 * @param brokerEntities
	 * @return List<Long>
	 */
	@SuppressWarnings("unchecked")
	public List getAssociatedProducerCodes(List<BrokerEntityAssoc> brokerEntities) {
		List producerCodes = new ArrayList();
		for(BrokerEntityAssoc entity : brokerEntities) {
			List<ProducerCodeInfo> producerCodeInfoList = entity.getBrokerEntity().getProducerCodes();
			for(ProducerCodeInfo info : producerCodeInfoList) {
				producerCodes.add(info.getId());
			}
		}
		return producerCodes;
	}
	
	/**
	 * Returns search input types for the level 2 page
	 * 
	 * @return Map<String, String>
	 */
	public  Map<String, String> getLevel2SearchTypeMap() {
		return LEVEL2_SEARCH_TYPE_MAP;
	}
	
	/**
	 * Returns search input types for the internal page
	 * 
	 * @return Map<String, String>
	 */
	public Map<String, String> getInternalSearchTypeMap() {
		return INTERNAL_SEARCH_TYPE_MAP;
	}	
}
