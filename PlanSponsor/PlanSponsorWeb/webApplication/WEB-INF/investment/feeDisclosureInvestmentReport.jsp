<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps"%>
<%@ taglib uri="/WEB-INF/render" prefix="render"%>

<%@ page import="com.manulife.pension.ps.web.util.Environment"%>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.util.render.RenderConstants"%>
<%@ page import="com.manulife.pension.platform.web.util.DataUtility"%>
<%@ page import="com.manulife.pension.ps.service.report.investment.valueobject.InvestmentCostReportData" %>
<%@ page import="com.manulife.pension.service.fee.util.estimatedcosts.FeeDataContextInterface.EstimatedJhRecordKeepingCostSummary" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
String SIG_PLUS_RATE_TYPE = Constants.RATE_TYPE_CX0;
pageContext.setAttribute("SIG_PLUS_RATE_TYPE",SIG_PLUS_RATE_TYPE,PageContext.PAGE_SCOPE);
String CY1_RATE_TYPE = Constants.RATE_TYPE_CY1;
pageContext.setAttribute("CY1_RATE_TYPE",CY1_RATE_TYPE,PageContext.PAGE_SCOPE);
String CY2_RATE_TYPE = Constants.RATE_TYPE_CY2;
pageContext.setAttribute("CY2_RATE_TYPE",CY2_RATE_TYPE,PageContext.PAGE_SCOPE);
if(request.getAttribute(Constants.REPORT_BEAN) != null){
	
	InvestmentCostReportData theReport = (InvestmentCostReportData)request.getAttribute(Constants.REPORT_BEAN);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
}

 EstimatedJhRecordKeepingCostSummary recordKeepingCostsummary =(EstimatedJhRecordKeepingCostSummary)request.getAttribute(Constants.ESTIMATED_JH_RECORDKEEPING_COST_SUMMARY);
 pageContext.setAttribute("recordKeepingCostsummary",recordKeepingCostsummary,PageContext.PAGE_SCOPE);	
 
 	String SITEMODE_USA=Constants.SITEMODE_USA;
	String SITEMODE_NY=Constants.SITEMODE_NY;
	String FUND_PACKAGE_RETAIL=Constants.FUND_PACKAGE_RETAIL;
	String FUND_PACKAGE_BROKER=Constants.FUND_PACKAGE_BROKER;
	String FUND_PACKAGE_HYBRID=Constants.FUND_PACKAGE_HYBRID;
	String FUND_PACKAGE_VENTURE = Constants.FUND_PACKAGE_VENTURE;
	String FUND_PACKAGE_MULTICLASS =Constants.FUND_PACKAGE_MULTICLASS;
	
	String EARLY_REDEMPTION_RETAIL_USPDF_URL=Constants.EARLY_REDEMPTION_RETAIL_USPDF_URL;
	String EARLY_REDEMPTION_BROKER_USPDF_URL=Constants.EARLY_REDEMPTION_BROKER_USPDF_URL;
	String EARLY_REDEMPTION_HYBRID_USPDF_URL=Constants.EARLY_REDEMPTION_HYBRID_USPDF_URL;
	String EARLY_REDEMPTION_VENTURE_USPDF_URL = Constants.EARLY_REDEMPTION_VENTURE_USPDF_URL;
	String EARLY_REDEMPTION_RETAILNML_USPDF_URL=Constants.EARLY_REDEMPTION_RETAILNML_USPDF_URL;
	String EARLY_REDEMPTION_HYBRIDNML_USPDF_URL= Constants.EARLY_REDEMPTION_HYBRIDNML_USPDF_URL;
	
	String EARLY_REDEMPTION_BROKER_NYPDF_URL=Constants.EARLY_REDEMPTION_BROKER_NYPDF_URL;
	String EARLY_REDEMPTION_HYBRID_NYPDF_URL=Constants.EARLY_REDEMPTION_HYBRID_NYPDF_URL;
	String EARLY_REDEMPTION_RETAIL_NYPDF_URL=Constants.EARLY_REDEMPTION_RETAIL_NYPDF_URL;
	String EARLY_REDEMPTION_VENTURE_NYPDF_URL = Constants.EARLY_REDEMPTION_VENTURE_NYPDF_URL;
	String EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL=Constants.EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL;
	String EARLY_REDEMPTION_RETAILNML_NYPDF_URL= Constants.EARLY_REDEMPTION_RETAILNML_NYPDF_URL;
	
	pageContext.setAttribute("SITEMODE_USA",SITEMODE_USA,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("SITEMODE_NY",SITEMODE_NY,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("FUND_PACKAGE_RETAIL",FUND_PACKAGE_RETAIL,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("FUND_PACKAGE_BROKER",FUND_PACKAGE_BROKER,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("FUND_PACKAGE_HYBRID",FUND_PACKAGE_HYBRID,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("FUND_PACKAGE_VENTURE",FUND_PACKAGE_VENTURE,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("FUND_PACKAGE_MULTICLASS",FUND_PACKAGE_MULTICLASS,PageContext.PAGE_SCOPE);
	
	pageContext.setAttribute("EARLY_REDEMPTION_RETAIL_USPDF_URL",EARLY_REDEMPTION_RETAIL_USPDF_URL,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("EARLY_REDEMPTION_BROKER_USPDF_URL",EARLY_REDEMPTION_BROKER_USPDF_URL,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("EARLY_REDEMPTION_HYBRID_USPDF_URL",EARLY_REDEMPTION_HYBRID_USPDF_URL,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("EARLY_REDEMPTION_VENTURE_USPDF_URL",EARLY_REDEMPTION_VENTURE_USPDF_URL,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("EARLY_REDEMPTION_RETAILNML_USPDF_URL",EARLY_REDEMPTION_RETAILNML_USPDF_URL,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("EARLY_REDEMPTION_HYBRIDNML_USPDF_URL",EARLY_REDEMPTION_HYBRIDNML_USPDF_URL,PageContext.PAGE_SCOPE);
	
	pageContext.setAttribute("EARLY_REDEMPTION_BROKER_NYPDF_URL",EARLY_REDEMPTION_BROKER_NYPDF_URL,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("EARLY_REDEMPTION_HYBRID_NYPDF_URL",EARLY_REDEMPTION_HYBRID_NYPDF_URL,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("EARLY_REDEMPTION_RETAIL_NYPDF_URL",EARLY_REDEMPTION_RETAIL_NYPDF_URL,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("EARLY_REDEMPTION_VENTURE_NYPDF_URL",EARLY_REDEMPTION_VENTURE_NYPDF_URL,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL",EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL,PageContext.PAGE_SCOPE);
	pageContext.setAttribute("EARLY_REDEMPTION_RETAILNML_NYPDF_URL",EARLY_REDEMPTION_RETAILNML_NYPDF_URL,PageContext.PAGE_SCOPE);
%>

<%-- CMA contents constants --%>
<un:useConstants scope="request" var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants scope="request" var="constants" className="com.manulife.pension.ps.web.Constants" />

<% 
	pageContext.setAttribute("isMerrillContract", userProfile.isMerrillLynchContract());
%>
 <content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}"  
				                        contentId="${contentConstants.REPORT_DETAILS_SUMMARY_CONTENT}"
				                        beanName="reportDetailsSummaryContent"/>
 <content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}"  
				                        contentId="${contentConstants.CLASS_ZERO_FOOTER}"
				                        beanName="classZeroFooter"/>
				                        
<content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}"  
				                        contentId="${contentConstants.NON_CLASS_ZERO_FOOTER}"
				                        beanName="nonClassZeroFooter"/>
				                        
<content:contentBean type="${contentConstants.TYPE_FEE_DISCLSOURES}"  
				                        contentId="${contentConstants.REGULATORY_FEE_WAIVER_DISCLOSURE_TEXT}"
				                        beanName="feeWaiverDisclosure"/>				                        
                   				                        
<content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}"  
				                        contentId="${contentConstants.REGULATORY_RESTRICTED_FUNDS_TEXT}"
				                        beanName="restricedFundsText"/>	
<content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}"  
				                        contentId="${contentConstants.SVGIF_DISCLOSURE}"
				                        beanName="svgifDisclosure"/>				                        
		<input type="hidden" name="pdfCapped" />				                        	                        		                        		                        
	<SCRIPT language=javascript>
	/**
	 * Submits the form.
	 */
	function doSubmit() {
	    document.investmentSelectionReportForm.submit();
	}
	/**
	 * Assigns selected view type and submits the form.
	 */
	function doView(view) {
		document.investmentSelectionReportForm.selectedView.value = view;
		doSubmit();
	}
	</SCRIPT>      

<%-- Error Table --%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><img src="/assets/unmanaged/images/s.gif" width="30"
			height="1"><br>
		<img src="/assets/unmanaged/images/s.gif" height="1"></td>
		<td width="100%" valign="top">
		<p><content:errors scope="session" /></p>
		</td>
	</tr>
</table>

<!--  Data part of the report -->
<ps:form modelAttribute="investmentSelectionReportForm" name="investmentSelectionReportForm" method="post" action="/do/investments/investmentRelatedCosts/">
<input type="hidden" name="selectedView"/>
<TABLE cellSpacing=0 cellPadding=0 width=100% border=0>
	<TBODY>	
		<TR>
			<TD width=30></TD>
			<TD width=715>
			<TABLE cellSpacing=0 cellPadding=0 width=730 border=0>
				<TBODY>
					<TR class=tablehead>
						<TD class="tableheadTDinfo" colSpan=16>
							<B>Summary as of</B> &nbsp;
<c:if test="${investmentSelectionReportForm.selectedView !='selected'}">
							    <B><render:date property="theReport.feeEffectiveDate"
 								                patternOut="<%=RenderConstants.EXTRA_LONG_MDY%>" /></B>
</c:if>
<c:if test="${investmentSelectionReportForm.selectedView == 'selected'}">
							    <ps:select name="investmentSelectionReportForm"
									       property="selectedAsOfDate"
									       onchange="doSubmit();" style="width:150px;">
									 <ps:dateOptions
												   name="<%= Constants.REPORT_DATES_FEE_DISCLOSURE %>"
												   renderStyle="<%=RenderConstants.LONG_STYLE%>" />
								</ps:select>
</c:if>
						</TD>
					</TR>
					
					<!--  ESTIMATED RECORDKEEPING COST COMPONENT STARTS -->
					<TR>
						<TD class=databorder>
													<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
													</TD>
						<TD class=boxbody width="100%"><TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
								<TBODY>
									<TR>
										
										<TD width="100%">
											${recordKeepingCostsummary.introductionText}
										</TD>
									</TR>
								</TBODY>
							</TABLE></TD>
						<TD class=databorder>
													<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
													</TD>
					</TR>
					<TR bgcolor="#DCECF1">
						<TD class=databorder>
													<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
													</TD>
						<TD class=boxbody width="100%"><TABLE style="table-layout: fixed" border=0 cellSpacing=0 cellPadding=0 width="100%">
								<TBODY>
									<TR>
										<TD width="715">
										<span class="copyBig">${recordKeepingCostsummary.lableText}${recordKeepingCostsummary.totalAmount}%
										</span></TD>
									</TR>
								</TBODY>
							</TABLE>
						</TD>
						<TD class=databorder>
													<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
													</TD>
					</TR>
					
					<c:if test="${recordKeepingCostsummary.preAlignmentIndicator eq false}">			
					<TR class=datacell1>
						<TD class=databorder>
													<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
													</TD>
						<TD vAlign=bottom width="100%">
							<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
								<TBODY>
								<TR>
										<!--   TOTAL A + B SUB-SECTION headings -->
										<TD valign="top" colspan="100%">
										<table border="0" cellspacing="0" cellpadding="0" width="100%">
													<tbody>
														<tr class="subsubhead">
															<td bgcolor="#CCCCCC" style="padding: 6px;" valign="top" width="50%">${recordKeepingCostsummary.sectionTotalA.introductionText}</td>
															<td><img src="/assets/s.gif" width="1" height="1"></td>
															<td bgcolor="#CCCCCC" style="padding: 6px;" valign="top" width="50%">${recordKeepingCostsummary.sectionTotalB.introductionText}</td>
														</tr>
													</tbody>
												</table>
										</TD>
									</TR>
									<TR>
									
									<!--   TOTAL A SUB-SECTION STARTS        -->
									
										<TD valign="top" width="365">
										<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
											<TR>
												<TD width=4></TD>
												<TD width=145></TD>
												<TD width=10></TD>
												<TD width=115></TD>
												<TD width=6></TD>
												<TD width=80></TD>
												<TD width=6></TD>
											</TR>
											<TR height="20" valign="middle">
												<TD width=4><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												<TD width=145><strong>Description</strong></TD>
												<TD width=10><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												<TD width=115><strong>Method of Payment</strong></TD>
												<TD width=6><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												<TD width=80 align=right><strong>Amount (%)</strong></TD>
												<TD width=5><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
											</tr>
											<c:forEach var="fee"
													items="${recordKeepingCostsummary.sectionTotalA.feeDetails}" varStatus="loop">
											<TR valign="top">
												<TD width=4><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												<TD width=145>${fee.description}</TD>
												<TD width=10><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												<TD width=115>${fee.methodOfPayment}</TD>
												<TD width=6><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
 											    <TD width="80" align="right" valign="bottom" >${fee.amount}</TD>
												<TD width=5><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
											</TR>
											<tr height="2">
												<td colspan="7"><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
											</tr>
											</c:forEach>
										</table>
										</TD>
										<!--  TOTAL A SUB-SECTION ENDS -->
										
										<TD><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
										
										<!--  TOTAL B SUB-SECTION STARTS -->
										
										<TD valign="top" width="365">
										<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
											<TR>
												<TD width=8></TD>
												<TD width=130></TD>
												<TD width=9></TD>
												<TD width=120></TD>
												<TD width=5></TD>
												<TD width=88></TD>
												<TD width=5></TD>
											</TR>
											<TR height="20" valign="middle">
												<TD width=8><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												<TD width=130><strong>Description</strong></TD>
												<TD width=9><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												<TD width=120><strong>Method of Payment</strong></TD>
												<TD width=5><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												<TD width=88 align=right><strong>Amount (%)</strong></TD>
												<TD width=5><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
											</tr>
											<c:forEach var="fee"
													items="${recordKeepingCostsummary.sectionTotalB.feeDetails}" varStatus="loop">
											<TR valign="top">
												<TD width=8><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												<TD width=130>${fee.description}</TD>
												<TD width=9><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												<TD width=120>${fee.methodOfPayment}</TD>
												<TD width=5><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
     										    <TD width=88 align=right valign="bottom" >${fee.amount}</TD>
												<TD width=5><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
											</TR>
											<tr height="2">
												<td colspan="7"><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
											</tr>
											</c:forEach>
										</table>
										</TD>
										<!--  TOTAL B SUB-SECTION ENDS -->
										
									</TR>
									<TR>
                                        <TD valign="bottom" width="50%" align="right"><div style="border-top: 1px solid; width: 40px; height:3px; margin-right:6px ; font-size:0pt;"></div></TD>
                                            <TD></TD>
                                        <TD valign="bottom" width="50%" align="right"><div style="border-top: 1px solid; width: 40px; height:3px; margin-right:5px ; font-size:0pt;"></div></TD>
                                    </TR>
								</TBODY>
							</TABLE>
						</TD>
						<TD class=databorder>
													<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
													</TD>
					</TR>
					</c:if>
					
					<TR class=datacell1>
											<TD class=databorder>
													<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
													</TD>
											<TD vAlign=bottom width="100%"><TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
													<TBODY>
														<TR class=subsubhead>
															<td bgcolor="#DCECF1" valign="middle" align="right" width="4%"><span class="BIG">A:</span></td>
															<TD bgColor="#DCECF1" valign="top" align="right" width="36%" style="padding: 6px;">${recordKeepingCostsummary.sectionTotalA.lableText}</TD>
															<TD bgColor="#DCECF1" valign="bottom" align="right" width="10%" style="padding: 6px;"><B>${recordKeepingCostsummary.sectionTotalA.totalAmount}</B></TD>
															<TD><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
															<td bgcolor="#DCECF1" valign="middle" align="right" width="4%"><span class="BIG">B:</span></td>
															<TD bgColor="#DCECF1" valign="top" width="365" align="right" width="40%" style="padding: 6px;">${recordKeepingCostsummary.sectionTotalB.lableText}</TD>
															<TD bgColor="#DCECF1" valign="bottom" align="right" width="6%" style="padding: 6px"><B>${recordKeepingCostsummary.sectionTotalB.totalAmount}</B></TD>
														</TR>
														<!--  SECTION B ADDENDUM DETAILS STARTS -->
														<c:if test="${recordKeepingCostsummary.preAlignmentIndicator eq false}">
															<c:forEach var="fee"
																						items="${recordKeepingCostsummary.revenueAddendumDetailsSubSection.feeDetails}" varStatus="loop">
																		<tr class="datacell1" >
																			<td width="50%" colspan="4">&nbsp;</td>
																			<td valign="top" align="right" width="4%"></td>
																			<td valign="top" align="right" width="40%" style="padding: 6px 6px 0px 6px;">${fee.description}</td>
									  										<td valign="bottom" align="right" width="6%" style="padding: 6px 6px 0px 6px;" >${fee.amount}</td>
																		</tr>
															</c:forEach>
															<tr class="datacell1">
                                                                            <td width="50%" colspan="4"></td>
                                                                            <td valign="top" align="right" width="3%"></td>
                                                                            <td valign="top" align="right" width="40%"></td>
                                                                            <td valign="bottom" align="right" width="6%" style="padding-right: 6px;"><div style="border-top: 1px solid; width: 40px; height:1px; font-size:0pt;"></div></td>
                                                            </tr>           
															
															<tr class="datacell1">
																			<td width="50%" colspan="4"></td>
																			<td valign="top" align="right" width="3%"></td>
																			<td valign="top" align="right" width="40%" style="padding:0px 6px 6px 6px; ">${recordKeepingCostsummary.revenueAddendumDetailsSubSection.lableText}</td>
																			<td valign="bottom" align="right" width="6%" style="padding:0px 6px 6px 6px; ">${recordKeepingCostsummary.revenueAddendumDetailsSubSection.amount}</td>
																		</tr>			
														</c:if>
														<!--  SECTION B ADDENDUM DETAILS ENDS -->
													</TBODY>
												</TABLE></TD>
											<TD class=databorder>
													<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
													</TD>
										</TR>
					
					<tr>
						<td class="databorder"><img height="1" width="1" src="/assets/unmanaged/images/s.gif"></td>
						<td class="databorder"><img height="1" width="1" src="/assets/unmanaged/images/s.gif"></td>
						<td class="databorder"><img height="1" width="1" src="/assets/unmanaged/images/s.gif"></td>
					</tr>																								
				</TBODY>
			</TABLE>			
			</TD>
		</TR>
		
		<!-- Class Zero Phase 2 Change - Starts -->
		<c:if test="${recordKeepingCostsummary.preClassZeroPhaseTwoInd eq false and recordKeepingCostsummary.dollarFeesDetailsAvailable eq true}">
		<tr>
			<TD width=30>&nbsp;</TD>
			
		</tr>
					
		<TR>
		<TD width=30></TD>
		<TD width=715>
			<TABLE cellSpacing=0 cellPadding=0 width=730 border=0>
				<TBODY>
					<!--  DOLLAR BASED SECTION STARTS -->
					<tr>
						<td class="databorder"><img height="1" width="1" src="/assets/unmanaged/images/s.gif"></td>
						<td class="databorder"><img height="1" width="1" src="/assets/unmanaged/images/s.gif"></td>
						<td class="databorder"><img height="1" width="1" src="/assets/unmanaged/images/s.gif"></td>
					</tr>
					
					<TR bgcolor="#DCECF1">
						<TD class=databorder>
													<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
													</TD>
						<TD class=boxbody width="100%"><TABLE style="table-layout: fixed" border=0 cellSpacing=0 cellPadding=0 width="100%">
								<TBODY>
									<TR>
										<TD width="715">
										<span class="copyBig">${recordKeepingCostsummary.dollarBasedSection.introductionText}
										</span>
										</TD>
									</TR>
								</TBODY>
							</TABLE>
						</TD>
						<TD class=databorder>
													<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
													</TD>
					</TR>
					
					<c:if test="${recordKeepingCostsummary.preAlignmentIndicator eq false}">			
					<TR class=datacell1>
						<TD class=databorder>
													<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
													</TD>
						<TD vAlign=bottom width="100%">
							<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
								<TBODY>
								<TR>
										<!--   SUB HEADING SECTION -->
										<TD valign="top" colspan="100%">
										<table border="0" cellspacing="0" cellpadding="0" width="100%">
													<tbody>
														<tr class="subsubhead">
															<!--<td bgcolor="#e5e5e5" style="padding: 6px;" valign="top" width="50%">${recordKeepingCostsummary.sectionTotalA.introductionText}</td>-->
															<td bgcolor="#CCCCCC" style="padding: 6px;" valign="top" width="100%">
															<span class="copyBig">${recordKeepingCostsummary.dollarBasedSection.lableText}
															</span>
															</td>
														</tr>
													</tbody>
												</table>
										</TD>
									</TR>
									<TR class=datacell1>
									
									<!--   SUB-SECTION STARTS        -->
									
										<TD valign="top" width="100%">
										<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
											<TR>
												<TD width=4></TD>
												<TD width=361></TD>
												<TD width=9></TD>
												<TD width=271></TD>
												<TD width=6></TD>
												<TD width=75></TD>
												<TD width=5></TD>
											</TR>
											<TR height="20" valign="middle">
												<TD width=4><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												<TD width=361><strong>Description</strong></TD>
												<TD width=9><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												<TD width=271><strong>Method of Payment</strong></TD>
												<TD width=6><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												<TD width=75 align=right><strong>Amount ($)</strong></TD>
												<TD width=5><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
											</tr>
											<c:forEach var="fee"
													items="${recordKeepingCostsummary.dollarBasedSection.feeDetails}" varStatus="loop">
											<TR valign="top">
												<TD width=4><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												<TD width=361>${fee.description}</TD>
												<TD width=9><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												<TD width=271>${fee.methodOfPayment}</TD>
												<TD width=6><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
 											    <TD width=75 align="right" valign="bottom" >${fee.amount}${fee.feeFrequency}</TD>
												<TD width=5><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
											</TR>
											<tr height="2">
												<td colspan="7"><IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></td>
											</tr>
											</c:forEach>
										</table>
										</TD>
										<!--  SUB-SECTION ENDS -->
										
									</TR>
								</TBODY>
							</TABLE>
						</TD>
						<TD class=databorder>
													<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
													</TD>
					</TR>
					
					<tr>
						<td class="databorder"><img height="1" width="1" src="/assets/unmanaged/images/s.gif"></td>
						<td class="databorder"><img height="1" width="1" src="/assets/unmanaged/images/s.gif"></td>
						<td class="databorder"><img height="1" width="1" src="/assets/unmanaged/images/s.gif"></td>
					</tr>
					
					</c:if>
																												
				</TBODY>
			</TABLE>			
			</TD>
			</TR>
			
			</c:if>
		<!-- Class Zero Phase 2 Change - Ends -->
	
		<TR>
			<TD width=30>&nbsp;</TD>
			<TD width=715 height=15></TD>
		</TR>
		
		
		<c:if test="${theReport.displayContractCostsFootnote eq true}">
		<content:contentBean type="${contentConstants.TYPE_MISCELLANEOUS}"  
	                                         contentId="${contentConstants.RECORD_KEEPING_CONTRACT_COSTS_FOOTNOTE}"
	                                         beanName="contractCostsFootnote"/>
				<TR>
					<TD width=30>&nbsp;</TD>
					<TD width=735>
					<p><content:getAttribute attribute="text"
						beanName="contractCostsFootnote" /></p>
					<p />
					</TD>
				</TR>
			</c:if>
		
	
		<TR>
			<TD width=30>&nbsp;</TD>
			<TD width=715>
				<TABLE cellSpacing=0 cellPadding=0 width=730 border=0>
					<TBODY>
						<tr>
							<td align="left" padding-left: 2px"><strong>Number of Funds Selected: </strong>${theReport.numberOfFundsSelected}</td>
				
							<c:forEach items="${theReport.investmentData}" var="fundGroup">
								<c:forEach items="${fundGroup.value}" var="fund" varStatus="status">
									<c:if test="${fund.redemptionFee gt 0 }">
										<c:set  scope="page" var="hasRedemptionFee"  value="true" />
									</c:if>
								</c:forEach>
							</c:forEach>
							
							<c:if test="${theReport.contractClassName != ''}">
							
								<td align="right" style="padding-right: 2px"><strong>Class of Funds: </strong>${theReport.contractClassName}</td>
</c:if>
						</tr>	
					</TBODY>
				</TABLE>		
			</TD>
		</TR>	
		
		<TR>
			<TD width=30>&nbsp;</TD>
			<TD width=715></TD>
		</TR>
		<TR>
			<TD width=30>&nbsp;</TD>
			<TD width=715>
	
<TABLE cellSpacing=0 cellPadding=0 width=730 border=0>
				<TBODY>
					<c:if test="${empty param.printFriendly}" >
					<tr>
						<td width="45%" valign="top">
						<strong>Shown</strong>:
<c:if test="${investmentSelectionReportForm.selectedView == 'selected'}">
				        Selected investment options<br>
<c:if test="${investmentSelectionReportForm.showAvailableOptions ==true}">
				        <a href="javascript:doView('available');">View all available investment options</a>
</c:if>
						<p></p>
</c:if>
<c:if test="${investmentSelectionReportForm.selectedView != 'selected'}">
				        All available investment options<br>
				        <a href="javascript:doView('selected');">View selected investment options</a>
</c:if>
				        <p></p>
				        </td>
				      	<td width="3%"><img src="/assets/unmanaged/images/s.gif" width="20" height="1"></td>
                        <td width="52%" align="right">
                        <c:if test="${hasRedemptionFee eq true}">
<c:if test="${environment.siteLocation == SITEMODE_USA}">
<c:if test="${userProfile.currentContract.contractAllocated == true}">
<c:if test="${userProfile.currentContract.nml == false}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_RETAIL}">
			                        	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_RETAIL_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_HYBRID}">
		                            	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRID_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_BROKER}">
		                            	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_BROKER_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_VENTURE}">
		                            	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_VENTURE_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_MULTICLASS}">
		                            	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRID_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
<c:if test="${userProfile.currentContract.nml == true}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_RETAIL}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_RETAILNML_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_HYBRID}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRIDNML_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_BROKER}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRIDNML_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_VENTURE}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_VENTURE_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_MULTICLASS}">
			                                <a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRIDNML_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
</c:if>
<c:if test="${userProfile.currentContract.contractAllocated !=true}">
	                        	<a href="javascript:openPDF('${EARLY_REDEMPTION_HYBRIDNML_USPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>

<c:if test="${envionment.siteLocation == SITEMODE_NY}">
<c:if test="${userProfile.currentContract.contractAllocated == true}">
<c:if test="${userProfile.currentContract.nml == false}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_RETAIL}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_RETAIL_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_HYBRID}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRID_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_BROKER}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_BROKER_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_VENTURE}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_VENTURE_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_MULTICLASS}">
	                                	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRID_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
<c:if test="${userProfile.currentContract.nml ==true}">
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_RETAIL}">
                                            <a href="javascript:PDFWindow('${EARLY_REDEMPTION_RETAILNML_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_HYBRID}">
                                            <a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_BROKER}">
                                            <a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_VENTURE}">
                                            <a href="javascript:PDFWindow('${EARLY_REDEMPTION_VENTURE_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
<c:if test="${userProfile.currentContract.fundPackageSeriesCode == FUND_PACKAGE_MULTICLASS}">
                                            <a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
</c:if>
<c:if test="${userProfile.currentContract.contractAllocated !=true}">
                              	<a href="javascript:PDFWindow('${EARLY_REDEMPTION_HYBRIDNML_NYPDF_URL}')" onMouseOver="self.status='Go to the PDF'; return true">View underlying mutual fund redemption fee information</a>
</c:if>
</c:if>
						</c:if>
                        </td>
					</tr>
					</c:if>
					<c:if test="${not empty param.printFriendly }" >
					<tr>
						<td width="52%" valign="top">
						<strong>Shown</strong>:
<c:if test="${investmentSelectionReportForm.selectedView == 'selected'}">
				        Selected investment options<br>
</c:if>
<c:if test="${investmentSelectionReportForm.selectedView != 'selected'}">
				        All available investment options<br>
</c:if>
				        </td>
					</tr>					
					</c:if>
					
					<tr><td colspan="3" style="padding-left:2px"><content:getAttribute attribute="text" beanName="reportDetailsSummaryContent" /></td></tr>
					<tr><td colspan="3" style="padding-left:2px"><content:getAttribute attribute="text" beanName="feeWaiverDisclosure" /></td></tr>
					<tr><td> <br> </td></tr>
					<c:if test = "${isMerrillContract}">
					<tr><td colspan="3" style="padding-left:2px"><content:getAttribute attribute="text" beanName="restricedFundsText" /></td></tr>
					</c:if>
					<tr><td><img height="25" src="/assets/unmanaged/images/s.gif" width=1></td></tr>
					<TR>
						<TD vAlign=top width="100%" colspan="3">
							
							<!-- table header follows -->
							<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
								<TBODY>
								   
								   <TR class=tablehead>
											<TD class=tableheadTD1 colSpan=20><B>Investment Information and John Hancock's Indirect Compensation</B></TD>
									</TR>
										
									<TR>
									
												<TD colSpan=17>
										
										<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
											<TBODY>
												<TR class=tablesubhead>
													<TD class=databorder rowspan="3"><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD vAlign=center align=left rowspan="3"></TD>
													<TD class=dataheaddivider rowspan="3"><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD vAlign=center align=left rowspan="3" colspan=2></TD>
													<TD class=dataheaddivider rowspan="3"><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD vAlign=center align=middle><B>Investment Services </B></TD>
													<TD class=dataheaddivider><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD vAlign=center align=middle bgColor=#e5e5e5 colspan="9"><B>Plan Services</B></TD>
													<TD class=dataheaddivider rowspan="2"><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD vAlign=bottom align=middle rowspan="2"> </TD>
													<TD class=dataheaddivider rowspan="2"><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD vAlign=bottom align=middle rowspan="3"></TD>
													<TD class=databorder rowspan="3"><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												</TR>
												<TR class=tablesubhead>
													<TD class=dataheaddivider vAlign=bottom colSpan=12><IMG src="/assets/unmanaged/images/s.gif" height=1></TD>
												
												</tr>
												<TR class=tablesubhead>
													<TD vAlign=bottom align=middle><B>(1) </B></TD>
													<TD class=dataheaddivider><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD vAlign=bottom align=middle bgColor=#e5e5e5><B>(2) </B></TD>
													<TD bgColor=#e5e5e5 colspan="3"><B></B></TD>
													<TD vAlign=bottom align=middle bgColor=#e5e5e5><B>(3)</B></TD>
													<TD bgColor=#e5e5e5 colspan="3"> </TD>
													<TD vAlign=bottom align=middle bgColor=#e5e5e5><B>(4) </B></TD>
													<TD class=dataheaddivider><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD vAlign=bottom align=middle><B>(5) </B></TD>
												</TR>
												<TR class=tablesubhead>
													<TD class=databorder><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD vAlign=top align=left><B>Fund code</B></TD>
													<TD class=dataheaddivider><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD vAlign=top align=left colspan=2 ><B>Fund name</B></TD>
													<TD class=dataheaddivider><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD vAlign=top align=middle><B>Underlying Fund Net Cost (%)</B></TD>
													<TD class=dataheaddivider><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD vAlign=top align=middle bgColor=#e5e5e5><B>Revenue From Underlying Fund (%) </B><br>
														(12b-1, STA, Other)</TD>
													<TD vAlign=top align=middle bgColor=#e5e5e5 colspan="3">+</TD>
													<TD vAlign=top align=middle bgColor=#e5e5e5><B>Revenue From <nobr>Sub-account</nobr> (%)</B></TD>
													<TD vAlign=top align=middle bgColor=#e5e5e5 colspan="3">=</TD>
													<TD vAlign=top align=middle bgColor=#e5e5e5><B>Total Revenue Used Towards Plan Cost (%)</B></TD>
													<TD class=dataheaddivider><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD vAlign=top align=middle><B>Expense Ratio (%)</B></TD>
													<TD class=dataheaddivider><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD vAlign=top align=middle><B>Redemption Fee (%)</B><sup>N20</sup></TD>
													<TD class=databorder><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												</TR>
												
												<!-- working up here -->
												<!-- table content section -->
												
												<c:forEach items="${pageScope.theReport.investmentData}" var="fundGroup">
												
													<TR class=tablesubhead>
														<TD class=databorder>
														<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
														
														<TD vAlign=top
																	bgColor=<c:out value="${fundGroup.key.colorcode}"/>
																	colSpan=20>


														<FONT color=<c:out value="${fundGroup.key.fontcolor}"/>>
														<B><c:out value="${fundGroup.key.groupname}" /></B>
														</FONT>
														</TD>
														<TD class=databorder>
														<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>
													    </TR>
													<c:set  scope="page" var="averageRowCount"  value="0" />
													<c:set  scope="page" var="maxCredit"  value="0.15" />
													<c:forEach items="${fundGroup.value}" var="fund" varStatus="status">
														<c:set  scope="page" var="averageRowCount"  value="${status.index}" />
														<c:choose>
														<c:when test="${investmentSelectionReportForm.selectedView eq 'selected'}">
															<c:if test="${fund.fundfeeChanged eq true or fund.redemptionFeeChanged eq true }">
																<c:if test="${status.index % 2 eq 0 }">
																	<TR class=datacell2 style="font-weight: bold;">
																</c:if>
																<c:if test="${status.index % 2 ne 0 }">
																	<TR class=datacell1 style="font-weight: bold;">
																</c:if>
															</c:if>
														
															<c:if test="${fund.fundfeeChanged ne true and fund.redemptionFeeChanged ne true }">
																<c:if test="${status.index % 2 eq 0 }">
																	<TR class=datacell2>
																</c:if>
																<c:if test="${status.index % 2 ne 0 }">
																	<TR class=datacell1>
																</c:if>
															</c:if>
														</c:when>
    													<c:otherwise>
    														<c:choose>
    														<c:when test="${fund.selectedFund eq true}">
    															<c:choose>
		    													<c:when test="${fund.fundfeeChanged eq true or fund.redemptionFeeChanged eq true }">
																	<TR class="selectedFund" style="font-weight: bold;">
																</c:when>
																<c:otherwise>
																	<TR class="selectedFund">
																</c:otherwise>
																</c:choose>
															</c:when>
															<c:otherwise>
																<c:choose>
																<c:when test="${fund.fundfeeChanged ne true and fund.redemptionFeeChanged ne true }">
																	<TR class="datacell1" style="font-weight: bold;">
																</c:when>
																<c:otherwise>
																	<TR class="datacell1">
																</c:otherwise>
																</c:choose>
															</c:otherwise>
															</c:choose>	
    													</c:otherwise>
														</c:choose>

														<TD class=databorder>
														<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1></TD>

														<!--  Fund Id -->
														<TD vAlign=top style="padding-left: 1px">
														<c:out value="${fund.fundId }" />
														</TD>
														<TD class=dataheaddivider vAlign=top>
														<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
														</TD>
														
														<TD vAlign=top class="fwiIndicator" >
															 <c:if  test="${pageScope.theReport.isFeeWaiverFund(fund.fundId)}">
	                                                           <b>&#8226;</b>
	                                                         </c:if>
                       
                                                            <c:if  test="${pageScope.theReport.isFeeWaiverFund(fund.fundId) && pageScope.theReport.isRestrictedFund(fund.fundId)}">
	                                                           </br>
	                                                        </c:if>
	                       
	                                                         <c:if  test="${pageScope.theReport.isFeeWaiverFund(fund.fundId) ne true && pageScope.theReport.isRestrictedFund(fund.fundId) ne true}">
	                                                            &nbsp;&nbsp;
	                                                         </c:if>
	                       
                                                           <c:if  test="${isMerrillContract && pageScope.theReport.isRestrictedFund(fund.fundId)}">
	                                                          <b><c:out value = "${constants.MERRILL_RESRICTED_FUND_SYMBOL}"></c:out></b>
	                                                       </c:if>
														</TD>
														
														<!--  Fund Name -->
														<TD>
														<c:if test="${fund.rateType == SIG_PLUS_RATE_TYPE||fund.rateType == CY1_RATE_TYPE||fund.rateType == CY2_RATE_TYPE}">
														<ps:fundSheetURL productId="${userProfile.currentContract.productId}" 
														fundId="${fund.fundId}"	fundType="${fund.fundType}" rateType="${fund.rateType}" 
														fundSeries ="${userProfile.currentContract.fundPackageSeriesCode}" 
														fundName ="${fund.fundName}" showLink="${requestScope['showLink']}" />
														</c:if>
														<c:if test="${fund.rateType != SIG_PLUS_RATE_TYPE && fund.rateType != CY1_RATE_TYPE && fund.rateType != CY2_RATE_TYPE}">
														<ps:fundSheetURL productId="${userProfile.currentContract.productId}" 
														fundId="${fund.fundId}"	fundType="${fund.fundType}" rateType="${userProfile.currentContract.defaultClass}" 
														fundSeries ="${userProfile.currentContract.fundPackageSeriesCode}" 
														fundName ="${fund.fundName}" showLink="${requestScope['showLink']}" />
														</c:if>
														<c:if test="${not empty fund.footNoteMarkers  }">
															<sup>${fund.footNoteMarkers}</sup>
														</c:if>
														<c:if test="${fund.fundType eq 'GA'}">
														   <c:set  scope="page" var="hasGarunteedFund"  value="true" />
														   <sup>N21</sup>
														</c:if>
														<c:if test="${fund.fundId eq 'MSV' || fund.fundId eq 'NMY'}">
															<c:if test="${fund.fundId eq 'MSV'}">
															   <c:set  scope="page" var="fundIdMsv"  value="true" />
															</c:if>
															<c:if test="${fund.fundId eq 'NMY'}">
															   <c:set  scope="page" var="fundIdNmy"  value="true" />
															</c:if>														
														   		<sup>N19</sup>
														</c:if>
														</TD>
														<TD class=dataheaddivider vAlign=top>
														<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
														</TD>
														
														<!--  Underlying Fund Net Cost -->
														<TD  align=middle>
														<c:if test="${fund.fundType eq 'GA'}">
	                                                            N/A
	                                                    </c:if> 
	                                                    <c:if test="${fund.fundType ne 'GA'}">
															<render:number property="fund.underlyingFundNetCost" defaultValue="0.00" pattern="0.00" />
														</c:if> 
														<BR>
														</TD>
														<TD class=dataheaddivider vAlign=top>
														<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
														</TD>
														
														<!-- Revenue From Underlying Fund -->
														<TD  align=middle>
														<c:if test="${fund.fundType eq 'GA'}">
	                                                           N/A
	                                                    </c:if> 
	                                                    <c:if test="${fund.fundType ne 'GA'}">
															<render:number property="fund.revenueFromUnderlyingFund" defaultValue="0.00" pattern="0.00" />
														</c:if> 
														<BR>
														</TD>
														
														<TD  align=middle colspan="3">+</TD>
														
														<!--  Revenue From Subaccount -->
														<TD  align=middle>
														<c:if test="${fund.fundType eq 'GA'}">
	                                                             N/A
	                                                    </c:if> 
	                                                    <c:if test="${fund.fundType ne 'GA'}">
															<render:number property="fund.revenueFromSubAccount" defaultValue="0.00" pattern="0.00" />
														</c:if> 
														<BR>
														</TD>
														
														<TD align=middle colspan="3">=</TD>

														<!--  Total Revenue -->
														<TD  align=middle>
														<c:if test="${fund.fundType eq 'GA'}">
	                                                             N/A
	                                                    </c:if> 
	                                                    <c:if test="${fund.fundType ne 'GA'}">
	                                                    	<c:if test="${fund.rateType == CY1_RATE_TYPE||fund.rateType == CY2_RATE_TYPE}">
	                                                    		<c:set  scope="page" var="maxCredit"  value="${fund.totalRevenueUsedTowardsPlanCosts}" />
	                                                    	</c:if>
															<render:number property="fund.totalRevenueUsedTowardsPlanCosts" defaultValue="0.00" pattern="0.00" />
														</c:if> 
														<BR>
														</TD>
														<TD class=dataheaddivider vAlign=top>
														<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
														</TD>
														
														<!--  Expense Ratio -->
														<TD	 align=middle>
														<c:if test="${fund.fundType eq 'GA'}">
	                                                            N/A
	                                                     </c:if> 
	                                                     <c:if test="${fund.fundType ne 'GA'}">
															<render:number property="fund.expenseRatio" defaultValue="0.00" pattern="0.00" />
														</c:if> 
														<BR>
														</TD>
														<TD class=dataheaddivider>
														<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
														</TD>

														<!--  Redemption Fee -->
														<TD align=middle>
														<c:if test="${fund.fundType eq 'GA'}">
	                                                            N/A
	                                                     </c:if> 
	                                                     <c:if test="${fund.fundType ne 'GA'}">
														      <render:number property="fund.redemptionFee" defaultValue="0.00" pattern="0.00" /> 
														</c:if>
														<BR>
														</TD>
														<TD class=databorder>
														<IMG height=1 src="/assets/unmanaged/images/s.gif" width=1>
														</TD>

													</TR>

													</c:forEach>
												</c:forEach>
												
												<!--  Averages -->
<c:if test="${not empty theReport.investmentData}">
													<c:choose>
														<c:when test="${investmentSelectionReportForm.selectedView eq 'selected'}">
													<c:if test="${(averageRowCount + 1) % 2 eq 0 }">
														<TR class="datacell2">
													</c:if>
													<c:if test="${(averageRowCount + 1) % 2 ne 0 }">
														<TR class="datacell1">
													</c:if>
													</c:when>
													<c:otherwise>
														<TR class="datacell2">
													</c:otherwise>
													</c:choose>
														<TD class=databorder><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD vAlign=top align="right" colspan="4"><b>Averages:</b><sup>N15</sup></TD>
														<TD class=dataheaddivider><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD vAlign=top align=middle><span class="highlight"><b><render:number property="theReport.averageUnderlyingFundNetCost" defaultValue="0.00" pattern="0.00" /></b></span></TD>
														<TD class=dataheaddivider><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD vAlign=top align=middle><span class="highlight"><b><render:number property="theReport.averageRevenueFromUnderlyingFund" defaultValue="0.00" pattern="0.00" /></b></span></TD>
														<TD vAlign=top align=middle colspan="3">+</TD>
														<TD vAlign=top align=middle><span class="highlight"><b><render:number property="theReport.averageRevenueFromSubAccount" defaultValue="0.00" pattern="0.00" /></b></span><BR></TD>
														<TD vAlign=top align=middle colspan="3">=</TD>
														<TD vAlign=top align=middle><span class="highlight"><b><render:number property="theReport.averageTotalRevenueUsedTowardsPlanCosts" defaultValue="0.00" pattern="0.00" /></b></span></TD>
														<TD class=dataheaddivider><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD vAlign=top align=middle><span class="highlight"><b><render:number property="theReport.averageExpenseRatio" defaultValue="0.00" pattern="0.00" /></b></span></TD>
														<TD class=dataheaddivider><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
														<TD vAlign=top align=middle> <BR></TD>
														<TD class=databorder><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													</TR>	
</c:if>
												<TR>
													<TD class=databorder colSpan=21><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												</TR>

												<!-- border gif files follow -->
												<TR>
													<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD><IMG src="/assets/unmanaged/images/s.gif" width=34 height=1></TD>
													<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD><IMG src="/assets/unmanaged/images/s.gif" width=207 height=1></TD>
													<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD><IMG src="/assets/unmanaged/images/s.gif" width=75 height=1></TD>
													<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD><IMG src="/assets/unmanaged/images/s.gif" width=75 height=1></TD>
													<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD><IMG src="/assets/unmanaged/images/s.gif" width=15 height=1></TD>
													<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD><IMG src="/assets/unmanaged/images/s.gif" width=75 height=1></TD>
													<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD><IMG src="/assets/unmanaged/images/s.gif" width=15 height=1></TD>
													<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD><IMG src="/assets/unmanaged/images/s.gif" width=75 height=1></TD>
													<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD><IMG src="/assets/unmanaged/images/s.gif" width=70 height=1></TD>
													<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
													<TD><IMG src="/assets/unmanaged/images/s.gif" width=75 height=1></TD>
													<TD width=1><IMG src="/assets/unmanaged/images/s.gif" width=1 height=1></TD>
												</TR>
												<TR>
													<TD width="1%"><IMG src="/assets/unmanaged/images/s.gif" height=1></TD>
													<TD colSpan=21><BR>
														<P></P></TD>
												</TR>
											</TBODY>
								</TABLE>
						  </TD>
					</TR>
				</TBODY>
			</TABLE>
			</TD>
		</TR>
	</TBODY>
</TABLE>
</TD>
</TR>
</TBODY>
</TABLE>
</ps:form>

<TABLE height=25 cellSpacing=0 cellPadding=0 width=760 border=0>
	<SCRIPT language=javascript>
<!--
function openWin(url) {
	options="toolbar=1,status=1,menubar=1,scrollbars=1,resizable=1,width=800,height=450,left=10,top=10";
	newwindow=window.open(url, "general", options);
	if (navigator.appName=="Netscape") {
		newwindow.focus();
	}
}

	  
//-->
</SCRIPT>
	<TBODY>
		<TR>
			<TD><BR>
			<TABLE class=fixedTable cellSpacing=0 cellPadding=0 width=765
				border=0>
				<TBODY>
					
					<!-- Footer 1 -->
					<TR>
						<TD width=30>&nbsp;</TD>
						<TD width=735>
					<p/> 
	<c:if test="${investmentSelectionReportForm.svgifFlag ==true}">
	                             <content:getAttribute attribute="text" beanName="svgifDisclosure" />
	</c:if>
<p/>
<c:if test="${investmentSelectionReportForm.classZero ==true}">
                             <content:getAttribute attribute="text" beanName="classZeroFooter" ><content:param> <fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${maxCredit}" />% </content:param></content:getAttribute>
</c:if>
<c:if test="${investmentSelectionReportForm.classZero ==false}">
						     <content:getAttribute attribute="text" beanName="nonClassZeroFooter" /> 
</c:if>
	
					    </TD>
					</TR>
					
					<!-- report Foot Notes Start -->
					<c:forEach items="${recordKeepingCostsummary.orderedFootNotes}" var="footNote">
						<TR>
							<TD width=30>&nbsp;</TD>
							<TD width=735>
							 <p>${footNote}</p>
							 <p/>
							</TD>
						 </TR>
					</c:forEach>
					<!-- report Foot Notes Ends -->
				    
					<TR>
						<TD width=30>&nbsp;</TD>
						<TD width=735>
						<p><ps:fundFootnotes symbols="symbolsArray" /></p>
						</TD>
					</TR>
					<TR>
						<TD width=30>&nbsp;</TD>
						<TD>
						<BR>
						<p class="footnote">
						<content:pageFootnotes beanName="layoutPageBean" /></p>
						</TD>
					</TR>
				</TBODY>
			</TABLE>
			</TD>
		</TR>
	</TBODY>
</TABLE>