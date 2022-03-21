<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="uiConstants" className="com.manulife.pension.ps.web.contract.PlanDataUi" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_PLAN_OTHER_PLAN_INFORMATION}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="otherPlanInformationText"/>

  
<content:contentBean 
	contentId="80730" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="qdiaNotificationMessage" />  
	
	
	  
<script type="text/javascript">

function textCounter( field, maxlimit ) {
  var temp = field.value;
  temp = temp.replace(/[\n\r\n]+/g, '  ');
  var nChar = temp.replace(/[\t]+/g, '').length;
  if ( nChar >= maxlimit )
  {
    field.value = temp.substring( 0, maxlimit );
    //alert( 'Textarea value can only be '+maxlimit+' characters in length.' );
    return false;
  }
}

function onQdiaRestrictionImposedChanged() {
  <c:choose>
    <c:when test="${pifDataForm.editMode}"> 
    var value = $("input[name='planInfoVO.otherInformation.qdiaRestrictionImposed']:checked").val();
    if (value == "${planDataConstants.YES_CODE}") {
      $("textarea[name='planInfoVO.otherInformation.qdiaRestrictionDetails']").prop("disabled", false);
    } else {
      $("textarea[name='planInfoVO.otherInformation.qdiaRestrictionDetails']").val('');
      $("textarea[name='planInfoVO.otherInformation.qdiaRestrictionDetails']").prop('disabled', true);
    }
    </c:when>
    <c:otherwise>
      <c:if test="${pifDataForm.planInfoVO.otherInformation.qdiaRestrictionImposed != planDataConstants.YES_CODE}"> 
      $("#qdiaRestrictionDetailsDiv").hide();
      </c:if>
    </c:otherwise>
  </c:choose>
}

$(document).ready(function() {
  onQdiaRestrictionImposedChanged();
});

</script>  

<div id="otherPlanInformationTabDivId" class="borderedDataBox">
<HTML><BODY>
<SCRIPT language=JavaScript1.2 type=text/javascript>
<!--

var submitted=false;

function setButtonAndSubmit(button) {
	 
}

-->
</SCRIPT>

<!--start table content -->
	<table width="729" class="dataTable">
		<TR><TD class=subhead>	
			<DIV class=sectionTitle>
				<c:if test="${pifDataForm.confirmMode}">
					<content:getAttribute beanName="otherPlanInformationText" attribute="text"/>
				</c:if>
			</DIV>
		</TD></TR>
	</table> 	  
      <DIV id=otherPlanInformationDataDivId>
      <DIV class=evenDataRow>
      <TABLE width="729" class=dataTable>
        <TBODY>
        <TR>
          <TD class=eligibilityAndParticipationLabelColumn>Does the plan provide for permitted disparity?</TD>
          <TD class=dataColumn>
			<c:choose>
			  <c:when test="${pifDataForm.editMode}">	
<form:radiobutton disabled="${disableFields}" path="planInfoVO.otherInformation.permittedDisparityProvided" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}


<form:radiobutton disabled="${disableFields}" path="planInfoVO.otherInformation.permittedDisparityProvided" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}


			  </c:when>
			  <c:otherwise>								
<c:if test="${pifDataForm.planInfoVO.otherInformation.permittedDisparityProvided == 'Y'}">Yes</c:if>

<c:if test="${pifDataForm.planInfoVO.otherInformation.permittedDisparityProvided == 'N'}">No</c:if>

			  </c:otherwise>
			</c:choose>							
		  </TD></TR></TBODY></TABLE>
	  </DIV>
	  
		<table width="729" class="dataTable">
			<TR><TD class=subsubhead><content:getAttribute  beanName="qdiaNotificationMessage" attribute="text"/></TD></TR>
		</table>  	  
      <DIV class=oddDataRow>
      <TABLE width="729" class=dataTable>
        <TBODY>
        <TR>
          <TD class=eligibilityAndParticipationLabelColumn>Does the plan permit investments in individual securities? </TD>
          <TD class=dataColumn>
                <c:choose>
                  <c:when test="${pifDataForm.editMode}">                           
<form:radiobutton disabled="${disableFields}" path="planInfoVO.otherInformation.individualSecuritiesInvestmentAllowed" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}


<form:radiobutton disabled="${disableFields}" path="planInfoVO.otherInformation.individualSecuritiesInvestmentAllowed" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}


                  </c:when>
                  <c:otherwise>
<c:if test="${pifDataForm.planInfoVO.otherInformation.individualSecuritiesInvestmentAllowed == 'Y'}">Yes</c:if>

<c:if test="${pifDataForm.planInfoVO.otherInformation.individualSecuritiesInvestmentAllowed == 'N'}">No</c:if>

                  </c:otherwise>
                </c:choose>
		  </TD></TR></TBODY></TABLE></DIV>
      <DIV class=evenDataRow>
      <TABLE width="729" class=dataTable>
        <TBODY>
        <TR>
          <TD class=eligibilityAndParticipationLabelColumn vAlign=top>
                  Does the plan impose any restrictions, fees or expenses (e.g. surrender charge,
                  exchange fee, or redemption fee) on transferring or withdrawing from the QDIA after the initial 90-day period
                  (as per QDIA regulations S2550.404c-5(c)(5)(iii))?		  
		  </TD>
          <TD class=dataColumn vAlign=top>
                <c:choose>
                  <c:when test="${pifDataForm.editMode}">                           
<form:radiobutton disabled="${disableFields}" onclick="onQdiaRestrictionImposedChanged()" path="planInfoVO.otherInformation.qdiaRestrictionImposed" value="${planDataConstants.YES_CODE}"/>${uiConstants.YES}



<form:radiobutton disabled="${disableFields}" onclick="onQdiaRestrictionImposedChanged()" path="planInfoVO.otherInformation.qdiaRestrictionImposed" value="${planDataConstants.NO_CODE}"/>${uiConstants.NO}



                  </c:when>
                  <c:otherwise>
<c:if test="${pifDataForm.planInfoVO.otherInformation.qdiaRestrictionImposed == 'Y'}">Yes</c:if>

<c:if test="${pifDataForm.planInfoVO.otherInformation.qdiaRestrictionImposed =='N'}">No</c:if>

                  </c:otherwise>
                </c:choose>
		  </TD></TR></TBODY></TABLE></DIV>

      <TABLE width="729" class=dataTable>
        <TBODY>
        <TR>
          <TD class=eligibilityAndParticipationLabelColumn vAlign=top>	
                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;If yes, specify the restrictions, fees and/or expenses. This <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;description will appear on initial QDIA notifications.
		  </TD>
          <TD class=dataColumn vAlign=top>      
		  <DIV id=qdiaRestrictionDetailsDiv class=oddDataRow>
<form:textarea rows="5" cols="40" path="planInfoVO.otherInformation.qdiaRestrictionDetails" disabled="${disableFields}"  onchange="textCounter(this,3500);" />

		  </DIV>
          </TD></TR></TBODY></TABLE>
		  
	</DIV>
					
<!--end table content -->
</BODY></HTML>
</div>
