<%@tag 
    import="com.manulife.pension.bd.web.ApplicationHelper"
	import="com.manulife.pension.bd.web.controller.SecurityManager"
	import="com.manulife.pension.bd.web.userprofile.BDUserProfile"
	import="com.manulife.pension.bd.web.util.BDSessionHelper"
	import="com.manulife.pension.bd.web.controller.AuthorizationSubject"
	%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@attribute name="url"
	type="java.lang.String"
	required="true" rtexprvalue="true"%>

<%
	AuthorizationSubject subject = ApplicationHelper.getAuthorizationSubject(
					(HttpServletRequest) request);
	SecurityManager securityManager = ApplicationHelper
			.getSecurityManager(application);
	if (securityManager.isUserAuthorized(subject, url)) {
%>
  
<jsp:doBody/>
<%
	}
%>


