<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.manulife.pension.ps.web.sendservice.NoticePlanDataForm"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%> 
<%@ page import="com.manulife.pension.ps.web.sendservice.util.PlanDataWebUtility"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>  
<%@ page import="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>

<c:set var="noticePlanCommonVO" value="${sessionScope.noticePlanCommonVO}" />

<style type="text/css">
.formulaHeader {
	 BACKGROUND-COLOR: #e6e7e8;
	 border:1px solid  #999999;
	 padding: 2px;
	 font-weight: bold;
}

.formulaBody {
	 border-left:1px solid  #999999;
	 border-bottom:1px solid  #999999;
	 border-right:1px solid  #999999;
}

.formulaBody td {
	 padding-top:2px;
}

.dioTable {
	padding-left:2px;
	padding-right:2px
}
</style>

<div id="investmentinfoTabDivId" class="borderedDataBox">
	<div class="subhead">
	  <c:if test="${not param.printFriendly}">
	   <div class="expandCollapseIcons" id="investmentinfoShowIconId" onclick="expandDataDiv('investmentinfo');">
	     <img class="icon" src="/assets/unmanaged/images/plus_icon.gif">
	   </div>
	   <div class="expandCollapseIcons" id="investmentinfoHideIconId" onclick="collapseDataDiv('investmentinfo');">
	      <img class="icon" src="/assets/unmanaged/images/minus_icon.gif">
	    </div>
	  </c:if>
	  <div class="sectionTitle">Investment Information</div>    
	  <div class="endDataRowAndClearFloats"></div>
	</div>
 	<div id="investmentinfoDataDivId">
	   <!--[if lt IE 7]>
	    <link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid_IE.css" type="text/css">
		<![endif]-->
		<div class=evenDataRow>
	      	<table class=dataTable>
	        	<tr>
					<td class=generalLabelColumn>The Plan's Default Investment Option  </td>
					<td class=dataColumn>
<c:if test="${empty noticePlanCommonVO.defaultInvestments}">
							<td></td>
</c:if>
<c:if test="${not empty noticePlanCommonVO.defaultInvestments}">
		       				<table>
		       					<tbody>
		       						<tr>
										<td width="20%" style="padding:2px;" class="formulaHeader">
											<div>
	                             				<table border="1">
			                                		<tr style="border: 1px; padding: 2px;font-weight: bold;">
				                                      <td class="dioTable">Name of Default Investment Option</td>
				                                      <td class="dioTable">Category</td>
			                                    	</tr>
<c:forEach items="${noticePlanCommonVO.defaultInvestments}" var="defaultInvestment">
														<tr class="evenDataRow">
<c:if test="${defaultInvestment.lifeCycleFund ==true}">
<td class="dioTable">${defaultInvestment.fundFamilyDisplayName}</td>
</c:if>
<c:if test="${defaultInvestment.lifeCycleFund ==false}">
<td class="dioTable">${defaultInvestment.fundName}</td>
</c:if>
<c:if test="${defaultInvestment.assetClass =='LCF'}">
												                <td class="dioTable">Target Date</td>
</c:if>
<c:if test="${defaultInvestment.assetClass !='LCF'}">
<c:if test="${defaultInvestment.assetClass =='LSF'}">
													        		<td class="dioTable">Target Risk</td>
</c:if>
<c:if test="${defaultInvestment.assetClass !='LSF'}">
													        		<td></td>
</c:if>
</c:if>
												      	</tr>
</c:forEach>
			                                	</table>
                              				</div>
										</td>
									</tr>	
								</tbody>
							</table>
</c:if>
					</td>
		    	</tr>
			</table>
		</div>		        
        <div class=oddDataRow>
      		<table class=dataTable>
        		<tr>
					<td class=generalLabelColumn>
					Is this DIO a Qualified Default Investment Alternative (QDIA)?: </TD>
					<td class=dataColumn>
<form:radiobutton disabled="${disableFields}" path="dIOisQDIA" id="QDIAYes" value="Y" />Yes

<form:radiobutton disabled="${disableFields}" path="dIOisQDIA" id="QDIANo" value="N" />No

					</td>
        		</tr>
        	</table>
        </div>		        
      	<div class=evenDataRow>
	      <table class=dataTable>
	        <tr>
				<td class=generalLabelColumn>
					When does the fee restriction apply to transfers out from this QDIA? 
				</td>
				<td class=dataColumn>
<form:select path="transferOutDaysCode" disabled="${disableFields}" styleId="transferOutDaysCode">
					 	<form:option value="30">30</form:option>
					 	<form:option value="60">60</form:option>
						<form:option value="90">90</form:option>
						<form:option value="00">Custom</form:option>
</form:select>
				</td>
				<td>&nbsp;</td>
				<td>
				<div id="custom" style="display:none">
<form:input path="transferOutDaysCustom" disabled="${disableFields}" maxlength="2" size="8" cssClass="numericInput" id="transferOutDaysCustom" /> days
				</div>
				<div id="nonCustom" style="display:block">
						days
				</div>
				</td>
	        </tr>
	       </table>
	     </div> 
<!--end table content -->
	</div>
</div>



<script>
$(document).ready(function(){
	var frm = document.noticePlanDataForm;
	var isDIOaQDIA = "${noticePlanDataForm.dIOisQDIA}";
	
	//hide or show the custom text box based on the selected no:of:days drop down  
	if(frm.transferOutDaysCode.value !=null && frm.transferOutDaysCode.value!=""){
		if(frm.transferOutDaysCode.value == '00'){
			document.getElementById('custom').style.display ="block";
			document.getElementById('nonCustom').style.display ="none";
		}
		else{
			document.getElementById('custom').style.display ="none";
			document.getElementById('nonCustom').style.display ="block";
			
		}
	}
	else{
		frm.transferOutDaysCode.value ="90";
		document.getElementById('custom').style.display ="none";
		document.getElementById('nonCustom').style.display ="none";
	}
	
});
</script>
