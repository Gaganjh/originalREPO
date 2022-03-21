package com.manulife.pension.ps.web.messagecenter.personalization;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.service.report.notice.valueobject.UserNoticeManagerAlertVO;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.PsBaseUtil;
import com.manulife.pension.ps.web.messagecenter.util.MessageServiceFacadeFactory;
import com.manulife.pension.util.content.GenericException;
@Component
public class MCNoticePreferenceValidator  implements Validator {

private static Logger logger = Logger.getLogger(MCNoticePreferenceValidator.class);
private static FastDateFormat dateFormat = FastDateFormat.getInstance("MM/dd/yyyy", Locale.US);
	@Override
	public boolean supports(Class<?> clazz) {
		return MCNoticePreferenceForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BindingResult bindingResult = (BindingResult) errors;
		
			ArrayList<GenericException> error= new ArrayList<GenericException>();
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		//SearchTPAForm form = (SearchTPAForm) target;
		MCNoticePreferenceForm noticePreferenceFrom = (MCNoticePreferenceForm) target;
		
		String alertName = null;
		int alertId ;
		int contractId ;
		String alertDate = null;
		String alertFrequencycode = null;
		String alertTiming = null;
		Boolean alertNameExistsError = false;
		//validations for save and save&finish buttons
		if (noticePreferenceFrom .getAction().equals(Constants.SAVE_ACTION) || noticePreferenceFrom .getAction().equals(Constants.FINISH_ACTION))
		{
			int iterate=0;
			for(UserNoticeManagerAlertVO userNoticeManagerAlert:noticePreferenceFrom.getUserNoticeManagerAlertList()){
				
				alertName=StringUtils.trimToEmpty(userNoticeManagerAlert.getAlertName());
				alertId=userNoticeManagerAlert.getAlertId();
				alertDate=userNoticeManagerAlert.getStringStartDate();
				alertFrequencycode=userNoticeManagerAlert.getAlertFrequenceCode();
				alertTiming=userNoticeManagerAlert.getAlertTimingCode();
				contractId=userNoticeManagerAlert.getContractId();
				
				iterate++;
				if(noticePreferenceFrom.getUserNoticeManagerAlertList().size()==iterate)
				{
					if(alertName.isEmpty() && alertDate.isEmpty() && (alertFrequencycode.isEmpty() && alertTiming.isEmpty()))
					{
						noticePreferenceFrom.getUserNoticeManagerAlertList().remove(noticePreferenceFrom.getUserNoticeManagerAlertList().size()-1);
						break;			
					}
				}
				
				//check if alert name already exists for the new alert			
				alertNameExistsError = checkAlertNameExists(error, alertName,
						alertId, contractId, alertNameExistsError,
						noticePreferenceFrom);

				//Validations for only those properties of existing alerts which are modified by user
				if(alertId!=0){
					//Get the alert details for the existing alert
					try {
						UserNoticeManagerAlertVO existingAlertVO =	MessageServiceFacadeFactory.getInstance(request.getServletContext()).getExistingAlertDetails(alertId);
					
					//Validation for existing alert name
					if(!existingAlertVO.getAlertName().equals(alertName)){
						if(alertName.isEmpty())
						{					
							error.add(new GenericException(ErrorCodes.ALERT_NAME_MANDATORY));
							break;
						}
						//checks if the alert name contains non printable characters
						checkAlertNameWithNonPrintableCharacters(error, alertName);
						
					}
					
					//Validation for existing alert Date
					if(!existingAlertVO.getStringStartDate().equals(alertDate)){
						if( alertDate.isEmpty())
						{
							error.add(new GenericException(ErrorCodes.ALERT_DATE_MANDATORY ));
							break;
						}
						
						if(StringUtils.isNotBlank(alertDate))
						{
						
						// Validate if the date contains special characters or other than normal date format of dd/mm/yyyy
							try {
								String alertDateWithoutSlashes=null;
								Date distDate=dateFormat.parse(alertDate);
								int slashCount=0;
								for (int i=0;i<alertDate.length();i++){
									if(alertDate.charAt(i)=='/'){
										slashCount++;	
									}
								}
								if(alertDate.contains("/") && slashCount==2){
								   alertDateWithoutSlashes = alertDate.replaceAll("/", "");
								
								try {
									Integer.parseInt(alertDateWithoutSlashes);	
								}
								catch(NumberFormatException e){
									
										error.add(new GenericException(ErrorCodes.INVALID_NOTICE_DUE_DATE_FORMAT));
										break;
								}
								}
								else{
									error.add(new GenericException(ErrorCodes.INVALID_NOTICE_DUE_DATE_FORMAT));
									break;
								}
								
								Calendar cal =Calendar.getInstance();
								cal.add(Calendar.DATE, Integer.parseInt(alertTiming.trim()));
								int day =cal.get(Calendar.DATE);
								if(Integer.parseInt(alertTiming.trim())>=30)
								{
									cal.add(Calendar.MONTH,1);
									if(cal.get(Calendar.MONTH)>12)
									{
										cal.add(Calendar.YEAR,1);
									}
									String stringDateToCompare= cal.get(Calendar.MONTH) +Constants.SLASH+ day +Constants.SLASH+cal.get(Calendar.YEAR);
									Date dateToCompare=dateFormat.parse(stringDateToCompare);
									if(distDate.compareTo(dateToCompare)< 0)
									{
										error.add(new GenericException(ErrorCodes.INVALID_NOTICE_DUE_DATE));
										break;
									}

								}else{
									cal.add(Calendar.MONTH,1);
									if(cal.get(Calendar.MONTH)>12)
									{
										cal.add(Calendar.YEAR,1);
									}
									String stringDateToCompare= cal.get(Calendar.MONTH) +Constants.SLASH+ day +Constants.SLASH+cal.get(Calendar.YEAR);
									Date dateToCompare=dateFormat.parse(stringDateToCompare);
									if(distDate.compareTo(dateToCompare)< 0)
									{
										error.add(new GenericException(ErrorCodes.INVALID_NOTICE_DUE_DATE));
										break;
									}
								}

								if (! dateFormat.format(distDate).equals(alertDate)) 
								{
									error.add(new GenericException(ErrorCodes.INVALID_NOTICE_DUE_DATE_FORMAT ));
								}
							}catch(ParseException e)
							{

								error.add(new GenericException(ErrorCodes.INVALID_NOTICE_DUE_DATE_FORMAT ));
								break;
							}
							catch(NumberFormatException e)
							{

								error.add(new GenericException(ErrorCodes.INVALID_ALERT_TIMING));
								break;
							}

						}
					
					}
					
					//Validation for existing alert timing
					if(!existingAlertVO.getAlertTimingCode().equals(alertTiming)){
						if(alertTiming.isEmpty())
						{
						error.add(new GenericException(ErrorCodes.INVALID_ALERT_TIMING));
						break;
						}
					
					
					//check the range of alert timings
					try{
						
						if(!alertTiming.isEmpty())
						{			
							int alertTimeInterval = Integer.parseInt(alertTiming.trim());
							if(alertTimeInterval < Constants.TIME_INTERVAL_MIN ||  alertTimeInterval > Constants.TIME_INTERVAL_MAX  )
							{
								error.add(new GenericException(ErrorCodes.INVALID_ALERT_TIMING));
								break;
							}
						}
					}catch(NumberFormatException e)
					{
						error.add(new GenericException(ErrorCodes.INVALID_ALERT_TIMING));
						break;
					}
					}
					
					//Validation for existing alert frequency
					if(!existingAlertVO.getAlertFrequenceCode().equals(alertFrequencycode))
					 {
						if(alertFrequencycode.isEmpty())
						{
						error.add(new GenericException(ErrorCodes.ALERT_FREQUENCYCODE_MANDATORY));
						}
					 }
					
					} catch (SystemException e) {
						logger.debug("Could not retrieve alert details for the existing alert with alert id" +alertId, e);
					}
				}
				else{
				//Validations for only new alerts added by user
					
				//checks all fields not empty before saving it
				if(alertName.isEmpty() || alertDate.isEmpty() || (alertFrequencycode.isEmpty() || alertTiming.isEmpty()))
				{
					if(alertName.isEmpty())
					{					
						error.add(new GenericException(ErrorCodes.ALERT_NAME_MANDATORY));
					}
					if( alertDate.isEmpty())
					{
						error.add(new GenericException(ErrorCodes.ALERT_DATE_MANDATORY ));
					}
					if(alertFrequencycode.isEmpty())
					{
						error.add(new GenericException(ErrorCodes.ALERT_FREQUENCYCODE_MANDATORY));
					}
					if(alertTiming.isEmpty())
					{
						error.add(new GenericException(ErrorCodes.INVALID_ALERT_TIMING));
						break;
					}
				}

				//Date validations
				//-->date format
				//-->valid date (i.e if the date given by the user is at least greater then alertTiming mentioned by the user)
				if(StringUtils.isNotBlank(alertDate))
				{
				
				// Validate if the date contains special characters or other than normal date format of dd/mm/yyyy
					try {
						String alertDateWithoutSlashes=null;
						Date distDate=dateFormat.parse(alertDate);
						int slashCount=0;
						for (int i=0;i<alertDate.length();i++){
							if(alertDate.charAt(i)=='/'){
								slashCount++;	
							}
						}
						if(alertDate.contains("/") && slashCount==2){
						   alertDateWithoutSlashes = alertDate.replaceAll("/", "");
						
						try {
							Integer.parseInt(alertDateWithoutSlashes);	
						}
						catch(NumberFormatException e){
							
								error.add(new GenericException(ErrorCodes.INVALID_NOTICE_DUE_DATE_FORMAT));
								break;
						}
						}
						else{
							error.add(new GenericException(ErrorCodes.INVALID_NOTICE_DUE_DATE_FORMAT));
							break;
						}
						
						Calendar cal =Calendar.getInstance();
						cal.add(Calendar.DATE, Integer.parseInt(alertTiming.trim()));
						int day =cal.get(Calendar.DATE);
						if(Integer.parseInt(alertTiming.trim())>=30)
						{
							cal.add(Calendar.MONTH,1);
							if(cal.get(Calendar.MONTH)>12)
							{
								cal.add(Calendar.YEAR,1);
							}
							String stringDateToCompare= cal.get(Calendar.MONTH) +Constants.SLASH+ day +Constants.SLASH+cal.get(Calendar.YEAR);
							Date dateToCompare=dateFormat.parse(stringDateToCompare);
							if(distDate.compareTo(dateToCompare)< 0)
							{
								error.add(new GenericException(ErrorCodes.INVALID_NOTICE_DUE_DATE));
								break;
							}

						}else{
							cal.add(Calendar.MONTH,1);
							if(cal.get(Calendar.MONTH)>12)
							{
								cal.add(Calendar.YEAR,1);
							}
							String stringDateToCompare= cal.get(Calendar.MONTH) +Constants.SLASH+ day +Constants.SLASH+cal.get(Calendar.YEAR);
							Date dateToCompare=dateFormat.parse(stringDateToCompare);
							if(distDate.compareTo(dateToCompare)< 0)
							{
								error.add(new GenericException(ErrorCodes.INVALID_NOTICE_DUE_DATE));
								break;
							}
						}

						if (! dateFormat.format(distDate).equals(alertDate)) 
						{
							error.add(new GenericException(ErrorCodes.INVALID_NOTICE_DUE_DATE_FORMAT ));
						}
					}catch(ParseException e)
					{

						error.add(new GenericException(ErrorCodes.INVALID_NOTICE_DUE_DATE_FORMAT ));
						break;
					}
					catch(NumberFormatException e)
					{

						error.add(new GenericException(ErrorCodes.INVALID_ALERT_TIMING));
						break;
					}

				}
				// Resets the information for JSP to display.
			
				//checks if the alert name contains non printable characters
				checkAlertNameWithNonPrintableCharacters(error, alertName);

				//check the range of alert timings
				try{
					if(!alertTiming.isEmpty())
					{			
						int alertTimeInterval = Integer.parseInt(alertTiming.trim());
						if(alertTimeInterval < Constants.TIME_INTERVAL_MIN ||  alertTimeInterval > Constants.TIME_INTERVAL_MAX  )
						{
							error.add(new GenericException(ErrorCodes.INVALID_ALERT_TIMING));
							break;
						}
					}
				}catch(NumberFormatException e)
				{
					error.add(new GenericException(ErrorCodes.INVALID_ALERT_TIMING));
					break;
				}
			}   
		}
		}
		
		
		String[] errorCodes = new String[10];
		if (error.size() > 0) {
			for(GenericException errorEx :error){
				errorEx.getMessage();
				errorCodes = new String[]{Integer.toString(errorEx.getErrorCode())};
				bindingResult.addError(new ObjectError(errors
		                 .getObjectName(),errorCodes , errorEx.getParams(), errorEx.getMessage()));
				
			}
			  if(request.getSession().getAttribute(CommonConstants.ERROR_RDRCT) == null){
			    	request.getSession().setAttribute(CommonConstants.ERROR_RDRCT, CommonConstants.ERROR_PAGE);
			    }
		request.getSession().removeAttribute(PsBaseUtil.ERROR_KEY);
		request.removeAttribute(PsBaseUtil.ERROR_KEY);
		request.getSession().setAttribute(PsBaseUtil.ERROR_KEY, error);
			request.setAttribute(PsBaseUtil.ERROR_KEY, error);
	}}
	

	/**
	 * Check the AlertNameWithNonPrintableCharacters
	 * @param error
	 * @param alertName
	 */
	private void checkAlertNameWithNonPrintableCharacters(Collection error,String alertName) {
		if(!alertName.isEmpty())
		{
			for(int i=0;i<alertName.length();i++)
			{
				char c=alertName.charAt(i);
				int a=(int)c;

				if(a < Constants.ASCII_MIN || a > Constants.ASCII_MAX)
				{
					error.add(new GenericException(ErrorCodes.NON_PRINTABLE_CHARACTERS ));
					break;
				}
			}		
		}
	}

	/**
	 * Check the Given AlertName exist or not.
	 * @param error
	 * @param alertName
	 * @param alertId
	 * @param contractId
	 * @param alertNameExistsError
	 * @param noticePreferenceFrom
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Boolean checkAlertNameExists(Collection error, String alertName,
			int alertId, int contractId, Boolean alertNameExistsError,
			MCNoticePreferenceForm noticePreferenceFrom) {

		// check if the form is having two alert names repeated
			int alertExistanceCount = 0;
			for(UserNoticeManagerAlertVO internalUserNoticeManagerAlert:noticePreferenceFrom.getUserNoticeManagerAlertList()){
				if(internalUserNoticeManagerAlert.getAlertName().equals(alertName)){
					alertExistanceCount++;
				}
			}
			if(alertExistanceCount>1){
				// Below error to be used when user duplicates name in the form.
				if(!alertNameExistsError){
					error.add(new GenericException(ErrorCodes. ALERT_NAME_EXISTS));
					alertNameExistsError = true;
				}
			}
		return alertNameExistsError;
	}
}

