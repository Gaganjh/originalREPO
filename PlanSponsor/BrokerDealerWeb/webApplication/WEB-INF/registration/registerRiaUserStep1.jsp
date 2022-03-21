<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@taglib tagdir="/WEB-INF/tags/userprofile" prefix="userprofile" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>

<content:contentBean contentId="<%=BDContentConstants.CANCEL_POP_UP_MESSAGE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>" id="cancelMessage" />
<content:contentBean contentId="<%=BDContentConstants.REGISTRATION_IDENTIFICATION_VALIDATION_SECTION_TITLE%>" type="<%=BDContentConstants.TYPE_MESSAGE%>"
id="idValidation" />
<content:contentBean contentId="<%=BDContentConstants.FIRM_REP_ACCESS_CODE_TEXT%>" type="<%=BDContentConstants.TYPE_LAYOUT_PAGE%>"
id="accessCodeText" />

<script type="text/javascript">
<!--
function doOnload() {
    var firstName=document.forms['registerRiaUserStep1Form'].firstName;
    if (firstName) {
        try {
        	firstName.focus();
        } catch (e) {
        }
    }    
}
//-->
</script>

<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>

<div id="content">
<bd:form action="/do/registerRiaUser/step1" modelAttribute="registerRiaUserStep1Form" name="registerRiaUserStep1Form">

 <input type="hidden" name="action"/>
<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>

<report:formatMessages scope="session"/>

<div class="BottomBorder">
  <div class="SubTitle Gold Left"><content:getAttribute attribute="text" beanName="idValidation"/>
</div>
                        
<div class="GrayLT Right">* = Required Field</div></div>
    <div class="regSection">
	    <div style="height: 1px"> </div>
	    <div class="OverFlow"> 
	      <div class="label"><b>* First Name:</b></div>
		  <div class="inputText">
		     <label>
<form:input path="firstName" maxlength="30" size="30" cssClass="input"/>
		     </label> 
		  </div>   
		  <div class="label"><b>* Last Name:</b></div>
		  <div class="inputText">
		     <label>
<form:input path="lastName" maxlength="30" size="30" cssClass="input"/>
		     </label> 
		  </div>
		  <div class="label"><b>* Email:</b></div>
		  <div class="inputText">
		     <label>
<form:input path="emailAddress" maxlength="70" size="30" cssClass="input"/>
		     </label> 
		  </div>
		  <div class="label"><b>* Telephone #:</b></div> 
		  <div class="inputText">
		      <userprofile:phoneNumInput/>
		  </div>
		  <div class="label"><b>* IARD Number:</b></div>
		  <div class="inputText">  
<form:input path="iardNumber" maxlength="25" size="30" cssClass="input"/>
		  </div>
	  </div>
  </div>
  <br class="clearFloat"/>
   <div class="formButtons">
     <c:choose>
      <c:when test="${not registerRiaUserStep1Form.disabled}"> 
	   	<div class="formButton"> 
	       <input type="button" class="blue-btn next" 
				onmouseover="this.className +=' btn-hover'" 
		        onmouseout="this.className='blue-btn next'"
		        name="continue" value="Continue"
		        onclick="return doProtectedSubmitBtn(document.registerRiaUserStep1Form, 'continue', this)">
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
	             onclick="return doRegistrationCancel(document.registerRiaUserStep1Form, '<content:getAttribute id='cancelMessage' attribute='text' />', this)"> 
	    </div>
	</div>
</bd:form>  
<layout:pageFooter/>
</div>

