<!DOCTYPE html>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
 
<%-- Imports --%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.service.report.notice.valueobject.ContractMailingOrderReportData"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeMailingOrderVO"%>

<content:contentBean contentId="<%=ContentConstants.NMC_ORDER_MAILING_CONFIRMATION%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="mailingNameSectionTitle"/>
<content:contentBean contentId="<%=ContentConstants.NMC_ORDER_STATUS_SECTION_INTRO%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="sectionIntro"/>
<content:contentBean contentId="<%=ContentConstants.NMC_ORDER_STATUS_NO_SEARCH%>" type="<%=ContentConstants.TYPE_MESSAGE%>" id="noSearchResult"/>
<%-- <c:if test="${not empty Constants.REPORT_BEAN} }"> --%>
<%
ContractMailingOrderReportData theReport = (ContractMailingOrderReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>

<c:set var="printFriendly" value="${param.printFriendly}" />
<%-- </c:if> --%>


<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />


 <ps:form cssStyle="margin-bottom:0;" method="POST" modelAttribute="orderStatusForm" name="orderStatusForm" action="/do/noticemanager/orderstatus/">
					    <form:hidden path="uploadAndShareTab" />
	<form:hidden path="buildYourPackageTab"/>
	<form:hidden path="buildYourPackageNPTab"  />
	<form:hidden path="orderStatusTab" />
					<input value="save" type="hidden" name="action"> <br>
					
					<content:errors scope="session"/>
					<table border="0" cellspacing="0" cellpadding="0" width="708">
						<tbody>
								   <tr>
												<jsp:include page="planNoticeTabs.jsp" flush="true">
														<jsp:param value="3" name="tabValue"/>
												</jsp:include>
									</tr>
									
									<tr>
										<TD class=boxborder width=1><IMG border=0 src="/assets/unmanaged/images/spacer.gif" width=1 height=1></TD>
										<TD> 
										
										<!-- start table content -->
											<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
												<TBODY>
													<TR>
									
										<td class="tableheadTD1" colspan="7" height="25"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									</tr>
									<tr>
										<td width="200"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td width="120"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td width="220"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td width="120"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
									</tr>
									<tr class="datacell1 disableLink">
										<td colspan="7">
											<table border="0" cellpadding="5" cellspacing="0">
												<tbody>
													<tr>
														<td><content:getAttribute attribute="text" beanName="sectionIntro"/>
														<c:if test="${printFriendly != null }" >
															<script>
																$(document).ready(function(){
																	$(".disableLink a").click(function(e){
																		e.preventDefault();
																	});
																});
															</script>
														</c:if>
														</td>
													</tr>
											</tbody></table>
										</td>
									</tr>
									<tr class="datacell1">
										<td colspan="7">&nbsp;</td>
									</tr>
									
									<!-- Start of body title -->
									<tr class="tablehead">
										<td class="tableheadTD1" colspan="7">
											<table border="0" width="100%" cellpadding="0" cellspacing="0">
												<tbody>
													<tr>
														<td class="tableheadTD" width="50%" style="padding-left: 2px;"><b><content:getAttribute attribute="text" beanName="mailingNameSectionTitle"/></b></td>
														<td align="left"  class="tableheadTDinfo">
															<c:if test="${theReport != null}"> 
															<B> <report:recordCounter report="theReport"
																	label="Orders" /></B>
															</c:if>
														</td>
														<td class="tableheadTDinfo"></td>
														<td align="right"  class="tableheadTD">
														<c:if test="${theReport != null}"> 
															<report:pageCounter report="theReport" arrowColor="black" formName="orderStatusForm"/>
														</c:if>
														</td>
													</tr>
												</tbody>
											</table>
										</td>
									</tr>
									<!-- End of body title -->
									<tr class="tablesubhead">
										<td valign="top" style="padding-left: 4px;">
										<c:if test="${printFriendly == null }" ><b>
											<report:sort field="<%=ContractMailingOrderReportData.MAILING_NAME%>"
											direction="desc" formName="orderStatusForm">Mailing name</report:sort></b>
										</c:if>
											<c:if test="${printFriendly != null }" ><b>
												Mailing name</b>
										</c:if></td>
										<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td valign="top" style="padding-left: 4px;">
											<c:if test="${printFriendly == null }" >
												<b><report:sort field="<%=ContractMailingOrderReportData.ORDER_NUMBER%>"
												direction="desc" formName="orderStatusForm">Order number</report:sort></b>
											</c:if>
											<c:if test="${printFriendly != null }" >
												<b>Order number</b>
											</c:if>
										</td>
										<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td valign="top" style="padding-left: 4px;">
											<c:if test="${printFriendly == null }" ><b>
												<report:sort field="<%=ContractMailingOrderReportData.ORDER_STATUS%>"
													direction="desc" formName="orderStatusForm">Order status</report:sort>
												</b>
											</c:if>
											<c:if test="${printFriendly != null }" ><b>
												Order status
												</b>
											</c:if>
										</td>
										<td class="dataheaddivider" valign="bottom" width="1">
										<img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td valign="top" style="padding-left: 4px;">
										<c:if test="${printFriendly == null }" ><b>
											<report:sort field="<%=ContractMailingOrderReportData.ORDER_STATUS_DATE%>"
											direction="desc" formName="orderStatusForm">Order status date</report:sort>
											</b>
										</c:if>
										<c:if test="${printFriendly != null }" ><b>
											Order status date
											</b>
										</c:if></td>
									</tr>
									
								<c:if test="${theReport != null }">
								
<c:if test="${theReport.planNoticeMailingOrders != null}">
<c:forEach items="${theReport.planNoticeMailingOrders}" var="theItem" varStatus="theIndex" >
 <c:set var="indexValue" value="${theIndex.index}"/> 
 <% 
  String indexVal = pageContext.getAttribute("indexValue").toString();
 %>


											
									<% if (Integer.parseInt(indexVal) % 2 == 0) { %>
							        <tr class="datacell3">
									<% } else { %>
							        <tr class="datacell1">
									<% } %>
								
<td style="padding-left: 4px;">${theItem.mailingName}</td>
										<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
<td style="padding-left: 4px;"><c:if test="${theItem.planNoticeMailingOrderStatus.lookupCode !='IN'}">
<c:if test="${theItem.planNoticeMailingOrderStatus.lookupCode !='IC'}">
${theItem.orderNumber}
</c:if>
</c:if>
											
<c:if test="${theItem.planNoticeMailingOrderStatus.lookupCode =='IN'}">
												NA
</c:if>
<c:if test="${theItem.planNoticeMailingOrderStatus.lookupCode =='IC'}">
													NA
</c:if>
											</td>
										<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td style="padding-left: 4px;">
${theItem.planNoticeMailingOrderStatus.lookupDesc}</td>
										<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
										<td style="padding-left: 4px;"><render:date
																		patternOut="MM/dd/yyyy"
																		property="theItem.orderStatusDate" /></td>
										
									</tr>
</c:forEach>
</c:if>
									</c:if>
<c:if test="${empty theReport.planNoticeMailingOrders}">
										 <tr class="datacell1">
											<td colspan="7"><content:getAttribute attribute="text" beanName="noSearchResult"/></td>
										
									</tr>
</c:if>
								</TBODY>
							</TABLE>
								</td>
								
								<TD class=boxborder width=1><IMG border=0 src="/assets/unmanaged/images/spacer.gif" width=1 height=1></TD>
								
							</tr>
							<tr>
								<td colspan="3"><table border="0" cellspacing="0"
										cellpadding="0" width="100%">
										<tbody>
											<tr>
												<td class="boxborder"><img border="0"
													src="/assets/unmanaged/images/spacer.gif" width="1" height="1"></td>
											</tr>
										</tbody>
									</table>
								</td>
							</tr>
							<tr>
					        	<td colspan="3">         
					            <table width="100%" border="0" cellspacing="0" cellpadding="0">
						            <tr>
								        <td width="50%"></td>
							        		<td>
							        		<c:if test="${theReport != null}"> 
															<b><report:recordCounter report="theReport" label="Orders"/></b>
													</c:if>
											</td>
											<td/>
							        		<td align="right">
							        		<c:if test="${theReport != null}"> 
							        		<report:pageCounter report="theReport" arrowColor="black" formName="orderStatusForm"/>
															</c:if></td>
								   	 </tr>
							    </table>
					            </td>
					        </tr>
					        <tr>
						   	  	<td colspan="3">
								<br>
								<p><content:pageFooter beanName="layoutPageBean"/></p>
					 			<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
					 			<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
					 		  	</td>
					  		</tr>
						</tbody>
					</table>
</ps:form>
<c:if test="${printFriendly != null }" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
         type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
         id="globalDisclosure"/>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>
