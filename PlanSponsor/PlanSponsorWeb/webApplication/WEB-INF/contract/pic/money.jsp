<%@page buffer="none" autoFlush="true" isErrorPage="false" %>
<%@ page import="com.manulife.pension.content.valueobject.*" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="uiConstants" className="com.manulife.pension.ps.web.contract.PlanDataUi" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="pifConstants" className="com.manulife.pension.service.pif.util.PIFConstants" />
<%--
<content:contentBean
  contentId="<%=ContentConstants.ELIGIBILITY_TAB_MUST_BE_COMPLETED%>"
  type="${contentConstants.TYPE_MISCELLANEOUS}" 
  id="eligibilityTabMustBeCompletedText" /> --%>
  
<content:contentBean contentId="80767" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="eligibilityTabMustBeCompletedText" />  

<content:contentBean contentId="81088" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="rolloverContributionsErrorText" />	

<script type="text/javascript">
   var rolloverContributionsErrorText = "<content:getAttribute beanName='rolloverContributionsErrorText' attribute='text'  filter='true' escapeJavaScript="true"/>"; 
   
function onRolloverContributionsPermittedChanged() {
  var enable = true;
  var rolloverContributionsPermitted = $("input[name='planInfoVO.pifMoneyType.rolloverContributionsPermitted']:checked").val();
  if (rolloverContributionsPermitted != "${planDataConstants.YES_CODE}") {
    enable = false;
  }
  <c:if test="${pifDataForm.editMode}">
<c:forEach items="${pifDataForm.planInfoVO.pifMoneyType.permittedRolloverMoneyTypes}" var="rolloverMT" varStatus="count" >


		<c:set var="disablePPY" value="pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_selectedIndicator_yes" scope="page"/> 
		<c:set var="disablePPN" value="pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_selectedIndicator_no" scope="page"/>
		<c:set var="disableRP" value="pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_removedFromPlan" scope="page"/>
	
    if (enable == false) { 
		document.getElementById('pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_selectedIndicator_yes').disabled = true;
		document.getElementById('pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_removedFromPlan').checked = false;
		document.getElementById('pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_removedFromPlan').disabled = true;
		document.getElementById('pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_selectedIndicator_no').checked = true;
		document.getElementById('planInfoVO_eligibility_rolloversDelayedUntilEligibilityReqtMet_selectedIndicator_na').checked = true;
	}else{
		<c:if test="${disableRP}">
			document.getElementById('pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_selectedIndicator_yes').disabled = true;
		</c:if>
		document.getElementById('pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_removedFromPlan').disabled = false;	
		document.getElementById('planInfoVO_eligibility_rolloversDelayedUntilEligibilityReqtMet_selectedIndicator_na').checked = false;
	}

</c:forEach>
   </c:if>
   showOrHideRolloverContributionsPermittedMessge();
}

function showOrHideRolloverContributionsPermittedMessge() {
  var enable = true;
  var rolloverContributionsPermitted = $("input[name='planInfoVO.pifMoneyType.rolloverContributionsPermitted']:checked").val();
  if (rolloverContributionsPermitted != "${planDataConstants.YES_CODE}") {
    enable = false;
  }
  var rolloverContributionsPermittedMessgeSpanId = "#pifDataUi_planInfoVO_pifMoneyType_rolloverContributionsPermitted_span";
  
  if (enable == false) {
    $(rolloverContributionsPermittedMessgeSpanId).hide();
  } else {
    $(rolloverContributionsPermittedMessgeSpanId).show();
  }
}

function onRolloverContributionsPermittedClicked() {
	  var enable = true;
	  var rolloverContributionsPermitted = $("input[name='planInfoVO.pifMoneyType.rolloverContributionsPermitted']:checked").val();
	  if (rolloverContributionsPermitted != "${planDataConstants.YES_CODE}") {
	    enable = false;		
	  }
	  
	  <c:if test="${pifDataForm.editMode}">
<c:forEach items="${pifDataForm.planInfoVO.pifMoneyType.permittedRolloverMoneyTypes}" var="rolloverMT" varStatus="count" >

			<c:set var="disablePPY" value="pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_selectedIndicator_yes" scope="page"/> 
			<c:set var="disablePPN" value="pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_selectedIndicator_no" scope="page"/>
			<c:set var="disableRP" value="pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_removedFromPlan" scope="page"/>	
		
	    if (enable == false) { 
			document.getElementById('pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_selectedIndicator_yes').disabled = true;
			document.getElementById('pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_removedFromPlan').checked = false;
			document.getElementById('pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_removedFromPlan').disabled = true;
			document.getElementById('pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_selectedIndicator_no').checked = true;
			document.getElementById('planInfoVO_eligibility_rolloversDelayedUntilEligibilityReqtMet_selectedIndicator_na').checked = true;
		}else{
			document.getElementById('pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_selectedIndicator_yes').disabled = false;
			document.getElementById('pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_selectedIndicator_yes').checked = false;
			document.getElementById('pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_selectedIndicator_no').checked = true;
			document.getElementById('pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count.index}_removedFromPlan').disabled = false;	
			document.getElementById('planInfoVO_eligibility_rolloversDelayedUntilEligibilityReqtMet_selectedIndicator_na').checked = false;
		}

</c:forEach>
	   </c:if>
	   showOrHideRolloverContributionsPermittedMessge();
	}
	
</script>
<div id="moneyTabDivId" class="borderedDataBox">
<!--start table content -->
<TABLE border=0 cellSpacing=0 cellPadding=0 width=729>
  <TBODY>
  <TR>
    <TD width="100%"><!--[if lt IE 7]>
  <link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid_IE.css" type="text/css">
<![endif]-->
      <DIV class=evenDataRow>
      <TABLE class=dataTable>
        <TBODY>
		<TR><TD class=subhead colspan="5">	
			<DIV class=sectionTitle>
				<c:if test="${pifDataForm.confirmMode}">
					<b>Money Types</b>
				</c:if>
			</DIV>
		</TD></TR>
		<TR><TD class=subsubhead colspan="5">Permitted Employee Money Types&nbsp;&nbsp;(For any other money types not listed below, please contact your John Hancock Representative)</TD></TR>
        <TR>
          <TD vAlign=top width=100 class=dataColumn><STRONG>Money Type Code</STRONG></TD>
          <TD class=dataColumn vAlign=top width=300><STRONG>Money Type Description&nbsp;&nbsp;(Employee Only)</STRONG></TD>
          <TD class=dataColumn vAlign=top width=140 align=center><STRONG>Permitted by plan</STRONG></TD>
          <TD class=dataColumn vAlign=top width=100 align=center><STRONG>TPA Source Code</STRONG></TD>
          <TD class=dataColumn vAlign=top width=70 align=center><STRONG>Removed from plan</STRONG></TD>
        </TR>
		<c:choose>
		  <c:when test="${pifDataForm.editMode or pifDataForm.confirmMode}">
<c:forEach items="${pifDataForm.planInfoVO.pifMoneyType.permittedEmployeeMoneyTypes}" var="permittedEmployeeMT" varStatus="permittedEmployeeMTStatus" >
<c:set var="count" value="${permittedEmployeeMTStatus.index}" />
				  <tr class="${(permittedEmployeeMTStatus.index % 2 == 0) ? 'oddDataRow' : 'evenDataRow'}">
				  <!-- money type code -->
					<td class="dataColumn" style="border-left-width: 0;" title="${permittedEmployeeMT.longName}(${permittedEmployeeMT.shortName})">
						${permittedEmployeeMT.shortName}
					</td>
				  <!-- money type description -->
					<td class="dataColumn">		
						${permittedEmployeeMT.moneyTypeDescription}
					</td>
				  <!-- permitted by plan -->	
					<td class="dataColumn" align="center">
					<c:choose>
					  <c:when test="${pifDataForm.editMode}">				
<form:radiobutton disabled="${disableFields || permittedEmployeeMT.removedFromPlan}" onclick="setDirtyFlag();" path="planInfoVO.pifMoneyType.permittedEmployeeMoneyTypes[${count}].selectedIndicator" id="pifDataUi_planInfoVO_pifMoneyType_permittedEmployeeMoneyTypes_${count}_selectedIndicator_yes" value="true"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.pifMoneyType.permittedEmployeeMoneyTypes[${count}].selectedIndicator" id="pifDataUi_planInfoVO_pifMoneyType_permittedEmployeeMoneyTypes_${count}_selectedIndicator_no" value="false"/>${uiConstants.NO}



					  </c:when>
					  <c:otherwise>
						<c:choose>
						  <c:when test="${permittedEmployeeMT.selectedIndicator}">	
							${uiConstants.YES}
						  </c:when>
						  <c:otherwise>	
							${uiConstants.NO}
						  </c:otherwise>
						</c:choose>	
					  </c:otherwise>
					</c:choose>															
					</td>
				  <!-- tpa source code -->	
					<td class="dataColumn" align="center">
						${permittedEmployeeMT.tpaSourceCode}
					</td>
				  <!-- removed from plan -->	
					<td class="dataColumn" align="center">
						<script type="text/javascript">
							$(document).ready(function() {
								removeFromPlanId = "#pifDataUi_planInfoVO_pifMoneyType_permittedEmployeeMoneyTypes_${count}_removedFromPlan";
								<c:if test="${disableFields}">
								$(removeFromPlanId).prop("disabled", true); 
								</c:if>								

								$(removeFromPlanId).on("click", function(event) {
									var eventId = "#"+event.target.id;
									var index = eventId.split('_')[4];
									var permittedByPlanYId = "#pifDataUi_planInfoVO_pifMoneyType_permittedEmployeeMoneyTypes_"+index+"_selectedIndicator_yes";
									var permittedByPlanNId = "#pifDataUi_planInfoVO_pifMoneyType_permittedEmployeeMoneyTypes_"+index+"_selectedIndicator_no";	
									var permittedByPlanId = "planInfoVO.pifMoneyType.permittedEmployeeMoneyTypes."+index+".selectedIndicator";
									var removedPlanId = "planInfoVO.pifMoneyType.permittedEmployeeMoneyTypes["+index+"].removedFromPlan";
									
									if($(eventId).is(':checked')){ 
										$(removedPlanId).val('true');
										$(permittedByPlanYId).prop("disabled", true); 
										$(permittedByPlanNId).prop("checked", true); 
									}else{
										$(removedPlanId).val('false');
<c:if test="${permittedEmployeeMT.selectedIndicator ==true}">
											$(permittedByPlanYId).prop("disabled", false); 
											$(permittedByPlanYId).prop("checked", true); 
											$(permittedByPlanNId).prop("checked", "");
</c:if>
<c:if test="${permittedEmployeeMT.selectedIndicator !=true}">
											$(permittedByPlanYId).prop("disabled", false); 
											$(permittedByPlanNId).prop("checked", true); 
</c:if>
									}
								});
							});							
						</script>
						<input type="checkbox" 
							id="pifDataUi_planInfoVO_pifMoneyType_permittedEmployeeMoneyTypes_${count}_removedFromPlan" 
							name="planInfoVO.pifMoneyType.permittedEmployeeMoneyTypes[${count}].removedFromPlan" 
							value="true"						
							onclick="setDirtyFlag();"
						<c:if test="${permittedEmployeeMT.removedFromPlan}">checked="checked"</c:if> />
<input type="hidden" name="planInfoVO.pifMoneyType.permittedEmployeeMoneyTypes[${count}].removedFromPlan" value="false" /><%-- input - name="pifDataForm" --%>
									   
					</td>										
				  </tr>	
</c:forEach>
		  </c:when>
		  <c:otherwise>
			View mode allowable money type for loan
		  </c:otherwise>
		</c:choose> 
      </TABLE> 
      
      <DIV class=evenDataRow>
      <TABLE width="100%" class=dataTable>
        <TBODY>
		<TR><TD class=subsubhead colspan="2">Rollover Contributions</TD></TR>
        <TR>
          <TD width="237" class=dataColumn>Are rollover contributions permitted by the plan </TD>
          <TD width="434" class=dataColumn style="BORDER-LEFT-WIDTH: 0px;">
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">				
<form:radiobutton disabled="${disableFields}" onclick="onRolloverContributionsPermittedClicked();setDirtyFlag();" path="planInfoVO.pifMoneyType.rolloverContributionsPermitted" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="onRolloverContributionsPermittedClicked();setDirtyFlag();" path="planInfoVO.pifMoneyType.rolloverContributionsPermitted" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}



				<span id="pifDataUi_planInfoVO_pifMoneyType_rolloverContributionsPermitted_span"
					 style="display: none">
					 <font color="red"><b>
					 <content:getAttribute beanName="eligibilityTabMustBeCompletedText" attribute="text"/>
					 </b></font>
				</span>
			  </c:when>
			  <c:otherwise>
<c:if test="${pifDataForm.planInfoVO.pifMoneyType.rolloverContributionsPermitted =='Y'}">Yes</c:if>

<c:if test="${pifDataForm.planInfoVO.pifMoneyType.rolloverContributionsPermitted =='N'}">No</c:if>

			  </c:otherwise>
			</c:choose>			
		  </TD>
        </TR></TBODY>
       </TABLE></DIV>  
       <TABLE class=dataTable>
        <TBODY>
        <TR>
          <TD style="BORDER-TOP: #cccccc 1px solid" vAlign=top width=100 class=dataColumn><STRONG>Money Type Code</STRONG></TD>
          <TD style="BORDER-TOP: #cccccc 1px solid" class=dataColumn vAlign=top width=300><STRONG>Money Type Description&nbsp;&nbsp;(Employee Only)</STRONG></TD>
          <TD style="BORDER-TOP: #cccccc 1px solid" class=dataColumn vAlign=top width=140 align=center><STRONG>Permitted by plan</STRONG></TD>
          <TD style="BORDER-TOP: #cccccc 1px solid" class=dataColumn vAlign=top width=100 align=center><STRONG>TPA Source Code</STRONG></TD>
          <TD style="BORDER-TOP: #cccccc 1px solid" class=dataColumn vAlign=top width=70 align=center><STRONG>Removed from plan</STRONG></TD>
        </TR>
		<c:choose>
		  <c:when test="${pifDataForm.editMode or pifDataForm.confirmMode}">
<c:forEach items="${pifDataForm.planInfoVO.pifMoneyType.permittedRolloverMoneyTypes}" var="permittedRolloverMT" varStatus="permittedRolloverMTStatus" >

<c:set var="count" value="${permittedRolloverMTStatus.index}" />
			  <tr class="${(permittedRolloverMTStatus.index % 2 == 0) ? 'oddDataRow' : 'evenDataRow'}">
			  <!-- money type code -->
				<!-- substring money type 'EEAT1-403a' to 'EEAT1'-->
				<c:set var="permittedRolloverMTShortName" value="${fn:trim(permittedRolloverMT.shortName)}"/>
				<c:if test="${fn:contains( permittedRolloverMTShortName,pifConstants.EEAT_401A) || fn:contains( permittedRolloverMTShortName,pifConstants.EEAT_403A)}">
					<c:set var="tempStr" value="${fn:split(permittedRolloverMT.shortName,'-')}"/>
					<c:set var="permittedRolloverMTShortName" value="${tempStr[0]}"/>
				</c:if>
				<td class="dataColumn" style="border-left-width: 0;" title="${permittedRolloverMT.longName}(${permittedRolloverMTShortName})">
					${permittedRolloverMTShortName}				
				</td>
			  <!-- money type description -->
				<td class="dataColumn">		
					${permittedRolloverMT.moneyTypeDescription}
				</td>
			  <!-- permitted by plan -->	
				<td class="dataColumn" align="center">
					<c:choose>
					  <c:when test="${pifDataForm.editMode}">				
<form:radiobutton disabled="${disableFields || permittedRolloverMT.removedFromPlan}" onclick="setDirtyFlag();" path="planInfoVO.pifMoneyType.permittedRolloverMoneyTypes[${count}].selectedIndicator" id="pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count}_selectedIndicator_yes" value="true"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.pifMoneyType.permittedRolloverMoneyTypes[${count}].selectedIndicator" id="pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count}_selectedIndicator_no" value="false"/>${uiConstants.NO}



					  </c:when>
					  <c:otherwise>
						<c:choose>
						  <c:when test="${permittedRolloverMT.selectedIndicator}">	
							${uiConstants.YES}
						  </c:when>
						  <c:otherwise>	
							${uiConstants.NO}
						  </c:otherwise>
						</c:choose>	
					  </c:otherwise>
					</c:choose>															
				</td>
			  <!-- tpa source code -->	
				<td class="dataColumn" align="center">
					${permittedRolloverMT.tpaSourceCode}
				</td>
			  <!-- removed from plan -->	
				<td class="dataColumn" align="center">
					<script type="text/javascript">
						$(document).ready(function() {
							removeFromPlanId = "#pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count}_removedFromPlan";
							<c:if test="${disableFields}">
							$(removeFromPlanId).prop("disabled", true); 
							</c:if>	

							$(removeFromPlanId).on("click", function(event) {
								var eventId = "#"+event.target.id;
								var index = eventId.split('_')[4];
								var permittedByPlanYId = "#pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_"+index+"_selectedIndicator_yes";
								var permittedByPlanNId = "#pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_"+index+"_selectedIndicator_no";	
								var permittedByPlanId = "planInfoVO.pifMoneyType.permittedRolloverMoneyTypes."+index+".selectedIndicator";
								var removedPlanId = "planInfoVO.pifMoneyType.permittedRolloverMoneyTypes["+index+"].removedFromPlan";
								
								if($(eventId).is(':checked')){ 
									$(removedPlanId).val('true');
									$(permittedByPlanYId).prop("disabled", true); 
									$(permittedByPlanNId).prop("checked", true); 
								}else{
									$(removedPlanId).val('false');
<c:if test="${permittedRolloverMT.selectedIndicator ==true}">
										$(permittedByPlanYId).prop("disabled", false); 
										$(permittedByPlanYId).prop("checked", true); 
										$(permittedByPlanNId).prop("checked", "");
</c:if>
<c:if test="${permittedRolloverMT.selectedIndicator !=true}">
										$(permittedByPlanYId).prop("disabled", false); 
										$(permittedByPlanNId).prop("checked", true); 
</c:if>
								}
							});
						});							
					</script>
					<input type="checkbox" 
						id="pifDataUi_planInfoVO_pifMoneyType_permittedRolloverMoneyTypes_${count}_removedFromPlan" 
						name="planInfoVO.pifMoneyType.permittedRolloverMoneyTypes[${count}].removedFromPlan" 
						value="true"						
						onclick="setDirtyFlag();"
						<c:if test="${permittedRolloverMT.removedFromPlan}">checked="checked" </c:if> />					
<input type="hidden" name="planInfoVO.pifMoneyType.permittedRolloverMoneyTypes[${count}].removedFromPlan" value="false" /><%--  input - name="pifDataForm" --%>
				</td>										
			  </tr>	  
</c:forEach>
		  </c:when>
		  <c:otherwise>
			View mode allowable money type for loan
		  </c:otherwise>
		</c:choose> 
      </TABLE>   
	<!-- Permitter Employer Money Types -->    
      <TABLE class=dataTable>
        <TBODY>
		<TR><TD class=subsubhead colspan="6">Permitted Employer Money Types</TD></TR>		
        <TR>
         <TD vAlign=top width=100 class=dataColumn><STRONG>Money Type Code</STRONG></TD>
          <TD class=dataColumn vAlign=top width=240><STRONG>Money Type Description&nbsp;&nbsp;(Employer Only)</STRONG></TD>
          <TD class=dataColumn vAlign=top width=100 align=center><STRONG>Permitted by plan</STRONG></TD>
		  <TD class=dataColumn vAlign=top width=100 align=center><STRONG>Trustee directs investments</STRONG></TD>		  
          <TD class=dataColumn vAlign=top width=100 align=center><STRONG>TPA Source Code</STRONG></TD>
          <TD class=dataColumn vAlign=top width=70 align=center><STRONG>Removed from plan</STRONG></TD>
        </TR>
        <TR>
		<c:choose>
		  <c:when test="${pifDataForm.editMode or pifDataForm.confirmMode}">
<c:forEach items="${pifDataForm.planInfoVO.pifMoneyType.permittedEmployerMoneyTypes}" var="permittedEmployerMT" varStatus="permittedEmployerMTStatus" >
<c:set var="count" value="${permittedEmployerMTStatus.index}" />
<c:if test="${permittedEmployerMT.moneyPurchaseIndicator !=true}">
				  <tr class="${(permittedEmployerMTStatus.index % 2 == 0) ? 'oddDataRow' : 'evenDataRow'}">
				  <!-- money type code -->
					<td class="dataColumn" style="border-left-width: 0;" title="${permittedEmployerMT.longName}(${permittedEmployerMT.shortName})">
						${permittedEmployerMT.shortName}
					</td>
				  <!-- money type description -->
					<td class="dataColumn">		
						${permittedEmployerMT.moneyTypeDescription}
					</td>
				  <!-- permitted by plan -->	
					<td class="dataColumn" align="center">
					<c:choose>
					  <c:when test="${pifDataForm.editMode}">				
<form:radiobutton disabled="${disableFields || permittedEmployerMT.removedFromPlan}" onclick="setDirtyFlag();" path="planInfoVO.pifMoneyType.permittedEmployerMoneyTypes[${count}].selectedIndicator" id="pifDataUi_planInfoVO_pifMoneyType_permittedEmployerMoneyTypes_${count}_selectedIndicator_yes" value="true"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.pifMoneyType.permittedEmployerMoneyTypes[${count}].selectedIndicator" id="pifDataUi_planInfoVO_pifMoneyType_permittedEmployerMoneyTypes_${count}_selectedIndicator_no" value="false"/>${uiConstants.NO}



					  </c:when>
					  <c:otherwise>
						<c:choose>
						  <c:when test="${permittedEmployerMT.selectedIndicator}">	
							${uiConstants.YES}
						  </c:when>
						  <c:otherwise>	
							${uiConstants.NO}
						  </c:otherwise>
						</c:choose>	
					  </c:otherwise>
					</c:choose>														
					</td>
					<!-- Trustee directs investments -->
					<td class="dataColumn" align="center">
						<script type="text/javascript">
						$(document).ready(function() {
							investmentIndicatorId = "#planInfoVO_pifMoneyType_permittedEmployerMoneyTypes_${count}_investmentIndicator";
							<c:if test="${disableFields}">
							$(investmentIndicatorId).prop("disabled", true); 
							</c:if>								
							$(investmentIndicatorId).on("click", function(event) {
								var eventId = "#"+event.target.id;
								var index = eventId.split('_')[3];
								investmentIndicatorHiddenId = document.getElementById("investmentIndicatorHidden_"+index);
								if($(eventId).is(':checked')){ 
									investmentIndicatorHiddenId.value = 'true';
								}else{
									investmentIndicatorHiddenId.value = 'false';
								}
							});
						});
						</script>					
						<input type="checkbox" 
							id="planInfoVO_pifMoneyType_permittedEmployerMoneyTypes_${count}_investmentIndicator" 
							name="planInfoVO.pifMoneyType.permittedEmployerMoneyTypes[${count}].investmentIndicator" 
							value="true"						
							onclick="setDirtyFlag();"
							<c:if test="${permittedEmployerMT.investmentIndicator}"> checked="checked" </c:if> />
<form:hidden path="planInfoVO.pifMoneyType.permittedEmployerMoneyTypes[${count}].investmentIndicator" id="investmentIndicatorHidden_${count}" /><%--  input - name="pifDataForm" --%>


					</td>					
				  <!-- tpa source code -->	
					<td class="dataColumn" align="center">
						${permittedEmployerMT.tpaSourceCode}
					</td>
				  <!-- removed from plan -->	
					<td class="dataColumn" align="center">
						<script type="text/javascript">
							$(document).ready(function() {
								removeFromPlanId = "#pifDataUi_planInfoVO_pifMoneyType_permittedEmployerMoneyTypes_${count}_removedFromPlan";
								<c:if test="${disableFields}">
								$(removeFromPlanId).prop("disabled", true); 
								</c:if>	

								$(removeFromPlanId).on("click", function(event) {
									var eventId = "#"+event.target.id;
									var index = eventId.split('_')[4];
									var permittedByPlanYId = "#pifDataUi_planInfoVO_pifMoneyType_permittedEmployerMoneyTypes_"+index+"_selectedIndicator_yes";
									var permittedByPlanNId = "#pifDataUi_planInfoVO_pifMoneyType_permittedEmployerMoneyTypes_"+index+"_selectedIndicator_no";	
									var permittedByPlanId = "planInfoVO.pifMoneyType.permittedEmployerMoneyTypes."+index+".selectedIndicator";
									var removedPlanId = "planInfoVO.pifMoneyType.permittedEmployerMoneyTypes["+index+"].removedFromPlan";
									
									if($(eventId).is(':checked')){ 
										$(removedPlanId).val('true');
										$(permittedByPlanYId).prop("disabled", true); 
										$(permittedByPlanNId).prop("checked", true); 
									}else{
										$(removedPlanId).val('false');
<c:if test="${permittedEmployerMT.selectedIndicator ==true}">
											$(permittedByPlanYId).prop("disabled", false); 
											$(permittedByPlanYId).prop("checked", true); 
											$(permittedByPlanNId).prop("checked", "");
</c:if>
<c:if test="${permittedEmployerMT.selectedIndicator !=true}">
											$(permittedByPlanYId).prop("disabled", false); 
											$(permittedByPlanNId).prop("checked", true); 
</c:if>
									}
								});
							});							
						</script>
						<input type="checkbox" 
							id="pifDataUi_planInfoVO_pifMoneyType_permittedEmployerMoneyTypes_${count}_removedFromPlan" 
							name="planInfoVO.pifMoneyType.permittedEmployerMoneyTypes[${count}].removedFromPlan" 
							value="true"						
							onclick="setDirtyFlag();"
						<c:if test="${permittedEmployerMT.removedFromPlan}">checked="checked" </c:if> />
<input type="hidden" name="planInfoVO.pifMoneyType.permittedEmployerMoneyTypes[${count}].removedFromPlan" value="false" /><%--  input - name="pifDataForm" --%>
					</td>										
				  </tr>
</c:if>
</c:forEach>
		  </c:when>
		  <c:otherwise>
			View mode allowable money type for loan
		  </c:otherwise>
		</c:choose>
        </TR>
      </TABLE> 
	<!-- Permitter Employer Money Types -->    
      <TABLE class=dataTable>
        <TBODY>
		<TR><TD class=subsubhead colspan="6">Permitted Employer Money Types</TD></TR>			
        <TR>
         <TD vAlign=top width=100 class=dataColumn><STRONG>Money Type Code</STRONG></TD>
          <TD class=dataColumn vAlign=top width=240><STRONG>Money Purchase or Target Benefit Plans Only</STRONG></TD>
          <TD class=dataColumn vAlign=top width=100 align=center><STRONG>Permitted by plan</STRONG></TD>
		  <TD class=dataColumn vAlign=top width=100 align=center><STRONG>Trustee directs investments</STRONG></TD>		  
          <TD class=dataColumn vAlign=top width=100 align=center><STRONG>TPA Source Code</STRONG></TD>
          <TD class=dataColumn vAlign=top width=70 align=center><STRONG>Removed from plan</STRONG></TD>
        </TR>
        <TR>
		<c:choose>
		  <c:when test="${pifDataForm.editMode or pifDataForm.confirmMode}">
<c:forEach items="${pifDataForm.planInfoVO.pifMoneyType.permittedEmployerMoneyTypes}" var="permittedERMoneyPurchaseMT" varStatus="permittedERMoneyPurchaseMTStatus" >
<c:set var="count" value="${permittedERMoneyPurchaseMTStatus.index}" />
<c:if test="${permittedERMoneyPurchaseMT.moneyPurchaseIndicator ==true}">
				  <tr class="${(permittedERMoneyPurchaseMTStatus.index % 2 == 0) ? 'oddDataRow' : 'evenDataRow'}">
				  <!-- money type code -->
					<td class="dataColumn" style="border-left-width: 0;" title="${permittedERMoneyPurchaseMT.longName}(${permittedERMoneyPurchaseMT.shortName})">
						${permittedERMoneyPurchaseMT.shortName}
					</td>
				  <!-- money type description -->
					<td class="dataColumn">		
						${permittedERMoneyPurchaseMT.moneyTypeDescription}
					</td>
				  <!-- permitted by plan -->	
					<td class="dataColumn" align="center">
					<c:choose>
					  <c:when test="${pifDataForm.editMode}">				
<form:radiobutton disabled="${disableFields || permittedERMoneyPurchaseMT.removedFromPlan}" onclick="setDirtyFlag();" path="planInfoVO.pifMoneyType.permittedEmployerMoneyTypes[${count}].selectedIndicator" id="pifDataUi_planInfoVO_pifMoneyType_permittedEmployerMoneyTypes_${count}_selectedIndicator_yes" value="true"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.pifMoneyType.permittedEmployerMoneyTypes[${count}].selectedIndicator" id="pifDataUi_planInfoVO_pifMoneyType_permittedEmployerMoneyTypes_${count}_selectedIndicator_no" value="false"/>${uiConstants.NO}



					  </c:when>
					  <c:otherwise>
						<c:choose>
						  <c:when test="${permittedERMoneyPurchaseMT.selectedIndicator}">	
							${uiConstants.YES}
						  </c:when>
						  <c:otherwise>	
							${uiConstants.NO}
						  </c:otherwise>
						</c:choose>	
					  </c:otherwise>
					</c:choose>								
					</td>
					<!-- Trustee directs investments -->
					<td class="dataColumn" align="center">					
						<script type="text/javascript">
						$(document).ready(function() {
							investmentIndicatorId = "#planInfoVO_pifMoneyType_permittedEmployerMoneyTypes_${count}_investmentIndicator";
							<c:if test="${disableFields}">
							$(investmentIndicatorId).prop("disabled", true); 
							</c:if>								
							$(investmentIndicatorId).on("click", function(event) {
								var eventId = "#"+event.target.id;
								var index = eventId.split('_')[3];
								investmentIndicatorHiddenId = document.getElementById("investmentIndicatorHidden_"+index);
								if($(eventId).is(':checked')){ 
									investmentIndicatorHiddenId.value = 'true';
								}else{
									investmentIndicatorHiddenId.value = 'false';
								}
							});
						});
						</script>					
						<input type="checkbox" 
							id="planInfoVO_pifMoneyType_permittedEmployerMoneyTypes_${count}_investmentIndicator" 
							name="planInfoVO.pifMoneyType.permittedEmployerMoneyTypes[${count}].investmentIndicator" 
							value="true"						
							onclick="setDirtyFlag();"
							<c:if test="${permittedERMoneyPurchaseMT.investmentIndicator}"> checked="checked" </c:if> />
<form:hidden path="planInfoVO.pifMoneyType.permittedEmployerMoneyTypes[${count}].investmentIndicator" id="investmentIndicatorHidden_${count}" /><%--  input - name="pifDataForm" --%>


					</td>					
				  <!-- tpa source code -->	
					<td class="dataColumn" align="center">
						${permittedERMoneyPurchaseMT.tpaSourceCode}
					</td>
				  <!-- removed from plan -->	
					<td class="dataColumn" align="center">
						<script type="text/javascript">
							$(document).ready(function() {
								removeFromPlanId = "#pifDataUi_planInfoVO_pifMoneyType_permittedEmployerMoneyTypes_${count}_removedFromPlan";
								<c:if test="${disableFields}">
								$(removeFromPlanId).prop("disabled", true); 
								</c:if>	

								$(removeFromPlanId).on("click", function(event) {
									var eventId = "#"+event.target.id;
									var index = eventId.split('_')[4];
									var permittedByPlanYId = "#pifDataUi_planInfoVO_pifMoneyType_permittedEmployerMoneyTypes_"+index+"_selectedIndicator_yes";
									var permittedByPlanNId = "#pifDataUi_planInfoVO_pifMoneyType_permittedEmployerMoneyTypes_"+index+"_selectedIndicator_no";	
									var permittedByPlanId = "planInfoVO.pifMoneyType.permittedEmployerMoneyTypes."+index+".selectedIndicator";
									var removedPlanId = "planInfoVO.pifMoneyType.permittedEmployerMoneyTypes["+index+"].removedFromPlan";
									
									if($(eventId).is(':checked')){ 
										$(removedPlanId).val('true');
										$(permittedByPlanYId).prop("disabled", true); 
										$(permittedByPlanNId).prop("checked", true); 
									}else{
										$(removedPlanId).val('false');
<c:if test="${permittedERMoneyPurchaseMT.selectedIndicator ==true}">
											$(permittedByPlanYId).prop("disabled", false); 
											$(permittedByPlanYId).prop("checked", true); 
											$(permittedByPlanNId).prop("checked", "");
</c:if>
<c:if test="${permittedERMoneyPurchaseMT.selectedIndicator !=true}">
											$(permittedByPlanYId).prop("disabled", false); 
											$(permittedByPlanNId).prop("checked", true); 
</c:if>
									}
								});
							});							
						</script>
						<input type="checkbox" 
							id="pifDataUi_planInfoVO_pifMoneyType_permittedEmployerMoneyTypes_${count}_removedFromPlan" 
							name="planInfoVO.pifMoneyType.permittedEmployerMoneyTypes[${count}].removedFromPlan" 
							value="true"						
							onclick="setDirtyFlag();"
						<c:if test="${permittedERMoneyPurchaseMT.removedFromPlan}">checked="checked" </c:if> />
<input type="hidden" name="planInfoVO.pifMoneyType.permittedEmployerMoneyTypes[${count}].removedFromPlan" value="false" /><%--  input - name="pifDataForm" --%>
					</td>										
				  </tr>
</c:if>
</c:forEach>
		  </c:when>
		  <c:otherwise>
			View mode allowable money type for loan
		  </c:otherwise>
		</c:choose>
        </TR>
      </TABLE>		  
</DIV>
 
<SCRIPT type=text/javascript>
$(document).ready(function() {
	onRolloverContributionsPermittedChanged();
});
</SCRIPT>

 </TD></TR></TABLE><!-- footer table -->						
<!--end table content -->
</div>
