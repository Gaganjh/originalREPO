<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="/WEB-INF/render" prefix="render" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

    
  <%@ page import="java.math.BigDecimal"%>
<%@ page import="com.manulife.pension.ps.web.plandata.TabPlanDataForm"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%> 
<%@ page import="com.manulife.pension.ps.web.plandata.util.TPAPlanDataWebUtility"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>  
<%@ page import="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO" %>

<content:contentBean contentId="<%=ContentConstants.CONTRIBUTION_AND_DISTRIBUTION_TAB_INSTRUCTION%>" type="<%=ContentConstants.TYPE_DISCLAIMER%>" id="contributionAndDistributionTabInstruction" />
<content:contentBean contentId="<%=ContentConstants.DYNAMIC_TEXT_ROTH_QUESTION%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="dynamicTextRothQuestion" />
<content:contentBean contentId="<%=ContentConstants.DYNAMIC_TEXT_CATCH_UP_QUESTION%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="dynamicTextCatchUpQuestion" />
<c:set var="noticePlanCommonVO" value="${sessionScope.noticePlanCommonVO}" />



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<style type="text/css">
.tpaheader {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 11px;
	width: 729px;
	font-weight: bolder;
	color: #000000;
	padding: 0px;
	background : #CCCCCC url(/assets/unmanaged/images/box_ul_corner.gif) no-repeat left top;
	clear: both;
	vertical-align: middle;
	border-right:1px solid #CCCCCC;
}
#spdEmployeeContributionRef {
    resize:none;
    overflow:auto;	
}
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Contribution and Distribution</title>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script>
</head>
<body>

<div id="generalTabDivId" class="borderedDataBox">
<TABLE border=0 cellSpacing=0 cellPadding=0 width=729>
    <TR>
    <TD width="100%"><!--[if lt IE 7]>
    <link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid_IE.css" type="text/css">
	<![endif]-->
	 <table width="729" class="dataTable">
			<TR><TD class=subsubhead><content:getAttribute id="contributionAndDistributionTabInstruction" attribute="text"/></TD></TR>
		</table>
	<table width="729" class="dataTable">
	<TR><TD class="sectionTitle"><b>Contributions</b></TD></TR>
	</table>
	
	  <DIV class=evenDataRow> 
	 
	  <TABLE class=dataTable>
        <TR vAlign=top>
        	<TD class=generalLabelColumn>
        	<ps:fieldHilight name="maxEmployeeBeforeTaxDeferralPct" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Plan's maximum employee deferral (includes Roth if applicable)
			</TD>
			
			<TD class=dataColumn>
			 <c:choose>
					<c:when test="${(noticePlanCommonVO.deferralMaxAmount!=null && noticePlanCommonVO.deferralMaxAmount!='')  
						 || (noticePlanCommonVO.deferralMaxPercent!=null && noticePlanCommonVO.deferralMaxPercent!='') || noticePlanCommonVO.deferralIrsApplies=='true' }">
						<c:choose>
								<c:when test="${noticePlanCommonVO.deferralMaxPercent!=null && noticePlanCommonVO.deferralMaxPercent!=''}">
								<c:if test="${(noticePlanCommonVO.deferralMaxAmount!=null and noticePlanCommonVO.deferralMaxAmount!='') or noticePlanCommonVO.deferralIrsApplies=='true'}">
		                       		<b>Percentage:</b>
		                       	</c:if>
${noticePlanCommonVO.deferralMaxPercent} %&nbsp;&nbsp;&nbsp;&nbsp;
								</c:when>
						</c:choose>
					
						<c:choose>
							<c:when test="${noticePlanCommonVO.deferralIrsApplies!=true && noticePlanCommonVO.deferralMaxAmount!=null && noticePlanCommonVO.deferralMaxAmount!=''}">
							<c:if test="${(noticePlanCommonVO.deferralMaxPercent!=null and noticePlanCommonVO.deferralMaxPercent!='') or noticePlanCommonVO.deferralIrsApplies=='true'}">
								<b>Annual Dollar Limit: </b>
							</c:if>
$ ${noticePlanCommonVO.deferralMaxAmount}
							</c:when>
							<c:when test="${noticePlanCommonVO.deferralIrsApplies=='true' }">
							<c:if test="${(noticePlanCommonVO.deferralMaxPercent!=null and noticePlanCommonVO.deferralMaxPercent!='') or (noticePlanCommonVO.deferralMaxAmount!=null and noticePlanCommonVO.deferralMaxAmount!='') }">
								<b>Annual Dollar Limit: </b>
							</c:if>
								IRS annual maximum
							</c:when>
						</c:choose> 
					</c:when>
					<c:otherwise>
						<p>Pending Plan Information Completion</p>
					</c:otherwise>
			</c:choose> 
			
			</TD>
        </TR></TABLE></DIV>
          <DIV class=oddDataRow> 
	      
      <TABLE class=dataTable>
        <TR vAlign=top>
        	<TD class=generalLabelColumn>
        	<ps:fieldHilight name="contributionMoneyType" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
        	<ps:fieldHilight name="moneyTypeFreequency" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Plan entry frequency for employee deferral
			</TD>
			<TD class=dataColumn>
				<c:choose>
					<c:when test="${noticePlanCommonVO.moneyTypeFreequency!=null && noticePlanCommonVO.moneyTypeFreequency!='' && noticePlanCommonVO.moneyTypeFreequency!='U' && noticePlanCommonVO.moneyTypeFreequency!=' '}">
${noticePlanCommonVO.contributionMoneyType}&nbsp;&nbsp;
${noticePlanCommonVO.moneyTypeFreequency}
					</c:when>
					<c:otherwise>
						<p>Pending Plan Information Completion</p>
					</c:otherwise>
				</c:choose>
			
			</TD>
        </TR></TABLE></DIV>         
         <DIV class=evenDataRow> 
	      
      <TABLE class=dataTable>
        <TR vAlign=top>
        	<TD class=generalLabelColumn>
        	<ps:fieldHilight name="planEntryFreequencyDate" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				First plan entry date
			</TD>
			<TD class=dataColumn>
			<c:choose>
					<c:when test="${noticePlanCommonVO.planEntryFreequencyDate!=null && noticePlanCommonVO.planEntryFreequencyDate!='' }">
${noticePlanCommonVO.planEntryFreequencyDate}
				</c:when>
					<c:otherwise>
						<p>Pending Plan Information Completion</p>
					</c:otherwise>
				</c:choose>
			
			</TD>
        </TR></TABLE></DIV>
       
    						
        <DIV class=oddDataRow id="salaryDeferralElectionsId" style="display: block;">
      <TABLE class=dataTable>
        <TR vAlign=top>
          <TD class=generalLabelColumn>
          	<ps:fieldHilight name="planEmployeeDeferralElection" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
			Participants may change their salary deferral elections
		  </TD>
                           <td class="dataColumn"  style="border-top:1px solid #CCCCCC">
                             <c:choose>
								<c:when test="${noticePlanCommonVO.planEmployeeDeferralElection.employeeDeferralElectionCode!=null && noticePlanCommonVO.planEmployeeDeferralElection.employeeDeferralElectionCode!='' && noticePlanCommonVO.planEmployeeDeferralElection.employeeDeferralElectionCode!=' ' && noticePlanCommonVO.planEmployeeDeferralElection.employeeDeferralElectionCode!='U' }">	
<c:if test="${noticePlanCommonVO.planEmployeeDeferralElection.employeeDeferralElectionCode =='P'}">As of each payroll period</c:if>

<c:if test="${noticePlanCommonVO.planEmployeeDeferralElection.employeeDeferralElectionCode =='M'}">On the first day of each month</c:if>

<c:if test="${noticePlanCommonVO.planEmployeeDeferralElection.employeeDeferralElectionCode =='Q'}">On the first day of each plan year quarter</c:if>

<c:if test="${noticePlanCommonVO.planEmployeeDeferralElection.employeeDeferralElectionCode =='S'}">On the first day of the plan year or the first day of the 7th month of the plan year</c:if>

<c:if test="${noticePlanCommonVO.planEmployeeDeferralElection.employeeDeferralElectionCode =='Y'}">On the first day of the plan year</c:if>

<c:if test="${noticePlanCommonVO.planEmployeeDeferralElection.employeeDeferralElectionCode =='O'}">

 <c:if test="${noticePlanCommonVO.planEmployeeDeferralElection.employeeDeferralElectionSelectedDay!=''}">

<c:if test="${not empty noticePlanCommonVO.planEmployeeDeferralElection.employeeDeferralElectionSelectedMonths}">
													<c:set var="deferralMonthsLength" value="${fn:length(noticePlanCommonVO.planEmployeeDeferralElection.employeeDeferralElectionSelectedMonths)}"/>
<c:forEach items="${noticePlanCommonVO.planEmployeeDeferralElection.employeeDeferralElectionSelectedMonths}" var="selectedMonths" varStatus="counter" >
														   <c:if test="${selectedMonths == '01'}">Jan</c:if>
														   <c:if test="${selectedMonths == '02'}">Feb</c:if>
														   <c:if test="${selectedMonths == '03'}">Mar</c:if>
														   <c:if test="${selectedMonths == '04'}">Apr</c:if>
														   <c:if test="${selectedMonths == '05'}">May</c:if>
														   <c:if test="${selectedMonths == '06'}">Jun</c:if>
														   <c:if test="${selectedMonths == '07'}">Jul</c:if>
														   <c:if test="${selectedMonths == '08'}">Aug</c:if>
														   <c:if test="${selectedMonths == '09'}">Sep</c:if>
														   <c:if test="${selectedMonths == '10'}">Oct</c:if>
														   <c:if test="${selectedMonths == '11'}">Nov</c:if>
														   <c:if test="${selectedMonths == '12'}">Dec</c:if>
														   &nbsp;${noticePlanCommonVO.planEmployeeDeferralElection.employeeDeferralElectionSelectedDay}                                         
														   <c:if test="${deferralMonthsLength > 1 && (counter.index+1) != deferralMonthsLength }">,</c:if>
</c:forEach>
</c:if>
</c:if>
</c:if>
								</c:when>
								<c:otherwise>
									Unspecified
								</c:otherwise>
							</c:choose>
                       </td>
                                                  

        </TR></TABLE>
        </DIV>
     
       
     
    <DIV class=evenDataRow id="rothRowId">
      
      <TABLE class=dataTable>
        <TR vAlign=top>
			<TD class=generalLabelColumn>
			<ps:fieldHilight name="planAllowRothDeferrals" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Does the plan allow for ongoing Roth contributions? 
			</TD>
			<TD class=dataColumn>
<form:radiobutton onchange="setDirtyFlag();" path="planAllowRothDeferrals" id="planAllowRothDeferralsYes" value="Y" />Yes
<form:radiobutton onchange="setDirtyFlag();" path="planAllowRothDeferrals" id="planAllowRothDeferralsNo" value="N" />No
			</TD>
        </TR></TABLE>
        <TABLE class=dataTable >
        <TR vAlign=top>
			<TD class=generalLabelColumn>
				 
			</TD>
			<TD class=dataColumn>			
			<content:getAttribute id="dynamicTextRothQuestion" attribute="text"/></TD>
        </TR></TABLE>   
 		 </DIV>
     
        <DIV class=oddDataRow id="catchUpRowId">
      
      <TABLE class=dataTable>
        <TR vAlign=top>
			<TD class=generalLabelColumn>
			  <ps:fieldHilight name="catchUpContributionsAllowed" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Does plan allow for catch-up contributions? 
			</TD>
			<TD class=dataColumn>
				 
				<c:choose>
					<c:when test="${noticePlanCommonVO.catchUpContributionsAllowed!=null && (noticePlanCommonVO.catchUpContributionsAllowed=='Y'
					                                                                       || noticePlanCommonVO.catchUpContributionsAllowed=='N') }">
						<label for="catchUpContributionsAllowedYes"><input type="radio" disabled name="catchUpContributionsAllowed" id="catchUpContributionsAllowedYes" value="Y">Yes</label>
				 		<label for="catchUpContributionsAllowedNo"><input type="radio" disabled name="catchUpContributionsAllowed" id="catchUpContributionsAllowedNo" value="N">No</label>
					</c:when>
					<c:otherwise>
						<p>Pending Plan Information Completion</p>
					</c:otherwise>
				</c:choose>
				
				 
			</TD>
        </TR></TABLE>
            <TABLE class=dataTable id="catchupMessageId">
				        <TR vAlign=top>
							<TD class=generalLabelColumn>
								 
							</TD>
							<TD class=dataColumn>
							
							<content:getAttribute id="dynamicTextCatchUpQuestion" attribute="text"/></TD>
				        </TR>
				    </TABLE>   
					 
        
        </DIV>
        <DIV class=evenDataRow id="rollOverRowId">
      
      <TABLE class=dataTable>
        <TR vAlign=top>
			<TD class=generalLabelColumn>
			 <ps:fieldHilight name="isRolloverContribution" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Are rollover contributions permitted by the plan?  
			</TD>
			<TD class=dataColumn>
				<c:choose>
					<c:when test="${noticePlanCommonVO.planAllowRolloverContribution!=null && (noticePlanCommonVO.planAllowRolloverContribution=='Y' || noticePlanCommonVO.planAllowRolloverContribution=='N')}">
						 <label for="isRolloverContributionYes"><input type="radio" disabled name="isRolloverContribution" id="isRolloverContributionYes" value="Y">Yes</label>
				 		<label for="isRolloverContributionNo"><input type="radio" disabled name="isRolloverContribution" id="isRolloverContributionNo" value="N">No</label>
					</c:when>
					<c:otherwise>
						<p>Pending Plan Information Completion</p>
					</c:otherwise>
				</c:choose>
				
				
			</TD>
        </TR></TABLE></DIV>
        
      <DIV class=oddDataRow id="spdRowId">
      <TABLE class=dataTable>
        <TR vAlign=top>
          <TD class=generalLabelColumn>
          <ps:fieldHilight name="spdEmployeeContributionRef" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
			Provide the Summary Plan Description reference(s), such as section name, for Employee Contributions. Information entered will appear on Notices as entered. 
		  </TD>
          <TD class=dataColumn>
<form:textarea path="spdEmployeeContributionRef" cols="40" rows="5" id="spdEmployeeContributionRef" />

           </TD>
        </TR></TABLE></DIV>
        <table width="729" class="dataTable">
	<TR><TD class=sectionTitle><b>Distributions</b></TD></TR>
	</table>
	
           <DIV class=evenDataRow id="planAllowRowId">
      
      <TABLE class=dataTable>
        <TR vAlign=top>
			<TD class=generalLabelColumn>
			 <ps:fieldHilight name="planAllowLoans" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Does plan allow loans? 
			</TD>
			<TD class=dataColumn>
			
				<c:choose>
					<c:when test="${noticePlanCommonVO.planAllowLoans!=null && (noticePlanCommonVO.planAllowLoans=='Y' || noticePlanCommonVO.planAllowLoans=='N')}">
						 <label for="planAllowLoansYes"><input type="radio" disabled name="planAllowLoans" id="planAllowLoansYes" value="Y">Yes</label>
				 		<label for="planAllowLoansNo"><input type="radio" disabled name="planAllowLoans" id="planAllowLoansNo" value="N">No</label>
					</c:when>
					<c:otherwise>
						<p>Pending Plan Information Completion</p>
					</c:otherwise>
				</c:choose>
				
				
			</TD>
        </TR></TABLE></DIV>
    
    <DIV class=oddDataRow id="loanAmountAllowedId">
      <TABLE class=dataTable>
        <TR vAlign=top>
          <TD class=generalLabelColumn>
          <ps:fieldHilight name="loanPercentAllowed" singleDisplay="true" className="errorIcon" displayToolTip="true" style="position: relative; top: 25px;"/>          
			Participants may borrow up to 
		  </TD>
	
	
          <TD class=dataColumn>
    <input type="text" name="loanPercentAllowed" value="${noticePlanCommonVO.loanPercentAllowed}"  disabled="true" 
maxlength="7" size="7" cssClass="numericInput" id="maximumLoanPercentageTextId" />       
% of their vested balance, to a maximum of $ <input type="text" name="loanAmountAllowed" value="${noticePlanCommonVO.loanPercentAllowed}" disabled="true" maxlength="7" size="7" 
cssClass="numericInput" id="maximumLoanAmountTextId" />


                    <c:choose>      
					<c:when test="${(noticePlanCommonVO.planAllowLoans=='Y' && (noticePlanCommonVO.loanAmountAllowed==null || noticePlanCommonVO.loanPercentAllowed==null))}">
						<p>Pending Plan Information Completion</p>
					</c:when>
					</c:choose>

           </TD>
               </TR></TABLE></DIV>
          <DIV class=evenDataRow id="inServiceRowId">
      <TABLE class=dataTable>
        <TR vAlign=top>
          <TD class=generalLabelColumn>
          <ps:fieldHilight name="planAllowsInServiceWithdrawals" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
			Does the plan allow in-service withdrawals?   
		  </TD>
          <TD class=dataColumn>
<form:radiobutton onchange="setDirtyFlag();" path="planAllowsInServiceWithdrawals" id="planAllowsInServiceWithdrawalsYes" value="Y"/>Yes
<form:radiobutton onchange="setDirtyFlag();" path="planAllowsInServiceWithdrawals" id="planAllowsInServiceWithdrawalsNo" value="N"/>No
            </TD>
        </TR>
        
        </TABLE></DIV>
          <DIV class=oddDataRow id="isHardshipRowId">
      <TABLE class=dataTable>
        <TR vAlign=top>
          <TD class=generalLabelColumn>
          <ps:fieldHilight name="isHardshipWithdrawal" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
			Does plan allow hardship withdrawals?   
		  </TD>
          <TD class=dataColumn>
          <c:choose>
					<c:when test="${noticePlanCommonVO.planAllowsHardshipWithdrawal!=null && (noticePlanCommonVO.planAllowsHardshipWithdrawal=='Y'
					                                                                       || noticePlanCommonVO.planAllowsHardshipWithdrawal=='N') }">
            <label for="isHardshipWithdrawalYes"><input type="radio"  disabled name="isHardshipWithdrawal" id="isHardshipWithdrawalYes"  checked value="Y">Yes</label>
				 <label for="isHardshipWithdrawalNo"><input type="radio"  disabled name="isHardshipWithdrawal" id="isHardshipWithdrawalNo" value="N">No</label>	
	</c:when>
					<c:otherwise>
						<p>Pending Plan Information Completion</p>
					</c:otherwise>
				</c:choose>

           </TD>
        </TR>
         </TABLE></DIV>
         <DIV class=evenDataRow id="isHWRowId">
    </DIV>
        
	</TD></TR></TABLE>
</div>
<script type="text/javascript">
function setContributions(){
var salaryDeferralElections = "${noticePlanCommonVO.planEmployeeDeferralElection.employeeDeferralElectionCode}";
 document.getElementById('spdEmployeeContributionRef').setAttribute('maxlength', '100');
/* if(null==salaryDeferralElections || salaryDeferralElections=='' || salaryDeferralElections==' ' || salaryDeferralElections=='U')
{
document.getElementById("salaryDeferralElectionsId").style.display = "none";
modifyBgColorIfSalaryDeferralElectionsNull();		
}else
{
document.getElementById("salaryDeferralElectionsId").style.display = "block";
} */
var planAllowLoans = "${noticePlanCommonVO.planAllowLoans}";
if(planAllowLoans=="Y")
{
document.getElementById('planAllowLoansYes').checked = true;
	document.getElementById('planAllowLoansNo').checked = false;
}else if(planAllowLoans=="N")
{
document.getElementById('planAllowLoansYes').checked = false;
	document.getElementById('planAllowLoansNo').checked = true;		
}
var isHardshipWithdrawal = "${noticePlanCommonVO.planAllowsHardshipWithdrawal}";
if(isHardshipWithdrawal=="Y")
{
document.getElementById('isHardshipWithdrawalYes').checked = true;
	document.getElementById('isHardshipWithdrawalNo').checked = false;
	//document.getElementById('contirbutionRestirictionOnHardships').disabled = false;
	
}else if(isHardshipWithdrawal=="N")
{
document.getElementById('isHardshipWithdrawalYes').checked = false;	
}
var catchUpContributionsAllowed = "${noticePlanCommonVO.catchUpContributionsAllowed}";

if(catchUpContributionsAllowed=="Y")
{
	document.getElementById('catchUpContributionsAllowedYes').checked = true;
	document.getElementById('catchUpContributionsAllowedNo').checked = false;
	document.getElementById('catchupMessageId').style.display = "block";
}else if(catchUpContributionsAllowed=="N")
{
	document.getElementById('catchUpContributionsAllowedYes').checked = false;
	document.getElementById('catchUpContributionsAllowedNo').checked = true;
	document.getElementById('catchupMessageId').style.display = "none";		
}else
{
document.getElementById('catchupMessageId').style.display = "none";
}
var isRolloverContribution = "${noticePlanCommonVO.planAllowRolloverContribution}";
if(isRolloverContribution=="Y")
{
	document.getElementById('isRolloverContributionYes').checked = true;
	document.getElementById('isRolloverContributionNo').checked = false;
}else if(isRolloverContribution=="N")
{

	document.getElementById('isRolloverContributionYes').checked = false;
	document.getElementById('isRolloverContributionNo').checked = true;
}		
	allowMaxEmployeeRothDeferrals();
}

$(document).ready(function(){
document.getElementById('dirtyFlagId').value = "false";

$('#spdEmployeeContributionRef').change(function() {
		setDirtyFlag();
		var spdRef = document.getElementById('spdEmployeeContributionRef').value;
		
	if(spdRef !=null && spdRef != ''){
	for (var i = 0; i < spdRef.length; i++) {
				var isAsciiPrintable=spdRef.charCodeAt(i) >= 32 && spdRef.charCodeAt(i) < 127;
				if(!isAsciiPrintable || spdRef.charAt(i)=='?'){
					alert(ERR_SPECIAL_CHARS);
					document.getElementById('spdEmployeeContributionRef').value ="";
					break;
				}
		}
	}
		
		return false;
	});
setContributions();
});

//To display tables rows in alternate colors if PIF value is suppressed
function modifyBgColorIfSalaryDeferralElectionsNull(){
	$("#rothRowId").removeClass("oddDataRow");
	$("#rothRowId").removeClass("evenDataRow");
	$("#rothRowId").addClass("oddDataRow");
	
	$("#catchUpRowId").removeClass("oddDataRow");
	$("#catchUpRowId").removeClass("evenDataRow");
	$("#catchUpRowId").addClass("evenDataRow");
	
	$("#rollOverRowId").removeClass("evenDataRow");
	$("#rollOverRowId").removeClass("oddDataRow");
	$("#rollOverRowId").addClass("oddDataRow");
	
	$("#spdRowId").removeClass("oddDataRow");
	$("#spdRowId").removeClass("evenDataRow");
	$("#spdRowId").addClass("evenDataRow");
	
	$("#planAllowRowId").removeClass("evenDataRow");
	$("#planAllowRowId").removeClass("oddDataRow");
	$("#planAllowRowId").addClass("oddDataRow");
	
	$("#loanAmountAllowedId").removeClass("oddDataRow");
	$("#loanAmountAllowedId").removeClass("evenDataRow");
	$("#loanAmountAllowedId").addClass("evenDataRow");
	
	$("#inServiceRowId").removeClass("evenDataRow");
	$("#inServiceRowId").removeClass("oddDataRow");
	$("#inServiceRowId").addClass("oddDataRow");
	
	$("#isHardshipRowId").removeClass("oddDataRow");
	$("#isHardshipRowId").removeClass("evenDataRow");
	$("#isHardshipRowId").addClass("evenDataRow");
	
	$("#isHWRowId").removeClass("evenDataRow");
	$("#isHWRowId").removeClass("oddDataRow");
	$("#isHWRowId").addClass("oddDataRow");
	
}

</script>

</html>