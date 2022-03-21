<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>       
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.profiles.AddEditClientUserForm" %>

<%@ page import="com.manulife.pension.ps.web.Constants"%>
<script type="text/javascript" src="/assets/unmanaged/javascript/layer_functions.js">
</script>

<% UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.REQUEST_SCOPE);
AddEditClientUserForm theForm = (AddEditClientUserForm)session.getAttribute("clientAddEditUserForm");
pageContext.setAttribute("theForm",theForm,PageContext.REQUEST_SCOPE);
%>
<script type="text/javascript">
var submitted = false;

function doFinish() {
	if (!submitted) {
		submitted = true;
		var url = new URL(window.location.href);
		url.setParameter('action', 'finish');
		window.location.href = url.encodeURL();
	} else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	}
}
function editProfile(userName){
	if (!submitted) {
		submitted = true;
		location.href='/do/profiles/editUser/?userName='+userName+'&clientUserAction=editUser';
	} else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	}
}

function showPermissions(id){
	var permissionId = 'sc' + id;
	expandcontent(permissionId);
}
</script>

<%--
Defect 007287: Don't highlight changes in add user confirmation screen.
--%>
<logicext:if name="layoutBean" property="param(task)" op="equal"
	value="add">
	<logicext:then>
		<style>
.highlightBold {
	color : #000000;
	font-weight: normal;
}
</style>
	</logicext:then>
</logicext:if>


<table border="0" cellpadding="0" cellspacing="0" width="760">
	<tbody>
		<tr>
			<td width="30"><img src="/assets/unmanaged/images/s.gif" height="1" width="30"></td>
			<td width="715">
			<table border="0" cellpadding="0" cellspacing="0" width="700">
				<tbody>
					<tr>
						<td width="525"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
						<td width="5"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
						<td width="170"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
					</tr>
					<tr>
						<td>
						<table border="0" cellpadding="0" cellspacing="0" width="760">
							<tbody>
								<tr>
									<td>
									<TABLE class=box cellSpacing=0 cellPadding=0 width=500 border=0>
										<TBODY>
											<TR class=tablehead>
												<TD class=tableheadTD1 colSpan=3><B><content:getAttribute
													id="layoutPageBean" attribute="body1Header" /> </B></TD>
											</TR>
											<TR>
												<TD class=boxborder width=1><IMG height=1
													src="/assets/unmanaged/images/s.gif" width=1></TD>
												<TD>
												<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
													<TBODY>
														<TR class="datacell1">
															<td width="80"><strong>First name </strong></td>
															<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif"	width=1></TD>
															<TD width="115">
																<ps:highlightIfChanged	name="theForm" property="firstName">
${theForm.firstName}
																</ps:highlightIfChanged>
															</TD>
															<td width="150"><strong>Fax number</strong></td>	
															<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
															<TD width="175">
															<c:if test="${theForm.faxNumber != ''}">
																<ps:highlightIfChanged	name="theForm" property="faxNumber">
																		<render:fax property="theForm.faxNumber"></render:fax>							
																</ps:highlightIfChanged>
</c:if>
															</TD>
														</TR>
														
														<TR class="datacell1">
															<td width="80"><strong>Last name </strong></td>
															<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
															<TD width="115">
																<ps:highlightIfChanged	name="theForm" property="lastName">
${theForm.lastName}
																</ps:highlightIfChanged></TD>
																
																<ps:isInternalUser name="userProfile" property="role">
															</ps:isInternalUser>
															
														<TR class="datacell1">														
																<td width="80"><strong>Primary Email</strong></td>	
																<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
																<TD width="115">
																	<ps:highlightIfChanged	name="theForm" property="email">
${theForm.email}
																	</ps:highlightIfChanged>
																</TD>

														<td width="150"><strong>Social Security	Number</strong></td>
															<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
															<TD width="115">
															<% if (theForm.getSsn().toString().length() > 0) { %>
															<ps:highlightIfChanged name="theForm" property="ssn">
																					<c:if test="${userProfile.role.roleId ne 'ICC'}">
																						<render:fullmaskSSN property="theForm.ssn" />
																					</c:if>
																					<c:if test="${userProfile.role.roleId eq 'ICC'}">
																						<render:ssn property="theForm.ssn" />
																					</c:if>
																				</ps:highlightIfChanged></TD>
															<% } %>
														</TR>
<TR class="datacell1">
															<td width="80"><strong>Telephone number</strong></td>	
															<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
															<TD width="115">
															<c:if test="${theForm.telephoneNumber != ''}">
																<ps:highlightIfChanged	name="theForm" property="telephoneNumber">
																		<render:phone property="theForm.telephoneNumber"></render:phone>
																</ps:highlightIfChanged>
</c:if>
<c:if test="${not empty theForm.telephoneExtension}">
																&nbsp;ext.&nbsp;
																<ps:highlightIfChanged	name="theForm" property="telephoneExtension">
${theForm.telephoneExtension}
																</ps:highlightIfChanged>
</c:if>
															</TD>
<logicext:if name="clientAddEditUserForm" property="canManageAllContracts" op="equal" value="true">
																<logicext:then>
																	<td width="150"><strong>Web Access</strong></td>
																	<TD class=datadivider><IMG height=1	src="/assets/unmanaged/images/s.gif" width=1></TD>
																	<TD>
																		<ps:highlightIfChanged name="theForm" property="webAccess">
${theForm.webAccess}
																		</ps:highlightIfChanged>
																	</TD>
																</logicext:then>
															</logicext:if>
															<td>&nbsp;</td>
															<TD class=datadivider><IMG height=1	src="/assets/unmanaged/images/s.gif" width=1></TD>
															<TD>&nbsp;</TD>
											
														</TR>

																													
														
<c:if test="${not empty theForm.secondaryEmail}">
														<TR class="datacell1">														
																<td width="80"><strong>Secondary Email</strong></td>	
																<TD width="1" class=datadivider><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
																<TD width="115">
${theForm.secondaryEmail}
																</TD>
														</TR>
</c:if>
													
														
														<tr>
															<TD class=datadivider colspan="6"><IMG height=1	src="/assets/unmanaged/images/s.gif" width=1></TD>
														</tr>
														<ps:isInternalUser name="userProfile" property="role">
														<TR class="datacell1">
															<td colspan="1" width="80"><strong>Comments </strong></td>
															<td colspan="5"><c:out value="${theForm.comments}"/></td>
														</TR>
														</ps:isInternalUser>
													</TBODY>
												</TABLE>
												</TD>
												<TD class=boxborder width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
											</TR>
											<TR>
												<TD colSpan=3>
												<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
													<TBODY>
														<TR>
															<TD width="1" class=boxborder><IMG height=1	src="/assets/unmanaged/images/s.gif" width=1></TD>
															<TD class=boxborder><IMG height=1 src="/assets/unmanaged/images/s.gif"	width=1></TD>
														</TR>
													</TBODY>
												</TABLE>
												</TD>
											</TR>
										</TBODY>
									</TABLE>
									<Br>
									<br>
									<TABLE class=box cellSpacing=0 cellPadding=0 width=695 border=0>
										<TBODY>
											<TR>
												<TD bgcolor="#FFF9F2">
												<c:if test="${empty param.printFriendly}">
												<TABLE cellSpacing=0 cellPadding=0 width="500" border=0>
													<TBODY>
														<%-- sco29 --%>
														<logicext:if name="theForm" property="webAccess" op="equal"	value="Yes">
														<logicext:then>
														<%-- internal user show link --%>
														<ps:isInternalUser name="userProfile" property="role">
														<TR>
															<TD><A href="javascript:openChangeWindow()"></A></TD>
															<TD align=right><STRONG><A href="javascript:expandAllSections()">
															Show all permissions & email preferences </A>/<A href="javascript:contractAllSections()">Hide all permissions & email preferences </A> </STRONG>
															</TD>
														</TR>
														</ps:isInternalUser>
														<ps:isExternal name="userProfile" property="role">
<c:if test="${theForm.nonBCFlag ==true}">
															<TR>
															<TD><A href="javascript:openChangeWindow()"></A></TD>
															<TD align=right><STRONG><A href="javascript:expandAllSections()">
															Show all permissions & email preferences </A>/<A href="javascript:contractAllSections()">Hide all permissions & email preferences </A></STRONG></TD>
															</TR>
</c:if>
														</ps:isExternal>
														</logicext:then>
														</logicext:if>
													</TBODY>
												</TABLE>
												<Br>
												</c:if>
												<%-- access contract start --%>
												<%@include file="contractAccessConfirmItem.jsp" %>
												<%-- access contract end --%>
												</TD>
											</TR>
											<c:if test="${empty param.printFriendly}">
											<TR>
												<TD bgcolor="#FFF9F2">
												<table width="500" border="0">
													<tr>
														<td width="187">
														<logicext:if name="theForm" property="contractAccessEmpty" op="equal"	value="false">
														<logicext:then>
														<div align="left"><input name="editButton"
															value="edit this profile"
															onClick="editProfile('<%=StringEscapeUtils.escapeJavaScript(theForm.getUserName())%>');"
															class="button134" type="submit">
														</div>
														</logicext:then>
														
														</logicext:if>
														</td>
														<td width="187">
														<div align="center"><input name="printButton"
															value="print" onClick="doPrintForConfirm();"
															class="button134" type="submit"></div>
														</td>
														<td width="187">
														<div align="right"><input name="finishButton"
															value="finish"
															onClick="doFinish()"
															class="button134" type="submit"></div>
														</td>
													</tr>
												</table>
												</TD>
											</TR>
											</c:if>
										</TBODY>
									</TABLE>
									</td>
								</tr>
								
								<c:if test="${empty param.printFriendly}">
									<tr><td valign="top"><Br></td></tr>
								</c:if>
						</td>
						<td width="5"><img src="/assets/unmanaged/images/s.gif" height="1" width="20"></td>
						<td valign="top"><img src="/assets/unmanaged/images/s.gif" height="5" width="1">
						<img src="/assets/unmanaged/images/s.gif" height="5" width="1">
						</td>
					</tr>
				</tbody>
		</table>
	</td>
	</tr>
</tbody>
</table>
