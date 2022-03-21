<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="content" uri="manulife/tags/content"%>
<%@ taglib prefix="ps" uri="manulife/tags/ps"%>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<%-- Constants --%>
<un:useConstants var="constants" className="com.manulife.pension.ps.web.Constants"/>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>


<c:set var="showTpaHeader" value="false"/>
<c:set var="user" value="${sessionScope[userProfile]}" />

<c:if test="${layoutBean.tpaHeaderUsed}">
  <ps:isTpa name="${constants.USERPROFILE_KEY}" property="role">
     <c:if test="${empty user.currentContract}">
      <c:set var="showTpaHeader" value="true"/>
    </c:if>
  </ps:isTpa>
</c:if>

<jsp:include page="standardheader.jsp" flush="true"/>

<c:if test="${not showTpaHeader}" >
  <table border="0" cellpadding="0" cellspacing="0" width="760">
    <tr>
      <c:if test="${!displayRules.printFriendly}">
        <td valign="top" width="30">
            <img src="/assets/unmanaged/images/body_corner.gif" height="8" width="8"><br><img src="/assets/unmanaged/images/s.gif" height="1" width="30">
        </td>
      </c:if>
      <c:if test="${displayRules.printFriendly}">
        <td valign="top" width="30">
          <img src="/assets/unmanaged/images/s.gif" height="1" width="30">
        </td>
      </c:if>

      <td valign="top" width="700">

	    <table border="0" cellpadding="0" cellspacing="0" width="700">

	      <tr>
	        <td width="500"><img src="/assets/unmanaged/images/s.gif" height="1" width="500"></td>
	        <td width="20"><img src="/assets/unmanaged/images/s.gif" height="1" width="20"></td>
            <td width="180"><img src="/assets/unmanaged/images/s.gif" height="1" width="180"></td>
          </tr>
	      <tr>
	        <td valign="top"><img src="/assets/unmanaged/images/s.gif" height="23" width="500"><br>
	        <img src="/assets/unmanaged/images/s.gif" height="1" width="5">
                <c:choose>
                  <c:when test="${not empty layoutBean.getParam('titleImageFallback')}">
                    <c:set var="pageTitleFallbackImage" value="${layoutBean.getParam('titleImageFallback')}"/>
                    <img src="<content:pageImage type="pageTitle" id="layoutPageBean" path="${pageTitleFallbackImage}"/>" alt="${layoutBean.getParam('titleImageAltText')}">
                  </c:when>
                  <c:otherwise>
                    <img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
                  </c:otherwise>
                </c:choose>

		        <br>
		  
              <c:if test="${not param.printFriendly}">
	          <table border="0" cellpadding="0" cellspacing="0" width="500">
	            <tr>
	              <td width="5"><img src="/assets/unmanaged/images/s.gif" height="1" width="5"></td>
	              <td width="300" valign="top">
                    <!-- Layout/subheader -->
                    <c:if test="${not empty layoutPageBean.subHeader}">
                      <strong><content:getAttribute beanName="layoutPageBean" attribute="subHeader" /></strong>
                    </c:if>
                    <!-- Layout/intro1 -->
                    <c:if test="${not empty layoutPageBean.introduction1}">
                      <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
                    </c:if>
                    <!--Layout/Body3 (Added with TPA Rewrite) -->
                    <c:choose>
                      <c:when test="${not layoutBean.showSelectContractLink}">
                        <!-- Show the body3 text for select/search contract page -->
                        <c:if test="${not empty layoutPageBean.body3}">
                          <content:getAttribute beanName="layoutPageBean" attribute="body3"/>
                        </c:if>
                      </c:when>
                      <c:otherwise>
                        <!--Layout/Intro2-->
                        <c:if test="${not empty layoutPageBean.introduction2}">
                          <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
                        </c:if>
                      </c:otherwise>
                    </c:choose>
                    <c:if test="${layoutBean.getParam('additionalIntroJsp')}">
                      <c:set var="additionalJSP" value="${layoutBean.getParam('additionalIntroJsp')}"/>
                      <jsp:include page="${additionalJSP}" flush="true"/>
                    </c:if>
                  </td>
	              <td width="20"><img src="/assets/unmanaged/images/s.gif" height="1" width="20"></td>
                </tr>
	          </table>
	          </c:if> <%-- not printFriendly --%>
	        </td>

	        <td><img src="/assets/unmanaged/images/s.gif" height="1" width="20"></td>
	        <td class="right" valign="top">
	
	          <img src="/assets/unmanaged/images/s.gif" height="25" width="1"><img src="/assets/unmanaged/images/s.gif" height="5" width="1"><br>
	        
              <c:if test="${not param.printFriendly}">
<c:if test="${layoutBean.getParam('suppressReportTools') !=true}">
	 	          <jsp:include page="standardreporttoolssection.jsp" flush="true" />
</c:if>
              </c:if>
	        
            </td>
          </tr>
          <tr>
            <td align="right"><br>
              <img src="/assets/unmanaged/images/box_lr_corner_E9E2C3.gif" height="5" width="5"></td>
          </tr>

        </table>
      </td>
    </tr>
  </table>
</c:if>
