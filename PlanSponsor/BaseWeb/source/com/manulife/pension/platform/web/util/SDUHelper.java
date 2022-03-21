package com.manulife.pension.platform.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.secureDocumentUpload.SDUConstants;
import com.manulife.pension.service.account.entity.AvailabilityStatus;
import com.manulife.pension.service.awd.util.AwdConstants;
import com.manulife.pension.service.awd.util.AwdHelper;
import com.manulife.pension.service.contract.ContractNotExistException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.SDUContractInfoVO;
import com.manulife.pension.util.BaseEnvironment;

public final class SDUHelper {
	
	 private static final Logger logger = Logger.getLogger(SDUHelper.class);
	 
	 private static BaseEnvironment baseEnvironment= new BaseEnvironment();  

	public static SDUContractInfoVO getContractDetails(String contractNumber)
	{
		
		SDUContractInfoVO sDUContractInfoVO=null;
		try {
		sDUContractInfoVO=ContractServiceDelegate.getInstance().getSDUContractDetails(Integer.parseInt(contractNumber));		
		}catch(ContractNotExistException ce) {
			logger.error(ce.getMessage());
		}
		catch(SystemException e) {
			logger.error(e.getMessage());
		}
		return sDUContractInfoVO;		
	}
	
	/**
	 * Checks if current contract status is one of the following: Proposal
	 * Signed (PS), Details Complete (DC), Pending Contract Approval (PC),
	 * Contract Approved (CA) In this case user has access only to SDU functionality.
	 */
	public static boolean isPreActiveContract(String contractStatus) {		
		if (Contract.STATUS_PROPOSAL_SIGNED.equals(contractStatus) || Contract.STATUS_PENDING_CONTRACT_APPROVAL.equals(contractStatus) || 
				Contract.STATUS_DETAILS_COMPLETED.equals(contractStatus) || Contract.STATUS_CONTRACT_APPROVED.equals(contractStatus) ) {
			return true;	
		}
		return false;
	}
	
	public static String getAfterMarketIndicator(Date submissionDate){

		String afterMarketIndicator="";
		SimpleDateFormat sdf = new SimpleDateFormat(AwdConstants.DATE_PATTERN_WITHOUT_TS,Locale.US);
		AvailabilityStatus aStatus = null;
		try{
			String formattedDate = AwdHelper.getFormattedDate(submissionDate,sdf);
			String formattedEODTS = formattedDate + " " + AwdConstants.TIMESTAMP_SUFFIX;
			SimpleDateFormat sdfTS = new SimpleDateFormat(AwdConstants.DATE_PATTERN_WITH_TS,Locale.US);
			aStatus = AccountServiceDelegate.getInstance().getNYSEAvailabilityStatusAsOf(AwdHelper.getParsedDate(formattedEODTS, sdfTS),false);
			//Format received date 
			String formattedReceivedDateStr = AwdHelper.getFormattedDate(submissionDate,sdf);
			Date formattedReceivedDate = AwdHelper.getParsedDate(formattedReceivedDateStr,sdf);

			//Format NYSE date
			Date nyseDate = aStatus.getDownTime();
			String formattedNyseDateStr = AwdHelper.getFormattedDate(nyseDate,sdf);
			Date formattedNyseDate = AwdHelper.getParsedDate(formattedNyseDateStr,sdf);

			//Compare if the dates match (only date. time is reset to 00:00:00)
			if(formattedReceivedDate.equals(formattedNyseDate)) {
				//if time before close of NYSE set to Indexed	
				if(submissionDate.before(aStatus.getDownTime()))  {
					afterMarketIndicator="N";
				}
				else {
					afterMarketIndicator="Y";
				}
			}
			else {
				//If weekend or holiday NYSE close time is of the previous working day
				if(submissionDate.after(aStatus.getDownTime())) {
					afterMarketIndicator="Y";
				}					
			}
		}
		catch(Exception e){
			logger.error(e.getMessage(),e);
		}

		return afterMarketIndicator;		
	}
	
	public static String getApigeeEdgeProxyURL(){			
		return baseEnvironment.getNamingVariable(
				SDUConstants.APIGEE_EDGE_PROXY_URL, null);
	}


	public static  HttpHeaders getHttpHeaders() throws SystemException {
		HttpHeaders headers = new HttpHeaders(); 		
		BaseEnvironment baseEnvironment= new BaseEnvironment();
		OAuthTokenGenerator oAuthTokenGenerator = new OAuthTokenGenerator();			
		String accessToken =	oAuthTokenGenerator.getOAuthToken(baseEnvironment.getNamingVariable(
				SDUConstants.APIGEE_OAUTH_TOKEN_URL, null),baseEnvironment.getNamingVariable(
				SDUConstants.FILE_UPLOAD_WIDGET_USER_ID, null), baseEnvironment.getNamingVariable(
						SDUConstants.FILE_UPLOAD_WIDGET_PASSWORD, null));

		if (logger.isDebugEnabled()) {
			logger.debug("Oauth Token received");
		}		
		headers.setContentType(MediaType.APPLICATION_JSON);
 		headers.set("Authorization", "Bearer " + accessToken );
 	    return headers;
	}
	public static String getAccessToken() throws SystemException {
		OAuthTokenGenerator oAuthTokenGenerator = new OAuthTokenGenerator();			
		String accessToken =	oAuthTokenGenerator.getOAuthToken(baseEnvironment.getNamingVariable(
				SDUConstants.APIGEE_OAUTH_TOKEN_URL, null),baseEnvironment.getNamingVariable(
				SDUConstants.FILE_UPLOAD_WIDGET_USER_ID, null), baseEnvironment.getNamingVariable(
						SDUConstants.FILE_UPLOAD_WIDGET_PASSWORD, null));
		if (logger.isDebugEnabled()) {
			logger.debug("Oauth Token received");
		}	
		return accessToken;
	}	

}
