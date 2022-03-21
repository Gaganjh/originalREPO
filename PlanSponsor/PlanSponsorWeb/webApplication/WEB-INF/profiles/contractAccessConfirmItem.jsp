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
<%@ page import="com.manulife.pension.ps.web.profiles.AddEditClientUserForm"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>


<c:set var="intermediaryContactId" value="${IntermediaryContact.ID}" />
<c:forEach items="${clientAddEditUserForm.allContractAccesses}" var="contractAccesses" varStatus="theIndex" >
 <c:set var="indexValue" value="${theIndex.index}"/> 
 
 <% 
  String theIndex1 = pageContext.getAttribute("indexValue").toString();
 
 %>
<c:if test="${contractAccesses.planSponsorSiteRole.value !='na'}">
	<!-- add contract -->
	<TABLE class=box cellSpacing=0 cellPadding=0 width=500 border=0>
		<TBODY>
			<TR class=tablehead>
<TD class=tableheadTD1 colspan="3" width="500"><B>${contractAccesses.contractName},&nbsp;${contractAccesses.contractNumber}&nbsp;permissions

				</B></TD>
			</TR>
			<tr class="datacell1">
				<TD class=boxborder width=1><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
				<TD>
					<table width="480" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td width="280" class="datacell1" valign="top">
								<table width="280" border="0" cellpadding="0" cellspacing="0">
									<tr class="datacell1">
										<td width="100" class="datacell1">
											<strong>Contract number </strong>
										</td>
										<TD width="1" class=datadivider>
											<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
										</TD>
										<TD width="178" class="datacell1">
${contractAccesses.contractNumber}
										</TD>
									</tr>
									<tr>
										<td width="100" class="datacell1">
											<strong>Contract name </strong>
										</td>
										<TD width="1" class=datadivider>
											<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
										</TD>
										<TD width="178" class="datacell1">
<b>${contractAccesses.contractName}</b>
										</TD>
									</tr>
									<tr class="datacell1">
										<td width="100" class="datacell1"><strong>Role</strong></td>
										<TD width="1" class=datadivider>
											<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
										</TD>
										<TD width="178" class="datacell1">
											<%
												String planSponsorSiteRoleStr = "allContractAccesses[" + theIndex1 +"].planSponsorSiteRole.value";
											%>
											<ps:highlightIfChanged name="theForm" property="<%=planSponsorSiteRoleStr %>">
${contractAccesses.planSponsorSiteRole}
											</ps:highlightIfChanged>
										</TD>
									</tr>
									
<c:if test="${contractAccesses.cbcIndicator ==true}">
<c:if test="${contractAccesses.planSponsorSiteRole.value== intermediaryContactId}">
									<tr class="datacell1">
										<td width="100" class="datacell1"><strong>Contact Type</strong></td>
										<TD width="1" class=datadivider>
											<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
										</TD>
										<TD width="178" class="datacell1">
											<%
												String contactTypeStr = "allContractAccesses[" + theIndex1 +"].roleType";
											%>
											<ps:highlightIfChanged name="theForm" property="<%=contactTypeStr %>">
${contractAccesses.roleTypeDisplayName}
											</ps:highlightIfChanged>
										</TD>
									</tr>
</c:if>
</c:if>
								<logicext:if name="contractAccesses" property="cbcIndicator" op="equal" value="true">
									<logicext:then>
										<logicext:if name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=Trustee.ID%>">
										<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AuthorizedSignor.ID%>" />
										<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AdministrativeContact.stringID%>" />
											<logicext:then>
											<tr class="datacell1">
												<td width="100" class="datacell1">
													<strong>Primary	contact </strong>
												</td>
												<TD width="1" class=datadivider>
													<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
												</TD>
												<td width="178" class="datacell1">
													<%
														String primaryContactStr = "allContractAccesses[" + theIndex1 +"].primaryContact";
													%>
													<ps:highlightIfChanged name="theForm" property="<%=primaryContactStr %>">
														<logicext:if name="contractAccesses" property="primaryContact" op="equal" value="true">
															<logicext:then>Yes</logicext:then>
															<logicext:else>No</logicext:else>
														</logicext:if>
													</ps:highlightIfChanged>
												</td>
											</tr>
											</logicext:then>
										</logicext:if>
									</logicext:then>
									<logicext:else>
										<tr class="datacell1">
											<td width="100" class="datacell1">
												<strong>Primary	contact</strong>
											</td>
											<TD width="1" class=datadivider>
												<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
											</TD>
											<td width="178" class="datacell1">
												<%
													String primaryContactStr = "allContractAccesses[" + theIndex1 +"].primaryContact";
												%>
												<ps:highlightIfChanged name="theForm" property="<%=primaryContactStr %>">
													<logicext:if name="contractAccesses" property="primaryContact" op="equal" value="true">
														<logicext:then>Yes</logicext:then>
														<logicext:else>No</logicext:else>
													</logicext:if>
												</ps:highlightIfChanged>
											</td>
										</tr>
									</logicext:else>
								</logicext:if>
								<logicext:if name="contractAccesses" property="cbcIndicator" op="equal" value="true">
								<logicext:then>
									<logicext:if name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=Trustee.ID%>">
									<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AuthorizedSignor.ID%>" />
									<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AdministrativeContact.stringID%>" />
										<logicext:then>
											<tr class="datacell1">
												<td width="100" class="datacell1">
													<strong>Client mail recipient </strong>
												</td>
												<TD width="1" class=datadivider>
													<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
												</TD>
												<td width="178" class="datacell1">
													<%
														String mailRecepientStr = "allContractAccesses[" + theIndex1 +"].mailRecepient";
													%>
													<ps:highlightIfChanged name="theForm" property="<%=mailRecepientStr %>">
														<logicext:if name="contractAccesses" property="mailRecepient" op="equal" value="true">
															<logicext:then>Yes</logicext:then>
															<logicext:else>No</logicext:else>
														</logicext:if>
													</ps:highlightIfChanged>
												</td>
											</tr>
										</logicext:then>
									</logicext:if>
								</logicext:then>
								<logicext:else>
									<%-- System converted contract --%>
									<tr class="datacell1">
										<td width="100" class="datacell1">
											<strong>Client mail recipient </strong>
										</td>
										<TD width="1" class=datadivider>
											<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
										</TD>
										<td width=198" class="datacell1">
											<%
												String mailRecepientStr = "allContractAccesses[" + theIndex1 +"].mailRecepient";
											%>
											<ps:highlightIfChanged name="theForm" property="<%=mailRecepientStr %>">
												<logicext:if name="contractAccesses" property="mailRecepient" op="equal" value="true">
													<logicext:then>Yes</logicext:then>
													<logicext:else>No</logicext:else>
												</logicext:if>
											</ps:highlightIfChanged>
										</td>
									</tr>
								</logicext:else>
								</logicext:if>
								</table>
							</td>
							<td class="datacell1" valign="top">
								<table width="199" border="0" cellpadding="0" cellspacing="0">
								
								<logicext:if name="contractAccesses" property="cbcIndicator" op="equal" value="true">
								<logicext:then>
									<logicext:if name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=Trustee.ID%>">
									<logicext:then>
									<tr class="datacell1">
											<td width="150" class="datacell1">
												<strong>Trustee mail recipient </strong>
											</td>
											<TD width="1" class=datadivider>
												<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
											</TD>
											<td width="48" class="datacell1">
												<%
													String trusteeMailRecepientStr = "allContractAccesses[" + theIndex1 +"].trusteeMailRecepient";
												%>
												<ps:highlightIfChanged name="theForm" property="<%=trusteeMailRecepientStr %>">
													<logicext:if name="contractAccesses" property="trusteeMailRecepient" op="equal" value="true">
														<logicext:then>Yes</logicext:then>
														<logicext:else>No</logicext:else>
													</logicext:if>
												</ps:highlightIfChanged>
											</td>
										</tr>
									</logicext:then>
									</logicext:if>
								</logicext:then>
								<logicext:else>
										<tr class="datacell1">
											<td width="150" class="datacell1">
												<strong>Trustee mail recipient </strong>
											</td>
											<TD width="1" class=datadivider>
												<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
											</TD>
											<td width="48" class="datacell1">
												<%
													String trusteeMailRecepientStr = "allContractAccesses[" + theIndex1 +"].trusteeMailRecepient";
												%>
												<ps:highlightIfChanged name="theForm" property="<%=trusteeMailRecepientStr %>">
													<logicext:if name="contractAccesses" property="trusteeMailRecepient" op="equal" value="true">
														<logicext:then>Yes</logicext:then>
														<logicext:else>No</logicext:else>
													</logicext:if>
												</ps:highlightIfChanged>
											</td>
										</tr>
								</logicext:else>
								</logicext:if>
								<logicext:if name="contractAccesses" property="cbcIndicator" op="equal" value="true">
								<logicext:then>
								<logicext:if name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=Trustee.ID %>">
								<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AuthorizedSignor.ID %>" />
								<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AdministrativeContact.stringID %>" />
								<logicext:then>
<c:if test="${contractAccesses.iccDesignateEligible ==true}">
										<tr class="datacell1">
											<td width="150" class="datacell1">
												<strong>Investment Comparative Chart Designate </strong>
											</td>
											<TD width="1" class=datadivider>
												<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
											</TD>
											<td width="48" class="datacell1">
												<%
													String iccDesignateStr = "allContractAccesses[" + theIndex1 +"].iccDesignate";
												%>
												<ps:highlightIfChanged name="theForm" property="<%=iccDesignateStr %>">
													<logicext:if name="contractAccesses" property="iccDesignate" op="equal" value="true">
														<logicext:then>Yes</logicext:then>
														<logicext:else>No</logicext:else>
													</logicext:if>
													</ps:highlightIfChanged>
											</td>
										</tr>
</c:if>
<c:if test="${contractAccesses.sendServiceDesignateEligible ==true}">
										<tr class="datacell1">
											<td width="150" class="datacell1">
												<strong>SEND Service Notice Contact </strong>
											</td>
											<TD width="1" class=datadivider>
												<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
											</TD>
											<td width="48" class="datacell1">
												<%
													String sendServiceDesignateStr = "allContractAccesses[" + theIndex1 +"].sendServiceDesignate";
												%>
												<ps:highlightIfChanged name="theForm" property="<%=sendServiceDesignateStr %>">
													<logicext:if name="contractAccesses" property="sendServiceDesignate" op="equal" value="true">
														<logicext:then>Yes</logicext:then>
														<logicext:else>No</logicext:else>
													</logicext:if>
												</ps:highlightIfChanged>
											</td>
										</tr>
</c:if>
								</logicext:then>
								</logicext:if>
								<ps:isInternalUser name="userProfile" property="role">
								<tr class="datacell1">
									<td width="150" class="datacell1">
										<strong>Participant statement consultant </strong>
									</td>
									<TD width="1" class=datadivider>
										<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
									</TD>
									<td width="48" class="datacell1">
										<%
											String statementIndicatorStr = "allContractAccesses[" + theIndex1 +"].statementIndicator";
										%>
										<ps:highlightIfChanged name="theForm" property="<%=statementIndicatorStr %>">
											<logicext:if name="contractAccesses" property="statementIndicator" op="equal" value="true">
												<logicext:then>Yes</logicext:then>
												<logicext:else>No</logicext:else>
											</logicext:if>
											</ps:highlightIfChanged>
									</td>
								</tr>
								
								</ps:isInternalUser>
								</logicext:then>
								<logicext:else>
									<%-- System converted contract --%>
<c:if test="${contractAccesses.iccDesignateEligible ==true}">
										<tr class="datacell1">
											<td width="150" class="datacell1">
												<strong>Investment Comparative Chart Designate </strong>
											</td>
											<TD width="1" class=datadivider>
												<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
											</TD>
											<td width="48" class="datacell1">
												<%
													String iccDesignateStr = "allContractAccesses[" + theIndex1 +"].iccDesignate";
												%>
												<ps:highlightIfChanged name="theForm" property="<%=iccDesignateStr %>">
													<logicext:if name="contractAccesses" property="iccDesignate" op="equal" value="true">
														<logicext:then>Yes</logicext:then>
														<logicext:else>No</logicext:else>
													</logicext:if>
													</ps:highlightIfChanged>
											</td>
										</tr>
</c:if>
<c:if test="${contractAccesses.sendServiceDesignateEligible ==true}">
										<tr class="datacell1">
											<td width="150" class="datacell1">
												<strong>SEND Service Notice Contact </strong>
											</td>
											<TD width="1" class=datadivider>
												<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
											</TD>
											<td width="48" class="datacell1">
												<%
													String sendServiceDesignateStr = "allContractAccesses[" + theIndex1 +"].sendServiceDesignate";
												%>
												<ps:highlightIfChanged name="theForm" property="<%=sendServiceDesignateStr %>">
													<logicext:if name="contractAccesses" property="sendServiceDesignate" op="equal" value="true">
														<logicext:then>Yes</logicext:then>
														<logicext:else>No</logicext:else>
													</logicext:if>
													</ps:highlightIfChanged>
											</td>
										</tr>
</c:if>
								</logicext:else>
								</logicext:if>
								
								<logicext:if name="contractAccesses" property="cbcIndicator" op="equal" value="true">
								<logicext:then>
									<%-- Business converted contract --%>
									<ps:isInternalUser name="userProfile" property="role">
										<logicext:if name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=Trustee.ID%>">
										<logicext:or name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AuthorizedSignor.ID%>" />
										<logicext:then>
										<tr class="datacell1">
													<td width="150" class="datacell1">
														<strong>Signature received trustee </strong>
													</td>
													<TD width="1" class=datadivider>
														<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
													</TD>
													<td width="48" class="datacell1">
														<%
															String signatureReceivedTrusteeStr = "allContractAccesses[" + theIndex1 +"].signatureReceivedTrustee";
														%>
														<ps:highlightIfChanged name="theForm" property="<%=signatureReceivedTrusteeStr %>">
															<logicext:if name="contractAccesses" property="signatureReceivedTrustee" op="equal" value="true">
																<logicext:then>Yes</logicext:then>
																<logicext:else>No</logicext:else>
															</logicext:if>
														</ps:highlightIfChanged>
													</td>
										</tr>
										</logicext:then>
										</logicext:if>
										
										<logicext:if name="contractAccesses" property="planSponsorSiteRole.value" op="equal" value="<%=AuthorizedSignor.ID%>" >
										<logicext:then>	
										<tr class="datacell1">
												<td width="150" class="datacell1">
													<strong>Signature received authorized signer </strong>
												</td>
												<TD width="1" class=datadivider>
													<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
												</TD>
												<td width="48" class="datacell1">
													<%
														String signatureReceivedAuthSignerStr = "allContractAccesses[" + theIndex1 +"].signatureReceivedAuthSigner";
													%>
													<ps:highlightIfChanged name="theForm" property="<%=signatureReceivedAuthSignerStr %>">
														<logicext:if name="contractAccesses" property="signatureReceivedAuthSigner" op="equal" value="true">
															<logicext:then>Yes</logicext:then>
															<logicext:else>No</logicext:else>
														</logicext:if>
														</ps:highlightIfChanged>
												</td>
											</tr>
										</logicext:then>
										</logicext:if>
									</ps:isInternalUser>
									
								</logicext:then>
								<logicext:else>
									<%-- System converted contract --%>
									<ps:isInternalUser name="userProfile" property="role">
										<tr class="datacell1">
												<td width="150" class="datacell1">
													<strong>Signature received trustee </strong>
												</td>
												<TD width="1" class=datadivider>
													<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
												</TD>
												<td width="48" class="datacell1">
														<%
															String signatureReceivedTrusteeStr = "allContractAccesses[" + theIndex1 +"].signatureReceivedTrustee";
														%>
														<ps:highlightIfChanged name="theForm" property="<%=signatureReceivedTrusteeStr %>">
															<logicext:if name="contractAccesses" property="signatureReceivedTrustee" op="equal" value="true">
																<logicext:then>Yes</logicext:then>
																<logicext:else>No</logicext:else>
															</logicext:if>
														</ps:highlightIfChanged>
												</td>
										</tr>
										<ps:permissionAccess permissions="EXTA">
											<tr class="datacell1">
												<td width="150" class="datacell1">
													<strong>Signature received authorized signer </strong>
												</td>
												<TD width="1" class=datadivider>
													<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
												</TD>
												<td width="48" class="datacell1">
													<%
														String signatureReceivedAuthSignerStr = "allContractAccesses[" + theIndex1 +"].signatureReceivedAuthSigner";
													%>
													<ps:highlightIfChanged name="theForm" property="<%=signatureReceivedAuthSignerStr %>">
														<logicext:if name="contractAccesses" property="signatureReceivedAuthSigner" op="equal" value="true">
															<logicext:then>Yes</logicext:then>
															<logicext:else>No</logicext:else>
														</logicext:if>
														</ps:highlightIfChanged>
												</td>
											</tr>
										</ps:permissionAccess>	
									</ps:isInternalUser>
								</logicext:else>
							</logicext:if>
								</table>
							</td>
						</tr>
					</table>
				</TD>
				<TD width="1" class=datadivider>
					<IMG height=1	src="/assets/unmanaged/images/s.gif" width=1>
				</TD>
			</TR>
			<TR>
				<TD colSpan="3">
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
						
						
				<c:if test="${empty param.printFriendly}">
				<Br>
				<!-- SCO31 -->
				<logicext:if name="contractAccesses" property="cbcIndicator" op="equal" value="false">
				<logicext:then>
				<logicext:if name="theForm" property="webAccess" op="equal"	value="Yes">
				<logicext:then>
				<TABLE class=box style="CURSOR: pointer" onclick="showPermissions('<%=theIndex1%>')" cellSpacing=0 cellPadding=0	width=570 border=0>
					<TBODY>
						<TR>
							<TD width="544" bgcolor="#FFF9F2" class=colSpan=3><span class="style3">
<u>View&nbsp;${contractAccesses.contractName} permissions
							 </u></span><img src="/assets/unmanaged/images/layer_icon_down.gif" width="17"	height="9">
							</TD>
						</TR>
					</TBODY>
				</TABLE>
				</logicext:then>
				</logicext:if>
				</logicext:then>
				</logicext:if>
				<!-- after business converter SCO47 & SCO48 -->
				<logicext:if name="contractAccesses" property="cbcIndicator" op="equal" value="true">
				<logicext:then>
				<logicext:if name="theForm" property="webAccess" op="equal"	value="Yes">
				<logicext:then>
				<ps:isInternalUser name="userProfile" property="role">
				<ps:permissionAccess permissions="EXMN">
				<TABLE class=box style="CURSOR: pointer" onclick="showPermissions('<%=theIndex1%>')" cellSpacing=0 cellPadding=0	width=570 border=0>
					<TBODY>
						<TR>
							<TD width="544" bgcolor="#FFF9F2" class=colSpan=3><span class="style3">
<u>View&nbsp;${contractAccesses.contractName} permissions
							 </u></span> <img src="/assets/unmanaged/images/layer_icon_down.gif" width="17" height="9"></TD>
						</TR>
					</TBODY>
				</TABLE>
				</ps:permissionAccess>
				</ps:isInternalUser>
				</logicext:then>
				</logicext:if>
				</logicext:then>
				</logicext:if>
				<Br>
				</c:if>
				<logicext:if name="theForm" property="webAccess" op="equal"	value="Yes">
				<logicext:then>
				<!--permission start-->
				<logicext:if name="contractAccesses" property="cbcIndicator" op="equal" value="false">
				<logicext:or name="userProfile" property="internalUser" op="equal" value="true" />
				<logicext:then>
				<%@include file="permissionView.jsp" %>
				</logicext:then>
				</logicext:if>
				</logicext:then>
				</logicext:if>
				<!--permission end -->
	<Br>
</c:if>
</c:forEach>
