<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="com.manulife.pension.service.security.utility.SecurityConstants" %>
<%@ page import="com.manulife.pension.ps.web.profiles.AddEditUserForm" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<% UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

String ADD_EDIT_USER_TPA_FIRM_LIST=Constants.ADD_EDIT_USER_TPA_FIRM_LIST;
pageContext.setAttribute("ADD_EDIT_USER_TPA_FIRM_LIST",ADD_EDIT_USER_TPA_FIRM_LIST,PageContext.PAGE_SCOPE);

String LOCKED_PASSWORD_STATUS=SecurityConstants.LOCKED_PASSWORD_STATUS;
pageContext.setAttribute("LOCKED_PASSWORD_STATUS",LOCKED_PASSWORD_STATUS,PageContext.PAGE_SCOPE);

%>
<content:contentBean contentId="<%=ContentConstants.WARNING_TPA_NO_MORE_REGISTERED_USER%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningTpaNoMoreRegisteredUser"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_TPA_LAST_USER_RECEIVE_ILOANS_EMAIL%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningLastUserReceiveIloansEmail"/>


<content:contentBean contentId="<%=ContentConstants.WARNING_TPA_LAST_USER_STAFF_PLAN_AND_RECEIVE_ILOANS_EMAIL%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningLastUserStaffPlanAndReceiveIloansEmail"/>
							
<content:contentBean contentId="<%=ContentConstants.WARNING_DELETE%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningDelete"/>

<c:if test="${ADD_EDIT_USER_TPA_FIRM_LIST}">
<c:set var="firmList" value="${ADD_EDIT_USER_TPA_FIRM_LIST}" scope="request"/> <%-- scope="request" --%>
</c:if>

<c:set var="userProfile" value="${userProfile}" scope="session"/> <%-- scope="session" type="com.manulife.pension.ps.web.controller.UserProfile" --%>
<c:set var="addEditUserForm" value="${addEditUserForm}" scope="session"/>
<jsp:useBean id="addEditUserForm" scope="session"
	class="com.manulife.pension.ps.web.profiles.AddEditUserForm" />


<script type="text/javascript" >
var submitted=false;
var prefsOpen = false;

function doViewPermissions(firmNumber){
	if (!submitted) {
		submitted = true;
		document.addEditUserForm.tpaFirmId.value = firmNumber;
		document.addEditUserForm.action.value = 'viewPermissions';
		return true;
	} else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	}
}


function toggleSection() {
	var section = document.all["prefs"].style;
	if (prefsOpen == false) {
		section.display = "block";
		prefsOpen = true;
		document.addEditUserForm.tabOpen.value  = "true";
	} else {
	    section.display = "none";
		prefsOpen = false;
		document.addEditUserForm.tabOpen.value ="false";
	}
}

function canSubmit() {
	if (!submitted) {
		submitted = true;
		return true;
	} else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	}
}

function doBack(form) {
	if (!submitted) {
		submitted = doCancel(form);
		return submitted;
	} else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	}
}

function doEdit(href) {
	if (!submitted) {
		submitted = true;
		window.location.href = href;
	} else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return;
	}
}

function deleteConfirm() {
	if (!submitted) {
		var warnings = getWarnings();
		var res = confirm(warnings + "<content:getAttribute beanName="warningDelete" attribute="text" filter="true"/>");
		if (res == true) {
			submitted = true;
			document.addEditUserForm.action.value = 'delete';
			document.addEditUserForm.actionLabel.value = 'delete';
			document.addEditUserForm.submit();
			return true;
		}else{
			
			return false;
		}
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}

function suspendConfirm() {
	if (!submitted) {

		var warnings = getWarnings();
		if (warnings != "") {
			submitted = confirm(warnings + "Are you sure you want to suspend this user?");
		} else {
			submitted = true;
		}
		return submitted;
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}

function getWarnings() {
	var warnings = "";
	 var i = 0;
	var tpaIdElement;
	while((tpaIdElement = document.getElementsByName("tpaFirms[" + i + "].id")[0]) != null) {
		// Warning if this was the last registered user for the firm
		if (document.getElementsByName("tpaFirms[" + i + "].lastRegisteredUser")[0].value == "true") {
			warnings += "Warning! This is the last active user for firm " + tpaIdElement.value +".\n";
		}

		// Warning if contract does not have a Client or TPA profile with manage users permission
		if (document.getElementsByName("tpaFirms[" + i + "].lastUserWithManageUsers")[0].value == "true") {
			warnings += "Warning! No users for firm " + tpaIdElement.value +" will have Manage Users permission.\n";
		}

		// Warning if contract does not have a Client or TPA profile with Receive i:loans email preference
<c:if test="${addEditUserForm.emailPreferences.showiLoanEmail == 'true'}">
		if (document.getElementsByName("tpaFirms[" + i + "].lastUserWithReceiveILoansEmail")[0].value == "true") {
			warnings += "Warning! No users for firm " + tpaIdElement.value +" will receive i:loans emails.\n";
		}
</c:if>

		// Warning if contract does not have a Client or TPA profile with Receive i:loans and TPA Staff Plan Access permission
<c:if test="${addEditUserForm.emailPreferences.showiLoanEmail == 'true'}">
		if (document.getElementsByName("tpaFirms[" + i + "].lastUserWithReceiveILoansEmailAndTPAStaffPlan")[0].value == "true") {
			warnings += "Warning! No users for firm " + tpaIdElement.value +" will receive i:loans emails and have TPA staff plan access permission.\n";
		}
</c:if>

		// Warning if contract does not have a Client or TPA profile with signing authority (approve i:withdrawals @ before) permission - Loans proj
		var signingAuthContracts = document.getElementsByName("tpaFirms[" + i + "].lastUserWithSigningAuthorityContracts")[0].value;
		if (signingAuthContracts != null && signingAuthContracts != "") {
			warnings += "Warning! No users for contract(s) " + signingAuthContracts +" will have Signing authority permission.\n";
		}

		// Warning if contract does not have a Client or TPA profile with review i:withdrawals permission
		var reviewWDWContracts = document.getElementsByName("tpaFirms[" + i + "].lastUserWithReviewIWithdrawalsContracts")[0].value;
		if (reviewWDWContracts != null && reviewWDWContracts != "") {
			warnings += "Warning! No users for contract(s) " + reviewWDWContracts +" will have Review i:withdrawals permission.\n";
		}

		// Warning if contract does not have a Client or TPA profile with review loans permission
		var reviewLoansContracts = document.getElementsByName("tpaFirms[" + i + "].lastUserWithReviewLoansContracts")[0].value;
		if (reviewLoansContracts != null && reviewLoansContracts != "") {
			warnings += "Warning! No users for contract(s) " + reviewLoansContracts +" will have Review Loans permission.\n";
		}

		i++;
	}

	return warnings;
}

</script>


<logicext:if name="layoutBean" property="param(task)" op="equal" value="view">
  <logicext:then>
<c:set var="actionPath" value="/do/profiles/viewTpaUser/" />
  </logicext:then>
</logicext:if>

<logicext:if name="layoutBean" property="param(task)" op="equal" value="suspend">
  <logicext:then>
<c:set var="actionPath" value="/do/profiles/suspendTpaProfile/" />
  </logicext:then>
</logicext:if>

<logicext:if name="layoutBean" property="param(task)" op="equal" value="unsuspend">
  <logicext:then>
<c:set var="actionPath" value="/do/profiles/unsuspendTpaProfile/" />
  </logicext:then>
</logicext:if>

<logicext:if name="layoutBean" property="param(task)" op="equal" value="delete">
  <logicext:then>
<c:set var="actionPath" value="/do/profiles/deleteTpaProfile/" />
  </logicext:then>
</logicext:if>


<%-- <jsp:useBean id="actionPath" type="java.lang.String"/> --%>


<ps:form cssClass="margin-bottom:0;"
         method="POST"
         action="${actionPath}" name="addEditUserForm" modelAttribute="addEditUserForm">
<form:hidden path="fromTPAContactsTab"/>
<form:hidden path="tpaFirmId"/>
<form:hidden path="tabOpen"/>
<form:hidden path="profileId"/>

<table width="700" border="0" cellspacing="0" cellpadding="0">

	<tr>
<%-- error line --%>
        <td>
       		 <p>
            	<div id="errordivcs"><content:errors scope="session"/></div>
            </p>
		</td>
 	</tr>
	<tr>
       	<td width="525"><img src="/assets/unmanaged/images/s.gif" width="525" height="1"></td>
   	</tr>
   	<tr>

<td>

<table>
 <tr>
  <td>

<%-- main table, one on left of screen --%>
<table width="425" border="0" cellpadding="0" cellspacing="0">
 	<tr>
    	<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
     	<td width="113"><img src="/assets/unmanaged/images/s.gif" width="113" height="1"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    	<td width="250"><img src="/assets/unmanaged/images/s.gif" width="250" height="1"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td width="80"><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
        <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
   		<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
   	</tr>
	<tr class="tablehead">
 		<td class="tableheadTD1" colspan="8"><b>
			<content:getAttribute id="layoutPageBean" attribute="body1Header"/></b>
 		</td>
 	</tr>
	<tr class="datacell1">
		<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<td colspan="6" align="center">


<%-- user profile starts  --%>
		  <table width="100%" border="0" cellspacing="0" cellpadding="3">

			<jsp:include page="viewProfileSection.jsp" flush="true" />
         	<tr></tr>

           	<tr>
           		<td></td>
           		<c:if test="${addEditUserForm.webAccess}"> 
           		<td align="right" onclick="toggleSection();">
           		   <a href="#">Email Preferences</a>&nbsp;<img src="/assets/unmanaged/images/layer_icon.gif" width="17" height="9">
           		</td>
           		</c:if>
           	</tr>

      	  </table>
        </td>
        <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr>

<%-- tpa firm  --%>
<c:if test="${not empty addEditUserForm.undeletedTpaFirms}">
     	
	       <jsp:include page="viewTpaUserAccessSectionNonCollapsible.jsp" flush="true"/>
	  	
</c:if>

	 <ps:roundedCorner numberOfColumns="8"
	                   emptyRowColor="white"
	                   oddRowColor="beige"
	                   evenRowColor="white"
		               name="addEditUserForm"
		               property="undeletedTpaFirms"/>

	<tr>
	   <td colspan="8">
	   <br/>
	   <table width="100%" border="0" cellspacing="0" cellpadding="0">
     	<tr>
       	   <td>
<input type="submit" class="button100Lg" onclick="return doBack(addEditUserForm);" value="back" name="action" /><%--  - property="actionLabel" --%>

           </td>

<logicext:if name="layoutBean" property="param(task)" op="equal" value="view">
<logicext:and name="addEditUserForm" property="profileStatus" op="notequal" value="Suspended"/>
  <logicext:then>
           <td>
<c:set var="theProfileId" value="${addEditUserForm.profileId}" />  
        	<input type=button class="button100Lg" value="edit this user"
   	 			onclick="doEdit('/do/profiles/editTpaUser/?profileId=${theProfileId}')" >
           </td>
<c:if test="${addEditUserForm.showUnlock ==true}">
<% String status=pageContext.getAttribute("LOCKED_PASSWORD_STATUS").toString();
pageContext.setAttribute("status",status);
%>
	  <logicext:if name="addEditUserForm" property="passwordState" op="equal" value="status">
		<logicext:then>
           <td>
<input type="submit" class="button100Lg" onclick="return canSubmit();" value="unlock password" name="action"/><%--  - property="actionLabel" --%>
           </td>
       </logicext:then>
     </logicext:if>
</c:if>
  <c:if test="${userProfile.role.roleId ne 'PLC'}">
           <td>
<input type="submit" class="button100Lg" onclick="return canSubmit();" value="reset password" name="action" /><%--  - property="actionLabel" --%>
           </td>
   </c:if>
  </logicext:then>
</logicext:if>

<logicext:if name="layoutBean" property="param(task)" op="equal" value="suspend">
  <logicext:then>
           <td align="right">
<input type="submit" class="button100Lg" onclick="return suspendConfirm();" value="suspend" name="action" /><%--  - property="actionLabel" --%>
           </td>
  </logicext:then>
</logicext:if>

<logicext:if name="layoutBean" property="param(task)" op="equal" value="unsuspend">
  <logicext:then>
           <td align="right">
<input type="submit" class="button100Lg" onclick="return canSubmit();" value="unsuspend" name="action"/><%--  - property="actionLabel" --%>
           </td>
  </logicext:then>
</logicext:if>

<logicext:if name="layoutBean" property="param(task)" op="equal" value="delete">
  <logicext:then>
           <td align="right">
<input type="submit" onclick="return deleteConfirm();" name="action" class="button100Lg" value="delete"/>
           </td>
  </logicext:then>
</logicext:if>

         </tr>
       </table>
	   </td>
	</tr>

</table>

  </td>
  <td>&nbsp;</td>
  <td valign="top">

   <%-- email fly-out part  --%>
   <jsp:include page="viewEmailPreferences.jsp" flush="true"/>

  </td>
 </tr>

</table>


</td>
</tr>
</table>

</ps:form>
