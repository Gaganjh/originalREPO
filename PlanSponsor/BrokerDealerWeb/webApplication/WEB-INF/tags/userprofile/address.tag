<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="form" type="com.manulife.pension.ezk.web.ActionForm" required="true" rtexprvalue="true"%>
<%@ attribute name="readOnly" type="java.lang.Boolean" required="false" rtexprvalue="true"%>

<c:set var="disabled" value="false"/>
<c:if test="${not empty readOnly &&  readOnly}">
  <c:set var="disabled" value="true"/>
</c:if>
<%-- 
   This tag is used to display address fields.
--%>

<script type="text/javascript">
<%-- 
This method is used to toggle the state and zip fields
based on the selected country.
--%> 
function toggleStateAndZip(elem) {
	if(elem.value == "USA") {
		document.getElementById("otherState").value = "";
		document.getElementById("otherZipCode").value = "";

		document.getElementById("usStateDiv").style.display = "inline";
		document.getElementById("otherStateDiv").style.display = "none";
		document.getElementById("usZip").style.display = "inline";
		document.getElementById("otherZip").style.display = "none";
	}
	else {
		document.getElementById("state").selectedIndex = 0;
		document.getElementById("zipCode1").value = "";
		document.getElementById("zipCode2").value = "";

		document.getElementById("usStateDiv").style.display = "none";
		document.getElementById("otherStateDiv").style.display = "inline";
		document.getElementById("usZip").style.display = "none";
		document.getElementById("otherZip").style.display = "inline";
	}
}
</script>
<c:choose>
	<c:when test="${form.address.country ==''}">
		<c:set var="USDivStyle" value="display:inline"/>
		<c:set var="otherDivStyle" value="display:none"/>
	</c:when>
	<c:when test="${form.address.country == null}">
		<c:set var="USDivStyle" value="display:inline"/>
		<c:set var="otherDivStyle" value="display:none"/>
	</c:when>
	<c:when test="${form.address.country == 'USA'}">
		<c:set var="USDivStyle" value="display:inline"/>
		<c:set var="otherDivStyle" value="display:none"/>
	</c:when>
	<c:otherwise>
		<c:set var="USDivStyle" value="display:none"/>
		<c:set var="otherDivStyle" value="display:inline"/>
	</c:otherwise>
</c:choose>

<div class="label">* Mailing Address 1:</div>
<div class="inputText">
	<form:input styleId="address1" cssClass="input" path="address.address1" size="30" maxlength="30" disabled="${disabled}"/>
</div>   
<div class="label">Address 2:</div>
<div class="inputText">
	<form:input styleId="address2" cssClass="input" path="address.address2" size="30" maxlength="30" disabled="${disabled}"/>
</div>
<div class="label">* City:</div>
<div class="inputText">
	<form:input styleId="city" cssClass="input" path="address.city" size="20" maxlength="25" disabled="${disabled}"/>
</div>
<div id="usStateDiv" style="${USDivStyle}" >
	<div class="label">* State:</div>
	<div class="inputText">
	  <form:select id="state" path="address.usState"  disabled="${disabled}">
		 <form:options items="${form.address.states}" itemLabel="label" itemValue="value"/>
	  </form:select>	
	</div>
</div>
<div id="otherStateDiv" style="${otherDivStyle}">
	<div class="label">State:</div>
	<div class="inputText">
		<form:input id="otherState" cssClass="input" path="address.otherState" size="2" maxlength="2"  disabled="${disabled}"/>
	</div>
</div>
<div id="usZip" style="${USDivStyle}">
	<div class="label">* Zip Code:</div>
	<div class="inputText">
		<form:input id="zipCode1" cssClass="input" path="address.usZipCode1" size="5" maxlength="5"  disabled="${disabled}"/>
		<form:input id="zipCode2" cssClass="input" path="address.usZipCode2" size="4" maxlength="4"  disabled="${disabled}"/>
	</div>
</div>
<div id="otherZip" style="${otherDivStyle}">
	<div class="label">Zip Code:</div>
	<div class="inputText">
		<form:input id="otherZipCode" cssClass="input" path="address.otherZipCode" size="10" maxlength="10"  disabled="${disabled}"/>
	</div>
</div>
<div class="label">* Country:</div>
<div class="inputText">
	  <form:select path="address.country" onchange="toggleStateAndZip(this)"  disabled="${disabled}">
		<form:options items="${form.address.countries}" itemLabel="label"  itemValue="value"/> 
	  </form:select>	
</div>
