<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.bd.web.util.BDWebCommonUtils"%>
<%@page import="com.manulife.pension.service.message.report.valueobject.BDMessageReportData"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.manulife.pension.service.message.report.valueobject.BDMessageReportDetails"%>
        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib tagdir="/WEB-INF/tags/security" prefix="security" %>
<%@ taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>


<%@page import="com.manulife.pension.bd.web.messagecenter.BDMessageCenterUtils"%>

<content:contentBean contentId="<%=BDContentConstants.MC_NO_MESSAGE_TEXT%>" 
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="noMessageText" />

<content:contentBean contentId="<%=BDContentConstants.MC_DELETE_MESSAGE_WARNING%>" 
	type="<%=BDContentConstants.TYPE_MESSAGE%>" id="deleteWarning" />



<% 

BDMessageReportData theReport =(BDMessageReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
BDMessageReportData reportItem =(BDMessageReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("reportItem",reportItem,PageContext.PAGE_SCOPE);
%>
<c:set var="underMimic" value="<%=BDMessageCenterUtils.isUnderMimic(request) %>"/>


<script type="text/javascript">
   var selectedMessages = [];
   var deleteWarning = '<content:getAttribute attribute="text" beanName="deleteWarning" filter="true"/>';
   var visitedMessages = [];
   
   function doDelete() {
	   document.forms['messageCenterForm'].deleteBtn.disabled=true;
	   document.forms['messageCenterForm'].deleteBtn2.disabled=true;
	   if (selectedMessages.length > 0) {
		   if (!confirm(deleteWarning)) {
			   document.forms['messageCenterForm'].deleteBtn.disabled=false;
			   document.forms['messageCenterForm'].deleteBtn2.disabled=false;
			   return false;
		   }
	   } 
	   try {
	   document.forms['messageCenterForm'].messageIds.value = selectedMessages.toString();
	   document.forms['messageCenterForm'].task.value = 'delete';
	   document.forms['messageCenterForm'].submit();
	   } catch (e) {
		   document.forms['messageCenterForm'].deleteBtn.disabled=true;
		   document.forms['messageCenterForm'].deleteBtn2.disabled=true;
	   }	   
   }
   
   function selectionChanged(chbox) {
	   if (chbox.checked) {
		   addToSelection(chbox.value);
	   } else {
		   removeFromSelection(chbox.value);
	   }
   }

   function addToSelection(value) {
	   selectedMessages.push(value);
   }

   function removeFromSelection(value) {
	   var newSelection =[];
	   for (i=0; i< selectedMessages.length; i++) {
	     if (selectedMessages[i] != value) {
		     newSelection.push(selectedMessages[i]);
	     }
	   }
	   selectedMessages = newSelection;
   }

   function doExpand(messageId, expand, messageVisited) {
	   var shortDiv = document.getElementById("short_" + messageId);
	   var longDiv = document.getElementById("long_" + messageId);

	   if (typeof shortDiv == "undefined" || typeof longDiv == "undefined") {
		   return;
	   }
	   if (expand) {
		   shortDiv.style.display="none";
		   longDiv.style.display="block";
	   } else {
		   shortDiv.style.display="block";
		   longDiv.style.display="none";
	   }

	   if (!messageVisited && addVisitedMessage(messageId)) {
		   invokeVisitMessage(messageId);		   		   
	   }
   }

   function invokeVisitMessage(messageId) {
	   <c:if test="${not underMimic}">
	     if (messageId != null) {
           showMessageVisited(messageId);
         }
		   var visitMessageCallback = { 
				    cache:false,
				    success: function(response) {
				      /*success handler code*/
				      var result = response.responseText;
				      if (result=="success") {
				      }
				    }, 
				    failure: function(response) {/*failure handler code*/}, 
				    argument: null 
				  };
		  if (messageId != null) {
			  var transaction = YAHOO.util.Connect.asyncRequest(
			    'GET', '/do/messagecenter/visitMessage?messageId=' + messageId,
			    visitMessageCallback);
		   }
	  </c:if>			    
   }
   
   function addVisitedMessage(messageId) {
	   for (i=0; i< visitedMessages.length; i++) {
		     if (visitedMessages[i] == messageId) {
			     return false;
		     }
	   }
	   visitedMessages.push(messageId);
	   return true;
   }

   function showMessageVisited(messageId) {
	   var shortSpan = document.getElementById("short_span_" + messageId);
	   var longSpan = document.getElementById("long_span_" + messageId);

	   if (typeof shortSpan == "undefined" || typeof longSpan == "undefined") {
		   return;
	   }
	   shortSpan.className='visited';
	   longSpan.className='visited';
   }
</script>

<style type="text/css">
	form {display:inline;}
	
	.table_display_info {
	  line-height: 1.1em;
	}

	.visited  {color: #999999}
	.new {color: #005B80}

	#message_table .new p {color: #005B80}
	#message_table .visited p {color: #999999}

</style>


<h2><content:getAttribute attribute="name" beanName="layoutPageBean"/></h2>
<p>
<br>
<c:choose>
  <c:when test="${underMimic}">
    My preferences
  </c:when>
  <c:otherwise>
	<a style="text-decoration: underline;" href="/do/myprofile/preference">Edit My Preferences</a>  
  </c:otherwise>
</c:choose>
</p>
<c:if test="${not empty layoutBean.layoutPageBean.introduction1}">
<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
</c:if>					
<c:if test="${not empty layoutBean.layoutPageBean.introduction2}">
<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>
</c:if>

<report:formatMessages scope="session"/>

<div class="page_section_subheader controls">
    <h3><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></h3>
</div>
<bd:form action="/do/messagecenter/" modelAttribute="messageCenterForm" name="messageCenterForm">

<input type="hidden" name="task" value="filter"/>
<input type="hidden" name="messageIds"/>
<c:if test="${theReport.totalCount>0}">
<div class="table_controls">
  <div class="table_action_buttons"> 
     <c:choose>
      <c:when test="${underMimic}">
      <div class="button_disabled">
          <input name="deleteBtn" type="button" style="width:100px; font-size:10px;" 
             value="Delete Selected" disabled="disabled"/>
      </div>
      </c:when>
      <c:otherwise>
      <div class="button_login">                
          <input name="deleteBtn" type="button" style="width:100px; font-size:10px;" 
            onclick="doDelete()" value="Delete Selected" />
      </div>      
      </c:otherwise>
     </c:choose>
  </div>
 </div>
 <div class="table_controls">
  <div class="table_action_buttons"> </div>
  <div class="table_display_info">
    <strong><report:recordCounter report="theReport" label="Messages"/></strong>
  </div>
  <div class="table_pagination">
    <report:pageCounter report="theReport" arrowColor="black" formName="messageCenterForm"/>
  </div>
</div>
</c:if> 
  <div id="message_table" class="report_table">
         <table class="report_table_content" summary="">
           <thead>
             <tr>
               <th width="8%" align="center" valign="bottom" class="val_str"><a href="#deleteAnchor" name="deleteAnchor" onclick="return false;" style="text-decoration:none">Delete Message</a></th>
               <th width="10%" align="center" valign="bottom" class="val_str"><report:sort field="<%=BDMessageReportData.SORT_FIELD_CONTRACT_NUMBER%>" direction="asc" formName="messageCenterForm">Contract Number</report:sort></th>
               <th width="23%" align="center" valign="bottom" class="val_str"><report:sort field="<%=BDMessageReportData.SORT_FIELD_CONTRACT_NAME%>" direction="asc" formName="messageCenterForm">Contract Name</report:sort></th>
               <th width="50%" valign="bottom" class="val_str"><report:sort field="<%=BDMessageReportData.SORT_FIELD_SHORT_TEXT%>" direction="asc" formName="messageCenterForm">Message</report:sort></th>
               <th width="9%" align="center" valign="bottom" nowrap="nowrap" class="val_str"><report:sort field="<%=BDMessageReportData.SORT_FIELD_EFFECTIVE_DATE%>" direction="desc" formName="messageCenterForm">Date</report:sort></th>
             </tr>
           </thead>
           <tbody>
	 	     <c:choose>
	 	       <c:when test="${theReport.totalCount==0}">
	 	        <tr>
	 	         <td colspan="5">
	 	          	 <content:getAttribute attribute="text" beanName="noMessageText"/>
				 </td>
				</tr>	       
	 	       </c:when>	 	       
	 	       <c:otherwise>
	 		 	<c:forEach var="item" items="${theReport.details}" varStatus="loopStatus">

			 	
			 	<c:choose>
					<c:when test="${loopStatus.index % 2 == 1}">
						<c:set var="trStyleClass" value="" />
					</c:when>
					<c:otherwise>
						<c:set var="trStyleClass" value="spec" />
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${item.visited}">
						<c:set var="spanStyle" value="visited" />
						<script type="text/javascript">
						  addVisitedMessage(${item.messageId})
						</script>
					</c:when>
					<c:otherwise>
						<c:set var="spanStyle" value="new" />
					</c:otherwise>
				</c:choose>
			   <tr class="${trStyleClass}">
               <td class="name" align="center">
                 <div align="center">
                  <c:choose>
                    <c:when test="${underMimic}">
                      <input type="checkbox" name="messageId" value="" disabled="disabled"/>
                    </c:when>
                    <c:otherwise>
                      <input type="checkbox" name="messageId" value="${item.messageId}" onclick="selectionChanged(this)" />
                    </c:otherwise>
                  </c:choose>                   
               	  </div>
               	</td>
		 	   <c:choose>
		 	     <c:when test="${empty item.contractId}">
 			 	  <td class="val_num_cnt" align="center">
			 	       -
			 	  </td>
			 	  <td class="name" align="center">
			 	       -
	               </td>
		 	     </c:when>
			 	     <c:otherwise>
 			 	  <td class="val_num_cnt">
			 	       ${item.contractId}
			 	  </td>
			 	  <td class="name">
			 	  		${item.contractName}
	               </td>			 	     	
			 	     </c:otherwise>
		 	   </c:choose>			 	   
               <td class="val_str">
                 <div id="short_${item.messageId}" style="display:block">
	                  <span id="short_span_${item.messageId}" class="${spanStyle }">
	                  <strong><span onclick="javascript:doExpand(${item.messageId}, true, ${item.visited})">${item.shortText }</span></strong>
	                  </span>
                  </div>
                 <div id="long_${item.messageId}" style="display:none">
                 <span id="long_span_${item.messageId}" class="${spanStyle }">
                  <strong>${item.shortText }</strong><br/>
                  ${item.longText}                  
                  <c:if test="${not empty item.infoURL}">
                    <a href="${item.infoURL}">${item.infoURLDescription}</a>
                  </c:if>
                  <c:if test="${not empty item.additionalDetails}">
                     <br/>
                     ${item.additionalDetails}                     
                  </c:if>
                  </span>                   
                  <br/>                 
                  &lt;<a href="javascript:doExpand(${item.messageId}, false, ${item.visited})">close</a>&gt;
                 </div>
               </td>
               <td class="date">
                 <fmt:formatDate pattern="MM/dd/yyyy" value="${item.effectiveDate}"/>
               </td>
             </tr>
			 </c:forEach>
			</c:otherwise>
			</c:choose>           
           </tbody>
       </table>
     </div>
       <!--.report_table_content-->
<!--.report_table_content-->
  <c:if test="${theReport.totalCount>0}"> 
	<div class="table_controls">
	      <div class="table_action_buttons">
			<c:choose>
               <c:when test="${underMimic}">
	              <div class="button_disabled">
	                  <input name="deleteBtn" type="button" style="width:100px; font-size:10px;" 
	                     value="Delete Selected" disabled="disabled"/>
	              </div>
               </c:when>
               <c:otherwise>
	              <div class="button_login">                
	                  <input name="deleteBtn2" type="button" style="width:100px; font-size:10px;" 
	                    onclick="doDelete()" value="Delete Selected" />
	              </div>      
               </c:otherwise>
              </c:choose>	      
	      </div>
	</div>
	<div class="table_controls">	      
	    <div class="table_action_buttons"> </div>
		  <div class="table_display_info">
		    <strong><report:recordCounter report="theReport" label="Messages"/></strong>
		  </div>
		  <div class="table_pagination">
		    <report:pageCounter report="theReport" arrowColor="black" formName="messageCenterForm"/>
		  </div>
	</div>
</c:if>

<!--.table_controls-->
	<div class="table_controls_footer"></div>
			
<!--.report_table-->
</bd:form>
<layout:pageFooter/>
