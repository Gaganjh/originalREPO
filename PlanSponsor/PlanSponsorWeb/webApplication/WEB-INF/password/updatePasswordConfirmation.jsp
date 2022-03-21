<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.password.UpdatePasswordForm" %>

<%
	UpdatePasswordForm updatePasswordForm = (UpdatePasswordForm)session.getAttribute("updatePasswordForm");
	pageContext.setAttribute("updatePasswordForm",updatePasswordForm,PageContext.PAGE_SCOPE);
	// changes for defect 8587
	String referrer = null; 
	if(null != request.getHeader("referer")){
	referrer = request.getHeader("referer");
	} 
	boolean  refererInd = Boolean.FALSE;
	if(null!= referrer && referrer.contains("/updatePassword/") ){
		refererInd=Boolean.TRUE;
	}
	// end changes for defect 8587 
	
	 
%>

<c:set var="updatePasswordForm" value="${updatePasswordForm}" scope="page" />


<script type="text/javascript">
<%if(refererInd){%>
	var homeUrl = "/do/home/homePage";
	var urlBegin = window.location.href.indexOf('/do/');
	var domain = window.location.href.substring(0,urlBegin);
	var homePageUrl = domain.concat(homeUrl);
	window.history.replaceState(window.history.state,null,homePageUrl);
	for(var i = 0; i < 12 ; i++){
	  window.history.pushState(window.history.state,null,homePageUrl);
	}
<%}%>

var submitted=false;

function setButtonAndSubmit(button) {

	if (!submitted) {
		submitted=true;
		document.updatePasswordForm.button.value = button;
		document.updatePasswordForm.submit();
	} else {
		window.status = "Transaction already in progress.  Please wait.";
	}
}
</script>
   

<ps:form method="POST" modelAttribute="updatePasswordForm" name="updatePasswordForm" action="/do/password/updatePasswordConfirmation/">

<%-- table with 3 columns --%>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" aria-describedby="Update Password Confirm Guidelines">
    <tr>
      <td width="500" ><img src="/assets/unmanaged/images/s.gif" width="500" height="1" alt="gif image"></td>
      <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1" alt="gif image"></td>
      <td width="65" ><img src="/assets/unmanaged/images/s.gif" width="65" height="1" alt="gif image"></td>
    </tr>
    <tr>
      <td valign="top">
         <table width="525" border="0" cellpadding="0" cellspacing="0" aria-describedby="Update Password Confirm Guidelines">
         <content:errors scope="session"/>
           <tr>
              <td><content:getAttribute beanName="layoutPageBean" attribute="body1">
<content:param>${updatePasswordForm.userFullName}</content:param>
<content:param>${updatePasswordForm.emailAddress}</content:param>
          		</content:getAttribute>
              </td>
          </tr>
         </table>
         <BR><br><br>

        <table cellSpacing=0 cellPadding=0 width=525 border=0 aria-describedby="Update Password Confirm Guidelines">
        	<tbody>
              <tr align=middle>
                <td width=131>&nbsp;</td>
                <td width=131>
<input type="button" onclick="javascript:window.print()" name="abc" class="button100Lg" value="print"/>


                </td>
                <td width=132>
<input type="button" onclick="javascript:setButtonAndSubmit('finished');return false;" name="fin" class="button100Lg" value="finish"/>
<form:hidden path="button" />
                </td>
              </tr>
            </tbody>
         </table>
       </td>
       <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" alt="gif image"></td>
       <td vAlign=top>&nbsp;</td>
	 </tr>
  </table>

</ps:form>





