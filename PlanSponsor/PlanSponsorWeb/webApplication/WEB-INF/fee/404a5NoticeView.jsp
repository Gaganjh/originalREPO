<%-- Tag Libs --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>

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
	contentId="<%=ContentConstants.PLAN_PROVISION_IS_NOT_SELECTED_MESSAGE%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="planProvisionRestrictionMsg" />
	
<content:contentBean
	contentId="<%=ContentConstants.PBA_RESTRICTION_INSTRUCTIONS_VIEW%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="pbaRestrictionMsg" />
	
<content:contentBean
	contentId="<%=ContentConstants.PBA_RESTRICTION_LABEL%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="pbaRestrictionLabel" />
	
<content:contentBean
	contentId="<%=ContentConstants.PBA_SECTION_INTRO_VIEW%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="pbaSectionintroview" />
	
<content:contentBean
	contentId="<%=ContentConstants.PBA_MIN_DEPOSIT_VIEW%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="pbaMindepositview" />		
	
	
<style type="text/css">
	tr.true td {   color: #7e7e7e;
			}
	
	tr.false td { 
		color: #000000;
			}
</style>


<%-- Java Scripts --%>
<script>
$(document).ready(function(){
	$("div.some-area").text(function(index, currentText) {
		var trimTest= currentText.trim();
		return trimTest.substr(0, 150);
	});
});
</script> 

<c:if test="${empty technicalDifficulties}">
	<c:if test="${empty param.printFriendly }" >

		<script language="JavaScript1.2" type="text/javascript">
			var sections = [ "dimSection","pbaSection", "nonTpaFees", "tpaFees" ];

			$(document).ready(function() {
				for ( var i = 0; i < sections.length; i++) {
					setUpExpandCollapseIcon(sections[i]);

					var rowIndex = $("." + sections[i] + "Count").length;
					if (rowIndex == 0) {
						contractSection(sections[i]);
					}
				}
				
				$("tr.true").on("mouseover",function() {    
					Tip('<content:getAttribute beanName="planProvisionRestrictionMsg" attribute="text" />'); }
				).on("mouseout",function(){    UnTip();  });
				
			});

			function setUpExpandCollapseIcon(sectionId) {
				var icon = $("#" + sectionId + "ExpandCollapseIcon");
				var section = $("#" + sectionId);
				icon.clicktoggle(function() {
					$('tr.' + sectionId).hide();
					icon.attr('src', '/assets/unmanaged/images/plus_icon.gif');
				},
						function() {
							$('tr.' + sectionId).show();
							icon.attr('src',
									'/assets/unmanaged/images/minus_icon.gif');
						});
			}

			function expandAllSections() {
				for ( var i = 0; i < sections.length; i++) {
					var icon = $("#" + sections[i] + "ExpandCollapseIcon");
					if (icon.attr('src') == '/assets/unmanaged/images/plus_icon.gif') {
						icon.trigger("click");
					}
				}
			}

			function contractAllSections() {
				for ( var i = 0; i < sections.length; i++) {
					var icon = $("#" + sections[i] + "ExpandCollapseIcon");
					if (icon.attr('src') == '/assets/unmanaged/images/minus_icon.gif') {
						icon.trigger("click");
					}
				}
			}

			function contractSection(section) {
				var icon = $("#" + section + "ExpandCollapseIcon");
				if (icon.attr('src') == '/assets/unmanaged/images/minus_icon.gif') {
					icon.trigger("click");
				}
			}

			function doEdit() {
			    document.noticeInfo404a5Form.action.value = "edit";
				document.noticeInfo404a5Form.submit();
			}
		</script>
	</c:if>
</c:if>
<%-- technical difficulties --%>


<td>
	<TABLE width="730" border="0" cellspacing="0" cellpadding="0"
		padding="0">
		<TBODY>
			<tr>
			<td colspan="9">
			<div id="messagesBox" class="messagesBox">
	        <%-- Override max height if print friendly is on so we don't scroll --%>
	        <ps:messages scope="session" suppressDuplicateMessages="true" />
            </div>
            <br />
			</td>
			</tr>
			<c:if test="${empty param.printFriendly }" >
				<tr>
					<td colspan="9"><a href="/do/view404a5NoticeInfoChangeHistory/">Change
							History</a><br></td>
				</tr>
			</c:if>
			<TR>
				<TD>&nbsp;</TD>
				<TD width=10><IMG border=0
					src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
				<TD>&nbsp;</TD>
			</TR>
			<TR>
				<td bgcolor="#FFF9F2" width="342"><a href="#"
					onclick="expandAllSections(); return false;"> <img
						src="/assets/unmanaged/images/plus_icon_all.gif" class="icon" border="0"></a>
					/ <a href="#" onclick="contractAllSections(); return false;"> <img
						src="/assets/unmanaged/images/minus_icon_all.gif" class="icon" border="0"></a>
					<b>All sections</b></td>
			</TR>
			<TR >
							 <TD  colspan="3" height=25 vAlign=center 
								style="padding-left: 6px">&nbsp</TD>
						    </TR>
			<TR>
				<TD vAlign=top width=730><TABLE border=0 cellSpacing=0
						cellPadding=0 width=730>
						<TBODY>
							
							<tr class="tablehead">
								<td class="tableheadTD1" colspan="10" height="25"
									valign="center"><b>404a-5 Notice Information Tool</b></td>
								<td class="databorder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							</tr>

							<tr class="datacell1">
								<td class="databorder" height="25"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								<td>&nbsp;<b>Last updated</b></td>
								<td height="25" align="left" valign="top"></td>
								<td class="dataheaddivider" height="25"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								<td height="25" align="left" valign="middle"><render:date
										property="noticeInfo404a5Form.lastUpdateDate"
										patternOut="MMMM dd, yyyy" />
								<td class="dataheaddivider" height="25"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								<td height="25" align="left" valign="middle"><b>Last
										updated by</b></td>
								<td class="dataheaddivider" height="25"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								<td colspan="2" height="25" align="left" valign="middle">${noticeInfo404a5Form.lastUpdatedUserId}</td>
								<td class="databorder" height="25"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							</tr>
							
							<TR>
							<TD class="databorder" colSpan="11"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						    </TR>
                            <TR >
							 <TD  colspan="3" height=25 vAlign=center 
								style="padding-left: 6px">&nbsp</TD>
						    </TR>
                           <%-- --------------DIM SECTION STARTS------------------ --%> 
                           
							<TR class=tablehead>
								<TD class="tableheadTD" height=25 vAlign=center colSpan=10
									style="padding-left: 6px"><img
									id="dimSectionExpandCollapseIcon"
									src="/assets/unmanaged/images/minus_icon.gif" width="13"
									height="13" style="cursor: hand; cursor: pointer">
									<b>Designated Investment Manager</b></TD>
								<TD class=databorder><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							</TR>

							<TR class=" dimSection">
								<TD class=databorder><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								<TD vAlign="top" width="" colSpan=9 >
								<TABLE id="dimSectionTable" border="0" cellSpacing="0" cellPadding="0" width="100%" >
										<TBODY>
										        <c:if
												test="${ noticeInfo404a5Form.designatedInvestmentManagerUi.valueEmpty eq 'false'}">
										        <tr class="datacell1 ">
										        <td colspan="5">
												<content:getAttribute beanName="layoutPageBean" attribute="body1" /> 
												</td>
												</tr>
												</c:if>
												<c:if test="${ noticeInfo404a5Form.designatedInvestmentManagerUi.valueEmpty eq 'true'}">
												<tr class="datacell1 ">
										        <td colspan="5">
												      There is no data for this section.
												</td>
												</tr>
												</c:if>
											   <c:set var="count" value="0" scope="page"/>
											   <c:if
												test="${ noticeInfo404a5Form.designatedInvestmentManagerUi.valueEmpty eq 'false'}">
												<c:if
													test="${not empty noticeInfo404a5Form.designatedInvestmentManagerUi.firstName}">
													     <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 dimSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 dimSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>First Name</B></TD>
														<TD width="70%" colspan="4" >${noticeInfo404a5Form.designatedInvestmentManagerUi.firstName}</TD>
													</TR>
												</c:if>
												<c:if
													test="${not empty noticeInfo404a5Form.designatedInvestmentManagerUi.lastName}">
													    <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 dimSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 dimSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>Last Name</B></TD>
														<TD width="70%" colspan="4">${noticeInfo404a5Form.designatedInvestmentManagerUi.lastName}</TD>
													</TR>
												</c:if>
												<c:if
													test="${not empty noticeInfo404a5Form.designatedInvestmentManagerUi.company}">
													   <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 dimSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 dimSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>Company</B></TD>
														<TD width="70%">${noticeInfo404a5Form.designatedInvestmentManagerUi.company}</TD>
													</TR>
												</c:if>
												<c:if
													test="${not empty noticeInfo404a5Form.designatedInvestmentManagerUi.addressLine1}">
													   <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 dimSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 dimSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>Address Line
																1</B></TD>
														<TD width="70%">${noticeInfo404a5Form.designatedInvestmentManagerUi.addressLine1}</TD>
													</TR>
												</c:if>
												<c:if
													test="${not empty noticeInfo404a5Form.designatedInvestmentManagerUi.addressLine2}">
													   <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 dimSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 dimSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>Address Line
																2</B></TD>
														<TD width="70%">${noticeInfo404a5Form.designatedInvestmentManagerUi.addressLine2}</TD>
													</TR>
												</c:if>
												<c:if
													test="${not empty noticeInfo404a5Form.designatedInvestmentManagerUi.city}">
													   <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 dimSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 dimSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>City</B></TD>
														<TD width="70%" colspan="4">${noticeInfo404a5Form.designatedInvestmentManagerUi.city}</TD>
													</TR>
												</c:if>
												<c:if
													test="${not empty noticeInfo404a5Form.designatedInvestmentManagerUi.state}">
													  <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 dimSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 dimSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>State</B></TD>
														<TD width="70%" colspan="4">${noticeInfo404a5Form.designatedInvestmentManagerUi.state}</TD>
													</TR>
												</c:if>
												<c:if
													test="${not empty noticeInfo404a5Form.designatedInvestmentManagerUi.zipCode}">
													  <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 dimSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 dimSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>Zip</B></TD>
														<TD width="70%" colspan="4">${noticeInfo404a5Form.designatedInvestmentManagerUi.zipCode}</TD>
													</TR>
												</c:if>
												<c:if
													test="${not empty noticeInfo404a5Form.designatedInvestmentManagerUi.phone}">
													  <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 dimSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 dimSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>Phone Number</B></TD>
														<TD width="70%" colspan="4">${noticeInfo404a5Form.designatedInvestmentManagerUi.phone}</TD>
													</TR>
												</c:if>
												<c:if
													test="${not empty noticeInfo404a5Form.designatedInvestmentManagerUi.phoneExt}">
													  <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 dimSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 dimSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>Phone Extension Number</B></TD>
														<TD width="70%" colspan="4">${noticeInfo404a5Form.designatedInvestmentManagerUi.phoneExt}</TD>
													</TR>
												</c:if>
												<c:if
													test="${not empty noticeInfo404a5Form.designatedInvestmentManagerUi.emailAddress}">
													  <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 dimSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 dimSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>Email Address</B></TD>
														<TD width="70%" colspan="4">${noticeInfo404a5Form.designatedInvestmentManagerUi.emailAddress}</TD>
													</TR>
												</c:if>
												<c:if
													test="${not empty noticeInfo404a5Form.designatedInvestmentManagerUi.specialNotes}">
													  <c:choose>
													 		<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 dimSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 dimSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>Special Notes</B></TD>
														<TD width="70%" colspan="4">${noticeInfo404a5Form.designatedInvestmentManagerUi.specialNotes}</TD>
												</TR>
												</c:if>
											</c:if>
										</TBODY>
									</TABLE>
								</TD>
								<TD class=databorder><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							</tr>
							<TR>
							<TD class="databorder" colSpan="11"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						    </TR>
                            <TR >
							 <TD  colspan="3" height=25 vAlign=center 
								style="padding-left: 6px">&nbsp</TD>
						    </TR>
							
                            <%-- ----------------- DIM SECTION ENDS -----------------  --%>  
                            
                            <%-- --------------PBA SECTION STARTS------------------ --%> 
                           
							<TR class=tablehead>
								<TD class="tableheadTD" height=25 vAlign=center colSpan=10
									style="padding-left: 6px"><img
									id="pbaSectionExpandCollapseIcon"
									src="/assets/unmanaged/images/minus_icon.gif" width="13"
									height="13" style="cursor: hand; cursor: pointer">
									<b>Personal Brokerage Account</b></TD>
								<TD class=databorder><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							</TR>

							<TR class=" pbaSection">
								<TD class=databorder><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								<TD vAlign="top" width="" colSpan=9 >
								<TABLE id="pbaSectionTable" border="0" cellSpacing="0" cellPadding="0" width="100%" >
										<TBODY>
											<c:if
												test="${not(noticeInfo404a5Form.personalBrokerageAccountUi.valueEmpty eq 'false' && (not empty noticeInfo404a5Form.standardPBAFees or 
												not empty noticeInfo404a5Form.customPBAFees))}">
												<tr class="datacell1 ">
											        <td colspan="5">
													      There is no data for this section.
													</td>
												</tr>
											</c:if>
												
											<c:if
												test="${noticeInfo404a5Form.personalBrokerageAccountUi.valueEmpty eq 'false' && (not empty noticeInfo404a5Form.standardPBAFees or 
												not empty noticeInfo404a5Form.customPBAFees)}">
										        <tr class="datacell1 ">
										        <td colspan="5">
												<content:getAttribute beanName="pbaSectionintroview" attribute="text" /> 
												</td>
												</tr>
											<c:set var="count" value="0" scope="page"/>
										   	<c:if
											test="${noticeInfo404a5Form.personalBrokerageAccountUi.valueEmpty eq 'false' }">
												<c:if
													test="${not empty noticeInfo404a5Form.personalBrokerageAccountUi.pbaProviderName }">
													     <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 pbaSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 pbaSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
												
														<TD vAlign=top width="30%"><B>PBA Provider Name</B></TD>
														<TD width="70%" colspan="4" >${noticeInfo404a5Form.personalBrokerageAccountUi.pbaProviderName}</TD>
												</TR>
												</c:if>
												
												
												<c:if
													test="${not empty noticeInfo404a5Form.personalBrokerageAccountUi.pbaPhone}">
													  <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 pbaSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 pbaSectionCount">
															</c:otherwise>
														</c:choose> 
												 
												 <c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%"><B>PBA Phone Number</B></TD>
														<TD width="70%" colspan="4">${noticeInfo404a5Form.personalBrokerageAccountUi.pbaPhone}</TD>
													</TR>
													</c:if>
													
													<c:if
													test="${not empty noticeInfo404a5Form.personalBrokerageAccountUi.pbaPhoneExt }">
													  <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 pbaSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 pbaSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
												
														<TD vAlign=top width="30%"><B>PBA ext.</B></TD>
														<TD width="70%" colspan="4">${noticeInfo404a5Form.personalBrokerageAccountUi.pbaPhoneExt}</TD>
													</TR>
													</c:if>
													
													<c:if
													test="${not empty noticeInfo404a5Form.personalBrokerageAccountUi.pbaEmailAddress }">
													  <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 pbaSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 pbaSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
												
														<TD vAlign=top width="30%"><B>PBA Email Address</B></TD>
														<TD width="70%" colspan="4">${noticeInfo404a5Form.personalBrokerageAccountUi.pbaEmailAddress}</TD>
													</TR>
													</c:if>
													<c:if
													test="${not empty noticeInfo404a5Form.personalBrokerageAccountUi.pbaMinDeposit  && noticeInfo404a5Form.personalBrokerageAccountUi.pbaMinDeposit ne '0.00'
															&& noticeInfo404a5Form.personalBrokerageAccountUi.pbaMinDeposit ne 'null' }">
													  <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 pbaSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 pbaSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
												
														<TD vAlign=top width="40%"><B><content:getAttribute beanName="pbaMindepositview" attribute="text" /></B></TD>
														<td colspan="4" align="left" width="70%">${noticeInfo404a5Form.personalBrokerageAccountUi.amountValueFormatted}</td>
												</TR>
												</c:if>
												 <c:if
												test="${ noticeInfo404a5Form.personalBrokerageAccountUi.pbaRestriction eq 'Y'}">
														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 pbaSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 pbaSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=bottom width="30%"><B><content:getAttribute beanName="pbaRestrictionLabel" attribute="text" /></B></TD>
														<td width="30%">
<input type="radio" disabled="true" name="personalBrokerageAccountUi.pbaRestriction" value="Y" checked="checked">Yes
<input type="radio" disabled="true" name="personalBrokerageAccountUi.pbaRestriction" value="N" >No
														</td>
														<TD width="30%" colspan="2">&nbsp;</TD>
														
												</TR>
												<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 pbaSectionCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 pbaSectionCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="70%" colspan="4"><B><content:getAttribute beanName="pbaRestrictionMsg" attribute="text" /></B></TD>
														
													
												</TR>
												 <c:if
												test="${ not empty noticeInfo404a5Form.pbaRestrictionList}">
												<c:forEach var="pbarestriction"
													items="${noticeInfo404a5Form.pbaRestrictionList}">
													 <c:if test="${ pbarestriction.deletedInd eq false }">
													 <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 pbaFeesCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 pbaFeesCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<td colspan="4" ><div  class="some-area" >
														${pbarestriction.restrictionDesc}</div></td>
													</tr>
													</c:if>
												</c:forEach>
											</c:if>
										   
										  </c:if>
										  </c:if>
										  
										  
												<!-- PBA Fee Table  -->
												<TR class="pbaSection">
													<TD colSpan=9><TABLE id="pbaSectionTable" border=0
															style="table-layout: fixed; word-wrap: break-word;"
															width="100%" cellSpacing="0" cellPadding="0">
															<TBODY>
																<TR style="background: #cccccc">
																	<%-- Fee Type --%>
																	<TD width="20"><IMG
																		src="/assets/unmanaged/images/s.gif" width="1"
																		height="1"></TD>
																	<TD width="100"><IMG
																		src="/assets/unmanaged/images/s.gif" width="1"
																		height="1"></TD>
																	<%-- Data divider--%>
																	<TD width="1"><IMG
																		src="/assets/unmanaged/images/s.gif" width="1"
																		height="1"></TD>
																	<%-- Value drop-down --%>
																	<TD width="90" class="type"><IMG
																		src="/assets/unmanaged/images/s.gif" width="1"
																		height="1"></TD>
																	<%-- Value text box --%>
																	<TD width="90" class=type><IMG
																		src="/assets/unmanaged/images/s.gif" width="1"
																		height="1"></TD>
																</TR>
																<TR class="tablesubhead" height="24">
																	<TD colspan="2"><b>Fee type</b></TD>
																	<TD class="dataheaddivider"><IMG
																		src="/assets/unmanaged/images/s.gif" width="1"
																		height="1"></TD>
																	<TD valign="middle" align="left" colspan="2"><b>Value</b></TD>
																</TR>
																 
																						
																<!-- Displaying Standard fee  -->
																<c:if
												test="${ not empty noticeInfo404a5Form.standardPBAFees}">
														<c:forEach var="fee"
														items="${noticeInfo404a5Form.standardPBAFees}">
														<c:if test="${fee.showFee eq true }">
														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 pbaFeesCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 pbaFeesCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<td colspan="2" valign="top" width="25%"><b>${fee.feeDescription}</b></td>
															<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td colspan="2" align="left" width="22%">${fee.amountValueFormatted}</td>
														</tr>
														</c:if>
													</c:forEach>
													</c:if>
													
													<!-- Displaying Custom fee  -->
													<c:if
												test="${ not empty noticeInfo404a5Form.customPBAFees}">
													<c:forEach var="fee"
													items="${noticeInfo404a5Form.customPBAFees}">
													<c:if test="${ fee.showFee eq true }">
													 <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 pbaFeesCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 pbaFeesCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<td colspan="2" valign="top" width="25%"><b>${fee.feeDescription}</b></td>
														<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td colspan="2" align="left" width="22%">${fee.amountValueFormatted}</td>
														
													</tr>
													</c:if>
												</c:forEach>
												</c:if>
															</TBODY>
														</TABLE></TD>
													<TD class=databorder><IMG
														src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												</TR>
											</c:if>
										</TBODY>
									</TABLE>
								</TD>
								<TD class=databorder><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							</tr>
							<TR>
							<TD class="databorder" colSpan="11"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						    </TR>
                            <TR >
							 <TD  colspan="3" height=25 vAlign=center 
								style="padding-left: 6px">&nbsp</TD>
						    </TR>
							
                            <%-- ----------------- PBA SECTION ENDS -----------------  --%>  
                            
                            <%-- ----------------- NON TPA EXPENSE SECTION STARTS ---- --%>  
                            
							<TR class=tablehead>
								<TD class=tableheadTD height=25 vAlign=center colSpan=10
									style="padding-left: 6px"><img
									id="nonTpaFeesExpandCollapseIcon"
									src="/assets/unmanaged/images/minus_icon.gif" width="13"
									height="13" style="cursor: hand; cursor: pointer"> <b>Individual Plan Expenses</b></TD>
								<TD class=databorder><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							</TR>
		
		                    <c:if test="${ not empty noticeInfo404a5Form.nonTpaFees}">
							<tr class="datacell1 nonTpaFees">
								<td class="databorder" height="25"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								<td colspan="9" height="25" valign="center"><content:getAttribute
										beanName="layoutPageBean" attribute="body2" />
								</td>
								<td class="databorder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							</tr>
							</c:if>
							
							
							<TR class="nonTpaFees">
								<TD class=databorder><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								<TD colSpan=9><TABLE id="nonTpaFeesTable" border=0 style="table-layout: fixed; word-wrap:break-word;"
										width="100%"  cellSpacing="0" cellPadding="0" >
										<TBODY>
										<TR style="background: #cccccc">
											<%-- Fee Type --%>
											<TD width="20"><IMG src="/assets/unmanaged/images/s.gif"
												width="1" height="1"></TD>
											<TD width="186"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<%-- Data divider--%>
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
											<TD colspan="2"><b>Fee
													type</b></TD>
											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<TD valign="middle" align="right" colspan="2"><b>Value</b></TD>
											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<TD vAlign="middle" align="left"><b>Special notes</b></TD>
										</TR>
											<c:if test="${ empty noticeInfo404a5Form.nonTpaFees}">
											           <tr class="datacell1 ">
													   <td valign="top" colspan="7" width="25%">There is no data for this section.</td>
													   </tr>
											</c:if>
											<c:if test="${ not empty noticeInfo404a5Form.nonTpaFees}">
											    <c:set var="count" value="0" scope="page"/>
												<c:forEach var="fee"
													items="${noticeInfo404a5Form.nonTpaFees}">
													<c:if test="${ fee.showFee eq true }">
													   <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 nonTpaFeesCount "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 nonTpaFeesCount ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<td valign="top" colspan="2" width="25%"><b>${fee.feeDescription}</b></td>
														<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td align="right" colspan="2" width="22%">${fee.amountValueFormatted}</td>
														<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td width="52%">${fee.notes}</td>
													</tr>
													</c:if>
												</c:forEach>
											</c:if>
										</TBODY>
									</TABLE></TD>
								<TD class=databorder><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							</TR>
							<TR>
							<TD class="databorder" colSpan="11"><IMG
								src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
						    </TR>
                            <TR >
							 <TD  colspan="3" height=25 vAlign=center 
								style="padding-left: 6px">&nbsp</TD>
						    </TR>
                            <%-- ----------------- NON TPA EXPENSE SECTION ENDS  ------------------%>  
                            
                            <%-- ----------------- TPA EXPENSE SECTION STARTS ------------------ --%>  
                            
							<TR class=tablehead>

								<TD class=tableheadTD height=25 vAlign=center colSpan=6
									style="padding-left: 6px"><img
									id="tpaFeesExpandCollapseIcon"
									src="/assets/unmanaged/images/minus_icon.gif" width="13"
									height="13" style="cursor: hand; cursor: pointer"> <b>Individual TPA Expenses</b></TD>
								<TD class=tableheadTD height=25 vAlign=center colSpan=4>
								        <c:if test="${ noticeInfo404a5Form.tpaRestricted  eq 'true' }">
<form:checkbox path="noticeInfo404a5Form.tpaRestricted" disabled="true" />


											  TPA access restricted
										 </c:if >
										</TD>
								<TD class=databorder><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							</TR>

                            <c:if test="${ noticeInfo404a5Form.hasTpaFees eq true }">
							<tr class="datacell1 tpaFees">
								<td class="databorder" height="25"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>

								<td colspan="9" height="25" valign="center"><content:getAttribute
										beanName="layoutPageBean" attribute="body3" />

								</td>
								<td class="databorder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							</tr>
							</c:if>
							
							<ps:isInternalOrTpa name="userProfile" property="role">
								<tr class="datacell1 tpaFees">
									<td class="databorder" height="25"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td colspan="9" height="25" valign="center"><c:if
											test="${noticeInfo404a5Form.tpaFeeType eq 'CUSTOMIZED'}">
						               Customized schedule in use.
						         </c:if> <c:if
											test="${noticeInfo404a5Form.tpaFeeType eq 'STANDARD'}">
						               Standard schedule in use.
						         </c:if> <c:if test="${noticeInfo404a5Form.tpaFeeType eq 'NONE'}">
						               No schedule exists.
						         </c:if></td>
									<td class="databorder"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								</tr>
							</ps:isInternalOrTpa>

							<tr class=" tpaFees">
								<td class="databorder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								<td colspan="9"><table id="tpaFeesTable" border="0" style="table-layout: fixed; word-wrap:break-word;"
										width="100%" cellSpacing="0" cellPadding="0" >
										<tbody>
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
											<TD colspan="2"><b>Fee
													type</b></TD>
											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<TD valign="middle" align="right" colspan="2"><b>Value</b></TD>
											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<TD vAlign="middle" align="left"><b>Special notes</b></TD>
										</TR>
										<c:if
												test="${ noticeInfo404a5Form.hasTpaFees eq false }">
											 <tr class="datacell1 ">
												<td valign="top" colspan="7" width="25%">There is no data for this section.</td>
											 </tr>
								       </c:if>
								           <c:set var="count" value="0" scope="page"/>
											<c:if
												test="${ not empty noticeInfo404a5Form.tpaFeesStandard}">
												
												 <%-- -----------------DISPLAY FEES FOR INTERNAL OR TPA USERS  ------------------%>
												<ps:isInternalOrTpa name="userProfile" property="role">
													<c:forEach var="fee"
														items="${noticeInfo404a5Form.tpaFeesStandard}">
														<c:if test="${ fee.showFee eq true }">
														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 tpaFeesCount ${fee.disabled}"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 tpaFeesCount ${fee.disabled}">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<td colspan="2" valign="top" width="25%"><b>${fee.feeDescription}</b></td>
															<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td colspan="2" align="right" width="22%">${fee.amountValueFormatted}</td>
															<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td  width="52%">${fee.notes}</td>
														</tr>
														</c:if>
													</c:forEach>
												</ps:isInternalOrTpa>
											
											 <%-- -----------------DISPLAY FEES FOR NOT INTERNAL OR TPA USERS  ------------------%>
												<ps:isNotInternalOrTpa name="userProfile" property="role">
													<c:forEach var="fee"
														items="${noticeInfo404a5Form.tpaFeesStandard}">
														<c:if test="${ fee.disabled eq false && fee.showFee eq true }">
														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 tpaFeesCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 tpaFeesCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<td colspan="2" valign="top" width="25%"><b>${fee.feeDescription}</b></td>
															<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td colspan="2" align="right" width="22%">${fee.amountValueFormatted}</td>
															<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td  width="52%">${fee.notes}</td>
														</tr>
														</c:if>
													</c:forEach>
												</ps:isNotInternalOrTpa>
											</c:if>
											<c:if
												test="${ not empty noticeInfo404a5Form.tpaFeesCustomized}">
												<c:forEach var="fee"
													items="${noticeInfo404a5Form.tpaFeesCustomized}">
													<c:if test="${ fee.showFee eq true }">
													 <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 tpaFeesCount"> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 tpaFeesCount">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<td colspan="2" valign="top" width="25%"><b>${fee.feeDescription}</b></td>
														<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td colspan="2" align="right" width="22%">${fee.amountValueFormatted}</td>
														<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td width="52%">${fee.notes}</td>
													</tr>
													</c:if>
												</c:forEach>
											</c:if>
										</tbody>
									</table></td>
								<td class="databorder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							</tr>
							
							<%-- ----------------- TPA EXPENSE SECTION ENDS ------------------ --%>  
							
							<TR>
								<TD class=databorder colSpan=11><IMG
									src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
							</TR>
						</TBODY>
					</TABLE></TD>
				<TD width=10><IMG border=0
					src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
				<TD vAlign=top width=190 align=middle><IMG
					src="/assets/unmanaged/images/s.gif" width=1 height=25> <IMG
					src="/assets/unmanaged/images/s.gif" width=1 height=5> <IMG
					src="/assets/unmanaged/images/s.gif" width=1 height=5></TD>
			</TR>
			<TR>
				<TD></TD>
				<TD width=10><IMG border=0
					src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
				<TD>&nbsp;</TD>
			</TR>
			<%-- ----------------- EDIT BUTTON START ------------------ --%>  
			
					<ps:form cssStyle="margin-bottom:0;" action="/do/view404a5NoticeInfo/" name="noticeInfo404a5Form" modelAttribute="noticeInfo404a5Form">
<input type="hidden" name="action" /><%-- input - name="noticeInfo404a5Form" --%>
						<TABLE border=0 cellSpacing=0 cellPadding=0 width=730>
							<tr>
								<td width="700" align="right"></td>
								<td width="230" align="right">
								<c:if test="${empty param.printFriendly }" >
				                <ps:isInternalUser name="userProfile" property="role">
								<ps:permissionAccess permissions="IPIH">
<input type="button" onclick="doEdit()" name="button" class="button134" value="edit"/>


							    </ps:permissionAccess>
								</ps:isInternalUser>
		                        </c:if>	
								</td>
							</tr>
						</TABLE>
					</ps:form>
				
			<%-- ----------------- EDIT BUTTON ENDS ------------------ --%>  
			<BR>
			<c:if test="${not empty param.printFriendly}">
		    <content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure"/>
		    <table  border=0 cellSpacing=0 cellPadding=0 width=730>
		    <tr>
		    <td width="100%"><p><content:pageFooter beanName="layoutPageBean" /></p></td>
		    </tr>
		    <tr>
		    <td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		    </tr>
		    </table>
		    </c:if>
	<TR>
		<TD><IMG border=0 src="/assets/unmanaged/images/s.gif" width=30
				height=1></TD>
		<TD></TD>
		<TD width=10><IMG border=0
				src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
		<TD>&nbsp;</TD>
	</TR>
	</TABLE>
</TABLE>	
<c:if test="${empty param.printFriendly}">
<tr>
	<td>
	    <br/>
		<table cellpadding="0" cellspacing="0" border="0" width="765" class="fixedTable" height="">
			<tr>
				<td width="30">&nbsp;</td>
				<td width="735"><content:pageFooter beanName="layoutPageBean" /></td>
			</tr>
		</table>
	</td>
</tr>
</c:if>

<script language="JavaScript" type="text/javascript"
				src="/assets/unmanaged/javascript/tooltip.js"></script>
