<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.participant.TaskCenterHistoryReportForm" %>
<%@ page import="org.apache.commons.lang.StringUtils" %> 
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.web.participant.TaskCenterHistoryReportForm" %>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TaskCenterHistoryReportData" %>        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.service.report.participant.transaction.valueobject.TaskCenterHistoryDetails" %>
		



<c:set var="theReport" value="${taskCenterHistoryReportForm.report}" />
<%TaskCenterHistoryReportData theReport=(TaskCenterHistoryReportData)pageContext.getAttribute("theReport"); %>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

TaskCenterHistoryReportForm taskCenterHistoryReportForm=(TaskCenterHistoryReportForm)session.getAttribute("taskCenterHistoryReportForm");
pageContext.setAttribute("taskCenterHistoryReportForm",taskCenterHistoryReportForm,PageContext.PAGE_SCOPE);
%>


<c:set var="enablePrint" value="${enablePrint}" />
<%Boolean enablePrint=(Boolean)pageContext.getAttribute("enablePrint"); %>


   </td>

   <td valign="top">			 

<style type="text/css">
H3 { page-break-after: always }
</style>

<% boolean firstToPrint=true; %>

<c:forEach items="${taskCenterHistoryReportForm.report.details}" var="theItem" varStatus="theIndex" >
<%TaskCenterHistoryDetails theItem=(TaskCenterHistoryDetails)pageContext.getAttribute("theItem"); %> 

<%
     if (theItem.isPrint()) {
    
       if (firstToPrint==false) { %>
          <h3>&nbsp;</h3> 
<%     } else { 
         firstToPrint=false; 
       } 
%>
      
<table width="760" border="0" cellspacing="0" cellpadding="0">       
      <tr>
        <td>Name</td>
        <td>
            <%=theItem.getName()%></br>
            <%=theItem.getSsn()%>        
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      
<% if (taskCenterHistoryReportForm.hasDivisionFeature()) { %>      
      <tr>
        <td>Division</td>
        <td><%=theItem.getDivision()%></td>
      </tr>      
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
<% }  %>
            
      <tr>
        <td>Type</td>
        <td><%=theItem.getType(theReport.getAutoOrSignup())%></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>Initiated date and how</td>
<td>${theItem.initiated}</br>
<% if (userProfile.isInternalUser()) { %>	            	 			          
           <%=theItem.getInitiatedSource()%></br>
           <%=theItem.getInitiatedByInternalView()%>
<% } else { %>
		   <%=theItem.getInitiatedSource()%></br>
		   <%=theItem.getInitiatedByExternalView()%>
<% } %>
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>Action Taken</td>
<td>${theItem.actionDate}</br>
<% if (userProfile.isInternalUser()) { %>	            	 			          
           <%=theItem.getProcessedSource()%></br>
           <%=theItem.getProcessedByInternal()%>
<% } else { %>
		   <%=theItem.getProcessedSource()%></br>
		   <%=theItem.getProcessedByExternal()%>
<% } %>                  
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>Details</td>
        <td><%=theItem.getDetails()%><br/><%=theItem.getRemarks()%></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
 
 </table>
 
	  <br></p>    
 
<% } %>
 
      
</c:forEach>


