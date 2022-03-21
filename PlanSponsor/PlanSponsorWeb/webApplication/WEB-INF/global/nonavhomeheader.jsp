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


    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="75"></td>
    <td rowspan="2" width="300" class="welcome"></td>
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
    <td width="30" valign="top">
     	<img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br>
    </td>
    <td width="700" valign="top">

	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
	    <tr>
	      <td width="71%" ><img src="/assets/unmanaged/images/s.gif" width="500" height="1"></td>
	      <td width="3%"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	      <td width="26%" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
	    </tr>
	    <tr>
	      <td valign="top">
	      <img src="/assets/unmanaged/images/s.gif" width="500" height="23"><br>
	      <img src="/assets/unmanaged/images/s.gif" width="5" height="1">

<c:set var="pageTitleFallbackImage" value="${layoutBean.getParam('titleImageFallback')}" /> 



	      <img src='<content:pageImage type="pageTitle" id="layoutPageBean" path="${pageTitleFallbackImage}"/>' 
alt="${layoutBean.getParam('titleImageAltText')}">
	      <br>
	        <table width="500" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
	            <td width="295" valign="top"><br>
	              <!--Layout/intro1-->
                  <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
                    <br>
  				  <!--Layout/Intro2-->
				  <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
         		  </td>
	            <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	            <td width="180"><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
	          </tr>
	        </table>
	      </td>
	    </tr>
	  </table>
      <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
    </td>
  </tr>

</c:if>


