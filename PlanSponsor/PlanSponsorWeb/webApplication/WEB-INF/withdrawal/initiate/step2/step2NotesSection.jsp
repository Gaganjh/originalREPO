<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_NOTES_SECTION_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="notesSectionTitle"/>
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_PRINT_SPECIAL_CONTENT_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="notesPrintCopyText"/>

<div style="padding-top:10px;padding-bottom:10px;">
 <table class="box" border="0" cellpadding="0" cellspacing="0" width="100%">
	  <tr>
     <td class="tableheadTD1" colspan="3">
      	<div style="padding-top:5px;padding-bottom:5px">
      		<span style="padding-right:2px" id="noteShowIcon" onclick="showNoteSection();">
      			<img src="/assets/unmanaged/images/plus_icon.gif" width="12" height="12" border="0">
      		</span>
      		<span style="padding-right:2px" id="noteHideIcon" onclick="hideNoteSection();">
      			<img src="/assets/unmanaged/images/minus_icon.gif" width="12" height="12" border="0">
      		</span>
      		<b><content:getAttribute beanName="notesSectionTitle" attribute="text"/></b>
      	</div>
      </td>
  	</tr>
   <tr>
     <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
     <td>
       <table border="0" cellpadding="0" cellspacing="0" width="100%" id="notesTable">
         <tr class="datacell1" valign="top">
           <td class="shortSectionNameColumn" style="vertical-align:top;">
             <strong>
             	<ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.currentAdminToParticipantNote.note" singleDisplay="true"/>
             	To participant
             </strong>
           </td>
           <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
           <td class="longIndentedValueColumn">
<form:textarea path="withdrawalRequestUi.withdrawalRequest.currentAdminToParticipantNote.note" cols="37" onchange="return handleAdminToParticipantNoteChanged();" rows="4" id="adminToParticipantNoteId"/>




           </td>
         </tr>
         <tr class="datacell1" valign="top">
           <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
           <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
           <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
         </tr>
         <tr class="datacell1">
           <td class="shortSectionNameColumn" style="vertical-align:top;">
             <strong>
             	<ps:fieldHilight name="withdrawalRequestUi.withdrawalRequest.currentAdminToAdminNote.note" singleDisplay="true"/>
             	For administrator(s) only
             </strong>
           </td>
           <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
           <td class="longIndentedValueColumn">
<form:textarea path="withdrawalRequestUi.withdrawalRequest.currentAdminToAdminNote.note" cols="37" onchange="return handleAdminToAdminNoteChanged();" rows="4" id="adminToAdminNoteId"/>




           </td>
         </tr>
         <tr class="datacell1" valign="top">
           <td colspan="3" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
         </tr>
         <tr class="datacell1" valign="top">
           <td colspan="3" class="indentedValue">
             <content:getAttribute beanName="layoutPageBean" attribute="body3Header"/>
           </td>
         </tr>
         <c:if test="${not withdrawalRequestUi.withdrawalRequest.participantInitiated}">
	          <tr class="datacell1" valign="top">
	            <td colspan="3" class="indentedValue">
		        		<content:getAttribute beanName="notesPrintCopyText" attribute="text"/>
	            </td>
	          </tr>
          </c:if>
        </table>
      </td>
      <td class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    </tr>
    <tr>
      <td colspan="3">
        <div id="notesFooter">
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
