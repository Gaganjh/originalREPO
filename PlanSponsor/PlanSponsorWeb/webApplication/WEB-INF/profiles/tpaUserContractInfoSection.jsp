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
	    	<logicext:if name="layoutBean" property="param(task)" op="equal" value="edit">
	    		<logicext:then>
		    	<c:if test="${userProfile.role.TPA}">
		    	<form:radiobutton path="signatureReceivedAuthSigner" disabled="true" value="true"/>&nbsp;yes
		    	</c:if>
		    	<c:if test="${userProfile.role.internalUser}">
		    		<ps:notPermissionAccess permissions="EXTA">
		    		<form:radiobutton path="signatureReceivedAuthSigner" disabled="true" value="true"/>&nbsp;yes
		    		</ps:notPermissionAccess>
			    	<ps:permissionAccess permissions="EXTA">
			    	<form:radiobutton path="signatureReceivedAuthSigner"  value="true"/>&nbsp;yes
			    	</ps:permissionAccess>
		    	</c:if>
		    	</logicext:then>
		    	<logicext:else>
		    		<render:yesno property="addEditUserForm.signatureReceivedAuthSigner" defaultValue="no" style="c" />
		    	</logicext:else>
		    </logicext:if>
	    	</td>
  			<td align="left" nowrap>
  			<logicext:if name="layoutBean" property="param(task)" op="equal" value="edit">
  				<logicext:then>
	  			<c:if test="${userProfile.role.TPA}">
	  			<form:radiobutton path="signatureReceivedAuthSigner" disabled="true" value="false"/>&nbsp;no
	  			</c:if>
	  			<c:if test="${userProfile.role.internalUser}">
		  			<ps:notPermissionAccess permissions="EXTA">
		  			<form:radiobutton path="signatureReceivedAuthSigner" disabled="true" value="false"/>&nbsp;no
					</ps:notPermissionAccess>
					<ps:permissionAccess permissions="EXTA">
					<form:radiobutton path="signatureReceivedAuthSigner"  value="false"/>&nbsp;no
					</ps:permissionAccess>
				</c:if>
				</logicext:then>
		    	<logicext:else>
		    		&nbsp;
		    	</logicext:else>
			</logicext:if>
			</td>
        	<td align="left" nowrap>&nbsp;</td>
        	<logicext:if name="layoutBean" property="param(task)" op="equal" value="edit">
	        	<logicext:then>
	        		<ps:trackChanges name="addEditUserForm" property="signatureReceivedAuthSigner"/>
	        	</logicext:then>
        	</logicext:if>
	  	</tr>
	
        <tr valign="top">
     		<td width="46%"><strong>TPA Contact</strong></td>
        	<td align="left" nowrap>
			<c:set  var="tpaManager" value='${tpaum}' />										<%-- CL 110473 --%>
        	<logicext:if name="layoutBean" property="param(task)" op="equal" value="edit">
	        	<logicext:then>
		        	<c:if test="${userProfile.role.TPA && tpaManager == null}">					<%-- CL 110473 --%>	
		        	<form:radiobutton path="primaryContact" disabled="true" value="true"/>&nbsp;yes
		        	</c:if>
		        	<c:if test="${userProfile.role.internalUser || tpaManager != null}">		<%-- CL 110473 --%>
					<form:radiobutton path="primaryContact" value="true"/>&nbsp;yes
		        	</c:if>
		        </logicext:then>
		    	<logicext:else>
		    		<render:yesno property="addEditUserForm.primaryContact" defaultValue="no" style="c" />
		    	</logicext:else>
	        </logicext:if>
        	</td>
  			<td align="left" nowrap>
  			<logicext:if name="layoutBean" property="param(task)" op="equal" value="edit">
  			<logicext:then>
	  			<c:if test="${userProfile.role.TPA  && tpaManager == null}">					<%-- CL 110473 --%>
	  			<form:radiobutton path="primaryContact" disabled="true" value="false"/>&nbsp;no
	  			</c:if>
	  			<c:if test="${userProfile.role.internalUser || tpaManager != null}">			<%-- CL 110473 --%>
	  			<form:radiobutton path="primaryContact" value="false"/>&nbsp;no
	        	</c:if>
	        </logicext:then>
		    	<logicext:else>
		    		&nbsp;
		    	</logicext:else>
	        </logicext:if>
  			</td>
        	<td align="left" nowrap>&nbsp;</td>
        	<logicext:if name="layoutBean" property="param(task)" op="equal" value="edit">
	        	<logicext:then>
	           	 <ps:trackChanges name="addEditUserForm" property="primaryContact"/>
	            </logicext:then>
            </logicext:if>
  		</tr> 
	</table>
	</td>
	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</tr>
</c:if>
