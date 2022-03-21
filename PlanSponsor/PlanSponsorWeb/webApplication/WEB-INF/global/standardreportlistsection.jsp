<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract" %>
<%@ page import="com.manulife.pension.service.security.role.UserRole" %>
<%@ page import="com.manulife.pension.service.security.role.PayrollAdministrator" %>

<%
UserProfile userProfile = (UserProfile) session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
pageContext.setAttribute("isPA", new Boolean(userProfile.getPrincipal().getRole() instanceof PayrollAdministrator));
%>

<c:if test="${isPA != true}">
	<c:if test="${not empty userProfile.currentContract }">
		<c:if test="${userProfile.welcomePageAccessOnly !=true}">

			<%-- Start of Quick Report --%>

			<content:contentBean
				contentId="<%=ContentConstants.PS_QUICK_REPORTS_SECTION%>"
				type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
				id="Quick_Reports_Section" />
			<content:contentBean
				contentId="<%=ContentConstants.PS_QUICK_REPORTS_LIST_OF_REPORTS%>"
				type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
				id="Quick_Reports_List_of_Reports" />

			<table width="180" border="0" cellspacing="0" cellpadding="0"
				class="box">
				<tr>
					<td width="1"><img src="/assets/unmanaged/images/s.gif"
						width="1" height="1"></td>
					<td width="178"><img src="/assets/unmanaged/images/s.gif"
						width="178" height="1"></td>
					<td width="1"><img src="/assets/unmanaged/images/s.gif"
						width="1" height="1"></td>
				</tr>
				<tr class="tablehead">

					<td colspan="3" class="tableheadTD1"><b> <%-- CMA managed--%>
							<content:getAttribute id="Quick_Reports_Section"
								attribute="title" /></b></td>
				</tr>
				<tr>
					<td class="boxborder"><img
						src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					<td class="boxbody" width="178">
						<form name="QuickLinksForm" style="margin-bottom: 0;">
							<c:if
								test="${not empty layoutBean.getParam('showQuickReportsDescription')}">
								<c:if
									test="${layoutBean.getParam('showQuickReportsDescription') == 'Y'}">
									<%-- CMA managed--%>
									<content:getAttribute id="Quick_Reports_Section"
										attribute="text" />
									<br>
									<img src="/assets/unmanaged/images/s.gif" width="1" height="1"
										vspace="1" />
									<br>
								</c:if>
							</c:if>
							<c:if test="${not empty userProfile.currentContract }">
								<quickreports:reportList id="rlist" />
							</c:if>
						</form>
					</td>
					<td class="boxborder"><img
						src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				</tr>
				<tr>
					<td colspan="3">
						<table width="180" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="boxborder" width="1"><img
									src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
								<td><img src="/assets/unmanaged/images/s.gif" width="1"
									height="4"></td>
								<td rowspan="2" width="5"><img
									src="/assets/unmanaged/images/box_lr_corner.gif" width="5"
									height="5"></td>
							</tr>
							<tr>
								<td class="boxborder"><img
									src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								<td class="boxborder"><img
									src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<img src="/assets/unmanaged/images/s.gif" width="1" height="5"><br>
			<%-- End of Quick Report --%>

		</c:if>
	</c:if>
</c:if>