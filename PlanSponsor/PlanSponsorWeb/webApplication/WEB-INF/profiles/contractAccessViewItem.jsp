<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@page import="com.manulife.pension.service.security.role.Trustee"%>
<%@page import="com.manulife.pension.service.security.role.AuthorizedSignor"%>
<%@page import="com.manulife.pension.service.security.role.AdministrativeContact"%>
<%@page import="com.manulife.pension.service.security.role.IntermediaryContact"%>
<%@page import="com.manulife.pension.ps.web.profiles.ClientUserContractAccess" %>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%>
<c:forEach items="${theForm.contractAccesses}" var="contractAccesses" varStatus="theIndex" >
<c:set var="indexValue" value="${theIndex.index}"/> 
	<%
	String temp = pageContext.getAttribute("indexValue").toString();
	java.lang.String buttonClass = "";
	if (Integer.parseInt(temp) % 2 == 0) {
		buttonClass = "button134Cell2";
	%>
	<tr id="contractAccessRow<%=temp%>" class="datacell2">
	<%
	} else {
		buttonClass = "button134Cell1";
	%>
	<tr id="contractAccessRow<%=temp%>" class="datacell1">
	<%
	}
	%>
		<td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"	width="1"></td>
		<td align="center">
<form:hidden path="theForm.contractAccesses[${theIndex.index}].contractNumber"/>		
<form:hidden path="theForm.contractAccesses[${theIndex.index}].contractName" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="theForm.contractAccesses[${theIndex.index}].lastUserWithManageUsers" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="theForm.contractAccesses[${theIndex.index}].lastUserWithSigningAuthority" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="theForm.contractAccesses[${theIndex.index}].lastUserWithReviewIWithdrawals" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="theForm.contractAccesses[${theIndex.index}].lastUserWithSubmissionsAccess" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="theForm.contractAccesses[${theIndex.index}].lastUserWithReviewLoansPermission" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="theForm.contractAccesses[${theIndex.index}].lastClientUserWithReviewIWithdrawals" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="theForm.contractAccesses[${theIndex.index}].lastClientUserWithReviewLoans" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="theForm.contractAccesses[${theIndex.index}].trusteeMailRecipientProfileId" /><%--  input - indexed="true" name="contractAccesses" --%>
		

		<table border="0" cellpadding="3" cellspacing="0" width="450">
			<tbody>
				<tr valign="top">
					<td width="200"><strong>Contract number </strong></td>
<td><strong class="highlight"> ${contractAccesses.contractNumber}</strong></td>
				</tr>
			</tbody>
		</table>
		<table border="0" cellpadding="3" cellspacing="0" width="448">
			<tbody>
				<tr valign="top">
					<td width="200" ><strong>Contract name </strong></td>
<td><strong class="highlight"> ${contractAccesses.contractName} </strong></td>
				</tr>
				<tr valign="top">
					<td width="200"><strong><span>Role</span></strong></td>
<td>${contractAccesses.planSponsorSiteRole.label}</td>
				</tr>
				<!-- checking contract is business converted -->
				<logicext:if name="contractAccesses" property="cbcIndicator" op="equal" value="true">
					<!-- contract is business converted -->
					<logicext:then>
						<!-- DFS11 svc88,svc69,svc70,svc72,svc73 -->
						<logicext:if name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=IntermediaryContact.ID %>">
						<logicext:then>
							<tr valign="top">
								<td width="200"><strong><span>Contact Type</span></strong></td>
<td>${contractAccesses.roleTypeDisplayName}</td>
							</tr>
						</logicext:then>
						</logicext:if>
						<logicext:if name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=Trustee.ID %>">
						<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AuthorizedSignor.ID %>" />
						<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AdministrativeContact.stringID %>" />
							<logicext:then>
								<tr valign="top">
									<td width="200"><strong>Primary contact </strong></td>
									<td align="left" nowrap="nowrap">
									<logicext:if name="contractAccesses" property="primaryContact" op="equal" value="true">
										<logicext:then>Yes</logicext:then>
										<logicext:else>No</logicext:else>
									</logicext:if>
									</td>
									</tr>
								<tr valign="top">
									<td width="200"><strong>Client mail recipient </strong></td>
									<td align="left" nowrap="nowrap">
									<logicext:if name="contractAccesses" property="mailRecepient" op="equal" value="true">
										<logicext:then>Yes</logicext:then>
										<logicext:else>No</logicext:else>
									</logicext:if>
									</td>
								</tr>
								<tr valign="top">
									<td width="200"><strong>Trustee mail recipient </strong></td>
									<td align="left" nowrap="nowrap">
									<logicext:if name="contractAccesses" property="trusteeMailRecepient" op="equal" value="true">
										<logicext:then>Yes</logicext:then>
										<logicext:else>No</logicext:else>
									</logicext:if>
									</td>
								</tr>
							</logicext:then>
						</logicext:if>
						<%-- CSX.26 a --%>
						<ps:isInternalUser name="userProfile" property="role" >
						<logicext:if name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=Trustee.ID %>">
						<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AuthorizedSignor.ID %>" />
						<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AdministrativeContact.stringID %>" />
						<logicext:then>
<c:if test="${contractAccesses.iccDesignateEligible ==true}">
									<tr valign="top">
										<td width="200"><strong>Investment comparative chart Designate</strong></td>
										<td align="left" nowrap="nowrap">
											<logicext:if name="contractAccesses" property="iccDesignate" op="equal" value="true">
												<logicext:then>Yes</logicext:then>
												<logicext:else>No</logicext:else>
											</logicext:if>
										</td>
									</tr>
</c:if>
<c:if test="${contractAccesses.sendServiceDesignateEligible ==true}">
									<tr valign="top">
										<td width="200"><strong>SEND Service Notice Contact</strong></td>
										<td align="left" nowrap="nowrap">
											<logicext:if name="contractAccesses" property="sendServiceDesignate" op="equal" value="true">
												<logicext:then>Yes</logicext:then>
												<logicext:else>No</logicext:else>
											</logicext:if>
										</td>
									</tr>
</c:if>
						</logicext:then>
						</logicext:if>
							<tr valign="top">
								<td width="200"><strong>Participant statement consultant </strong></td>
								<td align="left" nowrap="nowrap">
									<logicext:if name="contractAccesses" property="statementIndicator" op="equal" value="true">
										<logicext:then>Yes</logicext:then>
										<logicext:else>No</logicext:else>
									</logicext:if>
								</td>
							</tr>
						</ps:isInternalUser>
						<%-- CSX.26 b & c --%>
						<ps:isExternal name="userProfile" property="role">
							<logicext:if name="contractAccesses" property="statementIndicator" op="equal" value="true">
							<logicext:then>
							<tr valign="top">
								<td width="200"><strong>Participant statement consultant </strong></td>
								<td align="left" nowrap="nowrap">
									<logicext:if name="contractAccesses" property="statementIndicator" op="equal" value="true">
										<logicext:then>Yes</logicext:then>
										<logicext:else>No</logicext:else>
									</logicext:if>
								</td>
							</tr>
							</logicext:then>
							</logicext:if>
							<logicext:if name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=Trustee.ID %>">
						    <logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AuthorizedSignor.ID %>" />
						    <logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AdministrativeContact.stringID %>" />
							<logicext:then>
							<c:if test="${contractAccesses.iccDesignateEligible ==true}">
																<tr valign="top">
																	<td width="200"><strong>Investment comparative chart Designate</strong></td>
																	<td align="left" nowrap="nowrap">
																		<logicext:if name="contractAccesses" property="iccDesignate" op="equal" value="true">
																			<logicext:then>Yes</logicext:then>
																			<logicext:else>No</logicext:else>
																		</logicext:if>
																	</td>
																</tr>
							</c:if>
<c:if test="${contractAccesses.sendServiceDesignateEligible ==true}">
									<tr valign="top">
										<td width="200"><strong>SEND Service Notice Contact</strong></td>
										<td align="left" nowrap="nowrap">
											<logicext:if name="contractAccesses" property="sendServiceDesignate" op="equal" value="true">
												<logicext:then>Yes</logicext:then>
												<logicext:else>No</logicext:else>
											</logicext:if>
										</td>
									</tr>
</c:if>
							</logicext:then>
							</logicext:if>
						</ps:isExternal>
						
						<%-- CCX.27 --%>
						<ps:isInternalUser name="userProfile" property="role">
							<logicext:if name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=Trustee.ID %>">
							<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AuthorizedSignor.ID %>" />
							<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AdministrativeContact.stringID %>" />
								<logicext:then>
									<tr valign="top">
										<td width="200"><strong>Signature received - trustee </strong></td>
										<td align="left" nowrap="nowrap">
											<logicext:if name="contractAccesses" property="signatureReceivedTrustee" op="equal" value="true">
												<logicext:then>Yes</logicext:then>
												<logicext:else>No</logicext:else>
											</logicext:if>
										</td>
									</tr>
									<tr valign="top">
										<td width="200"><strong>Signature received - authorized signer </strong></td>
										<td align="left" nowrap="nowrap">
											<logicext:if name="contractAccesses" property="signatureReceivedAuthSigner" op="equal" value="true">
												<logicext:then>Yes</logicext:then>
												<logicext:else>No</logicext:else>
											</logicext:if>
										</td>
									</tr>
								</logicext:then>
							</logicext:if>
						</ps:isInternalUser>
						<ps:isExternal name="userProfile" property="role">
								<logicext:if name="contractAccesses" property="signatureReceivedTrustee" op="equal" value="true">
								<logicext:then>
									<tr valign="top">
										<td width="200"><strong>Signature received - trustee </strong></td>
										<td align="left" nowrap="nowrap">
											Yes
										</td>
									</tr>
								</logicext:then>
								</logicext:if>
								<logicext:if name="contractAccesses" property="signatureReceivedAuthSigner" op="equal" value="true">
								<logicext:then>
									<tr valign="top">
										<td width="200"><strong>Signature received - authorized signer </strong></td>
										<td align="left" nowrap="nowrap">
											Yes
										</td>
									</tr>
								</logicext:then>
								</logicext:if>
						</ps:isExternal>
						
					</logicext:then>
					<!-- contract is not business converted -->
					<logicext:else>
						<tr valign="top">
							<td width="200"><strong>Primary contact </strong></td>
							<td align="left" nowrap="nowrap">
								<logicext:if name="contractAccesses" property="primaryContact" op="equal" value="true">
									<logicext:then>Yes</logicext:then>
									<logicext:else>No</logicext:else>
								</logicext:if>
							</td>
						</tr>
						
						<tr valign="top">
							<td width="200"><strong>Client mail recipient </strong></td>
							<td align="left" nowrap="nowrap">
								<logicext:if name="contractAccesses" property="mailRecepient" op="equal" value="true">
									<logicext:then>Yes</logicext:then>
									<logicext:else>No</logicext:else>
								</logicext:if>
							</td>
						</tr>
						
						<tr valign="top">
							<td width="200"><strong>Trustee mail recipient </strong></td>
							<td align="left" nowrap="nowrap">
								<logicext:if name="contractAccesses" property="trusteeMailRecepient" op="equal" value="true">
									<logicext:then>Yes</logicext:then>
									<logicext:else>No</logicext:else>
								</logicext:if>
							</td>
						</tr>
						
						<ps:isInternalUser name="userProfile" property="role"> 
<c:if test="${contractAccesses.iccDesignateEligible ==true}">
							<tr valign="top">
								<td width="200"><strong>Investment comparative chart Designate</strong></td>
								<td align="left" nowrap="nowrap">
									<logicext:if name="contractAccesses" property="iccDesignate" op="equal" value="true">
										<logicext:then>Yes</logicext:then>
										<logicext:else>No</logicext:else>
									</logicext:if>
								</td>
							</tr>
</c:if>
<c:if test="${contractAccesses.sendServiceDesignateEligible ==true}">
							<tr valign="top">
								<td width="200"><strong>SEND Service Notice Contact</strong></td>
								<td align="left" nowrap="nowrap">
									<logicext:if name="contractAccesses" property="sendServiceDesignate" op="equal" value="true">
										<logicext:then>Yes</logicext:then>
										<logicext:else>No</logicext:else>
									</logicext:if>
								</td>
							</tr>
</c:if>
							<tr valign="top">
								<td width="200"><strong>Participant statement consultant </strong></td>
								<td align="left" nowrap="nowrap">
									<logicext:if name="contractAccesses" property="statementIndicator" op="equal" value="true">
										<logicext:then>Yes</logicext:then>
										<logicext:else>No</logicext:else>
									</logicext:if>
								</td>
							</tr>
						</ps:isInternalUser>
						<%-- CSX.26 b & c --%>
						<ps:isExternal name="userProfile" property="role"> 
<c:if test="${contractAccesses.iccDesignateEligible ==true}">
							<tr valign="top">
								<td width="200"><strong>Investment comparative chart Designate</strong></td>
								<td align="left" nowrap="nowrap">
									<logicext:if name="contractAccesses" property="iccDesignate" op="equal" value="true">
										<logicext:then>Yes</logicext:then>
										<logicext:else>No</logicext:else>
									</logicext:if>
								</td>
							</tr>
</c:if>
<c:if test="${contractAccesses.sendServiceDesignateEligible ==true}">
							<tr valign="top">
								<td width="200"><strong>SEND Service Notice Contact</strong></td>
								<td align="left" nowrap="nowrap">
									<logicext:if name="contractAccesses" property="sendServiceDesignate" op="equal" value="true">
										<logicext:then>Yes</logicext:then>
										<logicext:else>No</logicext:else>
									</logicext:if>
								</td>
							</tr>
</c:if>
							<logicext:if name="contractAccesses" property="statementIndicator" op="equal" value="true">
							<logicext:then>
							<tr valign="top">
								<td width="200"><strong>Participant statement consultant </strong></td>
								<td align="left" nowrap="nowrap">
									<logicext:if name="contractAccesses" property="statementIndicator" op="equal" value="true">
										<logicext:then>Yes</logicext:then>
										<logicext:else>No</logicext:else>
									</logicext:if>
								</td>
							</tr>
							</logicext:then>
							</logicext:if>
						</ps:isExternal>
						
						<tr valign="top">
							<td width="200"><strong>Signature received - trustee </strong></td>
							<td align="left" nowrap="nowrap">
								<logicext:if name="contractAccesses" property="signatureReceivedTrustee" op="equal" value="true">
									<logicext:then>Yes</logicext:then>
									<logicext:else>No</logicext:else>
								</logicext:if>
							</td>
						</tr>
						
						<tr valign="top">
							<td width="200"><strong>Signature received - authorized signer </strong></td>
							<td align="left" nowrap="nowrap">
								<logicext:if name="contractAccesses" property="signatureReceivedAuthSigner" op="equal" value="true">
									<logicext:then>Yes</logicext:then>
									<logicext:else>No</logicext:else>
								</logicext:if>
							</td>
						</tr>
						
						<!-- svc65,svc66 -->
						<ps:isInternalUser name="userProfile" property="role">
						<ps:permissionAccess permissions="EXMN">
							<tr valign="top">
								<td width="200"><strong>Manage Users</strong></td>
								<td  nowrap="nowrap">
								<logicext:if name="contractAccesses" property="manageUsers" op="equal" value="true">
									<logicext:then>Yes</logicext:then>
									<logicext:else>No</logicext:else>
								</logicext:if>
								</td>
							</tr>
						</ps:permissionAccess>
						</ps:isInternalUser>
						<!-- svc 67 -->
						<tr valign="top">
							<td width="200"><strong>Selected Access</strong></td>
							<td align="left" nowrap="nowrap">
							<logicext:if name="contractAccesses" property="selectedAccess" op="equal" value="true">
								<logicext:then>Yes</logicext:then>
								<logicext:else>No</logicext:else>
							</logicext:if>
							</td>
						</tr>
					</logicext:else>
				</logicext:if>
			</tbody>
		</table>
		<table border="0" cellpadding="3" cellspacing="0" width="448">
			<tbody>
				<tr valign="top">
					<td colspan="2">
					<table width="100%" border="0">
						<tr>
							<td width="160" valign="middle"><img src="/assets/unmanaged/images/s.gif" height="40" width="1"></td>
							<td width="135" valign="middle"></td>
							<td width="135" valign="middle">
							<!-- checking business converted -->
							<logicext:if name="contractAccesses" property="cbcIndicator" op="equal" value="true">
								<!-- contract is business converted -->
								<logicext:then>
									<!-- checking web access -->
									<logicext:if name="theForm" property="webAccess" op="equal" value="<%=Constants.USER_PROFILE_YES %>">
										<!-- svc74 -->
										<!-- web access is yes -->
										<logicext:then>
											<!-- interal user -->
											<ps:isInternalUser name="userProfile" property="role">
											<ps:permissionAccess permissions="EXMN">
<input name="action"  type="submit" value="view permissions" onClick="return doViewPermissions('${contractAccesses.contractNumber}');" class="<%=buttonClass%>">
											</ps:permissionAccess>
											</ps:isInternalUser>
										</logicext:then>
									</logicext:if>
								</logicext:then>
								<!-- contract is not business converted -->
								<logicext:else>
									<!-- DFS11 svc 68 -->
									<logicext:if name="theForm" property="webAccess" op="equal" value="<%=Constants.USER_PROFILE_YES %>">
										<logicext:then>
<input name="action"  type="submit" value="view permissions" onClick="return doViewPermissions('${contractAccesses.contractNumber}');" class="<%=buttonClass%>">
										</logicext:then>
									</logicext:if>
								</logicext:else>
							</logicext:if>
							</td>
						</tr>
					</table>
					</td>
				</tr>
			</tbody>
		</table>
		</td>
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"	width="1"></td>

	</tr>
</c:forEach>
