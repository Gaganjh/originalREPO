<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>

<%-- Imports --%>
<%@ taglib uri="/WEB-INF/ps-logicext.tld" prefix="logicext"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.web.tools.SubmissionPaymentForm" %>
<%@ page import="com.manulife.pension.ps.web.tools.SubmissionUploadForm" %>


<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_TOOLS_DEBIT_PERM%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="messageNoDebitPerm"/>							
<c:set var="lastRowStyle" value="" scope="request" />
<c:set var="trackChanges" value="false" scope="request" />



<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

SubmissionPaymentForm submissionPaymentForm = (SubmissionPaymentForm)request.getAttribute("requestForm");
pageContext.setAttribute("submissionPaymentForm",submissionPaymentForm,PageContext.PAGE_SCOPE);

String paymentTableHeight =String.valueOf(SubmissionPaymentForm.MAX_PAYMENT_TABLE_HEIGHT);
pageContext.setAttribute("paymentTableHeight",paymentTableHeight,PageContext.PAGE_SCOPE);

%>

<%-- BP AND TC --%>
<c:if test="${submissionPaymentForm.displayBillPaymentSection ==true}">
<c:if test="${submissionPaymentForm.displayTemporaryCreditSection ==true}">
<c:set var="accountWidth" value="128" />
<c:set var="headerFooterContWidth" value="85" />
<c:set var="rowContWidth" value="85" />
</c:if>
</c:if>
<%-- BP AND NOT TC --%>
<c:if test="${submissionPaymentForm.displayBillPaymentSection ==true}">
<c:if test="${submissionPaymentForm.displayTemporaryCreditSection !=true}">
<c:set var="accountWidth" value="214" />
<c:set var="headerFooterContWidth" value="85" />
<c:set var="rowContWidth" value="85" />
</c:if>
</c:if>
<%-- NOT BP AND TC --%>
<c:if test="${submissionPaymentForm.displayBillPaymentSection !=true}">
<c:if test="${submissionPaymentForm.displayTemporaryCreditSection ==true}">
<c:set var="accountWidth" value="214" />
<c:set var="headerFooterContWidth" value="85" />
<c:set var="rowContWidth" value="85" />
</c:if>
</c:if>
<%-- NOT BP AND NOT TC --%>
<c:if test="${submissionPaymentForm.displayMoreSections !=true}">
<c:set var="accountWidth" value="310" />
<c:set var="headerFooterContWidth" value="110" />
<c:set var="rowContWidth" value="110" />
</c:if>
							  <tr>
								<td class="datacell1">
									<table>
										<tr vAlign="center">
											<td>
												<span class="content">
													Payment effective date &nbsp;
<c:if test="${not empty submissionPaymentForm.theReport}">
	<report:errorIcon errors="<%=submissionPaymentForm.getTheReport()%>" codes="ID"/>
</c:if>
<c:if test="${contract.status !='CA'}">
<c:if test="${submissionPaymentForm.viewMode ==false}">
<form:input path="requestEffectiveDate" onchange="javascript:validatePaymentDateSelection();" size="9"/>
</c:if>
<c:if test="${submissionPaymentForm.viewMode ==true}">
<form:input path="requestEffectiveDate" disabled="true" size="9"/>
</c:if>
														<c:if test="${trackChanges == true}">
															<ps:trackChanges name="submissionPaymentForm" property="requestEffectiveDate"/>
</c:if>
</c:if>
<c:if test="${contract.status == 'CA'}">
<form:input path="requestEffectiveDate" disabled="true" size="9"/>
</c:if>
												</span>
											</td>
											<td>
												  <span class="content">
<c:if test="${contract.status != 'CA'}">
<c:if test="${submissionPaymentForm.viewMode ==false}">
															<a href="javascript:cal.popup();"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date"></a>
</c:if>
</c:if>
													&nbsp;(mm/dd/yyyy)
												  </span>
											</td>
											</tr>
									</table>
								</td>
							  </tr>
							  <tr class="datacell1">
								<td>&nbsp;</td>
							  </tr>
							
							<logicext:if name="submissionPaymentForm" property="paymentTableHeight" op="equal" value="<%=String.valueOf(SubmissionPaymentForm.MAX_PAYMENT_TABLE_HEIGHT)%>">
							<logicext:and name="submissionPaymentForm" property="displayMoreSections" op="equal" value="false"/>
				
							<logicext:then>
							  
							  <tr class="whiteborder">
								<td>

									<table width="500" border="0" cellpadding="0" cellspacing="0">
					<tbody>
						<tr class="tablesubhead">
							<td width="3" align="right" class="tablesubhead"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="3"></td>
							<td width="${accountWidth}" valign="top" class="tablesubhead"><img
								src="/assets/unmanaged/images/s.gif" height="1"
								width="${accountWidth}"></td>
							<td width="1" valign="bottom" class="dataheaddivider"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							<td width="${headerFooterContWidth}" valign="top"
								class="tablesubhead"><img
								src="/assets/unmanaged/images/s.gif" height="1"
								width="${headerFooterContWidth}"></td>
							<td width="15" valign="top" class="tablesubhead"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="15"></td>
						</tr>
						<tr class="tablesubhead">
							<td width="3" align="right"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="3"></td>
							<td width="${accountWidth}" valign="top"><span
								class="content"><b>Account</b></span></td>
							<td width="1" class="dataheaddivider" valign="bottom" width="1"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
							<td width="${headerFooterContWidth}" align="right" valign="top">
								<span class="content"><b>Contribution</b></span>
							</td>
							<td width="15" valign="top" class="tablesubhead"><img
								src="/assets/unmanaged/images/s.gif" height="15" width="15"></td>
						</tr>
					</tbody>
				</table>

<c:if test="${submissionPaymentForm.paymentTableHeight == paymentTableHeight}">
								<div class="paymentScroll">	
</c:if>
									<table width="485" border="0" cellpadding="0" cellspacing="0">
					<tbody>
						<tr class="datacell1">
							<td width="3" align="right" class="datacell1"><img
								src="/assets/unmanaged/images/s.gif" height="1" width="3"></td>
							<td width="${accountWidth}" align="left" class="datacell1"><img
								src="/assets/unmanaged/images/s.gif" height="1"
								width="${accountWidth}"></td>
									<td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td width="${rowContWidth}" align="right" nowrap="nowrap" class="datacell1"><img src="/assets/unmanaged/images/s.gif" height="1" width="${rowContWidth}"></td>
									<td width="15" valign="top" class="datacell1"><img src="/assets/unmanaged/images/s.gif" height="1" width="15"></td>
									  </tr>
								  	<% if (!userProfile.isAllowedDirectDebit()) { %>
									<tr class="datacell2" height="25">
										<td width="3" align="right"><img src="/assets/unmanaged/images/s.gif" height="1" width="3"></td>
										<td valign="top"><span class="content"><content:getAttribute beanName="messageNoDebitPerm" attribute="text"/></span></td>
										<td class="datadivider" width="1"></td>
										<td align="right" valign="top"><span class="content">&nbsp;</span></td>
										<td width="15" valign="top" class="datacell1"><img src="/assets/unmanaged/images/s.gif" height="1" width="15"></td>
									</tr>
									<% } else if (userProfile.isAllowedCashAccount() && submissionPaymentForm.getAccounts().size() == 1) { %>
									<tr class="datacell2" height="25">
										<td width="3" align="right"><img src="/assets/unmanaged/images/s.gif" height="1" width="3"></td>
										<td valign="top"><span class="content">Your contract is not set up for Electronic Checking. Please contact your client account representative for more information.</span></td>
										<td class="datadivider" width="1"></td>
										<td align="right" valign="top"><span class="content">&nbsp;</span></td>
										<td width="15" valign="top" class="datacell1"><img src="/assets/unmanaged/images/s.gif" height="1" width="15"></td>
									</tr>
									 <% } %>
									</tbody>
									</table>
									
									<table width="485" border="0" cellpadding="0" cellspacing="0">
									<tbody>
<c:forEach items="${submissionPaymentForm.accounts}" var="theAccount" varStatus="theIndex" >

<c:set var="indexValue" value="${theIndex.index}"/> 
				<% 				
				    String temp = pageContext.getAttribute("indexValue").toString();%>
				 <% if (Integer.parseInt(temp) % 2 == 0) { %>
									        <tr class="datacell1">
									        <% pageContext.setAttribute("lastRowStyle", "datacell2") ; %>
										<% } else { %>
									        <tr class="datacell2">
									        <% pageContext.setAttribute("lastRowStyle", "datacell1") ;%>
										<% } %>
												<td width="3" align="right" class="datacell<%= Integer.parseInt(temp) % 2 + 1 %>"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
<td width="${accountWidth}" align="left">
													<span class="content">
<c:if test="${theAccount.type == 'C'}">
							<a href=javascript:guideWindow("/do/contentpages/userguide/thirdlevelpop/?contentKey=54294")>
								</c:if> 
								${theAccount.label} 
								<c:if test="${theAccount.type == 'C'}">
									</a>
								</c:if> 
								<c:if test="${not empty submissionPaymentForm.paymentInfo}">
									<c:if test="${theAccount.type == 'C'}">
										<br />Current Balance:<b><render:number
												property="submissionPaymentForm.paymentInfo.cashAccountTotalBalance"
												type="c" /></b>
										<br />Available for allocation:<b><render:number
												property="submissionPaymentForm.paymentInfo.cashAccountAvailableBalance"
												type="c" /></b>
									</c:if>
								</c:if> </span></td>
							<td width="1" class="datadivider"></td>
							<td width="${rowContWidth}" align="right" nowrap="nowrap"><span
								class="content">$&nbsp;
									<c:if test="${!submissionPaymentForm.viewMode}">
										 <form:input cssClass="inputamountUPLOAD" maxlength="15" cssStyle="{text-align: right}" size="11"  path="amounts[${theIndex.index}]"  onblur="validatePaymentInstructionInput(this)" onfocus="this.select()"  />
									</c:if>
									<c:if test="${submissionPaymentForm.viewMode}"> 
									 <form:input cssClass="inputamountUPLOAD" maxlength="15" cssStyle="{text-align: right}" size="11"  path="amounts[${theIndex.index}]" onblur="validatePaymentInstructionInput(this)" onfocus="this.select()" disabled  />
									 </c:if></span>
								<c:if test="${trackChanges == true}">
									<ps:trackChanges name="submissionPaymentForm"
										property='<%= "amounts[" + pageContext.getAttribute("indexValue") + "]" %>' />
								</c:if></td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
									</div>
									<table width="500" border="0" cellpadding="0" cellspacing="0">
									 <tbody>
<tr class="${lastRowStyle}" valign="top" height="30">
										<td width="3" align="right"><img src="/assets/unmanaged/images/s.gif" height="1" width="3"></td>
<td align="left" width="${accountWidth}" class="tablesubheadbottom">
											<strong>Totals</strong>
<c:if test="${not empty submissionPaymentForm.theReport}">
												<report:errorIcon errors="<%=submissionPaymentForm.getTheReport()%>" codes="LD,CA"/>
</c:if>
										</td>
										<td class="datadivider" width="1"></td>
<td align="right" width="${headerFooterContWidth}" class="tablesubheadbottom">
											<table class="databorder" border="0" cellpadding="0" cellspacing="0" width="35">
												<tbody>
													<tr>
														<td><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
													</tr>
												</tbody>
											</table>
											<span class="highlightBold" align="right" id="contributionTotal">$0.00</span>
										</td>
										<td width="15" valign="top" class="tablesubheadbottom"><img src="/assets/unmanaged/images/s.gif" height="15" width="15"></td>
									</tr>
<tr class="${lastRowStyle}">
<td align="right" class="${lastRowStyle}"><img src="/assets/unmanaged/images/s.gif" height="1" width="3"></td>
<td valign="top" class="tablesubheadbottom" width="${accountWidth}"><img src="/assets/unmanaged/images/s.gif" height="1" width="${accountWidth}"></td>
									<td valign="bottom" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
<td valign="top" class="tablesubheadbottom" width="${headerFooterContWidth}"><img src="/assets/unmanaged/images/s.gif" height="1" width="${headerFooterContWidth}"></td>
									<td width="15" valign="top" class="tablesubheadbottom"><img src="/assets/unmanaged/images/s.gif" height="1" width="15"></td>
									</tr>
								</tbody>
								</table>
 								</td>
							  </tr>
							</logicext:then>
							
							<logicext:else>	
							  <tr class="whiteborder">
								<td>

									<table width="500" border="0" cellpadding="0" cellspacing="0">
									<tbody>
    								  <tr class="tablesubhead">
										<td width="3" align="right" class="tablesubhead"><img src="/assets/unmanaged/images/s.gif" height="1" width="3"></td>
<td width="${accountWidth}" valign="top" class="tablesubhead"><img src="/assets/unmanaged/images/s.gif" height="1" width="${accountWidth}"></td>
										<td width="1" valign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
<td width="${headerFooterContWidth}" valign="top" class="tablesubhead"><img src="/assets/unmanaged/images/s.gif" height="1" width="${headerFooterContWidth}"></td>
<c:if test="${submissionPaymentForm.displayBillPaymentSection ==true}">
										<td width="1" valign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td width="85" valign="top" class="tablesubhead"><img src="/assets/unmanaged/images/s.gif" height="1" width="85"></td>
</c:if>
<c:if test="${submissionPaymentForm.displayTemporaryCreditSection ==true}">
										<td width="1" valign="bottom" class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td width="85" valign="top" class="tablesubhead"><img src="/assets/unmanaged/images/s.gif" height="1" width="85"></td>
</c:if>
<c:if test="${submissionPaymentForm.displayMoreSections ==true}">
										<td width="1" align="right" class="tablesubhead"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td width="1" align="right" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td width="109" valign="top" class="tablesubhead"><img src="/assets/unmanaged/images/s.gif" height="1" width="109"></td>
</c:if>
									  </tr>
									  <tr class="tablesubhead">
										<td width="3" align="right"><img src="/assets/unmanaged/images/s.gif" height="1" width="3"></td>
<td width="${accountWidth}" valign="top"><span class="content"><b>Account</b></span></td>
										<td width="1" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
<td width="${headerFooterContWidth}" align="right" valign="top">
											<span class="content"><b>Contribution</b></span>
<c:if test="${submissionPaymentForm.displayMoreSections !=true}">
												<img src="/assets/unmanaged/images/s.gif" height="1" width="23">
</c:if>
										</td>
<c:if test="${submissionPaymentForm.displayBillPaymentSection ==true}">
										<td width="1" class="dataheaddivider" valign="bottom"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td width="85" align="right" valign="top">
												<span class="content">
													<b>Outstanding bill:&nbsp;</b>
													<render:number property="submissionPaymentForm.paymentInfo.outstandingBillPayment" type="c"/>
												</span>
										</td>
</c:if>
<c:if test="${submissionPaymentForm.displayTemporaryCreditSection ==true}">
										<td width="1" class="dataheaddivider" valign="bottom"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td width="85" align="right" valign="top">
												<span class="content">
													<b>Temporary credit:&nbsp;</b>
													<render:number property="submissionPaymentForm.paymentInfo.outstandingTemporaryCredit" type="c"/>
												</span>
										</td>
</c:if>
<c:if test="${submissionPaymentForm.displayMoreSections ==true}">
										<td width="1 class="highlightBold" align="right""><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
										<td width="1" class="databorder" align="right"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
										<td width="85" class="highlightBold" align="right">
											<strong>Total</strong>
											<img src="/assets/unmanaged/images/s.gif" height="1" width="21">
										</td>
</c:if>
									  </tr>
								  	</tbody>
								</table>

<c:if test="${submissionPaymentForm.paymentTableHeight ==paymentTableHeight}">
								<div class="paymentScroll">	
</c:if>
									<table width="500" border="0" cellpadding="0" cellspacing="0">
									<tbody>
									<tr class="datacell1">
										  <td width="3" align="right" class="datacell1"><img src="/assets/unmanaged/images/s.gif" height="1" width="3"></td>
<td width="${accountWidth}" align="left" class="datacell1"><img src="/assets/unmanaged/images/s.gif" height="1" width="${accountWidth}"></td>
										  <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
<td width="${rowContWidth}" align="right" nowrap="nowrap" class="datacell1"><img src="/assets/unmanaged/images/s.gif" height="1" width="${rowContWidth}"></td>
<c:if test="${submissionPaymentForm.displayBillPaymentSection ==true}">
										  <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										  <td width="85" align="right" nowrap="nowrap" class="datacell1"><img src="/assets/unmanaged/images/s.gif" height="1" width="85"></td>
</c:if>
<c:if test="${submissionPaymentForm.displayTemporaryCreditSection ==true}">
										  <td width="1" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										  <td width="85" align="right" nowrap="nowrap" class="datacell1"><img src="/assets/unmanaged/images/s.gif" height="1" width="85"></td>
</c:if>
<c:if test="${submissionPaymentForm.displayMoreSections ==true}">
										  <td width="1" align="right"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										  <td width="1" align="right" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
<c:if test="${submissionPaymentForm.paymentTableHeight ==paymentTableHeight}">
										    <td width="91" class="datacell1"><img src="/assets/unmanaged/images/s.gif" height="1" width="91"></td>
</c:if>
<c:if test="${submissionPaymentForm.paymentTableHeight !=paymentTableHeight}">
										    <td width="109" class="datacell1"><img src="/assets/unmanaged/images/s.gif" height="1" width="109"></td>
</c:if>
</c:if>
									  </tr>
								  	<% if (!userProfile.isAllowedDirectDebit()) { %>
									<tr class="datacell2" height="25">
										<td width="3" align="right"><img src="/assets/unmanaged/images/s.gif" height="1" width="3"></td>
										<td valign="top"><span class="content"><content:getAttribute beanName="messageNoDebitPerm" attribute="text"/></span></td>
										<td class="datadivider" width="1"></td>
										<td align="right" valign="top"><span class="content">&nbsp;</span></td>
<c:if test="${submissionPaymentForm.displayBillPaymentSection ==true}">
										<td class="datadivider" width="1"></td>
										<td align="right" valign="top">&nbsp;</td>
</c:if>
<c:if test="${submissionPaymentForm.displayTemporaryCreditSection ==true}">
										<td class="datadivider" width="1"></td>
										<td align="right" valign="top">&nbsp;</td>
</c:if>
<c:if test="${submissionPaymentForm.displayMoreSections ==true}">
										<td class="highlightBold" align="right"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
										<td class="databorder" align="right"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
										<td class="highlightBold" align="right"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1">&nbsp;</td>
</c:if>
									</tr>
									<% } else if (userProfile.isAllowedCashAccount() && submissionPaymentForm.getAccounts().size() == 1) { %>
									<tr class="datacell2" height="25">
										<td width="3" align="right"><img src="/assets/unmanaged/images/s.gif" height="1" width="3"></td>
										<td valign="top"><span class="content">Your contract is not set up for Electronic Checking. Please contact your client account representative for more information.</span></td>
										<td class="datadivider" width="1"></td>
										<td align="right" valign="top"><span class="content">&nbsp;</span></td>
<c:if test="${submissionPaymentForm.displayBillPaymentSection ==true}">
										<td class="datadivider" width="1"></td>
										<td align="right" valign="top">&nbsp;</td>
</c:if>
<c:if test="${submissionPaymentForm.displayTemporaryCreditSection ==true}">
										<td class="datadivider" width="1"></td>
										<td align="right" valign="top">&nbsp;</td>
</c:if>
<c:if test="${submissionPaymentForm.displayMoreSections ==true}">
										<td class="highlightBold" align="right"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
										<td class="databorder" align="right"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
										<td class="highlightBold" align="right"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1">&nbsp;</td>
</c:if>
									</tr>
									 <% } %>

<c:forEach items="${submissionPaymentForm.accounts}" var="theAccount" varStatus="theIndex" >
<c:set var="indexValue" value="${theIndex.index}"/> 
				<% 				
				    String temp = pageContext.getAttribute("indexValue").toString();
				    if (Integer.parseInt(temp) % 2 == 0) { %>
									        <tr class="datacell1">
									        <% 
									        if(Integer.parseInt(temp)==0){
									        	pageContext.setAttribute("lastRowStyle", "datacell1"); 
									        }else{
									        pageContext.setAttribute("lastRowStyle", "datacell2");}%>
										<% } else { %>
									        <tr class="datacell2">
									        <% pageContext.setAttribute("lastRowStyle", "datacell1") ; %>
										<% } %>
												<td width="3" align="right" class="datacell<%= Integer.parseInt(temp) % 2 + 1 %>"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
<td width="${accountWidth}" align="left">
													<span class="content">
<c:if test="${theAccount.type == 'C'}">
						<a href=javascript:guideWindow("/do/contentpages/userguide/thirdlevelpop/?contentKey=54294")>
</c:if>
${theAccount.label}
<c:if test="${theAccount.type == 'C'}">
															</a>
</c:if>
<c:if test="${not empty submissionPaymentForm.paymentInfo}">
<c:if test="${theAccount.type == 'C'}">
																	<br/>Current Balance:<b><render:number property="submissionPaymentForm.paymentInfo.cashAccountTotalBalance" type="c"/></b>
																	<br/>Available for allocation:<b><render:number property="submissionPaymentForm.paymentInfo.cashAccountAvailableBalance" type="c"/></b>
</c:if>
</c:if>
													</span>
												</td>
												<td width="1" class="datadivider"></td>
<td width="${rowContWidth}" align="right" >

<span class="content"><c:if test="${!submissionPaymentForm.viewMode}">
									 $<form:input path="amounts[${theIndex.index}]" cssClass="inputamountUPLOAD" maxlength="15" cssStyle="{text-align: right}" 
									  size="9" onblur="validatePaymentInstructionInput(this)" onfocus="this.select()"/>
	
									</c:if>
									<c:if test="${submissionPaymentForm.viewMode}"> 
									 $<form:input path="amounts[${theIndex.index}]" cssClass="inputamountUPLOAD" maxlength="15" cssStyle="{text-align: right}" size="9" onblur="validatePaymentInstructionInput(this)" onfocus="this.select()" disabled  />
									 </c:if></span>
													<c:if test="${trackChanges == true}">
														<ps:trackChanges name="submissionPaymentForm" property='<%= "amounts[" + pageContext.getAttribute("indexValue") + "]" %>'/>
</c:if>
												</td>
 <c:if test="${submissionPaymentForm.displayBillPaymentSection ==true}">
												<td width="1" class="datadivider"></td>
												<td width="85" align="right" nowrap="nowrap">
<span class="content"><c:if test="${!submissionPaymentForm.viewMode}">
									 $<form:input cssClass="inputamountUPLOAD"  maxlength="13" cssStyle="{text-align: right}" size="9"   path="billAmounts[${theIndex.index}]"  onblur="validatePaymentInstructionInput(this)" onfocus="this.select()"  />
									</c:if>
									<c:if test="${submissionPaymentForm.viewMode}"> 
									 $<form:input  cssClass="inputamountUPLOAD" maxlength="13" cssStyle="{text-align: right}" size="9"  path="billAmounts[${theIndex.index}]"  onblur="validatePaymentInstructionInput(this)" onfocus="this.select()" disabled  />
									 </c:if></span>
													<c:if test="${ trackChanges == true}">
														<ps:trackChanges name="submissionPaymentForm"  property='<%= "billAmounts[" + pageContext.getAttribute("indexValue") + "]" %>'/>
</c:if>
												</td>
</c:if>
<c:if test="${submissionPaymentForm.displayTemporaryCreditSection ==true}">
												<td width="1" class="datadivider"></td>
												<td width="85" align="right" nowrap="nowrap">
<span class="content"><c:if test="${!submissionPaymentForm.viewMode}">
									 $<form:input  cssClass="inputamountUPLOAD" maxlength="13" cssStyle="{text-align: right}" size="9"  path="creditAmounts[${theIndex.index}]"  onblur="validatePaymentInstructionInput(this)" onfocus="this.select()"  />
									</c:if>
									<c:if test="${submissionPaymentForm.viewMode}"> 
									 $<form:input cssClass="inputamountUPLOAD" maxlength="13" cssStyle="{text-align: right}" size="9" path="creditAmounts[${theIndex.index}]"  onblur="validatePaymentInstructionInput(this)" onfocus="this.select()" disabled  />
									 </c:if></span>
													<c:if test="${ trackChanges == true}">
														<ps:trackChanges name="submissionPaymentForm"  property='<%= "creditAmounts[" + pageContext.getAttribute("indexValue") + "]" %>'/>
</c:if>
												</td>
</c:if> 
<c:if test="${submissionPaymentForm.displayMoreSections ==true}">
												<td width="1" class="highlightBold" align="right"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
												<td width="1" class="databorder" align="right"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
<c:if test="${submissionPaymentForm.paymentTableHeight ==paymentTableHeight}">
												<td width="85" class="highlightBold" align="right">
</c:if>
<c:if test="${submissionPaymentForm.paymentTableHeight !=paymentTableHeight}">
												<td width="110" class="highlightBold" align="right">
</c:if>
<span class="highlightBold" id="accountsRow${theIndex.index}Total">$0.00</span>
												</td>
</c:if>
											</tr>
</c:forEach>
										</tbody>
									</table>
									</div>
									<table width="500" border="0" cellpadding="0" cellspacing="0">
									 <tbody>
<tr class="${lastRowStyle}" valign="top" height="30">
<!-- <td width="3" align="right"><img src="/assets/unmanaged/images/s.gif" height="1" width="3"></td> -->
<td align="left" width="${accountWidth}" class="tablesubheadbottom">
		&nbsp;<strong>Totals</strong>
<c:if test="${not empty submissionPaymentForm.theReport}">
												<report:errorIcon errors="<%=submissionPaymentForm.getTheReport()%>" codes="LD,CA"/>
</c:if>
										</td>
										<td class="datadivider" width="1"></td>
<td align="right" width="${headerFooterContWidth}" class="tablesubheadbottom">
											<table class="databorder" border="0" cellpadding="0" cellspacing="0" width="55">
												<tbody>
													<tr>
														<td><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
													</tr>
												</tbody>
											</table>
											<span class="highlightBold" align="right" id="contributionTotal">$0.00</span>
										</td>
<c:if test="${submissionPaymentForm.displayBillPaymentSection ==true}">
										<td class="datadivider" width="1"></td>
										<td align="right" class="tablesubheadbottom">
											<table class="databorder" border="0" cellpadding="0" cellspacing="0" width="55">
												<tbody>
													<tr>
														<td><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
													</tr>
												</tbody>
											</table>
											<span class="highlightBold" align="right" id="billPaymentTotal">$0.00</span>
										</td>
</c:if>
<c:if test="${submissionPaymentForm.displayTemporaryCreditSection ==true}">
										<td class="datadivider" width="1"></td>
										<td align="right" class="tablesubheadbottom">
											<table class="databorder" border="0" cellpadding="0" cellspacing="0" width="89">
												<tbody>
													<tr>
														<td><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
													</tr>
												</tbody>
											</table>
											<span class="highlightBold" align="right" id="temporaryCreditTotal">$0.00</span>
										</td>
</c:if>
<c:if test="${submissionPaymentForm.displayMoreSections ==true}">
										<td align="right">
											<img src="/assets/unmanaged/images/spacer.gif" height="1" width="1">
										</td>
										<td class="databorder" align="right">
											<img src="/assets/unmanaged/images/spacer.gif" height="1" width="1">
										</td>
										<td align="right" class="tablesubheadbottom">
<c:if test="${submissionPaymentForm.paymentTableHeight ==paymentTableHeight}">
											<table class="databorder" border="0" cellpadding="0" cellspacing="0" width="55">
</c:if>
<c:if test="${submissionPaymentForm.paymentTableHeight !=paymentTableHeight}">
											<table class="databorder" border="0" cellpadding="0" cellspacing="0" width="85">
</c:if>
												<tbody>
													<tr>
														<td><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
													</tr>
												</tbody>
											</table>
											<span class="highlightBold" align="right" id="grandTotal">$0.00</span>
<c:if test="${submissionPaymentForm.paymentTableHeight ==paymentTableHeight}">
												<img src="/assets/unmanaged/images/s.gif" height="1" width="21">
</c:if>
<c:if test="${submissionPaymentForm.paymentTableHeight !=paymentTableHeight}">
												<img src="/assets/unmanaged/images/s.gif" height="1" width="1">
</c:if>
										</td>
</c:if>
									</tr>
<tr class="${lastRowStyle}">

<%-- <td align="right" class="${lastRowStyle}"><img src="/assets/unmanaged/images/s.gif" height="1" width="3"></td> --%>
<td valign="top" class="tablesubheadbottom" width="${accountWidth}"><img src="/assets/unmanaged/images/s.gif" height="1" width="${accountWidth}"></td>
									<td valign="bottom" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
<td valign="top" class="tablesubheadbottom" width="${headerFooterContWidth}"><img src="/assets/unmanaged/images/s.gif" height="1" width="${headerFooterContWidth}"></td>
<c:if test="${submissionPaymentForm.displayBillPaymentSection ==true}">
									<td valign="bottom" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td valign="top" class="tablesubheadbottom"><img src="/assets/unmanaged/images/s.gif" height="1" width="85"></td>
</c:if>
<c:if test="${submissionPaymentForm.displayTemporaryCreditSection ==true}">
									<td valign="bottom" class="datadivider"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td valign="top" class="tablesubheadbottom"><img src="/assets/unmanaged/images/s.gif" height="1" width="85"></td>
</c:if>
<c:if test="${submissionPaymentForm.displayMoreSections ==true}">
									<td align="right" class="tablesubheadbottom"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td align="right" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									<td valign="top" class="tablesubheadbottom"><img src="/assets/unmanaged/images/s.gif" height="1" width="109"></td>
</c:if>
									</tr>
								</tbody>
								</table>
 								</td>
							  </tr>
							</logicext:else>
							</logicext:if>
							  
