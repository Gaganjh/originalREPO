<!-- Author: Venkatesh Kasiraj - This JSP was added for Contact Management Project June 2010
This page is added as a part of Header implementation of Contact Information page, 
which displays the first point of contact information for the logged in user  
-->
<%@page import="com.manulife.pension.service.contract.valueobject.ContractProfileVO"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.contacts.FirstPointOfContact"%>
<%@ page import="com.manulife.pension.ps.web.contacts.FirstPointOfContactHelper"%>
<%@ page import="com.manulife.pension.delegate.ContractServiceDelegate"%>
<%@ page import="com.manulife.pension.service.contract.util.ServiceFeatureConstants"%>
<%@ page import="com.manulife.pension.service.contract.valueobject.ContractServiceFeature"%>
<%@ page import="com.manulife.pension.ps.web.util.Environment" %>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%
	UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("CLIENT_AND_OTHER",Constants.FirstClientContactFeatureValue.CLIENT_AND_OTHER.getValue(),PageContext.PAGE_SCOPE);
	int contractNumber = userProfile.getCurrentContract().getContractNumber();
	request.setAttribute(Constants.FIRST_POINT_OF_CONTACT, 
			FirstPointOfContactHelper.getFirstPointOfContact(contractNumber));
	
	ContractProfileVO contractProfileVO = ContractServiceDelegate.getInstance().getContractProfileDetails(
			contractNumber, Environment.getInstance().getSiteLocation());
	pageContext.setAttribute("contractProfileVO",contractProfileVO,PageContext.PAGE_SCOPE);
	%>
	
<content:contentBean contentId="<%=ContentConstants.PASSIVE_TRUSTEE_TEXT%>" 
            type="<%=ContentConstants.TYPE_FEEDISCLOSURE%>" id="passiveTrusteeWording" />

<c:set var="firstPointOfContact" value="${firstPointOfContact}" />


				

<%-- Get the customer service feature for the logged in user and set the first point of contact
     in customer service feature form --%>


<c:set var="theIndex" value="0" />
<table width="500" border="0" cellspacing="0" cellpadding="0"
	class="box">
	<tbody>
		<tr>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
			<td width="100%"><img src="/assets/unmanaged/images/s.gif"
				width="178" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
		</tr>
		<tr class="tablehead">
			<td height="25" colspan="3" class="tableheadTD1"><b>First
			point of contact</b></td>
		</tr>
		<tr>
			<td class="boxborder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
			<td class="databox" width="100%">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<c:choose>
					<c:when test="${(theIndex % 2) == 0}">
						<tr class="datacell1">
					</c:when>
					<c:otherwise>
						<tr class="datacell2">
					</c:otherwise>
				</c:choose>
				<c:set var="theIndex" value="${theIndex + 1}" />
				<td width="60%">Which party is the first point of contact?</td>
				<td class="greyborder"><img
					src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
					width="1"></td>
<td width="40%" colspan="2" nowrap="nowrap" style="color:red"><strong>${firstPointOfContact.firstClientContactValue.displayValue}</strong></td>

				</tr>
				<!-- check whether if first contact is other than client-->
<c:if test="${firstPointOfContact.firstClientContact == CLIENT_AND_OTHER}">

					<c:choose>
						<c:when test="${(theIndex % 2) == 0}">
							<tr class="datacell1">
						</c:when>
						<c:otherwise>
							<tr class="datacell2">
						</c:otherwise>
					</c:choose>
					<c:set var="theIndex" value="${theIndex + 1}" />
					<td width="60%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;What other party
					will be contacted for selected issues?</td>
					<td width="1" class="greyborder"><img
						src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
						width="1"></td>
						
<td width="40%" colspan="2" nowrap="nowrap" style="color:red"><strong>${firstPointOfContact.firstClientContactOtherValue.displayValue}</strong></td>

					<c:choose>
						<c:when test="${(theIndex % 2) == 0}">
							<tr class="datacell1">
						</c:when>
						<c:otherwise>
							<tr class="datacell2">
						</c:otherwise>
					</c:choose>
					<c:set var="theIndex" value="${theIndex + 1}" />
					<td width="60%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;What selected
					issues will the other party be contacted for?</td>
					<td width="1" class="greyborder"><img
						src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
						width="1"></td>
<td width="40%" colspan="2" nowrap="nowrap" style="color:red"><strong>${firstPointOfContact.firstClientContactOtherTypeValue}</strong></td>

					</tr>
</c:if>
			</table>
			</td>
			<td class="boxborder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
		</tr>
		<tr>
			<td colspan="3" class="boxborder" width="1"><img
				src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
	</tbody>
</table>
<img src="/assets/unmanaged/images/s.gif" width="1" height="5">
<br /><br/>
<%-- passive trustee --%>
<ps:isInternalUser name="userProfile" property="role">
<table width="500" border="0" cellspacing="0" cellpadding="0"
	class="box">
	<tbody>
		<tr>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
			<td width="100%"><img src="/assets/unmanaged/images/s.gif"
				width="178" height="1"></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
		</tr>
		<tr class="tablehead">
			<td height="25" colspan="3" class="tableheadTD1"><b><content:getAttribute beanName="passiveTrusteeWording" attribute="text" /></b></td>
		</tr>
		<tr>
			<td class="boxborder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
			<td class="databox" width="100%">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr class="datacell2">
				<td width="60%">Matrix Trust Company Passive Trustee Service</td>
				<td class="greyborder"><img
					src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
					width="1"></td>
				<td width="40%" colspan="2"><c:out value="${passiveTrustee}"></c:out></td>
				</tr>
				
				<tr class="datacell2">
				
				 <c:set var="jhTrusteeContains" value="false" />
			 <c:forEach items="${contractProfileVO.featuresAndServices.contractFeatures}" var="feature">
				<c:if test="${feature eq 'John Hancock Trustee Service'}">
				   <c:set var="jhTrusteeContains" value="true" />
 				</c:if>
			</c:forEach>
			
			 <c:if test="${jhTrusteeContains == true}">
				<td width="60%">John Hancock Trustee Service</td>
				<td class="greyborder"><img
					src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
					width="1"></td>
				<td width="40%" colspan="2">Yes</td>
				</c:if>
				<c:if test="${jhTrusteeContains == false}">
				<td width="60%">John Hancock Trustee Service</td>
				<td class="greyborder"><img
					src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
					width="1"></td>
				<td width="40%" colspan="2">No</td>
				</c:if>
				</tr>
				</table>
			</td>
			<td class="boxborder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1"></td>
		</tr>
		<tr>
			<td colspan="3" class="boxborder" width="1"><img
				src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
	</tbody>
</table>
<br/>
</ps:isInternalUser>
