<%@ taglib uri="manulife/tags/ps" prefix="ps" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<%@ page import="com.manulife.pension.service.security.entity.LDAPUser" %>
<%@ page import="com.manulife.pension.service.security.ldap.LdapDao" %>

<SCRIPT language="javascript">
	var clicked = false;
	function doSubmit(action) {
		if (!clicked) {
			clicked = true;
			document.forms['passwordResetForm'].elements['action'].value = action;
			if(document.getElementById('reset') != null)
			document.getElementById('reset').disabled = true;
			document.forms['passwordResetForm'].submit();

		} else {
			window.status = "Transaction already in progress ... please wait.";
		}
	}
</SCRIPT>

<div id="errordivcs"><content:errors scope="session"/></div>
<ps:form method="POST" modelAttribute="passwordResetForm" name="passwordResetForm" action="/do/pwdemail/ResetPassword/">
			
  			
<input type="hidden" name="action"/>
<input type="hidden" name="fromReset" value="true"/>
<form:hidden path="profileId"/>
	
	<jsp:include flush="true" page="resetBasicSection.jsp"/>
	<BR/>
		
	<table width="505" border="0" cellpadding="0" cellspacing="0">
    <tr>
  		<td width="32%">
  			<input type="button" name="button1" value="back" class="button134" onclick="doSubmit('back')"/>
  		</td>
    	<td width="38%">
    		<input type="button" name="button2" value="edit employee snapshot" class="button175"  onclick="doSubmit('edit')"/>
    	</td>
    	<td width="30%" align="right">
	   		<c:if test="${!passwordResetForm.suppressResetPasswordButton}">
	   			<input id = 'reset' type="button" name="button3" value="reset Password" class="button134"  onclick="doSubmit('resetPassword'); return false;" />
	   		</c:if>
    	</td>
  	</tr>
	</table>
</ps:form>
