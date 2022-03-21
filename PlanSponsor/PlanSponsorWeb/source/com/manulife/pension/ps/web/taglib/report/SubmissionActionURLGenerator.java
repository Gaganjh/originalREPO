package com.manulife.pension.ps.web.taglib.report;

import org.apache.commons.lang3.time.FastDateFormat;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingDetails;
import com.manulife.pension.ps.service.report.census.valueobject.DeferralDetails;
import com.manulife.pension.ps.service.report.census.valueobject.Details;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressHistory;
import com.manulife.pension.ps.service.submission.valueobject.CensusSubmissionItem;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.census.CensusConstants;
import com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper;

/**
 * @author parkand
 *
 */
public class SubmissionActionURLGenerator {

	private static final String PARAM_START = "?";
	private static final String PARAM_SUBMISSION_NUMBER = "subNo=";
	private static final String PARAM_TYPE = "type=";
	private static final String PARAM_STATUS = "status=";
	private static final String PARAM_SEPARATOR = "&";
    private static final String PARAM_SOURCE = "source=";
    private static final String PARAM_ASOF_DATE = "asOfDate=";
	
	private static final String PARAM_CONTRACT_NUMBER = Constants.CONTRACT_NUMBER_PARAMETER + "=";
	private static final String PARAM_PROFILE_ID = Constants.PROFILE_ID_PARAMETER + "=";
	private static final String PARAM_SUBMISSION_ID = Constants.SUBMISSION_ID_PARAMETER + "=";
	private static final String PARAM_EMPLOYER_DESIGNATED_ID = Constants.EMPLOYER_DESIGNATED_ID_PARAMETER + "=";
    private static final String PARAM_SEQUENCE_NUMBER = Constants.SEQUENCE_NUMBER_PARAMETER + "=";
	
	private static final String VIEW_PAYMENT_ACTION = "/do/tools/viewPayment/";
	private static final String VIEW_CONTRIBUTION_ACTION = "/do/tools/viewContribution/";
	private static final String VIEW_CENSUS_DETAILS_ACTION = "/do/tools/viewCensusDetails/";
  
	/**
	 * viewContributionNew contains contribution details with make a payment button
     */	
	private static final String VIEW_CONTRIBUTION_ACTION_NEW = "/do/tools/viewContributionNew/";
	
	private static final String VIEW_SUBMISSION_ACTION = "/do/tools/viewSubmission/";
	
	private static final String VIEW_MODE = "mode=v";
	private static final String EDIT_MODE = "mode=e";
	
	private static final String DELETE_ACTION = "/do/tools/submissionDelete/";
	
	private static final String COPY_CONTRIBUTION_ACTION = "/do/tools/copyContribution/";
	private static final String EDIT_CONTRIBUTION_ACTION = "/do/tools/editContribution/";
	
    
    private static final String VIEW_VESTING_ACTION = "/do/tools/viewVesting/";
    private static final String COPY_VESTING_ACTION = "/do/tools/copyVesting/";
    private static final String EDIT_VESTING_ACTION = "/do/tools/editVesting/";
	
	private static final String SUBMITTED_CENSUS_EDIT_ACTION = "/do/tools/uploadCensusErrorCorrection/";
	
    private static final String CSDB_CENSUS_VIEW_ACTION = "/do/census/viewEmployeeSnapshot/";
    private static final String CSDB_CENSUS_EDIT_ACTION = "/do/census/editEmployeeSnapshot/";
    public static final String RESET_PWD_EMAIL_ACTION = "/do/pwdemail/ResetPassword/";
    private static final String CSDB_CENSUS_VESTING_INFO_ACTION = "/do/census/viewVestingInformation/";
    private static final String PARTICIPANT_TRANSACTION_HISTORY_ACTION = "/do/transaction/participantTransactionHistory/";
    public static final String ELIGIBILITY_INFO_ACTION = "/do/census/eligibilityInformation/";
    
    private static final String VIEW_LONG_TERM_PART_TIME_ACTION = "/do/tools/viewLongTermPartTimeDetails/";
    private static final String EDIT_LONG_TERM_PART_TIME_ACTION = "/do/tools/editLongTermPartTimeDetails/";
    
  //SimpleDateFormat is converted to FastDateFormat to make it thread safe
   public static final FastDateFormat  DATE_FORMATTER = FastDateFormat.getInstance("MM/dd/yyyy");

	public static String getViewURL(SubmissionHistoryItem item) {
		String type = item.getType();
       
		StringBuffer url = new StringBuffer();
		StringBuffer submissionNumberParameter = new StringBuffer(PARAM_START);
		submissionNumberParameter.append(PARAM_SUBMISSION_NUMBER);
		submissionNumberParameter.append(item.getSubmissionId());
		if (type == null) {
			url.append(VIEW_SUBMISSION_ACTION);
			url.append(submissionNumberParameter);
		} else if (type.equals(GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR) || type.equals(GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_TRANSFER)) {
			/**
			 * check systemStatus and is only view option to filter 
			 * VIEW_CONTRIBUTION_ACTION_NEW
			 */
			if(Constants.AWAITING_PAYMENT_STATUS.equals(item.getSystemStatus())) {	
				item.setSubmitterName(Constants.AWAITING_PAYMENT_PAYROLL_COMPANY);
				url.append(VIEW_CONTRIBUTION_ACTION_NEW);
			} else {
				url.append(VIEW_CONTRIBUTION_ACTION);
            }
			url.append(submissionNumberParameter.toString());
		} else if (type.equals(GFTUploadDetail.SUBMISSION_TYPE_PAYMENT)) {
			url.append(VIEW_PAYMENT_ACTION);
			url.append(submissionNumberParameter.toString());
		} else if (type.equals(GFTUploadDetail.SUBMISSION_TYPE_ADDRESS) ||
                   type.equals(GFTUploadDetail.SUBMISSION_TYPE_CENSUS)) {
			url.append(VIEW_CENSUS_DETAILS_ACTION);
			url.append(submissionNumberParameter.toString());
			url.append(PARAM_SEPARATOR);
			url.append(VIEW_MODE);
        } else if (type.equals(GFTUploadDetail.SUBMISSION_TYPE_VESTING)) {
            url.append(VIEW_VESTING_ACTION);
            url.append(submissionNumberParameter.toString());
		} else if(type.equals(SubmissionHistoryItemActionHelper.SUBMISSION_TYPE_LTPT)) {
			url.append(VIEW_LONG_TERM_PART_TIME_ACTION);
            url.append(submissionNumberParameter.toString());
		} else {
			url.append(VIEW_SUBMISSION_ACTION);
			url.append(submissionNumberParameter.toString());
		}
		return url.toString();
	}
	
	public static String getDeleteURL(SubmissionHistoryItem item) {
		StringBuffer url = new StringBuffer(DELETE_ACTION);
		url.append(PARAM_START);
		url.append(PARAM_SUBMISSION_NUMBER);
		url.append(item.getSubmissionId());
		url.append(PARAM_SEPARATOR);
		url.append(PARAM_TYPE);
		url.append(item.getType());
		url.append(PARAM_SEPARATOR);
		url.append(PARAM_STATUS);
		url.append(item.getSystemStatus());
		return url.toString();
	}
	
	public static String getCopyURL(SubmissionHistoryItem item) {
        String type = item.getType();
        StringBuffer url = new StringBuffer();
        if (type.equals(GFTUploadDetail.SUBMISSION_TYPE_VESTING)) {
            url.append(COPY_VESTING_ACTION);
        } else {
            url.append(COPY_CONTRIBUTION_ACTION);
        }
		url.append(PARAM_START);
		url.append(PARAM_SUBMISSION_NUMBER);
		url.append(item.getSubmissionId());
		return url.toString();
	}
	
	public static String getEditURL(SubmissionHistoryItem item) {
		String type = item.getType();
		StringBuffer url = new StringBuffer();
		StringBuffer submissionNumberParameter = new StringBuffer(PARAM_START);
		submissionNumberParameter.append(PARAM_SUBMISSION_NUMBER);
		submissionNumberParameter.append(item.getSubmissionId());
        if (type.equals(GFTUploadDetail.SUBMISSION_TYPE_ADDRESS) ||
            type.equals(GFTUploadDetail.SUBMISSION_TYPE_CENSUS)) {
			url.append(VIEW_CENSUS_DETAILS_ACTION);
			url.append(submissionNumberParameter.toString());
			url.append(PARAM_SEPARATOR);
			url.append(EDIT_MODE);
        } else if (type.equals(GFTUploadDetail.SUBMISSION_TYPE_VESTING)) {
            url.append(EDIT_VESTING_ACTION);
            url.append(submissionNumberParameter.toString());
		} else if (type.equals(SubmissionHistoryItemActionHelper.SUBMISSION_TYPE_LTPT)) {
			url.append(EDIT_LONG_TERM_PART_TIME_ACTION);
			url.append(submissionNumberParameter.toString());
		} else {
			url.append(EDIT_CONTRIBUTION_ACTION);
			url.append(submissionNumberParameter.toString());
		}
		return url.toString();
	}

	public static String getAddressEditURL(Details item) {
 		StringBuffer url = new StringBuffer();
 		StringBuffer params = new StringBuffer(PARAM_START);
 		params.append(PARAM_PROFILE_ID);
 		params.append(item.getProfileId());
        params.append(new String("&source=" + CensusConstants.ADDRESS_SUMMARY_PAGE));
 		url.append(CSDB_CENSUS_EDIT_ACTION);
 		url.append(params.toString());
 		return url.toString();
 	}
    
    public static String getAddressViewURL(Details item) {
        StringBuffer url = new StringBuffer();
        StringBuffer params = new StringBuffer(PARAM_START);
        params.append(PARAM_PROFILE_ID);
        params.append(item.getProfileId());
        params.append(new String("&source=" + CensusConstants.ADDRESS_SUMMARY_PAGE));
        url.append(CSDB_CENSUS_VIEW_ACTION);
        url.append(params.toString());
        return url.toString();
    }
    
    public static String getCensusSummaryViewURL(Details item) {
        StringBuffer url = new StringBuffer();
        StringBuffer params = new StringBuffer(PARAM_START);
        params.append(PARAM_PROFILE_ID);
        params.append(item.getProfileId());
        if(item instanceof EmployeeSummaryDetails) {
            params.append(new String("&source=" + CensusConstants.ELIGIBILITY_SUMMARY_PAGE));
        } if(item instanceof DeferralDetails) {
            params.append(new String("&source=" + CensusConstants.DEFERRAL_PAGE));
        } else {
            params.append(new String("&source=" + CensusConstants.CENSUS_SUMMARY_PAGE));
        }
            
        url.append(CSDB_CENSUS_VIEW_ACTION);
        url.append(params.toString());
        return url.toString();
    }
    
    public static String getCensusSummaryEditURL(Details item) {
        StringBuffer url = new StringBuffer();
        StringBuffer params = new StringBuffer(PARAM_START);
        params.append(PARAM_PROFILE_ID);
        params.append(item.getProfileId());
        if(item instanceof EmployeeSummaryDetails) {
            params.append(new String("&source=" + CensusConstants.ELIGIBILITY_SUMMARY_PAGE));
        } if(item instanceof DeferralDetails) {
            params.append(new String("&source=" + CensusConstants.DEFERRAL_PAGE));
        } else {
            params.append(new String("&source=" + CensusConstants.CENSUS_SUMMARY_PAGE));
        }
        
        url.append(CSDB_CENSUS_EDIT_ACTION);
        url.append(params.toString());
        return url.toString();
    }
    
    public static String getEligibilityInformationURL(Details item) {
        StringBuffer url = new StringBuffer();
        StringBuffer params = new StringBuffer(PARAM_START);
        params.append(PARAM_PROFILE_ID);
        params.append(item.getProfileId());
        url.append(ELIGIBILITY_INFO_ACTION);
        url.append(params.toString());
        return url.toString();
    }
    
    public static String getCensusVestingViewURL(Details item) {
        StringBuffer url = new StringBuffer();
        StringBuffer params = new StringBuffer(PARAM_START);
        params.append(PARAM_PROFILE_ID);
        params.append(item.getProfileId());
        params.append(new String("&source=" + CensusConstants.CENSUS_VESTING_PAGE));
            
        url.append(CSDB_CENSUS_VIEW_ACTION);
        url.append(params.toString());
        return url.toString();
    }
    
    public static String getCensusVestingInformationURL(Details item) {
        CensusVestingDetails theItem  = (CensusVestingDetails) item;
        StringBuffer url = new StringBuffer();
        StringBuffer params = new StringBuffer(PARAM_START);
        params.append(PARAM_PROFILE_ID);
        params.append(item.getProfileId());
        params.append(new String(PARAM_SEPARATOR + PARAM_SOURCE + CensusConstants.CENSUS_VESTING_PAGE));
        params.append(new String(PARAM_SEPARATOR + PARAM_ASOF_DATE + DATE_FORMATTER.format(theItem.getAsOfDate())));
        url.append(CSDB_CENSUS_VESTING_INFO_ACTION);
        url.append(params.toString());
        return url.toString();
    }
 	
    public static String getHistoryURL(Details item) {
        StringBuffer url = new StringBuffer();
        StringBuffer params = new StringBuffer(PARAM_START);
        params.append(PARAM_PROFILE_ID);
        params.append(item.getProfileId());        
        
        url.append(PARTICIPANT_TRANSACTION_HISTORY_ACTION);
        url.append(params.toString());
        return url.toString();
    }
 	public static String getAddressEditURL(ParticipantAddressHistory item) {
		String params = new String(PARAM_START+PARAM_PROFILE_ID+item.getProfileId()+"&source=addressHistory");
		return new String(CSDB_CENSUS_EDIT_ACTION+params);
	}

	public static String getCensusEditURL(CensusSubmissionItem item) {
		StringBuffer url = new StringBuffer();
		StringBuffer params = new StringBuffer(PARAM_START);
		params.append(PARAM_CONTRACT_NUMBER);
		params.append(item.getContractNumber());
		params.append(PARAM_SEPARATOR);
		params.append(PARAM_SUBMISSION_ID);
		params.append(item.getSubmissionId());
        params.append(PARAM_SEPARATOR);
        params.append(PARAM_SEQUENCE_NUMBER);
        params.append(item.getSequenceNumber());
		params.append(PARAM_SEPARATOR);
        params.append(PARAM_EMPLOYER_DESIGNATED_ID);
        params.append(item.getEmpId());
		url.append(SUBMITTED_CENSUS_EDIT_ACTION);
		url.append(params.toString());
		return url.toString();
	}
	
	//Reset pin url getResetPasswordURL
	 public static String getResetPasswordURL(Details item) {
	        StringBuffer url = new StringBuffer();
	        StringBuffer params = new StringBuffer(PARAM_START);
	        params.append(PARAM_PROFILE_ID);
	        params.append(item.getProfileId());
	        if(item instanceof EmployeeSummaryDetails) {
	            params.append(new String("&source=" + CensusConstants.ELIGIBILITY_SUMMARY_PAGE));
	        } if(item instanceof DeferralDetails) {
	            params.append(new String("&source=" + CensusConstants.DEFERRAL_PAGE));
	        } else {
	            params.append(new String("&source=" + CensusConstants.CENSUS_SUMMARY_PAGE));
	        }
	        
	        url.append(RESET_PWD_EMAIL_ACTION);
	        url.append(params.toString());
	        return url.toString();
	    }


}