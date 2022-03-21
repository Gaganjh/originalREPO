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
<%@ page import="com.manulife.pension.service.contract.valueobject.MoneyTypeVO" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.VestingParticipant" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.MoneyTypeHeader" %>
<%@ page import="com.manulife.pension.ps.service.submission.util.SubmissionErrorHelper" %>
<%@ page import="com.manulife.pension.service.contract.util.ServiceFeatureConstants" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.VestingDetailItem" %>
<%@ page import="com.manulife.pension.ps.web.tools.util.VestingDetailsHelper"%>
<%@ page import="com.manulife.pension.ps.service.report.submission.valueobject.VestingDetailsReportData" %>
<%@ page import="com.manulife.pension.ps.web.tools.VestingDetailsForm" %>
<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>
<%
VestingDetailsForm requestForm=(VestingDetailsForm)session.getAttribute("vestingDetailsForm");
%>
<c:set var="submissionItem" value="${theReport.vestingData}" />
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
VestingDetailsHelper vestingDetailsHelper = (VestingDetailsHelper)session.getAttribute(Constants.VESTING_DETAILS_HELPER);
pageContext.setAttribute("vestingDetailsHelper",vestingDetailsHelper,PageContext.PAGE_SCOPE);
%>


<c:set var="submissionItem" value="${theReport.vestingData}" />
<%
VestingDetailsReportData theReport = (VestingDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
VestingDetailItem submissionItem = (VestingDetailItem)theReport.getVestingData();
pageContext.setAttribute("submissionItem",submissionItem,PageContext.PAGE_SCOPE);

pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>




<% 
	int moneyTypesDisplayed = 2;
	int moneyTypeSize = 85;
	int paramsDisplayed = 2;
	int paramSize = 90;
	int fieldCount=vestingDetailsHelper.getParamFieldCount(submissionItem);
	int fieldsDisplayed=vestingDetailsHelper.getParamFieldsDisplayed(submissionItem,moneyTypesDisplayed,paramsDisplayed);
	int fieldSize=vestingDetailsHelper.getParamFieldSize(submissionItem,moneyTypeSize,paramSize);
	int fieldApplyLTPTCreditingSize = 0;
	int tableShrinkSize = 0;
	if(requestForm.isDisplayApplyLTPTCreditingField()){
		fieldApplyLTPTCreditingSize = 70;
	}
	if (fieldCount < fieldsDisplayed && theReport.getDetails() != null && theReport.getDetails().size() > 0) {
		tableShrinkSize = ((fieldsDisplayed - fieldCount) * fieldSize) - fieldApplyLTPTCreditingSize;
	}
	
	boolean maskSSN = requestForm.isMaskSSN();
	boolean fixedTableVisible = false;
	String style_scrolling="overflow:scroll; overflow-y:hidden; z-index:2; width:760px;"; 
	String style_fixed="overflow:hidden; z-index:3; visibility:hidden; position:absolute; width:572px;";
	boolean isMozilla = request.getHeader("User-Agent").toUpperCase().indexOf("GECKO") != -1;
	
%>

<c:if test="${not empty param.printFriendly }" >
<% style_scrolling="overflow:hidden;"; %>
</c:if>

<%if (theReport.getDetails() == null || theReport.getDetails().size() == 0 || fieldCount <= fieldsDisplayed) {%>
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
  <% if (fieldCount > fieldsDisplayed) { 
  	fixedTableVisible = true;						%>
  	scrollingTable = document.getElementById("scrollingTable");
  	fixedTable = document.getElementById("fixedTable");

  	if ( navigator.userAgent.toLowerCase().indexOf( 'gecko' ) != -1 ) {
     	fixedTable.style.overflow='-moz-scrollbars-none'; 
     	scrollingTable.style.overflow='-moz-scrollbars-horizontal';
  	}
  	
  	//alert(scrollingTable.offsetTop);
  	//alert(scrollingTable.offsetParent.offsetTop);
  	//alert(scrollingTable.offsetParent.offsetParent.offsetTop);
  	//alert(scrollingTable.offsetParent.offsetParent.offsetParent.offsetParent.offsetTop);
  	
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

function doCalendar(fieldName) {
	cal = new calendar(document.forms['vestingDetailsForm'].elements[fieldName]);
	cal.year_scoll = true;
	cal.time_comp = false;
	cal.popup();
}

</script>

<%-- START new vesting details table --%>
<a name="participantTable"/>

			<table width="<%=760 - tableShrinkSize%>" border="0" cellpadding="0" cellspacing="0">
<input type="hidden" name="calDate"/>
			<%-- create calendar object to validate entered dates --%> 
            <script type="text/javascript" >
				var cal = new calendar(document.forms['vestingDetailsForm'].elements['calDate'],null,null,null,null);
				cal.year_scroll = true;
				cal.time_comp = false;
			</script>
			
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
            				<ps:setReportPageSize editVesting="true"/>
            			<% } %>
            			</c:if>
            			</td>

					    <td width="10%" align="right" class="tableheadTD">
				            <report:pageCounterViaSubmit  formName="vestingDetailsForm" report="theReport" arrowColor="white"/>
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
								<b><report:sortLinkViaSubmit formName="vestingDetailsForm" field="sourceRecordNo" direction="asc" anchor="participantTable">#</report:sortLinkViaSubmit></b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="85">
								<b><report:sort  formName="vestingDetailsForm" field="ssn" direction="asc">SSN</report:sort></b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="65">
								<b><report:sort formName="vestingDetailsForm" field="empId" direction="asc">Employee ID</report:sort></b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="110">
								<b>First name</b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="110">
								<b><report:sortLinkViaSubmit formName="vestingDetailsForm" field="name" direction="asc" anchor="participantTable">Last name</report:sortLinkViaSubmit></b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="30">
								<b>Middle<br>initial</b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td align="left" width="95"><b>Vesting<br>effective date</b></td>
						</tr>
						<%-- End Header Row --%>

                    

						<%-- Start detail row iteration --%>
<c:forEach items="${theReport.details}" var="participant" varStatus="partIndex" >
<c:set var="partIndex" value="${partIndex.index}"/> 
<%String partIndex = pageContext.getAttribute("partIndex").toString();
VestingParticipant participant = (VestingParticipant)pageContext.getAttribute("participant");%>
						<% if (Integer.parseInt(partIndex) % 2 == 0) { %>
							<tr class="datacell1">
						<% } else { %>
							<tr class="datacell2">
						<% } %>
								<td align="center" nowrap width="35">
									<span class="content">
<c:if test="${participant.ignored ==false}">
<form:checkbox name="vestingDetailsForm" path="deleteBoxesMap[${partIndex}]" onclick='<%= "updateParticipantCountForDelete(this, " + partIndex + ")"%>' value="true"/> <%-- name="form" --%>
										<ps:trackChanges name="vestingDetailsForm" property='<%= "deleteBoxesMap[" + partIndex + "]" %>'/>
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
										<report:errorIcon errors="<%=theReport%>" codes="F3,F6,P1,S1,S2,SZ,S3,C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.SSN_FIELD_KEY)%>"/>
										<%=vestingDetailsHelper.getSSN(participant,maskSSN)%>
<c:if test="${participant.ignored ==false}">
<input type="hidden" name="theReport.employerIds[${partIndex}]" /> <%-- name="form" --%>
<input type="hidden" name="theReport.recordNumbers[${partIndex}]" /> <%-- name="form" --%>
</c:if>
									</span>
								</td>
								<td class="datadivider" width="1"></td>
								<td align="left" nowrap width="65">
									<span class="content">
										<report:errorIcon errors="<%=theReport%>" codes="F1,F2,F4,F5,C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.EMPLOYEE_NUMBER_FIELD_KEY)%>"/>
${participant.empId}
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
								<td align="left" nowrap width="30">
									<span class="content">
										<report:errorIcon errors="<%=theReport%>" codes="C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.MIDDLE_INITIAL_FIELD_KEY)%>"/>
${participant.middleInitial}
									</span>
								</td>
								<td class="datadivider" width="1"></td>
								
								<td align="left" nowrap width="95">
								<report:errorIcon errors="<%=theReport%>" codes="VA,VB,VC,VD,VH,VI,C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.VESTING_DATE_KEY)%>"/>
								<c:if test="${empty param.printFriendly }" >
<c:if test="${participant.ignored ==false}">
<form:input path="vestingDatesMap[${partIndex}]"  maxlength="13" onchange='<%= "return validateVestingEffDateInputOnChange(this," + partIndex + ");" %>' onfocus="this.select()" size="7"/>
							
									<!-- <a href="javascript:doCalendar('<%= "vestingDates[" + partIndex + "]" %>',0);"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date"></a> -->
									<ps:trackChanges name="vestingDetailsForm" property='<%= "vestingDatesMap[" + partIndex + "]" %>' /> 
</c:if>
	            				</c:if>
								<c:if test="${not empty param.printFriendly }" >
<c:if test="${participant.ignored ==false}">
<form:input path="vestingDatesMap[${partIndex}]"  disabled="true" maxlength="13" size="7"/>
</c:if>
								</c:if>
<c:if test="${participant.ignored ==true}">
									<%=vestingDetailsHelper.formatVestingDateForWeb(participant,ServiceFeatureConstants.PROVIDED)%>
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
							<b><report:sortLinkViaSubmit formName="vestingDetailsForm" field="sourceRecordNo" direction="asc" anchor="participantTable">#</report:sortLinkViaSubmit></b>
						</td>
						<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
						<td width="85">
							<b><report:sort  formName="vestingDetailsForm" field="ssn" direction="asc">SSN</report:sort></b>
						</td>
						<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
						<td width="65">
							<b><report:sort formName="vestingDetailsForm" field="empId" direction="asc">Employee ID</report:sort></b>
						</td>
						<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
						<td width="110">
							<b>First name</b>
						</td>
						<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
						<td width="110">
							<b><report:sortLinkViaSubmit formName="vestingDetailsForm" field="name" direction="asc" anchor="participantTable">Last name</report:sortLinkViaSubmit></b>
						</td>
						<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
						<td width="30">
							<b>Middle<br>initial</b>
						</td>
						
						
<c:if test="${vestingDetailsForm.displayApplyLTPTCreditingField eq true}">
<c:if test="${empty submissionItem.tpaSystemName}">
<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
	<td align="left" width="60">
	<b>Apply LTPT Crediting</b>
	</td>
</c:if>
</c:if>						
						
						<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
						<td align="left" width="95">
<c:if test="${submissionItem.vestingCSF == 'TPAP'}">
							<b>Vesting<br>effective date</b>
</c:if>

<c:if test="${submissionItem.vestingCSF == 'JHC'}">
<c:if test="${empty submissionItem.tpaSystemName}">
							<b>VYOS Date</b>
</c:if>
<c:if test="${not empty submissionItem.tpaSystemName}">
							<b>Vesting<br>effective date</b>
</c:if>
</c:if>
						</td>
						
<c:if test="${submissionItem.vestingCSF == 'TPAP'}">
<c:forEach items="${submissionItem.percentageMoneyTypes}" var="moneyTypeHeader">
							<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
<c:set var="moneyType" value="${moneyTypeHeader.moneyType}" />
<%MoneyTypeVO moneyType = (MoneyTypeVO)pageContext.getAttribute("moneyType");%>
<c:set var="identifier" value="${moneyTypeHeader.moneyTypeId}"/>
<%String identifier = (String)pageContext.getAttribute("identifier");%>
							<td align="right" width="<%=moneyTypeSize%>">
								<report:errorIcon errors="<%=theReport%>" codes="M9,MV,C7,C4" value="<%=identifier.toString()%>"  />
<b title="${moneyType.contractLongName}">
								<%=moneyType.getVestingName(true)%>
								</b>
							</td>
</c:forEach>
</c:if>
<c:if test="${submissionItem.vestingCSF == 'JHC' }">
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td align="left" nowrap width="<%=paramSize%>"><b>Vested years<br>of service</b></td>
</c:if>
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

									<ps:trackChanges name="vestingDetailsForm" property='<%= "deleteBoxesMap[" + partIndex + "]" %>'/>
								</span>
</c:if>
								<% } %>
							</td>
							<td class="datadivider" width="1"></td>
							<td align="center" nowrap width="20">
							    <% 	VestingParticipant participant = (VestingParticipant)pageContext.getAttribute("participant");
							    int recordNumber = participant.getRecordNumber();
							       if (recordNumber < 0) recordNumber = recordNumber * -1; %>
								<span class="content">
									<%=recordNumber%>
								</span>
							</td>
							<td class="datadivider" width="1"></td>
							<td align="left" nowrap width="85">
								<span class="content">
									<report:errorIcon errors="<%=theReport%>" codes="F3,F6,P1,S1,S2,SZ,S3,C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.SSN_FIELD_KEY)%>"/>
									<%=vestingDetailsHelper.getSSN(participant,maskSSN)%>
									<% if (!fixedTableVisible) { %>
<c:if test="${participant.ignored ==false}">
<input type="hidden" name="theReport.employerIds[${partIndex}]"/> <%-- name="form" --%>
<input type="hidden" name="theReport.recordNumbers[${partIndex}]"/> <%-- name="form" --%>
</c:if>
									<% } %>
								</span>
							</td>
							<td class="datadivider" width="1"></td>
							<td align="left" nowrap width="65">
								<span class="content">
									<report:errorIcon errors="<%=theReport%>" codes="F1,F2,F4,F5,C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.EMPLOYEE_NUMBER_FIELD_KEY)%>"/>
${participant.empId}
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
							<td align="left" nowrap width="30">
								<span class="content">
									<report:errorIcon errors="<%=theReport%>" codes="C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.MIDDLE_INITIAL_FIELD_KEY)%>"/>
${participant.middleInitial}
								</span>
							</td>
<c:if test="${vestingDetailsForm.displayApplyLTPTCreditingField eq true}">
<c:if test="${empty submissionItem.tpaSystemName}">			
							<td class="datadivider" width="1"></td>
							<td align="left" nowrap width="60">
							<% if (!fixedTableVisible) { %>

								<report:errorIcon errors="<%=theReport%>" codes="AL,RA" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.APPLY_LTPT_CREDITING_KEY)%>"/>

							<c:if test="${empty param.printFriendly }" >
<c:if test="${participant.ignored ==false}">
<form:input path="applyLTPTCreditingsMap[${partIndex}]"  maxlength="13" onchange='<%= "return validateApplyLTPTCreditingInputOnChange(this," + partIndex + ");" %>' onfocus="this.select()" size="8"/>
							
								<ps:trackChanges name="vestingDetailsForm" property='<%= "applyLTPTCreditingsMap[" + partIndex + "]" %>' /> 
</c:if>
            				</c:if>
							<c:if test="${not empty param.printFriendly }" >
<c:if test="${participant.ignored ==false}">
<form:input path="applyLTPTCreditings${partIndex}]"  disabled="true" maxlength="13" size="8"/>
</c:if>
							</c:if>
<c:if test="${participant.ignored ==true}">
								<%=vestingDetailsHelper.checkApplyLTPTCreditingForWeb(participant)%>
</c:if>
							<% } %>
							</td>
</c:if>
</c:if>							
							<td class="datadivider" width="1"></td>
							
							<td align="right" nowrap width="95">
							<% if (!fixedTableVisible) { %>
<c:if test="${submissionItem.vestingCSF == 'TPAP'}">
								<report:errorIcon errors="<%=theReport%>" codes="VA,VB,VC,VD,VH,VI,C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.VESTING_DATE_KEY)%>"/>
</c:if>
<c:if test="${submissionItem.vestingCSF == 'JHC'}">
								<report:errorIcon errors="<%=theReport%>" codes="VB,VC,PT,PW,VD,VH,VI,C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.VESTED_YEARS_OF_SERVICE_EFF_DATE_KEY)%>"/>
</c:if>
							<c:if test="${empty param.printFriendly }" >
<c:if test="${participant.ignored ==false}">
<form:input path="vestingDatesMap[${partIndex}]"  maxlength="13" onchange='<%= "return validateVestingEffDateInputOnChange(this," + partIndex + ");" %>' onfocus="this.select()" size="8"/>
								<!-- <a href="javascript:doCalendar('<%= "vestingDates[" + partIndex + "]" %>',0);"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date"></a> -->
								<ps:trackChanges name="vestingDetailsForm" property='<%= "vestingDatesMap[" + partIndex + "]" %>' /> 
</c:if>
            				</c:if>
							<c:if test="${not empty param.printFriendly }" >
<c:if test="${participant.ignored ==false}">
<form:input path="vestingDates${partIndex}]"  disabled="true" maxlength="13" size="8"/>
</c:if>
							</c:if>
<c:if test="${participant.ignored ==true}">
								<%=vestingDetailsHelper.formatVestingDateForWeb(participant,submissionItem.getVestingCSF())%>
</c:if>
							<% } %>
							</td>
<c:if test="${submissionItem.vestingCSF == 'TPAP'}">
<c:forEach items="${submissionItem.percentageMoneyTypes}" var="moneyTypeHeader" varStatus="colIndex" >
<c:set var="colIndex" value="${colIndex.index}"/> 
<%String colIndex = pageContext.getAttribute("colIndex").toString();%>
<%
MoneyTypeHeader moneyTypeHeader =(MoneyTypeHeader)pageContext.getAttribute("moneyTypeHeader");
%>
<c:set var="moneyType" value="${moneyTypeHeader.moneyType}"/>
<c:set var="fullyVested" value="${moneyType.fullyVested}"/>
<%String fullyVested = (String)pageContext.getAttribute("fullyVested");%>
								<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
								<td align="right" nowrap width="<%=moneyTypeSize%>">
									<report:errorIcon errors="<%=theReport%>" codes="VP,VR,VT,VV,C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" value="<%=moneyTypeHeader.getKey()%>"/>
<c:if test="${participant.ignored ==false}">
<form:input 
	maxlength="13" 
	style="{text-align: right}" 
	size="5" 
	name="form" 
	path="vestingColumnsMap[${colIndex }].row[${partIndex}]" 
	onchange='<%= "return validateParticipantVestingInputOnChange(this," + partIndex + ",\'" + fullyVested + "\');" %>' 
	onfocus="this.select()";/>%	

			
		
								
										<ps:trackChanges name="vestingDetailsForm" property='<%= "vestingColumnsMap[" + colIndex + "]" + ".row[" + partIndex + "]" %>'/> 
									
								
</c:if>
<c:if test="${participant.ignored ==true}">
										<report:amount amount="<%= (String)participant.getMoneyTypePercentages().get(moneyTypeHeader.getKey()) %>" errors="<%=theReport%>" codes="VP,VR,VT,VV,C3,A2" row="<%=String.valueOf(participant.getRecordNumber())%>" value="<%=moneyTypeHeader.getKey()%>"/>
</c:if>
								</td>
</c:forEach>
</c:if>
<c:if test="${submissionItem.vestingCSF == 'JHC'}">
								<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
								<td align="right" width="<%=paramSize%>">
									<report:errorIcon errors="<%=theReport%>" codes="PC,PQ,PR,PU,VU,VX,C3,A2,VF" row="<%=String.valueOf(participant.getRecordNumber())%>" field="<%=SubmissionErrorHelper.getFieldLabel(SubmissionErrorHelper.VESTED_YEARS_OF_SERVICE_KEY)%>"/>
<c:if test="${participant.ignored ==false}">
<form:input path="vestedYearsOfServiceMap[${partIndex}]"  maxlength="13" onchange='<%= "return validateVestedYearsOfServiceInputOnChange(this," + partIndex + ");" %>' onfocus="this.select()" size="7"/>                           
                                    	<ps:trackChanges name="vestingDetailsForm" property='<%= "vestedYearsOfServiceMap[" + partIndex + "]" %>' />
</c:if>
<c:if test="${participant.ignored ==true}">
${participant.vyos}&nbsp;&nbsp;&nbsp;
</c:if>
								</td>
</c:if>

						</tr>
</c:forEach>
					<% } else { %>
						<tr>
						<td colspan="7" align="center">
							<content:getAttribute beanName="noVestingRecordsFound" attribute="text" filter="true"/>
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
			  	<report:pageCounterViaSubmit formName="vestingDetailsForm" report="theReport" arrowColor="black"/>
			  </td>
			</tr>
			</table>
		

		<%
		// remove the copyids (used to generate warning icons for rows with removed participant/loan combos) from session as they are no longer needed
		if ( session != null ) {
			session.removeAttribute(Constants.COPY_IDS);
		}
		%>
	<%-- END new vesting details table --%>

