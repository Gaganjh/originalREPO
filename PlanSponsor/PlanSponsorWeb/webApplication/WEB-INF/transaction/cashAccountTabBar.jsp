
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfConstants" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
	
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<c:set var="definedBenefit" value="${userProfile.currentContract.definedBenefitContract}" /> 

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<c:if test="${empty param.printFriendly }" >
<td>
	<DIV class="nav_Main_display">
		<UL class="">
		<c:choose>
			<c:when test="${param['selectedTab'] == 'BalanceDetailsTab'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="/do/transaction/cashAccountReport/" id="CashAccount">Cash Account</a></LI>
				<LI class="on">Balance Details</LI>
				<c:if test="${definedBenefit==false}">
					<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="/do/transaction/cashAccountForfeituresReport/" id="Forfeitures">Forfeitures</a></LI>
</c:if>
			</c:when>
			<c:when test="${param['selectedTab'] == 'ForfeituresTab'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="/do/transaction/cashAccountReport/" id="CashAccount">Cash Account</a></LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="/do/transaction/cashAccountBalanceDetailsReport/" id="BalanceDetails">Balance Details</a></LI>
				<c:if test="${definedBenefit==false}">
					<LI class="on">Forfeitures</LI>
</c:if>
			</c:when>
			<c:otherwise>
				<LI class="on">Cash Account</LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="/do/transaction/cashAccountBalanceDetailsReport/" id="BalanceDetails">Balance Details</a></LI>
				<c:if test="${definedBenefit==false}">
					<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';"><a href="/do/transaction/cashAccountForfeituresReport/" id="Forfeitures">Forfeitures</a></LI>
</c:if>
			</c:otherwise>		
		</c:choose>
		</UL> 
	</DIV>
</td>	
</c:if>
<c:if test="${not empty param.printFriendly }" >
<td colspan="3" bgcolor="#FFF9F2">
	<DIV class="nav_Main_display">
		<UL class="">
		<c:choose>
			<c:when test="${param['selectedTab'] == 'BalanceDetailsTab'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';">Cash Account</LI>
				<LI class="on">Balance Details</LI>
				<c:if test="${definedBenefit==false}">
					<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';">Forfeitures</LI>
</c:if>
			</c:when>
			<c:when test="${param['selectedTab'] == 'ForfeituresTab'}">
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';">Cash Account</LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';">Balance Details</LI>
				<c:if test="${definedBenefit==false}">
					<LI class="on">Forfeitures</LI>
</c:if>
			</c:when>
			<c:otherwise>
				<LI class="on">Cash Account</LI>
				<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';">Balance Details</LI>
				<c:if test="${definedBenefit==false}">
					<LI onMouseOver="this.className='off_over';" onMouseOut="this.className='';">Forfeitures</LI>
</c:if>
			</c:otherwise>		
		</c:choose>
		</UL> 
	</DIV>
</td>	
</c:if>
</tr>
</table>
