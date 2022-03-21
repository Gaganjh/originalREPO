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
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.manulife.pension.platform.web.taglib.util.LabelValueBean" %>

<%@ page import="com.manulife.pension.service.contract.valueobject.MoneyTypeVO" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.SubmissionParticipant" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.MoneyTypeHeader" %>
<%@ page import="com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.ContributionDetailItem" %>
<%@ page import="com.manulife.pension.ps.web.tools.EditContributionDetailsForm" %>
<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>

<c:set var="reportBean" value="${editContributionDetailsForm.theReport}" scope="page" />
<c:set var="submissionItem" value="${reportBean.contributionData}" />
<%
ContributionDetailsReportData reportBean = (ContributionDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",reportBean,PageContext.PAGE_SCOPE);
EditContributionDetailsForm editContributionDetailsForm = (EditContributionDetailsForm)session.getAttribute("editContributionDetailsForm");
pageContext.setAttribute("editContributionDetailsForm",editContributionDetailsForm,PageContext.PAGE_SCOPE);
ContributionDetailItem submissionItem = reportBean.getContributionData();
pageContext.setAttribute("submissionItem",submissionItem,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>
<c:set var="contractMoneyTypes" value="${editContributionDetailsForm.contractMoneyTypes}" />

<%-- START new contribution details table --%>
<a name="participantTable"/>
	<table width="500" border="0" cellpadding="0" cellspacing="0">
			<tr class="whiteborder">
			  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td width="500"><img src="/assets/unmanaged/images/s.gif" height="1" /></td>
			  <td width="4"><img src="/assets/unmanaged/images/s.gif" width="4" height="1" /></td>
			  <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>

			<tr class="tablehead" height="25">
				<td class="tableheadTD1" colspan="4">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					  <tr>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()) { %>
						<td class="tableheadTD"><b>Submission details</b></td>
						<td align="right" class="tableheadTDinfo">
                            <report:pageCounterViaSubmit formName="editContributionDetailsForm" report="reportBean" arrowColor="white"/>
                        </td>
<% } else { %>					  
						<td class="tableheadTD"><b><content:getAttribute beanName="layoutPageBean" attribute="body3Header"/></b>
						<b><report:recordCounter report="reportBean" label=" - Total records "/></b>
					    <c:if test="${empty param.printFriendly }" >
						<td align="right" class="tableheadTDinfo">
                            <ps:setReportPageSize editContribution="true"/>
                        </td>
                        </c:if>
					    <c:if test="${not empty param.printFriendly }" >
						<td class="tableheadTDinfo">&nbsp;</td>
					    </c:if>
						<td align="right" class="tableheadTDinfo">
                            <report:pageCounterViaSubmit formName="editContributionDetailsForm" report="reportBean" arrowColor="white"/>
                        </td>
<% } %>                        
					  </tr>
					</table>
				</td>
			</tr>

			<tr class="datacell1">
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td colspan="2" class="whiteborder">
				<table witdh="100%" border="0" cellpadding="0" cellspacing="0">


						<%-- Start Header Row One --%>
						<tr class="datacell1">
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>            		  																																													
							<td width="35">&nbsp;</td>
							<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="35">&nbsp;</td>
							<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
<% } %>							
							<td width="200">&nbsp;</td>
							<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="85">&nbsp;</td>
							<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>            		  																																	
							<td align="right" width="85"><b>Participant totals ($)</b></td>
<% } else { %>
					        <td align="right" width="85"><b>Submission Totals ($)</b></td>							
<% } %>							
<c:forEach items="${submissionItem.allocationMoneyTypes}" var="moneyTypeHeader">
								<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
<c:set var="moneyType" value="${moneyTypeHeader.moneyType}"/>
								<td align="right" width="85">
									<%
									boolean hasMatch = false;
									for (Iterator it = ((Collection)pageContext.getAttribute("contractMoneyTypes")).iterator(); it.hasNext() && ! hasMatch; ) {
										LabelValueBean bean = (LabelValueBean)it.next();
										if (((MoneyTypeVO)pageContext.getAttribute("moneyType")).getContractShortName().equals(bean.getLabel())) {
											hasMatch = true;
										}
									}
									if (!hasMatch) {
									%>
										<report:errorIcon errors="<%=reportBean%>" codes="AT,MT,IM,MN"/>
									<%
									}
									%>
									<%if (reportBean.getDetails() != null && reportBean.getDetails().size() > 0) {%>
<b title="${moneyType.contractLongName}">${moneyType.contractShortName}</b>
									<% } else { %>
										&nbsp;
									<% } %>
								</td>
</c:forEach>
<c:if test="${editContributionDetailsForm.showLoans ==true}">
							<%
								for (int i = 0; i < submissionItem.getMaximumNumberOfLoans(); i++)
								{
							%>
								  <td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
								  <td align="center" width="85"><strong>Loan ID</strong></td>
								  <td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
								  <td align="right" width="85"><strong>Loan Amount</strong></td>
							<%
								}
							%>
</c:if>
						</tr>
						<%-- End Header Row One --%>


						<%-- Start Header Row Two --%>
						<% Map allocationTotalValues = submissionItem.getAllocationTotalValues(); %>
						<tr class="datacell2">
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>            		  																																													
							<td class="datacell1" width="35">&nbsp;</td>
							<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td class="datacell1" width="35">&nbsp;</td>
							<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
<% } %>							
							<td class="datacell1" width="200">&nbsp;</td>
							<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="85"><b>Totals</b></td>
							<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td align="right" width="85"><b><span class="highlight" id="participantFieldsGrandTotal"><render:number property="submissionItem.participantTotal" type="c"/></span></b></td>
<c:forEach items="${submissionItem.allocationMoneyTypes}" var="moneyType" varStatus="typeIndex" >
<c:set var="moneyTypeValue" value="${moneyType}"/>
								<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
								<td align="right" width="85">
<b><span class="highlight" id="moneyFieldsColTotal${typeIndex.index}">
									<%
										java.math.BigDecimal typeTotal = (java.math.BigDecimal) allocationTotalValues.get(((MoneyTypeHeader)pageContext.getAttribute("moneyTypeValue")).getKey());
										if(typeTotal!=null)
											pageContext.setAttribute("typeTotal", typeTotal, PageContext.PAGE_SCOPE);
										else
											pageContext.removeAttribute("typeTotal", PageContext.PAGE_SCOPE);
									%>
									<c:if test="${not empty typeTotal}">
										<render:number property="typeTotal" type="c"/>
</c:if>
									<c:if test="${empty typeTotal}">
										$0.00
</c:if>
									</span></b>
								</td>
</c:forEach>
<c:if test="${editContributionDetailsForm.showLoans ==true}">
<c:forEach items="${submissionItem.loanTotalValues}" var="loanTotal" varStatus="typeIndex" >
								  <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
								  <td align="right" width="85">&nbsp;</td>
								  <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
<td align="right" width="85"><b><span class="highlight" id="loanFieldsColTotal${typeIndex.index}"><render:number property="loanTotal" type="c"/></span></b></td>
</c:forEach>
</c:if>
						</tr>
						<%-- End Header Row Two --%>


						<%-- Start Header Row Three --%>
						<tr class="datacell1">
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>            		  																																													
							<td width="35"><a name="details"></a>&nbsp;</td>
							<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="35">&nbsp;</td>
							<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="200"><a href="#details" onclick="return saveAndForward('prepareAddParticipant');">Add Participant</a></td>
<% } else { %>							
							<td width="200"></td>							
<% } %>
							<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="85">&nbsp;</td>
							<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td align="right" width="85">&nbsp;</td>
<c:forEach items="${submissionItem.allocationMoneyTypes}" var="moneyTypeHeader" varStatus="typeIndex">
									<c:if test="${typeIndex.index == 0}">
										<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
										<td align="right" width="85">
										<%if (reportBean.getDetails() != null && reportBean.getDetails().size() > 0
												&& ((Collection)pageContext.getAttribute("contractMoneyTypes")).size() > 2) {%>
											<a href="#details" onclick="return saveAndForward('addMoneyType');">Add Money Type</a>
										<% } else { %>
											<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
										<% } %>
										</td>
</c:if>
									<c:if test="${typeIndex.index != 0}">
										<td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
										<td align="right" width="85">
											&nbsp;
										</td>
</c:if>
</c:forEach>
<c:if test="${editContributionDetailsForm.showLoans ==true}">
<c:forEach items="${editContributionDetailsForm.loanColumnsMap}" var="loanColumns" varStatus="loanIndex" >
									<c:if test="${loanIndex.index == 0}">
											<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
											<td align="right" width="85" colspan="3">
<c:if test="${contract.status !='CA'}">
													<% if (editContributionDetailsForm.getLoanColumnsMap().size() < 11) { %>
														<%if (reportBean.getDetails() != null && reportBean.getDetails().size() > 0) {%>
															<a href="#details" onclick="return saveAndForward('addLoan');">Add Loan</a>
														<% } else { %>
															<img src="/assets/unmanaged/images/s.gif" width="1" height="1">
														<% } %>
													<% } %>
</c:if>
											</td>
</c:if>
										<c:if test="${loanIndex.index != 0}">
											<td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
											<td align="right" width="85" colspan="3">
												&nbsp;
											</td>
</c:if>
</c:forEach>
</c:if>
						</tr>
						<%-- End Header Row Three --%>

						<%-- Start Header Row Four --%>
						<tr class="tablesubhead">
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>            		  																																							
							<td align="center" width="35">
								<b>Delete</b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td align="center" width="35">
								<b><report:sortLinkViaSubmit field="recordNumber" direction="asc" anchor="participantTable" formName="editContributionDetailsForm">#</report:sortLinkViaSubmit></b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="200">
<% } else { %>							
							<td colspan="3" width="286">
<% } %>							
								<b><report:sortLinkViaSubmit field="name" direction="asc" anchor="participantTable" formName="editContributionDetailsForm">Name</report:sortLinkViaSubmit></b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>            		  																																								
							<td width="85">
								<b>
									<report:sortLinkViaSubmit field="identifier" direction="asc" anchor="participantTable" formName="editContributionDetailsForm">
<c:if test="${submissionItem.participantSortOption !='EE'}">
											SSN
</c:if>
<c:if test="${submissionItem.participantSortOption =='EE'}">
											Employee #
</c:if>
									</report:sortLinkViaSubmit>
								</b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td align="right" width="85"><b>Participant totals ($)</b></td>
<% } else { %>
							<td align="right" width="85"><b>Submission Totals ($)</b></td>							
<% } %>							
                            <% java.util.List mtcs = editContributionDetailsForm.getMoneyTypeColumnsMap();%>
<c:forEach items="${editContributionDetailsForm.getMoneyTypeColumnsMap()}" varStatus="typeIndex" >
				
<c:set var="typeIndexValue" value="${typeIndex.index}"/>
<% 				
				    String temp = pageContext.getAttribute("typeIndexValue").toString();%>
												<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
								<td align="right" width="85">
									<%int pageCount=reportBean.getTotalPageCount();%>
									<form:select  path="moneyTypeColumnsMap[${typeIndex.index}]"   id='<%="moneyTypeHeader"+Integer.parseInt(temp)%>' onchange="javascript:changingMoneyTypes(this,'moneyTypeColumnsMap[${typeIndex.index}]',${reportBean.getTotalPageCount()});populateParticipantTotals();">
									<form:options  items="${contractMoneyTypes}" itemValue="value"  itemLabel="label"/>
								</form:select>
								</td>
</c:forEach>
<c:if test="${editContributionDetailsForm.showLoans ==true}">
<c:forEach items="${editContributionDetailsForm.loanColumnsMap}" var="loanColumns" varStatus="colIndex" >
									  <td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
									  <td align="center" width="85"><strong>Loan ID</strong></td>
									  <td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
									  <td align="right" width="85"><strong>Loan Amount</strong></td>
</c:forEach>
</c:if>
							</tr>
						<%-- End Header Row Four --%>

<c:set var="lastRowStyle" value="${datacell2}" scope="request" />
					<%if (reportBean.getDetails() != null && reportBean.getDetails().size() > 0) {%>

						<%-- Start detail row iteration --%>
<c:forEach items="${reportBean.details}" var="participant" varStatus="partIndex" >
<c:set var="indexValue" value="${partIndex.index}"/> 
<% int partIndex =(Integer)pageContext.getAttribute("indexValue");%>
<c:set var="participantValue" value="${participant}" />				<% 				
				    String temp = pageContext.getAttribute("indexValue").toString();
				    %>
				 <% if (Integer.parseInt(temp) % 2 == 0) { %>
						<tr class="datacell1">
<c:set var="lastRowStyle" value="${datacell2}" scope="request" />
					<% } else { %>
						<tr class="datacell2">
<c:set var="lastRowStyle" value="${datacell1}" scope="request" />
					<% } %>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>            		  																																												
							<td align="center" nowrap width="35">
								<span class="content">
								<%-- TODO CHANGE THE VALUE IN THE CHECKBOX TO true --%>
<form:checkbox path="deleteBoxesMap[${indexValue}]" onclick="updateParticipantCountForDelete(this, ${indexValue })" value="true" /><%-- name="form" --%>
									<ps:trackChanges name="editContributionDetailsForm" property='<%= "deleteBoxesMap[" + temp + "]" %>'/>
								</span>
							</td>
							<td class="datadivider" width="1"></td>
							<td align="center" nowrap width="35">
							    <% int recordNumber = ((SubmissionParticipant)pageContext.getAttribute("participantValue")).getRecordNumber();
							       if (recordNumber < 0) recordNumber = recordNumber * -1; %>
							<%-- 	<report:errorIcon errors="<%=reportBean%>" codes="IF,RE,ME,MI,IP,SR,LB" row="<%=String.valueOf(recordNumber)%>" identifier="<%=((SubmissionParticipant)pageContext.getAttribute("participantValue")).getIdentifier()%>"/> --%>
								<span class="content">
									<%=recordNumber%>
								</span>
							</td>
							<td class="datadivider" width="1"></td>
							<td align="left" nowrap width="200">
<% } else { %>							
							<td align="left" colspan="3" nowrap width="286">
<% } %>
								<span class="content">
${participant.name}
								</span>
							</td>
							<td class="datadivider" width="1"></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>            		  																																																			
							<td align="left" nowrap width="85">
								<span class="content">
								<%-- 	<report:errorIcon errors="<%=reportBean%>" codes="PI" row="<%=String.valueOf(((SubmissionParticipant)pageContext.getAttribute("participantValue")).getRecordNumber())%>"/> --%>
<c:if test="${submissionItem.participantSortOption !='EE'}">
										<render:ssn property="participant.identifier" useMask="false"/>
</c:if>
<c:if test="${submissionItem.participantSortOption =='EE'}">
${participant.identifier}
</c:if>
<input type="hidden" name="reportBean.employerIds[${partIndex.index}]" />
<input type="hidden" name="reportBean.recordNumbers[${partIndex.index}]" />
								</span>
							</td>
							<td class="datadivider" width="1"></td>
<% } %>							
							<td align="right" nowrap width="85">
<span class="content" id="participantFieldsRowTotal${partIndex.index}"><render:number property="participant.participantTotal" defaultValue = "0.00" type="c"/></span>
							</td>
							

							
<c:forEach items="${submissionItem.allocationMoneyTypes}" var="contributionColumns" varStatus="colIndex" >
<!-- type="com.manulife.pension.ps.service.submission.valueobject.MoneyTypeHeader" -->
<c:set var="colIndex1" value="${colIndex.index}" />
<%

int colIndex =(Integer)pageContext.getAttribute("colIndex1");
%>
								<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
								<td align="right" nowrap width="100">
									<c:set var="contribute" value="${editContributionDetailsForm.getContributionColumns(colIndex.index)}"/>
 
  $&nbsp;<form:input maxlength="15" cssStyle="{text-align: right}" size="11"  path="contributionColumnsMap[${colIndex.index}].row[${partIndex.index}]"
 onblur="validateParticipantInstructionsInput(this)" onfocus="this.select()" /> 
									<ps:trackChanges name="editContributionDetailsForm" property='<%= "contributionColumnsMap[" + colIndex + "]" + ".row[" + partIndex + "]" %>'/>
								</td>
</c:forEach>
<c:if test="${editContributionDetailsForm.showLoans ==true}">
<c:forEach items="${editContributionDetailsForm.loanColumnsMap}" var="loanColumns" varStatus="colIndex" >
									<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
									<td align="center" width="85">
<c:set var="colIndex1" value="${colIndex.index}" />
<%

int colIndex =(Integer)pageContext.getAttribute("colIndex1");
%>





<c:if test="${editContributionDetailsForm.loanColumnsMap[colIndex.index].rowId[ partIndex.index] !=0}">
<c:if test="${editContributionDetailsForm.loanColumnsMap[colIndex.index].rowId[ partIndex.index] !=99}">
${editContributionDetailsForm.loanColumnsMap[colIndex.index].rowId[ partIndex.index]}
</c:if>
</c:if>
<form:hidden path="loanColumnsMap[${colIndex.index}].rowId[${ partIndex.index}]" /> 

									</td>
									<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
									<td align="right" nowrap width="85">
$&nbsp;<form:input maxlength="13" cssStyle="{text-align: right}" size="9" path="loanColumnsMap[${colIndex.index}].row[${ partIndex.index}]" onblur="validateParticipantInstructionsInput(this)" onfocus="this.select()"/>								
										<ps:trackChanges name="editContributionDetailsForm" property='<%= "loanColumnsMap[" + colIndex + "]" + ".row[" + partIndex + "]" %>'/>
									</td>
</c:forEach>
</c:if>
						</tr>
</c:forEach>
					<% } else { %>
						<tr>
							<td colspan="7" align="center">Please add a participcant using the link above.</td>
						</tr>
					<% } %>
						<%-- End detail row iteration --%>

				  </table>
			  </td>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>

<tr class="${lastRowStyle}">
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td class="lastrow" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>
<tr class="${lastRowStyle}">
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4" /></td>
			  <td class="lastrow" colspan="1" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td colspan="2" rowspan="2" align="right" valign="bottom" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" /></td>
			</tr>
			<tr class="whiteborder">
			  <td class="databorder" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>
			<tr>
			  <td colspan="4" align="right">
			  	<report:pageCounterViaSubmit formName="editContributionDetailsForm"  report="reportBean" arrowColor="black"/>
			  </td>
			</tr>
		</table>

		<%
		// remove the copyids (used to generate warning icons for rows with removed participant/loan combos) from session as they are no longer needed
		if ( session != null ) {
			session.removeAttribute(Constants.COPY_IDS);
		}
		%>
	<%-- END new contribution details table --%>
