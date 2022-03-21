<%@ taglib tagdir="/WEB-INF/tags/bob" prefix="bob"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import=" com.manulife.pension.bd.web.userprofile.BDUserProfile" %>

<content:contentBean
	contentId="<%=BDContentConstants.PLAN_REVIEW_HISTORY_DISABLE_CONFIRM_TEXT%>"
	type="<%=BDContentConstants.TYPE_MESSAGE%>"
	id="deletePlanReviewReportConfirmText" />

<% 
BDUserProfile userProfile =(BDUserProfile)session.getAttribute(BDConstants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>



<div id="viewDisableReasonButton"
	class="ui-helper-hidden ui-state-highlight">
	<div style="background-color: #455660; border-top: #febe10 4px solid;">
		<ul class="proposal_nav_menu">
			<li><a id="Customize_Shipping_sec" class="selected_link"><span
					style="padding-left: 10px;"> <strong>Delete Plan
							Review Report</strong></span></a></li>
		</ul>
	</div>

	<div>
		<table class="printConfirmationbox" cellspacing="0" cellpadding="0"
			width="100%" style="padding-top: 17px; padding-bottom: 7px; padding-right: 10px;  padding-left:10px;">
			<tr>
				<td width="15%"></td>
				<td width="85%"></td>
			</tr>
			<tr>
				<td align="left"
					style="font-size: 12px; font-weight: 50px; color: black;"
					colspan="2"><content:getAttribute
						id="deletePlanReviewReportConfirmText" attribute="text" /><br />
				</td>
			</tr>
			<c:if
				test="${(userProfile.internalUser eq true) or (userProfile.inMimic eq true)}">
				<tr>
					<td>Reason Code:</td>
					<td>
					
<form:select path="planReviewHistoryDetailsReportForm" >



							<c:forEach var="viewDisableReasonItem"
								items="${planReviewHistoryDetailsReportForm.viewDisableReasonMap}">
								<option value="${viewDisableReasonItem.key}"><c:out
										value="${viewDisableReasonItem.value}" /></option>
							</c:forEach>
</form:select>
					<td>
				</tr>
			</c:if>
		</table>
		<br/><br/>
		<div Style="float: right; padding-right: 20px;">
			<c:if test="${sessionScope.bobResults !='contract'}">
			<input class="blue-btn next"
				onmouseover="this.className +=' btn-hover'" id="blue-btn_big"
				onmouseout="this.className='blue-btn'"
				onclick="deletePlanReviewReport();" value="Ok" type=button
				name='Ok' title='Ok' /> &nbsp; &nbsp; 
			</c:if>
			<c:if test="${sessionScope.bobResults =='contract'}">
			<input class="blue-btn next"
				onmouseover="this.className +=' btn-hover'" id="blue-btn_big"
				onmouseout="this.className='blue-btn'"
				onclick="deletePlanReviewReportContractLevel();" value="Ok" type=button
				name='Ok' title='Ok' /> &nbsp; &nbsp; 
			</c:if>
			
			<input
			class="blue-btn cancel" onmouseover="this.className +=' btn-hover'"
			id="blue-btn_big" onmouseout="this.className='blue-btn'"
			onclick="doClose();" value="Cancel" type=button name='Cancel'
			title='Cancel' />
		</div>
	</div>
</div>
