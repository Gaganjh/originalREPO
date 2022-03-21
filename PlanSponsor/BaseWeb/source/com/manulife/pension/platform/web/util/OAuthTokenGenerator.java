package com.manulife.pension.platform.web.util;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.exception.SystemException;


public class OAuthTokenGenerator {
    
    public BaseEnvironment baseEnvironment= new BaseEnvironment();
    public static final String GRANT_TYPE="client_credentials";
    public JsonFactory nodeFactory = new JsonFactory();
    private final Logger LOGGER = Logger.getLogger(this.getClass());
    
    /**This method add the token value, Accept and content type values to the 
     * Http header and returns to the calling method.
     *  
     * @return HttpHeaders
     */
    private  HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

     /**This method will call the Edge URL with a JSON payload to get the token
     * @param name
     * @return Integer
     * @throws Exception
     */
    public  String getOAuthToken(String edgeGatewayURL, String clientId,String clientSecret) throws SystemException {
        String jsonString="";
        String token = null;
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            JsonGenerator payload = nodeFactory.createGenerator(stream, JsonEncoding.UTF8);
            payload.writeStartObject();
            payload.writeStringField("client_id", clientId);
            payload.writeStringField("client_secret", clientSecret);
            payload.writeStringField("grant_type", GRANT_TYPE);
            payload.writeEndObject();
            payload.close();
            String body = payload.getOutputTarget().toString();
            RestTemplate restTemplate = new RestTemplate();
            // Data attached to the request.
            HttpEntity<String> requestBody = new HttpEntity<>(body,getHttpHeaders());
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonString = mapper.writeValueAsString(requestBody);
            ResponseEntity<String> result 
                 = restTemplate.postForEntity(edgeGatewayURL, requestBody, String.class);
            LOGGER.debug("Status code:" + result.getStatusCode());
            LOGGER.debug("(Client Side) Response received from OAuth URL: "+ result.getBody());
            // Code = 200.
            if (result.getStatusCode() == HttpStatus.OK) {
                JsonObject jsonObject = new JsonParser().parse(result.getBody()).getAsJsonObject();
                token= jsonObject.get("access_token").getAsString();
            }
 	    }
	    catch (JsonProcessingException jpe) {
	    	LOGGER.error("JsonProcessing Exception while getting OAuth Token for SDU Widget :  "+jpe.getMessage());
	    	throw new SystemException("JsonProcessingException while getting OAuth Token for SDU Widget :  "+jpe.getMessage());
	    	
		}
 	    catch(Exception e) {
 	    	 LOGGER.error("Exception while getting OAuth Token for SDU Widget: "+ e.getMessage());
 	    	throw new SystemException("Exception while getting OAuth Token for SDU Widget: "+ e.getMessage());
 	    }
        return token;
    }
    

}
