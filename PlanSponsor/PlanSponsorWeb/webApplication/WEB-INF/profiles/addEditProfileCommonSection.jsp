<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>


<!-- user profile common section starts scc6 scc7 CEX8  -->
<tr class="datacell1">
	<td width="200" valign="top">
		<ps:label fieldId="firstName" mandatory="true">First name</ps:label>
	</td>
	<td colspan="2" valign="top">
<form:input path="firstName" maxlength="30" size="40" cssClass="inputField"/>
 		<ps:trackChanges name="clientAddEditUserForm"  property="firstName" />
	</td>
</tr>
<tr class="datacell1">
	<td width="200" valign="top">
		<ps:label fieldId="lastName" mandatory="true">Last name</ps:label>
	</td>
	<td colspan="2" valign="top">
<form:input path="lastName" maxlength="30" size="40" cssClass="inputField"/>
 		<ps:trackChanges name="clientAddEditUserForm"  property="lastName" />
	</td>
</tr>
<tr class="datacell1">
	<td width="200" valign="top">
		<logicext:if name="clientAddEditUserForm" property="webAccess" op="equal" value="Yes">
			<logicext:then>
				<ps:label fieldId="email" mandatory="true">Primary Email</ps:label>
			</logicext:then>
			<logicext:else>
				<ps:label fieldId="email" mandatory="false">Primary Email</ps:label>
			</logicext:else>
		</logicext:if>
	</td>
	<td colspan="2" valign="top">
<form:input path="email" size="40" cssClass="inputField"/>
 		<ps:trackChanges name="clientAddEditUserForm"  property="email" />
	</td>
</tr>
<c:if test="${not empty clientAddEditUserForm.secondaryEmail}">
<tr class="datacell1">
	<td width="200" valign="top"><strong>Secondary Email</strong></td>
<td colspan="2" valign="top">${clientAddEditUserForm.secondaryEmail}</td>
</tr>				
</c:if>

<tr class="datacell1">
	<td width="200" valign="top">
		<ps:label fieldId="telephoneNumber" mandatory="false">Telephone number</ps:label>
	</td>


<c:if test="${clientAddEditUserForm.profileStatus ne 'Registered'}">
<td colspan="2" valign="top">
<form:input path="telephoneNumber.areaCode" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField"/>
-<form:input path="telephoneNumber.phonePrefix" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField"/>
-<form:input path="telephoneNumber.phoneSuffix" maxlength="4" onkeyup="return autoTab(this, 4, event);" size="4" cssClass="inputField"/>
		<img src="/assets/unmanaged/images/s.gif" border="0" height="1" width="1">ext.
<form:input path="telephoneExtension" maxlength="8" onkeyup="return autoTab(this, 8, event);" size="8" cssClass="inputField"/>
		 <ps:trackChanges name ="clientAddEditUserForm" property="telephoneNumber.areaCode" />
		<ps:trackChanges name ="clientAddEditUserForm" property="telephoneNumber.phoneSuffix" />
		<ps:trackChanges name ="clientAddEditUserForm" property="telephoneNumber.phonePrefix" />
		<ps:trackChanges name ="clientAddEditUserForm" property="telephoneExtension" /> 
</td>
</c:if>

<c:if test="${clientAddEditUserForm.profileStatus eq 'Registered'}">
		<td colspan="2" valign="top">
	<c:if test="${clientAddEditUserForm.telephoneNumber != ''}">
		<render:phone property="clientAddEditUserForm.telephoneNumber"/>
	</c:if>
	<c:if test="${not empty clientAddEditUserForm.telephoneExtension}">
		<img src="/assets/unmanaged/images/s.gif" border="0" height="1"	width="1">ext.${clientAddEditUserForm.telephoneExtension}
	</c:if>
</td>
</c:if>

</tr>

<tr class="datacell1">
	<td width="200" valign="top">
		<ps:label fieldId="faxNumber" mandatory="false">Fax number</ps:label>
	</td>
	<td colspan="2" valign="top">
<form:input path="faxNumber.areaCode" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField"/>
-<form:input path="faxNumber.faxPrefix" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField"/>
-<form:input path="faxNumber.faxSuffix" maxlength="4" onkeyup="return autoTab(this, 4, event);" size="4" cssClass="inputField"/>
		<ps:trackChanges name ="clientAddEditUserForm" property="faxNumber.areaCode" />
		<ps:trackChanges name ="clientAddEditUserForm" property="faxNumber.faxPrefix" />
		<ps:trackChanges name ="clientAddEditUserForm" property="faxNumber.faxSuffix" /> 
	</td>
</tr>

<c:if test="${clientAddEditUserForm.clientUserAction ne 'addUser'}">	
<tr class="datacell1">
				<td valign="top"><ps:label fieldId="mobileNumber">Mobile number</ps:label>
				</td>
				<td colspan="2" valign="top">${clientAddEditUserForm.mobileNumber}</td>
			</tr>
</c:if>