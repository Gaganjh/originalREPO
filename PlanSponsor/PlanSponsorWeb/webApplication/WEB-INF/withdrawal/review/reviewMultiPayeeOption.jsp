<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<jsp:useBean id="wdRequest" 
			 scope="session" 
			 class="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />
<jsp:useBean id="wdRequestMoneyType" 
			 scope="session" 
			 class="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType" />
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />
<un:useConstants var="moneyTypeConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType" />
<un:useConstants var="withdrawalMultiPayee" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalMultiPayee" />
<un:useConstants var="requestUiConstants" className="com.manulife.pension.ps.web.withdrawal.WithdrawalRequestUi" />

<tr>
 
 <td>
   <table width="498" border="0" cellpadding="0" cellspacing="0">
   <tr valign="top" class="datacell2">
           <td class="datacell2">
           	 <span class="sectionName">
               <strong>
			   Pay Directly To Me 
			   </strong>
			   </span>
			   </td>
			   <td class="datacell2"></td>
			   </tr>
			   <tr>
			   <td>
			   
			   Amount
			 
			  </td>
			  <c:if test = "${withdrawalForm.withdrawalRequestUi.paat == 'PAAT'}" >
			   <td>
			   <form:radiobutton id="paat" path="withdrawalRequestUi.payDirectlyTome" class="radioSelectpaat" value="PAAT" /><label> 100% of my non-taxable balance</label>
			   </td>
			   </c:if>
			   </tr>
			   <tr>
			   <td>
			   
			   
			  </td>
			  <c:if test = "${withdrawalForm.withdrawalRequestUi.par == 'PAR'}" >
			   <td >
			   <form:radiobutton id="par" path="withdrawalRequestUi.payDirectlyTome" class="radioSelectpar" value="PAR" /><label> 100% of my total Roth balance</label>
			   </td>
			   </c:if>
			   </tr>
			   <tr>
			   <td>
			  </td>	
			  	  
			  <c:if test = "${withdrawalForm.withdrawalRequestUi.pa == 'PA'}" >			   			  			  		
			   <td >			   
			    <ps:fieldHilight name="withdrawalRequestUi.payDirectlyTomeAmount" singleDisplay="true"/> 	   	  
			   <form:radiobutton id="pa" path="withdrawalRequestUi.payDirectlyTome" class="radioSelectpa" value="PA" /> <label>Following amount </label>
			  <form:input onchange="return validateFolloingamont();" id="payDirectlyTome" path="withdrawalRequestUi.payDirectlyTomeAmount" class="inputbox" maxlength="12" />
			  
			   </td>
			
			  </c:if>
			   </tr>


			   
	</table>
   
  </td>
</tr>
<tr>
 <td>
   <table width="498" border="0" cellpadding="0" cellspacing="0">
   <tr valign="top" class="datacell2">
           <td class="datacell2">
           	 <span class="sectionName">
               <strong>
			   Rollover Remaining Balance
			   </strong>
			   </span>
			   </td>
			   <td class="datacell2"></td>
			   </tr>
			   <tr>
			   <td>
			   Please make a selection from each option.
			   <td>
			 
			   </tr>
			   <c:if test="${withdrawalForm.withdrawalRequestUi.nonTaxablePayeeFlag != true }">
			   <div id="div1">
				<c:if test = "${withdrawalForm.withdrawalRequestUi.nrat == 'NRAT'}" >
			   <tr>
			   <td>
			<label class="nonrothfieldsSpecific">   Non Taxable Amount </label> 
			   
			  </td>
			   <td>
			   <form:radiobutton id="nrat1" path="withdrawalRequestUi.nratCategory" class="nonrothfieldsSpecific1" value="TIRA" /> <label class="nonrothfieldsSpecific1">Traditional IRA</label>
			   </td>
			   </tr>
			   <tr>
			   <td>
			   
			   
			  </td>
			   <td >			   
			   <form:radiobutton id="nrat2" path="withdrawalRequestUi.nratCategory" class="nonrothfieldsSpecific2" value="RIRA"/> <label class="nonrothfieldsSpecific2">Roth IRA</label>
			   </td>
			   </tr>
			   <tr>
			   <td>
			   
			   
			  </td>
			  </td>
			   <td >
			   <form:radiobutton id="nrat3" path="withdrawalRequestUi.nratCategory" class="nonrothfieldsSpecific3" value="EQP" /><label class="nonrothfieldsSpecific3"> Employer-sponsored qualified plan </label>
			   
			   </td>
			   </tr>
			   </c:if>
			   <c:if test = "${withdrawalForm.withdrawalRequestUi.nrat != 'NRAT'}" >
			    <form:hidden id="nrat_hidden" path="withdrawalRequestUi.nratCategory" value="NA" /> 
	   		   </c:if>				   
			   </div>
			   </c:if>
			   <tr><td><br></td></tr>
			   
			   <c:if test = "${withdrawalForm.withdrawalRequestUi.tb == 'TB'}" >
			   <tr>
				<td>
			    Taxable Balance			   
			  </td>
			   <td>
			   <form:radiobutton id="tb1" path="withdrawalRequestUi.tbCategory" cssClass="mandatory1" value="TIRA" /> Traditional IRA
			   </td>
			   </tr>
			   <tr>
			   <td>
			   
			   
			  </td>
			   <td >
			   <form:radiobutton id="tb2" path="withdrawalRequestUi.tbCategory" cssClass="mandatory2" value="RIRA"/> Roth IRA
			   </td>
			   </tr>
			   <tr>
			   <td>
			   
			   
			  </td>
			  </td>
			   <td >
			   <form:radiobutton id="tb3" path="withdrawalRequestUi.tbCategory"  cssClass="mandatory3" value="EQP" /> Employer-sponsored qualified plan
			   
			   </td>
			   </tr>
		   
			   </c:if>
			   <c:if test = "${withdrawalForm.withdrawalRequestUi.tb != 'TB'}" >
			    <form:hidden id="tb_hidden" path="withdrawalRequestUi.tbCategory" value="NA" /> 
	   		   </c:if>
			<c:if test="${withdrawalForm.withdrawalRequestUi.rothPayeeFlag != true }">
			 <div id="mydiv">
			   <tr><td><br></td></tr>
			   
			   <c:if test = "${withdrawalForm.withdrawalRequestUi.rb == 'RB'}" >
				
				<tr>
			    <td>
				
			  <lable class="rothfieldsSpecific">  ROTH Balance </lable>	   
			  </td>
			   <td>
			   <form:radiobutton id="rb1" path="withdrawalRequestUi.rbCategory" class="rothfieldsSpecific1"  value="RIRA" />  <lable class="rothfieldsSpecific1">ROTH IRA</lable>
			   </td>
			   </tr>
			   
			   <tr>
			   <td> 
			   
			   
			  </td>
			  </td>
			   <td >
			   <form:radiobutton id="rb2" path="withdrawalRequestUi.rbCategory" class="rothfieldsSpecific2" value="EQP" />  <lable class="rothfieldsSpecific2">Roth account in an employer-sponsored qualified plan </lable>
			  
			   </td>
			   </tr>
			   
			   <tr><td><br></td></tr>
			   </c:if>
			   <c:if test = "${withdrawalForm.withdrawalRequestUi.rb != 'RB'}" >
			    <form:hidden id="rb_hidden" path="withdrawalRequestUi.rbCategory" value="NA" /> 
	   		   </c:if>			   
			   			   </div>
			   			   </c:if>
	</table>
   
  </td>
</tr>
