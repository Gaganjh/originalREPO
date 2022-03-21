<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>


<content:contentBean contentId="<%=ContentConstants.EMIAL_PREFERENCE_SECTION_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="emailPrefSectionTitle"/>

<content:contentBean contentId="<%=ContentConstants.GENERAL_NOTIFICATIONS_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="generalNotificationSectionTitle"/>

<content:contentBean contentId="<%=ContentConstants.CLIENT_SERVICES_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="clientServicesGroupTitle"/>

<content:contentBean contentId="<%=ContentConstants.PARTICIPANT_SERVICES_GROUP_TITLE%>"
                   	 type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="participantServicesGroupTitle"/>

<jsp:useBean id="addEditUserForm" scope="session"
	class="com.manulife.pension.ps.web.profiles.AddEditUserForm" />
<c:set var="addEditUserForm" value="${addEditUserForm}" scope="request"/>

<c:if test="${addEditUserForm.tabOpen ==true}">
   <div id=prefs style="display:block">
</c:if>

<c:if test="${addEditUserForm.tabOpen ==false}">
   <div id=prefs style="display:none">
</c:if>


<table border="0" cellpadding="2" cellspacing="0" width="250">
    <tr class="tablehead">
 		<td height="28" class="tableheadTD1" colspan="4">
 		   <b><content:getAttribute beanName="emailPrefSectionTitle" attribute="text"/></b>
 		</td>
 	</tr>
 	<tr class="datacell1">
        <td rowspan="17" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        <td colspan="2" valign="top" class="tablesubhead"><b><content:getAttribute beanName="generalNotificationSectionTitle" attribute="text"/></b></td>
        <td width="1" rowspan="17" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    <tr>
        <td>Send newsletters, etc by email</td>
		<td align="left" nowrap>
		<form:radiobutton path="emailPreferences.sendNewsletters" value="true"/>Yes
		<form:radiobutton path="emailPreferences.sendNewsletters" value="false"/>No
		</td>
	</tr>
	<ps:trackChanges name= "addEditUserForm"  property="emailPreferences.sendNewsletters"/>

    
<c:if test="${addEditUserForm.emailPreferences.showiLoanEmail ==true}">
    <tr class="datacell1">
        <td colspan="2" valign="top" class="tablesubhead">
          <b><content:getAttribute beanName="participantServicesGroupTitle" attribute="text"/></b>
        </td>
    </tr>
      
    <tr>
        <td>Receive i:loans email</td>
		<td align="left" nowrap>
		<form:radiobutton path="emailPreferences.receiveiLoads" value="true"/>Yes
		<form:radiobutton path="emailPreferences.receiveiLoads" value="false"/>No
		</td>
	</tr>
	<ps:trackChanges name= "addEditUserForm"  property="emailPreferences.receiveiLoads"/>
</c:if>
	
	<tr>
        <td class="databorder" colspan="4"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
	
</table>
   
</div>   
