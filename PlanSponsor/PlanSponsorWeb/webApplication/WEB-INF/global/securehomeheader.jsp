<%-- Prevent the creation of a session --%>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="java.util.Calendar" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib" prefix="content" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="ps" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@page import="com.manulife.pension.ps.web.messagecenter.util.MCEnvironment"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCConstants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.SESSION_SCOPE);
%>
<c:if test="${empty param.printFriendly }" >

<table border="0" width="760" cellspacing="0" cellpadding="0">
<tr>
<c:if test="${applicationScope.environment.siteLocation=='usa'}" >
		<content:contentBean contentId="<%=ContentConstants.COMPANY_LOGO%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="companyLogo"/>

	  <ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
            <td class="logo" width="459"><a href="/do/home/ChangeContract/"><img src='<content:getAttribute id="companyLogo" attribute="image.path" />' border="0"></a></td>
	  </ps:isTpa>
	  <ps:isNotTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
            <td class="logo" width="459"><a href="/do/home/homePageFinder/"><img src='<content:getAttribute id="companyLogo" attribute="image.path" />' border="0"></a></td>
	  </ps:isNotTpa>
</c:if>
<c:if test="${applicationScope.environment.siteLocation=='ny'}" >
		<content:contentBean contentId="<%=ContentConstants.COMPANY_LOGO_NY %>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="companyLogoNY"/>

	 <ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
          <td class="logo" width="459"><a href="/do/home/ChangeContract/"><img src='<content:getAttribute id="companyLogoNY" attribute="image.path"/>' border="0"></a></td>
	 </ps:isTpa>
	 <ps:isNotTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
          <td class="logo" width="459"><a href="/do/home/homePageFinder/"><img src='<content:getAttribute id="companyLogoNY" attribute="image.path"/>' border="0"></a></td>
	 </ps:isNotTpa>
</c:if>

 	   <c:set var="signOutStyleClass" value="signOutLayerExternal"/>
 	   <c:if test="${userProfile.role.internalUser}">
 	   		<c:set var="signOutStyleClass" value="signOutLayerInternal"/>
 	   </c:if>
    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="75"></td>
    <td rowspan="2" width="300" class="welcome"><div class="${signOutStyleClass}">&nbsp;
     <a href="/do/home/Signout/" class="signOutLayerStyle2">Sign out</a>
     <ps:notPermissionAccess permissions="SELA">
     <c:if test="${not empty userProfile.currentContract}">
	<c:if test="${userProfile.currentContract.status ne 'CA'}">
       <span class="signOutLayerStyle1"> | </span>            
       <c:choose>
	       <c:when test="${userProfile.role.internalUser}">
    			<a href="/do/password/changePasswordInternal/?loginFlow=Y" class="signOutLayerStyle2">Change password</a>
	       </c:when>
	       <c:otherwise>
	   			<a href="/do/profiles/editMyProfile/?loginFlow=Y" class="signOutLayerStyle2">Edit my profile</a>					       
	       </c:otherwise>
       </c:choose>
	</c:if>
       </c:if>
       </ps:notPermissionAccess>
		</div>
    </td>
</tr>
<tr>
    <td class="welcome">
    	<img src="/assets/unmanaged/images/s.gif" width="30" height="1">
Welcome, <b>${userProfile.principal.firstName}</b>&nbsp;
<b>${userProfile.principal.lastName}</b>&nbsp;&nbsp;
	      	     <render:date value="<%= Calendar.getInstance().getTime().toString() %>" dateStyle="l" /></td>
    <td><img src="/assets/unmanaged/images/s.gif" width="1" height="25"></td>
</tr>

</table>

<table width="760" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="n1" width="30" valign="top"><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
      <img src="/assets/unmanaged/images/s.gif" width="30" height="17"></td>
    <td class="n1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    <td class="n1" width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
    <td class="n1" align="right">
   	<ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
          <a href="/do/home/ChangeContract/">Home</a>
	</ps:isTpa>
	<ps:isNotTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
          <a href="#">Home</a>
        </ps:isNotTpa>
      <ps:notPermissionAccess permissions="SELA">
     <c:if test="${not empty userProfile.currentContract}">
<c:if test="${userProfile.currentContract.contractAllocated ==true}">
      | <a href="/do/search/searchInputAction">Search</a>
</c:if>
      </c:if>
      </ps:notPermissionAccess>
	<c:if test="${not empty userProfile.currentContract}">
<c:if test="${userProfile.currentContract.contractAllocated ==true}">
      | <a href="/do/contacts/" title="contacts">Contact information</a>
</c:if>
    </c:if>
    <c:if test="${empty userProfile.currentContract}">
      <ps:isTpaum name ="userProfile" property ="principal.role" >      
      | <a href="/do/profiles/manageUsers/">Manage profiles</a>
      </ps:isTpaum>
    </c:if>
    
      <% if (MCEnvironment.isMessageCenterAvailable(request)) { %>
	  <ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >| <a href="<%=MCConstants.DispatchUrl%>">Message center</a></ps:isNotJhtc> 
	  <%} %>
	  
	  	<ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
        | <a href="/do/resources/ContactUsAction">Contact us</a>
        </ps:isTpa>
    </td>
    <td class="n1" width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
  </tr>
  <tr>
    <td class="n2"><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
      <img src="/assets/unmanaged/images/s.gif" width="30" height="17"></td>
     <td class="n2" colspan="3">
    </td>
    <td class="n2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
  </tr>
</table>
<c:if test="${not empty userProfile.currentContract}">
<c:if test="${userProfile.currentContract.definedBenefitContract ==true}">
     <jsp:include page="definedbenefitnavigationbar.jsp" flush="true" />
</c:if>
</c:if>
<c:if test="${not empty userProfile.currentContract}">
<c:if test="${userProfile.currentContract.definedBenefitContract !=true}">
<jsp:include page="navigationbar.jsp" flush="true" />
</c:if>
</c:if>

</c:if>


