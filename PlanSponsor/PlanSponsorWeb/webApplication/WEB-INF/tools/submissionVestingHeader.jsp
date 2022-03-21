<%-- Prevent the creation of a session --%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>



<%-- avoid duplication of header code --%>
<jsp:include page="../global/standardheader.jsp" flush="true"/>

<c:if test="${empty param.printFriendly }" >
<table width="760" border="0" cellspacing="0" cellpadding="0">
</c:if>
<c:if test="${not empty param.printFriendly }" >
<table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>

  <tr>
    <td width="30" valign="top">
 	<c:if test="${empty param.printFriendly }" >
     	<img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br>
    </c:if>
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



	      <img src="<content:pageImage type="pageTitle" id="layoutPageBean" path="pageTitleFallbackImage"/>"
alt='${layoutBean.getParam('titleImageAltText')}'">
	      <br>
<c:if test="${empty param.printFriendly }" >
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
	            <td width="180" valign="bottom">
                      <content:contentBean contentId="<%=ContentConstants.COMMON_HOWTO_SECTION_TITLE%>"
                                  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                  id="HowToTitle"/>
					  <content:howToLinks id="howToLinks" layoutBeanName="layoutPageBean"/>
	                <%-- Start of How To --%>
					<ps:howToBox howToLinks="howToLinks" howToTitle="HowToTitle"/><br><br>
<c:if test="${layoutBean.getParam('suppressReportTools') !=true}">
			 	        <jsp:include page="/WEB-INF/tools/submissionVestingToolsSection.jsp" flush="true" />
</c:if>
	              <%-- End of How To --%>
	            </td>
	          </tr>
	        </table>
</c:if>
	      </td>
<c:if test="${empty param.printFriendly }" >
	      <td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	      <td valign="top" class="right">
 	        <p>
				<img src="/assets/unmanaged/images/s.gif" alt="" height="23" width="191"><br>
				<report:submissionLinks profile="userProfile"/>
			</p>
	      </td>
</c:if>
	    </tr>
	  </table>
      <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
    </td>
  </tr>

