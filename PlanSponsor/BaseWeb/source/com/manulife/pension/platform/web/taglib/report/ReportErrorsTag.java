package com.manulife.pension.platform.web.taglib.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;

import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.platform.web.taglib.util.TagUtils;
import com.manulife.pension.platform.web.util.ContentHelper;
import com.manulife.pension.util.content.MessageProvider;
import com.manulife.pension.util.content.manager.ContentCacheConstants;
import com.manulife.pension.util.content.manager.ContentProperties;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationErrorCodeAndParamEqualityPredicate;


/**
 * This class represents a tag that shows errors , warnings and informational
 * messages in the appropriate format
 * 
 * @author Vanidha Rajendhiran
 */

public class ReportErrorsTag extends AbstractReportTag {

	/**
	 * Default serialVersion UID.
	 */
	private static final long serialVersionUID = 1L;

	public static final String FOOTER = "</dl></div>";

	private static final String SESSION_TEXT = "session";

	private static final String PAGE_TEXT = "page";

	private static final String REQUEST_TEXT = "request";

	private static final String APPLICATION_TEXT = "application";

	private boolean isPageContextScopeRequest = false;

	private String scope = null;
	
	private String escapeSequence = null;
	
	private Boolean suppressDuplicateMessages = false;

	private int pageContextScope = PageContext.SESSION_SCOPE;

	private Logger logger = Logger.getLogger(ReportErrorsTag.class);

	private static Map<String, Integer> scopes = new HashMap<String, Integer>();
	
	private static final String BD_WARNINGS="bdWarnings";
	
	private static final String BD_MESSAGES="bdMessages";
	
	private static final String DIV_CLASS_ERROR="message_error";
	
	private static final String DIV_CLASS_WARNING="message_warning";
	
	private static final String DIV_CLASS_INFORMATIONAL_MESSAGE="message_info";
	
    private static final String ERROR_MESSAGE="Error Message";
	
	private static final String WARNING_MESSAGE="Warning Message";
	
	private static final String INFORMATION_MESSGE="Information Message";
	private static MessageSource messageSource;
	
	static {

		scopes.put(PAGE_TEXT, new Integer(PageContext.PAGE_SCOPE));
		scopes.put(REQUEST_TEXT, new Integer(PageContext.REQUEST_SCOPE));
		scopes.put(SESSION_TEXT, new Integer(PageContext.SESSION_SCOPE));
		scopes.put(APPLICATION_TEXT, new Integer(
						PageContext.APPLICATION_SCOPE));
	}

	/**
	 * This method shows the errors in the collection.
	 * 
	 * @return Returns SKIP_BODY
	 * @throws JspException If an exception occurs.
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {

		String errorKey = null;
		boolean errorCollectionInd = false;
		boolean errorListInd = false;
		try {
			errorKey = getErrorKeyAttribute();
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}

		Collection errorCollection = null;
		Collection warningCollection = null;
		Collection messageCollection = null;
		
		List<ObjectError>  errorList =null;
		List<ObjectError>  warningList =null;
		List<ObjectError>  messageList =null;

		if (getScope() != null) {
			
			Object errorObj = TagUtils.getInstance().lookup(
					pageContext, errorKey, getScope());
			
			Object warningObj = TagUtils.getInstance().lookup(
					pageContext, BD_WARNINGS, getScope());
			
			Object messageObj = TagUtils.getInstance().lookup(
					pageContext, BD_MESSAGES, getScope());
			
			if( errorObj instanceof Collection){
        		errorCollection = (Collection) errorObj;
        		errorCollectionInd = true;
        	} else if (errorObj instanceof BeanPropertyBindingResult){
    			errorList=((BeanPropertyBindingResult) errorObj).getAllErrors();
    			errorListInd = true;
    			logger.info("Error List is :"+errorList); 
    		}
			
			if( warningObj instanceof Collection){
        		warningCollection = (Collection) warningObj;
        		errorCollectionInd = true;
        	} else if (warningObj instanceof BeanPropertyBindingResult){
    			warningList=((BeanPropertyBindingResult) warningObj).getAllErrors();
    			errorListInd = true;
    			logger.info("Error List is :"+warningList); 
    		}
			
			if( messageObj instanceof Collection){
        		messageCollection = (Collection) messageObj;
        		errorCollectionInd = true;
        	} else if (messageObj instanceof BeanPropertyBindingResult){
    			messageList=((BeanPropertyBindingResult) messageObj).getAllErrors();
    			errorListInd = true;
    			logger.info("Error List is :"+messageList); 
    		}
			
			
			if (getScope().equals(SESSION_TEXT) && errorCollectionInd ) {
				pageContext.setAttribute(errorKey, errorCollection,
						PageContext.REQUEST_SCOPE);
				pageContext.setAttribute(BD_WARNINGS, warningCollection,
						PageContext.REQUEST_SCOPE);
				pageContext.setAttribute(BD_MESSAGES, messageCollection,
						PageContext.REQUEST_SCOPE);
			}
			
			if (getScope().equals(SESSION_TEXT) && errorListInd ) {
				pageContext.setAttribute(errorKey, errorList,
						PageContext.REQUEST_SCOPE);
				pageContext.setAttribute(BD_WARNINGS, warningList,
						PageContext.REQUEST_SCOPE);
				pageContext.setAttribute(BD_MESSAGES, messageList,
						PageContext.REQUEST_SCOPE);
			}
			pageContext.removeAttribute(errorKey, scopes.get(getScope())
					.intValue());
			pageContext.removeAttribute(BD_WARNINGS, scopes.get(getScope())
					.intValue());
			pageContext.removeAttribute(BD_MESSAGES, scopes.get(getScope())
					.intValue());

		} 

		//To show error messages
		if (errorCollection != null && !errorCollection.isEmpty()) {

			frameMessage(errorCollection, DIV_CLASS_ERROR,ERROR_MESSAGE);
		}
		if (warningCollection != null && !warningCollection.isEmpty()) {

			frameMessage(warningCollection, DIV_CLASS_WARNING, WARNING_MESSAGE);
		}
		if (messageCollection != null && !messageCollection.isEmpty()) {

			frameMessage(messageCollection, DIV_CLASS_INFORMATIONAL_MESSAGE, INFORMATION_MESSGE);
		}
		
		if (errorList != null && !errorList.isEmpty()) {

			frameMessage1(errorList, DIV_CLASS_ERROR,ERROR_MESSAGE);
		}
		if (warningList != null && !warningList.isEmpty()) {

			frameMessage1(warningList, DIV_CLASS_WARNING, WARNING_MESSAGE);
		}
		if (messageList != null && !messageList.isEmpty()) {

			frameMessage1(messageList, DIV_CLASS_INFORMATIONAL_MESSAGE, INFORMATION_MESSGE);
		}
		
		

		return SKIP_BODY;
	}
	String[] errors = null;
	private void frameMessage1(List<ObjectError> errorList, String divClassError, String errorMessage) {
		StringBuffer html = new StringBuffer();
		logger.info("Inside errorlist method in errors.java******");
		errors = new String[errorList.size()];
		for (int i = 0; i < errorList.size(); i++) {
			ObjectError objError = errorList.get(i);
			logger.info("Objerror is : " + objError);
			Object[] arg = objError.getArguments();
			logger.info("arg is ********" + arg);
			String strMessage = null;
			if (!objError.getCode().equalsIgnoreCase("typeMismatch")) {
				try {
					strMessage = MessageProvider.getInstance().getMessage(Integer.parseInt(objError.getCode()),
							arg);
				} catch (ContentException e) {
					strMessage = "Invalid message format for error code [" + objError.getCode() + "]";

				} catch (IllegalArgumentException e) {
					String key = messageSource.getMessage(objError.getCode(), arg, null);
					logger.info(key);
					try {
						strMessage = MessageProvider.getInstance().getMessage(Integer.parseInt(key), arg);
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ContentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					errors[i] = "Invalid message format for error code [" + objError.getCode() + "]";
				} catch (Exception ex) {
					logger.info(ex);
				}
			}

			if (strMessage != null && !(strMessage.equals("null"))) {
				errors[i] = strMessage;
				// i++;
			}
		}
	
		html.append("<div class='message " + divClassError + "'>"
				+ "<dl>"
				+ "<dt>"+errorMessage+"</dt>");
					
		for (int i = 0; i < errors.length; i++) {
			
			    html.append("<dd>");
				html.append((i+1)+".&nbsp;&nbsp;");
				if (StringUtils.equalsIgnoreCase("HTML", escapeSequence)) {
					html.append(StringEscapeUtils.escapeJava(StringEscapeUtils.escapeHtml(errors[i])));
				} else {
					html.append(errors[i]);
				}
				
				html.append("</dd>");
			
		}

		html.append(FOOTER);

		try {
			pageContext.getOut().print(html.toString());
			
		} catch (IOException ex) {
			logger.warn("Got an exception while processing tag", ex);
			try {
				throw new JspException(ex.toString());
			} catch (JspException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * This method will create the html code depending on 
	 * if it's a warning or error or infromational message.
	 * 
	 * @param messageCollection
	 * @param messageType
	 * @throws JspException
	 */
	@SuppressWarnings("unchecked")
	private void frameMessage(Collection messageCollection, String divClass, String messageType)
			throws JspException {

		StringBuffer html = new StringBuffer();

		if (messageCollection != null && !messageCollection.isEmpty()) {
			
			Collection uniqueMessages = new ArrayList();
			Collection nonValidationErrorMessages = new ArrayList();
			
			// Check if we should suppress duplicate messages
			for(Object message : messageCollection) {
				
				if(message instanceof ValidationError) {
					
					if (BooleanUtils.isTrue(getSuppressDuplicateMessages())) {
		                if (!CollectionUtils.exists(uniqueMessages, new ValidationErrorCodeAndParamEqualityPredicate(
		                		(ValidationError) message))) {
		                   
		                    uniqueMessages.add((ValidationError) message);
		                }
		            }
				}else if(message instanceof ObjectError) {
					
					//if (BooleanUtils.isTrue(getSuppressDuplicateMessages())) {
		               /* if (!CollectionUtils.exists(uniqueMessages, new ValidationErrorCodeAndParamEqualityPredicate(
		                		(ObjectError) message))) {*/
		                   
		                    uniqueMessages.add((ObjectError) message);
		               /* }*/
		           // }
				}  else {
					nonValidationErrorMessages.add(message);
				}
			}
			
			Collection messages = new ArrayList();
			if (BooleanUtils.isTrue(getSuppressDuplicateMessages())) {
				messages.addAll(uniqueMessages);
				messages.addAll(nonValidationErrorMessages);
			} else {
				messages.addAll(messageCollection);
			}
			
            String[] errors = null;
            try {
                errors = ContentHelper.getMessagesUsingContentType(messages);
            } catch (Exception ex) {
                logger.warn("Got an exception while processing tag", ex);
                throw new JspException(ex);
            }
		    
            /**
             * Make sure that the HTML content doesn't have any double quotes ("), 
             * Funds & Performance page is using JSON, If there are any ("), the
             * JSON will not be created properly, which would result a bad output in 
             * the screen 
             */
			html.append("<div class='message " + divClass + "'>"
					+ "<dl>"
					+ "<dt>"+messageType+"</dt>");
						
			for (int i = 0; i < errors.length; i++) {
				
				    html.append("<dd>");
					html.append((i+1)+".&nbsp;&nbsp;");
					if (StringUtils.equalsIgnoreCase("HTML", escapeSequence)) {
						html.append(StringEscapeUtils.escapeJava(StringEscapeUtils.escapeHtml(errors[i])));
					} else {
						html.append(errors[i]);
					}
					
					html.append("</dd>");
				
			}

			html.append(FOOTER);

			try {
				pageContext.getOut().print(html.toString());
				
			} catch (IOException ex) {
				logger.warn("Got an exception while processing tag", ex);
				throw new JspException(ex.toString());
			}
		}

	}

	
	/**
	 * Sets the isPageContextScopeRequest.
	 * 
	 * @param isPageContextScopeRequest The isPageContextScopeRequest to set
	 */
	public void setIsPageContextScopeRequest(
			final boolean isPageContextScopeRequest) {
		this.isPageContextScopeRequest = isPageContextScopeRequest;
		if (this.isPageContextScopeRequest) {
			pageContextScope = PageContext.REQUEST_SCOPE;
		}
	}

	/**
	 * Gets the ErrorKeyAttribute.
	 * 
	 * @return String - The error key attribute.
	 * @throws Exception If an exception occurs.
	 */
	private String getErrorKeyAttribute() throws Exception {
		return ContentProperties.getInstance().getProperty(
				ContentCacheConstants.ERROR_KEY);
	}

	/**
	 * Sets the Scope.
	 * 
	 * @param scope The scope to set
	 */
	public void setScope(final String scope) {
		this.scope = scope;
	}

	/**
	 * Gets the Scope.
	 * 
	 * @return string
	 */
	public String getScope() {
		return this.scope;
	}

	/**
	 * @return the escapeSequence
	 */
	public String getEscapeSequence() {
		return escapeSequence;
	}

	/**
	 * @param escapeSequence the escapeSequence to set
	 */
	public void setEscapeSequence(String escapeSequence) {
		this.escapeSequence = escapeSequence;
	}
	
	/**
     * @return the suppressDuplicateMessages
     */
    public Boolean getSuppressDuplicateMessages() {
        return suppressDuplicateMessages;
    }

    /**
     * @param suppressDuplicateMessages the suppressDuplicateMessages to set
     */
    public void setSuppressDuplicateMessages(final Boolean suppressDuplicateMessages) {
        this.suppressDuplicateMessages = suppressDuplicateMessages;
    }
    
	public void setMessageSource(MessageSource arg) {
		this.messageSource = arg;

	}

}