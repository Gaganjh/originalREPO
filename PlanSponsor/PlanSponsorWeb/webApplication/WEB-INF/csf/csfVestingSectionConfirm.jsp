<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
       
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfForm" %>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tbody>
	<%-- Vesting will be --%>
<c:if test="${csfForm.displayNoticeGeneration ==true}">
		<tr class="datacell1">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
		<tr class="datacell2">
</c:if>
		<td width="375"><content:getAttribute beanName="vestingLabel" attribute="text" /></td>
		<td width="20"><ps:fieldHilight name="vestingPercentagesMethod" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
		<td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
<c:if test="${userProfile.internalUser ==true}">
			<td colspan="2" width="302">&nbsp;
				<form:select path="vestingPercentagesMethod" disabled="true">
					<form:option value="NA">Service not available</form:option>
					<form:option value="JHC">Calculated by John Hancock</form:option>				
					<form:option value="TPAP">Submitted electronically by TPA</form:option>
</form:select>
			</td>
</c:if>
<c:if test="${userProfile.internalUser !=true}">
			<td colspan="2" width="302">&nbsp;
				<form:select path="vestingPercentagesMethod" disabled="true">
					<form:option value="NA">Service not available</form:option>
					<form:option value="TPAP">Submitted electronically by TPA</form:option>
</form:select>
			</td>
</c:if>
		<ps:trackChanges name="csfForm" property="vestingPercentagesMethod" />
	</tr>
 
 	<%-- Reporting vesting percentage on participant statements --%>
<c:if test="${csfForm.displayNoticeGeneration ==true}">
		<tr class="datacell1 reportVesting" id="vestingDataOnStatementId">
</c:if>
<c:if test="${csfForm.displayNoticeGeneration !=true}">
		<tr class="datacell2 reportVesting" id="vestingDataOnStatementId">
</c:if>
		<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<content:getAttribute beanName="reportVestingPercentageLabel" attribute="text" /></td>
		<td width="20" align="right"><ps:fieldHilight name="vestingDataOnStatement" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
		<td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		
		<td colspan="2" width="302">
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
<td width="55"><form:radiobutton disabled="true" path="vestingDataOnStatement" value="Yes" />Yes</td>
<td width="247"><form:radiobutton disabled="true" path="vestingDataOnStatement" value="No" />No</td>
			</tr>
			</table>
		</td>
		<ps:trackChanges name="csfForm" property="vestingDataOnStatement" />
	</tr>	
	</tbody>
</table>
