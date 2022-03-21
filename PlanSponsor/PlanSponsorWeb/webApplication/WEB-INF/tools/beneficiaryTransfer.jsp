<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="psw" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="java.text.MessageFormat" %>

<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/censusValidation.js"></script>

<script type="text/javascript">

function doConfirmAndSubmit() {
    // only there is change, do confirm
    // Retrieve the field
    if (document.getElementById('oldContractId').value != "") {
    	if (${beneficiaryTransferForm.validContract}) {
    	    var message=document.getElementById('popUp').value;		
    		var field = document.getElementById('oldContractId');
    			  
    	    var myRegExp = new RegExp("\{+[0]+\}","g"); 
message = message.replace(myRegExp,field.value+'(${beneficiaryTransferForm.oldContractName})');
    	  	field = document.getElementById('newContractId');
    	  	myRegExp = new RegExp("\{+[1]+\}","g"); 
message = message.replace(myRegExp,field.value+'(${beneficiaryTransferForm.newContractName})');
    	  
    		    if (confirm(message)) {
    		       doSubmit("transfer");
    		       return true;
    		    } else {
    		       return false;
    		    }
    	 }    	
    } else {
    	return false;
    }
       
}

function doSubmit(action) {
	document.beneficiaryTransferForm.action.value=action;
	document.beneficiaryTransferForm.submit();
}
window.onload = doConfirmAndSubmit; 
</script>

<%--Error/Warning box to populate Error message--%>							
<table width="760" border="0" cellspacing="0" cellpadding="1">
	<tr>
		<td width="760" valign="top">
			<table>
					<tr>
					<td width="21"/>
						<td colspan="2">
						<div class="messagesBox"><%-- Override max height if print friendly is on so we don't scroll --%>
						<ps:messages scope="session" 
							maxHeight="${param.printFriendly ? '1000px' : '100px'}"
							suppressDuplicateMessages="true" />
						</div>
						</td>
					</tr><tr><td colspan="2">&nbsp;</td></tr>
				</table>
		</td>
	</tr>
</table>
<ps:form method="post" modelAttribute="beneficiaryTransferForm" name="beneficiaryTransferForm" action="/do/tools/beneficiaryTransfer/">
<form:hidden path ="message" id="popUp"/>
<input type="hidden" name="action"/>
<form:hidden path="validContract" value="false"/>
 	 <table width="760" border="0" cellpadding="0" cellspacing="0">
    <tbody>
        <tr>

          <td>
            <p>
            <table width="500" border="0" cellpadding="0" cellspacing="0">
              <tbody>
              <tr>
                <td width="15"><img src="/assets/unmanaged/images/s.gif" width="30" border="0" height="1"></td>
                <td valign="top" width="609"><br><br>
                  
                    
                  <div style="display: block;" id="basic">
                  <table width="500" border="0" cellpadding="0" cellspacing="0">

                    <tbody>
                    <tr>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1"> </td>
                      <td width="150">Transfer from old contract #</td>
                      <td width="26" align="right"> <ps:fieldHilight name="oldContract" displayToolTip="true"/> </td>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1"> </td>
<td width="300"> <form:input path="oldContract" maxlength="30" id="oldContractId"/>
                      <c:set var="myTest" value="oldContract"/>
                      
                    </td>

                      <td><img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1"> </td>
                    </tr>
                      
                      <tr>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1"> </td>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1"> </td>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1"> </td>
                      </tr>

                      
                      <tr>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1"> </td>
                      <td width="150">Transfer to new contract #</td>
                      <td width="26" align="right"></td>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1"> </td>
<td>${e:forHtmlContent(beneficiaryTransferForm.newContract)}</td>
<form:hidden  path="newContract" id="newContractId"/>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1"> </td></tr>

                      
                 </tbody>
			</table>
                    
                      <div style="display: block;" id="basic">
                      
                      </div>
                    </div>
			</td>
			</tr>
		</tbody>
		</table>
	 		<table width="615"><!-- start buttons -->
	 			<tbody>
	               	<tr>
	                 	<td><img src="/assets/unmanaged/images/s.gif" width="30" border="0" height="1"><br><br><br><br><br><br><br><br><br><br></td>
	                 	<td>

							<br>
							  <table width="700" border="0" cellpadding="0" cellspacing="0">
        					<tbody>
        					  <tr>
<td align="left"> <input type="button" onclick="return doSubmit('cancel');" name="button" class="button134" id="canceId" value="cancel"/></td>


<td align="left"><input type="button" onclick="return doSubmit('contractValidate');" name="button" class="button134" id="transfer" value="transfer"/></td>

						      </tr>
						  </tbody></table>  
	             		</td>
	             	</tr>

	        </tbody></table></p>
		</td>
	</tr>
</tbody></table>

        


 </ps:form>
 
 <!-- footer table -->
		<BR>
		<table height=25 cellSpacing=0 cellPadding=0 width=760 border=0>
		  <tr>
			  <td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
		              <tr>
					    <td width="30" valign="top">
					        <img src="/assets/unmanaged/images/s.gif" width="30" height="1">
					    </td>
						<td>
						<br>
							<p><content:pageFooter beanName="layoutPageBean"/></p>
		 					<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p> 
		 					<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p> 
		 				</td>
		 			</tr>
				</table>
		    </td>
		    <td width="3%"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
		    <td width="15%" ><img src="/assets/unmanaged/images/s.gif"  height="1"></td>
		  </tr> 
		</table>
 
