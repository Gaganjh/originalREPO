<%-- Withdrawal 1099R Section R/O JSP Fragment

NOTE: This fragment is specific to each payment recipient and requires the
current recipient bean to be set in the request scope.

@param withdrawalRequestUi - Request scoped WithdrawalRequestUi bean
@param withdrawalRequest - Request scoped WithdrawalRequest bean ( = withdrawalRequestUi.getWithdrawalRequest())
@param recipient - Request scoped bean of type WithdrawalRequestRecipient
@param recipientStatus - Request scoped bean containing the recipient iteration status 
@param recipient Request scoped bean of type WithdrawalRequestRecipient matching the current recipient status
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_1099R_SECTION_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="tax1099rSectionTitle"/>

<%-- Do not show this section if the paymentTo value is Trustee --%>
<c:if test="${withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE}">

<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
  <tr>
    <td class="tableheadTD1">
      <div style="padding-top:5px;padding-bottom:5px">
        <b><content:getAttribute attribute="text" beanName="tax1099rSectionTitle" /></b>
      </div>
    </td>
    <td class="tablehead" style="text-align:right" nowrap>
      &nbsp;
    </td>
  </tr>
</table>

<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
  <tr>
    <td class="boxborder" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    <td>
    
      <table border="0" cellpadding="0" cellspacing="0" width="498">
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>Name</strong></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn"><c:out value="${recipient.firstName}"/>&nbsp;<c:out value="${recipient.lastName}"/></td>
        </tr>
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>Address line 1</strong><span class="highlightBold"></span></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn"><c:out value="${recipient.address.addressLine1}"/></td>
        </tr>
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>Address line 2</strong></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn"><c:out value="${recipient.address.addressLine2}"/></td>
        </tr>
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>City</strong><span class="highlightBold"></span></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn"><c:out value="${recipient.address.city}"/></td>
        </tr>
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>State</strong><span class="highlightBold"></span></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn"><c:out value="${recipient.address.stateCode}"/></td>
        </tr>
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>ZIP Code</strong><span class="highlightBold"></span></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn">
            <c:choose>
              <c:when test="${not empty recipient.address.zipCode1 && not empty recipient.address.zipCode2 
              			&& recipient.address.countryCode == 'USA'}" >
                <c:out value="${recipient.address.zipCode1}"/>-<c:out value="${recipient.address.zipCode2}"/>
              </c:when>
              <c:otherwise>
                <c:out value="${recipient.address.zipCode}"/>
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn"><strong>Country</strong><span class="highlightBold"></span></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn">
            <ps:displayDescription collection="${countries}" keyName="value" keyValue="label" 
              key="${recipient.address.countryCode}" />
          </td>
        </tr>

  <c:if test="${recipientUi.showParticipantUsCitizenRow}">
    <c:if test="${recipientUi.nonUsPayeeExists}">
      <c:if test="${not withdrawalForm.withdrawalRequestUi.withdrawalRequest.wmsiOrPenchecksSelected}">
          <tr class="datacell1" valign="top">
            <td class="sectionNameColumn"><strong>Is participant a U.S. citizen?</strong></td>
            <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
            <td class="indentedValueColumn">
              <c:choose>
                <c:when test="${recipient.usCitizenInd == true}">Yes</c:when>
                <c:when test="${recipient.usCitizenInd == false}">No</c:when>
                <c:when test="${empty recipient.usCitizenInd}">&nbsp;</c:when>
              </c:choose>
            </td>
          </tr>
      </c:if>        
    </c:if>
  </c:if>
        
        <tr class="datacell1" valign="top">
          <td colspan="3" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>
  <c:if test="${withdrawalRequestUi.showStaticContent}">
        <tr>
          <td colspan="3">&nbsp;<content:getAttribute beanName="step2PageBean" attribute="body2"/></td>
        </tr>
  </c:if>
      </table>
    </td>
    <td width="2" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
  </tr>
  <tr>
    <td colspan="3">
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
    </td>
  </tr>
</table>


</c:if>