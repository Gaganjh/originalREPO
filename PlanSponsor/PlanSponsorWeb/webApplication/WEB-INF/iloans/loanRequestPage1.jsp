<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>

<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.iloans.IloansHelper"%>
<%@ page
	import="com.manulife.pension.service.account.valueobject.LoanRequestData"%>

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_ILOANS_GATEWAY_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MESSAGE%>" id="gatewayMessage" />
<script language="JavaScript1.2" type="text/javascript">
<!--
	var submitted = false;

	function setButtonAndSubmit(button) {
		if (!submitted) {
			submitted = true;
			document.loanRequestForm.button.value = button;
			document.loanRequestForm.submit();
		} else {
			window.status = "Transaction already in progress.  Please wait.";
		}
	}

	-->
</script>
<%@ page
	import="com.manulife.pension.ps.web.iloans.LoanRequestForm"%>
<jsp:useBean id="loanRequestForm" scope="session" type="com.manulife.pension.ps.web.iloans.LoanRequestForm" />


<content:errors scope="session" />
<%
	if (loanRequestForm.isGatewayStatusActive()) {
%>
<br>
<span class='redtext'><content:getAttribute
		beanName="gatewayMessage" attribute="text" /></span>
<%
	}
%>
<br>
<% String loanRequestData =LoanRequestData.REQUEST_STATUS_CODE_LOAN_DENIED;
	pageContext.setAttribute("loanRequestData",loanRequestData,PageContext.PAGE_SCOPE);	

%>

<ps:form method="POST" modelAttribute="loanRequestForm" name="loanRequestForm" action="/do/iloans/loanRequestPage1/">

	

	<table width="650" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				height="1" /></td>
			<td width="4"><img src="/assets/unmanaged/images/s.gif"
				height="1" /></td>
			<td width="160"><img src="/assets/unmanaged/images/s.gif"
				height="1" /></td>
			<td width="160"><img src="/assets/unmanaged/images/s.gif"
				height="1" /></td>
			<td width="160"><img src="/assets/unmanaged/images/s.gif"
				height="1" /></td>
			<td width="160"><img src="/assets/unmanaged/images/s.gif"
				height="1" /></td>
			<td width="4"><img src="/assets/unmanaged/images/s.gif"
				height="1" /></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				height="1" /></td>
		</tr>
		<tr class="tablehead">
			<td class="tableheadTD1" colspan="8"><b><content:getAttribute
						beanName="layoutPageBean" attribute="body1Header" /></b></td>
		</tr>

		<tr class="datacell1">
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
			<td colspan="6" rowspan="13">
				<table width="100%" border="0" cellpadding="0">
					<tr>
						<td align="left" colspan="2"><strong>Contract</strong></td>
<td align="left">${loanRequestForm.contractNumber}</td>

						<td align="left"><strong>Contract name</strong></td>
<td colspan="2" align="left">${loanRequestForm.contractName} <%-- filter="false" --%></td>

					</tr>
					<tr>
						<td align="left" colspan="2"><strong>SSN</strong></td>
						<td align="left"><render:ssn
								property="loanRequestForm.participantSSN" /></td>
						<td align="left"><strong>Participant name</strong></td>
<td colspan="2" align="left">${loanRequestForm.participantName}</td>

					</tr>
					<tr>
						<td colspan="2" align="left"><strong>Total
								participant balance</strong></td>
						<td align="left">$<render:number
								property="loanRequestForm.totalParticipantBalance"
								defaultValue="0.00" pattern="###,###,##0.00" /></td>
						<td align="left"><strong>Legally married</strong></td>
<td colspan="2" align="left"> 
<c:if test="${loanRequestForm.legallyMarried == ''}">

		  not available
</c:if>  
<c:if test="${loanRequestForm.legallyMarried != ''}">

${loanRequestForm.legallyMarried}
</c:if></td>
					</tr>
					<tr>
						<td height="17" colspan="2" align="left"><strong>Type
								of loan </strong></td>
<td height="17" align="left">${loanRequestForm.reqLoanReasonCode}</td>

						<td height="17" align="left"><strong>Confirmation
								number </strong></td>

<td colspan="2" height="17" align="left">${loanRequestForm.confirmationNumber}</td>

					</tr>
					<tr>
						<td height="17" colspan="2" align="left"><strong>Reason
								for loan</strong></td>
<td height="17" colspan="4" align="left">${loanRequestForm.reasonForLoan}</td>

					</tr>
					<tr>
						<td height="17" colspan="2" align="left"><strong>Initiated
								by</strong></td>
<c:if test="${loanRequestForm.tpaInitiated ==true}">

							<td height="17" colspan="4" align="left">TPA</td>
</c:if>
<c:if test="${loanRequestForm.tpaInitiated !=true}">

							<td height="17" colspan="4" align="left">Participant</td>
</c:if>
					</tr>
				</table>
			</td>
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
				width="1" height="2" /></td>
		</tr>
		<tr class="datacell1">
			<td class="databorder"></td>
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="1" /></td>
		</tr>

		<tr>

			<td class="databorder"></td>

			<ps:roundedCorner numberOfColumns="8" emptyRowColor="white" />
		</tr>

	</table>
	<br>
	<content:getAttribute beanName="layoutPageBean" attribute="body1" />
	<br>
	<br>

	<table width="650" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="1"><img src="/assets/unmanaged/images/s.gif"
				height="1" /></td>
			<td width="4"><img src="/assets/unmanaged/images/s.gif"
				height="1" /></td>
			<td
				width="380"><img src="/assets/unmanaged/images/s.gif"  height="1" /></td>
			<td width="268"><img src="/assets/unmanaged/images/s.gif"  height="1" /></td>
    		<td width="3"><img src="/assets/unmanaged/images/s.gif" height="1" /></td>
			<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" /></td>
       </tr>
    <tr class="tablehead">
      <td class="tableheadTD1" colspan="6"><b><content:getAttribute beanName="layoutPageBean" attribute="body2Header"/></b></td>
    </tr>
    <tr class="datacell1">
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	  <td colspan="4" rowspan="13" >
        <TABLE width="100%" border="0">
		  <TR>
		  	<TD  align="left" ><content:getAttribute beanName="layoutPageBean" attribute="body2"/></TD>
		  	<TD   align="left"><content:getAttribute beanName="layoutPageBean" attribute="body3"/></td>
          </TR>
          <TR>
            <TD align="left"  valign="top">
            	<table width="100%" cellpadding="0" cellspacing="4" border ="0">
            		<TR >
            			<TD width ="110" align="left" valign="top" ><strong>Money type</strong></TD>
            			<TD width ="110" align="left" valign="top"><strong>Dollars</strong></TD>
            			<TD width ="100" align="left" valign="top"><strong>Vesting %</strong></TD>
            			<TD width ="50" align="left" valign="top"><strong>Exclude</strong></TD>
            		</tr>
<form:hidden path="moneyTypeCheckbox" value="na" />
            		<%for (int i = 0; i < loanRequestForm.getMoneyType().length; i++) {%>
            		<TR>
                		<TD align="left" ><%=loanRequestForm.getMoneyType(i)%><input type="hidden" name="moneyType[<%=i%>]" value="<%=loanRequestForm.getMoneyType(i)%>" ></TD>
                		<TD align="left" >$ <input type="text" size="11" maxlength="15" name="moneyTypeAmount[<%=i%>]" value=<%=loanRequestForm.getMoneyTypeAmount(i)%> ></TD>
                		<%if (loanRequestForm.getMoneyTypeCategory(i)
							.equalsIgnoreCase("EE")) {%>
                		    <TD align="left" >100% <input type="hidden" name="moneyTypeVesting[<%=i%>]" value=100 ></TD>
                		<%} else {%>
                		    <TD align="left"  ><input type="text" size="3" maxlength="3" name="moneyTypeVesting[<%=i%>]" value=<%=loanRequestForm.getMoneyTypeVesting(i)%> > %</TD>
                        <%}
                        String moneyType=loanRequestForm.getMoneyTypeId(i);
                        pageContext.setAttribute("moneyType",moneyType,PageContext.PAGE_SCOPE);
                        %>
                		<td ><form:checkboxes name="loanRequestForm" path="moneyTypeCheckbox" value="${moneyType}" /> </td>
                	</tr>
                	<%}%>
                    <TR>
                    	<TD align="left"  >OTHER VESTED</TD>
<TD align="left" >$ <form:input path="otherMoneyTypeAmount" maxlength="15" size="11" /></td>
<TD align="left" ><form:input path="otherMoneyTypeVesting" maxlength="3" size="3" />%</TD>
                		<TD ><form:checkboxes name="loanRequestForm" path="moneyTypeCheckbox" value="OTH" />
                		</TD>
                    </tr>
                    <TR>
                    	<TD align="left"  ><strong>Vested Acct. Balance</strong></TD>
<TD align="left">$ <form:input path="appVestedAccountBalance" readonly="true" size="11" /></TD>
                    	<TD align="left" colspan ="2" >
<input type="button" onclick="javascript:setButtonAndSubmit('calculateStep1');return false;" name="calc1" class="button100Lg" value="Calculate"/>
<input type="hidden" name="button" /><%--  input - name="loanRequestForm" --%>
                    	</TD>

                    </TR>

                </table>		
           </td>          
              <TD align="left"  valign="top">
          		<table width="100%" cellpadding="0" cellspacing="4" border ="0">
          			<TR>
          				<TD  width ="108"  align="left">&nbsp;</TD>
          				<TD  width ="160" >&nbsp;</TD>
                    </TR>
                    <TR>
                    	<TD   align="left" ><strong><ps:label fieldId="<%=LoanRequestForm.FIELD_HIGHEST_LOAN_BALANCE_12_MTHS%>">Highest loan
              balance in last 12 months</ps:label></strong></TD>
<TD >$ <form:input path="highestLoanBalanceInLast12Mths" maxlength="12" size="6" cssClass="inputField" /></TD>
                    </TR>
                    <TR>
                    	<TD align="left" ><strong><ps:label fieldId="<%=LoanRequestForm.FIELD_NUMBER_OF_OUTSTANDING_LOAN_REQUESTS%>">Number of outstanding loans</ps:label></strong></TD>
<TD >&nbsp;&nbsp;<form:input path="numberOfOutstandingLoans" maxlength="2" size="3" cssClass="inputField" />
                    	</TD>
                    </TR>
                    <TR>
                    	<TD  align="left" ><strong><ps:label fieldId="<%=LoanRequestForm.FIELD_CURRENT_OUTSTANDING_BALANCE%>">Current outstanding balance</ps:label></strong></TD>
<TD >$ <form:input path="currentOutstandingBalance" maxlength="12" size="5" cssClass="inputField" />
                    	</TD>
                    </TR>
                    <TR>
                    	<td align="left" ><strong>Max. loan available</strong></td>
<td >$ <form:input path="maxLoanAvailable" maxlength="8" size="5" cssClass="inputField" />
<input type="button" onclick="javascript:setButtonAndSubmit('calculateStep2');return false;" name="cont" class="button100Lg" value="Calculate"/>
                        </td>
                    </TR>
					<TR>
						<TD height="35" colspan="" align="left"><strong>Proceed with loan</strong></TD>
<TD align="center" colspan="" height="35"><form:radiobutton path="proceedWithLoan" value="${loanRequestData}"/> Yes &nbsp;
<form:radiobutton path="proceedWithLoan" value="${loanRequestData}"/> No
                   	</TR>
                 </TABLE>
          </td>
        </tr>

       </table>
      </td>
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	</tr>
	<tr class="datacell2">
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
    </tr>
    <tr class="datacell1">
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
    </tr>
    <tr class="datacell2">
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
    </tr>
    <tr class="datacell1">
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
    </tr>
    <tr class="datacell2">
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
    </tr>
    <tr class="datacell1">
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
    </tr>
    <tr class="datacell2">
      <td class="databorder"></td>
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
    </tr>
    <tr class="datacell1">
      <td class="databorder"></td>
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
    </tr>
    <tr class="datacell2">
      <td class="databorder"></td>
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
    </tr>
    <tr class="datacell1">
      <td class="databorder"></td>
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
    </tr>
    <tr class="datacell2">
      <td class="databorder"></td>
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" rowspan ="3" width="1" height="3" /></td>
    </tr>
    <tr class="datacell1">
      <td class="databorder"></td>
      <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
	</tr>

	<tr class="datacell1">
		 
      <td class="databorder" ></td>
	
	  		<ps:roundedCorner numberOfColumns="6"
					  emptyRowColor="white"/>
   	</tr>
  

  </table>
  <br>
  <table width="650"  border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td width="475" align="right">&nbsp;</td>
<td width="25" align="right"><input type="button" onclick="javascript:setButtonAndSubmit('back');return false;" name="cont" class="button100Lg" value="back"/></td>
<td width="25" align="right"><input type="button" onclick="javascript:setButtonAndSubmit('saveExit');return false;" name="cont" class="button100Lg" value="save & exit"/></td>
<td width="25" align="right"><input type="button" onclick="javascript:setButtonAndSubmit('continue');return false;" name="cont" class="button100Lg" value="continue"/></td>
    </tr>
    <tr>
    	<td colspan="4">&nbsp;</td>
    </tr>

    <tr>
    	<td colspan="4">
    	<br>
    	<p><content:pageFooter beanName="layoutPageBean"/></p>
		<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
		<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p> 
		</td>
		</tr>
  </table>
 </ps:form>

<script>
	setFocusOnFirstInputField("loanRequestForm");
</script>
