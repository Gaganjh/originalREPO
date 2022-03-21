<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>

<content:contentBean contentId="<%=BDContentConstants.CANCEL_POP_UP_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="cancelMessage" />
<content:contentBean contentId="<%=BDContentConstants.CANCEL_POP_UP_MESSAGE_EXTERNAL_BROKER%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="infoLoseMessage" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_IDENTIFICATION_VALIDATION_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>"
id="idValidation" />

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
			document.getElementById('no').checked = true;
			return false;
		}
	  }
	  else {
		doProtectedSubmitBtn(frm, action, arguments[2]);
	  }
	  return false;
    }
    
	function toggleOrg(val) {
		if(val == "O") {
			document.getElementById("firmId").value = "";
			document.getElementById("firmName").value = "";
			document.getElementById("firm").style.display = "block";
			document.getElementById("company").style.display = "none";
		}
		else if(val == "I") {
			document.getElementById("companyName").value = "";
			document.getElementById("firm").style.display = "none";
			document.getElementById("company").style.display = "block";
		}
	}
</script>

<style>
#firmLabel {
	width: 150px;
	text-align: right;
	float: left;
}
#firmValue {
	margin-left: 10px;
	margin-top: 0px;
	width: 415px;
	float: left;
}
</style>
<c:set var="form" value="${requestScope.registerBasicBrokerValidationForm}" scope="page"/>

<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>

<div id="content">
	<bd:form action="/do/registerExternalBroker/basicBroker/step1" modelAttribute="registerBasicBrokerValidationForm" name="registerBasicBrokerValidationForm">
	
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
<form:radiobutton onclick="doChangeOption(document.registerBasicBrokerValidationForm, 'step1', this.value)" path="userHasContract" id="yes" value="Yes"/>
				Yes
			</label>
			<label>
<form:radiobutton onclick="doChangeOption(document.registerBasicBrokerValidationForm, 'step1', this.value)" path="userHasContract" id="no" value="No"/>
				No
			</label>
			<br />
		</span>
		</div>
		<div class="BottomBorder">
			<div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="idValidation"/></div>
		</div>
	 <div class="regSection" >
	   <div style="height:1px"> </div>
	   <div class="label">* First Name:</div>
	   <div class="inputText">
			<label>
<form:input path="firstName" maxlength="30" size="30" cssClass="input"/>
			</label>
	   </div>
	   <div class="label">* Last Name:</div>
	   <div class="inputText">
			<label>
<form:input path="lastName" maxlength="30" size="30" cssClass="input"/>
			</label>
			<br/>
	   </div>
	   <div class="label">* Email:</div>
	   <div class="inputText">
			<label>
<form:input path="emailAddress" maxlength="70" cssClass="input"/>
			</label>
			<br />
	   </div>
		<userprofile:address form="${form}"/>
		<div class="label">* Telephone:</div> 
		<div class="inputText">
			<userprofile:phoneNumInput/>
		</div>
	</div>
		<div class="BottomBorder">
			<div class="SubTitle Gold Left"><content:getAttribute attribute="body2Header" beanName="layoutPageBean"/></div>
		</div>
    <div class="regSection" style="overflow:visible">
        <div style="height:1px"> </div>    
		<div class="label">*&nbsp;Who are you affiliated with?<br /></div>
		<div class="inputText">
		  <label>
<form:radiobutton onclick="toggleOrg(this.value)" path="partyType" id="org" value="O"/>
			  Broker Dealer Firm
		  </label>
		  <label>
<form:radiobutton onclick="toggleOrg(this.value)" path="partyType" id="indiv" value="I"/>
			  Independent
		  </label>
		   <br/>
		</div>
		<br />
		<c:choose>
			<c:when test="${registerBasicBrokerValidationForm.partyType == 'O'}">
				<c:set var="firmStyle" value="display:block"/>
				<c:set var="companyStyle" value="display:none"/>
			</c:when>
			<c:when test="${registerBasicBrokerValidationForm.partyType == 'I'}">
				<c:set var="firmStyle" value="display:none"/>
				<c:set var="companyStyle" value="display:block"/>
			</c:when>
			<c:otherwise>
				<c:set var="firmStyle" value="display:none"/>
				<c:set var="companyStyle" value="display:none"/>
			</c:otherwise>
		</c:choose>
		<div id="firm" style="${firmStyle}">
			<div id="firmLabel">
				<label><br />
				* Firm Name
				</label>
			</div>
			<div id="firmValue">
				<label>
					<utils:firmSearch firmName="firmName" firmId="firmId"/>
				</label>
				<span style="color: #9B8A76;"><content:getAttribute attribute="body2" beanName="layoutPageBean"/></span>
			</div>  
		</div>
		<div id="company" style="${companyStyle}">
			<div class="label">Company Name</div>
				<div class="inputText" style="float:left;margin-left:10px;">
<span align="left"><form:input path="companyName" maxlength="30" cssClass="input" id="companyName"/></span>
					<br />
					<content:getAttribute attribute="body3" beanName="layoutPageBean"/>
				</div>
		</div>
		<div style="clear:left;"></div>
	 </div>
	    <div class="formButtons">
	     <c:choose>
	      <c:when test="${not registerBasicBrokerValidationForm.disabled}"> 
		      <div class="formButton"> 
	       		<input type="button" class="blue-btn next" 
				onmouseover="this.className +=' btn-hover'" 
		        onmouseout="this.className='blue-btn next'"
		        name="continue" value="Continue"
		        onclick="return doBeforeSubmit(document.registerBasicBrokerValidationForm, 'continue', this)">
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
		             onclick="return doRegistrationCancel(document.registerBasicBrokerValidationForm, '<content:getAttribute id='cancelMessage' attribute='text' />', this)"> 
		    </div>
		 </div>   
	</bd:form>
	<layout:pageFooter/>
</div>

