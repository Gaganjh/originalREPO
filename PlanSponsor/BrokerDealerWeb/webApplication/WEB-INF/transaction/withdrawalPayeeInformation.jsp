
<%-- taglib used --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/bdweb-taglib.tld" prefix="bd"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>

<%-- Imports --%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.CompletedWithdrawalDetailsReportData"%>
<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalPayeePaymentVO"%>

<%@ page
	import="com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalDetailsReportItem"%>
<%@ page import="java.util.List"%>
	<%@ page import="java.util.ArrayList"%>
	<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
	
	<%-- Beans used --%>
	
<%

CompletedWithdrawalDetailsReportData theReport = (CompletedWithdrawalDetailsReportData)request.getAttribute(BDConstants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>


		
		
	<%-- This jsp includes the following CMA content --%>
	
	<content:contentBean
		contentId="<%=BDContentConstants.MESSAGE_MULTI_PAYEE_NOTIFICATION%>"
		type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="MultiPayeeNotify" />

<c:forEach items="${theReport.details}" var="withdrawalDetailsItem" varStatus="theIndex"  >


			<div class="page_section_subheader_wd">
	<h3>
					<content:getAttribute id="layoutPageBean"
					attribute="body1Header" />
				</h3>
		</div>
		

			<c:set var="payeeN" value ="${payeeCount}" />
			
 			 <c:if test="${not empty withdrawalDetailsItem.withdrawalPayeePaymentVO}">
			 
<c:forEach items="${withdrawalDetailsItem.withdrawalPayeePaymentVO}" var="payee" varStatus="index" >
<c:set var="indexValue" value="${index.index}"/>  

 

			<div class="report_table">
       		 <div class="page_section_subsubheader" style="font: 12px/normal verdana; PADDING-TOP: 4PX;">
         		<h4><c:if test="${payeeN gt 1}">${indexValue+1}</c:if>
Payee Name: ${payee.payeeName}</h4>
       			 </div>
    	  </div>
			 
			<div id="payees">
			<table id="payeesTD" bgcolor="#f5f4f0" width="100%" align="center"
				border="0" cellpadding="0" cellspacing="0">
				<tbody>
					
							<tr>
								<td width="1%" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
								<td align="left" width="22%" valign="top">
									<strong>Payment Amount:</strong>
									</td>
								<td align="left"  width="77%"  valign="top">
<c:if test="${not empty payee.paymentAmount}">
										<render:number property="payee.paymentAmount" type="c" sign="true" />
</c:if>
								</td>
							</tr>
							<tr>
								<td width="1%" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
								<td align="left" valign="top">
									<strong>Payment Method: </strong>
									</td>
								<td align="left" valign="top">
${payee.paymentMethod}
								</td>
							</tr>
							<tr>
								<td width="1%" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
								<td align="left" valign="top">
										<strong>Payment To:</strong>
									</td>
								<td align="left"   valign="top">
${payee.paymentTo}
									
								</td>
							</tr>
							<tr>
								<td width="1%" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
								<td align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
								<td id="multiSubTitle2" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
							</tr>
							<tr>
								<td width="1%" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
								<td id="multiSubTitle" colspan="2" align="left" valign="top">
									Payee Address
								</td>
							</tr>
							<tr>
								<td width="1%" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
								<td align="left" valign="top" colspan="2">
${payee.payeeName}
									<br/>
${payee.addressLine1}
									 <br/>
${payee.addressLine2}
									 <br/>
									
<c:if test="${not empty payee.city}">
${payee.city},<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
</c:if>
<c:if test="${not empty payee.state}">
${payee.state}<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
</c:if>
${payee.zip}
									 <br/>
${payee.country}
								</td>
							</tr>
							<tr>
								<td width="1%" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
								<c:if test="${payeeN gt 1}">
								<td align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td> 
								</c:if>
								<td id="multiSubTitle3" colspan="2" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
							</tr>
							<tr>
								<td width="1%" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
								<c:if test="${payeeN gt 1}">
								<td align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
								</c:if>
								<td id="multiSubTitle"colspan="2" align="left" valign="top">
									Bank Details
								</td>
							</tr>
							<tr>
								<td width="1%" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</TD>
             					 <td width="22%" align="left" valign="top">
									<strong>Bank / Branch Name:</strong>
									</TD>
            	 				 <td width="77%" align="left" valign="top">
${payee.bankBranchName}
									
								</td>
							</tr>
							<tr>
								<td width="1%" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</TD>
             					 <td width="22%" align="left" valign="top">
									<strong>ABA / Routing Number: </strong>
									</TD>
            	 				 <td width="77%" align="left" valign="top">
${payee.routingABAnumber}
								</td>
							</tr>
							<tr>
								<td width="1%" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</TD>
             					 <td width="22%" align="left" valign="top">
									<strong>Credit Party Name: </strong>
									</TD>
            	 				 <td width="77%" align="left" valign="top">
${payee.creditPayeeName}

								</td>
							</tr>
							
							
							<tr>
								<td width="1%" align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td>
								<c:if test="${payeeN gt 1}">
								<td align="left" valign="top">
									<img src="/assets/unmanaged/images/s.gif" alt="" width="1" height="1">
								</td> 
								</c:if>
<c:if test="${not empty payee.afterTaxVO}">
								<td id="multiSubTitle" colspan="2" align="left" valign="top">
									After-tax Cost Basis
								</td>
</c:if>
							</tr>
							
				
				</tbody>
				</table>
				</div>
<c:if test="${not empty payee.afterTaxVO}">
					<DIV class="report_table">
						<TABLE width="100%" class="report_table_content">
										<THEAD>
											<TR class="val_str">
												<TH width="40%" class="val_str align_center">Money Type
												</TH>
												<TH width="20%" class="val_str align_center">Net
													Contributions ($)</TH>
												<TH width="20%" class="val_str align_center">Net
													Earnings($)</TH>

												<TH width="20%" class="val_str align_center">
													Withdrawal Amount($)</TH>


											</TR>
										</THEAD>
										<TBODY>


<c:forEach items="${payee.afterTaxVO}" var="afterTaxInfo" varStatus="index" >



												<c:set var="rowId" value="${index.index %2}" />
												<c:if test="${ rowId == 0}">
													<c:set var="rowClass" value="datacell1" />
</c:if>
												<c:if test="${ rowId != 0}">
													<c:set var="rowClass" value="datacell2" />
</c:if>
												<tr class="${rowClass}">
<td>${afterTaxInfo.moneyTypeInfo}</td>

													<td align="right"><render:number
															property="afterTaxInfo.afterTaxNetContribution" type="c"
															sign="false" /></td>
													<td align="right"><render:number
															property="afterTaxInfo.afterTaxNetEarning" type="c"
															sign="false" /></td>

													<td align="right"><render:number
															property="afterTaxInfo.afterTaxWDAmt" type="c"
															sign="false" /></td>

												</tr>
</c:forEach>

										</tbody>
									</table>
								</DIV>

</c:if> 
</c:forEach>
				</c:if> 
</c:forEach>
