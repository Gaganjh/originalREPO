<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.manulife.pension.ps.web.plandata.TabPlanDataForm"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%> 
<%@ page import="com.manulife.pension.ps.web.plandata.util.TPAPlanDataWebUtility"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>  
<%@ page import="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<content:contentBean contentId="<%=ContentConstants.INVESTMENT_INFORMATION_TAB_INSTRUCTION%>" type="<%=ContentConstants.TYPE_DISCLAIMER%>" id="investmentInformationTabInstruction" />
<jsp:useBean id="noticePlanCommonVO" scope="session" class="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO"/>


			 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Investment Information</title>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script>
</head>
<body>
<div id="investmentInformationTabDivId" class="borderedDataBox">
	<% NoticePlanCommonVO commonVO = (NoticePlanCommonVO)session.getAttribute("noticePlanCommonVO");
	int size = 0;
	   if(commonVO !=null){
	       if(commonVO.getDefaultInvestments()!=null){
	           size = commonVO.getDefaultInvestments().size();
	           System.out.println(commonVO.getDefaultInvestments().size());
	       }
	   }
	%>
	<table border=0 cellSpacing=0 cellPadding=0 width=729>
	    <tr>
		    <td width="100%"><!--[if lt IE 7]>
		    <link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid_IE.css" type="text/css">
			<![endif]-->
				<table width="729" class="dataTable">
					<tr><td class=subsubhead><content:getAttribute id="investmentInformationTabInstruction" attribute="text"/></td></tr>
				</table>
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
<c:if test="${defaultInvestment.assetClass == 'LCF'}">
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
							<ps:fieldHilight name="dIOisQDIA" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
							<ps:fieldHilight name="qdiaNoticeSelected" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
							Is this DIO a Qualified Default Investment Alternative (QDIA)?: </TD>
							<td class=dataColumn>
							
<form:radiobutton path="dIOisQDIA" id="QDIAYes" value="Y" />Yes

<form:radiobutton path="dIOisQDIA" id="QDIANo" value="N" />No

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
<form:select path="transferOutDaysCode" onchange="javascript:transferOutDaysChanged()" styleId="transferOutDaysCode">
							 	<form:option value="30">30</form:option>
							 	<form:option value="60">60</form:option>
								<form:option value="90">90</form:option>
								<form:option value="00">Custom</form:option>
</form:select>
						</td>
						<td>&nbsp;</td>
						<td>
						<div id="custom" style="display:none">
<form:input path="transferOutDaysCustom" disabled="disabled" maxlength="2" onblur="validateNoOfDays(this)" size="8" cssClass="numericInput" id="transferOutDaysCustom" /> days
								<ps:fieldHilight name="transferOutDaysCustom" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
						</div>
						<div id="nonCustom" style="display:block">
								days
						</div>
						</td>
			        </tr>
			       </table>
			     </div> 
			</td>
		</tr>
	</table>
<!--end table content -->
</div>



<script>
$(document).ready(function(){
	var frm = document.tabPlanDataForm;
	var isDIOaQDIA = "${e:forJavaScriptBlock(tabPlanDataForm.dIOisQDIA)}";
	
	//enable/disable no:of:days based on isDIOaQDIA
	if(isDIOaQDIA !=null && isDIOaQDIA !=""){
		if(isDIOaQDIA == 'Y'){
			document.getElementById('transferOutDaysCode').disabled = false;
			document.getElementById('transferOutDaysCustom').disabled = false;
		}
		else if(isDIOaQDIA == 'N'){
			document.getElementById('transferOutDaysCode').disabled = true;
			document.getElementById('transferOutDaysCustom').disabled = true;
		}
	}
	else{
		document.getElementById('transferOutDaysCode').disabled = true;
		document.getElementById('transferOutDaysCustom').disabled = true;
	}
	
	//hide or show the custom text box based on the selected no:of:days drop down  
	if(frm.transferOutDaysCode.value !=null && frm.transferOutDaysCode.value!=""){
		if(frm.transferOutDaysCode.value == '00'){
			document.getElementById('custom').style.display ="block"; 
		}
		else{
			document.getElementById('custom').style.display ="none";
		}
	}
	
	//when dIOisQDIA is changed
	$('input[name="dIOisQDIA"]').click(function() {
		setDirtyFlag();
	 	if($('[name="dIOisQDIA"]:checked').val() == 'Y'){
			document.getElementById('transferOutDaysCode').disabled = false;
			document.getElementById('transferOutDaysCustom').disabled = false;
		}
	 	else{
	 		document.getElementById('transferOutDaysCode').disabled = true;
	 		document.getElementById('transferOutDaysCustom').disabled = true;
	 		document.getElementById('transferOutDaysCustom').value='';
	 	}
	});
	
	if(document.getElementById('transferOutDaysCode').value == '00') {
		document.getElementById('custom').style.display ="block";
		document.getElementById('nonCustom').style.display ="none";
	}
	else {
		document.getElementById('custom').style.display ="none"; 
		document.getElementById('nonCustom').style.display ="block";
	}
	
});

//Validate on click of SAVE
function validateInvInfoData(){
	var frm = document.tabPlanDataForm;
	var isDIOaQDIA = "${e:forJavaScriptBlock(tabPlanDataForm.dIOisQDIA)}";
	var QDIAYes=document.getElementById('QDIAYes');
	
	if(QDIAYes.checked){
		if(frm.transferOutDaysCode.value == '00'){
			if(frm.transferOutDaysCustom.value != null && frm.transferOutDaysCustom.value != ''){
				if(frm.transferOutDaysCustom.value < 30 || frm.transferOutDaysCustom.value > 90){
					alert(ERR_TRANSFER_OUT_DAYS_CUSTOM_NOT_IN_SPECIFIED_LIMIT_OR_NON_NUMERIC);
					document.getElementById('transferOutDaysCustom').value='';
					return true;
				}
			}
		}
	}
	return false;
}

</script>

</body>
</html>
