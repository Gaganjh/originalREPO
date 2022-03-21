<%@page import="com.manulife.pension.ps.web.Constants"%>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<%@page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>
<%@page import="com.manulife.pension.ps.web.census.EmployeeSnapshotProperties"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE); %>
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_VESTING_SECTION%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="VestingSection" />

<form:hidden path="expandVesting"/>
<c:choose >
  <c:when test="${ param.printFriendly==true || param.expandVesting}">
    <c:set var="expandStyle" value="DISPLAY:block"/>
    <c:set var="expandIcon" value="/assets/unmanaged/images/minus_icon.gif"/>    
  </c:when>
  <c:otherwise>
    <c:set var="expandStyle" value="DISPLAY:none"/>
    <c:set var="expandIcon" value="/assets/unmanaged/images/plus_icon.gif"/>
</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${empty param.printFriendly}">
	<table class=box cellSpacing=0 cellPadding=0
		width="500" border="0">
		<tbody>
			<tr class="tablehead">
				<td class="tableheadTD1" colSpan="3">
				    <a href="javascript:toggleSection('vesting', 'headerVesting','expandVesting', 'expandVestingIcon')">
				       <img id="expandVestingIcon" src="<c:out value='${expandIcon}'/>" border="0"/></a>
				    &nbsp;
					<b><content:getAttribute id="VestingSection" beanName="VestingSection" attribute="text"/> </b>
				</td>
				<td id="headerVesting" class="tableheadTD">
				   &nbsp;
				</td>
			</tr>
		</tbody>
	</table>
	</c:when>
	<c:otherwise>
		<table class=box cellSpacing=0 cellPadding=0
		width="500" border="0">
		<tbody>
			<tr class="tablehead">
				<td class="tableheadTD1" colSpan="3">
					<b><content:getAttribute id="VestingSection" attribute="text"/></b>
				</td>
			</tr>
		</tbody>
	</table>	
	</c:otherwise>
</c:choose>

<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>
<div id="vesting" style="<c:out value='${expandStyle}'/>">
<c:remove var="expandStyle"/>
	<table id=vestingTable cellSpacing=0 cellPadding=0 width=500 border=0 class=greyborder>
		<tbody>

			 <jsp:include flush="true" page="vestingPercentages.jsp">
			   <jsp:param name="width" value="200"/>
			</jsp:include> 

			<tr>
	              <td height="1" colspan="7" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	             
	              <c:if test="${userProfile.showCensusHistoryValue}">
	              <td height="1" colspan="2" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	              </c:if>
	        </tr>
		</tbody>
	</table>
</div>
