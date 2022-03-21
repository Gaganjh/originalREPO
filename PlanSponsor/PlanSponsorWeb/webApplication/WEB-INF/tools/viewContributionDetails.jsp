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
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.ContributionDetailItem" %>
<%@ page import="com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData" %>
<%@ page import="com.manulife.pension.ps.web.tools.ViewContributionDetailsForm" %>
<style type="text/css">
<!--
div.scroll {
	height: 234px;
	width: 225px;
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
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_NO_PAYMENT_PROCESSED%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageNoPaymentInstructionsProcessed"/>

					  <%-- The first form is needed for the page --%>

<%-- The next form is needed for the request --%>

<%
ContributionDetailsReportData theReport = (ContributionDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
ContributionDetailItem contributionData = theReport.getContributionData();
pageContext.setAttribute("submissionItem",contributionData,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<c:set var="contract" value="${userProfile.currentContract}" scope="request" />

<script type="text/javascript" >
	//AG: formName == viewContributionDetailsForm
</SCRIPT>
<content:errors scope="session"/>
<A NAME="TopOfPage"></A>&nbsp;
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="fixedTable">

          <tr>
				<td width="16" class="big">&nbsp;</td>
				<td height="20">
					<ps:form action="/do/tools/viewContribution/" modelAttribute="viewContributionDetailsForm" name="viewContributionDetailsForm" method="POST" onsubmit="return confirmSend();">
				 <jsp:include page="contractViewSection.jsp" flush="true" /> 

					<br/>
					<br/>

					  <table width="500" border="0" cellpadding="0" cellspacing="0">
						<tr>
						  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="80" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="420" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1" /></td>
						  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
						<tr class="tablehead" height="25">
						  <td class="tableheadTD1" colspan="5"><b><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></b></td>
						</tr>
						<tr class="whiteborder">
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td colspan="3">
						  <table width="100%" border="0" cellpadding="0" cellspacing="0">
<c:if test="${viewContributionDetailsForm.isViewMode() ==true}">
<c:if test="${submissionItem.zeroContributionFile !=true}">
<c:if test="${viewContributionDetailsForm.isDisplayPaymentInstructionSection() ==true}">
								<jsp:include page="paymentViewSection.jsp" flush="true" />
</c:if>
<c:if test="${viewContributionDetailsForm.isDisplayPaymentInstructionSection() !=true}">
								<tr>
									<td class="datacell1"><span class="content"><content:getAttribute beanName="messageNoPaymentInstructionsIncluded" attribute="text"/></span>
									</td>
								</tr>
</c:if>
</c:if>
<c:if test="${submissionItem.zeroContributionFile ==true}">
							<tr>
									<td class="datacell1"><span class="content"><content:getAttribute beanName="messageNoPaymentInstructionsProcessed" attribute="text"/></span>
									</td>
								</tr>
</c:if>
</c:if>

						  </table>
						  </td>

						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
						<tr class="datacell1">
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td class="lastrow" colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>
						<tr class="datacell1">
						  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4" /></td>
						  <td class="lastrow" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						  <td colspan="2" rowspan="2" align="right" valign="bottom" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" /></td>
						</tr>
						<tr>
						  <td class="databorder" colspan="3"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
						</tr>

                </table>
				</td>

				<!--// end column 2 -->


				<!-- column 3 column gap -->
				<td width="15"><img src="/assets/unmanaged/images/s.gif" width="15" height="1" border="0"></td>
				<!--// emd column 3 -->

				<!-- column 4 HELPFUL HINT -->
				<td width="100%" align="left" valign="top">
					<c:if test="${empty param.printFriendly }" >
						<report:submissionErrors errors="<%=theReport%>" contributionDetailsErrors="true" width="230" printFriendly="false" forceView="true"/>
					</c:if>
				</td>


				<!--// end column 4 -->
			</tr>
          <tr>
				<td width="16" class="big">&nbsp;</td>
				<td colspan="3">
					<%-- START new contribution details table --%>
						<br/>
						<br/>
						<jsp:include page="participantViewSection.jsp" flush="true" />
						<c:if test="${empty param.printFriendly }" >
							<p/>
							<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
							  <tr>
								<td	width="16%">&nbsp;</td>
								<c:if test="${viewContributionDetailsForm.isEditFunctionAvailable()}">
 									<td width="2%"></td>
 									<td width="32%"><input type="submit" class="button100Lg" value="edit" name="actionLabel" onclick="window.location.href='/do/tools/editContribution/?subNo=<%=contributionData.getSubmissionId() %>'; return false;"/>
 									</td>
 								</c:if>
								<td width="2%"></td>
<td width="32%"><input type="submit" class="button175" onclick="window.location.href='/do/tools/submissionHistory/'; return false;" name="actionLabel" value="submission history" /></td>
								<td width="16%"></td>
							  </tr>
							  <tr>
								<td valign="top" width="16%">&nbsp;</td>
 								<c:if test="${viewContributionDetailsForm.isEditFunctionAvailable()}">
									<td width='2'></td>
									<td valign="top"><span class="content"><content:getAttribute beanName="editButtonDescription" attribute="text"/></span></td>
								</c:if>
								<td width='2'></td>
								<td valign="top"><span class="content"><content:getAttribute beanName="historyButtonDescription" attribute="text"/></span></td>
							  </tr>
							</table>
						</c:if>
						<c:if test="${not empty param.printFriendly }" >
							<br/>
							<br/>
							<report:submissionErrors errors="<%=theReport%>" contributionDetailsErrors="true" width="505" printFriendly="true" forceView="true"/>
						</c:if>
						</ps:form>
					<%-- END new contribution details table --%>
					<br>
				</td>
			</tr>
			<tr>
				<td class="big">&nbsp;</td>
				<td colspan="3">&nbsp;</td>
			</tr>

			<tr>
               <td height="20" width="30">&nbsp;</td>
               <td colspan="3" width="730">		 	   
					<p><content:pageFooter id="layoutPageBean"/></p>
				</td>
 			</tr>

			<c:if test="${viewContributionDetailsForm.isDisplayPaymentInstructionSection()}" >
 			<tr>
               <td height="20" width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="20"></td>
               <td width="730" colspan="3">		 	   
					<p class="footnote"><content:pageFootnotes id="layoutPageBean"/></p>
					<p class="disclaimer"><content:pageDisclaimer id="layoutPageBean" index="-1"/></p>
				</td>
 			</tr>
 			</c:if>

<c:if test="${viewContributionDetailsForm.isDisplayPaymentInstructionSection() ==true}">
<c:set var="detail" value="${viewContributionDetailsForm.paymentDetails}" />
<c:if test="${detail.displayDebitFootnote ==true}">
			<tr>
			  <td class="big">&nbsp;</td>
              <td height="20" colspan="3">
			     <table width="100%">
				 	<tr>
					  <td width="50%">
					  	<span class="disclaimer"><content:getAttribute beanName="fileUploadPaymentNote" attribute="text"/></span></td>
					  </td>
					  <td width="50%">&nbsp;</td>
					</tr>
			     </table>
			  </td>
			</tr>
</c:if> 
</c:if>
        </table>
