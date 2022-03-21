<%@ taglib uri="/WEB-INF/content-taglib" prefix="content"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="ps"%>

<%@ taglib prefix="un"
	uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>

<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
	UserProfile userProfile = (UserProfile) session.getAttribute(Constants.USERPROFILE_KEY);
	pageContext.setAttribute("userProfile", userProfile, PageContext.PAGE_SCOPE);
%>

<un:useConstants var="constants"
	className="com.manulife.pension.ps.web.Constants" />
<un:useConstants var="contentConstants"
	className="com.manulife.pension.ps.web.content.ContentConstants" />


<content:contentBean
	contentId="${contentConstants.WARNING_DISCARD_CHANGES}"
	type="${contentConstants.TYPE_MESSAGE}" id="warningDiscardChanges" />
	
<content:contentBean
	contentId="${contentConstants.TPA_FUND_AND_PERFORMANCE_LAYOUT_LAYER1}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="Layer1" />

<content:contentBean
	contentId="${contentConstants.FUND_CHECK_LEGEND_TITILE}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="fundCheckTitle" />

<content:contentBean
	contentId="${contentConstants.FUND_CHECK_LEGEND_DESC_STRONG}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	beanName="fundCheckStrong" />

<content:contentBean
	contentId="${contentConstants.FUND_CHECK_LEGEND_DESC_SATSIFACTORY}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	beanName="fundCheckSatisfactory" />

<content:contentBean
	contentId="${contentConstants.FUND_CHECK_LEGEND_DESC_UNSATSIFACTORY}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	beanName="fundCheckUnSatisfactory" />

<content:contentBean
	contentId="${contentConstants.FUND_CHECK_LEGEND_DESC_TO_BE_REMOVED}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	beanName="fundCheckToBeRemoved" />

<content:contentBean
	contentId="${contentConstants.FUND_CHECK_LEGEND_DESC_NA}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="fundCheckNA" />

<content:contentBean
	contentId="${contentConstants.TPA_FUND_INFORMATION_TAB}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="FundInfoTab" />

<content:contentBean
	contentId="${contentConstants.TPA_PRICES_AND_YTD_TAB}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	beanName="pricesAndYTDTab" />

<content:contentBean
	contentId="${contentConstants.PERFORMANCE_DISCLOSURE}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	beanName="PerformanceAndFeesTab" />

<content:contentBean
	contentId="${contentConstants.TPA_STANDARD_DEVIATION_TAB}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	beanName="StandardDeviationTab" />

<content:contentBean
	contentId="${contentConstants.TPA_FUND_CHARACTERISTICS_1_TAB}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="Fundchar1Tab" />

<content:contentBean
	contentId="${contentConstants.TPA_FUND_CHARACTERISTICS_2_TAB}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="Fundchar2Tab" />

<content:contentBean contentId="${contentConstants.TPA_MORNINGSTAR_TAB}"
	type="${contentConstants.TYPE_MISCELLANEOUS}" beanName="MorningstarTab" />

<content:contentBean
	contentId="${contentConstants.TPA_PRICES_AND_YTD_TAB}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	beanName="FundScoreCardTab" />

<content:contentBean
	contentId="${contentConstants.FUND_SCORECARD_DISCLOSURE_TEXT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	beanName="fundScorecardDisclosure" />

<content:contentBean
	contentId="${contentConstants.FEE_WAIVER_DISCLOSURE_TEXT}"
	type="${contentConstants.TYPE_DISCLAIMER}"
	beanName="feeWaiverDisclosureText" />

<content:contentBean
	contentId="${contentConstants.CUSTOM_QUERY_ACKNOWLEDGEMENT}"
	type="${contentConstants.TYPE_MISCELLANEOUS}"
	id="customFilterAcknowledgement" />

<content:contentBean 
	contentId="${contentConstants.TPA_FAP_GENERIC_VIEW_DISCLOSURE}" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	beanName="GenericViewDisclosure"/>

<!--  After navbar -->
<table width="700" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="700" valign="top">
			<%-- table with 3 columns --%>
			<table width="700" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="525"><img src="/assets/unmanaged/images/s.gif"
						width="525" height="1"></td>
					<td width="20"><img src="/assets/unmanaged/images/s.gif"
						width="20" height="1"></td>
					<td width="155"><img src="/assets/unmanaged/images/s.gif"
						width="155" height="1"></td>
				</tr>
				<tr>
					<td colspan="2">
						<table>
							<tr>
								<td><c:if
										test="${not empty layoutBean.getParam('titleImageFallback')}">
										<c:set var="pageTitleFallbackImage"
											value="${layoutBean.getParam('titleImageFallback')}" />
										<img
											src="<content:pageImage type="pageTitle" id="layoutPageBean" path="${pageTitleFallbackImage}"/>"
											alt="${layoutBean.getParam('titleImageAltText')}">
									</c:if> <c:if
										test="${empty layoutBean.getParam('titleImageFallback')}">
										<img
											src="<content:pageImage type="pageTitle" id="layoutPageBean"/>">
									</c:if></td>
								<td valign="bottom" style="FONT-SIZE: 12px; FONT-WEIGHT: bold;">
									as of ${fapForm.asOfDate}</td>
							</tr>
						</table>
					</td>
					<td></td>
				</tr>
				<tr>
					<td valign="top">

						<table width="525" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="5"><img src="/assets/unmanaged/images/s.gif"
									width="5" height="1"></td>
								<td valign="top" width="480">
									<!--Layout/subheader--> <c:if
										test="${not empty layoutPageBean.subHeader}">
										<strong><content:getAttribute
												beanName="layoutPageBean" attribute="subHeader" /></strong>
										<br />
									</c:if> <!--Layout/intro1--> <c:if
										test="${not empty layoutPageBean.introduction1}">
										<content:getAttribute beanName="layoutPageBean"
											attribute="introduction1" />
										<br />
									</c:if> <!--Layout/Body3 (Added with TPA Rewrite) --> <!-- Show the body3 text for select/search contract page -->
									<c:if test="${layoutBean.showSelectContractLink ==false}">
										<c:if test="${layoutPageBean.body3 != ''}">
											<content:getAttribute beanName="layoutPageBean"
												attribute="body3" />
											<br />
										</c:if>
									</c:if> <!--Layout/Intro2--> <c:if
										test="${layoutBean.showSelectContractLink ==true}">
										<c:if test="${not empty layoutPageBean.introduction2}">
											<content:getAttribute beanName="layoutPageBean"
												attribute="introduction2" />
											<br />
										</c:if>
									</c:if>

									<div id="customFilterAcknowledgement" style="display: none">
										<content:getAttribute beanName="customFilterAcknowledgement"
											attribute="text" />
									</div>

								</td>
								<td width="15"><img src="/assets/unmanaged/images/s.gif"
									width="3" height="1"></td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td width="5"><img src="/assets/unmanaged/images/s.gif"
									width="5" height="1"></td>
								<td>
									<div id="content">
										<div id="fundInformation">
											<p>
												<content:getAttribute attribute="text" 	beanName="FundInfoTab" />
											</p>
										</div>

										<div id="pricesAndYTD" style="display: none;">
											<p>
												<content:getAttribute attribute="text" beanName="pricesAndYTDTab" />
											</p>
											<p>
												<content:getAttribute attribute="text" beanName="feeWaiverDisclosureText" />
											</p>
										</div>

										<div id="performanceAndFees" style="display: none;">
											<p>
												<content:getAttribute attribute="text" beanName="PerformanceAndFeesTab" />
											</p>
											<p>
												<content:getAttribute attribute="text" beanName="feeWaiverDisclosureText" />
											</p>
										</div>

										<div id="standardDeviation" style="display: none;">
											<p>
												<content:getAttribute attribute="text"
													beanName="StandardDeviationTab" />
											</p>
										</div>

										<div id="fundCharacteristics1" style="display: none;">
											<p>
												<content:getAttribute attribute="text" beanName="Fundchar1Tab" />
											</p>
										</div>

										<div id="fundCharacteristics2" style="display: none;">
											<p>
												<content:getAttribute attribute="text" beanName="Fundchar2Tab" />
											</p>
										</div>

										<div id="morningstar" style="display: none;">
											<p>
												<content:getAttribute attribute="text" beanName="MorningstarTab" />
											</p>
											<p>
												<content:getAttribute attribute="text" beanName="feeWaiverDisclosureText" />
											</p>
										</div>

										<div id="fundScorecard" style="display: none;">
											<p>
												<content:getAttribute attribute="text" beanName="FundScoreCardTab" />
											</p>
											<p>
												<content:getAttribute attribute="text" beanName="feeWaiverDisclosureText" />
											</p>
											<p>
												<content:getAttribute attribute="text" beanName="fundScorecardDisclosure" />
											</p>
										</div>        
								        <div id="allFundsDisclosure">
								            <p>
								            	<content:getAttribute attribute="text" beanName="GenericViewDisclosure"/>
								            </p>
								        </div>
									</div>
								</td>
								<td width="15"><img src="/assets/unmanaged/images/s.gif"
									width="3" height="1"></td>
								<td>&nbsp;</td>
							</tr>
						</table>
					</td>

					<td valign="top" colspan="2" class="right">
					<img src="/assets/unmanaged/images/s.gif" width="1" height="25"><br/>

						<div style="align: right;">
							<table class="beigeBox" border="0" cellpadding="0"
								cellspacing="0" width="180">
								<tr>
									<td class="beigeBoxTD1">
										<table border="0" cellpadding="0" cellspacing="0" width="100%">
											<tr>
												<td><b><content:getAttribute attribute="name" beanName="Layer1" /></b></td>
											</tr>
											<tr>
												<td><p>
														<content:getAttribute attribute="text" beanName="Layer1" />
													</p></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td align="right"><img
										src="/assets/unmanaged/images/box_lr_corner_E9E2C3.gif"
										width="5" height="5"></td>
								</tr>
							</table>
						</div> <br /></td>
				</tr>
			</table> <br />
		</td>
		<td width="30" valign="top"><br> 
		<img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
	</tr>
</table>
