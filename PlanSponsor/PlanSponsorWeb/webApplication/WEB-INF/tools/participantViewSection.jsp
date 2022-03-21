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
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.manulife.pension.platform.web.taglib.util.LabelValueBean" %>
<%@ page import="com.manulife.pension.service.contract.valueobject.MoneyTypeVO" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.SubmissionParticipant" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.MoneyTypeHeader" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData" %>
<%@ page import="com.manulife.pension.ps.service.submission.valueobject.ContributionDetailItem" %>
<c:set var="lastRowStyle" value="" />

<%boolean isIE = request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") != -1;%>
<jsp:useBean id="viewContributionDetailsForm" scope="session" type="com.manulife.pension.ps.web.tools.ViewContributionDetailsForm" />

<%
ContributionDetailsReportData theReport = (ContributionDetailsReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
ContributionDetailItem submissionItem = theReport.getContributionData();
pageContext.setAttribute("submissionItem",submissionItem,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<content:contentBean contentId="<%=ContentConstants.MISCELLANEOUS_NO_PARTICIPANT_PROCESSED%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageNoPptProcessed"/>

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
                    	<td class="tableheadTDinfo">&nbsp;</td>
						<td align="right" class="tableheadTDinfo">
                            <report:pageCounter report="theReport" formName="viewContributionDetailsForm"/>
                        </td>
<% } else { %>					  
						<td class="tableheadTD"><b><content:getAttribute beanName="layoutPageBean" attribute="body3Header"/></b>
					<%if (theReport.getDetails() != null && theReport.getDetails().size() > 0) {%>
						<b><report:recordCounter report="theReport" label=" - Total records "/></b>
						<td class="tableheadTDinfo">&nbsp;</td>
						<td align="right" class="tableheadTDinfo">
                            <report:pageCounter report="theReport" formName="viewContributionDetailsForm"/>
                        </td>
                    <% } else { %>
                    	<td class="tableheadTDinfo">&nbsp;</td>
                     	<td class="tableheadTDinfo">&nbsp;</td>
                     <% } %>
<% } %>                     
            		  </tr>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>            		  
					  <c:if test="${empty param.printFriendly }" >
					  <tr>
						<td colspan="3" align="right" class="tableheadTDinfo">
             				<%if (theReport.getDetails() != null && theReport.getDetails().size() > 0) {%>
			               		<ps:setReportPageSize/>
                    		<% } else { %>
                    			&nbsp;
                    		<% } %>
                        </td>
					  </tr>
					  </c:if>
<% } %>                     					  
					</table>
				</td>
			</tr>

			<tr class="datacell1">
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td colspan="2" class="whiteborder">
				<table witdh="100%" border="0" cellpadding="0" cellspacing="0">

<c:if test="${submissionItem.zeroContributionFile !=true}">
					<%if (theReport.getDetails() != null && theReport.getDetails().size() > 0) {%>

						<%-- Start Header Row One --%>
						<tr class="datacell1">
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>            		  																		
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
<td align="right" width="85" title="${moneyType.contractLongName}">
								<%
									Collection contractMoneyTypes = viewContributionDetailsForm.getContractMoneyTypes();
									boolean hasMatch = false;
									for (Iterator it = contractMoneyTypes.iterator(); it.hasNext() && ! hasMatch; ) {
										LabelValueBean bean = (LabelValueBean)it.next();
										if (((MoneyTypeVO)pageContext.getAttribute("moneyType")).getContractShortName().equals(bean.getLabel())) {
											hasMatch = true;
										}
									}

									if (!hasMatch) {
									%>
										<report:errorIcon errors="<%=theReport%>" codes="AT,MT,IM,MN"/>
									<%
									}
									%> 
<b title="${moneyType.contractLongName}">${moneyType.contractShortName}</b>
								</td>
</c:forEach>
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
						</tr>
						<%-- End Header Row One --%>


						<%-- Start Header Row Two --%>
						<% Map allocationTotalValues = submissionItem.getAllocationTotalValues(); %>
						<tr class="datacell2">
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>            		  												
							<td class="datacell1" width="35">&nbsp;</td>
							<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
<% } %>							
							<td class="datacell1" width="200">&nbsp;</td>
							<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="85"><b>Totals</b></td>
							<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td align="right" width="85"><b><span class="highlight"><render:number property="submissionItem.participantTotal" type="c"/></span></b></td>
<c:forEach items="${submissionItem.allocationMoneyTypes}" var="moneyType" >
<c:set var="moneyTypeValue" value="${moneyType}"/>
<% 
MoneyTypeHeader moneyTypeHeader = (MoneyTypeHeader)pageContext.getAttribute("moneyTypeValue");%>
								<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
								<td align="right" width="85">
									<b><span class="highlight">
									<%
										java.math.BigDecimal typeTotal = (java.math.BigDecimal) allocationTotalValues.get(moneyTypeHeader.getKey());
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
<c:forEach items="${submissionItem.loanTotalValues}" var="loanTotal" >
								  <td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
								  <td align="right" width="85">&nbsp;</td>
								  <td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
								  <td align="right" width="85"><b><span class="highlight"><render:number property="loanTotal" type="c"/></span></b></td>
</c:forEach>
						</tr>
						<%-- End Header Row Two --%>


						<%-- Start Header Row Three --%>
						<tr class="tablesubhead">
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>            		  						
							<td align="center" width="35">
								<b><report:sort field="recordNumber" direction="asc" formName="viewContributionDetailsForm">#</report:sort></b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td width="200">
<% } else { %>
							<td colspan=3" width="286">
<% } %>							
								<b><report:sort field="name" direction="asc" formName="viewContributionDetailsForm">Name</report:sort></b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>            		  							
							<td width="85">
								<b>
									<report:sort field="identifier" direction="asc" formName="viewContributionDetailsForm">
<c:if test="${submissionItem.participantSortOption != 'EE'}">
											SSN
</c:if>
<c:if test="${submissionItem.participantSortOption == 'EE'}">
											Employee #
</c:if>
									</report:sort>
								</b>
							</td>
							<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
							<td align="right" width="85"><b>Participant totals ($)</b></td>
<% } else { %>														
							<td align="right" width="85"><b>Submission Totals ($)</b></td>							
<% } %>
<c:forEach items="${submissionItem.allocationMoneyTypes}" var="moneyTypeHeader">
								<td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
<c:set var="moneyType" value="${moneyTypeHeader.moneyType}"/>
<td align="right" width="85"><b title="${moneyType.contractLongName}">${moneyType.contractShortName}</b></td>
</c:forEach>
							<%
								for (int i = 0; i < submissionItem.getMaximumNumberOfLoans(); i++)
								{
							%>
								  <td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
								  <td align="center" width="85"><strong>Loan ID</strong></td>
								  <td class="dataheaddivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
								  <td align="right" width="85"><strong>Loan Amount</strong></td>
							<%
								}
							%>
						</tr>
						<%-- End Header Row Three --%>
					<%}%>

					<%if (theReport.getDetails() != null && theReport.getDetails().size() > 0) {%>

						<%-- Start detail row iteration --%>
<c:forEach items="${theReport.details}" var="participant" varStatus="partIndex" >
<c:set var="indexValue" value="${partIndex.index}"/>
<c:set var ="participantValue"value="${ participant}"/>
<% 
SubmissionParticipant subParticipant = (SubmissionParticipant)pageContext.getAttribute("participantValue");
				    String temp = pageContext.getAttribute("indexValue").toString();
				  if (Integer.parseInt(temp) % 2 == 0) { %>
			
						<tr class="datacell1">
					<% pageContext.setAttribute("lastRowStyle", "datacell2");
					} else { %>
						<tr class="datacell2">
					<% pageContext.setAttribute("lastRowStyle", "datacell1");
					} %>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>            		  					
							<td align="center" nowrap width="35">
							
								<% 
								int recordNumber = subParticipant.getRecordNumber();
								   if (recordNumber < 0) {
										recordNumber = recordNumber * -1;
									}%>
								<report:errorIcon errors="<%=theReport%>" codes="IF,RE,ME,MI,IP,SR,LB" row="<%=String.valueOf(recordNumber)%>"/>
								<span class="content">
									<%=recordNumber%>
								</span>
							</td>
							<td class="datadivider" width="1"></td>
							<td align="left" nowrap width="200">
<% } else { %>
							<td colspan="3" align="left" nowrap width="286">
<% } %>							
								<span class="content">
${participant.name}
								</span>
							</td>
							<td class="datadivider" width="1"></td>
<% if (userProfile.getCurrentContract().isDefinedBenefitContract()==false) { %>            		  							
							<td align="left" nowrap width="85">
								<span class="content">
									<report:errorIcon errors="<%=theReport%>" codes="PI" row="<%=String.valueOf(subParticipant.getRecordNumber())%>"/>
<c:if test="${submissionItem.participantSortOption != 'EE'}">
										<render:ssn property="participant.identifier" useMask="true"/>
</c:if>
<c:if test="${submissionItem.participantSortOption == 'EE'}">
${participant.identifier}
</c:if>
								</span>
							</td>
							<td class="datadivider" width="1"></td>
<% } %>							
							<td align="right" nowrap width="85">
								<span class="content"><render:number property="participant.participantTotal" defaultValue = "0.00" type="c" sign="false"/></span>
							</td>
<c:forEach items="${submissionItem.allocationMoneyTypes}" var="moneyTypeHeader" >
<c:set var ="moneyTypeHeaderValue"value="${moneyTypeHeader}"/>
<% 
MoneyTypeHeader moneyTypeHeader = (MoneyTypeHeader)pageContext.getAttribute("moneyTypeHeaderValue");%>
								<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
								<td align="right" nowrap width="85">
									<%
										java.math.BigDecimal amount = (java.math.BigDecimal)subParticipant.getMoneyTypeAmounts().get(moneyTypeHeader.getKey());
										if(amount!=null && !(amount.compareTo(new BigDecimal("0.00")) == 0))
											pageContext.setAttribute("amount", amount, PageContext.PAGE_SCOPE);
										else
											pageContext.removeAttribute("amount", PageContext.PAGE_SCOPE);
									%>
									<c:if test="${not empty amount}">
										<render:number property="amount" defaultValue="" type="c" sign="false"/>
</c:if>
									<c:if test="${empty amount}">
										&nbsp;
									</c:if>
								</td>
</c:forEach>
							<%
								int i = 0;
								while (i < submissionItem.getMaximumNumberOfLoans())
								{
							%>
<c:forEach items="${participant.loanAmounts}" var="loan" >
									<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
									<td align="center" width="85">
<c:set var="loanKey" value="${loan.key}" /> 

<% 				
				    String temp1 = pageContext.getAttribute("loanKey").toString();
				     %>
				
										<%String loanId = temp1.substring(0,temp1.indexOf("/"));
										  if (loanId.equals("99") || loanId.equals("0")) {
										  	loanId = "";
										  }	%>
										<%= loanId %>
									</td>
									<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
									<td align="right" nowrap width="85">
<c:if test="${loan.value !='0.00'}">
											<render:number property="loan.value" defaultValue="" type="c" sign="false"/>
</c:if>
<c:if test="${loan.value =='0.00'}">
											&nbsp;
</c:if>
									</td>
									<% i++; %>
</c:forEach>
								<%
									while (i < submissionItem.getMaximumNumberOfLoans())
									{
								%>
									<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
									<td align="center" width="85">&nbsp;</td>
									<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
									<td align="right" width="85">&nbsp;</td>
								<%
									i++;
									}
								%>
							<%
								}
							%>
						</tr>
</c:forEach>
					<% } else { %>
						<tr>
							<td colspan="7" align="center">There are no participcants included with this submission.</td>
						</tr>
					<% } %>
</c:if>
<c:if test="${submissionItem.zeroContributionFile ==true}">
					     <tr>
							<td colspan="7" align="center"><content:getAttribute beanName="messageNoPptProcessed" attribute="text"/></td>
						</tr>
</c:if>
					 

						<%-- End detail row iteration --%>

				  </table>
			  </td>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>
			<%if (theReport.getDetails() == null || theReport.getDetails().size() == 0) {
				pageContext.setAttribute("lastRowStyle","datacell2");
			  }%>
<tr class="${lastRowStyle}">
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td class="lastrow" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>
<tr class="${lastRowStyle}">
			  <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4" /></td>
			  <td class="lastrow" colspan="1" width="10000"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			  <td colspan="2" rowspan="2" align="right" valign="bottom" class="lastrow"><img src="/assets/unmanaged/images/box_lr_corner.gif" width="5" height="5" /></td>
			</tr>
			<tr class="whiteborder">
			  <td class="databorder" colspan="2"><img src="/assets/unmanaged/images/s.gif" width="1" height="1" /></td>
			</tr>
			<tr>
			  <td colspan="4" align="right"><report:pageCounter report="theReport" arrowColor="black" formName="viewContributionDetailsForm"/></td>
			</tr>
		</table>
	<%-- END new contribution details table --%>
