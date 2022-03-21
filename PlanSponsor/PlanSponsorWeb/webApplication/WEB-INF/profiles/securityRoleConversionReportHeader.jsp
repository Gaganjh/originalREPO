<%-- Prevent the creation of a session --%>
 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports" %>

<%-- Imports --%>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>



			 
<jsp:useBean id="securityRoleConversionReportForm" scope="session" type="com.manulife.pension.ps.web.profiles.SecurityRoleConversionReportForm" />



 
<%-- avoid duplication of header code --%> 
<c:if test="${empty param.printFriendly }" >
<jsp:include page="../global/nonavigationsecureheader.jsp" flush="true"/>
</c:if>
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
<table width="760" border="0" cellspacing="0" cellpadding="0">
</c:if>
<c:if test="${not empty param.printFriendly }" >
<table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>

  <tr>
    <td width="30" valign="top"></td>
    <td width="700" valign="top">

	  <table width="700" border="0" cellspacing="0" cellpadding="0">
	    <tr>
	      <td width="500" ><img src="/assets/unmanaged/images/s.gif" width="500" height="1"></td>
	      <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	      <td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
	    </tr>
	    <tr>
	      <td valign="top">
	      <img src="/assets/unmanaged/images/s.gif" width="500" height="1">
	      <img src="/assets/unmanaged/images/s.gif" width="5" height="1">
	      
<c:if test="${not empty layoutBean.getParam('titleImageFallback')}">
<c:set var="pageTitleFallbackImage" value="${layoutBean.getParam('titleImageFallback')}" /> 



	 	     <img src="<content:pageImage type="pageTitle" id="layoutPageBean" path="${pageTitleFallbackImage}"/>" alt="${layoutBean.getParam('titleImageAltText')}">
		      <br>
</c:if>
<c:if test="${empty layoutBean.getParam('titleImageFallback')}">
	 	     <img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
		      <br>
</c:if>

		  
		  <c:if test="${empty param.printFriendly }" >

	        <table width="500" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
	            <td valign="top"><br>
	              <!--Layout/intro1-->
	              <c:if test="${not empty layoutPageBean.introduction1}">
                    <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
                    <br>
</c:if>
  				  <!--Layout/Intro2-->
				  <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
         		</td>
	            <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	            <td width="180" valign="top">
	            <table>
	              <tr>
	                <td>
							<content:howToLinks id="howToLinks" layoutBeanName="layoutPageBean"/>
	                      	<content:contentBean contentId="<%=ContentConstants.COMMON_HOWTO_SECTION_TITLE%>"
	                                  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	                                  id="HowToTitle"/>
					</td>
				  </tr>
                </table>     
	            </td>
	            
	          </tr>
	        </table>
		  </c:if>
	      </td>
		  <c:if test="${empty param.printFriendly }" >
	      <td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	      <td valign="top" class="right">
	
	        <img src="/assets/unmanaged/images/s.gif" width="1" height="25"><br>
<c:if test="${layoutBean.getParam('suppressReportList') !=true}">
 	        	<jsp:include page="../global/standardreporttoolssection.jsp" flush="true" />
</c:if>
	      </td>
		</c:if>
	    </tr>
	  </table>
      <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
    </td>
    <td width="15" valign="top"><img src="/assets/unmanaged/images/s.gif" width="15" height="1"></td>
  </tr>
</table>
