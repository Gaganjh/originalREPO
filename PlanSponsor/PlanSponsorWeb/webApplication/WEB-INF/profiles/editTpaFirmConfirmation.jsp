<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>

<%@ page import="com.manulife.pension.ps.web.Constants" %>

<script type="text/javascript">
var submitted = false;

function doFinish() {
	if (!submitted) {
		submitted = true;
		var url = new URL(window.location.href);
		url.setParameter('action', 'finish');
		window.location.href = url.encodeURL();
	} else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	}
}

function editTpaFirm(href){
	if (!submitted) {
		submitted = true;
		window.location.href=href;
	} else {
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	}
}
</script>

<c:set var="theForm" value="${tpaFirmForm}" scope="request" />

<c:set var="contractAccesses" value="${theForm.contractAccesses}" scope="request" />

<ps:form cssClass="margin-bottom:0;"  method="POST"  action="/do/profiles/editTpaFirm/" name="tpaFirmForm" modelAttribute="tpaFirmForm">
        
<!--<table width="625" border="0" cellspacing="0" cellpadding="0">-->
<table width="700" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
<c:set var="addEditUserForm" value="${tpaFirmForm}" scope="request"/>
			<jsp:include page="contractAccess.jsp" flush="true" />
		</td>
		</tr>
		
	</tbody>
</table>
	  			<BR/>

      			<table cellSpacing=0 cellPadding=0 width="412" border=0>
        	 	<tbody>
              		<tr align="middle">
              		<td>
               			<input type=button class="button100Lg" value="edit this firm"
   	 						   onclick="editTpaFirm('/do/profiles/editTpaFirm/')">
    	 				&nbsp;&nbsp;&nbsp;&nbsp;
<input type="button" onclick="javascript:window.print()" name="print" class="button100Lg" value="print"/>


    	 				&nbsp;&nbsp;&nbsp;&nbsp;
<input type="button" onclick="doFinish()" name="finish" class="button100Lg" value="finish"/>


               		</td>
              		</tr>
            	</tbody>
       			</table>
</ps:form>
