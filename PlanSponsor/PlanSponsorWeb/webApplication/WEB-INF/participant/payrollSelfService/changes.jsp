<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.participant.payrollSelfService.PayrollSelfServiceChangesForm" %>
<%@ page import="com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangesReportData" %> 
   
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="/WEB-INF/psweb-taglib" prefix="ps" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<link href="/assets/unmanaged/stylesheet/manulife.css" rel="stylesheet" type="text/css" />  
<link href="/assets/unmanaged/stylesheet/plansponsor.css" rel="stylesheet" type="text/css" />
<link href="/assets/unmanaged/stylesheet/buttons.css" rel="stylesheet" type="text/css" /> 
<link href="/assets/unmanaged/stylesheet/psMainTabNav.css" rel="stylesheet" type="text/css" />
<link href="/assets/unmanaged/stylesheet/psTabNav.css" rel="stylesheet" type="text/css" />

<%
	PayrollSelfServiceChangesReportData theReport = (PayrollSelfServiceChangesReportData)request.getAttribute(Constants.REPORT_BEAN);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>
<content:contentBean contentId="<%=ContentConstants.PAYROLL_SELF_SERVICE_CHANGES_TAB_INTRO_TEXT%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="changesTabIntro" />
	
<content:contentBean contentId="<%=ContentConstants.PAYROLL_SELF_SERVICE_CHANGES_TAB_EMPTY_RESULTS%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="changesTabEmptyResults" />
	
<content:contentBean contentId="<%=ContentConstants.PAYROLL_SELF_SERVICE_ALERT_ICON_TEXT%>" 
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" 
	id="alertIconText" />

<ps:form method="POST" modelAttribute="payrollSelfServiceChangesForm" name="payrollSelfServiceChangesForm" action="/do/participant/payrollSelfService">
<input type="hidden" name="task" value="filter"/>
<form:hidden path="sortField"/>
<form:hidden path="sortDirection"/>
<form:hidden path="pageNumber"/>
<p>
	<content:errors scope="request" />
</p>
	<table width="760" border="0" cellspacing="0" cellpadding="0">
              <tbody>
                <tr>
                  <td width="30">&nbsp;</td>
                  <td width="715">
                    <script type="text/javascript">
                      // page sorting and filter scripts go here
                    </script>
                    <p></p>
                    <table width="700">
                      <tbody>
                        <tr>
                          <td width="10"><img width="15" height="1" src="/assets/unmanaged/images/spacer.gif" border="0"></td>
                          <td>&nbsp;</td>
                        </tr>
                      </tbody>
                    </table>
                    <!-- Tab selection -->                   
                      <table width="700" class="tableBorder" style="padding: 1px; background-color: rgb(204, 204, 204);" border="0" cellspacing="0" cellpadding="0">  
                        <tbody>
                          <tr>
                            <td height="16" class="tablesubhead" colspan="14"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                          </tr>
                                           
                          <tr> 
                            <td colspan="14"> 
                              <table width="740" border="0" cellspacing="0" cellpadding="0">
                                <tbody>
                                  <tr>
                                    <td colspan="3" class="filterSearch">
                                      <content:getAttribute beanName="changesTabIntro" attribute="text" />
                                    </td>
                                  </tr>
                                  <tr>
                                    <td width="200" valign="bottom" class="filterSearch" style="padding-left: 4px;">
                                      <span id="label_lastName"><b>Last name</b></span><br> 
                                      <form:input path="lastName" style="inputField" onkeypress="clearSSN(event)" type="text"/>
                                    </td>
                                    <td width="220" valign="bottom" class="filterSearch">
                                      <span id="label_ssn"><b>SSN</b></span><br> 
                                      <form:password path="ssnFirstGroup"  styleClass="inputField" onkeypress="clearName(event);" onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3" value="${payrollSelfServiceChangesForm.ssnFirstGroup}" />
                                      <form:password path="ssnSecondGroup"  styleClass="inputField" onkeypress="clearName(event);" onkeyup="return autoTab(this, 2, event);" size="2" maxlength="2" value="${payrollSelfServiceChangesForm.ssnSecondGroup}" />
                                      <form:input path="ssnThirdGroup"  styleClass="inputField" onkeypress="clearName(event);" onkeyup="return autoTab(this, 4, event);" size="4" maxlength="4" autocomplete="off" />                                 
                                	</td>
                                    <td width="280" valign="bottom" class="filterSearch">
                                      <b>Description </b><br> 
                                      <form:select id="selfServiceTypeId" path="selfServiceType">
										     <form:options items="${payrollSelfServiceChangesForm.selfServiceTypes()}"/>
									  </form:select>
                                    </td>   
                                  </tr>
                                  <tr>
                                    <td width="200" valign="top" class="filterSearch" style="padding-left: 2px;">
                                      <table width="100%" border="0">
                                        <tbody>
                                          <tr>
                                            <td colspan="2"><b>Effective date</b></td>
                                          </tr>
                                          <tr>
                                            <td width="15%"><strong>from:</strong></td>
                                            <td width="85%">
                                              <form:input id="effectiveDateFromId" path="effectiveDateFrom" tabindex="8" onchange="validateMonthDayYearDate(this)" type="text" size="10" maxlength="10" />
                                              <a href="javascript:calFromDate.popup();" tabindex="9"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date"></a>
                                            </td>
                                          </tr>
                                          <tr>
                                            <td></td>
                                            <td>(mm/dd/yyyy)</td>
                                          </tr>
                                        </tbody>
                                      </table>
                                    </td>
                                    <td width="220" valign="top" class="filterSearch">
                                      <table>
                                        <tbody>
                                          <tr>
                                            <td colspan="2">&nbsp;</td>
                                          </tr>
                                          <tr>
                                            <td><strong>to:</strong></td>
                                            <td>
                                              <form:input id="effectiveDateToId" path="effectiveDateTo" tabindex="10" onchange="validateMonthDayYearDate(this)" type="text" value="" size="10" maxlength="10" />
                                              <a href="javascript:calToDate.popup();" tabindex="11"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date"></a>
                                            </td>
                                          </tr>
                                          <tr>
                                            <td></td>
                                            <td>(mm/dd/yyyy)</td>
                                          </tr>
                                        </tbody>
                                      </table>
                                    </td>                                    
                                    <td width="280" valign="middle" class="filterSearch">
                                      <input type="submit" onclick="return doSearch();" tabindex="48" value="search">&nbsp;&nbsp;&nbsp;
                                      <input type="submit"  onclick="return doReset();" tabindex="49" value="reset">
                                    </td> 
                                  </tr>
                                  <tr>
                                    <td colspan="3" class="filterSearch">
                                      <img src="/assets/unmanaged/images/s.gif" width="1" height="5">
                                    </td>
                                  </tr>
                                  <tr class="datacell1">
                                    <td height="24" valign="bottom" colspan="3">
                                      <strong>Legend:&nbsp;</strong>
                                      <img height="12" src="/assets/unmanaged/images/view_icon.gif" width="12" border="0">&nbsp; View &nbsp;
                                      <img height="12" src="/assets/unmanaged/images/edit_icon.gif" width="12" border="0">&nbsp; Edit &nbsp;         
                                    </td>
                                  </tr>
                                </tbody>
                              </table>
                            </td>
                          </tr>
                          <tr>
                            <td colspan="14"> 
                              <table width="740" border="0" cellspacing="0" cellpadding="0">
                                <tbody>
                                  <tr class="tablehead">
                                    <td class="tableheadTD1" width="300">&nbsp;</td>
                                    <td class="tableheadTDinfo" align="left"><b>
                                    <report:recordCounter report="theReport" label="Requests"/></b></td>
                                    <td align="right" class="tableheadTDinfo" style="padding-right: 4px;">
                                     <report:pageCounter formName="payrollSelfServiceChangesForm"  report="theReport"/>
                                    </td>
                                  </tr>
                                </tbody>
                              </table>
                            </td>
                          </tr>
                          <tr class="tablesubhead">
                            <td width="20" align="left" class="" valign="top">
                              &nbsp;
                            </td>
                            <td width="140" align="left" class="name" valign="top" style="padding-left: 4px;">
                              <b><report:sort field="lastName" formName="payrollSelfServiceChangesForm"  direction="asc">Name</report:sort></b>
                            </td>
                            <td width="1" class="dataheaddivider"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                            <td width="144" align="left" class="description" valign="top" style="padding-left: 4px; padding-right: 0px;">
                              <b><report:sort field="description" formName="payrollSelfServiceChangesForm"  direction="asc">Description</report:sort></b> 
                            </td>
                            <td width="1" class="dataheaddivider"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                            <td width="92" align="left" class="initiated" valign="top" style="padding-left: 4px;">
                              <b><report:sort field="initiated" formName="payrollSelfServiceChangesForm"  direction="asc">Initiated</report:sort></b>
                            </td>
                            <td width="1" class="dataheaddivider"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                            <td width="94" align="left" class="effectiveDate" valign="top" style="padding-left: 4px;">
                              <b><report:sort field="effectiveDate" formName="payrollSelfServiceChangesForm"  direction="asc">Effective date</report:sort></b>
                            </td>
                            <td width="1" class="dataheaddivider"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                            <td width="80" align="left" class="details" valign="top" style="padding-left: 4px;">
                              <b>Details</b>
                            </td>
                            <td width="1" class="dataheaddivider"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                            <td width="85" align="left" class="moneyType" valign="top" style="padding-left: 4px;">
                              <b>Money type</b>
                            </td>
                             <td width="1" class="dataheaddivider"><img width="1" height="1" src="/assets/unmanaged/images/s.gif"></td>
                            <td width="50" align="left" class="Alert" valign="top" style="padding-left: 4px;">
                              <b>Alert</b>
                            </td>
                          </tr>
                   	  <c:if test = "${payrollSelfServiceChangesForm.report.details.isEmpty()}" >
						  	 <tr class="datacell1">						  	  
						   		<td colspan="14"> 
						   		  <table border="0" cellspacing="0" cellpadding="5">
                        <tbody>
                            <tr>
                                <td width="700" height="30" class="datacell1" style="padding: 4px;">
                                    <content:getAttribute beanName="changesTabEmptyResults" attribute="text" />
                                </td>
                            </tr>
                        </tbody>
                    </table>
						        	
						       </td>  							 
						    </tr>
						  </c:if>
						  
						  
					<!--  Report data display -->
					<c:forEach items="${payrollSelfServiceChangesForm.getReportPageDetails()}" var="payrollSelfServiceChange" varStatus="loopStatus">
 						<tr class="${loopStatus.index % 2 == 0 ? 'dataCell1' : 'dataCell2'}">
							<td align="center" class="actions">
								<table border="0" cellspacing="0" cellpadding="1">
									<tbody>
										<tr>
											<td> 
												<a href="/do/census/viewEmployeeSnapshot/?profileId=${payrollSelfServiceChange.profileId}&source=payrollSelfService">
												<img src="/assets/unmanaged/images/view_icon.gif" alt="View" border="0"/></a>
											</td>
										</tr>
										<c:if test = "${payrollSelfServiceChangesForm.showEditActionButton(pageContext.request)}" >   							      
											<tr>
												<td>
													<a href="/do/census/editEmployeeSnapshot/?profileId=${payrollSelfServiceChange.profileId}&source=payrollSelfService">
													<img src="/assets/unmanaged/images/edit_icon.gif" alt="Edit" border="0"/></a>
												</td>
											</tr> 
										</c:if>   					   
									</tbody>
								</table>
							</td>
							<td style="padding-left: 4px;">
								<a href="#"><c:out value="${payrollSelfServiceChange.getParticipantName()}"/></a><br>
								<c:out value="${payrollSelfServiceChange.getMaskedSsn()}"/>
							</td>
							<td class="datadivider" valign="top" width="1" height="1"></td>
							<td style="padding-left: 4px;"><c:out value="${payrollSelfServiceChange.getDescription()}"/></td>
							<td class="datadivider" valign="top" width="1" height="1"></td>
							<td style="padding-left: 4px;"><fmt:formatDate value="${payrollSelfServiceChange.getInitiatedDate()}" pattern="MMM dd, yyyy"/></td>
							<td class="datadivider" valign="top" width="1" height="1"></td>
							<td style="padding-left: 4px;"><fmt:formatDate value="${payrollSelfServiceChange.getEffectiveDate()}" pattern="MMM dd, yyyy"/></td>
							<td class="datadivider" valign="top" width="1" height="1"></td>
							<td style="padding-left: 4px;"><c:out value="${payrollSelfServiceChange.getDetails()}" /></td>
							<td class="datadivider" valign="top" width="1" height="1"></td>
							<td style="padding-left: 4px;"><c:out value="${payrollSelfServiceChange.getMoneyTypeDescription()}" /></td>
							<td class="datadivider" valign="top" width="1" height="1"></td>
							<td style="padding-left: 4px;"> 
							<c:if test = "${payrollSelfServiceChange.isWarningIconApplicable()}" > 						
							 <IMG height=16 src="/assets/unmanaged/images/alert.gif" title = "<content:getAttribute beanName="alertIconText" attribute="text" />" width=16 border=0>						
							 </c:if>
							 </td>							 
						</tr>
					</c:forEach>
						<tr>
                        <td colspan="14"> 							
                        </td>
                      </tr>                      
                </tbody>
              </table>
		      <table width="740" border="0" cellspacing="0" cellpadding="0">
				<tbody>
					<tr>
						<td width="20%">&nbsp;</td>
						<td align="center" width="60%"><b><report:recordCounter report="theReport" label="Requests"/></b></td>
						<td align="right" width="20%"><report:pageCounter formName="payrollSelfServiceChangesForm" report="theReport" arrowColor="black"/></td>
					</tr>	
				</tbody>
			</table>			
</ps:form>
<script type="text/javascript">
// create calendar object(s) just after form tag closed.
var calFromDate = new calendar(document.forms['payrollSelfServiceChangesForm'].elements['effectiveDateFrom']);
calFromDate.year_scroll = true;
calFromDate.time_comp = false;
var calToDate = new calendar(document.forms['payrollSelfServiceChangesForm'].elements['effectiveDateTo']);
calToDate.year_scroll = true;
calToDate.time_comp = false;
</script>
