<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="quickreports" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ page import="com.manulife.pension.ps.web.census.DeferralReportForm" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.pagelayout.LayoutBean" %>

<%-- Imports --%>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<jsp:useBean id="layoutBean" scope="request" type="com.manulife.pension.ps.web.pagelayout.LayoutBean" />











<jsp:useBean id="deferralReportForm" scope="session" type="com.manulife.pension.ps.web.census.DeferralReportForm" />

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
%>
<% 

    // has user accepted the Terms and Conditions of use yet?
    String disableLinks;
    if (deferralReportForm.isTermsAndConditionsAccepted()) {
    	disableLinks = "";
    } else {
    	disableLinks = "disabled";
    }
	
	//default settings
	String reportToolsLinksParamName = "reportToolsLinks";
	String howToReadReportLinkParamName = "howToReadReportLink";
	String reportToolsLinksParamNameAsIds = "reportToolsLinks";
	
	String reportToolsLinksKey = "reportToolsLinks";
	String howToReadReportLinkKey = "howToReadReportLink";
	
	//New key to hold the content id of GIFL version 3 or GIFL version 1& 2 reports
	String howToReadReportKey="howToReadReportKey";
	String href = null;
	String onclick = null;
    String linkId = null;
    String defhref=null;
    String style=null;
	
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
	if (!layoutBeanObj.getParams().containsKey(reportToolsLinksKey)){
		reportToolsLinksParamName = "reportToolsLinks";
		reportToolsLinksParamNameAsIds = "reportToolsLinks";
	}
	
	
	if (!layoutBeanObj.getParams().containsKey(howToReadReportLinkKey)){
		howToReadReportLinkParamName = "howToReadReportLink";
	}
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
<c:set var="defhref" value="?task=download&ext=.csv&reportType=allDetails" />
		    
		<%-- Shall specify the CSS style   --%> 
<c:set var="style" value="" />
	
	<%-- <logic:iterate id="contentId"
                       name="layoutBean"
                        property="<%=reportToolsLinksParamNameAsIds%>"
                       type="java.lang.Integer"> --%>
		        
<c:forEach items="${paramNameAsIds}" var="contentId" ><%-- type="java.lang.Integer" --%>
<%Integer temp=(Integer)pageContext.getAttribute("contentId");
%>



          <content:contentBean contentId="<%=temp.intValue()%>"
                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                               id="contentBean" override="true"/>
          
          <% if (temp.intValue() == ContentConstants.COMMON_PRINT_REPORT_TEXT) { 
        	  
              href="javascript://";
 			        onclick="doPrint()";
             } else if ((temp.intValue() == ContentConstants.COMMON_READ_THIS_REPORT_TEXT) ||
          				(temp.intValue() == ContentConstants.COMMON_USE_THIS_PAGE_TEXT)) { %>
          			
<c:if test="${not empty howToReadReportLinkParamName1}">


 	                   <%
 	                   String ind = null;

 	                   if (temp.intValue() == ContentConstants.COMMON_READ_THIS_REPORT_TEXT) {
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
          <% }
          
          if (deferralReportForm.isTermsAndConditionsAccepted()==false) {
          	onclick="return false;"; 
          	href = "#";
          	defhref = "#";
          	style = "disableAppearance";
          }
          %>
  		  
          <tr>
            <td width="17">
			  <a class = "<%=style%>" href="<%=href %>" onclick="<%=onclick%>" <%=disableLinks%>><content:image id="contentBean" contentfile="image"/></a></td>
            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
            <td>
              <a class = "<%=style%>" href="<%=href%>" onclick="<%=onclick%>" <%=disableLinks%>><content:getAttribute id="contentBean" attribute="title"/></a>
            </td>
          </tr>
</c:forEach>
        <%-- CSP 28-31 User must have a specific permission to be able to download the census report. --%>
<c:if test="${deferralReportForm.allowedToDownloadCensus ==true}">
	    	<% 
	         	String extension = null;
	        	if (layoutBeanObj.getParam("downloadExtension") == null) {
	        		extension = ".csv";
	        	} else {
	        		extension = "." + layoutBeanObj.getParam("downloadExtension");
	        	}
	        	String result = "/do/census/censusSummary/?task=download&ext=" + java.net.URLEncoder.encode(extension);
	       	    href = result;
	            onclick = "return true;";

	            if (deferralReportForm.isTermsAndConditionsAccepted()==false) {
	              	onclick="return false;";
	              	href = "#";
	              	defhref = "#";
	              	style = "disableAppearance";
	            }
	        %>
	        <tr>
	            <td width="17">
	              <a class="<%=style%>" href="<%=href%>"
								onclick="<%=onclick%>"><img
									src='/assets/generalimages/icon_excel.gif' border='0'></a>
							</td>
	            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
	            <td>
	              <a class = "<%=style%>" href="<%=href%>"  onclick="<%=onclick%>" >Download census report</a>
	            </td>
            </tr>
</c:if>
<c:if test="${deferralReportForm.allowedToDownload ==true}">
 			<tr>
              <td  width="17">
               <a class = "<%=style%>" href="${defhref}" onclick="<%=onclick%>" ><img src='/assets/generalimages/icon_excel.gif' border='0'></a>
              </td>
              <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
              <td><a class = "<%=style%>" href="${defhref}" onclick="<%=onclick%>">Download deferral report</a></td>
            </tr>            
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
