package com.manulife.pension.ps.web.withdrawal.util;


import java.net.URI;
import java.util.List;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


import com.manulife.esb.xsd.jh.workmanagement.AwdInstance;
import com.manulife.esb.xsd.jh.workmanagement.CreateObjectsResponse;
import com.manulife.esb.xsd.jh.workmanagement.FieldValue;
import com.manulife.pension.platform.web.util.OAuthTokenGenerator;
import com.manulife.pension.util.BaseEnvironment;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


public class WithdrawalRequestAWDCreateWorkItem {
	
	private static final BaseEnvironment baseEnvironment= new BaseEnvironment(); 
	private static String WORK_MANAGEMENT_SERVICE_URL;
	private static String APIGEE_OAUTH_TOKEN_URL;
	private static String WORK_MANAGEMENT_SERVICE_USERNAME;
	private static String WORK_MANAGEMENT_SERVICE_PASSWORD;
	
	static {
	    WORK_MANAGEMENT_SERVICE_URL=baseEnvironment.getNamingVariable("workmanagement.service.url", null); 
	    APIGEE_OAUTH_TOKEN_URL=baseEnvironment.getNamingVariable("apigee.oauth.token.url", null);
	    WORK_MANAGEMENT_SERVICE_USERNAME=baseEnvironment.getNamingVariable("awd.apigee.oauth.token.clientid", null);
	    WORK_MANAGEMENT_SERVICE_PASSWORD=baseEnvironment.getNamingVariable("awd.apigee.oauth.token.secret", null);
	    
    }
    public static final String OW_APPLICATION_ID="17269";
    private static final Logger logger = Logger.getLogger(WithdrawalRequestAWDCreateWorkItem.class);
	
	/**This method add the token value, Accept and content type values to the 
	 * Http header and returns to the calling method.
	 * @return HttpHeaders
	 */
	private  HttpHeaders getHttpHeaders(String oAuthToken) {
		HttpHeaders headers = new HttpHeaders();
 		headers.setContentType(MediaType.APPLICATION_JSON);
 		headers.add( "Authorization", "Bearer "+oAuthToken);
 	    return headers;
	}
	
	/**This method will send the withdrawal request details to AWD via the API call
     *  @param String ssn
     *  @param String contractNumber
     *  @param String transactionNumber
     *  @param String submissionId
     *  @param String file
     *  @return HashMap<String,String> awdWorkItemValues
     */
	public HashMap<String,String> sendOWDetailsToAwd(String ssn, String contractNumber, String transactionNumber, String submissionId, String file)
			throws Exception {
		HashMap<String,String> awdWorkItemValues = new HashMap<String,String>();
		Gson gson = new Gson();
		HttpEntity<String> requestBody=null;
		URI workManagementServiceURL =null;
		String jsonRequest = WithdrawalRequestAWDMapper.mapAWDServiceRequest(ssn, contractNumber, transactionNumber, submissionId, file);
		try {
			requestBody = new HttpEntity<>(jsonRequest, 
	 	    		getHttpHeaders(new OAuthTokenGenerator().getOAuthToken(APIGEE_OAUTH_TOKEN_URL,WORK_MANAGEMENT_SERVICE_USERNAME,WORK_MANAGEMENT_SERVICE_PASSWORD)));
	 	   	workManagementServiceURL = UriComponentsBuilder.fromUriString(WORK_MANAGEMENT_SERVICE_URL+"create?applicationId="+OW_APPLICATION_ID).build().encode().toUri();
	 	   	ResponseEntity<String> response = new RestTemplate().exchange(workManagementServiceURL, HttpMethod.POST, requestBody, String.class);
	 	   	if (response.getStatusCode() == HttpStatus.OK) {
	 	   	    HashMap<String, Object> responseMap =  (HashMap<String, Object>) gson.fromJson(response.getBody().toString(), HashMap.class);
	 	   		CreateObjectsResponse createObjectsResponse = gson.fromJson(gson.toJson(responseMap.get("detail")), CreateObjectsResponse.class);
				createObjectsResponse.getObjectsResponse().getFolderInstanceOrSourceInstanceOrWorkInstance().forEach(createWorkInstance->{
					if(((LinkedTreeMap)createWorkInstance).get("status")!=null) {//only source instance has status
						AwdInstance awdInstance = gson.fromJson(gson.toJson(((LinkedTreeMap)createWorkInstance).get("awdInstance")), AwdInstance.class);
						awdWorkItemValues.put("workItemURL", getWorkItemURL(awdInstance));
						awdWorkItemValues.put("workItemId", awdInstance.getId());
					}
				}
						);//end of for each
		    }else {
		        awdWorkItemValues.put("errorCode", gson.toJson(gson.fromJson(response.getBody().toString(), HashMap.class).get("code")));
                awdWorkItemValues.put("errorMessage", gson.toJson(gson.fromJson(response.getBody().toString(), HashMap.class).get("detailsMessage")));
		    }
	 	}
		
		catch (Exception e) { 
		    logger.error("The call to send withdrawal request for " + transactionNumber+" and "+ submissionId+" to AWD failed due to : "+e.getMessage()+
		            " URL= "+workManagementServiceURL!=null?workManagementServiceURL.toString():null+" RequestBody="+requestBody!=null?requestBody.toString():null);
	    	e.printStackTrace();
	 	}
		return awdWorkItemValues;
	}
	/**This method will extract the work item URL from the API call's response
     *  @param AwdInstance awdInstance
     *  @return String workItemURL
     */
	private String getWorkItemURL(AwdInstance awdInstance) {
		String workItemURL = "";
		List<FieldValue> fieldValues = awdInstance.getFieldValues().getFieldValue();
		if (null != fieldValues) {
			for (FieldValue fieldValue : fieldValues) {
				if ("WIURL".equalsIgnoreCase(fieldValue.getName())) {
					workItemURL = fieldValue.getValue();
					break;
				}
			}
		}
		return workItemURL;
	}
}


