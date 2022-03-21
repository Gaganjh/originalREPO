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
<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_NO_PAYMENT%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageNoPaymentInstructionsIncluded"/>

<c:if test="${not empty detail.paymentInstructions}">
							<tr> 
							  <td>Payment effective date&nbsp;</td>
							  <td><strong class="highlight"><render:date property="detail.requestedEffectiveDate" patternOut="MMMM dd, yyyy" defaultValue=""/></strong></td>
							</tr>
							<tr> 
							  <td colspan=2 > <br> </td>
							</tr>
							<tr> 
							  <td colspan="2"> 
								<table border="0" cellspacing="0" cellpadding="0" width="100%">
								  <tr> 
									<td width="250" ><b>Account </b></td>
									<td valign="bottom"> 
									  <div align="right"><b>Contribution Amount</b></div>
									</td>
<c:if test="${detail.displayBillPaymentSection ==true}">
									<td width="12"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
									<td valign="bottom"> 
									  <div align="right"><b>Bill Payment Amount</b></div>
									</td>
</c:if>
<c:if test="${detail.displayCreditPaymentSection ==true}">
		<td width="12"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
									<td valign="bottom"> 
									  <div align="right"><b>Temporary Credit Amount</b></div>
									</td>
</c:if>
<c:if test="${detail.displayTotalPaymentSection ==true}">
									<td class="highlightBold" align="right" width="12"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
									<td class="databorder" align="right" width="1"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
									<td class="highlightBold" align="right" width="75" valign="bottom"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"><strong>Total</strong></td>
</c:if>
								  </tr>
								  <tr>
									<td colspan="2" height='8'></td>
<c:if test="${detail.displayBillPaymentSection ==true}">
									<td width="12"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
									<td align='right'></td>
</c:if>
<c:if test="${detail.displayCreditPaymentSection ==true}">
									<td width="12"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
									<td align='right'></td>
</c:if>
<c:if test="${detail.displayTotalPaymentSection ==true}">
									<td class="highlightBold" align="right" width="12"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
									<td class="databorder" align="right" width="1"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
									<td class="highlightBold" align="right" width="75"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
</c:if>
								  </tr>
<c:forEach items="${detail.paymentInstructions}" var="instruction" varStatus="theIndex" >

								  		<tr>
<td width='250' >${instruction.label}</td>
								  			<td align='right'  >
								  				<render:number property="instruction.contributionValue" type="c"/>
								  			</td>
<c:if test="${detail.displayBillPaymentSection ==true}">
											  <td width="12"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
											  <td align='right'  >
											  	<render:number property="instruction.billValue" type="c"/>
											  </td>
</c:if>
<c:if test="${detail.displayCreditPaymentSection ==true}">
											  <td width="12"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
											  <td align='right'  >
											 	<render:number property="instruction.creditValue" type="c"/>
											  </td>
</c:if>
<c:if test="${detail.displayTotalPaymentSection ==true}">
												<td class="highlightBold" align="right" width="12"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
												<td class="databorder" align="right" width="1"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
												<td class="highlightBold" align="right" width="75">
													<span class="highlightBold"><render:number property="instruction.totalValue" type="c"/></span>
												</td>
</c:if>
								  		</tr>
</c:forEach>
								  
								  
									<tr valign="bottom">
										<td align="left">
											<strong>Totals</strong>
										</td>
										<td align="right">
											<table class="databorder" border="0" cellpadding="0" cellspacing="0" width="55">
												<tbody>
													<tr>
														<td><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
													</tr>
												</tbody>
											</table>
											<span class="highlightBold"><render:number property="detail.contributionTotal" type="c"/></span>
										</td>
<c:if test="${detail.displayBillPaymentSection ==true}">
										<td width="12"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
										<td align="right">
											<table class="databorder" border="0" cellpadding="0" cellspacing="0" width="55">
												<tbody>
													<tr>
														<td><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
													</tr>
												</tbody>
											</table>
											<span class="highlightBold"><render:number property="detail.billTotal" type="c"/></span>
										</td>
</c:if>
<c:if test="${detail.displayCreditPaymentSection ==true}">
										<td width="12"><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
										<td align="right">
											<table class="databorder" border="0" cellpadding="0" cellspacing="0" width="55">
												<tbody>
													<tr>
														<td><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
													</tr>
												</tbody>
											</table>
											<span class="highlightBold"><render:number property="detail.creditTotal" type="c"/></span>
										</td>
</c:if>
<c:if test="${detail.displayTotalPaymentSection ==true}">
										<td align="right">
											<img src="/assets/unmanaged/images/spacer.gif" height="1" width="1">
										</td>
										<td class="databorder" align="right" width="1">
											<img src="/assets/unmanaged/images/spacer.gif" height="1" width="1">
										</td>
										<td align="right">
											<img src="/assets/unmanaged/images/spacer.gif" height="1" width="1">
											<table class="databorder" border="0" cellpadding="0" cellspacing="0" width="55">
												<tbody>
													<tr>
														<td><img src="/assets/unmanaged/images/spacer.gif" height="1" width="1"></td>
													</tr>
												</tbody>
											</table>
<span class="highlightBold">$${detail.paymentInstructionsTotal}</span>
										</td>
</c:if>
									</tr>
								</table>
							  </td>
							</tr>
</c:if>

<c:if test="${empty detail.paymentInstructions}">
							<tr> 
							  <td colspan="2" ><content:getAttribute beanName="messageNoPaymentInstructionsIncluded" attribute="text"/><br>
							  </td>
							</tr>
</c:if>

