package com.manulife.pension.ps.web.withdrawal;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.manulife.pension.ps.web.withdrawal.util.WithdrawalRequestAWDCreateWorkItem;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.util.email.BodyPart;

public class WithdrawalSubmissionToAWD {
	private static final Logger logger = Logger.getLogger(WithdrawalSubmissionToAWD.class);
	
	 /**
     * Called to send withdrawal request details of successfully STP'ed submissions to AWD via API call .
     * 
     * @param WithdrawalRequest withdrawalRequest
     * @param String controlBlock
     */
    public String sendSubmissionToAWD(WithdrawalRequest withdrawalRequest, String transactionId){
        String file="";
        StringBuffer awdWorkItemDetails = new StringBuffer();
        try{
            ArrayList<BodyPart> bodyPartsOfMessage = WithdrawalServiceDelegate.getInstance().getReadyForEntryEmailContent(withdrawalRequest);
            for(int i = 0; i < bodyPartsOfMessage.size(); i ++) {
                BodyPart bodyPartOfMessage = bodyPartsOfMessage.get(i);
                if(bodyPartOfMessage.getClob() != null) {
                    file=bodyPartOfMessage.getClob();
                }
            }
            HashMap<String,String> awdWorkITemValue =new WithdrawalRequestAWDCreateWorkItem().sendOWDetailsToAwd(withdrawalRequest.getParticipantSSN(),String.valueOf(withdrawalRequest.getContractId()), 
                    transactionId, String.valueOf(withdrawalRequest.getSubmissionId()), file);
            if(null!=awdWorkITemValue){
            	if(null!=awdWorkITemValue.get("workItemURL") && null!=awdWorkITemValue.get("workItemId")){
            		awdWorkItemDetails.append(" AWD_WIURL= "+awdWorkITemValue.get("workItemURL").toString()+", AWD_WorkItemID= "+awdWorkITemValue.get("workItemId").toString());
            	}
            	else{
            		awdWorkItemDetails.append("The AWD API call failed: Error Code= "+
            				awdWorkITemValue.get("errorCode").toString()+", Error Message = "+awdWorkITemValue.get("errorMessage").toString());
            	}
            }
        }
        catch (Exception e) {
            final String message = "@@@@@ Exception in sending Items to AWD via API call(sendSubmissionToAWD)"
                    + Thread.currentThread().toString()+":error message is:"+e.getMessage();
            logger.error(message);
            e.printStackTrace();
        }
       return awdWorkItemDetails.toString();
    }

}
