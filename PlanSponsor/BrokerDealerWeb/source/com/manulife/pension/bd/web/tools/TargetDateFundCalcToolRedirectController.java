
package com.manulife.pension.bd.web.tools;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.controller.BDController;
import com.manulife.pension.bd.web.ikit.GetIKitFundInfoForm;
import com.manulife.pension.bd.web.validation.pentest.BDValidatorFWFail;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.platform.web.controller.BaseController;
import com.manulife.pension.util.BaseEnvironment;

/**
 * Redirects to the Target Date Fund Calculation Tool
 * 
 * 
 */
@Controller
@RequestMapping( value ="/TargetDateTool")

public class TargetDateFundCalcToolRedirectController extends BDController {

	public static HashMap<String,String> forwards = new HashMap<String,String>();
	static{
		forwards.put("fail","/do/home/");
	}

    private static final String RESPONSE_TYPE = "text/html";

    private static final String LOCATION = "Location";
    
    private static final String ALGORITHM = "AES/CBC/PKCS5PADDING"; 
    private static final String URL_TO_TOOL = "https://www.JHTargetDateTool.com/Login?auth=";    

   

    /**
     * Constructor.
     */
    public TargetDateFundCalcToolRedirectController() {
        super(TargetDateFundCalcToolRedirectController.class);
    }

    /**
     * @see BaseController#doExecute()
     */
    @RequestMapping( method =  {RequestMethod.POST,RequestMethod.GET})
	public String doExecute(@Valid @ModelAttribute("getIKitFundInfoForm") GetIKitFundInfoForm actionForm, BindingResult bindingResult,HttpServletRequest request,HttpServletResponse response) 
	throws IOException,ServletException, SystemException {
		if(bindingResult.hasErrors()){
	   		String errDirect =(String) request.getSession().getAttribute(CommonConstants.ERROR_RDRCT);
	   		if(errDirect!=null){
	   			request.getSession().removeAttribute(CommonConstants.ERROR_RDRCT);
	   			forwards.get("fail");//if input forward not //available, provided default
	   		}
	   	}
   
        if (logger.isDebugEnabled()) {
            logger.debug("Entry -> Target Date Fund Calculation Tool Redirect Action");
        }

        response.setContentType(RESPONSE_TYPE);
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        String url = createToolURL();
        response.setHeader(LOCATION, url);
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.

        if (logger.isDebugEnabled()) {
            logger.debug("Exit <- Target Date Fund Calculation Tool Redirect Action");
        }
        return null;
	}

	/**
     * Creates a URL to the new tool where URL_TO_TOOL is the base.
     * Adds an Base64 encoded, AES encrypted auth token to the end of the URL.
     * The token format is:
     * 16 bytes AES Initialization Vector + Encrypted String.  
     * (It is OK to share the random IV vector for AES)
     * The encrypted String is of the format:
     *              long time in milliseconds representing current timestamp.
     *              followed by a | delimiter
     *              followed by a random string of padding characters.
     *              
     * To reverse the token into a usable format the vendor must do the following:
     *      1) Get the auth token
     *      2) Base64Decode it.
     *      3) Take the first 16 bytes as the AES initialization Vector (IV).
     *      4) Take the remainder of the bytes as the encrypted value.
     *      5) Use the IV and the AES_ENCRYPTION_KEY key to decrypt the encrypted value
     *      6) Take the first part of the string up to the first occurence of 
     *          the pipe | character and convert to a "long" millisecond timestamp.
     *      7) Cast the timestamp as a date, and ensure the date is within an acceptable time 
     * (like 90 seconds).
     *          If valid timestamp, then vendor code will authenticate the user and allow them access.
     *          If not valid timestamp then the vendor will not allow the user in and will display an error.
     * @return url to vendor site, complete with token.
	 * @throws SystemException 
     */
	public String createToolURL() throws SystemException {
		try {

			// Create a Date Token padded with a random string of characters.
			String dateToken = String.valueOf((new Date()).getTime());
			String randomPaddingString = (new BigInteger(130,
					new SecureRandom()).toString(32));
			String tokenToBeEncrypted = dateToken + "|" + randomPaddingString;

			SecureRandom random = new SecureRandom();

			BaseEnvironment environment = new BaseEnvironment();
			String tdfCalcToolAccessKey = environment.getNamingVariable(
					BDConstants.TDF_TOOL_AUTHUNTICATION_KEY, null);
			if (StringUtils.isBlank(tdfCalcToolAccessKey)) {
				throw new IllegalArgumentException(
						"invalid value for the naming variable: "
								+ BDConstants.TDF_TOOL_AUTHUNTICATION_KEY);
			}
			SecretKeySpec secretKeySpec = new SecretKeySpec(
					tdfCalcToolAccessKey.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, random);

			// Encrypt and formulate the URL:
			byte[] encryptedBytes = cipher.doFinal(tokenToBeEncrypted
					.getBytes());
			byte[] finalBytes = new byte[encryptedBytes.length
					+ cipher.getIV().length];
			System.arraycopy(cipher.getIV(), 0, finalBytes, 0,
					cipher.getIV().length);
			System.arraycopy(encryptedBytes, 0, finalBytes,
					cipher.getIV().length, encryptedBytes.length);

			byte[] baseEncodedToken = Base64.encodeBase64(finalBytes);

			String tdfCalcToolUrl = environment.getNamingVariable(
					BDConstants.TDF_TOOL_URL, null);
			if (StringUtils.isBlank(tdfCalcToolUrl)) {
				throw new IllegalArgumentException(
						"invalid value for the naming variable: "
								+ BDConstants.TDF_TOOL_URL);
			}
			String base64EncodedAESEncryptedToken = new String(baseEncodedToken);
			String url = tdfCalcToolUrl + base64EncodedAESEncryptedToken;
			return url;
		} catch (Exception ex) {
			logger.error("Fail to encrypt the parameters", ex);
			throw new SystemException(ex, "Fail to encrypt the parameters");
		}

	}
	

	/**
	 * Validate form and request against penetration attack, prior to other validations.
	 */
	
	@Autowired
	   private BDValidatorFWFail  bdValidatorFWFail;
@InitBinder
	  public void initBinder(HttpServletRequest request,ServletRequestDataBinder  binder) {
	    binder.bind( request);
	    binder.addValidators(bdValidatorFWFail);
	}
	
}
