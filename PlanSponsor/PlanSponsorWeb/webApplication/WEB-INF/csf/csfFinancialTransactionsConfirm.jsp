
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfForm"%>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<c:set var="checkPayableToCode" value="${userProfile.currentContract.checkPayableToCode}"/>

<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/openWindow.js"></script>
<script language="javascript">

	function openLoanFeatures() {
	 	PDFWindow('/do/onlineloans/features/?task=print&printFriendly=true');
	}

</script>

<table border="0" cellpadding="0" cellspacing="0" width="698">
	<tbody>
	
		<tr class="tablesubhead">
			<td height="10" colspan="5" class="tablesubhead"><b><content:getAttribute
				beanName="financialTransactionsSubSectionTitle" attribute="text" /></b></td>

		</tr>
		<tr class="datacell2">
			<td width="375"><content:getAttribute beanName="participantsInterAccLabel" attribute="text" /></td>
			<td width="20">&nbsp;</td>
			<td width="1" class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"	width="1"></td>
<td colspan="2" valign="top">&nbsp;${csfForm.particiapntIATs} </td>
		</tr>
		<% int theIndex =0; %>
		
<c:if test="${csfForm.loanRecordKeepingInd =='Yes'}">
			<%theIndex++; if (theIndex % 2 == 0) { %>
			<tr class="datacell2">
	        <% } else { %>
	        <tr class="datacell1">
  		 	<% } %>
				<td width="375"><content:getAttribute beanName="participantsOnlineLoansLabel" attribute="text" />
<c:if test="${csfForm.participantServicesData.isLoansAllowed ==true}">
<c:if test="${csfForm.participantServicesData.isJHdoesLoanRK ==true}">
&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="javascript:openLoanFeatures();">Loan features at a glance</a>
</c:if>
</c:if>
				</td>
				<td width="20" align="right"><ps:fieldHilight name="participantInitiateLoansInd" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
				<td width="1" class="greyborder"><img
					src="/assets/unmanaged/images/spacer.gif" border="0" height="1"	width="1"></td>
<td width="55"><form:radiobutton disabled="true" path="participantInitiateLoansInd" value="Yes"/>Yes</td>
<td width="247"><form:radiobutton disabled="true" path="participantInitiateLoansInd" value="No"/>No</td>
			</tr>
</c:if>
		
		<%theIndex++; if (theIndex % 2 == 0) { %>
		<tr class="datacell2">
        <% } else { %>
        <tr class="datacell1">
  		 	<% } %>
			<td width="375"><content:getAttribute beanName="participantsInitiateWithdrawalLabel" attribute="text" /> </td>
			<td width="20" align="right"><ps:fieldHilight name="participantWithdrawalInd" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td width="1" class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"	width="1"></td>
<td width="55"><form:radiobutton disabled="true" path="participantWithdrawalInd" value="Yes"/>Yes</td>
<td width="247"><form:radiobutton disabled="true" path="participantWithdrawalInd" value="No"/>No</td>
		</tr>
		
		
	</tbody>
</table>
