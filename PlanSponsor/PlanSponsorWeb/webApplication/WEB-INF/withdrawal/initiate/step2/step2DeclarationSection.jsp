<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>

<un:useConstants var="declarationConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration" />
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_DECLARATION_SECTION_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="declarationSectionTitle"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_DECLARATION_TAX_NOTICE_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="declarationTaxNoticeText"/>
<content:contentBean 
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_DECLARATION_TAX_NOTICE_LINK_PSW}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="declarationTaxNoticeLink"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_DECLARATION_WAITING_PERIOD_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="declarationWaitingPeriodText"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_DECLARATION_IRA_PROVIDER_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="declarationIraProviderText"/>

<div style="padding-top:10px;padding-bottom:10px;">
 <table class="box" border="0" cellpadding="0" cellspacing="0" width="100%">
   <tr class="tablehead">
     <td colspan="3" class="tableheadTD1">
       <div style="padding-bottom: ${isIE ? '1' : '5'}px; padding-top: ${isIE ? '1' : '5'}px;">
         <b><content:getAttribute beanName="declarationSectionTitle" attribute="text"/></b>
       </div>
     </td>
   </tr>
   <tr>
     <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
     <td width="100%">
       <strong>The participant has certified:</strong>
       <br/>
       <table border="0" cellspacing="0" cellpadding="0">
         <tr>
           <td align="right" style="padding-left:4px;">
             <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.taxNoticeDeclaration" singleDisplay="true"/> 
             <form:checkbox path="withdrawalRequestUi.selectedDeclarations"
                            value="${declarationConstants.TAX_NOTICE_TYPE_CODE}"
                            id="taxNoticeDeclarationId"
                            onclick="return handleTaxNoticeDeclarationChanged();"/>
           </td>
           <td align="left">
           <c:choose>
				<c:when test="${withdrawalForm.withdrawalRequestUi.isIRSSpecialTaxNotice}">
					<content:getAttribute beanName="declarationTaxNoticeLink" attribute="text"/> 
				</c:when>
				<c:otherwise>	                 
	            	<content:getAttribute beanName="declarationTaxNoticeText" attribute="text"/>
	            </c:otherwise>
           	</c:choose>
           </td>
         </tr>
         <tr>
           <td align="right" style="padding-left:4px;">
             <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.waitingPeriodDeclaration" singleDisplay="true"/> 
             <form:checkbox path="withdrawalRequestUi.selectedDeclarations"
                            value="${declarationConstants.WAITING_PERIOD_WAIVED_TYPE_CODE}"
                            id="waitingPeriodDeclarationId"
                            onclick="return handleWaitingPeriodDeclarationChanged();"/>
           </td>
           <td align="left">
             <content:getAttribute beanName="declarationWaitingPeriodText" attribute="text"/>
           </td>
         </tr>
       </table>
     </td>
     <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
   </tr>
   <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.wmsiOrPenchecksSelected}">
     <tr>
       <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
       <td class="sectionNameColumn"><strong>It was certified that:</strong></td>
       <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
     </tr>
     <tr>
       <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
       <td style="padding-left:4px;">
         <ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.iraProviderDeclaration" singleDisplay="true"/> 
         <form:checkbox path="withdrawalRequestUi.selectedDeclarations"
                        value="${declarationConstants.IRA_SERVICE_PROVIDER_TYPE_CODE}"
                        id="wmsiDeclarationId"
                        onclick="return handleWmsiDeclarationChanged();"/>
            <content:getAttribute beanName="declarationIraProviderText" attribute="text"/>
          <br/>
          <br/>
        </td>
        <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      </tr>
    </c:if> 
     <tr class="datacell1">
       <td colspan="3" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
     </tr>
    <tr>
      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
      <td class="indentedValue"><content:getAttribute beanName="layoutPageBean" attribute="body3"/></td>
      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
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
</div>
