<%-- Prevent the creation of a session --%>
 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="quickreports" %>


<%-- Imports --%>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>


					
<%-- avoid duplication of header code --%> 
<jsp:include page="standardheader.jsp" flush="true"/>

<table width="760" border="0" cellspacing="0" cellpadding="0">

  <tr>
    <td width="23%" valign="top">
        <img src="/assets/unmanaged/images/body_corner.gif" width="8" height="8"><br><img src="/assets/unmanaged/images/s.gif" width="30" height="1">
    </td>
   	<td width="10%">
	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="350" ><img src="/assets/unmanaged/images/s.gif" width="350" height="20"></td>
          <td width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="20"></td>
          <td width="180" ><img src="/assets/unmanaged/images/s.gif" width="180" height="20"></td>
        </tr>
        <tr>
          <td valign="top">		    
<c:if test="${not empty  layoutBean.getParam('titleImageFallback')}">
<c:set var="pageTitleFallbackImage" value="${ layoutBean.getParam('titleImageFallback')}" /> 


		<img src="<content:pageImage type="pageTitle" id="layoutPageBean" path="${pageTitleFallbackImage}"/>"
alt="${layoutBean.getParam('titleImageAltText')}">
		      <br>
</c:if>
<c:if test="${empty  layoutBean.getParam('titleImageFallback')}">
	 	      <img src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">	
			   <!--Layout/intro1-->
			    <c:if test="${not empty layoutPageBean.introduction1}">
                    <content:getAttribute beanName="layoutPageBean" attribute="introduction1"/>
                    <br>
</c:if>
			   
		        <br>
</c:if>
			<td valign ="top">
			  <table>
				<tr><td valign ="top">
					<content:rightHandLayerDisplay layerName="layer1" beanName="layoutPageBean"/>
					<img src="/assets/unmanaged/images/s.gif" width="5" height="5">
					</td>
					<td width="1"><img src="/assets/unmanaged/images/s.gif" width="5" height="1"></td>
					<td valign ="top">
				  	<content:rightHandLayerDisplay layerName="layer2" beanName="layoutPageBean"/>
					<img src="/assets/unmanaged/images/s.gif" width="5" height="5">
					</td>
				</tr>
			  </table> 
			</td>				

		    <table width="10" border="0" cellspacing="0" cellpadding="0">
		  	  <tr><td><img src="/assets/unmanaged/images/s.gif" width="300" height="5"></td></tr>
          	  <tr>
	            <td valign="top">            
	            
  				  <!--Layout/Intro2-->				 
	              <c:if test="${not empty layoutPageBean.introduction2}">
				    <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
         		    <br>
					
<c:if test="${not empty  layoutBean.getParam('additionalIntroJsp')}">
<c:set var="additionalJSP" value="${ layoutBean.getParam('additionalIntroJsp')}" /> 



                      <jsp:include page="${additionalJSP}" flush="true"/>
</c:if>
                    <br>  
</c:if>
      			  </tr>
	        </table>
          </td>
        </tr>
      </table>
    </td>
    <td width="23%"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
  </tr>
</table>  


