<%@page import="com.manulife.pension.ps.web.fee.DesignatedInvestmentManagerUi"%>
<%@page import="com.manulife.pension.ps.web.fee.NoticeInfo404a5Form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
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
NoticeInfo404a5Form noticeInfo404a5Forms = (NoticeInfo404a5Form)session.getAttribute("noticeInfo404a5Form");
pageContext.setAttribute("noticeInfo404a5Forms",noticeInfo404a5Forms,PageContext.PAGE_SCOPE);
String dirty=noticeInfo404a5Forms.getDirty();
pageContext.setAttribute("dirty",dirty,PageContext.PAGE_SCOPE);
String technicalDifficulties =(String)request.getAttribute(Constants.TECHNICAL_DIFFICULTIES);
pageContext.setAttribute("technicalDifficulties",technicalDifficulties,PageContext.PAGE_SCOPE);
%>
<content:contentBean
	contentId="<%=ContentConstants.WARNNING_MESSAGE_ON_CANCEL_DATA_LOSS_TO_TPA_USER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="warningMessage" />
	
<content:contentBean
	contentId="<%=ContentConstants.WARNNING_MESSAGE_ON_CANCEL_DATA_LOSS_TO_TPA_USER%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="warningMessage" />	

<content:contentBean
	contentId="<%=ContentConstants.DIM_SECTION_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="introDim" />	

<content:contentBean
	contentId="<%=ContentConstants.NON_TPA_SECTION_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="introNonTpaFee" />	
	
<content:contentBean
	contentId="<%=ContentConstants.TPA_SECTION_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="introTpaFee" />	
	
<content:contentBean
	contentId="<%=ContentConstants.PBA_SECTION_INTRO%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="introPba" />
	
<content:contentBean
	contentId="<%=ContentConstants.PBA_MIN_DEPOSIT_VIEW%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="pbaMindepositview" />
	
<content:contentBean
	contentId="<%=ContentConstants.PBA_RESTRICTION_LABEL%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="pbaRestrictionLabel" />
	
<content:contentBean
	contentId="<%=ContentConstants.PBA_RESTRICTION_INSTRUCTIONS%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="pbaRestrictionInstruction" />
	
<script>
$(document).ready(function(){
    $("div.some-area").text(function(index, currentText) {
    	var trimTest= currentText.trim();
	    return trimTest.substr(0, 150);
 	});
});
</script>

  <c:if test="${empty technicalDifficulties}">
	<c:if test="${empty param.printFriendly}" >

		<script language="JavaScript1.2" type="text/javascript">
			var sections = [ "dimSection","pbaSection", "nonTpaFees", "tpaFees" ];

			
			function isLostChanges(){
				
				if(isFormDirty()){
					return confirm('<content:getAttribute beanName="warningMessage" attribute="text"/>');
				}
				return true;
			}
			
			$(document)
					.ready(
							function() {
								for ( var i = 0; i < sections.length; i++) {
									setUpExpandCollapseIcon(sections[i]);

									var rowIndex = $("#" + sections[i]
											+ "Table  tr").length;
									if (rowIndex == 0) {
										contractSection(sections[i]);
									}
								}
								
								registerTrackChangesFunction(isFormDirty);
								var hrefs  = document.links;
								if (hrefs != null)
								{
									for (i=0; i<hrefs.length; i++) { 
										if(hrefs[i].href != undefined && 
											hrefs[i].href.indexOf("/password/changePasswordInternal") != -1){
											hrefs[i].onclick = new Function ("return isLostChanges();");
										}
									
									}
								}
								var myVar = '<%= (String)pageContext.getAttribute("dirty") %>';
								
								if(myVar === "true") {
									
								setDirtyFlag();
								}
								
								$("a:not(.protectedLinkId):not(.signOutLayerStyle2)")
								.on("click",
										function(event) {
											return isLostChanges();
										});
							});

			function isFormDirty() {
				return $("#dirtyFlagId").val() == 'true';
			}
			function setDirtyFlag() {
				$("#dirtyFlagId").val("true");
			}

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

			function doBack() {
				document.noticeInfo404a5Form.action.value = "back";
				document.noticeInfo404a5Form.submit();
			}

			function doSubmit() {
				document.noticeInfo404a5Form.action.value = "confirm";
				document.noticeInfo404a5Form.submit();
			}
		</script>
	</c:if>
</c:if>
<%-- technical difficulties --%>
<br />
<td>
<div id="messagesBox" class="messagesBox">
	<%-- Override max height if print friendly is on so we don't scroll --%>
	<ps:messages scope="session" suppressDuplicateMessages="true" />
</div>
<ps:form action="/do/confirm404a5NoticeInfo/" name="noticeInfo404a5Form" modelAttribute="noticeInfo404a5Form">
	<TABLE width="730" border="0" cellspacing="0" cellpadding="0"
		padding="0">
		<TBODY>
			<tr>
			</tr>

			<TR>
				<TD>&nbsp;</TD>
				<TD width=10><IMG border=0
					src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
				<TD>&nbsp;</TD>
			</TR>
			<TR>
                 	<td colspan="3" valign="middle"><a href="#" class="protectedLinkId"
								onclick="expandAllSections(); return false;"><img
									src="/assets/unmanaged/images/plus_icon_all.gif" class="icon"  border="0"></a>
								/ <a href="#" onclick="contractAllSections(); return false;" class="protectedLinkId" ><img
									src="/assets/unmanaged/images/minus_icon_all.gif" class="icon"  border="0"></a>
								<b>All sections</b></td>
			</TR>
			<TR>
			 <TD  colspan="3" height=25 vAlign=center 
									style="padding-left: 6px">&nbsp</TD>
			</TR>
			<TR>
				<TD vAlign=top width=700><TABLE border=0 cellSpacing=0
						cellPadding=0 width=700>
						<TBODY>
							<TR>
								<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
									height=1></TD>
								<TD class=actions><IMG src="/assets/unmanaged/images/s.gif"
									width=148 height=1></TD>
								<TD class=submissionDate><IMG
									src="/assets/unmanaged/images/s.gif" width=35 height=1></TD>
								<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
									height=1></TD>
								<TD class=type><IMG src="/assets/unmanaged/images/s.gif"
									width=165 height=1></TD>
								<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
									height=1></TD>
								<TD class=paymentTotal><IMG
									src="/assets/unmanaged/images/s.gif" width=200 height=1></TD>
								<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
									height=1></TD>
								<TD><IMG src="/assets/unmanaged/images/s.gif" width=180
									height=1></TD>
								<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
									height=1></TD>
								<TD><IMG src="/assets/unmanaged/images/s.gif" width=1
									height=1></TD>
							</TR>

							<tr class="tablehead">
								<td class="tableheadTD1" colspan="10" height="25"
									valign="center"><b>404a-5 Notice Information Tool</b></td>
								<td class="databorder"><img
									src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							</tr>
							<c:if test="${ not empty noticeInfo404a5Form.designatedInvestmentManagerUi.changedItems}">

								<TR class=tablehead>
									<TD class="tableheadTD" height=25 vAlign=center colSpan=10
										style="padding-left: 6px"><img
										id="dimSectionExpandCollapseIcon"
										src="/assets/unmanaged/images/minus_icon.gif" width="13"
										height="13" style="cursor: hand; cursor: pointer">&nbsp;
										<b>Designated Investment Manager</b></TD>
									<TD class=databorder><IMG
										src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								</TR>

								<TR class="dimSection">
									<TD class=databorder><IMG
										src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
									<TD colSpan=9>

										<TABLE id="dimSectionTable" border="0" cellSpacing="0" cellPadding="0" >
											<TBODY>

													<TR class=datacell1>
														<TD width="100%" colspan="5">
														<content:getAttribute beanName="introDim" attribute="text" />
														</TD>
													</TR>
	<c:set var="count" value="0" scope="page"/>
<c:if test="${noticeInfo404a5Form.designatedInvestmentManagerUi.getChangedItems().contains('firstName') == true}">


														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<TD vAlign=top width="30%">&nbsp;<B>First Name</B></TD>
															<TD width="70%">${noticeInfo404a5Form.designatedInvestmentManagerUi.firstName}</TD>
														</TR>
</c:if>
													
<c:if test="${noticeInfo404a5Form.designatedInvestmentManagerUi.getChangedItems().contains('lastName') ==true}">


														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<TD vAlign=top width="30%">&nbsp;<B>Last Name</B></TD>
															<TD width="70%">${noticeInfo404a5Form.designatedInvestmentManagerUi.lastName}</TD>
														</TR>
</c:if>
													
<c:if test="${noticeInfo404a5Form.designatedInvestmentManagerUi.getChangedItems().contains('company') == true}">

														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<TD vAlign=top width="30%">&nbsp;<B>Company</B></TD>
															<TD width="70%">${noticeInfo404a5Form.designatedInvestmentManagerUi.company}</TD>
														</TR>
</c:if>
													
<c:if test="${noticeInfo404a5Form.designatedInvestmentManagerUi.getChangedItems().contains('addressLine1') ==true}">


														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<TD vAlign=top width="30%">&nbsp;<B>Address Line
																	1</B></TD>
															<TD width="70%">${noticeInfo404a5Form.designatedInvestmentManagerUi.addressLine1}</TD>
														</TR>
</c:if>
													
<c:if test="${noticeInfo404a5Form.designatedInvestmentManagerUi.getChangedItems().contains('addressLine2') ==true}">


														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<TD vAlign=top width="30%">&nbsp;<B>Address Line
																	2</B></TD>
															<TD width="70%">${noticeInfo404a5Form.designatedInvestmentManagerUi.addressLine2}</TD>
														</TR>
</c:if>
													
<c:if test="${noticeInfo404a5Form.designatedInvestmentManagerUi.getChangedItems().contains('city') ==true}">


														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<TD vAlign=top width="30%">&nbsp;<B>City</B></TD>
															<TD width="70%">${noticeInfo404a5Form.designatedInvestmentManagerUi.city}</TD>
														</TR>
</c:if>
													
<c:if test="${noticeInfo404a5Form.designatedInvestmentManagerUi.getChangedItems().contains('state') ==true}">


														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<TD vAlign=top width="30%">&nbsp;<B>State</B></TD>
															<TD width="70%">${noticeInfo404a5Form.designatedInvestmentManagerUi.state}</TD>
														</TR>
</c:if>
													
<c:if test="${noticeInfo404a5Form.designatedInvestmentManagerUi.getChangedItems().contains('zipCode') ==true}">


														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<TD vAlign=top width="30%">&nbsp;<B>Zip</B></TD>
															<TD width="70%">${noticeInfo404a5Form.designatedInvestmentManagerUi.zipCode}</TD>
														</TR>
</c:if>
													
<c:if test="${noticeInfo404a5Form.designatedInvestmentManagerUi.getChangedItems().contains('phone') ==true}">


														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<TD vAlign=top width="30%">&nbsp;<B>Phone Number</B></TD>
															<TD width="70%">${noticeInfo404a5Form.designatedInvestmentManagerUi.phone}</TD>
														</TR>
</c:if>
													
													
<c:if test="${noticeInfo404a5Form.designatedInvestmentManagerUi.getChangedItems().contains('phoneExt') ==true}">


														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>Phone Extension Number</B></TD>
														<TD width="70%">${noticeInfo404a5Form.designatedInvestmentManagerUi.phoneExt}</TD>
													</TR>
</c:if>
													
<c:if test="${noticeInfo404a5Form.designatedInvestmentManagerUi.getChangedItems().contains('emailAddress') ==true}">


														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<TD vAlign=top width="30%">&nbsp;<B>Email
																	Address</B></TD>
															<TD width="70%">${noticeInfo404a5Form.designatedInvestmentManagerUi.emailAddress}</TD>
														</TR>
</c:if>
													
<c:if test="${noticeInfo404a5Form.designatedInvestmentManagerUi.getChangedItems().contains('specialNotes') ==true}">


														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<TD vAlign=top width="30%">&nbsp;<B>Special
																	Notes</B></TD>
															<TD width="70%">${noticeInfo404a5Form.designatedInvestmentManagerUi.specialNotes}</TD>
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
							</c:if>
							
							
							<%-- --------------PBA SECTION STARTS------------------ --%> 
						
                           <c:if
								test="${ not empty noticeInfo404a5Form.personalBrokerageAccountUi.changedItems or not empty noticeInfo404a5Form.updatedStandardPBAFees
											or not  empty noticeInfo404a5Form.updatedCustomPBAFees or not empty noticeInfo404a5Form.updatedPbaRestrictionList}">
							<TR class=tablehead>
								<td class="databorder" height="25"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
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
										        <c:if test="${ empty noticeInfo404a5Form.updatedPbaRestrictionList }">
										        <tr class="datacell1 ">
										        <td colspan="5">
												<content:getAttribute beanName="introPba" attribute="text" /> 
												</td>
												</tr>
												</c:if>
											   <c:set var="count" value="0" scope="page"/>
<c:if test="${noticeInfo404a5Form.personalBrokerageAccountUi.getChangedItems().contains('pbaProviderName') ==true}">


														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>PBA Provider Name</B></TD>
														<TD width="70%" colspan="4" >${noticeInfo404a5Form.personalBrokerageAccountUi.pbaProviderName}</TD>
</c:if>
												
<c:if test="${noticeInfo404a5Form.personalBrokerageAccountUi.getChangedItems().contains('pbaPhone') ==true}">


														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>PBA Phone Number</B></TD>
														<TD width="70%" colspan="4">${noticeInfo404a5Form.personalBrokerageAccountUi.pbaPhone}</TD>
</c:if>
													
<c:if test="${noticeInfo404a5Form.personalBrokerageAccountUi.getChangedItems().contains('pbaPhoneExt') ==true}">


														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>PBA ext.</B></TD>
														<TD width="70%" colspan="4">${noticeInfo404a5Form.personalBrokerageAccountUi.pbaPhoneExt}</TD>
</c:if>
													
<c:if test="${noticeInfo404a5Form.personalBrokerageAccountUi.getChangedItems().contains('pbaEmailAddress') ==true}">


														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="30%">&nbsp;<B>PBA Email Address</B></TD>
														<TD width="70%" colspan="4">${noticeInfo404a5Form.personalBrokerageAccountUi.pbaEmailAddress}</TD>
</c:if>
													
<c:if test="${noticeInfo404a5Form.personalBrokerageAccountUi.getChangedItems().contains('pbaMinDeposit') ==true}">


														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<TD vAlign=top width="40%">&nbsp;<B><content:getAttribute beanName="pbaMindepositview" attribute="text" /></B></TD>
														<TD width="70%" colspan="4">${noticeInfo404a5Form.personalBrokerageAccountUi.amountValueFormatted}</TD>
</c:if>
												
												<%--PBA restriction: START--%>
												<c:if test="${not empty noticeInfo404a5Form.updatedPbaRestrictionList }">
												
														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<TD vAlign=top width="30%">&nbsp;<B><content:getAttribute beanName="pbaRestrictionLabel" attribute="text" /></B></TD>
														<td width="30%">
<form:radiobutton disabled="true" path="personalBrokerageAccountUi.pbaRestriction" value="Y" />Yes
<form:radiobutton disabled="true" path="personalBrokerageAccountUi.pbaRestriction" value="N" />No
														</td>
														<TD width="50%" colspan="3">&nbsp;</TD>
												</TR>
												</c:if>
												<c:if test="${ empty noticeInfo404a5Form.updatedPbaRestrictionList }">
												<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
												</c:choose>
												<TD vAlign=top width="70%" colspan="4">&nbsp;<B><content:getAttribute beanName="pbaRestrictionInstruction" attribute="text" /></B></TD>
												</TR>
												</c:if>												
												<%-- Restriction table --%>
												<c:if test="${ not empty noticeInfo404a5Form.updatedPbaRestrictionList}">
												<TR class="pbaSection">
													<TD colSpan=9><TABLE id="pbaResTable" style="border-collapse:collapse; table-layout: fixed; word-wrap: break-word;" width="100%" cellspacing="0" cellpadding="0" border="0">
															<TBODY>
																<TR style="background: #cccccc">
																	<%-- Restriction --%>
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
																<TR class="datacell2">
																	<td colspan="2" valign="top" width="25%"><b>Restriction Added</b></td>
																	<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
																	<td colspan="2" align="left" width="22%">
																	<table width="100%"  width="100%" cellspacing="0" cellpadding="0" border="0">
																	<c:if test="${ not empty noticeInfo404a5Form.addedPbaRestrictionList}">
																		<c:forEach var="addedRes" items="${noticeInfo404a5Form.addedPbaRestrictionList}">
																		<c:choose>
																				<c:when test="${count % 2 == 0}">
																					<tr class="datacell2 "> 
																				</c:when>
																		<c:otherwise>
																				<tr class="datacell1 ">
																		</c:otherwise>
																		</c:choose> 
																			<c:set var="count" value="${count + 1}" scope="page"/>
																			<td> <div  class="some-area"> ${addedRes.restrictionDesc} </div></td>
																		</tr>
																		</c:forEach>
																	</c:if>	
																	</table>
																	</td>
																</TR>
																<tr class="datacell1 ">
																	<td colspan="2" valign="top" width="25%"><b>Restriction Modified</b></td>
																	<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
																	<td colspan="2" align="left" width="22%">
																	<table width="100%"  width="100%" cellspacing="0" cellpadding="0" border="0">
																	<c:if test="${ not empty noticeInfo404a5Form.modifiedPbaRestrictionList}">
																		<c:forEach var="modifiedRes" items="${noticeInfo404a5Form.modifiedPbaRestrictionList}">
																		<c:choose>
																				<c:when test="${count % 2 == 0}">
																					<tr class="datacell2 "> 
																				</c:when>
																		<c:otherwise>
																				<tr class="datacell1 ">
																		</c:otherwise>
																		</c:choose> 
																			<c:set var="count" value="${count + 1}" scope="page"/>
																			<td><div  class="some-area">${modifiedRes.restrictionDesc}</div></td>
																		</tr>
																		</c:forEach>
																	</c:if>	
																	</table>
																	</td>
																</tr>
																<tr class="datacell2">
																	<td colspan="2" valign="top" width="25%"><b>Restriction Deleted</b></td>
																	<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
																	<td colspan="2" align="left" width="22%">
																	<table width="100%"  width="100%" cellspacing="0" cellpadding="0" border="0">
																	<c:if test="${ not empty noticeInfo404a5Form.deletedPbaRestrictionList}">
																		<c:forEach var="deletedRes" items="${noticeInfo404a5Form.deletedPbaRestrictionList}">
																		<c:choose>
																				<c:when test="${count % 2 == 0}">
																					<tr class="datacell2 "> 
																				</c:when>
																		<c:otherwise>
																				<tr class="datacell1 ">
																		</c:otherwise>
																		</c:choose> 
																			<c:set var="count" value="${count + 1}" scope="page"/>
																			<td><div  class="some-area">${deletedRes.restrictionDesc}</div></td>
																		</tr>
																		</c:forEach>
																	</c:if>	
																	</table>
																	</td>
																</tr>
																
															</TBODY>
														</TABLE></TD>
													<TD class=databorder><IMG
														src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												</TR>
												</c:if>
											
												<%-- end --%>
												
												<!-- PBA Fee Table  -->
												<c:if
								test="${ not empty noticeInfo404a5Form.updatedStandardPBAFees
											or not  empty noticeInfo404a5Form.updatedCustomPBAFees}">
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
																<c:set var="count" value="0" scope="page"/>
																<c:if
													test="${ not empty noticeInfo404a5Form.updatedStandardPBAFees}">
																<c:forEach var="fee"
														items="${noticeInfo404a5Form.updatedStandardPBAFees}">
														<c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<td colspan="2" valign="top" width="25%"><b>${fee.feeDescription}</b></td>
															<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td colspan="2" align="left" width="22%">${fee.amountValueFormatted}</td>
														</tr>
													</c:forEach>
													</c:if>
													
													<!-- Displaying Custom fee  -->
													
													<c:if
													test="${ not empty noticeInfo404a5Form.updatedCustomPBAFees}">
													<c:forEach var="fee"
													items="${noticeInfo404a5Form.updatedCustomPBAFees}">
													 <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<td colspan="2" valign="top" width="25%"><b>${fee.feeDescription}</b></td>
														<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td colspan="2" align="left" width="22%">${fee.amountValueFormatted}</td>
														
													</tr>
												</c:forEach>
												</c:if>
															</TBODY>
														</TABLE></TD>													
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
                            
						    </c:if>
							
                            <%-- ----------------- PBA SECTION ENDS -----------------  --%> 

							<c:if test="${ not empty noticeInfo404a5Form.updatedNonTpaFees}">
	                            <TR >
								 <TD  colspan="3" height=25 vAlign=center 
									style="padding-left: 6px">&nbsp</TD>
							    </TR>
								<TR class=tablehead>
									<TD class=tableheadTD1 height=25 vAlign=center colSpan=10
										style="padding-left: 6px"><img
										id="nonTpaFeesExpandCollapseIcon"
										src="/assets/unmanaged/images/minus_icon.gif" width="13"
										height="13" style="cursor: hand; cursor: pointer"> <b>Individual Plan Expenses</b></TD>
									<TD class=databorder><IMG
										src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								</TR>

								<tr class="datacell1 nonTpaFees">
									<td class="databorder" height="25"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td colspan="9" height="25" valign="center">
									<content:getAttribute beanName="introNonTpaFee" attribute="text" />
									</td>
									<td class="databorder"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								</tr>

								<TR class="nonTpaFees">
									<TD class=databorder><IMG
										src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
									<TD colSpan=9><TABLE id="nonTpaFeesTable" border=0 style="table-layout: fixed; word-wrap:break-word;"
											width="100%"  cellSpacing="0" cellPadding="0"  >
											<TBODY>
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
											<TD colspan="2"><b>Fee
													type</b></TD>
											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<TD valign="middle" align="right" colspan="2"><b>Value</b></TD>
											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<TD vAlign="middle" align="left"><b>Special notes</b></TD>
										</TR>
                                                <c:set var="count" value="0" scope="page"/>
												<c:forEach var="fee"
													items="${noticeInfo404a5Form.updatedNonTpaFees}">
													  <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
														<td valign="top" colspan="2" width="25%"><b>${fee.feeDescription}</b></td>
														<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td align="right" colspan="2" width="22%">${fee.amountValueFormatted}</td>
														<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<td width="52%">${fee.notes}</td>
													</tr>
												</c:forEach>

											</TBODY>
										</TABLE></TD>
									<TD class=databorder><IMG
										src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								</TR>
								<TD class="databorder" colSpan="11"><IMG
									src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
							    </TR>
							</c:if>

							<c:if
								test="${ not empty noticeInfo404a5Form.updatedTpaFeesStandard
											or not  empty noticeInfo404a5Form.updatedTpaFeesCustomized}">
											 <TR >
								 <TD  colspan="3" height=25 vAlign=center 
									style="padding-left: 6px">&nbsp</TD>
							    </TR>
								<TR class=tablehead>

									<TD class=tableheadTD1 height=25 vAlign=center colSpan=6
										style="padding-left: 6px"><img
										id="tpaFeesExpandCollapseIcon"
										src="/assets/unmanaged/images/minus_icon.gif" width="13"
										height="13" style="cursor: hand; cursor: pointer"> <b>Individual TPA Expenses</b></TD>
									<TD class=tableheadTD height=25 vAlign=center colSpan=4>
									<c:if test="${ noticeInfo404a5Form.tpaRestricted  eq true }">
<form:checkbox path="tpaRestricted" disabled="true" >


											  TPA access restricted
</form:checkbox>
									</c:if >
									</TD>
									<TD class=databorder><IMG
										src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
								</TR>

								<tr class="datacell1 tpaFees">
									<td class="databorder" height="25"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td colspan="9" height="25" valign="center">
									<content:getAttribute beanName="introTpaFee" attribute="text" />
									</td>
									<td class="databorder"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								</tr>


							
								<tr class="tpaFees">
									<td class="databorder"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td colspan="9"><table id="tpaFeesTable" border="0" style="table-layout: fixed; word-wrap:break-word;"
											width="100%" cellSpacing="0" cellPadding="0"  >
											<tbody>
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
											<TD colspan="2"><b>Fee
													type</b></TD>
											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<TD valign="middle" align="right" colspan="2"><b>Value</b></TD>
											<TD class="dataheaddivider"><IMG
												src="/assets/unmanaged/images/s.gif" width="1" height="1"></TD>
											<TD vAlign="middle" align="left"><b>Special notes</b></TD>
										</TR>
											    <c:set var="count" value="0" scope="page"/>
												<c:if
													test="${ not empty noticeInfo404a5Form.updatedTpaFeesStandard}">

													<c:forEach var="fee"
														items="${noticeInfo404a5Form.updatedTpaFeesStandard}">
														 <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<td colspan="2" valign="top" width="25%"><b>${fee.feeDescription}</b></td>
															<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td colspan="2" align="right" width="22%">${fee.amountValueFormatted}</td>
															<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td width="52%">${fee.notes}</td>
														</tr>
													</c:forEach>
												</c:if>
												<c:if
													test="${ not empty noticeInfo404a5Form.updatedTpaFeesCustomized}">
													<c:forEach var="fee"
														items="${noticeInfo404a5Form.updatedTpaFeesCustomized}">
														 <c:choose>
															<c:when test="${count % 2 == 0}">
																	<tr class="datacell2 "> 
															</c:when>
															<c:otherwise>
																	<tr class="datacell1 ">
															</c:otherwise>
														</c:choose> 
														<c:set var="count" value="${count + 1}" scope="page"/>
															<td colspan="2" valign="top" width="25%"><b>${fee.feeDescription}</b></td>
															<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td colspan="2" align="right" width="22%">${fee.amountValueFormatted}</td>
															<TD class=dataheaddivider ><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td width="52%">${fee.notes}</td>
														</tr>
													</c:forEach>
												</c:if>
											</tbody>
										</table></td>
									<td class="databorder"><img
										src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
								</tr>
							</c:if>
							
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
			<TR>
				<TD><IMG border=0 src="/assets/unmanaged/images/s.gif" width=30
					height=1></TD>
				<TD></TD>
				<TD width=10><IMG border=0
					src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
				<TD>&nbsp;</TD>
			</TR>
			<c:if test="${empty param.printFriendly }" >
<%-- 				<ps:form action="/do/confirm404a5NoticeInfo/" name="noticeInfo404a5Form" modelAttribute="noticeInfo404a5Form"> --%>
<input type="hidden" name="dirty" id="dirtyFlagId"/>
<input type="hidden" name="action" /><%--  input - name="noticeInfo404a5Form" --%>
					<TABLE border=0 cellSpacing=0 cellPadding=0 width=730>
						<tr>

							<td width="500" align="right"></td>

<td width="80" align="right"><input type="button" onclick="return doBack();" name="button" class="button134" value="back"/></td>



							<td width="10" align="right"></td>

<td width="80" align="right"><input type="button" onclick="return doSubmit();" name="button" class="button134" value="submit"/></td>



						</tr>
					</TABLE>
<%-- 				</ps:form> --%>
			</c:if>
			</ps:form>
			<BR>
	<TR>
		<TD><IMG border=0 src="/assets/unmanaged/images/s.gif" width=30
			height=1></TD>
		<TD></TD>
		<TD width=10><IMG border=0
			src="/assets/unmanaged/images/spacer.gif" width=15 height=1></TD>
		<TD>&nbsp;</TD>
	</TR>
<c:if test="${empty param.printFriendly}">
		<table cellpadding="0" cellspacing="0" border="0" width="730" class="fixedTable" height="">
			<tr>
				<td width="30">&nbsp;</td>
				<td width="700"><content:pageFooter beanName="layoutPageBean" /></td>
			</tr>
		</table>
</c:if>
