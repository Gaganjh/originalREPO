<%-- Prevent the creation of a session --%>
<%@ page session="false" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.owasp.encoder.Encode"%>
<script>
testPopup()
function testPopup() {
	if(window.opener != null) {
		try {
			window.opener.document.location.href = "/do/";			
			alert("Your session expired, please login again");
			window.close();			
		} catch (e) {
			//if exception is thrown assume the request came from a different server
			//in which case we allow the user to login without showing the alert.
		}
	}
}

var clicked = false;

function preSubmit() {
	if (!clicked) {
		clicked=true;
		// Add rsa device print
		document.getElementById('devicePrintRSA').value = encode_deviceprint();
		return true;
	} else {
		window.status = "Transaction already in progress ... please wait.";
		return false;
	}
}
</script>

<tr>
	  <td width="16" class="big" height="312"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
      <td width="515" valign="top" height="312">
             
       	<table width="525" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="314" height="155" valign="top" class="greyText"> 
              <table width="88%" height="71" border="0" cellpadding="0" cellspacing="0">
                <tr> 
                  <td width="9%"><img src="/assets/unmanaged/images/s.gif" width="17" height="17"></td>
                  <td width="91%"><img src="/assets/unmanaged/images/sign_in.gif" width="186" height="34"></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td class="greyText">
				      	<content:pageIntro beanName="layoutPageBean">
				      	<%-- To-Do:Environment() is not visible using jsp:usebean --%>
                  		<content:param>${environment.companyName}</content:param>
                  		<content:param>/do/login/passwordResetAuthentication/</content:param>					
                  	</content:pageIntro>
                   </td>
                </tr>
               </table>
              <br><Br><br>
              <table width="87%" height="71" border="0" cellpadding="0" cellspacing="0">
                <tr> 
                  <td width="9%"><img src="/assets/unmanaged/images/s.gif" width="17" height="17"></td>
                  <td width="91%"><img src="/assets/unmanaged/images/first_visit.gif" width="186" height="34"></td>
                </tr>
                <tr> 
                  <td>&nbsp;</td>
                  <td class="greyText">
                  	<content:pageBody beanName="layoutPageBean"/>
                  	<br>
                  	<a href="/do/registration/authentication/">Register</a>
                  </td>
                </tr>
              </table>
              <br>
            </td>
	 <form name="loginForm" method="POST" action="/login/loginServlet" onSubmit="return preSubmit();">
	    <input type="hidden" name="<%=Constants.DIRECT_URL_ATTR%>" 
	      value="<%=Encode.forHtmlAttribute(StringUtils.trimToEmpty((String)request.getAttribute(Constants.DIRECT_URL_ATTR))) %>">            
            <td width="211" valign="top">
            	<table width="81%" border="0" cellspacing="0" cellpadding="0">
                	<tr>
                  		<td><b>Username:</b></td>
                	</tr>
                	<tr>
                  		<td><input type="text" name="userName" autocomplete="off"></td>
                	</tr>
                	<tr>
                  		<td><b>Password:</b></td>
                	</tr>
                	<tr>
                  		<td><input type="password" autocomplete="off" name="password"></td>
                	</tr>
                	<tr>
                  		<td><Br>
                  		<input id="devicePrintRSA" type="hidden"  name="devicePrintRSA">
                  		<input name="Submit" type="submit" class="button100Lg" value="sign in" size="50"></td>
                	</tr>
                	<tr>
	                	<td><br><content:getAttribute beanName="layoutPageBean" attribute="body2"/></td>
                	</tr>
					<tr>
						<td><br><content:errors scope="request"/></td>
					</tr>
									
              	</table> 
              		<tr>
						<td colspan="2">
						<p><content:pageFooter beanName="layoutPageBean"/></p>
      					<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
      					<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p> 
      					</td>
      				</tr>
      	
              </td>
          
        </table>
        </td>
      
	</form> 
	             
      <td width="20" valign="top" height="312"><img src="/assets/unmanaged/images/spacer.gif" width="20" height="1"></td>
      <td width="214" valign="top" height="312"> 
        <div align="left"><img src="/assets/unmanaged/images/spacer.gif" width="8" height="8" border="0" align="top"> 
        </div>
		<center>
			<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="fixedModule1" />
        </center>
      </td>
	</tr>
	
<script>
	setFocusOnFirstInputField("loginForm");
</script>
