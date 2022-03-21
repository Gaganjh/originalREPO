<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfForm"%>

<%
	UserProfile userProfile = (UserProfile) session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile", userProfile, PageContext.PAGE_SCOPE);
%>
<table border="0" cellpadding="0" cellspacing="0" width="698">
	<tbody>
		<tr class="tablesubhead">
			<td height="10" colspan="5" class="tablesubhead">
			    <b> <content:getAttribute beanName="managedAccountsSubSectionTitle" attribute="text" /> </b>
			</td>
		</tr>
		
		<c:if test="${empty csfForm.managedAccountServiceFeature}">
			<tr class="datacell2">
				<td width="1" class="boxborder">
			       <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			    </td>
				<td width="696">
				    <content:getAttribute id="planHasNoManagedAccount" attribute="text" />
				</td>
				<td width="1" class="boxborder">
			        <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			    </td>
			</tr>
	    </c:if>
		<c:if test="${not empty csfForm.managedAccountServiceFeature}">
			<tr class="datacell2">
				<td width="375">
				   <content:getAttribute beanName="partCanOptInMA" attribute="text" />
				</td>
				<td class="greyborder">
				   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
					     width="1">
				</td>
				<td colspan="2">&nbsp; 
				     ${csfForm.managedAccountServiceFeature.serviceDescription}
				</td>
			</tr>
			<c:if test="${not empty csfForm.managedAccountServiceAvailableToPptDate}">
				<tr class="datacell1" id="sadSection">
					<td width="375">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
					   <content:getAttribute beanName="managedAccountAsOfDate" attribute="text">
							<content:param>
							</content:param>
						</content:getAttribute>
					</td>
					<td width="20" align="right">
					   <ps:fieldHilight name="managedAccountServiceAvailabilityDate" singleDisplay="true"
							            className="errorIcon" displayToolTip="true" />
					</td>
					<td class="greyborder" width="1">
					    <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
						     width="1">
					</td>
					<td colspan="2">&nbsp; 
					    <c:if test="${not csfForm.managedAccountSectionEditable}">
							${csfForm.managedAccountServiceAvailableToPptDate}
						</c:if>  
						<c:if test="${ csfForm.managedAccountSectionEditable}">
							<form:input path="managedAccountServiceAvailableToPptDate"
								        maxlength="10" readonly="true" size="10" disabled="true" /> (mm/dd/yyyy)
					    </c:if>		        
					</td>
				</tr>
			</c:if>	
		</c:if>
	</tbody>
</table>