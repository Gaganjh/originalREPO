<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalPayeePaymentVO"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalMoneyTypeVO"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalCalculatedInfoVO"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalDetailsReportItem"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.CompletedWithdrawalDetailsReportData"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 

<%
CompletedWithdrawalDetailsReportData theReport = (CompletedWithdrawalDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>



<%-- <content:contentBean contentId="<%=ContentConstants.MULTI_PAYEE_NOTIFICATION%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="MultiPayeeNotify" />  --%>
	
<c:forEach items="${theReport.details}" var="withdrawalDetailsItem" varStatus="theIndex" >

  

	
	<tr class="tableheadTD">
		<td colspan="8" class="tableheadTD" style="padding: 3px;">
			<strong>
				<content:getAttribute
				beanName="layoutPageBean" attribute="body2Header" />
			</strong>
		</td>
	</tr>

	<tr class="tableheadTD">
		<td colspan="10" class="multipayeeSec" >
			<br/>
			<c:set var="payeeN" value ="${payeeCount}" />
			<table width="98%" align="center" border="0" cellpadding="0">
			
			<tbody>
				<!--  Iterate and display payee payment Information -->
<c:forEach items="${withdrawalDetailsItem.withdrawalPayeePaymentVO}" var="payee" varStatus="index" >

<c:set var="indexValue" value="${index.index}"/>

						<tr>
							<TD width="30" align="left" id="payment">
								<c:if test="${payeeN gt 1}">
								${indexValue+1}.
								</c:if> 
							</TD>
<TD id="payment"> Payee name: ${payee.payeeName} </TD>
						 </tr>
							 <tr>
							<TD width="30" >
							</TD>
							<td>
							<table width="100%" border="0" cellpadding="5" cellspacing="0">
							 <TBODY>
							
							 <tr>
								<td>
									<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
								</td>
							</tr>
							<tr>
								<td align="left" valign="top">
									<strong>Payment amount:</strong>
									<render:number	property="payee.paymentAmount" type="c" sign="true" />
									<br/>
									
									<strong>Payment method:</strong>
${payee.paymentMethod}
									<br/>
									
									<strong>Payment to:</strong>
${payee.paymentTo}
									<br/>
									
																	
									
								</td>
							</tr>
							<tr>
								<td id="multiSubTitle3" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
								</td>
							</tr> 
							<tr>
								<td id="multiSubTitle" width="179" align="left" valign="top">
									Payee address
								</td>
							</tr>
							<tr>
								<td align="left" valign="top">
<p>${payee.payeeName}
									<br/>
									
${payee.addressLine1}
									<br/>
								
${payee.addressLine2}
									<br />
								
<c:if test="${not empty payee.city}">
${payee.city},<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
</c:if>
<c:if test="${not empty payee.state}">
${payee.state}<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
</c:if>
${payee.zip}
									<br/>
									
${payee.country}</p>
								</td>
							</tr>
							 <tr>
								<td id="multiSubTitle2" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
								</td>
							</tr> 
							<tr>
								<td id="multiSubTitle" width="193" align="left" valign="top">
									Bank details
								</td>
							</tr>
							<tr>
								<td align="left" valign="top">
									<strong>Bank / Branch name:</strong> 
${payee.bankBranchName}
									<br/>
									
									<strong>ABA / Routing number:</strong> 
${payee.routingABAnumber}
									<br/>
									
								
									<strong>Credit party name:</strong>
${payee.creditPayeeName}
								</td>
							</tr>
							 <tr>
								<td id="multiSubTitle2" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="0" height="1">
								</td>
							</tr> 
							</TBODY>
							</table>
									<!-- WDMar2017 Changes - wilange : PSW WithdrawalReport Changes -->
<c:if test="${not empty payee.afterTaxVO}">
								 <table id="table-stacked-cost" class="table table-bordered table-hover"  width="670">
					                <TBODY>

					                 <TR>
                             			 <TD width="193" align="left" id="multiSubTitle" colspan="7">After Tax Cost Basis</TD>
                       			     </TR>
                       			      <TR class="tablesubhead">
			                              <TD width="1" class="greyborder"><IMG width="0" height="1" alt="" src="/assets/unmanaged/images/s.gif"></TD>
			                              <TD width="264" height="25" align="left"><B>Money Type</B></TD>
			                              <TD width="1" align="right" class="dataheaddivider"><IMG width="0" height="1" alt="" src="/assets/unmanaged/images/s.gif"></TD>
			                              <TD width="140" align="right"><B>Net Contributions ($)</B></TD>
			                              <TD width="1" align="right" class="dataheaddivider"><IMG width="0" height="1" alt="" src="/assets/unmanaged/images/s.gif"></TD>
			                              <TD width="110" align="right"><B>Net Earnings($)</B></TD>
			                              <TD width="1" align="right" class="dataheaddivider"><IMG width="0" height="1" alt="" src="/assets/unmanaged/images/s.gif"></TD>
			                              <TD width="140" align="right"><B>Withdrawal Amount ($)</B></TD>
			                              <TD width="1" align="right" class="greyborder"><IMG width="0" height="1" alt="" src="/assets/unmanaged/images/s.gif"></TD>
			                            </TR>
                       			     
               
<c:forEach items="${payee.afterTaxVO}" var="afterTaxInfo" varStatus="index" >



									<c:set var="rowId" value="${index.index %2}" />
									<c:if test="${rowId==0}">
										<c:set var="rowClass" value="datacell1" />
</c:if>
									<c:if test="${rowId!=0}">
										<c:set var="rowClass" value="datacell2" />
</c:if>
									
					                  <tr class="${rowClass}">
					                  <TD width="1" class="greyborder"><IMG width="0" height="1" alt="" src="/assets/unmanaged/images/s.gif"></TD>
<TD height="10">${afterTaxInfo.moneyTypeInfo}</TD>
			                              <TD class="datadivider"><IMG width="0" height="1" alt="" src="/assets/unmanaged/images/s.gif"></TD>
			                              <TD align="right"> <render:number
												property="afterTaxInfo.afterTaxNetContribution"
												type="c" sign="false" /></TD>
			                              <TD class="datadivider"><IMG width="0" height="1" alt="" src="/assets/unmanaged/images/s.gif"></TD>
			                              <TD align="right"> <render:number
												property="afterTaxInfo.afterTaxNetEarning"
												type="c" sign="false" /></TD>
			                              <TD class="datadivider"><IMG width="0" height="1" alt="" src="/assets/unmanaged/images/s.gif"></TD>
			                              <TD align="right"><render:number
												property="afterTaxInfo.afterTaxWDAmt"
												type="c" sign="false" /></TD>
			                              <TD width="1" align="right" class="greyborder"><IMG width="0" height="1" alt="" src="/assets/unmanaged/images/s.gif"></TD>
					                  </tr>
</c:forEach>
                  
					                </tbody>
					              </table>
</c:if>
							
						</td>
						</tr>
						
</c:forEach>
			</tbody>
			</table>
		<br/>
		</td>
	</tr>
</c:forEach>
