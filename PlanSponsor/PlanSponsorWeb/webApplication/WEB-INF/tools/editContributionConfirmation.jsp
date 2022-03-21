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
<%@ page import="com.manulife.pension.platform.web.util.DataUtility" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.Contract" %>
<%@ page import="com.manulife.pension.ps.web.tools.EditContributionDetailBean" %>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
EditContributionDetailBean detail = (EditContributionDetailBean)session.getAttribute(Constants.EDIT_CONTRIBUTION_CONFIRM_DETAIL_DATA);
pageContext.setAttribute("detail",detail,PageContext.REQUEST_SCOPE);
EditContributionDetailBean bean = (EditContributionDetailBean)session.getAttribute(Constants.EDIT_CONTRIBUTION_CONFIRM_DETAIL_DATA);
pageContext.setAttribute("bean",bean,PageContext.PAGE_SCOPE);
ContributionDetailsReportData theReport = (ContributionDetailsReportData)session.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

Contract currentContract = userProfile.getCurrentContract();
pageContext.setAttribute("currentContract",currentContract,PageContext.PAGE_SCOPE);


%> 

<content:contentBean contentId="<%=ContentConstants.SUBMISSION_UPLOAD_PAYMENT_TEXT%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        beanName="fileUploadPaymentNote"/>

        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="fixedTable">
			<tr>
              <td width="30" class="big"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
              <td height="20">
              <strong><content:getAttribute beanName="layoutPageBean" attribute="body1Header" /></strong>
						<table cellspacing="0" cellpadding="0" width="100%" border="0">
                          <tr>
                            <td width="200" valign="top"><!--DWLayoutEmptyCell-->&nbsp;</td>
                            <td width="300" valign="top"><!--DWLayoutEmptyCell-->&nbsp;</td>
                          </tr>
                          <tr>
                            <td nowrap><strong>Contract&nbsp;</strong></td>
                            <td><strong class="highlight"><%= DataUtility.compute8DigitContractNumber(currentContract.getContractNumber())%>
${userProfile.currentContract.companyName}</strong></td>

                          </tr>
                          <tr>
                          	<td nowrap>&nbsp;</td>
                          	<td>&nbsp;</td>
                          </tr>

						  <tr>
                            <td nowrap><strong>Submission number&nbsp;</strong></td>
<td><strong class="highlight">${detail.confirmationNumber}</strong></td>
                          </tr>
                          <tr>
                        	<td nowrap>&nbsp;</td>
                        	<td>&nbsp;</td>
                        </tr>
                        <tr>
                        <td nowrap><strong>Submitted by&nbsp;</strong></td>
<td ><strong class="highlight">${detail.sender}</strong></td>
                      </tr>

						  <tr>
                            <td nowrap><strong>Received&nbsp;</strong></td>
                            <td ><strong class="highlight"><render:date property="detail.receivedDate" patternOut="MMMM dd, yyyy hh:mm a z" defaultValue=""/></strong></td>
                          </tr>
                          <tr>
                      		<td nowrap>&nbsp;</td>
                      		<td>&nbsp;</td>
                          </tr>

						  <tr>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false)  { %>						  
							  <td><strong>Payroll date&nbsp;</strong></td>
<% } else { %>
							  <td><strong>Contribution date&nbsp;</strong></td>
<% } %>							  
							  <td><strong class="highlight"><render:date property="detail.payrollDate" patternOut="MMMM dd, yyyy" defaultValue=""/></strong></td>
							</tr>

<c:if test="${not empty detail.generateStatementOption}">
							<tr>
							  <td valign="top"><strong>Generate Statements for quarter&nbsp;</strong></td>
<td><strong class="highlight">${detail.generateStatementOption}</strong><br>
  						      <% if (bean.getGenerateStatementOption().indexOf("will be") != -1) { %>
<c:forEach items="${detail.statementDates}" var="item" varStatus="theIndex" >
								&nbsp;&nbsp;<span class="highlightBold"><render:date property="item.startDate" patternOut="MMM-d-yyyy" defaultValue=""/> to
								<render:date property="item.endDate" patternOut="MMM-d-yyyy" defaultValue=""/><br>
								</span>
</c:forEach>
							<% } %>
							  </td>
							</tr>
</c:if>
							<tr>
	                         <tr>
	                      		<td nowrap>&nbsp;</td>
	                      		<td>&nbsp;</td>
	                          </tr>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false)  { %>
							  <td><strong>Number of participants&nbsp;</strong></td>
<td><strong class="highlight">${detail.numberOfParticipants}</strong></td>
<% } %>							  
							</tr>
							<tr>
							  <td><strong>Submission type&nbsp;</strong></td>
<td><strong class="highlight">${detail.submissionType}</strong></td>
							</tr>
	                         <tr>
	                      		<td nowrap>&nbsp;</td>
	                      		<td>&nbsp;</td>
	                          </tr>

							<tr>
							  <td><strong>Total allocation&nbsp;</strong></td>
<td><strong class="highlight">${detail.totalAllocations}</strong></td>
							</tr>
<c:if test="${detail.totalLoanRepayments ne '0.00'}">
							<tr>
							  <td><strong>Total loan repayment&nbsp;</strong></td>
<td><strong class="highlight">${detail.totalLoanRepayments}</strong></td>
							</tr>
							<tr>
							  <td><strong>Total contribution&nbsp;</strong></td>
<td><strong class="highlight">${detail.totalContributions}</strong></td>
							</tr>
</c:if>
							<tr>
							  <td><strong>Total allocation by money type </strong></td>
							  <td>
									<table>
										<tr>
<c:forEach items="${detail.contributionsByMoneyType}" var="moneyPair" >
												<td>
<strong>${moneyPair.value}</strong><BR/>
<strong class="highlight">${moneyPair.label}</strong>
												</td>
</c:forEach>
										</tr>
									</table>
							  </td>
							</tr>
                          <tr>
                            <td height="14" colspan="2">&nbsp;</td>
                          </tr>
                          <tr>
                            <td height="14" ><strong>Payment information</strong></td>
								<td height="14" >&nbsp;</td>
                          </tr>
						  <jsp:include page="paymentConfirmSection.jsp" flush="true" />
                          <tr>
                            <td height="14" colspan="2">&nbsp;</td>
                          </tr>
                          <tr valign="top">
                            <% if (theReport.getNumErrors() > 0
                            		|| theReport.getNumWarnings() > 0) { %>
                            	<td height="14" ><strong>Errors/Warnings</strong></td>
								<td height="14" ><report:submissionErrors errors="<%=theReport%>" width="100%" printFriendly="false" forceView="true" includeTable="false"/></td>
 							<% } else { %>
							    <td height="14" ><img src="/assets/unmanaged/images/spacer.gif" width="1" height="14" border="0"></td>
								<td height="14" >&nbsp;</td>
							<% } %>
                          </tr>
                      </table>
           	</td>
			<td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
			<td width="210" valign="top">
				   <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
			</td>
			<!--// end column 3 -->

		</tr>
		<tr align="center">
			<td colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="15"></td>
		</tr>
		<tr align="center">
			<td></td>
			<td colspan="2">
				<p/>
		        <table border="0" cellspacing="0" cellpadding="0" align="center">
		          <tr>
<td><input type="button" onclick="javascript:print(); return false;" name="actionLabel" class="button100Lg" value="print"/></td>
					<td width='15'  ></td>
<td><input type="submit" class="button175" onclick="window.location.href='/do/tools/submissionHistory/'; return false;" value="submission history" /></td>
		          </tr>
		        </table>
			</td>
			<td></td>
		</tr>
		<tr align="center">
			<td colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="15"></td>
		</tr>
<c:if test="${detail.displayDebitFootnote ==true}">
		<tr>
		  <td class="big">&nbsp;</td>
		  <td height="20"><content:getAttribute beanName="fileUploadPaymentNote" attribute="text"/>
		  </td>
		  <td width="15"><img src="/assets/unmanaged/images/spacer.gif" width="15" height="1" border="0"></td>
		  <td></td>
		</tr>
</c:if>
	</table>



