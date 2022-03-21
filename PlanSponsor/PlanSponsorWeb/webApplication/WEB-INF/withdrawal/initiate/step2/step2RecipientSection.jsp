<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_1099R_SECTION_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="recipientSectionTitle"/>

<div style="padding-top:10px;padding-bottom:10px;">
   <table class="box" border="0" cellpadding="0" cellspacing="0" width="100%">
     <tr>
      <td class="tableheadTD1" colspan="3">
      	<div style="padding-top:5px;padding-bottom:5px">
      		<span style="padding-right:2px" id="recipientShowIcon" onclick="showRecipientSection();">
      			<img src="/assets/unmanaged/images/plus_icon.gif" width="12" height="12" border="0">
      		</span>
      		<span style="padding-right:2px;padding-top:3px" id="recipientHideIcon" onclick="hideRecipientSection();">
      			<img src="/assets/unmanaged/images/minus_icon.gif" width="12" height="12" border="0">
      		</span>
      		<b><content:getAttribute beanName="recipientSectionTitle" attribute="text"/></b>
      	</div>
      </td>
     </tr>
     <tr>
       <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
       <td>
         <table border="0" cellpadding="0" cellspacing="0" width="100%" id="recipientTable">
           <tr class="datacell1" valign="top">
             <td class="sectionNameColumn"><strong>Name</strong></td>
             <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
             <td class="indentedValueColumn"><c:out value="${recipient.participantName}"/></td>
           </tr>
           <tr class="datacell1" valign="top">
             <td class="sectionNameColumn">
               <strong>
                 <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].addressUi.address.addressLine1" singleDisplay="true"/>
                 Address line 1
               </strong>
             </td>
             <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
             <td class="indentedValueColumn">
<form:input path="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.address.addressLine1" maxlength="30" onchange="return handleRecipientAddressLine1Changed(${recipientIndex});" cssClass="mandatory" id="recipientAddressLine1Id[${recipientIndex}]"/>




             </td>
           </tr>
           <tr class="datacell1" valign="top">
             <td class="sectionNameColumn">
               <strong>
                 <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].addressUi.address.addressLine2" singleDisplay="true"/>
                 Address line 2
               </strong>
             </td>
             <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
             <td class="indentedValueColumn">
<form:input path="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.address.addressLine2" maxlength="30" onchange="return handleRecipientAddressLine2Changed(${recipientIndex});" id="recipientAddressLine2Id[${recipientIndex}]"/>



             </td>
           </tr>
           <tr class="datacell1" valign="top">
             <td class="sectionNameColumn">
             	<strong>
             		<ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].addressUi.address.city" singleDisplay="true"/>
             		City
             	</strong>
             </td>
             <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
             <td class="indentedValueColumn">
<form:input path="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.address.city" maxlength="25" onchange="return handleRecipientCityChanged(${recipientIndex});" cssClass="mandatory" id="recipientCityId[${recipientIndex}]"/>




             </td>
           </tr>
           <tr class="datacell1" valign="top">
             <td class="sectionNameColumn">
               <strong>
                 <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].addressUi.address.stateCode" singleDisplay="true"/>
                 State
               </strong>
             </td>
             <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
             <td class="indentedValueColumn">
               <span id="recipientStateDropdownSpanId[${recipientIndex}]">
 <form:select path="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.address.stateCode" cssClass="mandatory" id="recipientStateDropdownId[${recipientIndex}]" onchange="return handleRecipientStateDropdownChanged(${recipientIndex});">



                   <form:option value="" >- select -</form:option>
                   <form:options items="${states}" itemValue="code" itemLabel="code"/>
</form:select>
               </span>
               <span id="recipientStateInputSpanId[${recipientIndex}]">
<form:input path="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.address.stateCode" maxlength="2" onchange="return handleRecipientStateInputChanged(${recipientIndex});" cssClass="mandatory" id="recipientStateInputId[${recipientIndex}]"/>




               </span>
             </td>
           </tr>
           <tr class="datacell1" valign="top">
             <td class="sectionNameColumn">
               <strong>
                 <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].addressUi.address.zipCode" singleDisplay="true"/>
                 Zip Code
               </strong>
             </td>
             <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
             <td class="indentedValueColumn">
               <span id="recipientZipSingleSpanId[${recipientIndex}]">
<form:input path="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.address.zipCode" maxlength="9" onchange="return handleRecipientZipCodeChanged(${recipientIndex});" cssClass="mandatory" id="recipientZipCodeId[${recipientIndex}]"/>




               </span>
               <span id="recipientZipDoubleSpanId[${recipientIndex}]">
<form:input path="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.address.zipCode1" maxlength="5" onchange="return handleRecipientZipCode1Changed(${recipientIndex});" size="5" cssClass="mandatory" id="recipientZipCode1Id[${recipientIndex}]"/>





                 &ndash;
<form:input path="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.address.zipCode2" maxlength="4" onchange="return handleRecipientZipCode2Changed(${recipientIndex});" size="4" id="recipientZipCode2Id[${recipientIndex}]"/>




               </span>
             </td>
           </tr>
           <tr class="datacell1" valign="top">
             <td class="sectionNameColumn">
               <strong>
                 <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].addressUi.address.countryCode" singleDisplay="true"/>
                 Country
               </strong>
             </td>
             <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
             <td class="indentedValueColumn">
 <form:select path="withdrawalRequestUi.recipients[${recipientIndex}].withdrawalRequestRecipient.address.countryCode" cssClass="mandatory" id="recipientCountryId[${recipientIndex}]" onchange="return handleRecipientCountryChanged(${recipientIndex});">



                  <form:option value="">- select -</form:option>
                 <form:options items="${countries}" itemValue="value" itemLabel="label"/>
</form:select>
              </td>
           </tr>
           <c:if test="${withdrawalForm.withdrawalRequestUi.recipients[recipientIndex].showParticipantUsCitizenRow}">
             <c:if test="${not withdrawalForm.withdrawalRequestUi.withdrawalRequest.wmsiOrPenchecksSelected}">
               <tr class="datacell1" valign="top">
                 <td>
                   <span id="participantUsCitizenCol1Id[${recipientIndex}]" class="sectionNameColumn" style="padding-left: ${isIE ? '2' : '4'}px;">
                     <strong>
                       <ps:fieldHilight name="withdrawalRequestUi.recipients[${recipientIndex}].usCitizenInd" singleDisplay="true"/>
                       Is participant a U.S. citizen?
                     </strong>
                   </span>
                 </td>
                 <td class="datadivider">
                   <span id="participantUsCitizenCol2Id[${recipientIndex}]">
                     <img src="/assets/unmanaged/images/s.gif" height="1" width="1">
                   </span>
                 </td>
                 <td>
                   <span id="participantUsCitizenCol3Id[${recipientIndex}]" class="valueColumn" style="padding-left: ${isIE ? '0' : '2'}px;">
<form:radiobutton onchange="onFieldChange(this);" path="withdrawalRequestUi.recipients[${recipientIndex}].usCitizenInd" cssClass="${(withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}" value="true">Yes</form:radiobutton>




<form:radiobutton onchange="onFieldChange(this);" path="withdrawalRequestUi.recipients[${recipientIndex}].usCitizenInd" cssClass="${(withdrawalForm.withdrawalRequestUi.mandatoryForBundledContract) ? 'mandatory' : ''}" value="false">No</form:radiobutton>




                   </span>
                 </td>
               </tr>
             </c:if>
           </c:if>
           <tr>
             <td colspan="3" class="indentedValue"><content:getAttribute beanName="layoutPageBean" attribute="body2"/></td>
           </tr>
         </table>
       </td>
       <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
     </tr>
     <tr>
       <td colspan="3">
         <div id="recipientFooter">
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
