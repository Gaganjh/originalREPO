<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<%-- Imports --%>

<%@ page import="com.manulife.pension.ps.web.Constants"%>

<%@page import="com.manulife.pension.service.security.role.Trustee"%>
<%@page import="com.manulife.pension.service.security.role.AuthorizedSignor"%>
<%@page import="com.manulife.pension.service.security.role.AdministrativeContact"%>
<%@page import="com.manulife.pension.service.security.role.IntermediaryContact"%>

<%@page import="com.manulife.pension.service.security.role.type.Auditor"%>
<%@page import="com.manulife.pension.service.security.role.type.Broker"%>
<%@page import="com.manulife.pension.service.security.role.type.RIA"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@page import="com.manulife.pension.ps.web.profiles.AddEditClientUserForm"%>
<%
	UserProfile userProfile = (UserProfile) session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile", userProfile, PageContext.PAGE_SCOPE);
	String tusteeId = Trustee.ID;
	pageContext.setAttribute("tusteeId", tusteeId, PageContext.PAGE_SCOPE);
	String authorizedSignorId = AuthorizedSignor.ID;
	pageContext.setAttribute("authorizedSignorId", authorizedSignorId, PageContext.PAGE_SCOPE);
	String IntermediaryContactId = IntermediaryContact.ID;
	pageContext.setAttribute("IntermediaryContactId", IntermediaryContactId, PageContext.PAGE_SCOPE);
	String administrativeContactId = AdministrativeContact.stringID;
	pageContext.setAttribute("administrativeContactId", administrativeContactId, PageContext.PAGE_SCOPE);
%>

<c:forEach items="${clientAddEditUserForm.contractAccesses}" var="contractAccesses" varStatus="theIndex" >

 <c:set var="indexValue" value="${theIndex.index}"/> 
 <% 
  String indexVal = pageContext.getAttribute("indexValue").toString();
 
 %>
<c:if test="${not empty contractAccesses.roleList}">
<c:set var="roleList" value="${contractAccesses.roleList}"/>
</c:if>

	<%
	java.lang.String buttonClass = "";
	java.lang.String bigButtonClass = "";
	if (Integer.parseInt(indexVal) % 2 == 0) {
		buttonClass = "button134Cell2";
		bigButtonClass = "button175Cell2";
	%>
	<tr id="contractAccessRow<%=indexVal%>" class="datacell2">
	<%
	} else {
		buttonClass = "button134Cell1";
		bigButtonClass = "button175Cell1";
	%>
	<tr id="contractAccessRow<%=indexVal%>" class="datacell1">
	<%
	}
	%>
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
		<td align="center">

<form:hidden path="contractAccesses[${theIndex.index}].contractNumber"/>
<input type="hidden" id="contractNumber<%=indexVal%>" value="${contractAccesses.contractNumber}">
<form:hidden path="contractAccesses[${theIndex.index}].contractName" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].primaryContactProfileId" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].primaryContactName" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].mailRecipientProfileId" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].mailRecipientName" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].trusteeMailRecipientProfileId" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].trusteeMailRecipientName" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].participantStatementConsultantProfileId" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].participantStatementConsultantName" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].iccDesignateProfileId" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].iccDesignateName" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].sendServiceDesignateProfileId" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].sendServiceDesignateName" /><%--  input - indexed="true" name="contractAccesses" --%>

<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.editContractServiceFeatures" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.editContractServiceFeaturesDefault" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.downloadReports" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.downloadReportsDefault" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.employerStatements" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.employerStatementsDefault" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.viewAllUsersSubmissions" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.viewAllUsersSubmissionsDefault" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.updateCensusData" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.updateCensusDataDefault" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.viewSalary" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.viewSalaryDefault" /><%--  input - indexed="true" name="contractAccesses" --%>

<form:hidden path="contractAccesses[${theIndex.index}].lastUserWithManageUsers" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].lastUserWithSigningAuthority" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].lastUserWithReviewIWithdrawals" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].lastUserWithSubmissionsAccess" /><%--  input - indexed="true" name="contractAccesses" --%>

		<logicext:if name="contractAccesses" property="cbcIndicator" op="equal" value="true">
		<logicext:or name="userProfile" property="internalUser" op="equal" value="false" />
		<logicext:then>
<form:hidden path="contractAccesses[${theIndex.index}].manageUsers" /><%--  input - indexed="true" name="contractAccesses" --%>
		</logicext:then>
		</logicext:if>

		<logicext:if name="contractAccesses" property="cbcIndicator" op="equal" value="true">
		<logicext:then>
<form:hidden path="contractAccesses[${theIndex.index}].selectedAccess" /><%--  input - indexed="true" name="contractAccesses" --%>
		</logicext:then>
		</logicext:if>

<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.reviewWithdrawals" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.signingAuthority" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.viewSubmissions" /><%--  input - indexed="true" name="contractAccesses" --%>

<form:hidden path="contractAccesses[${theIndex.index}].planSponsorSiteRole.label" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.initiateLoans" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.viewAllLoans" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].userPermissions.reviewLoans" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].lastUserWithReviewLoansPermission" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].lastClientUserWithReviewLoans" /><%--  input - indexed="true" name="contractAccesses" --%>
<form:hidden path="contractAccesses[${theIndex.index}].lastClientUserWithReviewIWithdrawals" /><%--  input - indexed="true" name="contractAccesses" --%>
		

		<table border="0" cellpadding="3" cellspacing="0" width="450">
			<tbody>
				<tr valign="top">
					<td width="200"><strong>Contract number </strong></td>
<td><strong class="highlight">${contractAccesses.contractNumber}</strong></td>
				</tr>
			</tbody>
		</table>

		<table border="0" cellpadding="3" cellspacing="0" width="450">
			<tbody>
				<tr valign="top">
					<td width="200" height="21"><strong>Contract name </strong></td>
<td colspan="3"><span class="highlight"><b> ${e:forHtmlContent(contractAccesses.contractName)} </b></span></td>
				</tr>
				<!-- role field -->
				<tr valign="top">
<c:if test="${empty contractAccesses.planSponsorSiteRole.value}">
						<td width="200" height="29"><ps:label indexPrefix="contractAccesses" fieldId="planSponsorSiteRole" mandatory="true">Role</ps:label></strong></td>
</c:if>
<c:if test="${not empty contractAccesses.planSponsorSiteRole.value}">
						<td width="200" height="29"><strong><span>Role</span></strong></td>
</c:if>
					<td colspan="3">
					<!-- check contract is business converted ? -->
					<logicext:if name="contractAccesses" property="cbcIndicator" op="equal" value="false">
						<!-- contract is not a business converted contract SCC13 -->
						<logicext:then>
${contractAccesses.planSponsorSiteRole.label}
						</logicext:then>
						<!-- contract is a business converted contract SCC21 -->
						<logicext:else>
								<ps:isExternal name="userProfile" property="role">
								<ps:permissionAccess permissions="EXMN">
								<!-- external user -->
<form:select path="contractAccesses[${theIndex.index}].planSponsorSiteRole.value" onchange="changeRole(this);" onfocus="storeCurrentRole(this)" multiple="false">
<c:if test="${empty contractAccesses.planSponsorSiteRole.value}">
											<form:option value="">Select one</form:option>
</c:if>
	 
						<form:options items="${roleList}" itemValue="value" itemLabel="label"/>
							
</form:select>
									<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="planSponsorSiteRole.value" escape="true" encode="true" addHiddenField="true"/>
								</ps:permissionAccess>
								</ps:isExternal>
								<!-- internal user -->
								<ps:isInternalUser name="userProfile" property="role">
								<ps:permissionAccess permissions="EXMN">
									<logicext:if name="clientAddEditUserForm" property="fieldsEnableForInternalUser" op="equal" value="false">
									<!-- internal user without manage external users Trustee & Authorized signor permission -->
										<logicext:then>
										<logicext:if name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=Trustee.ID%>">
										<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AuthorizedSignor.ID%>" />
										<logicext:then>
<form:select path="contractAccesses[${theIndex.index}].planSponsorSiteRole.value" disabled="true" onchange="changeRole(this);" onfocus="storeCurrentRole(this)" multiple="false"><%-- indexed="true"  --%>
<c:if test="${empty contractAccesses.planSponsorSiteRole.value}">
													<form:option value="">Select one</form:option>
</c:if>

						<form:options items="${roleList}" itemValue="value" itemLabel="label"/>												
</form:select>
											<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="planSponsorSiteRole.value" />
										</logicext:then>
										<logicext:else>
<form:select path="contractAccesses[${theIndex.index}].planSponsorSiteRole.value" onchange="changeRole(this);" onfocus="storeCurrentRole(this)" ><%--  indexed="true"  --%>
<c:if test="${empty contractAccesses.planSponsorSiteRole.value}">
													<form:option value="">Select one</form:option>
</c:if>
												
						<form:options items="${roleList}" itemValue="value" itemLabel="label"/>
</form:select>
											<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="planSponsorSiteRole.value" />
										</logicext:else>
									</logicext:if>
								</logicext:then>
								<logicext:else>
<form:select path="contractAccesses[${theIndex.index}].planSponsorSiteRole.value" onchange="changeRole(this);" onfocus="storeCurrentRole(this)" ><%--  indexed="true"  --%>
<c:if test="${empty contractAccesses.planSponsorSiteRole.value}">
													<form:option value="">Select one</form:option>
</c:if>
						<form:options items="${roleList}" itemValue="value" itemLabel="label"/></form:select>
											<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="planSponsorSiteRole.value" />
								</logicext:else>
								</logicext:if>
								</ps:permissionAccess>
								</ps:isInternalUser>
						</logicext:else>
					</logicext:if>
					</td>
				</tr>
<c:if test="${contractAccesses.planSponsorSiteRole.value == IntermediaryContactId}">
				<tr valign="top">
					<logicext:if name="contractAccesses" property="roleType" op="equal" value="">
					<logicext:and name="userProfile" property="internalUser" op="equal" value="true" />
					<logicext:then>
						<td width="200" height="29"><ps:label indexPrefix="contractAccesses" fieldId="roleType" mandatory="true">Contact type</ps:label></strong></td>
					</logicext:then>
					<logicext:else>
						<td width="200" height="29"><strong><span>Contact type</span></strong></td>
					</logicext:else>
					</logicext:if>
					<td colspan="3">
					<logicext:if name="userProfile" property="internalUser" op="equal" value="true">
					<logicext:then>
<form:select path="contractAccesses[${theIndex.index}].roleType" ><%--  - indexed="true"  --%>
							 <c:if test="${empty contractAccesses.roleType}">
								<form:option value="">Select one</form:option>
</c:if>
							<form:option value="AUD">Auditor</form:option>
							<form:option value="BRK">Broker</form:option>
							<form:option value="RIA">RIA</form:option>
</form:select>
						<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="roleType" />
					</logicext:then>
					<logicext:else>
						<logicext:if name="contractAccesses" property="roleType" op="equal" value="">
						<logicext:then>
							<%=Auditor.displayName%>
<form:hidden path="contractAccesses[${theIndex.index}].roleType" value="<%=Auditor.id%>"/> <%-- indexed="true" name="contractAccesses" --%>
						</logicext:then>
						<logicext:else>
${contractAccesses.roleTypeDisplayName}
						</logicext:else>
						</logicext:if>
					</logicext:else>
					</logicext:if>
					</td>
				</tr>
</c:if>
				

				

				<%-- Special attributes fields for business converted contract --%>
				<logicext:if name="contractAccesses" property="cbcIndicator" op="equal" value="true">
					<logicext:then>


					<ps:isExternal name="userProfile" property="role">

						<%-- trustee, authorized signor, administrative contact SCC27 show primary contact & mail recipient or primar contact and mail recipient are already selected --%>
						<c:if test="${contractAccesses.primaryContact or contractAccesses.planSponsorSiteRole.value eq tusteeId or
									contractAccesses.planSponsorSiteRole.value eq authorizedSignorId or contractAccesses.planSponsorSiteRole.value eq administrativeContactId}">
							<tr valign="top">
								<td width="200"><strong>Primary contact</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].primaryContact" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].primaryContact" value="false" />no</td>
									<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
									<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="primaryContact" />
							</tr>
						</c:if>
						<%-- To show Client mail recipient --%>
						<c:if test="${contractAccesses.mailRecepient or contractAccesses.planSponsorSiteRole.value eq tusteeId or
								contractAccesses.planSponsorSiteRole.value eq authorizedSignorId or contractAccesses.planSponsorSiteRole.value eq administrativeContactId}">
							<tr valign="top">
								<td width="200"><strong>Client mail recipient</strong></td>
<td align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].mailRecepient" value="true" />yes</td>
<td align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].mailRecepient" value="false" />no</td>
								<td align="left" nowrap="nowrap">&nbsp;</td>
								<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="mailRecepient" />
							</tr>
						</c:if>
						<%-- To show Trustee mail recipient --%>
						<c:if test="${contractAccesses.trusteeMailRecepient or contractAccesses.planSponsorSiteRole.value eq tusteeId}">
							<tr valign="top">
								<td width="200"><strong>Trustee mail recipient</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].trusteeMailRecepient" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].trusteeMailRecepient" value="false" />no</td>
								<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
								<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="trusteeMailRecepient" />
							</tr>
						</c:if>
						<%-- To show ICC Designate --%>
<c:if test="${contractAccesses.iccDesignateEligible ==true}">
						<c:if test="${contractAccesses.planSponsorSiteRole.value eq tusteeId or
								contractAccesses.planSponsorSiteRole.value eq authorizedSignorId or contractAccesses.planSponsorSiteRole.value eq administrativeContactId}">
							<tr valign="top">
								<td width="200"><strong>Investment Comparative Chart Designate</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].iccDesignate" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].iccDesignate" value="false" />no</td>
								<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
								<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="iccDesignate" />								
							</tr>
						</c:if>
</c:if>
					  <%-- To show SEND Service Designate --%>
<c:if test="${contractAccesses.sendServiceDesignateEligible ==true}">
						<c:if test="${contractAccesses.planSponsorSiteRole.value eq tusteeId or
								contractAccesses.planSponsorSiteRole.value eq authorizedSignorId or contractAccesses.planSponsorSiteRole.value eq administrativeContactId}">
							<tr valign="top">
								<td width="200"><strong>SEND Service Notice Contact</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].sendServiceDesignate" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].sendServiceDesignate" value="false" />no</td>
								<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
								<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="sendServiceDesignate" />								
							</tr>
						</c:if>
</c:if>
						<%-- To show Participant statement consultant --%>
						<c:if test="${userProfile.role.internalUser or contractAccesses.statementIndicator}">
							<tr valign="top">
								<td width="200"><strong>Participant statement consultant</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].statementIndicator" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].statementIndicator" value="false" />no</td>
								<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
								<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="statementIndicator" />								
							</tr>
						</c:if>
						<%-- To show Signature received - trustee --%>		
						<c:if test="${userProfile.role.internalUser and (contractAccesses.planSponsorSiteRole.value eq tusteeId or contractAccesses.signatureReceivedTrustee)}">
							<ps:permissionAccess permissions="EXMN">
								<tr valign="top">
									<td width="200"><strong>Signature received - trustee</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].signatureReceivedTrustee" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].signatureReceivedTrustee" value="false" />no</td>
									<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
									<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="signatureReceivedTrustee" />									
								</tr>
							</ps:permissionAccess>
						</c:if> 
						<%-- To show Signature received - authorized signer --%>		
						<c:if test="${userProfile.role.internalUser and (contractAccesses.planSponsorSiteRole.value eq authorizedSignorId or contractAccesses.signatureReceivedAuthSigner)}">
							<tr valign="top">
								<td width="200"><strong>Signature received - authorized signer</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].signatureReceivedAuthSigner" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].signatureReceivedAuthSigner" value="false" />no</td>
								<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
								<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="signatureReceivedAuthSigner" />
							</tr>
						</c:if>

					</ps:isExternal>

					<ps:isInternalUser name="userProfile" property="role">
						<%-- Disable the signature received trustee and Auth signner for internal users who does not have EXTA permissions --%>
						
						
						<%-- trustee, authorized signor, administrative contact SCC27 show primary contact & mail recipient or primar contact and mail recipient are already selected --%>
						<c:if test="${contractAccesses.primaryContact or contractAccesses.planSponsorSiteRole.value eq tusteeId or
									contractAccesses.planSponsorSiteRole.value eq authorizedSignorId or contractAccesses.planSponsorSiteRole.value eq administrativeContactId}">
							<c:set var="disableOption" value="true"/>
							<c:if test="${contractAccesses.planSponsorSiteRole.value ne tusteeId and
									contractAccesses.planSponsorSiteRole.value ne authorizedSignorId }">
								
									<c:set var="disableOption" value="false"/>
							</c:if>
							<c:if test="${contractAccesses.planSponsorSiteRole.value eq tusteeId or
									contractAccesses.planSponsorSiteRole.value eq authorizedSignorId }">
								<ps:permissionAccess permissions="EXTA">
									<c:set var="disableOption" value="false"/>
								</ps:permissionAccess>
							</c:if>
							
							<tr valign="top">
								<td width="200"><strong>Primary contact</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].primaryContact" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].primaryContact" value="false" />no</td>
									<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
									<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="primaryContact" />
							</tr>
							
						</c:if>
						<%-- To show Client mail recipient --%>
						<c:if test="${contractAccesses.mailRecepient or contractAccesses.planSponsorSiteRole.value eq tusteeId or
								contractAccesses.planSponsorSiteRole.value eq authorizedSignorId or contractAccesses.planSponsorSiteRole.value eq administrativeContactId}">
							<c:set var="disableOption" value="true"/>
							<c:if test="${contractAccesses.planSponsorSiteRole.value ne tusteeId and
									contractAccesses.planSponsorSiteRole.value ne authorizedSignorId}">
									
									<c:set var="disableOption" value="false"/>
							</c:if>
							<c:if test="${contractAccesses.planSponsorSiteRole.value eq tusteeId or
									contractAccesses.planSponsorSiteRole.value eq authorizedSignorId }">
								<ps:permissionAccess permissions="EXTA">
									<c:set var="disableOption" value="false"/>
								</ps:permissionAccess>
							</c:if>
							<tr valign="top">
								<td width="200"><strong>Client mail recipient</strong></td>
<td align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].mailRecepient" value="true" />yes</td>
<td align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].mailRecepient" value="false" />no</td>
								<td align="left" nowrap="nowrap">&nbsp;</td>
								<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="mailRecepient" />
							</tr>
						</c:if>
						
						<%-- To show Trustee mail recipient --%>
						<c:if test="${contractAccesses.trusteeMailRecepient or contractAccesses.planSponsorSiteRole.value eq tusteeId}">
								<c:set var="disableOption" value="true"/>
								<c:if test="${contractAccesses.planSponsorSiteRole.value ne tusteeId }">
									
									<c:set var="disableOption" value="false"/>
								</c:if>
								<c:if test="${contractAccesses.planSponsorSiteRole.value eq tusteeId }">
									<ps:permissionAccess permissions="EXTA">
										<c:set var="disableOption" value="false"/>
									</ps:permissionAccess>
								</c:if>
							<tr valign="top">
								<td width="200"><strong>Trustee mail recipient</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].trusteeMailRecepient" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].trusteeMailRecepient" value="false" />no</td>
								<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
								<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="trusteeMailRecepient" />
							</tr>
						</c:if>
						<%-- To show ICC Designate --%>
<c:if test="${contractAccesses.iccDesignateEligible ==true}">
                         <c:if test="${contractAccesses.planSponsorSiteRole.value eq tusteeId or
								contractAccesses.planSponsorSiteRole.value eq authorizedSignorId or contractAccesses.planSponsorSiteRole.value eq administrativeContactId}">
							<c:set var="disableOption" value="true"/>
							<c:if test="${contractAccesses.planSponsorSiteRole.value ne tusteeId and
									contractAccesses.planSponsorSiteRole.value ne authorizedSignorId}">
									
									<c:set var="disableOption" value="false"/>
							</c:if>
							<c:if test="${contractAccesses.planSponsorSiteRole.value eq tusteeId or
									contractAccesses.planSponsorSiteRole.value eq authorizedSignorId }">
								<ps:permissionAccess permissions="EXTA">
									<c:set var="disableOption" value="false"/>
								</ps:permissionAccess>
							</c:if>
							<tr valign="top">
								<td width="200"><strong>Investment Comparative Chart Designate</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].iccDesignate" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].iccDesignate" value="false" />no</td>
								<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
								<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="iccDesignate" />								
							</tr>
						</c:if>
</c:if>
						<%-- To show SEND Service Designate --%>
<c:if test="${contractAccesses.sendServiceDesignateEligible ==true}">
                         <c:if test="${contractAccesses.planSponsorSiteRole.value eq tusteeId or
								contractAccesses.planSponsorSiteRole.value eq authorizedSignorId or contractAccesses.planSponsorSiteRole.value eq administrativeContactId}">
							<c:set var="disableOption" value="true"/>
							<c:if test="${contractAccesses.planSponsorSiteRole.value ne tusteeId and
									contractAccesses.planSponsorSiteRole.value ne authorizedSignorId}">
									<c:set var="disableOption" value="false"/>
							</c:if>
							<c:if test="${contractAccesses.planSponsorSiteRole.value eq tusteeId or
									contractAccesses.planSponsorSiteRole.value eq authorizedSignorId }">
								<ps:permissionAccess permissions="EXTA">
									<c:set var="disableOption" value="false"/>
								</ps:permissionAccess>
							</c:if>
							<tr valign="top">
								<td width="200"><strong>SEND Service Notice Contact</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].sendServiceDesignate" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].sendServiceDesignate" value="false" />no</td>
								<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
								<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="sendServiceDesignate" />								
							</tr>
						</c:if>
</c:if>
						<%-- To show Participant statement consultant --%>
						<c:if test="${userProfile.role.internalUser or contractAccesses.statementIndicator}">

								<c:set var="disableOption" value="true"/>
								<c:if test="${contractAccesses.planSponsorSiteRole.value ne tusteeId and
									contractAccesses.planSponsorSiteRole.value ne authorizedSignorId}">
									
									<c:set var="disableOption" value="false"/>
								</c:if>
								<c:if test="${contractAccesses.planSponsorSiteRole.value eq tusteeId or
										contractAccesses.planSponsorSiteRole.value eq authorizedSignorId }">
									<ps:permissionAccess permissions="EXTA">
										<c:set var="disableOption" value="false"/>
									</ps:permissionAccess>
								</c:if>
							<tr valign="top">
								<td width="200"><strong>Participant statement consultant</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].statementIndicator" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].statementIndicator" value="false" />no</td>
								<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
								<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="statementIndicator" />								
							</tr>
						</c:if>
						<%-- To show Signature received - trustee --%>		
						<c:if test="${userProfile.role.internalUser and (contractAccesses.planSponsorSiteRole.value eq tusteeId or contractAccesses.signatureReceivedTrustee)}">
							<c:set var="disableOption" value="true"/>
							<ps:permissionAccess permissions="EXTA">
								<c:set var="disableOption" value="false"/>
							</ps:permissionAccess>
							<ps:permissionAccess permissions="EXMN">
								<tr valign="top">
									<td width="200"><strong>Signature received - trustee</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].signatureReceivedTrustee" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].signatureReceivedTrustee" value="false" />no</td>
									<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
									<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="signatureReceivedTrustee" />									
								</tr>
							</ps:permissionAccess>
						</c:if> 
						<%-- To show Signature received - authorized signer --%>		
						<c:if test="${userProfile.role.internalUser and (contractAccesses.planSponsorSiteRole.value eq authorizedSignorId or contractAccesses.signatureReceivedAuthSigner)}">
							<c:set var="disableOption" value="true"/>
							<ps:permissionAccess permissions="EXTA">
								<c:set var="disableOption" value="false"/>
							</ps:permissionAccess>
						
							<tr valign="top">
								<td width="200"><strong>Signature received - authorized signer</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].signatureReceivedAuthSigner" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].signatureReceivedAuthSigner" value="false" />no</td>
								<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
								<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="signatureReceivedAuthSigner" />
							</tr>
						</c:if>
					</ps:isInternalUser>
					
					<%-- contract is business convert end --%>
					</logicext:then>
					<logicext:else>
					<%-- contract is System converted contract  --%>								
						<tr valign="top">
							<td width="200"><strong>Primary contact</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].primaryContact" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].primaryContact" value="false" />no</td>
							<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
							<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="primaryContact" />
						</tr>
						<tr valign="top">
							<td width="200"><strong>Client mail recipient</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].mailRecepient" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].mailRecepient" value="false" />no</td>
							<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
							<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="mailRecepient" />							
						</tr>
						<tr valign="top">
							<td width="200"><strong>Trustee mail recipient</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].trusteeMailRecepient" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].trusteeMailRecepient" value="false" />no</td>
							<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
							<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="trusteeMailRecepient" />							
						</tr>
<c:if test="${contractAccesses.iccDesignateEligible ==true}">
						<ps:isExternal name="userProfile" property="role">
						<tr valign="top">
								<td width="200"><strong>Investment Comparative Chart Designate</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].iccDesignate" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].iccDesignate" value="false" />no</td>
								<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
								<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="iccDesignate" />								
						</tr>
						</ps:isExternal>
						<ps:isInternalUser name="userProfile" property="role">
						<tr valign="top">
								<td width="200"><strong>Investment Comparative Chart Designate</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].iccDesignate" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].iccDesignate" value="false" />no</td>
								<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
								<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="iccDesignate" />								
						</tr>
						</ps:isInternalUser>
</c:if>
<c:if test="${contractAccesses.sendServiceDesignateEligible ==true}">
							<ps:isExternal name="userProfile" property="role">
							<tr valign="top">
									<td width="200"><strong>SEND Service Notice Contact</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].sendServiceDesignate" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton disabled="true" path="contractAccesses[${theIndex.index}].sendServiceDesignate" value="false" />no</td>
									<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
									<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="sendServiceDesignate" />								
							</tr>
							</ps:isExternal>
						<ps:isInternalUser name="userProfile" property="role">
							<tr valign="top">
									<td width="200"><strong>SEND Service Notice Contact</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].sendServiceDesignate" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].sendServiceDesignate" value="false" />no</td>
									<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
									<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="sendServiceDesignate" />								
							</tr>
						</ps:isInternalUser>
</c:if>
						<%-- Logged in user is Internal display the Participant Statement Consultant --%>
						<ps:isInternalUser name="userProfile" property="role">
							<tr valign="top">
								<td width="200"><strong>Participant statement consultant</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].statementIndicator" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton path="contractAccesses[${theIndex.index}].statementIndicator" value="false" />no</td>
								<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
								<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="statementIndicator" />								
							</tr>
						</ps:isInternalUser>
						
						<%-- Disable the signature received trustee and Auth signner for all external users --%>
						<%-- Disable the signature received trustee and Auth signner for internal users who does not have EXTA permissions --%>
						<c:set var="disableOption" value="true"/>
						<ps:isInternalUser name="userProfile" property="role">
							<ps:permissionAccess permissions="EXTA">
								<c:set var="disableOption" value="false"/>
							</ps:permissionAccess>
						</ps:isInternalUser>
						
						<tr valign="top">
							<td width="200"><strong>Signature received - trustee</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].signatureReceivedTrustee" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].signatureReceivedTrustee" value="false" />no</td>
							<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
							<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="signatureReceivedTrustee" />							
						</tr>
						<tr valign="top">
							<td width="200"><strong>Signature received - authorized signer</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].signatureReceivedAuthSigner" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton disabled="${disableOption}" path="contractAccesses[${theIndex.index}].signatureReceivedAuthSigner" value="false" />no</td>
							<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
							<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="signatureReceivedAuthSigner" />							
						</tr>
						
						<%-- internal user show manage user fields SCC16 & SAC85--%>
						<ps:isInternalUser name="userProfile" property="role">
							<logicext:if name="contractAccesses" property="selectedAccess" op="equal" value="false">
							<logicext:then>
							<tr valign="top" id="manageUserDiv<%=indexVal%>" style="display: block">
							</logicext:then>
							<logicext:else>
							<tr valign="top" id="manageUserDiv<%=indexVal%>" style="display: none">
							</logicext:else>
							</logicext:if>
								<td width="200"><strong>Manage Users</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton onclick="changeManageUsers(this)" path="contractAccesses[${theIndex.index}].manageUsers" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton onclick="changeManageUsers(this)" path="contractAccesses[${theIndex.index}].manageUsers" value="false" />no</td>
								<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
								<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="manageUsers" />
							</tr>
						</ps:isInternalUser>

						<%-- show selected access field --%>
						<tr valign="top">
							<td width="200"><strong>Selected Access</strong></td>
<td width="65" align="left" nowrap="nowrap"><form:radiobutton onclick="changeSelectedAccess(this)" path="contractAccesses[${theIndex.index}].selectedAccess" value="true" />yes</td>
<td width="69" align="left" nowrap="nowrap"><form:radiobutton onclick="changeSelectedAccess(this)" path="contractAccesses[${theIndex.index}].selectedAccess" value="false" />no</td>
							<td width="92" align="left" nowrap="nowrap">&nbsp;</td>
							<ps:trackChanges name="clientAddEditUserForm" indexPrefix="contractAccesses" index="${theIndex.index}" property="selectedAccess" />
						</tr>
						
					</logicext:else>
					<%-- contract is System converted contract end --%>
				</logicext:if>
			</tbody>
		</table>

		<table border="0" cellpadding="3" cellspacing="0" width="450">
			<tbody>
				<tr valign="top">
					<td colspan="4">
					<table width="100%" border="0">
						<tr>
							<td width="160" valign="middle"><img src="/assets/unmanaged/images/s.gif" height="40" width="1"></td>
							<td width="135" valign="middle">
							<!-- delete button always display and enable -->
							<logicext:if name="contractAccesses" property="cbcIndicator" op="equal" value="true">
								<!-- contract is business converted contract -->
								<logicext:then>
										<ps:isExternal name="userProfile" property="role">
										<ps:permissionAccess permissions="EXMN">
										<!-- external user  -->
<input name="deleteButton${contractAccesses.contractNumber}" value="delete" onClick="doDelete('${contractAccesses.contractNumber}');" class="<%=buttonClass%>" type="button">

										</ps:permissionAccess>
										</ps:isExternal>
										<!-- internal user  -->
										<ps:isInternalUser name="userProfile" property="role">
										<ps:permissionAccess permissions="EXMN">
											<logicext:if name="clientAddEditUserForm" property="fieldsEnableForInternalUser" op="equal" value="false">
											<!-- internal user without manage external users Trustee & Authorized signor permission -->
											<logicext:then>
											<logicext:if name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=Trustee.ID%>">
											<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AuthorizedSignor.ID%>" />
											<logicext:then>
<input name="deleteButton${contractAccesses.contractNumber}" value="delete" disabled="disabled" onClick="doDelete('${contractAccesses.contractNumber}');" class="<%=buttonClass%>" type="button">

											</logicext:then>
											<logicext:else>
<input name="deleteButton${contractAccesses.contractNumber}" value="delete" onClick="doDelete('${contractAccesses.contractNumber}');" class="<%=buttonClass%>" type="button">

											</logicext:else>
											</logicext:if>
											</logicext:then>
											<logicext:else>
<input name="deleteButton${contractAccesses.contractNumber}" value="delete" onClick="doDelete('${contractAccesses.contractNumber}');" class="<%=buttonClass%>" type="button">

											</logicext:else>
											</logicext:if>
										</ps:permissionAccess>
										</ps:isInternalUser>
								</logicext:then>
								<!-- contract is not business converted contract -->
								<logicext:else>
<input name="deleteButton${contractAccesses.contractNumber}" value="delete" onClick="doDelete('${contractAccesses.contractNumber}');" class="<%=buttonClass%>" type="button">

								</logicext:else>
							</logicext:if>

							</td>
							<td width="135" valign="middle">
							<logicext:if name="contractAccesses" property="cbcIndicator" op="equal" value="true">
								<!-- contract is business converted contract SCC33-->
								<logicext:then>
								<!-- internal user  -->
								<ps:isInternalUser name="userProfile" property="role">
								<ps:permissionAccess permissions="EXMN">
									<logicext:if name="clientAddEditUserForm" property="webAccess"	op="equal" value="Yes">
										<logicext:then>
											<logicext:if name="contractAccesses" property="newContract" op="equal" value="true">
												<logicext:then>
												<logicext:if name="clientAddEditUserForm" property="fieldsEnableForInternalUser" op="equal" value="false">
												<logicext:then>
												<logicext:if name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=Trustee.ID%>">
												<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AuthorizedSignor.ID%>" />
												<logicext:then>
<input name="changePermissions${contractAccesses.contractNumber}" value="change default permissions" disabled="disabled" onClick="return doChangePermissions('${contractAccesses.contractNumber}');" class="<%=bigButtonClass%>"	type="button">

												</logicext:then>
												<logicext:else>
<input name="changePermissions${contractAccesses.contractNumber}" value="change default permissions" onClick="return doChangePermissions('${contractAccesses.contractNumber}');" class="<%=bigButtonClass%>"	type="button">

												</logicext:else>
												</logicext:if>
												</logicext:then>
												<logicext:else>
<input name="changePermissions${contractAccesses.contractNumber}" value="change default permissions" onClick="return doChangePermissions('${contractAccesses.contractNumber}');" class="<%=bigButtonClass%>"	type="button">

												</logicext:else>
												</logicext:if>
												</logicext:then>
												<logicext:else>
													<logicext:if name="clientAddEditUserForm" property="fieldsEnableForInternalUser" op="equal" value="false">
													<!-- internal user without manage external users Trustee & Authorized signor permission -->
													<logicext:then>
													<logicext:if name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=Trustee.ID%>">
													<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AuthorizedSignor.ID%>" />
													<logicext:then>
<input name="editPermissions${contractAccesses.contractNumber}" value="edit permissions" disabled="disabled" onClick="return doChangePermissions('${contractAccesses.contractNumber}');" class="<%=buttonClass%>"	type="button">

													</logicext:then>
													<logicext:else>
<input name="editPermissions${contractAccesses.contractNumber}" value="edit permissions" onClick="return doChangePermissions('${contractAccesses.contractNumber}');" class="<%=buttonClass%>"	type="button">

													</logicext:else>
													</logicext:if>
													</logicext:then>
													<logicext:else>
<input name="editPermissions${contractAccesses.contractNumber}" value="edit permissions" onClick="return doChangePermissions('${contractAccesses.contractNumber}');" class="<%=buttonClass%>"	type="button">

													</logicext:else>
													</logicext:if>
												</logicext:else>
											</logicext:if>
										</logicext:then>
									</logicext:if>
								</ps:permissionAccess>
								</ps:isInternalUser>
								
								</logicext:then>
								<!--contract is not business converted contract scc19 -->
								<logicext:else>
									<logicext:if name="clientAddEditUserForm" property="webAccess" op="equal" value="Yes">
									<logicext:then>
										<logicext:if name="contractAccesses" property="newContract" op="equal" value="true">
											<logicext:then>
<input name="changePermissions${contractAccesses.contractNumber}" value="change default permissions" onClick="return doChangePermissions('${contractAccesses.contractNumber}');" class="<%=bigButtonClass%>"	type="button">

											</logicext:then>
											<logicext:else>
<input name="editPermissions${contractAccesses.contractNumber}" value="edit permissions" onClick="return doChangePermissions('${contractAccesses.contractNumber}');" class="<%=buttonClass%>"	type="button">

											</logicext:else>
										</logicext:if>
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
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	</tr>
</c:forEach>
