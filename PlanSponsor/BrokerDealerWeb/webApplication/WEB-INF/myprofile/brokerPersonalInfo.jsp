<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>
<%@taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<content:contentBean contentId="<%=BDContentConstants.PERSONAL_INFO_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>"
id="personalInfoTitle" />

<c:set var="form" value="${sessionScope.myprofileBrokerPersonalInfoForm}" scope="page"/>

<c:set var="formName" value="myprofileBrokerPersonalInfoForm" scope="page"/>
<utils:cancelProtection name="myprofileBrokerPersonalInfoForm" changed="${form.changed}"
     exclusion="['action']"/>

<div id="contentFull">
	<layout:pageHeader nameStyle="h1"/>

<c:if test="${not empty param.activation}">
 <utils:info contentId="<%=BDContentConstants.BROKER_VERIFY_PERSONAL_INFO%>"/> 
</c:if>

<c:if test="${form.success}">
	<utils:info contentId="<%=BDContentConstants.MY_PROFILE_SAVE_SUCCESS_AND_EMAIL_SENT_MESSAGE_TEXT%>"/> 
</c:if>

<report:formatMessages scope="session"/>

<userprofile:myprofileTab/>

<bd:form action="/do/myprofile/brokerPersonalInfo" modelAttribute="myprofileBrokerPersonalInfoForm" name="myprofileBrokerPersonalInfoForm">

<form:hidden path="action"/>
<form:hidden path="primaryBrokerPartyId"/>
	<div class="BottomBorder">
		<div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="personalInfoTitle"/></div>
		<div class="SubTitle Gold Left">
			<strong>
				<span class="style2"><br /></span>
			</strong>
		</div>
		<div class="GrayLT Right">* = Required Field</div>
	</div>
	<div class="label">* Profile First Name:</div>
	<div class="inputText">
<form:input path="profileFirstName" maxlength="30" size="30" cssClass="input"/>
	</div> 
	<div class="label">* Profile Last Name:</div>
	<div class="inputText">
<form:input path="profileLastName" maxlength="30" size="30" cssClass="input"/>
	</div> 

	<userprofile:brokerEntityList name="brokerEntityProfilesList" form="${ form}" formName="${formName}"/>

   	<div class="formButton"> 
       <input type="button" class="blue-btn next" 
			onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="continue" value="Submit"
	        onclick="return doProtectedSubmitBtn(document.myprofileBrokerPersonalInfoForm, 'save', this)">
        </div> 

    <div class="formButton">
      <input type="button" class="grey-btn back" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='grey-btn back'"
             name="cancel" value="Close"
             onclick="return doCancelBtn(document.myprofileBrokerPersonalInfoForm, this)"> 
    </div>

</bd:form>
</div>

<layout:pageFooter/>
