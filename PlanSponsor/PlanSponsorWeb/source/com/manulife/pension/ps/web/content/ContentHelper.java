package com.manulife.pension.ps.web.content;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.controller.UserProfile;

/**
 * Helper Utility class to get the content id based on the GIFL version or/and internal users 
 * @author Ramesh Babu
 * @version 1.0
 */

public class ContentHelper {

     
    private static Map<String, Integer> giflVersion03Map = new HashMap<String, Integer>(); // Map for GIFL version 3 content id's for Non Internal Users
    private static Map<String, Integer> giflVersion01And02Map = new HashMap<String, Integer>(); // Map for GIFL version 1 & 2 content id's for Non Internal Users
    
    private static Map<String, Integer> giflVersion03InternalUserMap = new HashMap<String, Integer>(); // Map for GIFL version 3 content id's for Internal Users
    private static Map<String, Integer> giflVersion01And02InternalUserMap = new HashMap<String, Integer>(); // Map for GIFL version 1 & 2 content id's for Internal Users

    private static String GIFL_FEE_FORMAT = "0.00#";
    
    
    static {
   	//Map for GIFL version 3 contents
    	giflVersion03Map.put(Constants.PSW_CP_GIFL_SECTION, ContentConstants.PSW_CP_GIFL_V3_SECTION_NON_INTERNAL_USER);
    	giflVersion03Map.put(Constants.PSW_CP_GIFL_V3_FOOTNOTE, ContentConstants.PSW_CP_GIFL_FOOTNOTE);
    	giflVersion03Map.put(Constants.PSW_BB_FOOTNOTE, ContentConstants.PSW_BB_GIFL_V3_FOOTNOTE);
    	giflVersion03Map.put(Constants.PSW_PC_GIFL_MESSAGE, ContentConstants.PSW_PC_GIFL_V3_MESSAGE);
    }
    static
    {
    	//Map for GIFL version 1 & 2 contents
    	giflVersion01And02Map.put(Constants.PSW_CP_GIFL_SECTION, ContentConstants.PSW_CP_GIFL_V1V2_SECTION_NON_INTERNAL_USER);
    	giflVersion01And02Map.put(Constants.PSW_BB_FOOTNOTE, ContentConstants.BENEFIT_BASE_FOOTNOTE);
    	giflVersion01And02Map.put(Constants.PSW_PC_GIFL_MESSAGE, ContentConstants.PERFORMANCE_CHARTING_GIFL_MESSAGE);
    }	
    static {
    	//Map for GIFL version 3 and Internal users contents
    	giflVersion03InternalUserMap.put(Constants.PSW_CP_GIFL_SECTION, ContentConstants.PSW_CP_GIFL_V3_SECTION_INTERNAL_USER);
    }
    static 
    {
    	//Map for GIFL version 1 & 2 and External users contents
    	giflVersion01And02InternalUserMap.put(Constants.PSW_CP_GIFL_SECTION, ContentConstants.PSW_CP_GIFL_V1V2_SECTION_INTERNAL_USER);
    }	
    	
    
    /**
     * Get the content id based on the GIFL version irrespective of the users whether internal or external
     * @param contentName, the key value to get the correct content id
     * @param userProfile, user profile Object which contains the GIFL version
     * @return
     */
    
    public static int getContentIdByVersion(String contentName, UserProfile userProfile){
    	
    	
    	if(Constants.GIFL_VERSION_03.equals(getGiflVersion(userProfile)))
    		return getContentId(giflVersion03Map,contentName);
    	else
    		return getContentId(giflVersion01And02Map,contentName);
    
    }
    
    
    /**
     * Get the content id based on the GIFL version and user type whether internal or external users 
     * @param contentName, the key value to get the correct content id
     * @param userProfile, user profile Object which contains the GIFL version
     * @return
     */
    
    public static int getContentId(String contentName, UserProfile userProfile){
    	
    		if(isInternalUser(userProfile)){
    			
    	    	if(Constants.GIFL_VERSION_03.equals(getGiflVersion(userProfile)))
    	    		return getContentId(giflVersion03InternalUserMap,contentName);
    	    	else
    	    		return getContentId(giflVersion01And02InternalUserMap,contentName);
    		}
    		else
    			return getContentIdByVersion(contentName,userProfile);
    }	
      
    /**
     * returns the GIFL version (G01 or G02 or G03) of that contract which is already stored in User profile Object
     * @param userProfile
     * @return the string value of GIFL version
     */
     private static String getGiflVersion(UserProfile userProfile){

    	 String giflVersion = ""; 
    	 if(userProfile!=null && userProfile.getContractProfile()!=null && userProfile.getContractProfile().getContract()!=null){
    		 giflVersion  = userProfile.getContractProfile().getContract().getGiflVersion();
    	 }
    	 return giflVersion;
     }

     /**
      * Get the content id from the respective map like giflVersion03Map or giflVersion01And)2Map or giflVersion03InternalUserMap or giflVersion01And02InternalUserMap
      * @param map
      * @param contentName
      * @returns the actual content id
      */
     private static int getContentId(Map<String,Integer> map, String contentName)
     {
    	 if (map!=null){
    		 if(map.containsKey(contentName))
    			 return map.get(contentName);
    	 }
    	 return 0;
     }
     
     
     private static boolean isInternalUser(UserProfile userProfile){
    	 
    	 if(userProfile!=null){
    		 return userProfile.isInternalUser();
    	 }
    	 return false;
     }

     /**
      * Helper to get the formatted fee value for GIFL contract
      * 
      * @param contractNum
      * @return
      * @throws SystemException
      */
 	public static String getGIFLFeePercentageDisplay(int contractNum) throws SystemException {
		BigDecimal value = ContractServiceDelegate.getInstance().getContractAnnualGIFLFeePercentage(contractNum);
		if (value == null) {
			return "";
		} else {
			return new DecimalFormat(GIFL_FEE_FORMAT).format(value);
		}
	}
}