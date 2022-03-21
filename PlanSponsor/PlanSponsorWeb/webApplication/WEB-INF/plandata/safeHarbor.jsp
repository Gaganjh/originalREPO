<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.manulife.pension.ps.web.plandata.TabPlanDataForm"%>
<%@ page import="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO" %>
<%@ page import="com.manulife.pension.ps.web.Constants"%> 
<%@ page import="com.manulife.pension.ps.web.plandata.util.TPAPlanDataWebUtility"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>  
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Safe Harbor</title>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/newrelic_code.js"></script>

<content:contentBean contentId="<%=ContentConstants.SAFE_HARBOUR_TAB_INSTRUCTION%>" type="<%=ContentConstants.TYPE_DISCLAIMER%>" id="safeHarbourTabIndtruction" />
<content:contentBean contentId="<%=ContentConstants.AUTO_CONTRIBUTION_APPLIES_TO_MESSAGE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="autoContributionAppliesToMessage" />
<jsp:useBean id="tabPlanDataForm" scope="session" type="com.manulife.pension.ps.web.plandata.TabPlanDataForm" />

<jsp:useBean id="noticePlanCommonVO" scope="session" class="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO"/>
<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
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
#safeHarbourPlanName {
    resize:none;
    overflow:auto;	
}
#SHNonElectivePlanName {
    resize:none;
    overflow:auto;	
}
#summaryPlanDesc {
    resize:none;
    overflow:auto;	
}
#contributionFeature3SummaryText {
    resize:none;
    overflow:auto;	
}

</style>
</head>
<body>

	<%
		   String enableCheckBoxFlag = tabPlanDataForm.getEnablePlanYearEndDateAndPercentageComp();
		%>	
	<table width="729" class="dataTable">
		<TR><TD class=subsubhead><content:getAttribute id="safeHarbourTabIndtruction" attribute="text"/></TD></TR>
	</table>
<input type="hidden" name="shEnablePopUpForEmployerContributions" id="shEnablePopUpForEmployerContributionsId"/>
<input type="hidden" name="shContributionFeature1PctMissing" id="contributionFeature1PctMissing"/>
<input type="hidden" name="shContributionFeature2DateIdMissing" id="contributionFeature2DateIdMissing"/>
<input type="hidden" name="shContributionFeature3SummaryTextMissing" id="contributionFeature3SummaryTextMissing"/>

<div id="investmentInformationTabDivId" class="borderedDataBox">
	<table border=0 cellSpacing=0 cellPadding=0 width=729>
	    <tr>
		    <td width="100%"><!--[if lt IE 7]>
		    <link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid_IE.css" type="text/css">
			<![endif]-->
				<div class=evenDataRow>
			      <table class=dataTable>
			        <tr>
						<td class=generalLabelExtendedColumn vAlign=top>
						<ps:fieldHilight name="planHasSafeHarborMatchOrNonElective" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
							Select the plan's type of Safe Harbor
						</td>
						<td class=dataColumn>
<form:select path="planHasSafeHarborMatchOrNonElective" id="planHasSafeHarborMatchOrNonElective">
							 	<form:option value="">Select one</form:option>
							 	<form:option value="M">Match</form:option>
								<form:option value="N">Non-elective</form:option>
</form:select>
						</td>
			        </tr>
			      </table>			      
			    </div>
			    <div class="oddDataRow" id="SafeHarborMatchId">
			      <table class=dataTable>
			        <tr>
						<td class=generalLabelExtendedColumn>
							<table style="margin-top: 6px">
								<tbody>
									<tr>
										<td style="padding:2px;">
										
											<div class="formulaHeader">&nbsp;Matching Contributions <ps:fieldHilight name="matchContributionPctFormula" singleDisplay="true" className="errorIcon" displayToolTip="true"/></div>
											<div class="formulaBody" id="2_match_div">
												<table>
													<tbody>
														<tr>
															<td>
																<div style="padding-left:5px">	
<form:input path="matchContributionContribPct1" maxlength="5" onblur="validatePctRow1(this, 'row1col1');" size="5" cssClass="numericInput" id="matchContributionContribPct1" /> % of the first
																</div>
															</td>
															<td style="padding-right:5px">
<form:input path="matchContributionMatchPct1" maxlength="5" onblur="validatePctRow1(this, 'row1col2');" size="5" cssClass="numericInput" id="matchContributionMatchPct1" /> %
															</td>
														</tr>
														<tr>
															<td>
																<div style="padding-left:5px" id="2_next_div">
<form:input path="matchContributionContribPct2" maxlength="5" onblur="validatePct(this, 'row2col1');" size="5" cssClass="numericInput" id="matchContributionContribPct2" /> % of the next
																</div>
															</td>
															<td style="padding-right:5px">
<form:input path="matchContributionMatchPct2" maxlength="5" onblur="validatePct(this, 'row2col2');" size="5" cssClass="numericInput" id="matchContributionMatchPct2" /> %
															</td>
														</tr>
													</tbody>
												</table>				
											</div>
										</td>			
									</tr>
								</tbody>
							</table>							
						</td>
						<td class=dataColumn>
							<table>
								<tr>
									<td vAlign=top style="padding-bottom: 7px;">Safe Harbor Match contributions apply to each</td>
									<td style="padding-bottom: 7px;">
<form:select path="matchAppliesToContrib" onchange="setDirtyFlag();" >
<form:options items="${tabPlanDataForm.matchAppliesToContribList}" itemLabel="label" itemValue="value" />
</form:select>
								 	</td>
								</tr>
								<tr>
									<td vAlign=top style="padding-bottom: 7px;">Are matching contributions being made to another plan?</td>
									<td style="padding-bottom: 7px;">
<form:radiobutton path="matchContributionToAnotherPlan" id="anotherPlanMatchingContributionYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton path="matchContributionToAnotherPlan" id="anotherPlanMatchingContributionNo" value="${planDataConstants.NO_CODE}" />No

								 	</td>
								</tr>
								<tr>
									<td vAlign=top>If yes, enter the plan name <ps:fieldHilight name="matchContributionToAnotherPlanName" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
									<td>
<form:textarea path="matchContributionOtherPlanName" cols="40" rows="5" id="safeHarbourPlanName" />
								 	</td>
								</tr>
								<tr>
									<td vAlign=top>Does Safe Harbor match apply to Roth contributions? <ps:fieldHilight name="safeHarborAppliesToRoth" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
									<td>
<form:radiobutton path="safeHarborAppliesToRoth" id="SHMAppliedToRothContributionYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton path="safeHarborAppliesToRoth" id="SHMAppliedToRothContributionNo" value="${planDataConstants.NO_CODE}" />No

								 	</td>
								</tr>
								<tr>
									<td vAlign=top>Does Safe Harbor match apply to Catch-up contributions? <ps:fieldHilight name="sHAppliesToCatchUpContributions" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
									<td>
<form:radiobutton path="sHAppliesToCatchUpContributions" id="SHMAppliedToCatchUpContributionYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton path="sHAppliesToCatchUpContributions" id="SHMAppliedToCatchUpContributionNo" value="${planDataConstants.NO_CODE}" />No

								 	</td>
								</tr>
							</table>
						</td>
			        </tr>
			      </table>			      
			    </div>
			    <div class="oddDataRow" id="SafeHarborNonElectiveId">
			      <table class=dataTable>
			        <tr>
						<td class=generalLabelExtendedColumn>
							<ps:fieldHilight name="nonElectiveContribOption" singleDisplay="true" className="errorIcon" displayToolTip="true" style="float:left"/>
							<div>
<form:radiobutton path="nonElectiveContribOption" id="Guaranteed" value="G" />Guarantee

<form:radiobutton path="nonElectiveContribOption" id="Flexible" value="F" />Flexible

							</div>
							<table style="margin-top: 6px">
								<tbody>
									<tr>
										<td style="vertical-align:top;">
											<div class="formulaHeader">&nbsp;Non-elective Contributions <ps:fieldHilight name="nonElectiveContributionPct" singleDisplay="true" className="errorIcon" displayToolTip="true"/></div>
											<div class="formulaBody" id="2non_match_div">
												<div style="padding-left:10px; padding-top:2px;padding-bottom:2px;">
<form:input path="nonElectiveContributionPct" maxlength="5" onblur="validatePct(this, 'ne');" size="5" cssClass="numericInput" />&nbsp;% of compensation&nbsp;&nbsp;
												</div>
											</div>
										</td>
									</tr>
								</tbody>
							</table>
						</td>
						<td class=dataColumn>
							<table>
								<tr>
									<td vAlign=top style="padding-bottom: 7px;">Safe Harbor Non-elective contributions apply to each</td>
									<td style="padding-bottom: 7px;">
								
									
<form:select path="nonElectiveAppliesToContrib" onchange="setDirtyFlag();" >
<form:options items="${tabPlanDataForm.matchAppliesToContribList}" itemLabel="label" itemValue="value"/>
</form:select>
								 	</td>
								</tr>
								<tr>
									<td vAlign=top style="padding-bottom: 7px;">Are Non-elective contributions being made to another plan?</td>
									<td style="padding-bottom: 7px;">
<form:radiobutton path="nonElectiveContribOtherPlan" id="SHNEAppliedToAnotherPlanYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton path="nonElectiveContribOtherPlan" id="SHNEAppliedToAnotherPlanNo" value="${planDataConstants.NO_CODE}" />No

								 	</td>
								</tr>
								<tr>
									<td vAlign=top>If yes, enter the plan name <ps:fieldHilight name="sHNonElectivePlanName" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
									<td>
<form:textarea path="SHNonElectivePlanName" cols="40" rows="5" id="SHNonElectivePlanName" />
								 	</td>
								</tr>
							</table>
						</td>
			        </tr>
			      </table>			      
			    <table class=dataTable style="width: 729px;height: 12px; background-color: white;">
			    	<tr>
			    		<td class=generalLabelExtendedColumn></td>
			    		<td class=dataColumn></td>
			    	</tr>
			    </table>
			    <table class=dataTable style="width: 729px;">
			    <tr>
			    		<td class=generalLabelExtendedColumn>Select this option and complete the fields if Non-elective contribution is applicable to a prior plan year. If selected, section will only appear in the annual notice.<ps:fieldHilight name="safeHarborEnablePlanYearEndDatePerComp" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
			    		<td class=dataColumn>
<input type="hidden" name="enablePlanYearEndDateAndPercentageComp" id="enableCheckBoxHiddenFieldID"/>
									<% if("Y".equals(enableCheckBoxFlag)){ %>	
										<table style="margin-top: 5px;">
											<tr>
												<td style="vertical-align: top;">
								   		<input type="checkbox" value="Y" checked id="enablePlanYearEndDateAndPercentageCompId" onclick="enablePlanYearEndDateAndPerComp()"/>
								   				</td>
								   				<td>
								   		Effective for the plan year ending
<form:input path="contributionApplicableToPlanDate" maxlength="10" onblur="validateEffectivePlanYearEndDate(this)" onchange="setDirtyFlag();" size="10" cssStyle="width: 74px;" id="neContributionApplicableToPlanDateId"/>






							   			<img id="enablePlanYearEndDateAndPercentageCompIdImage" onclick="return handleDateIconClicked(event, 'neContributionApplicableToPlanDateId');" src="/assets/unmanaged/images/cal.gif" border="0"> (mm/dd/yyyy)
					  					, the Company decided to provide fully vested qualified Non-elective contribution of 
<form:input path="contributionApplicableToPlanPct" maxlength="5" onblur="validateFullyVestedContributionPct(this, 'Y')" size="5" cssStyle="width: 40px;" cssClass="numericInput" id="contributionApplicableToPlanPctId" />
					  					% of compensation.
					  							</td>
					  						</tr>
					  					</table>
								   		<% } else { %>
								   		<table style="margin-top: 5px;">
											<tr>
												<td style="vertical-align: top;">
								   		<input type="checkbox" value="Y" id="enablePlanYearEndDateAndPercentageCompId" onclick="enablePlanYearEndDateAndPerComp()" style="vertical-align: top;"/>
								   				</td>
								   				<td>
								   		Effective for the plan year ending
<form:input path="contributionApplicableToPlanDate" disabled="true" maxlength="10" onblur="validateEffectivePlanYearEndDate(this)" onchange="setDirtyFlag();" size="10" cssStyle="width: 74px;" id="neContributionApplicableToPlanDateId"/>







							   			<img id="enablePlanYearEndDateAndPercentageCompIdImage" src="/assets/unmanaged/images/cal.gif" border="0"> (mm/dd/yyyy)
					  					, the Company decided to provide fully vested qualified Non-elective contribution of 
<form:input path="contributionApplicableToPlanPct" disabled="true" maxlength="5" onblur="validateFullyVestedContributionPct(this, 'Y')" size="5" cssStyle="width: 40px;" cssClass="numericInput" id="contributionApplicableToPlanPctId" />
					  					% of compensation.
					  							</td>
					  						</tr>
					  					</table>
								   		<% }  %>
			    		</td>
			    	</tr>
			    </table>
			    </div>
			    
			    <div class="oddDataRow" id="planHashACA">
			      <table class=dataTable>
			        <tr>
						<td class=generalLabelExtendedColumn vAlign=top>
							Is this a Safe Harbor plan with an Automatic Contribution option?
						</td>
						<td class=dataColumn>
<form:radiobutton path="planHasSHACA" id="planHasSHACAYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton path="planHasSHACA" id="planHasSHACANo" value="${planDataConstants.NO_CODE}" />No

						</td>
			        </tr>
			      </table>			      
			    </div>
			    
			     <div class="evenDataRow" id="planHaShACAFileds">
			     
			     			     
					<DIV class=evenDataRow id="effectveDateOfAC">
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
					        </TR>
					      </TABLE>
				        </DIV>
				         <DIV class=oddDataRow id="defferalPercentageOfAC">
					<TABLE class=dataTable>
				        <TR vAlign=top>
				        	<TD class=generalLabelExtendedColumn>
				        	 <ps:fieldHilight name="defaultDeferralPercentage" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
								Initial default deferral percentage for automatic contributions (enrollment)
								</TD>
							<TD class=dataColumn>
								<c:choose>
									<c:when test="${noticePlanCommonVO.defaultDeferralPercentage !=null }">
<input type="text"  name="defaultDeferralPercentage"  disabled="${disableFieldsForContributions}" maxlength="6" value="${noticePlanCommonVO.defaultDeferralPercentage}" style="direction: rtl;" size="10" cssClass="numericInput" />%
									
									
									</c:when>
									<c:otherwise>
										<p>Pending Plan Information Completion</p>
									</c:otherwise>
								</c:choose>
							</TD>
				        </TR></TABLE>
				        </DIV>
				          <DIV class=evenDataRow id="acFeaturesApplies">      
				      <TABLE class=dataTable>
				        <TR vAlign=top>
				        	<TD class=generalLabelExtendedColumn width="210">
				        	 <ps:fieldHilight name="planAllowsInServiceWithdrawals" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
			The notice states that automatic contribution feature applies <b>"if you are eligible for the Plan and have not elected to make contributions, or to opt out of making contributions".</b> In addition to this statement, select who else this automatic contribution feature applies to, if applicable.  If a selection is made, the Notice will include your selection as indicated.
							</TD>
							<TD class=dataColumn>
							 	<ps:fieldHilight name="shContributionFeature1Pct" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
<form:checkbox path="sHautomaticContributionFeature1" id="automaticContributionFeature1" onclick="enableContributionFeature1Pct();" onchange="setDirtyFlag();" value="0"/>

												   If you are contributing less than
<form:input path="shContributionFeature1Pct" disabled="${disableFieldsForContributions}" maxlength="5" onblur="validateContributionLessDecimal(this, 'Y')" onchange="setDirtyFlag()" size="10" cssClass="numericInput" id="contributionFeature1Pct" />%<br/>

				         		 
				         		 <ps:fieldHilight name="shContributionFeature2Date" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
<form:checkbox path="sHautomaticContributionFeature2" id="automaticContributionFeature2" onclick="enableContributionFeature2DateId();" onchange="setDirtyFlag();" value="0"/>

												   If you are hired after
<form:input path="shContributionFeature2Date" disabled="${disableFieldsForContributions}" maxlength="10" onblur="validateHireAfterDate(this)" onchange="setDirtyFlag();" size="10" id="contributionFeature2DateId"/>





											   
									  <img id="contributionFeature2DateIdImage" onclick="return handleDateIconClicked(event, 'contributionFeature2DateId');" src="/assets/unmanaged/images/cal.gif" border="0"> (mm/dd/yyyy)
									
									<br/>
							 	  
							 	  <ps:fieldHilight name="shContributionFeature3SummaryText" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
<form:checkbox path="sHautomaticContributionFeature3" id="automaticContributionFeature3" onclick="enableContributionFeature3SummaryText();" onchange="setDirtyFlag();" value="0"/>

Complete if other<br/>   <form:textarea path="shContributionFeature3SummaryText" cols="40" disabled="${disableFieldsForContributions}" onchange="setDirtyFlag();" rows="5" id="contributionFeature3SummaryText" />

				         
							</TD>
							<TD width="180">
							<content:getAttribute id="autoContributionAppliesToMessage" attribute="text"/>
							<br/>			
							</TD>
				        </TR></TABLE></DIV>
				       <!-- PIF data values -->
				          <DIV class=oddDataRow id="acIncreases">
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
				        
				         <c:if test="${noticePlanCommonVO.aciAllowed =='Y'}" >
					          <DIV class=evenDataRow id="acIncreasesApplied">
						<TABLE class=dataTable>
					        <TR vAlign=top>
					        	<TD class=generalLabelExtendedColumn>
					        	 <ps:fieldHilight name="aciApplyDate" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
									When are annual increases applied?									
								</TD>
								<TD class=dataColumn>
<form:radiobutton disabled="${disableFieldsForContributions}" onchange="setDirtyFlag();" path="shACAAnnualIncreaseType" id="acaAnnualIncreaseType1" value="1"></form:radiobutton>
<input type="text"  name="annualApplyDate" disabled="${disableFieldsForContributions}" value="${noticePlanCommonVO.annualApplyDate}" style="direction: rtl;" maxlength="6" size="10" cssClass="numericInput" /> (mm/dd)
	
										   <br/>
<form:radiobutton disabled="${disableFieldsForContributions}" onchange="setDirtyFlag();" path="shACAAnnualIncreaseType" id="acaAnnualIncreaseType2" value="2"/>The pay date following the anniversary date you entered into the Plan<br/>
<form:radiobutton disabled="${disableFieldsForContributions}" onchange="setDirtyFlag();" path="shACAAnnualIncreaseType" id="acaAnnualIncreaseType3" value="3"/>The pay date following the anniversary of your date of hire
					        	
								</TD>
					        </TR></TABLE>
					        </DIV>
					          <DIV class=oddDataRow id="annualIncreases">
						<TABLE class=dataTable>
					        <TR vAlign=top>
					        	<TD class=generalLabelExtendedColumn>
					        	 <ps:fieldHilight name="annualIncrease" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
									Default annual increase		
									
								</TD>
								<TD class=dataColumn>
<input type="text"  name="defaultIncreasePercent" disabled="${disableFieldsForContributions}" value="${noticePlanCommonVO.defaultIncreasePercent}" style="direction: rtl;" maxlength="6" size="10" cssClass="numericInput"  />%
									</TD>
					        </TR></TABLE>
					        </DIV>
					         <DIV class=evenDataRow id="maxAutomaticIncreases">
						<TABLE class=dataTable>
					        <TR vAlign=top>
					        	<TD class=generalLabelExtendedColumn>
					        	 <ps:fieldHilight name="maxAutomaticIncrease" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
								Default maximum for automatic increase
								</TD>
								<td class=dataColumn>
<input type="text"  name="defaultAutoIncreaseMaxPercent" disabled="${disableFieldsForContributions}" value="${noticePlanCommonVO.defaultAutoIncreaseMaxPercent}" style="direction: rtl;"  maxlength="6" size="10" cssClass="numericInput" />%
									
									</td>
					        </TR></TABLE>
					        </DIV>
					        </c:if>
					         				        
					          <DIV class=oddDataRow id="acwithdrawls">
									<TABLE class=dataTable>
								        <TR vAlign=top>
								        	<TD class=generalLabelExtendedColumn>
								        	<ps:fieldHilight name="SHAutoContributionWD" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
												Does the plan allow automatic contribution withdrawals?
											</TD>
											<TD class=dataColumn>
<form:radiobutton onchange="setDirtyFlag();" path="SHAutoContributionWD" id="autoContributionWDYes" value="Y" />Yes
<form:radiobutton onchange="setDirtyFlag();" path="SHAutoContributionWD" id="autoContributionWDNo" value="N" />No
											</TD>
								        </TR>
								    </TABLE>      
					        	</DIV>
					        <DIV class=evenDataRow id="acwithdrawlsMonths">
						<TABLE class=dataTable>
					        <TR vAlign=top>
					        	<TD class=generalLabelExtendedColumn>
					        	<ps:fieldHilight name="SHAutomaticContributionDays" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
					        	 <ps:fieldHilight name="SHAutomaticContributionDaysOther" singleDisplay="true" className="errorIcon" displayToolTip="true" style="position: relative; top: 15px;"/>
									Participants may elect to withdraw their automatic contributions no later than
								</TD>
								<TD class=dataColumn>
								<form:select  path="SHAutomaticContributionDays" id="automaticContributionDays" onchange="showorhideACDOther();setDirtyFlag();">
									  <form:option value="90">90</form:option>
									  <form:option value="60">60</form:option>
									  <form:option value="30">30</form:option>
									  <form:option value="00">Other</form:option>
</form:select>&nbsp;days after the first automatic contribution is taken from their compensation.
								<span id="automaticContributionDaysOtherId" style="display: block;">
<form:input path="SHAutomaticContributionDaysOther" maxlength="2" onblur="validateOtherDays(this);" onchange="setDirtyFlag();" size="10" id="automaticContributionDaysOther"/>
	 
										</span> 
								</TD>
					        </TR></TABLE>
					        </DIV>
				 </div>
			     
			    <div class="evenDataRow" id="additionalEC">
			      <table class=dataTable>
			        <tr>
						<td class=generalLabelExtendedColumn vAlign=top>
						<ps:fieldHilight name="PlanHasAdditionalEmpContribution" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
						<ps:fieldHilight name="safeHarborEmployerContributionYes" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
							Does the plan have Additional Employer Contributions?
						</td>
						<td class=dataColumn>
						<span  onmouseover="javascript: if(document.getElementById('planHasAdditionalECYes').disabled) {Tip(ERR_SH_ADDN_EMP_CONTR);}" onmouseout="javascript: if(document.getElementById('planHasAdditionalECYes').disabled) { UnTip();}">
<form:radiobutton disabled="${tabPlanDataForm.shDisableYesForAdditionalEmployerContributions}" path="planHasAdditionalEC" id="planHasAdditionalECYes" value="${planDataConstants.YES_CODE}" />Yes

						</span>
<form:radiobutton path="planHasAdditionalEC" id="planHasAdditionalECNo" value="${planDataConstants.NO_CODE}" />No

						</td>
			        </tr>
			      </table>			      
			    </div>
        		<div class="oddDataRow" id="additionalECRef">
			      <table class=dataTable>
			        <tr>
						<td class=generalLabelExtendedColumn vAlign=top>
							If yes, provide the Summary Plan Description reference(s), such as section name, for this Employer Contribution. Information entered will appear on Notices as entered.
							<ps:fieldHilight name="summaryPlanDesc" singleDisplay="true" className="errorIcon" displayToolTip="true"/> 
						</td>
						<td class=dataColumn>
	<form:textarea path="summaryPlanDesc" cols="40" rows="5" style="margin: 2px;" disabled="${disableFields}" id="summaryPlanDesc" />

						</td>
			        </tr>
			      </table>			      
			    </div>
			    
			    <div class="evenDataRow" id="vesting" style="width: 729px;">
			    	<jsp:include page="/WEB-INF/plandata/vesting.jsp" flush="true"/>
			    </div>
			    
        	</td>
        </tr>
    </table>
</div>

<script>
$(document).ready(function(){
	var frm = document.tabPlanDataForm;
	document.getElementById("safeHarbourPlanName").setAttribute('maxlength', 100);
	document.getElementById("SHNonElectivePlanName").setAttribute('maxlength', 100);
	document.getElementById("summaryPlanDesc").setAttribute('maxlength', 100);
	document.getElementById("contributionFeature3SummaryText").setAttribute('maxlength', 200);
	
	//displaying the SHMatch section or SHNonElective section based on the selected value. If none is selected, both the sections will be hidden
	var planHasSafeHarborMatchOrNonElective = frm.planHasSafeHarborMatchOrNonElective.value;
	if(planHasSafeHarborMatchOrNonElective == "M"){
		document.getElementById("SafeHarborMatchId").style.display = "block";
		document.getElementById("SafeHarborNonElectiveId").style.display = "none";
		modifyBgColorIfMatch();
	} 
	else if(planHasSafeHarborMatchOrNonElective == "N"){
		document.getElementById("SafeHarborNonElectiveId").style.display = "block";
		document.getElementById("SafeHarborMatchId").style.display = "none";
		modifyBgColorIfNonElective();
	}
	else{
		document.getElementById("SafeHarborMatchId").style.display = "none";
		document.getElementById("SafeHarborNonElectiveId").style.display = "none";
		modifyBgColorIfNone();
	}
	
 	var previous;
    var oldValue = planHasSafeHarborMatchOrNonElective;   
	$('#planHasSafeHarborMatchOrNonElective').change(function() {
	//var prev = $(this).data('pre');
    var current = $(this).val();
    if(oldValue!=null && oldValue!='' && oldValue!=current)
    {
	var message = ERR_SH_CHANGES_LOST;
	if(confirmSwitchOptions(message)){
	}else
	{
	$("#planHasSafeHarborMatchOrNonElective").val(oldValue);
	}	
	}
		setDirtyFlag();
	 	if($("#planHasSafeHarborMatchOrNonElective").val() == 'M'){
			document.getElementById('SafeHarborMatchId').style.display="block";
			document.getElementById('SafeHarborNonElectiveId').style.display="none";
			modifyBgColorIfMatch();
		}
	 	else if($("#planHasSafeHarborMatchOrNonElective").val() == 'N'){
	 		document.getElementById('SafeHarborNonElectiveId').style.display="block";
	 		document.getElementById('SafeHarborMatchId').style.display="none";
	 		modifyBgColorIfNonElective();
		}
	 	else {
	 		document.getElementById('SafeHarborMatchId').style.display="none";
			document.getElementById('SafeHarborNonElectiveId').style.display="none";
			modifyBgColorIfNone();
	 	}
	});
	
	//SH Match other contribution plan
	if($('input[name="matchContributionToAnotherPlan"]:checked').val() == 'Y'){
		document.getElementById('safeHarbourPlanName').disabled=false;
	}
 	else {
		document.getElementById('safeHarbourPlanName').disabled=true;
	}
	
	
	$('input[name="matchContributionToAnotherPlan"]').click(function() {
		setDirtyFlag();
	 	if($('input[name="matchContributionToAnotherPlan"]:checked').val() == 'Y'){
			document.getElementById('safeHarbourPlanName').disabled=false;
		}
	 	else if($('input[name="matchContributionToAnotherPlan"]:checked').val() == 'N'){
	 		document.getElementById('safeHarbourPlanName').value='';
			document.getElementById('safeHarbourPlanName').disabled=true;
		}
	});
	
	$('#safeHarbourPlanName').change(function() {
		setDirtyFlag();
		var matchPlanName = document.getElementById('safeHarbourPlanName').value;
		if(matchPlanName !=null && matchPlanName != ''){
			for(var i = 0; i < matchPlanName.length;i++){
				var isAsciiPrintable=matchPlanName.charCodeAt(i) >= 32 && matchPlanName.charCodeAt(i) < 127;
				if(!isAsciiPrintable || matchPlanName.charAt(i)=='?'){
		        	alert (ERR_SPECIAL_CHARS);
					document.getElementById('safeHarbourPlanName').value ="";
					break;
		        }
		    }
		}
		return false;
	});
	
	
	//If the answer is Yes, enable the plan name.
	if($('input[name="nonElectiveContribOtherPlan"]:checked').val() == 'Y'){
		document.getElementById('SHNonElectivePlanName').disabled=false;
	}
 	else {
		document.getElementById('SHNonElectivePlanName').disabled=true;
	}
	
	$('input[name="nonElectiveContribOtherPlan"]').click(function() {
		setDirtyFlag();
	 	if($('input[name="nonElectiveContribOtherPlan"]:checked').val() == 'Y'){
			document.getElementById('SHNonElectivePlanName').disabled=false;
		}
	 	else if($('input[name="nonElectiveContribOtherPlan"]:checked').val() == 'N'){
	 		document.getElementById('SHNonElectivePlanName').value='';
			document.getElementById('SHNonElectivePlanName').disabled=true;
		}
	});
	
	
	$('#SHNonElectivePlanName').change(function() {
		setDirtyFlag();
		var nePlanName = document.getElementById('SHNonElectivePlanName').value;
		if(nePlanName !=null && nePlanName != ''){
			for(var i = 0; i < nePlanName.length;i++){
				var isAsciiPrintable=nePlanName.charCodeAt(i) >= 32 && nePlanName.charCodeAt(i) < 127;
				if(!isAsciiPrintable || nePlanName.charAt(i)=='?'){
		        	alert (ERR_SPECIAL_CHARS);
					document.getElementById('SHNonElectivePlanName').value ="";
					break;
		        }
		    }
		}
		return false;
	});
	
	
	//CR011 Start (If plan has Automatic contribution arrangements)
	
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
	
	if(document.getElementById("planHasSHACAYes").checked == true){
		document.getElementById("planHaShACAFileds").style.display ="block";
	}
 	else {
 		document.getElementById('planHaShACAFileds').value='';
		document.getElementById("planHaShACAFileds").style.display ="none";
	}
		
	$('input[name="planHasSHACA"]').click(function() {
		setDirtyFlag();
		var aciAllowed="${noticePlanCommonVO.aciAllowed}";
			if($('input[name="planHasSHACA"]:checked').val() == 'Y'){
				  	var effectiveDate="${tabPlanDataForm.effectiveDate}";
					var defaultDeferralPercentage=" ${noticePlanCommonVO.defaultDeferralPercentage}";
					if(effectiveDate != null && effectiveDate != '' && effectiveDate !=' ' && defaultDeferralPercentage!=null  && defaultDeferralPercentage != '' && defaultDeferralPercentage !=' '){
						document.getElementById("planHaShACAFileds").style.display ="block";
						if(aciAllowed=='Y'){
							document.getElementById('acaAnnualIncreaseType1').value = '1';
							document.getElementById('acaAnnualIncreaseType2').value = '0';
					 		document.getElementById('acaAnnualIncreaseType3').value = '0';
						}
						goToUrl('safeHarbor','refresh');
					}else{
						alert("You cannot select 'Yes' for \"Safe Harbor Automatic Contribution Arrangement?\" as either \"Automatic Contribution Provision Effective Value\" or \"Automatic Contribution Initial Default Percentage Value\" or both, have missing Plan Information values. Please complete plan details for these two fields before proceeding");
						document.getElementById('planHasSHACANo').checked = true;
						return false;
					}
			}
			else if($('input[name="planHasSHACA"]:checked').val() == 'N'){
				if(confirmSwitchAutoOptions("This selection will clear all the values you entered for Safe Harbor - Automatic Contribution Arrangement. Click OK to continue or CANCEL to return")){
					document.getElementById('contributionFeature1Pct').value='';
			 		document.getElementById('contributionFeature2DateId').value='';
			 		document.getElementById('contributionFeature3SummaryText').value='';
			 		document.getElementById('automaticContributionFeature1').value='0';
			 		document.getElementById('automaticContributionFeature2').value='0';
			 		document.getElementById('automaticContributionFeature3').value='0';
			 		document.getElementById('autoContributionWDYes').value='';
			 		document.getElementById('autoContributionWDNo').value='';
			 		document.getElementById('automaticContributionDaysOther').value='';
			 		document.getElementById('automaticContributionDays').value='';
			 		document.getElementById('contributionFeature1PctMissing').value=false;
			 		document.getElementById('contributionFeature2DateIdMissing').value=false;
			 		document.getElementById('contributionFeature3SummaryTextMissing').value=false; 
			 		if(aciAllowed=='Y'){
				 		document.getElementById('acaAnnualIncreaseType1').value = '1';
				 		document.getElementById('acaAnnualIncreaseType2').value = '0';
				 		document.getElementById('acaAnnualIncreaseType3').value = '0';
			 		}
					document.getElementById("planHaShACAFileds").style.display ="none";
					document.getElementById('planHasSHACAYes').checked = false;
					document.getElementById('planHasSHACANo').checked = true;
					goToUrl('safeHarbor','refresh');
				}
				else
				{	
					document.getElementById('planHasSHACAYes').checked = true;
					document.getElementById('planHasSHACANo').checked = false;
					return false;
				}
			}
	});
	
	if($('input[name="SHAutoContributionWD"]:checked').val() == 'Y')
	{
		document.getElementById('automaticContributionDays').disabled=false;
		document.getElementById('automaticContributionDaysOther').disabled=false;
	}
	else {
		document.getElementById('automaticContributionDays').disabled=true;
		document.getElementById('automaticContributionDaysOther').disabled=true;
	}


	$('input[name="SHAutoContributionWD"]').click(function() {
	 	if($('input[name="SHAutoContributionWD"]:checked').val() == 'Y'){
	 		document.getElementById('automaticContributionDays').disabled=false;
	 		document.getElementById('automaticContributionDaysOther').disabled=false;
		}
	 	else if($('input[name="SHAutoContributionWD"]:checked').val() == 'N'){
	 		document.getElementById('automaticContributionDays').disabled=true;
			document.getElementById('automaticContributionDaysOther').disabled=true;
		}
	});
	
	allowACIFields();
	showorhideACDOther();
	checkBoxBtatus();

	
	//CR011 End
	
	//If plan has Additional Employee contribution, enter plan name else disable
	if(document.getElementById("planHasAdditionalECYes").checked == true){
		document.getElementById('summaryPlanDesc').disabled=false;
		document.getElementById("vesting").style.display ="block";
	}
 	else {
 		document.getElementById('summaryPlanDesc').value='';
		document.getElementById('summaryPlanDesc').disabled=true;
		document.getElementById("vesting").style.display ="none";
	}
	
	$('input[name="planHasAdditionalEC"]').click(function() {
		setDirtyFlag();
	 	if(document.getElementById("planHasAdditionalECYes").checked == true){
			document.getElementById('summaryPlanDesc').disabled=false;
			document.getElementById("vesting").style.display ="block";
		}
	 	else if(document.getElementById("planHasAdditionalECNo").checked == true){
	 		document.getElementById('summaryPlanDesc').value='';
			document.getElementById('summaryPlanDesc').disabled=true;
			document.getElementById("vesting").style.display ="none";
		}
	});
	
	$('#summaryPlanDesc').change(function() {
		setDirtyFlag();
		var spd = document.getElementById('summaryPlanDesc').value;
		if(spd !=null && spd != ''){
			for(var i = 0; i < spd.length;i++){
				var isAsciiPrintable=spd.charCodeAt(i) >= 32 && spd.charCodeAt(i) < 127;
				if(!isAsciiPrintable || spd.charAt(i)=='?'){
					alert(ERR_SPECIAL_CHARS);
					document.getElementById('summaryPlanDesc').value ="";
					break;
				}
		    }
		}
		return false;
	});
	
	$('input[name="safeHarborAppliesToRoth"]').click(function() {
		setDirtyFlag();
	});
	
	$('input[name="sHAppliesToCatchUpContributions"]').click(function() {
		setDirtyFlag();
	});
	
	$('input[name="nonElectiveContribOption"]').click(function() {
		setDirtyFlag();
	});
	
	$('#neContributionApplicableToPlanDateId, #contributionApplicableToPlanPctId').change(function() {
		setDirtyFlag();
	});
	
});


//To display tables rows in alternate colors if SH match is selected
function modifyBgColorIfMatch(){
	
	
	$("#SafeHarborMatchId").removeClass("oddDataRow");
	$("#SafeHarborMatchId").removeClass("evenDataRow");
	$("#SafeHarborMatchId").addClass("oddDataRow");
	
	$("#planHashACA").removeClass("evenDataRow");
	$("#planHashACA").removeClass("oddDataRow");
	$("#planHashACA").addClass("evenDataRow");

if($('input[name="planHasSHACA"]:checked').val() == 'Y'){
	var aciAllowed="${noticePlanCommonVO.aciAllowed}";
		$("#effectveDateOfAC").removeClass("evenDataRow");
		$("#effectveDateOfAC").removeClass("oddDataRow");
		$("#effectveDateOfAC").addClass("oddDataRow");
		
		$("#defferalPercentageOfAC").removeClass("evenDataRow");
		$("#defferalPercentageOfAC").removeClass("oddDataRow");
		$("#defferalPercentageOfAC").addClass("evenDataRow");
		
		$("#acFeaturesApplies").removeClass("evenDataRow");
		$("#acFeaturesApplies").removeClass("oddDataRow");
		$("#acFeaturesApplies").addClass("oddDataRow");
		
		$("#acIncreases").removeClass("evenDataRow");
		$("#acIncreases").removeClass("oddDataRow");
		$("#acIncreases").addClass("evenDataRow");
		
		if(aciAllowed=='Y'){
			
			$("#acIncreasesApplied").removeClass("evenDataRow");
			$("#acIncreasesApplied").removeClass("oddDataRow");
			$("#acIncreasesApplied").addClass("oddDataRow");
						
			$("#annualIncreases").removeClass("evenDataRow");
			$("#annualIncreases").removeClass("oddDataRow");
			$("#annualIncreases").addClass("evenDataRow");
						
			$("#maxAutomaticIncreases").removeClass("evenDataRow");
			$("#maxAutomaticIncreases").removeClass("oddDataRow");
			$("#maxAutomaticIncreases").addClass("oddDataRow");
				
			$("#acwithdrawls").removeClass("evenDataRow");
			$("#acwithdrawls").removeClass("oddDataRow");
			$("#acwithdrawls").addClass("evenDataRow");
			
			$("#acwithdrawlsMonths").removeClass("evenDataRow");
			$("#acwithdrawlsMonths").removeClass("oddDataRow");
			$("#acwithdrawlsMonths").addClass("oddDataRow");
			
			$("#additionalEC").removeClass("evenDataRow");
			$("#additionalEC").removeClass("oddDataRow");
			$("#additionalEC").addClass("evenDataRow");
			
			$("#additionalECRef").removeClass("oddDataRow");
			$("#additionalECRef").removeClass("evenDataRow");
			$("#additionalECRef").addClass("oddDataRow");
			
			$("#vesting").removeClass("evenDataRow");
			$("#vesting").removeClass("oddDataRow");
			$("#vesting").addClass("evenDataRow");
			
		}else{
			
			$("#acwithdrawls").removeClass("evenDataRow");
			$("#acwithdrawls").removeClass("oddDataRow");
			$("#acwithdrawls").addClass("oddDataRow");
			
			$("#acwithdrawlsMonths").removeClass("evenDataRow");
			$("#acwithdrawlsMonths").removeClass("oddDataRow");
			$("#acwithdrawlsMonths").addClass("evenDataRow");
			
			$("#additionalEC").removeClass("evenDataRow");
			$("#additionalEC").removeClass("oddDataRow");
			$("#additionalEC").addClass("oddDataRow");
			
			$("#additionalECRef").removeClass("oddDataRow");
			$("#additionalECRef").removeClass("evenDataRow");
			$("#additionalECRef").addClass("evenDataRow");
			
			$("#vesting").removeClass("evenDataRow");
			$("#vesting").removeClass("oddDataRow");
			$("#vesting").addClass("oddDataRow");
		}
		
	
	}else{
		
		$("#additionalEC").removeClass("evenDataRow");
		$("#additionalEC").removeClass("oddDataRow");
		$("#additionalEC").addClass("oddDataRow");
		
		$("#additionalECRef").removeClass("oddDataRow");
		$("#additionalECRef").removeClass("evenDataRow");
		$("#additionalECRef").addClass("evenDataRow");
		
		$("#vesting").removeClass("evenDataRow");
		$("#vesting").removeClass("oddDataRow");
		$("#vesting").addClass("oddDataRow");
	}
}

//To display tables rows in alternate colors if SH NE is selected
function modifyBgColorIfNonElective(){
	$("#SafeHarborNonElectiveId").removeClass("oddDataRow");
	$("#SafeHarborNonElectiveId").removeClass("evenDataRow");
	$("#SafeHarborNonElectiveId").addClass("oddDataRow");
	
	$("#planHashACA").removeClass("evenDataRow");
	$("#planHashACA").removeClass("oddDataRow");
	$("#planHashACA").addClass("evenDataRow");
	
	if($('input[name="planHasSHACA"]:checked').val() == 'Y'){
		
		var aciAllowed="${noticePlanCommonVO.aciAllowed}";
		
		$("#effectveDateOfAC").removeClass("evenDataRow");
		$("#effectveDateOfAC").removeClass("oddDataRow");
		$("#effectveDateOfAC").addClass("oddDataRow");
		
		$("#defferalPercentageOfAC").removeClass("evenDataRow");
		$("#defferalPercentageOfAC").removeClass("oddDataRow");
		$("#defferalPercentageOfAC").addClass("evenDataRow");
		
		$("#acFeaturesApplies").removeClass("evenDataRow");
		$("#acFeaturesApplies").removeClass("oddDataRow");
		$("#acFeaturesApplies").addClass("oddDataRow");
		
		$("#acIncreases").removeClass("evenDataRow");
		$("#acIncreases").removeClass("oddDataRow");
		$("#acIncreases").addClass("evenDataRow");
		
if(aciAllowed=='Y'){
			
			$("#acIncreasesApplied").removeClass("evenDataRow");
			$("#acIncreasesApplied").removeClass("oddDataRow");
			$("#acIncreasesApplied").addClass("oddDataRow");
						
			$("#annualIncreases").removeClass("evenDataRow");
			$("#annualIncreases").removeClass("oddDataRow");
			$("#annualIncreases").addClass("evenDataRow");
						
			$("#maxAutomaticIncreases").removeClass("evenDataRow");
			$("#maxAutomaticIncreases").removeClass("oddDataRow");
			$("#maxAutomaticIncreases").addClass("oddDataRow");
				
			$("#acwithdrawls").removeClass("evenDataRow");
			$("#acwithdrawls").removeClass("oddDataRow");
			$("#acwithdrawls").addClass("evenDataRow");
			
			$("#acwithdrawlsMonths").removeClass("evenDataRow");
			$("#acwithdrawlsMonths").removeClass("oddDataRow");
			$("#acwithdrawlsMonths").addClass("oddDataRow");
			
			$("#additionalEC").removeClass("evenDataRow");
			$("#additionalEC").removeClass("oddDataRow");
			$("#additionalEC").addClass("evenDataRow");
			
			$("#additionalECRef").removeClass("oddDataRow");
			$("#additionalECRef").removeClass("evenDataRow");
			$("#additionalECRef").addClass("oddDataRow");
			
			$("#vesting").removeClass("evenDataRow");
			$("#vesting").removeClass("oddDataRow");
			$("#vesting").addClass("evenDataRow");
			
		}else{
		
			$("#acwithdrawls").removeClass("evenDataRow");
			$("#acwithdrawls").removeClass("oddDataRow");
			$("#acwithdrawls").addClass("oddDataRow");
			
			$("#acwithdrawlsMonths").removeClass("evenDataRow");
			$("#acwithdrawlsMonths").removeClass("oddDataRow");
			$("#acwithdrawlsMonths").addClass("evenDataRow");
			
			$("#additionalEC").removeClass("evenDataRow");
			$("#additionalEC").removeClass("oddDataRow");
			$("#additionalEC").addClass("oddDataRow");
			
			$("#additionalECRef").removeClass("oddDataRow");
			$("#additionalECRef").removeClass("evenDataRow");
			$("#additionalECRef").addClass("evenDataRow");
			
			$("#vesting").removeClass("evenDataRow");
			$("#vesting").removeClass("oddDataRow");
			$("#vesting").addClass("oddDataRow");
		}
		
	}else{
		
		$("#additionalEC").removeClass("evenDataRow");
		$("#additionalEC").removeClass("oddDataRow");
		$("#additionalEC").addClass("oddDataRow");
		
		$("#additionalECRef").removeClass("oddDataRow");
		$("#additionalECRef").removeClass("evenDataRow");
		$("#additionalECRef").addClass("evenDataRow");
		
		$("#vesting").removeClass("evenDataRow");
		$("#vesting").removeClass("oddDataRow");
		$("#vesting").addClass("oddDataRow");
	}
}

//To display tables rows in alternate colors if no SH type is selected
function modifyBgColorIfNone(){
	
	$("#planHashACA").removeClass("evenDataRow");
	$("#planHashACA").removeClass("oddDataRow");
	$("#planHashACA").addClass("oddDataRow");
	
if($('input[name="planHasSHACA"]:checked').val() == 'Y'){
	
	var aciAllowed="${noticePlanCommonVO.aciAllowed}";
		
		$("#effectveDateOfAC").removeClass("evenDataRow");
		$("#effectveDateOfAC").removeClass("oddDataRow");
		$("#effectveDateOfAC").addClass("evenDataRow");
		
		$("#defferalPercentageOfAC").removeClass("evenDataRow");
		$("#defferalPercentageOfAC").removeClass("oddDataRow");
		$("#defferalPercentageOfAC").addClass("oddDataRow");
		
		$("#acFeaturesApplies").removeClass("evenDataRow");
		$("#acFeaturesApplies").removeClass("oddDataRow");
		$("#acFeaturesApplies").addClass("evenDataRow");
		
		$("#acIncreases").removeClass("evenDataRow");
		$("#acIncreases").removeClass("oddDataRow");
		$("#acIncreases").addClass("oddDataRow");
		
		if(aciAllowed=='Y'){
			$("#acIncreasesApplied").removeClass("evenDataRow");
			$("#acIncreasesApplied").removeClass("oddDataRow");
			$("#acIncreasesApplied").addClass("evenDataRow");
						
			$("#annualIncreases").removeClass("evenDataRow");
			$("#annualIncreases").removeClass("oddDataRow");
			$("#annualIncreases").addClass("oddDataRow");
						
			$("#maxAutomaticIncreases").removeClass("evenDataRow");
			$("#maxAutomaticIncreases").removeClass("oddDataRow");
			$("#maxAutomaticIncreases").addClass("evenDataRow");
						
			$("#acwithdrawls").removeClass("evenDataRow");
			$("#acwithdrawls").removeClass("oddDataRow");
			$("#acwithdrawls").addClass("oddDataRow");
			
			$("#acwithdrawlsMonths").removeClass("evenDataRow");
			$("#acwithdrawlsMonths").removeClass("oddDataRow");
			$("#acwithdrawlsMonths").addClass("evenDataRow");
			
			$("#additionalEC").removeClass("oddDataRow");
			$("#additionalEC").removeClass("evenDataRow");
			$("#additionalEC").addClass("oddDataRow");
			
			$("#additionalECRef").removeClass("evenDataRow");
			$("#additionalECRef").removeClass("oddDataRow");
			$("#additionalECRef").addClass("evenDataRow");
			
			$("#vesting").removeClass("oddDataRow");
			$("#vesting").removeClass("evenDataRow");
			$("#vesting").addClass("oddDataRow");
		}else{
			$("#acwithdrawls").removeClass("evenDataRow");
			$("#acwithdrawls").removeClass("oddDataRow");
			$("#acwithdrawls").addClass("evenDataRow");
			
			$("#acwithdrawlsMonths").removeClass("evenDataRow");
			$("#acwithdrawlsMonths").removeClass("oddDataRow");
			$("#acwithdrawlsMonths").addClass("oddDataRow");
			
			$("#additionalEC").removeClass("oddDataRow");
			$("#additionalEC").removeClass("evenDataRow");
			$("#additionalEC").addClass("evenDataRow");
			
			$("#additionalECRef").removeClass("evenDataRow");
			$("#additionalECRef").removeClass("oddDataRow");
			$("#additionalECRef").addClass("oddDataRow");
			
			$("#vesting").removeClass("oddDataRow");
			$("#vesting").removeClass("evenDataRow");
			$("#vesting").addClass("evenDataRow");
		}
		
		
	}else{
		
		$("#additionalEC").removeClass("oddDataRow");
		$("#additionalEC").removeClass("evenDataRow");
		$("#additionalEC").addClass("evenDataRow");
		
		$("#additionalECRef").removeClass("evenDataRow");
		$("#additionalECRef").removeClass("oddDataRow");
		$("#additionalECRef").addClass("oddDataRow");
		
		$("#vesting").removeClass("oddDataRow");
		$("#vesting").removeClass("evenDataRow");
		$("#vesting").addClass("evenDataRow");
	}	
}

function enablePlanYearEndDateAndPerComp()
{
	setDirtyFlag();
	var frm = document.tabPlanDataForm;
	var confirmation;
	if(document.getElementById("enablePlanYearEndDateAndPercentageCompId").checked == false)
		confirmation = confirm(ERR_SH_NE_CHECKBOX);
	if(null!=confirmation){
		if(confirmation==true) {
			document.getElementById('enableCheckBoxHiddenFieldID').value='N';
			document.getElementById('neContributionApplicableToPlanDateId').value = '';
			document.getElementById('neContributionApplicableToPlanDateId').disabled=true;
			document.getElementById('contributionApplicableToPlanPctId').value = '';
			document.getElementById('contributionApplicableToPlanPctId').disabled = true;
			document.getElementById('enablePlanYearEndDateAndPercentageCompIdImage').removeAttribute('onclick');
		}
		else {
			document.getElementById("enablePlanYearEndDateAndPercentageCompId").checked = true;
			return true;
		}
	}
	else
	{
		document.getElementById('enableCheckBoxHiddenFieldID').value='Y';
		document.getElementById('neContributionApplicableToPlanDateId').disabled=false;
		document.getElementById('contributionApplicableToPlanPctId').disabled = false;
		document.getElementById('contributionApplicableToPlanPctId').value = '3.0';
		document.getElementById('enablePlanYearEndDateAndPercentageCompIdImage').setAttribute("onclick","return handleDateIconClicked(event, 'neContributionApplicableToPlanDateId');");
	}
}

function confirmSwitchOptions(message){
	return window.confirm(message);	
}

$('input[name="planHasAdditionalEC"]').change(function() {
	if(document.getElementById('shEnablePopUpForEmployerContributionsId').value == 'true' &&
			$('input[name="planHasAdditionalEC"]:checked').val() == 'Y'){
		alert(ERR_SH_ADDN_EMP_CONTR);
		document.getElementById('planHasAdditionalECYes').checked = false;
		document.getElementById('planHasAdditionalECNo').checked = true;
		document.getElementById('planHasAdditionalECNo').click();
	}
	goToUrl('safeHarbor','refresh');
});
 
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
if(frm.shACAAnnualIncreaseType == null)
	return;
else 
	acaAnnualIncreaseType = getValueFromRadioButton('shACAAnnualIncreaseType');
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
