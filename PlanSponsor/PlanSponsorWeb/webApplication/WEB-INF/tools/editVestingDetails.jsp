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
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.tools.SubmissionPaymentForm" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.VestingDetailItem"%>
<%@ page import="com.manulife.pension.ps.web.tools.util.VestingDetailsHelper"%>
<%@ page import="com.manulife.pension.ps.service.report.submission.valueobject.VestingDetailsReportData"%>
<%@ page import="com.manulife.pension.ps.web.tools.VestingDetailsForm"%>


<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>

<content:contentBean contentId="<%=ContentConstants.WARNING_DISCARD_CHANGES%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="warningDiscardChanges"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_CANCEL_SUBMISSION%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="warningCancelSubmission"/>
<content:contentBean contentId="<%=ContentConstants.NO_CHANGES_MADE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageNoChangesMade"/>
<content:contentBean contentId="<%=ContentConstants.VESTING_DETAILS_NO_RECORDS_FOUND%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="noVestingRecordsFound"/>
<content:contentBean contentId="<%=ContentConstants.INVALID_HISTORICAL_CSF%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="invalidHistoricalCSF"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_NOT_AUTHORIZED_CREATE_CONTRIB%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageContractStatusNotAuth"/>
<content:contentBean contentId="<%=ContentConstants.MESSAGE_VESTING_INVALID_DATE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageInvalidDate"/>
<content:contentBean contentId="<%=ContentConstants.MESSAGE_VESTING_FUTURE_DATE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageDateInTheFuture"/>
<content:contentBean contentId="<%=ContentConstants.MESSAGE_VESTING_TOO_OLD_DATE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageDate24MonthsInThePast"/>
<content:contentBean contentId="<%=ContentConstants.MESSAGE_VESTING_INVALID_PERCENTAGE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageInvalidPerc"/>
<content:contentBean contentId="<%=ContentConstants.MESSAGE_VESTING_TOO_MANY_DECIMALS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageTooManyDecimals"/>
<content:contentBean contentId="<%=ContentConstants.MESSAGE_VESTING_INVALID_PERCENTAGE_FULLY_VESTED_MT%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageInvalidPercFullyVestedMT"/>
<content:contentBean contentId="<%=ContentConstants.MESSAGE_VESTING_VYOS_NOT_AN_INTEGER%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageVYOSNotAnInteger"/>
<content:contentBean contentId="<%=ContentConstants.MESSAGE_VESTING_INVALID_VYOS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageInvalidVYOS"/>
<content:contentBean contentId="<%=ContentConstants.MESSAGE_VESTING_INVALID_DATE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageInvalidText"/>
<content:contentBean contentId="<%=ContentConstants.MESSAGE_VESTING_INVALID_APPLY_LTPT_CREDITING_VALUE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageInvalidApplyLTPTCreditingValue"/>

<%-- The first form is needed for the page --%>
<%

UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
VestingDetailsHelper vestingDetailsHelper = (VestingDetailsHelper)session.getAttribute(Constants.VESTING_DETAILS_HELPER);
pageContext.setAttribute("vestingDetailsHelper",vestingDetailsHelper,PageContext.PAGE_SCOPE);
VestingDetailsReportData theReport = (VestingDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);


String STATUS_COMPLETE = vestingDetailsHelper.STATUS_COMPLETE;
pageContext.setAttribute("STATUS_COMPLETE", STATUS_COMPLETE);
%>

<%
VestingDetailsForm vestingDetailsForm=(VestingDetailsForm)session.getAttribute("vestingDetailsForm");
%>




<c:set var="submissionItem" value="${theReport.vestingData}" />


<c:set var="contract" value="${sessionScope.USER_KEY.currentContract}" scope="request" />
<c:set var="trackChanges" value="true" scope="request" />

<style type="text/css">
<!--
div.scroll {
	height: 200px;
	width: 350px;
	overflow: auto;
	border-style: none;
	background-color: #fff;
	padding: 8px;}
div.inline {
	display: inline; }
-->
</style>

<script type="text/javascript" >

	//formName == vestingDetailsForm

	function doReset()
	{
		var reportURL = new URL(window.location.href);
	    reportURL.setParameter('task','reset');
	    window.location.href = reportURL.encodeURL();
	}

	function isFormChanged() {
<c:if test="${vestingDetailsForm.hasChanged ==true}">
			return true;
</c:if>
<c:if test="${vestingDetailsForm.hasChanged !=true}">
			return changeTracker.hasChanged();
</c:if>
	}

	// common messsages
	var warningDiscardChanges = '<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>';
	var warningCancelSubmission = '<content:getAttribute beanName="warningCancelSubmission" attribute="text" filter="true"/>';
	var messageInvalidDate = '<content:getAttribute beanName="messageInvalidDate" attribute="text" filter="true"/>';
	var messageDateInTheFuture = '<content:getAttribute beanName="messageDateInTheFuture" attribute="text" filter="true"/>';
	var messageDate24MonthsInThePast = '<content:getAttribute beanName="messageDate24MonthsInThePast" attribute="text" filter="true"/>';
	var messageInvalidPerc = '<content:getAttribute beanName="messageInvalidPerc" attribute="text" filter="true"/>';
	var messageTooManyDecimals = '<content:getAttribute beanName="messageTooManyDecimals" attribute="text" filter="true"/>';
var messageDataCheckerWarnings = '${vestingDetailsForm.dialogWarningMessages} <%-- filter="true" --%>';
	var messageNoChangesMade = '<content:getAttribute beanName="messageNoChangesMade" attribute="text" filter="true"/>';
	var messageInvalidPercFullyVestedMT = '<content:getAttribute beanName="messageInvalidPercFullyVestedMT" attribute="text" filter="true"/>';
	var messageVYOSNotAnInteger = '<content:getAttribute beanName="messageVYOSNotAnInteger" attribute="text" filter="true"/>';
	var messageInvalidVYOS = '<content:getAttribute beanName="messageInvalidVYOS" attribute="text" filter="true"/>';
	var messageInvalidText = '<content:getAttribute beanName="messageInvalidText" attribute="text" filter="true"/>';
	var messageInvalidApplyLTPTCreditingValue = '<content:getAttribute beanName="messageInvalidApplyLTPTCreditingValue" attribute="text" filter="true"/>';

	
var numberOfRecords = ${submissionItem.numberOfRecords};
var isInternalUser=${userProfile.internalUser};
var systemStatus=${submissionItem.systemStatus};
	
	
<c:forEach items="${vestingDetailsForm.participantFieldObjectNamesForJavascript}" var="theItem" varStatus="theIndex">
var participantFieldsRow${theIndex.index} =${theItem}; 
</c:forEach>
	
<c:forEach items="${vestingDetailsForm.moneyTypeFullyVestedIndicatorsForJavascript}" var="theFlag" varStatus="theIndex">
var moneyTypeFullyVestedIndicator${theIndex.index} = ${theFlag};
</c:forEach>
	
	var currentDate=<%=vestingDetailsForm.getCurrentDateJavascriptObject()%>;
	
	registerTrackChangesFunction(isFormChanged);
	if (window.addEventListener) {
		window.addEventListener('load', protectLinks, false);
	} else if (window.attachEvent) {
		window.attachEvent('onload', protectLinks);
	}

</SCRIPT>
<content:errors scope="session"/>
<A NAME="TopOfPage"></A>&nbsp;
		  <ps:form action="/do/tools/editVesting/" modelAttribute="vestingDetailsForm" name="vestingDetailsForm" method="POST" onsubmit="return confirmSend();">
		  			
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="fixedTable">

          <tr>
          		<%-- column 1 indent --%>
				<td width="15" class="big">&nbsp;</td>
				
				<%-- column 2 summary --%>
				<td width="350" height="20" valign="top">
					
<form:hidden path="subNo"/>
<form:hidden path="showConfirmDialog"/>
<form:hidden path="ignoreDataCheckWarnings"/>
<form:hidden path="forwardFromSave"/>
<form:hidden path="vestingCSF" value="${vsetcsf}"/>
<form:hidden path="futurePlanYearEnd"/>
					<jsp:include page="vestingSummarySection.jsp" flush="true" />
					
					
					<br/>
					<br/>
                </td>

				<%-- column 3 column gap --%>
				<td width="15"><img src="/assets/unmanaged/images/s.gif" width="15" height="1" border="0"></td>
				
				<%-- column 4 HELPFUL HINT --%>
				<td width="350" align="left" valign="top">
					<c:if test="${empty param.printFriendly }" >
						<c:if test="${systemStatus!=STATUS_COMPLETE}"> 
						<report:submissionErrors errors="<%=theReport%>" vestingDetailsErrors="true" width="350" printFriendly="false" forceView="true"/>
</c:if>
						<c:if test="${systemStatus==STATUS_COMPLETE}">
						<report:submissionErrors errors="<%=theReport%>" vestingDetailsErrors="true" width="350" printFriendly="false" forceView="false"/>
</c:if>
					</c:if>
				</td>

			</tr>
          	<tr>
				<td width="15" class="big">&nbsp;</td>
				<td colspan="3">
						<br/>
						<br/>
						<c:if test="${not empty param.printFriendly }" >
							<c:if test="${systemStatus!=STATUS_COMPLET}">
							<report:submissionErrors errors="<%=theReport%>" vestingDetailsErrors="true" width="500" printFriendly="true" forceView="true"/>
</c:if>
							<c:if test="${systemStatus==STATUS_COMPLETE}">
							<report:submissionErrors errors="<%=theReport%>" vestingDetailsErrors="true" width="500" printFriendly="true" forceView="false"/>
</c:if>
						</c:if>
						<br/>
						<br/>
						<%-- START new vesting details table --%>
<c:if test="${vestingDetailsForm.noPermission ==true}">
						<table width="715" border="0" cellpadding="0" cellspacing="0">
						<tr>
						  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="344" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
						<tr class="tablehead" height="25">
						  <td class="tableheadTD1" colspan="4"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b></td>
						</tr>
						<tr class="datacell1">
							<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
							<td class="datacell1" colspan="2" >
								<span class="content">
<c:if test="${userProfile.beforeCAStatusAccessOnly ==true}">
										<content:getAttribute beanName="messageContractStatusNotAuth" attribute="text"/>
</c:if>
<c:if test="${userProfile.beforeCAStatusAccessOnly ==false}">
										You are not authorized to make submissions.
</c:if>
								</span>
							</td>
							<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
						<tr class="datacell1">
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td class="lastrow" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
						<tr class="datacell1">
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4" /></td>
						  <td class="lastrow"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td  colspan="2" rowspan="2" align="right" valign="bottom" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" /></td>
						</tr>
						<tr>
						  <td class="databorder" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
						</table>
</c:if>
		
<c:if test="${vestingDetailsForm.noPermission ==false}">
<c:if test="${vestingDetailsForm.viewMode ==false}">
							<jsp:include page="vestingEditSection.jsp" flush="true" />
</c:if>
<c:if test="${vestingDetailsForm.viewMode ==true}">
							<jsp:include page="vestingViewSection.jsp" flush="true" />
</c:if>
						<br/><br/>
						<c:if test="${empty param.printFriendly}" >
						<table width="700" border="0" cellpadding="0" cellspacing="0">
						<tbody>
				  		<tr>
				    
							<td witdh="20%" align="center">
<c:if test="${vestingDetailsForm.viewMode ==false}">
								<span class="content">
<input type="submit" class="button134" onclick="return doCancelWithValue(this);" value="undo" name = "task"/><%--  - property="task" --%>
								</span>
</c:if>
							</td>
							<td witdh="20%" align="center">
								<span class="content">
<input type="submit" class="button134" onclick="return doCancelWithValue(this);" value="submission history" name = "task" /><%--  - property="task" --%>
								</span>
							</td>
							<td witdh="20%" align="center">
<c:if test="${vestingDetailsForm.viewMode ==false}">
								<span class="content">
<input type="submit" id="saveButton" class="button134" onclick="return doSaveWithValue(this);" value="save"  name = "task"/><%--  - property="task" --%>
								</span>
</c:if>
							</td>
							<td witdh="20%" align="center">
<c:if test="${vestingDetailsForm.viewMode ==false}">
								<span class="content">
								<% if (vestingDetailsForm.getTheReport().getVestingData().getSystemStatus()!= null) {
									if (vestingDetailsForm.getTheReport().getVestingData().getSystemStatus().equals("14") || vestingDetailsForm.getTheReport().getVestingData().getSystemStatus().equals("97")) { %>
<input type="submit" id="submitButton" class="button134" onclick="return doSaveWithValue(this);" value="submit" name = "task"/><%--  - property="task" --%>
									<% } else { %>
<input type="submit" id="submitButton" class="button134" onclick="return doSaveWithValue(this);" value="re-submit" name = "task"/><%--  - property="task" --%>
									<% } %>
								<% } %>
								</span>
</c:if>
							</td>
							<td witdh="20%" align="center">
								<span class="content">
<input type="submit" class="button134" onclick="return doCancelWithValue(this);" value="cancel submission" name = "task"/><%--  - property="task" --%>
								</span>
							</td>
				  		</tr>
						</tbody>
						</table>
						</c:if>
</c:if>
			<%-- END new vesting details table --%>
					<br>
				<td>
			</tr>
			<tr>
				<td class="big">&nbsp;</td>
				<td colspan="3">&nbsp;</td>
			</tr>
	        
			<tr>
               <td height="20" width="15">&nbsp;</td>
               <td colspan="3">		 	   
					<p><content:pageFooter id="layoutPageBean"/></p>
			   </td>
 			</tr>	
			</table>
			
			</ps:form>        

<script>
	<!-- //
	initPage();
	//-->
</script>

