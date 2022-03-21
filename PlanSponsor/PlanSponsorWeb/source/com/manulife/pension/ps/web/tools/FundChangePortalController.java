package com.manulife.pension.ps.web.tools;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.authtoken.AuthTokenProcessor;
import com.manulife.pension.platform.web.authtoken.AuthTokenProcessorFactory;
import com.manulife.pension.platform.web.authtoken.AuthTokenUtility;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;
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
public class FundChangePortalController  {
	
	private static final Logger logger = Logger.getLogger(FundChangePortalController.class);
	
	public static final Map<String,String> forwards = new HashMap<>() ;
	static{
		forwards.put("input","/tools/fundChangePortal.jsp");
		forwards.put("default","/tools/fundChangePortal.jsp");
		}
	
	@RequestMapping(value ="/fundChangePortal/", method =  {RequestMethod.GET}) 
	
	 public String doDefault(HttpServletRequest request,
				HttpServletResponse response) throws SystemException {
		
		String contractaccessCode = null;
		Contract currentContract = null;
		int contractNumber = 0;

		UserProfile userProfile = getUserProfile(request);

		if (userProfile != null) {
			currentContract = userProfile.getCurrentContract();
		}
		
		if (currentContract != null) {
			contractNumber = currentContract.getContractNumber();
			if (contractNumber != 0) {
				try {
					contractaccessCode = AccessCodeDao.INSTANCE.getAccessCode(currentContract.getContractNumber());
				} catch (ContractNotExistException e) {
					logger.error("Contract not found " + e.getMessage(), e);
				}
			}
		}

		AuthTokenProcessor tokenProcessor = AuthTokenProcessorFactory.getInstance().getAuthTokenProcessor();
		
		String jwtToken = null;
		
		try {
			 jwtToken = tokenProcessor.generateAuthToken(contractNumber, contractaccessCode, "PSW");
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

	public static UserProfile getUserProfile(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return (UserProfile) session.getAttribute(Constants.USERPROFILE_KEY);
		} else {
			return null;
		}
	}
}
