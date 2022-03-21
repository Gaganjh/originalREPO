<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="recipientConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient" />
<un:useConstants var="activityConstants" className="com.manulife.pension.service.withdrawal.helper.WithdrawalFieldDef" />

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_1_PERSONAL_INFORMATION}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="participantInformationSectionTitle"/>
<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_1_EE_SNAPSHOT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="employeeSnapshotText"/>
<content:contentBean type="LayoutPage" 
  contentId="${contentConstants.LAYOUT_PAGE_WITHDRAWALS_STEP_1}" 
  beanName="step1PageBean" /> 
  <content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_SPOUSAL_CONSENT_MAY_BE_REQUIRED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="spousalConsentMayBeRequiredText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_SPOUSAL_CONSENT_MAY_BE_REQUIRED_IF_MARRIED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="spousalConsentMayBeRequiredIsMarriedText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_SPOUSAL_CONSENT_MUST_BE_OBTAINED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="spousalConsentMustBeObtainedText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_SPOUSAL_CONSENT_IS_NOT_REQUIRED}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="spousalConsentIsNotRequiredText"/>
  
<script type="text/javascript" src="/assets/unmanaged/javascript/jquery-3.6.0.min.js"></script>
<script language="JavaScript1.2" type="text/javascript">

function setSpousalConsentText(legallyMarriedInd) {
	  $("#spousalConsentText").text('');
	  if (legallyMarriedInd == 'N') {
	    $("#spousalConsentText").text('<content:getAttribute beanName="spousalConsentIsNotRequiredText" escapeJavaScript="true" attribute="text"/>');
	  } else if (legallyMarriedInd == 'Y') {
	    <c:if test="${empty withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.spousalConsentRequired}">
	    $("#spousalConsentText").text('<content:getAttribute beanName="spousalConsentMayBeRequiredIsMarriedText" escapeJavaScript="true" attribute="text"/>');
	    </c:if>
	    <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.spousalConsentRequired eq true}">
	    $("#spousalConsentText").text('<content:getAttribute beanName="spousalConsentMustBeObtainedText" escapeJavaScript="true" attribute="text"/>');
	    </c:if>
	  } else {
	    <c:if test="${empty withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.spousalConsentRequired or withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.spousalConsentRequired eq true}">
	    $("#spousalConsentText").text('<content:getAttribute beanName="spousalConsentMayBeRequiredText" escapeJavaScript="true" attribute="text"/>');
	    </c:if>
	  }
	}

	$(document).ready(function() {
	  setSpousalConsentText("${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.legallyMarriedInd)}");
	});
</script>

<div style="padding-top:10px;padding-bottom:10px;">
  <table class="box" border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
      <td colspan="3">
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
       	  <tr>
            <td class="tableheadTD1">
             	<div style="padding-top:5px;padding-bottom:5px">
             		<span style="padding-right:2px" id="participantInformationShowIcon" onclick="showParticipantInformationSection();">
             			<img src="/assets/unmanaged/images/plus_icon.gif" width="12" height="12" border="0">
             		</span>
             		<span style="padding-right:2px" id="participantInformationHideIcon" onclick="hideParticipantInformationSection();">
             			<img src="/assets/unmanaged/images/minus_icon.gif" width="12" height="12" border="0">
             		</span>
             		<b><content:getAttribute beanName="participantInformationSectionTitle" attribute="text"/></b>
             	</div>
            </td>
      			<td class="tablehead" style="text-align:right" nowrap>
      				<b>
      					<a href="javascript:doEmployeeSnapshot(${withdrawalForm.withdrawalRequestUi.employeeProfileId})">
      						<content:getAttribute beanName="employeeSnapshotText" attribute="text"/>
      					</a>
      				</b>
      			</td>
         	</tr>
        </table>
      </td>
    </tr>
    <tr>
      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td>
        <table border="0" cellpadding="0" cellspacing="0" width="100%" id="participantInformationTable">
          <tr class="datacell1" valign="top">
	           <td class="sectionNameColumn"><strong>Name</strong></td>
	           <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	           <td class="indentedValueColumn">
	           	 ${withdrawalForm.withdrawalRequestUi.withdrawalRequest.firstName}&nbsp;
	           	 ${withdrawalForm.withdrawalRequestUi.withdrawalRequest.lastName}
	           </td>
	        </tr>
          <tr class="datacell1" valign="top">
	           <td class="sectionNameColumn"><strong>SSN</strong></td>
	           <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	           <td class="indentedValueColumn">
	           	 <render:ssn useMask="false">
	           	 	 ${withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantSSN}
	           	 </render:ssn>
	           </td>
	        </tr>
          <tr class="datacell1" valign="top">
	           <td class="sectionNameColumn"><strong>Contract number</strong></td>
	           <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	           <td class="indentedValueColumn">
	             ${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractId}
	           </td>
	        </tr>
          <tr class="datacell1" valign="top">
	           <td class="sectionNameColumn"><strong>Contract name</strong></td>
	           <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	           <td class="indentedValueColumn">
	           	 ${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractName}
	           </td>
	        </tr>
          <tr class="datacell1" valign="top">
				    <td class="sectionNameColumn">
					    <strong>
					    	State of residence
					    </strong>
				    </td>
				    <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
				    <td class="indentedValueColumn">
					    	<ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.stateOfResidence" singleDisplay="true">
    					    	<ps:activityHistory itemNumber="${activityConstants.STATE_OF_RESIDENCE.id}"/>
			                </ps:fieldHilight>
			          <c:choose>
			          	  <c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly}">
			          	  <form:hidden path="withdrawalRequestUi.withdrawalRequest.participantStateOfResidence" id="stateOfResidenceId"/>


				                
			          	   <c:choose>
				              <c:when test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence eq 'ZZ'}">
				                ZZ&nbsp;Outside US
				              </c:when>
				              <c:otherwise>
				                ${e:forHtmlContent(withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence)} &nbsp;
				                <ps:displayDescription collection="${statesWithoutMilitary}" keyName="code" keyValue="description" 
				                  key="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.participantStateOfResidence}"/>
				              </c:otherwise>
            				</c:choose>
			          	  </c:when>
          				 <c:otherwise>
 <form:select cssClass="mandatory" id="stateOfResidenceId" path="withdrawalRequestUi.withdrawalRequest.participantStateOfResidence" onchange="return handleStateOfResidenceChanged();">



                <form:option value="">- select -</form:option>
								<c:forEach items="${statesWithoutMilitary}"
				         					 var="state">
	                <form:option value="${state.code}" >${state.code}&nbsp;${state.description}</form:option>
     					  </c:forEach>
                <form:option value="${recipientConstants.STATE_OF_RESIDENCE_OUTSIDE_US}" >${recipientConstants.STATE_OF_RESIDENCE_OUTSIDE_US}&nbsp;Outside USA</form:option>
</form:select>
						</c:otherwise>
					</c:choose>
				    </td>
					</tr>
          <tr class="datacell1" valign="top">
					   <td class="sectionNameColumn">
					   	 <strong>
					   	 	 Date of birth
					   	 </strong>
					   </td>
	           <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
					   <td class="indentedValueColumn">
					   		<ps:fieldHilight name="withdrawalRequestUi.birthDate" singleDisplay="true">
						   	    <ps:activityHistory itemNumber="${activityConstants.DATE_OF_BIRTH.id}"/>
						   	 </ps:fieldHilight>
			          	  	<c:choose>
			          	  		<c:when test="${withdrawalForm.withdrawalRequestUi.isParticipantInitiated or withdrawalForm.withdrawalRequestUi.viewOnly}">
			          	  			
			          	  			<fmt:formatDate value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.birthDate}" 
			          	  					type="DATE" pattern="MM/dd/yyyy"/>
			          	  		</c:when>
			          	  		<c:otherwise>
<form:input path="withdrawalRequestUi.birthDate"  onclick=" onclickLookForBirthDateChange();" onchange="return handleBirthDateChanged();" cssClass="mandatory" id="birthDateId"/>



               						<img onclick=" handleDateIconClicked(event, 'birthDateId', true, false);lookForBirthDateChange(); " onchange=" lookForBirthDateChange();" src="/assets/unmanaged/images/cal.gif" border="0">
               						(mm/dd/yyyy)
			          	  		</c:otherwise>
			          	  	</c:choose>

						 </td>
					</tr>
					
				<c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.spousalConsentRequired ne false}">
           <tr class="datacell1" valign="top">
             <td class="sectionNameColumn">
               <strong>
                 <ps:fieldHilight name="withdrawalRequestUi.legallyMarriedInd" singleDisplay="true" />
                 Legally Married
               </strong><br><span id="spousalConsentText"></span>
             </td>
             <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
             <td class="indentedValueColumn" style="vertical-align:top;">
                <c:choose>
             <c:when test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.spousalConsentRequired ne false}">
	               
<form:radiobutton onclick="setSpousalConsentText('Y')" path="withdrawalRequestUi.legallyMarriedInd" value="Y"/>Yes



<form:radiobutton onclick="setSpousalConsentText('N')" path="withdrawalRequestUi.legallyMarriedInd" value="N"/>No



			   </c:when>
			   <c:otherwise>
                 <c:if test="${not empty withdrawalForm.withdrawalRequestUi.withdrawalRequest.legallyMarriedInd}">
                   <c:out value="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.legallyMarriedInd eq 'Y' ? 'Yes' : 'No'}"/>
                 </c:if> 
			   </c:otherwise>
			 </c:choose>
             </td>
          </tr>
          </c:if>	
					
          <tr class="datacell1" valign="top">
            <td colspan="3" class="indentedValue">
              <content:getAttribute beanName="step1PageBean" attribute="body1"/>
            </td>
          </tr>
        </table>
      </td>
      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    <tr>
      <td colspan="3">
        <div id="participantInformationFooter">
          <table border="0" cellpadding="0" cellspacing="0" width="100%">
            <tr>
              <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="4" width="1"></td>
              <td><img src="/assets/unmanaged/images/s.gif" height="4" width="1"></td>
              <td rowspan="2" width="5"><img src="/assets/unmanaged/images/box_lr_corner.gif" height="5" width="5"></td>
            </tr>
            <tr>
              <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            </tr>
          </table>
        </div>
      </td>
    </tr>
  </table>
</div>