<!-- Author: Venkatesh Kasiraj - This JSP was added for Contact Management Project June 2010
 This page is used display Header section in Contact Information page and it contains the 
 Title, Introduction (Standard Header), Quick Report Section, Standard Tools Section and First Point of Contact 
-->
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
 
<%-- Imports --%>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%> <%-- scope="session" type="com.manulife.pension.ps.web.controller.UserProfile" --%>

<content:contentBean
  	contentId="<%=ContentConstants.BUNDLE_CONTRACT_INTRO2%>"
  	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="bundleGaContract" />
  
<content:contentBean
	contentId="<%=ContentConstants.NON_BUNDLE_CONTRACT_INTRO2%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="nonBundleGaContract" />


<% boolean showTpaHeader = false; %>   	      

<c:if test="${layoutBean.tpaHeaderUsed ==true}">
  <ps:isTpa name="<%= Constants.USERPROFILE_KEY %>" property="role" >  
     <c:if test="${empty userProfile.currentContract}">
	  <% showTpaHeader = true; %>   	      
      <jsp:include page="/WEB-INF/global/tpaheader.jsp" flush="true"/>
	  <jsp:include page="../profiles/warnings.jsp" flush="true" />
    </c:if>     	    
  </ps:isTpa>  
</c:if>

<% if ( !showTpaHeader ) { %>
	<%-- avoid duplication of header code --%> 
	<jsp:include page="standardheader.jsp" flush="true"/>
	<jsp:include page="../profiles/warnings.jsp" flush="true" />
	
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
	        <img src="/assets/unmanaged/images/s.gif" width="30" height="1">
	      </c:if>
	    </td>
	    <td width="700" valign="top">
	
		  <table width="700" border="0" cellspacing="0" cellpadding="0">
		    <tr>
		      <td width="525" ><img src="/assets/unmanaged/images/s.gif" width="525" height="1"></td>
		      <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
		      <td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="1"></td>
		    </tr>
		    <tr>
		      <td valign="top">
		      <img src="/assets/unmanaged/images/s.gif" width="525" height="23"><br>
			  <img src="/assets/unmanaged/images/s.gif" width="5" height="1">
<c:if test="${not empty layoutBean.getParam('titleImageFallback')}">
<c:set var="pageTitleFallbackImage" value="${layoutBean.getParam('titleImageFallback')}" /> 



		 	     <img src="<content:pageImage type="pageTitle" id="layoutPageBean" 
		 	     				path="${pageTitleFallbackImage}"/>"
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
		            <td valign="top" width="350">
		              <!--Layout/subheader-->
		              <c:if test="${not empty layoutPageBean.subHeader}" >
	                    <strong><content:getAttribute beanName="layoutPageBean" attribute="subHeader"/></strong>
						<br>
</c:if>
		              <!--Layout/intro1-->
		              <c:if test="${not empty layoutPageBean.introduction1}">
	                    <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
	                    <br>
</c:if>
	         		  <!--Layout/Intro2-->	         		  
<c:if test="${userProfile.currentContract.bundledGaIndicator ==true}">
							<content:getAttribute beanName="bundleGaContract" attribute="text" />
</c:if>
<c:if test="${userProfile.currentContract.bundledGaIndicator !=true}">
							<content:getAttribute beanName="nonBundleGaContract" attribute="text" />
</c:if>
	         		  	<br>	         		  
         			</td>
		            <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
		            <td width="180" valign="top">
		            </td>
		          </tr>
		        </table>
	</c:if>
		      </td>
	<c:if test="${empty param.printFriendly }" >
		      	<td valign="top" rowspan="3" class="right"><img src="/assets/unmanaged/images/s.gif" 
		      				width="1" height="22">
		        <%-- Start of Quick Reports --------------------------------------------------------------%>
		 	        <jsp:include page="standardreportlistsection.jsp" flush="true" />
		        <%-- End of Quick Reports ----%>
		        	<br/>
<c:if test="${layoutBean.getParam('suppressReportTools') !=true}">
		 	        <jsp:include page="standardreporttoolssection.jsp" flush="true" />
</c:if>
		      </td>
	</c:if>
		    </tr>
		  </table>
	    </td>
	  </tr>
      	<ps:isInternalUser name="userProfile" property="role">
      	  <tr>
      	   	<td width="5" ><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
      	  	<td valign="top" colspan="3">
    	 		<jsp:include page="firstPointOfContact.jsp" flush="true"/>
    	 		<br/>
    	 	</td>
    	 </tr>
     	</ps:isInternalUser>	  
	</table>
<% } %>
