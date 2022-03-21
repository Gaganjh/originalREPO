<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
   
<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.profiles.TpaFirm" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<STYLE type="text/css">
   .borderLeft {border-left: 1px solid #89A2B3;}
   .borderRight {border-right: 1px solid #89A2B3;}
   .borderBottom {border-bottom: 1px solid #89A2B3;}
   .dataDividerRight {border-right: 1px solid #CCCCCC;}
   .boldEntry { font-weight:bold;}
</STYLE>
 
 
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_NO_PERMISSIONS%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="noPermissionsError" />
<script type="text/javascript" >
var submitted = false;

function doCancelChanges(theForm) {
	if (!submitted) {

		submitted = doCancel(theForm);

		return submitted;
	 } else {
		 
		 window.status = "Transaction already in progress.  Please wait.";
		 return false;
	 }
}

function isFormChanged() {
	var contractIndex = 0;
	var selectedIds = "";
	do {
		var prefix = "contractAccess[" + contractIndex + "]";
		var contractNumberField = document.getElementsByName(prefix + ".contractNumber");
		if (contractNumberField.length == 0)
			break;
		
		var directDebitCheckBoxes = document.getElementsByName(prefix + ".selectedDirectDebitAccounts");
		if (directDebitCheckBoxes != null && directDebitCheckBoxes.length > 0) {
			for (var i = 0; i < directDebitCheckBoxes.length; i++) {
				if (directDebitCheckBoxes[i].checked) {
					selectedIds += directDebitCheckBoxes[i].value + ",";
				}
			}
			var selectedIdsField = document.getElementsByName(prefix + ".selectedDirectDebitAccountIdsAsString");
			selectedIdsField[0].value = selectedIds;
		}
		contractIndex++;
	} while (1);
	
	var tpaUsers = document.getElementsByName("selectedTPAUsers");
	selectedIds = "";
	if ( tpaUsers != null && tpaUsers.length > 0 ) {
		for ( var i = 0 ; i < tpaUsers.length ; i++ ) {
			if ( tpaUsers[i].checked) { 
				selectedIds += tpaUsers[i].value + ",";
			}
		}
		var selectedIdsField = document.getElementsByName("selectedTPAUsersAsString");
		selectedIdsField[0].value = selectedIds;
	}
	
	return changeTracker.hasChanged();
}

registerTrackChangesFunction(isFormChanged);

<%
if (request.getAttribute("WARNINGS") != null) {
	String warn = (String) request.getAttribute("WARNINGS");
%>
	function doOnload() {
		submitted = true;
		alert("<%=warn%>");
		document.tpaFirmForm.action.value = 'confirm';
		document.tpaFirmForm.submit();
	}
<%}%>

</script>

<ps:form method="POST" cssStyle="margin-bottom:0;" action="/do/profiles/editTpaFirm/" modelAttribute="tpaFirmForm" name="tpaFirmForm">


<a name="TopOfPage"></a>

<table width="700" border="0" cellspacing="0" cellpadding="0">
  	<tr>
  		<td>
			<table width="525" style="border:0px solid blue" border="0" cellspacing="0" cellpadding="0">
			    <tr>
			        <td>
				        <p>
<c:if test="${tpaFirmForm.showEverything ==false}">
							<table id="psErrors"><tr><td class="redText"><ul><li><content:getAttribute beanName="noPermissionsError" attribute="text" filter="true"/></li></ul></td></tr></table>
</c:if>
			            <div id="errordivcs"><content:errors scope="session"/></div>
			            </p>
			        </td>
				</tr>
			</table>
			<table border="0" cellpadding="2" cellspacing="0" width="412">
		    <tbody>
			    <tr class="tablehead">
			        <td class="tableheadTD1" colspan="2" style="padding:4px"><strong>Manage TPA Firm Permissions </strong></td>
			    </tr>
			    <tr class="datacell1">
			        <td  class="borderLeft dataDividerRight" ><strong>TPA firm ID </strong></td>
<td class="highlight borderRight">${tpaFirmForm.id}</td>
		        </tr>
		   		<tr class="datacell1">
		        	<td  class="borderLeft borderBottom dataDividerRight" width="127"><strong>TPA firm name </strong></td>
<td class="highlight borderRight borderBottom">${tpaFirmForm.name}</td>
			    </tr>
			    </tbody>
	        </table>
<c:set var="addEditUserForm" value="${tpaFirmForm}" scope="request"/>
			<jsp:include flush="true" page="addEditContractAccessSection.jsp"/>
			<c:if test="${addEditUserForm.showEverything}">
			<table width="412" border="0" cellspacing="2" cellpadding="2">
			     <tr>
			     	<td>
<input type="submit" class="button100Lg" onclick="return doCancelChanges('tpaFirmForm');" value="cancel"name="action"/>
					<td align="right">
<input type="submit" class="button100Lg" onclick="return firmSubmit('tpaFirmForm');" value="save" name="action"/>
			       </td>
				</tr>
			</table>			
			<script type="text/javascript" >
			var onenter = new OnEnterSubmit('action', 'save');
			onenter.install();
			</script>
			</c:if>
		</td>
	</tr>
</table>
</td>

<td width="5" ><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>

<%-- helpful hints --%>		 
<td valign="top">
	<img src="/assets/unmanaged/images/s.gif" width="1" height="5">     
	<content:rightHandLayerDisplay layerName="layer1" beanName="layoutPageBean" />   
		  
	<img src="/assets/unmanaged/images/s.gif" width="1" height="5">
	<content:rightHandLayerDisplay layerName="layer2" beanName="layoutPageBean" />   
</td>
		 
</tr>
</table>
</ps:form>

