package com.manulife.pension.bd.web.tools;

import java.util.HashMap;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.authtoken.AuthTokenProcessor;
import com.manulife.pension.platform.web.authtoken.AuthTokenProcessorFactory;
import com.manulife.pension.platform.web.authtoken.AuthTokenUtility;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.dao.AccessCodeDao;
import com.manulife.pension.service.contract.valueobject.Contract;

/**
 * 
 * This is the controller class for Fund Change Portal
 * @author dasaran
 *
 */

@Controller
@RequestMapping(value = "/tools")
public class FundChangePortalController {
	
	private static final Logger logger = Logger.getLogger(FundChangePortalController.class);
	
	public static Map<String,String> forwards = new HashMap<>();
	static{
		forwards.put("input","/WEB-INF/tools/fundChangePortal.jsp");
		forwards.put("default","/WEB-INF/tools/fundChangePortal.jsp");
		}
	
	@RequestMapping(value ="/fundChangePortal/", method =  {RequestMethod.GET}) 
	
	 public String doDefault(HttpServletRequest request,
				HttpServletResponse response) throws SystemException {
		
		String contractaccessCode = null;
		Contract contract = null;
		int contractNumber = 0;

        BobContext bob = BDSessionHelper.getBobContext(request) ;
		
		if (bob != null) {
			contract = bob.getCurrentContract();
		}
		
		if (contract != null) {
			contractNumber = contract.getContractNumber();
			if (contractNumber != 0) {
				try {
					contractaccessCode = AccessCodeDao.INSTANCE.getAccessCode(contract.getContractNumber());
				} catch (ContractNotExistException e) {
					logger.error("Contract not found " + e.getMessage(), e);
				}
			}
		}

		AuthTokenProcessor tokenProcessor = AuthTokenProcessorFactory.getInstance().getAuthTokenProcessor();
		
		String jwtToken = null;
		
		try {
			 jwtToken = tokenProcessor.generateAuthToken(contractNumber, contractaccessCode, "FRW");
		} catch (SystemException e) {
			logger.error("Error in processing authentication token request: " + e.getMessage(), e);
		}
		
		if(StringUtils.isBlank(jwtToken)) {
			
		logger.error("JWToken recieved is either empty or null, JWToken: " + jwtToken);
		}
		request.setAttribute("redirected_ISAM_MLC_URL", AuthTokenUtility.REDIRECTED_TO_ISAM_EAI_MLC_URL);
		request.setAttribute("authorization", AuthTokenUtility.REDIRECTED_TO_ISAM_EAI_MLC_AUTHORIZATION);
		request.setAttribute("generatedAuthToken", jwtToken);

	    return forwards.get("default");
	 }

}
