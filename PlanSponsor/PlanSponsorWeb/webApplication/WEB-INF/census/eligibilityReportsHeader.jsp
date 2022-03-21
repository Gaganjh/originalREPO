<%-- Prevent the creation of a session --%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>

<%@ taglib uri="manulife/tags/content" prefix="content"%>
<%@ taglib uri="manulife/tags/ps" prefix="ps"%>
<%@ taglib uri="manulife/tags/ps" prefix="quickreports"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%-- Constant Files used--%>
<un:useConstants var="contentConstants"
	             className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="constants"
	             className="com.manulife.pension.ps.web.Constants" />
<un:useConstants var="censusConstant"
	             className="com.manulife.pension.ps.web.census.CensusConstants" />

<jsp:include page="../global/standardheader.jsp" flush="true" />

<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>




<c:if test="${empty param.printFriendly}">
    <table width="760" border="0" cellspacing="0" cellpadding="0">
</c:if>

<c:if test="${not empty param.printFriendly}">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>

<tr>

<td width="30" valign="top">
<c:if test="${empty param.printFriendly}">
        <img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br><img src="/assets/unmanaged/images/s.gif" width="30" height="1">
      </c:if>
</td>
					
<td width="700" valign="top">
<table width="700" border="0" cellspacing="0" cellpadding="0">
<tr>
<td width="500"><img src="/assets/unmanaged/images/s.gif" width="500" height="1"></td>
<td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
<td width="180"><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
</tr>
<tr>
<td valign="top"><img src="/assets/unmanaged/images/s.gif" width="500" height="23"><br>
	             <img src="/assets/unmanaged/images/s.gif" width="5" height="1">

<c:if test="${not empty layoutBean.getParam('titleImageFallback')}">
<c:set var="pageTitleFallbackImage" value="${layoutBean.getParam('titleImageFallback')}" /> 

		<img src="<content:pageImage type="pageTitle" 
		     id="layoutPageBean" path="${pageTitleFallbackImage}"/>"
alt="${layoutBean.getParam('titleImageAltText')}">
		<br>
</c:if>

<c:if test="${empty layoutBean.getParam('titleImageFallback')}">
		<img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
		<br>
</c:if>

<c:if test="${empty param.printFriendly}">
<table width="500" border="0" cellspacing="0" cellpadding="0">
<tr>
<td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
<td valign="top">
<br>
<!--Layout/intro1--> 
<c:if test="${not empty layoutPageBean.introduction1}">
	  <content:getAttribute beanName="layoutPageBean" attribute="introduction1" /><br>
</c:if>

<!--Layout/Intro2--> 
<content:getAttribute beanName="layoutPageBean" attribute="introduction2" /><br>

<c:if test="${not empty layoutBean.getParam('additionalIntroJsp')}">
<c:set var="additionalJSP" value="${layoutBean.getParam('additionalIntroJsp')}" /> 

	<jsp:include page="additionalJSP" flush="true" />
</c:if>
</td>

<td width="20">
<img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>

</tr>
</table>
</c:if>
</td>

<c:if test="${empty param.printFriendly}">
<td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
<td valign="top" class="right"><img src="/assets/unmanaged/images/s.gif" width="1" height="25"><br>
<c:if test="${layoutBean.getParam('suppressReportList') !=true}">
       <jsp:include page="../global/standardreportlistsection.jsp" flush="true" />
</c:if>
<c:set var="href" value="#"/>
<c:set var="onclick" value="return true;" />
								

</td>
</c:if>
</tr>
</table>
<img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
</td>
<td width="15" valign="top">
<img src="/assets/unmanaged/images/s.gif" width="15" height="1"></td>
</tr>
</table>
