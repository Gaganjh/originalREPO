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
<%@ page import="com.manulife.pension.platform.web.util.DataUtility" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract"%>
<%@ page import="com.manulife.pension.ps.web.tools.SubmissionUploadDetailBean" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
SubmissionUploadDetailBean detail = (SubmissionUploadDetailBean)request.getSession().getAttribute(Constants.SUBMISSION_UPLOAD_HISTORY_DETAIL_DATA);
pageContext.setAttribute("detail",detail,PageContext.SESSION_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<c:set var="currentContract" value="${userProfile.currentContract}" />

<content:contentBean contentId="<%=ContentConstants.SUBMISSION_UPLOAD_PAYMENT_TEXT%>"
                      type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                      beanName="fileUploadPaymentNote"/>

        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="fixedTable">
			<tr>
              <td width="30" class="big"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
              <td height="20">
              <strong><content:getAttribute beanName="layoutPageBean" attribute="body1Header" /></strong>
						<table cellspacing="0" cellpadding="0" width="100%" border="0">
                          <tr>
                            <td width="135" valign="top"><!--DWLayoutEmptyCell-->&nbsp;</td>
                            <td width="365" valign="top"><!--DWLayoutEmptyCell-->&nbsp;</td>
                          </tr>
                          <tr>
                            <td nowrap><strong>Contract </strong></td>
                            <%Contract contract =(Contract) pageContext.getAttribute("currentContract"); %>
                            <td><strong class="highlight"><%= DataUtility.compute8DigitContractNumber(contract.getContractNumber())%>
${userProfile.currentContract.companyName}</strong></td>

                          </tr>
                          <tr>
						  	<td nowrap>&nbsp;</td>
							<td>&nbsp;</strong></td>
                          </tr>

						  <tr>
                            <td nowrap><strong>Submission number&nbsp; </strong></td>
<td><strong class="highlight">${detail.confirmationNumber} </strong></td>
                          </tr>
                          <tr>
                            <td nowrap><strong>Date received </strong></td>
                            <td ><strong class="highlight"><render:date property="detail.receivedDate" patternOut="MMMM dd, yyyy hh:mm a z" defaultValue=""/></strong></td>
                          </tr>
                          <tr>
                            <td nowrap><strong>Submitted by </strong></td>
<td ><strong class="highlight">${detail.sender}</strong></td>
                          </tr>
                          <tr>
                            <td nowrap><strong>Submitter Email</strong></td>
<td><strong class="highlight">${detail.email}</strong></td>
                          </tr>						  
                          <tr>						  
                          <tr>
                            <td height="14" colspan="2"><!--DWLayoutEmptyCell-->&nbsp; </td>
                          </tr>
                          <tr>
                            <td height="14" nowrap><strong>File Information </strong></td>
                            <td height="14" >&nbsp;</td>
                          </tr>
<c:if test="${detail.fileUploadExists ==true}">
                          <tr>
                            <td nowrap>File type </td>
                            <td ><strong class="highlight">Regular Combination</strong></td>
                          </tr>
                          <tr>
                            <td nowrap>File name </td>
<td ><strong class="highlight">${detail.fileName}</strong></td>
                          </tr>

<c:if test="${detail.displayGenerateStatementSection ==true}">
<c:if test="${detail.submissionTypeName == 'Regular Contribution'}">
                  		<tr>
                    		  <td valign="top">Generate Statements for quarter</td>
<td colspan="6"><strong class="highlight">${detail.lastPayrollForQuarterOption}</strong><br>
							    <% if (detail.getLastPayrollForQuarterOption().indexOf("Yes") != -1) { %>
<c:forEach items="${detail.statementDates}" var="item" varStatus="theIndex" >
									&nbsp;&nbsp;<span class="highlightBold"><render:date property="item.startDate" patternOut="MMM-d-yyyy" defaultValue=""/> to
									<render:date property="item.endDate" patternOut="MMM-d-yyyy" defaultValue=""/><br>
									</span>
</c:forEach>
								<% } %>
							  </td>
                  		</tr>
</c:if>
</c:if>

</c:if>
<c:if test="${detail.fileUploadExists !=true}">
							<tr>
							  <td colspan=2 >No file was included in this upload.</td>
							</tr>
</c:if>

                          <tr>
                            <td height="14" colspan="2">&nbsp;</td>
                          </tr>
                          <tr>
                            <td height="14" ><strong>Payment information</strong></td>
							<td height="14" >&nbsp;</td>
                          </tr>
						  <jsp:include page="paymentConfirmSection.jsp" flush="true" />
                      </table>
           	</td>
			<td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
			<td width="210" valign="top">
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
			</td>
			<!--// end column 3 -->

		</tr>
		<tr>
			<td colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="15"></td>
		</tr>
		<tr>
			<td></td>
			<td colspan="2">
				<p/>
		        <table border="0" cellspacing="0" cellpadding="0" align="center">
		          <tr>
<td><input type="submit" class="button175" onclick="window.location.href='/do/tools/submissionHistory/'; return false;" value="submission history" /> </td>
					<td width='15'  ></td>
<td><input type="button" onclick="javascript:print(); return false;" name="actionLabel" class="button100Lg" value="print"/></td>
					<td width='15'  ></td>
<td><input type="submit" class="button175" onclick="window.location.href='/do/tools/submissionComboUpload/'; return false;" value="create new upload" /> </td>
		          </tr>
		        </table>
			</td>
			<td></td>
		</tr>
		<tr align="center">
			<td colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="15"></td>
		</tr>
		<tr align="center">
		<td colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="15"></td>
		</tr>

<c:if test="${detail.displayDebitFootnote ==true}">
		<tr>
		  <td width="20">&nbsp;</td>
		  <td height="20"><span class="disclaimer"><content:getAttribute beanName="fileUploadPaymentNote" attribute="text"/></span>
		  </td>
		  <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
		  <td></td>
		</tr>
</c:if>
	</table>
	<br>
