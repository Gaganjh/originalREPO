<%@page import="org.owasp.encoder.Encode"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.lang.NumberFormatException"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.net.URLConnection"%>
<%@page import="java.net.URL"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.net.URL"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.lang.System"%>
<%@page import="com.manulife.pension.util.BaseEnvironment"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>

<%  /** @author Mark Eldridge 
      *
      *  This JSP acts as a proxy to the JH Navigator site for use with the Find Literature page.
      *  It takes parameters passed on the request URI and tacks them onto the end of one of the
      *  JH Navigator URLs specified by the proxyURL URI parameter.  It copies the output from that URL
      *  back onto the output stream of this JSP.  
      *
      *  The Find Literature search engine (which is javascript based and called our Proxy as an AJAX asynchronous
      *  request.  The output from this gets used by the callback function in Marketing's JS files.  That function
      *  reads this output and displays results in the Find Literature section of the NBDW site.
      *
      *  There are multiple proxyURLs which are requested using a mandatory &proxyURL= parameter.
      *  (Note: If this parameter is not supplied it assumes "0" and will use proxyURL[0])
      *
      *  proxyURL parameter is an index to proxyURL[] array below to indicate which URL is to be used.
      *  Each index also has it's own exception handler in the catch block, which will return the 
      *  "emtpy" set of data as per Marketing's requirements.  This could be HTML or XML or JSON based on that
      *  particular proxy URL.
      *
      *  To add a new proxy, simply add it's URL to the end of the proxyURL array, and create an exception handling
      *  if statement in the CATCH block to handle exceptions and pass back an agreed upon "default" value.
      *  EG)  ProxyURL=1 returns <html></html> if any exception in this code occurs.
      *
      *  @param  &proxyURL=int       - a URI request parameter which specifies which JH Navigator ProxyURL to use.
      *  @param "&parameter"="value" - there may be other URI parameters which are to passed on to the URL this is acting as proxy to.
      *  @return output from the URL this JSP is acting as a proxy to.
      *
    **/  
    String searchTerms = "";
    int proxyURLIndex = 0;
    BaseEnvironment environment = new BaseEnvironment();
    String jhNavigatior = "";
    try {
    	jhNavigatior = environment.getNamingVariable(BDConstants.JH_NAVIGATIOR_BASE_URL, null);
    } catch (Exception e) {
    	Logger logger = Logger.getLogger("jhNavigator.jsp");
    	logger.info("Namespace for jhNavigator not defined");
    }
    if (jhNavigatior == null){
    	Logger logger = Logger.getLogger("jhNavigator.jsp");
   	    logger.error("Namespace not detected in JH Navigator Proxy - in jhNavigator.jsp ");	
    }
		
	final String[] proxyURLs = { jhNavigatior+"com/jhrps/navigator/catalog/svcSearchResultsJSON.cfm?",
								 jhNavigatior+"com/jhrps/navigator/catalog/svcGetItemDetailsHTML.cfm?",
								 jhNavigatior+"com/jhrps/navigator/catalog/svcSearchResultsListViewHTML.cfm?"
              };
    
    try {
        Enumeration paramEnum = request.getParameterNames();
        while (paramEnum.hasMoreElements()) {
          String key = (String) paramEnum.nextElement();
          if (key != null && "proxyurl".equalsIgnoreCase(key)) {
            try {
                proxyURLIndex = new Integer(request.getParameter(key)).intValue();
            } catch (NumberFormatException e) {
                Logger logger = Logger.getLogger("jhNavigator.jsp");
                logger.error("NumberFormatException detected in JH Navigator Proxy - Exception occurred in jhNavigator.jsp (proxy to the JHNavigator site). Could not parse value of required parameter:proxyURL");
            }
          } else {
            searchTerms = key + "=" + request.getParameter(key) +"&" + searchTerms;            
          }
        }
        if(searchTerms != null && searchTerms.length()>2) {
            searchTerms = searchTerms.substring(0, searchTerms.length()-1);
            URL url = new URL(proxyURLs[proxyURLIndex]+searchTerms);
            URLConnection urlConn = url.openConnection();
            String contentType = urlConn.getContentType();
            response.setContentType(contentType);
            InputStream in = urlConn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            char[] buffer = new char[1024];
            String contentString = "";
            String tmp = br.readLine();
                do  {        
                    contentString += tmp + "\n";
                    tmp = br.readLine();
                } while (tmp != null);
           out.print(contentString);
           out.flush();
        }        
    } catch (Exception e) {
        Logger logger = Logger.getLogger("jhNavigator.jsp");
        logger.error("Exception detected in JH Navigator Proxy - Exception occurred in jhNavigator.jsp (proxy to the JHNavigator site). proxyURLIndex=" + String.valueOf(proxyURLIndex) + ";searchTerms=" + searchTerms,e);

        if (proxyURLIndex==0) {
            // Exception Repsonse for Proxy URL 0 - empty JSON items.    
            out.print("{ \"numResults\":0, \"items\":[ ]  }");
        } else if (proxyURLIndex==1) {
            // Exception Repsonse for Proxy URL 1 - empty HTML document with exception message in comments
        	 out.print("<html><!-- JH Navigator Proxy Error: " + Encode.forHtml(e.getMessage()) + "--></html>");        
        } else if (proxyURLIndex==2) {
            // Exception Repsonse for Proxy URL 2 - empty HTML row element with exception message in comments
        	   out.print("<tr class=\"row\"><td colspan=\"5\"><!-- JH Navigator Proxy Error: " +Encode.forHtml( e.getMessage()) + "-->&nbsp;</td></tr>");             
        } else {
          // A catch all in case no proxyURLIndex was passed.
        	  out.print("<html><!-- JH Navigator Proxy Error: " + Encode.forHtml(e.getMessage()) + "--><body><br><br><span style='margin-left:40px;'>An error occurred communicating with the JH Navigator service.<br>Please hit the back button on your browser</span></body></html>");
        } 
        
        out.flush();
    }
%>