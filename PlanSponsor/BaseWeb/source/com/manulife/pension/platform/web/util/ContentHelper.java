package com.manulife.pension.platform.web.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.validation.ObjectError;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentType;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.content.valueobject.Message;
import com.manulife.pension.content.view.MutableMessage;
import com.manulife.pension.platform.web.content.CommonContentConstants;
import com.manulife.pension.platform.web.util.exception.GenericExceptionWithContentType;
import com.manulife.pension.util.content.GenericException;
import com.manulife.pension.util.content.MessageProvider;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;
import com.manulife.pension.validator.ValidationError;

/**
 * This helper class is used to retrieve content text.
 * 
 * @author Ramkumar
 *
 */
public class ContentHelper {
    
    /**
     * MISSING_CONTENT_TEXT.
     */
    private static final String MISSING_CONTENT_TEXT = "Missing Content";

    public static final Logger logger = Logger.getLogger(ContentHelper.class);

    /**
     * This method retrieves the content text with substitution of given parameters.
     * 
     * @param contentId
     * @param contentType
     * @param params
     * @param contentLocation
     * @return String
     */
    public static String getContentTextWithParamsSubstitution(int contentId, ContentType contentType, Location contentLocation,
            String... contentParams) {
        String text = "";
        if (contentType != null) {
            Content content = null;
            try {
                content = ContentCacheManager.getInstance().getContentById(contentId, contentType, contentLocation);
            } catch (ContentException contentException) {
                logger.error("Error occured while retrieveing CMA Content" + contentException);
            }
            if (content != null) {
                text = ContentUtility.getContentAttribute(content, CommonContentConstants.TEXT,
                        null, contentParams);
            }
        }
        return text;
    }

    /**
     * This method retrieves the content text without substitution of given parameters.
     * 
     * @param contentId
     * @param contentType
     * @param contentLocation
     * @return String
     */
    public static String getContentText(int contentId, ContentType contentType, Location contentLocation) {
        String text = "";
        if (contentType != null) {
            Content content = null;
            try {
                content = ContentCacheManager.getInstance().getContentById(contentId, contentType, contentLocation);
            } catch (ContentException contentException) {
                logger.error("Error occured while retrieveing CMA Content" + contentException);
            }
            if (content != null) {
                text = ContentUtility.getContentAttribute(content, CommonContentConstants.TEXT);
            }
        }
        return text;
    }

    /**
     * This method returns a String[] of error messages to be displayed. This method takes into
     * account, the content type of the message while retrieving the message text from CMA.
     * 
     * @param messageCollection - A Collection of Errors of type GenericExceptionWithContentType.
     * @return - A String[] of messages to be displayed to the user.
     * @throws ContentException
     */
    public static String[] getMessagesUsingContentType(
            Collection<GenericException> messageCollection)
            throws ContentException {

        ArrayList<String> errors = new ArrayList<String>();
        if (messageCollection != null) {

            for (Object messageObj : messageCollection) {
                if (messageObj != null) {
                    if (messageObj instanceof GenericExceptionWithContentType) {
                        errors.add(getMessage((GenericExceptionWithContentType) messageObj));

                    } else if (messageObj instanceof GenericException) {
                        errors.add(MessageProvider.getInstance().getMessage(
                                (GenericException) messageObj));
                        
                    } else if (messageObj instanceof ValidationError) {
                        errors.add(MessageProvider.getInstance().getMessage(
                                (GenericException) messageObj));
                    }else if (messageObj instanceof ObjectError) {
                    	String strMessage=""; 
                    	
                    	ObjectError objError = (ObjectError)messageObj;
                    	Object[] arg = objError.getArguments();
                    		if (!objError.getCode().equalsIgnoreCase("typeMismatch")) {
                				try {
                					strMessage = MessageProvider.getInstance().getMessage(Integer.parseInt(objError.getCode()),
                							arg);
                				} catch (ContentException e) {
                					strMessage = "Invalid message format for error code [" + objError.getCode() + "]";

                				} /*catch (IllegalArgumentException e) {
                					String key = messageSource.getMessage(objError.getCode(), arg, null);
                					logger.info(key);
                					try {
                						strMessage = MessageProvider.getInstance().getMessage(Integer.parseInt(key), arg);
                					} catch (Exception  e1) {
                						e1.printStackTrace();
                					}
                				} */catch (Exception ex) {
                					logger.info(ex);
                				}
                			}
                    		errors.add(strMessage);
                			
                		}                    	
                        
                }
            }
        }
        return (String[]) errors.toArray(new String[0]);
    }
    
    /**
     * Return formatted message for the exception provided as a parameter.
     * 
     * @param genericException - ContentException
     * @return The full message text
     * @throws ContentException
     */
    public static String getMessage(final GenericExceptionWithContentType genericException)
            throws ContentException {

        final Integer errorCode = new Integer(genericException.getErrorCode());

        Content content = null;
        try {
            content = ContentCacheManager.getInstance().getContentById(errorCode,
                    genericException.getContentType());
        } catch (ContentException contentException) {
            final String exceptionMessage = "Unable to lookup content for GenericExceptionWithContentType ["
                    + genericException + "]";
            logger.warn(content, contentException);
            throw new RuntimeException(exceptionMessage, contentException);
        } // end try/catch

        if (content == null) {
            logger.warn("Unable to lookup content for GenericExceptionWithContentType ["
                    + genericException + "]");
            content = new MutableMessage();
            ((MutableMessage) content).setText(MISSING_CONTENT_TEXT);
            ((MutableMessage) content).setCode(0);
        } // fi

        String text = ContentUtility.getContentAttribute(content, "text");
        MessageFormat msgFormat = null;
        if(genericException.isShowCMACodeInd() && content instanceof Message){
        	msgFormat = new MessageFormat(text + "&nbsp;&nbsp;[" + ((Message)content).getCode() + "]");
    	}else{
    		msgFormat = new MessageFormat(text);
    	}

        return MessageProvider.getInstance().format(msgFormat, errorCode,
                genericException.getParams());
    }


}
