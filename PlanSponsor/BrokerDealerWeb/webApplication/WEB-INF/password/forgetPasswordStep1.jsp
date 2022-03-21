<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<%@page import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<script type="text/javascript">
<!--
function doOnload() {
    var uidBox=document.forms['forgetPasswordForm'].userId;
    if (uidBox) {
        try {
            uidBox.focus();
        } catch (e) {
        }
    }    
}
//-->
</script>

<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>
<div id="content">
<bd:form action="/do/forgetPassword/step1" method="post" name="forgetPasswordForm" modelAttribute="forgetPasswordForm">

<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
	<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
	<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>
<report:formatMessages scope="session"/>
<div class="BottomBorder">
<div class="SubTitle Gold Left"><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></div>
<div class="GrayLT Right">* = Required Field</div></div>

   <input type="hidden" name="action" value="continue">  
   <div class="OverFlow">
	<div class="label">* Username:</div>
	<div class="inputText">
	  <label>
<form:input path="userId" maxlength="32"/> <br>
	  </label>
	
	  <br />
	</div>
  </div>
  <div class="formButtons">
        <div class="formButton">
          <input type="button" class="blue-btn next" 
                 onmouseover="this.className +=' btn-hover'" 
                 onmouseout="this.className='blue-btn next'"
                 name="continue" value="Continue"
                 onclick="return doProtectedSubmitBtn(document.forgetPasswordForm, 'continue', this)"> 
        </div>
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

