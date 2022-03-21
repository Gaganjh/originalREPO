<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="java.util.Calendar" %>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCConstants"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib" prefix="content" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%@page import="java.util.Set"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<% boolean showTpaHeader = false; %>   	      

<c:if test="${layoutBean.tpaHeaderUsed ==true}">
  <ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >  
    <c:if test="${empty userProfile.currentContract}">
	  <% showTpaHeader = true; %>   	      
      <jsp:include page="/WEB-INF/global/tpacontractlistingheader.jsp" flush="true"/>
    </c:if>      
  </ps:isTpa>  
</c:if>

<% if (!showTpaHeader) { %>
	<table border="0" width="100%" cellspacing="0" cellpadding="0" >
	<tr>
<c:if test="${applicationScope.environment.siteLocation=='usa'}" >
			<content:contentBean contentId="<%=ContentConstants.COMPANY_LOGO%>"
	                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	                     id="companyLogo"/>
	
		    <td class="logo" width="459"><a href="/do/home/homePageFinder/"><img src='<content:getAttribute id="companyLogo" attribute="image.path" />' border="0"></a></td>
</c:if>
<c:if test="${applicationScope.environment.siteLocation=='ny'}" >
			<content:contentBean contentId="<%=ContentConstants.COMPANY_LOGO_NY %>"
	                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	                     id="companyLogoNY"/>
		    <td class="logo" width="459"><a href="/do/home/homePageFinder/"><img src='<content:getAttribute id="companyLogoNY" attribute="image.path"/>' border="0"></a></td>
</c:if>
	    
	     
	    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="75"></td>
 	   <c:set var="signOutStyleClass" value="signOutLayerExternal"/>
 	   <c:if test="${userProfile.role.internalUser}">
 	   		<c:set var="signOutStyleClass" value="signOutLayerInternal"/>
 	   </c:if>
	    <td rowspan="2" width="300" class="welcome"><div class="${signOutStyleClass}">&nbsp;
     <a class="signOutLayerStyle2" href="/do/home/Signout/">Sign out</a>
       <span class="signOutLayerStyle1"> | </span>
       <c:choose>
	       <c:when test="${userProfile.role.internalUser}">
    			<a href="/do/password/changePasswordInternal/?loginFlow=Y" class="signOutLayerStyle2">Change password</a>
	       </c:when>
	       <c:otherwise>
	   			<a href="/do/profiles/editMyProfile/?loginFlow=Y" class="signOutLayerStyle2">Edit my profile</a>					       
	       </c:otherwise>
       </c:choose>
		</div></td>
	</tr>
	<tr>
	    <td class="welcome"><img src="/assets/unmanaged/images/s.gif" width="30" height="1">
	      &nbsp;
	    </td>
	    <td><img src="/assets/unmanaged/images/s.gif" width="1" height="25"></td>
	</tr>
	
	<tr>
	<td colspan="3">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" >
	  <tr>
	    <td class="n1" width="30" valign="top"><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
	      <img src="/assets/unmanaged/images/s.gif" width="30" height="17">
	    </td>
	    <td width="125" class="n1" valign="middle" align="left">
	       &nbsp;&nbsp;<render:date value="<%= Calendar.getInstance().getTime().toString() %>" dateStyle="l" /> 
	    </td>
	    <td class="n1" width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	    <td class="n1" align="right">
			 <ps:isInternalUser name="<%= Constants.USERPROFILE_KEY%>" property="role">
				<ps:permissionAccess permissions="INMN">
		         <a href="/do/profiles/manageInternalUsers/">Manage internal users</a>&nbsp;|&nbsp;
		      	</ps:permissionAccess>
		     </ps:isInternalUser>
		    <ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >
			<ps:isInternalUser name="<%= Constants.USERPROFILE_KEY %>" property="role" >
				<a href="<%=MCConstants.DispathUrlfromGlobal%>">Message center</a>
			</ps:isInternalUser>	
			<ps:isNotInternalOrTpa name="<%=Constants.USERPROFILE_KEY %>" property="role">
				<a href="<%=MCConstants.DispatchUrl%>">Message center</a>
			</ps:isNotInternalOrTpa>
			</ps:isNotJhtc>			    
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
	  <tr>
	    <td><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
	      <img src="/assets/unmanaged/images/s.gif" width="30" height="17"></td>
	     <td colspan="3">
	    </td>
	    <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>
	          
	</table>
	
<c:if test="${not empty layoutBean.getParam('suppressContent')}">
<c:set var="suppressContent" value="${layoutBean.getParam('suppressContent')}" /> 
</c:if>
	<c:if test="${suppressContent==false}" >		
	
	<table width="100%" border="0" cellspacing="0" cellpadding="0" >
	  <tr>
		<td width="30" valign="top">
		    <img src="/assets/unmanaged/images/s.gif" width="30" height="1">
		</td>
	    	
		<td width="730" valign="top">
		
		  <%-- table with 3 columns --%>
		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			   <tr>
			      <td valign="top">		      
<c:if test="${not empty layoutBean.getParam('titleImageFallback')}">

			 	   <img src="<content:pageImage type="pageTitle" id="layoutPageBean" path="${layoutBean.getParam('titleImageFallback')}"/>"  alt="${layoutBean.getParam('titleImageAltText')}"/>
				      <br>
</c:if>
<c:if test="${empty layoutBean.getParam('titleImageFallback')}">
			 	     <img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
</c:if>
		
			        <table width="479" border="0" cellspacing="0" cellpadding="0">
			          <tr>
			            <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
			            <td valign="top" width="303"><br>
			            	 
			              <!--Layout/intro1-->
			              <c:if test="${not empty layoutPageBean.introduction1}">
		                    <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
		                    <br>
</c:if>
		  				  <!--Layout/Intro2-->
			              <c:if test="${not empty layoutPageBean.introduction2}">
						    <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
		         		    <br>
</c:if>
<c:if test="${not empty layoutBean.getParam('additionalIntroJsp')}">
<c:set var="additionalJSP" value="${layoutBean.getParam('additionalIntroJsp')}" /> 



		                    <jsp:include page="${additionalJSP}" flush="true"/>
</c:if>
		         		</td>
		         		
		         		<td width="20"><img src="/assets/unmanaged/images/s.gif" width="3" height="1"></td>
			          </tr>
			        </table>
			      </td>
				  <td width="30" valign="top"><img src="/assets/unmanaged/images/s.gif" height="1" width="30"></td>
				  <td align="right" valign="top">
<c:if test="${not empty userProfile.messageCenterAccessibleContracts}">
					  <jsp:include page="messagecentersummary.jsp" flush="true"/>
</c:if>
				  </td>		
			    </tr>
			</table>
		</td>
	  </tr>
	</table>
	
</c:if>
	</td></tr>
	</table>
<% } %> <!-- end of not to show tpa header -->

