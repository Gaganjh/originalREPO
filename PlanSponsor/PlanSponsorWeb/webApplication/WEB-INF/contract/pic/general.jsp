<%@page buffer="none" autoFlush="true" isErrorPage="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants scope="request" var="renderConstants" className="com.manulife.util.render.RenderConstants" />
<un:useConstants var="uiConstants" className="com.manulife.pension.ps.web.pif.PIFDataUi" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean contentId="${contentConstants.MISCELLANEOUS_PLAN_HEADING_GENERAL}" 
	type="${contentConstants.TYPE_MISCELLANEOUS}"  id="generalText"/>
	
<content:contentBean contentId="${contentConstants.MISCELLANEOUS_PLAN_YEAREND_TXT_GENERAL}" 
	type="${contentConstants.TYPE_MISCELLANEOUS}"  id="pyeText"/>
	
	
 
<c:set scope="request" var="datePattern" value="${renderConstants.MEDIUM_MDY_SLASHED}"/>
<c:set scope="request" var="datePatternMD" value="${uiConstants.MEDIUM_MD_SLASHED}"/>
<style type="text/css">
.tpaheader {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 11px;
	width: 729px;
	font-weight: bolder;
	color: #000000;
	padding: 0px;
	background : #CCCCCC;
	clear: both;
	vertical-align: middle;
	border-right:1px solid #CCCCCC;
}
</style>

<div id="generalTabDivId" class="borderedDataBox">
	<!--start table content -->
	<c:if test="${pifDataForm.confirmMode}">
		<table class="tpaheader">
			<TR><td><content:getAttribute beanName="generalText" attribute="text"/></td></TR>
		</table> 
	</c:if>
	<c:if test="${pifDataForm.editMode}">
		<table width="729" class="dataTable">
			<TR><TD class=subhead>	
				<DIV class=sectionTitle>
				</DIV>
			</TD></TR>
		</table>
	</c:if>	
	<TABLE border=0 cellSpacing=0 cellPadding=0 width=729>
    <TR>
    <TD width="100%"><!--[if lt IE 7]>
    <link rel="stylesheet" href="/assets/unmanaged/stylesheet/liquid_IE.css" type="text/css">
	<![endif]-->
      <DIV class=evenDataRow>
      <TABLE class=dataTable>
        <TR>
			<TD class=generalLabelColumn>
				Plan name 
			</TD>
			<TD class=dataColumn>
			<c:choose>
              <c:when test="${pifDataForm.editMode}">
<form:input path="planInfoVO.generalInformations.planName" disabled="${disableFields}" maxlength="${planDataConstants.PLAN_NAME_MAX_LENGTH}" onchange="handlePlanNameChanged(this);" size="30"/>




			  </c:when>
              <c:otherwise>
                ${pifDataForm.planInfoVO.generalInformations.planName}
              </c:otherwise>
            </c:choose>                           
			</TD>
        </TR></TABLE></DIV>
      <DIV class=oddDataRow>
      <TABLE class=dataTable>
        <TR>
          <TD class=generalLabelColumn>
			Employer tax identification number
		  </TD>
          <TD class=dataColumn>
          <c:choose>
              <c:when test="${pifDataForm.editMode}">          
<form:input path="pifDataUi.employerTaxIdentificationNumber" disabled="${disableFields}" maxlength="9" onblur="handleEinBlur(this);" onchange="setDirtyFlag();" size="30"/>





			  </c:when>
              <c:otherwise>
				${pifDataForm.planInfoVO.generalInformations.employerTaxIdentificationNumber}
              </c:otherwise>
            </c:choose>                           
		  </TD>
        </TR></TABLE></DIV>
      <DIV class=evenDataRow>
      <TABLE class=dataTable>
        <TR>
			<TD class=generalLabelColumn>
				IRS plan number 
			</TD>
			<TD class=dataColumn>
			<c:choose>
              <c:when test="${pifDataForm.editMode}">
<form:input path="planInfoVO.generalInformations.irsPlanNumber" disabled="${disableFields}" maxlength="3" onblur="validatePlanNumberTpa(this)" onchange="setDirtyFlag();" size="30"/>





			  </c:when>
              <c:otherwise>
				${pifDataForm.planInfoVO.generalInformations.irsPlanNumber}
              </c:otherwise>
            </c:choose>                             
			</TD>
        </TR></TABLE></DIV>
      <DIV class=oddDataRow>
      <TABLE class=dataTable>
        <TR>
			<TD class=generalLabelColumn>
				Plan effective date 
			</TD>
			<TD class=dataColumn>
			<c:choose>
              <c:when test="${pifDataForm.editMode}">					  
<form:input path="pifDataUi.planEffectiveDate" disabled="${disableFields}" maxlength="10" onblur="validatePlanEffectiveDate(this)" onchange="setDirtyFlag();" id="planEffectiveDateId"/>





                  <img onclick=" return handleDateIconClicked(event, 'planEffectiveDateId');" src="/assets/unmanaged/images/cal.gif" border="0"> (mm&frasl;dd&frasl;yyyy)
			  </c:when>
              <c:otherwise>
              <fmt:formatDate value="${pifDataForm.planInfoVO.generalInformations.planEffectiveDate}" pattern="MM/dd/yyyy" />
			    ${empty pifDataForm.planInfoVO.generalInformations.planEffectiveDate ? '' : '(mm&frasl;dd&frasl;yyyy)'}            
              </c:otherwise>
            </c:choose>
			</TD>
		</TR></TABLE></DIV>
      <DIV class=evenDataRow>
      <TABLE class=dataTable>
        <TR>
			<TD class=generalLabelColumn>
				Plan year end
			</TD>
			<TD class=dataColumn>
			<c:choose>
              <c:when test="${pifDataForm.editMode}">				
<form:input path="pifDataUi.planYearEndString" maxlength="10" onblur="validatePlanEntryDate(this)" onchange="setDirtyFlag();" size="5" id="planYearEndId"/>





			  </c:when>
              <c:otherwise>
				${pifDataForm.planInfoVO.generalInformations.planYearEnd.data}
              </c:otherwise>
            </c:choose>					  
			<c:if test="${not empty pifDataForm.planInfoVO.generalInformations.planYearEnd.data}">
				  (mm&frasl;dd)
			</c:if>
			<c:if test="${pifDataForm.lookupData['SHOW_DEFAULT_PLAN_YEAR_END_TEXT'] eq true}">
			&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;<content:getAttribute beanName="pyeText" attribute="text"/>
			</c:if>
			</TD>
		</TR></TABLE></DIV>
      <DIV class=oddDataRow>
      <TABLE class=dataTable>
        <TR>
            <TD class=generalLabelExtendedColumn>Is there, or has there ever been, a Safe Harbor election in place as per Section 401(k)(12) of the Internal Revenue Code?</TD>
            <TD class=dataColumn>
			<c:choose>
              <c:when test="${pifDataForm.editMode}">				
<form:radiobutton onclick="setDirtyFlag();" path="planInfoVO.generalInformations.isSafeHarborPlan" value="Y"/>Yes


<form:radiobutton onclick="setDirtyFlag();" path="planInfoVO.generalInformations.isSafeHarborPlan" value="N"/>No


              </c:when>
              <c:otherwise>
                ${pifDataForm.planInfoVO.generalInformations.isSafeHarborPlanDisplay}
              </c:otherwise>
            </c:choose>								
			</TD>
		</TR></TABLE></DIV>
      <DIV class=evenDataRow>
      <TABLE class=dataTable>
        <TR>
			<TD class=generalLabelExtendedColumn>
				Does the plan include a 'Qualified Automatic Contribution Arrangement (QACA)'? 
			</TD>
			<TD class=dataColumn>
			<c:choose>
              <c:when test="${pifDataForm.editMode}">				
<form:radiobutton onclick="setDirtyFlag();" path="planInfoVO.generalInformations.isQacaIncluded" id="planDataUi_planData_intendsToMeetIrcQualifiedAutomaticContributionArrangement" value="Y"/>Yes



<form:radiobutton onclick="setDirtyFlag();" path="planInfoVO.generalInformations.isQacaIncluded" value="N"/>No

              </c:when>
              <c:otherwise>
                ${pifDataForm.planInfoVO.generalInformations.isQacaIncludedDisplay}
              </c:otherwise>
            </c:choose>							
			</TD>
        </TR></TABLE></DIV>
      <DIV class=oddDataRow>
      <TABLE class=dataTable>
        <TR>
			<TD class=generalLabelExtendedColumn>
				Does the plan allow for forfeiture reallocations?
			</TD>
			<TD class=dataColumn>
			<c:choose>
              <c:when test="${pifDataForm.editMode}">				
<form:radiobutton onclick="setDirtyFlag();" path="planInfoVO.generalInformations.isForfeitureReallocationAllowed" id="planDataUi_planData_intendsToMeetIrcQualifiedAutomaticContributionArrangement" value="Y"/>Yes

<form:radiobutton onclick="setDirtyFlag();" path="planInfoVO.generalInformations.isForfeitureReallocationAllowed" value="N"/>No

              </c:when>
              <c:otherwise>
                ${pifDataForm.planInfoVO.generalInformations.isForfeitureReallocationAllowedDisplay}
              </c:otherwise>
            </c:choose>								
			</TD></TR></TABLE></DIV>   
          
	</TD></TR></TABLE>
<!--end table content -->
</div>
