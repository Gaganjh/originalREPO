package com.manulife.pension.ps.web.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.delegate.exception.UploadFileCannotFoundOrEmptyException;
import com.manulife.pension.ps.web.home.SearchTPAForm;
import com.manulife.pension.ps.web.home.SearchTpaValidator;
import com.manulife.pension.ps.web.tools.exception.UploadFileExceedsMaxSizeException;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.ps.web.util.ValidatorUtil;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.log.LogUtility;
@Component
public class SubmissionUploadValidator extends ValidatorUtil  implements Validator {

private static Logger logger = Logger.getLogger(SubmissionUploadValidator.class);
protected static final int MAX_FILE_NAME_LENGTH = 256;
	@Override
	public boolean supports(Class<?> clazz) {
		return SubmissionUploadForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors error) {
		Collection errors = new ArrayList();
		SubmissionUploadForm theForm = (SubmissionUploadForm) target;
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		UserProfile userProfile = SessionHelper.getUserProfile(request);
		
		
		

		if (theForm.isSendAction()) {

			//retrieve the file representation
			MultipartFile file = theForm.getUploadFile();

			//UserProfile userProfile = getUserProfile(request);
			String fname = "";

			fname = file.getName();
			//retrieve the file size
			if (file == null || file.getSize() == 0) {
				//log the error in mrl
				UploadFileCannotFoundOrEmptyException ae =new UploadFileCannotFoundOrEmptyException(
						"SubmissionUploadAction", "doValidate "," file: " +
						fname + " is empty ");
				LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,userProfile.getPrincipal().getProfileId(),userProfile.getPrincipal().getUserName(),ae);
				// display the error on the page
				errors.add(new GenericException(
						ErrorCodes.UPLOAD_FILE_EMPTY,
						new String[] { file.getName() }));
			} else if (file.getSize() > Environment.getInstance()
					.getMaxUploadFileSizeKbytes() * 1024) {
				//log the error in mrl

				UploadFileExceedsMaxSizeException ae =new UploadFileExceedsMaxSizeException(
						"SubmissionUploadAction", "doValidate "," file: " +
						fname + " exceeds max size ");
				LogUtility.logApplicationException(Constants.PS_APPLICATION_ID,userProfile.getPrincipal().getProfileId(),userProfile.getPrincipal().getUserName(),ae);
				// display the error on the page
				errors.add(new GenericException(
						ErrorCodes.UPLOAD_FILE_EXCEEDS_MAX_SIZE,
						new String[] { fname }));
				//TODO need to find spring equivalent to destroy the file file.destroy();
			} else if (fname.length() > MAX_FILE_NAME_LENGTH) {
				// display the error on the page
				errors.add(new GenericException(
						ErrorCodes.SUBMISSION_FILE_NAME_TOO_LONG,
						new String[] { file.getName() }));
			}
		}

		if (errors.size() > 0) {
			//repopulate form if any errors
			//UserProfile userProfile = getUserProfile(request);

			
		}
	
	}

}
