<%@page import="com.manulife.pension.ps.web.home.RIRSHotLinkUtil"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 
<%-- Imports --%>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<content:contentBean contentId="<%=ContentConstants.RIRS_SMART_LINK_LAYER%>" 
   type="<%=ContentConstants.TYPE_LAYER%>" beanName="rirsSmartLink"/>
   
<script type="text/javascript">
 	 function submitForm() {
 		 document.rirsLogin.submit();
 	 }
</script>	

<c:set var="rirsParam" value="${null}"/>

<c:if test="${not userProfile.internalUser}">
  <c:set var="rirsParam" value="<%=RIRSHotLinkUtil.generateRIRHotLinkParam(userProfile) %>"/>
  <c:set var="rirsURL" value="<%=RIRSHotLinkUtil.getRIRSHotLinkURL() %>"/>
  <c:if test="${rirsParam != null && rirsURL != null}">
  	<form name="rirsLogin" action="${rirsURL}" method="POST" target="_rirs_Window">
   	   <input type="hidden" name="Val" value="${rirsParam}">
   	</form>
   </c:if>
</c:if>

  <table width="180" border="0" cellspacing="0" cellpadding="0" class="box">
  <tr> 
    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    <td width="236"><img src="/assets/unmanaged/images/s.gif" width="178" height="1"></td>
    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
  </tr>
  <tr class="tablehead"> 
    <td colspan="3" class="tableheadTD1"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td><b style="color:#ffffff"><content:getAttribute beanName="rirsSmartLink" attribute="title"/></b></td>
          <td align="right"></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr> 
    <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td class="boxbody" >
	    <c:choose>	      
	    <c:when test="${userProfile.internalUser || rirsParam == null || rirsURL == null}">
	      <content:getAttribute beanName="rirsSmartLink" attribute="text">
	        <content:param>#</content:param>
	      </content:getAttribute>
	    </c:when>
	    <c:otherwise>
	       <content:getAttribute beanName="rirsSmartLink" attribute="text">
	        <content:param>javascript:submitForm()</content:param>
	      </content:getAttribute>
	    </c:otherwise>
	    </c:choose>
	</td>	
    <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
  </tr>
  <tr> 
    <td colspan="3"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
          <td><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
          <td rowspan="2"  width="5"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
        </tr>
        <tr>  
          <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
      </table></td>
  </tr>
</table>
