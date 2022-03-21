<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@page import="com.manulife.pension.ps.web.profiles.AddEditClientUserForm"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.manulife.pension.ps.web.profiles.AddEditClientUserForm" %>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<% 
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
	AddEditClientUserForm theForm = (AddEditClientUserForm)session.getAttribute("clientAddEditUserForm");
	pageContext.setAttribute("theForm",theForm,PageContext.REQUEST_SCOPE);
%>

<script type="text/javascript">

var submitted=false;

function setActionAndSubmit(action) {
	if (!submitted) {
		submitted=true;
		document.clientAddEditUserForm.action.value = action;
		document.clientAddEditUserForm.submit();
	} else {
		window.status = "Transaction already in progress.  Please wait.";
	}
}

function doSubmit(href) {
	if (!submitted) {
		submitted = true;
		location.href=href;
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return;
	 }
}

function editProfile(theProfileId) {
	return doSubmit('/do/profiles/editUser/?profileId='+theProfileId);
}

function doViewPermissions(number) { 
	if (!submitted) {
		submitted = true;
		document.clientAddEditUserForm.selectedContractNumber.value = number;
		document.clientAddEditUserForm.action = '/do/profiles/viewUser/?action=viewPermissions';
		//document.clientAddEditUserForm.action.value = 'viewPermissions';
		document.clientAddEditUserForm.submit();
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return;
	 }
}

</script>

<ps:form method="POST" action="/do/profiles/viewUser/" name="clientAddEditUserForm" modelAttribute="clientAddEditUserForm" >
<form:hidden path="selectedContractNumber"/>
<form:hidden path="action" value=""/>
</ps:form>

<table border="0" cellpadding="0" cellspacing="0" width="700">
	<tbody>
		<tr>
			<td width="525"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
		</tr>
		<tr>
			<td><p><div id="errordivcs"><content:errors scope="session" /></div></p></td>
		</tr>
		<tr>
			<td>
			<table border="0" cellpadding="0" cellspacing="0" width="400">
				<tbody>
					<tr>
						<td><img src="/assets/unmanaged/images/s.gif" height="1" width="460"></td>
					</tr>
					<tr>
						<td>
						<table border="0" cellpadding="0" cellspacing="0" width="450">
							<tbody>
								<tr class="tablehead">
									<td class="tableheadTD1" colspan="3"><b><content:getAttribute id="layoutPageBean" attribute="body1Header"/></b>
									</td>
								</tr>
								<tr class="datacell1">
									<td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"	width="1"></td>
									<td width="448" class="datacell1" align="center">
										<table border="0" cellpadding="3" cellspacing="0" width="450">

										<tbody>

											<tr class="datacell1">
												<td width="200" valign="top"><strong><span
													id="label_firstName">First name</span></strong></td>
<td valign="top">${theForm.firstName}</td>

											</tr>
											<tr class="datacell1">
												<td width="200"  valign="top"><strong><span
													id="label_lastName">Last name</span></strong></td>
<td  valign="top">${theForm.lastName}</td>

											</tr>
											<%-- DFS11 svc41 --%>
											<c:if test="${theForm.email != null}">
											<tr class="datacell1">
												<td width="200" valign="top"><strong><span
													id="label_email">Primary Email&nbsp;</span></strong></td>
<td valign="top">${theForm.email}</td>

											</tr>
											</c:if>
											
<c:if test="${not empty theForm.secondaryEmail}">
											<tr class="datacell1">
												<td width="200" valign="top"><strong><span
													id="label_email">Secondary Email&nbsp;</span></strong></td>
<td valign="top">${theForm.secondaryEmail}</td>

											</tr>
</c:if>
											<%-- DFS11 svc42 --%>
											<logicext:if name="theForm"	property="ssn.empty" op="equal" value="false">
											<logicext:then>
											<tr class="datacell1">
												<td  width="200" valign="top"><strong><span id="label_ssn">Social
												security number&nbsp;</span></strong></td>
																	<c:if test="${userProfile.role.roleId ne 'ICC'}">
																		<td><render:fullmaskSSN property="theForm.ssn" /></td>
																	</c:if>
																	<c:if test="${userProfile.role.roleId eq 'ICC'}">
																		<td><render:ssn property="theForm.ssn" /></td>
																	</c:if>
																</tr>
											</logicext:then>
											</logicext:if>
											
													<tr class="datacell1">
											
											<tr class="datacell1">
												<td width="200" valign="top"><strong><span
													id="label_telephone">Telephone number&nbsp;</span></strong></td>
												<td valign="top">
												<c:if test="${theForm.telephoneNumber != ''}">
															<render:phone property="theForm.telephoneNumber" />
														
<c:if test="${not empty theForm.telephoneExtension}">
															<img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">ext.
${theForm.telephoneExtension}
</c:if>
</c:if>
												</td>
											</tr>
											
											<tr class="datacell1">
												<td width="200" valign="top"><strong><span
													id="label_fax">Fax number&nbsp;</span></strong></td>
												<td valign="top">
													<c:if test="${theForm.faxNumber != ''}">
													<render:fax property="theForm.faxNumber" />
</c:if>
												</td>
											</tr>

										<tr class="datacell1">
											<td width="200" valign="top">
											<strong><span id="label_mobile">Mobile number &nbsp;</span></strong></td>
												<td valign="top">${theForm.mobileNumber}</td>
										</tr>
											
											<ps:isInternalUser name="userProfile" property="role">
<c:if test="${not empty theForm.comments}">
											<tr class="datacell1">
												<td width="200" valign="top"><strong><span
													id="label_comments">Comments&nbsp;</span></strong></td>
												<td valign="top">
${theForm.comments}
												</td>
											</tr>
</c:if>
											</ps:isInternalUser>
											
											<%-- client user svc 42b --%>
											<ps:isExternal name="userProfile" property="role">
											<ps:permissionAccess permissions="EXMN">
											<logicext:if name="theForm"	property="canManageAllContracts" op="equal" value="true">
											<logicext:then>
											<logicext:if name="clientAddEditUserForm" property="ownBusnessConvertedContract" op="equal" value="true">
											<logicext:then>
											<tr class="datacell1">
												<td  width="200" valign="top"><strong>Web access</strong></td>
												<td valign="top">
${theForm.webAccess}
												</td>
											</tr>
											</logicext:then>
											</logicext:if>
											</logicext:then>
											</logicext:if>
											</ps:permissionAccess>
											</ps:isExternal>
											<%-- internal user svc 42a --%>
											<ps:isInternalUser name="userProfile" property="role">
											<ps:permissionAccess permissions="EXMN">
											<logicext:if name="theForm" property="canManageAllContracts" op="equal" value="true">
											<logicext:then>
											<logicext:if name="clientAddEditUserForm" property="ownBusnessConvertedContract" op="equal" value="true">
											<logicext:then>
											<tr class="datacell1">
												<td  width="200" valign="top"><strong>Web access</strong></td>
												<td valign="top">
${theForm.webAccess}
												</td>
											</tr>
											</logicext:then>
											</logicext:if>
											</logicext:then>
											</logicext:if>
											</ps:permissionAccess>
											</ps:isInternalUser>
											<%-- DFS11 svc43 & svc44 --%>
											<logicext:if name="clientAddEditUserForm" property="webAccess" op="equal" value="<%=AddEditClientUserForm.FORM_YES_CONSTANT%>">
											<logicext:then>
											<%-- Username should be displayed for all the External Users --%>
											<ps:isExternal name="userProfile" property="role">
												<ps:permissionAccess permissions="EXMN">
												 	 <tr class="datacell1">
														<td  width="200" valign="top"><strong>Username</strong></td>
<td valign="top">${theForm.userName}</td>
												     </tr>
											     </ps:permissionAccess>
											</ps:isExternal>
											<%-- Username is not displayed for all the Internal Users, except Team Lead--%>
											<ps:isInternalUser name="userProfile" property="role">
											    <c:if test="${userProfile.role.roleId eq 'PLC'}">
													<tr class="datacell1">
														<td  width="200" valign="top"><strong>Username</strong></td>
<td valign="top">${theForm.userName}</td>
													</tr>
												</c:if>
											</ps:isInternalUser>
											<tr class="datacell1">
												<td width="200" valign="top"><strong>Password status </strong></td>
<td valign="top">${theForm.passwordState}</td>
											</tr>
											</logicext:then>
											</logicext:if>
											<%-- DFS11 svc 45 --%>
											<tr class="datacell1">
												<td width="200" valign="top"><strong>Profile status</strong></td>
												<td valign="top">
												<logicext:if name="clientAddEditUserForm" property="webAccess" op="equal" value="<%=AddEditClientUserForm.FORM_YES_CONSTANT%>">
												<logicext:then>
${theForm.profileStatus}
												</logicext:then>
												<logicext:else>
													n/a
												</logicext:else>
												</logicext:if>
												</td>
											</tr>
										</tbody>
									</table>

									</td>
									<td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1"	width="1"></td>
								</tr>
								<%-- Access contract page start --%>
								<jsp:include page="/WEB-INF/profiles/contractAccessViewItem.jsp"/>
								<%-- Access contract page end --%>
								<tr>
									<td class="databorder" colspan="3"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								</tr>
							</tbody>
						</table>
						<p>
						<table border="0" cellpadding="0" cellspacing="0" width="450">
							<tbody>
								<tr>
									<td align="right">
									<div align="left"><input name="action" value="back"
										onClick="return setActionAndSubmit('finish')"
										class="button134" type="submit"></div>
									</td>
									<c:if test="${userProfile.role.roleId ne 'PLC'}"> 
									<td align="right">
									<logicext:if name="clientAddEditUserForm" property="webAccess" op="equal" value="<%=AddEditClientUserForm.FORM_YES_CONSTANT%>">
									<logicext:and name="clientAddEditUserForm" property="profileStatus" op="notequal" value="<%=AddEditClientUserForm.PROFILE_STATUS_SUSPENDED%>"/>
										<logicext:then>
											<input type="submit" name="action" value="reset password" class="button134" onclick="return doSubmit('/do/profiles/viewUser/?action=reset&fromPSContactTab=true')">
										</logicext:then>
									</logicext:if></td>
									</c:if> 
									<td align="right">
										<logicext:if name="clientAddEditUserForm" property="profileStatus" op="notequal" value="<%=AddEditClientUserForm.PROFILE_STATUS_SUSPENDED%>">
											<logicext:then>
<c:set var="theProfileId" value="${clientAddEditUserForm.profileId}" />  
                                                <input name="action" value="edit this profile" onClick="editProfile('${theProfileId}');" class="button134" type="submit">
											</logicext:then>
										</logicext:if>
									</td>
									
								</tr>
								<tr>
									<td align="right">&nbsp;</td>
									<td align="right">&nbsp;</td>
									<td align="right">&nbsp;</td>
								</tr>
								<tr>
									<td width="150" align="right">
									<div align="left">
									<logicext:if name="clientAddEditUserForm" property="passwordState" op="equal" value="<%=com.manulife.pension.service.security.utility.SecurityConstants.LOCKED_PASSWORD_STATUS%>">
									<logicext:and name="clientAddEditUserForm" property="profileStatus" op="notequal" value="<%=AddEditClientUserForm.PROFILE_STATUS_SUSPENDED%>"/>
										<logicext:then>
											<input type="submit" name="action" value="<%=AddEditClientUserForm.BUTTON_LABEL_UNLOCK%>" class="button134"  onclick="return doSubmit('/do/profiles/viewUser/?action=unlock')">
										</logicext:then>
									</logicext:if>
									</div>
									</td>
									<td width="150" align="right">
									<div align="center"></div>
									</td>
									<td width="150" align="right">
									<div align="right"></div>
									</td>
								</tr>
							</tbody>
						</table>
						</p>

						</td>

					</tr>
				</tbody>
			</table>
			</td>
		</tr>
	</tbody>
</table>
