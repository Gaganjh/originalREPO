<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@page import="com.manulife.pension.ps.web.messagecenter.util.MCEnvironment"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCConstants"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="ps" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>

<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("STATUS_CONTRACT_APPROVED",Contract.STATUS_CONTRACT_APPROVED,PageContext.PAGE_SCOPE);
	String referrer = null; 
	if(request.getHeader("referer")!=null){
	referrer = request.getHeader("referer");
	} 
	String refererInd = null;
	if(null!= referrer && referrer.contains("/password/updatePassword/") ){
		refererInd="true";
	}
	else{
		refererInd="false";
	}
%>
<content:contentBean contentId="<%=ContentConstants.WARNING_DISCARD_CHANGES%>"
                     type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningDiscardChanges"/>

<% boolean showTpaHeader = false; %>


<c:if test="${layoutBean.tpaHeaderUsed ==true}">
  <ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
    <c:if test="${empty userProfile.currentContract }" >
         <% showTpaHeader = true; %>
         <jsp:include page="/WEB-INF/global/tpaheader.jsp" flush="true"/>
</c:if>
</ps:isTpa>


</c:if>

<% if ( !showTpaHeader ) { %>
<c:if test="${not empty param.printFriendly }" >
<table border="0" width="760" cellspacing="0" cellpadding="0">
<tr>
<c:if test="${applicationScope.environment.siteLocation=='usa'}" >
		<td width="400" class="datacell1"><img src="/assets/unmanaged/images/JH_blue_resized.gif" border="0"></td>
</c:if>
<c:if test="${applicationScope.environment.siteLocation=='ny'}" >
		<td width="400" class="datacell1"><img src="/assets/unmanaged/images/JH_blue_resized_NY.gif" border="0"></td>
</c:if>
	<td width="100%" class="datacell1" colspan="2">&nbsp;</td>
</tr>
</table>
<br/>
<c:if test="${not empty userProfile.currentContract }" >
${userProfile.currentContract.companyName} | Contract: ${userProfile.currentContract.contractNumber}
<br/>
</c:if>
</c:if>
<c:if test="${empty param.printFriendly }" >
<table border="0" width="760" cellspacing="0" cellpadding="0">
<tr >
<c:if test="${applicationScope.environment.siteLocation=='usa'}" >
		<content:contentBean contentId="<%=ContentConstants.COMPANY_LOGO%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="companyLogo"/>

		
	    <ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
              <td class="logo" width="459" ><a href="/do/home/ChangeContract/"><img src='<content:getAttribute id="companyLogo" attribute="image.path" />' border="0"></a></td>
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
           <% if(refererInd !="true") { %>
              <td class="logo" width="459"><a href="/do/home/ChangeContract/"><img src='<content:getAttribute id="companyLogoNY" attribute="image.path"/>' border="0"></a></td>
            <% } %>
	    </ps:isTpa>
	    <ps:isNotTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
	      <td class="logo" width="459"><a href="/do/home/homePageFinder/"><img src='<content:getAttribute id="companyLogoNY" attribute="image.path"/>' border="0"></a></td>
	    </ps:isNotTpa>
</c:if>


    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="75"></td>
 	   <c:set var="signOutStyleClass" value="signOutLayerExternal"/>
 	   <c:if test="${userProfile.role.internalUser}">
 	   		<c:set var="signOutStyleClass" value="signOutLayerInternal"/>
 	   </c:if>
     <ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" > 
     <td rowspan="2" width="300" class="welcome"><div class="signOutLayerTPA">
     <a class="signOutLayerStyle2" href="#" onMouseOver='self.status="Sign out"; return true' onclick="javascript:doSignOut('<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>');return false;">&nbsp;Sign out</a>
           <% if(refererInd !="true") { %>
	  <span class="signOutLayerTPAStyle1"> | </span>
	  <a class="signOutLayerStyle2" href="#" onMouseOver='self.status="Contract home"; return true' onclick="javascript:doContractHome('<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>');return false;">Contract home</a>
	  <%} %>     
	<c:if test="${not empty userProfile.currentContract }" >
<c:if test="${userProfile.currentContract.contractAllocated ==true}">
        <% if(refererInd !="true") {%>
       <c:choose>
	       <c:when test="${userProfile.role.internalUser}">
	       <span class="signOutLayerTPAStyle1"> | </span>
    			<a href="/do/password/changePasswordInternal/?loginFlow=Y" class="signOutLayerStyle2">Change password</a>
	       </c:when>
	       <c:otherwise>
	       <span class="signOutLayerTPAStyle1"> | </span>
	   			<a href="/do/profiles/editMyProfile/?loginFlow=Y" class="signOutLayerStyle2">Edit my profile</a>					       
	       </c:otherwise>
       </c:choose>  
       <%} %>     
</c:if>
		  <!-- Contact Management removed the contact us link fro unallocated contract-->
<c:if test="${userProfile.currentContract.contractAllocated ==false}">
<c:if test="${userProfile.role != 'UAL'}">
</c:if>
</c:if>
	</c:if>
	<c:if test="${empty userProfile.currentContract }" >
         <% if(refererInd !="true") {%>
       <c:choose>
	       <c:when test="${userProfile.role.internalUser}">
	        <span class="signOutLayerTPAStyle1"> | </span> 
    			<a href="/do/password/changePasswordInternal/?loginFlow=Y" class="signOutLayerStyle2">Change password</a>
	       </c:when>
	       <c:otherwise>
	        <span class="signOutLayerTPAStyle1"> | </span> 
	   			<a href="/do/profiles/editMyProfile/?loginFlow=Y" class="signOutLayerStyle2">Edit my profile</a>					       
	       </c:otherwise>
       </c:choose><%} %>
	</c:if>
	</div>
	</ps:isTpa>
	     <ps:isNotTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >  
	<td rowspan="2" width="300" class="welcome"><div class="${signOutStyleClass}">&nbsp;
	     <a class="signOutLayerStyle2" href="#" onMouseOver='self.status="Sign out"; return true' onclick="javascript:doSignOut('<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>');return false;">Sign out</a>
		<c:if test="${not empty userProfile.currentContract }" >
<c:if test="${userProfile.currentContract.contractAllocated ==true}">
			           <% if(refererInd !="true") {%>
		               <c:choose>
					       <c:when test="${userProfile.role.internalUser}">
					      		<span class="signOutLayerStyle1"> | </span>
				    			<a href="/do/password/changePasswordInternal/?loginFlow=Y" class="signOutLayerStyle2">Change password</a>
					       </c:when>
					       <c:otherwise>
					       		<span class="signOutLayerStyle1"> | </span>
					   			<a href="/do/profiles/editMyProfile/?loginFlow=Y" class="signOutLayerStyle2">Edit my profile</a>					       
					       </c:otherwise>
				       </c:choose>  <%} %>     
</c:if>
			  <!-- Contact Management removed the contact us link fro unallocated contract-->
<c:if test="${userProfile.currentContract.contractAllocated ==false}">
<c:if test="${userProfile.role != 'UAL'}">
</c:if>
</c:if>
	</c:if>
		<c:if test="${empty userProfile.currentContract }" >
	       <% if(refererInd !="true") {%>
	       <c:choose>
		       <c:when test="${userProfile.role.internalUser}">
	    			 <span class="signOutLayerStyle1"> | </span>
	    			<a href="/do/password/changePasswordInternal/?loginFlow=Y" class="signOutLayerStyle2">Change password</a>
		       </c:when>
		       <c:otherwise>
		   			 <span class="signOutLayerStyle1"> | </span>
		   			<a href="/do/profiles/editMyProfile/?loginFlow=Y" class="signOutLayerStyle2">Edit my profile</a>					       
		       </c:otherwise>
	       </c:choose>  <%} %>     
		</c:if>
		</div>
	</ps:isNotTpa>
	</td>
</tr>
<c:if test="${not empty userProfile.currentContract }" >
<tr>
    <td class="welcome"><img src="/assets/unmanaged/images/s.gif" width="30" height="1">
<c:if test="${userProfile.role != 'UAL'}">
	    <b><render:text property="userProfile.currentContract.companyName" length="30"/></b> <b></b>
    	<b> |
</b>Contract: ${userProfile.currentContract.contractNumber}
<c:if test="${userProfile.multipleContracts ==true}">
	    	(<a href="/do/home/ChangeContract/"><span class="welcome"><u>change</u></span></a>)
</c:if>
<c:if test="${userProfile.multipleContracts ==false}">
	      <ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
	    	(<a href="/do/home/ChangeContract/"><span class="welcome"><u>change</u></span></a>)
	      </ps:isTpa>
</c:if>
</c:if>
    </td>
    <td><img src="/assets/unmanaged/images/s.gif" width="1" height="25"></td>
</tr>
</c:if>
<c:if test="${empty userProfile.currentContract }" >
<tr><td class="welcome">&nbsp;</td></tr>
</c:if>
</table>

<table width="760" border="0" cellspacing="0" cellpadding="0" >
  <tr>
    <td class="n1" width="30" valign="top"><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
      <img src="/assets/unmanaged/images/s.gif" width="30" height="17"></td>
    <td class="n1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    <td class="n1" width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
    <td class="n1" align="right">

	<ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
           
            <% if(refererInd !="true") {%>
          <a href="/do/home/ChangeContract/">Home</a>
            <%} %>
	</ps:isTpa>
	<% if(refererInd !="true") {%>
	<ps:isNotTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
          <a href="/do/home/homePageFinder/">Home</a>
	</ps:isNotTpa>
	<%} %>
<% if(refererInd !="true") {%>
<c:if test="${not empty userProfile.currentContract }" >
      <ps:notPermissionAccess permissions="SELA">
<c:if test="${userProfile.currentContract.contractAllocated == true}">
<c:if test="${userProfile.currentContract.status != STATUS_CONTRACT_APPROVED}">
      | <a href="/do/search/searchInputAction">Search</a>
</c:if>
</c:if>
      </ps:notPermissionAccess>
</c:if>
<c:if test="${not empty userProfile.currentContract }" >
<c:if test="${userProfile.currentContract.contractAllocated ==true}">
	  	  | <a href="/do/contacts/">Contact information</a>
</c:if>
</c:if>
<%} %>
	<% if(refererInd !="true") {%>
      <% if (MCEnvironment.isMessageCenterAvailable(request)) { %>
         <ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >| <a href="<%=MCConstants.DispatchUrl%>">Message center</a></ps:isNotJhtc>
      <%} %>
     <%} %>
	  	<ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
       <% if(refererInd !="true") {%>
        | <a href="/do/resources/ContactUsAction">Contact us</a>
       <%} %>
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
<% if(refererInd !="true") {%>
<c:if test="${not empty userProfile.currentContract }" >
<c:if test="${userProfile.currentContract.definedBenefitContract ==true}">
     <jsp:include page="definedbenefitnavigationbar.jsp" flush="true" />
</c:if>

<c:if test="${userProfile.currentContract.definedBenefitContract !=true}">
     <jsp:include page="navigationbar.jsp" flush="true" />
</c:if>
</c:if>
<%} %>

</c:if>
<% } %>
