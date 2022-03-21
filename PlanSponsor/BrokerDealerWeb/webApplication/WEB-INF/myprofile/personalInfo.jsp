<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@ page import="com.manulife.pension.bd.web.BDConstants" %>
<%@taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<content:contentBean contentId="<%=BDContentConstants.RIA_EMAIL_EXISTS%>" 
   type="<%=BDContentConstants.TYPE_MESSAGE%>" id="emailWarning" />
 <%
 String BASIC_BROKER_ROLE_ID = BDConstants.BASIC_BROKER_ROLE_ID ; 
 pageContext.setAttribute("BASIC_BROKER_ROLE_ID",BASIC_BROKER_ROLE_ID,PageContext.PAGE_SCOPE);
 String BROKER_ASSISTANT_ROLE_ID = BDConstants.BROKER_ASSISTANT_ROLE_ID ;
 pageContext.setAttribute("BROKER_ASSISTANT_ROLE_ID",BROKER_ASSISTANT_ROLE_ID,PageContext.PAGE_SCOPE);
 String FIRM_REP_ROLE_ID = BDConstants.FIRM_REP_ROLE_ID;
 pageContext.setAttribute("FIRM_REP_ROLE_ID",FIRM_REP_ROLE_ID,PageContext.PAGE_SCOPE);
 String RIA_USER_ROLE_ID = BDConstants.RIA_USER_ROLE_ID  ;
 pageContext.setAttribute("RIA_USER_ROLE_ID",RIA_USER_ROLE_ID,PageContext.PAGE_SCOPE);
 %>
<script type="text/javascript">
<!--
  
	var emailWarning = '<content:getAttribute beanName="emailWarning" attribute="text" filter="true"/>';
	
	$(document).ready(function(){
		var initialEmail = document.forms['myprofilePersonalInfoForm'].elements['externalUserProfile.emailAddress'].value;
		$('#saveMyProfileBtn').click(function(){
			checkEmail(this);
		});
		
		function checkEmail(btn){	
			var changedEmail = document.forms['myprofilePersonalInfoForm'].elements['externalUserProfile.emailAddress'].value;
			if(initialEmail.toLowerCase() == changedEmail.toLowerCase()){
				doProtectedSubmitBtn(document.forms['myprofilePersonalInfoForm'], 'save', btn);
			}else{
				doMyProfileProtectedSubmitBtn(document.forms['myprofilePersonalInfoForm'], 'save', btn);
			}
		}
	});
	
	//Common Function for AJAX Calls
	function ajax_getJSON(actionPath, requstParameters, callbackMethod, btn) {
			$.get(actionPath, requstParameters, function(data) {
				// Call back method
					var parsedData = $.parseJSON(data);
					if (parsedData.sessionExpired != undefined) {
						// session expired.... redirecting to login page
					top.location.reload(true);
				} else {
					callbackMethod(parsedData, btn);
				}
			}, "text");
	}
	 //Callback method for Request status
	function myprofile_emailCheck_callbackMethod(parsedData, btn){
		if(parsedData.Status == "exists"){
				alert(emailWarning);
		}
		doProtectedSubmitBtn(document.forms['myprofilePersonalInfoForm'], 'save', btn);
	}
	
	function doMyProfileProtectedSubmitBtn(form, action, btn){
		var jsonObjparam="";
		var emailId = document.forms['myprofilePersonalInfoForm'].elements['externalUserProfile.emailAddress'].value;
	    jsonObjparam=$.trim(emailId);
	    ajax_getJSON("/do/myprofile/personalInfo?action=checkDuplicateEmail",
	    	{jsonObj:jsonObjparam
	    	}, myprofile_emailCheck_callbackMethod, btn);
	}

//-->
</script>

<c:set var="form" value="${sessionScope.myprofilePersonalInfoForm}" scope="page"/>
<utils:cancelProtection name="myprofilePersonalInfoForm" changed="${form.changed}"
     exclusion="['action']"/>

 <div id="contentFull">
	<layout:pageHeader nameStyle="h1"/>

<c:if test="${form.success}">
	<utils:info contentId="<%=BDContentConstants.MY_PROFILE_SAVE_SUCCESS_MESSAGE_TEXT%>"/> 
</c:if>

<report:formatMessages scope="session"/>

<userprofile:myprofileTab/>
<bd:form action="/do/myprofile/personalInfo" modelAttribute="myprofilePersonalInfoForm" name="myprofilePersonalInfoForm">

<form:hidden path="action"/>
	<c:if test="${not empty myprofilePersonalInfoForm.externalUserProfile}">	
	<div class="BottomBorder">
		<div class="SubTitle Gold Left">
			<strong>
				<span class="style2"><br /></span>
			</strong>
		</div>
		<div class="GrayLT Right">* = Required Field</div>
	</div>
<c:if test="${myprofilePersonalInfoForm.roleId == BASIC_BROKER_ROLE_ID}">
		<c:if test="${not empty myprofilePersonalInfoForm.externalUserProfile.brokerDealerFirm}">
			<div class="label">BD Firm Name:</div>
<div class="inputText">${myprofilePersonalInfoForm.externalUserProfile.brokerDealerFirm.firmName}</div>
		</c:if>
		<c:forEach var="riafirmName" items="${myprofilePersonalInfoForm.sortedRiaFirmNames}" varStatus="status">
			<c:if test="${status.first}">
				<div class="label">View RIA Statements For:</div>
			</c:if>
			<div class="inputText"><c:out value="${riafirmName}"/></div>
		</c:forEach>
</c:if>
<c:if test="${myprofilePersonalInfoForm.roleId == BROKER_ASSISTANT_ROLE_ID}">
		<c:forEach var="firmName" items="${myprofilePersonalInfoForm.sortedBrokerDealerFirmNames}" varStatus="status">
			<c:if test="${status.first}">
				<div class="label">BD Firm Name:</div>
			</c:if>
			<div class="inputText"><c:out value="${firmName}"/></div>
		</c:forEach>
		<c:forEach var="riafirmName" items="${myprofilePersonalInfoForm.sortedRiaFirmNames}" varStatus="status">
			<c:if test="${status.first}">
				<div class="label">View RIA Statements For:</div>
			</c:if>
			<div class="inputText"><c:out value="${riafirmName}"/></div>
		</c:forEach>
</c:if>
<c:if test="${myprofilePersonalInfoForm.roleId == FIRM_REP_ROLE_ID}"> 
		<c:forEach var="firmName" items="${myprofilePersonalInfoForm.sortedBrokerDealerFirmNames}" varStatus="status">
			<c:if test="${status.first}">
				<div class="label">BD Firm Name:</div>
			</c:if>
			<div class="inputText"><c:out value="${firmName}"/></div>
		</c:forEach>
		<c:forEach var="riafirmName" items="${myprofilePersonalInfoForm.sortedRiaFirmNames}" varStatus="status">
			<c:if test="${status.first}">
				<div class="label">View RIA Statements For:</div>
			</c:if>
			<div class="inputText"><c:out value="${riafirmName}"/></div>
		</c:forEach>
</c:if>
<c:if test="${myprofilePersonalInfoForm.roleId == RIA_USER_ROLE_ID}"> 
		<c:forEach var="firmName" items="${myprofilePersonalInfoForm.sortedBrokerDealerFirmNames}" varStatus="status">
			<c:if test="${status.first}">
				<div class="label">BD Firm Name:</div>
			</c:if>
			<div class="inputText"><c:out value="${firmName}"/></div>
		</c:forEach>
		<c:choose>
			<c:when test="${fn:length(myprofilePersonalInfoForm.sortedRiaFirmNames) eq 0}">
				<div class="label">View RIA Statements For:</div>
				<div class="inputText">&nbsp;</div>
			</c:when>
			<c:otherwise>
				<c:forEach var="riafirmName" items="${myprofilePersonalInfoForm.sortedRiaFirmNames}" varStatus="status">
				   <c:if test="${status.first}">
					<div class="label">View RIA Statements For:</div>
				   </c:if>
				   <div class="inputText"><c:out value="${riafirmName}"/></div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
</c:if>
<c:if test="${myprofilePersonalInfoForm.roleId == BROKER_ASSISTANT_ROLE_ID}">
		<div class="label">Financial Representative: </div>
<div class="inputText">${myprofilePersonalInfoForm.financialRepName}</div>
</c:if>
<c:if test="${myprofilePersonalInfoForm.showCompanyName ==true}">
		<div class="label">Company Name:</div>
		<div class="inputText">
<form:input path="externalUserProfile.companyName" maxlength="30" size="30" cssClass="input" />
		</div>
</c:if>
	<div class="label">* First Name:</div>
	<div class="inputText">
<form:input path="externalUserProfile.firstName" maxlength="30" size="30" cssClass="input" />
	</div>
	<div class="label">* Last Name:</div>
	<div class="inputText">
<form:input path="externalUserProfile.lastName" maxlength="30" size="30" cssClass="input" />
	</div>
	<div class="label">* Email:</div>
	<div class="inputText">
<form:input path="externalUserProfile.emailAddress" maxlength="70" size="50" cssClass="input" />
	</div>
	<userprofile:address form="${form}"/>
	<div class="label">* Telephone#:</div> 
	<div class="inputText">
	    <userprofile:phoneNumInput/>
	</div>
	
<c:if test="${myprofilePersonalInfoForm.roleId == RIA_USER_ROLE_ID}"> 
   	<div class="formButton"> 
       <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="save" value="Save" id="saveMyProfileBtn" >
    </div> 
</c:if>
	
<c:if test="${myprofilePersonalInfoForm.roleId != RIA_USER_ROLE_ID}">
   	<div class="formButton"> 
       	<input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="save" value="Save"
	        onclick="return doProtectedSubmitBtn(document.myprofilePersonalInfoForm, 'save', this)">
    </div>
</c:if>
	
    <div class="formButton">
      <input type="button" class="grey-btn back" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='grey-btn back'"
             name="cancel" value="Close"
             onclick="return doCancelBtn(document.myprofilePersonalInfoForm, this)"> 
    </div>
	</c:if>
</bd:form>
</div>

<layout:pageFooter/>
