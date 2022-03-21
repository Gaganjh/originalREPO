<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports" %>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.service.security.role.UserRole" %>
<%@ page import="com.manulife.pension.service.security.role.InternalUser" %>
<%@ page import="com.manulife.pension.service.security.role.ThirdPartyAdministrator" %>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.web.tools.util.CensusDetailsHelper"%>
<%-- Imports --%>
<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.ps.web.pagelayout.LayoutBean" />


<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>


<%
CensusDetailsHelper censusDetailsHelper = (CensusDetailsHelper)session.getAttribute(Constants.CENSUS_DETAILS_HELPER);
pageContext.setAttribute("censusDetailsHelper",censusDetailsHelper,PageContext.PAGE_SCOPE);
%>

<%
	Environment environment = (Environment)session.getAttribute(Constants.ENVIRONMENT_KEY);
	pageContext.setAttribute("ENVIRONMENT_KEY",environment,PageContext.SESSION_SCOPE); 
	
  
    String SITEMODE_USA=Constants.SITEMODE_USA;
    pageContext.setAttribute("SITEMODE_USA",SITEMODE_USA,PageContext.PAGE_SCOPE); 
%>

<%-- Start of Report Tools Links --%>

<%
	//default settings
	String reportToolsLinksParamName = "reportToolsLinks";
	String howToReadReportLinkParamName = "howToReadReportLink";
	String reportToolsLinksParamNameAsIds = "reportToolsLinks";

	String reportToolsLinksKey = "reportToolsLinks";
	String howToReadReportLinkKey = "howToReadReportLink";

	String href = null;
	String onclick = null;

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

	
	String howToReadReportLinkParamName1 = layoutBean.getParam(howToReadReportLinkParamName);
	pageContext.setAttribute("paramName", layoutBean.getParam(reportToolsLinksParamName));
	if(layoutBean.getParam(reportToolsLinksParamName) != null){
		pageContext.setAttribute("paramNameAsIds", layoutBean.getParamAsIds(reportToolsLinksParamNameAsIds));
		pageContext.setAttribute("howToReadReportLinkParamName1", howToReadReportLinkParamName1);
	}
	
	
	
%>


<c:if test="${not empty paramName}">
<table width="180" border="0" cellspacing="0" cellpadding="0" class="beigeBox">
  <tr>
    <td class="beigeBoxTD1">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
      
<c:set var="removeLink" value="false"/>
<%
String removeLink= pageContext.getAttribute("removeLink").toString();
%>
      	
<c:forEach items="${paramNameAsIds}" var="contentId" >
<%
Integer contentId = (Integer)pageContext.getAttribute("contentId");
%>





          <content:contentBean contentId="<%=contentId.intValue()%>"
                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                               id="contentBean" override="true"/>

<%
		   href="#";
		   removeLink = "false";
           if (contentId.intValue() == ContentConstants.COMMON_PRINT_REPORT_TEXT) {
             	href = "javascript://";
             	onclick = "doPrint()";
           } else if (contentId.intValue() == ContentConstants.COMMON_PRINT_REPORT_FOR_PARTICIPANT_TEXT) {
           		href = "javascript://";
             	onclick = "doPrint()";
           } else if (contentId.intValue() == ContentConstants.COMMON_DOWNLOAD_TO_CSV_TEXT) { %>
<c:if test="${censusDetailsHelper.allowedToDownload ==true}">
       		<%
                String extension = null;
            	if (layoutBean.getParam("downloadExtension") == null) {
            		extension = ".csv";
            	} else {
            		extension = "." + layoutBean.getParam("downloadExtension");
            	}
            	String res = "javascript:doDownload('" + java.net.URLEncoder.encode(extension) + "')";
          	    href = res;
          	    onclick = "return true;";
          	%>
</c:if>
<c:if test="${censusDetailsHelper.allowedToDownload ==false}">
		  	<%
 	            removeLink = "true";
 	        %>
</c:if>
       <%  } else if (contentId.intValue() == ContentConstants.COMMON_POPUP_GUIDE_LINK_TEXT) {
              onclick = "javascript:showPopupGuide()";

           } else if (contentId.intValue() == ContentConstants.COMMON_USER_GUIDE_TEXT) {
%>
<c:if test="${applicationScope.environment.siteLocation=='usa'}" >
<%
              		onclick = "javascript:PDFWindow('" + layoutBean.getParam("userGuideUSA") + " ')";
%>
</c:if>
<c:if test="${ENVIRONMENT_KEY.siteLocation !=SITEMODE_USA}">
<%
              		onclick = "javascript:PDFWindow('" + layoutBean.getParam("userGuideNY") + " ')";
%>
</c:if>
<%
           } else if ((contentId.intValue() == ContentConstants.COMMON_READ_THIS_REPORT_TEXT) ||
          				(contentId.intValue() == ContentConstants.COMMON_USE_THIS_PAGE_TEXT)) {
%>

<c:if test="${not empty howToReadReportLinkParamName1}">
<c:set var="howToReadReportKey" value="${howToReadReportLinkParamName1}"/>
<%
String howToReadReportKey = pageContext.getAttribute("howToReadReportKey").toString();

%>


 	                   <%
 	                   String ind = null;

 	                   if (contentId.intValue() == ContentConstants.COMMON_READ_THIS_REPORT_TEXT) {
			 	 			ind = "r";
			 	 		} else {
			 	 			ind = "p";
			 	 		}
 	                   String result = "javascript:doHowTo('" + howToReadReportKey + "','" + ind + "')";
                  	   href = result;
				       onclick="return true";
					%>
</c:if>
<c:if test="${empty howToReadReportLinkParamName1}">
					<%
					    onclick="return true";
					%>
</c:if>
          <% } else {

				onclick="return true";

           } %>

		  <% if (removeLink.equals("false")) { %>
          <tr>
            <td width="17">
			  <a href="<%= href %>" onclick="<%= onclick %>"><content:image id="contentBean" contentfile="image"/></a></td>
            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
            <td>
              <a href="<%= href %>" onclick="<%= onclick %>"><content:getAttribute id="contentBean" attribute="title"/></a>
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
