<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%-- Imports --%>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.service.security.valueobject.UserInfo" %>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

String REPORT_BEAN=Constants.REPORT_BEAN;
pageContext.setAttribute("REPORT_BEAN",REPORT_BEAN,PageContext.PAGE_SCOPE);


%>

<c:set var="userProfile" value="${userProfile}" scope="session"/> <%-- scope="session" type="com.manulife.pension.ps.web.controller.UserProfile" --%>

<c:set var="userInfo" value="${reportBean.userInfo}" scope="request"/>

<img
    src="/assets/unmanaged/images/s.gif" width="1" height="20">
<table width ="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td width="193"><img src="/assets/unmanaged/images/s.gif" width="153" height="1"></td>
        <td width="325"><img src="/assets/unmanaged/images/s.gif" width="153" height="1"></td>
        <td width="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr>
    <tr class="tablehead">
        <td class="tableheadTD1" colspan="5"><strong><content:getAttribute id="layoutPageBean" attribute="body1Header"/></strong></td>
    </tr>

    <tr class="datacell1">
        <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td colspan="3" align="center" >
        <table width="100%" border="0" cellspacing="0" cellpadding="3">
	    <tr>
        	<td><strong>Name</strong></td>
        	<td align="left"><render:name firstName="${userInfo.firstName}" lastName="${userInfo.lastName}" style="f"/></td>
        </tr>
        <tr >
        	<td ><strong>Primary Email</strong></td>
<td >${userInfo.email}</td>
	    </tr>
<c:if test="${not empty userInfo.secondaryEmail}">
	     <tr >
        	<td ><strong>Secondary Email</strong></td>
<td >${userInfo.secondaryEmail}</td>
	    </tr>
</c:if>
	    <ps:isTpa name="userInfo" property="role">			
			<tr >
				<td><strong>Telephone number</strong></td>
	        	<td >
	        	<c:if test="${userInfo.phoneNumber!=''}" >
	        	<render:phone property="userInfo.phoneNumber"/>
	        		<c:if test="${userInfo.phoneExtension!=''}" >
	        	 ext 
</c:if>
${userInfo.phoneExtension}
</c:if>
	        	</td>
		    </tr>
		    <tr >
	        	<td ><strong>Fax number</strong></td>
	        	<td >
	        	<c:if test="${userInfo.fax.areaCode and userInfo.fax.areaCode!=''}">
	         <render:fax property="userInfo.fax"/>  
</c:if>
	        	</td>
		    </tr>
		</ps:isTpa>
        <tr>
	        <td ><strong>Username</strong></td>
<td >${userInfo.userName}</td>
	    </tr>	
    <%-- only for external users --%>
    <ps:isNotInternalOrTpa name="userInfo" property="role">
    	<tr class="datacell1">
 	        <td><strong>Challenge question</strong><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td>${userInfo.challengeQuestion}</td>
	    </tr>
    </ps:isNotInternalOrTpa>
    <%--only for external users --%>

    	<tr>
        	<td><strong>Profile last updated</strong></td>
        	<td>
			<render:date property="userInfo.profileLastUpdatedTS"
						 patternOut="<%= RenderConstants.EXTRA_LONG_MDY %>"
						 defaultValue="" />
						 <c:if test="${not empty userInfo.profileLastUpdatedBy}">
by: ${userInfo.profileLastUpdatedBy}
<c:if test="${userInfo.profileLastUpdatedByInternal ==true}">
				at <ps:companyName/>
</c:if>
			</c:if>
			</td>
        </tr>
        </table>

        </td>
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	</tr>

	<ps:roundedCorner numberOfColumns="5"
					  emptyRowColor="white"/>
</table>
