<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="quickreports" uri="manulife/tags/ps" %>
<%@ taglib uri="manulife/tags/ps" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ page import="com.manulife.pension.ps.web.census.EmployeeEnrollmentSummaryReportForm" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.pagelayout.LayoutBean" %>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
LayoutBean layoutBeanObj = (LayoutBean)request.getAttribute("layoutBean");
%>

<%-- Imports --%>
<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.ps.web.pagelayout.LayoutBean"/>




<jsp:useBean id="employeeEnrollmentSummaryReportForm" scope="session" type="com.manulife.pension.ps.web.census.EmployeeEnrollmentSummaryReportForm" />

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.service.security.role.UserRole" %>
<%@ page import="com.manulife.pension.service.security.role.InternalUser" %>
<%@ page import="com.manulife.pension.service.security.role.ThirdPartyAdministrator" %>
<%@ page import="com.manulife.pension.ps.web.controller.SecurityManager" %>

<%-- Start of Report Tools Links --%>

<%

	//default settings
	String reportToolsLinksParamName = "reportToolsLinks";
	String howToReadReportLinkParamName = "howToReadReportLink";
	String reportToolsLinksParamNameAsIds = "reportToolsLinks";

	String reportToolsLinksKey = "reportToolsLinks";
	String howToReadReportLinkKey = "howToReadReportLink";

	//This code is used to not display the 'How To read this page' link on the editMyProfile
	//and manageExternalUser pages for Internal and TPA roles.  The links continue to
	//display for External users.  For both Internal and TPA roles there are new params
	//to indicate that the 'How To read this page'link is not to display.
	//Params  reportToolsLinksInternal and
	//reportToolsLinksTPA (with an empty "" string value) will stop the 'How To' box
	//from displaying completely.  While the
	//howToReadReportLinkInternal and howToReadReportLinkTPA params (with an empty "" string value)
	//will stop the 'How to read this page' link from displaying.
	//If a pagelayout does not contain these new params, then
	//the link will continue to display as usual.

	UserRole role = userProfile.getRole();
	//if role is of type InternalUser (IUM, SCAR, CAR, TL, RM)
	if(role instanceof InternalUser){
		reportToolsLinksParamName = "reportToolsLinksInternal";
		howToReadReportLinkParamName = "howToReadReportLinkInternal";
		reportToolsLinksParamNameAsIds = "reportToolsLinksInternal";
		reportToolsLinksKey = "reportToolsLinksInternal";
		howToReadReportLinkKey = "howToReadReportLinkInternal";
	//if role is of type ThirdPartyAdministrator (TPA)
	} else if (role instanceof ThirdPartyAdministrator){
		reportToolsLinksParamName = "reportToolsLinksTPA";
		howToReadReportLinkParamName = "howToReadReportLinkTPA";
		reportToolsLinksParamNameAsIds = "reportToolsLinksTPA";
		reportToolsLinksKey = "reportToolsLinksTPA";
		howToReadReportLinkKey = "howToReadReportLinkTPA";
	//default settings (External users)
	} else {
		reportToolsLinksParamName = "reportToolsLinks";
		howToReadReportLinkParamName = "howToReadReportLink";
		reportToolsLinksParamNameAsIds = "reportToolsLinks";
		reportToolsLinksKey = "reportToolsLinks";
		howToReadReportLinkKey = "howToReadReportLink";
	}

	//if layoutBean does not contain the reportToolsLinksKey then reset to
	//default settings
	if (!layoutBean.getParams().containsKey(reportToolsLinksKey)){
		reportToolsLinksParamName = "reportToolsLinks";
		reportToolsLinksParamNameAsIds = "reportToolsLinks";
	}

	if (!layoutBean.getParams().containsKey(howToReadReportLinkKey)){
		howToReadReportLinkParamName = "howToReadReportLink";
	}
	String removeLink="false";
	String href ="#";
	String onclick ="return true;";
	String howToReadReportLinkParamName1 = layoutBeanObj.getParam(howToReadReportLinkParamName);
	pageContext.setAttribute("paramName", layoutBeanObj.getParam(reportToolsLinksParamName));
	if(layoutBeanObj.getParam(reportToolsLinksParamName) != null){
		pageContext.setAttribute("paramNameAsIds", layoutBeanObj.getParamAsIds(reportToolsLinksParamNameAsIds));
		pageContext.setAttribute("howToReadReportLinkParamName1", howToReadReportLinkParamName1);
	}
%>


<c:if test="${not empty paramName}">
<table width="180" border="0" cellspacing="0" cellpadding="0" class="beigeBox">
  <tr>
    <td class="beigeBoxTD1">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">

<c:forEach items="${paramNameAsIds}" var="contentId"  varStatus="indexVal"> <%-- type="java.lang.Integer" --%>
<c:set var="temp" value="${contentId}"/>
<%Integer contentId = (Integer)pageContext.getAttribute("contentId"); %>
<fmt:parseNumber var="key" type="number" value="${temp}" />



          <content:contentBean contentId="${temp}"
                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                               id="contentBean" override="true"/>

          <% if (contentId.intValue() == ContentConstants.COMMON_PRINT_REPORT_TEXT) {
              href="javascript://";
 			        onclick="doPrint()";
           } else if ((contentId.intValue() == ContentConstants.COMMON_READ_THIS_REPORT_TEXT) ||
          				(contentId.intValue() == ContentConstants.COMMON_USE_THIS_PAGE_TEXT)) { %>
<c:if test="${not empty howToReadReportLinkParamName1}">
<%-- <c:set var="howToReadReportKey" value="${howToReadReportLinkParamName1}"/> --%>



 	                   <%
 	                   String ind = null;

 	                   if (contentId.intValue() == ContentConstants.COMMON_READ_THIS_REPORT_TEXT) {
			 	 			ind = "r";
			 	 		} else {
			 	 			ind = "p";
			 	 		}
 	                  String result = "javascript:doHowTo('" + howToReadReportLinkParamName1 + "','" + ind + "')";
 	                   href= result;
		   			   onclick="return true;";
 	                   %>
</c:if>
<c:if test="${empty howToReadReportLinkParamName1}">
	              <%
					href="#";
				    onclick="return true;";
				  %>
</c:if>
          <% } %>


          <tr>
            <td width="17">
			  <a href="<%= href %>" onclick="<%= onclick %>"><content:image id="contentBean" contentfile="image"/></a></td>
            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
            <td>
              <a href="<%= href %>" onclick="<%= onclick %>"><content:getAttribute id="contentBean" attribute="title"/></a>
            </td>
          </tr>
</c:forEach>
        <%-- CSP 28-31 User must have a specific permission to be able to download the census report. --%>
<c:if test="${employeeEnrollmentSummaryReportForm.allowedToDownloadCensus ==true}">
	        <%
	         	String extension = null;
	        	if (layoutBean.getParam("downloadExtension") == null) {
	        		extension = ".csv";
	        	} else {
	        		extension = "." + layoutBean.getParam("downloadExtension");
	        	}
	        	String result = "/do/census/censusSummary/?task=download&ext=" + java.net.URLEncoder.encode(extension);
	       	    href = result;
	            onclick = "return true;";
	        %>
	        <tr>
            <td width="17">
              <a href="<%= href %>" onclick="<%= onclick %>"><img src='/assets/generalimages/icon_excel.gif' border='0'></a></td>
            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
            <td>
              <a href="<%= href %>" onclick="<%= onclick %>">Download census report</a>
            </td>
          </tr>
</c:if>

<c:if test="${employeeEnrollmentSummaryReportForm.allowedToDownload ==true}">
 				<tr>
          <td width="17">
              <a href="?task=download&ext=.csv&reportType=allDetails" onclick="return true;"><img src='/assets/generalimages/icon_excel.gif' border='0'></a>
          </td>
          <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
          <td><a href="?task=download&ext=.csv&reportType=allDetails" onclick="return true;">Download eligibility report</a></td>
        </tr>
        <%-- The below jsp code is commented for the change in opt out CSV report --%>
       <%-- <% if(employeeEnrollmentSummaryReportForm.isShowOptOutReport()) { %>
          
           <tr>
            <td width="17">
                <a href="?task=download&ext=.csv&reportType=optOut" onclick="return true;"><img src='/assets/generalimages/icon_excel.gif' border='0'></a>
            </td>
            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
            <td><a href="?task=download&ext=.csv&reportType=optOut" onclick="return true;">Download 'post opt out date changes' report</a></td>
          </tr> 
        <% } %> --%>
         <% if(employeeEnrollmentSummaryReportForm.isEligibiltyCalcOn() || employeeEnrollmentSummaryReportForm.isEZstartOn() ){ %> 
       
        <ps:linkAccessible path="/do/census/employeeEligibilityReports/">
       	<tr>
       	<%
	         	
	        	String eligiblityReportsURL = "/do/census/employeeEligibilityReports/" ;
	       	    href = eligiblityReportsURL;
	            onclick = "return true;";
	        %>
           <td width="17">
               <a href="<%= href %>" onclick="return true;"><img src='/assets/generalimages/web_icon.gif' border='0'></a>
           </td>
           <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
           <td><a href="<%= href %>" onclick="return true;">View other eligibility reports</a></td>
         </tr> 
         </ps:linkAccessible>
          <% } %>
</c:if>
      </table>
    </td>
  </tr>
  <tr>
    <td align="right"><img src="/assets/unmanaged/images/box_lr_corner_E9E2C3.gif" width="5" height="5"></td>
   </tr>
 </table>

</c:if>

<%-- End of Report Tools Links --%>
