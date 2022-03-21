package com.manulife.pension.ps.web.noticemanager.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeMailingOrderVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.content.GenericException;

/**
 * Merrill Interacting function for placing the notice order and handling the response.
 * @author krishta
 *
 */
public class MerillInteractionNoticeManager 
 {
	private static final Logger logger = Logger.getLogger(MerillInteractionNoticeManager.class);
	
	@SuppressWarnings("unchecked")
	public static Collection getNoticeManagerUploadedAccessPermission(HttpServletRequest request, HttpServletResponse response,
			byte[] generatedPackage,byte[] censusAddressFile, UserProfile userProfile, PlanNoticeMailingOrderVO planNoticeMailingOrderVO,String addressFileName) throws SystemException {
       
		CloseableHttpClient httpclient = HttpClients.createSystem();
		
        Collection merrillErrors = new ArrayList();
        logger.info("----Merill order placing control starts here----");
        
        try {
        	BaseEnvironment environment = new BaseEnvironment();
        	String merrillAccessUrl = "";
        	String statusUpdateUrl = "";
        	try{
        		merrillAccessUrl = environment.getNamingVariable(Constants.MERRILL_ACCESS_URL, null);
        		statusUpdateUrl =  environment.getNamingVariable(Constants.ORDER_STATUS_UPDATE_URL, null);
        	}catch (Exception e){
        		throw new SystemException(e,  "Error occured while accessing the Merril and order status update URL");
        	}
        	
        	String siteLocation = Environment.getInstance().getSiteLocation();
        	String siteProtocol = Environment.getInstance().getSiteProtocol();
        	String siteDomain = Environment.getInstance().getSiteDomain();
        	String pswUrl = siteProtocol+"://"+siteDomain;
            HttpPost httppost = new HttpPost(merrillAccessUrl);
            String userEmailId = "";
            userEmailId =  userProfile.getEmail();
            if (logger.isDebugEnabled()) {
            	logger.debug("----Merill Interaction----");
            	logger.debug("Below information is being sent to Merrill URL: " + merrillAccessUrl);
            	logger.debug("Merrill : Email Id: " +userEmailId );
            	logger.debug("Merrill : Tracking Number: " +planNoticeMailingOrderVO.getOrderNumber() );
            	logger.debug("Merrill : Contract Mailing Address: " +  planNoticeMailingOrderVO.getMailingAddress());
            	logger.debug("Merrill : Name of the mailing: " +planNoticeMailingOrderVO.getMailingName() );
            	logger.debug("Merrill : Site location: " +siteLocation );
            	logger.debug("Merrill : pswUrl: " +pswUrl );
            	logger.debug("Merrill : statusUpdateUrl: " +statusUpdateUrl );
            }
            
            StringBody trackingNumber = new StringBody(planNoticeMailingOrderVO.getOrderNumber().toString(), ContentType.TEXT_PLAIN);
            StringBody contractMailingAddress = new StringBody(planNoticeMailingOrderVO.getMailingAddress(), ContentType.TEXT_PLAIN);
            StringBody nameOfMailing = new StringBody(planNoticeMailingOrderVO.getMailingName(), ContentType.TEXT_PLAIN);
            StringBody emailAddress = new StringBody(userEmailId, ContentType.TEXT_PLAIN);
            StringBody pageRootURL = new StringBody(pswUrl, ContentType.TEXT_PLAIN);
            StringBody statusURL = new StringBody(statusUpdateUrl, ContentType.TEXT_PLAIN);
            
            StringBody md5Hash = null;
			try {
				md5Hash = new StringBody(new ComputeMD5Hash().createHash(userEmailId), ContentType.TEXT_PLAIN);
			} catch (NoSuchAlgorithmException e) {
				logger.error("Exception while MD5 hashing Email Id");
				throw new SystemException(e,  "Problem with computing MD5 hashing");
				
			}
		
            Integer contractId = userProfile.getCurrentContract().getContractNumber();
            if (logger.isDebugEnabled()) {
            	logger.debug("Merrill : Hashed  Mail address : " + md5Hash.toString() );
            	logger.debug("Merrill : Contract Number :" +contractId );
            }
          
            StringBody contractNumber = new StringBody(contractId.toString(), ContentType.TEXT_PLAIN);
            FileBody pdfDocument = new FileBody(generateFile(generatedPackage,"document.pdf"), ContentType.create("application/pdf"), "document.pdf");
            if(pdfDocument!=null &&logger.isDebugEnabled()){
            	logger.debug("Merrill : PDF attached Length: " +pdfDocument.getContentLength() );
            	logger.debug("Merrill : PDF attached Name: " +pdfDocument.getFilename() );
            }
            FileBody addressFile = new FileBody(generateFile(censusAddressFile, addressFileName), 
            					ContentType.create("application/vnd.ms-excel"), addressFileName);
            if(addressFile!=null && logger.isDebugEnabled()){
            	logger.debug("Merrill : addressFile attached Length: " +addressFile.getContentLength() );
            	logger.debug("Merrill : addressFile attached Name: " +addressFile.getFilename() );
            }
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("trackingNumber", trackingNumber)
                .addPart("contractMailingAddress", contractMailingAddress)
                .addPart("nameOfMailing", nameOfMailing)
                .addPart("emailAddress", emailAddress)
                .addPart("md5Hash", md5Hash)
                .addPart("contractNumber", contractNumber)
                .addPart("pdfDocument", pdfDocument)
                .addPart("addressFile", addressFile)
                .addPart("pageRootURL", pageRootURL)
                .addPart("statusURL", statusURL)
                .build();
            httppost.setEntity(reqEntity);
            if (logger.isDebugEnabled()) {
            	logger.debug("----Placing request to Merill----- " + httppost.getRequestLine());
            }
        	CloseableHttpResponse clientResponse;
			try {
				clientResponse = httpclient.execute(httppost);
			} catch (ClientProtocolException e) {
				logger.error(ExceptionUtils.getStackTrace(e));
				logger.error("Merrill Interaction Failed as Merill server protocol is different from ours");
				merrillErrors.add(new GenericException(ErrorCodes.NMC_BUILD__MERILL_SYSTEM_DOWN));
				return merrillErrors;
			} catch (IOException e) {
				logger.error(ExceptionUtils.getStackTrace(e));
				logger.error("Merrill Interaction Failed as Merill could not be reached at this time due to IO Exception(Auth/Malformed)..");
				merrillErrors.add(new GenericException(ErrorCodes.NMC_BUILD__MERILL_SYSTEM_DOWN));
				
				return merrillErrors;
			}
			catch (Exception e) {
				logger.error(ExceptionUtils.getStackTrace(e));
				logger.error("Merrill Interaction Failed as Merill could not be reached at this time due to time-out/Unavailablity");
				merrillErrors.add(new GenericException(ErrorCodes.NMC_BUILD__MERILL_SYSTEM_DOWN));
				return merrillErrors;
			}
            try {
            	if (logger.isDebugEnabled()) {
            		logger.debug("----------------------------------------");
            		logger.debug("-----Merill Hit is completed with below response-------");
            		logger.debug("Status recieved: " + clientResponse.getStatusLine());
            	}
                Header[] headers = clientResponse.getAllHeaders();
                if (logger.isDebugEnabled()) {
                	logger.debug("-----Header received-------");
                }	
                for (Header header : headers) {
                	logger.info(header.getName() + " = " + header.getValue());
                }
                HttpEntity resEntity = clientResponse.getEntity();
                if (resEntity != null) {
                    if (resEntity.getContentType() != null && logger.isDebugEnabled()) {
                        logger.debug("Response content type: " + resEntity.getContentType().getValue());
                        
                    }
                    if (logger.isDebugEnabled()) {
                    	logger.debug("Response content length: " + resEntity.getContentLength());
                    }
                    if (resEntity.getContentLength() > 0){
						resEntity.writeTo(System.out);
						  if (logger.isDebugEnabled()) {
							  	logger.debug("Response is present and being written as the response");
						  }
                    }
                }
                try {
                	merrillErrors = prepareResponse(httppost, request, response, clientResponse, true);
    			} catch (ServletException e) {
    				logger.error(ExceptionUtils.getStackTrace(e));
    				logger.error("Problem while reading Http Client reponse returned from Merrill ");
    				merrillErrors.add(new GenericException(ErrorCodes.NMC_BUILD__MERILL_SYSTEM_DOWN));
    				return merrillErrors;
    			}
                EntityUtils.consume(resEntity);
            } catch (IOException e) {
            	logger.error(ExceptionUtils.getStackTrace(e));	
				logger.error("Problem while reading Http Client reponse returned from Merrill ");
				merrillErrors.add(new GenericException(ErrorCodes.NMC_BUILD__MERILL_SYSTEM_DOWN));
				return merrillErrors;
			} finally {
                try {
                	clientResponse.close();
				} catch (IOException e) {
					logger.error(ExceptionUtils.getStackTrace(e));
					logger.error("Problem while reading Http Client reponse returned from Merrill ");
    				merrillErrors.add(new GenericException(ErrorCodes.NMC_BUILD__MERILL_SYSTEM_DOWN));
    				return merrillErrors;
				}
            }
			
        } finally {
            try {
				httpclient.close();
			} catch (IOException e) {
				logger.error(ExceptionUtils.getStackTrace(e));
				logger.error("Merill Interaction: Http Client recieved could not be closed");
				merrillErrors.add(new GenericException(ErrorCodes.NMC_BUILD__MERILL_SYSTEM_DOWN));
				return merrillErrors;
			}
        }
    
	return merrillErrors;
		
	}
	/**
	 * Method to create file object for merge notice document and address file
	 * @return
	 * @throws SystemException 
	 */
	public static File generateFile(byte[] document,String fileName) throws SystemException{
		File someFile  =  null;
		try {
			 someFile = new File(fileName);
			FileOutputStream fos = new FileOutputStream(someFile);
			fos.write(document);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			throw new SystemException(e,  "Problem with merged byte array and csv");
		} catch (IOException e) {
			throw new SystemException(e,  "Problem with merged byte array and csv");
		}
		return someFile;
	}
	
	/**
	 * Handle the response, set back the response to 
	 * @param method
	 * @param request
	 * @param response
	 * @param clientResponse
	 * @param isCopy
	 * @return
	 * @throws ServletException
	 */
	@SuppressWarnings("unchecked")
	public static Collection prepareResponse(HttpRequestBase method, HttpServletRequest request, HttpServletResponse response, HttpResponse clientResponse, boolean isCopy) throws ServletException {
		Collection merrillErrors = new ArrayList();
		try {
			int statusCode = clientResponse.getStatusLine().getStatusCode();
			
			if(statusCode == 500){
				
				Header[] headers = clientResponse.getAllHeaders();
				for (Header header : headers) {
					String headername = header.getName();
					String NMErrorCodeValue ;
					if (headername.contains("NMErrorCode")  || headername.equalsIgnoreCase("NMErrorCode") ) { 
						// Package not accepted and error code is returned from merrill
						NMErrorCodeValue = header.getValue();
						logger.error("Merrill responded with Error code for the order which was raised. NMErrorCode = "+NMErrorCodeValue+ ", Please refer the error code in the list to idenitify the actual issue.");
							
						Object[] errorCode = new String[] { NMErrorCodeValue, NMErrorCodeValue };
						merrillErrors.add(new GenericException(ErrorCodes.NMC_BUILD_TRANSFERRED_PACKAGE_IS_NOT_ACCEPTED_TO_MERILL_AND_GOT_ERROR_CODE, errorCode));
						return merrillErrors;
					}
				}
				
                logger.error("Merrill failed to process the request and result in 500 status with internal Error");                
                merrillErrors.add(new GenericException(ErrorCodes.NMC_BUILD_TRANSFERRED_PACKAGE_IS_NOT_ACCEPTED_TO_MERILL_AND_GOT_NO_ERROR_CODE));
				return merrillErrors;
			}
			if(statusCode == 401 || statusCode == 403){
				clientResponse.setHeader("WWW-Authenticate", "");
				merrillErrors.add(new GenericException(ErrorCodes.NMC_BUILD_TRANSFERRED_PACKAGE_IS_NOT_ACCEPTED_TO_MERILL_AND_GOT_NO_ERROR_CODE));
				return merrillErrors;
			}
			//response.reset();
			response.setStatus(statusCode);

			// Passed back all heads, but process cookies differently.
			Header[] headers = clientResponse.getAllHeaders();
			for (Header header : headers) {
				String headername = header.getName();

				if (headername.equalsIgnoreCase("Set-Cookie") ) { // $NON-NLS-1$
					if(forwardCookies(method, request)) {
						// If cookie, have to rewrite domain/path for browser.
						String setcookieval = header.getValue();
	
						if (setcookieval != null) {
							String thisserver = request.getServerName();
	
							String thisdomain;
							if (thisserver.indexOf('.') == -1) {
								thisdomain = "";
							}
							else {
								thisdomain = thisserver.substring(thisserver.indexOf('.'));
							}
							String domain = null;
	
							String thispath = request.getContextPath() + request.getServletPath();
							String path = null;
	
							String[][] cookparams = getCookieStrings(setcookieval);
	
							for (int j = 1; j < cookparams.length; j++) {
								if ("domain".equalsIgnoreCase(cookparams[j][0])) { // $NON-NLS-1$
									domain = cookparams[j][1];
									cookparams[j][1] = null;
								} else if ("path".equalsIgnoreCase(cookparams[j][0])) { // $NON-NLS-1$
									path = cookparams[j][1];
									cookparams[j][1] = null;
								}
							}
	
							if (domain == null) {
								domain = method.getURI().getHost();
							}
	
							// Set cookie name
							String encoded = encodeCookieNameAndPath(cookparams[0][0], path, domain);
							if(encoded!=null) {
								String newcookiename = PASSTHRUID + encoded;
	
								StringBuilder newset = new StringBuilder(newcookiename);
								newset.append('=');
								newset.append(cookparams[0][1]);
	
								for (int j = 1; j < cookparams.length; j++) {
									String settingname = cookparams[j][0];
									String settingvalue = cookparams[j][1];
									if (settingvalue != null) {
										newset.append("; ").append(settingname); // $NON-NLS-1$
										newset.append('=').append(settingvalue); // $NON-NLS-1$
									}
								}
	
								newset.append("; domain=").append(thisdomain); // $NON-NLS-1$
								newset.append("; path=").append(thispath); // $NON-NLS-1$
	
								String newsetcookieval = newset.toString();
								// this implementation of HttpServletRequest seems to have issues... setHeader works as I would
								// expect addHeader to.
								response.setHeader(headername, newsetcookieval);
							}
						}
					}
				}
				else if (!headername.equalsIgnoreCase("Transfer-Encoding")) { // $NON-NLS-1$
					String headerval = header.getValue();

					if (headername.equalsIgnoreCase("content-type")) {
						int loc = headerval.indexOf(';');
						String type;
						if (loc > 0) {
							type = headerval.substring(0, loc).trim();
						} else {
							type = headerval;
						}
						if (!isMimeTypeAllowed(type)) {
							isCopy = false;
							break;
						} else {
							response.setHeader(headername, headerval);
						}
					} else if ( (statusCode == 401 || statusCode == 403)  && headername.equalsIgnoreCase("WWW-Authenticate")) { // $NON-NLS-1$
						if (headerval.indexOf("Basic") != -1) { // $NON-NLS-1$
							String pathInfo = request.getPathInfo();
							String[] pathParts = (pathInfo.startsWith("/") ? pathInfo.substring(1) : pathInfo).split("/");
							if (pathParts.length > 1) {
								StringBuilder strb = new StringBuilder("Basic realm=\""); // $NON-NLS-1$
								strb.append(request.getContextPath());
								strb.append(request.getServletPath());
								strb.append('/');
								strb.append(pathParts[0]);
								strb.append('/');
								strb.append(pathParts[1]);
								strb.append('"');
								headerval = strb.toString();
								response.setHeader(headername, headerval);
							}
						}
						//TODO double check wether this is the success scenario
						return merrillErrors;
					} else {
						response.setHeader(headername, headerval);
					}
				}
			}

			// Need to move response body over too
			if(statusCode == HttpServletResponse.SC_NO_CONTENT || statusCode == HttpServletResponse.SC_NOT_MODIFIED) {
				response.setHeader("Content-Length", "0");
			} else if(isCopy) {
				HttpEntity entity = clientResponse.getEntity();
				InputStream inStream = entity.getContent();
				try{
					if (inStream != null) {
						OutputStream os = response.getOutputStream();
						StreamUtil.copyStream(inStream, os);
						os.flush();
						 response.getWriter();
					}else {
						response.setHeader("Content-Length", "0");
					}
				}catch (Exception e) {
					logger.error("Merrill successfully placed order and control goes to Merrill Landing page");               
				} 
				
			}
		} catch(IOException ex) {
			logger.error("Successfully transferred package to Merrill but did not get any expected error handling scenario still an exception is caught");  
			merrillErrors.add(new GenericException(ErrorCodes.NMC_BUILD_TRANSFERRED_PACKAGE_IS_NOT_ACCEPTED_TO_MERILL_AND_GOT_NO_ERROR_CODE));
			return merrillErrors;
		}
		return merrillErrors;
	}
	
	protected static boolean forwardCookies(HttpRequestBase method, HttpServletRequest request) {
		return true;
	}
	protected boolean isCookieAllowed(String cookieName) throws ServletException {
		//if(cookieName.equalsIgnoreCase("JSESSIONID")) {
		//    return false;
		//}
		return true;
	}
	protected static boolean isMimeTypeAllowed(String cookieName) throws ServletException {
		return true;
	}
	private static String[][] getCookieStrings(String set_cookie) {
		String[] pairs = set_cookie.split(";");
		String[][] ret = new String[pairs.length][];

		for (int i = 0; i < pairs.length; i++) {
			String p = pairs[i];
			ret[i] = new String[2];

			int eqloc = p.indexOf('=');
			if (eqloc < 0) {
				ret[i][0] = p;
				ret[i][1] = null;
			}
			else {
				ret[i][0] = p.substring(0, eqloc).trim();
				ret[i][1] = p.substring(eqloc + 1).trim();
			}
		}

		return ret;
	}
	protected static String encodeCookieNameAndPath(String name, String path, String domain) throws ServletException {
		try {
			String s = new StringBuilder(name)
			.append(';')
			.append(path)
			.append(';')
			.append(domain)
			.toString();
			String encoded = URLEncoder.encode(s, "UTF-8");
			return encoded;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	/**
	 * The wrapper for cookie domain and path.  These are stored in the cookie value
	 * when the cookie is passed to the browser.  So that the proxy domain and path
	 * can be used when the cookie is passed back to the browser.
	 */
	private static final String PASSTHRUID  = "JH_APP";
}
