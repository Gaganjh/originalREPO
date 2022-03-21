package com.manulife.pension.ps.web.messagecenter.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.util.Environment;

/**
 * The facade factory.  It will return the one in the ServletContext if
 * it exists or the default instance one
 * 
 * @author guweigu
 *
 */
public class MessageServiceFacadeFactory {
	private static final MessageServiceFacade defaultInstance = new MessageServiceFacadeImpl(
			Environment.getInstance().getAppId());


	/**
	 * A facade instance should be put into ServletContext
	 * at the initialization time (i.e. A application context
	 * listener could do this for example)
	 *  
	 * @param servlet
	 * @return
	 */
	public static MessageServiceFacade getInstance(ServletContext servlet) {
		MessageServiceFacade facade = (MessageServiceFacade) servlet.getAttribute(
						MCConstants.AttrMessageServiceFacade);
		return facade == null ? defaultInstance : facade;
	}
}
