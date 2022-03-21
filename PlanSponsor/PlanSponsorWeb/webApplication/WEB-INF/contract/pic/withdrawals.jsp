<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@page import="com.manulife.pension.service.pif.valueobject.PIFAllowableMoneyTypesVO" %>

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="uiConstants" className="com.manulife.pension.ps.web.contract.PlanDataUi" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="pifConstants" className="com.manulife.pension.service.pif.util.PIFConstants" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_HEADING_WITHDRAWAL}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="withdrawalsText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_QMAC_QNEC_NOTE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="hardshipWithdrawalQmacQnecMoneyTypeText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_NO_HARDSHIP_MONEY_TYPES}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="noHardshipMoneyTypesText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_PROVISIONS_FOR_WITHDRAWAL_REASONS_TEXT_TPA}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="planWithdrawalReasonText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_RETIREMENT_PROVISIONS}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="retirementProvisions"/>
<content:contentBean
contentId="${contentConstants.FEE_INFORMATION_FOR_404A5_DISCLOSURE_PURPOSES_FOR_WITHDRAWALS}"
  type="${contentConstants.TYPE_MISCELLANEOUS}" 
  id="applyFeeInformation404a5DisclosurePurposeForWithdrawals" />

<div id="withdrawalsTabDivId" class="borderedDataBox">
<HTML><BODY>
<SCRIPT  type=text/javascript>

function formatPIFWithdrawalAmount(field){ 
    if (field.value.length==0) {
        return true;
     }
 	if (field.value!="0.00") {
 	    if (field.value == "******") {
 	      // masked currency field.
 	      return true;
 	    }
 		var num = field.value.replace(/\$|\,/g,'');
 		if (num.length == 0) {
 			return false;
 		} else if (isNaN(num)) {
 			return false;
 		} else if (num.indexOf(".") != -1 && num.substring(num.indexOf(".")+1,num.length).length > 2) {
 			return false;
 		} else {
 			field.value=parseCurrencyInput(num);
 			return true;
 		}
 	}	
}
function validatePIFCurrencyField(field, min, max, invalidMsg, decimalMsg, minMsg, maxMsg) {
    if (field.value.length==0) {
       return true;
    }
	if (field.value!="0.00") {
	    if (field.value == "******") {
	      // masked currency field.
	      return true;
	    }
		var num = field.value.replace(/\$|\,/g,'');
		if (num.length == 0) {
			alert(invalidMsg);
			setTimeout(function(){field.focus(); field.select();},10);
			field.value="";
			return false;
		} else if (isNaN(num)) {
			alert(invalidMsg);
			setTimeout(function(){field.focus(); field.select();},10);
			field.value="";
			return false;
		} else if (num.indexOf(".") != -1 && num.substring(num.indexOf(".")+1,num.length).length > 2) {
			alert(decimalMsg);
			setTimeout(function(){field.focus(); field.select();},10);
			field.value="";
			return false;
		} else if (num < min) {
			alert(minMsg);
			setTimeout(function(){field.focus(); field.select();},10);
			field.value="";
			return false;
		} else if (num > max) {
			alert(maxMsg);
			setTimeout(function(){field.focus(); field.select();},10);
			field.value="";
			return false;
		} else {
			field.value=parseCurrencyInput(num);
			return true;
		}
	}		 
}
function validatePIFMaxWithdrawalAmount(field) {
    return validatePIFCurrencyField(field, 0, 999999.99, ERR_INVALID_MAX_HARDSHIP, ERR_DECIMAL_2_maxWithdrawal, ERR_MAX_HARDSHIP_RANGE, ERR_OUT_OF_RANGE_maxHardship);
}

function validatePIFMinWithdrawalAmount(field) {
    return validatePIFCurrencyField(field, 0, 999999.99, ERR_INVALID_MIN_HARDSHIP, ERR_DECIMAL_2_minWithdrawal, ERR_MIN_HARDSHIP_RANGE, ERR_OUT_OF_RANGE_minHardship);
}

function handleAllowedMoneyTypesForWithdrawals(field,count){
	setDirtyFlag();
	allowedMoneyTypesForWithdrawalsHiddenId = document.getElementById("withdrawals_allowedMoneyTypes_selectedIndicator"+count);
	if(field.checked){ 
		allowedMoneyTypesForWithdrawalsHiddenId.value = 'Y';
	}else{
		allowedMoneyTypesForWithdrawalsHiddenId.value = 'N';
	}
}

$(document).ready(function() { 
  <c:if test="${pifDataForm.editMode}">
    handleAllowHardshipWithdrawalsClicked('${pifDataForm.planInfoVO.withdrawals.allowHardshipWithdrawals}');  
    formatPIFWithdrawalAmount(document.getElementById("minimumHardshipAmountId"));
    formatPIFWithdrawalAmount(document.getElementById("maximumHardshipAmountId"));
    var allowPreRetirementWithdrawalsAge = '${pifDataForm.planInfoVO.withdrawals.allowPreRetirementWithdrawals}';
    handleAllowPreRetirementWithdrawalsClicked(allowPreRetirementWithdrawalsAge);
  </c:if>
});

</SCRIPT>
<!--start table content -->
    <DIV id=withdrawalsDataDivId>
	<table width="729" class="dataTable">
		<TR><TD class=subhead>	
			<DIV class=sectionTitle>
				<c:if test="${pifDataForm.confirmMode}">
					<content:getAttribute beanName="withdrawalsText" attribute="text"/>
				</c:if>
			</DIV>
		</TD></TR>
		<TR><TD class=subsubhead>General Withdrawal provisions</TD></TR>
	</table> 
    <DIV class=evenDataRow>
      <TABLE width="729" class=dataTable>
        <TBODY>
        <TR>
          <TD class=withdrawalsExtendedLabelColumn>Does the plan require spousal consent for distributions?</TD>
          <TD class=dataColumn>
            <c:choose>
              <c:when test="${pifDataForm.editMode}">
<form:radiobutton disabled="${disableFields}" onclick="return handleRequiresSpousalConsentForDistributionsClicked('${planDataConstants.YES_CODE}', '${uiConstants.YES}')" path="planInfoVO.withdrawals.requiresSpousalConsentForDistributions" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="return handleRequiresSpousalConsentForDistributionsClicked('${planDataConstants.NO_CODE}', '${uiConstants.NO}')" path="planInfoVO.withdrawals.requiresSpousalConsentForDistributions" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}



              </c:when>
              <c:otherwise>
<c:if test="${pifDataForm.planInfoVO.withdrawals.requiresSpousalConsentForDistributions == 'Y'}">

					Yes
</c:if>
<c:if test="${pifDataForm.planInfoVO.withdrawals.requiresSpousalConsentForDistributions == 'N'}">

					No
</c:if>
              </c:otherwise>
            </c:choose>
		  </TD>
		</TR>
        <TR>
          <TD class=withdrawalsExtendedLabelColumn>Does the plan allow mandatory distributions (involuntary withdrawals)?</TD>
          <TD class=dataColumn>
            <c:choose>
              <c:when test="${pifDataForm.editMode}">
<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.withdrawals.allowMandatoryDistributions" value="true"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.withdrawals.allowMandatoryDistributions" value="false"/>${uiConstants.NO}



              </c:when>
              <c:otherwise>
<c:if test="${pifDataForm.planInfoVO.withdrawals.allowMandatoryDistributions ==true}">

					Yes
</c:if>
<c:if test="${pifDataForm.planInfoVO.withdrawals.allowMandatoryDistributions ==false}">

					No
</c:if>
              </c:otherwise>
            </c:choose>
		  </TD>
        </TR>
        <TR>
          <TD class=withdrawalsExtendedLabelColumn>Does the plan allow in-service withdrawals of employee voluntary contributions (EEVND)?</TD>
          <TD class=dataColumn>
            <c:choose>
              <c:when test="${pifDataForm.editMode}">
			  <c:set scope="request" var="disableEEVND" value="true"/>
<c:forEach items="${pifDataForm.planInfoVO.pifMoneyType.permittedEmployeeMoneyTypes}" var="permittedEmployeeMT" varStatus="count" >
<c:if test="${permittedEmployeeMT.moneyTypeCode == 'EEVND'}">
<c:if test="${permittedEmployeeMT.selectedIndicator ==true}">
							<c:set scope="request" var="disableEEVND" value="false"/>
<form:radiobutton disabled="${disableEEVND}" onclick="setDirtyFlag();" path="planInfoVO.withdrawals.allowEmployeeVoluntaryContributions" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}



<form:radiobutton disabled="${disableEEVND}" onclick="setDirtyFlag();" path="planInfoVO.withdrawals.allowEmployeeVoluntaryContributions" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}



</c:if>
<c:if test="${permittedEmployeeMT.selectedIndicator !=true}">
<form:radiobutton disabled="${disableEEVND}" path="planInfoVO.withdrawals.allowEmployeeVoluntaryContributions"/>${uiConstants.YES}


<form:radiobutton disabled="${disableEEVND}" path="planInfoVO.withdrawals.allowEmployeeVoluntaryContributions"/>${uiConstants.NO}


</c:if>
</c:if>
</c:forEach>
              </c:when>
              <c:otherwise>
<c:forEach items="${pifDataForm.planInfoVO.pifMoneyType.permittedEmployeeMoneyTypes}" var="permittedEmployeeMT" varStatus="count" >
<c:if test="${permittedEmployeeMT.moneyTypeCode == 'EEVND'}">
<c:if test="${permittedEmployeeMT.selectedIndicator == true}">
<c:if test="${pifDataForm.planInfoVO.withdrawals.allowEmployeeVoluntaryContributions == 'Y'}">

								Yes
</c:if>
<c:if test="${pifDataForm.planInfoVO.withdrawals.allowEmployeeVoluntaryContributions == 'N'}">

								No
</c:if>
</c:if>
</c:if>
</c:forEach>
              </c:otherwise>
            </c:choose>
		  </TD>
        </TR>
        <TD class=withdrawalsExtendedLabelColumn>Does the plan allow qualified birth or adoption distributions?</TD>
          <TD class=dataColumn>
            <c:choose>
              <c:when test="${pifDataForm.editMode}">
<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.withdrawals.allowQualifiedBirthOrAdoptionDistribution" value="true"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="setDirtyFlag();" path="planInfoVO.withdrawals.allowQualifiedBirthOrAdoptionDistribution" value="false"/>${uiConstants.NO}



              </c:when>
              <c:otherwise>
<c:if test="${pifDataForm.planInfoVO.withdrawals.allowQualifiedBirthOrAdoptionDistribution ==true}">

					Yes
</c:if>
<c:if test="${pifDataForm.planInfoVO.withdrawals.allowQualifiedBirthOrAdoptionDistribution ==false}">

					No
</c:if>
              </c:otherwise>
            </c:choose>
		  </TD>
        </TR>
        <TR>
        </TBODY>
      </TABLE>
	  </DIV>
		<table width="729" class="dataTable">
			<TR><TD class=subsubhead>Retirement provisions</TD></TR>
		</table>	  
      <DIV class=evenDataRow>
      <TABLE class=dataTable>
	    <!-- retirement withdrawals -->
        <TBODY>
        <TR>
          <TD width="400" class=withdrawalsExtendedLabelColumn>Retirement withdrawals are permitted when the employee reaches the following age </TD>
		  <TD class=dataColumn>
          <c:choose>
            <c:when test="${pifDataForm.editMode}">
<form:input path="planInfoVO.withdrawals.retirementAge" disabled="${disableFields}" maxlength="4" onblur="validateNormalRetireAge(this)" onchange="setDirtyFlag();" size="2" cssClass="numericInput"/>






            </c:when>
            <c:otherwise> 
                ${pifDataForm.planInfoVO.withdrawals.retirementAge}
            </c:otherwise>
          </c:choose> 
		  </TD>
        </TR>		
		<!-- early retirment withdrawal -->
        <TR>
          <TD width="400" class=withdrawalsExtendedLabelColumn>Does the plan allow pre-retirement withdrawals?</TD>
		  <TD class=dataColumn>
            <c:choose>
              <c:when test="${pifDataForm.editMode}">
<form:radiobutton disabled="${disableFields}" onclick="return handleAllowPreRetirementWithdrawalsClicked('true');setDirtyFlag();" path="planInfoVO.withdrawals.allowPreRetirementWithdrawals" id="preRetirementWithdrawalsId" value="true"/>${uiConstants.YES}




<form:radiobutton disabled="${disableFields}" onclick="return handleAllowPreRetirementWithdrawalsClicked('false');setDirtyFlag();" path="planInfoVO.withdrawals.allowPreRetirementWithdrawals" id="preRetirementWithdrawalsId" value="false"/>${uiConstants.NO}




              </c:when>
              <c:otherwise>
<c:if test="${pifDataForm.planInfoVO.withdrawals.allowPreRetirementWithdrawals ==true}">

					Yes
</c:if>
<c:if test="${pifDataForm.planInfoVO.withdrawals.allowPreRetirementWithdrawals ==false}">

					No
</c:if>
              </c:otherwise>
            </c:choose>
			</TD>
		</TR>
        <TR>
          <TD width="400" class=withdrawalsExtendedLabelColumn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Pre-retirement withdrawals are permitted when the<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;employee reaches the following age </TD>
		  <TD class=dataColumn>
          <c:choose>
            <c:when test="${pifDataForm.editMode}"> 	
 <%--<form:input path="planInfoVO.withdrawals.preRetirementWithdrawalsAge" disabled="${disableFields}" maxlength="4" onblur="validateEarlyRetireAge(this)" onchange="setDirtyFlag();" size="2" cssClass="numericInput" id="preRetirementWithdrawalsAgeId"/>--%>
<form:input value="${pifDataForm.planInfoVO.withdrawals.preRetirementWithdrawalsAge}" path="planInfoVO.withdrawals.preRetirementWithdrawalsAge" disabled="${disableFields}" maxlength="4" onblur="validateEarlyRetireAge(this)" onchange="setDirtyFlag();" size="2" cssClass="numericInput" id="preRetirementWithdrawalsAgeId"/>
<input type="hidden" value="${pifDataForm.planInfoVO.withdrawals.preRetirementWithdrawalsAge}" name="preRetirementWithdrawalsAge" id="preRetirementWithdrawalsAgeHidden" />

            </c:when>
            <c:otherwise> 
               ${pifDataForm.planInfoVO.withdrawals.preRetirementWithdrawalsAge}
            </c:otherwise>
          </c:choose>
		  </TD>
		</TR>
		</TBODY>
	  </TABLE>
	  </DIV>
		<table width="729" class="dataTable">
			<TR><TD class=subsubhead>Hardship Provisions (if applicable)</TD></TR>
		</table>	  
      <DIV class=evenDataRow>
      <TABLE class=dataTable>
        <TBODY>
        <TR>
          <TD width="400" class=withdrawalsExtendedLabelColumn>Does the plan allow hardship withdrawals?</TD>
          <TD class=dataColumn>
           <c:choose>
              <c:when test="${pifDataForm.editMode}">
<form:radiobutton disabled="${disableFields}" onclick="handleAllowHardshipWithdrawalsClicked('true');setDirtyFlag();" path="planInfoVO.withdrawals.allowHardshipWithdrawals" id="allowHardshipWithdrawalsId" value="true"/>${uiConstants.YES}




<form:radiobutton disabled="${disableFields}" onclick="handleAllowHardshipWithdrawalsClicked('false');setDirtyFlag();" path="planInfoVO.withdrawals.allowHardshipWithdrawals" id="allowHardshipWithdrawalsId" value="false"/>${uiConstants.NO}




              </c:when>
              <c:otherwise>
<c:if test="${pifDataForm.planInfoVO.withdrawals.allowHardshipWithdrawals ==true}">

					Yes
</c:if>
<c:if test="${pifDataForm.planInfoVO.withdrawals.allowHardshipWithdrawals ==false}">

					No
</c:if>
              </c:otherwise>
            </c:choose>	
		  
		  </TD></TR>
        <TR>
          <TD width="400" class=withdrawalsExtendedLabelColumn>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Hardship withdrawals are based on</TD>
          <TD class=dataColumn>
              <c:choose>
                <c:when test="${pifDataForm.editMode}">
<form:radiobutton onclick="return handleHardshipProvisionClicked('${planDataConstants.HARDSHIP_WITHDRAWAL_PROVISION_FACTS_AND_CIRCUMSTANCES}')" path="planInfoVO.withdrawals.hardshipWithdrawalsBase" id="hardshipWithdrawalProvisionsFactsCircumstancesId" value="${planDataConstants.HARDSHIP_WITHDRAWAL_PROVISION_FACTS_AND_CIRCUMSTANCES}"/>${uiConstants.FACTS_AND_CIRCUMSTANCES}



<form:radiobutton onclick="return handleHardshipProvisionClicked('${planDataConstants.HARDSHIP_WITHDRAWAL_PROVISION_SAFE_HARBOR}')" path="planInfoVO.withdrawals.hardshipWithdrawalsBase" id="hardshipWithdrawalProvisionsSafeHarborId" value="${planDataConstants.HARDSHIP_WITHDRAWAL_PROVISION_SAFE_HARBOR}"/>Safe Harbor rules
<input type="hidden" value=${pifDataForm.planInfoVO.withdrawals.hardshipWithdrawalsBase} name="hardshipWithdrawalsBase" id="hardshipWithdrawalProvisionsHiddenId" />

                </c:when>
                <c:otherwise>
<c:if test="${pifDataForm.planInfoVO.withdrawals.hardshipWithdrawalsBase == 'F'}">

					${uiConstants.FACTS_AND_CIRCUMSTANCES}
</c:if>
<c:if test="${pifDataForm.planInfoVO.withdrawals.hardshipWithdrawalsBase == 'S'}">

					Safe Harbor rules
</c:if>
                </c:otherwise>
              </c:choose> 			  
		  </TD>
        </TR>
        
        
             <TR>
          <TD width="400" class=withdrawalsExtendedLabelColumn>Does the plan allow earnings from EEDEF hardship withdrawals?</TD>
          <TD class=dataColumn>
          
            <c:choose>
              <c:when test="${pifDataForm.editMode}">
              <form:radiobutton path="planInfoVO.withdrawals.eedefEarningsAllowedInd" id="eedefEarningsAllowedIndIdYes" 
                            onclick="return handleEedefEarningsAllowedIndClicked('${planDataConstants.YES_CODE}')" 
                            value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}
               <form:radiobutton path="planInfoVO.withdrawals.eedefEarningsAllowedInd" id="eedefEarningsAllowedIndIdNo"
                            onclick="return handleEedefEarningsAllowedIndClicked('${planDataConstants.NO_CODE}')" 
                            value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}
                           <%--  <form:hidden path="planInfoVO.withdrawals.eedefEarningsAllowedInd" id="eedefEarningsAllowedIndHiddenId"/> --%>
                 <input type="hidden" name="pifDataForm.planInfoVO.withdrawals.eedefEarningsAllowedInd" id="eedefEarningsAllowedIndHiddenId"/>
   
			 							             
      
              </c:when>
              <%-- <c:when test="${pifDataForm.confirmMode}">
              </c:when> --%>
              <c:otherwise>
              <c:if test="${pifDataForm.planInfoVO.withdrawals.eedefEarningsAllowedInd == 'Y'}">
					Yes
				  </c:if>
				    <c:if test="${pifDataForm.planInfoVO.withdrawals.eedefEarningsAllowedInd == 'N'}">
					No
				  </c:if>	
              </c:otherwise>
            </c:choose>
  
            </td>
          </tr>
  
        <TR>
          <TD width="400" class=withdrawalsExtendedLabelColumn>Minimum amount for hardship withdrawal</TD>
          <TD class=dataColumn>
              <c:choose>
                <c:when test="${pifDataForm.editMode}">
$<form:input path="planInfoVO.withdrawals.minimumHardshipAmount" 
disabled="${disableFields}" maxlength="14" onblur="validatePIFMinWithdrawalAmount(this)" 
onchange="setDirtyFlag();" size="14" cssClass="numericInput" id="minimumHardshipAmountId"/>







<input type="hidden" name="planInfoVO.withdrawals.minimumHardshipAmount" id="minimumHardshipAmountHiddenId" /><%--  input - name="pifDataForm" --%>
 							  





                </c:when>
                <c:otherwise>
                  <fmt:formatNumber value="${pifDataForm.planInfoVO.withdrawals.minimumHardshipAmount}" type="CURRENCY" currencyCode="USD"/>
                </c:otherwise>
              </c:choose>
			
		  </TD>
        </TR>
        <TR>
          <TD width="400" class=withdrawalsExtendedLabelColumn>Maximum amount for hardship withdrawal</TD>
          <TD class=dataColumn>
              <c:choose>
                <c:when test="${pifDataForm.editMode}">
$<form:input path="planInfoVO.withdrawals.maximumHardshipAmount" disabled="${disableFields}"  
maxlength="14" onblur="validatePIFMaxWithdrawalAmount(this)"  onchange="setDirtyFlag();" size="14" 
cssClass="numericInput" id="maximumHardshipAmountId"/>

<input type="hidden" id="maximumHardshipAmountHiddenId"
			 name="pifDataForm.planInfoVO.withdrawals.maximumHardshipAmount" />
                </c:when>
                <c:otherwise>
                  <fmt:formatNumber value="${pifDataForm.planInfoVO.withdrawals.maximumHardshipAmount}" type="CURRENCY" currencyCode="USD"/>
                </c:otherwise>
              </c:choose>
		  </TD>
        </TR>
        </TBODY>
      </TABLE></DIV>
		<table width="729" class="dataTable">
			<TR><TD class=subsubhead>Allowable Money Types for Hardship Withdrawals</TD>
			</TR>
		<TR><TD>
      <DIV class=evenDataRow>	 
      <c:set var="showQnecQmacText" value="false"/>
<c:forEach items="${pifDataForm.planInfoVO.withdrawals.allowedMoneyTypesForHardship}" var="allowableMoneyType" varStatus="count" >
<c:if test="${allowableMoneyType.selectedMoneyType ==true}">
			  <div class="data">
			  <c:if test="${pifDataForm.confirmMode}"> <c:set var="disabledFlag" value="true"/> </c:if>
			  <c:if test="${!pifDataForm.confirmMode}"> <c:set var="disabledFlag" value="false"/> </c:if>
				<form:checkbox	id="pifDataUi_planInfoVO_withdrawals_allowedMoneyTypesForHardship[${count.index}]_selectedIndicator"				
					path="planInfoVO.withdrawals.allowedMoneyTypesForHardship[${count.index}].selectedIndicator" 
					value="Y" disabled="${disabledFlag}" />
					
							
					
		<!-- substring money type 'EEAT1-403a' to 'EEAT1'-->
				<c:set var="allowableWithdrawalsMTShortName" value="${fn:trim(allowableMoneyType.moneyTypeShortName)}"/>
				<c:if test="${fn:contains( allowableWithdrawalsMTShortName,pifConstants.EEAT_401A) || fn:contains( allowableWithdrawalsMTShortName,pifConstants.EEAT_403A)}">
					<c:set var="tempStr" value="${fn:split(allowableMoneyType.moneyTypeShortName,'-')}"/>
					<c:set var="allowableWithdrawalsMTShortName" value="${tempStr[0]}"/>
				</c:if>
				
				 <c:if test="${(allowableWithdrawalsMTShortName == 'QMAC') or (allowableWithdrawalsMTShortName == 'QNEC')}">
                    
                    <c:set var="showQnecQmacText" value="true"/>
                  </c:if>
				${allowableMoneyType.moneyTypeLongName}&nbsp;(${allowableWithdrawalsMTShortName})				 
				</div>
				<c:if test="${count.index % 2 == 0}">
					<div class="endDataRowAndClearFloats"></div>
				</c:if>		
				
					</c:if>
</c:forEach>
					<!--   US29087 Update Allowable Money Types for Hardship Withdrawals on TPA PIF Page-->
					<div class="endDataRowAndClearFloats"></div>
					<br/>
        
          <div class="data">
            <content:getAttribute beanName="hardshipWithdrawalQmacQnecMoneyTypeText" attribute="text"/>
          </div>
        
        <!--   US29087 Update Allowable Money Types for Hardship Withdrawals on TPA PIF Page-->
     	  </DIV>
	  </td></tr>
	  </table><!-- forms of distribution -->
		<table width="729" class="dataTable">
			<TR><TD class=subsubhead>Forms of Distribution</TD></TR>
		</table>  	  
      <DIV class=evenDataRow>
      <TABLE class=dataTable>
        <TBODY>
        <TR>
          <TD style="BORDER-LEFT-WIDTH: 0px; class: " dataColumn?>
			<script type="text/javascript">
			$(document).ready(function() {
				lumpSumIndicatorId= "#planInfoVO_withdrawals_withdrawalDistributionMethod_lumpSumIndicator";
				<c:if test="${disableFields}">
				$(lumpSumIndicatorId).prop("disabled", true); 
				</c:if>

				$(lumpSumIndicatorId).on("click", function() {
				lumpSumIndicatorHiddenId = document.getElementById('withdrawals_lumpSumIndicator');
					if($(lumpSumIndicatorId).is(':checked')){ 
						lumpSumIndicatorHiddenId.value='true';
					}else{
						lumpSumIndicatorHiddenId.value='false';
					}
				});
			});
			</script>	
			<input type="checkbox" 
				id="planInfoVO_withdrawals_withdrawalDistributionMethod_lumpSumIndicator" 
				name="planInfoVO.withdrawals.withdrawalDistributionMethod.lumpSumIndicator" 
				value="true"						
				onclick="setDirtyFlag();"
				<c:if test="${pifDataForm.planInfoVO.withdrawals.withdrawalDistributionMethod.lumpSumIndicator}">checked="checked" </c:if> />Lump sum

<input type="hidden" name="planInfoVO.withdrawals.withdrawalDistributionMethod.lumpSumIndicator" id="withdrawals_lumpSumIndicator" /><%--  input - name="pifDataForm" --%>
			<script type="text/javascript">
			$(document).ready(function() {
				installmentsIndicatorId= "#planInfoVO_withdrawals_withdrawalDistributionMethod_installmentsIndicator";
				<c:if test="${disableFields}">
				$(installmentsIndicatorId).prop("disabled", true); 
				</c:if>

				$(installmentsIndicatorId).on("click", function() {
				installmentsIndicatorHiddenId = document.getElementById('withdrawals_installmentsIndicator');
					if($(installmentsIndicatorId).is(':checked')){ 
						installmentsIndicatorHiddenId.value='true';
					}else{
						installmentsIndicatorHiddenId.value='false';
					}
				});
			});
			</script>	
			<input type="checkbox" 
				id="planInfoVO_withdrawals_withdrawalDistributionMethod_installmentsIndicator" 
				name="planInfoVO.withdrawals.withdrawalDistributionMethod.installmentsIndicator" 
				value="true"						
				onclick="setDirtyFlag();"
				<c:if test="${pifDataForm.planInfoVO.withdrawals.withdrawalDistributionMethod.installmentsIndicator}">checked="checked" </c:if> />Installments
<form:hidden path="planInfoVO.withdrawals.withdrawalDistributionMethod.installmentsIndicator" id="withdrawals_installmentsIndicator" /><%--  input - name="pifDataForm" --%>

			<script type="text/javascript">
			$(document).ready(function() {
				annuityIndicatorId= "#planInfoVO_withdrawals_withdrawalDistributionMethod_annuityIndicator";
				<c:if test="${disableFields}">
				$(annuityIndicatorId).prop("disabled", true); 
				</c:if>

				$(annuityIndicatorId).on("click", function() {
				annuityIndicatorHiddenId = document.getElementById('withdrawals_annuityIndicator');
					if($(annuityIndicatorId).is(':checked')){ 
						annuityIndicatorHiddenId.value='true';
					}else{
						annuityIndicatorHiddenId.value='false';
					}
				});
			});
			</script>	
			<input type="checkbox" 
				id="planInfoVO_withdrawals_withdrawalDistributionMethod_annuityIndicator" 
				name="planInfoVO.withdrawals.withdrawalDistributionMethod.annuityIndicator" 
				value="true"						
				onclick="setDirtyFlag();"
				<c:if test="${pifDataForm.planInfoVO.withdrawals.withdrawalDistributionMethod.annuityIndicator}">checked="checked" </c:if> />Annuity
<form:hidden path="planInfoVO.withdrawals.withdrawalDistributionMethod.annuityIndicator" id="withdrawals_annuityIndicator" /><%--  input - name="pifDataForm" --%>

                
         <!-- partial withdrawal -->              
              <script type="text/javascript">
                    $(document).ready(function() {
                        partialWithdrawalId = "#planInfoVO_withdrawals_withdrawalDistributionMethod_partialWithdrawalIndicator";
						<c:if test="${disableFields}">
						$(partialWithdrawalId).prop("disabled", true); 
						</c:if>						
                        var minWithdrawalAmtInputId = "#planData_withdrawalDistributionMethod_minWithdrawalAmount";
						var minWithdrawalAmtInputHidden = "#withdrawals_partialWithdrawalAmt";

                        if (! $(partialWithdrawalId).is(":checked")) {
                          $(minWithdrawalAmtInputId).val(""); 
						  $(minWithdrawalAmtInputHidden).val(""); 
                          $(minWithdrawalAmtInputId).prop("disabled", true);
                        }

                        $(partialWithdrawalId).on("click", function() {
						partialWithdrawalHiddenId = document.getElementById('withdrawals_partialWithdrawalIndicator');
                           if ($(this).is(':checked')) { 
                              $(minWithdrawalAmtInputId).prop("disabled", false);
							  partialWithdrawalHiddenId.value='true';
                           } else {
                              $(minWithdrawalAmtInputId).val(""); 
							  $(minWithdrawalAmtInputHidden).val(""); 
                              $(minWithdrawalAmtInputId).prop("disabled", true);
							  partialWithdrawalHiddenId.value='false';
                           }
                      });
                    });
              </script>   
				<input type="checkbox" 
				id="planInfoVO_withdrawals_withdrawalDistributionMethod_partialWithdrawalIndicator" 
				name="planInfoVO.withdrawals.withdrawalDistributionMethod.partialWithdrawalIndicator" 
				value="true"						
				onclick="setDirtyFlag();"
				<c:if test="${pifDataForm.planInfoVO.withdrawals.withdrawalDistributionMethod.partialWithdrawalIndicator}">checked="checked" </c:if> />

<input type="hidden" name="planInfoVO.withdrawals.withdrawalDistributionMethod.partialWithdrawalIndicator" id="withdrawals_partialWithdrawalIndicator" /><%--  input - name="pifDataForm" --%>
         <!-- minimum withdrawal amount -->
                <c:choose>
                      <c:when test="${pifDataForm.editMode or pifDataForm.confirmMode}"> 
                        Partial withdrawal, minimum $
<form:input id="planData_withdrawalDistributionMethod_minWithdrawalAmount"
	                             path="pifDataUi.partialWithdrawalMinimumWithdrawalAmount"
	                             onchange="setDirtyFlag(); validatePartialWithdrawalMinimumAmount(this);"	                             
	                             disabled="${disableFields}"
                                 cssClass="numericInput" 
	                             size="8"/>
						
							<input type="hidden" name="pifDataForm.pifDataUi.partialWithdrawalMinimumWithdrawalAmount" id="withdrawals_partialWithdrawalAmt"/>

	                  </c:when>
                      <c:otherwise>
  	                    <c:choose>
		                  <c:when test="${empty pifDataForm.pifDataUi.partialWithdrawalMinimumWithdrawalAmount or pifDataForm.planInfoVO.withdrawals.withdrawalDistributionMethod.minWithdrawalAmount == 0}">
		                    Partial withdrawal
		                  </c:when>
		                  <c:otherwise>
		                    Partial withdrawal, minimum
		                    <fmt:formatNumber type="CURRENCY" minFractionDigits="0" maxFractionDigits="0" value="${pifDataForm.planInfoVO.withdrawals.withdrawalDistributionMethod.minWithdrawalAmount}"/>
		                  </c:otherwise>
		                </c:choose>
                      </c:otherwise>
                </c:choose> 
			<script type="text/javascript">
			$(document).ready(function() {
				otherIndicatorId= "#planInfoVO_withdrawals_withdrawalDistributionMethod_otherIndicator";
				<c:if test="${disableFields}">
				$(otherIndicatorId).prop("disabled", true); 
				</c:if>

				$(otherIndicatorId).on("click", function() {
				otherIndicatorHiddenId = document.getElementById('withdrawals_otherIndicator');
					if($(otherIndicatorId).is(':checked')){ 
						otherIndicatorHiddenId.value='true';
					}else{
						otherIndicatorHiddenId.value='false';
					}
				});
			});
			</script>	
			<input type="checkbox" 
				id="planInfoVO_withdrawals_withdrawalDistributionMethod_otherIndicator" 
				name="planInfoVO.withdrawals.withdrawalDistributionMethod.otherIndicator" 
				value="true"						
				onclick="setDirtyFlag();"
				<c:if test="${pifDataForm.planInfoVO.withdrawals.withdrawalDistributionMethod.otherIndicator}">checked="checked" </c:if> />Other
<form:hidden path="planInfoVO.withdrawals.withdrawalDistributionMethod.otherIndicator" id="withdrawals_otherIndicator" /><%--  input - name="pifDataForm" --%>

             <br><br><content:getAttribute id="applyFeeInformation404a5DisclosurePurposeForWithdrawals" attribute="text"/>    
			</TD>
			</TR>
			</TBODY></TABLE>
		</DIV>
	</DIV>
<!--end table content -->
</BODY></HTML>
</div>
