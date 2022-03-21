<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.profiles.UserPermissions"%>
<%@ page import="com.manulife.pension.ps.web.profiles.BankAccount"%>
<%@ page import="com.manulife.pension.ps.web.profiles.AddEditClientUserForm" %>
<%@ page import="com.manulife.pension.ps.web.profiles.ClientUserContractAccess" %>


 <% 
  String theIndex = pageContext.getAttribute("indexValue").toString();
 ClientUserContractAccess contractAccesses = (ClientUserContractAccess)pageContext.getAttribute("contractAccesses");

 %>
<content:contentBean
	contentId="<%=ContentConstants.USER_MANAGEMENT_GROUP_TITLE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="groupTitleUserManagement" />

<content:contentBean
	contentId="<%=ContentConstants.REPORTING_GROUP_TITLE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="groupTitleReporting" />

<content:contentBean
	contentId="<%=ContentConstants.PLAN_SERVICES_GROUP_TITLE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="groupTitlePlanServices" />

<content:contentBean
	contentId="<%=ContentConstants.CLIENT_SERVICES_GROUP_TITLE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="groupTitleClientServices" />
	
<content:contentBean
	contentId="<%=ContentConstants.EMPLOYEE_MANAGEMENT_GROUP_TITLE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="groupTitleEmployeeManagement" />

<c:set var="userPermissions" value="${contractAccesses.userPermissions}" />

<%UserPermissions userPermissions =(UserPermissions)pageContext.getAttribute("userPermissions"); %>
<c:if test="${empty param.printFriendly }" >	
	<DIV class="switchcontent" id="sc${indexValue}">
</c:if>

<c:if test="${not empty param.printFriendly }" >	
	<DIV class="switchcontent" id="sc${indexValue}" style="display:block">
</c:if>

<table width="705" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td valign="top" bgcolor="#FFF9F2">
		<TABLE class=box cellSpacing=0 cellPadding=0 width=500 border=0>
			<TBODY>
				<TR class=tablehead>
					<TD class=tableheadTD1 colSpan=3><b>Permissions </b></TD>
				</TR>
			</TBODY>
		</TABLE>
		<!-- user management begin -->
<c:if test="${userPermissions.showUserManagementSection ==true}">
			<TABLE class=box cellSpacing=0 cellPadding=0 width=500 border=0>
				<TBODY>
					<TR class=tablehead>
						<TD class=tablesubhead colSpan=3><b><content:getAttribute
				beanName="groupTitleUserManagement" attribute="text" /></b></TD>
					</TR>
				</TBODY>
			</TABLE>
			<TABLE class=box cellSpacing=0 cellPadding=0 width=500 border=0>
				<TBODY>
					<TR>
						<TD class=boxborder width=1><IMG height=1 src="/assets/unmanaged/images/s.gif"
							width=1></TD>
						<TD>
						<TABLE cellSpacing=0 cellPadding=0 width=100% border=0>
							<TBODY>
<c:if test="${userPermissions.selectedAccess ==false}">
<c:if test="${userPermissions.showManageUsers ==true}">
								<tr valign="top">
									<td width="200" class="datacell2"><strong>Manage users </strong></td>
									<td width="100" align="left" nowrap="nowrap" class="datacell2">
									<div align="left">
									<% 
										String manageUsersStr = "allContractAccesses[" + theIndex +"].userPermissions.manageUsers";
									%>
<c:if test="${userPermissions.manageUsers ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=manageUsersStr%>">Yes</ps:highlightIfChanged>
</c:if>
									<c:if test="${userPermissions.manageUsers != true}">
										<ps:highlightIfChanged name="theForm" property="<%=manageUsersStr%>">No</ps:highlightIfChanged>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
</c:if>
<c:if test="${userPermissions.showSelectedAccess ==true}">
								<tr valign="top">
									<td width="200" class="datacell2"><strong>Selected access</strong></td>
									<td width="100" align="left" nowrap="nowrap" class="datacell2">
									<div align="left">
									<% 
										String selectAccessStr = "allContractAccesses[" + theIndex +"].userPermissions.selectedAccess";
									%>
<c:if test="${userPermissions.selectedAccess ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=selectAccessStr%>">Yes</ps:highlightIfChanged>
</c:if>		
									
									<c:if test="${userPermissions.selectedAccess !=true}">
										<ps:highlightIfChanged name="theForm" property="<%=selectAccessStr%>">No</ps:highlightIfChanged>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
							</TBODY>
						</TABLE>
						</TD>
						<TD class=boxborder width=1><IMG height=1 src="/assets/unmanaged/images/s.gif"	width=1></TD>
					</TR>
				</TBODY>
			</TABLE>
</c:if>
			<!-- user management end -->
			<!-- Reporting begin -->
			<% if (!userPermissions.isSelectedAccess() && (userPermissions.isShowDownloadReports() || userPermissions.isShowEmployerStatements())) { %>
			<TABLE class=box cellSpacing=0 cellPadding=0 width=500 border=0>
				<TBODY>
					<TR class=tablehead>
						<TD class=tablesubhead colSpan=3><B><content:getAttribute
				beanName="groupTitleReporting" attribute="text" /></B></TD>
					</TR>
				</TBODY>
			</TABLE>
			<TABLE class=box cellSpacing=0 cellPadding=0 width=500 border=0>
				<TBODY>
					<TR>
						<TD class=boxborder width=1><IMG height=1 src="/assets/unmanaged/images/s.gif"
							width=1></TD>
						<TD width=644>
						<TABLE cellSpacing=0 cellPadding=0 width=100% border=0>
							<TBODY>
<c:if test="${userPermissions.showDownloadReports ==true}">
								<tr valign="top">
									<td width="200" class="datacell2"><strong>Download reports - full SSN</strong></td>
									<td width="100" align="left" nowrap="nowrap" class="datacell2">
									<div align="left">
									<% 
										String downloadReportStr = "allContractAccesses[" + theIndex +"].userPermissions.downloadReports";
									%>
<c:if test="${userPermissions.downloadReports ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=downloadReportStr%>">Yes</ps:highlightIfChanged>
</c:if>
									<c:if test="${userPermissions.downloadReports !=true}">
										<ps:highlightIfChanged name="theForm" property="<%=downloadReportStr%>">No</ps:highlightIfChanged>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
<c:if test="${userPermissions.showEmployerStatements ==true}">
								<tr valign="top">
									<td width="200" class="datacell2"><strong>Employer
									statements</strong></td>
									<td width="100" align="left" nowrap="nowrap" class="datacell2">
									<div align="left">
									<% 
										String employerStatementsStr = "allContractAccesses[" + theIndex +"].userPermissions.employerStatements";
									%>
<c:if test="${userPermissions.employerStatements ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=employerStatementsStr%>">Yes</ps:highlightIfChanged>
</c:if>
									<c:if test="${userPermissions.employerStatements !=true}">
										<ps:highlightIfChanged name="theForm" property="<%=employerStatementsStr%>">No</ps:highlightIfChanged>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
							</TBODY>
						</TABLE>
						</TD>
						<TD width=1 bgcolor="#D9DAE8" class=boxborder><IMG height=1	src="/assets/unmanaged/images/s.gif" width=1></TD>
					</TR>
				</TBODY>
			</TABLE>
			<% } %>
			<!-- reporting end -->
			<!-- client services begin -->
			<% if (userPermissions.isShowSigningAuthority() || userPermissions.isShowSubmissionsSection() || userPermissions.isShowWithdrawalsSection() || ( !userPermissions.isSelectedAccess() && (userPermissions.isShowUpdateCensusData() || userPermissions.isShowViewSalary()))) { %>
			<TABLE class=box cellSpacing=0 cellPadding=0 width=500 border=0>
				<TBODY>
					<TR class=tablehead>
						<TD colSpan=3 class=tablesubhead style7><B><content:getAttribute
				beanName="groupTitleClientServices" attribute="text" /></B></TD>
					</TR>
				</TBODY>
			</TABLE>
			<TABLE class=box cellSpacing=0 cellPadding=0 width=500 border=0>
				<TBODY>
					<TR>
						<TD width=1 class=boxborder style7><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
						<TD width=733 class="style7">
						<TABLE cellSpacing=0 cellPadding=0 width=100% border=0>
							<TBODY>
								<%-- SIGNING AUTHORITY - LOANS - BEGIN --%>
<c:if test="${userPermissions.showSigningAuthority ==true}">
					            <tr valign="top" class="datacell2">
            						<td width="219"><strong>Signing authority </strong></td>								
									<td width="100" align="left" nowrap="nowrap" class="datacell2">
									<div align="left">
									<% 
										String signingAuthorityStr = "allContractAccesses[" + theIndex +"].userPermissions.signingAuthority";
									%>
<c:if test="${userPermissions.signingAuthority ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=signingAuthorityStr%>">Yes</ps:highlightIfChanged>
</c:if>
									<c:if test="${userPermissions.signingAuthority !=true}">
										<ps:highlightIfChanged name="theForm" property="<%=signingAuthorityStr%>">No</ps:highlightIfChanged>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
								
							<!---Notice Manager begin -->
			
<c:if test="${userPermissions.selectedAccess ==false}">
<c:if test="${userPermissions.showNoticeManager ==true}">
								<tr valign="top">
									<td width="200" class="datacell2">
									<strong>Access to Notice Manager</strong></td>
									<td width="100" align="left" nowrap="nowrap" class="datacell2">
									<div align="left">
									<% 
										String noticeManagerStr = "allContractAccesses[" + theIndex +"].userPermissions.noticeManager";
									%>
<c:if test="${userPermissions.noticeManager ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=noticeManagerStr%>">Yes</ps:highlightIfChanged>
</c:if>
									<c:if test="${userPermissions.noticeManager !=true}">
										<ps:highlightIfChanged name="theForm" property="<%=noticeManagerStr%>">No</ps:highlightIfChanged>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
</c:if>
				
								<%-- SIGNING AUTHORITY - LOANS - END --%>															
<c:if test="${userPermissions.showSubmissionsSection ==true}">
								<tr valign="top">
									<td colspan="2" bgcolor="#E6E6E6"><b>Submissions</b></td>
								</tr>
<c:if test="${userPermissions.showViewSubmissions ==true}">
								<tr valign="top">
									<td width="200" class="datacell2"><strong>View submissions</strong></td>
									<td width="100" align="left" nowrap="nowrap" class="datacell2">
									<div align="left">
									<% 
										String viewSubmissionsStr = "allContractAccesses[" + theIndex +"].userPermissions.viewSubmissions";
									%>
<c:if test="${userPermissions.viewSubmissions ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=viewSubmissionsStr%>">Yes</ps:highlightIfChanged>
</c:if>
									<c:if test="${userPermissions.viewSubmissions !=true}">
										<ps:highlightIfChanged name="theForm" property="<%=viewSubmissionsStr%>">No</ps:highlightIfChanged>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
<c:if test="${userPermissions.showCreateUploadSubmissions ==true}">
								<tr valign="top">
									<td width="200" class="datacell2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<strong> Create/Upload submissions </strong></td>
									<td width="100" align="left" nowrap="nowrap" class="datacell2">
									<div align="left">
									<% 
										String createUploadSubmissionsStr = "allContractAccesses[" + theIndex +"].userPermissions.createUploadSubmissions";
									%>
<c:if test="${userPermissions.createUploadSubmissions ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=createUploadSubmissionsStr%>">Yes</ps:highlightIfChanged>
</c:if>
<c:if test="${userPermissions.createUploadSubmissions !=true}">
										<ps:highlightIfChanged name="theForm" property="<%=createUploadSubmissionsStr%>">No</ps:highlightIfChanged>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
<c:if test="${userPermissions.selectedAccess ==false}">
<c:if test="${userPermissions.showViewAllUsersSubmissions ==true}">
								<tr valign="top">
									<td width="200" class="datacell2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<strong>View all users' submissions</strong></td>
									<td width="100" align="left" nowrap="nowrap" class="datacell2">
									<div align="left">
									<% 
										String viewAllUsersSubmissionsStr = "allContractAccesses[" + theIndex +"].userPermissions.viewAllUsersSubmissions";
									%>
<c:if test="${userPermissions.viewAllUsersSubmissions ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=viewAllUsersSubmissionsStr%>">Yes</ps:highlightIfChanged>
</c:if>
<c:if test="${userPermissions.viewAllUsersSubmissions !=true}">
										<ps:highlightIfChanged name="theForm" property="<%=viewAllUsersSubmissionsStr%>">No</ps:highlightIfChanged>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
</c:if>
<c:if test="${userPermissions.showCashAccount ==true}">
								<tr valign="top">
									<td width="200" class="datacell2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<strong>Cash account</strong></td>
									<td width="100" align="left" nowrap="nowrap" class="datacell2">
									<div align="left">
									<% 
										String cashAccountStr = "allContractAccesses[" + theIndex +"].userPermissions.cashAccount";
									%>
<c:if test="${userPermissions.cashAccount ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=cashAccountStr%>">Yes</ps:highlightIfChanged>
</c:if>
									<c:if test="${userPermissions.cashAccount !=true}">
										<ps:highlightIfChanged name="theForm" property="<%=cashAccountStr%>">No</ps:highlightIfChanged>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
<c:if test="${userPermissions.showDirectDebit ==true}">
								<tr valign="top">
									<td width="200" class="datacell2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<strong>Direct debit</strong></td>
									<td width="100" align="left" nowrap="nowrap" class="datacell2">
									<div align="left">
									<% 
										String directDebitStr = "allContractAccesses[" + theIndex +"].userPermissions.directDebit";
									%>
<c:if test="${userPermissions.directDebit ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=directDebitStr%>">Yes</ps:highlightIfChanged>
</c:if>
									<c:if test="${userPermissions.directDebit !=true}">
										<ps:highlightIfChanged name="theForm" property="<%=directDebitStr%>">No</ps:highlightIfChanged>
</c:if>
									</div>
									</td>
								</tr>

<c:if test="${not empty userPermissions.selectedDirectDebitAccountsAsList}">
									<tr valign="top"><td colspan="2" class="datacell2">
									<%
									AddEditClientUserForm theForm1 = (AddEditClientUserForm)request.getAttribute("theForm");
									com.manulife.pension.ps.web.profiles.ClientUserContractAccess oldContractAccess = ((AddEditClientUserForm) theForm1.getClonedForm()).findContractAccess(contractAccesses.getContractNumber().intValue());
									java.util.List oldAccountList = oldContractAccess == null ? new java.util.ArrayList() : oldContractAccess.getUserPermissions().getSelectedDirectDebitAccountsAsList();
									%>
<c:forEach items="${userPermissions.selectedDirectDebitAccountsAsList}" var="account" >
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										
										<%
										
										BankAccount account = (BankAccount)pageContext.getAttribute("account");
										if (!oldAccountList.contains(account)) {%>
										<span class="highlightBold">
										<%}%>
${account.label}
										<%if (!oldAccountList.contains(account)) {%>
										</span>
										<%}%>
										<br/>
</c:forEach>
									</td></tr>
</c:if>
</c:if>
</c:if>

<c:if test="${userPermissions.showLoansPermissions ==true}">
									<tr valign="top">
										<td colspan="2" bgcolor="#E6E6E6"><b>Loans</b></td>
									</tr>
<c:if test="${userPermissions.showInitiateLoans ==true}">
									<tr valign="top">
										<td width="200" class="datacell2"><strong>Initiate loans</strong></td>
										<td width="100" align="left" nowrap="nowrap" class="datacell2">
										<div align="left">
										<% 
										String initiateLoansStr = "allContractAccesses[" + theIndex +"].userPermissions.initiateLoans";
										%>
<c:if test="${userPermissions.initiateLoans ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=initiateLoansStr%>">Yes</ps:highlightIfChanged>
</c:if>
<c:if test="${userPermissions.initiateLoans !=true}">
											<ps:highlightIfChanged name="theForm" property="<%=initiateLoansStr%>">No</ps:highlightIfChanged>
</c:if>
										</div>
										</td>
									</tr>
</c:if>
<c:if test="${userPermissions.showViewAllLoans ==true}">
									<tr valign="top">
										<td width="200" class="datacell2"><strong>View all loans</strong></td>
										<td width="100" align="left" nowrap="nowrap" class="datacell2">
										<div align="left">
										<% 
										String viewAllLoansStr = "allContractAccesses[" + theIndex +"].userPermissions.viewAllLoans";
										%>
<c:if test="${userPermissions.viewAllLoans ==true}">
											<ps:highlightIfChanged name="theForm" property="<%=viewAllLoansStr%>">Yes</ps:highlightIfChanged>
</c:if>
										<c:if test="${userPermissions.viewAllLoans !=true}">
											<ps:highlightIfChanged name="theForm" property="<%=viewAllLoansStr%>">No</ps:highlightIfChanged>
</c:if>
										</div>
										</td>
									</tr>
</c:if>
<c:if test="${userPermissions.showReviewLoans ==true}">
									<tr valign="top">
										<td width="200" class="datacell2"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Review loans</strong></td>
										<td width="100" align="left" nowrap="nowrap" class="datacell2">
										<div align="left">
										<% 
										String reviewLoansStr = "allContractAccesses[" + theIndex +"].userPermissions.reviewLoans";
										%>
<c:if test="${userPermissions.reviewLoans ==true}">
											<ps:highlightIfChanged name="theForm" property="<%=reviewLoansStr%>">Yes</ps:highlightIfChanged>
</c:if>
										<c:if test="${userPermissions.reviewLoans ==true}">
											<ps:highlightIfChanged name="theForm" property="<%=reviewLoansStr%>">No</ps:highlightIfChanged>
</c:if>
										</div>
										</td>
									</tr>
</c:if>
</c:if>

<c:if test="${userPermissions.showWithdrawalsSection ==true}">
								<tr valign="top">
									<td colspan="2" bgcolor="#E6E6E6"><b>i:withdrawals</b></td>
								</tr>
<c:if test="${userPermissions.showInitiateAndViewMyWithdrawals ==true}">
								<tr valign="top">
									<td width="200" class="datacell2"><strong>Initiate i:withdrawals</strong></td>
									<td width="100" align="left" nowrap="nowrap" class="datacell2">
									<div align="left">
									<% 
										String initiateAndViewMyWithdrawalsStr = "allContractAccesses[" + theIndex +"].userPermissions.initiateAndViewMyWithdrawals";
									%>
<c:if test="${userPermissions.initiateAndViewMyWithdrawals ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=initiateAndViewMyWithdrawalsStr%>">Yes</ps:highlightIfChanged>
</c:if>
<c:if test="${userPermissions.initiateAndViewMyWithdrawals !=true}">
										<ps:highlightIfChanged name="theForm" property="<%=initiateAndViewMyWithdrawalsStr%>">No</ps:highlightIfChanged>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
<c:if test="${userPermissions.showViewAllWithdrawals ==true}">
								<tr valign="top">
									<td width="200" class="datacell2"><strong>View all i:withdrawals</strong></td>
									<td width="100" align="left" nowrap="nowrap" class="datacell2">
									<div align="left">
									<% 
										String viewAllWithdrawalsStr = "allContractAccesses[" + theIndex +"].userPermissions.viewAllWithdrawals";
									%>
<c:if test="${userPermissions.viewAllWithdrawals ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=viewAllWithdrawalsStr%>">Yes</ps:highlightIfChanged>
</c:if>
									<c:if test="${userPermissions.viewAllWithdrawals !=true}">
										<ps:highlightIfChanged name="theForm" property="<%=viewAllWithdrawalsStr%>">No</ps:highlightIfChanged>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
<c:if test="${userPermissions.showReviewWithdrawals ==true}">
								<tr valign="top">
									<td width="200" class="datacell2"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Review i:withdrawals</strong></td>
									<td width="100" align="left" nowrap="nowrap" class="datacell2">
									<div align="left">
									<% 
										String reviewWithdrawalsStr = "allContractAccesses[" + theIndex +"].userPermissions.reviewWithdrawals";
									%>
<c:if test="${userPermissions.reviewWithdrawals ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=reviewWithdrawalsStr%>">Yes</ps:highlightIfChanged>
</c:if>
									<c:if test="${userPermissions.reviewWithdrawals !=true}">
										<ps:highlightIfChanged name="theForm" property="<%=reviewWithdrawalsStr%>">No</ps:highlightIfChanged>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
</c:if>
								<% if (!userPermissions.isSelectedAccess() && (userPermissions.isShowUpdateCensusData() || userPermissions.isShowViewSalary())) { %>
								<tr valign="top">
									<td colspan="2" bgcolor="#E6E6E6"><b><content:getAttribute
				beanName="groupTitleEmployeeManagement" attribute="text" /></b></td>
								</tr>
<c:if test="${userPermissions.showUpdateCensusData ==true}">
								<tr valign="top" class="datacell2">
									<td class="datacell2"><strong>Update census data</strong></td>
									<td align="left" nowrap="nowrap">
									<div align="left">
									<% 
										String updateCensusDataStr = "allContractAccesses[" + theIndex +"].userPermissions.updateCensusData";
									%>
<c:if test="${userPermissions.updateCensusData ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=updateCensusDataStr%>">Yes</ps:highlightIfChanged>
</c:if>
									<c:if test="${userPermissions.updateCensusData !=true}">
										<ps:highlightIfChanged name="theForm" property="<%=updateCensusDataStr%>">No</ps:highlightIfChanged>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
<c:if test="${userPermissions.showViewSalary ==true}">
								<tr valign="top">
									<td class="datacell2"><strong>View salary </strong></td>
									<td align="left" nowrap="nowrap" class="datacell2">
									<div align="left">
									<% 
										String viewSalaryStr = "allContractAccesses[" + theIndex +"].userPermissions.viewSalary";
									%>
<c:if test="${userPermissions.viewSalary ==true}">
										<ps:highlightIfChanged name="theForm" property="<%=viewSalaryStr%>">Yes</ps:highlightIfChanged>
</c:if>
									<c:if test="${userPermissions.viewSalary !=true}">
										<ps:highlightIfChanged name="theForm" property="<%=viewSalaryStr%>">No</ps:highlightIfChanged>
</c:if>
									</div>
									</td>
								</tr>
</c:if>
								<% } %>
							</TBODY>
						</TABLE>
						</TD>
						<TD width=1 class=boxborder style7><IMG height=1
							src="/assets/unmanaged/images/s.gif" width=1></TD>
					</TR>
					<TR>
						<TD colSpan=3 class="style7">
						<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
							<TBODY>
								<TR>
									<TD width="1" class=boxborder><IMG height=1	src="/assets/unmanaged/images/s.gif" width=1></TD>
									<TD class=boxborder><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
								</TR>
							</TBODY>
						</TABLE>
						</TD>
					</TR>
				</TBODY>
			</TABLE>
			<% } %>
			<!-- client service end -->
			<BR>
			</td>
	</tr>
</table>
</DIV>
