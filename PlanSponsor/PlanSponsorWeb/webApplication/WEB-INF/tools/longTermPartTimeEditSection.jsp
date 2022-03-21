<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.manulife.pension.platform.web.taglib.util.LabelValueBean" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.LongTermPartTimeParticipant" %>
<%@ page import="com.manulife.pension.ps.service.submission.util.SubmissionErrorHelper" %>
<%@ page import="com.manulife.pension.service.contract.util.ServiceFeatureConstants" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.LongTermPartTimeDetailItem" %>
<%@ page import="com.manulife.pension.ps.web.tools.util.LongTermPartTimeDetailsHelper"%>
<%@ page import="com.manulife.pension.ps.service.report.submission.valueobject.LongTermPartTimeDetailsReportData" %>
<%@ page import="com.manulife.pension.ps.web.tools.LongTermPartTimeDetailsForm" %>
<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>
<%
LongTermPartTimeDetailsForm requestForm=(LongTermPartTimeDetailsForm)session.getAttribute("longTermPartTimeDetailsForm");
%>
<c:set var="submissionItem" value="${theReport.longTermPartTimeData}" />
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
LongTermPartTimeDetailsHelper longTermPartTimeDetailsHelper = (LongTermPartTimeDetailsHelper)session.getAttribute(Constants.LONG_TERM_PART_TIME_DETAILS_HELPER);
pageContext.setAttribute("longTermPartTimeDetailsHelper",longTermPartTimeDetailsHelper,PageContext.PAGE_SCOPE);
%>


<c:set var="submissionItem" value="${theReport.longTermPartTimeData}" />
<%
LongTermPartTimeDetailsReportData theReport = (LongTermPartTimeDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
LongTermPartTimeDetailItem submissionItem = (LongTermPartTimeDetailItem)theReport.getLongTermPartTimeData();
pageContext.setAttribute("submissionItem",submissionItem,PageContext.PAGE_SCOPE);

pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>




<% 
	int tableShrinkSize = 0;
	if (theReport.getDetails() != null && theReport.getDetails().size() > 0) {
		tableShrinkSize = theReport.getDetails().size();
	}
	
	boolean maskSSN = requestForm.isMaskSSN();
	boolean fixedTableVisible = false;
	String style_scrolling="overflow:hidden; overflow-y:hidden; z-index:2; width:760px;"; 
	String style_fixed="overflow:hidden; z-index:3; visibility:hidden; position:absolute; width:572px;";
	boolean isMozilla = request.getHeader("User-Agent").toUpperCase().indexOf("GECKO") != -1;
	
%>

<c:if test="${not empty param.printFriendly }" >
<% style_scrolling="overflow:hidden;"; %>
</c:if>

<%if (theReport.getDetails() == null || theReport.getDetails().size() == 0) {%>
<% style_scrolling="overflow:hidden;"; %>
<% } %>

<script type="text/javascript">

var fixedTable;
var scrollingTable;

if (window.addEventListener) {
	window.addEventListener('load', init, false);
} else if (window.attachEvent) {
	window.attachEvent('onload', init);
} else if (document.getElementById)
	window.onload=init;
	
function init() {
<c:if test="${not empty theReport.details}">
  <c:if test="${empty param.printFriendly }" >
  <% if (tableShrinkSize == 0) { 
  	fixedTableVisible = true; %>
  	scrollingTable = document.getElementById("scrollingTable");
  	fixedTable = document.getElementById("fixedTable");

  	if ( navigator.userAgent.toLowerCase().indexOf( 'gecko' ) != -1 ) {
     	fixedTable.style.overflow='-moz-scrollbars-none'; 
     	scrollingTable.style.overflow='-moz-scrollbars-horizontal';
  	}
  	
  	fixedTable.style.top = scrollingTable.offsetTop + 
  						   scrollingTable.offsetParent.offsetTop +
  						   scrollingTable.offsetParent.offsetParent.offsetTop + 
  						   scrollingTable.offsetParent.offsetParent.offsetParent.offsetParent.offsetTop;
  						   
  	fixedTable.style.left = 8;
  	fixedTable.style.visibility = 'visible';
  <% } %>
  </c:if>
</c:if>
}

</script>

<%-- START new longTermPartTime details table --%>
<a name="participantTable"/>

			<table width="<%=760 - tableShrinkSize%>" border="0" cellpadding="0" cellspacing="0">
<input type="hidden" name="calDate"/>
			
			<tr class="tablehead" height="25">
				<td class="tableheadTD1" colspan="4">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					  <tr>
					    <td width="25%" class="tableheadTD">
							<b><content:getAttribute beanName="layoutPageBean" attribute="body3Header"/></b>
						</td>

						<td width="15%" class="tableheadTD" align="center">
        					<c:if test="${empty param.printFriendly }" >
        					<%if (theReport.getDetails() != null && theReport.getDetails().size() > 0) {%>
        						<a href="" onClick="doReset();return false;">Default Sort</a>
	        				<% } %>
	            			</c:if>
        				</td>

        				<td width="25%" align="left" class="tableheadTD">
        				<% if (theReport.getDetails() != null && theReport.getDetails().size() > 0) {%>
        					<b><report:recordCounter   report="theReport" label="Total Records "/></b>
        				<% } %>
        				</td>

						<td width="25%" align="left" class="tableheadTD">
        				<c:if test="${empty param.printFriendly }" >
        				<%if (theReport.getDetails() != null && theReport.getDetails().size() > 0) {%>
            				<ps:setReportPageSize editLongTermPartTimeDetails="true"/>
            			<% } %>
            			</c:if>
            			</td>

					    <td width="10%" align="right" class="tableheadTD">
				            <report:pageCounterViaSubmit  formName="longTermPartTimeDetailsForm" report="theReport" arrowColor="white"/>
				        </td>

					  </tr>
					</table>
				</td>
			</tr>
			</table>
			
	    	<% if (fixedTableVisible) { %>
	    	<div id="fixedTable" style="<%=style_fixed%>">
	    	<table width="572" border="0" cellpadding="0" cellspacing="0">
				<tr class="whiteborder">
				  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
				  <td width="570"><img src="/assets/unmanaged/images/s.gif" height="1" /></td>
				  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
				</tr>
				<tr class="datacell1">
				  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
				  <td class="whiteborder">
					<table witdh="100%" border="0" cellpadding="0" cellspacing="0">
						
						<%-- Start Header Row --%>
						<tr class="tablesubhead">
							<td align="center" width="35">
								<b>Delete</b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td align="center" width="20">
								<b><report:sortLinkViaSubmit formName="longTermPartTimeDetailsForm" field="sourceRecordNo" direction="asc" anchor="participantTable">#</report:sortLinkViaSubmit></b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="85">
								<b><report:sort  formName="longTermPartTimeDetailsForm" field="ssn" direction="asc">SSN</report:sort></b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="110">
								<b>First name</b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="110">
								<b><report:sortLinkViaSubmit formName="longTermPartTimeDetailsForm" field="name" direction="asc" anchor="participantTable">Last name</report:sortLinkViaSubmit></b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="110">
								<b>Middle Initial</b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td align="left" width="250"><b>LTPT Assessment Year</b></td>
						</tr>
						<%-- End Header Row --%>

                    

						<%-- Start detail row iteration --%>
<c:forEach items="${theReport.details}" var="participant" varStatus="partIndex" >
<c:set var="partIndex" value="${partIndex.index}"/> 
<%String partIndex = pageContext.getAttribute("partIndex").toString();
LongTermPartTimeParticipant participant = (LongTermPartTimeParticipant)pageContext.getAttribute("participant");%>
						<% if (Integer.parseInt(partIndex) % 2 == 0) { %>
							<tr class="datacell1">
						<% } else { %>
							<tr class="datacell2">
						<% } %>
								<td align="center" nowrap width="35">
									<span class="content">
<c:if test="${participant.ignored ==false}">
<form:checkbox name="longTermPartTimeDetailsForm" path="deleteBoxesMap[${partIndex}]" onclick='<%= "updateParticipantCountForDelete(this, " + partIndex + ")"%>' value="true"/> 
										<ps:trackChanges name="longTermPartTimeDetailsForm" property='<%= "deleteBoxesMap[" + partIndex + "]" %>'/>
</c:if>
									</span>
								</td>
								<td class="datadivider" width="1"></td>
								<td align="center" nowrap width="20">
								    <% int recordNumber = participant.getRecordNumber();
								       if (recordNumber < 0) recordNumber = recordNumber * -1; %>
									<span class="content">
										<%=recordNumber%>
									</span>
								</td>
								<td class="datadivider" width="1"></td>
								<td align="left" nowrap width="85">
									<span class="content">
										<report:errorIcon errors="<%=theReport%>" codes="IE,IR,F3,F6,P1,S1,S2,SZ,US,C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.SSN_FIELD_KEY)%>"/>
										<%=longTermPartTimeDetailsHelper.getSSN(participant,maskSSN)%>
<c:if test="${participant.ignored ==false}">
<input type="hidden" name="theReport.employerIds[${partIndex}]" /> <%-- name="form" --%>
<input type="hidden" name="theReport.recordNumbers[${partIndex}]" /> <%-- name="form" --%>
</c:if>
									</span>
								</td>
								<td class="datadivider" width="1"></td>
<td align="left" nowrap width="110" title="${participant.firstName}">
									<span class="content">
										<report:errorIcon errors="<%=theReport%>" codes="C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.FIRST_NAME_FIELD_KEY)%>"/>
${participant.firstNameTruncated}
									</span>
								</td>
								<td class="datadivider" width="1"></td>
<td align="left" nowrap width="110" title="${participant.lastName}">
									<span class="content">
										<report:errorIcon errors="<%=theReport%>" codes="L1,L2,L3,C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.LAST_NAME_FIELD_KEY)%>"/>
${participant.lastNameTruncated}
									</span>
								</td>
								<td class="datadivider" width="1"></td>
								<td align="left" nowrap width="110">
									<span class="content">
										<report:errorIcon errors="<%=theReport%>" codes="C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.MIDDLE_INITIAL_FIELD_KEY)%>"/>
${participant.middleInitial}
									</span>
								</td>
								<td class="datadivider" width="1"></td>
								
								<td align="left" nowrap width="250">
								<report:errorIcon errors="<%=theReport%>" codes="LT,IY,HR" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.LONG_TERM_PART_TIME_ASSESSMENT_YEAR_FIELD_KEY)%>"/>

   						  <c:if test="${empty param.printFriendly }" >
<c:if test="${participant.ignored ==false}">
<form:input path="longTermPartTimeAssessmentYearsMap[${partIndex}]"  maxlength="13" onchange='<%= "return validateParticipantLongTermPartTimeAssessmentYearInputOnChange(this," + partIndex + ");" %>' onfocus="this.select()" size="7"/>
							
									<ps:trackChanges name="longTermPartTimeDetailsForm" property='<%= "longTermPartTimeAssessmentYearsMap[" + partIndex + "]" %>' /> 
</c:if>
	            				</c:if>
								<c:if test="${not empty param.printFriendly }" >
<c:if test="${participant.ignored ==false}">
<form:input path="longTermPartTimeAssessmentYearsMap[${partIndex}]"  disabled="true" maxlength="13" size="7"/>
</c:if>
								</c:if>
<c:if test="${participant.ignored ==true}">
		<%=longTermPartTimeDetailsHelper.checkLongTermPartTimeAssessmentYearForWeb(participant)%>
</c:if>
								</td>
							
							</tr>
</c:forEach>
						<%-- End detail row iteration --%>

				  </table>
			  </td>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>
			<tr>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>
			</table>
			</div>
	    	<% } %>
			
			<div id="scrollingTable" style="<%=style_scrolling%>">
			<table width="<%=760 - tableShrinkSize%>" border="0" cellpadding="0" cellspacing="0">
			<tr class="whiteborder">
			  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td width="754"><img src="/assets/unmanaged/images/s.gif" height="1" /></td>
			  <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1" /></td>
			  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>
			<tr class="datacell1">
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td colspan="2" class="whiteborder">
				<table witdh="100%" border="0" cellpadding="0" cellspacing="0">

					<%if (theReport.getDetails() != null && theReport.getDetails().size() > 0) {%>
					
					<%-- Start Header Row --%>
					<tr class="tablesubhead">
						<td align="center" width="35">
							<b>Delete</b>
						</td>
						<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
						<td align="center" width="20">
							<b><report:sortLinkViaSubmit formName="longTermPartTimeDetailsForm" field="sourceRecordNo" direction="asc" anchor="participantTable">#</report:sortLinkViaSubmit></b>
						</td>
						<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
						<td width="85">
							<b><report:sort  formName="longTermPartTimeDetailsForm" field="ssn" direction="asc">SSN</report:sort></b>
						</td>
						<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
						<td width="110">
							<b>First name</b>
						</td>
						<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
						<td width="110">
							<b><report:sortLinkViaSubmit formName="longTermPartTimeDetailsForm" field="name" direction="asc" anchor="participantTable">Last name</report:sortLinkViaSubmit></b>
						</td>
						<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
						<td width="110">
							<b>Middle<br>initial</b>
						</td>
						<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
						<td align="left" width="284"><b>LTPT Assessment Year</b></td>
					</tr>
					<%-- End Header Row --%>

					<%-- Start detail row iteration --%>
<c:forEach items="${theReport.details}" var="participant" varStatus="partIndex" >
<c:set var="partIndex" value="${partIndex.index}"/> 
<%String partIndex = pageContext.getAttribute("partIndex").toString();%>
					<% if (Integer.parseInt(partIndex) % 2 == 0) { %>
						<tr class="datacell1">
					<% } else { %>
						<tr class="datacell2">
					<% } %>
							<td align="center" nowrap width="35">
								<% if (!fixedTableVisible) { %>
<c:if test="${participant.ignored ==false}">
								<span class="content">
<form:checkbox path="deleteBoxesMap[${partIndex}]" onclick='<%= "updateParticipantCountForDelete(this, " + partIndex + ")"%>' value="true" /><%-- name="form" --%>

									<ps:trackChanges name="longTermPartTimeDetailsForm" property='<%= "deleteBoxesMap[" + partIndex + "]" %>'/>
								</span>
</c:if>
								<% } %>
							</td>
							<td class="datadivider" width="1"></td>
							<td align="center" nowrap width="20">
							    <% 	LongTermPartTimeParticipant participant = (LongTermPartTimeParticipant)pageContext.getAttribute("participant");
							    int recordNumber = participant.getRecordNumber();
							       if (recordNumber < 0) recordNumber = recordNumber * -1; %>
								<span class="content">
									<%=recordNumber%>
								</span>
							</td>
							<td class="datadivider" width="1"></td>
							<td align="left" nowrap width="85">
								<span class="content">
									<report:errorIcon errors="<%=theReport%>" codes="IE,IR,F3,F6,P1,S1,S2,SZ,US,C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.SSN_FIELD_KEY)%>"/>
									<%=longTermPartTimeDetailsHelper.getSSN(participant,maskSSN)%>
									<% if (!fixedTableVisible) { %>
<c:if test="${participant.ignored ==false}">
<input type="hidden" name="theReport.employerIds[${partIndex}]"/> <%-- name="form" --%>
<input type="hidden" name="theReport.recordNumbers[${partIndex}]"/> <%-- name="form" --%>
</c:if>
									<% } %>
								</span>
							</td>
							<td class="datadivider" width="1"></td>
<td align="left" nowrap width="110" title="${participant.firstName}">
								<span class="content">
									<report:errorIcon errors="<%=theReport%>" codes="C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.FIRST_NAME_FIELD_KEY)%>"/>
${participant.firstNameTruncated}
								</span>
							</td>
							<td class="datadivider" width="1"></td>
<td align="left" nowrap width="110" title="${participant.lastName}">
								<span class="content">
									<report:errorIcon errors="<%=theReport%>" codes="L1,L2,L3,C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.LAST_NAME_FIELD_KEY)%>"/>
${participant.lastNameTruncated}
								</span>
							</td>
							<td class="datadivider" width="1"></td>
							<td align="left" nowrap width="110">
								<span class="content">
									<report:errorIcon errors="<%=theReport%>" codes="C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.MIDDLE_INITIAL_FIELD_KEY)%>"/>
${participant.middleInitial}
								</span>
							</td>
								<td class="datadivider" width="1"></td>
								
								<td align="left" nowrap width="255">
								<report:errorIcon errors="<%=theReport%>" codes="LT,IY,HR" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.LONG_TERM_PART_TIME_ASSESSMENT_YEAR_FIELD_KEY)%>"/>

   						  <c:if test="${empty param.printFriendly }" >
<c:if test="${participant.ignored ==false}">
<form:input path="longTermPartTimeAssessmentYearsMap[${partIndex}]"  maxlength="13" onchange='<%= "return validateParticipantLongTermPartTimeAssessmentYearInputOnChange(this," + partIndex + ");" %>' onfocus="this.select()" size="7"/>
							
									<ps:trackChanges name="longTermPartTimeDetailsForm" property='<%= "longTermPartTimeAssessmentYearsMap[" + partIndex + "]" %>' /> 
</c:if>
	            				</c:if>
								<c:if test="${not empty param.printFriendly }" >
<c:if test="${participant.ignored ==false}">
<form:input path="longTermPartTimeAssessmentYearsMap[${partIndex}]"  disabled="true" maxlength="13" size="7"/>
</c:if>
								</c:if>
<c:if test="${participant.ignored ==true}">
									<%=longTermPartTimeDetailsHelper.checkLongTermPartTimeAssessmentYearForWeb(participant)%>
</c:if>
								</td>

						</tr>
</c:forEach>
					<% } else { %>
						<tr>
						<td colspan="7" align="center">
						<script>
							document.write(noLTPTRecordsFound.bold());
						</script>
						</td>
						</tr>
					<% } %>
						<%-- End detail row iteration --%>

				  </table>
			  </td>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>
			<tr>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>
			</table>
			</div>
			
			<table width="<%=760 - tableShrinkSize%>" border="0" cellpadding="0" cellspacing="0">
			<tr>
			  <td colspan="4" align="right">
			  	<report:pageCounterViaSubmit formName="longTermPartTimeDetailsForm" report="theReport" arrowColor="black"/>
			  </td>
			</tr>
			</table>
		

		<%
		// remove the copyids (used to generate warning icons for rows with removed participant/loan combos) from session as they are no longer needed
		if ( session != null ) {
			session.removeAttribute(Constants.COPY_IDS);
		}
		%>
	<%-- END new longTermPartTime details table --%>
