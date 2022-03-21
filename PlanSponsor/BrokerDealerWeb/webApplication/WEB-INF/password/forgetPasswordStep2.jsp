<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<%-- somehow sup cause problem is IE. Use this as a workaround --%>
<style type="text/css">
span.super {
    font-size: 10px; 
	height: 0;
	line-height: 1;
	vertical-align: baseline;
	_vertical-align: bottom;
	position: relative;
	bottom: 1ex;
}
</style>

<script type="text/javascript">
<!--
function doOnload() {
    var answer1=document.forms['forgetPasswordForm'].answer1;
    if (answer1) {
        try {
        	answer1.focus();
        } catch (e) {
        }
    }    
}
//-->
</script>

<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>


<div id="content">
<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
	<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
	<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>
<report:formatMessages scope="session"/>
<bd:form action="/do/forgetPassword/step2" method="post" modelAttribute="forgetPasswordForm" name="forgetPasswordForm">

   <input type="hidden" name="action">  
<div class="BottomBorder">
  <div class="SubTitle Gold Left"><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></div>
</div>
<div class="OverFlow">
<div class="label"> Username:</div>
<form:hidden path="userId"/>
<div class="inputText">
    <label>
      <c:out value="${forgetPasswordForm.userId}"/>
    </label>
</div>
</div>
<br>
<div class="BottomBorder">
  <div class="SubTitle Gold Left"><content:getAttribute attribute="body2Header" beanName="layoutPageBean"/></div>
  <div class="GrayLT Right">* = Required Field</div><br />
</div>

  <div class="label"> 1<span class="super">st</span>  Challenge Question:</div>
   <div class="inputText">
     <label>
      <c:out value="${forgetPasswordForm.question1}"/>
     </label>
   </div>
  <div class="label">* Your Answer:</div>
  <div class="inputText">
  <label>
<form:password path="answer1" showPassword="true" cssClass="input" maxlength="32"/>
  </label>
  </div>
  <div class="label"> 2<span class="super">nd</span>  Challenge Question:</div>
  <div class="inputText">
     <label>
      <c:out value="${forgetPasswordForm.question2}"/>
     </label> 
   </div>
  <div class="label">* Your Answer:</div>
  <div class="inputText">
  <label>
<form:password path="answer2" showPassword="true" cssClass="input" maxlength="32"/>
  </label>
  </div>
  <div class="formButtons">
  <c:choose>
    <c:when test="${not forgetPasswordForm.disabled}">
    <div class="formButton">
      <input type="button" class="blue-btn next" 
             onmouseover="this.className +=' btn-hover'" 
             onmouseout="this.className='blue-btn next'"
             name="continue" value="Continue"
             onclick="return doProtectedSubmitBtn(document.forgetPasswordForm, 'continue', this)"> 
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
             onclick="return doProtectedSubmitBtn(document.forgetPasswordForm, 'cancel', this)"> 
    </div>
  </div>
</bd:form>
<layout:pageFooter/>
</div>

