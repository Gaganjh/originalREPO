<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib tagdir="/WEB-INF/tags/utils" prefix="utils" %>

<%@tag import="com.manulife.pension.bd.web.content.BDContentConstants"%>

<%@attribute name="name" type="java.lang.String" required="true"%>
<%@attribute name="changed" type="java.lang.String" required="true"%>
<%@attribute name="exclusion" type="java.lang.String" required="false"%>

<% %>

<c:if test="${empty exclusion}">
   <c:set var="exclusion" value="[]"/>
</c:if>

<content:contentBean contentId="<%=BDContentConstants.CANCEL_POP_UP_MESSAGE%>" 
    type="<%=BDContentConstants.TYPE_MESSAGE%>" id="cancelMessage" />

<script type="text/javascript">
<!--
	var msg = "<content:getAttribute id='cancelMessage' attribute='text' filter='true' />";
	var changed = ${changed};

	function doCancel(frm) {
		if (isChanged()) {
			if (!confirm(msg)) {
				return false;
			}
		}
	  var linkObj = document.getElementById('cancelAnchor');
	  doProtectedSubmit(frm, 'cancel', linkObj);
	  return false;
    }

	// workaround for firefox
	function formChangeIndcator(frm) {
		if (typeof frm.changed == "undefined") {
			return false;
		} else {
			return frm.changed.value.toString() == 'true' ;
		}  
	}
	
	function doSubmitCheckChange(frm, action, btn) {
		btn.disabled = true;
		if (formChangeIndcator(frm) || isChanged()) {
			if (!confirm(msg)) {
				btn.disabled=false;
				return false;
			}
		}
		doProtectedSubmitBtn(frm, action, btn);
	}
	
	function doCancelBtn(frm, btn) {
		btn.disabled = true;
		if (formChangeIndcator(frm) || isChanged()) {
			if (!confirm(msg)) {
				btn.disabled=false;
				return false;
			}
		}
	  doProtectedSubmit(frm, 'cancel', btn);
	  return false;
    }
	
	function isChanged() {
		return changed || formIsDirty(document.forms['${name}'], ${exclusion});
	}
	
	registerTrackChangesFunction(isChanged, '<content:getAttribute id='cancelMessage' attribute='text' filter='true' />');
	
	if (window.addEventListener) {
		window.addEventListener('load', protectLinksFromCancel, false);
	} else if (window.attachEvent) {
		window.attachEvent('onload', protectLinksFromCancel);
	}
//-->
</script>
    