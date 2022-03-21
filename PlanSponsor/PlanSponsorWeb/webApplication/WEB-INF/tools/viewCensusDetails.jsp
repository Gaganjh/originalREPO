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
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.service.report.submission.valueobject.CensusSubmissionReportData" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.CensusSubmissionItem" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem" %>
<%@ page import="com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper" %>
<%@ page import="com.manulife.pension.ps.web.tools.util.CensusDetailsHelper" %>
<%@ page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem" %>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
SubmissionHistoryItem submissionCase = (SubmissionHistoryItem)session.getAttribute(Constants.SUBMISSION_CASE_DATA);
pageContext.setAttribute("submissionCase",submissionCase,PageContext.PAGE_SCOPE);
CensusSubmissionReportData theReport = (CensusSubmissionReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
CensusDetailsHelper censusDetailsHelper = (CensusDetailsHelper)session.getAttribute(Constants.CENSUS_DETAILS_HELPER);
pageContext.setAttribute("censusDetailsHelper",censusDetailsHelper,PageContext.PAGE_SCOPE);
%>

<style type="text/css">

div.scroll {
	height: 150px;
	width: 365px;
	overflow: auto;
	border-style: none;
	background-color: #fff;
	padding: 8px;}
div.inline {
	display: inline; }

table#headers {table-layout:fixed;}
table#data    {table-layout:relative;}

</style>

		     
<content:contentBean contentId="<%=ContentConstants.CENSUS_DETAILS_DOWNLOAD_REPORT_WARNING%>"
                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                               id="downloadReportWarning"/>
                               	     
<content:contentBean contentId="<%=ContentConstants.CENSUS_DETAILS_NO_EMPLOYEE_RECORDS_FOUND%>"
                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                               id="noEmployeeRecordsFound"/>
                               
<c:set var="downloadWarning"><content:getAttribute id="downloadReportWarning" attribute="text"/></c:set>
<c:set var="printFriendly" value="${param.printFriendly}"/>
<SCRIPT  language="JavaScript">

var fixedTable;
var scrollingTable;


if (window.addEventListener) {
	window.addEventListener('load', init, false);
} else if (window.attachEvent) {
	window.attachEvent('onload', init);
} else if (document.getElementById)
	window.onload=init;

function submitform()
{
    var reportURL = new URL(window.location.href);
    reportURL.setParameter('action','submit');
    window.location.href = reportURL.encodeURL();
}

function doReset()
{
	var reportURL = new URL(window.location.href);
    reportURL.setParameter('task','reset');
    window.location.href = reportURL.encodeURL();
}

function doEdit()
{
  var reportURL = new URL(window.location.href);
  reportURL.setParameter('mode','e');
  window.location.href = reportURL.encodeURL();
}

function doDiscard()
{
  var msg = "\nThere are records that could not be processed and displayed, are you sure you would like to discard them?";
  if ( confirm(msg) ) {
    var reportURL = new URL(window.location.href);
    reportURL.setParameter('action','discard');
    window.location.href = reportURL.encodeURL();
  }
  return false;
}

function doDownload(ext)
{
  var msg = "${downloadWarning}";
  if ( confirm(msg) ) {
    var reportURL = new URL(window.location.href);
    reportURL.setParameter('task','download');
    reportURL.setParameter('ext',ext);
    window.location.href = reportURL.encodeURL();
  } 
  
}

function init() {
<c:if test="${not empty theReport.details}">
  	scrollingTable = document.getElementById("scrollingTable");
  	fixedTable = document.getElementById("fixedTable");

  	if ( navigator.userAgent.toLowerCase().indexOf( 'gecko' ) != -1 ) {
     	fixedTable.style.overflow='-moz-scrollbars-none'; 
     	scrollingTable.style.overflow='-moz-scrollbars-horizontal';
  	}
  	
  	//alert(scrollingTable.offsetTop);
  	//alert(scrollingTable.offsetParent.offsetParent.offsetTop);
  	//alert(scrollingTable.offsetParent.offsetParent.offsetParent.offsetParent.offsetTop);
  	
  	fixedTable.style.top = scrollingTable.offsetTop + 
  						   scrollingTable.offsetParent.offsetParent.offsetTop + 
  						   scrollingTable.offsetParent.offsetParent.offsetParent.offsetParent.offsetTop;
  	fixedTable.style.left = 15;
  	fixedTable.style.visibility = 'visible';
  	
</c:if>
}

</SCRIPT>

<%
	String sCompanyName = userProfile.getCurrentContract().getCompanyName();
	String sContractNumber = ""+userProfile.getCurrentContract().getContractNumber();

	String sReferenceNumber = ""+submissionCase.getSubmissionId();
	String sSubmittedBy = ""+submissionCase.getSubmitterName();
	String sSubmittedId = ""+submissionCase.getSubmitterID();
	String sSubmitterEmail = submissionCase.getSubmitterEmail();
	if ( sSubmitterEmail == null || sSubmitterEmail.length() == 0 ) sSubmitterEmail = "Not provided";
	SubmissionHistoryItemActionHelper helper = SubmissionHistoryItemActionHelper.getInstance();
	String sStatus = helper.getDisplayStatus(submissionCase);

	if (sContractNumber == null) sContractNumber = "";
	if (sCompanyName == null || sCompanyName.length() == 0) sCompanyName = "";
	String completeMessage = "false";
%>

<% 
	String style_scrolling="overflow:scroll; overflow-y:hidden; z-index:2; width:745px;"; 
	String style_fixed="overflow:hidden; z-index:3; visibility:hidden; position:absolute; width:265px;";
%>
<c:if test="${not empty param.printFriendly }" >
<% style_scrolling="overflow:hidden;"; %>
</c:if>


<p>
	<content:errors scope="session" />
</p>

<ps:form cssStyle="margin-bottom:0;"
         method="POST" modelAttribute="viewCensusDetailsForm" name="viewCensusDetailsForm"
         action="/do/tools/viewCensusDetails/">

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0" /></td>
    <td width="350" valign="top">
      <table width="350" height="150" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="1"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
          <td width="172"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
          <td width="172"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
          <td width="4"><img src="/assets/unmanaged/images/spacer.gif" width="4" height="1" /></td>
          <td width="1"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
        </tr>
        <tr class="tablehead">
          <td class="tableheadTD1" colspan="5">             
          	<b>Census Submission Summary</b>  
          </td>
        </tr>
        <tr class="datacell1">
          <td class="databorder">
          	<img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" />
          </td>
          <td colspan="3" align="left" valign="top" class="datacell1">
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
          	<tr class="datacell1">
          	  <td width="50%"><strong>Contract:</strong></td>
              <td width="50%" class="highlightBold"><%=sCompanyName%>&nbsp;<%=sContractNumber%></td>
          	</tr>
            <tr class="datacell1">
              <td width="50%"><strong>Submission Number:</strong></td>
              <td width="50%" class="highlightBold"><%=sReferenceNumber%></td>
            </tr>
            <tr class="datacell1">
              <td class="datacell1"><strong>Status:</strong></td>
              <td align="left" valign="top" class="highlightBold"><%=sStatus%></td>
            </tr>
            <tr class="datacell1">
              <td class="datacell1"><strong>Submitted by:</strong></td>
              <% if (userProfile.isInternalUser()) { %>
					<% if (submissionCase.isInternalSubmitter()) { %>
						<td align="left" valign="top" class="highlightBold" title="<%=sSubmittedBy%>" >
					<% } else { %>
						<td align="left" valign="top" class="highlightBold" title="<%=sSubmittedId%>" >
					<% } %>
			  <% } else { %>
					<td align="left" valign="top" class="highlightBold">
			  <% } %>
			  <% if ((("I".equals(submissionCase.getSubmitterType()) && "FL".equals(submissionCase.getApplicationCode()))
					|| submissionCase.isInternalSubmitter())) { %>
					John Hancock Representative
			  <% } else if ( ("I".equals(submissionCase.getSubmitterType())) && ("AL".equals(submissionCase.getApplicationCode()))) {	%>
					<%=Constants.AWAITING_PAYMENT_PAYROLL_COMPANY%>
			  <% } else { %>
					<%=sSubmittedBy%>
			  <% } %>
              </td>
            </tr>
            <tr class="datacell1">
              <td><strong>Submitter Email:</strong></td>
              <td class="highlightBold">
                <span class="content"><%=sSubmitterEmail%></span>
              </td>
            </tr>
            <tr class="datacell1">
              <td class="datacell1"><strong>Number of Employees:</strong></td>
              <td align="left" valign="top" class="highlightBold"><report:recordCounter report="theReport" totalOnly="true" label=""/></td>
            </tr>
            <tr class="datacell1">
              <td><strong>Submission Date:</strong></td>
              <td class="highlightBold">
                <span class="content"><render:date property="submissionCase.submissionDate" patternOut="MM/dd/yyyy HH:mm" defaultValue=""/></span>
              </td>
            </tr>

<%--             <% if (submissionCase.isLocked() && censusDetailsHelper.isLocked()) {%>
            <tr class="datacell1">
              <td class="datacell1"><strong>In Use by: </strong></td>
              <td align="left" valign="top" class="highlightBold">
			  <% if (userProfile.isInternalUser()) { %>
				<c:if test="${submissionCase.lockedByInternalUser!=true}">
					<span title="${submissionCase.lock.userId}"><strong class="highlight">${submissionCase.lock.userName}</strong></span>
				</c:if>
				<c:if test="${submissionCase.lockedByInternalUser==true}">
					<span title="${submissionCase.lock.userId}<strong class="highlight">John Hancock Representative</strong>
				</c:if>
			  <% } else { %>
				<c:if test="${submissionCase.lockedByInternalUser!=true}">
					<strong class="highlight">${submissionCase.lock.userId}</strong>
				</c:if>
				<c:if test="${submissionCase.lockedByInternalUser==true}">
					<strong class="highlight">John Hancock Representative</strong>
				</c:if>
			  <% } %>
              </td>
            </tr>
            <% } %> --%>

          </table>
          </td>
          <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
        </tr>
        <tr class="divider">
          <td height="4" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
          <td class="whiteborder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
          <td class="whiteborder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
          <td height="5" colspan="2" rowspan="2" valign="top" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" /></td>
        </tr>
        <tr>
          <td height="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
          <td height="1" colspan="2" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
        </tr>
      </table>
    </td>
    <c:if test="${empty param.printFriendly }" >
    <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0" /></td>
    <td width="350" valign="top">
      <report:submissionErrors errors="<%=theReport%>" width="350"/>
    </td>
    </c:if>
    <c:if test="${not empty param.printFriendly }" >
    <td colspan="2">&nbsp;</td>
    </c:if>
  </tr>
  <tr>
    <td colspan="4">&nbsp;</td>
  </tr>
</table>

<c:if test="${not empty param.printFriendly }" >
<table width="760" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0" /></td>
    <td><report:submissionErrors errors="<%=theReport%>" width="730" printFriendly="true"/></td>
  </tr>
  <tr>
    <td colspan="2"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
  </tr>
</table>
<p>
</c:if>


<table width="760" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0" /></td>
    <td valign="top">
	  <table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr class="tablehead">
        <td width="25%" class="tableheadTD1">      
        	<b>Census submission details</b>
        </td>
        <td width="15%" class="tableheadTD" align="left">
        <c:if test="${empty param.printFriendly }" >     
        	<a href="" onClick="doReset();">Default Sort</a>
        </c:if>
        </td>
        <td width="25%" align="left" class="tableheadTD">
        	<span class="tableheadTD"><strong>
        	<report:recordCounter report="theReport" totalOnly="false" label="Total Records:"/>
        	</strong></span>
        </td>
        <td width="25%" align="left" class="tableheadTD">
        	<span class="tableheadTD">
        	<c:if test="${empty param.printFriendly }" >
            <ps:setReportPageSize/>
            </c:if>
            </span>
		</td>
        <td width="10%" align="right" class="databorder"><span class="tableheadTD">
            <report:pageCounter name="viewCensusDetailsForm" formName="viewCensusDetailsForm" report="theReport" arrowColor="black" name="parameterMap"/></span>
        </td>
      </tr>
      </table>

<c:if test="${not empty theReport.details}">
	  <c:if test="${empty param.printFriendly }" >
	  <div id="fixedTable" style="<%=style_fixed%>">
	  <table border="0" cellpadding="0" cellspacing="0" id="headers">
	  <tr class="tablesubhead">
      	<td width="1" valign="top" height="60" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
      	<% if (censusDetailsHelper.isEditMode()) { %>
        <td height="60" width="25" valign="top" class="tablesubhead"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /><strong>Edit</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
      	<% } %>
        <td width="35" valign="top" nowrap="nowrap" class="tablesubhead"><strong><report:sort field="<%=CensusSubmissionReportData.SORT_RECORD_NUMBER%>"  formName="viewCensusDetailsForm"  direction="asc">#</report:sort></strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <% if (censusDetailsHelper.isEditMode()) { %>
        <td width="100" valign="top" nowrap="nowrap" class="tablesubhead"><strong><report:sort field="<%=CensusSubmissionReportData.SORT_STATUS%>" formName="viewCensusDetailsForm"  direction="asc">Status</report:sort></strong></td>
        <% } else { %>
        <td width="126" valign="top" nowrap="nowrap" class="tablesubhead"><strong><report:sort field="<%=CensusSubmissionReportData.SORT_STATUS%>" formName="viewCensusDetailsForm"  direction="asc">Status</report:sort></strong></td>
        <% } %>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" valign="top" nowrap="nowrap" class="tablesubhead"><strong><report:sort field="<%=censusDetailsHelper.getEmployeeIdentifierSortField(theReport)%>" formName="viewCensusDetailsForm"  direction="asc"><%=censusDetailsHelper.getEmployeeIdentifierColumnLabel(theReport)%></report:sort></strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
      </tr>
      
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
 <c:set var="indexValue" value="${theIndex.index}"/> 
 <% 
  String indexVal = pageContext.getAttribute("indexValue").toString();
  CensusSubmissionItem theItem = (CensusSubmissionItem)pageContext.getAttribute("theItem");
 %>




		<% if (Integer.parseInt(indexVal) % 2 == 0) { %>
			<tr align="left" valign="top" class="datacell1">
		<% } else { %>
			<tr align="left" valign="top" class="datacell2">
					<% } %>

        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
		<% if (censusDetailsHelper.isEditMode()) { %>
        <td align="center"><report:actions profile="userProfile" item="theItem" action="editSubmittedCensus"/></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
        <% } %>
<td>${theItem.sourceRecordNo} <%-- ignore="true" --%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
		<% if (userProfile.isInternalUser()) { %>
<td valign="top" title="${theItem.processStatus} <%-- ignore="true" --%>">
		<% } else { %>
			<td valign="top">
		<% } %>
${theItem.displayStatus} <%-- ignore="true" --%>
        <% if (theItem.isDoubleRowHeight()) { %><br>&nbsp;<% } %></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getIdToolTip(theReport,theItem)%>">
           	<%=censusDetailsHelper.getId(theReport,theItem,false)%>
        </td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        
</c:forEach>
        
	  </table>
	  </div>
	  </c:if>
</c:if>
	  
	  <div id="scrollingTable" style="<%=style_scrolling%>">
	  <table id="data" border="0" cellpadding="0" cellspacing="0">
      <tr class="tablesubhead">
      	<td width="1" height="60" valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
      <c:if test="${empty param.printFriendly }" >
      <% if (censusDetailsHelper.isEditMode()) { %>
        <td width="25" height="60" valign="top" class="tablesubhead"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /><strong>Edit</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
      <% } %>
      </c:if>
        <td width="35" valign="top" nowrap="nowrap" class="tablesubhead"><strong><report:sort field="<%=CensusSubmissionReportData.SORT_RECORD_NUMBER%>" formName="viewCensusDetailsForm"  direction="asc">#</report:sort></strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <% if (censusDetailsHelper.isEditMode()) { %>
        <td width="100" valign="top" nowrap="nowrap" class="tablesubhead"><strong><report:sort field="<%=CensusSubmissionReportData.SORT_STATUS%>" formName="viewCensusDetailsForm"  direction="asc">Status</report:sort></strong></td>
        <% } else { %>
        <td width="126" valign="top" nowrap="nowrap" class="tablesubhead"><strong><report:sort field="<%=CensusSubmissionReportData.SORT_STATUS%>" formName="viewCensusDetailsForm"  direction="asc">Status</report:sort></strong></td>
        <% } %>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" valign="top" nowrap="nowrap" class="tablesubhead"><strong><report:sort field="<%=censusDetailsHelper.getEmployeeIdentifierSortField(theReport)%>" formName="viewCensusDetailsForm"  direction="asc"><%=censusDetailsHelper.getEmployeeIdentifierColumnLabel(theReport)%></report:sort></strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" valign="top" nowrap="nowrap" class="tablesubhead"><strong><report:sort field="<%=censusDetailsHelper.getOtherEmployeeIdentifierSortField(theReport)%>" formName="viewCensusDetailsForm"  direction="asc"><%=censusDetailsHelper.getOtherEmployeeIdentifierColumnLabel(theReport)%></report:sort></strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" valign="top" nowrap="nowrap" class="tablesubhead"><strong>First Name</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="120" valign="top" nowrap="nowrap" class="tablesubhead"><strong><report:sort field="<%=CensusSubmissionReportData.SORT_NAME%>" formName="viewCensusDetailsForm"  direction="asc">Last Name</report:sort></strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" valign="top" class="tablesubhead"><strong>Middle Initial</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" valign="top" class="tablesubhead"><strong>Prefix</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="120" valign="top" nowrap="nowrap" class="tablesubhead"><strong>Address Line 1</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="120" valign="top" class="tablesubhead" nowrap="nowrap"><strong>Address Line 2</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="50" valign="top" class="tablesubhead"><strong>City</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="80" valign="top" class="tablesubhead"><strong>State</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="50" valign="top" class="tablesubhead"><strong>Zip Code</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="70" valign="top" class="tablesubhead"><strong>Country</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" valign="top" class="tablesubhead"><strong>State of Residence</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="150" valign="top" nowrap="nowrap" class="tablesubhead"><strong>Employer Provided Email Address</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="200" valign="top" class="tablesubhead"><strong><report:sort field="<%=CensusSubmissionReportData.SORT_DIVISION%>" formName="viewCensusDetailsForm"  direction="asc">Division</report:sort></strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" valign="top" class="tablesubhead"><strong>Date of Birth<br>(mm/dd/yyyy)</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" valign="top" class="tablesubhead"><strong>Hire Date<br>(mm/dd/yyyy)</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" valign="top" class="tablesubhead"><strong>Employment Status</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" nowrap="nowrap" valign="top" class="tablesubhead"><strong>Employment Status<br>Effective Date<br>(mm/dd/yyyy)</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" nowrap="nowrap" valign="top" class="tablesubhead"><strong>Eligible to participate</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" nowrap="nowrap" valign="top" class="tablesubhead"><strong>Eligibility Date<br>(mm/dd/yyyy)</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" nowrap="nowrap" valign="top" class="tablesubhead"><strong>Opt Out</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" nowrap="nowrap" valign="top" class="tablesubhead"><strong>Plan YTD<br>hours worked</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" valign="top" class="tablesubhead"><strong>Eligible Plan YTD Compensation($)</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="200" nowrap="nowrap" valign="top" class="tablesubhead"><strong>Plan YTD hours worked/<br>Eligible comp effective date<br>(mm/dd/yyyy)</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" nowrap="nowrap" valign="top" class="tablesubhead"><strong>Annual<br>Base Salary($)</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" nowrap="nowrap" valign="top" class="tablesubhead"><strong>Before Tax<br>Deferral Percentage(%)</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" nowrap="nowrap" valign="top" class="tablesubhead"><strong>Designated Roth<br>Deferral Percentage(%)</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" nowrap="nowrap" valign="top" class="tablesubhead"><strong>Before Tax<br>Flat Dollar Deferral($)</strong></td>
        <td width="1" valign="top" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td width="100" nowrap="nowrap" valign="top" class="tablesubhead"><strong>Designated Roth<br>Flat Dollar Deferral($)</strong></td>       
        <td width="1" valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
      </tr>

<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
 <c:set var="indexValue" value="${theIndex.index}"/> 
 <% 
  String indexVal = pageContext.getAttribute("indexValue").toString();
  CensusSubmissionItem theItem = (CensusSubmissionItem)pageContext.getAttribute("theItem");
 %>




		<% if (Integer.parseInt(indexVal) % 2 == 0) { %>
			<tr align="left" valign="top" class="datacell1">
		<% } else { %>
			<tr align="left" valign="top" class="datacell2">
					<% } %>

        <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
        <c:if test="${empty param.printFriendly }" >
		<% if (censusDetailsHelper.isEditMode()) { %>
        <td align="center"><report:actions profile="userProfile" item="theItem" action="editSubmittedCensus"/></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
        <% } %>
        </c:if>
<td>${theItem.sourceRecordNo} <%-- ignore="true" --%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
		<% if (userProfile.isInternalUser()) { %>
<td title="${theItem.processStatus} <%-- ignore="true" --%>">
		<% } else { %>
			<td>
		<% } %>
${theItem.displayStatus} <%-- ignore="true" --%>
        <% if (theItem.isDoubleRowHeight()) { %><br>&nbsp;<% } %></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getIdToolTip(theReport,theItem)%>">
        	<%=censusDetailsHelper.getId(theReport,theItem,false)%>
        </td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getOtherIdToolTip(theReport,theItem)%>">
        	<%=censusDetailsHelper.getOtherId(theReport,theItem,false)%>
        </td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getFirstNameToolTip(theItem)%>"><%=censusDetailsHelper.getFirstName(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getLastNameToolTip(theItem)%>"><%=censusDetailsHelper.getLastName(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title=""><%=censusDetailsHelper.getMiddleInitial(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getNamePrefixToolTip(theItem)%>"><%=censusDetailsHelper.getNamePrefix(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getAddressLine1ToolTip(theItem)%>"><%=censusDetailsHelper.getAddressLine1(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getAddressLine2ToolTip(theItem)%>"><%=censusDetailsHelper.getAddressLine2(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getCityToolTip(theItem)%>"><%=censusDetailsHelper.getCity(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getStateToolTip(theItem)%>"><%=censusDetailsHelper.getState(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getZipToolTip(theItem)%>"><%=censusDetailsHelper.getZip(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
		<td nowrap="nowrap" title="<%=censusDetailsHelper.getCountryToolTip(theItem)%>"><%=censusDetailsHelper.getCountry(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getStateOfResidenceToolTip(theItem)%>"><%=censusDetailsHelper.getStateOfResidence(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getEmployeeProvidedEmailToolTip(theItem)%>"><%=censusDetailsHelper.getEmployeeProvidedEmail(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getDivisionToolTip(theItem)%>"><%=censusDetailsHelper.getDivision(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getBirthDateToolTip(theItem)%>"><%=censusDetailsHelper.getBirthDate(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getHireDateToolTip(theItem)%>"><%=censusDetailsHelper.getHireDate(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title=""><%=censusDetailsHelper.getEmployeeStatus(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getEmployeeStatusDateToolTip(theItem)%>"><%=censusDetailsHelper.getEmployeeStatusDate(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title=""><%=censusDetailsHelper.getEligibilityIndicator(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getEligibilityDateToolTip(theItem)%>"><%=censusDetailsHelper.getEligibilityDate(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title=""><%=censusDetailsHelper.getOptOutIndicator(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getPlanYTDHoursWorkedToolTip(theItem)%>"><%=censusDetailsHelper.getPlanYTDHoursWorked(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getPlanYTDCompensationToolTip(theItem)%>"><%=censusDetailsHelper.getPlanYTDCompensation(theReport,theItem,userProfile,false,true)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getPlanYTDHoursWorkedEffDateToolTip(theItem)%>"><%=censusDetailsHelper.getPlanYTDHoursWorkedEffDate(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getAnnualBaseSalaryToolTip(theItem)%>"><%=censusDetailsHelper.getAnnualBaseSalary(theReport,theItem,userProfile,false,true)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getBeforeTaxDeferralPercToolTip(theItem)%>"><%=censusDetailsHelper.getBeforeTaxDeferralPerc(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getDesigRothDeferralPercToolTip(theItem)%>"><%=censusDetailsHelper.getDesigRothDeferralPerc(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getBeforeTaxDeferralAmtToolTip(theItem)%>"><%=censusDetailsHelper.getBeforeTaxDeferralAmt(theItem)%></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
        <td nowrap="nowrap" title="<%=censusDetailsHelper.getDesigRothDeferralAmtToolTip(theItem)%>"><%=censusDetailsHelper.getDesigRothDeferralAmt(theItem)%></td>
        <td valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1" border="0" /></td>
      </tr>

</c:forEach>
</c:if>
	
<c:if test="${empty theReport.details}">
		<tr class="datacell1">
		  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <c:if test="${empty param.printFriendly }" >
		  <% if (censusDetailsHelper.isEditMode()) { %>
			<td valign="top" colspan="31">
		  <% } else { %>
			<td valign="top" colspan="29">
		  <% } %>
		  </c:if>
		  <c:if test="${not empty param.printFriendly }" >
			<td valign="top" colspan="31">
		  </c:if>
			<b><content:getAttribute id="noEmployeeRecordsFound" attribute="text"/></b>
		  </td>
		  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
</c:if>
      </table>
      </div>
      
      <c:if test="${empty param.printFriendly }" >
      <table width="745" border="0" cellspacing="0" cellpadding="0">
      <tr align="right" class="tableheadTDlastrow">
      	<% if (censusDetailsHelper.isEditMode()) { %>
        <td height="5" colspan="31" class="tableheadTDlastrow"><report:pageCounter  name="viewCensusDetailsForm" formName="viewCensusDetailsForm"  report="theReport" arrowColor="black" name="parameterMap"/></td>
        <% } else { %>
        <td height="5" colspan="29" class="tableheadTDlastrow"><report:pageCounter  name="viewCensusDetailsForm" formName="viewCensusDetailsForm"  report="theReport" arrowColor="black" name="parameterMap"/></td>
        <% } %>
      </tr>
      </table>
      </c:if>
      
	</td>
  </tr>
  <c:if test="${empty param.printFriendly }" >
  <tr>
    <td>&nbsp;</td>
    <td><br />
      <table width="745" border="0" cellspacing="0" cellpadding="0">
        <tr valign="top">
          <td width="341">&nbsp;</td>
          <td width="188">
          	<% if ( !censusDetailsHelper.isEditMode() && censusDetailsHelper.isEditAllowed()) { %>
<input type="button" onclick="doEdit()" name="action" class="button100Lg" value="edit"/>
            <br />
			Edit your submission.
			<% } %>
		  </td>
          <td width="216">
<input type="submit" class="button134" name="action" value="submission history"  alt="Submission History"/>
            <br />
			This will allow you to send the submission as it is once you have completed your submission
		  </td>
        </tr>
      </table>
    </td>
  </tr>
  </c:if>
  <tr>
  	<td>&nbsp;</td>
	<td>
		<br>
		<p><content:pageFooter beanName="layoutPageBean"/></p>
 		<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 		<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
 	</td>
  </tr>
</table>
</ps:form>

<c:if test="${not empty param.printFriendly }" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        id="globalDisclosure"/>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>



