<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<%-- Imports --%>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.security.valueobject.ManageUsersReportData" %>
<%@ page import="com.manulife.pension.ps.web.profiles.AccessLevelHelper" %>

<%@page import="com.manulife.pension.service.security.role.UserRole"%>
<%@page import="com.manulife.pension.service.security.role.NoAccess"%>
<%@page import="com.manulife.pension.service.security.role.IntermediaryContact"%>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_ACTIVE_USERS%>" 
			type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="noActiveUsers" />

<content:contentBean contentId="<%=ContentConstants.FR_TAB_ADVISORY_MESSAGE%>" 
			type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="advisoryMessage" />

<jsp:useBean class="java.lang.String" id="resultsMessageKey" scope="request"/>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
	
	ManageUsersReportData theReport = (ManageUsersReportData)request.getAttribute(Constants.REPORT_BEAN);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
	
	
%>



<jsp:useBean class="com.manulife.pension.ps.web.contacts.BrokerContactsReportForm" id="brokerContactsForm" scope="session"/>
 



<style type="text/css">
* html div#profileCommentsDIV {
	height: expression( this.scrollHeight > 18 ? "41px" : "auto" );
}
div#profileCommentsDIV {
	width:550px;
	min-height:10px;
/*	height: auto !important; */
	max-height: 41px;
	overflow:auto;
}

</style>
<script language=javascript type='text/javascript'>
	var submitted = false;
		
	// Register the event.
	if (window.addEventListener) {	
		window.addEventListener('load', protectLinksFromCancel, false);
	} else if (window.attachEvent) {	
		window.attachEvent('onload', protectLinksFromCancel);
	}	
</script>


<ps:form  method="POST" action="/do/contacts/broker/" name="brokerContactsForm" modelAttribute="brokerContactsForm">
 
<DIV id="errordivcs"><content:errors scope="session"/></DIV><br>

<c:set var="col_span" scope="page">
11
</c:set>
<%-- TAB section --%>
  <jsp:include page="ContactsTab.jsp" flush="true">
 	<jsp:param value="4" name="tabValue"/>
  	<jsp:param value="${tpaFirmAccessForContract}" name="tpaFirmAccessForContract"/>
  </jsp:include>
  
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="beigeborder2">
   	<table width="100%" border="0" cellspacing="0" cellpadding="5">    		
 
				<tr class="datacell1">
					<td colspan="${col_span}" class="beigeborder2">
 						<%-- Comments section --%>
						<table width="100%">
							<%-- Plan Sponsor Comments --%>
							<ps:isInternalUser name="userProfile" property="role">
								<tr>
									<td colspan="2">
										<div style="padding-bottom:4px">
											<b>Plan Sponsor Comments:</b>
											<b class="highlight">${contractComment.displayLastUpdatedUser} </b>
										</div>
									</td>
								</tr>
								<tr>
									<td width="20%">&nbsp;</td>
									<td width="80%" style="padding-bottom:20px">
										<c:if test="${empty param.printFriendly }" >
											<div style="width:100%;height:40px;overflow:auto;">
												${contractComment.commentUIText}
											</div>
										</c:if>
										<c:if test="${not empty param.printFriendly }" >
											<div style="width:100%;">
												${contractComment.commentUIText}
											</div>
										</c:if>
									</td>	
								</tr>
							</ps:isInternalUser>
							

						</table>
					</td>
				</tr>
				<%
					boolean riadesgflg = true;
				%>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >




					<c:if test="${theItem.riaDesignation338Ind}">
						<%
						if(riadesgflg){
							riadesgflg = false;
						%>
			 			<tr class="datacell1">
							<td colspan="11" >
								<table border="0">
									<tr>
										<td width="45" valign="top">
											<strong>Legend:</strong>
										</td>
										<td width="20" valign="top" title="RIA 3(38) Designate">
											<img src="/assets/unmanaged/images/icon_ria_338_designation.jpg">
										</td>
										<td width="55" valign="top">RIA 3(38) Designate</td>
									</tr>
								</table>					
							</td>
						</tr>
						<%
						 }
						%>
					</c:if>
</c:forEach>
</c:if>
			  
			<tr class="datacell1">
				<td height="25" colspan="11" class="tableheadTD">
					<b><content:getAttribute id="layoutPageBean" attribute="body1Header"/></b>
					<br>
					<content:getAttribute id="advisoryMessage" attribute="text"/>
				</td>
            </tr>
			
			<tr class="tablesubhead">
                <td width="142" class="pgNumBack">
					<b>
						<report:sort field="<%=ManageUsersReportData.SORT_FIELD_LAST_NAME%>" direction="asc" formName="brokerContactsForm">
							Contact name
						</report:sort>
					</b></td>
                <td class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="200" class="pgNumBack"><B>Email address </B></td>
                <td class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="200" class="pgNumBack"><B>Mailing Address </B></td>
                <td class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="150" class="pgNumBack"><B>Phone</B></td>
                <td class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="120" class="pgNumBack"><B>Fax</B></td>
                <td class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                <td width="120" class="pgNumBack"><B>Special Attributes</B></td>
            </tr>
			
			<%--   display a message if there are no users available for display --%>

<c:if test="${empty theReport.details}">
			<tr class="datacell1">
				
				<td colspan = "9"  valign="middle">
					<c:if test="${empty resultsMessageKey}">
						<content:getAttribute id="noActiveUsers" attribute="text"/>
</c:if>
								<c:if test="${not empty resultsMessageKey}">	
${resultsMessageKey} <%-- scope="request" --%>
</c:if>
				</td>
			</tr>
			
</c:if>
	
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >	


    <c:set var="indexValue" value="${theIndex.index}"/> 
				<% 				
				    String temp = pageContext.getAttribute("indexValue").toString();%>
				 <% if (Integer.parseInt(temp) % 2 == 0) { %>
				<tr class="datacell1">
						<% } else { %>
				<tr class="datacell2">
				<% } %>
					<td valign="center" >
						<render:name firstName="${theItem.getFirstName()}" lastName="${theItem.getLastName()}" style="f" defaultValue=""/>
					</td>
					<td valign="center" class="greyborder">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
					</td>
 
					<td valign="center">
<c:if test="${not empty theItem.email}">
						<c:if test="${empty param.printFriendly }" >
							<a href="mailto:${theItem.email}">
						</c:if>
								${theItem.email}
						<c:if test="${empty param.printFriendly }" >								
							</a>
						</c:if>
</c:if>
					</td>
					<td valign="center" class="greyborder">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
					</td>
					<td valign="center">
					<c:set var="commaCheck" value="false" />
								<c:if test="${not empty theItem.mailingAddrLine1}"> ${theItem.mailingAddrLine1} </c:if>
								<c:if test="${not empty theItem.mailingAddrLine2}"> <br> ${theItem.mailingAddrLine2} </c:if>
								<c:if test="${not empty theItem.mailingAddrLine3}"> <br> ${theItem.mailingAddrLine3} </c:if>

								<c:if test="${not empty theItem.mailingCityName}">
								  <c:set var="commaCheck" value="true" />
								</c:if>  
								
								<c:if test="${not empty theItem.mailingStateCode}">
								  <c:set var="commaCheck" value="true" />
								</c:if>  
								
								<c:if test="${commaCheck}">
								<br>
								</c:if>								
								<c:if test="${not empty theItem.mailingCityName}"> ${theItem.mailingCityName}  </c:if> <c:if test="${not empty theItem.mailingStateCode}">, ${theItem.mailingStateCode} </c:if>
								

								<c:if test="${not empty theItem.mailingZipCode}"> <br> ${theItem.mailingZipCode} </c:if>
					</td>
					<td valign="center" class="greyborder">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
					</td>
					<td valign="center">
					${theItem.mailingPhoneNumber}
					</td>
					<td valign="center" class="greyborder">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
					</td>
					<td valing="center" >
					${theItem.mailingFaxNumber}
					</td>
					<td valign="center" class="greyborder">
						<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
					</td>
					<c:choose>
						<c:when test="${theItem.riaDesignation338Ind}">
							<td valign="center" title="RIA 3(38) Designate">
								<img src="/assets/unmanaged/images/icon_ria_338_designation.jpg" />
							</td>
						</c:when>
						<c:otherwise>
							<td></td>
						</c:otherwise>
					</c:choose>
				</tr>
				
				<ps:isInternalUser name="userInfo" property="role">
				<c:if test="${not empty theItem.contactCommentText}">
					<tr>
		  				<td class="datadivider" colspan="11"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
					</tr>	
					<% if (Integer.parseInt(temp) % 2 == 0) { %>
					<tr class="datacell1">
							<% } else { %>
					<tr class="datacell2">
					<% } %>
	 
						<td valign="top">
							<strong>Comments</strong>
						</td>
						<td class="greyborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
 						<td colspan="7" >
							<b class="highlight">${theItem.contactComment.displayLastUpdatedUser}</b><br/>
							<div id="profileCommentsDIV">
									${theItem.contactCommentText}
							</div>
						</td>
					</tr>
				</c:if>
				</ps:isInternalUser>
			<tr>
		  		<td class="datadivider" colspan="11"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>

</c:forEach>
</c:if>
  
    	</table>
		</td>
		</tr>
		</table>
 </ps:form>
