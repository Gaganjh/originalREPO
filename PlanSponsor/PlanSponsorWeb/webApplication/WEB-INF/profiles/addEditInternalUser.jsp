<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%> 
 	<%@ page import="java.util.*"%>
       
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page
	import="com.manulife.pension.ps.web.profiles.AddEditUserForm"%>
<%@page import="com.manulife.pension.ps.web.profiles.AccessLevelHelper"%>
<%@page import="com.manulife.pension.service.security.BDUserRoleType"%>
<%@page import="com.manulife.pension.service.security.role.RelationshipManager"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>


<logicext:if name="layoutBean" property="param(task)" op="equal" value="add">
	<logicext:then>
<c:set var="actionPath" value="/do/profiles/addInternalUser/" />
	</logicext:then>
	<logicext:else>
<c:set var="actionPath" value="/do/profiles/editInternalUser/" />
	</logicext:else>
</logicext:if>
<%
ArrayList newInternal=(ArrayList)pageContext.getRequest().getAttribute(Constants.INTERNAL_ACCESS_LEVEL);
 ArrayList newEZK=(ArrayList)pageContext.getRequest().getAttribute(Constants.EZK_ACCESS_LEVEL);
ArrayList newBD=(ArrayList)pageContext.getRequest().getAttribute(Constants.BD_ACCESS_LEVEL);
String roleCode = BDUserRoleType.RVP.getUserRoleCode();
pageContext.setAttribute("roleCode", roleCode);
pageContext.setAttribute("newInternal", newInternal);
pageContext.setAttribute("newEZK", newEZK);
pageContext.setAttribute("newBD", newBD);
String relationshipManagerId=com.manulife.pension.service.security.role.RelationshipManager.ID;
pageContext.setAttribute("relationshipManagerId",relationshipManagerId,PageContext.PAGE_SCOPE);

%>
<script type="text/javascript" >
var submitted = false;

function doSubmit() {
	if (!submitted) {
		submitted = true;
		return true;
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}

function doCancelChanges(theForm) {
	if (!submitted) {
		submitted = doCancel(theForm);
		return submitted;
	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}

function toggleRVPNames(bdRolesDropDownBox) {
	var selectedRole = bdRolesDropDownBox.options[bdRolesDropDownBox.selectedIndex].value;
	if (selectedRole == "RVP") {
		document.getElementById("rvpNames").style.display = "";
	}
	else {
		document.getElementById("rvpNames").style.display = "none";
		document.forms['addEditUserForm'].rvpId.value="";
	}
}

function setRvpDisplayName(rvpSelect) {
	var displayName = rvpSelect.options[rvpSelect.selectedIndex].text;
	rvpSelect.form.rvpDisplayName.value = displayName;
}

function setRmDisplayName(rmSelect) {
	var displayName = rmSelect.options[rmSelect.selectedIndex].text;
	rmSelect.form.rmDisplayName.value = displayName;
}

function isFormChanged() {
	return changeTracker.hasChanged();
}

function showHidePermissions(value) {
	if (value == "<%=AccessLevelHelper.NO_ACCESS%>" || value == "PTC" ) {
		document.getElementById("perm1").style.display = "none";
		document.getElementById("perm2").style.display = "none";
		document.getElementById("perm3").style.display = "none";
		document.getElementById("perm4").style.display = "none";
	} else {
		document.getElementById("perm1").style.display = "block";
		document.getElementById("perm2").style.display = "block";
		document.getElementById("perm3").style.display = "block";
		document.getElementById("perm4").style.display = "block";
	}
	
	if (value == "PRM") {
		document.getElementById("rmNames").style.display = "";
	}
	else {
		document.getElementById("rmNames").style.display = "none";
		document.forms['addEditUserForm'].rvpId.value="";
	}
}

registerTrackChangesFunction(isFormChanged);
</script>

<ps:form cssClass="margin-bottom:0;" method="POST"
	action="${actionPath}" name="addEditUserForm" modelAttribute="addEditUserForm">

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<%-- error line --%>
			<td><strong><font color="#CC6600">*</font></strong> Required
				Information
				<p>
				<div id="errordivcs">
					<content:errors scope="session" />
				</div>
				</p></td>
		</tr>
		<tr>
			<td width="525"><img src="/assets/unmanaged/images/s.gif"
				width="525" height="1"></td>
		</tr>
		<tr>

			<%-- 1st cell is for profile details --%>
			<td>
				<%-- detail table starts --%>
				<table width="525" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td width=1><img height=1
							src="/assets/unmanaged/images/s.gif" width=1></td>
						<td width=519><img height=1
							src="/assets/unmanaged/images/s.gif" width=1></td>
						<td width=4><img height=1
							src="/assets/unmanaged/images/s.gif" width=4></td>
						<td width=1><img height=1
							src="/assets/unmanaged/images/s.gif" width=1></td>
					</tr>
					<tr class="tablehead">
						<td class="tableheadTD1" colspan="4"><b><content:getAttribute
									id="layoutPageBean" attribute="body1Header" /></b></td>
					</tr>
					<tr class="datacell1">
						<td class="databorder"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
						<td colspan="2" align="center">
							<%-- user profile starts  --%>
							<table class="addEditInternalUser" width="100%" border="0"
								cellspacing="0" cellpadding="0">
								<%-- include common stuff --%>
								<jsp:include page="addEditProfileSection.jsp" flush="true" />


								<%-- page specific stuff --%>
								<tr class=datacell1>
									<td><ps:label fieldId="employeeNumber" mandatory="true">Employee number</ps:label>
									</td>
<td><form:input path="employeeNumber" maxlength="9" size="15" cssClass="inputField"/></td>

									<ps:trackChanges name="addEditUserForm" property="employeeNumber" />
								</tr>

								<logicext:if name="layoutBean" property="param(task)" op="equal"
									value="add">
									<logicext:then>
										<tr class=datacell1>
											<td><ps:label fieldId="userName" mandatory="true">Username</ps:label>
											</td>
<td><form:input path="userName" maxlength="30" size="50" cssClass="inputField"/></td>

											<ps:trackChanges name="addEditUserForm" property="userName" />
										</tr>
									</logicext:then>
									<logicext:else>
										<tr class=datacell1>
											<td><strong>Username</strong></td>
<td>${e:forHtmlContent(addEditUserForm.userName)}</td>

<form:hidden path = "userName" /><%--  input - name="addEditUserForm" --%>
										</tr>

									</logicext:else>
								</logicext:if>


								<logicext:if name="layoutBean" property="param(task)" op="equal"
									value="edit">
									<logicext:then>
										<tr class=datacell1>
											<td><strong> Password status </strong></td>
											<td><ps:passwordState name="addEditUserForm"
													property="passwordState" /></td>
										</tr>
									</logicext:then>
								</logicext:if>

								<tr class=datacell1>
									<td valign="top"><strong>Plan sponsor access
											level</strong> <strong><font color="#CC6600"> *</font></strong></td>
<td valign="top"><form:select path="planSponsorSiteRole" onchange="showHidePermissions(this.value);">

<form:options items="${newInternal}" itemValue="value" itemLabel="label" />											
</form:select> <ps:trackChanges name="addEditUserForm" property="planSponsorSiteRole" /></td>
								</tr>
<input type="hidden" name="rmDisplayName"/>
								<c:set var="rmStyle" value="display:none" />
								
<c:if test="${addEditUserForm.planSponsorSiteRole == relationshipManagerId}">


									<c:set var="rmStyle" value="" />
</c:if>
								<tr id="rmNames" class=datacell1 style="${rmStyle}">
									<td valign="top"><ps:label fieldId="rmId" mandatory="true">Associated RM Block of Business</ps:label></td>
<td valign="top"><form:select path="rmId" onchange="setRmDisplayName(this)">

<form:options items="${addEditUserForm.rmList}" itemLabel="label" itemValue="value" />
</form:select> <ps:trackChanges name="addEditUserForm" property="rmId" /></td>

								</tr>

								<tr class=datacell1>
								<c:choose>
									<c:when test="${addEditUserForm.planSponsorSiteRole eq 'NA' or addEditUserForm.planSponsorSiteRole eq 'PTC'}">
										<td valign="top">
											<div id="perm1" style="display: none";>
												<strong>Produce Participant Fee Change Notice<font
												color="#CC6600"> *</font></strong>
											</div>
										</td>
										<td valign="top">
											<div id="perm2" style="display: none";>
<form:radiobutton path="accessIPIHypotheticalTool" value="Yes"/>Yes
<form:radiobutton path="accessIPIHypotheticalTool" value="No"/>No
											</div>
										</td>
									</c:when>
									<c:otherwise>
										<td valign="top">
											<div id="perm1" style="display: block";>
												<strong>Produce Participant Fee Change Notice<font
												color="#CC6600"> *</font></strong>
											</div>
										</td>
										<td valign="top">
											<div id="perm2" style="display: block";>
<form:radiobutton path="accessIPIHypotheticalTool" value="Yes"/>Yes
<form:radiobutton path="accessIPIHypotheticalTool" value="No"/>No
											</div>
										</td>
									</c:otherwise>
								</c:choose>
								</tr>
						
								
								<tr class=datacell1>
								<c:choose>
									<c:when test="${addEditUserForm.planSponsorSiteRole eq 'NA' or addEditUserForm.planSponsorSiteRole eq 'PTC'}">
										<td valign="top">
											<div id="perm3" style="display: none";>
												<strong>Access to 408b2 Supplemental Disclosure Auto
												Regeneration<font
												color="#CC6600"> *</font></strong>
											</div>
										</td>
										<td valign="top">
											<div id="perm4" style="display: none";>
<form:radiobutton path="access408DisclosureRegen" value="Yes"/>Yes
<form:radiobutton path="access408DisclosureRegen" value="No"/>No
											</div>
										</td>
									</c:when>
									<c:otherwise>
										<td valign="top">
											<div id="perm3" style="display: block";>
												<strong>Access to 408b2 Supplemental Disclosure Auto
												Regeneration<font
												color="#CC6600"> *</font></strong>
											</div>
										</td>
										<td valign="top">
											<div id="perm4" style="display: block";>
<form:radiobutton path="access408DisclosureRegen" value="Yes"/>Yes
<form:radiobutton path="access408DisclosureRegen" value="No"/>No
											</div>
										</td>
									</c:otherwise>
								</c:choose>
								</tr>
								
								
								<tr class=datacell1>
									<td valign="top"><strong>Participant access level</strong>
										<strong><font color="#CC6600"> *</font></strong></td>
<td valign="top"><form:select path="participantSiteRole">
<form:options items="${newEZK}" itemValue="value" itemLabel="label"/>
</form:select> <ps:trackChanges name="addEditUserForm" property="participantSiteRole" /></td>
								</tr>

								<!-- BD Start -->

								<tr class=datacell1>
									<td valign="top"><strong>Broker Dealer access
											level</strong> <strong><font color="#CC6600"> *</font></strong></td>
<td valign="top"> <form:select path="brokerDealerSiteRole" onchange="toggleRVPNames(this)">
<form:options items="${newBD}" itemValue="value" itemLabel="label"/>										
</form:select> <ps:trackChanges name="addEditUserForm" property="brokerDealerSiteRole" /></td>
								</tr>

<input type="hidden" name="rvpDisplayName"/>

								<c:set var="rvpStyle" value="display:none" />
								
<c:if test="${addEditUserForm.brokerDealerSiteRole == roleCode}">


									<c:set var="rvpStyle" value="" />
</c:if>
								<tr id="rvpNames" class=datacell1 style="${rvpStyle}">
									<td valign="top"><ps:label fieldId="rvpId"
											mandatory="true">RVP:</ps:label></td>
<td valign="top"><form:select path="rvpId" onchange="setRvpDisplayName(this)"> 

<form:options items="${addEditUserForm.rvpList}" itemValue="value" itemLabel="label"/>
</form:select> <ps:trackChanges name="addEditUserForm" property="rvpId" /></td>
								</tr>
								<logicext:if name="layoutBean" property="param(task)" op="equal"
									value="edit">
									<logicext:then>
										<tr class=datacell1>
											<td valign="top"><strong>License verified</strong></td>
<td valign="top">${addEditUserForm.bdLicenceVerified}</td>

										</tr>
									</logicext:then>
								</logicext:if>
								<!-- BD End -->

							</table>
						</td>
						<td class="databorder"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
					<tr class=whiteborder>
						<td class=databorder><img height=4
							src="/assets/unmanaged/images/s.gif" width=1></td>
						<td class=whiteborder><img height=1
							src="/assets/unmanaged/images/s.gif" width=1></td>
						<td class=whiteborder colSpan=2 rowSpan=2><img height=5
							src="/assets/unmanaged/images/box_lr_corner.gif" width=5></td>
					</tr>
					<tr>
						<td class="databorder" colspan="2"><img
							src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
					</tr>
				</table>
				<p>
				<table width="525" border="0" cellspacing="0" cellpadding="0">
					<tr>
<td align="right"><input type="submit" class="button100Lg" onclick="return doCancelChanges(addEditUserForm);" name="action" value="cancel"/> <%-- property="actionLabel" --%>



&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="submit" class="button100Lg" onclick="return doSubmit();" name="action" value="save"/> <%-- property="actionLabel" --%> <script



								type="text/javascript">
			var onenter = new OnEnterSubmit('action', 'save');
									onenter.install();
								</script></td>
					</tr>
				</table>
				</p>
			</td>

		</tr>
	</table>
</ps:form>

<script type="text/javascript" >
	document.forms["addEditUserForm"].firstName.focus();
</script>




