<%-- Prevent the creation of a session --%>
 
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="quickreports" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<%-- Imports --%>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.census.DeferralReportForm" %>

<%-- avoid duplication of header code --%> 
<jsp:include page="../global/standardheader.jsp" flush="true"/>

<jsp:useBean id="deferralReportForm" scope="session" type="com.manulife.pension.ps.web.census.DeferralReportForm" />




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

	        <table width="500" border="0" cellspacing="0" cellpadding="0">
	          <tr>
	            <td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
	            <td valign="top"><br>
	              <!--Layout/intro1-->
					<c:if test="${not empty layoutPageBean.introduction1}">
                    	<content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
</c:if>
  				  <!--Layout/Intro2-->
				  <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
         		  <!--<br>-->
<c:if test="${not empty layoutBean.getParam('additionalIntroJsp')}">
<c:set var="additionalJSP" value="${layoutBean.getParam('additionalIntroJsp')}" /> 



                    <jsp:include page="${additionalJSP}" flush="true"/>
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

<c:if test="${deferralReportForm.allowedToEdit ==false}">
<c:set var="excludes" value="${ContentConstants.MISCELLANEOUS_CENSUS_AUTO_ENROLLMENT_HOWTO},${ContentConstants.MISCELLANEOUS_CENSUS_EDIT_EMPLOYEE_HOWTO },${ ContentConstants.MISCELLANEOUS_CENSUS_ADD_EMPLOYEE_HOWTO}"/>
                            <ps:howToBox howToLinks="howToLinks" howToTitle="HowToTitle" excludes="excludes"/>  		
</c:if>
<c:if test="${deferralReportForm.allowedToEdit ==true}">
                          	<ps:howToBox howToLinks="howToLinks" howToTitle="HowToTitle"/>  
</c:if>
                        </logicext:then>
                      </logicext:if>
                    </ps:notPermissionAccess>    
                  </td>
                </tr>	 
                <tr>
                  <td height="52" valign="bottom"><div align="center">
<c:if test="${deferralReportForm.allowedToEdit ==true}">
                 	    <div align="center"><input name="button" type="submit" class="button134" value="add employee" onclick="return doAdd();" /></div>
</c:if>
                  </div></td>
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
 	        	<jsp:include page="../global/standardreportlistsection.jsp" flush="true" />
</c:if>
<c:if test="${layoutBean.getParam('suppressReportTools') !=true}">
	 	        <jsp:include page="deferralReportToolsSection.jsp" flush="true" />
</c:if>

	      </td>
</c:if>
	</tr>
	
	<!-- Leave a space -->
	<tr>    
    <td>&nbsp;</td>    
  </tr>
  

	  </table>
  </td>
 </tr>
</table>





