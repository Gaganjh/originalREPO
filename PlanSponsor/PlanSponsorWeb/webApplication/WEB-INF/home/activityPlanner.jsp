<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
 
<%-- Imports --%>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>

<%-- This jsp includes the following CMA content --%>
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_RETIREMENT_RESOURCE_CENTER%>"
                     type="<%=ContentConstants.TYPE_STANDALONE_TOOL%>"
                     id="retirementResourceCenter"/>
<c:if test="${not empty retirementResourceCenter}">

  <table width="180" border="0" cellspacing="0" cellpadding="0" class="box">
  <tr> 
    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
    <td width="236"><img src="/assets/unmanaged/images/s.gif" width="178" height="1"></td>
    <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
  </tr>
  <tr class="tablehead"> 
    <td colspan="3" class="tableheadTD1"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td><b style="color:#ffffff"><%-- CMA Managed --%><content:getAttribute id="retirementResourceCenter" attribute="title"/></b></td>
          <td align="right"></td>
        </tr>
      </table></td>
  </tr>
  <tr> 
    <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td class="boxbody" >
	    	<a href="#" onclick="ActivityPlannerWindow('<content:getAttribute id="retirementResourceCenter" attribute="url"/>');return false;">
					<content:getAttribute id="retirementResourceCenter" attribute="linkText"/>
			</a>
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
</c:if>
                      
 
