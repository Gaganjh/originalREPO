
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ attribute name="readOnly" type="java.lang.Boolean" required="false" rtexprvalue="true"%>

<script type="text/javascript">
<%-- 
This method is used to automatically tab from one field to 
another once the required length of characters entered.
--%> 
function phoneAutoTab(elem, e) {
	if(e.keyCode) {
		code = e.keyCode;
	}
	else if(e.which) {
		code = e.which;
	}
	if(code != 9 && code != 16) {
		if(elem == elem.form.elements["phoneNumber.areaCode"] && elem.value.length == 3) {
			elem.form.elements["phoneNumber.number1"].focus();
		}
		else if(elem == elem.form.elements["phoneNumber.number1"] && elem.value.length == 3) {
			elem.form.elements["phoneNumber.number2"].focus();
		}
	}
}
</script>

 <form:input path="phoneNumber.areaCode" cssClass="input" size="3" maxlength="3" 
 		onkeyup="phoneAutoTab(this, event)" disabled="${readOnly}"/>
 <form:input path="phoneNumber.number1" cssClass="input" size="3" maxlength="3" 
 		onkeyup="phoneAutoTab(this, event)" 
 		onclick="this.focus()"
 		disabled="${readOnly}"/>
 <form:input path="phoneNumber.number2" cssClass="input" size="4" maxlength="4" 
 		onkeyup="phoneAutoTab(this, event)" 
 		onclick="this.focus()"
 		disabled="${readOnly}"/>
