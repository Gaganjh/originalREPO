<%@page buffer="none" autoFlush="true" isErrorPage="false" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

								<table id="changeDCScale" border="0" cellSpacing="0" cellPadding="0" width="50%">
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
													<td valign="middle" height="25" align="center" colspan="2" width="20%"><b>Year</b></td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td valign="middle" align="center" height="25" width="40%"><b>Current DI Charge (%)</b></td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td valign="middle" height="25" align="center" colspan="2" width="40%"><b>New DI Charge (%)</b></td>
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												<c:forEach var="currentDiScale" items="${ipiToolForm.currentDiScale}" varStatus="Loop1">
												<c:set var="inputDiScale" value="${ipiToolForm.inputDiScale[Loop1.index]}"/>
												<c:if test="${(empty currentDiScale && currentDiScale != 0 )}">
													<c:set var="newRowId" value="newDi${Loop1.index}"/>
												</c:if>
												<c:if test="${not empty currentDiScale || currentDiScale == 0 || not empty inputDiScale || inputDiScale == 0 }">
												<c:set var="inputDiScaleValue" value="${inputDiScale}"/>		
												<tr class="datacell1" id="${newRowId}">
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td valign="middle" height="25" align="center" colspan="2">${Loop1.index + 1}</td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td valign="middle" align="center" height="25">
														<c:choose> 
															<c:when test="${not empty currentDiScale || currentDiScale == 0}">
																<fmt:formatNumber value="${currentDiScale}" maxFractionDigits="3"/>%
															</c:when>
															<c:otherwise>
																
															</c:otherwise>
														</c:choose>
													</td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td valign="middle" height="25" align="center" colspan="2"><input id="changeDCScale_${Loop1.index}" value="${inputDiScaleValue}" type="text"/></td>
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												</c:if>
												</c:forEach>												
												<tr>
													<td class="databorder" colSpan="9"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
											</tbody>
										</table>
										<br/>
										<table id= "calculateDi" border="0" cellSpacing="0" cellPadding="0" width="50%">
											<tbody>
												<tr>
													<td  align="left">
														<img id="addDCRow" class="icon" src="/assets/unmanaged/images/plus_icon.gif"/>&nbsp;Add row
													</td>
													<td align="right" width="30%">
<input type="button" onclick="return doClearDCValues('${_csrf.token}');" class="button134" value="Reset DI Scale"/>
													</td>
													<td align="right" width="">
<input type="button" onclick="return calculateDC('${_csrf.token}');" class="button134" id="dcButton" value="Calculate DI Scale"/>
													</td>
												</tr>
												<tr>
												<td colspan="3">&nbsp;</td>
												</tr>
										</tbody>
									</table>
	<!--end table content -->
