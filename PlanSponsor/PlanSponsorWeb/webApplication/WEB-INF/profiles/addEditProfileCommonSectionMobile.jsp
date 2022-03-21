<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ page import="com.manulife.pension.ps.web.profiles.AddEditUserForm"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
        

<!-- user profile common section starts scc6 scc7 CEX8  -->
<tr class="datacell1">
	
	<td width="200" valign="top"><strong><span
													id="label_firstName">First name</span></strong></td>
<td valign="top">${clientAddEditUserForm.firstName}</td>
</tr>
<tr class="datacell1">
	<td width="200"  valign="top"><strong><span
													id="label_lastName">Last name</span></strong></td>
													<td  valign="top">${clientAddEditUserForm.lastName}</td>
</tr>
<c:if test="${clientAddEditUserForm.email != null}">
											<tr class="datacell1">
												<td width="200" valign="top"><strong><span
													id="label_email">Primary Email&nbsp;</span></strong></td>
<td valign="top">${clientAddEditUserForm.email}</td>

											</tr>
											</c:if>
											
<c:if test="${not empty clientAddEditUserForm.secondaryEmail}">
											<tr class="datacell1">
												<td width="200" valign="top"><strong><span
													id="label_email">Secondary Email&nbsp;</span></strong></td>
<td valign="top">${clientAddEditUserForm.secondaryEmail}</td>

											</tr>
</c:if>

<logicext:if name="clientAddEditUserForm"	property="ssn.empty" op="equal" value="false">
											<logicext:then>
											<tr class="datacell1">
												<td  width="200" valign="top"><strong><span id="label_ssn">Social
												security number&nbsp;</span></strong></td>
												<c:if test="${userProfile.role.roleId ne 'ICC'}">
												
												<td ><render:fullmaskSSN property="clientAddEditUserForm.ssn"  /></td>
												</c:if>
												<c:if test="${userProfile.role.roleId eq 'ICC'}">
												<td ><render:ssn property="clientAddEditUserForm.ssn"/></td>
												</c:if>
											</tr>
											</logicext:then>
											</logicext:if>
 
<tr class="datacell1">
											
<tr class="datacell1">
	<td width="200" valign="top"><strong><span id="label_telephone">Telephone number&nbsp;</span></strong></td>
	<td valign="top">											
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
</tr>
														

<tr class="datacell1">
												<td width="200" valign="top"><strong><span
													id="label_fax">Fax number&nbsp;</span></strong></td>
												<td valign="top">
													<c:if test="${clientAddEditUserForm.faxNumber != ''}">
													<render:fax property="clientAddEditUserForm.faxNumber" />
</c:if>
												</td>
											</tr>

<tr class="datacell1">
	<td width="200" valign="top">
		<ps:label fieldId="mobileNumber" mandatory="false" >Mobile number</ps:label>
	</td>
	<td colspan="2" valign="top">${clientAddEditUserForm.mobileNumber}</td>
</tr>
<tr class="datacell1">
														<td  width="200" valign="top"><strong>Username</strong></td>
<td valign="top">${clientAddEditUserForm.userName}</td>
												     </tr>

<tr class="datacell1">
												<td width="200" valign="top"><strong>Password status </strong></td>
<td valign="top">${clientAddEditUserForm.passwordState}</td>
											</tr>
