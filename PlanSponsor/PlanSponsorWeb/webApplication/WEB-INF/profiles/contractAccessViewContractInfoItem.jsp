<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%-- Imports --%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%> 
<%@page import="com.manulife.pension.service.security.role.Trustee"%>
<%@page
	import="com.manulife.pension.service.security.role.AuthorizedSignor"%>
<%@page
	import="com.manulife.pension.service.security.role.AdministrativeContact"%>
<%@page
	import="com.manulife.pension.service.security.role.IntermediaryContact"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@ page import="com.manulife.pension.ps.web.Constants" %>

<c:set var="colspan_display" value="9" />
<tr>
	<td width="1" class="databorder"><img
		src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">

			<tr class="datacell1">
				<td colspan="${colspan_display}">
					<table width="100%" border="0">
						<tr>

							<td width="20" valign="top"><strong>Legend:</strong></td>
							<td width="30" valign="top"><img
								src="/assets/unmanaged/images/view_icon.gif"> View
								Contract Permissions</td>
							<td width="30" valign="top"><img
								src="/assets/unmanaged/images/edit_icon.gif"> Edit
								Contract Permissions</td>

						</tr>
					</table>
				</td>
			</tr>

			<tr class="datacell1">
				<td height="25" colspan="${colspan_display}" class="tablesubhead">
					<b>Contracts</b>
				</td>
			</tr>

			<tr class="tablesubhead" height="20">
				<td width="1" class="databorder"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
				<td width="35" class="pgNumBack"><B>Action</B></td>
				<td width="1" class="greyborder"><img
					src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
					width="1"></td>
				<td width="80" class="pgNumBack"><b> Contract No </b></td>
				<td class="dataheaddivider"><img
					src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
					width="1"></td>
				<td width="142" class="pgNumBack"><b> Contract Name </b></td>
				<td class="dataheaddivider"><img
					src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
					width="1"></td>
				<td width="90" class="pgNumBack"><B>Role </B></td>
				<td width="1" class="databorder"><img
					src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
			</tr>

<c:forEach items="${theForm.contractAccesses}" var="contractAccesses" varStatus="theIndex" >

 <c:set var="indexValue" value="${theIndex.index}"/> 
 <%
 String temp = pageContext.getAttribute("indexValue").toString();
 %>


				<%
					if (Integer.parseInt(temp) % 2 == 0) {
				%>
				<tr id="contractAccessRow<%=temp%>" class="datacell1"
					height="20">
					<%
						} else {
					%>
				
				<tr id="contractAccessRow<%=temp%>" class="datacell2"
					height="20">
					<%
						}
					%>
					<td width="1" class="databorder"><img
						src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
					<td>
						<table width="40" cellpadding="0" cellspacing="0">
							<tr>
								<td class="candyButtonIconLeftColumn">
									<!-- checking business converted --> <logicext:if
										name="contractAccesses" property="cbcIndicator" op="equal"
										value="true">
										<!-- contract is business converted -->
										<logicext:then>
											<!-- checking web access -->
											<logicext:if name="theForm" property="webAccess" op="equal"
												value="<%=Constants.USER_PROFILE_YES %>">
												<!-- svc74 -->
												<!-- web access is yes -->
												<logicext:then>
													<!-- interal user -->
													<ps:isInternalUser name="userProfile" property="role">
														<ps:permissionAccess permissions="EXMN">
															<a href="javascript://"
onClick="doViewPermissions('${contractAccesses.contractNumber}');">
																<img height=12 alt="View Permissions"
																title="View Permissions"
																src="/assets/unmanaged/images/view_icon.gif" width=12
																border=0>
															</a>
														</ps:permissionAccess>
													</ps:isInternalUser>
												</logicext:then>
											</logicext:if>
										</logicext:then>
										<!-- contract is not business converted -->
										<logicext:else>
											<!-- DFS11 svc 68 -->
											<logicext:if name="theForm" property="webAccess" op="equal"
												value="<%=Constants.USER_PROFILE_YES %>">
												<logicext:then>
													<a href="javascript://"
onClick="doViewPermissions('${contractAccesses.contractNumber}');">
														<img height=12 alt="View Permissions"
														title="View Permissions"
														src="/assets/unmanaged/images/view_icon.gif" width=12
														border=0>
													</a>
												</logicext:then>
											</logicext:if>
										</logicext:else>
									</logicext:if>

								</td>
								
								<td class="candyButtonIcon">
								
<c:set var="theProfileId" value="${clientAddEditUserForm.profileId}" />  

								<a href="javascript://"
onClick="editProfile('${theProfileId}', '${contractAccesses.contractNumber}');">

										<img src="/assets/unmanaged/images/edit_icon.gif"
										alt="Edit Permissions" title="Edit Permissions" width=12 height=12
										border="0">
								</a></td>
							</tr>
						</table>
					</td>

					<td width="1" class="databorder"><img
						src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
<input type="hidden" name="contractNumber" /><%--  input - indexed="true" name="contractAccesses" --%>

<input type="hidden" name="contractName" /><%--  input - indexed="true" name="contractAccesses" --%>

<input type="hidden" name="lastUserWithManageUsers" /><%--  input - indexed="true" name="contractAccesses" --%>

<input type="hidden" name="lastUserWithSigningAuthority" /><%--  input - indexed="true" name="contractAccesses" --%>

<input type="hidden" name="lastUserWithReviewIWithdrawals" /><%--  input - indexed="true" name="contractAccesses" --%>

<input type="hidden" name="lastUserWithSubmissionsAccess" /><%--  input - indexed="true" name="contractAccesses" --%>

<input type="hidden" name="lastUserWithReviewLoansPermission" /><%--  input - indexed="true" name="contractAccesses" --%>

<input type="hidden" name="lastClientUserWithReviewIWithdrawals" /><%--  input - indexed="true" name="contractAccesses" --%>

<input type="hidden" name="lastClientUserWithReviewLoans" /><%--  input - indexed="true" name="contractAccesses" --%>

<input type="hidden" name="trusteeMailRecipientProfileId" /><%--  input - indexed="true" name="contractAccesses" --%>


<td><strong class="highlight"> ${contractAccesses.contractNumber}</strong></td>

					<td width="1" class="databorder"><img
						src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
<td><strong class="highlight"> ${contractAccesses.contractName}

					</strong></td>
					<td width="1" class="databorder"><img
						src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
<td>${contractAccesses.planSponsorSiteRole.label}</td>

					<td width="1" class="databorder"><img
						src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
				</tr>
</c:forEach>

		</table>
	</td>
	<td width="1" class="databorder"><img
		src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
</tr>
