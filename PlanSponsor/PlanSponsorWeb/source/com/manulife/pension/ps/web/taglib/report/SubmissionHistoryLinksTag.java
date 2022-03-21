package com.manulife.pension.ps.web.taglib.report;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.springframework.web.util.UrlPathHelper;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.taglib.report.AbstractReportTag;
import com.manulife.pension.platform.web.taglib.util.TagUtils;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.eligibility.util.LongTermPartTimeAssessmentUtil;

/**
 * @author parkand
 *
 */
public class SubmissionHistoryLinksTag extends AbstractReportTag {

	private String profile;
	
	private static final String PAGE_SCOPE = "page";
	
	// HTML constants
	private static final String TITLE = "<img src=\"/assets/unmanaged/images/navigation.gif\">";
	
	private static final String ARROW = "<img src=\"/assets/unmanaged/images/s.gif\">&nbsp;";
	
	private static final String MAKE_CONTRIBUTION_IDENTIFIER = "makeContribution";
	private static final String MAKE_CONTRIBUTION_PATH = "/do/home/makeContribution/";
	private static final String HISTORY_REPORT_TITLE = "View submission history report";
	private static final String HISTORY_REPORT_LINK = "/do/tools/submissionHistory/";
	private static final String SUBMISSION_HISTORY_LINK_IDENTIFIER = "submissionHistory";
	
	private static final String UPLOAD_CONTRIBUTION_FILE_TITLE = "Upload a contribution file";
	private static final String UPLOAD_CONTRIBUTION_FILE_LINK = "/do/tools/submissionUpload/";
    
    private static final String UPLOAD_CENSUS_FILE_TITLE = "Upload a census file";
    private static final String UPLOAD_CENSUS_FILE_LINK = "/do/tools/censusUpload/";
    
    private static final String UPLOAD_VESTING_FILE_TITLE = "Upload a vesting file";
    private static final String UPLOAD_VESTING_FILE_LINK = "/do/tools/vestingUpload/";

	private static final String CREATE_NEW_CONTRIBUTION_SUBMISSION_TITLE = "Create a new contribution submission";
	private static final String CREATE_NEW_CONTRIBUTION_SUBMISSION_LINK = "/do/tools/createContribution/";
	private static final String EDIT_CONTRIBUTION_SUBMISSION_URL = "/do/tools/editContribution/";

	private static final String MAKE_A_PAYMENT_TITLE = "Make a payment";
	private static final String MAKE_A_PAYMENT_LINK = "/do/tools/makePayment/";

	private static final String VIEW_DOCUMENT_UPLOAD_HISTORY_TITLE = "View Document History";
	private static final String VIEW_DOCUMENT_UPLOAD_HISTORY_LINK = "/do/tools/viewDocumentUploadHistory/";

	/**
	 * Added for Send Conversion File
	 */
	private static final String UPLOAD_CONVERSION_FILE_TITLE = "Send a conversion file";
	private static final String UPLOAD_CONVERSION_FILE_LINK = "/do/tools/uploadConversionFile/";
	
	private static final String COPY_MY_LAST_SUBMISSION_TITLE = "Copy my last contribution";
	private static final String COPY_MY_LAST_SUBMISSION_LINK = "/do/tools/copyLastContribution/";
	
	/**
	 * Added for LongTermPartTime file upload.
	 */
	private static final String UPLOAD_LTPT_INFORMATION_FILE_TITLE = "Upload LTPT information file";
	private static final String UPLOAD_LTPT_INFORMATION_FILE_LINK = "/do/tools/uploadLongTermPartTimeInfo/";
	
	/**
	 * Added for Combo file upload.
	 */
	private static final String UPLOAD_COMBO_FILE_LINK = "/do/tools/submissionComboUpload/";	//CL 105689
	private static final String UPLOAD_COMBO_FILE_TITLE = "Upload a combination file";				//CL 105689
	
	private static final String CONFIRMATION_ACTION = "action=confirm";

	//"<span class=\"highlightBold\">Submission history report</span>";
	private static final String START_SPAN = "<span class=\"highlightBold\">";
	private static final String END_SPAN = "</span>";
	private static final String START_HREF = "<a href=\"";
	private static final String STOP_HREF = "\">";
	private static final String END_HREF = "</a>";
	
	private static final String NEWLINE = "\n";
	private static final String BREAK = "<br/>";
	
	public int doStartTag() throws JspException {
		UserProfile userProfile = (UserProfile)TagUtils.getInstance().lookup(pageContext,profile,PAGE_SCOPE);
		
		// read the page name
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
	    
		try {
		    String action = (String)request.getParameter("action");
		    String task = (String)request.getParameter("task");
		    
			String path =new UrlPathHelper().getOriginatingRequestUri(request);
			//	RequestUtils.requestURL((HttpServletRequest)pageContext.getRequest()).getPath();
			StringBuffer buffer = new StringBuffer();
			
			buffer.append(TITLE).append(BREAK).append(NEWLINE);

			// Pilot determination code
			Contract contract = userProfile.getCurrentContract();
			int contractNumber = contract.getContractNumber();
            boolean definedBenefit = contract.isDefinedBenefitContract();
            
            if (!definedBenefit) {
                buffer = createLinks(path, buffer, task, action, userProfile, contractNumber);
            } else {
                buffer = createLinksForDefinedBenefit(path, buffer, task, action);
            }
			
			pageContext.getOut().print(buffer.toString());
		} catch (MalformedURLException e) {
			throw new JspException(e.getMessage());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		} catch (SystemException e) {
			throw new JspException(e.getMessage());
		}
		return EVAL_BODY_TAG;
	}

    private StringBuffer createLinks(String path, StringBuffer buffer, String task, String action, UserProfile userProfile, int contractNumber) throws SystemException {
			
			if ( (HISTORY_REPORT_LINK.endsWith(path) && path.indexOf(SUBMISSION_HISTORY_LINK_IDENTIFIER) != -1)
					|| (MAKE_CONTRIBUTION_PATH.endsWith(path) && path.indexOf(MAKE_CONTRIBUTION_IDENTIFIER) != -1)) {
				buffer.append(ARROW).append(START_SPAN).append(HISTORY_REPORT_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);				
			} else {
				buffer.append(ARROW).append(START_HREF).append(HISTORY_REPORT_LINK).append(STOP_HREF).append(HISTORY_REPORT_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
			}
			if ( !UPLOAD_CONTRIBUTION_FILE_LINK.endsWith(path) || (action != null && action.equalsIgnoreCase("confirm"))) {
				buffer.append(ARROW).append(START_HREF).append(UPLOAD_CONTRIBUTION_FILE_LINK).append(STOP_HREF).append(UPLOAD_CONTRIBUTION_FILE_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
			} else {
				buffer.append(ARROW).append(START_SPAN).append(UPLOAD_CONTRIBUTION_FILE_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);
			}
			//For uploading Combo File Link else showing Only name of the Combo file. 
			//CL 105689 Begin
			if ( !UPLOAD_COMBO_FILE_LINK.endsWith(path)) {
				buffer.append(ARROW).append(START_HREF).append(UPLOAD_COMBO_FILE_LINK).append(STOP_HREF).append(UPLOAD_COMBO_FILE_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
			} else {
				buffer.append(ARROW).append(START_SPAN).append(UPLOAD_COMBO_FILE_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);
			}
			//CL 105689 End			
            if ( !UPLOAD_CENSUS_FILE_LINK.endsWith(path) ) {
                buffer.append(ARROW).append(START_HREF).append(UPLOAD_CENSUS_FILE_LINK).append(STOP_HREF).append(UPLOAD_CENSUS_FILE_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
            } else {
                buffer.append(ARROW).append(START_SPAN).append(UPLOAD_CENSUS_FILE_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);
            }
            
            // LTPT Template upload URL
			if (!LongTermPartTimeAssessmentUtil.getInstance().getAllMoneyTypeIdsWithPartTimeEnabled(contractNumber)
					.isEmpty()) {
				if (!UPLOAD_LTPT_INFORMATION_FILE_LINK.endsWith(path)) {
					buffer.append(ARROW).append(START_HREF).append(UPLOAD_LTPT_INFORMATION_FILE_LINK).append(STOP_HREF)
							.append(UPLOAD_LTPT_INFORMATION_FILE_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
				} else {
					buffer.append(ARROW).append(START_SPAN).append(UPLOAD_LTPT_INFORMATION_FILE_TITLE).append(END_SPAN)
							.append(BREAK).append(NEWLINE);
				}
			}
			
            if (userProfile.getRole().isInternalUser() || userProfile.getRole().isTPA()) {
                if ( !UPLOAD_VESTING_FILE_LINK.endsWith(path) ) {
                    buffer.append(ARROW).append(START_HREF).append(UPLOAD_VESTING_FILE_LINK).append(STOP_HREF).append(UPLOAD_VESTING_FILE_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
                } else {
                    buffer.append(ARROW).append(START_SPAN).append(UPLOAD_VESTING_FILE_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);
                }
            }
			if ( !EDIT_CONTRIBUTION_SUBMISSION_URL.endsWith(path) || (task != null && task.equalsIgnoreCase("confirm"))) {
				buffer.append(ARROW).append(START_HREF).append(CREATE_NEW_CONTRIBUTION_SUBMISSION_LINK).append(STOP_HREF).append(CREATE_NEW_CONTRIBUTION_SUBMISSION_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
			} else {
				buffer.append(ARROW).append(START_SPAN).append(CREATE_NEW_CONTRIBUTION_SUBMISSION_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);
			}
			if ( !MAKE_A_PAYMENT_LINK.endsWith(path) || (action != null && action.equalsIgnoreCase("confirm"))) {
				buffer.append(ARROW).append(START_HREF).append(MAKE_A_PAYMENT_LINK).append(STOP_HREF).append(MAKE_A_PAYMENT_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
			} else {
				buffer.append(ARROW).append(START_SPAN).append(MAKE_A_PAYMENT_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);
			}
			
			if ( !UPLOAD_CONVERSION_FILE_LINK.endsWith(path)) {
				buffer.append(ARROW).append(START_HREF).append(UPLOAD_CONVERSION_FILE_LINK).append(STOP_HREF).append(UPLOAD_CONVERSION_FILE_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
			} else {
				// if we are on the confirmation page, the Send a conversion file should be a clickable link
				if (action != null && "confirm".equalsIgnoreCase(action)) {
					buffer.append(ARROW).append(START_HREF).append(UPLOAD_CONVERSION_FILE_LINK).append(STOP_HREF).append(UPLOAD_CONVERSION_FILE_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
				} else {
					buffer.append(ARROW).append(START_SPAN).append(UPLOAD_CONVERSION_FILE_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);
				}
			}
			//based on truck code commented the below section
			/*if ( !VIEW_DOCUMENT_UPLOAD_HISTORY_LINK.endsWith(path)) {
				buffer.append(ARROW).append(START_HREF).append(VIEW_DOCUMENT_UPLOAD_HISTORY_LINK).append(STOP_HREF).append(VIEW_DOCUMENT_UPLOAD_HISTORY_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
			} else {
				// if we are on the confirmation page, the Send a miscellaneous file should be a clickable link
				if (action != null && "confirm".equalsIgnoreCase(action)) {
					buffer.append(ARROW).append(START_HREF).append(VIEW_DOCUMENT_UPLOAD_HISTORY_LINK).append(STOP_HREF).append(VIEW_DOCUMENT_UPLOAD_HISTORY_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
				} else {
					buffer.append(ARROW).append(START_SPAN).append(VIEW_DOCUMENT_UPLOAD_HISTORY_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);
				}
			}*/
			
			if ( !COPY_MY_LAST_SUBMISSION_LINK.endsWith(path)) {
				buffer.append(ARROW).append(START_HREF).append(COPY_MY_LAST_SUBMISSION_LINK).append(STOP_HREF).append(COPY_MY_LAST_SUBMISSION_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
			} else {
				buffer.append(ARROW).append(START_SPAN).append(COPY_MY_LAST_SUBMISSION_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);
			}
        return buffer;
		}

    private StringBuffer createLinksForDefinedBenefit(String path, StringBuffer buffer, String task, String action) {

        if ( !HISTORY_REPORT_LINK.endsWith(path) && path.indexOf(SUBMISSION_HISTORY_LINK_IDENTIFIER) == -1 ) {
            buffer.append(ARROW).append(START_HREF).append(HISTORY_REPORT_LINK).append(STOP_HREF).append(HISTORY_REPORT_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
        } else {
            buffer.append(ARROW).append(START_SPAN).append(HISTORY_REPORT_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);
        }
/*        if ( !UPLOAD_CONTRIBUTION_FILE_LINK.endsWith(path) || (action != null && action.equalsIgnoreCase("confirm"))) {
            buffer.append(ARROW).append(START_HREF).append(UPLOAD_CONTRIBUTION_FILE_LINK).append(STOP_HREF).append(UPLOAD_CONTRIBUTION_FILE_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
        } else {
            buffer.append(ARROW).append(START_SPAN).append(UPLOAD_CONTRIBUTION_FILE_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);
        }
        if ( !UPLOAD_EMPLOYEE_INFO_LINK.endsWith(path) ) {
            buffer.append(ARROW).append(START_HREF).append(UPLOAD_EMPLOYEE_INFO_LINK).append(STOP_HREF).append(UPLOAD_EMPLOYEE_INFO_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
        } else {
            buffer.append(ARROW).append(START_SPAN).append(UPLOAD_EMPLOYEE_INFO_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);
        }
*/        
        if ( !EDIT_CONTRIBUTION_SUBMISSION_URL.endsWith(path) || (task != null && task.equalsIgnoreCase("confirm"))) {
            buffer.append(ARROW).append(START_HREF).append(CREATE_NEW_CONTRIBUTION_SUBMISSION_LINK).append(STOP_HREF).append(CREATE_NEW_CONTRIBUTION_SUBMISSION_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
        } else {
            buffer.append(ARROW).append(START_SPAN).append(CREATE_NEW_CONTRIBUTION_SUBMISSION_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);
        }
        if ( !MAKE_A_PAYMENT_LINK.endsWith(path) || (action != null && action.equalsIgnoreCase("confirm"))) {
            buffer.append(ARROW).append(START_HREF).append(MAKE_A_PAYMENT_LINK).append(STOP_HREF).append(MAKE_A_PAYMENT_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
        } else {
            buffer.append(ARROW).append(START_SPAN).append(MAKE_A_PAYMENT_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);
        }
/*        if ( !UPLOAD_MISC_FILE_LINK.endsWith(path)) {
            buffer.append(ARROW).append(START_HREF).append(UPLOAD_MISC_FILE_LINK).append(STOP_HREF).append(UPLOAD_MISC_FILE_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
        } else {
            // if we are on the confirmation page, the Send a miscellaneous file should be a clickable link
            if (action != null && "confirm".equalsIgnoreCase(action)) {
                buffer.append(ARROW).append(START_HREF).append(UPLOAD_MISC_FILE_LINK).append(STOP_HREF).append(UPLOAD_MISC_FILE_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
            } else {
                buffer.append(ARROW).append(START_SPAN).append(UPLOAD_MISC_FILE_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);
            }
        }
*/
        if ( !COPY_MY_LAST_SUBMISSION_LINK.endsWith(path)) {
            buffer.append(ARROW).append(START_HREF).append(COPY_MY_LAST_SUBMISSION_LINK).append(STOP_HREF).append(COPY_MY_LAST_SUBMISSION_TITLE).append(END_HREF).append(BREAK).append(NEWLINE);
        } else {
            buffer.append(ARROW).append(START_SPAN).append(COPY_MY_LAST_SUBMISSION_TITLE).append(END_SPAN).append(BREAK).append(NEWLINE);
	}
        return buffer;
    }
	/**
	 * @return Returns the profile.
	 */
	public String getProfile() {
		return profile;
	}

	/**
	 * @param profile The profile to set.
	 */
	public void setProfile(String profile) {
		this.profile = profile;
	}
}