<%-- taglib used --%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="notifications" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.participant.ParticipantSummaryReportForm" %>
<%@ page import="com.manulife.pension.ps.web.census.CensusSummaryReportForm" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
	
	pageContext.setAttribute("MAX_NUM_PART_FOR_SELECT_LIST",String.valueOf(Constants.MAX_NUM_PART_FOR_SELECT_LIST),PageContext.SESSION_SCOPE); 
	
	
	CensusSummaryReportForm censusSummaryReportForm = (CensusSummaryReportForm)session.getAttribute("censusSummaryReportForm");
	pageContext.setAttribute("censusSummaryReportForm",censusSummaryReportForm,PageContext.PAGE_SCOPE);
	
	
	
%>

<%-- This jsp is included as part of the secureHomePage.jsp --%>
<c:set var="employeeList" value="${contractSummary.employees}" />
<c:set var="searchResults" value="${employeeList.employees}"/>
<c:if test ="${not empty participantSummaryReportForm }" >
	<%
		// reset the participant summary form -- this way the old criteria
		// does not get carried with the new last name search criteria
		((ParticipantSummaryReportForm)pageContext.findAttribute("participantSummaryReportForm")).clear();
	%>

</c:if>
<%-- This jsp includes the following CMA content --%>
<content:contentBean contentId="<%=ContentConstants.PS_PARTICIPANTS_EMPLOYEES_SECTION_TITLE%>"
									type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
									beanName="Participants_Employees_Section_Title"/>

<content:contentBean contentId="<%=ContentConstants.PS_PARTICIPANTS_SECTION_ADDRESS_DESCRIPTION%>"
									type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
									beanName="Participants_Section_Address_Description"/>

<content:contentBean contentId="<%=ContentConstants.PS_PARTICIPANTS_SECTION_VIEW_ALL_TEXT%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="viewAllParticipantsText"/>

<content:contentBean contentId="<%=ContentConstants.PS_CENSUS_SECTION_VIEW_ALL_TEXT%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="viewAllEmployeesText"/>

<content:contentBean contentId="<%=ContentConstants.PS_GOTO_EMPLOYEE_SNAPSHOT_TEXT%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="gotoEmployeeSnapshotText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOTAL_ACCOUNT_HOLDERS_TEXT%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="totalAccountHoldersText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOTAL_NONACCOUNT_HOLDERS_TEXT%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="totalNonAccountHoldersText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOTAL_EMPLOYEES_TEXT%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="totalEmployeesText"/>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_ACCOUNT_HOLDERS_HIGHLIGHTED_TEXT%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="highlightedText"/>


<SCRIPT type="text/javascript">
function submitform()
{
	var theForm = document.getElementsByName("censusSummaryReportForm")[0];
	var url = theForm.action;
	var value = document.getElementsByName("namePhrase")[0].value;
	if (value.length > 0) {
		url += "?task=filter&fromContractHome=true&namePhrase=" + value;
	}
	location.href = url;

}

</SCRIPT>
<table width="240" border="0" cellspacing="0" cellpadding="0" class="box">
              <tr>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="238"><img src="/assets/unmanaged/images/s.gif" width="238" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              </tr>
              <tr class="tablehead">
                <td colspan="3" class="tableheadTD1"><b><content:getAttribute beanName="Participants_Employees_Section_Title" attribute="title"/></b></td>
              </tr>
              <tr>
                <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="boxbody">
	                <table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr>
							<td><content:getAttribute id="totalAccountHoldersText" attribute="text"/> </td>
<td align="right"><b class="highlight">${employeeList.totalParticipantCount}</b></td>
						</tr>
						<tr>
							<td><content:getAttribute id="totalNonAccountHoldersText" attribute="text"/> </td>
<td align="right"><b class="highlight">${employeeList.totalNonParticipantCount}</b></td>
						</tr>
						<tr>
							<td><content:getAttribute id="totalEmployeesText" attribute="text"/> </td>
<td align="right"><b class="highlight">${employeeList.totalEmployeeCount}</b></td>
						</tr>
					</table>
				<br>
			<ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >
	<%-- CMA managed--%>
			<content:getAttribute id="viewAllParticipantsText" attribute="text"/>
<!--    	<a href="/do/participant/participantSummary">View account summary</a> of all account holders. -->
				  <br>
			<content:getAttribute id="viewAllEmployeesText" attribute="text"/>
<!--	 	<a href="/do/census/censusSummary/">View census summary</a> of all employees. -->
                  <br>
                  <br>
                  <c:if test="${employeeList.totalEmployeeCount le MAX_NUM_PART_FOR_SELECT_LIST}">
	            <content:getAttribute id="highlightedText" attribute="text"/>
	              <br>
	              <br>
				<content:getAttribute id="gotoEmployeeSnapshotText" attribute="text"/>
	<!--	 	Select a record and you will be taken to the Employee Snapshot page. -->
	              <br>
            </c:if>
	     		    <p>
					<ps:form method="GET" action="/do/census/censusSummary/" modelAttribute="censusSummaryReportForm" name="censusSummaryReportForm" onsubmit="javascript:submitform();return false;">
						<b>Last name</b><br/>
				        <input type="text" name="namePhrase" style="inputField"/>
				        &nbsp;&nbsp;<a href="javascript:submitform()">Search</a>
	        		</ps:form>
                	</p>
                	
                	 <c:if test="${employeeList.totalEmployeeCount le MAX_NUM_PART_FOR_SELECT_LIST}">
                  <c:if test="${employeeList.totalEmployeeCount gt '0'}">
                  <table border="0" cellspacing="0" cellpadding="0" width="235">
                    <tr>
                      <td align="left">
                      	<%-- the OnChange calls the goToEmployeeSnapshot() function and passes the forwarding URL and value --%>
                      	<% String recordClass="datacell1";%>
<form:select path="homePageForm" onchange="goToEmployeeSnapshot(this)" size="18" ><%-- CCAT: Extra attributes for tag form:select - property="selectedParticipant" style="participantList" --%>
<c:forEach items="${searchResults}" var="employeeItem" >
<c:if test="${employeeItem.participant ==true}">
								<% recordClass="datacell2"; %>
</c:if>
<c:if test="${employeeItem.participant ==false}">
								<% recordClass="datacell1"; %>
</c:if>
<form:option cssClass="<%=recordClass%>" value="${employeeItem.profileId}">${employeeItem.wholeName}</form:option>
</c:forEach>
</form:select>
                        <br>
                      </td>
                    </tr>
                  </table>
                  <br/>
                  </c:if>
                  </c:if>
                  <content:getAttribute beanName="Participants_Section_Address_Description" attribute="text"/><br>
                  <c:if test="${not empty userProfile.currentContract}">
<c:if test="${userProfile.currentContract.mta ==false}">
                      	<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_HOW_TO_ENROLL_PARTICIPANTS_LINK%>"
                                  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                  id="howToEnroll"/>
                      	<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_SNAPSHOT_ENCOURAGE_ENROLLMENT_LINK%>"
                                  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                  id="howToEncourage"/>
						<content:getAttribute beanName="howToEnroll" attribute="text"/><br/>
						<content:getAttribute beanName="howToEncourage" attribute="text"/><br/>
</c:if>
	     		    </c:if>
	     		  <br/>
	     		  </ps:isNotJhtc>
                </td>
                <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                
              </tr>
              <tr>
                <td colspan="3">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
                      <td rowspan="2"  width="5"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
                    </tr>
                    <tr>
                      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
                  </table>
                  
                </td>
              </tr>
</table>
