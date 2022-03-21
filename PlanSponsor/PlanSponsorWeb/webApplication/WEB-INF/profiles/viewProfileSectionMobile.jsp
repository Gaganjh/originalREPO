<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>


<% UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<c:set var="userProfile" value="${sessionScope.userProfile}" /> <%-- scope="session" type="com.manulife.pension.ps.web.controller.UserProfile" --%>



 			<tr class="datacell1">
 				<td width="46%" valign="top">
 				  <ps:label fieldId="firstName">First name</ps:label>
 				</td>
   				<td  valign="top">
${addEditUserForm.firstName}
           		</td>
 			</tr>
         	<tr class="datacell1">
        		<td width="46%" valign="top">
        		  <ps:label fieldId="lastName">Last name</ps:label>
        		</td>
            	<td valign="top">
${addEditUserForm.lastName}
            	</td>
          	</tr>
          	<tr class="datacell1">
           		<td valign="top">
        		  <ps:label fieldId="email">Primary Email</ps:label>
           		</td>
               	<td valign="top">
${addEditUserForm.email}
               	</td>
          	</tr>
<c:if test="${not empty addEditUserForm.secondaryEmail}">
          	<tr class="datacell1">
           		<td valign="top">
        		  <ps:label fieldId="secondaryEmail">Secondary Email</ps:label>
           		</td>
               	<td valign="top">
${addEditUserForm.secondaryEmail}
               	</td>
          	</tr>
</c:if>
          	<tr class="datacell1">
	<td width="200" valign="top"><strong><span id="label_telephone">Telephone number&nbsp;</span></strong></td>
	<td valign="top">											
<form:input path="phone.areaCode" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField" value="${addEditUserForm.phone.areaCode}"/> - 
<form:input path="phone.phonePrefix" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField" value="${addEditUserForm.phone.phonePrefix}"/> - 
<form:input path="phone.phoneSuffix" maxlength="4" onkeyup="return autoTab(this, 4, event);" size="4" cssClass="inputField" value="${addEditUserForm.phone.phoneSuffix}"/> ext
 <form:input path="ext" maxlength="8" onkeyup="return autoTab(this, 8, event);" size="8" cssClass="inputField" value="${addEditUserForm.ext}"/> 
 <ps:trackChanges name="addEditUserForm" property="phone.areaCode" /> 
 <ps:trackChanges name="addEditUserForm" property="phone.phonePrefix" /> 
 <ps:trackChanges name="addEditUserForm" property="phone.phoneSuffix" /> 
 <ps:trackChanges name="addEditUserForm" property="ext" />
	</td>
</tr>

          	<tr class="datacell1">
           		<td valign="top">
        		  <ps:label fieldId="fax">Fax number</ps:label>
           		</td>
               	<td valign="top">	
               		<c:if test="${addEditUserForm.fax != ''}">               	
               			<render:fax property="addEditUserForm.fax"/>
</c:if>
               	</td>
          	</tr>  

<tr class="datacell1">
	<td valign="top"><ps:label fieldId="mobile">Mobile number</ps:label>
	</td>
	<td valign="top">${addEditUserForm.mobile}</td>
</tr>


<c:if test="${layoutBean.getParam('task') == 'view'}">
          	 <ps:isTpa name="userProfile" property="role">
          	 <ps:permissionAccess permissions="TUMN">      
		            <tr class="datacell1">
		           		<td valign="top">
		        		  <ps:label fieldId="userName">Username</ps:label>
		           		</td>
		               	<td valign="top">
${addEditUserForm.userName}
		               	</td>
		          	</tr>
	          	</ps:permissionAccess>
          	 </ps:isTpa>
          	  <ps:isInternalUser name="userProfile" property="role">
	          	<c:if test="${userProfile.role.roleId eq 'PLC'}">
						<tr class="datacell1">
			           	   <td valign="top">
			        		  <ps:label fieldId="userName">Username</ps:label>
			           	   </td>
			               <td valign="top">
${addEditUserForm.userName}
			               </td>
			          	</tr>
	          	</c:if>
			</ps:isInternalUser>
</c:if>
<c:if test="${layoutBean.getParam('task') != 'view'}">
          	 <ps:isTpa name="userProfile" property="role">
	          	 <ps:permissionAccess permissions="TUMN">      
			            <tr class="datacell1">
			           		<td valign="top">
			        		  <ps:label fieldId="userName">Username</ps:label>
			           		</td>
			               	<td valign="top">
${addEditUserForm.userName}
			               	</td>
			          	</tr>
		          	</ps:permissionAccess>
          	 </ps:isTpa>
</c:if>
<tr>
	<td><ps:label fieldId="ssn">Social security number</ps:label></td>
	<c:if test="${userProfile.role.roleId ne 'ICC'}">
		<td><render:fullmaskSSN property="addEditUserForm.ssn" /></td>
	</c:if>
	<c:if test="${userProfile.role.roleId eq 'ICC'}">
		<td><render:ssn property="addEditUserForm.ssn" /></td>
	</c:if>
</tr>

<tr class="datacell1">
 				<td valign="top">
 				  <ps:label fieldId="webAccess">Web access</ps:label>
 				</td>
   				<td  valign="top">
  				  <logicext:if name="addEditUserForm" property="webAccess" op="equal" value="true">
                  	<logicext:then>yes</logicext:then>
				    <logicext:else>no</logicext:else>
				  </logicext:if>
           		</td>
 			</tr>
    		
 			<tr>
           		<td><strong> Password status </strong></td>
<td>${addEditUserForm.passwordState}</td>
           	</tr>
           	
<logicext:if name="layoutBean" property="param(task)" op="equal" value="view">
  <logicext:then>
           	<tr>
           		<td><strong> Profile status </strong></td>
<td>${addEditUserForm.profileStatus}</td>
           	</tr>
  </logicext:then>
</logicext:if>
			
    		
           	
