
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@page import="com.manulife.pension.ps.web.profiles.UserProfileForm"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<% UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

UserProfileForm theForm = (UserProfileForm)session.getAttribute("userProfileForm");
pageContext.setAttribute("theForm",theForm,PageContext.REQUEST_SCOPE);
%>

<script type="text/javascript">
	var submitted = false;

	function setActionAndSubmit(action) {
		if (!submitted) {
			submitted = true;
			document.userProfileForm.action.value = action;
			document.userProfileForm.submit();
		} else {
			window.status = "Transaction already in progress.  Please wait.";
		}
	}

	function doViewPermissions(number) {
		document.userProfileForm.selectedContractNumber.value = number;
		setActionAndSubmit("viewPermissions");
	}
</script>



<table border="0" cellpadding="0" cellspacing="0" width="700">
	<tbody>
		<tr>
			<td width="525"><img src="/assets/unmanaged/images/s.gif"
				height="1" width="1"></td>
		</tr>
		<tr>
			<td>
				<table border="0" cellpadding="0" cellspacing="0" width="400">

					<tbody>
						<tr>

							<td>
								<p></p>
								<div id="errordivcs">
									<content:errors scope="session" />
								</div>
								<p></p>
							</td>
						</tr>
						<tr>
							<td><img src="/assets/unmanaged/images/s.gif" height="1"
								width="460"></td>
						</tr>
						<tr>
							<td>
								<table border="0" cellpadding="0" cellspacing="0" width="450">
									<tbody>
										<tr>
											<td width="1"><img src="/assets/unmanaged/images/s.gif"
												height="1" width="1"></td>
											<td><img src="/assets/unmanaged/images/s.gif" height="1"
												width="1"></td>
											<td width="1"><img src="/assets/unmanaged/images/s.gif"
												height="1" width="1"></td>
										</tr>
										<tr class="tablehead">
											<td class="tableheadTD1" colspan="3"><b><content:getAttribute
														id="layoutPageBean" attribute="body1Header" /></b></td>
										</tr>
										<tr class="datacell1">
											<td width="1" class="databorder"><img
												src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
											<td width="448" class="datacell1" align="center">
												<table border="0" cellpadding="0" cellspacing="0"
													width="450">

													<tbody>

														<tr class="datacell1">
															<td width="200" valign="top"><strong><span
																	id="label_firstName">First name</span></strong></td>
<td valign="top">${theForm.firstName}</td>

														</tr>
														<tr class="datacell1">
															<td width="200" valign="top"><strong><span
																	id="label_lastName">Last name</span></strong></td>
<td valign="top">${theForm.lastName}</td>

														</tr>
														<!-- DFS11 svc41 -->
														
													<c:if test="${not empty theForm.email}">
															<tr class="datacell1">
																<td width="200" valign="top"><strong><span
																		id="label_email">Primary Email&nbsp;</span></strong></td>
<td valign="top">${theForm.email}</td>

															</tr>
														</c:if>
<c:if test="${not empty theForm.secondaryEmail}">
															<tr class="datacell1">
																<td width="200" valign="top"><strong><span
																		id="label_secondaryEmail">Secondary Email&nbsp;</span></strong></td>
<td valign="top">${theForm.secondaryEmail}</td>

															</tr>
</c:if>
														<!-- DFS11 svc42 -->
														<logicext:if name="theForm" property="ssn.empty"
															op="equal" value="false">
															<logicext:then>
																<tr class="datacell1">
																	<td width="200" valign="top"><strong><span
																			id="label_ssn">Social security number&nbsp;</span></strong></td>
																	<c:if test="${userProfile.role.roleId ne 'ICC'}">
																		<td><render:fullmaskSSN property="theForm.ssn" /></td>
																	</c:if>
																	<c:if test="${userProfile.role.roleId eq 'ICC'}">
																		<td><render:ssn property="theForm.ssn" /></td>
																	</c:if>
																</tr>
															</logicext:then>
														</logicext:if>

														<ps:isExternal name="theForm" property="userRole">
														
																	<tr class="datacell1">																										
															<td width="200" valign="top"><strong><span
																		id="label_telephone">Telephone number&nbsp;</span></strong></td>
																<td valign="top"><c:if
																		test="${theForm.telephoneNumber != ''}">

																		<render:phone property="theForm.telephoneNumber" />

																		<c:if test="${not empty theForm.telephoneExtension}">

																			<img src="/assets/unmanaged/images/s.gif" border="0"
																				height="1" width="1">ext.
${theForm.telephoneExtension}
</c:if>
																	</c:if></td>
															</tr>

															<tr class="datacell1">
																<td width="200" valign="top"><strong><span
																		id="label_fax">Fax number&nbsp;</span></strong></td>
<td valign="top"> <c:if test="${theForm.faxNumber != ''}" >

																		<render:fax property="theForm.faxNumber" />
</c:if></td>
															</tr>
															<tr class="datacell1">
																<td width="200" valign="top">
																	<strong><span id="label_mobile">Mobile number&nbsp;</span></strong></td>
																<td valign="top">${theForm.mobileNumber}</td>
																</tr>	
														</ps:isExternal>

														<ps:isInternalUser name="userProfile" property="role">
<c:if test="${not empty theForm.commentDetails.commentText}">

																<tr class="datacell1">
																	<td width="200" valign="top"><strong><span
																			id="label_comments">Comments&nbsp;</span></strong></td>
<td valign="top">${theForm.commentDetails.commentText}</td>

																</tr>
</c:if>
														</ps:isInternalUser>



														<!-- client user svc 42b -->
														<ps:isExternal name="userProfile" property="role">
															<ps:permissionAccess permissions="EXMN">
																<logicext:if name="theForm"
																	property="canManageAllContracts" op="equal"
																	value="true">
																	<logicext:then>
																		<logicext:if name="theForm"
																			property="ownBusnessConvertedContract" op="equal"
																			value="true">
																			<logicext:then>
																				<tr class="datacell1">
																					<td width="200" valign="top"><strong>Web
																							access</strong></td>
<td valign="top">${theForm.webAccess}</td>

																				</tr>
																			</logicext:then>
																		</logicext:if>
																	</logicext:then>
																</logicext:if>
															</ps:permissionAccess>
														</ps:isExternal>
														<!-- internal user svc 42a -->
														<ps:isInternalUser name="userProfile" property="role">
															<ps:permissionAccess permissions="EXMN">
																<logicext:if name="theForm"
																	property="canManageAllContracts" op="equal"
																	value="true">
																	<logicext:then>
																		<logicext:if name="theForm"
																			property="ownBusnessConvertedContract" op="equal"
																			value="true">
																			<logicext:then>
																				<tr class="datacell1">
																					<td width="200" valign="top"><strong>Web
																							access</strong></td>
<td valign="top">${theForm.webAccess}</td>

																				</tr>
																			</logicext:then>
																		</logicext:if>
																	</logicext:then>
																</logicext:if>
															</ps:permissionAccess>
														</ps:isInternalUser>
														<!-- DFS11 svc43 & svc44 -->
														<logicext:if name="theForm" property="webAccess"
															op="equal"
															value="<%=UserProfileForm.FORM_YES_CONSTANT%>">
															<logicext:then>
																<ps:isExternal name="userProfile" property="role">
																	<ps:permissionAccess permissions="EXMN">
																		<tr class="datacell1">
																			<td width="200" valign="top"><strong>Username</strong></td>
<td valign="top">${theForm.userName}</td>

																		</tr>
																	</ps:permissionAccess>
																</ps:isExternal>
																<tr class="datacell1">
																	<td width="200" valign="top"><strong>Password
																			status </strong></td>
<td valign="top">${theForm.passwordState}</td>

																</tr>
															</logicext:then>
														</logicext:if>
														<!-- DFS11 svc 45 -->
														<tr class="datacell1">
															<td width="200" valign="top"><strong>Profile
																	status</strong></td>
<td valign="top">${theForm.profileStatus}</td>

														</tr>
													</tbody>
												</table>

											</td>
											<td width="1" class="databorder"><img
												src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										</tr>
										<!-- Access contract page start -->
										<jsp:include page="/WEB-INF/profiles/contractAccessViewItem.jsp"></jsp:include>
										<!-- Access contract page end -->
										<tr>
											<td class="databorder" colspan="3"><img
												src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										</tr>
									</tbody>
								</table>
								<p>

									<ps:form cssClass="margin-bottom:0;" method="POST" action="/do/profiles/unsuspendProfile/" name="userProfileForm" modelAttribute="userProfileForm">

									<form:hidden path="userName"/>
									<form:hidden path="firstName"/>
									<form:hidden path="lastName"/>
									<form:hidden path="action"/>
									<form:hidden path="selectedContractNumber"/>
										<table border="0" cellpadding="0" cellspacing="0" width="450">
											<tbody>
												<tr>
													<td align="right">
														<div align="left">
															<input name="backButton" type="button"
																class="button100Lg" value="back"
																onclick="return setActionAndSubmit('back');" />
														</div>
													</td>
													<td align="right"><input name="unsuspendButton"
														type="button" class="button100Lg" value="unsuspend"
														size="50"
														onclick="return setActionAndSubmit('unsuspend');">
														</td>
												</tr>
												<tr>
													<td align="right">&nbsp;</td>
													<td align="right">&nbsp;</td>
													<td align="right">&nbsp;</td>
												</tr>
											</tbody>
										</table>
										</ps:form>
								</p>
							</td>
						</tr>
					</tbody>
				</table>
			</td>
		</tr>
	</tbody>
</table>

