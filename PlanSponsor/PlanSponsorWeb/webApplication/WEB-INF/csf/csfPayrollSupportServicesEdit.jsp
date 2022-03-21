<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.contract.csf.CsfForm"%>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<table border="0" cellpadding="0" cellspacing="0" width="698">
	<tbody>
		<tr class="tablehead">
			<td class="tablehead" width="698" colspan="5"><b><content:getAttribute
				beanName="participantServicesSectionTitle" attribute="text" /></b></td>
		</tr>
		<tr class="tablesubhead">
			<td height="10" colspan="5" class="tablesubhead"><b> <content:getAttribute
				beanName="payrollSupportServicesSubSectionTitle" attribute="text" />
			</b></td>
		</tr>
		<tr>
			<td colspan="5" class="tablesubhead" height="15"><b><content:getAttribute
					beanName="addressMngtLabel" attribute="text" /></b></td>
		</tr>
		<tr class="datacell2">
			<td width="375" valign="top" align="left"><content:getAttribute
					beanName="participantsAddressLabel" attribute="text" /></td>
			<td width="20" valign="top" align="right"><ps:fieldHilight name="activeAddress" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
			<td colspan="2">
			<input type="hidden" name="addressChanges" />
				<form:checkbox path="addressChanges" id="address" value="ACT" />Active 
				<form:checkbox path="addressChanges" id="address" value="TRM" />Terminated
				<form:checkbox path="addressChanges" id="address" value="RTD" />Retired
				<form:checkbox path="addressChanges" id="address" value="DSB" />Disabled
			</td>
		</tr>
<%-- <form:hidden path="addressChanges"/> --%>

		<tr class="datacell2" id="usaState">
			<td colspan="5" class="tablesubhead"><b><content:getAttribute
					beanName="managingDeferralsOnlineLabel" attribute="text" /> </b></td>
		</tr>
			
		<tr class="datacell1" >
			<td width="375"><content:getAttribute beanName="participantsDefferralAmountLabel" attribute="text" /></td>
			<td width="20" valign="top" align="right"><ps:fieldHilight name="deferralType" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td class="greyborder"><img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1"	width="1"></td>
			<td colspan="2">&nbsp;
				<form:select path="deferralType" onclick="removeNotSelectedOption()" onchange="enableDeferralAmounts()" disabled="${csfForm.isDeferralEZiDisabled && (csfForm.aciSignupMethod == 'A' || csfForm.aciSignupMethod == 'S')}">
					
					<c:if test="${empty deferralType}">
						<form:option value="" >Not Selected</form:option>
					</c:if>
					<form:option value="%">%</form:option>
					<form:option value="$">$</form:option>
					<form:option value="E">both $ and %</form:option>
</form:select>
				<ps:trackChanges name="csfForm" property="deferralType" />
			</td>
		</tr>
					
		<tr class="datacell2">
			<td width="375"><content:getAttribute beanName="participantsAreAllowedToDeferralsLabel" attribute="text" /></td>
			<td width="20" valign="top" align="right"><ps:fieldHilight name="changeDeferralsOnline" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
		
			<td class="greyborder"><span class="greyborder">
			   <img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></span>
			</td>
<c:if test="${userProfile.internalUser ==true}">
<td width="55"><form:radiobutton disabled="${csfForm.isDeferralEZiDisabled && (csfForm.aciSignupMethod == 'A' || csfForm.aciSignupMethod == 'S')}" onclick="checkDeferralOnlineItems();" path="changeDeferralsOnline" value="Yes"/>Yes</td>
<td width="248"><form:radiobutton disabled="${csfForm.isDeferralEZiDisabled && (csfForm.aciSignupMethod == 'A' || csfForm.aciSignupMethod == 'S')}" onclick="checkDeferralOnlineItems();" path="changeDeferralsOnline" value="No"/>No</td>
</c:if>
<c:if test="${userProfile.internalUser !=true}">
			    <td colspan="2">
${csfForm.changeDeferralsOnline}
			    </td>	
</c:if>
			<ps:trackChanges name="csfForm" property="changeDeferralsOnline" />
		</tr>
					
		<tr class="datacell2 deferralOnline" id="defaultDeferralSection">
			<td width="375" valign="top">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<content:getAttribute beanName="defaultDeferralScheduleLabel" attribute="text" /></td>
			<td width="20">&nbsp;</td>
			<td class="greyborder"><img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1"	width="1"></td>
			<td colspan="2">
				<table>
					<tr id="deferralLimitDollarsId">
						<td>
$<form:input path="deferralLimitDollars" disabled="${csfForm.isDeferralEZiDisabled && (csfForm.aciSignupMethod == 'A' || csfForm.aciSignupMethod == 'S')}" maxlength="6" size="6" cssClass="inputField"/>
							<!-- <content:getAttribute beanName="toAMaxOfAmontLabel" attribute="text" /> --> 
							 <ps:fieldHilight name="deferralLimitDollars" singleDisplay="true" className="errorIcon" displayToolTip="true"/> to a Max of $
<form:input path="deferralMaxLimitDollars" disabled="${csfForm.isDeferralEZiDisabled && (csfForm.aciSignupMethod == 'A' || csfForm.aciSignupMethod == 'S')}" maxlength="6" size="6" cssClass="inputField"/>
							 <ps:fieldHilight name="deferralMaxLimitDollars" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
						</td>
					</tr>
					<tr>
<form:hidden path="aciSignupMethod"/>
<form:hidden path="resetDeferralValues"/>
<form:hidden path="isDeferralEZiDisabled"/>
<form:hidden path="planAciInd"/>
						<td>
						<div id="deferralLimitPercentAutoId">
&nbsp;${csfForm.planDeferralLimitPercent}%
							<!-- <content:getAttribute beanName="toAMaxOfPercentageLabel" attribute="text" /> -->   
							 to a Max of 
${csfForm.planDeferralMaxLimitPercent}%&nbsp; as per plan provisions
						</div>
						<div id="deferralLimitPercentId">
&nbsp;<form:input path="deferralLimitPercent" disabled="${csfForm.isDeferralEZiDisabled && (csfForm.aciSignupMethod == 'A' || csfForm.aciSignupMethod == 'S')}" maxlength="3" size="6" cssClass="inputField"/>
							<!-- <content:getAttribute beanName="toAMaxOfPercentageLabel" attribute="text" /> --> %  
							<ps:fieldHilight name="deferralLimitPercent" singleDisplay="true" className="errorIcon" displayToolTip="true"/>to a Max of
<form:input path="deferralMaxLimitPercent" disabled="${csfForm.isDeferralEZiDisabled && (csfForm.aciSignupMethod == 'A' || csfForm.aciSignupMethod == 'S')}" maxlength="3" size="6" cssClass="inputField"/>%
							 <ps:fieldHilight name="deferralMaxLimitPercent" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
						</div>
						</td>
					</tr>
				</table>
			 <ps:trackChanges name="csfForm" property="deferralLimitDollars" /><ps:trackChanges name="csfForm" property="deferralMaxLimitDollars" />
			 <ps:trackChanges name="csfForm" property="deferralLimitPercent" /><ps:trackChanges name="csfForm" property="deferralMaxLimitPercent" />
			</td>
		</tr>
					
		<tr class="datacell1" id="participantsErollOnlineId">
			<td width="375"><content:getAttribute beanName="participantsErollOnlineLabel" attribute="text" /></td>
			<td width="20" align="right"><ps:fieldHilight name="enrollOnline" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td class="greyborder"><span class="greyborder">
			   <img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></span>
			</td>
<td width="55"><form:radiobutton path="enrollOnline" value="Yes"/>Yes</td>
<td width="248"><form:radiobutton path="enrollOnline" value="No"/>No</td>
			<ps:trackChanges name="csfForm" property="enrollOnline" />
		</tr>
					
		<tr class="datacell2">
			<td width="375"><content:getAttribute beanName="payrollCutoffEnrollLabel" attribute="text" /> </td>
			<td width="20" align="right"> <ps:fieldHilight name="payrollCutoff" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			<td width="1" class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"	width="1"></td>
<td colspan="2" valign="top">&nbsp;<form:input path="payrollCutoff" disabled="${csfForm.isDeferralEZiDisabled && (csfForm.aciSignupMethod == 'A' || csfForm.aciSignupMethod == 'S')}" maxlength="2" size="6" cssClass="inputField"/>&nbsp;

			days</td>
			<ps:trackChanges name="csfForm" property="payrollCutoff" />
		</tr>
		<tr class="tablesubhead">
			<td height="10" colspan="5" class="tablesubhead"><b><content:getAttribute
			id="onlineBeneficiaryDesignationService" attribute="text" /></b></td>
		</tr>
		<tr class="datacell2">
		<td width="375"><content:getAttribute beanName="onlineBeneficiaryDesignationServiceLabel" attribute="text" /></td>
		<td width="20" align="right"><ps:fieldHilight name="onlineBeneficiaryInd" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
		<td class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"></td>
<td width="55"><form:radiobutton path="onlineBeneficiaryInd" value="Yes" />Yes</td>
<td width="247"><form:radiobutton path="onlineBeneficiaryInd" value="No" />No</td>
		<ps:trackChanges name="csfForm" property="onlineBeneficiaryInd" />
		</tr>
	</tbody>
</table>
