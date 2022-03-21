<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="psw" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<script type="text/javascript">


function doSubmit(action) {
	document.beneficiaryTransferForm.action.value=action;
	document.beneficiaryTransferForm.submit();
}

/**
 * Opens up a new window and perform the same request again (with printFriendly
 * parameter.
 */
function doPrint()
{
  var reportURL = new URL();
  reportURL.setParameter("task", "print");
  reportURL.setParameter("printFriendly", "true");
  reportURL.setParameter("action","print");
  window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
}

</script>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>


	<ps:form method="post" modelAttribute="beneficiaryTransferForm" name="beneficiaryTransferForm"  action="/do/tools/beneficiaryTransfer/">
<input type="hidden" name="validContract" value="false"/>
<input type="hidden" name="action"/>
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
                      <td width="41"><img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1"> </td>
<td width="417">Beneficiary information has been transferred from contract ${beneficiaryTransferForm.oldContract}(${beneficiaryTransferForm.oldContractName}) to contract ${beneficiaryTransferForm.newContract}(${beneficiaryTransferForm.newContractName})</td>
                      </tr><tr>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1"> </td>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1"> </td>
                      </tr>

                      
                      <tr>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1"> </td>
<td width="417">Total number of participants transferred: ${beneficiaryTransferForm.recordCount} </td>
                      <td><img src="/assets/unmanaged/images/s.gif" width="1" border="0" height="1"> </td>
                      </tr>
                 </tbody>
			</table>

                    
                      <div style="display: block;" id="basic">
                      
                      </div>
                    </div>
			</td>
			</tr>
		</tbody>
		</table>
        
        
        <c:if test="${empty param.printFriendly }" >
	 		<table width="615"><!-- start buttons -->
	 			<tbody>

	               	<tr>
	                 	<td><img src="/assets/unmanaged/images/s.gif" width="30" border="0" height="1"><br><br><br><br><br><br><br><br><br><br></td>
	                 	<td>


							<br>
							  <table width="700" border="0" cellpadding="0" cellspacing="0">
        					<tbody>
        					  <tr>

<td align="right"><input type="button" onclick="return doSubmit('continue')" name="button" class="button134" id="continue" value="continue"/></td>

                             </tr>
						  </tbody></table>  
	             		</td>
	             	</tr>
	        </tbody></table>
	        </c:if></p>
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
		<c:if test="${not empty param.printFriendly }" >
	<content:contentBean
	contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure" />

		<table width="760" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="100%">
					<content:getAttribute
					beanName="globalDisclosure" attribute="text" />
				</td>
			</tr>
		</table>
</c:if>
