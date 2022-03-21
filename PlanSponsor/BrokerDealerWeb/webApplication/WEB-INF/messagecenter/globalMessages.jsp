<%-- Imports used--%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@page import="com.manulife.pension.bd.web.BDConstants"%>
<%@page import="com.manulife.pension.service.message.report.valueobject.BDGlobalMessageReportData"%>
 <%@page import="com.manulife.pension.service.message.report.valueobject.BDGlobalMessage"%>
<%-- Taglibs used--%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>


<%-- Constant Files used--%>
<un:useConstants var="contentConstants"
	className="com.manulife.pension.bd.web.content.BDContentConstants" />
<% 

BDGlobalMessageReportData theReport =(BDGlobalMessageReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);


%>

<c:set var="reportItem" value="${item}" scope="page" />
			 	


<content:contentBean contentId="${contentConstants.MC_NO_MESSAGE_TEXT}"
                     type="${contentConstants.TYPE_MISCELLANEOUS}"
                     id="noMessageText"/>

<content:contentBean contentId="${contentConstants.GLOBAL_MESSAGES_PUBLISH_CONFIRM}"
                     type="${contentConstants.TYPE_MISCELLANEOUS}"
                     id="publishConfirm"/>

<content:contentBean contentId="${contentConstants.GLOBAL_MESSAGES_EXPIRE_CONFIRM}"
                     type="${contentConstants.TYPE_MISCELLANEOUS}"
                     id="expireConfirm"/>


<script type="text/javascript">

/**
 * invoked on page onload. Enables the buttons if the raio button is preselected
 */
function doOnload() {
	if(document.getElementById("selectedContentId").value != '') {
		enableButtons(document.getElementById("selectedContentId").value, document.getElementById("messagePublished").value);
	} else {
		disableButtons();
	}
}


/*
 * enables the buttons
 */
function enableButtons(contentId, publishedDate) {
	document.getElementById("disabledPublish").style.display="none";	   
	document.getElementById("disabledExpire").style.display="none";	   
	document.getElementById("enabledPublish").style.display="";	   
	document.getElementById("enabledExpire").style.display="";	 
	document.getElementById("selectedContentId").value=contentId;
	document.getElementById("messagePublished").value=publishedDate;
}

/**
 * disables the buttons
 */
function disableButtons() {
	document.getElementById("disabledPublish").style.display="";	   
	document.getElementById("disabledExpire").style.display="";	   
	document.getElementById("enabledPublish").style.display="none";	   
	document.getElementById("enabledExpire").style.display="none";	 
}

/**
 * invoked on clicking publish button
 */
function publishMessage() {
	if(confirm('<content:getAttribute attribute="text" beanName="publishConfirm"/>')){
		disableButtons();
		document.forms['globalMessagesForm'].task.value='publish';
		document.forms['globalMessagesForm'].submit();
	}
}

/**
 * invoked on clicking expire button
 */
function expireMessage() {
	if(document.getElementById("messagePublished").value == 'false') {
		disableButtons();
		document.forms['globalMessagesForm'].task.value='expire';
		document.forms['globalMessagesForm'].submit();
	} else {
	if(confirm('<content:getAttribute attribute="text" beanName="expireConfirm"/>')){
		disableButtons();
		document.forms['globalMessagesForm'].task.value='expire';
		document.forms['globalMessagesForm'].submit();
	} }
}
</script>

<style type="text/css">
	form {display:inline;}
	
	.table_display_info {
	  line-height: 1.1em;
	}

</style>

<layout:pageHeader nameStyle="h2"/>

<div id="messages" class="report_table">
<report:formatMessages scope="session"/>


<div class="table_controls_footer"></div>

<div class="page_section_subheader controls">
    <h3><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></h3>
</div>
<input type="hidden" id="selectedContentId" name="selectedContentId"/>
<bd:form action="/do/globalMessages/"  modelAttribute="globalMessagesForm" name="globalMessagesForm">


<input type="hidden" name="task" value="sort"/>


<form:hidden path="messagePublished" id="messagePublished"/>

<%-- Error- message box --%>
<DIV id="errordivcs"><report:formatMessages scope="session"/></DIV>
<br>

<!--.table_controls-->
<table class="report_table_content" summary="This is an example table for reports. This particular attribute contains a description of what data the table contains. This should be populated with a lengthier description than just the table title.">
       <thead>
             <tr>
                 <th width="12%" valign="bottom" class="val_str"><a href="#selectAnchor" name="selectAnchor" onclick="return false;" style="text-decoration:none">Select Message</a></th>
                 <th width="60%" valign="bottom" class="val_str"><report:sort  field="<%=BDGlobalMessageReportData.SORT_MESSAGE_TEXT%>"  formName="globalMessagesForm" direction="asc">Message </report:sort></th>
                 <th width="10%" valign="bottom" nowrap="nowrap"class="val_str"><report:sort field="<%=BDGlobalMessageReportData.SORT_CMA_KEY%>"  formName="globalMessagesForm"  direction="desc">CMA Key </report:sort></th>
                 <th width="18%" valign="bottom" class="val_str"><report:sort field="<%=BDGlobalMessageReportData.SORT_PUBLISHED_DATE%>"  formName="globalMessagesForm"  direction="desc">Message Live Date </report:sort></th>
             </tr>
             </thead>
             <tbody>
             <c:choose>
	 	               <c:when test="${theReport.totalCount==0}">
	 	               <tr>
	 	               <td colspan="4">
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
			          <tr class="${trStyleClass}">
                      <td class="name">
                          <div align="center">
<form:radiobutton onclick="enableButtons(${item.cmaMessageContent.key}, ${not empty item.publishedDate});" path="selectedContentId" value="${item.cmaMessageContent.key}"/>


                          </div>
                     </td>
                     <td class="name"  align="left"> ${item.cmaMessageContent.text} <br/> 
                                                     ${item.cmaMessageContent.longText} <br/> 
                                                     ${item.cmaMessageContent.additionalText}
                     </td>
                     <td class="name" align="center">${item.cmaMessageContent.key}</td>
                     <td class="val_str" align="center">
                     <c:choose>
                         <c:when test="${empty item.publishedDate}">
                              <strong>-</strong>
                         </c:when>
                         <c:otherwise>
                             <fmt:formatDate pattern="MM/dd/yyyy hh:mm a 'ET'" value="${item.publishedDate}"/>
                         </c:otherwise>
                    </c:choose>
                    </td>
                    </tr>
			 </c:forEach>        
           </c:otherwise>
		 </c:choose>             
      </tbody>
</table>

<div class="table_controls_footer"></div>
<div class="table_controls">
   <table width="100%">
     <tr>
       <td height="20"> 
       <c:if test="${theReport.totalCount!=0}">
        <div id="disabledPublish" class="button_disabled">
                    <input name="button2" disabled="disabled" type="button" id="button" style="width:100px; font-size:10px;"  value="Publish" />
        </div> 
        <div  id="enabledPublish" class="button_login">
                    <input name="button2"  type="button" id="button" style="width:100px; font-size:10px;" onclick="publishMessage()" value="Publish" />
        </div> 
        <div id="disabledExpire" class="button_disabled">
                    <input name="button2" disabled="disabled" type="button" id="button" style="width:100px; font-size:10px;"  value="Expire" />
        </div> 
        <div  id="enabledExpire"  class="button_login">
                    <input name="button2"  type="button" id="button" style="width:100px; font-size:10px;" onclick="expireMessage()" value="Expire" />
        </div>
        </c:if>
       </td>
     </tr>
   </table>
 </div>
</bd:form>
</div> 
<layout:pageFooter/>

