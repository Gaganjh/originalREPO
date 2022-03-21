<%@ page isErrorPage="true" %>
<%@ page import="com.manulife.pension.exception.SystemException" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.pagelayout.LayoutBeanRepository" %>
<%@ page import="com.manulife.pension.ps.web.pagelayout.LayoutBean" %>
<%@ page import="com.manulife.pension.exception.ExceptionHandlerUtility" %>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>



<%

	// clear errors out of session
	if ( session != null ) {
		session.removeAttribute(Constants.ERROR_KEY);
	}

	if ( exception != null )
	{
	    // log user info
	    StringBuffer msg = new StringBuffer("Exception handled in error.jsp. ");
	    if ( session != null )
	    {
	    	Object obj = session.getAttribute(Constants.USERPROFILE_KEY);
	    	if ( obj != null )
			{
				UserProfile user = (UserProfile)obj;
				msg.append(user);
			}
		}

		SystemException se = null;

		Throwable causingException = ExceptionHandlerUtility.getSystemExceptionOrCausedByException(exception);

		if (causingException instanceof SystemException) {
		  se = (SystemException)causingException;
		} else {
	      se = new SystemException(causingException, "error.jsp", "NA", msg.toString());
	    }

		//log exception
		com.manulife.pension.util.log.LogUtility.log(se);

		request.setAttribute("errorCode", "1099");
		request.setAttribute("uniqueErrorId", se.getUniqueId());
	}

	LayoutBean layoutBean = LayoutBeanRepository.getInstance().getPageBean("/home/secureHomePage.jsp");
	request.setAttribute(Constants.LAYOUT_BEAN, layoutBean);

%>
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />
<html>
<title>System Error Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Expires" content="0"/>
<meta http-equiv="Cache-Control" content="no-store,no-cache,must-revalidate,post-check=0,pre-check=0"/>
<meta http-equiv="Pragma" content="no-cache"/>

<link rel="stylesheet" href="/assets/unmanaged/stylesheet/manulife.css" type="text/css">

<script type="text/javascript">
	var ie4 = (document.all != null);
	var ns4 = (document.layers != null); // not supported
	var ns6 = ((document.getElementById) && (navigator.appName.indexOf('Netscape') != -1));
	var isMac = (navigator.appVersion.indexOf("Mac") != -1);
</script>

<script>
	<script language="javascript" src="/assets/unmanaged/javascript/layer_functions.js"/>
	<script language="javascript" src="/assets/unmanaged/javascript/nav.js"/>
	<script language="javascript" src="/assets/unmanaged/javascript/manulife.js"/>
	<script language="javascript" src="/assets/unmanaged/javascript/URL.js"/>
</script>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="init(0,0)" >

  <table width="765" border="0" cellspacing="0" cellpadding="0" class="fixedTable">
    <tr>
      <td width="%10">
        &nbsp;
      </td>
      <td>
		System Error occurred.<br>
		Please try again. If you are still experiencing the same problem
		please call your client account representative. <b>[<%= request.getAttribute("errorCode") %> - <%= request.getAttribute("uniqueErrorId") %>]
	  </td>
	</tr>
	<tr>
      <td width="%10">
        &nbsp;
      </td>
	  <td>
	    <a href="/do/home/homePageFinder/">Home Page</a>
	  </td>
	</tr>
  </table>


<br>
</body>
</html>
