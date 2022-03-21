
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@page import="com.manulife.pension.ps.web.profiles.UserProfileForm"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>

<% UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

UserProfileForm theForm = (UserProfileForm)session.getAttribute("userProfileForm");
pageContext.setAttribute("theForm",theForm,PageContext.REQUEST_SCOPE);
%>

<content:contentBean
	contentId="<%=ContentConstants.WARNING_ATLEAST_ONE_TRUSTEE_MAIL_RECIPIENT%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	beanName="noTrusteeMailRecipient" />


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

	function doSuspend() {
		if (!submitted) {
			var warnings = "";
			var i = 0;
			var contractNumberElement;
			while ((contractNumberElement = document
					.getElementsByName("contractAccesses[" + i
							+ "].contractNumber")[0]) != null) {
				// Warning if contract does not have a Client or TPA profile with manage users permission
				if (document.getElementsByName("contractAccesses[" + i
						+ "].lastUserWithManageUsers")[0].value == "true") {
					warnings += "Warning! No client users for contract "
							+ contractNumberElement.value
							+ " will have Manage Users permission.\n";
				}

				// Warning if contract does not have a Client or TPA profile with submissions access permission
				if (document.getElementsByName("contractAccesses[" + i
						+ "].lastUserWithSubmissionsAccess")[0].value == "true") {
					warnings += "Warning! No client users for contract "
							+ contractNumberElement.value
							+ " will have Submissions Access permission.\n";
				}

				// Warning if contract does not have a Client or TPA profile with signing authority (approve i:withdrawals) permission - Loans proj
				if (document.getElementsByName("contractAccesses[" + i
						+ "].lastUserWithSigningAuthority")[0].value == "true") {
					warnings += "A user with Signing authority permission must be specified for the contract "
							+ contractNumberElement.value + ".\n";
				}

				// Warning if contract does not have a Client or TPA profile with review i:withdrawals permission
				// This can come when who will review withdrawal requests = "TPA"
				if (document.getElementsByName("contractAccesses[" + i
						+ "].lastUserWithReviewIWithdrawals")[0].value == "true") {
					warnings += "A profile with Review i:withdrawals permission must be specified for the contract "
							+ contractNumberElement.value + ".\n";
				}

				// Warning if contract does not have a Client or TPA profile with review loans permission
				// This can come when who will review loan requests = "TPA"
				if (document.getElementsByName("contractAccesses[" + i
						+ "].lastUserWithReviewLoansPermission")[0].value == "true") {
					warnings += "A profile with Review loans permission must be specified for the contract "
							+ contractNumberElement.value + ".\n";
				}

				// Warning if contract does not have a Client profile with review i:withdrawals permission
				// This can come when who will review withdrawal requests = "Plan Sponsor"
				if (document.getElementsByName("contractAccesses[" + i
						+ "].lastClientUserWithReviewIWithdrawals")[0].value == "true") {
					warnings += "A client profile with Review i:withdrawals permission must be specified for the contract "
							+ contractNumberElement.value + ".\n";
				}

				// Warning if contract does not have a Client profile with review loans permission
				// This can come when who will review loan requests = "Plan Sponsor"
				if (document.getElementsByName("contractAccesses[" + i
						+ "].lastClientUserWithReviewLoans")[0].value == "true") {
					warnings += "A client profile with Review loans permission must be specified for the contract "
							+ contractNumberElement.value + ".\n";
				}

				if (document.getElementsByName("contractAccesses[" + i
						+ "].trusteeMailRecipientProfileId")[0].value != ""
						&& document.getElementsByName("contractAccesses[" + i
								+ "].trusteeMailRecipientProfileId")[0].value == document.userProfileForm.userName.value) {
					var warMsg = '<content:getAttribute beanName="noTrusteeMailRecipient" attribute="text"/>';
					warMsg = warMsg.replace(/\{0\}/,
							contractNumberElement.value);
					warnings += warMsg + "\n";
				}

				i++;
			}

			i = 0;
			var tpaIdElement;
			while ((tpaIdElement = document.getElementsByName("tpaFirm[" + i
					+ "].id")[0]) != null) {
				// Warning if contract does not have a Client or TPA profile with manage users permission
				if (document.getElementsByName("tpaFirm[" + i
						+ "].lastUserWithManageUsers")[0].value == "true") {
					warnings += "Warning! No users for firm "
							+ tpaIdElement.value
							+ " will have Manage Users permission.\n";
				}

				// Warning if contract does not have a Client or TPA profile with Receive i:loans email preference
				if (document.getElementsByName("tpaFirm[" + i
						+ "].lastUserWithReceiveILoansEmail")[0].value == "true") {
					warnings += "Warning! No users for firm "
							+ tpaIdElement.value
							+ " will receive i:loans emails.\n";
				}

				// Warning if contract does not have a Client or TPA profile with Receive i:loans and TPA Staff Plan Access permission
				if (document.getElementsByName("tpaFirm[" + i
						+ "].lastUserWithReceiveILoansEmailAndTPAStaffPlan")[0].value == "true") {
					warnings += "Warning! No users for firm "
							+ tpaIdElement.value
							+ " will receive i:loans emails and have TPA staff plan access permission.\n";
				}

				// Warning if contract does not have a Client or TPA profile with signing authority (approve i:withdrawals) permission - Loans proj
				var signingAuthContracts = document
						.getElementsByName("tpaFirm[" + i
								+ "].lastUserWithASigningAuthorityContracts")[0].value;
				if (signingAuthContracts != null && signingAuthContracts != "") {
					warnings += "Warning! No users for contract(s) "
							+ signingAuthContracts
							+ " will have Signing authority permission.\n";
				}

				// Warning if contract does not have a Client or TPA profile with review i:withdrawals permission
				var reviewWDWContracts = document.getElementsByName("tpaFirm["
						+ i + "].lastUserWithReviewIWithdrawalsContracts")[0].value;
				if (reviewWDWContracts != null && reviewWDWContracts != "") {
					warnings += "Warning! No users for contract(s) "
							+ reviewWDWContracts
							+ " will have Review i:withdrawals permission.\n";
				}

				// Warning if contract does not have a Client or TPA profile with review loans permission
				var reviewLoansContracts = document
						.getElementsByName("tpaFirm[" + i
								+ "].lastUserWithReviewLoansContracts")[0].value;
				if (reviewLoansContracts != null && reviewLoansContracts != "") {
					warnings += "Warning! No users for contract(s) "
							+ reviewLoansContracts
							+ " will have Review loans permission.\n";
				}

				i++;
			}

			var confirmAction = true;
			if (warnings != "") {
				confirmAction = confirm(warnings
						+ "Are you sure you want to suspend this user?");
			}
			if (confirmAction) {
				setActionAndSubmit("suspend");
			}
		} else {
			window.status = "Transaction already in progress.  Please wait.";
		}
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
												<table border="0" cellpadding="3" cellspacing="0"
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

																		<c:if test="${theForm.telephoneExtension != ''}">

																			<img src="/assets/unmanaged/images/s.gif" border="0"
																				height="1" width="1">ext.${theForm.telephoneExtension}
																		</c:if>
																	</c:if></td>
															</tr>

															<tr class="datacell1">
																<td width="200" valign="top"><strong><span
																		id="label_fax">Fax number&nbsp;</span></strong></td>
<td valign="top"><c:if test="${theForm.faxNumber !=''}" >

																		<render:fax property="theForm.faxNumber" />
</c:if></td>
															</tr>
													
															<tr class="datacell1">
																<td width="200" valign="top"><strong><span id="label_mobile">Mobile number&nbsp;</span></strong></td>
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
										<jsp:include page="/WEB-INF/profiles/contractAccessViewItem.jsp"/>
										<!-- Access contract page end -->
										<tr>
											<td class="databorder" colspan="3"><img
												src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										</tr>
									</tbody>
								</table>
								<p>

									<ps:form cssClass="margin-bottom:0;" method="POST" action="/do/profiles/suspendProfile/" name="userProfileForm" modelAttribute="userProfileForm">

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
																onclick="setActionAndSubmit('back');" />
														</div>
													</td>
													<td align="right"><input name="suspendButton"
														type="button" class="button100Lg" value="suspend"
														size="50" onclick="doSuspend();"> </td>
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

