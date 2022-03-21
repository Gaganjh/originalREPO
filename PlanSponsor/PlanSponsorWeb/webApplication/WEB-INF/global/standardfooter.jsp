<%-- Prevent the creation of a session --%>
<%@ page session="false" %>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib" prefix="content" %>


<script language="javascript" >
<!--
function openWin(url) {
	options="toolbar=1,status=1,menubar=1,scrollbars=1,resizable=1,width=800,height=450,left=10,top=10";
	newwindow=window.open(url, "general", options);
	if (navigator.appName=="Netscape") {
		newwindow.focus();
	}
}
//-->
</script>





<c:if test="${empty param.printFriendly}" >
<c:if test="${layoutBean.pba ==true}">
<%
    // because the session is turned off for jsp, has to explicitly
    // get session from request and check PBA for the current contract.
    boolean pbaContract = false;
    javax.servlet.http.HttpSession theSession = request.getSession(false);
    if (theSession != null) {
    	UserProfile profile = (UserProfile) theSession.getAttribute(Constants.USERPROFILE_KEY);
    	if (profile != null && profile.getCurrentContract() != null) {
    		pbaContract = profile.getCurrentContract().isPBA();
    	}
    }
    if (pbaContract) {
%>
<content:contentBean contentId="<%=ContentConstants.IS_PBA%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="pba"/>
<% } %>
</c:if>

<!---->
<tr>
	<td>
	     <br/>
		<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
             type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
             id="globalDisclosure"/>

		<table cellpadding="0" cellspacing="0" border="0" width="765" class="fixedTable" height="">
			<tr>
				<td width="30">&nbsp;</td>
				<td width="735"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
			</tr>
		</table>
	</td>
</tr>
<!--</table>-->
<tr>
   <td>
	<table cellpadding="0" cellspacing="0" border="0" width="765" class="fixedTable" height="25">
	  <tr>
<c:if test="${applicationScope.environment.siteLocation=='usa'}" >
      <td class="greyText" align="right">
      	<a href="javascript:openWin('https://www.johnhancock.com/accessibility.html');">Accessibility</a> &nbsp;|&nbsp;
        <a href="javascript:openWin('https://www.johnhancock.com/legal.html');" class="greyText">Legal</a> &nbsp;|&nbsp;
        <a href="javascript:openWin('https://www.johnhancock.com/privacysecurity.html');" class="greyText">Privacy & Security</a> |&nbsp;
        <a href="javascript:openWin('https://www.johnhancock.com');">Corporate Web site</a>&nbsp;
<c:if test="${layoutBean.getParam('showRecommendedSettingsFooter') == 'Y'}">


            |&nbsp;<A href="/public/recommendedSettings">Recommended settings</a>
</c:if>
        </td>
</c:if>

<c:if test="${applicationScope.environment.siteLocation=='ny'}" >
      <td>&nbsp;</td>
      <td class="greyText" align="right">
      		<a href="javascript:openWin('https://www.johnhancock.com/accessibility.html');">Accessibility</a> &nbsp;|&nbsp;
      		<a href="javascript:openWin('https://www.johnhancock.com/legal.html');" class="greyText">Legal</a>&nbsp;|&nbsp;
 			<a href="javascript:openWin('https://www.johnhancock.com/privacysecurity.html');" class="greyText">Privacy & Security</a> |&nbsp;
 			<a href="javascript:openWin('https://www.johnhancock.com');">Corporate Web site</a>&nbsp;
<c:if test="${layoutBean.getParam('showRecommendedSettingsFooter') == 'Y'}">


            |&nbsp;<A href="/public/recommendedSettings">Recommended settings</a>
</c:if>
        </td>
</c:if>
	  </tr>
    </table>
  </td>
</tr>
</c:if>
