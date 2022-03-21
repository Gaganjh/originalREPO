<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@page import="com.manulife.pension.service.message.valueobject.MessageRecipient"%>
<%@page import="com.manulife.pension.ps.web.messagecenter.history.MCCarViewUtils"%>
<%@page import="com.manulife.pension.service.message.valueobject.MessageRecipientStatusHistory"%>

<script language="javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<script type="text/javascript">
<!--
   
   function gotoLink(url) {
   	 <c:if test="${empty param.printFriendly}">
   	    window.location=url;
   	 </c:if>
   	 return;
   }
   var lastFlyout = null;
   function doFlyout(id, event) {
    if ( lastFlyout != null ) {
    	lastFlyout.style.display = "none";
    }
   	var div = document.getElementById("flyout" + id);
	if (div.style.display == "none" ) {
		div.style.display = "";
	} else if ( div.style.display == "" || div.style.display == "block" ) {
		div.style.display = "none";
	}
	var cursor = getPosition(event);
	div.style.left = cursor.x + 20;
	div.style.top = cursor.y - 10;
	lastFlyout= div;
	
   }
	function getPosition(e) {
	    e = e || window.event;
	    var cursor = {x:0, y:0};
	    if (e.pageX || e.pageY) {
	        cursor.x = e.pageX;
	        cursor.y = e.pageY;
	    } 
	    else {
	        var de = document.documentElement;
	        var b = document.body;
	        cursor.x = e.clientX + 
	            (de.scrollLeft || b.scrollLeft) - (de.clientLeft || 0);
	        cursor.y = e.clientY + 
	            (de.scrollTop || b.scrollTop) - (de.clientTop || 0);
	    }
	    return cursor;
	}   
	function checkLink(url, contractId) {
		  var checkAccessCallBack = { 
		    cache:false,
		    success: function(response) {
		      /*success handler code*/
		      var result = response.responseText;
		      if ( result == "no" ) {
		      	alert('Sorry, you do not have access to that page.')
		      } else {
		      	params = result.split("||");
		      	gotoLink('/do/mcCarView/infoLink?url=' + params[0] + '&contractId=' + params[1]);
	   	      }
		    }, 
		    failure: function(response) { alert('Sorry, you do not have access to that page.')}, 
		    argument: null 
		  };
		  var transaction = YAHOO.util.Connect.asyncRequest('GET'
		  		, '/do/mcCarView/checkAccess?url=' + url + '&contractId=' + contractId
		  		, checkAccessCallBack);	
	}
	

//-->
</script>
<content:errors scope="session"/>
<c:if test="${not empty layoutPageBean.body1Header}">
<content:getAttribute beanName="layoutPageBean" attribute="body1Header"/>
<br/>
<br/>
</c:if> 
<TABLE width="100%" border="0" cellpadding="0" cellspacing="0" style="padding-left:10px">
<TBODY>
  <tr>
    <td colspan="4" class="tableheadTD1" style="padding:3px"><b>&nbsp;Message details </b></td>
  </tr>
  <tr>
    <td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
    <td width="128" valign="top" class="filterSearch"><b>Message ID: </b></td>
    <td width="570" valign="top" class="filterSearch"><c:out value="${messageDetailForm.message.messageId}"/></td>
    <td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
</tr>
<tr>
	<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
	<td width="128" valign="top" class="filterSearch"><b>Message Category: </b></td>
	<td width="570" valign="top" class="filterSearch">
	<c:if test="${messageDetailForm.message.messageTemplate.messageActionType.value == 'DCP' }">Declare Complete</c:if>
	<c:if test="${messageDetailForm.message.messageTemplate.messageActionType.value == 'INF' }">FYI</c:if>
	<c:if test="${messageDetailForm.message.messageTemplate.messageActionType.value == 'ACT' }">Act Now</c:if>
	<!-- <td width="570" valign="top" class="filterSearch"><c:out value="${messageDetailForm.message.messageTemplate.messageActionType.value}"/> </td> -->
	<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
</tr>
<tr>
	<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
	<td width="128" valign="top" class="filterSearch"><b>Message Type: </b></td>
	<td onmouseover="Tip('Message template id <c:out value="${messageDetailForm.message.messageTemplate.id}"/>')"
        onmouseout="UnTip()" width="570" valign="top" class="filterSearch"><b><c:out value="${messageDetailForm.message.shortText}"/> </b></td>
	<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
</tr>
<tr>
	<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
	<td width="128" valign="top" class="filterSearch"><b>Description: </b></td>
	<td width="570" valign="top" class="filterSearch">
			<!-- <b><c:out value="${messageDetailForm.message.shortText}"/></b> -->
			<c:if test="${messageDetailForm.message.escalated}">${messageDetailForm.message.escalationTag}<br></c:if>
			${messageDetailForm.message.longText}
	</td>
	<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
</tr>
<tr>
<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
<td width="128" valign="top" class="filterSearch"><b>Generated: </b></td>
<td onmouseover="Tip('Event Type id <c:out value="${messageDetailForm.message.eventCreated.eventTypeId}"/>')"
onmouseout="UnTip()"  width="570" valign="top" class="filterSearch"><fmt:formatDate value="${messageDetailForm.message.created}" pattern="MMM d, yyyy"/> 
<c:if test="${not empty messageDetailForm.message.eventCreated}">
by event: <c:out value="${messageDetailForm.message.eventCreated.eventTypeName}"/> </c:if></td>
<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
</tr> 
<c:if test="${not empty messageDetailForm.message.previousMessageId}">
<tr>
	<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
	<td width="128" valign="top" class="filterSearch"><b> Replaced: </b></td>
	<td width="570" valign="top" class="filterSearch"> This message has replaced a similar message with message id  
	<a href="/do/mcCarView/messageDetail?messageId=${messageDetailForm.message.previousMessageId}&contractId=${messageDetailForm.message.contractId}">
	<c:out value="${messageDetailForm.message.previousMessageId}"/>
	</a></td>
	<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
</tr>
</c:if>
<c:if test="${not empty messageDetailForm.message.escalatedTs}">
<tr>
	<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
	<td width="128" valign="top" class="filterSearch"><b>Escalated: </b></td>
	<td width="570" valign="top" class="filterSearch"><fmt:formatDate value="${messageDetailForm.message.escalatedTs}" pattern="MMM d, yyyy"/>                      </td>
	<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
</tr>
</c:if>
<!-- <tr>
	<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
	<td width="128" valign="top" class="filterSearch"><b>Act link : </b></td>
	<td width="570" valign="top" class="filterSearch">
		<c:if test="${not empty messageDetailForm.message.actionUrl.url}">
			${ messageDetailForm.message.actionUrl.urlDescription}
		</c:if>	
		<c:if test="${empty messageDetailForm.message.actionUrl.url}">
			N/A
		</c:if>		
	</td>
	<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
</tr> -->
<!-- <tr>
	<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
	<td width="128" valign="top" class="filterSearch"><b>Info link : </b></td>
	<td width="570" valign="top" class="filterSearch">
			<c:if test="${messageDetailForm.carView}">
				<a href="javascript:gotoLink('/do/mcCarView/infoLink?url=${messageDetailForm.message.infoUrl.url}&contractId=${messageDetailForm.message.contractId}')">
			</c:if>
			<c:if test="${!messageDetailForm.carView}">
				<a href="javascript:gotoLink('/do/messagecenter/history/infoLink?url=${messageDetailForm.message.infoUrl.url}&contractId=${messageDetailForm.message.contractId}')">				
			</c:if>
		<c:out value="${messageDetailForm.message.infoUrl.urlDescription}"/>							
		</a>                           
	</td>
	<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
</tr> -->
<tr>
	<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
	<td width="128" valign="top" class="filterSearch"><b>Tab&nbsp;/&nbsp;section: </b></td>
	<td width="570" valign="top" class="filterSearch">
    	<c:out value="${messageDetailForm.message.messageTab}"/>&nbsp;/&nbsp; <c:out value="${messageDetailForm.message.messageSection}"/>
    </td>
	<td width="1"   valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
</tr>
<tr>
  <td colspan="4" valign="top" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
</tr>
</TBODY>
</TABLE>
<br/>
<c:if test="${not empty layoutPageBean.body1Header}">
<content:getAttribute beanName="layoutPageBean" attribute="body2Header"/>
<br/>
<br/>
</c:if> 
<table width="100%" border="0" cellspacing="0" cellpadding="0"  style="padding-left:10px">
                 <tr class="tableheadTD1">
                   <td colspan="7" class="tableheadTD1" style="padding:3px">
					<B>&nbsp;Message status history</B>
					</td>
                 </tr>
                                <tr>
                                  <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                  <td  class="tablesubhead"><B>Effective date</B></td>
                                  <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                  <td  class="tablesubhead"><b>Status</b></td>
                                  <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                  <td   class="tablesubhead"><B>Initiating user</B></td>
                                  <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                </tr>
								<c:forEach items="${messageDetailForm.message.messageStatusHistories}" var="messageHistory">                                
                                <tr class="datacell1"">
                                  <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                  <td  class="datacell1"><fmt:formatDate value="${messageHistory.effectiveTS}" pattern="MMM d, yyyy @ hh:mm a"/></td>
                                  <td width="1" class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                  <td   class="datacell1" nowrap><c:out value="${messageHistory.messageStatusDisplay}"/></td>
                                  <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                  <td   <c:if test="${not empty messageHistory.eventDetail}">
                                  			<c:if test="${messageHistory.messageStatus.value == 'OB'}">
                                  				onmouseover="Tip('Event Type id <c:out value="${messageHistory.eventDetail.eventTypeId}"/>')"
                                  			onmouseout="UnTip()" </c:if> </c:if> class="datacell1"> 
                                  			<c:choose>
                                  			   <c:when test="${messageHistory.userProfileId > 100}">
                                  			     <c:out value="${messageHistory.firstName}"/>&nbsp;<c:out value="${messageHistory.lastName}"/>
                                  			   </c:when>
                                  			   <c:otherwise>
                                  			      System
                                  			   </c:otherwise>
                                  			</c:choose>
                                  			&nbsp;
                                  			<c:choose>
                                  			  <c:when test="${not empty messageHistory.role.displayName }" >(<c:out value="${messageHistory.role.displayName}"/>)</c:when>
                                  			  <c:otherwise>(Message Center)</c:otherwise>
                                  			</c:choose>                                  			
                                  <c:if test="${messageHistory.messageStatus.value == 'RP'}" ><br>This message was replaced by similar message with message id <a href="/do/mcCarView/messageDetail?messageId=${messageDetailForm.message.replacedByMessageId}&contractId=${messageDetailForm.message.contractId}">
                                  <c:out value="${messageDetailForm.message.replacedByMessageId}"/>
                                  </a> </c:if>
                                  <c:if test="${not empty messageHistory.eventDetail}">
                                  	<c:if test="${messageHistory.messageStatus.value == 'OB'}" ><br>Obsoleted by event: 
                                  		<c:out value="${messageHistory.eventDetail.eventTypeName}"/>
                                  	</c:if>
                                  </c:if></td>
                                  <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                </tr>
                                </c:forEach>
                                <tr class="datacell1">
                                  <td colspan="7" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                                </tr>
						</table>                          
<br/>
<c:if test="${not empty layoutPageBean.body1Header}">
<content:getAttribute beanName="layoutPageBean" attribute="body3Header"/>
<br/>
<br/>
</c:if> 
<table border="0" cellspacing="0" cellpadding="0"  style="padding-left:10px">
                        <tr class="tableheadTD1">
                          <td colspan="9" class="tableheadTD1" style="padding:3px">
								<B>&nbsp;Message Recipients</B>
							</td>
                        </tr>
                         <tr>
                           <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                           <td class="tablesubhead" style="padding-left:1px"><B>Recipient</B></td>
                           <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                           <td class="tablesubhead" style="padding-left:1px"><b>Current Status</b></td>
                           <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                           <td class="tablesubhead" style="padding-left:1px"><B>Current Priority</B></td>
                           <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                           <td class="tablesubhead" style="padding-left:1px"><B>Status History</B></td>
                           <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                         </tr>
					     <c:forEach items="${messageDetailForm.message.messageRecipients}" var="messageRecipient" varStatus="loopStatus"> 
					     <% MessageRecipient recipient =  (MessageRecipient) pageContext.getAttribute("messageRecipient"); %>  
							<c:choose>
								<c:when test="${loopStatus.index % 2 == 1}">
									<c:set var="trStyleClass" value="datacell4" />
								</c:when>
								<c:otherwise>
									<c:set var="trStyleClass" value="datacell1" />
								</c:otherwise>
							</c:choose>
                         <tr class="${trStyleClass}">
                           <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                           <td class="${trStyleClass}" nowrap style="padding-left:2px"><c:out value="${messageRecipient.recipient.lastName}"/>,&nbsp;<c:out value="${messageRecipient.recipient.firstName}"/>&nbsp;(<c:out value="${messageRecipient.recipient.userRole.displayName	}"/>) </td>
                           <td width="1" class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                           <td class="${trStyleClass}" nowrap style="padding-left:2px"><%= MCCarViewUtils.getRecipientStatusDisplay(recipient.getRecipientStatus())%></td>
                           <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                           <td class="${trStyleClass}" nowrap style="padding-left:2px"><%= MCCarViewUtils.getPriorityDisplay(recipient.getPriority())%></td>
                           <td class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                           <td class="${trStyleClass}" style="text-align:right;padding-left:2px">
                           		<c:if test="${not empty messageRecipient.statusHistories }">
                           			<span onmouseover="this.style.cursor='pointer'" onclick="doFlyout('${messageRecipient.recipient.userProfileId }', event)">
                           			<img src="/assets/unmanaged/images/layer_icon.gif" border="0">
                           			</span>
                           		</c:if>
                           </td>
                           <td class="databorder" width="1" nowrap><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                         </tr>
                         </c:forEach>
                         <tr class="${trStyleClass}">
                           <td colspan="9" class="boxborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                         </tr>
                         </table>
                         <br>
                         <br>
<c:forEach items="${messageDetailForm.message.messageRecipients}" var="messageRecipient">
	<c:if  test="${not empty messageRecipient.statusHistories }">
		<div id="flyout${messageRecipient.recipient.userProfileId }" style="position:absolute;display:none">
			<table border="0" cellspacing="0" cellpadding="0"  style="padding-left:10px">
                        <tr class="tableheadTD1">
                          <td  class="tableheadTD1" colspan="4" style="padding:3px" nowrap >
								<B>&nbsp;Recipient: ${messageRecipient.recipient.firstName}&nbsp;${messageRecipient.recipient.lastName}&nbsp;(${messageRecipient.recipient.userRole.displayName})	</B>
							</td>
                        </tr>
                         <tr>
                           <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                           <td class="tablesubhead"><B>Effective Date</B></td>
                           <td width="1" class="dataheaddivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                           <td class="tablesubhead" style="border-right:1px solid #002D62"><b>Status</b></td>
                         </tr>
					     <c:forEach items="${messageRecipient.statusHistories}" var="history">     
					     <% MessageRecipientStatusHistory history = (MessageRecipientStatusHistory)pageContext.getAttribute("history"); %>                           
                         <tr class="datacell1">
                           <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                           <td class="datacell1" nowrap>${history.effectiveTS }</td>
                           <td width="1" class="datadivider"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                           <td class="datacell1" nowrap style="border-right:1px solid #002D62"><%=MCCarViewUtils.getRecipientStatusDisplay(history.getRecipientStatus()) %></td>
                         </tr>
                         </c:forEach>
                         <tr class="datacell1">
                           <td colspan="4" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
                         </tr>
        	</table>
		</div>
	</c:if>
</c:forEach>                                
                         
<input name="task" type="submit" class="button134" onClick="document.location='/do/mcdispatch/'" value="back">                         
