<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="quickreports" uri="manulife/tags/ps" %>
<%@	taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@	taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@	taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- Imports --%>
<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.ps.web.pagelayout.LayoutBean" />

<%@ page import="com.manulife.pension.ps.web.pagelayout.LayoutBean" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.service.security.role.UserRole" %>
<%@ page import="com.manulife.pension.service.security.role.InternalUser" %>
<%@ page import="com.manulife.pension.service.security.role.ThirdPartyAdministrator" %>
<%@ page import="com.manulife.pension.ps.web.controller.SecurityManager" %>

<%-- Start of Report Tools Links --%>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
LayoutBean layoutBeanObj = (LayoutBean)request.getAttribute("layoutBean");


	//default settings
	String reportToolsLinksParamName = "reportToolsLinks";
	String reportToolsLinksParamNameAsIds = "reportToolsLinks";

	String reportToolsLinksKey = "reportToolsLinks";

	UserRole role = userProfile.getRole();
	//if role is of type InternalUser (IUM, SCAR, CAR, TL, RM)
	if(role instanceof InternalUser) {
		reportToolsLinksParamName = "reportToolsLinksInternal";	
		reportToolsLinksParamNameAsIds = "reportToolsLinksInternal";
		reportToolsLinksKey = "reportToolsLinksInternal";
	//if role is of type ThirdPartyAdministrator (TPA)
	} else if (role instanceof ThirdPartyAdministrator){
		reportToolsLinksParamName = "reportToolsLinksTPA";
		reportToolsLinksParamNameAsIds = "reportToolsLinksTPA";
		reportToolsLinksKey = "reportToolsLinksTPA";	
	//default settings (External users)
	} else {
		reportToolsLinksParamName = "reportToolsLinks";
		reportToolsLinksParamNameAsIds = "reportToolsLinks";
		reportToolsLinksKey = "reportToolsLinks";
	}

	//if layoutBean does not contain the reportToolsLinksKey then reset to
	//default settings
	if (!layoutBean.getParams().containsKey(reportToolsLinksKey)){
		reportToolsLinksParamName = "reportToolsLinks";
		reportToolsLinksParamNameAsIds = "reportToolsLinks";
	}

String removeLink="false";
String href ="#";
String onclick ="return true;";

pageContext.setAttribute("paramName", layoutBeanObj.getParam(reportToolsLinksParamName));
if(layoutBeanObj.getParam(reportToolsLinksParamName) != null){
	pageContext.setAttribute("paramNameAsIds", layoutBeanObj.getParamAsIds(reportToolsLinksParamNameAsIds));	
}
%>




<c:if test="${not empty paramName}">
<table width="180" border="0" cellspacing="0" cellpadding="0" class="beigeBox">
  <tr>
    <td class="beigeBoxTD1">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">

<c:forEach items="${paramNameAsIds}" var="contentId" varStatus="indexVal"> <%-- type="java.lang.Integer" --%>

<c:set var="contentId" value="${contentId}"/>
<%String contentId = pageContext.getAttribute("contentId").toString(); %>

          <content:contentBean contentId="<%=Integer.parseInt(contentId)%>"
                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                               id="contentBean" override="true"/>

		  <% removeLink = "false"; %>
          <%  if (Integer.parseInt(contentId) == ContentConstants.PAYROLL_FEEDBACK_SERVICE_ACTIVITY_HISTORY_REPORT_LINK) { 
        	  
	         	String extension = null;
	        	if (layoutBean.getParam("downloadExtension") == null) {
	        		extension = ".csv";
	        	} else {
	        		extension = "." + layoutBean.getParam("downloadExtension");
	        	}
	        	String result = "/do/participant/payrollSelfServiceActivityHistory/?task=download&ext=" + java.net.URLEncoder.encode(extension);
	       	    href = result;
	            onclick = "return true;";
	        %>
		  <% }else if (Integer.parseInt(contentId)  == ContentConstants.COMMON_DOWNLOAD_TO_CSV_TEXT) {
              String extension = null;
            	if (layoutBeanObj.getParam("downloadExtension") == null) {
            		extension = ".csv";
            	} else {
            		extension = "." + layoutBeanObj.getParam("downloadExtension");
            	}
          	href = "?task=download&ext=" + java.net.URLEncoder.encode(extension);
          	onclick = "return true";
			    	
       } %>


		  <% if (removeLink.equals("false")) { %>
          <tr>
            <td width="17">
			  <a href="<%= href %>" onclick="<%= onclick %>"><content:image id="contentBean" contentfile="image"/></a></td>
            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
            <td>
              <a href="<%= href %>" onclick="<%= onclick %>"><content:getAttribute id="contentBean" attribute="text"/></a>
            </td>
          </tr>
          <% } %>
</c:forEach>
      </table>
    </td>
  </tr>
  <tr>
    <td align="right"><img src="/assets/unmanaged/images/box_lr_corner_E9E2C3.gif" width="5" height="5"></td>
   </tr>
 </table>

</c:if>

<%-- End of Report Tools Links --%>
