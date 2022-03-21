package com.manulife.pension.platform.utility.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.content.bizdelegates.ContentBrowseServiceDelegate;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.service.ContentSource;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentType;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.Location;

import com.manulife.pension.content.view.MutablePageFootnote;
import com.manulife.pension.content.view.MutableContent;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ireports.content.ContentUtility;
import com.manulife.pension.ireports.dao.Footnote;
import com.manulife.pension.platform.utility.content.CommonContentConstants;
import com.manulife.pension.platform.utility.util.exception.GenericException;

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
            /*try {
                content = ContentCacheManager.getInstance().getContentById(contentId, contentType, contentLocation);
            } catch (ContentException contentException) {
                logger.error("Error occured while retrieveing CMA Content" + contentException);
            }*/
            try {
    			content = ContentBrowseServiceDelegate.getInstance().getContentByKey(contentId, contentType.getName(), contentLocation,ContentSource.PSW);
    		} catch (SystemException e) {
    			 logger.error("Failed while fetching content by the content ID : " + e.getMessage());
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
     * @throws SystemException 
     */
    public static String getContentText(int contentId, ContentType contentType, Location contentLocation) {
        String text = "";
        if (contentType != null) {
            Content content = null;
            //content = //ContentCacheManager.getInstance().getContentById(contentId, contentType, contentLocation);
			try {
				content = ContentBrowseServiceDelegate.getInstance().getContentByKey(contentId, contentType.getName(), contentLocation,ContentSource.PSW);
			} catch (SystemException e) {
				 logger.error("Failed while fetching content by the content ID : " + e.getMessage());
			}
            if (content != null) {
                text = ContentUtility.getContentAttribute(content, CommonContentConstants.TEXT);
            }
        }
        return text;
    }
    
    /**
     * This method retrieves the content text without substitution of given parameters.
     * 
     * @param staticFootnoteSymbols
     * @param contentLocation
     * @return Map footnoteSymbol,Footnote
     * @throws SystemException 
     */
	public static Map<String, Footnote> getContentTextByDisplayName(
			String[] staticFootnoteSymbols, Location location
			) throws SystemException {
		String footnoteText = "";
		MutablePageFootnote message = null;
		MutableContent content = null;
		Footnote footnote = null;
		Map<String, Footnote> result = new HashMap<String, Footnote>();
		Content[] results = ContentBrowseServiceDelegate.getInstance()
				.findContent(
						ContentTypeManager.instance().PAGE_FOOTNOTE.getName(),
						location, ContentSource.PSW);

		for (int k = 0; k < staticFootnoteSymbols.length; k++) {
			String symbol = staticFootnoteSymbols[k];
			for (int i = 0; i < results.length; i++) {
				content = (MutableContent) results[i];
				if (content.getIsEnabled() 
						&& content.getDisplayName().equals(symbol)) {
					message = (MutablePageFootnote) content;
					footnoteText = message.getText();
					footnote = new Footnote(symbol, footnoteText);
					result.put(symbol, footnote);
					results = ArrayUtils.removeElement(results, content);
					break;
				}

			}
		
		}
		return result;
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
        /*if (messageCollection != null) {

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
                    }
                }
            }
        }*/
        return (String[]) errors.toArray(new String[0]);
    }
    
    /**
     * Return formatted message for the exception provided as a parameter.
     * 
     * @param genericException - ContentException
     * @return The full message text
     * @throws ContentException
     */
   //This method is not used by the Ireports this is avaliable in Web layer
/*    public static String getMessage(final GenericExceptionWithContentType genericException)
            throws ContentException {

        final Integer errorCode = new Integer(genericException.getErrorCode());
        //TODO Hard coded the Location
        Content content = null;
        try {
            content = ContentCacheManager.getInstance().getContentById(errorCode,
                    genericException.getContentType());
           content = ContentBrowseServiceDelegate.getInstance().getContentByKey(errorCode,genericException.getContentType().getName(), Location.USA, ContentSource.PSW,true);
    		
        } catch (ContentException contentException) {
            final String exceptionMessage = "Unable to lookup content for GenericExceptionWithContentType ["
                    + genericException + "]";
            logger.warn(content, contentException);
            throw new RuntimeException(exceptionMessage, contentException);
        } // end try/catch

        catch (SystemException contentException) {
            final String exceptionMessage = "Unable to lookup content for GenericExceptionWithContentType ["
                    + genericException + "]";
            logger.warn(content, contentException);
            throw new RuntimeException(exceptionMessage, contentException);
        }
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
    }*/


}
