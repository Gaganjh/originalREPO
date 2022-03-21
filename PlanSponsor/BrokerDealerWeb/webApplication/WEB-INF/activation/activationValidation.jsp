<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>

<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<script type="text/javascript">
<!--
function doOnload() {
    var userId=document.forms['activationValidationForm'].userId;
    if (userId) {
        try {
        	userId.focus();
        } catch (e) {
        }
    }    
}
//-->
</script>


<c:if test="${(not empty layoutPageBean.body1Header) and (not empty layoutPageBean.body1)}">
<div id="rightColumn1">
	<h1><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></h1>
	<p><content:getAttribute attribute="body1" beanName="layoutPageBean"/></p>
</div>
</c:if>

<div id="content">
<bd:form action="/do/activation/validation" method="post" modelAttribute="activationValidationForm" name="activationValidationForm">


	<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
	<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
	<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>
<report:formatMessages scope="session"/>

<c:if test="${activationValidationForm.brokerPendingWarning}">
  <utils:warning contentId="<%=BDContentConstants.ACTIVATION_PENDING_BROKER_WARNING %>"/>
</c:if>

<c:if test="${activationValidationForm.contextContent!=0}">
    <content:contentBean contentId="${activationValidationForm.contextContent}" 
          type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="contextContent" override="true"/>
    <p><content:getAttribute attribute="text" beanName="contextContent"/></p>
</c:if>

<DIV id="errordivcs"><content:errors scope="session"/></DIV><br>

<c:if test="${not activationValidationForm.disabled}">
  <div class="BottomBorder">
	    <div class="SubTitle Gold Left"></div>
	    <div class="GrayLT Right">* = Required Field</div>
 </div>
  <div class="label">* Username:</div>
  <div class="inputText">
	  <label>
<form:input path="userId" maxlength="20" cssStyle="width:150px"/>
	  </label>
  </div>
  <div class="label">* Password:</div>
  <div class="inputText">
	  <label>
<form:password path="password" maxlength="64" cssStyle="width:150px"/>
	  </label>
  </div>
  </c:if>
  <div class="formButtons">
    <c:choose>
      <c:when test="${not activationValidationForm.disabled}">
      	<div class="formButton"> 
	       <input type="button" class="blue-btn next" 
	        onmouseover="this.className +=' btn-hover'" 
	        onmouseout="this.className='blue-btn next'"
	        name="continue" value="Log In"
	        onclick="return doProtectedSubmitBtn(document.activationValidationForm, 'continue', this)">
        </div> 
       </c:when>
       <c:otherwise>
		    <div class="formButton">
		      <input type="button" class="disabled-grey-btn next" 
		             name="continue" value="Login In"
		             disabled="disabled">
		    </div> 
       </c:otherwise>
    </c:choose>   
    <div class="formButton">
      <input type="button" class="grey-btn back" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='grey-btn back'"
             name="cancel" value="Close"
             onclick="return doProtectedSubmitBtn(document.activationValidationForm, 'cancel', this)"> 
    </div>
   </div> 
   <input type="hidden" name="action">  
 </bd:form>
<layout:pageFooter/>
</div>
