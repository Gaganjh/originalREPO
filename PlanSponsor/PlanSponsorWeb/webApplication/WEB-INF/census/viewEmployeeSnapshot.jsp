<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.content.valueobject.ContentType"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.service.security.role.RelationshipManager"%>
<%@ page import="com.manulife.pension.ps.web.census.CensusConstants" %>
<%@ page import="com.manulife.pension.ps.web.census.EmployeeSnapshotForm" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<style type="text/css">
DIV.scroll {
	OVERFLOW: auto;
	WIDTH: auto;
	BORDER-TOP-STYLE: none;
	BORDER-RIGHT-STYLE: none;
	BORDER-LEFT-STYLE: none;
	HEIGHT: 75px;
	BACKGROUND-COLOR: #fff;
	BORDER-BOTTOM-STYLE: none;
	padding: 8px;
	visibility: visible;
}
</style>

<SCRIPT type=text/javascript>
<!--
function doPrint()
{
  var printURL;
  urlquery=location.href.split("?");
      printURL = location.href+"&printFriendly=true";
  if (urlquery.length > 1) {
      printURL = location.href+"&printFriendly=true";
  } else {
printURL = location.href+"?profileId=${employee.employeeDetailVO.profileId}&printFriendly=true";
  }
  window.open(printURL,"","width=720,height=480,resizable,toolbar,scrollbars,");
}

function openWin(url) {
	options="toolbar=1,status=1,menubar=1,scrollbars=1,resizable=1,width=800,height=450,left=10,top=10";
	newwindow=window.open(url, "general", options);
	if (navigator.appName=="Netscape") {
		newwindow.focus();
	}
}

function doSubmit(action) {
    doFormSubmit('employeeForm', action);
}

function toggleSection(sectionId, statusId, indId, icon) {
    toggleFormSection('employeeForm', sectionId, statusId, indId, icon);
}

function expandAll(expand) {
    expandFormAllSection('employeeForm', expand);
}

function openVestingInformation() {
window.location="/do/census/viewVestingInformation/?profileId=${e:forJavaScriptBlock(employeeForm.profileId)}&source=<%=CensusConstants.EMPLOYEE_SNAPSHOT_PAGE%>";
    return true;
}

//-->
</SCRIPT>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_VESTING_INFO_LINK%>" 
					type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="vestingInfoLink"/>

<content:contentBean contentId="<%=ContentConstants.OBDS_LINK_IN_EMPLOYEE_SNAPSHOT_PAGE%>" 
					type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="beneficiaryInfoLink"/>
				
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<%
EmployeeSnapshotForm employeeForm = (EmployeeSnapshotForm)session.getAttribute("employeeForm");
pageContext.setAttribute("employeeForm",employeeForm,PageContext.PAGE_SCOPE);
%>

	
<c:set var="moneyTypes" value="${employeeForm.moneyTypes}" scope="request" /> 

<%-- <jsp:useBean id="employeeForm" class="com.manulife.pension.ps.web.census.EmployeeSnapshotForm"/> --%>

<jsp:useBean id="securityProfile" class="com.manulife.pension.ps.web.census.util.EmployeeSnapshotSecurityProfile" scope="request">
	<jsp:setProperty name="securityProfile" property="userProfile" value="<%=userProfile %>"/>
</jsp:useBean>

		<ps:form action="/do/census/viewEmployeeSnapshot/" name="employeeForm" modelAttribute="employeeForm">



	



<form:hidden path="source"/>
<input type="hidden" name="fromView" value="true"/>
<input type="hidden" name="action"/>
<input type="hidden" name="onlineBeneficiaryLinkDisplayed"/>
		
        <c:if test="${not empty requestScope[Constants.TPA_CONTRACT_ID_KEY]}">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${Constants.TPA_CONTRACT_NAME_KEY} <%-- scope="request" --%> | Contract: ${Constants.TPA_CONTRACT_ID_KEY} <%-- scope="request" --%>
           <br><br>
</c:if>
        <content:errors scope="session"/>
		<table cellSpacing=0 cellPadding=0 width=760 border=0>
			<tbody>
				<tr>
					<td>
					<P>
					<table cellSpacing=0 cellPadding=0 width=500 border=0>
						<tbody>
							<tr>
								<td width=15>
								 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=30 border=0></td>
								<td vAlign=top width=609>

								<jsp:include flush="true" page="confirmationToDo.jsp"/>
								
								<BR>
				      			<jsp:include flush="true" page="employeeSnapshotErrors.jsp"/>
				      			<c:if test="${param.printFriendly != true}">
								 <logicext:if name="employeeForm"
							      		property="onlineBeneficiaryLinkDisplayed" op="equal" value="true">
									<logicext:and name="employee" property="participant"
									op="equal" value="true"/>
										 <logicext:then>
<a href="/do/census/beneficiary/viewBeneficiaryInformation/?profileId=${employee.employeeDetailVO.profileId}">
											<content:getAttribute beanName="beneficiaryInfoLink"
											attribute="text" /> </a>
										<br></br>
								       </logicext:then> 
								</logicext:if>
								</c:if>
								 <jsp:include flush="true" page="viewBasicSection.jsp"></jsp:include> 
								<BR>
								<BR>
								 <jsp:include flush="true" page="viewEmploymentSection.jsp"></jsp:include> 
								<BR>
								<BR>
								 <jsp:include flush="true" page="viewContactSection.jsp"></jsp:include> 
								<BR>
								<BR>
								<jsp:include flush="true" page="viewParticipationSection.jsp"></jsp:include> 
<c:if test="${employeeForm.showVesting eq true}">
								<BR><BR>
								
								
<c:set var="showVestingLink" value="${employeeForm.showVestingLink}"/>
<c:set var="vestingCollected" value="${employeeForm.vestingCollected}"/>
<c:set var="expandVesting" value="${employeeForm.expandVesting}"/>
					<jsp:include flush="true" page="viewVestingSection.jsp">
								   <jsp:param name="showVestingLink" value="${showVestingLink}"/>
								   <jsp:param name="expandVesting" value="${expandVesting}"/>
								   <jsp:param name="vestingCollected" value="${vestingCollected}"/>
								</jsp:include>
								
</c:if>
								</td>
							</tr>
						</tbody>
					</table>
					<BR>
					<c:if test="${param.printFriendly!=true}">
					<table width=615>
						<tbody>
							<tr>
								<td><img height=1 src="/assets/unmanaged/images/spacer.gif" width=30 border=0></td>
								<td>
									<table cellSpacing=0 cellPadding=0 width=600 border=0>
										<tbody>
											<tr vAlign=top>
												<td align="left" width="278">
												<input type="button" name="button1" value="back" class="button134" onclick="doSubmit('back')"/>
												</td>
												<td align="right" width=206>
												   <c:if test="${securityProfile.updateCensusData && !securityProfile.contractDI}">
												    <input type="button" name="button2" value="edit" class="button134"  onclick="doSubmit('edit')"/>
												   </c:if>
												   &nbsp;
												</td>
												<td width=116>&nbsp;</td>
											</tr>
										</tbody>
									</table>
								</td>
							</tr>
						</tbody>
					</table>
					</c:if>
					</td>
				</tr>
			</tbody>
		</table>
		</ps:form>
		<!-- footer table -->
		<BR>
		<table height=25 cellSpacing=0 cellPadding=0 width=760 border=0>
		  <tr>
		  <td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
		              <tr>
					    <td width="30" valign="top">
					        <img src="/assets/unmanaged/images/s.gif" width="30" height="1">
					    </td>
						<td>
						<br>
							<p><content:pageFooter beanName="layoutPageBean"/></p>
		 					<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
		 					<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
		 				</td>
		 			</tr>
				</table>
		    </td>
		    <td width="3%"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
		    <td width="15%" ><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
		  </tr>
		</table>
 <script type="text/javascript" src="/assets/unmanaged/javascript/oldstyle_tooltip.js"></script>

  <c:if test="${not empty param.printFriendly}">
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        id="globalDisclosure"/>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
  </c:if> 
