<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>

<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%-- Beans used --%>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

 <%--- start statements info --%>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
 	<tr class="tablesubhead">
		<td colspan="3"><b>Statement information</b></td>
	</tr>
	<tr class="datacell1" valign="top">
    	<td width="45%"><b>Basis</b></td>
   		<td class="datadivider" width="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td width="55%"><b class="highlight">${contractProfile.statementInfo.basis}</b></td>
	</tr>
	
	<c:if test="${userProfile.currentContract.isDefinedBenefitContract() == false}">
	<tr class="datacell2" valign="top">
 		<td><b>Delivery method</b></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td><b class="highlight">${contractProfile.statementInfo.deliveryMethod}</b></td>
	</tr>
	<tr class="datacell1" valign="top">
		<td><b>Statement type</b></td>
        <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td><b class="highlight">${contractProfile.statementInfo.statementType}</b></td>
	</tr>
</c:if>
    <tr class="datacell2" valign="top">
		<td><b>Last printed</b></td>
		<td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td><b class="highlight"><render:date property = "contractProfile.statementInfo.lastPrintDate" defaultValue = "" /></b></td>
	</tr>
                    
	<tr>
		<td colspan="3" class="beigeborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>
</table>
 <%--- end statements info --%>
