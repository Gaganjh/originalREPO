<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants" %>

<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>

<div id="content">
	<bd:form action="/do/registerExternalBroker/start" method="GET" modelAttribute="registerExternalBrokerStartForm" name="registerExternalBrokerStartForm">
	
		<input type="hidden" name="action"/>
		<h1><content:getAttribute attribute="name" beanName="layoutPageBean"/></h1>
		<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
		<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>
		<div class="BottomBorder">
			<div class="SubTitle Gold Left"><content:getAttribute attribute="body1Header" beanName="layoutPageBean"/></div>
			<div class="GrayLT Right">* = Required Field</div>
		</div>
		<div class="regSection">
		<p class="style1"><content:getAttribute attribute="body1" beanName="layoutPageBean"/> </p>
			<span class="RadioLabel">* Contract</span>
			<span class="RadioValues">
				<label>
<form:radiobutton onclick="return doProtectedSubmit(document.registerExternalBrokerStartForm, 'step1')" path="userHasContract" id="yes" value="Yes" />
					Yes
				</label>
				<label>
<form:radiobutton onclick="return doProtectedSubmit(document.registerExternalBrokerStartForm, 'step1')" path="userHasContract" id="no" value="No" />
					No
				</label>
			</span>
			<br />
		</div>
		<br class="clearFloat"/>
	    <div class="formButtons">
	    <div class="formButton">
	      <input type="button" class="grey-btn back" 
	             onmouseover="this.className +=' btn-hover'" 
	             onmouseout="this.className='grey-btn back'"
	             name="cancel" value="Cancel"
	             onclick="return doProtectedSubmitBtn(document.registerExternalBrokerStartForm, 'cancel', this)"> 
	    </div>
	    </div>
	</bd:form>
<layout:pageFooter/>
</div>

