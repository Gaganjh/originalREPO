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
<content:contentBean contentId="<%=ContentConstants.HANDLE_BAD_FILE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="handleBadFile"/>
<c:set var="printFriendly" value="${param.printFriendly}" />

<%
ContributionDetailsReportData theReport = (ContributionDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
ContributionDetailItem submissionItem = (ContributionDetailItem)theReport.getContributionData();
pageContext.setAttribute("submissionItem",submissionItem,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

ViewContributionDetailsForm form = (ViewContributionDetailsForm)session.getAttribute("viewContributionDetailsForm");
pageContext.setAttribute("form",form,PageContext.PAGE_SCOPE);

ViewContributionDetailsForm requestForm = (ViewContributionDetailsForm)session.getAttribute("viewContributionDetailsForm");
pageContext.setAttribute("requestForm",requestForm,PageContext.REQUEST_SCOPE);
%> 

<%-- The next form is needed for the request --%>

<c:set var="contract" value="${sessionScope.USER_KEY.currentContract}" scope="request" />

<script type="text/javascript" >
	//AG: formName == viewContributionDetailsForm
</SCRIPT>

<A NAME="TopOfPage"></A>&nbsp;
          <table width="100%" border="0" cellpadding="0" cellspacing="0" class="fixedTable">

          <tr>
				<td width="16" class="big">&nbsp;</td>
				<td height="20">
					<ps:form action="/do/tools/viewContributionNew/" modelAttribute="viewContributionDetailsForm" name="viewContributionDetailsForm" method="POST" onsubmit="return confirmSend();">
					<jsp:include page="contractViewSection.jsp" flush="true" />

					<br/>
					<br/>

					  <table width="140" border="0" cellpadding="0" cellspacing="0">
												 
						  <tr>
							<td >
				<input name="makeapayment" type="submit" class="button175" value="Make a payment"  onClick="Javascript:document.forms[0].action='/do/tools/makePayment/';">
				
						</td>
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
				<td  valign="bottom" align="left" valign="top">
				<!--//Content here showing is dummy content. Need to change when it is going to production. -->
				<content:getAttribute beanName="handleBadFile" attribute="text"/>
				
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
							<p/>&nbsp;&nbsp;&nbsp;&nbsp;
							<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
							  <tr>
								<td	width="16%">&nbsp;</td>
								<% if ( form.isEditFunctionAvailable() ) { %>
 									<td width="2%"></td>
 									<td width="32%">
<input type="submit" class="button100Lg" name="action" onclick="window.location.href='/do/tools/editContribution/?subNo=<%=submissionItem.getSubmissionId() %>'; return false;" value="edit" /><%-- property="actionLabel" do/tools/editContribution/?subNo="+ submissionItem.getSubmissionId() +" --%>
 									</td>						
 								 <% } %> 
								<td width="2%"></td>
<td width="32%"><input type="submit" class="button175" name="action" onclick="window.location.href='/do/tools/submissionHistory/'; return false;" value="submission history" /><%-- CCAT: Extra attributes for tag td - property="actionLabel" --%></td>
								<td width="16%"></td>
							  </tr>
							  <tr>
								<td valign="top" width="16%">&nbsp;</td>
 								<% if ( form.isEditFunctionAvailable() ) { %>
									<td width='2'></td>
									<td valign="top"><span class="content"><content:getAttribute beanName="editButtonDescription" attribute="text"/></span></td>
								<% } %>
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

			<%if (form.isDisplayPaymentInstructionSection()) { %>
 			<tr>
               <td height="20" width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="20"></td>
               <td width="730" colspan="3">		 	   
					<p class="footnote"><content:pageFootnotes id="layoutPageBean"/></p>
					<p class="disclaimer"><content:pageDisclaimer id="layoutPageBean" index="-1"/></p>
				</td>
 			</tr>
 			<% } %>

<c:if test="${form.displayPaymentInstructionSection ==true}">
<c:set var="detail" value="${requestForm.paymentDetails}"/>
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
