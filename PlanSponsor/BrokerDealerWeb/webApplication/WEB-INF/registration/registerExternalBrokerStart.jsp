<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script type="text/javascript" >
var submitted = false;
function doProtectedSubmit1(frm,action,obj) {
	var hrefValue = null;
	if (obj != undefined && obj != null) {
		hrefValue = obj.getAttribute("href");
		obj.removeAttribute('href');
	}
	if(!submitted) {
		  submitted = true;
		 // frm._csrf.value=csrf;
		 
		  frm.action.value=action;
		  try {
			  frm.submit();
		  }
		  catch (e) {
			  // anything going bad, recover the button and allow submit
			  if (hrefValue != null) {
				  obj.setAttribute('href', hrefValue);
			  }
	  		  submitted = false;
		  }
	}
	else {
		window.status = "Transaction already in progress. Please wait.";
	}
	return false;

}

</script>


<bd:form action="/do/registerExternalBroker/start" method="GET" cssClass="display:inline" modelAttribute="registerExternalBrokerStartForm" name="registerExternalBrokerStartForm">

	<input type="hidden" name="action"/>
</bd:form>
<layout:rightColumnLayer layerName="layer1"/>
<layout:rightColumnLayer layerName="layer2"/>

<div id="content">
    <layout:pageHeader nameStyle="h1"/>
    <report:formatMessages scope="session"/>
	<div class="RegBox" style="overflow: auto">
	  <div id="otherBox">
	    <h5>
	    <content:getAttribute attribute="body2" beanName="layoutPageBean"/>	    
	    </h5>
	    <div style="margin-top:15px">
	    <label>
	     <input name="Register" type="button" class="regButton" id="Register" onclick="return doProtectedSubmit1(document.registerExternalBrokerStartForm, 'start', this)"  value="Register Now" />
	     </label>
	    </div>
	  </div>
	  <div id="frBox">
	    <h5>
	     <content:getAttribute attribute="body1" beanName="layoutPageBean"/>
	    </h5>
	    <div style="margin-top:15px">
	    <label>
	    <input name="Login" type="button" class="regButton" id="Login" onclick="document.location='/do/migration/step1'" value="Login Now" />
	    </label>
	    </div>
	  </div>
      <br class="clearFloat" />
	</div>
<layout:pageFooter/>
</div>

