<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script language="javascript">
	function openLoanFeatures() {
		PDFWindow('/do/onlineloans/features/?task=print&printFriendly=true');
	 }
</script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/openWindow.js"></script>
<table border="0" cellpadding="0" cellspacing="0"  width="698">
	<!-- Participant Services -->
	<tr class="tablehead">
		<td class="tablehead" width="698" colspan="3"><b><content:getAttribute
			beanName="participantServicesSectionTitle" attribute="text" /></b></td>
	</tr>
	<tr>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	    <td width="696" height="10" class="tablesubhead"><b> <content:getAttribute
			beanName="payrollSupportServicesSubSectionTitle" attribute="text" />
		</b></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
	<tr class="datacell2">
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td width="696" >
		<content:getAttribute id="participantOnlineAddressChanges" attribute="text">
<c:if test="${not empty csfForm.participantServicesData.addressParam}">
				<content:param>${csfForm.participantServicesData.addressParam}</content:param>
</c:if>
		</content:getAttribute></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
	<tr class="datacell1">
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td width="696" ><content:getAttribute
			id="participantDeferrelAmtType" attribute="text" /></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
	<tr class="datacell2">
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td width="646" ><content:getAttribute
			id="participantDeferrelsOnline1" attribute="text" /></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
	<tr class="datacell2">
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td width="696" ><content:getAttribute
			id="participantDeferrelsOnline2" attribute="text" /></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
	
	<tr class="datacell1">
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td width="646" ><content:getAttribute
			id="participantsAreAllowedToEnrolledOnline" attribute="text" /></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>

<c:if test="${csfForm.participantServicesData.determineOurStandardServiceEligible ==true}">


		<tr class="datacell1">
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i><content:getAttribute
				id="customizedLabel" attribute="text" /></i></td>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
		<c:if test="${0 != csfForm.participantServicesData.defaultDeferralScheduledIncreaseAmtAndMax}">
			<tr class="datacell1">
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
				<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
					id="defaultDeferralScheduledIncreaseAmtAndMax" attribute="text">
<c:forEach items="${csfForm.participantServicesData.deferrelSchedParamas}" var="deferrelSchedParamas">
						<content:param>
${deferrelSchedParamas} <%-- filter="false" --%>
						</content:param>
</c:forEach>
				</content:getAttribute></td>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
			</tr>
		</c:if>
		<c:if test="${0 != csfForm.participantServicesData.payrollCutOffForOnlineDeferralAndAutoEnrollmentChanges}">
			<tr class="datacell1">
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
				<td width="696" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<content:getAttribute
					id="payrollCutOffForOnlineDeferralAndAutoEnrollmentChanges"
					attribute="text" ><content:param>
${csfForm.participantServicesData.payrollParam}
						</content:param></content:getAttribute></td>
				<td width="1" class="boxborder">
					<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				</td>
			</tr>
		</c:if>
</c:if>

<%-- Beneficiary Information Details --%>
	<tr>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td height="10" class="tablesubhead" ><b><content:getAttribute
			id="onlineBeneficiaryDesignationService" attribute="text" /></b></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
	
<c:if test="${csfForm.participantServicesData.onlineBeneficiaryDesignationAllowed ==true}">


		
		<tr class="datacell2">
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="696" ><content:getAttribute
				id="participantsAllowedOnlineBeneficiaryDesignation" attribute="text" /></td>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
</c:if>
	
<c:if test="${csfForm.participantServicesData.onlineBeneficiaryDesignationAllowed ==false}">


		
		<tr class="datacell2">
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="696" ><content:getAttribute
				id="participantsNotAllowedOnlineBeneficiaryDesignation" attribute="text" /></td>
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
</c:if>

	<tr>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td height="10" class="tablesubhead"><b> <content:getAttribute
			beanName="financialTransactionsSubSectionTitle" attribute="text" />
		</b></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
	<tr class="datacell2">
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td width="696" ><content:getAttribute
			id="participantsCanInitiateInterAccountTransfersOnline"
			attribute="text" /></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>
<c:if test="${csfForm.participantServicesData.isJHdoesLoanRK ==true}">
		<tr class="datacell1">
			<td width="1" class="boxborder">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
			<td width="696">
				<content:getAttribute
							id="participantsCanInitiateOnlineLoanRequests" attribute="text" />
<c:if test="${csfForm.participantServicesData.isLoansAllowed ==true}">

						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<a href="#" onclick="javascript:openLoanFeatures();">
							<b>Loan features at a glance</b>
</a>
</c:if>
			</td>
			<td width="1" class="boxborder" align="right">
				<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
			</td>
		</tr>
</c:if>
	<tr class="datacell2">
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td width="696" ><content:getAttribute
			id="participantsCanInitiateWithdrawalRequests" attribute="text" /></td>
		<td width="1" class="boxborder">
			<img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
	</tr>

<c:if test="${csfForm.planEligibleForManagedAccounts}">
	<tr>
		<td width="1" class="boxborder">
		    <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		</td>
		<td width="696" height="10" class="tablesubhead">
		    <b> <content:getAttribute beanName="managedAccountsSubSectionTitle" attribute="text" /> </b>
		</td>
        <td width="1" class="boxborder">
		    <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
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
			<td width="1" class="boxborder">
		        <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		    </td>
			<td width="696">
			    <content:getAttribute id="planHasManagedAccount" attribute="text">
					<content:param>${csfForm.managedAccountServiceFeature.serviceDescription}</content:param>
				</content:getAttribute>
			</td>
			<td width="1" class="boxborder">
		        <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		    </td>
		</tr>
		<c:if test="${not empty csfForm.managedAccountServiceAvailableToPptDate}">
			<tr class="datacell1">
				<td width="1" class="boxborder">
		            <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		        </td>
				<td width="696">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				    <content:getAttribute id="managedAccountAsOfDate" attribute="text">
						<content:param>${csfForm.managedAccountServiceAvailableToPptDate}</content:param>
					</content:getAttribute>
				</td>
				<td width="1" class="boxborder">
		            <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		        </td>
			</tr>
		</c:if>
	</c:if>
</c:if>
</table>
