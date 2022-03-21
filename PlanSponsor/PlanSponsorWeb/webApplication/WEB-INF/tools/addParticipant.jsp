<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.AddableParticipant" %>
<%@ page import="com.manulife.pension.ps.web.tools.AddParticipantForm" %>

<style type="text/css">
<!--
div.scroll {
	height: 234px;
	width: 225px;
	overflow: auto;
	border-style: none;
	background-color: #fff;
	padding: 8px;}
-->
</style>

<script type="text/javascript" >

	function checkCancel() {	
		var selectedFound = false;		
		if (document.addParticipantForm.newSelected != null && document.addParticipantForm.newSelected.checked == "1") {
			selectedFound = true;
		} else {
			for (var i = 0; i < document.addParticipantForm.length; i++) {
				e = document.addParticipantForm.elements[i];
				if (e.type == "checkbox" && e.disabled == false && e.checked == true) {
					selectedFound = true;
					break;
				}
			}
		}
		if (selectedFound) {
			if (confirm('You have chosen to return to the submission details without adding the selected participants. Are you sure you want to continue?')) {
				document.location.href=document.addParticipantForm.editPageURL.value;
			}
		} else {
			document.location.href=document.addParticipantForm.editPageURL.value;
		}
	}

	var newRow = false; 
	function createParticipant(){
		var newLocation = location.href;
		if (newRow != true) {
			var query = location.search.substring(0);
			var pos = query.indexOf("?");
			if (pos != -1) {
				newLocation = location.href+"&create=true";
			} else {
				newLocation = location.href+"?create=true";
			}	
			if (doCancel() == false) return false;
			document.location.href=newLocation;
			return false;
		} else {
			return true;
		}	
	}
	
	function checkCreate() { 
		
		var query = location.search.substring(1);
		var pos = query.indexOf("create=true");
		if (pos != -1) { 
			newRow = true;
		} else {
			if (document.addParticipantForm.create.value == "Y") {
				newRow = true;
			}
		}
		return newRow;
	} 
	
	var submitted=false;
	var buttonClicked = null;
	
	function confirmSend() {
		if (submitted) {
			window.status = "Transaction already in progress.  Please wait.";
			return false;
		} 
		if (buttonClicked.value == "insert participant") {
			if (!createParticipant()) {
		  		return false;
			}
			buttonClicked.value = "insertParticipant";		
		}
			 
		if (document.addParticipantForm.newSelected != null && document.addParticipantForm.newSelected.checked == "1") {
			if ((document.addParticipantForm.createParticipantName.value == null) 
					|| (isBlank(document.addParticipantForm.createParticipantName.value))
					|| (document.addParticipantForm.createParticipantId.value == null) 
					|| (isBlank(document.addParticipantForm.createParticipantId.value))) {
				if (document.addParticipantForm.sortOption.value == 'EE') {	
					alert('Please enter the complete Name and Employee Number for the participant that was added.');
				} else {
					alert('Please enter the complete Name and SSN for the participant that was added.');
				}	
				return false;
			} else if (notNumericOrDashes(document.addParticipantForm.createParticipantId.value)) {
				alert('SSN must include only nine numeric characters plus optional dashes');
				return false;
			} else if (zeroSSN(document.addParticipantForm.createParticipantId.value)) {
				alert('SSN must not be all zeros');
			    return false;
			} else if (document.addParticipantForm.sortOption.value == 'EE'
					&& document.addParticipantForm.createParticipantId.value.length > 9) {
				alert('Employee Id must include a maximum of nine characters');
			    return false;
			} else {
				submitted=true;
				return true;
			}
		} else {
			for (var i = 0; i < document.addParticipantForm.length; i++) {
				e = document.addParticipantForm.elements[i];
				if (e.type == "checkbox" && e.disabled != true && e.checked == true) {
					submitted=true;
					return true;
				}	
			}
			alert('Please select a participant to add to the contribution.');
			return false;
		}
		
	}
	
	function notNumericOrDashes(s) {
		if (document.addParticipantForm.sortOption.value == 'EE') return false;
		var ssnLength = 0;
		for(var i = 0; i < s.length; i++) {
			var c= s.charAt(i);
			if ((isNaN(c)) && (c != '-')) return true;
			if (c != '-') ssnLength++;
		}
		if (ssnLength != 9) {
			return true;
		} else {	
			return false;
		}	
	}
	
	function zeroSSN(s) {
		if (document.addParticipantForm.sortOption.value == 'EE') return false;
		var nonZeroFound = false;
		for(var i = 0; i < s.length; i++) {
			var c= s.charAt(i);
			if ((c != '0') && (c != '-')) nonZeroFound = true;
		}
		return !nonZeroFound;
	
	}
	
	function isBlank(s) {
		for(var i = 0; i < s.length; i++) {
			var c= s.charAt(i);
			if ((c != ' ') && (c != '\n') && (c != '')) return false;
		}
		return true;
 	}
	function resetValues() {
		for (var i = 0; i < document.addParticipantForm.length; i++) {
			e = document.addParticipantForm.elements[i];
			if (e.type == "checkbox" && e.disabled == false) {
				e.checked = false;
			}
		}
		var query = location.search.substring(1);
		var pos = query.indexOf("create=true");
		if (pos != -1 || document.addParticipantForm.create.value == "Y") {
			document.addParticipantForm.newSelected.checked = false;
		}
		
	}
	function selectAll() {
		for (var i = 0; i < document.addParticipantForm.length; i++) {
			e = document.addParticipantForm.elements[i];
			if (e.type == "checkbox") {
				e.checked = true;
			}
		}
	}
	function goToOTopOfPage() {
		document.location.href='#TopOfPage';
	}
	
	function doCancel()	{
 		var formChanged = false;
  
	  	if (trackChangesFunction != null) {
  			formChanged = trackChangesFunction();
  		}
  
	 	if (formChanged) {
    		if (window.confirm(warningDiscardChanges)) {
    		  	return true;
    		} else {
      			return false;
    		}
  		}
  		return true;
	}

	function protectLinks() {
		
		var hrefs  = document.links;
		if (hrefs != null)
		{
			for (i=0; i<hrefs.length; i++) {
				if(
					hrefs[i].onclick != undefined && 
					(hrefs[i].onclick.toString().indexOf("openWin") != -1 || hrefs[i].onclick.toString().indexOf("popup") != -1 || hrefs[i].onclick.toString().indexOf("doSignOut") != -1)
				) {
					// don't replace window open or popups as they won't loose there changes with those
				}
				else if(
					hrefs[i].href != undefined && 
					(hrefs[i].href.indexOf("openWin") != -1 || hrefs[i].href.indexOf("popup") != -1 || hrefs[i].href.indexOf("doSignOut") != -1)
				) {
					// don't replace window open or popups as they won't loose there changes with those
				}
				else if(hrefs[i].onclick != undefined) {
					hrefs[i].onclick = new Function ("var result = discardChanges('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');" + "var childFunction = " + hrefs[i].onclick + "; if(result) result = childFunction(); return result;");
				}
				else {
					hrefs[i].onclick = new Function ("return discardChanges('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');");
				}
				//alert (hrefs[i].onclick);
			}
		}
		
	 }	
	
</SCRIPT> 

<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>

<content:contentBean contentId="<%=ContentConstants.ADD_PARTICIPANT_CANCEL_BUTTON_DESCRIPTION%>"
                      type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                      beanName="returnToEditButtonDescription"/>
<content:contentBean contentId="<%=ContentConstants.ADD_PARTICIPANT_ADD_SELECTED_BUTTON_DESCRIPTION%>"
                      type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                      beanName="addSelectedButtonDescription"/>
<content:contentBean contentId="<%=ContentConstants.ADD_PARTICIPANT_CREATE_PARTICIPANT_BUTTON_DESCRIPTION%>"
                      type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                      beanName="createParticipantButtonDescription"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_DISCARD_CHANGES%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="warningDiscardChanges"/>

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
AddParticipantForm form = (AddParticipantForm)session.getAttribute("addParticipantForm");
pageContext.setAttribute("form",form,PageContext.PAGE_SCOPE);

AddParticipantForm requestForm = (AddParticipantForm)session.getAttribute("addParticipantForm");
pageContext.setAttribute("requestForm",requestForm,PageContext.REQUEST_SCOPE);
%>

<c:set var="theReport" value="${form.theReport}" scope="page" />



<c:set var="contract" value="${sessionScope.USER_KEY.currentContract}" scope="request" />
<c:set var="trackChanges" value="true" scope="request" />
<script type="text/javascript" >
	var warningDiscardChanges = '<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>';
	
	function isFormChanged() {
<c:if test="${form.hasChanged ==true}">
			return true;
</c:if>
<c:if test="${form.hasChanged !=true}">
			return changeTracker.hasChanged();
</c:if>
	}
	
	registerTrackChangesFunction(isFormChanged);
	if (window.addEventListener) {
		window.addEventListener('load', protectLinks, false);
	} else if (window.attachEvent) {
		window.attachEvent('onload', protectLinks);
	}
	
</SCRIPT>

<A NAME="TopOfPage"></A>&nbsp;
     <table width="100%" border="0" cellpadding="0" cellspacing="0" class="fixedTable">

		<tr>
			<td width="16" class="big"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			<td>
				<DIV id=errordivcs><content:errors scope="session"/></DIV><br>
			</td>
			<td width="15"><img src="/assets/unmanaged/images/s.gif" width="15" height="1" border="0"></td>
			<td><img src="/assets/unmanaged/images/s.gif" width="1	" height="1" border="0"></td>
		</tr>

	        <tr>
			<td width="16" class="big">&nbsp;</td>
			<td colspan="3">
		</td>
	</table>	
				<%-- START new add particpant table --%>
	<table width="380" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="4%">&nbsp;</td>
			<td width="2%"></td>
			<td width="58%"></td> 
			<td width="2%"></td>
			<td width="16%"><a href="javascript:selectAll();">select all</a></td>
			<td width="2%"></td>
			<td width="16%"><a href="javascript:resetValues();">deselect all</a></td> 
		</tr>
	    <tr>
	        <td><img src="/assets/unmanaged/images/s.gif" width="2" height="1" /></td>
		    <td colspan="6">
		    <table width="375" border="0" cellpadding="0" cellspacing="0">
			<tr>
			  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td><img src="/assets/unmanaged/images/s.gif" width="375" height="1" /></td>
			  <td><img src="/assets/unmanaged/images/s.gif" width="4" height="1" /></td>
			  <td><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>
			
			<tr class="tablehead" height="25">
				<td class="tableheadTD1" colspan="4">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td class="tableheadTD"><b><p>Add Participant</p></b></td>
						<td class="tableheadTDinfo"><b><report:recordCounter report="theReport" label=" - Total records "/></b></td>
					  </tr>
					</table>
				</td>
		        </tr>
			
			<tr class="datacell1">
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td colspan="2" >
			  <ps:form action="/do/tools/addParticipant/" modelAttribute="addParticipantForm" name="addParticipantForm" method="POST" onsubmit="return confirmSend();">
				<input type="hidden" name="editPageURL" value=<%=form.getEditPageURL()%>/>
				<input type="hidden" name="sortOption" value="${theReport.participantSortOption}"/>
				<input type="hidden" name="create" value="<%=form.getCreateIndicator()%>"/>
				<table witdh="100%" border="0" cellpadding="0" cellspacing="0">
						
						<%-- Start Header Row --%>
						<tr class="tablesubhead">
							<td width="200">
								<b><report:sort formName="addParticipantForm" field="name" direction="asc">Name</report:sort></b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="85">
								<b>
									<report:sort formName="addParticipantForm" field="identifier" direction="asc">
<c:if test="${theReport.participantSortOption =='EE'}">
											Employee #
</c:if>
<c:if test="${theReport.participantSortOption !='EE'}">
											SSN
</c:if>
									</report:sort>
								</b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td align="left" width="85">
								<b><report:sort formName="addParticipantForm" field="includedInSubmission" direction="asc">Include in submission</report:sort></b>
							</td>
						</tr>
						<%-- End Header Row --%>
						
						<%-- Start create participant row --%>
						<script>
						if (checkCreate() == true) {
						changeTracker.trackElement('createParticipantName','***');
						changeTracker.trackElement('createParticipantId','***');
							
							document.write('<tr class="datacell2">');
						document.write('<c:set var="newLastRowStyle" value="datacell2" />');
						document.write('<td align="left" nowrap width="200">');
						document.write('		<INPUT TYPE=TEXT NAME="createParticipantName" VALUE="<%=form.getCreateParticipantName() %>" MAXLENGTH="40">');
						document.write('</td>');
						document.write('<td class="datadivider" width="1"></td>');
						document.write('<td align="left" nowrap width="85">');
						document.write('		<INPUT TYPE=TEXT NAME="createParticipantId" VALUE="<%=form.getCreateParticipantId() %>" MAXLENGTH="11">');
						document.write('</td>');
						document.write('<td class="datadivider" width="1"></td>');
						document.write('<td align="left" nowrap width="85">');
						document.write('		<INPUT TYPE=CHECKBOX NAME="newSelected" VALUE=1 CHECKED>');
						document.write('</td>');		
						document.write('</tr>');
						document.close();
						}
						</script>

						<%-- Start detail row iteration --%>
<c:forEach items="${theReport.details}" var="participant" varStatus="partIndex" >

 <c:set var="indexValue" value="${partIndex.index}"/>
 <c:set var="lastRowStyle" value="datacell2" /> 
 <% 
 int indexVal =Integer.parseInt(pageContext.getAttribute("indexValue").toString()) ;
  String lastRowStyle = pageContext.getAttribute("lastRowStyle").toString();
  AddableParticipant participant =(AddableParticipant)pageContext.getAttribute("participant");
 %>
 

					<% if (indexVal% 2 == 0) { 
					    lastRowStyle="datacell2";
					%>
						<tr class="datacell1">
						
					<% } else { 
					    lastRowStyle="datacell1";
					%>
						<tr class="datacell2">
					<% } %>
							<td align="left" nowrap width="200">
${participant.name}
							</td>
							<td class="datadivider" width="1"></td>
							<td align="left" nowrap width="85">
<c:if test="${theReport.participantSortOption =='EE'}">
${participant.identifier}
</c:if>
<c:if test="${theReport.participantSortOption !='EE'}">
										<render:ssn property="participant.identifier" useMask="false"/>
</c:if>
							</td>
							<td class="datadivider" width="1"></td>
							<td align="left" nowrap width="85">
								<span class="content">
								<%if(null == participant.getIncludedInSubmission()) {%>
								
 <form:checkbox path="addBoxes[${indexValue}]"   disabled="true" value="addBoxes[${indexValue}]" checked="checked"/>
								<%} else {%>
	 <form:checkbox path="addBoxes[${indexValue}]" value="addBoxes[${indexValue}]" />							
								<%}%> 
								</span>
								 <ps:trackChanges name="form" property='<%= "addBox[" + indexVal + "]" %>'/> 
							</td>	
						</tr>
</c:forEach>
						<%-- End detail row iteration --%>
				
				  </table>
			  </td>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>

			<tr class="datacell1">
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td class="lastrow" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>
			<tr class="datacell1">
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td class="lastrow" colspan="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td  colspan="2" rowspan="2" align="right" valign="bottom" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" /></td>
			</tr>
			<tr>
			  <td class="databorder" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>
			<tr>
			</tr>
		</table>
	    </td>
	    <td><img src="/assets/unmanaged/images/s.gif" width="150" height="1" /></td>
	    </tr>
	    </table>
		<table width="380" border="0" cellspacing="0" cellpadding="0" align="left">
		  <tr>
			<td width="4%">&nbsp;</td>
			<td width="2%"></td>
			<td width="58%"></td> 
  			<td width="2%"></td>
			<td width="16%"><a href="javascript:selectAll();">select all</a></td>
			<td width="2%"></td>
			<td width="16%"><a href="javascript:resetValues();">deselect all</a></td> 
		  </tr>
		</table> 
		<p>&nbsp;</p>
		<table width="380" border="0" cellspacing="0" cellpadding="0" align="left">
		  <tr>
			<td width="4%">&nbsp;</td>
			<td width="2%"></td>
<td width="18%"><input type="submit" name="task" id="saveButton" class="button134" value="insert participant" onclick="buttonClicked=this; return true;" /></td>
			<td width="2%"></td>
			<td width="18%"><input type="button" class="button134" onclick="checkCancel();" value="cancel" /></td>
			<td width="2%"></td>
<td width="18%"><input type="submit" id="saveButton" name="task" class="button134" onclick="buttonClicked=this; return true;" value="save" /></td>
		  </tr>
		  <tr>
			<td width="4%" valign="top">&nbsp;</td>
			<td width="2%"'></td>
			<td withd= "18%" valign="top"><span class="content"><content:getAttribute beanName="createParticipantButtonDescription" attribute="text"/></span></td>
			<td width='2%'></td>
			<td width="18%" valign="top"><span class="content"><content:getAttribute beanName="returnToEditButtonDescription" attribute="text"/></span></td>
			<td width='2%'></td>
			<td width="18%" valign="top"><span class="content"><content:getAttribute beanName="addSelectedButtonDescription" attribute="text"/></span></td>
		  </tr> 
		</table>
		</ps:form>
		<%-- END add participant table --%>
		<br>
	</td>
	</tr>
	<tr>
		<td class="big">&nbsp;</td>
		<td colspan="3">&nbsp;</td>
	</tr>
        </table>
