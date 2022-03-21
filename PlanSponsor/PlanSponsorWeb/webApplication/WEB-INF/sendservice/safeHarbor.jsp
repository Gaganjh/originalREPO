<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.manulife.pension.ps.web.sendservice.NoticePlanDataForm"%>
<%@ page import="com.manulife.pension.service.notices.valueobject.NoticePlanCommonVO" %>
<%@ page import="com.manulife.pension.ps.web.Constants"%> 
<%@ page import="com.manulife.pension.ps.web.sendservice.util.PlanDataWebUtility"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>  
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>

<c:set var="noticePlanCommonVO" value="${sessionScope.noticePlanCommonVO}" />
<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />

<% String Y ="${planDataConstants.YES_CODE}"; 
pageContext.setAttribute("Y", Y,PageContext.PAGE_SCOPE); %>



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

.safeHarbourLabelExtendedColumn {
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
</style>


<%
NoticePlanDataForm noticePlanDataForm = (NoticePlanDataForm)request.getAttribute("noticePlanDataForm");
%>
<%

String enableCheckBoxFlag = noticePlanDataForm.getEnablePlanYearEndDateAndPercentageComp();

%>
	   
<div id="safeHarbourTabDivId" class="borderedDataBox">
	<div class="subhead">
	    <c:if test="${not param.printFriendly}">
	      <div class="expandCollapseIcons" id="safeHarbourShowIconId" onclick="expandDataDiv('safeHarbour');">
	        <img class="icon" src="/assets/unmanaged/images/plus_icon.gif">
	      </div>
	      <div class="expandCollapseIcons" id="safeHarbourHideIconId" onclick="collapseDataDiv('safeHarbour');">
	        <img class="icon" src="/assets/unmanaged/images/minus_icon.gif">
		  </div>
	    </c:if>
    	<div class="sectionTitle">Safe Harbor</div>
    	<div class="sectionHighlightIcon" id="safeHarbourSectionHighlightIconId">
	   		<ps:sectionHilight name="safeHarbour" singleDisplay="true" displayToolTip="true" suppressDuplicateMessages="true"/>
	  	 </div>
    	<div class="endDataRowAndClearFloats"></div>
  	</div>
 	<div id="safeHarbourDataDivId">
	   <!--[if lt IE 7]>
	   <link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid_IE.css" type="text/css">
	   <![endif]-->
				
		<div class=evenDataRow>
	      <table class=dataTable>
	        <tr>
				<td class=safeHarbourLabelExtendedColumn vAlign=top>
					Select the plan's type of Safe Harbor
				</td>
				<td class=dataColumn>
<form:select path="planHasSafeHarborMatchOrNonElective" disabled="${disableFields}" id="planHasSafeHarborMatchOrNonElective">
					 	<form:option value="">Select one</form:option>
					 	<form:option value="M">Match</form:option>
						<form:option value="N">Non-elective</form:option>
</form:select>
				</td>
	        </tr>
	      </table>			      
	    </div>
<c:if test="${noticePlanDataForm.planHasSafeHarborMatchOrNonElective == 'M'}">
		    <div class="oddDataRow" id="SafeHarborMatchId">
		      <table class=dataTable>
		        <tr>
					<td class=safeHarbourLabelExtendedColumn>
						<table style="margin-top: 6px">
							<tbody>
								<tr>
									<td style="padding:2px;">
										<div class="formulaHeader">&nbsp;Matching Contributions </div>
										<div class="formulaBody" id="2_match_div">
											<table>
												<tbody>
													<tr>
														<td>
															<div style="padding-left:5px">	
<form:input path="matchContributionContribPct1" disabled="${disableFields}" maxlength="5" size="5" cssClass="numericInput" id="matchContributionContribPct1" /> % of the first
															</div>
														</td>
														<td style="padding-right:5px">
<form:input path="matchContributionMatchPct1" disabled="${disableFields}" maxlength="5" size="5" cssClass="numericInput" id="matchContributionMatchPct1" /> %
														</td>
													</tr>
													<tr>
														<td>
															<div style="padding-left:5px" id="2_next_div">
<form:input path="matchContributionContribPct2" disabled="${disableFields}" maxlength="5" size="5" cssClass="numericInput" id="matchContributionContribPct2" /> % of the next
															</div>
														</td>
														<td style="padding-right:5px">
<form:input path="matchContributionMatchPct2" disabled="${disableFields}" maxlength="5" size="5" cssClass="numericInput" id="matchContributionMatchPct2" /> %
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
								<td vAlign=top style="padding-bottom: 15px;">Safe Harbor Match contributions apply to each</td>
								<td style="padding-bottom: 15px;">
<form:select path="matchAppliesToContrib" disabled="${disableFields}" >
<form:options items="${noticePlanDataForm.matchAppliesToContribList}" itemLabel="label" itemValue="value"/>
</form:select>
							 	</td>
							</tr>
							<tr>
								<td vAlign=top style="padding-bottom: 7px;">Are matching contributions being made to another plan?</td>
									<td style="padding-bottom: 7px;">
<form:radiobutton disabled="${disableFields}" path="matchContributionToAnotherPlan" id="anotherPlanMatchingContributionYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton disabled="${disableFields}" path="matchContributionToAnotherPlan" id="anotherPlanMatchingContributionNo" value="${planDataConstants.NO_CODE}" />No

							 	</td>
							</tr>
							<tr>
								<td vAlign=top>If yes, enter the plan name</td>
								<td>
<form:textarea path="matchContributionOtherPlanName" cols="40" disabled="${disableFields}" rows="5" id="safeHarbourPlanName" /><%--  - name="noticePlanDataForm" --%>
							 	</td>
							</tr>
							<tr>
								<td vAlign=top>Does Safe Harbor match apply to Roth contributions?</td>
								
								<td>
<form:radiobutton disabled="${disableFields}" path="safeHarborAppliesToRoth" id="SHMAppliedToRothContributionYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton disabled="${disableFields}" path="safeHarborAppliesToRoth" id="SHMAppliedToRothContributionNo" value="${planDataConstants.NO_CODE}" />No

							 	</td>
							</tr>
							<tr>
								<td vAlign=top>Does Safe Harbor match apply to Catch-up contributions?</td>
								<td>
<form:radiobutton disabled="${disableFields}" path="sHAppliesToCatchUpContributions" id="SHMAppliedToCatchUpContributionYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton disabled="${disableFields}" path="sHAppliesToCatchUpContributions" id="SHMAppliedToCatchUpContributionNo" value="${planDataConstants.NO_CODE}" />No

							 	</td>
							</tr>
						</table>
					</td>
		        </tr>
		      </table>			      
		    </div>
</c:if>
<c:if test="${noticePlanDataForm.planHasSafeHarborMatchOrNonElective =='N'}">
		    <div class="oddDataRow" id="SafeHarborNonElectiveId">
		      <table class=dataTable>
		        <tr>
					<td class=safeHarbourLabelExtendedColumn>
						<div>
<form:radiobutton disabled="${disableFields}" path="nonElectiveContribOption" id="Guaranteed" value="G" />Guarantee

<form:radiobutton disabled="${disableFields}" path="nonElectiveContribOption" id="Flexible" value="F" />Flexible

						</div>
						<table style="margin-top: 6px">
							<tbody>
								<tr>
									<td style="vertical-align:top;">
										<div class="formulaHeader">&nbsp;Non-elective Contributions</div>
										<div class="formulaBody" id="2non_match_div">
											<div style="padding-left:10px; padding-top:2px;padding-bottom:2px;">
<form:input path="nonElectiveContributionPct" disabled="${disableFields}" maxlength="5" size="5" cssClass="numericInput" />&nbsp;% of compensation&nbsp;&nbsp;
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
<form:select path="nonElectiveAppliesToContrib" disabled="${disableFields}" >
<form:options items="${noticePlanDataForm.matchAppliesToContribList}" itemValue="value" itemLabel="label"/>
</form:select>
							 	</td>
							</tr>
							<tr>
								<td vAlign=top style="padding-bottom: 7px;">Are Non-elective contributions being made to another plan?</td>
									<td style="padding-bottom: 7px;">
<form:radiobutton disabled="${disableFields}" path="nonElectiveContribOtherPlan" id="SHNEAppliedToAnotherPlanYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton disabled="${disableFields}" path="nonElectiveContribOtherPlan" id="SHNEAppliedToAnotherPlanNo" value="${planDataConstants.NO_CODE}" />No

							 	</td>
							</tr>
							<tr>
								<td vAlign=top>If yes, enter the plan name</td>
								<td>
<form:textarea path="SHNonElectivePlanName" cols="40" disabled="${disableFields}" rows="5" id="SHNonElectivePlanName" /><%--  - name="noticePlanDataForm" --%>
							 	</td>
							</tr>
						</table>
					</td>
		        </tr>
		      </table>	
		      <table class=dataTable style="width: 729px;height: 12px; background-color: white;">
			    	<tr>
			    		<td class=safeHarbourLabelExtendedColumn></td>
			    		<td class=dataColumn></td>
			    	</tr>
			  </table>
			  <table class=dataTable style="width: 729px;">
			  	<tr>
		    		<td class=safeHarbourLabelExtendedColumn>Select this option and complete the fields if Non-elective contribution is applicable to a prior plan year. If selected, section will only appear in the annual notice.<ps:fieldHilight name="safeHarborEnablePlanYearEndDatePerComp" singleDisplay="true" className="errorIcon" displayToolTip="true"/></td>
		    		<td class=dataColumn>
<input type="hidden" name="enablePlanYearEndDateAndPercentageComp" id="enableCheckBoxHiddenFieldID"/>
						<% if("Y".equals(enableCheckBoxFlag)){ %>
						<table style="margin-top: 5px;">
							<tr>
								<td style="vertical-align: top;">
				   					<input type="checkbox" value="Y" checked id="enablePlanYearEndDateAndPercentageCompId" disabled="${disableFields}"/>
				   				</td>
				   				<td>
							   		Effective for the plan year ending
<form:input path="contributionApplicableToPlanDate" disabled="${disableFields}" maxlength="10" size="10" cssStyle="width: 74px;" id="neContributionApplicableToPlanDateId"/>





						   			<img id="enablePlanYearEndDateAndPercentageCompIdImage" src="/assets/unmanaged/images/cal.gif" border="0" disabled> (mm/dd/yyyy)
				  					, the Company decided to provide fully vested qualified Non-elective contribution of 
<form:input path="contributionApplicableToPlanPct" disabled="${disableFields}" maxlength="5" size="5" cssStyle="width: 40px;" cssClass="numericInput" id="contributionApplicableToPlanPctId" />
				  					% of compensation.
	  							</td>
	  						</tr>
	  					</table>
				   		<% } else { %>
				   		<table style="margin-top: 5px;">
							<tr>
								<td style="vertical-align: top;">
				   					<input type="checkbox" value="Y" id="enablePlanYearEndDateAndPercentageCompId" style="vertical-align: top;" disabled="${disableFields}"/>
				   				</td>
				   				<td>
							   		Effective for the plan year ending
<form:input path="contributionApplicableToPlanDate" disabled="${disableFields}" maxlength="10" size="10" cssStyle="width: 74px;" id="neContributionApplicableToPlanDateId"/>





						   			<img id="enablePlanYearEndDateAndPercentageCompIdImage" src="/assets/unmanaged/images/cal.gif" border="0" disabled> (mm/dd/yyyy)
				  					, the Company decided to provide fully vested qualified Non-elective contribution of 
<form:input path="contributionApplicableToPlanPct" disabled="${disableFields}" maxlength="5" size="5" cssStyle="width: 40px;" cssClass="numericInput" id="contributionApplicableToPlanPctId" />
				  					% of compensation.
	  							</td>
	  						</tr>
	  					</table>
				   		<% }  %>
		    		</td>
			    </tr>
			  </table>
		    </div>
</c:if>
	    
	     <div class="oddDataRow" id="planHashACA">
			      <table class=dataTable>
			        <tr>
						<td class=safeHarbourLabelExtendedColumn vAlign=top>
							Is this a Safe Harbor plan with an Automatic Contribution option?
						</td>
						<td class=dataColumn>
<form:radiobutton disabled="${disableFields}" path="planHasSHACA" id="planHasSHACAYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton path="planHasSHACA" disabled="${disableFields}" id="planHasSHACANo" value="${planDataConstants.NO_CODE}" />No

						</td>
			        </tr>
			      </table>			      
		</div>
	    
<c:if test="${noticePlanDataForm.planHasSHACA =='Y'}">
	     	<DIV class=evenDataRow id="effectveDateOfAC">
				<TABLE class=dataTable>
				<TR vAlign=top>
					<TD class=safeHarbourLabelExtendedColumn><ps:fieldHilight
							name="effectiveDateSH" singleDisplay="true"
							className="errorIcon" displayToolTip="true" /> Effective date of automatic contribution (enrollment) provision</TD>
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
		<DIV class=oddDataRow id="defferalPercentageOfAC">
			<TABLE class=dataTable>
				<TR vAlign=top>
					<TD class=safeHarbourLabelExtendedColumn><ps:fieldHilight
							name="defaultDeferralPercentageSH" singleDisplay="true"
							className="errorIcon" displayToolTip="true" /> Initial default deferral percentage for automatic contributions
							</TD>
					<TD class=dataColumn><c:choose>
							<c:when
								test="${noticePlanCommonVO.defaultDeferralPercentage !=null }">
<input type="text" name="defaultDeferralPercentage" disabled="${disableFields}" value="${noticePlanCommonVO.defaultDeferralPercentage}" maxlength="6"  style="direction: rtl;" size="10" cssClass="numericInput" />%



							</c:when>
							<c:otherwise>
								<p>Pending Plan Information Completion</p>
							</c:otherwise>
						</c:choose></TD>
				</TR>
			</TABLE>
		</DIV>
		
		  <DIV class=evenDataRow id="acFeaturesApplies">      
				      <TABLE class=dataTable>
				        <TR vAlign=top>
				        	<TD class=safeHarbourLabelExtendedColumn width="210">
				        	The notice states that automatic contribution feature applies <b>"if you are eligible for the Plan and have not elected to make contributions, or to opt out of making contributions".</b> In addition to this statement, select who else this automatic contribution feature applies to, if applicable.  If a selection is made, the Notice will include your selection as indicated.</TD>
							<TD class=dataColumn>
<form:checkbox path="sHautomaticContributionFeature1" id="automaticContributionFeature1" disabled="${disableFields}" value="0"/>


									 If you are contributing less than
<form:input path="shContributionFeature1Pct" disabled="${disableFields}" maxlength="5" size="10" cssClass="numericInput" id="contributionFeature1Pct" />%





									 <br/>  
<form:checkbox path="sHautomaticContributionFeature2" id="automaticContributionFeature2" disabled="${disableFields}" value="0"/>



									 If you are hired after
<form:input path="shContributionFeature2Date" disabled="${disableFields}" maxlength="10" size="10" id="contributionFeature2DateId"/>




									 <img id="contributionFeature2DateIdImage" src="/assets/unmanaged/images/cal.gif" border="0"> (mm/dd/yyyy)
									 <br/>
<form:checkbox path="sHautomaticContributionFeature3" id="automaticContributionFeature3" disabled="${disableFields}" value="0"/>



								     Complete if other<br/>  
<form:textarea path="shContributionFeature3SummaryText" cols="40" disabled="${disableFields}" rows="5" id="contributionFeature3SummaryText" /><%--  - name="noticePlanDataForm" --%>




				         
							</TD>
							<TD width="180">
							<content:getAttribute id="autoContributionAppliesToMessage" attribute="text"/>
							<br/>			
							</TD>
				        </TR></TABLE>
			</DIV>
			
			 <DIV class=oddDataRow id="acIncreases">
					<TABLE class=dataTable>
				        <TR vAlign=top>
				        	<TD class=safeHarbourLabelExtendedColumn>
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
					 <DIV class=evenDataRow id="acIncreasesApplied">
						<TABLE class=dataTable>
					          <TR vAlign=top>
						        	<TD class=safeHarbourLabelExtendedColumn>
						        	 <ps:fieldHilight name="aciApplyDate" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
										When are annual increases applied?							
									</TD>
									<TD class=dataColumn>
<form:radiobutton disabled="${disableFields}" path="shACAAnnualIncreaseType" id="acaAnnualIncreaseType1" value="1" />




<input type="text" name="annualApplyDate" disabled="${disableFields}" value="${noticePlanCommonVO.annualApplyDate}"  style="direction: rtl;" maxlength="6" size="10" cssClass="numericInput" /> (mm/dd)



										  <br/>
<form:radiobutton disabled="${disableFields}" path="shACAAnnualIncreaseType" id="acaAnnualIncreaseType2" value="2"/> The pay date following the anniversary date you entered into the Plan



								          <br/>
<form:radiobutton disabled="${disableFields}" path="shACAAnnualIncreaseType" id="acaAnnualIncreaseType3" value="3"/>The pay date following the anniversary of your date of hire



						        	</TD>
					        	</TR>
					       </TABLE>
					   </DIV>
			
					     <DIV class=oddDataRow id="annualIncreases">
								<TABLE class=dataTable>
							        <TR vAlign=top>
							        	<TD class=safeHarbourLabelExtendedColumn>
							        	 <ps:fieldHilight name="annualIncrease" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
											Default annual increase		
											
										</TD>
										<TD class=dataColumn>
										
<input type="text" name="defaultIncreasePercent" disabled="${disableFields}" value="${noticePlanCommonVO.defaultIncreasePercent}"  style="direction: rtl;" maxlength="6" size="10" cssClass="numericInput" />%
											</TD>
							        </TR></TABLE>
					      </DIV>
					      <DIV class=evenDataRow id="maxAutomaticIncreases">
								<TABLE class=dataTable>
							        <TR vAlign=top>
							        	<TD class=safeHarbourLabelExtendedColumn>
							        	 <ps:fieldHilight name="maxAutomaticIncrease" singleDisplay="true" className="errorIcon" displayToolTip="true"/>
											Default maximum for automatic increase
										</TD>
										<td class=dataColumn>
<input type="text" name="defaultAutoIncreaseMaxPercent" disabled="${disableFields}" value="${noticePlanCommonVO.defaultAutoIncreaseMaxPercent}" maxlength="6" style="direction: rtl;" size="10" cssClass="numericInput" />%										

											
											</td>
							        </TR></TABLE>
					        </DIV>
			 </c:if>
			
			      <DIV class=oddDataRow id="acwithdrawls">
						<TABLE class=dataTable>
								<TR vAlign=top>
								        	<TD class=safeHarbourLabelExtendedColumn>
												Does the plan allow automatic contribution withdrawals?
											</TD>
											<TD class=dataColumn>
<form:radiobutton disabled="${disableFields}" path="SHAutoContributionWD" id="autoContributionWDYes" value="Y"/>Yes
<form:radiobutton disabled="${disableFields}" path="SHAutoContributionWD" id="autoContributionWDNo" value="N"/>No
											</TD>
								        </TR>
							</TABLE>      
					</DIV>
					 <DIV class=evenDataRow id="acwithdrawlsMonths">
						<TABLE class=dataTable>
					        <TR vAlign=top>
					        	<TD class=safeHarbourLabelExtendedColumn>
					        	Participants may elect to withdraw their automatic contributions no later than
								</TD>
								<TD class=dataColumn>
								<form:select  path="SHAutomaticContributionDays" styleId="automaticContributionDays" disabled="${disableFields}">
									  <form:option value="90">90</form:option>
									  <form:option value="60">60</form:option>
									  <form:option value="30">30</form:option>
									  <form:option value="00">Other</form:option>
</form:select>&nbsp;days after the first automatic contribution is taken from their compensation.
								<span id="automaticContributionDaysOtherId" style="display: block;">
<form:input path="SHAutomaticContributionDaysOther" disabled="${disableFields}" maxlength="2" onblur="validateOtherDays(this);" size="10" id="automaticContributionDaysOther"/>




										 
										</span> 
								</TD>
					        </TR></TABLE>
					   </DIV>
			
			
</c:if>
	     
	     
	    <div class="evenDataRow" id="additionalEC">
	      <table class=dataTable>
	        <tr>
				<td class=safeHarbourLabelExtendedColumn vAlign=top>
					Does the plan have Additional Employer Contributions?
				</td>
				<td class=dataColumn>
<form:radiobutton disabled="${disableFields}" path="planHasAdditionalEC" id="planHasAdditionalECYes" value="${planDataConstants.YES_CODE}" />Yes

<form:radiobutton disabled="${disableFields}" path="planHasAdditionalEC" id="planHasAdditionalECNo" value="${planDataConstants.NO_CODE}" />No

				</td>
	        </tr>
	      </table>			      
	    </div>
      	<div class="oddDataRow" id="additionalECRef">
	      <table class=dataTable>
	        <tr>
				<td class=safeHarbourLabelExtendedColumn vAlign=top>
					If yes, provide the Summary Plan Description reference(s), such as section name, for this Employer Contribution. Information entered will appear on Notices as entered.
				</td>
				<td class=dataColumn>
<form:textarea path="summaryPlanDesc" cols="40" disabled="${disableFields}" rows="5" cssStyle="margin: 2px;" id="summaryPlanDesc" /><%-- name="noticePlanDataForm" --%>
				</td>
	        </tr>
	      </table>			      
	    </div>
	    <c:if test="${noticePlanDataForm.planHasAdditionalEC =='Y'}">

			  <div class="evenDataRow" id="vesting" style="width: 729px;">
			    	 <jsp:include flush="true" page="vesting.jsp"></jsp:include>
			    </div> 
	    </c:if>
</div>
</div>


<script>
$(document).ready(function(){
	var frm = document.noticePlanDataForm;
	
	//displaying the SHMatch section or SHNonElective section based on the selected value. If none is selected, both the sections will be hidden
	var planHasSafeHarborMatchOrNonElective = frm.planHasSafeHarborMatchOrNonElective.value;
	if(planHasSafeHarborMatchOrNonElective == "M"){
		modifyBgColorIfMatch();
	} 
	else if(planHasSafeHarborMatchOrNonElective == "N"){
		modifyBgColorIfNonElective();
	}
	else{
		modifyBgColorIfNone();
	}
	
	;
	allowACIFields();
	showorhideACDOther();
	checkBoxBtatus();
	enableContributionFeature3SummaryText();		
	enableContributionFeature2DateId();
	enableContributionFeature1Pct();
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
				
				$("#additionalEC").removeClass("evenDataRow");
				$("#additionalEC").removeClass("oddDataRow");
				$("#additionalEC").addClass("evenDataRow");
				
				$("#additionalECRef").removeClass("oddDataRow");
				$("#additionalECRef").removeClass("evenDataRow");
				$("#additionalECRef").addClass("oddDataRow");
				
				$("#vesting").removeClass("evenDataRow");
				$("#vesting").removeClass("oddDataRow");
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

function allowACIFields()
{ 
	var aciAllowed="${noticePlanCommonVO.aciAllowed}";
	var annualApplyDate= "${noticePlanCommonVO.annualApplyDate}";
	var frm = document.noticePlanDataForm; 
	var acaAnnualIncreaseType;
	if(frm.shACAAnnualIncreaseType == null)
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
