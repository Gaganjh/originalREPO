<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="java.util.Calendar" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

 	<table border="0" width="100%" cellspacing="0" cellpadding="0">
	<tr>
<c:if test="${applicationScope.environment.siteLocation=='usa'}" >
			<content:contentBean contentId="<%=ContentConstants.COMPANY_LOGO%>"
	                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	                     id="companyLogo"/>
	
		    <td class="logo" width="459">
	    		<img src='<content:getAttribute id="companyLogo" attribute="image.path" />' border="0">
		    </td>
</c:if>
<c:if test="${applicationScope.environment.siteLocation=='ny'}" >
			<content:contentBean contentId="<%=ContentConstants.COMPANY_LOGO_NY %>"
	                     type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	                     id="companyLogoNY"/>
		    <td class="logo" width="459"><a href="/do/home/homePageFinder/"><img src='<content:getAttribute id="companyLogoNY" attribute="image.path"/>' border="0"></a></td>
</c:if>
	    
	     
	    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="75"></td>
	    <td rowspan="2" width="300" class="welcome"><div class="signOutLayerExternal" style="text-align:right">
     <a href="/do/home/Signout/" class="signOutLayerStyle2">Sign out</a>
 		</div></td>
	</tr>
	<tr>
	    <td class="welcome"><img src="/assets/unmanaged/images/s.gif" width="30" height="1">
	      &nbsp;
	    </td>
	    <td><img src="/assets/unmanaged/images/s.gif" width="1" height="25"></td>
	</tr>
	
	<tr>
	<td colspan="3">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td class="n1" width="30" valign="top"><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
	      <img src="/assets/unmanaged/images/s.gif" width="30" height="17">
	    </td>
	    <td width="125" class="n1" valign="middle" align="left">
	       &nbsp;&nbsp;<render:date value="<%= Calendar.getInstance().getTime().toString() %>" dateStyle="l" /> 
	    </td>
	    <td class="n1" width="20"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
	    <td class="n1" align="right">
	      
	    </td>
	    <td class="n1" width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
	  </tr>
	  <tr>
	    <td class="n2"><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
	      <img src="/assets/unmanaged/images/s.gif" width="30" height="17"></td>
	     <td class="n2" colspan="3">
	    </td>
	    <td class="n2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>
	  <tr>
	    <td><img src="/assets/unmanaged/images/s.gif" width="8" height="8"><br>
	      <img src="/assets/unmanaged/images/s.gif" width="30" height="17"></td>
	     <td colspan="3">
	    </td>
	    <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  </tr>
	          
	</table>
