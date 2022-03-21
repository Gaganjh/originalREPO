<%@page buffer="none" autoFlush="true" isErrorPage="false" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

	<table id="changeBACScale" border="0" cellSpacing="0" cellPadding="0" >
											<tbody>
												<tr>
													<td colSpan="15" height="1"><img border="0" src="/assets/unmanaged/images/spacer.gif" width="30" height="1"></td>
												</tr>
												<tr class="tablehead">
													<td class="tableheadTD1" height="25" vAlign="middle" colSpan="14"> </td>
													<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												<tr>
													<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td class="actions"><img src="/assets/unmanaged/images/s.gif" width="90" height="1"></td>
													<td class="submissionDate"><img src="/assets/unmanaged/images/s.gif" width=20 height="1"></td>
													<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td class="type"><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
													<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td class="type"><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
													<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td class="type"><img src="/assets/unmanaged/images/s.gif" width="100" height="1"></td>
													<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td class="paymentTotal"><img src="/assets/unmanaged/images/s.gif" width=120 height=1></td>
													<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td><img src="/assets/unmanaged/images/s.gif" width="120" height="1"></td>
													<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												<tr class="tablesubhead">
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td vAlign="middle" height="25" align="center" colspan="2"><b>Current Band Starts at</b></td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td valign="middle" align="center" height="25"><b>Current Band Ends at</b></td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td valign="middle" align="center" height="25"><b>New Band <br>Starts at</b></td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td valign="middle" align="center" height="25"><b>New Band <br>Ends at</b></td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td vAlign="middle" height="25" align="center"><b>Current Basic Asset Charge Rate (%)</b></td>
													<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
													<td vAlign="middle" height="25" align="center" colspan="2"><b>New Basic Asset Charge Rate (%)</b></td>
													<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
												<c:forEach var="currentBacScale" items="${ipiToolForm.currentBacScale}" varStatus="Loop1">
													<c:set var="inputBacScale" value="${ipiToolForm.inputBacScale[Loop1.index]}"/>
													<c:set var="BacScale" value="${currentBacScale.bandEnd}"/>
													
											 	<c:if test="${(empty BacScale || BacScale == '0' )}">
														<c:set var="newRowId" value="newBac${Loop1.index}"/>
													</c:if>	  
													  <c:if test="${(not empty BacScale && BacScale != '0' )|| (not empty inputBacScale.bandEnd && inputBacScale.bandEnd != 0 )}">
														<c:choose>
															<c:when test="${inputBacScale.charge == '0'}">
																<c:set var="inputCharge" value=""/>
															</c:when>
															<c:otherwise>
																<c:set var="inputCharge" value="${inputBacScale.charge}"/>
															</c:otherwise>
														</c:choose>
														<c:choose>
															<c:when test="${inputBacScale.bandEnd == '0'}">
																<c:set var="inputBandEnd" value=""/>
															</c:when>
															<c:otherwise>
																<c:set var="inputBandEnd" value="${inputBacScale.bandEnd}"/>
															</c:otherwise>
														</c:choose>
															<tr class="datacell1" id="${newRowId}">
															<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
															<td vAlign="middle" height="25" align="center" colspan="2">
															<div id="cbs_${Loop1.index}">
																	<c:choose>
																		<c:when test="${not empty currentBacScale.bandStart && currentBacScale.bandStart != '0'}">
																		$<fmt:formatNumber type="number" pattern="###,###,###.##" value="${currentBacScale.bandStart}" maxFractionDigits="2"  minFractionDigits="2"/>
																		</c:when>
																		<c:otherwise>
																			
																		</c:otherwise>
																	</c:choose>
															</div></td>
															<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
															<td valign="middle" align="center" height="25">
															<div id="cbe_${Loop1.index}">
																	<c:choose>
																		<c:when test="${not empty BacScale && BacScale != '0'}">
																		$<fmt:formatNumber type="number" pattern="###,###,###.##" value="${BacScale}" maxFractionDigits="2"  minFractionDigits="2"/>
																		</c:when>
																		<c:otherwise>
																			
																		</c:otherwise>
																	</c:choose>
															</div></td>
															<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
															<td valign="middle" align="center" height="25">
																<div id="newband_${Loop1.index}">
																	<c:choose>
																		<c:when test="${Loop1.first}">
																		0.00
																		</c:when>
																		<c:when test="${not empty inputBacScale.bandStart && inputBacScale.bandStart != '0'}">
																			<fmt:formatNumber type="number" pattern="#########.##" value="${inputBacScale.bandStart}"/>
																		</c:when>
																		<c:otherwise></c:otherwise>
																	</c:choose>
																</div>
															</td>
															<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
															<td valign="middle" align="center" height="25">
															<input class="changeBACScale_${Loop1.index}" id="changeBACScale_${Loop1.index}_0" value="${inputBandEnd}" type="text" onblur="onblurevent(this)"/></td>
															<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
															<td vAlign="middle" height="25" align="center">
															<div id="cbac_${Loop1.index}">
																<c:choose>
																		<c:when test="${not empty currentBacScale.charge && currentBacScale.charge != '0'}">
																			<fmt:formatNumber value="${currentBacScale.charge}" maxFractionDigits="6"/>%
																		</c:when>
																		<c:otherwise>
																			
																		</c:otherwise>
																</c:choose>
															</div></td>
															<td class="dataheaddivider" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
															<td vAlign="middle" height="25" align="center" colspan="2"><input value="${inputCharge}" id="changeBACScale_${Loop1.index}_1" type="text"/></td>
															<td class="databorder" height="25"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
														</tr>	
														</c:if>
													</c:forEach>
												<tr id="dataBorder1">
													<td class="databorder" colSpan="15"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
												</tr>
											</tbody>
										</table>
										<br/>
										<table border="0" cellSpacing="0" cellPadding="0" width="700">
											<tbody>
												<tr>
													<td  align="left">
														<img id="addBACRow" class="icon" src="/assets/unmanaged/images/plus_icon.gif"/>&nbsp;Add row
													</td>
													<td align="" width="25%">&nbsp;</td>
													<td align="right">
<input type="button" onclick="return doClearBACValues('${_csrf.token}');" class="button134" value="Reset BAC Scale"/>
													</td>
													<td align="right" width="25%">
<input type="button" onclick="return calculateBAC('${_csrf.token}');" class="button150" id="bacButton" value="Calculate BAC Scale"/>
													</td>
												</tr>
												<tr>
												<td colspan="4">&nbsp;</td>
												</tr>
										</tbody>
									</table>
	<!--end table content -->
