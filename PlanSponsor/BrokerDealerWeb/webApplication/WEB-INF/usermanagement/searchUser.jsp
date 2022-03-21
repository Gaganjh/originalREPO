<%@ page
	import="com.manulife.pension.bd.web.usermanagement.ExtUserSearchReportHelper"%>
<%@ page
	import="com.manulife.pension.service.security.bd.report.valueobject.BDExtUserSearchData"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.bd.web.util.BDWebCommonUtils"%>
<%@ page import="com.manulife.pension.service.security.BDUserRoleType"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import=" com.manulife.pension.bd.web.userprofile.BDUserProfile"%>
<%@ page
	import="com.manulife.pension.service.security.bd.report.valueobject.BDExtUserSearchDetails"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags/security" prefix="security"%>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<script type="text/javascript">
<!--
	function searchSubmit() {
		disableButtons();
		document.forms['searchExtUserForm'].firstName.value = document.forms['filter1'].firstName.value;
		document.forms['searchExtUserForm'].lastName.value = document.forms['filter1'].lastName.value;
		document.forms['searchExtUserForm'].contractNum.value = document.forms['filter1'].contractNum.value;
		document.forms['searchExtUserForm'].producerCode.value = document.forms['filter1'].producerCode.value;
		if(document.forms['filter1'].emailAddress) {
			document.forms['searchExtUserForm'].emailAddress.value = document.forms['filter1'].emailAddress.value;
		}
		document.forms['searchExtUserForm'].submit();
		return false;
	}

	function getSelection() {
		var selections = document.forms['searchExtUserForm'].elements['selection'];
		if (!selections)
			return null;
		var selectionsLength = selections.length;
		if (selectionsLength == undefined)
			if (selections.checked)
				return selections.value;
			else
				return null;
		for (var i = 0; i < selectionsLength; i++) {
			if (selections[i].checked) {
				return selections[i].value;
			}
		}
	}

	function manageUser(action) {
		disableButtons();
		var selection = getSelection();
		var roleCode;
		if (selection == null) {
			roleCode = null;
		} else {
			roleCode = document.forms['searchExtUserForm'].elements['role-'
					+ selection].value;
		}
		document.forms['userManagementDispatchForm'].action.value = action;
		document.forms['userManagementDispatchForm'].userProfileId.value = selection;
		document.forms['userManagementDispatchForm'].userRoleCode.value = roleCode;
		document.forms['userManagementDispatchForm'].submit();
		return false;
	}
	
	function createUser() {
		disableButtons();
		var selection = getSelection();
		var roleCode;
		var firstName;
		var lastName;
		var emailAddress;
		
		if (selection == null) {
			roleCode = null;
			firstName = null;
			lastName = null;
			emailAddress = null;
		} else {
			roleCode = document.forms['searchExtUserForm'].elements['role-'
					+ selection].value;
			firstName = document.forms['searchExtUserForm'].elements['firstName-'
			                                    					+ selection].value;
			lastName = document.forms['searchExtUserForm'].elements['lastName-'
			                                    					+ selection].value;
			emailAddress = document.forms['searchExtUserForm'].elements['emailAddress-'
			                                    					+ selection].value;
		}
		
		if(selection != null && (roleCode == null || roleCode == '')){
			document.forms['createRiaUserForm'].noRolePartyId.value = selection;
			document.forms['createRiaUserForm'].firstName.value = firstName;
			document.forms['createRiaUserForm'].lastName.value = lastName;
			document.forms['createRiaUserForm'].emailAddress.value = emailAddress;
		}
		document.forms['createRiaUserForm'].submit();
		return false;
	}

	function disableButtons() {
		disableButton(document.getElementById('searchAnchor'));
		disableButton(document.getElementById('manageAnchor1'));
		disableButton(document.getElementById('manageAnchor2'));
		disableButton(document.getElementById('resetPwdAnchor1'));
		disableButton(document.getElementById('resetPwdAnchor2'));
		disableButton(document.getElementById('advisorViewAnchor1'));
		disableButton(document.getElementById('advisorViewAnchor2'));
	}

	function disableButton(obj) {
		if (obj != undefined && obj != null) {
			obj.removeAttribute('href');
			obj.removeAttribute('onclick');
		}
	}
//-->
</script>

<style type="text/css">
form {
	display: inline;
}

@-moz-document url-prefix() {
		#createAnchor1{
			padding-left: 0px;
		}
		#createAnchor2{
			padding-left: 0px;
		}
}  
	
</style>





<% 

BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.SESSION_SCOPE);

BDExtUserSearchData theReport =(BDExtUserSearchData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>

<layout:pageHeader nameStyle="h2" />

<c:if test="<%=!BDWebCommonUtils.hasErrors(request)%>">
	<c:if test="${theReport.totalCount==0}">
		<c:choose>
			<c:when test="${searchExtUserForm.emptyForm}">
				<utils:info
					contentId="<%=BDContentConstants.USER_SEARCH_NO_CRITERIA%>" />
			</c:when>
			<c:otherwise>
				<utils:info
					contentId="<%=BDContentConstants.USER_SEARCH_NO_RESULT%>" />
			</c:otherwise>
		</c:choose>
	</c:if>
</c:if>

<report:formatMessages scope="session" />


<div class="page_section_subheader controls">
	<form name="filter1" class="page_section_filter form">
		<table width="700" border="0" cellspacing="0" cellpadding="0" style="display: inline-table;">
			<tr>
				<td>Last Name:</td>
				<td><input type="text" name="lastName"
					value="${searchExtUserForm.lastName}" size="30" maxlength="30" /></td>
				<td>Contract Number:</td>
				<td><input type="text" name="contractNum"
					value="${searchExtUserForm.contractNum}" size="12" maxlength="10" /></td>
				 <%-- <c:if test="${(userProfile.role.roleType.userRoleCode eq 'RUM')}"> --%>
					<td>Email Address:</td>
					<td><input type="text" name="emailAddress"
						value="${searchExtUserForm.emailAddress}" size="28" maxlength="70" /></td>
				<%-- </c:if> --%>
				<td></td>
			</tr>

			<tr>
				<td>First Name:</td>
				<td><input type="text" name="firstName"
					value="${searchExtUserForm.firstName}" size="30" maxlength="30" /></td>
				<td>Producer Code:</td>
				<td><input type="text" name="producerCode"
					value="${searchExtUserForm.producerCode}" size="12" maxlength="7" /></td>
				<%-- <c:choose>
					<c:when test="${userProfile.role.roleType.userRoleCode eq 'RUM'}"> --%>
						<div
							style="margin: 0px; margin-left: 5px; margin-top: 15px; float: right;" class="anchorClass">
							<a class="buttonheader" id="searchAnchor" name="search"
								href="#search" style="text-decoration: none"
								onclick="return searchSubmit()"><span>Submit</span></a>
							<security:isAuthorized url="/do/usermanagement/reports/inforce">
								<a class="buttonheader"
									href="/do/usermanagement/reports/inforce"><span>User
										Reports</span></a>
							</security:isAuthorized>
						</div>
					<%-- </c:when>
					<c:otherwise>
						<td><a class="buttonheader" id="searchAnchor" name="search"
							href="#search" style="text-decoration: none"
							onclick="return searchSubmit()"><span>Submit</span></a> <security:isAuthorized
								url="/do/usermanagement/reports/inforce">
								<a class="buttonheader"
									href="/do/usermanagement/reports/inforce"><span>User
										Reports</span></a>
							</security:isAuthorized></td>
					</c:otherwise>
				</c:choose> --%>
			</tr>
		</table>

	</form>
</div>
<bd:form action="/do/usermanagement/search" modelAttribute="searchExtUserForm" name="searchExtUserForm">
<input type="hidden" name="task" value="filter"/>
<form:hidden path="firstName"/>
<form:hidden path="lastName"/>
<form:hidden path="contractNum"/>
<form:hidden path="producerCode"/>
<form:hidden path="emailAddress"/>


	<div class="report_table">
		<div class="table_controls">
			<div class="button_regular">
				<a name="manage1" id="manageAnchor1" href="#manage1"
					onclick="return manageUser('manage')">Manage User </a>
			</div>
			<security:isAuthorized url="/do/usermanagement/resetPassword">
				<div class="button_regular">
					<a name="resetPwd1" id="resetPwdAnchor1" href="#resetPwd1"
						onclick="return manageUser('resetPassword')">Reset Password</a>
				</div>
			</security:isAuthorized>
			<div class="button_regular">
				<a name="advisorView1" id="advisorViewAnchor1" href="#advisorView1"
					onclick="return manageUser('mimic')">Advisor View</a>
			</div>
			<security:isAuthorized url="/do/createFirmRep/">
				<div class="button_regular">
					<a href="/do/createFirmRep/create?start=y">Create Firm Rep</a>
				</div>
			</security:isAuthorized>
			<security:isAuthorized url="/do/createRiaUser/create">
				<div class="button_regular">
					<a name="create1" id="createAnchor1" href="#create1"
					onclick="return createUser()">Create RIA Statement Viewer</a>
				</div>
			</security:isAuthorized>
		</div>

		<c:if test="${theReport.totalCount>0}">
			<div class="table_controls">
				<div class="table_action_buttons"></div>
				<div class="table_display_info">
					<strong> <report:recordCounter report="theReport"
							label="Users" />
					</strong>
				</div>
				<div class="table_pagination">
					<report:pageCounter formName="searchExtUserForm"  report="theReport" arrowColor="black" />
				</div>
			</div>
		</c:if>
		<table class="report_table_content">
			<thead>
				<tr>
					<th class="cbx" width="5%">&nbsp;</th>
					<th class="name" width="20%">Name</th>
					<th class="name" width="25%">User Role</th>
					<th class="val_str">Firm Name</th>
					<th class="val_str">Email Address</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${theReport.totalCount==0}">
						<tr>
							<td colspan="5">&nbsp;</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="item" items="${theReport.details}"
							varStatus="loopStatus">


							<c:choose>
								<c:when test="${loopStatus.index % 2 == 1}">
									<c:set var="trStyleClass" value="" />
								</c:when>
								<c:otherwise>
									<c:set var="trStyleClass" value="spec" />
								</c:otherwise>
							</c:choose>

							<tr class="${trStyleClass}">
								<td class="cbx"><input type="radio" name="selection"
									value="${item.userProfileId}" /></td>
								<td class="name"> 
									<input type="hidden"
									name="firstName-${item.userProfileId}"
									value="${item.firstName}">
									<input type="hidden"
									name="lastName-${item.userProfileId}"
									value="${item.lastName}"> ${item.firstName} ${item.lastName}
								</td>
								<c:set var="roleType" value="${item.roleType}"/>
								<%
									BDUserRoleType temp = (BDUserRoleType) pageContext.getAttribute("roleType");
									String roleCodeVal = ExtUserSearchReportHelper.getUserRoleDisplay(temp);
									pageContext.setAttribute("roleCodeVal", roleCodeVal);
								%>
								<td class="name"><input type="hidden"
									name="role-${item.userProfileId}"
									value="${item.roleType.userRoleCode}"> ${roleCodeVal}
								</td>
								<td class="val_str"><c:choose>
										<c:when test="${empty item.firmNameList}">
										
			 	         -
			 	       </c:when>
										<c:otherwise>
											<c:forEach var="firm" items="${item.firmNameList}"
												varStatus="loopStatus">
			 	             ${firm}
			 	             <c:if test="${not loopStatus.last}">
													<br>
												</c:if>
											</c:forEach>
										</c:otherwise>
									</c:choose> &nbsp;</td>
								<td class="name">
									<input type="hidden"
									name="emailAddress-${item.userProfileId}"
									value="${item.emailAddress}"> ${item.emailAddress}
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		<div class="table_controls">
			<div class="button_regular">
				<a name="manage2" id="manageAnchor2" href="#manage2"
					onclick="return manageUser('manage')">Manage User </a>
			</div>
			<security:isAuthorized url="/do/usermanagement/resetPassword">
				<div class="button_regular">
					<a name="resetPwd2" id="resetPwdAnchor2" href="#resetPwd2"
						onclick="return manageUser('resetPassword')">Reset Password</a>
				</div>
			</security:isAuthorized>
			<div class="button_regular">
				<a name="advisorView2" id="advisorViewAnchor2" href="#advisorView2"
					onclick="return manageUser('mimic')">Advisor View</a>
			</div>
			<security:isAuthorized url="/do/createFirmRep/">
				<div class="button_regular">
					<a href="/do/createFirmRep/create?start=y">Create Firm Rep</a>
				</div>
			</security:isAuthorized>
			<security:isAuthorized url="/do/createRiaUser/create">
				<div class="button_regular">
					<a name="create2" id="createAnchor2" href="#create2"
					onclick="return createUser()">Create RIA Statement Viewer</a>
				</div>
			</security:isAuthorized>
		</div>
		<c:if test="${theReport.totalCount>0}">
			<div class="table_controls">
				<div class="table_action_buttons"></div>
				<div class="table_display_info">
					<strong> <report:recordCounter report="theReport"
							label="Users" />
					</strong>
				</div>
				<div class="table_pagination">
					<report:pageCounter report="theReport" formName="searchExtUserForm" arrowColor="black" />
				</div>
			</div>
		</c:if>
		<div class="table_controls_footer"></div>
	</div>
	<!--.report_table-->
</bd:form>

<bd:form action="/do/usermanagement/dispatch" modelAttribute="userManagementDispatchForm" name="userManagementDispatchForm">
<form:hidden path="action"/>
<form:hidden path="userProfileId"/>
<form:hidden path="userRoleCode"/>
</bd:form>

<bd:form action="/do/createRiaUser/create?start=y" method="POST" modelAttribute="createRiaUserForm" name="createRiaUserForm">
<form:hidden path="noRolePartyId"/>
<form:hidden path="firstName"/>
<form:hidden path="lastName"/>
<form:hidden path="emailAddress"/>
</bd:form>

<layout:pageFooter />
