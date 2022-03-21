<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>

<% 
UserProfile userProfile =(UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>


<c:if test="${addEditUserForm.fromTPAContactsTab ==true}">
<tr class="datacell1">
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	<td colspan="6" align="center">
	
	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<tr valign="top">
	 		<td width="46%"><strong>Contract Number</strong></td>
	    	<td colspan="3"><strong class="highlight">${userProfile.currentContract.contractNumber}</strong>
	            </td>
	  	</tr>
	
        <tr valign="top">
     		<td width="46%"><strong>Contract Name</strong></td>
        	<td colspan="3"><strong class="highlight">${userProfile.currentContract.companyName}</strong>
            </td>
  		</tr>
  		
  		<tr valign="center">
	 		<td width="46%"><strong>Signature Received - Authorized Signer</strong></td>
	    	<td align="left" nowrap>
		    		<render:yesno property="addEditUserForm.signatureReceivedAuthSigner" defaultValue="no" style="c" />
	    	</td>
	  	</tr>
	
        <tr valign="top">
     		<td width="46%"><strong>TPA Contact</strong></td>
        	<td align="left" nowrap>
			<c:set  var="tpaManager" value='${tpaum}' />										<%-- CL 110473 --%>
		    		<render:yesno property="addEditUserForm.primaryContact" defaultValue="no" style="c" />
        	</td>
  		</tr> 
	</table>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</tr>
</c:if>
