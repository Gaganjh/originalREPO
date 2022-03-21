<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

        
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>

<content:contentBean contentId="<%=BDContentConstants.CANCEL_POP_UP_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="cancelMessage" />
<content:contentBean contentId="<%=BDContentConstants.CANCEL_POP_UP_MESSAGE_EXTERNAL_BROKER%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="infoLoseMessage" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_IDENTIFICATION_VALIDATION_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>"
id="idValidation" />
<content:contentBean contentId="<%=BDContentConstants.EXTERNAL_BROKER_EMAIL_TEXT%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="emailText" />
<content:contentBean contentId="<%=BDContentConstants.EXTERNAL_BROKER_CONTRACT_TEXT%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="contractText" />
<content:contentBean contentId="<%=BDContentConstants.EXTERNAL_BROKER_SSN_TEXT%>" type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="ssnText" />

<script type="text/javascript">
function doChangeOption(frm, action, value) {
	   if (value != frm.defaultOption.value) {
		   return doBeforeSubmit(frm, action, value)
	   }  else {
		   return false;
	   }
}

function doBeforeSubmit(frm, action) {
	  var exclusionArr = new Array();
	  exclusionArr[0] = "action";
	  exclusionArr[1] = "userHasContract";
   	  var changed = frm.changed.value;
	  if(action == "step1") {
		var returnVal = true; 
		if(isDirty(frm,exclusionArr) || changed == 'true') {
			returnVal = confirm("<content:getAttribute id='infoLoseMessage' attribute='text' />");
		}
		if(returnVal) {
			window.location = "/do/registerExternalBroker/start?action=step1&userHasContract=" + arguments[2];
		}
		else {
			document.getElementById('yes').checked = true;
			return false;
		}
	  }
	  else {
		doProtectedSubmitBtn(frm, action, arguments[2]);
	  }
	  return false;  
    }
</script>

<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>

<c:set var="form" value="${requestScope.registerBrokerValidationForm}" scope="page"/>

<div id="content">
	<bd:form action="/do/registerExternalBroker/broker/step1" modelAttribute="registerBrokerValidationForm" name="registerBrokerValidationForm">
	
		<input type="hidden" name="action"/>
<input type="hidden" name="changed"/>
		<input type="hidden" name="defaultOption" value="${e:forHtmlAttribute(form.userHasContract)}">
		<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
		<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
		<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>
		<report:formatMessages scope="session"/>

		<div class="BottomBorder">
			<div class="SubTitle Gold Left"><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></div>
			<div class="GrayLT Right">* = Required Field</div>
		</div>
		<div class="regSection">
		<p class="style1"><content:getAttribute attribute="body1" beanName="layoutPageBean"/> </p>
		<span class="RadioLabel">* Contract</span>
		<span class="RadioValues">
			<label>
<form:radiobutton onclick="return doChangeOption(document.registerBrokerValidationForm, 'step1', this.value)" path="userHasContract" id="yes" value="Yes" />
				Yes
			</label>
			<label>
<form:radiobutton onclick="return doChangeOption(document.registerBrokerValidationForm, 'step1', this.value)" path="userHasContract" id="no" value="No" />
				No
			</label>
			<br />
		</span>
		</div>
		<div class="BottomBorder">
			<div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="idValidation"/></div>
		</div>
		<div class="regSection">
	        <div style="height:1px"> </div>    		
		   <div class="label">* Profile First Name:</div>
		   <div class="inputText">
				<label>
<form:input path="firstName" maxlength="30" size="30" cssClass="input"/>
				</label>
		   </div>
		   <div class="label">* Profile Last Name:</div>
		   <div class="inputText">
				<label>
<form:input path="lastName" maxlength="30" size="30" cssClass="input"/>
				</label>
				<br />
		   </div>
		   <div class="label">* Email:</div>
		   <div class="inputText">
				<label>
<form:input path="emailAddress" maxlength="70" cssClass="input"/>
				</label>
				<br />
				<content:getAttribute attribute="text" beanName="emailText"/>
		   </div>
		   <div class="label">* Contract Number:</div>
		   <div class="inputText">
				<label>
<form:input path="contractNumber" maxlength="7" size="8" cssClass="input"/>
				</label>
				<br />
				<content:getAttribute attribute="text" beanName="contractText"/>
		   </div>	   
	   		<userprofile:ssnTaxId/>
   		</div>  <!-- regSection -->
   		
   		<br class="clearFloat"/>
   		<div class="formButtons">   		
	   	<div class="formButton"> 
	       <input type="button" class="blue-btn next" 
				onmouseover="this.className +=' btn-hover'" 
		        onmouseout="this.className='blue-btn next'"
		        name="continue" value="Continue"
		        onclick="return doBeforeSubmit(document.registerBrokerValidationForm, 'continue', this)">
	    </div> 

	    <div class="formButton">
	      <input type="button" class="grey-btn back" 
	             onmouseover="this.className +=' btn-hover'" 
	             onmouseout="this.className='grey-btn back'"
	             name="cancel" value="Cancel"
	             onclick="return doRegistrationCancel(document.registerBrokerValidationForm, '<content:getAttribute id='cancelMessage' attribute='text' />', this)"> 
	    </div>
	    </div>
	</bd:form>

<layout:pageFooter/>
</div>

