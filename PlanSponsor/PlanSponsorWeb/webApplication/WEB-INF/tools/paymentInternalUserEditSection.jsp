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
<%@ page import="com.manulife.pension.ps.web.tools.SubmissionAccountBean" %>
<%@ page import="com.manulife.pension.ps.web.tools.SubmissionUploadDetailBean" %>
<%@ page import="com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData" %>
<%@ page import="com.manulife.pension.ps.web.tools.EditContributionDetailsForm" %>
<c:set var="lastRowStyle" value="" scope="request" />
<%-- copy the form from the request in to the page scope --%>
<c:set var="theReport" value="${editContributionDetailsForm.theReport}" scope="page" />
<c:set var="detail" value="${editContributionDetailsForm.paymentDetails}" scope="page" />
<%
EditContributionDetailsForm editContributionDetailsForm = (EditContributionDetailsForm)request.getAttribute("requestForm");
pageContext.setAttribute("editContributionDetailsForm",editContributionDetailsForm,PageContext.PAGE_SCOPE);
ContributionDetailsReportData theReport = (ContributionDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

  <tr class="whiteborder">
	<td><span class="content"><content:getAttribute beanName="layoutPageBean" attribute="body2"/></span></td>
  </tr>
  <tr class="whiteborder">
	<td>
		<table>
			<tr vAlign="center">
				<td>
					<b>Payment effective date:&nbsp;</b>
					<report:errorIcon errors="<%=theReport%>" codes="ID"/>
					<strong class="highlight">
							<render:date property="detail.requestedEffectiveDate" patternOut="MMMM dd, yyyy" defaultValue=""/>
					</strong>
				</td>
				<td>
					  <span class="content">
						&nbsp;
					  </span>
				</td>
				</tr>
		</table>
	</td>
  </tr>
  <tr class="whiteborder">
	<td colspan="1" >&nbsp;</td>
  </tr>
  <tr class="whiteborder">
	<td>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tbody>
		  <tr class="tablesubhead">
			<td valign="top" width="100%">
				<span class="content"><b>Account</b></span>
			</td>
			<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
			<td align="right" valign="top" width="100"><span class="content"><b>Contribution</b></span></td>
<c:if test="${detail.displayBillPaymentSection ==true}">
			<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
			<td align="right" valign="top" width="85">
					<span class="content">
						<b>Outstanding bill</b>
					</span>
			</td>
</c:if>
<c:if test="${detail.displayCreditPaymentSection ==true}">
			<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
			<td align="right" valign="top" width="85">
					<span class="content">
						<b>Temporary credit</b>
					</span>
			</td>
</c:if>
<c:if test="${detail.displayTotalPaymentSection ==true}">
			<td class="highlightBold" align="right" width="1"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
			<td class="databorder" align="right" width="1"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
			<td class="highlightBold" align="right" width="85"><strong>Total</strong></td>
</c:if>
		</tr>
<c:forEach items="${detail.paymentInstructions}" var="instruction" varStatus="theIndex" >
<c:set var="indexValue" value="${theIndex.index}"/> 
				<% 				
				    String temp = pageContext.getAttribute("indexValue").toString();%>
				 <% if (Integer.parseInt(temp) % 2 == 0) { %>
		<tr class="datacell1">
		<% pageContext.setAttribute("lastRowStyle", "datacell2"); %>
	<% } else { %>
		<tr class="datacell2">
		<% pageContext.setAttribute("lastRowStyle", "datacell1"); %>
	<% } %>
			<td align="left">
				<span class="content">
${instruction.label}
				</span>
			</td>
			<td class="datadivider" width="1"></td>
			<td align="right" nowrap width="100">
				<span class="content"><render:number property="instruction.contributionValue" defaultValue=""  type="c"/></span>
			</td>
<c:if test="${detail.displayBillPaymentSection ==true}">
			<td class="datadivider" width="1"></td>
			<td align="right" nowrap width="85">
				<span class="content"><render:number property="instruction.billValue" defaultValue=""  type="c"/></span>
			</td>
</c:if>
<c:if test="${detail.displayCreditPaymentSection ==true}">
			<td class="datadivider" width="1"></td>
			<td align="right" nowrap width="85">
				<span class="content"><render:number property="instruction.creditValue" defaultValue=""  type="c"/></span>
			</td>
</c:if>
<c:if test="${detail.displayTotalPaymentSection ==true}">
			<td class="highlightBold" align="right"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
			<td class="databorder" align="right"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
			<td class="highlightBold" align="right" width="85">
<span class="highlightBold" id="accountsRow${theIndex.index}Total"><render:number property="instruction.totalValue" type="c"/></span>
			</td>
</c:if>
		</tr>
</c:forEach>

<tr class="${lastRowStyle}" valign="top" height="30">
			<td align="left">
				<strong>Totals</strong>
				<report:errorIcon errors="<%=theReport%>" codes="LD,CA"/>
			</td>
			<td class="datadivider" width="1"></td>
			<td align="right" width="100">
				<table class="databorder" border="0" cellpadding="0" cellspacing="0" width="70">
					<tbody>
						<tr>
							<td><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
						</tr>
					</tbody>
				</table>
				<span class="highlightBold" id="contributionTotal"><render:number property="detail.contributionTotal" type="c"/></span>
			</td>
<c:if test="${detail.displayBillPaymentSection ==true}">
			<td class="datadivider" width="1"></td>
			<td align="right">
				<table class="databorder" border="0" cellpadding="0" cellspacing="0" width="70">
					<tbody>
						<tr>
							<td><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
						</tr>
					</tbody>
				</table>
				<span class="highlightBold" id="billPaymentTotal"><render:number property="detail.billTotal" type="c"/></span>
			</td>
</c:if>
<c:if test="${detail.displayCreditPaymentSection ==true}">
			<td class="datadivider" width="1"></td>
			<td align="right">
				<table class="databorder" border="0" cellpadding="0" cellspacing="0" width="70">
					<tbody>
						<tr>
							<td><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
						</tr>
					</tbody>
				</table>
				<span class="highlightBold" id="temporaryCreditTotal"><render:number property="detail.creditTotal" type="c"/></span>
			</td>
</c:if>
<c:if test="${detail.displayTotalPaymentSection ==true}">
			<td align="right">
				<img src="/assets/unmanaged/images/spacer.gif" height="1" width="1">
			</td>
			<td class="databorder" align="right">
				<img src="/assets/unmanaged/images/spacer.gif" height="1" width="1">
			</td>
			<td align="right">
				<table class="databorder" border="0" cellpadding="0" cellspacing="0" width="55">
					<tbody>
						<tr>
							<td><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
						</tr>
					</tbody>
				</table>
<span class="highlightBold" id="grandTotal">$${detail.paymentInstructionsTotal}</span>
			</td>
</c:if>
		</tr>
	</tbody>
	</table>
	</td>
  </tr>
