<%@ page isErrorPage="true" %>
<%@ page import="com.manulife.pension.exception.SystemException" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@ page import="com.manulife.pension.exception.ExceptionHandlerUtility" %>
<%@page import="com.manulife.pension.bd.web.userprofile.BDUserProfile"%>
<%@page import="com.manulife.pension.platform.web.CommonErrorCodes"%>




<%@ taglib prefix="content" uri="manulife/tags/content" %>

<%

	// clear errors out of session
	if ( session != null ) {
		session.removeAttribute(BDConstants.ERROR_KEY);
	}

	if ( exception != null )
	{
	    // log user info
	    StringBuffer msg = new StringBuffer("Exception handled in error.jsp. ");
	    if ( session != null )
	    {
	    	Object obj = session.getAttribute(BDConstants.USERPROFILE_KEY);
	    	if ( obj != null )
			{
				BDUserProfile user = (BDUserProfile)obj;
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

%>

<html>
<title>System Error Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="Expires" content="0"/>
<meta http-equiv="Cache-Control" content="no-store,no-cache,must-revalidate,post-check=0,pre-check=0"/>
<meta http-equiv="Pragma" content="no-cache"/>

<script type="text/javascript" >
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
      <c:if test="${empty sessionScope.CommonErrorCodes.ERROR_FRW_VALIDATION}">					
      <td>
		System Error occurred.<br>
		Please try again. If you are still experiencing the same problem
		please call your client account representative. <b>[<%= request.getAttribute("errorCode") %> - <%= request.getAttribute("uniqueErrorId") %>]
	  </td>
	  </c:if>
	   <c:if test="${not empty sessionScope.CommonErrorCodes.ERROR_FRW_VALIDATION}">				
		<td>
			<content:errors scope="request" />
		</td>
	 </c:if>
	</tr>
	<tr>
      <td width="%10">
        &nbsp;
      </td>
	  <td>
	    <a href="/do/home/">Home Page</a>
	  </td>
	</tr>
  </table>


<br>
</body>
</html>
