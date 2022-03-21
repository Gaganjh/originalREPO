package com.manulife.pension.platform.web.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * This tag will output the JavaScirpt, which would do an Asynchronous request periodically 
 *
 * @author ayyalsa
 */
public class KeepAliveTag extends TagSupport {

	/** Default Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	/** Attribute to get the minutes parameter **/
	private String minutes;

	/**
	 * @return the minutes
	 */
	public String getMinutes() {
		return minutes;
	}

	/**
	 * @param minutes the minutes to set
	 */
	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}
	
	@Override
	public int doEndTag()  throws JspException {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(" <script type='text/javascript'> \n ");
		buffer.append(" 	var sessionPingCount = 0;\n ");
		buffer.append(" 	var keepSessionAliveMinutes = " + getMinutes() + ";\n ");
		buffer.append(" 	var pingIntervalMinutes = 5;\n ");
		buffer.append(" 	var millisecondsPerMinute = 60000;\n ");
		buffer.append(" 	var maxPingCount = Math.ceil(keepSessionAliveMinutes / pingIntervalMinutes);\n ");
		buffer.append(" 	var repeatPing = setInterval('doRepeatPing()',pingIntervalMinutes*millisecondsPerMinute);\n ");
		
		buffer.append("		function doRepeatPing() { \n ");
		buffer.append(" 		sessionPingCount = sessionPingCount + 1; \n ");
		buffer.append(" 		ajaxSendPing();\n ");
		buffer.append(" 		if (sessionPingCount >= maxPingCount) { \n ");
		buffer.append(" 	        clearInterval(repeatPing); \n ");
		buffer.append(" 	    } \n ");
		buffer.append(" 	}\n ");

		buffer.append(" 	function ajaxSendPing() { \n ");
		buffer.append(" 		var ajaxReq;\n ");
		buffer.append(" 		try { \n ");
		buffer.append(" 			ajaxReq = new ActiveXObject('Msxml2.XMLHTTP'); \n ");
		buffer.append(" 		} catch (e) { \n ");
		buffer.append(" 			try { \n ");
		buffer.append(" 				ajaxReq = new ActiveXObject('Microsoft.XMLHTTP'); \n ");
		buffer.append(" 			}catch (e2) { \n ");
		buffer.append(" 				try { \n ");
		buffer.append(" 					ajaxReq = new XMLHttpRequest(); \n ");
		buffer.append(" 				}catch (e3) { \n ");
		buffer.append(" 					ajaxReq = false; \n ");
		buffer.append(" 				} \n ");
		buffer.append(" 			} \n ");
		buffer.append(" 		} \n ");
		buffer.append(" 		try { \n ");
		buffer.append(" 			ajaxReq.onreadystatechange = function(){} \n " );
//		buffer.append("					if (ajaxReq.readyState==4) { alert('Callback Fired');  }} \n ");
		buffer.append(" 			ajaxReq.open('get', '/PingServlet'); \n ");
		buffer.append(" 			ajaxReq.send(null); \n ");
		buffer.append(" 		} catch (e4) { \n ");
		buffer.append(" 		} \n ");
		buffer.append(" 	} \n ");
		buffer.append(" </script>\n ");
		
		try {
			
			JspWriter out = pageContext.getOut();
			out.print(buffer.toString());
			
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
		
		return SKIP_BODY;
	}
	
}
