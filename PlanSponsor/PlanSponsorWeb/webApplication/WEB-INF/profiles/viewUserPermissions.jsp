<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@page import="org.owasp.encoder.Encode"%>

<%@ page import="com.manulife.pension.ps.web.profiles.UserPermissionsForm"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.service.security.valueobject.DefaultRolePermissions" %>
<%@ page import="com.manulife.pension.ps.web.profiles.UserPermissions" %>
<content:contentBean
	contentId="<%=ContentConstants.USER_MANAGEMENT_GROUP_TITLE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="groupTitleUserManagement" />

<content:contentBean
	contentId="<%=ContentConstants.REPORTING_GROUP_TITLE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="groupTitleReporting" />

<content:contentBean
	contentId="<%=ContentConstants.CLIENT_SERVICES_GROUP_TITLE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="groupTitleClientServices" />
	
<%
UserPermissionsForm theForm = (UserPermissionsForm)session.getAttribute("userPermissionsForm");
pageContext.setAttribute("theForm",theForm,PageContext.PAGE_SCOPE);

UserPermissions userPermissions= theForm.getUserPermissions();
pageContext.setAttribute("userPermissions",userPermissions,PageContext.PAGE_SCOPE);

%>



<script>
	var submitted = false;
	function doSubmit() {
		if (!submitted) {
			submitted = true;
			return true;
		} else {
			window.status = "Transaction already in progress.  Please wait.";
			return false;
		}
	}
</script>

<div id="errordivcs"><content:errors scope="session" /></div>

<ps:form method="POST" action="/do/profiles/userPermissions/" name="userPermissionsForm" modelAttribute="userPermissionsForm">
	<input id="fromPage" type="hidden" value="<%=Encode.forHtmlAttribute(request.getParameter("fromPage"))%>">

	<TABLE cellSpacing=0 cellPadding=0 width=760 border=0>
		<TBODY>
			<TR>
				<TD>
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
						<TR>
							<TD><IMG height=1 src="/assets/unmanaged/images/s.gif"
								width=30><BR />
							<IMG height=1 src="/assets/unmanaged/images/s.gif"></TD>
							<TD width=500><BR />
							<jsp:include flush="true" page="userPermissionsHeader.jsp"></jsp:include>
							<BR />

								<TABLE class=box style="CURSOR: pointer"
									onclick="expandSection('sc1')" cellSpacing=0 cellPadding=0
									width=412 border=0>
									<TBODY>
										<TR class=tablehead>
											<TD class=tableheadTD1 colSpan=3><img id="sc1img"
												src="/assets/unmanaged/images/plus_icon.gif">&nbsp;&nbsp;<b><content:getAttribute beanName="groupTitleUserManagement" attribute="text"/></b></TD>
										</TR>
									</TBODY>
								</TABLE>
								<DIV class=switchcontent id=sc1>
								<TABLE class=box cellSpacing=0 cellPadding=0 width=412 border=0>
									<TBODY>
										<TR>
											<TD class=boxborder width=1><IMG height=1
												src="/assets/unmanaged/images/s.gif" width=1></TD>
											<TD>
											<TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
												<TBODY>

<c:if test="${userPermissions.selectedAccess ==false}">
<c:if test="${userPermissions.showManageUsers ==true}">

														<tr valign="top" class="datacell2">
															<td width="219"><strong>Manage users </strong></td>
															<td width="75" align="left" nowrap="nowrap">
																<% if (theForm.isBusinessConverted() && userPermissions.isManageUsers() == (userPermissions.getManageUsersDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getManageUsersDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.manageUsers ==true}"> Yes</c:if> <c:if test="${userPermissions.manageUsers !=true}"> No</c:if></td>




																<% if (theForm.isBusinessConverted() && userPermissions.isManageUsers() == (userPermissions.getManageUsersDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getManageUsersDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
															<td width="50" />
														</tr>
</c:if>
</c:if>

<c:if test="${userPermissions.showSelectedAccess ==true}">

														<tr valign="top" class="datacell2">
															<td width="219"><strong>Selected access </strong></td>
															<td width="75" align="left" nowrap="nowrap">
																<% if (theForm.isBusinessConverted() && userPermissions.isSelectedAccess() == (userPermissions.getSelectedAccessDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getSelectedAccessDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.selectedAccess ==true}"> Yes</c:if> <c:if test="${userPermissions.selectedAccess !=true}"> No</c:if></td>




																<% if (theForm.isBusinessConverted() && userPermissions.isSelectedAccess() == (userPermissions.getSelectedAccessDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getSelectedAccessDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
															<td width="50" />
														</tr>
</c:if>

													
												</TBODY>
											</TABLE>
											</TD>
											<TD class=boxborder width=1><IMG height=1src=
												"/assets/unmanaged/images/s.gif" width=1></TD>
										</TR>
										<TR>
											<TD colSpan=3>
											<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
												<TBODY>
													<TR>
														<TD width="1" class=boxborder><IMG height=1
															src="/assets/unmanaged/images/s.gif" width=1></TD>
														<TD class=boxborder><IMG height=1
															src="/assets/unmanaged/images/s.gif" width=1></TD>
													</TR>
												</TBODY>
											</TABLE>
											</TD>
										</TR>
									</TBODY>
								</TABLE>
								</DIV>&nbsp;<BR />

							<% if (!userPermissions.isSelectedAccess() && (userPermissions.isShowDownloadReports() || userPermissions.isShowEmployerStatements())) { %>
								<TABLE class=box style="CURSOR: pointer"
									onclick="expandSection('sc2')" cellSpacing=0 cellPadding=0
									width=412 border=0>
									<TBODY>
										<TR class=tablehead>
											<TD class=tableheadTD1 colSpan=3><img id="sc2img"
												src="/assets/unmanaged/images/plus_icon.gif">&nbsp;&nbsp;<B><content:getAttribute beanName="groupTitleReporting" attribute="text"/></B>
											</TD>
										</TR>
									</TBODY>
								</TABLE>
								<DIV class=switchcontent id=sc2>
								<TABLE class=box cellSpacing=0 cellPadding=0 width=412 border=0>
									<TBODY>
										<TR>
											<TD class=boxborder width=1><IMG height=1
												src="/assets/unmanaged/images/s.gif" width=1></TD>
											<TD width=644>
											<TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
												<TBODY>

<c:if test="${userPermissions.showDownloadReports ==true}">

														<tr valign="top" class="datacell2">
															<td width="219"><strong>Download reports -
															full SSN </strong></td>
															<td width="75" align="left" nowrap="nowrap">
																<% if (theForm.isBusinessConverted() && userPermissions.isDownloadReports() == (userPermissions.getDownloadReportsDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getDownloadReportsDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.downloadReports ==true}"> Yes</c:if> <c:if test="${userPermissions.downloadReports !=true}"> No</c:if></td>




																<% if (theForm.isBusinessConverted() && userPermissions.isDownloadReports() == (userPermissions.getDownloadReportsDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getDownloadReportsDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
															<td width="50" />
														</tr>
</c:if>

<c:if test="${userPermissions.showEmployerStatements ==true}">

														<tr valign="top" class="datacell2">
															<td width="219"><strong>Employer statements
															</strong></td>
															<td width="75" align="left" nowrap="nowrap">
																<% if (theForm.isBusinessConverted() && userPermissions.isEmployerStatements() == (userPermissions.getEmployerStatementsDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getEmployerStatementsDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.employerStatements ==true}"> Yes</c:if> <c:if test="${userPermissions.employerStatements !=true}"> No</c:if></td>




																<% if (theForm.isBusinessConverted() && userPermissions.isEmployerStatements() == (userPermissions.getEmployerStatementsDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getEmployerStatementsDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
															<td width="50" />
														</tr>
</c:if>

												</TBODY>
											</TABLE>
											</TD>
											<TD width=1 bgcolor="#D9DAE8" class=boxborder><IMG
												height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
										</TR>
										<TR>
											<TD colSpan=3>
											<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
												<TBODY>
													<TR>
														<TD width="1" class=boxborder><IMG height=1
															src="/assets/unmanaged/images/s.gif" width=1></TD>
														<TD class=boxborder><IMG height=1
															src="/assets/unmanaged/images/s.gif" width=1></TD>
													</TR>
												</TBODY>
											</TABLE>
											</TD>
										</TR>
									</TBODY>
								</TABLE>
								</DIV>
								<BR />
							<% } %>

							<% if (userPermissions.isShowSubmissionsSection() || userPermissions.isShowWithdrawalsSection() || ( !userPermissions.isSelectedAccess() && (userPermissions.isShowUpdateCensusData() || userPermissions.isShowViewSalary()))) { %>
								<TABLE class=box style="CURSOR: pointer"
									onclick="expandSection('sc4')" cellSpacing=0 cellPadding=0
									width=412 border=0>
									<TBODY>
										<TR class=tablehead>
											<TD class=tableheadTD1 colSpan=3><img id="sc4img"
												src="/assets/unmanaged/images/plus_icon.gif">&nbsp;&nbsp;<B><content:getAttribute beanName="groupTitleClientServices" attribute="text"/></B></TD>
										</TR>
									</TBODY>
								</TABLE>
								<DIV class=switchcontent id=sc4>
								<TABLE class=box cellSpacing=0 cellPadding=0 width=412 border=0>
									<TBODY>
										<TR>
											<TD class=boxborder width=1><IMG height=1
												src="/assets/unmanaged/images/s.gif" width=1></TD>
											<TD width=733>
											<TABLE cellSpacing=0 cellPadding=0 width=410 border=0>
												<TBODY>
													<%-- SIGNING AUTHORITY -LOANS - BEGIN --%>	
<c:if test="${userPermissions.showSigningAuthority ==true}">

													        <tr valign="top" class="datacell2">
            													<td width="219"><strong>Signing authority </strong></td>
																<td width="75" align="left" nowrap="nowrap">
																	<% if (userPermissions.isSigningAuthority() == (userPermissions.getSigningAuthorityDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getSigningAuthorityDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.signingAuthority ==true}"> Yes</c:if> <c:if test="${userPermissions.signingAuthority !=true}"> No</c:if></td>




																	<% if (userPermissions.isSigningAuthority() == (userPermissions.getSigningAuthorityDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getSigningAuthorityDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
																<td width="50" />
															</tr>
</c:if>
														
														<%-- Notice Manager--%>
<c:if test="${userPermissions.selectedAccess ==false}">
<c:if test="${userPermissions.showNoticeManager ==true}">

															<tr valign="top" class="datacell2">
																<td width="219"><strong>Access to Notice Manager</strong></td>
																<td width="75" align="left" nowrap="nowrap">
																	<% if (theForm.isBusinessConverted() && userPermissions.isNoticeManager() == (userPermissions.getNoticeManagerDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getNoticeManagerDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.noticeManager ==true}"> Yes</c:if> <c:if test="${userPermissions.noticeManager !=true}"> No</c:if></td>




															<% if (theForm.isBusinessConverted() && userPermissions.isNoticeManager() == (userPermissions.getNoticeManagerDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getNoticeManagerDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
																<td width="50" />
															</tr>
</c:if>
</c:if>
														<%-- Notice Manager End--%>
													<%-- SIGNING AUTHORITY -LOANS - END --%>	
<c:if test="${userPermissions.showSubmissionsSection ==true}">

														<tr valign="top">
															<td colspan="3" class="tablesubhead"><b>Submissions</b></td>
														</tr>

<c:if test="${userPermissions.showViewSubmissions ==true}">

															<tr valign="top" class="datacell2">
																<td width="219"><strong>View submissions </strong></td>
																<td width="75" align="left" nowrap="nowrap">
																	<% if (theForm.isBusinessConverted() && userPermissions.isViewSubmissions() == (userPermissions.getViewSubmissionsDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getViewSubmissionsDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.viewSubmissions ==true}"> Yes</c:if> <c:if test="${userPermissions.viewSubmissions !=true}"> No</c:if></td>




																	<% if (theForm.isBusinessConverted() && userPermissions.isViewSubmissions() == (userPermissions.getViewSubmissionsDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getViewSubmissionsDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
																<td width="50" />
															</tr>
</c:if>

<c:if test="${userPermissions.showCreateUploadSubmissions ==true}">

															<tr valign="top" class="datacell2">
																<td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Create/upload
																submissions </strong></td>
																<td width="75" align="left" nowrap="nowrap">
																	<% if (theForm.isBusinessConverted() && userPermissions.isCreateUploadSubmissions() == (userPermissions.getCreateUploadSubmissionsDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getCreateUploadSubmissionsDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.createUploadSubmissions ==true}"> Yes</c:if>


<c:if test="${userPermissions.createUploadSubmissions !=true}"> No</c:if>

																	<% if (theForm.isBusinessConverted() && userPermissions.isCreateUploadSubmissions() == (userPermissions.getCreateUploadSubmissionsDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getCreateUploadSubmissionsDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
																</td>
																<td width="50" />
															</tr>
</c:if>

<c:if test="${userPermissions.selectedAccess ==false}">
<c:if test="${userPermissions.showViewAllUsersSubmissions ==true}">

															<tr valign="top" class="datacell2">
																<td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;View
																all users' submissions </strong></td>
																<td width="75" align="left" nowrap="nowrap">
																	<% if (theForm.isBusinessConverted() && userPermissions.isViewAllUsersSubmissions() == (userPermissions.getViewAllUsersSubmissionsDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getViewAllUsersSubmissionsDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.viewAllUsersSubmissions ==true}"> Yes</c:if>


<c:if test="${userPermissions.viewAllUsersSubmissions !=true}"> No</c:if>

																	<% if (theForm.isBusinessConverted() && userPermissions.isViewAllUsersSubmissions() == (userPermissions.getViewAllUsersSubmissionsDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getViewAllUsersSubmissionsDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
																</td>
																<td width="50" />
</c:if>
</c:if>

<c:if test="${userPermissions.showCashAccount ==true}">

															</tr>
															<tr valign="top" class="datacell2">
																<td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Cash
																account </strong></td>
																<td width="75" align="left" nowrap="nowrap">
																	<% if (theForm.isBusinessConverted() && userPermissions.isCashAccount() == (userPermissions.getCashAccountDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getCashAccountDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.cashAccount ==true}"> Yes</c:if> <c:if test="${userPermissions.cashAccount !=true}"> No</c:if></td>




																	<% if (theForm.isBusinessConverted() && userPermissions.isCashAccount() == (userPermissions.getCashAccountDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getCashAccountDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
																<td width="50" />
															</tr>
</c:if>

<c:if test="${userPermissions.showDirectDebit ==true}">

															<tr valign="top" class="datacell2">
																<td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Direct
																debit </strong></td>
																<td width="75" align="left" nowrap="nowrap">
																	<% if (theForm.isBusinessConverted() && userPermissions.isDirectDebit() == (userPermissions.getDirectDebitDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getDirectDebitDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.directDebit ==true}"> Yes</c:if> <c:if test="${userPermissions.directDebit !=true}"> No</c:if></td>




																	<% if (theForm.isBusinessConverted() && userPermissions.isDirectDebit() == (userPermissions.getDirectDebitDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getDirectDebitDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
																<td width="50" />
															</tr>

<c:if test="${not empty userPermissions.selectedDirectDebitAccountsAsList}">

																<tr valign="top" class="datacell2">
<td align="left" nowrap colspan="3"><c:forEach items="${userPermissions.selectedDirectDebitAccountsAsList}" var="account" >



&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${account.label}

																		<br />
</c:forEach></td>
																</tr>
</c:if>

</c:if>

</c:if>

<!-- Loans Permissions -->
<c:if test="${userPermissions.showLoansPermissions ==true}">

														<tr valign="top">
															<td colspan="3" class="tablesubhead"><b>Loans</b></td>
														</tr>

<c:if test="${userPermissions.showInitiateLoans ==true}">

															<tr valign="top" class="datacell2">
																<td width="219"><strong>Initiate loans </strong></td>
																<td width="75" align="left" nowrap="nowrap">
																	<% if (theForm.isBusinessConverted() && userPermissions.isInitiateLoans() == (userPermissions.getInitiateLoansDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getInitiateLoansDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.initiateLoans ==true}"> Yes</c:if>


<c:if test="${userPermissions.initiateLoans !=true}"> No</c:if>

																	<% if (theForm.isBusinessConverted() && userPermissions.isInitiateLoans() == (userPermissions.getInitiateLoansDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getInitiateLoansDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
																</td>
																<td width="50" />
															</tr>
</c:if>

<c:if test="${userPermissions.showViewAllLoans ==true}">

															<tr valign="top" class="datacell2">
																<td width="219"><strong>View all loans </strong></td>
																<td width="75" align="left" nowrap="nowrap">
																	<% if (theForm.isBusinessConverted() && userPermissions.isViewAllLoans() == (userPermissions.getViewAllLoansDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getViewAllLoansDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.viewAllLoans ==true}"> Yes</c:if> <c:if test="${userPermissions.viewAllLoans !=true}"> No</c:if></td>




																	<% if (theForm.isBusinessConverted() && userPermissions.isViewAllLoans() == (userPermissions.getViewAllLoansDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getViewAllLoansDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
																<td width="50" />
															</tr>
</c:if>

<c:if test="${userPermissions.showReviewLoans ==true}">

															<tr valign="top" class="datacell2">
																<td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Review
																loans </strong></td>
																<td width="75" align="left" nowrap="nowrap">
																	<% if (theForm.isBusinessConverted() && userPermissions.isReviewLoans() == (userPermissions.getReviewLoansDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getReviewLoansDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.reviewLoans ==true}"> Yes</c:if> <c:if test="${userPermissions.reviewLoans !=true}"> No</c:if></td>




																	<% if (theForm.isBusinessConverted() && userPermissions.isReviewLoans() == (userPermissions.getReviewLoansDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getReviewLoansDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
																<td width="50" />
															</tr>
</c:if>
</c:if>
<!--  Loans permissions end -->
<c:if test="${userPermissions.showWithdrawalsSection ==true}">

														<tr valign="top">
															<td colspan="3" class="tablesubhead"><b>i:withdrawals</b></td>
														</tr>

<c:if test="${userPermissions.showInitiateAndViewMyWithdrawals ==true}">

															<tr valign="top" class="datacell2">
																<td width="219"><strong>Initiate i:withdrawals </strong></td>
																<td width="75" align="left" nowrap="nowrap">
																	<% if (theForm.isBusinessConverted() && userPermissions.isInitiateAndViewMyWithdrawals() == (userPermissions.getInitiateAndViewMyWithdrawalsDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getInitiateAndViewMyWithdrawalsDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.initiateAndViewMyWithdrawals ==true}"> Yes</c:if>


<c:if test="${userPermissions.initiateAndViewMyWithdrawals !=true}"> No</c:if>

																	<% if (theForm.isBusinessConverted() && userPermissions.isInitiateAndViewMyWithdrawals() == (userPermissions.getInitiateAndViewMyWithdrawalsDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getInitiateAndViewMyWithdrawalsDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
																</td>
																<td width="50" />
															</tr>
</c:if>

<c:if test="${userPermissions.showViewAllWithdrawals ==true}">

															<tr valign="top" class="datacell2">
																<td width="219"><strong>View all
																i:withdrawals </strong></td>
																<td width="75" align="left" nowrap="nowrap">
																	<% if (theForm.isBusinessConverted() && userPermissions.isViewAllWithdrawals() == (userPermissions.getViewAllWithdrawalsDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getViewAllWithdrawalsDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.viewAllWithdrawals ==true}"> Yes</c:if> <c:if test="${userPermissions.viewAllWithdrawals !=true}"> No</c:if></td>




																	<% if (theForm.isBusinessConverted() && userPermissions.isViewAllWithdrawals() == (userPermissions.getViewAllWithdrawalsDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getViewAllWithdrawalsDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
																<td width="50" />
															</tr>
</c:if>

<c:if test="${userPermissions.showReviewWithdrawals ==true}">

															<tr valign="top" class="datacell2">
																<td width="219"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Review
																i:withdrawals </strong></td>
																<td width="75" align="left" nowrap="nowrap">
																	<% if (theForm.isBusinessConverted() && userPermissions.isReviewWithdrawals() == (userPermissions.getReviewWithdrawalsDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getReviewWithdrawalsDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.reviewWithdrawals ==true}"> Yes</c:if> <c:if test="${userPermissions.reviewWithdrawals !=true}"> No</c:if></td>




																	<% if (theForm.isBusinessConverted() && userPermissions.isReviewWithdrawals() == (userPermissions.getReviewWithdrawalsDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getReviewWithdrawalsDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
																<td width="50" />
															</tr>
</c:if>
</c:if>

													<% if (!userPermissions.isSelectedAccess() && (userPermissions.isShowUpdateCensusData() || userPermissions.isShowViewSalary())) { %>
														<tr valign="top">
															<td colspan="3" class="tablesubhead"><b>Employee
															Management</b></td>
														</tr>

<c:if test="${userPermissions.showUpdateCensusData ==true}">

															<tr valign="top" class="datacell2">
																<td width="219"><strong>Update census data
																</strong></td>
																<td width="75" align="left" nowrap="nowrap">
																	<% if (theForm.isBusinessConverted() && userPermissions.isUpdateCensusData() == (userPermissions.getUpdateCensusDataDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getUpdateCensusDataDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.updateCensusData ==true}"> Yes</c:if> <c:if test="${userPermissions.updateCensusData !=true}"> No</c:if></td>




																	<% if (theForm.isBusinessConverted() && userPermissions.isUpdateCensusData() == (userPermissions.getUpdateCensusDataDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getUpdateCensusDataDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
																<td width="50" />
															</tr>
</c:if>

<c:if test="${userPermissions.showViewSalary ==true}">

															<tr valign="top" class="datacell2">
																<td width="219"><strong>View salary </strong></td>
																<td width="75" align="left" nowrap="nowrap">
																	<% if (theForm.isBusinessConverted() && userPermissions.isViewSalary() == (userPermissions.getViewSalaryDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getViewSalaryDefault().equals(DefaultRolePermissions.YES))) { %><b><u><% } %>
<c:if test="${userPermissions.viewSalary ==true}"> Yes</c:if> <c:if test="${userPermissions.viewSalary !=true}"> No</c:if></td>




																	<% if (theForm.isBusinessConverted() && userPermissions.isViewSalary() == (userPermissions.getViewSalaryDefault().equals(DefaultRolePermissions.TRUE) || userPermissions.getViewSalaryDefault().equals(DefaultRolePermissions.YES))) { %></b></u><% } %>
																<td width="50" />
															</tr>
</c:if>
													<% } %>

												</TBODY>
											</TABLE>
											</TD>
											<TD class=boxborder width=1><IMG height=1
												src="/assets/unmanaged/images/s.gif" width=1></TD>
										</TR>
										<TR>
											<TD colSpan=3>
											<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
												<TBODY>
													<TR>
														<TD width="1" class=boxborder><IMG height=1
															src="/assets/unmanaged/images/s.gif" width=1></TD>
														<TD class=boxborder><IMG height=1
															src="/assets/unmanaged/images/s.gif" width=1></TD>
													</TR>
												</TBODY>
											</TABLE>
											</TD>
										</TR>
									</TBODY>
								</TABLE>
								</DIV>
								<BR />
							<% } %>

							<BR />
							<BR />
							<TABLE cellSpacing=0 cellPadding=0 width=412 border=0>
								<TBODY>
									<TR>
										<TD width=175 align=right>
										<div align="center"><INPUT class="button134"
											name="action" type="submit"
											value="back"
											onclick="return doSubmit();">
										</TD>
										<TD align=right width=175>&nbsp;</TD>
										<TD align=right width=175>&nbsp;</TD>
									</TR>
								</TBODY>
							</TABLE>
							</A></TD>
							<TD width=260>&nbsp;</TD>
							<!-- end main content table -->
						</TR>
						<TR>
							<TD>&nbsp;</TD>
							<TD colSpan=2>&nbsp;</TD>
						</TR>
					</TBODY>
				</TABLE>

				</ps:form>
