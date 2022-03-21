<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib" prefix="content" %>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

String SITEMODE_USA=Constants.SITEMODE_USA;
String SITEMODE_NY=Constants.SITEMODE_NY;

pageContext.setAttribute("SITEMODE_USA",SITEMODE_USA,PageContext.PAGE_SCOPE);
pageContext.setAttribute("SITEMODE_NY",SITEMODE_NY,PageContext.PAGE_SCOPE);
%>

<script type="text/javascript" >
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
<c:if test="${userprofile.currentContract.PBA == true }">

<content:contentBean contentId="<%=ContentConstants.IS_PBA%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="pba"/>
</c:if>
</c:if>
<!---->
<!--</table>-->
<!--the following code is also coded on the contractFundsReport.jsp. If you change the code here, ensure contract
FundsReport is also changed-->
    <tr>
	<table cellpadding="0" cellspacing="0" border="0" width="765" class="fixedTable" height="25">
<c:if test="${environment.siteLocation == SITEMODE_USA}" >
      <td class="greyText" align="right">
      	<a href="javascript:openWin('https://www.johnhancock.com/accessibility.html');">Accessibility</a> &nbsp;|&nbsp;
        <a href="javascript:openWin('https://www.johnhancock.com/legal.html');" class="greyText">Legal</a> &nbsp;|&nbsp;
        <a href="javascript:openWin('https://www.johnhancock.com/privacysecurity.html');" class="greyText">Privacy & Security</a> |&nbsp;
        <a href="javascript:openWin('https://www.manulife.com/corporate/corporate2.nsf/Public/Homepage');">Corporate Web site</a>&nbsp;
<c:if test="${layoutBean.getParam('showRecommendedSettingsFooter') == 'Y'}">


            |&nbsp;<A href="/public/recommendedSettings">Recommended settings</a>
</c:if>
        </td>
</c:if>

<c:if test="${environment.siteLocation == SITEMODE_NY}" >
      <td>&nbsp;</td>
      <td class="greyText" align="right">
      		<a href="javascript:openWin('https://www.johnhancock.com/accessibility.html');">Accessibility</a> &nbsp;|&nbsp;
      		<a href="javascript:openWin('https://www.johnhancock.com/general/legal.html');" class="greyText">Legal</a>&nbsp;|&nbsp;
			<a href="javascript:openWin('https://www.johnhancock.com/general/privacysecurity.html');" class="greyText">Privacy & Security</a> |&nbsp;
			<a href="javascript:openWin('https://www.johnhancock.com/individual.html');">Corporate Web site</a>&nbsp;
<c:if test="${layoutBean.getParam('showRecommendedSettingsFooter') == 'Y'}">


            |&nbsp;<A href="/public/recommendedSettings">Recommended settings</a>
</c:if>
        </td>
</c:if>
    </table>
    </tr>
</c:if>