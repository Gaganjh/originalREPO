package com.manulife.pension.ps.web.taglib.distribution;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import com.manulife.pension.platform.web.taglib.util.TagUtils;

import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.util.content.MessageProvider;

public class InitialMessagesTag extends BodyTagSupport {

	private static final long serialVersionUID = 1L;

	public static final String FOOTER = "</ul></td></tr></table>";

	public static final String LIST_ITEM_OPEN_TAG = "<li>";

	public static final String LIST_ITEM_CLOSE_TAG = "</li>";

	public static final String HEADER = "<table><tr><td class=\"orangeText\"><ul>";

	private Logger logger = Logger.getLogger(InitialMessagesTag.class);

	private String key;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public int doStartTag() throws JspException {

		Collection<Integer> messages = null;

		if (key == null) {
			/*
			 * Preserve existing behavior if key is not supplied.
			 */
			key = Constants.WITHDRAWAL_INITIAL_MESSAGES_KEY;
		}
		
		messages = (Collection<Integer>) TagUtils.getInstance().lookup(
				pageContext, key, "request");

		try {
			if (messages != null) {
				StringBuffer html = new StringBuffer();
				html.append(HEADER);
				Iterator<Integer> it = messages.iterator();
				while (it.hasNext()) {
					html.append(LIST_ITEM_OPEN_TAG);
					html.append(MessageProvider.getInstance().getMessage(
							it.next(), null));
					html.append(LIST_ITEM_CLOSE_TAG);
					html.append("\n");

				}
				html.append(FOOTER);
				try {
					pageContext.getOut().print(html.toString());
				} catch (IOException ex) {
					logger.warn("Got an exception while processing tag", ex);
					throw new JspException(ex.toString());
				}
			}

		} catch (Exception ex) {
			logger.warn("Got an exception while processing tag", ex);
			throw new JspException(ex.toString());
		}
		return SKIP_BODY;
	}
}
