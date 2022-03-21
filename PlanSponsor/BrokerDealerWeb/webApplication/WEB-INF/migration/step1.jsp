<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>

<script type="text/javascript">
<!--
function doOnload() {
    var userName=document.forms['migrationForm'].userName;
    if (userName) {
        try {
        	userName.focus();
        } catch (e) {
        }
    }    
}
//-->
</script>

<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>

<div id="content">
<bd:form action="/do/migration/step1" modelAttribute="migrationForm" name="migrationForm">
<input type="hidden" name="action"/>
	<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
	<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
	<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>

	<report:formatMessages scope="request"/>
    	<div class="BottomBorder">
			<div class="SubTitle Gold Left"><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></div>
			<div class="GrayLT Right">* = Required Field</div>
		</div>
		<div class="regSection">
		<p><content:getAttribute attribute="body1" beanName="layoutPageBean"/></p>
		<div class="label" style="margin-top:8px">* Username:</div>
		<div class="inputText" style="margin-top:22px">
			<label>
				<input type="text" name="userName" maxlength="40" style="width:150px" autocomplete="off"/>
			</label>
		</div>
		<div class="label" style="margin-top:18px">* Password:</div>
		<div class="inputText">
			<label>
		    	<input type="password" name="password" maxlength="40" style="width:150px"/>
		    </label>
		</div>
       </div>
       <br class="clearFloat"/>
	    <div class="formButtons">
	   	<div class="formButton"> 
	       <input type="button" class="blue-btn next" 
				onmouseover="this.className +=' btn-hover'" 
		        onmouseout="this.className='blue-btn next'"
		        name="continue" value="Continue"
		        onclick="return doProtectedSubmitBtn(document.migrationForm, 'continue', this)">
	    </div> 
	
	    <div class="formButton">
	      <input type="button" class="grey-btn back" 
	             onmouseover="this.className +=' btn-hover'" 
	             onmouseout="this.className='grey-btn back'"
	             name="cancel" value="Cancel"
	             onclick="return doProtectedSubmitBtn(document.migrationForm, 'cancel', this)"> 
	    </div>
	    </div>
</bd:form>  
<layout:pageFooter/>
</div>

