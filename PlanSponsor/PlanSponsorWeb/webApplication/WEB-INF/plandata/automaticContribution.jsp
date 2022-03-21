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
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>  
    
  <%@ page import="java.math.BigDecimal"%>
<%@ page import="com.manulife.pension.ps.web.plandata.TabPlanDataForm"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%> 
<%@ page import="com.manulife.pension.ps.web.plandata.util.TPAPlanDataWebUtility"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>  
<%@ page import="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO" %>

<content:contentBean contentId="<%=ContentConstants.AUTO_CONTRIBUTION_TAB_INSTRUCTION%>" type="<%=ContentConstants.TYPE_DISCLAIMER%>" id="autoContributionInstruction" />
<content:contentBean contentId="<%=ContentConstants.AUTO_CONTRIBUTION_APPLIES_TO_MESSAGE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="autoContributionAppliesToMessage" />
<c:set var="noticePlanCommonVO" value="${sessionScope.noticePlanCommonVO}" />


  

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid.css" type="text/css">
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

.formulaFirstColumn {
	 width: 125px;
	 vertical-align: middle;
}

.generalLabelExtendedColumn {
	width: 230px;
}
#spdEmployerContributionRef {
    resize:none;
    overflow:auto;	
}
#contributionFeature3SummaryText {
    resize:none;
    overflow:auto;	
}
#qACASHNonElectivePlanName {
    resize:none;
    overflow:auto;	
}
#safeHarbourPlanName {
    resize:none;
    overflow:auto;	
}
#qACASummaryPlanDesc {
    resize:none;
    overflow:auto;	
}
</style>

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
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Automatic Contributions</title>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script>
</head>
<body>


<input type="hidden" name="contributionFeature1PctMissing" id="contributionFeature1PctMissing"/>
<input type="hidden" name="contributionFeature2DateIdMissing" id="contributionFeature2DateIdMissing"/>
<input type="hidden" name="contributionFeature3SummaryTextMissing" id="contributionFeature3SummaryTextMissing"/>
<input type="hidden" name="automaticContributionProvisionTypeHidden" id="automaticContributionProvisionType"/>
<input type="hidden" name="eacaEnablePopUpForEmployerContributions" id="eacaEnablePopUpForEmployerContributionsId"/>
<input type="hidden" name="qacaEnablePopUpForEmployerContributions" id="qacaEnablePopUpForEmployerContributionsId"/>

<div id="generalTabDivId" class="borderedDataBox">
<table width="729" class="dataTable">
			<TR><TD class=subsubhead><content:getAttribute id="autoContributionInstruction" attribute="text"/></TD></TR>
		</table>

<TABLE border=0 cellSpacing=0 cellPadding=0 width=729>
    <TR>
    <TD width="100%"><!--[if lt IE 7]>
    <link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid_IE.css" type="text/css">
	<![endif]-->
	  <DIV class=evenDataRow>      
      <TABLE class=dataTable>
        <TR vAlign=top>
        	<TD class=generalLabelExtendedColumn>
        	 <ps:fieldHilight name="automaticContributionProvisionType" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Select automatic contribution provision type
			</TD>
			<TD class=dataColumn>
<form:radiobutton onchange="setDirtyFlag();return verifySHCompleted();" path="automaticContributionProvisionType" id="automaticContributionProvisionType1" value="ACA" />Automatic Contribution Arrangement (ACA)<br/>
<form:radiobutton onchange="setDirtyFlag();return verifySHCompleted();" path="automaticContributionProvisionType" id="automaticContributionProvisionType2" value="EACA" />Eligible Automatic Contribution Arrangement (EACA)<br/>
<form:radiobutton onchange="setDirtyFlag();return verifySHCompleted();" path="automaticContributionProvisionType" id="automaticContributionProvisionType3" value="QACA" />Qualified Automatic Contribution Arrangement (QACA)
         
			</TD>
        </TR></TABLE></DIV>
        <div id="acaFieldsDiv" style="display: block;">
        <DIV class=oddDataRow>
	<TABLE class=dataTable>
        <TR vAlign=top>
        	<TD class=generalLabelExtendedColumn>
        	 <ps:fieldHilight name="effectiveDate" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Effective date of automatic contribution (enrollment) provision
			</TD>
			
			<TD class=dataColumn>
			<c:choose>
					<c:when test="${tabPlanDataForm.effectiveDate !=null }">
					<span>
<form:input path="effectiveDate" disabled="${disableFieldsForContributions}" maxlength="10" size="10" id="acpEffectiveDateId"/>



					</span> 
					</c:when>
					<c:otherwise>
						<p>Pending Plan Information Completion</p>
					</c:otherwise>
				</c:choose>
			</TD>
        </TR></TABLE>
        </DIV>
         <DIV class=evenDataRow>
	<TABLE class=dataTable>
        <TR vAlign=top>
        	<TD class=generalLabelExtendedColumn>
        	 <ps:fieldHilight name="defaultDeferralPercentage" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Initial default deferral percentage for automatic contributions (enrollment)			
				
			</TD>
			<TD class=dataColumn>
				<c:choose>
					<c:when test="${noticePlanCommonVO.defaultDeferralPercentage !=null }">
<input type="text"  name="defaultDeferralPercentage" disabled="${disableFieldsForContributions}" value="${noticePlanCommonVO.defaultDeferralPercentage}" style="direction: rtl;" maxlength="6" size="10" cssClass="numericInput" />%
					</c:when>
					<c:otherwise>
						<p>Pending Plan Information Completion</p>
					</c:otherwise>
				</c:choose>
			</TD>
        </TR></TABLE>
        </DIV>
          <DIV class=oddDataRow>      
      <TABLE class=dataTable>
        <TR vAlign=top>
        	<TD class=generalLabelExtendedColumn width="210">
        	 <ps:fieldHilight name="planAllowsInServiceWithdrawals" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
        	 The notice states that automatic contribution feature applies <strong>"if you are eligible for the Plan and have not elected to make contributions, or to opt out of making contributions".</strong> In addition to this statement, select who else this automatic contribution feature applies to, if applicable.  If a selection is made, the Notice will include your selection as indicated.				
			</TD>
			<TD class=dataColumn>
			 	<ps:fieldHilight name="contributionFeature1Pct" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
<form:checkbox path="automaticContributionFeature1" id="automaticContributionFeature1" onclick="enableContributionFeature1Pct();" onchange="setDirtyFlag();" value="0"/>

								   If you are contributing less than
<form:input path="contributionFeature1Pct" disabled="${disableFieldsForContributions}" maxlength="5" onblur="validateContributionLessDecimal(this, 'Y')" onchange="setDirtyFlag()" size="10" cssClass="numericInput" id="contributionFeature1Pct" />%<br/>
         		 
         		 <ps:fieldHilight name="contributionFeature2Date" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
<form:checkbox path="automaticContributionFeature2" id="automaticContributionFeature2" onclick="enableContributionFeature2DateId();" onchange="setDirtyFlag();" value="0"/>

								   If you are hired after
<form:input path="contributionFeature2Date" disabled="${disableFieldsForContributions}" maxlength="10" onblur="validateHireAfterDate(this)" onchange="setDirtyFlag();" size="10" id="contributionFeature2DateId"/>





							   
					  <img id="contributionFeature2DateIdImage" onclick="return handleDateIconClicked(event, 'contributionFeature2DateId');" src="/assets/unmanaged/images/cal.gif" border="0"> (mm/dd/yyyy)
					
					<br/>
			 	  
			 	  <ps:fieldHilight name="contributionFeature3SummaryText" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
<form:checkbox path="automaticContributionFeature3" id="automaticContributionFeature3" onclick="enableContributionFeature3SummaryText();" onchange="setDirtyFlag();" value="0"/>

Complete if other<br/> <form:textarea path="contributionFeature3SummaryText" cols="40" disabled="${disableFieldsForContributions}" onchange="setDirtyFlag();" rows="5" id="contributionFeature3SummaryText" />
         
			</TD>
			<TD width="180">
			<content:getAttribute id="autoContributionAppliesToMessage" attribute="text"/>
			<br/>			
			</TD>
        </TR></TABLE></DIV>
       <!-- PIF data values -->
          <DIV class=evenDataRow>
	<TABLE class=dataTable>
        <TR vAlign=top>
        	<TD class=generalLabelExtendedColumn>
        	 <ps:fieldHilight name="aciAllowed" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
        	 <ps:fieldHilight name="aciAllowedUnspecified" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Does the plan provide for automatic contribution increases? 			
				
			</TD>
			<TD class=dataColumn>
			<c:choose>
					<c:when test="${noticePlanCommonVO.aciAllowed!=null && noticePlanCommonVO.aciAllowed!='' && noticePlanCommonVO.aciAllowed!=' '}">
					<c:choose>
					<c:when test="${noticePlanCommonVO.aciAllowed =='Y'}">
						Yes
						</c:when>
						<c:when test="${noticePlanCommonVO.aciAllowed =='N'}">
						No
						</c:when>
						<c:when test="${noticePlanCommonVO.aciAllowed =='U'}">
						Unspecified
						</c:when>
						<c:otherwise>
${noticePlanCommonVO.aciAllowed}
					</c:otherwise>
					</c:choose>
					</c:when>
					<c:otherwise>
						<p>Pending Plan Information Completion</p>
					</c:otherwise>
				</c:choose>
			</TD>
        </TR></TABLE>
        </DIV>
        <c:if test="${noticePlanCommonVO.aciAllowed =='Y'}">
          <DIV class=oddDataRow>
	<TABLE class=dataTable>
        <TR vAlign=top>
        	<TD class=generalLabelExtendedColumn>
        	 <ps:fieldHilight name="aciApplyDate" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				When are annual increases applied?
				
			</TD>
			<TD class=dataColumn>
<form:radiobutton disabled="${disableFieldsForContributions}" onchange="setDirtyFlag();" path="acaAnnualIncreaseType" id="acaAnnualIncreaseType1" value="1"></form:radiobutton>
<input type="text"  name="annualApplyDate" disabled="${disableFieldsForContributions}" value="${noticePlanCommonVO.annualApplyDate}" style="direction: rtl;" maxlength="6" size="10" cssClass="numericInput" /> (mm/dd)
					   <br/>
<form:radiobutton disabled="${disableFieldsForContributions}" onchange="setDirtyFlag();" path="acaAnnualIncreaseType" id="acaAnnualIncreaseType2" value="2"/>the pay date following the anniversary date you entered into the Plan<br/>
<form:radiobutton disabled="${disableFieldsForContributions}" onchange="setDirtyFlag();" path="acaAnnualIncreaseType" id="acaAnnualIncreaseType3" value="3"/> the pay date following the anniversary of your date of hire
        	
			</TD>
        </TR></TABLE>
        </DIV>
          <DIV class=evenDataRow>
	<TABLE class=dataTable>
        <TR vAlign=top>
        	<TD class=generalLabelExtendedColumn>
        	 <ps:fieldHilight name="annualIncrease" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Default annual increase 		
				
			</TD>
			<TD class=dataColumn>
<input type="text"  name="defaultIncreasePercent" disabled="${disableFieldsForContributions}" value="${noticePlanCommonVO.defaultIncreasePercent}" maxlength="6" size="10" style="direction: rtl;" cssClass="numericInput" />%
				</TD>
        </TR></TABLE>
        </DIV>
         <DIV class=oddDataRow>
	<TABLE class=dataTable>
        <TR vAlign=top>
        	<TD class=generalLabelExtendedColumn>
        	 <ps:fieldHilight name="maxAutomaticIncrease" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Default maximum for automatic increase
			</TD>
			<td class=dataColumn>
<input type="text"  name="defaultAutoIncreaseMaxPercent" disabled="${disableFieldsForContributions}" value="${noticePlanCommonVO.defaultAutoIncreaseMaxPercent}" style="direction: rtl;" maxlength="6" size="10" cssClass="numericInput" />%
				
				</td>
        </TR></TABLE>
        </DIV>
        </c:if>
          <c:if test="${noticePlanCommonVO.aciAllowed !='Y'}">
        <DIV class=oddDataRow>
			<TABLE class=dataTable>
       			 <TR vAlign=top>
        	<TD class=generalLabelExtendedColumn>&nbsp;
        	</TD>
        	</TR>
        	</TABLE>
        	</DIV>
        </c:if> 
       <!-- PIF data values -->   
         
            </div>
            <div id="eacaFieldsDiv" style="display: block;">
            <DIV class=oddDataRow>
				<TABLE class=dataTable>
			        <TR vAlign=top>
			        	<TD class=generalLabelExtendedColumn>
			        	<ps:fieldHilight name="autoContributionWD" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
							Does the plan allow for automatic contribution withdrawals?
						</TD>
						<TD class=dataColumn>
<form:radiobutton onchange="setDirtyFlag();" path="autoContributionWD" id="autoContributionWDYes" value="Y"/>Yes
<form:radiobutton onchange="setDirtyFlag();" path="autoContributionWD" id="autoContributionWDNo" value="N"/>No
						</TD>
			        </TR>
			    </TABLE>      
        	</DIV>
        <DIV class=evenDataRow>
	<TABLE class=dataTable>
        <TR vAlign=top>
        	<TD class=generalLabelExtendedColumn>
        	<ps:fieldHilight name="automaticContributionDays" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
        	 <ps:fieldHilight name="automaticContributionDaysOther" singleDisplay="true" className="errorIcon" displayToolTip="true" style="position: relative; top: 15px;"/>
				Participants may elect to withdraw their automatic contributions no later than 
			</TD>
			<TD class=dataColumn>
			<form:select  path="automaticContributionDays" styleId="automaticContributionDays" onchange="showorhideACDOther();setDirtyFlag();">
				  <form:option value="90">90</form:option>
				  <form:option value="60">60</form:option>
				  <form:option value="30">30</form:option>
				  <form:option value="00">Other</form:option>
</form:select>&nbsp;days after the first automatic contribution is taken from their compensation.
			<span id="automaticContributionDaysOtherId" style="display: block;">
<form:input path="automaticContributionDaysOther" maxlength="2" onblur="validateOtherDays(this);" onchange="setDirtyFlag();" size="10" id="automaticContributionDaysOther"/>




					 
					</span> 
			</TD>
        </TR></TABLE>
        </DIV>
        <DIV class=oddDataRow>
	<TABLE class=dataTable>
        <TR vAlign=top>
        	<TD class=generalLabelExtendedColumn>
        	 <ps:fieldHilight name="employerContributions" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
        	 <ps:fieldHilight name="eacaAutoContribYes" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
				Does the plan have Employer contributions
			</TD>
			<TD class=dataColumn>
<form:radiobutton onchange="setDirtyFlag();" onclick="showSPDEmployerContributionRef();" path="employerContributions" id="employerContributionsYes" value="Y"/>Yes
<form:radiobutton onchange="setDirtyFlag();" onclick="showSPDEmployerContributionRef();" path="employerContributions" id="employerContributionsNo" value="N"/>No
    
			</TD>
        </TR>
        
        
        </TABLE>
        
      
        </DIV>
        <div id="spdEmployerContributionRefDivId" style="display: block;">
         <DIV class=evenDataRow>
	<TABLE class=dataTable>
        <TR vAlign=top>
        	<TD class=generalLabelExtendedColumn>
        	 <ps:fieldHilight name="spdEmployerContributionRef" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
        	 If yes, provide the Summary Plan Description reference(s), such as section name, for this Employer Contribution. Information entered will appear on Notices as entered.
				 
			</TD>
			<TD class=dataColumn>
<form:textarea path="spdEmployerContributionRef" cols="40" onchange="setDirtyFlag();" rows="5" cssStyle="margin: 2px;" id="spdEmployerContributionRef" />
			</TD>
        </TR></TABLE>
        </DIV>
        </div>
          <div class="evenDataRow" id="vestingEACA" style="width: 729px;">
          <span id="vestingEACASpan" style="display: block;">
          
			    	<jsp:include page="/WEB-INF/plandata/EACAvesting.jsp"/>
			    	</span>
			    </div>
         </div>
           <div id="qacaFieldsDiv" style="display: none;">
      <div class=evenDataRow>
			      <table class=dataTable>
			        <tr vAlign=top>
						<td class=generalLabelExtendedColumn>
						 <ps:fieldHilight name="qACAPlanHasSafeHarborMatchOrNonElective" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
							Select the plan's type of Safe Harbor
						</td>
						<td class=dataColumn>
<form:select path="qACAPlanHasSafeHarborMatchOrNonElective" onchange="setDirtyFlag();" styleId="qACAPlanHasSafeHarborMatchOrNonElective">
							 	<form:option value="">Select one</form:option>
							 	<form:option value="SHMAC">Match</form:option>
								<form:option value="SHNEC">Non-elective</form:option>
</form:select>
						</td>
			        </tr>
			      </table>			      
			    </div>
			       <div class="oddDataRow" id="SafeHarborMatchId">
			      <table class=dataTable>
			        <tr vAlign=top>
						<td class=generalLabelExtendedColumn>
							<table style="margin-top: 6px" id="shMatchVestingTable">
								<tbody>
									<tr vAlign=top>
										<td style="padding:1px;">
											<div class="formulaHeader">&nbsp;Matching Contributions
											 <ps:fieldHilight name="qACAMatchContributionContribPct2" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
											</div>
											<div class="formulaBody" id="2_match_div">
												<table style="width: 100%">
													<tbody>
														<tr vAlign=top>
															<td>
<form:input path="qACAMatchContributionContribPct1" maxlength="5" onblur="validateQACAMatchingContributions(this, 'Y')" onchange="setDirtyFlag()" size="5" cssStyle="width: 34px;" cssClass="numericInput" />% of the first
<form:input path="qACAMatchContributionMatchPct1" maxlength="5" onblur="assignPctValues();validateQACAMatchingContributionsValues(this, 'Y')" onchange="setDirtyFlag()" size="5" cssStyle="width: 34px;" cssClass="numericInput" id="qACAMatchContributionMatchPct1" />%
															</td>
														</tr>
														<tr vAlign=top>
															<td>
plus <form:input path="qACAMatchContributionContribPct2" maxlength="5" onblur="validateQACAMatchingContributionsPctValues(this, 'Y');populateQACASafeHarborRow2Column2();" onchange="setDirtyFlag()" size="5" cssStyle="width: 33px;" cssClass="numericInput" id="qACAMatchContributionContribPct2" /><%-- name="tabPlanDataForm" --%>% between
<form:input path="qACAMatchContributionMatchPct1Value" disabled="${disableFieldsForContributions}" maxlength="3" size="3" cssStyle="width: 26px;" cssClass="numericInput" id="qACAMatchContributionMatchPct1Value" />% and
<form:input path="qACAMatchContributionMatchPct2" maxlength="5" onblur="validateQACAMatchingContributionsPercentValues(this, 'Y');populateQACASafeHarborRow2Column2();" onchange="setDirtyFlag()" size="5" cssStyle="width: 31px; padding-left: 0px; padding-right: 0px;" cssClass="numericInput" id="qACAMatchContributionMatchPct2" />%
															</td>
														</tr>
													</tbody>
												</table>				
											</div>
										</td>			
									</tr>
									<tr vAlign=top>
										<td style="padding:2px;">
						
										</td>
									</tr>
								</tbody>
							</table>							
						</td>
						<td class=dataColumn>
							<table>
								<tr vAlign=top>
									<td style="padding-bottom: 7px;"> <ps:fieldHilight name="qACAArrangementOptions" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
									Safe Harbor Match contributions apply to each</td>
									<td style="padding-bottom: 7px;">
<form:select path="qACAArrangementOptions" onchange="setDirtyFlag();" >
<form:options items="${tabPlanDataForm.matchAppliesToContribList}" itemLabel="label" itemValue="value"/>
</form:select>
								 	</td>
								</tr>
							
								<tr vAlign=top>
									<td style="padding-bottom: 7px;">
									 <ps:fieldHilight name="qACAMatchContributionToAnotherPlan" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
									Are matching contributions being made to another plan?</td>
									<td style="padding-bottom: 7px;">
<form:radiobutton onchange="setDirtyFlag();" path="qACAMatchContributionToAnotherPlan" id="anotherPlanMatchingContributionYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton onchange="setDirtyFlag();" path="qACAMatchContributionToAnotherPlan" id="anotherPlanMatchingContributionNo" value="${planDataConstants.NO_CODE}" />No

								 	</td>
								</tr>
								<tr vAlign=top>
									<td>
									 <ps:fieldHilight name="qACAMatchContributionOtherPlanName" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
									If yes, enter the plan name</td>
									<td>
<form:textarea path="qACAMatchContributionOtherPlanName" cols="40" disabled="disabled" onchange="setDirtyFlag();" rows="5" id="safeHarbourPlanName" />
								 	</td>
								</tr>
								<div id="SafeHarborRothContributionDivId" style="display:none">
								<tr vAlign=top>
									<td>
									 <ps:fieldHilight name="qACASafeHarborAppliesToRoth" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
									Does Safe Harbor match apply to Roth contributions?</td>
									<td>
<form:radiobutton onchange="setDirtyFlag();" path="qACASafeHarborAppliesToRoth" id="SHMAppliedToRothContributionYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton onchange="setDirtyFlag();" path="qACASafeHarborAppliesToRoth" id="SHMAppliedToRothContributionNo" value="${planDataConstants.NO_CODE}" />No

								 	</td>
								</tr></div>
								<div id="SafeHarborCatchUpContributionDivId" style="display:none">
								<tr vAlign=top>
									<td>
									 <ps:fieldHilight name="qACASHAppliesToCatchUpContributions" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
									Does Safe Harbor match apply to Catch-up contributions?</td>
									<td>
<form:radiobutton onchange="setDirtyFlag();" path="qACASHAppliesToCatchUpContributions" id="SHMAppliedToCatchUpContributionYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton onchange="setDirtyFlag();" path="qACASHAppliesToCatchUpContributions" id="SHMAppliedToCatchUpContributionNo" value="${planDataConstants.NO_CODE}" />No

								 	</td>
								</tr></div>
									
								
								
												</table>
						</td>
									        </tr>
			      </table>			      
			    </div>
			
			   <div class="oddDataRow" id="SafeHarborNonElectiveId">
			      <table class=dataTable>
			        <tr vAlign=top>
						<td class=generalLabelExtendedColumn>
							<table style="margin-top: 6px" id="shNonElectiveVestingTable">
								<tbody>
									<tr>
										<td style="vertical-align:top;">
											<div class="formulaHeader">&nbsp;Non-elective Contributions <ps:fieldHilight name="qACANonElectiveContributionPct" singleDisplay="true" className="errorIcon" displayToolTip="true"/></div>
											<div class="formulaBody" id="2non_match_div">
												<div style="padding-left:10px; padding-top:2px;padding-bottom:2px;">
<form:input path="qACANonElectiveContributionPct" maxlength="5" onblur="validateQACASHNonElective(this, 'Y')" onchange="setDirtyFlag()" size="5" cssClass="numericInput" />&nbsp;% of compensation&nbsp;&nbsp;
												</div>
											</div>
										</td>
									</tr>
									<tr vAlign=top>
										<td style="padding:2px;">
						
										</td>
									</tr>
								</tbody>
							</table>							
						</td>
						<td class=dataColumn>
							<table>
								<tr vAlign=top>
									<td style="padding-bottom: 7px;"> <ps:fieldHilight name="qACANonElectiveAppliesToContrib" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
									Safe Harbor Non-elective contributions apply to each</td>
									<td style="padding-bottom: 7px;">
<form:select path="qACANonElectiveAppliesToContrib" onchange="setDirtyFlag();" >
<form:options items="${tabPlanDataForm.matchAppliesToContribList}" itemLabel="label" itemValue="value"/>
</form:select>
								 	</td>
								</tr>
								<tr vAlign=top>
									<td style="padding-bottom: 7px;"> <ps:fieldHilight name="qACANonElectiveContribOtherPlan" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
									Are Non-elective contributions being made to another plan?</td>
									<td style="padding-bottom: 7px;">
<form:radiobutton onchange="setDirtyFlag();" path="qACANonElectiveContribOtherPlan" id="SHNEAppliedToAnotherPlanYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton onchange="setDirtyFlag();" path="qACANonElectiveContribOtherPlan" id="SHNEAppliedToAnotherPlanNo" value="${planDataConstants.NO_CODE}" />No

								 	</td>
								</tr>
								<tr vAlign=top>
									<td>
									 <ps:fieldHilight name="qACASHNonElectivePlanName" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
									 If yes, enter the plan name</td>
									<td>
<form:textarea path="qACASHNonElectivePlanName" cols="40" disabled="disabled" onchange="setDirtyFlag();" rows="5" id="qACASHNonElectivePlanName" />
								 	</td>
								</tr>
							</table>
						</td>
			        </tr>
			      </table>			      
			    </div>
			       
			       <div id="safeHarborVestingTable">
			       <div class="formulaHeader">Complete the Safe Harbor Vesting Information								 
									<ps:fieldHilight name="qACASHMatchVesting" singleDisplay="true" className="errorIcon" displayToolTip="true"/>								
									</div>
											<div class="formulaBody" id="2_match_div">
										<table>
										<tbody>
								<tr vAlign=top>
<td><form:radiobutton onchange="setDirtyFlag();" onclick="showorhideQACASHMatchVestingNoFields();" path="qACASHMatchVesting" id="qACASHMatchVestingYes" value="${planDataConstants.YES_CODE}" />100% vested when contributed <br/>

<form:radiobutton onchange="setDirtyFlag();" onclick="showorhideQACASHMatchVestingNoFields();" path="qACASHMatchVesting" id="qACASHMatchVestingNo" value="${planDataConstants.NO_CODE}" />Complete formula</td>

								
								</tr>
										<tr>
										<td width="20%" style="padding:2px;" class="formulaHeader">
										<div id="yearsOfServiceId">
                                	<table border="1">
                                		<tr style="border: 1px; padding: 2px;font-weight: bold;">
	                                      <td>Years of Service</td>
	                                      <td>Percentage	                                    
	                                      </td>
	                                       <ps:fieldHilight name="qACASHMatchVestingPct1" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
                                    	</tr>
<tr class="evenDataRow"><td>Less than one year</td><td><form:input path="qACASHMatchVestingPct1" disabled="${disableFieldsForContributions}" maxlength="5" onblur="validateSHMatchVesting1(this, 'Y')" onchange="setDirtyFlag()" size="5" cssStyle="width: 32px;" cssClass="numericInput" id="qACASHMatchVestingPct1"/>%</td></tr>

<tr class="evenDataRow"><td>One Year, but less than Two Years</td><td><form:input path="qACASHMatchVestingPct2" disabled="${disableFieldsForContributions}" maxlength="5" onblur="validateSHMatchVesting2(this, 'Y')" onchange="setDirtyFlag()" size="5" cssStyle="width: 32px;" cssClass="numericInput" id="qACASHMatchVestingPct2"/>%</td></tr>

							   <tr class="evenDataRow"><td>Two or more years</td><td>100%</tr>
                                </table>
                                </div>
										</td>
									</tr>	
								</tbody></table></div>
								</div>
								
			       <div class="oddDataRow" id="additionalEC">
			      <table class=dataTable>
			        <tr vAlign=top>
						<td class=generalLabelExtendedColumn>
						 <ps:fieldHilight name="qACAPlanHasAdditionalEC" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
						 	Does the plan allow automatic contribution withdrawals?  
						</td>
						<td class=dataColumn>
<form:radiobutton onchange="setDirtyFlag();" onclick="showorhideqACAAutomaticContributionDays();" path="qACAPlanHasAdditionalEC" id="qACAPlanHasAdditionalECYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton onchange="setDirtyFlag();" onclick="showorhideqACAAutomaticContributionDays();" path="qACAPlanHasAdditionalEC" id="qACAPlanHasAdditionalECNo" value="${planDataConstants.NO_CODE}" />No

						</td>
			        </tr>
			      </table>			      
			    </div>
			      <DIV class=evenDataRow id="qACAAutoDays">
	<TABLE class=dataTable>
        <TR vAlign=top>
        	<TD class=generalLabelExtendedColumn>
        	<ps:fieldHilight name="qACAAutomaticContributionDays" singleDisplay="true" className="errorIcon" displayToolTip="true"/>        	
			 <ps:fieldHilight name="qACAAutomaticContributionDaysOther" singleDisplay="true" className="errorIcon" displayToolTip="true" style="position: relative; top: 15px;"/>
			 Participants may elect to withdraw their automatic contributions no later than			 
			</TD>
			<TD class=dataColumn>
			<form:select onchange="setDirtyFlag();" disabled="${disableFieldsForContributions}" path="qACAAutomaticContributionDays" styleId="qACAAutomaticContributionDays" onchange="showorhideQACDOther();">
				  <form:option value="90">90</form:option>
				  <form:option value="60">60</form:option>
				  <form:option value="30">30</form:option>
				  <form:option value="00">Other</form:option>
</form:select>&nbsp;days after the first automatic contribution is taken from their compensation.
			<span id="qACAAutomaticContributionDaysOtherId" style="display: block;">
<form:input path="qACAAutomaticContributionDaysOther" maxlength="2" onblur="validateOtherDays(this);" onchange="setDirtyFlag();" size="10" id="qACAAutomaticContributionDaysOther"/>




					 
					</span> 
			</TD>
        </TR></TABLE>
        </DIV>
      <div class="oddDataRow" id="additionalECon">
			      <table class=dataTable>
			        <tr vAlign=top>
						<td class=generalLabelExtendedColumn>
						 <ps:fieldHilight name="qACAPlanHasAdditionalECon" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
						 <ps:fieldHilight name="qacaAutoContribYes" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
						 	Does the plan have Additional Employer Contributions?
						</td>
						<td class=dataColumn>
<form:radiobutton onchange="setDirtyFlag();" onclick="showorhideQACASummaryPlanDesc();" path="qACAPlanHasAdditionalECon" id="qACAPlanHasAdditionalEConYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton onchange="setDirtyFlag();" onclick="showorhideQACASummaryPlanDesc();" path="qACAPlanHasAdditionalECon" id="qACAPlanHasAdditionalEConNo" value="${planDataConstants.NO_CODE}" />No

						</td>
			        </tr>
			      </table>			      
			    </div>
        		<div class="evenDataRow" id="additionalECRef">
			      <table class=dataTable>
			        <tr vAlign=top>
						<td class=generalLabelExtendedColumn>
						 <ps:fieldHilight name="qACASummaryPlanDesc" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
						 If yes, provide the Summary Plan Description reference(s), such as section name, for this Employer Contribution. Information entered will appear on Notices as entered.						 	 
						</td>
						<td class=dataColumn>
<form:textarea path="qACASummaryPlanDesc" cols="40" disabled="${disableFieldsForContributions}" onchange="setDirtyFlag();" rows="5" cssStyle="margin: 2px;" id="qACASummaryPlanDesc" />
						</td>
			        </tr>
			      </table>	      
			      			      
			    </div>
			<div class="evenDataRow" id="vestingQACA" style="width: 729px;">
			
			<span id="vestingQACASpan" style="display: block;" >
					
			    	<jsp:include page="/WEB-INF/plandata/QACAvesting.jsp"/>
			    	</span>
			    </div>
        </div>
        </TD>
        </TR>
        </TABLE>
        </div>
        <script>
       
        
$(document).ready(function(){
$('#contributionFeature3SummaryText').change(function() {
		setDirtyFlag();
		var summaryText = document.getElementById('contributionFeature3SummaryText').value;		
		if(summaryText !=null && summaryText != ''){			
		    for (var i = 0; i < summaryText.length; i++) {
				var isAsciiPrintable=summaryText.charCodeAt(i) >= 32 && summaryText.charCodeAt(i) < 127;
				if(!isAsciiPrintable || summaryText.charAt(i)=='?'){
					alert(ERR_SPECIAL_CHARS);
					document.getElementById('contributionFeature3SummaryText').value ="";
					break;
				}
		}
		}
		return false;
	});
$('#spdEmployerContributionRef').change(function() {
		setDirtyFlag();
		var spdRef = document.getElementById('spdEmployerContributionRef').value;	
		if(spdRef !=null && spdRef != ''){
		    for (var i = 0; i < spdRef.length; i++) {
				var isAsciiPrintable=spdRef.charCodeAt(i) >= 32 && spdRef.charCodeAt(i) < 127;
				if(!isAsciiPrintable || spdRef.charAt(i)=='?'){
					alert(ERR_SPECIAL_CHARS);
					document.getElementById('spdEmployerContributionRef').value ="";
					break;
				}
		}
		}
		return false;
	});

$('#safeHarbourPlanName').change(function() {
		setDirtyFlag();
		var matchPlanName = document.getElementById('safeHarbourPlanName').value;	
		if(matchPlanName !=null && matchPlanName != ''){
			  for (var i = 0; i < matchPlanName.length; i++) {
				var isAsciiPrintable=matchPlanName.charCodeAt(i) >= 32 && matchPlanName.charCodeAt(i) < 127;
				if(!isAsciiPrintable || matchPlanName.charAt(i)=='?'){
					alert(ERR_SPECIAL_CHARS);
					document.getElementById('safeHarbourPlanName').value ="";
					break;
				}
		}
		}
		return false;
	});
	
$('#qACASHNonElectivePlanName').change(function() {
		setDirtyFlag();
		var nonElectivePlanName = document.getElementById('qACASHNonElectivePlanName').value;		
		if(nonElectivePlanName !=null && nonElectivePlanName != ''){			
		    for (var i = 0; i < nonElectivePlanName.length; i++) {
				var isAsciiPrintable=nonElectivePlanName.charCodeAt(i) >= 32 && nonElectivePlanName.charCodeAt(i) < 127;
				if(!isAsciiPrintable || nonElectivePlanName.charAt(i)=='?'){
					alert(ERR_SPECIAL_CHARS);
					document.getElementById('qACASHNonElectivePlanName').value ="";
					break;
				}
		}
		}
		return false;
	});

$('#qACASummaryPlanDesc').change(function() {
		setDirtyFlag();
		var qacaSummaryDes = document.getElementById('qACASummaryPlanDesc').value;	
		if(qacaSummaryDes !=null && qacaSummaryDes != ''){
			 for (var i = 0; i < qacaSummaryDes.length; i++) {
				var isAsciiPrintable=qacaSummaryDes.charCodeAt(i) >= 32 && qacaSummaryDes.charCodeAt(i) < 127;
				if(!isAsciiPrintable || qacaSummaryDes.charAt(i)=='?'){
					alert(ERR_SPECIAL_CHARS);
					document.getElementById('qACASummaryPlanDesc').value ="";
					break;
				}
		}
		}
		return false;
	});

if((document.getElementById('qACAMatchContributionMatchPct1').value == 4 || document.getElementById('qACAMatchContributionMatchPct1').value > 4) &&
	document.getElementById('qACAMatchContributionContribPct2').value == '' && document.getElementById('qACAMatchContributionMatchPct2').value == '' ) {
	document.getElementById('qACAMatchContributionMatchPct1Value').value = '';
}

assignAutoTabValues();
showOrHideACAFields();
allowACIFields();
placeSafeHarborVestingTableQACA();
showorhideACDOther();
showorhideQACDOther();
showSPDEmployerContributionRef();
showorhideQACASHMatchVestingNoFields();
showorhideQACASummaryPlanDesc();
showorhideqACAAutomaticContributionDays();
checkBoxBtatus();
enableContributionFeature2DateId();
enableContributionFeature1Pct();
enableContributionFeature3SummaryText();
showorhideqACAAutomaticContributionDays();
populateQACASafeHarborRow2Column2();
});

$('input[name="employerContributions"]').change(function() {
	if((document.getElementById('qacaEnablePopUpForEmployerContributionsId').value == 'true' || document.getElementById('eacaEnablePopUpForEmployerContributionsId').value == 'true') &&
			$('input[name="employerContributions"]:checked').val() == 'Y') {
		alert(ERR_EMP_CONTRIB);
		document.getElementById('employerContributionsYes').checked = false;
		document.getElementById('employerContributionsNo').checked = true;
		document.getElementById('employerContributionsNo').click();
	}
		goToUrl('automaticContribution','refresh');
});

$('input[name="qACAPlanHasAdditionalECon"]').change(function() {
	if((document.getElementById('qacaEnablePopUpForEmployerContributionsId').value == 'true' || document.getElementById('eacaEnablePopUpForEmployerContributionsId').value == 'true') &&
			$('input[name="qACAPlanHasAdditionalECon"]:checked').val() == 'Y') {
		alert(ERR_EMP_CONTRIB);
		document.getElementById('qACAPlanHasAdditionalEConYes').checked = false;
		document.getElementById('qACAPlanHasAdditionalEConNo').checked = true;
		document.getElementById('qACAPlanHasAdditionalEConNo').click();
	}
		goToUrl('automaticContribution','refresh');
});

//CR009 - Start

if($('input[name="autoContributionWD"]:checked').val() == 'Y')
{
	document.getElementById('automaticContributionDays').disabled=false;
	document.getElementById('automaticContributionDaysOther').disabled=false;
}
else {
	document.getElementById('automaticContributionDays').disabled=true;
	document.getElementById('automaticContributionDaysOther').disabled=true;
}


$('input[name="autoContributionWD"]').click(function() {
 	if($('input[name="autoContributionWD"]:checked').val() == 'Y'){
 		document.getElementById('automaticContributionDays').disabled=false;
 		document.getElementById('automaticContributionDaysOther').disabled=false;
	}
 	else if($('input[name="autoContributionWD"]:checked').val() == 'N'){
 		document.getElementById('automaticContributionDays').disabled=true;
		document.getElementById('automaticContributionDaysOther').disabled=true;
	}
});
//CR009 - End

function getValueFromRadioButton(name) {
	   //Get all elements with the name
	   var buttons = document.getElementsByName(name);
	   for(var i = 0; i < buttons.length; i++) {
	      //Check if button is checked
	      var button = buttons[i];
	      if(button.checked) {
	         //Return value
	         return button.value;
	      }
	   }
	   //No radio button is selected. 
	   return null;
	}
	
function allowACIFields()
{
var aciAllowed="${noticePlanCommonVO.aciAllowed}";
var annualApplyDate= "${noticePlanCommonVO.annualApplyDate}";
var frm = document.tabPlanDataForm; 
var acaAnnualIncreaseType;
if(frm.acaAnnualIncreaseType == null)
	return;
else 
	acaAnnualIncreaseType = getValueFromRadioButton('acaAnnualIncreaseType');
if(aciAllowed == 'Y')
{
document.getElementById('acaAnnualIncreaseType1').disabled = false;
document.getElementById('acaAnnualIncreaseType2').disabled = false;
document.getElementById('acaAnnualIncreaseType3').disabled = false;
if(annualApplyDate != null && annualApplyDate != '' && annualApplyDate !=' ')
{
if(acaAnnualIncreaseType != 2 && acaAnnualIncreaseType != 3)
{
document.getElementById('acaAnnualIncreaseType1').checked = true;
}
}
}
else if(aciAllowed == 'N' || aciAllowed == 'U')
{
//$("#aciApplyDateIdImage").removeAttr("onclick");
document.getElementById('acaAnnualIncreaseType1').disabled = true;
document.getElementById('acaAnnualIncreaseType2').disabled = true;
document.getElementById('acaAnnualIncreaseType3').disabled = true;
document.getElementById('acaAnnualIncreaseType1').checked = false;
document.getElementById('acaAnnualIncreaseType2').checked = false;
document.getElementById('acaAnnualIncreaseType3').checked = false;

}else
{
document.getElementById('acaAnnualIncreaseType1').disabled = true;
document.getElementById('acaAnnualIncreaseType2').disabled = true;
document.getElementById('acaAnnualIncreaseType3').disabled = true;
}
}
</script>

</body>

</html>