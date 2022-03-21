<%-- Note Section R/O JSP Fragment

NOTE: This fragment presents Admin2Participant and Admin2Admin notes.

@param withdrawalRequestUi - Request scoped WithdrawalRequestUi bean
@param withdrawalRequest - Request scoped WithdrawalRequest bean ( = withdrawalRequestUi.getWithdrawalRequest())
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>

<content:contentBean 
  contentId="${contentConstants.SECTION_HEADING_WITHDRAWAL_STEP_2_NOTES_SECTION_TITLE}"
  type="${contentConstants.TYPE_MISCELLANEOUS}" 
  id="notesSectionTitle"/>

<content:contentBean 
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_PRINT_SPECIAL_CONTENT_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}" 
  id="notesSpecialContent"/>

<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
  <tr>
    <td class="tableheadTD1">
      <div style="padding-top:5px;padding-bottom:5px">
        <b><content:getAttribute attribute="text" beanName="notesSectionTitle" /></b>
      </div>
    </td>
    <td class="tablehead" style="text-align:right" nowrap>
      &nbsp;
    </td>
  </tr>
</table>

<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
  <tr>
    <td width="1" class="boxborder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
    <td>
      <table border="0" cellpadding="0" cellspacing="0" width="498">
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn" style="padding-left:4px;vertical-align:top;">
          	  <c:choose>
          	     <c:when test="${withdrawalRequestUi.isParticipantInitiated}">
          		<strong>To participant if denying</strong>
		     </c:when>
          	     <c:otherwise>          		
          		<strong>To participant</strong>
          	     </c:otherwise>
          	  </c:choose>          		
          </td>
          <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn">

		
<c:if test="${fn:length(withdrawalRequest.readOnlyAdminToParticipantNotes) > 0}">

        
 			<c:if test="${empty param.printFriendly}"> 
          		<div style="overflow-y:auto;height:expression(this.scrollHeight>100?'100px':'auto');max-height:100px;
          			overflow-x:auto;width:expression(this.scrollWidth>400?'400px':'auto');">
        	</c:if>
            
		            
  <c:forEach items="${withdrawalRequest.readOnlyAdminToParticipantNotes}" var="note" >
    <c:set scope="request" var="userName" value="${withdrawalForm.userNames[note.createdById]}" />
		        <span class="highlightBold">
		          <c:out value="${userName.firstName}" />&nbsp;<c:out value="${userName.lastName}" />
		        </span>
		        <span class="highlightBold">
		          <fmt:formatDate value="${note.created}" type="both" pattern="${timeStampPattern}"/>
		        </span>
		        <span>${note.noteForDisplay}</span><br>
  </c:forEach>
		              
        
 			<c:if test="${empty param.printFriendly}"> 
          		</div>
          	</c:if>
           
		
</c:if>

          </td>
        </tr>
        <tr class="datacell1" valign="top">
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>

	<c:if test="${empty param.printParticipant}">        
    
        <tr class="datacell1" valign="top">
          <td class="sectionNameColumn" style="padding-left:4px;vertical-align:top;"><strong>For administrator(s) only</strong></td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td class="indentedValueColumn">

<c:if test="${fn:length(withdrawalRequest.readOnlyAdminToAdminNotes) > 0}">

       
     	<c:if test="${empty param.printFriendly}">
            <div style="overflow-y:auto;height:expression(this.scrollHeight>100?'100px':'auto');max-height:100px;
            	overflow-x:auto;width:expression(this.scrollWidth>400?'400px':'auto');">
  	  	</c:if>
  	  
          
  <c:forEach items="${withdrawalRequest.readOnlyAdminToAdminNotes}" var="note" >
    <c:set scope="request" var="userName" value="${withdrawalForm.userNames[note.createdById]}" />
              <span class="highlightBold">
                <c:out value="${userName.firstName}" />&nbsp;<c:out value="${userName.lastName}" />
              </span>
              <span class="highlightBold">
                <fmt:formatDate value="${note.created}" type="both" pattern="${timeStampPattern}"/>
              </span>
              <span>${note.noteForDisplay}</span><br>
  </c:forEach>
                        
      
     	<c:if test="${empty param.printFriendly}">
            </div>
        </c:if>
  		

</c:if>
            
          </td>
        </tr>
</c:if>

    

	<c:if test="${empty param.printParticipant}">      
        <tr class="datacell1">
          <td colspan="3" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>
        
  <c:if test="${withdrawalRequestUi.showStaticContent}">
    <c:if test="${!withdrawalRequestUi.isParticipantInitiated}">
        <tr class="datacell1" valign="top">
          <td colspan="3" class="sectionCommentText">
            <content:getAttribute beanName="notesSpecialContent" attribute="text"/>
          </td>
        </tr>
    </c:if>
  
        <tr class="datacell1" valign="top">
          <td colspan="3" class="sectionCommentText">
            <content:getAttribute beanName="step2PageBean" attribute="body3Header"/>
          </td>
        </tr>
   </c:if>
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