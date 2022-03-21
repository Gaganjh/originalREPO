<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>


 <%--- start contract contact information---%> 
 	 <tr>
	 	<td class="boxborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	 	<td>
		    <table width="100%" cellpadding="0" cellspacing="0" border="0">
            	<tr class="datacell1" valign="top">
             	    <td colspan="2" width="25%"><b>Contract name</b></td>
              		<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td colspan="2" width="25%"><b class="highlight">${contractProfile.contractName}</b><br>
                	<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                	<td width="25%">
                  		<p><b>Mailing address</b>
                  		</p>
                	</td>
                	<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
                	<td>
${contractProfile.contractName}<br>
${contractProfile.address.line1}<br>
${contractProfile.address.line2}<br>
${contractProfile.address.city}
<c:if test="${contractProfile.address.completeStateCode ==true}"> ,</c:if>
${contractProfile.address.stateCode}
${contractProfile.address.zipCode}
                  		
                  		<br>
                	</td>
               	</tr>
              
              	<tr class="datacell1">
                	<td colspan="9" class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
              	</tr>
             </table>
<%--- end contract contact information --%>

