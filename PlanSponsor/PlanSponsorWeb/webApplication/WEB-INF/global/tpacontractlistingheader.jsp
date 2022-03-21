<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="ps" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@page import="java.util.Set"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCConstants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
	
	
%> <%-- scope="session" type="com.manulife.pension.ps.web.controller.UserProfile" --%>
<content:contentBean contentId="<%=ContentConstants.WARNING_DISCARD_CHANGES%>"
                     type="<%=ContentConstants.TYPE_MESSAGE%>"
                     id="warningDiscardChanges"/>


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
</c:if>
<c:if test="${empty param.printFriendly }" >
  <table border="0" width="760" cellspacing="0" cellpadding="0">
    <tr>
<c:if test="${applicationScope.environment.siteLocation=='usa'}" >
		<content:contentBean contentId="<%=ContentConstants.COMPANY_LOGO%>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="companyLogo"/>

	    <td class="logo" width="459"><a href="/do/home/ChangeContract/"><img src='<content:getAttribute id="companyLogo" attribute="image.path" />' border="0"></a></td>
</c:if>
<c:if test="${applicationScope.environment.siteLocation=='ny'}" >
		<content:contentBean contentId="<%=ContentConstants.COMPANY_LOGO_NY %>"
                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                     id="companyLogoNY"/>
	    <td class="logo" width="459"><a href="/do/home/ChangeContract/"><img src='<content:getAttribute id="companyLogoNY" attribute="image.path"/>' border="0"></a></td>
</c:if>

      <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="75"></td>
      <td rowspan="2" width="300" class="welcome"><div class="signOutLayerExternal">&nbsp;
     <a class="signOutLayerStyle2" href="#" onMouseOver='self.status="Sign out"; return true' onclick="javascript:doSignOut('<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>');return false;">Sign out</a>
       <span class="signOutLayerStyle1"> | </span>            
       <a href="/do/profiles/editMyProfile/?loginFlow=Y" class="signOutLayerStyle2">Edit my profile</a>
		</div>
	</td>
    </tr>
    <tr>
      <td class="welcome"><img src="/assets/unmanaged/images/s.gif" width="30" height="1">
<c:if test="${layoutBean.showSelectContractLink ==true}">
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;
          <a href="/do/home/ChangeContract/"><span class="welcome"><u>Select contract</u></span></a>
</c:if>
      </td>
      <td><img src="/assets/unmanaged/images/s.gif" width="1" height="25"></td>
    </tr>
  </table>
  <table width="760" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td class="n1" width="30" valign="top"><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
        <img src="/assets/unmanaged/images/s.gif" width="30" height="17"></td>
      <td class="n1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      <td class="n1" width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
      <td class="n1" align="right"><a href="/do/home/ChangeContract/">Home</a>
      <ps:isTpaum name ="userProfile" property ="principal.role" >
      | <a href="/do/profiles/manageUsers/">Manage profiles</a>
      </ps:isTpaum>
<c:if test="${not empty userProfile.messageCenterAccessibleContracts}">
        <ps:isNotJhtc  name="<%= Constants.USERPROFILE_KEY %>" property="role" >| <a href="<%=MCConstants.DispatchUrl%>">Message center</a></ps:isNotJhtc>
</c:if>
        | <a href="/do/resources/ContactUsAction">Contact us</a>
      </td>
      <td class="n1" width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
    </tr>
    <tr>
      <td class="n2"><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
        <img src="/assets/unmanaged/images/s.gif" width="30" height="17">
      </td>
      <td class="n2" colspan="3"></td>
      <td class="n2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    </tr>
    <tr>
    	<td colspan="3" valign="top" align="left"><img src="/assets/unmanaged/images/s.gif" width="8" height="8" align="top"><br>
    	<img src="/assets/unmanaged/images/s.gif" width="30" height="17">
    	</td>
  </tr>

   </table>

  <script language="javascript" type="text/javascript" src="/assets/unmanaged/javascript/nav.js"></script>  

  <jsp:include page="tpanavigationbar.jsp" flush="true" />
</c:if>
<!-- After navbar -->
<c:if test="${empty layoutBean.getParam('suppressTPAHeaderContent')}">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
	<td width="30" valign="top">
	    <img src="/assets/unmanaged/images/s.gif" width="30" height="1">
	</td>
	
	<td width="730" valign="top">

	  <%-- table with 3 columns --%>
	  		  <%-- table with 3 columns --%>
		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			   <tr>
			      <td valign="top">
		      
<c:if test="${not empty layoutBean.getParam('titleImageFallback')}">
<c:set var="pageTitleFallbackImage" value="${layoutBean.getParam('titleImageFallback')}" /> 



			 	     <img src="<content:pageImage type='pageTitle' id='layoutPageBean'  path="${pageTitleFallbackImage}"/>"
alt="${layoutBean.getParam('titleImageAltText')}">
				     <!--  <br> -->
</c:if>
<c:if test="${empty layoutBean.getParam('titleImageFallback')}">
			 	     <img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
				      <!-- <br> -->
</c:if>
		
			        <table width="459" border="0" cellspacing="0" cellpadding="0">
			          <tr>
			            <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
			            <td valign="top" width="303"><br>
			              <!--Layout/subheader-->
			              <c:if test="${layoutPageBean.subHeader != ''}">
			              
		                    <strong><content:getAttribute beanName="layoutPageBean" attribute="subHeader"/></strong>
							<!-- <br> -->
</c:if>
			              <!--Layout/intro1-->
			              <c:if test="${not empty layoutPageBean.introduction1}">
		                    <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
		                    <br>
</c:if>
			  			  <!--Layout/Body3 (Added with TPA Rewrite) -->
			  			  <!-- Show the body3 text for select/search contract page -->
<c:if test="${layoutBean.showSelectContractLink ==false}">
<c:if test="${layoutPageBean.body3 != ''}">
						      <content:getAttribute beanName="layoutPageBean" attribute="body3"/>
			         	      <br>
</c:if>
</c:if>
<c:if test="${layoutBean.showSelectContractLink ==true}">
			  			    <c:if test="${not empty layoutPageBean.introduction2}">
						      <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
			         	      <br>
</c:if>
</c:if>
<c:if test="${not empty layoutBean.getParam('additionalIntroJsp')}">
<c:set var="additionalJSP" value="${layoutBean.getParam('additionalIntroJsp')}" /> 



		                    <jsp:include page="${additionalJSP}" flush="true"/>
</c:if>
		         		</td>
			          </tr>
			        </table>
			      </td>
				  <td align="right" valign="top">
<c:if test="${not empty userProfile.messageCenterAccessibleContracts}">
					  <jsp:include page="tpamessagecentersummary.jsp" flush="true"/>
</c:if>
				  </td>		
			    </tr>
			</table>
		</td>
	  </tr>
	</table>
</c:if>
