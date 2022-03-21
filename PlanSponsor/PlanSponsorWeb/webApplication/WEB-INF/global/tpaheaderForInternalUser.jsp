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
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%> 

<c:set var="showTpaHeader" value="false"/>

<c:if test="${layoutBean.tpaHeaderUsed ==true}">
  <ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >  
    	  <c:set var="showTpaHeader" value="true"/>
   </ps:isTpa>  
</c:if>

<c:if test="${showTpaHeader eq true}">
	<jsp:include page="/WEB-INF/global/tpaheader.jsp" flush="true"/>
</c:if>

<c:if test="${showTpaHeader eq false}">
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

<c:if test="${empty param.printFriendly}">
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
      <td rowspan="2" width="300" class="welcome">
<c:if test="${layoutBean.showSelectContractLink ==true}"><div class="signOutLayer">
				<a class="signOutLayerStyle2" href="/do/home/ChangeContract/">
					<span class="welcome"><u>Select contract</u></span>
				</a>
</div></c:if>
		</td>
    </tr>
    <tr>
      <td class="welcome" valign="top"><img src="/assets/unmanaged/images/s.gif" width="30" height="1">
		TPA Name: 
		<% if (userProfile.isInternalUser()) {%>
			<strong>${sessionScope['tpaUserInfo'].firstName} ${sessionScope['tpaUserInfo'].lastName}</strong>
		<% } %>
		(<a href="/do/home/searchTPA/?first=Y"><span class="welcome"><u>change</u></span></a>)
      </td>
      <td><img src="/assets/unmanaged/images/s.gif" width="1" height="25"></td>
    </tr>
  </table>
  <table width="760" border="0" cellspacing="0" cellpadding="0" >
    <tr>
      <td class="n1" width="30" valign="top"><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
        <img src="/assets/unmanaged/images/s.gif" width="30" height="17"></td>
      <td class="n1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
      <td class="n1" width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
      <td class="n1" align="right"><a href="#" onMouseOver='self.status="Sign out"; return true' onclick="javascript:doSignOut('<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>');return false;">Sign out</a>
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
   </table>
   <script language="javascript" type="text/javascript" src="/assets/unmanaged/javascript/nav.js"></script> 
	<jsp:include page="tpanavigationbarforInternalUser.jsp" flush="true" />
</c:if>
<!-- After navbar -->

<c:if test="${empty layoutBean.getParam('suppressTPAHeaderContent')}">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    	<td valign="top" align="left">
    	<img src="/assets/unmanaged/images/s.gif" width="8" height="8" align="top"><br>
    	<img src="/assets/unmanaged/images/s.gif" width="30" height="1">
    	</td>
	<td width="700" valign="top">

	  <%-- table with 3 columns --%>
	  <table width="700" border="0" cellspacing="0" cellpadding="0">
        <tr>
		  <td width="525" ><img src="/assets/unmanaged/images/s.gif" width="525" height="1"></td>
		  <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
		  <td width="155" ><img src="/assets/unmanaged/images/s.gif" width="155" height="1"></td>
	    </tr>
		<tr>
		  <td valign="top">
<c:if test="${not empty layoutBean.getParam('titleImageFallback')}">
<c:set var="pageTitleFallbackImage" value="${layoutBean.getParam('titleImageFallback')}" /> 



		 	     <img src="<content:pageImage type="pageTitle" id="layoutPageBean" path="${pageTitleFallbackImage}"/>"
alt="${layoutBean.getParam('titleImageAltText')}">
			     <br>
</c:if>
<c:if test="${empty layoutBean.getParam('titleImageFallback')}">
		 	   <img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
			   <br>
</c:if>

			<c:if test="${empty param.printFriendly }" >
	        <table width="525" border="0" cellspacing="0" cellpadding="0">
	          <tr>
		        <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
		        <td valign="top" width="300">
	              <!--Layout/subheader-->
	              <c:if test= "${layoutPageBean.subHeader != ''}">
                    <strong><content:getAttribute beanName="layoutPageBean" attribute="subHeader"/></strong>
					
</c:if>
	              <!--Layout/intro1-->
	              <c:if test="${not empty layoutPageBean.introduction1}">
                    <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
                    <br>
</c:if>
	  			  <!--Layout/Body3 (Added with TPA Rewrite) -->
	  			  <!-- Show the body3 text for select/search contract page -->
<c:if test="${layoutBean.showSelectContractLink ==false}">
  		            <c:if test="${not empty layoutPageBean.body3}">
				      <content:getAttribute beanName="layoutPageBean" attribute="body3"/>
	         	      <br>
</c:if>
</c:if>
	  			  <!--Layout/Intro2-->
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
	         		
	         	<td width="15"><img src="/assets/unmanaged/images/s.gif" width="3" height="1"></td>
	        	<td width="180" valign="top">
<c:if test="${layoutBean.getParam('suppressAdminGuide') ==false}">
					<content:howToLinks id="howToLinks" layoutBeanName="layoutPageBean"/>	
	              	<content:contentBean contentId="<%=ContentConstants.COMMON_HOWTO_SECTION_TITLE%>"
	                                type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	                                id="HowToTitle"/>
  		            <%-- Start of How To --%>
		          
				    <ps:howToBox howToLinks="howToLinks" howToTitle="HowToTitle"/>
		            <%-- End of How To --%>
</c:if>

		        </td>
	         	<td>&nbsp;</td>
		        </tr>
		      </table>
		      </c:if>
		    </td>
		    <c:if test="${empty param.printFriendly }" >
		      <td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
		      <td valign="top" class="right">
		
		        <img src="/assets/unmanaged/images/s.gif" width="1" height="25"><br>
<c:if test="${layoutBean.getParam('suppressReportTools') !=true}">
              <jsp:include page="standardreporttoolssection.jsp" flush="true" />
</c:if>
                
<c:if test="${layoutBean.getParam('includeHelpfulHint') ==true}">
					<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer1" />
					<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer2" />
					<content:rightHandLayerDisplay beanName="layoutPageBean" layerName="layer3" />
</c:if>
		      </td>
		</c:if>
	      </tr>
		</table>
	
		<br>
  	  </td>
	  <td width="30" valign="top"><br>
	    <img src="/assets/unmanaged/images/s.gif" width="30" height="1">
	  </td>
    </tr>
  </table>
</td>
</tr>
</table>
</c:if>
</c:if>
