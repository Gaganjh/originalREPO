<%@ taglib uri="/WEB-INF/bd-report.tld" prefix="report"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/render.tld" prefix="render"%>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content"%>
<%@ taglib tagdir="/WEB-INF/tags/navigation" prefix="navigation"%>
<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout"%>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>
<%@ page import="com.manulife.pension.bd.web.bob.BobContext"%>
<%@ page import="com.manulife.pension.bd.web.BDConstants"%>
<%@ page import="com.manulife.pension.bd.web.content.BDContentConstants"%>
<%@ page import="com.manulife.pension.util.content.GenericException"%>
<%@ page import="com.manulife.pension.bd.web.BDErrorCodes"%>
<%@ page import="com.manulife.pension.bd.web.util.BDSessionHelper"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.manulife.pension.bd.web.BDPdfConstants"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>

<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsRebalReportData" %> 
<%@ page import="com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFund"%>
<c:set var="layoutPageBean" value="${layoutBean.layoutPageBean}" scope="request" />


<input type="hidden" name="pdfCapped" /><%--  input - name="transactionDetailsRebalForm" --%>

<%-- Beans used --%>

<jsp:useBean id="transactionDetailsRebalForm" scope="session" type="com.manulife.pension.bd.web.bob.transaction.TransactionDetailsRebalForm" />

<%
	BobContext bobContext = (BobContext)session.getAttribute(BDConstants.BOBCONTEXT_KEY);
	pageContext.setAttribute("bobContext",bobContext,PageContext.PAGE_SCOPE);

	TransactionDetailsRebalReportData theReport = (TransactionDetailsRebalReportData)request.getAttribute(BDConstants.REPORT_BEAN);
	pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 



<content:contentBean
    contentId="<%=BDContentConstants.REBALANCE_DETAILS_EXPAND%>"
    type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="showDetailMsg" />
<content:contentBean
    contentId="<%=BDContentConstants.REBALANCE_DETAILS_COLLAPSE%>"
    type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="hideDetailMsg" />

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_EXCEL_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="csvIcon"/>

<content:contentBean contentId="<%=BDContentConstants.MISCELLANEOUS_PDF_IMAGE_TEXT%>"
        type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>"
        id="pdfIcon"/>

<c:if test="${not empty theReport}">




    <div id="summaryBox">
    <h1><content:getAttribute id="layoutPageBean" attribute="subHeader" /></h1>
    <br />
    Transaction Type:<strong> Inter-account transfer - Rebalance</strong> <br />
    <% if (bobContext.getCurrentContract().isDefinedBenefitContract()==false) { %>
	Name: <strong>${theReport.participantName}<br />

    <%
    }
    %> <%
     if (bobContext.getCurrentContract().isDefinedBenefitContract() == false) {
     %>
</strong>SSN: <strong>${theReport.participantSSN}</strong><br />

    <%
    }
    %> <br />
    Invested Date: <strong><render:date dateStyle="m"
        property="theReport.transactionDate" /><br />
    </strong> Request Date: <strong><render:date dateStyle="m"
        property="theReport.requestDate" /></strong><br />
    <br />
Transaction Number: <strong>${e:forHtmlContent(theReport.transactionNumber)}</strong><br />

Submission Method: <strong>${theReport.mediaCode}</strong></div>

</c:if>    
    <jsp:include page="/WEB-INF/global/displayContractInfo.jsp" />
    
    <report:formatMessages scope="request" />

    <navigation:contractReportsTab />


<c:if test="${not empty theReport}">




    <div class="page_section_subheader controls">
    
    <h3><content:getAttribute id="layoutPageBean"
        attribute="body1Header" /></h3>
    <c:if test="${empty requestScope.isError}">
     <a href="javascript://" onClick="doPrintPDF()"  class="pdf_icon"  title="<content:getAttribute beanName="pdfIcon" attribute="text"/>"> <content:image contentfile="image" id="pdfIcon" /> </a>
     <a href="javascript://" onClick="doDownloadCSV();return false;" class="csv_icon"  title="<content:getAttribute beanName="csvIcon"  attribute="text"/>"> <content:image contentfile="image" id="csvIcon" /></a>
    </c:if>
    </div>
    <div class="report_table">
    <table class="report_table_content">
        <thead>
            <tr>
                <th width="52%" class="val_str">Investment
                Option</th>
                <%
                if (bobContext.getCurrentContract().isDefinedBenefitContract() == false) {
                %>
                <th class="val_str align_center">Employee Assets ($) </th>
                <th class="val_str align_center">% Of Account </th>
                <%
                }
                %>
                <th class="val_str align_center">Employer Assets ($) </th>
                <th class="val_str align_center">% Of Account </th>
            </tr>
        </thead>
        <tbody>
<c:forEach items="${theReport.beforeChange}" var="category" >


                <tr class="spec">
<td width="52%" class="name"><b>${category.groupName}</b></td>

                    <%
                    if (bobContext.getCurrentContract().isDefinedBenefitContract() == false) {
                    %>
                    <td width="12%" class="name">&nbsp;</td>
                    <td width="12%" class="name">&nbsp;</td>
                    <%
                    }
                    %>
                    <td width="12%" class="name">&nbsp;</td>
                    <td width="12%" class="name">&nbsp;</td>
                </tr>
<c:forEach items="${category.funds}" var="fund" varStatus="fundIndex" >

<c:set var="fundIndex" value="${fundIndex.index}"/> 
<%String temp = pageContext.getAttribute("fundIndex").toString();%>
<% if (Integer.parseInt(temp) % 2 == 0) { %> 
                            <tr>
                        <% } else { %>
                            <tr class="spec">
                        <% } %>
<td width="52%" class="name">${fund.name}</td>

    
                        <%
                                    if (bobContext.getCurrentContract()
                                    .isDefinedBenefitContract() == false) {
                        %>
                        <%
                        if (theReport.getTotalEEBeforeAmount().doubleValue() != (double) 0.0) {
                        %>
                        <td width="12%" class="cur"><report:number
                            property="fund.employeeAmount" /></td>
                        <td width="12%" class="cur"><report:number
                            property="fund.employeePercentage" /></td>
                        <%
                        } else {
                        %>
                        <td width="12%" class="cur">-</td>
                        <td width="12%" class="cur">-</td>
                        <%
                        }
                        %>
                        <%
                        }
                        %>
                        <%
                        if (theReport.getTotalERBeforeAmount().doubleValue() != (double) 0.0) {
                        %>
                        <td width="12%" class="cur"><report:number
                            property="fund.employerAmount" /></td>
                        <td width="12%" class="cur"><report:number
                            property="fund.employerPercentage" /></td>
                        <%
                        } else {
                        %>
                        <td width="12%" class="cur">-</td>
                        <td align=width= "12%" class="cur">-</td>
                        <%
                        }
                        %>
    
                    </tr>
</c:forEach>
</c:forEach>
            <tr class="spec">
                <td width="52%" class="name">
                <div align="right"><b>Total Amount:</b></div>
                </td>
                <%
                if (bobContext.getCurrentContract().isDefinedBenefitContract() == false) {
                %>
                <%
                if (theReport.getTotalEEBeforeAmount().doubleValue() != (double) 0.0) {
                %>
                <td width="12%" class="cur"><strong><report:number
                    property="theReport.totalEEBeforeAmount" /></strong></td>
                <td width="12%" class="cur"><strong><report:number
                    property="theReport.totalEEBeforePct" /></strong></td>
                <%
                } else {
                %>
                <td width="12%" class="cur">-</td>
                <td width="12%" class="cur">-</td>
                <%
                }
                %>
                <%
                }
                %>
    
                <%
                if (theReport.getTotalERBeforeAmount().doubleValue() != (double) 0.0) {
                %>
                <td width="12%" class="cur"><b><report:number
                    property="theReport.totalERBeforeAmount" /></b></td>
                <td width="12%" class="cur"><b><report:number
                    property="theReport.totalERBeforePct" /></b></td>
                <%
                } else {
                %>
                <td width="12%" class="cur">-</td>
                <td width="12%" class="cur">-</td>
                <%
                }
                %>
            </tr>
        </tbody>
    </table>
    </div><%-- end of Account Before Rebalance report table--%>
    <div class="page_section_subheader controls">
    <h3><content:getAttribute id="layoutPageBean"
        attribute="body2Header" /></h3>
    </div>
    <div class="report_table">
    <table class="report_table_content">
        <thead>
            <tr>
                <th width="52%" class="val_str">Investment
                Option</th>
                <%
                if (bobContext.getCurrentContract().isDefinedBenefitContract() == false) {
                %>
                <th width="12%" class="val_str align_center">Employee
                Assets ($) </th>
                <th width="12%" class="val_str align_center">% Of Account </th>
                <%
                }
                %>
                <th width="12%" class="val_str align_center">Employer
                Assets ($)</th>
                <th width="12%" class="val_str align_center">% Of Account </th>
            </tr>
        </thead>
        <tbody>
<c:forEach items="${theReport.afterChange}" var="category" >


                <tr class="spec">
<td width="52%" class="name"><b>${category.groupName}</b></td>

                    <%
                    if (bobContext.getCurrentContract().isDefinedBenefitContract() == false) {
                    %>
                    <td width="12%" class="name">&nbsp;</td>
                    <td width="12%" class="name">&nbsp;</td>
                    <%
                    }
                    %>
                    <td width="12%" class="name">&nbsp;</td>
                    <td width="12%" class="name">&nbsp;</td>
                </tr>
<c:forEach items="${category.funds}" var="fund" varStatus="fundIndex" >

<c:set var="fundIndex" value="${fundIndex.index}"/> 
<%String temp = pageContext.getAttribute("fundIndex").toString();%>
                       <% if (Integer.parseInt(temp) % 2 == 0) { %>
                            <tr>
                        <% } else { %>
                            <tr class="spec">
                        <% } %>
<td width="52%" class="name">${fund.name}</td>

    
                        <%
                                    if (bobContext.getCurrentContract()
                                    .isDefinedBenefitContract() == false) {
                        %>
                        <%
                        if (theReport.getTotalEEAfterAmount().doubleValue() != (double) 0.0) {
                        %>
                        <td width="12%" class="cur"><report:number
                            property="fund.employeeAmount" /></td>
                        <td width="12%" class="cur"><report:number
                            property="fund.employeePercentage" /></td>
                        <%
                        } else {
                        %>
                        <td width="12%" class="cur">-</td>
                        <td width="12%" class="cur">-</td>
                        <%
                        }
                        %>
                        <%
                        }
                        %>
    
    
                        <%
                        if (theReport.getTotalERAfterAmount().doubleValue() != (double) 0.0) {
                        %>
                        <td width="12%" class="cur"><report:number
                            property="fund.employerAmount" /></td>
                        <td width="12%" class="cur"><report:number
                            property="fund.employerPercentage" /></td>
                        <%
                        } else {
                        %>
                        <td width="12%" class="cur">-</td>
                        <td width="12%" class="cur">-</td>
                        <%
                        }
                        %>
                    </tr>
</c:forEach>
</c:forEach>
            <tr>
                <td width="52%" class="name">
                <div align="right"><b>Total Amount:</b></div>
                </td>
                <%
                if (bobContext.getCurrentContract().isDefinedBenefitContract() == false) {
                %>
                <%
                if (theReport.getTotalEEAfterAmount().doubleValue() != (double) 0.0) {
                %>
                <td width="12%" class="cur"><b><report:number
                    property="theReport.totalEEAfterAmount" /></b></td>
                <td width="12%" class="cur"><b><report:number
                    property="theReport.totalEEAfterPct" /></b></td>
                <%
                } else {
                %>
                <td width="12%" class="cur">-</td>
                <td width="12%" class="cur">-</td>
                <%
                }
                %>
    
                <%
                }
                %>
                <%
                if (theReport.getTotalERAfterAmount().doubleValue() != (double) 0.0) {
                %>
                <td width="12%" class="cur"><b><report:number
                    property="theReport.totalERAfterAmount" /></b></td>
                <td width="12%" class="cur"><b><report:number
                    property="theReport.totalERAfterPct" /></b></td>
                <%
                } else {
                %>
                <td width="12%" class="cur">-</td>
                <td width="12%" class="cur">-</td>
                <%
                }
                %>
            </tr>
    
        </tbody>
    </table>
    </div><%--end of Account After Rebalance report table--%>
        <%
            Boolean showMessage = false;
            if ((theReport.getRedemptionFees().doubleValue() != 0) || 
            (theReport.getMva().doubleValue() > (double)0.0)) {
                showMessage = true;
            }
    
            if (showMessage) {
        %>
            <div class="message message_info">
                    <dl>
                        <dt>Information Message</dt>
        <%  } %>
        <%
            int messageCount = 0;
            
            if (theReport.getRedemptionFees().doubleValue() > (double)0.0) {%>
                <dd><%=++messageCount%> .&nbsp;&nbsp;
                <content:contentBean contentId="<%=BDContentConstants.MESSAGE_REDEMPTION_FEE_APPLED%>"
                                           type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="redemptionMsg"/>
                <content:getAttribute beanName="redemptionMsg" attribute="text">
                    <content:param>
                        -<report:number property="theReport.redemptionFees" type="c" sign="true"/>
                    </content:param>
                </content:getAttribute>
                </dd>
        <%  } %>
    
        <%  if (theReport.getRedemptionFees().doubleValue() != 0) {%>
                <dd><%=++messageCount%> &nbsp;&nbsp;
                <a href="#" onClick="javascript:PDFWindow('<%=BDConstants.REDEMPTION_FEE_PDF_URL%>')">View underlying mutual fund redemption fee information</a></dd>
        <%  } %>
                
    
        <%  if (theReport.getMva().doubleValue() > (double)0.0) { %>
                <dd><%=++messageCount%> &nbsp;&nbsp;
                <content:contentBean contentId="<%=BDContentConstants.MESSAGE_MVA_APPLIED%>"
                                              type="<%=BDContentConstants.TYPE_MISCELLANEOUS%>" id="mvaMsg"/>
                <content:getAttribute beanName="mvaMsg" attribute="text">
                    <content:param>
                        -<report:number property="theReport.mva" type="c" sign="true"/>
                    </content:param>
                </content:getAttribute>
                </dd>
        <%  } %>    
        
        <%  if (showMessage) { %>
                    </dl>
                </div> <!--.message_info-->
        <%  } %>
    
    
    
    <div class="page_section_subheader controls">
    <h3><content:getAttribute id="layoutPageBean"
        attribute="body3Header" /></h3>
    </div>
    <%
                boolean showComments = ((theReport.getMva().doubleValue() != (double) 0.0) || (theReport
                .getRedemptionFees().doubleValue() != (double) 0.0));
    %>
    <div class="report_table">
    <table class="report_table_content">
        <thead>
            <tr>
                <th width="30%" class="val_str">Investment
                Option</th>
                <th width="30%" class="val_str">Money Type </th>
                <th width="15%" class="val_str align_center">Amount ($) </th>
                <th width="12%" class="val_str align_center">Unit Value </th>
                <th width="18%" class="val_str align_center">Number Of Units</th>
                <%
                if (showComments) {
                %>
                <th width="12%" class="val_str">Comments</th>
                <%
                }
                %>
            </tr>
        </thead>
        <tbody>
<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="category" >


                    <tr class="spec">
<td width="30%" class="name"><B>${category.groupName}</B></td>

                        <td width="30%" class="name">&nbsp;</td>
                        <td width="15%" class="name">&nbsp;</td>
                        <td width="12%" class="name">&nbsp;</td>
                        <td width="18%" class="name">&nbsp;</td>
                        <%
                        if (showComments) {
                        %>
                        <td width="12%" class="name">&nbsp;</td>
                        <%
                        }
                        %>
                        <!-- NOTE: This may not be best solution. sub removed because tr th not detecting first child...-->
                    </tr>
<c:forEach items="${category.funds}" var="fund" varStatus="fundIndex" >

<c:set var="fundIndex" value="${fundIndex.index}"/> 
<%String temp = pageContext.getAttribute("fundIndex").toString();%>
                       <% if (Integer.parseInt(temp) % 2 == 0) { %>
                                <tr>
                            <% } else { %>
                                <tr class="spec">
                            <% } %>
<td width="30%" class="name">${fund.name}</td>

                            <td width="30%" class="name">
                            
<c:if test="${not empty fund.comments}">
                            
</c:if>
                            
                            
${fund.moneyTypeDescription}</td>
<%
TransactionDetailsFund fund = (TransactionDetailsFund)pageContext.getAttribute("fund");
%>
                            <td width="10%" class="cur pct "><report:number
                                property="fund.amount" type="c" sign="false" /></td>
                            <td width="12%" class="cur">
                            
                            <%
                            if (fund.displayUnitValue()) {
                            %> <report:number
                                property="fund.displayUnitValue" scale="2" sign="true" />
                            <%
                                        // If has no units, means we have interest rate here
                                        if (!fund.displayNumberOfUnits()) {
                            %>&#37; <%
            }
            }
     %>
                            </td>
                            <td width="18%" class="cur pct ">
                            <%
                            if (fund.displayNumberOfUnits()) {
                            %> <report:number
                                property="fund.displayNumberOfUnits" scale="6" sign="false" /> <%
     }else{
     %> - <% } %>
                            </td>
                            <%
                            if (showComments) {
                            %>
<td width="12%" class="name">${fund.comments}</td>

                            <%
                            }
                            %>
                        </tr>
</c:forEach>
</c:forEach>
</c:if>
        </tbody>
    </table>
</c:if>

</div><%--end of Rebalance Details report table--%>

<!--.report_table-->

<layout:pageFooter/>
