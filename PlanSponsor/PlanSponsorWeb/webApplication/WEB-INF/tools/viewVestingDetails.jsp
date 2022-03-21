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
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.service.report.submission.valueobject.VestingDetailsReportData" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.VestingDetailItem" %>
<%@ page import="com.manulife.pension.ps.web.tools.util.VestingDetailsHelper" %>
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

<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>

<content:contentBean contentId="<%=ContentConstants.SUBMISSION_UPLOAD_PAYMENT_TEXT%>"
    type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
    beanName="fileUploadPaymentNote"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_NO_PAYMENT%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageNoPaymentInstructionsIncluded"/>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_MISC_HISTORY_BUTTON_DESCRIPTION%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="historyButtonDescription"/>
<content:contentBean contentId="<%=ContentConstants.CONTRIBUTION_DETAILS_EDIT_BUTTON_DESCRIPTION%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="editButtonDescription"/>


<jsp:useBean id="vestingDetailsForm" scope="session" type="com.manulife.pension.ps.web.tools.VestingDetailsForm"/>





<content:contentBean contentId="<%=ContentConstants.VESTING_DETAILS_DOWNLOAD_REPORT_WARNING%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="downloadReportWarning"/>                             
<content:contentBean contentId="<%=ContentConstants.VESTING_DETAILS_NO_RECORDS_FOUND%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="noVestingRecordsFound"/>
<content:contentBean contentId="<%=ContentConstants.INVALID_HISTORICAL_CSF%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="invalidHistoricalCSF"/>
                               	                                   
<c:set var="downloadWarning"><content:getAttribute id="downloadReportWarning" attribute="text"/></c:set>


<%
VestingDetailsReportData theReport = (VestingDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
VestingDetailsHelper vestingDetailsHelper = (VestingDetailsHelper)session.getAttribute(Constants.VESTING_DETAILS_HELPER);
pageContext.setAttribute("vestingDetailsHelper",vestingDetailsHelper,PageContext.PAGE_SCOPE);
String STATUS_COMPLETE = vestingDetailsHelper.STATUS_COMPLETE;
pageContext.setAttribute("STATUS_COMPLETE", STATUS_COMPLETE);


%>





<c:set var="submissionItem" value="${theReport.vestingData}" />
<c:set var="systemStatus" value="${submissionItem.systemStatus}"/>

<%-- The next form is needed for the request --%>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<c:set var="contract" value="${sessionScope.USER_KEY.currentContract}" scope="request" />

<script type="text/javascript" >

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

function doReset()
{
	var reportURL = new URL(window.location.href);
    reportURL.setParameter('task','reset');
    window.location.href = reportURL.encodeURL();
}

</SCRIPT>
<p><content:errors scope="session"/></p>
<A NAME="TopOfPage"></A>&nbsp;
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="fixedTable">
		  <ps:form action="/do/tools/viewVesting/" modelAttribute="vestingDetailsForm" name="vestingDetailsForm" method="POST" onsubmit="return confirmSend();">
          <tr>
				<td width="15" class="big">&nbsp;</td>
				<!-- column 2 summary section -->
				<td width="350" height="20" valign="top">
					<jsp:include page="vestingSummarySection.jsp" flush="true" />
				</td>

				<!-- column 3 column gap -->
				<td width="15"><img src="/assets/unmanaged/images/s.gif" width="15" height="1" border="0"></td>
				

				<!-- column 4 errors and warnings -->
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
					<%-- START new vesting details table --%>
						<br/>
						<br/>
						<c:if test="${not empty param.printFriendly }" >
							<c:if test="${systemStatus!=STATUS_COMPLETE}">
							<report:submissionErrors errors="<%=theReport%>" vestingDetailsErrors="true" width="505" printFriendly="true" forceView="true"/>
</c:if>
							<c:if test="${systemStatus==STATUS_COMPLETE}">
							<report:submissionErrors errors="<%=theReport%>" vestingDetailsErrors="true" width="505" printFriendly="true" forceView="false"/>
</c:if>
						</c:if>
						<br/>
						<br/>
						<jsp:include page="vestingViewSection.jsp" flush="true" />
						<c:if test="${empty param.printFriendly }" >
							<p/>
							<table border="0" cellspacing="0" cellpadding="0" align="center" width="700">
							  <tr>
								<td	width="20%">&nbsp;</td>
								<% if ( vestingDetailsHelper.isVestingEditable() ) {
									%>
<td width="40%"><input type="submit" class="button100Lg" onclick="window.location.href='/do/tools/editVesting/?subNo=${submissionItem.submissionId}'; return false;" value="edit" /></td>
 								<% } else { %>
 									<td width="40%">&nbsp;</td>
 								<% } %>
<td width="40%"><input type="submit" class="button175" onclick="window.location.href='/do/tools/submissionHistory/'; return false;" value="submission history" /></td>
								<td width="20%">&nbsp;</td>
							  </tr>
							  <tr>
								<td valign="top">&nbsp;</td>
 								<% if ( vestingDetailsHelper.isVestingEditable() ) { %>
									<td valign="top"><span class="content"><content:getAttribute beanName="editButtonDescription" attribute="text"/></span></td>
								<% } else { %>
 									<td valign="top">&nbsp;</td>
								<% } %>
								<td valign="top"><span class="content"><content:getAttribute beanName="historyButtonDescription" attribute="text"/></span></td>
								<td valign="top">&nbsp;</td>
							  </tr>
							</table>
						</c:if>
						
					<%-- END new vesting details table --%>
					<br>
				</td>
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
		</ps:form>
        </table>
