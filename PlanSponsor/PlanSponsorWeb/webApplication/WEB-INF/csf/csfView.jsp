<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfForm" %>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfConstants" %>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<ps:form method="POST" action="/do/contract/viewServiceFeatures/?action=edit" modelAttribute="csfForm" name="csfForm">

	<jsp:include flush="true" page="csfViewCMAKeys.jsp"/>		
	<jsp:include flush="true" page="csfPageContentHeader.jsp"></jsp:include> 
					 
	<table border="0" cellpadding="0" cellspacing="0" width="760">
	<tbody>
		<tr>
			<td>&nbsp;</td>
			<td valign="top" width="727">
				<table>
					<tr>
						<td colspan="2">
						<div class="messagesBox"><%-- Override max height if print friendly is on so we don't scroll --%>
						<ps:messages scope="session"
							maxHeight="${param.printFriendly ? '1000px' : '100px'}"
							suppressDuplicateMessages="true" showOnlyWarningContent="true" />
						</div>
						</td>
					</tr><tr><td colspan="2">&nbsp;</td></tr>
				</table>
				<table class="box" border="0" cellpadding="0" cellspacing="0" width="698">
					<tbody>
						<c:if test="${userProfile.internalUser}">
							<tr>
								<jsp:include flush="true" page="navigationTabBar.jsp">
									<jsp:param name="selectedTab" value="<%=CsfConstants.CSF_TAB%>"/>
								</jsp:include>
							</tr>
						</c:if>
						<tr>
						<td colspan="3">
							<c:if test="${userProfile.getCurrentContract().isDefinedBenefitContract() == true}">
								<table border="0" cellpadding="0" cellspacing="0" width="698">
									<tr>
										<td width="1" class="boxborder">
											<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
										</td>
										<td height="10" width="696" class="tablehead"><b><content:getAttribute
											beanName="planSponsorServicesSectionTitle" attribute="text" /></b></td>
										<td width="1" class="boxborder">
											<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
										</td>
									</tr>
									<tr>
										<td width="1" class="boxborder">
											<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
										</td>
										<td height="10"  class="tablesubhead"><b><content:getAttribute
											id="electronicTransactionsSubSectionTitle" attribute="title" /> </b></td>
										<td width="1" class="boxborder">
											<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
										</td>
									</tr>
									<tr class="datacell2">
										<td width="1" class="boxborder"><img
											src="/assets/unmanaged/images/spacer.gif" border="0"
											height="1" width="1"></td>
										<td width="696" height="10"><content:getAttribute
											id="consentSubsequentAmendments" attribute="text" /></td>
										<td width="1" class="boxborder"><img
											src="/assets/unmanaged/images/spacer.gif" border="0"
											height="1" width="1"></td>
									</tr>
								</table>
</c:if>

							<c:if test="${userProfile.getCurrentContract().isDefinedBenefitContract() == false}">
								<jsp:include flush="true" page="csfParticipantServicesView.jsp" /> 
								<jsp:include flush="true" page="csfPlanSponsorServicesView.jsp" />
</c:if>
						</td>
						</tr>
						<tr>
							<td>
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<tbody>
									<tr>
										<td class="boxborder"><img
											src="/assets/unmanaged/images/spacer.gif" border="0"
											height="1" width="1"></td>
									</tr>
								</tbody>
							</table>
							</td>
						</tr>
					</tbody>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:if test="${empty param.printFriendly}">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tbody>
					<tr>
						<td>&nbsp;</td>
						<td align="right">&nbsp;</td>
					</tr>
				</tbody>
				</table>
	
				<form:hidden path="button" id="submitAction" disabled="true" />
				<table border="0" cellpadding="0" cellspacing="0" width="698">
				<tbody>
					<tr valign="top">
						<td width="35" valign="top">&nbsp;</td>
						<c:if test="${csfForm.editable ==true}">
						<td width="200"><div align="center"></div></td>
						<td width="463">
							<div align="right">
								<input type="submit" class="button134" title="Edit service features" value="<%= CsfConstants.EDIT_BUTTON %>" property="button"/>
							</div>
						</td>
						</c:if>
					</tr>
				</tbody>
				</table>
				<br>
				</c:if>
				<img src="/assets/unmanaged/images/s.gif" height="1" width="20">
			</td>
		</tr>
	</tbody>
	</table>
</ps:form>
