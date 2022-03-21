<%@ taglib prefix="ps" uri="manulife/tags/ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" >
function doSubmit(action) {
	document.forms['passwordResetForm'].elements['action'].value = action;
	document.forms['passwordResetForm'].submit();
}
</SCRIPT>

<div id="errordivcs"><content:errors scope="session"/></div>
<content:contentBean
	contentId="<%=ContentConstants.RESET_PWD_CONFIRMATION_MESSAGE%>"
	type="<%=ContentConstants.TYPE_LAYOUT_PAGE%>"
	id="ResetPasswordConfirmation" />
<BR/>
<ps:form action="/do/pwdemail/ResetPasswordConfirm/" modelAttribute="passwordResetForm" name="passwordResetForm">
          
  			
<input type="hidden" name="action"/>
	<table>
		<tr>
			<td>
				<b><content:getAttribute id="ResetPasswordConfirmation" attribute="body1Header"/></b>
			</td>
		</tr>
		<tr>
			<td>
				<content:getAttribute id="ResetPasswordConfirmation" attribute="body1">
					<content:param><c:out value="${passwordResetForm.firstName}"/> 
					<c:out value="${passwordResetForm.lastName}"/></content:param>
					<content:param><c:out value="${passwordResetForm.employerProvidedEmailAddress}"/></content:param>   
					<content:param><c:out value="${passwordResetForm.requestedTs}"/></content:param>    
				</content:getAttribute>
			</td>
		</tr>
		<tr>
			<td><br/><br/></td>
		</tr>
		<tr>
			<td align='right'>
				<input type="button" name="button3" value="finish" class="button134" onclick="doSubmit('finish')" />
			</td>
		</tr>
	</table>
</ps:form>
