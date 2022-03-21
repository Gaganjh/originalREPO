<%-- Prevent the creation of a session --%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="quickreports" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
        

<%-- Imports --%>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.service.security.role.RelationshipManager" %>

<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>





<jsp:useBean id="censusVestingReportForm" scope="session" type="com.manulife.pension.ps.web.census.CensusVestingReportForm" /> 


<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_VESTING_INTRO_TPA_TEXT%>"
   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
   id="introContentTpaText"/>
   
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_VESTING_INTRO_CAR_TEXT%>"
   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
   id="introContentCarText"/>
   
<%-- avoid duplication of header code --%>
<jsp:include page="../global/standardheader.jsp" flush="true"/>

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



	 	     <img src="<content:pageImage type="pageTitle" id="layoutPageBean" path="${pageTitleFallbackImage}"/>" alt="${layoutBean.getParam('titleImageAltText')}"/>
	 	     
		      <br>
</c:if>
<c:if test="${empty layoutBean.getParam('titleImageFallback')}">
	 	     <img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
		      <br>
</c:if>


		  <c:if test="${empty param.printFriendly}">

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
				  <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/><br>
				  		<!--Intro Contact Content JIRA#131-->
<c:if test="${userProfile.currentContract.bundledGaIndicator ==true}">
						<content:getAttribute beanName="introContentCarText"	attribute="text" />
</c:if>
					<c:if test="${userProfile.currentContract.bundledGaIndicator!=true}">
						<content:getAttribute beanName="introContentTpaText" attribute="text" />
</c:if>
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
							<content:howToLinks id="howToLinks" layoutBeanName="layoutPageBean"/>
	                      	<content:contentBean contentId="<%=ContentConstants.COMMON_HOWTO_SECTION_TITLE%>"
	                                  type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	                                  id="HowToTitle"/>

	                		<%-- Edit = false, AE = false, Add = false --%>
<c:if test="${censusVestingReportForm.allowedToEdit ==false}">
<c:if test="${censusVestingReportForm.allowedToAutoEnrollment ==false}">
<%-- <c:set var="excludes" value="${ContentConstants.MISCELLANEOUS_CENSUS_AUTO_ENROLLMENT_HOWTO},${ContentConstants.MISCELLANEOUS_CENSUS_EDIT_EMPLOYEE_HOWTO},${ContentConstants.MISCELLANEOUS_CENSUS_ADD_EMPLOYEE_HOWTO}"/> --%>
<c:set var="excludes"><%=ContentConstants.MISCELLANEOUS_CENSUS_AUTO_ENROLLMENT_HOWTO%>,<%=ContentConstants.MISCELLANEOUS_CENSUS_EDIT_EMPLOYEE_HOWTO%>,<%=ContentConstants.MISCELLANEOUS_CENSUS_ADD_EMPLOYEE_HOWTO%></c:set>
								<ps:howToBox howToLinks="howToLinks" howToTitle="HowToTitle" excludes="excludes"/>
</c:if>
</c:if>
	                		<%-- Edit = false, AE = true, Add = false --%>
<c:if test="${censusVestingReportForm.allowedToEdit ==false}">
<c:if test="${censusVestingReportForm.allowedToAutoEnrollment ==true}">
<%-- <c:set var="excludes" value="${ContentConstants.MISCELLANEOUS_CENSUS_EDIT_EMPLOYEE_HOWTO},${ContentConstants.MISCELLANEOUS_CENSUS_ADD_EMPLOYEE_HOWTO}"/> --%>
<c:set var="excludes"><%=ContentConstants.MISCELLANEOUS_CENSUS_EDIT_EMPLOYEE_HOWTO%>,<%=ContentConstants.MISCELLANEOUS_CENSUS_ADD_EMPLOYEE_HOWTO%></c:set>
								<ps:howToBox howToLinks="howToLinks" howToTitle="HowToTitle" excludes="excludes"/>
</c:if>
</c:if>
		                	<%-- Edit = true, AE = false, Add = false/true --%>
<c:if test="${censusVestingReportForm.allowedToEdit ==true}">
<c:if test="${censusVestingReportForm.allowedToAutoEnrollment ==false}">
<c:if test="${censusVestingReportForm.allowedToAdd ==false}">
<%-- <c:set var="excludes" value="${ContentConstants.MISCELLANEOUS_CENSUS_AUTO_ENROLLMENT_HOWTO},${ContentConstants.MISCELLANEOUS_CENSUS_ADD_EMPLOYEE_HOWTO}"/> --%>
<c:set var="excludes"><%=ContentConstants.MISCELLANEOUS_CENSUS_AUTO_ENROLLMENT_HOWTO%>,<%=ContentConstants.MISCELLANEOUS_CENSUS_ADD_EMPLOYEE_HOWTO%></c:set>
									<ps:howToBox howToLinks="howToLinks" howToTitle="HowToTitle" excludes="excludes"/>
</c:if>
<c:if test="${censusVestingReportForm.allowedToAdd ==true}">
<%-- <c:set var="excludes" value="${ContentConstants.MISCELLANEOUS_CENSUS_AUTO_ENROLLMENT_HOWTO}"/> --%>
<c:set var="excludes"><%=ContentConstants.MISCELLANEOUS_CENSUS_AUTO_ENROLLMENT_HOWTO%></c:set>
									<ps:howToBox howToLinks="howToLinks" howToTitle="HowToTitle" excludes="excludes"/>
</c:if>
</c:if>
</c:if>
							<%-- Edit = true, AE = true, Add = false/true --%>
<c:if test="${censusVestingReportForm.allowedToEdit ==true}">
<c:if test="${censusVestingReportForm.allowedToAutoEnrollment ==true}">
<c:if test="${censusVestingReportForm.allowedToAdd ==false}">
<%-- <c:set var="excludes" value="${ContentConstants.MISCELLANEOUS_CENSUS_ADD_EMPLOYEE_HOWTO}"/> --%>
<c:set var="excludes"><%=ContentConstants.MISCELLANEOUS_CENSUS_AUTO_ENROLLMENT_HOWTO%>,<%=ContentConstants.MISCELLANEOUS_CENSUS_EDIT_EMPLOYEE_HOWTO%>,<%=ContentConstants.MISCELLANEOUS_CENSUS_ADD_EMPLOYEE_HOWTO%></c:set>
									<ps:howToBox howToLinks="howToLinks" howToTitle="HowToTitle" excludes="excludes"/>
</c:if>
<c:if test="${censusVestingReportForm.allowedToAdd ==true}">
		                			<ps:howToBox howToLinks="howToLinks" howToTitle="HowToTitle"/>
</c:if>
</c:if>
</c:if>

						</logicext:then>
					</logicext:if>
					</ps:notPermissionAccess>
					</td>
				  </tr>
				  <tr>
                 	<td height="52" valign="bottom">
<c:if test="${censusVestingReportForm.allowedToAdd ==true}">
                 	<div align="center"><input name="button" type="submit" class="button134" value="add employee" onclick="return doAdd();" /></div>
</c:if>
                  	</td>
                  </tr>
                </table>
	            </td>

	          </tr>
	        </table>
		  </c:if>
	      </td>
		  <c:if test="${empty param.printFriendly}">
	      <td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	      <td valign="top" class="right">

	        <img src="/assets/unmanaged/images/s.gif" width="1" height="25"><br>
	        <c:if test="${not censusVestingReportForm.isEditMode}">
<c:if test="${layoutBean.getParam('suppressReportList') !=true}">
 	        	<jsp:include page="../global/standardreportlistsection.jsp" flush="true" />
</c:if>
            </c:if>
<c:if test="${layoutBean.getParam('suppressReportTools') !=true}">
	 	        <jsp:include page="censusVestingReportToolsSection.jsp" flush="true" />
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
