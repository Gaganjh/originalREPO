<%@page import="com.manulife.pension.ps.web.fee.NoticeInfo404a5Form"%>
<%@page import="com.manulife.pension.ps.web.fee.FeeUIHolder"%>
<%@page import="com.manulife.pension.ps.web.fee.PBAFeeUIHolder"%>
<%@page import="com.manulife.pension.ps.web.fee.PBARestrictionUi"%>
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

<content:contentBean
	contentId="<%=ContentConstants.WARNNING_MESSAGE_ON_CANCEL_DATA_LOSS_TO_TPA_USER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="warningMessage" />

<content:contentBean
	contentId="<%=ContentConstants.MORE_THAN_5_CUSTOM_FEE_WARNING_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="moreThan5CusotmFeeAdded" />

<content:contentBean
	contentId="<%=ContentConstants.LEGEND_404a5_NOTICE_INFO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="legend" />

<content:contentBean contentId="<%=ContentConstants.DIM_SECTION_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="introDim" />

<content:contentBean contentId="<%=ContentConstants.PBA_SECTION_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="introPba" />

<content:contentBean contentId="<%=ContentConstants.PBA_MIN_DEPOSIT%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="pbaMinDeposit" />

<content:contentBean
	contentId="<%=ContentConstants.PBA_RESTRICTION_LABEL%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="pbaRestrictionLabel" />

<content:contentBean
	contentId="<%=ContentConstants.PBA_RESTRICTION_INSTRUCTIONS%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="pbaRestrictionInstructions" />

<content:contentBean
	contentId="<%=ContentConstants.PBA_RESTRICTION_COUNT_EXCEEDING_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="moreThan10PbaRestrictionAdded" />

<content:contentBean
	contentId="<%=ContentConstants.PBA_CUSTOM_FEE_COUNT_EXCEEDING_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="moreThan10PbaCustomFeeAdded" />

<content:contentBean
	contentId="<%=ContentConstants.NON_TPA_SECTION_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="introNonTpaFee" />

<content:contentBean contentId="<%=ContentConstants.TPA_SECTION_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="introTpaFee" />

<content:contentBean
	contentId="<%=ContentConstants.FEE_NOT_APPLICABLE_TO_CONTRACT_WARNING_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="planProvisionRestrictionMsg" />


<%
	UserProfile userProfile = (UserProfile) session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile", userProfile, PageContext.PAGE_SCOPE);
	NoticeInfo404a5Form noticeInfo404a5Forms = (NoticeInfo404a5Form)session.getAttribute("noticeInfo404a5Form");
	pageContext.setAttribute("noticeInfo404a5Forms",noticeInfo404a5Forms,PageContext.PAGE_SCOPE);
%>



<style type="text/css">
tr.true td {
	color: #7e7e7e;
}

tr.false td {
	color: #000000;
}
</style>

<c:if test="${empty param.printFriendly }">


	<script type="text/javascript">
		var sections = [ "dimSection", "pbaSection", "nonTpaFeeSection",
				"tpaFeeSection" ];
							
		function setUpExpandCollapseIcon(sectionId) {
			var icon = $("#" + sectionId + "ExpandCollapseIcon");
			var section = $("#" + sectionId);
			icon.clicktoggle(function() {
				$('tr.' + sectionId).hide();
				icon.attr('src', '/assets/unmanaged/images/plus_icon.gif');
			}, function() {
				$('tr.' + sectionId).show();
				icon.attr('src', '/assets/unmanaged/images/minus_icon.gif');
			});
		};

		function expandAllSections() {
			for (var i = 0; i < sections.length; i++) {
				var icon = $("#" + sections[i] + "ExpandCollapseIcon");
				if (icon.attr('src') == '/assets/unmanaged/images/plus_icon.gif') {
					icon.trigger("click");
				}
			}
		};

		function contractAllSections() {
			for (var i = 0; i < sections.length; i++) {
				var icon = $("#" + sections[i] + "ExpandCollapseIcon");
				if (icon.attr('src') == '/assets/unmanaged/images/minus_icon.gif') {
					icon.trigger("click");
				}
			}
		};

		function delRow(row_id, table_name) {
			var userAck = true;
			var row = $(row_id).parent().parent();
			if (row.find("#feeDescription").val() != ''
					|| row.find("#amountValue").val() != ''
					|| row.find("#notes").val() != '') {
				setDirtyFlag();
			}
			var rowCount = getAdditionalFeesRowCount("#" + table_name);
			if (rowCount > 1) {
				setDefaults($(row_id).parent().parent());
				$(row_id.parentNode).parent().hide();
				if (rowCount == 2) {
					var lastRow = $("#" + table_name + " tr:visible:last");
					if (lastRow.find("#feeDescription").val() == ''
							&& lastRow.find("#amountValue").val() == ''
							&& lastRow.find("#notes").val() == '') {
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

		function delRestrictionRow(row_id, table_name) {
			var row = $(row_id).parent().parent();
			if (row.find("#restrictionDesc").val() != '') {
				setDirtyFlag();
			}
			var rowCount = getAdditionalRestrictionsRowCount("#" + table_name);
			if (rowCount > 1) {
				setDefaultsForRestriction($(row_id).parent().parent());
				$(row_id.parentNode).parent().hide();
				if (rowCount == 2) {
					var lastRow = $("#" + table_name + " tr:visible:last");
					if (lastRow.find("#restrictionDesc").val() == '') {
						lastRow.find("#deleteRow").removeAttr('href');
						lastRow.find("#deleteRow").removeAttr('click');
					}
				}
			} else {
				setDefaultsForRestriction($(row_id).parent().parent());
				addRestrictionRow(table_name, true);
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
				document.noticeInfo404a5Form.action.value = "back";
				document.noticeInfo404a5Form.submit();
			} else {
				return false;
			}
		}

		function doNext() {
			document.noticeInfo404a5Form.action.value = "confirm";
			document.noticeInfo404a5Form.submit();
		}

		function isFormDirty() {
			return $("#dirtyFlagId").val() == 'true';
		}

		function setDirtyFlag() {
			<%noticeInfo404a5Forms.setDirty("true");%>
			$("#dirtyFlagId").val("true");
		}

		function isLostChanges() {
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
								var hrefs = document.links;
								if (hrefs != null) {
									for (i = 0; i < hrefs.length; i++) {
										if (hrefs[i].href != undefined
												&& hrefs[i].href
														.indexOf("/password/changePasswordInternal") != -1) {
											hrefs[i].onclick = new Function(
													"return isLostChanges();");
										}

									}
								}

								$("tr.true")
										.on("mouseover",
												function() {
													Tip("<content:getAttribute beanName="planProvisionRestrictionMsg" attribute="text" filter="true"/>");
												}).mouseout(function() {
											UnTip();
										});

								// To track changes to form data
								$(":input:enabled:not(:hidden):not(:button)")
										.on("change",
												function() {
													if (this.defaultValue != this.value) {
														setDirtyFlag();
													}
												});

								$(
										"a:not(.protectedLinkId):not(.signOutLayerStyle2)")
										.on("click",function(event) {
											return isLostChanges();
										});

								$("#addRowNonTpa").on("click",function() {
									addRow("NonTpaDataTable", false);
								});

								$("#addRowTpa").on("click",function() {
									addRow("TpaDataTable", false);
								});

								$("#addPBAfee").on("click",function() {
									addRow("PbaDataTable", false);
								});

								$("#addRowPbaRestriction").on("click",
										function() {
											addRestrictionRow(
													"PbaRestrictionDataTable",
													false);
										});

								for (var i = 0; i < sections.length; i++) {
									setUpExpandCollapseIcon(sections[i]);
								}

								$(
										"#TpaDataTable tr[id=additionalFeesRow] td :input:enabled")
										.on("change",
												function() {
													if (this.defaultValue != this.value) {
														formModified = true;
														var rowCount = getAdditionalFeesRowCount("#TpaDataTable");
														if (rowCount == 1) {
															var lastRow = $("#TpaDataTable tr:visible:last");
															lastRow
																	.find(
																			'#deleteRow')
																	.attr(
																			'href',
																			'javascript://');
															lastRow
																	.find(
																			'#deleteRow')
																	.attr(
																			'click',
																			"delRow(this, 'TpaDataTable');");

														}
													}
												});

								$(
										"#NonTpaDataTable tr[id=additionalFeesRow] td :input:enabled")
										.on("change",
												function() {
													if (this.defaultValue != this.value) {
														formModified = true;
														var rowCount = getAdditionalFeesRowCount("#NonTpaDataTable");
														if (rowCount == 1) {
															var lastRow = $("#NonTpaDataTable tr:visible:last");
															lastRow
																	.find(
																			'#deleteRow')
																	.attr(
																			'href',
																			'javascript://');
															lastRow
																	.find(
																			'#deleteRow')
																	.attr(
																			'click',
																			"delRow(this, 'NonTpaDataTable');");

														}
													}
												});
								$(
										"#PbaDataTable tr[id=additionalFeesRow] td :input:enabled")
										.on("change",
												function() {
													if (this.defaultValue != this.value) {
														formModified = true;
														var rowCount = getAdditionalFeesRowCount("#PbaDataTable");
														if (rowCount == 1) {
															var lastRow = $("#PbaDataTable tr:visible:last");
															lastRow
																	.find(
																			'#deleteRow')
																	.attr(
																			'href',
																			'javascript://');
															lastRow
																	.find(
																			'#deleteRow')
																	.attr(
																			'click',
																			"delRow(this, 'PbaDataTable');");

														}
													}
												});

								$(
										"#PbaRestrictionDataTable tr[id=additionalRestrictionRow] td :input:enabled")
										.on("change",
												function() {
													if (this.defaultValue != this.value) {
														formModified = true;
														var rowCount = getAdditionalRestrictionsRowCount("#PbaRestrictionDataTable");
														if (rowCount == 1) {
															var lastRow = $("#PbaRestrictionDataTable tr:visible:last");
															lastRow
																	.find(
																			'#deleteRow')
																	.attr(
																			'href',
																			'javascript://');
															lastRow
																	.find(
																			'#deleteRow')
																	.attr(
																			'click',
																			"delRestrictionRow(this, 'PbaRestrictionDataTable');");

														}
													}
												});

								//Restriction validation
								if ($(
										'input[name="personalBrokerageAccountUi.pbaRestriction"]:checked')
										.val() == null
										|| $(
												'input[name="personalBrokerageAccountUi.pbaRestriction"]:checked')
												.val() == '') {
									document.getElementById('ResIndNo').checked = true;
								}

								if ($(
										'input[name="personalBrokerageAccountUi.pbaRestriction"]:checked')
										.val() == 'Y') {
									document
											.getElementById("PbaRestrictionDataTable").style.display = "block";
									document
											.getElementById("pbaRestrictionlink").style.display = "block";
								} else {
									document
											.getElementById("PbaRestrictionDataTable").style.display = "none";
									document
											.getElementById("pbaRestrictionlink").style.display = "none";
								}

								$(
										'input[name="personalBrokerageAccountUi.pbaRestriction"]')
										.on("click",
												function() {
													if ($(
															'input[name="personalBrokerageAccountUi.pbaRestriction"]:checked')
															.val() == 'Y') {
														document
																.getElementById("PbaRestrictionDataTable").style.display = "block";
														document
																.getElementById("pbaRestrictionlink").style.display = "block";
													} else if ($(
															'input[name="personalBrokerageAccountUi.pbaRestriction"]:checked')
															.val() == 'N') {
														document
																.getElementById("PbaRestrictionDataTable").style.display = "none";
														document
																.getElementById("pbaRestrictionlink").style.display = "none";
													}
												});

								disableDeleteButton("TpaDataTable");
								disableDeleteButton("NonTpaDataTable");
								disableDeleteButton("PbaDataTable");
								disableDeleteButtonForRestriction("PbaRestrictionDataTable");
							});
		} catch (e) {
			// If JQuery is not loaded, exception will be caught here
		}

		function disableDeleteButton(tableId) {
			var tableRows = getAdditionalFeesRowCount("#" + tableId);
			if (tableRows == 1) {
				var lastRow = $("#" + tableId + " tr:visible:last");
				lastRow.find("#deleteRow").removeAttr('href');
				lastRow.find("#deleteRow").removeAttr('click');
			}
		}

		function disableDeleteButtonForRestriction(tableId) {
			var tableRows = getAdditionalRestrictionsRowCount("#" + tableId);
			if (tableRows == 1) {
				var lastRow = $("#" + tableId + " tr:visible:last");
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

		function setDefaultsForRestriction(row) {
			row.find("#restrictionDesc").val("");
			row.find("#deletedInd").val("true");
			row.find(".errorIcon").remove();
		}

		function addRow(table_name, isDeleted) {
			var rowCount = getAdditionalFeesRowCount("#" + table_name);
			if ((rowCount == 5 && table_name != "PbaDataTable")
					|| (rowCount == 10 && table_name == "PbaDataTable")) {
				if (rowCount == 5 && table_name != "PbaDataTable") {
					alert('<content:getAttribute beanName="moreThan5CusotmFeeAdded" attribute="text" />');
				} else {
					alert('<content:getAttribute beanName="moreThan10PbaCustomFeeAdded" attribute="text" />');
				}
			}

			else {

				var lastRow = $("#" + table_name + " tr:visible:last");
				if (isDeleted == true) {
					lastRow.find("#deleteRow").removeAttr('href');
					lastRow.find("#deleteRow").removeAttr('click');
				} else {
					lastRow.find('#deleteRow').attr('href', 'javascript://');
					lastRow.find('#deleteRow').attr('click',
							"delRow(this, '" + table_name + "');");
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

		function addRestrictionRow(table_name, isDeleted) {
			var rowCount = getAdditionalRestrictionsRowCount("#" + table_name);
			if (rowCount == 10) {
				alert('<content:getAttribute beanName="moreThan10PbaRestrictionAdded" attribute="text" />');
			} else {
				var lastRow = $("#" + table_name + " tr:visible:last");
				if (isDeleted == true) {
					lastRow.find("#deleteRow").removeAttr('href');
					lastRow.find("#deleteRow").removeAttr('click');
				} else {
					lastRow.find('#deleteRow').attr('href', 'javascript://');
					lastRow.find('#deleteRow').attr('click',
							"delRestrictionRow(this, '" + table_name + "');");
				}

				var row = lastRow.clone(true);
				setDefaultsForRestriction(row);
				row.find("#deletedInd").val("false");
				var newIndex = $("#" + table_name
						+ " tr[id=additionalRestrictionRow]").length;
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

		function getAdditionalRestrictionsRowCount(tableId) {
			return $(tableId + " tr:visible[id=additionalRestrictionRow]").length;
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
				action="/do/edit404a5NoticeInfo/" name="noticeInfo404a5Form"
				modelAttribute="noticeInfo404a5Form">
				<input type="hidden" name="action" />
				<%--  input - name="noticeInfo404a5Form" --%>
				<input type="hidden" name="dirty" id="dirtyFlagId" />
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
						<TR>
							<td colspan="3" valign="middle"><a href="#"
								class="protectedLinkId"
								onclick="expandAllSections(); return false;"><img
									src="/assets/unmanaged/images/plus_icon_all.gif" class="icon"
									border="0"></a> / <a href="#"
								onclick="contractAllSections(); return false;"
								class="protectedLinkId"><img
									src="/assets/unmanaged/images/minus_icon_all.gif" class="icon"
									border="0"></a> <b>All sections</b></td>
						</TR>
						<TR>
							<TD colspan="3" height=25 vAlign=center style="padding-left: 6px">&nbsp</TD>
						</TR>
						<TR class=tablehead>
							<td class="databorder" height="25"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							<TD class="tableheadTD" height=25 vAlign=center
								style="padding-left: 6px"><img
								id="dimSectionExpandCollapseIcon"
								src="/assets/unmanaged/images/minus_icon.gif" width="13"
								height="13" style="cursor: hand; cursor: pointer">&nbsp; <b>Designated
									Investment Manager</b></TD>
							<TD class=databorder><IMG
								src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
						</TR>

						<TR class=" dimSection">
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
							<TD vAlign="top" width="">
								<TABLE border="0" cellSpacing="0" cellPadding="0" width="100%">
									<TBODY>
										<TR class=datacell1>
											<TD width="100%" colspan="3"><content:getAttribute
													beanName="introDim" attribute="text" /></TD>
										</TR>

										<TR class=datacell2>
											<TD vAlign=top width="19%">&nbsp;<B>First Name</B> <ps:fieldHilight
													name="firstName" singleDisplay="true" className="errorIcon"
													displayToolTip="true" />
											</TD>
											<td width="31%"><form:input
													path="designatedInvestmentManagerUi.firstName"
													maxlength="30" cssStyle="WIDTH: 98%" /></td>


											<TD width="50%" colspan="3">&nbsp;</TD>
										</TR>

										<TR class=datacell1>
											<TD vAlign=top width="19%">&nbsp;<B>Last Name</B> <ps:fieldHilight
													name="lastName" singleDisplay="true" className="errorIcon"
													displayToolTip="true" />
											</TD>
											<td width="31%"><form:input
													path="designatedInvestmentManagerUi.lastName"
													maxlength="30" cssStyle="WIDTH: 98%" /></td>
											<TD width="50%" colspan="3">&nbsp;</TD>
										</TR>

										<TR class=datacell2>
											<TD vAlign=top width="19%">&nbsp;<B>Company</B> <ps:fieldHilight
													name="company" singleDisplay="true" className="errorIcon"
													displayToolTip="true" />
											</TD>
											<td width="31%"><form:input
													path="designatedInvestmentManagerUi.company" maxlength="30"
													cssStyle="WIDTH: 98%" /></td>


											<TD width="50%" colspan="3">&nbsp;</TD>
										</TR>

										<TR class=datacell1>
											<TD vAlign=top width="19%">&nbsp;<B>Address Line 1</B>&nbsp;<ps:fieldHilight
													name="addressLine1" singleDisplay="true"
													className="errorIcon" displayToolTip="true" />
											</TD>
											<td width="31%"><form:input
													path="designatedInvestmentManagerUi.addressLine1"
													maxlength="30" cssStyle="WIDTH: 98%" /></td>


											<TD width="50%" colspan="3">&nbsp;</TD>
										</TR>

										<TR class=datacell2>
											<TD vAlign=top width="19%">&nbsp;<B>Address Line 2</B> <ps:fieldHilight
													name="addressLine2" singleDisplay="true"
													className="errorIcon" displayToolTip="true" />
											</TD>
											<td width="21%"><form:input
													path="designatedInvestmentManagerUi.addressLine2"
													maxlength="30" cssStyle="WIDTH: 98%" /></td>


											<TD width="50%" colspan="3">&nbsp;</TD>
										</TR>

										<TR class=datacell1>
											<TD vAlign=top width="19%">&nbsp;<B>City</B>&nbsp;<ps:fieldHilight
													name="city" singleDisplay="true" className="errorIcon"
													displayToolTip="true" />
											</TD>
											<td width="31%"><form:input
													path="designatedInvestmentManagerUi.city" maxlength="30"
													cssStyle="WIDTH: 98%" /></td>


											<TD width="50%" colspan="3">&nbsp;</TD>
										</TR>

										<TR class=datacell2>
											<TD vAlign=top width="19%">&nbsp;<B>State</B>&nbsp;<ps:fieldHilight
													name="state" singleDisplay="true" className="errorIcon"
													displayToolTip="true" />
											</TD>
											<td width="31%"><form:select
													path="designatedInvestmentManagerUi.state">

													<form:option value="" />
													<form:options items="${noticeInfo404a5Form.states}" />
												</form:select></td>
											<TD width="50%" colspan="3">&nbsp;</TD>
										</TR>

										<TR class=datacell1>
											<TD vAlign=top width="19%">&nbsp;<B>Zip</B>&nbsp;<ps:fieldHilight
													name="zipCode" singleDisplay="true" className="errorIcon"
													displayToolTip="true" />
											</TD>
											<td width="31%"><form:input
													path="designatedInvestmentManagerUi.zipCode1" maxlength="5"
													cssStyle="WIDTH: 48%" /> <form:input
													path="designatedInvestmentManagerUi.zipCode2" maxlength="4"
													cssStyle="WIDTH: 48%" /></td>




											<TD width="50%" colspan="3">&nbsp;</TD>
										</TR>

										<TR class=datacell2>
											<TD vAlign=top width="19%">&nbsp;<B>Phone Number</B>&nbsp;<ps:fieldHilight
													name="phone" singleDisplay="true" className="errorIcon"
													displayToolTip="true" />
											</TD>
											<td width="31%">
												<div style="white-space: nowrap">
													<form:input
														path="designatedInvestmentManagerUi.phonePrefix"
														maxlength="3" cssStyle="WIDTH: 14%" />


													<form:input
														path="designatedInvestmentManagerUi.phoneAreaCode"
														maxlength="3" cssStyle="WIDTH: 14%" />


													<form:input
														path="designatedInvestmentManagerUi.phoneNumber"
														maxlength="4" cssStyle="WIDTH: 23%" />


													&nbsp; ext.
													<form:input path="designatedInvestmentManagerUi.phoneExt"
														maxlength="6" cssStyle="WIDTH: 30%" />


												</div>
											</td>
											<TD width="50%" colspan="3">&nbsp;</TD>
										</TR>

										<TR class=datacell1>
											<TD vAlign=top width="19%">&nbsp;<B>Email Address</B>&nbsp;<ps:fieldHilight
													name="emailAddress" singleDisplay="true"
													className="errorIcon" displayToolTip="true" />
											</TD>
											<td width="31%"><form:input
													path="designatedInvestmentManagerUi.emailAddress"
													maxlength="99" cssStyle="WIDTH: 98%" /></td>


											<TD width="50%" colspan="3">&nbsp;</TD>
										</TR>

										<TR class=datacell2>
											<TD vAlign=top width="19%">&nbsp;<B>Special Notes</B>&nbsp;<ps:fieldHilight
													name="specialNotes" singleDisplay="true"
													className="errorIcon" displayToolTip="true" /></TD>
											<td width="31%"><form:input
													path="designatedInvestmentManagerUi.specialNotes"
													maxlength="140" cssStyle="WIDTH: 98%" /></td>


											<TD width="50%" colspan="3">&nbsp;</TD>
										</TR>

									</TBODY>
								</TABLE>
							</TD>
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</tr>

						<TR>
							<TD class="databorder" colSpan="3"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>
						<TR>
							<TD colspan="3" height=25 vAlign=center style="padding-left: 6px">&nbsp</TD>
						</TR>
						<!-- PBA requirements:start -->
						<TR class="tablehead">
							<td class="databorder" height="25"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							<TD class="tableheadTD" height=25 vAlign=center
								style="padding-left: 6px"><img
								id="pbaSectionExpandCollapseIcon"
								src="/assets/unmanaged/images/minus_icon.gif" width="13"
								height="13" style="cursor: hand; cursor: pointer">&nbsp; <b>Personal
									Brokerage Account</b></TD>

							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>

						<TR class="pbaSection">
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
							<TD vAlign="top" width="">
								<TABLE border="0" cellSpacing="0" cellPadding="0" width="100%">
									<TBODY>
										<TR class=datacell1>
											<TD width="100%" colspan="3"><content:getAttribute
													beanName="introPba" attribute="text" /></TD>
										</TR>

										<TR class=datacell2>
											<TD vAlign="bottom" width="19%">&nbsp;<B>PBA
													Provider Name</B> <ps:fieldHilight name="pbaProviderName"
													singleDisplay="true" className="errorIcon"
													displayToolTip="true" />
											</TD>
											<td width="31%"><form:input
													path="personalBrokerageAccountUi.pbaProviderName"
													maxlength="100" cssStyle="WIDTH: 98%" /></td>


											<TD width="50%" colspan="3">&nbsp;</TD>
										</TR>

										<TR class=datacell1>
											<TD vAlign="bottom" width="19%">&nbsp;<B>PBA Phone
													Number</B>&nbsp;<ps:fieldHilight name="pbaPhone"
													singleDisplay="true" className="errorIcon"
													displayToolTip="true" />
											</TD>
											<td width="31%">
												<div style="white-space: nowrap">
													<form:input
														path="personalBrokerageAccountUi.pbaPhoneAreaCode"
														maxlength="3" cssStyle="WIDTH: 14%" />


													<form:input
														path="personalBrokerageAccountUi.pbaPhonePrefix"
														maxlength="3" cssStyle="WIDTH: 14%" />


													<form:input
														path="personalBrokerageAccountUi.pbaPhoneNumber"
														maxlength="4" cssStyle="WIDTH: 20%" />


													&nbsp; PBA ext.
													<form:input path="personalBrokerageAccountUi.pbaPhoneExt"
														maxlength="6" cssStyle="WIDTH: 26%" />


												</div>
											</td>
											<TD width="50%" colspan="3">&nbsp;</TD>
										</TR>

										<TR class=datacell2>
											<TD vAlign="bottom" width="19%">&nbsp;<B>PBA Email
													Address</B>&nbsp;<ps:fieldHilight name="pbaEmailAddress"
													singleDisplay="true" className="errorIcon"
													displayToolTip="true" />
											</TD>
											<td width="31%"><form:input
													path="personalBrokerageAccountUi.pbaEmailAddress"
													maxlength="99" cssStyle="WIDTH: 98%" /></td>


											<TD width="50%" colspan="3">&nbsp;</TD>
										</TR>

										<TR class=datacell1>
											<TD vAlign="bottom" width="43%">&nbsp;<B><content:getAttribute
														beanName="pbaMinDeposit" attribute="text" /></B> <ps:fieldHilight
													name="pbaMinDeposit" singleDisplay="true"
													className="errorIcon" displayToolTip="true" />
											</TD>
											<td width="31%">$ <form:input
													path="personalBrokerageAccountUi.pbaMinDeposit"
													maxlength="12" cssStyle="WIDTH: 95%" />


											</td>
											<TD width="50%" colspan="3">&nbsp;</TD>
										</TR>

										<TR class=datacell2>
											<TD vAlign="bottom" width="19%">&nbsp;<B><content:getAttribute
														beanName="pbaRestrictionLabel" attribute="text" /></B> <ps:fieldHilight
													name="company" singleDisplay="true" className="errorIcon"
													displayToolTip="true" />
											</TD>
											<td width="50%" colspan="3">
												<!--//TODO Need to have Radio buttons  --> <form:radiobutton
													path="personalBrokerageAccountUi.pbaRestriction"
													id="ResIndYes" value="Y" />Yes <form:radiobutton
													path="personalBrokerageAccountUi.pbaRestriction"
													id="ResIndNo" value="N" />No

											</td>

										</TR>

										<TR class=datacell1>
											<TD width="100%" colspan="3">
												<TABLE id="PbaRestrictionDataTable" border="0"
													cellSpacing="0" cellPadding="0" width=""
													style="border-collapse: collapse; table-layout: fixed">
													<TBODY>
														<TD width="100%" colspan="3"><B><content:getAttribute
																	beanName="pbaRestrictionInstructions" attribute="text" /></B>
															<ps:fieldHilight name="pbaRestriction"
																singleDisplay="true" className="errorIcon"
																displayToolTip="true" /></TD>

														<c:forEach
															items="${noticeInfo404a5Form.pbaRestrictionList}"
															var="pbaRestriction" varStatus="rowIndex">
															<c:set var="indexValue" value="${rowIndex.index}" />
															<c:if test="${pbaRestriction.deletedInd eq true }">
																<tr style="display: none;" class="datacell1"
																	id="additionalRestrictionRow" height="25">
															</c:if>

															<c:if test="${ pbaRestriction.deletedInd eq false }">
																<TR class="datacell1" id="additionalRestrictionRow"
																	height="25">
															</c:if>

															<TD style="padding-left: 4px; WIDTH: 2.5%"><A
																class="protectedLinkId" href="javascript://"
																id="deleteRow"
																onClick="delRestrictionRow(this, 'PbaRestrictionDataTable');"><IMG
																	border=0 src="/assets/unmanaged/images/delete_icon.gif"></A></TD>
															<!--  Restriction Description -->
															<TD>
																<div style="display: inline; white-space: nowrap;">
																	<form:input
																		path="pbaRestrictionList[${indexValue}].restrictionDesc"
																		maxlength="500" cssStyle="WIDTH: 90%"
																		id="restrictionDesc" />


																	<ps:fieldHilight
																		name="pbaRestriction.restrictionDesc${indexValue}"
																		singleDisplay="true" className="errorIcon"
																		displayToolTip="true" />
																	<form:hidden  path="pbaRestrictionList[${indexValue}].deletedInd" id="deletedInd" />
																	<%-- input - indexed="true" name="pbaRestriction" --%>
																</div>

															</TD>
															</TR>
														</c:forEach>

													</TBODY>
												</TABLE>
											</TD>
										</TR>
										<TR class=datacell1 id="pbaRestrictionlink">
											<td valign="middle" width="100%" colspan="2"><a
												class="protectedLinkId" id="addRowPbaRestriction"
												href="javascript://"> Create additional restriction</a></td>
										</TR>

										</TR>

										<TR class=datacell1>
											<td width="31%"></td>
											<TD width="50%" colspan="3">&nbsp;</TD>
										</TR>
									</TBODY>
								</TABLE>
							</TD>
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</tr>

						<TR class="pbaSection">
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
							<TD vAlign="top" width="">
								<TABLE id="PbaDataTable" border="0" cellSpacing="0"
									cellPadding="0" width="100%"
									style="border-collapse: collapse; table-layout: fixed">
									<TBODY>
										<TR style="background: #cccccc">
											<%-- Fee Type --%>
											<TD width="20"><IMG src="/assets/unmanaged/images/s.gif"
												width="1" height="1"></TD>
											<TD width="350"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<%-- Data divider --%>
											<TD width="1"><IMG src="/assets/unmanaged/images/s.gif"
												width="1" height="1"></TD>
											<%-- Value drop-down --%>
											<TD width="80" class="type"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<%-- Value text box --%>
											<TD width="350" class=type><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>

										</TR>
										<TR class="tablesubhead" height="24">
											<TD style="padding-left: 4px;" colspan="2"><b>Fee
													type</b>
											<ps:fieldHilight name="pbaFee" singleDisplay="true"
													className="errorIcon" displayToolTip="true" /></TD>
											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<TD style="padding-left: 4px;" valign="middle" align="left"
												colspan="4"><b>Value&nbsp;</b></TD>
										</TR>
										<%-- Standard Fee List --%>
										<c:forEach items="${noticeInfo404a5Form.standardPBAFees}"
											var="pbaStandardFee" varStatus="rowIndex">
											<c:set var="indexValue" value="${rowIndex.index}" />

											<%
												PBAFeeUIHolder theRecords = (PBAFeeUIHolder) (pageContext.getAttribute("pbaStandardFee"));

														String temp = pageContext.getAttribute("indexValue").toString();
														boolean beigeBackgroundCls = false; // used to determine if the cell should use the background style
														boolean lastLineBkgrdClass = false; // used to determine if the last line should be beige or not
											%>
											<%
												if (Integer.parseInt(temp) % 2 != 0) {
															beigeBackgroundCls = true;
															lastLineBkgrdClass = true;
											%>
											<tr class="datacell1">
												<%
													} else {
																beigeBackgroundCls = false;
																lastLineBkgrdClass = false;
												%>
											
											<tr class="datacell2">
												<%
													}
												%>
												<%--  Fee Description --%>
												<TD style="padding-left: 4px;" vAlign="bottom" colspan="2">
													<B>${pbaStandardFee.feeDescription}</B> <form:hidden path ="standardPBAFees[${indexValue}].deleted" id="pbaDeletedInd" /> <ps:fieldHilight
														name="pbaStandardFee${indexValue}" singleDisplay="true"
														className="errorIcon" displayToolTip="true" />
												</TD>
												<TD class="dataheaddivider"><IMG
													src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>

												<%-- Amount Type --%>
												<TD align="right"><form:select
														path="standardPBAFees[${indexValue}].feeValueType"
														styleId="pbaAmountType">
														<%--  indexed="true"   --%>

														<form:option value="D">$</form:option>
														<form:option value="P">%</form:option>
													</form:select></TD>

												<%-- Amount Value--%>
												<TD style="padding-left: 4px;" valign="middle" align="left"
													colspan="4"><form:input
														path="standardPBAFees[${indexValue}].feeValue"
														maxlength="7" size="11" cssStyle="{text-align: right}"
														id="pbaAmountValue" /></TD>




											</TR>
										</c:forEach>
										<%-- Custom Fee List --%>
										<c:forEach items="${noticeInfo404a5Form.customPBAFees}"
											var="pbaCustomizedFee" varStatus="rowIndex">

											<%
												PBAFeeUIHolder feeDetails = (PBAFeeUIHolder) (pageContext.getAttribute("pbaCustomizedFee"));
											%>

											<c:if test="${ pbaCustomizedFee.deleted  eq true }">
												<tr style="display: none;" class="datacell1"
													id="additionalFeesRow" height="25">
											</c:if>

											<c:if test="${ pbaCustomizedFee.deleted  eq false }">
												<TR class="datacell1" id="additionalFeesRow" height="25">
											</c:if>

											<%--  Fee Description --%>
											<TD style="padding-left: 4px;"><A
												class="protectedLinkId" href="javascript://" id="deleteRow"
												onClick="delRow(this, 'PbaDataTable');"><IMG border=0
													src="/assets/unmanaged/images/delete_icon.gif"></A></TD>

											<%-- Amount Type --%>
											<TD>
												<div style="display: inline; white-space: nowrap;">
													<form:input
														path="customPBAFees[${rowIndex.index}].feeDescription"
														maxlength="50" cssStyle="WIDTH: 90%" id="feeDescription" />

													<form:hidden
														path="customPBAFees[${rowIndex.index}].deleted"
														id="deletedInd" />
													<ps:fieldHilight name="pbaCustomizedFee${rowIndex.index}"
														singleDisplay="true" className="errorIcon"
														displayToolTip="true" />
												</div>
											</TD>
											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>

											<%-- Amount Type --%>
											<TD align="right"><form:select
													path="customPBAFees[${rowIndex.index}].feeValueType"
													styleId="amountType">
													<%-- indexed="true"  --%>


													<form:option value="D">$</form:option>
													<form:option value="P">%</form:option>
												</form:select></TD>

											<%-- Amount Value--%>
											<TD style="padding-left: 4px;" valign="middle" align="left"
												colspan="4"><form:input
													path="customPBAFees[${rowIndex.index}].feeValue"
													maxlength="7" size="11" cssStyle="{text-align: right}"
													id="amountValue" /></TD>




											</TR>
										</c:forEach>

									</TBODY>
								</TABLE>
							</TD>
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>
						<TR class="datacell1 pbaSection" height="25">
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
							<TD vAlign="middle" style="padding-left: 4px;"><a
								class="protectedLinkId" id="addPBAfee" href="javascript://">Create
									additional fee type</a></TD>
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>

						<TR>
							<TD class="databorder" colSpan="3"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>
						<TR>
							<TD colspan="3" height=25 vAlign=center style="padding-left: 6px">&nbsp</TD>
						</TR>
						<!-- PBA requirements: end -->
						<TR class="tablehead">
							<td class="databorder" height="25"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							<TD class="tableheadTD" height=25 vAlign=center
								style="padding-left: 6px"><img
								id="nonTpaFeeSectionExpandCollapseIcon"
								src="/assets/unmanaged/images/minus_icon.gif" width="13"
								height="13" style="cursor: hand; cursor: pointer">&nbsp; <b>Individual
									Plan Expenses</b></TD>

							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>

						<tr class="datacell1 nonTpaFeeSection">
							<td class="databorder" height="25"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							<td height="25" valign="center"><content:getAttribute
									beanName="introNonTpaFee" attribute="text" /></td>
							<td class="databorder"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
						</tr>


						<TR class="nonTpaFeeSection">
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
							<TD vAlign="top" width="">
								<TABLE id="NonTpaDataTable" border="0" cellSpacing="0"
									cellPadding="0" width="100%"
									style="border-collapse: collapse; table-layout: fixed">
									<TBODY>
										<TR style="background: #cccccc">
											<!-- Fee Type -->
											<TD width="20"><IMG src="/assets/unmanaged/images/s.gif"
												width="1" height="1"></TD>
											<TD width="186"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<%-- Data divider --%>
											<TD width="1"><IMG src="/assets/unmanaged/images/s.gif"
												width="1" height="1"></TD>
											<%-- Value drop-down --%>
											<TD width="80" class="type"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<%-- Value text box --%>
											<TD width="90" class=type><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<%-- Data divider --%>
											<TD width="1"><IMG src="/assets/unmanaged/images/s.gif"
												width="1" height="1"></TD>
											<%-- Special notes--%>
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



										<%-- Custom Fee List --%>
										<c:forEach items="${noticeInfo404a5Form.nonTpaFees}"
											var="nonTpaFee" varStatus="rowIndex">
											<%
												FeeUIHolder feeDetail = (FeeUIHolder) pageContext.getAttribute("nonTpaFee");
											%>
test
											<c:if test="${ nonTpaFee.deleted  eq true }">
												<tr style="display: none;" class="datacell1"
													id="additionalFeesRow" height="25">
											</c:if>

											<c:if test="${ nonTpaFee.deleted  eq false }">
												<TR class="datacell1" id="additionalFeesRow" height="25">
											</c:if>

											<%--  Fee Description --%>
											<TD style="padding-left: 4px;"><A
												class="protectedLinkId" href="javascript://" id="deleteRow"
												onClick="delRow(this, 'NonTpaDataTable');"><IMG border=0
													src="/assets/unmanaged/images/delete_icon.gif"></A></TD>

											<%-- Amount Type --%>
											<TD>
												<div style="display: inline; white-space: nowrap;">
													<form:input path="nonTpaFees[${rowIndex.index}].feeDescription"
														value="${nonTpaFee.feeDescription}" maxlength="50"
														cssStyle="WIDTH: 90%" id="feeDescription" />
						
													<form:hidden path="nonTpaFees[${rowIndex.index}].deleted" id="deletedInd" />
						
													<ps:fieldHilight name="nonTpaFee${rowIndex.index}"
														singleDisplay="true" className="errorIcon"
														displayToolTip="true" />
												</div>
											</TD>
											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>

											<!-- Amount Type -->
											<TD align="right"><form:select
													path="nonTpaFees[${rowIndex.index}].feeValueType"
													value="${nonTpaFee.feeValueType}" styleId="amountType">
													<form:option value="$" />
													<form:option value="%" />
												</form:select></TD>

											<%-- Amount Value--%>
											<TD align="right"><form:input
													path="nonTpaFees[${rowIndex.index}].feeValue" value="${nonTpaFee.feeValue}"
													maxlength="7" size="11" cssStyle="{text-align: right}"
													id="amountValue" />
											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>

											<%-- Notes --%>
											<TD><form:input path="nonTpaFees[${rowIndex.index}].notes"
													value="${nonTpaFee.notes}" maxlength="140"
													cssStyle="WIDTH: 98%" id="notes" />
											</TD>


											</TR>
										</c:forEach>

									</TBODY>
								</TABLE>
							</TD>
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>
						<TR class="datacell1 nonTpaFeeSection" height="25">
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
							<TD vAlign="middle" style="padding-left: 4px;"><a
								class="protectedLinkId" id="addRowNonTpa" href="javascript://">Create
									additional fee type</a></TD>
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>
						<TR>
							<TD class="databorder" colSpan="3"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>
						<TR>
							<TD colspan="3" height=25 vAlign=center style="padding-left: 6px">&nbsp</TD>
						</TR>

						<TR class="tablehead">
							<td class="databorder" height="25"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							<TD class="tableheadTD" vAlign="top" width=""
								style="padding-left: 6px">
								<TABLE border="0" cellSpacing="0" cellPadding="0" width="100%"
									style="border-collapse: collapse">
									<TBODY>
										<TR style="background: #cccccc">
											<TD class="tableheadTD" height=25 vAlign=center colSpan=10><img
												id="tpaFeeSectionExpandCollapseIcon"
												src="/assets/unmanaged/images/minus_icon.gif" width="13"
												height="13" style="cursor: hand; cursor: pointer">&nbsp;
												<b>Individual TPA Expenses</b></TD>
											<TD class=tableheadTD height=25 vAlign=center colSpan=4>
												<c:if test="${ noticeInfo404a5Form.tpaRestricted  eq true }">
													<form:checkbox path="tpaRestricted" disabled="true"/>

											           TPA access restricted
												</c:if>
											</TD>
										</TR>
									</TBODY>
								</TABLE>
							</TD>
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>

						<tr class="datacell1 tpaFeeSection">
							<td class="databorder" height="25"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							<td height="25" valign="center"><content:getAttribute
									beanName="introTpaFee" attribute="text" /></td>
							<td class="databorder"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
						</tr>

						<TR class="tpaFeeSection">
							<TD class="databorder" width="1"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
							<TD vAlign="top" width="">
								<TABLE id="TpaDataTable" border="0" cellSpacing="0"
									cellPadding="0" width="100%"
									style="border-collapse: collapse; table-layout: fixed">
									<TBODY>
										<TR style="background: #cccccc">
											<%-- Fee Type --%>
											<TD width="20"><IMG src="/assets/unmanaged/images/s.gif"
												width="1" height="1"></TD>
											<TD width="186"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<%-- Data divider --%>
											<TD width="1"><IMG src="/assets/unmanaged/images/s.gif"
												width="1" height="1"></TD>
											<%-- Value drop-down --%>
											<TD width="80" class="type"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<%-- Value text box --%>
											<TD width="90" class=type><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<%-- Data divider --%>
											<TD width="1"><IMG src="/assets/unmanaged/images/s.gif"
												width="1" height="1"></TD>
											<%-- Special notes--%>
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

										<%-- Standard Fee List --%>
										<c:forEach items="${noticeInfo404a5Form.tpaFeesStandard}"
											var="tpaStandardFee" varStatus="rowIndex">
											<c:set var="indexValue" value="${rowIndex.index}" />
											<%
														String temp = pageContext.getAttribute("indexValue").toString();
														boolean beigeBackgroundCls = false; // used to determine if the cell should use the background style
														boolean lastLineBkgrdClass = false; // used to determine if the last line should be beige or not
											%>
											<%
												if (Integer.parseInt(temp) % 2 != 0) {
															beigeBackgroundCls = true;
															lastLineBkgrdClass = true;
											%>
											<tr class="datacell1">
												<%
													} else {
																beigeBackgroundCls = false;
																lastLineBkgrdClass = false;
												%>
											
											<tr class="datacell2">
												<%
													}
												%>
												<%--  <c:choose>
										    <c:when test="${rowIndex % 2 == 0}">
										     <tr class="datacell2 ${tpaStandardFee.disabled}"> 
										    </c:when>
										    <c:otherwise>
										      <tr class="datacell1 ${tpaStandardFee.disabled}">
										     </c:otherwise>
										    </c:choose>  --%>
												<%--  Fee Description --%>
												<TD style="padding-left: 4px;" vAlign="top" colspan="2">
													<B>${tpaStandardFee.feeDescription}</B> 
													<form:hidden path="tpaFeesStandard[${indexValue}].deleted" id="deletedInd" />
													<ps:fieldHilight
														name="tpaStandardFee${rowIndex.index}" singleDisplay="true"
														className="errorIcon" displayToolTip="true" />
												</TD>
												<TD class="dataheaddivider"><IMG
													src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>

												<%-- Amount Type --%>
												<TD align="right"><c:if
														test="${tpaStandardFee.feeDescription eq 'Loan Set-up'}">

${tpaStandardFee.feeValueType}
</c:if> <c:if test="${tpaStandardFee.feeDescription ne 'Loan Set-up'}">

														<form:select path="tpaFeesStandard[${indexValue}].feeValueType"
															disabled="${tpaStandardFee.disabled}" styleId="amountType">
															<%--  indexed="true"  --%>

															<form:option value="$" />
															<form:option value="%" />
														</form:select>
													</c:if></TD>

												<%-- Amount Value--%>
												<TD align="right"><form:input path="tpaFeesStandard[${indexValue}].feeValue"
														value="${tpaStandardFee.feeValue}"
														disabled="${tpaStandardFee.disabled}" maxlength="7" size="11"
														cssStyle="{text-align: right}" id="amountValue" />
													<%--  form:input - indexed="true" name="tpaStandardFee" --%></TD>




												<TD class="dataheaddivider"><IMG
													src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>

												<%-- Notes --%>
												<TD><form:input path="tpaFeesStandard[${indexValue}].notes"
														value="${tpaStandardFee.notes}"
														disabled="${tpaStandardFee.disabled}" maxlength="140"
														cssStyle="WIDTH: 98%" />
												</TD>


											</TR>
										</c:forEach>

										<%-- Custom Fee List --%>
										<c:forEach items="${noticeInfo404a5Form.tpaFeesCustomized}"
											var="tpaCustomizedFee" varStatus="rowIndex">
											
											<c:if test="${ tpaCustomizedFee.deleted  eq true }">
												<tr style="display: none;" class="datacell1"
													id="additionalFeesRow" height="25">
											</c:if>

											<c:if test="${ tpaCustomizedFee.deleted  eq false }">
												<TR class="datacell1" id="additionalFeesRow" height="25">
											</c:if>

											<%--  Fee Description --%>
											<TD style="padding-left: 4px;"><A
												class="protectedLinkId" href="javascript://" id="deleteRow"
												onClick="delRow(this, 'TpaDataTable');"><IMG border=0
													src="/assets/unmanaged/images/delete_icon.gif"></A></TD>

											<%-- Amount Type --%>
											<TD>
												<div style="display: inline; white-space: nowrap;">
													<form:input path="tpaFeesCustomized[${rowIndex.index}].feeDescription"
														value="${tpaCustomizedFee.feeDescription}" maxlength="50"
														cssStyle="WIDTH: 90%" id="feeDescription" />
													<%--  form:input - indexed="true" name="tpaCustomizedFee" --%>


													<form:hidden path="tpaFeesCustomized[${rowIndex.index}].deleted" id="deletedInd" />
													
													<ps:fieldHilight name="tpaCustomizedFee${rowIndex.index}"
														singleDisplay="true" className="errorIcon"
														displayToolTip="true" />
												</div>
											</TD>
											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>

											<%-- Amount Type --%>
											<TD align="right"><form:select
													path="tpaFeesCustomized[${rowIndex.index}].feeValueType" styleId="amountType">

													<form:option value="$" />
													<form:option value="%" />
												</form:select></TD>

											<%-- Amount Value--%>
											<TD align="right"><form:input path="tpaFeesCustomized[${rowIndex.index}].feeValue"
													value="${tpaCustomizedFee.feeValue}" maxlength="7"
													size="11" cssStyle="{text-align: right}" id="amountValue" />
											</TD>



											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>

											<%-- Notes --%>
											<TD><form:input path="tpaFeesCustomized[${rowIndex.index}].notes"
													value="${tpaCustomizedFee.notes}" maxlength="140"
													cssStyle="WIDTH: 98%" id="notes" />
											</TD>


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
						<TR>
							<TD class="databorder" colSpan="3"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						</TR>
					</TBODY>
				</TABLE>
				<br>

				<TABLE border=0 cellSpacing=0 cellPadding=0 width=730>
					<tr>

						<td width="410" align="right"></td>

						<td width="160" align="right"><input type="button"
							onclick="return doCancelEdit();" name="button" class="button134"
							value="cancel" /></td>



						<td width="160" align="right"><input type="button"
							onclick="return doNext();" name="button" class="button134"
							value="next" /></td>


					</tr>
				</table>

			</ps:form> <script language="JavaScript" type="text/javascript"
				src="/assets/unmanaged/javascript/tooltip.js"></script>
	</tr>
</table>
<c:if test="${empty param.printFriendly}">
	<table cellpadding="0" cellspacing="0" border="0" width="730"
		class="fixedTable" height="">
		<tr>
			<td width="30">&nbsp;</td>
			<td width="700"><content:pageFooter beanName="layoutPageBean" /></td>
		</tr>
	</table>
</c:if>
