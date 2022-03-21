<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script language="JavaScript1.2" type="text/javascript">

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<content:contentBean contentId="<%=ContentConstants.WARNING_LOAN_SETUP_FEE%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="loanSetupFeeAlert"/>
<!--

var submitted=false;

function setButtonAndSubmit(button) {
    
	if (!submitted) {
        submitted=true;
        document.loanRequestForm.button.value = button;
        document.loanRequestForm.submit();
	} else {
		window.status = "Transaction already in progress.  Please wait.";
	}
	
}

function setButtonAndContinue(button) {
    
	if (!submitted) {

	    var showWarning = false;
	    
	    if (document.loanRequestForm.loanSetupFee.value != null &&
	        document.loanRequestForm.loanSetupFee.value > 1000) {
	        showWarning = true;
	    }
	    
	    if (showWarning) {
	        if (confirm("<content:getAttribute beanName="loanSetupFeeAlert" attribute="text" filter="true"/>")) {
	            submitted=true;
	            document.loanRequestForm.button.value = button;
	            document.loanRequestForm.submit();
	        } else {
	            return false;
	        }
	    } else {
            submitted=true;
            document.loanRequestForm.button.value = button;
            document.loanRequestForm.submit();
	    }
	} else {
		window.status = "Transaction already in progress.  Please wait.";
	}
	
}

// checking maximum length
function imposeMaximumLength(Object, MaximumLength)
{
	if (Object.getAttribute && Object.value.length > MaximumLength)
		Object.value=Object.value.substring(0,MaximumLength);
}

-->
</script>

<%@ page
	import="com.manulife.pension.service.account.valueobject.LoanPayrollFrequency"%>
<%@ page
	import="com.manulife.pension.service.account.valueobject.LoanRequestData"%>
<%@ page
	import="com.manulife.pension.ps.web.iloans.LoanRequestForm"%>
<jsp:useBean id="loanRequestForm" scope="session" type="com.manulife.pension.ps.web.iloans.LoanRequestForm" />

<% 
	String loanDenied =LoanRequestData.REQUEST_STATUS_CODE_LOAN_DENIED;
	pageContext.setAttribute("loanDenied",loanDenied,PageContext.PAGE_SCOPE);	
	
	String loanApproved =LoanRequestData.REQUEST_STATUS_CODE_LOAN_APPROVED;
	pageContext.setAttribute("loanApproved",loanApproved,PageContext.PAGE_SCOPE);	
	
%>

<content:errors scope="session" />
<br>
<ps:form method="POST" modelAttribute="loanRequestForm" name="loanRequestForm" action="/do/iloans/loanRequestPage2/">

	

<form:hidden path="button" />
	<table width="650" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td width="113"><img src="/assets/unmanaged/images/s.gif"
				width="80" height="1" /></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td width="463"><img src="/assets/unmanaged/images/s.gif"
				width="250" height="1" /></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td width="113"><img src="/assets/unmanaged/images/s.gif"
				width="80" height="1" /></td>
			<td width="3"><img src="/assets/unmanaged/images/s.gif"
				width="4" height="1" /></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="tablehead">
			<td class="tableheadTD1" colspan="8"><b><content:getAttribute
						beanName="layoutPageBean" attribute="body1Header" /></b></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td colspan="6" rowspan="13"><TABLE width="100%" border="0">
					<tr>
						<td colspan="3" align="left"><strong>Participant
								modeled information</strong></td>
						<td width="3">&nbsp;</td>
						<td colspan="3" align="left"><strong>TPA verified
								information</strong></td>
					</tr>
					<tr>
						<td align="left">&nbsp;</td>
						<td></td>
						<td align="right">&nbsp;</td>
						<td>&nbsp;</td>
						<td align="left"><strong><ps:label
									fieldId="<%=LoanRequestForm.FIELD_MAX_AMORTIZATION_PERIOD%>">Max. amortization period</ps:label></strong></td>
						<td></td>
<td><form:input path="maxAmortizationPeriod" maxlength="2" size="3" cssClass="inputField" /> years</span></td>


					</tr>
					<tr>
						<td width="164" align="left"><strong>Estimated
								interest rate</strong></td>
						<td width="3"></td>
<td width="84" align="right"><c:if test="${loanRequestForm.tpaInitiated ==false}">

${loanRequestForm.reqLoanInterestRate}%

</c:if> <c:if test="${loanRequestForm.tpaInitiated ==true}">n/a

</c:if></td>
						<td width="3">&nbsp;</td>
						<td width="138" align="left"><strong><ps:label
									fieldId="<%=LoanRequestForm.FIELD_LOAN_INTEREST_RATE%>">Loan interest
            rate</ps:label></strong></td>
						<td width="3"></td>
<td width="196"><form:input path="loanInterestRate" maxlength="7" size="5" cssClass="inputField" /> %</td>


					</tr>
					<tr>
						<td align="left"><strong>Repayment frequency</strong></td>
						<td width="3"></td>
<td width="84" align="right"><c:if test="${loanRequestForm.tpaInitiated ==false}">

${loanRequestForm.reqPaymentFrequency}

</c:if> <c:if test="${loanRequestForm.tpaInitiated ==true}">n/a

</c:if></td>
						<td width="3">&nbsp;</td>
						<td width="138" align="left" valign="middle"><strong><ps:label
									fieldId="<%=LoanRequestForm.FIELD_REPAYMENT_FREQUENCY%>">Repayment
            frequency</ps:label></strong></td>
						<td width="3"></td>
<td width="196"><form:select path="repaymentFrequency" >

<!-- Need to check type of below item  -->  
<c:forEach items="${LoanPayrollFrequency.LOAN_PAYROLL_FREQUENCIES }" var="payrollFreqList" ><%-- collection="<%=LoanPayrollFrequency.LOAN_PAYROLL_FREQUENCIES%>" --%>

 <c:set var="temp" value= "${payrollFreqList.getPeriodsPerYear()}" />
						<fmt:parseNumber var="key" type="number" value="${temp}" />
									<form:option
										value="${key}"><!-- need more consontration on this not tested -->
${payrollFreqList.description}
									</form:option>
</c:forEach>

</form:select></td>
					</tr>
					<tr>
						<td align="left"><strong>Est. max. loan available</strong></td>
						<td width="3">&nbsp;</td>
<td width="84" align="right"><c:if test="${loanRequestForm.tpaInitiated ==false}">

		  $<render:number property="loanRequestForm.reqMaxLoanAvailable"
									defaultValue="0.00" pattern="###,###,##0.00" />
</c:if> <c:if test="${loanRequestForm.tpaInitiated ==true}">n/a

</c:if></td>
						<td width="3">&nbsp;</td>
						<td width="138" align="left"><strong><ps:label
									fieldId="<%=LoanRequestForm.FIELD_MAX_LOAN_AVAILABLE%>">Max. loan available</ps:label></strong></td>
						<td width="3">$</td>
						<td width="196"><render:number
								property="loanRequestForm.appMaxLoanAvailable"
								defaultValue="0.00" pattern="###,###,##0.00" /></td>
					</tr>
					<tr>
						<td align="left"><strong>Amortization period</strong></td>
						<td></td>
<td width="84" align="right"><c:if test="${loanRequestForm.tpaInitiated ==false}">

${loanRequestForm.reqAmortizationPeriod}

</c:if> <c:if test="${loanRequestForm.tpaInitiated ==true}">

			n/a
</c:if></td>
						<td width="3">&nbsp;</td>
						<td align="left"><strong><ps:label
									fieldId="<%=LoanRequestForm.FIELD_AMORTIZATION_PERIOD%>">Amortization period</ps:label></strong></td>
						<td width="3"></td>
<td><form:input path="amortizationPeriod" maxlength="2" size="3" cssClass="inputField" /> years</td>


					</tr>
					<tr>
						<td align="left"><strong>Requested amount</strong></td>
						<td width="3">&nbsp;</td>
<td width="84" align="right"><c:if test="${loanRequestForm.tpaInitiated ==false}">

		  $<render:number property="loanRequestForm.reqLoanAmount"
									defaultValue="0.00" pattern="###,###,##0.00" />
</c:if> <c:if test="${loanRequestForm.tpaInitiated ==true}">

			n/a
</c:if></td>
						<td width="3">&nbsp;</td>
						<td width="138" align="left"><strong><ps:label
									fieldId="<%=LoanRequestForm.FIELD_LOAN_AMOUNT%>">Loan amount</ps:label></strong></td>
						<td width="3">$</td>
<td width="196"><form:input path="loanAmount" maxlength="8" size="5" cssClass="inputField" /></td>


					</tr>
					<tr>
						<td align="left"><strong>Repayment amount</strong></td>
						<td width="3">&nbsp;</td>
<td width="84" align="right"><c:if test="${loanRequestForm.tpaInitiated ==false}">

		  $<render:number property="loanRequestForm.reqPaymentAmount"
									defaultValue="0.00" pattern="###,###,##0.00" />
</c:if> <c:if test="${loanRequestForm.tpaInitiated ==true}">

		  n/a
</c:if></td>
						<td width="3">&nbsp;</td>
						<td width="138" align="left"><strong><ps:label
									fieldId="<%=LoanRequestForm.FIELD_REPAYMENT_AMOUNT%>">Repayment amount</ps:label></strong></td>
						<td width="3">$</td>
<td width="196"><form:input path="appPaymentAmount" maxlength="8" readonly="true" size="5" cssClass="inputField" /> <input type="button" onclick="javascript:setButtonAndSubmit('calculateStep3');return false;" name="cont" class="button100Lg" value="Calculate"/>




						</td>
					</tr>
					<tr>
						<td align="left"><strong>Request date</strong></td>
						<td width="3"></td>
<td width="84" align="right">${loanRequestForm.reqDate}</td>

						<td width="3">&nbsp;</td>
						<td width="138" align="left"><strong><ps:label
									fieldId="<%=LoanRequestForm.FIELD_EXPIRY_DATE%>">Plan info expiry
            date</ps:label></strong></td>
						<td width="3"></td>
<td width="196"><form:input path="expiryDate" maxlength="10" size="10" cssClass="inputField" /> <a href="javascript:cal1.popup();"><img


								src="/assets/unmanaged/images/cal.gif" width="16" height="16"
								border="0" alt="Click Here to Pick up the date"></a>
							(mm/dd/yyyy)<br></td>
					</tr>
					<TBODY>
					</TBODY>
				</TABLE></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<ps:roundedCorner numberOfColumns="8" emptyRowColor="white" />
		</tr>
		<tr>
	</table>
	<br>
	<table width="650" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td width="4"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td width="640"><img src="/assets/unmanaged/images/s.gif"
				width="640" height="1" /></td>
			<td width="4"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="tablehead">
			<td class="tableheadTD1" colspan="6"><b><content:getAttribute
						beanName="layoutPageBean" attribute="body2Header" /></b></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td colspan="3" rowspan="13"><table border="0">
					<tr>
						<td width="160"><img src="/assets/unmanaged/images/s.gif"
							width="80" height="1" /></td>
						<td width="80"><img src="/assets/unmanaged/images/s.gif"
							width="80" height="1" /></td>
						<td width="80"><img src="/assets/unmanaged/images/s.gif"
							width="80" height="1" /></td>
						<td width="160"><img src="/assets/unmanaged/images/s.gif"
							width="160" height="1" /></td>
					</tr>
					<tr>

						<td colspan="4"><content:getAttribute
								beanName="layoutPageBean" attribute="body1" /></td>
					</tr>
					<tr>
<td colspan="4"><form:input path="defProvision" maxlength="250" size="80" cssClass="inputField" /></td>


					</tr>
					<td colspan="4"><content:getAttribute
							beanName="layoutPageBean" attribute="body2" /></td>
					</tr>
					<tr>
						<td align="left"><strong>Proceed with loan</strong></td>
<td colspan="3"><c:if test="${loanRequestForm.proceedWithLoan ==loanDenied}">


<form:radiobutton disabled="true" path="proceedWithLoan" value="${loanApproved}" disabled="true" /> Yes &nbsp;


<form:radiobutton disabled="true" path="proceedWithLoan" value="${loanDenied}" disabled="true" /> No</td>


</c:if>
<c:if test="${loanRequestForm.proceedWithLoan !=loanDenied}">

<form:radiobutton path="proceedWithLoan" value="${loanApproved}"/> Yes &nbsp

<form:radiobutton path="proceedWithLoan" value="${loanDenied}"/> No</td>

</c:if>

						</td>

					</tr>
					<tr>
						<td colspan="4" align="left" class="boldtext">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="4" align="left"><content:getAttribute
								beanName="layoutPageBean" attribute="body3" /></td>
					</tr>
					<tr>
						<td colspan="4" align="left" class="boldtext">&nbsp;</td>
					</tr>

					<tr>
						<td align="left"><strong><ps:label
									fieldId="<%=LoanRequestForm.FIELD_LOAN_SETUP_FEE%>">TPA Loan Issue Fee</ps:label></strong></td>
<td>$ <form:input path="loanSetupFee" maxlength="5" size="5" cssClass="inputField" />


						<td><strong>Legally married</strong></td>
<td>&nbsp;${loanRequestForm.legallyMarried}</td>

					</tr>
					<tr>
						<td align="left" class="boldtext">&nbsp;</td>
						<td>&nbsp;</td>
						<td><strong><ps:label
									fieldId="<%=LoanRequestForm.FIELD_SPOUSAL_CONSENT%>">Spousal consent required</ps:label></strong></td>
<td><form:radiobutton path="spousalConsentRadio" value="Y"/>
Yes &nbsp; <form:radiobutton path="spousalConsentRadio" value="N"/>
							No</td>
					</tr>
					<tr>
						<td colspan="4" align="left" class="boldtext">&nbsp;</td>
					</tr>
					<tr>
						<td colspan="4" align="left"><strong><ps:label
									fieldId="<%=LoanRequestForm.FIELD_ADDITIONAL_COMMENTS%>">Enter additional
            comments</ps:label></strong> &nbsp;&nbsp;E.g. Reason for reduced max loan
							available, higher interest rate, reason loan is not approved,
							etc.(750 character maximum)</td>
					</tr>
					<tr>
<td colspan="4" align="left"><span class="inputField"><form:textarea path="addComments" cols="75" onchange="return imposeMaximumLength(this, 750);" onkeyup="return imposeMaximumLength(this, 750);" rows="10" cssClass="inputField" /><%-- - name="loanRequestForm" --%></span></td>




					</tr>
				</table></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell2">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<ps:roundedCorner numberOfColumns="5" emptyRowColor="white" />
		</tr>

	</table>
	<br>
	<table width="650" border="0" cellspacing="1" cellpadding="1">
		<tr>
			<td width="475" align="right">&nbsp;</td>
<td width="25" align="right"><input type="button" onclick="javascript:setButtonAndSubmit('back');return false;" name="back" class="button100Lg" value="back"/></td>


<td width="25" align="right"><input type="button" onclick="javascript:setButtonAndSubmit('saveExit');return false;" name="cont" class="button100Lg" value="save & exit"/></td>


<td width="25" align="right"><input type="button" onclick="javascript:setButtonAndContinue('continue');return false;" name="continue" class="button100Lg" value="continue"/></td>


		</tr>
		<tr>
			<td colspan="4"><br>
				<p>
					<content:pageFooter beanName="layoutPageBean" />
				</p>
				<p class="footnote">
					<content:pageFootnotes beanName="layoutPageBean" />
				</p>
				<p class="disclaimer">
					<content:pageDisclaimer beanName="layoutPageBean" index="-1" />
				</p> <br></td>
		</tr>

	</table>

</ps:form>
<script>
	setFocusOnFirstInputField("loanRequestForm");
</script>
<script type="text/javascript" >
	 			var cal1 = new calendar(document.forms['loanRequestForm'].elements['expiryDate']);
	 			cal1.year_scroll = true;
	 			cal1.time_comp = false;

</script>
