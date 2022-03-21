<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
<%-- Imports --%>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.profiles.ManagePasscodeExemptionForm"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="com.manulife.pension.ps.web.profiles.ManagePasscodeExemptionController"%>

<%-- Define a user info object --%>


<c:set var="userInfo" value="${USERINFO_KEY}" />
<c:set var="exemptInfo" value="${EXEMPTINFO_KEY}" />
<c:set var="theForm" value="${managePasscodeExemptionForm}"/>



<script language=javascript type='text/javascript'>
var submitted=false;

function doSubmit(actionName) {
	if (!submitted) {
		document.managePasscodeExemptionForm.action.value=actionName;
		submitted = true;
		return true;
  	 } else {
		 window.status = "Transaction already in progress.  Please wait.";
		return false;
	 }
}


</script>

        <table width="525" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td colspan="8">
				<content:errors scope="request"/>
			</td>
		  </tr>
          <tr>
            <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td width="113"><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
            <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td width="463"><img src="/assets/unmanaged/images/s.gif" width="250" height="1"></td>
            <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td width="113"><img src="/assets/unmanaged/images/s.gif" width="80" height="1"></td>
            <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1"></td>
            <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          </tr>

          <tr class="tablehead">
            <td class="tableheadTD1" colspan="8"> <b><content:getAttribute id="layoutPageBean" attribute="body1Header"/></b></td>
          </tr>
          <tr class="datacell1">
            <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td colspan="6" align="center"><table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr class="datacell1">
                <td width="46%" valign="top" class="datacell1"><strong>First name</strong></td>
<td width="54%" valign="top" class="datacell1">${e:forHtmlContent(userInfo.firstName)}</td>
              </tr>
              <tr class="datacell1">
                <td width="33%" valign="top" class="datacell1"><strong>Last name</strong></td>
<td valign="top" class="datacell1">${e:forHtmlContent(userInfo.lastName)}</td>
              </tr>
              <tr class="datacell1">
                <td valign="top" class="datacell1"><strong>Primary Email</strong></td>
<td valign="top" class="datacell1">${e:forHtmlContent(userInfo.email)}</td>
              </tr>
               <tr class="datacell1">
                <td valign="top" class="datacell1"><strong>Mobile number</strong></td>
<td valign="top" class="datacell1">${theForm.mobile}</td>
              </tr>
              <tr class="datacell1">
                <td valign="top" class="datacell1"><strong>User role</strong></td>
<td valign="top" class="datacell1">${theForm.userRole}</td>
              </tr>
             
               
              <tr class="datacell1">
                <td valign="top" width="50%" class="datacell1"><strong>Exemption type</strong></td>
                <td valign="top" class="datacell1">
					<c:if test="${exemptInfo.typeCode eq 'TEMP'}">
					    Temporary
					</c:if> <c:if test="${exemptInfo.typeCode eq 'PERM'}">
					    Permanent
					</c:if>
                </td>   
				<td valign="top" width="25%" class="datacell1">
				</td>
              </tr>
              <tr class="datacell1">
                <td valign="top" width="50%" class="datacell1"><strong>Exemption timestamp</strong></td>
<td valign="top" class="datacell1">${theForm.exemptTimeStamp}</td>
				<td valign="top" width="25%" class="datacell1">
				</td>
              </tr>
              <tr class="datacell1">
                <td valign="top" width="50%" class="datacell1"><strong>Exemption requested by</strong></td>
<td valign="top" class="datacell1">${exemptInfo.requestedByName}</td>
				<td valign="top" width="25%" class="datacell1">
				</td>
              </tr>
              <tr class="datacell1">
                <td valign="top" width="50%" class="datacell1"><strong>Exemption processed by</strong></td>
<td valign="top" class="datacell1">${theForm.exemptProccessedByName}</td>
				<td valign="top" width="25%" class="datacell1">
				</td>
              </tr>
              <tr class="datacell1">
                <td valign="top" width="50%" class="datacell1"><strong>PPM ticket number</strong></td>
<td valign="top" class="datacell1">${exemptInfo.ppmNumber}</td>
              </tr>
              
            </table></td>
            <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          </tr>
          <tr class="whiteborder">
            <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>
            <td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td class="whiteborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td  colspan="2" rowspan="2" class="whiteborder"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5"></td>
          </tr>
          <tr>
            <td class="databorder" colspan="6"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          </tr>
        </table>

        <br>
        <table width="525" border="0" cellspacing="0" cellpadding="0">
          <tr align="right">
          <ps:form cssClass="margin-bottom:0;"
                   method="POST" modelAttribute="managePasscodeExemptionForm"
                   action="/do/passcode/managePasscodeExempt/">
            
<form:hidden path="userName" />	
<form:hidden path="firstName" />	
<form:hidden path="lastName" />	
<form:hidden path="email" />	
<%-- <form:hidden path="secondaryEmail" /> --%>			

<%-- <input type="hidden" name="userName" /> input - name="userInfo"
<input type="hidden" name="firstName" /> input - name="userInfo"
<input type="hidden" name="lastName" /> input - name="userInfo"
<input type="hidden" name="email" /> input - name="userInfo"
<input type="hidden" name="secondaryEmail" /> input - name="userInfo" --%>
			
			<td align="right">
<input type="submit" class="button150" onclick="return doSubmit('back');" name="action" value="<%= ManagePasscodeExemptionForm.BUTTON_LABEL_BACK %>" />

			</td>
			
            <td align="right" width="40%">
<input type="submit" class="button150" onclick="return doSubmit('remove');" name="action" value="<%= ManagePasscodeExemptionForm.BUTTON_LABEL_REMOVE_EXEMPT%>" />


			</td>
			
          </ps:form>
          </tr>
        </table>
        
        



