<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<% String technicalDifficulties =(String)request.getAttribute(Constants.TECHNICAL_DIFFICULTIES);
pageContext.setAttribute("technicalDifficulties",technicalDifficulties,PageContext.PAGE_SCOPE);
%>
<c:if test="${empty  technicalDifficulties}">
	<c:if test="${empty param.printFriendly }" >
		<script language="javascript" type="text/javascript"
			src="/assets/unmanaged/javascript/calendar.js"></script>

<script type="text/javascript" >
	function doSubmit(action) {
		document.getElementById("action").value = action;
		document.tpaStandardFeeScheduleForm.submit();
	}
	
	function doBlockScheduleReport() {
        var reportURL = new URL("/do/tpa/blockScheduleReport/");
		reportURL.setParameter("selectedTpaId", document.getElementById("selectedTpaId").value);
		location.href = reportURL.encodeURL();
	 }
</script>
	</c:if>
</c:if>
<%-- technical difficulties --%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">

	<tr>
		<td><img src="/assets/unmanaged/images/s.gif" width="30"
			height="1"></td>
		<td width="100%" valign="top">
		<div id="messagesBox" class="messagesBox"><%-- Override max height if print friendly is on so we don't scroll --%>
		<ps:messages scope="session"
			maxHeight="${param.printFriendly ? '1000px' : '100px'}"
			suppressDuplicateMessages="true" /><br></div>
		
		<c:if test="${empty param.printFriendly}">
<c:if test="${tpaStandardFeeScheduleForm.showChangeHistoryLink ==true}">

				<a href="javascript:doSubmit('changeHistory');">Change
				History </a>
</c:if>
<%--  input - name="tpaStandardFeeScheduleForm" --%>
		</c:if> 
		
		<ps:form
			action="/do/viewTpaStandardFeeSchedule/" modelAttribute="tpaStandardFeeScheduleForm"  name="tpaStandardFeeScheduleForm">
					
			<%-- Start Standard Fee Schedule information --%>
<form:hidden  path="selectedTpaFirmId"  id="selectedTpaId" />
<input type="hidden" name="action" id="action" /><%--  input - name="tpaStandardFeeScheduleForm" --%>
			<TABLE border=0 cellSpacing=0 cellPadding=0 width=700>
				<TBODY>
					<TR>
						<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
							height=1></TD>
						<TD class=actions><IMG src="/assets/unmanaged/images/s.gif"
							width=203 height=1></TD>
						
						<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
							height=1></TD>
						<TD class=type><IMG src="/assets/unmanaged/images/s.gif"
							width=145 height=1></TD>
						<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
							height=1></TD>
						<TD class=paymentTotal><IMG
							src="/assets/unmanaged/images/s.gif" width=200 height=1></TD>
						<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
							height=1></TD>
						<TD><IMG src="/assets/unmanaged/images/s.gif" width=180
							height=1></TD>
						<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
							height=1></TD>
						<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
							height=1></TD>
					</TR>
					<TR class=tablehead>
						<TD class=tableheadTD1 height=25 vAlign=center colSpan=9><b>Individual
						Expenses</b></TD>
						<TD class=databorder><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
					</TR>
					<TR class=datacell1>
						<TD class=databorder height=25><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
						<TD><b>&nbsp;Last updated</b></TD>
						<TD class=dataheaddivider height=25><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
<TD valign=middle align=left height=25>${tpaStandardFeeScheduleForm.lastUpdatedDate}</TD>


						<TD class=dataheaddivider height=25><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
						<TD vAlign=middle height=25 align=left><b>Last updated by</b></TD>
						<TD class=dataheaddivider height=25><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
<TD vAlign=middle height=25 align=left colspan="2">${tpaStandardFeeScheduleForm.lastUpdatedUserName}</TD>


						<TD class=databorder height=25><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
					</tr>
					<TR class=tablesubhead>
						<TD class=databorder height=25><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
						<TD><b>Fee type</b></TD>
						
						<TD class=dataheaddivider height=25><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
						<TD valign=middle align=right height=25><b>Value</b></TD>
						<TD class=dataheaddivider height=25><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
						<TD vAlign=middle height=25 align=left colspan="4"><b>Special
						notes</b></TD>
						<TD class=databorder height=25><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
					</TR>
							
<c:if test="${empty tpaStandardFeeScheduleForm.actualFeeItemsList}">

						<content:contentBean
							contentId="<%=ContentConstants.VIEW_STANDARD_FEE_SCHEDULE_PAGE_NO_STANDARD_SCHEDULE_EXISTS%>"
							type="<%=ContentConstants.TYPE_MESSAGE%>"
						id="NoStandardFeeScheduleExists" />
						<tr class="datacell1">
						    <TD class=databorder height=25><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							<td colspan="8"><content:getAttribute
								id="NoStandardFeeScheduleExists" attribute="text" /></td>
							<TD class=databorder height=25><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>	
						</tr>
</c:if>
			
<c:if test="${not empty tpaStandardFeeScheduleForm.actualFeeItemsList}">

<c:forEach items="${tpaStandardFeeScheduleForm.actualFeeItemsList}" var="details" varStatus="theIndex" >




							
								<c:choose>
							    <c:when test="${theIndex.index % 2 == 0}">
							     <tr class="datacell2"> 
							    </c:when>
							    <c:otherwise>
							      <tr class="datacell1">
							     </c:otherwise>
							    </c:choose> 
									
								<TD class=databorder ><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
<TD vAlign=top style="white-space: wrap;"><B>${details.feeDescription}</B></TD>

								
								<TD class=datadivider ><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
<TD valign=middle align=right >${details.displayFormattedAmount}</TD>

								<TD class=datadivider ><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
<TD vAlign=middle  align=left colspan="4" style="word-break:break-all;">${details.notes}</TD>

								<TD class=databorder ><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>

								</TR>
</c:forEach>
</c:if>
					<TR>
						<TD class=databorder colSpan=10><IMG
							src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
					</TR>
					<TR>
						<TD colSpan=5><IMG src="/assets/unmanaged/images/s.gif"
							width=1 height=1></TD>
						<TD class=dark_grey_color vAlign=center colSpan=4></TD>
						<TD class=dark_grey_color colSpan=4 align=right></TD>
						<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
							height=1></TD>
					</TR>
				</TBODY>
			</TABLE>
			<br>

			<TABLE border=0 cellSpacing=0 cellPadding=0 width=730>
				<tr>

					<td width="410" align="right"></td>
					<c:if test="${empty param.printFriendly }" >
<c:if test="${tpaStandardFeeScheduleForm.showBackButton ==false}">

<td width="160" align="right"><input type="button" onclick="return doSubmit('back');" name="button" class="button134" value="back"/></td>


</c:if>
<c:if test="${tpaStandardFeeScheduleForm.tpaUserManager ==true}">

<td width="160" align="right"><input type="button" onclick="return doSubmit('edit');" name="button" class="button134" value="edit"/></td>


</c:if>
<c:if test="${tpaStandardFeeScheduleForm.otherThanInternalUserPresent ==true}">

<td width="160" align="right"><input type="button" onclick="return doSubmit('customizeContract');" name="button" class="button134" value="customize contract"/></td>



</c:if>
					</c:if>
				</tr>
			</table>
			<br>
			<c:if test="${not empty param.printFriendly}">
		    <content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure"/>
		    <table  border=0 cellSpacing=0 cellPadding=0 width=730>
		    <tr>
		    <td width="100%"><p><content:pageFooter beanName="layoutPageBean" /></p></td>
		    </tr>
		    <tr>
		    <td width="100%">&nbsp;</td>
		    </tr>
		    <tr>
		    <td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		    </tr>
		    </table>
		    </c:if>
		</ps:form> <%-- End Standard Fee Schedule information --%> <%-- technical difficulties --%>
		</td>
	</tr>
</table>
<c:if test="${empty param.printFriendly}">
		<table cellpadding="0" cellspacing="0" border="0" width="730" class="fixedTable" height="">
			<tr>
				<td width="30">&nbsp;</td>
				<td width="700"><content:pageFooter beanName="layoutPageBean" /></td>
			</tr>
		</table>
</c:if>
