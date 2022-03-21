<%@ page import="com.manulife.pension.platform.web.util.ViewStatementsUtility" %>
<%@ page
	import="com.manulife.pension.bd.web.bob.participant.ParticipantStatementsForm"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>


<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />

<report:formatMessages scope="request"/>

<script type="text/javascript">

function launchViewStatements(isParticipant)
{
	var vsWin;

	if ( isParticipant == "<%=BDConstants.YES%>")
	{	 
		if (document.participantStatementsForm.viewingPreference[0].checked) {
			vsWin = window.open("<%=ViewStatementsUtility.getUrlForParticipantStatements(BDConstants.DEFAULT_VIEWING_PREFERENCE)%>");
		}
		else {
			vsWin = window.open("<%=ViewStatementsUtility.getUrlForParticipantStatements(BDConstants.VIEWING_PREFERENCE)%>");
		}
	} 
	else {
		if (document.participantStatementsForm.viewingPreference[0].checked) {
			vsWin = window.open("<%=ViewStatementsUtility.getUrlForStatements(BDConstants.DEFAULT_VIEWING_PREFERENCE)%>");
		}
		else {
			vsWin = window.open("<%=ViewStatementsUtility.getUrlForStatements(BDConstants.VIEWING_PREFERENCE)%>");
		}
	}

	
	vsWin.focus();
}
</script>

<cssClass style="margin-bottom:0;"
         method="POST"
         action="/do/bob/participant/participantStatements/" modelAttribute="participantStatementsForm" name="participantStatementsForm">

	<c:if test="${not empty profileId}">
			<input type="hidden" name="profileId" value='<%= request.getParameter("profileId") %>' />
	</c:if>	
	
	<jsp:include page="/WEB-INF/global/displayContractInfo.jsp"/>
	
	<navigation:contractReportsTab />
	
	<div class="page_section_subheader controls">
	
	<h3><content:getAttribute id="layoutPageBean" attribute="subHeader"/></h3>
	
	</div>
	
	<div class="report_table">
	<table class="report_table_content"	summary="">
		<thead>
			<tr>
				<th colspan="2" class="name"><strong>
				<content:getAttribute id="layoutPageBean" attribute="body1Header"/>
				</strong>
				</th>
			 </tr>
		 </thead>
		 <tbody>
			<tr class="spec">
				<td width="5%" valign="top" class="date">
<form:radiobutton path="viewingPreference" value="4"/></td>
				<td width="95%" valign="top" class="name"><strong>PDF:</strong><br />
				<content:getAttribute beanName="layoutPageBean" attribute="body1"/>
				</td>
				
			</tr>
			<tr>
<td valign="top" class="date"><form:radiobutton path="viewingPreference" value="1" /></td>
				<td valign="top" class="name"><strong>Image file:</strong><Br />
				<content:getAttribute beanName="layoutPageBean" attribute="body2"/></td>
			</tr>
		</tbody>
	</table>
	</div>
	<!--.report_table-->
	
	<div class="button_regular"><a
		href="javascript:launchViewStatements('<%= request.getAttribute(BDConstants.IS_PARTICIPANT) %>');">Submit</a></div>
</bd:form>
<Br />
<Br />
<br />
<layout:pageFooter/>



