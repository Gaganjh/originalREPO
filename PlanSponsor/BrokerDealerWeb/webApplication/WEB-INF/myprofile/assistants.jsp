<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<%@page import="com.manulife.pension.bd.web.myprofile.MyProfileUtil"%>
<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@page import="com.manulife.pension.service.security.bd.valueobject.BrokerAssistantUserInfo"%>


<content:contentBean contentId="<%=BDContentConstants.ADD_ASSISTANT_LINK_TEXT%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="addAssistantButton" />

<content:contentBean contentId="<%=BDContentConstants.ADD_ASSISTANT_TERM_TEXT%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="addAssistantTerm" />

<content:contentBean contentId="<%=BDContentConstants.NO_ASSISTANT_TEXT%>" 
   type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="noAssistantsText" />

<script type="text/javascript">
<!--
	function doCancel(frm) {
		frm.firstName.value = "";
		frm.lastName.value = "";
		frm.email.value = "";
		frm.term.checked=false;
		showAddDiv(false);
	}
	
    function showAddDiv(show) {
        var ele = document.getElementById("report_customization_wrapper");
    	if (typeof ele == "undefined") {
     	   return false;
	     }
	    if (show) {
           ele.style.display = "block";
	    } else {
	    	ele.style.display = "none";
	    }
        ele = document.getElementById("addButtonDiv");
    	if (typeof ele == "undefined") {
      	   return false;
 	     }
	    if (show) {
    		ele.style.display = "none";
	    } else {
	    	ele.style.display = "block";
	    }
	    return false;
    }

    function removeAssistant(frm, profileId) {
        if (confirm("Are you sure want to remove this Assistant?")) {
	        frm.action.value="remove";
	        frm.userProfileId.value=profileId;
	        frm.submit();
        }
    }

    function resendActivation(frm, profileId) {
        frm.action.value="resendActivation";
        frm.userProfileId.value=profileId;
        frm.submit();
    }
//-->
</script>
<layout:pageHeader nameStyle="h2"/>

<c:if test="${not empty sessionScope.successContent}">
 <utils:info contentId="${sessionScope.successContent}"/> 
<%
   session.removeAttribute("successContent");
    
%>
</c:if>

<style type="text/css">
	form {display:inline;}
	.button_search #button {
		font-size:10px;
	}	
</style>

<report:formatMessages scope="session"/>

<userprofile:myprofileTab/>

      <c:choose>
       <c:when test="${requestScope.addBrokerAssistantForm.show}">
          <c:set var="showAddDiv" value="display:block"/>
          <c:set var="showAddButtonDiv" value="display:none"/>
       </c:when>
       <c:otherwise>
          <c:set var="showAddDiv" value="display:none"/>
          <c:set var="showAddButtonDiv" value="display:block"/>
       </c:otherwise>
      </c:choose>

   <div class="page_section_subheader controls">
	<h3><content:getAttribute attribute="body1Header" beanName="layoutPageBean" /></h3>
	<div id="addButtonDiv" style="${showAddButtonDiv};margin-top:10px">
     <a class="buttonheader" name="showAddDiv" href="#showAddDiv" onclick="return showAddDiv(true)"><span><content:getAttribute attribute="text" beanName="addAssistantButton" /></span></a>
    </div>
   </div>
   <div class="report_table">
    <div id="bob_overview_report">
     <div id="report_customization_wrapper" style="${showAddDiv}">
	    <bd:form action="/do/myprofile/addAssistant" modelAttribute="addBrokerAssistantForm" name="addBrokerAssistantForm">
	   
		<div class="BottomBorder">
			<div class="SubTitle Gold Left">
			     Add Assistant
			</div>
			<div class="GrayLT Right">* = Required Field</div>
		</div>
	
	    <input type="hidden" name="action" value="save">
		<div class="label">* First Name:</div>
		<div class="inputText">
		  <label>
<form:input path="firstName" maxlength="30" cssClass="input"/>
		  </label>
		</div>
		<div class="label">* Last Name:</div>
		<div class="inputText">
		  <label>
<form:input path="lastName" maxlength="30" cssClass="input"/>
		  </label>
		</div>
		<div class="label">* Email:</div>
		<div class="inputText">
		  <label>
<form:input path="email" maxlength="70" cssClass="input"/>
		  </label>
		</div>
	      
		<div class="label">*</div>
		<div class="inputText">
		  <label>
<form:checkbox path="term"/>
		    <content:getAttribute attribute="text" beanName="addAssistantTerm" /></label>
			<br />
		</div>
		<div id="actionButtons">
		 <div class="formButton"> 
	       <input type="button" class="blue-btn next" 
				onmouseover="this.className +=' btn-hover'" 
		        onmouseout="this.className='blue-btn next'"
		        name="continue" value="Save"
		        onclick="return doProtectedSubmitBtn(document.addBrokerAssistantForm, 'save', this)">
         </div> 

	     <div class="formButton">
	      <input type="button" class="grey-btn back" 
	             onmouseover="this.className +=' btn-hover'" 
	             onmouseout="this.className='grey-btn back'"
	             name="cancel" value="Cancel"
	             onclick="return doCancel(document.addBrokerAssistantForm)"> 
	     </div>
	    </div>
	   </bd:form>
       </div>
      </div>
	  <bd:form action="/do/myprofile/manageAssistant" modelAttribute="manageBrokerAssistantForm" name="manageBrokerAssistantForm">
<form:hidden path="action"/>
<form:hidden path="userProfileId"/>
		 <table border="0" cellpadding="0" cellspacing="0" class="report_table_content">
				<thead>
						<tr>
							<th width="20%" valign="bottom" nowrap="nowrap" class="val_str"><strong>Name</strong></th>
						    <th width="40%" valign="bottom" class="val_str"><strong>Email Address </strong></th>
						    <th width="23%" valign="bottom" class="val_str"><strong>Registered Date </strong></th>		        
							<th width="17%" valign="bottom" class="val_str"><strong>Remove Assistant </strong></th>
						 </tr>
				</thead>
				<tbody>
				   <c:choose>
				     <c:when test="${empty requestScope.assistantList}">
				        <tr>
				          <td colspan="4">
				             <content:getAttribute attribute="text" beanName="noAssistantsText"/>
				          </td>
				        </tr>
				     </c:when>
				     <c:otherwise>
					   <c:forEach var="assistant" items="${requestScope.assistantList}" varStatus="loopStatus">
						 	<c:choose>
								<c:when test="${loopStatus.index % 2 == 1}">
									<c:set var="trStyleClass" value="" />
								</c:when>
								<c:otherwise>
									<c:set var="trStyleClass" value="spec" />
								</c:otherwise>
							</c:choose>
							<tr class="${trStyleClass}">
								<td class="name">${assistant.firstName} ${assistant.lastName}</td>
								<td class="val_num_cnt">
									    ${assistant.emailAddress }
								</td>
								  <c:choose>
									  <c:when test="${assistant.registered}">
										  <td class="date">
										      <fmt:formatDate value="${assistant.registeredTs}" pattern="MM/dd/yyyy"/> 	
										  </td>
									  </c:when>
									  <c:otherwise>
										  <td class="button">
<c:set var="assistant" value="${assistant}" />
<%
BrokerAssistantUserInfo assistant = (BrokerAssistantUserInfo)pageContext.getAttribute("assistant");
%>
											  <c:choose>
												  <c:when test="<%=MyProfileUtil.isAllowResendInvitationForAssistant(request, assistant) %>">
										         <div class="button_search">
								                      <input id="button" type="button" value="Resend Registration" onclick="resendActivation(this.form, ${assistant.userProfileId})"/>
												 </div>					          						     							         
								                 </c:when>
											      <c:otherwise>
											      <div class="button_disabled">
											          <input id="button" type="button" value="Resend Registration" />
											      </div>
											      </c:otherwise> 
											  </c:choose>
										  </td>
								     </c:otherwise>
								  </c:choose>
							    <td class="button">
							       <div class="button_search">
				                          <input id="button" type="button" value="Remove" onclick="removeAssistant(this.form, ${assistant.userProfileId})"/>
				                   </div>					          
				                </td>
							 </tr>
					   </c:forEach>
					   </c:otherwise>
				   </c:choose>
				</tbody>
			</table>
       </bd:form>
  </div>
  
  <layout:pageFooter/>	
