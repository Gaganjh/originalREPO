package com.manulife.pension.bd.web.bob;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.bob.blockOfBusiness.util.BlockOfBusinessUtility;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.delegate.ParticipantServiceDelegate;

/**
 * This is a Helper class that will be used by those Action classes which need a BobContext.
 * 
 * Mostly, the pages that require BobContext are contract-specific pages.
 * 
 * @author harlomte
 * 
 */
public class BobContextUtils {

    private static final String PARTICIPANT_ID = "participantId";
    private static final String PPT_ID = "pptId";
    private static final String PROFILE_ID = "profileId";

    /**
     * This method checks to see if any contract number is being passed in as a request parameter.
     * 
     * If yes, based on if the BobContext is already existing for the given contract number or not
     * and based on if the user has access to given contract number or not, the bobContext is either
     * created or the existing bobContext is updated with contract information of the contract
     * number passed in as request parameter.
     * 
     * @param request
     * @throws SystemException
     */
    public static void setUpBobContext(HttpServletRequest request) throws SystemException {
        String contractNum = request.getParameter(BDConstants.CONTRACT_NUMBER);
        
        setUpBobContext(contractNum, request);
    }
    
    public static void setUpBobContext(String contractNum, 
    		HttpServletRequest request) throws SystemException {
    	
    	if (StringUtils.isBlank(contractNum)) {
            return;
        }
        
        Integer contractNumber = null;
        try {
            contractNumber = Integer.valueOf(contractNum);
        } catch (NumberFormatException ne) {
            // Do nothing. contractNumber will remain null, which will cause the bobcontext to have
            // a null contract number.
        }
        
        BobContext bobContext = createBobContext(request, contractNumber);
        
        BDSessionHelper.setBobContext(request, bobContext);
    }

    /**
     * This method creates a BobContext.
     * 
     * @param request
     * @param contractNumber
     * @throws SystemException
     */
    private static BobContext createBobContext(HttpServletRequest request, Integer contractNumber)
            throws SystemException {
        // Check if BobContext is already existing.
        BobContext bobContext = BDSessionHelper.getBobContext(request);
        if (bobContext == null || bobContext.getCurrentContract() == null) {
            // Create BobContext with the contract Number.
            bobContext = new BobContext();
            bobContext.setUserProfile(BDSessionHelper.getUserProfile(request));
            bobContext.setMimickingUserProfile(BlockOfBusinessUtility
                    .getMimckingUserProfile(request));
        }
        bobContext.setCurrentContract(request, contractNumber);
        
        return bobContext;
    }

    /**
     * This method sets up the participant profile id in bob context
     * 
     * @param request
     * @throws SystemException
     */
    public static void setupProfileId(HttpServletRequest request) throws SystemException {

        BobContext bobContext = BDSessionHelper.getBobContext(request);
        String profileId = request.getParameter(PROFILE_ID);
        
        if (profileId != null) {
            bobContext.setPptProfileId(profileId);
        } else {
            String participantId = (String) ObjectUtils.defaultIfNull(request
                    .getParameter(PARTICIPANT_ID), "");
            if (participantId.length() == 0) {
                participantId = (String) ObjectUtils
                        .defaultIfNull(request.getParameter(PPT_ID), "");
            }
            if (participantId.length() > 0) {
                profileId = ParticipantServiceDelegate.getInstance().getProfileIdByParticipantId(
                        participantId, bobContext.getCurrentContract().getContractNumber());
                bobContext.setPptProfileId(profileId);
            }
        }
    }
    
    /**
     * Sets the participant gifl indicator in the bob context.
     * 
     * @param participantGiflInd
     * @param request
     */
    public static void setParticipantGiflInd(String participantGiflInd,HttpServletRequest request) {
    	BobContext bobContext = BDSessionHelper.getBobContext(request);
    	bobContext.setParticipantGiflInd(participantGiflInd);
    }
}
