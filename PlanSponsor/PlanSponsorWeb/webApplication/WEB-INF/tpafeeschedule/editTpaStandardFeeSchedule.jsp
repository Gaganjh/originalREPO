<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
String technicalDifficulties =(String)request.getAttribute(Constants.TECHNICAL_DIFFICULTIES);
pageContext.setAttribute("technicalDifficulties",technicalDifficulties,PageContext.PAGE_SCOPE);
%>
<content:contentBean
	contentId="<%=ContentConstants.WARNNING_MESSAGE_ON_CANCEL_DATA_LOSS_TO_TPA_USER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="warningMessage" />
<content:contentBean
	contentId="<%=ContentConstants.MORE_THAN_5_CUSTOM_FEE_WARNING_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="maximumCustomFeesMessage" />
<content:contentBean
contentId="<%=ContentConstants.EDIT_TPA_STANDARD_FEE_SCHEDULE_PAGE_LEGEND%>"
type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
id="legend" />





<c:if test="${empty  technicalDifficulties}">
<c:if test="${empty param.printFriendly }" >
<script language="javascript" type="text/javascript"
		src="/assets/unmanaged/javascript/calendar.js">
</script>
<script type="text/javascript" >

	var formModified = false; // Variable to track changes to form.
	
	/*
	 * To delete the row from the additional fee list
	 */
	function delRow(row_id) {

		var rowCount = getAdditionalFeesRowCount("#dataTable");
		
		var row = $(row_id.parentNode).parent();
		if(row.find("#feeDescription").val() != '' ||
				row.find("#amountValue").val() != '' ||
				row.find("#notes").val() != ''){
			formModified = true;
		}
		
		setDefaults($(row_id).parent().parent());
		if (rowCount > 1) {
			$(row_id.parentNode).parent().hide();
			if(rowCount == 2){
				var lastRow = $("#dataTable tr:visible:last");
				if(lastRow.find("#feeDescription").val() == '' &&
				lastRow.find("#amountValue").val() == '' &&
				lastRow.find("#notes").val() == ''){
					lastRow.find("#deleteRow").removeAttr('href');
					lastRow.find("#deleteRow").removeAttr('click');
				}
			}
		} else {
			addRow('dataTable', true);
			$(row_id.parentNode).parent().hide();
		}

		return false;
	}

	function doCancelEdit() {
		
		var message;  
		
		if(isFormDirty()){
			message = '<content:getAttribute beanName="warningMessage" attribute="text" />';
		    var response = confirm(message);
		
			if (response == true) {
			
		    	document.forms['tpaStandardFeeScheduleForm'].action = "/do/editTpaStandardFeeSchedule/?action=cancelEdit";
		    	document.tpaStandardFeeScheduleForm.submit();	    	
			}
		} else {
			document.forms['tpaStandardFeeScheduleForm'].action = "/do/editTpaStandardFeeSchedule/?action=cancelEdit";
			document.tpaStandardFeeScheduleForm.submit();
		}
	}

	function doNext() {
		document.forms['tpaStandardFeeScheduleForm'].action = "/do/editTpaStandardFeeSchedule/?action=next";
		document.tpaStandardFeeScheduleForm.submit();
	}

	

	function isFormDirty() {
		var redirectedFromPreview = $('#editPageModified').val();
		return (formModified || (redirectedFromPreview=="true"));
	}
	
	function isLostChanges(){
		
		if(isFormDirty()){
			return confirm('<content:getAttribute beanName="warningMessage" attribute="text"/>');
		}
		return true;
	}

	try {
		$(document).ready(function() {
			
			
			registerTrackChangesFunction(isFormDirty);
			var hrefs  = document.links;
			if (hrefs != null)
			{
				for (i=0; i<hrefs.length; i++) { 
					if(hrefs[i].href != undefined && 
						hrefs[i].href.indexOf("/profiles/editMyProfile") != -1){
						hrefs[i].onclick = new Function ("return isLostChanges();");
					}
				
				}
			}

			// To track changes to form data
			$(":input:enabled:not(:hidden):not(:button)").on("change",function() {
				if (this.defaultValue != this.value) {
					formModified = true;
				}
			});

			
			$("a:not(.signOutLayerStyle2):not(.protectedLinkId)").on("click",function(event){
				return isLostChanges();
			});

			$("#dataTable tr[id=additionalFeesRow] td :input:enabled").on("change",function() {
				if (this.defaultValue != this.value) {
					formModified = true;
					var rowCount = getAdditionalFeesRowCount("#dataTable");
					if(rowCount == 1){
						var lastRow = $("#dataTable tr:visible:last");
						lastRow.find('#deleteRow').attr('href', 'javascript://');
						lastRow.find('#deleteRow').attr('click', "delRow(this);");
						
					}
				}
			});

			
			/*
			 * To add rows to the additional fee list. Max 5 is allowed. 
			 * If exceeds 5, error message displayed in pop-up.
			 */
			$("#addRow").on("click",function() {
				addRow("dataTable", false);
			});

			disableDeleteButton('dataTable');

		});
	} catch (e) {
		// If JQuery is not loaded, exception will be caught here
	}


	function disableDeleteButton(tableId){
		var tableRows = getAdditionalFeesRowCount("#"+tableId);
		if(tableRows == 1){
			var lastRow = $("#"+tableId+" tr:visible:last");
			lastRow.find("#deleteRow").removeAttr('href');
			lastRow.find("#deleteRow").removeAttr('click');
		}
	}
	
	function addRow(table_name, isDeleted) {
		var rowCount = getAdditionalFeesRowCount("#" + table_name);
		
		if (rowCount == 5) {
			
			alert('<content:getAttribute beanName="maximumCustomFeesMessage" attribute="text"/>');
		} else {
			var lastRow = $("#" + table_name + " tr:visible:last");
			if(isDeleted == true){
				lastRow.find("#deleteRow").removeAttr('href');
				lastRow.find("#deleteRow").removeAttr('click');
			} else {
				lastRow.find('#deleteRow').attr('href', 'javascript://');
				lastRow.find('#deleteRow').attr('click', "delRow(this);");
			}
			
			var row = lastRow.clone(true);
			setDefaults(row);

			row.find("#deletedIndicator").val("false");
			var newIndex = $("#" + table_name + " tr[id=additionalFeesRow]").length;
			row.find("input,select")
					.attr(
							"name",
							function() {
								var index = (this.name.substring(this.name
										.indexOf('[') + 1, this.name
										.indexOf(']')));

								return this.name.replace(index, newIndex);
							});

			row.appendTo("#" + table_name);
		}
	}


	
	/*
	 * To set the default values for the input elements in the additional fees section
	 */
	function setDefaults(row) {
		row.find("#feeDescription").val("");
		row.find("#amountType").val("$");
		row.find("#amountValue").val("");
		row.find("#notes").val("");
		row.find("#deletedIndicator").val("true");
		row.find(".errorIcon").remove();
	}

	/*
	 * To get the number of additional fee rows 
	 * Returns the number of rows with id additionalFeesRow
	 */
	function getAdditionalFeesRowCount(tableId) {
		return $(tableId + " tr:visible[id=additionalFeesRow]").length;
	}
</script>
</c:if>
</c:if>
<%-- technical difficulties --%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><img src="/assets/unmanaged/images/s.gif" width="30"
			height="1"></td>
		<td width="100%" valign="top"><%-- error line --%> &nbsp;&nbsp;
		<div id="messagesBox" class="messagesBox"><%-- Override max height if print friendly is on so we don't scroll --%>
		<ps:messages scope="session"
			maxHeight="${param.printFriendly ? '1000px' : '100px'}"
			suppressDuplicateMessages="true" /></div>
		<DIV style="DISPLAY: block" id=basic></DIV>

		<%-- technical difficulties --%> <br>
		<ps:form action="/do/previewTpaStandardFeeSchedule/"  modelAttribute="tpaStandardFeeScheduleForm" name="tpaStandardFeeScheduleForm">
			<%-- Start Standard Fee Schedule Edit information --%>
	<form:hidden path="editPageModified" id="editPageModified"/>
			<TABLE border="0" cellSpacing="0" cellPadding="0" width="100%">
				<TBODY>
					<TR height="20" >
						<td colspan="3" valign="middle">
							<p><strong>Legend: </strong>  
							<IMG src="/assets/unmanaged/images/delete_icon.gif">&nbsp;<content:getAttribute id="legend" attribute="text" /> </p>
						</td>
					</TR>
					<TR class="tablehead">
						<TD class="tableheadTD1" height="25" vAlign="middle" colSpan="2"><b>Individual Expenses</b></TD>
						<TD class="databorder" width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
					</TR>
					
					<TR>
						<TD class="databorder" width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						<TD vAlign="top" width="">
						<TABLE id="dataTable" border="0" cellSpacing="0" cellPadding="0" width="100%" style="border-collapse: collapse; table-layout: fixed">
							<TBODY>
								<TR style="background:#cccccc">
									<!-- Fee Type -->
									<TD width="20"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
									<TD width="186"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
									<!-- Data divider -->
									<TD width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
									<!-- Value drop-down -->
									<TD width="80" class="type"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
									<!-- Value text box -->
									<TD width="90" class=type><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
									<!-- Data divider -->
									<TD width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
									<!-- Special notes-->
									<TD width="350"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
								</TR>
								<TR class="tablesubhead" height="24">
									<TD style="padding-left:4px;" colspan="2"><b>Fee type</b></TD>
									<TD class="dataheaddivider"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
									<TD valign="middle" align="right" colspan="2"><b>Value&nbsp;</b></TD>
									<TD class="dataheaddivider"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
									<TD vAlign="middle" align="left"><b>Special notes</b></TD>
								</TR>
								
								<!-- Standard Fee List --> 
<c:forEach items="${tpaStandardFeeScheduleForm.feeLabelList}" var="feeLabelItems" varStatus="rowIndex" >

										<c:choose>
									    <c:when test="${rowIndex.index % 2 == 0}">
									     <tr class="datacell2"> 
									    </c:when>
									    <c:otherwise>
									      <tr class="datacell1">
									     </c:otherwise>
									    </c:choose> 
								
									<!--  Fee Description -->
										<TD style="padding-left: 4px;" vAlign="top" colspan="2">
<b>${feeLabelItems.feeDescription}</b><ps:fieldHilight

											name="tpaStandardFee${rowIndex.index}" style="float:none"
											singleDisplay="true" className="errorIcon"
											displayToolTip="true" /></TD>
										<TD class="dataheaddivider"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1">
									</TD>							
									<!-- Amount Type -->
									<TD align="right">
<c:if test="${feeLabelItems.feeDescription =='Loan Set-up'}">


${feeLabelItems.amountType}
</c:if>
<c:if test="${feeLabelItems.feeDescription !='Loan Set-up'}">


<form:select path="feeLabelList[${rowIndex.index}].amountType" id="amountType" ><%--  - indexed="true"   --%>
												<form:option value="$" /><form:option value="%" />
</form:select>
</c:if>
									</TD>
									
									<!-- Amount Value-->
									<TD align="right">
<form:input path="feeLabelList[${rowIndex.index}].amountValue" maxlength="7" size="11" cssStyle="{text-align: right}" id="amountValue" /><%--  form:input - indexed="true" name="feeLabelItems" --%>

													
										
									</TD>
									<TD class="dataheaddivider"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
									
									<!-- Notes -->
									<TD>
<form:input path="feeLabelList[${rowIndex.index}].notes" maxlength="140" cssStyle="WIDTH: 98%" /><%--  form:input - indexed="true" name="feeLabelItems" --%>
									</TD>
								</TR>
</c:forEach>
								
								<!-- Custom Fee List --> 
<c:forEach items="${tpaStandardFeeScheduleForm.additionalFeeItemsList}" var="additionalFeeItems" varStatus="rowIndex" >




<c:if test="${additionalFeeItems.deletedIndicator ==false}">

								<TR class="datacell1" id="additionalFeesRow" height="25">
									<!--  Del row icon -->
									<TD style="padding-left:4px;" >
										<A href="javascript://" class="protectedLinkId" id="deleteRow" onClick="delRow(this);" ><IMG border="0" src="/assets/unmanaged/images/delete_icon.gif"></A>
									</TD>
									
									<!-- Fee Description -->
									<TD>
									
<form:input path="additionalFeeItemsList[${rowIndex.index}].feeDescription" maxlength="50" cssStyle="WIDTH: 90%" id="feeDescription" /><%-- tag form:input - indexed="true" name="additionalFeeItems" --%> <ps:fieldHilight


													name="tpaCustomizedFee${rowIndex.index}" singleDisplay="true"
													style="float:none" className="errorIcon" displayToolTip="true" />
												
								
									</TD>
									<TD class="dataheaddivider"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
									
									<!-- Amount Type -->
									<TD align="right">
<form:select path="additionalFeeItemsList[${rowIndex.index}].amountType" styleId="amountType"><%--  indexed="true"   --%>
											<form:option value="$" /> 
											<form:option value="%" />
</form:select>
									</TD>
									
									<!-- Amount Value-->
									<TD align="right">
<form:input path="additionalFeeItemsList[${rowIndex.index}].amountValue" maxlength="7" size="11" cssStyle="{text-align: right}" id="amountValue" /><%-- form:input - indexed="true" name="additionalFeeItems" --%>

<form:hidden path="additionalFeeItemsList[${rowIndex.index}].deletedIndicator" id="deletedIndicator" /><%-- input - indexed="true" name="additionalFeeItems" --%>

									</TD>
									<TD class="dataheaddivider"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
									
									<!-- Notes -->
									<TD>
<form:input path="additionalFeeItemsList[${rowIndex.index}].notes" maxlength="140" cssStyle="WIDTH: 98%" id="notes" /><%--  form:input - indexed="true" name="additionalFeeItems" --%>

									</TD>
								</TR>
</c:if>
</c:forEach>

							</TBODY>
						</TABLE>
						</TD>
						<TD class="databorder" width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
					</TR>
					<TR class="datacell1" height="25">
						<TD class="databorder" width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						<TD vAlign="middle" style="padding-left: 4px;">
							<a class="protectedLinkId" id="addRow" href="javascript://">Create additional fee type</a>
						</TD>
						<TD class="databorder" width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
					</TR>
						
					<TR>
						<TD class="databorder" colSpan="3"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
					</TR>
					<TR>
						<TD colspan="3"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
					</TR>
				</TBODY>
			</TABLE>
			<br>

			<TABLE border="0" cellSpacing="0" cellPadding="0" width="730">
				<tr>

					<td width="410" align="right"></td>

<td width="160" align="right"><input type="button" onclick="return doCancelEdit();" name="button" class="button134" value="cancel"/></td>



<td width="160" align="right"><input type="button" onclick="return doNext();" name="button" class="button134" value="next"/></td>


				</tr>
			</table>

		</ps:form> <script language="JavaScript" type="text/javascript"
			src="/assets/unmanaged/javascript/tooltip.js"></script> 
			
			<%-- End Standard Fee Schedule Edit information --%>

		<%-- technical difficulties --%></td>
	</tr>
</table>

<c:if test="${not empty param.printFriendly }" >
	<content:contentBean
		contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
		type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure" />
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute id="globalDisclosure"
				attribute="text" /></td>
		</tr>
	</table>
</c:if>
<c:if test="${empty param.printFriendly}">
		<table cellpadding="0" cellspacing="0" border="0" width="730" class="fixedTable" height="">
			<tr>
				<td width="30">&nbsp;</td>
				<td width="700"><content:pageFooter beanName="layoutPageBean" /></td>
			</tr>
		</table>
</c:if>
