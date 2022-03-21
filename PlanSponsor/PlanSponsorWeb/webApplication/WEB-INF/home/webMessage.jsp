
<%-- This jsp is included as part of the secureHomePage.jsp --%>

<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="notifications" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract" %>
<%@ page import="com.manulife.pension.service.security.role.InternalUser" %>

<%-- This jsp includes the following CMA content --%>
<content:contentBean contentId="<%=ContentConstants.PS_WEB_MESSAGE_HEADER%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="WebMessageHeader"/>
<content:contentBean contentId="<%=ContentConstants.PS_WEB_MESSAGE_DISCLAIMER%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" beanName="Web_Message_Disclaimer"/>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<script type="text/javascript" >

function confirm_open(url) {
    var disclaimer = "<content:getAttribute beanName='Web_Message_Disclaimer' attribute='text' filter='true'/>";
    var r = confirm(disclaimer);
    if (r == true) {
	options="toolbar=1,status=1,menubar=1,scrollbars=1,resizable=1,width=800,height=450,left=10,top=10";
	newwindow=window.open(url, "general", options);
	if (navigator.appName=="Netscape") {
		newwindow.focus();
	}
    }
}
</script>

<c:set var="contractSummary" value="${contractSummary}" /> 
<c:set var="participantList" value="${contractSummary.participants}" /> 
<c:set var="webMessages" value="${contractSummary.webMessages}" /> 

<c:set var="contractStatus" value="${userProfile.currentContract.status}"/>

<table width="240" border="0" cellspacing="0" cellpadding="0" class="box">
              <tr>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="238"><img src="/assets/unmanaged/images/s.gif" width="238" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              </tr>
              <tr class="tablehead">
                <td colspan="3" class="tableheadTD1">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td class="tableheadTD"><b><%-- CMA managed--%><content:getAttribute beanName="WebMessageHeader" attribute="title"/></b></td>
                   </tr>
                </table></td>
              </tr>

              <tr>
                <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td class="boxbody" valign="top">

                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
<c:forEach items="${webMessages}" var="webMessage" >


                    <tr>
                      <td width="35%" align="left" valign="top">
<c:if test="${not empty webMessage.webMessageUrl}">
<a href="javascript:confirm_open('${webMessage.webMessageUrl}');">
${webMessage.webMessageText}</a></td>
</c:if>
<c:if test="${empty webMessage.webMessageUrl}">
${webMessage.webMessageText}<br><br></td>
</c:if>
                    </tr>
</c:forEach>
                  </table>
                  <Br> </td>
                <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              </tr>
              <tr>
                <td colspan="3">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
                      <td rowspan="2"  width="5"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
                    </tr>
                    <tr>
                      <td height="2" class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                    </tr>
                </table></td>
              </tr>
            </table>
