<%-- Prevent the creation of a session --%>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@page import="com.manulife.pension.ps.web.messagecenter.util.MCEnvironment"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.MCConstants"%>
<%@ page import="java.util.Calendar" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib" prefix="content" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="ps" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%> <%-- scope="session" type="UserProfile" --%>

<c:if test="${empty param.printFriendly }" >

<table border="0" width="760" cellspacing="0" cellpadding="0">
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

 	   <c:set var="signOutStyleClass" value="signOutLayerExternal"/>
 	   <c:if test="${userProfile.role.internalUser}">
 	   		<c:set var="signOutStyleClass" value="signOutLayerInternal"/>
 	   </c:if>
    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="75"></td>
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
          <a href="/do/home/homePageFinder/">Home</a>
	</ps:isNotTpa>
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
      <%}%>
	  	<ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
        | <a href="/do/resources/ContactUsAction">Contact us</a>
        </ps:isTpa>
    </td>
    <td class="n1" width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
  </tr>
  <tr>
    <td class="n2"><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
      <img src="/assets/unmanaged/images/s.gif" width="30" height="17"></td>
    <td class="n2" colspan="3"></td>
    <td class="n2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
  </tr>
</table>

<c:if test="${userProfile.currentContract.definedBenefitContract ==true}">
     <jsp:include page="definedbenefitnavigationbar.jsp" flush="true" />
</c:if>

<c:if test="${userProfile.currentContract.definedBenefitContract !=true}">
<jsp:include page="navigationbar.jsp" flush="true" />
</c:if>
</c:if>

<!- After navbar -->

<c:if test="${empty param.printFriendly }" >
<table width="760" border="0" cellspacing="0" cellpadding="0">
</c:if>
<c:if test="${not empty param.printFriendly }" >
<table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>

  <tr>
    <td width="30" valign="top">
      <c:if test="${empty param.printFriendly }" >
        <img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br><img src="/assets/unmanaged/images/s.gif" width="30" height="1">
      </c:if>
    </td>
    <td width="730" valign="top">

	  <table width="730" border="0" cellspacing="0" cellpadding="0">
		<tr><td colspan="5">&nbsp;</td></tr>
	    <tr>
	      <td valign="top"><img src="/assets/unmanaged/images/s.gif" width="5" height="1">
<c:if test="${not empty layoutBean.getParam('titleImageFallback')}">
<c:set var="pageTitleFallbackImage" value="${layoutBean.getParam('titleImageFallback')}" /> 



	 	   <img src="<content:pageImage type="pageTitle" id="layoutPageBean" path='${pageTitleFallbackImage}'/>"
alt='${layoutBean.getParam('titleImageAltText')}'/>
		     <br>
</c:if>
<c:if test="${empty layoutBean.getParam('titleImageFallback')}">
	 	     <img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
		      <br>
</c:if>
		  </td>
		  <td width="10"><img src="/assets/unmanaged/images/s.gif" width="10" height="1"></td>
		  <td width="180" valign="top" align="center">
		  	<content:howToLinks id="howToLinks" layoutBeanName="layoutPageBean"/>
            <!--  Want to see more of the website?  -->
            <content:contentBean contentId="<%=ContentConstants.VIEW_DEMO_BOX_TITLE%>"
	                                  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	                                  id="HowToTitle"/>
            <%-- Start of How To --%>
			<ps:howToBox howToLinks="howToLinks" howToTitle="HowToTitle"/>
 		    <%-- End of How To --%>
		  </td>
	      <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
		  <td width="170" valign="top" align="left">
		    <table class="beigeBox" cellSpacing="0" cellPadding="0" width="170" border="0">
               <tr>
              	<td valign="top" align="left"><img height="5" src="/assets/unmanaged/images/box_ul_corner.gif" width="5"></td>
              	<td width="160"><img src="/assets/unmanaged/images/s.gif" height="1" width="160"></td>
                <td width="5"><img src="/assets/unmanaged/images/s.gif" height="1" width="5"></td>
              </tr>
              <tr>
                <td colspan="3" class="beigeBoxTD">
	                <!--Layout/intro2-->
	                <!-- <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/> -->
	                If you have any questions regarding the information shown here please
	                contact your John Hancock USA Client Account Representative.
	            </td>
              </tr>
              <tr>
              	<td width="5"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              	<td width="160"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
                <td align="right"><img height="5" src="/assets/unmanaged/images/box_lr_corner_E9E2C3.gif" width="5"></td>
              </tr>
            </table>
		  </td>
		</tr>
		<tr>
          <td width="330" valign="top" height="100"><br>
	          <!--Layout/intro1-->
	          <c:if test="${not empty layoutPageBean.introduction1}">
	            <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
	            <br>
</c:if>
	 	  </td>
	      <td width="10"><img src="/assets/unmanaged/images/s.gif" width="10" height="1"></td>
	      <td width="180"></td>
	      <td colspan="2" style="padding-left:20px;">
	      <ps:isExternal name="userProfile" property="role">
	      	        <c:if test="${not empty requestScope.mcModel}">
		        <%---------------  Start of Message Center box---------------------%>
		        <jsp:include page="../home/messageCenterBox.jsp" flush="true"/>
		        <%---------------- End of message center box -----------------------%> 
		    </c:if>
		    
		  </ps:isExternal>
	      </td>
	    </tr>
	    <tr>
  		  <td colspan="5" align="left">
  		  	<br><br>
  			<b><render:text property="userProfile.currentContract.companyName" length="30"/></b>
<b> | </b>Contract: ${userProfile.currentContract.contractNumber}
<c:if test="${userProfile.multipleContracts ==true}">
    			(<a href="/do/home/ChangeContract/"><u>change</u></a>)
</c:if>
<c:if test="${userProfile.multipleContracts ==false}">
      		<ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >
    			(<a href="/do/home/ChangeContract/"><u>change</u></a>)
      		</ps:isTpa>
</c:if>
			<br><br>
		  </td>
		</tr>
	  </table>
	</td>
  </tr>

</table>




