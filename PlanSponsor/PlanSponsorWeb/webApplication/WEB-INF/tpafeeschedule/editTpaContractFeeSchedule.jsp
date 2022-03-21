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
%>
<content:contentBean
	contentId="<%=ContentConstants.WARNNING_MESSAGE_ON_CANCEL_DATA_LOSS_TO_TPA_USER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="warningMessage" />

<content:contentBean
	contentId="<%=ContentConstants.MORE_THAN_5_CUSTOM_FEE_WARNING_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="moreThan5CusotmFeeAdded" />

<content:contentBean
	contentId="<%=ContentConstants.EDIT_TPA_STANDARD_FEE_SCHEDULE_PAGE_LEGEND%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="legend" />	

<content:contentBean
	contentId="<%=ContentConstants.FEE_NOT_APPLICABLE_TO_CONTRACT_WARNING_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="planProvisionRestrictionMsg" />





<style type="text/css">
	tr.true td {   color: #7e7e7e;
			}
	
	tr.false td { 
		color: #000000;
			}
</style>

<c:if test="${empty param.printFriendly }" >


	<script type="text/javascript" >
		function delRow(row_id, table_name) {
			var userAck = true;
			var row = $(row_id).parent().parent();
			if(row.find("#feeDescription").val() != '' ||
					row.find("#amountValue").val() != '' ||
					row.find("#notes").val() != ''){
				setDirtyFlag();
			}
			var rowCount = getAdditionalFeesRowCount("#" + table_name);
			if (rowCount > 1) {
				setDefaults($(row_id).parent().parent());
				$(row_id.parentNode).parent().hide();
				if(rowCount == 2){
					var lastRow = $("#" + table_name + " tr:visible:last");
					if(lastRow.find("#feeDescription").val() == '' &&
					lastRow.find("#amountValue").val() == '' &&
					lastRow.find("#notes").val() == ''){
						lastRow.find("#deleteRow").removeAttr('href');
						lastRow.find("#deleteRow").removeAttr('click');
					}
				}
			} else {
				setDefaults($(row_id).parent().parent());
				addRow(table_name, true);
				$(row_id.parentNode).parent().hide();
			}
			return false;
		}

		function doCancelEdit() {

			var message;
			var response = true;
			if (isFormDirty()) {
				message = '<content:getAttribute beanName="warningMessage" attribute="text" />';
				var response = confirm(message);
			}
			if (response == true) {
				document.tpaCustomizeContractForm.action.value = "back";
				document.tpaCustomizeContractForm.submit();
			} else {
				return false;
			}
		}

		function doNext() {
			document.tpaCustomizeContractForm.action.value = "confirm";
			document.tpaCustomizeContractForm.submit();
		}

		function doResetStandardSchedule() {
			document.tpaCustomizeContractForm.action.value = "resetToStandardSchedule";
			document.tpaCustomizeContractForm.submit();
		}

		function isFormDirty() {
			return $("#dirtyFlagId").val() == 'true';
		}

		function setDirtyFlag() {
			$("#dirtyFlagId").val("true");
		}
		
		function isLostChanges(){
			if (isFormDirty()) {
				
				return confirm('<content:getAttribute beanName="warningMessage" attribute="text" />');
			}
			return true;
		}

		try {
			$(document)
					.ready(
							function() {
								
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
								
								$("tr.true").on("mouseover",function() {    
									Tip("<content:getAttribute beanName="planProvisionRestrictionMsg" attribute="text" filter="true"/>"); } 
								).on("mouseout",function(){    UnTip();  });

								// To track changes to form data
								$(":input:enabled:not(:hidden):not(:button)")
										.on("change",
												function() {
													if (this.defaultValue != this.value) {
														setDirtyFlag();
													}
												});

								$("#TpaDataTable tr[id=additionalFeesRow] td :input:enabled").on("change",function() {
									if (this.defaultValue != this.value) {
										setDirtyFlag();
										var rowCount = getAdditionalFeesRowCount("#TpaDataTable");
										if(rowCount == 1){
											var lastRow = $("#TpaDataTable tr:visible:last");
											lastRow.find('#deleteRow').attr('href', 'javascript://');
											lastRow.find('#deleteRow').attr('click', "delRow(this, 'TpaDataTable');");
											
										}
									}
								});

								$("a:not(.signOutLayerStyle2):not(.protectedLinkId)")
										.on("click",
												function(event) {
														return isLostChanges();
												});

								
								$("#addRowTpa").on("click",function() {			
										addRow("TpaDataTable", false);
								});

								disableDeleteButton("TpaDataTable");
								
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
		
		function setDefaults(row) {
			row.find("#feeDescription").val("");
			row.find("#amountType").val("$");
			row.find("#amountValue").val("");
			row.find("#notes").val("");
			row.find("#deletedInd").val("true");
			row.find(".errorIcon").remove();
		}
		function addRow(table_name, isDeleted) {
			var rowCount = getAdditionalFeesRowCount("#" + table_name);
			
			if (rowCount == 5) {
				alert('<content:getAttribute beanName="moreThan5CusotmFeeAdded" attribute="text" />');
			} else {
				var lastRow = $("#" + table_name + " tr:visible:last");
				if(isDeleted == true){
					lastRow.find("#deleteRow").removeAttr('href');
					lastRow.find("#deleteRow").removeAttr('click');
				} else {
					lastRow.find('#deleteRow').attr('href', 'javascript://');
					lastRow.find('#deleteRow').attr('click', "delRow(this, '"+table_name+"');");
				}
				
				var row = lastRow.clone(true);
				setDefaults(row);

				row.find("#deletedInd").val("false");
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

		function setDeleted(row) {
			row.find("#deletedInd").val("true");
		}

		function getAdditionalFeesRowCount(tableId) {
			return $(tableId + " tr:visible[id=additionalFeesRow]").length;
		}

		
	</script>
</c:if>




<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><img src="/assets/unmanaged/images/s.gif" width="30"
			height="1"></td>
		<td width="100%" valign="top">

			<div id="messagesBox" class="messagesBox">

				<ps:messages scope="session"
					maxHeight="${param.printFriendly ? '1000px' : '100px'}"
					suppressDuplicateMessages="true" />
			</div>
			<DIV style="DISPLAY: block" id=basic></DIV> <br> <ps:form
				action="/do/editTpaCustomizedContractFee/" modelAttribute="tpaCustomizeContractForm" name="tpaCustomizeContractForm">
<input type="hidden" name="action" /><%--  input - name="tpaCustomizeContractForm" --%>
<input type="hidden" name="dirty" id="dirtyFlagId"/>
				<TABLE border="0" cellSpacing=0 cellPadding=0 width="100%">
					<TBODY>
						<TR height="20">
							<td colspan="3" valign="middle">
								<p>
									<strong>Legend: </strong> <IMG
										src="/assets/unmanaged/images/delete_icon.gif"> 
										<content:getAttribute beanName="legend" attribute="text" />
								</p> <br />
							</td>
						</TR>
						<tr class="tablehead">
							<td class="tableheadTD1" colspan="10" height="25" valign="center"
								style="padding-left: 6px"><b>Customize Contract</b></td>
							<td class="databorder"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
						</tr>

						<TR class="datacell1">
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
							<TD class="datacell1" height=25 vAlign=center colSpan=9
								style="padding-left: 6px">
								<TABLE border=0 width="100%">
									<TBODY>
										<TR class=datacell1>
											<TD vAlign=top width="19%"><B>Contract number</B></TD>
<TD width="18%">${tpaCustomizeContractForm.selectedContract}</TD>


											<TD width="18%">&nbsp;</TD>
											<TD width="25%">&nbsp;</TD>
											<TD width="32px" align="right">&nbsp;</td>
										</TR>
										<TR class=datacell1>
											<TD vAlign=top width="19%"><B>Contract name</B></TD>
<TD width="25%">${tpaCustomizeContractForm.selectedContractName}</TD>


											<TD width="18%">&nbsp;</TD>
											<TD width="25%">&nbsp;</TD>
											<TD width="32px" align="right">&nbsp;</td>
										</TR>
									</TBODY>
								</TABLE>
							</TD>

							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>



						<TR class="tablehead">
							<TD class="tableheadTD" height=25 vAlign=center colSpan=10
								style="padding-left: 6px"><b>Individual Expenses </b></TD>

							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>


						<TR class="tpaFeeSection">
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
							<TD vAlign="top" width="">
								<TABLE id="TpaDataTable" border="0" cellSpacing="0"
									cellPadding="0" width="100%" style="border-collapse: collapse; table-layout: fixed">
									<TBODY>
										<TR style="background: #cccccc">
											<!-- Fee Type -->
											<TD width="20"><IMG src="/assets/unmanaged/images/s.gif"
												width="1" height="1"></TD>
											<TD width="186"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<!-- Data divider -->
											<TD width="1"><IMG src="/assets/unmanaged/images/s.gif"
												width="1" height="1"></TD>
											<!-- Value drop-down -->
											<TD width="80" class="type"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<!-- Value text box -->
											<TD width="90" class=type><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<!-- Data divider -->
											<TD width="1"><IMG src="/assets/unmanaged/images/s.gif"
												width="1" height="1"></TD>
											<!-- Special notes-->
											<TD width="350"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
										</TR>
										<TR class="tablesubhead" height="24">
											<TD style="padding-left: 4px;" colspan="2"><b>Fee
													type</b></TD>
											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<TD valign="middle" align="right" colspan="2"><b>Value&nbsp;</b></TD>
											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<TD vAlign="middle" align="left"><b>Special notes</b></TD>
										</TR>

										<!-- Standard Fee List -->
<c:forEach items="${tpaCustomizeContractForm.tpaFeesStandard}" var="tpaStandardFee" varStatus="rowIndex">



	                                       <c:choose>
										    <c:when test="${rowIndex.index % 2 == 0}">
										     <tr class="datacell2 ${tpaStandardFee.disabled}" > 
										    </c:when>
										    <c:otherwise>
										      <tr class="datacell1 ${tpaStandardFee.disabled}">
										     </c:otherwise>
										    </c:choose> 
										    
												<!--  Fee Description -->
												<TD style="padding-left: 4px;" vAlign="top" colspan="2">
<B>${tpaStandardFee.feeDescription}</B> <input type="hidden" name="deleted" id="deletedInd" /><%--  input - indexed="true" name="tpaStandardFee" --%> 
			<ps:fieldHilight		name="tpaStandardFee${rowIndex.index}" singleDisplay="true"
														className="errorIcon" displayToolTip="true" />
												</TD>
												<TD class="dataheaddivider"><IMG
													src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>

												<!-- Amount Type -->
<TD align="right"><c:if test="${tpaStandardFee.feeDescription =='Loan Set-up'}">

${tpaStandardFee.feeValueType}
</c:if> <c:if test="${tpaStandardFee.feeDescription !='Loan Set-up'}">

<form:select path="tpaFeesStandard[${rowIndex.index}].feeValueType" disabled="${tpaStandardFee.disabled}" styleId="amountType" ><%--  indexed="true"   --%>

															<form:option value="$" />
															<form:option value="%" />
</form:select>
</c:if></TD>

												<!-- Amount Value-->
<TD align="right"><form:input path="tpaFeesStandard[${rowIndex.index}].feeValue" disabled="${tpaStandardFee.disabled}" maxlength="7" size="11" cssStyle="{text-align: right}" id="amountValue" /></TD>




												<TD class="dataheaddivider"><IMG
													src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>

												<!-- Notes -->
<TD><form:input path="tpaFeesStandard[${rowIndex.index}].notes" disabled="${tpaStandardFee.disabled}" maxlength="140" cssStyle="WIDTH: 98%" /></TD>


											</TR>
</c:forEach>

										<!-- Custom Fee List -->
<c:forEach items="${tpaCustomizeContractForm.tpaFeesCustomized}" var="tpaCustomizedFee" varStatus="rowIndex">



											<c:if test="${ tpaCustomizedFee.deleted  eq 'true' }">
												<tr style="display: none;" class="datacell1"
													id="additionalFeesRow" height="25">
											</c:if>

											<c:if test="${ tpaCustomizedFee.deleted  eq 'false' }">
												<TR class="datacell1" id="additionalFeesRow" height="25">
											</c:if>

											<!--  Fee Description -->
											<TD style="padding-left: 4px;"><A
												class="protectedLinkId" href="javascript://" id="deleteRow"
												onClick="delRow(this, 'TpaDataTable');"><IMG border=0
													src="/assets/unmanaged/images/delete_icon.gif"></A></TD>

											<!-- Amount Type -->
											<TD>
											<div style="display: inline; white-space: nowrap;">
<form:input path="tpaFeesCustomized[${rowIndex.index}].feeDescription" maxlength="50" cssStyle="WIDTH: 90%" id="feeDescription" /><%--  form:input - indexed="true" name="tpaCustomizedFee" --%> <input type="hidden" name="deleted" id="deletedInd" /><%-- input - indexed="true" name="tpaCustomizedFee" --%> 

<ps:fieldHilight name="tpaCustomizedFee${rowIndex.index}" singleDisplay="true"
													className="errorIcon" displayToolTip="true" />
											</div>		
											</TD>
											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>

											<!-- Amount Type -->
<TD align="right"><form:select path="tpaFeesCustomized[${rowIndex.index}].feeValueType" styleId="amountType" ><%--  indexed="true"  --%>

													<form:option value="$" />
													<form:option value="%" />
</form:select></TD>

											<!-- Amount Value-->
<TD align="right"><form:input path="tpaFeesCustomized[${rowIndex.index}].feeValue" maxlength="7" size="11" cssStyle="{text-align: right}" id="amountValue" /><%-- form:input - indexed="true" name="tpaCustomizedFee" --%></TD>


											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>

											<!-- Notes -->
<TD><form:input path="tpaFeesCustomized[${rowIndex.index}].notes" maxlength="140" cssStyle="WIDTH: 98%" id="notes" /><%--  form:input - indexed="true" name="tpaCustomizedFee" --%></TD>


											</TR>
</c:forEach>

									</TBODY>
								</TABLE>
							</TD>
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>
						<TR class="datacell1 tpaFeeSection" height="25">
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
							<TD vAlign="middle" style="padding-left: 4px;"><a
								class="protectedLinkId" id="addRowTpa" href="javascript://">Create
									additional fee type</a></TD>
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>
						<TR class="datacell1 height="25">
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
							<TD vAlign="middle" style="padding-left: 4px;"></TD>
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>
						<TR>
							<TD class="databorder" colSpan="3"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>
						<TR>
							<TD colspan="3"><IMG src="/assets/unmanaged/images/s.gif"
								width="1" height="1"></TD>
						</TR>
					</TBODY>
				</TABLE>
				<br>

				<TABLE border=0 cellSpacing=0 cellPadding=0 width=730>
					<tr>
					<td width="160" align="right">	
<c:if test="${tpaCustomizeContractForm.resetToStandardSchedule ==true}">
<input type="submit" onclick="return doResetStandardSchedule();" name="button" class="button175" value="reset to standard schedule"/>



</c:if>
					</td>
								
						<td width="250" align="right"></td>

<td width="160" align="right"><input type="submit" onclick="return doCancelEdit();" name="button" class="button134" value="cancel"/></td>



<td width="160" align="right"><input type="submit" onclick="return doNext();" name="button" class="button134" value="next"/></td>


					</tr>
				</table>

			</ps:form> <script language="JavaScript" type="text/javascript"
				src="/assets/unmanaged/javascript/tooltip.js"></script>
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
