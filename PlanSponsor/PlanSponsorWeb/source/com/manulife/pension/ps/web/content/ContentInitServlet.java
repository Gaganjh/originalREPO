package com.manulife.pension.ps.web.content;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.util.content.manager.ContentCacheManager;


/* This servlet is a workaround for the problem with the ContentType value object.
 * So, we use this servlet to initialize the ContentType value object within the
 * web container.
 */

public class ContentInitServlet extends HttpServlet {
	
	public void init(ServletConfig config) throws ServletException {
		super.init ( config );
		try {
			Content cntBean = ContentCacheManager.getInstance().getContentById(53965, ContentTypeManager.instance().MISCELLANEOUS);			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void service(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		//This servlet should never be called!!
	}
}