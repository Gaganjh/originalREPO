<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="quickreports" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Imports --%>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.pagelayout.LayoutBean" %>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.service.security.role.UserRole" %>
<%@ page import="com.manulife.pension.service.security.role.InternalUser" %>
<%@ page import="com.manulife.pension.service.security.role.ThirdPartyAdministrator" %>
<%@ page import="com.manulife.pension.ps.web.controller.SecurityManager" %>
<%@ page import="com.manulife.pension.service.vesting.VestingConstants" %>
<%@page import="org.owasp.encoder.Encode"%>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
LayoutBean layoutBeanObj = (LayoutBean)request.getAttribute("layoutBean");
%>
<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.ps.web.pagelayout.LayoutBean" />

<jsp:useBean id="censusVestingReportForm" scope="session" type="com.manulife.pension.ps.web.census.CensusVestingReportForm" /> 



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
		// if vesting is TPA provided (Collection) vs. JHC calculated (Calculation)
		if (censusVestingReportForm.getVestingServiceFeature().equals(VestingConstants.VestingServiceFeature.COLLECTION)) {
            howToReadReportLinkParamName = "howToReadReportLinkTPAP";
            %>
            <c:if test="${censusVestingReportForm.isEditMode}">
              <%
                howToReadReportLinkParamName = "howToReadReportLinkTPAPEdit";
              %>
            </c:if>
            <%
		} else {
			howToReadReportLinkParamName = "howToReadReportLinkJHC";
		}
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
<c:set var="href" value="#"/>
<c:set var="onclick" value="return true;" />
<c:set var="removeLink" value="false"/>
<c:forEach items="${paramNameAsIds}" var="contentId" varStatus="indexVal"> 

<c:set var="contentId" value="${contentId}"/>
<%String contentId = pageContext.getAttribute("contentId").toString(); %>




          <content:contentBean contentId="<%=Integer.parseInt(contentId)%>"
                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                               id="contentBean" override="true"/>

		  <% removeLink = "false"; %>
          <% if (Integer.parseInt(contentId) == ContentConstants.COMMON_PRINT_REPORT_TEXT) { 
              	href = "javascript://";
                onclick = "doPrint()";
                // We don't display the print link in edit mode.
                if (censusVestingReportForm.getIsEditMode()) {
                    removeLink = "true";
                } // fi
          } else if (Integer.parseInt(contentId) == ContentConstants.COMMON_CENSUS_DOWNLOAD_TO_CSV_TEXT) { %>
           
		  	<!-- User must have a specific permission to be able to download the census report. -->
<c:if test="${censusVestingReportForm.allowedToDownloadCensus eq true}">
	        <% 
	         	String extension = null;
	        	if (layoutBean.getParam("downloadExtension") == null) {
	        		extension = ".csv";
	        	} else {
	        		extension = "." + layoutBean.getParam("downloadExtension");
	        	}
	        	String result = "/do/census/censusSummary/?task=download&ext=" + java.net.URLEncoder.encode(extension);
	        	
                if (StringUtils.isNotBlank(censusVestingReportForm.getStatus())) {
                    result += "&status=" + censusVestingReportForm.getStatus();
	        	} // fi
                if (StringUtils.isNotBlank(censusVestingReportForm.getNamePhrase())) {
                    result += "&namePhrase=" + censusVestingReportForm.getNamePhrase();
                } // fi
                if (StringUtils.isNotBlank(censusVestingReportForm.getSsnOne())) {
                    result += "&ssnOne=" + censusVestingReportForm.getSsnOne();
                } // fi
                if (StringUtils.isNotBlank(censusVestingReportForm.getSsnTwo())) {
                    result += "&ssnTwo=" +  censusVestingReportForm.getSsnTwo();
                } // fi
                if (StringUtils.isNotBlank(censusVestingReportForm.getSsnThree())) {
                    result += "&ssnThree=" + censusVestingReportForm.getSsnThree();
                } // fi
                if (StringUtils.isNotBlank(censusVestingReportForm.getAsOfDate())) {
                    result += "&asOfDate=" + Encode.forHtml(censusVestingReportForm.getAsOfDate());
                } // fi
                if (StringUtils.isNotBlank(censusVestingReportForm.getDivision())) {
                    result += "&division=" + censusVestingReportForm.getDivision();
                } // fi
                if (StringUtils.isNotBlank(censusVestingReportForm.getSegment())) {
                    result += "&segment=" + censusVestingReportForm.getSegment();
                } // fi

	        	href = result;
	            onclick = "return true;";
	        %>
</c:if>
<c:if test="${censusVestingReportForm.allowedToDownloadCensus eq false}">
		  	<% 
 	            removeLink = "true";	                   
 	        %> 	 
</c:if>
		  <% } else if (Integer.parseInt(contentId) == ContentConstants.COMMON_VESTING_DOWNLOAD_TO_CSV_TEXT) { %>
           
		  <!-- 	User must have a specific permission to be able to download the vesting report. -->
<c:if test="${censusVestingReportForm.allowedToDownloadVesting eq true}">
	        <% 
	         	String extension = null;
	        	if (layoutBean.getParam("downloadExtension") == null) {
	        		extension = ".csv";
	        	} else {
	        		extension = "." + layoutBean.getParam("downloadExtension");
	        	}
	        	String result = "/do/census/censusVesting/?task=download&ext=" + java.net.URLEncoder.encode(extension);
	       	    href = result;
	            onclick = "return true;";
	        %>
</c:if>
<c:if test="${censusVestingReportForm.allowedToDownloadVesting eq false}">
		  	<% 
 	            removeLink = "true";	                   
 	        %> 	 
</c:if>
		  <% } else if ((Integer.parseInt(contentId) == ContentConstants.COMMON_READ_THIS_REPORT_TEXT) ||
          				(Integer.parseInt(contentId) == ContentConstants.COMMON_USE_THIS_PAGE_TEXT)) { %>
<c:if test="${not empty howToReadReportLinkParamName1}" >
<c:set var="howToReadReportKey" value="${howToReadReportLinkParamName1}"/>
<%
String howToReadReportKey = pageContext.getAttribute("howToReadReportKey").toString();

%>



 	                   <%
 	                   String ind = null;

 	                   if (Integer.parseInt(contentId) == ContentConstants.COMMON_READ_THIS_REPORT_TEXT) {
			 	 			ind = "r";
			 	 		} else {
			 	 			ind = "p";
			 	 		}
 	                   String result = "javascript:doHowTo('" + howToReadReportKey + "','" + ind + "')";
 	               	   href = result;
 	                   onclick = "return true;"; 	                   
 	                   %>
</c:if>
<c:if test="${empty howToReadReportLinkParamName1}">
 	           <% 
 	           		   href = "#";
 	                   onclick = "return true;"; 	                   
 	           %> 	           
</c:if>
          <% }  %>


          <%
            // Allow the variables to be seen in the page context.
            pageContext.setAttribute("href", href);
            pageContext.setAttribute("onclick", onclick);
            pageContext.setAttribute("removeLink", removeLink);
          %>

          <c:if test="${removeLink == false}">
          <tr>
            <td width="17">
			  <a href="${href}" onclick="${onclick}"><content:image id="contentBean" contentfile="image"/></a></td>
            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
            <td>
              <a href="${href}" onclick="${onclick}"><content:getAttribute id="contentBean" attribute="text"/></a>
            </td>
          </tr>
          </c:if>
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
