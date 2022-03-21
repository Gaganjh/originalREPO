<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<%-- user profile starts  --%>

 			<tr class="datacell1">
 				<td width="46%" valign="top">
 				  <ps:label fieldId="firstName" mandatory="true">First name</ps:label>
 				</td>
   				<td  valign="top">
   				<form:input path="firstName"  maxlength="30" size="50" class="inputField"/>
              		<ps:trackChanges name="addEditUserForm" property="firstName"/>
           		</td>
 			</tr>
         	<tr class="datacell1">
        		<td width="46%" valign="top">
        		  <ps:label fieldId="lastName" mandatory="true">Last name</ps:label>
        		</td>
            	<td valign="top">
            	<form:input path="lastName" maxlength="30" size="50" class="inputField"/>
            	</td>
              	<ps:trackChanges name="addEditUserForm" property="lastName"/>
          	</tr>
            <tr class="datacell1">
           		<td valign="top">
        		  <ps:label fieldId="email" mandatory="true">Primary Email</ps:label>
           		</td>
               	<td valign="top">
               	<form:input path="email" size="50" class="inputField"/>
               		<ps:trackChanges name="addEditUserForm" property="email"/>
               	</td>
          	</tr>
<c:if test="${not empty addEditUserForm.secondaryEmail}">
          	 <tr class="datacell1">
           		<td valign="top">
        		  <ps:label fieldId="secondaryEmail" mandatory="false">Secondary Email</ps:label>
           		</td>
               	<td valign="top">
${addEditUserForm.secondaryEmail}
               	</td>
          	</tr>
</c:if>
