<%@ taglib tagdir="/WEB-INF/tags/forms" prefix="forms"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

    <%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>

<%@ page import="com.manulife.pension.bd.web.tools.forms.BDFormsForm"%>
<%@ page
	import="com.manulife.pension.content.valueobject.ContentTypeManager"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>



<%-- This jsp includes the following CMA content --%>
<content:contentByType id="bdForms"
	                   type="<%=ContentTypeManager.instance().BD_FORM %>" />

<script type="text/javascript" >                
// set the menu id
function doMenuId(selectedMenuId){ 
	document.forms["bdFormsForm"].elements["selectedMenuId"].value =  selectedMenuId;
	document.forms["bdFormsForm"].submit();
	return;
}
</script>
<bd:form method="POST" action="/do/forms/" modelAttribute="bdFormsForm" name="bdFormsForm">

<input type="hidden" name="selectedMenuId" />
	<DIV id="errordivcs"><report:formatMessages scope="session"/></DIV>
	<div id="summaryBoxFP"></div>
	<h2><content:getAttribute attribute="name" beanName="layoutPageBean"/></h2>
	<p><content:getAttribute attribute="introduction1" beanName="layoutPageBean"/></p>
	<p><content:getAttribute attribute="introduction2" beanName="layoutPageBean"/></p>
	<forms:userMenuTab scope="session"
		menuID="${bdFormsForm.selectedMenuId}" />

</bd:form>
<layout:pageFooter/>

