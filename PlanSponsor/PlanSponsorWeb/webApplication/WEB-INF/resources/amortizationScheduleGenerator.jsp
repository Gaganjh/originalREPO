<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.resources.AmortizationScheduleForm" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>


<script type="text/javascript" >
function doOnload()
	{
	var displayPdf=<%=session.getAttribute("pdf")!=null%>;
	if (displayPdf)
	        PDFWindow('/do/util/pdf/');
	}

</SCRIPT>

<ps:form method="POST" modelAttribute="amortizationScheduleForm" name="amortizationScheduleForm" action="/do/resources/amortizationScheduleGenerator/" >
<table width="700" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td colspan="3"><strong><font color="#CC6600">*</font></strong> Required Information 
            <content:errors scope="request"/>
        </td>
	</tr>


    <tr>

        <%-- 1st cell is for profile details --%>
        <td><br>
        <table width="650" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                <td width="200"><img src="/assets/unmanaged/images/s.gif" width="153" height="1"></td>
                <td width="489"><img src="/assets/unmanaged/images/s.gif" width="153" height="1"></td>
                <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
                <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            </tr>
            <tr class="tablehead">
                <td class="tableheadTD1" colspan="5"><strong><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></strong></td>
            </tr>
			<tr class="datacell1">
				<td rowspan="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
				<td colspan = "3">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr class="datacell1">
						<td colspan = "5">
						  <p><content:getAttribute beanName="layoutPageBean" attribute="body1"/></p>
						  <p>&nbsp;</p>
                		</td>
					</tr>
	              <tr> 
	                <td width="20%"><ps:label fieldId="contractName" mandatory="false">Contract name</ps:label></td>
					
	                <td width="31%"> &nbsp;&nbsp; 
<form:input path="contractName" maxlength="30" size="30" cssClass="inputField"/></td>
	                </td>
					<script type="text/javascript" >
					document.forms["amortizationScheduleForm"].contractName.focus();
					</script>

	                <td width="20%"><ps:label fieldId="participantName" mandatory="false">Participant name</ps:label></td>
	                <td width="30%"> 
<form:input path="participantName" maxlength="30" size="30" cssClass="inputField"/></td>
	                </td>
	              </tr>
	
	              <tr> 
	                <td width="20%"><ps:label fieldId="loanDate" mandatory="true">Date of loan</ps:label></td>
	                <td width="31%">&nbsp;&nbsp; 
<form:input path="loanDate" maxlength="10" size="10" cssClass="inputField"/>
					<a href="javascript:cal1.popup();"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Click Here to Pick up the date"></a> (mm/dd/yyyy)<br>
					</td>

					<td width="20%"><ps:label fieldId="dateOfFirstPayment" mandatory="true">Date of first payment</ps:label></td>
<td width="30%"> <form:input path="firstPaymentDate" maxlength="10" size="10" cssClass="inputField"/>
					<a href="javascript:cal2.popup();"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Click Here to Pick up the date"></a> (mm/dd/yyyy)<br>
					</td>
					</tr>
	              <tr> 
	                <td width="20%"><ps:label fieldId="loanAmount" mandatory="true">Loan amount($)</ps:label></td>
	                <td width="31%">&nbsp;&nbsp;	                  
<form:input path="loanAmount" maxlength="10" size="10" cssClass="inputField"/>
	                </td>

	                <td width="20%"><ps:label fieldId="amortizationPeriod" mandatory="true">Amortization period</ps:label></td>
	                <td width="30%"> 
<form:input path="amortizationYears" maxlength="3" size="7" cssClass="inputField"/></td>
	              </tr>
				  <%
					// generate dropdown
					java.util.Collection repaymenDropDown = AmortizationScheduleForm.PAYROLL_FREQUENCY_LIST;
					pageContext.setAttribute("repaymenDropDown", repaymenDropDown);		  
	      		   %>
	              <tr> 
	                <td width="20%"><ps:label fieldId="Repaymentfrequency" mandatory="true">Repayment frequency</ps:label></td>
	                <td width="31%"> &nbsp;&nbsp; 
			  		<form:select path="repaymentFrequency">
					<form:options items= "${repaymenDropDown}" 
						 itemValue="value"
						itemLabel="label" />
</form:select>
				   </td>

	                <td width="20%"><ps:label fieldId="nominalAnnualRate" mandatory="true">Nominal annual rate</ps:label></td>
	                <td width="30%"> 
<form:input path="nominalAnnualRate" maxlength="5" size="7" cssClass="inputField"/>%</td>
              		</tr>

					</table>
					</td>
					<td rowspan="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>           
				</tr> 
				<ps:roundedCorner numberOfColumns="5"
						  emptyRowColor="white"/>

			</table>
			</td>
			<td><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
			<td valign="top">&nbsp;</td>
		</tr>
	</table>  
        		<br>
				
				
				<table width="650" border="0" cellspacing="0" cellpadding="0">
				    <tr>
				        <td align="right">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

						</td>
						<td align="right">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						
						</td>
												</td>
												<td align="right">
<input type="submit" class="button100Lg" name="actionLabel" value="Generate Pdf" />
												
						</td>
				    </tr>
				</table>
				
</ps:form>
	 <script type="text/javascript" >
	 			var cal1 = new calendar(document.forms['amortizationScheduleForm'].elements['loanDate']);
	 			cal1.year_scroll = true;
	 			cal1.time_comp = false;
	 		
	 			var cal2 = new calendar(document.forms['amortizationScheduleForm'].elements['firstPaymentDate']);
	 			cal1.year_scroll = true;
	 			cal1.time_comp = false;
	
	
	</script>
