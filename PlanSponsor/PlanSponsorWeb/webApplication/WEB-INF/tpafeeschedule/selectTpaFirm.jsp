<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<script type="text/javascript">
		
		function doStandardize(tpaFirmId) {
			document.getElementById("action").value = 'standardize';
			document.getElementById("selectedTpaFirmId").value = tpaFirmId;
			return
		}
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="100%" valign="top">
			<%-- error line --%> &nbsp;
			<div id="messagesBox" class="messagesBox">
				<%-- Override max height if print friendly is on so we don't scroll --%>
				<ps:messages scope="session"
					maxHeight="${param.printFriendly ? '1000px' : '100px'}"
					suppressDuplicateMessages="true" />
				<br>
			</div> &nbsp; <br>
		<br> <ps:form action="/do/feeSchedule/selectTpaFirm/" modelAttribute="selectTpaFirmForm">
				<form:hidden path="selectedTpaFirmId" id="selectedTpaFirmId" />
				<%--  input - name="selectTpaFirmForm" --%>
				<form:hidden path="action" id="action" />
				<%-- input - name="selectTpaFirmForm" --%>
			</ps:form>
			<TABLE border=0 cellSpacing=0 cellPadding=0 width="403">
				<TBODY>
					<TR>
						<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
							height=1></TD>
						<TD class=actions><IMG src="/assets/unmanaged/images/s.gif"
							width="200" height=1></TD>
						<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
							height=1></TD>
						<TD class=submissionDate><IMG
							src="/assets/unmanaged/images/s.gif" width="200" height=1></TD>
						<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
							height=1></TD>
					</TR>
					<TR class=tablehead>
						<TD class=tableheadTD1 height=25 align="left" vAlign=center
							colSpan=5>&nbsp;<b><content:getAttribute
									id="layoutPageBean" attribute="body1Header" /> </b></TD>
					</TR>
					<TR class=tablesubhead>
						<TD class=databorder height=25><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
						<TD><b>&nbsp;TPA firm ID</b></TD>
						<TD class=dataheaddivider height=25><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
						<TD valign=middle align=left height=25><b>&nbsp;TPA firm
								name</b></TD>
						<TD class=databorder height=25><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
					</TR>
					<c:if test="${not empty tpaFirmMap}">
						<c:forEach items="${tpaFirmMap}" var="theItem"
							varStatus="theIndex">
							<c:choose>
								<c:when test="${theIndex.index % 2 == 0}">
									<tr class="datacell2 ">
								</c:when>
								<c:otherwise>
									<tr class="datacell1 ">
								</c:otherwise>
							</c:choose>
							<TD class=databorder><IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD class="payrollDate" align=left><a
								href="/do/feeSchedule/selectTpaFirm/?action=standardize&selectedTpaFirmId=${theItem.key}">${theItem.key}
							</a></TD>
							<TD class="datadivider" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<TD class="payrollDate" align=left><a
								href="/do/feeSchedule/selectTpaFirm/?action=standardize&selectedTpaFirmId=${theItem.key}">${theItem.value}</a></TD>
							<TD class=databorder><IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							</TR>
						</c:forEach>
					</c:if>
					<TR>
						<TD class=databorder colSpan="5"><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
					</TR>
				</TBODY>
			</TABLE>
			<br />
		</td>
	</tr>
</table>
<c:if test="${empty param.printFriendly}">
	<table cellpadding="0" cellspacing="0" border="0" width="730"
		class="fixedTable" height="">
		<tr>
			<td width="700"><content:pageFooter beanName="layoutPageBean" /></td>
		</tr>
	</table>
</c:if>
<c:if test="${not empty param.printFriendly }">
	<content:contentBean
		contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute id="globalDisclosure"
					attribute="text" /></td>
		</tr>
	</table>
</c:if>
