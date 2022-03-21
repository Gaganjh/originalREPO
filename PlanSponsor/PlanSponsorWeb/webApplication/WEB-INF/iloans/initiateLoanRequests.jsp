<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page
	import="com.manulife.pension.ps.web.iloans.InitiateLoanRequestsForm"%>


<ps:form style="margin-bottom:0;" method="POST" modelAttribute="initiateLoanRequestsForm" name="initiateLoanRequestsForm"
	action="/do/iloans/initiateLoanRequests/">

		

<table width="700" border="0" cellspacing="0" cellpadding="0">

		<c:if test="${initiateLoanRequestsForm.onlineLoan}">
			<tr>			
				<td class="redText" colspan="3"><strong> Contract has been converted to
				 <a href="/do/withdrawal/loanAndWithdrawalRequestsInit/">
				online loans </a> </strong></td>
			</tr>
		</c:if>

		<tr>
        <td colspan="3"><strong><font color="#CC6600">*</font></strong> Required Information 
            <content:errors scope="session"/>
        </td>
	</tr>

    <tr>

        <%-- 1st cell is for profile details --%>
        <td><br>
        <table width="650" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="25"><img src="/assets/unmanaged/images/s.gif" width="153" height="1"></td>
                <td width="200"><img src="/assets/unmanaged/images/s.gif" width="153" height="1"></td>
	            <td width="25"><img src="/assets/unmanaged/images/s.gif" width="153" height="1"></td>
                <td width="300"><img src="/assets/unmanaged/images/s.gif" width="153" height="1"></td>				
                <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr class="tablehead">
                <td class="tableheadTD1" colspan="7"><strong><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></strong></td>
            </tr>
            <tr class="datacell1">
         		<td rowspan="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
             	<td colspan = "4">
             	<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr class="datacell1">
                		<td><ps:label fieldId="contractNumber" mandatory="true">Contract number</ps:label></td>
<td><form:input path="contractNumber" maxlength="<%=Constants.STR_CONTRACT_NUMBER_MAX_LENGTH%>" size="10"/>
						<script type="text/javascript" >
						document.forms["initiateLoanRequestsForm"].contractNumber.focus();
						</script>
								  <%
										// generate dropdown
										java.util.Collection typeOfLoanList = InitiateLoanRequestsForm.TYPE_OF_LOAN_LIST;
										pageContext.setAttribute("typeOfLoanList", typeOfLoanList);		  
						      		   %>
		                <td><ps:label fieldId="typeOfLoan" mandatory="true">Type of loan</ps:label></td>
						<td>
								  		<form:select path="typeOfLoan">
										<form:options items= "${typeOfLoanList}" 
											 itemValue="value"
											itemLabel="label" />
</form:select>
				   </td>
            	</tr>
            		<tr class="datacell1">
                	<td><ps:label fieldId="ssn" mandatory="true">SSN</ps:label></td>
					<td>
<form:input path="ssn1" maxlength="3" onkeyup="return autoTab(this, 3, event);" size="3" cssClass="inputField"/>
<form:input path="ssn2" maxlength="2" onkeyup="return autoTab(this, 2, event);" size="2" cssClass="inputField"/>
<form:input path="ssn3" maxlength="4" onkeyup="return autoTab(this, 4, event);" size="4" cssClass="inputField"/>
					</td>
              		<td><ps:label fieldId="reasonForLoan" mandatory="false">Reason for loan</ps:label></td>
<td rowspan = "2"><form:textarea path="reasonForLoan" cols="25" rows="3"/>
            </tr> 
			        <tr class="datacell1">
                	<td><ps:label fieldId="legallyMarried" mandatory="false">Legally married</ps:label></td>
<td><ps:label fieldId="yes" mandatory="false">Yes</ps:label><form:radiobutton path="legallyMarried" value="yes"/>
&nbsp;&nbsp;<ps:label fieldId="no" mandatory="false">No</ps:label><form:radiobutton path="legallyMarried" value="no"/>
					</td>
		</tr>
		</table>
	</td>
	<td width="4"><img src="/assets/unmanaged/images/s.gif" width="1"
		height="1"></td>
	<td rowspan="1" class="databorder"><img
		src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	<ps:roundedCorner numberOfColumns="7" emptyRowColor="white" />

	</table>
	</td>
	<td><img src="/assets/unmanaged/images/s.gif" width="20"
		height="1"></td>
	<td valign="top">&nbsp;</td>
	</tr>
	</table>
	<br>


	<table width="650" border="0" cellspacing="0" cellpadding="0">
		<tr>
<td align="right"><input type="submit" class="button100Lg" name="actionLabel" value="back" />

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="submit" class="button100Lg" name="actionLabel" value="continue" /> <script

					language="javascript">
						var onenter = new OnEnterSubmit('actionLabel',
								'continue');
						onenter.install();
					</script></td>
		</tr>
	</table>
	<br>
</ps:form>
