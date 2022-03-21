<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>

<content:contentBean contentId="<%=BDContentConstants.CANCEL_POP_UP_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="cancelMessage" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_IDENTIFICATION_VALIDATION_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>"
id="idValidation" />

<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>

<div id="content">
	<bd:form action="/do/registerBrokerAssistant/step1" modelAttribute="registerBrokerAssistantStep1Form" name="registerBrokerAssistantStep1Form">
	
		<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
		<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
		<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>
		<report:formatMessages scope="session"/>
		<div class="BottomBorder">
			<div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="idValidation"/></div>
			<div class="GrayLT Right">* = Required Field</div>
		</div>
		<input type="hidden" name="action"/>
		<div class="regSection">
	        <div style="height:1px"> </div>    
			
			<div class="OverFlow">
				<div class="label">* Last Name:</div>
				<div class="inputText">
<form:input path="lastName" maxlength="30" size="30" cssClass="input"/>
				</div>
				<div class="label">* Financial Representative Last Name:</div> 
				<div class="inputText">
<form:input path="supervisorLastName" maxlength="30" size="30" cssClass="input"/>
				</div>
			</div>
		</div> <!-- regSection -->
		<br class="clearFloat"/>
		<div class="formButtons">		
		  <c:choose>
			<c:when test="${not registerBrokerAssistantStep1Form.disabled}">
			   	<div class="formButton"> 
			       <input type="button" class="blue-btn next" 
						onmouseover="this.className +=' btn-hover'" 
				        onmouseout="this.className='blue-btn next'"
				        name="continue" value="Continue"
				        onclick="return doProtectedSubmitBtn(document.registerBrokerAssistantStep1Form, 'continue', this)">
		        </div> 
		     </c:when>
	        <c:otherwise>      
				<div class="formButton">
				  <input type="button" class="disabled-grey-btn next" 
						 name="continue" value="Continue"
						 disabled="disabled">
				</div> 
	      </c:otherwise>
	      </c:choose>
		    <div class="formButton">
		      <input type="button" class="grey-btn back" 
		             onmouseover="this.className +=' btn-hover'" 
		             onmouseout="this.className='grey-btn back'"
		             name="cancel" value="Cancel"
		             onclick="return doRegistrationCancel(document.registerBrokerAssistantStep1Form, '<content:getAttribute id='cancelMessage' attribute='text' />', this)"> 
		    </div>
		</div>
	</bd:form>  
<layout:pageFooter/>
</div>

