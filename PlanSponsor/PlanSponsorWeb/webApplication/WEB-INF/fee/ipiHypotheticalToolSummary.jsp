<%@page buffer="none" autoFlush="true" isErrorPage="false" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.manulife.pension.ps.web.Constants"%>

<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>


			 
<html>
<head>
<title></title>

<script type="text/javascript" >

$(document).ready(function() {
	var plusIcon = "/assets/unmanaged/images/plus_icon.gif";
	var minusIcon = "/assets/unmanaged/images/minus_icon.gif";
	$('#bacExpandViewImg').attr('src',minusIcon);
	$('#dcExpandViewImg').attr('src',minusIcon);
	$('#bacSectionViewImg').attr('src',plusIcon);
	$('#bacSectionView').hide();
	$('#dcSectionViewImg').attr('src',plusIcon);
	$('#dcSectionView').hide();
	
	$('#bacSectionViewImg').on("click",function() {

			$('#bacSectionView').clicktoggle();
			var newsrc=$(this).attr('src') == plusIcon ? minusIcon : plusIcon ;
			$(this).attr('src',newsrc);
			  
		});
		
	$('#dcSectionViewImg').on("click",function() {

			$('#dcSectionView').clicktoggle();
			var newsrc=$(this).attr('src') == plusIcon ? minusIcon : plusIcon ;
			$(this).attr('src',newsrc);
			  
		});
		
		$('#bacExpandViewImg').on("click",function() {

			$('#bacExpandView').clicktoggle();
			var newsrc=$(this).attr('src') == plusIcon ? minusIcon : plusIcon ;
			$(this).attr('src',newsrc);
			  
		});
		
		$('#dcExpandViewImg').on("click",function() {

			$('#dcExpandView').clicktoggle();
			var newsrc=$(this).attr('src') == plusIcon ? minusIcon : plusIcon ;
			$(this).attr('src',newsrc);
			  
		});
	
});

</script>
</head>
<body>
<c:set var="form" value="${requestScope.ipiTool}"/>
<c:set scope="request" var="isJhSectionDisplayed" value="false"/>
<c:set scope="request" var="isSalesSectionDisplayed" value="false"/>
<c:set scope="request" var="isTrusteeSectionDisplayed" value="false"/>
<c:set scope="request" var="isFeeChanged" value="false"/>
<c:set scope="request" var="isAssetChargeRmNotBL" value="false"/>
<br/><br/>
	<div id = "summary">
		<table border="0" cellSpacing="0" cellPadding="0" width=760>
		<tbody >
		  <tr>
		    <td width=30>&nbsp;</td>
			  <td>
					<table border="0" cellSpacing="0" cellPadding="0" width="700">
						<tbody>
								<tr>
									<td>
										  <!--Retrieve from CMA -->
										<img src="<content:pageImage type="pageTitle" beanName="layoutPageBean"/>" alt="Participant Fee Change Notice Summary">
										<br><br>
									</td>
								</tr>
								<tr>
									<td>
									<!--Layout/Intro1--> 
										<content:pageIntro beanName="layoutPageBean" />
										<br>
										<content:getAttribute beanName="layoutPageBean"
										  attribute="introduction2">

										</content:getAttribute> <br>
										<br>
									</td>
								</tr>
								<tr>
									<td vAlign="top" width="730">
									<table border="0" cellSpacing="0" cellPadding="0" width="730">
											<tbody>
												<tr class="tablehead">
													<td class="tableheadTD1" height="25" vAlign="middle" colspan="9"><b>Contract Overview</b></td>
													<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												<tr class="datacell1">
													<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td colspan="8">
													<table border="0" width="100%" style= "table-layout: fixed;">
															<tbody>
																<tr class="datacell1">
																	<td vAlign="top" width="20%"><b>Contract Number</b></td>
																	<td vAlign="top" width="40%">${userProfile.currentContract.contractNumber}</td>
																	<td vAlign="top" width="20%"><b>Contract Status</b></td>
																	<td vAlign="top" width="20%">${userProfile.currentContract.status}</td>
																</tr>
																<tr class="datacell1">
																	<td vAlign="top" width="20%"><b>Contract Name</b></td>
																	<td vAlign="top" width="40%">${userProfile.currentContract.contractLegalName}</td>
																	<td vAlign="top" width="20%"><b>Effective Date</b></td>
																	<td vAlign="top" width="20%">
																	<c:choose>
																		<c:when test="${userProfile.currentContract.status == 'AC' || userProfile.currentContract.status == 'CF'}">
																			<fmt:formatDate value="${userProfile.currentContract.effectiveDate}" pattern="MMMM dd, yyyy" />
																		</c:when>
																		<c:otherwise>
																			TBD
																		</c:otherwise>
																	</c:choose>
																	</td>
																</tr>
																<tr class="datacell1">
																	<td vAlign="top" width="20%"><b>PIM/CAR Name</b></td>
																	<td vAlign="top" width="40%">${userProfile.currentContract.contractCarName}</td>
																	<td vAlign="top" width="20%"><b>Total Assets</b></td>
																	<td vAlign="top" width="20%">$<fmt:formatNumber type="number" pattern="###,###,###.##" value="${userProfile.currentContract.totalAssets}"/></td>
																</tr>
															</tbody>
														</table></td>
													<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr> 
												<tr class="tablehead">
													<td class="tablehead" height="25" vAlign="middle" colSpan="9"><b>Summary of Changes Requested</b></td>
													<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr> 
												<tr class="tablesubhead">
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td vAlign="top" height="25" align="left" width="40%" colspan="2"></td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td valign="middle" align="center" height="25" width="20%"><b>Current pricing</b></td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td vAlign="middle" height="25" align="center" width="22%"><b>New pricing request</b></td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td vAlign="middle" height="25" align="center" width=""><b>Final pricing</b></td>
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>

												<c:forEach var="ipiTool" items="${form.fieldAttributeValue}"> 
												
												<c:choose>
													<c:when test="${ipiTool.sectionName == 'Summary of New IPI Charges'}">
														<tr class="tablehead">
															<td class="tablehead" height="25" vAlign="middle" colSpan="9"><b>${ipiTool.sectionName}</b></td>
															<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
														</tr>
														<tr class="subsubhead">
															<td class="databorder" height="21"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
															<td colSpan=8>
																<table border=0 width="100%" cellpadding="0" cellspacing="0">
																	<tbody>
																	<tr class="subsubhead">
																		<td vAlign="top" height="21" align="left" width="38%">&nbsp;</td>
																		<td class="dataheaddivider" height="21"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																		<td valign="middle" align="center" height="21" width="31%"><b>Current pricing</b></td>
																		<td class="dataheaddivider" height="21"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																		<td vAlign="middle" height="21" align="center" width="31%"><b>New pricing</b></td>
																	</tr>
																	</tbody>
																</table>
															</td>
															<td class="databorder" height="21"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>	
														</tr>
													</c:when>
													<c:otherwise>
													</c:otherwise>
												</c:choose>
												<c:forEach var="fields" items="${ipiTool.fields}" varStatus="loop"> 
												
												 <c:if test="${ipiTool.sectionName != 'Summary of New IPI Charges' && ipiTool.sectionName== 'Recovery Methods'}">
												<c:if test="${loop.first}">
												<tr class="subsubhead">
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td colspan="8"><b>${ipiTool.sectionName}</b></td>
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												</c:if>
												<!--Allignment changes Req.IPI.104 and IPI.105 - Asset Charges Recovery Method modifications -->
												<c:if test="${fields.formName == 'assetChargeRm' }">
													<c:choose>
														<c:when test="${empty fields.inputValue &&  fields.currentValue != 'BL'}"> <!-- if No input selected by user, check current Value -->
															<c:set scope="request" var="isAssetChargeRmNotBL" value="true"/>
														</c:when>
														<c:when test="${not empty fields.inputValue &&  fields.inputValue != 'BL'}" > <!-- if an input is selected by user, check input Value -->
															<c:choose>
																<c:when test="${fields.currentValue != fields.inputValue}"> <!-- if value is changed from BL to DD -->
																	<c:set scope="request" var="isFeeChanged" value="true"/>
																</c:when>
																<c:otherwise> <!-- if value is changed to DD to DD -->
																	<c:set scope="request" var="isAssetChargeRmNotBL" value="true"/>
																</c:otherwise>
															</c:choose>
														</c:when>
													</c:choose>
												</c:if>
												
												<tr class="datacell1">
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td vAlign="middle" height="25" align="left" colspan="2">${fields.labelText}</td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td valign="middle" align="center" height="25">
														${fields.currentValue}
													</td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td valign="middle" align="center" height="25">
													<c:choose>
															<c:when test='${not empty fields.inputValue}'>
																${e:forHtmlContent(fields.inputValue)}
															</c:when>
															<c:otherwise>
																- 
															</c:otherwise>
													</c:choose>
													</td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td valign="middle" align="center" height="25">
													<c:choose>
															<c:when test='${not empty fields.inputValue}'>
																${e:forHtmlContent(fields.inputValue)}
															</c:when>
															<c:otherwise>
																${fields.currentValue}
														    </c:otherwise>
													</c:choose>
													</td>
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												</c:if>
												 <c:if test="${ipiTool.sectionName != 'Summary of New IPI Charges' && ipiTool.sectionName != 'Recovery Methods'}">
												<fmt:formatNumber var ="currentValue" value="${fields.currentValue}"/>
												<fmt:formatNumber var ="inputValue" value="${fields.inputValue}"/>
												<c:if test="${currentValue != '0' || not empty inputValue && inputValue != '0'}">
												  <c:if test="${ipiTool.sectionName == 'John Hancock Charges' && !isJhSectionDisplayed}">
												<c:set scope="request" var="isJhSectionDisplayed" value="true"/>
												<tr class="subsubhead">
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td colspan="8"><b>${ipiTool.sectionName}</b></td>
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												</c:if>  
												<c:if test="${ipiTool.sectionName == 'Sales & Service Charges' && !isSalesSectionDisplayed}">
												<c:set scope="request" var="isSalesSectionDisplayed" value="true"/>
												<tr class="subsubhead">
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td colspan="8"><b>${ipiTool.sectionName}</b></td>
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												</c:if>
												<c:if test="${ipiTool.sectionName == 'Trustee Charges' && !isTrusteeSectionDisplayed}">
												<c:set scope="request" var="isTrusteeSectionDisplayed" value="true"/>
												<tr class="subsubhead">
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td colspan="8"><b>${ipiTool.sectionName}</b></td>
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												</c:if>
												<c:choose>
															<c:when test="${fields.formName == 'dioDiscount'}">
																	<tr style="display:none">
															</c:when>
															<c:otherwise>
																		<tr class="datacell1">												
															</c:otherwise>
												</c:choose>	
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td vAlign="middle" height="25" align="left" colspan="2">${fields.labelText}</td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td valign="middle" align="center" height="25">
															<c:choose>
																<c:when test="${fields.unit == '$'}">
																	${fields.unit}<fmt:formatNumber type="number" pattern="###,###,###.##" value="${fields.currentValue}" maxFractionDigits="${fields.scale}"  minFractionDigits="${fields.scale}" var="formatCurrentField"/>${formatCurrentField}
																</c:when>
																<c:otherwise>
																	<fmt:formatNumber value="${fields.currentValue}" maxFractionDigits="${fields.scale}" var="formatCurrentField"/>${formatCurrentField}${fields.unit}
																</c:otherwise>
															</c:choose>
													</td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td vAlign="middle" height="25" align="center">
														<c:choose>
															<c:when test="${fields.unit == '$' &&  not empty fields.inputValue}">
																${fields.unit}<fmt:formatNumber type="number" pattern="###,###,###.##" value="${fields.inputValue}" maxFractionDigits="${fields.scale}"  minFractionDigits="${fields.scale}" var="formatInputField"/>${formatInputField}
															</c:when>
															<c:when test="${fields.unit == '%' && not empty fields.inputValue}">
																<fmt:formatNumber value="${fields.inputValue}" maxFractionDigits="${fields.scale}" var="formatInputField"/>${formatInputField}${fields.unit}
															</c:when>
															<c:otherwise> - </c:otherwise>
														</c:choose>
													</td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td vAlign="middle" height="25" align="center">
														<c:choose>
															<c:when test="${fields.unit == '$'}">
																<c:choose>
																	<c:when test="${not empty fields.inputValue}">
																		${fields.unit}<fmt:formatNumber  type="number" pattern="###,###,###.##" value="${fields.inputValue}" maxFractionDigits="${fields.scale}"  minFractionDigits="${fields.scale}"/>
																	</c:when>
																	<c:otherwise>
																		${fields.unit}<fmt:formatNumber type="number" pattern="###,###,###.##" value="${fields.currentValue}" maxFractionDigits="${fields.scale}"  minFractionDigits="${fields.scale}"/>
																	</c:otherwise>
																</c:choose>
															</c:when>
															<c:otherwise>
															<c:choose>
																	<c:when test="${not empty fields.inputValue}">
																		<fmt:formatNumber value="${fields.inputValue}" maxFractionDigits="${fields.scale}"/>${fields.unit}
																	</c:when>
																	<c:otherwise>
																		<fmt:formatNumber value="${fields.currentValue}" maxFractionDigits="${fields.scale}"/>${fields.unit}           
																	</c:otherwise>
															</c:choose>
															</c:otherwise>
														</c:choose>
													</td>
													<!--Allignment changes Req.IPI.104 and IPI.105 - MVE modifications -->
													<c:if test="${isAssetChargeRmNotBL && fields.formName == 'marketValueEqualzer' &&  not empty fields.inputValue && formatCurrentField != formatInputField  }">
														<c:set scope="request" var="isFeeChanged" value="true"/>
													</c:if>
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												</c:if>
												</c:if>  
											 	<c:if test="${loop.first && ipiTool.sectionName== 'John Hancock Charges' && (currentValue != 0 || not empty inputValue && inputValue != 0)}">
												<tr class="datacell1" valign="top">
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"> </td>
													<td colspan="8">
														<c:choose>
															<c:when test="${not empty fields.inputValue || fields.inputValue == '0' }">
																<div>
																	<img id="bacExpandViewImg" class="icon">&nbsp;BAC Scale
																</div> 
																<div id="bacExpandView">
															</c:when>
															<c:otherwise>
																<div>
																	<img id="bacSectionViewImg" class="icon">&nbsp;BAC Scale
																</div>
																<div id="bacSectionView">
															</c:otherwise>
														</c:choose>
														<table id="changeDCScale" border="0" cellSpacing="0" cellPadding="0" width="60%">
															<tbody>
																<tr>
																	<td colSpan="9" height="1"><img border="0" src="/assets/unmanaged/images/spacer.gif" width="30" height="1"></td>
																</tr>
																<tr class="tablehead">
																	<td class="tableheadTD1" height="25" vAlign="middle" colSpan="8"> </td>
																	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																</tr>
																<tr class="tablesubhead">
																	<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																	<td valign="middle" height="25" align="center" colspan="2" width="33%"><b>Band Starts at</b></td>
																	<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																	<td valign="middle" align="center" height="25" width="33%"><b>Band Ends at</b></td>
																	<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																	<td valign="middle" height="25" align="center" colspan="2" width="34%"><b>Asset Charge Rate (%)</b></td>
																	<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																</tr>
																<c:set scope="request" var="isMaxBandEnd" value="false" />
																<c:set scope="request" var="isChargeChanged" value="false" />
																<c:set scope="request" var="isMaxCurrentBandEnd" value="false" />
																<c:set scope="request" var="isBandChanged" value="false" />
																<c:forEach var="currentBacScale" items="${form.currentBacScale}" varStatus="Loop1">
																	<c:set var="inputBacScale" value="${form.inputBacScale[Loop1.index]}"/>
																	<c:set var="inputBacScale" value="${form.inputBacScale[Loop1.index]}"/>
																	<c:if test="${Loop1.first}"}>
																			<c:if test="${not empty inputBacScale.bandEnd && inputBacScale.bandEnd != '0' }">
																				<c:set scope="request" var="isBandChanged" value="true" />
																			</c:if>
																			<c:if  test="${not empty inputBacScale.charge && inputBacScale.charge != '0' }">
																				<c:set scope="request" var="isChargeChanged" value="true" />
																			</c:if>
																	</c:if>
																	<c:if test="${isBandChanged && inputBacScale.bandEnd == '0'}"}>
																		<c:set scope="request" var="isMaxBandEnd" value="true" />
																	</c:if>
																		<tr class="datacell1" id="${newRowId}">
																		<c:choose>
																		<c:when test="${isBandChanged && !isMaxBandEnd}">
																				<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																				<td valign="middle" height="25" align="center" colspan="2">
																				<c:choose>
																					<c:when test="${Loop1.first}">
																						$0.00
																					</c:when>
																					<c:otherwise>
																						$<fmt:formatNumber type="number" pattern="###,###,###.##" value="${inputBacScale.bandStart}" maxFractionDigits="2"  minFractionDigits="2"/>
																					</c:otherwise>
																				</c:choose>
																				</td>
																				<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																				<td valign="middle" align="center" height="25">
																						$<fmt:formatNumber type="number" pattern="###,###,###.##" value="${inputBacScale.bandEnd}" maxFractionDigits="2"  minFractionDigits="2"/>
																				</td>
																			</c:when>
																			<c:when test="${!isBandChanged && !isMaxCurrentBandEnd}">
																			<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																				<td valign="middle" height="25" align="center" colspan="2">
																				<c:choose>
																					<c:when test="${Loop1.first}">
																						$0.00
																					</c:when>
																					<c:otherwise>
																						$<fmt:formatNumber type="number" pattern="###,###,###.##" value="${currentBacScale.bandStart}" maxFractionDigits="2"  minFractionDigits="2"/>
																					</c:otherwise>
																				</c:choose>
																				</td>
																				<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																				<td valign="middle" align="center" height="25">
																						$<fmt:formatNumber type="number" pattern="###,###,###.##" value="${currentBacScale.bandEnd}" maxFractionDigits="2"  minFractionDigits="2"/>
																				</td>
																			</c:when>
																			</c:choose>
																			<c:choose>
																			<c:when test="${isChargeChanged && isBandChanged && !isMaxBandEnd}">
																				<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																				<td valign="middle" height="25" align="center" colspan="2">
																				<c:choose>
																					<c:when test="${not empty inputBacScale.charge}">
																						<fmt:formatNumber value="${inputBacScale.charge}" maxFractionDigits="3"/>%
																					</c:when>
																					<c:otherwise>
																						0%
																					</c:otherwise>
																				</c:choose>
																				</td>
																				<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																			</c:when>
																			<c:when test="${isChargeChanged && !isBandChanged && not empty currentBacScale.bandEnd && currentBacScale.bandEnd != '0' }">
																				<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																				<td valign="middle" height="25" align="center" colspan="2">
																				<c:choose>
																					<c:when test="${not empty inputBacScale.charge}">
																						<fmt:formatNumber value="${inputBacScale.charge}" maxFractionDigits="3"/>%
																					</c:when>
																					<c:otherwise>
																						0%
																					</c:otherwise>
																				</c:choose>
																				</td>
																				<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																			</c:when>
																			<c:when test="${!isChargeChanged && isBandChanged && !isMaxBandEnd }">
																				<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																				<td valign="middle" height="25" align="center" colspan="2">
																				<c:choose>
																					<c:when test="${not empty currentBacScale.charge}">
																						<fmt:formatNumber value="${currentBacScale.charge}" maxFractionDigits="3"/>%
																					</c:when>
																					<c:otherwise>
																						0%
																					</c:otherwise>
																				</c:choose>
																				</td>
																				<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																			</c:when>
																			<c:when test="${!isChargeChanged && !isBandChanged && not empty currentBacScale.bandEnd && currentBacScale.bandEnd != '0' }">
																				<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																				<td valign="middle" height="25" align="center" colspan="2">
																				<c:choose>
																					<c:when test="${not empty currentBacScale.charge}">
																						<fmt:formatNumber value="${currentBacScale.charge}" maxFractionDigits="3"/>%
																					</c:when>
																					<c:otherwise>
																						0%
																					</c:otherwise>
																				</c:choose>
																				</td>
																				<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																			</c:when>
																		</c:choose>
																		</tr>
																		<c:if test="${currentBacScale.bandEnd == '999999999.99'}"}>
																			<c:set scope="request" var="isMaxCurrentBandEnd" value="true" />
																		</c:if>
																</c:forEach>												
																<tr>
																	<td class="databorder" colSpan="9"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																</tr>
															</tbody>
														</table>
														<br/>
														</div>	
													</td>
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"> </td>
												</tr>
												</c:if>  
												  <c:if test="${loop.last && ipiTool.sectionName== 'John Hancock Charges' && (currentValue != '0' || not empty inputValue && inputValue != '0')}">
												<tr class="datacell1" valign="top">
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td colspan="8">
													
														<c:choose>
															<c:when test="${not empty ipiToolForm.discontinuanceFee || ipiToolForm.discontinuanceFee == '0' }">
																<div>
																	<img id="dcExpandViewImg" class="icon">&nbsp;Discontinuance Charge Scale
																</div>	
																<div id="dcExpandView">
															</c:when>
															<c:otherwise>
																<div>
																	<img id="dcSectionViewImg" class="icon">&nbsp;Discontinuance Charge Scale
																</div>	
																<div id="dcSectionView">
															</c:otherwise>
														</c:choose>
														<table id="changeDCScale" border="0" cellSpacing="0" cellPadding="0" width="250">
														<tbody>
															<tr>
																<td colSpan="7" height="1"><img border="0" src="/assets/unmanaged/images/spacer.gif" width="" height="1"></td>
															</tr>
															<tr class="tablehead">
																<td class="tableheadTD1" height="25" colSpan="6"> </td>
																<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
															</tr>
															<tr class="tablesubhead">
																<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																<td valign="middle" height="25" align="center" colspan="2" width="50%"><b>Year</b></td>
																<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																<td valign="middle" height="25" align="center" colspan="2" width="50%"><b>DI Charge (%)</b></td>
																<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
															</tr>
																<c:forEach var="currentDiScale" items="${form.currentDiScale}" varStatus="Loop2">
																	<c:set var="inputDiScale" value="${form.inputDiScale[Loop2.index]}"/>	
																	<c:if test="${not empty inputDiScale || inputDiScale == '0'}">
																	<c:set scope="request" var="isChanged" value="true" />
																	</c:if>
																	<c:if test="${not empty currentDiScale || currentDiScale == '0' || not empty inputDiScale || inputDiScale == '0' }">
																		<tr class="datacell1">
																			<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																			<td valign="middle" height="25" align="center" colspan="2">${Loop2.index + 1}</td>
																			<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																			<td valign="middle" height="25" align="center" colspan="2">
																				<c:choose>
																					<c:when test="${not empty inputDiScale || inputDiScale == '0'}">
																						<fmt:formatNumber value="${inputDiScale}" maxFractionDigits="6"/>%
																					</c:when>
																					<c:when test="${empty inputDiScale && isChanged}">
																						0%
																					</c:when>
																					<c:otherwise>
																						<fmt:formatNumber value="${currentDiScale}" maxFractionDigits="6"/>%
																					</c:otherwise>
																				</c:choose>
																			</td>
																			<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																		</tr>
																	</c:if>
																</c:forEach>
															<tr>
																<td class="databorder" colSpan="7"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
															</tr>
														</tbody>
													</table>	
													<br/>															
													</div>
													</td>
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												</c:if>
												<c:if test="${ipiTool.sectionName== 'Summary of New IPI Charges'}">
												<tr class="datacell1">
													<td class="databorder" height="21"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td colSpan="8">
														<table border="0" width="100%" cellpadding="0" cellspacing="0">
															<tbody>
															<tr class="datacell1">
																<td vAlign="middle" height="21" align="left" width="38%">${fields.labelText}</td>
																<td class="dataheaddivider" height="21"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																<td valign="middle" align="center" height="21" width="31%">
																<c:if test="${!isFeeChanged && fields.currentValue != fields.inputValue }">
																	<c:set scope="request" var="isFeeChanged" value="true"/>
																</c:if>
																	<c:choose>
																		<c:when test="${fields.unit == '$'}">
																		${fields.unit}<fmt:formatNumber type="number" pattern="###,###,###.##" value="${fields.currentValue}" maxFractionDigits="${fields.scale}"  minFractionDigits="${fields.scale}"/>
																		</c:when>
																		<c:otherwise>
																		<fmt:formatNumber value="${fields.currentValue}" maxFractionDigits="${fields.scale}"/>${fields.unit}
																		</c:otherwise>
																	</c:choose>
																</td>
																<td class="dataheaddivider" height="21"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
																<td vAlign="middle" height="21" align="center" width="31%">
																<c:choose>
																	<c:when test="${fields.unit == '$'}">
																		${fields.unit}<fmt:formatNumber type="number" pattern="###,###,###.##" value="${fields.inputValue}" maxFractionDigits="${fields.scale}"  minFractionDigits="${fields.scale}"/>
																	</c:when>
																	<c:when test="${fields.unit == '%'}">
																		<fmt:formatNumber value="${fields.inputValue}" maxFractionDigits="${fields.scale}"/>${fields.unit}
																	</c:when>
																</c:choose>
																</td>
															</tr>
															</tbody>
														</table>
													</td>
													<td class="databorder" height="21"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												</c:if> 
												</c:forEach>
												</c:forEach>
												<tr>
													<td class="databorder" colSpan="10"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												<tr>
													<td colSpan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td class="dark_grey_color" vAlign="middle" colSpan="4"></td>
													<td class="dark_grey_color" colSpan="2" align="right"></td>
													<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
											</tbody>
										</table>
									</td>
								</tr>
							</tbody>
						</table>
						<br/>	
						
						<table border="0" cellSpacing="0" cellPadding="0" width="700">
							<tr>
							<ps:form id="previous_Action" action="/do/fee/hypotheticalTool/?action=previous" modelAttribute="ipiToolForm" name="ipiToolForm">
<input type="hidden" name="myAction" id="goPrevious" value="previous" /> 
							<td width="40%">
<input type="submit" class="button134" value="Previous"/>
							</td>
							</ps:form>
							<ps:form action="/do/fee/hypotheticalTool/" modelAttribute="ipiToolForm" name="ipiToolForm">
<input type="hidden" name="myAction" id="generateCSV" value="generate" />  
							<td align="right">
							<c:choose>
								<c:when test="${isFeeChanged || requestScope.isDiFeeChanged}">
<input type="submit" class="button200" value="Create PPT Change Notice"/>
								</c:when>
								<c:otherwise>
<input type="submit" class="button134" value="Create CSV"/>
								</c:otherwise>
							</c:choose>
							</td>
							</ps:form>
							</tr>
						</table>
			 	 </td>
	     	</tr>
			<tr>
				<td width=30>
					&nbsp;
				</td>
			   <td>
				 <p><content:pageFooter beanName="layoutPageBean"/></p>
				 <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
				 <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
			   </td>
			</tr>
		</tbody>
	</table>
	</div>
</body>
</html>
