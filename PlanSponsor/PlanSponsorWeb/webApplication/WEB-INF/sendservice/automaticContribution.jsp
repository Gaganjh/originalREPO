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
    <%@ page import=" com.manulife.pension.service.contract.valueobject.PlanData" %>
  <%@ page import="java.math.BigDecimal"%>
<%@ page import="com.manulife.pension.ps.web.sendservice.NoticePlanDataForm" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %> 
<%@ page import="com.manulife.pension.ps.web.sendservice.util.PlanDataWebUtility" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>  
<%@ page import="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO" %>

<content:contentBean contentId="<%=ContentConstants.AUTO_CONTRIBUTION_TAB_INSTRUCTION%>" type="<%=ContentConstants.TYPE_DISCLAIMER%>" id="autoContributionInstruction" />
<content:contentBean contentId="<%=ContentConstants.AUTO_CONTRIBUTION_APPLIES_TO_MESSAGE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="autoContributionAppliesToMessage" />

<jsp:useBean id="noticePlanCommonVO" scope="session" class="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO"/>

<% String Y ="${planDataConstants.YES_CODE}"; 
pageContext.setAttribute("Y", Y,PageContext.PAGE_SCOPE); %> 


<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

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

.autoContributionLabelExtendedColumn {
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
#autoContributionPlanName {
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

<input type="hidden" name="contributionFeature1PctMissing" id="contributionFeature1PctMissing"/>
<input type="hidden" name="contributionFeature2DateIdMissing" id="contributionFeature2DateIdMissing"/>
<input type="hidden" name="contributionFeature3SummaryTextMissing" id="contributionFeature3SummaryTextMissing"/>

<div id="autoContributionTabDivId" class="borderedDataBox">
	<div class="subhead">
		 <c:if test="${not param.printFriendly}">
			  <div class="expandCollapseIcons" id="autoContributionShowIconId" onclick="expandDataDiv('autoContribution');">
			    <img class="icon" src="/assets/unmanaged/images/plus_icon.gif">
			  </div>
			  <div class="expandCollapseIcons" id="autoContributionHideIconId" onclick="collapseDataDiv('autoContribution');">
			    <img class="icon" src="/assets/unmanaged/images/minus_icon.gif">
			  </div>
		 </c:if>
		 <div class="sectionTitle">
		 Automatic Contributions
		 </div>
	 	 <div class="sectionHighlightIcon" id="autoContributionSectionHighlightIconId">
	   		<ps:sectionHilight name="automaticContribution" singleDisplay="true" displayToolTip="true" suppressDuplicateMessages="true"/>
	  	 </div>
		 <div class="endDataRowAndClearFloats"></div>
	</div>
	<div id="autoContributionDataDivId">	 
		<div id="autoContributionTabDivId" class="borderedDataBox">
	
	<TABLE border=0 cellSpacing=0 cellPadding=0 width=729>
	    <TR>
	    <TD width="100%"><!--[if lt IE 7]>
	   <link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid_IE.css" type="text/css">
	<![endif]-->
	<DIV class=evenDataRow>
		<TABLE class=dataTable>
			<TR vAlign=top>
				<TD class=autoContributionLabelExtendedColumn>Select automatic contribution
					provision type</TD>
<TD class=dataColumn><form:radiobutton disabled="${disableFields}" path="automaticContributionProvisionType" id="automaticContributionProvisionType1" value="ACA"/>Automatic Contribution Arrangement (ACA)<br />



<form:radiobutton disabled="${disableFields}" path="automaticContributionProvisionType" id="automaticContributionProvisionType2" value="EACA"/>Eligible Automatic Contribution Arrangement (EACA)<br />


<form:radiobutton disabled="${disableFields}" path="automaticContributionProvisionType" id="automaticContributionProvisionType3" value="QACA"/>Qualified Automatic Contribution Arrangement (QACA)



				</TD>
			</TR>
		</TABLE>
	</DIV>
	<div id="acaFieldsDiv" style="display: block;">
		<DIV class=oddDataRow>
			<TABLE class=dataTable>
				<TR vAlign=top>
					<TD class=autoContributionLabelExtendedColumn><ps:fieldHilight
							name="effectiveDate" singleDisplay="true"
							className="errorIcon" displayToolTip="true" /> Effective date
						of automatic contribution (enrollment) provision</TD>
					<TD class=dataColumn><c:choose>
							<c:when
								test="${noticePlanDataForm.effectiveDate !=null }">
<span> <form:input path="effectiveDate" disabled="${disableFields}" maxlength="10" size="10" id="acpEffectiveDateId"/>


								</span>
							</c:when>
							<c:otherwise>
								<p>Pending Plan Information Completion</p>
							</c:otherwise>
						</c:choose></TD>
				</TR>
			</TABLE>
		</DIV>
		<DIV class=evenDataRow>
			<TABLE class=dataTable>
				<TR vAlign=top>
					<TD class=autoContributionLabelExtendedColumn><ps:fieldHilight
							name="defaultDeferralPercentage" singleDisplay="true"
							className="errorIcon" displayToolTip="true" /> Initial
						default deferral percentage for automatic contributions
						(enrollment)</TD>
					<TD class=dataColumn><c:choose>
							<c:when
								test="${noticePlanCommonVO.defaultDeferralPercentage !=null }">
								
<input type="text"  name="defaultDeferralPercentage"  disabled="${disableFields}" maxlength="6" value="${noticePlanCommonVO.defaultDeferralPercentage}" size="10" style="direction: rtl;" cssClass="numericInput" />%

							</c:when>
							<c:otherwise>
								<p>Pending Plan Information Completion</p>
							</c:otherwise>
						</c:choose></TD>
				</TR>
			</TABLE>
		</DIV>
		<DIV class=oddDataRow>
			<TABLE class=dataTable>
				<TR vAlign=top>
					<TD class=autoContributionLabelExtendedColumn width="210">The notice
						states that automatic contribution feature applies <strong>"if
							you are eligible for the Plan and have not elected to make
							contributions, or to opt out of making contributions".</strong> In
						addition to this statement, select who else this automatic
						contribution feature applies to, if applicable. If a selection
						is made, the Notice will include your selection as indicated.
					</TD>
					<TD class=dataColumn>
<form:checkbox path="automaticContributionFeature1" id="automaticContributionFeature1" disabled="${disableFields}" value="0"/> If you are contributing less than


<form:input path="contributionFeature1Pct" disabled="${disableFields}" maxlength="5" size="10" cssClass="numericInput" id="contributionFeature1Pct" />%<br />






<form:checkbox path="automaticContributionFeature2" id="automaticContributionFeature2" disabled="${disableFields}" value="0"/> If you


						are hired after 
<form:input path="contributionFeature2Date" disabled="${disableFields}" maxlength="10" size="10" id="contributionFeature2DateId"/>



							<img id="contributionFeature2DateIdImage"						
						src="/assets/unmanaged/images/cal.gif" border="0">
						(mm/dd/yyyy) <br />
<form:checkbox path="automaticContributionFeature3" id="automaticContributionFeature3" disabled="${disableFields}" value="0"/>


						Complete if other<br /> 
<form:textarea path="contributionFeature3SummaryText" cols="40" disabled="${disableFields}" rows="5" id="contributionFeature3SummaryText" />




					</TD>
					<TD width="180"><content:getAttribute
							id="autoContributionAppliesToMessage" attribute="text" /> <br />
					</TD>
				</TR>
			</TABLE>
		</DIV>
		<DIV class=evenDataRow>
			<TABLE class=dataTable>
				<TR vAlign=top>
					<TD class=autoContributionLabelExtendedColumn><ps:fieldHilight
							name="aciAllowed" singleDisplay="true" className="errorIcon"
							displayToolTip="true" /> Does the plan provide for automatic
						contribution increases?
						<ps:fieldHilight name="aciAllowedUnspecified" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
						</TD>
					<TD class=dataColumn>
						<c:choose>
							<c:when
								test="${noticePlanCommonVO.aciAllowed!=null && noticePlanCommonVO.aciAllowed!='' && noticePlanCommonVO.aciAllowed!=' '}">
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
				</TR>
			</TABLE>
		</DIV>
		<c:if test="${noticePlanCommonVO.aciAllowed =='Y'}">
			<DIV class=oddDataRow>
				<TABLE class=dataTable>
					<TR vAlign=top>
						<TD class=autoContributionLabelExtendedColumn><ps:fieldHilight
								name="aciApplyDate" singleDisplay="true"
								className="errorIcon" displayToolTip="true" /> When are
							annual increases applied?</TD>
<TD class=dataColumn><form:radiobutton disabled="${disableFields}" path="acaAnnualIncreaseType" id="acaAnnualIncreaseType1" value="1"></form:radiobutton> 


<input type="text"  name="annualApplyDate" disabled="${disableFields}" value="${noticePlanCommonVO.annualApplyDate}" style="direction: rtl;" maxlength="6" size="10" cssClass="numericInput" /> (mm/dd)




 <br /> <form:radiobutton disabled="${disableFields}" path="acaAnnualIncreaseType" id="acaAnnualIncreaseType2" value="2" />the pay date following the anniversary date you entered into the Plan<br />



<form:radiobutton disabled="${disableFields}" path="acaAnnualIncreaseType" id="acaAnnualIncreaseType3" value="3"/> the pay date following the anniversary of your date of hire



						</TD>
					</TR>
				</TABLE>
			</DIV>
			<DIV class=evenDataRow>
				<TABLE class=dataTable>
					<TR vAlign=top>
						<TD class=autoContributionLabelExtendedColumn><ps:fieldHilight
								name="annualIncrease" singleDisplay="true"
								className="errorIcon" displayToolTip="true" /> Default
							annual increase</TD>
<TD class=dataColumn>
<input type="text"  name="defaultIncreasePercent" disabled="${disableFields}" value="${noticePlanCommonVO.defaultIncreasePercent}" style="direction: rtl;"  maxlength="6" size="10" cssClass="numericInput" />%</TD>
 


					</TR>
				</TABLE>
			</DIV>
			<DIV class=oddDataRow>
				<TABLE class=dataTable>
					<TR vAlign=top>
						<TD class=autoContributionLabelExtendedColumn><ps:fieldHilight
								name="maxAutomaticIncrease" singleDisplay="true"
								className="errorIcon" displayToolTip="true" /> Default
							maximum for automatic increase</TD>
<TD class=dataColumn><input type="text"  name="defaultAutoIncreaseMaxPercent" disabled="${disableFields}" value="${noticePlanCommonVO.defaultAutoIncreaseMaxPercent}" style="direction: rtl;" maxlength="6" size="10" cssClass="numericInput" />%




						</TD>
					</TR>
				</TABLE>
			</DIV>
		</c:if>
		<c:if test="${noticePlanCommonVO.aciAllowed !='Y'}">
        	<DIV class=oddDataRow>
				<TABLE class=dataTable>
	       			 <TR vAlign=top>
	        			<TD class=autoContributionLabelExtendedColumn>&nbsp;</TD>
	        		</TR>
	        	</TABLE>
        	</DIV>
        </c:if> 
	</div>
	<div id="eacaFieldsDiv" style="display: block;">
		<DIV class=oddDataRow>
				<TABLE class=dataTable>
			        <TR vAlign=top>
			        	<TD class=autoContributionLabelExtendedColumn>
							Does the plan allow for automatic contribution withdrawals?
						</TD>
						<TD class=dataColumn>
<form:radiobutton disabled="${disableFields}" onchange="setDirtyFlag();" path="eacaPlanHasAutoContribWD" id="autoContributionWDYes" value="Y"/>Yes
<form:radiobutton disabled="${disableFields}" onchange="setDirtyFlag();" path="eacaPlanHasAutoContribWD" id="autoContributionWDNo" value="N"/>No
						</TD>
			        </TR>
			    </TABLE>      
        	</DIV>
		<DIV class=evenDataRow>
			<TABLE class=dataTable>
				<TR vAlign=top>
					<TD class=autoContributionLabelExtendedColumn>Participants
						may elect to withdraw their automatic contributions no later
						than</TD>
<TD class=dataColumn> <form:select path="automaticContributionDays" styleId="automaticContributionDays" disabled="${disableFields}">



							<form:option value="90">90</form:option>
							<form:option value="60">60</form:option>
							<form:option value="30">30</form:option>
							<form:option value="00">Other</form:option>
</form:select>&nbsp;days after the first automatic contribution is taken
						from their compensation. <span
						id="automaticContributionDaysOtherId" style="display: block;">
<form:input path="automaticContributionDaysOther" disabled="${disableFields}" maxlength="2" size="10" id="automaticContributionDaysOther"/>




					</span></TD>
				</TR>
			</TABLE>
		</DIV>
		<DIV class=oddDataRow>
			<TABLE class=dataTable>
				<TR vAlign=top>
					<TD class=autoContributionLabelExtendedColumn>Does the plan
						have Employer contributions</TD>
<TD class=dataColumn><form:radiobutton disabled="${disableFields}" path="employerContributions" id="employerContributionsYes" value="Y"/>Yes <form:radiobutton disabled="${disableFields}" path="employerContributions" id="employerContributionsNo" value="N"/>No</TD>






				</TR>
			</TABLE>
		</DIV>
		<div id="spdEmployerContributionRefDivId" style="display: block;">
			<DIV class=evenDataRow>
				<TABLE class=dataTable>
					<TR vAlign=top>
						<TD class=autoContributionLabelExtendedColumn>If yes,
							provide the Summary Plan Description reference(s), such as
							section name, for this Employer Contribution. Information
							entered will appear on Notices as entered.</TD>
<TD class=dataColumn><form:textarea path="spdEmployerContributionRef" cols="40" disabled="${disableFields}" rows="5" cssStyle="margin: 2px;" id="spdEmployerContributionRef" /></TD>



					</TR>
				</TABLE>
			</DIV>
		</div>
<c:if test="${noticePlanDataForm.employerContributions =='Y'}">

			<div class="evenDataRow" id="vestingEACA" style="width: 729px;">
				<span id="vestingEACASpan" style="display: block;"> <jsp:include
						flush="true" page="EACAvesting.jsp" />
				</span>
			</div>
</c:if>
	</div>
	<div id="qacaFieldsDiv" style="display: none;">
	   <div class=evenDataRow>
	      <table class=dataTable>
	        <tr vAlign=top>
				<td class=autoContributionLabelExtendedColumn>				 
					Select the plan's type of Safe Harbor
				</td>
				<td class=dataColumn>
<form:select path="qACAPlanHasSafeHarborMatchOrNonElective" disabled="${disableFields}" styleId="qACAPlanHasSafeHarborMatchOrNonElective" >
					 	<form:option value="">Select one</form:option>
					 	<form:option value="SHMAC">Match</form:option>
						<form:option value="SHNEC">Non-elective</form:option>
</form:select>
				</td>
	        </tr>
	      </table>			      
	    </div>	    
<c:if test="${noticePlanDataForm.qACAPlanHasSafeHarborMatchOrNonElective =='SHMAC'}">
			<div class="oddDataRow" id="SafeHarborMatchId">
				<table class=dataTable>
					<tr vAlign=top>
						<td class=autoContributionLabelExtendedColumn>
							<table style="margin-top: 6px" id="shMatchVestingTable">
								<tbody>
									<tr vAlign=top>
										<td style="padding: 1px;">
											<div class="formulaHeader">
												&nbsp;Matching Contributions												
											</div>
											<div class="formulaBody" id="2_match_div">
												<table style="width: 100%">
													<tbody>
														<tr vAlign=top>
<td><form:input path="qACAMatchContributionContribPct1" disabled="${disableFields}" maxlength="5" size="5" cssStyle="width: 34px;" cssClass="numericInput" />% of




the first <form:input path="qACAMatchContributionMatchPct1" disabled="${disableFields}" maxlength="5" size="5" cssStyle="width: 34px;" cssClass="numericInput" id="qACAMatchContributionMatchPct1" />%</td>





														</tr>
														<tr vAlign=top>
<td>plus <form:input path="qACAMatchContributionContribPct2" disabled="${disableFields}" maxlength="5" size="5" cssStyle="width: 33px;" cssClass="numericInput" id="qACAMatchContributionContribPct2" />%





between <form:input path="qACAMatchContributionMatchPct1Value" disabled="${disableFields}" maxlength="3" size="3" cssStyle="width: 26px;" cssClass="numericInput" id="qACAMatchContributionMatchPct1Value" />%





and <form:input path="qACAMatchContributionMatchPct2" disabled="${disableFields}" maxlength="5" size="5" cssStyle="width: 31px; padding-left: 0px; padding-right: 0px;" cssClass="numericInput" id="qACAMatchContributionMatchPct2" />%





															</td>
														</tr>
													</tbody>
												</table>
											</div>
										</td>
									</tr>
									<tr vAlign=top>
										<td style="padding: 2px;"></td>
									</tr>
									

								</tbody>
							</table>
						</td>
						<td class=dataColumn>
							<table>
								<tr vAlign=top>
									<td>Safe Harbor
									Match contributions apply to each</td>
<td style="padding-bottom: 7px;"><form:select path="qACAArrangementOptions" disabled="${disableFields}" >


<form:options items="${noticePlanDataForm.matchAppliesToContribList}" itemLabel="label" itemValue="value"/>
</form:select></td>
								</tr>

								<tr vAlign=top>
									<td style="padding-bottom: 7px;">Are matching contributions being
									made to another plan?</td>
<td style="padding-bottom: 7px;"><form:radiobutton disabled="${disableFields}" path="qACAMatchContributionToAnotherPlan" id="anotherPlanMatchingContributionYes" value="${planDataConstants.YES_CODE}" />Yes




<form:radiobutton disabled="${disableFields}" path="qACAMatchContributionToAnotherPlan" id="anotherPlanMatchingContributionNo" value="${planDataConstants.NO_CODE}" />No




									</td>
								</tr>
								<tr vAlign=top>
									<td>If yes, enter the plan name</td>
<td><form:textarea path="qACAMatchContributionOtherPlanName" cols="40" disabled="${disableFields}" rows="5" id="autoContributionPlanName" /></td>




								</tr>
								<div id="SafeHarborRothContributionDivId"
									style="display: none">
									<tr vAlign=top>
										<td>Does
											Safe Harbor match apply to Roth contributions?</td>
<td><form:radiobutton disabled="${disableFields}" path="qACASafeHarborAppliesToRoth" id="SHMAppliedToRothContributionYes" value="${planDataConstants.YES_CODE}" />Yes




<form:radiobutton disabled="${disableFields}" path="qACASafeHarborAppliesToRoth" id="SHMAppliedToRothContributionNo" value="${planDataConstants.NO_CODE}" />No




										</td>
									</tr>
								</div>
								<div id="SafeHarborCatchUpContributionDivId"
									style="display: none">
									<tr vAlign=top>
										<td> Does Safe Harbor match apply to
										Catch-up contributions?</td>
<td><form:radiobutton disabled="${disableFields}" path="qACASHAppliesToCatchUpContributions" id="SHMAppliedToCatchUpContributionYes" value="${planDataConstants.YES_CODE}" />Yes




<form:radiobutton disabled="${disableFields}" path="qACASHAppliesToCatchUpContributions" id="SHMAppliedToCatchUpContributionNo" value="${planDataConstants.NO_CODE}" />No




										</td>
									</tr>
								</div>
							</table>
						</td>
					</tr>
				</table>
			</div>
</c:if>
<c:if test="${noticePlanDataForm.qACAPlanHasSafeHarborMatchOrNonElective =='SHNEC'}">
		   	<div class="oddDataRow" id="SafeHarborNonElectiveId">
		      <table class=dataTable>
		        <tr vAlign=top>
					<td class=autoContributionLabelExtendedColumn>
						<table style="margin-top: 6px" id="shNonElectiveVestingTable">
							<tbody>
								<tr>
									<td style="vertical-align:top;">
										<div class="formulaHeader">&nbsp;Non-elective Contributions</div>
										<div class="formulaBody" id="2non_match_div">
											<div style="padding-left:10px; padding-top:2px;padding-bottom:2px;">
<form:input path="qACANonElectiveContributionPct" disabled="${disableFields}" maxlength="5" size="5" cssClass="numericInput" />&nbsp;% of compensation&nbsp;&nbsp;
											</div>
										</div>
									</td>
								</tr>
								<tr vAlign=top>
									<td style="padding:2px;"></td>
								</tr>
							</tbody>
						</table>							
					</td>
					<td class=dataColumn>
						<table>
							<tr vAlign=top>
								<td style="padding-bottom: 7px;">
									Safe Harbor Non-elective contributions apply to each</td>
									<td style="padding-bottom: 7px;">
<form:select path="qACANonElectiveAppliesToContrib" disabled="${disableFields}" >
<form:options items="${noticePlanDataForm.matchAppliesToContribList}" itemLabel="label" itemValue="value"/>
</form:select>
							 	</td>
							</tr>
							<tr vAlign=top>
								<td style="padding-bottom: 7px;">
									Are Non-elective contributions being made to another plan?</td>
									<td style="padding-bottom: 7px;">
<form:radiobutton disabled="${disableFields}" path="qACANonElectiveContribOtherPlan" id="SHNEAppliedToAnotherPlanYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton disabled="${disableFields}" path="qACANonElectiveContribOtherPlan" id="SHNEAppliedToAnotherPlanNo" value="${planDataConstants.NO_CODE}" />No

							 	</td>
							</tr>
							<tr vAlign=top>
								<td>								 
								 If yes, enter the plan name</td>
								<td>
<form:textarea path="qACASHNonElectivePlanName" cols="40" disabled="${disableFields}" rows="5" id="qACASHNonElectivePlanName" />
							 	</td>
							</tr>
						</table>
					</td>
		        </tr>
		      </table>			      
		    </div>
</c:if>
	    
	    <div id="safeHarborVestingTable">
	    	<div class="formulaHeader">
						Complete the Safe Harbor Vesting Information
			</div>
			<div class="formulaBody" id="2_match_div">
					<table>
						<tbody>
							<tr vAlign=top>
<td><form:radiobutton disabled="${disableFields}" path="qACASHMatchVesting" id="qACASHMatchVestingYes" value="${planDataConstants.YES_CODE}" />100% vested when contributed <br />




<form:radiobutton disabled="${disableFields}" path="qACASHMatchVesting" id="qACASHMatchVestingNo" value="${planDataConstants.NO_CODE}" />Complete formula</td>




							</tr>
							<tr>
								<td width="20%" style="padding: 2px;"
									class="formulaHeader">
									<div id="yearsOfServiceId">
										<table border="1">
											<tr
												style="border: 1px; padding: 2px; font-weight: bold;">
												<td>Years of Service</td>
												<td>Percentage</td>
											</tr>

 <c:if  test="${qACASHMatchVesting =='Y'}">

												<tr class="evenDataRow">
													<td>Less than one year</td>
<td><form:input path="qACASHMatchVestingPct1" disabled="${disableFields}" maxlength="5" size="5" cssStyle="width: 32px;" cssClass="numericInput" id="qACASHMatchVestingPct1" />%</td>




												</tr>
												<tr class="evenDataRow">
													<td>One Year, but less than Two Years</td>
<td><form:input path="qACASHMatchVestingPct2" disabled="${disableFields}" maxlength="5" size="5" cssStyle="width: 32px;" cssClass="numericInput" id="qACASHMatchVestingPct2" />%</td>




												</tr>
</c:if>
<c:if  test="${qACASHMatchVesting != 'Y' }">
												<tr class="evenDataRow">
													<td>Less than one year</td>
<td><form:input path="qACASHMatchVestingPct1" disabled="${disableFields}" maxlength="5" size="5" cssStyle="width: 32px;" cssClass="numericInput" id="qACASHMatchVestingPct1"/>%</td>




												</tr>
												<tr class="evenDataRow">
													<td>One Year, but less than Two Years</td>
<td><form:input path="qACASHMatchVestingPct2" disabled="${disableFields}" maxlength="5" size="5" cssStyle="width: 32px;" cssClass="numericInput" id="qACASHMatchVestingPct2"/>%</td>




												</tr>
</c:if>
											<tr class="evenDataRow">
												<td>Two or more years</td>
												<td>100%
											</tr>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
		</div>
	    
	    <c:choose>
			<c:when test="${noticePlanDataForm.qACAPlanHasSafeHarborMatchOrNonElective == null or noticePlanDataForm.qACAPlanHasSafeHarborMatchOrNonElective==''}">
				<div class="oddDataRow" id="additionalEC">	
			</c:when>
			<c:otherwise>
				<div class="evenDataRow" id="additionalEC">
			</c:otherwise>
		</c:choose>	    
	      <table class=dataTable>
	        <tr vAlign=top>
				<td class=autoContributionLabelExtendedColumn>
				 	Does the plan allow automatic contribution withdrawals?  
				</td>
				<td class=dataColumn>
<form:radiobutton disabled="${disableFields}" path="qACAPlanHasAdditionalEC" id="qACAPlanHasAdditionalECYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton disabled="${disableFields}" path="qACAPlanHasAdditionalEC" id="qACAPlanHasAdditionalECNo" value="${planDataConstants.NO_CODE}" />No

				</td>
			</tr>
		  </table>			      
		</div>
		<c:choose>
			<c:when test="${noticePlanDataForm.qACAPlanHasSafeHarborMatchOrNonElective == null or noticePlanDataForm.qACAPlanHasSafeHarborMatchOrNonElective==''}">
				<div class="evenDataRow" id="qACAAutoDays">	
			</c:when>
			<c:otherwise>
				<div class="oddDataRow" id="qACAAutoDays">
			</c:otherwise>
		</c:choose>
			<TABLE class=dataTable>
				<TR vAlign=top>
					<TD class=autoContributionLabelExtendedColumn>Participants may elect to withdraw
						their automatic contributions no later than</TD>
<TD class=dataColumn> <form:select disabled="${disableFields}" path="qACAAutomaticContributionDays" styleId="qACAAutomaticContributionDays">



							<form:option value="90">90</form:option>
							<form:option value="60">60</form:option>
							<form:option value="30">30</form:option>
							<form:option value="00">Other</form:option>
</form:select>&nbsp;days after the first automatic contribution is taken
						from their compensation. <span
						id="qACAAutomaticContributionDaysOtherId"
style="display: block;"> <form:input path="qACAAutomaticContributionDaysOther" disabled="${disableFields}" maxlength="2" size="10" id="qACAAutomaticContributionDaysOther"/>





					</span></TD>
				</TR>
			</TABLE>
		</DIV>
		<c:choose>
			<c:when test="${noticePlanDataForm.qACAPlanHasSafeHarborMatchOrNonElective == null or noticePlanDataForm.qACAPlanHasSafeHarborMatchOrNonElective==''}">
				<div class="oddDataRow" id="additionalECon">	
			</c:when>
			<c:otherwise>
				<div class="evenDataRow" id="additionalECon">
			</c:otherwise>
		</c:choose>
	      <table class=dataTable>
	        <tr vAlign=top>
				<td class=autoContributionLabelExtendedColumn>
				 	Does the plan have Additional Employer Contributions?
				</td>
				<td class=dataColumn>
<form:radiobutton disabled="${disableFields}" path="qACAPlanHasAdditionalECon" id="qACAPlanHasAdditionalEConYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton disabled="${disableFields}" path="qACAPlanHasAdditionalECon" id="qACAPlanHasAdditionalEConNo" value="${planDataConstants.NO_CODE}" />No

				</td>
	        </tr>
	      </table>			      
	    </div>
	    <c:choose>
			<c:when test="${noticePlanDataForm.qACAPlanHasSafeHarborMatchOrNonElective == null or noticePlanDataForm.qACAPlanHasSafeHarborMatchOrNonElective==''}">
				<div class="evenDataRow" id="additionalECRef">	
			</c:when>
			<c:otherwise>
				<div class="oddDataRow" id="additionalECRef">
			</c:otherwise>
		</c:choose>
	      <table class=dataTable>
	        <tr vAlign=top>
				<td class=autoContributionLabelExtendedColumn>
				 If yes, provide the Summary Plan Description reference(s), such as section name, for this Employer Contribution. Information entered will appear on Notices as entered.						 	 
				</td>
				<td class=dataColumn>
<form:textarea path="qACASummaryPlanDesc" cols="40" disabled="${disableFields}" rows="5" cssStyle="margin: 2px;" id="qACASummaryPlanDesc" />
				</td>
	        </tr>
	      </table>      
	    </div>
<c:if test="${noticePlanDataForm.qACAPlanHasAdditionalECon =='Y'}">
			<div class="evenDataRow" id="vestingQACA" style="width: 729px;">			
				<span id="vestingQACASpan" style="display: block;" >
				    <jsp:include flush="true" page="QACAvesting.jsp"/>
				</span>
			</div>
</c:if>
	</div>
	     </TD>
	     </TR>
	     </TABLE>
	     </div>
	     <script>
	       
	        
	$(document).ready(function(){
		assignAutoTabValues();
		showOrHideACAFields();
		allowACIFields();
		placeSafeHarborVestingTableQACA();
		showorhideACDOther();
		showorhideQACDOther();
		showSPDEmployerContributionRef();
		showorhideqACAAutomaticContributionDays();
		checkBoxBtatus();
		enableContributionFeature3SummaryText();		
		enableContributionFeature2DateId();
		enableContributionFeature1Pct();
		//populateQACASafeHarborRow2Column2();
	});
	
	
	function allowACIFields()
	{ 
		var aciAllowed="${noticePlanCommonVO.aciAllowed}";
		var annualApplyDate= "${noticePlanCommonVO.annualApplyDate}";
		var frm = document.noticePlanDataForm; 
		var acaAnnualIncreaseType;
		if(frm.acaAnnualIncreaseType == null)
			return;
		else {
			acaAnnualIncreaseType = getValueFromRadioButton('acaAnnualIncreaseType');
		}	
		if(aciAllowed == 'Y')
		{
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
			document.getElementById('acaAnnualIncreaseType1').checked = false;
			document.getElementById('acaAnnualIncreaseType2').checked = false;
			document.getElementById('acaAnnualIncreaseType3').checked = false;
		}
	}
	
	</script>
	</div>
</div>
