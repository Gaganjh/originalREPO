<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<content:contentBean contentId="<%=ContentConstants.WARNING_REGISTRATION_DECLINE%>"
                           	type="<%=ContentConstants.TYPE_MESSAGE%>"
                          	id="warningRegistrationDecline"/>
                          	
<script type="text/javascript" >
<!--

function MM_popupMsg(msg) { //v1.0
  var yesno = window.confirm(msg);

  if (yesno==true) {
	  return true;
  } else {
      return false;
  }
}

-->
</script>

 
<%-- This jsp includes the following CMA content --%>

<table width="760" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
				<td width="15%"><img height="8" src="/assets/unmanaged/images/spacer.gif" width="10" border="0"></td>
				<td width="5%"><img height="1" src="/assets/unmanaged/images/spacer.gif" width="15" border="0"></td>
				<td width="50%" valign="top" class="greyText"> <img src="/assets/unmanaged/images/s.gif" width="402" height="23"><br>
					<img src="<content:pageImage type="pageTitle" beanName="layoutPageBean"/>" alt="<content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>" width="215" height="34"><br>		<br>
					<img src="/assets/unmanaged/images/s.gif" width="1" height="20"> <br>
					<table width="425" border="0" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
								<td colspan="8">
									<b><content:getAttribute beanName="layoutPageBean" attribute="subHeader"/></b>
								</td>
							</tr>
							<tr>
								<td colspan="8"> 
									<content:getAttribute attribute="introduction1" beanName="layoutPageBean"/>
									<content:getAttribute attribute="introduction2" beanName="layoutPageBean"/>
									<br><content:getAttribute attribute="body1" beanName="layoutPageBean"/>
									<br><content:getAttribute attribute="body2" beanName="layoutPageBean"/>
								</td>
							</tr>
							<tr>
								<td colspan="2" align="left">
									<content:errors scope="request" />
								</td>
							</tr>
							</tbody>
					</table>
					<br>
						<table width="425" border="0" cellspacing="0" cellpadding="0">
							<tbody>
							<tr align="center">
								<ps:form method="POST" modelAttribute="registerForm" name="registerForm" action="/do/registration/terms/" >
								<td width="112">&nbsp;</td>
								<td width="139">
									<input name="action" type="submit" class="button100Lg" name="action" onClick="return MM_popupMsg('<content:getAttribute beanName="warningRegistrationDecline" attribute="text"/>');" value="decline" >
								</td>
								<td width="144">
<input type="submit" class="button100Lg" name="action" value="accept" />
								</td>
								</ps:form>
							</tr>
							</tbody>
						</table>
					</td>
				<td width="5%" height="312" valign="top" class="fixedTable"><img src="/assets/unmanaged/images/spacer.gif" width="20" height="1"></td>
				<td width="20%" height="312" valign="top" class="fixedTable">
					<div align="left"><br></div>
				</td>
		</tr>
		<tr>
				<td width="1%"><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
				<td colspan="13" width="99%">
					<br>
					<p><content:pageFooter beanName="layoutPageBean"/></p>
					<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
					<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
				</td>
		</tr>
	</tbody>
</table>
