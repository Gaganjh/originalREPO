<%-- Prevent the creation of a session --%>
 
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="quickreports" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%> 
        
<%@page import="com.manulife.pension.ps.web.pagelayout.LayoutBean" %>
<%-- Imports --%>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>

<%-- avoid duplication of header code --%> 
<%@page import="com.manulife.pension.ps.web.census.EmployeeStatusHistoryReportForm"%>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@page import="com.manulife.pension.ps.web.census.util.VestingHelper"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<jsp:include page="../global/standardheader.jsp" flush="true"/>


<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
LayoutBean layoutBeanObj = (LayoutBean)request.getAttribute("layoutBean");
%>





<c:if test="${empty param.printFriendly }" >
<table width="760" border="0" cellspacing="0" cellpadding="0">
</c:if>
<c:if test="${not empty param.printFriendly }" >
<table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if>

  <tr>
    <td width="30" valign="top">
      <c:if test="${empty param.printFriendly }" >
        <img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br><img src="/assets/unmanaged/images/s.gif" width="30" height="1">
      </c:if>
    </td>
    <td width="700" valign="top">

	  <table width="700" border="0" cellspacing="0" cellpadding="0">
	    <tr>
	      <td width="500" ><img src="/assets/unmanaged/images/s.gif" width="500" height="1"></td>
	      <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	      <td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
	    </tr>
	    <tr>
	      <td valign="top">
	      <img src="/assets/unmanaged/images/s.gif" width="500" height="23"><br>
	      <img src="/assets/unmanaged/images/s.gif" width="5" height="1">

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
<c:set var="href" value="#"/>
<c:set var="onclick" value="return true;" />
<%

	String href=(String)pageContext.getAttribute("href");
	String onclick=(String)pageContext.getAttribute("onclick");
%>
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
	                <ps:notPermissionAccess permissions="SELA">
					<logicext:if name="<%= Constants.USERPROFILE_KEY%>" property="currentContract.mta" op="equal" value="false">
					<logicext:and name="<%= Constants.USERPROFILE_KEY%>" property="currentContract.contractAllocated" op="notEqual" value="false"/>

						<logicext:then>
			      <%
			      String reportToolsLinksParamNameAsIds = "reportToolsLinks";
			      pageContext.setAttribute("paramNameAsIds", layoutBeanObj.getParamAsIds(reportToolsLinksParamNameAsIds));
			      %>
<c:forEach items="${paramNameAsIds}" var="contentId" ><%-- type="java.lang.Integer" --%>
<c:set var="temp" value= "${contentId}" />
<%Integer contentId =(Integer)pageContext.getAttribute("temp"); %>


			
			          <content:contentBean contentId="<%=contentId.intValue()%>"
			                               type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
			                               id="contentBean" override="true"/>
	                             		          
<c:set var="howToReadReportKey" value="${layoutBean.getParam('howToReadReportLink')}" /> 
<%String howToReadReportKey=(String)pageContext.getAttribute("howToReadReportKey"); %>


			              <%
			              		href = "javascript:doHowTo('" + howToReadReportKey+ "','r')";
			              		onclick = "return true;";
			              %>

			          <tr>
			            <td width="17">
						  <a href="<%=href %>" onclick="<%=onclick %>">
						    <content:image id="contentBean" contentfile="image"/></a>
						  </td>
			            <td width="5"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
			            <td>
			              <a href="<%=href %>" onclick="<%=onclick %>">
			                <content:getAttribute id="contentBean" attribute="text"/>
			              </a>
			            </td>
			          </tr>          
</c:forEach>
						</logicext:then>
					</logicext:if>
					</ps:notPermissionAccess>
					</td>
				  </tr>
				  <tr>
                 	<td height="52" valign="bottom">
                  	</td>
                  </tr>
                </table>
	            </td>

	          </tr>
	        </table>
		  </c:if>
	      </td>
	    </tr>
	  </table>
      <img src="/assets/unmanaged/images/s.gif" width="1" height="20"><br>
    </td>
    <td width="15" valign="top"><img src="/assets/unmanaged/images/s.gif" width="15" height="1"></td>
  </tr>
</table>


